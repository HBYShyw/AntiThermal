package com.android.server.notification;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NotificationAdjustmentExtractor implements NotificationSignalExtractor {
    private static final boolean DBG = false;
    private static final String TAG = "AdjustmentExtractor";

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void initialize(Context context, NotificationUsageStats notificationUsageStats) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setConfig(RankingConfig rankingConfig) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setZenHelper(ZenModeHelper zenModeHelper) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public RankingReconsideration process(NotificationRecord notificationRecord) {
        if (notificationRecord != null && notificationRecord.getNotification() != null) {
            notificationRecord.applyAdjustments();
        }
        return null;
    }
}
