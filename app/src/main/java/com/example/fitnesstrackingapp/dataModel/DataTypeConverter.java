package com.example.fitnesstrackingapp.dataModel;

import androidx.room.TypeConverter;

import java.util.Date;

public class DataTypeConverter {
    @TypeConverter
    public static Date fromTimeStamp(Long value){
        return value == null? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimeStamp(Date date){
        return date == null? null : date.getTime();
    }
}
