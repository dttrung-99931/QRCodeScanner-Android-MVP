package com.example.barcodescanner.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.barcodescanner.R;
import com.example.barcodescanner.ui.main.scan.ScanFragment;


/**
 * Created by Trung on 8/7/2020
 */
class MainFragmentAdapter extends FragmentStatePagerAdapter {

    private final Context mContext;

    public MainFragmentAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default: return new ScanFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        switch (position) {
//            case 0: return mContext.getString(R.string.title_history);
//            case 1: return mContext.getString(R.string.title_scan);
//            case 2: return mContext.getString(R.string.title_settings);
//        }
//        return super.getPageTitle(position);
//    }
}
