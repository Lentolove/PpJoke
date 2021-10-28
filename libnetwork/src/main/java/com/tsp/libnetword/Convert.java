package com.tsp.libnetword;

import java.lang.reflect.Type;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 将服务器反馈的数据转换成 bean 类接口
 * version: 1.0
 */
public interface Convert<T> {
    T convert(String response, Type type);
}
