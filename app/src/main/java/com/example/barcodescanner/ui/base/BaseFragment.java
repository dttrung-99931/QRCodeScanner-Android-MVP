package com.example.barcodescanner.ui.base;

import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class BaseFragment extends Fragment implements BaseView{
    private BaseActivity mBaseActivity;

    public BaseFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity)
            mBaseActivity = (BaseActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showToastMsg(String msg) {
        mBaseActivity.showToastMsg(msg);
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

    @CallSuper
    public void onErrHappened() {
        mBaseActivity.onErrHappened();
    }

    public void showToastMsg(int stringResId) {
        mBaseActivity.showToastMsg(stringResId);
    }

}
