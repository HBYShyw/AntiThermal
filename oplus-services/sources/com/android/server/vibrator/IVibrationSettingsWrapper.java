package com.android.server.vibrator;

import android.net.Uri;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IVibrationSettingsWrapper {
    default void registerSettingsObserverExt(Uri uri) {
    }

    default void updateNotificationUsageVibrationIntensity(int i) {
    }

    default void updateRingtoneUsageVibrationIntensity(int i) {
    }

    default void updateTouchUsageVibrationIntensity(int i) {
    }

    default IVibrationSettingsExt getExtImpl() {
        return new IVibrationSettingsExt() { // from class: com.android.server.vibrator.IVibrationSettingsWrapper.1
        };
    }
}
