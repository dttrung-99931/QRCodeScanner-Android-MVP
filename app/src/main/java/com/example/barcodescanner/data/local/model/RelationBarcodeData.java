package com.example.barcodescanner.data.local.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.barcodescanner.R;
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

    public RelationBarcodeData(BarcodeData barcodeData, List<BarcodeField> barcodeFields) {
        this.barcodeData = barcodeData;
        this.barcodeFields = barcodeFields;
    }

    public RelationBarcodeData() {
    }

    public static RelationBarcodeData fromBarcode(Barcode barcode) {
        int barcodeTypeResId = -1;
        List<BarcodeField> barcodeFields = new ArrayList<>();

        if (barcode.valueFormat == Barcode.TEXT) {
            barcodeTypeResId = R.string.text;
            barcodeFields.add(
                    new BarcodeField(R.string.text, barcode.rawValue)
            );
        }
        else if (barcode.email != null) {
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
        } else if (barcode.url != null) {
            barcodeTypeResId = R.string.url;
            addFieldCheckEmpty(
                    barcodeFields,
                    new BarcodeField(R.string.url_title, barcode.url.title),
                    true
            );
            addFieldCheckEmpty(
                    barcodeFields,
                    new BarcodeField(R.string.url, barcode.url.url),
                    true
            );
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
