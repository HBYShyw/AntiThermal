package com.android.server.media.metrics;

import android.content.Context;
import android.media.MediaMetrics;
import android.media.metrics.IMediaMetricsManager;
import android.media.metrics.NetworkEvent;
import android.media.metrics.PlaybackErrorEvent;
import android.media.metrics.PlaybackMetrics;
import android.media.metrics.PlaybackStateEvent;
import android.media.metrics.TrackChangeEvent;
import android.os.Binder;
import android.os.PersistableBundle;
import android.provider.DeviceConfig;
import android.util.Base64;
import android.util.Slog;
import android.util.StatsEvent;
import android.util.StatsLog;
import com.android.internal.annotations.GuardedBy;
import com.android.server.SystemService;
import com.android.server.vr.Vr2dDisplay;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class MediaMetricsManagerService extends SystemService {
    private static final String FAILED_TO_GET = "failed_to_get";
    private static final int LOGGING_LEVEL_BLOCKED = 99999;
    private static final int LOGGING_LEVEL_EVERYTHING = 0;
    private static final int LOGGING_LEVEL_NO_UID = 1000;
    private static final String MEDIA_METRICS_MODE = "media_metrics_mode";
    private static final int MEDIA_METRICS_MODE_ALLOWLIST = 3;
    private static final int MEDIA_METRICS_MODE_BLOCKLIST = 2;
    private static final int MEDIA_METRICS_MODE_OFF = 0;
    private static final int MEDIA_METRICS_MODE_ON = 1;
    private static final String PLAYER_METRICS_APP_ALLOWLIST = "player_metrics_app_allowlist";
    private static final String PLAYER_METRICS_APP_BLOCKLIST = "player_metrics_app_blocklist";
    private static final String PLAYER_METRICS_PER_APP_ATTRIBUTION_ALLOWLIST = "player_metrics_per_app_attribution_allowlist";
    private static final String PLAYER_METRICS_PER_APP_ATTRIBUTION_BLOCKLIST = "player_metrics_per_app_attribution_blocklist";
    private static final String TAG = "MediaMetricsManagerService";
    private static final String mMetricsId = "metrics.manager";

    @GuardedBy({"mLock"})
    private List<String> mAllowlist;

    @GuardedBy({"mLock"})
    private List<String> mBlockList;
    private final Context mContext;
    private final Object mLock;

    @GuardedBy({"mLock"})
    private Integer mMode;

    @GuardedBy({"mLock"})
    private List<String> mNoUidAllowlist;

    @GuardedBy({"mLock"})
    private List<String> mNoUidBlocklist;
    private final SecureRandom mSecureRandom;

    public MediaMetricsManagerService(Context context) {
        super(context);
        this.mMode = null;
        this.mAllowlist = null;
        this.mNoUidAllowlist = null;
        this.mBlockList = null;
        this.mNoUidBlocklist = null;
        this.mLock = new Object();
        this.mContext = context;
        this.mSecureRandom = new SecureRandom();
    }

    public void onStart() {
        publishBinderService("media_metrics", new BinderService());
        DeviceConfig.addOnPropertiesChangedListener("media", this.mContext.getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.media.metrics.MediaMetricsManagerService$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                MediaMetricsManagerService.this.updateConfigs(properties);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfigs(DeviceConfig.Properties properties) {
        synchronized (this.mLock) {
            this.mMode = Integer.valueOf(properties.getInt(MEDIA_METRICS_MODE, 2));
            List<String> listLocked = getListLocked(PLAYER_METRICS_APP_ALLOWLIST);
            if (listLocked != null || this.mMode.intValue() != 3) {
                this.mAllowlist = listLocked;
            }
            List<String> listLocked2 = getListLocked(PLAYER_METRICS_PER_APP_ATTRIBUTION_ALLOWLIST);
            if (listLocked2 != null || this.mMode.intValue() != 3) {
                this.mNoUidAllowlist = listLocked2;
            }
            List<String> listLocked3 = getListLocked(PLAYER_METRICS_APP_BLOCKLIST);
            if (listLocked3 != null || this.mMode.intValue() != 2) {
                this.mBlockList = listLocked3;
            }
            List<String> listLocked4 = getListLocked(PLAYER_METRICS_PER_APP_ATTRIBUTION_BLOCKLIST);
            if (listLocked4 != null || this.mMode.intValue() != 2) {
                this.mNoUidBlocklist = listLocked4;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public List<String> getListLocked(String str) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String string = DeviceConfig.getString("media", str, FAILED_TO_GET);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            if (string.equals(FAILED_TO_GET)) {
                Slog.d(TAG, "failed to get " + str + " from DeviceConfig");
                return null;
            }
            return Arrays.asList(string.split(","));
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class BinderService extends IMediaMetricsManager.Stub {
        private BinderService() {
        }

        public void reportPlaybackMetrics(String str, PlaybackMetrics playbackMetrics, int i) {
            int loggingLevel = loggingLevel();
            if (loggingLevel == MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED) {
                return;
            }
            StatsLog.write(StatsEvent.newBuilder().setAtomId(Vr2dDisplay.DEFAULT_VIRTUAL_DISPLAY_DPI).writeInt(loggingLevel == 0 ? Binder.getCallingUid() : 0).writeString(str).writeLong(playbackMetrics.getMediaDurationMillis()).writeInt(playbackMetrics.getStreamSource()).writeInt(playbackMetrics.getStreamType()).writeInt(playbackMetrics.getPlaybackType()).writeInt(playbackMetrics.getDrmType()).writeInt(playbackMetrics.getContentType()).writeString(playbackMetrics.getPlayerName()).writeString(playbackMetrics.getPlayerVersion()).writeByteArray(new byte[0]).writeInt(playbackMetrics.getVideoFramesPlayed()).writeInt(playbackMetrics.getVideoFramesDropped()).writeInt(playbackMetrics.getAudioUnderrunCount()).writeLong(playbackMetrics.getNetworkBytesRead()).writeLong(playbackMetrics.getLocalBytesRead()).writeLong(playbackMetrics.getNetworkTransferDurationMillis()).writeString(Base64.encodeToString(playbackMetrics.getDrmSessionId(), 0)).usePooledBuffer().build());
        }

        public void reportBundleMetrics(String str, PersistableBundle persistableBundle, int i) {
            if (loggingLevel() != MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED && persistableBundle.getInt("bundlesession-statsd-atom") == 322) {
                String string = persistableBundle.getString("playbackstateevent-sessionid");
                int i2 = persistableBundle.getInt("playbackstateevent-state", -1);
                long j = persistableBundle.getLong("playbackstateevent-lifetime", -1L);
                if (string == null || i2 < 0 || j < 0) {
                    Slog.d(MediaMetricsManagerService.TAG, "dropping incomplete data for atom 322: _sessionId: " + string + " _state: " + i2 + " _lifetime: " + j);
                    return;
                }
                StatsLog.write(StatsEvent.newBuilder().setAtomId(322).writeString(string).writeInt(i2).writeLong(j).usePooledBuffer().build());
            }
        }

        public void reportPlaybackStateEvent(String str, PlaybackStateEvent playbackStateEvent, int i) {
            if (loggingLevel() == MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED) {
                return;
            }
            StatsLog.write(StatsEvent.newBuilder().setAtomId(322).writeString(str).writeInt(playbackStateEvent.getState()).writeLong(playbackStateEvent.getTimeSinceCreatedMillis()).usePooledBuffer().build());
        }

        private String getSessionIdInternal(int i) {
            byte[] bArr = new byte[12];
            MediaMetricsManagerService.this.mSecureRandom.nextBytes(bArr);
            String encodeToString = Base64.encodeToString(bArr, 11);
            new MediaMetrics.Item(MediaMetricsManagerService.mMetricsId).set(MediaMetrics.Property.EVENT, "create").set(MediaMetrics.Property.LOG_SESSION_ID, encodeToString).record();
            return encodeToString;
        }

        public void releaseSessionId(String str, int i) {
            Slog.v(MediaMetricsManagerService.TAG, "Releasing sessionId " + str + " for userId " + i + " [NOP]");
        }

        public String getPlaybackSessionId(int i) {
            return getSessionIdInternal(i);
        }

        public String getRecordingSessionId(int i) {
            return getSessionIdInternal(i);
        }

        public String getTranscodingSessionId(int i) {
            return getSessionIdInternal(i);
        }

        public String getEditingSessionId(int i) {
            return getSessionIdInternal(i);
        }

        public String getBundleSessionId(int i) {
            return getSessionIdInternal(i);
        }

        public void reportPlaybackErrorEvent(String str, PlaybackErrorEvent playbackErrorEvent, int i) {
            if (loggingLevel() == MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED) {
                return;
            }
            StatsLog.write(StatsEvent.newBuilder().setAtomId(323).writeString(str).writeString(playbackErrorEvent.getExceptionStack()).writeInt(playbackErrorEvent.getErrorCode()).writeInt(playbackErrorEvent.getSubErrorCode()).writeLong(playbackErrorEvent.getTimeSinceCreatedMillis()).usePooledBuffer().build());
        }

        public void reportNetworkEvent(String str, NetworkEvent networkEvent, int i) {
            if (loggingLevel() == MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED) {
                return;
            }
            StatsLog.write(StatsEvent.newBuilder().setAtomId(321).writeString(str).writeInt(networkEvent.getNetworkType()).writeLong(networkEvent.getTimeSinceCreatedMillis()).usePooledBuffer().build());
        }

        public void reportTrackChangeEvent(String str, TrackChangeEvent trackChangeEvent, int i) {
            if (loggingLevel() == MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED) {
                return;
            }
            StatsLog.write(StatsEvent.newBuilder().setAtomId(324).writeString(str).writeInt(trackChangeEvent.getTrackState()).writeInt(trackChangeEvent.getTrackChangeReason()).writeString(trackChangeEvent.getContainerMimeType()).writeString(trackChangeEvent.getSampleMimeType()).writeString(trackChangeEvent.getCodecName()).writeInt(trackChangeEvent.getBitrate()).writeLong(trackChangeEvent.getTimeSinceCreatedMillis()).writeInt(trackChangeEvent.getTrackType()).writeString(trackChangeEvent.getLanguage()).writeString(trackChangeEvent.getLanguageRegion()).writeInt(trackChangeEvent.getChannelCount()).writeInt(trackChangeEvent.getAudioSampleRate()).writeInt(trackChangeEvent.getWidth()).writeInt(trackChangeEvent.getHeight()).writeFloat(trackChangeEvent.getVideoFrameRate()).usePooledBuffer().build());
        }

        private int loggingLevel() {
            synchronized (MediaMetricsManagerService.this.mLock) {
                int callingUid = Binder.getCallingUid();
                if (MediaMetricsManagerService.this.mMode == null) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        MediaMetricsManagerService.this.mMode = Integer.valueOf(DeviceConfig.getInt("media", MediaMetricsManagerService.MEDIA_METRICS_MODE, 2));
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    } catch (Throwable th) {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        throw th;
                    }
                }
                if (MediaMetricsManagerService.this.mMode.intValue() == 1) {
                    return 0;
                }
                int intValue = MediaMetricsManagerService.this.mMode.intValue();
                int i = MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                if (intValue == 0) {
                    Slog.v(MediaMetricsManagerService.TAG, "Logging level blocked: MEDIA_METRICS_MODE_OFF");
                    return MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                }
                String[] packagesForUid = MediaMetricsManagerService.this.getContext().getPackageManager().getPackagesForUid(callingUid);
                if (packagesForUid != null && packagesForUid.length != 0) {
                    if (MediaMetricsManagerService.this.mMode.intValue() == 2) {
                        if (MediaMetricsManagerService.this.mBlockList == null) {
                            MediaMetricsManagerService mediaMetricsManagerService = MediaMetricsManagerService.this;
                            mediaMetricsManagerService.mBlockList = mediaMetricsManagerService.getListLocked(MediaMetricsManagerService.PLAYER_METRICS_APP_BLOCKLIST);
                            if (MediaMetricsManagerService.this.mBlockList == null) {
                                Slog.v(MediaMetricsManagerService.TAG, "Logging level blocked: Failed to get PLAYER_METRICS_APP_BLOCKLIST.");
                                return MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                            }
                        }
                        Integer loggingLevelInternal = loggingLevelInternal(packagesForUid, MediaMetricsManagerService.this.mBlockList, MediaMetricsManagerService.PLAYER_METRICS_APP_BLOCKLIST);
                        if (loggingLevelInternal != null) {
                            return loggingLevelInternal.intValue();
                        }
                        if (MediaMetricsManagerService.this.mNoUidBlocklist == null) {
                            MediaMetricsManagerService mediaMetricsManagerService2 = MediaMetricsManagerService.this;
                            mediaMetricsManagerService2.mNoUidBlocklist = mediaMetricsManagerService2.getListLocked(MediaMetricsManagerService.PLAYER_METRICS_PER_APP_ATTRIBUTION_BLOCKLIST);
                            if (MediaMetricsManagerService.this.mNoUidBlocklist == null) {
                                Slog.v(MediaMetricsManagerService.TAG, "Logging level blocked: Failed to get PLAYER_METRICS_PER_APP_ATTRIBUTION_BLOCKLIST.");
                                return MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                            }
                        }
                        Integer loggingLevelInternal2 = loggingLevelInternal(packagesForUid, MediaMetricsManagerService.this.mNoUidBlocklist, MediaMetricsManagerService.PLAYER_METRICS_PER_APP_ATTRIBUTION_BLOCKLIST);
                        if (loggingLevelInternal2 == null) {
                            return 0;
                        }
                        return loggingLevelInternal2.intValue();
                    }
                    if (MediaMetricsManagerService.this.mMode.intValue() == 3) {
                        if (MediaMetricsManagerService.this.mNoUidAllowlist == null) {
                            MediaMetricsManagerService mediaMetricsManagerService3 = MediaMetricsManagerService.this;
                            mediaMetricsManagerService3.mNoUidAllowlist = mediaMetricsManagerService3.getListLocked(MediaMetricsManagerService.PLAYER_METRICS_PER_APP_ATTRIBUTION_ALLOWLIST);
                            if (MediaMetricsManagerService.this.mNoUidAllowlist == null) {
                                Slog.v(MediaMetricsManagerService.TAG, "Logging level blocked: Failed to get PLAYER_METRICS_PER_APP_ATTRIBUTION_ALLOWLIST.");
                                return MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                            }
                        }
                        Integer loggingLevelInternal3 = loggingLevelInternal(packagesForUid, MediaMetricsManagerService.this.mNoUidAllowlist, MediaMetricsManagerService.PLAYER_METRICS_PER_APP_ATTRIBUTION_ALLOWLIST);
                        if (loggingLevelInternal3 != null) {
                            return loggingLevelInternal3.intValue();
                        }
                        if (MediaMetricsManagerService.this.mAllowlist == null) {
                            MediaMetricsManagerService mediaMetricsManagerService4 = MediaMetricsManagerService.this;
                            mediaMetricsManagerService4.mAllowlist = mediaMetricsManagerService4.getListLocked(MediaMetricsManagerService.PLAYER_METRICS_APP_ALLOWLIST);
                            if (MediaMetricsManagerService.this.mAllowlist == null) {
                                Slog.v(MediaMetricsManagerService.TAG, "Logging level blocked: Failed to get PLAYER_METRICS_APP_ALLOWLIST.");
                                return MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                            }
                        }
                        Integer loggingLevelInternal4 = loggingLevelInternal(packagesForUid, MediaMetricsManagerService.this.mAllowlist, MediaMetricsManagerService.PLAYER_METRICS_APP_ALLOWLIST);
                        if (loggingLevelInternal4 != null) {
                            return loggingLevelInternal4.intValue();
                        }
                        Slog.v(MediaMetricsManagerService.TAG, "Logging level blocked: Not detected in any allowlist.");
                        return MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                    }
                    Slog.v(MediaMetricsManagerService.TAG, "Logging level blocked: Blocked by default.");
                    return MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                }
                Slog.d(MediaMetricsManagerService.TAG, "empty package from uid " + callingUid);
                if (MediaMetricsManagerService.this.mMode.intValue() == 2) {
                    i = 1000;
                }
                return i;
            }
        }

        private Integer loggingLevelInternal(String[] strArr, List<String> list, String str) {
            if (inList(strArr, list)) {
                return Integer.valueOf(listNameToLoggingLevel(str));
            }
            return null;
        }

        private boolean inList(String[] strArr, List<String> list) {
            for (String str : strArr) {
                Iterator<String> it = list.iterator();
                while (it.hasNext()) {
                    if (str.equals(it.next())) {
                        return true;
                    }
                }
            }
            return false;
        }

        private int listNameToLoggingLevel(String str) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1894232751:
                    if (str.equals(MediaMetricsManagerService.PLAYER_METRICS_PER_APP_ATTRIBUTION_BLOCKLIST)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1289480849:
                    if (str.equals(MediaMetricsManagerService.PLAYER_METRICS_APP_ALLOWLIST)) {
                        c = 1;
                        break;
                    }
                    break;
                case 1900310029:
                    if (str.equals(MediaMetricsManagerService.PLAYER_METRICS_PER_APP_ATTRIBUTION_ALLOWLIST)) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 2:
                    return 1000;
                case 1:
                    return 0;
                default:
                    return MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
            }
        }
    }
}
