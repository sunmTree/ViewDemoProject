package com.sm.libs.scene;

import android.animation.Animator;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;

/**
 * Created by sm on 17-2-25.
 */

public class ViewScene {
    private static final String TAG = "viewScene.Scene";
    private SparseArray<ViewState> viewStateMap = new SparseArray<>(128);
    private final String name;
    private ViewScene.SceneEventListener sceneEventListener;

    public ViewScene(String name) {
        this.name = name;
    }

    public ViewScene addViewState(ViewState viewState){
        viewState.checkIfValid();
        this.viewStateMap.put(getKey(viewState),viewState);
        return this;
    }

    public Animator generateTransitionAnimator(ViewScene from, boolean transitionFinished){
        Log.d(TAG, "generateTransitionAnimator() called with from = [" + from + "], transitionFinished = [" + transitionFinished + "]");
        SceneViewPropertyAnimatorSet sceneSet = new SceneViewPropertyAnimatorSet();
        ArrayList<Animator> animators = new ArrayList<>(128);
        int COUNT = this.viewStateMap.size();

        for (int i=0; i< COUNT; ++i){
            ViewState viewState = this.viewStateMap.valueAt(i);
            Animator animator = viewState.generateTransitionAnimator(null, transitionFinished);
            animators.add(animator);
        }

        sceneSet.playTogether(animators);
        return sceneSet;
    }

    private static int getKey(ViewState viewState) {
        return viewState.getView().hashCode();
    }

    public String toString() {
        return "Scene{name=\'" + this.name + '\'' + '}';
    }

    public int getKey() {
        return this.name.hashCode();
    }

    public String getName() {
        return this.name;
    }

    public void apply(){

        int COUNT = this.viewStateMap.size();

        for (int i=0; i< COUNT; ++i){
            ViewState viewState = this.viewStateMap.valueAt(i);
            viewState.apply();
        }
    }

    public void checkIfInvalidate(){
        if (this.viewStateMap.size() < 1){
            throw new ShouldNotReachHereException("Buggy! Empty view state map!");
        }
    }

    public void destory(){
        this.viewStateMap.clear();
        this.sceneEventListener = null;
    }

    public SceneEventListener getSceneEventListener() {
        return sceneEventListener;
    }

    public void setSceneEventListener(SceneEventListener sceneEventListener) {
        this.sceneEventListener = sceneEventListener;
    }

    void onLeaving() {
        if(null != this.sceneEventListener) {
            this.sceneEventListener.onSceneLeaving(this);
        }

    }

    void onLeft() {
        if(null != this.sceneEventListener) {
            this.sceneEventListener.onSceneLeft(this);
        }

    }

    void onEntering() {
        if(null != this.sceneEventListener) {
            this.sceneEventListener.onSceneEntering(this);
        }

    }

    void onEntered() {
        if(null != this.sceneEventListener) {
            this.sceneEventListener.onSceneEntered(this);
        }

    }


    public interface SceneEventListener{
        void onSceneEntering(ViewScene var1);

        void onSceneEntered(ViewScene var1);

        void onSceneLeaving(ViewScene var1);

        void onSceneLeft(ViewScene var1);
    }
}
