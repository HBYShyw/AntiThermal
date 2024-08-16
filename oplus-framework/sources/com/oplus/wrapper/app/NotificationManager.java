package com.oplus.wrapper.app;

/* loaded from: classes.dex */
public class NotificationManager {
    private final android.app.NotificationManager mNotificationManager;

    public NotificationManager(android.app.NotificationManager notificationManager) {
        this.mNotificationManager = notificationManager;
    }

    public int getZenMode() {
        return this.mNotificationManager.getZenMode();
    }
}
