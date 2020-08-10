package com.example.barcodescanner.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;

import com.example.barcodescanner.R;
import com.example.barcodescanner.databinding.ActivityMainBinding;
import com.example.barcodescanner.ui.base.BaseActivity;
import com.example.barcodescanner.ui.main.result.ResultFragment;
import com.example.barcodescanner.ui.main.scan.ScanFragment;
import com.example.barcodescanner.util.CommonUtil;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.vision.barcode.Barcode;

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
        // Init selected scan fragment page
        mBinding.navBottom.setSelectedItemId(R.id.menu_item_scan);

        mBinding.viewPager.setAdapter(mMainFragmentAdapter);
        mBinding.navBottom.setOnNavigationItemSelectedListener(
                item -> {
                    mBinding.viewPager.setCurrentItem(
                            mapToViewPagerPos(item.getItemId())
                    );
                    return true;
                }
        );

        mBinding.viewPager.setCurrentItem(1);

        // Disable view pager switch
        mBinding.viewPager.setUserInputEnabled(false);
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

    public void showBarcodeResultWithDelay(SparseArray<Barcode> detectedBarcodes) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            ResultFragment.show(
                    detectedBarcodes,
                    getSupportFragmentManager(),
                    mCallBack
            );
        }, 300);
    }

}