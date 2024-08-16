package com.oplus.view;

import android.content.Context;
import android.os.Binder;
import android.os.Process;
import android.os.SystemProperties;
import android.os.Trace;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import com.oplus.android.internal.util.OplusFrameworkStatsLog;
import com.oplus.view.IJankManager;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class JankManager implements IJankManager {
    private static final String COMMA_SEPARATOR = ",";
    public static final int INVALID_SCENE_ID = -1;
    private static final String PKG_LAUNCHER = "com.android.launcher";
    private static final long SLOW_LAUNCH_THRESHOLD = 800000000;
    private static volatile JankManager sInstance = null;
    private boolean mDebug;
    private String mLastTopActivityName;
    private String mLastTopPackageName;
    private SparseArray<LatencySceneInfo> mLatencySceneArrays;
    private int mLauncherAnimationAction;
    private int mLauncherAnimationScene;
    private final IJankManager.SceneInfo mSceneInfo;
    private SparseArray<SsSceneInfo> mSsAnimSceneArrays;
    private String mStableLastTopActivity;
    private String mStableLastTopPkg;
    private String mStartingWindowPkg;
    private String mTopActivityName;
    private String mTopPackageName;

    private JankManager() {
        this.mDebug = SystemProperties.getBoolean("persist.sys.assert.panic", false) || SystemProperties.get("ro.build.version.ota", "na").contains("PRE_");
        this.mSceneInfo = new IJankManager.SceneInfo();
        this.mSsAnimSceneArrays = new SparseArray<>();
        this.mLatencySceneArrays = new SparseArray<>();
        this.mLauncherAnimationScene = -1;
        this.mLauncherAnimationAction = -1;
    }

    public static JankManager getInstance() {
        if (sInstance == null) {
            synchronized (JankManager.class) {
                if (sInstance == null) {
                    sInstance = new JankManager();
                }
            }
        }
        return sInstance;
    }

    @Override // com.oplus.view.IJankManager
    public void updateAppSceneAndAction(String packageName, int scene, int action) {
        if ("com.android.launcher".equals(packageName)) {
            if (scene > 6 || scene < 1) {
                Log.e(IJankManager.NAME, "unkown scene " + scene);
                return;
            }
            if (action < 0 || action > 1) {
                Log.e(IJankManager.NAME, "unkown action " + action);
                return;
            }
            this.mLauncherAnimationScene = scene;
            this.mLauncherAnimationAction = action;
            if (action == 1) {
                gfxSceneBegin(null, scene, "launcher_anim", 0L);
            }
            if (action == 0) {
                gfxSceneEnd(null, scene);
            }
        }
    }

    @Override // com.oplus.view.IJankManager
    public int getAppScene(String packageName) {
        if ("com.android.launcher".equals(packageName)) {
            return this.mLauncherAnimationScene;
        }
        return -1;
    }

    @Override // com.oplus.view.IJankManager
    public int getAppAction(String packageName) {
        if ("com.android.launcher".equals(packageName)) {
            return this.mLauncherAnimationAction;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class SsSceneInfo {
        private long mAnimCostThreshold;
        private String mSceneDescription;
        private long mSsAnimJankCount;
        private long mSsAnimPreCount;

        SsSceneInfo(String sceneDescription, long animCostThreshold) {
            initSsAnimInfo(sceneDescription, animCostThreshold);
        }

        public void initSsAnimInfo(String sceneDescription, long animCostThreshold) {
            this.mSsAnimJankCount = 0L;
            this.mSsAnimPreCount++;
            this.mSceneDescription = sceneDescription;
            this.mAnimCostThreshold = animCostThreshold;
        }

        public void addSsAnimJankCount() {
            this.mSsAnimJankCount++;
        }

        public long getSsAnimJankCount() {
            return this.mSsAnimJankCount;
        }

        public long getSsAnimPreCount() {
            return this.mSsAnimPreCount;
        }

        public String getSceneDescription() {
            return this.mSceneDescription;
        }

        public long getAnimCostThreshold() {
            return this.mAnimCostThreshold;
        }

        public void resetPreCount() {
            this.mSsAnimPreCount = 0L;
        }

        public String toString() {
            return "SsSceneInfo{mSsAnimJankCount=" + this.mSsAnimJankCount + ", mSsAnimPreCount=" + this.mSsAnimPreCount + ", mAnimCostThreshold=" + this.mAnimCostThreshold + ", mSceneDescription='" + this.mSceneDescription + "'}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class LatencySceneInfo {
        private long mLatencyPreCount;

        LatencySceneInfo() {
        }

        public void addLatencyPreCount() {
            this.mLatencyPreCount++;
        }

        public long getLatencyPreCount() {
            return this.mLatencyPreCount;
        }

        public void resetPreCount() {
            this.mLatencyPreCount = 0L;
        }

        public String toString() {
            return "LatencySceneInfo{mLatencyPreCount=" + this.mLatencyPreCount + '}';
        }
    }

    @Override // com.oplus.view.IJankManager
    public void notifyAddStartingWindow(String packageName) {
        this.mStartingWindowPkg = packageName;
    }

    @Override // com.oplus.view.IJankManager
    public void notifyActivityStarted(String packageName, String activityName) {
        if (packageName != null && packageName.equals(this.mTopPackageName) && activityName != null && activityName.equals(this.mTopActivityName)) {
            return;
        }
        String str = this.mTopPackageName;
        this.mLastTopPackageName = str;
        String str2 = this.mTopActivityName;
        this.mLastTopActivityName = str2;
        this.mStableLastTopPkg = str;
        this.mStableLastTopActivity = str2;
        this.mTopPackageName = packageName;
        this.mTopActivityName = activityName;
    }

    @Override // com.oplus.view.IJankManager
    public void latencySceneBegin(Context context, int scene, String sceneDes, long latencyThresholdMs) {
        if (TextUtils.isEmpty(sceneDes)) {
            Log.e(IJankManager.NAME, "latencySceneBegin must have sceneDes");
            return;
        }
        if (latencyThresholdMs <= 0) {
            Log.e(IJankManager.NAME, "latencyThresholdMs must more than 0");
            return;
        }
        IJankManager.SceneInfo sceneInfo = new IJankManager.SceneInfo();
        sceneInfo.initBasicInfo(context, 0, scene, sceneDes);
        sceneInfo.setLatencyThresholdMs(latencyThresholdMs);
        appSceneBegin(context, sceneInfo);
    }

    @Override // com.oplus.view.IJankManager
    public void latencySceneBegin(Context context, IJankManager.SceneInfo sceneInfo) {
        if (sceneInfo.getSceneType() != 0) {
            Log.e(IJankManager.NAME, "latencySceneBegin with error type");
            return;
        }
        if (TextUtils.isEmpty(sceneInfo.getSceneDescription())) {
            Log.e(IJankManager.NAME, "latencySceneBegin must have sceneDes");
        } else if (sceneInfo.getLatencyThresholdNs() <= 0) {
            Log.e(IJankManager.NAME, "latencyThresholdMs must more than 0");
        } else {
            appSceneBegin(context, sceneInfo);
        }
    }

    @Override // com.oplus.view.IJankManager
    public void latencySceneEnd(Context context, int scene, ArrayList<String> latencyInfo) {
        if (this.mSceneInfo.getScene() != scene) {
            Log.e(IJankManager.NAME, "latencySceneEnd unknown scene " + scene + " mScene:" + this.mSceneInfo.getScene());
        } else {
            appSceneEnd(context, 0, scene, latencyInfo);
        }
    }

    @Override // com.oplus.view.IJankManager
    public void ssAnimSceneBegin(Context context, int scene, String sceneDes, long animCostThreshold, int skippedAnimFrame) {
        if (Binder.getCallingUid() != 1000) {
            return;
        }
        if (IJankManager.SsAnimScene.isInvalidSsScene(scene)) {
            Log.e(IJankManager.NAME, "ssAnimSceneBegin with invalid scene id");
            return;
        }
        if (TextUtils.isEmpty(sceneDes)) {
            Log.e(IJankManager.NAME, "ssAnimSceneBegin must have sceneDes");
        } else if (animCostThreshold > 0 && skippedAnimFrame > 0) {
            Log.e(IJankManager.NAME, "There is only one standard");
        } else {
            IJankManager.SceneInfo sceneInfo = new IJankManager.SceneInfo(1, scene, sceneDes, animCostThreshold, skippedAnimFrame);
            appSceneBegin(context, sceneInfo);
        }
    }

    @Override // com.oplus.view.IJankManager
    public void ssAnimSceneEnd(int scene) {
        if (Binder.getCallingUid() != 1000) {
            return;
        }
        if (IJankManager.SsAnimScene.isInvalidSsScene(scene)) {
            Log.e(IJankManager.NAME, "ssAnimSceneBegin with invalid scene id");
        } else {
            appSceneEnd(null, 1, scene, null);
        }
    }

    @Override // com.oplus.view.IJankManager
    public void gfxSceneBegin(Context context, int scene, String sceneDes, long policy) {
        IJankManager.SceneInfo sceneInfo = new IJankManager.SceneInfo();
        sceneInfo.initBasicInfo(context, 2, scene, sceneDes);
        sceneInfo.setPolicy(policy);
        appSceneBegin(context, sceneInfo);
    }

    @Override // com.oplus.view.IJankManager
    public void gfxSceneBegin(Context context, IJankManager.SceneInfo sceneInfo) {
        if (sceneInfo.getSceneType() != 2) {
            Log.e(IJankManager.NAME, "gfxSceneBegin with error type");
        } else if (sceneInfo.getSkippedFrameThreshold() > 0 && sceneInfo.getSkippedFrame() > 0) {
            Log.e(IJankManager.NAME, "There is only one standard");
        } else {
            appSceneBegin(context, sceneInfo);
        }
    }

    @Override // com.oplus.view.IJankManager
    public void gfxSceneEnd(Context context, int scene) {
        appSceneEnd(context, 2, scene, null);
    }

    private void appSceneBegin(Context context, IJankManager.SceneInfo sceneInfo) {
        synchronized (this) {
            try {
            } catch (Throwable th) {
                th = th;
            }
            try {
                if (IJankManager.AppSceneType.isInvalidType(sceneInfo.getSceneType())) {
                    Log.e(IJankManager.NAME, "appSceneBegin with invalid sceneType id");
                    return;
                }
                if (sceneInfo.getScene() <= -1) {
                    Log.e(IJankManager.NAME, "appSceneBegin with invalid scene id");
                    return;
                }
                this.mSceneInfo.initBasicInfo(context, sceneInfo.getSceneType(), sceneInfo.getScene(), sceneInfo.getSceneDescription());
                this.mSceneInfo.updateThreshold(sceneInfo.getLatencyThresholdNs(), sceneInfo.getAnimCostThreshold(), sceneInfo.getSkippedAnimFrame(), sceneInfo.getSkippedFrameThreshold(), sceneInfo.getSkippedFrame(), sceneInfo.getScenePolicy());
                this.mSceneInfo.updateSceneBeginTime();
                if (sceneInfo.getSceneType() == 1) {
                    initSsAnimInfo(sceneInfo.getScene(), sceneInfo.getSceneDescription(), sceneInfo.getAnimCostThreshold());
                }
                if (this.mDebug) {
                    Trace.asyncTraceBegin(8L, "appSceneType_" + sceneInfo.getSceneType(), sceneInfo.getSceneType());
                    Trace.asyncTraceBegin(8L, "appScene_" + sceneInfo.getScene(), sceneInfo.getScene());
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    private void appSceneEnd(Context context, int sceneType, int scene, ArrayList<String> sceneInfo) {
        if (this.mSceneInfo.getSceneType() != sceneType) {
            return;
        }
        if (this.mDebug) {
            Trace.asyncTraceEnd(8L, "appSceneType_" + sceneType, sceneType);
            Trace.asyncTraceEnd(8L, "appScene_" + scene, scene);
        }
        synchronized (this) {
            switch (sceneType) {
                case 0:
                    latencySceneSettle(scene, sceneInfo);
                    break;
                case 1:
                    sSAnimSceneSettle(scene);
                    break;
                case 2:
                    gfxSceneSettle(scene);
                    break;
            }
            reset();
        }
    }

    @Override // com.oplus.view.IJankManager
    public void flushFrame(int scene, long frameIntervalNanos, long frameCostTime) {
        if (this.mSceneInfo.getSceneType() != 1 || this.mSceneInfo.getScene() <= -1) {
            return;
        }
        synchronized (this) {
            if (this.mSceneInfo.getAnimCostThreshold() == 0) {
                this.mSceneInfo.setAnimCostThreshold(r0.getSkippedAnimFrame() * frameIntervalNanos);
            }
            if (this.mSceneInfo.getAnimCostThreshold() <= 0) {
                Log.e(IJankManager.NAME, "Unable to determine the threshold for judging frame loss:" + scene);
            } else {
                if (frameCostTime >= this.mSceneInfo.getAnimCostThreshold()) {
                    addSsAnimJankCount(scene);
                }
            }
        }
    }

    @Override // com.oplus.view.IJankManager
    public int getSceneType() {
        int sceneType;
        synchronized (this) {
            sceneType = this.mSceneInfo.getSceneType();
        }
        return sceneType;
    }

    @Override // com.oplus.view.IJankManager
    public int getScene() {
        int scene;
        synchronized (this) {
            scene = this.mSceneInfo.getScene();
        }
        return scene;
    }

    @Override // com.oplus.view.IJankManager
    public long getSceneBeginTime() {
        long sceneBeginTime;
        synchronized (this) {
            sceneBeginTime = this.mSceneInfo.getSceneBeginTime();
        }
        return sceneBeginTime;
    }

    @Override // com.oplus.view.IJankManager
    public String getSceneDescription() {
        String sceneDescription;
        synchronized (this) {
            sceneDescription = this.mSceneInfo.getSceneDescription();
        }
        return sceneDescription;
    }

    @Override // com.oplus.view.IJankManager
    public long getLatencyThresholdNs() {
        long latencyThresholdNs;
        synchronized (this) {
            latencyThresholdNs = this.mSceneInfo.getLatencyThresholdNs();
        }
        return latencyThresholdNs;
    }

    @Override // com.oplus.view.IJankManager
    public long getAnimCostThreshold() {
        long animCostThreshold;
        synchronized (this) {
            animCostThreshold = this.mSceneInfo.getAnimCostThreshold();
        }
        return animCostThreshold;
    }

    @Override // com.oplus.view.IJankManager
    public int getSkippedAnimFrame() {
        int skippedAnimFrame;
        synchronized (this) {
            skippedAnimFrame = this.mSceneInfo.getSkippedAnimFrame();
        }
        return skippedAnimFrame;
    }

    @Override // com.oplus.view.IJankManager
    public long getSkippedFrameThreshold() {
        long skippedFrameThreshold;
        synchronized (this) {
            skippedFrameThreshold = this.mSceneInfo.getSkippedFrameThreshold();
        }
        return skippedFrameThreshold;
    }

    @Override // com.oplus.view.IJankManager
    public int getSkippedFrame() {
        int skippedFrame;
        synchronized (this) {
            skippedFrame = this.mSceneInfo.getSkippedFrame();
        }
        return skippedFrame;
    }

    @Override // com.oplus.view.IJankManager
    public long getScenePolicy() {
        long scenePolicy;
        synchronized (this) {
            scenePolicy = this.mSceneInfo.getScenePolicy();
        }
        return scenePolicy;
    }

    @Override // com.oplus.view.IJankManager
    public boolean isSceneEnabled(int sceneType, int scene) {
        boolean z;
        synchronized (this) {
            z = getSceneType() == sceneType && getScene() == scene;
        }
        return z;
    }

    private void reset() {
        synchronized (this) {
            this.mSceneInfo.reset();
        }
    }

    private void sSAnimSceneSettle(int scene) {
        SsSceneInfo sceneInfo = this.mSsAnimSceneArrays.get(scene);
        if (sceneInfo != null) {
            long count = sceneInfo.getSsAnimJankCount();
            Trace.traceCounter(8L, IJankManager.SsAnimScene.TASK_OPERATION_ANIMATION_DESC, ((int) count) + 1);
            if (count > 0) {
                Log.p("Quality", sceneInfo.getSceneDescription() + ":" + count + COMMA_SEPARATOR + sceneInfo.getSsAnimPreCount() + COMMA_SEPARATOR + sceneInfo.getAnimCostThreshold() + COMMA_SEPARATOR + this.mStableLastTopPkg + COMMA_SEPARATOR + this.mStableLastTopActivity + COMMA_SEPARATOR + this.mTopPackageName + COMMA_SEPARATOR + this.mTopActivityName);
                OplusFrameworkStatsLog.write(100078, System.currentTimeMillis(), Process.myTid(), sceneInfo.getSceneDescription(), count, sceneInfo.getSsAnimPreCount(), sceneInfo.getAnimCostThreshold(), this.mStableLastTopPkg, this.mStableLastTopActivity, this.mTopPackageName, this.mTopActivityName);
                sceneInfo.resetPreCount();
            }
        }
        this.mStableLastTopPkg = null;
        this.mStableLastTopActivity = null;
    }

    private void addSsAnimJankCount(int scene) {
        SsSceneInfo sceneInfo = this.mSsAnimSceneArrays.get(scene);
        if (sceneInfo != null) {
            sceneInfo.addSsAnimJankCount();
        }
    }

    private void initSsAnimInfo(int scene, String sceneDescription, long animCostThreshold) {
        SsSceneInfo sceneInfo = this.mSsAnimSceneArrays.get(scene);
        if (sceneInfo == null) {
            this.mSsAnimSceneArrays.put(scene, new SsSceneInfo(sceneDescription, animCostThreshold));
        } else {
            sceneInfo.initSsAnimInfo(sceneDescription, animCostThreshold);
        }
    }

    private void latencySceneSettle(int scene, ArrayList<String> latencyInfo) {
        LatencySceneInfo sceneInfo = null;
        if (this.mSceneInfo.getLatencyThresholdNs() > 0) {
            LatencySceneInfo sceneInfo2 = this.mLatencySceneArrays.get(scene);
            sceneInfo = sceneInfo2;
            if (sceneInfo == null) {
                sceneInfo = new LatencySceneInfo();
                this.mLatencySceneArrays.put(scene, sceneInfo);
            }
        }
        if (sceneInfo != null) {
            long latencyTime = System.nanoTime() - this.mSceneInfo.getSceneBeginTime();
            if (Binder.getCallingUid() == 1000 && 1 == scene) {
                handleTaskOperationLatency(sceneInfo, latencyTime);
                this.mLastTopPackageName = null;
                this.mLastTopActivityName = null;
                this.mStartingWindowPkg = null;
                return;
            }
            if (latencyTime < this.mSceneInfo.getLatencyThresholdNs()) {
                return;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(this.mSceneInfo.getSceneDescription());
            builder.append(":");
            builder.append(latencyTime);
            builder.append(COMMA_SEPARATOR);
            builder.append(sceneInfo.getLatencyPreCount());
            if (latencyInfo != null) {
                Iterator<String> it = latencyInfo.iterator();
                while (it.hasNext()) {
                    String tag = it.next();
                    builder.append(COMMA_SEPARATOR);
                    builder.append(tag);
                }
            }
            Log.p("Quality", builder.toString());
            sceneInfo.resetPreCount();
        }
    }

    private void handleTaskOperationLatency(LatencySceneInfo sceneInfo, long latencyTime) {
        long latencyThresholdNs;
        if (this.mLastTopPackageName == null) {
            return;
        }
        sceneInfo.addLatencyPreCount();
        if (!"com.android.launcher".equals(this.mLastTopPackageName)) {
            latencyThresholdNs = this.mSceneInfo.getLatencyThresholdNs();
        } else {
            latencyThresholdNs = SLOW_LAUNCH_THRESHOLD;
        }
        long latencyThreshold = latencyThresholdNs;
        if (latencyTime >= latencyThreshold) {
            Log.p("Quality", this.mSceneInfo.getSceneDescription() + ":" + latencyTime + COMMA_SEPARATOR + sceneInfo.getLatencyPreCount() + COMMA_SEPARATOR + this.mLastTopPackageName + COMMA_SEPARATOR + this.mLastTopActivityName + COMMA_SEPARATOR + this.mTopPackageName + COMMA_SEPARATOR + this.mTopActivityName + COMMA_SEPARATOR + this.mStartingWindowPkg);
            OplusFrameworkStatsLog.write(100075, System.currentTimeMillis(), this.mSceneInfo.getSceneDescription(), latencyTime, sceneInfo.getLatencyPreCount(), latencyThreshold, this.mLastTopPackageName, this.mLastTopActivityName, this.mTopPackageName, this.mTopActivityName, this.mStartingWindowPkg);
            sceneInfo.resetPreCount();
        }
    }

    private void gfxSceneSettle(int scene) {
    }
}
