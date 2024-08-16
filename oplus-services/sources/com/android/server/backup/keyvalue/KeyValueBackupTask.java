package com.android.server.backup.keyvalue;

import android.app.IBackupAgent;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.IBackupCallback;
import android.app.backup.IBackupManagerMonitor;
import android.app.backup.IBackupObserver;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.ConditionVariable;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.SELinux;
import android.os.WorkSource;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import com.android.server.AppWidgetBackupBridge;
import com.android.server.backup.BackupAgentTimeoutParameters;
import com.android.server.backup.BackupRestoreTask;
import com.android.server.backup.DataChangedJournal;
import com.android.server.backup.KeyValueBackupJob;
import com.android.server.backup.OperationStorage;
import com.android.server.backup.UserBackupManagerService;
import com.android.server.backup.fullbackup.PerformFullTransportBackupTask;
import com.android.server.backup.internal.OnTaskFinishedListener;
import com.android.server.backup.remote.RemoteCall;
import com.android.server.backup.remote.RemoteCallable;
import com.android.server.backup.remote.RemoteResult;
import com.android.server.backup.transport.BackupTransportClient;
import com.android.server.backup.transport.TransportConnection;
import com.android.server.backup.transport.TransportNotAvailableException;
import com.android.server.backup.utils.BackupEligibilityRules;
import com.android.server.backup.utils.BackupManagerMonitorUtils;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class KeyValueBackupTask implements BackupRestoreTask, Runnable {
    private static final String BLANK_STATE_FILE_NAME = "blank_state";

    @VisibleForTesting
    public static final String NEW_STATE_FILE_SUFFIX = ".new";

    @VisibleForTesting
    static final String NO_DATA_END_SENTINEL = "@end@";
    private static final String PM_PACKAGE = "@pm@";

    @VisibleForTesting
    public static final String STAGING_FILE_SUFFIX = ".data";
    private static final String SUCCESS_STATE_SUBDIR = "backing-up";
    private static final String TAG = "KVBT";
    private static final AtomicInteger THREAD_COUNT = new AtomicInteger();
    private static final int THREAD_PRIORITY = 10;
    private IBackupAgent mAgent;
    private final BackupAgentTimeoutParameters mAgentTimeoutParameters;
    private ParcelFileDescriptor mBackupData;
    private File mBackupDataFile;
    private final BackupEligibilityRules mBackupEligibilityRules;
    private final UserBackupManagerService mBackupManagerService;
    private final File mBlankStateFile;
    private final ConditionVariable mCancelAcknowledged = new ConditionVariable(false);
    private volatile boolean mCancelled = false;
    private final int mCurrentOpToken;
    private PackageInfo mCurrentPackage;
    private final File mDataDirectory;
    private PerformFullTransportBackupTask mFullBackupTask;
    private boolean mHasDataToBackup;
    private final DataChangedJournal mJournal;
    private ParcelFileDescriptor mNewState;
    private File mNewStateFile;
    private boolean mNonIncremental;
    private final OperationStorage mOperationStorage;
    private final List<String> mOriginalQueue;
    private final PackageManager mPackageManager;
    private volatile RemoteCall mPendingCall;
    private final List<String> mPendingFullBackups;
    private final List<String> mQueue;
    private final Object mQueueLock;
    private final KeyValueBackupReporter mReporter;
    private ParcelFileDescriptor mSavedState;
    private File mSavedStateFile;
    private final File mStateDirectory;
    private final OnTaskFinishedListener mTaskFinishedListener;
    private final TransportConnection mTransportConnection;
    private final int mUserId;
    private final boolean mUserInitiated;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private @interface StateTransaction {
        public static final int COMMIT_NEW = 0;
        public static final int DISCARD_ALL = 2;
        public static final int DISCARD_NEW = 1;
    }

    private int getBackupFinishedStatus(boolean z, int i) {
        if (z) {
            return -2003;
        }
        return (i == -1005 || i == -1002 || i == 0) ? 0 : -1000;
    }

    private int getPerformBackupFlags(boolean z, boolean z2) {
        return (z2 ? 4 : 2) | (z ? 1 : 0);
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void execute() {
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void operationComplete(long j) {
    }

    public static KeyValueBackupTask start(UserBackupManagerService userBackupManagerService, OperationStorage operationStorage, TransportConnection transportConnection, String str, List<String> list, DataChangedJournal dataChangedJournal, IBackupObserver iBackupObserver, IBackupManagerMonitor iBackupManagerMonitor, OnTaskFinishedListener onTaskFinishedListener, List<String> list2, boolean z, boolean z2, BackupEligibilityRules backupEligibilityRules) {
        KeyValueBackupTask keyValueBackupTask = new KeyValueBackupTask(userBackupManagerService, operationStorage, transportConnection, str, list, dataChangedJournal, new KeyValueBackupReporter(userBackupManagerService, iBackupObserver, iBackupManagerMonitor), onTaskFinishedListener, list2, z, z2, backupEligibilityRules);
        Thread thread = new Thread(keyValueBackupTask, "key-value-backup-" + THREAD_COUNT.incrementAndGet());
        thread.start();
        KeyValueBackupReporter.onNewThread(thread.getName());
        return keyValueBackupTask;
    }

    @VisibleForTesting
    public KeyValueBackupTask(UserBackupManagerService userBackupManagerService, OperationStorage operationStorage, TransportConnection transportConnection, String str, List<String> list, DataChangedJournal dataChangedJournal, KeyValueBackupReporter keyValueBackupReporter, OnTaskFinishedListener onTaskFinishedListener, List<String> list2, boolean z, boolean z2, BackupEligibilityRules backupEligibilityRules) {
        this.mBackupManagerService = userBackupManagerService;
        this.mOperationStorage = operationStorage;
        this.mPackageManager = userBackupManagerService.getPackageManager();
        this.mTransportConnection = transportConnection;
        this.mOriginalQueue = list;
        this.mQueue = new ArrayList(list);
        this.mJournal = dataChangedJournal;
        this.mReporter = keyValueBackupReporter;
        this.mTaskFinishedListener = onTaskFinishedListener;
        this.mPendingFullBackups = list2;
        this.mUserInitiated = z;
        this.mNonIncremental = z2;
        BackupAgentTimeoutParameters agentTimeoutParameters = userBackupManagerService.getAgentTimeoutParameters();
        Objects.requireNonNull(agentTimeoutParameters, "Timeout parameters cannot be null");
        this.mAgentTimeoutParameters = agentTimeoutParameters;
        File file = new File(userBackupManagerService.getBaseStateDir(), str);
        this.mStateDirectory = file;
        this.mDataDirectory = userBackupManagerService.getDataDir();
        this.mCurrentOpToken = userBackupManagerService.generateRandomIntegerToken();
        this.mQueueLock = userBackupManagerService.getQueueLock();
        this.mBlankStateFile = new File(file, BLANK_STATE_FILE_NAME);
        this.mUserId = userBackupManagerService.getUserId();
        this.mBackupEligibilityRules = backupEligibilityRules;
    }

    private void registerTask() {
        this.mOperationStorage.registerOperation(this.mCurrentOpToken, 0, this, 2);
    }

    private void unregisterTask() {
        this.mOperationStorage.removeOperation(this.mCurrentOpToken);
    }

    @Override // java.lang.Runnable
    public void run() {
        Process.setThreadPriority(10);
        int i = 0;
        this.mHasDataToBackup = false;
        HashSet hashSet = new HashSet();
        try {
            startTask();
            while (!this.mQueue.isEmpty() && !this.mCancelled) {
                String remove = this.mQueue.remove(0);
                try {
                    if ("@pm@".equals(remove)) {
                        backupPm();
                    } else {
                        backupPackage(remove);
                    }
                    setSuccessState(remove, true);
                    hashSet.add(remove);
                } catch (AgentException e) {
                    setSuccessState(remove, false);
                    if (e.isTransitory()) {
                        this.mBackupManagerService.dataChangedImpl(remove);
                    }
                }
            }
            informTransportOfUnchangedApps(hashSet);
        } catch (TaskException e2) {
            if (e2.isStateCompromised()) {
                this.mBackupManagerService.resetBackupState(this.mStateDirectory);
            }
            revertTask();
            i = e2.getStatus();
        }
        finishTask(i);
    }

    private void informTransportOfUnchangedApps(Set<String> set) {
        String[] succeedingPackages = getSucceedingPackages();
        if (succeedingPackages == null) {
            return;
        }
        int i = this.mUserInitiated ? 9 : 8;
        try {
            BackupTransportClient connectOrThrow = this.mTransportConnection.connectOrThrow("KVBT.informTransportOfEmptyBackups()");
            boolean z = false;
            for (String str : succeedingPackages) {
                if (set.contains(str)) {
                    Log.v(TAG, "Skipping package which was backed up this time: " + str);
                } else {
                    try {
                        PackageInfo packageInfo = this.mPackageManager.getPackageInfo(str, 0);
                        if (!isEligibleForNoDataCall(packageInfo)) {
                            clearStatus(str);
                        } else {
                            sendNoDataChangedTo(connectOrThrow, packageInfo, i);
                            z = true;
                        }
                    } catch (PackageManager.NameNotFoundException unused) {
                        clearStatus(str);
                    }
                }
            }
            if (z) {
                PackageInfo packageInfo2 = new PackageInfo();
                packageInfo2.packageName = NO_DATA_END_SENTINEL;
                sendNoDataChangedTo(connectOrThrow, packageInfo2, i);
            }
        } catch (RemoteException | TransportNotAvailableException e) {
            Log.e(TAG, "Could not inform transport of all unchanged apps", e);
        }
    }

    private boolean isEligibleForNoDataCall(PackageInfo packageInfo) {
        return this.mBackupEligibilityRules.appIsKeyValueOnly(packageInfo) && this.mBackupEligibilityRules.appIsRunningAndEligibleForBackupWithTransport(this.mTransportConnection, packageInfo.packageName);
    }

    private void sendNoDataChangedTo(BackupTransportClient backupTransportClient, PackageInfo packageInfo, int i) throws RemoteException {
        try {
            ParcelFileDescriptor open = ParcelFileDescriptor.open(this.mBlankStateFile, AudioFormat.MP2);
            try {
                int performBackup = backupTransportClient.performBackup(packageInfo, open, i);
                if (performBackup != -1000 && performBackup != -1001) {
                    backupTransportClient.finishBackup();
                    return;
                }
                Log.w(TAG, "Aborting informing transport of unchanged apps, transport errored");
            } finally {
                IoUtils.closeQuietly(open);
            }
        } catch (FileNotFoundException unused) {
            Log.e(TAG, "Unable to find blank state file, aborting unchanged apps signal.");
        }
    }

    private String[] getSucceedingPackages() {
        File topLevelSuccessStateDirectory = getTopLevelSuccessStateDirectory(false);
        if (topLevelSuccessStateDirectory == null) {
            return null;
        }
        return topLevelSuccessStateDirectory.list();
    }

    private void setSuccessState(String str, boolean z) {
        File successStateFileFor = getSuccessStateFileFor(str);
        if (successStateFileFor == null || successStateFileFor.exists() == z) {
            return;
        }
        if (!z) {
            clearStatus(str, successStateFileFor);
            return;
        }
        try {
            if (successStateFileFor.createNewFile()) {
                return;
            }
            Log.w(TAG, "Unable to permanently record success for " + str);
        } catch (IOException e) {
            Log.w(TAG, "Unable to permanently record success for " + str, e);
        }
    }

    private void clearStatus(String str) {
        File successStateFileFor = getSuccessStateFileFor(str);
        if (successStateFileFor == null) {
            return;
        }
        clearStatus(str, successStateFileFor);
    }

    private void clearStatus(String str, File file) {
        if (!file.exists() || file.delete()) {
            return;
        }
        Log.w(TAG, "Unable to remove status file for " + str);
    }

    private File getSuccessStateFileFor(String str) {
        File topLevelSuccessStateDirectory = getTopLevelSuccessStateDirectory(true);
        if (topLevelSuccessStateDirectory == null) {
            return null;
        }
        return new File(topLevelSuccessStateDirectory, str);
    }

    private File getTopLevelSuccessStateDirectory(boolean z) {
        File file = new File(this.mStateDirectory, SUCCESS_STATE_SUBDIR);
        if (file.exists() || !z || file.mkdirs()) {
            return file;
        }
        Log.e(TAG, "Unable to create backing-up state directory");
        return null;
    }

    private int sendDataToTransport(PackageInfo packageInfo) throws AgentException, TaskException {
        try {
            return sendDataToTransport();
        } catch (IOException e) {
            this.mReporter.onAgentDataError(packageInfo.packageName, e);
            throw TaskException.causedBy(e);
        }
    }

    private void startTask() throws TaskException {
        if (this.mBackupManagerService.isBackupOperationInProgress()) {
            this.mReporter.onSkipBackup();
            throw TaskException.create();
        }
        this.mFullBackupTask = createFullBackupTask(this.mPendingFullBackups);
        registerTask();
        if (this.mQueue.isEmpty() && this.mPendingFullBackups.isEmpty()) {
            this.mReporter.onEmptyQueueAtStart();
            return;
        }
        if (this.mQueue.remove("@pm@") || !this.mNonIncremental) {
            this.mQueue.add(0, "@pm@");
        } else {
            this.mReporter.onSkipPm();
        }
        this.mReporter.onQueueReady(this.mQueue);
        File file = new File(this.mStateDirectory, "@pm@");
        try {
            BackupTransportClient connectOrThrow = this.mTransportConnection.connectOrThrow("KVBT.startTask()");
            String name = connectOrThrow.name();
            if (name.contains("EncryptedLocalTransport")) {
                this.mNonIncremental = true;
            }
            this.mReporter.onTransportReady(name);
            if (file.length() <= 0) {
                this.mReporter.onInitializeTransport(name);
                this.mBackupManagerService.resetBackupState(this.mStateDirectory);
                int initializeDevice = connectOrThrow.initializeDevice();
                this.mReporter.onTransportInitialized(initializeDevice);
                if (initializeDevice == 0) {
                } else {
                    throw TaskException.stateCompromised();
                }
            }
        } catch (TaskException e) {
            throw e;
        } catch (Exception e2) {
            this.mReporter.onInitializeTransportError(e2);
            throw TaskException.stateCompromised();
        }
    }

    private PerformFullTransportBackupTask createFullBackupTask(List<String> list) {
        return new PerformFullTransportBackupTask(this.mBackupManagerService, this.mOperationStorage, this.mTransportConnection, null, (String[]) list.toArray(new String[list.size()]), false, null, new CountDownLatch(1), this.mReporter.getObserver(), this.mReporter.getMonitor(), this.mTaskFinishedListener, this.mUserInitiated, this.mBackupEligibilityRules);
    }

    private void backupPm() throws TaskException {
        this.mReporter.onStartPackageBackup("@pm@");
        PackageInfo packageInfo = new PackageInfo();
        this.mCurrentPackage = packageInfo;
        packageInfo.packageName = "@pm@";
        try {
            try {
                extractPmAgentData(packageInfo);
                cleanUpAgentForTransportStatus(sendDataToTransport(this.mCurrentPackage));
            } catch (TaskException e) {
                throw TaskException.stateCompromised(e);
            }
        } catch (AgentException | TaskException e2) {
            this.mReporter.onExtractPmAgentDataError(e2);
            cleanUpAgentForError(e2);
            if (e2 instanceof TaskException) {
                throw ((TaskException) e2);
            }
            throw TaskException.stateCompromised(e2);
        }
    }

    private void backupPackage(String str) throws AgentException, TaskException {
        this.mReporter.onStartPackageBackup(str);
        PackageInfo packageForBackup = getPackageForBackup(str);
        this.mCurrentPackage = packageForBackup;
        try {
            extractAgentData(packageForBackup);
            BackupManagerMonitorUtils.monitorAgentLoggingResults(this.mReporter.getMonitor(), this.mCurrentPackage, this.mAgent);
            cleanUpAgentForTransportStatus(sendDataToTransport(this.mCurrentPackage));
        } catch (AgentException | TaskException e) {
            cleanUpAgentForError(e);
            throw e;
        }
    }

    private PackageInfo getPackageForBackup(String str) throws AgentException {
        try {
            PackageInfo packageInfoAsUser = this.mPackageManager.getPackageInfoAsUser(str, AudioFormat.OPUS, this.mUserId);
            ApplicationInfo applicationInfo = packageInfoAsUser.applicationInfo;
            if (!this.mBackupEligibilityRules.appIsEligibleForBackup(applicationInfo)) {
                this.mReporter.onPackageNotEligibleForBackup(str);
                throw AgentException.permanent();
            }
            if (this.mBackupEligibilityRules.appGetsFullBackup(packageInfoAsUser)) {
                this.mReporter.onPackageEligibleForFullBackup(str);
                throw AgentException.permanent();
            }
            if (!this.mBackupEligibilityRules.appIsStopped(applicationInfo)) {
                return packageInfoAsUser;
            }
            this.mReporter.onPackageStopped(str);
            throw AgentException.permanent();
        } catch (PackageManager.NameNotFoundException e) {
            this.mReporter.onAgentUnknown(str);
            throw AgentException.permanent(e);
        }
    }

    private IBackupAgent bindAgent(PackageInfo packageInfo) throws AgentException {
        String str = packageInfo.packageName;
        try {
            IBackupAgent bindToAgentSynchronous = this.mBackupManagerService.bindToAgentSynchronous(packageInfo.applicationInfo, 0, this.mBackupEligibilityRules.getBackupDestination());
            if (bindToAgentSynchronous != null) {
                return bindToAgentSynchronous;
            }
            this.mReporter.onAgentError(str);
            throw AgentException.transitory();
        } catch (SecurityException e) {
            this.mReporter.onBindAgentError(str, e);
            throw AgentException.transitory(e);
        }
    }

    private void finishTask(int i) {
        Iterator<String> it = this.mQueue.iterator();
        while (it.hasNext()) {
            this.mBackupManagerService.dataChangedImpl(it.next());
        }
        DataChangedJournal dataChangedJournal = this.mJournal;
        if (dataChangedJournal != null && !dataChangedJournal.delete()) {
            this.mReporter.onJournalDeleteFailed(this.mJournal);
        }
        long currentToken = this.mBackupManagerService.getCurrentToken();
        String str = null;
        if (this.mHasDataToBackup && i == 0 && currentToken == 0) {
            try {
                BackupTransportClient connectOrThrow = this.mTransportConnection.connectOrThrow("KVBT.finishTask()");
                str = connectOrThrow.name();
                this.mBackupManagerService.setCurrentToken(connectOrThrow.getCurrentRestoreSet());
                this.mBackupManagerService.writeRestoreTokens();
            } catch (Exception e) {
                this.mReporter.onSetCurrentTokenError(e);
            }
        }
        synchronized (this.mQueueLock) {
            this.mBackupManagerService.setBackupRunning(false);
            if (i == -1001) {
                this.mReporter.onTransportNotInitialized(str);
                try {
                    triggerTransportInitializationLocked();
                } catch (Exception e2) {
                    this.mReporter.onPendingInitializeTransportError(e2);
                    i = -1000;
                }
            }
        }
        unregisterTask();
        this.mReporter.onTaskFinished();
        if (this.mCancelled) {
            this.mCancelAcknowledged.open();
        }
        if (!this.mCancelled && i == 0 && this.mFullBackupTask != null && !this.mPendingFullBackups.isEmpty()) {
            this.mReporter.onStartFullBackup(this.mPendingFullBackups);
            new Thread(this.mFullBackupTask, "full-transport-requested").start();
            return;
        }
        PerformFullTransportBackupTask performFullTransportBackupTask = this.mFullBackupTask;
        if (performFullTransportBackupTask != null) {
            performFullTransportBackupTask.unregisterTask();
        }
        this.mTaskFinishedListener.onFinished("KVBT.finishTask()");
        this.mReporter.onBackupFinished(getBackupFinishedStatus(this.mCancelled, i));
        this.mBackupManagerService.getWakelock().release();
    }

    @GuardedBy({"mQueueLock"})
    private void triggerTransportInitializationLocked() throws Exception {
        this.mBackupManagerService.getPendingInits().add(this.mTransportConnection.connectOrThrow("KVBT.triggerTransportInitializationLocked").name());
        deletePmStateFile();
        this.mBackupManagerService.backupNow();
    }

    private void deletePmStateFile() {
        new File(this.mStateDirectory, "@pm@").delete();
    }

    private void extractPmAgentData(PackageInfo packageInfo) throws AgentException, TaskException {
        Preconditions.checkArgument(packageInfo.packageName.equals("@pm@"));
        IBackupAgent asInterface = IBackupAgent.Stub.asInterface(this.mBackupManagerService.makeMetadataAgentWithEligibilityRules(this.mBackupEligibilityRules).onBind());
        this.mAgent = asInterface;
        extractAgentData(packageInfo, asInterface);
    }

    private void extractAgentData(PackageInfo packageInfo) throws AgentException, TaskException {
        this.mBackupManagerService.setWorkSource(new WorkSource(packageInfo.applicationInfo.uid));
        try {
            IBackupAgent bindAgent = bindAgent(packageInfo);
            this.mAgent = bindAgent;
            extractAgentData(packageInfo, bindAgent);
        } finally {
            this.mBackupManagerService.setWorkSource(null);
        }
    }

    private void extractAgentData(PackageInfo packageInfo, final IBackupAgent iBackupAgent) throws AgentException, TaskException {
        String str = packageInfo.packageName;
        this.mReporter.onExtractAgentData(str);
        this.mSavedStateFile = new File(this.mStateDirectory, str);
        this.mBackupDataFile = new File(this.mDataDirectory, str + STAGING_FILE_SUFFIX);
        this.mNewStateFile = new File(this.mStateDirectory, str + NEW_STATE_FILE_SUFFIX);
        this.mReporter.onAgentFilesReady(this.mBackupDataFile);
        boolean z = false;
        try {
            this.mSavedState = ParcelFileDescriptor.open(this.mNonIncremental ? this.mBlankStateFile : this.mSavedStateFile, AudioFormat.MP2);
            this.mBackupData = ParcelFileDescriptor.open(this.mBackupDataFile, 1006632960);
            this.mNewState = ParcelFileDescriptor.open(this.mNewStateFile, 1006632960);
            if (this.mUserId == 0 && !SELinux.restorecon(this.mBackupDataFile)) {
                this.mReporter.onRestoreconFailed(this.mBackupDataFile);
            }
            BackupTransportClient connectOrThrow = this.mTransportConnection.connectOrThrow("KVBT.extractAgentData()");
            final long backupQuota = connectOrThrow.getBackupQuota(str, false);
            final int transportFlags = connectOrThrow.getTransportFlags();
            z = true;
            checkAgentResult(packageInfo, remoteCall(new RemoteCallable() { // from class: com.android.server.backup.keyvalue.KeyValueBackupTask$$ExternalSyntheticLambda1
                @Override // com.android.server.backup.remote.RemoteCallable
                public final void call(Object obj) {
                    KeyValueBackupTask.this.lambda$extractAgentData$0(iBackupAgent, backupQuota, transportFlags, (IBackupCallback) obj);
                }
            }, this.mAgentTimeoutParameters.getKvBackupAgentTimeoutMillis(), "doBackup()"));
        } catch (Exception e) {
            this.mReporter.onCallAgentDoBackupError(str, z, e);
            if (z) {
                throw AgentException.transitory(e);
            }
            throw TaskException.create();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$extractAgentData$0(IBackupAgent iBackupAgent, long j, int i, IBackupCallback iBackupCallback) throws RemoteException {
        iBackupAgent.doBackup(this.mSavedState, this.mBackupData, this.mNewState, j, iBackupCallback, i);
    }

    private void checkAgentResult(PackageInfo packageInfo, RemoteResult remoteResult) throws AgentException, TaskException {
        if (remoteResult == RemoteResult.FAILED_THREAD_INTERRUPTED) {
            this.mCancelled = true;
            this.mReporter.onAgentCancelled(packageInfo);
            throw TaskException.create();
        }
        if (remoteResult == RemoteResult.FAILED_CANCELLED) {
            this.mReporter.onAgentCancelled(packageInfo);
            throw TaskException.create();
        }
        if (remoteResult == RemoteResult.FAILED_TIMED_OUT) {
            this.mReporter.onAgentTimedOut(packageInfo);
            throw AgentException.transitory();
        }
        Preconditions.checkState(remoteResult.isPresent());
        long j = remoteResult.get();
        if (j == -1) {
            this.mReporter.onAgentResultError(packageInfo);
            throw AgentException.transitory();
        }
        Preconditions.checkState(j == 0);
    }

    private void agentFail(IBackupAgent iBackupAgent, String str) {
        try {
            iBackupAgent.fail(str);
        } catch (Exception unused) {
            this.mReporter.onFailAgentError(this.mCurrentPackage.packageName);
        }
    }

    private String SHA1Checksum(byte[] bArr) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-1").digest(bArr);
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(Integer.toHexString(b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            this.mReporter.onDigestError(e);
            return "00";
        }
    }

    private void writeWidgetPayloadIfAppropriate(FileDescriptor fileDescriptor, String str) throws IOException {
        String str2;
        byte[] widgetState = AppWidgetBackupBridge.getWidgetState(str, this.mUserId);
        File file = new File(this.mStateDirectory, str + "_widget");
        boolean exists = file.exists();
        if (exists || widgetState != null) {
            this.mReporter.onWriteWidgetData(exists, widgetState);
            if (widgetState != null) {
                str2 = SHA1Checksum(widgetState);
                if (exists) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    try {
                        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
                        try {
                            String readUTF = dataInputStream.readUTF();
                            dataInputStream.close();
                            fileInputStream.close();
                            if (Objects.equals(str2, readUTF)) {
                                return;
                            }
                        } finally {
                        }
                    } catch (Throwable th) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
            } else {
                str2 = null;
            }
            BackupDataOutput backupDataOutput = new BackupDataOutput(fileDescriptor);
            if (widgetState != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                try {
                    DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
                    try {
                        dataOutputStream.writeUTF(str2);
                        dataOutputStream.close();
                        fileOutputStream.close();
                        backupDataOutput.writeEntityHeader(UserBackupManagerService.KEY_WIDGET_STATE, widgetState.length);
                        backupDataOutput.writeEntityData(widgetState, widgetState.length);
                        return;
                    } finally {
                    }
                } catch (Throwable th3) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                    }
                    throw th3;
                }
            }
            backupDataOutput.writeEntityHeader(UserBackupManagerService.KEY_WIDGET_STATE, -1);
            file.delete();
        }
    }

    private int sendDataToTransport() throws AgentException, TaskException, IOException {
        Preconditions.checkState(this.mBackupData != null);
        checkBackupData(this.mCurrentPackage.applicationInfo, this.mBackupDataFile);
        String str = this.mCurrentPackage.packageName;
        writeWidgetPayloadIfAppropriate(this.mBackupData.getFileDescriptor(), str);
        int transportPerformBackup = transportPerformBackup(this.mCurrentPackage, this.mBackupDataFile, this.mSavedStateFile.length() == 0);
        handleTransportStatus(transportPerformBackup, str, this.mBackupDataFile.length());
        return transportPerformBackup;
    }

    private int transportPerformBackup(PackageInfo packageInfo, File file, boolean z) throws TaskException {
        String str = packageInfo.packageName;
        if (file.length() <= 0) {
            this.mReporter.onEmptyData(packageInfo);
            return 0;
        }
        this.mHasDataToBackup = true;
        try {
            ParcelFileDescriptor open = ParcelFileDescriptor.open(file, AudioFormat.EVRC);
            try {
                BackupTransportClient connectOrThrow = this.mTransportConnection.connectOrThrow("KVBT.transportPerformBackup()");
                this.mReporter.onTransportPerformBackup(str);
                int performBackup = connectOrThrow.performBackup(packageInfo, open, getPerformBackupFlags(this.mUserInitiated, z));
                if (performBackup == 0) {
                    performBackup = connectOrThrow.finishBackup();
                } else if (performBackup == -1001) {
                    this.mReporter.onTransportNotInitialized(connectOrThrow.name());
                }
                if (open != null) {
                    open.close();
                }
                if (!z || performBackup != -1006) {
                    return performBackup;
                }
                this.mReporter.onPackageBackupNonIncrementalAndNonIncrementalRequired(str);
                throw TaskException.create();
            } finally {
            }
        } catch (Exception e) {
            this.mReporter.onPackageBackupTransportError(str, e);
            throw TaskException.causedBy(e);
        }
    }

    private void handleTransportStatus(int i, String str, long j) throws TaskException, AgentException {
        if (i == 0) {
            this.mReporter.onPackageBackupComplete(str, j);
            return;
        }
        if (i == -1006) {
            this.mReporter.onPackageBackupNonIncrementalRequired(this.mCurrentPackage);
            this.mQueue.add(0, str);
        } else {
            if (i == -1002) {
                this.mReporter.onPackageBackupRejected(str);
                throw AgentException.permanent();
            }
            if (i == -1005) {
                this.mReporter.onPackageBackupQuotaExceeded(str);
                agentDoQuotaExceeded(this.mAgent, str, j);
                throw AgentException.permanent();
            }
            this.mReporter.onPackageBackupTransportFailure(str);
            throw TaskException.forStatus(i);
        }
    }

    private void agentDoQuotaExceeded(final IBackupAgent iBackupAgent, String str, final long j) {
        if (iBackupAgent != null) {
            try {
                final long backupQuota = this.mTransportConnection.connectOrThrow("KVBT.agentDoQuotaExceeded()").getBackupQuota(str, false);
                remoteCall(new RemoteCallable() { // from class: com.android.server.backup.keyvalue.KeyValueBackupTask$$ExternalSyntheticLambda0
                    @Override // com.android.server.backup.remote.RemoteCallable
                    public final void call(Object obj) {
                        iBackupAgent.doQuotaExceeded(j, backupQuota, (IBackupCallback) obj);
                    }
                }, this.mAgentTimeoutParameters.getQuotaExceededTimeoutMillis(), "doQuotaExceeded()");
            } catch (Exception e) {
                this.mReporter.onAgentDoQuotaExceededError(e);
            }
        }
    }

    private void checkBackupData(ApplicationInfo applicationInfo, File file) throws IOException, AgentException {
        if (applicationInfo == null || (applicationInfo.flags & 1) != 0) {
            return;
        }
        ParcelFileDescriptor open = ParcelFileDescriptor.open(file, AudioFormat.EVRC);
        try {
            BackupDataInput backupDataInput = new BackupDataInput(open.getFileDescriptor());
            while (backupDataInput.readNextHeader()) {
                String key = backupDataInput.getKey();
                if (key != null && key.charAt(0) >= 65280) {
                    this.mReporter.onAgentIllegalKey(this.mCurrentPackage, key);
                    agentFail(this.mAgent, "Illegal backup key: " + key);
                    throw AgentException.permanent();
                }
                backupDataInput.skipEntityData();
            }
            open.close();
        } catch (Throwable th) {
            if (open != null) {
                try {
                    open.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void handleCancel(boolean z) {
        Preconditions.checkArgument(z, "Can't partially cancel a key-value backup task");
        markCancel();
        waitCancel();
    }

    @VisibleForTesting
    public void markCancel() {
        this.mReporter.onCancel();
        this.mCancelled = true;
        RemoteCall remoteCall = this.mPendingCall;
        if (remoteCall != null) {
            remoteCall.cancel();
        }
    }

    @VisibleForTesting
    public void waitCancel() {
        this.mCancelAcknowledged.block();
    }

    private void revertTask() {
        long j;
        this.mReporter.onRevertTask();
        try {
            j = this.mTransportConnection.connectOrThrow("KVBT.revertTask()").requestBackupTime();
        } catch (Exception e) {
            this.mReporter.onTransportRequestBackupTimeError(e);
            j = 0;
        }
        KeyValueBackupJob.schedule(this.mBackupManagerService.getUserId(), this.mBackupManagerService.getContext(), j, this.mBackupManagerService);
        Iterator<String> it = this.mOriginalQueue.iterator();
        while (it.hasNext()) {
            this.mBackupManagerService.dataChangedImpl(it.next());
        }
    }

    private void cleanUpAgentForError(BackupException backupException) {
        cleanUpAgent(1);
    }

    private void cleanUpAgentForTransportStatus(int i) {
        if (i == -1006) {
            cleanUpAgent(2);
        } else {
            if (i == 0) {
                cleanUpAgent(0);
                return;
            }
            throw new AssertionError();
        }
    }

    private void cleanUpAgent(int i) {
        applyStateTransaction(i);
        File file = this.mBackupDataFile;
        if (file != null) {
            file.delete();
        }
        this.mBlankStateFile.delete();
        this.mSavedStateFile = null;
        this.mBackupDataFile = null;
        this.mNewStateFile = null;
        tryCloseFileDescriptor(this.mSavedState, "old state");
        tryCloseFileDescriptor(this.mBackupData, "backup data");
        tryCloseFileDescriptor(this.mNewState, "new state");
        this.mSavedState = null;
        this.mBackupData = null;
        this.mNewState = null;
        ApplicationInfo applicationInfo = this.mCurrentPackage.applicationInfo;
        if (applicationInfo != null) {
            this.mBackupManagerService.unbindAgent(applicationInfo);
        }
        this.mAgent = null;
    }

    private void applyStateTransaction(int i) {
        if (i == 0) {
            this.mNewStateFile.renameTo(this.mSavedStateFile);
            return;
        }
        if (i == 1) {
            File file = this.mNewStateFile;
            if (file != null) {
                file.delete();
                return;
            }
            return;
        }
        if (i == 2) {
            this.mSavedStateFile.delete();
            this.mNewStateFile.delete();
        } else {
            throw new IllegalArgumentException("Unknown state transaction " + i);
        }
    }

    private void tryCloseFileDescriptor(Closeable closeable, String str) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
                this.mReporter.onCloseFileDescriptorError(str);
            }
        }
    }

    private RemoteResult remoteCall(RemoteCallable<IBackupCallback> remoteCallable, long j, String str) throws RemoteException {
        this.mPendingCall = new RemoteCall(this.mCancelled, remoteCallable, j);
        RemoteResult call = this.mPendingCall.call();
        this.mReporter.onRemoteCallReturned(call, str);
        this.mPendingCall = null;
        return call;
    }
}
