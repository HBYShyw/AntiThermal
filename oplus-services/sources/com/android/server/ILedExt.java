package com.android.server;

import android.content.Context;
import android.hardware.health.HealthInfo;
import com.android.server.lights.LightsManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ILedExt {
    default void handleScreenState(boolean z) {
    }

    default void initLedExtImpl(Context context, LightsManager lightsManager, Object obj) {
    }

    default boolean isIgnoreUpdateLights(HealthInfo healthInfo) {
        return false;
    }

    default void setDebugSwitch(boolean z) {
    }

    default void systemReady() {
    }

    default void turnOffBatteryLights() {
    }
}
