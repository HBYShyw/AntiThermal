package com.android.server.backup.keyvalue;

import android.app.backup.IBackupManagerMonitor;
import android.app.backup.IBackupObserver;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.EventLogTags;
import com.android.server.backup.DataChangedJournal;
import com.android.server.backup.UserBackupManagerService;
import com.android.server.backup.remote.RemoteResult;
import com.android.server.backup.utils.BackupManagerMonitorUtils;
import com.android.server.backup.utils.BackupObserverUtils;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@VisibleForTesting
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class KeyValueBackupReporter {
    private static final boolean DEBUG = true;

    @VisibleForTesting
    static final boolean MORE_DEBUG = false;

    @VisibleForTesting
    static final String TAG = "KeyValueBackupTask";
    private final UserBackupManagerService mBackupManagerService;
    private IBackupManagerMonitor mMonitor;
    private final IBackupObserver mObserver;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAgentFilesReady(File file) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCancel() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onRemoteCallReturned(RemoteResult remoteResult, String str) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onRevertTask() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTransportPerformBackup(String str) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onWriteWidgetData(boolean z, byte[] bArr) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void onNewThread(String str) {
        Slog.d(TAG, "Spinning thread " + str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KeyValueBackupReporter(UserBackupManagerService userBackupManagerService, IBackupObserver iBackupObserver, IBackupManagerMonitor iBackupManagerMonitor) {
        this.mBackupManagerService = userBackupManagerService;
        this.mObserver = iBackupObserver;
        this.mMonitor = iBackupManagerMonitor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBackupManagerMonitor getMonitor() {
        return this.mMonitor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBackupObserver getObserver() {
        return this.mObserver;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSkipBackup() {
        Slog.d(TAG, "Skipping backup since one is already in progress");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onEmptyQueueAtStart() {
        Slog.w(TAG, "Backup begun with an empty queue, nothing to do");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onQueueReady(List<String> list) {
        Slog.v(TAG, "Beginning backup of " + list.size() + " targets");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTransportReady(String str) {
        EventLog.writeEvent(EventLogTags.BACKUP_START, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onInitializeTransport(String str) {
        Slog.i(TAG, "Initializing transport and resetting backup state");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTransportInitialized(int i) {
        if (i == 0) {
            EventLog.writeEvent(EventLogTags.BACKUP_INITIALIZE, new Object[0]);
        } else {
            EventLog.writeEvent(EventLogTags.BACKUP_TRANSPORT_FAILURE, "(initialize)");
            Slog.e(TAG, "Transport error in initializeDevice()");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onInitializeTransportError(Exception exc) {
        Slog.e(TAG, "Error during initialization", exc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSkipPm() {
        Slog.d(TAG, "Skipping backup of PM metadata");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onExtractPmAgentDataError(Exception exc) {
        Slog.e(TAG, "Error during PM metadata backup", exc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onStartPackageBackup(String str) {
        Slog.d(TAG, "Starting key-value backup of " + str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageNotEligibleForBackup(String str) {
        Slog.i(TAG, "Package " + str + " no longer supports backup, skipping");
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str, -2001);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageEligibleForFullBackup(String str) {
        Slog.i(TAG, "Package " + str + " performs full-backup rather than key-value, skipping");
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str, -2001);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageStopped(String str) {
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str, -2001);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAgentUnknown(String str) {
        Slog.d(TAG, "Package does not exist, skipping");
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str, -2002);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBindAgentError(String str, SecurityException securityException) {
        Slog.d(TAG, "Error in bind/backup", securityException);
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str, -1003);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAgentError(String str) {
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str, -1003);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onExtractAgentData(String str) {
        Slog.d(TAG, "Invoking agent on " + str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onRestoreconFailed(File file) {
        Slog.e(TAG, "SELinux restorecon failed on " + file);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCallAgentDoBackupError(String str, boolean z, Exception exc) {
        if (z) {
            Slog.e(TAG, "Error invoking agent on " + str + ": " + exc);
            BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str, -1003);
        } else {
            Slog.e(TAG, "Error before invoking agent on " + str + ": " + exc);
        }
        EventLog.writeEvent(EventLogTags.BACKUP_AGENT_FAILURE, str, exc.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onFailAgentError(String str) {
        Slog.w(TAG, "Error conveying failure to " + str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAgentIllegalKey(PackageInfo packageInfo, String str) {
        String str2 = packageInfo.packageName;
        EventLog.writeEvent(EventLogTags.BACKUP_AGENT_FAILURE, str2, "bad key");
        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 5, packageInfo, 3, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_ILLEGAL_KEY", str));
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str2, -1003);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAgentDataError(String str, IOException iOException) {
        Slog.w(TAG, "Unable to read/write agent data for " + str + ": " + iOException);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDigestError(NoSuchAlgorithmException noSuchAlgorithmException) {
        Slog.e(TAG, "Unable to use SHA-1!");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onEmptyData(PackageInfo packageInfo) {
        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 7, packageInfo, 3, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageBackupComplete(String str, long j) {
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str, 0);
        EventLog.writeEvent(EventLogTags.BACKUP_PACKAGE, str, Long.valueOf(j));
        this.mBackupManagerService.logBackupComplete(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageBackupRejected(String str) {
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str, -1002);
        EventLogTags.writeBackupAgentFailure(str, "Transport rejected");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageBackupQuotaExceeded(String str) {
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str, -1005);
        EventLog.writeEvent(EventLogTags.BACKUP_QUOTA_EXCEEDED, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAgentDoQuotaExceededError(Exception exc) {
        Slog.e(TAG, "Unable to notify about quota exceeded: " + exc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageBackupNonIncrementalRequired(PackageInfo packageInfo) {
        Slog.i(TAG, "Transport lost data, retrying package");
        BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 51, packageInfo, 1, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageBackupNonIncrementalAndNonIncrementalRequired(String str) {
        Slog.e(TAG, "Transport requested non-incremental but already the case");
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str, -1000);
        EventLog.writeEvent(EventLogTags.BACKUP_TRANSPORT_FAILURE, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageBackupTransportFailure(String str) {
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str, -1000);
        EventLog.writeEvent(EventLogTags.BACKUP_TRANSPORT_FAILURE, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageBackupTransportError(String str, Exception exc) {
        Slog.e(TAG, "Transport error backing up " + str, exc);
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, str, -1000);
        EventLog.writeEvent(EventLogTags.BACKUP_TRANSPORT_FAILURE, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCloseFileDescriptorError(String str) {
        Slog.w(TAG, "Error closing " + str + " file-descriptor");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAgentTimedOut(PackageInfo packageInfo) {
        String packageName = getPackageName(packageInfo);
        Slog.i(TAG, "Agent " + packageName + " timed out");
        EventLog.writeEvent(EventLogTags.BACKUP_AGENT_FAILURE, packageName);
        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 21, packageInfo, 2, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_CANCEL_ALL", false));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAgentCancelled(PackageInfo packageInfo) {
        String packageName = getPackageName(packageInfo);
        Slog.i(TAG, "Cancel backing up " + packageName);
        EventLog.writeEvent(EventLogTags.BACKUP_AGENT_FAILURE, packageName);
        this.mMonitor = BackupManagerMonitorUtils.monitorEvent(this.mMonitor, 21, packageInfo, 2, BackupManagerMonitorUtils.putMonitoringExtra((Bundle) null, "android.app.backup.extra.LOG_CANCEL_ALL", true));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAgentResultError(PackageInfo packageInfo) {
        String packageName = getPackageName(packageInfo);
        BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -1003);
        EventLog.writeEvent(EventLogTags.BACKUP_AGENT_FAILURE, packageName, "result error");
        Slog.w(TAG, "Agent " + packageName + " error in onBackup()");
    }

    private String getPackageName(PackageInfo packageInfo) {
        return packageInfo != null ? packageInfo.packageName : "no_package_yet";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTransportRequestBackupTimeError(Exception exc) {
        Slog.w(TAG, "Unable to contact transport for recommended backoff: " + exc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onJournalDeleteFailed(DataChangedJournal dataChangedJournal) {
        Slog.e(TAG, "Unable to remove backup journal file " + dataChangedJournal);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSetCurrentTokenError(Exception exc) {
        Slog.e(TAG, "Transport threw reporting restore set: " + exc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTransportNotInitialized(String str) {
        EventLog.writeEvent(EventLogTags.BACKUP_RESET, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPendingInitializeTransportError(Exception exc) {
        Slog.w(TAG, "Failed to query transport name for pending init: " + exc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBackupFinished(int i) {
        BackupObserverUtils.sendBackupFinished(this.mObserver, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onStartFullBackup(List<String> list) {
        Slog.d(TAG, "Starting full backups for: " + list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTaskFinished() {
        Slog.i(TAG, "K/V backup pass finished");
    }
}
