package com.android.server.backup;

import android.util.Slog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class UserBackupManagerFilePersistedSettings {
    private static final String BACKUP_ENABLE_FILE = "backup_enabled";

    UserBackupManagerFilePersistedSettings() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean readBackupEnableState(int i) {
        boolean readBackupEnableState = readBackupEnableState(UserBackupManagerFiles.getBaseStateDir(i));
        Slog.d(BackupManagerService.TAG, "user:" + i + " readBackupEnableState enabled:" + readBackupEnableState);
        return readBackupEnableState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void writeBackupEnableState(int i, boolean z) {
        Slog.d(BackupManagerService.TAG, "user:" + i + " writeBackupEnableState enable:" + z);
        writeBackupEnableState(UserBackupManagerFiles.getBaseStateDir(i), z);
    }

    private static boolean readBackupEnableState(File file) {
        File file2 = new File(file, BACKUP_ENABLE_FILE);
        if (file2.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file2);
                try {
                    int read = fileInputStream.read();
                    if (read != 0 && read != 1) {
                        Slog.e(BackupManagerService.TAG, "Unexpected enabled state:" + read);
                    }
                    boolean z = read != 0;
                    fileInputStream.close();
                    return z;
                } finally {
                }
            } catch (IOException unused) {
                Slog.e(BackupManagerService.TAG, "Cannot read enable state; assuming disabled");
            }
        } else {
            Slog.i(BackupManagerService.TAG, "isBackupEnabled() => false due to absent settings file");
        }
        return false;
    }

    private static void writeBackupEnableState(File file, boolean z) {
        File file2 = new File(file, BACKUP_ENABLE_FILE);
        File file3 = new File(file, "backup_enabled-stage");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file3);
            try {
                fileOutputStream.write(z ? 1 : 0);
                fileOutputStream.close();
                if (!file3.renameTo(file2)) {
                    Slog.e(BackupManagerService.TAG, "Write enable failed as could not rename staging file to actual");
                }
                fileOutputStream.close();
            } catch (Throwable th) {
                try {
                    fileOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException | RuntimeException e) {
            Slog.e(BackupManagerService.TAG, "Unable to record backup enable state; reverting to disabled: " + e.getMessage());
            file2.delete();
            file3.delete();
        }
    }
}
