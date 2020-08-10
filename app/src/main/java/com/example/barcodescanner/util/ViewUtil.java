package com.example.barcodescanner.util;

import android.content.res.Resources;

/**
 * Created by Trung on 8/10/2020
 */
public class ViewUtil {
    public static int sdpToPx(Resources res,  int sdpResId) {
        return res.getDimensionPixelSize(sdpResId);
    }
}
