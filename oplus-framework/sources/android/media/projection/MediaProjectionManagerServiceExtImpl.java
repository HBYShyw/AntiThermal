package android.media.projection;

import android.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.oplus.Telephony;
import android.util.Slog;

/* loaded from: classes.dex */
public class MediaProjectionManagerServiceExtImpl implements IMediaProjectionManagerServiceExt {
    private static final String ACTION = "android.settings.BACK_STAGE_MEDIA_DIALOG";
    private static final String CLS_NAME = "com.oplus.systemui.media.BackStageMediaProjectionActivity";
    private static final String DOMESTIC = "domestic";
    private static final String MEDIA_PROJECTION_CHANNEL_ID = "mediaprojection.notification.channel";
    private static final String MEDIA_PROJECTION_CHANNEL_NAME = "mediaprojection";
    private static final int MEDIA_PROJECTION_NOTIFY_ID = 1;
    private static final String OPLUS_AUDIO_MONITOR = "com.oplus.audiomonitor";
    private static final String OPLUS_CAST = "com.oplus.cast";
    private static final String OPLUS_LOG = "com.oplus.olc";
    private static final String OPLUS_SCREEN_RECORDER = "com.oplus.screenrecorder";
    private static final String OPLUS_TRANSLATE = "com.coloros.translate.engine";
    private static final String PKG_NAME = "com.android.systemui";
    private static final String SYSTEM_EXT_AREA = "ro.oplus.image.system_ext.area";
    private static final String TAG = "MediaProjectionManagerServiceExtImpl";
    private static MediaProjectionManagerServiceExtImpl sInstance;
    private String mCallingPkn;
    private UserHandle mCallingUser;
    private NotificationManager mNotificationManager;
    private PackageManager mPackageManager;
    private boolean mShowed = false;

    public static MediaProjectionManagerServiceExtImpl getInstance(Object obj) {
        MediaProjectionManagerServiceExtImpl mediaProjectionManagerServiceExtImpl;
        synchronized (MediaProjectionManagerServiceExtImpl.class) {
            if (sInstance == null) {
                sInstance = new MediaProjectionManagerServiceExtImpl();
            }
            mediaProjectionManagerServiceExtImpl = sInstance;
        }
        return mediaProjectionManagerServiceExtImpl;
    }

    public void start(Context context, String packageName, UserHandle callingUser) {
        Slog.i(TAG, "MediaProjection ext start packageName:" + packageName);
        if (isOplusApp(packageName)) {
            return;
        }
        this.mCallingPkn = packageName;
        this.mCallingUser = callingUser;
        if (isDomestic()) {
            Slog.i(TAG, " Domestic showNotifiCation packageName:" + packageName);
            showNotifiCation(context, packageName, callingUser);
        }
    }

    private boolean isOplusApp(String packageName) {
        if (packageName.equals(OPLUS_SCREEN_RECORDER) || packageName.equals(OPLUS_CAST) || packageName.equals(OPLUS_LOG) || packageName.equals(OPLUS_TRANSLATE)) {
            return true;
        }
        return packageName.equals(OPLUS_AUDIO_MONITOR) && SystemProperties.getBoolean("ro.oplus.audio.voip_record_white_app_support", false);
    }

    public void stop(Context context) {
        Slog.i(TAG, " MediaProjection ext stop");
        if (isDomestic() && this.mShowed) {
            cancelNotification(context);
        }
    }

    private boolean isDomestic() {
        String area = SystemProperties.get(SYSTEM_EXT_AREA, DOMESTIC);
        return DOMESTIC.equals(area);
    }

    private void createNotificationChannel(String channelId, String channelName, int importance, Context context) {
        if (this.mNotificationManager == null) {
            this.mNotificationManager = (NotificationManager) context.getSystemService(Telephony.ThreadsColumns.NOTIFICATION);
        }
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        this.mNotificationManager.createNotificationChannel(notificationChannel);
    }

    private void showNotifiCation(Context context, String packageName, UserHandle callingUser) {
        if (this.mNotificationManager == null) {
            this.mNotificationManager = (NotificationManager) context.getSystemService(Telephony.ThreadsColumns.NOTIFICATION);
        }
        if (this.mPackageManager == null) {
            this.mPackageManager = context.getPackageManager();
        }
        long callingId = Binder.clearCallingIdentity();
        try {
            try {
                try {
                    if (!this.mShowed) {
                        Slog.d(TAG, "createNotificationChannel");
                        createNotificationChannel(MEDIA_PROJECTION_CHANNEL_ID, MEDIA_PROJECTION_CHANNEL_NAME, 2, context);
                    }
                    this.mShowed = true;
                    ApplicationInfo ai = this.mPackageManager.getApplicationInfoAsUser(packageName, PackageManager.ApplicationInfoFlags.of(0L), callingUser);
                    createNotification(context, packageName, callingUser, ai);
                } catch (Exception e) {
                    Slog.e(TAG, "showNotifiCation Exception: " + e.getLocalizedMessage());
                }
            } catch (PackageManager.NameNotFoundException e2) {
                Slog.e(TAG, "showNotifiCation NameNotFoundException");
            }
        } finally {
            Binder.restoreCallingIdentity(callingId);
        }
    }

    private void createNotification(Context context, String pkgname, UserHandle callingUser, ApplicationInfo ai) {
        Notification.Builder builder = new Notification.Builder(context, MEDIA_PROJECTION_CHANNEL_ID);
        Intent intent = new Intent();
        intent.setClassName("com.android.systemui", CLS_NAME);
        intent.setPackage("com.android.systemui");
        intent.setAction(ACTION);
        Context pendContext = context.createContextAsUser(callingUser, 0);
        PendingIntent pendingIntentClick = PendingIntent.getActivity(pendContext, 0, intent, 201326592);
        builder.setContentIntent(pendingIntentClick);
        String title = context.getString(R.string.number_picker_increment_button);
        builder.setContentTitle(title);
        builder.setContentText(context.getString(R.string.number_picker_increment_scroll_action));
        builder.setSmallIcon(Icon.createWithResource(pkgname, ai.icon));
        builder.setWhen(System.currentTimeMillis());
        builder.setOngoing(true);
        Bundle extras = new Bundle();
        extras.putString("android.substName", ai.loadLabel(this.mPackageManager).toString());
        builder.addExtras(extras);
        Notification notification = builder.build();
        notification.flags += 2;
        Slog.i(TAG, "showNotifiCation notifyAsUser");
        this.mNotificationManager.notifyAsUser("", 1, notification, callingUser);
    }

    public void cancelNotification(Context context) {
        long callingId = Binder.clearCallingIdentity();
        try {
            try {
                if (this.mNotificationManager == null) {
                    this.mNotificationManager = (NotificationManager) context.getSystemService(Telephony.ThreadsColumns.NOTIFICATION);
                }
                this.mNotificationManager.cancelAsUser("", 1, this.mCallingUser);
            } catch (Exception e) {
                Slog.e(TAG, "cancelCastNotification catch exception:" + e.getLocalizedMessage());
            }
        } finally {
            Binder.restoreCallingIdentity(callingId);
        }
    }
}
