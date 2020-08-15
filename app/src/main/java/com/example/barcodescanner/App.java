package com.example.barcodescanner;

import android.app.Application;

import com.example.barcodescanner.data.local.AppDB;

/**
 * Created by Trung on 8/12/2020
 */
public class App extends Application {
    @Override
    public void onCreate() {
        AppDB.init(this);
        super.onCreate();
    }
}
