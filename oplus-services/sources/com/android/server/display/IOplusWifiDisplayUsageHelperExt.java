package com.android.server.display;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pWfdInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusWifiDisplayUsageHelperExt {
    default void getDiscoverPeersState(boolean z, Context context) {
    }

    default void getP2pRole(String str, int i, boolean z, Context context) {
    }

    default void getPeers(Context context) {
    }

    default void getSessionInfo(boolean z, String str, String str2, Context context) {
    }

    default void getWfdDisconnected(Context context) {
    }

    default void getWfdScanState(boolean z, Context context) {
    }

    default void reportWfdConnectionTime(Long l, WifiP2pDevice wifiP2pDevice, Context context) {
    }

    default void reportWfdEnableState(WifiP2pWfdInfo wifiP2pWfdInfo, boolean z, Context context) {
    }

    default void wfdConnecteSuceess(WifiP2pGroup wifiP2pGroup, WifiP2pDevice wifiP2pDevice, Context context) {
    }

    default void wfdConnectedFailed(String str, WifiP2pDevice wifiP2pDevice, WifiP2pGroup wifiP2pGroup, Context context) {
    }
}
