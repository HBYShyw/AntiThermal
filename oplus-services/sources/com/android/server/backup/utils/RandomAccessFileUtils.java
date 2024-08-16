package com.android.server.backup.utils;

import android.util.Slog;
import com.android.server.backup.BackupManagerService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class RandomAccessFileUtils {
    private static RandomAccessFile getRandomAccessFile(File file) throws FileNotFoundException {
        return new RandomAccessFile(file, "rwd");
    }

    public static void writeBoolean(File file, boolean z) {
        try {
            RandomAccessFile randomAccessFile = getRandomAccessFile(file);
            try {
                randomAccessFile.writeBoolean(z);
                randomAccessFile.close();
            } finally {
            }
        } catch (IOException e) {
            Slog.w(BackupManagerService.TAG, "Error writing file:" + file.getAbsolutePath(), e);
        }
    }

    public static boolean readBoolean(File file, boolean z) {
        try {
            RandomAccessFile randomAccessFile = getRandomAccessFile(file);
            try {
                boolean readBoolean = randomAccessFile.readBoolean();
                randomAccessFile.close();
                return readBoolean;
            } finally {
            }
        } catch (IOException e) {
            Slog.w(BackupManagerService.TAG, "Error reading file:" + file.getAbsolutePath(), e);
            return z;
        }
    }
}
