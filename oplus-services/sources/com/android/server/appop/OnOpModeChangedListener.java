package com.android.server.appop;

import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class OnOpModeChangedListener {
    private static final int UID_ANY = -2;
    private int mCallingPid;
    private int mCallingUid;
    private int mFlags;
    private int mWatchedOpCode;
    private int mWatchingUid;

    public abstract void onOpModeChanged(int i, int i2, String str) throws RemoteException;

    public abstract String toString();

    /* JADX INFO: Access modifiers changed from: package-private */
    public OnOpModeChangedListener(int i, int i2, int i3, int i4, int i5) {
        this.mWatchingUid = i;
        this.mFlags = i2;
        this.mWatchedOpCode = i3;
        this.mCallingUid = i4;
        this.mCallingPid = i5;
    }

    public int getWatchingUid() {
        return this.mWatchingUid;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public int getWatchedOpCode() {
        return this.mWatchedOpCode;
    }

    public int getCallingUid() {
        return this.mCallingUid;
    }

    public int getCallingPid() {
        return this.mCallingPid;
    }

    public boolean isWatchingUid(int i) {
        int i2;
        return i == -2 || (i2 = this.mWatchingUid) < 0 || i2 == i;
    }
}
