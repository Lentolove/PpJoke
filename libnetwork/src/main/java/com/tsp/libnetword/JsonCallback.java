package com.tsp.libnetword;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 数据回调接口
 * version: 1.0
 */
public abstract class JsonCallback<T> {

    /**
     * 请求成功
     */
    public void onSuccess(ApiResponse<T> response) {

    }

    /**
     * 请求失败
     */
    public void onError(ApiResponse<T> response) {

    }

    /**
     * 从缓存中获取
     */
    public void onCacheSuccess(ApiResponse<T> response) {

    }
}
