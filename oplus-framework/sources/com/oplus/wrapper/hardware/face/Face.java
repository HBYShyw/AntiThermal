package com.oplus.wrapper.hardware.face;

import android.os.Parcelable;

/* loaded from: classes.dex */
public class Face {
    private final android.hardware.face.Face mFace;

    public Face(android.hardware.face.Face face) {
        this.mFace = face;
    }

    public Parcelable getFace() {
        return this.mFace;
    }

    public CharSequence getName() {
        return this.mFace.getName();
    }

    public int getBiometricId() {
        return this.mFace.getBiometricId();
    }

    public long getDeviceId() {
        return this.mFace.getDeviceId();
    }
}
