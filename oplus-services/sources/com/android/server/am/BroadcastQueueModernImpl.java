package com.android.server.am;

import android.app.BroadcastOptions;
import android.app.IApplicationThread;
import android.app.UidObserver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.res.CompatibilityInfo;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.os.Bundle;
import android.os.BundleMerger;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.MathUtils;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import com.android.internal.os.TimeoutRecord;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.am.BroadcastProcessQueue;
import dalvik.annotation.optimization.NeverCompile;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BroadcastQueueModernImpl extends BroadcastQueue {
    private static final int MSG_BG_ACTIVITY_START_TIMEOUT = 4;
    private static final int MSG_CHECK_HEALTH = 5;
    private static final int MSG_CHECK_PENDING_COLD_START_VALIDITY = 6;
    private static final int MSG_DELIVERY_TIMEOUT_HARD = 3;
    private static final int MSG_DELIVERY_TIMEOUT_SOFT = 2;
    private static final int MSG_PROCESS_FREEZABLE_CHANGED = 7;
    private static final int MSG_UID_STATE_CHANGED = 8;
    private static final int MSG_UPDATE_RUNNING_LIST = 1;
    private final BroadcastConstants mBgConstants;

    @VisibleForTesting
    final BroadcastProcessQueue.BroadcastConsumer mBroadcastConsumerDeferApply;

    @VisibleForTesting
    final BroadcastProcessQueue.BroadcastConsumer mBroadcastConsumerDeferClear;
    private final BroadcastProcessQueue.BroadcastConsumer mBroadcastConsumerSkip;
    private final BroadcastProcessQueue.BroadcastConsumer mBroadcastConsumerSkipAndCanceled;
    private IBroadcastQueueModernImplExt mBroadcastQueueModernImplExt;
    private final BroadcastConstants mConstants;
    private final BroadcastConstants mFgConstants;
    private long mLastTestFailureTime;
    private final Handler.Callback mLocalCallback;
    private final Handler mLocalHandler;

    @GuardedBy({"mService"})
    private final AtomicReference<ArrayMap<BroadcastRecord, Boolean>> mMatchingRecordsCache;

    @GuardedBy({"mService"})
    private final SparseArray<BroadcastProcessQueue> mProcessQueues;

    @GuardedBy({"mService"})
    private final AtomicReference<ArrayMap<BroadcastRecord, Boolean>> mRecordsLookupCache;

    @GuardedBy({"mService"})
    private final AtomicReference<ArraySet<BroadcastRecord>> mReplacedBroadcastsCache;
    private BroadcastProcessQueue mRunnableHead;

    @GuardedBy({"mService"})
    private final BroadcastProcessQueue[] mRunning;

    @GuardedBy({"mService"})
    private BroadcastProcessQueue mRunningColdStart;

    @GuardedBy({"mService"})
    private final SparseBooleanArray mUidForeground;

    @GuardedBy({"mService"})
    private final ArrayList<Pair<BooleanSupplier, CountDownLatch>> mWaitingFor;
    private BroadcastQueueWrapper mWrapper;
    private static final Predicate<BroadcastProcessQueue> QUEUE_PREDICATE_ANY = new Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda17
        @Override // java.util.function.Predicate
        public final boolean test(Object obj) {
            boolean lambda$static$8;
            lambda$static$8 = BroadcastQueueModernImpl.lambda$static$8((BroadcastProcessQueue) obj);
            return lambda$static$8;
        }
    };
    private static final BroadcastProcessQueue.BroadcastPredicate BROADCAST_PREDICATE_ANY = new BroadcastProcessQueue.BroadcastPredicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda18
        @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
        public final boolean test(BroadcastRecord broadcastRecord, int i) {
            boolean lambda$static$9;
            lambda$static$9 = BroadcastQueueModernImpl.lambda$static$9(broadcastRecord, i);
            return lambda$static$9;
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$static$8(BroadcastProcessQueue broadcastProcessQueue) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$static$9(BroadcastRecord broadcastRecord, int i) {
        return true;
    }

    @Override // com.android.server.am.BroadcastQueue
    public void backgroundServicesFinishedLocked(int i) {
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean isDelayBehindServices() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BroadcastQueueModernImpl(ActivityManagerService activityManagerService, Handler handler, BroadcastConstants broadcastConstants, BroadcastConstants broadcastConstants2) {
        this(activityManagerService, handler, broadcastConstants, broadcastConstants2, new BroadcastSkipPolicy(activityManagerService), new BroadcastHistory(broadcastConstants));
    }

    BroadcastQueueModernImpl(ActivityManagerService activityManagerService, Handler handler, BroadcastConstants broadcastConstants, BroadcastConstants broadcastConstants2, BroadcastSkipPolicy broadcastSkipPolicy, BroadcastHistory broadcastHistory) {
        super(activityManagerService, handler, "modern", broadcastSkipPolicy, broadcastHistory);
        this.mProcessQueues = new SparseArray<>();
        this.mRunnableHead = null;
        this.mWaitingFor = new ArrayList<>();
        this.mReplacedBroadcastsCache = new AtomicReference<>();
        this.mRecordsLookupCache = new AtomicReference<>();
        this.mMatchingRecordsCache = new AtomicReference<>();
        this.mUidForeground = new SparseBooleanArray();
        Handler.Callback callback = new Handler.Callback() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda7
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(Message message) {
                boolean lambda$new$0;
                lambda$new$0 = BroadcastQueueModernImpl.this.lambda$new$0(message);
                return lambda$new$0;
            }
        };
        this.mLocalCallback = callback;
        this.mBroadcastConsumerSkip = new BroadcastProcessQueue.BroadcastConsumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda8
            @Override // com.android.server.am.BroadcastProcessQueue.BroadcastConsumer
            public final void accept(BroadcastRecord broadcastRecord, int i) {
                BroadcastQueueModernImpl.this.lambda$new$10(broadcastRecord, i);
            }
        };
        this.mBroadcastConsumerSkipAndCanceled = new BroadcastProcessQueue.BroadcastConsumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda9
            @Override // com.android.server.am.BroadcastProcessQueue.BroadcastConsumer
            public final void accept(BroadcastRecord broadcastRecord, int i) {
                BroadcastQueueModernImpl.this.lambda$new$11(broadcastRecord, i);
            }
        };
        this.mBroadcastConsumerDeferApply = new BroadcastProcessQueue.BroadcastConsumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda10
            @Override // com.android.server.am.BroadcastProcessQueue.BroadcastConsumer
            public final void accept(BroadcastRecord broadcastRecord, int i) {
                BroadcastQueueModernImpl.this.lambda$new$12(broadcastRecord, i);
            }
        };
        this.mBroadcastConsumerDeferClear = new BroadcastProcessQueue.BroadcastConsumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda11
            @Override // com.android.server.am.BroadcastProcessQueue.BroadcastConsumer
            public final void accept(BroadcastRecord broadcastRecord, int i) {
                BroadcastQueueModernImpl.this.lambda$new$13(broadcastRecord, i);
            }
        };
        this.mWrapper = new BroadcastQueueWrapper();
        this.mBroadcastQueueModernImplExt = (IBroadcastQueueModernImplExt) ExtLoader.type(IBroadcastQueueModernImplExt.class).base(this).create();
        Objects.requireNonNull(broadcastConstants);
        this.mConstants = broadcastConstants;
        this.mFgConstants = broadcastConstants;
        Objects.requireNonNull(broadcastConstants2);
        this.mBgConstants = broadcastConstants2;
        this.mLocalHandler = new Handler(handler.getLooper(), callback);
        this.mRunning = new BroadcastProcessQueue[broadcastConstants.getMaxRunningQueues()];
        this.mBroadcastQueueModernImplExt.initArgs(activityManagerService, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enqueueUpdateRunningList() {
        this.mLocalHandler.removeMessages(1);
        this.mLocalHandler.sendEmptyMessage(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(Message message) {
        switch (message.what) {
            case 1:
                updateRunningList();
                return true;
            case 2:
                deliveryTimeoutSoft((BroadcastProcessQueue) message.obj, message.arg1);
                return true;
            case 3:
                deliveryTimeoutHard((BroadcastProcessQueue) message.obj);
                return true;
            case 4:
                ActivityManagerService activityManagerService = this.mService;
                ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService) {
                    try {
                        SomeArgs someArgs = (SomeArgs) message.obj;
                        ProcessRecord processRecord = (ProcessRecord) someArgs.arg1;
                        BroadcastRecord broadcastRecord = (BroadcastRecord) someArgs.arg2;
                        someArgs.recycle();
                        processRecord.removeBackgroundStartPrivileges(broadcastRecord);
                    } finally {
                        ActivityManagerService.resetPriorityAfterLockedSection();
                    }
                }
                ActivityManagerService.resetPriorityAfterLockedSection();
                return true;
            case 5:
                checkHealth();
                return true;
            case 6:
                checkPendingColdStartValidity();
                return true;
            case 7:
                ActivityManagerService activityManagerService2 = this.mService;
                ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService2) {
                    try {
                        refreshProcessQueueLocked((ProcessRecord) message.obj);
                    } finally {
                    }
                }
                ActivityManagerService.resetPriorityAfterLockedSection();
                return true;
            case 8:
                int intValue = ((Integer) message.obj).intValue();
                int i = message.arg1;
                ActivityManagerService activityManagerService3 = this.mService;
                ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService3) {
                    try {
                        if (i == 2) {
                            this.mUidForeground.put(intValue, true);
                        } else {
                            this.mUidForeground.delete(intValue);
                        }
                        refreshProcessQueuesLocked(intValue);
                    } finally {
                    }
                }
                ActivityManagerService.resetPriorityAfterLockedSection();
                return true;
            default:
                return false;
        }
    }

    private int getRunningSize() {
        int i = 0;
        int i2 = 0;
        while (true) {
            BroadcastProcessQueue[] broadcastProcessQueueArr = this.mRunning;
            if (i >= broadcastProcessQueueArr.length) {
                return i2;
            }
            if (broadcastProcessQueueArr[i] != null) {
                i2++;
            }
            i++;
        }
    }

    private int getRunningUrgentCount() {
        int i = 0;
        int i2 = 0;
        while (true) {
            BroadcastProcessQueue[] broadcastProcessQueueArr = this.mRunning;
            if (i >= broadcastProcessQueueArr.length) {
                return i2;
            }
            BroadcastProcessQueue broadcastProcessQueue = broadcastProcessQueueArr[i];
            if (broadcastProcessQueue != null && broadcastProcessQueue.getActive().isUrgent()) {
                i2++;
            }
            i++;
        }
    }

    private int getRunningIndexOf(BroadcastProcessQueue broadcastProcessQueue) {
        int i = 0;
        while (true) {
            BroadcastProcessQueue[] broadcastProcessQueueArr = this.mRunning;
            if (i >= broadcastProcessQueueArr.length) {
                return -1;
            }
            if (broadcastProcessQueueArr[i] == broadcastProcessQueue) {
                return i;
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mService"})
    public void updateRunnableList(BroadcastProcessQueue broadcastProcessQueue) {
        if (getRunningIndexOf(broadcastProcessQueue) >= 0) {
            return;
        }
        broadcastProcessQueue.updateDeferredStates(this.mBroadcastConsumerDeferApply, this.mBroadcastConsumerDeferClear);
        broadcastProcessQueue.updateRunnableAt();
        boolean isRunnable = broadcastProcessQueue.isRunnable();
        BroadcastProcessQueue broadcastProcessQueue2 = this.mRunnableHead;
        boolean z = (broadcastProcessQueue != broadcastProcessQueue2 && broadcastProcessQueue.runnableAtPrev == null && broadcastProcessQueue.runnableAtNext == null) ? false : true;
        if (isRunnable) {
            if (z) {
                BroadcastProcessQueue broadcastProcessQueue3 = broadcastProcessQueue.runnableAtPrev;
                boolean z2 = broadcastProcessQueue3 == null || broadcastProcessQueue3.getRunnableAt() <= broadcastProcessQueue.getRunnableAt();
                BroadcastProcessQueue broadcastProcessQueue4 = broadcastProcessQueue.runnableAtNext;
                boolean z3 = broadcastProcessQueue4 == null || broadcastProcessQueue4.getRunnableAt() >= broadcastProcessQueue.getRunnableAt();
                if (!z2 || !z3) {
                    BroadcastProcessQueue removeFromRunnableList = BroadcastProcessQueue.removeFromRunnableList(this.mRunnableHead, broadcastProcessQueue);
                    this.mRunnableHead = removeFromRunnableList;
                    this.mRunnableHead = BroadcastProcessQueue.insertIntoRunnableList(removeFromRunnableList, broadcastProcessQueue);
                }
            } else {
                this.mRunnableHead = BroadcastProcessQueue.insertIntoRunnableList(broadcastProcessQueue2, broadcastProcessQueue);
            }
        } else if (z) {
            this.mRunnableHead = BroadcastProcessQueue.removeFromRunnableList(broadcastProcessQueue2, broadcastProcessQueue);
        }
        if (!broadcastProcessQueue.isEmpty() || broadcastProcessQueue.isActive() || broadcastProcessQueue.isProcessWarm()) {
            return;
        }
        removeProcessQueue(broadcastProcessQueue.processName, broadcastProcessQueue.uid);
    }

    private void updateRunningList() {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                updateRunningListLocked();
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00ea  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00e4  */
    @GuardedBy({"mService"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateRunningListLocked() {
        boolean scheduleReceiverColdLocked;
        int length = (this.mRunning.length - getRunningSize()) - Math.min(getRunningUrgentCount(), this.mConstants.EXTRA_RUNNING_URGENT_PROCESS_QUEUES);
        if (length == 0) {
            return;
        }
        int traceBegin = BroadcastQueue.traceBegin("updateRunningList");
        long uptimeMillis = SystemClock.uptimeMillis();
        boolean z = !this.mWaitingFor.isEmpty();
        this.mLocalHandler.removeMessages(1);
        BroadcastProcessQueue broadcastProcessQueue = this.mRunnableHead;
        boolean z2 = false;
        while (true) {
            if (broadcastProcessQueue == null || length <= 0) {
                break;
            }
            BroadcastProcessQueue broadcastProcessQueue2 = broadcastProcessQueue.runnableAtNext;
            long runnableAt = broadcastProcessQueue.getRunnableAt();
            if (broadcastProcessQueue.isRunnable() && (getRunningSize() < this.mConstants.MAX_RUNNING_PROCESS_QUEUES || broadcastProcessQueue.isPendingUrgent())) {
                if (runnableAt > uptimeMillis && !z) {
                    this.mLocalHandler.sendEmptyMessageAtTime(1, runnableAt);
                    break;
                }
                broadcastProcessQueue.clearDeferredStates(this.mBroadcastConsumerDeferClear);
                updateWarmProcess(broadcastProcessQueue);
                boolean isProcessWarm = broadcastProcessQueue.isProcessWarm();
                if (isProcessWarm) {
                    this.mService.mOomAdjuster.unfreezeTemporarily(broadcastProcessQueue.app, 3);
                    if (!broadcastProcessQueue.isProcessWarm()) {
                        enqueueUpdateRunningList();
                    }
                    if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                        BroadcastQueue.logv("Promoting " + broadcastProcessQueue + " runnable at " + broadcastProcessQueue.getRunnableAt() + " reason " + broadcastProcessQueue.getRunnableAtReason() + " from runnable to running; process is " + broadcastProcessQueue.app);
                    }
                    promoteToRunningLocked(broadcastProcessQueue);
                    if (!isProcessWarm) {
                        z2 |= broadcastProcessQueue.runningOomAdjusted;
                        try {
                            scheduleReceiverColdLocked = scheduleReceiverWarmLocked(broadcastProcessQueue);
                        } catch (BroadcastDeliveryFailedException unused) {
                            reEnqueueActiveBroadcast(broadcastProcessQueue);
                            scheduleReceiverColdLocked = true;
                        }
                    } else {
                        scheduleReceiverColdLocked = scheduleReceiverColdLocked(broadcastProcessQueue);
                    }
                    if (scheduleReceiverColdLocked) {
                        demoteFromRunningLocked(broadcastProcessQueue);
                    }
                    length--;
                } else {
                    if (this.mRunningColdStart == null) {
                        this.mRunningColdStart = broadcastProcessQueue;
                    } else if (!isPendingColdStartValid()) {
                        clearInvalidPendingColdStart();
                        this.mRunningColdStart = broadcastProcessQueue;
                    }
                    if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                    }
                    promoteToRunningLocked(broadcastProcessQueue);
                    if (!isProcessWarm) {
                    }
                    if (scheduleReceiverColdLocked) {
                    }
                    length--;
                }
            }
            broadcastProcessQueue = broadcastProcessQueue2;
        }
        if (z2) {
            this.mService.updateOomAdjPendingTargetsLocked(3);
        }
        checkPendingColdStartValidity();
        checkAndRemoveWaitingFor();
        BroadcastQueue.traceEnd(traceBegin);
    }

    private boolean isPendingColdStartValid() {
        if (this.mRunningColdStart.app.getPid() > 0) {
            return !this.mRunningColdStart.app.isKilled();
        }
        return this.mRunningColdStart.app.isPendingStart();
    }

    private void clearInvalidPendingColdStart() {
        BroadcastQueue.logw("Clearing invalid pending cold start: " + this.mRunningColdStart);
        this.mRunningColdStart.reEnqueueActiveBroadcast();
        demoteFromRunningLocked(this.mRunningColdStart);
        clearRunningColdStart();
        enqueueUpdateRunningList();
    }

    private void checkPendingColdStartValidity() {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                if (this.mRunningColdStart == null) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                if (isPendingColdStartValid()) {
                    this.mLocalHandler.sendEmptyMessageDelayed(6, this.mConstants.PENDING_COLD_START_CHECK_INTERVAL_MILLIS);
                } else {
                    clearInvalidPendingColdStart();
                }
                ActivityManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void reEnqueueActiveBroadcast(BroadcastProcessQueue broadcastProcessQueue) {
        BroadcastQueue.checkState(broadcastProcessQueue.isActive(), "isActive");
        BroadcastRecord active = broadcastProcessQueue.getActive();
        int activeIndex = broadcastProcessQueue.getActiveIndex();
        setDeliveryState(broadcastProcessQueue, broadcastProcessQueue.app, active, activeIndex, active.receivers.get(activeIndex), 0, "reEnqueueActiveBroadcast");
        broadcastProcessQueue.reEnqueueActiveBroadcast();
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean onApplicationAttachedLocked(ProcessRecord processRecord) throws BroadcastDeliveryFailedException {
        if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            BroadcastQueue.logv("Process " + processRecord + " is attached");
        }
        BroadcastProcessQueue processQueue = getProcessQueue(processRecord);
        if (processQueue != null) {
            setQueueProcess(processQueue, processRecord);
        }
        BroadcastProcessQueue broadcastProcessQueue = this.mRunningColdStart;
        if (broadcastProcessQueue == null || broadcastProcessQueue != processQueue) {
            return false;
        }
        this.mRunningColdStart = null;
        notifyStartedRunning(processQueue);
        this.mService.updateOomAdjPendingTargetsLocked(3);
        processQueue.traceProcessEnd();
        processQueue.traceProcessRunningBegin();
        try {
            if (scheduleReceiverWarmLocked(processQueue)) {
                demoteFromRunningLocked(processQueue);
            }
            enqueueUpdateRunningList();
            return true;
        } catch (BroadcastDeliveryFailedException e) {
            reEnqueueActiveBroadcast(processQueue);
            demoteFromRunningLocked(processQueue);
            throw e;
        }
    }

    @Override // com.android.server.am.BroadcastQueue
    public void onApplicationTimeoutLocked(ProcessRecord processRecord) {
        onApplicationCleanupLocked(processRecord);
    }

    @Override // com.android.server.am.BroadcastQueue
    public void onApplicationProblemLocked(ProcessRecord processRecord) {
        onApplicationCleanupLocked(processRecord);
    }

    @Override // com.android.server.am.BroadcastQueue
    public void onApplicationCleanupLocked(ProcessRecord processRecord) {
        if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            BroadcastQueue.logv("Process " + processRecord + " is cleaned up");
        }
        BroadcastProcessQueue processQueue = getProcessQueue(processRecord);
        BroadcastProcessQueue broadcastProcessQueue = this.mRunningColdStart;
        if (broadcastProcessQueue != null && broadcastProcessQueue == processQueue && broadcastProcessQueue.app == processRecord) {
            clearRunningColdStart();
        }
        if (processQueue == null || processQueue.app != processRecord) {
            return;
        }
        setQueueProcess(processQueue, null);
        if (processQueue.isActive()) {
            finishReceiverActiveLocked(processQueue, 5, "onApplicationCleanupLocked");
            demoteFromRunningLocked(processQueue);
        }
        if (processQueue.forEachMatchingBroadcast(new BroadcastProcessQueue.BroadcastPredicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda5
            @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
            public final boolean test(BroadcastRecord broadcastRecord, int i) {
                boolean lambda$onApplicationCleanupLocked$1;
                lambda$onApplicationCleanupLocked$1 = BroadcastQueueModernImpl.lambda$onApplicationCleanupLocked$1(broadcastRecord, i);
                return lambda$onApplicationCleanupLocked$1;
            }
        }, this.mBroadcastConsumerSkip, true) || processQueue.isEmpty()) {
            updateRunnableList(processQueue);
            enqueueUpdateRunningList();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onApplicationCleanupLocked$1(BroadcastRecord broadcastRecord, int i) {
        return broadcastRecord.receivers.get(i) instanceof BroadcastFilter;
    }

    private void clearRunningColdStart() {
        this.mRunningColdStart.traceProcessEnd();
        this.mRunningColdStart = null;
        enqueueUpdateRunningList();
    }

    @Override // com.android.server.am.BroadcastQueue
    public void onProcessFreezableChangedLocked(ProcessRecord processRecord) {
        this.mLocalHandler.removeMessages(7, processRecord);
        this.mLocalHandler.obtainMessage(7, processRecord).sendToTarget();
    }

    @Override // com.android.server.am.BroadcastQueue
    public int getPreferredSchedulingGroupLocked(ProcessRecord processRecord) {
        BroadcastProcessQueue processQueue = getProcessQueue(processRecord);
        if (processQueue == null || getRunningIndexOf(processQueue) < 0) {
            return Integer.MIN_VALUE;
        }
        return processQueue.getPreferredSchedulingGroupLocked();
    }

    @Override // com.android.server.am.BroadcastQueue
    public void enqueueBroadcastLocked(BroadcastRecord broadcastRecord) {
        if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            BroadcastQueue.logv("Enqueuing " + broadcastRecord + " for " + broadcastRecord.receivers.size() + " receivers");
        }
        int traceBegin = BroadcastQueue.traceBegin("enqueueBroadcast");
        broadcastRecord.applySingletonPolicy(this.mService);
        applyDeliveryGroupPolicy(broadcastRecord);
        broadcastRecord.enqueueTime = SystemClock.uptimeMillis();
        broadcastRecord.enqueueRealTime = SystemClock.elapsedRealtime();
        broadcastRecord.enqueueClockTime = System.currentTimeMillis();
        this.mHistory.onBroadcastEnqueuedLocked(broadcastRecord);
        ArraySet<BroadcastRecord> andSet = this.mReplacedBroadcastsCache.getAndSet(null);
        if (andSet == null) {
            andSet = new ArraySet<>();
        }
        ArrayMap<BroadcastRecord, Boolean> andSet2 = this.mMatchingRecordsCache.getAndSet(null);
        if (andSet2 == null) {
            andSet2 = new ArrayMap<>();
        }
        broadcastRecord.setMatchingRecordsCache(andSet2);
        boolean z = false;
        for (int i = 0; i < broadcastRecord.receivers.size(); i++) {
            Object obj = broadcastRecord.receivers.get(i);
            BroadcastProcessQueue orCreateProcessQueue = getOrCreateProcessQueue(BroadcastRecord.getReceiverProcessName(obj), BroadcastRecord.getReceiverUid(obj));
            String shouldSkipMessage = this.mSkipPolicy.shouldSkipMessage(broadcastRecord, obj);
            if (shouldSkipMessage != null) {
                setDeliveryState(null, null, broadcastRecord, i, obj, 2, "skipped by policy at enqueue: " + shouldSkipMessage);
            } else {
                BroadcastRecord enqueueOrReplaceBroadcast = orCreateProcessQueue.enqueueOrReplaceBroadcast(broadcastRecord, i, this.mBroadcastConsumerDeferApply);
                this.mBroadcastQueueModernImplExt.handleEnqueuedBroadcastOption(broadcastRecord, 2, i);
                if (enqueueOrReplaceBroadcast != null) {
                    andSet.add(enqueueOrReplaceBroadcast);
                }
                updateRunnableList(orCreateProcessQueue);
                enqueueUpdateRunningList();
                z = true;
            }
        }
        skipAndCancelReplacedBroadcasts(andSet);
        andSet.clear();
        this.mReplacedBroadcastsCache.compareAndSet(null, andSet);
        andSet2.clear();
        broadcastRecord.clearMatchingRecordsCache();
        this.mMatchingRecordsCache.compareAndSet(null, andSet2);
        if (broadcastRecord.receivers.isEmpty() || !z) {
            scheduleResultTo(broadcastRecord);
            notifyFinishBroadcast(broadcastRecord);
        }
        BroadcastQueue.traceEnd(traceBegin);
    }

    private void skipAndCancelReplacedBroadcasts(ArraySet<BroadcastRecord> arraySet) {
        for (int i = 0; i < arraySet.size(); i++) {
            BroadcastRecord valueAt = arraySet.valueAt(i);
            for (int i2 = 0; i2 < valueAt.receivers.size(); i2++) {
                if (!BroadcastRecord.isDeliveryStateTerminal(valueAt.getDeliveryState(i2))) {
                    this.mBroadcastConsumerSkipAndCanceled.accept(valueAt, i2);
                }
            }
        }
    }

    private void applyDeliveryGroupPolicy(final BroadcastRecord broadcastRecord) {
        int deliveryGroupPolicy;
        BroadcastProcessQueue.BroadcastConsumer broadcastConsumer;
        final BundleMerger deliveryGroupExtrasMerger;
        if (this.mService.shouldIgnoreDeliveryGroupPolicy(broadcastRecord.intent.getAction()) || (deliveryGroupPolicy = broadcastRecord.getDeliveryGroupPolicy()) == 0) {
            return;
        }
        if (deliveryGroupPolicy == 1) {
            broadcastConsumer = this.mBroadcastConsumerSkipAndCanceled;
        } else if (deliveryGroupPolicy == 2) {
            if (broadcastRecord.receivers.size() > 1 || (deliveryGroupExtrasMerger = broadcastRecord.options.getDeliveryGroupExtrasMerger()) == null) {
                return;
            } else {
                broadcastConsumer = new BroadcastProcessQueue.BroadcastConsumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda15
                    @Override // com.android.server.am.BroadcastProcessQueue.BroadcastConsumer
                    public final void accept(BroadcastRecord broadcastRecord2, int i) {
                        BroadcastQueueModernImpl.this.lambda$applyDeliveryGroupPolicy$2(broadcastRecord, deliveryGroupExtrasMerger, broadcastRecord2, i);
                    }
                };
            }
        } else {
            BroadcastQueue.logw("Unknown delivery group policy: " + deliveryGroupPolicy);
            return;
        }
        final ArrayMap<BroadcastRecord, Boolean> recordsLookupCache = getRecordsLookupCache();
        forEachMatchingBroadcast(QUEUE_PREDICATE_ANY, new BroadcastProcessQueue.BroadcastPredicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda16
            @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
            public final boolean test(BroadcastRecord broadcastRecord2, int i) {
                boolean lambda$applyDeliveryGroupPolicy$3;
                lambda$applyDeliveryGroupPolicy$3 = BroadcastQueueModernImpl.this.lambda$applyDeliveryGroupPolicy$3(broadcastRecord, recordsLookupCache, broadcastRecord2, i);
                return lambda$applyDeliveryGroupPolicy$3;
            }
        }, broadcastConsumer, true);
        this.mBroadcastQueueModernImplExt.handleEnqueuedBroadcastOption(broadcastRecord, 1, -1);
        recordsLookupCache.clear();
        this.mRecordsLookupCache.compareAndSet(null, recordsLookupCache);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyDeliveryGroupPolicy$2(BroadcastRecord broadcastRecord, BundleMerger bundleMerger, BroadcastRecord broadcastRecord2, int i) {
        broadcastRecord.intent.mergeExtras(broadcastRecord2.intent, bundleMerger);
        this.mBroadcastConsumerSkipAndCanceled.accept(broadcastRecord2, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$applyDeliveryGroupPolicy$3(BroadcastRecord broadcastRecord, ArrayMap arrayMap, BroadcastRecord broadcastRecord2, int i) {
        if (BroadcastRecord.isDeliveryStateTerminal(broadcastRecord2.getDeliveryState(i)) || broadcastRecord.callingUid != broadcastRecord2.callingUid || broadcastRecord.userId != broadcastRecord2.userId || !broadcastRecord.matchesDeliveryGroup(broadcastRecord2)) {
            return false;
        }
        if (broadcastRecord2.ordered || broadcastRecord2.prioritized) {
            return containsAllReceivers(broadcastRecord, broadcastRecord2, arrayMap);
        }
        if (broadcastRecord2.resultTo != null) {
            if (broadcastRecord2.getDeliveryState(i) == 6) {
                return broadcastRecord.containsReceiver(broadcastRecord2.receivers.get(i));
            }
            return containsAllReceivers(broadcastRecord, broadcastRecord2, arrayMap);
        }
        return broadcastRecord.containsReceiver(broadcastRecord2.receivers.get(i));
    }

    private ArrayMap<BroadcastRecord, Boolean> getRecordsLookupCache() {
        ArrayMap<BroadcastRecord, Boolean> andSet = this.mRecordsLookupCache.getAndSet(null);
        return andSet == null ? new ArrayMap<>() : andSet;
    }

    private boolean containsAllReceivers(BroadcastRecord broadcastRecord, BroadcastRecord broadcastRecord2, ArrayMap<BroadcastRecord, Boolean> arrayMap) {
        int indexOfKey = arrayMap.indexOfKey(broadcastRecord2);
        if (indexOfKey > 0) {
            return arrayMap.valueAt(indexOfKey).booleanValue();
        }
        boolean containsAllReceivers = broadcastRecord.containsAllReceivers(broadcastRecord2.receivers);
        arrayMap.put(broadcastRecord2, Boolean.valueOf(containsAllReceivers));
        return containsAllReceivers;
    }

    @GuardedBy({"mService"})
    private boolean scheduleReceiverColdLocked(BroadcastProcessQueue broadcastProcessQueue) {
        BroadcastQueue.checkState(broadcastProcessQueue.isActive(), "isActive");
        broadcastProcessQueue.setActiveViaColdStart(true);
        BroadcastRecord active = broadcastProcessQueue.getActive();
        int activeIndex = broadcastProcessQueue.getActiveIndex();
        Object obj = active.receivers.get(activeIndex);
        if (obj instanceof BroadcastFilter) {
            this.mRunningColdStart = null;
            finishReceiverActiveLocked(broadcastProcessQueue, 2, "BroadcastFilter for cold app");
            return true;
        }
        String shouldSkipReceiver = shouldSkipReceiver(broadcastProcessQueue, active, activeIndex);
        if (shouldSkipReceiver != null) {
            this.mRunningColdStart = null;
            finishReceiverActiveLocked(broadcastProcessQueue, 2, shouldSkipReceiver);
            return true;
        }
        ResolveInfo resolveInfo = (ResolveInfo) obj;
        String skipScheduleReceiverColdLocked = this.mBroadcastQueueModernImplExt.skipScheduleReceiverColdLocked(this, active, resolveInfo);
        if (skipScheduleReceiverColdLocked != null) {
            this.mRunningColdStart = null;
            finishReceiverActiveLocked(broadcastProcessQueue, 2, skipScheduleReceiverColdLocked);
            return true;
        }
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        ApplicationInfo applicationInfo = activityInfo.applicationInfo;
        ComponentName componentName = activityInfo.getComponentName();
        if ((applicationInfo.flags & AudioDevice.OUT_AUX_LINE) != 0) {
            broadcastProcessQueue.setActiveWasStopped(true);
        }
        int flags = active.intent.getFlags() | 4;
        HostingRecord hostingRecord = new HostingRecord("broadcast", componentName, active.intent.getAction(), active.getHostingRecordTriggerType());
        BroadcastOptions broadcastOptions = active.options;
        int i = (broadcastOptions == null || broadcastOptions.getTemporaryAppAllowlistDuration() <= 0) ? 0 : 1;
        boolean z = (active.intent.getFlags() & 33554432) != 0;
        if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            BroadcastQueue.logv("Scheduling " + active + " to cold " + broadcastProcessQueue);
        }
        ProcessRecord startProcessLocked = this.mService.startProcessLocked(broadcastProcessQueue.processName, applicationInfo, true, flags, hostingRecord, i, z, false);
        broadcastProcessQueue.app = startProcessLocked;
        if (startProcessLocked == null) {
            this.mRunningColdStart = null;
            finishReceiverActiveLocked(broadcastProcessQueue, 5, "startProcessLocked failed");
            return true;
        }
        this.mBroadcastQueueModernImplExt.hookScheduleReceiverColdAfterStartProc(active, resolveInfo);
        return false;
    }

    @GuardedBy({"mService"})
    private boolean scheduleReceiverWarmLocked(BroadcastProcessQueue broadcastProcessQueue) throws BroadcastDeliveryFailedException {
        BroadcastQueue.checkState(broadcastProcessQueue.isActive(), "isActive");
        int traceBegin = BroadcastQueue.traceBegin("scheduleReceiverWarmLocked");
        while (broadcastProcessQueue.isActive()) {
            BroadcastRecord active = broadcastProcessQueue.getActive();
            int activeIndex = broadcastProcessQueue.getActiveIndex();
            if (active.terminalCount == 0) {
                active.dispatchTime = SystemClock.uptimeMillis();
                active.dispatchRealTime = SystemClock.elapsedRealtime();
                active.dispatchClockTime = System.currentTimeMillis();
            }
            String shouldSkipReceiver = shouldSkipReceiver(broadcastProcessQueue, active, activeIndex);
            if (shouldSkipReceiver == null) {
                shouldSkipReceiver = this.mBroadcastQueueModernImplExt.skipScheduleReceiverWarmLocked(this, active, active.receivers.get(activeIndex));
            }
            if (shouldSkipReceiver == null) {
                if (dispatchReceivers(broadcastProcessQueue, active, activeIndex)) {
                    BroadcastQueue.traceEnd(traceBegin);
                    return false;
                }
            } else {
                finishReceiverActiveLocked(broadcastProcessQueue, 2, shouldSkipReceiver);
            }
            if (shouldRetire(broadcastProcessQueue)) {
                break;
            }
            broadcastProcessQueue.makeActiveNextPending();
        }
        BroadcastQueue.traceEnd(traceBegin);
        return true;
    }

    private String shouldSkipReceiver(BroadcastProcessQueue broadcastProcessQueue, BroadcastRecord broadcastRecord, int i) {
        int deliveryState = getDeliveryState(broadcastRecord, i);
        ProcessRecord processRecord = broadcastProcessQueue.app;
        Object obj = broadcastRecord.receivers.get(i);
        if (BroadcastRecord.isDeliveryStateTerminal(deliveryState)) {
            return "already terminal state";
        }
        if (processRecord != null && processRecord.isInFullBackup()) {
            return "isInFullBackup";
        }
        String shouldSkipMessage = this.mSkipPolicy.shouldSkipMessage(broadcastRecord, obj);
        if (shouldSkipMessage != null) {
            return shouldSkipMessage;
        }
        if (broadcastRecord.getReceiverIntent(obj) == null) {
            return "getReceiverIntent";
        }
        if ((obj instanceof BroadcastFilter) && ((BroadcastFilter) obj).receiverList.pid != processRecord.getPid()) {
            return "BroadcastFilter for mismatched PID";
        }
        if (this.mBroadcastQueueModernImplExt.shouldSkipReceiver(broadcastRecord, obj)) {
            return "Skipping delivery to " + broadcastProcessQueue.uid + " register due to frozen state";
        }
        if (!this.mBroadcastQueueModernImplExt.skipReceiverForOsense(broadcastRecord, obj)) {
            return null;
        }
        return "Skipping delivery to " + broadcastProcessQueue.uid + " register due to osense cpnproxy";
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x021c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean dispatchReceivers(BroadcastProcessQueue broadcastProcessQueue, BroadcastRecord broadcastRecord, int i) throws BroadcastDeliveryFailedException {
        boolean z;
        String str;
        int i2;
        String str2;
        String str3;
        ProcessRecord processRecord = broadcastProcessQueue.app;
        Object obj = broadcastRecord.receivers.get(i);
        boolean isAssumedDelivered = broadcastRecord.isAssumedDelivered(i);
        if (this.mService.mProcessesReady && !broadcastRecord.timeoutExempt && !isAssumedDelivered) {
            broadcastProcessQueue.lastCpuDelayTime = broadcastProcessQueue.app.getCpuDelayTime();
            int i3 = (int) (broadcastRecord.isForeground() ? this.mFgConstants.TIMEOUT : this.mBgConstants.TIMEOUT);
            Handler handler = this.mLocalHandler;
            handler.sendMessageDelayed(Message.obtain(handler, 2, i3, 0, broadcastProcessQueue), i3);
        }
        if (broadcastRecord.mBackgroundStartPrivileges.allowsAny()) {
            processRecord.addOrUpdateBackgroundStartPrivileges(broadcastRecord, broadcastRecord.mBackgroundStartPrivileges);
            long j = broadcastRecord.isForeground() ? this.mFgConstants.ALLOW_BG_ACTIVITY_START_TIMEOUT : this.mBgConstants.ALLOW_BG_ACTIVITY_START_TIMEOUT;
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = processRecord;
            obtain.arg2 = broadcastRecord;
            Handler handler2 = this.mLocalHandler;
            handler2.sendMessageDelayed(Message.obtain(handler2, 4, obtain), j);
        }
        BroadcastOptions broadcastOptions = broadcastRecord.options;
        if (broadcastOptions != null && broadcastOptions.getTemporaryAppAllowlistDuration() > 0) {
            if (broadcastRecord.options.getTemporaryAppAllowlistType() == 4) {
                this.mService.mOomAdjuster.mCachedAppOptimizer.unfreezeTemporarily(processRecord, 3, broadcastRecord.options.getTemporaryAppAllowlistDuration());
            } else {
                this.mService.tempAllowlistUidLocked(broadcastProcessQueue.uid, broadcastRecord.options.getTemporaryAppAllowlistDuration(), broadcastRecord.options.getTemporaryAppAllowlistReasonCode(), broadcastRecord.toShortString(), broadcastRecord.options.getTemporaryAppAllowlistType(), broadcastRecord.callingUid);
            }
        }
        if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            BroadcastQueue.logv("Scheduling " + broadcastRecord + " to warm " + processRecord);
        }
        setDeliveryState(broadcastProcessQueue, processRecord, broadcastRecord, i, obj, 4, "scheduleReceiverWarmLocked");
        Intent receiverIntent = broadcastRecord.getReceiverIntent(obj);
        IApplicationThread onewayThread = processRecord.getOnewayThread();
        if (onewayThread != null) {
            try {
                if (broadcastRecord.shareIdentity) {
                    try {
                        this.mService.mPackageManagerInt.grantImplicitAccess(broadcastRecord.userId, broadcastRecord.intent, UserHandle.getAppId(processRecord.uid), broadcastRecord.callingUid, true);
                    } catch (RemoteException e) {
                        e = e;
                        z = false;
                        BroadcastQueue.logw("Failed to schedule " + broadcastRecord + " to " + obj + " via " + processRecord + ": " + e);
                        processRecord.killLocked("Can't deliver broadcast", 13, 26, true);
                        if (obj instanceof ResolveInfo) {
                        }
                        finishReceiverActiveLocked(broadcastProcessQueue, 5, "remote app");
                        return z;
                    }
                }
                broadcastProcessQueue.lastProcessState = processRecord.mState.getCurProcState();
                try {
                    if (obj instanceof BroadcastFilter) {
                        notifyScheduleRegisteredReceiver(processRecord, broadcastRecord, (BroadcastFilter) obj);
                        IIntentReceiver iIntentReceiver = ((BroadcastFilter) obj).receiverList.receiver;
                        int i4 = broadcastRecord.resultCode;
                        String str4 = broadcastRecord.resultData;
                        Bundle bundle = broadcastRecord.resultExtras;
                        boolean z2 = broadcastRecord.ordered;
                        boolean z3 = broadcastRecord.initialSticky;
                        int i5 = broadcastRecord.userId;
                        int reportedProcState = processRecord.mState.getReportedProcState();
                        boolean z4 = broadcastRecord.shareIdentity;
                        if (z4) {
                            str = str4;
                            i2 = broadcastRecord.callingUid;
                        } else {
                            str = str4;
                            i2 = -1;
                        }
                        if (z4) {
                            str3 = broadcastRecord.callerPackage;
                            str2 = str;
                        } else {
                            str2 = str;
                            str3 = null;
                        }
                        onewayThread.scheduleRegisteredReceiver(iIntentReceiver, receiverIntent, i4, str2, bundle, z2, z3, isAssumedDelivered, i5, reportedProcState, i2, str3);
                        if (isAssumedDelivered) {
                            finishReceiverActiveLocked(broadcastProcessQueue, 1, "assuming delivered");
                            return false;
                        }
                    } else {
                        notifyScheduleReceiver(processRecord, broadcastRecord, (ResolveInfo) obj);
                        ActivityInfo activityInfo = ((ResolveInfo) obj).activityInfo;
                        int i6 = broadcastRecord.resultCode;
                        String str5 = broadcastRecord.resultData;
                        Bundle bundle2 = broadcastRecord.resultExtras;
                        boolean z5 = broadcastRecord.ordered;
                        int i7 = broadcastRecord.userId;
                        int reportedProcState2 = processRecord.mState.getReportedProcState();
                        boolean z6 = broadcastRecord.shareIdentity;
                        onewayThread.scheduleReceiver(receiverIntent, activityInfo, (CompatibilityInfo) null, i6, str5, bundle2, z5, isAssumedDelivered, i7, reportedProcState2, z6 ? broadcastRecord.callingUid : -1, z6 ? broadcastRecord.callerPackage : null);
                    }
                    IBroadcastQueueModernImplExt iBroadcastQueueModernImplExt = this.mBroadcastQueueModernImplExt;
                    ProcessRecord processRecord2 = broadcastProcessQueue.app;
                    iBroadcastQueueModernImplExt.broadcastStatistic(processRecord2, broadcastRecord, obj, this.mService.getUidStateLocked(processRecord2.uid));
                    return true;
                } catch (RemoteException e2) {
                    e = e2;
                    BroadcastQueue.logw("Failed to schedule " + broadcastRecord + " to " + obj + " via " + processRecord + ": " + e);
                    processRecord.killLocked("Can't deliver broadcast", 13, 26, true);
                    if (obj instanceof ResolveInfo) {
                        this.mLocalHandler.removeMessages(2, broadcastProcessQueue);
                        if ((e instanceof DeadObjectException) && Process.getThreadGroupLeader(processRecord.mPid) == -1) {
                            this.mService.appDiedLocked(processRecord, "Died when broadcast dispatch");
                        } else {
                            throw new BroadcastDeliveryFailedException(e);
                        }
                    }
                    finishReceiverActiveLocked(broadcastProcessQueue, 5, "remote app");
                    return z;
                }
            } catch (RemoteException e3) {
                e = e3;
                z = false;
            }
        } else {
            finishReceiverActiveLocked(broadcastProcessQueue, 5, "missing IApplicationThread");
            return false;
        }
    }

    private void scheduleResultTo(BroadcastRecord broadcastRecord) {
        int i;
        if (broadcastRecord.resultTo == null) {
            return;
        }
        ProcessRecord processRecord = broadcastRecord.resultToApp;
        IApplicationThread onewayThread = processRecord != null ? processRecord.getOnewayThread() : null;
        if (onewayThread != null) {
            this.mService.mOomAdjuster.unfreezeTemporarily(processRecord, 2);
            if (broadcastRecord.shareIdentity && (i = processRecord.uid) != broadcastRecord.callingUid) {
                this.mService.mPackageManagerInt.grantImplicitAccess(broadcastRecord.userId, broadcastRecord.intent, UserHandle.getAppId(i), broadcastRecord.callingUid, true);
            }
            try {
                IIntentReceiver iIntentReceiver = broadcastRecord.resultTo;
                Intent intent = broadcastRecord.intent;
                int i2 = broadcastRecord.resultCode;
                String str = broadcastRecord.resultData;
                Bundle bundle = broadcastRecord.resultExtras;
                boolean z = broadcastRecord.initialSticky;
                int i3 = broadcastRecord.userId;
                int reportedProcState = processRecord.mState.getReportedProcState();
                boolean z2 = broadcastRecord.shareIdentity;
                onewayThread.scheduleRegisteredReceiver(iIntentReceiver, intent, i2, str, bundle, false, z, true, i3, reportedProcState, z2 ? broadcastRecord.callingUid : -1, z2 ? broadcastRecord.callerPackage : null);
            } catch (RemoteException e) {
                BroadcastQueue.logw("Failed to schedule result of " + broadcastRecord + " via " + processRecord + ": " + e);
                processRecord.killLocked("Can't deliver broadcast", 13, 26, true);
            }
        }
        broadcastRecord.resultTo = null;
    }

    private void deliveryTimeoutSoft(BroadcastProcessQueue broadcastProcessQueue, int i) {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                deliveryTimeoutSoftLocked(broadcastProcessQueue, i);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private void deliveryTimeoutSoftLocked(BroadcastProcessQueue broadcastProcessQueue, int i) {
        ProcessRecord processRecord = broadcastProcessQueue.app;
        if (processRecord != null) {
            long constrain = MathUtils.constrain(processRecord.getCpuDelayTime() - broadcastProcessQueue.lastCpuDelayTime, 0L, i);
            Handler handler = this.mLocalHandler;
            handler.sendMessageDelayed(Message.obtain(handler, 3, broadcastProcessQueue), constrain);
            return;
        }
        deliveryTimeoutHardLocked(broadcastProcessQueue);
    }

    private void deliveryTimeoutHard(BroadcastProcessQueue broadcastProcessQueue) {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                deliveryTimeoutHardLocked(broadcastProcessQueue);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private void deliveryTimeoutHardLocked(BroadcastProcessQueue broadcastProcessQueue) {
        finishReceiverActiveLocked(broadcastProcessQueue, 3, "deliveryTimeoutHardLocked");
        demoteFromRunningLocked(broadcastProcessQueue);
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean finishReceiverLocked(ProcessRecord processRecord, int i, String str, Bundle bundle, boolean z, boolean z2) {
        BroadcastProcessQueue processQueue = getProcessQueue(processRecord);
        if (processQueue == null || !processQueue.isActive()) {
            BroadcastQueue.logw("Ignoring finishReceiverLocked; no active broadcast for " + processQueue);
            return false;
        }
        BroadcastRecord active = processQueue.getActive();
        int activeIndex = processQueue.getActiveIndex();
        if (active.ordered) {
            active.resultCode = i;
            active.resultData = str;
            active.resultExtras = bundle;
            if (!active.isNoAbort()) {
                active.resultAbort = z;
            }
        }
        finishReceiverActiveLocked(processQueue, 1, "remote app");
        if (active.resultAbort) {
            for (int i2 = activeIndex + 1; i2 < active.receivers.size(); i2++) {
                setDeliveryState(null, null, active, i2, active.receivers.get(i2), 2, "resultAbort");
            }
        }
        if (shouldRetire(processQueue)) {
            demoteFromRunningLocked(processQueue);
            return true;
        }
        processQueue.makeActiveNextPending();
        try {
            if (!scheduleReceiverWarmLocked(processQueue)) {
                return false;
            }
            demoteFromRunningLocked(processQueue);
            return true;
        } catch (BroadcastDeliveryFailedException unused) {
            reEnqueueActiveBroadcast(processQueue);
            demoteFromRunningLocked(processQueue);
            return true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x002e, code lost:
    
        if (r6.getActiveCountSinceIdle() >= r5.mConstants.MAX_RUNNING_ACTIVE_BROADCASTS) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x001f, code lost:
    
        if (r0 < r5.MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0022, code lost:
    
        r5 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean shouldRetire(BroadcastProcessQueue broadcastProcessQueue) {
        boolean z;
        if (UserHandle.isCore(broadcastProcessQueue.uid)) {
            int activeAssumedDeliveryCountSinceIdle = broadcastProcessQueue.getActiveAssumedDeliveryCountSinceIdle();
            int activeCountSinceIdle = broadcastProcessQueue.getActiveCountSinceIdle() - broadcastProcessQueue.getActiveAssumedDeliveryCountSinceIdle();
            BroadcastConstants broadcastConstants = this.mConstants;
            if (activeCountSinceIdle < broadcastConstants.MAX_CORE_RUNNING_BLOCKING_BROADCASTS) {
            }
            z = true;
        }
        return (broadcastProcessQueue.isRunnable() && broadcastProcessQueue.isProcessWarm() && !z) ? false : true;
    }

    private void finishReceiverActiveLocked(BroadcastProcessQueue broadcastProcessQueue, int i, String str) {
        if (!broadcastProcessQueue.isActive()) {
            BroadcastQueue.logw("Ignoring finishReceiverActiveLocked; no active broadcast for " + broadcastProcessQueue);
            return;
        }
        int traceBegin = BroadcastQueue.traceBegin("finishReceiver");
        ProcessRecord processRecord = broadcastProcessQueue.app;
        BroadcastRecord active = broadcastProcessQueue.getActive();
        int activeIndex = broadcastProcessQueue.getActiveIndex();
        Object obj = active.receivers.get(activeIndex);
        setDeliveryState(broadcastProcessQueue, processRecord, active, activeIndex, obj, i, str);
        if (i == 3) {
            active.anrCount++;
            this.mBroadcastQueueModernImplExt.handleBroadcastTimeout(active, processRecord, activeIndex);
            if (processRecord != null && !processRecord.isDebugging() && !this.mBroadcastQueueModernImplExt.ignoreAnr(processRecord, active)) {
                this.mService.appNotResponding(broadcastProcessQueue.app, TimeoutRecord.forBroadcastReceiver(active.intent, BroadcastRecord.getReceiverPackageName(obj), BroadcastRecord.getReceiverClassName(obj)));
            }
        } else {
            this.mLocalHandler.removeMessages(2, broadcastProcessQueue);
            this.mLocalHandler.removeMessages(3, broadcastProcessQueue);
        }
        checkAndRemoveWaitingFor();
        BroadcastQueue.traceEnd(traceBegin);
    }

    @GuardedBy({"mService"})
    private void promoteToRunningLocked(BroadcastProcessQueue broadcastProcessQueue) {
        int runningIndexOf = getRunningIndexOf(null);
        this.mRunning[runningIndexOf] = broadcastProcessQueue;
        this.mRunnableHead = BroadcastProcessQueue.removeFromRunnableList(this.mRunnableHead, broadcastProcessQueue);
        broadcastProcessQueue.runningTraceTrackName = "BroadcastQueue.mRunning[" + runningIndexOf + "]";
        broadcastProcessQueue.runningOomAdjusted = broadcastProcessQueue.isPendingManifest() || broadcastProcessQueue.isPendingOrdered() || broadcastProcessQueue.isPendingResultTo();
        boolean isProcessWarm = broadcastProcessQueue.isProcessWarm();
        if (isProcessWarm) {
            notifyStartedRunning(broadcastProcessQueue);
        }
        broadcastProcessQueue.makeActiveNextPending();
        if (isProcessWarm) {
            broadcastProcessQueue.traceProcessRunningBegin();
        } else {
            broadcastProcessQueue.traceProcessStartingBegin();
        }
    }

    @GuardedBy({"mService"})
    private void demoteFromRunningLocked(BroadcastProcessQueue broadcastProcessQueue) {
        if (!broadcastProcessQueue.isActive()) {
            BroadcastQueue.logw("Ignoring demoteFromRunning; no active broadcast for " + broadcastProcessQueue);
            return;
        }
        int traceBegin = BroadcastQueue.traceBegin("demoteFromRunning");
        broadcastProcessQueue.makeActiveIdle();
        broadcastProcessQueue.traceProcessEnd();
        this.mRunning[getRunningIndexOf(broadcastProcessQueue)] = null;
        updateRunnableList(broadcastProcessQueue);
        enqueueUpdateRunningList();
        notifyStoppedRunning(broadcastProcessQueue);
        BroadcastQueue.traceEnd(traceBegin);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDeliveryState(BroadcastProcessQueue broadcastProcessQueue, ProcessRecord processRecord, BroadcastRecord broadcastRecord, int i, Object obj, int i2, String str) {
        int traceBegin = BroadcastQueue.traceBegin("setDeliveryState");
        int deliveryState = getDeliveryState(broadcastRecord, i);
        boolean deliveryState2 = broadcastRecord.setDeliveryState(i, i2, str);
        if (broadcastProcessQueue != null) {
            if (i2 == 4) {
                broadcastProcessQueue.traceActiveBegin();
                broadcastRecord.getWrapper().setDeliveryState(i, broadcastProcessQueue.getWrapper().getRunnableAtWithoutRefresh(), broadcastProcessQueue.getWrapper().getRunnableAtReasonWithoutRefresh());
            } else if (deliveryState == 4 && BroadcastRecord.isDeliveryStateTerminal(i2)) {
                broadcastProcessQueue.traceActiveEnd();
            }
        }
        if (!BroadcastRecord.isDeliveryStateTerminal(deliveryState) && BroadcastRecord.isDeliveryStateTerminal(i2)) {
            if (ActivityManagerDebugConfig.DEBUG_BROADCAST && i2 != 1) {
                BroadcastQueue.logw("Delivery state of " + broadcastRecord + " to " + obj + " via " + processRecord + " changed from " + BroadcastRecord.deliveryStateToString(deliveryState) + " to " + BroadcastRecord.deliveryStateToString(i2) + " because " + str);
            }
            notifyFinishReceiver(broadcastProcessQueue, processRecord, broadcastRecord, i, obj);
        }
        if (deliveryState2) {
            if (broadcastRecord.beyondCount == broadcastRecord.receivers.size()) {
                scheduleResultTo(broadcastRecord);
            }
            if (broadcastRecord.ordered || broadcastRecord.prioritized) {
                for (int i3 = 0; i3 < broadcastRecord.receivers.size(); i3++) {
                    if (!BroadcastRecord.isDeliveryStateTerminal(getDeliveryState(broadcastRecord, i3)) || i3 == i) {
                        Object obj2 = broadcastRecord.receivers.get(i3);
                        BroadcastProcessQueue processQueue = getProcessQueue(BroadcastRecord.getReceiverProcessName(obj2), BroadcastRecord.getReceiverUid(obj2));
                        if (processQueue != null) {
                            processQueue.invalidateRunnableAt();
                            updateRunnableList(processQueue);
                        }
                    }
                }
                enqueueUpdateRunningList();
            }
        }
        BroadcastQueue.traceEnd(traceBegin);
    }

    private int getDeliveryState(BroadcastRecord broadcastRecord, int i) {
        return broadcastRecord.getDeliveryState(i);
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean cleanupDisabledPackageReceiversLocked(final String str, final Set<String> set, final int i) {
        Predicate<BroadcastProcessQueue> predicate;
        BroadcastProcessQueue.BroadcastPredicate broadcastPredicate;
        if (str != null) {
            final int packageUid = this.mService.mPackageManagerInt.getPackageUid(str, 8192L, i);
            predicate = new Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda19
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$cleanupDisabledPackageReceiversLocked$4;
                    lambda$cleanupDisabledPackageReceiversLocked$4 = BroadcastQueueModernImpl.lambda$cleanupDisabledPackageReceiversLocked$4(packageUid, (BroadcastProcessQueue) obj);
                    return lambda$cleanupDisabledPackageReceiversLocked$4;
                }
            };
            if (set != null) {
                broadcastPredicate = new BroadcastProcessQueue.BroadcastPredicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda20
                    @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
                    public final boolean test(BroadcastRecord broadcastRecord, int i2) {
                        boolean lambda$cleanupDisabledPackageReceiversLocked$5;
                        lambda$cleanupDisabledPackageReceiversLocked$5 = BroadcastQueueModernImpl.lambda$cleanupDisabledPackageReceiversLocked$5(str, set, broadcastRecord, i2);
                        return lambda$cleanupDisabledPackageReceiversLocked$5;
                    }
                };
            } else {
                broadcastPredicate = new BroadcastProcessQueue.BroadcastPredicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda21
                    @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
                    public final boolean test(BroadcastRecord broadcastRecord, int i2) {
                        boolean lambda$cleanupDisabledPackageReceiversLocked$6;
                        lambda$cleanupDisabledPackageReceiversLocked$6 = BroadcastQueueModernImpl.lambda$cleanupDisabledPackageReceiversLocked$6(str, broadcastRecord, i2);
                        return lambda$cleanupDisabledPackageReceiversLocked$6;
                    }
                };
            }
        } else {
            predicate = new Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda22
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$cleanupDisabledPackageReceiversLocked$7;
                    lambda$cleanupDisabledPackageReceiversLocked$7 = BroadcastQueueModernImpl.lambda$cleanupDisabledPackageReceiversLocked$7(i, (BroadcastProcessQueue) obj);
                    return lambda$cleanupDisabledPackageReceiversLocked$7;
                }
            };
            BroadcastProcessQueue.BroadcastPredicate broadcastPredicate2 = BROADCAST_PREDICATE_ANY;
            cleanupUserStateLocked(this.mUidForeground, i);
            broadcastPredicate = broadcastPredicate2;
        }
        return forEachMatchingBroadcast(predicate, broadcastPredicate, this.mBroadcastConsumerSkip, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$cleanupDisabledPackageReceiversLocked$4(int i, BroadcastProcessQueue broadcastProcessQueue) {
        return broadcastProcessQueue.uid == i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$cleanupDisabledPackageReceiversLocked$5(String str, Set set, BroadcastRecord broadcastRecord, int i) {
        Object obj = broadcastRecord.receivers.get(i);
        if (!(obj instanceof ResolveInfo)) {
            return false;
        }
        ActivityInfo activityInfo = ((ResolveInfo) obj).activityInfo;
        return str.equals(activityInfo.packageName) && set.contains(activityInfo.name);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$cleanupDisabledPackageReceiversLocked$6(String str, BroadcastRecord broadcastRecord, int i) {
        return str.equals(BroadcastRecord.getReceiverPackageName(broadcastRecord.receivers.get(i)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$cleanupDisabledPackageReceiversLocked$7(int i, BroadcastProcessQueue broadcastProcessQueue) {
        return UserHandle.getUserId(broadcastProcessQueue.uid) == i;
    }

    @GuardedBy({"mService"})
    private void cleanupUserStateLocked(SparseBooleanArray sparseBooleanArray, int i) {
        for (int size = sparseBooleanArray.size() - 1; size >= 0; size--) {
            if (UserHandle.getUserId(sparseBooleanArray.keyAt(size)) == i) {
                sparseBooleanArray.removeAt(size);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$10(BroadcastRecord broadcastRecord, int i) {
        setDeliveryState(null, null, broadcastRecord, i, broadcastRecord.receivers.get(i), 2, "mBroadcastConsumerSkip");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$11(BroadcastRecord broadcastRecord, int i) {
        setDeliveryState(null, null, broadcastRecord, i, broadcastRecord.receivers.get(i), 2, "mBroadcastConsumerSkipAndCanceled");
        broadcastRecord.resultCode = 0;
        broadcastRecord.resultData = null;
        broadcastRecord.resultExtras = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$12(BroadcastRecord broadcastRecord, int i) {
        setDeliveryState(null, null, broadcastRecord, i, broadcastRecord.receivers.get(i), 6, "mBroadcastConsumerDeferApply");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$13(BroadcastRecord broadcastRecord, int i) {
        setDeliveryState(null, null, broadcastRecord, i, broadcastRecord.receivers.get(i), 0, "mBroadcastConsumerDeferClear");
    }

    private boolean testAllProcessQueues(Predicate<BroadcastProcessQueue> predicate, String str, PrintWriter printWriter) {
        for (int i = 0; i < this.mProcessQueues.size(); i++) {
            for (BroadcastProcessQueue valueAt = this.mProcessQueues.valueAt(i); valueAt != null; valueAt = valueAt.processNameNext) {
                if (!predicate.test(valueAt)) {
                    long uptimeMillis = SystemClock.uptimeMillis();
                    if (uptimeMillis > this.mLastTestFailureTime + 1000) {
                        this.mLastTestFailureTime = uptimeMillis;
                        printWriter.println("Test " + str + " failed due to " + valueAt.toShortString() + " " + valueAt.describeStateLocked());
                        printWriter.flush();
                    }
                    return false;
                }
            }
        }
        printWriter.println("Test " + str + " passed");
        printWriter.flush();
        return true;
    }

    private boolean forEachMatchingBroadcast(Predicate<BroadcastProcessQueue> predicate, BroadcastProcessQueue.BroadcastPredicate broadcastPredicate, BroadcastProcessQueue.BroadcastConsumer broadcastConsumer, boolean z) {
        boolean z2 = false;
        for (int size = this.mProcessQueues.size() - 1; size >= 0; size--) {
            for (BroadcastProcessQueue valueAt = this.mProcessQueues.valueAt(size); valueAt != null; valueAt = valueAt.processNameNext) {
                if (predicate.test(valueAt) && valueAt.forEachMatchingBroadcast(broadcastPredicate, broadcastConsumer, z)) {
                    updateRunnableList(valueAt);
                    z2 = true;
                }
            }
        }
        if (z2) {
            enqueueUpdateRunningList();
        }
        return z2;
    }

    private boolean forEachMatchingQueue(Predicate<BroadcastProcessQueue> predicate, Consumer<BroadcastProcessQueue> consumer) {
        boolean z = false;
        for (int size = this.mProcessQueues.size() - 1; size >= 0; size--) {
            for (BroadcastProcessQueue valueAt = this.mProcessQueues.valueAt(size); valueAt != null; valueAt = valueAt.processNameNext) {
                if (predicate.test(valueAt)) {
                    consumer.accept(valueAt);
                    updateRunnableList(valueAt);
                    z = true;
                }
            }
        }
        if (z) {
            enqueueUpdateRunningList();
        }
        return z;
    }

    @Override // com.android.server.am.BroadcastQueue
    public void start(ContentResolver contentResolver) {
        this.mFgConstants.startObserving(this.mHandler, contentResolver);
        this.mBgConstants.startObserving(this.mHandler, contentResolver);
        this.mService.registerUidObserver(new UidObserver() { // from class: com.android.server.am.BroadcastQueueModernImpl.1
            public void onUidStateChanged(int i, int i2, long j, int i3) {
                BroadcastQueueModernImpl.this.mLocalHandler.removeMessages(8, Integer.valueOf(i));
                BroadcastQueueModernImpl.this.mLocalHandler.obtainMessage(8, i2, 0, Integer.valueOf(i)).sendToTarget();
            }
        }, 1, 2, "android");
        this.mLocalHandler.sendEmptyMessage(5);
    }

    @Override // com.android.server.am.BroadcastQueue
    /* renamed from: isIdleLocked */
    public boolean lambda$waitForIdle$1() {
        return lambda$waitForIdle$17(ActivityManagerDebugConfig.LOG_WRITER_INFO);
    }

    /* renamed from: isIdleLocked, reason: merged with bridge method [inline-methods] */
    public boolean lambda$waitForIdle$17(PrintWriter printWriter) {
        return testAllProcessQueues(new Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean isIdle;
                isIdle = ((BroadcastProcessQueue) obj).isIdle();
                return isIdle;
            }
        }, "idle", printWriter);
    }

    @Override // com.android.server.am.BroadcastQueue
    /* renamed from: isBeyondBarrierLocked */
    public boolean lambda$waitForBarrier$2(long j) {
        return lambda$waitForBarrier$19(j, ActivityManagerDebugConfig.LOG_WRITER_INFO);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$isBeyondBarrierLocked$15(long j, BroadcastProcessQueue broadcastProcessQueue) {
        return broadcastProcessQueue.isBeyondBarrierLocked(j);
    }

    /* renamed from: isBeyondBarrierLocked, reason: merged with bridge method [inline-methods] */
    public boolean lambda$waitForBarrier$19(final long j, PrintWriter printWriter) {
        return testAllProcessQueues(new Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$isBeyondBarrierLocked$15;
                lambda$isBeyondBarrierLocked$15 = BroadcastQueueModernImpl.lambda$isBeyondBarrierLocked$15(j, (BroadcastProcessQueue) obj);
                return lambda$isBeyondBarrierLocked$15;
            }
        }, "barrier", printWriter);
    }

    @Override // com.android.server.am.BroadcastQueue
    /* renamed from: isDispatchedLocked */
    public boolean lambda$waitForDispatched$3(Intent intent) {
        return lambda$waitForDispatched$21(intent, ActivityManagerDebugConfig.LOG_WRITER_INFO);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$isDispatchedLocked$16(Intent intent, BroadcastProcessQueue broadcastProcessQueue) {
        return broadcastProcessQueue.isDispatched(intent);
    }

    /* renamed from: isDispatchedLocked, reason: merged with bridge method [inline-methods] */
    public boolean lambda$waitForDispatched$21(final Intent intent, PrintWriter printWriter) {
        return testAllProcessQueues(new Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda14
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$isDispatchedLocked$16;
                lambda$isDispatchedLocked$16 = BroadcastQueueModernImpl.lambda$isDispatchedLocked$16(intent, (BroadcastProcessQueue) obj);
                return lambda$isDispatchedLocked$16;
            }
        }, "dispatch of " + intent, printWriter);
    }

    @Override // com.android.server.am.BroadcastQueue
    public void waitForIdle(final PrintWriter printWriter) {
        waitFor(new BooleanSupplier() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda4
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$waitForIdle$17;
                lambda$waitForIdle$17 = BroadcastQueueModernImpl.this.lambda$waitForIdle$17(printWriter);
                return lambda$waitForIdle$17;
            }
        });
    }

    @Override // com.android.server.am.BroadcastQueue
    public void waitForBarrier(final PrintWriter printWriter) {
        Predicate<BroadcastProcessQueue> predicate;
        final long uptimeMillis = SystemClock.uptimeMillis();
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                predicate = QUEUE_PREDICATE_ANY;
                forEachMatchingQueue(predicate, new Consumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((BroadcastProcessQueue) obj).addPrioritizeEarliestRequest();
                    }
                });
            } finally {
                ActivityManagerService.resetPriorityAfterLockedSection();
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
        try {
            waitFor(new BooleanSupplier() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda1
                @Override // java.util.function.BooleanSupplier
                public final boolean getAsBoolean() {
                    boolean lambda$waitForBarrier$19;
                    lambda$waitForBarrier$19 = BroadcastQueueModernImpl.this.lambda$waitForBarrier$19(uptimeMillis, printWriter);
                    return lambda$waitForBarrier$19;
                }
            });
            ActivityManagerService activityManagerService2 = this.mService;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService2) {
                try {
                    forEachMatchingQueue(predicate, new Consumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            ((BroadcastProcessQueue) obj).removePrioritizeEarliestRequest();
                        }
                    });
                } finally {
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        } catch (Throwable th) {
            ActivityManagerService activityManagerService3 = this.mService;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService3) {
                try {
                    forEachMatchingQueue(QUEUE_PREDICATE_ANY, new Consumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            ((BroadcastProcessQueue) obj).removePrioritizeEarliestRequest();
                        }
                    });
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                } finally {
                }
            }
        }
    }

    @Override // com.android.server.am.BroadcastQueue
    public void waitForDispatched(final Intent intent, final PrintWriter printWriter) {
        waitFor(new BooleanSupplier() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda6
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$waitForDispatched$21;
                lambda$waitForDispatched$21 = BroadcastQueueModernImpl.this.lambda$waitForDispatched$21(intent, printWriter);
                return lambda$waitForDispatched$21;
            }
        });
    }

    private void waitFor(BooleanSupplier booleanSupplier) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mWaitingFor.add(Pair.create(booleanSupplier, countDownLatch));
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
        enqueueUpdateRunningList();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkAndRemoveWaitingFor() {
        if (this.mWaitingFor.isEmpty()) {
            return;
        }
        this.mWaitingFor.removeIf(new Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$checkAndRemoveWaitingFor$22;
                lambda$checkAndRemoveWaitingFor$22 = BroadcastQueueModernImpl.lambda$checkAndRemoveWaitingFor$22((Pair) obj);
                return lambda$checkAndRemoveWaitingFor$22;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$checkAndRemoveWaitingFor$22(Pair pair) {
        if (!((BooleanSupplier) pair.first).getAsBoolean()) {
            return false;
        }
        ((CountDownLatch) pair.second).countDown();
        return true;
    }

    @Override // com.android.server.am.BroadcastQueue
    public void forceDelayBroadcastDelivery(final String str, final long j) {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                forEachMatchingQueue(new Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda23
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$forceDelayBroadcastDelivery$23;
                        lambda$forceDelayBroadcastDelivery$23 = BroadcastQueueModernImpl.lambda$forceDelayBroadcastDelivery$23(str, (BroadcastProcessQueue) obj);
                        return lambda$forceDelayBroadcastDelivery$23;
                    }
                }, new Consumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda24
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((BroadcastProcessQueue) obj).forceDelayBroadcastDelivery(j);
                    }
                });
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$forceDelayBroadcastDelivery$23(String str, BroadcastProcessQueue broadcastProcessQueue) {
        return str.equals(broadcastProcessQueue.getPackageName());
    }

    @Override // com.android.server.am.BroadcastQueue
    public String describeStateLocked() {
        return getRunningSize() + " running";
    }

    private void checkHealth() {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                checkHealthLocked();
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private void checkHealthLocked() {
        try {
            assertHealthLocked();
            this.mLocalHandler.sendEmptyMessageDelayed(5, 60000L);
        } catch (Exception e) {
            Slog.wtf(BroadcastQueue.TAG, e);
            dumpToDropBoxLocked(e.toString());
        }
    }

    @VisibleForTesting
    void assertHealthLocked() {
        int i;
        BroadcastProcessQueue broadcastProcessQueue = this.mRunnableHead;
        BroadcastProcessQueue broadcastProcessQueue2 = null;
        while (true) {
            if (broadcastProcessQueue == null) {
                break;
            }
            BroadcastQueue.checkState(broadcastProcessQueue.runnableAtPrev == broadcastProcessQueue2, "runnableAtPrev");
            BroadcastQueue.checkState(broadcastProcessQueue.isRunnable(), "isRunnable " + broadcastProcessQueue);
            if (broadcastProcessQueue2 != null) {
                BroadcastQueue.checkState(broadcastProcessQueue.getRunnableAt() >= broadcastProcessQueue2.getRunnableAt(), "getRunnableAt " + broadcastProcessQueue + " vs " + broadcastProcessQueue2);
            }
            broadcastProcessQueue2 = broadcastProcessQueue;
            broadcastProcessQueue = broadcastProcessQueue.runnableAtNext;
        }
        for (BroadcastProcessQueue broadcastProcessQueue3 : this.mRunning) {
            if (broadcastProcessQueue3 != null) {
                BroadcastQueue.checkState(broadcastProcessQueue3.isActive(), "isActive " + broadcastProcessQueue3);
            }
        }
        BroadcastProcessQueue broadcastProcessQueue4 = this.mRunningColdStart;
        if (broadcastProcessQueue4 != null) {
            BroadcastQueue.checkState(getRunningIndexOf(broadcastProcessQueue4) >= 0, "isOrphaned " + this.mRunningColdStart);
        }
        this.mBroadcastQueueModernImplExt.beginAssertHealthLocked();
        for (i = 0; i < this.mProcessQueues.size(); i++) {
            for (BroadcastProcessQueue valueAt = this.mProcessQueues.valueAt(i); valueAt != null; valueAt = valueAt.processNameNext) {
                valueAt.assertHealthLocked();
                this.mBroadcastQueueModernImplExt.assertHealthLocked(valueAt);
            }
        }
        this.mBroadcastQueueModernImplExt.endAssertHealthLocked(this.mProcessQueues, this.mLocalHandler);
    }

    private void updateWarmProcess(BroadcastProcessQueue broadcastProcessQueue) {
        if (broadcastProcessQueue.isProcessWarm()) {
            return;
        }
        ProcessRecord processRecordLocked = this.mService.getProcessRecordLocked(broadcastProcessQueue.processName, broadcastProcessQueue.uid);
        broadcastProcessQueue.setProcessAndUidState(processRecordLocked, this.mUidForeground.get(broadcastProcessQueue.uid, false), isProcessFreezable(processRecordLocked));
    }

    private void setQueueProcess(BroadcastProcessQueue broadcastProcessQueue, ProcessRecord processRecord) {
        if (broadcastProcessQueue.setProcessAndUidState(processRecord, this.mUidForeground.get(broadcastProcessQueue.uid, false), isProcessFreezable(processRecord))) {
            updateRunnableList(broadcastProcessQueue);
        }
    }

    @GuardedBy({"mService"})
    private boolean isProcessFreezable(ProcessRecord processRecord) {
        boolean z;
        if (processRecord == null) {
            return false;
        }
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mService.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                z = processRecord.mOptRecord.isPendingFreeze() || processRecord.mOptRecord.isFrozen();
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
        return z;
    }

    @GuardedBy({"mService"})
    private void refreshProcessQueuesLocked(int i) {
        for (BroadcastProcessQueue broadcastProcessQueue = this.mProcessQueues.get(i); broadcastProcessQueue != null; broadcastProcessQueue = broadcastProcessQueue.processNameNext) {
            setQueueProcess(broadcastProcessQueue, broadcastProcessQueue.app);
        }
        enqueueUpdateRunningList();
    }

    @GuardedBy({"mService"})
    private void refreshProcessQueueLocked(ProcessRecord processRecord) {
        ProcessRecord processRecord2;
        BroadcastProcessQueue processQueue = getProcessQueue(processRecord.processName, processRecord.uid);
        if (processQueue == null || (processRecord2 = processQueue.app) == null || processRecord2.getPid() != processRecord.getPid()) {
            return;
        }
        setQueueProcess(processQueue, processQueue.app);
        enqueueUpdateRunningList();
    }

    private void notifyStartedRunning(BroadcastProcessQueue broadcastProcessQueue) {
        ProcessRecord processRecord = broadcastProcessQueue.app;
        if (processRecord != null) {
            processRecord.mReceivers.incrementCurReceivers();
            if (this.mService.mInternal.getRestrictionLevel(broadcastProcessQueue.uid) < 40) {
                this.mService.updateLruProcessLocked(broadcastProcessQueue.app, false, null);
            }
            this.mService.mOomAdjuster.unfreezeTemporarily(broadcastProcessQueue.app, 3);
            if (broadcastProcessQueue.runningOomAdjusted) {
                broadcastProcessQueue.app.mState.forceProcessStateUpTo(11);
                this.mService.enqueueOomAdjTargetLocked(broadcastProcessQueue.app);
            }
        }
    }

    private void notifyStoppedRunning(BroadcastProcessQueue broadcastProcessQueue) {
        ProcessRecord processRecord = broadcastProcessQueue.app;
        if (processRecord != null) {
            processRecord.mReceivers.decrementCurReceivers();
            if (broadcastProcessQueue.runningOomAdjusted) {
                this.mService.enqueueOomAdjTargetLocked(broadcastProcessQueue.app);
            }
        }
    }

    private void notifyScheduleRegisteredReceiver(ProcessRecord processRecord, BroadcastRecord broadcastRecord, BroadcastFilter broadcastFilter) {
        reportUsageStatsBroadcastDispatched(processRecord, broadcastRecord);
    }

    private void notifyScheduleReceiver(ProcessRecord processRecord, BroadcastRecord broadcastRecord, ResolveInfo resolveInfo) {
        reportUsageStatsBroadcastDispatched(processRecord, broadcastRecord);
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        String str = activityInfo.packageName;
        processRecord.addPackage(str, activityInfo.applicationInfo.longVersionCode, this.mService.mProcessStats);
        boolean z = broadcastRecord.intent.getComponent() != null;
        boolean equals = Objects.equals(broadcastRecord.callerPackage, str);
        if (z && !equals) {
            this.mService.mUsageStatsService.reportEvent(str, broadcastRecord.userId, 31);
        }
        this.mService.notifyPackageUse(str, 3);
        this.mService.mPackageManagerInt.setPackageStoppedState(str, false, broadcastRecord.userId);
    }

    private void reportUsageStatsBroadcastDispatched(ProcessRecord processRecord, BroadcastRecord broadcastRecord) {
        String packageName;
        BroadcastOptions broadcastOptions = broadcastRecord.options;
        long idForResponseEvent = broadcastOptions != null ? broadcastOptions.getIdForResponseEvent() : 0L;
        if (idForResponseEvent <= 0) {
            return;
        }
        if (broadcastRecord.intent.getPackage() != null) {
            packageName = broadcastRecord.intent.getPackage();
        } else {
            packageName = broadcastRecord.intent.getComponent() != null ? broadcastRecord.intent.getComponent().getPackageName() : null;
        }
        String str = packageName;
        if (str == null) {
            return;
        }
        this.mService.mUsageStatsService.reportBroadcastDispatched(broadcastRecord.callingUid, str, UserHandle.of(broadcastRecord.userId), idForResponseEvent, SystemClock.elapsedRealtime(), this.mService.getUidStateLocked(processRecord.uid));
    }

    private void notifyFinishReceiver(BroadcastProcessQueue broadcastProcessQueue, ProcessRecord processRecord, BroadcastRecord broadcastRecord, int i, Object obj) {
        if (broadcastRecord.wasDeliveryAttempted(i)) {
            logBroadcastDeliveryEventReported(broadcastProcessQueue, processRecord, broadcastRecord, i, obj);
        }
        if (broadcastRecord.terminalCount == broadcastRecord.receivers.size()) {
            notifyFinishBroadcast(broadcastRecord);
        }
    }

    private void logBroadcastDeliveryEventReported(BroadcastProcessQueue broadcastProcessQueue, ProcessRecord processRecord, BroadcastRecord broadcastRecord, int i, Object obj) {
        int i2;
        int i3;
        int receiverUid = BroadcastRecord.getReceiverUid(obj);
        int i4 = broadcastRecord.callingUid;
        if (i4 == -1) {
            i4 = 1000;
        }
        int i5 = i4;
        String action = broadcastRecord.intent.getAction();
        int i6 = obj instanceof BroadcastFilter ? 1 : 2;
        if (broadcastProcessQueue == null) {
            i3 = 0;
            i2 = -1;
        } else if (broadcastProcessQueue.getActiveViaColdStart()) {
            i2 = 20;
            i3 = 3;
        } else {
            i2 = broadcastProcessQueue.lastProcessState;
            i3 = 1;
        }
        long j = broadcastRecord.scheduledTime[i];
        long j2 = j - broadcastRecord.enqueueTime;
        long j3 = broadcastRecord.terminalTime[i] - j;
        if (broadcastProcessQueue != null) {
            FrameworkStatsLog.write(FrameworkStatsLog.BROADCAST_DELIVERY_EVENT_REPORTED, receiverUid, i5, action, i6, i3, j2, 0L, j3, broadcastProcessQueue.getActiveWasStopped() ? 2 : 1, processRecord != null ? processRecord.info.packageName : null, broadcastRecord.callerPackage, broadcastRecord.calculateTypeForLogging(), broadcastRecord.getDeliveryGroupPolicy(), broadcastRecord.intent.getFlags(), BroadcastRecord.getReceiverPriority(obj), broadcastRecord.callerProcState, i2);
        }
    }

    private void notifyFinishBroadcast(BroadcastRecord broadcastRecord) {
        this.mService.notifyBroadcastFinishedLocked(broadcastRecord);
        broadcastRecord.finishTime = SystemClock.uptimeMillis();
        broadcastRecord.nextReceiver = broadcastRecord.receivers.size();
        this.mHistory.onBroadcastFinishedLocked(broadcastRecord);
        BroadcastQueueImpl.logBootCompletedBroadcastCompletionLatencyIfPossible(broadcastRecord);
        if (broadcastRecord.intent.getComponent() == null && broadcastRecord.intent.getPackage() == null && (broadcastRecord.intent.getFlags() & 1073741824) == 0) {
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < broadcastRecord.receivers.size(); i3++) {
                if (broadcastRecord.receivers.get(i3) instanceof ResolveInfo) {
                    i++;
                    if (broadcastRecord.delivery[i3] == 2) {
                        i2++;
                    }
                }
            }
            this.mService.addBroadcastStatLocked(broadcastRecord.intent.getAction(), broadcastRecord.callerPackage, i, i2, SystemClock.uptimeMillis() - broadcastRecord.enqueueTime);
        }
    }

    @VisibleForTesting
    BroadcastProcessQueue getOrCreateProcessQueue(ProcessRecord processRecord) {
        return getOrCreateProcessQueue(processRecord.processName, processRecord.info.uid);
    }

    @VisibleForTesting
    BroadcastProcessQueue getOrCreateProcessQueue(String str, int i) {
        BroadcastProcessQueue broadcastProcessQueue = this.mProcessQueues.get(i);
        while (broadcastProcessQueue != null) {
            if (Objects.equals(broadcastProcessQueue.processName, str)) {
                return broadcastProcessQueue;
            }
            BroadcastProcessQueue broadcastProcessQueue2 = broadcastProcessQueue.processNameNext;
            if (broadcastProcessQueue2 == null) {
                break;
            }
            broadcastProcessQueue = broadcastProcessQueue2;
        }
        BroadcastProcessQueue broadcastProcessQueue3 = new BroadcastProcessQueue(this.mConstants, str, i);
        setQueueProcess(broadcastProcessQueue3, this.mService.getProcessRecordLocked(str, i));
        if (broadcastProcessQueue == null) {
            this.mProcessQueues.put(i, broadcastProcessQueue3);
        } else {
            broadcastProcessQueue.processNameNext = broadcastProcessQueue3;
        }
        this.mBroadcastQueueModernImplExt.hookCreateProcessQueue(broadcastProcessQueue3);
        return broadcastProcessQueue3;
    }

    @VisibleForTesting
    BroadcastProcessQueue getProcessQueue(ProcessRecord processRecord) {
        return getProcessQueue(processRecord.processName, processRecord.info.uid);
    }

    @VisibleForTesting
    BroadcastProcessQueue getProcessQueue(String str, int i) {
        for (BroadcastProcessQueue broadcastProcessQueue = this.mProcessQueues.get(i); broadcastProcessQueue != null; broadcastProcessQueue = broadcastProcessQueue.processNameNext) {
            if (Objects.equals(broadcastProcessQueue.processName, str)) {
                return broadcastProcessQueue;
            }
        }
        return null;
    }

    @VisibleForTesting
    BroadcastProcessQueue removeProcessQueue(ProcessRecord processRecord) {
        return removeProcessQueue(processRecord.processName, processRecord.info.uid);
    }

    @VisibleForTesting
    BroadcastProcessQueue removeProcessQueue(String str, int i) {
        BroadcastProcessQueue broadcastProcessQueue = null;
        for (BroadcastProcessQueue broadcastProcessQueue2 = this.mProcessQueues.get(i); broadcastProcessQueue2 != null; broadcastProcessQueue2 = broadcastProcessQueue2.processNameNext) {
            if (Objects.equals(broadcastProcessQueue2.processName, str)) {
                if (broadcastProcessQueue != null) {
                    broadcastProcessQueue.processNameNext = broadcastProcessQueue2.processNameNext;
                } else {
                    BroadcastProcessQueue broadcastProcessQueue3 = broadcastProcessQueue2.processNameNext;
                    if (broadcastProcessQueue3 != null) {
                        this.mProcessQueues.put(i, broadcastProcessQueue3);
                    } else {
                        this.mProcessQueues.remove(i);
                    }
                }
                return broadcastProcessQueue2;
            }
            broadcastProcessQueue = broadcastProcessQueue2;
        }
        return null;
    }

    @Override // com.android.server.am.BroadcastQueue
    @NeverCompile
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1138166333441L, this.mQueueName);
        this.mHistory.dumpDebug(protoOutputStream);
        protoOutputStream.end(start);
    }

    @Override // com.android.server.am.BroadcastQueue
    @NeverCompile
    public boolean dumpLocked(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, int i, boolean z, boolean z2, boolean z3, String str, boolean z4) {
        long uptimeMillis = SystemClock.uptimeMillis();
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("📋 Per-process queues:");
        indentingPrintWriter.increaseIndent();
        for (int i2 = 0; i2 < this.mProcessQueues.size(); i2++) {
            for (BroadcastProcessQueue valueAt = this.mProcessQueues.valueAt(i2); valueAt != null; valueAt = valueAt.processNameNext) {
                valueAt.dumpLocked(uptimeMillis, indentingPrintWriter);
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("\u1f9cd Runnable:");
        indentingPrintWriter.increaseIndent();
        BroadcastProcessQueue broadcastProcessQueue = this.mRunnableHead;
        if (broadcastProcessQueue == null) {
            indentingPrintWriter.println("(none)");
        } else {
            while (broadcastProcessQueue != null) {
                TimeUtils.formatDuration(broadcastProcessQueue.getRunnableAt(), uptimeMillis, indentingPrintWriter);
                indentingPrintWriter.print(' ');
                indentingPrintWriter.print(BroadcastProcessQueue.reasonToString(broadcastProcessQueue.getRunnableAtReason()));
                indentingPrintWriter.print(' ');
                indentingPrintWriter.print(broadcastProcessQueue.toShortString());
                indentingPrintWriter.println();
                broadcastProcessQueue = broadcastProcessQueue.runnableAtNext;
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("🏃 Running:");
        indentingPrintWriter.increaseIndent();
        for (BroadcastProcessQueue broadcastProcessQueue2 : this.mRunning) {
            if (broadcastProcessQueue2 != null && broadcastProcessQueue2 == this.mRunningColdStart) {
                indentingPrintWriter.print("\u1f976 ");
            } else {
                indentingPrintWriter.print("\u3000 ");
            }
            if (broadcastProcessQueue2 != null) {
                indentingPrintWriter.println(broadcastProcessQueue2.toShortString());
            } else {
                indentingPrintWriter.println("(none)");
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("Broadcasts with ignored delivery group policies:");
        indentingPrintWriter.increaseIndent();
        this.mService.dumpDeliveryGroupPolicyIgnoredActions(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("Foreground UIDs:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(this.mUidForeground);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        if (z) {
            this.mConstants.dump(indentingPrintWriter);
        }
        if (!z2) {
            return z4;
        }
        return this.mHistory.dumpLocked(indentingPrintWriter, str, this.mQueueName, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"), z3, z4);
    }

    public IBroadcastQueueWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class BroadcastQueueWrapper implements IBroadcastQueueWrapper {
        private BroadcastQueueWrapper() {
        }

        @Override // com.android.server.am.IBroadcastQueueWrapper
        public IBroadcastQueueModernImplExt getModernExtImpl() {
            return BroadcastQueueModernImpl.this.mBroadcastQueueModernImplExt;
        }

        @Override // com.android.server.am.IBroadcastQueueWrapper
        public SparseArray<BroadcastProcessQueue> getProcessQueues() {
            return BroadcastQueueModernImpl.this.mProcessQueues;
        }

        @Override // com.android.server.am.IBroadcastQueueWrapper
        public void enqueueBroadcastLocked(ArrayList<BroadcastRecord> arrayList, boolean z, boolean z2) {
            HashSet hashSet = new HashSet();
            Iterator<BroadcastRecord> it = arrayList.iterator();
            while (it.hasNext()) {
                BroadcastRecord next = it.next();
                int traceBegin = BroadcastQueue.traceBegin("enqueueBroadcast");
                next.enqueueTime = SystemClock.uptimeMillis();
                next.enqueueRealTime = SystemClock.elapsedRealtime();
                next.enqueueClockTime = System.currentTimeMillis();
                for (int i = 0; i < next.receivers.size(); i++) {
                    Object obj = next.receivers.get(i);
                    BroadcastProcessQueue orCreateProcessQueue = BroadcastQueueModernImpl.this.getOrCreateProcessQueue(BroadcastRecord.getReceiverProcessName(obj), BroadcastRecord.getReceiverUid(obj));
                    orCreateProcessQueue.getWrapper().enqueueBroadcast(next, i, z, z2);
                    if (next.isDeferUntilActive() && orCreateProcessQueue.isDeferredUntilActive()) {
                        BroadcastQueueModernImpl.this.setDeliveryState(orCreateProcessQueue, null, next, i, obj, 6, "deferred at enqueue time");
                    }
                    hashSet.add(orCreateProcessQueue);
                }
                BroadcastQueue.traceEnd(traceBegin);
            }
            Iterator it2 = hashSet.iterator();
            while (it2.hasNext()) {
                BroadcastQueueModernImpl.this.updateRunnableList((BroadcastProcessQueue) it2.next());
                BroadcastQueueModernImpl.this.enqueueUpdateRunningList();
            }
        }
    }
}
