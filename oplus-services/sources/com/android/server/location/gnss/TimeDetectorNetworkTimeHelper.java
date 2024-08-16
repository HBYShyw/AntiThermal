package com.android.server.location.gnss;

import android.app.time.UnixEpochTime;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.IndentingPrintWriter;
import android.util.LocalLog;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import com.android.server.job.controllers.JobStatus;
import com.android.server.location.gnss.NetworkTimeHelper;
import com.android.server.timedetector.NetworkTimeSuggestion;
import com.android.server.timedetector.TimeDetectorInternal;
import com.android.server.timezonedetector.StateChangeListener;
import java.io.PrintWriter;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class TimeDetectorNetworkTimeHelper extends NetworkTimeHelper {

    @VisibleForTesting
    static final int MAX_NETWORK_TIME_AGE_MILLIS = 86400000;
    static final int NTP_REFRESH_INTERVAL_MILLIS = 86400000;
    private final LocalLog mDumpLog = new LocalLog(10, false);
    private final Environment mEnvironment;
    private final NetworkTimeHelper.InjectTimeCallback mInjectTimeCallback;

    @GuardedBy({"this"})
    private boolean mNetworkTimeInjected;

    @GuardedBy({"this"})
    private boolean mPeriodicTimeInjectionEnabled;
    private static final String TAG = "TDNetworkTimeHelper";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Environment {
        void clearDelayedTimeQueryCallback();

        long elapsedRealtimeMillis();

        NetworkTimeSuggestion getLatestNetworkTime();

        void requestDelayedTimeQueryCallback(TimeDetectorNetworkTimeHelper timeDetectorNetworkTimeHelper, long j);

        void requestImmediateTimeQueryCallback(TimeDetectorNetworkTimeHelper timeDetectorNetworkTimeHelper, String str);

        void setNetworkTimeUpdateListener(StateChangeListener stateChangeListener);
    }

    public static boolean isInUse() {
        return false;
    }

    TimeDetectorNetworkTimeHelper(Environment environment, NetworkTimeHelper.InjectTimeCallback injectTimeCallback) {
        Objects.requireNonNull(injectTimeCallback);
        this.mInjectTimeCallback = injectTimeCallback;
        Objects.requireNonNull(environment);
        Environment environment2 = environment;
        this.mEnvironment = environment2;
        environment2.setNetworkTimeUpdateListener(new StateChangeListener() { // from class: com.android.server.location.gnss.TimeDetectorNetworkTimeHelper$$ExternalSyntheticLambda0
            @Override // com.android.server.timezonedetector.StateChangeListener
            public final void onChange() {
                TimeDetectorNetworkTimeHelper.this.onNetworkTimeAvailable();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.location.gnss.NetworkTimeHelper
    public synchronized void setPeriodicTimeInjectionMode(boolean z) {
        this.mPeriodicTimeInjectionEnabled = z;
        if (!z) {
            removePeriodicNetworkTimeQuery();
        }
        this.mEnvironment.requestImmediateTimeQueryCallback(this, "setPeriodicTimeInjectionMode(" + z + ")");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onNetworkTimeAvailable() {
        this.mEnvironment.requestImmediateTimeQueryCallback(this, "onNetworkTimeAvailable");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.location.gnss.NetworkTimeHelper
    public void onNetworkAvailable() {
        synchronized (this) {
            if (!this.mNetworkTimeInjected) {
                this.mEnvironment.requestImmediateTimeQueryCallback(this, "onNetworkAvailable");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.location.gnss.NetworkTimeHelper
    public void demandUtcTimeInjection() {
        this.mEnvironment.requestImmediateTimeQueryCallback(this, "demandUtcTimeInjection");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void delayedQueryAndInjectNetworkTime() {
        queryAndInjectNetworkTime("delayedTimeQueryCallback");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void queryAndInjectNetworkTime(String str) {
        NetworkTimeSuggestion latestNetworkTime = this.mEnvironment.getLatestNetworkTime();
        maybeInjectNetworkTime(latestNetworkTime, str);
        removePeriodicNetworkTimeQuery();
        if (this.mPeriodicTimeInjectionEnabled) {
            logToDumpLog("queryAndInjectNtpTime: Scheduling periodic query reason=" + str + " latestNetworkTime=" + latestNetworkTime + " maxDelayMillis=86400000");
            this.mEnvironment.requestDelayedTimeQueryCallback(this, (long) 86400000);
        }
    }

    private long calculateTimeSignalAgeMillis(NetworkTimeSuggestion networkTimeSuggestion) {
        if (networkTimeSuggestion == null) {
            return JobStatus.NO_LATEST_RUNTIME;
        }
        return this.mEnvironment.elapsedRealtimeMillis() - networkTimeSuggestion.getUnixEpochTime().getElapsedRealtimeMillis();
    }

    @GuardedBy({"this"})
    private void maybeInjectNetworkTime(NetworkTimeSuggestion networkTimeSuggestion, String str) {
        if (calculateTimeSignalAgeMillis(networkTimeSuggestion) > 86400000) {
            logToDumpLog("maybeInjectNetworkTime: Not injecting latest network time latestNetworkTime=" + networkTimeSuggestion + " reason=" + str);
            return;
        }
        UnixEpochTime unixEpochTime = networkTimeSuggestion.getUnixEpochTime();
        long unixEpochTimeMillis = unixEpochTime.getUnixEpochTimeMillis();
        logToDumpLog("maybeInjectNetworkTime: Injecting latest network time latestNetworkTime=" + networkTimeSuggestion + " reason=" + str + " System time offset millis=" + (unixEpochTimeMillis - System.currentTimeMillis()));
        this.mInjectTimeCallback.injectTime(unixEpochTimeMillis, unixEpochTime.getElapsedRealtimeMillis(), networkTimeSuggestion.getUncertaintyMillis());
        this.mNetworkTimeInjected = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.location.gnss.NetworkTimeHelper
    public void dump(PrintWriter printWriter) {
        printWriter.println("TimeDetectorNetworkTimeHelper:");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.increaseIndent();
        synchronized (this) {
            indentingPrintWriter.println("mPeriodicTimeInjectionEnabled=" + this.mPeriodicTimeInjectionEnabled);
        }
        indentingPrintWriter.println("Debug log:");
        this.mDumpLog.dump(indentingPrintWriter);
    }

    private void logToDumpLog(String str) {
        this.mDumpLog.log(str);
        if (DEBUG) {
            Log.d(TAG, str);
        }
    }

    private void removePeriodicNetworkTimeQuery() {
        this.mEnvironment.clearDelayedTimeQueryCallback();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class EnvironmentImpl implements Environment {
        private final Handler mHandler;
        private final Object mScheduledRunnableToken = new Object();
        private final Object mImmediateRunnableToken = new Object();
        private final TimeDetectorInternal mTimeDetectorInternal = (TimeDetectorInternal) LocalServices.getService(TimeDetectorInternal.class);

        EnvironmentImpl(Looper looper) {
            this.mHandler = new Handler(looper);
        }

        @Override // com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment
        public long elapsedRealtimeMillis() {
            return SystemClock.elapsedRealtime();
        }

        @Override // com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment
        public NetworkTimeSuggestion getLatestNetworkTime() {
            return this.mTimeDetectorInternal.getLatestNetworkSuggestion();
        }

        @Override // com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment
        public void setNetworkTimeUpdateListener(StateChangeListener stateChangeListener) {
            this.mTimeDetectorInternal.addNetworkTimeUpdateListener(stateChangeListener);
        }

        @Override // com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment
        public void requestImmediateTimeQueryCallback(final TimeDetectorNetworkTimeHelper timeDetectorNetworkTimeHelper, final String str) {
            synchronized (this) {
                this.mHandler.removeCallbacksAndMessages(this.mImmediateRunnableToken);
                this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.location.gnss.TimeDetectorNetworkTimeHelper$EnvironmentImpl$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        TimeDetectorNetworkTimeHelper.this.queryAndInjectNetworkTime(str);
                    }
                }, this.mImmediateRunnableToken, 0L);
            }
        }

        @Override // com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment
        public void requestDelayedTimeQueryCallback(final TimeDetectorNetworkTimeHelper timeDetectorNetworkTimeHelper, long j) {
            synchronized (this) {
                clearDelayedTimeQueryCallback();
                Handler handler = this.mHandler;
                Objects.requireNonNull(timeDetectorNetworkTimeHelper);
                handler.postDelayed(new Runnable() { // from class: com.android.server.location.gnss.TimeDetectorNetworkTimeHelper$EnvironmentImpl$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        TimeDetectorNetworkTimeHelper.this.delayedQueryAndInjectNetworkTime();
                    }
                }, this.mScheduledRunnableToken, j);
            }
        }

        @Override // com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment
        public synchronized void clearDelayedTimeQueryCallback() {
            this.mHandler.removeCallbacksAndMessages(this.mScheduledRunnableToken);
        }
    }
}
