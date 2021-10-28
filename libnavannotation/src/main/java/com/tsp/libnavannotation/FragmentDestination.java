package com.tsp.libnavannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 定义 FragmentDestination 路由注解, 用于 Navigation 跳转
 * version: 1.0
 */
@Target(ElementType.TYPE)
public @interface FragmentDestination {
    String pageUrl();

    boolean needLogin() default false;

    boolean asStarter() default false;
}
