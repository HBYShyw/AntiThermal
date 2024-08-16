package com.oplus.scrolloptim;

import android.os.Trace;
import com.oplus.scrolloptim.SceneManager;

/* loaded from: classes.dex */
public class ScrOptController {
    public static final int NUM_3 = 3;
    private static final String TAG = ScrOptController.class.getSimpleName();
    private static volatile ScrOptController sInstance;
    private long mFrameInsertTimeNanos;
    private long mFrameTimeNanos;
    private long mLastFrameTimeNanos;
    private SceneManager.OptimConfig mOptimConfig;
    private long mOriginalFrameTimeNanos;
    private SceneManager mSceneManager;
    private long mStartNanos;
    private VsyncTimeSetter mVsyncTimeSetter;
    private boolean mInAnimAheadState = false;
    private boolean mIsInsertingFrame = false;
    private boolean mIsScrollOptDebugEnable = OplusDebugUtil.DEBUG_SWITCH;
    private boolean mHasAdjusted = false;
    private boolean mIsTimeBackward = false;
    private boolean mNeedForceDraw = false;
    private long mLastFrameAnimOptTimeNanos = 0;

    public ScrOptController() {
        SceneManager sceneManager = new SceneManager();
        this.mSceneManager = sceneManager;
        this.mOptimConfig = sceneManager.getOptimConfig();
        this.mVsyncTimeSetter = new VsyncTimeSetter();
    }

    public static ScrOptController getInstance() {
        if (sInstance == null) {
            sInstance = new ScrOptController();
        }
        return sInstance;
    }

    public SceneManager getSceneManager() {
        return this.mSceneManager;
    }

    public VsyncTimeSetter getVsyncTimeSetter() {
        return this.mVsyncTimeSetter;
    }

    public boolean hasAdjustedFrameTimeNanos() {
        return this.mHasAdjusted;
    }

    public void updateFrameTimeNanos(long frameTimeNanos, long lastFrameTimeNanos, long startNanos) {
        long vsyncTimeNanos;
        this.mFrameTimeNanos = frameTimeNanos;
        this.mStartNanos = startNanos;
        if (isScrollOptEnabledScene()) {
            if (!isInsertingFrame()) {
                this.mOriginalFrameTimeNanos = frameTimeNanos;
            }
            if (isInAnimAheadState()) {
                this.mFrameTimeNanos = this.mVsyncTimeSetter.syncWithVsync(this.mLastFrameAnimOptTimeNanos, this.mFrameTimeNanos);
                if (this.mIsScrollOptDebugEnable) {
                    OplusDebugUtil.trace("use pre anim time-" + (this.mLastFrameAnimOptTimeNanos / 1000000) + " to " + (this.mFrameTimeNanos / 1000000));
                }
            } else if (isFling()) {
                if (isInsertingFrame()) {
                    this.mFrameTimeNanos = this.mFrameInsertTimeNanos;
                } else {
                    long vsyncTimeNanos2 = this.mLastFrameTimeNanos;
                    long j = this.mLastFrameTimeNanos;
                    if (j < lastFrameTimeNanos) {
                        int gapCount = (int) ((lastFrameTimeNanos - j) / this.mVsyncTimeSetter.getFrameIntervalNanos());
                        long vsyncTimeNanos3 = this.mLastFrameTimeNanos + (gapCount * this.mVsyncTimeSetter.getFrameIntervalNanos());
                        vsyncTimeNanos = vsyncTimeNanos3;
                    } else {
                        vsyncTimeNanos = vsyncTimeNanos2;
                    }
                    this.mFrameTimeNanos = this.mVsyncTimeSetter.getNextFrameTimeNanos(this.mFrameTimeNanos, startNanos, vsyncTimeNanos);
                    if (this.mIsScrollOptDebugEnable) {
                        OplusDebugUtil.trace("adjustFrameTimeNanos frameTimeMills-" + (frameTimeNanos / 1000000) + " to " + (this.mFrameTimeNanos / 1000000) + "(lastFrameTimeNanos=" + lastFrameTimeNanos + ")");
                    }
                }
            }
            long j2 = this.mFrameTimeNanos;
            this.mHasAdjusted = j2 != frameTimeNanos;
            if (!this.mInAnimAheadState) {
                this.mIsTimeBackward = j2 - this.mLastFrameTimeNanos < 500000;
            } else {
                this.mIsTimeBackward = false;
            }
            boolean z = this.mIsTimeBackward;
            if (!z) {
                this.mLastFrameTimeNanos = j2;
            }
            if (this.mIsScrollOptDebugEnable && z) {
                OplusDebugUtil.trace("TimeBackward occurs! mFrameTimeNanos=" + this.mFrameTimeNanos + ", mLastFrameTimeNanos=" + this.mLastFrameTimeNanos);
            }
        }
    }

    public void onDoFrameFinished() {
        this.mSceneManager.onDoFrameFinished();
    }

    public boolean isTimeBackward() {
        return this.mIsTimeBackward;
    }

    public boolean isNeedForceDraw() {
        return this.mNeedForceDraw;
    }

    public boolean isPreDraw() {
        return this.mFrameTimeNanos > this.mStartNanos;
    }

    public void setNeedForceDraw(boolean needForceDraw) {
        this.mNeedForceDraw = needForceDraw;
    }

    public void setLastFrameAnimOptTimeNanos(long timeNanos) {
        this.mLastFrameAnimOptTimeNanos = timeNanos;
    }

    public void setFrameInsertTimeNanos(long timeNanos) {
        this.mFrameInsertTimeNanos = timeNanos;
    }

    public long getLastFrameAnimOptTimeNanos() {
        return this.mLastFrameAnimOptTimeNanos;
    }

    public long getOriginalFrameTimeNanos() {
        return this.mOriginalFrameTimeNanos;
    }

    public long getFrameTimeNanos() {
        return this.mFrameTimeNanos;
    }

    public long getLastFrameTimeNanos() {
        return this.mLastFrameTimeNanos;
    }

    public boolean isScrollOptDebugEnable() {
        return this.mIsScrollOptDebugEnable;
    }

    public void updateFrameInterval(long frameIntervalNanos) {
        this.mVsyncTimeSetter.updateFrameInterval(frameIntervalNanos);
    }

    public boolean reachInsertCountThreshold(long frameTimeNanos, long nextFrameTimeNanos) {
        long count = this.mVsyncTimeSetter.getFrameGapCount(nextFrameTimeNanos, frameTimeNanos, true);
        if (this.mIsScrollOptDebugEnable) {
            OplusDebugUtil.trace("reachInsertCountThreshold: nextFrameTimeNanos = " + (nextFrameTimeNanos / 1000000) + ", frameTimeNanos = " + (frameTimeNanos / 1000000) + ", count = " + count + ", (count > mOptimConfig.checkInsertNum()) = " + (count > ((long) this.mOptimConfig.checkInsertNum())));
        }
        return count > ((long) this.mOptimConfig.checkInsertNum());
    }

    public long getNextFrameTimeNanos(long frameTimeNanos, long now, long lastFrameTimeNanos) {
        return this.mVsyncTimeSetter.getNextFrameTimeNanos(frameTimeNanos, now, lastFrameTimeNanos);
    }

    public void traceBeginForOptimizeSlidingEffect() {
        if (this.mIsScrollOptDebugEnable) {
            Trace.traceBegin(8L, "Choreographer#doFrame_extra, -start " + (this.mStartNanos / 1000000) + ", -original " + (this.mOriginalFrameTimeNanos / 1000000) + ", -real " + (this.mFrameTimeNanos / 1000000) + ", -frameIntervalMili " + (this.mVsyncTimeSetter.getFrameIntervalNanos() / 1000000) + ", -isOptEnable " + this.mOptimConfig.checkOptEnable() + ", -insertNum " + this.mOptimConfig.checkInsertNum() + ", -insertTimes " + this.mVsyncTimeSetter.getFrameGapCount(this.mFrameTimeNanos, this.mOriginalFrameTimeNanos, true) + ", -isFlinging " + isFling() + ", -isScrollChangedEnable " + this.mOptimConfig.checkIsEnabledForScrollChanged());
        }
    }

    public void traceEndForOptimizeSlidingEffect() {
        if (this.mIsScrollOptDebugEnable) {
            Trace.traceEnd(8L);
        }
    }

    public boolean isScrollOptEnabledScene() {
        return this.mOptimConfig.checkOptEnable() && (this.mOptimConfig.checkAnimAheadEnable() || this.mOptimConfig.checkFrameInsertEnable());
    }

    public boolean checkAnimAheadEnable() {
        return this.mOptimConfig.checkAnimAheadEnable();
    }

    public boolean checkFrameInsertEnable() {
        return this.mOptimConfig.checkFrameInsertEnable();
    }

    public SceneManager.OptimConfig getOptimConfig() {
        return this.mOptimConfig;
    }

    public boolean isFling() {
        return this.mSceneManager.isFling();
    }

    public boolean isInAnimAheadState() {
        return this.mInAnimAheadState;
    }

    public boolean isInsertingFrame() {
        return this.mIsInsertingFrame;
    }

    public void setAnimAheadState(boolean state) {
        this.mInAnimAheadState = state;
    }

    public void setInsertingFrame(boolean state) {
        this.mIsInsertingFrame = state;
    }
}
