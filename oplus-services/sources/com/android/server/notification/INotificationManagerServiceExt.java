package com.android.server.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.media.AudioManager;
import android.media.IRingtonePlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.service.notification.Adjustment;
import android.service.notification.StatusBarNotification;
import com.android.internal.logging.InstanceIdSequence;
import com.android.server.notification.ManagedServices;
import com.android.server.notification.toast.ToastRecord;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface INotificationManagerServiceExt {
    default NotificationManager.Policy adjustNotificationPolicy(String str, NotificationManager.Policy policy) {
        return policy;
    }

    default boolean canListenNotificationChannelChange(String str) {
        return false;
    }

    default boolean canShowLightsLocked(NotificationRecord notificationRecord) {
        return false;
    }

    default void cancelAllLocked(int i, int i2, int i3, int i4, ManagedServices.ManagedServiceInfo managedServiceInfo, boolean z) {
    }

    default void cancelAllNotificationsInt(String str, int i, int i2, String str2, String str3, int i3, int i4, boolean z, int i5, int i6, ManagedServices.ManagedServiceInfo managedServiceInfo) {
    }

    default void clearNonCancelableSummaryKeys(int i) {
    }

    default Intent createAutoGroupSummaryAppIntent(String str) {
        return null;
    }

    default void detectCancelAction(Context context, int i, String str, int i2) {
    }

    default void detectRedPacket(int i, int i2, NotificationRecord notificationRecord) {
    }

    default Uri determineFinalSoundUri(Uri uri) {
        return null;
    }

    default VibrationEffect determineFinalVibrate(VibrationEffect vibrationEffect) {
        return null;
    }

    default boolean dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        return false;
    }

    default boolean enabledAndUserMatches(StatusBarNotification statusBarNotification, ManagedServices.ManagedServiceInfo managedServiceInfo) {
        return false;
    }

    default void fixStopForegroundRemoveFlagSlow() {
    }

    default NotificationChannel getConversationNotificationChannel(String str, int i, String str2, String str3, boolean z, boolean z2) {
        return null;
    }

    default int getMultiAppUserId() {
        return -1;
    }

    default Intent getNotificationCenterIntent() {
        return null;
    }

    default NotificationChannelGroup getNotificationChannelGroupWithChannels(String str, int i, String str2, boolean z) {
        return null;
    }

    default void handleSavePolicyFile() {
    }

    default boolean hasCustomizeBreathLight() {
        return false;
    }

    default void init(NotificationManagerService notificationManagerService, Context context, NotificationRecordLogger notificationRecordLogger, InstanceIdSequence instanceIdSequence) {
    }

    default boolean interceptEnqueueNotificationInternal(String str, String str2, int i, int i2, String str3, int i3, Notification notification, int i4) {
        return false;
    }

    default boolean isAllowedAsConvo(String str) {
        return false;
    }

    default boolean isAppBlockedByWLB(NotificationRecord notificationRecord, PreferencesHelper preferencesHelper) {
        return false;
    }

    default boolean isAppRingtonePermissionGrantedInner(String str, int i) {
        return false;
    }

    default boolean isAppVibrationPermissionGrantedInner(String str, int i) {
        return false;
    }

    default boolean isGroupSummaryCancelableForCancelAll(NotificationRecord notificationRecord, int i, boolean z, ManagedServices.UserProfiles userProfiles, int i2) {
        return false;
    }

    default boolean isHansFreezed(int i, String str, int i2, String str2) {
        return false;
    }

    default boolean isLoggable() {
        return false;
    }

    default boolean isNotificationForCurrentUser(NotificationRecord notificationRecord, int i) {
        return false;
    }

    default boolean isShutdown() {
        return false;
    }

    default boolean isStowOptionKey(Adjustment adjustment, String str) {
        return false;
    }

    default boolean isStudyCenterActivated() {
        return false;
    }

    default boolean isVibrationRingMuteSupport() {
        return false;
    }

    default int modifyToastDisplayIdForMirageDisplay(int i, String str) {
        return 0;
    }

    default void notifyNotificationCentent(String str, String str2, String str3) {
    }

    default void notifyRecordVisibilityChangedLocked(NotificationRecord notificationRecord, boolean z, int i, int i2) {
    }

    default void onBootPhase(int i) {
    }

    default void onClearAllNotifications(int i, int i2, int i3) {
    }

    default boolean onHandleEnqueuedNotification(NotificationRecord notificationRecord, NotificationRecord notificationRecord2) {
        return false;
    }

    default void onPendingNotifyPosted(StatusBarNotification statusBarNotification) {
    }

    default void onStart() {
    }

    default boolean playAsync(IRingtonePlayer iRingtonePlayer, NotificationRecord notificationRecord, Uri uri, boolean z) {
        return false;
    }

    default void scheduleDurationReachedLocked(Handler handler, ToastRecord toastRecord) {
    }

    default void setCurrentShowTime(long j) {
    }

    default void setHapticExtrasForNotification(NotificationRecord notificationRecord) {
    }

    default void setKeepAliveAppIfNeed(String str, int i, boolean z) {
    }

    default void setKeepAliveAppIfNeeded(String str, int i, boolean z) {
    }

    default void setNavigationStatus(String str, String str2, int i, int i2, int i3) {
    }

    default void setSupportConversation(NotificationRecord notificationRecord) {
    }

    default void setVibrationType(String str, Bundle bundle) {
    }

    default boolean shouldBeReplacedByFluidCard(NotificationRecord notificationRecord) {
        return false;
    }

    default boolean shouldContinuousShowToast(ArrayList<ToastRecord> arrayList) {
        return false;
    }

    default boolean shouldContinuousShowToast(boolean z, int i, ArrayList<ToastRecord> arrayList) {
        return false;
    }

    default boolean shouldForcePlayRedPackageRington(NotificationRecord notificationRecord) {
        return false;
    }

    default boolean shouldInterceptToast(int i, String str) {
        return false;
    }

    default boolean shouldInterceptToast(String str) {
        return false;
    }

    default boolean shouldKeepNotifcationWhenForceStop(String str, NotificationRecord notificationRecord, int i) {
        return false;
    }

    default boolean shouldKeepNotifcationWhenTopStyleNotShow(NotificationRecord notificationRecord) {
        return false;
    }

    default boolean shouldLimitChannels(PreferencesHelper preferencesHelper, String str, int i, int i2) {
        return false;
    }

    default boolean shouldShowNotificationToast() {
        return true;
    }

    default boolean shouldSuppressEffect(NotificationRecord notificationRecord) {
        return false;
    }

    default boolean shouldUpdateNavigationMode(String str, int i, int i2) {
        return false;
    }

    default boolean shouldUseHapticFeature(AudioManager audioManager, NotificationRecord notificationRecord) {
        return false;
    }

    default boolean skipToastWhileActPreload(int i, String str) {
        return false;
    }

    default boolean updateLightsStateLocked(NotificationRecord notificationRecord) {
        return false;
    }

    default void updateNoClearNotification(Notification notification, String str) {
    }

    default void updateNotification(int i, String str, String str2, boolean z) {
    }

    default void updateNotification(int i, String str, boolean z) {
    }

    default boolean vibrateLinearmotorIfNeed(boolean z, Uri uri) {
        return false;
    }

    default boolean vibrateLinearmotorIfNeed(boolean z, Uri uri, boolean z2) {
        return false;
    }

    default List<String> getStudyCenterWhiteLists() {
        return new ArrayList();
    }

    default ParceledListSlice<NotificationChannel> getNotificationChannels(String str, int i, boolean z) {
        return ParceledListSlice.emptyList();
    }

    default ParceledListSlice<NotificationChannelGroup> getNotificationChannelGroups(String str, int i, boolean z, boolean z2, boolean z3) {
        return ParceledListSlice.emptyList();
    }

    default void planNextRemove(Handler handler, Runnable runnable, String str) {
        handler.post(runnable);
    }
}
