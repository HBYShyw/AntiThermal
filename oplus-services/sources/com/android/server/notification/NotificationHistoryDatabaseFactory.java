package com.android.server.notification;

import android.content.Context;
import android.os.Handler;
import java.io.File;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NotificationHistoryDatabaseFactory {
    private static NotificationHistoryDatabase sTestingNotificationHistoryDb;

    public static void setTestingNotificationHistoryDatabase(NotificationHistoryDatabase notificationHistoryDatabase) {
        sTestingNotificationHistoryDb = notificationHistoryDatabase;
    }

    public static NotificationHistoryDatabase create(Context context, Handler handler, File file) {
        NotificationHistoryDatabase notificationHistoryDatabase = sTestingNotificationHistoryDb;
        return notificationHistoryDatabase != null ? notificationHistoryDatabase : new NotificationHistoryDatabase(handler, file);
    }
}
