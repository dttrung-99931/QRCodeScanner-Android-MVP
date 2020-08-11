package com.example.barcodescanner.util;

import android.content.res.Resources;
import android.graphics.Rect;
import android.util.SparseArray;

import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by Trung on 8/10/2020
 */
public class ViewUtil {
    public static int sdpToPx(Resources res,  int sdpResId) {
        return res.getDimensionPixelSize(sdpResId);
    }

    public static Barcode getOneDetectedBarCodeInScanArea(SparseArray<Barcode> barcodes, Rect scanArea) {
        for (int i=0; i < barcodes.size(); i++) {
            Barcode barcode = barcodes.get(barcodes.keyAt(i));
            if (scanArea.contains(barcode.getBoundingBox()))
                return barcode;
        }
        return null;
    }
}
