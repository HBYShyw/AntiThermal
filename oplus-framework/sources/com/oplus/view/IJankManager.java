package com.oplus.view;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import java.util.ArrayList;

/* loaded from: classes.dex */
public interface IJankManager extends IOplusCommonFeature {
    public static final IJankManager DEFAULT = new IJankManager() { // from class: com.oplus.view.IJankManager.1
    };
    public static final int MS_TO_NS = 1000000;
    public static final String NAME = "IJankManager";
    public static final int TASK_OPERATION_LATENCY_THRESHOLD = 3000;

    /* loaded from: classes.dex */
    public static class Action {
        public static final int ACTION_END = 0;
        public static final int ACTION_START = 1;
    }

    /* loaded from: classes.dex */
    public static final class JPolicy {
        public static final long OVERRIDE_BY_NEXT_SCENE_FLAG = 16;
        public static final long USD_SELF_TIME_FLAG = 2;
        public static final long USE_DOWN_EVENT_FLAG = 4;
        public static final long USE_UP_EVENT_FLAG = 8;
    }

    /* loaded from: classes.dex */
    public static class LauncherScene {
        public static final int APP_START_ANIM = 1;
        public static final int BACK_TO_LAUNCHER_ANIM = 2;
        public static final int ENTER_RECENT_TASK_ANIM_FROM_APP = 4;
        public static final int ENTER_RECENT_TASK_ANIM_FROM_LAUNCHER = 3;
        public static final int LAUNCH_ANIM_FROM_RECENT = 6;
        public static final int OUT_RECENT_TASK_ANIM = 5;
    }

    /* loaded from: classes.dex */
    public static final class SsOperationScene {
        public static final int TASK_OPERATION_LATENCY = 1;
        public static final String TASK_OPERATION_LATENCY_DESC = "app_transition_latency";
    }

    /* loaded from: classes.dex */
    public static final class AppSceneType {
        public static final int SCENE_TYPE_GFX_FRAME = 2;
        public static final int SCENE_TYPE_LATENCY = 0;
        public static final int SCENE_TYPE_SS_ANIM = 1;

        public static boolean isInvalidType(int sceneType) {
            return sceneType < 0 || sceneType > 2;
        }
    }

    /* loaded from: classes.dex */
    public static final class SsAnimScene {
        public static final int SCREEN_ROTATION_ANIMATION = 2;
        public static final int TASK_OPERATION_ANIMATION = 1;
        public static final String TASK_OPERATION_ANIMATION_DESC = "app_transition_anim";

        public static boolean isInvalidSsScene(int scene) {
            return (scene == 1 || scene == 2) ? false : true;
        }
    }

    /* loaded from: classes.dex */
    public static final class SceneInfo {
        private long mAnimCostThreshold;
        private long mLatencyThresholdNs;
        private String mPackageName;
        private long mPolicy;
        private int mScene;
        private long mSceneBeginTime;
        private String mSceneDescription;
        private int mSceneType;
        private int mSkippedAnimFrame;
        private int mSkippedFrame;
        private long mSkippedFrameThreshold;

        public SceneInfo() {
            this.mSceneType = -1;
            this.mScene = -1;
            this.mAnimCostThreshold = 0L;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SceneInfo(int sceneType, int scene, String sceneDes, long animCostThreshold, int skippedAnimFrame) {
            this.mSceneType = -1;
            this.mScene = -1;
            this.mAnimCostThreshold = 0L;
            this.mSceneType = sceneType;
            this.mScene = scene;
            this.mSceneDescription = sceneDes;
            this.mAnimCostThreshold = animCostThreshold;
            this.mSkippedAnimFrame = skippedAnimFrame;
            this.mSceneBeginTime = System.nanoTime();
        }

        public void initBasicInfo(Context context, int sceneType, int scene, String sceneDes) {
            if (context != null) {
                this.mPackageName = context.getPackageName();
            }
            this.mSceneType = sceneType;
            this.mScene = scene;
            this.mSceneDescription = sceneDes;
        }

        public void updateThreshold(long latencyThresholdNs, long animCostThreshold, int skippedAnimFrame, long skippedFrameThreshold, int skippedFrame, long policy) {
            this.mLatencyThresholdNs = latencyThresholdNs;
            this.mAnimCostThreshold = animCostThreshold;
            this.mSkippedAnimFrame = skippedAnimFrame;
            this.mSkippedFrameThreshold = skippedFrameThreshold;
            this.mSkippedFrame = skippedFrame;
            this.mPolicy = policy;
        }

        public void reset() {
            this.mScene = -1;
            this.mSceneBeginTime = 0L;
            this.mSceneDescription = null;
            this.mLatencyThresholdNs = 0L;
            this.mAnimCostThreshold = 0L;
            this.mSkippedAnimFrame = 0;
            this.mSkippedFrameThreshold = 0L;
            this.mSkippedFrame = 0;
            this.mPolicy = 0L;
        }

        public String toString() {
            return "SceneInfo{mPackageName=" + this.mPackageName + ", mSceneType=" + this.mSceneType + ", mScene=" + this.mScene + ", mSceneBeginTime=" + this.mSceneBeginTime + ", mSceneDescription='" + this.mSceneDescription + "', mLatencyThresholdNs=" + this.mLatencyThresholdNs + ", mAnimCostThreshold=" + this.mAnimCostThreshold + ", mSkippedAnimFrame=" + this.mSkippedAnimFrame + ", mSkippedFrameThreshold=" + this.mSkippedFrameThreshold + ", mSkippedFrame=" + this.mSkippedFrame + ", mPolicy=" + this.mPolicy + '}';
        }

        public void updateSceneBeginTime() {
            this.mSceneBeginTime = System.nanoTime();
        }

        public void setLatencyThresholdMs(long latencyThresholdMs) {
            this.mLatencyThresholdNs = 1000000 * latencyThresholdMs;
        }

        public void setPolicy(long policy) {
            this.mPolicy = policy;
        }

        public void setAnimCostThreshold(long mAnimCostThreshold) {
            this.mAnimCostThreshold = mAnimCostThreshold;
        }

        public int getSceneType() {
            int i;
            synchronized (this) {
                i = this.mSceneType;
            }
            return i;
        }

        public int getScene() {
            int i;
            synchronized (this) {
                i = this.mScene;
            }
            return i;
        }

        public long getSceneBeginTime() {
            long j;
            synchronized (this) {
                j = this.mSceneBeginTime;
            }
            return j;
        }

        public String getSceneDescription() {
            String str;
            synchronized (this) {
                str = this.mSceneDescription;
            }
            return str;
        }

        public long getLatencyThresholdNs() {
            long j;
            synchronized (this) {
                j = this.mLatencyThresholdNs;
            }
            return j;
        }

        public long getAnimCostThreshold() {
            long j;
            synchronized (this) {
                j = this.mAnimCostThreshold;
            }
            return j;
        }

        public int getSkippedAnimFrame() {
            int i;
            synchronized (this) {
                i = this.mSkippedAnimFrame;
            }
            return i;
        }

        public long getSkippedFrameThreshold() {
            long j;
            synchronized (this) {
                j = this.mSkippedFrameThreshold;
            }
            return j;
        }

        public int getSkippedFrame() {
            int i;
            synchronized (this) {
                i = this.mSkippedFrame;
            }
            return i;
        }

        public long getScenePolicy() {
            long j;
            synchronized (this) {
                j = this.mPolicy;
            }
            return j;
        }
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IJankManager;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void updateAppSceneAndAction(String packageName, int scene, int action) {
    }

    default int getAppScene(String packageName) {
        return -1;
    }

    default int getAppAction(String packageName) {
        return -1;
    }

    default void latencySceneBegin(Context context, int scene, String sceneDes, long latencyThresholdMs) {
    }

    default void latencySceneBegin(Context context, SceneInfo sceneInfo) {
    }

    default void latencySceneEnd(Context context, int scene, ArrayList<String> latencyInfo) {
    }

    default void ssAnimSceneBegin(Context context, int scene, String sceneDes, long animCostThreshold, int skippedAnimFrame) {
    }

    default void ssAnimSceneEnd(int scene) {
    }

    default void gfxSceneBegin(Context context, int scene, String sceneDes, long policy) {
    }

    default void gfxSceneBegin(Context context, SceneInfo sceneInfo) {
    }

    default void gfxSceneEnd(Context context, int scene) {
    }

    default int getSceneType() {
        return -1;
    }

    default int getScene() {
        return -1;
    }

    default long getSceneBeginTime() {
        return -1L;
    }

    default String getSceneDescription() {
        return null;
    }

    default long getLatencyThresholdNs() {
        return -1L;
    }

    default long getAnimCostThreshold() {
        return -1L;
    }

    default int getSkippedAnimFrame() {
        return -1;
    }

    default long getSkippedFrameThreshold() {
        return -1L;
    }

    default int getSkippedFrame() {
        return -1;
    }

    default long getScenePolicy() {
        return -1L;
    }

    default boolean isSceneEnabled(int sceneType, int scene) {
        return false;
    }

    default void flushFrame(int scene, long frameIntervalNanos, long frameCostTime) {
    }

    default void notifyActivityStarted(String packageName, String activityName) {
    }

    default void notifyAddStartingWindow(String packageName) {
    }
}
