package com.tsp.libnetword;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 封装统一的服务器返回结果
 * version: 1.0
 */
public class ApiResponse<T> {
    public boolean success;
    public int status;
    public String message;
    public T body;
}
