package com.android.server.backup.utils;

import android.app.backup.IFullBackupRestoreObserver;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.backup.BackupManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FullBackupRestoreObserverUtils {
    public static IFullBackupRestoreObserver sendStartRestore(IFullBackupRestoreObserver iFullBackupRestoreObserver) {
        if (iFullBackupRestoreObserver == null) {
            return iFullBackupRestoreObserver;
        }
        try {
            iFullBackupRestoreObserver.onStartRestore();
            return iFullBackupRestoreObserver;
        } catch (RemoteException unused) {
            Slog.w(BackupManagerService.TAG, "full restore observer went away: startRestore");
            return null;
        }
    }

    public static IFullBackupRestoreObserver sendOnRestorePackage(IFullBackupRestoreObserver iFullBackupRestoreObserver, String str) {
        if (iFullBackupRestoreObserver == null) {
            return iFullBackupRestoreObserver;
        }
        try {
            iFullBackupRestoreObserver.onRestorePackage(str);
            return iFullBackupRestoreObserver;
        } catch (RemoteException unused) {
            Slog.w(BackupManagerService.TAG, "full restore observer went away: restorePackage");
            return null;
        }
    }

    public static IFullBackupRestoreObserver sendEndRestore(IFullBackupRestoreObserver iFullBackupRestoreObserver) {
        if (iFullBackupRestoreObserver == null) {
            return iFullBackupRestoreObserver;
        }
        try {
            iFullBackupRestoreObserver.onEndRestore();
            return iFullBackupRestoreObserver;
        } catch (RemoteException unused) {
            Slog.w(BackupManagerService.TAG, "full restore observer went away: endRestore");
            return null;
        }
    }
}
