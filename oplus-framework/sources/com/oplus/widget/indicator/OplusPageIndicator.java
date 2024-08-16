package com.oplus.widget.indicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.oplus.internal.R;
import com.oplus.util.OplusContextUtil;
import com.oplus.util.OplusDarkModeUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusPageIndicator extends FrameLayout implements IPagerIndicator {
    private static final boolean DEBUG = false;
    private static final int MSG_START_TRACE_ANIMATION = 17;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "OplusPageIndicator2";
    private int mCurrentPosition;
    private int mDotColor;
    private int mDotCornerRadius;
    private int mDotSize;
    private int mDotSpacing;
    private int mDotStepDistance;
    private int mDotStrokeWidth;
    private int mDotsCount;
    private float mFinalLeft;
    private float mFinalRight;
    private Handler mHandler;
    private List<View> mIndicatorDots;
    private LinearLayout mIndicatorDotsParent;
    private boolean mIsAnimated;
    private boolean mIsAnimating;
    private boolean mIsAnimatorCanceled;
    private boolean mIsClickable;
    private boolean mIsPaused;
    private boolean mIsStrokeStyle;
    private int mLastPosition;
    private boolean mNeedSettlePositionTemp;
    private OnIndicatorDotClickListener mOnDotClickListener;
    private ValueAnimator mTraceAnimator;
    private int mTraceDotColor;
    private float mTraceLeft;
    private Paint mTracePaint;
    private RectF mTraceRect;
    private float mTraceRight;
    private boolean mTranceCutTailRight;
    private int mWidth;

    /* loaded from: classes.dex */
    public interface OnIndicatorDotClickListener {
        void onClick(int i);
    }

    public OplusPageIndicator(Context context) {
        this(context, null);
    }

    public OplusPageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusPageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDotStepDistance = 0;
        this.mTraceLeft = 0.0f;
        this.mTraceRight = 0.0f;
        this.mFinalLeft = 0.0f;
        this.mFinalRight = 0.0f;
        this.mTranceCutTailRight = false;
        this.mIsAnimated = false;
        this.mIsAnimating = false;
        this.mIsAnimatorCanceled = false;
        this.mIsPaused = false;
        this.mNeedSettlePositionTemp = false;
        this.mTraceRect = new RectF();
        OplusDarkModeUtil.setForceDarkAllow(this, false);
        this.mIndicatorDots = new ArrayList();
        this.mDotSize = dpToPx(10);
        this.mDotSpacing = dpToPx(5);
        this.mDotStrokeWidth = dpToPx(2);
        this.mDotCornerRadius = this.mDotSize / 2;
        this.mDotColor = -1;
        this.mTraceDotColor = OplusContextUtil.getAttrColor(context, 201392133);
        this.mIsClickable = true;
        this.mIsStrokeStyle = true;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OplusPageIndicator);
            this.mTraceDotColor = typedArray.getColor(1, this.mTraceDotColor);
            if (!OplusDarkModeUtil.isNightMode(context)) {
                this.mDotColor = typedArray.getColor(0, this.mDotColor);
            }
            this.mDotSize = (int) typedArray.getDimension(2, this.mDotSize);
            this.mDotSpacing = (int) typedArray.getDimension(3, this.mDotSpacing);
            this.mDotCornerRadius = (int) typedArray.getDimension(5, this.mDotSize / 2);
            this.mIsClickable = typedArray.getBoolean(6, this.mIsClickable);
            this.mDotStrokeWidth = (int) typedArray.getDimension(4, this.mDotStrokeWidth);
            typedArray.recycle();
        }
        this.mTraceRect.top = 0.0f;
        this.mTraceRect.bottom = this.mDotSize;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mTraceAnimator = ofFloat;
        ofFloat.setDuration(240L);
        this.mTraceAnimator.setInterpolator(new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f));
        this.mTraceAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.widget.indicator.OplusPageIndicator.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                if (!OplusPageIndicator.this.mIsAnimatorCanceled) {
                    float diffLeft = OplusPageIndicator.this.mTraceLeft - OplusPageIndicator.this.mFinalLeft;
                    float diffRight = OplusPageIndicator.this.mTraceRight - OplusPageIndicator.this.mFinalRight;
                    float left = OplusPageIndicator.this.mTraceLeft - (diffLeft * value);
                    if (left > OplusPageIndicator.this.mTraceRect.right - OplusPageIndicator.this.mDotSize) {
                        left = OplusPageIndicator.this.mTraceRect.right - OplusPageIndicator.this.mDotSize;
                    }
                    float right = OplusPageIndicator.this.mTraceRight - (diffRight * value);
                    if (right < OplusPageIndicator.this.mTraceRect.left + OplusPageIndicator.this.mDotSize) {
                        right = OplusPageIndicator.this.mTraceLeft + OplusPageIndicator.this.mDotSize;
                    }
                    if (!OplusPageIndicator.this.mNeedSettlePositionTemp) {
                        if (OplusPageIndicator.this.mTranceCutTailRight) {
                            OplusPageIndicator.this.mTraceRect.right = right;
                        } else {
                            OplusPageIndicator.this.mTraceRect.left = left;
                        }
                    } else {
                        OplusPageIndicator.this.mTraceRect.left = left;
                        OplusPageIndicator.this.mTraceRect.right = right;
                    }
                    OplusPageIndicator.this.invalidate();
                }
            }
        });
        this.mTraceAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.oplus.widget.indicator.OplusPageIndicator.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!OplusPageIndicator.this.mIsAnimatorCanceled) {
                    OplusPageIndicator.this.mTraceRect.right = OplusPageIndicator.this.mTraceRect.left + OplusPageIndicator.this.mDotSize;
                    OplusPageIndicator.this.mNeedSettlePositionTemp = false;
                    OplusPageIndicator.this.mIsAnimated = true;
                    OplusPageIndicator.this.invalidate();
                }
                OplusPageIndicator.this.mIsAnimating = false;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                OplusPageIndicator.this.mIsAnimatorCanceled = false;
                OplusPageIndicator oplusPageIndicator = OplusPageIndicator.this;
                oplusPageIndicator.mTraceLeft = oplusPageIndicator.mTraceRect.left;
                OplusPageIndicator oplusPageIndicator2 = OplusPageIndicator.this;
                oplusPageIndicator2.mTraceRight = oplusPageIndicator2.mTraceRect.right;
            }
        });
        Paint paint = new Paint(1);
        this.mTracePaint = paint;
        paint.setStyle(Paint.Style.FILL);
        this.mTracePaint.setColor(this.mTraceDotColor);
        this.mDotStepDistance = this.mDotSize + (this.mDotSpacing * 2);
        this.mHandler = new Handler() { // from class: com.oplus.widget.indicator.OplusPageIndicator.3
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 17) {
                    OplusPageIndicator.this.startTraceAnimator();
                }
                super.handleMessage(msg);
            }
        };
        this.mIndicatorDotsParent = new LinearLayout(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        this.mIndicatorDotsParent.setLayoutParams(params);
        this.mIndicatorDotsParent.setOrientation(0);
        addView(this.mIndicatorDotsParent);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.oplus.widget.indicator.OplusPageIndicator.4
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                OplusPageIndicator.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                OplusPageIndicator oplusPageIndicator = OplusPageIndicator.this;
                oplusPageIndicator.snapToPoition(oplusPageIndicator.mCurrentPosition);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void snapToPoition(int position) {
        verifyFinalPosition(this.mCurrentPosition);
        this.mTraceRect.left = this.mFinalLeft;
        this.mTraceRect.right = this.mFinalRight;
        invalidate();
    }

    public void addDot() {
        this.mDotsCount++;
        verifyLayoutWidth();
        addIndicatorDots(1);
    }

    public void removeDot() {
        this.mDotsCount--;
        verifyLayoutWidth();
        removeIndicatorDots(1);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        RectF rectF = this.mTraceRect;
        int i = this.mDotCornerRadius;
        canvas.drawRoundRect(rectF, i, i, this.mTracePaint);
    }

    private void setupDotView(boolean stroke, View dot, int color) {
        GradientDrawable drawable = (GradientDrawable) dot.getBackground();
        if (stroke) {
            drawable.setStroke(this.mDotStrokeWidth, color);
        } else {
            drawable.setColor(color);
        }
        drawable.setCornerRadius(this.mDotCornerRadius);
    }

    private View buildDot(boolean stroke, int color) {
        View dot = LayoutInflater.from(getContext()).inflate(201917456, (ViewGroup) this, false);
        View dotView = dot.findViewById(201457795);
        dotView.setBackground(getContext().getResources().getDrawable(stroke ? 201850954 : 201850953));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) dotView.getLayoutParams();
        int i = this.mDotSize;
        params.height = i;
        params.width = i;
        dotView.setLayoutParams(params);
        int i2 = this.mDotSpacing;
        params.setMargins(i2, 0, i2, 0);
        setupDotView(stroke, dotView, color);
        return dot;
    }

    private void removeIndicatorDots(int count) {
        for (int i = 0; i < count; i++) {
            this.mIndicatorDotsParent.removeViewAt(r1.getChildCount() - 1);
            this.mIndicatorDots.remove(r1.size() - 1);
        }
    }

    private void addIndicatorDots(int count) {
        for (int i = 0; i < count; i++) {
            View dot = buildDot(this.mIsStrokeStyle, this.mDotColor);
            final int index = i;
            if (this.mIsClickable) {
                dot.setOnClickListener(new View.OnClickListener() { // from class: com.oplus.widget.indicator.OplusPageIndicator.5
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        if (OplusPageIndicator.this.mOnDotClickListener != null) {
                            OplusPageIndicator.this.mNeedSettlePositionTemp = true;
                            OplusPageIndicator.this.mIsAnimated = false;
                            OplusPageIndicator.this.stopTraceAnimator();
                            OplusPageIndicator.this.mOnDotClickListener.onClick(index);
                        }
                    }
                });
            }
            this.mIndicatorDots.add(dot.findViewById(201457795));
            this.mIndicatorDotsParent.addView(dot);
        }
    }

    private void verifyLayoutWidth() {
        int i = this.mDotsCount;
        if (i < 1) {
            return;
        }
        this.mWidth = this.mDotStepDistance * i;
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startTraceAnimator() {
        if (this.mTraceAnimator == null) {
            return;
        }
        stopTraceAnimator();
        this.mTraceAnimator.start();
    }

    public void stopTraceAnimator() {
        if (!this.mIsAnimatorCanceled) {
            this.mIsAnimatorCanceled = true;
        }
        ValueAnimator valueAnimator = this.mTraceAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mTraceAnimator.cancel();
        }
    }

    private void pauseTrace() {
        this.mIsPaused = true;
    }

    private void resumeTrace() {
        this.mIsPaused = false;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(this.mWidth, this.mDotSize);
    }

    public void setDotsCount(int count) {
        setVisibility(count <= 1 ? 8 : 0);
        this.mDotsCount = count;
        verifyLayoutWidth();
        this.mIndicatorDots.clear();
        this.mIndicatorDotsParent.removeAllViews();
        addIndicatorDots(count);
    }

    public void setCurrentPosition(int position) {
        this.mCurrentPosition = position;
        this.mLastPosition = position;
        snapToPoition(position);
    }

    public void setTraceDotColor(int color) {
        this.mTraceDotColor = color;
        this.mTracePaint.setColor(color);
    }

    public void setPageIndicatorDotsColor(int color) {
        this.mDotColor = color;
        List<View> list = this.mIndicatorDots;
        if (list != null && !list.isEmpty()) {
            for (View dot : this.mIndicatorDots) {
                setupDotView(this.mIsStrokeStyle, dot, color);
            }
        }
    }

    public void setOnDotClickListener(OnIndicatorDotClickListener onDotClickListener) {
        this.mOnDotClickListener = onDotClickListener;
    }

    @Override // com.oplus.widget.indicator.IPagerIndicator
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        boolean rtl = isLayoutRtl();
        boolean scrollLeft = true;
        int i = this.mCurrentPosition;
        if (!rtl ? i > position : i <= position) {
            scrollLeft = false;
        }
        if (scrollLeft) {
            if (rtl) {
                float f = this.mWidth;
                int i2 = this.mDotSpacing;
                float rightX = f - ((i2 + (position * r4)) + (this.mDotStepDistance * positionOffset));
                this.mTraceRect.right = rightX;
            } else {
                int i3 = this.mDotSpacing + this.mDotSize;
                float rightX2 = i3 + (position * r3) + (this.mDotStepDistance * positionOffset);
                this.mTraceRect.right = rightX2;
            }
            if (this.mIsPaused) {
                if (!this.mIsAnimating && this.mIsAnimated) {
                    RectF rectF = this.mTraceRect;
                    rectF.left = rectF.right - this.mDotSize;
                } else if (this.mTraceRect.right - this.mTraceRect.left < this.mDotSize) {
                    RectF rectF2 = this.mTraceRect;
                    rectF2.left = rectF2.right - this.mDotSize;
                }
            } else if (this.mIsAnimated) {
                RectF rectF3 = this.mTraceRect;
                rectF3.left = rectF3.right - this.mDotSize;
            } else if (this.mTraceRect.right - this.mTraceRect.left < this.mDotSize) {
                RectF rectF4 = this.mTraceRect;
                rectF4.left = rectF4.right - this.mDotSize;
            }
        } else {
            if (rtl) {
                float leftX = ((this.mWidth - (this.mDotStepDistance * (position + positionOffset))) - this.mDotSpacing) - this.mDotSize;
                this.mTraceRect.left = leftX;
            } else {
                float leftX2 = this.mDotSpacing + (this.mDotStepDistance * (position + positionOffset));
                this.mTraceRect.left = leftX2;
            }
            if (this.mIsPaused) {
                if (!this.mIsAnimating && this.mIsAnimated) {
                    RectF rectF5 = this.mTraceRect;
                    rectF5.right = rectF5.left + this.mDotSize;
                } else if (this.mTraceRect.right - this.mTraceRect.left < this.mDotSize) {
                    RectF rectF6 = this.mTraceRect;
                    rectF6.right = rectF6.left + this.mDotSize;
                }
            } else if (this.mIsAnimated) {
                RectF rectF7 = this.mTraceRect;
                rectF7.right = rectF7.left + this.mDotSize;
            } else if (this.mTraceRect.right - this.mTraceRect.left < this.mDotSize) {
                RectF rectF8 = this.mTraceRect;
                rectF8.right = rectF8.left + this.mDotSize;
            }
        }
        this.mTraceLeft = this.mTraceRect.left;
        this.mTraceRight = this.mTraceRect.right;
        if (positionOffset == 0.0f) {
            this.mCurrentPosition = position;
        }
        invalidate();
    }

    private void verifyFinalPosition(int position) {
        if (isLayoutRtl()) {
            float f = this.mWidth - (this.mDotSpacing + (this.mDotStepDistance * position));
            this.mFinalRight = f;
            this.mFinalLeft = f - this.mDotSize;
        } else {
            int i = this.mDotSpacing;
            int i2 = this.mDotSize;
            float f2 = i + i2 + (this.mDotStepDistance * position);
            this.mFinalRight = f2;
            this.mFinalLeft = f2 - i2;
        }
    }

    @Override // com.oplus.widget.indicator.IPagerIndicator
    public void onPageSelected(int position) {
        boolean z = false;
        if (this.mLastPosition != position && this.mIsAnimated) {
            this.mIsAnimated = false;
        }
        if (!isLayoutRtl() ? this.mLastPosition > position : this.mLastPosition <= position) {
            z = true;
        }
        this.mTranceCutTailRight = z;
        verifyFinalPosition(position);
        if (this.mLastPosition != position) {
            if (this.mHandler.hasMessages(17)) {
                this.mHandler.removeMessages(17);
            }
            stopTraceAnimator();
            this.mHandler.sendEmptyMessageDelayed(17, 100L);
        } else if (this.mHandler.hasMessages(17)) {
            this.mHandler.removeMessages(17);
        }
        this.mLastPosition = position;
    }

    @Override // com.oplus.widget.indicator.IPagerIndicator
    public void onPageScrollStateChanged(int state) {
        if (state == 1) {
            pauseTrace();
            if (this.mIsAnimated) {
                this.mIsAnimated = false;
                return;
            }
            return;
        }
        if (state == 2) {
            resumeTrace();
        }
    }

    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    private int dpToPx(int dp) {
        return (int) (getContext().getResources().getDisplayMetrics().density * dp);
    }
}
