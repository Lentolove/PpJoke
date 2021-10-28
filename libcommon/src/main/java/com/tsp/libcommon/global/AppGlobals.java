package com.tsp.libcommon.global;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;

/**
 * author : shengping.tian
 * time   : 2021/10/28
 * desc   :通过反射 取全局的 Application
 * version: 1.0
 */
public class AppGlobals {

    private static Application sApplication;

    public static Application getApplication() {
        if (sApplication == null) {
            try {
                sApplication = (Application) Class.forName("android.app.ActivityThread")
                        .getMethod("currentApplication")
                        .invoke(null, (Object[]) null);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sApplication;
    }
}
