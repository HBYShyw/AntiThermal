package com.oplus.wrapper.telephony.ims;

/* loaded from: classes.dex */
public class ImsMmTelManager {
    private final android.telephony.ims.ImsMmTelManager mImsMmTelManager;

    public ImsMmTelManager(android.telephony.ims.ImsMmTelManager manager) {
        this.mImsMmTelManager = manager;
    }

    public boolean isCapable(int capability, int imsRegTech) {
        return this.mImsMmTelManager.isCapable(capability, imsRegTech);
    }
}
