package com.tsp.ppjoke;

import android.app.Application;

import com.tsp.libnetword.ApiService;

/**
 * author : shengping.tian
 * time   : 2021/10/28
 * desc   :项目在线Api文档地址：http://123.56.232.18:8080/serverdemo/swagger-ui.html#/
 * version: 1.0
 */
public class JokeApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiService.init("http://123.56.232.18:8080/serverdemo", null);
    }
}
