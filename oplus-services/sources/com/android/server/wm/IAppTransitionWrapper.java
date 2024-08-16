package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IAppTransitionWrapper {
    default String getNextAppTransitionPackage() {
        return null;
    }

    default int getNextAppTransitionType() {
        return 0;
    }

    default IAppTransitionExt getExtImpl() {
        return new IAppTransitionExt() { // from class: com.android.server.wm.IAppTransitionWrapper.1
        };
    }
}
