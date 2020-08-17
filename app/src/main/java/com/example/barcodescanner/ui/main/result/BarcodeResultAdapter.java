package com.example.barcodescanner.ui.main.result;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodescanner.R;
import com.example.barcodescanner.data.local.model.BarcodeField;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.databinding.ItemBarcodeFieldBinding;
import com.example.barcodescanner.ui.base.BaseViewHolder;
import com.example.barcodescanner.util.BarcodeActionUtil;
import com.example.barcodescanner.util.CommonUtil;
import com.example.barcodescanner.util.ViewUtil;

import static java.lang.Math.max;

/**
 * Created by Trung on 8/9/2020
 */
public class BarcodeResultAdapter extends RecyclerView.Adapter<BarcodeResultAdapter.BarCodeFieldViewHolder> {
    private RelationBarcodeData relationBarcodeData;

    public BarcodeResultAdapter() {
    }

    public void setRelationBarcodeData(RelationBarcodeData relationBarcodeData) {
        this.relationBarcodeData = relationBarcodeData;
    }

    @NonNull
    @Override
    public BarCodeFieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BarCodeFieldViewHolder(
                ItemBarcodeFieldBinding.inflate(
                        LayoutInflater.from(parent.getContext())
                ));
    }

    @Override
    public void onBindViewHolder(@NonNull BarCodeFieldViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return relationBarcodeData.barcodeFields.size();
    }

    class BarCodeFieldViewHolder extends BaseViewHolder {
        ItemBarcodeFieldBinding mBinding;

        public BarCodeFieldViewHolder(ItemBarcodeFieldBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        @Override
        public void bindView(int position) {
            BarcodeField field = relationBarcodeData.barcodeFields.get(position);
            if (relationBarcodeData.barcodeFields.size() == 1) {
                changeItemUI();
            } else {
                mBinding.tvFieldName.setText(field.getFieldNameStrResId());
            }
            mBinding.tvFieldValue.setText(field.getFieldValue());
            mBinding.btnCopy.setOnClickListener(v -> {
                Context appContext = v.getContext().getApplicationContext();
                BarcodeActionUtil.copyToClipBoard(
                        mBinding.tvFieldValue.getText(),
                        appContext
                );
                Toast.makeText(appContext, appContext.getString(R.string.copied),
                        Toast.LENGTH_SHORT).show();
            });
        }

        // Hide tvFieldName and add padding top for tvFieldValue
        private void changeItemUI() {
            mBinding.tvFieldName.setVisibility(View.GONE);
            int newPaddingTop = ViewUtil.sdpToPx(itemView.getResources(), R.dimen._9sdp);
            int newPaddingStart = ViewUtil.sdpToPx(itemView.getResources(), R.dimen._13sdp);
            mBinding.tvFieldValue.setPadding(
                    newPaddingStart,
                    newPaddingTop,
                    mBinding.tvFieldValue.getPaddingEnd(),
                    mBinding.tvFieldValue.getPaddingBottom()
            );
        }
    }
}
