package com.oplus.resolver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;

/* loaded from: classes.dex */
public class OplusResolverCircleProgressBar extends View {
    public static final int ACCURACY = 2;
    private static final float ANIMATOR_DURATION_FACTOR = 1.5f;
    private static final int ANIMATOR_SHORTEST_DURATION = 180;
    private static final boolean DEBUG = true;
    private static final PathInterpolator DEFAULT_DISAPPEAR_INTERPOLATOR = new PathInterpolator(0.3f, 0.0f, 0.0f, 1.0f);
    private static final int DISAPPEAR_DELAY = 300;
    private static final int DISAPPEAR_DURATION = 267;
    public static final int ORIGINAL_ANGLE = -90;
    private static final int PROGRESS_POINT_COUNT = 360;
    private static final String TAG = "OplusResolverCircleProgressBar";
    private static final int TIMEOUT_SEND_ACCESSIBILITY_EVENT = 10;
    private float arcRadius;
    private AccessibilityEventSender mAccessibilityEventSender;
    private ValueAnimator mAnimator;
    private RectF mArcRect;
    private Paint mBottomCirclePaint;
    private Context mContext;
    private int mCurrentStepProgress;
    private int mHalfStrokeWidth;
    private int mHalfWidth;
    private int mHeight;
    private AccessibilityManager mManager;
    private int mMax;
    private int mProgress;
    private int mProgressBarBgCircleColor;
    private int mProgressBarColor;
    private Paint mProgressPaint;
    private int mStrokeDefaultWidth;
    private int mStrokeWidth;
    private int mWidth;

    public OplusResolverCircleProgressBar(Context context) {
        this(context, null);
    }

    public OplusResolverCircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusResolverCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mWidth = 0;
        this.mHeight = 0;
        this.mStrokeWidth = 0;
        this.mMax = 100;
        this.mProgress = 0;
        this.mCurrentStepProgress = 0;
        this.mContext = context;
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(201654443);
        this.mWidth = dimensionPixelSize;
        this.mHeight = dimensionPixelSize;
        this.mProgressBarBgCircleColor = context.getResources().getColor(201719883);
        this.mProgressBarColor = context.getResources().getColor(201719884);
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(201654444);
        this.mStrokeDefaultWidth = dimensionPixelSize2;
        this.mStrokeWidth = dimensionPixelSize2;
        init();
    }

    private void init() {
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        initBottomCirclePaint();
        initProgressPaint();
        setProgress(this.mProgress);
        setMax(this.mMax);
        this.mManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
    }

    private void initProgressPaint() {
        Paint paint = new Paint(1);
        this.mProgressPaint = paint;
        paint.setColor(this.mProgressBarColor);
        this.mProgressPaint.setStyle(Paint.Style.STROKE);
        this.mProgressPaint.setStrokeWidth(this.mStrokeWidth);
    }

    private void initBottomCirclePaint() {
        Paint paint = new Paint(1);
        this.mBottomCirclePaint = paint;
        paint.setColor(this.mProgressBarBgCircleColor);
        this.mBottomCirclePaint.setStyle(Paint.Style.STROKE);
        this.mBottomCirclePaint.setStrokeWidth(this.mStrokeWidth);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void verifyProgress() {
        int i = this.mMax;
        if (i > 0) {
            float perStep = i / 360.0f;
            int i2 = (int) (this.mProgress / perStep);
            this.mCurrentStepProgress = i2;
            if (360 - i2 < 2) {
                this.mCurrentStepProgress = 360;
            }
        }
        invalidate();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        Log.i(TAG, "mCurrentStepProgress: " + this.mCurrentStepProgress + ",mArcRect: " + this.mArcRect + ",mHalfWidth:" + this.mHalfWidth);
        canvas.save();
        int i = this.mHalfWidth;
        canvas.rotate(-90.0f, i, i);
        canvas.drawArc(this.mArcRect, this.mCurrentStepProgress, 360.0f, false, this.mBottomCirclePaint);
        canvas.drawArc(this.mArcRect, 0.0f, this.mCurrentStepProgress, false, this.mProgressPaint);
        canvas.restore();
    }

    public void setProgress(int progress) {
        Log.i(TAG, "setProgress: " + progress);
        if (progress < 0) {
            progress = 0;
        }
        if (progress > this.mMax) {
            progress = this.mMax;
        }
        if (getVisibility() != 0 || this.mProgress >= progress) {
            Log.i(TAG, "the view is invisible or progress is small");
            this.mProgress = progress;
            return;
        }
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        startAnimator(this.mProgress, progress);
        onProgressRefresh();
    }

    public int getProgress() {
        return this.mProgress;
    }

    public void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        if (max != this.mMax) {
            this.mMax = max;
            if (this.mProgress > max) {
                this.mProgress = max;
            }
        }
        Log.i(TAG, "setMax mMax = " + this.mMax);
        verifyProgress();
    }

    public int getMax() {
        return this.mMax;
    }

    void onProgressRefresh() {
        AccessibilityManager accessibilityManager = this.mManager;
        if (accessibilityManager != null && accessibilityManager.isEnabled() && this.mManager.isTouchExplorationEnabled()) {
            scheduleAccessibilityEventSender();
        }
    }

    private void scheduleAccessibilityEventSender() {
        AccessibilityEventSender accessibilityEventSender = this.mAccessibilityEventSender;
        if (accessibilityEventSender == null) {
            this.mAccessibilityEventSender = new AccessibilityEventSender();
        } else {
            removeCallbacks(accessibilityEventSender);
        }
        postDelayed(this.mAccessibilityEventSender, 10L);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        AccessibilityEventSender accessibilityEventSender = this.mAccessibilityEventSender;
        if (accessibilityEventSender != null) {
            removeCallbacks(accessibilityEventSender);
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.mProgress = this.mProgress;
        return ss;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i1, int i2, int i3) {
        super.onSizeChanged(i, i1, i2, i3);
        this.mHalfStrokeWidth = this.mStrokeWidth / 2;
        this.mHalfWidth = getWidth() / 2;
        this.arcRadius = r0 - this.mHalfStrokeWidth;
        int i4 = this.mHalfWidth;
        float f = this.arcRadius;
        this.mArcRect = new RectF(i4 - f, i4 - f, i4 + f, i4 + f);
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setProgress(ss.mProgress);
        requestLayout();
    }

    private void startAnimator(final int currentProgress, int targetProgress) {
        Log.i(TAG, "currentProgress: " + currentProgress + ",targetProgress=" + targetProgress);
        ValueAnimator ofInt = ValueAnimator.ofInt(targetProgress - currentProgress);
        this.mAnimator = ofInt;
        ofInt.setDuration((int) (((targetProgress - currentProgress) * ANIMATOR_DURATION_FACTOR) + 180.0f));
        this.mAnimator.setInterpolator(new LinearInterpolator());
        this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.resolver.OplusResolverCircleProgressBar.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = ((Integer) animation.getAnimatedValue()).intValue();
                OplusResolverCircleProgressBar.this.mProgress = currentProgress + value;
                OplusResolverCircleProgressBar.this.verifyProgress();
                if (OplusResolverCircleProgressBar.this.mProgress == OplusResolverCircleProgressBar.this.mMax) {
                    OplusResolverCircleProgressBar.this.disappearSelf();
                }
            }
        });
        this.mAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disappearSelf() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
        animator.setInterpolator(DEFAULT_DISAPPEAR_INTERPOLATOR);
        animator.setDuration(267L);
        animator.setStartDelay(300L);
        animator.addListener(new AnimatorListenerAdapter() { // from class: com.oplus.resolver.OplusResolverCircleProgressBar.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                OplusResolverCircleProgressBar.this.setVisibility(8);
                OplusResolverCircleProgressBar.this.setAlpha(1.0f);
                OplusResolverCircleProgressBar.this.release();
            }
        });
        animator.start();
    }

    public void release() {
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mAnimator.cancel();
            this.mAnimator = null;
        }
        this.mProgress = 0;
        this.mCurrentStepProgress = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AccessibilityEventSender implements Runnable {
        private AccessibilityEventSender() {
        }

        @Override // java.lang.Runnable
        public void run() {
            OplusResolverCircleProgressBar.this.sendAccessibilityEvent(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.oplus.resolver.OplusResolverCircleProgressBar.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int mProgress;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.mProgress = ((Integer) in.readValue(null)).intValue();
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(Integer.valueOf(this.mProgress));
        }

        public String toString() {
            return "OplusCircleProgressBar.SavedState { " + Integer.toHexString(System.identityHashCode(this)) + " mProgress = " + this.mProgress + " }";
        }
    }
}
