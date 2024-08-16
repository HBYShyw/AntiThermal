package com.oplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/* loaded from: classes.dex */
public class OplusAutofillSaveLayout extends LinearLayout {
    public OplusAutofillSaveLayout(Context context) {
        this(context, null);
    }

    public OplusAutofillSaveLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusAutofillSaveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackground();
    }

    private void initBackground() {
        setBackgroundResource(201850907);
    }
}
