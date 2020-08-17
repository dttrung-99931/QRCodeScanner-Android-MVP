package com.example.barcodescanner.util;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.app.ShareCompat;

import com.example.barcodescanner.R;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.ui.base.BaseActivity;
import com.google.android.gms.vision.barcode.Barcode;

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

        switch (relBarcodeData.barcodeData.getTypeId()) {
            case Barcode.URL: {
                btnImgResId = R.drawable.ic_browser;
                onClickListener = v ->
                        BarcodeActionUtil.openBrowser(
                                relBarcodeData.getUrl(),
                                appContext
                        );
                break;
            }
            case Barcode.TEXT: {
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
            case Barcode.PHONE: {
                btnAction.setImageResource(R.drawable.ic_call);
                btnAction.setOnClickListener(v -> {
                    baseActivity.processMakingCall(relBarcodeData.getPhoneNum());
                });
                break;
            }
            case Barcode.EMAIL: {
                btnAction.setImageResource(R.drawable.ic_send_email);
                btnAction.setOnClickListener(v -> {
                    BarcodeActionUtil.sendEmail(relBarcodeData, baseActivity);
                });
                break;
            }
            case Barcode.CONTACT_INFO: {
                btnAction.setImageResource(R.drawable.ic_add_contact);
                btnAction.setOnClickListener(v -> {
                    BarcodeActionUtil.addContact(relBarcodeData, appContext);
                });
                break;
            }
            case Barcode.SMS: {
                btnAction.setImageResource(R.drawable.ic_send_email);
                btnAction.setOnClickListener(v -> {
                    baseActivity.processSendingMsg(relBarcodeData);
                });
                break;
            }

        }

        if (btnImgResId != -1) {
            btnAction.setImageResource(btnImgResId);
            btnAction.setOnClickListener(onClickListener);
        }
    }

    private static void sendEmail(RelationBarcodeData relBarcodeData, BaseActivity baseActivity) {
        String emailAddress = relBarcodeData.getEmailAddress();
        String emailSubject = relBarcodeData.getEmailSubject();
        String emailBody = relBarcodeData.getEmailBody();

        ShareCompat.IntentBuilder.from(baseActivity)
                .setType("message/rfc822")
                .addEmailTo(emailAddress)
                .setSubject(emailSubject)
                .setText(emailBody)
                .setChooserTitle(R.string.send_email)
                .startChooser();
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

        String phoneNum = relBarcodeData.getPhoneNum();
        String name = relBarcodeData.getContactPeopleName();
        String org = relBarcodeData.getOrg();
        String email = relBarcodeData.getContactEmails();

        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNum);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, org);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);

        appContext.startActivity(intent);
    }

    public static void sendSms(RelationBarcodeData relBarcodeData, Context appContext) {
        Uri uri = Uri.parse("smsto:" + relBarcodeData.getSmsPhoneNum());
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", relBarcodeData.getSmsContent());
        appContext.startActivity(intent);
    }
}
