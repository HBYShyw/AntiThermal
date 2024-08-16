package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IKeyguardControllerWrapper {
    default IKeyguardControllerExt getExtImpl() {
        return new IKeyguardControllerExt() { // from class: com.android.server.wm.IKeyguardControllerWrapper.1
        };
    }
}
