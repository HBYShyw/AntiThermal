package com.oplus.wrapper.ims;

import android.content.Context;

/* loaded from: classes.dex */
public class ImsManager {
    private final com.android.ims.ImsManager mImsManager;

    private ImsManager(com.android.ims.ImsManager imsManager) {
        this.mImsManager = imsManager;
    }

    public static ImsManager getInstance(Context context, int phoneId) {
        return new ImsManager(com.android.ims.ImsManager.getInstance(context, phoneId));
    }

    public boolean isWfcEnabledByUser() {
        return this.mImsManager.isWfcEnabledByUser();
    }

    public boolean isWfcEnabledByPlatform() {
        return this.mImsManager.isWfcEnabledByPlatform();
    }

    public boolean isVtEnabledByPlatform() {
        return this.mImsManager.isVtEnabledByPlatform();
    }

    public boolean isVolteEnabledByPlatform() {
        return this.mImsManager.isVolteEnabledByPlatform();
    }
}
