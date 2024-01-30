package com.sp.navdrawertest.ui.contactus;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sp.navdrawertest.R;
import com.sp.navdrawertest.databinding.FragmentContactusBinding;

public class ContactUsFragment extends Fragment {
    static int PERMISSION_CODE=100;
    private FragmentContactusBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ContactUsViewModel contactUsViewModel =
                new ViewModelProvider(this).get(ContactUsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_contactus, container, false);
        Button CallButton= view.findViewById(R.id.callbutton);
        Button EmailButton =view.findViewById(R.id.emailbutton);

        CallButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, make the call
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:+6591164229"));
                    startActivity(i);
                } else {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
                }

            }
        });

        EmailButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:rileyclairekang@gmail.com"));
                startActivity(emailIntent);

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}