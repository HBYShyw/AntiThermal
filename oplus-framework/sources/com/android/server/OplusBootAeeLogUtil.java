package com.android.server;

import android.content.Context;
import android.os.OplusManager;
import android.os.OplusUsageManager;
import android.os.Process;
import android.os.ProjectManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.olc.ExceptionInfo;
import android.os.olc.OlcManager;
import android.util.Slog;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/* loaded from: classes.dex */
public class OplusBootAeeLogUtil {
    private static final String TAG = "OppoBootReceiver_OppoBootAeeLogUtil";
    private static final String mLastExceptionProperty = "persist.hungtask.oppo.kill";

    private static String isLastSystemServerRebootFormBolckException() {
        String strSend = ProjectManager.getHungtask();
        if (strSend != null && !strSend.trim().isEmpty()) {
            return strSend;
        }
        return null;
    }

    private static boolean isMtkPlatform() {
        return SystemProperties.get("ro.board.platform", "oppo").toLowerCase().startsWith("mt");
    }

    public static int checkMtkHwtState(Context ctx) {
        int type = -1;
        if (!isMtkPlatform()) {
            return -1;
        }
        String bootAeeDB = SystemProperties.get("vendor.debug.mtk.aeev.db", (String) null);
        Slog.d(TAG, "aee db path is " + bootAeeDB);
        String issue = "";
        if (bootAeeDB != null && (bootAeeDB.contains(OplusManager.ISSUE_KERNEL_HWT) || bootAeeDB.contains("HW_Reboot") || bootAeeDB.contains(OplusManager.ISSUE_KERNEL_HANG))) {
            if (bootAeeDB.contains(OplusManager.ISSUE_KERNEL_HWT)) {
                type = 120;
                issue = OplusManager.ISSUE_KERNEL_HWT;
            } else if (bootAeeDB.contains("HW_Reboot")) {
                type = 121;
                issue = OplusManager.ISSUE_KERNEL_HARDWARE_REBOOT;
            } else if (bootAeeDB.contains(OplusManager.ISSUE_KERNEL_HANG)) {
                type = 122;
                issue = OplusManager.ISSUE_KERNEL_HANG;
            }
            Slog.d(TAG, "aee db type is " + type + ", issue is " + issue);
            if (type != -1 && !issue.isEmpty()) {
                OplusManager.writeLogToPartition(type, "HWT_HardwareReboot_HANG", OplusManager.ANDROID_TAG, issue, ctx.getResources().getString(201588998));
            }
        }
        return type;
    }

    public static void prepareMtkLog(boolean isAndroidReboot, String headers) {
        if (!isMtkPlatform()) {
            return;
        }
        String java_uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 15);
        String aeePath = parseAeeLogPath(isAndroidReboot);
        String aeeType = parseAeeTag(isAndroidReboot, aeePath);
        int aeeOLCExceptionId = parseAeeOLCExceptionId(isAndroidReboot, aeePath);
        boolean olcServiceIsWorking = SystemProperties.getBoolean("sys.olc.service.on", false);
        if (isAndroidReboot) {
            int system_server_current_pid = Process.myPid();
            int system_server_previous_pid = SystemProperties.getInt("persist.sys.systemserver.pid", -1);
            if (system_server_current_pid == system_server_previous_pid) {
                Slog.e(TAG, "may not crash, system_server_current_pid == system_server_previous_pid = " + system_server_current_pid);
            } else {
                Slog.d(TAG, "android restart maybe crash or killed, system_server_current_pid = " + system_server_current_pid + " system_server_previous_pid = " + system_server_previous_pid);
            }
            if (aeePath != null && new File(aeePath + "/ZZ_INTERNAL").exists()) {
                SystemProperties.set("sys.mtk.last.aee.db", aeePath);
                if (olcServiceIsWorking) {
                    packageAeeLogsForOLC(aeeType, aeePath, aeeOLCExceptionId);
                    return;
                } else {
                    packageAeeLogs(aeeType, aeePath, java_uuid);
                    return;
                }
            }
            String lastSystemReboot = isLastSystemServerRebootFormBolckException();
            if (lastSystemReboot != null) {
                String str = headers + "system_Server reboot from Block Exception! system_server_current_pid = " + system_server_current_pid + ", system_server_previous_pid = " + system_server_previous_pid + ", lastSystemReboot = " + lastSystemReboot;
            } else {
                String str2 = headers + "system_Server crash but can not get efficacious log! system_server_current_pid = " + system_server_current_pid + ", system_server_previous_pid = " + system_server_previous_pid;
            }
            waitForStringPropertyReady("vendor.debug.mtk.aee.status", "free", "free", 60);
            waitForStringPropertyReady("vendor.debug.mtk.aee.status64", "free", "free", 60);
            String aeePath2 = parseAeeLogPath(isAndroidReboot);
            String aeeType2 = parseAeeTag(isAndroidReboot, aeePath2);
            int aeeOLCExceptionId2 = parseAeeOLCExceptionId(isAndroidReboot, aeePath2);
            if (aeePath2 == null || aeeType2 == null || aeeOLCExceptionId2 == 0) {
                Slog.e(TAG, "prepareMtkLog failed for aeePath or aeeType illegal!");
                return;
            } else if (olcServiceIsWorking) {
                packageAeeLogsForOLC(aeeType2, aeePath2, aeeOLCExceptionId2);
                return;
            } else {
                packageAeeLogs(aeeType2, aeePath2, java_uuid);
                return;
            }
        }
        if (aeePath == null || aeeType == null || aeeOLCExceptionId == 0) {
            Slog.e(TAG, "prepareMtkLog is not unnormal reboot. aeePath is " + aeePath + " aeeType is " + aeeType + " isAndroidReboot = " + isAndroidReboot);
        } else if (olcServiceIsWorking) {
            packageAeeLogsForOLC(aeeType, aeePath, aeeOLCExceptionId);
        } else {
            packageAeeLogs(aeeType, aeePath, java_uuid);
        }
    }

    private static void packageAeeLogs(String aeeType, String aeePath, String uuid) {
        String aeeGzFile = "/data/persist_log/DCS/de/AEE_DB/" + aeeType + "@" + uuid + "@" + SystemProperties.get("ro.build.version.ota") + "@" + System.currentTimeMillis() + ".dat.gz";
        Slog.v(TAG, "prepare zip! aeeType is " + aeeType + " aeePath is " + aeePath);
        try {
            zipFolder(aeePath, "/data/persist_log/DCS/de/AEE_DB/aee.zip");
            gzipFile("/data/persist_log/DCS/de/AEE_DB/aee.zip", aeeGzFile);
            new File("/data/persist_log/DCS/de/AEE_DB/aee.zip").delete();
            if (new File(aeeGzFile).exists()) {
                Slog.v(TAG, "package end, delete file " + aeePath);
                deleteDir(new File(aeePath).getAbsoluteFile());
            }
            SystemProperties.set("sys.backup.minidump.tag", aeeType);
            SystemProperties.set("ctl.start", "backup_minidumplog");
        } catch (Exception e) {
            Slog.e(TAG, "dumpEnvironmentGzFile failed!");
            e.printStackTrace();
        }
    }

    private static void packageAeeLogsForOLC(String aeeType, String aeePath, int aeeOLCExceptionId) {
        try {
            if (copyFolder(aeePath, "/data/persist_log/DCS/de/AEE_DB/aeeForOLC")) {
                SystemProperties.set("sys.backup.minidump.tag", aeeType);
                raiseAeeExceptionToOLC(aeeOLCExceptionId);
            }
        } catch (Exception e) {
            Slog.e(TAG, "dumpEnvironmentGzFile failed!");
            e.printStackTrace();
        }
    }

    private static boolean copyFolder(String sourcePath, String destinationPath) {
        File sourceFile;
        try {
            File destinationFolder = new File(destinationPath);
            if (!destinationFolder.exists() && !destinationFolder.mkdirs()) {
                Slog.e(TAG, "copyFolder: cannot create destination directory.");
                return false;
            }
            File sourceFolder = new File(sourcePath);
            String[] files = sourceFolder.list();
            for (String file : files) {
                if (sourcePath.endsWith(File.separator)) {
                    sourceFile = new File(sourcePath + file);
                } else {
                    sourceFile = new File(sourcePath + File.separator + file);
                }
                if (sourceFile.isDirectory()) {
                    copyFolder(sourcePath + "/" + file, destinationPath + "/" + file);
                } else {
                    if (!sourceFile.exists()) {
                        Slog.e(TAG, "copyFolder:  sourceFile not exist.");
                        return false;
                    }
                    if (!sourceFile.isFile()) {
                        Slog.e(TAG, "copyFolder:  sourceFile not file.");
                        return false;
                    }
                    if (!sourceFile.canRead()) {
                        Slog.e(TAG, "copyFolder:  sourceFile cannot read.");
                        return false;
                    }
                    if (sourceFile.getName().equals(".nomedia")) {
                        Slog.d(TAG, "donot copy nomedia");
                    } else {
                        String sourceFilePath = sourcePath + "/" + file;
                        String destinationFilePath = destinationPath + "/" + file;
                        if (!copyFile(sourceFilePath, destinationFilePath)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyFile(String sourceFile, String destinationFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            while (true) {
                try {
                    try {
                        int byteRead = fileInputStream.read(buffer);
                        if (byteRead != -1) {
                            fileOutputStream.write(buffer, 0, byteRead);
                        } else {
                            return true;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                } finally {
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    private static void raiseAeeExceptionToOLC(int aeeOLCExceptionId) {
        Slog.d(TAG, "start to handle MTK aee reboot data, exceptionId: " + aeeOLCExceptionId);
        ExceptionInfo info = new ExceptionInfo();
        info.setTime(System.currentTimeMillis());
        info.setId(aeeOLCExceptionId);
        info.setExceptionType(0);
        info.setExceptionLevel(0);
        info.setAtomicLogs(2147483648L);
        info.setLogParmas(null);
        if (OlcManager.raiseException(info) != 0) {
            Slog.e(TAG, "failed to raise olc Exception");
        }
    }

    /* loaded from: classes.dex */
    private static class MTKDBExceptionId {
        private static final int MTK_OLC_HANG_ExceptionId = 268558356;
        private static final int MTK_OLC_HARDWARE_REBOOT_ExceptionId = 268558355;
        private static final int MTK_OLC_HWT_ExceptionId = 268558354;
        private static final int MTK_OLC_JE_ExceptionId = 268558338;
        private static final int MTK_OLC_KE_ExceptionId = 268558353;
        private static final int MTK_OLC_NE_ExceptionId = 268558337;
        private static final int MTK_OLC_SWT_ExceptionId = 268558339;

        private MTKDBExceptionId() {
        }
    }

    private static int parseAeeOLCExceptionId(boolean isAndroidReboot, String aeePath) {
        int lastIndex;
        if (aeePath == null || (lastIndex = aeePath.lastIndexOf(".")) == -1) {
            return 0;
        }
        String endStr = aeePath.substring(lastIndex + 1);
        if (endStr.equals("NE")) {
            return 268558337;
        }
        if (endStr.equals("JE")) {
            return 268558338;
        }
        if (endStr.equals("SWT")) {
            return 268558339;
        }
        if (endStr.equals("KE")) {
            return 268558353;
        }
        if (endStr.equals(OplusManager.ISSUE_KERNEL_HWT)) {
            return 268558354;
        }
        if (endStr.equals(OplusManager.ISSUE_KERNEL_HARDWARE_REBOOT)) {
            return 268558355;
        }
        if (endStr.equals(OplusManager.ISSUE_KERNEL_HANG)) {
            return 268558356;
        }
        return isAndroidReboot ? 268558338 : 268558353;
    }

    private static void waitForIntPropertyReady(String prop, int failValue, int expectValue, int maxTime) {
        Slog.d(TAG, "waitForPropertyReady!int " + prop);
        SystemClock.sleep(2000L);
        int loopCount = maxTime * 2;
        for (int i = 0; i < loopCount && SystemProperties.getInt(prop, failValue) != expectValue; i++) {
            SystemClock.sleep(500L);
        }
        SystemClock.sleep(1000L);
        Slog.d(TAG, "waitForPropertyReady end!int " + prop);
    }

    private static void waitForStringPropertyReady(String prop, String failValue, String expectValue, int maxTime) {
        Slog.d(TAG, "waitForPropertyReady!String " + prop);
        SystemClock.sleep(2000L);
        int i = maxTime * 2;
        for (int i2 = 0; i2 < 40; i2++) {
            SystemClock.sleep(500L);
            if (SystemProperties.get(prop, failValue).equals(expectValue)) {
                break;
            }
        }
        SystemClock.sleep(1000L);
        Slog.d(TAG, "waitForPropertyReady end!String " + prop);
    }

    private static String tryGetStringProperty(String prop, int maxTimes) {
        Slog.d(TAG, "tryGetStringProperty!String " + prop);
        for (int i = 0; i < maxTimes; i++) {
            String aeeDBProp = SystemProperties.get(prop);
            if (aeeDBProp != null && aeeDBProp.length() > 0) {
                Slog.i(TAG, " tryGetStringProperty aeeDBProp !String " + aeeDBProp);
                return aeeDBProp;
            }
            SystemClock.sleep(5000L);
        }
        Slog.d(TAG, "tryGetStringProperty end!String " + prop);
        return null;
    }

    private static String parseAeeTag(boolean isAndroidReboot, String aeePath) {
        int lastIndex;
        if (aeePath == null || (lastIndex = aeePath.lastIndexOf(".")) == -1) {
            return null;
        }
        String endStr = aeePath.substring(lastIndex + 1);
        if (endStr.equals("NE")) {
            return "AEE_SYSTEM_TOMBSTONE_CRASH";
        }
        if (endStr.equals("JE")) {
            return "AEE_SYSTEM_SERVER";
        }
        if (endStr.equals("SWT")) {
            return "AEE_SYSTEM_SERVER_WATCHDOG";
        }
        return (endStr.equals("KE") || endStr.equals(OplusManager.ISSUE_KERNEL_HWT) || endStr.equals(OplusManager.ISSUE_KERNEL_HARDWARE_REBOOT) || endStr.equals(OplusManager.ISSUE_KERNEL_HANG) || !isAndroidReboot) ? "AEE_SYSTEM_LAST_KMSG" : "AEE_SYSTEM_SERVER";
    }

    private static String parseAeeLogPath(boolean isAndroidReboot) {
        String aeePropName = isAndroidReboot ? "vendor.debug.mtk.aee.db" : "vendor.debug.mtk.aeev.db";
        String aeeDBProp = tryGetStringProperty(aeePropName, 20);
        if (aeeDBProp == null || aeeDBProp.equals("")) {
            Slog.i(TAG, " parserAeeLogPath aeeDBProp is null");
            return null;
        }
        if (aeeDBProp.indexOf(":") == -1) {
            Slog.w(TAG, "parserAeeLogPath aeeDBProp " + aeeDBProp + " is not null but inavailable");
            return null;
        }
        String aeePath = aeeDBProp.substring(aeeDBProp.indexOf(":") + 1);
        if (!isAndroidReboot) {
            moveVendorAeeToData(aeePath);
            return aeePath.replaceFirst("/data/vendor/aee_exp", "/data/persist_log/aee_exp");
        }
        return aeePath;
    }

    private static boolean moveVendorAeeToData(String aeePath) {
        OplusUsageManager usageManager = OplusUsageManager.getOplusUsageManager();
        if (usageManager == null) {
            Slog.e(TAG, "moveVendorAeeToData can not find usageManager");
            return false;
        }
        String dataAeePath = aeePath.replaceFirst("/data/vendor/aee_exp", "/data/persist_log/aee_exp");
        return usageManager.readEntireOplusDir(aeePath, dataAeePath, true);
    }

    private static void zipFolder(String inputFolderPath, String outZipPath) {
        File srcFile = new File(inputFolderPath);
        File[] files = srcFile.listFiles();
        Slog.d(TAG, "Zip directory: " + inputFolderPath + " to " + outZipPath);
        ZipOutputStream zos = null;
        try {
            try {
                zos = new ZipOutputStream(new FileOutputStream(outZipPath));
                byte[] buf = new byte[1024];
                for (File file : files) {
                    if (file != null && file.canRead()) {
                        try {
                            InputStream is = new BufferedInputStream(new FileInputStream(file));
                            try {
                                ZipEntry entry = new ZipEntry(file.getName());
                                zos.putNextEntry(entry);
                                while (true) {
                                    int len = is.read(buf, 0, 1024);
                                    if (len <= 0) {
                                        break;
                                    } else {
                                        zos.write(buf, 0, len);
                                    }
                                }
                                zos.closeEntry();
                                is.close();
                                is.close();
                            } catch (Throwable th) {
                                try {
                                    is.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                                throw th;
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    zos.close();
                } catch (IOException e2) {
                }
            } catch (IOException e3) {
                Slog.e(TAG, "error zipping up profile data", e3);
                if (zos != null) {
                    try {
                        zos.close();
                    } catch (IOException e4) {
                    }
                }
            }
        } catch (Throwable th3) {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e5) {
                }
            }
            throw th3;
        }
    }

    public static void gzipFile(String source_filepath, String destinaton_zip_filepath) {
        byte[] buffer = new byte[1024];
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(destinaton_zip_filepath);
            GZIPOutputStream gzipOuputStream = new GZIPOutputStream(fileOutputStream);
            FileInputStream fileInput = new FileInputStream(source_filepath);
            while (true) {
                int bytes_read = fileInput.read(buffer);
                if (bytes_read > 0) {
                    gzipOuputStream.write(buffer, 0, bytes_read);
                } else {
                    fileInput.close();
                    gzipOuputStream.finish();
                    gzipOuputStream.close();
                    fileOutputStream.close();
                    Slog.d(TAG, "The file was compressed successfully!");
                    return;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void deleteDir(File dir) {
        if (!dir.exists()) {
            return;
        }
        File[] fileList = dir.listFiles();
        if (dir.isDirectory() && fileList != null && fileList.length > 0) {
            for (File file : fileList) {
                deleteDir(file);
            }
            deleteFile(dir);
            return;
        }
        deleteFile(dir);
    }

    private static void deleteFile(File dir) {
        if (dir.delete()) {
            Slog.w(TAG, "file: " + dir + " delete succeed");
        } else {
            Slog.e(TAG, "file: " + dir + " delete failed");
        }
    }
}
