package com.example.barcodescanner.ui.main.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.barcodescanner.data.local.pref.Settings;
import com.example.barcodescanner.databinding.FragmentHistoryBinding;
import com.example.barcodescanner.databinding.FragmentScanBinding;
import com.example.barcodescanner.databinding.FragmentSettingBinding;
import com.example.barcodescanner.ui.base.BaseFragment;

/**
 * Created by Trung on 8/7/2020
 */
public class SettingFragment extends BaseFragment implements SettingPresenter.View{

    FragmentSettingBinding mBinding;
    SettingPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView();
        init();
        return mBinding.getRoot();
    }

    private void init() {
        mPresenter = new SettingPresenter();
        mPresenter.onAttached(this);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetached();
        super.onDestroyView();
    }

    private void bindView() {
        mBinding = FragmentSettingBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        mPresenter.loadSettings();
    }

    private void setupView() {
        mBinding.swEnableVibration.setOnCheckedChangeListener((buttonView, isChecked) -> {
           mPresenter.setVibrationEnabled(isChecked);
        });
    }

    @Override
    public void showSettings(Settings settings) {
        mBinding.swEnableVibration.setChecked(
                settings.isVibrationEnabled()
        );
    }
}
