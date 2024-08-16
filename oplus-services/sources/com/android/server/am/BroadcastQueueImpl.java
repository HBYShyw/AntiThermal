package com.android.server.am;

import android.app.BroadcastOptions;
import android.app.IApplicationThread;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.content.res.CompatibilityInfo;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.IndentingPrintWriter;
import android.util.Slog;
import android.util.SparseIntArray;
import android.util.proto.ProtoOutputStream;
import com.android.internal.os.TimeoutRecord;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.LocalServices;
import com.android.server.pm.UserJourneyLogger;
import com.android.server.pm.UserManagerInternal;
import dalvik.annotation.optimization.NeverCompile;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BroadcastQueueImpl extends BroadcastQueue {
    static final int BROADCAST_INTENT_MSG = 200;
    private static final int BROADCAST_NEXT_MSG = 202;
    static final int BROADCAST_TIMEOUT_MSG = 201;
    private static final String TAG_BROADCAST = BroadcastQueue.TAG + ActivityManagerDebugConfig.POSTFIX_BROADCAST;
    private static final String TAG_MU = "BroadcastQueue_MU";
    private IBroadcastQueueExt mBroadcastQueueExt;
    boolean mBroadcastsScheduled;
    final BroadcastConstants mConstants;
    final boolean mDelayBehindServices;
    final BroadcastDispatcher mDispatcher;
    final BroadcastHandler mHandler;
    boolean mLogLatencyMetrics;
    private int mNextToken;
    final ArrayList<BroadcastRecord> mParallelBroadcasts;
    BroadcastRecord mPendingBroadcast;
    int mPendingBroadcastRecvIndex;
    boolean mPendingBroadcastTimeoutMessage;
    final int mSchedGroup;
    final SparseIntArray mSplitRefcounts;
    private BroadcastQueueWrapper mWrapper;

    @Override // com.android.server.am.BroadcastQueue
    public void onProcessFreezableChangedLocked(ProcessRecord processRecord) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class BroadcastHandler extends Handler {
        public BroadcastHandler(Looper looper) {
            super(looper, null);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 200) {
                if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                    Slog.v(BroadcastQueueImpl.TAG_BROADCAST, "Received BROADCAST_INTENT_MSG [" + BroadcastQueueImpl.this.mQueueName + "]");
                }
                BroadcastQueueImpl.this.processNextBroadcast(true);
                return;
            }
            if (i != 201) {
                return;
            }
            ActivityManagerService activityManagerService = BroadcastQueueImpl.this.mService;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    BroadcastQueueImpl.this.broadcastTimeoutLocked(true);
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BroadcastQueueImpl(ActivityManagerService activityManagerService, Handler handler, String str, BroadcastConstants broadcastConstants, boolean z, int i) {
        this(activityManagerService, handler, str, broadcastConstants, new BroadcastSkipPolicy(activityManagerService), new BroadcastHistory(broadcastConstants), z, i);
    }

    BroadcastQueueImpl(ActivityManagerService activityManagerService, Handler handler, String str, BroadcastConstants broadcastConstants, BroadcastSkipPolicy broadcastSkipPolicy, BroadcastHistory broadcastHistory, boolean z, int i) {
        super(activityManagerService, handler, str, broadcastSkipPolicy, broadcastHistory);
        this.mParallelBroadcasts = new ArrayList<>();
        this.mSplitRefcounts = new SparseIntArray();
        this.mNextToken = 0;
        this.mBroadcastsScheduled = false;
        this.mPendingBroadcast = null;
        this.mLogLatencyMetrics = true;
        this.mWrapper = new BroadcastQueueWrapper();
        this.mBroadcastQueueExt = (IBroadcastQueueExt) ExtLoader.type(IBroadcastQueueExt.class).create();
        BroadcastHandler broadcastHandler = new BroadcastHandler(handler.getLooper());
        this.mHandler = broadcastHandler;
        this.mConstants = broadcastConstants;
        this.mDelayBehindServices = z;
        this.mSchedGroup = i;
        BroadcastDispatcher broadcastDispatcher = new BroadcastDispatcher(this, broadcastConstants, broadcastHandler, this.mService);
        this.mDispatcher = broadcastDispatcher;
        this.mBroadcastQueueExt.initOplusBroadcastQueueEx(activityManagerService, this, broadcastHandler, this.mQueueName, broadcastDispatcher);
    }

    @Override // com.android.server.am.BroadcastQueue
    public void start(ContentResolver contentResolver) {
        this.mDispatcher.start();
        this.mConstants.startObserving(this.mHandler, contentResolver);
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean isDelayBehindServices() {
        return this.mDelayBehindServices;
    }

    public BroadcastRecord getPendingBroadcastLocked() {
        String str;
        if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            String str2 = TAG_BROADCAST;
            StringBuilder sb = new StringBuilder();
            sb.append(" mPendingBroadcast = ");
            sb.append(this.mPendingBroadcast);
            if (this.mPendingBroadcast != null) {
                str = " curApp.pid = " + this.mPendingBroadcast.curApp.getPid();
            } else {
                str = " null";
            }
            sb.append(str);
            Slog.v(str2, sb.toString());
        }
        return this.mPendingBroadcast;
    }

    public BroadcastRecord getActiveBroadcastLocked() {
        return this.mDispatcher.getActiveBroadcastLocked();
    }

    @Override // com.android.server.am.BroadcastQueue
    public int getPreferredSchedulingGroupLocked(ProcessRecord processRecord) {
        BroadcastRecord activeBroadcastLocked = getActiveBroadcastLocked();
        if (activeBroadcastLocked != null && activeBroadcastLocked.curApp == processRecord) {
            return this.mSchedGroup;
        }
        BroadcastRecord pendingBroadcastLocked = getPendingBroadcastLocked();
        if (pendingBroadcastLocked == null || pendingBroadcastLocked.curApp != processRecord) {
            return Integer.MIN_VALUE;
        }
        return this.mSchedGroup;
    }

    @Override // com.android.server.am.BroadcastQueue
    public void enqueueBroadcastLocked(BroadcastRecord broadcastRecord) {
        BroadcastRecord broadcastRecord2;
        ProcessRecord processRecord;
        Intent intent;
        boolean z;
        int i;
        int i2;
        int i3;
        String str;
        long uptimeMillis;
        ProcessRecord processRecord2;
        broadcastRecord.applySingletonPolicy(this.mService);
        boolean z2 = false;
        boolean z3 = (broadcastRecord.intent.getFlags() & AudioFormat.APTX) != 0;
        boolean z4 = broadcastRecord.ordered;
        if (!z4) {
            List<Object> list = broadcastRecord.receivers;
            int size = list != null ? list.size() : 0;
            int i4 = 0;
            while (true) {
                if (i4 >= size) {
                    break;
                }
                if (broadcastRecord.receivers.get(i4) instanceof ResolveInfo) {
                    z4 = true;
                    break;
                }
                i4++;
            }
        }
        if (z4) {
            BroadcastRecord replaceOrderedBroadcastLocked = z3 ? replaceOrderedBroadcastLocked(broadcastRecord) : null;
            if (replaceOrderedBroadcastLocked != null) {
                IIntentReceiver iIntentReceiver = replaceOrderedBroadcastLocked.resultTo;
                if (iIntentReceiver != null) {
                    try {
                        replaceOrderedBroadcastLocked.mIsReceiverAppRunning = true;
                        processRecord = replaceOrderedBroadcastLocked.resultToApp;
                        intent = replaceOrderedBroadcastLocked.intent;
                        z = replaceOrderedBroadcastLocked.shareIdentity;
                        i = replaceOrderedBroadcastLocked.userId;
                        i2 = replaceOrderedBroadcastLocked.callingUid;
                        i3 = broadcastRecord.callingUid;
                        str = broadcastRecord.callerPackage;
                        uptimeMillis = SystemClock.uptimeMillis() - replaceOrderedBroadcastLocked.enqueueTime;
                        processRecord2 = replaceOrderedBroadcastLocked.resultToApp;
                        broadcastRecord2 = replaceOrderedBroadcastLocked;
                    } catch (RemoteException e) {
                        e = e;
                        broadcastRecord2 = replaceOrderedBroadcastLocked;
                    }
                    try {
                        performReceiveLocked(replaceOrderedBroadcastLocked, processRecord, iIntentReceiver, intent, 0, null, null, false, false, z, i, i2, i3, str, uptimeMillis, 0L, 0, processRecord2 != null ? processRecord2.mState.getCurProcState() : -1);
                        return;
                    } catch (RemoteException e2) {
                        e = e2;
                        Slog.w(BroadcastQueue.TAG, "Failure [" + this.mQueueName + "] sending broadcast result of " + broadcastRecord2.intent, e);
                        return;
                    }
                }
                return;
            }
            enqueueOrderedBroadcastLocked(broadcastRecord);
            scheduleBroadcastsLocked();
            return;
        }
        if (z3 && replaceParallelBroadcastLocked(broadcastRecord) != null) {
            z2 = true;
        }
        if (z2) {
            return;
        }
        enqueueParallelBroadcastLocked(broadcastRecord);
        scheduleBroadcastsLocked();
    }

    public void enqueueParallelBroadcastLocked(BroadcastRecord broadcastRecord) {
        broadcastRecord.enqueueClockTime = System.currentTimeMillis();
        broadcastRecord.enqueueTime = SystemClock.uptimeMillis();
        broadcastRecord.enqueueRealTime = SystemClock.elapsedRealtime();
        this.mParallelBroadcasts.add(broadcastRecord);
        enqueueBroadcastHelper(broadcastRecord);
        this.mBroadcastQueueExt.hookEnqueueParallelBroadcast(this.mParallelBroadcasts, broadcastRecord, BroadcastQueue.TAG);
    }

    public void enqueueOrderedBroadcastLocked(BroadcastRecord broadcastRecord) {
        broadcastRecord.enqueueClockTime = System.currentTimeMillis();
        broadcastRecord.enqueueTime = SystemClock.uptimeMillis();
        broadcastRecord.enqueueRealTime = SystemClock.elapsedRealtime();
        this.mDispatcher.enqueueOrderedBroadcastLocked(broadcastRecord);
        enqueueBroadcastHelper(broadcastRecord);
    }

    private void enqueueBroadcastHelper(BroadcastRecord broadcastRecord) {
        if (Trace.isTagEnabled(64L)) {
            Trace.asyncTraceBegin(64L, createBroadcastTraceTitle(broadcastRecord, 0), System.identityHashCode(broadcastRecord));
        }
    }

    public final BroadcastRecord replaceParallelBroadcastLocked(BroadcastRecord broadcastRecord) {
        return replaceBroadcastLocked(this.mParallelBroadcasts, broadcastRecord, "PARALLEL");
    }

    public final BroadcastRecord replaceOrderedBroadcastLocked(BroadcastRecord broadcastRecord) {
        return this.mDispatcher.replaceBroadcastLocked(broadcastRecord, "ORDERED");
    }

    private BroadcastRecord replaceBroadcastLocked(ArrayList<BroadcastRecord> arrayList, BroadcastRecord broadcastRecord, String str) {
        Intent intent = broadcastRecord.intent;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            BroadcastRecord broadcastRecord2 = arrayList.get(size);
            if (broadcastRecord2.userId == broadcastRecord.userId && intent.filterEquals(broadcastRecord2.intent)) {
                if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                    Slog.v(TAG_BROADCAST, "***** DROPPING " + str + " [" + this.mQueueName + "]: " + intent);
                }
                if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                    Slog.v(TAG_BROADCAST, "queue: " + arrayList + " r " + broadcastRecord + " old " + broadcastRecord2);
                }
                arrayList.set(size, broadcastRecord);
                return broadcastRecord2;
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:56:0x01b2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void processCurBroadcastLocked(BroadcastRecord broadcastRecord, ProcessRecord processRecord) throws RemoteException {
        ProcessRecord processRecord2;
        ProcessReceiverRecord processReceiverRecord;
        String str;
        int i;
        if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            Slog.v(TAG_BROADCAST, "Process cur broadcast " + broadcastRecord + " for app " + processRecord);
        }
        IApplicationThread thread = processRecord.getThread();
        if (thread == null) {
            throw new RemoteException();
        }
        if (processRecord.isInFullBackup()) {
            skipReceiverLocked(broadcastRecord);
            return;
        }
        broadcastRecord.curApp = processRecord;
        broadcastRecord.curAppLastProcessState = processRecord.mState.getCurProcState();
        ProcessReceiverRecord processReceiverRecord2 = processRecord.mReceivers;
        processReceiverRecord2.addCurReceiver(broadcastRecord);
        processRecord.mState.forceProcessStateUpTo(11);
        boolean z = false;
        if (this.mService.mInternal.getRestrictionLevel(processRecord.info.packageName, processRecord.userId) < 40) {
            this.mService.updateLruProcessLocked(processRecord, false, null);
        }
        this.mService.enqueueOomAdjTargetLocked(processRecord);
        this.mService.updateOomAdjPendingTargetsLocked(3);
        maybeReportBroadcastDispatchedEventLocked(broadcastRecord, broadcastRecord.curReceiver.applicationInfo.uid);
        broadcastRecord.intent.setComponent(broadcastRecord.curComponent);
        BroadcastOptions broadcastOptions = broadcastRecord.options;
        if (broadcastOptions != null && broadcastOptions.getTemporaryAppAllowlistDuration() > 0 && broadcastRecord.options.getTemporaryAppAllowlistType() == 4) {
            this.mService.mOomAdjuster.mCachedAppOptimizer.unfreezeTemporarily(processRecord, 3, broadcastRecord.options.getTemporaryAppAllowlistDuration());
        }
        try {
            if (ActivityManagerDebugConfig.DEBUG_BROADCAST_LIGHT) {
                try {
                    Slog.v(TAG_BROADCAST, "Delivering to component " + broadcastRecord.curComponent + ": " + broadcastRecord);
                } catch (Throwable th) {
                    th = th;
                    processRecord2 = null;
                    processReceiverRecord = processReceiverRecord2;
                    if (!z) {
                    }
                    throw th;
                }
            }
            this.mService.notifyPackageUse(broadcastRecord.intent.getComponent().getPackageName(), 3);
            IBroadcastQueueExt iBroadcastQueueExt = this.mBroadcastQueueExt;
            ActivityManagerService activityManagerService = this.mService;
            BroadcastHandler broadcastHandler = this.mHandler;
            String str2 = TAG_BROADCAST;
            if (iBroadcastQueueExt.optimizationBroadcast(activityManagerService, broadcastRecord, processRecord, broadcastHandler, BroadcastQueue.TAG, str2)) {
                processRecord2 = null;
                processReceiverRecord = processReceiverRecord2;
                str = str2;
                i = 3;
            } else {
                if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                    str = str2;
                    Slog.v(str, "impl AOSP Process cur broadcast " + broadcastRecord + " DELIVERED for app " + processRecord + " r.ordered " + broadcastRecord.ordered);
                } else {
                    str = str2;
                }
                processRecord2 = null;
                processReceiverRecord = processReceiverRecord2;
                try {
                    thread.scheduleReceiver(prepareReceiverIntent(broadcastRecord.intent, broadcastRecord.curFilteredExtras), broadcastRecord.curReceiver, (CompatibilityInfo) null, broadcastRecord.resultCode, broadcastRecord.resultData, broadcastRecord.resultExtras, broadcastRecord.ordered, false, broadcastRecord.userId, broadcastRecord.shareIdentity ? broadcastRecord.callingUid : -1, processRecord.mState.getReportedProcState(), broadcastRecord.shareIdentity ? broadcastRecord.callerPackage : null);
                    i = 3;
                } catch (Throwable th2) {
                    th = th2;
                    z = false;
                    if (!z) {
                    }
                    throw th;
                }
            }
            broadcastRecord.oplusState = i;
            if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                Slog.v(str, "Process cur broadcast " + broadcastRecord + " DELIVERED for app " + processRecord);
            }
            z = true;
        } catch (Throwable th3) {
            th = th3;
            processRecord2 = null;
            processReceiverRecord = processReceiverRecord2;
        }
        try {
            this.mBroadcastQueueExt.hookAfterScheduleCurReceiver(broadcastRecord, processRecord);
            if (processRecord.isKilled()) {
                throw new RemoteException("app gets killed during broadcasting");
            }
        } catch (Throwable th4) {
            th = th4;
            if (!z) {
                if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                    Slog.v(TAG_BROADCAST, "Process cur broadcast " + broadcastRecord + ": NOT STARTED!");
                }
                broadcastRecord.curApp = processRecord2;
                broadcastRecord.curAppLastProcessState = -1;
                processReceiverRecord.removeCurReceiver(broadcastRecord);
            }
            throw th;
        }
    }

    public void updateUidReadyForBootCompletedBroadcastLocked(int i) {
        this.mDispatcher.updateUidReadyForBootCompletedBroadcastLocked(i);
        scheduleBroadcastsLocked();
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean onApplicationAttachedLocked(ProcessRecord processRecord) throws BroadcastDeliveryFailedException {
        updateUidReadyForBootCompletedBroadcastLocked(processRecord.uid);
        BroadcastRecord broadcastRecord = this.mPendingBroadcast;
        if (broadcastRecord == null || broadcastRecord.curApp != processRecord) {
            return false;
        }
        return sendPendingBroadcastsLocked(processRecord);
    }

    @Override // com.android.server.am.BroadcastQueue
    public void onApplicationTimeoutLocked(ProcessRecord processRecord) {
        skipCurrentOrPendingReceiverLocked(processRecord);
    }

    @Override // com.android.server.am.BroadcastQueue
    public void onApplicationProblemLocked(ProcessRecord processRecord) {
        skipCurrentOrPendingReceiverLocked(processRecord);
    }

    @Override // com.android.server.am.BroadcastQueue
    public void onApplicationCleanupLocked(ProcessRecord processRecord) {
        skipCurrentOrPendingReceiverLocked(processRecord);
    }

    public boolean sendPendingBroadcastsLocked(ProcessRecord processRecord) throws BroadcastDeliveryFailedException {
        BroadcastRecord broadcastRecord = this.mPendingBroadcast;
        if (broadcastRecord == null || broadcastRecord.curApp.getPid() <= 0 || broadcastRecord.curApp.getPid() != processRecord.getPid()) {
            return false;
        }
        if (broadcastRecord.curApp != processRecord) {
            Slog.e(BroadcastQueue.TAG, "App mismatch when sending pending broadcast to " + processRecord.processName + ", intended target is " + broadcastRecord.curApp.processName);
            return false;
        }
        try {
            this.mPendingBroadcast = null;
            if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                Slog.v(BroadcastQueue.TAG, "mQueueName " + this.mQueueName + " sendPendingBroadcastsLocked mPendingBroadcast = " + this.mPendingBroadcast + " app = " + processRecord);
            }
            broadcastRecord.mIsReceiverAppRunning = false;
            processCurBroadcastLocked(broadcastRecord, processRecord);
            return true;
        } catch (Exception e) {
            Slog.w(BroadcastQueue.TAG, "Exception in new application when starting receiver " + broadcastRecord.curComponent.flattenToShortString(), e);
            logBroadcastReceiverDiscardLocked(broadcastRecord);
            finishReceiverLocked(broadcastRecord, broadcastRecord.resultCode, broadcastRecord.resultData, broadcastRecord.resultExtras, broadcastRecord.resultAbort, false);
            scheduleBroadcastsLocked();
            broadcastRecord.state = 0;
            broadcastRecord.oplusState = 0;
            throw new BroadcastDeliveryFailedException(e);
        }
    }

    public boolean skipCurrentOrPendingReceiverLocked(ProcessRecord processRecord) {
        BroadcastRecord broadcastRecord;
        this.mBroadcastQueueExt.hookSkipCurrentReceiver(this, processRecord);
        BroadcastRecord activeBroadcastLocked = this.mDispatcher.getActiveBroadcastLocked();
        if (activeBroadcastLocked == null || activeBroadcastLocked.curApp != processRecord) {
            activeBroadcastLocked = null;
        }
        if (activeBroadcastLocked == null && (broadcastRecord = this.mPendingBroadcast) != null && broadcastRecord.curApp == processRecord) {
            if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                Slog.v(TAG_BROADCAST, "[" + this.mQueueName + "] skip & discard pending app " + activeBroadcastLocked);
            }
            activeBroadcastLocked = this.mPendingBroadcast;
        }
        if (activeBroadcastLocked == null) {
            return false;
        }
        skipReceiverLocked(activeBroadcastLocked);
        return true;
    }

    private void skipReceiverLocked(BroadcastRecord broadcastRecord) {
        logBroadcastReceiverDiscardLocked(broadcastRecord);
        finishReceiverLocked(broadcastRecord, broadcastRecord.resultCode, broadcastRecord.resultData, broadcastRecord.resultExtras, broadcastRecord.resultAbort, false);
        scheduleBroadcastsLocked();
    }

    public void scheduleBroadcastsLocked() {
        if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            Slog.v(TAG_BROADCAST, "Schedule broadcasts [" + this.mQueueName + "]: current=" + this.mBroadcastsScheduled);
        }
        if (this.mBroadcastsScheduled) {
            if (System.currentTimeMillis() - this.mBroadcastQueueExt.getLastTimeForDispatchMsg() <= ActivityManagerService.BROADCAST_BG_TIMEOUT * 2) {
                return;
            }
            Slog.d(BroadcastQueue.TAG, "Schedule broadcasts:Bad suitation happend, maybe we lost the BROADCAST_INTENT_MSG msg!");
            this.mHandler.dump(this.mBroadcastQueueExt.getLogPrinterForMsgDump(), "msgQueue");
            this.mBroadcastsScheduled = false;
        }
        BroadcastHandler broadcastHandler = this.mHandler;
        broadcastHandler.sendMessage(broadcastHandler.obtainMessage(200, this));
        this.mBroadcastsScheduled = true;
        this.mBroadcastQueueExt.setLastTimeForDispatchMsg(System.currentTimeMillis());
    }

    public BroadcastRecord getMatchingOrderedReceiver(ProcessRecord processRecord) {
        BroadcastRecord activeBroadcastLocked = this.mDispatcher.getActiveBroadcastLocked();
        if (activeBroadcastLocked == null) {
            Slog.w(TAG_BROADCAST, "getMatchingOrderedReceiver [" + this.mQueueName + "] no active broadcast");
            return null;
        }
        if (activeBroadcastLocked.curApp == processRecord) {
            return activeBroadcastLocked;
        }
        Slog.w(TAG_BROADCAST, "getMatchingOrderedReceiver [" + this.mQueueName + "] active broadcast " + activeBroadcastLocked.curApp + " doesn't match " + processRecord);
        return null;
    }

    private int nextSplitTokenLocked() {
        int i = this.mNextToken + 1;
        int i2 = i > 0 ? i : 1;
        this.mNextToken = i2;
        return i2;
    }

    private void postActivityStartTokenRemoval(final ProcessRecord processRecord, final BroadcastRecord broadcastRecord) {
        String intern = (processRecord.toShortString() + broadcastRecord.toString()).intern();
        this.mHandler.removeCallbacksAndMessages(intern);
        this.mHandler.postAtTime(new Runnable() { // from class: com.android.server.am.BroadcastQueueImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BroadcastQueueImpl.this.lambda$postActivityStartTokenRemoval$0(processRecord, broadcastRecord);
            }
        }, intern, broadcastRecord.receiverTime + this.mConstants.ALLOW_BG_ACTIVITY_START_TIMEOUT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postActivityStartTokenRemoval$0(ProcessRecord processRecord, BroadcastRecord broadcastRecord) {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                processRecord.removeBackgroundStartPrivileges(broadcastRecord);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean finishReceiverLocked(ProcessRecord processRecord, int i, String str, Bundle bundle, boolean z, boolean z2) {
        BroadcastRecord matchingOrderedReceiver = getMatchingOrderedReceiver(processRecord);
        if (matchingOrderedReceiver != null) {
            return finishReceiverLocked(matchingOrderedReceiver, i, str, bundle, z, z2);
        }
        return false;
    }

    public boolean finishReceiverLocked(BroadcastRecord broadcastRecord, int i, String str, Bundle bundle, boolean z, boolean z2) {
        ActivityInfo activityInfo;
        ProcessRecord processRecord;
        BroadcastHandler broadcastHandler = this.mHandler;
        if (broadcastHandler != null && broadcastHandler.hasMessages(202, broadcastRecord)) {
            this.mHandler.removeMessages(202, broadcastRecord);
            if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                Slog.i(BroadcastQueue.TAG, "finishReceiverLocked : mQueueName = " + this.mQueueName + ", r= " + broadcastRecord.toString() + ", receiver=" + broadcastRecord.curReceiver);
                StringBuilder sb = new StringBuilder();
                sb.append("finishReceiverLocked : mQueueName = ");
                sb.append(this.mQueueName);
                sb.append(", mPendingBroadcast = ");
                sb.append(this.mPendingBroadcast);
                Slog.i(BroadcastQueue.TAG, sb.toString());
            }
        }
        if (ActivityManagerDebugConfig.DEBUG_BROADCAST_LIGHT && broadcastRecord != null) {
            Slog.i(BroadcastQueue.TAG, "finishReceiverLocked : mQueueName = " + this.mQueueName + ",r = " + broadcastRecord.toString() + ", r.state = " + broadcastRecord.state + " " + Debug.getCallers(4));
        }
        int i2 = broadcastRecord.state;
        ActivityInfo activityInfo2 = broadcastRecord.curReceiver;
        long uptimeMillis = SystemClock.uptimeMillis();
        long j = uptimeMillis - broadcastRecord.receiverTime;
        broadcastRecord.state = 0;
        broadcastRecord.oplusState = 0;
        int i3 = broadcastRecord.nextReceiver - 1;
        int i4 = broadcastRecord.mWasReceiverAppStopped ? 2 : 1;
        if (i3 >= 0 && i3 < broadcastRecord.receivers.size() && broadcastRecord.curApp != null) {
            Object obj = broadcastRecord.receivers.get(i3);
            int i5 = broadcastRecord.curApp.uid;
            int i6 = broadcastRecord.callingUid;
            if (i6 == -1) {
                i6 = 1000;
            }
            int i7 = i6;
            String action = broadcastRecord.intent.getAction();
            int i8 = obj instanceof BroadcastFilter ? 1 : 2;
            int i9 = broadcastRecord.mIsReceiverAppRunning ? 1 : 3;
            long j2 = broadcastRecord.dispatchTime;
            long j3 = j2 - broadcastRecord.enqueueTime;
            long j4 = broadcastRecord.receiverTime;
            FrameworkStatsLog.write(FrameworkStatsLog.BROADCAST_DELIVERY_EVENT_REPORTED, i5, i7, action, i8, i9, j3, j4 - j2, uptimeMillis - j4, i4, broadcastRecord.curApp.info.packageName, broadcastRecord.callerPackage, broadcastRecord.calculateTypeForLogging(), broadcastRecord.getDeliveryGroupPolicy(), broadcastRecord.intent.getFlags(), BroadcastRecord.getReceiverPriority(obj), broadcastRecord.callerProcState, broadcastRecord.curAppLastProcessState);
        }
        if (i2 == 0) {
            Slog.w(TAG_BROADCAST, "finishReceiver [" + this.mQueueName + "] called but state is IDLE");
        }
        if (broadcastRecord.mBackgroundStartPrivileges.allowsAny() && (processRecord = broadcastRecord.curApp) != null) {
            if (j > this.mConstants.ALLOW_BG_ACTIVITY_START_TIMEOUT) {
                processRecord.removeBackgroundStartPrivileges(broadcastRecord);
            } else {
                postActivityStartTokenRemoval(processRecord, broadcastRecord);
            }
        }
        int i10 = broadcastRecord.nextReceiver;
        if (i10 > 0) {
            broadcastRecord.terminalTime[i10 - 1] = uptimeMillis;
        }
        if (!broadcastRecord.timeoutExempt) {
            ProcessRecord processRecord2 = broadcastRecord.curApp;
            if (processRecord2 != null) {
                long j5 = this.mConstants.SLOW_TIME;
                if (j5 > 0 && j > j5) {
                    if (processRecord2 != null && !UserHandle.isCore(processRecord2.uid)) {
                        if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
                            Slog.i(TAG_BROADCAST, "Broadcast receiver " + (broadcastRecord.nextReceiver - 1) + " was slow: " + activityInfo2 + " br=" + broadcastRecord);
                        }
                        this.mDispatcher.startDeferring(broadcastRecord.curApp.uid);
                    } else if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
                        Slog.i(TAG_BROADCAST, "Core uid " + broadcastRecord.curApp.uid + " receiver was slow but not deferring: " + activityInfo2 + " br=" + broadcastRecord);
                    }
                }
            }
        } else if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
            Slog.i(TAG_BROADCAST, "Finished broadcast " + broadcastRecord.intent.getAction() + " is exempt from deferral policy");
        }
        broadcastRecord.intent.setComponent(null);
        ProcessRecord processRecord3 = broadcastRecord.curApp;
        if (processRecord3 != null && processRecord3.mReceivers.hasCurReceiver(broadcastRecord)) {
            broadcastRecord.curApp.mReceivers.removeCurReceiver(broadcastRecord);
            this.mService.enqueueOomAdjTargetLocked(broadcastRecord.curApp);
        }
        BroadcastFilter broadcastFilter = broadcastRecord.curFilter;
        if (broadcastFilter != null) {
            broadcastFilter.receiverList.curBroadcast = null;
        }
        broadcastRecord.curFilter = null;
        broadcastRecord.curReceiver = null;
        broadcastRecord.curApp = null;
        broadcastRecord.curAppLastProcessState = -1;
        broadcastRecord.curFilteredExtras = null;
        broadcastRecord.mWasReceiverAppStopped = false;
        this.mPendingBroadcast = null;
        broadcastRecord.resultCode = i;
        broadcastRecord.resultData = str;
        broadcastRecord.resultExtras = bundle;
        if (z && (broadcastRecord.intent.getFlags() & AudioFormat.OPUS) == 0) {
            broadcastRecord.resultAbort = z;
        } else {
            broadcastRecord.resultAbort = false;
        }
        if (z2 && broadcastRecord.curComponent != null && broadcastRecord.queue.isDelayBehindServices() && ((BroadcastQueueImpl) broadcastRecord.queue).getActiveBroadcastLocked() == broadcastRecord) {
            if (broadcastRecord.nextReceiver < broadcastRecord.receivers.size()) {
                Object obj2 = broadcastRecord.receivers.get(broadcastRecord.nextReceiver);
                if (obj2 instanceof ActivityInfo) {
                    activityInfo = (ActivityInfo) obj2;
                    if ((activityInfo2 != null || activityInfo == null || activityInfo2.applicationInfo.uid != activityInfo.applicationInfo.uid || !activityInfo2.processName.equals(activityInfo.processName)) && this.mService.mServices.hasBackgroundServicesLocked(broadcastRecord.userId)) {
                        Slog.i(BroadcastQueue.TAG, "Delay finish: " + broadcastRecord.curComponent.flattenToShortString());
                        broadcastRecord.state = 4;
                        return false;
                    }
                }
            }
            activityInfo = null;
            if (activityInfo2 != null) {
            }
            Slog.i(BroadcastQueue.TAG, "Delay finish: " + broadcastRecord.curComponent.flattenToShortString());
            broadcastRecord.state = 4;
            return false;
        }
        broadcastRecord.curComponent = null;
        boolean z3 = i2 == 1 || i2 == 3;
        if (z3) {
            processNextBroadcastLocked(false, true);
        }
        return z3;
    }

    @Override // com.android.server.am.BroadcastQueue
    public void backgroundServicesFinishedLocked(int i) {
        BroadcastRecord activeBroadcastLocked = this.mDispatcher.getActiveBroadcastLocked();
        if (activeBroadcastLocked != null && activeBroadcastLocked.userId == i && activeBroadcastLocked.state == 4) {
            Slog.i(BroadcastQueue.TAG, "Resuming delayed broadcast");
            activeBroadcastLocked.curComponent = null;
            activeBroadcastLocked.state = 0;
            activeBroadcastLocked.oplusState = 0;
            processNextBroadcastLocked(false, false);
        }
    }

    public void performReceiveLocked(BroadcastRecord broadcastRecord, ProcessRecord processRecord, IIntentReceiver iIntentReceiver, Intent intent, int i, String str, Bundle bundle, boolean z, boolean z2, boolean z3, int i2, int i3, int i4, String str2, long j, long j2, int i5, int i6) throws RemoteException {
        int i7;
        boolean z4;
        String str3;
        if (z3) {
            this.mService.mPackageManagerInt.grantImplicitAccess(i2, intent, UserHandle.getAppId(i3), i4, true);
        }
        if (processRecord != null) {
            IApplicationThread thread = processRecord.getThread();
            if (thread != null) {
                try {
                    try {
                        z4 = true;
                        try {
                            thread.scheduleRegisteredReceiver(iIntentReceiver, intent, i, str, bundle, z, z2, !z, i2, processRecord.mState.getReportedProcState(), z3 ? i4 : -1, z3 ? str2 : null);
                        } catch (RemoteException e) {
                            e = e;
                            ActivityManagerService activityManagerService = this.mService;
                            ActivityManagerService.boostPriorityForLockedSection();
                            synchronized (activityManagerService) {
                                try {
                                    Slog.w(BroadcastQueue.TAG, "Failed to schedule " + intent + " to " + iIntentReceiver + " via " + processRecord + ": " + e);
                                    processRecord.killLocked("Can't deliver broadcast", 13, 26, z4);
                                    Slog.w(BroadcastQueue.TAG, "can't deliver broadcast, let's cleanup...");
                                    int pid = processRecord.getPid();
                                    if ((Process.getUidForPid(pid) == processRecord.uid || Process.getUidForPid(pid) == -1) && Process.getThreadGroupLeader(pid) == pid && (str3 = processRecord.processName) != null && !str3.equals("com.android.systemui")) {
                                        this.mService.appDiedLocked(processRecord, "broadcast Optimize");
                                    }
                                } catch (Throwable th) {
                                    ActivityManagerService.resetPriorityAfterLockedSection();
                                    throw th;
                                }
                            }
                            ActivityManagerService.resetPriorityAfterLockedSection();
                            throw e;
                        }
                    } catch (RemoteException e2) {
                        e = e2;
                        z4 = true;
                    }
                } catch (Exception e3) {
                    Slog.w(BroadcastQueue.TAG, "Exception deliver broadcast to" + processRecord, e3);
                }
                i7 = -1;
            } else {
                throw new RemoteException("app.thread must not be null");
            }
        } else {
            i7 = -1;
            iIntentReceiver.performReceive(intent, i, str, bundle, z, z2, i2);
        }
        if (z) {
            return;
        }
        int i8 = i3;
        int i9 = i4;
        if (i8 == i7) {
            i8 = 1000;
        }
        if (i9 == i7) {
            i9 = 1000;
        }
        FrameworkStatsLog.write(FrameworkStatsLog.BROADCAST_DELIVERY_EVENT_REPORTED, i8, i9, intent.getAction(), 1, 1, j, j2, 0L, 1, processRecord != null ? processRecord.info.packageName : null, str2, broadcastRecord.calculateTypeForLogging(), broadcastRecord.getDeliveryGroupPolicy(), broadcastRecord.intent.getFlags(), i5, broadcastRecord.callerProcState, i6);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:48:0x014f A[Catch: RemoteException -> 0x01a1, TRY_LEAVE, TryCatch #0 {RemoteException -> 0x01a1, blocks: (B:46:0x0137, B:48:0x014f), top: B:45:0x0137 }] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0183 A[Catch: RemoteException -> 0x01bd, TryCatch #1 {RemoteException -> 0x01bd, blocks: (B:56:0x017a, B:58:0x0183, B:60:0x018b, B:62:0x018f, B:64:0x01c1, B:94:0x01b9), top: B:37:0x00ee }] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01c1 A[Catch: RemoteException -> 0x01bd, TRY_LEAVE, TryCatch #1 {RemoteException -> 0x01bd, blocks: (B:56:0x017a, B:58:0x0183, B:60:0x018b, B:62:0x018f, B:64:0x01c1, B:94:0x01b9), top: B:37:0x00ee }] */
    /* JADX WARN: Removed duplicated region for block: B:67:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0212  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0229  */
    /* JADX WARN: Removed duplicated region for block: B:81:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01b9 A[Catch: RemoteException -> 0x01bd, TryCatch #1 {RemoteException -> 0x01bd, blocks: (B:56:0x017a, B:58:0x0183, B:60:0x018b, B:62:0x018f, B:64:0x01c1, B:94:0x01b9), top: B:37:0x00ee }] */
    /* JADX WARN: Type inference failed for: r4v1, types: [com.android.server.am.BroadcastRecord, com.android.server.am.BroadcastFilter] */
    /* JADX WARN: Type inference failed for: r4v12 */
    /* JADX WARN: Type inference failed for: r4v2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void deliverToRegisteredReceiverLocked(BroadcastRecord broadcastRecord, BroadcastFilter broadcastFilter, boolean z, int i) {
        boolean z2;
        Bundle bundle;
        String str;
        BroadcastFilter broadcastFilter2;
        BroadcastRecord broadcastRecord2;
        BroadcastQueueImpl broadcastQueueImpl;
        ?? r4;
        BroadcastFilter broadcastFilter3;
        ProcessRecord processRecord;
        ProcessRecord processRecord2;
        BroadcastFilter broadcastFilter4;
        BroadcastFilter broadcastFilter5;
        ProcessRecord processRecord3;
        Bundle extras;
        boolean shouldSkip = this.mSkipPolicy.shouldSkip(broadcastRecord, broadcastFilter);
        boolean z3 = true;
        if (shouldSkip || broadcastRecord.filterExtrasForReceiver == null || (extras = broadcastRecord.intent.getExtras()) == null) {
            z2 = shouldSkip;
            bundle = null;
        } else {
            Bundle apply = broadcastRecord.filterExtrasForReceiver.apply(Integer.valueOf(broadcastFilter.receiverList.uid), extras);
            if (apply == null) {
                if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                    Slog.v(BroadcastQueue.TAG, "Skipping delivery to " + broadcastFilter.receiverList.app + " : receiver is filtered by the package visibility");
                }
                bundle = apply;
                z2 = true;
            } else {
                z2 = shouldSkip;
                bundle = apply;
            }
        }
        boolean z4 = false;
        if (this.mBroadcastQueueExt.hookSkipDeliverReceiver(this, broadcastRecord, null, broadcastFilter, z2, true)) {
            broadcastRecord.delivery[i] = 2;
            return;
        }
        broadcastRecord.delivery[i] = 1;
        if (z) {
            broadcastRecord.curFilter = broadcastFilter;
            ReceiverList receiverList = broadcastFilter.receiverList;
            receiverList.curBroadcast = broadcastRecord;
            broadcastRecord.state = 2;
            ProcessRecord processRecord4 = receiverList.app;
            if (processRecord4 != null) {
                broadcastRecord.curApp = processRecord4;
                broadcastRecord.curAppLastProcessState = processRecord4.mState.getCurProcState();
                broadcastFilter.receiverList.app.mReceivers.addCurReceiver(broadcastRecord);
                this.mService.enqueueOomAdjTargetLocked(broadcastRecord.curApp);
                this.mService.updateOomAdjPendingTargetsLocked(3);
            }
        } else {
            ProcessRecord processRecord5 = broadcastFilter.receiverList.app;
            if (processRecord5 != null) {
                this.mService.mOomAdjuster.unfreezeTemporarily(processRecord5, 3);
            }
        }
        try {
            if (ActivityManagerDebugConfig.DEBUG_BROADCAST_LIGHT) {
                Slog.i(TAG_BROADCAST, "Delivering to " + broadcastFilter + " : " + broadcastRecord);
            }
            ProcessRecord processRecord6 = broadcastFilter.receiverList.app;
            broadcastRecord2 = null;
            broadcastQueueImpl = (processRecord6 == null || !processRecord6.isInFullBackup()) ? null : 1;
            processRecord2 = broadcastFilter.receiverList.app;
            broadcastFilter4 = processRecord2;
        } catch (RemoteException e) {
            e = e;
            str = BroadcastQueue.TAG;
            broadcastFilter2 = broadcastFilter;
            broadcastRecord2 = broadcastRecord;
            broadcastQueueImpl = this;
        }
        try {
            if (processRecord2 != 0) {
                boolean isKilled = processRecord2.isKilled();
                broadcastFilter4 = isKilled;
                if (isKilled != 0) {
                    broadcastFilter5 = isKilled;
                    if (broadcastQueueImpl == null && !z3) {
                        long uptimeMillis = SystemClock.uptimeMillis();
                        broadcastRecord.receiverTime = uptimeMillis;
                        broadcastRecord.scheduledTime[i] = uptimeMillis;
                        maybeAddBackgroundStartPrivileges(broadcastFilter.receiverList.app, broadcastRecord);
                        maybeScheduleTempAllowlistLocked(broadcastFilter.owningUid, broadcastRecord, broadcastRecord.options);
                        maybeReportBroadcastDispatchedEventLocked(broadcastRecord, broadcastFilter.owningUid);
                        ReceiverList receiverList2 = broadcastFilter.receiverList;
                        ProcessRecord processRecord7 = receiverList2.app;
                        IIntentReceiver iIntentReceiver = receiverList2.receiver;
                        Intent prepareReceiverIntent = prepareReceiverIntent(broadcastRecord.intent, bundle);
                        int i2 = broadcastRecord.resultCode;
                        String str2 = broadcastRecord.resultData;
                        Bundle bundle2 = broadcastRecord.resultExtras;
                        boolean z5 = broadcastRecord.ordered;
                        boolean z6 = broadcastRecord.initialSticky;
                        boolean z7 = broadcastRecord.shareIdentity;
                        int i3 = broadcastRecord.userId;
                        int i4 = broadcastFilter.receiverList.uid;
                        try {
                            int i5 = broadcastRecord.callingUid;
                            String str3 = broadcastRecord.callerPackage;
                            try {
                                long j = broadcastRecord.dispatchTime;
                                long j2 = j - broadcastRecord.enqueueTime;
                                long j3 = broadcastRecord.receiverTime - j;
                                int priority = broadcastFilter.getPriority();
                                ProcessRecord processRecord8 = broadcastFilter.receiverList.app;
                                int curProcState = processRecord8 == null ? processRecord8.mState.getCurProcState() : -1;
                                broadcastQueueImpl = this;
                                str = BroadcastQueue.TAG;
                                try {
                                    broadcastQueueImpl.performReceiveLocked(broadcastRecord, processRecord7, iIntentReceiver, prepareReceiverIntent, i2, str2, bundle2, z5, z6, z7, i3, i4, i5, str3, j2, j3, priority, curProcState);
                                    try {
                                        broadcastRecord2 = broadcastRecord;
                                        BroadcastFilter broadcastFilter6 = broadcastFilter;
                                        z4 = false;
                                        z4 = false;
                                        z4 = false;
                                        z4 = false;
                                        broadcastQueueImpl.mBroadcastQueueExt.hookAfterPerformReceive(broadcastRecord2, broadcastFilter6, null);
                                        processRecord3 = broadcastFilter6.receiverList.app;
                                        broadcastFilter5 = broadcastFilter6;
                                        if (processRecord3 != null) {
                                            broadcastFilter5 = broadcastFilter6;
                                            if (broadcastRecord2.mBackgroundStartPrivileges.allowsAny()) {
                                                broadcastFilter5 = broadcastFilter6;
                                                if (!broadcastRecord2.ordered) {
                                                    broadcastQueueImpl.postActivityStartTokenRemoval(broadcastFilter6.receiverList.app, broadcastRecord2);
                                                    broadcastFilter5 = broadcastFilter6;
                                                }
                                            }
                                        }
                                        if (z) {
                                            return;
                                        }
                                        broadcastRecord2.state = 3;
                                        return;
                                    } catch (RemoteException e2) {
                                        e = e2;
                                        broadcastRecord2 = broadcastRecord;
                                        broadcastFilter2 = broadcastFilter;
                                        r4 = 0;
                                        broadcastFilter3 = broadcastFilter2;
                                        String str4 = str;
                                        Slog.w(str4, "Failure sending broadcast " + broadcastRecord2.intent + " to " + broadcastFilter3 + " , " + broadcastFilter3.receiverList.app, e);
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Failure sending broadcast ");
                                        sb.append(broadcastRecord2.intent);
                                        Slog.w(str4, sb.toString(), e);
                                        processRecord = broadcastFilter3.receiverList.app;
                                        if (processRecord != null) {
                                        }
                                        if (z) {
                                        }
                                    }
                                } catch (RemoteException e3) {
                                    e = e3;
                                    broadcastQueueImpl = this;
                                }
                            } catch (RemoteException e4) {
                                e = e4;
                                broadcastQueueImpl = this;
                                broadcastFilter2 = broadcastFilter;
                                broadcastRecord2 = broadcastRecord;
                                str = BroadcastQueue.TAG;
                            }
                        } catch (RemoteException e5) {
                            e = e5;
                            broadcastQueueImpl = this;
                            str = BroadcastQueue.TAG;
                            broadcastFilter2 = broadcastFilter;
                            broadcastRecord2 = broadcastRecord;
                        }
                    }
                    str = BroadcastQueue.TAG;
                    BroadcastFilter broadcastFilter7 = broadcastFilter;
                    broadcastRecord2 = broadcastRecord;
                    broadcastQueueImpl = this;
                    z4 = false;
                    z4 = false;
                    broadcastFilter5 = broadcastFilter7;
                    if (z) {
                        skipReceiverLocked(broadcastRecord);
                        broadcastFilter5 = broadcastFilter7;
                    }
                    if (z) {
                    }
                }
            }
            if (broadcastQueueImpl == null) {
                long uptimeMillis2 = SystemClock.uptimeMillis();
                broadcastRecord.receiverTime = uptimeMillis2;
                broadcastRecord.scheduledTime[i] = uptimeMillis2;
                maybeAddBackgroundStartPrivileges(broadcastFilter.receiverList.app, broadcastRecord);
                maybeScheduleTempAllowlistLocked(broadcastFilter.owningUid, broadcastRecord, broadcastRecord.options);
                maybeReportBroadcastDispatchedEventLocked(broadcastRecord, broadcastFilter.owningUid);
                ReceiverList receiverList22 = broadcastFilter.receiverList;
                ProcessRecord processRecord72 = receiverList22.app;
                IIntentReceiver iIntentReceiver2 = receiverList22.receiver;
                Intent prepareReceiverIntent2 = prepareReceiverIntent(broadcastRecord.intent, bundle);
                int i22 = broadcastRecord.resultCode;
                String str22 = broadcastRecord.resultData;
                Bundle bundle22 = broadcastRecord.resultExtras;
                boolean z52 = broadcastRecord.ordered;
                boolean z62 = broadcastRecord.initialSticky;
                boolean z72 = broadcastRecord.shareIdentity;
                int i32 = broadcastRecord.userId;
                int i42 = broadcastFilter.receiverList.uid;
                int i52 = broadcastRecord.callingUid;
                String str32 = broadcastRecord.callerPackage;
                long j4 = broadcastRecord.dispatchTime;
                long j22 = j4 - broadcastRecord.enqueueTime;
                long j32 = broadcastRecord.receiverTime - j4;
                int priority2 = broadcastFilter.getPriority();
                ProcessRecord processRecord82 = broadcastFilter.receiverList.app;
                int curProcState2 = processRecord82 == null ? processRecord82.mState.getCurProcState() : -1;
                broadcastQueueImpl = this;
                str = BroadcastQueue.TAG;
                broadcastQueueImpl.performReceiveLocked(broadcastRecord, processRecord72, iIntentReceiver2, prepareReceiverIntent2, i22, str22, bundle22, z52, z62, z72, i32, i42, i52, str32, j22, j32, priority2, curProcState2);
                broadcastRecord2 = broadcastRecord;
                BroadcastFilter broadcastFilter62 = broadcastFilter;
                z4 = false;
                z4 = false;
                z4 = false;
                z4 = false;
                broadcastQueueImpl.mBroadcastQueueExt.hookAfterPerformReceive(broadcastRecord2, broadcastFilter62, null);
                processRecord3 = broadcastFilter62.receiverList.app;
                broadcastFilter5 = broadcastFilter62;
                if (processRecord3 != null) {
                }
                if (z) {
                }
            }
            str = BroadcastQueue.TAG;
            BroadcastFilter broadcastFilter72 = broadcastFilter;
            broadcastRecord2 = broadcastRecord;
            broadcastQueueImpl = this;
            z4 = false;
            z4 = false;
            broadcastFilter5 = broadcastFilter72;
            if (z) {
            }
            if (z) {
            }
        } catch (RemoteException e6) {
            e = e6;
            broadcastFilter3 = broadcastFilter5;
            r4 = z4;
            String str42 = str;
            Slog.w(str42, "Failure sending broadcast " + broadcastRecord2.intent + " to " + broadcastFilter3 + " , " + broadcastFilter3.receiverList.app, e);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Failure sending broadcast ");
            sb2.append(broadcastRecord2.intent);
            Slog.w(str42, sb2.toString(), e);
            processRecord = broadcastFilter3.receiverList.app;
            if (processRecord != null) {
                processRecord.removeBackgroundStartPrivileges(broadcastRecord2);
                if (z) {
                    broadcastFilter3.receiverList.app.mReceivers.removeCurReceiver(broadcastRecord2);
                    broadcastQueueImpl.mService.enqueueOomAdjTargetLocked(broadcastRecord2.curApp);
                }
            }
            if (z) {
                return;
            }
            broadcastRecord2.curFilter = r4;
            broadcastFilter3.receiverList.curBroadcast = r4;
            return;
        }
        z3 = false;
        broadcastFilter5 = broadcastFilter4;
    }

    void maybeScheduleTempAllowlistLocked(int i, BroadcastRecord broadcastRecord, BroadcastOptions broadcastOptions) {
        if (broadcastOptions == null || broadcastOptions.getTemporaryAppAllowlistDuration() <= 0) {
            return;
        }
        long temporaryAppAllowlistDuration = broadcastOptions.getTemporaryAppAllowlistDuration();
        int temporaryAppAllowlistType = broadcastOptions.getTemporaryAppAllowlistType();
        int temporaryAppAllowlistReasonCode = broadcastOptions.getTemporaryAppAllowlistReasonCode();
        String temporaryAppAllowlistReason = broadcastOptions.getTemporaryAppAllowlistReason();
        long j = temporaryAppAllowlistDuration > 2147483647L ? 2147483647L : temporaryAppAllowlistDuration;
        StringBuilder sb = new StringBuilder();
        sb.append("broadcast:");
        UserHandle.formatUid(sb, broadcastRecord.callingUid);
        sb.append(":");
        if (broadcastRecord.intent.getAction() != null) {
            sb.append(broadcastRecord.intent.getAction());
        } else if (broadcastRecord.intent.getComponent() != null) {
            broadcastRecord.intent.getComponent().appendShortString(sb);
        } else if (broadcastRecord.intent.getData() != null) {
            sb.append(broadcastRecord.intent.getData());
        }
        sb.append(",reason:");
        sb.append(temporaryAppAllowlistReason);
        if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            Slog.v(BroadcastQueue.TAG, "Broadcast temp allowlist uid=" + i + " duration=" + j + " type=" + temporaryAppAllowlistType + " : " + sb.toString());
        }
        if (temporaryAppAllowlistType != 4) {
            this.mService.tempAllowlistUidLocked(i, j, temporaryAppAllowlistReasonCode, sb.toString(), temporaryAppAllowlistType, broadcastRecord.callingUid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNextBroadcast(boolean z) {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                processNextBroadcastLocked(z, false);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private static Intent prepareReceiverIntent(Intent intent, Bundle bundle) {
        Intent intent2 = new Intent(intent);
        if (bundle != null) {
            intent2.replaceExtras(bundle);
        }
        return intent2;
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:77)
        */
    public void processNextBroadcastLocked(boolean r45, boolean r46) {
        /*
            Method dump skipped, instructions count: 3563
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.BroadcastQueueImpl.processNextBroadcastLocked(boolean, boolean):void");
    }

    private String getTargetPackage(BroadcastRecord broadcastRecord) {
        Intent intent = broadcastRecord.intent;
        if (intent == null) {
            return null;
        }
        if (intent.getPackage() != null) {
            return broadcastRecord.intent.getPackage();
        }
        if (broadcastRecord.intent.getComponent() != null) {
            return broadcastRecord.intent.getComponent().getPackageName();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:13:0x002e  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void logBootCompletedBroadcastCompletionLatencyIfPossible(BroadcastRecord broadcastRecord) {
        int i;
        int i2;
        List<Object> list = broadcastRecord.receivers;
        int size = list != null ? list.size() : 0;
        if (broadcastRecord.nextReceiver < size) {
            return;
        }
        String action = broadcastRecord.intent.getAction();
        if ("android.intent.action.LOCKED_BOOT_COMPLETED".equals(action)) {
            i2 = 1;
        } else {
            if (!"android.intent.action.BOOT_COMPLETED".equals(action)) {
                i = 0;
                if (i == 0) {
                    int i3 = (int) (broadcastRecord.dispatchTime - broadcastRecord.enqueueTime);
                    int uptimeMillis = (int) (SystemClock.uptimeMillis() - broadcastRecord.enqueueTime);
                    int i4 = (int) (broadcastRecord.dispatchRealTime - broadcastRecord.enqueueRealTime);
                    int elapsedRealtime = (int) (SystemClock.elapsedRealtime() - broadcastRecord.enqueueRealTime);
                    UserManagerInternal userManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
                    UserInfo userInfo = userManagerInternal != null ? userManagerInternal.getUserInfo(broadcastRecord.userId) : null;
                    int userTypeForStatsd = userInfo != null ? UserJourneyLogger.getUserTypeForStatsd(userInfo.userType) : 0;
                    String str = TAG_BROADCAST;
                    StringBuilder sb = new StringBuilder();
                    sb.append("BOOT_COMPLETED_BROADCAST_COMPLETION_LATENCY_REPORTED action:");
                    sb.append(action);
                    sb.append(" dispatchLatency:");
                    sb.append(i3);
                    sb.append(" completeLatency:");
                    sb.append(uptimeMillis);
                    sb.append(" dispatchRealLatency:");
                    sb.append(i4);
                    sb.append(" completeRealLatency:");
                    sb.append(elapsedRealtime);
                    sb.append(" receiversSize:");
                    sb.append(size);
                    sb.append(" userId:");
                    sb.append(broadcastRecord.userId);
                    sb.append(" userType:");
                    sb.append(userInfo != null ? userInfo.userType : null);
                    Slog.i(str, sb.toString());
                    FrameworkStatsLog.write(FrameworkStatsLog.BOOT_COMPLETED_BROADCAST_COMPLETION_LATENCY_REPORTED, i, i3, uptimeMillis, i4, elapsedRealtime, broadcastRecord.userId, userTypeForStatsd);
                    return;
                }
                return;
            }
            i2 = 2;
        }
        i = i2;
        if (i == 0) {
        }
    }

    private void maybeReportBroadcastDispatchedEventLocked(BroadcastRecord broadcastRecord, int i) {
        String targetPackage;
        BroadcastOptions broadcastOptions = broadcastRecord.options;
        if (broadcastOptions == null || broadcastOptions.getIdForResponseEvent() <= 0 || (targetPackage = getTargetPackage(broadcastRecord)) == null) {
            return;
        }
        this.mService.mUsageStatsService.reportBroadcastDispatched(broadcastRecord.callingUid, targetPackage, UserHandle.of(broadcastRecord.userId), broadcastRecord.options.getIdForResponseEvent(), SystemClock.elapsedRealtime(), this.mService.getUidStateLocked(i));
    }

    private void maybeAddBackgroundStartPrivileges(ProcessRecord processRecord, BroadcastRecord broadcastRecord) {
        if (broadcastRecord == null || processRecord == null || !broadcastRecord.mBackgroundStartPrivileges.allowsAny()) {
            return;
        }
        this.mHandler.removeCallbacksAndMessages((processRecord.toShortString() + broadcastRecord.toString()).intern());
        processRecord.addOrUpdateBackgroundStartPrivileges(broadcastRecord, broadcastRecord.mBackgroundStartPrivileges);
    }

    final void setBroadcastTimeoutLocked(long j) {
        if (this.mPendingBroadcastTimeoutMessage) {
            return;
        }
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(201, this), j);
        this.mPendingBroadcastTimeoutMessage = true;
    }

    final void cancelBroadcastTimeoutLocked() {
        if (this.mPendingBroadcastTimeoutMessage) {
            this.mHandler.removeMessages(201, this);
            this.mPendingBroadcastTimeoutMessage = false;
        }
    }

    final void broadcastTimeoutLocked(boolean z) {
        Object obj;
        final ProcessRecord processRecord;
        boolean z2 = false;
        if (z) {
            this.mPendingBroadcastTimeoutMessage = false;
        }
        if (this.mDispatcher.isEmpty() || this.mDispatcher.getActiveBroadcastLocked() == null) {
            return;
        }
        Trace.traceBegin(64L, "broadcastTimeoutLocked()");
        try {
            long uptimeMillis = SystemClock.uptimeMillis();
            final BroadcastRecord activeBroadcastLocked = this.mDispatcher.getActiveBroadcastLocked();
            if (z) {
                if (!this.mService.mProcessesReady) {
                    return;
                }
                if (activeBroadcastLocked.timeoutExempt) {
                    if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                        Slog.i(TAG_BROADCAST, "Broadcast timeout but it's exempt: " + activeBroadcastLocked.intent.getAction());
                    }
                    return;
                }
                long j = activeBroadcastLocked.receiverTime + this.mConstants.TIMEOUT;
                if (j > uptimeMillis) {
                    if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                        Slog.v(TAG_BROADCAST, "Premature timeout [" + this.mQueueName + "] @ " + uptimeMillis + ": resetting BROADCAST_TIMEOUT_MSG for " + j);
                    }
                    setBroadcastTimeoutLocked(j);
                    return;
                }
            }
            if (activeBroadcastLocked.state == 4) {
                StringBuilder sb = new StringBuilder();
                sb.append("Waited long enough for: ");
                ComponentName componentName = activeBroadcastLocked.curComponent;
                sb.append(componentName != null ? componentName.flattenToShortString() : "(null)");
                Slog.i(BroadcastQueue.TAG, sb.toString());
                activeBroadcastLocked.curComponent = null;
                activeBroadcastLocked.state = 0;
                activeBroadcastLocked.oplusState = 0;
                processNextBroadcastLocked(false, false);
                return;
            }
            ProcessRecord processRecord2 = activeBroadcastLocked.curApp;
            if (processRecord2 != null && processRecord2.isDebugging()) {
                z2 = true;
            }
            long j2 = uptimeMillis - activeBroadcastLocked.receiverTime;
            Slog.w(BroadcastQueue.TAG, "Timeout of broadcast " + activeBroadcastLocked + " - curFilter=" + activeBroadcastLocked.curFilter + " curReceiver=" + activeBroadcastLocked.curReceiver + ", started " + j2 + "ms ago");
            activeBroadcastLocked.receiverTime = uptimeMillis;
            if (!z2) {
                activeBroadcastLocked.anrCount++;
            }
            int i = activeBroadcastLocked.nextReceiver;
            if (i > 0) {
                obj = activeBroadcastLocked.receivers.get(i - 1);
                activeBroadcastLocked.delivery[activeBroadcastLocked.nextReceiver - 1] = 3;
            } else {
                obj = activeBroadcastLocked.curReceiver;
            }
            Slog.w(BroadcastQueue.TAG, "Receiver during timeout of " + activeBroadcastLocked + " : " + obj + " r.state " + activeBroadcastLocked.state);
            if (obj != null && (obj instanceof ResolveInfo)) {
                Slog.w(BroadcastQueue.TAG, "Receiver during timeout of " + activeBroadcastLocked + " : " + obj + " r.state " + activeBroadcastLocked.state + " r.oplusState " + activeBroadcastLocked.oplusState);
            }
            logBroadcastReceiverDiscardLocked(activeBroadcastLocked);
            TimeoutRecord forBroadcastReceiver = TimeoutRecord.forBroadcastReceiver(activeBroadcastLocked.intent, j2);
            if (obj == null || !(obj instanceof BroadcastFilter)) {
                processRecord = activeBroadcastLocked.curApp;
            } else {
                BroadcastFilter broadcastFilter = (BroadcastFilter) obj;
                int i2 = broadcastFilter.receiverList.pid;
                if (i2 == 0 || i2 == ActivityManagerService.MY_PID) {
                    processRecord = null;
                } else {
                    forBroadcastReceiver.mLatencyTracker.waitingOnPidLockStarted();
                    synchronized (this.mService.mPidsSelfLocked) {
                        forBroadcastReceiver.mLatencyTracker.waitingOnPidLockEnded();
                        processRecord = this.mService.mPidsSelfLocked.get(broadcastFilter.receiverList.pid);
                    }
                }
            }
            new Thread(new Runnable() { // from class: com.android.server.am.BroadcastQueueImpl.1
                @Override // java.lang.Runnable
                public void run() {
                    ProcessRecord processRecord3;
                    if (!activeBroadcastLocked.ordered || (processRecord3 = processRecord) == null || processRecord3.getThread() == null) {
                        return;
                    }
                    try {
                        Intent intent = activeBroadcastLocked.intent;
                        processRecord.getThread().getBroadcastState(intent != null ? intent.getFlags() : 0);
                        Slog.w(BroadcastQueue.TAG, "Timeout receiver in proc " + processRecord + " broadcast " + activeBroadcastLocked);
                    } catch (Exception e) {
                        Slog.v(BroadcastQueue.TAG, "Exception " + e + " record " + activeBroadcastLocked);
                    }
                }
            }).start();
            if (this.mPendingBroadcast == activeBroadcastLocked) {
                this.mPendingBroadcast = null;
            }
            finishReceiverLocked(activeBroadcastLocked, activeBroadcastLocked.resultCode, activeBroadcastLocked.resultData, activeBroadcastLocked.resultExtras, activeBroadcastLocked.resultAbort, false);
            scheduleBroadcastsLocked();
            if (!z2 && processRecord != null) {
                this.mService.appNotResponding(processRecord, forBroadcastReceiver);
            }
        } finally {
            Trace.traceEnd(64L);
        }
    }

    private final void addBroadcastToHistoryLocked(BroadcastRecord broadcastRecord) {
        if (broadcastRecord.callingUid < 0) {
            return;
        }
        broadcastRecord.finishTime = SystemClock.uptimeMillis();
        if (Trace.isTagEnabled(64L)) {
            Trace.asyncTraceEnd(64L, createBroadcastTraceTitle(broadcastRecord, 1), System.identityHashCode(broadcastRecord));
        }
        this.mService.notifyBroadcastFinishedLocked(broadcastRecord);
        this.mHistory.addBroadcastToHistoryLocked(broadcastRecord);
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean cleanupDisabledPackageReceiversLocked(String str, Set<String> set, int i) {
        boolean z = false;
        for (int size = this.mParallelBroadcasts.size() - 1; size >= 0; size--) {
            z |= this.mParallelBroadcasts.get(size).cleanupDisabledPackageReceiversLocked(str, set, i, true);
        }
        return this.mDispatcher.cleanupDisabledPackageReceiversLocked(str, set, i, true) | z;
    }

    final void logBroadcastReceiverDiscardLocked(BroadcastRecord broadcastRecord) {
        int i = broadcastRecord.nextReceiver - 1;
        if (i >= 0 && i < broadcastRecord.receivers.size()) {
            Object obj = broadcastRecord.receivers.get(i);
            if (obj instanceof BroadcastFilter) {
                BroadcastFilter broadcastFilter = (BroadcastFilter) obj;
                EventLog.writeEvent(EventLogTags.AM_BROADCAST_DISCARD_FILTER, Integer.valueOf(broadcastFilter.owningUserId), Integer.valueOf(System.identityHashCode(broadcastRecord)), broadcastRecord.intent.getAction(), Integer.valueOf(i), Integer.valueOf(System.identityHashCode(broadcastFilter)));
                return;
            } else {
                ResolveInfo resolveInfo = (ResolveInfo) obj;
                EventLog.writeEvent(EventLogTags.AM_BROADCAST_DISCARD_APP, Integer.valueOf(UserHandle.getUserId(resolveInfo.activityInfo.applicationInfo.uid)), Integer.valueOf(System.identityHashCode(broadcastRecord)), broadcastRecord.intent.getAction(), Integer.valueOf(i), resolveInfo.toString());
                return;
            }
        }
        if (i < 0) {
            Slog.w(BroadcastQueue.TAG, "Discarding broadcast before first receiver is invoked: " + broadcastRecord);
        }
        EventLog.writeEvent(EventLogTags.AM_BROADCAST_DISCARD_APP, -1, Integer.valueOf(System.identityHashCode(broadcastRecord)), broadcastRecord.intent.getAction(), Integer.valueOf(broadcastRecord.nextReceiver), "NONE");
    }

    private String createBroadcastTraceTitle(BroadcastRecord broadcastRecord, int i) {
        Object[] objArr = new Object[4];
        objArr[0] = i == 0 ? "in queue" : "dispatched";
        String str = broadcastRecord.callerPackage;
        if (str == null) {
            str = "";
        }
        objArr[1] = str;
        ProcessRecord processRecord = broadcastRecord.callerApp;
        objArr[2] = processRecord == null ? "process unknown" : processRecord.toShortString();
        Intent intent = broadcastRecord.intent;
        objArr[3] = intent != null ? intent.getAction() : "";
        return TextUtils.formatSimple("Broadcast %s from %s (%s) %s", objArr);
    }

    @Override // com.android.server.am.BroadcastQueue
    /* renamed from: isIdleLocked, reason: merged with bridge method [inline-methods] */
    public boolean lambda$waitForIdle$1() {
        return this.mParallelBroadcasts.isEmpty() && this.mDispatcher.isIdle() && this.mPendingBroadcast == null;
    }

    @Override // com.android.server.am.BroadcastQueue
    /* renamed from: isBeyondBarrierLocked, reason: merged with bridge method [inline-methods] */
    public boolean lambda$waitForBarrier$2(long j) {
        if (lambda$waitForIdle$1()) {
            return true;
        }
        for (int i = 0; i < this.mParallelBroadcasts.size(); i++) {
            if (this.mParallelBroadcasts.get(i).enqueueTime <= j) {
                return false;
            }
        }
        BroadcastRecord pendingBroadcastLocked = getPendingBroadcastLocked();
        if (pendingBroadcastLocked == null || pendingBroadcastLocked.enqueueTime > j) {
            return this.mDispatcher.isBeyondBarrier(j);
        }
        return false;
    }

    @Override // com.android.server.am.BroadcastQueue
    /* renamed from: isDispatchedLocked, reason: merged with bridge method [inline-methods] */
    public boolean lambda$waitForDispatched$3(Intent intent) {
        if (lambda$waitForIdle$1()) {
            return true;
        }
        for (int i = 0; i < this.mParallelBroadcasts.size(); i++) {
            if (intent.filterEquals(this.mParallelBroadcasts.get(i).intent)) {
                return false;
            }
        }
        BroadcastRecord pendingBroadcastLocked = getPendingBroadcastLocked();
        if (pendingBroadcastLocked == null || !intent.filterEquals(pendingBroadcastLocked.intent)) {
            return this.mDispatcher.isDispatched(intent);
        }
        return false;
    }

    @Override // com.android.server.am.BroadcastQueue
    public void waitForIdle(PrintWriter printWriter) {
        waitFor(new BooleanSupplier() { // from class: com.android.server.am.BroadcastQueueImpl$$ExternalSyntheticLambda2
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$waitForIdle$1;
                lambda$waitForIdle$1 = BroadcastQueueImpl.this.lambda$waitForIdle$1();
                return lambda$waitForIdle$1;
            }
        }, printWriter, "idle");
    }

    @Override // com.android.server.am.BroadcastQueue
    public void waitForBarrier(PrintWriter printWriter) {
        final long uptimeMillis = SystemClock.uptimeMillis();
        waitFor(new BooleanSupplier() { // from class: com.android.server.am.BroadcastQueueImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$waitForBarrier$2;
                lambda$waitForBarrier$2 = BroadcastQueueImpl.this.lambda$waitForBarrier$2(uptimeMillis);
                return lambda$waitForBarrier$2;
            }
        }, printWriter, "barrier");
    }

    @Override // com.android.server.am.BroadcastQueue
    public void waitForDispatched(final Intent intent, PrintWriter printWriter) {
        waitFor(new BooleanSupplier() { // from class: com.android.server.am.BroadcastQueueImpl$$ExternalSyntheticLambda3
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                boolean lambda$waitForDispatched$3;
                lambda$waitForDispatched$3 = BroadcastQueueImpl.this.lambda$waitForDispatched$3(intent);
                return lambda$waitForDispatched$3;
            }
        }, printWriter, "dispatch");
    }

    private void waitFor(BooleanSupplier booleanSupplier, PrintWriter printWriter, String str) {
        long j = 0;
        while (true) {
            ActivityManagerService activityManagerService = this.mService;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    if (booleanSupplier.getAsBoolean()) {
                        break;
                    }
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
            long uptimeMillis = SystemClock.uptimeMillis();
            if (uptimeMillis >= 1000 + j) {
                String str2 = "Queue [" + this.mQueueName + "] waiting for " + str + " condition; state is " + describeStateLocked();
                Slog.v(BroadcastQueue.TAG, str2);
                if (printWriter != null) {
                    printWriter.println(str2);
                    printWriter.flush();
                }
                j = uptimeMillis;
            }
            cancelDeferrals();
            SystemClock.sleep(100L);
        }
        String str3 = "Queue [" + this.mQueueName + "] reached " + str + " condition";
        Slog.v(BroadcastQueue.TAG, str3);
        if (printWriter != null) {
            printWriter.println(str3);
            printWriter.flush();
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    public void cancelDeferrals() {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mDispatcher.cancelDeferralsLocked();
                scheduleBroadcastsLocked();
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    @Override // com.android.server.am.BroadcastQueue
    public String describeStateLocked() {
        return this.mParallelBroadcasts.size() + " parallel; " + this.mDispatcher.describeStateLocked();
    }

    @Override // com.android.server.am.BroadcastQueue
    @NeverCompile
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1138166333441L, this.mQueueName);
        for (int size = this.mParallelBroadcasts.size() - 1; size >= 0; size--) {
            this.mParallelBroadcasts.get(size).dumpDebug(protoOutputStream, 2246267895810L);
        }
        this.mDispatcher.dumpDebug(protoOutputStream, 2246267895811L);
        BroadcastRecord broadcastRecord = this.mPendingBroadcast;
        if (broadcastRecord != null) {
            broadcastRecord.dumpDebug(protoOutputStream, 1146756268036L);
        }
        this.mHistory.dumpDebug(protoOutputStream);
        protoOutputStream.end(start);
    }

    @Override // com.android.server.am.BroadcastQueue
    @NeverCompile
    public boolean dumpLocked(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, int i, boolean z, boolean z2, boolean z3, String str, boolean z4) {
        boolean z5;
        BroadcastRecord broadcastRecord;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        if (this.mParallelBroadcasts.isEmpty() && this.mDispatcher.isEmpty() && this.mPendingBroadcast == null) {
            z5 = z4;
        } else {
            boolean z6 = false;
            boolean z7 = z4;
            for (int size = this.mParallelBroadcasts.size() - 1; size >= 0; size--) {
                BroadcastRecord broadcastRecord2 = this.mParallelBroadcasts.get(size);
                if (str == null || str.equals(broadcastRecord2.callerPackage)) {
                    if (!z6) {
                        if (z7) {
                            printWriter.println();
                        }
                        printWriter.println("  Active broadcasts [" + this.mQueueName + "]:");
                        z7 = true;
                        z6 = true;
                    }
                    printWriter.println("  Active Broadcast " + this.mQueueName + " #" + size + ":");
                    broadcastRecord2.dump(printWriter, "    ", simpleDateFormat);
                }
            }
            this.mDispatcher.dumpLocked(printWriter, str, this.mQueueName, simpleDateFormat);
            if (str == null || ((broadcastRecord = this.mPendingBroadcast) != null && str.equals(broadcastRecord.callerPackage))) {
                printWriter.println();
                printWriter.println("  Pending broadcast [" + this.mQueueName + "]:");
                BroadcastRecord broadcastRecord3 = this.mPendingBroadcast;
                if (broadcastRecord3 != null) {
                    broadcastRecord3.dump(printWriter, "    ", simpleDateFormat);
                } else {
                    printWriter.println("    (null)");
                }
                z5 = true;
            } else {
                z5 = z7;
            }
        }
        if (z) {
            this.mConstants.dump(new IndentingPrintWriter(printWriter));
        }
        return z2 ? this.mHistory.dumpLocked(printWriter, str, this.mQueueName, simpleDateFormat, z3, z5) : z5;
    }

    public IBroadcastQueueWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class BroadcastQueueWrapper implements IBroadcastQueueWrapper {
        private BroadcastQueueWrapper() {
        }

        @Override // com.android.server.am.IBroadcastQueueWrapper
        public IBroadcastQueueExt getExtImpl() {
            return BroadcastQueueImpl.this.mBroadcastQueueExt;
        }

        @Override // com.android.server.am.IBroadcastQueueWrapper
        public void processNextBroadcastLocked(boolean z, boolean z2) {
            BroadcastQueueImpl.this.processNextBroadcastLocked(z, z2);
        }

        @Override // com.android.server.am.IBroadcastQueueWrapper
        public void processNextBroadcast(boolean z) {
            BroadcastQueueImpl.this.processNextBroadcast(z);
        }

        @Override // com.android.server.am.IBroadcastQueueWrapper
        public String getQueueName() {
            return BroadcastQueueImpl.this.mQueueName;
        }
    }
}
