package com.android.server.wm;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DisplayRotationSocExtImpl implements IDisplayRotationSocExt {
    DisplayRotation mRotation;

    @Override // com.android.server.wm.IDisplayRotationSocExt
    public int hookGetWifiDisplayRotation() {
        return -1;
    }

    @Override // com.android.server.wm.IDisplayRotationSocExt
    public boolean hookIsWifiDisplayConnected() {
        return false;
    }

    @Override // com.android.server.wm.IDisplayRotationSocExt
    public void hookRegisterWifiDisplay(Context context, WindowManagerService windowManagerService) {
    }

    public DisplayRotationSocExtImpl(Object obj) {
        this.mRotation = (DisplayRotation) obj;
    }
}
