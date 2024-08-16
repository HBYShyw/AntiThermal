package com.android.server.bluetooth;

import android.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BluetoothNotificationManager {
    public static final String APM_NOTIFICATION_CHANNEL = "apm_notification_channel";
    private static final String APM_NOTIFICATION_GROUP = "apm_notification_group";
    private static final String HELP_PAGE_URL = "https://support.google.com/pixelphone/answer/12639358";
    private static final String NOTIFICATION_TAG = "com.android.bluetooth";
    private static final String TAG = "BluetoothNotificationManager";
    private final Context mContext;
    private boolean mInitialized = false;
    private NotificationManager mNotificationManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothNotificationManager(Context context) {
        this.mContext = context;
    }

    private NotificationManager getNotificationManagerForCurrentUser() {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                Context context = this.mContext;
                return (NotificationManager) context.createPackageContextAsUser(context.getPackageName(), 0, UserHandle.CURRENT).getSystemService(NotificationManager.class);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Failed to get NotificationManager for current user: " + e.getMessage());
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return null;
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void createNotificationChannels() {
        if (this.mNotificationManager != null) {
            cleanAllBtNotification();
        }
        NotificationManager notificationManagerForCurrentUser = getNotificationManagerForCurrentUser();
        this.mNotificationManager = notificationManagerForCurrentUser;
        if (notificationManagerForCurrentUser == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new NotificationChannel(APM_NOTIFICATION_CHANNEL, APM_NOTIFICATION_GROUP, 4));
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                this.mNotificationManager.createNotificationChannels(arrayList);
            } catch (Exception e) {
                Log.e(TAG, "Error Message: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void cleanAllBtNotification() {
        for (StatusBarNotification statusBarNotification : getActiveNotifications()) {
            if (NOTIFICATION_TAG.equals(statusBarNotification.getTag())) {
                cancel(statusBarNotification.getId());
            }
        }
    }

    public void notify(int i, Notification notification) {
        if (!this.mInitialized) {
            createNotificationChannels();
            this.mInitialized = true;
        }
        NotificationManager notificationManager = this.mNotificationManager;
        if (notificationManager == null) {
            return;
        }
        notificationManager.notify(NOTIFICATION_TAG, i, notification);
    }

    public void sendApmNotification(String str, String str2) {
        if (!this.mInitialized) {
            createNotificationChannels();
            this.mInitialized = true;
        }
        notify(74, new Notification.Builder(this.mContext, APM_NOTIFICATION_CHANNEL).setAutoCancel(true).setLocalOnly(true).setContentTitle(str).setContentText(str2).setContentIntent(PendingIntent.getActivity(this.mContext.createContextAsUser(UserHandle.CURRENT, 0), AudioFormat.OPUS, new Intent("android.intent.action.VIEW").setData(Uri.parse(HELP_PAGE_URL)).addFlags(AudioFormat.EVRC), 67108864)).setVisibility(1).setStyle(new Notification.BigTextStyle().bigText(str2)).setSmallIcon(R.drawable.stat_sys_data_bluetooth).build());
    }

    public void cancel(int i) {
        NotificationManager notificationManager = this.mNotificationManager;
        if (notificationManager == null) {
            return;
        }
        notificationManager.cancel(NOTIFICATION_TAG, i);
    }

    public StatusBarNotification[] getActiveNotifications() {
        NotificationManager notificationManager = this.mNotificationManager;
        return notificationManager == null ? new StatusBarNotification[0] : notificationManager.getActiveNotifications();
    }
}
