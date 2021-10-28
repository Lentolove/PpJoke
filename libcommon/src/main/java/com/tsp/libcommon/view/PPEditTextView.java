package com.tsp.libcommon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : AppCompatEditText 监听backPress事件。以销毁对话框
 * version: 1.0
 */
public class PPEditTextView extends AppCompatEditText {
    private IonBackKeyEvent keyEvent;

    public PPEditTextView(Context context) {
        super(context);
    }

    public PPEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PPEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 复写这个方案 可以在对话框弹框中 ，监听backPress事件。以销毁对话框
     */
    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (keyEvent != null) {
                if (keyEvent.onKeyEvent()) {
                    return true;
                }
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }

    public void setOnBackKeyEventListener(IonBackKeyEvent event) {
        this.keyEvent = event;
    }

    public interface IonBackKeyEvent {
        boolean onKeyEvent();
    }
}
