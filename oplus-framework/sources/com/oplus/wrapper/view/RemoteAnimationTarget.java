package com.oplus.wrapper.view;

import android.app.ActivityManager;
import android.graphics.Point;
import android.graphics.Rect;
import com.oplus.wrapper.app.WindowConfiguration;

/* loaded from: classes.dex */
public class RemoteAnimationTarget {
    private final android.view.RemoteAnimationTarget mRemoteAnimationTarget;
    public static final int MODE_OPENING = getModeOpening();
    public static final int MODE_CLOSING = getModeClosing();
    public static final int MODE_CHANGING = getModeChanging();

    public RemoteAnimationTarget(int taskId, int mode, android.view.SurfaceControl leash, boolean isTranslucent, Rect clipRect, Rect contentInsets, int prefixOrderIndex, Point position, Rect localBounds, Rect screenSpaceBounds, WindowConfiguration windowConfig, boolean isNotInRecents, android.view.SurfaceControl startLeash, Rect startBounds, ActivityManager.RunningTaskInfo taskInfo, boolean allowEnterPip, int windowType) {
        this.mRemoteAnimationTarget = new android.view.RemoteAnimationTarget(taskId, mode, leash, isTranslucent, clipRect, contentInsets, prefixOrderIndex, position, localBounds, screenSpaceBounds, windowConfig.getmWindowConfiguration(), isNotInRecents, startLeash, startBounds, taskInfo, allowEnterPip, windowType);
    }

    public RemoteAnimationTarget(int taskId, int mode, android.view.SurfaceControl leash, boolean isTranslucent, Rect clipRect, Rect contentInsets, int prefixOrderIndex, Point position, Rect localBounds, Rect screenSpaceBounds, WindowConfiguration windowConfig, boolean isNotInRecents, android.view.SurfaceControl startLeash, Rect startBounds, ActivityManager.RunningTaskInfo taskInfo, boolean allowEnterPip) {
        this.mRemoteAnimationTarget = new android.view.RemoteAnimationTarget(taskId, mode, leash, isTranslucent, clipRect, contentInsets, prefixOrderIndex, position, localBounds, screenSpaceBounds, windowConfig.getmWindowConfiguration(), isNotInRecents, startLeash, startBounds, taskInfo, allowEnterPip);
    }

    public RemoteAnimationTarget(android.view.RemoteAnimationTarget remoteAnimationTarget) {
        this.mRemoteAnimationTarget = remoteAnimationTarget;
    }

    public static android.view.RemoteAnimationTarget[] getInternalRemoteAnimationTarget(RemoteAnimationTarget[] remoteAnimationTargets) {
        android.view.RemoteAnimationTarget[] internalRemoteAnimationTargets = new android.view.RemoteAnimationTarget[remoteAnimationTargets.length];
        for (int i = 0; i < remoteAnimationTargets.length; i++) {
            internalRemoteAnimationTargets[i] = remoteAnimationTargets[i].mRemoteAnimationTarget;
        }
        return internalRemoteAnimationTargets;
    }

    public static RemoteAnimationTarget[] getWrapperRemoteAnimationTarget(android.view.RemoteAnimationTarget[] remoteAnimationTargets) {
        RemoteAnimationTarget[] wrapperRemoteAnimationTargets = new RemoteAnimationTarget[remoteAnimationTargets.length];
        for (int i = 0; i < remoteAnimationTargets.length; i++) {
            wrapperRemoteAnimationTargets[i] = new RemoteAnimationTarget(remoteAnimationTargets[i]);
        }
        return wrapperRemoteAnimationTargets;
    }

    public final boolean getAllowEnterPip() {
        return this.mRemoteAnimationTarget.allowEnterPip;
    }

    public final Rect getClipRect() {
        return this.mRemoteAnimationTarget.clipRect;
    }

    public final Rect getContentInsets() {
        return this.mRemoteAnimationTarget.contentInsets;
    }

    public final boolean getIsNotInRecents() {
        return this.mRemoteAnimationTarget.isNotInRecents;
    }

    public final boolean getIsTranslucent() {
        return this.mRemoteAnimationTarget.isTranslucent;
    }

    public final android.view.SurfaceControl getLeash() {
        return this.mRemoteAnimationTarget.leash;
    }

    public final Rect getLocalBounds() {
        return this.mRemoteAnimationTarget.localBounds;
    }

    public final int getMode() {
        return this.mRemoteAnimationTarget.mode;
    }

    public final Point getPosition() {
        return this.mRemoteAnimationTarget.position;
    }

    public final int getWindowType() {
        return this.mRemoteAnimationTarget.windowType;
    }

    public final WindowConfiguration getWindowConfiguration() {
        return new WindowConfiguration(this.mRemoteAnimationTarget.windowConfiguration);
    }

    public ActivityManager.RunningTaskInfo getTaskInfo() {
        return this.mRemoteAnimationTarget.taskInfo;
    }

    public final int getTaskId() {
        return this.mRemoteAnimationTarget.taskId;
    }

    public final android.view.SurfaceControl getStartLeash() {
        return this.mRemoteAnimationTarget.startLeash;
    }

    public final Rect getStartBounds() {
        return this.mRemoteAnimationTarget.startBounds;
    }

    public final Rect sourceContainerBounds() {
        return this.mRemoteAnimationTarget.sourceContainerBounds;
    }

    public final Rect getScreenSpaceBounds() {
        return this.mRemoteAnimationTarget.screenSpaceBounds;
    }

    public final int getPrefixOrderIndex() {
        return this.mRemoteAnimationTarget.prefixOrderIndex;
    }

    private static int getModeOpening() {
        return 0;
    }

    private static int getModeClosing() {
        return 1;
    }

    private static int getModeChanging() {
        return 2;
    }
}
