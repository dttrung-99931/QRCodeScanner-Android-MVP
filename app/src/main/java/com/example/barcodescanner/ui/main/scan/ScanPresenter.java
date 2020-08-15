package com.example.barcodescanner.ui.main.scan;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;
import android.util.SparseArray;

import com.example.barcodescanner.data.local.AppDB;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.ui.base.BasePresenter;
import com.example.barcodescanner.ui.base.BaseView;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import io.reactivex.Completable;

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
    public void initBarcodeDetector(Context appContext) {
        mBarCodeDetector = new BarcodeDetector
                .Builder(appContext)
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

    public void onShowBarcodeResultComplete(RelationBarcodeData relBarcodeData) {
        Completable result = AppDB.barcodeDAO().insertBarcodeDataWithFields(relBarcodeData);
        addDisposable(
                setupRX(result)
                        .subscribe(() -> {
                            getView().notifyRefreshHistoryFragment();
                        })
        );
    }

    public interface View extends BaseView {

        void showBarcodeDetectionResult(Pair<Bitmap, SparseArray<Barcode>> barcodes);

        void notifyRefreshHistoryFragment();
    }
}
