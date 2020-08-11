package com.example.barcodescanner.ui.main.result;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodescanner.R;
import com.example.barcodescanner.data.model.BarCodeData;
import com.example.barcodescanner.databinding.ItemBarcodeFieldBinding;
import com.example.barcodescanner.ui.base.BaseViewHolder;
import com.example.barcodescanner.util.CommonUtil;
import com.example.barcodescanner.util.ViewUtil;

import java.util.List;

/**
 * Created by Trung on 8/9/2020
 */
public class BarCodeFieldAdapter extends RecyclerView.Adapter<BarCodeFieldAdapter.BarCodeFieldViewHolder> {
    private List<BarCodeData.BarCodeField> mBarCodeFields;

    public BarCodeFieldAdapter(List<BarCodeData.BarCodeField> barCodeFields) {
        mBarCodeFields = barCodeFields;
    }

    public void setBarCodeFields(List<BarCodeData.BarCodeField> mBarCodeFields) {
        this.mBarCodeFields = mBarCodeFields;
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
                changeItemUI();
            } else {
                mBinding.tvFieldName.setText(field.getFieldName());
            }
            mBinding.tvFieldValue.setText(field.getFieldValue());
            mBinding.btnCopy.setOnClickListener(v -> {
                Context context = v.getContext();
                CommonUtil.copyToClipBoard(context,
                        mBinding.tvFieldValue.getText());
                Toast.makeText(context, context.getString(R.string.copied),
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
