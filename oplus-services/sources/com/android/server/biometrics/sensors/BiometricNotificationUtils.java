package com.android.server.biometrics.sensors;

import android.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BiometricNotificationUtils {
    private static final String BAD_CALIBRATION_NOTIFICATION_TAG = "FingerprintService";
    private static final String KEY_RE_ENROLL_FACE = "re_enroll_face_unlock";
    private static final int NOTIFICATION_ID = 1;
    private static final long NOTIFICATION_INTERVAL_MS = 86400000;
    private static final String RE_ENROLL_NOTIFICATION_TAG = "FaceService";
    private static final String TAG = "BiometricNotificationUtils";
    private static long sLastAlertTime;

    public static void showReEnrollmentNotification(Context context) {
        String string = context.getString(R.string.keyboardview_keycode_delete);
        String string2 = context.getString(R.string.keyboardview_keycode_done);
        String string3 = context.getString(R.string.keyboardview_keycode_cancel);
        Intent intent = new Intent("android.settings.FACE_SETTINGS");
        intent.setPackage("com.android.settings");
        intent.putExtra(KEY_RE_ENROLL_FACE, true);
        showNotificationHelper(context, string, string2, string3, PendingIntent.getActivityAsUser(context, 0, intent, 67108864, null, UserHandle.CURRENT), "FaceEnrollNotificationChannel", RE_ENROLL_NOTIFICATION_TAG);
    }

    public static void showBadCalibrationNotification(Context context) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = sLastAlertTime;
        long j2 = elapsedRealtime - j;
        if (j != 0 && j2 < 86400000) {
            Slog.v(TAG, "Skipping calibration notification : " + j2);
            return;
        }
        sLastAlertTime = elapsedRealtime;
        String string = context.getString(R.string.kg_pattern_instructions);
        String string2 = context.getString(R.string.kg_pin_instructions);
        String string3 = context.getString(R.string.kg_password_wrong_pin_code);
        Intent intent = new Intent("android.settings.FINGERPRINT_SETTINGS");
        intent.setPackage("com.android.settings");
        showNotificationHelper(context, string, string2, string3, PendingIntent.getActivityAsUser(context, 0, intent, 67108864, null, UserHandle.CURRENT), "FingerprintBadCalibrationNotificationChannel", BAD_CALIBRATION_NOTIFICATION_TAG);
    }

    private static void showNotificationHelper(Context context, String str, String str2, String str3, PendingIntent pendingIntent, String str4, String str5) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        NotificationChannel notificationChannel = new NotificationChannel(str4, str, 4);
        Notification build = new Notification.Builder(context, str4).setSmallIcon(R.drawable.ic_lockscreen_outerring).setContentTitle(str2).setContentText(str3).setSubText(str).setOnlyAlertOnce(true).setLocalOnly(true).setAutoCancel(true).setCategory("sys").setContentIntent(pendingIntent).setVisibility(-1).build();
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notifyAsUser(str5, 1, build, UserHandle.CURRENT);
    }

    public static void cancelReEnrollNotification(Context context) {
        ((NotificationManager) context.getSystemService(NotificationManager.class)).cancelAsUser(RE_ENROLL_NOTIFICATION_TAG, 1, UserHandle.CURRENT);
    }

    public static void cancelBadCalibrationNotification(Context context) {
        ((NotificationManager) context.getSystemService(NotificationManager.class)).cancelAsUser(BAD_CALIBRATION_NOTIFICATION_TAG, 1, UserHandle.CURRENT);
    }
}
