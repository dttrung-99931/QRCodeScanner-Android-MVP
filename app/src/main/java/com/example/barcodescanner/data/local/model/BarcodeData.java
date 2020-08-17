package com.example.barcodescanner.data.local.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.barcodescanner.R;
import com.example.barcodescanner.data.local.DateConverter;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Trung on 8/12/2020
 */
@Entity
public class BarcodeData {
    public static int ID_NO_PARENT = -1;

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private int typeId;

    private int barcodeDataParentId;

    @TypeConverters({DateConverter.class})
    private Date createdAt;

    public static BarcodeData createWithNoParent(int type) {
        return new BarcodeData(type, ID_NO_PARENT);
    }

    private BarcodeData(int typeId, int barcodeDataParentId) {
        this.typeId = typeId;
        this.barcodeDataParentId = barcodeDataParentId;
        this.createdAt = Calendar.getInstance().getTime();
    }

    public BarcodeData() {
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTypeStrResId() {
        switch (typeId) {
            case Barcode.TEXT: return R.string.text;
            case Barcode.URL: return R.string.url;
            case Barcode.PHONE: return R.string.phone;
            case Barcode.PRODUCT: return R.string.product;
            case Barcode.CONTACT_INFO: return R.string.contact;
            case Barcode.SMS: return R.string.sms;
            case Barcode.EMAIL: return R.string.email;
        }
        return R.string.unknown_barcode_type;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getBarcodeDataParentId() {
        return barcodeDataParentId;
    }

    public void setBarcodeDataParentId(int barcodeDataParentId) {
        this.barcodeDataParentId = barcodeDataParentId;
    }
}
