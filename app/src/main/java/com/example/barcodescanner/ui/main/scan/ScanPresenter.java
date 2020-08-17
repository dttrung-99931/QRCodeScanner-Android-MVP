package com.example.barcodescanner.ui.main.scan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Pair;
import android.util.SparseArray;

import com.example.barcodescanner.data.local.AppDB;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.ui.base.BasePresenter;
import com.example.barcodescanner.ui.base.BaseView;
import com.example.barcodescanner.util.BarcodeUtil;
import com.example.barcodescanner.util.CommonUtil;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import io.reactivex.Completable;
import io.reactivex.Single;

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

    public void detectBarcode(Image image) {
        Single<Pair<Bitmap, SparseArray<Barcode>>> result =
                BarcodeUtil.detect(image, mBarCodeDetector);
        addDisposable(
                setupComputationRX(result)
                .subscribe((bitmapSparseArrayPair, throwable) -> {
                    updateUI(() -> getView().showBarcodeDetectionResult(bitmapSparseArrayPair));
                })
        );
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
