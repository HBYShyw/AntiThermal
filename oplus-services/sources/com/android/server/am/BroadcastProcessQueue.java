package com.android.server.am;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.util.IndentingPrintWriter;
import android.util.TimeUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.Preconditions;
import com.android.server.am.IBroadcastProcessQueueExt;
import com.android.server.backup.BackupManagerConstants;
import dalvik.annotation.optimization.NeverCompile;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BroadcastProcessQueue {
    static final int REASON_BLOCKED = 4;
    static final int REASON_CACHED = 1;
    static final int REASON_CACHED_INFINITE_DEFER = 8;
    static final int REASON_CONTAINS_ALARM = 12;
    static final int REASON_CONTAINS_FOREGROUND = 10;
    static final int REASON_CONTAINS_INSTRUMENTED = 16;
    static final int REASON_CONTAINS_INTERACTIVE = 14;
    static final int REASON_CONTAINS_MANIFEST = 17;
    static final int REASON_CONTAINS_ORDERED = 11;
    static final int REASON_CONTAINS_PRIORITIZED = 13;
    static final int REASON_CONTAINS_RESULT_TO = 15;
    static final int REASON_CORE_UID = 19;
    static final int REASON_EMPTY = 0;
    static final int REASON_FORCE_DELAYED = 7;
    static final int REASON_FOREGROUND = 18;
    static final int REASON_INSTRUMENTED = 5;
    static final int REASON_MAX_PENDING = 3;
    static final int REASON_NORMAL = 2;
    static final int REASON_PERSISTENT = 6;
    static final int REASON_TOP_PROCESS = 20;
    static final boolean VERBOSE = false;
    public static IBroadcastProcessQueueExt.IStaticExt mStaticExt = (IBroadcastProcessQueueExt.IStaticExt) ExtLoader.type(IBroadcastProcessQueueExt.IStaticExt.class).create();
    ProcessRecord app;
    final BroadcastConstants constants;
    long lastCpuDelayTime;
    int lastProcessState;
    private BroadcastRecord mActive;
    private int mActiveAssumedDeliveryCountSinceIdle;
    private int mActiveCountConsecutiveNormal;
    private int mActiveCountConsecutiveUrgent;
    private int mActiveCountSinceIdle;
    private int mActiveIndex;
    private boolean mActiveViaColdStart;
    private boolean mActiveWasStopped;
    private String mCachedToShortString;
    private String mCachedToString;
    private int mCountAlarm;
    private int mCountDeferred;
    private int mCountEnqueued;
    private int mCountForeground;
    private int mCountForegroundDeferred;
    private int mCountInstrumented;
    private int mCountInteractive;
    private int mCountManifest;
    private int mCountOrdered;
    private int mCountPrioritizeEarliestRequests;
    private int mCountPrioritized;
    private int mCountPrioritizedDeferred;
    private int mCountResultTo;
    private long mForcedDelayedDurationMs;
    private boolean mLastDeferredStates;
    private boolean mProcessFreezable;
    private boolean mProcessInstrumented;
    private boolean mProcessPersistent;
    private boolean mRunnableAtInvalidated;
    private boolean mUidForeground;
    final String processName;
    BroadcastProcessQueue processNameNext;
    BroadcastProcessQueue runnableAtNext;
    BroadcastProcessQueue runnableAtPrev;
    boolean runningOomAdjusted;
    String runningTraceTrackName;
    final int uid;
    private final ArrayDeque<SomeArgs> mPending = new ArrayDeque<>();
    private final ArrayDeque<SomeArgs> mPendingUrgent = new ArrayDeque<>(4);
    private final ArrayDeque<SomeArgs> mPendingOffload = new ArrayDeque<>(4);
    private long mRunnableAt = Long.MAX_VALUE;
    private int mRunnableAtReason = 0;
    private IBroadcastProcessQueueExt mBroadcastProcessQueueExt = (IBroadcastProcessQueueExt) ExtLoader.type(IBroadcastProcessQueueExt.class).base(this).create();
    private final IBroadcastProcessQueueWrapper mWrapper = new BroadcastProcessQueueWrapper();

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface BroadcastConsumer {
        void accept(BroadcastRecord broadcastRecord, int i);
    }

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface BroadcastPredicate {
        boolean test(BroadcastRecord broadcastRecord, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface Reason {
    }

    public BroadcastProcessQueue(BroadcastConstants broadcastConstants, String str, int i) {
        Objects.requireNonNull(broadcastConstants);
        this.constants = broadcastConstants;
        Objects.requireNonNull(str);
        this.processName = str;
        this.uid = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayDeque<SomeArgs> getQueueForBroadcast(BroadcastRecord broadcastRecord) {
        if (broadcastRecord.isUrgent()) {
            return this.mPendingUrgent;
        }
        if (broadcastRecord.isOffload()) {
            return this.mPendingOffload;
        }
        return this.mPending;
    }

    public BroadcastRecord enqueueOrReplaceBroadcast(BroadcastRecord broadcastRecord, int i, BroadcastConsumer broadcastConsumer) {
        BroadcastRecord replaceBroadcast;
        if (broadcastRecord.isReplacePending() && broadcastRecord.getDeliveryGroupPolicy() == 0 && (replaceBroadcast = replaceBroadcast(broadcastRecord, i)) != null) {
            if (this.mLastDeferredStates && shouldBeDeferred() && broadcastRecord.getDeliveryState(i) == 0) {
                broadcastConsumer.accept(broadcastRecord, i);
            }
            return replaceBroadcast;
        }
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = broadcastRecord;
        obtain.argi1 = i;
        getQueueForBroadcast(broadcastRecord).addLast(obtain);
        onBroadcastEnqueued(broadcastRecord, i);
        if (!this.mLastDeferredStates || !shouldBeDeferred() || broadcastRecord.getDeliveryState(i) != 0) {
            return null;
        }
        broadcastConsumer.accept(broadcastRecord, i);
        return null;
    }

    public void reEnqueueActiveBroadcast() {
        if (isActive()) {
            BroadcastRecord active = getActive();
            int activeIndex = getActiveIndex();
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = active;
            obtain.argi1 = activeIndex;
            getQueueForBroadcast(active).addFirst(obtain);
            onBroadcastEnqueued(active, activeIndex);
        }
    }

    private BroadcastRecord replaceBroadcast(BroadcastRecord broadcastRecord, int i) {
        return replaceBroadcastInQueue(getQueueForBroadcast(broadcastRecord), broadcastRecord, i);
    }

    private BroadcastRecord replaceBroadcastInQueue(ArrayDeque<SomeArgs> arrayDeque, BroadcastRecord broadcastRecord, int i) {
        Iterator<SomeArgs> descendingIterator = arrayDeque.descendingIterator();
        Object obj = broadcastRecord.receivers.get(i);
        while (descendingIterator.hasNext()) {
            SomeArgs next = descendingIterator.next();
            BroadcastRecord broadcastRecord2 = (BroadcastRecord) next.arg1;
            int i2 = next.argi1;
            Object obj2 = broadcastRecord2.receivers.get(i2);
            if (broadcastRecord.callingUid == broadcastRecord2.callingUid && broadcastRecord.userId == broadcastRecord2.userId && broadcastRecord.intent.filterEquals(broadcastRecord2.intent) && BroadcastRecord.isReceiverEquals(obj, obj2) && broadcastRecord.initialSticky == broadcastRecord2.initialSticky && broadcastRecord2.allReceiversPending() && broadcastRecord.isMatchingRecord(broadcastRecord2)) {
                next.arg1 = broadcastRecord;
                next.argi1 = i;
                broadcastRecord.copyEnqueueTimeFrom(broadcastRecord2);
                onBroadcastDequeued(broadcastRecord2, i2);
                onBroadcastEnqueued(broadcastRecord, i);
                return broadcastRecord2;
            }
        }
        return null;
    }

    public boolean forEachMatchingBroadcast(BroadcastPredicate broadcastPredicate, BroadcastConsumer broadcastConsumer, boolean z) {
        return forEachMatchingBroadcastInQueue(this.mPendingOffload, broadcastPredicate, broadcastConsumer, z) | forEachMatchingBroadcastInQueue(this.mPending, broadcastPredicate, broadcastConsumer, z) | false | forEachMatchingBroadcastInQueue(this.mPendingUrgent, broadcastPredicate, broadcastConsumer, z);
    }

    private boolean forEachMatchingBroadcastInQueue(ArrayDeque<SomeArgs> arrayDeque, BroadcastPredicate broadcastPredicate, BroadcastConsumer broadcastConsumer, boolean z) {
        Iterator<SomeArgs> it = arrayDeque.iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            SomeArgs next = it.next();
            BroadcastRecord broadcastRecord = (BroadcastRecord) next.arg1;
            int i = next.argi1;
            if (broadcastPredicate.test(broadcastRecord, i)) {
                broadcastConsumer.accept(broadcastRecord, i);
                if (z) {
                    next.recycle();
                    it.remove();
                    onBroadcastDequeued(broadcastRecord, i);
                } else {
                    invalidateRunnableAt();
                }
                z2 = true;
            }
        }
        return z2;
    }

    public boolean setProcessAndUidState(ProcessRecord processRecord, boolean z, boolean z2) {
        this.app = processRecord;
        this.mCachedToString = null;
        this.mCachedToShortString = null;
        if (processRecord != null) {
            return setProcessPersistent(processRecord.isPersistent()) | setUidForeground(z) | false | setProcessFreezable(z2) | setProcessInstrumented(processRecord.getActiveInstrumentation() != null);
        }
        return setProcessPersistent(false) | setUidForeground(false) | false | setProcessFreezable(false) | setProcessInstrumented(false);
    }

    private boolean setUidForeground(boolean z) {
        if (this.mUidForeground == z) {
            return false;
        }
        this.mUidForeground = z;
        invalidateRunnableAt();
        return true;
    }

    private boolean setProcessFreezable(boolean z) {
        if (this.mProcessFreezable == z) {
            return false;
        }
        this.mProcessFreezable = z;
        invalidateRunnableAt();
        return true;
    }

    private boolean setProcessInstrumented(boolean z) {
        if (this.mProcessInstrumented == z) {
            return false;
        }
        this.mProcessInstrumented = z;
        invalidateRunnableAt();
        return true;
    }

    private boolean setProcessPersistent(boolean z) {
        if (this.mProcessPersistent == z) {
            return false;
        }
        this.mProcessPersistent = z;
        invalidateRunnableAt();
        return true;
    }

    public boolean isProcessWarm() {
        ProcessRecord processRecord = this.app;
        return (processRecord == null || processRecord.getOnewayThread() == null || this.app.isKilled()) ? false : true;
    }

    public int getPreferredSchedulingGroupLocked() {
        if (!isActive()) {
            return Integer.MIN_VALUE;
        }
        if (this.mCountForeground > this.mCountForegroundDeferred) {
            return 2;
        }
        BroadcastRecord broadcastRecord = this.mActive;
        return (broadcastRecord == null || !broadcastRecord.isForeground()) ? 0 : 2;
    }

    public int getActiveCountSinceIdle() {
        return this.mActiveCountSinceIdle;
    }

    public int getActiveAssumedDeliveryCountSinceIdle() {
        return this.mActiveAssumedDeliveryCountSinceIdle;
    }

    public void setActiveViaColdStart(boolean z) {
        this.mActiveViaColdStart = z;
    }

    public void setActiveWasStopped(boolean z) {
        this.mActiveWasStopped = z;
    }

    public boolean getActiveViaColdStart() {
        return this.mActiveViaColdStart;
    }

    public boolean getActiveWasStopped() {
        return this.mActiveWasStopped;
    }

    public String getPackageName() {
        ProcessRecord processRecord = this.app;
        if (processRecord == null) {
            return null;
        }
        return processRecord.getApplicationInfo().packageName;
    }

    public void makeActiveNextPending() {
        SomeArgs removeNextBroadcast = removeNextBroadcast();
        BroadcastRecord broadcastRecord = (BroadcastRecord) removeNextBroadcast.arg1;
        this.mActive = broadcastRecord;
        int i = removeNextBroadcast.argi1;
        this.mActiveIndex = i;
        this.mActiveCountSinceIdle++;
        this.mActiveAssumedDeliveryCountSinceIdle += broadcastRecord.isAssumedDelivered(i) ? 1 : 0;
        this.mActiveViaColdStart = false;
        this.mActiveWasStopped = false;
        removeNextBroadcast.recycle();
        onBroadcastDequeued(this.mActive, this.mActiveIndex);
    }

    public void makeActiveIdle() {
        this.mActive = null;
        this.mActiveIndex = 0;
        this.mActiveCountSinceIdle = 0;
        this.mActiveAssumedDeliveryCountSinceIdle = 0;
        this.mActiveViaColdStart = false;
        invalidateRunnableAt();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBroadcastEnqueued(BroadcastRecord broadcastRecord, int i) {
        this.mCountEnqueued++;
        if (broadcastRecord.deferUntilActive) {
            this.mCountDeferred++;
        }
        if (broadcastRecord.isForeground()) {
            if (broadcastRecord.deferUntilActive) {
                this.mCountForegroundDeferred++;
            }
            this.mCountForeground++;
        }
        if (broadcastRecord.ordered) {
            this.mCountOrdered++;
        }
        if (broadcastRecord.alarm) {
            this.mCountAlarm++;
        }
        if (broadcastRecord.prioritized) {
            if (broadcastRecord.deferUntilActive) {
                this.mCountPrioritizedDeferred++;
            }
            this.mCountPrioritized++;
        }
        if (broadcastRecord.interactive) {
            this.mCountInteractive++;
        }
        if (broadcastRecord.resultTo != null) {
            this.mCountResultTo++;
        }
        if (broadcastRecord.callerInstrumented) {
            this.mCountInstrumented++;
        }
        if (broadcastRecord.receivers.get(i) instanceof ResolveInfo) {
            this.mCountManifest++;
        }
        invalidateRunnableAt();
    }

    private void onBroadcastDequeued(BroadcastRecord broadcastRecord, int i) {
        this.mCountEnqueued--;
        if (broadcastRecord.deferUntilActive) {
            this.mCountDeferred--;
        }
        if (broadcastRecord.isForeground()) {
            if (broadcastRecord.deferUntilActive) {
                this.mCountForegroundDeferred--;
            }
            this.mCountForeground--;
        }
        if (broadcastRecord.ordered) {
            this.mCountOrdered--;
        }
        if (broadcastRecord.alarm) {
            this.mCountAlarm--;
        }
        if (broadcastRecord.prioritized) {
            if (broadcastRecord.deferUntilActive) {
                this.mCountPrioritizedDeferred--;
            }
            this.mCountPrioritized--;
        }
        if (broadcastRecord.interactive) {
            this.mCountInteractive--;
        }
        if (broadcastRecord.resultTo != null) {
            this.mCountResultTo--;
        }
        if (broadcastRecord.callerInstrumented) {
            this.mCountInstrumented--;
        }
        if (broadcastRecord.receivers.get(i) instanceof ResolveInfo) {
            this.mCountManifest--;
        }
        invalidateRunnableAt();
    }

    public void traceProcessStartingBegin() {
        Trace.asyncTraceForTrackBegin(64L, this.runningTraceTrackName, toShortString() + " starting", hashCode());
    }

    public void traceProcessRunningBegin() {
        Trace.asyncTraceForTrackBegin(64L, this.runningTraceTrackName, toShortString() + " running", hashCode());
    }

    public void traceProcessEnd() {
        Trace.asyncTraceForTrackEnd(64L, this.runningTraceTrackName, hashCode());
    }

    public void traceActiveBegin() {
        Trace.asyncTraceForTrackBegin(64L, this.runningTraceTrackName, this.mActive.toShortString() + " scheduled", hashCode());
    }

    public void traceActiveEnd() {
        Trace.asyncTraceForTrackEnd(64L, this.runningTraceTrackName, hashCode());
    }

    public BroadcastRecord getActive() {
        BroadcastRecord broadcastRecord = this.mActive;
        Objects.requireNonNull(broadcastRecord);
        return broadcastRecord;
    }

    public int getActiveIndex() {
        Objects.requireNonNull(this.mActive);
        return this.mActiveIndex;
    }

    public boolean isEmpty() {
        return this.mPending.isEmpty() && this.mPendingUrgent.isEmpty() && this.mPendingOffload.isEmpty();
    }

    public boolean isActive() {
        return this.mActive != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean forceDelayBroadcastDelivery(long j) {
        if (this.mForcedDelayedDurationMs == j) {
            return false;
        }
        this.mForcedDelayedDurationMs = j;
        invalidateRunnableAt();
        return true;
    }

    private SomeArgs removeNextBroadcast() {
        ArrayDeque<SomeArgs> queueForNextBroadcast = queueForNextBroadcast();
        if (queueForNextBroadcast == this.mPendingUrgent) {
            this.mActiveCountConsecutiveUrgent++;
        } else if (queueForNextBroadcast == this.mPending) {
            this.mActiveCountConsecutiveUrgent = 0;
            this.mActiveCountConsecutiveNormal++;
        } else if (queueForNextBroadcast == this.mPendingOffload) {
            this.mActiveCountConsecutiveUrgent = 0;
            this.mActiveCountConsecutiveNormal = 0;
        }
        if (isQueueEmpty(queueForNextBroadcast)) {
            return null;
        }
        return queueForNextBroadcast.removeFirst();
    }

    ArrayDeque<SomeArgs> queueForNextBroadcast() {
        return queueForNextBroadcast(this.mPendingUrgent, queueForNextBroadcast(this.mPending, this.mPendingOffload, this.mActiveCountConsecutiveNormal, this.constants.MAX_CONSECUTIVE_NORMAL_DISPATCHES), this.mActiveCountConsecutiveUrgent, this.constants.MAX_CONSECUTIVE_URGENT_DISPATCHES);
    }

    private ArrayDeque<SomeArgs> queueForNextBroadcast(ArrayDeque<SomeArgs> arrayDeque, ArrayDeque<SomeArgs> arrayDeque2, int i, int i2) {
        if (isQueueEmpty(arrayDeque)) {
            return arrayDeque2;
        }
        if (isQueueEmpty(arrayDeque2)) {
            return arrayDeque;
        }
        SomeArgs peekFirst = arrayDeque2.peekFirst();
        BroadcastRecord broadcastRecord = (BroadcastRecord) peekFirst.arg1;
        int i3 = peekFirst.argi1;
        BroadcastRecord broadcastRecord2 = (BroadcastRecord) arrayDeque.peekFirst().arg1;
        boolean z = false;
        if ((this.mCountPrioritizeEarliestRequests > 0 || i >= i2) && broadcastRecord.enqueueTime <= broadcastRecord2.enqueueTime && !broadcastRecord.isBlocked(i3)) {
            z = true;
        }
        return z ? arrayDeque2 : arrayDeque;
    }

    private static boolean isQueueEmpty(ArrayDeque<SomeArgs> arrayDeque) {
        return arrayDeque == null || arrayDeque.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean addPrioritizeEarliestRequest() {
        int i = this.mCountPrioritizeEarliestRequests;
        if (i == 0) {
            this.mCountPrioritizeEarliestRequests = i + 1;
            invalidateRunnableAt();
            return true;
        }
        this.mCountPrioritizeEarliestRequests = i + 1;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removePrioritizeEarliestRequest() {
        int i = this.mCountPrioritizeEarliestRequests - 1;
        this.mCountPrioritizeEarliestRequests = i;
        if (i == 0) {
            invalidateRunnableAt();
            return true;
        }
        if (i < 0) {
            this.mCountPrioritizeEarliestRequests = 0;
        }
        return false;
    }

    SomeArgs peekNextBroadcast() {
        ArrayDeque<SomeArgs> queueForNextBroadcast = queueForNextBroadcast();
        if (isQueueEmpty(queueForNextBroadcast)) {
            return null;
        }
        return queueForNextBroadcast.peekFirst();
    }

    @VisibleForTesting
    BroadcastRecord peekNextBroadcastRecord() {
        ArrayDeque<SomeArgs> queueForNextBroadcast = queueForNextBroadcast();
        if (isQueueEmpty(queueForNextBroadcast)) {
            return null;
        }
        return (BroadcastRecord) queueForNextBroadcast.peekFirst().arg1;
    }

    public boolean isPendingManifest() {
        return this.mCountManifest > 0;
    }

    public boolean isPendingOrdered() {
        return this.mCountOrdered > 0;
    }

    public boolean isPendingResultTo() {
        return this.mCountResultTo > 0;
    }

    public boolean isPendingUrgent() {
        BroadcastRecord peekNextBroadcastRecord = peekNextBroadcastRecord();
        if (peekNextBroadcastRecord != null) {
            return peekNextBroadcastRecord.isUrgent();
        }
        return false;
    }

    public boolean isIdle() {
        return (!isActive() && isEmpty()) || isDeferredUntilActive();
    }

    public boolean isBeyondBarrierLocked(long j) {
        SomeArgs peekFirst = this.mPending.peekFirst();
        SomeArgs peekFirst2 = this.mPendingUrgent.peekFirst();
        SomeArgs peekFirst3 = this.mPendingOffload.peekFirst();
        BroadcastRecord broadcastRecord = this.mActive;
        return ((broadcastRecord == null || (broadcastRecord.enqueueTime > j ? 1 : (broadcastRecord.enqueueTime == j ? 0 : -1)) > 0) && (peekFirst == null || (((BroadcastRecord) peekFirst.arg1).enqueueTime > j ? 1 : (((BroadcastRecord) peekFirst.arg1).enqueueTime == j ? 0 : -1)) > 0) && (peekFirst2 == null || (((BroadcastRecord) peekFirst2.arg1).enqueueTime > j ? 1 : (((BroadcastRecord) peekFirst2.arg1).enqueueTime == j ? 0 : -1)) > 0) && (peekFirst3 == null || (((BroadcastRecord) peekFirst3.arg1).enqueueTime > j ? 1 : (((BroadcastRecord) peekFirst3.arg1).enqueueTime == j ? 0 : -1)) > 0)) || isDeferredUntilActive();
    }

    public boolean isDispatched(Intent intent) {
        BroadcastRecord broadcastRecord = this.mActive;
        return ((broadcastRecord == null || !intent.filterEquals(broadcastRecord.intent)) && isDispatchedInQueue(this.mPending, intent) && isDispatchedInQueue(this.mPendingUrgent, intent) && isDispatchedInQueue(this.mPendingOffload, intent)) || isDeferredUntilActive();
    }

    private boolean isDispatchedInQueue(ArrayDeque<SomeArgs> arrayDeque, Intent intent) {
        SomeArgs next;
        Iterator<SomeArgs> it = arrayDeque.iterator();
        while (it.hasNext() && (next = it.next()) != null) {
            if (intent.filterEquals(((BroadcastRecord) next.arg1).intent)) {
                return false;
            }
        }
        return true;
    }

    public boolean isRunnable() {
        if (this.mRunnableAtInvalidated) {
            updateRunnableAt();
        }
        return this.mRunnableAt != Long.MAX_VALUE;
    }

    public boolean isDeferredUntilActive() {
        if (this.mRunnableAtInvalidated) {
            updateRunnableAt();
        }
        return this.mRunnableAtReason == 8;
    }

    public boolean hasDeferredBroadcasts() {
        return this.mCountDeferred > 0;
    }

    public long getRunnableAt() {
        if (this.mRunnableAtInvalidated) {
            updateRunnableAt();
        }
        return this.mRunnableAt;
    }

    public int getRunnableAtReason() {
        if (this.mRunnableAtInvalidated) {
            updateRunnableAt();
        }
        return this.mRunnableAtReason;
    }

    public void invalidateRunnableAt() {
        this.mRunnableAtInvalidated = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String reasonToString(int i) {
        String reasonToStringExtend = mStaticExt.reasonToStringExtend(i);
        if (reasonToStringExtend != null) {
            return reasonToStringExtend;
        }
        switch (i) {
            case 0:
                return "EMPTY";
            case 1:
                return "CACHED";
            case 2:
                return "NORMAL";
            case 3:
                return "MAX_PENDING";
            case 4:
                return "BLOCKED";
            case 5:
                return "INSTRUMENTED";
            case 6:
                return "PERSISTENT";
            case 7:
                return "FORCE_DELAYED";
            case 8:
                return "INFINITE_DEFER";
            case 9:
            default:
                return Integer.toString(i);
            case 10:
                return "CONTAINS_FOREGROUND";
            case 11:
                return "CONTAINS_ORDERED";
            case 12:
                return "CONTAINS_ALARM";
            case 13:
                return "CONTAINS_PRIORITIZED";
            case 14:
                return "CONTAINS_INTERACTIVE";
            case 15:
                return "CONTAINS_RESULT_TO";
            case 16:
                return "CONTAINS_INSTRUMENTED";
            case 17:
                return "CONTAINS_MANIFEST";
            case 18:
                return "FOREGROUND";
            case 19:
                return "CORE_UID";
            case 20:
                return "TOP_PROCESS";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateRunnableAt() {
        if (this.mRunnableAtInvalidated) {
            this.mRunnableAtInvalidated = false;
            SomeArgs peekNextBroadcast = peekNextBroadcast();
            if (peekNextBroadcast != null) {
                BroadcastRecord broadcastRecord = (BroadcastRecord) peekNextBroadcast.arg1;
                int i = peekNextBroadcast.argi1;
                long j = broadcastRecord.enqueueTime;
                if (broadcastRecord.isBlocked(i)) {
                    this.mRunnableAt = Long.MAX_VALUE;
                    this.mRunnableAtReason = 4;
                    return;
                }
                long j2 = this.mForcedDelayedDurationMs;
                if (j2 > 0) {
                    this.mRunnableAt = j2 + j;
                    this.mRunnableAtReason = 7;
                } else if (this.mCountForeground > this.mCountForegroundDeferred) {
                    this.mRunnableAt = this.constants.DELAY_URGENT_MILLIS + j;
                    this.mRunnableAtReason = 10;
                } else if (this.mCountInteractive > 0) {
                    this.mRunnableAt = this.constants.DELAY_URGENT_MILLIS + j;
                    this.mRunnableAtReason = 14;
                } else if (this.mCountInstrumented > 0) {
                    this.mRunnableAt = this.constants.DELAY_URGENT_MILLIS + j;
                    this.mRunnableAtReason = 16;
                } else if (this.mProcessInstrumented) {
                    this.mRunnableAt = this.constants.DELAY_URGENT_MILLIS + j;
                    this.mRunnableAtReason = 5;
                } else if (this.mUidForeground) {
                    this.mRunnableAt = this.constants.DELAY_FOREGROUND_PROC_MILLIS + j;
                    this.mRunnableAtReason = 18;
                } else {
                    ProcessRecord processRecord = this.app;
                    if (processRecord != null && processRecord.getSetProcState() == 2) {
                        this.mRunnableAt = this.constants.DELAY_FOREGROUND_PROC_MILLIS + j;
                        this.mRunnableAtReason = 20;
                    } else if (this.mProcessPersistent) {
                        this.mRunnableAt = this.constants.DELAY_PERSISTENT_PROC_MILLIS + j;
                        this.mRunnableAtReason = 6;
                    } else if (this.mCountOrdered > 0) {
                        this.mRunnableAt = j;
                        this.mRunnableAtReason = 11;
                    } else if (this.mCountAlarm > 0) {
                        this.mRunnableAt = j;
                        this.mRunnableAtReason = 12;
                    } else if (this.mCountPrioritized > this.mCountPrioritizedDeferred) {
                        this.mRunnableAt = j;
                        this.mRunnableAtReason = 13;
                    } else if (this.mCountManifest > 0) {
                        this.mRunnableAt = j;
                        this.mRunnableAtReason = 17;
                    } else if (this.mProcessFreezable) {
                        if (broadcastRecord.deferUntilActive) {
                            if (this.mCountDeferred == this.mCountEnqueued) {
                                this.mRunnableAt = Long.MAX_VALUE;
                                this.mRunnableAtReason = 8;
                            } else if (broadcastRecord.isForeground()) {
                                this.mRunnableAt = this.constants.DELAY_URGENT_MILLIS + j;
                                this.mRunnableAtReason = 10;
                            } else if (broadcastRecord.prioritized) {
                                this.mRunnableAt = j;
                                this.mRunnableAtReason = 13;
                            } else if (broadcastRecord.resultTo != null) {
                                this.mRunnableAt = j;
                                this.mRunnableAtReason = 15;
                            } else {
                                this.mRunnableAt = this.constants.DELAY_CACHED_MILLIS + j;
                                this.mRunnableAtReason = 1;
                            }
                        } else {
                            this.mRunnableAt = this.constants.DELAY_CACHED_MILLIS + j;
                            this.mRunnableAtReason = 1;
                        }
                    } else if (this.mCountResultTo > 0) {
                        this.mRunnableAt = j;
                        this.mRunnableAtReason = 15;
                    } else if (UserHandle.isCore(this.uid)) {
                        this.mRunnableAt = j;
                        this.mRunnableAtReason = 19;
                    } else {
                        this.mRunnableAt = this.constants.DELAY_NORMAL_MILLIS + j;
                        this.mRunnableAtReason = 2;
                    }
                }
                long customizedRunnableAt = this.mBroadcastProcessQueueExt.getCustomizedRunnableAt(j);
                if (this.mRunnableAt > customizedRunnableAt) {
                    this.mRunnableAt = customizedRunnableAt;
                    this.mRunnableAtReason = 101;
                }
                if (this.mPending.size() + this.mPendingUrgent.size() + this.mPendingOffload.size() >= this.constants.MAX_PENDING_BROADCASTS) {
                    this.mRunnableAt = Math.min(this.mRunnableAt, j);
                    this.mRunnableAtReason = 3;
                    return;
                }
                return;
            }
            this.mRunnableAt = Long.MAX_VALUE;
            this.mRunnableAtReason = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateDeferredStates(BroadcastConsumer broadcastConsumer, BroadcastConsumer broadcastConsumer2) {
        boolean shouldBeDeferred = shouldBeDeferred();
        if (this.mLastDeferredStates != shouldBeDeferred) {
            this.mLastDeferredStates = shouldBeDeferred;
            if (shouldBeDeferred) {
                forEachMatchingBroadcast(new BroadcastPredicate() { // from class: com.android.server.am.BroadcastProcessQueue$$ExternalSyntheticLambda1
                    @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
                    public final boolean test(BroadcastRecord broadcastRecord, int i) {
                        boolean lambda$updateDeferredStates$0;
                        lambda$updateDeferredStates$0 = BroadcastProcessQueue.lambda$updateDeferredStates$0(broadcastRecord, i);
                        return lambda$updateDeferredStates$0;
                    }
                }, broadcastConsumer, false);
            } else {
                forEachMatchingBroadcast(new BroadcastPredicate() { // from class: com.android.server.am.BroadcastProcessQueue$$ExternalSyntheticLambda2
                    @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
                    public final boolean test(BroadcastRecord broadcastRecord, int i) {
                        boolean lambda$updateDeferredStates$1;
                        lambda$updateDeferredStates$1 = BroadcastProcessQueue.lambda$updateDeferredStates$1(broadcastRecord, i);
                        return lambda$updateDeferredStates$1;
                    }
                }, broadcastConsumer2, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateDeferredStates$0(BroadcastRecord broadcastRecord, int i) {
        return broadcastRecord.getDeliveryState(i) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateDeferredStates$1(BroadcastRecord broadcastRecord, int i) {
        return broadcastRecord.getDeliveryState(i) == 6;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearDeferredStates(BroadcastConsumer broadcastConsumer) {
        if (this.mLastDeferredStates) {
            this.mLastDeferredStates = false;
            forEachMatchingBroadcast(new BroadcastPredicate() { // from class: com.android.server.am.BroadcastProcessQueue$$ExternalSyntheticLambda0
                @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
                public final boolean test(BroadcastRecord broadcastRecord, int i) {
                    boolean lambda$clearDeferredStates$2;
                    lambda$clearDeferredStates$2 = BroadcastProcessQueue.lambda$clearDeferredStates$2(broadcastRecord, i);
                    return lambda$clearDeferredStates$2;
                }
            }, broadcastConsumer, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$clearDeferredStates$2(BroadcastRecord broadcastRecord, int i) {
        return broadcastRecord.getDeliveryState(i) == 6;
    }

    @VisibleForTesting
    boolean shouldBeDeferred() {
        if (this.mRunnableAtInvalidated) {
            updateRunnableAt();
        }
        int i = this.mRunnableAtReason;
        return i == 1 || i == 8;
    }

    public void assertHealthLocked() {
        if (!isActive()) {
            Preconditions.checkState(!this.mRunnableAtInvalidated, "mRunnableAtInvalidated");
        }
        assertHealthLocked(this.mPending);
        assertHealthLocked(this.mPendingUrgent);
        assertHealthLocked(this.mPendingOffload);
    }

    private void assertHealthLocked(ArrayDeque<SomeArgs> arrayDeque) {
        ArrayList<String> beginAssertHealthLocked = this.mBroadcastProcessQueueExt.beginAssertHealthLocked();
        if (arrayDeque.isEmpty()) {
            return;
        }
        Iterator<SomeArgs> descendingIterator = arrayDeque.descendingIterator();
        while (descendingIterator.hasNext()) {
            SomeArgs next = descendingIterator.next();
            BroadcastRecord broadcastRecord = (BroadcastRecord) next.arg1;
            if (!BroadcastRecord.isDeliveryStateTerminal(broadcastRecord.getDeliveryState(next.argi1)) && !broadcastRecord.isDeferUntilActive()) {
                long uptimeMillis = SystemClock.uptimeMillis() - broadcastRecord.enqueueTime;
                this.mBroadcastProcessQueueExt.assertHealthLocked(broadcastRecord, uptimeMillis, beginAssertHealthLocked);
                Preconditions.checkState(uptimeMillis < BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_FUZZ_MILLISECONDS, "waitingTime");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public static BroadcastProcessQueue insertIntoRunnableList(BroadcastProcessQueue broadcastProcessQueue, BroadcastProcessQueue broadcastProcessQueue2) {
        if (broadcastProcessQueue == null) {
            return broadcastProcessQueue2;
        }
        long runnableAt = broadcastProcessQueue2.getRunnableAt();
        BroadcastProcessQueue broadcastProcessQueue3 = null;
        BroadcastProcessQueue broadcastProcessQueue4 = broadcastProcessQueue;
        while (broadcastProcessQueue4 != null) {
            if (broadcastProcessQueue4.getRunnableAt() > runnableAt) {
                broadcastProcessQueue2.runnableAtNext = broadcastProcessQueue4;
                broadcastProcessQueue2.runnableAtPrev = broadcastProcessQueue4.runnableAtPrev;
                broadcastProcessQueue4.runnableAtPrev = broadcastProcessQueue2;
                BroadcastProcessQueue broadcastProcessQueue5 = broadcastProcessQueue2.runnableAtPrev;
                if (broadcastProcessQueue5 != null) {
                    broadcastProcessQueue5.runnableAtNext = broadcastProcessQueue2;
                }
                return broadcastProcessQueue4 == broadcastProcessQueue ? broadcastProcessQueue2 : broadcastProcessQueue;
            }
            broadcastProcessQueue3 = broadcastProcessQueue4;
            broadcastProcessQueue4 = broadcastProcessQueue4.runnableAtNext;
        }
        broadcastProcessQueue2.runnableAtPrev = broadcastProcessQueue3;
        broadcastProcessQueue3.runnableAtNext = broadcastProcessQueue2;
        return broadcastProcessQueue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public static BroadcastProcessQueue removeFromRunnableList(BroadcastProcessQueue broadcastProcessQueue, BroadcastProcessQueue broadcastProcessQueue2) {
        if (broadcastProcessQueue == broadcastProcessQueue2) {
            broadcastProcessQueue = broadcastProcessQueue2.runnableAtNext;
        }
        BroadcastProcessQueue broadcastProcessQueue3 = broadcastProcessQueue2.runnableAtNext;
        if (broadcastProcessQueue3 != null) {
            broadcastProcessQueue3.runnableAtPrev = broadcastProcessQueue2.runnableAtPrev;
        }
        BroadcastProcessQueue broadcastProcessQueue4 = broadcastProcessQueue2.runnableAtPrev;
        if (broadcastProcessQueue4 != null) {
            broadcastProcessQueue4.runnableAtNext = broadcastProcessQueue3;
        }
        broadcastProcessQueue2.runnableAtNext = null;
        broadcastProcessQueue2.runnableAtPrev = null;
        return broadcastProcessQueue;
    }

    public String toString() {
        if (this.mCachedToString == null) {
            this.mCachedToString = "BroadcastProcessQueue{" + toShortString() + "}";
        }
        return this.mCachedToString;
    }

    public String toShortString() {
        if (this.mCachedToShortString == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" ");
            ProcessRecord processRecord = this.app;
            sb.append(processRecord != null ? Integer.valueOf(processRecord.getPid()) : "?");
            sb.append(":");
            sb.append(this.processName);
            sb.append("/");
            sb.append(UserHandle.formatUid(this.uid));
            this.mCachedToShortString = sb.toString();
        }
        return this.mCachedToShortString;
    }

    public String describeStateLocked() {
        return describeStateLocked(SystemClock.uptimeMillis());
    }

    public String describeStateLocked(long j) {
        StringBuilder sb = new StringBuilder();
        if (isRunnable()) {
            sb.append("runnable at ");
            TimeUtils.formatDuration(getRunnableAt(), j, sb);
        } else {
            sb.append("not runnable");
        }
        sb.append(" because ");
        sb.append(reasonToString(this.mRunnableAtReason));
        return sb.toString();
    }

    @NeverCompile
    public void dumpLocked(long j, IndentingPrintWriter indentingPrintWriter) {
        if (this.mActive == null && isEmpty()) {
            return;
        }
        indentingPrintWriter.print(toShortString());
        indentingPrintWriter.print(" ");
        indentingPrintWriter.print(describeStateLocked(j));
        indentingPrintWriter.println();
        indentingPrintWriter.increaseIndent();
        dumpProcessState(indentingPrintWriter);
        dumpBroadcastCounts(indentingPrintWriter);
        BroadcastRecord broadcastRecord = this.mActive;
        if (broadcastRecord != null) {
            dumpRecord("ACTIVE", j, indentingPrintWriter, broadcastRecord, this.mActiveIndex);
        }
        Iterator<SomeArgs> it = this.mPendingUrgent.iterator();
        while (it.hasNext()) {
            SomeArgs next = it.next();
            dumpRecord("URGENT", j, indentingPrintWriter, (BroadcastRecord) next.arg1, next.argi1);
        }
        Iterator<SomeArgs> it2 = this.mPending.iterator();
        while (it2.hasNext()) {
            SomeArgs next2 = it2.next();
            dumpRecord(null, j, indentingPrintWriter, (BroadcastRecord) next2.arg1, next2.argi1);
        }
        Iterator<SomeArgs> it3 = this.mPendingOffload.iterator();
        while (it3.hasNext()) {
            SomeArgs next3 = it3.next();
            dumpRecord("OFFLOAD", j, indentingPrintWriter, (BroadcastRecord) next3.arg1, next3.argi1);
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
    }

    @NeverCompile
    private void dumpProcessState(IndentingPrintWriter indentingPrintWriter) {
        StringBuilder sb = new StringBuilder();
        if (this.mUidForeground) {
            sb.append("FG");
        }
        if (this.mProcessFreezable) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("FRZ");
        }
        if (this.mProcessInstrumented) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("INSTR");
        }
        if (this.mProcessPersistent) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("PER");
        }
        if (sb.length() > 0) {
            indentingPrintWriter.print("state:");
            indentingPrintWriter.println(sb);
        }
        if (this.runningOomAdjusted) {
            indentingPrintWriter.print("runningOomAdjusted:");
            indentingPrintWriter.println(this.runningOomAdjusted);
        }
    }

    @NeverCompile
    private void dumpBroadcastCounts(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.print("e:");
        indentingPrintWriter.print(this.mCountEnqueued);
        indentingPrintWriter.print(" d:");
        indentingPrintWriter.print(this.mCountDeferred);
        indentingPrintWriter.print(" f:");
        indentingPrintWriter.print(this.mCountForeground);
        indentingPrintWriter.print(" fd:");
        indentingPrintWriter.print(this.mCountForegroundDeferred);
        indentingPrintWriter.print(" o:");
        indentingPrintWriter.print(this.mCountOrdered);
        indentingPrintWriter.print(" a:");
        indentingPrintWriter.print(this.mCountAlarm);
        indentingPrintWriter.print(" p:");
        indentingPrintWriter.print(this.mCountPrioritized);
        indentingPrintWriter.print(" pd:");
        indentingPrintWriter.print(this.mCountPrioritizedDeferred);
        indentingPrintWriter.print(" int:");
        indentingPrintWriter.print(this.mCountInteractive);
        indentingPrintWriter.print(" rt:");
        indentingPrintWriter.print(this.mCountResultTo);
        indentingPrintWriter.print(" ins:");
        indentingPrintWriter.print(this.mCountInstrumented);
        indentingPrintWriter.print(" m:");
        indentingPrintWriter.print(this.mCountManifest);
        indentingPrintWriter.print(" csi:");
        indentingPrintWriter.print(this.mActiveCountSinceIdle);
        indentingPrintWriter.print(" adcsi:");
        indentingPrintWriter.print(this.mActiveAssumedDeliveryCountSinceIdle);
        indentingPrintWriter.print(" ccu:");
        indentingPrintWriter.print(this.mActiveCountConsecutiveUrgent);
        indentingPrintWriter.print(" ccn:");
        indentingPrintWriter.print(this.mActiveCountConsecutiveNormal);
        indentingPrintWriter.println();
    }

    @NeverCompile
    private void dumpRecord(String str, long j, IndentingPrintWriter indentingPrintWriter, BroadcastRecord broadcastRecord, int i) {
        TimeUtils.formatDuration(broadcastRecord.enqueueTime, j, indentingPrintWriter);
        indentingPrintWriter.print(' ');
        indentingPrintWriter.println(broadcastRecord.toString());
        indentingPrintWriter.print("    ");
        int i2 = broadcastRecord.delivery[i];
        indentingPrintWriter.print(BroadcastRecord.deliveryStateToString(i2));
        if (i2 == 4) {
            indentingPrintWriter.print(" at ");
            TimeUtils.formatDuration(broadcastRecord.scheduledTime[i], j, indentingPrintWriter);
        }
        if (str != null) {
            indentingPrintWriter.print(' ');
            indentingPrintWriter.print(str);
        }
        Object obj = broadcastRecord.receivers.get(i);
        if (obj instanceof BroadcastFilter) {
            indentingPrintWriter.print(" for registered ");
            indentingPrintWriter.print(Integer.toHexString(System.identityHashCode((BroadcastFilter) obj)));
        } else {
            indentingPrintWriter.print(" for manifest ");
            indentingPrintWriter.print(((ResolveInfo) obj).activityInfo.name);
        }
        indentingPrintWriter.println();
        int i3 = broadcastRecord.blockedUntilBeyondCount[i];
        if (i3 != -1) {
            indentingPrintWriter.print("    blocked until ");
            indentingPrintWriter.print(i3);
            indentingPrintWriter.print(", currently at ");
            indentingPrintWriter.print(broadcastRecord.beyondCount);
            indentingPrintWriter.print(" of ");
            indentingPrintWriter.print(broadcastRecord.receivers.size());
            indentingPrintWriter.print(", recordIndex: ");
            indentingPrintWriter.println(i);
        }
    }

    public IBroadcastProcessQueueWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class BroadcastProcessQueueWrapper implements IBroadcastProcessQueueWrapper {
        private BroadcastProcessQueueWrapper() {
        }

        @Override // com.android.server.am.IBroadcastProcessQueueWrapper
        public IBroadcastProcessQueueExt getExtImpl() {
            return BroadcastProcessQueue.this.mBroadcastProcessQueueExt;
        }

        @Override // com.android.server.am.IBroadcastProcessQueueWrapper
        public void enqueueBroadcast(BroadcastRecord broadcastRecord, int i, boolean z, boolean z2) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = broadcastRecord;
            obtain.argi1 = i;
            obtain.argi2 = z ? 1 : 0;
            if (z2) {
                BroadcastProcessQueue.this.getQueueForBroadcast(broadcastRecord).addFirst(obtain);
            } else {
                BroadcastProcessQueue.this.getQueueForBroadcast(broadcastRecord).addLast(obtain);
            }
            BroadcastProcessQueue.this.onBroadcastEnqueued(broadcastRecord, i);
        }

        @Override // com.android.server.am.IBroadcastProcessQueueWrapper
        public long getRunnableAtWithoutRefresh() {
            return BroadcastProcessQueue.this.mRunnableAt;
        }

        @Override // com.android.server.am.IBroadcastProcessQueueWrapper
        public int getRunnableAtReasonWithoutRefresh() {
            return BroadcastProcessQueue.this.mRunnableAtReason;
        }
    }
}
