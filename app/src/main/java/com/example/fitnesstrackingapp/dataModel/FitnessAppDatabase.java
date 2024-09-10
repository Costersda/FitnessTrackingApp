package com.example.fitnesstrackingapp.dataModel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = {User.class, UserFitness.class},
        version = 1
)
@TypeConverters(DataTypeConverter.class)
public abstract class FitnessAppDatabase extends RoomDatabase {

    private static FitnessAppDatabase database = null;

    public abstract UserDao userDao();
    public abstract UserFitnessDao userFitnessDao();

    public static FitnessAppDatabase createDatabaseInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(
                            context.getApplicationContext(),
                            FitnessAppDatabase.class,
                            "app_database"
                    )
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }
}
