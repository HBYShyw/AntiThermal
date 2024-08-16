package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowManagerServiceSocExtImpl implements IWindowManagerServiceSocExt {
    WindowManagerService mWindowManagerService;

    @Override // com.android.server.wm.IWindowManagerServiceSocExt
    public void hookStartFreezingDisplay() {
    }

    @Override // com.android.server.wm.IWindowManagerServiceSocExt
    public void hookStopFreezingDisplayLocked() {
    }

    public WindowManagerServiceSocExtImpl(Object obj) {
        this.mWindowManagerService = (WindowManagerService) obj;
    }
}
