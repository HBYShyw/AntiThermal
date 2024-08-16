package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ScreenRotationAnimationSocExtImpl implements IScreenRotationAnimationSocExt {
    ScreenRotationAnimation mAnimation;

    @Override // com.android.server.wm.IScreenRotationAnimationSocExt
    public void hookPerfLockAcquired() {
    }

    @Override // com.android.server.wm.IScreenRotationAnimationSocExt
    public void hookPerfLockRelease() {
    }

    @Override // com.android.server.wm.IScreenRotationAnimationSocExt
    public void init() {
    }

    public ScreenRotationAnimationSocExtImpl(Object obj) {
        this.mAnimation = (ScreenRotationAnimation) obj;
    }
}
