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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private int mCurColor;
    // 当前画布
    private Canvas mCanvas;
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
        mCurColor = R.color.black;
    }
    // 设置当前颜色
    public void setmCurColor(int color){
        Log.d(TAG,"color:"+color);
        mCurColor = color;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG,"surfaceCreated");
        mStarDraw = true;
        MyPath myPath = new MyPath();
        myPath.setmColor(mCurColor);
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
        while(mStarDraw) {
            if (mAddPath.size() > 0) {
                for (int i = 0; i < mAddPath.size(); i++) {
                    draw(mAddPath.get(i));
                }
            }
        }
    }

    // 绘制
    private void draw(MyPath path){
        Log.d(TAG,"draw");
        mCanvas = mSurfaceHolder.lockCanvas();

        if(null != mCanvas){
            mPaint = new Paint();
            mCanvas.drawColor(Color.WHITE);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(10);
            Log.d(TAG,"path.getmColor():"+path.getmColor());
            mPaint.setColor(getResources().getColor(path.getmColor()));
            mCanvas.drawPath(path.getmPath(),mPaint);
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
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
        MyPath myPath = new MyPath();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"ACTION_DOWN");
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG,"ACTION_UP");
                mPath.lineTo(x,y);
                myPath.setmPath(mPath);
                Log.d(TAG,"mCurColor:"+mCurColor);
                myPath.setmColor(mCurColor);
                mAddPath.add(myPath);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"ACTION_MOVE");
                mPath.lineTo(x , y);
                break;
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
