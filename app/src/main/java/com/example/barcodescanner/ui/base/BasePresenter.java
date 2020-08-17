package com.example.barcodescanner.ui.base;
import com.example.barcodescanner.util.CommonUtil;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BasePresenter<T extends BaseView>{

    private T mView;
    private CompositeDisposable mDisposables;

    public BasePresenter() {
        mDisposables = new CompositeDisposable();
    }

    public void onAttached(T iBaseView) {
        this.mView = iBaseView;
    }

    public void onDetached() {
        mView = null;
        mDisposables.dispose();
        mDisposables = null;
    }

    protected T getView(){
        return mView;
    }

    public void logd(String msg){
        CommonUtil.logd(msg);
    }

    public void loge(String msg){
        CommonUtil.loge(msg);
    }

    // Check if the presenter is attached with a view
    // always use this to check attach state of presenter
    // before update view
    public boolean isAttached(){
        return mView != null;
    }

    protected <TData> Maybe<TData> setupRX(Maybe<TData> maybe) {
        return maybe.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected <TData> Single<TData> setupRX(Single<TData> single) {
        return single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected <TData> Single<TData> setupComputationRX(Single<TData> single) {
        return single.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected Completable setupRX(Completable completable) {
        return completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }

    protected void updateUI(Runnable update){
        if (getView() != null) {
            update.run();
        } else CommonUtil.logd("BasePresenter Update detached View");
    }
}
