package com.coui.appcompat.edittext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class COUIScrolledEditText extends COUIEditText {
    private int E0;

    public COUIScrolledEditText(Context context) {
        super(context);
    }

    @Override // com.coui.appcompat.edittext.COUIEditText, android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 2) {
            this.E0 = (getLineHeight() * getMaxLines()) + getPaddingTop() + getPaddingBottom();
            if (getHeight() >= this.E0 && getLineCount() > 1) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public COUIScrolledEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public COUIScrolledEditText(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }
}
