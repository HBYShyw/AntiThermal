package com.oplus.wrapper.app;

import android.app.AutomaticZenRule;
import android.app.INotificationManager;
import android.app.ITransientNotification;
import android.app.ITransientNotificationCallback;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationHistory;
import android.app.NotificationManager;
import android.content.AttributionSource;
import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.Adjustment;
import android.service.notification.Condition;
import android.service.notification.IConditionProvider;
import android.service.notification.INotificationListener;
import android.service.notification.NotificationListenerFilter;
import android.service.notification.StatusBarNotification;
import android.service.notification.ZenModeConfig;
import com.oplus.wrapper.content.pm.ParceledListSlice;
import java.util.List;

/* loaded from: classes.dex */
public interface INotificationManager {
    boolean areNotificationsEnabledForPackage(String str, int i) throws RemoteException;

    void cancelAllNotifications(String str, int i) throws RemoteException;

    void createNotificationChannelGroups(String str, ParceledListSlice<?> parceledListSlice) throws RemoteException;

    StatusBarNotification[] getActiveNotifications(String str) throws RemoteException;

    ParceledListSlice<?> getNotificationChannelGroups(String str) throws RemoteException;

    void updateNotificationChannelForPackage(String str, int i, NotificationChannel notificationChannel) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, INotificationManager {
        private final android.app.INotificationManager mTarget = new INotificationManager.Stub() { // from class: com.oplus.wrapper.app.INotificationManager.Stub.1
            public void cancelAllNotifications(String pkg, int userId) throws RemoteException {
                Stub.this.cancelAllNotifications(pkg, userId);
            }

            public void clearData(String s, int i, boolean b) throws RemoteException {
            }

            public void enqueueTextToast(String s, IBinder iBinder, CharSequence charSequence, int i, boolean isUiContext, int i1, ITransientNotificationCallback iTransientNotificationCallback) throws RemoteException {
            }

            public void enqueueToast(String s, IBinder iBinder, ITransientNotification iTransientNotification, int i, boolean isUiContext, int i1) throws RemoteException {
            }

            public void cancelToast(String s, IBinder iBinder) throws RemoteException {
            }

            public void finishToken(String s, IBinder iBinder) throws RemoteException {
            }

            public void enqueueNotificationWithTag(String s, String s1, String s2, int i, Notification notification, int i1) throws RemoteException {
            }

            public void cancelNotificationWithTag(String s, String s1, String s2, int i, int i1) throws RemoteException {
            }

            public boolean isInCall(String s, int i) throws RemoteException {
                return false;
            }

            public boolean canUseFullScreenIntent(AttributionSource attributionSource) throws RemoteException {
                return false;
            }

            public void setShowBadge(String s, int i, boolean b) throws RemoteException {
            }

            public boolean canShowBadge(String s, int i) throws RemoteException {
                return false;
            }

            public boolean hasSentValidMsg(String s, int i) throws RemoteException {
                return false;
            }

            public boolean isInInvalidMsgState(String s, int i) throws RemoteException {
                return false;
            }

            public boolean hasUserDemotedInvalidMsgApp(String s, int i) throws RemoteException {
                return false;
            }

            public void setInvalidMsgAppDemoted(String s, int i, boolean b) throws RemoteException {
            }

            public boolean hasSentValidBubble(String s, int i) throws RemoteException {
                return false;
            }

            public void setNotificationsEnabledForPackage(String s, int i, boolean b) throws RemoteException {
            }

            public void setNotificationsEnabledWithImportanceLockForPackage(String s, int i, boolean b) throws RemoteException {
            }

            public boolean areNotificationsEnabledForPackage(String pkg, int uid) throws RemoteException {
                return Stub.this.areNotificationsEnabledForPackage(pkg, uid);
            }

            public boolean areNotificationsEnabled(String s) throws RemoteException {
                return false;
            }

            public int getPackageImportance(String s) throws RemoteException {
                return 0;
            }

            public boolean isImportanceLocked(String s, int i) throws RemoteException {
                return false;
            }

            public List<String> getAllowedAssistantAdjustments(String s) throws RemoteException {
                return null;
            }

            public boolean shouldHideSilentStatusIcons(String s) throws RemoteException {
                return false;
            }

            public void setHideSilentStatusIcons(boolean b) throws RemoteException {
            }

            public void setBubblesAllowed(String s, int i, int i1) throws RemoteException {
            }

            public boolean areBubblesAllowed(String s) throws RemoteException {
                return false;
            }

            public boolean areBubblesEnabled(UserHandle userHandle) throws RemoteException {
                return false;
            }

            public int getBubblePreferenceForPackage(String s, int i) throws RemoteException {
                return 0;
            }

            public void createNotificationChannelGroups(String pkg, android.content.pm.ParceledListSlice channelGroupList) throws RemoteException {
                if (channelGroupList == null) {
                    return;
                }
                ParceledListSlice listSlice = new ParceledListSlice(channelGroupList);
                Stub.this.createNotificationChannelGroups(pkg, listSlice);
            }

            public void createNotificationChannels(String s, android.content.pm.ParceledListSlice parceledListSlice) throws RemoteException {
            }

            public void createNotificationChannelsForPackage(String s, int i, android.content.pm.ParceledListSlice parceledListSlice) throws RemoteException {
            }

            public android.content.pm.ParceledListSlice getConversations(boolean b) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice getConversationsForPackage(String s, int i) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice getNotificationChannelGroupsForPackage(String s, int i, boolean b) throws RemoteException {
                return null;
            }

            public NotificationChannelGroup getNotificationChannelGroupForPackage(String s, String s1, int i) throws RemoteException {
                return null;
            }

            public NotificationChannelGroup getPopulatedNotificationChannelGroupForPackage(String s, int i, String s1, boolean b) throws RemoteException {
                return null;
            }

            public void updateNotificationChannelGroupForPackage(String s, int i, NotificationChannelGroup notificationChannelGroup) throws RemoteException {
            }

            public void updateNotificationChannelForPackage(String s, int i, NotificationChannel notificationChannel) throws RemoteException {
                Stub.this.updateNotificationChannelForPackage(s, i, notificationChannel);
            }

            public void unlockNotificationChannel(String s, int i, String s1) throws RemoteException {
            }

            public void unlockAllNotificationChannels() throws RemoteException {
            }

            public NotificationChannel getNotificationChannel(String s, int i, String s1, String s2) throws RemoteException {
                return null;
            }

            public NotificationChannel getConversationNotificationChannel(String s, int i, String s1, String s2, boolean b, String s3) throws RemoteException {
                return null;
            }

            public void createConversationNotificationChannelForPackage(String s, int i, NotificationChannel notificationChannel, String s1) throws RemoteException {
            }

            public NotificationChannel getNotificationChannelForPackage(String s, int i, String s1, String s2, boolean b) throws RemoteException {
                return null;
            }

            public void deleteNotificationChannel(String s, String s1) throws RemoteException {
            }

            public android.content.pm.ParceledListSlice<?> getNotificationChannels(String s, String s1, int i) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice<?> getNotificationChannelsForPackage(String s, int i, boolean b) throws RemoteException {
                return null;
            }

            public int getNumNotificationChannelsForPackage(String s, int i, boolean b) throws RemoteException {
                return 0;
            }

            public int getDeletedChannelCount(String s, int i) throws RemoteException {
                return 0;
            }

            public int getBlockedChannelCount(String s, int i) throws RemoteException {
                return 0;
            }

            public void deleteNotificationChannelGroup(String s, String s1) throws RemoteException {
            }

            public NotificationChannelGroup getNotificationChannelGroup(String s, String s1) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice<?> getNotificationChannelGroups(String pkg) throws RemoteException {
                ParceledListSlice<?> parceledListSlice = Stub.this.getNotificationChannelGroups(pkg);
                return parceledListSlice.getParceledListSlice();
            }

            public boolean onlyHasDefaultChannel(String s, int i) throws RemoteException {
                return false;
            }

            public boolean areChannelsBypassingDnd() throws RemoteException {
                return false;
            }

            public android.content.pm.ParceledListSlice<?> getNotificationChannelsBypassingDnd(String s, int i) throws RemoteException {
                return null;
            }

            public boolean isPackagePaused(String s) throws RemoteException {
                return false;
            }

            public void deleteNotificationHistoryItem(String s, int i, long l) throws RemoteException {
            }

            public boolean isPermissionFixed(String s, int i) throws RemoteException {
                return false;
            }

            public void silenceNotificationSound() throws RemoteException {
            }

            public StatusBarNotification[] getActiveNotifications(String callingPkg) throws RemoteException {
                return Stub.this.getActiveNotifications(callingPkg);
            }

            public StatusBarNotification[] getActiveNotificationsWithAttribution(String s, String s1) throws RemoteException {
                return new StatusBarNotification[0];
            }

            public StatusBarNotification[] getHistoricalNotifications(String s, int i, boolean b) throws RemoteException {
                return new StatusBarNotification[0];
            }

            public StatusBarNotification[] getHistoricalNotificationsWithAttribution(String s, String s1, int i, boolean b) throws RemoteException {
                return new StatusBarNotification[0];
            }

            public NotificationHistory getNotificationHistory(String s, String s1) throws RemoteException {
                return null;
            }

            public void registerListener(INotificationListener iNotificationListener, ComponentName componentName, int i) throws RemoteException {
            }

            public void unregisterListener(INotificationListener iNotificationListener, int i) throws RemoteException {
            }

            public void cancelNotificationFromListener(INotificationListener iNotificationListener, String s, String s1, int i) throws RemoteException {
            }

            public void cancelNotificationsFromListener(INotificationListener iNotificationListener, String[] strings) throws RemoteException {
            }

            public void snoozeNotificationUntilContextFromListener(INotificationListener iNotificationListener, String s, String s1) throws RemoteException {
            }

            public void snoozeNotificationUntilFromListener(INotificationListener iNotificationListener, String s, long l) throws RemoteException {
            }

            public void requestBindListener(ComponentName componentName) throws RemoteException {
            }

            public void requestUnbindListener(INotificationListener iNotificationListener) throws RemoteException {
            }

            public void requestUnbindListenerComponent(ComponentName component) throws RemoteException {
            }

            public void requestBindProvider(ComponentName componentName) throws RemoteException {
            }

            public void requestUnbindProvider(IConditionProvider iConditionProvider) throws RemoteException {
            }

            public void setNotificationsShownFromListener(INotificationListener iNotificationListener, String[] strings) throws RemoteException {
            }

            public android.content.pm.ParceledListSlice getActiveNotificationsFromListener(INotificationListener iNotificationListener, String[] strings, int i) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice getSnoozedNotificationsFromListener(INotificationListener iNotificationListener, int i) throws RemoteException {
                return null;
            }

            public void clearRequestedListenerHints(INotificationListener iNotificationListener) throws RemoteException {
            }

            public void requestHintsFromListener(INotificationListener iNotificationListener, int i) throws RemoteException {
            }

            public int getHintsFromListener(INotificationListener iNotificationListener) throws RemoteException {
                return 0;
            }

            public int getHintsFromListenerNoToken() throws RemoteException {
                return 0;
            }

            public void requestInterruptionFilterFromListener(INotificationListener iNotificationListener, int i) throws RemoteException {
            }

            public int getInterruptionFilterFromListener(INotificationListener iNotificationListener) throws RemoteException {
                return 0;
            }

            public void setOnNotificationPostedTrimFromListener(INotificationListener iNotificationListener, int i) throws RemoteException {
            }

            public void setInterruptionFilter(String s, int i) throws RemoteException {
            }

            public void updateNotificationChannelGroupFromPrivilegedListener(INotificationListener iNotificationListener, String s, UserHandle userHandle, NotificationChannelGroup notificationChannelGroup) throws RemoteException {
            }

            public void updateNotificationChannelFromPrivilegedListener(INotificationListener iNotificationListener, String s, UserHandle userHandle, NotificationChannel notificationChannel) throws RemoteException {
            }

            public android.content.pm.ParceledListSlice getNotificationChannelsFromPrivilegedListener(INotificationListener iNotificationListener, String s, UserHandle userHandle) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice getNotificationChannelGroupsFromPrivilegedListener(INotificationListener iNotificationListener, String s, UserHandle userHandle) throws RemoteException {
                return null;
            }

            public void applyEnqueuedAdjustmentFromAssistant(INotificationListener iNotificationListener, Adjustment adjustment) throws RemoteException {
            }

            public void applyAdjustmentFromAssistant(INotificationListener iNotificationListener, Adjustment adjustment) throws RemoteException {
            }

            public void applyAdjustmentsFromAssistant(INotificationListener iNotificationListener, List<Adjustment> list) throws RemoteException {
            }

            public void unsnoozeNotificationFromAssistant(INotificationListener iNotificationListener, String s) throws RemoteException {
            }

            public void unsnoozeNotificationFromSystemListener(INotificationListener iNotificationListener, String s) throws RemoteException {
            }

            public ComponentName getEffectsSuppressor() throws RemoteException {
                return null;
            }

            public boolean matchesCallFilter(Bundle bundle) throws RemoteException {
                return false;
            }

            public void cleanUpCallersAfter(long l) throws RemoteException {
            }

            public boolean isSystemConditionProviderEnabled(String s) throws RemoteException {
                return false;
            }

            public boolean isNotificationListenerAccessGranted(ComponentName componentName) throws RemoteException {
                return false;
            }

            public boolean isNotificationListenerAccessGrantedForUser(ComponentName componentName, int i) throws RemoteException {
                return false;
            }

            public boolean isNotificationAssistantAccessGranted(ComponentName componentName) throws RemoteException {
                return false;
            }

            public void setNotificationListenerAccessGranted(ComponentName componentName, boolean b, boolean b1) throws RemoteException {
            }

            public void setNotificationAssistantAccessGranted(ComponentName componentName, boolean b) throws RemoteException {
            }

            public void setNotificationListenerAccessGrantedForUser(ComponentName componentName, int i, boolean b, boolean b1) throws RemoteException {
            }

            public void setNotificationAssistantAccessGrantedForUser(ComponentName componentName, int i, boolean b) throws RemoteException {
            }

            public List<String> getEnabledNotificationListenerPackages() throws RemoteException {
                return null;
            }

            public List<ComponentName> getEnabledNotificationListeners(int i) throws RemoteException {
                return null;
            }

            public ComponentName getAllowedNotificationAssistantForUser(int i) throws RemoteException {
                return null;
            }

            public ComponentName getAllowedNotificationAssistant() throws RemoteException {
                return null;
            }

            public ComponentName getDefaultNotificationAssistant() throws RemoteException {
                return null;
            }

            public void setNASMigrationDoneAndResetDefault(int i, boolean b) throws RemoteException {
            }

            public boolean hasEnabledNotificationListener(String s, int i) throws RemoteException {
                return false;
            }

            public int getZenMode() throws RemoteException {
                return 0;
            }

            public ZenModeConfig getZenModeConfig() throws RemoteException {
                return null;
            }

            public NotificationManager.Policy getConsolidatedNotificationPolicy() throws RemoteException {
                return null;
            }

            public void setZenMode(int i, Uri uri, String s) throws RemoteException {
            }

            public void notifyConditions(String s, IConditionProvider iConditionProvider, Condition[] conditions) throws RemoteException {
            }

            public boolean isNotificationPolicyAccessGranted(String s) throws RemoteException {
                return false;
            }

            public NotificationManager.Policy getNotificationPolicy(String s) throws RemoteException {
                return null;
            }

            public void setNotificationPolicy(String s, NotificationManager.Policy policy) throws RemoteException {
            }

            public boolean isNotificationPolicyAccessGrantedForPackage(String s) throws RemoteException {
                return false;
            }

            public void setNotificationPolicyAccessGranted(String s, boolean b) throws RemoteException {
            }

            public void setNotificationPolicyAccessGrantedForUser(String s, int i, boolean b) throws RemoteException {
            }

            public AutomaticZenRule getAutomaticZenRule(String s) throws RemoteException {
                return null;
            }

            public List<ZenModeConfig.ZenRule> getZenRules() throws RemoteException {
                return null;
            }

            public String addAutomaticZenRule(AutomaticZenRule automaticZenRule, String s) throws RemoteException {
                return null;
            }

            public boolean updateAutomaticZenRule(String s, AutomaticZenRule automaticZenRule) throws RemoteException {
                return false;
            }

            public boolean removeAutomaticZenRule(String s) throws RemoteException {
                return false;
            }

            public boolean removeAutomaticZenRules(String s) throws RemoteException {
                return false;
            }

            public int getRuleInstanceCount(ComponentName componentName) throws RemoteException {
                return 0;
            }

            public void setAutomaticZenRuleState(String s, Condition condition) throws RemoteException {
            }

            public byte[] getBackupPayload(int i) throws RemoteException {
                return new byte[0];
            }

            public void applyRestore(byte[] bytes, int i) throws RemoteException {
            }

            public android.content.pm.ParceledListSlice getAppActiveNotifications(String s, int i) throws RemoteException {
                return null;
            }

            public void setNotificationDelegate(String s, String s1) throws RemoteException {
            }

            public String getNotificationDelegate(String s) throws RemoteException {
                return null;
            }

            public boolean canNotifyAsPackage(String s, String s1, int i) throws RemoteException {
                return false;
            }

            public void setPrivateNotificationsAllowed(boolean b) throws RemoteException {
            }

            public boolean getPrivateNotificationsAllowed() throws RemoteException {
                return false;
            }

            public long pullStats(long l, int i, boolean b, List<ParcelFileDescriptor> list) throws RemoteException {
                return 0L;
            }

            public NotificationListenerFilter getListenerFilter(ComponentName componentName, int i) throws RemoteException {
                return null;
            }

            public void setListenerFilter(ComponentName componentName, int i, NotificationListenerFilter notificationListenerFilter) throws RemoteException {
            }

            public void migrateNotificationFilter(INotificationListener iNotificationListener, int i, List<String> list) throws RemoteException {
            }

            public void setToastRateLimitingEnabled(boolean b) throws RemoteException {
            }
        };

        public static INotificationManager asInterface(IBinder obj) {
            return new Proxy(INotificationManager.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements INotificationManager {
            private final android.app.INotificationManager mTarget;

            Proxy(android.app.INotificationManager target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.app.INotificationManager
            public boolean areNotificationsEnabledForPackage(String pkg, int uid) throws RemoteException {
                return this.mTarget.areNotificationsEnabledForPackage(pkg, uid);
            }

            @Override // com.oplus.wrapper.app.INotificationManager
            public StatusBarNotification[] getActiveNotifications(String callingPkg) throws RemoteException {
                return this.mTarget.getActiveNotifications(callingPkg);
            }

            @Override // com.oplus.wrapper.app.INotificationManager
            public ParceledListSlice getNotificationChannelGroups(String pkg) throws RemoteException {
                android.content.pm.ParceledListSlice parceledListSlice = this.mTarget.getNotificationChannelGroups(pkg);
                if (parceledListSlice == null) {
                    return null;
                }
                return new ParceledListSlice(parceledListSlice);
            }

            @Override // com.oplus.wrapper.app.INotificationManager
            public void createNotificationChannelGroups(String pkg, ParceledListSlice channelGroupList) throws RemoteException {
                if (channelGroupList == null) {
                    return;
                }
                this.mTarget.createNotificationChannelGroups(pkg, channelGroupList.getParceledListSlice());
            }

            @Override // com.oplus.wrapper.app.INotificationManager
            public void cancelAllNotifications(String pkg, int userId) throws RemoteException {
                this.mTarget.cancelAllNotifications(pkg, userId);
            }

            @Override // com.oplus.wrapper.app.INotificationManager
            public void updateNotificationChannelForPackage(String pkg, int uid, NotificationChannel channel) throws RemoteException {
                this.mTarget.updateNotificationChannelForPackage(pkg, uid, channel);
            }
        }
    }
}
