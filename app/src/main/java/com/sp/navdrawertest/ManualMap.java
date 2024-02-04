package com.sp.navdrawertest;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sp.navdrawertest.databinding.ManualMapBinding;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ManualMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SearchView mapSearchView;
    private ManualMapBinding binding;
    private double selectedLatitude, selectedLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ManualMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapSearchView = findViewById(R.id.searchMap);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.manual_map);
        mapFragment.getMapAsync(this);


        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                String location = mapSearchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null) {
                    Geocoder geocoder = new Geocoder(ManualMap.this);

                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        if (!addressList.isEmpty()) {
                            Address address = addressList.get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                            // Create an Intent to return the selected latitude and longitude
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("latitude", address.getLatitude());
                            resultIntent.putExtra("longitude", address.getLongitude());

                            // Set the result code and data
                            setResult(Activity.RESULT_OK, resultIntent);

                            // Finish the activity
                            finish();

                        } else {
                            // Handle the case when no results are found
                            Toast.makeText(ManualMap.this, "Location not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Set up other map configurations if needed
    }
}