package com.android.server.timedetector;

import android.R;
import android.annotation.RequiresPermission;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.time.UnixEpochTime;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.IndentingPrintWriter;
import android.util.LocalLog;
import android.util.Log;
import android.util.NtpTrustedTime;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.DumpUtils;
import com.android.server.INetworkTimeUpdateServiceExt;
import com.android.server.LocalServices;
import com.android.server.job.controllers.JobStatus;
import com.android.server.pm.PackageManagerService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NetworkTimeUpdateService extends Binder {
    private static final String ACTION_POLL = "com.android.server.timedetector.NetworkTimeUpdateService.action.POLL";
    private static final boolean DBG = false;
    private static final int POLL_REQUEST = 0;
    private static final String TAG = "NetworkTimeUpdateService";
    private final ConnectivityManager mCM;
    private final Context mContext;
    private final Engine mEngine;
    private final Handler mHandler;
    private final NtpTrustedTime mNtpTrustedTime;
    private final Engine.RefreshCallbacks mRefreshCallbacks;
    private final PowerManager.WakeLock mWakeLock;
    private final Object mLock = new Object();
    private INetworkTimeUpdateServiceExt mNetworkTimeUpdateServiceExt = (INetworkTimeUpdateServiceExt) ExtLoader.type(INetworkTimeUpdateServiceExt.class).create();

    @GuardedBy({"mLock"})
    private Network mDefaultNetwork = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Engine {

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public interface RefreshCallbacks {
            void scheduleNextRefresh(long j);

            void submitSuggestion(NetworkTimeSuggestion networkTimeSuggestion);
        }

        void dump(PrintWriter printWriter);

        boolean forceRefreshForTests(Network network, RefreshCallbacks refreshCallbacks);

        void refreshAndRescheduleIfRequired(Network network, String str, RefreshCallbacks refreshCallbacks);
    }

    public NetworkTimeUpdateService(Context context) {
        Objects.requireNonNull(context);
        this.mContext = context;
        this.mCM = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
        this.mWakeLock = ((PowerManager) context.getSystemService(PowerManager.class)).newWakeLock(1, TAG);
        NtpTrustedTime ntpTrustedTime = NtpTrustedTime.getInstance(context);
        this.mNtpTrustedTime = ntpTrustedTime;
        this.mEngine = new EngineImpl(new Supplier() { // from class: com.android.server.timedetector.NetworkTimeUpdateService$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return Long.valueOf(SystemClock.elapsedRealtime());
            }
        }, context.getResources().getInteger(R.integer.db_journal_size_limit), context.getResources().getInteger(R.integer.db_wal_autocheckpoint), context.getResources().getInteger(R.integer.db_wal_truncate_size), ntpTrustedTime);
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(AlarmManager.class);
        final TimeDetectorInternal timeDetectorInternal = (TimeDetectorInternal) LocalServices.getService(TimeDetectorInternal.class);
        final PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_POLL, (Uri) null).setPackage(PackageManagerService.PLATFORM_PACKAGE_NAME), 67108864);
        this.mRefreshCallbacks = new Engine.RefreshCallbacks() { // from class: com.android.server.timedetector.NetworkTimeUpdateService.1
            @Override // com.android.server.timedetector.NetworkTimeUpdateService.Engine.RefreshCallbacks
            public void scheduleNextRefresh(long j) {
                alarmManager.cancel(broadcast);
                alarmManager.set(3, j, broadcast);
            }

            @Override // com.android.server.timedetector.NetworkTimeUpdateService.Engine.RefreshCallbacks
            public void submitSuggestion(NetworkTimeSuggestion networkTimeSuggestion) {
                timeDetectorInternal.suggestNetworkTime(networkTimeSuggestion);
            }
        };
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        this.mHandler = handlerThread.getThreadHandler();
        this.mNetworkTimeUpdateServiceExt.init(context, this);
    }

    public void systemRunning() {
        this.mContext.registerReceiver(new ScheduledRefreshBroadcastReceiver(), new IntentFilter(ACTION_POLL));
        this.mNetworkTimeUpdateServiceExt.checkSystemTime();
        Log.d(TAG, "NetworkTimeUpdateService systemReady");
        this.mCM.registerDefaultNetworkCallback(new NetworkConnectivityCallback(), this.mHandler);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("auto_time"), false, new AutoTimeSettingObserver(this.mHandler, this.mContext));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @RequiresPermission("android.permission.SET_TIME")
    public void setServerConfigForTests(NtpTrustedTime.NtpConfig ntpConfig) {
        this.mContext.enforceCallingPermission("android.permission.SET_TIME", "set NTP server config for tests");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mNtpTrustedTime.setServerConfigForTests(ntpConfig);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @RequiresPermission("android.permission.SET_TIME")
    public boolean forceRefreshForTests() {
        Network network;
        this.mContext.enforceCallingPermission("android.permission.SET_TIME", "force network time refresh");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                network = this.mDefaultNetwork;
            }
            if (network != null) {
                return this.mEngine.forceRefreshForTests(network, this.mRefreshCallbacks);
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPollNetworkTime(String str) {
        Network network;
        synchronized (this.mLock) {
            network = this.mDefaultNetwork;
        }
        this.mWakeLock.acquire();
        try {
            this.mEngine.refreshAndRescheduleIfRequired(network, str, this.mRefreshCallbacks);
        } finally {
            this.mWakeLock.release();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class ScheduledRefreshBroadcastReceiver extends BroadcastReceiver implements Runnable {
        private ScheduledRefreshBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            NetworkTimeUpdateService.this.mHandler.post(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            NetworkTimeUpdateService.this.onPollNetworkTime("scheduled refresh");
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class NetworkConnectivityCallback extends ConnectivityManager.NetworkCallback {
        private NetworkConnectivityCallback() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            Log.d(NetworkTimeUpdateService.TAG, String.format("New default network %s; checking time.", network));
            synchronized (NetworkTimeUpdateService.this.mLock) {
                NetworkTimeUpdateService.this.mDefaultNetwork = network;
            }
            NetworkTimeUpdateService.this.onPollNetworkTime("network available");
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            synchronized (NetworkTimeUpdateService.this.mLock) {
                if (network.equals(NetworkTimeUpdateService.this.mDefaultNetwork)) {
                    NetworkTimeUpdateService.this.mDefaultNetwork = null;
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class AutoTimeSettingObserver extends ContentObserver {
        private final Context mContext;

        AutoTimeSettingObserver(Handler handler, Context context) {
            super(handler);
            Objects.requireNonNull(context);
            this.mContext = context;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            if (isAutomaticTimeEnabled()) {
                NetworkTimeUpdateService.this.onPollNetworkTime("automatic time enabled");
            }
        }

        private boolean isAutomaticTimeEnabled() {
            return Settings.Global.getInt(this.mContext.getContentResolver(), "auto_time", 0) != 0;
        }
    }

    @Override // android.os.Binder
    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            synchronized (this.mLock) {
                printWriter.println("mDefaultNetwork=" + this.mDefaultNetwork);
            }
            this.mEngine.dump(printWriter);
            printWriter.println();
        }
    }

    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
        new NetworkTimeUpdateServiceShellCommand(this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class EngineImpl implements Engine {
        private final Supplier<Long> mElapsedRealtimeMillisSupplier;

        @GuardedBy({"this"})
        private Long mLastRefreshAttemptElapsedRealtimeMillis;
        private final LocalLog mLocalDebugLog = new LocalLog(30, false);
        private final int mNormalPollingIntervalMillis;
        private final NtpTrustedTime mNtpTrustedTime;
        private final int mShortPollingIntervalMillis;

        @GuardedBy({"this"})
        private int mTryAgainCounter;
        private final int mTryAgainTimesMax;

        @VisibleForTesting
        EngineImpl(Supplier<Long> supplier, int i, int i2, int i3, NtpTrustedTime ntpTrustedTime) {
            Objects.requireNonNull(supplier);
            this.mElapsedRealtimeMillisSupplier = supplier;
            if (i2 > i) {
                throw new IllegalArgumentException(String.format("shortPollingIntervalMillis (%s) > normalPollingIntervalMillis (%s)", Integer.valueOf(i2), Integer.valueOf(i)));
            }
            this.mNormalPollingIntervalMillis = i;
            this.mShortPollingIntervalMillis = i2;
            this.mTryAgainTimesMax = i3;
            Objects.requireNonNull(ntpTrustedTime);
            this.mNtpTrustedTime = ntpTrustedTime;
        }

        @Override // com.android.server.timedetector.NetworkTimeUpdateService.Engine
        public boolean forceRefreshForTests(Network network, Engine.RefreshCallbacks refreshCallbacks) {
            boolean tryRefresh = tryRefresh(network);
            logToDebugAndDumpsys("forceRefreshForTests: refreshSuccessful=" + tryRefresh);
            if (tryRefresh) {
                makeNetworkTimeSuggestion(this.mNtpTrustedTime.getCachedTimeResult(), "EngineImpl.forceRefreshForTests()", refreshCallbacks);
            }
            return tryRefresh;
        }

        /* JADX WARN: Removed duplicated region for block: B:43:0x00bc A[Catch: all -> 0x0135, TryCatch #0 {, blocks: (B:18:0x0050, B:21:0x006a, B:22:0x006d, B:24:0x0071, B:25:0x0074, B:27:0x007b, B:28:0x007d, B:30:0x0084, B:31:0x0086, B:33:0x008b, B:34:0x008e, B:36:0x0092, B:37:0x0097, B:39:0x009c, B:40:0x00a0, B:43:0x00bc, B:44:0x00c9, B:45:0x0133, B:48:0x00a2, B:50:0x00a6, B:51:0x00ab, B:52:0x0095), top: B:17:0x0050 }] */
        @Override // com.android.server.timedetector.NetworkTimeUpdateService.Engine
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void refreshAndRescheduleIfRequired(Network network, String str, Engine.RefreshCallbacks refreshCallbacks) {
            boolean z;
            long j;
            long longValue;
            if (network == null) {
                logToDebugAndDumpsys("refreshIfRequiredAndReschedule: reason=" + str + ": No default network available. No refresh attempted and no next attempt scheduled.");
                return;
            }
            NtpTrustedTime.TimeResult cachedTimeResult = this.mNtpTrustedTime.getCachedTimeResult();
            synchronized (this) {
                long longValue2 = this.mElapsedRealtimeMillisSupplier.get().longValue();
                z = calculateTimeResultAgeMillis(cachedTimeResult, longValue2) >= ((long) this.mNormalPollingIntervalMillis) && isRefreshAllowed(longValue2);
            }
            boolean tryRefresh = z ? tryRefresh(network) : false;
            synchronized (this) {
                NtpTrustedTime.TimeResult cachedTimeResult2 = this.mNtpTrustedTime.getCachedTimeResult();
                long longValue3 = this.mElapsedRealtimeMillisSupplier.get().longValue();
                long calculateTimeResultAgeMillis = calculateTimeResultAgeMillis(cachedTimeResult2, longValue3);
                if (z) {
                    if (tryRefresh) {
                        this.mTryAgainCounter = 0;
                    } else {
                        int i = this.mTryAgainTimesMax;
                        if (i < 0) {
                            this.mTryAgainCounter = 1;
                        } else {
                            int i2 = this.mTryAgainCounter + 1;
                            this.mTryAgainCounter = i2;
                            if (i2 > i) {
                                this.mTryAgainCounter = 0;
                            }
                        }
                    }
                }
                int i3 = this.mNormalPollingIntervalMillis;
                if (calculateTimeResultAgeMillis < i3) {
                    this.mTryAgainCounter = 0;
                }
                if (calculateTimeResultAgeMillis < i3) {
                    makeNetworkTimeSuggestion(cachedTimeResult2, str, refreshCallbacks);
                }
                long j2 = this.mTryAgainCounter > 0 ? this.mShortPollingIntervalMillis : this.mNormalPollingIntervalMillis;
                if (calculateTimeResultAgeMillis < j2) {
                    longValue = cachedTimeResult2.getElapsedRealtimeMillis();
                } else {
                    Long l = this.mLastRefreshAttemptElapsedRealtimeMillis;
                    if (l != null) {
                        longValue = l.longValue();
                    } else {
                        Log.w(NetworkTimeUpdateService.TAG, "mLastRefreshAttemptElapsedRealtimeMillis unexpectedly missing. Scheduling using currentElapsedRealtimeMillis");
                        logToDebugAndDumpsys("mLastRefreshAttemptElapsedRealtimeMillis unexpectedly missing. Scheduling using currentElapsedRealtimeMillis");
                        j = longValue3 + j2;
                        if (j <= longValue3) {
                            Log.w(NetworkTimeUpdateService.TAG, "nextRefreshElapsedRealtimeMillis is a time in the past. Scheduling using currentElapsedRealtimeMillis instead");
                            logToDebugAndDumpsys("nextRefreshElapsedRealtimeMillis is a time in the past. Scheduling using currentElapsedRealtimeMillis instead");
                            j = longValue3 + j2;
                        }
                        refreshCallbacks.scheduleNextRefresh(j);
                        logToDebugAndDumpsys("refreshIfRequiredAndReschedule: network=" + network + ", reason=" + str + ", initialTimeResult=" + cachedTimeResult + ", shouldAttemptRefresh=" + z + ", refreshSuccessful=" + tryRefresh + ", currentElapsedRealtimeMillis=" + formatElapsedRealtimeMillis(longValue3) + ", latestTimeResult=" + cachedTimeResult2 + ", mTryAgainCounter=" + this.mTryAgainCounter + ", refreshAttemptDelayMillis=" + j2 + ", nextRefreshElapsedRealtimeMillis=" + formatElapsedRealtimeMillis(j));
                    }
                }
                j = longValue + j2;
                if (j <= longValue3) {
                }
                refreshCallbacks.scheduleNextRefresh(j);
                logToDebugAndDumpsys("refreshIfRequiredAndReschedule: network=" + network + ", reason=" + str + ", initialTimeResult=" + cachedTimeResult + ", shouldAttemptRefresh=" + z + ", refreshSuccessful=" + tryRefresh + ", currentElapsedRealtimeMillis=" + formatElapsedRealtimeMillis(longValue3) + ", latestTimeResult=" + cachedTimeResult2 + ", mTryAgainCounter=" + this.mTryAgainCounter + ", refreshAttemptDelayMillis=" + j2 + ", nextRefreshElapsedRealtimeMillis=" + formatElapsedRealtimeMillis(j));
            }
        }

        private static String formatElapsedRealtimeMillis(long j) {
            return Duration.ofMillis(j) + " (" + j + ")";
        }

        private static long calculateTimeResultAgeMillis(NtpTrustedTime.TimeResult timeResult, long j) {
            return timeResult == null ? JobStatus.NO_LATEST_RUNTIME : timeResult.getAgeMillis(j);
        }

        @GuardedBy({"this"})
        private boolean isRefreshAllowed(long j) {
            Long l = this.mLastRefreshAttemptElapsedRealtimeMillis;
            return l == null || j >= l.longValue() + ((long) this.mShortPollingIntervalMillis);
        }

        private boolean tryRefresh(Network network) {
            long longValue = this.mElapsedRealtimeMillisSupplier.get().longValue();
            synchronized (this) {
                this.mLastRefreshAttemptElapsedRealtimeMillis = Long.valueOf(longValue);
            }
            return this.mNtpTrustedTime.forceRefresh(network);
        }

        private void makeNetworkTimeSuggestion(NtpTrustedTime.TimeResult timeResult, String str, Engine.RefreshCallbacks refreshCallbacks) {
            NetworkTimeSuggestion networkTimeSuggestion = new NetworkTimeSuggestion(new UnixEpochTime(timeResult.getElapsedRealtimeMillis(), timeResult.getTimeMillis()), timeResult.getUncertaintyMillis());
            networkTimeSuggestion.addDebugInfo(str);
            networkTimeSuggestion.addDebugInfo(timeResult.toString());
            refreshCallbacks.submitSuggestion(networkTimeSuggestion);
        }

        @Override // com.android.server.timedetector.NetworkTimeUpdateService.Engine
        public void dump(PrintWriter printWriter) {
            IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
            indentingPrintWriter.println("mNormalPollingIntervalMillis=" + this.mNormalPollingIntervalMillis);
            indentingPrintWriter.println("mShortPollingIntervalMillis=" + this.mShortPollingIntervalMillis);
            indentingPrintWriter.println("mTryAgainTimesMax=" + this.mTryAgainTimesMax);
            synchronized (this) {
                Long l = this.mLastRefreshAttemptElapsedRealtimeMillis;
                indentingPrintWriter.println("mLastRefreshAttemptElapsedRealtimeMillis=" + (l == null ? "null" : formatElapsedRealtimeMillis(l.longValue())));
                indentingPrintWriter.println("mTryAgainCounter=" + this.mTryAgainCounter);
            }
            indentingPrintWriter.println();
            indentingPrintWriter.println("NtpTrustedTime:");
            indentingPrintWriter.increaseIndent();
            this.mNtpTrustedTime.dump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.println("Debug log:");
            indentingPrintWriter.increaseIndent();
            this.mLocalDebugLog.dump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
        }

        private void logToDebugAndDumpsys(String str) {
            this.mLocalDebugLog.log(str);
        }
    }
}
