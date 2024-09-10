package com.example.fitnesstrackingapp.dataModel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "user_fitness",
        foreignKeys = {
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "email",
                    childColumns = "email"
            )
        }
)
public class UserFitness {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "steps")
    private int steps;

    //Constructor
    public UserFitness(@NonNull Date date, String email, int steps) {
        this.date = date;
        this.email = email;
        this.steps = steps;
    }

    //Setters
    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    //Getters
    @NonNull
    public Date getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }

    public int getSteps() {
        return steps;
    }
}
