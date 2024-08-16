package com.android.server.lights;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ILightsServiceWrapper {
    default boolean getDebug() {
        return false;
    }

    default Object getLightsByType() {
        return null;
    }

    default void setLightLocked(Object obj, int i, int i2, int i3, int i4, int i5) {
    }

    default void setLightUnchecked(Object obj, int i, int i2, int i3, int i4, int i5) {
    }
}
