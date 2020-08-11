package com.example.barcodescanner.ui.main.scan;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;
import android.util.SparseArray;

import com.example.barcodescanner.ui.base.BasePresenter;
import com.example.barcodescanner.ui.base.BaseView;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

/**
 * Created by Trung on 8/7/2020
 */
class ScanPresenter extends BasePresenter<ScanPresenter.View> {
    private BarcodeDetector mBarCodeDetector;

    public ScanPresenter() {
    }

    /*
    * Call this function after this.onAttached on view
    * to init mBarCodeDetector
    * */
    public void initBarcodeDetector(Context apllicationContext){
        mBarCodeDetector = new BarcodeDetector
                .Builder(apllicationContext)
                .setBarcodeFormats(
                        com.google.android.gms.vision.barcode.Barcode.DATA_MATRIX
                                | Barcode.QR_CODE
                )
                .build();
    }

    private BarcodeDetectionAysn.Callback barcodeDetectCallback = result -> {
        getView().showBarcodeDetectionResult(result);
    };

    public void detectBarcode(Bitmap bitmap) {
        new BarcodeDetectionAysn(mBarCodeDetector, barcodeDetectCallback)
                .execute(bitmap);
    }

    public interface View extends BaseView {

        void showBarcodeDetectionResult(Pair<Bitmap, SparseArray<Barcode>> barcodes);
    }
}
