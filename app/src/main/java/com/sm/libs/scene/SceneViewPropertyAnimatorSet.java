package com.sm.libs.scene;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by sm on 17-2-25.
 */

public class SceneViewPropertyAnimatorSet extends Animator implements Animator.AnimatorListener{

    private static final String TAG = "Scene.AnimatorSet";
    private ArrayList<AnimatorListener> mListeners = new ArrayList(32);
    private ValueAnimator playAnimator = new ValueAnimator();
    private Set<Animator> animatorSet = new HashSet(32);
    private long duration;
    private boolean mRunning;

    public SceneViewPropertyAnimatorSet() {
    }

    @Override
    public void addListener(AnimatorListener listener) {
        this.mListeners.add(listener);
    }

    public void cancel(){
        if (this.playAnimator != null){
            this.playAnimator.cancel();
        }
    }

    public void playTogether(Collection<Animator> animators){
        Iterator<Animator> i$ = animators.iterator();

        while (i$.hasNext()){
            Animator animator = i$.next();
            this.playTogether(animator);
        }
    }

    public void playTogether(Animator animator){
        if (! this.animatorSet.contains(animator)){
            long d = animator.getStartDelay() + animator.getDuration();
            if (d > this.duration){
                this.duration = d;
            }

            this.animatorSet.add(animator);
        }
    }

    @Override
    public void end() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ArrayList<AnimatorListener> getListeners() {
        return this.mListeners;
    }

    @Override
    public long getStartDelay() {
        return 0L;
    }

    @Override
    public void setStartDelay(long startDelay) {

    }

    public void setTarget(){throw new RuntimeException("Not implements");}

    @Override
    public Animator setDuration(long duration) {
        return this;
    }

    @Override
    public long getDuration() {
        return this.duration;
    }

    @Override
    public void setInterpolator(TimeInterpolator value) {

    }

    public boolean isStarted(){
        return this.mRunning;
    }

    @Override
    public void removeAllListeners() {
        this.mListeners.clear();
    }

    public void removeListener(AnimatorListener listener){
        this.mListeners.remove(listener);
    }

    @Override
    public boolean isRunning() {
        return this.mRunning;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        Log.d("Scene.AnimatorSet", "onAnimationStart() called with animation = [" + animation + "] + size = " + this.mListeners.size());

        for(int i = 0; i < this.mListeners.size(); ++i) {
            AnimatorListener listener = (AnimatorListener)this.mListeners.get(i);
            listener.onAnimationStart(this);
        }

        this.mRunning = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        for(int i = 0; i < this.mListeners.size(); ++i) {
            AnimatorListener listener = (AnimatorListener)this.mListeners.get(i);
            listener.onAnimationEnd(this);
        }

        this.mRunning = false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        Iterator i = this.animatorSet.iterator();

        while(i.hasNext()) {
            Animator listener = (Animator)i.next();
            listener.cancel();
        }

        for(int var4 = 0; var4 < this.mListeners.size(); ++var4) {
            AnimatorListener var5 = this.mListeners.get(var4);
            var5.onAnimationCancel(this);
        }

        this.mRunning = false;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        for(int i = 0; i < this.mListeners.size(); ++i) {
            AnimatorListener listener = (AnimatorListener)this.mListeners.get(i);
            listener.onAnimationRepeat(this);
        }
    }

    public void setupEndValues(){
        Iterator<Animator> i$ = this.animatorSet.iterator();

        while (i$.hasNext()){
            Animator animator = i$.next();
            animator.setupEndValues();
        }
    }

    public void setupStartValues(){
        Iterator<Animator> i$ = this.animatorSet.iterator();

        while (i$.hasNext()){
            Animator animator = i$.next();
            animator.setupStartValues();
        }
    }

    public void start(){
        if (! this.playAnimator.isStarted()){
            this.playAnimator.setFloatValues(new float[]{0.0f,1.0f});
            this.playAnimator.setDuration(this.duration);
            this.playAnimator.removeAllListeners();
            this.playAnimator.addListener(this);
            this.playAnimator.start();

            Iterator<Animator> i$ = this.animatorSet.iterator();
            while (i$.hasNext()){
                Animator animator = (Animator)i$.next();
                animator.start();
            }
        }
    }

}
