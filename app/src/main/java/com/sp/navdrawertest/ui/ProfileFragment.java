package com.sp.navdrawertest.ui;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sp.navdrawertest.CustomMapFragment;
import android.Manifest;
import com.sp.navdrawertest.R;


public class ProfileFragment extends Fragment {

    private TextView placesTextView, journalTextView, savedTextView;
    private CardView mapcardview;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        placesTextView = view.findViewById(R.id.placestextview);
        journalTextView = view.findViewById(R.id.journaltextview);
        savedTextView = view.findViewById(R.id.savedtextview);

        placesTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                placesTextView.setTextColor(Color.parseColor("#039970"));
                showMapView();
            }
        });

        return view;
    }

    private void showMapView(){
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
                    googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){
                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            // Add a marker at the long-pressed location
                            googleMap.addMarker(new MarkerOptions().position(latLng).title("New location"));
                        }
                    });
                }
            });

            getChildFragmentManager().beginTransaction().replace(R.id.ProfileLayout,mapFragment).commit();
        }
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
                        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){
                            @Override
                            public void onMapLongClick(LatLng latLng) {
                                // Add a marker at the long-pressed location
                                googleMap.addMarker(new MarkerOptions().position(latLng).title("New location"));
                            }
                        });
                    }
                });

                getChildFragmentManager().beginTransaction().replace(R.id.ProfileLayout,mapFragment).commit();
            } else {
                // Permission is denied, handle the situation here (e.g., show a message to the user)
            }
        }
    }
}