package com.oplus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.oplus.internal.R;

/* loaded from: classes.dex */
public class OplusDragTextShadowContainer extends ViewGroup {
    private final float mCornerRadius;
    private final float mDeltaLength;
    private boolean mDrawShadow;
    private float mDx;
    private float mDy;
    private int mShadowColor;
    private float mShadowRadius;

    public OplusDragTextShadowContainer(Context context) {
        this(context, null);
    }

    public OplusDragTextShadowContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusDragTextShadowContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OplusGlobalDragTextShadowContainer);
        this.mShadowColor = a.getColor(0, -65536);
        this.mShadowRadius = a.getDimension(1, 0.0f);
        this.mDeltaLength = a.getDimension(2, 0.0f);
        this.mCornerRadius = a.getDimension(3, 0.0f);
        this.mDx = a.getDimension(4, 0.0f);
        this.mDy = a.getDimension(5, 0.0f);
        this.mDrawShadow = a.getBoolean(6, true);
        a.recycle();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.mDrawShadow) {
            if (getLayerType() != 1) {
                setLayerType(1, null);
            }
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setColor(this.mShadowColor);
            paint.setColor(0);
            paint.setShadowLayer(this.mShadowRadius, this.mDx, this.mDy, this.mShadowColor);
            View child = getChildAt(0);
            int left = child.getLeft();
            int top = child.getTop();
            int right = child.getRight();
            int bottom = child.getBottom();
            RectF rectF = new RectF(left, top, right, bottom);
            float f = this.mShadowRadius;
            canvas.drawRoundRect(rectF, f, f, paint);
        }
        super.dispatchDraw(canvas);
    }

    public void setmDrawShadow(boolean mDrawShadow) {
        if (this.mDrawShadow == mDrawShadow) {
            return;
        }
        this.mDrawShadow = mDrawShadow;
        postInvalidate();
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasureSpecMode;
        int widthMeasureSpecSize;
        int heightMeasureSpecMode;
        int heightMeasureSpecMode2;
        int height;
        int width;
        int width2;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() != 1) {
            throw new IllegalStateException("only one child!");
        }
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        View child = getChildAt(0);
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        int childBottomMargin = (int) (Math.max(this.mDeltaLength, layoutParams.bottomMargin) + 1.0f);
        int childLeftMargin = (int) (Math.max(this.mDeltaLength, layoutParams.leftMargin) + 1.0f);
        int childRightMargin = (int) (Math.max(this.mDeltaLength, layoutParams.rightMargin) + 1.0f);
        int childTopMargin = (int) (Math.max(this.mDeltaLength, layoutParams.topMargin) + 1.0f);
        if (widthMode != 0) {
            int widthMode2 = layoutParams.width;
            if (widthMode2 == -1) {
                widthMeasureSpecMode = 1073741824;
                widthMeasureSpecSize = (measuredWidth - childLeftMargin) - childRightMargin;
            } else if (-2 == layoutParams.width) {
                widthMeasureSpecMode = Integer.MIN_VALUE;
                widthMeasureSpecSize = (measuredWidth - childLeftMargin) - childRightMargin;
            } else {
                widthMeasureSpecMode = 1073741824;
                widthMeasureSpecSize = layoutParams.width;
            }
        } else {
            widthMeasureSpecMode = 0;
            widthMeasureSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
        }
        if (heightMode == 0) {
            heightMeasureSpecMode = 0;
            heightMeasureSpecMode2 = View.MeasureSpec.getSize(heightMeasureSpec);
        } else {
            int heightMeasureSpecMode3 = layoutParams.height;
            if (heightMeasureSpecMode3 == -1) {
                heightMeasureSpecMode = 1073741824;
                int heightMeasureSpecMode4 = measuredHeight - childBottomMargin;
                heightMeasureSpecMode2 = heightMeasureSpecMode4 - childTopMargin;
            } else {
                int heightMeasureSpecMode5 = layoutParams.height;
                if (-2 == heightMeasureSpecMode5) {
                    heightMeasureSpecMode = Integer.MIN_VALUE;
                    int heightMeasureSpecMode6 = measuredHeight - childBottomMargin;
                    heightMeasureSpecMode2 = heightMeasureSpecMode6 - childTopMargin;
                } else {
                    heightMeasureSpecMode = 1073741824;
                    heightMeasureSpecMode2 = layoutParams.height;
                }
            }
        }
        int heightMeasureSpecSize = View.MeasureSpec.makeMeasureSpec(widthMeasureSpecSize, widthMeasureSpecMode);
        measureChild(child, heightMeasureSpecSize, View.MeasureSpec.makeMeasureSpec(heightMeasureSpecMode2, heightMeasureSpecMode));
        int parentWidthMeasureSpec = View.MeasureSpec.getMode(widthMeasureSpec);
        int parentHeightMeasureSpec = View.MeasureSpec.getMode(heightMeasureSpec);
        int childHeight = child.getMeasuredHeight();
        int childWidth = child.getMeasuredWidth();
        if (parentHeightMeasureSpec != Integer.MIN_VALUE) {
            height = measuredHeight;
        } else {
            int height2 = childHeight + childTopMargin + childBottomMargin;
            height = height2;
        }
        if (parentWidthMeasureSpec != Integer.MIN_VALUE) {
            width = measuredWidth;
        } else {
            int width3 = childWidth + childRightMargin + childLeftMargin;
            width = width3;
        }
        float f = width;
        int width4 = width;
        float f2 = this.mDeltaLength;
        if (f >= childWidth + (f2 * 2.0f)) {
            width2 = width4;
        } else {
            width2 = (int) (childWidth + (f2 * 2.0f));
        }
        if (height < childHeight + (f2 * 2.0f)) {
            height = (int) (childHeight + (f2 * 2.0f));
        }
        if (height != measuredHeight || width2 != measuredWidth) {
            setMeasuredDimension(width2, height);
        }
    }

    /* loaded from: classes.dex */
    static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child = getChildAt(0);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int childMeasureWidth = child.getMeasuredWidth();
        int childMeasureHeight = child.getMeasuredHeight();
        child.layout((measuredWidth - childMeasureWidth) / 2, (measuredHeight - childMeasureHeight) / 2, (measuredWidth + childMeasureWidth) / 2, (measuredHeight + childMeasureHeight) / 2);
    }
}
