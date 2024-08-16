package com.oplus.dynamicframerate;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.ViewRootImpl;
import android.view.animation.AnimationUtils;
import com.oplus.dynamicframerate.AnimationSpeedAware;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class SceneManager implements ISceneManager {
    private Object mActiveVri;
    private IAnimationSpeedAware mAnimationSpeedAware;
    private IDynamicFrameRateController mController;
    private Handler mHandler;
    private final boolean mIsEnable;
    private final boolean mIsEnableSkipScheduleTraversals;
    private long mMultiWindowIdleFrameIntervalNanos;
    private long mPreviousVsyncTimeNanos;
    private Map<ViewRootImpl, Long> mSkipVriMap;
    private static final String TAG = SceneManager.class.getSimpleName();
    private static final DefaultAnimationSpeedAware DEFAULT_INSTANCE = new DefaultAnimationSpeedAware();

    public SceneManager(boolean isEnable, IDynamicFrameRateController controller) {
        this.mIsEnable = isEnable;
        if (!isEnable) {
            this.mAnimationSpeedAware = DEFAULT_INSTANCE;
        } else {
            this.mAnimationSpeedAware = new AnimationSpeedAware(this);
            this.mSkipVriMap = new HashMap();
        }
        this.mController = controller;
        this.mIsEnableSkipScheduleTraversals = false;
    }

    public IAnimationSpeedAware getAnimationSpeedAware() {
        return this.mAnimationSpeedAware;
    }

    public AnimationSpeedAware.AnimationInfo getUpdatedAnimationInfo() {
        AnimationSpeedAware animationSpeedAware = (AnimationSpeedAware) this.mAnimationSpeedAware;
        animationSpeedAware.syncInfoTogether();
        AnimationSpeedAware.AnimationInfo info = animationSpeedAware.getAnimationInfo();
        return info;
    }

    public void resetAnimationInfo() {
        AnimationSpeedAware animationSpeedAware = (AnimationSpeedAware) this.mAnimationSpeedAware;
        animationSpeedAware.resetAnimationInfo();
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
        this.mAnimationSpeedAware.setHandler(handler);
    }

    public boolean shouldSkipScheduleTraversals(Object vri) {
        if (!isValidForSkipScheduleTraversals()) {
            return false;
        }
        Object obj = this.mActiveVri;
        if (obj == null || obj == vri) {
            if (OplusDebugUtil.DEBUG) {
                OplusDebugUtil.e(TAG, "shouldSkipScheduleTraversals: (mActiveVri == null || mActiveVri == vri ) case; vri = " + vri + ", mActiveVri = " + this.mActiveVri);
            }
            return false;
        }
        long vsyncTimeNanos = AnimationUtils.currentAnimationTimeMillis() * 1000000;
        long j = this.mPreviousVsyncTimeNanos;
        long gap = (vsyncTimeNanos - j) + 500000;
        if (gap > this.mMultiWindowIdleFrameIntervalNanos) {
            if (OplusDebugUtil.DEBUG) {
                OplusDebugUtil.d(TAG, "shouldSkipScheduleTraversals: gap not satisfy case; mPreviousVsyncTimeNanos = " + this.mPreviousVsyncTimeNanos + ", vsyncTimeNanos = " + vsyncTimeNanos + ", mMultiWindowIdleFrameIntervalNanos = " + this.mMultiWindowIdleFrameIntervalNanos + ", vri = " + vri + ", mActiveVri = " + this.mActiveVri);
                OplusDebugUtil.trace("shouldSkipScheduleTraversals: gap not satisfy case; mPreviousVsyncTimeNanos = " + this.mPreviousVsyncTimeNanos + ", vsyncTimeNanos = " + vsyncTimeNanos + ", mMultiWindowIdleFrameIntervalNanos = " + this.mMultiWindowIdleFrameIntervalNanos + ", vri = " + vri + ", mActiveVri = " + this.mActiveVri);
            }
            this.mPreviousVsyncTimeNanos = vsyncTimeNanos;
            return false;
        }
        this.mSkipVriMap.put((ViewRootImpl) vri, Long.valueOf((j + gap) / 1000000));
        if (OplusDebugUtil.DEBUG) {
            OplusDebugUtil.d(TAG, "shouldSkipScheduleTraversals: skip; mPreviousVsyncTimeNanos = " + this.mPreviousVsyncTimeNanos + ", vsyncTimeNanos = " + vsyncTimeNanos + ", mMultiWindowIdleFrameIntervalNanos = " + this.mMultiWindowIdleFrameIntervalNanos + ", vri = " + vri + ", mActiveVri = " + this.mActiveVri);
            OplusDebugUtil.trace("shouldSkipScheduleTraversals: skip; mPreviousVsyncTimeNanos = " + this.mPreviousVsyncTimeNanos + ", vsyncTimeNanos = " + vsyncTimeNanos + ", mMultiWindowIdleFrameIntervalNanos = " + this.mMultiWindowIdleFrameIntervalNanos + ", vri = " + vri + ", mActiveVri = " + this.mActiveVri);
            return true;
        }
        return true;
    }

    public void setMultiWindowIdleFrameIntervalNanos(long frameIntervalNanos) {
        this.mMultiWindowIdleFrameIntervalNanos = frameIntervalNanos;
    }

    @Override // com.oplus.dynamicframerate.ISceneManager
    public void onStateSlientUpdate(int state) {
        AnimationSpeedAware animationSpeedAware = (AnimationSpeedAware) this.mAnimationSpeedAware;
        AnimationSpeedAware.AnimationInfo info = animationSpeedAware.getAnimationInfo();
        this.mController.onAnimationInfoSlientUpdate(info);
    }

    @Override // com.oplus.dynamicframerate.ISceneManager
    public void onVriUpdate(Object vri) {
        if (!isValidForSkipScheduleTraversals()) {
            return;
        }
        if (this.mActiveVri != vri) {
            OplusDebugUtil.e(TAG, "onVriUpdate: active vri from [" + this.mActiveVri + "] to [" + vri + "]");
            this.mPreviousVsyncTimeNanos = 0L;
        }
        this.mActiveVri = vri;
    }

    @Override // com.oplus.dynamicframerate.ISceneManager
    public void onDoFrameFinished() {
        if (!isValidForSkipScheduleTraversals()) {
            return;
        }
        for (ViewRootImpl vri : this.mSkipVriMap.keySet()) {
            Handler handler = this.mHandler;
            handler.sendMessageAtTime(Message.obtain(handler, 1, vri), this.mSkipVriMap.get(vri).longValue());
        }
        this.mSkipVriMap.clear();
    }

    @Override // com.oplus.dynamicframerate.ISceneManager
    public void handleScheduleTraversals(Object obj) {
        if (!isValidForSkipScheduleTraversals()) {
            return;
        }
        ViewRootImpl vri = (ViewRootImpl) obj;
        vri.getWrapper().scheduleTraversals();
    }

    @Override // com.oplus.dynamicframerate.ISceneManager
    public void onUpdateInternalDisplay(Display display) {
        Display.Mode tmpMode = display.getMode();
        int width = tmpMode.getPhysicalWidth();
        int height = tmpMode.getPhysicalHeight();
        this.mAnimationSpeedAware.updateScreenSize(width, height);
    }

    private boolean isValidForSkipScheduleTraversals() {
        return this.mIsEnable && this.mIsEnableSkipScheduleTraversals && Looper.getMainLooper() == Looper.myLooper();
    }

    /* loaded from: classes.dex */
    private static class DefaultAnimationSpeedAware implements IAnimationSpeedAware {
        private DefaultAnimationSpeedAware() {
        }
    }
}
