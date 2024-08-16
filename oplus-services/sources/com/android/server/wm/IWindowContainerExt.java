package com.android.server.wm;

import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Pair;
import android.view.DisplayInfo;
import android.view.SurfaceControl;
import android.view.WindowManager;
import android.view.animation.Animation;
import com.android.server.wm.BLASTSyncEngine;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IWindowContainerExt {
    default void addAnimationUpdateRecorder(Animation animation, int i, boolean z, int i2, int i3, int i4, int i5) {
    }

    default void addChild(WindowContainer windowContainer, WindowContainer windowContainer2) {
    }

    default void addRoundedCornersToAnimationIfNeed(WindowManager.LayoutParams layoutParams, int i, boolean z, boolean z2, Animation animation) {
    }

    default void adjustAnimationBounds(WindowContainer windowContainer, Rect rect) {
    }

    default void adjustAnimationForMultiTask(WindowContainer windowContainer, Animation animation, Rect rect) {
    }

    default void adjustAnimationFrameForExpandedWindow(WindowContainer windowContainer, Rect rect, int i, boolean z) {
    }

    default int adjustOrientationForBracketMode(int i) {
        return i;
    }

    default void adjustPointsOffsetForParallelWindowAnimation(WindowContainer windowContainer, Point point) {
    }

    default boolean adjustZBoostForTransit(WindowContainer windowContainer, int i, boolean z, boolean z2) {
        return z2;
    }

    default boolean blockUpdateSurfacePosition(WindowContainer windowContainer) {
        return false;
    }

    default boolean canPaintAnimation(int i) {
        return false;
    }

    default Animation createAnimationForLauncherExit() {
        return null;
    }

    default void dispatchConfigurationToChild(DisplayContent displayContent, Configuration configuration) {
    }

    default void dump(PrintWriter printWriter, String str, WindowContainer windowContainer) {
    }

    default boolean enableTaskBackgroundColor(WindowContainer windowContainer) {
        return false;
    }

    default boolean forceFinishSync(WindowContainer windowContainer, BLASTSyncEngine.SyncGroup syncGroup) {
        return false;
    }

    default boolean forceUpdateConfig(ConfigurationContainer configurationContainer, int i) {
        return false;
    }

    default AnimationAdapter getClipAnimationAdapter(AnimationAdapter animationAdapter, Animation animation, Point point, Rect rect, int i, float f, int i2, boolean z, WindowContainer windowContainer) {
        return animationAdapter;
    }

    default int getFixedScreenOrientation(WindowContainer windowContainer, int i) {
        return i;
    }

    default int getOverrideAppRootTaskClipMode(int i, WindowContainer windowContainer) {
        return i;
    }

    default Pair<AnimationAdapter, AnimationAdapter> getRemoteAnimationAdapterForCompactWindow(RemoteAnimationController remoteAnimationController, WindowContainer windowContainer, int i, boolean z) {
        return null;
    }

    default Pair<AnimationAdapter, AnimationAdapter> getRemoteAnimationAdapterForSplitScreen(RemoteAnimationController remoteAnimationController, WindowContainer windowContainer, int i, boolean z, boolean z2) {
        return null;
    }

    default float getWindowCornerRadiusForAnimation(WindowContainer windowContainer, float f, Animation animation, WindowManager.LayoutParams layoutParams, int i) {
        return f;
    }

    default void handleComapctReparent(WindowContainer windowContainer, boolean z, WindowContainer windowContainer2) {
    }

    default Pair<AnimationAdapter, AnimationAdapter> hookGetAnimationAdapter(WindowContainer windowContainer, WindowManager.LayoutParams layoutParams, int i, boolean z, boolean z2) {
        return null;
    }

    default boolean hookGetOrientation(WindowContainer windowContainer) {
        return false;
    }

    default boolean hookupdateSurfacePosition(int i, Task task, WindowContainer windowContainer) {
        return false;
    }

    default boolean isFingerPrintToken(WindowContainer windowContainer) {
        return false;
    }

    default boolean isLightOsCompactWindow(int i) {
        return false;
    }

    default boolean isSettingTaskFragment(WindowContainer windowContainer) {
        return false;
    }

    default boolean isSyncFinishedInCompactWindow(WindowContainer windowContainer, int i) {
        return true;
    }

    default boolean isZoomMode(int i) {
        return false;
    }

    default void onAnimationFinished(WindowContainer windowContainer, Handler handler) {
    }

    default void onAnimationLeashCreated(WindowContainer windowContainer, SurfaceControl.Transaction transaction) {
    }

    default void onAnimationLeashLost(WindowContainer windowContainer, SurfaceControl.Transaction transaction) {
    }

    default void onChildAdded(WindowContainer windowContainer, WindowContainer windowContainer2) {
    }

    default void onChildRemoved(WindowContainer windowContainer, WindowContainer windowContainer2) {
    }

    default void onOriginListAdded(WindowContainer windowContainer, WindowContainer windowContainer2) {
    }

    default void onParentConfirmed(WindowContainer windowContainer) {
    }

    default void onSyncFinishedDrawing(WindowContainer windowContainer) {
    }

    default void removeChild(WindowContainer windowContainer, WindowContainer windowContainer2) {
    }

    default void setMaskIfNeedsInCompactWindow(WindowContainer windowContainer, int i) {
    }

    default boolean shouldCropAnimationLeashInEmbedding(WindowContainer windowContainer) {
        return true;
    }

    default boolean shouldIgnorePositionChildAtTop(WindowContainer windowContainer, WindowContainer windowContainer2) {
        return false;
    }

    default boolean shouldResumeTaskTopActivity(Task task, ActivityRecord activityRecord) {
        return true;
    }

    default boolean shouldSkipAnimationForFlexibleWindow(WindowContainer windowContainer) {
        return false;
    }

    default boolean shouldUpdateConfig(ConfigurationContainer configurationContainer, int i) {
        return false;
    }

    default boolean skipCheckSyncFinished(WindowContainer windowContainer, WindowContainer windowContainer2, int i) {
        return false;
    }

    default boolean skipLoadAnimation() {
        return true;
    }

    default boolean skipSystemCreation(WindowContainer windowContainer) {
        return false;
    }

    default void tryPaintAnimation(Animation animation, int i, boolean z) {
    }

    default int[] getAdjustDisplayInfo(DisplayInfo displayInfo) {
        return new int[]{displayInfo.appWidth, displayInfo.appHeight};
    }
}
