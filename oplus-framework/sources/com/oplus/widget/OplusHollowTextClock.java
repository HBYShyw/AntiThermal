package com.oplus.widget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.RemoteViews;
import android.widget.TextClock;

@RemoteViews.RemoteView
/* loaded from: classes.dex */
public class OplusHollowTextClock extends TextClock {
    private static final String ARRT_IS_GLOW = "isGlow";
    private static final String ARRT_IS_HOLLOW = "isHollow";
    private TextPaint mGlowPaint;
    private boolean mIsGlow;
    private boolean mIsHollow;
    private TextPaint mStrokePaint;

    public OplusHollowTextClock(Context context) {
        this(context, null);
    }

    public OplusHollowTextClock(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public OplusHollowTextClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mIsHollow = false;
        this.mIsGlow = false;
        initAttrs(attrs, defStyleAttr);
        initStroke();
        initGlow();
    }

    private void initStroke() {
        if (this.mStrokePaint == null) {
            this.mStrokePaint = new TextPaint();
        }
        this.mStrokePaint.setTextSize(getTextSize());
        this.mStrokePaint.setTypeface(getTypeface());
        this.mStrokePaint.setFlags(getPaintFlags());
        this.mStrokePaint.setStyle(Paint.Style.STROKE);
        this.mStrokePaint.setColor(getTextColors().getDefaultColor());
        this.mStrokePaint.setStrokeWidth(2.0f);
    }

    private void initGlow() {
        if (this.mGlowPaint == null) {
            this.mGlowPaint = new TextPaint();
        }
        this.mGlowPaint.setTextSize(getTextSize());
        this.mGlowPaint.setTypeface(getTypeface());
        this.mGlowPaint.setFlags(getPaintFlags());
        this.mGlowPaint.setStyle(Paint.Style.FILL);
        this.mGlowPaint.setColor(getTextColors().getDefaultColor());
        this.mGlowPaint.setMaskFilter(new BlurMaskFilter(20.0f, this.mIsHollow ? BlurMaskFilter.Blur.OUTER : BlurMaskFilter.Blur.SOLID));
    }

    private void initAttrs(AttributeSet attrs, int defStyleAttr) {
        if (attrs == null) {
            return;
        }
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String name = attrs.getAttributeName(i);
            if (ARRT_IS_HOLLOW.equals(name)) {
                this.mIsHollow = attrs.getAttributeBooleanValue(i, false);
            }
            if (ARRT_IS_GLOW.equals(name)) {
                this.mIsGlow = attrs.getAttributeBooleanValue(i, false);
            }
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        if (!this.mIsHollow && !this.mIsGlow) {
            super.onDraw(canvas);
            return;
        }
        String text = getText().toString();
        if (this.mIsHollow) {
            float x = (getWidth() - this.mStrokePaint.measureText(text)) / 2.0f;
            float y = getBaseline();
            canvas.drawText(text, x, y, this.mStrokePaint);
        }
        if (this.mIsGlow) {
            float xGlow = (getWidth() - this.mGlowPaint.measureText(text)) / 2.0f;
            float yGlow = getBaseline();
            canvas.drawText(text, xGlow, yGlow, this.mGlowPaint);
        }
    }
}
