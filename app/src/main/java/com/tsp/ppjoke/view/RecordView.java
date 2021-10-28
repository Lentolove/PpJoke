package com.tsp.ppjoke.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.tsp.libcommon.utils.PixUtils;
import com.tsp.main.R;


/**
 * author : shengping.tian
 * time   : 2021/10/28
 * desc   : 自定义一个类似于微信的视频拍照按钮，短按拍照，长按录制
 * version: 1.0
 */
public class RecordView extends View implements View.OnLongClickListener, View.OnClickListener {

    /**
     * 每隔 100 ms 更新一次
     */
    private static final int PROGRESS_INTERVAL = 100;

    /**
     * 内部填充画笔
     */
    private final Paint fillPaint;

    /**
     * 圆形进度条画笔
     */
    private final Paint progressPaint;

    /**
     * 进度条最大值
     */
    private int progressMaxValue;

    /**
     * 录制按钮半径
     */
    private final int radius;

    /**
     * 录制按钮进度条宽度
     */
    private final int progressWidth;

    /**
     * 进度条颜色
     */
    private final int progressColor;

    /**
     * 圆形按钮填充色
     */
    private final int fillColor;

    /**
     * 最大录制时长
     */
    private final int maxDuration;

    /**
     * 当前进度之
     */
    private int progressValue;
    /**
     * 是否正在录制
     */
    private boolean isRecording;
    /**
     * 开始录制的时间
     */
    private long startRecordTime;

    /**
     * 回调接口
     */
    private IonRecordListener mListener;


    public RecordView(Context context) {
        this(context, null);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        //解析 styleable 属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordView, defStyleAttr, defStyleRes);
        radius = typedArray.getDimensionPixelOffset(R.styleable.RecordView_radius, 0);
        progressWidth = typedArray.getDimensionPixelOffset(R.styleable.RecordView_progress_width, PixUtils.dp2px(3));
        progressColor = typedArray.getColor(R.styleable.RecordView_progress_color, Color.RED);
        fillColor = typedArray.getColor(R.styleable.RecordView_fill_color, Color.WHITE);
        maxDuration = typedArray.getInteger(R.styleable.RecordView_duration, 10);
        setMaxDuration(maxDuration);
        typedArray.recycle();
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(fillColor);
        fillPaint.setStyle(Paint.Style.FILL);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(progressWidth);

        //处理 View 的触摸事件
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isRecording = true;
                    startRecordTime = System.currentTimeMillis();
                    handler.sendEmptyMessage(0);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    long nowTime = System.currentTimeMillis();
                    if (nowTime - startRecordTime > ViewConfiguration.getLongPressTimeout()) {
                        //则认为此次触摸事件是一次长按事件
                        handler.removeCallbacksAndMessages(null);
                        isRecording = false;
                        startRecordTime = 0;
                        progressValue = 0;
                        postInvalidate();
                    }
                }
                return false;
            }
        });
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if(isRecording){
            //画圆
            canvas.drawCircle(width / 2.0f, height / 2.0f, width / 2.0f, fillPaint);
            int left = progressWidth / 2;
            int top = progressWidth / 2;
            int right = width - left;
            int bottom = height - top;
            //计算弧度值
            float sweepAngle = (progressValue * 1.0f / progressMaxValue) * 360;
            //画外框进度条
            canvas.drawArc(left, top, right, bottom, -90, sweepAngle, false, progressPaint);
        }else {
            canvas.drawCircle(width / 2.0f, height / 2.0f, radius, fillPaint);
        }
    }

    /**
     * 借助 handler 来每隔 100ms 来更新一次视图
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            progressValue++;
            postInvalidate();
            if (progressValue <= progressMaxValue) {
                sendEmptyMessageDelayed(0, PROGRESS_INTERVAL);
            } else {
                finishRecord();
            }
        }
    };

    /**
     * 结束录制
     */
    private void finishRecord() {
        if (mListener != null) {
            mListener.onFinish();
        }
    }

    /**
     * 设置最大时长
     */
    public void setMaxDuration(int maxDuration) {
        this.progressMaxValue = maxDuration * 1000 / PROGRESS_INTERVAL;
    }

    /**
     * 设置监听回调
     */
    public void setOnRecordListener(IonRecordListener listener) {
        mListener = listener;
    }


    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onClick();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mListener != null) {
            mListener.onLongClick();
        }
        return true;
    }

    public interface IonRecordListener {
        void onClick();

        void onLongClick();

        void onFinish();
    }
}
