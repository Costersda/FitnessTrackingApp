package com.example.fitnesstrackingapp.dataModel;

public class StepHistoryItem {
    private String date;
    private int stepCount;

    // Constructor
    public StepHistoryItem(String date, int stepCount) {
        this.date = date;
        this.stepCount = stepCount;
    }

    // Getters
    public String getDate() {
        return date;
    }
    public int getStepCount() {
        return stepCount;
    }

    // Setters
    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
    public void setDate(String date) {
        this.date = date;
    }


}