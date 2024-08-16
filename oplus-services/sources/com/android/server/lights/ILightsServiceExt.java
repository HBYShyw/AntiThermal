package com.android.server.lights;

import android.content.Context;
import android.os.Looper;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ILightsServiceExt {
    default void dumpOplus(PrintWriter printWriter) {
    }

    default void dumpStackTrace(String str) {
    }

    default void init(Context context, Looper looper) {
    }

    default boolean isOplusBreathingLight(int i) {
        return false;
    }

    default void onBootComplete(int i) {
    }

    default void onSetLight(int i, int i2, int i3) {
    }

    default void setBootAnimationLightInternal(boolean z, int i) {
    }

    default void setOplusLightUnchecked(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
    }
}
