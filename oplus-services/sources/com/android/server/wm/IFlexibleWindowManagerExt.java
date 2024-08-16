package com.android.server.wm;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.window.ClientWindowFrames;
import com.android.server.am.ActivityManagerService;
import com.android.server.wm.ActivityStarter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IFlexibleWindowManagerExt {
    public static final String KEY_ACTIVITY_NO_ANIM = "androidx.flexible.activityNoAnimation";

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface FlexibleWindowTaskStateListener {
        void onStateChanged(int i);
    }

    default void addFlexibleWindowTaskStateListener(FlexibleWindowTaskStateListener flexibleWindowTaskStateListener) {
    }

    default void adjustInputMethodTargetFrame(DisplayContent displayContent, WindowState windowState, ClientWindowFrames clientWindowFrames) {
    }

    default void handleUiModeChanged(int i) {
    }

    default void init(ActivityTaskManagerService activityTaskManagerService) {
    }

    default boolean interceptStartActivityFromFlexibleWindow(Task task, Task task2, ActivityOptions activityOptions, ActivityRecord activityRecord, ActivityStarter.Request request, ActivityRecord activityRecord2) {
        return false;
    }

    default boolean isEmbbeddingTaskAnimating() {
        return false;
    }

    default boolean isInPocketStudio(int i) {
        return false;
    }

    default boolean isSupportEmbedded(int i) {
        return false;
    }

    default void moveTaskToFront(Task task, ActivityOptions activityOptions, String str) {
    }

    default void onFlexibleWindowBackPressedOnTaskRoot(Task task, ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onFlexibleWindowTaskAppeared(Task task, ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onFlexibleWindowTaskInfoChanged(Task task, ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onFlexibleWindowTaskVanished(Task task, ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onRecentsAnimationExecuting(Task task, boolean z, int i) {
    }

    default void onSystemReadyEnd(ActivityManagerService activityManagerService) {
    }

    default void preReportDropResult(WindowState windowState, boolean z) {
    }

    default void prepareSurfaces(Task task) {
    }

    default void removeFlexibleWindowTaskStateListener(FlexibleWindowTaskStateListener flexibleWindowTaskStateListener) {
    }

    default void setIsEmbbeddingTaskAnimating(boolean z) {
    }

    default boolean skipShowRecentTask(Task task, int i) {
        return false;
    }

    default void systemReady() {
    }

    default void updateFlexibleWindowTask(Task task, Task task2, ActivityOptions activityOptions, ActivityRecord activityRecord, ActivityRecord activityRecord2, int i) {
    }

    default ArrayList<WindowState> getSkipWaitingForDrawn() {
        return new ArrayList<>();
    }
}
