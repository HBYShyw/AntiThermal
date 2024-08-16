package com.android.server.backup.restore;

import android.util.Slog;
import com.android.server.backup.BackupAgentTimeoutParameters;
import com.android.server.backup.BackupRestoreTask;
import com.android.server.backup.OperationStorage;
import com.android.server.backup.UserBackupManagerService;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AdbRestoreFinishedLatch implements BackupRestoreTask {
    private static final String TAG = "AdbRestoreFinishedLatch";
    private UserBackupManagerService backupManagerService;
    private final BackupAgentTimeoutParameters mAgentTimeoutParameters;
    private final int mCurrentOpToken;
    final CountDownLatch mLatch = new CountDownLatch(1);
    private final OperationStorage mOperationStorage;

    @Override // com.android.server.backup.BackupRestoreTask
    public void execute() {
    }

    public AdbRestoreFinishedLatch(UserBackupManagerService userBackupManagerService, OperationStorage operationStorage, int i) {
        this.backupManagerService = userBackupManagerService;
        this.mOperationStorage = operationStorage;
        this.mCurrentOpToken = i;
        BackupAgentTimeoutParameters agentTimeoutParameters = userBackupManagerService.getAgentTimeoutParameters();
        Objects.requireNonNull(agentTimeoutParameters, "Timeout parameters cannot be null");
        this.mAgentTimeoutParameters = agentTimeoutParameters;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void await() {
        try {
            this.mLatch.await(this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException unused) {
            Slog.w(TAG, "Interrupted!");
        }
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void operationComplete(long j) {
        this.mLatch.countDown();
        this.mOperationStorage.removeOperation(this.mCurrentOpToken);
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void handleCancel(boolean z) {
        Slog.w(TAG, "adb onRestoreFinished() timed out");
        this.mLatch.countDown();
        this.mOperationStorage.removeOperation(this.mCurrentOpToken);
    }
}
