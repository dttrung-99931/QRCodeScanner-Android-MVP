package com.example.barcodescanner.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.example.barcodescanner.R;
import com.example.barcodescanner.databinding.ActivityMainBinding;
import com.example.barcodescanner.ui.base.BaseActivity;

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
        setupViewPagerAndBottomNav();
    }

    MainFragmentAdapter mMainFragmentAdapter = new MainFragmentAdapter(
            getSupportFragmentManager());

    private void setupViewPagerAndBottomNav() {
        mBinding.viewPager.setAdapter(mMainFragmentAdapter);
        mBinding.viewPager.setOnPageChangeListener(
                new OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        int itemMenuId = mapToBottomNavItemMenuId(position);
                        mBinding.navBottom.setSelectedItemId(itemMenuId);
                    }

                    private int mapToBottomNavItemMenuId(int selectedViewPagerPos) {
                        switch (selectedViewPagerPos) {
                            case 0: return R.id.menu_item_history;
                            case 1: return R.id.menu_item_scan;
                            default: return R.id.menu_item_settings;
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );
        mBinding.navBottom.setOnNavigationItemSelectedListener(
                item -> {
                    mBinding.viewPager.setCurrentItem(
                            mapToViewPagerPos(item.getItemId())
                    );
                    return true;
                }
        );
    }

    private int mapToViewPagerPos(int itemId) {
        switch (itemId) {
            case R.id.menu_item_history: return 0;
            case R.id.menu_item_scan: return 1;
            default: return 2;
        }
    }

    private void bindView() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }
}