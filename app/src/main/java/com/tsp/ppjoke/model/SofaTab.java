package com.tsp.ppjoke.model;

import java.util.List;

/**
 * author : shengping.tian
 * time   : 2021/10/28
 * desc   : SofaTab 页面导航栏条目实体类
 * version: 1.0
 */

public class SofaTab {

    /**
     * activeSize : 16
     * normalSize : 14
     * activeColor : #ED7282
     * normalColor : #666666
     * select : 0
     * tabGravity : 0
     * tabs : [{"title":"图片","index":0,"tag":"pics","enable":true},{"title":"视频","index":1,"tag":"video","enable":true},{"title":"文本","index":1,"tag":"text","enable":true}]
     */


    public int activeSize;

    public int normalSize;

    public String activeColor;

    public String normalColor;

    public int select;

    public int tabGravity;

    public List<Tabs> tabs;

    public static class Tabs {
        /**
         * title : 图片
         * index : 0
         * tag : pics
         * enable : true
         */
        public String title;
        public int index;
        /**
         * 沙发页面类型
         * pics 图片类型
         * video 视频类型
         * text 文本类型
         */
        public String tag;
        public boolean enable;
    }

}
