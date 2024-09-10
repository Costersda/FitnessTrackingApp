package com.example.fitnesstrackingapp.viewModel;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fitnesstrackingapp.dataModel.FitnessAppDatabase;
import com.example.fitnesstrackingapp.dataModel.User;
import com.example.fitnesstrackingapp.dataModel.UserDao;
import com.example.fitnesstrackingapp.dataModel.UserFitnessDao;

public class ViewModelUser extends ViewModel {
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public void loadUserProfile(Context context) {
        // Load user profile from shared preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String firstName = sharedPreferences.getString("first_name", "");
        String lastName = sharedPreferences.getString("last_name", "");
        int age = sharedPreferences.getInt("age", 0);
        String gender = sharedPreferences.getString("gender", "");
        int fitnessGoal = sharedPreferences.getInt("fitness_goal", 0);

        User user = new User(email, firstName, lastName, age, gender, fitnessGoal);
        userLiveData.setValue(user);
    }

    public void deleteUserProfile(Context context) {
        // Delete user profile from shared preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all data for the user
        editor.apply();

        // Delete user profile from the database using Room
        FitnessAppDatabase database = FitnessAppDatabase.createDatabaseInstance(context);
        UserDao userDao = database.userDao();
        User user = userLiveData.getValue(); // Get the user from LiveData
        if (user != null) {
            userDao.deleteUser(user);
        }

    }

    public void deleteUserFitnessData(Context context, String userEmail) {
        FitnessAppDatabase database = FitnessAppDatabase.createDatabaseInstance(context);
        UserFitnessDao userFitnessDao = database.userFitnessDao();

        // Delete UserFitness data for the specified email
        userFitnessDao.deleteByUserEmail(userEmail);
    }


}
