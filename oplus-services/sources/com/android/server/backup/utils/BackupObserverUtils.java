package com.android.server.backup.utils;

import android.app.backup.BackupProgress;
import android.app.backup.IBackupObserver;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.backup.BackupManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BackupObserverUtils {
    public static void sendBackupOnUpdate(IBackupObserver iBackupObserver, String str, BackupProgress backupProgress) {
        if (iBackupObserver != null) {
            try {
                iBackupObserver.onUpdate(str, backupProgress);
            } catch (RemoteException unused) {
                Slog.w(BackupManagerService.TAG, "Backup observer went away: onUpdate");
            }
        }
    }

    public static void sendBackupOnPackageResult(IBackupObserver iBackupObserver, String str, int i) {
        if (iBackupObserver != null) {
            try {
                iBackupObserver.onResult(str, i);
            } catch (RemoteException unused) {
                Slog.w(BackupManagerService.TAG, "Backup observer went away: onResult");
            }
        }
    }

    public static void sendBackupFinished(IBackupObserver iBackupObserver, int i) {
        if (iBackupObserver != null) {
            try {
                iBackupObserver.backupFinished(i);
            } catch (RemoteException unused) {
                Slog.w(BackupManagerService.TAG, "Backup observer went away: backupFinished");
            }
        }
    }
}
