package com.android.server.am;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.compat.CompatChanges;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.net.NetworkPolicyManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManagerInternal;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.CompositeRWLock;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.LocalServices;
import com.android.server.ServiceThread;
import com.android.server.am.ActivityManagerService;
import com.android.server.wm.ActivityServiceConnectionsHolder;
import com.android.server.wm.ActivityTaskManagerDebugConfig;
import com.android.server.wm.WindowProcessController;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class OomAdjuster {
    static final long CAMERA_MICROPHONE_CAPABILITY_CHANGE_ID = 136219221;
    static final long PROCESS_CAPABILITY_CHANGE_ID = 136274596;
    static final String TAG = "OomAdjuster";
    static final long USE_SHORT_FGS_USAGE_INTERACTION_TIME = 183972877;
    public static IOomAdjusterExt mOomAdjusterExt = (IOomAdjusterExt) ExtLoader.type(IOomAdjusterExt.class).create();
    private final int MSG_CHANGE_PROCESS_GROUP_BY_OCL;

    @CompositeRWLock({"mService", "mProcLock"})
    ActiveUids mActiveUids;
    int mAdjSeq;
    CacheOomRanker mCacheOomRanker;
    CachedAppOptimizer mCachedAppOptimizer;
    ActivityManagerConstants mConstants;
    private double mLastFreeSwapPercent;
    PowerManagerInternal mLocalPowerManager;
    int mNewNumAServiceProcs;
    int mNewNumServiceProcs;
    private long mNextNoKillDebugMessageTime;
    int mNumCachedHiddenProcs;
    int mNumNonCachedProcs;
    int mNumServiceProcs;
    private final int mNumSlots;

    @GuardedBy({"mService"})
    private boolean mOomAdjUpdateOngoing;
    private IOomAdjusterWrapper mOomAdjWrapper;

    @GuardedBy({"mService"})
    private boolean mPendingFullOomAdjUpdate;
    private final ArraySet<ProcessRecord> mPendingProcessSet;
    private final ActivityManagerGlobalLock mProcLock;
    private final Handler mProcessGroupHandler;
    private final ProcessList mProcessList;
    private final ArraySet<ProcessRecord> mProcessesInCycle;
    private final ActivityManagerService mService;
    public IOomAdjusterSocExt mSocExt;
    private final ArrayList<UidRecord> mTmpBecameIdle;
    private final ComputeOomAdjWindowCallback mTmpComputeOomAdjWindowCallback;
    final long[] mTmpLong;
    private final ArrayList<ProcessRecord> mTmpProcessList;
    private final ArraySet<ProcessRecord> mTmpProcessSet;
    private final ArrayDeque<ProcessRecord> mTmpQueue;
    private final int[] mTmpSchedGroup;
    private final ActiveUids mTmpUidRecords;

    public static final int oomAdjReasonToProto(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 13;
            case 14:
                return 14;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
            case 20:
                return 20;
            case 21:
                return 21;
            case 22:
                return 22;
            default:
                return -1;
        }
    }

    public static final String oomAdjReasonToString(int i) {
        switch (i) {
            case 0:
                return "updateOomAdj_meh";
            case 1:
                return "updateOomAdj_activityChange";
            case 2:
                return "updateOomAdj_finishReceiver";
            case 3:
                return "updateOomAdj_startReceiver";
            case 4:
                return "updateOomAdj_bindService";
            case 5:
                return "updateOomAdj_unbindService";
            case 6:
                return "updateOomAdj_startService";
            case 7:
                return "updateOomAdj_getProvider";
            case 8:
                return "updateOomAdj_removeProvider";
            case 9:
                return "updateOomAdj_uiVisibility";
            case 10:
                return "updateOomAdj_allowlistChange";
            case 11:
                return "updateOomAdj_processBegin";
            case 12:
                return "updateOomAdj_processEnd";
            case 13:
                return "updateOomAdj_shortFgs";
            case 14:
                return "updateOomAdj_systemInit";
            case 15:
                return "updateOomAdj_backup";
            case 16:
                return "updateOomAdj_shell";
            case 17:
                return "updateOomAdj_removeTask";
            case 18:
                return "updateOomAdj_uidIdle";
            case 19:
                return "updateOomAdj_stopService";
            case 20:
                return "updateOomAdj_executingService";
            case 21:
                return "updateOomAdj_restrictionChange";
            case 22:
                return "updateOomAdj_componentDisabled";
            default:
                return "_unknown";
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    public boolean isChangeEnabled(int i, ApplicationInfo applicationInfo, boolean z) {
        PlatformCompatCache.getInstance();
        return PlatformCompatCache.isChangeEnabled(i, applicationInfo, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OomAdjuster(ActivityManagerService activityManagerService, ProcessList processList, ActiveUids activeUids) {
        this(activityManagerService, processList, activeUids, createAdjusterThread());
    }

    private static ServiceThread createAdjusterThread() {
        ServiceThread serviceThread = new ServiceThread(TAG, -10, false);
        serviceThread.start();
        return serviceThread;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OomAdjuster(ActivityManagerService activityManagerService, ProcessList processList, ActiveUids activeUids, ServiceThread serviceThread) {
        this.mTmpLong = new long[3];
        this.mAdjSeq = 0;
        this.mNumServiceProcs = 0;
        this.mNewNumAServiceProcs = 0;
        this.mNewNumServiceProcs = 0;
        this.mNumNonCachedProcs = 0;
        this.mNumCachedHiddenProcs = 0;
        this.mTmpSchedGroup = new int[1];
        this.mTmpProcessList = new ArrayList<>();
        this.mTmpBecameIdle = new ArrayList<>();
        this.mTmpProcessSet = new ArraySet<>();
        this.mPendingProcessSet = new ArraySet<>();
        this.mProcessesInCycle = new ArraySet<>();
        this.mOomAdjUpdateOngoing = false;
        this.mPendingFullOomAdjUpdate = false;
        this.mSocExt = (IOomAdjusterSocExt) ExtLoader.type(IOomAdjusterSocExt.class).base(this).create();
        this.MSG_CHANGE_PROCESS_GROUP_BY_OCL = 1001;
        this.mLastFreeSwapPercent = 1.0d;
        this.mTmpComputeOomAdjWindowCallback = new ComputeOomAdjWindowCallback();
        this.mOomAdjWrapper = new OomAdjusterWrapper();
        this.mService = activityManagerService;
        this.mProcessList = processList;
        this.mProcLock = activityManagerService.mProcLock;
        this.mActiveUids = activeUids;
        this.mLocalPowerManager = (PowerManagerInternal) LocalServices.getService(PowerManagerInternal.class);
        this.mConstants = activityManagerService.mConstants;
        this.mCachedAppOptimizer = new CachedAppOptimizer(activityManagerService);
        this.mCacheOomRanker = new CacheOomRanker(activityManagerService);
        this.mSocExt.initPerfConfig();
        this.mProcessGroupHandler = new Handler(serviceThread.getLooper(), new Handler.Callback() { // from class: com.android.server.am.OomAdjuster$$ExternalSyntheticLambda1
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(Message message) {
                boolean lambda$new$0;
                lambda$new$0 = OomAdjuster.lambda$new$0(message);
                return lambda$new$0;
            }
        });
        this.mTmpUidRecords = new ActiveUids(activityManagerService, false);
        this.mTmpQueue = new ArrayDeque<>(this.mConstants.CUR_MAX_CACHED_PROCESSES << 1);
        this.mNumSlots = 10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$new$0(Message message) {
        if (mOomAdjusterExt.isOclGrpRequestMsgAndSetGroup(message)) {
            return true;
        }
        int i = message.arg1;
        int i2 = message.arg2;
        ProcessRecord processRecord = (ProcessRecord) message.obj;
        if (i == ActivityManagerService.MY_PID) {
            return true;
        }
        if (Trace.isTagEnabled(64L)) {
            Trace.traceBegin(64L, "setProcessGroup " + processRecord.processName + " to " + i2);
        }
        try {
            try {
                Process.setProcessGroup(i, i2);
            } catch (Exception e) {
                if (ActivityManagerDebugConfig.DEBUG_ALL) {
                    Slog.w(TAG, "Failed setting process group of " + i + " to " + i2, e);
                }
            }
            mOomAdjusterExt.notifyProcGrpChange(processRecord, i2);
            return true;
        } finally {
            Trace.traceEnd(64L);
        }
    }

    public void requestProcessGroupChange(int i, int i2, int i3, int i4, String str) {
        if (i2 >= 0) {
            Message obtainMessage = this.mProcessGroupHandler.obtainMessage(1001, i3, i4);
            Bundle bundle = new Bundle();
            bundle.putInt("uid", i);
            bundle.putInt("pid", i2);
            bundle.putString("reason", str);
            obtainMessage.setData(bundle);
            this.mProcessGroupHandler.sendMessage(obtainMessage);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initSettings() {
        this.mCachedAppOptimizer.init();
        this.mCacheOomRanker.init(ActivityThread.currentApplication().getMainExecutor());
        if (this.mService.mConstants.KEEP_WARMING_SERVICES.size() > 0) {
            this.mService.mContext.registerReceiverForAllUsers(new BroadcastReceiver() { // from class: com.android.server.am.OomAdjuster.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    ActivityManagerService activityManagerService = OomAdjuster.this.mService;
                    ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService) {
                        try {
                            OomAdjuster.this.handleUserSwitchedLocked();
                        } catch (Throwable th) {
                            ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    ActivityManagerService.resetPriorityAfterLockedSection();
                }
            }, new IntentFilter("android.intent.action.USER_SWITCHED"), null, this.mService.mHandler);
        }
    }

    @GuardedBy({"mService"})
    @VisibleForTesting
    void handleUserSwitchedLocked() {
        this.mProcessList.forEachLruProcessesLOSP(false, new Consumer() { // from class: com.android.server.am.OomAdjuster$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                OomAdjuster.this.updateKeepWarmIfNecessaryForProcessLocked((ProcessRecord) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mService"})
    public void updateKeepWarmIfNecessaryForProcessLocked(ProcessRecord processRecord) {
        boolean z;
        ArraySet<ComponentName> arraySet = this.mService.mConstants.KEEP_WARMING_SERVICES;
        PackageList pkgList = processRecord.getPkgList();
        int size = arraySet.size() - 1;
        while (true) {
            if (size < 0) {
                z = false;
                break;
            } else {
                if (pkgList.containsKey(arraySet.valueAt(size).getPackageName())) {
                    z = true;
                    break;
                }
                size--;
            }
        }
        if (z) {
            ProcessServiceRecord processServiceRecord = processRecord.mServices;
            for (int numberOfRunningServices = processServiceRecord.numberOfRunningServices() - 1; numberOfRunningServices >= 0; numberOfRunningServices--) {
                processServiceRecord.getRunningServiceAt(numberOfRunningServices).updateKeepWarmLocked();
            }
        }
    }

    @GuardedBy({"mService", "mProcLock"})
    private boolean performUpdateOomAdjLSP(ProcessRecord processRecord, int i, ProcessRecord processRecord2, long j, int i2) {
        if (processRecord.getThread() == null) {
            return false;
        }
        processRecord.mState.resetCachedInfo();
        processRecord.mState.setCurBoundByNonBgRestrictedApp(false);
        UidRecord uidRecord = processRecord.getUidRecord();
        if (uidRecord != null) {
            if (ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                Slog.i(ActivityManagerService.TAG_UID_OBSERVERS, "Starting update of " + uidRecord);
            }
            uidRecord.reset();
        }
        this.mPendingProcessSet.remove(processRecord);
        mOomAdjusterExt.updateRecentLockApps();
        this.mProcessesInCycle.clear();
        computeOomAdjLSP(processRecord, i, processRecord2, false, j, false, true);
        if (!this.mProcessesInCycle.isEmpty()) {
            for (int size = this.mProcessesInCycle.size() - 1; size >= 0; size--) {
                this.mProcessesInCycle.valueAt(size).mState.setCompletedAdjSeq(this.mAdjSeq - 1);
            }
            return true;
        }
        if (uidRecord != null) {
            uidRecord.forEachProcess(new Consumer() { // from class: com.android.server.am.OomAdjuster$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    OomAdjuster.this.updateAppUidRecIfNecessaryLSP((ProcessRecord) obj);
                }
            });
            if (uidRecord.getCurProcState() != 20 && (uidRecord.getSetProcState() != uidRecord.getCurProcState() || uidRecord.getSetCapability() != uidRecord.getCurCapability() || uidRecord.isSetAllowListed() != uidRecord.isCurAllowListed())) {
                ActiveUids activeUids = this.mTmpUidRecords;
                activeUids.clear();
                activeUids.put(uidRecord.getUid(), uidRecord);
                updateUidsLSP(activeUids, SystemClock.elapsedRealtime());
            }
        }
        return applyOomAdjLSP(processRecord, false, j, SystemClock.elapsedRealtime(), i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void updateOomAdjLocked(int i) {
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                updateOomAdjLSP(i);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    @GuardedBy({"mService", "mProcLock"})
    private void updateOomAdjLSP(int i) {
        if (checkAndEnqueueOomAdjTargetLocked(null)) {
            return;
        }
        try {
            mOomAdjusterExt.onOomAdjUpdateLSP(null, i, true, null);
            this.mOomAdjUpdateOngoing = true;
            performUpdateOomAdjLSP(i);
        } finally {
            this.mOomAdjUpdateOngoing = false;
            updateOomAdjPendingTargetsLocked(i);
        }
    }

    @GuardedBy({"mService", "mProcLock"})
    private void performUpdateOomAdjLSP(int i) {
        ProcessRecord topApp = this.mService.getTopApp();
        this.mPendingProcessSet.clear();
        AppProfiler appProfiler = this.mService.mAppProfiler;
        appProfiler.mHasHomeProcess = false;
        appProfiler.mHasPreviousProcess = false;
        updateOomAdjInnerLSP(i, topApp, null, null, true, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean updateOomAdjLocked(ProcessRecord processRecord, int i) {
        boolean updateOomAdjLSP;
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                updateOomAdjLSP = updateOomAdjLSP(processRecord, i);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
        return updateOomAdjLSP;
    }

    @GuardedBy({"mService", "mProcLock"})
    private boolean updateOomAdjLSP(ProcessRecord processRecord, int i) {
        if (processRecord == null || !this.mConstants.OOMADJ_UPDATE_QUICK) {
            if (processRecord != null) {
                mOomAdjusterExt.setFullOomAdjUpdateInfo(processRecord.uid, processRecord.processName, "quick update");
            }
            updateOomAdjLSP(i);
            return true;
        }
        if (checkAndEnqueueOomAdjTargetLocked(processRecord)) {
            return true;
        }
        try {
            this.mOomAdjUpdateOngoing = true;
            mOomAdjusterExt.onOomAdjUpdateLSP(processRecord, i, false, null);
            return performUpdateOomAdjLSP(processRecord, i);
        } finally {
            this.mOomAdjUpdateOngoing = false;
            updateOomAdjPendingTargetsLocked(i);
        }
    }

    @GuardedBy({"mService", "mProcLock"})
    private boolean performUpdateOomAdjLSP(ProcessRecord processRecord, int i) {
        ProcessRecord topApp = this.mService.getTopApp();
        Trace.traceBegin(64L, oomAdjReasonToString(i));
        this.mService.mOomAdjProfiler.oomAdjStarted();
        this.mAdjSeq++;
        ProcessStateRecord processStateRecord = processRecord.mState;
        boolean isCached = processStateRecord.isCached();
        int curRawAdj = processStateRecord.getCurRawAdj();
        int i2 = curRawAdj >= 900 ? curRawAdj : 1001;
        boolean isProcStateBackground = ActivityManager.isProcStateBackground(processStateRecord.getSetProcState());
        int setCapability = processStateRecord.getSetCapability();
        processStateRecord.setContainsCycle(false);
        processStateRecord.setProcStateChanged(false);
        processStateRecord.resetCachedInfo();
        processStateRecord.setCurBoundByNonBgRestrictedApp(false);
        this.mPendingProcessSet.remove(processRecord);
        processRecord.mOptRecord.setLastOomAdjChangeReason(i);
        boolean performUpdateOomAdjLSP = performUpdateOomAdjLSP(processRecord, i2, topApp, SystemClock.uptimeMillis(), i);
        if (!performUpdateOomAdjLSP || (isCached == processStateRecord.isCached() && curRawAdj != -10000 && this.mProcessesInCycle.isEmpty() && setCapability == processStateRecord.getCurCapability() && isProcStateBackground == ActivityManager.isProcStateBackground(processStateRecord.getSetProcState()))) {
            this.mProcessesInCycle.clear();
            if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                Slog.i(ActivityManagerService.TAG_OOM_ADJ, "No oomadj changes for " + processRecord);
            }
            this.mService.mOomAdjProfiler.oomAdjEnded();
            Trace.traceEnd(64L);
            return performUpdateOomAdjLSP;
        }
        ArrayList<ProcessRecord> arrayList = this.mTmpProcessList;
        ActiveUids activeUids = this.mTmpUidRecords;
        this.mPendingProcessSet.add(processRecord);
        for (int size = this.mProcessesInCycle.size() - 1; size >= 0; size--) {
            this.mPendingProcessSet.add(this.mProcessesInCycle.valueAt(size));
        }
        this.mProcessesInCycle.clear();
        boolean collectReachableProcessesLocked = collectReachableProcessesLocked(this.mPendingProcessSet, arrayList, activeUids);
        this.mPendingProcessSet.clear();
        if (!collectReachableProcessesLocked) {
            processStateRecord.setReachable(false);
            arrayList.remove(processRecord);
        }
        if (arrayList.size() > 0) {
            this.mAdjSeq--;
            updateOomAdjInnerLSP(i, topApp, arrayList, activeUids, collectReachableProcessesLocked, false);
        } else if (processStateRecord.getCurRawAdj() == 1001) {
            arrayList.add(processRecord);
            assignCachedAdjIfNecessary(arrayList);
            applyOomAdjLSP(processRecord, false, SystemClock.uptimeMillis(), SystemClock.elapsedRealtime(), i);
        }
        this.mTmpProcessList.clear();
        this.mService.mOomAdjProfiler.oomAdjEnded();
        Trace.traceEnd(64L);
        return true;
    }

    @GuardedBy({"mService"})
    private boolean collectReachableProcessesLocked(ArraySet<ProcessRecord> arraySet, ArrayList<ProcessRecord> arrayList, ActiveUids activeUids) {
        ArrayDeque<ProcessRecord> arrayDeque = this.mTmpQueue;
        arrayDeque.clear();
        arrayList.clear();
        int size = arraySet.size();
        for (int i = 0; i < size; i++) {
            ProcessRecord valueAt = arraySet.valueAt(i);
            valueAt.mState.setReachable(true);
            arrayDeque.offer(valueAt);
        }
        activeUids.clear();
        boolean z = false;
        for (ProcessRecord poll = arrayDeque.poll(); poll != null; poll = arrayDeque.poll()) {
            arrayList.add(poll);
            UidRecord uidRecord = poll.getUidRecord();
            if (uidRecord != null) {
                activeUids.put(uidRecord.getUid(), uidRecord);
            }
            ProcessServiceRecord processServiceRecord = poll.mServices;
            for (int numberOfConnections = processServiceRecord.numberOfConnections() - 1; numberOfConnections >= 0; numberOfConnections--) {
                ConnectionRecord connectionAt = processServiceRecord.getConnectionAt(numberOfConnections);
                ProcessRecord processRecord = connectionAt.hasFlag(2) ? connectionAt.binding.service.isolationHostProc : connectionAt.binding.service.app;
                if (processRecord != null && processRecord != poll && (processRecord.mState.getMaxAdj() < -900 || processRecord.mState.getMaxAdj() >= 0)) {
                    z |= processRecord.mState.isReachable();
                    if (!processRecord.mState.isReachable() && (!connectionAt.hasFlag(32) || !connectionAt.notHasFlag(134217856))) {
                        arrayDeque.offer(processRecord);
                        processRecord.mState.setReachable(true);
                    }
                }
            }
            ProcessProviderRecord processProviderRecord = poll.mProviders;
            for (int numberOfProviderConnections = processProviderRecord.numberOfProviderConnections() - 1; numberOfProviderConnections >= 0; numberOfProviderConnections--) {
                ProcessRecord processRecord2 = processProviderRecord.getProviderConnectionAt(numberOfProviderConnections).provider.proc;
                if (processRecord2 != null && processRecord2 != poll && (processRecord2.mState.getMaxAdj() < -900 || processRecord2.mState.getMaxAdj() >= 0)) {
                    z |= processRecord2.mState.isReachable();
                    if (!processRecord2.mState.isReachable()) {
                        arrayDeque.offer(processRecord2);
                        processRecord2.mState.setReachable(true);
                    }
                }
            }
            List<ProcessRecord> sdkSandboxProcessesForAppLocked = this.mProcessList.getSdkSandboxProcessesForAppLocked(poll.uid);
            for (int size2 = (sdkSandboxProcessesForAppLocked != null ? sdkSandboxProcessesForAppLocked.size() : 0) - 1; size2 >= 0; size2--) {
                ProcessRecord processRecord3 = sdkSandboxProcessesForAppLocked.get(size2);
                z |= processRecord3.mState.isReachable();
                if (!processRecord3.mState.isReachable()) {
                    arrayDeque.offer(processRecord3);
                    processRecord3.mState.setReachable(true);
                }
            }
            if (poll.isSdkSandbox) {
                for (int numberOfRunningServices = processServiceRecord.numberOfRunningServices() - 1; numberOfRunningServices >= 0; numberOfRunningServices--) {
                    ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections = processServiceRecord.getRunningServiceAt(numberOfRunningServices).getConnections();
                    for (int size3 = connections.size() - 1; size3 >= 0; size3--) {
                        ArrayList<ConnectionRecord> valueAt2 = connections.valueAt(size3);
                        for (int size4 = valueAt2.size() - 1; size4 >= 0; size4--) {
                            ProcessRecord processRecord4 = valueAt2.get(size4).binding.attributedClient;
                            if (processRecord4 != null && processRecord4 != poll && ((processRecord4.mState.getMaxAdj() < -900 || processRecord4.mState.getMaxAdj() >= 0) && !processRecord4.mState.isReachable())) {
                                arrayDeque.offer(processRecord4);
                                processRecord4.mState.setReachable(true);
                            }
                        }
                    }
                }
            }
        }
        int size5 = arrayList.size();
        if (size5 > 0) {
            int i2 = 0;
            for (int i3 = size5 - 1; i2 < i3; i3--) {
                ProcessRecord processRecord5 = arrayList.get(i2);
                arrayList.set(i2, arrayList.get(i3));
                arrayList.set(i3, processRecord5);
                i2++;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void enqueueOomAdjTargetLocked(ProcessRecord processRecord) {
        if (processRecord == null || processRecord.mState.getMaxAdj() <= 0) {
            return;
        }
        this.mPendingProcessSet.add(processRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void removeOomAdjTargetLocked(ProcessRecord processRecord, boolean z) {
        if (processRecord != null) {
            this.mPendingProcessSet.remove(processRecord);
            if (z) {
                PlatformCompatCache.getInstance().invalidate(processRecord.info);
            }
        }
    }

    @GuardedBy({"mService"})
    private boolean checkAndEnqueueOomAdjTargetLocked(ProcessRecord processRecord) {
        if (!this.mOomAdjUpdateOngoing) {
            return false;
        }
        if (processRecord != null) {
            this.mPendingProcessSet.add(processRecord);
        } else {
            this.mPendingFullOomAdjUpdate = true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void updateOomAdjPendingTargetsLocked(int i) {
        if (this.mPendingFullOomAdjUpdate) {
            this.mPendingFullOomAdjUpdate = false;
            this.mPendingProcessSet.clear();
            updateOomAdjLocked(i);
        } else {
            if (this.mPendingProcessSet.isEmpty() || this.mOomAdjUpdateOngoing) {
                return;
            }
            try {
                this.mOomAdjUpdateOngoing = true;
                performUpdateOomAdjPendingTargetsLocked(i);
            } finally {
                this.mOomAdjUpdateOngoing = false;
                updateOomAdjPendingTargetsLocked(i);
            }
        }
    }

    @GuardedBy({"mService"})
    private void performUpdateOomAdjPendingTargetsLocked(int i) {
        ProcessRecord topApp = this.mService.getTopApp();
        Trace.traceBegin(64L, oomAdjReasonToString(i));
        this.mService.mOomAdjProfiler.oomAdjStarted();
        ArrayList<ProcessRecord> arrayList = this.mTmpProcessList;
        ActiveUids activeUids = this.mTmpUidRecords;
        collectReachableProcessesLocked(this.mPendingProcessSet, arrayList, activeUids);
        this.mPendingProcessSet.clear();
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                mOomAdjusterExt.onPendingOomAdjUpdateLSP(arrayList, i, arrayList == null, null);
                updateOomAdjInnerLSP(i, topApp, arrayList, activeUids, true, false);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
        arrayList.clear();
        this.mService.mOomAdjProfiler.oomAdjEnded();
        Trace.traceEnd(64L);
    }

    @GuardedBy({"mService", "mProcLock"})
    private void updateOomAdjInnerLSP(int i, ProcessRecord processRecord, ArrayList<ProcessRecord> arrayList, ActiveUids activeUids, boolean z, boolean z2) {
        ActiveUids activeUids2;
        ArrayList<ProcessRecord> arrayList2;
        int i2;
        int i3;
        int i4;
        ActiveUids activeUids3;
        ArrayList<ProcessRecord> arrayList3;
        boolean z3;
        if (z2) {
            Trace.traceBegin(64L, oomAdjReasonToString(i));
            this.mService.mOomAdjProfiler.oomAdjStarted();
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = uptimeMillis - this.mConstants.mMaxEmptyTimeMillis;
        boolean z4 = false;
        boolean z5 = arrayList == null;
        ArrayList<ProcessRecord> lruProcessesLOSP = z5 ? this.mProcessList.getLruProcessesLOSP() : arrayList;
        int size = lruProcessesLOSP.size();
        if (activeUids == null) {
            int size2 = this.mActiveUids.size();
            ActiveUids activeUids4 = this.mTmpUidRecords;
            activeUids4.clear();
            for (int i5 = 0; i5 < size2; i5++) {
                UidRecord valueAt = this.mActiveUids.valueAt(i5);
                activeUids4.put(valueAt.getUid(), valueAt);
            }
            activeUids2 = activeUids4;
        } else {
            activeUids2 = activeUids;
        }
        for (int size3 = activeUids2.size() - 1; size3 >= 0; size3--) {
            UidRecord valueAt2 = activeUids2.valueAt(size3);
            if (ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                Slog.i(ActivityManagerService.TAG_UID_OBSERVERS, "Starting update of " + valueAt2);
            }
            valueAt2.reset();
        }
        this.mAdjSeq++;
        if (z5) {
            this.mNewNumServiceProcs = 0;
            this.mNewNumAServiceProcs = 0;
        }
        boolean z6 = z5 || z;
        mOomAdjusterExt.updateRecentLockApps();
        int i6 = size - 1;
        for (int i7 = i6; i7 >= 0; i7--) {
            ProcessStateRecord processStateRecord = lruProcessesLOSP.get(i7).mState;
            processStateRecord.setReachable(false);
            if (processStateRecord.getAdjSeq() != this.mAdjSeq) {
                processStateRecord.setContainsCycle(false);
                processStateRecord.setCurRawProcState(19);
                processStateRecord.setCurRawAdj(1001);
                processStateRecord.setSetCapability(0);
                processStateRecord.resetCachedInfo();
                processStateRecord.setCurBoundByNonBgRestrictedApp(false);
            }
        }
        this.mProcessesInCycle.clear();
        int i8 = i6;
        int i9 = 0;
        while (i8 >= 0) {
            ProcessRecord processRecord2 = lruProcessesLOSP.get(i8);
            ProcessStateRecord processStateRecord2 = processRecord2.mState;
            if (processRecord2.isKilledByAm() || processRecord2.getThread() == null) {
                i3 = i8;
                i4 = size;
                activeUids3 = activeUids2;
                arrayList3 = lruProcessesLOSP;
                z3 = z4;
            } else {
                processStateRecord2.setProcStateChanged(z4);
                processRecord2.mOptRecord.setLastOomAdjChangeReason(i);
                i3 = i8;
                i4 = size;
                arrayList3 = lruProcessesLOSP;
                activeUids3 = activeUids2;
                z3 = z4;
                computeOomAdjLSP(processRecord2, 1001, processRecord, z5, uptimeMillis, false, z6);
                int i10 = i9 | (processStateRecord2.containsCycle() ? 1 : 0);
                processStateRecord2.setCompletedAdjSeq(this.mAdjSeq);
                i9 = i10;
            }
            i8 = i3 - 1;
            activeUids2 = activeUids3;
            size = i4;
            z4 = z3;
            lruProcessesLOSP = arrayList3;
        }
        int i11 = size;
        ActiveUids activeUids5 = activeUids2;
        ArrayList<ProcessRecord> arrayList4 = lruProcessesLOSP;
        boolean z7 = z4;
        if (this.mCacheOomRanker.useOomReranking()) {
            this.mCacheOomRanker.reRankLruCachedAppsLSP(this.mProcessList.getLruProcessesLSP(), this.mProcessList.getLruProcessServiceStartLOSP());
        }
        if (z6) {
            int i12 = z7 ? 1 : 0;
            while (i9 != 0 && i12 < 10) {
                int i13 = i12 + 1;
                int i14 = z7 ? 1 : 0;
                while (i14 < i11) {
                    ArrayList<ProcessRecord> arrayList5 = arrayList4;
                    ProcessRecord processRecord3 = arrayList5.get(i14);
                    ProcessStateRecord processStateRecord3 = processRecord3.mState;
                    if (!processRecord3.isKilledByAm() && processRecord3.getThread() != null && processStateRecord3.containsCycle()) {
                        processStateRecord3.decAdjSeq();
                        processStateRecord3.decCompletedAdjSeq();
                    }
                    i14++;
                    arrayList4 = arrayList5;
                }
                ArrayList<ProcessRecord> arrayList6 = arrayList4;
                int i15 = z7 ? 1 : 0;
                i9 = i15;
                while (i15 < i11) {
                    ProcessRecord processRecord4 = arrayList6.get(i15);
                    ProcessStateRecord processStateRecord4 = processRecord4.mState;
                    if (processRecord4.isKilledByAm() || processRecord4.getThread() == null || !processStateRecord4.containsCycle()) {
                        arrayList2 = arrayList6;
                        i2 = i15;
                    } else {
                        arrayList2 = arrayList6;
                        i2 = i15;
                        if (computeOomAdjLSP(processRecord4, 1001, processRecord, true, uptimeMillis, true, true)) {
                            i9 = 1;
                        }
                    }
                    i15 = i2 + 1;
                    arrayList6 = arrayList2;
                }
                arrayList4 = arrayList6;
                i12 = i13;
            }
        }
        this.mProcessesInCycle.clear();
        assignCachedAdjIfNecessary(this.mProcessList.getLruProcessesLOSP());
        this.mNumNonCachedProcs = z7 ? 1 : 0;
        this.mNumCachedHiddenProcs = z7 ? 1 : 0;
        boolean updateAndTrimProcessLSP = updateAndTrimProcessLSP(uptimeMillis, elapsedRealtime, j, activeUids5, i);
        this.mNumServiceProcs = this.mNewNumServiceProcs;
        ActivityManagerService activityManagerService = this.mService;
        if (activityManagerService.mAlwaysFinishActivities) {
            activityManagerService.mAtmInternal.scheduleDestroyAllActivities("always-finish");
        }
        if (updateAndTrimProcessLSP) {
            ActivityManagerService activityManagerService2 = this.mService;
            activityManagerService2.mAppProfiler.requestPssAllProcsLPr(uptimeMillis, z7, activityManagerService2.mProcessStats.isMemFactorLowered());
        }
        updateUidsLSP(activeUids5, elapsedRealtime);
        synchronized (this.mService.mProcessStats.mLock) {
            long uptimeMillis2 = SystemClock.uptimeMillis();
            if (this.mService.mProcessStats.shouldWriteNowLocked(uptimeMillis2)) {
                ActivityManagerService activityManagerService3 = this.mService;
                activityManagerService3.mHandler.post(new ActivityManagerService.ProcStatsRunnable(activityManagerService3, activityManagerService3.mProcessStats));
            }
            this.mService.mProcessStats.updateTrackingAssociationsLocked(this.mAdjSeq, uptimeMillis2);
        }
        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
            long uptimeMillis3 = SystemClock.uptimeMillis() - uptimeMillis;
            Slog.d(ActivityManagerService.TAG_OOM_ADJ, "Did OOM ADJ in " + uptimeMillis3 + "ms");
        }
        if (z2) {
            this.mService.mOomAdjProfiler.oomAdjEnded();
            Trace.traceEnd(64L);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:92:0x012a  */
    @GuardedBy({"mService", "mProcLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void assignCachedAdjIfNecessary(ArrayList<ProcessRecord> arrayList) {
        int i;
        int i2;
        int i3;
        boolean z;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        ArrayList<ProcessRecord> arrayList2 = arrayList;
        int size = arrayList.size();
        ActivityManagerConstants activityManagerConstants = this.mConstants;
        int i11 = 1001;
        if (activityManagerConstants.USE_TIERED_CACHED_ADJ) {
            long uptimeMillis = SystemClock.uptimeMillis();
            for (int i12 = size - 1; i12 >= 0; i12--) {
                ProcessRecord processRecord = arrayList2.get(i12);
                ProcessStateRecord processStateRecord = processRecord.mState;
                ProcessCachedOptimizerRecord processCachedOptimizerRecord = processRecord.mOptRecord;
                if (!processRecord.isKilledByAm() && processRecord.getThread() != null && processStateRecord.getCurAdj() >= 1001) {
                    ProcessServiceRecord processServiceRecord = processRecord.mServices;
                    if (processCachedOptimizerRecord == null || !processCachedOptimizerRecord.isFreezeExempt()) {
                        i10 = (processStateRecord.getSetAdj() < 900 || processStateRecord.getLastStateTime() + this.mConstants.TIERED_CACHED_ADJ_DECAY_TIME >= uptimeMillis) ? 910 : ProcessList.CACHED_APP_LMK_FIRST_ADJ;
                    } else {
                        i10 = 900;
                    }
                    processStateRecord.setCurRawAdj(i10);
                    processStateRecord.setCurAdj(processServiceRecord.modifyRawOomAdj(i10));
                }
            }
            return;
        }
        int i13 = activityManagerConstants.CUR_MAX_CACHED_PROCESSES - activityManagerConstants.CUR_MAX_EMPTY_PROCESSES;
        int i14 = size - this.mNumNonCachedProcs;
        int i15 = this.mNumCachedHiddenProcs;
        int i16 = i14 - i15;
        if (i16 <= i13) {
            i13 = i16;
        }
        int i17 = i15 > 0 ? (i15 + this.mNumSlots) - 1 : 1;
        int i18 = this.mNumSlots;
        int i19 = i17 / i18;
        if (i19 < 1) {
            i19 = 1;
        }
        int i20 = ((i13 + i18) - 1) / i18;
        if (i20 < 1) {
            i20 = 1;
        }
        int i21 = -1;
        int i22 = 915;
        int i23 = 0;
        int i24 = 0;
        int i25 = 0;
        int i26 = 0;
        int i27 = 905;
        int i28 = 900;
        int i29 = 910;
        int i30 = size - 1;
        int i31 = -1;
        while (i30 >= 0) {
            ProcessRecord processRecord2 = arrayList2.get(i30);
            ProcessStateRecord processStateRecord2 = processRecord2.mState;
            if (processRecord2.isKilledByAm() || processRecord2.getThread() == null || processStateRecord2.getCurAdj() < i11) {
                i = i21;
                int i32 = i27;
                i2 = i20;
                i3 = i32;
                i31 = i31;
            } else {
                ProcessServiceRecord processServiceRecord2 = processRecord2.mServices;
                int i33 = i20;
                i = i21;
                switch (processStateRecord2.getCurProcState()) {
                    case 16:
                    case 17:
                    case 18:
                        int connectionGroup = processServiceRecord2.getConnectionGroup();
                        i3 = i27;
                        if (connectionGroup != 0) {
                            int connectionImportance = processServiceRecord2.getConnectionImportance();
                            int i34 = processRecord2.uid;
                            if (i23 == i34 && i24 == connectionGroup) {
                                if (connectionImportance > i25) {
                                    if (i28 < i29 && i28 < 999) {
                                        i26++;
                                    }
                                    i25 = connectionImportance;
                                }
                                z = true;
                                if (!z || i28 == i29) {
                                    i4 = i29;
                                    i29 = i28;
                                    i5 = i26;
                                } else {
                                    i31++;
                                    if (i31 >= i19) {
                                        i4 = i29 + 10;
                                        if (i4 > 999) {
                                            i4 = 999;
                                        }
                                        i31 = 0;
                                    } else {
                                        i4 = i29;
                                        i29 = i28;
                                    }
                                    i5 = 0;
                                }
                                int i35 = i29 + i5;
                                processStateRecord2.setCurRawAdj(i35);
                                processStateRecord2.setCurAdj(processServiceRecord2.modifyRawOomAdj(i35));
                                if (ActivityManagerDebugConfig.DEBUG_LRU) {
                                    Slog.d(ActivityManagerService.TAG_LRU, "Assigning activity LRU #" + i30 + " adj: " + processStateRecord2.getCurAdj() + " (curCachedAdj=" + i29 + " curCachedImpAdj=" + i5 + ")");
                                }
                                i26 = i5;
                                i28 = i29;
                                i2 = i33;
                                i29 = i4;
                                break;
                            } else {
                                i24 = connectionGroup;
                                i23 = i34;
                                i25 = connectionImportance;
                            }
                        }
                        z = false;
                        if (z) {
                        }
                        i4 = i29;
                        i29 = i28;
                        i5 = i26;
                        int i352 = i29 + i5;
                        processStateRecord2.setCurRawAdj(i352);
                        processStateRecord2.setCurAdj(processServiceRecord2.modifyRawOomAdj(i352));
                        if (ActivityManagerDebugConfig.DEBUG_LRU) {
                        }
                        i26 = i5;
                        i28 = i29;
                        i2 = i33;
                        i29 = i4;
                        break;
                    default:
                        int i36 = i27;
                        if (i36 != i22) {
                            int i37 = i + 1;
                            i2 = i33;
                            if (i37 >= i2) {
                                int i38 = i22 + 10;
                                i6 = i38 <= 999 ? i38 : 999;
                                i7 = 0;
                            } else {
                                int i39 = i22;
                                i22 = i36;
                                i7 = i37;
                                i6 = i39;
                            }
                        } else {
                            i2 = i33;
                            i6 = i22;
                            i22 = i36;
                            i7 = i;
                        }
                        processStateRecord2.setCurRawAdj(i22);
                        processStateRecord2.setCurAdj(processServiceRecord2.modifyRawOomAdj(i22));
                        if (ActivityManagerDebugConfig.DEBUG_LRU) {
                            String str = ActivityManagerService.TAG_LRU;
                            i8 = i7;
                            StringBuilder sb = new StringBuilder();
                            i9 = i31;
                            sb.append("Assigning empty LRU #");
                            sb.append(i30);
                            sb.append(" adj: ");
                            sb.append(processStateRecord2.getCurAdj());
                            sb.append(" (curEmptyAdj=");
                            sb.append(i22);
                            sb.append(")");
                            Slog.d(str, sb.toString());
                        } else {
                            i8 = i7;
                            i9 = i31;
                        }
                        i3 = i22;
                        i21 = i8;
                        i31 = i9;
                        i22 = i6;
                        continue;
                }
            }
            i21 = i;
            i30--;
            arrayList2 = arrayList;
            i20 = i2;
            i27 = i3;
            i11 = 1001;
        }
    }

    private static double getFreeSwapPercent() {
        return CachedAppOptimizer.getFreeSwapPercent();
    }

    @GuardedBy({"mService", "mProcLock"})
    private boolean updateAndTrimProcessLSP(long j, long j2, long j3, ActiveUids activeUids, int i) {
        double d;
        int i2;
        double d2;
        boolean z;
        int i3;
        double d3;
        boolean z2;
        ProcessRecord processRecord;
        int i4;
        int i5;
        int i6;
        ProcessRecord processRecord2;
        int i7;
        int i8;
        ArrayList<ProcessRecord> lruProcessesLOSP = this.mProcessList.getLruProcessesLOSP();
        int size = lruProcessesLOSP.size();
        boolean shouldKillExcessiveProcesses = shouldKillExcessiveProcesses(j);
        if (!shouldKillExcessiveProcesses && this.mNextNoKillDebugMessageTime < j) {
            Slog.d(TAG, "Not killing cached processes");
            this.mNextNoKillDebugMessageTime = j + 5000;
        }
        int i9 = shouldKillExcessiveProcesses ? this.mConstants.CUR_MAX_EMPTY_PROCESSES : Integer.MAX_VALUE;
        int i10 = shouldKillExcessiveProcesses ? this.mConstants.CUR_MAX_CACHED_PROCESSES - i9 : Integer.MAX_VALUE;
        boolean z3 = ActivityManagerConstants.PROACTIVE_KILLS_ENABLED;
        double d4 = ActivityManagerConstants.LOW_SWAP_THRESHOLD_PERCENT;
        double freeSwapPercent = z3 ? getFreeSwapPercent() : 1.0d;
        boolean z4 = true;
        int i11 = size - 1;
        ProcessRecord processRecord3 = null;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        int i15 = 0;
        int i16 = 0;
        int i17 = 0;
        while (i11 >= 0) {
            ProcessRecord processRecord4 = lruProcessesLOSP.get(i11);
            ArrayList<ProcessRecord> arrayList = lruProcessesLOSP;
            ProcessStateRecord processStateRecord = processRecord4.mState;
            if (processRecord4.isKilledByAm() || processRecord4.getThread() == null || processRecord4.isPendingFinishAttach()) {
                i2 = i11;
                d2 = freeSwapPercent;
                z = shouldKillExcessiveProcesses;
                i3 = i10;
                d3 = d4;
                z2 = z4;
                processRecord = processRecord3;
                i12 = i12;
                i14 = i14;
                i13 = i13;
            } else {
                int i18 = i12;
                if (processStateRecord.getCompletedAdjSeq() == this.mAdjSeq) {
                    i5 = i18;
                    d3 = d4;
                    i6 = i13;
                    processRecord2 = processRecord4;
                    z = shouldKillExcessiveProcesses;
                    i2 = i11;
                    d2 = freeSwapPercent;
                    i7 = i14;
                    i4 = i10;
                    z2 = true;
                    applyOomAdjLSP(processRecord4, true, j, j2, i);
                } else {
                    i2 = i11;
                    d2 = freeSwapPercent;
                    z = shouldKillExcessiveProcesses;
                    i4 = i10;
                    d3 = d4;
                    i5 = i18;
                    z2 = true;
                    i6 = i13;
                    processRecord2 = processRecord4;
                    i7 = i14;
                }
                ProcessServiceRecord processServiceRecord = processRecord2.mServices;
                int curProcState = processStateRecord.getCurProcState();
                if (curProcState == 16 || curProcState == 17) {
                    this.mNumCachedHiddenProcs += z2 ? 1 : 0;
                    int i19 = i15 + 1;
                    int connectionGroup = processServiceRecord.getConnectionGroup();
                    if (connectionGroup == 0) {
                        connectionGroup = 0;
                        i6 = 0;
                    } else if (i6 == processRecord2.info.uid && (i8 = i5) == connectionGroup) {
                        i17++;
                        connectionGroup = i8;
                    } else {
                        i6 = processRecord2.info.uid;
                    }
                    i3 = i4;
                    if (i19 - i17 > i3) {
                        if (!mOomAdjusterExt.onHookKillCacheEmpty(processRecord2.getWindowProcessController())) {
                            processRecord2.killLocked("cached #" + i19, "too many cached", 13, 2, true);
                        }
                    } else if (z3) {
                        i15 = i19;
                        i5 = connectionGroup;
                        i13 = i6;
                        processRecord = processRecord2;
                    }
                    i15 = i19;
                    i5 = connectionGroup;
                    i13 = i6;
                    processRecord = processRecord3;
                } else {
                    if (curProcState == 19) {
                        if (i7 <= this.mConstants.CUR_TRIM_EMPTY_PROCESSES || processRecord2.getLastActivityTime() >= j3) {
                            int i20 = i7 + 1;
                            if (i20 > i9) {
                                if (!mOomAdjusterExt.onHookKillCacheEmpty(processRecord2.getWindowProcessController())) {
                                    processRecord2.killLocked("empty #" + i20, "too many empty", 13, 3, true);
                                }
                            } else if (z3) {
                                i7 = i20;
                                i13 = i6;
                                processRecord = processRecord2;
                                i3 = i4;
                            }
                            processRecord = processRecord3;
                            i7 = i20;
                            i13 = i6;
                            i3 = i4;
                        } else if (!mOomAdjusterExt.onHookKillCacheEmpty(processRecord2.getWindowProcessController())) {
                            processRecord2.killLocked("empty for " + ((j - processRecord2.getLastActivityTime()) / 1000) + "s", "empty for too long", 13, 4, true);
                        }
                    } else {
                        this.mNumNonCachedProcs += z2 ? 1 : 0;
                    }
                    processRecord = processRecord3;
                    i13 = i6;
                    i3 = i4;
                }
                if (processRecord2.isolated && processServiceRecord.numberOfRunningServices() <= 0 && processRecord2.getIsolatedEntryPoint() == null) {
                    processRecord2.killLocked("isolated not needed", 13, 17, z2);
                } else if (processRecord2.isSdkSandbox && processServiceRecord.numberOfRunningServices() <= 0 && processRecord2.getActiveInstrumentation() == null) {
                    processRecord2.killLocked("sandbox not needed", 13, 28, z2);
                } else {
                    updateAppUidRecLSP(processRecord2);
                }
                if (processStateRecord.getCurProcState() >= 14 && !processRecord2.isKilledByAm()) {
                    i16++;
                }
                i14 = i7;
                i12 = i5;
            }
            i11 = i2 - 1;
            processRecord3 = processRecord;
            z4 = z2;
            lruProcessesLOSP = arrayList;
            d4 = d3;
            shouldKillExcessiveProcesses = z;
            i10 = i3;
            freeSwapPercent = d2;
        }
        double d5 = freeSwapPercent;
        boolean z5 = z4;
        boolean z6 = shouldKillExcessiveProcesses;
        double d6 = d4;
        int i21 = i14;
        if (z3 && z6) {
            d = d5;
            if (d < d6 && processRecord3 != null && d < this.mLastFreeSwapPercent) {
                processRecord3.killLocked("swap low and too many cached", 13, 2, z5);
            }
        } else {
            d = d5;
        }
        this.mLastFreeSwapPercent = d;
        return this.mService.mAppProfiler.updateLowMemStateLSP(i15, i21, i16, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mService", "mProcLock"})
    public void updateAppUidRecIfNecessaryLSP(ProcessRecord processRecord) {
        if (processRecord.isKilledByAm() || processRecord.getThread() == null) {
            return;
        }
        if (processRecord.isolated && processRecord.mServices.numberOfRunningServices() <= 0 && processRecord.getIsolatedEntryPoint() == null) {
            return;
        }
        updateAppUidRecLSP(processRecord);
    }

    @GuardedBy({"mService", "mProcLock"})
    private void updateAppUidRecLSP(ProcessRecord processRecord) {
        UidRecord uidRecord = processRecord.getUidRecord();
        if (uidRecord != null) {
            ProcessStateRecord processStateRecord = processRecord.mState;
            uidRecord.setEphemeral(processRecord.info.isInstantApp());
            if (uidRecord.getCurProcState() > processStateRecord.getCurProcState()) {
                uidRecord.setCurProcState(processStateRecord.getCurProcState());
            }
            if (processRecord.mServices.hasForegroundServices()) {
                uidRecord.setForegroundServices(true);
            }
            uidRecord.setCurCapability(uidRecord.getCurCapability() | processStateRecord.getCurCapability());
        }
    }

    @GuardedBy({"mService", "mProcLock"})
    private void updateUidsLSP(ActiveUids activeUids, long j) {
        int i;
        this.mProcessList.incrementProcStateSeqAndNotifyAppsLOSP(activeUids);
        ArrayList<UidRecord> arrayList = this.mTmpBecameIdle;
        arrayList.clear();
        PowerManagerInternal powerManagerInternal = this.mLocalPowerManager;
        if (powerManagerInternal != null) {
            powerManagerInternal.startUidChanges();
        }
        for (int size = activeUids.size() - 1; size >= 0; size--) {
            UidRecord valueAt = activeUids.valueAt(size);
            if (valueAt.getCurProcState() != 20 && (valueAt.getSetProcState() != valueAt.getCurProcState() || valueAt.getSetCapability() != valueAt.getCurCapability() || valueAt.isSetAllowListed() != valueAt.isCurAllowListed() || valueAt.getProcAdjChanged())) {
                if (ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                    Slog.i(ActivityManagerService.TAG_UID_OBSERVERS, "Changes in " + valueAt + ": proc state from " + valueAt.getSetProcState() + " to " + valueAt.getCurProcState() + ", capability from " + valueAt.getSetCapability() + " to " + valueAt.getCurCapability() + ", allowlist from " + valueAt.isSetAllowListed() + " to " + valueAt.isCurAllowListed() + ", procAdjChanged: " + valueAt.getProcAdjChanged());
                }
                if (ActivityManager.isProcStateBackground(valueAt.getCurProcState()) && !valueAt.isCurAllowListed()) {
                    if (!ActivityManager.isProcStateBackground(valueAt.getSetProcState()) || valueAt.isSetAllowListed()) {
                        valueAt.setLastBackgroundTime(j);
                        if (this.mService.mDeterministicUidIdle || !this.mService.mHandler.hasMessages(58)) {
                            this.mService.mHandler.sendEmptyMessageDelayed(58, this.mConstants.BACKGROUND_SETTLE_TIME);
                        }
                    }
                    if (!valueAt.isIdle() || valueAt.isSetIdle()) {
                        i = 0;
                    } else {
                        if (valueAt.getSetProcState() != 20) {
                            arrayList.add(valueAt);
                        }
                        i = 2;
                    }
                } else {
                    if (valueAt.isIdle()) {
                        EventLogTags.writeAmUidActive(valueAt.getUid());
                        valueAt.setIdle(false);
                        i = 4;
                    } else {
                        i = 0;
                    }
                    valueAt.setLastBackgroundTime(0L);
                }
                boolean z = valueAt.getSetProcState() > 11;
                boolean z2 = valueAt.getCurProcState() > 11;
                if (z != z2 || valueAt.getSetProcState() == 20) {
                    i |= z2 ? 8 : 16;
                }
                if (valueAt.getSetCapability() != valueAt.getCurCapability()) {
                    i |= 32;
                }
                if (valueAt.getSetProcState() != valueAt.getCurProcState()) {
                    i |= Integer.MIN_VALUE;
                }
                if (valueAt.getProcAdjChanged()) {
                    i |= 64;
                }
                valueAt.setSetProcState(valueAt.getCurProcState());
                valueAt.setSetCapability(valueAt.getCurCapability());
                valueAt.setSetAllowListed(valueAt.isCurAllowListed());
                valueAt.setSetIdle(valueAt.isIdle());
                valueAt.clearProcAdjChanged();
                int i2 = i & Integer.MIN_VALUE;
                if (i2 != 0 || (i & 32) != 0) {
                    this.mService.mAtmInternal.onUidProcStateChanged(valueAt.getUid(), valueAt.getSetProcState());
                }
                if (i != 0) {
                    this.mService.enqueueUidChangeLocked(valueAt, -1, i);
                }
                if (i2 != 0 || (i & 32) != 0) {
                    this.mService.noteUidProcessState(valueAt.getUid(), valueAt.getCurProcState(), valueAt.getCurCapability());
                }
                if (valueAt.hasForegroundServices()) {
                    this.mService.mServices.foregroundServiceProcStateChangedLocked(valueAt);
                }
            }
            this.mService.mInternal.deletePendingTopUid(valueAt.getUid(), j);
        }
        PowerManagerInternal powerManagerInternal2 = this.mLocalPowerManager;
        if (powerManagerInternal2 != null) {
            powerManagerInternal2.finishUidChanges();
        }
        int size2 = arrayList.size();
        if (size2 > 0) {
            for (int i3 = size2 - 1; i3 >= 0; i3--) {
                try {
                    this.mService.mServices.stopInBackgroundLocked(arrayList.get(i3).getUid());
                } catch (IndexOutOfBoundsException unused) {
                    Log.i(TAG, "becameIdle list IndexOutOfBoundsException");
                    return;
                }
            }
        }
    }

    private boolean shouldKillExcessiveProcesses(long j) {
        long lastUserUnlockingUptime = this.mService.mUserController.getLastUserUnlockingUptime();
        if (lastUserUnlockingUptime == 0) {
            return !this.mConstants.mNoKillCachedProcessesUntilBootCompleted;
        }
        return lastUserUnlockingUptime + this.mConstants.mNoKillCachedProcessesPostBootCompletedDurationMillis <= j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ComputeOomAdjWindowCallback implements WindowProcessController.ComputeOomAdjCallback {
        int adj;
        ProcessRecord app;
        int appUid;
        boolean foregroundActivities;
        int logUid;
        boolean mHasVisibleActivities;
        ProcessStateRecord mState;
        int procState;
        int processStateCurTop;
        int schedGroup;

        ComputeOomAdjWindowCallback() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void initialize(ProcessRecord processRecord, int i, boolean z, boolean z2, int i2, int i3, int i4, int i5, int i6) {
            this.app = processRecord;
            this.adj = i;
            this.foregroundActivities = z;
            this.mHasVisibleActivities = z2;
            this.procState = i2;
            this.schedGroup = i3;
            this.appUid = i4;
            this.logUid = i5;
            this.processStateCurTop = i6;
            this.mState = processRecord.mState;
        }

        public void onVisibleActivity() {
            if (this.adj > 100) {
                this.adj = 100;
                this.mState.setAdjType("vis-activity");
                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    OomAdjuster.this.reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise adj to vis-activity: " + this.app);
                }
            }
            int i = this.procState;
            int i2 = this.processStateCurTop;
            if (i > i2) {
                this.procState = i2;
                this.mState.setAdjType("vis-activity");
                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    OomAdjuster.this.reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to vis-activity (top): " + this.app);
                }
            }
            if (this.schedGroup < 2) {
                this.schedGroup = 2;
            }
            this.mState.setCached(false);
            this.mState.setEmpty(false);
            this.foregroundActivities = true;
            this.mHasVisibleActivities = true;
        }

        public void onPausedActivity() {
            if (this.adj > 200) {
                this.adj = 200;
                this.mState.setAdjType("pause-activity");
                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    OomAdjuster.this.reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise adj to pause-activity: " + this.app);
                }
            }
            int i = this.procState;
            int i2 = this.processStateCurTop;
            if (i > i2) {
                this.procState = i2;
                this.mState.setAdjType("pause-activity");
                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    OomAdjuster.this.reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to pause-activity (top): " + this.app);
                }
            }
            if (this.schedGroup < 2) {
                this.schedGroup = 2;
            }
            this.mState.setCached(false);
            this.mState.setEmpty(false);
            this.foregroundActivities = true;
            this.mHasVisibleActivities = false;
        }

        public void onStoppingActivity(boolean z) {
            if (this.adj > 200) {
                this.adj = 200;
                this.mState.setAdjType("stop-activity");
                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    OomAdjuster.this.reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise adj to stop-activity: " + this.app);
                }
            }
            if (!z && this.procState > 15) {
                this.procState = 15;
                this.mState.setAdjType("stop-activity");
                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    OomAdjuster.this.reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to stop-activity: " + this.app);
                }
            }
            this.mState.setCached(false);
            this.mState.setEmpty(false);
            this.foregroundActivities = true;
            this.mHasVisibleActivities = false;
        }

        public void onOtherActivity() {
            if (this.procState > 16) {
                this.procState = 16;
                this.mState.setAdjType("cch-act");
                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    OomAdjuster.this.reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to cached activity: " + this.app);
                }
            }
            this.mHasVisibleActivities = false;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:647:0x0a99, code lost:
    
        if (r1 >= 200) goto L520;
     */
    /* JADX WARN: Code restructure failed: missing block: B:692:0x077b, code lost:
    
        if (r12 < (r15.lastActivity + r59.mConstants.MAX_SERVICE_INACTIVITY)) goto L335;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:114:0x04fa  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0556  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x064c  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0681  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0689  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x06d2  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x0708 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0d5b  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0fe6  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x1053  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x1076  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x10d9  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x10fb  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x110a  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x1122  */
    /* JADX WARN: Removed duplicated region for block: B:262:0x112d  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x1164  */
    /* JADX WARN: Removed duplicated region for block: B:273:0x111b  */
    /* JADX WARN: Removed duplicated region for block: B:375:0x0fd3 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:379:0x0719  */
    /* JADX WARN: Removed duplicated region for block: B:398:0x07cb  */
    /* JADX WARN: Removed duplicated region for block: B:419:0x0807 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:428:0x0823  */
    /* JADX WARN: Removed duplicated region for block: B:460:0x0923  */
    /* JADX WARN: Removed duplicated region for block: B:463:0x093b  */
    /* JADX WARN: Removed duplicated region for block: B:500:0x0a02  */
    /* JADX WARN: Removed duplicated region for block: B:512:0x0aea  */
    /* JADX WARN: Removed duplicated region for block: B:526:0x0b5f  */
    /* JADX WARN: Removed duplicated region for block: B:531:0x0b6e  */
    /* JADX WARN: Removed duplicated region for block: B:533:0x0b75  */
    /* JADX WARN: Removed duplicated region for block: B:539:0x0b81  */
    /* JADX WARN: Removed duplicated region for block: B:543:0x0b94  */
    /* JADX WARN: Removed duplicated region for block: B:551:0x0c2b  */
    /* JADX WARN: Removed duplicated region for block: B:574:0x0bf7  */
    /* JADX WARN: Removed duplicated region for block: B:593:0x0b48  */
    /* JADX WARN: Removed duplicated region for block: B:608:0x0aca  */
    /* JADX WARN: Removed duplicated region for block: B:610:0x0ad0  */
    /* JADX WARN: Removed duplicated region for block: B:673:0x0c03  */
    /* JADX WARN: Removed duplicated region for block: B:678:0x092a  */
    /* JADX WARN: Removed duplicated region for block: B:696:0x07b6  */
    /* JADX WARN: Removed duplicated region for block: B:708:0x07be  */
    /* JADX WARN: Removed duplicated region for block: B:712:0x05f9  */
    /* JADX WARN: Removed duplicated region for block: B:718:0x0625  */
    /* JADX WARN: Type inference failed for: r5v32 */
    /* JADX WARN: Type inference failed for: r5v33, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r5v45 */
    @GuardedBy({"mService", "mProcLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean computeOomAdjLSP(ProcessRecord processRecord, int i, ProcessRecord processRecord2, boolean z, long j, boolean z2, boolean z3) {
        int i2;
        int i3;
        int i4;
        boolean z4;
        boolean z5;
        int i5;
        int i6;
        int i7;
        int i8;
        ProcessServiceRecord processServiceRecord;
        int i9;
        int i10;
        int i11;
        String str;
        int i12;
        int i13;
        int i14;
        String str2;
        int i15;
        BackupRecord backupRecord;
        boolean isCurBoundByNonBgRestrictedApp;
        int numberOfRunningServices;
        int i16;
        ProcessServiceRecord processServiceRecord2;
        ProcessStateRecord processStateRecord;
        String str3;
        int i17;
        int i18;
        int i19;
        String str4;
        int i20;
        int i21;
        boolean z6;
        ProcessProviderRecord processProviderRecord;
        int numberOfProviders;
        boolean z7;
        ProcessStateRecord processStateRecord2;
        ProcessServiceRecord processServiceRecord3;
        int i22;
        ProcessProviderRecord processProviderRecord2;
        boolean z8;
        int i23;
        int i24;
        int i25;
        ProcessServiceRecord processServiceRecord4;
        boolean z9;
        int i26;
        int i27;
        ProcessServiceRecord processServiceRecord5;
        String str5;
        ProcessProviderRecord processProviderRecord3;
        int i28;
        int i29;
        int i30;
        String str6;
        ContentProviderRecord contentProviderRecord;
        ProcessStateRecord processStateRecord3;
        ?? r5;
        int i31;
        int i32;
        int i33;
        int i34;
        ContentProviderConnection contentProviderConnection;
        int i35;
        ProcessStateRecord processStateRecord4;
        ProcessServiceRecord processServiceRecord6;
        int i36;
        String str7;
        String str8;
        int i37;
        int i38;
        int i39;
        ContentProviderRecord contentProviderRecord2;
        ProcessRecord processRecord3;
        ProcessStateRecord processStateRecord5;
        boolean z10;
        String str9;
        String str10;
        String str11;
        int i40;
        ServiceRecord runningServiceAt;
        boolean z11;
        int i41;
        ProcessServiceRecord processServiceRecord7;
        int i42;
        ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections;
        int size;
        ArrayList<ConnectionRecord> valueAt;
        int i43;
        ProcessRecord processRecord4;
        int i44;
        int i45;
        ArrayList<ConnectionRecord> arrayList;
        ArrayMap<IBinder, ArrayList<ConnectionRecord>> arrayMap;
        ConnectionRecord connectionRecord;
        int i46;
        int i47;
        int i48;
        int i49;
        int i50;
        ProcessServiceRecord processServiceRecord8;
        ProcessStateRecord processStateRecord6;
        int i51;
        int i52;
        String str12;
        int i53;
        int i54;
        ProcessStateRecord processStateRecord7;
        char c;
        boolean z12;
        ProcessRecord processRecord5;
        ConnectionRecord connectionRecord2;
        ConnectionRecord connectionRecord3;
        String str13;
        ProcessStateRecord processStateRecord8;
        String str14;
        int i55;
        int i56;
        int i57;
        boolean z13;
        int i58;
        int i59;
        ProcessServiceRecord processServiceRecord9;
        Object obj;
        String str15;
        boolean z14;
        int curCapability;
        int i60;
        int i61;
        int i62;
        ProcessRecord processRecord6;
        int i63;
        int i64;
        int i65;
        ProcessRecord processRecord7 = processRecord;
        long j2 = j;
        ProcessStateRecord processStateRecord9 = processRecord7.mState;
        if (this.mAdjSeq == processStateRecord9.getAdjSeq()) {
            if (processStateRecord9.getAdjSeq() == processStateRecord9.getCompletedAdjSeq()) {
                return false;
            }
            processStateRecord9.setContainsCycle(true);
            this.mProcessesInCycle.add(processRecord7);
            return false;
        }
        if (processRecord.getThread() == null) {
            processStateRecord9.setAdjSeq(this.mAdjSeq);
            processStateRecord9.setCurrentSchedulingGroup(0);
            processStateRecord9.setCurProcState(19);
            processStateRecord9.setCurAdj(999);
            processStateRecord9.setCurRawAdj(999);
            processStateRecord9.setCompletedAdjSeq(processStateRecord9.getAdjSeq());
            processStateRecord9.setCurCapability(0);
            return false;
        }
        processStateRecord9.setAdjTypeCode(0);
        processStateRecord9.setAdjSource(null);
        processStateRecord9.setAdjTarget(null);
        processStateRecord9.setEmpty(false);
        processStateRecord9.setCached(false);
        if (!z2) {
            processStateRecord9.setNoKillOnBgRestrictedAndIdle(false);
            UidRecord uidRecord = processRecord.getUidRecord();
            processRecord7.mOptRecord.setShouldNotFreeze(uidRecord != null && uidRecord.isCurAllowListed());
        }
        int i66 = processRecord7.info.uid;
        int i67 = this.mService.mCurOomAdjUid;
        int curAdj = processStateRecord9.getCurAdj();
        int curProcState = processStateRecord9.getCurProcState();
        int curCapability2 = processStateRecord9.getCurCapability();
        ProcessServiceRecord processServiceRecord10 = processRecord7.mServices;
        if (processStateRecord9.getMaxAdj() <= 0) {
            if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Making fixed: " + processRecord7);
            }
            processStateRecord9.setAdjType("fixed");
            processStateRecord9.setAdjSeq(this.mAdjSeq);
            processStateRecord9.setCurRawAdj(processStateRecord9.getMaxAdj());
            processStateRecord9.setHasForegroundActivities(false);
            processStateRecord9.setCurrentSchedulingGroup(2);
            processStateRecord9.setCurCapability(63);
            processStateRecord9.setCurProcState(0);
            processStateRecord9.setSystemNoUi(true);
            if (processRecord7 == processRecord2) {
                processStateRecord9.setSystemNoUi(false);
                processStateRecord9.setCurrentSchedulingGroup(3);
                processStateRecord9.setAdjType("pers-top-activity");
            } else if (processStateRecord9.hasTopUi()) {
                processStateRecord9.setSystemNoUi(false);
                processStateRecord9.setAdjType("pers-top-ui");
            } else if (processStateRecord9.getCachedHasVisibleActivities()) {
                processStateRecord9.setSystemNoUi(false);
            }
            if (!processStateRecord9.isSystemNoUi()) {
                if (this.mService.mWakefulness.get() == 1 || processStateRecord9.isRunningRemoteAnimation()) {
                    processStateRecord9.setCurProcState(1);
                    processStateRecord9.setCurrentSchedulingGroup(3);
                } else {
                    processStateRecord9.setCurProcState(5);
                    processStateRecord9.setCurrentSchedulingGroup(1);
                }
            }
            processStateRecord9.setCurRawProcState(processStateRecord9.getCurProcState());
            processStateRecord9.setCurAdj(processStateRecord9.getMaxAdj());
            processStateRecord9.setCompletedAdjSeq(processStateRecord9.getAdjSeq());
            return processStateRecord9.getCurAdj() < curAdj || processStateRecord9.getCurProcState() < curProcState;
        }
        processStateRecord9.setSystemNoUi(false);
        int topProcessState = this.mService.mAtmInternal.getTopProcessState();
        int curCapability3 = z2 ? processRecord7.mState.getCurCapability() : 0;
        if (processRecord7 == processRecord2 && topProcessState == 2) {
            if (this.mService.mAtmInternal.useTopSchedGroupForTopProcess()) {
                processStateRecord9.setAdjType(HostingRecord.HOSTING_TYPE_TOP_ACTIVITY);
                i65 = 3;
            } else {
                processStateRecord9.setAdjType("intermediate-top-activity");
                i65 = 2;
            }
            this.mSocExt.topAppRenderThreadBoost(processRecord7);
            if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                String str16 = ActivityManagerService.TAG_OOM_ADJ;
                i2 = curProcState;
                StringBuilder sb = new StringBuilder();
                i3 = curAdj;
                sb.append("Making top: ");
                sb.append(processRecord7);
                reportOomAdjMessageLocked(str16, sb.toString());
            } else {
                i2 = curProcState;
                i3 = curAdj;
            }
            i6 = i65;
            i4 = 0;
            z4 = true;
            z5 = true;
            i5 = 2;
        } else {
            i2 = curProcState;
            i3 = curAdj;
            if (processStateRecord9.isRunningRemoteAnimation()) {
                processStateRecord9.setAdjType("running-remote-anim");
                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                    reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Making running remote anim: " + processRecord7);
                }
                i5 = topProcessState;
                i6 = 3;
                i4 = 100;
            } else if (processRecord.getActiveInstrumentation() != null) {
                processStateRecord9.setAdjType("instrumentation");
                curCapability3 |= 16;
                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                    reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Making instrumentation: " + processRecord7);
                }
                i4 = 0;
                z4 = false;
                z5 = false;
                i5 = 4;
                i6 = 2;
            } else if (processStateRecord9.getCachedIsReceivingBroadcast(this.mTmpSchedGroup)) {
                int i68 = this.mTmpSchedGroup[0];
                processStateRecord9.setAdjType("broadcast");
                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                    reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Making broadcast: " + processRecord7);
                }
                i6 = i68;
                i5 = 11;
                i4 = 0;
            } else if (processServiceRecord10.numberOfExecutingServices() > 0) {
                int i69 = processServiceRecord10.shouldExecServicesFg() ? 2 : 0;
                processStateRecord9.setAdjType("exec-service");
                if (mOomAdjusterExt.isFrozen(i66)) {
                    processStateRecord9.setAdjType("frozen-exec-service");
                    i7 = 500;
                } else {
                    i7 = 0;
                }
                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                    String str17 = ActivityManagerService.TAG_OOM_ADJ;
                    StringBuilder sb2 = new StringBuilder();
                    i8 = i69;
                    sb2.append("Making exec-service: ");
                    sb2.append(processRecord7);
                    reportOomAdjMessageLocked(str17, sb2.toString());
                } else {
                    i8 = i69;
                }
                i6 = i8;
                z4 = false;
                z5 = false;
                i5 = 10;
                i4 = i7;
            } else {
                if (processRecord7 == processRecord2) {
                    processStateRecord9.setAdjType("top-sleeping");
                    if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                        reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Making top (sleeping): " + processRecord7);
                    }
                    i5 = topProcessState;
                    i4 = 0;
                    z4 = true;
                    z5 = false;
                } else {
                    if (!processStateRecord9.containsCycle()) {
                        processStateRecord9.setCached(true);
                        processStateRecord9.setEmpty(true);
                        processStateRecord9.setAdjType("cch-empty");
                    }
                    if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                        reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Making empty: " + processRecord7);
                    }
                    i4 = i;
                    z4 = false;
                    z5 = false;
                    i5 = 19;
                }
                i6 = 0;
            }
            z4 = false;
            z5 = false;
        }
        if (z4 || !processStateRecord9.getCachedHasActivities()) {
            processServiceRecord = processServiceRecord10;
            i9 = curCapability2;
        } else {
            processServiceRecord = processServiceRecord10;
            i9 = curCapability2;
            processStateRecord9.computeOomAdjFromActivitiesIfNecessary(this.mTmpComputeOomAdjWindowCallback, i4, z4, z5, i5, i6, i66, i67, topProcessState);
            i4 = processStateRecord9.getCachedAdj();
            z4 = processStateRecord9.getCachedForegroundActivities();
            z5 = processStateRecord9.getCachedHasVisibleActivities();
            i5 = processStateRecord9.getCachedProcState();
            i6 = processStateRecord9.getCachedSchedGroup();
        }
        int i70 = i4;
        boolean z15 = z4;
        boolean z16 = z5;
        int i71 = i5;
        if (i71 <= 18 || !processStateRecord9.getCachedHasRecentTasks()) {
            i10 = i9;
        } else {
            processStateRecord9.setAdjType("cch-rec");
            if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                String str18 = ActivityManagerService.TAG_OOM_ADJ;
                StringBuilder sb3 = new StringBuilder();
                i10 = i9;
                sb3.append("Raise procstate to cached recent: ");
                sb3.append(processRecord7);
                reportOomAdjMessageLocked(str18, sb3.toString());
            } else {
                i10 = i9;
            }
            i71 = 18;
        }
        boolean hasForegroundServices = processServiceRecord.hasForegroundServices();
        boolean hasNonShortForegroundServices = processServiceRecord.hasNonShortForegroundServices();
        boolean z17 = hasForegroundServices && !processServiceRecord.areAllShortForegroundServicesProcstateTimedOut(j2);
        String str19 = ": ";
        if (i70 <= 200) {
            i11 = 4;
            if (i71 <= 4) {
                i14 = 0;
                if (processServiceRecord.hasForegroundServices() || i70 <= 50) {
                    str2 = "Raise to ";
                } else {
                    str2 = "Raise to ";
                    if (processStateRecord9.getLastTopTime() + this.mConstants.TOP_TO_FGS_GRACE_DURATION > j2 || processStateRecord9.getSetProcState() <= 2) {
                        if (processServiceRecord.hasNonShortForegroundServices()) {
                            processStateRecord9.setAdjType("fg-service-act");
                            i70 = 50;
                        } else {
                            processStateRecord9.setAdjType("fg-service-short-act");
                            i70 = 51;
                        }
                        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                            reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise to recent fg: " + processRecord7);
                        }
                    }
                }
                if (processServiceRecord.hasTopStartedAlmostPerceptibleServices() && i70 > 52 && (processStateRecord9.getLastTopTime() + this.mConstants.TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION > j2 || processStateRecord9.getSetProcState() <= 2)) {
                    processStateRecord9.setAdjType("top-ej-act");
                    if (!ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                        reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise to recent fg for EJ: " + processRecord7);
                    }
                    i70 = 52;
                }
                if ((i70 <= 200 || i71 > 8) && processStateRecord9.getForcingToImportant() != null) {
                    processStateRecord9.setCached(false);
                    processStateRecord9.setAdjType("force-imp");
                    processStateRecord9.setAdjSource(processStateRecord9.getForcingToImportant());
                    if (!ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                        reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise to force imp: " + processRecord7);
                    }
                    i71 = 8;
                    i70 = 200;
                    i6 = 2;
                }
                if (processStateRecord9.getCachedIsHeavyWeight()) {
                    if (i70 > 400) {
                        processStateRecord9.setCached(false);
                        processStateRecord9.setAdjType("heavy");
                        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                            reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise adj to heavy: " + processRecord7);
                        }
                        i70 = 400;
                        i6 = 0;
                    }
                    if (i71 > 13) {
                        processStateRecord9.setAdjType("heavy");
                        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                            reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to heavy: " + processRecord7);
                        }
                        i71 = 13;
                    }
                }
                if (processStateRecord9.getCachedIsHomeProcess()) {
                    if (i70 > 600) {
                        processStateRecord9.setCached(false);
                        processStateRecord9.setAdjType("home");
                        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                            reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise adj to home: " + processRecord7);
                        }
                        i70 = 600;
                        i6 = 0;
                    }
                    if (i71 > 14) {
                        processStateRecord9.setAdjType("home");
                        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                            reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to home: " + processRecord7);
                        }
                        i71 = 14;
                    }
                }
                if (processStateRecord9.getCachedIsPreviousProcess() && processStateRecord9.getCachedHasActivities()) {
                    if (i71 < 15 && processStateRecord9.getSetProcState() == 15 && processStateRecord9.getLastStateTime() + ActivityManagerConstants.MAX_PREVIOUS_TIME < j2) {
                        processStateRecord9.setAdjType("previous-expired");
                        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                            reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Expire prev adj: " + processRecord7);
                        }
                        i71 = 15;
                        i70 = ProcessList.CACHED_APP_MIN_ADJ;
                        i15 = 0;
                        if (z2) {
                            i71 = Math.min(i71, processStateRecord9.getCurRawProcState());
                            i70 = Math.min(i70, processStateRecord9.getCurRawAdj());
                            i15 = Math.max(i15, processStateRecord9.getCurrentSchedulingGroup());
                        }
                        processStateRecord9.setCurRawAdj(i70);
                        processStateRecord9.setCurRawProcState(i71);
                        processStateRecord9.setHasStartedServices(false);
                        processStateRecord9.setAdjSeq(this.mAdjSeq);
                        backupRecord = this.mService.mBackupTargets.get(processRecord7.userId);
                        if (backupRecord != null && processRecord7 == backupRecord.app) {
                            if (i70 > 300) {
                                if (ActivityManagerDebugConfig.DEBUG_BACKUP) {
                                    Slog.v(ActivityManagerService.TAG_BACKUP, "oom BACKUP_APP_ADJ for " + processRecord7);
                                }
                                if (i71 > 8) {
                                    i71 = 8;
                                }
                                processStateRecord9.setAdjType(HostingRecord.HOSTING_TYPE_BACKUP);
                                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                                    reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise adj to backup: " + processRecord7);
                                }
                                processStateRecord9.setCached(false);
                                i70 = 300;
                            }
                            if (i71 > 9) {
                                processStateRecord9.setAdjType(HostingRecord.HOSTING_TYPE_BACKUP);
                                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                                    reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to backup: " + processRecord7);
                                }
                                i71 = 9;
                            }
                        }
                        isCurBoundByNonBgRestrictedApp = processStateRecord9.isCurBoundByNonBgRestrictedApp();
                        numberOfRunningServices = processServiceRecord.numberOfRunningServices() - 1;
                        int i72 = i14;
                        boolean z18 = false;
                        while (numberOfRunningServices >= 0 && (i70 > 0 || i15 == 0 || i71 > 2)) {
                            runningServiceAt = processServiceRecord.getRunningServiceAt(numberOfRunningServices);
                            int i73 = i15;
                            if (runningServiceAt.startRequested) {
                                z11 = isCurBoundByNonBgRestrictedApp;
                            } else {
                                processStateRecord9.setHasStartedServices(true);
                                if (i71 > 10) {
                                    processStateRecord9.setAdjType("started-services");
                                    if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                                        String str20 = ActivityManagerService.TAG_OOM_ADJ;
                                        StringBuilder sb4 = new StringBuilder();
                                        z11 = isCurBoundByNonBgRestrictedApp;
                                        sb4.append("Raise procstate to started service: ");
                                        sb4.append(processRecord7);
                                        reportOomAdjMessageLocked(str20, sb4.toString());
                                    } else {
                                        z11 = isCurBoundByNonBgRestrictedApp;
                                    }
                                    i71 = 10;
                                } else {
                                    z11 = isCurBoundByNonBgRestrictedApp;
                                }
                                if (runningServiceAt.mKeepWarming || !processStateRecord9.hasShownUi() || processStateRecord9.getCachedIsHomeProcess()) {
                                    if (runningServiceAt.mKeepWarming) {
                                        i64 = i71;
                                        i41 = curCapability3;
                                        processServiceRecord7 = processServiceRecord;
                                    } else {
                                        i64 = i71;
                                        i41 = curCapability3;
                                        processServiceRecord7 = processServiceRecord;
                                    }
                                    if (i70 > 500) {
                                        processStateRecord9.setAdjType("started-services");
                                        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                                            reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise adj to started service: " + processRecord7);
                                        }
                                        i42 = 0;
                                        processStateRecord9.setCached(false);
                                        i70 = 500;
                                        if (i70 > 500) {
                                            processStateRecord9.setAdjType("cch-started-services");
                                        }
                                        i71 = i64;
                                        if (runningServiceAt.isForeground) {
                                            int i74 = runningServiceAt.foregroundServiceType;
                                            if (runningServiceAt.mAllowWhileInUsePermissionInFgs) {
                                                int i75 = i72 | ((i74 & 8) != 0 ? 1 : i42);
                                                if (processStateRecord9.getCachedCompatChange(1)) {
                                                    i63 = ((i74 & 128) != 0 ? 4 : 0) | ((i74 & 64) != 0 ? 2 : 0) | i75;
                                                } else {
                                                    i63 = i75 | 6;
                                                }
                                                i72 = i63;
                                            }
                                        }
                                        connections = runningServiceAt.getConnections();
                                        size = connections.size() - 1;
                                        i15 = i73;
                                        while (size >= 0 && (i70 > 0 || i15 == 0 || i71 > 2)) {
                                            valueAt = connections.valueAt(size);
                                            int i76 = i67;
                                            boolean z19 = z18;
                                            int i77 = i15;
                                            int i78 = i70;
                                            int i79 = i71;
                                            i43 = 0;
                                            while (i43 < valueAt.size()) {
                                                if (i78 <= 0 && i77 != 0) {
                                                    if (i79 <= 2) {
                                                        break;
                                                    }
                                                }
                                                ProcessStateRecord processStateRecord10 = processStateRecord9;
                                                ConnectionRecord connectionRecord4 = valueAt.get(i43);
                                                AppBindRecord appBindRecord = connectionRecord4.binding;
                                                int i80 = i79;
                                                ProcessRecord processRecord8 = appBindRecord.client;
                                                if (processRecord8 == processRecord7) {
                                                    i45 = size;
                                                    arrayList = valueAt;
                                                    arrayMap = connections;
                                                    str13 = str19;
                                                    i46 = numberOfRunningServices;
                                                    i47 = i10;
                                                    i48 = i2;
                                                    i49 = i3;
                                                    str14 = str2;
                                                    i56 = i76;
                                                    processServiceRecord9 = processServiceRecord7;
                                                    processStateRecord8 = processStateRecord10;
                                                    i79 = i80;
                                                    i53 = i43;
                                                    i57 = i66;
                                                    i54 = i78;
                                                } else {
                                                    ProcessRecord processRecord9 = (!processRecord7.isSdkSandbox || (processRecord6 = appBindRecord.attributedClient) == null) ? processRecord8 : processRecord6;
                                                    ProcessStateRecord processStateRecord11 = processRecord9.mState;
                                                    if (z3) {
                                                        i48 = i2;
                                                        i51 = i80;
                                                        i49 = i3;
                                                        i53 = i43;
                                                        processRecord4 = processRecord9;
                                                        i50 = i76;
                                                        i44 = i66;
                                                        i54 = i78;
                                                        str12 = null;
                                                        i45 = size;
                                                        arrayList = valueAt;
                                                        i47 = i10;
                                                        arrayMap = connections;
                                                        i46 = numberOfRunningServices;
                                                        i52 = 2;
                                                        processServiceRecord8 = processServiceRecord7;
                                                        connectionRecord = connectionRecord4;
                                                        processStateRecord6 = processStateRecord10;
                                                        computeOomAdjLSP(processRecord9, i, processRecord2, z, j, z2, true);
                                                        processStateRecord7 = processStateRecord11;
                                                    } else {
                                                        processRecord4 = processRecord9;
                                                        i44 = i66;
                                                        i45 = size;
                                                        arrayList = valueAt;
                                                        arrayMap = connections;
                                                        connectionRecord = connectionRecord4;
                                                        i46 = numberOfRunningServices;
                                                        i47 = i10;
                                                        i48 = i2;
                                                        i49 = i3;
                                                        i50 = i76;
                                                        processServiceRecord8 = processServiceRecord7;
                                                        processStateRecord6 = processStateRecord10;
                                                        i51 = i80;
                                                        i52 = 2;
                                                        str12 = null;
                                                        i53 = i43;
                                                        i54 = i78;
                                                        processStateRecord7 = processStateRecord11;
                                                        processStateRecord7.setCurRawAdj(processStateRecord11.getCurAdj());
                                                        processStateRecord7.setCurRawProcState(processStateRecord7.getCurProcState());
                                                    }
                                                    int curRawAdj = processStateRecord7.getCurRawAdj();
                                                    int curRawProcState = processStateRecord7.getCurRawProcState();
                                                    boolean z20 = curRawProcState < i52;
                                                    if (processStateRecord7.isCurBoundByNonBgRestrictedApp() || curRawProcState <= 3) {
                                                        c = 4;
                                                    } else {
                                                        c = 4;
                                                        if (curRawProcState != 4 || processStateRecord7.isBackgroundRestricted()) {
                                                            z12 = false;
                                                            boolean z21 = z11 | z12;
                                                            processRecord5 = processRecord4;
                                                            if (!processRecord5.mOptRecord.shouldNotFreeze()) {
                                                                processRecord7.mOptRecord.setShouldNotFreeze(true);
                                                            }
                                                            int bfslCapabilityFromClient = i41 | getBfslCapabilityFromClient(processRecord5);
                                                            connectionRecord2 = connectionRecord;
                                                            if (connectionRecord2.notHasFlag(32)) {
                                                                connectionRecord3 = connectionRecord2;
                                                                str13 = str19;
                                                                processStateRecord8 = processStateRecord6;
                                                                str14 = str2;
                                                                i55 = i54;
                                                                i56 = i50;
                                                                i57 = i44;
                                                                if (curRawAdj < 900) {
                                                                    z13 = true;
                                                                    processRecord7.mOptRecord.setShouldNotFreeze(true);
                                                                } else {
                                                                    z13 = true;
                                                                }
                                                                i58 = bfslCapabilityFromClient;
                                                                i79 = i51;
                                                                i59 = i77;
                                                            } else {
                                                                if (connectionRecord2.hasFlag(4096)) {
                                                                    bfslCapabilityFromClient |= processStateRecord7.getCurCapability();
                                                                }
                                                                if ((processStateRecord7.getCurCapability() & 8) != 0 && (curRawProcState > 5 || connectionRecord2.hasFlag(131072))) {
                                                                    bfslCapabilityFromClient |= 8;
                                                                }
                                                                if ((processStateRecord7.getCurCapability() & 32) == 0 || curRawProcState > 6) {
                                                                    obj = processRecord5;
                                                                } else {
                                                                    obj = processRecord5;
                                                                    if (connectionRecord2.hasFlag(4294967296L)) {
                                                                        bfslCapabilityFromClient |= 32;
                                                                    }
                                                                }
                                                                i58 = bfslCapabilityFromClient;
                                                                connectionRecord3 = connectionRecord2;
                                                                String str21 = str19;
                                                                if (shouldSkipDueToCycle(processRecord, processStateRecord7, i51, i54, z2)) {
                                                                    processStateRecord8 = processStateRecord6;
                                                                    z11 = z21;
                                                                    i41 = i58;
                                                                    i79 = i51;
                                                                    str14 = str2;
                                                                    str13 = str21;
                                                                    i56 = i50;
                                                                    i57 = i44;
                                                                    processServiceRecord9 = processServiceRecord8;
                                                                } else {
                                                                    if (curRawProcState >= 16) {
                                                                        curRawProcState = 19;
                                                                    }
                                                                    if (connectionRecord3.hasFlag(16)) {
                                                                        if (curRawAdj < 900) {
                                                                            processRecord7.mOptRecord.setShouldNotFreeze(true);
                                                                        }
                                                                        if (!processStateRecord6.hasShownUi() || processStateRecord6.getCachedIsHomeProcess()) {
                                                                            processStateRecord8 = processStateRecord6;
                                                                            i55 = i54;
                                                                            if (j2 >= runningServiceAt.lastActivity + this.mConstants.MAX_SERVICE_INACTIVITY) {
                                                                                str15 = i55 > curRawAdj ? "cch-bound-services" : str12;
                                                                                curRawAdj = i55;
                                                                            }
                                                                        } else {
                                                                            i55 = i54;
                                                                            str15 = i55 > curRawAdj ? "cch-bound-ui-services" : str12;
                                                                            processStateRecord8 = processStateRecord6;
                                                                            processStateRecord8.setCached(false);
                                                                            curRawAdj = i55;
                                                                            curRawProcState = i51;
                                                                        }
                                                                        if (i55 > curRawAdj) {
                                                                            if (!processStateRecord8.hasShownUi() || processStateRecord8.getCachedIsHomeProcess() || curRawAdj <= 200) {
                                                                                if (!connectionRecord3.hasFlag(72)) {
                                                                                    if (!connectionRecord3.hasFlag(256) || curRawAdj > 200) {
                                                                                        i60 = 100;
                                                                                    } else if (i55 >= 250) {
                                                                                        i59 = i77;
                                                                                        i60 = 250;
                                                                                        z14 = false;
                                                                                        if (!processStateRecord7.isCached()) {
                                                                                        }
                                                                                        if (i55 > i60) {
                                                                                        }
                                                                                        i79 = i51;
                                                                                        if (!connectionRecord3.notHasFlag(8388612)) {
                                                                                        }
                                                                                        if (i59 < 3) {
                                                                                            i59 = 3;
                                                                                            z19 = true;
                                                                                        }
                                                                                        if (!z14) {
                                                                                        }
                                                                                        if (i79 > curRawProcState) {
                                                                                        }
                                                                                        if (i79 < 7) {
                                                                                        }
                                                                                        processRecord7 = processRecord;
                                                                                        if (str15 == null) {
                                                                                        }
                                                                                        z13 = true;
                                                                                    } else {
                                                                                        i60 = 250;
                                                                                    }
                                                                                    if (connectionRecord3.hasFlag(65536) && connectionRecord3.notHasFlag(4) && curRawAdj < 200) {
                                                                                        if (i55 >= 200) {
                                                                                            i60 = 201;
                                                                                            i59 = i77;
                                                                                            z14 = false;
                                                                                            if (!processStateRecord7.isCached()) {
                                                                                            }
                                                                                            if (i55 > i60) {
                                                                                            }
                                                                                            i79 = i51;
                                                                                            if (!connectionRecord3.notHasFlag(8388612)) {
                                                                                            }
                                                                                            if (i59 < 3) {
                                                                                            }
                                                                                            if (!z14) {
                                                                                            }
                                                                                            if (i79 > curRawProcState) {
                                                                                            }
                                                                                            if (i79 < 7) {
                                                                                            }
                                                                                            processRecord7 = processRecord;
                                                                                            if (str15 == null) {
                                                                                            }
                                                                                            z13 = true;
                                                                                        } else {
                                                                                            i60 = 200;
                                                                                        }
                                                                                    }
                                                                                    if (connectionRecord3.hasFlag(65536) && connectionRecord3.hasFlag(4) && curRawAdj < 200) {
                                                                                        i60 = FrameworkStatsLog.CAMERA_ACTION_EVENT;
                                                                                        if (i55 >= 227) {
                                                                                            i60 = FrameworkStatsLog.CAMERA_ACTION_EVENT;
                                                                                            i59 = i77;
                                                                                            z14 = false;
                                                                                            if (!processStateRecord7.isCached()) {
                                                                                            }
                                                                                            if (i55 > i60) {
                                                                                            }
                                                                                            i79 = i51;
                                                                                            if (!connectionRecord3.notHasFlag(8388612)) {
                                                                                            }
                                                                                            if (i59 < 3) {
                                                                                            }
                                                                                            if (!z14) {
                                                                                            }
                                                                                            if (i79 > curRawProcState) {
                                                                                            }
                                                                                            if (i79 < 7) {
                                                                                            }
                                                                                            processRecord7 = processRecord;
                                                                                            if (str15 == null) {
                                                                                            }
                                                                                            z13 = true;
                                                                                        }
                                                                                    }
                                                                                    if (connectionRecord3.hasFlag(1073741824)) {
                                                                                        i61 = 200;
                                                                                        if (curRawAdj < 200) {
                                                                                            i60 = 200;
                                                                                        }
                                                                                    } else {
                                                                                        i61 = 200;
                                                                                    }
                                                                                    if (curRawAdj < i61) {
                                                                                        if (connectionRecord3.hasFlag(AudioFormat.EVRC)) {
                                                                                            i62 = 100;
                                                                                            if (curRawAdj <= 100 && i55 > 100) {
                                                                                                i60 = 100;
                                                                                                i59 = i77;
                                                                                                z14 = false;
                                                                                                if (!processStateRecord7.isCached()) {
                                                                                                }
                                                                                                if (i55 > i60) {
                                                                                                }
                                                                                                i79 = i51;
                                                                                                if (!connectionRecord3.notHasFlag(8388612)) {
                                                                                                }
                                                                                                if (i59 < 3) {
                                                                                                }
                                                                                                if (!z14) {
                                                                                                }
                                                                                                if (i79 > curRawProcState) {
                                                                                                }
                                                                                                if (i79 < 7) {
                                                                                                }
                                                                                                processRecord7 = processRecord;
                                                                                                if (str15 == null) {
                                                                                                }
                                                                                                z13 = true;
                                                                                            }
                                                                                        } else {
                                                                                            i62 = 100;
                                                                                        }
                                                                                        i60 = i55 > i62 ? Math.max(curRawAdj, i60) : i55;
                                                                                        i59 = i77;
                                                                                        z14 = false;
                                                                                        if (!processStateRecord7.isCached()) {
                                                                                        }
                                                                                        if (i55 > i60) {
                                                                                        }
                                                                                        i79 = i51;
                                                                                        if (!connectionRecord3.notHasFlag(8388612)) {
                                                                                        }
                                                                                        if (i59 < 3) {
                                                                                        }
                                                                                        if (!z14) {
                                                                                        }
                                                                                        if (i79 > curRawProcState) {
                                                                                        }
                                                                                        if (i79 < 7) {
                                                                                        }
                                                                                        processRecord7 = processRecord;
                                                                                        if (str15 == null) {
                                                                                        }
                                                                                        z13 = true;
                                                                                    }
                                                                                } else if (curRawAdj < -700) {
                                                                                    connectionRecord3.trackProcState(0, this.mAdjSeq);
                                                                                    i60 = ProcessList.PERSISTENT_SERVICE_ADJ;
                                                                                    z14 = true;
                                                                                    i59 = 2;
                                                                                    i51 = 0;
                                                                                    if (!processStateRecord7.isCached()) {
                                                                                        processStateRecord8.setCached(false);
                                                                                    }
                                                                                    if (i55 > i60) {
                                                                                        processStateRecord8.setCurRawAdj(i60);
                                                                                        str15 = HostingRecord.HOSTING_TYPE_SERVICE;
                                                                                        i55 = i60;
                                                                                    }
                                                                                    i79 = i51;
                                                                                    if (!connectionRecord3.notHasFlag(8388612)) {
                                                                                        int currentSchedulingGroup = processStateRecord7.getCurrentSchedulingGroup();
                                                                                        if (currentSchedulingGroup > i59) {
                                                                                            i59 = connectionRecord3.hasFlag(64) ? currentSchedulingGroup : 2;
                                                                                        }
                                                                                        if (curRawProcState < 2) {
                                                                                            if (connectionRecord3.hasFlag(AudioFormat.EVRC)) {
                                                                                                curRawProcState = 4;
                                                                                            } else {
                                                                                                curRawProcState = (connectionRecord3.hasFlag(67108864) || (this.mService.mWakefulness.get() == 1 && connectionRecord3.hasFlag(33554432))) ? 5 : 6;
                                                                                            }
                                                                                        } else if (curRawProcState == 2) {
                                                                                            if (processStateRecord7.getCachedCompatChange(0)) {
                                                                                                if (connectionRecord3.hasFlag(4096)) {
                                                                                                    curCapability = processStateRecord7.getCurCapability();
                                                                                                }
                                                                                                curRawProcState = 3;
                                                                                            } else {
                                                                                                curCapability = processStateRecord7.getCurCapability();
                                                                                            }
                                                                                            i58 |= curCapability;
                                                                                            curRawProcState = 3;
                                                                                        }
                                                                                    } else if (connectionRecord3.notHasFlag(AudioDevice.OUT_IP)) {
                                                                                        if (curRawProcState < 8) {
                                                                                            curRawProcState = 8;
                                                                                        }
                                                                                    } else if (curRawProcState < 7) {
                                                                                        curRawProcState = 7;
                                                                                    }
                                                                                    if (i59 < 3 && connectionRecord3.hasFlag(524288) && z20) {
                                                                                        i59 = 3;
                                                                                        z19 = true;
                                                                                    }
                                                                                    if (!z14) {
                                                                                        connectionRecord3.trackProcState(curRawProcState, this.mAdjSeq);
                                                                                    }
                                                                                    if (i79 > curRawProcState) {
                                                                                        processStateRecord8.setCurRawProcState(curRawProcState);
                                                                                        if (str15 == null) {
                                                                                            str15 = HostingRecord.HOSTING_TYPE_SERVICE;
                                                                                        }
                                                                                        i79 = curRawProcState;
                                                                                    }
                                                                                    if (i79 < 7 || !connectionRecord3.hasFlag(AudioFormat.APTX)) {
                                                                                        processRecord7 = processRecord;
                                                                                    } else {
                                                                                        processRecord7 = processRecord;
                                                                                        processRecord7.setPendingUiClean(true);
                                                                                    }
                                                                                    if (str15 == null) {
                                                                                        processStateRecord8.setAdjType(str15);
                                                                                        processStateRecord8.setAdjTypeCode(2);
                                                                                        Object obj2 = obj;
                                                                                        processStateRecord8.setAdjSource(obj2);
                                                                                        processStateRecord8.setAdjSourceProcState(curRawProcState);
                                                                                        processStateRecord8.setAdjTarget(runningServiceAt.instanceName);
                                                                                        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON) {
                                                                                            i56 = i50;
                                                                                            i57 = i44;
                                                                                        } else {
                                                                                            i56 = i50;
                                                                                            i57 = i44;
                                                                                            if (i56 != i57) {
                                                                                                str14 = str2;
                                                                                                str13 = str21;
                                                                                            }
                                                                                        }
                                                                                        String str22 = ActivityManagerService.TAG_OOM_ADJ;
                                                                                        StringBuilder sb5 = new StringBuilder();
                                                                                        str14 = str2;
                                                                                        sb5.append(str14);
                                                                                        sb5.append(str15);
                                                                                        str13 = str21;
                                                                                        sb5.append(str13);
                                                                                        sb5.append(processRecord7);
                                                                                        sb5.append(", due to ");
                                                                                        sb5.append(obj2);
                                                                                        sb5.append(" adj=");
                                                                                        sb5.append(i55);
                                                                                        sb5.append(" procState=");
                                                                                        sb5.append(ProcessList.makeProcStateString(i79));
                                                                                        reportOomAdjMessageLocked(str22, sb5.toString());
                                                                                    } else {
                                                                                        str14 = str2;
                                                                                        str13 = str21;
                                                                                        i56 = i50;
                                                                                        i57 = i44;
                                                                                    }
                                                                                    z13 = true;
                                                                                }
                                                                                i60 = curRawAdj;
                                                                                i59 = i77;
                                                                                z14 = false;
                                                                                if (!processStateRecord7.isCached()) {
                                                                                }
                                                                                if (i55 > i60) {
                                                                                }
                                                                                i79 = i51;
                                                                                if (!connectionRecord3.notHasFlag(8388612)) {
                                                                                }
                                                                                if (i59 < 3) {
                                                                                }
                                                                                if (!z14) {
                                                                                }
                                                                                if (i79 > curRawProcState) {
                                                                                }
                                                                                if (i79 < 7) {
                                                                                }
                                                                                processRecord7 = processRecord;
                                                                                if (str15 == null) {
                                                                                }
                                                                                z13 = true;
                                                                            } else if (i55 >= 900) {
                                                                                str15 = "cch-bound-ui-services";
                                                                                i79 = i51;
                                                                                i59 = i77;
                                                                                z14 = false;
                                                                                if (!connectionRecord3.notHasFlag(8388612)) {
                                                                                }
                                                                                if (i59 < 3) {
                                                                                }
                                                                                if (!z14) {
                                                                                }
                                                                                if (i79 > curRawProcState) {
                                                                                }
                                                                                if (i79 < 7) {
                                                                                }
                                                                                processRecord7 = processRecord;
                                                                                if (str15 == null) {
                                                                                }
                                                                                z13 = true;
                                                                            }
                                                                        }
                                                                        i79 = i51;
                                                                        i59 = i77;
                                                                        z14 = false;
                                                                        if (!connectionRecord3.notHasFlag(8388612)) {
                                                                        }
                                                                        if (i59 < 3) {
                                                                        }
                                                                        if (!z14) {
                                                                        }
                                                                        if (i79 > curRawProcState) {
                                                                        }
                                                                        if (i79 < 7) {
                                                                        }
                                                                        processRecord7 = processRecord;
                                                                        if (str15 == null) {
                                                                        }
                                                                        z13 = true;
                                                                    } else {
                                                                        processStateRecord8 = processStateRecord6;
                                                                        i55 = i54;
                                                                    }
                                                                    str15 = str12;
                                                                    if (i55 > curRawAdj) {
                                                                    }
                                                                    i79 = i51;
                                                                    i59 = i77;
                                                                    z14 = false;
                                                                    if (!connectionRecord3.notHasFlag(8388612)) {
                                                                    }
                                                                    if (i59 < 3) {
                                                                    }
                                                                    if (!z14) {
                                                                    }
                                                                    if (i79 > curRawProcState) {
                                                                    }
                                                                    if (i79 < 7) {
                                                                    }
                                                                    processRecord7 = processRecord;
                                                                    if (str15 == null) {
                                                                    }
                                                                    z13 = true;
                                                                }
                                                            }
                                                            processServiceRecord9 = processServiceRecord8;
                                                            if (connectionRecord3.hasFlag(AudioFormat.OPUS)) {
                                                                processServiceRecord9.setTreatLikeActivity(z13);
                                                            }
                                                            ActivityServiceConnectionsHolder<ConnectionRecord> activityServiceConnectionsHolder = connectionRecord3.activity;
                                                            if (connectionRecord3.hasFlag(128) || activityServiceConnectionsHolder == null || i55 <= 0 || !activityServiceConnectionsHolder.isActivityVisible()) {
                                                                i54 = i55;
                                                            } else {
                                                                processStateRecord8.setCurRawAdj(0);
                                                                if (connectionRecord3.notHasFlag(4)) {
                                                                    i59 = connectionRecord3.hasFlag(64) ? 4 : 2;
                                                                }
                                                                processStateRecord8.setCached(false);
                                                                processStateRecord8.setAdjType(HostingRecord.HOSTING_TYPE_SERVICE);
                                                                processStateRecord8.setAdjTypeCode(2);
                                                                processStateRecord8.setAdjSource(activityServiceConnectionsHolder);
                                                                processStateRecord8.setAdjSourceProcState(i79);
                                                                processStateRecord8.setAdjTarget(runningServiceAt.instanceName);
                                                                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i56 == i57) {
                                                                    reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise to service w/activity: " + processRecord7);
                                                                }
                                                                i54 = 0;
                                                            }
                                                            i77 = i59;
                                                            z11 = z21;
                                                            i41 = i58;
                                                            i43 = i53 + 1;
                                                            processServiceRecord7 = processServiceRecord9;
                                                            i66 = i57;
                                                            str2 = str14;
                                                            str19 = str13;
                                                            connections = arrayMap;
                                                            valueAt = arrayList;
                                                            numberOfRunningServices = i46;
                                                            i78 = i54;
                                                            i2 = i48;
                                                            i3 = i49;
                                                            i10 = i47;
                                                            j2 = j;
                                                            i76 = i56;
                                                            processStateRecord9 = processStateRecord8;
                                                            size = i45;
                                                        }
                                                    }
                                                    z12 = true;
                                                    boolean z212 = z11 | z12;
                                                    processRecord5 = processRecord4;
                                                    if (!processRecord5.mOptRecord.shouldNotFreeze()) {
                                                    }
                                                    int bfslCapabilityFromClient2 = i41 | getBfslCapabilityFromClient(processRecord5);
                                                    connectionRecord2 = connectionRecord;
                                                    if (connectionRecord2.notHasFlag(32)) {
                                                    }
                                                    processServiceRecord9 = processServiceRecord8;
                                                    if (connectionRecord3.hasFlag(AudioFormat.OPUS)) {
                                                    }
                                                    ActivityServiceConnectionsHolder<ConnectionRecord> activityServiceConnectionsHolder2 = connectionRecord3.activity;
                                                    if (connectionRecord3.hasFlag(128)) {
                                                    }
                                                    i54 = i55;
                                                    i77 = i59;
                                                    z11 = z212;
                                                    i41 = i58;
                                                    i43 = i53 + 1;
                                                    processServiceRecord7 = processServiceRecord9;
                                                    i66 = i57;
                                                    str2 = str14;
                                                    str19 = str13;
                                                    connections = arrayMap;
                                                    valueAt = arrayList;
                                                    numberOfRunningServices = i46;
                                                    i78 = i54;
                                                    i2 = i48;
                                                    i3 = i49;
                                                    i10 = i47;
                                                    j2 = j;
                                                    i76 = i56;
                                                    processStateRecord9 = processStateRecord8;
                                                    size = i45;
                                                }
                                                i43 = i53 + 1;
                                                processServiceRecord7 = processServiceRecord9;
                                                i66 = i57;
                                                str2 = str14;
                                                str19 = str13;
                                                connections = arrayMap;
                                                valueAt = arrayList;
                                                numberOfRunningServices = i46;
                                                i78 = i54;
                                                i2 = i48;
                                                i3 = i49;
                                                i10 = i47;
                                                j2 = j;
                                                i76 = i56;
                                                processStateRecord9 = processStateRecord8;
                                                size = i45;
                                            }
                                            int i81 = i78;
                                            int i82 = i2;
                                            int i83 = i79;
                                            processServiceRecord7 = processServiceRecord7;
                                            i67 = i76;
                                            i66 = i66;
                                            str2 = str2;
                                            str19 = str19;
                                            connections = connections;
                                            numberOfRunningServices = numberOfRunningServices;
                                            i15 = i77;
                                            z18 = z19;
                                            i3 = i3;
                                            i10 = i10;
                                            j2 = j;
                                            processStateRecord9 = processStateRecord9;
                                            size--;
                                            i70 = i81;
                                            i71 = i83;
                                            i2 = i82;
                                        }
                                        numberOfRunningServices--;
                                        processServiceRecord = processServiceRecord7;
                                        i67 = i67;
                                        i66 = i66;
                                        str2 = str2;
                                        str19 = str19;
                                        curCapability3 = i41;
                                        i2 = i2;
                                        i3 = i3;
                                        i10 = i10;
                                        j2 = j;
                                        processStateRecord9 = processStateRecord9;
                                        isCurBoundByNonBgRestrictedApp = z11;
                                    }
                                    i42 = 0;
                                    if (i70 > 500) {
                                    }
                                    i71 = i64;
                                    if (runningServiceAt.isForeground) {
                                    }
                                    connections = runningServiceAt.getConnections();
                                    size = connections.size() - 1;
                                    i15 = i73;
                                    while (size >= 0) {
                                        valueAt = connections.valueAt(size);
                                        int i762 = i67;
                                        boolean z192 = z18;
                                        int i772 = i15;
                                        int i782 = i70;
                                        int i792 = i71;
                                        i43 = 0;
                                        while (i43 < valueAt.size()) {
                                        }
                                        int i812 = i782;
                                        int i822 = i2;
                                        int i832 = i792;
                                        processServiceRecord7 = processServiceRecord7;
                                        i67 = i762;
                                        i66 = i66;
                                        str2 = str2;
                                        str19 = str19;
                                        connections = connections;
                                        numberOfRunningServices = numberOfRunningServices;
                                        i15 = i772;
                                        z18 = z192;
                                        i3 = i3;
                                        i10 = i10;
                                        j2 = j;
                                        processStateRecord9 = processStateRecord9;
                                        size--;
                                        i70 = i812;
                                        i71 = i832;
                                        i2 = i822;
                                    }
                                    numberOfRunningServices--;
                                    processServiceRecord = processServiceRecord7;
                                    i67 = i67;
                                    i66 = i66;
                                    str2 = str2;
                                    str19 = str19;
                                    curCapability3 = i41;
                                    i2 = i2;
                                    i3 = i3;
                                    i10 = i10;
                                    j2 = j;
                                    processStateRecord9 = processStateRecord9;
                                    isCurBoundByNonBgRestrictedApp = z11;
                                } else if (i70 > 500) {
                                    processStateRecord9.setAdjType("cch-started-ui-services");
                                }
                            }
                            i41 = curCapability3;
                            processServiceRecord7 = processServiceRecord;
                            i42 = 0;
                            if (runningServiceAt.isForeground) {
                            }
                            connections = runningServiceAt.getConnections();
                            size = connections.size() - 1;
                            i15 = i73;
                            while (size >= 0) {
                            }
                            numberOfRunningServices--;
                            processServiceRecord = processServiceRecord7;
                            i67 = i67;
                            i66 = i66;
                            str2 = str2;
                            str19 = str19;
                            curCapability3 = i41;
                            i2 = i2;
                            i3 = i3;
                            i10 = i10;
                            j2 = j;
                            processStateRecord9 = processStateRecord9;
                            isCurBoundByNonBgRestrictedApp = z11;
                        }
                        i16 = i15;
                        boolean z22 = isCurBoundByNonBgRestrictedApp;
                        int i84 = curCapability3;
                        processServiceRecord2 = processServiceRecord;
                        processStateRecord = processStateRecord9;
                        str3 = str19;
                        i17 = i10;
                        i18 = i2;
                        i19 = i3;
                        str4 = str2;
                        i20 = i67;
                        i21 = i66;
                        z6 = false;
                        processProviderRecord = processRecord7.mProviders;
                        numberOfProviders = processProviderRecord.numberOfProviders() - 1;
                        z7 = z22;
                        while (true) {
                            if (numberOfProviders < 0) {
                                processStateRecord2 = processStateRecord;
                                processServiceRecord3 = processServiceRecord2;
                                i22 = i20;
                                processProviderRecord2 = processProviderRecord;
                                z8 = z6;
                                i23 = i21;
                                break;
                            }
                            if (i70 <= 0 && i16 != 0 && i71 <= 2) {
                                processStateRecord2 = processStateRecord;
                                processServiceRecord3 = processServiceRecord2;
                                i22 = i20;
                                processProviderRecord2 = processProviderRecord;
                                z8 = z6;
                                i23 = i21;
                                break;
                            }
                            ContentProviderRecord providerAt = processProviderRecord.getProviderAt(numberOfProviders);
                            boolean z23 = z7;
                            int size2 = providerAt.connections.size() - 1;
                            int i85 = i71;
                            int i86 = i70;
                            int i87 = i16;
                            while (true) {
                                if (size2 < 0) {
                                    i27 = i85;
                                    processServiceRecord5 = processServiceRecord2;
                                    str5 = str4;
                                    processProviderRecord3 = processProviderRecord;
                                    i28 = numberOfProviders;
                                    i29 = i87;
                                    i30 = i20;
                                    str6 = str3;
                                    contentProviderRecord = providerAt;
                                    processStateRecord3 = processStateRecord;
                                    r5 = z6;
                                    i31 = i21;
                                    break;
                                }
                                if (i86 <= 0 && i87 != 0 && i85 <= 2) {
                                    i27 = i85;
                                    processServiceRecord5 = processServiceRecord2;
                                    i31 = i21;
                                    str5 = str4;
                                    processProviderRecord3 = processProviderRecord;
                                    i28 = numberOfProviders;
                                    i29 = i87;
                                    i30 = i20;
                                    str6 = str3;
                                    contentProviderRecord = providerAt;
                                    processStateRecord3 = processStateRecord;
                                    r5 = 0;
                                    break;
                                }
                                ContentProviderConnection contentProviderConnection2 = providerAt.connections.get(size2);
                                int i88 = i20;
                                ProcessRecord processRecord10 = contentProviderConnection2.client;
                                ProcessProviderRecord processProviderRecord4 = processProviderRecord;
                                ProcessStateRecord processStateRecord12 = processRecord10.mState;
                                if (processRecord10 == processRecord7) {
                                    i33 = i86;
                                    i34 = size2;
                                    i35 = i85;
                                    processStateRecord4 = processStateRecord;
                                    processServiceRecord6 = processServiceRecord2;
                                    i36 = i21;
                                    str7 = str4;
                                    str8 = str3;
                                    i37 = numberOfProviders;
                                    i38 = i88;
                                    i39 = i87;
                                    contentProviderRecord2 = providerAt;
                                } else {
                                    if (z3) {
                                        i37 = numberOfProviders;
                                        i39 = i87;
                                        i33 = i86;
                                        i34 = size2;
                                        contentProviderConnection = contentProviderConnection2;
                                        i35 = i85;
                                        processStateRecord4 = processStateRecord;
                                        processServiceRecord6 = processServiceRecord2;
                                        str8 = str3;
                                        contentProviderRecord2 = providerAt;
                                        i36 = i21;
                                        str7 = str4;
                                        i38 = i88;
                                        processRecord3 = processRecord10;
                                        computeOomAdjLSP(processRecord10, i, processRecord2, z, j, z2, true);
                                    } else {
                                        i33 = i86;
                                        i34 = size2;
                                        contentProviderConnection = contentProviderConnection2;
                                        i35 = i85;
                                        processStateRecord4 = processStateRecord;
                                        processServiceRecord6 = processServiceRecord2;
                                        i36 = i21;
                                        str7 = str4;
                                        str8 = str3;
                                        i37 = numberOfProviders;
                                        i38 = i88;
                                        i39 = i87;
                                        contentProviderRecord2 = providerAt;
                                        processRecord3 = processRecord10;
                                        processStateRecord12.setCurRawAdj(processStateRecord12.getCurAdj());
                                        processStateRecord12.setCurRawProcState(processStateRecord12.getCurProcState());
                                    }
                                    if (!shouldSkipDueToCycle(processRecord, processStateRecord12, i35, i33, z2)) {
                                        int curRawAdj2 = processStateRecord12.getCurRawAdj();
                                        int curRawProcState2 = processStateRecord12.getCurRawProcState();
                                        i84 |= getBfslCapabilityFromClient(processRecord3);
                                        if (curRawProcState2 >= 16) {
                                            curRawProcState2 = 19;
                                        }
                                        if (processRecord3.mOptRecord.shouldNotFreeze()) {
                                            processRecord7.mOptRecord.setShouldNotFreeze(true);
                                        }
                                        z23 |= processStateRecord12.isCurBoundByNonBgRestrictedApp() || curRawProcState2 <= 3 || (curRawProcState2 == 4 && !processStateRecord12.isBackgroundRestricted());
                                        i86 = i33;
                                        if (i86 > curRawAdj2) {
                                            if (processStateRecord4.hasShownUi() && !processStateRecord4.getCachedIsHomeProcess()) {
                                                if (curRawAdj2 > 200) {
                                                    str9 = "cch-ui-provider";
                                                    processStateRecord5 = processStateRecord4;
                                                    z10 = false;
                                                    processStateRecord5.setCached(processStateRecord5.isCached() & processStateRecord12.isCached());
                                                }
                                            }
                                            z10 = false;
                                            i86 = Math.max(curRawAdj2, 0);
                                            processStateRecord5 = processStateRecord4;
                                            processStateRecord5.setCurRawAdj(i86);
                                            str9 = "provider";
                                            processStateRecord5.setCached(processStateRecord5.isCached() & processStateRecord12.isCached());
                                        } else {
                                            processStateRecord5 = processStateRecord4;
                                            z10 = false;
                                            str9 = null;
                                        }
                                        if (curRawProcState2 <= 4) {
                                            if (str9 == null) {
                                                str9 = "provider";
                                            }
                                            curRawProcState2 = curRawProcState2 == 2 ? 3 : 5;
                                        }
                                        contentProviderConnection.trackProcState(curRawProcState2, this.mAdjSeq);
                                        int i89 = i35;
                                        if (i89 > curRawProcState2) {
                                            processStateRecord5.setCurRawProcState(curRawProcState2);
                                            i89 = curRawProcState2;
                                        }
                                        if (processStateRecord12.getCurrentSchedulingGroup() > i39) {
                                            i39 = 2;
                                        }
                                        if (str9 != null) {
                                            processStateRecord5.setAdjType(str9);
                                            processStateRecord5.setAdjTypeCode(1);
                                            processStateRecord5.setAdjSource(processRecord3);
                                            processStateRecord5.setAdjSourceProcState(curRawProcState2);
                                            processStateRecord5.setAdjTarget(contentProviderRecord2.name);
                                            if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON) {
                                                i40 = i36;
                                            } else {
                                                i40 = i36;
                                                if (i38 != i40) {
                                                    str10 = str7;
                                                    str11 = str8;
                                                }
                                            }
                                            String str23 = ActivityManagerService.TAG_OOM_ADJ;
                                            StringBuilder sb6 = new StringBuilder();
                                            str10 = str7;
                                            sb6.append(str10);
                                            sb6.append(str9);
                                            str11 = str8;
                                            sb6.append(str11);
                                            sb6.append(processRecord7);
                                            sb6.append(", due to ");
                                            sb6.append(processRecord3);
                                            sb6.append(" adj=");
                                            sb6.append(i86);
                                            sb6.append(" procState=");
                                            sb6.append(ProcessList.makeProcStateString(i89));
                                            reportOomAdjMessageLocked(str23, sb6.toString());
                                        } else {
                                            str10 = str7;
                                            str11 = str8;
                                            i40 = i36;
                                        }
                                        i35 = i89;
                                        size2 = i34 - 1;
                                        i21 = i40;
                                        z6 = z10;
                                        processStateRecord = processStateRecord5;
                                        i20 = i38;
                                        providerAt = contentProviderRecord2;
                                        processProviderRecord = processProviderRecord4;
                                        processServiceRecord2 = processServiceRecord6;
                                        str3 = str11;
                                        str4 = str10;
                                        i87 = i39;
                                        numberOfProviders = i37;
                                        i85 = i35;
                                    }
                                }
                                i86 = i33;
                                str10 = str7;
                                str11 = str8;
                                processStateRecord5 = processStateRecord4;
                                i40 = i36;
                                z10 = false;
                                size2 = i34 - 1;
                                i21 = i40;
                                z6 = z10;
                                processStateRecord = processStateRecord5;
                                i20 = i38;
                                providerAt = contentProviderRecord2;
                                processProviderRecord = processProviderRecord4;
                                processServiceRecord2 = processServiceRecord6;
                                str3 = str11;
                                str4 = str10;
                                i87 = i39;
                                numberOfProviders = i37;
                                i85 = i35;
                            }
                            if (contentProviderRecord.hasExternalProcessHandles()) {
                                if (i86 > 0) {
                                    processStateRecord3.setCurRawAdj(r5);
                                    processStateRecord3.setCached(r5);
                                    processStateRecord3.setAdjType("ext-provider");
                                    processStateRecord3.setAdjTarget(contentProviderRecord.name);
                                    if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i30 == i31) {
                                        reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise adj to external provider: " + processRecord7);
                                    }
                                    i86 = r5;
                                    i32 = 6;
                                    i29 = 2;
                                } else {
                                    i32 = 6;
                                }
                                if (i27 > i32) {
                                    processStateRecord3.setCurRawProcState(i32);
                                    if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i30 == i31) {
                                        reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to external provider: " + processRecord7);
                                    }
                                    i27 = i32;
                                }
                            }
                            i16 = i29;
                            numberOfProviders = i28 - 1;
                            str3 = str6;
                            i70 = i86;
                            i71 = i27;
                            i21 = i31;
                            z6 = r5;
                            processStateRecord = processStateRecord3;
                            i20 = i30;
                            z7 = z23;
                            processProviderRecord = processProviderRecord3;
                            processServiceRecord2 = processServiceRecord5;
                            str4 = str5;
                        }
                        if (processProviderRecord2.getLastProviderTime() > 0) {
                            int i90 = i22;
                            if (processProviderRecord2.getLastProviderTime() + this.mConstants.CONTENT_PROVIDER_RETAIN_TIME > j) {
                                if (i70 > 700) {
                                    processStateRecord2.setCached(z8);
                                    processStateRecord2.setAdjType("recent-provider");
                                    i26 = i90;
                                    if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i26 == i23) {
                                        reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise adj to recent provider: " + processRecord7);
                                    }
                                    i70 = 700;
                                    i16 = z8;
                                } else {
                                    i26 = i90;
                                }
                                if (i71 > 15) {
                                    processStateRecord2.setAdjType("recent-provider");
                                    if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i26 == i23) {
                                        reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to recent provider: " + processRecord7);
                                    }
                                    i24 = 15;
                                    if (i24 >= 19) {
                                        if (processServiceRecord3.hasClientActivities()) {
                                            processStateRecord2.setAdjType("cch-client-act");
                                            i24 = 17;
                                        } else if (processServiceRecord3.isTreatedLikeActivity()) {
                                            processStateRecord2.setAdjType("cch-as-act");
                                            i25 = 500;
                                            i24 = 16;
                                            if (i70 == i25) {
                                                if (z && !z2) {
                                                    processStateRecord2.setServiceB(this.mNewNumAServiceProcs > this.mNumServiceProcs / 3 ? true : z8);
                                                    this.mNewNumServiceProcs++;
                                                    if (!processStateRecord2.isServiceB()) {
                                                        if (!this.mService.mAppProfiler.isLastMemoryLevelNormal() && processRecord7.mProfile.getLastPss() >= this.mProcessList.getCachedRestoreThresholdKb()) {
                                                            processStateRecord2.setServiceHighRam(true);
                                                            processStateRecord2.setServiceB(true);
                                                        } else {
                                                            this.mNewNumAServiceProcs++;
                                                        }
                                                    } else {
                                                        processStateRecord2.setServiceHighRam(z8);
                                                    }
                                                }
                                                if (processStateRecord2.isServiceB()) {
                                                    i70 = ProcessList.SERVICE_B_ADJ;
                                                }
                                            }
                                            processStateRecord2.setCurRawAdj(i70);
                                            if (processRecord.getWrapper().getExtImpl().isRPLaunch() && i70 > 850) {
                                                processStateRecord2.setAdjType("preload_app");
                                                processStateRecord2.setEmpty(z8);
                                                processStateRecord2.setCached(z8);
                                                i70 = IProcessListWrapper.PRELOAD_APP_ADJ;
                                                i16 = z8;
                                                i24 = 10;
                                            }
                                            processServiceRecord4 = processServiceRecord3;
                                            int modifyRawOomAdj = processServiceRecord4.modifyRawOomAdj(i70);
                                            int i91 = (modifyRawOomAdj > processStateRecord2.getMaxAdj() || (modifyRawOomAdj = processStateRecord2.getMaxAdj()) > 250) ? i16 : 2;
                                            if (i24 >= 5) {
                                                z9 = true;
                                                if (this.mService.mWakefulness.get() != 1 && !z18 && i91 > 1) {
                                                    i91 = 1;
                                                }
                                            } else {
                                                z9 = true;
                                            }
                                            if (processServiceRecord4.hasForegroundServices()) {
                                                i84 |= i72;
                                            }
                                            int defaultCapability = i84 | getDefaultCapability(processRecord7, i24);
                                            if (i24 > 5) {
                                                defaultCapability &= -17;
                                            }
                                            processStateRecord2.setCurAdj(modifyRawOomAdj);
                                            processStateRecord2.setCurCapability(defaultCapability);
                                            processStateRecord2.setCurrentSchedulingGroup(i91);
                                            processStateRecord2.setCurProcState(i24);
                                            processStateRecord2.setCurRawProcState(i24);
                                            processStateRecord2.updateLastInvisibleTime(z16);
                                            processStateRecord2.setHasForegroundActivities(z15);
                                            processStateRecord2.setCompletedAdjSeq(this.mAdjSeq);
                                            processStateRecord2.setCurBoundByNonBgRestrictedApp(z7);
                                            mOomAdjusterExt.setImportantAppAdj(processRecord2, processRecord7);
                                            mOomAdjusterExt.bindSmallCore(processRecord2, processRecord7);
                                            return (processStateRecord2.getCurAdj() >= i19 || processStateRecord2.getCurProcState() < i18 || processStateRecord2.getCurCapability() != i17) ? z9 : z8;
                                        }
                                    }
                                    i25 = 500;
                                    if (i70 == i25) {
                                    }
                                    processStateRecord2.setCurRawAdj(i70);
                                    if (processRecord.getWrapper().getExtImpl().isRPLaunch()) {
                                        processStateRecord2.setAdjType("preload_app");
                                        processStateRecord2.setEmpty(z8);
                                        processStateRecord2.setCached(z8);
                                        i70 = IProcessListWrapper.PRELOAD_APP_ADJ;
                                        i16 = z8;
                                        i24 = 10;
                                    }
                                    processServiceRecord4 = processServiceRecord3;
                                    int modifyRawOomAdj2 = processServiceRecord4.modifyRawOomAdj(i70);
                                    if (modifyRawOomAdj2 > processStateRecord2.getMaxAdj()) {
                                    }
                                    if (i24 >= 5) {
                                    }
                                    if (processServiceRecord4.hasForegroundServices()) {
                                    }
                                    int defaultCapability2 = i84 | getDefaultCapability(processRecord7, i24);
                                    if (i24 > 5) {
                                    }
                                    processStateRecord2.setCurAdj(modifyRawOomAdj2);
                                    processStateRecord2.setCurCapability(defaultCapability2);
                                    processStateRecord2.setCurrentSchedulingGroup(i91);
                                    processStateRecord2.setCurProcState(i24);
                                    processStateRecord2.setCurRawProcState(i24);
                                    processStateRecord2.updateLastInvisibleTime(z16);
                                    processStateRecord2.setHasForegroundActivities(z15);
                                    processStateRecord2.setCompletedAdjSeq(this.mAdjSeq);
                                    processStateRecord2.setCurBoundByNonBgRestrictedApp(z7);
                                    mOomAdjusterExt.setImportantAppAdj(processRecord2, processRecord7);
                                    mOomAdjusterExt.bindSmallCore(processRecord2, processRecord7);
                                    if (processStateRecord2.getCurAdj() >= i19) {
                                    }
                                }
                            }
                        }
                        i24 = i71;
                        if (i24 >= 19) {
                        }
                        i25 = 500;
                        if (i70 == i25) {
                        }
                        processStateRecord2.setCurRawAdj(i70);
                        if (processRecord.getWrapper().getExtImpl().isRPLaunch()) {
                        }
                        processServiceRecord4 = processServiceRecord3;
                        int modifyRawOomAdj22 = processServiceRecord4.modifyRawOomAdj(i70);
                        if (modifyRawOomAdj22 > processStateRecord2.getMaxAdj()) {
                        }
                        if (i24 >= 5) {
                        }
                        if (processServiceRecord4.hasForegroundServices()) {
                        }
                        int defaultCapability22 = i84 | getDefaultCapability(processRecord7, i24);
                        if (i24 > 5) {
                        }
                        processStateRecord2.setCurAdj(modifyRawOomAdj22);
                        processStateRecord2.setCurCapability(defaultCapability22);
                        processStateRecord2.setCurrentSchedulingGroup(i91);
                        processStateRecord2.setCurProcState(i24);
                        processStateRecord2.setCurRawProcState(i24);
                        processStateRecord2.updateLastInvisibleTime(z16);
                        processStateRecord2.setHasForegroundActivities(z15);
                        processStateRecord2.setCompletedAdjSeq(this.mAdjSeq);
                        processStateRecord2.setCurBoundByNonBgRestrictedApp(z7);
                        mOomAdjusterExt.setImportantAppAdj(processRecord2, processRecord7);
                        mOomAdjusterExt.bindSmallCore(processRecord2, processRecord7);
                        if (processStateRecord2.getCurAdj() >= i19) {
                        }
                    }
                    if (i70 > 700) {
                        processStateRecord9.setCached(false);
                        processStateRecord9.setAdjType("previous");
                        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                            reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise adj to prev: " + processRecord7);
                        }
                        i70 = ProcessList.PREVIOUS_APP_ADJ;
                        i6 = 0;
                    }
                    if (i71 > 15) {
                        processStateRecord9.setAdjType("previous");
                        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                            reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to prev: " + processRecord7);
                        }
                        i71 = 15;
                    }
                }
                i15 = i6;
                if (z2) {
                }
                processStateRecord9.setCurRawAdj(i70);
                processStateRecord9.setCurRawProcState(i71);
                processStateRecord9.setHasStartedServices(false);
                processStateRecord9.setAdjSeq(this.mAdjSeq);
                backupRecord = this.mService.mBackupTargets.get(processRecord7.userId);
                if (backupRecord != null) {
                    if (i70 > 300) {
                    }
                    if (i71 > 9) {
                    }
                }
                isCurBoundByNonBgRestrictedApp = processStateRecord9.isCurBoundByNonBgRestrictedApp();
                numberOfRunningServices = processServiceRecord.numberOfRunningServices() - 1;
                int i722 = i14;
                boolean z182 = false;
                while (numberOfRunningServices >= 0) {
                    runningServiceAt = processServiceRecord.getRunningServiceAt(numberOfRunningServices);
                    int i732 = i15;
                    if (runningServiceAt.startRequested) {
                    }
                    i41 = curCapability3;
                    processServiceRecord7 = processServiceRecord;
                    i42 = 0;
                    if (runningServiceAt.isForeground) {
                    }
                    connections = runningServiceAt.getConnections();
                    size = connections.size() - 1;
                    i15 = i732;
                    while (size >= 0) {
                    }
                    numberOfRunningServices--;
                    processServiceRecord = processServiceRecord7;
                    i67 = i67;
                    i66 = i66;
                    str2 = str2;
                    str19 = str19;
                    curCapability3 = i41;
                    i2 = i2;
                    i3 = i3;
                    i10 = i10;
                    j2 = j;
                    processStateRecord9 = processStateRecord9;
                    isCurBoundByNonBgRestrictedApp = z11;
                }
                i16 = i15;
                boolean z222 = isCurBoundByNonBgRestrictedApp;
                int i842 = curCapability3;
                processServiceRecord2 = processServiceRecord;
                processStateRecord = processStateRecord9;
                str3 = str19;
                i17 = i10;
                i18 = i2;
                i19 = i3;
                str4 = str2;
                i20 = i67;
                i21 = i66;
                z6 = false;
                processProviderRecord = processRecord7.mProviders;
                numberOfProviders = processProviderRecord.numberOfProviders() - 1;
                z7 = z222;
                while (true) {
                    if (numberOfProviders < 0) {
                    }
                    i16 = i29;
                    numberOfProviders = i28 - 1;
                    str3 = str6;
                    i70 = i86;
                    i71 = i27;
                    i21 = i31;
                    z6 = r5;
                    processStateRecord = processStateRecord3;
                    i20 = i30;
                    z7 = z23;
                    processProviderRecord = processProviderRecord3;
                    processServiceRecord2 = processServiceRecord5;
                    str4 = str5;
                }
                if (processProviderRecord2.getLastProviderTime() > 0) {
                }
                i24 = i71;
                if (i24 >= 19) {
                }
                i25 = 500;
                if (i70 == i25) {
                }
                processStateRecord2.setCurRawAdj(i70);
                if (processRecord.getWrapper().getExtImpl().isRPLaunch()) {
                }
                processServiceRecord4 = processServiceRecord3;
                int modifyRawOomAdj222 = processServiceRecord4.modifyRawOomAdj(i70);
                if (modifyRawOomAdj222 > processStateRecord2.getMaxAdj()) {
                }
                if (i24 >= 5) {
                }
                if (processServiceRecord4.hasForegroundServices()) {
                }
                int defaultCapability222 = i842 | getDefaultCapability(processRecord7, i24);
                if (i24 > 5) {
                }
                processStateRecord2.setCurAdj(modifyRawOomAdj222);
                processStateRecord2.setCurCapability(defaultCapability222);
                processStateRecord2.setCurrentSchedulingGroup(i91);
                processStateRecord2.setCurProcState(i24);
                processStateRecord2.setCurRawProcState(i24);
                processStateRecord2.updateLastInvisibleTime(z16);
                processStateRecord2.setHasForegroundActivities(z15);
                processStateRecord2.setCompletedAdjSeq(this.mAdjSeq);
                processStateRecord2.setCurBoundByNonBgRestrictedApp(z7);
                mOomAdjusterExt.setImportantAppAdj(processRecord2, processRecord7);
                mOomAdjusterExt.bindSmallCore(processRecord2, processRecord7);
                if (processStateRecord2.getCurAdj() >= i19) {
                }
            }
        } else {
            i11 = 4;
        }
        if (hasForegroundServices && hasNonShortForegroundServices) {
            str = "fg-service";
            i13 = i11;
            i12 = 200;
            i14 = 16;
        } else {
            if (z17) {
                str = "fg-service-short";
                i12 = 226;
                i13 = i11;
            } else if (processStateRecord9.hasOverlayUi()) {
                str = "has-overlay-ui";
                i12 = 200;
                i13 = 6;
            } else {
                str = null;
                i12 = 0;
                i13 = 0;
            }
            i14 = 0;
        }
        if (str != null) {
            processStateRecord9.setAdjType(str);
            processStateRecord9.setCached(false);
            if (processStateRecord9.hasOverlayUi() || ("fg-service".equals(str) && !mOomAdjusterExt.skipSetSchedGroup(processRecord7))) {
                i6 = 2;
            }
            if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || i67 == i66) {
                reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise to " + str + ": " + processRecord7 + " ");
            }
            i70 = i12;
            i71 = i13;
        }
        if (processServiceRecord.hasForegroundServices()) {
        }
        str2 = "Raise to ";
        if (processServiceRecord.hasTopStartedAlmostPerceptibleServices()) {
            processStateRecord9.setAdjType("top-ej-act");
            if (!ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON) {
            }
            reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise to recent fg for EJ: " + processRecord7);
            i70 = 52;
        }
        if (i70 <= 200) {
        }
        processStateRecord9.setCached(false);
        processStateRecord9.setAdjType("force-imp");
        processStateRecord9.setAdjSource(processStateRecord9.getForcingToImportant());
        if (!ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON) {
        }
        reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Raise to force imp: " + processRecord7);
        i71 = 8;
        i70 = 200;
        i6 = 2;
        if (processStateRecord9.getCachedIsHeavyWeight()) {
        }
        if (processStateRecord9.getCachedIsHomeProcess()) {
        }
        if (processStateRecord9.getCachedIsPreviousProcess()) {
            if (i71 < 15) {
            }
            if (i70 > 700) {
            }
            if (i71 > 15) {
            }
        }
        i15 = i6;
        if (z2) {
        }
        processStateRecord9.setCurRawAdj(i70);
        processStateRecord9.setCurRawProcState(i71);
        processStateRecord9.setHasStartedServices(false);
        processStateRecord9.setAdjSeq(this.mAdjSeq);
        backupRecord = this.mService.mBackupTargets.get(processRecord7.userId);
        if (backupRecord != null) {
        }
        isCurBoundByNonBgRestrictedApp = processStateRecord9.isCurBoundByNonBgRestrictedApp();
        numberOfRunningServices = processServiceRecord.numberOfRunningServices() - 1;
        int i7222 = i14;
        boolean z1822 = false;
        while (numberOfRunningServices >= 0) {
        }
        i16 = i15;
        boolean z2222 = isCurBoundByNonBgRestrictedApp;
        int i8422 = curCapability3;
        processServiceRecord2 = processServiceRecord;
        processStateRecord = processStateRecord9;
        str3 = str19;
        i17 = i10;
        i18 = i2;
        i19 = i3;
        str4 = str2;
        i20 = i67;
        i21 = i66;
        z6 = false;
        processProviderRecord = processRecord7.mProviders;
        numberOfProviders = processProviderRecord.numberOfProviders() - 1;
        z7 = z2222;
        while (true) {
            if (numberOfProviders < 0) {
            }
            i16 = i29;
            numberOfProviders = i28 - 1;
            str3 = str6;
            i70 = i86;
            i71 = i27;
            i21 = i31;
            z6 = r5;
            processStateRecord = processStateRecord3;
            i20 = i30;
            z7 = z23;
            processProviderRecord = processProviderRecord3;
            processServiceRecord2 = processServiceRecord5;
            str4 = str5;
        }
        if (processProviderRecord2.getLastProviderTime() > 0) {
        }
        i24 = i71;
        if (i24 >= 19) {
        }
        i25 = 500;
        if (i70 == i25) {
        }
        processStateRecord2.setCurRawAdj(i70);
        if (processRecord.getWrapper().getExtImpl().isRPLaunch()) {
        }
        processServiceRecord4 = processServiceRecord3;
        int modifyRawOomAdj2222 = processServiceRecord4.modifyRawOomAdj(i70);
        if (modifyRawOomAdj2222 > processStateRecord2.getMaxAdj()) {
        }
        if (i24 >= 5) {
        }
        if (processServiceRecord4.hasForegroundServices()) {
        }
        int defaultCapability2222 = i8422 | getDefaultCapability(processRecord7, i24);
        if (i24 > 5) {
        }
        processStateRecord2.setCurAdj(modifyRawOomAdj2222);
        processStateRecord2.setCurCapability(defaultCapability2222);
        processStateRecord2.setCurrentSchedulingGroup(i91);
        processStateRecord2.setCurProcState(i24);
        processStateRecord2.setCurRawProcState(i24);
        processStateRecord2.updateLastInvisibleTime(z16);
        processStateRecord2.setHasForegroundActivities(z15);
        processStateRecord2.setCompletedAdjSeq(this.mAdjSeq);
        processStateRecord2.setCurBoundByNonBgRestrictedApp(z7);
        mOomAdjusterExt.setImportantAppAdj(processRecord2, processRecord7);
        mOomAdjusterExt.bindSmallCore(processRecord2, processRecord7);
        if (processStateRecord2.getCurAdj() >= i19) {
        }
    }

    private int getDefaultCapability(ProcessRecord processRecord, int i) {
        int i2;
        int defaultProcessNetworkCapabilities = NetworkPolicyManager.getDefaultProcessNetworkCapabilities(i);
        if (i == 0 || i == 1 || i == 2) {
            i2 = 63;
        } else if (i != 3) {
            i2 = 0;
            if (i == 4 && (processRecord.getActiveInstrumentation() != null || (!processRecord.mServices.hasForegroundServices() && !CompatChanges.isChangeEnabled(254662522L, processRecord.info.uid)))) {
                i2 = 6;
            }
        } else {
            i2 = 16;
        }
        return defaultProcessNetworkCapabilities | i2;
    }

    int getBfslCapabilityFromClient(ProcessRecord processRecord) {
        if (processRecord.mState.getCurProcState() < 4) {
            return 16;
        }
        return processRecord.mState.getCurCapability() & 16;
    }

    private boolean shouldSkipDueToCycle(ProcessRecord processRecord, ProcessStateRecord processStateRecord, int i, int i2, boolean z) {
        if (!processStateRecord.containsCycle()) {
            return false;
        }
        processRecord.mState.setContainsCycle(true);
        this.mProcessesInCycle.add(processRecord);
        if (processStateRecord.getCompletedAdjSeq() < this.mAdjSeq) {
            return !z || (processStateRecord.getCurRawProcState() >= i && processStateRecord.getCurRawAdj() >= i2);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mService"})
    public void reportOomAdjMessageLocked(String str, String str2) {
        Slog.d(str, str2);
        synchronized (this.mService.mOomAdjObserverLock) {
            ActivityManagerService activityManagerService = this.mService;
            if (activityManagerService.mCurOomAdjObserver != null) {
                activityManagerService.mUiHandler.obtainMessage(70, str2).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onWakefulnessChanged(int i) {
        this.mCachedAppOptimizer.onWakefulnessChanged(i);
    }

    /* JADX WARN: Removed duplicated region for block: B:148:0x02cf  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x028a  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x027f  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x029a  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x031d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @GuardedBy({"mService", "mProcLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean applyOomAdjLSP(final ProcessRecord processRecord, boolean z, long j, long j2, int i) {
        boolean z2;
        int i2;
        int i3;
        boolean z3;
        int i4;
        long j3;
        long j4;
        ProcessStateRecord processStateRecord = processRecord.mState;
        UidRecord uidRecord = processRecord.getUidRecord();
        if (processStateRecord.getCurRawAdj() != processStateRecord.getSetRawAdj()) {
            processStateRecord.setSetRawAdj(processStateRecord.getCurRawAdj());
        }
        if (processStateRecord.getCurAdj() != processStateRecord.getSetAdj()) {
            this.mCachedAppOptimizer.onOomAdjustChanged(processStateRecord.getSetAdj(), processStateRecord.getCurAdj(), processRecord);
        }
        if (processStateRecord.getCurAdj() != processStateRecord.getSetAdj()) {
            this.mSocExt.backgroundAppsTransition(processRecord, processStateRecord);
            ProcessList.setOomAdj(processRecord.getPid(), processRecord.uid, processRecord.mState.getCurAdj());
            if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH || ActivityManagerDebugConfig.DEBUG_OOM_ADJ || this.mService.mCurOomAdjUid == processRecord.info.uid) {
                reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Set " + processRecord.getPid() + " " + processRecord.processName + " adj " + processStateRecord.getCurAdj() + ": " + processStateRecord.getAdjType());
            }
            processStateRecord.setSetAdj(processStateRecord.getCurAdj());
            if (uidRecord != null) {
                uidRecord.noteProcAdjChanged();
            }
            processStateRecord.setVerifiedAdj(-10000);
        }
        int currentSchedulingGroup = processStateRecord.getCurrentSchedulingGroup();
        boolean z4 = false;
        if (processStateRecord.getSetSchedGroup() != currentSchedulingGroup) {
            int setSchedGroup = processStateRecord.getSetSchedGroup();
            processStateRecord.setSetSchedGroup(currentSchedulingGroup);
            if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH || ActivityManagerDebugConfig.DEBUG_OOM_ADJ || this.mService.mCurOomAdjUid == processRecord.uid) {
                reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Setting sched group of " + processRecord.processName + " to " + currentSchedulingGroup + ": " + processStateRecord.getAdjType());
            }
            if (processRecord.getWaitingToKill() != null && processRecord.mReceivers.numberOfCurReceivers() == 0 && ActivityManager.isProcStateBackground(processStateRecord.getSetProcState())) {
                processRecord.killLocked(processRecord.getWaitingToKill(), 10, 22, true);
                z2 = false;
                mOomAdjusterExt.adjustUxProcess(processRecord, currentSchedulingGroup, processStateRecord.getCurProcState());
                if (processStateRecord.hasRepForegroundActivities() == processStateRecord.hasForegroundActivities()) {
                    processStateRecord.setRepForegroundActivities(processStateRecord.hasForegroundActivities());
                    i2 = i;
                    i3 = 1;
                } else {
                    i2 = i;
                    i3 = 0;
                }
                updateAppFreezeStateLSP(processRecord, i2);
                if (processStateRecord.getReportedProcState() != processStateRecord.getCurProcState()) {
                    processStateRecord.setReportedProcState(processStateRecord.getCurProcState());
                    if (processRecord.getThread() != null) {
                        try {
                            processRecord.getThread().setProcessState(processStateRecord.getReportedProcState());
                        } catch (RemoteException unused) {
                        }
                    }
                }
                if (processStateRecord.getSetProcState() != 20 || ProcessList.procStatesDifferForMem(processStateRecord.getCurProcState(), processStateRecord.getSetProcState())) {
                    processStateRecord.setLastStateTime(j);
                    if (ActivityManagerDebugConfig.DEBUG_PSS) {
                        Slog.d(AppProfiler.TAG_PSS, "Process state change from " + ProcessList.makeProcStateString(processStateRecord.getSetProcState()) + " to " + ProcessList.makeProcStateString(processStateRecord.getCurProcState()) + " next pss in " + (processRecord.mProfile.getNextPssTime() - j) + ": " + processRecord);
                    }
                    z4 = true;
                }
                synchronized (this.mService.mAppProfiler.mProfilerLock) {
                    processRecord.mProfile.updateProcState(processRecord.mState);
                    z3 = z2;
                    i4 = i3;
                    this.mService.mAppProfiler.updateNextPssTimeLPf(processStateRecord.getCurProcState(), processRecord.mProfile, j, z4);
                }
                if (processStateRecord.getSetProcState() != processStateRecord.getCurProcState()) {
                    if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH || ActivityManagerDebugConfig.DEBUG_OOM_ADJ || this.mService.mCurOomAdjUid == processRecord.uid) {
                        reportOomAdjMessageLocked(ActivityManagerService.TAG_OOM_ADJ, "Proc state change of " + processRecord.processName + " to " + ProcessList.makeProcStateString(processStateRecord.getCurProcState()) + " (" + processStateRecord.getCurProcState() + "): " + processStateRecord.getAdjType());
                    }
                    boolean z5 = processStateRecord.getSetProcState() < 10;
                    boolean z6 = processStateRecord.getCurProcState() < 10;
                    if (z5 && !z6) {
                        processStateRecord.setWhenUnimportant(j);
                        processRecord.mProfile.mLastCpuTime.set(0L);
                    }
                    if (z6 && !z5) {
                        mOomAdjusterExt.handleImportantChanged(processRecord, processStateRecord.getCurProcState(), processStateRecord.getSetProcState());
                    }
                    maybeUpdateUsageStatsLSP(processRecord, j2);
                    maybeUpdateLastTopTime(processStateRecord, j);
                    processStateRecord.setSetProcState(processStateRecord.getCurProcState());
                    if (processStateRecord.getSetProcState() >= 14) {
                        processStateRecord.setNotCachedSinceIdle(false);
                    }
                    if (!z) {
                        synchronized (this.mService.mProcessStats.mLock) {
                            ActivityManagerService activityManagerService = this.mService;
                            activityManagerService.setProcessTrackerStateLOSP(processRecord, activityManagerService.mProcessStats.getMemFactorLocked());
                        }
                    } else {
                        processStateRecord.setProcStateChanged(true);
                    }
                } else if (processStateRecord.hasReportedInteraction()) {
                    if (processStateRecord.getCachedCompatChange(2)) {
                        j4 = this.mConstants.USAGE_STATS_INTERACTION_INTERVAL_POST_S;
                    } else {
                        j4 = this.mConstants.USAGE_STATS_INTERACTION_INTERVAL_PRE_S;
                    }
                    if (j2 - processStateRecord.getInteractionEventTime() > j4) {
                        maybeUpdateUsageStatsLSP(processRecord, j2);
                    }
                } else {
                    if (processStateRecord.getCachedCompatChange(2)) {
                        j3 = this.mConstants.SERVICE_USAGE_INTERACTION_TIME_POST_S;
                    } else {
                        j3 = this.mConstants.SERVICE_USAGE_INTERACTION_TIME_PRE_S;
                    }
                    if (j2 - processStateRecord.getFgInteractionTime() > j3) {
                        maybeUpdateUsageStatsLSP(processRecord, j2);
                    }
                }
                if (processStateRecord.getCurCapability() != processStateRecord.getSetCapability()) {
                    processStateRecord.setSetCapability(processStateRecord.getCurCapability());
                }
                boolean isCurBoundByNonBgRestrictedApp = processStateRecord.isCurBoundByNonBgRestrictedApp();
                if (isCurBoundByNonBgRestrictedApp != processStateRecord.isSetBoundByNonBgRestrictedApp()) {
                    processStateRecord.setSetBoundByNonBgRestrictedApp(isCurBoundByNonBgRestrictedApp);
                    if (!isCurBoundByNonBgRestrictedApp && processStateRecord.isBackgroundRestricted()) {
                        this.mService.mHandler.post(new Runnable() { // from class: com.android.server.am.OomAdjuster$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                OomAdjuster.this.lambda$applyOomAdjLSP$1(processRecord);
                            }
                        });
                    }
                }
                if (i4 != 0) {
                    if (ActivityManagerDebugConfig.DEBUG_PROCESS_OBSERVERS) {
                        Slog.i(ProcessList.TAG_PROCESS_OBSERVERS, "Changes in " + processRecord + ": " + i4);
                    }
                    ActivityManagerService.ProcessChangeItem enqueueProcessChangeItemLocked = this.mProcessList.enqueueProcessChangeItemLocked(processRecord.getPid(), processRecord.info.uid);
                    enqueueProcessChangeItemLocked.changes = i4 | enqueueProcessChangeItemLocked.changes;
                    enqueueProcessChangeItemLocked.foregroundActivities = processStateRecord.hasRepForegroundActivities();
                    if (ActivityManagerDebugConfig.DEBUG_PROCESS_OBSERVERS) {
                        Slog.i(ProcessList.TAG_PROCESS_OBSERVERS, "Item " + Integer.toHexString(System.identityHashCode(enqueueProcessChangeItemLocked)) + " " + processRecord.toShortString() + ": changes=" + enqueueProcessChangeItemLocked.changes + " foreground=" + enqueueProcessChangeItemLocked.foregroundActivities + " type=" + processStateRecord.getAdjType() + " source=" + processStateRecord.getAdjSource() + " target=" + processStateRecord.getAdjTarget());
                    }
                }
                if (processStateRecord.isCached() && !processStateRecord.shouldNotKillOnBgRestrictedAndIdle() && (!processStateRecord.isSetCached() || processStateRecord.isSetNoKillOnBgRestrictedAndIdle())) {
                    processStateRecord.setLastCanKillOnBgRestrictedAndIdleTime(j2);
                    if (this.mService.mDeterministicUidIdle || !this.mService.mHandler.hasMessages(58)) {
                        this.mService.mHandler.sendEmptyMessageDelayed(58, this.mConstants.mKillBgRestrictedAndCachedIdleSettleTimeMs);
                    }
                }
                processStateRecord.setSetCached(processStateRecord.isCached());
                processStateRecord.setSetNoKillOnBgRestrictedAndIdle(processStateRecord.shouldNotKillOnBgRestrictedAndIdle());
                return z3;
            }
            int i5 = currentSchedulingGroup != 0 ? currentSchedulingGroup != 1 ? (currentSchedulingGroup == 3 || currentSchedulingGroup == 4) ? 5 : -1 : 7 : 0;
            mOomAdjusterExt.setProcRecdOldSchedGroup(processRecord, setSchedGroup);
            Handler handler = this.mProcessGroupHandler;
            handler.sendMessage(handler.obtainMessage(0, processRecord.getPid(), i5, processRecord));
            try {
                int renderThreadTid = processRecord.getRenderThreadTid();
                if (currentSchedulingGroup == 3) {
                    if (setSchedGroup != 3) {
                        processRecord.getWindowProcessController().onTopProcChanged();
                        if (this.mService.mUseFifoUiScheduling) {
                            processStateRecord.setSavedPriority(Process.getThreadPriority(processRecord.getPid()));
                            ActivityManagerService.scheduleAsFifoPriority(processRecord.getPid(), true);
                            if (renderThreadTid != 0) {
                                ActivityManagerService.scheduleAsFifoPriority(renderThreadTid, true);
                                if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                                    Slog.d("UI_FIFO", "Set RenderThread (TID " + renderThreadTid + ") to FIFO");
                                }
                            } else if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                                Slog.d("UI_FIFO", "Not setting RenderThread TID");
                            }
                        } else {
                            Process.setThreadPriority(processRecord.getPid(), -10);
                            if (renderThreadTid != 0) {
                                try {
                                    Process.setThreadPriority(renderThreadTid, -10);
                                } catch (IllegalArgumentException unused2) {
                                }
                            }
                        }
                        ProcessRecord topAppOnlyLocked = this.mService.getTopAppOnlyLocked();
                        if (topAppOnlyLocked != null) {
                            mOomAdjusterExt.adjustTopApp(topAppOnlyLocked.info.packageName, topAppOnlyLocked.mPid, topAppOnlyLocked.getRenderThreadTid(), topAppOnlyLocked.hwuiTaskThreads);
                        }
                    }
                } else if (setSchedGroup == 3 && currentSchedulingGroup != 3) {
                    processRecord.getWindowProcessController().onTopProcChanged();
                    if (this.mService.mUseFifoUiScheduling) {
                        try {
                            Process.setThreadScheduler(processRecord.getPid(), 0, 0);
                            Process.setThreadPriority(processRecord.getPid(), processStateRecord.getSavedPriority());
                            if (renderThreadTid != 0) {
                                Process.setThreadScheduler(renderThreadTid, 0, 0);
                            }
                        } catch (IllegalArgumentException e) {
                            Slog.w(TAG, "Failed to set scheduling policy, thread does not exist:\n" + e);
                        } catch (SecurityException e2) {
                            Slog.w(TAG, "Failed to set scheduling policy, not allowed:\n" + e2);
                        }
                    } else {
                        Process.setThreadPriority(processRecord.getPid(), 0);
                    }
                    if (renderThreadTid != 0) {
                        Process.setThreadPriority(renderThreadTid, -4);
                    }
                    ProcessRecord topAppOnlyLocked2 = this.mService.getTopAppOnlyLocked();
                    if (topAppOnlyLocked2 != null) {
                        mOomAdjusterExt.adjustTopApp(topAppOnlyLocked2.info.packageName, topAppOnlyLocked2.mPid, topAppOnlyLocked2.getRenderThreadTid(), topAppOnlyLocked2.hwuiTaskThreads);
                    }
                }
            } catch (Exception e3) {
                if (ActivityManagerDebugConfig.DEBUG_ALL) {
                    Slog.w(TAG, "Failed setting thread priority of " + processRecord.getPid(), e3);
                }
            }
        }
        z2 = true;
        mOomAdjusterExt.adjustUxProcess(processRecord, currentSchedulingGroup, processStateRecord.getCurProcState());
        if (processStateRecord.hasRepForegroundActivities() == processStateRecord.hasForegroundActivities()) {
        }
        updateAppFreezeStateLSP(processRecord, i2);
        if (processStateRecord.getReportedProcState() != processStateRecord.getCurProcState()) {
        }
        if (processStateRecord.getSetProcState() != 20) {
        }
        processStateRecord.setLastStateTime(j);
        if (ActivityManagerDebugConfig.DEBUG_PSS) {
        }
        z4 = true;
        synchronized (this.mService.mAppProfiler.mProfilerLock) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyOomAdjLSP$1(ProcessRecord processRecord) {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mService.mServices.stopAllForegroundServicesLocked(processRecord.uid, processRecord.info.packageName);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setAttachingSchedGroupLSP(ProcessRecord processRecord) {
        int i;
        ProcessStateRecord processStateRecord = processRecord.mState;
        if (processStateRecord.hasForegroundActivities()) {
            try {
                processRecord.getWindowProcessController().onTopProcChanged();
                if (this.mService.mUseFifoUiScheduling) {
                    ActivityManagerService.scheduleAsFifoPriority(processRecord.getPid(), true);
                } else {
                    Process.setThreadPriority(processRecord.getPid(), -10);
                }
                mOomAdjusterExt.onHookadjustUxProcess(processRecord, processRecord.getRenderThreadTid(), 1, false);
                ProcessRecord topAppOnlyLocked = this.mService.getTopAppOnlyLocked();
                if (topAppOnlyLocked != null) {
                    mOomAdjusterExt.adjustTopApp(topAppOnlyLocked.info.packageName, topAppOnlyLocked.mPid, topAppOnlyLocked.getRenderThreadTid(), topAppOnlyLocked.hwuiTaskThreads);
                }
                i = 3;
            } catch (Exception e) {
                Slog.w(TAG, "Failed to pre-set top priority to " + processRecord + " " + e);
            }
            processStateRecord.setSetSchedGroup(i);
            processStateRecord.setCurrentSchedulingGroup(i);
        }
        i = 2;
        processStateRecord.setSetSchedGroup(i);
        processStateRecord.setCurrentSchedulingGroup(i);
    }

    @VisibleForTesting
    void maybeUpdateUsageStats(ProcessRecord processRecord, long j) {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                ActivityManagerService.boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        maybeUpdateUsageStatsLSP(processRecord, j);
                    } catch (Throwable th) {
                        ActivityManagerService.resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                }
                ActivityManagerService.resetPriorityAfterProcLockedSection();
            } catch (Throwable th2) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x0081, code lost:
    
        if (r14 > (r0.getFgInteractionTime() + r8)) goto L28;
     */
    @GuardedBy({"mService", "mProcLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void maybeUpdateUsageStatsLSP(ProcessRecord processRecord, long j) {
        long j2;
        long j3;
        ProcessStateRecord processStateRecord = processRecord.mState;
        if (ActivityManagerDebugConfig.DEBUG_USAGE_STATS) {
            Slog.d(TAG, "Checking proc [" + Arrays.toString(processRecord.getPackageList()) + "] state changes: old = " + processStateRecord.getSetProcState() + ", new = " + processStateRecord.getCurProcState());
        }
        if (this.mService.mUsageStatsService == null) {
            return;
        }
        boolean cachedCompatChange = processStateRecord.getCachedCompatChange(2);
        if (ActivityManager.isProcStateConsideredInteraction(processStateRecord.getCurProcState())) {
            processStateRecord.setFgInteractionTime(0L);
        } else if (processStateRecord.getCurProcState() <= 4) {
            if (processStateRecord.getFgInteractionTime() == 0) {
                processStateRecord.setFgInteractionTime(j);
            } else if (cachedCompatChange) {
                j2 = this.mConstants.SERVICE_USAGE_INTERACTION_TIME_POST_S;
            } else {
                j2 = this.mConstants.SERVICE_USAGE_INTERACTION_TIME_PRE_S;
            }
            r7 = false;
        } else {
            r7 = processStateRecord.getCurProcState() <= 6;
            processStateRecord.setFgInteractionTime(0L);
        }
        if (cachedCompatChange) {
            j3 = this.mConstants.USAGE_STATS_INTERACTION_INTERVAL_POST_S;
        } else {
            j3 = this.mConstants.USAGE_STATS_INTERACTION_INTERVAL_PRE_S;
        }
        if (r7 && (!processStateRecord.hasReportedInteraction() || j - processStateRecord.getInteractionEventTime() > j3)) {
            processStateRecord.setInteractionEventTime(j);
            String[] packageList = processRecord.getPackageList();
            if (packageList != null) {
                for (String str : packageList) {
                    this.mService.mUsageStatsService.reportEvent(str, processRecord.userId, 6);
                }
            }
        }
        processStateRecord.setReportedInteraction(r7);
        if (r7) {
            return;
        }
        processStateRecord.setInteractionEventTime(0L);
    }

    private void maybeUpdateLastTopTime(ProcessStateRecord processStateRecord, long j) {
        if (processStateRecord.getSetProcState() > 2 || processStateRecord.getCurProcState() <= 2) {
            return;
        }
        processStateRecord.setLastTopTime(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void idleUidsLocked() {
        int size = this.mActiveUids.size();
        this.mService.mHandler.removeMessages(58);
        if (size <= 0) {
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = elapsedRealtime - this.mConstants.BACKGROUND_SETTLE_TIME;
        PowerManagerInternal powerManagerInternal = this.mLocalPowerManager;
        if (powerManagerInternal != null) {
            powerManagerInternal.startUidChanges();
        }
        long j2 = 0;
        for (int i = size - 1; i >= 0; i--) {
            UidRecord valueAt = this.mActiveUids.valueAt(i);
            long lastBackgroundTime = valueAt.getLastBackgroundTime();
            if (lastBackgroundTime > 0 && !valueAt.isIdle()) {
                if (lastBackgroundTime <= j) {
                    EventLogTags.writeAmUidIdle(valueAt.getUid());
                    ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                    ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            valueAt.setIdle(true);
                            valueAt.setSetIdle(true);
                        } catch (Throwable th) {
                            ActivityManagerService.resetPriorityAfterProcLockedSection();
                            throw th;
                        }
                    }
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    this.mService.doStopUidLocked(valueAt.getUid(), valueAt);
                } else if (j2 == 0 || j2 > lastBackgroundTime) {
                    j2 = lastBackgroundTime;
                }
            }
        }
        PowerManagerInternal powerManagerInternal2 = this.mLocalPowerManager;
        if (powerManagerInternal2 != null) {
            powerManagerInternal2.finishUidChanges();
        }
        if (this.mService.mConstants.mKillBgRestrictedAndCachedIdle) {
            ArraySet<ProcessRecord> arraySet = this.mProcessList.mAppsInBackgroundRestricted;
            int size2 = arraySet.size();
            for (int i2 = 0; i2 < size2; i2++) {
                long lambda$killAppIfBgRestrictedAndCachedIdleLocked$4 = this.mProcessList.lambda$killAppIfBgRestrictedAndCachedIdleLocked$4(arraySet.valueAt(i2), elapsedRealtime) - this.mConstants.BACKGROUND_SETTLE_TIME;
                if (lambda$killAppIfBgRestrictedAndCachedIdleLocked$4 > 0 && (j2 == 0 || j2 > lambda$killAppIfBgRestrictedAndCachedIdleLocked$4)) {
                    j2 = lambda$killAppIfBgRestrictedAndCachedIdleLocked$4;
                }
            }
        }
        if (j2 > 0) {
            this.mService.mHandler.sendEmptyMessageDelayed(58, (j2 + this.mConstants.BACKGROUND_SETTLE_TIME) - elapsedRealtime);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setAppIdTempAllowlistStateLSP(int i, boolean z) {
        boolean z2 = false;
        for (int size = this.mActiveUids.size() - 1; size >= 0; size--) {
            UidRecord valueAt = this.mActiveUids.valueAt(size);
            if (valueAt.getUid() == i && valueAt.isCurAllowListed() != z) {
                valueAt.setCurAllowListed(z);
                z2 = true;
            }
        }
        if (z2) {
            mOomAdjusterExt.setFullOomAdjUpdateInfo(i, null, z ? "add tmp white" : "rm tmp white");
            updateOomAdjLSP(10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setUidTempAllowlistStateLSP(int i, boolean z) {
        UidRecord uidRecord = this.mActiveUids.get(i);
        if (uidRecord == null || uidRecord.isCurAllowListed() == z) {
            return;
        }
        uidRecord.setCurAllowListed(z);
        mOomAdjusterExt.setFullOomAdjUpdateInfo(i, null, z ? "add tmp white" : "rm tmp white");
        updateOomAdjLSP(10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void dumpProcessListVariablesLocked(ProtoOutputStream protoOutputStream) {
        protoOutputStream.write(1120986464305L, this.mAdjSeq);
        protoOutputStream.write(1120986464306L, this.mProcessList.getLruSeqLOSP());
        protoOutputStream.write(1120986464307L, this.mNumNonCachedProcs);
        protoOutputStream.write(1120986464309L, this.mNumServiceProcs);
        protoOutputStream.write(1120986464310L, this.mNewNumServiceProcs);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void dumpSequenceNumbersLocked(PrintWriter printWriter) {
        printWriter.println("  mAdjSeq=" + this.mAdjSeq + " mLruSeq=" + this.mProcessList.getLruSeqLOSP());
        mOomAdjusterExt.dumpOomAdjStatsLocked(printWriter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void dumpProcCountsLocked(PrintWriter printWriter) {
        printWriter.println("  mNumNonCachedProcs=" + this.mNumNonCachedProcs + " (" + this.mProcessList.getLruSizeLOSP() + " total) mNumCachedHiddenProcs=" + this.mNumCachedHiddenProcs + " mNumServiceProcs=" + this.mNumServiceProcs + " mNewNumServiceProcs=" + this.mNewNumServiceProcs);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void dumpCachedAppOptimizerSettings(PrintWriter printWriter) {
        this.mCachedAppOptimizer.dump(printWriter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void dumpCacheOomRankerSettings(PrintWriter printWriter) {
        this.mCacheOomRanker.dump(printWriter);
    }

    @GuardedBy({"mService", "mProcLock"})
    private void updateAppFreezeStateLSP(ProcessRecord processRecord, int i) {
        if (this.mCachedAppOptimizer.useFreezer() && !processRecord.mOptRecord.isFreezeExempt()) {
            ProcessCachedOptimizerRecord processCachedOptimizerRecord = processRecord.mOptRecord;
            if (processCachedOptimizerRecord.isFrozen() && processCachedOptimizerRecord.shouldNotFreeze()) {
                this.mCachedAppOptimizer.unfreezeAppLSP(processRecord, CachedAppOptimizer.getUnfreezeReasonCodeFromOomAdjReason(i));
                return;
            }
            ProcessStateRecord processStateRecord = processRecord.mState;
            if (processStateRecord.getCurAdj() >= 900 && !processCachedOptimizerRecord.isFrozen() && !processCachedOptimizerRecord.shouldNotFreeze()) {
                this.mCachedAppOptimizer.freezeAppAsyncLSP(processRecord);
            } else if (processStateRecord.getSetAdj() < 900) {
                this.mCachedAppOptimizer.unfreezeAppLSP(processRecord, CachedAppOptimizer.getUnfreezeReasonCodeFromOomAdjReason(i));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void unfreezeTemporarily(ProcessRecord processRecord, int i) {
        if (this.mCachedAppOptimizer.useFreezer()) {
            ProcessCachedOptimizerRecord processCachedOptimizerRecord = processRecord.mOptRecord;
            if (processCachedOptimizerRecord.isFrozen() || processCachedOptimizerRecord.isPendingFreeze()) {
                ArrayList<ProcessRecord> arrayList = this.mTmpProcessList;
                ActiveUids activeUids = this.mTmpUidRecords;
                this.mTmpProcessSet.add(processRecord);
                collectReachableProcessesLocked(this.mTmpProcessSet, arrayList, activeUids);
                this.mTmpProcessSet.clear();
                int size = arrayList.size();
                for (int i2 = 0; i2 < size; i2++) {
                    this.mCachedAppOptimizer.unfreezeTemporarily(arrayList.get(i2), i);
                }
                arrayList.clear();
            }
        }
    }

    public IOomAdjusterWrapper getWrapper() {
        return this.mOomAdjWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class OomAdjusterWrapper implements IOomAdjusterWrapper {
        private OomAdjusterWrapper() {
        }

        @Override // com.android.server.am.IOomAdjusterWrapper
        public void setFullOomAdjUpdateInfo(int i, String str, String str2) {
            OomAdjuster.mOomAdjusterExt.setFullOomAdjUpdateInfo(i, str, str2);
        }
    }
}
