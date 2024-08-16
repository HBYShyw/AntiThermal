package com.android.server.am;

import android.app.BackgroundStartPrivileges;
import android.app.IApplicationThread;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ProcessInfo;
import android.content.pm.VersionedPackage;
import android.content.res.CompatibilityInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.system.OsConstants;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.DebugUtils;
import android.util.EventLog;
import android.util.IntArray;
import android.util.Slog;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.CompositeRWLock;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.procstats.ProcessState;
import com.android.internal.app.procstats.ProcessStats;
import com.android.internal.os.Zygote;
import com.android.internal.util.Preconditions;
import com.android.server.FgThread;
import com.android.server.wm.WindowProcessController;
import com.android.server.wm.WindowProcessListener;
import com.oplus.uifirst.IOplusUIFirstManagerExt;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ProcessRecord implements WindowProcessListener {
    static final int MAX_GL_THREADS = 4;
    static final String TAG = "ActivityManager";
    static HashMap<Integer, Boolean> sBackAnrForPids = new HashMap<>();
    final boolean appZygote;
    ArrayDeque<Integer> glThreads;
    IntArray hwuiTaskThreads;
    public volatile ApplicationInfo info;
    public final boolean isSdkSandbox;
    final boolean isolated;

    @GuardedBy({"mBackgroundStartPrivileges"})
    private final ArrayMap<Binder, BackgroundStartPrivileges> mBackgroundStartPrivileges;

    @GuardedBy({"mBackgroundStartPrivileges"})
    private BackgroundStartPrivileges mBackgroundStartPrivilegesMerged;
    private volatile long mBindApplicationTime;
    private volatile boolean mBindMountPending;

    @GuardedBy({"mService"})
    private CompatibilityInfo mCompat;

    @GuardedBy({"mService"})
    private IBinder.DeathRecipient mDeathRecipient;

    @GuardedBy({"mService"})
    private boolean mDebugging;

    @GuardedBy({"mService"})
    private long[] mDisabledCompatChanges;

    @GuardedBy({"mService"})
    private int mDyingPid;
    final ProcessErrorStateRecord mErrorState;

    @GuardedBy({"mService"})
    private int[] mGids;
    private volatile HostingRecord mHostingRecord;

    @GuardedBy({"mService"})
    private boolean mInFullBackup;

    @CompositeRWLock({"mService", "mProcLock"})
    private ActiveInstrumentation mInstr;

    @GuardedBy({"mService"})
    private String mInstructionSet;

    @GuardedBy({"mService"})
    private String mIsolatedEntryPoint;

    @GuardedBy({"mService"})
    private String[] mIsolatedEntryPointArgs;

    @CompositeRWLock({"mService", "mProcLock"})
    private long mKillTime;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mKilled;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mKilledByAm;

    @CompositeRWLock({"mService", "mProcLock"})
    private long mLastActivityTime;

    @GuardedBy({"mService"})
    private int mLruSeq;
    private volatile int mMountMode;

    @CompositeRWLock({"mService", "mProcLock"})
    private IApplicationThread mOnewayThread;
    final ProcessCachedOptimizerRecord mOptRecord;

    @GuardedBy({"mService"})
    private boolean mPendingFinishAttach;

    @GuardedBy({"mService"})
    private boolean mPendingStart;
    private volatile boolean mPersistent;

    @CompositeRWLock({"mService", "mProcLock"})
    public int mPid;

    @CompositeRWLock({"mService", "mProcLock"})
    private ArraySet<String> mPkgDeps;
    public final PackageList mPkgList;
    volatile ProcessRecord mPredecessor;
    private final ActivityManagerGlobalLock mProcLock;
    volatile boolean mProcessGroupCreated;
    private IProcessRecordExt mProcessRecordExt;
    final ProcessProfileRecord mProfile;
    final ProcessProviderRecord mProviders;
    final ProcessReceiverRecord mReceivers;
    private volatile boolean mRemoved;

    @GuardedBy({"mProcLock"})
    private int mRenderThreadTid;

    @GuardedBy({"mService"})
    private String mRequiredAbi;
    private volatile String mSeInfo;
    final ActivityManagerService mService;
    public final ProcessServiceRecord mServices;
    private String mShortStringName;
    volatile boolean mSkipProcessGroupCreation;
    private IProcessRecordSocExt mSocExt;
    private volatile long mStartElapsedTime;

    @GuardedBy({"mService"})
    private long mStartSeq;
    private volatile int mStartUid;
    private volatile long mStartUptime;
    public final ProcessStateRecord mState;
    private String mStringName;
    volatile ProcessRecord mSuccessor;
    Runnable mSuccessorStartRunnable;

    @CompositeRWLock({"mService", "mProcLock"})
    public IApplicationThread mThread;
    private IOplusUIFirstManagerExt mUIFirstManagerExt;

    @CompositeRWLock({"mService", "mProcLock"})
    private UidRecord mUidRecord;

    @GuardedBy({"mProcLock"})
    private boolean mUnlocked;

    @GuardedBy({"mService"})
    private boolean mUsingWrapper;

    @GuardedBy({"mProcLock"})
    private boolean mWaitedForDebugger;

    @GuardedBy({"mService"})
    private String mWaitingToKill;
    private final WindowProcessController mWindowProcessController;
    private ProcessRecordWrapper mWrapper;
    final ProcessInfo processInfo;
    public final String processName;
    boolean runningRemoteAnimation;
    final String sdkSandboxClientAppPackage;
    final String sdkSandboxClientAppVolumeUuid;
    public final int uid;
    public final int userId;
    int uxGlThreadValue;
    int uxValue;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStartParams(int i, HostingRecord hostingRecord, String str, long j, long j2) {
        this.mStartUid = i;
        this.mHostingRecord = hostingRecord;
        this.mSeInfo = str;
        this.mStartUptime = j;
        this.mStartElapsedTime = j2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void dump(PrintWriter printWriter, String str) {
        long uptimeMillis = SystemClock.uptimeMillis();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        printWriter.print(str);
        printWriter.print("user #");
        printWriter.print(this.userId);
        printWriter.print(" uid=");
        printWriter.print(this.info.uid);
        if (this.uid != this.info.uid) {
            printWriter.print(" ISOLATED uid=");
            printWriter.print(this.uid);
        }
        printWriter.print(" gids={");
        if (this.mGids != null) {
            for (int i = 0; i < this.mGids.length; i++) {
                if (i != 0) {
                    printWriter.print(", ");
                }
                printWriter.print(this.mGids[i]);
            }
        }
        printWriter.println("}");
        if (this.processInfo != null) {
            printWriter.print(str);
            printWriter.println("processInfo:");
            if (this.processInfo.deniedPermissions != null) {
                for (int i2 = 0; i2 < this.processInfo.deniedPermissions.size(); i2++) {
                    printWriter.print(str);
                    printWriter.print("  deny: ");
                    printWriter.println((String) this.processInfo.deniedPermissions.valueAt(i2));
                }
            }
            if (this.processInfo.gwpAsanMode != -1) {
                printWriter.print(str);
                printWriter.println("  gwpAsanMode=" + this.processInfo.gwpAsanMode);
            }
            if (this.processInfo.memtagMode != -1) {
                printWriter.print(str);
                printWriter.println("  memtagMode=" + this.processInfo.memtagMode);
            }
        }
        printWriter.print(str);
        printWriter.print("mRequiredAbi=");
        printWriter.print(this.mRequiredAbi);
        printWriter.print(" instructionSet=");
        printWriter.println(this.mInstructionSet);
        if (this.info.className != null) {
            printWriter.print(str);
            printWriter.print("class=");
            printWriter.println(this.info.className);
        }
        if (this.info.manageSpaceActivityName != null) {
            printWriter.print(str);
            printWriter.print("manageSpaceActivityName=");
            printWriter.println(this.info.manageSpaceActivityName);
        }
        printWriter.print(str);
        printWriter.print("dir=");
        printWriter.print(this.info.sourceDir);
        printWriter.print(" publicDir=");
        printWriter.print(this.info.publicSourceDir);
        printWriter.print(" data=");
        printWriter.println(this.info.dataDir);
        this.mPkgList.dump(printWriter, str);
        if (this.mPkgDeps != null) {
            printWriter.print(str);
            printWriter.print("packageDependencies={");
            for (int i3 = 0; i3 < this.mPkgDeps.size(); i3++) {
                if (i3 > 0) {
                    printWriter.print(", ");
                }
                printWriter.print(this.mPkgDeps.valueAt(i3));
            }
            printWriter.println("}");
        }
        printWriter.print(str);
        printWriter.print("compat=");
        printWriter.println(this.mCompat);
        if (this.mInstr != null) {
            printWriter.print(str);
            printWriter.print("mInstr=");
            printWriter.println(this.mInstr);
        }
        printWriter.print(str);
        printWriter.print("thread=");
        printWriter.println(this.mThread);
        printWriter.print(str);
        printWriter.print("pid=");
        printWriter.println(this.mPid);
        printWriter.print(str);
        printWriter.print("lastActivityTime=");
        TimeUtils.formatDuration(this.mLastActivityTime, uptimeMillis, printWriter);
        printWriter.print(str);
        printWriter.print("startUptimeTime=");
        TimeUtils.formatDuration(this.mStartElapsedTime, uptimeMillis, printWriter);
        printWriter.print(str);
        printWriter.print("startElapsedTime=");
        TimeUtils.formatDuration(this.mStartElapsedTime, elapsedRealtime, printWriter);
        printWriter.println();
        if (this.mPersistent || this.mRemoved) {
            printWriter.print(str);
            printWriter.print("persistent=");
            printWriter.print(this.mPersistent);
            printWriter.print(" removed=");
            printWriter.println(this.mRemoved);
        }
        if (this.mDebugging) {
            printWriter.print(str);
            printWriter.print("mDebugging=");
            printWriter.println(this.mDebugging);
        }
        if (this.mPendingStart) {
            printWriter.print(str);
            printWriter.print("pendingStart=");
            printWriter.println(this.mPendingStart);
        }
        printWriter.print(str);
        printWriter.print("startSeq=");
        printWriter.println(this.mStartSeq);
        printWriter.print(str);
        printWriter.print("mountMode=");
        printWriter.println(DebugUtils.valueToString(Zygote.class, "MOUNT_EXTERNAL_", this.mMountMode));
        if (this.mKilled || this.mKilledByAm || this.mWaitingToKill != null) {
            printWriter.print(str);
            printWriter.print("killed=");
            printWriter.print(this.mKilled);
            printWriter.print(" killedByAm=");
            printWriter.print(this.mKilledByAm);
            printWriter.print(" waitingToKill=");
            printWriter.println(this.mWaitingToKill);
        }
        if (this.mIsolatedEntryPoint != null || this.mIsolatedEntryPointArgs != null) {
            printWriter.print(str);
            printWriter.print("isolatedEntryPoint=");
            printWriter.println(this.mIsolatedEntryPoint);
            printWriter.print(str);
            printWriter.print("isolatedEntryPointArgs=");
            printWriter.println(Arrays.toString(this.mIsolatedEntryPointArgs));
        }
        if (this.mState.getSetProcState() > 10) {
            this.mProfile.dumpCputime(printWriter, str);
        }
        this.mProfile.dumpPss(printWriter, str, uptimeMillis);
        this.mState.dump(printWriter, str, uptimeMillis);
        this.mErrorState.dump(printWriter, str, uptimeMillis);
        this.mServices.dump(printWriter, str, uptimeMillis);
        this.mProviders.dump(printWriter, str, uptimeMillis);
        this.mReceivers.dump(printWriter, str, uptimeMillis);
        this.mOptRecord.dump(printWriter, str, uptimeMillis);
        this.mWindowProcessController.dump(printWriter, str);
        this.mProcessRecordExt.dump(printWriter, str, uptimeMillis);
    }

    ProcessRecord(ActivityManagerService activityManagerService, ApplicationInfo applicationInfo, String str, int i) {
        this(activityManagerService, applicationInfo, str, i, null, -1, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x00a9, code lost:
    
        if (r1.nativeHeapZeroInitialized == (-1)) goto L21;
     */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x009a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ProcessRecord(ActivityManagerService activityManagerService, ApplicationInfo applicationInfo, String str, int i, String str2, int i2, String str3) {
        ProcessInfo processInfo;
        boolean isSdkSandboxUid;
        PackageList packageList = new PackageList(this);
        this.mPkgList = packageList;
        this.mBackgroundStartPrivileges = new ArrayMap<>();
        this.mBackgroundStartPrivilegesMerged = BackgroundStartPrivileges.NONE;
        this.hwuiTaskThreads = new IntArray(2);
        this.glThreads = new ArrayDeque<>(4);
        this.uxValue = 0;
        this.uxGlThreadValue = 0;
        this.mWrapper = new ProcessRecordWrapper();
        this.mProcessRecordExt = (IProcessRecordExt) ExtLoader.type(IProcessRecordExt.class).base(this).create();
        this.mSocExt = (IProcessRecordSocExt) ExtLoader.type(IProcessRecordSocExt.class).base(this).create();
        this.mUIFirstManagerExt = (IOplusUIFirstManagerExt) ExtLoader.type(IOplusUIFirstManagerExt.class).create();
        this.mService = activityManagerService;
        this.mProcLock = activityManagerService.mProcLock;
        this.info = applicationInfo;
        PackageManagerInternal packageManagerInternal = activityManagerService.mPackageManagerInt;
        if (packageManagerInternal != null) {
            if (i2 > 0) {
                ArrayMap<String, ProcessInfo> processesForUid = packageManagerInternal.getProcessesForUid(i2);
                if (processesForUid != null) {
                    processInfo = processesForUid.get(str3);
                    if (processInfo != null) {
                        if (processInfo.deniedPermissions == null) {
                            if (processInfo.gwpAsanMode == -1) {
                                if (processInfo.memtagMode == -1) {
                                }
                            }
                        }
                    }
                }
                processInfo = null;
                if (processInfo != null) {
                }
            } else {
                ArrayMap<String, ProcessInfo> processesForUid2 = packageManagerInternal.getProcessesForUid(i);
                if (processesForUid2 != null) {
                    processInfo = processesForUid2.get(str);
                    if (processInfo != null) {
                    }
                }
                processInfo = null;
                if (processInfo != null) {
                }
            }
            this.processInfo = processInfo;
            this.isolated = Process.isIsolated(i);
            isSdkSandboxUid = Process.isSdkSandboxUid(i);
            this.isSdkSandbox = isSdkSandboxUid;
            this.appZygote = UserHandle.getAppId(i) < 90000 && UserHandle.getAppId(i) <= 98999;
            this.uid = i;
            int userId = UserHandle.getUserId(i);
            this.userId = userId;
            this.processName = str;
            this.sdkSandboxClientAppPackage = str2;
            if (!isSdkSandboxUid) {
                ApplicationInfo clientInfoForSdkSandbox = getClientInfoForSdkSandbox();
                this.sdkSandboxClientAppVolumeUuid = clientInfoForSdkSandbox != null ? clientInfoForSdkSandbox.volumeUuid : null;
            } else {
                this.sdkSandboxClientAppVolumeUuid = null;
            }
            this.mPersistent = false;
            this.mRemoved = false;
            ProcessProfileRecord processProfileRecord = new ProcessProfileRecord(this);
            this.mProfile = processProfileRecord;
            this.mServices = new ProcessServiceRecord(this);
            this.mProviders = new ProcessProviderRecord(this);
            this.mReceivers = new ProcessReceiverRecord(this);
            this.mErrorState = new ProcessErrorStateRecord(this);
            ProcessStateRecord processStateRecord = new ProcessStateRecord(this);
            this.mState = processStateRecord;
            ProcessCachedOptimizerRecord processCachedOptimizerRecord = new ProcessCachedOptimizerRecord(this);
            this.mOptRecord = processCachedOptimizerRecord;
            long uptimeMillis = SystemClock.uptimeMillis();
            processProfileRecord.init(uptimeMillis);
            processCachedOptimizerRecord.init(uptimeMillis);
            processStateRecord.init(uptimeMillis);
            this.mWindowProcessController = new WindowProcessController(activityManagerService.mActivityTaskManager, this.info, str, i, userId, this, this);
            packageList.put(applicationInfo.packageName, new ProcessStats.ProcessStateHolder(applicationInfo.longVersionCode));
        }
        processInfo = null;
        this.processInfo = processInfo;
        this.isolated = Process.isIsolated(i);
        isSdkSandboxUid = Process.isSdkSandboxUid(i);
        this.isSdkSandbox = isSdkSandboxUid;
        this.appZygote = UserHandle.getAppId(i) < 90000 && UserHandle.getAppId(i) <= 98999;
        this.uid = i;
        int userId2 = UserHandle.getUserId(i);
        this.userId = userId2;
        this.processName = str;
        this.sdkSandboxClientAppPackage = str2;
        if (!isSdkSandboxUid) {
        }
        this.mPersistent = false;
        this.mRemoved = false;
        ProcessProfileRecord processProfileRecord2 = new ProcessProfileRecord(this);
        this.mProfile = processProfileRecord2;
        this.mServices = new ProcessServiceRecord(this);
        this.mProviders = new ProcessProviderRecord(this);
        this.mReceivers = new ProcessReceiverRecord(this);
        this.mErrorState = new ProcessErrorStateRecord(this);
        ProcessStateRecord processStateRecord2 = new ProcessStateRecord(this);
        this.mState = processStateRecord2;
        ProcessCachedOptimizerRecord processCachedOptimizerRecord2 = new ProcessCachedOptimizerRecord(this);
        this.mOptRecord = processCachedOptimizerRecord2;
        long uptimeMillis2 = SystemClock.uptimeMillis();
        processProfileRecord2.init(uptimeMillis2);
        processCachedOptimizerRecord2.init(uptimeMillis2);
        processStateRecord2.init(uptimeMillis2);
        this.mWindowProcessController = new WindowProcessController(activityManagerService.mActivityTaskManager, this.info, str, i, userId2, this, this);
        packageList.put(applicationInfo.packageName, new ProcessStats.ProcessStateHolder(applicationInfo.longVersionCode));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public UidRecord getUidRecord() {
        return this.mUidRecord;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setUidRecord(UidRecord uidRecord) {
        this.mUidRecord = uidRecord;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageList getPkgList() {
        return this.mPkgList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public ArraySet<String> getPkgDeps() {
        return this.mPkgDeps;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setPkgDeps(ArraySet<String> arraySet) {
        this.mPkgDeps = arraySet;
    }

    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getPid() {
        return this.mPid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setPid(int i) {
        this.mPid = i;
        this.mWindowProcessController.setPid(i);
        this.mShortStringName = null;
        this.mStringName = null;
        synchronized (this.mProfile.mProfilerLock) {
            this.mProfile.setPid(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public int getSetAdj() {
        return this.mState.getSetAdj();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public IApplicationThread getThread() {
        return this.mThread;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public IApplicationThread getOnewayThread() {
        return this.mOnewayThread;
    }

    @GuardedBy(anyOf = {"mService", "mProcLock"})
    int getCurProcState() {
        return this.mState.getCurProcState();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getSetProcState() {
        return this.mState.getSetProcState();
    }

    @GuardedBy({"mService", "mProcLock"})
    public void makeActive(IApplicationThread iApplicationThread, ProcessStatsService processStatsService) {
        this.mProfile.onProcessActive(iApplicationThread, processStatsService);
        this.mThread = iApplicationThread;
        if (this.mPid == Process.myPid()) {
            this.mOnewayThread = new SameProcessApplicationThread(iApplicationThread, FgThread.getHandler());
        } else {
            this.mOnewayThread = iApplicationThread;
        }
        this.mWindowProcessController.setThread(iApplicationThread);
        this.mProcessRecordExt.createProcessInfo(this);
    }

    @GuardedBy({"mService", "mProcLock"})
    public void makeInactive(ProcessStatsService processStatsService) {
        this.mThread = null;
        this.mOnewayThread = null;
        this.mWindowProcessController.setThread((IApplicationThread) null);
        this.mProfile.onProcessInactive(processStatsService);
        this.mProcessRecordExt.resetUxState();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public int getDyingPid() {
        return this.mDyingPid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setDyingPid(int i) {
        this.mDyingPid = i;
    }

    @GuardedBy({"mService"})
    int[] getGids() {
        return this.mGids;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setGids(int[] iArr) {
        this.mGids = iArr;
    }

    @GuardedBy({"mService"})
    String getRequiredAbi() {
        return this.mRequiredAbi;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setRequiredAbi(String str) {
        this.mRequiredAbi = str;
        this.mWindowProcessController.setRequiredAbi(str);
    }

    @GuardedBy({"mService"})
    String getInstructionSet() {
        return this.mInstructionSet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setInstructionSet(String str) {
        this.mInstructionSet = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPersistent(boolean z) {
        this.mPersistent = z;
        this.mWindowProcessController.setPersistent(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPersistent() {
        return this.mPersistent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isPendingStart() {
        return this.mPendingStart;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setPendingStart(boolean z) {
        this.mPendingStart = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setPendingFinishAttach(boolean z) {
        this.mPendingFinishAttach = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isPendingFinishAttach() {
        return this.mPendingFinishAttach;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public long getStartSeq() {
        return this.mStartSeq;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setStartSeq(long j) {
        this.mStartSeq = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HostingRecord getHostingRecord() {
        return this.mHostingRecord;
    }

    void setHostingRecord(HostingRecord hostingRecord) {
        this.mHostingRecord = hostingRecord;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getSeInfo() {
        return this.mSeInfo;
    }

    void setSeInfo(String str) {
        this.mSeInfo = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getStartUptime() {
        return this.mStartUptime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public long getStartTime() {
        return this.mStartUptime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getStartElapsedTime() {
        return this.mStartElapsedTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getBindApplicationTime() {
        return this.mBindApplicationTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBindApplicationTime(long j) {
        this.mBindApplicationTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getStartUid() {
        return this.mStartUid;
    }

    void setStartUid(int i) {
        this.mStartUid = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMountMode() {
        return this.mMountMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMountMode(int i) {
        this.mMountMode = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isBindMountPending() {
        return this.mBindMountPending;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBindMountPending(boolean z) {
        this.mBindMountPending = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean isUnlocked() {
        return this.mUnlocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setUnlocked(boolean z) {
        this.mUnlocked = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public int getRenderThreadTid() {
        return this.mRenderThreadTid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setRenderThreadTid(int i) {
        this.mRenderThreadTid = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public CompatibilityInfo getCompat() {
        return this.mCompat;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setCompat(CompatibilityInfo compatibilityInfo) {
        this.mCompat = compatibilityInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public long[] getDisabledCompatChanges() {
        return this.mDisabledCompatChanges;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setDisabledCompatChanges(long[] jArr) {
        this.mDisabledCompatChanges = jArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void unlinkDeathRecipient() {
        IApplicationThread iApplicationThread;
        if (this.mDeathRecipient != null && (iApplicationThread = this.mThread) != null) {
            iApplicationThread.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
        }
        this.mDeathRecipient = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setDeathRecipient(IBinder.DeathRecipient deathRecipient) {
        this.mDeathRecipient = deathRecipient;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public IBinder.DeathRecipient getDeathRecipient() {
        return this.mDeathRecipient;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setActiveInstrumentation(ActiveInstrumentation activeInstrumentation) {
        this.mInstr = activeInstrumentation;
        boolean z = activeInstrumentation != null;
        this.mWindowProcessController.setInstrumenting(z, z ? activeInstrumentation.mSourceUid : -1, z && activeInstrumentation.mHasBackgroundActivityStartsPermission);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public ActiveInstrumentation getActiveInstrumentation() {
        return this.mInstr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public boolean isKilledByAm() {
        return this.mKilledByAm;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setKilledByAm(boolean z) {
        this.mKilledByAm = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public boolean isKilled() {
        return this.mKilled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setKilled(boolean z) {
        this.mKilled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public long getKillTime() {
        return this.mKillTime;
    }

    @GuardedBy({"mService", "mProcLock"})
    void setKillTime(long j) {
        this.mKillTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public String getWaitingToKill() {
        return this.mWaitingToKill;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setWaitingToKill(String str) {
        this.mWaitingToKill = str;
    }

    public boolean isRemoved() {
        return this.mRemoved;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRemoved(boolean z) {
        this.mRemoved = z;
    }

    @GuardedBy({"mService"})
    public boolean isDebugging() {
        return this.mDebugging;
    }

    public ApplicationInfo getClientInfoForSdkSandbox() {
        if (!this.isSdkSandbox || this.sdkSandboxClientAppPackage == null) {
            throw new IllegalStateException("getClientInfoForSdkSandbox called for non-sandbox process");
        }
        return this.mService.getPackageManagerInternal().getApplicationInfo(this.sdkSandboxClientAppPackage, 0L, 1000, this.userId);
    }

    public boolean isDebuggable() {
        if ((this.info.flags & 2) != 0) {
            return true;
        }
        if (!this.isSdkSandbox) {
            return false;
        }
        ApplicationInfo clientInfoForSdkSandbox = getClientInfoForSdkSandbox();
        return (clientInfoForSdkSandbox == null || (clientInfoForSdkSandbox.flags & 2) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setDebugging(boolean z) {
        this.mDebugging = z;
        this.mWindowProcessController.setDebugging(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean hasWaitedForDebugger() {
        return this.mWaitedForDebugger;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setWaitedForDebugger(boolean z) {
        this.mWaitedForDebugger = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public long getLastActivityTime() {
        return this.mLastActivityTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setLastActivityTime(long j) {
        this.mLastActivityTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isUsingWrapper() {
        return this.mUsingWrapper;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setUsingWrapper(boolean z) {
        this.mUsingWrapper = z;
        this.mWindowProcessController.setUsingWrapper(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public int getLruSeq() {
        return this.mLruSeq;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setLruSeq(int i) {
        this.mLruSeq = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public String getIsolatedEntryPoint() {
        return this.mIsolatedEntryPoint;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setIsolatedEntryPoint(String str) {
        this.mIsolatedEntryPoint = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public String[] getIsolatedEntryPointArgs() {
        return this.mIsolatedEntryPointArgs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setIsolatedEntryPointArgs(String[] strArr) {
        this.mIsolatedEntryPointArgs = strArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isInFullBackup() {
        return this.mInFullBackup;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setInFullBackup(boolean z) {
        this.mInFullBackup = z;
    }

    @GuardedBy({"mService"})
    public void setCached(boolean z) {
        this.mState.setCached(z);
    }

    @GuardedBy({"mService"})
    public boolean isCached() {
        return this.mState.isCached();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasActivities() {
        return this.mWindowProcessController.hasActivities();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasActivitiesOrRecentTasks() {
        return this.mWindowProcessController.hasActivitiesOrRecentTasks();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasRecentTasks() {
        return this.mWindowProcessController.hasRecentTasks();
    }

    @GuardedBy({"mService"})
    public ApplicationInfo getApplicationInfo() {
        return this.info;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public boolean onCleanupApplicationRecordLSP(ProcessStatsService processStatsService, boolean z, boolean z2) {
        this.mErrorState.onCleanupApplicationRecordLSP();
        resetPackageList(processStatsService);
        if (z2) {
            unlinkDeathRecipient();
        }
        makeInactive(processStatsService);
        setWaitingToKill(null);
        this.mState.onCleanupApplicationRecordLSP();
        this.mServices.onCleanupApplicationRecordLocked();
        this.mReceivers.onCleanupApplicationRecordLocked();
        return this.mProviders.onCleanupApplicationRecordLocked(z);
    }

    public boolean isInterestingToUserLocked() {
        if (this.mWindowProcessController.isInterestingToUser()) {
            return true;
        }
        return this.mServices.hasForegroundServices();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void scheduleCrashLocked(String str, int i, Bundle bundle) {
        if (this.mKilledByAm || this.mThread == null) {
            return;
        }
        if (this.mPid == Process.myPid()) {
            Slog.w("ActivityManager", "scheduleCrash: trying to crash system process!");
            return;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                this.mThread.scheduleCrash(str, i, bundle);
            } catch (RemoteException unused) {
                killLocked("scheduleCrash for '" + str + "' failed", 4, true);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void killLocked(String str, int i, boolean z) {
        killLocked(str, i, 0, z, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void killLocked(String str, int i, int i2, boolean z) {
        killLocked(str, str, i, i2, z, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void killLocked(String str, String str2, int i, int i2, boolean z) {
        killLocked(str, str2, i, i2, z, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void killLocked(String str, int i, int i2, boolean z, boolean z2) {
        killLocked(str, str, i, i2, z, z2);
    }

    @GuardedBy({"mService"})
    void killLocked(String str, String str2, int i, int i2, boolean z, boolean z2) {
        if (this.mKilledByAm) {
            return;
        }
        Trace.traceBegin(64L, "kill");
        if (i == 6 && this.mErrorState.getAnrAnnotation() != null) {
            str2 = str2 + ": " + this.mErrorState.getAnrAnnotation();
        }
        if (this.mService != null && (z || this.info.uid == this.mService.mCurOomAdjUid)) {
            this.mService.reportUidInfoMessageLocked("ActivityManager", "Killing " + toShortString() + " (adj " + this.mState.getSetAdj() + "): " + str, this.info.uid);
        }
        int threadGroupLeader = Process.getThreadGroupLeader(this.mPid);
        if (threadGroupLeader != this.mPid) {
            Slog.w("ActivityManager", "mPid = " + this.mPid + "tgid = " + threadGroupLeader + " is reused by others, skip kill [" + this.mPid + "]");
            return;
        }
        this.mOptRecord.setPendingFreeze(false);
        this.mOptRecord.setFrozen(false);
        if (this.mPid > 0) {
            this.mService.mProcessList.noteAppKill(this, i, i2, str2);
            this.mProcessRecordExt.saveAmKillRecordToList(System.currentTimeMillis(), this.mPid, this.processName, str);
            EventLog.writeEvent(EventLogTags.AM_KILL, Integer.valueOf(this.userId), Integer.valueOf(this.mPid), this.processName, Integer.valueOf(this.mState.getSetAdj()), str);
            Process.killProcessQuiet(this.mPid);
            killProcessGroupIfNecessaryLocked(z2);
        } else {
            this.mPendingStart = false;
        }
        if (!this.mPersistent) {
            ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    this.mKilled = true;
                    this.mKilledByAm = true;
                    this.mKillTime = SystemClock.uptimeMillis();
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterProcLockedSection();
        }
        this.mSocExt.killLocked(this.mService, this.mErrorState, this);
        Trace.traceEnd(64L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void killProcessGroupIfNecessaryLocked(boolean z) {
        boolean z2;
        boolean z3 = true;
        if (this.mHostingRecord != null && (this.mHostingRecord.usesWebviewZygote() || this.mHostingRecord.usesAppZygote())) {
            synchronized (this) {
                z2 = this.mProcessGroupCreated;
                if (!z2) {
                    this.mSkipProcessGroupCreation = true;
                }
            }
            z3 = z2;
        }
        if (z3) {
            if (z) {
                ProcessList.killProcessGroup(this.uid, this.mPid);
            } else {
                Process.sendSignalToProcessGroup(this.uid, this.mPid, OsConstants.SIGKILL);
            }
        }
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        dumpDebug(protoOutputStream, j, -1);
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j, int i) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1120986464257L, this.mPid);
        protoOutputStream.write(1138166333442L, this.processName);
        protoOutputStream.write(1120986464259L, this.info.uid);
        if (UserHandle.getAppId(this.info.uid) >= 10000) {
            protoOutputStream.write(1120986464260L, this.userId);
            protoOutputStream.write(1120986464261L, UserHandle.getAppId(this.info.uid));
        }
        if (this.uid != this.info.uid) {
            protoOutputStream.write(1120986464262L, UserHandle.getAppId(this.uid));
        }
        protoOutputStream.write(1133871366151L, this.mPersistent);
        if (i >= 0) {
            protoOutputStream.write(1120986464264L, i);
        }
        protoOutputStream.end(start);
    }

    public String toShortString() {
        String str = this.mShortStringName;
        if (str != null) {
            return str;
        }
        StringBuilder sb = new StringBuilder(128);
        toShortString(sb);
        String sb2 = sb.toString();
        this.mShortStringName = sb2;
        return sb2;
    }

    void toShortString(StringBuilder sb) {
        sb.append(this.mPid);
        sb.append(':');
        sb.append(this.processName);
        sb.append('/');
        if (this.info.uid < 10000) {
            sb.append(this.uid);
            return;
        }
        sb.append('u');
        sb.append(this.userId);
        int appId = UserHandle.getAppId(this.info.uid);
        if (appId >= 10000) {
            sb.append('a');
            sb.append(appId - 10000);
        } else {
            sb.append('s');
            sb.append(appId);
        }
        if (this.uid != this.info.uid) {
            sb.append('i');
            sb.append(UserHandle.getAppId(this.uid) - 99000);
        }
    }

    public String toString() {
        String str = this.mStringName;
        if (str != null) {
            return str;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append("ProcessRecord{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(' ');
        toShortString(sb);
        sb.append('}');
        String sb2 = sb.toString();
        this.mStringName = sb2;
        return sb2;
    }

    public boolean addPackage(String str, long j, ProcessStatsService processStatsService) {
        synchronized (processStatsService.mLock) {
            synchronized (this.mPkgList) {
                if (this.mPkgList.containsKey(str)) {
                    return false;
                }
                ProcessStats.ProcessStateHolder processStateHolder = new ProcessStats.ProcessStateHolder(j);
                ProcessState baseProcessTracker = this.mProfile.getBaseProcessTracker();
                if (baseProcessTracker != null) {
                    processStatsService.updateProcessStateHolderLocked(processStateHolder, str, this.info.uid, j, this.processName);
                    this.mPkgList.put(str, processStateHolder);
                    ProcessState processState = processStateHolder.state;
                    if (processState != baseProcessTracker) {
                        processState.makeActive();
                    }
                } else {
                    this.mPkgList.put(str, processStateHolder);
                }
                return true;
            }
        }
    }

    public void resetPackageList(ProcessStatsService processStatsService) {
        PackageList packageList;
        synchronized (processStatsService.mLock) {
            final ProcessState baseProcessTracker = this.mProfile.getBaseProcessTracker();
            PackageList packageList2 = this.mPkgList;
            try {
                synchronized (packageList2) {
                    try {
                        int size = this.mPkgList.size();
                        if (baseProcessTracker != null) {
                            baseProcessTracker.setState(-1, processStatsService.getMemFactorLocked(), SystemClock.uptimeMillis(), this.mPkgList.getPackageListLocked());
                            if (size != 1) {
                                this.mPkgList.forEachPackageProcessStats(new Consumer() { // from class: com.android.server.am.ProcessRecord$$ExternalSyntheticLambda0
                                    @Override // java.util.function.Consumer
                                    public final void accept(Object obj) {
                                        ProcessRecord.lambda$resetPackageList$0(baseProcessTracker, (ProcessStats.ProcessStateHolder) obj);
                                    }
                                });
                                this.mPkgList.clear();
                                ProcessStats.ProcessStateHolder processStateHolder = new ProcessStats.ProcessStateHolder(this.info.longVersionCode);
                                packageList = packageList2;
                                processStatsService.updateProcessStateHolderLocked(processStateHolder, this.info.packageName, this.info.uid, this.info.longVersionCode, this.processName);
                                this.mPkgList.put(this.info.packageName, processStateHolder);
                                ProcessState processState = processStateHolder.state;
                                if (processState != baseProcessTracker) {
                                    processState.makeActive();
                                }
                            } else {
                                packageList = packageList2;
                            }
                        } else {
                            packageList = packageList2;
                            if (size != 1) {
                                this.mPkgList.clear();
                                this.mPkgList.put(this.info.packageName, new ProcessStats.ProcessStateHolder(this.info.longVersionCode));
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$resetPackageList$0(ProcessState processState, ProcessStats.ProcessStateHolder processStateHolder) {
        ProcessState processState2 = processStateHolder.state;
        if (processState2 == null || processState2 == processState) {
            return;
        }
        processState2.makeInactive();
    }

    public String[] getPackageList() {
        return this.mPkgList.getPackageList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<VersionedPackage> getPackageListWithVersionCode() {
        return this.mPkgList.getPackageListWithVersionCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowProcessController getWindowProcessController() {
        return this.mWindowProcessController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addOrUpdateBackgroundStartPrivileges(Binder binder, BackgroundStartPrivileges backgroundStartPrivileges) {
        Objects.requireNonNull(binder, "entity");
        Objects.requireNonNull(backgroundStartPrivileges, "backgroundStartPrivileges");
        Preconditions.checkArgument(backgroundStartPrivileges.allowsAny(), "backgroundStartPrivileges does not allow anything");
        this.mWindowProcessController.addOrUpdateBackgroundStartPrivileges(binder, backgroundStartPrivileges);
        setBackgroundStartPrivileges(binder, backgroundStartPrivileges);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeBackgroundStartPrivileges(Binder binder) {
        Objects.requireNonNull(binder, "entity");
        this.mWindowProcessController.removeBackgroundStartPrivileges(binder);
        setBackgroundStartPrivileges(binder, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BackgroundStartPrivileges getBackgroundStartPrivileges() {
        BackgroundStartPrivileges backgroundStartPrivileges;
        synchronized (this.mBackgroundStartPrivileges) {
            if (this.mBackgroundStartPrivilegesMerged == null) {
                this.mBackgroundStartPrivilegesMerged = BackgroundStartPrivileges.NONE;
                for (int size = this.mBackgroundStartPrivileges.size() - 1; size >= 0; size--) {
                    this.mBackgroundStartPrivilegesMerged = this.mBackgroundStartPrivilegesMerged.merge(this.mBackgroundStartPrivileges.valueAt(size));
                }
            }
            backgroundStartPrivileges = this.mBackgroundStartPrivilegesMerged;
        }
        return backgroundStartPrivileges;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x001a, code lost:
    
        if (r6 != r4.mBackgroundStartPrivileges.put(r5, r6)) goto L12;
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x001e A[Catch: all -> 0x0023, TryCatch #0 {, blocks: (B:6:0x0007, B:10:0x001e, B:11:0x0021, B:16:0x0012), top: B:4:0x0005 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setBackgroundStartPrivileges(Binder binder, BackgroundStartPrivileges backgroundStartPrivileges) {
        synchronized (this.mBackgroundStartPrivileges) {
            boolean z = true;
            if (backgroundStartPrivileges == null) {
                if (this.mBackgroundStartPrivileges.remove(binder) != null) {
                    if (z) {
                        this.mBackgroundStartPrivilegesMerged = null;
                    }
                }
                z = false;
                if (z) {
                }
            }
        }
    }

    public void clearProfilerIfNeeded() {
        synchronized (this.mService.mAppProfiler.mProfilerLock) {
            this.mService.mAppProfiler.clearProfilerLPf();
        }
    }

    public void updateServiceConnectionActivities() {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mService.mServices.updateServiceConnectionActivitiesLocked(this.mServices);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    public void setPendingUiClean(boolean z) {
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                this.mProfile.setPendingUiClean(z);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    public void setPendingUiCleanAndForceProcessStateUpTo(int i) {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                setPendingUiClean(true);
                this.mState.forceProcessStateUpTo(i);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    public void updateProcessInfo(boolean z, boolean z2, boolean z3) {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            if (z) {
                try {
                    this.mService.mServices.updateServiceConnectionActivitiesLocked(this.mServices);
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            if (this.mThread == null) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                return;
            }
            this.mService.updateLruProcessLocked(this, z2, null);
            if (z3) {
                this.mService.updateOomAdjLocked(this, 1);
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }
    }

    public long getCpuTime() {
        return this.mService.mAppProfiler.getCpuTimeForPid(this.mPid);
    }

    public long getCpuDelayTime() {
        return this.mService.mAppProfiler.getCpuDelayTimeForPid(this.mPid);
    }

    public void onStartActivity(int i, boolean z, String str, long j) {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mWaitingToKill = null;
                if (z) {
                    synchronized (this.mService.mAppProfiler.mProfilerLock) {
                        this.mService.mAppProfiler.setProfileProcLPf(this);
                    }
                }
                if (str != null) {
                    addPackage(str, j, this.mService.mProcessStats);
                }
                updateProcessInfo(false, true, true);
                setPendingUiClean(true);
                this.mState.setHasShownUi(true);
                this.mState.forceProcessStateUpTo(i);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    public void appDied(String str) {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mService.appDiedLocked(this, str);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    public void setRunningRemoteAnimation(boolean z) {
        if (this.mPid == Process.myPid()) {
            Slog.wtf("ActivityManager", "system can't run remote animation");
            return;
        }
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                if (this.runningRemoteAnimation == z) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                this.mProcessRecordExt.callOrmsSetSceneActionForRemoteAnimation(z);
                this.runningRemoteAnimation = z;
                this.mState.setRunningRemoteAnimation(z);
                this.mUIFirstManagerExt.setTaskAsRemoteAnimationUx(this.mPid, this.mRenderThreadTid, this.hwuiTaskThreads, this.info.packageName, z);
                ActivityManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public long getInputDispatchingTimeoutMillis() {
        return this.mWindowProcessController.getInputDispatchingTimeoutMillis();
    }

    public int getProcessClassEnum() {
        if (this.mPid == ActivityManagerService.MY_PID) {
            return 3;
        }
        if (this.info == null) {
            return 0;
        }
        return (this.info.flags & 1) != 0 ? 2 : 1;
    }

    @VisibleForTesting
    List<ProcessRecord> getLruProcessList() {
        return this.mService.mProcessList.getLruProcessesLOSP();
    }

    public IProcessRecordWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class ProcessRecordWrapper implements IProcessRecordWrapper {
        private ProcessRecordWrapper() {
        }

        @Override // com.android.server.am.IProcessRecordWrapper
        public IProcessRecordExt getExtImpl() {
            return ProcessRecord.this.mProcessRecordExt;
        }

        @Override // com.android.server.am.IProcessRecordWrapper
        public int getRenderThreadTid() {
            return ProcessRecord.this.mRenderThreadTid;
        }

        @Override // com.android.server.am.IProcessRecordWrapper
        public IntArray getHwuiTaskThreads() {
            return ProcessRecord.this.hwuiTaskThreads;
        }

        private IProcessRecordSocExt getSocExtImpl() {
            return ProcessRecord.this.mSocExt;
        }

        @Override // com.android.server.am.IProcessRecordWrapper
        public long getLastActivityTime() {
            return ProcessRecord.this.mLastActivityTime;
        }
    }
}
