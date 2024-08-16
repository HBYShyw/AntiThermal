package com.oplus.wrapper.view;

/* loaded from: classes.dex */
public class KeyEvent {
    private final android.view.KeyEvent mKeyEvent;

    public KeyEvent(android.view.KeyEvent keyEvent) {
        this.mKeyEvent = keyEvent;
    }

    public final void setDisplayId(int displayId) {
        this.mKeyEvent.setDisplayId(displayId);
    }

    public final void recycle() {
        this.mKeyEvent.recycle();
    }
}
