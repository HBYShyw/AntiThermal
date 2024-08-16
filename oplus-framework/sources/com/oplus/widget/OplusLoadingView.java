package com.oplus.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.android.internal.widget.OplusViewExplorerByTouchHelper;
import com.oplus.internal.R;
import com.oplus.util.OplusContextUtil;

/* loaded from: classes.dex */
public class OplusLoadingView extends View {
    private static final boolean DEBUG = false;
    public static final int DEFAULT_TYPE = 1;
    private static final float LARGE_POINT_END_ALPHA = 1.0f;
    private static final float LARGE_POINT_START_ALPHA = 0.215f;
    public static final int LARGE_TYPE = 2;
    private static final float MEDIUM_POINT_END_ALPHA = 0.4f;
    private static final float MEDIUM_POINT_START_ALPHA = 0.1f;
    public static final int MEDIUM_TYPE = 1;
    private static final int ONE_CIRCLE_DEGREE = 360;
    private static final int ONE_CYCLE_DURATION = 960;
    private static final int PROGRESS_POINT_COUNT = 12;
    private static final String TAG = "OplusLoadingView";
    private String mAccessDescription;
    private float mCenterX;
    private float mCenterY;
    private Context mContext;
    private float mCurrentDegree;
    private float mEndAlpha;
    private int mHeight;
    private int mLoadingType;
    private int mLoadingViewColor;
    private OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction mOplusViewTalkBalkInteraction;
    private int mPointRadius;
    private float[] mPointsAlpha;
    private ValueAnimator mProgressAnimator;
    private Paint mProgressPaint;
    private float mStartAlpha;
    private float mStepDegree;
    private int mStrokeDefaultWidth;
    private int mStrokeLargeWidth;
    private int mStrokeMediumWidth;
    private int mStrokeWidth;
    private OplusViewExplorerByTouchHelper mTouchHelper;
    private int mWidth;

    public OplusLoadingView(Context context) {
        this(context, null);
    }

    public OplusLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 201392174);
    }

    public OplusLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, 201392174, 0);
    }

    public OplusLoadingView(Context context, AttributeSet attrs, int defStyle, int styleRes) {
        super(context, attrs, defStyle);
        this.mPointsAlpha = new float[12];
        this.mWidth = 0;
        this.mHeight = 0;
        this.mLoadingType = 1;
        this.mStrokeWidth = 0;
        this.mPointRadius = 0;
        this.mStepDegree = 30.0f;
        this.mAccessDescription = null;
        this.mStartAlpha = 0.1f;
        this.mEndAlpha = MEDIUM_POINT_END_ALPHA;
        this.mOplusViewTalkBalkInteraction = new OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction() { // from class: com.oplus.widget.OplusLoadingView.2
            private int mVirtualViewId = -1;

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public void getItemBounds(int position, Rect rect) {
                if (position == 0) {
                    rect.set(0, 0, OplusLoadingView.this.mWidth, OplusLoadingView.this.mHeight);
                }
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public void performAction(int virtualViewId, int actiontype, boolean resolvePara) {
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public int getCurrentPosition() {
                return -1;
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public int getItemCounts() {
                return 1;
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public int getVirtualViewAt(float x, float y) {
                if (x >= 0.0f && x <= OplusLoadingView.this.mWidth && y >= 0.0f && y <= OplusLoadingView.this.mHeight) {
                    return 0;
                }
                return -1;
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public CharSequence getItemDescription(int virtualViewId) {
                if (OplusLoadingView.this.mAccessDescription != null) {
                    return OplusLoadingView.this.mAccessDescription;
                }
                return getClass().getSimpleName();
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public CharSequence getClassName() {
                return OplusLoadingView.class.getName();
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public int getDisablePosition() {
                return -1;
            }
        };
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OplusLoadingView, defStyle, 0);
        int length = getResources().getDimensionPixelSize(201654376);
        this.mWidth = a.getDimensionPixelSize(3, length);
        this.mHeight = a.getDimensionPixelSize(1, length);
        this.mLoadingType = a.getInteger(2, 1);
        int defaultColor = OplusContextUtil.getAttrColor(context, 201392133);
        this.mLoadingViewColor = a.getColor(0, defaultColor);
        a.recycle();
        this.mStrokeDefaultWidth = context.getResources().getDimensionPixelSize(201654375);
        this.mStrokeMediumWidth = context.getResources().getDimensionPixelSize(201654305);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(201654377);
        this.mStrokeLargeWidth = dimensionPixelSize;
        this.mStrokeWidth = this.mStrokeDefaultWidth;
        int i = this.mLoadingType;
        if (1 == i) {
            this.mStrokeWidth = this.mStrokeMediumWidth;
            this.mStartAlpha = 0.1f;
            this.mEndAlpha = MEDIUM_POINT_END_ALPHA;
        } else if (2 == i) {
            this.mStrokeWidth = dimensionPixelSize;
            this.mStartAlpha = LARGE_POINT_START_ALPHA;
            this.mEndAlpha = 1.0f;
        }
        this.mPointRadius = this.mStrokeWidth >> 1;
        this.mCenterX = this.mWidth >> 1;
        this.mCenterY = this.mHeight >> 1;
        OplusViewExplorerByTouchHelper oplusViewExplorerByTouchHelper = new OplusViewExplorerByTouchHelper(this);
        this.mTouchHelper = oplusViewExplorerByTouchHelper;
        oplusViewExplorerByTouchHelper.setOplusViewTalkBalkInteraction(this.mOplusViewTalkBalkInteraction);
        setAccessibilityDelegate(this.mTouchHelper);
        setImportantForAccessibility(1);
        this.mAccessDescription = this.mContext.getString(201588872);
        Paint paint = new Paint(1);
        this.mProgressPaint = paint;
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mProgressPaint.setColor(this.mLoadingViewColor);
    }

    private void createAnimator() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mProgressAnimator = ofFloat;
        ofFloat.setDuration(960L);
        this.mProgressAnimator.setInterpolator(new LinearInterpolator());
        this.mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.widget.OplusLoadingView.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                OplusLoadingView.this.setPointsAlpha(fraction);
                OplusLoadingView.this.invalidate();
            }
        });
        this.mProgressAnimator.setRepeatMode(1);
        this.mProgressAnimator.setRepeatCount(-1);
        this.mProgressAnimator.setInterpolator(new LinearInterpolator());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPointsAlpha(float fraction) {
        float hideDuration = 0.083333336f * 4.0f;
        float totalDuration = 0.083333336f + hideDuration;
        float deltaAlpha = this.mEndAlpha - this.mStartAlpha;
        int fullCount = ((int) ((1.0f - totalDuration) / 0.083333336f)) + 1;
        for (int i = 0; i < 12; i++) {
            if (i > fullCount + (fraction / 0.083333336f)) {
                this.mPointsAlpha[i] = (((-deltaAlpha) / (0.083333336f * 4.0f)) * (fraction - (((i - fullCount) - 1) * 0.083333336f))) + (deltaAlpha / 4.0f) + this.mStartAlpha;
            } else if (i * 0.083333336f <= fraction && fraction < (i + 1) * 0.083333336f) {
                this.mPointsAlpha[i] = ((deltaAlpha / 0.083333336f) * (fraction - (i * 0.083333336f))) + this.mStartAlpha;
            } else if ((i + 1) * 0.083333336f <= fraction && fraction <= (i + 5) * 0.083333336f) {
                this.mPointsAlpha[i] = (((-deltaAlpha) / (0.083333336f * 4.0f)) * (fraction - (i * 0.083333336f))) + (deltaAlpha / 4.0f) + this.mEndAlpha;
            }
        }
    }

    private void destroyAnimator() {
        ValueAnimator valueAnimator = this.mProgressAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.mProgressAnimator.removeAllListeners();
            this.mProgressAnimator.removeAllUpdateListeners();
            this.mProgressAnimator = null;
        }
    }

    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        createAnimator();
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroyAnimator();
    }

    private void startAnimations() {
        ValueAnimator valueAnimator = this.mProgressAnimator;
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                this.mProgressAnimator.cancel();
            }
            this.mProgressAnimator.start();
        }
    }

    private void cancelAnimations() {
        ValueAnimator valueAnimator = this.mProgressAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    @Override // android.view.View
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == 0) {
            startAnimations();
        } else {
            cancelAnimations();
        }
    }

    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < 12; i++) {
            float f = this.mStepDegree;
            float f2 = this.mCenterX;
            canvas.rotate(f, f2, f2);
            int alpha = (int) (this.mPointsAlpha[i] * 255.0f);
            this.mProgressPaint.setAlpha(alpha);
            float f3 = this.mCenterX;
            int i2 = this.mPointRadius;
            canvas.drawCircle(f3, i2, i2, this.mProgressPaint);
        }
        canvas.restore();
    }
}
