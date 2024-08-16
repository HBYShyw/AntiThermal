package com.oplus.wrapper.net;

/* loaded from: classes.dex */
public class NetworkCapabilities {
    private final android.net.NetworkCapabilities mNetworkCapabilities;

    public NetworkCapabilities(android.net.NetworkCapabilities networkCapabilities) {
        this.mNetworkCapabilities = networkCapabilities;
    }

    public String getSsid() {
        return this.mNetworkCapabilities.getSsid();
    }
}
