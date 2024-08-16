package com.oplus.wrapper.telecom;

/* loaded from: classes.dex */
public class TelecomManager {
    private final android.telecom.TelecomManager mTelecomManager;

    public TelecomManager(android.telecom.TelecomManager telecomManager) {
        this.mTelecomManager = telecomManager;
    }

    public boolean isRinging() {
        return this.mTelecomManager.isRinging();
    }

    public boolean isInEmergencyCall() {
        return this.mTelecomManager.isInEmergencyCall();
    }
}
