package com.oplus.wrapper.net.wifi.p2p;

import android.net.wifi.p2p.WifiP2pManager;

/* loaded from: classes.dex */
public class WifiP2pManager {
    private final android.net.wifi.p2p.WifiP2pManager mWifiP2pManager;

    /* loaded from: classes.dex */
    public interface PersistentGroupInfoListener {
        void onPersistentGroupInfoAvailable(WifiP2pGroupList wifiP2pGroupList);
    }

    public WifiP2pManager(android.net.wifi.p2p.WifiP2pManager wifiP2pManager) {
        this.mWifiP2pManager = wifiP2pManager;
    }

    public void setWifiP2pChannels(WifiP2pManager.Channel channel, int listeningChannel, int operatingChannel, WifiP2pManager.ActionListener listener) {
        this.mWifiP2pManager.setWifiP2pChannels(channel, listeningChannel, operatingChannel, listener);
    }

    public void requestPersistentGroupInfo(WifiP2pManager.Channel channel, final PersistentGroupInfoListener listener) {
        WifiP2pManager.PersistentGroupInfoListener persistentGroupInfoListener = null;
        if (listener != null) {
            persistentGroupInfoListener = new WifiP2pManager.PersistentGroupInfoListener() { // from class: com.oplus.wrapper.net.wifi.p2p.WifiP2pManager.1
                public void onPersistentGroupInfoAvailable(android.net.wifi.p2p.WifiP2pGroupList wifiP2pGroupList) {
                    if (wifiP2pGroupList == null) {
                        return;
                    }
                    listener.onPersistentGroupInfoAvailable(new WifiP2pGroupList(wifiP2pGroupList));
                }
            };
        }
        this.mWifiP2pManager.requestPersistentGroupInfo(channel, persistentGroupInfoListener);
    }

    public void deletePersistentGroup(WifiP2pManager.Channel channel, int netId, WifiP2pManager.ActionListener listener) {
        this.mWifiP2pManager.deletePersistentGroup(channel, netId, listener);
    }
}
