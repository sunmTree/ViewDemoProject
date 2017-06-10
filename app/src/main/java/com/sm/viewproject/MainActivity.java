package com.sm.viewproject;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
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
    private View contentView;

    private static final String LEFT_ANIMATOR = "trans_x_left";
    private static final String RIGHT_ANIMATOR = "trans_x_right";

    private ArrayList<String> arraies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mScrollView = (ScrollView) findViewById(R.id.main_scroll);
        mImageView = (ImageView) findViewById(R.id.main_img);
        contentView = findViewById(R.id.activity_main);
//        initTrans();
        initCompare();

        findViewById(R.id.main_btn_left).setOnClickListener(this);
        findViewById(R.id.main_btn_right).setOnClickListener(this);

        arraies.add("one");
        arraies.add("two");
    }

    private void initCompare() {
        int[] arrays = new int[]{5, 3, 9, 7, 1, 18, 24, 10};
        int count = CompareDemo.countRunAndMakeAscending(arrays, 0, arrays.length);
        Log.d(TAG, " with the count [ " + count + " ]");
    }

    private Bitmap getSuggestBitmap(ImageView imageView, int resourceId) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        int screenW = displayMetrics.widthPixels;
        int screenH = displayMetrics.heightPixels;

        //noinspection ResourceType  android.content.res.Resources$NotFoundException: Resource ID #0xa23e6050
        Drawable drawable = resources.getDrawable(resourceId);
        Bitmap bitmap = Bitmap.createBitmap(screenW / 2, screenH / 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, screenW / 2, screenH / 2);
        drawable.setDither(true);
        drawable.draw(canvas);
        int byteCount = bitmap.getByteCount();
        Bitmap bitmap1 = BitmapFactory.decodeResource(resources, R.mipmap.apus_new_wallpaper_suggest_ori);
        int byteCount1 = bitmap1.getByteCount();
        return bitmap;
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
        switch (v.getId()) {
            case R.id.main_btn_left:
//                mImageView.setImageBitmap(getSuggestBitmap(mImageView,R.mipmap.apus_new_wallpaper_suggest_ori));
                break;
            case R.id.main_btn_right:
                scaleAnim();
//                mImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.apus_new_wallpaper_suggest_ori));
                break;
        }
    }

    private void scaleAnim() {
        final int width = mImageView.getWidth();
        final int height = mImageView.getHeight();
        if (width == 0 || height == 0) {
            return;
        }
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int heightPixels = getResources().getDisplayMetrics().heightPixels;

        float widthScale = widthPixels * 1f / width;
        float heightScale = heightPixels * 1f / height;
        float transX = (widthPixels - width) / 2.0f;
        float transY = (heightPixels - height) / 2.0f;

        float x = mImageView.getX();
        float pivotX = mImageView.getPivotX();

        ValueAnimator animator1 = createAnimFloat(mImageView, 1, widthScale, 0);
        ValueAnimator animator2 = createAnimFloat(mImageView, 1, heightScale, 1);
        ValueAnimator animator3 = createAnimFloat(mImageView, 0, transX, 2);
        ValueAnimator animator4 = createAnimFloat(mImageView, 0, transY, 3);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1, animator2, animator3, animator4);
        set.setDuration(400).setInterpolator(new LinearInterpolator());
        set.start();
    }

    /**
     * @param view
     * @param start
     * @param end
     * @param animType 0 mean scaleX ; 1 mean  scaleY ; 2 mean translateX ; 3 mean translateY
     * @return
     */
    private ValueAnimator createAnimFloat(final View view, float start, float end, final int animType) {
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                switch (animType) {
                    case 0:
                        view.setScaleX(value);
                        break;
                    case 1:
                        view.setScaleY(value);
                        break;
                    case 2:
                        view.setTranslationX(-value);
                        break;
                    case 3:
                        view.setTranslationY(-value);
                        break;
                }
            }
        });
        return animator;
    }


}
