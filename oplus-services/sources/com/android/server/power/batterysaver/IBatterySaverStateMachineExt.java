package com.android.server.power.batterysaver;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IBatterySaverStateMachineExt {
    default void init(Context context) {
    }

    default boolean isOplusFeatureDisalbed() {
        return true;
    }

    default void onBootCompleted(boolean z) {
    }

    default void onSetBatterySaverEnabledManually(boolean z) {
    }
}
