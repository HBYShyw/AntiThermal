package com.android.server.wm;

import com.android.server.wm.SystemGesturesPointerEventListener;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class SystemGesturesPointerEventListenerSocExtImpl implements ISystemGesturesPointerEventListenerSocExt {
    SystemGesturesPointerEventListener mSystemGesturesPointerEventListener;

    @Override // com.android.server.wm.ISystemGesturesPointerEventListenerSocExt
    public boolean hookGetScrollFired() {
        return false;
    }

    @Override // com.android.server.wm.ISystemGesturesPointerEventListenerSocExt
    public void hookOnFling(SystemGesturesPointerEventListener.Callbacks callbacks, float f, float f2, int i) {
    }

    @Override // com.android.server.wm.ISystemGesturesPointerEventListenerSocExt
    public void hookSetScrollFired(boolean z) {
    }

    public SystemGesturesPointerEventListenerSocExtImpl(Object obj) {
        this.mSystemGesturesPointerEventListener = (SystemGesturesPointerEventListener) obj;
    }
}
