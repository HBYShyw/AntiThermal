package com.oplus.scrolloptim;

/* loaded from: classes.dex */
public class VsyncTimeSetter {
    public static final int FRAME_INTERVAL_COMPARE_GAP_NANOS = 500000;
    private long mFrameIntervalNanos = 16666666;
    private boolean mIsScrollOptDebugEnable = OplusDebugUtil.DEBUG_SWITCH;

    public long getFrameIntervalNanos() {
        return this.mFrameIntervalNanos;
    }

    public long getFrameGapCount(long greaterNanos, long smallerNanos, boolean roundUp) {
        long more = roundUp ? 500000L : -500000L;
        return ((greaterNanos - smallerNanos) + more) / this.mFrameIntervalNanos;
    }

    public long syncWithVsync(long timeNanos, long referVsyncTimeNanos) {
        long ret;
        long j = this.mFrameIntervalNanos;
        long offset = (timeNanos - referVsyncTimeNanos) % j;
        long threshold = (j >> 1) + (j >> 2);
        if (offset < 0) {
            long offset2 = -offset;
            ret = offset2 > threshold ? timeNanos - (j - offset2) : timeNanos + offset2;
        } else if (offset > threshold) {
            ret = timeNanos + (j - offset);
        } else {
            ret = timeNanos - offset;
            if (ret + threshold < timeNanos) {
                ret = timeNanos;
            }
        }
        if (this.mIsScrollOptDebugEnable) {
            OplusDebugUtil.trace("syncWithVsync-timeNanos" + timeNanos + "-ret " + ret + "-referVsyncTimeNanos " + referVsyncTimeNanos);
        }
        return ret;
    }

    public long getNextFrameTimeNanos(long frameTimeNanos, long now, long lastFrameTimeNanos) {
        long ret;
        long j = this.mFrameIntervalNanos;
        long intendedNextFrameTimeNanos = lastFrameTimeNanos + j;
        long gap = intendedNextFrameTimeNanos - now;
        if (gap < 0) {
            int gapFrameCount = (int) ((-gap) / j);
            ret = (gapFrameCount * j) + intendedNextFrameTimeNanos;
        } else {
            ret = intendedNextFrameTimeNanos;
        }
        if (this.mIsScrollOptDebugEnable) {
            OplusDebugUtil.trace("adjustFrameTimeNanos-mLastFrameTimeNanos " + lastFrameTimeNanos + "-now " + now + "-ret " + ret);
        }
        return syncWithVsync(ret, frameTimeNanos);
    }

    public void updateFrameInterval(long newFrameIntervalNanos) {
        if (newFrameIntervalNanos > 0) {
            this.mFrameIntervalNanos = newFrameIntervalNanos;
        }
    }
}
