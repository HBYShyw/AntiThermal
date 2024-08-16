package com.android.server.notification;

import android.app.NotificationHistory;
import android.text.TextUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class NotificationHistoryFilter {
    private String mChannel;
    private int mNotificationCount;
    private String mPackage;

    private NotificationHistoryFilter() {
    }

    public String getPackage() {
        return this.mPackage;
    }

    public String getChannel() {
        return this.mChannel;
    }

    public int getMaxNotifications() {
        return this.mNotificationCount;
    }

    public boolean isFiltering() {
        return (getPackage() == null && getChannel() == null && this.mNotificationCount >= Integer.MAX_VALUE) ? false : true;
    }

    public boolean matchesPackageAndChannelFilter(NotificationHistory.HistoricalNotification historicalNotification) {
        if (TextUtils.isEmpty(getPackage())) {
            return true;
        }
        if (getPackage().equals(historicalNotification.getPackage())) {
            return TextUtils.isEmpty(getChannel()) || getChannel().equals(historicalNotification.getChannelId());
        }
        return false;
    }

    public boolean matchesCountFilter(NotificationHistory notificationHistory) {
        return notificationHistory.getHistoryCount() < this.mNotificationCount;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Builder {
        private String mPackage = null;
        private String mChannel = null;
        private int mNotificationCount = Integer.MAX_VALUE;

        public Builder setPackage(String str) {
            this.mPackage = str;
            return this;
        }

        public Builder setChannel(String str, String str2) {
            setPackage(str);
            this.mChannel = str2;
            return this;
        }

        public Builder setMaxNotifications(int i) {
            this.mNotificationCount = i;
            return this;
        }

        public NotificationHistoryFilter build() {
            NotificationHistoryFilter notificationHistoryFilter = new NotificationHistoryFilter();
            notificationHistoryFilter.mPackage = this.mPackage;
            notificationHistoryFilter.mChannel = this.mChannel;
            notificationHistoryFilter.mNotificationCount = this.mNotificationCount;
            return notificationHistoryFilter;
        }
    }
}
