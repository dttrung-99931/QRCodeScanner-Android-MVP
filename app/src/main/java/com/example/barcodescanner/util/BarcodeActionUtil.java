package com.example.barcodescanner.util;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.barcodescanner.R;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.ui.base.BaseActivity;

/**
 * Created by Trung on 8/16/2020
 */
public class BarcodeActionUtil {
    public static void openBrowser(String url, Context appContext) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        appContext.startActivity(intent);
    }

    public static void copyToClipBoard(CharSequence text, Context appContext) {
        ClipboardManager clipboard = (ClipboardManager)
                appContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copy", text);
        clipboard.setPrimaryClip(clip);
    }

    public static void setupBtnMainAction(ImageButton btnAction, RelationBarcodeData relBarcodeData, BaseActivity baseActivity) {
        View.OnClickListener onClickListener = null;
        int btnImgResId = -1;
        Context appContext = btnAction.getContext().getApplicationContext();
        switch (relBarcodeData.barcodeData.getTypeStrResId()) {
            case R.string.url: {
                btnImgResId = R.drawable.ic_browser;
                onClickListener = v ->
                        BarcodeActionUtil.openBrowser(
                                relBarcodeData.getUrl(),
                                appContext
                        );
                break;
            }
            case R.string.text: {
                btnImgResId = R.drawable.ic_copy;
                onClickListener = v -> {
                    BarcodeActionUtil.copyToClipBoard(
                            relBarcodeData.getRawText(), appContext
                    );
                    Toast.makeText(appContext, appContext.getString(R.string.copied),
                            Toast.LENGTH_SHORT).show();
                };
                break;
            }
            case R.string.phone: {
                btnAction.setImageResource(R.drawable.ic_call);
                btnAction.setOnClickListener(v -> {
                    baseActivity.processMakingCall(relBarcodeData.getPhoneNum());
                });
            }
        }

        if (btnImgResId != -1) {
            btnAction.setImageResource(btnImgResId);
            btnAction.setOnClickListener(onClickListener);
        }
    }

    @SuppressLint("MissingPermission")
    public static void makeCall(String phoneNum, Context appContext) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNum));
        appContext.startActivity(intent);
    }

    public static void addContact(RelationBarcodeData relBarcodeData,
                                  Context appContext) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, relBarcodeData.getPhoneNum());
        appContext.startActivity(intent);
    }
}
