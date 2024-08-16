package com.oplus.wrapper.window;

/* loaded from: classes.dex */
public class WindowContainerToken {
    private final android.window.WindowContainerToken mWindowContainerToken;

    public WindowContainerToken(android.window.WindowContainerToken windowContainerToken) {
        this.mWindowContainerToken = windowContainerToken;
    }

    public boolean equals(Object obj) {
        return this.mWindowContainerToken.equals(obj);
    }

    public int hashCode() {
        return this.mWindowContainerToken.hashCode();
    }

    public android.window.WindowContainerToken get() {
        return this.mWindowContainerToken;
    }
}
