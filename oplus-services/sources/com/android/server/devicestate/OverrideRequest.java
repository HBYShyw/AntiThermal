package com.android.server.devicestate;

import android.os.IBinder;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class OverrideRequest {
    public static final int OVERRIDE_REQUEST_TYPE_BASE_STATE = 1;
    public static final int OVERRIDE_REQUEST_TYPE_EMULATED_STATE = 0;
    private final int mFlags;
    private final int mPid;
    private final int mRequestType;
    private final int mRequestedState;
    private final IBinder mToken;
    private final int mUid;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface OverrideRequestType {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OverrideRequest(IBinder iBinder, int i, int i2, int i3, int i4, int i5) {
        this.mToken = iBinder;
        this.mPid = i;
        this.mUid = i2;
        this.mRequestedState = i3;
        this.mFlags = i4;
        this.mRequestType = i5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder getToken() {
        return this.mToken;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPid() {
        return this.mPid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getUid() {
        return this.mUid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRequestedState() {
        return this.mRequestedState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getFlags() {
        return this.mFlags;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRequestType() {
        return this.mRequestType;
    }
}
