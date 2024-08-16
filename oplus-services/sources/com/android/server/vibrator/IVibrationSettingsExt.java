package com.android.server.vibrator;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IVibrationSettingsExt {
    default void init(Context context) {
    }

    default void onSystemReady() {
    }

    default boolean shouldIgnoreVibration(int i, int i2) {
        return false;
    }

    default boolean shouldVibrateForRingerModeLocked(int i, int i2) {
        return i2 != 0;
    }

    default void updateSettings() {
    }
}
