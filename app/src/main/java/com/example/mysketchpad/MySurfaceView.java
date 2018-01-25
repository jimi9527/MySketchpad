package com.example.mysketchpad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * author: daxiong9527
 * mail : 15570350453@163.com
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{
    private static final String TAG = "MySurfaceView";
    private SurfaceHolder mSurfaceHolder;
    // 控制绘制开关
    private boolean mStarDraw;
    // 触摸的路径
    private Path mPath;
    private Paint mPaint;
    public MySurfaceView(Context context) {
        super(context);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        // 设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        // 设置常亮
        this.setKeepScreenOn(true);

        mPath = new Path();
        mPaint = new Paint();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mStarDraw = true;
        new Thread(this).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mStarDraw = false;
    }

    @Override
    public void run() {
        while (mStarDraw) {
            draw();
        }
    }

    // 绘制
    private void draw(){
        Log.d(TAG,"draw");
        Canvas canvas = mSurfaceHolder.lockCanvas();
        if(null != canvas){
            canvas.drawColor(Color.WHITE);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(10);
            mPaint.setColor(Color.BLACK);
            canvas.drawPath(mPath,mPaint);

            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"ACTION_DOWN");
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG,"ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"ACTION_MOVE");
                mPath.lineTo(x , y);
                break;

        }

        return true;




    }
}
