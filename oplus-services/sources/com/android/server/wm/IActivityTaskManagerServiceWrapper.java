package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IActivityTaskManagerServiceWrapper {
    default boolean canShowDialogs() {
        return false;
    }

    default IActivityTaskManagerServiceExt getExtImpl() {
        return null;
    }

    default IFlexibleWindowManagerExt getFlexibleExtImpl() {
        return null;
    }

    default WindowProcessController getHomeProcess() {
        return null;
    }

    default boolean isIOPreloadPkg(String str, int i) {
        return false;
    }
}
