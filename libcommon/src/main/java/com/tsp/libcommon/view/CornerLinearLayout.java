package com.tsp.libcommon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 圆角类型的 LinearLayout
 * version: 1.0
 */
public class CornerLinearLayout extends LinearLayout {
    public CornerLinearLayout(Context context) {
        this(context, null);
    }

    public CornerLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CornerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        ViewHelper.setViewOutline(this, attrs, defStyleAttr, defStyleRes);
    }
}
