package com.android.server.am;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IActiveServicesWrapper {
    default void setDynamicalLogEnable(boolean z) {
    }

    default IActiveServicesExt getExtImpl() {
        return new IActiveServicesExt() { // from class: com.android.server.am.IActiveServicesWrapper.1
        };
    }
}
