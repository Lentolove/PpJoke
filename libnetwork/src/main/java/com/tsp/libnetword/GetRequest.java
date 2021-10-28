package com.tsp.libnetword;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : Get请求
 * version: 1.0
 */
public class GetRequest<T> extends Request<T, GetRequest> {
    public GetRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        //get 请求把参数拼接在 url后面
        String url = UrlCreator.createUrlFromParams(mUrl, params);
        okhttp3.Request request = builder.get().url(url).build();
        return request;
    }
}
