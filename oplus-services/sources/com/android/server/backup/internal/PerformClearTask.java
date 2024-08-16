package com.android.server.backup.internal;

import android.content.pm.PackageInfo;
import android.util.Slog;
import com.android.server.backup.BackupManagerService;
import com.android.server.backup.TransportManager;
import com.android.server.backup.UserBackupManagerService;
import com.android.server.backup.transport.BackupTransportClient;
import com.android.server.backup.transport.TransportConnection;
import java.io.File;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class PerformClearTask implements Runnable {
    private final UserBackupManagerService mBackupManagerService;
    private final OnTaskFinishedListener mListener;
    private final PackageInfo mPackage;
    private final TransportConnection mTransportConnection;
    private final TransportManager mTransportManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PerformClearTask(UserBackupManagerService userBackupManagerService, TransportConnection transportConnection, PackageInfo packageInfo, OnTaskFinishedListener onTaskFinishedListener) {
        this.mBackupManagerService = userBackupManagerService;
        this.mTransportManager = userBackupManagerService.getTransportManager();
        this.mTransportConnection = transportConnection;
        this.mPackage = packageInfo;
        this.mListener = onTaskFinishedListener;
    }

    @Override // java.lang.Runnable
    public void run() {
        StringBuilder sb;
        BackupTransportClient backupTransportClient = null;
        try {
            try {
                new File(new File(this.mBackupManagerService.getBaseStateDir(), this.mTransportManager.getTransportDirName(this.mTransportConnection.getTransportComponent())), this.mPackage.packageName).delete();
                backupTransportClient = this.mTransportConnection.connectOrThrow("PerformClearTask.run()");
                backupTransportClient.clearBackupData(this.mPackage);
            } catch (Exception e) {
                Slog.e(BackupManagerService.TAG, "Transport threw clearing data for " + this.mPackage + ": " + e.getMessage());
                if (backupTransportClient != null) {
                    try {
                        backupTransportClient.finishBackup();
                    } catch (Exception e2) {
                        e = e2;
                        sb = new StringBuilder();
                        sb.append("Unable to mark clear operation finished: ");
                        sb.append(e.getMessage());
                        Slog.e(BackupManagerService.TAG, sb.toString());
                        this.mListener.onFinished("PerformClearTask.run()");
                        this.mBackupManagerService.getWakelock().release();
                    }
                }
            }
            try {
                backupTransportClient.finishBackup();
            } catch (Exception e3) {
                e = e3;
                sb = new StringBuilder();
                sb.append("Unable to mark clear operation finished: ");
                sb.append(e.getMessage());
                Slog.e(BackupManagerService.TAG, sb.toString());
                this.mListener.onFinished("PerformClearTask.run()");
                this.mBackupManagerService.getWakelock().release();
            }
            this.mListener.onFinished("PerformClearTask.run()");
            this.mBackupManagerService.getWakelock().release();
        } catch (Throwable th) {
            if (backupTransportClient != null) {
                try {
                    backupTransportClient.finishBackup();
                } catch (Exception e4) {
                    Slog.e(BackupManagerService.TAG, "Unable to mark clear operation finished: " + e4.getMessage());
                }
            }
            this.mListener.onFinished("PerformClearTask.run()");
            this.mBackupManagerService.getWakelock().release();
            throw th;
        }
    }
}
