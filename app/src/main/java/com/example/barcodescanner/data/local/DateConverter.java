package com.example.barcodescanner.data.local;

import android.annotation.SuppressLint;

import androidx.room.TypeConverter;

import com.example.barcodescanner.util.CommonUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Trung on 8/15/2020
 */
public class DateConverter {
    public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy hh:mm";

    @SuppressLint("SimpleDateFormat")
    static DateFormat defaultDateFormat =
            new SimpleDateFormat(DEFAULT_DATE_PATTERN);

    @TypeConverter
    public static Date timestampStrToDate(String value) {
        if (value != null) {
            try {
                return defaultDateFormat.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @TypeConverter
    public static String dateToTimestampStr(Date date) {
        return defaultDateFormat.format(date);
    }

    public static String format(Date date, String pattern) {
        if (pattern.equals(DEFAULT_DATE_PATTERN))
            return defaultDateFormat.format(date);

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }
}
