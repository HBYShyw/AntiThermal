package com.android.server.backup.fullbackup;

import android.app.IBackupAgent;
import android.app.backup.BackupProgress;
import android.app.backup.IBackupCallback;
import android.app.backup.IBackupManagerMonitor;
import android.app.backup.IBackupObserver;
import android.app.backup.IFullBackupRestoreObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.EventLog;
import android.util.Log;
import android.util.Slog;
import com.android.server.EventLogTags;
import com.android.server.backup.BackupAgentTimeoutParameters;
import com.android.server.backup.BackupAndRestoreFeatureFlags;
import com.android.server.backup.BackupRestoreTask;
import com.android.server.backup.FullBackupJob;
import com.android.server.backup.OperationStorage;
import com.android.server.backup.TransportManager;
import com.android.server.backup.UserBackupManagerService;
import com.android.server.backup.fullbackup.PerformFullTransportBackupTask;
import com.android.server.backup.internal.OnTaskFinishedListener;
import com.android.server.backup.remote.RemoteCall;
import com.android.server.backup.remote.RemoteCallable;
import com.android.server.backup.transport.BackupTransportClient;
import com.android.server.backup.transport.TransportConnection;
import com.android.server.backup.transport.TransportNotAvailableException;
import com.android.server.backup.utils.BackupEligibilityRules;
import com.android.server.backup.utils.BackupManagerMonitorUtils;
import com.android.server.backup.utils.BackupObserverUtils;
import com.google.android.collect.Sets;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class PerformFullTransportBackupTask extends FullBackupTask implements BackupRestoreTask {
    private static final String TAG = "PFTBT";
    private final BackupAgentTimeoutParameters mAgentTimeoutParameters;
    private final BackupEligibilityRules mBackupEligibilityRules;
    IBackupObserver mBackupObserver;
    SinglePackageBackupRunner mBackupRunner;
    private final int mBackupRunnerOpToken;
    private volatile boolean mCancelAll;
    private final Object mCancelLock;
    private final int mCurrentOpToken;
    PackageInfo mCurrentPackage;
    private volatile boolean mIsDoingBackup;
    FullBackupJob mJob;
    CountDownLatch mLatch;
    private final OnTaskFinishedListener mListener;
    private IBackupManagerMonitor mMonitor;
    OperationStorage mOperationStorage;
    List<PackageInfo> mPackages;
    private final TransportConnection mTransportConnection;
    boolean mUpdateSchedule;
    private UserBackupManagerService mUserBackupManagerService;
    private final int mUserId;
    boolean mUserInitiated;

    /* renamed from: -$$Nest$fgetmBackupEligibilityRules, reason: not valid java name */
    static /* bridge */ /* synthetic */ BackupEligibilityRules m2344$$Nest$fgetmBackupEligibilityRules(PerformFullTransportBackupTask performFullTransportBackupTask) {
        return performFullTransportBackupTask.mBackupEligibilityRules;
    }

    /* renamed from: -$$Nest$fgetmMonitor, reason: not valid java name */
    static /* bridge */ /* synthetic */ IBackupManagerMonitor m2345$$Nest$fgetmMonitor(PerformFullTransportBackupTask performFullTransportBackupTask) {
        return performFullTransportBackupTask.mMonitor;
    }

    /* renamed from: -$$Nest$fgetmUserBackupManagerService, reason: not valid java name */
    static /* bridge */ /* synthetic */ UserBackupManagerService m2346$$Nest$fgetmUserBackupManagerService(PerformFullTransportBackupTask performFullTransportBackupTask) {
        return performFullTransportBackupTask.mUserBackupManagerService;
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void execute() {
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void operationComplete(long j) {
    }

    public static PerformFullTransportBackupTask newWithCurrentTransport(UserBackupManagerService userBackupManagerService, OperationStorage operationStorage, IFullBackupRestoreObserver iFullBackupRestoreObserver, String[] strArr, boolean z, FullBackupJob fullBackupJob, CountDownLatch countDownLatch, IBackupObserver iBackupObserver, IBackupManagerMonitor iBackupManagerMonitor, boolean z2, String str, BackupEligibilityRules backupEligibilityRules) {
        final TransportManager transportManager = userBackupManagerService.getTransportManager();
        final TransportConnection currentTransportClient = transportManager.getCurrentTransportClient(str);
        if (currentTransportClient == null) {
            throw new IllegalStateException("No TransportConnection available");
        }
        return new PerformFullTransportBackupTask(userBackupManagerService, operationStorage, currentTransportClient, iFullBackupRestoreObserver, strArr, z, fullBackupJob, countDownLatch, iBackupObserver, iBackupManagerMonitor, new OnTaskFinishedListener() { // from class: com.android.server.backup.fullbackup.PerformFullTransportBackupTask$$ExternalSyntheticLambda0
            @Override // com.android.server.backup.internal.OnTaskFinishedListener
            public final void onFinished(String str2) {
                TransportManager.this.disposeOfTransportClient(currentTransportClient, str2);
            }
        }, z2, backupEligibilityRules);
    }

    public PerformFullTransportBackupTask(UserBackupManagerService userBackupManagerService, OperationStorage operationStorage, TransportConnection transportConnection, IFullBackupRestoreObserver iFullBackupRestoreObserver, String[] strArr, boolean z, FullBackupJob fullBackupJob, CountDownLatch countDownLatch, IBackupObserver iBackupObserver, IBackupManagerMonitor iBackupManagerMonitor, OnTaskFinishedListener onTaskFinishedListener, boolean z2, BackupEligibilityRules backupEligibilityRules) {
        super(iFullBackupRestoreObserver);
        this.mCancelLock = new Object();
        this.mUserBackupManagerService = userBackupManagerService;
        this.mOperationStorage = operationStorage;
        this.mTransportConnection = transportConnection;
        this.mUpdateSchedule = z;
        this.mLatch = countDownLatch;
        this.mJob = fullBackupJob;
        this.mPackages = new ArrayList(strArr.length);
        this.mBackupObserver = iBackupObserver;
        this.mMonitor = iBackupManagerMonitor;
        this.mListener = onTaskFinishedListener == null ? OnTaskFinishedListener.NOP : onTaskFinishedListener;
        this.mUserInitiated = z2;
        this.mCurrentOpToken = userBackupManagerService.generateRandomIntegerToken();
        this.mBackupRunnerOpToken = userBackupManagerService.generateRandomIntegerToken();
        BackupAgentTimeoutParameters agentTimeoutParameters = userBackupManagerService.getAgentTimeoutParameters();
        Objects.requireNonNull(agentTimeoutParameters, "Timeout parameters cannot be null");
        this.mAgentTimeoutParameters = agentTimeoutParameters;
        this.mUserId = userBackupManagerService.getUserId();
        this.mBackupEligibilityRules = backupEligibilityRules;
        if (userBackupManagerService.isBackupOperationInProgress()) {
            Slog.d(TAG, "Skipping full backup. A backup is already in progress.");
            this.mCancelAll = true;
            return;
        }
        for (String str : strArr) {
            try {
                PackageInfo packageInfoAsUser = userBackupManagerService.getPackageManager().getPackageInfoAsUser(str, AudioFormat.OPUS, this.mUserId);
                this.mCurrentPackage = packageInfoAsUser;
                if (!this.mBackupEligibilityRules.appIsEligibleForBackup(packageInfoAsUser.applicationInfo)) {
                    this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 9, this.mCurrentPackage, 3, null);
                    BackupObserverUtils.sendBackupOnPackageResult(this.mBackupObserver, str, -2001);
                } else if (!this.mBackupEligibilityRules.appGetsFullBackup(packageInfoAsUser)) {
                    this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 10, this.mCurrentPackage, 3, null);
                    BackupObserverUtils.sendBackupOnPackageResult(this.mBackupObserver, str, -2001);
                } else if (this.mBackupEligibilityRules.appIsStopped(packageInfoAsUser.applicationInfo)) {
                    this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 11, this.mCurrentPackage, 3, null);
                    BackupObserverUtils.sendBackupOnPackageResult(this.mBackupObserver, str, -2001);
                } else {
                    this.mPackages.add(packageInfoAsUser);
                }
            } catch (PackageManager.NameNotFoundException unused) {
                Slog.i(TAG, "Requested package " + str + " not found; ignoring");
                this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 12, this.mCurrentPackage, 3, null);
            }
        }
        this.mPackages = userBackupManagerService.filterUserFacingPackages(this.mPackages);
        HashSet newHashSet = Sets.newHashSet();
        Iterator<PackageInfo> it = this.mPackages.iterator();
        while (it.hasNext()) {
            newHashSet.add(it.next().packageName);
        }
        Slog.d(TAG, "backupmanager pftbt token=" + Integer.toHexString(this.mCurrentOpToken));
        this.mOperationStorage.registerOperationForPackages(this.mCurrentOpToken, 0, newHashSet, this, 2);
    }

    public void unregisterTask() {
        this.mOperationStorage.removeOperation(this.mCurrentOpToken);
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void handleCancel(boolean z) {
        synchronized (this.mCancelLock) {
            if (!z) {
                Slog.wtf(TAG, "Expected cancelAll to be true.");
            }
            if (this.mCancelAll) {
                Slog.d(TAG, "Ignoring duplicate cancel call.");
                return;
            }
            this.mCancelAll = true;
            if (this.mIsDoingBackup) {
                this.mUserBackupManagerService.handleCancel(this.mBackupRunnerOpToken, z);
                try {
                    this.mTransportConnection.getConnectedTransport("PFTBT.handleCancel()").cancelFullBackup();
                } catch (RemoteException | TransportNotAvailableException e) {
                    Slog.w(TAG, "Error calling cancelFullBackup() on transport: " + e);
                }
            }
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:1|2|3|(13:(3:99|100|(1:102)(6:103|104|105|(11:107|108|109|110|(1:112)(1:129)|113|(1:115)|116|72|121|(1:123))(15:133|(2:403|404)|135|136|(1:(7:138|139|141|142|(1:144)(1:391)|145|124)(1:401))|367|(1:369)(1:386)|370|(1:372)|373|5ad|378|(1:380)|86|87)|27|28))|12|13|14|(1:16)(1:36)|17|(1:19)|20|672|25|(1:30)|27|28)|5|6|7|(1:9)(1:65)|10|11|(2:(0)|(1:338))) */
    /* JADX WARN: Can't wrap try/catch for region: R(23:1|2|3|(3:99|100|(1:102)(6:103|104|105|(11:107|108|109|110|(1:112)(1:129)|113|(1:115)|116|72|121|(1:123))(15:133|(2:403|404)|135|136|(1:(7:138|139|141|142|(1:144)(1:391)|145|124)(1:401))|367|(1:369)(1:386)|370|(1:372)|373|5ad|378|(1:380)|86|87)|27|28))|5|6|7|(1:9)(1:65)|10|11|12|13|14|(1:16)(1:36)|17|(1:19)|20|672|25|(1:30)|27|28|(2:(0)|(1:338))) */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x042d, code lost:
    
        r7 = -1000;
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x042f, code lost:
    
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(r32.mBackupObserver, r5, -1000);
        android.util.Slog.w(com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG, "Transport failed; aborting backup: " + r13);
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.FULL_BACKUP_TRANSPORT_FAILURE, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x0450, code lost:
    
        r32.mUserBackupManagerService.tearDownAgentAndKill(r15.applicationInfo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x0459, code lost:
    
        if (r32.mCancelAll == false) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:229:0x045b, code lost:
    
        r12 = -2003;
     */
    /* JADX WARN: Code restructure failed: missing block: B:230:0x045e, code lost:
    
        android.util.Slog.i(com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG, "Full backup completed with status: " + r12);
        com.android.server.backup.utils.BackupObserverUtils.sendBackupFinished(r32.mBackupObserver, r12);
        cleanUpPipes(r25);
        cleanUpPipes(r1);
        unregisterTask();
        r1 = r32.mJob;
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x0486, code lost:
    
        if (r1 == null) goto L184;
     */
    /* JADX WARN: Code restructure failed: missing block: B:232:0x0488, code lost:
    
        r1.finishBackupPass(r32.mUserId);
     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x048d, code lost:
    
        r4 = r32.mUserBackupManagerService.getQueueLock();
     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x0493, code lost:
    
        monitor-enter(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:236:0x0494, code lost:
    
        r32.mUserBackupManagerService.setRunningFullBackupTask(null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x049a, code lost:
    
        monitor-exit(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x049b, code lost:
    
        r32.mListener.onFinished("PFTBT.run()");
        r32.mLatch.countDown();
     */
    /* JADX WARN: Code restructure failed: missing block: B:239:0x04a9, code lost:
    
        if (r32.mUpdateSchedule == false) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x04ab, code lost:
    
        r32.mUserBackupManagerService.scheduleNextFullBackupJob(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x045d, code lost:
    
        r12 = -1000;
     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x04b6, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x04b7, code lost:
    
        r9 = r25;
        r12 = -1000;
     */
    /* JADX WARN: Code restructure failed: missing block: B:251:0x04bc, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:252:0x04bd, code lost:
    
        r9 = r25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x012a, code lost:
    
        r2 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x06a0, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x06a1, code lost:
    
        r8 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x06b6, code lost:
    
        r1 = null;
        r2 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x06dc, code lost:
    
        r12 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0707, code lost:
    
        r1.finishBackupPass(r32.mUserId);
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0713, code lost:
    
        r32.mUserBackupManagerService.setRunningFullBackupTask(null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x071a, code lost:
    
        r32.mListener.onFinished("PFTBT.run()");
        r32.mLatch.countDown();
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0728, code lost:
    
        if (r32.mUpdateSchedule != false) goto L326;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x072a, code lost:
    
        r32.mUserBackupManagerService.scheduleNextFullBackupJob(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x06de, code lost:
    
        r12 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0699, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x069a, code lost:
    
        r1 = 0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:194:0x04e8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0509 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x074b  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0776  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0782 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x074d  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x06dc  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0707  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0713 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x06de  */
    /* JADX WARN: Type inference failed for: r11v25 */
    /* JADX WARN: Type inference failed for: r11v28 */
    /* JADX WARN: Type inference failed for: r11v9 */
    /* JADX WARN: Type inference failed for: r6v15, types: [com.android.server.backup.transport.BackupTransportClient] */
    /* JADX WARN: Type inference failed for: r6v50 */
    /* JADX WARN: Type inference failed for: r6v79 */
    /* JADX WARN: Type inference failed for: r7v33 */
    /* JADX WARN: Type inference failed for: r7v5 */
    /* JADX WARN: Type inference failed for: r7v7, types: [boolean] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void run() {
        int i;
        int i2;
        int i3;
        Exception exc;
        ParcelFileDescriptor[] parcelFileDescriptorArr;
        ParcelFileDescriptor[] parcelFileDescriptorArr2;
        Throwable th;
        int i4;
        FullBackupJob fullBackupJob;
        FullBackupJob fullBackupJob2;
        ParcelFileDescriptor[] parcelFileDescriptorArr3;
        Throwable th2;
        int i5;
        byte[] bArr;
        int i6;
        BackupTransportClient backupTransportClient;
        ParcelFileDescriptor[] parcelFileDescriptorArr4;
        String str;
        PackageInfo packageInfo;
        long j;
        ?? r11;
        BackupTransportClient backupTransportClient2;
        String str2;
        long j2;
        ParcelFileDescriptor[] parcelFileDescriptorArr5;
        boolean z;
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        long j3;
        long j4;
        int i7;
        int i8;
        SinglePackageBackupRunner singlePackageBackupRunner = null;
        long j5 = 0;
        int i9 = 0;
        try {
        } catch (Exception e) {
            e = e;
            i = -2003;
            i3 = -1000;
        } catch (Throwable th3) {
            th = th3;
            i = -2003;
            i2 = 0;
        }
        try {
            if (this.mUserBackupManagerService.isEnabled()) {
                try {
                } catch (Exception e2) {
                    i = -2003;
                    i3 = -1000;
                    exc = e2;
                    parcelFileDescriptorArr2 = null;
                    parcelFileDescriptorArr = null;
                    j5 = 0;
                } catch (Throwable th4) {
                    i = -2003;
                    th = th4;
                    parcelFileDescriptorArr2 = null;
                    parcelFileDescriptorArr = null;
                    j5 = 0;
                }
                if (this.mUserBackupManagerService.isSetupComplete()) {
                    BackupTransportClient connect = this.mTransportConnection.connect("PFTBT.run()");
                    ?? r7 = 1;
                    try {
                        try {
                        } catch (Throwable th5) {
                            th = th5;
                            parcelFileDescriptorArr2 = null;
                            parcelFileDescriptorArr = null;
                            i2 = 0;
                            i = -2003;
                        }
                    } catch (Exception e3) {
                        exc = e3;
                        parcelFileDescriptorArr2 = null;
                        parcelFileDescriptorArr = null;
                    }
                    if (connect != null) {
                        if (this.mMonitor == null) {
                            try {
                                this.mMonitor = connect.getBackupManagerMonitor();
                            } catch (RemoteException unused) {
                                Slog.i(TAG, "Failed to retrieve monitor from transport");
                            }
                        }
                        int size = this.mPackages.size();
                        byte[] bArr2 = new byte[BackupAndRestoreFeatureFlags.getFullBackupWriteToTransportBufferSizeBytes()];
                        long j6 = 0;
                        parcelFileDescriptorArr2 = null;
                        parcelFileDescriptorArr = null;
                        int i10 = 0;
                        ?? r6 = connect;
                        while (true) {
                            if (i10 >= size) {
                                break;
                            }
                            try {
                                this.mBackupRunner = singlePackageBackupRunner;
                                PackageInfo packageInfo2 = this.mPackages.get(i10);
                                String str3 = packageInfo2.packageName;
                                Slog.i(TAG, "Initiating full-data transport backup of " + str3 + " token: " + this.mCurrentOpToken);
                                EventLog.writeEvent(EventLogTags.FULL_BACKUP_PACKAGE, str3);
                                ParcelFileDescriptor[] createPipe = ParcelFileDescriptor.createPipe();
                                try {
                                    int i11 = this.mUserInitiated ? r7 : i9;
                                    synchronized (this.mCancelLock) {
                                        try {
                                            if (this.mCancelAll) {
                                                break;
                                            }
                                            int performFullBackup = r6.performFullBackup(packageInfo2, createPipe[i9], i11);
                                            if (performFullBackup == 0) {
                                                try {
                                                    long backupQuota = r6.getBackupQuota(packageInfo2.packageName, r7);
                                                    ParcelFileDescriptor[] createPipe2 = ParcelFileDescriptor.createPipe();
                                                    try {
                                                        i5 = i10;
                                                        bArr = bArr2;
                                                        i6 = size;
                                                        backupTransportClient = r6;
                                                        parcelFileDescriptorArr4 = createPipe;
                                                        boolean z2 = r7;
                                                        str = str3;
                                                        packageInfo = packageInfo2;
                                                        try {
                                                            this.mBackupRunner = new SinglePackageBackupRunner(createPipe2[r7], packageInfo2, this.mTransportConnection, backupQuota, this.mBackupRunnerOpToken, r6.getTransportFlags());
                                                            createPipe2[z2 ? 1 : 0].close();
                                                            createPipe2[z2 ? 1 : 0] = null;
                                                            this.mIsDoingBackup = z2;
                                                            j = backupQuota;
                                                            parcelFileDescriptorArr2 = createPipe2;
                                                            r11 = z2;
                                                        } catch (Throwable th6) {
                                                            th2 = th6;
                                                            parcelFileDescriptorArr2 = createPipe2;
                                                            parcelFileDescriptorArr3 = parcelFileDescriptorArr4;
                                                        }
                                                    } catch (Throwable th7) {
                                                        th2 = th7;
                                                        parcelFileDescriptorArr3 = createPipe;
                                                        parcelFileDescriptorArr2 = createPipe2;
                                                    }
                                                } catch (Throwable th8) {
                                                    th2 = th8;
                                                    parcelFileDescriptorArr3 = createPipe;
                                                }
                                            } else {
                                                i5 = i10;
                                                bArr = bArr2;
                                                i6 = size;
                                                backupTransportClient = r6;
                                                str = str3;
                                                packageInfo = packageInfo2;
                                                parcelFileDescriptorArr4 = createPipe;
                                                r11 = r7;
                                                j = Long.MAX_VALUE;
                                            }
                                            try {
                                                if (performFullBackup == 0) {
                                                    try {
                                                        parcelFileDescriptorArr4[0].close();
                                                        parcelFileDescriptorArr4[0] = null;
                                                        new Thread(this.mBackupRunner, "package-backup-bridge").start();
                                                        FileInputStream fileInputStream2 = new FileInputStream(parcelFileDescriptorArr2[0].getFileDescriptor());
                                                        FileOutputStream fileOutputStream2 = new FileOutputStream(parcelFileDescriptorArr4[r11].getFileDescriptor());
                                                        long preflightResultBlocking = this.mBackupRunner.getPreflightResultBlocking();
                                                        if (preflightResultBlocking < 0) {
                                                            this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 16, this.mCurrentPackage, 3, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_PREFLIGHT_ERROR", preflightResultBlocking));
                                                            i7 = (int) preflightResultBlocking;
                                                            backupTransportClient2 = backupTransportClient;
                                                            str2 = str;
                                                        } else {
                                                            long j7 = 0;
                                                            byte[] bArr3 = bArr;
                                                            while (true) {
                                                                int read = fileInputStream2.read(bArr3);
                                                                if (read > 0) {
                                                                    fileOutputStream2.write(bArr3, 0, read);
                                                                    synchronized (this.mCancelLock) {
                                                                        if (this.mCancelAll) {
                                                                            backupTransportClient2 = backupTransportClient;
                                                                        } else {
                                                                            backupTransportClient2 = backupTransportClient;
                                                                            performFullBackup = backupTransportClient2.sendBackupData(read);
                                                                        }
                                                                    }
                                                                    fileInputStream = fileInputStream2;
                                                                    fileOutputStream = fileOutputStream2;
                                                                    long j8 = j7 + read;
                                                                    IBackupObserver iBackupObserver = this.mBackupObserver;
                                                                    if (iBackupObserver != null && preflightResultBlocking > 0) {
                                                                        bArr = bArr3;
                                                                        BackupProgress backupProgress = new BackupProgress(preflightResultBlocking, j8);
                                                                        j7 = j8;
                                                                        str2 = str;
                                                                        BackupObserverUtils.sendBackupOnUpdate(iBackupObserver, str2, backupProgress);
                                                                        j3 = preflightResultBlocking;
                                                                        j4 = j7;
                                                                        if (read > 0 || performFullBackup != 0) {
                                                                            break;
                                                                        }
                                                                        backupTransportClient = backupTransportClient2;
                                                                        str = str2;
                                                                        fileInputStream2 = fileInputStream;
                                                                        bArr3 = bArr;
                                                                        fileOutputStream2 = fileOutputStream;
                                                                        preflightResultBlocking = j3;
                                                                        j7 = j4;
                                                                    } else {
                                                                        j7 = j8;
                                                                        bArr = bArr3;
                                                                    }
                                                                } else {
                                                                    fileInputStream = fileInputStream2;
                                                                    fileOutputStream = fileOutputStream2;
                                                                    bArr = bArr3;
                                                                    backupTransportClient2 = backupTransportClient;
                                                                }
                                                                str2 = str;
                                                                j3 = preflightResultBlocking;
                                                                j4 = j7;
                                                                if (read > 0) {
                                                                    break;
                                                                    break;
                                                                }
                                                                backupTransportClient = backupTransportClient2;
                                                                str = str2;
                                                                fileInputStream2 = fileInputStream;
                                                                bArr3 = bArr;
                                                                fileOutputStream2 = fileOutputStream;
                                                                preflightResultBlocking = j3;
                                                                j7 = j4;
                                                            }
                                                            if (performFullBackup == -1005) {
                                                                Slog.w(TAG, "Package hit quota limit in-flight " + str2 + ": " + j4 + " of " + j);
                                                                this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 18, this.mCurrentPackage, 1, null);
                                                                this.mBackupRunner.sendQuotaExceeded(j4, j);
                                                            }
                                                            i7 = performFullBackup;
                                                        }
                                                        int backupResultBlocking = this.mBackupRunner.getBackupResultBlocking();
                                                        synchronized (this.mCancelLock) {
                                                            this.mIsDoingBackup = false;
                                                            if (!this.mCancelAll) {
                                                                if (backupResultBlocking == 0) {
                                                                    int finishBackup = backupTransportClient2.finishBackup();
                                                                    if (i7 == 0) {
                                                                        i7 = finishBackup;
                                                                    }
                                                                } else {
                                                                    backupTransportClient2.cancelFullBackup();
                                                                }
                                                            }
                                                        }
                                                        performFullBackup = (i7 != 0 || backupResultBlocking == 0) ? i7 : backupResultBlocking;
                                                        if (performFullBackup != 0) {
                                                            Slog.w(TAG, "Error " + performFullBackup + " backing up " + str2);
                                                        }
                                                        j2 = backupTransportClient2.requestFullBackupTime();
                                                    } catch (Exception e4) {
                                                        exc = e4;
                                                        j5 = j6;
                                                        parcelFileDescriptorArr = parcelFileDescriptorArr4;
                                                        i = -2003;
                                                        i3 = -1000;
                                                        try {
                                                            Slog.w(TAG, "Exception trying full transport backup", exc);
                                                            this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 19, this.mCurrentPackage, 3, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EXCEPTION_FULL_BACKUP", Log.getStackTraceString(exc)));
                                                            if (!this.mCancelAll) {
                                                            }
                                                            Slog.i(TAG, "Full backup completed with status: " + i4);
                                                            BackupObserverUtils.sendBackupFinished(this.mBackupObserver, i4);
                                                            cleanUpPipes(parcelFileDescriptorArr);
                                                            cleanUpPipes(parcelFileDescriptorArr2);
                                                            unregisterTask();
                                                            fullBackupJob = this.mJob;
                                                            if (fullBackupJob != null) {
                                                            }
                                                            synchronized (this.mUserBackupManagerService.getQueueLock()) {
                                                            }
                                                        } catch (Throwable th9) {
                                                            th = th9;
                                                            i2 = i3;
                                                            if (!this.mCancelAll) {
                                                            }
                                                            Slog.i(TAG, "Full backup completed with status: " + r13);
                                                            BackupObserverUtils.sendBackupFinished(this.mBackupObserver, r13);
                                                            cleanUpPipes(parcelFileDescriptorArr);
                                                            cleanUpPipes(parcelFileDescriptorArr2);
                                                            unregisterTask();
                                                            fullBackupJob2 = this.mJob;
                                                            if (fullBackupJob2 != null) {
                                                            }
                                                            synchronized (this.mUserBackupManagerService.getQueueLock()) {
                                                            }
                                                        }
                                                    } catch (Throwable th10) {
                                                        th = th10;
                                                        j5 = j6;
                                                        parcelFileDescriptorArr = parcelFileDescriptorArr4;
                                                        i = -2003;
                                                        i2 = 0;
                                                        if (!this.mCancelAll) {
                                                        }
                                                        Slog.i(TAG, "Full backup completed with status: " + r13);
                                                        BackupObserverUtils.sendBackupFinished(this.mBackupObserver, r13);
                                                        cleanUpPipes(parcelFileDescriptorArr);
                                                        cleanUpPipes(parcelFileDescriptorArr2);
                                                        unregisterTask();
                                                        fullBackupJob2 = this.mJob;
                                                        if (fullBackupJob2 != null) {
                                                        }
                                                        synchronized (this.mUserBackupManagerService.getQueueLock()) {
                                                        }
                                                    }
                                                    try {
                                                        Slog.i(TAG, "Transport suggested backoff=" + j2);
                                                    } catch (Exception e5) {
                                                        e = e5;
                                                        j5 = j2;
                                                        parcelFileDescriptorArr = parcelFileDescriptorArr4;
                                                        i = -2003;
                                                        i3 = -1000;
                                                        exc = e;
                                                        Slog.w(TAG, "Exception trying full transport backup", exc);
                                                        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 19, this.mCurrentPackage, 3, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EXCEPTION_FULL_BACKUP", Log.getStackTraceString(exc)));
                                                        if (!this.mCancelAll) {
                                                        }
                                                        Slog.i(TAG, "Full backup completed with status: " + i4);
                                                        BackupObserverUtils.sendBackupFinished(this.mBackupObserver, i4);
                                                        cleanUpPipes(parcelFileDescriptorArr);
                                                        cleanUpPipes(parcelFileDescriptorArr2);
                                                        unregisterTask();
                                                        fullBackupJob = this.mJob;
                                                        if (fullBackupJob != null) {
                                                        }
                                                        synchronized (this.mUserBackupManagerService.getQueueLock()) {
                                                        }
                                                    } catch (Throwable th11) {
                                                        th = th11;
                                                        j5 = j2;
                                                        parcelFileDescriptorArr = parcelFileDescriptorArr4;
                                                        i = -2003;
                                                        i2 = 0;
                                                        th = th;
                                                        if (!this.mCancelAll) {
                                                        }
                                                        Slog.i(TAG, "Full backup completed with status: " + r13);
                                                        BackupObserverUtils.sendBackupFinished(this.mBackupObserver, r13);
                                                        cleanUpPipes(parcelFileDescriptorArr);
                                                        cleanUpPipes(parcelFileDescriptorArr2);
                                                        unregisterTask();
                                                        fullBackupJob2 = this.mJob;
                                                        if (fullBackupJob2 != null) {
                                                        }
                                                        synchronized (this.mUserBackupManagerService.getQueueLock()) {
                                                        }
                                                    }
                                                } else {
                                                    backupTransportClient2 = backupTransportClient;
                                                    str2 = str;
                                                    j2 = j6;
                                                }
                                                try {
                                                    if (this.mUpdateSchedule) {
                                                        this.mUserBackupManagerService.enqueueFullBackup(str2, System.currentTimeMillis());
                                                    }
                                                    if (performFullBackup == -1002) {
                                                        BackupObserverUtils.sendBackupOnPackageResult(this.mBackupObserver, str2, -1002);
                                                        Slog.i(TAG, "Transport rejected backup of " + str2 + ", skipping");
                                                        z = true;
                                                        EventLog.writeEvent(EventLogTags.FULL_BACKUP_AGENT_FAILURE, str2, "transport rejected");
                                                        if (this.mBackupRunner != null) {
                                                            this.mUserBackupManagerService.tearDownAgentAndKill(packageInfo.applicationInfo);
                                                        }
                                                    } else {
                                                        z = true;
                                                        if (performFullBackup == -1005) {
                                                            BackupObserverUtils.sendBackupOnPackageResult(this.mBackupObserver, str2, -1005);
                                                            Slog.i(TAG, "Transport quota exceeded for package: " + str2);
                                                            EventLog.writeEvent(EventLogTags.FULL_BACKUP_QUOTA_EXCEEDED, str2);
                                                            this.mUserBackupManagerService.tearDownAgentAndKill(packageInfo.applicationInfo);
                                                        } else if (performFullBackup == -1003) {
                                                            BackupObserverUtils.sendBackupOnPackageResult(this.mBackupObserver, str2, -1003);
                                                            Slog.w(TAG, "Application failure for package: " + str2);
                                                            EventLog.writeEvent(EventLogTags.BACKUP_AGENT_FAILURE, str2);
                                                            this.mUserBackupManagerService.tearDownAgentAndKill(packageInfo.applicationInfo);
                                                        } else {
                                                            i = -2003;
                                                            if (performFullBackup == -2003) {
                                                                try {
                                                                    BackupObserverUtils.sendBackupOnPackageResult(this.mBackupObserver, str2, -2003);
                                                                    Slog.w(TAG, "Backup cancelled. package=" + str2 + ", cancelAll=" + this.mCancelAll);
                                                                    EventLog.writeEvent(EventLogTags.FULL_BACKUP_CANCELLED, str2);
                                                                    this.mUserBackupManagerService.tearDownAgentAndKill(packageInfo.applicationInfo);
                                                                    parcelFileDescriptorArr5 = parcelFileDescriptorArr4;
                                                                    i3 = -1000;
                                                                    cleanUpPipes(parcelFileDescriptorArr5);
                                                                    cleanUpPipes(parcelFileDescriptorArr2);
                                                                    if (packageInfo.applicationInfo != null) {
                                                                        try {
                                                                            Slog.i(TAG, "Unbinding agent in " + str2);
                                                                            try {
                                                                                this.mUserBackupManagerService.getActivityManager().unbindBackupAgent(packageInfo.applicationInfo);
                                                                            } catch (RemoteException unused2) {
                                                                            }
                                                                        } catch (Exception e6) {
                                                                            e = e6;
                                                                            long j9 = j2;
                                                                            exc = e;
                                                                            parcelFileDescriptorArr = parcelFileDescriptorArr5;
                                                                            j5 = j9;
                                                                            Slog.w(TAG, "Exception trying full transport backup", exc);
                                                                            this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 19, this.mCurrentPackage, 3, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EXCEPTION_FULL_BACKUP", Log.getStackTraceString(exc)));
                                                                            if (!this.mCancelAll) {
                                                                            }
                                                                            Slog.i(TAG, "Full backup completed with status: " + i4);
                                                                            BackupObserverUtils.sendBackupFinished(this.mBackupObserver, i4);
                                                                            cleanUpPipes(parcelFileDescriptorArr);
                                                                            cleanUpPipes(parcelFileDescriptorArr2);
                                                                            unregisterTask();
                                                                            fullBackupJob = this.mJob;
                                                                            if (fullBackupJob != null) {
                                                                            }
                                                                            synchronized (this.mUserBackupManagerService.getQueueLock()) {
                                                                            }
                                                                        } catch (Throwable th12) {
                                                                            th = th12;
                                                                            i2 = 0;
                                                                            long j10 = j2;
                                                                            th = th;
                                                                            parcelFileDescriptorArr = parcelFileDescriptorArr5;
                                                                            j5 = j10;
                                                                            if (!this.mCancelAll) {
                                                                            }
                                                                            Slog.i(TAG, "Full backup completed with status: " + r13);
                                                                            BackupObserverUtils.sendBackupFinished(this.mBackupObserver, r13);
                                                                            cleanUpPipes(parcelFileDescriptorArr);
                                                                            cleanUpPipes(parcelFileDescriptorArr2);
                                                                            unregisterTask();
                                                                            fullBackupJob2 = this.mJob;
                                                                            if (fullBackupJob2 != null) {
                                                                            }
                                                                            synchronized (this.mUserBackupManagerService.getQueueLock()) {
                                                                            }
                                                                        }
                                                                    }
                                                                    j6 = j2;
                                                                    r6 = backupTransportClient2;
                                                                    i10 = i5 + 1;
                                                                    r7 = z;
                                                                    parcelFileDescriptorArr = parcelFileDescriptorArr5;
                                                                    size = i6;
                                                                    bArr2 = bArr;
                                                                    singlePackageBackupRunner = null;
                                                                    i9 = 0;
                                                                } catch (Exception e7) {
                                                                    e = e7;
                                                                    j5 = j2;
                                                                    parcelFileDescriptorArr = parcelFileDescriptorArr4;
                                                                    i3 = -1000;
                                                                    exc = e;
                                                                    Slog.w(TAG, "Exception trying full transport backup", exc);
                                                                    this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 19, this.mCurrentPackage, 3, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EXCEPTION_FULL_BACKUP", Log.getStackTraceString(exc)));
                                                                    if (!this.mCancelAll) {
                                                                    }
                                                                    Slog.i(TAG, "Full backup completed with status: " + i4);
                                                                    BackupObserverUtils.sendBackupFinished(this.mBackupObserver, i4);
                                                                    cleanUpPipes(parcelFileDescriptorArr);
                                                                    cleanUpPipes(parcelFileDescriptorArr2);
                                                                    unregisterTask();
                                                                    fullBackupJob = this.mJob;
                                                                    if (fullBackupJob != null) {
                                                                    }
                                                                    synchronized (this.mUserBackupManagerService.getQueueLock()) {
                                                                    }
                                                                } catch (Throwable th13) {
                                                                    th = th13;
                                                                    j5 = j2;
                                                                    parcelFileDescriptorArr = parcelFileDescriptorArr4;
                                                                    i2 = 0;
                                                                    th = th;
                                                                    if (!this.mCancelAll) {
                                                                    }
                                                                    Slog.i(TAG, "Full backup completed with status: " + r13);
                                                                    BackupObserverUtils.sendBackupFinished(this.mBackupObserver, r13);
                                                                    cleanUpPipes(parcelFileDescriptorArr);
                                                                    cleanUpPipes(parcelFileDescriptorArr2);
                                                                    unregisterTask();
                                                                    fullBackupJob2 = this.mJob;
                                                                    if (fullBackupJob2 != null) {
                                                                    }
                                                                    synchronized (this.mUserBackupManagerService.getQueueLock()) {
                                                                    }
                                                                }
                                                            } else if (performFullBackup != 0) {
                                                                try {
                                                                    try {
                                                                        break;
                                                                    } catch (Exception e8) {
                                                                        e = e8;
                                                                        parcelFileDescriptorArr5 = parcelFileDescriptorArr4;
                                                                        i3 = -1000;
                                                                        long j92 = j2;
                                                                        exc = e;
                                                                        parcelFileDescriptorArr = parcelFileDescriptorArr5;
                                                                        j5 = j92;
                                                                        Slog.w(TAG, "Exception trying full transport backup", exc);
                                                                        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 19, this.mCurrentPackage, 3, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EXCEPTION_FULL_BACKUP", Log.getStackTraceString(exc)));
                                                                        if (!this.mCancelAll) {
                                                                        }
                                                                        Slog.i(TAG, "Full backup completed with status: " + i4);
                                                                        BackupObserverUtils.sendBackupFinished(this.mBackupObserver, i4);
                                                                        cleanUpPipes(parcelFileDescriptorArr);
                                                                        cleanUpPipes(parcelFileDescriptorArr2);
                                                                        unregisterTask();
                                                                        fullBackupJob = this.mJob;
                                                                        if (fullBackupJob != null) {
                                                                        }
                                                                        synchronized (this.mUserBackupManagerService.getQueueLock()) {
                                                                        }
                                                                    }
                                                                } catch (Throwable th14) {
                                                                    th = th14;
                                                                    parcelFileDescriptorArr5 = parcelFileDescriptorArr4;
                                                                    i2 = 0;
                                                                    long j102 = j2;
                                                                    th = th;
                                                                    parcelFileDescriptorArr = parcelFileDescriptorArr5;
                                                                    j5 = j102;
                                                                    if (!this.mCancelAll) {
                                                                    }
                                                                    Slog.i(TAG, "Full backup completed with status: " + r13);
                                                                    BackupObserverUtils.sendBackupFinished(this.mBackupObserver, r13);
                                                                    cleanUpPipes(parcelFileDescriptorArr);
                                                                    cleanUpPipes(parcelFileDescriptorArr2);
                                                                    unregisterTask();
                                                                    fullBackupJob2 = this.mJob;
                                                                    if (fullBackupJob2 != null) {
                                                                    }
                                                                    synchronized (this.mUserBackupManagerService.getQueueLock()) {
                                                                    }
                                                                }
                                                            } else {
                                                                parcelFileDescriptorArr5 = parcelFileDescriptorArr4;
                                                                i3 = -1000;
                                                                BackupObserverUtils.sendBackupOnPackageResult(this.mBackupObserver, str2, 0);
                                                                EventLog.writeEvent(EventLogTags.FULL_BACKUP_SUCCESS, str2);
                                                                this.mUserBackupManagerService.logBackupComplete(str2);
                                                                cleanUpPipes(parcelFileDescriptorArr5);
                                                                cleanUpPipes(parcelFileDescriptorArr2);
                                                                if (packageInfo.applicationInfo != null) {
                                                                }
                                                                j6 = j2;
                                                                r6 = backupTransportClient2;
                                                                i10 = i5 + 1;
                                                                r7 = z;
                                                                parcelFileDescriptorArr = parcelFileDescriptorArr5;
                                                                size = i6;
                                                                bArr2 = bArr;
                                                                singlePackageBackupRunner = null;
                                                                i9 = 0;
                                                            }
                                                        }
                                                    }
                                                    parcelFileDescriptorArr5 = parcelFileDescriptorArr4;
                                                    i = -2003;
                                                    i3 = -1000;
                                                    cleanUpPipes(parcelFileDescriptorArr5);
                                                    cleanUpPipes(parcelFileDescriptorArr2);
                                                    if (packageInfo.applicationInfo != null) {
                                                    }
                                                    j6 = j2;
                                                    r6 = backupTransportClient2;
                                                    i10 = i5 + 1;
                                                    r7 = z;
                                                    parcelFileDescriptorArr = parcelFileDescriptorArr5;
                                                    size = i6;
                                                    bArr2 = bArr;
                                                    singlePackageBackupRunner = null;
                                                    i9 = 0;
                                                } catch (Exception e9) {
                                                    e = e9;
                                                    parcelFileDescriptorArr5 = parcelFileDescriptorArr4;
                                                    i = -2003;
                                                } catch (Throwable th15) {
                                                    th = th15;
                                                    parcelFileDescriptorArr5 = parcelFileDescriptorArr4;
                                                    i = -2003;
                                                }
                                            } catch (Throwable th16) {
                                                th = th16;
                                                parcelFileDescriptorArr3 = parcelFileDescriptorArr4;
                                                i = -2003;
                                                i3 = -1000;
                                                th2 = th;
                                                while (true) {
                                                    try {
                                                        try {
                                                            break;
                                                        } catch (Exception e10) {
                                                            e = e10;
                                                            exc = e;
                                                            parcelFileDescriptorArr = parcelFileDescriptorArr3;
                                                            j5 = j6;
                                                            Slog.w(TAG, "Exception trying full transport backup", exc);
                                                            this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 19, this.mCurrentPackage, 3, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_EXCEPTION_FULL_BACKUP", Log.getStackTraceString(exc)));
                                                            if (!this.mCancelAll) {
                                                            }
                                                            Slog.i(TAG, "Full backup completed with status: " + i4);
                                                            BackupObserverUtils.sendBackupFinished(this.mBackupObserver, i4);
                                                            cleanUpPipes(parcelFileDescriptorArr);
                                                            cleanUpPipes(parcelFileDescriptorArr2);
                                                            unregisterTask();
                                                            fullBackupJob = this.mJob;
                                                            if (fullBackupJob != null) {
                                                            }
                                                            synchronized (this.mUserBackupManagerService.getQueueLock()) {
                                                            }
                                                        } catch (Throwable th17) {
                                                            th = th17;
                                                            th = th;
                                                            parcelFileDescriptorArr = parcelFileDescriptorArr3;
                                                            j5 = j6;
                                                            i2 = 0;
                                                            if (!this.mCancelAll) {
                                                            }
                                                            Slog.i(TAG, "Full backup completed with status: " + r13);
                                                            BackupObserverUtils.sendBackupFinished(this.mBackupObserver, r13);
                                                            cleanUpPipes(parcelFileDescriptorArr);
                                                            cleanUpPipes(parcelFileDescriptorArr2);
                                                            unregisterTask();
                                                            fullBackupJob2 = this.mJob;
                                                            if (fullBackupJob2 != null) {
                                                            }
                                                            synchronized (this.mUserBackupManagerService.getQueueLock()) {
                                                            }
                                                        }
                                                    } catch (Throwable th18) {
                                                        th2 = th18;
                                                    }
                                                    th2 = th18;
                                                }
                                                throw th2;
                                            }
                                            th2 = th8;
                                            parcelFileDescriptorArr3 = createPipe;
                                            i = -2003;
                                            i3 = -1000;
                                        } catch (Throwable th19) {
                                            th = th19;
                                            parcelFileDescriptorArr3 = createPipe;
                                        }
                                        while (true) {
                                            break;
                                            th2 = th18;
                                        }
                                        throw th2;
                                    }
                                } catch (Exception e11) {
                                    e = e11;
                                    parcelFileDescriptorArr3 = createPipe;
                                    i = -2003;
                                    i3 = -1000;
                                } catch (Throwable th20) {
                                    th = th20;
                                    parcelFileDescriptorArr3 = createPipe;
                                    i = -2003;
                                }
                            } catch (Exception e12) {
                                i = -2003;
                                i3 = -1000;
                                exc = e12;
                            } catch (Throwable th21) {
                                i = -2003;
                                th = th21;
                            }
                        }
                        int i12 = this.mCancelAll ? -2003 : 0;
                        Slog.i(TAG, "Full backup completed with status: " + i12);
                        BackupObserverUtils.sendBackupFinished(this.mBackupObserver, i12);
                        cleanUpPipes(parcelFileDescriptorArr);
                        cleanUpPipes(parcelFileDescriptorArr2);
                        unregisterTask();
                        FullBackupJob fullBackupJob3 = this.mJob;
                        if (fullBackupJob3 != null) {
                            fullBackupJob3.finishBackupPass(this.mUserId);
                        }
                        synchronized (this.mUserBackupManagerService.getQueueLock()) {
                            this.mUserBackupManagerService.setRunningFullBackupTask(null);
                        }
                        this.mListener.onFinished("PFTBT.run()");
                        this.mLatch.countDown();
                        if (this.mUpdateSchedule) {
                            this.mUserBackupManagerService.scheduleNextFullBackupJob(j6);
                        }
                        Slog.i(TAG, "Full data backup pass finished.");
                        this.mUserBackupManagerService.getWakelock().release();
                        return;
                    }
                    Slog.w(TAG, "Transport not present; full data backup not performed");
                    try {
                        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 15, this.mCurrentPackage, 1, null);
                        int i13 = this.mCancelAll ? -2003 : -1000;
                        Slog.i(TAG, "Full backup completed with status: " + i13);
                        BackupObserverUtils.sendBackupFinished(this.mBackupObserver, i13);
                        cleanUpPipes(null);
                        cleanUpPipes(null);
                        unregisterTask();
                        FullBackupJob fullBackupJob4 = this.mJob;
                        if (fullBackupJob4 != null) {
                            fullBackupJob4.finishBackupPass(this.mUserId);
                        }
                        synchronized (this.mUserBackupManagerService.getQueueLock()) {
                            this.mUserBackupManagerService.setRunningFullBackupTask(null);
                        }
                        this.mListener.onFinished("PFTBT.run()");
                        this.mLatch.countDown();
                        if (this.mUpdateSchedule) {
                            this.mUserBackupManagerService.scheduleNextFullBackupJob(0L);
                        }
                    } catch (Throwable th22) {
                        th = th22;
                        parcelFileDescriptorArr2 = null;
                        parcelFileDescriptorArr = null;
                        i = -2003;
                        i2 = -1000;
                        int i14 = !this.mCancelAll ? i : i2;
                        Slog.i(TAG, "Full backup completed with status: " + i14);
                        BackupObserverUtils.sendBackupFinished(this.mBackupObserver, i14);
                        cleanUpPipes(parcelFileDescriptorArr);
                        cleanUpPipes(parcelFileDescriptorArr2);
                        unregisterTask();
                        fullBackupJob2 = this.mJob;
                        if (fullBackupJob2 != null) {
                            fullBackupJob2.finishBackupPass(this.mUserId);
                        }
                        synchronized (this.mUserBackupManagerService.getQueueLock()) {
                            this.mUserBackupManagerService.setRunningFullBackupTask(null);
                        }
                        this.mListener.onFinished("PFTBT.run()");
                        this.mLatch.countDown();
                        if (this.mUpdateSchedule) {
                            this.mUserBackupManagerService.scheduleNextFullBackupJob(j5);
                        }
                        Slog.i(TAG, "Full data backup pass finished.");
                        this.mUserBackupManagerService.getWakelock().release();
                        throw th;
                    }
                    Slog.i(TAG, "Full data backup pass finished.");
                    this.mUserBackupManagerService.getWakelock().release();
                    return;
                }
            }
            this.mUpdateSchedule = false;
            int i15 = this.mCancelAll ? -2003 : -2001;
            Slog.i(TAG, "Full backup completed with status: " + i15);
            BackupObserverUtils.sendBackupFinished(this.mBackupObserver, i15);
            cleanUpPipes(null);
            cleanUpPipes(null);
            unregisterTask();
            FullBackupJob fullBackupJob5 = this.mJob;
            if (fullBackupJob5 != null) {
                fullBackupJob5.finishBackupPass(this.mUserId);
            }
            synchronized (this.mUserBackupManagerService.getQueueLock()) {
                this.mUserBackupManagerService.setRunningFullBackupTask(null);
            }
            this.mListener.onFinished("PFTBT.run()");
            this.mLatch.countDown();
            if (this.mUpdateSchedule) {
                this.mUserBackupManagerService.scheduleNextFullBackupJob(0L);
            }
            Slog.i(TAG, "Full data backup pass finished.");
            this.mUserBackupManagerService.getWakelock().release();
            return;
        } catch (Throwable th23) {
            th = th23;
            i2 = i8;
            j5 = 0;
            parcelFileDescriptorArr2 = null;
            parcelFileDescriptorArr = null;
            th = th;
            if (!this.mCancelAll) {
            }
            Slog.i(TAG, "Full backup completed with status: " + i14);
            BackupObserverUtils.sendBackupFinished(this.mBackupObserver, i14);
            cleanUpPipes(parcelFileDescriptorArr);
            cleanUpPipes(parcelFileDescriptorArr2);
            unregisterTask();
            fullBackupJob2 = this.mJob;
            if (fullBackupJob2 != null) {
            }
            synchronized (this.mUserBackupManagerService.getQueueLock()) {
            }
        }
        i = -2003;
        i3 = -1000;
        Slog.i(TAG, "full backup requested but enabled=" + this.mUserBackupManagerService.isEnabled() + " setupComplete=" + this.mUserBackupManagerService.isSetupComplete() + "; ignoring");
        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, this.mUserBackupManagerService.isSetupComplete() ? 13 : 14, null, 3, null);
        i8 = 0;
    }

    void cleanUpPipes(ParcelFileDescriptor[] parcelFileDescriptorArr) {
        if (parcelFileDescriptorArr != null) {
            ParcelFileDescriptor parcelFileDescriptor = parcelFileDescriptorArr[0];
            if (parcelFileDescriptor != null) {
                parcelFileDescriptorArr[0] = null;
                try {
                    parcelFileDescriptor.close();
                } catch (IOException unused) {
                    Slog.w(TAG, "Unable to close pipe!");
                }
            }
            ParcelFileDescriptor parcelFileDescriptor2 = parcelFileDescriptorArr[1];
            if (parcelFileDescriptor2 != null) {
                parcelFileDescriptorArr[1] = null;
                try {
                    parcelFileDescriptor2.close();
                } catch (IOException unused2) {
                    Slog.w(TAG, "Unable to close pipe!");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class SinglePackageBackupPreflight implements BackupRestoreTask, FullBackupPreflight {
        private final int mCurrentOpToken;
        final long mQuota;
        final TransportConnection mTransportConnection;
        private final int mTransportFlags;
        final AtomicLong mResult = new AtomicLong(-1003);
        final CountDownLatch mLatch = new CountDownLatch(1);

        @Override // com.android.server.backup.BackupRestoreTask
        public void execute() {
        }

        SinglePackageBackupPreflight(TransportConnection transportConnection, long j, int i, int i2) {
            this.mTransportConnection = transportConnection;
            this.mQuota = j;
            this.mCurrentOpToken = i;
            this.mTransportFlags = i2;
        }

        @Override // com.android.server.backup.fullbackup.FullBackupPreflight
        public int preflightFullBackup(PackageInfo packageInfo, final IBackupAgent iBackupAgent) {
            long fullBackupAgentTimeoutMillis = PerformFullTransportBackupTask.this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis();
            try {
                PerformFullTransportBackupTask.this.mUserBackupManagerService.prepareOperationTimeout(this.mCurrentOpToken, fullBackupAgentTimeoutMillis, this, 0);
                iBackupAgent.doMeasureFullBackup(this.mQuota, this.mCurrentOpToken, PerformFullTransportBackupTask.this.mUserBackupManagerService.getBackupManagerBinder(), this.mTransportFlags);
                this.mLatch.await(fullBackupAgentTimeoutMillis, TimeUnit.MILLISECONDS);
                final long j = this.mResult.get();
                if (j < 0) {
                    return (int) j;
                }
                int checkFullBackupSize = this.mTransportConnection.connectOrThrow("PFTBT$SPBP.preflightFullBackup()").checkFullBackupSize(j);
                if (checkFullBackupSize != -1005) {
                    return checkFullBackupSize;
                }
                RemoteCall.execute(new RemoteCallable() { // from class: com.android.server.backup.fullbackup.PerformFullTransportBackupTask$SinglePackageBackupPreflight$$ExternalSyntheticLambda0
                    @Override // com.android.server.backup.remote.RemoteCallable
                    public final void call(Object obj) {
                        PerformFullTransportBackupTask.SinglePackageBackupPreflight.this.lambda$preflightFullBackup$0(iBackupAgent, j, (IBackupCallback) obj);
                    }
                }, PerformFullTransportBackupTask.this.mAgentTimeoutParameters.getQuotaExceededTimeoutMillis());
                return checkFullBackupSize;
            } catch (Exception e) {
                Slog.w(PerformFullTransportBackupTask.TAG, "Exception preflighting " + packageInfo.packageName + ": " + e.getMessage());
                return -1003;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$preflightFullBackup$0(IBackupAgent iBackupAgent, long j, IBackupCallback iBackupCallback) throws RemoteException {
            iBackupAgent.doQuotaExceeded(j, this.mQuota, iBackupCallback);
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void operationComplete(long j) {
            this.mResult.set(j);
            this.mLatch.countDown();
            PerformFullTransportBackupTask.this.mOperationStorage.removeOperation(this.mCurrentOpToken);
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void handleCancel(boolean z) {
            this.mResult.set(-1003L);
            this.mLatch.countDown();
            PerformFullTransportBackupTask.this.mOperationStorage.removeOperation(this.mCurrentOpToken);
        }

        @Override // com.android.server.backup.fullbackup.FullBackupPreflight
        public long getExpectedSizeOrErrorCode() {
            try {
                this.mLatch.await(PerformFullTransportBackupTask.this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis(), TimeUnit.MILLISECONDS);
                return this.mResult.get();
            } catch (InterruptedException unused) {
                return -1L;
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class SinglePackageBackupRunner implements Runnable, BackupRestoreTask {
        final CountDownLatch mBackupLatch;
        private volatile int mBackupResult;
        private final int mCurrentOpToken;
        private FullBackupEngine mEngine;
        private final int mEphemeralToken;
        private volatile boolean mIsCancelled;
        final ParcelFileDescriptor mOutput;
        final SinglePackageBackupPreflight mPreflight;
        final CountDownLatch mPreflightLatch;
        private volatile int mPreflightResult;
        private final long mQuota;
        final PackageInfo mTarget;
        private final int mTransportFlags;

        @Override // com.android.server.backup.BackupRestoreTask
        public void execute() {
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void operationComplete(long j) {
        }

        SinglePackageBackupRunner(ParcelFileDescriptor parcelFileDescriptor, PackageInfo packageInfo, TransportConnection transportConnection, long j, int i, int i2) throws IOException {
            this.mOutput = ParcelFileDescriptor.dup(parcelFileDescriptor.getFileDescriptor());
            this.mTarget = packageInfo;
            this.mCurrentOpToken = i;
            int generateRandomIntegerToken = PerformFullTransportBackupTask.this.mUserBackupManagerService.generateRandomIntegerToken();
            this.mEphemeralToken = generateRandomIntegerToken;
            this.mPreflight = new SinglePackageBackupPreflight(transportConnection, j, generateRandomIntegerToken, i2);
            this.mPreflightLatch = new CountDownLatch(1);
            this.mBackupLatch = new CountDownLatch(1);
            this.mPreflightResult = -1003;
            this.mBackupResult = -1003;
            this.mQuota = j;
            this.mTransportFlags = i2;
            registerTask(packageInfo.packageName);
        }

        void registerTask(String str) {
            PerformFullTransportBackupTask.this.mOperationStorage.registerOperationForPackages(this.mCurrentOpToken, 0, Sets.newHashSet(new String[]{str}), this, 0);
        }

        void unregisterTask() {
            PerformFullTransportBackupTask.this.mOperationStorage.removeOperation(this.mCurrentOpToken);
        }

        /*  JADX ERROR: JadxRuntimeException in pass: ConstructorVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r0v2 ??, still in use, count: 1, list:
              (r0v2 ?? I:com.android.server.backup.fullbackup.FullBackupEngine) from 0x003a: IPUT 
              (r0v2 ?? I:com.android.server.backup.fullbackup.FullBackupEngine)
              (r18v0 'this' ?? I:com.android.server.backup.fullbackup.PerformFullTransportBackupTask$SinglePackageBackupRunner A[IMMUTABLE_TYPE, THIS])
             com.android.server.backup.fullbackup.PerformFullTransportBackupTask.SinglePackageBackupRunner.mEngine com.android.server.backup.fullbackup.FullBackupEngine
            	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:151)
            	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:116)
            	at jadx.core.utils.InsnRemover.lambda$unbindInsns$1(InsnRemover.java:88)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
            	at jadx.core.utils.InsnRemover.unbindInsns(InsnRemover.java:87)
            	at jadx.core.utils.InsnRemover.perform(InsnRemover.java:72)
            	at jadx.core.dex.visitors.ConstructorVisitor.replaceInvoke(ConstructorVisitor.java:54)
            	at jadx.core.dex.visitors.ConstructorVisitor.visit(ConstructorVisitor.java:34)
            */
        @Override // java.lang.Runnable
        public void run() {
            /*
                r18 = this;
                r14 = r18
                java.lang.String r15 = "Error closing transport pipe in runner"
                java.lang.String r13 = "PFTBT"
                java.io.FileOutputStream r3 = new java.io.FileOutputStream
                android.os.ParcelFileDescriptor r0 = r14.mOutput
                java.io.FileDescriptor r0 = r0.getFileDescriptor()
                r3.<init>(r0)
                com.android.server.backup.fullbackup.FullBackupEngine r0 = new com.android.server.backup.fullbackup.FullBackupEngine
                com.android.server.backup.fullbackup.PerformFullTransportBackupTask r1 = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this
                com.android.server.backup.UserBackupManagerService r2 = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.m2346$$Nest$fgetmUserBackupManagerService(r1)
                com.android.server.backup.fullbackup.PerformFullTransportBackupTask$SinglePackageBackupPreflight r4 = r14.mPreflight
                android.content.pm.PackageInfo r5 = r14.mTarget
                r6 = 0
                long r8 = r14.mQuota
                int r10 = r14.mCurrentOpToken
                int r11 = r14.mTransportFlags
                com.android.server.backup.fullbackup.PerformFullTransportBackupTask r1 = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this
                com.android.server.backup.utils.BackupEligibilityRules r12 = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.m2344$$Nest$fgetmBackupEligibilityRules(r1)
                com.android.server.backup.fullbackup.PerformFullTransportBackupTask r1 = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this
                android.app.backup.IBackupManagerMonitor r16 = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.m2345$$Nest$fgetmMonitor(r1)
                r1 = r0
                r7 = r18
                r17 = r13
                r13 = r16
                r1.<init>(r2, r3, r4, r5, r6, r7, r8, r10, r11, r12, r13)
                r14.mEngine = r0
                boolean r0 = r14.mIsCancelled     // Catch: java.lang.Throwable -> L79
                if (r0 != 0) goto L48
                com.android.server.backup.fullbackup.FullBackupEngine r0 = r14.mEngine     // Catch: java.lang.Throwable -> L79
                int r0 = r0.preflightCheck()     // Catch: java.lang.Throwable -> L79
                r14.mPreflightResult = r0     // Catch: java.lang.Throwable -> L79
            L48:
                java.util.concurrent.CountDownLatch r0 = r14.mPreflightLatch     // Catch: java.lang.Throwable -> L71 java.lang.Exception -> L75
                r0.countDown()     // Catch: java.lang.Throwable -> L71 java.lang.Exception -> L75
                int r0 = r14.mPreflightResult     // Catch: java.lang.Throwable -> L71 java.lang.Exception -> L75
                if (r0 != 0) goto L5d
                boolean r0 = r14.mIsCancelled     // Catch: java.lang.Throwable -> L71 java.lang.Exception -> L75
                if (r0 != 0) goto L5d
                com.android.server.backup.fullbackup.FullBackupEngine r0 = r14.mEngine     // Catch: java.lang.Throwable -> L71 java.lang.Exception -> L75
                int r0 = r0.backupOnePackage()     // Catch: java.lang.Throwable -> L71 java.lang.Exception -> L75
                r14.mBackupResult = r0     // Catch: java.lang.Throwable -> L71 java.lang.Exception -> L75
            L5d:
                r18.unregisterTask()
                java.util.concurrent.CountDownLatch r0 = r14.mBackupLatch
                r0.countDown()
                android.os.ParcelFileDescriptor r0 = r14.mOutput     // Catch: java.io.IOException -> L6b
                r0.close()     // Catch: java.io.IOException -> L6b
                goto Laa
            L6b:
                r1 = r17
            L6d:
                android.util.Slog.w(r1, r15)
                goto Laa
            L71:
                r0 = move-exception
                r1 = r17
                goto Lab
            L75:
                r0 = move-exception
                r1 = r17
                goto L85
            L79:
                r0 = move-exception
                r1 = r17
                java.util.concurrent.CountDownLatch r2 = r14.mPreflightLatch     // Catch: java.lang.Throwable -> L82 java.lang.Exception -> L84
                r2.countDown()     // Catch: java.lang.Throwable -> L82 java.lang.Exception -> L84
                throw r0     // Catch: java.lang.Throwable -> L82 java.lang.Exception -> L84
            L82:
                r0 = move-exception
                goto Lab
            L84:
                r0 = move-exception
            L85:
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L82
                r2.<init>()     // Catch: java.lang.Throwable -> L82
                java.lang.String r3 = "Exception during full package backup of "
                r2.append(r3)     // Catch: java.lang.Throwable -> L82
                android.content.pm.PackageInfo r3 = r14.mTarget     // Catch: java.lang.Throwable -> L82
                java.lang.String r3 = r3.packageName     // Catch: java.lang.Throwable -> L82
                r2.append(r3)     // Catch: java.lang.Throwable -> L82
                java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L82
                android.util.Slog.w(r1, r2, r0)     // Catch: java.lang.Throwable -> L82
                r18.unregisterTask()
                java.util.concurrent.CountDownLatch r0 = r14.mBackupLatch
                r0.countDown()
                android.os.ParcelFileDescriptor r0 = r14.mOutput     // Catch: java.io.IOException -> L6d
                r0.close()     // Catch: java.io.IOException -> L6d
            Laa:
                return
            Lab:
                r18.unregisterTask()
                java.util.concurrent.CountDownLatch r2 = r14.mBackupLatch
                r2.countDown()
                android.os.ParcelFileDescriptor r2 = r14.mOutput     // Catch: java.io.IOException -> Lb9
                r2.close()     // Catch: java.io.IOException -> Lb9
                goto Lbc
            Lb9:
                android.util.Slog.w(r1, r15)
            Lbc:
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.fullbackup.PerformFullTransportBackupTask.SinglePackageBackupRunner.run():void");
        }

        public void sendQuotaExceeded(long j, long j2) {
            this.mEngine.sendQuotaExceeded(j, j2);
        }

        long getPreflightResultBlocking() {
            try {
                this.mPreflightLatch.await(PerformFullTransportBackupTask.this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis(), TimeUnit.MILLISECONDS);
                if (this.mIsCancelled) {
                    return -2003L;
                }
                if (this.mPreflightResult == 0) {
                    return this.mPreflight.getExpectedSizeOrErrorCode();
                }
                return this.mPreflightResult;
            } catch (InterruptedException unused) {
                return -1003L;
            }
        }

        int getBackupResultBlocking() {
            try {
                this.mBackupLatch.await(PerformFullTransportBackupTask.this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis(), TimeUnit.MILLISECONDS);
                if (this.mIsCancelled) {
                    return -2003;
                }
                return this.mBackupResult;
            } catch (InterruptedException unused) {
                return -1003;
            }
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void handleCancel(boolean z) {
            Slog.w(PerformFullTransportBackupTask.TAG, "Full backup cancel of " + this.mTarget.packageName);
            PerformFullTransportBackupTask performFullTransportBackupTask = PerformFullTransportBackupTask.this;
            performFullTransportBackupTask.mMonitor = BackupManagerMonitorUtils.monitorEvent(performFullTransportBackupTask.mMonitor, 4, PerformFullTransportBackupTask.this.mCurrentPackage, 2, null);
            this.mIsCancelled = true;
            PerformFullTransportBackupTask.this.mUserBackupManagerService.handleCancel(this.mEphemeralToken, z);
            PerformFullTransportBackupTask.this.mUserBackupManagerService.tearDownAgentAndKill(this.mTarget.applicationInfo);
            this.mPreflightLatch.countDown();
            this.mBackupLatch.countDown();
            PerformFullTransportBackupTask.this.mOperationStorage.removeOperation(this.mCurrentOpToken);
        }
    }
}
