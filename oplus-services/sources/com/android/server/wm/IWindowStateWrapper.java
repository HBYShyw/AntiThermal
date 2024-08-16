package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IWindowStateWrapper {
    default boolean getAppOpVisibility() {
        return false;
    }

    default IWindowStateExt getExtImpl() {
        return new IWindowStateExt() { // from class: com.android.server.wm.IWindowStateWrapper.1
        };
    }
}
