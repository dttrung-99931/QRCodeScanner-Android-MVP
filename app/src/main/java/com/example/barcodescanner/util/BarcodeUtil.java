package com.example.barcodescanner.util;

import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by Trung on 8/15/2020
 */
public abstract class BarcodeUtil {
    public static String formatPhones(Barcode.Phone[] phones) {
        return format(phones, new BarcodeDataConverter<Barcode.Phone>() {
            @Override
            public String toString(Barcode.Phone phone) {
                return phone.number;
            }

            @Override
            public Barcode.Phone fromString(String str) {
                return null;
            }
        });
    }

    public static String formatUrls(String[] urls) {
        return format(urls, new BarcodeDataConverter<String>() {
            @Override
            public String toString(String s) {
                return s;
            }

            @Override
            public String fromString(String str) {
                return null;
            }
        });
    }

    interface BarcodeDataConverter <TBarcodeData>{
        String toString(TBarcodeData data);
        TBarcodeData fromString(String str);
    }

    public static <TBarcodeData> String format(
            TBarcodeData[] barcodeDataList,
            BarcodeDataConverter<TBarcodeData> converter) {
        StringBuilder builder = new StringBuilder();

        for (TBarcodeData data : barcodeDataList) {
            builder.append(converter.toString(data))
                    .append("\n\n");
        }
        String joinedAddresses = builder.toString();

        if (barcodeDataList.length > 0) {
            // remove last "\n\n"
            joinedAddresses = joinedAddresses
                    .substring(0, joinedAddresses.length()-2);
        }
        return joinedAddresses;
    }

    public static String formatAddresses(Barcode.Address[] addresses) {
        return format(addresses, new BarcodeDataConverter<Barcode.Address>() {
            @Override
            public String toString(Barcode.Address address) {
                return CommonUtil.joinStrs(
                        ", ", "", address.addressLines);
            }

            @Override
            public Barcode.Address fromString(String str) {
                return null;
            }
        });
    }

    public static String formatEmails(Barcode.Email[] emails) {
        return format(emails, new BarcodeDataConverter<Barcode.Email>() {
            @Override
            public String toString(Barcode.Email email) {
                return email.address;
            }

            @Override
            public Barcode.Email fromString(String str) {
                return null;
            }
        });
    }
}
