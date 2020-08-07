package com.example.barcodescanner.ui.splash;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.barcodescanner.ui.main.MainActivity;
import com.example.barcodescanner.ui.base.BaseActivity;

/**
 * Created by Trung on 8/6/2020
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.open(this);
    }
}
