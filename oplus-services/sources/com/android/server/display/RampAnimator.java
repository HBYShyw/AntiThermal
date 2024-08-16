package com.android.server.display;

import android.util.FloatProperty;
import android.util.Slog;
import android.view.Choreographer;
import com.android.internal.display.BrightnessSynchronizer;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class RampAnimator<T> {
    private boolean isPrimaryAnimator;
    private float mAnimatedValue;
    private boolean mAnimating;
    private float mAnimationDecreaseMaxTimeSecs;
    private float mAnimationIncreaseMaxTimeSecs;
    private final Choreographer mChoreographer;
    private float mCurrentValue;
    private int mDisplayId;
    private boolean mFirstTime;
    private long mLastFrameTimeNanos;
    private Listener mListener;
    private final T mObject;
    private final FloatProperty<T> mProperty;
    private float mRate;
    private IRampAnimatorExt mRmpExt;
    private float mTargetValue;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Listener {
        void onAnimationEnd(boolean z);

        void onAnimationStart(boolean z);
    }

    public RampAnimator(T t, FloatProperty<T> floatProperty, boolean z) {
        this(t, floatProperty);
        this.isPrimaryAnimator = z;
        Slog.d("RampAnimator", "construct animator, isPrimaryAnimator:" + this.isPrimaryAnimator);
    }

    public RampAnimator(T t, FloatProperty<T> floatProperty) {
        this.mFirstTime = true;
        this.mRmpExt = (IRampAnimatorExt) ExtLoader.type(IRampAnimatorExt.class).base(this).create();
        this.mDisplayId = 0;
        this.isPrimaryAnimator = true;
        this.mObject = t;
        this.mProperty = floatProperty;
        this.mChoreographer = Choreographer.getInstance();
    }

    void setAnimationTimeLimits(long j, long j2) {
        this.mAnimationIncreaseMaxTimeSecs = j > 0 ? ((float) j) / 1000.0f : 0.0f;
        this.mAnimationDecreaseMaxTimeSecs = j2 > 0 ? ((float) j2) / 1000.0f : 0.0f;
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    boolean setAnimationTarget(float f, float f2) {
        float f3;
        float f4;
        boolean z;
        Listener listener;
        boolean edrHdrBrightnessNoAnimation = this.mRmpExt.getEdrHdrBrightnessNoAnimation();
        if (this.isPrimaryAnimator) {
            this.mFirstTime = edrHdrBrightnessNoAnimation || this.mRmpExt.updateRealTimeBrightness(this.mDisplayId, (f > this.mTargetValue ? 1 : (f == this.mTargetValue ? 0 : -1)) != 0, f) || this.mFirstTime;
        } else {
            this.mFirstTime = this.mRmpExt.getEdrSdrBrightnessNoAnimation();
        }
        boolean z2 = this.mFirstTime;
        if (z2 || f2 <= 0.0f) {
            if (!z2 && f == this.mCurrentValue) {
                return false;
            }
            this.mFirstTime = false;
            this.mRate = 0.0f;
            this.mTargetValue = f;
            this.mCurrentValue = f;
            setPropertyValue(f);
            if (this.mAnimating) {
                this.mAnimating = false;
            }
            Listener listener2 = this.mListener;
            if (listener2 != null) {
                listener2.onAnimationEnd(this.isPrimaryAnimator);
            }
            return true;
        }
        float f5 = this.mCurrentValue;
        if (f > f5) {
            f3 = this.mAnimationIncreaseMaxTimeSecs;
            if (f3 > 0.0f && (f - f5) / f2 > f3) {
                f4 = f - f5;
                f2 = f4 / f3;
                z = this.mAnimating;
                if (z || f2 > this.mRate || ((f <= f5 && f5 <= this.mTargetValue) || (this.mTargetValue <= f5 && f5 <= f))) {
                    this.mRate = f2;
                }
                boolean z3 = this.mTargetValue != f;
                this.mTargetValue = f;
                if (!z && f != f5) {
                    this.mRmpExt.onAnimationStart(f5, f);
                    listener = this.mListener;
                    if (listener != null) {
                        listener.onAnimationStart(this.isPrimaryAnimator);
                    }
                    this.mAnimating = true;
                    this.mAnimatedValue = this.mCurrentValue;
                }
                return z3;
            }
        }
        if (f < f5) {
            f3 = this.mAnimationDecreaseMaxTimeSecs;
            if (f3 > 0.0f && (f5 - f) / f2 > f3) {
                f4 = f5 - f;
                f2 = f4 / f3;
            }
        }
        z = this.mAnimating;
        if (z) {
        }
        this.mRate = f2;
        if (this.mTargetValue != f) {
        }
        this.mTargetValue = f;
        if (!z) {
            this.mRmpExt.onAnimationStart(f5, f);
            listener = this.mListener;
            if (listener != null) {
            }
            this.mAnimating = true;
            this.mAnimatedValue = this.mCurrentValue;
        }
        return z3;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    boolean isAnimating() {
        return this.mAnimating;
    }

    private void setPropertyValue(float f) {
        this.mRmpExt.setValue(this.mDisplayId, f);
        this.mProperty.setValue(this.mObject, f);
    }

    void performNextAnimationStep(long j) {
        float f = ((float) (j - this.mLastFrameTimeNanos)) * 1.0E-9f;
        this.mLastFrameTimeNanos = j;
        float f2 = this.mRmpExt.getBrightnessNoAnimation() ? 0.0f : 1.0f;
        if (f2 == 0.0f) {
            float f3 = this.mTargetValue;
            this.mAnimatedValue = f3;
            this.mRmpExt.animateRun(this.mCurrentValue, f3, f3, f2);
        } else {
            float f4 = this.mRate;
            float amount = this.mRmpExt.getAmount(this.mDisplayId, this.mTargetValue, this.mAnimatedValue, f, f4, (f * f4) / f2, this.isPrimaryAnimator);
            float f5 = this.mTargetValue;
            if (f5 > this.mCurrentValue) {
                this.mAnimatedValue = Math.min(this.mAnimatedValue + amount, f5);
            } else {
                this.mAnimatedValue = Math.max(this.mAnimatedValue - amount, f5);
            }
            this.mRmpExt.animateRun(this.mCurrentValue, this.mAnimatedValue, this.mTargetValue, amount);
        }
        float f6 = this.mCurrentValue;
        float f7 = this.mAnimatedValue;
        this.mCurrentValue = f7;
        if (!BrightnessSynchronizer.floatEquals(f6, f7)) {
            setPropertyValue(this.mCurrentValue);
        }
        if (this.mAnimating && BrightnessSynchronizer.floatEquals(this.mTargetValue, this.mCurrentValue)) {
            this.mAnimating = false;
            Listener listener = this.mListener;
            if (listener != null) {
                listener.onAnimationEnd(this.isPrimaryAnimator);
            }
            this.mRmpExt.onAnimationEnd(this.mCurrentValue);
        }
    }

    public void setDisplayId(int i) {
        this.mDisplayId = i;
        IRampAnimatorExt iRampAnimatorExt = this.mRmpExt;
        if (iRampAnimatorExt != null) {
            iRampAnimatorExt.setDisplayId(i);
        }
    }

    public void setLoggingEnabled(boolean z) {
        IRampAnimatorExt iRampAnimatorExt = this.mRmpExt;
        if (iRampAnimatorExt != null) {
            iRampAnimatorExt.setLoggingEnabled(z);
        }
    }

    public boolean animateTo(float f, float f2, float f3, boolean z) {
        IRampAnimatorExt iRampAnimatorExt = this.mRmpExt;
        if (iRampAnimatorExt == null) {
            return false;
        }
        iRampAnimatorExt.animateTo(this.mDisplayId, f, f2, f3, z);
        return false;
    }

    public void setLastFrameTimeNanos(long j) {
        this.mLastFrameTimeNanos = j;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static class DualRampAnimator<T> {
        private boolean mAwaitingCallback;
        private final RampAnimator<T> mFirst;
        private Listener mListener;
        private final RampAnimator<T> mSecond;
        private final Runnable mAnimationCallback = new Runnable() { // from class: com.android.server.display.RampAnimator.DualRampAnimator.1
            @Override // java.lang.Runnable
            public void run() {
                long frameTimeNanos = DualRampAnimator.this.mChoreographer.getFrameTimeNanos();
                DualRampAnimator.this.mFirst.performNextAnimationStep(frameTimeNanos);
                DualRampAnimator.this.mSecond.performNextAnimationStep(frameTimeNanos);
                if (DualRampAnimator.this.isAnimating()) {
                    DualRampAnimator.this.postAnimationCallback();
                } else {
                    Listener unused = DualRampAnimator.this.mListener;
                    DualRampAnimator.this.mAwaitingCallback = false;
                }
            }
        };
        private final Choreographer mChoreographer = Choreographer.getInstance();

        /* JADX INFO: Access modifiers changed from: package-private */
        public DualRampAnimator(T t, FloatProperty<T> floatProperty, FloatProperty<T> floatProperty2) {
            this.mFirst = new RampAnimator<>(t, floatProperty, true);
            this.mSecond = new RampAnimator<>(t, floatProperty2, false);
        }

        public void setAnimationTimeLimits(long j, long j2) {
            this.mFirst.setAnimationTimeLimits(j, j2);
            this.mSecond.setAnimationTimeLimits(j, j2);
        }

        public boolean animateTo(float f, float f2, float f3) {
            boolean animationTarget = this.mFirst.setAnimationTarget(f, f3) | this.mSecond.setAnimationTarget(f2, f3);
            boolean isAnimating = isAnimating();
            boolean z = this.mAwaitingCallback;
            if (isAnimating != z) {
                if (isAnimating) {
                    long nanoTime = System.nanoTime();
                    this.mFirst.setLastFrameTimeNanos(nanoTime);
                    this.mSecond.setLastFrameTimeNanos(nanoTime);
                    this.mAwaitingCallback = true;
                    postAnimationCallback();
                } else if (z) {
                    this.mChoreographer.removeCallbacks(1, this.mAnimationCallback, null);
                    this.mAwaitingCallback = false;
                }
            }
            return animationTarget;
        }

        public void setListener(Listener listener) {
            this.mListener = listener;
            this.mFirst.setListener(listener);
            this.mSecond.setListener(this.mListener);
        }

        public boolean isAnimating() {
            return this.mFirst.isAnimating() || this.mSecond.isAnimating();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void postAnimationCallback() {
            this.mChoreographer.postCallback(1, this.mAnimationCallback, null);
        }

        public void setDisplayId(int i) {
            this.mFirst.setDisplayId(i);
            this.mSecond.setDisplayId(i);
        }

        public void setLoggingEnabled(boolean z) {
            this.mFirst.setLoggingEnabled(z);
            this.mSecond.setLoggingEnabled(z);
        }

        public boolean animateTo(float f, float f2, float f3, boolean z) {
            return this.mFirst.animateTo(f, f2, f3, z);
        }
    }
}
