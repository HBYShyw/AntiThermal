package com.android.server.backup.fullbackup;

import android.app.backup.IFullBackupRestoreObserver;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.backup.BackupManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class FullBackupTask implements Runnable {
    IFullBackupRestoreObserver mObserver;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FullBackupTask(IFullBackupRestoreObserver iFullBackupRestoreObserver) {
        this.mObserver = iFullBackupRestoreObserver;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void sendStartBackup() {
        IFullBackupRestoreObserver iFullBackupRestoreObserver = this.mObserver;
        if (iFullBackupRestoreObserver != null) {
            try {
                iFullBackupRestoreObserver.onStartBackup();
            } catch (RemoteException unused) {
                Slog.w(BackupManagerService.TAG, "full backup observer went away: startBackup");
                this.mObserver = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void sendOnBackupPackage(String str) {
        IFullBackupRestoreObserver iFullBackupRestoreObserver = this.mObserver;
        if (iFullBackupRestoreObserver != null) {
            try {
                iFullBackupRestoreObserver.onBackupPackage(str);
            } catch (RemoteException unused) {
                Slog.w(BackupManagerService.TAG, "full backup observer went away: backupPackage");
                this.mObserver = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void sendEndBackup() {
        IFullBackupRestoreObserver iFullBackupRestoreObserver = this.mObserver;
        if (iFullBackupRestoreObserver != null) {
            try {
                iFullBackupRestoreObserver.onEndBackup();
            } catch (RemoteException unused) {
                Slog.w(BackupManagerService.TAG, "full backup observer went away: endBackup");
                this.mObserver = null;
            }
        }
    }
}
