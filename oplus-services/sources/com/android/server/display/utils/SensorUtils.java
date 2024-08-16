package com.android.server.display.utils;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.text.TextUtils;
import com.android.server.display.DisplayDeviceConfig;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SensorUtils {
    public static final int NO_FALLBACK = 0;

    public static Sensor findSensor(SensorManager sensorManager, DisplayDeviceConfig.SensorData sensorData, int i) {
        if (sensorData == null) {
            return null;
        }
        return findSensor(sensorManager, sensorData.type, sensorData.name, i);
    }

    public static Sensor findSensor(SensorManager sensorManager, String str, String str2, int i) {
        if (sensorManager == null) {
            return null;
        }
        boolean z = !TextUtils.isEmpty(str2);
        boolean z2 = !TextUtils.isEmpty(str);
        if (z || z2) {
            for (Sensor sensor : sensorManager.getSensorList(-1)) {
                if (!z || str2.equals(sensor.getName())) {
                    if (!z2 || str.equals(sensor.getStringType())) {
                        return sensor;
                    }
                }
            }
        }
        if (i != 0) {
            return sensorManager.getDefaultSensor(i);
        }
        return null;
    }
}
