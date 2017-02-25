package com.sm.libs.scene;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sm on 17-2-25.
 */

public class SceneViewPropertyAnimator extends Animator implements Animator.AnimatorListener{
    private static final String TAG = "scene.animator";
    EnumSet<Properties> mPropertiesToSet = EnumSet.noneOf(Properties.class);
    ViewPropertyAnimator mViewPropertyAnimator;
    List<Object> animators;
    View mTarget;
    float mTranslationX;
    float mTranslationY;
    float mScaleX;
    float mScaleY;
    float mRotationY;
    float mRotationX;
    float mRotation;
    float mAlpha;
    long mStartDelay;
    long mDuration;
    TimeInterpolator mInterpolator;
    ArrayList<AnimatorListener> mListeners;
    boolean mRunning = false;
    FirstFrameAnimatorHelper mFirstFrameHelper;
    List<PropertyValuesHolder> holders = new ArrayList(32);

    public SceneViewPropertyAnimator(View mTarget) {
        this.mTarget = mTarget;
        this.mListeners = new ArrayList<>();
    }

    public void addListener(AnimatorListener listener){this.mListeners.add(listener);}

    public void cancel(){
        if (this.animators != null){
            Iterator<Object> i$ = this.animators.iterator();

            while (i$.hasNext()){
                Object animator = i$.next();
                if (animator instanceof Animator){
                    ((Animator) animator).cancel();
                }else if (animator instanceof ViewPropertyAnimator){
                    ((ViewPropertyAnimator) animator).cancel();
                }
            }

            this.animators.clear();
        }

        if (null != this.mViewPropertyAnimator){
            this.mViewPropertyAnimator.cancel();
            this.mViewPropertyAnimator = null;
        }
    }

    public Animator clone(){
        throw new RuntimeException("Not implements");
    }

    public void end(){
        throw new RuntimeException("Not implements");
    }

    @Override
    public ArrayList<AnimatorListener> getListeners() {
        return this.mListeners;
    }

    @Override
    public long getStartDelay() {
        return this.mStartDelay;
    }

    @Override
    public void setStartDelay(long startDelay) {
        this.mPropertiesToSet.add(Properties.START_DELAY);
        this.mStartDelay = startDelay;
    }

    @Override
    public SceneViewPropertyAnimator setDuration(long duration) {
        this.mPropertiesToSet.add(Properties.DURATION);
        this.mDuration = duration;
        return this;
    }

    @Override
    public long getDuration() {
        return this.mDuration;
    }

    @Override
    public void setInterpolator(TimeInterpolator value) {
        this.mPropertiesToSet.add(Properties.INTERPOLATOR);
        this.mInterpolator = value;
    }

    public boolean isStarted(){
        return this.mViewPropertyAnimator != null;
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        this.mFirstFrameHelper.onAnimationStart(animation);

        for (int i=0; i< mListeners.size(); i++){
            AnimatorListener animatorListener = this.mListeners.get(i);
            animatorListener.onAnimationStart(this);
        }

        this.mRunning = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        for (int i=0; i< mListeners.size(); i++){
            AnimatorListener animatorListener = mListeners.get(i);
            animatorListener.onAnimationEnd(this);
        }

        this.mRunning = false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        for (int i=0; i < this.mListeners.size(); i++){
            AnimatorListener animatorListener = this.mListeners.get(i);
            animatorListener.onAnimationCancel(this);
        }

        this.mRunning = false;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        for (int i=0; i< mListeners.size(); i++){
            AnimatorListener animatorListener = this.mListeners.get(i);
            animatorListener.onAnimationRepeat(this);
        }
    }

    @Override
    public void removeAllListeners() {
        this.mListeners.clear();
    }

    public void removeListener(AnimatorListener listener){
        this.mListeners.remove(listener);
    }

    public void setTarget(Object target){throw new RuntimeException("Not implements");}

    public void setupEndValues(){
        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.TRANSLATION_X)) {
            this.mTarget.setTranslationX(this.mTranslationX);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.TRANSLATION_Y)) {
            this.mTarget.setTranslationY(this.mTranslationY);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.SCALE_X)) {
            this.mTarget.setScaleX(this.mScaleX);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.ROTATION_Y)) {
            this.mTarget.setRotationY(this.mRotationY);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.ROTATION_X)) {
            this.mTarget.setRotationX(this.mRotationX);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.ROTATION)) {
            this.mTarget.setRotation(this.mRotation);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.SCALE_Y)) {
            this.mTarget.setScaleY(this.mScaleY);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.ALPHA)) {
            this.mTarget.setAlpha(this.mAlpha);
        }
    }

    public void setupStartValues(){}

    public void start(){
        ViewPropertyAnimator viewPropertyAnimator = this.mTarget.animate();
        String log = null;
        log = "start() starting animation with property map {";
        this.mFirstFrameHelper = new FirstFrameAnimatorHelper(viewPropertyAnimator, this.mTarget);
        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.TRANSLATION_X)) {
            log = log + "transX: " + this.mTranslationX + "; ";
            viewPropertyAnimator.translationX(this.mTranslationX);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.TRANSLATION_Y)) {
            log = log + "transY: " + this.mTranslationY + "; ";
            viewPropertyAnimator.translationY(this.mTranslationY);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.SCALE_X)) {
            log = log + "scaleX: " + this.mScaleX + "; ";
            viewPropertyAnimator.scaleX(this.mScaleX);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.ROTATION_Y)) {
            log = log + "rotationY: " + this.mRotationY + "; ";
            viewPropertyAnimator.rotationY(this.mRotationY);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.ROTATION_X)) {
            log = log + "rotationX: " + this.mRotationX + "; ";
            viewPropertyAnimator.rotationX(this.mRotationX);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.ROTATION)) {
            log = log + "rotation: " + this.mRotation + "; ";
            viewPropertyAnimator.rotation(this.mRotation);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.SCALE_Y)) {
            log = log + "scaleY: " + this.mScaleY + "; ";
            viewPropertyAnimator.scaleY(this.mScaleY);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.ALPHA)) {
            log = log + "alpha: " + this.mAlpha + "; ";
            viewPropertyAnimator.alpha(this.mAlpha);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.START_DELAY)) {
            log = log + "startDelay: " + this.mStartDelay + "; ";
            viewPropertyAnimator.setStartDelay(this.mStartDelay);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.DURATION)) {
            log = log + "duration: " + this.mDuration + "; ";
            viewPropertyAnimator.setDuration(this.mDuration);
        }

        if(this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.INTERPOLATOR)) {
            log = log + "interpolator: " + this.mInterpolator + "; ";
            viewPropertyAnimator.setInterpolator(this.mInterpolator);
        }

        if(Build.VERSION.SDK_INT > 15 && this.mPropertiesToSet.contains(SceneViewPropertyAnimator.Properties.WITH_LAYER)) {
            log = log + "withLayer; ";
            viewPropertyAnimator.withLayer();
        }

        if (null != this.animators){
            this.animators.clear();
        }

        viewPropertyAnimator.setListener(this);
        if (this.holders.isEmpty()){
            log = log + "NO holders";
            this.mViewPropertyAnimator = viewPropertyAnimator;
            this.mViewPropertyAnimator.start();
        }else {
            if (null == this.animators){
                this.animators = new ArrayList<>(32);
            }

            this.animators.add(viewPropertyAnimator);
            int COUNT = this.holders.size();

            for (int i=0; i< COUNT; i++){
                PropertyValuesHolder holder = this.holders.get(i);
                log = log + "property[" + holder.getPropertyName() + "] " + holder + "; ";
                ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this.mTarget, new PropertyValuesHolder[]{holder});
                if (this.mInterpolator != null){
                    objectAnimator.setInterpolator(mInterpolator);
                }

                objectAnimator.setDuration(this.getDuration());
                objectAnimator.setStartDelay(this.getStartDelay());
                this.animators.add(objectAnimator);
                objectAnimator.start();
            }
        }

        Log.d("scene.animator", log + "}");
    }

    public SceneViewPropertyAnimator translationX(float value) {
        this.mPropertiesToSet.add(SceneViewPropertyAnimator.Properties.TRANSLATION_X);
        this.mTranslationX = value;
        return this;
    }

    public SceneViewPropertyAnimator translationY(float value) {
        this.mPropertiesToSet.add(SceneViewPropertyAnimator.Properties.TRANSLATION_Y);
        this.mTranslationY = value;
        return this;
    }

    public SceneViewPropertyAnimator scaleX(float value) {
        this.mPropertiesToSet.add(SceneViewPropertyAnimator.Properties.SCALE_X);
        this.mScaleX = value;
        return this;
    }

    public SceneViewPropertyAnimator scaleY(float value) {
        this.mPropertiesToSet.add(SceneViewPropertyAnimator.Properties.SCALE_Y);
        this.mScaleY = value;
        return this;
    }

    public SceneViewPropertyAnimator rotationY(float value) {
        this.mPropertiesToSet.add(SceneViewPropertyAnimator.Properties.ROTATION_Y);
        this.mRotationY = value;
        return this;
    }

    public SceneViewPropertyAnimator rotationX(float value) {
        this.mPropertiesToSet.add(SceneViewPropertyAnimator.Properties.ROTATION_X);
        this.mRotationX = value;
        return this;
    }

    public SceneViewPropertyAnimator rotation(float value) {
        this.mPropertiesToSet.add(SceneViewPropertyAnimator.Properties.ROTATION);
        this.mRotation = value;
        return this;
    }

    public SceneViewPropertyAnimator alpha(float value) {
        this.mPropertiesToSet.add(SceneViewPropertyAnimator.Properties.ALPHA);
        this.mAlpha = value;
        return this;
    }

    public SceneViewPropertyAnimator withLayer() {
        this.mPropertiesToSet.add(SceneViewPropertyAnimator.Properties.WITH_LAYER);
        return this;
    }

    public SceneViewPropertyAnimator floatProperty(String property, float start, float end) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofFloat(property, new float[]{start, end});
        this.holders.add(holder);
        return this;
    }

    static enum Properties{
        TRANSLATION_X,
        TRANSLATION_Y,
        SCALE_X,
        SCALE_Y,
        ROTATION,
        ROTATION_X,
        ROTATION_Y,
        ALPHA,
        START_DELAY,
        DURATION,
        INTERPOLATOR,
        WITH_LAYER;

        private Properties(){
        }
    }
}
