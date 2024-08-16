package com.oplus.wrapper.net;

/* loaded from: classes.dex */
public class LegacyVpnInfo {
    private final com.android.internal.net.LegacyVpnInfo mLegacyVpnInfo;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LegacyVpnInfo(com.android.internal.net.LegacyVpnInfo legacyVpnInfo) {
        this.mLegacyVpnInfo = legacyVpnInfo;
    }

    public int getState() {
        return this.mLegacyVpnInfo.state;
    }
}
