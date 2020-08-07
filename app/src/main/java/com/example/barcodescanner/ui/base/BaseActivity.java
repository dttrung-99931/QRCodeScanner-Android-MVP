package com.example.barcodescanner.ui.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.barcodescanner.R;

public class BaseActivity extends AppCompatActivity implements BaseView {

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
}
