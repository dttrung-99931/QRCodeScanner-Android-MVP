package com.example.barcodescanner.ui.main.result;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.barcodescanner.R;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.databinding.FragmentResultBinding;
import com.example.barcodescanner.ui.base.BaseDialogFragment;
import com.example.barcodescanner.ui.main.MainActivity;
import com.example.barcodescanner.util.BarcodeActionUtil;

/**
 * Created by Trung on 8/7/2020
 */
public class ResultFragment extends BaseDialogFragment implements ResultPresenter.View {

    private FragmentResultBinding mBinding;
    private RelationBarcodeData mRelBarcodeData;
    private MainActivity.FragmentDismissCallBack mCallBack;
    public static String TAG = "ResultFragment";
    private int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

    public ResultFragment(
            RelationBarcodeData relationBarcodeData,
            MainActivity.FragmentDismissCallBack callBack) {
        mRelBarcodeData = relationBarcodeData;
        mCallBack = callBack;
    }

    public ResultFragment(RelationBarcodeData relationBarcodeData) {
        mRelBarcodeData = relationBarcodeData;
    }

    public ResultFragment(RelationBarcodeData relationBarcodeData, int height) {
        this(relationBarcodeData);
        mHeight = height;
    }

    public static void show(
            RelationBarcodeData relationBarcodeData,
            FragmentManager fragmentManager,
            MainActivity.FragmentDismissCallBack callBack) {
        ResultFragment fragment = new ResultFragment(relationBarcodeData, callBack);
        fragment.show(fragmentManager, TAG);
    }

    public static void show(
            RelationBarcodeData relationBarcodeData,
            FragmentManager fragmentManager,
            int width) {
        ResultFragment fragment = new ResultFragment(relationBarcodeData, width);
        fragment.show(fragmentManager, TAG);
    }

    public static void show(
            RelationBarcodeData relationBarcodeData,
            FragmentManager fragmentManager
    ) {
        ResultFragment fragment = new ResultFragment(relationBarcodeData);
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
        changeHeight();
        setupRecyclerViewBarcodeResult();
        mBinding.tvBarCodeType.setText(
                mRelBarcodeData.barcodeData.getTypeStrResId());
        setupBtnAction();
    }

    private void setupBtnAction() {
        ImageButton btnMainAction = mBinding.btnAction1;
        switch (mRelBarcodeData.barcodeData.getTypeStrResId()) {
            case R.string.text:
                mBinding.btnAction1.setVisibility(View.GONE);
                break;
            case R.string.phone:
                setupBtnAddContact();
                break;
        }
        BarcodeActionUtil.setupBtnMainAction(
                btnMainAction,
                mRelBarcodeData,
                getBaseActivity()
        );
    }

    private void setupBtnAddContact() {
        mBinding.btnAction2.setVisibility(View.VISIBLE);
        mBinding.btnAction2.setImageResource(R.drawable.ic_add_contact);
        mBinding.btnAction2.setOnClickListener(v -> {
            BarcodeActionUtil.addContact(mRelBarcodeData,
                    requireContext().getApplicationContext());
        });
    }

    private void changeHeight() {
        ViewGroup.LayoutParams params = mBinding.layoutResult.getLayoutParams();
        if (params.height != mHeight) {
            params.height = mHeight;
            mBinding.layoutResult.setLayoutParams(params);
        }
    }

    private void setupRecyclerViewBarcodeResult() {
        BarcodeResultAdapter mBarCodeResultAdapter = new BarcodeResultAdapter();
        mBarCodeResultAdapter.setRelationBarcodeData(mRelBarcodeData);
        mBinding.recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );
        mBinding.recyclerView.setAdapter(mBarCodeResultAdapter);
    }
}
