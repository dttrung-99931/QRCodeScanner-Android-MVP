package com.example.barcodescanner.ui.base;
import com.example.barcodescanner.util.CommonUtil;

public class BasePresenter<T extends BaseView>{

    private T mView;

    public BasePresenter() {
    }

    public void onAttached(T iBaseView) {
        this.mView = iBaseView;
    }

    public void onDetached() {
        if (mView != null) mView = null;
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
}
