package com.oplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.Checkable;
import android.widget.LinearLayout;

/* loaded from: classes.dex */
public class OplusCheckableLayout extends LinearLayout implements Checkable {
    private Checkable mCheckable;

    public OplusCheckableLayout(Context context) {
        super(context);
    }

    public OplusCheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OplusCheckableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                KeyEvent.Callback childAt = getChildAt(i);
                if (childAt instanceof Checkable) {
                    this.mCheckable = (Checkable) childAt;
                    return;
                }
            }
        }
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean isChecked) {
        Checkable checkable = this.mCheckable;
        if (checkable != null) {
            checkable.setChecked(isChecked);
        }
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        Checkable checkable = this.mCheckable;
        return checkable != null && checkable.isChecked();
    }

    @Override // android.widget.Checkable
    public void toggle() {
        Checkable checkable = this.mCheckable;
        if (checkable != null) {
            checkable.toggle();
        }
    }
}
