package com.android.server.pm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IShortcutServiceWrapper {
    default IShortcutServiceExt getExtImpl() {
        return new IShortcutServiceExt() { // from class: com.android.server.pm.IShortcutServiceWrapper.1
        };
    }
}
