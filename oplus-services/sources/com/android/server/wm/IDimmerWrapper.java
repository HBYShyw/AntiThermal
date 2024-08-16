package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDimmerWrapper {
    default WindowContainer getLastRequestedDimContainer() {
        return null;
    }

    default IDimmerExt getExtImpl() {
        return new IDimmerExt() { // from class: com.android.server.wm.IDimmerWrapper.1
        };
    }
}
