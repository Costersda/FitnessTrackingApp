package com.example.fitnesstrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.fitnesstrackingapp.dataModel.FitnessAppDatabase;
import com.example.fitnesstrackingapp.dataModel.User;
import com.example.fitnesstrackingapp.dataModel.UserDao;

import java.text.ParseException;
import java.util.Objects;

public class UserRegistrationActivity extends AppCompatActivity {

    private Button btnSaveProfile;
    private String gender = "";

    FitnessAppDatabase database;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        // If user already exists go to the Main screen
        checkProfileExists();

        database = FitnessAppDatabase.createDatabaseInstance(getApplicationContext());
        userDao = database.userDao();

        //Save Profile Button
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnSaveProfile.setOnClickListener(v -> saveUserProfile());

    }

    private void saveUserProfile() {
        EditText etEmail, etFirstName, etLastName, etAge, etFitnessGoal;
        RadioButton btnMale, btnFemale;
        //Sets Views
        etFirstName = findViewById(R.id.saveFirstName);
        etLastName = findViewById(R.id.saveLastName);
        etEmail = findViewById(R.id.saveEmail);
        etAge = findViewById(R.id.saveAge);
        etFitnessGoal = findViewById(R.id.saveFitnessGoal);
        btnMale = findViewById(R.id.radioButtonMale);
        btnFemale = findViewById(R.id.radioButtonFemale);
        //Set entered information as Strings
        String fName, lName, email, age, fitnessGoal;
        fName = etFirstName.getText().toString();
        lName = etLastName.getText().toString();
        email = etEmail.getText().toString();
        age = etAge.getText().toString();
        fitnessGoal = etFitnessGoal.getText().toString();
        if (btnMale.isChecked()){
            gender = "Male";
        }
        if (btnFemale.isChecked()){
            gender = "Female";
        }
        // Check all information is present
        if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || age.isEmpty() || fitnessGoal.isEmpty() || (Objects.equals(gender, ""))) {
            Toast.makeText(this, "You need to enter information to save details", Toast.LENGTH_LONG).show();
        } else {
            User user = new User(email, fName, lName, Integer.parseInt(age), gender, Integer.parseInt(fitnessGoal));
            // Add user to database
            long emailDao = userDao.addUser(user);
            if (emailDao != -1) {
                Toast.makeText(this, "User record " + user.getFirstName() + " successfully added",
                        Toast.LENGTH_LONG).show();

                // Save user profile to shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", email);
                editor.putString("first_name", fName);
                editor.putString("last_name", lName);
                editor.putInt("age", Integer.parseInt(age));
                editor.putString("gender", gender);
                editor.putInt("fitness_goal", Integer.parseInt(fitnessGoal));
                editor.apply();

                // Navigate to the Main activity
                Intent intent = new Intent(UserRegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void checkProfileExists(){
        // Check if the user's profile exists in shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("email")) {
            // User's profile already exists, navigate to MainActivity
            startActivity(new Intent(UserRegistrationActivity.this, MainActivity.class));
            finish();
        }
    }
}