package com.sp.navdrawertest.ui;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.sp.navdrawertest.CaptureAct;
import com.sp.navdrawertest.MainActivity;
import com.sp.navdrawertest.R;

public class QRFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr, container, false);
        Button ScanNow = view.findViewById(R.id.scannowbutton);

        ScanNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                scanCode();
            }
        });
        return view;
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Find an EcoGo QR Code to Scan!\nVolume Up/Down to turn flash On/Off");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents()!=null){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Nice Scan!");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("BACK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });
}

