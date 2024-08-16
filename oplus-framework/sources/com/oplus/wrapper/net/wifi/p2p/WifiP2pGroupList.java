package com.oplus.wrapper.net.wifi.p2p;

import android.net.wifi.p2p.WifiP2pGroup;
import java.util.List;

/* loaded from: classes.dex */
public class WifiP2pGroupList {
    private final android.net.wifi.p2p.WifiP2pGroupList mWifiP2pGroupList;

    public WifiP2pGroupList(android.net.wifi.p2p.WifiP2pGroupList wifiP2pGroupList) {
        this.mWifiP2pGroupList = wifiP2pGroupList;
    }

    public List<WifiP2pGroup> getGroupList() {
        return this.mWifiP2pGroupList.getGroupList();
    }
}
