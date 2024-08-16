package com.android.server.am;

import android.content.pm.ApplicationInfo;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class BackupRecord {
    public static final int BACKUP_FULL = 1;
    public static final int BACKUP_NORMAL = 0;
    public static final int RESTORE = 2;
    public static final int RESTORE_FULL = 3;
    ProcessRecord app;
    final ApplicationInfo appInfo;
    final int backupDestination;
    final int backupMode;
    String stringName;
    final int userId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BackupRecord(ApplicationInfo applicationInfo, int i, int i2, int i3) {
        this.appInfo = applicationInfo;
        this.backupMode = i;
        this.userId = i2;
        this.backupDestination = i3;
    }

    public String toString() {
        String str = this.stringName;
        if (str != null) {
            return str;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append("BackupRecord{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(' ');
        sb.append(this.appInfo.packageName);
        sb.append(' ');
        sb.append(this.appInfo.name);
        sb.append(' ');
        sb.append(this.appInfo.backupAgentName);
        sb.append('}');
        String sb2 = sb.toString();
        this.stringName = sb2;
        return sb2;
    }
}
