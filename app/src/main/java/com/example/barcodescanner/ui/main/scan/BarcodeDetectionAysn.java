package com.example.barcodescanner.ui.main.scan;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Pair;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

/**
 * Created by Trung on 8/11/2020
 */
class BarcodeDetectionAysn extends AsyncTask<Bitmap, Void,
        Pair<Bitmap, SparseArray<Barcode>>> {

    private final BarcodeDetector mBarcodeDetector;
    private Callback mCallback;

    interface Callback{
        void onBarcodesDetected(Pair<Bitmap, SparseArray<Barcode>> result);
    }

    public BarcodeDetectionAysn(BarcodeDetector barcodeDetector, Callback barcodeDetectCallback) {
        mBarcodeDetector = barcodeDetector;
        mCallback = barcodeDetectCallback;
    }

    @Override
    protected Pair<Bitmap, SparseArray<Barcode>>
        doInBackground(Bitmap... bitmaps) {
        Bitmap bitmap = bitmaps[0];
        Frame frame = new Frame.Builder()
                .setBitmap(bitmap)
                .build();
        return new Pair<>(bitmap, mBarcodeDetector.detect(frame));
    }

    @Override
    protected void onPostExecute(Pair<Bitmap, SparseArray<Barcode>> result) {
        mCallback.onBarcodesDetected(result);
        mCallback = null;
    }
}
