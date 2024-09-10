package com.example.fitnesstrackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Delay for a few seconds and then start the main activity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, UserRegistrationActivity.class));
            finish(); // Finish the splash activity
        }, 2000); // 2000 milliseconds delay
    }
}
