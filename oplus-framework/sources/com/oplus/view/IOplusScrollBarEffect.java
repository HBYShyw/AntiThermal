package com.oplus.view;

import android.graphics.Rect;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public interface IOplusScrollBarEffect {

    /* loaded from: classes.dex */
    public interface ViewCallback {
        boolean awakenScrollBars();

        boolean isLayoutRtl();
    }

    void getDrawRect(Rect rect);

    int getThumbLength(int i, int i2, int i3, int i4);

    boolean isTouchPressed();

    void onOverScrolled(int i, int i2, int i3, int i4);

    void onTouchEvent(MotionEvent motionEvent);
}
