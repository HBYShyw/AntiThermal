package com.android.server.backup;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface BackupRestoreTask {
    void execute();

    void handleCancel(boolean z);

    void operationComplete(long j);
}
