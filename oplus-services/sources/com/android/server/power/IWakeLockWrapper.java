package com.android.server.power;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IWakeLockWrapper {
    default String getLockLevelString() {
        return "";
    }

    default IWakeLockExt getExtImpl() {
        return new IWakeLockExt() { // from class: com.android.server.power.IWakeLockWrapper.1
        };
    }
}
