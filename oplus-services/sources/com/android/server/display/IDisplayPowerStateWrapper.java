package com.android.server.display;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IDisplayPowerStateWrapper {
    default boolean getColorFadePrepared() {
        return false;
    }

    default boolean getDebug() {
        return false;
    }

    default void scheduleScreenUpdate() {
    }

    default void setLoggingEnabled(boolean z) {
    }

    default void setScreenReady(boolean z) {
    }
}
