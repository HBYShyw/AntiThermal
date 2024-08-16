package com.android.server.locksettings;

import android.content.Context;
import android.content.pm.UserInfo;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.widget.RebootEscrowListener;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.crypto.SecretKey;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RebootEscrowManager {
    private static final int BOOT_COUNT_TOLERANCE = 5;
    private static final int DEFAULT_LOAD_ESCROW_BASE_TIMEOUT_MILLIS = 180000;
    private static final int DEFAULT_LOAD_ESCROW_DATA_RETRY_COUNT = 3;
    private static final int DEFAULT_LOAD_ESCROW_DATA_RETRY_INTERVAL_SECONDS = 30;
    private static final int DEFAULT_LOAD_ESCROW_TIMEOUT_EXTENSION_MILLIS = 5000;
    static final int ERROR_KEYSTORE_FAILURE = 7;
    static final int ERROR_LOAD_ESCROW_KEY = 3;
    static final int ERROR_NONE = 0;
    static final int ERROR_NO_NETWORK = 8;
    static final int ERROR_NO_PROVIDER = 2;
    static final int ERROR_PROVIDER_MISMATCH = 6;
    static final int ERROR_RETRY_COUNT_EXHAUSTED = 4;
    static final int ERROR_TIMEOUT_EXHAUSTED = 9;
    static final int ERROR_UNKNOWN = 1;
    static final int ERROR_UNLOCK_ALL_USERS = 5;
    static final String OTHER_VBMETA_DIGEST_PROP_NAME = "ota.other.vbmeta_digest";

    @VisibleForTesting
    public static final String REBOOT_ESCROW_ARMED_KEY = "reboot_escrow_armed_count";
    static final String REBOOT_ESCROW_KEY_ARMED_TIMESTAMP = "reboot_escrow_key_stored_timestamp";
    static final String REBOOT_ESCROW_KEY_OTHER_VBMETA_DIGEST = "reboot_escrow_key_other_vbmeta_digest";
    static final String REBOOT_ESCROW_KEY_PROVIDER = "reboot_escrow_key_provider";
    static final String REBOOT_ESCROW_KEY_VBMETA_DIGEST = "reboot_escrow_key_vbmeta_digest";
    private static final String TAG = "RebootEscrowManager";
    static final String VBMETA_DIGEST_PROP_NAME = "ro.boot.vbmeta.digest";
    private final Callbacks mCallbacks;
    private final RebootEscrowEventLog mEventLog;
    private final Handler mHandler;
    private final Injector mInjector;
    private final Object mKeyGenerationLock;
    private final RebootEscrowKeyStoreManager mKeyStoreManager;
    private int mLoadEscrowDataErrorCode;
    private boolean mLoadEscrowDataWithRetry;
    private ConnectivityManager.NetworkCallback mNetworkCallback;

    @GuardedBy({"mKeyGenerationLock"})
    private RebootEscrowKey mPendingRebootEscrowKey;
    private RebootEscrowListener mRebootEscrowListener;
    private boolean mRebootEscrowReady;
    private boolean mRebootEscrowTimedOut;
    private boolean mRebootEscrowWanted;
    private final LockSettingsStorage mStorage;
    private final UserManager mUserManager;
    PowerManager.WakeLock mWakeLock;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Callbacks {
        boolean isUserSecure(int i);

        void onRebootEscrowRestored(byte b, byte[] bArr, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    @interface RebootEscrowErrorCode {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Injector {
        protected Context mContext;
        private final RebootEscrowKeyStoreManager mKeyStoreManager = new RebootEscrowKeyStoreManager();
        private RebootEscrowProviderInterface mRebootEscrowProvider;
        private final LockSettingsStorage mStorage;

        @VisibleForTesting
        public int getLoadEscrowTimeoutMillis() {
            return RebootEscrowManager.DEFAULT_LOAD_ESCROW_BASE_TIMEOUT_MILLIS;
        }

        Injector(Context context, LockSettingsStorage lockSettingsStorage) {
            this.mContext = context;
            this.mStorage = lockSettingsStorage;
        }

        private RebootEscrowProviderInterface createRebootEscrowProvider() {
            RebootEscrowProviderInterface rebootEscrowProviderHalImpl;
            if (serverBasedResumeOnReboot()) {
                Slog.i(RebootEscrowManager.TAG, "Using server based resume on reboot");
                rebootEscrowProviderHalImpl = new RebootEscrowProviderServerBasedImpl(this.mContext, this.mStorage);
            } else {
                Slog.i(RebootEscrowManager.TAG, "Using HAL based resume on reboot");
                rebootEscrowProviderHalImpl = new RebootEscrowProviderHalImpl();
            }
            if (rebootEscrowProviderHalImpl.hasRebootEscrowSupport()) {
                return rebootEscrowProviderHalImpl;
            }
            return null;
        }

        void post(Handler handler, Runnable runnable) {
            handler.post(runnable);
        }

        void postDelayed(Handler handler, Runnable runnable, long j) {
            handler.postDelayed(runnable, j);
        }

        public boolean serverBasedResumeOnReboot() {
            if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.reboot_escrow")) {
                return DeviceConfig.getBoolean("ota", "server_based_ror_enabled", false);
            }
            return true;
        }

        public boolean waitForInternet() {
            return DeviceConfig.getBoolean("ota", "wait_for_internet_ror", false);
        }

        public boolean isNetworkConnected() {
            NetworkCapabilities networkCapabilities;
            ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class);
            return connectivityManager != null && (networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork())) != null && networkCapabilities.hasCapability(12) && networkCapabilities.hasCapability(16);
        }

        public boolean requestNetworkWithInternet(ConnectivityManager.NetworkCallback networkCallback) {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class);
            if (connectivityManager == null) {
                return false;
            }
            connectivityManager.requestNetwork(new NetworkRequest.Builder().addCapability(12).build(), networkCallback, getLoadEscrowTimeoutMillis());
            return true;
        }

        public void stopRequestingNetwork(ConnectivityManager.NetworkCallback networkCallback) {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class);
            if (connectivityManager == null) {
                return;
            }
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }

        public Context getContext() {
            return this.mContext;
        }

        public UserManager getUserManager() {
            return (UserManager) this.mContext.getSystemService("user");
        }

        public RebootEscrowKeyStoreManager getKeyStoreManager() {
            return this.mKeyStoreManager;
        }

        public RebootEscrowProviderInterface createRebootEscrowProviderIfNeeded() {
            if (this.mRebootEscrowProvider == null) {
                this.mRebootEscrowProvider = createRebootEscrowProvider();
            }
            return this.mRebootEscrowProvider;
        }

        PowerManager.WakeLock getWakeLock() {
            return ((PowerManager) this.mContext.getSystemService(PowerManager.class)).newWakeLock(1, RebootEscrowManager.TAG);
        }

        public RebootEscrowProviderInterface getRebootEscrowProvider() {
            return this.mRebootEscrowProvider;
        }

        public void clearRebootEscrowProvider() {
            this.mRebootEscrowProvider = null;
        }

        public int getBootCount() {
            return Settings.Global.getInt(this.mContext.getContentResolver(), "boot_count", 0);
        }

        public long getCurrentTimeMillis() {
            return System.currentTimeMillis();
        }

        public int getLoadEscrowDataRetryLimit() {
            return DeviceConfig.getInt("ota", "load_escrow_data_retry_count", 3);
        }

        public int getLoadEscrowDataRetryIntervalSeconds() {
            return DeviceConfig.getInt("ota", "load_escrow_data_retry_interval_seconds", 30);
        }

        @VisibleForTesting
        public int getWakeLockTimeoutMillis() {
            return getLoadEscrowTimeoutMillis() + 5000;
        }

        public void reportMetric(boolean z, int i, int i2, int i3, int i4, int i5, int i6) {
            FrameworkStatsLog.write(238, z, i, i2, i3, i4, i5, i6);
        }

        public RebootEscrowEventLog getEventLog() {
            return new RebootEscrowEventLog();
        }

        public String getVbmetaDigest(boolean z) {
            if (z) {
                return SystemProperties.get(RebootEscrowManager.OTHER_VBMETA_DIGEST_PROP_NAME);
            }
            return SystemProperties.get(RebootEscrowManager.VBMETA_DIGEST_PROP_NAME);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RebootEscrowManager(Context context, Callbacks callbacks, LockSettingsStorage lockSettingsStorage, Handler handler) {
        this(new Injector(context, lockSettingsStorage), callbacks, lockSettingsStorage, handler);
    }

    @VisibleForTesting
    RebootEscrowManager(Injector injector, Callbacks callbacks, LockSettingsStorage lockSettingsStorage, Handler handler) {
        this.mLoadEscrowDataErrorCode = 0;
        this.mRebootEscrowTimedOut = false;
        this.mLoadEscrowDataWithRetry = false;
        this.mKeyGenerationLock = new Object();
        this.mInjector = injector;
        this.mCallbacks = callbacks;
        this.mStorage = lockSettingsStorage;
        this.mUserManager = injector.getUserManager();
        this.mEventLog = injector.getEventLog();
        this.mKeyStoreManager = injector.getKeyStoreManager();
        this.mHandler = handler;
    }

    private void setLoadEscrowDataErrorCode(final int i, Handler handler) {
        if (this.mInjector.waitForInternet()) {
            this.mInjector.post(handler, new Runnable() { // from class: com.android.server.locksettings.RebootEscrowManager$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    RebootEscrowManager.this.lambda$setLoadEscrowDataErrorCode$0(i);
                }
            });
        } else {
            this.mLoadEscrowDataErrorCode = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setLoadEscrowDataErrorCode$0(int i) {
        this.mLoadEscrowDataErrorCode = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void compareAndSetLoadEscrowDataErrorCode(int i, int i2, Handler handler) {
        if (i == this.mLoadEscrowDataErrorCode) {
            setLoadEscrowDataErrorCode(i2, handler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGetRebootEscrowKeyFailed(List<UserInfo> list, int i, Handler handler) {
        Slog.w(TAG, "Had reboot escrow data for users, but no key; removing escrow storage.");
        Iterator<UserInfo> it = list.iterator();
        while (it.hasNext()) {
            this.mStorage.removeRebootEscrow(it.next().id);
        }
        onEscrowRestoreComplete(false, i, handler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loadRebootEscrowDataIfAvailable(final Handler handler) {
        final List<UserInfo> users = this.mUserManager.getUsers();
        final ArrayList arrayList = new ArrayList();
        for (UserInfo userInfo : users) {
            if (this.mCallbacks.isUserSecure(userInfo.id) && this.mStorage.hasRebootEscrow(userInfo.id)) {
                arrayList.add(userInfo);
            }
        }
        if (arrayList.isEmpty()) {
            Slog.i(TAG, "No reboot escrow data found for users, skipping loading escrow data");
            clearMetricsStorage();
            return;
        }
        PowerManager.WakeLock wakeLock = this.mInjector.getWakeLock();
        this.mWakeLock = wakeLock;
        if (wakeLock != null) {
            wakeLock.setReferenceCounted(false);
            this.mWakeLock.acquire(this.mInjector.getWakeLockTimeoutMillis());
        }
        if (this.mInjector.waitForInternet()) {
            this.mInjector.postDelayed(handler, new Runnable() { // from class: com.android.server.locksettings.RebootEscrowManager$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RebootEscrowManager.this.lambda$loadRebootEscrowDataIfAvailable$1();
                }
            }, this.mInjector.getLoadEscrowTimeoutMillis());
            this.mInjector.post(handler, new Runnable() { // from class: com.android.server.locksettings.RebootEscrowManager$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    RebootEscrowManager.this.lambda$loadRebootEscrowDataIfAvailable$2(handler, users, arrayList);
                }
            });
        } else {
            this.mInjector.post(handler, new Runnable() { // from class: com.android.server.locksettings.RebootEscrowManager$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    RebootEscrowManager.this.lambda$loadRebootEscrowDataIfAvailable$3(handler, users, arrayList);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRebootEscrowDataIfAvailable$1() {
        this.mRebootEscrowTimedOut = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRebootEscrowDataIfAvailable$3(Handler handler, List list, List list2) {
        lambda$scheduleLoadRebootEscrowDataOrFail$4(handler, 0, list, list2);
    }

    void scheduleLoadRebootEscrowDataOrFail(final Handler handler, final int i, final List<UserInfo> list, final List<UserInfo> list2) {
        Objects.requireNonNull(handler);
        int loadEscrowDataRetryLimit = this.mInjector.getLoadEscrowDataRetryLimit();
        int loadEscrowDataRetryIntervalSeconds = this.mInjector.getLoadEscrowDataRetryIntervalSeconds();
        if (i < loadEscrowDataRetryLimit && !this.mRebootEscrowTimedOut) {
            Slog.i(TAG, "Scheduling loadRebootEscrowData retry number: " + i);
            this.mInjector.postDelayed(handler, new Runnable() { // from class: com.android.server.locksettings.RebootEscrowManager$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    RebootEscrowManager.this.lambda$scheduleLoadRebootEscrowDataOrFail$4(handler, i, list, list2);
                }
            }, (long) (loadEscrowDataRetryIntervalSeconds * 1000));
            return;
        }
        if (this.mInjector.waitForInternet()) {
            if (this.mRebootEscrowTimedOut) {
                Slog.w(TAG, "Failed to load reboot escrow data within timeout");
                compareAndSetLoadEscrowDataErrorCode(0, 9, handler);
            } else {
                Slog.w(TAG, "Failed to load reboot escrow data after " + i + " attempts");
                compareAndSetLoadEscrowDataErrorCode(0, 4, handler);
            }
            onGetRebootEscrowKeyFailed(list, i, handler);
            return;
        }
        Slog.w(TAG, "Failed to load reboot escrow data after " + i + " attempts");
        if (this.mInjector.serverBasedResumeOnReboot() && !this.mInjector.isNetworkConnected()) {
            this.mLoadEscrowDataErrorCode = 8;
        } else {
            this.mLoadEscrowDataErrorCode = 4;
        }
        onGetRebootEscrowKeyFailed(list, i, handler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: loadRebootEscrowDataOnInternet, reason: merged with bridge method [inline-methods] */
    public void lambda$loadRebootEscrowDataIfAvailable$2(final Handler handler, final List<UserInfo> list, final List<UserInfo> list2) {
        if (!this.mInjector.serverBasedResumeOnReboot()) {
            lambda$scheduleLoadRebootEscrowDataOrFail$4(handler, 0, list, list2);
            return;
        }
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.server.locksettings.RebootEscrowManager.1
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                RebootEscrowManager.this.compareAndSetLoadEscrowDataErrorCode(8, 0, handler);
                if (RebootEscrowManager.this.mLoadEscrowDataWithRetry) {
                    return;
                }
                RebootEscrowManager.this.mLoadEscrowDataWithRetry = true;
                RebootEscrowManager.this.lambda$scheduleLoadRebootEscrowDataOrFail$4(handler, 0, list, list2);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onUnavailable() {
                Slog.w(RebootEscrowManager.TAG, "Failed to connect to network within timeout");
                RebootEscrowManager.this.compareAndSetLoadEscrowDataErrorCode(0, 8, handler);
                RebootEscrowManager.this.onGetRebootEscrowKeyFailed(list, 0, handler);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                Slog.w(RebootEscrowManager.TAG, "Network lost, still attempting to load escrow key.");
                RebootEscrowManager.this.compareAndSetLoadEscrowDataErrorCode(0, 8, handler);
            }
        };
        this.mNetworkCallback = networkCallback;
        if (this.mInjector.requestNetworkWithInternet(networkCallback)) {
            return;
        }
        lambda$scheduleLoadRebootEscrowDataOrFail$4(handler, 0, list, list2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: loadRebootEscrowDataWithRetry, reason: merged with bridge method [inline-methods] */
    public void lambda$scheduleLoadRebootEscrowDataOrFail$4(Handler handler, int i, List<UserInfo> list, List<UserInfo> list2) {
        SecretKey keyStoreEncryptionKey = this.mKeyStoreManager.getKeyStoreEncryptionKey();
        if (keyStoreEncryptionKey == null) {
            Slog.i(TAG, "Failed to load the key for resume on reboot from key store.");
        }
        try {
            RebootEscrowKey andClearRebootEscrowKey = getAndClearRebootEscrowKey(keyStoreEncryptionKey, handler);
            if (andClearRebootEscrowKey == null) {
                if (this.mLoadEscrowDataErrorCode == 0) {
                    if (this.mInjector.serverBasedResumeOnReboot() != this.mStorage.getInt(REBOOT_ESCROW_KEY_PROVIDER, -1, 0)) {
                        setLoadEscrowDataErrorCode(6, handler);
                    } else {
                        setLoadEscrowDataErrorCode(3, handler);
                    }
                }
                onGetRebootEscrowKeyFailed(list, i + 1, handler);
                return;
            }
            this.mEventLog.addEntry(1);
            Iterator<UserInfo> it = list2.iterator();
            boolean z = true;
            while (it.hasNext()) {
                z &= restoreRebootEscrowForUser(it.next().id, andClearRebootEscrowKey, keyStoreEncryptionKey);
            }
            if (!z) {
                compareAndSetLoadEscrowDataErrorCode(0, 5, handler);
            }
            onEscrowRestoreComplete(z, i + 1, handler);
        } catch (IOException e) {
            Slog.i(TAG, "Failed to load escrow key, scheduling retry.", e);
            scheduleLoadRebootEscrowDataOrFail(handler, i + 1, list, list2);
        }
    }

    private void clearMetricsStorage() {
        this.mStorage.removeKey(REBOOT_ESCROW_ARMED_KEY, 0);
        this.mStorage.removeKey(REBOOT_ESCROW_KEY_ARMED_TIMESTAMP, 0);
        this.mStorage.removeKey(REBOOT_ESCROW_KEY_VBMETA_DIGEST, 0);
        this.mStorage.removeKey(REBOOT_ESCROW_KEY_OTHER_VBMETA_DIGEST, 0);
        this.mStorage.removeKey(REBOOT_ESCROW_KEY_PROVIDER, 0);
    }

    private int getVbmetaDigestStatusOnRestoreComplete() {
        String vbmetaDigest = this.mInjector.getVbmetaDigest(false);
        String string = this.mStorage.getString(REBOOT_ESCROW_KEY_VBMETA_DIGEST, "", 0);
        String string2 = this.mStorage.getString(REBOOT_ESCROW_KEY_OTHER_VBMETA_DIGEST, "", 0);
        if (string2.isEmpty()) {
            return vbmetaDigest.equals(string) ? 0 : 2;
        }
        if (vbmetaDigest.equals(string2)) {
            return 0;
        }
        return vbmetaDigest.equals(string) ? 1 : 2;
    }

    private void reportMetricOnRestoreComplete(boolean z, int i, Handler handler) {
        int i2 = this.mInjector.serverBasedResumeOnReboot() ? 2 : 1;
        long j = this.mStorage.getLong(REBOOT_ESCROW_KEY_ARMED_TIMESTAMP, -1L, 0);
        long currentTimeMillis = this.mInjector.getCurrentTimeMillis();
        int i3 = (j == -1 || currentTimeMillis <= j) ? -1 : ((int) (currentTimeMillis - j)) / 1000;
        int vbmetaDigestStatusOnRestoreComplete = getVbmetaDigestStatusOnRestoreComplete();
        if (!z) {
            compareAndSetLoadEscrowDataErrorCode(0, 1, handler);
        }
        Slog.i(TAG, "Reporting RoR recovery metrics, success: " + z + ", service type: " + i2 + ", error code: " + this.mLoadEscrowDataErrorCode);
        this.mInjector.reportMetric(z, this.mLoadEscrowDataErrorCode, i2, i, i3, vbmetaDigestStatusOnRestoreComplete, -1);
        setLoadEscrowDataErrorCode(0, handler);
    }

    private void onEscrowRestoreComplete(boolean z, int i, Handler handler) {
        int i2 = this.mStorage.getInt(REBOOT_ESCROW_ARMED_KEY, -1, 0);
        int bootCount = this.mInjector.getBootCount() - i2;
        if (z || (i2 != -1 && bootCount <= 5)) {
            reportMetricOnRestoreComplete(z, i, handler);
        }
        this.mKeyStoreManager.clearKeyStoreEncryptionKey();
        this.mInjector.clearRebootEscrowProvider();
        clearMetricsStorage();
        ConnectivityManager.NetworkCallback networkCallback = this.mNetworkCallback;
        if (networkCallback != null) {
            this.mInjector.stopRequestingNetwork(networkCallback);
        }
        PowerManager.WakeLock wakeLock = this.mWakeLock;
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    private RebootEscrowKey getAndClearRebootEscrowKey(SecretKey secretKey, Handler handler) throws IOException {
        RebootEscrowProviderInterface createRebootEscrowProviderIfNeeded = this.mInjector.createRebootEscrowProviderIfNeeded();
        if (createRebootEscrowProviderIfNeeded == null) {
            Slog.w(TAG, "Had reboot escrow data for users, but RebootEscrowProvider is unavailable");
            setLoadEscrowDataErrorCode(2, handler);
            return null;
        }
        if (createRebootEscrowProviderIfNeeded.getType() == 1 && secretKey == null) {
            setLoadEscrowDataErrorCode(7, handler);
            return null;
        }
        RebootEscrowKey andClearRebootEscrowKey = createRebootEscrowProviderIfNeeded.getAndClearRebootEscrowKey(secretKey);
        if (andClearRebootEscrowKey != null) {
            this.mEventLog.addEntry(4);
        }
        return andClearRebootEscrowKey;
    }

    private boolean restoreRebootEscrowForUser(int i, RebootEscrowKey rebootEscrowKey, SecretKey secretKey) {
        if (!this.mStorage.hasRebootEscrow(i)) {
            return false;
        }
        try {
            byte[] readRebootEscrow = this.mStorage.readRebootEscrow(i);
            this.mStorage.removeRebootEscrow(i);
            RebootEscrowData fromEncryptedData = RebootEscrowData.fromEncryptedData(rebootEscrowKey, readRebootEscrow, secretKey);
            this.mCallbacks.onRebootEscrowRestored(fromEncryptedData.getSpVersion(), fromEncryptedData.getSyntheticPassword(), i);
            Slog.i(TAG, "Restored reboot escrow data for user " + i);
            this.mEventLog.addEntry(7, i);
            return true;
        } catch (IOException e) {
            Slog.w(TAG, "Could not load reboot escrow data for user " + i, e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void callToRebootEscrowIfNeeded(int i, byte b, byte[] bArr) {
        if (this.mRebootEscrowWanted) {
            if (this.mInjector.createRebootEscrowProviderIfNeeded() == null) {
                Slog.w(TAG, "Not storing escrow data, RebootEscrowProvider is unavailable");
                return;
            }
            RebootEscrowKey generateEscrowKeyIfNeeded = generateEscrowKeyIfNeeded();
            if (generateEscrowKeyIfNeeded == null) {
                Slog.e(TAG, "Could not generate escrow key");
                return;
            }
            SecretKey generateKeyStoreEncryptionKeyIfNeeded = this.mKeyStoreManager.generateKeyStoreEncryptionKeyIfNeeded();
            if (generateKeyStoreEncryptionKeyIfNeeded == null) {
                Slog.e(TAG, "Failed to generate encryption key from keystore.");
                return;
            }
            try {
                this.mStorage.writeRebootEscrow(i, RebootEscrowData.fromSyntheticPassword(generateEscrowKeyIfNeeded, b, bArr, generateKeyStoreEncryptionKeyIfNeeded).getBlob());
                this.mEventLog.addEntry(6, i);
                setRebootEscrowReady(true);
            } catch (IOException e) {
                setRebootEscrowReady(false);
                Slog.w(TAG, "Could not escrow reboot data", e);
            }
        }
    }

    private RebootEscrowKey generateEscrowKeyIfNeeded() {
        synchronized (this.mKeyGenerationLock) {
            RebootEscrowKey rebootEscrowKey = this.mPendingRebootEscrowKey;
            if (rebootEscrowKey != null) {
                return rebootEscrowKey;
            }
            try {
                RebootEscrowKey generate = RebootEscrowKey.generate();
                this.mPendingRebootEscrowKey = generate;
                return generate;
            } catch (IOException unused) {
                Slog.w(TAG, "Could not generate reboot escrow key");
                return null;
            }
        }
    }

    private void clearRebootEscrowIfNeeded() {
        this.mRebootEscrowWanted = false;
        setRebootEscrowReady(false);
        RebootEscrowProviderInterface createRebootEscrowProviderIfNeeded = this.mInjector.createRebootEscrowProviderIfNeeded();
        if (createRebootEscrowProviderIfNeeded == null) {
            Slog.w(TAG, "RebootEscrowProvider is unavailable for clear request");
        } else {
            createRebootEscrowProviderIfNeeded.clearRebootEscrowKey();
        }
        this.mInjector.clearRebootEscrowProvider();
        clearMetricsStorage();
        Iterator it = this.mUserManager.getUsers().iterator();
        while (it.hasNext()) {
            this.mStorage.removeRebootEscrow(((UserInfo) it.next()).id);
        }
        this.mEventLog.addEntry(3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int armRebootEscrowIfNeeded() {
        RebootEscrowKey rebootEscrowKey;
        if (!this.mRebootEscrowReady) {
            return 2;
        }
        RebootEscrowProviderInterface rebootEscrowProvider = this.mInjector.getRebootEscrowProvider();
        if (rebootEscrowProvider == null) {
            Slog.w(TAG, "Not storing escrow key, RebootEscrowProvider is unavailable");
            clearRebootEscrowIfNeeded();
            return 3;
        }
        boolean serverBasedResumeOnReboot = this.mInjector.serverBasedResumeOnReboot();
        int type = rebootEscrowProvider.getType();
        if (serverBasedResumeOnReboot != type) {
            Slog.w(TAG, "Expect reboot escrow provider " + (serverBasedResumeOnReboot ? 1 : 0) + ", but the RoR is prepared with " + type + ". Please prepare the RoR again.");
            clearRebootEscrowIfNeeded();
            return 4;
        }
        synchronized (this.mKeyGenerationLock) {
            rebootEscrowKey = this.mPendingRebootEscrowKey;
        }
        if (rebootEscrowKey == null) {
            Slog.e(TAG, "Escrow key is null, but escrow was marked as ready");
            clearRebootEscrowIfNeeded();
            return 5;
        }
        SecretKey keyStoreEncryptionKey = this.mKeyStoreManager.getKeyStoreEncryptionKey();
        if (keyStoreEncryptionKey == null) {
            Slog.e(TAG, "Failed to get encryption key from keystore.");
            clearRebootEscrowIfNeeded();
            return 6;
        }
        if (!rebootEscrowProvider.storeRebootEscrowKey(rebootEscrowKey, keyStoreEncryptionKey)) {
            return 7;
        }
        this.mStorage.setInt(REBOOT_ESCROW_ARMED_KEY, this.mInjector.getBootCount(), 0);
        this.mStorage.setLong(REBOOT_ESCROW_KEY_ARMED_TIMESTAMP, this.mInjector.getCurrentTimeMillis(), 0);
        this.mStorage.setString(REBOOT_ESCROW_KEY_VBMETA_DIGEST, this.mInjector.getVbmetaDigest(false), 0);
        this.mStorage.setString(REBOOT_ESCROW_KEY_OTHER_VBMETA_DIGEST, this.mInjector.getVbmetaDigest(true), 0);
        this.mStorage.setInt(REBOOT_ESCROW_KEY_PROVIDER, type, 0);
        this.mEventLog.addEntry(2);
        return 0;
    }

    private void setRebootEscrowReady(final boolean z) {
        if (this.mRebootEscrowReady != z) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.locksettings.RebootEscrowManager$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    RebootEscrowManager.this.lambda$setRebootEscrowReady$5(z);
                }
            });
        }
        this.mRebootEscrowReady = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setRebootEscrowReady$5(boolean z) {
        this.mRebootEscrowListener.onPreparedForReboot(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean prepareRebootEscrow() {
        clearRebootEscrowIfNeeded();
        if (this.mInjector.createRebootEscrowProviderIfNeeded() == null) {
            Slog.w(TAG, "No reboot escrow provider, skipping resume on reboot preparation.");
            return false;
        }
        this.mRebootEscrowWanted = true;
        this.mEventLog.addEntry(5);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean clearRebootEscrow() {
        clearRebootEscrowIfNeeded();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRebootEscrowListener(RebootEscrowListener rebootEscrowListener) {
        this.mRebootEscrowListener = rebootEscrowListener;
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class RebootEscrowEvent {
        static final int CLEARED_LSKF_REQUEST = 3;
        static final int FOUND_ESCROW_DATA = 1;
        static final int REQUESTED_LSKF = 5;
        static final int RETRIEVED_LSKF_FOR_USER = 7;
        static final int RETRIEVED_STORED_KEK = 4;
        static final int SET_ARMED_STATUS = 2;
        static final int STORED_LSKF_FOR_USER = 6;
        final int mEventId;
        final long mTimestamp;
        final Integer mUserId;
        final long mWallTime;

        RebootEscrowEvent(int i) {
            this(i, null);
        }

        RebootEscrowEvent(int i, Integer num) {
            this.mEventId = i;
            this.mUserId = num;
            this.mTimestamp = SystemClock.uptimeMillis();
            this.mWallTime = System.currentTimeMillis();
        }

        String getEventDescription() {
            switch (this.mEventId) {
                case 1:
                    return "Found escrow data";
                case 2:
                    return "Set armed status";
                case 3:
                    return "Cleared request for LSKF";
                case 4:
                    return "Retrieved stored KEK";
                case 5:
                    return "Requested LSKF";
                case 6:
                    return "Stored LSKF for user";
                case 7:
                    return "Retrieved LSKF for user";
                default:
                    return "Unknown event ID " + this.mEventId;
            }
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class RebootEscrowEventLog {
        private RebootEscrowEvent[] mEntries = new RebootEscrowEvent[16];
        private int mNextIndex = 0;

        void addEntry(int i) {
            addEntryInternal(new RebootEscrowEvent(i));
        }

        void addEntry(int i, int i2) {
            addEntryInternal(new RebootEscrowEvent(i, Integer.valueOf(i2)));
        }

        private void addEntryInternal(RebootEscrowEvent rebootEscrowEvent) {
            int i = this.mNextIndex;
            RebootEscrowEvent[] rebootEscrowEventArr = this.mEntries;
            rebootEscrowEventArr[i] = rebootEscrowEvent;
            this.mNextIndex = (i + 1) % rebootEscrowEventArr.length;
        }

        void dump(IndentingPrintWriter indentingPrintWriter) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
            int i = 0;
            while (true) {
                RebootEscrowEvent[] rebootEscrowEventArr = this.mEntries;
                if (i >= rebootEscrowEventArr.length) {
                    return;
                }
                RebootEscrowEvent rebootEscrowEvent = rebootEscrowEventArr[(this.mNextIndex + i) % rebootEscrowEventArr.length];
                if (rebootEscrowEvent != null) {
                    indentingPrintWriter.print("Event #");
                    indentingPrintWriter.println(i);
                    indentingPrintWriter.println(" time=" + simpleDateFormat.format(new Date(rebootEscrowEvent.mWallTime)) + " (timestamp=" + rebootEscrowEvent.mTimestamp + ")");
                    indentingPrintWriter.print(" event=");
                    indentingPrintWriter.println(rebootEscrowEvent.getEventDescription());
                    if (rebootEscrowEvent.mUserId != null) {
                        indentingPrintWriter.print(" user=");
                        indentingPrintWriter.println(rebootEscrowEvent.mUserId);
                    }
                }
                i++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        boolean z;
        indentingPrintWriter.print("mRebootEscrowWanted=");
        indentingPrintWriter.println(this.mRebootEscrowWanted);
        indentingPrintWriter.print("mRebootEscrowReady=");
        indentingPrintWriter.println(this.mRebootEscrowReady);
        indentingPrintWriter.print("mRebootEscrowListener=");
        indentingPrintWriter.println(this.mRebootEscrowListener);
        indentingPrintWriter.print("mLoadEscrowDataErrorCode=");
        indentingPrintWriter.println(this.mLoadEscrowDataErrorCode);
        synchronized (this.mKeyGenerationLock) {
            z = this.mPendingRebootEscrowKey != null;
        }
        indentingPrintWriter.print("mPendingRebootEscrowKey is ");
        indentingPrintWriter.println(z ? "set" : "not set");
        RebootEscrowProviderInterface rebootEscrowProvider = this.mInjector.getRebootEscrowProvider();
        indentingPrintWriter.print("RebootEscrowProvider type is " + (rebootEscrowProvider == null ? "null" : String.valueOf(rebootEscrowProvider.getType())));
        indentingPrintWriter.println();
        indentingPrintWriter.println("Event log:");
        indentingPrintWriter.increaseIndent();
        this.mEventLog.dump(indentingPrintWriter);
        indentingPrintWriter.println();
        indentingPrintWriter.decreaseIndent();
    }
}
