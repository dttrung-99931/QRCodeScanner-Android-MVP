package com.example.barcodescanner.ui.main.history;

import com.example.barcodescanner.App;
import com.example.barcodescanner.data.local.AppDB;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.ui.base.BasePresenter;
import com.example.barcodescanner.ui.base.BaseView;
import com.example.barcodescanner.util.CommonUtil;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Trung on 8/7/2020
 */
class HistoryPresenter extends BasePresenter<HistoryPresenter.View> {


    public void loadBarcodeData() {
        Single<List<RelationBarcodeData>> relBarcodeData =
            AppDB.barcodeDAO().getAllRelationBarcodeData();

        addDisposable(setupRX(relBarcodeData)
                .subscribe((relationBarcodeData, throwable) -> {
                    getView().showBarcodeHistories(relationBarcodeData);
                })
        );

        addDisposable(setupRX(AppDB.barcodeDAO().getAllBarcodeFields())
                .subscribe((barcodeField3s, throwable) ->
                {
                    CommonUtil.logd("Bar code field size "+ barcodeField3s.size());
                })
        );

    }

    public void deleteBarcodeData(RelationBarcodeData relationBarcodeData) {
        Completable deleteResult = AppDB.barcodeDAO()
                .deleteBarcodeData(relationBarcodeData.barcodeData);

        setupRX(deleteResult).subscribe();
    }

    public interface View extends BaseView {
        void showBarcodeHistories(List<RelationBarcodeData> relBarcodeDataList);
    }
}
