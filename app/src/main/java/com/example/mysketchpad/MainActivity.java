package com.example.mysketchpad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private MySurfaceView mMySurfaceView;
    private LinearLayout mLinearAdd;
    private int[] mColorArray = new int[]{R.color.black,R.color.red,R.color.orange
    ,R.color.yellow,R.color.green,R.color.cyan,R.color.blue,R.color.purple};
    private SeekBar mSeekBar;
    private Button mBtnClear,mBtnEraser,mBtnUndo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mLinearAdd = (LinearLayout) findViewById(R.id.linear_add);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mBtnClear = (Button) findViewById(R.id.clear);
        mBtnEraser = (Button) findViewById(R.id.eraser);
        mBtnUndo = (Button) findViewById(R.id.undo);
        mMySurfaceView = (MySurfaceView) findViewById(R.id.surfaceview);

        mBtnClear.setOnClickListener(this);
        mBtnUndo.setOnClickListener(this);
        mBtnEraser.setOnClickListener(this);
        mLinearAdd.setOnClickListener(this);
        for (int i = 0; i < mColorArray.length; i++){
            final Button mBtn = new Button(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80,80);
            layoutParams.leftMargin = 10;
            layoutParams.topMargin = 10;
            layoutParams.bottomMargin = 10;
            mBtn.setLayoutParams(layoutParams);
            mBtn.setBackground(getResources().getDrawable(mColorArray[i]));
            mBtn.setTag(i);
            mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) mBtn.getTag();
                    Log.d(TAG,"pos:"+pos);
                    mMySurfaceView.setmCurColorId(mColorArray[pos]);
                }
            });
            mLinearAdd.addView(mBtn);
        }

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d(TAG,"seekBar--i:"+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linear_add:
                break;
            case R.id.clear:
                mMySurfaceView.clear();
                break;
            case R.id.eraser:
                break;
            case R.id.undo:
                mMySurfaceView.undo();
                break;
        }
    }
}
