package com.example.barcodescanner.ui.main.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.barcodescanner.R;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.databinding.FragmentHistoryBinding;
import com.example.barcodescanner.ui.base.BaseFragment;
import com.example.barcodescanner.ui.base.SpacingDecorator;
import com.example.barcodescanner.util.ViewUtil;

import java.util.List;

/**
 * Created by Trung on 8/7/2020
 */
public class HistoryFragment extends BaseFragment implements HistoryPresenter.View {

    FragmentHistoryBinding mBinding;
    BarcodeHistoryAdapter mBarcodeHistoryAdapter;
    HistoryPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView();
        init();
        return mBinding.getRoot();
    }

    private void init() {
        mPresenter = new HistoryPresenter();
        mPresenter.onAttached(this);
    }

    private void bindView() {
        mBinding = FragmentHistoryBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        mPresenter.loadBarcodeData();
    }

    private void setupView() {
        setupRecyclerViewBarcodeFields();
    }

    private void setupRecyclerViewBarcodeFields() {
        mBarcodeHistoryAdapter = new BarcodeHistoryAdapter();
        mBinding.recyclerView.addItemDecoration(
                new SpacingDecorator(ViewUtil.sdpToPx(getResources(), R.dimen._3sdp))
        );
        mBarcodeHistoryAdapter.setChildFragmentManager(getChildFragmentManager());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setReverseLayout(true);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBinding.recyclerView.setAdapter(mBarcodeHistoryAdapter);
    }

    @Override
    public void showBarcodeHistories(List<RelationBarcodeData> relBarcodeDataList) {
        mBarcodeHistoryAdapter.setRelBarcodeDataList(relBarcodeDataList);
        mBarcodeHistoryAdapter.notifyDataSetChanged();
        mBinding.recyclerView.scrollToPosition(mBarcodeHistoryAdapter.getItemCount()-1);
    }

    public void refresh() {
        mPresenter.loadBarcodeData();
    }
}
