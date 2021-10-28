package com.tsp.libcommon.view;

import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.tsp.libcommon.R;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 设置 View 的圆角工具类
 * version: 1.0
 */
public class ViewHelper {
    //四个角都设置成圆角
    public static final int RADIUS_ALL = 0;
    //左边设置成圆角
    public static final int RADIUS_LEFT = 1;
    //上方设置圆角
    public static final int RADIUS_TOP = 2;
    //右边设置圆角
    public static final int RADIUS_RIGHT = 3;
    //底部设置圆角
    public static final int RADIUS_BOTTOM = 4;

    /**
     * 支持配置的属性
     */
    public static void setViewOutline(View view, AttributeSet attributes, int defStyleAttr, int defStyleRes) {
        TypedArray array = view.getContext().obtainStyledAttributes(attributes, R.styleable.viewOutLineStrategy, defStyleAttr, defStyleRes);
        int radius = array.getDimensionPixelSize(R.styleable.viewOutLineStrategy_clip_radius, 0);
        int hideSide = array.getInt(R.styleable.viewOutLineStrategy_clip_side, 0);
        array.recycle();
        setViewOutline(view, radius, hideSide);
    }

    /**
     * 设置 View 的圆角
     * @param owner      被设置的 View
     * @param radius     圆角大小
     * @param radiusSide 圆角位置
     */
    public static void setViewOutline(View owner, int radius, int radiusSide) {
        owner.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int w = view.getWidth(), h = view.getHeight();
                if (w == 0 || h == 0) return;
                if (radiusSide != RADIUS_ALL) {
                    //不是四个角
                    int left = 0, top = 0, right = w, bottom = h;
                    if (radiusSide == RADIUS_LEFT) {
                        //左边切圆角
                        right += radius;
                    } else if (radiusSide == RADIUS_TOP) {
                        bottom += radius;
                    } else if (radiusSide == RADIUS_RIGHT) {
                        left -= radius;
                    } else if (radiusSide == RADIUS_BOTTOM) {
                        bottom -= radius;
                    }
                    outline.setRoundRect(left, top, right, bottom, radius);
                    return;
                }
                int top = 0, bottom = h, left = 0, right = w;
                if (radius <= 0) {
                    outline.setRect(left, top, right, bottom);
                } else {
                    outline.setRoundRect(left, top, right, bottom, radius);
                }
            }
        });
        owner.setClipToOutline(radius > 0);
        owner.invalidate();
    }
}
