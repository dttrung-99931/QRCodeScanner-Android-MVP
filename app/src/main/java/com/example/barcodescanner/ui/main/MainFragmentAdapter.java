package com.example.barcodescanner.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.barcodescanner.ui.main.history.HistoryFragment;
import com.example.barcodescanner.ui.main.scan.ScanFragment;
import com.example.barcodescanner.ui.main.setting.SettingFragment;


/**
 * Created by Trung on 8/7/2020
 */
class MainFragmentAdapter extends FragmentStateAdapter {

    public MainFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new HistoryFragment();
            case 1: return new ScanFragment();
            default: return new SettingFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
