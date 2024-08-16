package com.android.server.am;

import android.os.Handler;
import android.os.MessageQueue;
import android.os.Process;
import android.os.StrictMode;
import android.util.FeatureFlagUtils;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.ProcStatsUtil;
import com.android.internal.os.ProcessCpuTracker;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PhantomProcessList {
    private static final String[] CGROUP_PATH_PREFIXES = {"/acct/uid_", "/sys/fs/cgroup/uid_"};
    private static final String CGROUP_PID_PREFIX = "/pid_";
    private static final String CGROUP_PROCS = "/cgroup.procs";
    private static final int CGROUP_V1 = 0;
    private static final int CGROUP_V2 = 1;
    static final String TAG = "ActivityManager";

    @VisibleForTesting
    Injector mInjector;
    private final Handler mKillHandler;
    private final ActivityManagerService mService;

    @GuardedBy({"mLock"})
    int mUpdateSeq;
    final Object mLock = new Object();

    @GuardedBy({"mLock"})
    final SparseArray<PhantomProcessRecord> mPhantomProcesses = new SparseArray<>();

    @GuardedBy({"mLock"})
    final SparseArray<SparseArray<PhantomProcessRecord>> mAppPhantomProcessMap = new SparseArray<>();

    @GuardedBy({"mLock"})
    final SparseArray<PhantomProcessRecord> mPhantomProcessesPidFds = new SparseArray<>();

    @GuardedBy({"mLock"})
    final SparseArray<PhantomProcessRecord> mZombiePhantomProcesses = new SparseArray<>();

    @GuardedBy({"mLock"})
    private final ArrayList<PhantomProcessRecord> mTempPhantomProcesses = new ArrayList<>();

    @GuardedBy({"mLock"})
    private final SparseArray<ProcessRecord> mPhantomToAppProcessMap = new SparseArray<>();

    @GuardedBy({"mLock"})
    private final SparseArray<InputStream> mCgroupProcsFds = new SparseArray<>();

    @GuardedBy({"mLock"})
    private final byte[] mDataBuffer = new byte[4096];

    @GuardedBy({"mLock"})
    private boolean mTrimPhantomProcessScheduled = false;

    @VisibleForTesting
    int mCgroupVersion = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PhantomProcessList(ActivityManagerService activityManagerService) {
        this.mService = activityManagerService;
        ProcessList processList = activityManagerService.mProcessList;
        this.mKillHandler = ProcessList.sKillHandler;
        this.mInjector = new Injector();
        probeCgroupVersion();
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    void lookForPhantomProcessesLocked() {
        this.mPhantomToAppProcessMap.clear();
        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            synchronized (this.mService.mPidsSelfLocked) {
                for (int size = this.mService.mPidsSelfLocked.size() - 1; size >= 0; size--) {
                    lookForPhantomProcessesLocked(this.mService.mPidsSelfLocked.valueAt(size));
                }
            }
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }

    @GuardedBy({"mLock", "mService.mPidsSelfLocked"})
    private void lookForPhantomProcessesLocked(ProcessRecord processRecord) {
        int readCgroupProcs;
        if (processRecord.appZygote || processRecord.isKilled() || processRecord.isKilledByAm()) {
            return;
        }
        int pid = processRecord.getPid();
        InputStream inputStream = this.mCgroupProcsFds.get(pid);
        if (inputStream == null) {
            String cgroupFilePath = getCgroupFilePath(processRecord.info.uid, pid);
            try {
                inputStream = this.mInjector.openCgroupProcs(cgroupFilePath);
                this.mCgroupProcsFds.put(pid, inputStream);
            } catch (FileNotFoundException | SecurityException e) {
                if (ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                    Slog.w("ActivityManager", "Unable to open " + cgroupFilePath, e);
                    return;
                }
                return;
            }
        }
        byte[] bArr = this.mDataBuffer;
        long j = 0;
        int i = 0;
        do {
            try {
                readCgroupProcs = this.mInjector.readCgroupProcs(inputStream, bArr, 0, bArr.length);
                if (readCgroupProcs == -1) {
                    break;
                }
                j += readCgroupProcs;
                for (int i2 = 0; i2 < readCgroupProcs; i2++) {
                    byte b = bArr[i2];
                    if (b == 10) {
                        addChildPidLocked(processRecord, i, pid);
                        i = 0;
                    } else {
                        i = (i * 10) + (b - 48);
                    }
                }
            } catch (IOException e2) {
                Slog.e("ActivityManager", "Error in reading cgroup procs from " + processRecord, e2);
                IoUtils.closeQuietly(inputStream);
                this.mCgroupProcsFds.delete(pid);
                return;
            }
        } while (readCgroupProcs >= bArr.length);
        if (i != 0) {
            addChildPidLocked(processRecord, i, pid);
        }
        inputStream.skip(-j);
    }

    private void probeCgroupVersion() {
        for (int length = CGROUP_PATH_PREFIXES.length - 1; length >= 0; length--) {
            if (new File(CGROUP_PATH_PREFIXES[length] + 1000).exists()) {
                this.mCgroupVersion = length;
                return;
            }
        }
    }

    @VisibleForTesting
    String getCgroupFilePath(int i, int i2) {
        return CGROUP_PATH_PREFIXES[this.mCgroupVersion] + i + CGROUP_PID_PREFIX + i2 + CGROUP_PROCS;
    }

    static String getProcessName(int i) {
        String readTerminatedProcFile = ProcStatsUtil.readTerminatedProcFile("/proc/" + i + "/cmdline", (byte) 0);
        if (readTerminatedProcFile == null) {
            return null;
        }
        int lastIndexOf = readTerminatedProcFile.lastIndexOf(47);
        return (lastIndexOf <= 0 || lastIndexOf >= readTerminatedProcFile.length() + (-1)) ? readTerminatedProcFile : readTerminatedProcFile.substring(lastIndexOf + 1);
    }

    @GuardedBy({"mLock", "mService.mPidsSelfLocked"})
    private void addChildPidLocked(ProcessRecord processRecord, int i, int i2) {
        if (i2 != i) {
            ProcessRecord processRecord2 = this.mService.mPidsSelfLocked.get(i);
            if (processRecord2 != null) {
                if (processRecord2.appZygote || !ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                    return;
                }
                Slog.w("ActivityManager", "Unexpected: " + processRecord2 + " appears in the cgroup.procs of " + processRecord);
                return;
            }
            int indexOfKey = this.mPhantomToAppProcessMap.indexOfKey(i);
            if (indexOfKey >= 0) {
                if (processRecord == this.mPhantomToAppProcessMap.valueAt(indexOfKey)) {
                    return;
                } else {
                    this.mPhantomToAppProcessMap.setValueAt(indexOfKey, processRecord);
                }
            } else {
                this.mPhantomToAppProcessMap.put(i, processRecord);
            }
            int uidForPid = Process.getUidForPid(i);
            String processName = this.mInjector.getProcessName(i);
            if (processName == null || uidForPid < 0) {
                this.mPhantomToAppProcessMap.delete(i);
            } else {
                getOrCreatePhantomProcessIfNeededLocked(processName, uidForPid, i, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAppDied(int i) {
        synchronized (this.mLock) {
            int indexOfKey = this.mCgroupProcsFds.indexOfKey(i);
            if (indexOfKey >= 0) {
                InputStream valueAt = this.mCgroupProcsFds.valueAt(indexOfKey);
                this.mCgroupProcsFds.removeAt(indexOfKey);
                IoUtils.closeQuietly(valueAt);
            }
        }
    }

    @GuardedBy({"mLock"})
    PhantomProcessRecord getOrCreatePhantomProcessIfNeededLocked(String str, int i, int i2, boolean z) {
        ProcessRecord processRecord;
        if (isAppProcess(i2)) {
            return null;
        }
        int indexOfKey = this.mPhantomProcesses.indexOfKey(i2);
        if (indexOfKey >= 0) {
            PhantomProcessRecord valueAt = this.mPhantomProcesses.valueAt(indexOfKey);
            if (valueAt.equals(str, i, i2)) {
                return valueAt;
            }
            Slog.w("ActivityManager", "Stale " + valueAt + ", removing");
            onPhantomProcessKilledLocked(valueAt);
        } else {
            int indexOfKey2 = this.mZombiePhantomProcesses.indexOfKey(i2);
            if (indexOfKey2 >= 0) {
                PhantomProcessRecord valueAt2 = this.mZombiePhantomProcesses.valueAt(indexOfKey2);
                if (valueAt2.equals(str, i, i2)) {
                    return valueAt2;
                }
                this.mZombiePhantomProcesses.removeAt(indexOfKey2);
            }
        }
        if (z && (processRecord = this.mPhantomToAppProcessMap.get(i2)) != null) {
            try {
                int pid = processRecord.getPid();
                PhantomProcessRecord phantomProcessRecord = new PhantomProcessRecord(str, i, i2, pid, this.mService, new Consumer() { // from class: com.android.server.am.PhantomProcessList$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PhantomProcessList.this.onPhantomProcessKilledLocked((PhantomProcessRecord) obj);
                    }
                });
                phantomProcessRecord.mUpdateSeq = this.mUpdateSeq;
                this.mPhantomProcesses.put(i2, phantomProcessRecord);
                SparseArray<PhantomProcessRecord> sparseArray = this.mAppPhantomProcessMap.get(pid);
                if (sparseArray == null) {
                    sparseArray = new SparseArray<>();
                    this.mAppPhantomProcessMap.put(pid, sparseArray);
                }
                sparseArray.put(i2, phantomProcessRecord);
                if (phantomProcessRecord.mPidFd != null) {
                    this.mKillHandler.getLooper().getQueue().addOnFileDescriptorEventListener(phantomProcessRecord.mPidFd, 5, new MessageQueue.OnFileDescriptorEventListener() { // from class: com.android.server.am.PhantomProcessList$$ExternalSyntheticLambda1
                        @Override // android.os.MessageQueue.OnFileDescriptorEventListener
                        public final int onFileDescriptorEvents(FileDescriptor fileDescriptor, int i3) {
                            int onPhantomProcessFdEvent;
                            onPhantomProcessFdEvent = PhantomProcessList.this.onPhantomProcessFdEvent(fileDescriptor, i3);
                            return onPhantomProcessFdEvent;
                        }
                    });
                    this.mPhantomProcessesPidFds.put(phantomProcessRecord.mPidFd.getInt$(), phantomProcessRecord);
                }
                scheduleTrimPhantomProcessesLocked();
                return phantomProcessRecord;
            } catch (IllegalStateException unused) {
            }
        }
        return null;
    }

    private boolean isAppProcess(int i) {
        boolean z;
        synchronized (this.mService.mPidsSelfLocked) {
            z = this.mService.mPidsSelfLocked.get(i) != null;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int onPhantomProcessFdEvent(FileDescriptor fileDescriptor, int i) {
        synchronized (this.mLock) {
            PhantomProcessRecord phantomProcessRecord = this.mPhantomProcessesPidFds.get(fileDescriptor.getInt$());
            if (phantomProcessRecord == null) {
                return 0;
            }
            if ((i & 1) != 0) {
                phantomProcessRecord.onProcDied(true);
            } else {
                phantomProcessRecord.killLocked("Process error", true);
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void onPhantomProcessKilledLocked(PhantomProcessRecord phantomProcessRecord) {
        FileDescriptor fileDescriptor = phantomProcessRecord.mPidFd;
        if (fileDescriptor != null && fileDescriptor.valid()) {
            this.mKillHandler.getLooper().getQueue().removeOnFileDescriptorEventListener(phantomProcessRecord.mPidFd);
            this.mPhantomProcessesPidFds.remove(phantomProcessRecord.mPidFd.getInt$());
            IoUtils.closeQuietly(phantomProcessRecord.mPidFd);
        }
        this.mPhantomProcesses.remove(phantomProcessRecord.mPid);
        int indexOfKey = this.mAppPhantomProcessMap.indexOfKey(phantomProcessRecord.mPpid);
        if (indexOfKey < 0) {
            return;
        }
        SparseArray<PhantomProcessRecord> valueAt = this.mAppPhantomProcessMap.valueAt(indexOfKey);
        valueAt.remove(phantomProcessRecord.mPid);
        if (valueAt.size() == 0) {
            this.mAppPhantomProcessMap.removeAt(indexOfKey);
        }
        if (phantomProcessRecord.mZombie) {
            this.mZombiePhantomProcesses.put(phantomProcessRecord.mPid, phantomProcessRecord);
        } else {
            this.mZombiePhantomProcesses.remove(phantomProcessRecord.mPid);
        }
    }

    @GuardedBy({"mLock"})
    private void scheduleTrimPhantomProcessesLocked() {
        if (this.mTrimPhantomProcessScheduled) {
            return;
        }
        this.mTrimPhantomProcessScheduled = true;
        this.mService.mHandler.post(new ActivityManagerConstants$$ExternalSyntheticLambda2(this));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void trimPhantomProcessesIfNecessary() {
        if (this.mService.mSystemReady && FeatureFlagUtils.isEnabled(this.mService.mContext, "settings_enable_monitor_phantom_procs")) {
            ActivityManagerGlobalLock activityManagerGlobalLock = this.mService.mProcLock;
            ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    synchronized (this.mLock) {
                        this.mTrimPhantomProcessScheduled = false;
                        if (this.mService.mConstants.MAX_PHANTOM_PROCESSES < this.mPhantomProcesses.size()) {
                            for (int size = this.mPhantomProcesses.size() - 1; size >= 0; size--) {
                                this.mTempPhantomProcesses.add(this.mPhantomProcesses.valueAt(size));
                            }
                            synchronized (this.mService.mPidsSelfLocked) {
                                Collections.sort(this.mTempPhantomProcesses, new Comparator() { // from class: com.android.server.am.PhantomProcessList$$ExternalSyntheticLambda2
                                    @Override // java.util.Comparator
                                    public final int compare(Object obj, Object obj2) {
                                        int lambda$trimPhantomProcessesIfNecessary$0;
                                        lambda$trimPhantomProcessesIfNecessary$0 = PhantomProcessList.this.lambda$trimPhantomProcessesIfNecessary$0((PhantomProcessRecord) obj, (PhantomProcessRecord) obj2);
                                        return lambda$trimPhantomProcessesIfNecessary$0;
                                    }
                                });
                            }
                            for (int size2 = this.mTempPhantomProcesses.size() - 1; size2 >= this.mService.mConstants.MAX_PHANTOM_PROCESSES; size2--) {
                                this.mTempPhantomProcesses.get(size2).killLocked("Trimming phantom processes", true);
                            }
                            this.mTempPhantomProcesses.clear();
                        }
                    }
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterProcLockedSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$trimPhantomProcessesIfNecessary$0(PhantomProcessRecord phantomProcessRecord, PhantomProcessRecord phantomProcessRecord2) {
        ProcessRecord processRecord = this.mService.mPidsSelfLocked.get(phantomProcessRecord.mPpid);
        ProcessRecord processRecord2 = this.mService.mPidsSelfLocked.get(phantomProcessRecord2.mPpid);
        if (processRecord == null && processRecord2 == null) {
            return 0;
        }
        if (processRecord == null) {
            return 1;
        }
        if (processRecord2 == null) {
            return -1;
        }
        if (processRecord.mState.getCurAdj() != processRecord2.mState.getCurAdj()) {
            return processRecord.mState.getCurAdj() - processRecord2.mState.getCurAdj();
        }
        long j = phantomProcessRecord.mKnownSince;
        long j2 = phantomProcessRecord2.mKnownSince;
        if (j != j2) {
            return j < j2 ? 1 : -1;
        }
        return 0;
    }

    @GuardedBy({"mLock"})
    void pruneStaleProcessesLocked() {
        for (int size = this.mPhantomProcesses.size() - 1; size >= 0; size--) {
            PhantomProcessRecord valueAt = this.mPhantomProcesses.valueAt(size);
            if (valueAt.mUpdateSeq < this.mUpdateSeq) {
                if (ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                    Slog.v("ActivityManager", "Pruning " + valueAt + " as it should have been dead.");
                }
                valueAt.killLocked("Stale process", true);
            }
        }
        for (int size2 = this.mZombiePhantomProcesses.size() - 1; size2 >= 0; size2--) {
            PhantomProcessRecord valueAt2 = this.mZombiePhantomProcesses.valueAt(size2);
            if (valueAt2.mUpdateSeq < this.mUpdateSeq && ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                Slog.v("ActivityManager", "Pruning " + valueAt2 + " as it should have been dead.");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void killPhantomProcessGroupLocked(ProcessRecord processRecord, PhantomProcessRecord phantomProcessRecord, int i, int i2, String str) {
        synchronized (this.mLock) {
            int indexOfKey = this.mAppPhantomProcessMap.indexOfKey(phantomProcessRecord.mPpid);
            if (indexOfKey >= 0) {
                SparseArray<PhantomProcessRecord> valueAt = this.mAppPhantomProcessMap.valueAt(indexOfKey);
                for (int size = valueAt.size() - 1; size >= 0; size--) {
                    PhantomProcessRecord valueAt2 = valueAt.valueAt(size);
                    if (valueAt2 == phantomProcessRecord) {
                        valueAt2.killLocked(str, true);
                    } else {
                        valueAt2.killLocked("Caused by siling process: " + str, false);
                    }
                }
            }
        }
        processRecord.killLocked("Caused by child process: " + str, i, i2, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forEachPhantomProcessOfApp(ProcessRecord processRecord, Function<PhantomProcessRecord, Boolean> function) {
        synchronized (this.mLock) {
            int indexOfKey = this.mAppPhantomProcessMap.indexOfKey(processRecord.getPid());
            if (indexOfKey >= 0) {
                SparseArray<PhantomProcessRecord> valueAt = this.mAppPhantomProcessMap.valueAt(indexOfKey);
                for (int size = valueAt.size() - 1; size >= 0 && function.apply(valueAt.valueAt(size)).booleanValue(); size--) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"tracker"})
    public void updateProcessCpuStatesLocked(ProcessCpuTracker processCpuTracker) {
        synchronized (this.mLock) {
            this.mUpdateSeq++;
            lookForPhantomProcessesLocked();
            for (int countStats = processCpuTracker.countStats() - 1; countStats >= 0; countStats--) {
                ProcessCpuTracker.Stats stats = processCpuTracker.getStats(countStats);
                PhantomProcessRecord orCreatePhantomProcessIfNeededLocked = getOrCreatePhantomProcessIfNeededLocked(stats.name, stats.uid, stats.pid, false);
                if (orCreatePhantomProcessIfNeededLocked != null) {
                    orCreatePhantomProcessIfNeededLocked.mUpdateSeq = this.mUpdateSeq;
                    long j = orCreatePhantomProcessIfNeededLocked.mCurrentCputime + stats.rel_utime + stats.rel_stime;
                    orCreatePhantomProcessIfNeededLocked.mCurrentCputime = j;
                    if (orCreatePhantomProcessIfNeededLocked.mLastCputime == 0) {
                        orCreatePhantomProcessIfNeededLocked.mLastCputime = j;
                    }
                    orCreatePhantomProcessIfNeededLocked.updateAdjLocked();
                }
            }
            pruneStaleProcessesLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        synchronized (this.mLock) {
            dumpPhantomeProcessLocked(printWriter, str, "All Active App Child Processes:", this.mPhantomProcesses);
            dumpPhantomeProcessLocked(printWriter, str, "All Zombie App Child Processes:", this.mZombiePhantomProcesses);
        }
    }

    void dumpPhantomeProcessLocked(PrintWriter printWriter, String str, String str2, SparseArray<PhantomProcessRecord> sparseArray) {
        int size = sparseArray.size();
        if (size == 0) {
            return;
        }
        printWriter.println();
        printWriter.print(str);
        printWriter.println(str2);
        for (int i = 0; i < size; i++) {
            PhantomProcessRecord valueAt = sparseArray.valueAt(i);
            printWriter.print(str);
            printWriter.print("  proc #");
            printWriter.print(i);
            printWriter.print(": ");
            printWriter.println(valueAt.toString());
            valueAt.dump(printWriter, str + "    ");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        Injector() {
        }

        InputStream openCgroupProcs(String str) throws FileNotFoundException, SecurityException {
            return new FileInputStream(str);
        }

        int readCgroupProcs(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
            return inputStream.read(bArr, i, i2);
        }

        String getProcessName(int i) {
            return PhantomProcessList.getProcessName(i);
        }
    }
}
