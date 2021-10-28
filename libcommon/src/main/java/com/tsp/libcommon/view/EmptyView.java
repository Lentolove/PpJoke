package com.tsp.libcommon.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tsp.libcommon.R;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 页面无数据时候显示的空布局
 * version: 1.0
 */
public class EmptyView extends LinearLayout {

    /**
     * 空页面显示的图标
     */
    private ImageView icon;

    /**
     * 空页面显示的文本
     */
    private TextView title;

    /**
     * 页面的点击按钮
     */
    private Button action;


    public EmptyView(@NonNull Context context) {
        this(context, null);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int style) {
        super(context, attrs, defStyleAttr, style);
        initView(context);
    }

    /**
     * 加载布局
     */
    private void initView(Context context) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this, true);
        icon = findViewById(R.id.empty_icon);
        title = findViewById(R.id.empty_text);
        action = findViewById(R.id.empty_action);
    }

    /**
     * 设置图标
     */
    public void setEmptyIcon(@DrawableRes int iconRes) {
        icon.setImageResource(iconRes);
    }

    /**
     * 设置标题显示
     */
    public void setTitle(String text) {
        if (TextUtils.isEmpty(text)) {
            title.setVisibility(GONE);
        } else {
            title.setText(text);
            title.setVisibility(VISIBLE);
        }

    }

    /**
     * 设置按钮的点击事件
     */
    public void setButton(String text, OnClickListener listener) {
        if (TextUtils.isEmpty(text)) {
            action.setVisibility(GONE);
        } else {
            action.setText(text);
            action.setVisibility(VISIBLE);
            action.setOnClickListener(listener);
        }
    }
}
