package com.example.barcodescanner.ui.main.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView;
import com.example.barcodescanner.R;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.databinding.FragmentHistoryBinding;
import com.example.barcodescanner.ui.base.BaseFragment;
import com.example.barcodescanner.ui.base.SpacingDecorator;
import com.example.barcodescanner.util.ViewUtil;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * Created by Trung on 8/7/2020
 */
public class HistoryFragment extends BaseFragment implements HistoryPresenter.View {

    FragmentHistoryBinding mBinding;
    BarcodeHistoryAdapter2 mBarcodeHistoryAdapter;
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
        setupRecyclerViewBarcodeHistories();
    }

    private void setupRecyclerViewBarcodeHistories() {
        mBarcodeHistoryAdapter = new BarcodeHistoryAdapter2();
        DragDropSwipeRecyclerView recyclerView = mBinding.recyclerView;

        recyclerView.addItemDecoration(
                new SpacingDecorator(ViewUtil.sdpToPx(getResources(), R.dimen._3sdp))
        );
        mBarcodeHistoryAdapter.setChildFragmentManager(getChildFragmentManager());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mBarcodeHistoryAdapter);

        recyclerView.setSwipeListener((removedPos, swipeDirection, o) -> {
            mBarcodeHistoryAdapter.backupRemovedItem(removedPos);
            showCancelRemoveSnakeBar(
                    mBarcodeHistoryAdapter.getDataSet().get(removedPos)
            );
            if (removedPos > 0) {
                mBarcodeHistoryAdapter.notifyItemChanged(removedPos - 1);
            }
            return false;
        });
        recyclerView.disableDragDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.DOWN);
        recyclerView.disableDragDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.UP);
    }

    private void showCancelRemoveSnakeBar(RelationBarcodeData removedItem) {
        Snackbar.make(mBinding.getRoot(),
                R.string.cancel_remove, BaseTransientBottomBar.LENGTH_SHORT)
                .setAction(R.string.cancel, v -> {
                    mBarcodeHistoryAdapter.restoreRemovedItem();
                })
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (!mBarcodeHistoryAdapter.isRemoveCanceled()) {
                            mPresenter.deleteBarcodeData(removedItem);
                        }
                    }
                })
                .show();
    }

    @Override
    public void showBarcodeHistories(List<RelationBarcodeData> relBarcodeDataList) {
        mBarcodeHistoryAdapter.setDataSet(relBarcodeDataList);
        mBarcodeHistoryAdapter.notifyDataSetChanged();
        mBinding.recyclerView.scrollToPosition(mBarcodeHistoryAdapter.getItemCount()-1);
    }

    public void refresh() {
        mPresenter.loadBarcodeData();
    }
}
