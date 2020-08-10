package com.example.barcodescanner.data.model;

import android.content.Context;

import com.example.barcodescanner.R;
import com.example.barcodescanner.util.CommonUtil;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.vision.barcode.Barcode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Trung on 8/9/2020
 */
public class BarCodeData {
    String typeName;
    List<BarCodeField> barCodeFields = new ArrayList<>();

    public BarCodeData(String barCodeTypeName, Object data, Context context)
            throws IllegalAccessException {

        typeName = barCodeTypeName;

        /* Barcode.ContactInfo is special class that contains
        *  Barcode.Phone, Barcode.Email ... */
        if (data instanceof Barcode.ContactInfo) {
            parseBarcodeContactInfo((Barcode.ContactInfo) data, context);
        }
        else parseNormalBarcode(data, context);
    }

    private void parseBarcodeContactInfo(Barcode.ContactInfo contactInfo, Context context)
            throws IllegalAccessException {

        barCodeFields.add(new BarCodeField(
            context.getString(R.string.name),
                CommonUtil.compressObjToString(
                        contactInfo.name, context, true)
        ));

        barCodeFields.add(new BarCodeField(
            context.getString(R.string.organization),
                contactInfo.organization
        ));

        barCodeFields.add(new BarCodeField(
            context.getString(R.string.organization),
                CommonUtil.compressObjArrToString(
                        contactInfo.addresses, context, true)
        ));

        barCodeFields.add(new BarCodeField(
            context.getString(R.string.organization),
                CommonUtil.compressObjArrToString(
                        contactInfo.phones, context, true)
        ));

        barCodeFields.add(new BarCodeField(
            context.getString(R.string.organization),
                CommonUtil.joinStrs(", ", ".", contactInfo.urls)
        ));


    }

    private void parseNormalBarcode(Object data, Context context) throws IllegalAccessException {
        barCodeFields.addAll(
                CommonUtil.parseToBarCodeDataFields(data, context, true)
        );
    }

    public static List<BarCodeData> listFromBarcode(Barcode barcode, Context context)
            throws IllegalAccessException {
        List<BarCodeData> barCodeDataList = new ArrayList<>();

        if (barcode.valueFormat == Barcode.TEXT) {
            List<BarCodeField> barCodeFields = Collections.singletonList(
                    new BarCodeField(context.getString(R.string.value), barcode.rawValue));

            barCodeDataList.add(new BarCodeData(
                    context.getString(R.string.text),
                    barCodeFields)
            );
        }
        if (barcode.email != null) barCodeDataList.add(
                new BarCodeData(context.getString(R.string.email), barcode.email, context)
        );
        if (barcode.url != null) barCodeDataList.add(
                new BarCodeData(context.getString(R.string.url), barcode.url, context)
        );
        if (barcode.sms != null) barCodeDataList.add(
                new BarCodeData(context.getString(R.string.sms), barcode.sms, context)
        );
        if (barcode.wifi != null) barCodeDataList.add(
                new BarCodeData(context.getString(R.string.wifi), barcode.wifi, context)
        );
        if (barcode.calendarEvent != null) barCodeDataList.add(
                new BarCodeData(context.getString(R.string.calendarEvent), barcode.calendarEvent, context)
        );
        if (barcode.phone != null) barCodeDataList.add(
                new BarCodeData(context.getString(R.string.phone), barcode.phone, context)
        );
        if (barcode.contactInfo != null) barCodeDataList.add(
                new BarCodeData(context.getString(R.string.contactInfo), barcode.contactInfo, context)
        );
        if (barcode.driverLicense != null) barCodeDataList.add(
                new BarCodeData(context.getString(R.string.driverLicense), barcode.driverLicense, context)
        );

        return barCodeDataList;
    }

    public BarCodeData(String typeName, List<BarCodeField> barCodeFields) {
        this.typeName = typeName;
        this.barCodeFields = barCodeFields;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<BarCodeField> getBarCodeFields() {
        return barCodeFields;
    }

    public void setBarCodeFields(List<BarCodeField> barCodeFields) {
        this.barCodeFields = barCodeFields;
    }

    public static class BarCodeField {
        private String fieldName;
        private String fieldValue;

        public BarCodeField(String fieldName, String fieldValue) {
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }
    }
}
