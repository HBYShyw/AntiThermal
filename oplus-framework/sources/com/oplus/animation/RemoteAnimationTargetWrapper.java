package com.oplus.animation;

import android.app.ActivityManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;

/* loaded from: classes.dex */
public class RemoteAnimationTargetWrapper {
    public static final int ACTIVITY_TYPE_ASSISTANT = 4;
    public static final int ACTIVITY_TYPE_HOME = 2;
    public static final int ACTIVITY_TYPE_RECENTS = 3;
    public static final int ACTIVITY_TYPE_STANDARD = 1;
    public static final int ACTIVITY_TYPE_UNDEFINED = 0;
    public static final int MODE_CHANGING = 2;
    public static final int MODE_CLOSING = 1;
    public static final int MODE_OPENING = 0;
    public final int activityType;
    public final boolean allowEnterPip;
    public final Rect clipRect;
    public final Rect contentInsets;
    public final boolean isNotInRecents;
    public final boolean isTranslucent;
    public final SurfaceControl leash;
    public final Rect localBounds;
    public LaunchViewInfo mLaunchViewInfo;
    public final int mode;
    public final Point position;
    public final int prefixOrderIndex;
    public final int rotationChange;
    public final Rect screenSpaceBounds;
    public final Rect sourceContainerBounds;
    public final Rect startBounds;
    public final SurfaceControl startLeash;
    public final Rect startScreenSpaceBounds;
    public int taskId;
    public ActivityManager.RunningTaskInfo taskInfo;
    public final int windowType;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteAnimationTargetWrapper(RemoteAnimationTarget originTarget) {
        this.taskId = originTarget.taskId;
        this.mode = originTarget.mode;
        this.leash = originTarget.leash;
        this.isTranslucent = originTarget.isTranslucent;
        this.clipRect = originTarget.clipRect;
        this.position = originTarget.position;
        this.localBounds = originTarget.localBounds;
        this.sourceContainerBounds = originTarget.sourceContainerBounds;
        Rect rect = originTarget.screenSpaceBounds;
        this.screenSpaceBounds = rect;
        this.startScreenSpaceBounds = rect;
        this.prefixOrderIndex = originTarget.prefixOrderIndex;
        this.isNotInRecents = originTarget.isNotInRecents;
        this.contentInsets = originTarget.contentInsets;
        this.activityType = originTarget.windowConfiguration.getActivityType();
        this.taskInfo = originTarget.taskInfo;
        this.allowEnterPip = originTarget.allowEnterPip;
        this.rotationChange = 0;
        this.startLeash = originTarget.startLeash;
        this.windowType = originTarget.windowType;
        this.startBounds = originTarget.startBounds;
        this.mLaunchViewInfo = (LaunchViewInfo) originTarget.mExt.getOplusLaunchViewInfo();
    }
}
