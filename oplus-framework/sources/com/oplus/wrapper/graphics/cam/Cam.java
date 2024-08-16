package com.oplus.wrapper.graphics.cam;

/* loaded from: classes.dex */
public class Cam {
    private final com.android.internal.graphics.cam.Cam mCam;

    public Cam(com.android.internal.graphics.cam.Cam cam) {
        this.mCam = cam;
    }

    public static Cam fromInt(int argb) {
        return new Cam(com.android.internal.graphics.cam.Cam.fromInt(argb));
    }

    public float getChroma() {
        return this.mCam.getChroma();
    }

    public float getHue() {
        return this.mCam.getHue();
    }
}
