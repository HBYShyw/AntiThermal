package com.android.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.DropBoxManager;
import android.os.Environment;
import android.os.FileUtils;
import android.os.MessageQueue;
import android.os.ParcelFileDescriptor;
import android.os.RecoverySystem;
import android.os.SystemProperties;
import android.provider.Downloads;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.text.TextUtils;
import android.util.AtomicFile;
import android.util.EventLog;
import android.util.Slog;
import android.util.Xml;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.am.DropboxRateLimiter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BootReceiver extends BroadcastReceiver {
    private static final String E2FSCK_FS_MODIFIED = "FILE SYSTEM WAS MODIFIED";
    private static final String ERROR_REPORT_TRACE_PIPE = "/sys/kernel/tracing/instances/bootreceiver/trace_pipe";
    private static final String F2FS_FSCK_FS_MODIFIED = "[FSCK] Unreachable";
    private static final String FSCK_PASS_PATTERN = "Pass ([1-9]E?):";
    private static final String FSCK_TREE_OPTIMIZATION_PATTERN = "Inode [0-9]+ extent tree.*could be shorter";
    private static final int FS_STAT_FSCK_FS_FIXED = 1024;
    private static final String FS_STAT_PATTERN = "fs_stat,[^,]*/([^/,]+),(0x[0-9a-fA-F]+)";
    private static final int GMSCORE_LASTK_LOG_SIZE = 196608;
    private static final int LASTK_LOG_SIZE;
    private static final String LAST_HEADER_FILE = "last-header.txt";
    private static final String[] LAST_KMSG_FILES;
    private static final String LAST_SHUTDOWN_TIME_PATTERN = "powerctl_shutdown_time_ms:([0-9]+):([0-9]+)";
    private static final String LOG_FILES_FILE = "log-files.xml";
    private static final int LOG_SIZE = 4194304;
    private static final int MAX_ERROR_REPORTS = 8;
    private static final String METRIC_SHUTDOWN_TIME_START = "begin_shutdown";
    private static final String METRIC_SYSTEM_SERVER = "shutdown_system_server";
    private static final String[] MOUNT_DURATION_PROPS_POSTFIX;
    private static final String OLD_UPDATER_CLASS = "com.google.android.systemupdater.SystemUpdateReceiver";
    private static final String OLD_UPDATER_PACKAGE = "com.google.android.systemupdater";
    private static final String SHUTDOWN_METRICS_FILE = "/data/system/shutdown-metrics.txt";
    private static final String SHUTDOWN_TRON_METRICS_PREFIX = "shutdown_";
    private static final String TAG = "BootReceiver";
    private static final String TAG_TOMBSTONE = "SYSTEM_TOMBSTONE";
    private static final String TAG_TOMBSTONE_PROTO = "SYSTEM_TOMBSTONE_PROTO";
    private static final String TAG_TOMBSTONE_PROTO_WITH_HEADERS = "SYSTEM_TOMBSTONE_PROTO_WITH_HEADERS";
    private static final String TAG_TRUNCATED = "[[TRUNCATED]]\n";
    private static final File TOMBSTONE_TMP_DIR;
    private static final int UMOUNT_STATUS_NOT_AVAILABLE = 4;
    private static final File lastHeaderFile;
    public static IBootReceiverExt mBootReceiverExt;
    private static BootReceiverWrapper mBootReceiverWrapper;
    private static final DropboxRateLimiter sDropboxRateLimiter;
    private static final AtomicFile sFile;
    private static int sSentReports;

    static {
        LASTK_LOG_SIZE = SystemProperties.getInt("ro.debuggable", 0) == 1 ? GMSCORE_LASTK_LOG_SIZE : 65536;
        TOMBSTONE_TMP_DIR = new File("/data/tombstones");
        sFile = new AtomicFile(new File(Environment.getDataSystemDirectory(), LOG_FILES_FILE), "log-files");
        lastHeaderFile = new File(Environment.getDataSystemDirectory(), LAST_HEADER_FILE);
        MOUNT_DURATION_PROPS_POSTFIX = new String[]{"early", "default", "late"};
        LAST_KMSG_FILES = new String[]{"/sys/fs/pstore/console-ramoops", "/proc/last_kmsg"};
        sSentReports = 0;
        mBootReceiverExt = (IBootReceiverExt) ExtLoader.type(IBootReceiverExt.class).create();
        sDropboxRateLimiter = new DropboxRateLimiter();
        mBootReceiverWrapper = new BootReceiverWrapper();
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(final Context context, Intent intent) {
        mBootReceiverExt.init(context);
        new Thread() { // from class: com.android.server.BootReceiver.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                BootReceiver.mBootReceiverExt.incrementCriticalDataAndRecordRebootBlocked();
                BootReceiver.mBootReceiverExt.notifyOTAUpdateResult(context);
                try {
                    BootReceiver.this.logBootEvents(context);
                } catch (Exception e) {
                    Slog.e(BootReceiver.TAG, "Can't log boot events", e);
                }
                try {
                    BootReceiver.this.removeOldUpdatePackages(context);
                } catch (Exception e2) {
                    Slog.e(BootReceiver.TAG, "Can't remove old update packages", e2);
                }
                BootReceiver.mBootReceiverExt.syncCacheToEmmc();
                BootReceiver.mBootReceiverExt.initPowerkeyMonitor();
            }
        }.start();
        try {
            FileDescriptor open = Os.open(ERROR_REPORT_TRACE_PIPE, OsConstants.O_RDONLY, FrameworkStatsLog.NON_A11Y_TOOL_SERVICE_WARNING_REPORT);
            IoThread.get().getLooper().getQueue().addOnFileDescriptorEventListener(open, 1, new MessageQueue.OnFileDescriptorEventListener() { // from class: com.android.server.BootReceiver.2
                final int mBufferSize = 1024;
                byte[] mTraceBuffer = new byte[1024];

                @Override // android.os.MessageQueue.OnFileDescriptorEventListener
                public int onFileDescriptorEvents(FileDescriptor fileDescriptor, int i) {
                    try {
                        if (Os.read(fileDescriptor, this.mTraceBuffer, 0, 1024) > 0 && new String(this.mTraceBuffer).indexOf("\n") != -1 && BootReceiver.sSentReports < 8) {
                            SystemProperties.set("dmesgd.start", "1");
                            BootReceiver.sSentReports++;
                        }
                        return 1;
                    } catch (Exception e) {
                        Slog.wtf(BootReceiver.TAG, "Error watching for trace events", e);
                        return 0;
                    }
                }
            });
        } catch (ErrnoException e) {
            Slog.wtf(TAG, "Could not open /sys/kernel/tracing/instances/bootreceiver/trace_pipe", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeOldUpdatePackages(Context context) {
        Downloads.removeAllDownloadsByPackage(context, OLD_UPDATER_PACKAGE, OLD_UPDATER_CLASS);
    }

    private static String getPreviousBootHeaders() {
        try {
            return FileUtils.readTextFile(lastHeaderFile, 0, null);
        } catch (IOException unused) {
            return null;
        }
    }

    private static String getCurrentBootHeaders() throws IOException {
        StringBuilder sb = new StringBuilder(512);
        sb.append("Build: ");
        sb.append(Build.FINGERPRINT);
        sb.append("\n");
        sb.append("Hardware: ");
        sb.append(Build.BOARD);
        sb.append("\n");
        sb.append("Revision: ");
        sb.append(SystemProperties.get("ro.revision", ""));
        sb.append("\n");
        sb.append("Bootloader: ");
        sb.append(Build.BOOTLOADER);
        sb.append("\n");
        sb.append("Radio: ");
        sb.append(Build.getRadioVersion());
        sb.append("\n");
        sb.append("Kernel: ");
        sb.append(FileUtils.readTextFile(new File("/proc/version"), 1024, "...\n"));
        sb.append("\n");
        return sb.toString();
    }

    private static String getBootHeadersToLogAndUpdate() throws IOException {
        String previousBootHeaders = getPreviousBootHeaders();
        String currentBootHeaders = getCurrentBootHeaders();
        try {
            FileUtils.stringToFile(lastHeaderFile, currentBootHeaders);
        } catch (IOException e) {
            Slog.e(TAG, "Error writing " + lastHeaderFile, e);
        }
        if (previousBootHeaders == null) {
            return "isPrevious: false\n" + currentBootHeaders;
        }
        return "isPrevious: true\n" + previousBootHeaders;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logBootEvents(Context context) throws IOException {
        DropBoxManager dropBoxManager = (DropBoxManager) context.getSystemService("dropbox");
        String bootHeadersToLogAndUpdate = getBootHeadersToLogAndUpdate();
        SystemProperties.get("ro.boot.bootreason", (String) null);
        String handleAftermath = RecoverySystem.handleAftermath(context);
        if (handleAftermath != null && dropBoxManager != null) {
            dropBoxManager.addText("SYSTEM_RECOVERY_LOG", bootHeadersToLogAndUpdate + handleAftermath);
        }
        HashMap<String, Long> readTimestamps = readTimestamps();
        if (SystemProperties.getLong("ro.runtime.firstboot", 0L) == 0) {
            SystemProperties.set("ro.runtime.firstboot", Long.toString(System.currentTimeMillis()));
            if (dropBoxManager != null) {
                dropBoxManager.addText("SYSTEM_BOOT", bootHeadersToLogAndUpdate);
            }
            mBootReceiverExt.recordAbnormalRestart(dropBoxManager);
            addFileToDropBox(dropBoxManager, readTimestamps, bootHeadersToLogAndUpdate, "/cache/recovery/last_kmsg", -4194304, "SYSTEM_RECOVERY_KMSG");
            addAuditErrorsToDropBox(dropBoxManager, readTimestamps, bootHeadersToLogAndUpdate, -4194304, "SYSTEM_AUDIT");
        } else {
            if (dropBoxManager != null) {
                dropBoxManager.addText("SYSTEM_RESTART", bootHeadersToLogAndUpdate);
            }
            mBootReceiverExt.addFile(dropBoxManager, readTimestamps, bootHeadersToLogAndUpdate, context);
        }
        logFsShutdownTime();
        logFsMountTime();
        addFsckErrorsToDropBoxAndLogFsStat(dropBoxManager, readTimestamps, bootHeadersToLogAndUpdate, -4194304, "SYSTEM_FSCK");
        logSystemServerShutdownTimeMetrics();
        writeTimestamps(readTimestamps);
    }

    @VisibleForTesting
    public static void resetDropboxRateLimiter() {
        sDropboxRateLimiter.reset();
    }

    public static void addTombstoneToDropBox(Context context, File file, boolean z, String str) {
        DropBoxManager dropBoxManager = (DropBoxManager) context.getSystemService(DropBoxManager.class);
        if (dropBoxManager == null) {
            Slog.e(TAG, "Can't log tombstone: DropBoxManager not available");
            return;
        }
        DropboxRateLimiter.RateLimitResult shouldRateLimit = sDropboxRateLimiter.shouldRateLimit(z ? TAG_TOMBSTONE_PROTO_WITH_HEADERS : TAG_TOMBSTONE, str);
        if (shouldRateLimit.shouldRateLimit()) {
            return;
        }
        HashMap<String, Long> readTimestamps = readTimestamps();
        try {
            if (z) {
                if (recordFileTimestamp(file, readTimestamps)) {
                    byte[] readAllBytes = Files.readAllBytes(file.toPath());
                    File createTempFile = File.createTempFile(file.getName(), ".tmp", TOMBSTONE_TMP_DIR);
                    Files.setPosixFilePermissions(createTempFile.toPath(), PosixFilePermissions.fromString("rw-rw----"));
                    try {
                        try {
                            ProtoOutputStream protoOutputStream = new ProtoOutputStream(ParcelFileDescriptor.open(createTempFile, 805306368).getFileDescriptor());
                            protoOutputStream.write(1151051235329L, readAllBytes);
                            protoOutputStream.write(1120986464258L, shouldRateLimit.droppedCountSinceRateLimitActivated());
                            protoOutputStream.flush();
                            dropBoxManager.addFile(TAG_TOMBSTONE_PROTO_WITH_HEADERS, createTempFile, 0);
                        } catch (FileNotFoundException e) {
                            Slog.e(TAG, "failed to open for write: " + createTempFile, e);
                            throw e;
                        }
                    } finally {
                        createTempFile.delete();
                    }
                }
            } else {
                addFileToDropBox(dropBoxManager, readTimestamps, getBootHeadersToLogAndUpdate() + shouldRateLimit.createHeader(), file.getPath(), 4194304, TAG_TOMBSTONE);
            }
            mBootReceiverExt.hookAddTombstoneToDropBox(file.getPath());
        } catch (IOException e2) {
            Slog.e(TAG, "Can't log tombstone", e2);
        } catch (Exception e3) {
            Slog.e(TAG, "Can't log tombstone", e3);
        }
        writeTimestamps(readTimestamps);
    }

    private static void addLastkToDropBox(DropBoxManager dropBoxManager, HashMap<String, Long> hashMap, String str, String str2, String str3, int i, String str4) throws IOException {
        int length = str.length() + 14 + str2.length();
        if (LASTK_LOG_SIZE + length > GMSCORE_LASTK_LOG_SIZE) {
            i = GMSCORE_LASTK_LOG_SIZE > length ? -(GMSCORE_LASTK_LOG_SIZE - length) : 0;
        }
        addFileWithFootersToDropBox(dropBoxManager, hashMap, str, str2, str3, i, str4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addFileToDropBox(DropBoxManager dropBoxManager, HashMap<String, Long> hashMap, String str, String str2, int i, String str3) throws IOException {
        addFileWithFootersToDropBox(dropBoxManager, hashMap, str, "", str2, i, str3);
    }

    private static void addFileWithFootersToDropBox(DropBoxManager dropBoxManager, HashMap<String, Long> hashMap, String str, String str2, String str3, int i, String str4) throws IOException {
        if (dropBoxManager == null || !dropBoxManager.isTagEnabled(str4)) {
            return;
        }
        File file = new File(str3);
        long lastModified = file.lastModified();
        if (lastModified <= 0) {
            return;
        }
        if (hashMap.containsKey(str3) && hashMap.get(str3).longValue() == lastModified && !str4.equals("SYSTEM_TOMBSTONE_CRASH")) {
            return;
        }
        if (recordFileTimestamp(file, hashMap) || (str4.equals("SYSTEM_TOMBSTONE_CRASH") && file.lastModified() > 0)) {
            String readTextFile = FileUtils.readTextFile(file, i, TAG_TRUNCATED);
            String str5 = str + readTextFile + str2;
            if (str4.equals(TAG_TOMBSTONE) && readTextFile.contains(">>> system_server <<<")) {
                addTextToDropBox(dropBoxManager, "system_server_native_crash", str5, str3, i);
            }
            if (str4.equals(TAG_TOMBSTONE)) {
                FrameworkStatsLog.write(186);
            }
            addTextToDropBox(dropBoxManager, str4, str5, str3, i);
        }
    }

    private static boolean recordFileTimestamp(File file, HashMap<String, Long> hashMap) {
        long lastModified = file.lastModified();
        if (lastModified <= 0) {
            return false;
        }
        String path = file.getPath();
        if (hashMap.containsKey(path) && hashMap.get(path).longValue() == lastModified) {
            return false;
        }
        hashMap.put(path, Long.valueOf(lastModified));
        return true;
    }

    private static void addTextToDropBox(DropBoxManager dropBoxManager, String str, String str2, String str3, int i) {
        Slog.i(TAG, "Copying " + str3 + " to DropBox (" + str + ")");
        dropBoxManager.addText(str, str2);
        EventLog.writeEvent(81002, str3, Integer.valueOf(i), str);
    }

    private static void addAuditErrorsToDropBox(DropBoxManager dropBoxManager, HashMap<String, Long> hashMap, String str, int i, String str2) throws IOException {
        if (dropBoxManager == null || !dropBoxManager.isTagEnabled(str2)) {
            return;
        }
        Slog.i(TAG, "Copying audit failures to DropBox");
        File file = new File("/proc/last_kmsg");
        long lastModified = file.lastModified();
        if (lastModified <= 0) {
            file = new File("/sys/fs/pstore/console-ramoops");
            lastModified = file.lastModified();
            if (lastModified <= 0) {
                file = new File("/sys/fs/pstore/console-ramoops-0");
                lastModified = file.lastModified();
            }
        }
        if (lastModified <= 0) {
            return;
        }
        if (hashMap.containsKey(str2) && hashMap.get(str2).longValue() == lastModified) {
            return;
        }
        hashMap.put(str2, Long.valueOf(lastModified));
        String readTextFile = FileUtils.readTextFile(file, i, TAG_TRUNCATED);
        StringBuilder sb = new StringBuilder();
        for (String str3 : readTextFile.split("\n")) {
            if (str3.contains("audit")) {
                sb.append(str3 + "\n");
            }
        }
        Slog.i(TAG, "Copied " + sb.toString().length() + " worth of audits to DropBox");
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(sb.toString());
        dropBoxManager.addText(str2, sb2.toString());
    }

    private static void addFsckErrorsToDropBoxAndLogFsStat(DropBoxManager dropBoxManager, HashMap<String, Long> hashMap, String str, int i, String str2) throws IOException {
        boolean z = dropBoxManager != null && dropBoxManager.isTagEnabled(str2);
        Slog.i(TAG, "Checking for fsck errors");
        File file = new File("/dev/fscklogs/log");
        if (file.lastModified() <= 0) {
            return;
        }
        String readTextFile = FileUtils.readTextFile(file, i, TAG_TRUNCATED);
        Pattern compile = Pattern.compile(FS_STAT_PATTERN);
        String[] split = readTextFile.split("\n");
        boolean z2 = false;
        int i2 = 0;
        int i3 = 0;
        for (String str3 : split) {
            if (str3.contains(E2FSCK_FS_MODIFIED) || str3.contains(F2FS_FSCK_FS_MODIFIED)) {
                z2 = true;
            } else if (str3.contains("fs_stat")) {
                Matcher matcher = compile.matcher(str3);
                if (matcher.find()) {
                    handleFsckFsStat(matcher, split, i2, i3);
                    i2 = i3;
                } else {
                    Slog.w(TAG, "cannot parse fs_stat:" + str3);
                }
            }
            i3++;
        }
        if (z && z2) {
            addFileToDropBox(dropBoxManager, hashMap, str, "/dev/fscklogs/log", i, str2);
        }
        file.renameTo(new File("/dev/fscklogs/fsck"));
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0029. Please report as an issue. */
    private static void logFsMountTime() {
        int i;
        for (String str : MOUNT_DURATION_PROPS_POSTFIX) {
            int i2 = SystemProperties.getInt("ro.boottime.init.mount_all." + str, 0);
            if (i2 != 0) {
                str.hashCode();
                char c = 65535;
                switch (str.hashCode()) {
                    case 3314342:
                        if (str.equals("late")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 96278371:
                        if (str.equals("early")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1544803905:
                        if (str.equals("default")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        i = 12;
                        break;
                    case 1:
                        i = 11;
                        break;
                    case 2:
                        i = 10;
                        break;
                }
                FrameworkStatsLog.write(FrameworkStatsLog.BOOT_TIME_EVENT_DURATION_REPORTED, i, i2);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0032  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void logSystemServerShutdownTimeMetrics() {
        String readTextFile;
        File file = new File(SHUTDOWN_METRICS_FILE);
        String str = null;
        if (file.exists()) {
            try {
                readTextFile = FileUtils.readTextFile(file, 0, null);
            } catch (IOException e) {
                Slog.e(TAG, "Problem reading " + file, e);
            }
            if (!TextUtils.isEmpty(readTextFile)) {
                String str2 = null;
                String str3 = null;
                String str4 = null;
                for (String str5 : readTextFile.split(",")) {
                    String[] split = str5.split(":");
                    if (split.length != 2) {
                        Slog.e(TAG, "Wrong format of shutdown metrics - " + readTextFile);
                    } else {
                        if (split[0].startsWith(SHUTDOWN_TRON_METRICS_PREFIX)) {
                            logTronShutdownMetric(split[0], split[1]);
                            if (split[0].equals(METRIC_SYSTEM_SERVER)) {
                                str4 = split[1];
                            }
                        }
                        if (split[0].equals("reboot")) {
                            str = split[1];
                        } else if (split[0].equals("reason")) {
                            str2 = split[1];
                        } else if (split[0].equals(METRIC_SHUTDOWN_TIME_START)) {
                            str3 = split[1];
                        }
                    }
                }
                logStatsdShutdownAtom(str, str2, str3, str4);
            }
            file.delete();
        }
        readTextFile = null;
        if (!TextUtils.isEmpty(readTextFile)) {
        }
        file.delete();
    }

    private static void logTronShutdownMetric(String str, String str2) {
        try {
            int parseInt = Integer.parseInt(str2);
            if (parseInt >= 0) {
                MetricsLogger.histogram((Context) null, str, parseInt);
            }
        } catch (NumberFormatException unused) {
            Slog.e(TAG, "Cannot parse metric " + str + " int value - " + str2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0065 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0043 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0037  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void logStatsdShutdownAtom(String str, String str2, String str3, String str4) {
        boolean z;
        long parseLong;
        if (str != null) {
            if (!str.equals("y")) {
                if (!str.equals("n")) {
                    Slog.e(TAG, "Unexpected value for reboot : " + str);
                }
            } else {
                z = true;
                boolean z2 = z;
                if (str2 == null) {
                    Slog.e(TAG, "No value received for shutdown reason");
                    str2 = "<EMPTY>";
                }
                String str5 = str2;
                long j = 0;
                if (str3 == null) {
                    try {
                        parseLong = Long.parseLong(str3);
                    } catch (NumberFormatException unused) {
                        Slog.e(TAG, "Cannot parse shutdown start time: " + str3);
                    }
                    if (str4 != null) {
                        try {
                            j = Long.parseLong(str4);
                        } catch (NumberFormatException unused2) {
                            Slog.e(TAG, "Cannot parse shutdown duration: " + str3);
                        }
                    } else {
                        Slog.e(TAG, "No value received for shutdown duration");
                    }
                    FrameworkStatsLog.write(56, z2, str5, parseLong, j);
                }
                Slog.e(TAG, "No value received for shutdown start time");
                parseLong = 0;
                if (str4 != null) {
                }
                FrameworkStatsLog.write(56, z2, str5, parseLong, j);
            }
        } else {
            Slog.e(TAG, "No value received for reboot");
        }
        z = false;
        boolean z22 = z;
        if (str2 == null) {
        }
        String str52 = str2;
        long j2 = 0;
        if (str3 == null) {
        }
        parseLong = 0;
        if (str4 != null) {
        }
        FrameworkStatsLog.write(56, z22, str52, parseLong, j2);
    }

    private static void logFsShutdownTime() {
        File file;
        String[] strArr = LAST_KMSG_FILES;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                file = null;
                break;
            }
            file = new File(strArr[i]);
            if (file.exists()) {
                break;
            } else {
                i++;
            }
        }
        if (file == null) {
            return;
        }
        try {
            Matcher matcher = Pattern.compile(LAST_SHUTDOWN_TIME_PATTERN, 8).matcher(FileUtils.readTextFile(file, -16384, null));
            if (matcher.find()) {
                FrameworkStatsLog.write(FrameworkStatsLog.BOOT_TIME_EVENT_DURATION_REPORTED, 9, Integer.parseInt(matcher.group(1)));
                FrameworkStatsLog.write(FrameworkStatsLog.BOOT_TIME_EVENT_ERROR_CODE_REPORTED, 2, Integer.parseInt(matcher.group(2)));
                Slog.i(TAG, "boot_fs_shutdown," + matcher.group(1) + "," + matcher.group(2));
                return;
            }
            FrameworkStatsLog.write(FrameworkStatsLog.BOOT_TIME_EVENT_ERROR_CODE_REPORTED, 2, 4);
            Slog.w(TAG, "boot_fs_shutdown, string not found");
        } catch (IOException e) {
            Slog.w(TAG, "cannot read last msg", e);
        }
    }

    @VisibleForTesting
    public static int fixFsckFsStat(String str, int i, String[] strArr, int i2, int i3) {
        String str2;
        boolean z;
        int i4;
        if ((i & 1024) != 0) {
            Pattern compile = Pattern.compile(FSCK_PASS_PATTERN);
            Pattern compile2 = Pattern.compile(FSCK_TREE_OPTIMIZATION_PATTERN);
            String str3 = "";
            boolean z2 = false;
            boolean z3 = false;
            boolean z4 = false;
            int i5 = i2;
            while (i5 < i3) {
                str2 = strArr[i5];
                if (str2.contains(E2FSCK_FS_MODIFIED) || str2.contains(F2FS_FSCK_FS_MODIFIED)) {
                    break;
                }
                if (str2.startsWith("Pass ")) {
                    Matcher matcher = compile.matcher(str2);
                    if (matcher.find()) {
                        str3 = matcher.group(1);
                    }
                    i4 = 1;
                } else if (str2.startsWith("Inode ")) {
                    if (!compile2.matcher(str2).find() || !str3.equals("1")) {
                        z = true;
                        break;
                    }
                    Slog.i(TAG, "fs_stat, partition:" + str + " found tree optimization:" + str2);
                    i4 = 1;
                    z2 = true;
                } else if (str2.startsWith("[QUOTA WARNING]") && str3.equals("5")) {
                    Slog.i(TAG, "fs_stat, partition:" + str + " found quota warning:" + str2);
                    if (!z2) {
                        z = false;
                        z3 = true;
                        break;
                    }
                    i4 = 1;
                    z3 = true;
                } else {
                    if (!str2.startsWith("Update quota info") || !str3.equals("5")) {
                        if (str2.startsWith("Timestamp(s) on inode") && str2.contains("beyond 2310-04-04 are likely pre-1970") && str3.equals("1")) {
                            Slog.i(TAG, "fs_stat, partition:" + str + " found timestamp adjustment:" + str2);
                            int i6 = i5 + 1;
                            if (strArr[i6].contains("Fix? yes")) {
                                i5 = i6;
                            }
                            i4 = 1;
                            z4 = true;
                        } else {
                            str2 = str2.trim();
                            if (!str2.isEmpty() && !str3.isEmpty()) {
                                z = true;
                                break;
                            }
                        }
                    }
                    i4 = 1;
                }
                i5 += i4;
            }
            str2 = null;
            z = false;
            if (z) {
                if (str2 != null) {
                    Slog.i(TAG, "fs_stat, partition:" + str + " fix:" + str2);
                }
            } else if (z3 && !z2) {
                Slog.i(TAG, "fs_stat, got quota fix without tree optimization, partition:" + str);
            } else if ((z2 && z3) || z4) {
                Slog.i(TAG, "fs_stat, partition:" + str + " fix ignored");
                return i & (-1025);
            }
        }
        return i;
    }

    private static void handleFsckFsStat(Matcher matcher, String[] strArr, int i, int i2) {
        String group = matcher.group(1);
        try {
            int fixFsckFsStat = fixFsckFsStat(group, Integer.decode(matcher.group(2)).intValue(), strArr, i, i2);
            if ("userdata".equals(group) || "data".equals(group)) {
                FrameworkStatsLog.write(FrameworkStatsLog.BOOT_TIME_EVENT_ERROR_CODE_REPORTED, 3, fixFsckFsStat);
            }
            Slog.i(TAG, "fs_stat, partition:" + group + " stat:0x" + Integer.toHexString(fixFsckFsStat));
        } catch (NumberFormatException unused) {
            Slog.w(TAG, "cannot parse fs_stat: partition:" + group + " stat:" + matcher.group(2));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x013c, code lost:
    
        if (r2 != false) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0116, code lost:
    
        if (r7 != false) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00be, code lost:
    
        if (r7 != false) goto L90;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static HashMap<String, Long> readTimestamps() {
        HashMap<String, Long> hashMap;
        boolean z;
        XmlPullParserException e;
        NullPointerException e2;
        IllegalStateException e3;
        IOException e4;
        FileInputStream openRead;
        int next;
        AtomicFile atomicFile = sFile;
        synchronized (atomicFile) {
            hashMap = new HashMap<>();
            boolean z2 = false;
            try {
            } catch (Throwable th) {
                th = th;
            }
            try {
                try {
                    openRead = atomicFile.openRead();
                } catch (FileNotFoundException unused) {
                } catch (IOException e5) {
                    z = false;
                    e4 = e5;
                } catch (IllegalStateException e6) {
                    z = false;
                    e3 = e6;
                } catch (NullPointerException e7) {
                    z = false;
                    e2 = e7;
                } catch (XmlPullParserException e8) {
                    z = false;
                    e = e8;
                }
                try {
                    TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(openRead);
                    do {
                        next = resolvePullParser.next();
                        z = true;
                        if (next == 2) {
                            break;
                        }
                    } while (next != 1);
                    if (next != 2) {
                        throw new IllegalStateException("no start tag found");
                    }
                    int depth = resolvePullParser.getDepth();
                    while (true) {
                        int next2 = resolvePullParser.next();
                        if (next2 == 1 || (next2 == 3 && resolvePullParser.getDepth() <= depth)) {
                            break;
                        }
                        if (next2 != 3 && next2 != 4) {
                            if (resolvePullParser.getName().equals("log")) {
                                hashMap.put(resolvePullParser.getAttributeValue((String) null, "filename"), Long.valueOf(resolvePullParser.getAttributeLong((String) null, "timestamp")));
                            } else {
                                Slog.w(TAG, "Unknown tag: " + resolvePullParser.getName());
                                XmlUtils.skipCurrentTag(resolvePullParser);
                            }
                        }
                    }
                    if (openRead != null) {
                        try {
                            openRead.close();
                        } catch (FileNotFoundException unused2) {
                            z2 = true;
                            Slog.i(TAG, "No existing last log timestamp file " + sFile.getBaseFile() + "; starting empty");
                        } catch (IOException e9) {
                            e4 = e9;
                            Slog.w(TAG, "Failed parsing " + e4);
                        } catch (IllegalStateException e10) {
                            e3 = e10;
                            Slog.w(TAG, "Failed parsing " + e3);
                            if (!z) {
                                hashMap.clear();
                            }
                            return hashMap;
                        } catch (NullPointerException e11) {
                            e2 = e11;
                            Slog.w(TAG, "Failed parsing " + e2);
                            if (!z) {
                                hashMap.clear();
                            }
                            return hashMap;
                        } catch (XmlPullParserException e12) {
                            e = e12;
                            Slog.w(TAG, "Failed parsing " + e);
                        }
                    }
                } finally {
                }
            } catch (Throwable th2) {
                z = false;
                th = th2;
                if (!z) {
                    hashMap.clear();
                }
                throw th;
            }
        }
        return hashMap;
    }

    private static void writeTimestamps(HashMap<String, Long> hashMap) {
        AtomicFile atomicFile = sFile;
        synchronized (atomicFile) {
            try {
                try {
                    FileOutputStream startWrite = atomicFile.startWrite();
                    try {
                        TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                        resolveSerializer.startDocument((String) null, Boolean.TRUE);
                        resolveSerializer.startTag((String) null, "log-files");
                        for (String str : hashMap.keySet()) {
                            resolveSerializer.startTag((String) null, "log");
                            resolveSerializer.attribute((String) null, "filename", str);
                            resolveSerializer.attributeLong((String) null, "timestamp", hashMap.get(str).longValue());
                            resolveSerializer.endTag((String) null, "log");
                        }
                        resolveSerializer.endTag((String) null, "log-files");
                        resolveSerializer.endDocument();
                        sFile.finishWrite(startWrite);
                    } catch (IOException e) {
                        Slog.w(TAG, "Failed to write timestamp file, using the backup: " + e);
                        sFile.failWrite(startWrite);
                    }
                } catch (IOException e2) {
                    Slog.w(TAG, "Failed to write timestamp file: " + e2);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static BootReceiverWrapper getWrapper() {
        return mBootReceiverWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class BootReceiverWrapper implements IBootReceiverWrapper {
        @Override // com.android.server.IBootReceiverWrapper
        public void addFileToDropBox(DropBoxManager dropBoxManager, HashMap<String, Long> hashMap, String str, String str2, int i, String str3) {
            try {
                BootReceiver.addFileToDropBox(dropBoxManager, hashMap, str, str2, i, str3);
            } catch (IOException e) {
                Slog.w(BootReceiver.TAG, "Failed to add file to dropBox: " + e);
            }
        }
    }
}
