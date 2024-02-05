package com.sp.navdrawertest.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sp.navdrawertest.CustomMapFragment;
import android.Manifest;
import android.widget.Toast;

import com.sp.navdrawertest.MarkerData;
import com.sp.navdrawertest.R;


public class ProfileFragment extends Fragment {

    private TextView placesTextView, journalTextView, savedTextView,profileusername;
    private CardView mapcardview;
    public String currentUser,OpenText;
    private ImageView facebookbutton, linkedinbutton;

    public ProfileFragment(){
    }

    public static ProfileFragment newInstance(String userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("userID", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve arguments and set currentUser
        if (getArguments() != null) {
            currentUser = getArguments().getString("userID");
            Log.d("ProfileFragment", "currentUser: " + currentUser);
            OpenText="Find me on EcoGo! Username:" + currentUser;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        placesTextView = view.findViewById(R.id.placestextview);
        journalTextView = view.findViewById(R.id.journaltextview);
        savedTextView = view.findViewById(R.id.savedtextview);
        profileusername = view.findViewById(R.id.profileusername);
        facebookbutton=view.findViewById(R.id.facebookicon);
        linkedinbutton=view.findViewById(R.id.linkedinicon);

        if (getArguments() != null) {
            currentUser = getArguments().getString("userID");
            Log.d("ProfileFragment", "onCreateView - currentUser: " + currentUser);
            OpenText="Find me on EcoGo! Username:" + currentUser;
        }

        OpenText="Find me on EcoGo! Username:" + currentUser;
        profileusername.setText(currentUser);

        facebookbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sharingToSocialMedia("com.facebook.katana",OpenText);
            }
        });

        linkedinbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sharingToSocialMedia("com.linkedin.katana",OpenText);
            }
        });

        placesTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                savedTextView.setTextColor(Color.parseColor("#545454"));
                journalTextView.setTextColor(Color.parseColor("#545454"));
                placesTextView.setTextColor(Color.parseColor("#039970"));
                showMapView();
            }
        });

        journalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedTextView.setTextColor(Color.parseColor("#545454"));
                placesTextView.setTextColor(Color.parseColor("#545454"));
                journalTextView.setTextColor(Color.parseColor("#039970"));
                showJournalView();
            }
        });

        savedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedTextView.setTextColor(Color.parseColor("#039970"));
                placesTextView.setTextColor(Color.parseColor("#545454"));
                journalTextView.setTextColor(Color.parseColor("#545454"));
                showSavedView();
            }
        });

        return view;
    }

    private void showSavedView() {
        LinearLayout profileLayout = getView().findViewById(R.id.ProfileLayout);
        profileLayout.removeAllViews(); // Clear previous views
    }

    private void showJournalView() {
        LinearLayout profileLayout = getView().findViewById(R.id.ProfileLayout);
        profileLayout.removeAllViews(); // Clear previous views
    }

    private void showMapView() {
        LinearLayout profileLayout = getView().findViewById(R.id.ProfileLayout);
        profileLayout.removeAllViews(); // Clear previous views

        // Check for location permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Permission is granted, proceed with accessing the user's location
            CustomMapFragment mapFragment = CustomMapFragment.newInstance(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    googleMap.setMyLocationEnabled(true);
                    googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("New location"));
                            marker.showInfoWindow();
                            // Set a custom info window adapter to handle the info window view
                            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                @Override
                                public View getInfoWindow(Marker marker) {
                                    // Inflate your custom layout for the info window
                                    View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                                    // Get reference to EditText in the layout
                                    EditText titleEditText = view.findViewById(R.id.titleEditText);

                                    // Set current title in EditText
                                    titleEditText.setText(marker.getTitle());

                                    // Handle save button click
                                    Button saveButton = view.findViewById(R.id.saveButton);
                                    saveButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Save the new title to the marker and dismiss the info window
                                            marker.setTitle(titleEditText.getText().toString());
                                            marker.hideInfoWindow();
                                        }
                                    });

                                    // Save marker to the database
                                    saveMarkerToDatabase(currentUser, latLng.latitude, latLng.longitude, titleEditText.getText().toString());

                                    // Retrieve markers from the database
                                    retrieveMarkersFromDatabase(googleMap);

                                    return view;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {
                                    // Not used if getInfoWindow returns a non-null view
                                    return null;
                                }
                            });
                        }
                    });

                    // Retrieve markers from the database
                    retrieveMarkersFromDatabase(googleMap);
                }
            });

            getChildFragmentManager().beginTransaction().replace(R.id.ProfileLayout, mapFragment).commit();
        }
    }

    private void saveMarkerToDatabase(String userId, double latitude, double longitude, String title) {
        DatabaseReference markerRef = FirebaseDatabase.getInstance().getReference("markers");
        String markerId = markerRef.push().getKey(); // Generate a unique key for the marker

        MarkerData markerData = new MarkerData(userId, latitude, longitude, title);

        markerRef.child(markerId).setValue(markerData);
    }

    private void retrieveMarkersFromDatabase(GoogleMap googleMap) {
        // When retrieving markers from the database, filter by user ID
        DatabaseReference markerRef = FirebaseDatabase.getInstance().getReference("markers");
        markerRef.orderByChild("userId").equalTo(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MarkerData markerData = dataSnapshot.getValue(MarkerData.class);
                    // Check if the markerData is not null and userId is not null
                    if (markerData != null && markerData.getUserId() != null && markerData.getUserId().equals(currentUser)) {
                        LatLng latLng = new LatLng(markerData.getLatitude(), markerData.getLongitude());

                        if (googleMap != null) {
                            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(markerData.getTitle()));
                        } else {
                            Log.e("ProfileFragment", "GoogleMap is null");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProfileFragment", "Error retrieving markers", error.toException());
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, proceed with accessing the user's location
                CustomMapFragment mapFragment = CustomMapFragment.newInstance(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        googleMap.setMyLocationEnabled(true);
                        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                            @Override
                            public void onMapLongClick(LatLng latLng) {
                                Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("New location"));
                                marker.showInfoWindow();
                                // Set a custom info window adapter to handle the info window view
                                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                    @Override
                                    public View getInfoWindow(Marker marker) {
                                        // Inflate your custom layout for the info window
                                        View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                                        // Get reference to EditText in the layout
                                        EditText titleEditText = view.findViewById(R.id.titleEditText);

                                        // Set current title in EditText
                                        titleEditText.setText(marker.getTitle());

                                        // Handle save button click
                                        Button saveButton = view.findViewById(R.id.saveButton);
                                        saveButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Save the new title to the marker and dismiss the info window
                                                marker.setTitle(titleEditText.getText().toString());
                                                marker.hideInfoWindow();
                                            }
                                        });

                                        // Save marker to the database
                                        saveMarkerToDatabase(currentUser, latLng.latitude, latLng.longitude, titleEditText.getText().toString());

                                        // Retrieve markers from the database
                                        retrieveMarkersFromDatabase(googleMap);

                                        return view;
                                    }

                                    @Override
                                    public View getInfoContents(Marker marker) {
                                        // Not used if getInfoWindow returns a non-null view
                                        return null;
                                    }
                                });
                            }
                        });

                        // Retrieve markers from the database
                        retrieveMarkersFromDatabase(googleMap);
                    }
                });

                getChildFragmentManager().beginTransaction().replace(R.id.ProfileLayout,mapFragment).commit();
            } else {
                // Permission is denied, handle the situation here (e.g., show a message to the user)
            }
        }
    }

    private void sharingToSocialMedia(String application, String linkopen){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,linkopen);
        boolean installed = checkappInstall(application);

        if(installed){
            intent.setPackage(application);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Install Application First", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkappInstall(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        try{
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e){

        }
        return false;
    }
}