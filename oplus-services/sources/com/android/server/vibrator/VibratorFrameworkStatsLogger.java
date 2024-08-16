package com.android.server.vibrator;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.vibrator.VibrationStats;
import java.util.ArrayDeque;
import java.util.Queue;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class VibratorFrameworkStatsLogger {
    private static final String TAG = "VibratorFrameworkStatsLogger";
    private static final int VIBRATION_REPORTED_MAX_QUEUE_SIZE = 300;
    private static final int VIBRATION_REPORTED_MIN_INTERVAL_MILLIS = 10;
    private static final int VIBRATION_REPORTED_WARNING_QUEUE_SIZE = 200;
    private final Runnable mConsumeVibrationStatsQueueRunnable;
    private final Handler mHandler;

    @GuardedBy({"mLock"})
    private long mLastVibrationReportedLogUptime;
    private final Object mLock;
    private final long mVibrationReportedLogIntervalMillis;
    private final long mVibrationReportedQueueMaxSize;

    @GuardedBy({"mLock"})
    private Queue<VibrationStats.StatsInfo> mVibrationStatsQueue;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VibratorFrameworkStatsLogger(Handler handler) {
        this(handler, 10, VIBRATION_REPORTED_MAX_QUEUE_SIZE);
    }

    @VisibleForTesting
    VibratorFrameworkStatsLogger(Handler handler, int i, int i2) {
        this.mLock = new Object();
        this.mConsumeVibrationStatsQueueRunnable = new Runnable() { // from class: com.android.server.vibrator.VibratorFrameworkStatsLogger$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                VibratorFrameworkStatsLogger.this.lambda$new$0();
            }
        };
        this.mVibrationStatsQueue = new ArrayDeque();
        this.mHandler = handler;
        this.mVibrationReportedLogIntervalMillis = i;
        this.mVibrationReportedQueueMaxSize = i2;
    }

    public void writeVibratorStateOnAsync(final int i, final long j) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.vibrator.VibratorFrameworkStatsLogger$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                FrameworkStatsLog.write_non_chained(84, i, null, 1, j);
            }
        });
    }

    public void writeVibratorStateOffAsync(final int i) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.vibrator.VibratorFrameworkStatsLogger$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                FrameworkStatsLog.write_non_chained(84, i, null, 0, 0);
            }
        });
    }

    public void writeVibrationReportedAsync(VibrationStats.StatsInfo statsInfo) {
        int size;
        boolean z;
        long max;
        synchronized (this.mLock) {
            size = this.mVibrationStatsQueue.size();
            z = size == 0;
            if (size < this.mVibrationReportedQueueMaxSize) {
                this.mVibrationStatsQueue.offer(statsInfo);
            }
            max = Math.max(0L, (this.mLastVibrationReportedLogUptime + this.mVibrationReportedLogIntervalMillis) - SystemClock.uptimeMillis());
        }
        if (size + 1 == 200) {
            Slog.w(TAG, " Approaching vibration metrics queue limit, events might be dropped.");
        }
        if (z) {
            this.mHandler.postDelayed(this.mConsumeVibrationStatsQueueRunnable, max);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: writeVibrationReportedFromQueue, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        VibrationStats.StatsInfo poll;
        boolean z;
        synchronized (this.mLock) {
            poll = this.mVibrationStatsQueue.poll();
            z = !this.mVibrationStatsQueue.isEmpty();
            if (poll != null) {
                this.mLastVibrationReportedLogUptime = SystemClock.uptimeMillis();
            }
        }
        if (poll == null) {
            Slog.w(TAG, "Unexpected vibration metric flush with empty queue. Ignoring.");
        } else {
            poll.writeVibrationReported();
        }
        if (z) {
            this.mHandler.postDelayed(this.mConsumeVibrationStatsQueueRunnable, this.mVibrationReportedLogIntervalMillis);
        }
    }
}
