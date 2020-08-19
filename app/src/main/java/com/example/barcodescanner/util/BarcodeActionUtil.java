package com.example.barcodescanner.util;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.app.ShareCompat;

import com.example.barcodescanner.R;
import com.example.barcodescanner.data.local.model.BarcodeField;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.ui.base.BaseActivity;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

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
            case Barcode.WIFI: {
                btnAction.setImageResource(R.drawable.ic_wifi);
                btnAction.setOnClickListener(v -> {
                    connectWifi(relBarcodeData, appContext);
                });
                break;
            }

        }

        if (btnImgResId != -1) {
            btnAction.setImageResource(btnImgResId);
            btnAction.setOnClickListener(onClickListener);
        }
    }

    private static void connectWifi(RelationBarcodeData relBarcodeData, Context appContext) {
        WifiConfiguration wifiConfig = createWifiConfig(relBarcodeData);

        // Add wifi config
        WifiManager wifiManager = (WifiManager) appContext
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Turn on wifi
        if (!wifiManager.isWifiEnabled()){
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                wifiManager.setWifiEnabled(true);
                while (!wifiManager.isWifiEnabled()){}
            } else {
                Toast.makeText(appContext, R.string.suggest_turn_on_wifi,
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
        wifiManager.addNetwork(wifiConfig);

        // Active the wifi
        List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration configuration : wifiConfigurations) {
            if (configuration.SSID != null && configuration.SSID.equals(wifiConfig.SSID)){
                wifiManager.disconnect();
                wifiManager.enableNetwork(configuration.networkId, true);
                wifiManager.reconnect();
                openWifiSettings(appContext);
                break;
            }
        }
    }

    private static void openWifiSettings(Context appContext) {
        appContext.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
    }

    private static WifiConfiguration createWifiConfig(RelationBarcodeData relBarcodeData) {
        String ssid = relBarcodeData.getFieldValue(BarcodeField.FIELD_NAME_ID_NAME);
        String password = relBarcodeData.getFieldValue(BarcodeField.FIELD_NAME_ID_PASSWORD);
        int encryptionType = Integer.parseInt(
                relBarcodeData.getFieldValue(BarcodeField.FIELD_NAME_ID_ENSCRYPTION_TYPE)
        );

        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);

        // Setup wifi config
        // WPA or WPA2
        if (encryptionType == 2) {
            wifiConfig.preSharedKey = String.format("\"%s\"", password);
        }
        // WEP
        else if (encryptionType == 3) {
            wifiConfig.wepKeys[0] = String.format("\"%s\"", password);
            wifiConfig.wepTxKeyIndex = 0;
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        }
        // OPEN wifi
        else {
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        return wifiConfig;
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
