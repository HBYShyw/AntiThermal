package com.oplus.wrapper.telephony;

/* loaded from: classes.dex */
public class NetworkRegistrationInfo {
    private final android.telephony.NetworkRegistrationInfo mNetworkRegistrationInfo;

    public NetworkRegistrationInfo(android.telephony.NetworkRegistrationInfo networkRegistrationInfo) {
        this.mNetworkRegistrationInfo = networkRegistrationInfo;
    }

    public int getNrState() {
        return this.mNetworkRegistrationInfo.getNrState();
    }
}
