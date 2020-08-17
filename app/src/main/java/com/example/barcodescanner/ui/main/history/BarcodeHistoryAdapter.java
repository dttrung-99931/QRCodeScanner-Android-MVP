package com.example.barcodescanner.ui.main.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodescanner.R;
import com.example.barcodescanner.data.local.DateConverter;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.databinding.ItemBarcodeHistoryBinding;
import com.example.barcodescanner.databinding.ItemDateBinding;
import com.example.barcodescanner.ui.base.BaseActivity;
import com.example.barcodescanner.ui.base.BaseViewHolder;
import com.example.barcodescanner.ui.main.result.ResultFragment;
import com.example.barcodescanner.util.BarcodeActionUtil;
import com.example.barcodescanner.util.CommonUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Trung on 8/9/2020
 */
public class BarcodeHistoryAdapter extends
        RecyclerView.Adapter<BaseViewHolder> {
    public static final int VIEW_TYPE_BARCODE_HISTORY = 1;
    public static final int VIEW_TYPE_DATE = 2;

    private List<RelationBarcodeData> mRelBarcodeDataList = new ArrayList<>();
    private FragmentManager mChildFragmentManager;

    public void setRelBarcodeDataList(List<RelationBarcodeData> mRelBarcodeDataList) {
        this.mRelBarcodeDataList = mRelBarcodeDataList;
        insertRelBarcodeDataDateDivider();
    }

    private void insertRelBarcodeDataDateDivider() {
        for (int i = 0; i <= mRelBarcodeDataList.size()-2; i++) {
            Date date = mRelBarcodeDataList.get(i)
                    .barcodeData.getCreatedAt();
            Date nearDate = mRelBarcodeDataList.get(i + 1)
                    .barcodeData.getCreatedAt();

            if (!CommonUtil.equalsDate(date, nearDate)) {
                mRelBarcodeDataList.add(i+1,
                        RelationBarcodeData.REL_BARCODE_DATA_DATE_DIVIDER);
                i++;
            }
        }

        if (mRelBarcodeDataList.size() != 0) {
            mRelBarcodeDataList.add(mRelBarcodeDataList.size(),
                    RelationBarcodeData.REL_BARCODE_DATA_DATE_DIVIDER);
        }
    }

    public void setChildFragmentManager(FragmentManager mChildFragmentManager) {
        this.mChildFragmentManager = mChildFragmentManager;
    }

    @Override
    public int getItemViewType(int position) {
        return mRelBarcodeDataList.get(position).viewType;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_BARCODE_HISTORY) {
            return new BarcodeHistoryViewHolder(
                    ItemBarcodeHistoryBinding.inflate(
                            LayoutInflater.from(parent.getContext())
                    ));
        }
        return new DateDividerViewHolder(
                ItemDateBinding.inflate(
                        LayoutInflater.from(parent.getContext())
                ));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mRelBarcodeDataList.size();
    }

    class DateDividerViewHolder extends BaseViewHolder {
        private final ItemDateBinding mDateBinding;

        public DateDividerViewHolder(ItemDateBinding binding) {
            super(binding.getRoot());
            mDateBinding = binding;
        }

        @Override
        public void bindView(int position) {
            RelationBarcodeData nextData = mRelBarcodeDataList.get(position-1);

            String dateStr;
            Date nextDate = nextData.barcodeData.getCreatedAt();
            if (!CommonUtil.isCurrentDate(nextDate)) {
                dateStr = DateConverter.format(
                        nextData.barcodeData.getCreatedAt(),
                        "dd/MM/yyyy"
                );
            } else dateStr = itemView.getContext().getString(R.string.today);

            mDateBinding.tvDate.setText(dateStr);
        }
    }

    class BarcodeHistoryViewHolder extends BaseViewHolder {
        ItemBarcodeHistoryBinding mHistoryBinding;

        public BarcodeHistoryViewHolder(ItemBarcodeHistoryBinding binding) {
            super(binding.getRoot());
            mHistoryBinding = binding;
        }

        @Override
        public void bindView(int position) {
            RelationBarcodeData relBarcodeData = mRelBarcodeDataList.get(position);
            mHistoryBinding.tvFieldName.setText(
                    relBarcodeData.barcodeData.getTypeStrResId()
            );
            if (relBarcodeData.barcodeFields.size() > 0) {
                mHistoryBinding.tvFieldValue.setText(
                        relBarcodeData.barcodeFields.get(0).getFieldValue()
                );
            }
            mHistoryBinding.tvTime.setText(
                    DateConverter.format(
                            relBarcodeData.barcodeData.getCreatedAt(),
                            "HH:mm")
            );
            mHistoryBinding.layoutCardViewResult.setOnClickListener(v -> {
                ResultFragment.show(relBarcodeData, mChildFragmentManager);
            });

            /*Click event of mBinding.tvFieldValue not passed to mBinding.layoutCardViewResult
             * So it must be set onclick individually*/
            mHistoryBinding.tvFieldValue.setOnClickListener(v -> {
                ResultFragment.show(relBarcodeData, mChildFragmentManager);
            });

            BarcodeActionUtil.setupBtnMainAction(
                    mHistoryBinding.btnAction1,
                    relBarcodeData,
                    (BaseActivity) itemView.getContext());
        }

    }

}
