package com.android.server.backup.internal;

import com.android.server.backup.BackupRestoreTask;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Operation {
    public final BackupRestoreTask callback;
    public int state;
    public final int type;

    public Operation(int i, BackupRestoreTask backupRestoreTask, int i2) {
        this.state = i;
        this.callback = backupRestoreTask;
        this.type = i2;
    }
}
