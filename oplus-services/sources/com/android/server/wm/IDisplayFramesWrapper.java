package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDisplayFramesWrapper extends IDisplayFramesStaticWrapper {
    default IDisplayFramesExt getExtImpl() {
        return new IDisplayFramesExt() { // from class: com.android.server.wm.IDisplayFramesWrapper.1
        };
    }
}
