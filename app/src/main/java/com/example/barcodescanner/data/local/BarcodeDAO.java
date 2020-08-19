package com.example.barcodescanner.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.barcodescanner.data.local.model.BarcodeData;
import com.example.barcodescanner.data.local.model.BarcodeField;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by Trung on 8/12/2020
 */
@Dao
public abstract class BarcodeDAO{
    @Query("select * from BarcodeData")
    abstract public Single<List<RelationBarcodeData>> getAllRelationBarcodeData();

    @Query("select * from BarcodeField")
    abstract public Single<List<BarcodeField>> getAllBarcodeFields();

    @Insert
    abstract public Maybe<Long> insertBarcodeData(BarcodeData barcodeData);

    @Insert
    abstract public void insertBarcodeFields(List<BarcodeField> barcodeFields);

    @Insert
    public Completable insertBarcodeDataWithFields(RelationBarcodeData data) {
        return Completable.create(emitter -> {
            Long barcodeDataId = insertBarcodeData(data.barcodeData).blockingGet();
            for (BarcodeField field : data.barcodeFields) {
                field.setBarcodeDataId(barcodeDataId);
            }
            insertBarcodeFields(data.barcodeFields);
            emitter.onComplete();
        });
    }

    @Delete
    public abstract Completable deleteBarcodeData(BarcodeData barcodeData);
}
