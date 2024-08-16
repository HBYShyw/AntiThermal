package com.android.server.notification;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import com.android.server.lights.LogicalLight;
import com.android.server.notification.NotificationManagerService;
import com.android.server.zenmode.IZenModeManagerExt;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface INotificationManagerServiceWrapper {
    default boolean areNotificationsEnabledForPackageInt(String str, int i) {
        return false;
    }

    default void cancelNotificationLocked(NotificationRecord notificationRecord, boolean z, int i, boolean z2, String str, long j) {
    }

    default void checkCallerIsSameApp(String str) {
    }

    default void checkCallerIsSystem() {
    }

    default void checkCallerIsSystemOrSameApp(String str) {
    }

    default boolean checkDisqualifyingFeatures(int i, int i2, int i3, String str, NotificationRecord notificationRecord, boolean z, boolean z2) {
        return false;
    }

    default void checkRestrictedCategories(Notification notification) {
    }

    default void clearLightsLocked() {
    }

    default void doChannelWarningToast(int i, CharSequence charSequence) {
    }

    default ActivityManager getActivityManager() {
        return null;
    }

    default AlarmManager getAlarmManager() {
        return null;
    }

    default IBinder getAllowListToken() {
        return null;
    }

    default Handler getHandler() {
        return null;
    }

    default INotificationManagerServiceExt getNMSExt() {
        return null;
    }

    default LogicalLight getNotificationLight() {
        return null;
    }

    default NotificationManagerService.NotificationListeners getNotificationListeners() {
        return null;
    }

    default NotificationUsageStats getNotificationUsageStats() {
        return null;
    }

    default PackageManager getPackageManagerClient() {
        return null;
    }

    default PermissionHelper getPermissionHelper() {
        return null;
    }

    default IBinder getService() {
        return null;
    }

    default ShortcutHelper getShortcutHelper() {
        return null;
    }

    default SnoozeHelper getSnoozeHelper() {
        return null;
    }

    default String getSoundNotificationKey() {
        return null;
    }

    default String getVibrateNotificationKey() {
        return null;
    }

    default ZenModeHelper getZenModeHelper() {
        return null;
    }

    default IZenModeManagerExt getZenModeManagerExt() {
        return null;
    }

    default boolean isAutomotive() {
        return false;
    }

    default boolean isCallerSystemOrPhone() {
        return false;
    }

    default boolean isInCall() {
        return false;
    }

    default boolean isNotificationForCurrentUser(NotificationRecord notificationRecord) {
        return false;
    }

    default boolean isTelevision() {
        return false;
    }

    default boolean notificationEffectsEnabledForAutomotive() {
        return false;
    }

    default boolean playSound(NotificationRecord notificationRecord, Uri uri) {
        return false;
    }

    default boolean playVibration(NotificationRecord notificationRecord, VibrationEffect vibrationEffect, boolean z) {
        return false;
    }

    default boolean removeFromNotificationListsLocked(NotificationRecord notificationRecord) {
        return false;
    }

    default boolean systemReady() {
        return false;
    }

    default void updateNotificationPulse() {
    }

    default boolean useAttentionLight() {
        return false;
    }
}
