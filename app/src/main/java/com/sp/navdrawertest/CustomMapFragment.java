package com.sp.navdrawertest;

import android.os.Bundle;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class CustomMapFragment extends SupportMapFragment {

    private OnMapReadyCallback callback;

    public CustomMapFragment() {
    }

    public static CustomMapFragment newInstance(OnMapReadyCallback callback) {
        CustomMapFragment fragment = new CustomMapFragment();
        fragment.callback = callback;
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMapAsync(callback);
    }
}