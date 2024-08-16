package com.android.server.backup.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.ArraySet;
import android.util.Slog;
import com.android.server.backup.BackupManagerService;
import com.android.server.backup.UserBackupManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class RunInitializeReceiver extends BroadcastReceiver {
    private final UserBackupManagerService mUserBackupManagerService;

    public RunInitializeReceiver(UserBackupManagerService userBackupManagerService) {
        this.mUserBackupManagerService = userBackupManagerService;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (UserBackupManagerService.RUN_INITIALIZE_ACTION.equals(intent.getAction())) {
            synchronized (this.mUserBackupManagerService.getQueueLock()) {
                ArraySet<String> pendingInits = this.mUserBackupManagerService.getPendingInits();
                Slog.v(BackupManagerService.TAG, "Running a device init; " + pendingInits.size() + " pending");
                if (pendingInits.size() > 0) {
                    String[] strArr = (String[]) pendingInits.toArray(new String[pendingInits.size()]);
                    this.mUserBackupManagerService.clearPendingInits();
                    this.mUserBackupManagerService.initializeTransports(strArr, null);
                }
            }
        }
    }
}
