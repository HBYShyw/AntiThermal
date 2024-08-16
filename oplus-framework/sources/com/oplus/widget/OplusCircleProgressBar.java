package com.oplus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import com.oplus.internal.R;
import com.oplus.os.WaveformEffect;
import com.oplus.util.OplusContextUtil;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class OplusCircleProgressBar extends View {
    public static final int ACCURACY = 2;
    private static final int ALPHA_SHOW_DURATION = 360;
    private static final float BASE_PROGRESS_POINT_ALPHA = 0.215f;
    public static final int DEFAULT_TYPE = 0;
    public static final int LARGE_TYPE = 2;
    public static final int MEDIUM_TYPE = 1;
    private static final int ONE_CIRCLE_DEGREE = 360;
    public static final int ORIGINAL_ANGLE = -90;
    private static final int PROGRESS_POINT_COUNT = 360;
    private static final String TAG = "OplusCircleProgressBar";
    private static final int TIMEOUT_SEND_ACCESSIBILITY_EVENT = 10;
    private final boolean DEBUG;
    private float arcRadius;
    private AccessibilityEventSender mAccessibilityEventSender;
    private RectF mArcRect;
    private Paint mBackGroundPaint;
    private float mCenterX;
    private float mCenterY;
    private Context mContext;
    private int mCurrentStepProgress;
    private int mHalfStrokeWidth;
    private int mHalfWidth;
    private int mHeight;
    private AccessibilityManager mManager;
    private int mMax;
    private ArrayList<ProgressPoint> mPointList;
    private int mPointRadius;
    private int mPreStepProgress;
    private int mProgress;
    private int mProgressBarBgCircleColor;
    private int mProgressBarColor;
    private int mProgressBarType;
    private Paint mProgressPaint;
    private float mStepDegree;
    private int mStrokeDefaultWidth;
    private int mStrokeLargeWidth;
    private int mStrokeMediumWidth;
    private int mStrokeWidth;
    private int mWidth;

    public OplusCircleProgressBar(Context context) {
        this(context, null);
    }

    public OplusCircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 201392152);
    }

    public OplusCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.DEBUG = false;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mProgressBarType = 0;
        this.mStrokeWidth = 0;
        this.mPointRadius = 0;
        this.mMax = 100;
        this.mProgress = 0;
        this.mCurrentStepProgress = 0;
        this.mPreStepProgress = -1;
        this.mStepDegree = 1.0f;
        this.mPointList = new ArrayList<>();
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OplusCircleProgressBar, defStyleAttr, 0);
        int length = getResources().getDimensionPixelSize(201654376);
        this.mWidth = a.getDimensionPixelSize(1, length);
        this.mHeight = a.getDimensionPixelSize(2, length);
        this.mProgressBarType = a.getInteger(0, 0);
        int defaultColor = OplusContextUtil.getAttrColor(context, 201392133);
        int defaultBgCircleColor = OplusContextUtil.getAttrColor(context, 201392134);
        this.mProgressBarBgCircleColor = a.getColor(3, defaultBgCircleColor);
        this.mProgressBarColor = a.getColor(4, defaultColor);
        this.mProgress = a.getInteger(5, this.mProgress);
        this.mMax = a.getInteger(6, this.mMax);
        a.recycle();
        this.mStrokeDefaultWidth = context.getResources().getDimensionPixelSize(201654375);
        this.mStrokeMediumWidth = context.getResources().getDimensionPixelSize(201654305);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(201654377);
        this.mStrokeLargeWidth = dimensionPixelSize;
        this.mStrokeWidth = this.mStrokeDefaultWidth;
        int i = this.mProgressBarType;
        if (1 == i) {
            this.mStrokeWidth = this.mStrokeMediumWidth;
        } else if (2 == i) {
            this.mStrokeWidth = dimensionPixelSize;
        }
        this.mPointRadius = this.mStrokeWidth >> 1;
        this.mCenterX = this.mWidth >> 1;
        this.mCenterY = this.mHeight >> 1;
        init();
    }

    private void init() {
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        initBackgroundPaint();
        initProgressPaint();
        setProgress(this.mProgress);
        setMax(this.mMax);
        this.mManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
    }

    private void initProgressPaint() {
        Paint paint = new Paint(1);
        this.mProgressPaint = paint;
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mProgressPaint.setColor(this.mProgressBarColor);
        this.mProgressPaint.setStyle(Paint.Style.STROKE);
        this.mProgressPaint.setStrokeWidth(this.mStrokeWidth);
        this.mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initBackgroundPaint() {
        Paint paint = new Paint(1);
        this.mBackGroundPaint = paint;
        paint.setColor(this.mProgressBarBgCircleColor);
        this.mBackGroundPaint.setStyle(Paint.Style.STROKE);
    }

    private void verifyProgress() {
        int i = this.mMax;
        if (i > 0) {
            float perStep = i / 360.0f;
            int i2 = (int) (this.mProgress / perStep);
            this.mCurrentStepProgress = i2;
            if (360 - i2 < 2) {
                this.mCurrentStepProgress = WaveformEffect.EFFECT_RINGTONE_ROCK;
            }
            this.mPreStepProgress = this.mCurrentStepProgress;
        } else {
            this.mCurrentStepProgress = 0;
            this.mPreStepProgress = 0;
        }
        invalidate();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        drawBackgroudCicle(canvas);
        canvas.save();
        int i = this.mHalfWidth;
        canvas.rotate(-90.0f, i, i);
        canvas.drawArc(this.mArcRect, 0.0f, this.mCurrentStepProgress, false, this.mProgressPaint);
        canvas.restore();
    }

    private void drawBackgroudCicle(Canvas canvas) {
        this.mBackGroundPaint.setStrokeWidth(this.mStrokeWidth);
        int i = this.mHalfWidth;
        canvas.drawCircle(i, i, this.arcRadius, this.mBackGroundPaint);
    }

    public void setProgress(int progress) {
        Log.i(TAG, "setProgress: " + progress);
        if (progress < 0) {
            progress = 0;
        }
        if (progress > this.mMax) {
            progress = this.mMax;
        }
        if (progress != this.mProgress) {
            this.mProgress = progress;
        }
        verifyProgress();
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

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AccessibilityEventSender implements Runnable {
        private AccessibilityEventSender() {
        }

        @Override // java.lang.Runnable
        public void run() {
            OplusCircleProgressBar.this.sendAccessibilityEvent(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.oplus.widget.OplusCircleProgressBar.SavedState.1
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

    /* loaded from: classes.dex */
    private class ProgressPoint {
        float currentAlpha;

        public ProgressPoint() {
        }

        public float getCurrentAlpha() {
            return this.currentAlpha;
        }

        public void setCurrentAlpha(float currentAlpha) {
            this.currentAlpha = currentAlpha;
        }
    }
}
