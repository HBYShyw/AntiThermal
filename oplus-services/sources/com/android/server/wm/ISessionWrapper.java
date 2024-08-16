package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ISessionWrapper {
    default ISessionExt getExtImpl() {
        return new ISessionExt() { // from class: com.android.server.wm.ISessionWrapper.1
        };
    }
}
