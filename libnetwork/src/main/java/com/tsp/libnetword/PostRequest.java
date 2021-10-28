package com.tsp.libnetword;

import java.util.Map;

import okhttp3.FormBody;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : Post 请求
 * version: 1.0
 */
public class PostRequest<T> extends Request<T,PostRequest> {

    public PostRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        //post请求表单提交
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            bodyBuilder.add(entry.getKey(), String.valueOf(entry.getValue()));
        }
        okhttp3.Request request = builder.url(mUrl).post(bodyBuilder.build()).build();
        return request;
    }
}
