package com.oplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/* loaded from: classes.dex */
public class OplusAutofillSaveTextView extends TextView {
    public OplusAutofillSaveTextView(Context context) {
        this(context, null);
    }

    public OplusAutofillSaveTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusAutofillSaveTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs();
    }

    private void initAttrs() {
        setTextColor(getResources().getColor(201719831));
        setBackgroundResource(201850908);
    }
}
