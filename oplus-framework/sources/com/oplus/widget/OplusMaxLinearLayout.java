package com.oplus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import com.oplus.internal.R;

/* loaded from: classes.dex */
public class OplusMaxLinearLayout extends LinearLayout {
    public static final int INVALID_MAX_VALUE = Integer.MAX_VALUE;
    private int mLandscapeMaxHeight;
    private int mLandscapeMaxWidth;
    private int mPortraitMaxHeight;
    private int mPortraitMaxWidth;

    public OplusMaxLinearLayout(Context context) {
        super(context);
    }

    public OplusMaxLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OplusMaxLinearLayout);
        this.mPortraitMaxHeight = typedArray.getDimensionPixelSize(0, INVALID_MAX_VALUE);
        this.mLandscapeMaxHeight = typedArray.getDimensionPixelSize(1, INVALID_MAX_VALUE);
        this.mPortraitMaxWidth = typedArray.getDimensionPixelSize(2, INVALID_MAX_VALUE);
        this.mLandscapeMaxWidth = typedArray.getDimensionPixelSize(3, INVALID_MAX_VALUE);
        typedArray.recycle();
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        boolean needRemeasure = false;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int maxWidth = getMaxWidth();
        int maxHeight = getMaxHeight();
        if (width > maxWidth) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(maxWidth, 1073741824);
            needRemeasure = true;
        }
        if (height > maxHeight) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(maxHeight, 1073741824);
            needRemeasure = true;
        }
        if (needRemeasure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setPortraitMaxWidth(int maxValue) {
        this.mPortraitMaxWidth = maxValue;
    }

    public void setPortraitMaxHeight(int maxValue) {
        this.mPortraitMaxHeight = maxValue;
    }

    public void setLandscapeMaxWidth(int maxValue) {
        this.mLandscapeMaxWidth = maxValue;
    }

    public void setLandscapeMaxHeight(int maxValue) {
        this.mLandscapeMaxHeight = maxValue;
    }

    public int getMaxWidth() {
        return isPortrait() ? this.mPortraitMaxWidth : this.mLandscapeMaxWidth;
    }

    public int getMaxHeight() {
        return isPortrait() ? this.mPortraitMaxHeight : this.mLandscapeMaxHeight;
    }

    private boolean isPortrait() {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return metrics.widthPixels < metrics.heightPixels;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getBackground() instanceof InsetDrawable) {
            final int radius = getContext().getResources().getDimensionPixelOffset(201654493);
            final int insetPadding = getContext().getResources().getDimensionPixelOffset(201654480);
            setClipToOutline(true);
            setOutlineProvider(new ViewOutlineProvider() { // from class: com.oplus.widget.OplusMaxLinearLayout.1
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    int i = insetPadding;
                    outline.setRoundRect(i, i, OplusMaxLinearLayout.this.getMeasuredWidth() - insetPadding, OplusMaxLinearLayout.this.getMeasuredHeight() - insetPadding, radius);
                }
            });
        }
    }
}
