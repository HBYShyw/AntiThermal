package com.android.server.am;

import android.R;
import android.app.ActivityManager;
import android.app.ApplicationExitInfo;
import android.app.IAppTraceRetriever;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Binder;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.system.OsConstants;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.Pair;
import android.util.Pools;
import android.util.Slog;
import android.util.SparseArray;
import android.util.proto.ProtoInputStream;
import android.util.proto.ProtoOutputStream;
import android.util.proto.WireTypeMismatchException;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.ProcessMap;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.function.HexConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.IoThread;
import com.android.server.LocalServices;
import com.android.server.ServiceThread;
import com.android.server.SystemServiceManager;
import com.android.server.am.AppExitInfoTracker;
import com.android.server.os.NativeTombstoneManager;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.zip.GZIPOutputStream;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AppExitInfoTracker {

    @VisibleForTesting
    static final String APP_EXIT_INFO_FILE = "procexitinfo";
    private static final long APP_EXIT_INFO_PERSIST_INTERVAL = TimeUnit.MINUTES.toMillis(30);
    private static final long APP_EXIT_INFO_STATSD_LOG_DEBOUNCE = TimeUnit.SECONDS.toMillis(15);
    private static final int APP_EXIT_RAW_INFO_POOL_SIZE = 8;

    @VisibleForTesting
    static final String APP_EXIT_STORE_DIR = "procexitstore";
    private static final String APP_TRACE_FILE_SUFFIX = ".gz";
    private static final int FOREACH_ACTION_NONE = 0;
    private static final int FOREACH_ACTION_REMOVE_ITEM = 1;
    private static final int FOREACH_ACTION_STOP_ITERATION = 2;
    private static final String TAG = "ActivityManager";
    private int mAppExitInfoHistoryListSize;
    private KillHandler mKillHandler;

    @VisibleForTesting
    File mProcExitInfoFile;

    @VisibleForTesting
    File mProcExitStoreDir;
    private ActivityManagerService mService;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private Runnable mAppExitInfoPersistTask = null;

    @GuardedBy({"mLock"})
    private long mLastAppExitInfoPersistTimestamp = 0;

    @VisibleForTesting
    AtomicBoolean mAppExitInfoLoaded = new AtomicBoolean();

    @GuardedBy({"mLock"})
    final ArrayList<ApplicationExitInfo> mTmpInfoList = new ArrayList<>();

    @GuardedBy({"mLock"})
    final ArrayList<ApplicationExitInfo> mTmpInfoList2 = new ArrayList<>();
    final IsolatedUidRecords mIsolatedUidRecords = new IsolatedUidRecords();
    final AppExitInfoExternalSource mAppExitInfoSourceZygote = new AppExitInfoExternalSource("zygote", null);
    final AppExitInfoExternalSource mAppExitInfoSourceLmkd = new AppExitInfoExternalSource("lmkd", 3);

    @GuardedBy({"mLock"})
    final SparseArray<SparseArray<byte[]>> mActiveAppStateSummary = new SparseArray<>();

    @GuardedBy({"mLock"})
    final SparseArray<SparseArray<File>> mActiveAppTraces = new SparseArray<>();
    final AppTraceRetriever mAppTraceRetriever = new AppTraceRetriever();
    public IAppExitInfoTrackerExt mAppExitInfoTrackerExt = (IAppExitInfoTrackerExt) ExtLoader.type(IAppExitInfoTrackerExt.class).create();

    @GuardedBy({"mLock"})
    private final ProcessMap<AppExitInfoContainer> mData = new ProcessMap<>();

    @GuardedBy({"mLock"})
    private final Pools.SynchronizedPool<ApplicationExitInfo> mRawRecordsPool = new Pools.SynchronizedPool<>(8);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface LmkdKillListener {
        void onLmkdKillOccurred(int i, int i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(ActivityManagerService activityManagerService) {
        this.mService = activityManagerService;
        ServiceThread serviceThread = new ServiceThread("ActivityManager:killHandler", 10, true);
        serviceThread.start();
        this.mKillHandler = new KillHandler(serviceThread.getLooper());
        File file = new File(SystemServiceManager.ensureSystemDir(), APP_EXIT_STORE_DIR);
        this.mProcExitStoreDir = file;
        if (!FileUtils.createDir(file)) {
            Slog.e("ActivityManager", "Unable to create " + this.mProcExitStoreDir);
            return;
        }
        this.mProcExitInfoFile = new File(this.mProcExitStoreDir, APP_EXIT_INFO_FILE);
        this.mAppExitInfoHistoryListSize = activityManagerService.mContext.getResources().getInteger(R.integer.config_attentiveTimeout);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSystemReady() {
        registerForUserRemoval();
        registerForPackageRemoval();
        IoThread.getHandler().post(new Runnable() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                AppExitInfoTracker.this.lambda$onSystemReady$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$0() {
        SystemProperties.set("persist.sys.lmk.reportkills", Boolean.toString(SystemProperties.getBoolean("sys.lmk.reportkills", false)));
        loadExistingProcessExitInfo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleNoteProcessDied(ProcessRecord processRecord) {
        if (processRecord == null || processRecord.info == null || !this.mAppExitInfoLoaded.get()) {
            return;
        }
        ApplicationExitInfo obtainRawRecord = obtainRawRecord(processRecord, System.currentTimeMillis());
        this.mAppExitInfoTrackerExt.notifyOplusExitInfo(obtainRawRecord, processRecord);
        obtainRawRecord.setDescription(this.mAppExitInfoTrackerExt.updateExitInfoMsg(obtainRawRecord.getDescription(), processRecord));
        this.mAppExitInfoTrackerExt.removeProcessInfo(processRecord);
        this.mKillHandler.obtainMessage(4103, obtainRawRecord).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleNoteAppKill(final ProcessRecord processRecord, int i, int i2, String str) {
        if (!this.mAppExitInfoLoaded.get() || processRecord == null || processRecord.info == null) {
            return;
        }
        final ApplicationExitInfo obtainRawRecord = obtainRawRecord(processRecord, System.currentTimeMillis());
        obtainRawRecord.setReason(i);
        obtainRawRecord.setSubReason(i2);
        obtainRawRecord.setDescription(this.mAppExitInfoTrackerExt.updateExitInfoMsg(str, processRecord));
        this.mService.mHandler.post(new Runnable() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                AppExitInfoTracker.this.lambda$scheduleNoteAppKill$1(obtainRawRecord, processRecord);
            }
        });
        this.mKillHandler.obtainMessage(4104, obtainRawRecord).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleNoteAppKill$1(ApplicationExitInfo applicationExitInfo, ProcessRecord processRecord) {
        this.mAppExitInfoTrackerExt.notifyOplusExitInfo(applicationExitInfo, processRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleNoteAppRecoverableCrash(ProcessRecord processRecord) {
        if (!this.mAppExitInfoLoaded.get() || processRecord == null || processRecord.info == null) {
            return;
        }
        ApplicationExitInfo obtainRawRecord = obtainRawRecord(processRecord, System.currentTimeMillis());
        obtainRawRecord.setReason(5);
        obtainRawRecord.setSubReason(0);
        obtainRawRecord.setDescription("recoverable_crash");
        this.mKillHandler.obtainMessage(4106, obtainRawRecord).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleNoteAppKill(int i, int i2, int i3, int i4, String str) {
        ProcessRecord processRecord;
        if (this.mAppExitInfoLoaded.get()) {
            synchronized (this.mService.mPidsSelfLocked) {
                processRecord = this.mService.mPidsSelfLocked.get(i);
            }
            if (processRecord == null) {
                if (ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                    Slog.w("ActivityManager", "Skipping saving the kill reason for pid " + i + "(uid=" + i2 + ") since its process record is not found");
                    return;
                }
                return;
            }
            scheduleNoteAppKill(processRecord, i3, i4, str);
        }
    }

    void setLmkdKillListener(final LmkdKillListener lmkdKillListener) {
        synchronized (this.mLock) {
            this.mAppExitInfoSourceLmkd.setOnProcDiedListener(new BiConsumer() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda4
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    AppExitInfoTracker.lambda$setLmkdKillListener$2(AppExitInfoTracker.LmkdKillListener.this, (Integer) obj, (Integer) obj2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setLmkdKillListener$2(LmkdKillListener lmkdKillListener, Integer num, Integer num2) {
        lmkdKillListener.onLmkdKillOccurred(num.intValue(), num2.intValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleNoteLmkdProcKilled(int i, int i2) {
        this.mKillHandler.obtainMessage(4101, i, i2).sendToTarget();
    }

    private void scheduleChildProcDied(int i, int i2, int i3) {
        this.mKillHandler.obtainMessage(4102, i, i2, Integer.valueOf(i3)).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleZygoteSigChld(int i, int i2, int i3) {
        if (ActivityManagerDebugConfig.DEBUG_PROCESSES) {
            Slog.i("ActivityManager", "Got SIGCHLD from zygote: pid=" + i + ", uid=" + i2 + ", status=" + Integer.toHexString(i3));
        }
        scheduleChildProcDied(i, i2, i3);
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    void handleNoteProcessDiedLocked(ApplicationExitInfo applicationExitInfo) {
        if (applicationExitInfo != null) {
            if (ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                Slog.i("ActivityManager", "Update process exit info for " + applicationExitInfo.getPackageName() + "(" + applicationExitInfo.getPid() + "/u" + applicationExitInfo.getRealUid() + ")");
            }
            ApplicationExitInfo exitInfoLocked = getExitInfoLocked(applicationExitInfo.getPackageName(), applicationExitInfo.getPackageUid(), applicationExitInfo.getPid());
            Pair<Long, Object> remove = this.mAppExitInfoSourceZygote.remove(applicationExitInfo.getPid(), applicationExitInfo.getRealUid());
            Pair<Long, Object> remove2 = this.mAppExitInfoSourceLmkd.remove(applicationExitInfo.getPid(), applicationExitInfo.getRealUid());
            Pair<String, Pair<Integer, Integer>> removeAthenaKillRecord = this.mAppExitInfoTrackerExt.removeAthenaKillRecord(applicationExitInfo.getPid(), applicationExitInfo.getRealUid());
            this.mIsolatedUidRecords.removeIsolatedUidLocked(applicationExitInfo.getRealUid());
            if (exitInfoLocked == null) {
                exitInfoLocked = addExitInfoLocked(applicationExitInfo);
            }
            if (remove2 != null) {
                updateExistingExitInfoRecordLocked(exitInfoLocked, null, 3);
            } else if (removeAthenaKillRecord != null) {
                this.mAppExitInfoTrackerExt.updateApplicationExitInfo(exitInfoLocked, ((Integer) ((Pair) removeAthenaKillRecord.second).first).intValue(), ((Integer) ((Pair) removeAthenaKillRecord.second).second).intValue(), (String) removeAthenaKillRecord.first);
                scheduleLogToStatsdLocked(exitInfoLocked, true);
            } else {
                if (this.mAppExitInfoTrackerExt.updateKillReasonInfo(exitInfoLocked, remove != null ? (Integer) remove.second : null)) {
                    scheduleLogToStatsdLocked(exitInfoLocked, true);
                } else if (remove != null) {
                    updateExistingExitInfoRecordLocked(exitInfoLocked, (Integer) remove.second, null);
                } else {
                    scheduleLogToStatsdLocked(exitInfoLocked, false);
                }
            }
            this.mAppExitInfoTrackerExt.updateOplusExitInfo(exitInfoLocked);
            this.mAppExitInfoTrackerExt.notifyAppExitInfo(exitInfoLocked);
        }
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    void handleNoteAppKillLocked(ApplicationExitInfo applicationExitInfo) {
        ApplicationExitInfo exitInfoLocked = getExitInfoLocked(applicationExitInfo.getPackageName(), applicationExitInfo.getPackageUid(), applicationExitInfo.getPid());
        if (exitInfoLocked == null) {
            exitInfoLocked = addExitInfoLocked(applicationExitInfo);
        } else {
            exitInfoLocked.setReason(applicationExitInfo.getReason());
            exitInfoLocked.setSubReason(applicationExitInfo.getSubReason());
            exitInfoLocked.setStatus(0);
            exitInfoLocked.setTimestamp(System.currentTimeMillis());
            exitInfoLocked.setDescription(applicationExitInfo.getDescription());
        }
        scheduleLogToStatsdLocked(exitInfoLocked, true);
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    void handleNoteAppRecoverableCrashLocked(ApplicationExitInfo applicationExitInfo) {
        addExitInfoLocked(applicationExitInfo, true);
    }

    @GuardedBy({"mLock"})
    private ApplicationExitInfo addExitInfoLocked(ApplicationExitInfo applicationExitInfo) {
        return addExitInfoLocked(applicationExitInfo, false);
    }

    @GuardedBy({"mLock"})
    private ApplicationExitInfo addExitInfoLocked(ApplicationExitInfo applicationExitInfo, boolean z) {
        Integer uidByIsolatedUid;
        if (!this.mAppExitInfoLoaded.get()) {
            Slog.w("ActivityManager", "Skipping saving the exit info due to ongoing loading from storage");
            return null;
        }
        ApplicationExitInfo applicationExitInfo2 = new ApplicationExitInfo(applicationExitInfo);
        String[] packageList = applicationExitInfo.getPackageList();
        int realUid = applicationExitInfo.getRealUid();
        if (packageList != null) {
            if (UserHandle.isIsolated(realUid) && (uidByIsolatedUid = this.mIsolatedUidRecords.getUidByIsolatedUid(realUid)) != null) {
                realUid = uidByIsolatedUid.intValue();
            }
            for (String str : packageList) {
                addExitInfoInnerLocked(str, realUid, applicationExitInfo2, z);
            }
            if (Process.isSdkSandboxUid(realUid)) {
                for (String str2 : packageList) {
                    addExitInfoInnerLocked(str2, applicationExitInfo.getPackageUid(), applicationExitInfo2, z);
                }
            }
        }
        schedulePersistProcessExitInfo(false);
        return applicationExitInfo2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x006c, code lost:
    
        if (r6.intValue() == 3) goto L26;
     */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0060  */
    @GuardedBy({"mLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateExistingExitInfoRecordLocked(ApplicationExitInfo applicationExitInfo, Integer num, Integer num2) {
        boolean z;
        if (applicationExitInfo == null || !isFresh(applicationExitInfo.getTimestamp())) {
            return;
        }
        boolean z2 = true;
        if (num != null) {
            if (OsConstants.WIFEXITED(num.intValue())) {
                applicationExitInfo.setReason(1);
                applicationExitInfo.setStatus(OsConstants.WEXITSTATUS(num.intValue()));
            } else if (OsConstants.WIFSIGNALED(num.intValue())) {
                if (applicationExitInfo.getReason() == 0) {
                    applicationExitInfo.setReason(2);
                    applicationExitInfo.setStatus(OsConstants.WTERMSIG(num.intValue()));
                } else if (applicationExitInfo.getReason() == 5) {
                    applicationExitInfo.setStatus(OsConstants.WTERMSIG(num.intValue()));
                }
            }
            z = true;
            if (num2 != null) {
                applicationExitInfo.setReason(num2.intValue());
            }
            z2 = z;
            scheduleLogToStatsdLocked(applicationExitInfo, z2);
        }
        z = false;
        if (num2 != null) {
        }
        z2 = z;
        scheduleLogToStatsdLocked(applicationExitInfo, z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public boolean updateExitInfoIfNecessaryLocked(final int i, int i2, final Integer num, final Integer num2) {
        Integer uidByIsolatedUid = this.mIsolatedUidRecords.getUidByIsolatedUid(i2);
        if (uidByIsolatedUid != null) {
            i2 = uidByIsolatedUid.intValue();
        }
        final int i3 = i2;
        final ArrayList<ApplicationExitInfo> arrayList = this.mTmpInfoList;
        arrayList.clear();
        forEachPackageLocked(new BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda7
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                Integer lambda$updateExitInfoIfNecessaryLocked$3;
                lambda$updateExitInfoIfNecessaryLocked$3 = AppExitInfoTracker.this.lambda$updateExitInfoIfNecessaryLocked$3(i3, arrayList, i, num, num2, (String) obj, (SparseArray) obj2);
                return lambda$updateExitInfoIfNecessaryLocked$3;
            }
        });
        return arrayList.size() > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$updateExitInfoIfNecessaryLocked$3(int i, ArrayList arrayList, int i2, Integer num, Integer num2, String str, SparseArray sparseArray) {
        AppExitInfoContainer appExitInfoContainer = (AppExitInfoContainer) sparseArray.get(i);
        if (appExitInfoContainer == null) {
            return 0;
        }
        arrayList.clear();
        appExitInfoContainer.getExitInfoLocked(i2, 1, arrayList);
        if (arrayList.size() == 0) {
            return 0;
        }
        ApplicationExitInfo applicationExitInfo = (ApplicationExitInfo) arrayList.get(0);
        if (applicationExitInfo.getRealUid() != i) {
            arrayList.clear();
            return 0;
        }
        updateExistingExitInfoRecordLocked(applicationExitInfo, num, num2);
        this.mAppExitInfoTrackerExt.updateOplusExitInfo(applicationExitInfo);
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void getExitInfo(String str, final int i, final int i2, int i3, ArrayList<ApplicationExitInfo> arrayList) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                if (!TextUtils.isEmpty(str)) {
                    AppExitInfoContainer appExitInfoContainer = (AppExitInfoContainer) this.mData.get(str, i);
                    if (appExitInfoContainer != null) {
                        appExitInfoContainer.getExitInfoLocked(i2, i3, arrayList);
                    }
                } else {
                    final ArrayList<ApplicationExitInfo> arrayList2 = this.mTmpInfoList2;
                    arrayList2.clear();
                    forEachPackageLocked(new BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda2
                        @Override // java.util.function.BiFunction
                        public final Object apply(Object obj, Object obj2) {
                            Integer lambda$getExitInfo$4;
                            lambda$getExitInfo$4 = AppExitInfoTracker.this.lambda$getExitInfo$4(i, arrayList2, i2, (String) obj, (SparseArray) obj2);
                            return lambda$getExitInfo$4;
                        }
                    });
                    Collections.sort(arrayList2, new Comparator() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda3
                        @Override // java.util.Comparator
                        public final int compare(Object obj, Object obj2) {
                            int lambda$getExitInfo$5;
                            lambda$getExitInfo$5 = AppExitInfoTracker.lambda$getExitInfo$5((ApplicationExitInfo) obj, (ApplicationExitInfo) obj2);
                            return lambda$getExitInfo$5;
                        }
                    });
                    int size = arrayList2.size();
                    if (i3 > 0) {
                        size = Math.min(size, i3);
                    }
                    for (int i4 = 0; i4 < size; i4++) {
                        arrayList.add(arrayList2.get(i4));
                    }
                    arrayList2.clear();
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$getExitInfo$4(int i, ArrayList arrayList, int i2, String str, SparseArray sparseArray) {
        AppExitInfoContainer appExitInfoContainer = (AppExitInfoContainer) sparseArray.get(i);
        if (appExitInfoContainer != null) {
            this.mTmpInfoList.clear();
            arrayList.addAll(appExitInfoContainer.toListLocked(this.mTmpInfoList, i2));
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$getExitInfo$5(ApplicationExitInfo applicationExitInfo, ApplicationExitInfo applicationExitInfo2) {
        return Long.compare(applicationExitInfo2.getTimestamp(), applicationExitInfo.getTimestamp());
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public ApplicationExitInfo getExitInfoLocked(String str, int i, int i2) {
        ArrayList<ApplicationExitInfo> arrayList = this.mTmpInfoList;
        arrayList.clear();
        getExitInfo(str, i, i2, 1, arrayList);
        ApplicationExitInfo applicationExitInfo = arrayList.size() > 0 ? arrayList.get(0) : null;
        arrayList.clear();
        return applicationExitInfo;
    }

    @VisibleForTesting
    void onUserRemoved(int i) {
        this.mAppExitInfoSourceZygote.removeByUserId(i);
        this.mAppExitInfoSourceLmkd.removeByUserId(i);
        this.mIsolatedUidRecords.removeByUserId(i);
        synchronized (this.mLock) {
            this.mAppExitInfoTrackerExt.removeByUserId(i);
            removeByUserIdLocked(i);
            schedulePersistProcessExitInfo(true);
        }
    }

    @VisibleForTesting
    void onPackageRemoved(String str, int i, boolean z) {
        if (str != null) {
            boolean isEmpty = TextUtils.isEmpty(this.mService.mPackageManagerInt.getNameForUid(i));
            synchronized (this.mLock) {
                if (isEmpty) {
                    try {
                        this.mAppExitInfoSourceZygote.removeByUidLocked(i, z);
                        this.mAppExitInfoSourceLmkd.removeByUidLocked(i, z);
                        this.mAppExitInfoTrackerExt.removeByUid(i, UserHandle.isIsolated(i) ? this.mIsolatedUidRecords.getUidByIsolatedUid(i) : null, z);
                        this.mIsolatedUidRecords.removeAppUid(i, z);
                    } finally {
                    }
                }
                removePackageLocked(str, i, isEmpty, z ? -1 : UserHandle.getUserId(i));
                schedulePersistProcessExitInfo(true);
            }
        }
    }

    private void registerForUserRemoval() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_REMOVED");
        this.mService.mContext.registerReceiverForAllUsers(new BroadcastReceiver() { // from class: com.android.server.am.AppExitInfoTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -1);
                if (intExtra < 1) {
                    return;
                }
                AppExitInfoTracker.this.onUserRemoved(intExtra);
            }
        }, intentFilter, null, this.mKillHandler);
    }

    private void registerForPackageRemoval() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        intentFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_REMOVED@PACKAGE=NOREPLACING");
        this.mService.mContext.registerReceiverForAllUsers(new BroadcastReceiver() { // from class: com.android.server.am.AppExitInfoTracker.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                    return;
                }
                AppExitInfoTracker.this.onPackageRemoved(intent.getData().getSchemeSpecificPart(), intent.getIntExtra("android.intent.extra.UID", -10000), intent.getBooleanExtra("android.intent.extra.REMOVED_FOR_ALL_USERS", false));
            }
        }, intentFilter, null, this.mKillHandler);
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0049, code lost:
    
        if (r0 != null) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x006d, code lost:
    
        monitor-enter(r6.mLock);
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x006e, code lost:
    
        pruneAnrTracesIfNecessaryLocked();
        r6.mAppExitInfoLoaded.set(true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0077, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x004b, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0068, code lost:
    
        if (r0 == null) goto L34;
     */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void loadExistingProcessExitInfo() {
        if (!this.mProcExitInfoFile.canRead()) {
            this.mAppExitInfoLoaded.set(true);
            return;
        }
        FileInputStream fileInputStream = null;
        try {
            try {
                fileInputStream = new AtomicFile(this.mProcExitInfoFile).openRead();
                ProtoInputStream protoInputStream = new ProtoInputStream(fileInputStream);
                for (int nextField = protoInputStream.nextField(); nextField != -1; nextField = protoInputStream.nextField()) {
                    if (nextField == 1) {
                        synchronized (this.mLock) {
                            this.mLastAppExitInfoPersistTimestamp = protoInputStream.readLong(1112396529665L);
                        }
                    } else if (nextField == 2) {
                        loadPackagesFromProto(protoInputStream, nextField);
                    }
                }
            } catch (Exception e) {
                Slog.w("ActivityManager", "Error in loading historical app exit info from persistent storage: " + e);
            }
        } catch (Throwable th) {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException unused) {
                }
            }
            throw th;
        }
    }

    private void loadPackagesFromProto(ProtoInputStream protoInputStream, long j) throws IOException, WireTypeMismatchException {
        long start = protoInputStream.start(j);
        String str = "";
        int nextField = protoInputStream.nextField();
        while (nextField != -1) {
            if (nextField == 1) {
                str = protoInputStream.readString(1138166333441L);
            } else if (nextField != 2) {
                continue;
            } else {
                AppExitInfoContainer appExitInfoContainer = new AppExitInfoContainer(this.mAppExitInfoHistoryListSize);
                int readFromProto = appExitInfoContainer.readFromProto(protoInputStream, 2246267895810L);
                synchronized (this.mLock) {
                    this.mData.put(str, readFromProto, appExitInfoContainer);
                }
            }
            nextField = protoInputStream.nextField();
        }
        protoInputStream.end(start);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0055 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void persistProcessExitInfo() {
        FileOutputStream fileOutputStream;
        AtomicFile atomicFile = new AtomicFile(this.mProcExitInfoFile);
        long currentTimeMillis = System.currentTimeMillis();
        try {
            fileOutputStream = atomicFile.startWrite();
            try {
                final ProtoOutputStream protoOutputStream = new ProtoOutputStream(fileOutputStream);
                protoOutputStream.write(1112396529665L, currentTimeMillis);
                synchronized (this.mLock) {
                    forEachPackageLocked(new BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda19
                        @Override // java.util.function.BiFunction
                        public final Object apply(Object obj, Object obj2) {
                            Integer lambda$persistProcessExitInfo$6;
                            lambda$persistProcessExitInfo$6 = AppExitInfoTracker.lambda$persistProcessExitInfo$6(protoOutputStream, (String) obj, (SparseArray) obj2);
                            return lambda$persistProcessExitInfo$6;
                        }
                    });
                    this.mLastAppExitInfoPersistTimestamp = currentTimeMillis;
                }
                protoOutputStream.flush();
                atomicFile.finishWrite(fileOutputStream);
            } catch (IOException e) {
                e = e;
                Slog.w("ActivityManager", "Unable to write historical app exit info into persistent storage: " + e);
                atomicFile.failWrite(fileOutputStream);
                synchronized (this.mLock) {
                }
            }
        } catch (IOException e2) {
            e = e2;
            fileOutputStream = null;
        }
        synchronized (this.mLock) {
            this.mAppExitInfoPersistTask = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$persistProcessExitInfo$6(ProtoOutputStream protoOutputStream, String str, SparseArray sparseArray) {
        long start = protoOutputStream.start(2246267895810L);
        protoOutputStream.write(1138166333441L, str);
        int size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            ((AppExitInfoContainer) sparseArray.valueAt(i)).writeToProto(protoOutputStream, 2246267895810L);
        }
        protoOutputStream.end(start);
        return 0;
    }

    @VisibleForTesting
    void schedulePersistProcessExitInfo(boolean z) {
        synchronized (this.mLock) {
            Runnable runnable = this.mAppExitInfoPersistTask;
            if (runnable == null || z) {
                if (runnable != null) {
                    IoThread.getHandler().removeCallbacks(this.mAppExitInfoPersistTask);
                }
                this.mAppExitInfoPersistTask = new Runnable() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        AppExitInfoTracker.this.persistProcessExitInfo();
                    }
                };
                IoThread.getHandler().postDelayed(this.mAppExitInfoPersistTask, z ? 0L : APP_EXIT_INFO_PERSIST_INTERVAL);
            }
        }
    }

    @VisibleForTesting
    void clearProcessExitInfo(boolean z) {
        File file;
        synchronized (this.mLock) {
            if (this.mAppExitInfoPersistTask != null) {
                IoThread.getHandler().removeCallbacks(this.mAppExitInfoPersistTask);
                this.mAppExitInfoPersistTask = null;
            }
            if (z && (file = this.mProcExitInfoFile) != null) {
                file.delete();
            }
            this.mData.getMap().clear();
            this.mActiveAppStateSummary.clear();
            this.mActiveAppTraces.clear();
            pruneAnrTracesIfNecessaryLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearHistoryProcessExitInfo(String str, int i) {
        NativeTombstoneManager nativeTombstoneManager = (NativeTombstoneManager) LocalServices.getService(NativeTombstoneManager.class);
        Optional empty = Optional.empty();
        if (TextUtils.isEmpty(str)) {
            synchronized (this.mLock) {
                removeByUserIdLocked(i);
            }
        } else {
            int packageUid = this.mService.mPackageManagerInt.getPackageUid(str, 131072L, i);
            Optional of = Optional.of(Integer.valueOf(UserHandle.getAppId(packageUid)));
            synchronized (this.mLock) {
                removePackageLocked(str, packageUid, true, i);
            }
            empty = of;
        }
        nativeTombstoneManager.purge(Optional.of(Integer.valueOf(i)), empty);
        schedulePersistProcessExitInfo(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpHistoryProcessExitInfo(final PrintWriter printWriter, String str) {
        printWriter.println("ACTIVITY MANAGER PROCESS EXIT INFO (dumpsys activity exit-info)");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        synchronized (this.mLock) {
            printWriter.println("Last Timestamp of Persistence Into Persistent Storage: " + simpleDateFormat.format(new Date(this.mLastAppExitInfoPersistTimestamp)));
            if (TextUtils.isEmpty(str)) {
                forEachPackageLocked(new BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda11
                    @Override // java.util.function.BiFunction
                    public final Object apply(Object obj, Object obj2) {
                        Integer lambda$dumpHistoryProcessExitInfo$7;
                        lambda$dumpHistoryProcessExitInfo$7 = AppExitInfoTracker.this.lambda$dumpHistoryProcessExitInfo$7(printWriter, simpleDateFormat, (String) obj, (SparseArray) obj2);
                        return lambda$dumpHistoryProcessExitInfo$7;
                    }
                });
            } else {
                SparseArray<AppExitInfoContainer> sparseArray = (SparseArray) this.mData.getMap().get(str);
                if (sparseArray != null) {
                    dumpHistoryProcessExitInfoLocked(printWriter, "  ", str, sparseArray, simpleDateFormat);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$dumpHistoryProcessExitInfo$7(PrintWriter printWriter, SimpleDateFormat simpleDateFormat, String str, SparseArray sparseArray) {
        dumpHistoryProcessExitInfoLocked(printWriter, "  ", str, sparseArray, simpleDateFormat);
        return 0;
    }

    @GuardedBy({"mLock"})
    private void dumpHistoryProcessExitInfoLocked(PrintWriter printWriter, String str, String str2, SparseArray<AppExitInfoContainer> sparseArray, SimpleDateFormat simpleDateFormat) {
        printWriter.println(str + "package: " + str2);
        int size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            printWriter.println(str + "  Historical Process Exit for uid=" + sparseArray.keyAt(i));
            sparseArray.valueAt(i).dumpLocked(printWriter, str + "    ", simpleDateFormat);
        }
    }

    @GuardedBy({"mLock"})
    private void addExitInfoInnerLocked(String str, int i, ApplicationExitInfo applicationExitInfo, boolean z) {
        AppExitInfoContainer appExitInfoContainer = (AppExitInfoContainer) this.mData.get(str, i);
        if (appExitInfoContainer == null) {
            appExitInfoContainer = new AppExitInfoContainer(this.mAppExitInfoHistoryListSize);
            if (UserHandle.isIsolated(applicationExitInfo.getRealUid())) {
                Integer uidByIsolatedUid = this.mIsolatedUidRecords.getUidByIsolatedUid(applicationExitInfo.getRealUid());
                if (uidByIsolatedUid != null) {
                    appExitInfoContainer.mUid = uidByIsolatedUid.intValue();
                }
            } else {
                appExitInfoContainer.mUid = applicationExitInfo.getRealUid();
            }
            this.mData.put(str, i, appExitInfoContainer);
        }
        if (z) {
            appExitInfoContainer.addRecoverableCrashLocked(applicationExitInfo);
        } else {
            appExitInfoContainer.addExitInfoLocked(applicationExitInfo);
        }
    }

    @GuardedBy({"mLock"})
    private void scheduleLogToStatsdLocked(ApplicationExitInfo applicationExitInfo, boolean z) {
        if (applicationExitInfo.isLoggedInStatsd()) {
            return;
        }
        if (z) {
            this.mKillHandler.removeMessages(4105, applicationExitInfo);
            performLogToStatsdLocked(applicationExitInfo);
        } else {
            if (this.mKillHandler.hasMessages(4105, applicationExitInfo)) {
                return;
            }
            KillHandler killHandler = this.mKillHandler;
            killHandler.sendMessageDelayed(killHandler.obtainMessage(4105, applicationExitInfo), APP_EXIT_INFO_STATSD_LOG_DEBOUNCE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void performLogToStatsdLocked(ApplicationExitInfo applicationExitInfo) {
        if (applicationExitInfo.isLoggedInStatsd()) {
            return;
        }
        applicationExitInfo.setLoggedInStatsd(true);
        String packageName = applicationExitInfo.getPackageName();
        String processName = applicationExitInfo.getProcessName();
        if (TextUtils.equals(packageName, processName)) {
            processName = null;
        } else if (processName != null && packageName != null && processName.startsWith(packageName)) {
            processName = processName.substring(packageName.length());
        }
        FrameworkStatsLog.write(FrameworkStatsLog.APP_PROCESS_DIED, applicationExitInfo.getPackageUid(), processName, applicationExitInfo.getReason(), applicationExitInfo.getSubReason(), applicationExitInfo.getImportance(), (int) applicationExitInfo.getPss(), (int) applicationExitInfo.getRss(), applicationExitInfo.hasForegroundServices());
    }

    @GuardedBy({"mLock"})
    private void forEachPackageLocked(BiFunction<String, SparseArray<AppExitInfoContainer>, Integer> biFunction) {
        if (biFunction != null) {
            ArrayMap map = this.mData.getMap();
            int size = map.size() - 1;
            while (size >= 0) {
                int intValue = biFunction.apply((String) map.keyAt(size), (SparseArray) map.valueAt(size)).intValue();
                if (intValue == 1) {
                    SparseArray sparseArray = (SparseArray) map.valueAt(size);
                    for (int size2 = sparseArray.size() - 1; size2 >= 0; size2--) {
                        ((AppExitInfoContainer) sparseArray.valueAt(size2)).destroyLocked();
                    }
                    map.removeAt(size);
                } else if (intValue == 2) {
                    size = 0;
                }
                size--;
            }
        }
    }

    @GuardedBy({"mLock"})
    private void removePackageLocked(String str, int i, boolean z, int i2) {
        if (z) {
            this.mActiveAppStateSummary.remove(i);
            int indexOfKey = this.mActiveAppTraces.indexOfKey(i);
            if (indexOfKey >= 0) {
                SparseArray<File> valueAt = this.mActiveAppTraces.valueAt(indexOfKey);
                for (int size = valueAt.size() - 1; size >= 0; size--) {
                    valueAt.valueAt(size).delete();
                }
                this.mActiveAppTraces.removeAt(indexOfKey);
            }
        }
        ArrayMap map = this.mData.getMap();
        SparseArray sparseArray = (SparseArray) map.get(str);
        if (sparseArray == null) {
            return;
        }
        if (i2 == -1) {
            for (int size2 = sparseArray.size() - 1; size2 >= 0; size2--) {
                ((AppExitInfoContainer) sparseArray.valueAt(size2)).destroyLocked();
            }
            this.mData.getMap().remove(str);
            return;
        }
        int size3 = sparseArray.size() - 1;
        while (true) {
            if (size3 < 0) {
                break;
            }
            if (UserHandle.getUserId(sparseArray.keyAt(size3)) == i2) {
                ((AppExitInfoContainer) sparseArray.valueAt(size3)).destroyLocked();
                sparseArray.removeAt(size3);
                break;
            }
            size3--;
        }
        if (sparseArray.size() == 0) {
            map.remove(str);
        }
    }

    @GuardedBy({"mLock"})
    private void removeByUserIdLocked(final int i) {
        if (i == -1) {
            this.mData.getMap().clear();
            this.mActiveAppStateSummary.clear();
            this.mActiveAppTraces.clear();
            pruneAnrTracesIfNecessaryLocked();
            return;
        }
        removeFromSparse2dArray(this.mActiveAppStateSummary, new Predicate() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$removeByUserIdLocked$8;
                lambda$removeByUserIdLocked$8 = AppExitInfoTracker.lambda$removeByUserIdLocked$8(i, (Integer) obj);
                return lambda$removeByUserIdLocked$8;
            }
        }, null, null);
        removeFromSparse2dArray(this.mActiveAppTraces, new Predicate() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$removeByUserIdLocked$9;
                lambda$removeByUserIdLocked$9 = AppExitInfoTracker.lambda$removeByUserIdLocked$9(i, (Integer) obj);
                return lambda$removeByUserIdLocked$9;
            }
        }, null, new Consumer() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((File) obj).delete();
            }
        });
        forEachPackageLocked(new BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda15
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                Integer lambda$removeByUserIdLocked$11;
                lambda$removeByUserIdLocked$11 = AppExitInfoTracker.lambda$removeByUserIdLocked$11(i, (String) obj, (SparseArray) obj2);
                return lambda$removeByUserIdLocked$11;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeByUserIdLocked$8(int i, Integer num) {
        return UserHandle.getUserId(num.intValue()) == i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeByUserIdLocked$9(int i, Integer num) {
        return UserHandle.getUserId(num.intValue()) == i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$removeByUserIdLocked$11(int i, String str, SparseArray sparseArray) {
        int size = sparseArray.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            if (UserHandle.getUserId(sparseArray.keyAt(size)) == i) {
                ((AppExitInfoContainer) sparseArray.valueAt(size)).destroyLocked();
                sparseArray.removeAt(size);
                break;
            }
            size--;
        }
        return Integer.valueOf(sparseArray.size() != 0 ? 0 : 1);
    }

    @VisibleForTesting
    ApplicationExitInfo obtainRawRecord(ProcessRecord processRecord, long j) {
        ApplicationExitInfo applicationExitInfo = (ApplicationExitInfo) this.mRawRecordsPool.acquire();
        if (applicationExitInfo == null) {
            applicationExitInfo = new ApplicationExitInfo();
        }
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mService.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                int definingUid = processRecord.getHostingRecord() != null ? processRecord.getHostingRecord().getDefiningUid() : 0;
                applicationExitInfo.setPid(processRecord.getPid());
                applicationExitInfo.setRealUid(processRecord.uid);
                applicationExitInfo.setPackageUid(processRecord.info.uid);
                if (definingUid <= 0) {
                    definingUid = processRecord.info.uid;
                }
                applicationExitInfo.setDefiningUid(definingUid);
                applicationExitInfo.setProcessName(processRecord.processName);
                applicationExitInfo.setConnectionGroup(processRecord.mServices.getConnectionGroup());
                applicationExitInfo.setPackageName(processRecord.info.packageName);
                applicationExitInfo.setPackageList(processRecord.getPackageList());
                applicationExitInfo.setReason(0);
                applicationExitInfo.setSubReason(0);
                applicationExitInfo.setStatus(0);
                applicationExitInfo.setImportance(ActivityManager.RunningAppProcessInfo.procStateToImportance(processRecord.mState.getReportedProcState()));
                applicationExitInfo.setPss(processRecord.mProfile.getLastPss());
                applicationExitInfo.setRss(processRecord.mProfile.getLastRss());
                applicationExitInfo.setTimestamp(j);
                applicationExitInfo.setHasForegroundServices(processRecord.mServices.hasReportedForegroundServices());
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
        return applicationExitInfo;
    }

    @VisibleForTesting
    void recycleRawRecord(ApplicationExitInfo applicationExitInfo) {
        applicationExitInfo.setProcessName(null);
        applicationExitInfo.setDescription(null);
        applicationExitInfo.setPackageList(null);
        this.mRawRecordsPool.release(applicationExitInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void setProcessStateSummary(int i, int i2, byte[] bArr) {
        synchronized (this.mLock) {
            Integer uidByIsolatedUid = this.mIsolatedUidRecords.getUidByIsolatedUid(i);
            if (uidByIsolatedUid != null) {
                i = uidByIsolatedUid.intValue();
            }
            putToSparse2dArray(this.mActiveAppStateSummary, i, i2, bArr, new AppExitInfoTracker$$ExternalSyntheticLambda0(), null);
        }
    }

    @VisibleForTesting
    byte[] getProcessStateSummary(int i, int i2) {
        synchronized (this.mLock) {
            Integer uidByIsolatedUid = this.mIsolatedUidRecords.getUidByIsolatedUid(i);
            if (uidByIsolatedUid != null) {
                i = uidByIsolatedUid.intValue();
            }
            int indexOfKey = this.mActiveAppStateSummary.indexOfKey(i);
            if (indexOfKey < 0) {
                return null;
            }
            return this.mActiveAppStateSummary.valueAt(indexOfKey).get(i2);
        }
    }

    public void scheduleLogAnrTrace(int i, int i2, String[] strArr, File file, long j, long j2) {
        this.mKillHandler.sendMessage(PooledLambda.obtainMessage(new HexConsumer() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda16
            public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
                AppExitInfoTracker.this.handleLogAnrTrace(((Integer) obj).intValue(), ((Integer) obj2).intValue(), (String[]) obj3, (File) obj4, ((Long) obj5).longValue(), ((Long) obj6).longValue());
            }
        }, Integer.valueOf(i), Integer.valueOf(i2), strArr, file, Long.valueOf(j), Long.valueOf(j2)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void handleLogAnrTrace(int i, int i2, String[] strArr, File file, long j, long j2) {
        if (!file.exists() || ArrayUtils.isEmpty(strArr)) {
            return;
        }
        long length = file.length();
        long j3 = j2 - j;
        if (j >= length || j2 > length || j3 <= 0) {
            return;
        }
        File file2 = new File(this.mProcExitStoreDir, file.getName() + APP_TRACE_FILE_SUFFIX);
        if (copyToGzFile(file, file2, j, j3)) {
            synchronized (this.mLock) {
                Integer uidByIsolatedUid = this.mIsolatedUidRecords.getUidByIsolatedUid(i2);
                int intValue = uidByIsolatedUid != null ? uidByIsolatedUid.intValue() : i2;
                if (ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                    Slog.i("ActivityManager", "Stored ANR traces of " + i + "/u" + intValue + " in " + file2);
                }
                boolean z = true;
                for (String str : strArr) {
                    AppExitInfoContainer appExitInfoContainer = (AppExitInfoContainer) this.mData.get(str, intValue);
                    if (appExitInfoContainer != null && appExitInfoContainer.appendTraceIfNecessaryLocked(i, file2)) {
                        z = false;
                    }
                }
                if (z) {
                    putToSparse2dArray(this.mActiveAppTraces, intValue, i, file2, new AppExitInfoTracker$$ExternalSyntheticLambda0(), new Consumer() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            ((File) obj).delete();
                        }
                    });
                }
            }
        }
    }

    private static boolean copyToGzFile(File file, File file2, long j, long j2) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            try {
                GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(file2)));
                try {
                    byte[] bArr = new byte[8192];
                    bufferedInputStream.skip(j);
                    while (j2 > 0) {
                        int read = bufferedInputStream.read(bArr, 0, (int) Math.min(8192, j2));
                        if (read < 0) {
                            break;
                        }
                        gZIPOutputStream.write(bArr, 0, read);
                        j2 -= read;
                    }
                    gZIPOutputStream.close();
                    bufferedInputStream.close();
                    return j2 == 0 && file2.exists();
                } finally {
                }
            } finally {
            }
        } catch (IOException e) {
            if (ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                Slog.e("ActivityManager", "Error in copying ANR trace from " + file + " to " + file2, e);
            }
            return false;
        }
    }

    @GuardedBy({"mLock"})
    private void pruneAnrTracesIfNecessaryLocked() {
        final ArraySet arraySet = new ArraySet();
        if (ArrayUtils.isEmpty(this.mProcExitStoreDir.listFiles(new FileFilter() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda8
            @Override // java.io.FileFilter
            public final boolean accept(File file) {
                boolean lambda$pruneAnrTracesIfNecessaryLocked$13;
                lambda$pruneAnrTracesIfNecessaryLocked$13 = AppExitInfoTracker.lambda$pruneAnrTracesIfNecessaryLocked$13(arraySet, file);
                return lambda$pruneAnrTracesIfNecessaryLocked$13;
            }
        }))) {
            return;
        }
        forEachPackageLocked(new BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda9
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                Integer lambda$pruneAnrTracesIfNecessaryLocked$15;
                lambda$pruneAnrTracesIfNecessaryLocked$15 = AppExitInfoTracker.lambda$pruneAnrTracesIfNecessaryLocked$15(arraySet, (String) obj, (SparseArray) obj2);
                return lambda$pruneAnrTracesIfNecessaryLocked$15;
            }
        });
        forEachSparse2dArray(this.mActiveAppTraces, new Consumer() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AppExitInfoTracker.lambda$pruneAnrTracesIfNecessaryLocked$16(arraySet, (File) obj);
            }
        });
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            new File(this.mProcExitStoreDir, (String) arraySet.valueAt(size)).delete();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$pruneAnrTracesIfNecessaryLocked$13(ArraySet arraySet, File file) {
        String name = file.getName();
        boolean z = name.startsWith("anr_") && name.endsWith(APP_TRACE_FILE_SUFFIX);
        if (z) {
            arraySet.add(name);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$pruneAnrTracesIfNecessaryLocked$15(final ArraySet arraySet, String str, SparseArray sparseArray) {
        for (int size = sparseArray.size() - 1; size >= 0; size--) {
            ((AppExitInfoContainer) sparseArray.valueAt(size)).forEachRecordLocked(new BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda5
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Integer lambda$pruneAnrTracesIfNecessaryLocked$14;
                    lambda$pruneAnrTracesIfNecessaryLocked$14 = AppExitInfoTracker.lambda$pruneAnrTracesIfNecessaryLocked$14(arraySet, (Integer) obj, (ApplicationExitInfo) obj2);
                    return lambda$pruneAnrTracesIfNecessaryLocked$14;
                }
            });
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$pruneAnrTracesIfNecessaryLocked$14(ArraySet arraySet, Integer num, ApplicationExitInfo applicationExitInfo) {
        File traceFile = applicationExitInfo.getTraceFile();
        if (traceFile != null) {
            arraySet.remove(traceFile.getName());
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$pruneAnrTracesIfNecessaryLocked$16(ArraySet arraySet, File file) {
        arraySet.remove(file.getName());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T extends SparseArray<U>, U> void putToSparse2dArray(SparseArray<T> sparseArray, int i, int i2, U u, Supplier<T> supplier, Consumer<U> consumer) {
        T valueAt;
        int indexOfKey = sparseArray.indexOfKey(i);
        if (indexOfKey < 0) {
            valueAt = supplier.get();
            sparseArray.put(i, valueAt);
        } else {
            valueAt = sparseArray.valueAt(indexOfKey);
        }
        int indexOfKey2 = valueAt.indexOfKey(i2);
        if (indexOfKey2 >= 0) {
            if (consumer != 0) {
                consumer.accept(valueAt.valueAt(indexOfKey2));
            }
            valueAt.setValueAt(indexOfKey2, u);
            return;
        }
        valueAt.put(i2, u);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T extends SparseArray<U>, U> void forEachSparse2dArray(SparseArray<T> sparseArray, Consumer<U> consumer) {
        if (consumer != 0) {
            for (int size = sparseArray.size() - 1; size >= 0; size--) {
                T valueAt = sparseArray.valueAt(size);
                if (valueAt != null) {
                    for (int size2 = valueAt.size() - 1; size2 >= 0; size2--) {
                        consumer.accept(valueAt.valueAt(size2));
                    }
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T extends SparseArray<U>, U> void removeFromSparse2dArray(SparseArray<T> sparseArray, Predicate<Integer> predicate, Predicate<Integer> predicate2, Consumer<U> consumer) {
        T valueAt;
        for (int size = sparseArray.size() - 1; size >= 0; size--) {
            if ((predicate == null || predicate.test(Integer.valueOf(sparseArray.keyAt(size)))) && (valueAt = sparseArray.valueAt(size)) != null) {
                for (int size2 = valueAt.size() - 1; size2 >= 0; size2--) {
                    if (predicate2 == null || predicate2.test(Integer.valueOf(valueAt.keyAt(size2)))) {
                        if (consumer != 0) {
                            consumer.accept(valueAt.valueAt(size2));
                        }
                        valueAt.removeAt(size2);
                    }
                }
                if (valueAt.size() == 0) {
                    sparseArray.removeAt(size);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends SparseArray<U>, U> U findAndRemoveFromSparse2dArray(SparseArray<T> sparseArray, int i, int i2) {
        int indexOfKey = sparseArray.indexOfKey(i);
        U u = null;
        if (indexOfKey >= 0) {
            T valueAt = sparseArray.valueAt(indexOfKey);
            if (valueAt == null) {
                return null;
            }
            int indexOfKey2 = valueAt.indexOfKey(i2);
            if (indexOfKey2 >= 0) {
                u = (U) valueAt.valueAt(indexOfKey2);
                valueAt.removeAt(indexOfKey2);
                if (valueAt.size() == 0) {
                    sparseArray.removeAt(indexOfKey);
                }
            }
        }
        return u;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class AppExitInfoContainer {
        private int mMaxCapacity;
        private int mUid;
        private SparseArray<ApplicationExitInfo> mInfos = new SparseArray<>();
        private SparseArray<ApplicationExitInfo> mRecoverableCrashes = new SparseArray<>();

        AppExitInfoContainer(int i) {
            this.mMaxCapacity = i;
        }

        @GuardedBy({"mLock"})
        void getInfosLocked(SparseArray<ApplicationExitInfo> sparseArray, int i, int i2, ArrayList<ApplicationExitInfo> arrayList) {
            if (i > 0) {
                ApplicationExitInfo applicationExitInfo = sparseArray.get(i);
                if (applicationExitInfo != null) {
                    arrayList.add(applicationExitInfo);
                    return;
                }
                return;
            }
            int size = sparseArray.size();
            int i3 = 0;
            if (i2 <= 0 || size <= i2) {
                while (i3 < size) {
                    arrayList.add(sparseArray.valueAt(i3));
                    i3++;
                }
                Collections.sort(arrayList, new Comparator() { // from class: com.android.server.am.AppExitInfoTracker$AppExitInfoContainer$$ExternalSyntheticLambda0
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int lambda$getInfosLocked$0;
                        lambda$getInfosLocked$0 = AppExitInfoTracker.AppExitInfoContainer.lambda$getInfosLocked$0((ApplicationExitInfo) obj, (ApplicationExitInfo) obj2);
                        return lambda$getInfosLocked$0;
                    }
                });
                return;
            }
            if (i2 == 1) {
                ApplicationExitInfo valueAt = sparseArray.valueAt(0);
                for (int i4 = 1; i4 < size; i4++) {
                    ApplicationExitInfo valueAt2 = sparseArray.valueAt(i4);
                    if (valueAt.getTimestamp() < valueAt2.getTimestamp()) {
                        valueAt = valueAt2;
                    }
                }
                arrayList.add(valueAt);
                return;
            }
            ArrayList<ApplicationExitInfo> arrayList2 = AppExitInfoTracker.this.mTmpInfoList2;
            arrayList2.clear();
            for (int i5 = 0; i5 < size; i5++) {
                arrayList2.add(sparseArray.valueAt(i5));
            }
            Collections.sort(arrayList2, new Comparator() { // from class: com.android.server.am.AppExitInfoTracker$AppExitInfoContainer$$ExternalSyntheticLambda1
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$getInfosLocked$1;
                    lambda$getInfosLocked$1 = AppExitInfoTracker.AppExitInfoContainer.lambda$getInfosLocked$1((ApplicationExitInfo) obj, (ApplicationExitInfo) obj2);
                    return lambda$getInfosLocked$1;
                }
            });
            while (i3 < i2) {
                arrayList.add(arrayList2.get(i3));
                i3++;
            }
            arrayList2.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int lambda$getInfosLocked$0(ApplicationExitInfo applicationExitInfo, ApplicationExitInfo applicationExitInfo2) {
            return Long.compare(applicationExitInfo2.getTimestamp(), applicationExitInfo.getTimestamp());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int lambda$getInfosLocked$1(ApplicationExitInfo applicationExitInfo, ApplicationExitInfo applicationExitInfo2) {
            return Long.compare(applicationExitInfo2.getTimestamp(), applicationExitInfo.getTimestamp());
        }

        @GuardedBy({"mLock"})
        void getExitInfoLocked(int i, int i2, ArrayList<ApplicationExitInfo> arrayList) {
            getInfosLocked(this.mInfos, i, i2, arrayList);
        }

        @GuardedBy({"mLock"})
        void addInfoLocked(SparseArray<ApplicationExitInfo> sparseArray, ApplicationExitInfo applicationExitInfo) {
            int size = sparseArray.size();
            if (size >= this.mMaxCapacity) {
                int i = -1;
                long j = Long.MAX_VALUE;
                for (int i2 = 0; i2 < size; i2++) {
                    ApplicationExitInfo valueAt = sparseArray.valueAt(i2);
                    if (valueAt.getTimestamp() < j) {
                        j = valueAt.getTimestamp();
                        i = i2;
                    }
                }
                if (i >= 0) {
                    File traceFile = sparseArray.valueAt(i).getTraceFile();
                    if (traceFile != null) {
                        traceFile.delete();
                    }
                    sparseArray.removeAt(i);
                }
            }
            int packageUid = applicationExitInfo.getPackageUid();
            if (Process.isSdkSandboxUid(applicationExitInfo.getRealUid())) {
                packageUid = applicationExitInfo.getRealUid();
            }
            int pid = applicationExitInfo.getPid();
            if (applicationExitInfo.getProcessStateSummary() == null) {
                applicationExitInfo.setProcessStateSummary((byte[]) AppExitInfoTracker.findAndRemoveFromSparse2dArray(AppExitInfoTracker.this.mActiveAppStateSummary, packageUid, pid));
            }
            if (applicationExitInfo.getTraceFile() == null) {
                applicationExitInfo.setTraceFile((File) AppExitInfoTracker.findAndRemoveFromSparse2dArray(AppExitInfoTracker.this.mActiveAppTraces, packageUid, pid));
            }
            applicationExitInfo.setAppTraceRetriever(AppExitInfoTracker.this.mAppTraceRetriever);
            sparseArray.append(pid, applicationExitInfo);
        }

        @GuardedBy({"mLock"})
        void addExitInfoLocked(ApplicationExitInfo applicationExitInfo) {
            addInfoLocked(this.mInfos, applicationExitInfo);
        }

        @GuardedBy({"mLock"})
        void addRecoverableCrashLocked(ApplicationExitInfo applicationExitInfo) {
            addInfoLocked(this.mRecoverableCrashes, applicationExitInfo);
        }

        @GuardedBy({"mLock"})
        boolean appendTraceIfNecessaryLocked(int i, File file) {
            ApplicationExitInfo applicationExitInfo = this.mInfos.get(i);
            if (applicationExitInfo == null) {
                return false;
            }
            applicationExitInfo.setTraceFile(file);
            applicationExitInfo.setAppTraceRetriever(AppExitInfoTracker.this.mAppTraceRetriever);
            return true;
        }

        @GuardedBy({"mLock"})
        void destroyLocked(SparseArray<ApplicationExitInfo> sparseArray) {
            for (int size = sparseArray.size() - 1; size >= 0; size--) {
                ApplicationExitInfo valueAt = sparseArray.valueAt(size);
                File traceFile = valueAt.getTraceFile();
                if (traceFile != null) {
                    traceFile.delete();
                }
                valueAt.setTraceFile(null);
                valueAt.setAppTraceRetriever(null);
            }
        }

        @GuardedBy({"mLock"})
        void destroyLocked() {
            destroyLocked(this.mInfos);
            destroyLocked(this.mRecoverableCrashes);
        }

        @GuardedBy({"mLock"})
        void forEachRecordLocked(BiFunction<Integer, ApplicationExitInfo, Integer> biFunction) {
            if (biFunction == null) {
                return;
            }
            for (int size = this.mInfos.size() - 1; size >= 0; size--) {
                int intValue = biFunction.apply(Integer.valueOf(this.mInfos.keyAt(size)), this.mInfos.valueAt(size)).intValue();
                if (intValue == 1) {
                    File traceFile = this.mInfos.valueAt(size).getTraceFile();
                    if (traceFile != null) {
                        traceFile.delete();
                    }
                    this.mInfos.removeAt(size);
                } else if (intValue == 2) {
                    return;
                }
            }
            for (int size2 = this.mRecoverableCrashes.size() - 1; size2 >= 0; size2--) {
                int intValue2 = biFunction.apply(Integer.valueOf(this.mRecoverableCrashes.keyAt(size2)), this.mRecoverableCrashes.valueAt(size2)).intValue();
                if (intValue2 == 1) {
                    File traceFile2 = this.mRecoverableCrashes.valueAt(size2).getTraceFile();
                    if (traceFile2 != null) {
                        traceFile2.delete();
                    }
                    this.mRecoverableCrashes.removeAt(size2);
                } else if (intValue2 == 2) {
                    return;
                }
            }
        }

        @GuardedBy({"mLock"})
        void dumpLocked(PrintWriter printWriter, String str, SimpleDateFormat simpleDateFormat) {
            ArrayList arrayList = new ArrayList();
            for (int size = this.mInfos.size() - 1; size >= 0; size--) {
                arrayList.add(this.mInfos.valueAt(size));
            }
            for (int size2 = this.mRecoverableCrashes.size() - 1; size2 >= 0; size2--) {
                arrayList.add(this.mRecoverableCrashes.valueAt(size2));
            }
            Collections.sort(arrayList, new Comparator() { // from class: com.android.server.am.AppExitInfoTracker$AppExitInfoContainer$$ExternalSyntheticLambda2
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$dumpLocked$2;
                    lambda$dumpLocked$2 = AppExitInfoTracker.AppExitInfoContainer.lambda$dumpLocked$2((ApplicationExitInfo) obj, (ApplicationExitInfo) obj2);
                    return lambda$dumpLocked$2;
                }
            });
            int size3 = arrayList.size();
            for (int i = 0; i < size3; i++) {
                ((ApplicationExitInfo) arrayList.get(i)).dump(printWriter, str + "  ", "#" + i, simpleDateFormat);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int lambda$dumpLocked$2(ApplicationExitInfo applicationExitInfo, ApplicationExitInfo applicationExitInfo2) {
            return Long.compare(applicationExitInfo2.getTimestamp(), applicationExitInfo.getTimestamp());
        }

        @GuardedBy({"mLock"})
        void writeToProto(ProtoOutputStream protoOutputStream, long j) {
            long start = protoOutputStream.start(j);
            protoOutputStream.write(1120986464257L, this.mUid);
            for (int i = 0; i < this.mInfos.size(); i++) {
                this.mInfos.valueAt(i).writeToProto(protoOutputStream, 2246267895810L);
            }
            for (int i2 = 0; i2 < this.mRecoverableCrashes.size(); i2++) {
                this.mRecoverableCrashes.valueAt(i2).writeToProto(protoOutputStream, 2246267895811L);
            }
            protoOutputStream.end(start);
        }

        int readFromProto(ProtoInputStream protoInputStream, long j) throws IOException, WireTypeMismatchException {
            long start = protoInputStream.start(j);
            int nextField = protoInputStream.nextField();
            while (nextField != -1) {
                if (nextField == 1) {
                    this.mUid = protoInputStream.readInt(1120986464257L);
                } else if (nextField == 2) {
                    ApplicationExitInfo applicationExitInfo = new ApplicationExitInfo();
                    applicationExitInfo.readFromProto(protoInputStream, 2246267895810L);
                    this.mInfos.put(applicationExitInfo.getPid(), applicationExitInfo);
                } else if (nextField == 3) {
                    ApplicationExitInfo applicationExitInfo2 = new ApplicationExitInfo();
                    applicationExitInfo2.readFromProto(protoInputStream, 2246267895811L);
                    this.mRecoverableCrashes.put(applicationExitInfo2.getPid(), applicationExitInfo2);
                }
                nextField = protoInputStream.nextField();
            }
            protoInputStream.end(start);
            return this.mUid;
        }

        @GuardedBy({"mLock"})
        List<ApplicationExitInfo> toListLocked(List<ApplicationExitInfo> list, int i) {
            if (list == null) {
                list = new ArrayList<>();
            }
            for (int size = this.mInfos.size() - 1; size >= 0; size--) {
                if (i == 0 || i == this.mInfos.keyAt(size)) {
                    list.add(this.mInfos.valueAt(size));
                }
            }
            for (int size2 = this.mRecoverableCrashes.size() - 1; size2 >= 0; size2--) {
                if (i == 0 || i == this.mRecoverableCrashes.keyAt(size2)) {
                    list.add(this.mRecoverableCrashes.valueAt(size2));
                }
            }
            return list;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class IsolatedUidRecords {

        @GuardedBy({"mLock"})
        private final SparseArray<ArraySet<Integer>> mUidToIsolatedUidMap = new SparseArray<>();

        @GuardedBy({"mLock"})
        private final SparseArray<Integer> mIsolatedUidToUidMap = new SparseArray<>();

        IsolatedUidRecords() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void addIsolatedUid(int i, int i2) {
            synchronized (AppExitInfoTracker.this.mLock) {
                ArraySet<Integer> arraySet = this.mUidToIsolatedUidMap.get(i2);
                if (arraySet == null) {
                    arraySet = new ArraySet<>();
                    this.mUidToIsolatedUidMap.put(i2, arraySet);
                }
                arraySet.add(Integer.valueOf(i));
                this.mIsolatedUidToUidMap.put(i, Integer.valueOf(i2));
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void removeIsolatedUid(int i, int i2) {
            synchronized (AppExitInfoTracker.this.mLock) {
                int indexOfKey = this.mUidToIsolatedUidMap.indexOfKey(i2);
                if (indexOfKey >= 0) {
                    ArraySet<Integer> valueAt = this.mUidToIsolatedUidMap.valueAt(indexOfKey);
                    valueAt.remove(Integer.valueOf(i));
                    if (valueAt.isEmpty()) {
                        this.mUidToIsolatedUidMap.removeAt(indexOfKey);
                    }
                }
                this.mIsolatedUidToUidMap.remove(i);
            }
        }

        @GuardedBy({"mLock"})
        Integer getUidByIsolatedUid(int i) {
            Integer num;
            if (UserHandle.isIsolated(i)) {
                synchronized (AppExitInfoTracker.this.mLock) {
                    num = this.mIsolatedUidToUidMap.get(i);
                }
                return num;
            }
            return Integer.valueOf(i);
        }

        @GuardedBy({"mLock"})
        private void removeAppUidLocked(int i) {
            ArraySet<Integer> arraySet = this.mUidToIsolatedUidMap.get(i);
            if (arraySet != null) {
                for (int size = arraySet.size() - 1; size >= 0; size--) {
                    this.mIsolatedUidToUidMap.remove(arraySet.removeAt(size).intValue());
                }
            }
        }

        @VisibleForTesting
        void removeAppUid(int i, boolean z) {
            synchronized (AppExitInfoTracker.this.mLock) {
                if (z) {
                    int appId = UserHandle.getAppId(i);
                    for (int size = this.mUidToIsolatedUidMap.size() - 1; size >= 0; size--) {
                        int keyAt = this.mUidToIsolatedUidMap.keyAt(size);
                        if (appId == UserHandle.getAppId(keyAt)) {
                            removeAppUidLocked(keyAt);
                        }
                        this.mUidToIsolatedUidMap.removeAt(size);
                    }
                } else {
                    removeAppUidLocked(i);
                    this.mUidToIsolatedUidMap.remove(i);
                }
            }
        }

        @GuardedBy({"mLock"})
        int removeIsolatedUidLocked(int i) {
            int intValue;
            if (!UserHandle.isIsolated(i) || (intValue = this.mIsolatedUidToUidMap.get(i, -1).intValue()) == -1) {
                return i;
            }
            this.mIsolatedUidToUidMap.remove(i);
            ArraySet<Integer> arraySet = this.mUidToIsolatedUidMap.get(intValue);
            if (arraySet != null) {
                arraySet.remove(Integer.valueOf(i));
            }
            return intValue;
        }

        void removeByUserId(int i) {
            if (i == -2) {
                i = AppExitInfoTracker.this.mService.mUserController.getCurrentUserId();
            }
            synchronized (AppExitInfoTracker.this.mLock) {
                if (i == -1) {
                    this.mIsolatedUidToUidMap.clear();
                    this.mUidToIsolatedUidMap.clear();
                    return;
                }
                for (int size = this.mIsolatedUidToUidMap.size() - 1; size >= 0; size--) {
                    this.mIsolatedUidToUidMap.keyAt(size);
                    int intValue = this.mIsolatedUidToUidMap.valueAt(size).intValue();
                    if (UserHandle.getUserId(intValue) == i) {
                        this.mIsolatedUidToUidMap.removeAt(size);
                        this.mUidToIsolatedUidMap.remove(intValue);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class KillHandler extends Handler {
        static final int MSG_APP_KILL = 4104;
        static final int MSG_APP_RECOVERABLE_CRASH = 4106;
        static final int MSG_CHILD_PROC_DIED = 4102;
        static final int MSG_LMKD_PROC_KILLED = 4101;
        static final int MSG_PROC_DIED = 4103;
        static final int MSG_STATSD_LOG = 4105;

        KillHandler(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 4101:
                    AppExitInfoTracker.this.mAppExitInfoSourceLmkd.onProcDied(message.arg1, message.arg2, null);
                    return;
                case MSG_CHILD_PROC_DIED /* 4102 */:
                    AppExitInfoTracker.this.mAppExitInfoSourceZygote.onProcDied(message.arg1, message.arg2, (Integer) message.obj);
                    return;
                case 4103:
                    ApplicationExitInfo applicationExitInfo = (ApplicationExitInfo) message.obj;
                    synchronized (AppExitInfoTracker.this.mLock) {
                        AppExitInfoTracker.this.handleNoteProcessDiedLocked(applicationExitInfo);
                    }
                    AppExitInfoTracker.this.recycleRawRecord(applicationExitInfo);
                    return;
                case MSG_APP_KILL /* 4104 */:
                    ApplicationExitInfo applicationExitInfo2 = (ApplicationExitInfo) message.obj;
                    synchronized (AppExitInfoTracker.this.mLock) {
                        AppExitInfoTracker.this.handleNoteAppKillLocked(applicationExitInfo2);
                    }
                    AppExitInfoTracker.this.recycleRawRecord(applicationExitInfo2);
                    return;
                case 4105:
                    synchronized (AppExitInfoTracker.this.mLock) {
                        AppExitInfoTracker.this.performLogToStatsdLocked((ApplicationExitInfo) message.obj);
                    }
                    return;
                case 4106:
                    ApplicationExitInfo applicationExitInfo3 = (ApplicationExitInfo) message.obj;
                    synchronized (AppExitInfoTracker.this.mLock) {
                        AppExitInfoTracker.this.handleNoteAppRecoverableCrashLocked(applicationExitInfo3);
                    }
                    AppExitInfoTracker.this.recycleRawRecord(applicationExitInfo3);
                    return;
                default:
                    super.handleMessage(message);
                    return;
            }
        }
    }

    @VisibleForTesting
    boolean isFresh(long j) {
        return j + 300000 >= System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class AppExitInfoExternalSource {
        private static final long APP_EXIT_INFO_FRESHNESS_MS = 300000;

        @GuardedBy({"mLock"})
        private final SparseArray<SparseArray<Pair<Long, Object>>> mData = new SparseArray<>();
        private final Integer mPresetReason;
        private BiConsumer<Integer, Integer> mProcDiedListener;
        private final String mTag;

        AppExitInfoExternalSource(String str, Integer num) {
            this.mTag = str;
            this.mPresetReason = num;
        }

        @GuardedBy({"mLock"})
        private void addLocked(int i, int i2, Object obj) {
            Integer uidByIsolatedUid = AppExitInfoTracker.this.mIsolatedUidRecords.getUidByIsolatedUid(i2);
            if (uidByIsolatedUid != null) {
                i2 = uidByIsolatedUid.intValue();
            }
            SparseArray<Pair<Long, Object>> sparseArray = this.mData.get(i2);
            if (sparseArray == null) {
                sparseArray = new SparseArray<>();
                this.mData.put(i2, sparseArray);
            }
            sparseArray.put(i, new Pair<>(Long.valueOf(System.currentTimeMillis()), obj));
        }

        @VisibleForTesting
        Pair<Long, Object> remove(int i, int i2) {
            Pair<Long, Object> pair;
            synchronized (AppExitInfoTracker.this.mLock) {
                Integer uidByIsolatedUid = AppExitInfoTracker.this.mIsolatedUidRecords.getUidByIsolatedUid(i2);
                if (uidByIsolatedUid != null) {
                    i2 = uidByIsolatedUid.intValue();
                }
                SparseArray<Pair<Long, Object>> sparseArray = this.mData.get(i2);
                if (sparseArray == null || (pair = sparseArray.get(i)) == null) {
                    return null;
                }
                sparseArray.remove(i);
                return AppExitInfoTracker.this.isFresh(((Long) pair.first).longValue()) ? pair : null;
            }
        }

        void removeByUserId(int i) {
            if (i == -2) {
                i = AppExitInfoTracker.this.mService.mUserController.getCurrentUserId();
            }
            synchronized (AppExitInfoTracker.this.mLock) {
                if (i == -1) {
                    this.mData.clear();
                    return;
                }
                for (int size = this.mData.size() - 1; size >= 0; size--) {
                    if (UserHandle.getUserId(this.mData.keyAt(size)) == i) {
                        this.mData.removeAt(size);
                    }
                }
            }
        }

        @GuardedBy({"mLock"})
        void removeByUidLocked(int i, boolean z) {
            Integer uidByIsolatedUid;
            if (UserHandle.isIsolated(i) && (uidByIsolatedUid = AppExitInfoTracker.this.mIsolatedUidRecords.getUidByIsolatedUid(i)) != null) {
                i = uidByIsolatedUid.intValue();
            }
            if (z) {
                int appId = UserHandle.getAppId(i);
                for (int size = this.mData.size() - 1; size >= 0; size--) {
                    if (UserHandle.getAppId(this.mData.keyAt(size)) == appId) {
                        this.mData.removeAt(size);
                    }
                }
                return;
            }
            this.mData.remove(i);
        }

        void setOnProcDiedListener(BiConsumer<Integer, Integer> biConsumer) {
            synchronized (AppExitInfoTracker.this.mLock) {
                this.mProcDiedListener = biConsumer;
            }
        }

        void onProcDied(final int i, final int i2, Integer num) {
            if (ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                Slog.i("ActivityManager", this.mTag + ": proc died: pid=" + i + " uid=" + i2 + ", status=" + num);
            }
            if (AppExitInfoTracker.this.mService == null) {
                return;
            }
            synchronized (AppExitInfoTracker.this.mLock) {
                if (!AppExitInfoTracker.this.updateExitInfoIfNecessaryLocked(i, i2, num, this.mPresetReason)) {
                    addLocked(i, i2, num);
                }
                final BiConsumer<Integer, Integer> biConsumer = this.mProcDiedListener;
                if (biConsumer != null) {
                    AppExitInfoTracker.this.mService.mHandler.post(new Runnable() { // from class: com.android.server.am.AppExitInfoTracker$AppExitInfoExternalSource$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            AppExitInfoTracker.AppExitInfoExternalSource.lambda$onProcDied$0(biConsumer, i, i2);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onProcDied$0(BiConsumer biConsumer, int i, int i2) {
            biConsumer.accept(Integer.valueOf(i), Integer.valueOf(i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AppTraceRetriever extends IAppTraceRetriever.Stub {
        AppTraceRetriever() {
        }

        public ParcelFileDescriptor getTraceFileDescriptor(String str, int i, int i2) {
            AppExitInfoTracker.this.mService.enforceNotIsolatedCaller("getTraceFileDescriptor");
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("Invalid package name");
            }
            int callingPid = Binder.getCallingPid();
            int callingUid = Binder.getCallingUid();
            int userId = UserHandle.getUserId(i);
            AppExitInfoTracker.this.mService.mUserController.handleIncomingUser(callingPid, callingUid, userId, true, 0, "getTraceFileDescriptor", null);
            int enforceDumpPermissionForPackage = AppExitInfoTracker.this.mService.enforceDumpPermissionForPackage(str, userId, callingUid, "getTraceFileDescriptor");
            if (enforceDumpPermissionForPackage == -1) {
                return null;
            }
            synchronized (AppExitInfoTracker.this.mLock) {
                ApplicationExitInfo exitInfoLocked = AppExitInfoTracker.this.getExitInfoLocked(str, enforceDumpPermissionForPackage, i2);
                if (exitInfoLocked == null) {
                    return null;
                }
                File traceFile = exitInfoLocked.getTraceFile();
                if (traceFile == null) {
                    return null;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    try {
                        return ParcelFileDescriptor.open(traceFile, AudioFormat.EVRC);
                    } catch (FileNotFoundException unused) {
                        return null;
                    }
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }
    }
}
