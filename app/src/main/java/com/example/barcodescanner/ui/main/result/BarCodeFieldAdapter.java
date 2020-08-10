package com.example.barcodescanner.ui.main.result;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodescanner.R;
import com.example.barcodescanner.data.model.BarCodeData;
import com.example.barcodescanner.databinding.ItemBarcodeFieldBinding;
import com.example.barcodescanner.ui.base.BaseViewHolder;
import com.example.barcodescanner.util.ViewUtil;

import java.util.List;

/**
 * Created by Trung on 8/9/2020
 */
class BarCodeFieldAdapter extends RecyclerView.Adapter<BarCodeFieldAdapter.BarCodeFieldViewHolder> {
    private List<BarCodeData.BarCodeField> mBarCodeFields;

    public BarCodeFieldAdapter(List<BarCodeData.BarCodeField> barCodeFields) {
        mBarCodeFields = barCodeFields;
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
        return mBarCodeFields.size();
    }

    class BarCodeFieldViewHolder extends BaseViewHolder {
        ItemBarcodeFieldBinding mBinding;

        public BarCodeFieldViewHolder(ItemBarcodeFieldBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        @Override
        public void bindView(int position) {
            BarCodeData.BarCodeField field = mBarCodeFields.get(position);
            if (mBarCodeFields.size() == 1) {
                // Hide tvFieldName and add padding top for tvFieldValue
                mBinding.tvFieldName.setVisibility(View.GONE);
                int newPaddingTop = ViewUtil.sdpToPx(itemView.getResources(), R.dimen._9sdp);
                int newPaddingStart = ViewUtil.sdpToPx(itemView.getResources(), R.dimen._13sdp);
                mBinding.tvFieldValue.setPadding(
                        newPaddingStart,
                        newPaddingTop,
                        mBinding.tvFieldValue.getPaddingEnd(),
                        mBinding.tvFieldValue.getPaddingBottom()
                );
            } else {
                mBinding.tvFieldName.setText(field.getFieldName());
            }
            mBinding.tvFieldValue.setText(field.getFieldValue());
        }
    }
}
