package com.example.barcodescanner.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.barcodescanner.util.CommonUtil;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by Trung on 5/29/2020
 */
public class BaseDialogFragment extends DialogFragment implements BaseView{
    private BaseActivity mBaseActivity;
    private boolean isBackgroundTransparent;

    public BaseDialogFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity)
            mBaseActivity = (BaseActivity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        if (mBaseActivity != null) mBaseActivity = null;
        super.onDetach();
    }

    public void showToastMsg(String msg) {
        mBaseActivity.showToastMsg(msg);
    }

    public void showToastMsg(int msgResId) {
        mBaseActivity.showToastMsg(msgResId);
    }

    public void showProgressDialog() {
        getBaseActivity().showProgressDialog();
    }

    public void hideProgressDialog() {
        getBaseActivity().hideProgressDialog();
    }

    public BaseActivity getBaseActivity() {
        return mBaseActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(MATCH_PARENT, MATCH_PARENT);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public boolean isBackgroundTransparent() {
        return isBackgroundTransparent;
    }

    public void setBackgroundTransparent(boolean backgroundTransparent) {
        isBackgroundTransparent = backgroundTransparent;
    }

    // override to ignore Can not perform this action after onSaveInstanceState exception
    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException e){
            CommonUtil.loge("BaseDialogFragment.show " + e.getMessage());
        }
    }

    // override to ignore Can not perform this action after onSaveInstanceState exception
    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (IllegalStateException e){
            CommonUtil.loge("BaseDialogFragment.dismiss " + e.getMessage());
        }
    }

    @CallSuper
    public void onErrHappened() {
        mBaseActivity.onErrHappened();
    }
}
