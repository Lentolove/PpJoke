package com.tsp.libcommon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 为了解决在切换 fragment 时候，页面topBar延伸到状态栏问题
 * version: 1.0
 */
public class WindowInsetsFrameLayout extends FrameLayout {
    public WindowInsetsFrameLayout(@NonNull Context context) {
        super(context);
    }

    public WindowInsetsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WindowInsetsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WindowInsetsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        requestApplyInsets();
    }

    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        WindowInsets windowInsets = super.dispatchApplyWindowInsets(insets);
        if (!insets.isConsumed()) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                windowInsets = getChildAt(i).dispatchApplyWindowInsets(insets);
            }
        }
        return windowInsets;
    }
}
