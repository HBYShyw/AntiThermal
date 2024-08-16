package com.android.server.policy;

import android.view.KeyEvent;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IKeyCombinationManagerExt {
    default boolean canAODScreenshot(KeyEvent keyEvent) {
        return false;
    }
}
