package com.android.server.wm;

import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IAsyncRotationControllerWrapper {
    default void forceRemoveOp(WindowToken windowToken) {
    }

    default String getAsyncRotationInfo() {
        return "";
    }

    default Set<WindowToken> getTargetWindowTokens() {
        return null;
    }
}
