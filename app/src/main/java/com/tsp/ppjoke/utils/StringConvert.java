package com.tsp.ppjoke.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

/**
 * author : shengping.tian
 * time   : 2021/10/28
 * desc   : 用于DataBinding数据转换
 * version: 1.0
 */
public class StringConvert {

    public static String convertFeedUgc(int count) {
        if (count < 10000) {
            return String.valueOf(count);
        }

        return count / 10000 + "万";
    }

    public static String convertTagFeedList(int num) {
        if (num < 10000) {
            return num + "人观看";
        } else {
            return num / 10000 + "万人观看";
        }
    }

    /**
     * 创建富文本的样式
     */
    public static CharSequence convertSpannable(int count, String intro) {
        String countStr = String.valueOf(count);
        SpannableString ss = new SpannableString(countStr + intro);
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, countStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(16, true), 0, countStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, countStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
