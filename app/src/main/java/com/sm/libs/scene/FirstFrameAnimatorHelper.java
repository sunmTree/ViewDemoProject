package com.sm.libs.scene;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by sm on 17-2-25.???
 */

public class FirstFrameAnimatorHelper extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
    private static final boolean DEBUG = false;
    private static final int MAX_DELAY = 1000;
    private static final int IDEAL_FRAME_DURATION = 16;
    private View mTarget;
    private long mStartFrame;
    private long mStartTime = -1L;
    private boolean mHandlingOnAnimationUpdate;
    private boolean mAdjustedSecondFrameTime;
    private static long sGlobalFrameCounter;
    private static boolean sVisible;

    public FirstFrameAnimatorHelper(ValueAnimator animator , View mTarget) {
        this.mTarget = mTarget;
        animator.addUpdateListener(this);
    }

    public FirstFrameAnimatorHelper(ViewPropertyAnimator vpa, View mTarget){
        this.mTarget = mTarget;
        vpa.setListener(this);
    }

    public void onAnimationStart(Animator animator){
        ValueAnimator va = (ValueAnimator) animator;
        va.addUpdateListener(this);
        // 这句话什么意思
        this.onAnimationUpdate(va);
    }

    public static void setIsVisible(boolean visible){
        sVisible = visible;
    }

    @Override
    public void onAnimationUpdate(final ValueAnimator animation) {
        long currentTime = System.currentTimeMillis();
        if (this.mStartTime == -1L){
            this.mStartFrame = sGlobalFrameCounter;
            this.mStartTime = currentTime;
        }

        if (!this.mHandlingOnAnimationUpdate && sVisible &&
                animation.getCurrentPlayTime() < animation.getDuration()){
            this.mHandlingOnAnimationUpdate = true;
            long frameNum = sGlobalFrameCounter - this.mStartFrame;
            if (frameNum == 0L && currentTime < this.mStartTime + 1000L){
                this.mTarget.getRootView().invalidate();
                animation.setCurrentPlayTime(0L);
            }else if (frameNum == 1L && currentTime < this.mStartTime + 1000L &&
                    !this.mAdjustedSecondFrameTime && currentTime > this.mStartTime + 16L){
                animation.setCurrentPlayTime(16L);
                this.mAdjustedSecondFrameTime = true;
            }else if (frameNum > 1L){
                this.mTarget.post(new Runnable() {
                    @Override
                    public void run() {
                        animation.removeUpdateListener(FirstFrameAnimatorHelper.this);
                    }
                });
            }

            this.mHandlingOnAnimationUpdate = false;
        }
    }

    public void print(ValueAnimator animator){
        float flatFraction = animator.getCurrentPlayTime() / animator.getDuration();
    }
}
