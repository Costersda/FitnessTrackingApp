package com.example.fitnesstrackingapp.viewModel;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.fitnesstrackingapp.StepHistoryAdapter;
import com.example.fitnesstrackingapp.dataModel.FitnessAppDatabase;
import com.example.fitnesstrackingapp.dataModel.StepHistoryItem;
import com.example.fitnesstrackingapp.dataModel.UserFitness;
import com.example.fitnesstrackingapp.dataModel.UserFitnessDao;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ViewModelFitnessTracking extends ViewModel{

    private MutableLiveData<List<StepHistoryItem>> stepHistoryLiveData = new MutableLiveData<>();
    public LiveData<List<StepHistoryItem>> getStepHistoryLiveData() {
        return stepHistoryLiveData;
    }
    private StepHistoryAdapter stepHistoryAdapter;


    public void saveDailySteps(Context context, int steps){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        Date currentDate = new Date();
        int currentSteps = steps;
        UserFitness userFitness = new UserFitness(currentDate, email, currentSteps);
        FitnessAppDatabase database = FitnessAppDatabase.createDatabaseInstance(context);
        UserFitnessDao userFitnessDao = database.userFitnessDao();
        // Save details to the UserFitness Database table
        userFitnessDao.insertUserFitness(userFitness);
        stepHistoryLiveData.postValue(getStepHistory(context));
    }

    public List<StepHistoryItem> getStepHistory(Context context){
        // Retrieve the user's email from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        // Retrieve the last 7 entries from UserFitness table with the same email
        FitnessAppDatabase database = FitnessAppDatabase.createDatabaseInstance(context);
        UserFitnessDao userFitnessDao = database.userFitnessDao();

        List<UserFitness> userFitnessList = userFitnessDao.getLast7Entries(userEmail);
        // Log.e("list", "has length of " + String.valueOf(userFitnessList.size()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

        // Convert UserFitness objects to StepHistoryItem objects
        List<StepHistoryItem> stepHistoryList = new ArrayList<>();
        for (UserFitness userFitness : userFitnessList) {
            StepHistoryItem stepHistoryItem = new StepHistoryItem(dateFormat.format(userFitness.getDate()), userFitness.getSteps());
            stepHistoryList.add(stepHistoryItem);
        }
        return stepHistoryList;
    }

    // Populate the Step History ListView
    public void loadStepHistory(Context context) {
        List<StepHistoryItem> stepHistoryList = getStepHistory(context);
        stepHistoryAdapter = new StepHistoryAdapter(stepHistoryList, context);
        stepHistoryAdapter.addAll(stepHistoryList);
    }

}