package com.example.barcodescanner.ui.main.result;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.barcodescanner.data.model.BarCodeData;
import com.example.barcodescanner.databinding.FragmentResultBinding;
import com.example.barcodescanner.ui.base.BaseDialogFragment;
import com.example.barcodescanner.ui.main.MainActivity;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

/**
 * Created by Trung on 8/7/2020
 */
public class ResultFragment extends BaseDialogFragment implements ResultPresenter.View{

    FragmentResultBinding mBinding;
    SparseArray<Barcode> mDetectedBarcodes;
    MainActivity.FragmentDismissCallBack mCallBack;
    public static String TAG = "ResultFragment";

    public ResultFragment(
            SparseArray<Barcode> detectedBarcodes,
            MainActivity.FragmentDismissCallBack callBack) {
        mDetectedBarcodes = detectedBarcodes;
        mCallBack = callBack;
    }

    public static void show(
            SparseArray<Barcode> detectedBarcodes,
            FragmentManager fragmentManager,
            MainActivity.FragmentDismissCallBack callBack){
        ResultFragment fragment = new ResultFragment(detectedBarcodes, callBack);
        fragment.show(fragmentManager, TAG);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (mCallBack != null) {
            mCallBack.onFragmentDismiss();
        }
        super.onDismiss(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView();
        return mBinding.getRoot();
    }

    private void bindView() {
        mBinding = FragmentResultBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    private void setupView() {

        try {
            Barcode barcode = mDetectedBarcodes.get(mDetectedBarcodes.keyAt(0));
            List<BarCodeData> barCodeDataList =
                    BarCodeData.listFromBarcode(barcode, requireContext());

            if (barCodeDataList.size() != 0) {
                BarCodeData barCodeData = barCodeDataList.get(0);
                mBinding.tvBarCodeType.setText(barCodeData.getTypeName());
                setupRecyclerViewBarcodeFields(barCodeData);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private BarCodeFieldAdapter mBarCodeFieldAdapter;

    private void setupRecyclerViewBarcodeFields(BarCodeData barCodeData) {
        mBarCodeFieldAdapter = new BarCodeFieldAdapter(
                barCodeData.getBarCodeFields());
        mBinding.recyclerViewBarCodeFields.setAdapter(mBarCodeFieldAdapter);
        mBinding.recyclerViewBarCodeFields.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );
    }
}
