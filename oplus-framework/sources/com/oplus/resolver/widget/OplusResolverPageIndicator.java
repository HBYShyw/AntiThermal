package com.oplus.resolver.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.oplus.internal.R;
import com.oplus.widget.indicator.IPagerIndicator;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusResolverPageIndicator extends FrameLayout implements IPagerIndicator {
    private static final float BEZIER_OFFSET_INTERCEPT = 3.0f;
    private static final float BEZIER_OFFSET_MAX_FACTOR = 1.0f;
    private static final float BEZIER_OFFSET_MIN_FACTOR = 0.0f;
    private static final float BEZIER_OFFSET_SLOPE = -1.0f;
    private static final float BEZIER_OFFSET_X_INTERCEPT;
    private static final float BEZIER_OFFSET_X_INTERCEPT_2;
    private static final float BEZIER_OFFSET_X_MAX_FACTOR = 1.5f;
    private static final float BEZIER_OFFSET_X_MAX_FACTOR_2;
    private static final float BEZIER_OFFSET_X_MIN_FACTOR;
    private static final float BEZIER_OFFSET_X_MIN_FACTOR_2 = 0.0f;
    private static final float BEZIER_OFFSET_X_SLOPE;
    private static final float BEZIER_OFFSET_X_SLOPE_2;
    private static final boolean DEBUG = false;
    private static final int DELAY_TRACE_ANIMATION = 0;
    private static final float DISTANCE_TURN_POINT = 2.8f;
    private static final int DURATION_TRACE_ANIMATION = 300;
    private static final float FLOAT_HALF = 0.5f;
    private static final float FLOAT_ONE = 1.0f;
    private static final float FLOAT_SQRT_2;
    private static final float FLOAT_ZERO = 0.0f;
    private static final int MAX_ALPHA = 255;
    private static final int MIN_ALPHA = 0;
    private static final int MIS_POSITION = -1;
    private static final int MSG_START_TRACE_ANIMATION = 17;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final float STICKY_DISTANCE_FACTOR = 2.95f;
    private static final String TAG = "OplusPageIndicator";
    private static final float TRACE_ANIMATION_CONTROL_X1 = 0.33f;
    private static final float TRACE_ANIMATION_CONTROL_X2 = 0.66f;
    private Context mContext;
    private int mCurrentPosition;
    private float mDepartControlX;
    private float mDepartEndX;
    private int mDepartPosition;
    private RectF mDepartRect;
    private int mDepartStickyAlpha;
    private Path mDepartStickyPath;
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
    private boolean mIsAnimatorCanceled;
    private boolean mIsClickable;
    private boolean mIsPageSelected;
    private boolean mIsPaused;
    private boolean mIsStrokeStyle;
    private int mLastPosition;
    private boolean mNeedSettlePositionTemp;
    private float mOffset;
    private float mOffsetX;
    private float mOffsetY;
    private OnIndicatorDotClickListener mOnDotClickListener;
    private float mPortControlX;
    private float mPortEndX;
    private int mPortPosition;
    private RectF mPortRect;
    private int mPortStickyAlpha;
    private Path mPortStickyPath;
    private int mStyle;
    private ValueAnimator mTraceAnimator;
    private boolean mTraceCutTailRight;
    private int mTraceDotColor;
    private float mTraceLeft;
    private Paint mTracePaint;
    private RectF mTraceRect;
    private float mTraceRight;
    private int mWidth;

    /* loaded from: classes.dex */
    public interface OnIndicatorDotClickListener {
        void onClick(int i);
    }

    static {
        float sqrt = (float) Math.sqrt(2.0d);
        FLOAT_SQRT_2 = sqrt;
        BEZIER_OFFSET_X_SLOPE = 7.5f - (2.5f * sqrt);
        BEZIER_OFFSET_X_INTERCEPT = (7.5f * sqrt) - 21.0f;
        BEZIER_OFFSET_X_MIN_FACTOR = sqrt * 0.5f;
        BEZIER_OFFSET_X_SLOPE_2 = 0.625f * sqrt;
        BEZIER_OFFSET_X_INTERCEPT_2 = (-1.25f) * sqrt;
        BEZIER_OFFSET_X_MAX_FACTOR_2 = sqrt * 0.5f;
    }

    public OplusResolverPageIndicator(Context context) {
        this(context, null);
    }

    public OplusResolverPageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusResolverPageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDotStepDistance = 0;
        this.mPortStickyAlpha = 0;
        this.mDepartStickyAlpha = 0;
        this.mTraceLeft = 0.0f;
        this.mTraceRight = 0.0f;
        this.mFinalLeft = 0.0f;
        this.mFinalRight = 0.0f;
        this.mPortControlX = 0.0f;
        this.mPortEndX = 0.0f;
        this.mDepartControlX = 0.0f;
        this.mDepartEndX = 0.0f;
        this.mOffset = 0.0f;
        this.mOffsetX = 0.0f;
        this.mOffsetY = 0.0f;
        this.mTraceCutTailRight = false;
        this.mIsAnimated = false;
        this.mIsAnimatorCanceled = false;
        this.mIsPaused = false;
        this.mNeedSettlePositionTemp = false;
        this.mIsPageSelected = false;
        this.mPortStickyPath = new Path();
        this.mDepartStickyPath = new Path();
        this.mTraceRect = new RectF();
        this.mPortRect = new RectF();
        this.mDepartRect = new RectF();
        if (attrs != null && attrs.getStyleAttribute() != 0) {
            this.mStyle = attrs.getStyleAttribute();
        } else {
            this.mStyle = defStyleAttr;
        }
        this.mContext = context;
        this.mIndicatorDots = new ArrayList();
        this.mIsStrokeStyle = false;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OplusPageIndicator, defStyleAttr, 0);
            this.mTraceDotColor = typedArray.getColor(1, 0);
            this.mDotColor = typedArray.getColor(0, 0);
            this.mDotSize = (int) typedArray.getDimension(2, 0.0f);
            this.mDotSpacing = (int) typedArray.getDimension(3, 0.0f);
            this.mDotCornerRadius = (int) typedArray.getDimension(5, this.mDotSize * 0.5f);
            this.mIsClickable = typedArray.getBoolean(6, true);
            this.mDotStrokeWidth = (int) typedArray.getDimension(4, 0.0f);
            typedArray.recycle();
        }
        this.mTraceRect.top = 0.0f;
        this.mTraceRect.bottom = this.mDotSize;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mTraceAnimator = ofFloat;
        ofFloat.setDuration(300L);
        this.mTraceAnimator.setInterpolator(new PathInterpolator(TRACE_ANIMATION_CONTROL_X1, 0.0f, TRACE_ANIMATION_CONTROL_X2, 1.0f));
        this.mTraceAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.resolver.widget.OplusResolverPageIndicator.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                if (!OplusResolverPageIndicator.this.mIsAnimatorCanceled) {
                    float diffLeft = OplusResolverPageIndicator.this.mTraceLeft - OplusResolverPageIndicator.this.mFinalLeft;
                    float diffRight = OplusResolverPageIndicator.this.mTraceRight - OplusResolverPageIndicator.this.mFinalRight;
                    float left = OplusResolverPageIndicator.this.mTraceLeft - (diffLeft * value);
                    if (left > OplusResolverPageIndicator.this.mTraceRect.right - OplusResolverPageIndicator.this.mDotSize) {
                        left = OplusResolverPageIndicator.this.mTraceRect.right - OplusResolverPageIndicator.this.mDotSize;
                    }
                    float right = OplusResolverPageIndicator.this.mTraceRight - (diffRight * value);
                    if (right < OplusResolverPageIndicator.this.mTraceRect.left + OplusResolverPageIndicator.this.mDotSize) {
                        right = OplusResolverPageIndicator.this.mTraceRect.left + OplusResolverPageIndicator.this.mDotSize;
                    }
                    if (!OplusResolverPageIndicator.this.mNeedSettlePositionTemp) {
                        if (OplusResolverPageIndicator.this.mTraceCutTailRight) {
                            OplusResolverPageIndicator.this.mTraceRect.right = right;
                        } else {
                            OplusResolverPageIndicator.this.mTraceRect.left = left;
                        }
                    } else {
                        OplusResolverPageIndicator.this.mTraceRect.left = left;
                        OplusResolverPageIndicator.this.mTraceRect.right = right;
                    }
                    if (OplusResolverPageIndicator.this.mTraceCutTailRight) {
                        OplusResolverPageIndicator oplusResolverPageIndicator = OplusResolverPageIndicator.this;
                        oplusResolverPageIndicator.mDepartControlX = oplusResolverPageIndicator.mTraceRect.right - (OplusResolverPageIndicator.this.mDotSize * 0.5f);
                    } else {
                        OplusResolverPageIndicator oplusResolverPageIndicator2 = OplusResolverPageIndicator.this;
                        oplusResolverPageIndicator2.mDepartControlX = oplusResolverPageIndicator2.mTraceRect.left + (OplusResolverPageIndicator.this.mDotSize * 0.5f);
                    }
                    OplusResolverPageIndicator oplusResolverPageIndicator3 = OplusResolverPageIndicator.this;
                    oplusResolverPageIndicator3.mDepartEndX = oplusResolverPageIndicator3.mDepartRect.left + (OplusResolverPageIndicator.this.mDotSize * 0.5f);
                    OplusResolverPageIndicator oplusResolverPageIndicator4 = OplusResolverPageIndicator.this;
                    oplusResolverPageIndicator4.mDepartStickyPath = oplusResolverPageIndicator4.calculateTangentBezierPath(oplusResolverPageIndicator4.mDepartPosition, OplusResolverPageIndicator.this.mDepartControlX, OplusResolverPageIndicator.this.mDepartEndX, OplusResolverPageIndicator.this.mDotSize * 0.5f, false);
                    OplusResolverPageIndicator.this.invalidate();
                }
            }
        });
        this.mTraceAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.oplus.resolver.widget.OplusResolverPageIndicator.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                OplusResolverPageIndicator.this.clearStickyPath(false);
                if (!OplusResolverPageIndicator.this.mIsAnimatorCanceled) {
                    OplusResolverPageIndicator.this.mTraceRect.right = OplusResolverPageIndicator.this.mTraceRect.left + OplusResolverPageIndicator.this.mDotSize;
                    OplusResolverPageIndicator.this.mNeedSettlePositionTemp = false;
                    OplusResolverPageIndicator.this.mIsAnimated = true;
                    OplusResolverPageIndicator.this.invalidate();
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                OplusResolverPageIndicator.this.mIsAnimatorCanceled = false;
                OplusResolverPageIndicator oplusResolverPageIndicator = OplusResolverPageIndicator.this;
                oplusResolverPageIndicator.mTraceLeft = oplusResolverPageIndicator.mTraceRect.left;
                OplusResolverPageIndicator oplusResolverPageIndicator2 = OplusResolverPageIndicator.this;
                oplusResolverPageIndicator2.mTraceRight = oplusResolverPageIndicator2.mTraceRect.right;
            }
        });
        Paint paint = new Paint(1);
        this.mTracePaint = paint;
        paint.setStyle(Paint.Style.FILL);
        this.mTracePaint.setColor(this.mTraceDotColor);
        this.mDotStepDistance = this.mDotSize + (this.mDotSpacing * 2);
        this.mHandler = new Handler() { // from class: com.oplus.resolver.widget.OplusResolverPageIndicator.3
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 17) {
                    OplusResolverPageIndicator.this.startTraceAnimator();
                }
                super.handleMessage(msg);
            }
        };
        this.mIndicatorDotsParent = new LinearLayout(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        this.mIndicatorDotsParent.setLayoutParams(params);
        this.mIndicatorDotsParent.setOrientation(0);
        addView(this.mIndicatorDotsParent);
        snapToPosition(this.mCurrentPosition);
    }

    private void snapToPosition(int position) {
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

    public void removeDot() throws IndexOutOfBoundsException {
        int i = this.mDotsCount;
        if (i < 1) {
            throw new IndexOutOfBoundsException("Can't remove dot because the count of dots is 0.");
        }
        this.mDotsCount = i - 1;
        verifyLayoutWidth();
        removeIndicatorDots(1);
        clearStickyPath();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.mTracePaint.setAlpha(255);
        RectF rectF = this.mTraceRect;
        int i = this.mDotCornerRadius;
        canvas.drawRoundRect(rectF, i, i, this.mTracePaint);
        this.mTracePaint.setAlpha(this.mPortStickyAlpha);
        RectF rectF2 = this.mPortRect;
        int i2 = this.mDotCornerRadius;
        canvas.drawRoundRect(rectF2, i2, i2, this.mTracePaint);
        canvas.drawPath(this.mPortStickyPath, this.mTracePaint);
        this.mTracePaint.setAlpha(this.mDepartStickyAlpha);
        RectF rectF3 = this.mDepartRect;
        int i3 = this.mDotCornerRadius;
        canvas.drawRoundRect(rectF3, i3, i3, this.mTracePaint);
        canvas.drawPath(this.mDepartStickyPath, this.mTracePaint);
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
        params.width = this.mDotSize;
        params.height = this.mDotSize;
        dotView.setLayoutParams(params);
        int i = this.mDotSpacing;
        params.setMargins(i, 0, i, 0);
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
                dot.setOnClickListener(new View.OnClickListener() { // from class: com.oplus.resolver.widget.OplusResolverPageIndicator.4
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        if (OplusResolverPageIndicator.this.mOnDotClickListener != null && OplusResolverPageIndicator.this.mLastPosition != index) {
                            OplusResolverPageIndicator.this.mNeedSettlePositionTemp = true;
                            OplusResolverPageIndicator.this.mIsAnimated = false;
                            OplusResolverPageIndicator oplusResolverPageIndicator = OplusResolverPageIndicator.this;
                            oplusResolverPageIndicator.mCurrentPosition = oplusResolverPageIndicator.mLastPosition;
                            OplusResolverPageIndicator.this.stopTraceAnimator();
                            OplusResolverPageIndicator.this.mOnDotClickListener.onClick(index);
                        }
                    }
                });
            }
            this.mIndicatorDots.add(dot.findViewById(201457795));
            this.mIndicatorDotsParent.addView(dot);
        }
        if (count <= 1) {
            this.mIndicatorDotsParent.setVisibility(8);
        } else {
            this.mIndicatorDotsParent.setVisibility(0);
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
        removeIndicatorDots(this.mDotsCount);
        this.mDotsCount = count;
        verifyLayoutWidth();
        addIndicatorDots(count);
    }

    public void setCurrentPosition(int position) {
        this.mLastPosition = position;
        this.mCurrentPosition = position;
        snapToPosition(position);
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
        float leftX = 0.0f;
        float rightX = 0.0f;
        boolean rtl = isLayoutRtl();
        boolean z = false;
        int i = this.mCurrentPosition;
        if (!rtl ? i <= position : i > position) {
            z = true;
        }
        boolean scrollLeft = z;
        if (scrollLeft) {
            if (!rtl) {
                this.mPortPosition = position + 1;
                int i2 = this.mDotSpacing + this.mDotSize;
                rightX = i2 + (position * r3) + (this.mDotStepDistance * positionOffset);
            } else {
                this.mPortPosition = position;
                float f = this.mWidth;
                int i3 = this.mDotSpacing;
                rightX = f - ((i3 + (position * r4)) + (this.mDotStepDistance * positionOffset));
            }
            this.mTraceRect.right = rightX;
            if (this.mIsPaused) {
                if (!this.mTraceAnimator.isRunning() && this.mIsAnimated) {
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
                this.mPortPosition = position + 1;
                leftX = ((this.mWidth - (this.mDotStepDistance * (position + positionOffset))) - this.mDotSpacing) - this.mDotSize;
            } else {
                this.mPortPosition = position;
                leftX = this.mDotSpacing + (this.mDotStepDistance * (position + positionOffset));
            }
            this.mTraceRect.left = leftX;
            if (this.mIsPaused) {
                if (!this.mTraceAnimator.isRunning() && this.mIsAnimated) {
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
        if (scrollLeft) {
            this.mPortControlX = this.mTraceRect.right - (this.mDotSize * 0.5f);
        } else {
            this.mPortControlX = this.mTraceRect.left + (this.mDotSize * 0.5f);
        }
        verifyStickyPosition(this.mPortPosition, true);
        float f2 = this.mPortRect.left;
        int i4 = this.mDotSize;
        float f3 = (i4 * 0.5f) + f2;
        this.mPortEndX = f3;
        this.mPortStickyPath = calculateTangentBezierPath(this.mPortPosition, this.mPortControlX, f3, i4 * 0.5f, true);
        if (positionOffset == 0.0f) {
            this.mCurrentPosition = position;
            clearStickyPath(true);
        }
        invalidate();
    }

    private void verifyFinalPosition(int position) {
        if (isLayoutRtl()) {
            this.mFinalRight = this.mWidth - (this.mDotSpacing + (this.mDotStepDistance * position));
        } else {
            this.mFinalRight = this.mDotSpacing + this.mDotSize + (this.mDotStepDistance * position);
        }
        this.mFinalLeft = this.mFinalRight - this.mDotSize;
    }

    private void verifyStickyPosition(int position, boolean isPortStickyPath) {
        if (isPortStickyPath) {
            this.mPortRect.top = 0.0f;
            this.mPortRect.bottom = this.mDotSize;
            if (isLayoutRtl()) {
                this.mPortRect.right = this.mWidth - (this.mDotSpacing + (this.mDotStepDistance * position));
            } else {
                this.mPortRect.right = this.mDotSpacing + this.mDotSize + (this.mDotStepDistance * position);
            }
            RectF rectF = this.mPortRect;
            rectF.left = rectF.right - this.mDotSize;
            return;
        }
        this.mDepartRect.top = 0.0f;
        this.mDepartRect.bottom = this.mDotSize;
        if (isLayoutRtl()) {
            this.mDepartRect.right = this.mWidth - (this.mDotSpacing + (this.mDotStepDistance * position));
        } else {
            this.mDepartRect.right = this.mDotSpacing + this.mDotSize + (this.mDotStepDistance * position);
        }
        RectF rectF2 = this.mDepartRect;
        rectF2.left = rectF2.right - this.mDotSize;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Path calculateTangentBezierPath(int position, float controlX, float endX, float radius, boolean isPortStickyPath) {
        Path bezierPath;
        if (isPortStickyPath) {
            this.mPortStickyAlpha = 255;
            bezierPath = this.mPortStickyPath;
        } else {
            this.mDepartStickyAlpha = 255;
            bezierPath = this.mDepartStickyPath;
        }
        bezierPath.reset();
        float distance = Math.abs(controlX - endX);
        if (distance >= STICKY_DISTANCE_FACTOR * radius || position == -1) {
            clearStickyPath(isPortStickyPath);
            return bezierPath;
        }
        calculateControlPointOffset(distance, radius);
        float f = FLOAT_SQRT_2;
        float offsetXTurnPoint = f * 0.5f * radius;
        float offsetYTurnPoint = f * 0.5f * radius;
        if (controlX > endX) {
            this.mOffsetX = -this.mOffsetX;
            offsetXTurnPoint = -offsetXTurnPoint;
        }
        if (distance >= DISTANCE_TURN_POINT * radius) {
            bezierPath.moveTo(controlX + offsetXTurnPoint, radius + offsetYTurnPoint);
            bezierPath.lineTo(this.mOffsetX + controlX, this.mOffsetY + radius);
            bezierPath.quadTo((controlX + endX) * 0.5f, this.mOffset + radius, endX - this.mOffsetX, this.mOffsetY + radius);
            bezierPath.lineTo(endX - offsetXTurnPoint, radius + offsetYTurnPoint);
            bezierPath.lineTo(endX - offsetXTurnPoint, radius - offsetYTurnPoint);
            bezierPath.lineTo(endX - this.mOffsetX, radius - this.mOffsetY);
            bezierPath.quadTo((controlX + endX) * 0.5f, radius - this.mOffset, this.mOffsetX + controlX, radius - this.mOffsetY);
            bezierPath.lineTo(controlX + offsetXTurnPoint, radius - offsetYTurnPoint);
            bezierPath.lineTo(controlX + offsetXTurnPoint, radius + offsetYTurnPoint);
        } else {
            bezierPath.moveTo(this.mOffsetX + controlX, this.mOffsetY + radius);
            bezierPath.quadTo((controlX + endX) * 0.5f, this.mOffset + radius, endX - this.mOffsetX, this.mOffsetY + radius);
            bezierPath.lineTo(endX - this.mOffsetX, radius - this.mOffsetY);
            bezierPath.quadTo((controlX + endX) * 0.5f, radius - this.mOffset, this.mOffsetX + controlX, radius - this.mOffsetY);
            bezierPath.lineTo(this.mOffsetX + controlX, this.mOffsetY + radius);
        }
        return bezierPath;
    }

    private void calculateControlPointOffset(float distance, float radius) {
        this.mOffset = Math.max(Math.min((BEZIER_OFFSET_SLOPE * distance) + (BEZIER_OFFSET_INTERCEPT * radius), 1.0f * radius), radius * 0.0f);
        this.mOffsetX = radius * BEZIER_OFFSET_X_MAX_FACTOR;
        this.mOffsetY = 0.0f;
        if (distance >= DISTANCE_TURN_POINT * radius) {
            float max = Math.max(Math.min((BEZIER_OFFSET_X_SLOPE * distance) + (BEZIER_OFFSET_X_INTERCEPT * radius), BEZIER_OFFSET_X_MAX_FACTOR * radius), BEZIER_OFFSET_X_MIN_FACTOR * radius);
            this.mOffsetX = max;
            this.mOffsetY = ((distance - (max * 2.0f)) * radius) / ((FLOAT_SQRT_2 * distance) - (2.0f * radius));
        } else {
            this.mOffsetX = Math.max(Math.min((BEZIER_OFFSET_X_SLOPE_2 * distance) + (BEZIER_OFFSET_X_INTERCEPT_2 * radius), BEZIER_OFFSET_X_MAX_FACTOR_2 * radius), 0.0f);
            this.mOffsetY = (float) Math.sqrt(Math.pow(radius, 2.0d) - Math.pow(this.mOffsetX, 2.0d));
        }
    }

    private void clearStickyPath() {
        clearStickyPath(true);
        clearStickyPath(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearStickyPath(boolean isPortStickyPath) {
        if (isPortStickyPath) {
            this.mPortPosition = -1;
            this.mPortRect.setEmpty();
            this.mPortStickyPath.reset();
            this.mPortStickyAlpha = 0;
            return;
        }
        this.mDepartPosition = -1;
        this.mDepartRect.setEmpty();
        this.mDepartStickyPath.reset();
        this.mDepartStickyAlpha = 0;
    }

    @Override // com.oplus.widget.indicator.IPagerIndicator
    public void onPageSelected(int position) {
        this.mIsPageSelected = true;
        if (this.mLastPosition != position && this.mIsAnimated) {
            this.mIsAnimated = false;
        }
        this.mTraceCutTailRight = !isLayoutRtl() ? this.mLastPosition <= position : this.mLastPosition > position;
        int pageDiff = Math.abs(this.mLastPosition - position);
        this.mTraceAnimator.setDuration((pageDiff >= 1 ? pageDiff : 1) * 300);
        verifyFinalPosition(position);
        int i = this.mLastPosition;
        this.mDepartPosition = i;
        verifyStickyPosition(i, false);
        if (this.mLastPosition != position) {
            if (this.mHandler.hasMessages(17)) {
                this.mHandler.removeMessages(17);
            }
            stopTraceAnimator();
            this.mHandler.sendEmptyMessageDelayed(17, 0L);
        } else if (this.mHandler.hasMessages(17)) {
            this.mHandler.removeMessages(17);
        }
        this.mLastPosition = position;
    }

    @Override // com.oplus.widget.indicator.IPagerIndicator
    public void onPageScrollStateChanged(int state) {
        if (state == 1) {
            pauseTrace();
            clearStickyPath(false);
            this.mTraceAnimator.pause();
            if (this.mIsAnimated) {
                this.mIsAnimated = false;
            }
        } else if (state == 2) {
            resumeTrace();
            this.mTraceAnimator.resume();
        } else if (state == 0 && (this.mIsPaused || !this.mIsPageSelected)) {
            if (this.mHandler.hasMessages(17)) {
                this.mHandler.removeMessages(17);
            }
            stopTraceAnimator();
            this.mHandler.sendEmptyMessageDelayed(17, 0L);
        }
        this.mIsPageSelected = false;
    }

    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    public void setDotSize(int size) {
        this.mDotSize = size;
    }

    public void setDotSpacing(int space) {
        this.mDotSpacing = space;
    }

    public void setDotCornerRadius(int radius) {
        this.mDotCornerRadius = radius;
    }

    public void setIsClickable(boolean isClickable) {
        this.mIsClickable = isClickable;
    }

    public void setDotStrokeWidth(int width) {
        this.mDotStrokeWidth = width;
    }

    public int getDotsCount() {
        return this.mDotsCount;
    }
}
