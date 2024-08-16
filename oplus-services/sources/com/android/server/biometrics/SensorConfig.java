package com.android.server.biometrics;

import android.hardware.biometrics.BiometricManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SensorConfig {
    public final int id;
    final int modality;

    @BiometricManager.Authenticators.Types
    public final int strength;

    public SensorConfig(String str) {
        String[] split = str.split(":");
        this.id = Integer.parseInt(split[0]);
        this.modality = Integer.parseInt(split[1]);
        this.strength = Integer.parseInt(split[2]);
    }
}
