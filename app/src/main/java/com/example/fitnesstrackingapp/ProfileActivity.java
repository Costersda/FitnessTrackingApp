package com.example.fitnesstrackingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesstrackingapp.dataModel.FitnessAppDatabase;
import com.example.fitnesstrackingapp.dataModel.UserDao;

import com.example.fitnesstrackingapp.viewModel.ViewModelUser;

public class ProfileActivity extends AppCompatActivity {

    TextView tvFName, tvLName, tvEmail, tvAge, tvGender, tvStepGoal;
    Button btnBackToMain, btnDeleteProfile;
    private ViewModelUser viewModelUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set Views
        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnDeleteProfile = findViewById(R.id.btnDeleteProfile);

        viewModelUser = new ViewModelProvider(this).get(ViewModelUser.class);
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        // Set user information
        setUserInfo();

        // Back to Main Button
        btnBackToMain.setOnClickListener(v -> {
            finish();
        });

        // Button that deletes user profile from shared preferences and database
        btnDeleteProfile.setOnClickListener(v -> {
            viewModelUser.deleteUserFitnessData(this, userEmail);
            viewModelUser.deleteUserProfile(this);
            Toast.makeText(this, "Profile Deleted!", Toast.LENGTH_LONG).show();
            finish();
            Intent intent = new Intent(getApplicationContext(), UserRegistrationActivity.class);
            startActivity(intent);
        });
    }

    // Set user information
    private void setUserInfo(){
        tvFName = findViewById(R.id.tvFirstName);
        tvLName = findViewById(R.id.tvLastName);
        tvEmail = findViewById(R.id.tvEmail);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvStepGoal = findViewById(R.id.tvDailyStepGoal);

        viewModelUser.getUserLiveData().observe(this, user -> {
            tvFName.setText("First Name: " + user.getFirstName());
            tvLName.setText("Last Name: " + user.getLastName());
            tvEmail.setText("Email: " + user.getEmail());
            tvAge.setText("Age: " + user.getAge());
            tvGender.setText("Gender: " + user.getGender());
            tvStepGoal.setText("Daily Step Goal: " + user.getFitnessGoal());
        });
        viewModelUser.loadUserProfile(this);
    }


}