package com.sm.libs.receive;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.os.Build;
import android.util.Log;

import java.util.List;

/**
 * Created by michael on 17-2-24.
 */

public class BadTokenExceptionHandler {
    // crash log api <= 16
    /*
    android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@41830288 is not valid; is your activity running?
    at android.view.ViewRootImpl.setView(ViewRootImpl.java:683)
    at android.view.WindowManagerImpl.addView(WindowManagerImpl.java:301)
    at android.view.WindowManagerImpl.addView(WindowManagerImpl.java:215)
    at android.view.WindowManagerImpl$CompatModeWrapper.addView(WindowManagerImpl.java:140)
    at android.view.Window$LocalWindowManager.addView(Window.java:537)
    at android.app.ActivityThread.handleResumeActivity(ActivityThread.java:2521)
    at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2000)
    at android.app.ActivityThread.access$600(ActivityThread.java:128)
    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1161)
    at android.os.Handler.dispatchMessage(Handler.java:99)
    at android.os.Looper.loop(Looper.java:137)
    at android.app.ActivityThread.main(ActivityThread.java:4517)
    at java.lang.reflect.Method.invokeNative(Native Method)
    at java.lang.reflect.Method.invoke(Method.java:511)
    at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:993)
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:760)
    at dalvik.system.NativeStart.main(Native Method)
    */
    // crash log api > 16 && api <= 25
    /*
    android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@429750c8 is not valid; is your activity running?
    at android.view.ViewRootImpl.setView(ViewRootImpl.java:591)
    at android.view.WindowManagerGlobal.addView(WindowManagerGlobal.java:259)
    at android.view.WindowManagerImpl.addView(WindowManagerImpl.java:69)
    at android.app.ActivityThread.handleResumeActivity(ActivityThread.java:3120)
    at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2498)
    at android.app.ActivityThread.access$800(ActivityThread.java:166)
    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1283)
    at android.os.Handler.dispatchMessage(Handler.java:102)
    at android.os.Looper.loop(Looper.java:136)
    at android.app.ActivityThread.main(ActivityThread.java:5584)
    at java.lang.reflect.Method.invokeNative(Native Method)
    at java.lang.reflect.Method.invoke(Method.java:515)
    at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:1268)
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1084)
    at dalvik.system.NativeStart.main(Native Method)
    */
    public static void checkBadTokenException(Activity activity) {
        if (activity == null) return;
//        dumpMyRunningTaskInfo(activity);
//        DebugTime.start("isInRunningTask");
        boolean isRunning = isInRunningTask(activity);
//        DebugTime.stop("isInRunningTask");
//        Log.d("BadToken", "isRunning = " + isRunning);

        if (isRunning) {
//            Log.d("BadToken", "current is running, continue");
            // continue
        } else {
//            Log.d("BadToken", "current activity is not on running, maybe system has collected it, so handle BadTokenException !!!!!!!!!!!!!!!!");
            // I have be system gc, so handle BadToken
            handleBadTokenException(activity);
        }
    }

    // framework source
    /*
    final Activity a = r.activity;

    if (localLOGV) Slog.v(
    TAG, "Resume " + r + " started activity: " +
    a.mStartedActivity + ", hideForNow: " + r.hideForNow
    + ", finished: " + a.mFinished);

    final int forwardBit = isForward ?
            WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION : 0;

    // If the window hasn't yet been added to the window manager,
    // and this guy didn't finish itself or start another activity,
    // then go ahead and add the window.
    boolean willBeVisible = !a.mStartedActivity;
    if (!willBeVisible) {
        try {
            willBeVisible = ActivityManagerNative.getDefault().willActivityBeVisible(
                    a.getActivityToken());
        } catch (RemoteException e) {
        }
    }
    if (r.window == null && !a.mFinished && willBeVisible) {
        r.window = r.activity.getWindow();
        View decor = r.window.getDecorView();
        decor.setVisibility(View.INVISIBLE);
        ViewManager wm = a.getWindowManager();
        WindowManager.LayoutParams l = r.window.getAttributes();
        a.mDecor = decor;
        l.type = WindowManager.LayoutParams.TYPE_BASE_APPLICATION;
        l.softInputMode |= forwardBit;
        if (a.mVisibleFromClient) {
            a.mWindowAdded = true;
            wm.addView(decor, l);
        }

        // If the window has already been added, but during resume
        // we started another activity, then don't yet make the
        // window visible.
    } else if (!willBeVisible) {
        if (localLOGV) Slog.v(
                TAG, "Launch " + r + " mStartedActivity set");
        r.hideForNow = true;
    }
    */
    private static void handleBadTokenException(Activity activity) {
        if (activity == null) return;

//        Log.d("BadToken", "handleBadTokenException");
        // TODO upload 2 server, handleBadTokenException is available
        // activity api
        /*
        a.mFinished                 reflect
        a.mStartedActivity          startActivity
        a.mVisibleFromClient        setVisible
        */
//        Log.d("BadToken", "activity.setVisible(false);");
        activity.setVisible(false);
        activity.finish();
    }

    private static final int TASKS_MAX_NUM = 99;
    private static boolean isInRunningTask(Activity activity) {
        if (activity == null) {
            return false;
        }

        // TODO check running tasks size that api < 21
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(TASKS_MAX_NUM);
        int size = runningTasks.size();
//        Log.d("BadToken", "------------------------------------------------------------------------------");
//        Log.d("BadToken", "runningTasks size = " + size);
        if (size > 0 && size <= TASKS_MAX_NUM) {
            for (int i = 0; i < size; i++) {
//                Log.d("BadToken", "----------");
                ActivityManager.RunningTaskInfo runningTask = runningTasks.get(i);
                if (runningTask != null) {
//                    dumpRunningTaskInfo(runningTask);
                    // component = current component, return true
                    ComponentName currentActivity = activity.getComponentName();
                    ComponentName baseActivity = runningTask.baseActivity;
                    ComponentName topActivity = runningTask.topActivity;
                    // compare base activity
                    if (currentActivity != null && baseActivity != null) {
                        String currentPackageName = currentActivity.getPackageName();
                        String basePackageName = baseActivity.getPackageName();
//                        Log.d("BadToken", "currentPackageName = " + currentPackageName);
//                        Log.d("BadToken", "basePackageName = " + basePackageName);
                        if (currentPackageName != null && basePackageName != null && currentPackageName.equals(basePackageName)) {
                            String currentClassName = currentActivity.getClassName();
                            String baseClassName = baseActivity.getClassName();
//                            Log.d("BadToken", "currentClassName = " + currentClassName);
//                            Log.d("BadToken", "baseClassName = " + baseClassName);
                            if (currentClassName != null && baseClassName != null && currentClassName.equals(baseClassName)) {
//                                Log.d("BadToken", "current base pkg equals, clz equals also......");
//                                Log.d("BadToken", "------------------------------------------------------------------------------");
                                return true;
                            } else {
//                                Log.d("BadToken", "current base pkg equals, but clz not equals!!!!!!");
                            }
                        }
                    }
                    // compare top activity
                    if (currentActivity != null && topActivity != null) {
                        String currentPkgName = currentActivity.getPackageName();
                        String topPkgName = topActivity.getPackageName();
//                        Log.d("BadToken", "currentPkgName = " + currentPkgName);
//                        Log.d("BadToken", "topPkgName = " + topPkgName);
                        if (currentPkgName != null && topPkgName != null && currentPkgName.equals(topPkgName)) {
                            String currentClzName = currentActivity.getClassName();
                            String topClzName = topActivity.getClassName();
//                            Log.d("BadToken", "currentClzName = " + currentClzName);
//                            Log.d("BadToken", "topClzName = " + topClzName);
                            if (currentClzName != null && topClzName != null && currentClzName.equals(topClzName)) {
//                                Log.d("BadToken", "current top pkg equals, clz equals also......");
//                                Log.d("BadToken", "------------------------------------------------------------------------------");
                                return true;
                            } else {
//                                Log.d("BadToken", "current top pkg equals, but clz not equals!!!!!!");
                            }
                        }
                    }
                }
            }
        }
//        Log.d("BadToken", "current activity is not in my running task top!!!!!!");
//        Log.d("BadToken", "------------------------------------------------------------------------------");
        return false;
    }

    /*
    // TODO only for test will be remove
    private static final int DEFAULT_BLOCK_MILLIS = 30 * 1000;

    public static void blockUI(String tag) {
        blockUI(tag, DEFAULT_BLOCK_MILLIS);
    }

    public static void blockUI(String tag, long timeMillis) {
        try {
            Log.d("BadToken", tag + " start block UI " + timeMillis + " millis");
            Thread.sleep(timeMillis);
            Log.d("BadToken", tag + " after block UI " + timeMillis + " millis");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    */

    /*
    private static void dumpRunningTaskInfo(ActivityManager.RunningTaskInfo runningTask) {
//        Log.d("BadToken", "----------");
        int id = runningTask.id;
        CharSequence description = runningTask.description;
        int numRunning = runningTask.numRunning;
        int numActivities = runningTask.numActivities;
        ComponentName baseActivity = runningTask.baseActivity;
        ComponentName topActivity = runningTask.topActivity;
        *//*
        Log.d("BadToken", "id = " + id);
        Log.d("BadToken", "description = " + description);
        Log.d("BadToken", "numRunning = " + numRunning);
        Log.d("BadToken", "numActivities = " + numActivities);
        Log.d("BadToken", "baseActivity = " + baseActivity);
        Log.d("BadToken", "topActivity = " + topActivity);
        *//*
    }
    */

    /*
    private static void dumpMyRunningTaskInfo(Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(TASKS_MAX_NUM);
        int size = runningTasks.size();
//        Log.d("BadToken", "------------------------------------------------------------------------------");
//        Log.d("BadToken", "runningTasks size = " + size);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                ActivityManager.RunningTaskInfo runningTask = runningTasks.get(i);
                if (runningTask != null) {
//                    dumpRunningTaskInfo(runningTask);
                }
            }

        }
//        Log.d("BadToken", "------------------------------------------------------------------------------");
    }
    */
}
