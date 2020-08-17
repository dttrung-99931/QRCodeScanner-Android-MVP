package com.example.barcodescanner.data.local.pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Trung on 8/17/2020
 */
public class Pref {
    private static final String KEY_ENABLE_VIBRATION = "KEY_ENABLE_VIBRATION";

    private static SharedPreferences sPref;

    private static Settings sSettings;

    private Pref() {
    }

    /*
     * Call once in Application to init pref
     * */
    public static void init(Context appContext) {
        sPref = appContext.getSharedPreferences("QRApp.sp", Context.MODE_PRIVATE);
        sSettings = new Settings(isVibrationEnabled());
    }

    public static Settings getSettings() {
        return sSettings;
    }

    public static boolean isVibrationEnabled() {
        return sPref.getBoolean(KEY_ENABLE_VIBRATION, true);
    }

    public static void setVibrationEnabled(boolean enabled) {
        sSettings.setVibrationEnabled(enabled);
        sPref.edit()
                .putBoolean(KEY_ENABLE_VIBRATION, enabled)
                .apply();
    }
}
