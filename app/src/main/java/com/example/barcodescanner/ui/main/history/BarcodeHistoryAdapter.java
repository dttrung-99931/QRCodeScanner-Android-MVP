package com.example.barcodescanner.ui.main.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodescanner.data.local.DateConverter;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.databinding.ItemBarcodeHistoryBinding;
import com.example.barcodescanner.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 8/9/2020
 */
public class BarcodeHistoryAdapter extends
        RecyclerView.Adapter<BarcodeHistoryAdapter.BarcodeHistoryViewHolder> {

    private List<RelationBarcodeData> mRelBarcodeDataList = new ArrayList<>();

    public void setRelBarcodeDataList(List<RelationBarcodeData> mRelBarcodeDataList) {
        this.mRelBarcodeDataList = mRelBarcodeDataList;
    }

    @NonNull
    @Override
    public BarcodeHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BarcodeHistoryViewHolder(
                ItemBarcodeHistoryBinding.inflate(
                        LayoutInflater.from(parent.getContext())
                ));
    }

    @Override
    public void onBindViewHolder(@NonNull BarcodeHistoryViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mRelBarcodeDataList.size();
    }

    class BarcodeHistoryViewHolder extends BaseViewHolder {
        ItemBarcodeHistoryBinding mBinding;

        public BarcodeHistoryViewHolder(ItemBarcodeHistoryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        @Override
        public void bindView(int position) {
            RelationBarcodeData relBarcodeData = mRelBarcodeDataList.get(position);
            mBinding.tvFieldName.setText(
                    relBarcodeData.barcodeData.getTypeStrResId()
            );
            mBinding.tvFieldValue.setText(
                    relBarcodeData.barcodeFields.get(0).getFieldValue()
            );
            mBinding.tvTime.setText(
                    DateConverter.dateToTimestampStr(
                            relBarcodeData.barcodeData.getCreatedAt())
            );
        }
    }
}
