package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDisplayContentWrapper {
    default ActivityRecord getFixedRotationLaunchingApp() {
        return null;
    }

    default INonStaticDisplayContentExt getNonStaticExtImpl() {
        return null;
    }

    default IDisplayContentExt getExtImpl() {
        return new IDisplayContentExt() { // from class: com.android.server.wm.IDisplayContentWrapper.1
        };
    }
}
