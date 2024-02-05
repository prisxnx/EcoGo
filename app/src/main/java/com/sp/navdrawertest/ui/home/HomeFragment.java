package com.sp.navdrawertest.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.sp.navdrawertest.R;
import com.sp.navdrawertest.RvAdapter;
import com.sp.navdrawertest.adminInfo;
import com.sp.navdrawertest.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements RvAdapter.OnItemClickListener {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private RvAdapter rvAdapter;
    private HomeViewModel viewModel;
    private MapView mapView;
    private GoogleMap map;
    private Location currentLocation;
    private LocationManager locationManager;
    private double latitude, longitude;
    private static final int MANUAL_MAP_REQUEST_CODE = 123;
    private boolean manualLocationSelected = false;
    private double manualLatitude, manualLongitude, currentLatitude, currentLongitude;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // Enable features such as zoom, scroll, etc.
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        // ...
    }

    @Override
    public void onCardClick(int position) {

    }

    @Override
    public void onCardClick(adminInfo adminInfo) {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.horizontalRv;

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Observe changes in the data source
        viewModel.getAdminPosts().observe(getViewLifecycleOwner(), new Observer<ArrayList<adminInfo>>() {
            @Override
            public void onChanged(ArrayList<adminInfo> newAdminPosts) {
                updateRecyclerView(newAdminPosts);
            }
        });

        // Fetch data from the Realtime Database
        viewModel.fetchAdminPosts();

        if (rvAdapter == null) {
            rvAdapter = new RvAdapter(new ArrayList<>());
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(rvAdapter);
            rvAdapter.setOnItemClickListener(this);
        } else {
            // If adapter already exists, update the data and notify the adapter
            rvAdapter.setAdminInfoList(new ArrayList<>());
        }
    }

    private void updateRecyclerView(ArrayList<adminInfo> newData) {
        // Update the RecyclerView adapter or dataset
        if (rvAdapter != null) {
            rvAdapter.setAdminInfoList(newData);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRouteDraw(List<List<LatLng>> route) {
        // Handle the result of the Directions API request here
        // For example, you can draw the route on a Google Map:
        if (map != null) {
            PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            for (List<LatLng> leg : route) {
                options.addAll(leg);
            }
            map.addPolyline(options);
        }
    }

    @Override
    public void onItemClick(adminInfo adminInfo) {
        getCurrentLocation();
        LatLng userCurrentLocation = new LatLng(currentLatitude, currentLongitude);

        latitude = Double.parseDouble(adminInfo.getLocationLatitude());
        longitude = Double.parseDouble(adminInfo.getLocationLongitude());
        LatLng clickedItemLocation = new LatLng(latitude, longitude);
        String url = getDirectionsUrl(userCurrentLocation, clickedItemLocation);
        new DrawRouteTask(requireContext()).execute(url);

        View mapViewLayout = LayoutInflater.from(requireContext()).inflate(R.layout.homemapview, (ViewGroup) getView(), false);
        ViewGroup rootView = (ViewGroup) getView();
        if (rootView != null) {
            rootView.removeAllViews();
            rootView.addView(mapViewLayout);
        }

        mapView = mapViewLayout.findViewById(R.id.homemap);
        mapView.onCreate(null);
        mapView.getMapAsync(this::onMapReady);
    }

    private Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Check if the locationManager is not null to avoid potential issues
            if (locationManager != null) {
                // Request location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                // Get the last known location as an initial value
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (lastKnownLocation != null) {
                    // If lastKnownLocation is not null, return it
                    return lastKnownLocation;
                } else {
                    // If lastKnownLocation is null, continue listening for updates in locationListener
                    // Note: You may want to add a timeout mechanism here to handle cases where location updates are not received in a reasonable time.
                }
            }
        } else {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        return null;
    }

    // Create a LocationListener to handle location updates
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                // Get the latitude and longitude
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
            }
        }

        // Implement other LocationListener methods if needed
        @Override
        public void onProviderDisabled(String provider) {
            // Handle provider disabled
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Handle provider enabled
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Handle status changed
        }
    };

    private String getDirectionsUrl(LatLng origin, LatLng destination) {
        // Create a Google Maps Directions API request URL
        String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin.latitude + "," + origin.longitude +
                "&destination=" + destination.latitude + "," + destination.longitude +
                "&key=" + getApiKey();

        return url;
    }

    private String getApiKey() {
        // Replace this method with your own method to get the Google Maps API key
        // For example, you can store the API key in your app's strings.xml file and retrieve it using the following code snippet:

        String apiKey = getString(R.string.google_maps_api_key);

        return apiKey;
    }

    private class DrawRouteTask extends AsyncTask<String, Void, List<List<LatLng>>> {

        private Context context;

        public DrawRouteTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<List<LatLng>> doInBackground(String... urls) {
            // Execute the Google Maps Directions API request and parse the response to get the route between the origin and destination locations
            // You can use the following code snippet to achieve this:

            List<List<LatLng>> routes = null;

            try {
                // Create a Google Maps Directions API request URL
                String url = urls[0];

                // Execute the Google Maps Directions API request and parse the response to get the route between the origin and destination locations
                // You can use the following code snippet to achieve this:

                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Access-Control-Allow-Origin", "*");
                httpURLConnection.setRequestProperty("Access-Control-Allow-Methods", "*");

                // Read the response from the Google Maps Directions API request
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                // Parse the response to get the route between the origin and destination locations
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray routesArray = jsonObject.getJSONArray("routes");
                routes = new ArrayList<>();
                for (int i = 0; i < routesArray.length(); i++) {
                    JSONObject route = routesArray.getJSONObject(i);
                    JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                    String points = overviewPolyline.getString("points");
                    List<LatLng> decodePoly = PolyUtil.decode(points);
                    routes.add(decodePoly);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return routes;
        }
    }

    public interface RouteListener {
        void onRouteDraw(List<List<LatLng>> route);
    }
}
