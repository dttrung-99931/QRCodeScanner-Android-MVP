package com.example.barcodescanner.data.local;

import android.annotation.SuppressLint;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Trung on 8/15/2020
 */
public class DateConverter {
    @SuppressLint("SimpleDateFormat")
    static DateFormat dateFormat =
            new SimpleDateFormat("dd/MM/yyyy hh:mm");

    @TypeConverter
    public static Date timestampStrToDate(String value) {
        if (value != null) {
            try {
                return dateFormat.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @TypeConverter
    public static String dateToTimestampStr(Date date) {
        return dateFormat.format(date);
    }
}
