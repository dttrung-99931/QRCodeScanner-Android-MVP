package com.example.barcodescanner.ui.main.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter;
import com.example.barcodescanner.R;
import com.example.barcodescanner.data.local.DateConverter;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.databinding.ItemBarcodeHistory2Binding;
import com.example.barcodescanner.ui.base.BaseActivity;
import com.example.barcodescanner.ui.main.base.BaseDragDropViewHolder;
import com.example.barcodescanner.ui.main.result.ResultFragment;
import com.example.barcodescanner.util.BarcodeActionUtil;
import com.example.barcodescanner.util.CommonUtil;

import java.util.Date;

/**
 * Created by Trung on 8/9/2020
 */
public class BarcodeHistoryAdapter2 extends
        DragDropSwipeAdapter<RelationBarcodeData, BaseDragDropViewHolder> {

    private FragmentManager mChildFragmentManager;

    public void setChildFragmentManager(FragmentManager mChildFragmentManager) {
        this.mChildFragmentManager = mChildFragmentManager;
    }

    @NonNull
    @Override
    public BaseDragDropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BarcodeHistoryViewHolder(
                ItemBarcodeHistory2Binding.inflate(
                        LayoutInflater.from(parent.getContext())
                ));
    }

    @Override
    protected View getViewToTouchToStartDraggingItem(RelationBarcodeData relationBarcodeData, BaseDragDropViewHolder baseDragDropViewHolder, int i) {
        return baseDragDropViewHolder.itemView;
    }

    @Override
    protected void onBindViewHolder(RelationBarcodeData relationBarcodeData, BaseDragDropViewHolder baseDragDropViewHolder, int i) {
        baseDragDropViewHolder.bindView(i);
    }

    @Override
    protected BaseDragDropViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    protected void onSwipeAnimationFinished(BaseDragDropViewHolder viewHolder) {
        super.onSwipeAnimationFinished(viewHolder);
    }

    private int restoreRemovedPos = -1;
    private RelationBarcodeData restoreRemovedRelBarcode;

    public void backupRemovedItem(int pos) {
        restoreRemovedPos = pos;
        restoreRemovedRelBarcode = getDataSet().get(pos);
    }

    public void restoreRemovedItem() {
        if (restoreRemovedPos != -1) {
            insertItem(restoreRemovedPos, restoreRemovedRelBarcode);
        }
        if (restoreRemovedPos > 0){
            notifyItemChanged(restoreRemovedPos-1);
        }
        restoreRemovedPos = -1;
    }

    public boolean isRemoveCanceled() {
        return restoreRemovedPos == -1;
    }

    class BarcodeHistoryViewHolder extends BaseDragDropViewHolder {
        ItemBarcodeHistory2Binding mHistoryBinding;

        public BarcodeHistoryViewHolder(ItemBarcodeHistory2Binding binding) {
            super(binding.getRoot());
            mHistoryBinding = binding;
        }

        @Override
        public void bindView(int position) {
            processTvTimeDividerBinding(position);
            RelationBarcodeData relBarcodeData = getDataSet().get(position);

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

        private void processTvTimeDividerBinding(int position) {
            Date date = getDataSet().get(position)
                    .barcodeData.getCreatedAt();

            if (position == getDataSet().size() - 1) {
                bindAndShowTvDate(date);
                return;
            }

            if (position <= getDataSet().size() - 2) {
                Date nearDate = getDataSet().get(position + 1)
                        .barcodeData.getCreatedAt();

                if (!CommonUtil.equalsDate(date, nearDate)) {
                    bindAndShowTvDate(date);
                    return;
                }
            }

            mHistoryBinding.tvDate.setVisibility(View.GONE);
        }

        private void bindAndShowTvDate(Date date) {
            String dateStr;
            if (!CommonUtil.isCurrentDate(date)) {
                dateStr = DateConverter.format(
                        date, "dd/MM/yyyy"
                );
            } else dateStr = itemView.getContext().getString(R.string.today);

            mHistoryBinding.tvDate.setText(dateStr);
            mHistoryBinding.tvDate.setVisibility(View.VISIBLE);
        }

    }

}
