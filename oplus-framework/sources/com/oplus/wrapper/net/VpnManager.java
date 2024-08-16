package com.oplus.wrapper.net;

/* loaded from: classes.dex */
public class VpnManager {
    private final android.net.VpnManager mVpnManager;

    public VpnManager(android.net.VpnManager vpnManager) {
        this.mVpnManager = vpnManager;
    }

    public VpnConfig getVpnConfig(int userId) {
        com.android.internal.net.VpnConfig vpnConfig = this.mVpnManager.getVpnConfig(userId);
        if (vpnConfig == null) {
            return null;
        }
        return new VpnConfig(vpnConfig);
    }

    public LegacyVpnInfo getLegacyVpnInfo(int userId) {
        com.android.internal.net.LegacyVpnInfo legacyVpnInfo = this.mVpnManager.getLegacyVpnInfo(userId);
        if (legacyVpnInfo == null) {
            return null;
        }
        return new LegacyVpnInfo(legacyVpnInfo);
    }
}
