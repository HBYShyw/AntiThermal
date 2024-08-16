package com.oplus.wrapper.telephony;

import android.os.Bundle;

/* loaded from: classes.dex */
public class ServiceState {
    private final android.telephony.ServiceState mServiceState;

    public ServiceState(android.telephony.ServiceState serviceState) {
        this.mServiceState = serviceState;
    }

    public int getNrState() {
        return this.mServiceState.getNrState();
    }

    public static android.telephony.ServiceState newFromBundle(Bundle m) {
        return android.telephony.ServiceState.newFromBundle(m);
    }

    public int getDataRegState() {
        return this.mServiceState.getDataRegState();
    }
}
