package com.android.server.devicepolicy;

import android.app.admin.SecurityLog;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SecurityLogMonitor implements Runnable {
    private static final long BROADCAST_RETRY_INTERVAL_MS;
    private static final int BUFFER_ENTRIES_CRITICAL_LEVEL = 9216;
    private static final int BUFFER_ENTRIES_MAXIMUM_LEVEL = 10240;

    @VisibleForTesting
    static final int BUFFER_ENTRIES_NOTIFICATION_LEVEL = 1024;
    private static final boolean DEBUG = false;
    private static final long FORCE_FETCH_THROTTLE_NS;
    private static final long OVERLAP_NS;
    private static final long POLLING_INTERVAL_MS;
    private static final long RATE_LIMIT_INTERVAL_MS = TimeUnit.HOURS.toMillis(2);
    private static final String TAG = "SecurityLogMonitor";

    @GuardedBy({"mLock"})
    private boolean mAllowedToRetrieve;

    @GuardedBy({"mLock"})
    private boolean mCriticalLevelLogged;
    private int mEnabledUser;
    private final Semaphore mForceSemaphore;

    @GuardedBy({"mLock"})
    private long mId;
    private long mLastEventNanos;
    private final ArrayList<SecurityLog.SecurityEvent> mLastEvents;

    @GuardedBy({"mForceSemaphore"})
    private long mLastForceNanos;
    private final Lock mLock;

    @GuardedBy({"mLock"})
    private Thread mMonitorThread;

    @GuardedBy({"mLock"})
    private long mNextAllowedRetrievalTimeMillis;

    @GuardedBy({"mLock"})
    private boolean mPaused;

    @GuardedBy({"mLock"})
    private ArrayList<SecurityLog.SecurityEvent> mPendingLogs;
    private final DevicePolicyManagerService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SecurityLogMonitor(DevicePolicyManagerService devicePolicyManagerService) {
        this(devicePolicyManagerService, 0L);
    }

    @VisibleForTesting
    SecurityLogMonitor(DevicePolicyManagerService devicePolicyManagerService, long j) {
        this.mLock = new ReentrantLock();
        this.mMonitorThread = null;
        this.mPendingLogs = new ArrayList<>();
        this.mAllowedToRetrieve = false;
        this.mCriticalLevelLogged = false;
        this.mLastEvents = new ArrayList<>();
        this.mLastEventNanos = -1L;
        this.mNextAllowedRetrievalTimeMillis = -1L;
        this.mPaused = false;
        this.mForceSemaphore = new Semaphore(0);
        this.mLastForceNanos = 0L;
        this.mService = devicePolicyManagerService;
        this.mId = j;
        this.mLastForceNanos = System.nanoTime();
    }

    static {
        TimeUnit timeUnit = TimeUnit.MINUTES;
        BROADCAST_RETRY_INTERVAL_MS = timeUnit.toMillis(30L);
        POLLING_INTERVAL_MS = timeUnit.toMillis(1L);
        TimeUnit timeUnit2 = TimeUnit.SECONDS;
        OVERLAP_NS = timeUnit2.toNanos(3L);
        FORCE_FETCH_THROTTLE_NS = timeUnit2.toNanos(10L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start(int i) {
        Slog.i(TAG, "Starting security logging for user " + i);
        this.mEnabledUser = i;
        SecurityLog.writeEvent(210011, new Object[0]);
        this.mLock.lock();
        try {
            if (this.mMonitorThread == null) {
                this.mPendingLogs = new ArrayList<>();
                this.mCriticalLevelLogged = false;
                this.mId = 0L;
                this.mAllowedToRetrieve = false;
                this.mNextAllowedRetrievalTimeMillis = -1L;
                this.mPaused = false;
                Thread thread = new Thread(this);
                this.mMonitorThread = thread;
                thread.start();
            }
        } finally {
            this.mLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stop() {
        Slog.i(TAG, "Stopping security logging.");
        SecurityLog.writeEvent(210012, new Object[0]);
        this.mLock.lock();
        try {
            Thread thread = this.mMonitorThread;
            if (thread != null) {
                thread.interrupt();
                try {
                    this.mMonitorThread.join(TimeUnit.SECONDS.toMillis(5L));
                } catch (InterruptedException e) {
                    Log.e(TAG, "Interrupted while waiting for thread to stop", e);
                }
                this.mPendingLogs = new ArrayList<>();
                this.mId = 0L;
                this.mAllowedToRetrieve = false;
                this.mNextAllowedRetrievalTimeMillis = -1L;
                this.mPaused = false;
                this.mMonitorThread = null;
            }
        } finally {
            this.mLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pause() {
        Slog.i(TAG, "Paused.");
        this.mLock.lock();
        this.mPaused = true;
        this.mAllowedToRetrieve = false;
        this.mLock.unlock();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resume() {
        this.mLock.lock();
        try {
            if (!this.mPaused) {
                Log.d(TAG, "Attempted to resume, but logging is not paused.");
                return;
            }
            this.mPaused = false;
            this.mAllowedToRetrieve = false;
            this.mLock.unlock();
            Slog.i(TAG, "Resumed.");
            try {
                notifyDeviceOwnerOrProfileOwnerIfNeeded(false);
            } catch (InterruptedException e) {
                Log.w(TAG, "Thread interrupted.", e);
            }
        } finally {
            this.mLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void discardLogs() {
        this.mLock.lock();
        this.mAllowedToRetrieve = false;
        this.mPendingLogs = new ArrayList<>();
        this.mCriticalLevelLogged = false;
        this.mLock.unlock();
        Slog.i(TAG, "Discarded all logs.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<SecurityLog.SecurityEvent> retrieveLogs() {
        this.mLock.lock();
        try {
            if (!this.mAllowedToRetrieve) {
                this.mLock.unlock();
                return null;
            }
            this.mAllowedToRetrieve = false;
            this.mNextAllowedRetrievalTimeMillis = SystemClock.elapsedRealtime() + RATE_LIMIT_INTERVAL_MS;
            ArrayList<SecurityLog.SecurityEvent> arrayList = this.mPendingLogs;
            this.mPendingLogs = new ArrayList<>();
            this.mCriticalLevelLogged = false;
            return arrayList;
        } finally {
            this.mLock.unlock();
        }
    }

    private void getNextBatch(ArrayList<SecurityLog.SecurityEvent> arrayList) throws IOException {
        if (this.mLastEventNanos < 0) {
            SecurityLog.readEvents(arrayList);
        } else {
            SecurityLog.readEventsSince(this.mLastEvents.isEmpty() ? this.mLastEventNanos : Math.max(0L, this.mLastEventNanos - OVERLAP_NS), arrayList);
        }
        int i = 0;
        while (true) {
            if (i >= arrayList.size() - 1) {
                break;
            }
            long timeNanos = arrayList.get(i).getTimeNanos();
            i++;
            if (timeNanos > arrayList.get(i).getTimeNanos()) {
                arrayList.sort(new Comparator() { // from class: com.android.server.devicepolicy.SecurityLogMonitor$$ExternalSyntheticLambda0
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int lambda$getNextBatch$0;
                        lambda$getNextBatch$0 = SecurityLogMonitor.lambda$getNextBatch$0((SecurityLog.SecurityEvent) obj, (SecurityLog.SecurityEvent) obj2);
                        return lambda$getNextBatch$0;
                    }
                });
                break;
            }
        }
        SecurityLog.redactEvents(arrayList, this.mEnabledUser);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$getNextBatch$0(SecurityLog.SecurityEvent securityEvent, SecurityLog.SecurityEvent securityEvent2) {
        return Long.signum(securityEvent.getTimeNanos() - securityEvent2.getTimeNanos());
    }

    private void saveLastEvents(ArrayList<SecurityLog.SecurityEvent> arrayList) {
        this.mLastEvents.clear();
        if (arrayList.isEmpty()) {
            return;
        }
        this.mLastEventNanos = arrayList.get(arrayList.size() - 1).getTimeNanos();
        int size = arrayList.size() - 2;
        while (size >= 0 && this.mLastEventNanos - arrayList.get(size).getTimeNanos() < OVERLAP_NS) {
            size--;
        }
        this.mLastEvents.addAll(arrayList.subList(size + 1, arrayList.size()));
    }

    @GuardedBy({"mLock"})
    private void mergeBatchLocked(ArrayList<SecurityLog.SecurityEvent> arrayList) {
        ArrayList<SecurityLog.SecurityEvent> arrayList2 = this.mPendingLogs;
        arrayList2.ensureCapacity(arrayList2.size() + arrayList.size());
        int i = 0;
        int i2 = 0;
        while (i < this.mLastEvents.size() && i2 < arrayList.size()) {
            SecurityLog.SecurityEvent securityEvent = arrayList.get(i2);
            long timeNanos = securityEvent.getTimeNanos();
            if (timeNanos > this.mLastEventNanos) {
                break;
            }
            SecurityLog.SecurityEvent securityEvent2 = this.mLastEvents.get(i);
            long timeNanos2 = securityEvent2.getTimeNanos();
            if (timeNanos2 > timeNanos) {
                assignLogId(securityEvent);
                this.mPendingLogs.add(securityEvent);
            } else if (timeNanos2 < timeNanos) {
                i++;
            } else {
                if (!securityEvent2.eventEquals(securityEvent)) {
                    assignLogId(securityEvent);
                    this.mPendingLogs.add(securityEvent);
                }
                i++;
            }
            i2++;
        }
        List<SecurityLog.SecurityEvent> subList = arrayList.subList(i2, arrayList.size());
        Iterator<SecurityLog.SecurityEvent> it = subList.iterator();
        while (it.hasNext()) {
            assignLogId(it.next());
        }
        this.mPendingLogs.addAll(subList);
        checkCriticalLevel();
        if (this.mPendingLogs.size() > BUFFER_ENTRIES_MAXIMUM_LEVEL) {
            this.mPendingLogs = new ArrayList<>(this.mPendingLogs.subList(r1.size() - 5120, this.mPendingLogs.size()));
            this.mCriticalLevelLogged = false;
            Slog.i(TAG, "Pending logs buffer full. Discarding old logs.");
        }
    }

    @GuardedBy({"mLock"})
    private void checkCriticalLevel() {
        if (SecurityLog.isLoggingEnabled() && this.mPendingLogs.size() >= BUFFER_ENTRIES_CRITICAL_LEVEL && !this.mCriticalLevelLogged) {
            this.mCriticalLevelLogged = true;
            SecurityLog.writeEvent(210015, new Object[0]);
        }
    }

    @GuardedBy({"mLock"})
    private void assignLogId(SecurityLog.SecurityEvent securityEvent) {
        securityEvent.setId(this.mId);
        long j = this.mId;
        if (j == Long.MAX_VALUE) {
            Slog.i(TAG, "Reached maximum id value; wrapping around.");
            this.mId = 0L;
        } else {
            this.mId = j + 1;
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean tryAcquire;
        Process.setThreadPriority(10);
        ArrayList<SecurityLog.SecurityEvent> arrayList = new ArrayList<>();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                tryAcquire = this.mForceSemaphore.tryAcquire(POLLING_INTERVAL_MS, TimeUnit.MILLISECONDS);
                getNextBatch(arrayList);
                this.mLock.lockInterruptibly();
            } catch (IOException e) {
                Log.e(TAG, "Failed to read security log", e);
            } catch (InterruptedException e2) {
                Log.i(TAG, "Thread interrupted, exiting.", e2);
            }
            try {
                mergeBatchLocked(arrayList);
                this.mLock.unlock();
                saveLastEvents(arrayList);
                arrayList.clear();
                notifyDeviceOwnerOrProfileOwnerIfNeeded(tryAcquire);
            } catch (Throwable th) {
                this.mLock.unlock();
                throw th;
                break;
            }
        }
        this.mLastEvents.clear();
        long j = this.mLastEventNanos;
        if (j != -1) {
            this.mLastEventNanos = j + 1;
        }
        Slog.i(TAG, "MonitorThread exit.");
    }

    private void notifyDeviceOwnerOrProfileOwnerIfNeeded(boolean z) throws InterruptedException {
        this.mLock.lockInterruptibly();
        try {
            if (this.mPaused) {
                return;
            }
            int size = this.mPendingLogs.size();
            boolean z2 = (size >= 1024 || (z && size > 0)) && !this.mAllowedToRetrieve;
            if (size > 0 && SystemClock.elapsedRealtime() >= this.mNextAllowedRetrievalTimeMillis) {
                z2 = true;
            }
            if (z2) {
                this.mAllowedToRetrieve = true;
                this.mNextAllowedRetrievalTimeMillis = SystemClock.elapsedRealtime() + BROADCAST_RETRY_INTERVAL_MS;
            }
            if (z2) {
                Slog.i(TAG, "notify DO or PO");
                this.mService.sendDeviceOwnerOrProfileOwnerCommand("android.app.action.SECURITY_LOGS_AVAILABLE", null, this.mEnabledUser);
            }
        } finally {
            this.mLock.unlock();
        }
    }

    public long forceLogs() {
        long nanoTime = System.nanoTime();
        synchronized (this.mForceSemaphore) {
            long j = (this.mLastForceNanos + FORCE_FETCH_THROTTLE_NS) - nanoTime;
            if (j > 0) {
                return TimeUnit.NANOSECONDS.toMillis(j) + 1;
            }
            this.mLastForceNanos = nanoTime;
            if (this.mForceSemaphore.availablePermits() == 0) {
                this.mForceSemaphore.release();
            }
            return 0L;
        }
    }
}
