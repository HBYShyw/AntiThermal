package com.android.server.display;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Handler;
import com.mediatek.server.display.MtkWifiDisplayController;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class WifiDisplayControllerSocExtImpl implements IWifiDisplayControllerSocExt {
    private static final String TAG = "MtkWifiDisplayControllerSocExtImpl";
    private MtkWifiDisplayController mMtkController;

    public WifiDisplayControllerSocExtImpl(Object obj) {
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public WifiP2pConfig overWriteConfig(WifiP2pConfig wifiP2pConfig) {
        return this.mMtkController.overWriteConfig(wifiP2pConfig);
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public void initSocWifiDisplayController(Context context, Handler handler, WifiDisplayController wifiDisplayController) {
        this.mMtkController = new MtkWifiDisplayController(context, handler, wifiDisplayController);
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public void checkReConnect() {
        this.mMtkController.checkReConnect();
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public void setWFD(boolean z) {
        this.mMtkController.setWFD(z);
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public void setRemoveGroupFlag(boolean z) {
        this.mMtkController.setRemoveGroupFlag(z);
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public boolean getRemoveGroupFlag() {
        return this.mMtkController.getRemoveGroupFlag();
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public void setReConnectDevice(WifiP2pDevice wifiP2pDevice) {
        this.mMtkController.setReConnectDevice(wifiP2pDevice);
    }
}
