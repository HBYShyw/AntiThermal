package com.oplus.wrapper.window;

import android.app.ActivityManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceControl;
import android.window.TransitionInfo;
import com.oplus.zoomwindow.OplusZoomWindowManager;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class TransitionInfo {
    private final android.window.TransitionInfo mTransitionInfo;
    public static final int FLAG_NONE = getFlagNone();
    public static final int FLAG_SHOW_WALLPAPER = getFlagShowWallpaper();
    public static final int FLAG_IS_WALLPAPER = getFlagIsWallpaper();
    public static final int FLAG_TRANSLUCENT = getFlagTranslucent();
    public static final int FLAG_STARTING_WINDOW_TRANSFER_RECIPIENT = getFlagStartingWindowTransferRecipient();
    public static final int FLAG_IS_VOICE_INTERACTION = getFlagIsVoiceInteraction();
    public static final int FLAG_IS_DISPLAY = getFlagIsDisplay();
    public static final int FLAG_DISPLAY_HAS_ALERT_WINDOWS = getFlagDisplayHasAlertWindows();
    public static final int FLAG_IS_INPUT_METHOD = getFlagIsInputMethod();
    public static final int FLAG_IN_TASK_WITH_EMBEDDED_ACTIVITY = getFlagInTaskWithEmbeddedActivity();
    public static final int FLAG_FILLS_TASK = getFlagFillsTask();
    public static final int FLAG_WILL_IME_SHOWN = getFlagWillImeShown();
    public static final int FLAG_CROSS_PROFILE_OWNER_THUMBNAIL = getFlagCrossProfileOwnerThumbnail();
    public static final int FLAG_CROSS_PROFILE_WORK_THUMBNAIL = getFlagCrossProfileWorkThumbnail();
    public static final int FLAG_IS_BEHIND_STARTING_WINDOW = getFlagIsBehindStartingWindow();
    public static final int FLAG_IS_OCCLUDED = getFlagIsOccluded();
    public static final int FLAG_IS_SYSTEM_WINDOW = getFlagIsSystemWindow();
    public static final int FLAG_BACK_GESTURE_ANIMATED = getFlagBackGestureAnimated();
    public static final int FLAG_NO_ANIMATION = getFlagNoAnimation();
    public static final int FLAG_TASK_LAUNCHING_BEHIND = getFlagTaskLaunchingBehind();
    public static final int FLAG_MOVED_TO_TOP = getFlagMovedToTop();
    public static final int FLAG_SYNC = getFlagSync();
    public static final int FLAG_FIRST_CUSTOM = getFlagFirstCustom();

    public TransitionInfo(android.window.TransitionInfo transitionInfo) {
        this.mTransitionInfo = transitionInfo;
    }

    private static int getFlagNone() {
        return 0;
    }

    private static int getFlagShowWallpaper() {
        return 1;
    }

    private static int getFlagIsWallpaper() {
        return 2;
    }

    private static int getFlagTranslucent() {
        return 4;
    }

    private static int getFlagStartingWindowTransferRecipient() {
        return 8;
    }

    private static int getFlagIsVoiceInteraction() {
        return 16;
    }

    private static int getFlagIsDisplay() {
        return 32;
    }

    private static int getFlagDisplayHasAlertWindows() {
        return 128;
    }

    private static int getFlagIsInputMethod() {
        return 256;
    }

    private static int getFlagInTaskWithEmbeddedActivity() {
        return 512;
    }

    private static int getFlagFillsTask() {
        return 1024;
    }

    private static int getFlagWillImeShown() {
        return 2048;
    }

    private static int getFlagCrossProfileOwnerThumbnail() {
        return 4096;
    }

    private static int getFlagCrossProfileWorkThumbnail() {
        return 8192;
    }

    private static int getFlagIsBehindStartingWindow() {
        return 16384;
    }

    private static int getFlagIsOccluded() {
        return 32768;
    }

    private static int getFlagIsSystemWindow() {
        return 65536;
    }

    private static int getFlagBackGestureAnimated() {
        return 131072;
    }

    private static int getFlagNoAnimation() {
        return 262144;
    }

    private static int getFlagTaskLaunchingBehind() {
        return 524288;
    }

    private static int getFlagMovedToTop() {
        return 1048576;
    }

    private static int getFlagSync() {
        return 2097152;
    }

    private static int getFlagFirstCustom() {
        return OplusZoomWindowManager.FLAG_TOUCH_OUTSIDE_CONTROL_VIEW;
    }

    public android.window.TransitionInfo get() {
        return this.mTransitionInfo;
    }

    public List<Change> getChanges() {
        List<TransitionInfo.Change> change = this.mTransitionInfo.getChanges();
        ArrayList<Change> wrapperChange = new ArrayList<>();
        for (int i = 0; i < change.size(); i++) {
            wrapperChange.add(new Change(change.get(i)));
        }
        return wrapperChange;
    }

    public int getFlags() {
        return this.mTransitionInfo.getFlags();
    }

    public Change getChange(WindowContainerToken token) {
        android.window.WindowContainerToken windowContainerToken = token.get();
        if (windowContainerToken == null) {
            return null;
        }
        TransitionInfo.Change change = this.mTransitionInfo.getChange(windowContainerToken);
        return new Change(change);
    }

    public int getType() {
        return this.mTransitionInfo.getType();
    }

    public static boolean isIndependent(Change change, TransitionInfo info) {
        return android.window.TransitionInfo.isIndependent(change.get(), info.get());
    }

    public Root getRoot(int idx) {
        return new Root(this.mTransitionInfo.getRoot(idx));
    }

    public SurfaceControl getRootLeash() {
        return this.mTransitionInfo.getRootLeash();
    }

    /* loaded from: classes.dex */
    public static final class Change {
        private static final String TAG = "Change";
        private final TransitionInfo.Change mChange;

        public Change(TransitionInfo.Change change) {
            this.mChange = change;
        }

        public TransitionInfo.Change get() {
            return this.mChange;
        }

        public boolean getAllowEnterPip() {
            return this.mChange.getAllowEnterPip();
        }

        public WindowContainerToken getContainer() {
            android.window.WindowContainerToken container = this.mChange.getContainer();
            if (container == null) {
                Log.d(TAG, "getContainer: container is null");
                return null;
            }
            return new WindowContainerToken(container);
        }

        public Rect getEndAbsBounds() {
            return this.mChange.getEndAbsBounds();
        }

        public Point getEndRelOffset() {
            return this.mChange.getEndRelOffset();
        }

        public int getEndRotation() {
            return this.mChange.getEndRotation();
        }

        public int getFlags() {
            return this.mChange.getFlags();
        }

        public SurfaceControl getLeash() {
            return this.mChange.getLeash();
        }

        public int getMode() {
            return this.mChange.getMode();
        }

        public WindowContainerToken getParent() {
            return new WindowContainerToken(this.mChange.getParent());
        }

        public Rect getStartAbsBounds() {
            return this.mChange.getStartAbsBounds();
        }

        public int getStartRotation() {
            return this.mChange.getStartRotation();
        }

        public ActivityManager.RunningTaskInfo getTaskInfo() {
            return this.mChange.getTaskInfo();
        }
    }

    /* loaded from: classes.dex */
    public static final class Root {
        private final TransitionInfo.Root mRoot;

        public Root(TransitionInfo.Root root) {
            this.mRoot = root;
        }

        public SurfaceControl getLeash() {
            return this.mRoot.getLeash();
        }
    }
}
