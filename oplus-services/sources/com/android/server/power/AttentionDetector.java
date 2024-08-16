package com.android.server.power;

import android.R;
import android.app.ActivityManager;
import android.app.SynchronousUserSwitchObserver;
import android.attention.AttentionManagerInternal;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.LocalServices;
import com.android.server.job.controllers.JobStatus;
import com.android.server.wm.WindowManagerInternal;
import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AttentionDetector {
    private static final boolean DEBUG = false;
    static final long DEFAULT_POST_DIM_CHECK_DURATION_MILLIS = 7000;
    static final long DEFAULT_PRE_DIM_CHECK_DURATION_MILLIS = 2000;
    static final String KEY_MAX_EXTENSION_MILLIS = "max_extension_millis";
    static final String KEY_POST_DIM_CHECK_DURATION_MILLIS = "post_dim_check_duration_millis";
    static final String KEY_PRE_DIM_CHECK_DURATION_MILLIS = "pre_dim_check_duration_millis";
    private static final String TAG = "AttentionDetector";

    @VisibleForTesting
    protected AttentionManagerInternal mAttentionManager;

    @VisibleForTesting
    AttentionCallbackInternalImpl mCallback;

    @VisibleForTesting
    protected ContentResolver mContentResolver;
    private Context mContext;

    @VisibleForTesting
    protected long mDefaultMaximumExtensionMillis;
    private long mEffectivePostDimTimeoutMillis;
    private boolean mIsSettingEnabled;
    private long mLastActedOnNextScreenDimming;
    private long mLastUserActivityTime;
    private final Object mLock;
    private long mMaximumExtensionMillis;
    private final Runnable mOnUserAttention;

    @VisibleForTesting
    protected long mPreDimCheckDurationMillis;
    private long mRequestedPostDimTimeoutMillis;

    @VisibleForTesting
    protected WindowManagerInternal mWindowManager;
    private AtomicLong mConsecutiveTimeoutExtendedCount = new AtomicLong(0);
    private final AtomicBoolean mRequested = new AtomicBoolean(false);

    @VisibleForTesting
    protected int mRequestId = 0;
    private int mWakefulness = 1;

    public AttentionDetector(Runnable runnable, Object obj) {
        this.mOnUserAttention = runnable;
        this.mLock = obj;
    }

    @VisibleForTesting
    void updateEnabledFromSettings(Context context) {
        this.mIsSettingEnabled = Settings.Secure.getIntForUser(context.getContentResolver(), "adaptive_sleep", 0, -2) == 1;
    }

    public void systemReady(final Context context) {
        this.mContext = context;
        updateEnabledFromSettings(context);
        this.mContentResolver = context.getContentResolver();
        this.mAttentionManager = (AttentionManagerInternal) LocalServices.getService(AttentionManagerInternal.class);
        this.mWindowManager = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
        this.mDefaultMaximumExtensionMillis = context.getResources().getInteger(R.integer.config_attentiveWarningDuration);
        try {
            ActivityManager.getService().registerUserSwitchObserver(new UserSwitchObserver(), TAG);
        } catch (RemoteException unused) {
        }
        context.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("adaptive_sleep"), false, new ContentObserver(new Handler(context.getMainLooper())) { // from class: com.android.server.power.AttentionDetector.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                AttentionDetector.this.updateEnabledFromSettings(context);
            }
        }, -1);
        readValuesFromDeviceConfig();
        DeviceConfig.addOnPropertiesChangedListener("attention_manager_service", context.getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.power.AttentionDetector$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                AttentionDetector.this.lambda$systemReady$0(properties);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$0(DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    public long updateUserActivity(long j, long j2) {
        if (j == this.mLastActedOnNextScreenDimming || !this.mIsSettingEnabled || !isAttentionServiceSupported() || this.mWindowManager.isKeyguardShowingAndNotOccluded()) {
            return j;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        long j3 = j - this.mPreDimCheckDurationMillis;
        long j4 = this.mLastUserActivityTime + this.mMaximumExtensionMillis;
        if (uptimeMillis < j3) {
            return j3;
        }
        if (j4 < j3) {
            return j;
        }
        if (this.mRequested.get()) {
            return j3;
        }
        this.mRequested.set(true);
        this.mRequestId++;
        this.mLastActedOnNextScreenDimming = j;
        this.mCallback = new AttentionCallbackInternalImpl(this.mRequestId);
        this.mEffectivePostDimTimeoutMillis = Math.min(this.mRequestedPostDimTimeoutMillis, j2);
        Slog.v(TAG, "Checking user attention, ID: " + this.mRequestId);
        if (!this.mAttentionManager.checkAttention(this.mPreDimCheckDurationMillis + this.mEffectivePostDimTimeoutMillis, this.mCallback)) {
            this.mRequested.set(false);
        }
        return j3;
    }

    public int onUserActivity(long j, int i) {
        if (i == 0 || i == 1 || i == 2 || i == 3) {
            cancelCurrentRequestIfAny();
            this.mLastUserActivityTime = j;
            resetConsecutiveExtensionCount();
            return 1;
        }
        if (i != 4) {
            return -1;
        }
        this.mConsecutiveTimeoutExtendedCount.incrementAndGet();
        return 0;
    }

    public void onWakefulnessChangeStarted(int i) {
        this.mWakefulness = i;
        if (i != 1) {
            cancelCurrentRequestIfAny();
            resetConsecutiveExtensionCount();
        }
    }

    private void cancelCurrentRequestIfAny() {
        if (this.mRequested.get()) {
            this.mAttentionManager.cancelAttentionCheck(this.mCallback);
            this.mRequested.set(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetConsecutiveExtensionCount() {
        long andSet = this.mConsecutiveTimeoutExtendedCount.getAndSet(0L);
        if (andSet > 0) {
            FrameworkStatsLog.write(168, andSet);
        }
    }

    @VisibleForTesting
    boolean isAttentionServiceSupported() {
        AttentionManagerInternal attentionManagerInternal = this.mAttentionManager;
        return attentionManagerInternal != null && attentionManagerInternal.isAttentionServiceSupported();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("AttentionDetector:");
        printWriter.println(" mIsSettingEnabled=" + this.mIsSettingEnabled);
        printWriter.println(" mMaxExtensionMillis=" + this.mMaximumExtensionMillis);
        printWriter.println(" mPreDimCheckDurationMillis=" + this.mPreDimCheckDurationMillis);
        printWriter.println(" mEffectivePostDimTimeout=" + this.mEffectivePostDimTimeoutMillis);
        printWriter.println(" mLastUserActivityTime(excludingAttention)=" + this.mLastUserActivityTime);
        printWriter.println(" mAttentionServiceSupported=" + isAttentionServiceSupported());
        printWriter.println(" mRequested=" + this.mRequested);
    }

    @VisibleForTesting
    protected long getPreDimCheckDurationMillis() {
        long j = DeviceConfig.getLong("attention_manager_service", KEY_PRE_DIM_CHECK_DURATION_MILLIS, DEFAULT_PRE_DIM_CHECK_DURATION_MILLIS);
        if (j >= 0 && j <= 13000) {
            return j;
        }
        Slog.w(TAG, "Bad flag value supplied for: pre_dim_check_duration_millis");
        return DEFAULT_PRE_DIM_CHECK_DURATION_MILLIS;
    }

    @VisibleForTesting
    protected long getPostDimCheckDurationMillis() {
        long j = DeviceConfig.getLong("attention_manager_service", KEY_POST_DIM_CHECK_DURATION_MILLIS, DEFAULT_POST_DIM_CHECK_DURATION_MILLIS);
        if (j >= 0 && j <= JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY) {
            return j;
        }
        Slog.w(TAG, "Bad flag value supplied for: post_dim_check_duration_millis");
        return DEFAULT_POST_DIM_CHECK_DURATION_MILLIS;
    }

    @VisibleForTesting
    protected long getMaxExtensionMillis() {
        long j = DeviceConfig.getLong("attention_manager_service", KEY_MAX_EXTENSION_MILLIS, this.mDefaultMaximumExtensionMillis);
        if (j >= 0 && j <= 3600000) {
            return j;
        }
        Slog.w(TAG, "Bad flag value supplied for: max_extension_millis");
        return this.mDefaultMaximumExtensionMillis;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0042 A[LOOP:0: B:2:0x0004->B:16:0x0042, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0059 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void onDeviceConfigChange(Set<String> set) {
        for (String str : set) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -2018189628:
                    if (str.equals(KEY_POST_DIM_CHECK_DURATION_MILLIS)) {
                        c = 0;
                    }
                    switch (c) {
                        case 0:
                        case 1:
                        case 2:
                            readValuesFromDeviceConfig();
                            return;
                        default:
                            Slog.i(TAG, "Ignoring change on " + str);
                    }
                case -511526975:
                    if (str.equals(KEY_MAX_EXTENSION_MILLIS)) {
                        c = 1;
                    }
                    switch (c) {
                    }
                case 417901319:
                    if (str.equals(KEY_PRE_DIM_CHECK_DURATION_MILLIS)) {
                        c = 2;
                    }
                    switch (c) {
                    }
                default:
                    switch (c) {
                    }
            }
        }
    }

    private void readValuesFromDeviceConfig() {
        this.mMaximumExtensionMillis = getMaxExtensionMillis();
        this.mPreDimCheckDurationMillis = getPreDimCheckDurationMillis();
        this.mRequestedPostDimTimeoutMillis = getPostDimCheckDurationMillis();
        Slog.i(TAG, "readValuesFromDeviceConfig():\nmMaximumExtensionMillis=" + this.mMaximumExtensionMillis + "\nmPreDimCheckDurationMillis=" + this.mPreDimCheckDurationMillis + "\nmRequestedPostDimTimeoutMillis=" + this.mRequestedPostDimTimeoutMillis);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class AttentionCallbackInternalImpl extends AttentionManagerInternal.AttentionCallbackInternal {
        private final int mId;

        AttentionCallbackInternalImpl(int i) {
            this.mId = i;
        }

        public void onSuccess(int i, long j) {
            Slog.v(AttentionDetector.TAG, "onSuccess: " + i + ", ID: " + this.mId);
            int i2 = this.mId;
            AttentionDetector attentionDetector = AttentionDetector.this;
            if (i2 == attentionDetector.mRequestId && attentionDetector.mRequested.getAndSet(false)) {
                synchronized (AttentionDetector.this.mLock) {
                    if (AttentionDetector.this.mWakefulness != 1) {
                        return;
                    }
                    if (i == 1) {
                        AttentionDetector.this.mOnUserAttention.run();
                    } else {
                        AttentionDetector.this.resetConsecutiveExtensionCount();
                    }
                }
            }
        }

        public void onFailure(int i) {
            Slog.i(AttentionDetector.TAG, "Failed to check attention: " + i + ", ID: " + this.mId);
            AttentionDetector.this.mRequested.set(false);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class UserSwitchObserver extends SynchronousUserSwitchObserver {
        private UserSwitchObserver() {
        }

        public void onUserSwitching(int i) throws RemoteException {
            AttentionDetector attentionDetector = AttentionDetector.this;
            attentionDetector.updateEnabledFromSettings(attentionDetector.mContext);
        }
    }
}
