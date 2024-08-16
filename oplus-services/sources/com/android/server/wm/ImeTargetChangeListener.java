package com.android.server.wm;

import android.os.IBinder;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ImeTargetChangeListener {
    default void onImeInputTargetVisibilityChanged(IBinder iBinder, boolean z, boolean z2) {
    }

    default void onImeTargetOverlayVisibilityChanged(IBinder iBinder, int i, boolean z, boolean z2) {
    }
}
