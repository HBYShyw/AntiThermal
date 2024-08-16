package com.android.server.notification;

import android.content.Context;
import android.os.VibrationEffect;
import com.android.server.notification.NotificationRecord;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface INotificationRecordExt {
    public static final int IMPORTANCE_FOR_PACKAGE = 3;

    default int adjustImportanceForPackage(int i) {
        return 3;
    }

    default void adjustPackageVisibilityOverride(int i) {
    }

    default int calculateColor(String str, int i, int i2) {
        return i2;
    }

    default NotificationRecord.Light calculateLights(boolean z, int i, int i2, int i3) {
        return null;
    }

    default NotificationRecord.Light calculateLights(boolean z, NotificationRecord.Light light) {
        return light;
    }

    default VibrationEffect createDefaultVibration(VibratorHelper vibratorHelper, boolean z) {
        return null;
    }

    default boolean getAppBanner() {
        return true;
    }

    default NotificationRecord.Light getCustomizeBreathLight(Context context) {
        return null;
    }

    default boolean getIsSupportRearLight() {
        return false;
    }

    default boolean getSupportConversation() {
        return false;
    }

    default boolean hasCustomizeBreathLight() {
        return false;
    }

    default boolean isLoggable() {
        return false;
    }

    default boolean isMultilLed() {
        return false;
    }

    default boolean isUnimportant() {
        return false;
    }

    default boolean isVersionForJP() {
        return false;
    }

    default long[] modifyVibrationPatternIfNeeded(long[] jArr, boolean z) {
        return jArr;
    }

    default void setAppBanner(boolean z) {
    }

    default void setSupportConversation(boolean z) {
    }

    default void setUnimportant(boolean z) {
    }
}
