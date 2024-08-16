package com.oplus.oms.split.common;

import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;

/* loaded from: classes.dex */
public class FileUtil {
    private static final int BUFFER_SIZE = 8192;
    private static final int CAPACITY = 32;
    private static final String MD5 = "MD5";
    private static final String TAG = "Split.FileUtil";

    private FileUtil() {
    }

    public static boolean copyFileRetry(File from, File tmp, File target) {
        if (fileNotExists(from) || fileNotExists(tmp) || fileNotExists(target)) {
            return false;
        }
        int numAttempts = 0;
        boolean isSuccess = false;
        while (numAttempts < 3 && !isSuccess) {
            numAttempts++;
            try {
                copyFile(from, tmp);
                if (!tmp.renameTo(target)) {
                    SplitLog.w(TAG, "Failed to rename " + tmp.getName() + " to " + target.getName(), new Object[0]);
                } else {
                    isSuccess = true;
                    SplitLog.d(TAG, "copyFileRetry success " + target.getAbsolutePath(), new Object[0]);
                }
            } catch (IOException e) {
                SplitLog.w(TAG, "Failed to copy:" + from + ", attempts times : " + numAttempts, new Object[0]);
            }
        }
        deleteFileSafely(tmp);
        return isSuccess;
    }

    public static void copyFile(InputStream input, OutputStream output) throws IOException {
        BufferedInputStream bufferedInput = new BufferedInputStream(input);
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
        try {
            byte[] buffer = new byte[8192];
            for (int length = bufferedInput.read(buffer); length != -1; length = bufferedInput.read(buffer)) {
                bufferedOutput.write(buffer, 0, length);
            }
            bufferedOutput.flush();
        } finally {
            closeQuietly(output);
            closeQuietly(input);
        }
    }

    public static boolean deleteFileSafely(File file) {
        if (fileNotExists(file)) {
            return true;
        }
        boolean isDeleteSuccessful = false;
        int numAttempts = 0;
        while (numAttempts < 3 && !isDeleteSuccessful) {
            numAttempts++;
            try {
                Files.delete(file.toPath());
                isDeleteSuccessful = true;
            } catch (IOException e) {
                isDeleteSuccessful = false;
                SplitLog.w(TAG, "deleteFileSafely error " + e.getMessage(), new Object[0]);
            }
        }
        SplitLog.d(TAG, isDeleteSuccessful + " to delete file: " + file.getAbsolutePath(), new Object[0]);
        return isDeleteSuccessful;
    }

    public static void copyFile(File source, File dest) throws IOException {
        if (fileNotExists(source) || fileNotExists(dest)) {
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(source);
        try {
            copyFile(fileInputStream, new FileOutputStream(dest));
            fileInputStream.close();
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static String getMD5(File file) {
        if (fileNotExists(file)) {
            return null;
        }
        try {
            FileInputStream fin = new FileInputStream(file);
            try {
                String md5 = getMD5(fin);
                fin.close();
                return md5;
            } finally {
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean fileNotExists(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        return false;
    }

    public static String getMD5(InputStream is) {
        if (is == null) {
            return null;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(is);
            MessageDigest md = MessageDigest.getInstance(MD5);
            StringBuilder md5Str = new StringBuilder(32);
            byte[] buf = new byte[8192];
            while (true) {
                int readCount = bis.read(buf);
                if (readCount == -1) {
                    break;
                }
                md.update(buf, 0, readCount);
            }
            byte[] hashValue = md.digest();
            for (byte b : hashValue) {
                md5Str.append(Integer.toString((b & 255) + 256, 16).substring(1));
            }
            return md5Str.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static void closeQuietly(Object obj) {
        if (obj == null) {
            SplitLog.e(TAG, "Closeable obj null", new Object[0]);
        } else {
            if (obj instanceof Closeable) {
                try {
                    ((Closeable) obj).close();
                    return;
                } catch (IOException e) {
                    SplitLog.e(TAG, "Closeable error", new Object[0]);
                    return;
                }
            }
            throw new IllegalArgumentException("obj: " + obj + " cannot be closed.");
        }
    }

    public static boolean isLegalFile(File file) {
        return file != null && file.exists() && file.canRead() && file.isFile() && file.length() > 0;
    }

    public static boolean deleteDir(File file) {
        return deleteDir(file, true);
    }

    public static boolean deleteOthersDirs(File current, String abi) {
        if (fileNotExists(current)) {
            SplitLog.d(TAG, "deleteOthersDirs current null", new Object[0]);
            return false;
        }
        File parent = current.getParentFile();
        if (fileNotExists(parent)) {
            SplitLog.d(TAG, "deleteOthersDirs parent null", new Object[0]);
            return false;
        }
        File[] files = parent.listFiles();
        if (files == null || files.length == 0) {
            SplitLog.d(TAG, "deleteOthersDirs files null", new Object[0]);
            return false;
        }
        for (File subFile : files) {
            if (subFile.isDirectory() && !subFile.getName().equals(current.getName())) {
                if (!TextUtils.isEmpty(abi)) {
                    File splitLibDir = new File(subFile, abi);
                    if (splitLibDir.exists()) {
                        splitLibDir.setWritable(true);
                        splitLibDir.setExecutable(true);
                    }
                }
                deleteDir(subFile);
            }
        }
        return true;
    }

    public static boolean deleteDir(File file, boolean deleteRootDir) {
        File[] files;
        if (fileNotExists(file)) {
            return false;
        }
        if (file.isFile()) {
            deleteFileSafely(file);
            return true;
        }
        if (file.isDirectory() && (files = file.listFiles()) != null) {
            for (File subFile : files) {
                deleteDir(subFile);
            }
            if (deleteRootDir) {
                deleteFileSafely(file);
                return true;
            }
            return true;
        }
        return true;
    }
}
