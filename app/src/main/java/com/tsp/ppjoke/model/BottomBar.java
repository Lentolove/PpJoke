package com.tsp.ppjoke.model;

import java.util.List;

/**
 * author : shengping.tian
 * time   : 2021/10/28
 * desc   : 底部导航栏实体类，采用配置的形式，可通过下发配置动态显示底部导航栏的个数等
 * version: 1.0
 */
public class BottomBar {

    /**
     * 被选择 tab 的颜色
     */
    public String activeColor;
    /**
     * 未被选中时的颜色
     */
    public String inActiveColor;
    /**
     * 导航栏条目集合
     */
    public List<Tab> tabs;

    /**
     * 底部导航栏默认选中项
     */
    public int selectTab;

    /**
     * size : 24
     * enable : true
     * index : 0
     * pageUrl : main/tabs/home
     * title : 首页
     * tintColor : #ff678f
     */
    public static class Tab {
        /**
         * tab 的字体大小
         */
        public int size;
        /**
         * 是否可点击
         */
        public boolean enable;
        /**
         * 对应的底部索引
         */
        public int index;
        /**
         * tab 对应的页面 url ，用于跳转
         */
        public String pageUrl;
        /**
         * tab显示的文本
         */
        public String title;
        /**
         * 提示颜色
         */
        public String tintColor;
    }
}
