package com.example.barcodescanner.ui.main.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.barcodescanner.databinding.FragmentHistoryBinding;
import com.example.barcodescanner.databinding.FragmentScanBinding;
import com.example.barcodescanner.ui.base.BaseFragment;

/**
 * Created by Trung on 8/7/2020
 */
public class HistoryFragment extends BaseFragment implements HistoryPresenter.View{

    FragmentHistoryBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView();
        return mBinding.getRoot();
    }

    private void bindView() {
        mBinding = FragmentHistoryBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    private void setupView() {

    }
}
