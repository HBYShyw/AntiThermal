package com.oplus.wrapper.net;

/* loaded from: classes.dex */
public class NetworkStats {
    private final android.net.NetworkStats mNetworkStats;

    public NetworkStats(long elapsedRealtime, int initialSize) {
        this.mNetworkStats = new android.net.NetworkStats(elapsedRealtime, initialSize);
    }

    public long getTotalBytes() {
        return this.mNetworkStats.getTotalBytes();
    }
}
