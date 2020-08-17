package com.example.barcodescanner.data.local.pref;

/**
 * Created by Trung on 8/17/2020
 */
public class Settings {
    private boolean isVibrationEnabled;

    public Settings(boolean isVibrationEnabled) {
        this.isVibrationEnabled = isVibrationEnabled;
    }

    public boolean isVibrationEnabled() {
        return isVibrationEnabled;
    }

    public void setVibrationEnabled(boolean enabled) {
        isVibrationEnabled = enabled;
    }
}
