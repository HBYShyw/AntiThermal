package com.oplus.wrapper.os;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

/* loaded from: classes.dex */
public class FileUtils {
    public static int setPermissions(File path, int mode, int uid, int gid) {
        return android.os.FileUtils.setPermissions(path, mode, uid, gid);
    }

    public static int setPermissions(String path, int mode, int uid, int gid) {
        return android.os.FileUtils.setPermissions(path, mode, uid, gid);
    }

    public static int setPermissions(FileDescriptor fd, int mode, int uid, int gid) {
        return android.os.FileUtils.setPermissions(fd, mode, uid, gid);
    }

    public static boolean copyFile(File srcFile, File destFile) {
        return android.os.FileUtils.copyFile(srcFile, destFile);
    }

    public static String readTextFile(File file, int max, String ellipsis) throws IOException {
        return android.os.FileUtils.readTextFile(file, max, ellipsis);
    }

    public static void copyFileOrThrow(File srcFile, File destFile) throws IOException {
        android.os.FileUtils.copyFileOrThrow(srcFile, destFile);
    }

    public static File[] listFilesOrEmpty(File dir) {
        return android.os.FileUtils.listFilesOrEmpty(dir);
    }
}
