package com.android.server.media;

import android.content.Context;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import com.android.server.job.controllers.JobStatus;
import java.io.PrintWriter;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class MediaSessionDeviceConfig {
    private static final long DEFAULT_MEDIA_BUTTON_RECEIVER_FGS_ALLOWLIST_DURATION_MS = 10000;
    private static final long DEFAULT_MEDIA_SESSION_CALLBACK_FGS_ALLOWLIST_DURATION_MS = 10000;
    private static final long DEFAULT_MEDIA_SESSION_CALLBACK_FGS_WHILE_IN_USE_TEMP_ALLOW_DURATION_MS = 10000;
    private static final String KEY_MEDIA_BUTTON_RECEIVER_FGS_ALLOWLIST_DURATION_MS = "media_button_receiver_fgs_allowlist_duration_ms";
    private static final String KEY_MEDIA_SESSION_CALLBACK_FGS_ALLOWLIST_DURATION_MS = "media_session_calback_fgs_allowlist_duration_ms";
    private static final String KEY_MEDIA_SESSION_CALLBACK_FGS_WHILE_IN_USE_TEMP_ALLOW_DURATION_MS = "media_session_callback_fgs_while_in_use_temp_allow_duration_ms";
    private static volatile long sMediaButtonReceiverFgsAllowlistDurationMs = 10000;
    private static volatile long sMediaSessionCallbackFgsAllowlistDurationMs = 10000;
    private static volatile long sMediaSessionCallbackFgsWhileInUseTempAllowDurationMs = 10000;

    MediaSessionDeviceConfig() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void refresh(final DeviceConfig.Properties properties) {
        properties.getKeyset();
        properties.getKeyset().forEach(new Consumer() { // from class: com.android.server.media.MediaSessionDeviceConfig$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                MediaSessionDeviceConfig.lambda$refresh$0(properties, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$refresh$0(DeviceConfig.Properties properties, String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1976080914:
                if (str.equals(KEY_MEDIA_BUTTON_RECEIVER_FGS_ALLOWLIST_DURATION_MS)) {
                    c = 0;
                    break;
                }
                break;
            case -1060130895:
                if (str.equals(KEY_MEDIA_SESSION_CALLBACK_FGS_WHILE_IN_USE_TEMP_ALLOW_DURATION_MS)) {
                    c = 1;
                    break;
                }
                break;
            case 1803361950:
                if (str.equals(KEY_MEDIA_SESSION_CALLBACK_FGS_ALLOWLIST_DURATION_MS)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                sMediaButtonReceiverFgsAllowlistDurationMs = properties.getLong(str, JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY);
                return;
            case 1:
                sMediaSessionCallbackFgsWhileInUseTempAllowDurationMs = properties.getLong(str, JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY);
                return;
            case 2:
                sMediaSessionCallbackFgsAllowlistDurationMs = properties.getLong(str, JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY);
                return;
            default:
                return;
        }
    }

    public static void initialize(Context context) {
        DeviceConfig.addOnPropertiesChangedListener("media", context.getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.media.MediaSessionDeviceConfig$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                MediaSessionDeviceConfig.refresh(properties);
            }
        });
        refresh(DeviceConfig.getProperties("media", new String[0]));
    }

    public static long getMediaButtonReceiverFgsAllowlistDurationMs() {
        return sMediaButtonReceiverFgsAllowlistDurationMs;
    }

    public static long getMediaSessionCallbackFgsAllowlistDurationMs() {
        return sMediaSessionCallbackFgsAllowlistDurationMs;
    }

    public static long getMediaSessionCallbackFgsWhileInUseTempAllowDurationMs() {
        return sMediaSessionCallbackFgsWhileInUseTempAllowDurationMs;
    }

    public static void dump(PrintWriter printWriter, String str) {
        printWriter.println("Media session config:");
        String str2 = str + "  %s: [cur: %s, def: %s]";
        Long valueOf = Long.valueOf(sMediaButtonReceiverFgsAllowlistDurationMs);
        Long valueOf2 = Long.valueOf(JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY);
        printWriter.println(TextUtils.formatSimple(str2, new Object[]{KEY_MEDIA_BUTTON_RECEIVER_FGS_ALLOWLIST_DURATION_MS, valueOf, valueOf2}));
        printWriter.println(TextUtils.formatSimple(str2, new Object[]{KEY_MEDIA_SESSION_CALLBACK_FGS_ALLOWLIST_DURATION_MS, Long.valueOf(sMediaSessionCallbackFgsAllowlistDurationMs), valueOf2}));
        printWriter.println(TextUtils.formatSimple(str2, new Object[]{KEY_MEDIA_SESSION_CALLBACK_FGS_WHILE_IN_USE_TEMP_ALLOW_DURATION_MS, Long.valueOf(sMediaSessionCallbackFgsWhileInUseTempAllowDurationMs), valueOf2}));
    }
}
