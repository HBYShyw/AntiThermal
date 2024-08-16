package com.android.server.biometrics.sensors.face;

import com.android.server.biometrics.sensors.LockoutTracker;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class LockoutHalImpl implements LockoutTracker {
    private int mCurrentUserLockoutMode;

    @Override // com.android.server.biometrics.sensors.LockoutTracker
    public int getLockoutModeForUser(int i) {
        return this.mCurrentUserLockoutMode;
    }

    public void setCurrentUserLockoutMode(int i) {
        this.mCurrentUserLockoutMode = i;
    }
}
