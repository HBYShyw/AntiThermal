package com.oplus.wrapper.view;

/* loaded from: classes.dex */
public class Surface {
    private final android.view.Surface mSurface;

    public Surface() {
        this.mSurface = new android.view.Surface();
    }

    public Surface(android.view.Surface surface) {
        this.mSurface = surface;
    }

    public void destroy() {
        this.mSurface.destroy();
    }

    public android.view.Surface getSurface() {
        return this.mSurface;
    }
}
