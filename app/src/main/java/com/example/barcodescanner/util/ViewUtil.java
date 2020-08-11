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

    public static Barcode getOneDetectedBarCodeInScanArea(
            SparseArray<Barcode> barcodes,
            Rect scanArea,
            float widthScaleFactor,
            float heightScaleFactor) {

        for (int i=0; i < barcodes.size(); i++) {
            Barcode barcode = barcodes.get(barcodes.keyAt(i));
            if (scanArea.contains(
                    scaleRect(barcode.getBoundingBox(), widthScaleFactor, heightScaleFactor)
            ))
                return barcode;
        }
        return null;
    }

    public static Rect scaleRect(Rect rect, float widthFactor, float heightFactor) {
        return new Rect(
                (int) (rect.left * widthFactor),
                (int) (rect.top * heightFactor),
                (int) (rect.right * widthFactor),
                (int) (rect.bottom * heightFactor)
        );
    }
}
