package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ITransitionWrapper {
    default boolean isWcTranslucent(WindowContainer windowContainer) {
        return false;
    }

    default ITransitionExt getExtImpl() {
        return new ITransitionExt() { // from class: com.android.server.wm.ITransitionWrapper.1
        };
    }
}
