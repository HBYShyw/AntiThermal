package com.oplus.wrapper.net;

import android.content.Context;

/* loaded from: classes.dex */
public class NetworkPolicyManager {
    private final android.net.NetworkPolicyManager mNetworkPolicyManager;

    private NetworkPolicyManager(android.net.NetworkPolicyManager networkPolicyManager) {
        this.mNetworkPolicyManager = networkPolicyManager;
    }

    public int getUidPolicy(int uid) {
        return this.mNetworkPolicyManager.getUidPolicy(uid);
    }

    public boolean getRestrictBackground() {
        return this.mNetworkPolicyManager.getRestrictBackground();
    }

    public static NetworkPolicyManager from(Context context) {
        android.net.NetworkPolicyManager networkPolicyManager = android.net.NetworkPolicyManager.from(context);
        if (networkPolicyManager == null) {
            return null;
        }
        return new NetworkPolicyManager(networkPolicyManager);
    }
}
