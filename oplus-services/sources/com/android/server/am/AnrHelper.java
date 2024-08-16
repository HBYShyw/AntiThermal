package com.android.server.am;

import android.app.usage.UsageStatsManagerInternal;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Trace;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.TimeoutRecord;
import com.android.server.FgThread;
import com.android.server.LocalServices;
import com.android.server.wm.WindowProcessController;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AnrHelper {
    static final int APP_NOT_RESPONDING_DEFER_MSG = 4;
    static final int APP_NOT_RESPONDING_DEFER_TIMEOUT_MILLIS = 10000;
    private static final int DEFAULT_THREAD_KEEP_ALIVE_SECOND = 10;
    private static final String TAG = "ActivityManager";

    @GuardedBy({"mAnrRecords"})
    private final ArrayList<AnrRecord> mAnrRecords;
    private final ExecutorService mAuxiliaryTaskExecutor;
    private final ExecutorService mEarlyDumpExecutor;
    private Handler mFgHandler;
    private long mLastAnrTimeMs;

    @GuardedBy({"mAnrRecords"})
    private int mProcessingPid;
    private final AtomicBoolean mRunning;
    private final ActivityManagerService mService;
    private final Set<Integer> mTempDumpedPids;
    private static final long EXPIRED_REPORT_TIME_MS = TimeUnit.SECONDS.toMillis(10);
    private static final long CONSECUTIVE_ANR_TIME_MS = TimeUnit.MINUTES.toMillis(2);
    private static final ThreadFactory sDefaultThreadFactory = new ThreadFactory() { // from class: com.android.server.am.AnrHelper$$ExternalSyntheticLambda1
        @Override // java.util.concurrent.ThreadFactory
        public final Thread newThread(Runnable runnable) {
            Thread lambda$static$0;
            lambda$static$0 = AnrHelper.lambda$static$0(runnable);
            return lambda$static$0;
        }
    };
    private static final ThreadFactory sMainProcessDumpThreadFactory = new ThreadFactory() { // from class: com.android.server.am.AnrHelper$$ExternalSyntheticLambda2
        @Override // java.util.concurrent.ThreadFactory
        public final Thread newThread(Runnable runnable) {
            Thread lambda$static$1;
            lambda$static$1 = AnrHelper.lambda$static$1(runnable);
            return lambda$static$1;
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Thread lambda$static$0(Runnable runnable) {
        return new Thread(runnable, "AnrAuxiliaryTaskExecutor");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Thread lambda$static$1(Runnable runnable) {
        return new Thread(runnable, "AnrMainProcessDumpThread");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnrHelper(ActivityManagerService activityManagerService) {
        this(activityManagerService, makeExpiringThreadPoolWithSize(1, sDefaultThreadFactory), makeExpiringThreadPoolWithSize(2, sMainProcessDumpThreadFactory));
    }

    @VisibleForTesting
    AnrHelper(ActivityManagerService activityManagerService, ExecutorService executorService, ExecutorService executorService2) {
        this.mAnrRecords = new ArrayList<>();
        this.mTempDumpedPids = Collections.synchronizedSet(new ArraySet());
        this.mRunning = new AtomicBoolean(false);
        this.mLastAnrTimeMs = 0L;
        this.mProcessingPid = -1;
        this.mFgHandler = new Handler(FgThread.getHandler().getLooper()) { // from class: com.android.server.am.AnrHelper.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 4) {
                    return;
                }
                Object obj = message.obj;
                AnrHelper.this.appNotResponding((AnrRecord) obj);
            }
        };
        this.mService = activityManagerService;
        this.mAuxiliaryTaskExecutor = executorService;
        this.mEarlyDumpExecutor = executorService2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void appNotResponding(ProcessRecord processRecord, TimeoutRecord timeoutRecord) {
        appNotResponding(processRecord, null, null, null, null, false, timeoutRecord, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void appNotResponding(ProcessRecord processRecord, String str, ApplicationInfo applicationInfo, String str2, WindowProcessController windowProcessController, boolean z, final TimeoutRecord timeoutRecord, boolean z2) {
        try {
            timeoutRecord.mLatencyTracker.appNotRespondingStarted();
            final int i = processRecord.mPid;
            timeoutRecord.mLatencyTracker.waitingOnAnrRecordLockStarted();
            synchronized (this.mAnrRecords) {
                timeoutRecord.mLatencyTracker.waitingOnAnrRecordLockEnded();
                if (i == 0) {
                    Slog.i("ActivityManager", "Skip zero pid ANR, process=" + processRecord.processName);
                } else if (this.mProcessingPid == i) {
                    Slog.i("ActivityManager", "Skip duplicated ANR, pid=" + i + " " + timeoutRecord.mReason);
                } else if (this.mTempDumpedPids.add(Integer.valueOf(i))) {
                    for (int size = this.mAnrRecords.size() - 1; size >= 0; size--) {
                        if (this.mAnrRecords.get(size).mPid == i) {
                            Slog.i("ActivityManager", "Skip queued ANR, pid=" + i + " " + timeoutRecord.mReason);
                        }
                    }
                    timeoutRecord.mLatencyTracker.earlyDumpRequestSubmittedWithSize(this.mTempDumpedPids.size());
                    Future submit = this.mEarlyDumpExecutor.submit(new Callable() { // from class: com.android.server.am.AnrHelper$$ExternalSyntheticLambda0
                        @Override // java.util.concurrent.Callable
                        public final Object call() {
                            File lambda$appNotResponding$2;
                            lambda$appNotResponding$2 = AnrHelper.this.lambda$appNotResponding$2(i, timeoutRecord);
                            return lambda$appNotResponding$2;
                        }
                    });
                    timeoutRecord.mLatencyTracker.anrRecordPlacingOnQueueWithSize(this.mAnrRecords.size());
                    appNotResponding(new AnrRecord(processRecord, str, applicationInfo, str2, windowProcessController, z, timeoutRecord, z2, submit));
                } else {
                    Slog.i("ActivityManager", "Skip ANR being predumped, pid=" + i + " " + timeoutRecord.mReason);
                }
            }
        } finally {
            timeoutRecord.mLatencyTracker.appNotRespondingEnded();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ File lambda$appNotResponding$2(int i, TimeoutRecord timeoutRecord) throws Exception {
        File dumpStackTracesTempFile = StackTracesDumpHelper.dumpStackTracesTempFile(i, timeoutRecord.mLatencyTracker);
        this.mTempDumpedPids.remove(Integer.valueOf(i));
        return dumpStackTracesTempFile;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deferAppNotResponding(ProcessRecord processRecord, String str, ApplicationInfo applicationInfo, String str2, WindowProcessController windowProcessController, boolean z, ExecutorService executorService, TimeoutRecord timeoutRecord, long j, boolean z2, Future<File> future) {
        AnrRecord anrRecord = new AnrRecord(processRecord, str, applicationInfo, str2, windowProcessController, z, timeoutRecord, z2, future);
        Message obtain = Message.obtain();
        obtain.what = 4;
        obtain.obj = anrRecord;
        this.mFgHandler.sendMessageDelayed(obtain, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appNotResponding(AnrRecord anrRecord) {
        if (anrRecord != null) {
            ActivityManagerService activityManagerService = this.mService;
            ProcessRecord processRecord = anrRecord.mApp;
            String str = anrRecord.mActivityShortComponentName;
            activityManagerService.handleAppNotResponding(processRecord, str, anrRecord.mAppInfo, str, anrRecord.mParentProcess, anrRecord.mAboveSystem, anrRecord.mTimeoutRecord.mReason, anrRecord.mEventId);
        }
        synchronized (this.mAnrRecords) {
            this.mAnrRecords.add(anrRecord);
        }
        startAnrConsumerIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnrConsumerIfNeeded() {
        if (this.mRunning.compareAndSet(false, true)) {
            new AnrConsumerThread().start();
        }
    }

    private static ThreadPoolExecutor makeExpiringThreadPoolWithSize(int i, ThreadFactory threadFactory) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(i, i, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue(), threadFactory);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnrConsumerThread extends Thread {
        AnrConsumerThread() {
            super("AnrConsumer");
        }

        private AnrRecord next() {
            synchronized (AnrHelper.this.mAnrRecords) {
                if (AnrHelper.this.mAnrRecords.isEmpty()) {
                    return null;
                }
                AnrRecord anrRecord = (AnrRecord) AnrHelper.this.mAnrRecords.remove(0);
                AnrHelper.this.mProcessingPid = anrRecord.mPid;
                anrRecord.mTimeoutRecord.mLatencyTracker.anrRecordsQueueSizeWhenPopped(AnrHelper.this.mAnrRecords.size());
                return anrRecord;
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                AnrRecord next = next();
                if (next == null) {
                    break;
                }
                AnrHelper.this.scheduleBinderHeavyHitterAutoSamplerIfNecessary();
                int i = next.mApp.mPid;
                if (i != next.mPid) {
                    Slog.i("ActivityManager", "Skip ANR with mismatched pid=" + next.mPid + ", current pid=" + i);
                } else {
                    long uptimeMillis = SystemClock.uptimeMillis();
                    long j = uptimeMillis - next.mTimestamp;
                    boolean z = j > AnrHelper.EXPIRED_REPORT_TIME_MS;
                    next.appNotResponding(z);
                    long uptimeMillis2 = SystemClock.uptimeMillis();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Completed ANR of ");
                    sb.append(next.mApp.processName);
                    sb.append(" in ");
                    sb.append(uptimeMillis2 - uptimeMillis);
                    sb.append("ms, latency ");
                    sb.append(j);
                    sb.append(z ? "ms (expired, only dump ANR app)" : "ms");
                    Slog.d("ActivityManager", sb.toString());
                }
            }
            AnrHelper.this.mRunning.set(false);
            synchronized (AnrHelper.this.mAnrRecords) {
                AnrHelper.this.mProcessingPid = -1;
                if (!AnrHelper.this.mAnrRecords.isEmpty()) {
                    AnrHelper.this.startAnrConsumerIfNeeded();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleBinderHeavyHitterAutoSamplerIfNecessary() {
        try {
            Trace.traceBegin(64L, "scheduleBinderHeavyHitterAutoSamplerIfNecessary()");
            long uptimeMillis = SystemClock.uptimeMillis();
            if (this.mLastAnrTimeMs + CONSECUTIVE_ANR_TIME_MS > uptimeMillis) {
                this.mService.scheduleBinderHeavyHitterAutoSampler();
            }
            this.mLastAnrTimeMs = uptimeMillis;
        } finally {
            Trace.traceEnd(64L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnrRecord {
        final boolean mAboveSystem;
        final String mActivityShortComponentName;
        final ProcessRecord mApp;
        final ApplicationInfo mAppInfo;
        final Future<File> mFirstPidFilePromise;
        final boolean mIsContinuousAnr;
        final WindowProcessController mParentProcess;
        final String mParentShortComponentName;
        final int mPid;
        final TimeoutRecord mTimeoutRecord;
        final long mTimestamp = SystemClock.uptimeMillis();
        final String mEventId = UUID.randomUUID().toString();

        AnrRecord(ProcessRecord processRecord, String str, ApplicationInfo applicationInfo, String str2, WindowProcessController windowProcessController, boolean z, TimeoutRecord timeoutRecord, boolean z2, Future<File> future) {
            this.mApp = processRecord;
            this.mPid = processRecord.mPid;
            this.mActivityShortComponentName = str;
            this.mParentShortComponentName = str2;
            this.mTimeoutRecord = timeoutRecord;
            this.mAppInfo = applicationInfo;
            this.mParentProcess = windowProcessController;
            this.mAboveSystem = z;
            this.mIsContinuousAnr = z2;
            this.mFirstPidFilePromise = future;
        }

        void appNotResponding(boolean z) {
            try {
                this.mTimeoutRecord.mLatencyTracker.anrProcessingStarted();
                this.mApp.mErrorState.appNotResponding(this.mActivityShortComponentName, this.mAppInfo, this.mParentShortComponentName, this.mParentProcess, this.mAboveSystem, this.mTimeoutRecord, AnrHelper.this.mAuxiliaryTaskExecutor, z, this.mIsContinuousAnr, this.mFirstPidFilePromise, this.mEventId);
                UsageStatsManagerInternal usageStatsManagerInternal = (UsageStatsManagerInternal) LocalServices.getService(UsageStatsManagerInternal.class);
                if (usageStatsManagerInternal != null && this.mApp.info != null) {
                    usageStatsManagerInternal.reportEvent(this.mApp.info.packageName, this.mApp.userId, 32);
                }
            } finally {
                this.mTimeoutRecord.mLatencyTracker.anrProcessingEnded();
            }
        }
    }
}
