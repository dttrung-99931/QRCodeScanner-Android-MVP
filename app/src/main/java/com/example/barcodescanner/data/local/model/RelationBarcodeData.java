package com.example.barcodescanner.data.local.model;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import com.example.barcodescanner.R;
import com.example.barcodescanner.ui.main.history.BarcodeHistoryAdapter;
import com.example.barcodescanner.util.BarcodeUtil;
import com.example.barcodescanner.util.CommonUtil;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 8/12/2020
 */
/*
 * RelationBarcodeData is a relation container
 * that holds BarcodeData{typeResId} and its fields/
 * */
public class RelationBarcodeData {
    @Embedded
    public BarcodeData barcodeData;

    @Relation(parentColumn = "id", entityColumn = "barcodeDataId")
    public List<BarcodeField> barcodeFields = new ArrayList<>();

    @Ignore
    public int viewType = BarcodeHistoryAdapter.VIEW_TYPE_BARCODE_HISTORY;

    public static final RelationBarcodeData REL_BARCODE_DATA_DATE_DIVIDER =
            new RelationBarcodeData(BarcodeHistoryAdapter.VIEW_TYPE_DATE);

    public RelationBarcodeData(BarcodeData barcodeData, List<BarcodeField> barcodeFields) {
        this.barcodeData = barcodeData;
        this.barcodeFields = barcodeFields;
    }

    public RelationBarcodeData() {
    }

    public RelationBarcodeData(int viewType) {
         this.viewType = viewType;
    }

    public static RelationBarcodeData fromBarcode(Barcode barcode) {
        int barcodeTypeResId = -1;
        List<BarcodeField> barcodeFields = new ArrayList<>();

        switch (barcode.valueFormat) {
            case Barcode.TEXT: {
                barcodeTypeResId = R.string.text;
                barcodeFields.add(
                        new BarcodeField(R.string.text, barcode.rawValue)
                );
                break;
            }
            case Barcode.EMAIL:{
                barcodeTypeResId = R.string.email;
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.email_address, barcode.email.address),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.email_subject, barcode.email.subject),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.email_body, barcode.email.body),
                        true
                );
                break;
            }
            case Barcode.URL: {
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.url, barcode.url.url),
                        true
                );
                barcodeTypeResId = R.string.url;
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.url_title, barcode.url.title),
                        true
                );
                break;
            }
            case Barcode.SMS: {
                barcodeTypeResId = R.string.sms;
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.sms_phone_num, barcode.sms.phoneNumber),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.sms_msg, barcode.sms.message),
                        true
                );
                break;
            }
            case Barcode.WIFI: {
                barcodeTypeResId = R.string.wifi;
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.wifi_name, barcode.wifi.ssid),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.wifi_password, barcode.wifi.password),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.wifi_encryption_type,
                                barcode.wifi.encryptionType + ""),
                        true
                );
                break;
            }
            case Barcode.PRODUCT: {
                barcodeTypeResId = R.string.product;
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.text, barcode.rawValue),
                        true
                );
                break;
            }
            case Barcode.CONTACT_INFO: {
                barcodeTypeResId = R.string.contact;
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.contact_name,
                                barcode.contactInfo.name.formattedName),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.contact_addresses,
                                BarcodeUtil.formatAddresses(barcode.contactInfo.addresses)),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.contact_emails,
                                BarcodeUtil.formatEmails(barcode.contactInfo.emails)),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.contact_organization,
                                barcode.contactInfo.organization),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.contact_title,
                                barcode.contactInfo.title),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.contact_phones,
                                BarcodeUtil.formatPhones(barcode.contactInfo.phones)),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(R.string.contact_urls,
                                BarcodeUtil.formatUrls(barcode.contactInfo.urls)),
                        true
                );
                break;
            }
        }

        if (barcodeTypeResId != -1) return new RelationBarcodeData(
                BarcodeData.createWithNoParent(barcodeTypeResId),
                barcodeFields
        );
        CommonUtil.logd("BarcodeData2.fromBarcode(): Undefined barcode type");
        return null;
    }

    private static void addFieldCheckEmpty(
            List<BarcodeField> barcodeFields,
            BarcodeField field, boolean ignoreEmpty) {
        if (!ignoreEmpty || !field.getFieldValue().isEmpty()) {
            barcodeFields.add(field);
        }
    }
}
