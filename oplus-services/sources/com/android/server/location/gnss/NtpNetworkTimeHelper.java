package com.android.server.location.gnss;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.IndentingPrintWriter;
import android.util.LocalLog;
import android.util.Log;
import android.util.NtpTrustedTime;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.location.gnss.NetworkTimeHelper;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NtpNetworkTimeHelper extends NetworkTimeHelper {
    private static final long MAX_RETRY_INTERVAL = 14400000;

    @VisibleForTesting
    static final long NTP_INTERVAL = 86400000;

    @VisibleForTesting
    static final long RETRY_INTERVAL = 300000;
    private static final int STATE_IDLE = 2;
    private static final int STATE_PENDING_NETWORK = 0;
    private static final int STATE_RETRIEVING_AND_INJECTING = 1;
    private static final String WAKELOCK_KEY = "NtpTimeHelper";
    private static final long WAKELOCK_TIMEOUT_MILLIS = 60000;
    private final NetworkTimeHelper.InjectTimeCallback mCallback;
    private final ConnectivityManager mConnMgr;
    private final LocalLog mDumpLog;
    private final Handler mHandler;

    @GuardedBy({"this"})
    private int mInjectNtpTimeState;

    @GuardedBy({"this"})
    private final ExponentialBackOff mNtpBackOff;
    private final NtpTrustedTime mNtpTime;

    @GuardedBy({"this"})
    private boolean mPeriodicTimeInjection;
    private final PowerManager.WakeLock mWakeLock;
    private static final String TAG = "NtpNetworkTimeHelper";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);

    @VisibleForTesting
    NtpNetworkTimeHelper(Context context, Looper looper, NetworkTimeHelper.InjectTimeCallback injectTimeCallback, NtpTrustedTime ntpTrustedTime) {
        this.mDumpLog = new LocalLog(10, false);
        this.mNtpBackOff = new ExponentialBackOff(RETRY_INTERVAL, 14400000L);
        this.mInjectNtpTimeState = 0;
        this.mConnMgr = (ConnectivityManager) context.getSystemService("connectivity");
        this.mCallback = injectTimeCallback;
        this.mNtpTime = ntpTrustedTime;
        this.mHandler = new Handler(looper);
        this.mWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, WAKELOCK_KEY);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NtpNetworkTimeHelper(Context context, Looper looper, NetworkTimeHelper.InjectTimeCallback injectTimeCallback) {
        this(context, looper, injectTimeCallback, NtpTrustedTime.getInstance(context));
    }

    @Override // com.android.server.location.gnss.NetworkTimeHelper
    synchronized void setPeriodicTimeInjectionMode(boolean z) {
        if (z) {
            this.mPeriodicTimeInjection = true;
        }
    }

    @Override // com.android.server.location.gnss.NetworkTimeHelper
    void demandUtcTimeInjection() {
        lambda$blockingGetNtpTimeAndInject$0("demandUtcTimeInjection");
    }

    @Override // com.android.server.location.gnss.NetworkTimeHelper
    synchronized void onNetworkAvailable() {
        if (this.mInjectNtpTimeState == 0) {
            lambda$blockingGetNtpTimeAndInject$0("onNetworkAvailable");
        }
    }

    @Override // com.android.server.location.gnss.NetworkTimeHelper
    void dump(PrintWriter printWriter) {
        printWriter.println("NtpNetworkTimeHelper:");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.increaseIndent();
        synchronized (this) {
            indentingPrintWriter.println("mInjectNtpTimeState=" + this.mInjectNtpTimeState);
            indentingPrintWriter.println("mPeriodicTimeInjection=" + this.mPeriodicTimeInjection);
            indentingPrintWriter.println("mNtpBackOff=" + this.mNtpBackOff);
        }
        indentingPrintWriter.println("Debug log:");
        indentingPrintWriter.increaseIndent();
        this.mDumpLog.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("NtpTrustedTime:");
        indentingPrintWriter.increaseIndent();
        this.mNtpTime.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
    }

    private boolean isNetworkConnected() {
        NetworkInfo activeNetworkInfo = this.mConnMgr.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: retrieveAndInjectNtpTime, reason: merged with bridge method [inline-methods] */
    public synchronized void lambda$blockingGetNtpTimeAndInject$0(String str) {
        if (this.mInjectNtpTimeState == 1) {
            return;
        }
        if (!isNetworkConnected()) {
            maybeInjectCachedNtpTime(str + "[Network not connected]");
            this.mInjectNtpTimeState = 0;
            return;
        }
        this.mInjectNtpTimeState = 1;
        this.mWakeLock.acquire(WAKELOCK_TIMEOUT_MILLIS);
        new Thread(new Runnable() { // from class: com.android.server.location.gnss.NtpNetworkTimeHelper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NtpNetworkTimeHelper.this.blockingGetNtpTimeAndInject();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void blockingGetNtpTimeAndInject() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        NtpTrustedTime.TimeResult cachedTimeResult = this.mNtpTime.getCachedTimeResult();
        long j = 86400000;
        boolean forceRefresh = (cachedTimeResult == null || cachedTimeResult.getAgeMillis() >= 86400000) ? this.mNtpTime.forceRefresh() : true;
        synchronized (this) {
            this.mInjectNtpTimeState = 2;
            if (maybeInjectCachedNtpTime("blockingGetNtpTimeAndInject:, debugId=" + elapsedRealtime + ", refreshSuccess=" + forceRefresh)) {
                this.mNtpBackOff.reset();
            } else {
                logWarn("maybeInjectCachedNtpTime() returned false");
                j = this.mNtpBackOff.nextBackoffMillis();
            }
            if (this.mPeriodicTimeInjection || !forceRefresh) {
                logDebug("blockingGetNtpTimeAndInject: Scheduling later NTP retrieval, debugId=" + elapsedRealtime + ", mPeriodicTimeInjection=" + this.mPeriodicTimeInjection + ", refreshSuccess=" + forceRefresh + ", delayMillis=" + j);
                StringBuilder sb = new StringBuilder();
                sb.append("scheduled: debugId=");
                sb.append(elapsedRealtime);
                final String sb2 = sb.toString();
                this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.location.gnss.NtpNetworkTimeHelper$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        NtpNetworkTimeHelper.this.lambda$blockingGetNtpTimeAndInject$0(sb2);
                    }
                }, j);
            }
        }
        this.mWakeLock.release();
    }

    private synchronized boolean maybeInjectCachedNtpTime(String str) {
        NtpTrustedTime.TimeResult cachedTimeResult = this.mNtpTime.getCachedTimeResult();
        if (cachedTimeResult != null && cachedTimeResult.getAgeMillis() < 86400000) {
            final long timeMillis = cachedTimeResult.getTimeMillis();
            logDebug("maybeInjectCachedNtpTime: Injecting latest NTP time, reason=" + str + ", ntpResult=" + cachedTimeResult + ", System time offset millis=" + (timeMillis - System.currentTimeMillis()));
            final long elapsedRealtimeMillis = cachedTimeResult.getElapsedRealtimeMillis();
            final int uncertaintyMillis = cachedTimeResult.getUncertaintyMillis();
            this.mHandler.post(new Runnable() { // from class: com.android.server.location.gnss.NtpNetworkTimeHelper$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    NtpNetworkTimeHelper.this.lambda$maybeInjectCachedNtpTime$1(timeMillis, elapsedRealtimeMillis, uncertaintyMillis);
                }
            });
            return true;
        }
        logDebug("maybeInjectCachedNtpTime: Not injecting latest NTP time, reason=" + str + ", ntpResult=" + cachedTimeResult);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeInjectCachedNtpTime$1(long j, long j2, int i) {
        this.mCallback.injectTime(j, j2, i);
    }

    private void logWarn(String str) {
        this.mDumpLog.log(str);
        Log.e(TAG, str);
    }

    private void logDebug(String str) {
        this.mDumpLog.log(str);
        if (DEBUG) {
            Log.d(TAG, str);
        }
    }
}
