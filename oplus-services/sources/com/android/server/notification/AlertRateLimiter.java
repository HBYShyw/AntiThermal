package com.android.server.notification;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AlertRateLimiter {
    static final long ALLOWED_ALERT_INTERVAL = 1000;
    private long mLastNotificationMillis = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldRateLimitAlert(long j) {
        long j2 = j - this.mLastNotificationMillis;
        if (j2 < 0 || j2 < 1000) {
            return true;
        }
        this.mLastNotificationMillis = j;
        return false;
    }
}
