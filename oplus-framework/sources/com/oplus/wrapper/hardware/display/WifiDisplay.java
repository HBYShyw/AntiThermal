package com.oplus.wrapper.hardware.display;

/* loaded from: classes.dex */
public class WifiDisplay {
    private final android.hardware.display.WifiDisplay mWifiDisplay;

    public WifiDisplay(android.hardware.display.WifiDisplay wifiDisplay) {
        this.mWifiDisplay = wifiDisplay;
    }

    public boolean isAvailable() {
        return this.mWifiDisplay.isAvailable();
    }

    public String getDeviceName() {
        return this.mWifiDisplay.getDeviceName();
    }

    public String getDeviceAddress() {
        return this.mWifiDisplay.getDeviceAddress();
    }
}
