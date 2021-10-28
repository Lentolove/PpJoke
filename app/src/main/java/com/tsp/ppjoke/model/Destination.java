package com.tsp.ppjoke.model;

/**
 * author : shengping.tian
 * time   : 2021/10/28
 * desc   : 页面导航实体类
 * version: 1.0
 */
public class Destination {
    /**
     * 页面的地址
     */
    public String pageUrl;
    /**
     * 页面唯一 ID
     */
    public int id;
    /**
     * 启动该页面是否需要登录权限
     */
    public boolean needLogin;
    /**
     * 是否作为起始页
     */
    public boolean asStarter;
    /**
     * 是否是fragment
     */
    public boolean isFragment;
    /**
     * 页面的全类名
     */
    public String className;
}
