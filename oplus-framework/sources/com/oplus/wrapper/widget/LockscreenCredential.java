package com.oplus.wrapper.widget;

import android.os.Parcelable;

/* loaded from: classes.dex */
public class LockscreenCredential {
    private final com.android.internal.widget.LockscreenCredential mLockscreenCredential;

    LockscreenCredential(com.android.internal.widget.LockscreenCredential lockscreenCredential) {
        this.mLockscreenCredential = lockscreenCredential;
    }

    public static LockscreenCredential createNone() {
        com.android.internal.widget.LockscreenCredential lockscreenCredential = com.android.internal.widget.LockscreenCredential.createNone();
        if (lockscreenCredential == null) {
            return null;
        }
        return new LockscreenCredential(lockscreenCredential);
    }

    public static LockscreenCredential createPassword(CharSequence password) {
        com.android.internal.widget.LockscreenCredential credential = com.android.internal.widget.LockscreenCredential.createPassword(password);
        if (credential == null) {
            return null;
        }
        return new LockscreenCredential(credential);
    }

    public Parcelable getLockscreenCredential() {
        return this.mLockscreenCredential;
    }
}
