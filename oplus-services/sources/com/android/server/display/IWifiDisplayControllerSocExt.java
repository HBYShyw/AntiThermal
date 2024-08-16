package com.android.server.display;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Handler;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IWifiDisplayControllerSocExt {
    default void checkReConnect() {
    }

    default boolean getRemoveGroupFlag() {
        return false;
    }

    default void initSocWifiDisplayController(Context context, Handler handler, WifiDisplayController wifiDisplayController) {
    }

    default WifiP2pConfig overWriteConfig(WifiP2pConfig wifiP2pConfig) {
        return null;
    }

    default void setReConnectDevice(WifiP2pDevice wifiP2pDevice) {
    }

    default void setRemoveGroupFlag(boolean z) {
    }

    default void setWFD(boolean z) {
    }
}
