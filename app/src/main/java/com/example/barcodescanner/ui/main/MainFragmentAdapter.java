package com.example.barcodescanner.ui.main;

import android.content.Context;
import android.location.SettingInjectorService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.barcodescanner.R;
import com.example.barcodescanner.ui.main.history.HistoryFragment;
import com.example.barcodescanner.ui.main.scan.ScanFragment;
import com.example.barcodescanner.ui.main.setting.SettingFragment;


/**
 * Created by Trung on 8/7/2020
 */
class MainFragmentAdapter extends FragmentStatePagerAdapter {

    public MainFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new HistoryFragment();
            case 1: return new ScanFragment();
            default: return new SettingFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
