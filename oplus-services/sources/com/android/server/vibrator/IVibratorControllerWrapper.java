package com.android.server.vibrator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IVibratorControllerWrapper {
    default void linearMotorVibratorOff() {
    }

    default void linearMotorVibratorOn(int i, int i2, boolean z, long j) {
    }

    default void linearMotorVibratorSetVmax(int i) {
    }

    default long performExtPrebaked(int i, long j, int i2, long j2) {
        return -1L;
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

    default IVibratorControllerExt getExtImpl() {
        return new IVibratorControllerExt() { // from class: com.android.server.vibrator.IVibratorControllerWrapper.1
        };
    }
}
