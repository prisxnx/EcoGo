package com.sp.navdrawertest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.intro);
        mediaPlayer.start();

        intent = new Intent(SplashScreen.this, Login.class);
        Handler handler = new Handler();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(intent);
                finish();
            }
        },5000);
    }
}