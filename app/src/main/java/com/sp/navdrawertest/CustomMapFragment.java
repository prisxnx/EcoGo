package com.sp.navdrawertest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sp.navdrawertest.MarkerData;

public class CustomMapFragment extends SupportMapFragment {

    private OnMapReadyCallback callback;
    private String userId;

    public static CustomMapFragment newInstance(OnMapReadyCallback callback) {
        CustomMapFragment fragment = new CustomMapFragment();
        fragment.callback = callback; // Set the callback during initialization
        return fragment;
    }

    public void setOnMapReadyCallback(OnMapReadyCallback callback) {
        this.callback = callback;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (callback != null) {
            getMapAsync(callback);
        } else {
            getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (callback != null) {
                        callback.onMapReady(googleMap);
                    }
                    loadMarkers(googleMap);
                }
            });
        }
    }

    private void loadMarkers(GoogleMap googleMap) {
        FirebaseDatabase.getInstance().getReference("markers")
                .orderByChild("userId")
                .equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            MarkerData markerData = snapshot.getValue(MarkerData.class);
                            LatLng markerPosition = new LatLng(markerData.getLatitude(), markerData.getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(markerPosition).title(markerData.getTitle()));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error here
                    }
                });
    }
}