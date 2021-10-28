package com.tsp.libnetword;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 根据请求地址及请求参数拼接成对应的地址
 * version: 1.0
 */
public class UrlCreator {

    public static String createUrlFromParams(String url, Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        if (url.indexOf("?") > 0 || url.indexOf("&") > 0) {
            builder.append("&");
        } else {
            builder.append("?");
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            try {
                String value = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
                builder.append(entry.getKey()).append("=").append(value).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
