package com.example.barcodescanner.data.local.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Trung on 8/12/2020
 */

/*
*  Barcode.Phone{type = 1, phoneNumber = 0988202071} is in DB convert to
*  BarcodeField2{
*       id = 1
*       BarcodeField2{
*           id = 2,
*           parentBarcodeFieldId = 1, // The top BarcodeField
*           fieldNameStrResId = R.string.type,
*           filedValue = 0988202071 },
*       BarcodeField2{
*           id = 3,
*           parentBarcodeFieldId = 1, // The top BarcodeField
*           fieldNameStrResId = R.string.type,
*           filedValue = 0988202071 }
* }
* */
@Entity
public class BarcodeField {
    public static final int ID_NO_PARENT = -1;

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long barcodeDataId;
    private int fieldNameStrResId;
    private String fieldValue;

    public BarcodeField() {
    }

    public BarcodeField(int fieldNameStrResId, String fieldValue) {
        this.fieldNameStrResId = fieldNameStrResId;
        this.fieldValue = fieldValue;
    }

    public BarcodeField(Long barcodeDataId, int fieldNameStrResId, String fieldValue) {
        this.barcodeDataId = barcodeDataId;
        this.fieldNameStrResId = fieldNameStrResId;
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
        return fieldNameStrResId;
    }

    public void setFieldNameStrResId(int fieldNameStrResId) {
        this.fieldNameStrResId = fieldNameStrResId;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
