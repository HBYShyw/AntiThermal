package com.oplus.wrapper.view;

import android.view.Display;

/* loaded from: classes.dex */
public class DisplayInfo {
    private final android.view.DisplayInfo mDisplayInfo;

    public DisplayInfo() {
        this.mDisplayInfo = new android.view.DisplayInfo();
    }

    public DisplayInfo(android.view.DisplayInfo displayInfo) {
        this.mDisplayInfo = displayInfo;
    }

    public int getLogicalHeight() {
        return this.mDisplayInfo.logicalHeight;
    }

    public int getLogicalWidth() {
        return this.mDisplayInfo.logicalWidth;
    }

    public int getRotation() {
        return this.mDisplayInfo.rotation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public android.view.DisplayInfo getInnerDisplayInfo() {
        return this.mDisplayInfo;
    }

    public Display.Mode getMode() {
        return this.mDisplayInfo.getMode();
    }
}
