package com.example.barcodescanner.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.barcodescanner.R;
import com.example.barcodescanner.databinding.ActivityMainBinding;
import com.example.barcodescanner.ui.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends BaseActivity {
    ActivityMainBinding mBinding;

    public static void open(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();
    }

    private void setupView() {
        bindView();
        setupViewPager();
    }

    MainFragmentAdapter mMainFragmentAdapter = new MainFragmentAdapter(
            getSupportFragmentManager(), this);

    private void setupViewPager() {
        mBinding.mViewPager.setAdapter(mMainFragmentAdapter);
        mBinding.mTabLayout.setupWithViewPager(mBinding.mViewPager);
        setCustomTabLayoutIcon();
    }

    private void setCustomTabLayoutIcon() {
        int []iconResIds = new int[]{
                R.drawable.ic_history,
                R.drawable.ic_scan,
                R.drawable.ic_settings
        };
        for (int i = 0; i < mBinding.mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mBinding.mTabLayout.getTabAt(i);
            tab.setCustomView(R.layout.customer_tab);
            tab.setIcon(iconResIds[i]);
        }
    }

    private void bindView() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }
}