package com.example.fitnesstrackingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitnesstrackingapp.viewModel.ViewModelUser;

public class MainActivity extends AppCompatActivity {

    TextView welcomeText;
    Button btnProfile, btnLocateMe, btnSteps;

    private static final int PERMISSION_REQUEST_CODE = 123;

    private ViewModelUser viewModelUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Views
        welcomeText = findViewById(R.id.welcomeText);
        btnProfile = findViewById(R.id.btnViewProfile);
        btnLocateMe = findViewById(R.id.btnLocateSelf);
        btnSteps = findViewById(R.id.btnStepCounter);

        askPermissions();



        viewModelUser = new ViewModelProvider(this).get(ViewModelUser.class);
        // Observe user LiveData and update the UI
        viewModelUser.getUserLiveData().observe(this, user -> {
            welcomeText.setText("Welcome, " + user.getFirstName());
        });
        // Load user profile from shared preferences
        viewModelUser.loadUserProfile(this);

        // View Profile Button
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Locate Yourself Button
        btnLocateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LocateMeMapsActivity.class);
                startActivity(intent);
            }
        });

        // Step Counter Button
        btnSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FitnessTrackingActivity.class);
                startActivity(intent);
            }
        });
    }

    //Returns true if all apps required permissions are granted
    private boolean arePermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                        == PackageManager.PERMISSION_GRANTED;
    }

    // Shows the Locate Me and Step Counter buttons if permissions are granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (arePermissionsGranted()) {
                // Permissions granted
                btnLocateMe.setVisibility(View.VISIBLE);
                btnSteps.setVisibility(View.VISIBLE);
            }
        }
    }

    // Dialog message if permissions are denied
    private void showPermissionsDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions Denied");
        builder.setMessage("You must enable required permissions. Please enable them in the app's settings.");
        builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAppSettings();
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Opens the apps settings
    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // Asks the user for the necessary permissions
    private void askPermissions(){
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION,false);
                    Boolean activityRecognitionGranted = result.getOrDefault(Manifest.permission.ACTIVITY_RECOGNITION, false);
                    if (fineLocationGranted != null && fineLocationGranted
                            && coarseLocationGranted != null && coarseLocationGranted) {
                        // Precise location access granted.
                        btnLocateMe.setVisibility(View.VISIBLE);
                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        // Only approximate location access granted.
                        btnLocateMe.setVisibility(View.INVISIBLE);
                        showPermissionsDeniedDialog();
                    } else {
                        // No location access granted.
                        btnLocateMe.setVisibility(View.INVISIBLE);
                        // Show a dialog or UI element to request permissions
                        showPermissionsDeniedDialog();
                    }
                    if (activityRecognitionGranted != null && activityRecognitionGranted) {
                        // Activity recognition access granted.
                        btnSteps.setVisibility(View.VISIBLE);
                    } else {
                        // No activity recognition access granted.
                        btnSteps.setVisibility(View.INVISIBLE);
                        showPermissionsDeniedDialog();
                    }
                });

        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACTIVITY_RECOGNITION
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if required permissions are granted on resume
        if (arePermissionsGranted()) {
            btnLocateMe.setVisibility(View.VISIBLE);
            btnSteps.setVisibility(View.VISIBLE);
        }
    }
}
