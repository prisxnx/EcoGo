package com.sp.navdrawertest.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sp.navdrawertest.R;
import com.sp.navdrawertest.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;
    private LinearLayout existingLayout;
    private LinearLayout webViewLayout;
    private WebView aboutWebView;
    private ProgressBar webViewProgressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AboutViewModel galleryViewModel =
                new ViewModelProvider(this).get(AboutViewModel.class);

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        existingLayout = root.findViewById(R.id.existingLayout);
        webViewLayout = root.findViewById(R.id.webViewLayout);
        aboutWebView = root.findViewById(R.id.aboutWebView);
        webViewProgressBar = root.findViewById(R.id.webViewProgressBar);

        Button readMoreButton = root.findViewById(R.id.readMoreButton);
        readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle visibility between existing layout and WebView layout
                toggleLayoutVisibility();

            }
        });

        // Load your about page URL
        String sustainabilityUrl = "https://earth.org/what-does-carbon-footprint-mean/#:~:text=As%20we%20go%20about%20our,stable%20climate%20for%20future%20generations.";
        aboutWebView.loadUrl(sustainabilityUrl);

        // Handle WebView settings
        WebSettings webSettings = aboutWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Handle WebView navigation
        aboutWebView.setWebViewClient(new WebViewClient());
        aboutWebView.setWebChromeClient(new WebChromeClient());

        return root;
    }

    private void toggleLayoutVisibility() {
        if (existingLayout.getVisibility() == View.VISIBLE) {
            existingLayout.setVisibility(View.GONE);
            webViewLayout.setVisibility(View.VISIBLE);
        } else {
            existingLayout.setVisibility(View.VISIBLE);
            webViewLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}