package com.oplus.wrapper.dalvik.system;

/* loaded from: classes.dex */
public class CloseGuard {
    private final dalvik.system.CloseGuard mCloseGuard;

    private CloseGuard(dalvik.system.CloseGuard closeGuard) {
        this.mCloseGuard = closeGuard;
    }

    public static CloseGuard get() {
        return new CloseGuard(dalvik.system.CloseGuard.get());
    }

    public void open(String closer) {
        this.mCloseGuard.open(closer);
    }

    public void warnIfOpen() {
        this.mCloseGuard.warnIfOpen();
    }
}
