package com.android.server.display;

import android.util.Pair;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IDisplayPowerStateExt {
    default boolean getAodStatus() {
        return false;
    }

    default int getBootupBrightness() {
        return 400;
    }

    default void setAodStatus(boolean z) {
    }

    default void setColorFadeLevel(float f) {
    }

    default void setDisplayThreadSched(int i, int i2) {
    }

    default void setUxThread() {
    }

    default Pair<Float, Float> screenUpdateExt(int i, float f, float f2, float f3, float f4, int i2) {
        return Pair.create(Float.valueOf(f), Float.valueOf(f2));
    }
}
