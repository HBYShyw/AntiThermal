package com.android.server.notification;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class CriticalNotificationExtractor implements NotificationSignalExtractor {
    static final int CRITICAL = 0;
    static final int CRITICAL_LOW = 1;
    private static final boolean DBG = false;
    static final int NORMAL = 2;
    private static final String TAG = "CriticalNotificationExt";
    private boolean mSupportsCriticalNotifications = false;

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setConfig(RankingConfig rankingConfig) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setZenHelper(ZenModeHelper zenModeHelper) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void initialize(Context context, NotificationUsageStats notificationUsageStats) {
        this.mSupportsCriticalNotifications = supportsCriticalNotifications(context);
    }

    private boolean supportsCriticalNotifications(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.type.automotive", 0);
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public RankingReconsideration process(NotificationRecord notificationRecord) {
        if (this.mSupportsCriticalNotifications && notificationRecord != null && notificationRecord.getNotification() != null) {
            if (notificationRecord.isCategory("car_emergency")) {
                notificationRecord.setCriticality(0);
            } else if (notificationRecord.isCategory("car_warning")) {
                notificationRecord.setCriticality(1);
            } else {
                notificationRecord.setCriticality(2);
            }
        }
        return null;
    }
}
