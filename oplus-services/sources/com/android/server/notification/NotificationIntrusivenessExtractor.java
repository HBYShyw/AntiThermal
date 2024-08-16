package com.android.server.notification;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NotificationIntrusivenessExtractor implements NotificationSignalExtractor {

    @VisibleForTesting
    static final long HANG_TIME_MS = 10000;
    private static final String TAG = "IntrusivenessExtractor";
    private static final boolean DBG = Log.isLoggable(TAG, 3);

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setConfig(RankingConfig rankingConfig) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setZenHelper(ZenModeHelper zenModeHelper) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void initialize(Context context, NotificationUsageStats notificationUsageStats) {
        if (DBG) {
            Slog.d(TAG, "Initializing  " + getClass().getSimpleName() + ".");
        }
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public RankingReconsideration process(NotificationRecord notificationRecord) {
        if (notificationRecord == null || notificationRecord.getNotification() == null) {
            if (DBG) {
                Slog.d(TAG, "skipping empty notification");
            }
            return null;
        }
        long j = 10000;
        if (notificationRecord.getFreshnessMs(System.currentTimeMillis()) < 10000 && notificationRecord.getImportance() >= 3) {
            if (notificationRecord.getSound() != null && notificationRecord.getSound() != Uri.EMPTY) {
                notificationRecord.setRecentlyIntrusive(true);
            }
            if (notificationRecord.getVibration() != null) {
                notificationRecord.setRecentlyIntrusive(true);
            }
            if (notificationRecord.getNotification().fullScreenIntent != null) {
                notificationRecord.setRecentlyIntrusive(true);
            }
        }
        if (notificationRecord.isRecentlyIntrusive()) {
            return new RankingReconsideration(notificationRecord.getKey(), j) { // from class: com.android.server.notification.NotificationIntrusivenessExtractor.1
                @Override // com.android.server.notification.RankingReconsideration
                public void work() {
                }

                @Override // com.android.server.notification.RankingReconsideration
                public void applyChangesLocked(NotificationRecord notificationRecord2) {
                    if (System.currentTimeMillis() - notificationRecord2.getLastIntrusive() >= 10000) {
                        notificationRecord2.setRecentlyIntrusive(false);
                    }
                }
            };
        }
        return null;
    }
}
