package com.android.server.backup;

import android.os.Environment;
import java.io.File;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class UserBackupManagerFiles {
    private static final String BACKUP_PERSISTENT_DIR = "backup";
    private static final String BACKUP_STAGING_DIR = "backup_stage";

    UserBackupManagerFiles() {
    }

    private static File getBaseDir(int i) {
        return Environment.getDataSystemCeDirectory(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static File getBaseStateDir(int i) {
        if (i != 0) {
            return new File(getBaseDir(i), "backup");
        }
        return new File(Environment.getDataDirectory(), "backup");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static File getDataDir(int i) {
        if (i != 0) {
            return new File(getBaseDir(i), BACKUP_STAGING_DIR);
        }
        return new File(Environment.getDownloadCacheDirectory(), BACKUP_STAGING_DIR);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static File getStateDirInSystemDir(int i) {
        return new File(getBaseStateDir(0), "" + i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static File getStateFileInSystemDir(String str, int i) {
        return new File(getStateDirInSystemDir(i), str);
    }
}
