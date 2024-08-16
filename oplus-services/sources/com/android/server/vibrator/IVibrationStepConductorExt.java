package com.android.server.vibrator;

import android.os.VibrationEffect;
import android.os.vibrator.VibrationEffectSegment;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IVibrationStepConductorExt {
    public static final float DEFAULT_AMPLITUDE_RATIO = 1.0f;

    default float getVibrationAmplitudeRatio() {
        return 1.0f;
    }

    default AbstractVibratorStep nextVibrateStep(VibrationEffectSegment vibrationEffectSegment, long j, VibratorController vibratorController, VibrationEffect.Composed composed, int i, long j2) {
        return null;
    }

    default void setVibrationAmplitudeRatio(float f) {
    }

    default void updateVibrationAmplitude(Step step) {
    }
}
