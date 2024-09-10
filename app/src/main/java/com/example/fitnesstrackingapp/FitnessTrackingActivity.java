package com.example.fitnesstrackingapp;

import static com.example.fitnesstrackingapp.StepHistoryAdapter.setListViewHeightBasedOnItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesstrackingapp.viewModel.ViewModelFitnessTracking;
import com.example.fitnesstrackingapp.viewModel.ViewModelUser;

public class FitnessTrackingActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager manager;
    private StepHistoryAdapter stepHistoryAdapter;

    private ViewModelUser viewModelUser;
    private ViewModelFitnessTracking viewModelFitness;
    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;
    public static final String PREFS_NAME = "PREFS_NAME";
    public static final String SAVED_STEPS = "SAVED_STEPS";

    TextView stepGoal, userMessage;
    Button btnBackToMain;
    ListView listStepHistory;

    private int goalSteps, stepsRemaining, currentSteps;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_tracking);

        // Set Views
        stepGoal = findViewById(R.id.stepGoal);
        btnBackToMain = findViewById(R.id.stepsBackBtn);
        userMessage = findViewById(R.id.encouragingMessage);
        listStepHistory = findViewById(R.id.listStepHistory);

        viewModelUser = new ViewModelProvider(this).get(ViewModelUser.class);
        // Use LiveData to update the Steps and encouraging message
        viewModelUser.getUserLiveData().observe(this, user -> {
            goalSteps = user.getFitnessGoal();
            userEmail = user.getEmail();
            stepGoal.setText("Of " + goalSteps);
            userMessage.setText("Just " + goalSteps + " steps to go!");
        });
        viewModelUser.loadUserProfile(this);

        viewModelFitness = new ViewModelProvider(this).get(ViewModelFitnessTracking.class);
        stepHistoryAdapter = new StepHistoryAdapter(viewModelFitness.getStepHistory(getApplicationContext()), this);
        listStepHistory.setAdapter(stepHistoryAdapter);

        // Adjust the height of the ListView based on its content
        setListViewHeightBasedOnItems(listStepHistory);

        // Update the Step History when there are changes eg. adding a entry
        viewModelFitness.getStepHistoryLiveData().observe(this, stepHistory -> {
                    //stepHistoryAdapter.setStepHistory(stepHistory);
                    stepHistoryAdapter.updateData(stepHistory);
                    // Adjust the height of the ListView based on its content
                    setListViewHeightBasedOnItems(listStepHistory);
                    stepHistoryAdapter.notifyDataSetChanged(); // Notify adapter of data changes
                });
        viewModelFitness.loadStepHistory(this);

        loadData();
        resetSteps();
        checkDeviceSensor();

        // Back to Main Button
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void saveData(){
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(SAVED_STEPS, previousTotalSteps);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSteps = preferences.getFloat(SAVED_STEPS, 0f);
        previousTotalSteps = savedSteps;
    }

    private void resetSteps(){
        TextView steps = findViewById(R.id.currentDailySteps);
        steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Long Press To Reset Steps", Toast.LENGTH_SHORT).show();
            }
        });

        steps.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                previousTotalSteps = totalSteps;
                viewModelFitness.saveDailySteps(FitnessTrackingActivity.this, currentSteps);
                steps.setText("0");
                currentSteps = 0;
                saveData();
                userMessage.setText("Just " + goalSteps + " steps to go!");
                return true;
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView tv_stepsTaken = findViewById(R.id.currentDailySteps);
        totalSteps = event.values[0];

        // Current steps are calculated by taking the difference of total steps
        // and previous steps
        currentSteps = (int) totalSteps - (int) previousTotalSteps;
        stepsRemaining = goalSteps - currentSteps;
        tv_stepsTaken.setText(String.valueOf(currentSteps));
        // Check if the step count surpasses the step goal
        if (currentSteps >= goalSteps) {
            displayCongratulatoryMessage();
        } else {
            displayStepCount();
        }
    }

    private void displayCongratulatoryMessage() {
        userMessage.setText("Congratulations!\nYou've reached your step goal!");
    }

    private void displayStepCount() {
        userMessage.setText("Just " + stepsRemaining + " steps to go!");
    }

    private void checkDeviceSensor(){
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor == null){
            Toast.makeText(this, "No Sensor Detected on this Device", Toast.LENGTH_SHORT).show();
        } else {
            manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}