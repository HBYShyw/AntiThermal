package com.android.server.policy;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.ArraySet;
import android.view.Display;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IDeviceStateProviderImplExt {
    default int adjustDeviceState(int i, int i2, Map<Sensor, SensorEvent> map, Sensor sensor) {
        return i2;
    }

    default boolean getDisplayOn(int i) {
        return false;
    }

    default int getSensorDelay(int i, String str) {
        return 0;
    }

    default void init(DeviceStateProviderImpl deviceStateProviderImpl, Context context) {
    }

    default boolean isNeedBreakSetDeviceState(int i) {
        return false;
    }

    default boolean isNeedInterceptDeviceState(int i, int i2) {
        return false;
    }

    default void notifyCreateSleepToken(String str) {
    }

    default void notifyCreateSleepToken(String str, int i, Display display) {
    }

    default void notifyRemoveSleepToken(String str) {
    }

    default void notifyRemoveSleepToken(String str, int i, Display display) {
    }

    default void onBootPhase(int i) {
    }

    default void registerSensor(SensorEventListener sensorEventListener) {
    }

    default void setNeededSensors(ArraySet<Sensor> arraySet) {
    }

    default boolean unregisterSensorsIfLockStateChanged(int i, boolean z) {
        return true;
    }
}
