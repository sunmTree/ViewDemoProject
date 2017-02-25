package com.sm.libs.scene;

import android.animation.Animator;
import android.util.Log;
import android.util.SparseArray;

/**
 * Created by sm on 17-2-25.
 */

public class ViewSceneManager {
    private static final int STATE_IDLE = 0;
    private static final int STATE_TRANSITIONING = 1;
    private static final String TAG = "viewScene.Manager";
    private final SparseArray<ViewScene> sceneMap = new SparseArray(32);
    private ViewScene currentScene;
    private ViewScene targetScene;
    private Animator currentAnimator;
    private int state = 0;

    public ViewSceneManager() {
    }

    public ViewSceneManager addScene(ViewScene scene){
        scene.checkIfInvalidate();
        int key = getKey(scene);
        ViewScene oldScene = this.sceneMap.get(key);
        if (null != oldScene){
            throw new ShouldNotReachHereException("Duplicated scene with key = " + key + ", OLD = " + oldScene + ", NEW = " + scene);
        }else {
            this.sceneMap.put(key,scene);
            if (this.currentScene == null){
                this.currentScene = scene;
                scene.apply();
            }
        }

        return this;
    }

    public ViewScene getScene(String name){
        return this.sceneMap.get(getKey(name));
    }

    public void beginTransition(String name){
        final ViewScene viewScene = this.sceneMap.get(getKey(name));
        if (null == viewScene){
            throw new ShouldNotReachHereException("Buggy!");
        }else if (viewScene == this.currentScene && this.state == 0){
            Log.w("viewScene.Manager", "beginTransition() current = " + this.currentScene + ", target = " + viewScene + ", state = " + this.state + ", IGNORED A !");
        }else {
            if (viewScene == this.targetScene){
                Log.w("viewScene.Manager", "beginTransition() current = " + this.currentScene + ", target = " + viewScene + ", state = " + this.state + ", IGNORED B !");
            }

            if (viewScene != this.targetScene){
                boolean transitionFinished = !this.stopCurrentAnimator();
                this.targetScene = viewScene;
                this.setState(1);
                this.currentAnimator = viewScene.generateTransitionAnimator(this.currentScene, transitionFinished);
                this.currentAnimator.addListener(new Animator.AnimatorListener() {
                    boolean cancelled;
                    @Override
                    public void onAnimationStart(Animator animation) {
                        this.cancelled = false;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!this.cancelled){
                            if (null != ViewSceneManager.this.currentScene){
                                ViewSceneManager.this.currentScene.onLeft();
                            }

                            ViewSceneManager.this.setCurrentScene(viewScene);
                            viewScene.onEntered();
                            ViewSceneManager.this.targetScene = null;
                            ViewSceneManager.this.setState(0);
                        }else {
                            viewScene.onLeft();
                            if (null != ViewSceneManager.this.currentScene){
                                ViewSceneManager.this.currentScene.onEntered();
                            }
                        }

                        if(ViewSceneManager.this.currentAnimator == animation) {
                            ViewSceneManager.this.currentAnimator = null;
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        this.cancelled = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                this.currentAnimator.start();
                if (transitionFinished){
                    if (null != this.currentScene){
                        this.currentScene.onLeaving();
                    }

                    this.targetScene.onEntering();
                }
            }
        }
    }

    private boolean stopCurrentAnimator() {
        if(this.currentAnimator != null && this.currentAnimator.isStarted()) {
            this.currentAnimator.cancel();
            return true;
        } else {
            return false;
        }
    }

    private void setState(int state) {
        if(state != this.state) {
            Log.i("viewScene.Manager", "setState() NEW = " + state + ", OLD = " + this.state);
            this.state = state;
        }
    }

    private void setCurrentScene(ViewScene scene) {
        if(scene != this.currentScene) {
            Log.i("viewScene.Manager", "setCurrentScene() NEW = " + scene + ", OLD = " + this.currentScene);
            this.currentScene = scene;
        }
    }

    private int getKey(ViewScene scene) {
        return getKey(scene.getName());
    }

    private static int getKey(String name) {
        return name.hashCode();
    }

    public void destroy() {
        int COUNT = this.sceneMap.size();

        for(int i = 0; i < COUNT; ++i) {
            ViewScene scene = (ViewScene)this.sceneMap.valueAt(i);
            scene.destory();
        }

        this.stopCurrentAnimator();
        this.sceneMap.clear();
    }

    public String getCurrentScene() {
        return this.targetScene != null?this.targetScene.getName():this.currentScene.getName();
    }
}
