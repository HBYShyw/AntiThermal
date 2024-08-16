package com.android.server.wm;

import android.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import com.android.internal.util.ImageUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class AlertWindowNotification {
    private static final String CHANNEL_PREFIX = "com.android.server.wm.AlertWindowNotification - ";
    private static final int NOTIFICATION_ID = 0;
    private static NotificationChannelGroup sChannelGroup;
    private static int sNextRequestCode;
    private final NotificationManager mNotificationManager;
    private String mNotificationTag;
    private final String mPackageName;
    private boolean mPosted;
    private final int mRequestCode;
    private final WindowManagerService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AlertWindowNotification(WindowManagerService windowManagerService, String str) {
        this.mService = windowManagerService;
        this.mPackageName = str;
        this.mNotificationManager = (NotificationManager) windowManagerService.mContext.getSystemService("notification");
        this.mNotificationTag = CHANNEL_PREFIX + str;
        int i = sNextRequestCode;
        sNextRequestCode = i + 1;
        this.mRequestCode = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void post() {
        this.mService.mH.post(new Runnable() { // from class: com.android.server.wm.AlertWindowNotification$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AlertWindowNotification.this.onPostNotification();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancel(final boolean z) {
        this.mService.mH.post(new Runnable() { // from class: com.android.server.wm.AlertWindowNotification$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AlertWindowNotification.this.lambda$cancel$0(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onCancelNotification, reason: merged with bridge method [inline-methods] */
    public void lambda$cancel$0(boolean z) {
        if (this.mPosted) {
            this.mPosted = false;
            this.mNotificationManager.cancel(this.mNotificationTag, 0);
            if (z) {
                this.mNotificationManager.deleteNotificationChannel(this.mNotificationTag);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPostNotification() {
        if (this.mPosted) {
            return;
        }
        this.mPosted = true;
        Context context = this.mService.mContext;
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = getApplicationInfo(packageManager, this.mPackageName);
        String charSequence = applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo).toString() : this.mPackageName;
        createNotificationChannel(context, charSequence);
        String string = context.getString(R.string.autofill_address_line_2_re, charSequence);
        Bundle bundle = new Bundle();
        bundle.putStringArray("android.foregroundApps", new String[]{this.mPackageName});
        Notification.Builder contentIntent = new Notification.Builder(context, this.mNotificationTag).setOngoing(true).setContentTitle(context.getString(R.string.autofill_address_line_3_re, charSequence)).setContentText(string).setSmallIcon(R.drawable.android_logotype).setColor(context.getColor(R.color.system_notification_accent_color)).setStyle(new Notification.BigTextStyle().bigText(string)).setLocalOnly(true).addExtras(bundle).setContentIntent(getContentIntent(context, this.mPackageName));
        if (applicationInfo != null) {
            Drawable applicationIcon = packageManager.getApplicationIcon(applicationInfo);
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.app_icon_size);
            Bitmap buildScaledBitmap = ImageUtils.buildScaledBitmap(applicationIcon, dimensionPixelSize, dimensionPixelSize);
            if (buildScaledBitmap != null) {
                contentIntent.setLargeIcon(buildScaledBitmap);
            }
        }
        this.mNotificationManager.notify(this.mNotificationTag, 0, contentIntent.build());
    }

    private PendingIntent getContentIntent(Context context, String str) {
        Intent intent = new Intent("android.settings.MANAGE_APP_OVERLAY_PERMISSION", Uri.fromParts("package", str, null));
        intent.setFlags(268468224);
        return PendingIntent.getActivity(context, this.mRequestCode, intent, 335544320);
    }

    private void createNotificationChannel(Context context, String str) {
        if (sChannelGroup == null) {
            NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(CHANNEL_PREFIX, this.mService.mContext.getString(R.string.autofill_address_line_1_label_re));
            sChannelGroup = notificationChannelGroup;
            this.mNotificationManager.createNotificationChannelGroup(notificationChannelGroup);
        }
        String string = context.getString(R.string.autofill_address_line_1_re, str);
        if (this.mNotificationManager.getNotificationChannel(this.mNotificationTag) != null) {
            return;
        }
        NotificationChannel notificationChannel = new NotificationChannel(this.mNotificationTag, string, 1);
        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(false);
        notificationChannel.setBlockable(true);
        notificationChannel.setGroup(sChannelGroup.getId());
        notificationChannel.setBypassDnd(true);
        if (this.mNotificationManager.getNotificationChannelGroup(sChannelGroup.getId()) == null) {
            this.mNotificationManager.createNotificationChannelGroup(sChannelGroup);
        }
        this.mNotificationManager.createNotificationChannel(notificationChannel);
    }

    private ApplicationInfo getApplicationInfo(PackageManager packageManager, String str) {
        try {
            return packageManager.getApplicationInfo(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }
}
