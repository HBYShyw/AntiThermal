package com.oplus.wrapper.view;

/* loaded from: classes.dex */
public class MotionEvent {
    private final android.view.MotionEvent mMotionEvent;

    public MotionEvent(android.view.MotionEvent motionEvent) {
        this.mMotionEvent = motionEvent;
    }

    public int getDisplayId() {
        return this.mMotionEvent.getDisplayId();
    }

    public void setDisplayId(int displayId) {
        this.mMotionEvent.setDisplayId(displayId);
    }

    public final void recycle() {
        this.mMotionEvent.recycle();
    }
}
