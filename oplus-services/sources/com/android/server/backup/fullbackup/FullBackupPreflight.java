package com.android.server.backup.fullbackup;

import android.app.IBackupAgent;
import android.content.pm.PackageInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface FullBackupPreflight {
    long getExpectedSizeOrErrorCode();

    int preflightFullBackup(PackageInfo packageInfo, IBackupAgent iBackupAgent);
}
