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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    // 当前的颜色
    private int mCurColorId;
    private final Object mDrawWaiter = new Object();
    public MySurfaceView(Context context) {
        super(context);
    }

    private List<MyPath> mAddPath;

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


        mAddPath = new ArrayList<>();
        mPath = new Path();
        // 默认当前颜色是黑色
        mCurColorId = R.color.black;
    }
    // 设置当前颜色
    public void setmCurColorId(int color){
        Log.d(TAG,"color:"+color);
        mCurColorId = color;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG,"surfaceCreated");
        mStarDraw = true;
        MyPath myPath = new MyPath();
        myPath.setmColor(mCurColorId);
        myPath.setmPath(mPath);
        mAddPath.add(myPath);
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
            if (mAddPath.size() > 0) {
                Canvas canvas = mSurfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                for (int i = 0; i < mAddPath.size(); i++) {
                    MyPath myPath = mAddPath.get(i);
                    int color = getResources().getColor(myPath.getmColor());
                    draw(canvas, myPath.getmPath(), color);
                }
                draw(canvas, mPath, getResources().getColor(mCurColorId));
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }

            try {
                synchronized (mDrawWaiter) {
                    mDrawWaiter.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 绘制
    private void draw(Canvas canvas, Path path, int color) {
        Log.d(TAG, "draw");
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        Log.d(TAG, "color: " + color);
        mPaint.setColor(color);
        canvas.drawPath(path, mPaint);
    }

    // 清除
    public void clear(){
        mPath.reset();
    }
    // 撤销
    public void undo(){
        mAddPath.remove(mAddPath.size() - 1);
      /*  if(mAddPath.size() > 0){
            mPath.reset();
            Iterator<Path> iterator = mAddPath.iterator();
            while (iterator.hasNext()){
                Path path = iterator.next();
                draw(path);
            }
        }*/
    }

    //



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"ACTION_DOWN");
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_UP: {
                MyPath myPath = new MyPath();
                Log.d(TAG, "ACTION_UP");
                mPath.lineTo(x, y);
                myPath.setmPath(mPath);
                myPath.setmColor(mCurColorId);
                Log.d(TAG, "mCurColorId:" + mCurColorId);
                mAddPath.add(myPath);
                mPath = new Path();
                break;
            }
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"ACTION_MOVE");
                mPath.lineTo(x , y);
                break;
        }

        synchronized (mDrawWaiter) {
            mDrawWaiter.notify();
        }
        return true;
    }


    class MyPath {
        Path mPath;
        int mColor;

        public Path getmPath() {
            return mPath;
        }

        public void setmPath(Path mPath) {
            this.mPath = mPath;
        }

        public int getmColor() {
            return mColor;
        }

        public void setmColor(int mColor) {
            this.mColor = mColor;
        }
    }

}
