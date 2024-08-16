package com.oplus.wrapper.net;

/* loaded from: classes.dex */
public class VpnConfig {
    private final com.android.internal.net.VpnConfig mVpnConfig;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VpnConfig(com.android.internal.net.VpnConfig vpnConfig) {
        this.mVpnConfig = vpnConfig;
    }

    public boolean getLegacy() {
        return this.mVpnConfig.legacy;
    }
}
