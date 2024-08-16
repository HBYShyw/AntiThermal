package com.oplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/* loaded from: classes.dex */
public class OplusAutofillSaveTitleTextView extends TextView {
    public OplusAutofillSaveTitleTextView(Context context) {
        this(context, null);
    }

    public OplusAutofillSaveTitleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusAutofillSaveTitleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs();
    }

    private void initAttrs() {
        setTextColor(getResources().getColor(201719832));
    }
}
