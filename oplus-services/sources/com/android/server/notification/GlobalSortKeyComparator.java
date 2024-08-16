package com.android.server.notification;

import android.util.Slog;
import java.util.Comparator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GlobalSortKeyComparator implements Comparator<NotificationRecord> {
    private static final String TAG = "GlobalSortComp";

    @Override // java.util.Comparator
    public int compare(NotificationRecord notificationRecord, NotificationRecord notificationRecord2) {
        if (notificationRecord.getGlobalSortKey() == null) {
            Slog.wtf(TAG, "Missing left global sort key: " + notificationRecord);
            return 1;
        }
        if (notificationRecord2.getGlobalSortKey() == null) {
            Slog.wtf(TAG, "Missing right global sort key: " + notificationRecord2);
            return -1;
        }
        return notificationRecord.getGlobalSortKey().compareTo(notificationRecord2.getGlobalSortKey());
    }
}
