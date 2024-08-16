package com.android.server.vibrator;

import android.os.Handler;
import android.os.PowerManager;
import android.util.SparseArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IVibratorManagerServiceWrapper {
    default VibrationStepConductor getCurrentVibrationStepConductor() {
        return null;
    }

    default Handler getHandler() {
        return null;
    }

    default InputDeviceDelegate getInputDeviceDelegate() {
        return null;
    }

    default Object getSyncLock() {
        return null;
    }

    default VibrationSettings getVibrationSettings() {
        return null;
    }

    default PowerManager.WakeLock getVibratorPartialWakeLock() {
        return null;
    }

    default SparseArray<VibratorController> getVibrators() {
        return null;
    }

    default boolean isDebuggable() {
        return false;
    }

    default void noteVibratorOffExtImpl(int i) {
    }

    default void noteVibratorOnExtImpl(int i, long j) {
    }

    default void setDebuggable(boolean z) {
    }

    default IVibratorManagerServiceExt getExtImpl() {
        return new IVibratorManagerServiceExt() { // from class: com.android.server.vibrator.IVibratorManagerServiceWrapper.1
        };
    }
}
