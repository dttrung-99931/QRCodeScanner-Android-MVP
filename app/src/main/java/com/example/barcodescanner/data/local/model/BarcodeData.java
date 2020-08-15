package com.example.barcodescanner.data.local.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.barcodescanner.data.local.DateConverter;

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

    private int typeStrResId;

    private int barcodeDataParentId;

    @TypeConverters({DateConverter.class})
    private Date createdAt;

    public static BarcodeData createWithNoParent(int typeStrResId) {
        return new BarcodeData(typeStrResId, ID_NO_PARENT);
    }

    private BarcodeData(int typeStrResId, int barcodeDataParentId) {
        this.typeStrResId = typeStrResId;
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
        return typeStrResId;
    }

    public void setTypeStrResId(int typeStrResId) {
        this.typeStrResId = typeStrResId;
    }

    public int getBarcodeDataParentId() {
        return barcodeDataParentId;
    }

    public void setBarcodeDataParentId(int barcodeDataParentId) {
        this.barcodeDataParentId = barcodeDataParentId;
    }
}
