package com.sm.libs.scene;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.view.View;

/**
 * Created by sm on 17-2-25.
 */

public class ViewState {
    private static final String TAG = "viewScene.ViewState";
    private final View view;
    private final SceneViewPropertyAnimator animator;
    private long startDelay;

    public ViewState(View view) {
        this.view = view;
        this.animator = new SceneViewPropertyAnimator(view);
    }

    public ViewState setDuration(long duration){
        this.animator.setDuration(duration);
        return this;
    }

    public ViewState setInterpolator(TimeInterpolator value) {
        this.animator.setInterpolator(value);
        return this;
    }

    public ViewState setStartDelay(long startDelay) {
        this.startDelay = startDelay;
        return this;
    }

    public ViewState translationX(float value) {
        this.animator.translationX(value);
        return this;
    }

    public ViewState translationY(float value) {
        this.animator.translationY(value);
        return this;
    }

    public ViewState scaleX(float value) {
        this.animator.scaleX(value);
        return this;
    }

    public ViewState scaleY(float value) {
        this.animator.scaleY(value);
        return this;
    }

    public ViewState rotationY(float value) {
        this.animator.rotationY(value);
        return this;
    }

    public ViewState rotationX(float value) {
        this.animator.rotationX(value);
        return this;
    }

    public ViewState rotation(float value) {
        this.animator.rotation(value);
        return this;
    }

    public ViewState alpha(float value) {
        this.animator.alpha(value);
        return this;
    }

    public ViewState floatProperty(String property, float start, float end) {
        this.animator.floatProperty(property, start, end);
        return this;
    }

    public ViewState withLayer() {
        this.animator.withLayer();
        return this;
    }

    public View getView() {
        return this.view;
    }

    public void apply(){this.animator.setupEndValues();}

    public Animator generateTransitionAnimator(ViewState from, boolean transitionFinished){
        this.animator.setStartDelay(transitionFinished?this.startDelay:0L);
        return this.animator;
    }

    public void checkIfValid(){
        if (this.animator.getDuration() <= 0L){
            this.setDuration(400L);
        }
    }

    public String toString() {
        return "ViewState{view=" + this.view + '}';
    }
}
