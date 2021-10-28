package com.tsp.libnavannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 定义 ActivityDestination 路由注解, 用于 Navigation 跳转
 * version: 1.0
 */
@Target(ElementType.TYPE)
public @interface ActivityDestination {

    /**
     * 当前页面Activity 路由地址
     */
    String pageUrl();

    /**
     * 进入当前页面是否需要登录
     */
    boolean needLogin() default false;

    /**
     * 是否作为起始页
     */
    boolean asStarter() default false;
}
