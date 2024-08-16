package com.android.server.vibrator;

import com.android.server.vibrator.VibratorController;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IVibratorControllerExt {
    default void linearMotorVibratorOff() {
    }

    default void linearMotorVibratorOn(int i, int i2, boolean z, long j) {
    }

    default void linearMotorVibratorSetVmax(int i) {
    }

    default int richtapPerformEffect(int i, byte b, long j) {
        return -1;
    }

    default void richtapPerformEnvelope(int[] iArr, boolean z, int i, long j) {
    }

    default void richtapPerformHe(int i, int i2, int i3, int i4, int[] iArr, long j) {
    }

    default void richtapSetAmplitude(int i) {
    }

    default void richtapStop() {
    }

    default void setOnVibrationCompleteListener(VibratorController.OnVibrationCompleteListener onVibrationCompleteListener) {
    }
}
