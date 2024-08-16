package com.oplus.wrapper.hardware.display;

import android.os.Parcelable;

/* loaded from: classes.dex */
public class WifiDisplayStatus {
    private final android.hardware.display.WifiDisplayStatus mWifiDisplayStatus;
    public static final int DISPLAY_STATE_NOT_CONNECTED = getDisplayStateNotConnected();
    public static final int DISPLAY_STATE_CONNECTING = getDisplayStateConnecting();
    public static final int DISPLAY_STATE_CONNECTED = getDisplayStateConnected();

    public WifiDisplayStatus(Parcelable parcelable) throws IllegalArgumentException {
        if (!(parcelable instanceof android.hardware.display.WifiDisplayStatus)) {
            throw new IllegalArgumentException("the argument parcelable must be WifiDisplayStatus!");
        }
        this.mWifiDisplayStatus = (android.hardware.display.WifiDisplayStatus) parcelable;
    }

    private static int getDisplayStateNotConnected() {
        return 0;
    }

    private static int getDisplayStateConnecting() {
        return 1;
    }

    private static int getDisplayStateConnected() {
        return 2;
    }

    public WifiDisplayStatus(android.hardware.display.WifiDisplayStatus wifiDisplayStatus) {
        this.mWifiDisplayStatus = wifiDisplayStatus;
    }

    public int getActiveDisplayState() {
        return this.mWifiDisplayStatus.getActiveDisplayState();
    }

    public WifiDisplay[] getDisplays() {
        android.hardware.display.WifiDisplay[] displays = this.mWifiDisplayStatus.getDisplays();
        if (displays == null || displays.length == 0) {
            return null;
        }
        WifiDisplay[] copy = new WifiDisplay[displays.length];
        for (int index = 0; index < displays.length; index++) {
            WifiDisplay wifiDisplay = null;
            android.hardware.display.WifiDisplay display = displays[index];
            if (display != null) {
                wifiDisplay = new WifiDisplay(display);
            }
            copy[index] = wifiDisplay;
        }
        return copy;
    }

    public WifiDisplay getActiveDisplay() {
        android.hardware.display.WifiDisplay activeDisplay = this.mWifiDisplayStatus.getActiveDisplay();
        if (activeDisplay == null) {
            return null;
        }
        return new WifiDisplay(activeDisplay);
    }

    public int getScanState() {
        return this.mWifiDisplayStatus.getScanState();
    }
}
