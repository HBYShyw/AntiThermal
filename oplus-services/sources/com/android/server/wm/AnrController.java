package com.android.server.wm;

import android.os.IBinder;
import android.os.SystemClock;
import android.os.Trace;
import android.util.Slog;
import android.util.SparseArray;
import android.view.InputApplicationHandle;
import com.android.internal.os.TimeoutRecord;
import com.android.server.FgThread;
import java.util.OptionalInt;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class AnrController {
    private static final long PRE_DUMP_MIN_INTERVAL_MS;
    private static final long PRE_DUMP_MONITOR_TIMEOUT_MS;
    private volatile long mLastPreDumpTimeMs;
    private final WindowManagerService mService;
    private final SparseArray<ActivityRecord> mUnresponsiveAppByDisplay = new SparseArray<>();

    private void preDumpIfLockTooSlow() {
    }

    static {
        TimeUnit timeUnit = TimeUnit.SECONDS;
        PRE_DUMP_MIN_INTERVAL_MS = timeUnit.toMillis(20L);
        PRE_DUMP_MONITOR_TIMEOUT_MS = timeUnit.toMillis(1L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnrController(WindowManagerService windowManagerService) {
        this.mService = windowManagerService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Finally extract failed */
    public void notifyAppUnresponsive(InputApplicationHandle inputApplicationHandle, TimeoutRecord timeoutRecord) {
        WindowState windowState;
        try {
            timeoutRecord.mLatencyTracker.notifyAppUnresponsiveStarted();
            timeoutRecord.mLatencyTracker.preDumpIfLockTooSlowStarted();
            preDumpIfLockTooSlow();
            timeoutRecord.mLatencyTracker.preDumpIfLockTooSlowEnded();
            timeoutRecord.mLatencyTracker.waitingOnGlobalLockStarted();
            WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    timeoutRecord.mLatencyTracker.waitingOnGlobalLockEnded();
                    ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(inputApplicationHandle.token);
                    if (forTokenLocked == null) {
                        Slog.e("WindowManager", "Unknown app appToken:" + inputApplicationHandle.name + ". Dropping notifyNoFocusedWindowAnr request");
                    } else if (forTokenLocked.mAppStopped) {
                        Slog.d("WindowManager", "App is in stopped state:" + inputApplicationHandle.name + ". Dropping notifyNoFocusedWindowAnr request");
                    } else {
                        DisplayContent displayContent = this.mService.mRoot.getDisplayContent(forTokenLocked.getDisplayId());
                        IBinder iBinder = displayContent != null ? displayContent.getInputMonitor().mInputFocus : null;
                        InputTarget inputTargetFromToken = this.mService.getInputTargetFromToken(iBinder);
                        boolean z = false;
                        if (inputTargetFromToken != null) {
                            windowState = inputTargetFromToken.getWindowState();
                            if (SystemClock.uptimeMillis() - displayContent.getInputMonitor().mInputFocusRequestTimeMillis >= ActivityTaskManagerService.getInputDispatchingTimeoutMillisLocked(windowState.getActivityRecord())) {
                                z = true;
                            }
                        } else {
                            windowState = null;
                        }
                        if (!z) {
                            Slog.i("WindowManager", "ANR in " + forTokenLocked.getName() + ".  Reason: " + timeoutRecord.mReason);
                            this.mUnresponsiveAppByDisplay.put(forTokenLocked.getDisplayId(), forTokenLocked);
                        }
                        WindowManagerService.resetPriorityAfterLockedSection();
                        if (z && notifyWindowUnresponsive(iBinder, timeoutRecord)) {
                            Slog.i("WindowManager", "Blamed " + windowState.getName() + " using pending focus request. Focused activity: " + forTokenLocked.getName());
                        } else {
                            forTokenLocked.inputDispatchingTimedOut(timeoutRecord, -1);
                        }
                        if (!z) {
                            dumpAnrStateAsync(forTokenLocked, null, timeoutRecord.mReason);
                        }
                        return;
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            timeoutRecord.mLatencyTracker.notifyAppUnresponsiveEnded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyWindowUnresponsive(IBinder iBinder, OptionalInt optionalInt, TimeoutRecord timeoutRecord) {
        try {
            timeoutRecord.mLatencyTracker.notifyWindowUnresponsiveStarted();
            if (!notifyWindowUnresponsive(iBinder, timeoutRecord)) {
                if (!optionalInt.isPresent()) {
                    Slog.w("WindowManager", "Failed to notify that window token=" + iBinder + " was unresponsive.");
                } else {
                    notifyWindowUnresponsive(optionalInt.getAsInt(), timeoutRecord);
                }
            }
        } finally {
            timeoutRecord.mLatencyTracker.notifyWindowUnresponsiveEnded();
        }
    }

    private boolean notifyWindowUnresponsive(IBinder iBinder, TimeoutRecord timeoutRecord) {
        timeoutRecord.mLatencyTracker.preDumpIfLockTooSlowStarted();
        preDumpIfLockTooSlow();
        timeoutRecord.mLatencyTracker.preDumpIfLockTooSlowEnded();
        timeoutRecord.mLatencyTracker.waitingOnGlobalLockStarted();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                timeoutRecord.mLatencyTracker.waitingOnGlobalLockEnded();
                InputTarget inputTargetFromToken = this.mService.getInputTargetFromToken(iBinder);
                if (inputTargetFromToken == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                WindowState windowState = inputTargetFromToken.getWindowState();
                int pid = inputTargetFromToken.getPid();
                ActivityRecord activityRecord = windowState.mInputChannelToken == iBinder ? windowState.mActivityRecord : null;
                Slog.i("WindowManager", "ANR in " + inputTargetFromToken + ". Reason:" + timeoutRecord.mReason);
                boolean isWindowAboveSystem = isWindowAboveSystem(windowState);
                WindowManagerService.resetPriorityAfterLockedSection();
                if (activityRecord != null) {
                    activityRecord.inputDispatchingTimedOut(timeoutRecord, pid);
                } else {
                    this.mService.mAmInternal.inputDispatchingTimedOut(pid, isWindowAboveSystem, timeoutRecord);
                }
                dumpAnrStateAsync(activityRecord, windowState, timeoutRecord.mReason);
                return true;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void notifyWindowUnresponsive(int i, TimeoutRecord timeoutRecord) {
        Slog.i("WindowManager", "ANR in input window owned by pid=" + i + ". Reason: " + timeoutRecord.mReason);
        this.mService.mAmInternal.inputDispatchingTimedOut(i, true, timeoutRecord);
        dumpAnrStateAsync(null, null, timeoutRecord.mReason);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyWindowResponsive(IBinder iBinder, OptionalInt optionalInt) {
        if (notifyWindowResponsive(iBinder)) {
            return;
        }
        if (!optionalInt.isPresent()) {
            Slog.w("WindowManager", "Failed to notify that window token=" + iBinder + " was responsive.");
            return;
        }
        notifyWindowResponsive(optionalInt.getAsInt());
    }

    private boolean notifyWindowResponsive(IBinder iBinder) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                InputTarget inputTargetFromToken = this.mService.getInputTargetFromToken(iBinder);
                if (inputTargetFromToken == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                int pid = inputTargetFromToken.getPid();
                WindowManagerService.resetPriorityAfterLockedSection();
                this.mService.mAmInternal.inputDispatchingResumed(pid);
                return true;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void notifyWindowResponsive(int i) {
        this.mService.mAmInternal.inputDispatchingResumed(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onFocusChanged(WindowState windowState) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord activityRecord = this.mUnresponsiveAppByDisplay.get(windowState.getDisplayId());
                if (activityRecord != null && activityRecord == windowState.mActivityRecord) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    this.mService.mAmInternal.inputDispatchingResumed(activityRecord.getPid());
                    this.mUnresponsiveAppByDisplay.remove(windowState.getDisplayId());
                    return;
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* renamed from: com.android.server.wm.AnrController$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    class AnonymousClass1 extends Thread {
        final /* synthetic */ CountDownLatch val$latch;
        final /* synthetic */ Runnable val$monitor;
        final /* synthetic */ String val$name;
        final /* synthetic */ long val$now;
        final /* synthetic */ boolean[] val$shouldDumpSf;

        AnonymousClass1(Runnable runnable, CountDownLatch countDownLatch, long j, String str, boolean[] zArr) {
            this.val$monitor = runnable;
            this.val$latch = countDownLatch;
            this.val$now = j;
            this.val$name = str;
            this.val$shouldDumpSf = zArr;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            this.val$monitor.run();
            this.val$latch.countDown();
            long uptimeMillis = SystemClock.uptimeMillis() - this.val$now;
            if (uptimeMillis > AnrController.PRE_DUMP_MONITOR_TIMEOUT_MS) {
                Slog.i("WindowManager", "Pre-dump acquired " + this.val$name + " in " + uptimeMillis + "ms");
                return;
            }
            if ("WindowManager".equals(this.val$name)) {
                this.val$shouldDumpSf[0] = false;
            }
        }
    }

    private void dumpAnrStateAsync(final ActivityRecord activityRecord, final WindowState windowState, final String str) {
        FgThread.getExecutor().execute(new Runnable() { // from class: com.android.server.wm.AnrController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AnrController.this.lambda$dumpAnrStateAsync$0(activityRecord, windowState, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpAnrStateAsync$0(ActivityRecord activityRecord, WindowState windowState, String str) {
        try {
            Trace.traceBegin(64L, "dumpAnrStateLocked()");
            WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    Slog.i("WindowManager", "dumpAnrStateAsync windowState num: " + this.mService.mWindowMap.size());
                    this.mService.saveANRStateLocked(activityRecord, windowState, str);
                    this.mService.mAtmService.saveANRState(str);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            Trace.traceEnd(64L);
        }
    }

    private boolean isWindowAboveSystem(WindowState windowState) {
        return windowState.mBaseLayer > this.mService.mPolicy.getWindowLayerFromTypeLw(2038, windowState.mOwnerCanAddInternalSystemWindow);
    }
}
