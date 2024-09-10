package com.example.fitnesstrackingapp.dataModel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> readAllUsers();

    @Insert
    long addUser(User user);     // -1 means record is not inserted

    @Update
    int updateUser(User user);    // 0 means no record is updated

    @Delete
    int deleteUser(User user);    // 0 means no record is deleted

}
