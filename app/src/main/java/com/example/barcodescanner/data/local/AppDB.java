package com.example.barcodescanner.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.barcodescanner.data.local.model.BarcodeData;
import com.example.barcodescanner.data.local.model.BarcodeField;

/**
 * Created by Trung on 8/12/2020
 */
@Database(entities = {BarcodeField.class,
                      BarcodeData.class}, version = 7)
public abstract class AppDB extends RoomDatabase {
    public abstract BarcodeDAO getBarcodeDAO();

    private static AppDB db;

    /*
    * Call only one time in Application class to init db
    * */
    public static void init(Context appContext) {
        db = Room.databaseBuilder(appContext,
                AppDB.class, "BarcodeAppDB")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static AppDB getInstance() {
        return db;
    }

    public static BarcodeDAO barcodeDAO(){
        return db.getBarcodeDAO();
    }
}
