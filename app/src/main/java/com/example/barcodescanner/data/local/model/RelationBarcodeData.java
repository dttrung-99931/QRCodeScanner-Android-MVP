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
        int barcodeType = -1;
        List<BarcodeField> barcodeFields = new ArrayList<>();

        switch (barcode.valueFormat) {
            case Barcode.TEXT: {
                barcodeType = Barcode.TEXT;
                barcodeFields.add(
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_TEXT, barcode.rawValue)
                );
                break;
            }
            case Barcode.EMAIL:{
                barcodeType = Barcode.EMAIL;
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_ADDRESS,
                                barcode.email.address),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_SUBJECT,
                                barcode.email.subject),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_BODY,
                                barcode.email.body),
                        true
                );
                break;
            }
            case Barcode.URL: {
                barcodeType = Barcode.URL;
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_URL, barcode.url.url),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_TITLE,
                                barcode.url.title),
                        true
                );
                break;
            }
            case Barcode.SMS: {
                barcodeType = Barcode.SMS;
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_PHONE_NUM,
                                barcode.sms.phoneNumber),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_MSG, barcode.sms.message),
                        true
                );
                break;
            }
            case Barcode.WIFI: {
                barcodeType = Barcode.WIFI;
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_NAME,
                                barcode.wifi.ssid),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_PASSWORD,
                                barcode.wifi.password),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_ENSCRYPTION_TYPE,
                                barcode.wifi.encryptionType + ""),
                        true
                );
                break;
            }
            case Barcode.PRODUCT: {
                barcodeType = Barcode.PRODUCT;
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_INFO,
                                barcode.rawValue),
                        true
                );
                break;
            }
            case Barcode.PHONE: {
                barcodeType = Barcode.PHONE;
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_PHONE_NUM,
                                barcode.phone.number),
                        true
                );
                break;
            }
            case Barcode.CONTACT_INFO: {
                barcodeType = Barcode.CONTACT_INFO;
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_NAME,
                                barcode.contactInfo.name.formattedName),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_ADDRESSES,
                                BarcodeUtil.formatAddresses(barcode.contactInfo.addresses)),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_ADDRESSES,
                                BarcodeUtil.formatEmails(barcode.contactInfo.emails)),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_ORG,
                                barcode.contactInfo.organization),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_TITLE,
                                barcode.contactInfo.title),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_PHONE_NUMS,
                                BarcodeUtil.formatPhones(barcode.contactInfo.phones)),
                        true
                );
                addFieldCheckEmpty(
                        barcodeFields,
                        new BarcodeField(BarcodeField.FIELD_NAME_ID_URLS,
                                BarcodeUtil.formatUrls(barcode.contactInfo.urls)),
                        true
                );
                break;
            }
        }

        if (barcodeType != -1) return new RelationBarcodeData(
                BarcodeData.createWithNoParent(barcodeType),
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

    public String getUrl() {
        for (BarcodeField field : barcodeFields) {
            if (field.getFieldNameId() == BarcodeField.FIELD_NAME_ID_URL)
                return field.getFieldValue();
        }
        return "";
    }

    public String getRawText() {
        return BarcodeUtil.formatBarcodeFields(
                barcodeFields.toArray(new BarcodeField[0])
        );
    }

    public String getPhoneNum() {
        for (BarcodeField field : barcodeFields) {
            if (field.getFieldNameId() == BarcodeField.FIELD_NAME_ID_PHONE_NUM)
                return field.getFieldValue();
        }
        return "";
    }
}
