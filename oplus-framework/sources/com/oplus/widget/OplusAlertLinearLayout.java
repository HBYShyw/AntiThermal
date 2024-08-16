package com.oplus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import com.oplus.internal.R;

/* loaded from: classes.dex */
public final class OplusAlertLinearLayout extends LinearLayout {
    public static final int TYPE_SHADOW_WITH_CORNER = 0;
    private Drawable mBackgroundDrawable;
    private int mBackgroundRadius;
    private int mFixedBottom;
    private int mFixedLeft;
    private int mFixedRight;
    private int mFixedTop;
    private boolean mHasShadow;
    private boolean mNeedClip;
    private int mShadowBottom;
    private Drawable mShadowDrawable;
    private int mShadowLeft;
    private int mShadowRight;
    private int mShadowTop;

    public OplusAlertLinearLayout(Context context) {
        this(context, null);
    }

    public OplusAlertLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusAlertLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mShadowLeft = 0;
        this.mShadowTop = 0;
        this.mShadowRight = 0;
        this.mShadowBottom = 0;
        int defaultRadius = context.getResources().getDimensionPixelSize(201654299);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.OplusAlertLinearLayout, defStyleAttr, 0);
        this.mBackgroundRadius = array.getDimensionPixelSize(0, defaultRadius);
        this.mShadowDrawable = context.getResources().getDrawable(201850941);
        if (array.hasValue(1)) {
            this.mShadowDrawable = array.getDrawable(1);
        }
        this.mBackgroundDrawable = context.getDrawable(201850942);
        if (array.hasValue(2)) {
            this.mBackgroundDrawable = array.getDrawable(2);
        }
        array.recycle();
    }

    public void setHasShadow(boolean hasShadow) {
        this.mHasShadow = hasShadow;
        if (hasShadow) {
            setBackground(this.mShadowDrawable);
            this.mShadowLeft = getPaddingLeft();
            this.mShadowRight = getPaddingRight();
            this.mShadowTop = getPaddingTop();
            this.mShadowBottom = getPaddingBottom();
        } else {
            setBackground(null);
            setPadding(0, 0, 0, 0);
            this.mShadowLeft = 0;
            this.mShadowTop = 0;
            this.mShadowRight = 0;
            this.mShadowBottom = 0;
        }
        requestLayout();
    }

    public void setType(int type) {
        boolean hasShadow = type == 0;
        setHasShadow(hasShadow);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mFixedLeft = this.mShadowLeft;
        this.mFixedTop = this.mShadowTop;
        this.mFixedRight = w - this.mShadowRight;
        this.mFixedBottom = h - this.mShadowBottom;
        if (this.mNeedClip) {
            clipBackground();
        } else {
            setClipToOutline(false);
        }
    }

    public void setNeedClip(boolean needClip) {
        this.mNeedClip = needClip;
    }

    private void clipBackground() {
        ViewOutlineProvider provider = new ViewOutlineProvider() { // from class: com.oplus.widget.OplusAlertLinearLayout.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(OplusAlertLinearLayout.this.mFixedLeft, OplusAlertLinearLayout.this.mFixedTop, OplusAlertLinearLayout.this.mFixedRight, OplusAlertLinearLayout.this.mFixedBottom, OplusAlertLinearLayout.this.mBackgroundRadius);
            }
        };
        setClipToOutline(true);
        setOutlineProvider(provider);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        Drawable noShadowDrawable = this.mHasShadow ? this.mBackgroundDrawable : getNoShadowDrawable();
        this.mBackgroundDrawable = noShadowDrawable;
        if (noShadowDrawable != null) {
            noShadowDrawable.setBounds(this.mFixedLeft, this.mFixedTop, this.mFixedRight, this.mFixedBottom);
            this.mBackgroundDrawable.draw(canvas);
        }
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    public Drawable getNoShadowDrawable() {
        return getContext().getDrawable(201850943);
    }
}
