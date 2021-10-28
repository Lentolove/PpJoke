//package com.tsp.libcommon.global
//
//import android.app.Application
//
///**
// *     author : shengping.tian
// *     time   : 2021/10/27
// *     desc   : 通过反射 取全局的 Application
// *     version: 1.0
// */
//object AppGlobals {
//
//    private var mApplication: Application? = null
//
//    @JvmStatic
//    fun getApplication(): Application {
//        if (mApplication == null) {
//            try {
//                mApplication = Class.forName("android.app.ActivityThread")
//                    .getMethod("currentApplication")
//                    .invoke(null) as Application
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        return mApplication!!
//    }
//}