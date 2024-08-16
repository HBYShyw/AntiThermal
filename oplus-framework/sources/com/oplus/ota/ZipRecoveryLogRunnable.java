package com.oplus.ota;

import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Slog;
import com.oplus.oms.split.common.SplitConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/* loaded from: classes.dex */
public class ZipRecoveryLogRunnable implements Runnable {
    private static final String OPLUS_REGION_MARK = "ro.vendor.oplus.regionmark";
    private static final String OTA_VERSION = "ro.build.version.ota";
    private static final String RECOVERY_LOG_DCS_DIR = "/data/persist_log/DCS/de/recovery_log";
    private static final String REGION_CN = "CN";
    private static final String TAG = "ZipRecoveryLogRunnable";

    @Override // java.lang.Runnable
    public void run() {
        if (!isSupportUploadLog()) {
            Slog.w(TAG, "Upload recovery log not supported!");
            return;
        }
        File dcsLogPath = new File(RECOVERY_LOG_DCS_DIR);
        if (!dcsLogPath.exists() && !dcsLogPath.mkdirs()) {
            Slog.d(TAG, "recovery_log create failed:" + dcsLogPath.getPath());
            return;
        }
        if (!chmod(dcsLogPath)) {
            delete(dcsLogPath);
        }
        Slog.d(TAG, "recovery_log create success");
        File cacheFile = new File("/cache/recovery");
        if (!ZipFolder(cacheFile, "/data/persist_log/DCS/de/recovery_log/recoveryLog@")) {
            Slog.d(TAG, "ZipFolder error,delete log");
            delete(dcsLogPath);
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:11:0x005b -> B:5:0x006f). Please report as a decompilation issue!!! */
    private boolean ZipFolder(File srcFile, String zipFileString) {
        boolean result = false;
        ZipOutputStream outZip = null;
        try {
            try {
                try {
                    File zipFile = new File(zipFileString + System.currentTimeMillis() + "@" + SystemProperties.get(OTA_VERSION) + "@" + getSystemCurrentTime() + SplitConstants.DOT_ZIP);
                    outZip = new ZipOutputStream(new FileOutputStream(zipFile));
                    result = ZipFiles(srcFile, outZip);
                    chmod(zipFile);
                    outZip.finish();
                    outZip.close();
                } catch (Throwable th) {
                    if (outZip != null) {
                        try {
                            outZip.finish();
                            outZip.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                result = false;
                if (outZip != null) {
                    outZip.finish();
                    outZip.close();
                }
            }
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return result;
    }

    private boolean ZipFiles(File sourceFile, ZipOutputStream zipOutputSteam) {
        FileInputStream inputStream = null;
        try {
            try {
                if (zipOutputSteam == null) {
                    Slog.w(TAG, "zipOutputSteam = null");
                    if (0 != 0) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return false;
                }
                Slog.w(TAG, "start zipfiles : " + sourceFile.getName());
                if (sourceFile.isFile()) {
                    Slog.w(TAG, "is file : " + sourceFile.getName());
                    ZipEntry zipEntry = new ZipEntry(sourceFile.getName());
                    inputStream = new FileInputStream(sourceFile);
                    zipOutputSteam.putNextEntry(zipEntry);
                    byte[] buffer = new byte[4096];
                    while (true) {
                        int len = inputStream.read(buffer);
                        if (len == -1) {
                            break;
                        }
                        zipOutputSteam.write(buffer, 0, len);
                    }
                    zipOutputSteam.closeEntry();
                } else {
                    File[] fileList = sourceFile.listFiles();
                    Slog.w(TAG, "ZipFiles fileList = " + fileList.toString());
                    if (fileList.length <= 0) {
                        ZipEntry zipEntry2 = new ZipEntry(sourceFile.getName());
                        zipOutputSteam.putNextEntry(zipEntry2);
                        zipOutputSteam.closeEntry();
                    }
                    for (File file : fileList) {
                        if (!ZipFiles(file, zipOutputSteam)) {
                            Slog.w(TAG, "ZipFiles error ,return");
                            if (0 != 0) {
                                try {
                                    inputStream.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            return false;
                        }
                    }
                }
                if (inputStream == null) {
                    return true;
                }
                try {
                    inputStream.close();
                    return true;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    return true;
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            e5.printStackTrace();
            if (0 != 0) {
                try {
                    inputStream.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
            }
            return false;
        }
    }

    private boolean chmod(File file) {
        PosixFilePermission[] permissions777 = {PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_WRITE, PosixFilePermission.GROUP_EXECUTE, PosixFilePermission.OTHERS_READ, PosixFilePermission.OTHERS_WRITE, PosixFilePermission.OTHERS_EXECUTE};
        if (!file.exists()) {
            Slog.d(TAG, "file " + file.getName() + " not exists");
            return false;
        }
        return changePermission(file.getAbsolutePath(), permissions777);
    }

    private boolean changePermission(String pathStr, PosixFilePermission... permissions) {
        if (TextUtils.isEmpty(pathStr)) {
            Slog.d(TAG, "dir is empty!");
            return false;
        }
        File file = new File(pathStr);
        if (!file.exists()) {
            Slog.d(TAG, "file " + pathStr + " not exists");
            return false;
        }
        try {
            Set<PosixFilePermission> permissionSet = new HashSet<>();
            Path path = Paths.get(pathStr, new String[0]);
            for (PosixFilePermission permission : permissions) {
                permissionSet.add(permission);
            }
            Files.setPosixFilePermissions(path, permissionSet);
            Slog.d(TAG, "change permission success :" + pathStr);
            return true;
        } catch (IOException e) {
            Slog.d(TAG, "change permission failed !");
            e.printStackTrace();
            return false;
        }
    }

    private void delete(File file) {
        if (!file.exists()) {
            Slog.e(TAG, file.getName() + " not exist");
            return;
        }
        if (file.isFile()) {
            boolean re = file.delete();
            if (!re) {
                Slog.e(TAG, file.getName() + " delete failed");
                return;
            }
            return;
        }
        boolean re2 = file.isDirectory();
        if (re2) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                boolean re3 = file.delete();
                if (!re3) {
                    Slog.e(TAG, file.getName() + " delete failed");
                    return;
                }
                return;
            }
            for (File file2 : childFiles) {
                delete(file2);
            }
            boolean re4 = file.delete();
            if (!re4) {
                Slog.e(TAG, file.getName() + " delete failed");
            }
        }
    }

    private String getSystemCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return sdf.format(Long.valueOf(System.currentTimeMillis()));
    }

    private boolean isSupportUploadLog() {
        String region = SystemProperties.get("ro.vendor.oplus.regionmark", "");
        if (region != null && "CN".equals(region)) {
            return true;
        }
        return false;
    }
}
