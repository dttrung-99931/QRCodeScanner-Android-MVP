package com.example.barcodescanner.ui.main.setting;

import com.example.barcodescanner.data.local.pref.Pref;
import com.example.barcodescanner.data.local.pref.Settings;
import com.example.barcodescanner.ui.base.BasePresenter;
import com.example.barcodescanner.ui.base.BaseView;

/**
 * Created by Trung on 8/7/2020
 */
class SettingPresenter extends BasePresenter<SettingPresenter.View> {


    public void setVibrationEnabled(boolean enabled) {
        Pref.setVibrationEnabled(enabled);
    }

    public void loadSettings() {
        getView().showSettings(Pref.getSettings());
    }

    public interface View extends BaseView {

        void showSettings(Settings settings);
    }
}
