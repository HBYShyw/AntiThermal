package com.oplus.wrapper.hardware.display;

import android.graphics.Point;
import com.oplus.wrapper.view.Display;

/* loaded from: classes.dex */
public class DisplayManager {
    public static final String DISPLAY_CATEGORY_ALL_INCLUDING_DISABLED = getDisplayCategoryAllIncludingDisabled();
    private final android.hardware.display.DisplayManager mDisplayManager;

    public DisplayManager(android.hardware.display.DisplayManager displayManager) {
        this.mDisplayManager = displayManager;
    }

    private static String getDisplayCategoryAllIncludingDisabled() {
        return "android.hardware.display.category.ALL_INCLUDING_DISABLED";
    }

    public WifiDisplayStatus getWifiDisplayStatus() {
        android.hardware.display.WifiDisplayStatus wifiDisplayStatus = this.mDisplayManager.getWifiDisplayStatus();
        if (wifiDisplayStatus == null) {
            return null;
        }
        return new WifiDisplayStatus(wifiDisplayStatus);
    }

    public void connectWifiDisplay(String deviceAddress) {
        this.mDisplayManager.connectWifiDisplay(deviceAddress);
    }

    public void disconnectWifiDisplay() {
        this.mDisplayManager.disconnectWifiDisplay();
    }

    public Display[] getDisplays() {
        android.view.Display[] displays = this.mDisplayManager.getDisplays();
        Display[] oplusDisplays = new Display[displays.length];
        for (int index = 0; index < displays.length; index++) {
            android.view.Display display = displays[index];
            Display oplusDisplay = null;
            if (display != null) {
                oplusDisplay = new Display(display);
            }
            oplusDisplays[index] = oplusDisplay;
        }
        return oplusDisplays;
    }

    public Point getStableDisplaySize() {
        return this.mDisplayManager.getStableDisplaySize();
    }
}
