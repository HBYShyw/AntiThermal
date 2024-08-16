package com.android.server.vibrator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IVibrationStepConductorWrapper {
    default void notifyVibrationAmplitudeUpdated() {
    }

    default IVibrationStepConductorExt getExtImpl() {
        return new IVibrationStepConductorExt() { // from class: com.android.server.vibrator.IVibrationStepConductorWrapper.1
        };
    }
}
