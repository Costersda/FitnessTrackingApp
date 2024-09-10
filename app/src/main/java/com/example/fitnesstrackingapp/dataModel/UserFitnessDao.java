package com.example.fitnesstrackingapp.dataModel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface UserFitnessDao {

    @Query("SELECT * FROM user_fitness")
    List<UserFitness> readAllUserFitness();

    // Delete all records with selected email
    @Query("DELETE FROM user_fitness WHERE email = :userEmail")
    void deleteByUserEmail(String userEmail);

    @Insert
    long insertUserFitness(UserFitness userFitness);

    @Update
    int updateUserFitness(UserFitness userFitness);

    @Delete
    int deleteUserFitness(UserFitness userFitness);

    // Get step history for the past seven days
    @Query("SELECT * FROM user_fitness WHERE email = :userEmail ORDER BY date DESC LIMIT 7")
    List<UserFitness> getLast7Entries(String userEmail);

}
