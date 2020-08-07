package com.example.barcodescanner.ui.base;

/*
* The abstraction for all IView like IMainActivity, later
* */
public interface BaseView {

    void showToastMsg(String msg);

    void showToastMsg(int stringResId);

    void showProgressDialog();

    void hideProgressDialog();

    BaseActivity getBaseActivity();

    void onErrHappened();
}
