package com.android.server.wm;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.SurfaceControl;
import android.window.TaskSnapshot;
import com.android.internal.policy.AttributeCache;
import com.android.server.am.IOplusActivityManagerServiceEx;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IOplusEmbeddingManagerService extends IOplusCommonFeature {
    public static final IOplusEmbeddingManagerService DEFAULT = new IOplusEmbeddingManagerService() { // from class: com.android.server.wm.IOplusEmbeddingManagerService.1
    };
    public static final String NAME = "IOplusEmbeddingManagerService";

    default void addFlagsOfIntentFromSettingTaskFragment(ActivityRecord activityRecord, Intent intent, String str, AttributeCache.Entry entry, int i) {
    }

    default void adjustAnimationFrameForExpandedWindow(WindowContainer windowContainer, Rect rect, int i, boolean z) {
    }

    default void adjustTouchableRegionInActivityEmbedding(WindowState windowState, Rect rect) {
    }

    default boolean assignLayersIfNeed(ActivityRecord activityRecord) {
        return true;
    }

    default boolean canSpecifyOrientationInActivityEmbedding(TaskFragment taskFragment) {
        return false;
    }

    default void cpuFrequencyBoostIfNeed(ActivityRecord activityRecord) {
    }

    default void disposeFullTfIfNeeded(Task task) {
    }

    default int getOverrideAppRootTaskClipMode(int i, WindowContainer windowContainer) {
        return i;
    }

    default int handleStartingWindowForEmbededWindow(int i, ActivityRecord activityRecord, TaskSnapshot taskSnapshot, int i2) {
        return i;
    }

    default boolean hasEmbedTaskFragment(Task task) {
        return false;
    }

    default boolean hookIsActivityEmbedded(boolean z, TaskFragment taskFragment, ActivityRecord activityRecord) {
        return z;
    }

    default void init(IOplusActivityManagerServiceEx iOplusActivityManagerServiceEx, IOplusActivityTaskManagerServiceEx iOplusActivityTaskManagerServiceEx) {
    }

    default boolean isActivityConfigOverrideDisable(ActivityRecord activityRecord, WindowProcessController windowProcessController) {
        return false;
    }

    default boolean isActivityEmbedded(TaskFragment taskFragment, ActivityRecord activityRecord) {
        return false;
    }

    default boolean isAllowPkgEmbed(String str) {
        return false;
    }

    default boolean isAllowedToEmbedActivity(ActivityRecord activityRecord, int i) {
        return true;
    }

    default boolean isFuncEnable() {
        return false;
    }

    default boolean isInPhoneGuide(ActivityRecord activityRecord) {
        return false;
    }

    default boolean isNeedFullScreenFromSettingTaskFragment(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return false;
    }

    default boolean isNotTransferForEmbeded(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return false;
    }

    default boolean isSettingDialog(WindowState windowState) {
        return false;
    }

    default boolean isSettingTaskFragment(TaskFragment taskFragment) {
        return false;
    }

    default TaskFragment modifyParentForEmbeddingSettingIfNeed(ActivityRecord activityRecord, Task task, TaskFragment taskFragment) {
        return taskFragment;
    }

    default void onTaskFragmentPrepareSurface(TaskFragment taskFragment) {
    }

    default void overrideTaskFragmentAnimationIfNeed(DisplayContent displayContent, Task task, ActivityRecord activityRecord) {
    }

    default int reorderIndex(Task task, WindowContainer windowContainer, int i) {
        return i;
    }

    default void resetLeashCropIfNeed(ActivityRecord activityRecord, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
    }

    default void resizeTouchRegionForSpecial(ActivityRecord activityRecord, WindowFrames windowFrames, Region region, WindowState windowState) {
    }

    default boolean shouldCropAnimationLeashInEmbedding(WindowContainer windowContainer) {
        return true;
    }

    default boolean shouldIgnoreOrientationRequests(ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldLayoutFullscreenInEmbedding(WindowState windowState) {
        return false;
    }

    default boolean shouldRequestFocusForWindow(TaskFragment taskFragment) {
        return false;
    }

    default boolean supportMultiResumeInActivityEmbedding(ActivityRecord activityRecord) {
        return false;
    }

    default boolean syncEmbeddedWindowDrawStateIfNeeded(WindowState windowState) {
        return false;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusEmbeddingManagerService;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
