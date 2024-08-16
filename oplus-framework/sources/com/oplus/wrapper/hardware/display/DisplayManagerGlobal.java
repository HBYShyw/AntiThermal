package com.oplus.wrapper.hardware.display;

import com.oplus.wrapper.view.DisplayInfo;

/* loaded from: classes.dex */
public class DisplayManagerGlobal {
    private final android.hardware.display.DisplayManagerGlobal mDisplayManagerGlobal;

    public DisplayManagerGlobal(android.hardware.display.DisplayManagerGlobal displayManagerGlobal) {
        this.mDisplayManagerGlobal = displayManagerGlobal;
    }

    public static DisplayManagerGlobal getInstance() {
        return new DisplayManagerGlobal(android.hardware.display.DisplayManagerGlobal.getInstance());
    }

    public DisplayInfo getDisplayInfo(int displayId) {
        return new DisplayInfo(this.mDisplayManagerGlobal.getDisplayInfo(displayId));
    }
}
