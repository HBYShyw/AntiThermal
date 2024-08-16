package com.android.server.biometrics;

import android.content.Context;
import android.hardware.biometrics.PromptInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IPreAuthInfoExt {
    default boolean needSkipEligibleSensorAdd(BiometricSensor biometricSensor, int i, String str, Context context, PromptInfo promptInfo) {
        return false;
    }
}
