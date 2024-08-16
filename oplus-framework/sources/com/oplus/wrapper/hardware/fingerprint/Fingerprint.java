package com.oplus.wrapper.hardware.fingerprint;

import android.os.Parcelable;

/* loaded from: classes.dex */
public class Fingerprint {
    private final android.hardware.fingerprint.Fingerprint mFingerprint;

    public Fingerprint(android.hardware.fingerprint.Fingerprint fingerprint) {
        this.mFingerprint = fingerprint;
    }

    public Parcelable getFingerprint() {
        return this.mFingerprint;
    }

    public int getBiometricId() {
        return this.mFingerprint.getBiometricId();
    }

    public int getGroupId() {
        return this.mFingerprint.getGroupId();
    }

    public CharSequence getName() {
        return this.mFingerprint.getName();
    }

    public long getDeviceId() {
        return this.mFingerprint.getDeviceId();
    }
}
