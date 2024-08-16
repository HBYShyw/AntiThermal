package com.android.server.sensors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ISensorServiceExt {
    default void onBootPhase(int i) {
    }

    default void scheduleRecordProxUsage() {
    }

    @Deprecated
    default void switchADFRState(boolean z) {
    }
}
