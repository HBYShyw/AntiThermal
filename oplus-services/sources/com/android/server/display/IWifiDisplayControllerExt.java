package com.android.server.display;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IWifiDisplayControllerExt {
    default WifiP2pConfig generateConfigByGoIntent(WifiP2pConfig wifiP2pConfig, WifiP2pDevice wifiP2pDevice) {
        return wifiP2pConfig;
    }

    default void initWifiDisplayControllerExtImpl(Context context) {
    }

    default void updateWFDControl() {
    }
}
