package com.example.barcodescanner.data.local.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.barcodescanner.R;

/**
 * Created by Trung on 8/12/2020
 */

@Entity
public class BarcodeField {
    public static final int ID_NO_PARENT = -1;
    public static final int FIELD_NAME_ID_TEXT = 1;

    public static final int FIELD_NAME_ID_ADDRESS = 2;
    public static final int FIELD_NAME_ID_SUBJECT = 3;
    public static final int FIELD_NAME_ID_BODY = 4;

    public static final int FIELD_NAME_ID_URL = 5;
    public static final int FIELD_NAME_ID_TITLE = 6;
    public static final int FIELD_NAME_ID_PHONE_NUM = 7;
    public static final int FIELD_NAME_ID_MSG = 8;
    public static final int FIELD_NAME_ID_NAME = 9;
    public static final int FIELD_NAME_ID_PASSWORD = 10;
    public static final int FIELD_NAME_ID_ENSCRYPTION_TYPE = 11;
    public static final int FIELD_NAME_ID_ADDRESSES = 12;
    public static final int FIELD_NAME_ID_ORG = 13;
    public static final int FIELD_NAME_ID_PHONE_NUMS = 14;
    public static final int FIELD_NAME_ID_URLS = 16;
    public static final int FIELD_NAME_ID_INFO = 17;

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long barcodeDataId;
    private int fieldNameId;
    private String fieldValue;

    public BarcodeField() {
    }

    public BarcodeField(int fieldNameId, String fieldValue) {
        this.fieldNameId = fieldNameId;
        this.fieldValue = fieldValue;
    }

    public BarcodeField(Long barcodeDataId, int fieldNameId, String fieldValue) {
        this.barcodeDataId = barcodeDataId;
        this.fieldNameId = fieldNameId;
        this.fieldValue = fieldValue;
    }

    public static int getIdNoParent() {
        return ID_NO_PARENT;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBarcodeDataId() {
        return barcodeDataId;
    }

    public void setBarcodeDataId(Long barcodeDataId) {
        this.barcodeDataId = barcodeDataId;
    }

    public int getFieldNameStrResId() {
        switch (fieldNameId) {
            case FIELD_NAME_ID_TEXT: return R.string.text;
            case FIELD_NAME_ID_URL: return R.string.url;
            case FIELD_NAME_ID_NAME: return R.string._name;
            case FIELD_NAME_ID_ADDRESS: return R.string.address;
            case FIELD_NAME_ID_SUBJECT: return R.string.subject;
            case FIELD_NAME_ID_BODY: return R.string.body;
            case FIELD_NAME_ID_TITLE: return R.string.title;
            case FIELD_NAME_ID_PHONE_NUM: return R.string.phone_num;
            case FIELD_NAME_ID_MSG: return R.string.msg;
            case FIELD_NAME_ID_PASSWORD: return R.string.password;
            case FIELD_NAME_ID_ENSCRYPTION_TYPE: return R.string.encryption_type;
            case FIELD_NAME_ID_ADDRESSES: return R.string.addresses;
            case FIELD_NAME_ID_ORG: return R.string.organization;
            case FIELD_NAME_ID_PHONE_NUMS: return R.string.phone_nums;
            case FIELD_NAME_ID_URLS: return R.string.urls;
            case FIELD_NAME_ID_INFO: return R.string.information;
        }
        return R.string.unknown;
    }

    public int getFieldNameId() {
        return fieldNameId;
    }

    public void setFieldNameId(int fieldNameId) {
        this.fieldNameId = fieldNameId;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
