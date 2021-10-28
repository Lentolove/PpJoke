package com.tsp.libcommon.utils;

import android.util.DisplayMetrics;

import com.tsp.libcommon.global.AppGlobals;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : dp工具类
 * version: 1.0
 */
public class PixUtils {

    public static int dp2px(int dpValue) {
        DisplayMetrics metrics = AppGlobals.getApplication().getResources().getDisplayMetrics();
        return (int) (metrics.density * dpValue + 0.5f);
    }

    public static int getScreenWidth() {
        DisplayMetrics metrics = AppGlobals.getApplication().getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics metrics = AppGlobals.getApplication().getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }
}
