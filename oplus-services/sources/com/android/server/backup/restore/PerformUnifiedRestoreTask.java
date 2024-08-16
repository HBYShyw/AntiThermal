package com.android.server.backup.restore;

import android.app.IActivityManager;
import android.app.IBackupAgent;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.IBackupManagerMonitor;
import android.app.backup.IRestoreObserver;
import android.app.backup.RestoreDescription;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.EventLog;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.AppWidgetBackupBridge;
import com.android.server.EventLogTags;
import com.android.server.LocalServices;
import com.android.server.backup.BackupAgentTimeoutParameters;
import com.android.server.backup.BackupAndRestoreFeatureFlags;
import com.android.server.backup.BackupManagerService;
import com.android.server.backup.BackupRestoreTask;
import com.android.server.backup.BackupUtils;
import com.android.server.backup.OperationStorage;
import com.android.server.backup.PackageManagerBackupAgent;
import com.android.server.backup.TransportManager;
import com.android.server.backup.UserBackupManagerService;
import com.android.server.backup.internal.OnTaskFinishedListener;
import com.android.server.backup.keyvalue.KeyValueBackupTask;
import com.android.server.backup.transport.BackupTransportClient;
import com.android.server.backup.transport.TransportConnection;
import com.android.server.backup.utils.BackupEligibilityRules;
import com.android.server.backup.utils.BackupManagerMonitorUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class PerformUnifiedRestoreTask implements BackupRestoreTask {
    private UserBackupManagerService backupManagerService;
    private List<PackageInfo> mAcceptSet;
    private IBackupAgent mAgent;
    private final BackupAgentTimeoutParameters mAgentTimeoutParameters;
    private ParcelFileDescriptor mBackupData;
    private File mBackupDataName;
    private final BackupEligibilityRules mBackupEligibilityRules;
    private int mCount;
    private PackageInfo mCurrentPackage;
    private boolean mDidLaunch;
    private final int mEphemeralOpToken;
    private boolean mFinished;
    private boolean mIsSystemRestore;
    private final OnTaskFinishedListener mListener;
    private IBackupManagerMonitor mMonitor;
    private ParcelFileDescriptor mNewState;
    private File mNewStateName;
    private IRestoreObserver mObserver;
    private final OperationStorage mOperationStorage;
    private PackageManagerBackupAgent mPmAgent;
    private int mPmToken;
    private RestoreDescription mRestoreDescription;
    private File mStageName;
    private long mStartRealtime;
    private UnifiedRestoreState mState;
    private File mStateDir;
    private int mStatus;
    private PackageInfo mTargetPackage;
    private long mToken;
    private final TransportConnection mTransportConnection;
    private final TransportManager mTransportManager;
    private final int mUserId;
    private byte[] mWidgetData;

    @VisibleForTesting
    PerformUnifiedRestoreTask(UserBackupManagerService userBackupManagerService, TransportConnection transportConnection) {
        this.mListener = null;
        this.mAgentTimeoutParameters = null;
        this.mOperationStorage = null;
        this.mTransportConnection = transportConnection;
        this.mTransportManager = null;
        this.mEphemeralOpToken = 0;
        this.mUserId = 0;
        this.mBackupEligibilityRules = null;
        this.backupManagerService = userBackupManagerService;
    }

    public PerformUnifiedRestoreTask(UserBackupManagerService userBackupManagerService, OperationStorage operationStorage, TransportConnection transportConnection, IRestoreObserver iRestoreObserver, IBackupManagerMonitor iBackupManagerMonitor, long j, PackageInfo packageInfo, int i, boolean z, String[] strArr, OnTaskFinishedListener onTaskFinishedListener, BackupEligibilityRules backupEligibilityRules) {
        this.backupManagerService = userBackupManagerService;
        this.mOperationStorage = operationStorage;
        int userId = userBackupManagerService.getUserId();
        this.mUserId = userId;
        this.mTransportManager = userBackupManagerService.getTransportManager();
        this.mEphemeralOpToken = userBackupManagerService.generateRandomIntegerToken();
        this.mState = UnifiedRestoreState.INITIAL;
        this.mStartRealtime = SystemClock.elapsedRealtime();
        this.mTransportConnection = transportConnection;
        this.mObserver = iRestoreObserver;
        this.mMonitor = iBackupManagerMonitor;
        this.mToken = j;
        this.mPmToken = i;
        this.mTargetPackage = packageInfo;
        this.mIsSystemRestore = z;
        this.mFinished = false;
        this.mDidLaunch = false;
        this.mListener = onTaskFinishedListener;
        BackupAgentTimeoutParameters agentTimeoutParameters = userBackupManagerService.getAgentTimeoutParameters();
        Objects.requireNonNull(agentTimeoutParameters, "Timeout parameters cannot be null");
        this.mAgentTimeoutParameters = agentTimeoutParameters;
        this.mBackupEligibilityRules = backupEligibilityRules;
        if (packageInfo != null) {
            ArrayList arrayList = new ArrayList();
            this.mAcceptSet = arrayList;
            arrayList.add(packageInfo);
        } else {
            if (strArr == null) {
                strArr = packagesToNames(PackageManagerBackupAgent.getStorableApplications(userBackupManagerService.getPackageManager(), userId, backupEligibilityRules));
                Slog.i(BackupManagerService.TAG, "Full restore; asking about " + strArr.length + " apps");
            }
            this.mAcceptSet = new ArrayList(strArr.length);
            boolean z2 = false;
            boolean z3 = false;
            for (String str : strArr) {
                try {
                    PackageInfo packageInfoAsUser = userBackupManagerService.getPackageManager().getPackageInfoAsUser(str, 0, this.mUserId);
                    if ("android".equals(packageInfoAsUser.packageName)) {
                        z2 = true;
                    } else if (UserBackupManagerService.SETTINGS_PACKAGE.equals(packageInfoAsUser.packageName)) {
                        z3 = true;
                    } else if (backupEligibilityRules.appIsEligibleForBackup(packageInfoAsUser.applicationInfo)) {
                        this.mAcceptSet.add(packageInfoAsUser);
                    }
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
            if (z2) {
                try {
                    this.mAcceptSet.add(0, userBackupManagerService.getPackageManager().getPackageInfoAsUser("android", 0, this.mUserId));
                } catch (PackageManager.NameNotFoundException unused2) {
                }
            }
            if (z3) {
                try {
                    this.mAcceptSet.add(userBackupManagerService.getPackageManager().getPackageInfoAsUser(UserBackupManagerService.SETTINGS_PACKAGE, 0, this.mUserId));
                } catch (PackageManager.NameNotFoundException unused3) {
                }
            }
        }
        this.mAcceptSet = userBackupManagerService.filterUserFacingPackages(this.mAcceptSet);
    }

    private String[] packagesToNames(List<PackageInfo> list) {
        int size = list.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = list.get(i).packageName;
        }
        return strArr;
    }

    /* renamed from: com.android.server.backup.restore.PerformUnifiedRestoreTask$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$server$backup$restore$UnifiedRestoreState;

        static {
            int[] iArr = new int[UnifiedRestoreState.values().length];
            $SwitchMap$com$android$server$backup$restore$UnifiedRestoreState = iArr;
            try {
                iArr[UnifiedRestoreState.INITIAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$server$backup$restore$UnifiedRestoreState[UnifiedRestoreState.RUNNING_QUEUE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$server$backup$restore$UnifiedRestoreState[UnifiedRestoreState.RESTORE_KEYVALUE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$server$backup$restore$UnifiedRestoreState[UnifiedRestoreState.RESTORE_FULL.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$server$backup$restore$UnifiedRestoreState[UnifiedRestoreState.RESTORE_FINISHED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$server$backup$restore$UnifiedRestoreState[UnifiedRestoreState.FINAL.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void execute() {
        switch (AnonymousClass1.$SwitchMap$com$android$server$backup$restore$UnifiedRestoreState[this.mState.ordinal()]) {
            case 1:
                startRestore();
                return;
            case 2:
                dispatchNextRestore();
                return;
            case 3:
                restoreKeyValue();
                return;
            case 4:
                restoreFull();
                return;
            case 5:
                restoreFinished();
                return;
            case 6:
                if (!this.mFinished) {
                    finalizeRestore();
                } else {
                    Slog.e(BackupManagerService.TAG, "Duplicate finish");
                }
                this.mFinished = true;
                return;
            default:
                return;
        }
    }

    private void startRestore() {
        sendStartRestore(this.mAcceptSet.size());
        if (this.mIsSystemRestore) {
            AppWidgetBackupBridge.systemRestoreStarting(this.mUserId);
        }
        try {
            this.mStateDir = new File(this.backupManagerService.getBaseStateDir(), this.mTransportManager.getTransportDirName(this.mTransportConnection.getTransportComponent()));
            PackageInfo packageInfo = new PackageInfo();
            packageInfo.packageName = UserBackupManagerService.PACKAGE_MANAGER_SENTINEL;
            this.mAcceptSet.add(0, packageInfo);
            PackageInfo[] packageInfoArr = (PackageInfo[]) this.mAcceptSet.toArray(new PackageInfo[0]);
            BackupTransportClient connectOrThrow = this.mTransportConnection.connectOrThrow("PerformUnifiedRestoreTask.startRestore()");
            if (this.mMonitor == null) {
                this.mMonitor = connectOrThrow.getBackupManagerMonitor();
            }
            int startRestore = connectOrThrow.startRestore(this.mToken, packageInfoArr);
            this.mStatus = startRestore;
            if (startRestore != 0) {
                Slog.e(BackupManagerService.TAG, "Transport error " + this.mStatus + "; no restore possible");
                this.mStatus = -1000;
                executeNextState(UnifiedRestoreState.FINAL);
                return;
            }
            RestoreDescription nextRestorePackage = connectOrThrow.nextRestorePackage();
            if (nextRestorePackage == null) {
                Slog.e(BackupManagerService.TAG, "No restore metadata available; halting");
                this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 22, this.mCurrentPackage, 3, null);
                this.mStatus = -1000;
                executeNextState(UnifiedRestoreState.FINAL);
                return;
            }
            if (!UserBackupManagerService.PACKAGE_MANAGER_SENTINEL.equals(nextRestorePackage.getPackageName())) {
                Slog.e(BackupManagerService.TAG, "Required package metadata but got " + nextRestorePackage.getPackageName());
                this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 23, this.mCurrentPackage, 3, null);
                this.mStatus = -1000;
                executeNextState(UnifiedRestoreState.FINAL);
                return;
            }
            PackageInfo packageInfo2 = new PackageInfo();
            this.mCurrentPackage = packageInfo2;
            packageInfo2.packageName = UserBackupManagerService.PACKAGE_MANAGER_SENTINEL;
            packageInfo2.applicationInfo = new ApplicationInfo();
            this.mCurrentPackage.applicationInfo.uid = 1000;
            PackageManagerBackupAgent makeMetadataAgent = this.backupManagerService.makeMetadataAgent(null);
            this.mPmAgent = makeMetadataAgent;
            this.mAgent = IBackupAgent.Stub.asInterface(makeMetadataAgent.onBind());
            initiateOneRestore(this.mCurrentPackage, 0L);
            this.backupManagerService.getBackupHandler().removeMessages(18);
            if (this.mPmAgent.hasMetadata()) {
                return;
            }
            Slog.e(BackupManagerService.TAG, "PM agent has no metadata, so not restoring");
            this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 24, this.mCurrentPackage, 3, null);
            EventLog.writeEvent(EventLogTags.RESTORE_AGENT_FAILURE, UserBackupManagerService.PACKAGE_MANAGER_SENTINEL, "Package manager restore metadata missing");
            this.mStatus = -1000;
            this.backupManagerService.getBackupHandler().removeMessages(20, this);
            executeNextState(UnifiedRestoreState.FINAL);
        } catch (Exception e) {
            Slog.e(BackupManagerService.TAG, "Unable to contact transport for restore: " + e.getMessage());
            this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 25, null, 1, null);
            this.mStatus = -1000;
            this.backupManagerService.getBackupHandler().removeMessages(20, this);
            executeNextState(UnifiedRestoreState.FINAL);
        }
    }

    private void dispatchNextRestore() {
        UnifiedRestoreState unifiedRestoreState;
        UnifiedRestoreState unifiedRestoreState2 = UnifiedRestoreState.FINAL;
        try {
            RestoreDescription nextRestorePackage = this.mTransportConnection.connectOrThrow("PerformUnifiedRestoreTask.dispatchNextRestore()").nextRestorePackage();
            this.mRestoreDescription = nextRestorePackage;
            String packageName = nextRestorePackage != null ? nextRestorePackage.getPackageName() : null;
            if (packageName == null) {
                Slog.e(BackupManagerService.TAG, "Failure getting next package name");
                EventLog.writeEvent(EventLogTags.RESTORE_TRANSPORT_FAILURE, new Object[0]);
                return;
            }
            if (this.mRestoreDescription == RestoreDescription.NO_MORE_PACKAGES) {
                Slog.v(BackupManagerService.TAG, "No more packages; finishing restore");
                EventLog.writeEvent(EventLogTags.RESTORE_SUCCESS, Integer.valueOf(this.mCount), Integer.valueOf((int) (SystemClock.elapsedRealtime() - this.mStartRealtime)));
                return;
            }
            Slog.i(BackupManagerService.TAG, "Next restore package: " + this.mRestoreDescription);
            sendOnRestorePackage(packageName);
            PackageManagerBackupAgent.Metadata restoredMetadata = this.mPmAgent.getRestoredMetadata(packageName);
            if (restoredMetadata == null) {
                Slog.e(BackupManagerService.TAG, "No metadata for " + packageName);
                EventLog.writeEvent(EventLogTags.RESTORE_AGENT_FAILURE, packageName, "Package metadata missing");
                executeNextState(UnifiedRestoreState.RUNNING_QUEUE);
                return;
            }
            try {
                PackageInfo packageInfoAsUser = this.backupManagerService.getPackageManager().getPackageInfoAsUser(packageName, AudioFormat.OPUS, this.mUserId);
                this.mCurrentPackage = packageInfoAsUser;
                if (restoredMetadata.versionCode > packageInfoAsUser.getLongVersionCode()) {
                    if ((this.mCurrentPackage.applicationInfo.flags & 131072) == 0) {
                        String str = "Source version " + restoredMetadata.versionCode + " > installed version " + this.mCurrentPackage.getLongVersionCode();
                        Slog.w(BackupManagerService.TAG, "Package " + packageName + ": " + str);
                        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 27, this.mCurrentPackage, 3, BackupManagerMonitorUtils.putMonitoringExtra(BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_RESTORE_VERSION", restoredMetadata.versionCode), "android.app.backup.extra.LOG_RESTORE_ANYWAY", false));
                        EventLog.writeEvent(EventLogTags.RESTORE_AGENT_FAILURE, packageName, str);
                        executeNextState(UnifiedRestoreState.RUNNING_QUEUE);
                        return;
                    }
                    Slog.v(BackupManagerService.TAG, "Source version " + restoredMetadata.versionCode + " > installed version " + this.mCurrentPackage.getLongVersionCode() + " but restoreAnyVersion");
                    this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 27, this.mCurrentPackage, 3, BackupManagerMonitorUtils.putMonitoringExtra(BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_RESTORE_VERSION", restoredMetadata.versionCode), "android.app.backup.extra.LOG_RESTORE_ANYWAY", true));
                }
                this.mWidgetData = null;
                int dataType = this.mRestoreDescription.getDataType();
                if (dataType == 1) {
                    unifiedRestoreState = UnifiedRestoreState.RESTORE_KEYVALUE;
                } else {
                    if (dataType != 2) {
                        Slog.e(BackupManagerService.TAG, "Unrecognized restore type " + dataType);
                        executeNextState(UnifiedRestoreState.RUNNING_QUEUE);
                        return;
                    }
                    unifiedRestoreState = UnifiedRestoreState.RESTORE_FULL;
                }
                executeNextState(unifiedRestoreState);
            } catch (PackageManager.NameNotFoundException unused) {
                Slog.e(BackupManagerService.TAG, "Package not present: " + packageName);
                this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 26, this.mCurrentPackage, 3, null);
                EventLog.writeEvent(EventLogTags.RESTORE_AGENT_FAILURE, packageName, "Package missing on device");
                executeNextState(UnifiedRestoreState.RUNNING_QUEUE);
            }
        } catch (Exception e) {
            Slog.e(BackupManagerService.TAG, "Can't get next restore target from transport; halting: " + e.getMessage());
            EventLog.writeEvent(EventLogTags.RESTORE_TRANSPORT_FAILURE, new Object[0]);
            unifiedRestoreState2 = UnifiedRestoreState.FINAL;
        } finally {
            executeNextState(unifiedRestoreState2);
        }
    }

    private void restoreKeyValue() {
        PackageInfo packageInfo = this.mCurrentPackage;
        String str = packageInfo.packageName;
        String str2 = packageInfo.applicationInfo.backupAgentName;
        if (str2 == null || "".equals(str2)) {
            this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 28, this.mCurrentPackage, 2, null);
            EventLog.writeEvent(EventLogTags.RESTORE_AGENT_FAILURE, str, "Package has no agent");
            executeNextState(UnifiedRestoreState.RUNNING_QUEUE);
            return;
        }
        PackageManagerBackupAgent.Metadata restoredMetadata = this.mPmAgent.getRestoredMetadata(str);
        if (!BackupUtils.signaturesMatch(restoredMetadata.sigHashes, this.mCurrentPackage, (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class))) {
            Slog.w(BackupManagerService.TAG, "Signature mismatch restoring " + str);
            this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 29, this.mCurrentPackage, 3, null);
            EventLog.writeEvent(EventLogTags.RESTORE_AGENT_FAILURE, str, "Signature mismatch");
            executeNextState(UnifiedRestoreState.RUNNING_QUEUE);
            return;
        }
        IBackupAgent bindToAgentSynchronous = this.backupManagerService.bindToAgentSynchronous(this.mCurrentPackage.applicationInfo, 2, this.mBackupEligibilityRules.getBackupDestination());
        this.mAgent = bindToAgentSynchronous;
        if (bindToAgentSynchronous == null) {
            Slog.w(BackupManagerService.TAG, "Can't find backup agent for " + str);
            this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 30, this.mCurrentPackage, 3, null);
            EventLog.writeEvent(EventLogTags.RESTORE_AGENT_FAILURE, str, "Restore agent missing");
            executeNextState(UnifiedRestoreState.RUNNING_QUEUE);
            return;
        }
        this.mDidLaunch = true;
        try {
            initiateOneRestore(this.mCurrentPackage, restoredMetadata.versionCode);
            this.mCount++;
        } catch (Exception e) {
            Slog.e(BackupManagerService.TAG, "Error when attempting restore: " + e.toString());
            keyValueAgentErrorCleanup(false);
            executeNextState(UnifiedRestoreState.RUNNING_QUEUE);
        }
    }

    private void initiateOneRestore(PackageInfo packageInfo, long j) {
        UnifiedRestoreState unifiedRestoreState;
        String str = packageInfo.packageName;
        Slog.d(BackupManagerService.TAG, "initiateOneRestore packageName=" + str);
        this.mBackupDataName = new File(this.backupManagerService.getDataDir(), str + ".restore");
        this.mStageName = new File(this.backupManagerService.getDataDir(), str + ".stage");
        this.mNewStateName = new File(this.mStateDir, str + KeyValueBackupTask.NEW_STATE_FILE_SUFFIX);
        boolean shouldStageBackupData = shouldStageBackupData(str);
        File file = shouldStageBackupData ? this.mStageName : this.mBackupDataName;
        try {
            BackupTransportClient connectOrThrow = this.mTransportConnection.connectOrThrow("PerformUnifiedRestoreTask.initiateOneRestore()");
            ParcelFileDescriptor open = ParcelFileDescriptor.open(file, 1006632960);
            if (connectOrThrow.getRestoreData(open) != 0) {
                Slog.e(BackupManagerService.TAG, "Error getting restore data for " + str);
                EventLog.writeEvent(EventLogTags.RESTORE_TRANSPORT_FAILURE, new Object[0]);
                open.close();
                file.delete();
                if (BackupAndRestoreFeatureFlags.getUnifiedRestoreContinueAfterTransportFailureInKvRestore()) {
                    unifiedRestoreState = UnifiedRestoreState.RUNNING_QUEUE;
                } else {
                    unifiedRestoreState = UnifiedRestoreState.FINAL;
                }
                executeNextState(unifiedRestoreState);
                return;
            }
            if (shouldStageBackupData) {
                open.close();
                open = ParcelFileDescriptor.open(file, AudioFormat.EVRC);
                this.mBackupData = ParcelFileDescriptor.open(this.mBackupDataName, 1006632960);
                filterExcludedKeys(str, new BackupDataInput(open.getFileDescriptor()), new BackupDataOutput(this.mBackupData.getFileDescriptor()));
                this.mBackupData.close();
            }
            open.close();
            this.mBackupData = ParcelFileDescriptor.open(this.mBackupDataName, AudioFormat.EVRC);
            this.mNewState = ParcelFileDescriptor.open(this.mNewStateName, 1006632960);
            this.backupManagerService.prepareOperationTimeout(this.mEphemeralOpToken, this.mAgentTimeoutParameters.getRestoreAgentTimeoutMillis(packageInfo.applicationInfo.uid), this, 1);
            this.mAgent.doRestoreWithExcludedKeys(this.mBackupData, j, this.mNewState, this.mEphemeralOpToken, this.backupManagerService.getBackupManagerBinder(), new ArrayList(getExcludedKeysForPackage(str)));
        } catch (Exception e) {
            Slog.e(BackupManagerService.TAG, "Unable to call app for restore: " + str, e);
            EventLog.writeEvent(EventLogTags.RESTORE_AGENT_FAILURE, str, e.toString());
            keyValueAgentErrorCleanup(false);
            executeNextState(UnifiedRestoreState.RUNNING_QUEUE);
        }
    }

    @VisibleForTesting
    boolean shouldStageBackupData(String str) {
        return (str.equals("android") && getExcludedKeysForPackage("android").isEmpty()) ? false : true;
    }

    @VisibleForTesting
    Set<String> getExcludedKeysForPackage(String str) {
        return this.backupManagerService.getExcludedRestoreKeys(str);
    }

    @VisibleForTesting
    void filterExcludedKeys(String str, BackupDataInput backupDataInput, BackupDataOutput backupDataOutput) throws Exception {
        Set<String> excludedKeysForPackage = getExcludedKeysForPackage(str);
        byte[] bArr = new byte[8192];
        while (backupDataInput.readNextHeader()) {
            String key = backupDataInput.getKey();
            int dataSize = backupDataInput.getDataSize();
            if (excludedKeysForPackage != null && excludedKeysForPackage.contains(key)) {
                Slog.i(BackupManagerService.TAG, "Skipping blocked key " + key);
                backupDataInput.skipEntityData();
            } else if (key.equals(UserBackupManagerService.KEY_WIDGET_STATE)) {
                Slog.i(BackupManagerService.TAG, "Restoring widget state for " + str);
                byte[] bArr2 = new byte[dataSize];
                this.mWidgetData = bArr2;
                backupDataInput.readEntityData(bArr2, 0, dataSize);
            } else {
                if (dataSize > bArr.length) {
                    bArr = new byte[dataSize];
                }
                backupDataInput.readEntityData(bArr, 0, dataSize);
                backupDataOutput.writeEntityHeader(key, dataSize);
                backupDataOutput.writeEntityData(bArr, dataSize);
            }
        }
    }

    private void restoreFull() {
        try {
            new Thread(new StreamFeederThread(), "unified-stream-feeder").start();
        } catch (IOException unused) {
            Slog.e(BackupManagerService.TAG, "Unable to construct pipes for stream restore!");
            executeNextState(UnifiedRestoreState.RUNNING_QUEUE);
        }
    }

    private void restoreFinished() {
        Slog.d(BackupManagerService.TAG, "restoreFinished packageName=" + this.mCurrentPackage.packageName);
        try {
            this.backupManagerService.prepareOperationTimeout(this.mEphemeralOpToken, this.mAgentTimeoutParameters.getRestoreAgentFinishedTimeoutMillis(), this, 1);
            this.mAgent.doRestoreFinished(this.mEphemeralOpToken, this.backupManagerService.getBackupManagerBinder());
        } catch (Exception e) {
            String str = this.mCurrentPackage.packageName;
            Slog.e(BackupManagerService.TAG, "Unable to finalize restore of " + str);
            EventLog.writeEvent(EventLogTags.RESTORE_AGENT_FAILURE, str, e.toString());
            keyValueAgentErrorCleanup(true);
            executeNextState(UnifiedRestoreState.RUNNING_QUEUE);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class StreamFeederThread extends RestoreEngine implements Runnable, BackupRestoreTask {
        FullRestoreEngine mEngine;
        FullRestoreEngineThread mEngineThread;
        private final int mEphemeralOpToken;
        final String TAG = "StreamFeederThread";
        ParcelFileDescriptor[] mTransportPipes = ParcelFileDescriptor.createPipe();
        ParcelFileDescriptor[] mEnginePipes = ParcelFileDescriptor.createPipe();

        @Override // com.android.server.backup.BackupRestoreTask
        public void execute() {
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void operationComplete(long j) {
        }

        public StreamFeederThread() throws IOException {
            this.mEphemeralOpToken = PerformUnifiedRestoreTask.this.backupManagerService.generateRandomIntegerToken();
            setRunning(true);
        }

        /* JADX WARN: Code restructure failed: missing block: B:35:0x015f, code lost:
        
            if (r11 == (-1000)) goto L33;
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x0164, code lost:
        
            r0 = com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE;
         */
        /* JADX WARN: Code restructure failed: missing block: B:53:0x0288, code lost:
        
            if (r0 != 64536) goto L34;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:77:0x02bc  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x0304  */
        /* JADX WARN: Removed duplicated region for block: B:83:0x02c2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r11v16 */
        /* JADX WARN: Type inference failed for: r11v5 */
        /* JADX WARN: Type inference failed for: r11v6 */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            Throwable th;
            BackupEligibilityRules backupEligibilityRules;
            ?? r11;
            UnifiedRestoreState unifiedRestoreState;
            char c;
            UnifiedRestoreState unifiedRestoreState2;
            int i;
            UnifiedRestoreState unifiedRestoreState3 = UnifiedRestoreState.INITIAL;
            EventLog.writeEvent(EventLogTags.FULL_RESTORE_PACKAGE, PerformUnifiedRestoreTask.this.mCurrentPackage.packageName);
            UserBackupManagerService userBackupManagerService = PerformUnifiedRestoreTask.this.backupManagerService;
            OperationStorage operationStorage = PerformUnifiedRestoreTask.this.mOperationStorage;
            IBackupManagerMonitor iBackupManagerMonitor = PerformUnifiedRestoreTask.this.mMonitor;
            PackageInfo packageInfo = PerformUnifiedRestoreTask.this.mCurrentPackage;
            int i2 = this.mEphemeralOpToken;
            BackupEligibilityRules backupEligibilityRules2 = PerformUnifiedRestoreTask.this.mBackupEligibilityRules;
            FullRestoreEngine fullRestoreEngine = new FullRestoreEngine(userBackupManagerService, operationStorage, this, null, iBackupManagerMonitor, packageInfo, false, i2, false, backupEligibilityRules2);
            this.mEngine = fullRestoreEngine;
            this.mEngineThread = new FullRestoreEngineThread(fullRestoreEngine, this.mEnginePipes[0]);
            ParcelFileDescriptor parcelFileDescriptor = this.mEnginePipes[1];
            ParcelFileDescriptor[] parcelFileDescriptorArr = this.mTransportPipes;
            ParcelFileDescriptor parcelFileDescriptor2 = parcelFileDescriptorArr[0];
            ParcelFileDescriptor parcelFileDescriptor3 = parcelFileDescriptorArr[1];
            int i3 = 32768;
            byte[] bArr = new byte[32768];
            FileOutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
            FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptor2.getFileDescriptor());
            new Thread(this.mEngineThread, "unified-restore-engine").start();
            try {
                try {
                    BackupTransportClient connectOrThrow = PerformUnifiedRestoreTask.this.mTransportConnection.connectOrThrow("PerformUnifiedRestoreTask$StreamFeederThread.run()");
                    i = 0;
                    while (true) {
                        if (i != 0) {
                            break;
                        }
                        try {
                            int nextFullRestoreDataChunk = connectOrThrow.getNextFullRestoreDataChunk(parcelFileDescriptor3);
                            if (nextFullRestoreDataChunk > 0) {
                                if (nextFullRestoreDataChunk > i3) {
                                    bArr = new byte[nextFullRestoreDataChunk];
                                    i3 = nextFullRestoreDataChunk;
                                }
                                while (nextFullRestoreDataChunk > 0) {
                                    int read = fileInputStream.read(bArr, 0, nextFullRestoreDataChunk);
                                    fileOutputStream.write(bArr, 0, read);
                                    nextFullRestoreDataChunk -= read;
                                }
                            } else {
                                if (nextFullRestoreDataChunk == -1) {
                                    i = 0;
                                    break;
                                }
                                Slog.e("StreamFeederThread", "Error " + nextFullRestoreDataChunk + " streaming restore for " + PerformUnifiedRestoreTask.this.mCurrentPackage.packageName);
                                EventLog.writeEvent(EventLogTags.RESTORE_TRANSPORT_FAILURE, new Object[0]);
                                i = nextFullRestoreDataChunk;
                            }
                        } catch (IOException unused) {
                            Slog.e("StreamFeederThread", "Unable to route data for restore");
                            EventLog.writeEvent(EventLogTags.RESTORE_AGENT_FAILURE, PerformUnifiedRestoreTask.this.mCurrentPackage.packageName, "I/O error on pipes");
                            IoUtils.closeQuietly(this.mEnginePipes[1]);
                            IoUtils.closeQuietly(this.mTransportPipes[0]);
                            IoUtils.closeQuietly(this.mTransportPipes[1]);
                            this.mEngineThread.waitForResult();
                            IoUtils.closeQuietly(this.mEnginePipes[0]);
                            PerformUnifiedRestoreTask.this.mDidLaunch = this.mEngine.getAgent() != null;
                            try {
                                PerformUnifiedRestoreTask.this.mTransportConnection.connectOrThrow("PerformUnifiedRestoreTask$StreamFeederThread.run()").abortFullRestore();
                                c = 64533;
                            } catch (Exception e) {
                                Slog.e("StreamFeederThread", "Transport threw from abortFullRestore: " + e.getMessage());
                                c = 64536;
                            }
                            PerformUnifiedRestoreTask.this.backupManagerService.clearApplicationDataAfterRestoreFailure(PerformUnifiedRestoreTask.this.mCurrentPackage.packageName);
                        } catch (Exception e2) {
                            e = e2;
                            Slog.e("StreamFeederThread", "Transport failed during restore: " + e.getMessage());
                            EventLog.writeEvent(EventLogTags.RESTORE_TRANSPORT_FAILURE, new Object[0]);
                            IoUtils.closeQuietly(this.mEnginePipes[1]);
                            IoUtils.closeQuietly(this.mTransportPipes[0]);
                            IoUtils.closeQuietly(this.mTransportPipes[1]);
                            this.mEngineThread.waitForResult();
                            IoUtils.closeQuietly(this.mEnginePipes[0]);
                            PerformUnifiedRestoreTask.this.mDidLaunch = this.mEngine.getAgent() != null;
                            try {
                                PerformUnifiedRestoreTask.this.mTransportConnection.connectOrThrow("PerformUnifiedRestoreTask$StreamFeederThread.run()").abortFullRestore();
                            } catch (Exception e3) {
                                Slog.e("StreamFeederThread", "Transport threw from abortFullRestore: " + e3.getMessage());
                            }
                            PerformUnifiedRestoreTask.this.backupManagerService.clearApplicationDataAfterRestoreFailure(PerformUnifiedRestoreTask.this.mCurrentPackage.packageName);
                            unifiedRestoreState2 = UnifiedRestoreState.FINAL;
                            PerformUnifiedRestoreTask.this.executeNextState(unifiedRestoreState2);
                            setRunning(false);
                            return;
                        }
                    }
                    IoUtils.closeQuietly(this.mEnginePipes[1]);
                    IoUtils.closeQuietly(this.mTransportPipes[0]);
                    IoUtils.closeQuietly(this.mTransportPipes[1]);
                    this.mEngineThread.waitForResult();
                    IoUtils.closeQuietly(this.mEnginePipes[0]);
                    PerformUnifiedRestoreTask.this.mDidLaunch = this.mEngine.getAgent() != null;
                } catch (IOException unused2) {
                } catch (Exception e4) {
                    e = e4;
                } catch (Throwable th2) {
                    th = th2;
                    backupEligibilityRules = null;
                    IoUtils.closeQuietly(this.mEnginePipes[1]);
                    IoUtils.closeQuietly(this.mTransportPipes[0]);
                    IoUtils.closeQuietly(this.mTransportPipes[1]);
                    this.mEngineThread.waitForResult();
                    IoUtils.closeQuietly(this.mEnginePipes[0]);
                    PerformUnifiedRestoreTask.this.mDidLaunch = this.mEngine.getAgent() != null;
                    if (backupEligibilityRules == null) {
                    }
                    PerformUnifiedRestoreTask.this.executeNextState(unifiedRestoreState);
                    setRunning(false);
                    throw th;
                }
                if (i == 0) {
                    unifiedRestoreState2 = UnifiedRestoreState.RESTORE_FINISHED;
                    PerformUnifiedRestoreTask.this.mAgent = this.mEngine.getAgent();
                    PerformUnifiedRestoreTask.this.mWidgetData = this.mEngine.getWidgetData();
                    PerformUnifiedRestoreTask.this.executeNextState(unifiedRestoreState2);
                    setRunning(false);
                    return;
                }
                try {
                    PerformUnifiedRestoreTask.this.mTransportConnection.connectOrThrow("PerformUnifiedRestoreTask$StreamFeederThread.run()").abortFullRestore();
                } catch (Exception e5) {
                    Slog.e("StreamFeederThread", "Transport threw from abortFullRestore: " + e5.getMessage());
                    i = -1000;
                }
                PerformUnifiedRestoreTask.this.backupManagerService.clearApplicationDataAfterRestoreFailure(PerformUnifiedRestoreTask.this.mCurrentPackage.packageName);
            } catch (Throwable th3) {
                th = th3;
                backupEligibilityRules = backupEligibilityRules2;
                IoUtils.closeQuietly(this.mEnginePipes[1]);
                IoUtils.closeQuietly(this.mTransportPipes[0]);
                IoUtils.closeQuietly(this.mTransportPipes[1]);
                this.mEngineThread.waitForResult();
                IoUtils.closeQuietly(this.mEnginePipes[0]);
                PerformUnifiedRestoreTask.this.mDidLaunch = this.mEngine.getAgent() != null;
                if (backupEligibilityRules == null) {
                    try {
                        PerformUnifiedRestoreTask.this.mTransportConnection.connectOrThrow("PerformUnifiedRestoreTask$StreamFeederThread.run()").abortFullRestore();
                        r11 = backupEligibilityRules;
                    } catch (Exception e6) {
                        Slog.e("StreamFeederThread", "Transport threw from abortFullRestore: " + e6.getMessage());
                        r11 = -1000;
                    }
                    PerformUnifiedRestoreTask.this.backupManagerService.clearApplicationDataAfterRestoreFailure(PerformUnifiedRestoreTask.this.mCurrentPackage.packageName);
                    unifiedRestoreState = r11 == -1000 ? UnifiedRestoreState.FINAL : UnifiedRestoreState.RUNNING_QUEUE;
                } else {
                    unifiedRestoreState = UnifiedRestoreState.RESTORE_FINISHED;
                    PerformUnifiedRestoreTask.this.mAgent = this.mEngine.getAgent();
                    PerformUnifiedRestoreTask.this.mWidgetData = this.mEngine.getWidgetData();
                }
                PerformUnifiedRestoreTask.this.executeNextState(unifiedRestoreState);
                setRunning(false);
                throw th;
            }
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void handleCancel(boolean z) {
            PerformUnifiedRestoreTask.this.mOperationStorage.removeOperation(this.mEphemeralOpToken);
            Slog.w("StreamFeederThread", "Full-data restore target timed out; shutting down");
            PerformUnifiedRestoreTask performUnifiedRestoreTask = PerformUnifiedRestoreTask.this;
            performUnifiedRestoreTask.mMonitor = BackupManagerMonitorUtils.monitorEvent(performUnifiedRestoreTask.mMonitor, 45, PerformUnifiedRestoreTask.this.mCurrentPackage, 2, null);
            this.mEngineThread.handleTimeout();
            IoUtils.closeQuietly(this.mEnginePipes[1]);
            ParcelFileDescriptor[] parcelFileDescriptorArr = this.mEnginePipes;
            parcelFileDescriptorArr[1] = null;
            IoUtils.closeQuietly(parcelFileDescriptorArr[0]);
            this.mEnginePipes[0] = null;
        }
    }

    private void finalizeRestore() {
        PackageManagerBackupAgent packageManagerBackupAgent;
        try {
            this.mTransportConnection.connectOrThrow("PerformUnifiedRestoreTask.finalizeRestore()").finishRestore();
        } catch (Exception e) {
            Slog.e(BackupManagerService.TAG, "Error finishing restore", e);
        }
        IRestoreObserver iRestoreObserver = this.mObserver;
        if (iRestoreObserver != null) {
            try {
                iRestoreObserver.restoreFinished(this.mStatus);
            } catch (RemoteException unused) {
                Slog.d(BackupManagerService.TAG, "Restore observer died at restoreFinished");
            }
        }
        this.backupManagerService.getBackupHandler().removeMessages(8);
        if (this.mPmToken > 0) {
            try {
                this.backupManagerService.getPackageManagerBinder().finishPackageInstall(this.mPmToken, this.mDidLaunch);
            } catch (RemoteException unused2) {
            }
        } else {
            this.backupManagerService.getBackupHandler().sendEmptyMessageDelayed(8, this.mAgentTimeoutParameters.getRestoreSessionTimeoutMillis());
        }
        if (this.mIsSystemRestore) {
            AppWidgetBackupBridge.systemRestoreFinished(this.mUserId);
        }
        if (this.mIsSystemRestore && (packageManagerBackupAgent = this.mPmAgent) != null) {
            this.backupManagerService.setAncestralPackages(packageManagerBackupAgent.getRestoredPackages());
            this.backupManagerService.setAncestralToken(this.mToken);
            this.backupManagerService.setAncestralBackupDestination(this.mBackupEligibilityRules.getBackupDestination());
            this.backupManagerService.writeRestoreTokens();
        }
        synchronized (this.backupManagerService.getPendingRestores()) {
            if (this.backupManagerService.getPendingRestores().size() > 0) {
                Slog.d(BackupManagerService.TAG, "Starting next pending restore.");
                this.backupManagerService.getBackupHandler().sendMessage(this.backupManagerService.getBackupHandler().obtainMessage(20, this.backupManagerService.getPendingRestores().remove()));
            } else {
                this.backupManagerService.setRestoreInProgress(false);
            }
        }
        Slog.i(BackupManagerService.TAG, "Restore complete.");
        this.mListener.onFinished("PerformUnifiedRestoreTask.finalizeRestore()");
    }

    void keyValueAgentErrorCleanup(boolean z) {
        if (z) {
            this.backupManagerService.clearApplicationDataAfterRestoreFailure(this.mCurrentPackage.packageName);
        }
        keyValueAgentCleanup();
    }

    void keyValueAgentCleanup() {
        this.mBackupDataName.delete();
        this.mStageName.delete();
        try {
            ParcelFileDescriptor parcelFileDescriptor = this.mBackupData;
            if (parcelFileDescriptor != null) {
                parcelFileDescriptor.close();
            }
        } catch (IOException unused) {
        }
        try {
            ParcelFileDescriptor parcelFileDescriptor2 = this.mNewState;
            if (parcelFileDescriptor2 != null) {
                parcelFileDescriptor2.close();
            }
        } catch (IOException unused2) {
        }
        this.mNewState = null;
        this.mBackupData = null;
        this.mNewStateName.delete();
        if (this.mCurrentPackage.applicationInfo != null) {
            try {
                this.backupManagerService.getActivityManager().unbindBackupAgent(this.mCurrentPackage.applicationInfo);
                ApplicationInfo applicationInfo = this.mCurrentPackage.applicationInfo;
                boolean z = !UserHandle.isCore(applicationInfo.uid) && (this.mRestoreDescription.getDataType() == 2 || (65536 & applicationInfo.flags) != 0);
                if (this.mTargetPackage == null && z) {
                    Slog.d(BackupManagerService.TAG, "Restore complete, killing host process of " + this.mCurrentPackage.applicationInfo.processName);
                    IActivityManager activityManager = this.backupManagerService.getActivityManager();
                    ApplicationInfo applicationInfo2 = this.mCurrentPackage.applicationInfo;
                    activityManager.killApplicationProcess(applicationInfo2.processName, applicationInfo2.uid);
                }
            } catch (RemoteException unused3) {
            }
        }
        this.backupManagerService.getBackupHandler().removeMessages(18, this);
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void operationComplete(long j) {
        UnifiedRestoreState unifiedRestoreState;
        this.mOperationStorage.removeOperation(this.mEphemeralOpToken);
        int i = AnonymousClass1.$SwitchMap$com$android$server$backup$restore$UnifiedRestoreState[this.mState.ordinal()];
        if (i == 1) {
            unifiedRestoreState = UnifiedRestoreState.RUNNING_QUEUE;
        } else if (i == 3 || i == 4) {
            unifiedRestoreState = UnifiedRestoreState.RESTORE_FINISHED;
        } else if (i == 5) {
            EventLog.writeEvent(EventLogTags.RESTORE_PACKAGE, this.mCurrentPackage.packageName, Integer.valueOf((int) this.mBackupDataName.length()));
            BackupManagerMonitorUtils.monitorAgentLoggingResults(this.mMonitor, this.mCurrentPackage, this.mAgent);
            keyValueAgentCleanup();
            byte[] bArr = this.mWidgetData;
            if (bArr != null) {
                this.backupManagerService.restoreWidgetData(this.mCurrentPackage.packageName, bArr);
            }
            unifiedRestoreState = UnifiedRestoreState.RUNNING_QUEUE;
        } else {
            Slog.e(BackupManagerService.TAG, "Unexpected restore callback into state " + this.mState);
            keyValueAgentErrorCleanup(true);
            unifiedRestoreState = UnifiedRestoreState.FINAL;
        }
        executeNextState(unifiedRestoreState);
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void handleCancel(boolean z) {
        this.mOperationStorage.removeOperation(this.mEphemeralOpToken);
        Slog.e(BackupManagerService.TAG, "Timeout restoring application " + this.mCurrentPackage.packageName);
        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 31, this.mCurrentPackage, 2, null);
        EventLog.writeEvent(EventLogTags.RESTORE_AGENT_FAILURE, this.mCurrentPackage.packageName, "restore timeout");
        keyValueAgentErrorCleanup(true);
        executeNextState(UnifiedRestoreState.RUNNING_QUEUE);
    }

    @VisibleForTesting
    void executeNextState(UnifiedRestoreState unifiedRestoreState) {
        this.mState = unifiedRestoreState;
        this.backupManagerService.getBackupHandler().sendMessage(this.backupManagerService.getBackupHandler().obtainMessage(20, this));
    }

    @VisibleForTesting
    UnifiedRestoreState getCurrentUnifiedRestoreStateForTesting() {
        return this.mState;
    }

    @VisibleForTesting
    void setCurrentUnifiedRestoreStateForTesting(UnifiedRestoreState unifiedRestoreState) {
        this.mState = unifiedRestoreState;
    }

    @VisibleForTesting
    void setStateDirForTesting(File file) {
        this.mStateDir = file;
    }

    @VisibleForTesting
    void initiateOneRestoreForTesting(PackageInfo packageInfo, long j) {
        initiateOneRestore(packageInfo, j);
    }

    void sendStartRestore(int i) {
        IRestoreObserver iRestoreObserver = this.mObserver;
        if (iRestoreObserver != null) {
            try {
                iRestoreObserver.restoreStarting(i);
            } catch (RemoteException unused) {
                Slog.w(BackupManagerService.TAG, "Restore observer went away: startRestore");
                this.mObserver = null;
            }
        }
    }

    void sendOnRestorePackage(String str) {
        IRestoreObserver iRestoreObserver = this.mObserver;
        if (iRestoreObserver != null) {
            try {
                iRestoreObserver.onUpdate(this.mCount, str);
            } catch (RemoteException unused) {
                Slog.d(BackupManagerService.TAG, "Restore observer died in onUpdate");
                this.mObserver = null;
            }
        }
    }

    void sendEndRestore() {
        IRestoreObserver iRestoreObserver = this.mObserver;
        if (iRestoreObserver != null) {
            try {
                iRestoreObserver.restoreFinished(this.mStatus);
            } catch (RemoteException unused) {
                Slog.w(BackupManagerService.TAG, "Restore observer went away: endRestore");
                this.mObserver = null;
            }
        }
    }
}
