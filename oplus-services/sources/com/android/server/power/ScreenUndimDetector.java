package com.android.server.power;

import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.DeviceConfig;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ScreenUndimDetector {
    private static final boolean DEBUG = false;
    private static final boolean DEFAULT_KEEP_SCREEN_ON_ENABLED = false;

    @VisibleForTesting
    static final long DEFAULT_KEEP_SCREEN_ON_FOR_MILLIS;

    @VisibleForTesting
    static final long DEFAULT_MAX_DURATION_BETWEEN_UNDIMS_MILLIS;

    @VisibleForTesting
    static final int DEFAULT_UNDIMS_REQUIRED = 2;
    static final String KEY_KEEP_SCREEN_ON_ENABLED = "keep_screen_on_enabled";

    @VisibleForTesting
    static final String KEY_KEEP_SCREEN_ON_FOR_MILLIS = "keep_screen_on_for_millis";

    @VisibleForTesting
    static final String KEY_MAX_DURATION_BETWEEN_UNDIMS_MILLIS = "max_duration_between_undims_millis";

    @VisibleForTesting
    static final String KEY_UNDIMS_REQUIRED = "undims_required";
    private static final int OUTCOME_POWER_BUTTON = 1;
    private static final int OUTCOME_TIMEOUT = 2;
    private static final String TAG = "ScreenUndimDetector";
    private static final String UNDIM_DETECTOR_WAKE_LOCK = "UndimDetectorWakeLock";
    private InternalClock mClock;

    @VisibleForTesting
    int mCurrentScreenPolicy;
    private long mInteractionAfterUndimTime;
    private boolean mKeepScreenOnEnabled;
    private long mKeepScreenOnForMillis;
    private long mMaxDurationBetweenUndimsMillis;

    @VisibleForTesting
    int mUndimCounter;

    @VisibleForTesting
    long mUndimCounterStartedMillis;
    private long mUndimOccurredTime;
    private int mUndimsRequired;

    @VisibleForTesting
    PowerManager.WakeLock mWakeLock;

    static {
        TimeUnit timeUnit = TimeUnit.MINUTES;
        DEFAULT_KEEP_SCREEN_ON_FOR_MILLIS = timeUnit.toMillis(10L);
        DEFAULT_MAX_DURATION_BETWEEN_UNDIMS_MILLIS = timeUnit.toMillis(5L);
    }

    public ScreenUndimDetector() {
        this.mUndimCounter = 0;
        this.mUndimOccurredTime = -1L;
        this.mInteractionAfterUndimTime = -1L;
        this.mClock = new InternalClock();
    }

    ScreenUndimDetector(InternalClock internalClock) {
        this.mUndimCounter = 0;
        this.mUndimOccurredTime = -1L;
        this.mInteractionAfterUndimTime = -1L;
        this.mClock = internalClock;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class InternalClock {
        InternalClock() {
        }

        public long getCurrentTime() {
            return SystemClock.elapsedRealtime();
        }
    }

    public void systemReady(Context context) {
        readValuesFromDeviceConfig();
        DeviceConfig.addOnPropertiesChangedListener("attention_manager_service", context.getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.power.ScreenUndimDetector$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                ScreenUndimDetector.this.lambda$systemReady$0(properties);
            }
        });
        this.mWakeLock = ((PowerManager) context.getSystemService(PowerManager.class)).newWakeLock(536870922, UNDIM_DETECTOR_WAKE_LOCK);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$0(DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    public void recordScreenPolicy(int i, int i2) {
        int i3;
        if (i != 0 || i2 == (i3 = this.mCurrentScreenPolicy)) {
            return;
        }
        this.mCurrentScreenPolicy = i2;
        if (this.mKeepScreenOnEnabled) {
            if (i3 != 2) {
                if (i3 != 3) {
                    return;
                }
                if (i2 == 0 || i2 == 1) {
                    checkAndLogUndim(1);
                }
                if (i2 != 2) {
                    reset();
                    return;
                }
                return;
            }
            if (i2 == 3) {
                long currentTime = this.mClock.getCurrentTime();
                if (currentTime - this.mUndimCounterStartedMillis >= this.mMaxDurationBetweenUndimsMillis) {
                    reset();
                }
                int i4 = this.mUndimCounter;
                if (i4 == 0) {
                    this.mUndimCounterStartedMillis = currentTime;
                }
                int i5 = i4 + 1;
                this.mUndimCounter = i5;
                if (i5 >= this.mUndimsRequired) {
                    reset();
                    if (this.mWakeLock != null) {
                        this.mUndimOccurredTime = this.mClock.getCurrentTime();
                        this.mWakeLock.acquire(this.mKeepScreenOnForMillis);
                        return;
                    }
                    return;
                }
                return;
            }
            if (i2 == 0 || i2 == 1) {
                checkAndLogUndim(2);
            }
            reset();
        }
    }

    @VisibleForTesting
    void reset() {
        this.mUndimCounter = 0;
        this.mUndimCounterStartedMillis = 0L;
        PowerManager.WakeLock wakeLock = this.mWakeLock;
        if (wakeLock == null || !wakeLock.isHeld()) {
            return;
        }
        this.mWakeLock.release();
    }

    private boolean readKeepScreenOnNotificationEnabled() {
        return DeviceConfig.getBoolean("attention_manager_service", KEY_KEEP_SCREEN_ON_ENABLED, false);
    }

    private long readKeepScreenOnForMillis() {
        return DeviceConfig.getLong("attention_manager_service", KEY_KEEP_SCREEN_ON_FOR_MILLIS, DEFAULT_KEEP_SCREEN_ON_FOR_MILLIS);
    }

    private int readUndimsRequired() {
        int i = DeviceConfig.getInt("attention_manager_service", KEY_UNDIMS_REQUIRED, 2);
        if (i >= 1 && i <= 5) {
            return i;
        }
        Slog.e(TAG, "Provided undimsRequired=" + i + " is not allowed [1, 5]; using the default=2");
        return 2;
    }

    private long readMaxDurationBetweenUndimsMillis() {
        return DeviceConfig.getLong("attention_manager_service", KEY_MAX_DURATION_BETWEEN_UNDIMS_MILLIS, DEFAULT_MAX_DURATION_BETWEEN_UNDIMS_MILLIS);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0063 A[LOOP:0: B:2:0x0004->B:19:0x0063, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0078 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void onDeviceConfigChange(Set<String> set) {
        for (String str : set) {
            Slog.i(TAG, "onDeviceConfigChange; key=" + str);
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -2114725254:
                    if (str.equals(KEY_UNDIMS_REQUIRED)) {
                        c = 0;
                    }
                    switch (c) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                            readValuesFromDeviceConfig();
                            return;
                        default:
                            Slog.i(TAG, "Ignoring change on " + str);
                    }
                case -1871288230:
                    if (str.equals(KEY_KEEP_SCREEN_ON_ENABLED)) {
                        c = 1;
                    }
                    switch (c) {
                    }
                case 352003779:
                    if (str.equals(KEY_KEEP_SCREEN_ON_FOR_MILLIS)) {
                        c = 2;
                    }
                    switch (c) {
                    }
                case 1709324730:
                    if (str.equals(KEY_MAX_DURATION_BETWEEN_UNDIMS_MILLIS)) {
                        c = 3;
                    }
                    switch (c) {
                    }
                default:
                    switch (c) {
                    }
            }
        }
    }

    @VisibleForTesting
    void readValuesFromDeviceConfig() {
        this.mKeepScreenOnEnabled = readKeepScreenOnNotificationEnabled();
        this.mKeepScreenOnForMillis = readKeepScreenOnForMillis();
        this.mUndimsRequired = readUndimsRequired();
        this.mMaxDurationBetweenUndimsMillis = readMaxDurationBetweenUndimsMillis();
        Slog.i(TAG, "readValuesFromDeviceConfig():\nmKeepScreenOnForMillis=" + this.mKeepScreenOnForMillis + "\nmKeepScreenOnNotificationEnabled=" + this.mKeepScreenOnEnabled + "\nmUndimsRequired=" + this.mUndimsRequired);
    }

    public void userActivity(int i) {
        if (i == 0 && this.mUndimOccurredTime != 1 && this.mInteractionAfterUndimTime == -1) {
            this.mInteractionAfterUndimTime = this.mClock.getCurrentTime();
        }
    }

    private void checkAndLogUndim(int i) {
        if (this.mUndimOccurredTime != -1) {
            long currentTime = this.mClock.getCurrentTime();
            long j = currentTime - this.mUndimOccurredTime;
            long j2 = this.mInteractionAfterUndimTime;
            FrameworkStatsLog.write(365, i, j, j2 != -1 ? currentTime - j2 : -1L);
            this.mUndimOccurredTime = -1L;
            this.mInteractionAfterUndimTime = -1L;
        }
    }
}
