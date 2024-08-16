package com.coui.appcompat.statement;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import com.coui.appcompat.scrollview.COUIScrollView;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIMaxHeightScrollView extends COUIScrollView {
    private int N;
    private int O;

    public COUIMaxHeightScrollView(Context context) {
        this(context, null);
    }

    public int getMaxHeight() {
        return this.N;
    }

    public int getMinHeight() {
        return this.O;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.scrollview.COUIScrollView, android.widget.ScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        int size = View.MeasureSpec.getSize(i11);
        int i12 = this.N;
        if (i12 > 0) {
            i11 = View.MeasureSpec.makeMeasureSpec(Math.min(i12, size), Integer.MIN_VALUE);
        }
        super.onMeasure(i10, i11);
        int measuredHeight = getMeasuredHeight();
        int i13 = this.O;
        if (measuredHeight < i13) {
            super.onMeasure(i10, View.MeasureSpec.makeMeasureSpec(i13, 1073741824));
        }
    }

    public void setMaxHeight(int i10) {
        this.N = i10;
        requestLayout();
    }

    public void setMinHeight(int i10) {
        this.O = i10;
        requestLayout();
    }

    public COUIMaxHeightScrollView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIMaxHeightScrollView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIMaxHeightScrollView);
        this.N = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIMaxHeightScrollView_scrollViewMaxHeight, 0);
        this.O = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIMaxHeightScrollView_scrollViewMinHeight, 0);
        obtainStyledAttributes.recycle();
    }
}
