package com.oplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: classes.dex */
public class OplusAutofillSaveLineView extends View {
    public OplusAutofillSaveLineView(Context context) {
        this(context, null);
    }

    public OplusAutofillSaveLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusAutofillSaveLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackground();
    }

    private void initBackground() {
        setBackgroundColor(getResources().getColor(201719833));
    }
}
