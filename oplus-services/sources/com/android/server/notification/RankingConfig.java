package com.android.server.notification;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.pm.ParceledListSlice;
import android.os.UserHandle;
import java.util.Collection;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface RankingConfig {
    boolean badgingEnabled(UserHandle userHandle);

    boolean bubblesEnabled(UserHandle userHandle);

    boolean canShowBadge(String str, int i);

    boolean canShowNotificationsOnLockscreen(int i);

    boolean canShowPrivateNotificationsOnLockScreen(int i);

    boolean createNotificationChannel(String str, int i, NotificationChannel notificationChannel, boolean z, boolean z2, int i2, boolean z3);

    void createNotificationChannelGroup(String str, int i, NotificationChannelGroup notificationChannelGroup, boolean z, int i2, boolean z2);

    boolean deleteNotificationChannel(String str, int i, String str2, int i2, boolean z);

    int getBubblePreference(String str, int i);

    NotificationChannel getConversationNotificationChannel(String str, int i, String str2, String str3, boolean z, boolean z2);

    NotificationChannel getNotificationChannel(String str, int i, String str2, boolean z);

    ParceledListSlice<NotificationChannelGroup> getNotificationChannelGroups(String str, int i, boolean z, boolean z2, boolean z3);

    Collection<NotificationChannelGroup> getNotificationChannelGroups(String str, int i);

    ParceledListSlice<NotificationChannel> getNotificationChannels(String str, int i, boolean z);

    boolean isGroupBlocked(String str, int i, String str2);

    boolean isMediaNotificationFilteringEnabled();

    void permanentlyDeleteNotificationChannel(String str, int i, String str2);

    void permanentlyDeleteNotificationChannels(String str, int i);

    void setShowBadge(String str, int i, boolean z);

    void updateNotificationChannel(String str, int i, NotificationChannel notificationChannel, boolean z, int i2, boolean z2);
}
