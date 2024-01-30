package com.sp.navdrawertest.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sp.navdrawertest.R;

import java.util.HashMap;
import java.util.Map;

public class CarbonFootprintFragment extends Fragment {

    private Spinner spinnerTransport, spinnerAccommodation, spinnerActivity;
    private EditText distance, noNights, noActivities;
    private TextView totalCarbonFootprintTextView;

    private static final Map<String, Double> TRANSPORT_EMISSION_FACTORS;
    private static final Map<String, Double> ACCOMMODATION_EMISSION_FACTORS;
    private static final Map<String, Double> ACTIVITY_EMISSION_FACTORS;

    static {
        TRANSPORT_EMISSION_FACTORS = new HashMap<>();
        TRANSPORT_EMISSION_FACTORS.put("Walk", 0.0);
        TRANSPORT_EMISSION_FACTORS.put("Motorcycle", 0.1);
        TRANSPORT_EMISSION_FACTORS.put("Car", 0.2);
        TRANSPORT_EMISSION_FACTORS.put("Bus", 0.05);
        TRANSPORT_EMISSION_FACTORS.put("MRT", 0.03);
        TRANSPORT_EMISSION_FACTORS.put("Airplane", 0.3);
        TRANSPORT_EMISSION_FACTORS.put("Ship", 0.0);

        ACCOMMODATION_EMISSION_FACTORS = new HashMap<>();
        ACCOMMODATION_EMISSION_FACTORS.put("Nil", 0.0);
        ACCOMMODATION_EMISSION_FACTORS.put("Hostel", 10.0);
        ACCOMMODATION_EMISSION_FACTORS.put("Airbnb", 12.0);
        ACCOMMODATION_EMISSION_FACTORS.put("Hotel", 15.0);

        ACTIVITY_EMISSION_FACTORS = new HashMap<>();
        ACTIVITY_EMISSION_FACTORS.put("Hiking", 2.0);
        ACTIVITY_EMISSION_FACTORS.put("Biking", 3.0);
        ACTIVITY_EMISSION_FACTORS.put("Sightseeing", 1.5);
        ACTIVITY_EMISSION_FACTORS.put("Shopping", 5.0);
        ACTIVITY_EMISSION_FACTORS.put("Dining", 4.0);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carbon_footprint, container, false);

        // Find the Spinner by its ID
        spinnerTransport = view.findViewById(R.id.spinner_Trans);
        spinnerAccommodation = view.findViewById(R.id.spinner_ACC);
        spinnerActivity = view.findViewById(R.id.spinner_AC);
        distance = view.findViewById(R.id.distance_travelled);
        noNights = view.findViewById(R.id.no_nights);
        noActivities = view.findViewById(R.id.no_ac);
        totalCarbonFootprintTextView = view.findViewById(R.id.CF_total);

        setupSpinners();

        Button calculateButton = view.findViewById(R.id.button_calculate);
        calculateButton.setOnClickListener(v -> calculateCarbonFootprint());

        return view;
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> transportAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.transportation,
                android.R.layout.simple_spinner_item
        );
        transportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTransport.setAdapter(transportAdapter);

        ArrayAdapter<CharSequence> accommodationAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.accommodations,
                android.R.layout.simple_spinner_item
        );
        accommodationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAccommodation.setAdapter(accommodationAdapter);

        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.activities,
                android.R.layout.simple_spinner_item
        );
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivity.setAdapter(activityAdapter);
    }
    private void calculateCarbonFootprint() {

        if (isDefaultItemSelected(spinnerTransport) || isDefaultItemSelected(spinnerAccommodation) || isDefaultItemSelected(spinnerActivity)) {
            Toast.makeText(requireContext(), "Please select valid options for all items", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(distance.getText()) || TextUtils.isEmpty(noNights.getText()) || TextUtils.isEmpty(noActivities.getText())) {
            Toast.makeText(requireContext(), "Please enter values for all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double distanceValue = getDoubleFromEditText(distance);
        int noNightsValue = getIntFromEditText(noNights);
        int noActivitiesValue = getIntFromEditText(noActivities);

        double transportFactor = getSelectedEmissionFactor(spinnerTransport, TRANSPORT_EMISSION_FACTORS);
        double accommodationFactor = getSelectedEmissionFactor(spinnerAccommodation, ACCOMMODATION_EMISSION_FACTORS);
        double activityFactor = getSelectedEmissionFactor(spinnerActivity, ACTIVITY_EMISSION_FACTORS);

        double totalCarbonFootprint = (distanceValue * transportFactor) +
                (noNightsValue * accommodationFactor) +
                (noActivitiesValue * activityFactor);

        totalCarbonFootprintTextView.setText(String.valueOf(totalCarbonFootprint));
    }

    private double getSelectedEmissionFactor(Spinner spinner, Map<String, Double> emissionFactors) {
        String selectedOption = spinner.getSelectedItem().toString();
        return emissionFactors.getOrDefault(selectedOption, 0.0);
    }

    private double getDoubleFromEditText(EditText editText) {
        try {
            return Double.parseDouble(editText.getText().toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int getIntFromEditText(EditText editText) {
        try {
            return Integer.parseInt(editText.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean isDefaultItemSelected(Spinner spinner) {
        // Check if the default "Select an item" option is selected
        return spinner.getSelectedItemPosition() == 0;
    }
}