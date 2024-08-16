package com.oplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

@RemoteViews.RemoteView
/* loaded from: classes.dex */
public class OplusNotificationProgressBar extends ProgressBar {
    public OplusNotificationProgressBar(Context context) {
        super(context);
        initProgressDrawable();
    }

    public OplusNotificationProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProgressDrawable();
    }

    public OplusNotificationProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProgressDrawable();
    }

    public OplusNotificationProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initProgressDrawable();
    }

    private void initProgressDrawable() {
        setProgressDrawable(getResources().getDrawable(201850909));
    }
}
