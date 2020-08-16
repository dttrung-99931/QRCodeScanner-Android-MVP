package com.example.barcodescanner.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.barcodescanner.R;
import com.example.barcodescanner.databinding.ActivityMainBinding;
import com.example.barcodescanner.ui.base.BaseActivity;
import com.example.barcodescanner.ui.main.history.HistoryFragment;
import com.example.barcodescanner.ui.main.scan.ScanFragment;

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
            getSupportFragmentManager(), getLifecycle());

    private void setupViewPagerAndBottomNav() {
        mBinding.viewPager.setAdapter(mMainFragmentAdapter);

        // Init selected scan fragment page
        mBinding.navBottom.setSelectedItemId(R.id.menu_item_scan);
        mBinding.viewPager.setCurrentItem(1);

        // Set switch fragment on viewpager when active navBottom item changed
        mBinding.navBottom.setOnNavigationItemSelectedListener(
                item -> {
                    mBinding.viewPager.setCurrentItem(
                            mapToViewPagerPos(item.getItemId())
                    );
                    return true;
                }
        );

        // Stop/pause scanning when switching through/back ScanFragment
        mBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                ScanFragment scanFragment = getScanFragment();
                if (scanFragment == null) return;
                if (position != 1){
                    scanFragment.stopDetecting();
                } else {
                    scanFragment.resumeDetecting();
                }
                super.onPageSelected(position);
            }
        });

        // Disable view pager switching
        mBinding.viewPager.setUserInputEnabled(false);
    }

    private ScanFragment getScanFragment() {
        return (ScanFragment) getFragmentInViewPager(1);
    }

    private int mapToViewPagerPos(int itemId) {
        switch (itemId) {
            case R.id.menu_item_history:
                return 0;
            case R.id.menu_item_scan:
                return 1;
            default:
                return 2;
        }
    }

    private void bindView() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    public void notifyRefreshHistoryFragment() {
        HistoryFragment historyFragment = (HistoryFragment)
                getFragmentInViewPager(0);
        historyFragment.refresh();
    }

    private Fragment getFragmentInViewPager(int i) {
        String fragmentTagInViewPager = "f" + i;
        return getSupportFragmentManager()
                .findFragmentByTag(fragmentTagInViewPager);
    }

    public interface FragmentDismissCallBack {
        void onFragmentDismiss();
    }

    private FragmentDismissCallBack mCallBack = () -> {
        String curFragmentTagInViewPager =
                "f" + mBinding.viewPager.getCurrentItem();
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(curFragmentTagInViewPager);
        if (fragment instanceof ScanFragment) {
            ((ScanFragment)fragment).resumeScanningWithDelay();
        }
    };

}