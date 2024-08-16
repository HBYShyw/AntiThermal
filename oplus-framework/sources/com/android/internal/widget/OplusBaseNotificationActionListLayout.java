package com.android.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/* loaded from: classes.dex */
public class OplusBaseNotificationActionListLayout extends LinearLayout {
    public int mMaxChildHeight;

    public OplusBaseNotificationActionListLayout(Context context) {
        this(context, null);
    }

    public OplusBaseNotificationActionListLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public OplusBaseNotificationActionListLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public OplusBaseNotificationActionListLayout(Context context, AttributeSet attributeSet, int i, int i1) {
        super(context, attributeSet, i, i1);
        this.mMaxChildHeight = 0;
    }

    public void resetCurrentMaxChildHeight(View child) {
        if (child.getMeasuredHeight() >= this.mMaxChildHeight) {
            this.mMaxChildHeight = child.getMeasuredHeight();
        }
    }

    public void setActionLayoutMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mMaxChildHeight != 0) {
            setMeasuredDimension(resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec), this.mMaxChildHeight);
        } else {
            setMeasuredDimension(resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec), resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        }
    }
}
