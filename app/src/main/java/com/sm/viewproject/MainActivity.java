package com.sm.viewproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.sm.java.CompareDemo;
import com.sm.libs.scene.ViewScene;
import com.sm.libs.scene.ViewSceneManager;
import com.sm.libs.scene.ViewState;
import com.sm.scroll.ScrollView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private ScrollView mScrollView;
    private ViewSceneManager mViewSceneManager;
    private ImageView mImageView;

    private static final String LEFT_ANIMATOR = "trans_x_left";
    private static final String RIGHT_ANIMATOR = "trans_x_right";

    private ArrayList<String> arraies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScrollView = (ScrollView) findViewById(R.id.main_scroll);
        mImageView = (ImageView) findViewById(R.id.main_img);

        initTrans();
        initCompare();

        findViewById(R.id.main_btn_left).setOnClickListener(this);
        findViewById(R.id.main_btn_right).setOnClickListener(this);

        arraies.add("one");
        arraies.add("two");
    }

    private void initCompare() {
        int[] arrays = new int[]{5,3,9,7,1,18,24,10};
        int count = CompareDemo.countRunAndMakeAscending(arrays, 0, arrays.length);
        Log.d(TAG, " with the count [ "+count+" ]");
    }

    private void initTrans() {
        mViewSceneManager = new ViewSceneManager();
        {
            ViewScene leftScene = new ViewScene(LEFT_ANIMATOR);
            ViewState leftState = new ViewState(mImageView);
            leftState.setDuration(2000);
//            leftState.translationX(400);
            leftState.alpha(1);
            leftState.rotation(30);
            leftState.setStartDelay(2000);
            leftScene.addViewState(leftState);
            mViewSceneManager.addScene(leftScene);
        }
        {
            ViewScene rightScene = new ViewScene(RIGHT_ANIMATOR);
            ViewState rightState = new ViewState(mImageView);
            rightState.setDuration(200);
//            rightState.translationX(-400);
            rightState.alpha(1);
            rightState.rotation(-30);
            rightState.setStartDelay(200);
            rightScene.addViewState(rightState);
            mViewSceneManager.addScene(rightScene);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_btn_left:
                if (mViewSceneManager != null && mViewSceneManager.getScene(LEFT_ANIMATOR) != null)
                mViewSceneManager.beginTransition(LEFT_ANIMATOR);
                break;
            case R.id.main_btn_right:
                if (mViewSceneManager != null && mViewSceneManager.getScene(RIGHT_ANIMATOR) != null)
                mViewSceneManager.beginTransition(RIGHT_ANIMATOR);
                break;
        }
    }


}
