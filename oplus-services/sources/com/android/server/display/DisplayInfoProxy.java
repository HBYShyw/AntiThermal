package com.android.server.display;

import android.hardware.display.DisplayManagerGlobal;
import android.view.DisplayInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DisplayInfoProxy {
    private DisplayInfo mInfo;

    public DisplayInfoProxy(DisplayInfo displayInfo) {
        this.mInfo = displayInfo;
    }

    public void set(DisplayInfo displayInfo) {
        this.mInfo = displayInfo;
        DisplayManagerGlobal.invalidateLocalDisplayInfoCaches();
    }

    public DisplayInfo get() {
        return this.mInfo;
    }
}
