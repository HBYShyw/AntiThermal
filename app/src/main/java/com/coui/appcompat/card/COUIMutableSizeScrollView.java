package com.coui.appcompat.card;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.coui.appcompat.scrollview.COUIScrollView;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIMutableSizeScrollView extends COUIScrollView {
    private int N;
    protected final int O;
    protected final PointF P;
    protected final PointF Q;

    public COUIMutableSizeScrollView(Context context) {
        this(context, null);
    }

    public int getMaxHeight() {
        return this.N;
    }

    @Override // com.coui.appcompat.scrollview.COUIScrollView, android.widget.ScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.P.x = motionEvent.getX();
            this.P.y = motionEvent.getY();
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.scrollview.COUIScrollView, android.widget.ScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        if (getChildCount() != 1) {
            return;
        }
        int measuredHeight = getChildAt(0).getMeasuredHeight();
        int i12 = this.N;
        if (i12 < 0 || measuredHeight <= i12) {
            return;
        }
        setMeasuredDimension(View.MeasureSpec.getSize(i10), this.N);
    }

    @Override // com.coui.appcompat.scrollview.COUIScrollView, android.widget.ScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 2) {
            this.Q.x = motionEvent.getX();
            this.Q.y = motionEvent.getY();
            PointF pointF = this.Q;
            float f10 = pointF.x;
            PointF pointF2 = this.P;
            float f11 = f10 - pointF2.x;
            float f12 = pointF.y - pointF2.y;
            float abs = Math.abs(f11) * 0.5f;
            float abs2 = Math.abs(f12) * 1.0f;
            int i10 = this.O;
            if (abs > i10 || abs2 > i10) {
                if (true == (abs > abs2)) {
                    if (y(0, (int) f11)) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setMaxHeight(int i10) {
        this.N = i10;
        requestLayout();
    }

    public boolean y(int i10, int i11) {
        if (i10 == 0) {
            return false;
        }
        return canScrollVertically((int) (-Math.signum(i11)));
    }

    public COUIMutableSizeScrollView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIMutableSizeScrollView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.O = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.P = new PointF();
        this.Q = new PointF();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIMaxHeightScrollView);
        this.N = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIMaxHeightScrollView_scrollViewMaxHeight, -1);
        obtainStyledAttributes.recycle();
    }
}
