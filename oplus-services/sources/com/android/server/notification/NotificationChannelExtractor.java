package com.android.server.notification;

import android.app.NotificationChannel;
import android.content.Context;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NotificationChannelExtractor implements NotificationSignalExtractor {
    private static final boolean DBG = false;
    private static final String TAG = "ChannelExtractor";
    private RankingConfig mConfig;
    private Context mContext;
    private IOplusNotificationChannelExtractorExt mOplusExtractorExt = (IOplusNotificationChannelExtractorExt) ExtLoader.type(IOplusNotificationChannelExtractorExt.class).base(this).create();

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setZenHelper(ZenModeHelper zenModeHelper) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void initialize(Context context, NotificationUsageStats notificationUsageStats) {
        this.mContext = context;
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public RankingReconsideration process(NotificationRecord notificationRecord) {
        RankingConfig rankingConfig;
        if (notificationRecord == null || notificationRecord.getNotification() == null || (rankingConfig = this.mConfig) == null) {
            return null;
        }
        NotificationChannel conversationNotificationChannel = rankingConfig.getConversationNotificationChannel(notificationRecord.getSbn().getPackageName(), notificationRecord.getSbn().getUid(), notificationRecord.getChannel().getId(), notificationRecord.getSbn().getShortcutId(), true, false);
        this.mOplusExtractorExt.updateNotificationChannel(notificationRecord, this.mConfig, conversationNotificationChannel);
        notificationRecord.updateNotificationChannel(conversationNotificationChannel);
        return null;
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setConfig(RankingConfig rankingConfig) {
        this.mConfig = rankingConfig;
    }
}
