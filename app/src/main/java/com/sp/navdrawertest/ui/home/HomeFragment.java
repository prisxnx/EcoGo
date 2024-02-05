package com.sp.navdrawertest.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // Enable features such as zoom, scroll, etc.
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        // ...
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
        View mapViewLayout = LayoutInflater.from(requireContext()).inflate(R.layout.homemapview, (ViewGroup) getView(), false);
        ViewGroup rootView = (ViewGroup) getView();
        if (rootView != null) {
            rootView.removeAllViews();
            rootView.addView(mapViewLayout);
        }
        mapView = mapViewLayout.findViewById(R.id.homemapview);
        mapView.onCreate(null);
        mapView.getMapAsync(this::onMapReady);

        Location currentLocation = getCurrentLocation();
        double latitude = Double.parseDouble(String.valueOf(currentLocation.getLatitude()));
        double longitude = Double.parseDouble(String.valueOf(currentLocation.getLongitude()));
        LatLng userCurrentLocation = new LatLng(latitude, longitude);

        latitude = Double.parseDouble(adminInfo.getLocationLatitude());
        longitude = Double.parseDouble(adminInfo.getLocationLongitude());
        LatLng clickedItemLocation = new LatLng(latitude, longitude);
        String url = getDirectionsUrl(userCurrentLocation, clickedItemLocation);
        new DrawRouteTask(requireContext()).execute(url);
    }

    private Location getCurrentLocation() {
        // Get the user's current location using the appropriate method (e.g., using the Fused Location Provider API)
        // You can use the Fused Location Provider API to get the user's current location
        // For example, you can use the following code snippet to get the user's current location:

        Location currentLocation = null;
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                currentLocation = lastKnownLocation;
            }
        }

        return currentLocation;
    }

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