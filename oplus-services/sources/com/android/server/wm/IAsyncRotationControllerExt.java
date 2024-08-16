package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IAsyncRotationControllerExt {
    default boolean canBeAsync(WindowToken windowToken) {
        return true;
    }
}
