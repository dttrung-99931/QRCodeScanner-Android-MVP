package com.example.barcodescanner.ui.base;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.barcodescanner.R;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.util.BarcodeActionUtil;

public class BaseActivity extends AppCompatActivity implements BaseView {

    public static final int REQUEST_CODE_CALL_PERMISSION = 10;
    private static final int REQUEST_CODE_SMS_PERMISSION = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }


    public void showToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToastMsg(int stringResId) {
        Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show();
    }

    public BaseActivity getBaseActivity() {
        return this;
    }

    @CallSuper
    public void onErrHappened() {
    }

    
    protected void onDestroy() {
        super.onDestroy();
    }

    public  void replaceFragment(int containerId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    public void replaceFragment(int containerId, Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment,tag)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    public void addFragment(int containerId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(containerId, fragment)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    public void addFragment(int containerId, Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .add(containerId, fragment, tag)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    public void backFragment() {
        getSupportFragmentManager().popBackStack();
    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /*
    *  set auto show / hide keyboard when a editext focused.
    * */
    public void setAutoShowHideKeyboardFor(EditText editText) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (hasFocus)
                inputMethodManager.showSoftInput(editText, 0);
            else
                inputMethodManager.hideSoftInputFromWindow(
                    editText.getWindowToken(), 0);
        });
    }


    public void hideKeyboard() {
        View v = getCurrentFocus();
        if (v != null) {
            InputMethodManager manager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(
                    v.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
            );
        }
    }

    public void openBrowser(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        startActivity(intent);
    }

    public AlertDialog createAlertDialog(String msg, DialogInterface.OnClickListener onYes,
                                         DialogInterface.OnClickListener onNo) {
        AlertDialog alertDialog = new AlertDialog
                .Builder(getBaseActivity())
                .create();
        alertDialog.setMessage(msg);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                getString(R.string.yes), onYes);

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                getString(R.string.no), onNo);

        return alertDialog;
    }

    public void showAlertDialog(String msg, DialogInterface.OnClickListener onYes,
                                DialogInterface.OnClickListener onNo) {
        AlertDialog alertDialog = new AlertDialog
                .Builder(getBaseActivity())
                .create();
        alertDialog.setMessage(msg);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                getString(R.string.yes), onYes);

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                getString(R.string.no), onNo);

        alertDialog.show();
    }

    public void showProgressDialog() {

    }

    public void hideProgressDialog() {

    }

    public boolean isPermissionGranted(String permission) {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void processMakingCall(String phoneNum) {
        if (getBaseActivity().isPermissionGranted(Manifest.permission.CALL_PHONE)) {
            makeCall(phoneNum);
        } else {
            getBaseActivity().requestPermission(
                    Manifest.permission.CALL_PHONE,
                    BaseActivity.REQUEST_CODE_CALL_PERMISSION,
                    isGranted -> {
                        if (isGranted) {
                            makeCall(phoneNum);
                        } else showToastMsg(R.string.msg_call_permission_required);
                    }
            );
        }
    }

    private void makeCall(String phoneNum) {
        BarcodeActionUtil.makeCall(
                phoneNum,
                getApplicationContext()
        );
    }

    public void processSendingMsg(RelationBarcodeData relBarcodeData) {
        String smsPermission = Manifest.permission.SEND_SMS;
        if (isPermissionGranted(smsPermission)) {
            BarcodeActionUtil.sendSms(relBarcodeData, getApplicationContext());
        } else requestPermission(smsPermission, REQUEST_CODE_SMS_PERMISSION,
                isGranted -> {
                    if (isGranted)
                        BarcodeActionUtil.sendSms(relBarcodeData, getApplicationContext());
                    else showToastMsg(R.string.msg_send_sms_permission);
                });
    }

    public interface RequestPermissionCallBack {
        public void onResult(boolean isGranted);
    }

    private RequestPermissionCallBack mCurRequestPermissionCallBack;
    private int mCurRequestPermissionCode;

    public void requestPermission(
            String permission, int requestCode,
            RequestPermissionCallBack callBack
    ) {
        mCurRequestPermissionCallBack = callBack;
        mCurRequestPermissionCode = requestCode;
        requestPermissions(new String[]{permission}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mCurRequestPermissionCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCurRequestPermissionCallBack.onResult(true);
            } else {
                mCurRequestPermissionCallBack.onResult(false);
            }
            mCurRequestPermissionCallBack = null;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
