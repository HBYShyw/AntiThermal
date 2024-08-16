package com.oplus.wrapper.view;

/* loaded from: classes.dex */
public class Window {
    private final android.view.Window mWindow;

    public Window(android.view.Window window) {
        this.mWindow = window;
    }

    public void setCloseOnTouchOutside(boolean close) {
        this.mWindow.setCloseOnTouchOutside(close);
    }

    public boolean isDestroyed() {
        return this.mWindow.isDestroyed();
    }

    public void addSystemFlags(int flags) {
        this.mWindow.addSystemFlags(flags);
    }
}
