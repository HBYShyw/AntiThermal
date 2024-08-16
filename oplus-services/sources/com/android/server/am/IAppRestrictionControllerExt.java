package com.android.server.am;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAppRestrictionControllerExt {
    public static final String ACTION_NOTIFICATION_DELETE = "com.oplus.action.APPRESTRICTION_NOTIFICATION_DELETE";

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface IStaticExt {
        default void cancelSummaryNotificationIfNecessary() {
        }

        default void decrementCount() {
        }

        default int getNotificationCounts() {
            return -1;
        }

        default void incrementCount() {
        }

        default void registerReceiverForDeleteNotification(Context context, Handler handler) {
        }

        default void setDeleteIntent(Notification.Builder builder, Context context) {
        }
    }

    default void dump(PrintWriter printWriter, String str) {
    }
}
