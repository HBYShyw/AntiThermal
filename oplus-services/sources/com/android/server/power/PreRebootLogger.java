package com.android.server.power;

import android.content.Context;
import android.os.Environment;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class PreRebootLogger {
    private static final String PREREBOOT_DIR = "prereboot";
    private static final String TAG = "PreRebootLogger";
    private static final String[] BUFFERS_TO_DUMP = {"system"};
    private static final String[] SERVICES_TO_DUMP = {"rollback", "package"};
    private static final Object sLock = new Object();
    private static final long MAX_DUMP_TIME = TimeUnit.SECONDS.toMillis(20);

    PreRebootLogger() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void log(Context context) {
        log(context, getDumpDir());
    }

    @VisibleForTesting
    static void log(Context context, File file) {
        if (needDump(context)) {
            return;
        }
        wipe(file);
    }

    private static boolean needDump(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "adb_enabled", 0) == 1 && !context.getPackageManager().getPackageInstaller().getActiveStagedSessions().isEmpty();
    }

    @VisibleForTesting
    static void dump(final File file, long j) {
        Slog.d(TAG, "Dumping pre-reboot information...");
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Thread thread = new Thread(new Runnable() { // from class: com.android.server.power.PreRebootLogger$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PreRebootLogger.lambda$dump$0(file, atomicBoolean);
            }
        });
        thread.start();
        try {
            thread.join(j);
        } catch (InterruptedException e) {
            Slog.e(TAG, "Failed to dump pre-reboot information due to interrupted", e);
        }
        if (atomicBoolean.get()) {
            return;
        }
        Slog.w(TAG, "Failed to dump pre-reboot information due to timeout");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$0(File file, AtomicBoolean atomicBoolean) {
        synchronized (sLock) {
            for (String str : BUFFERS_TO_DUMP) {
                dumpLogsLocked(file, str);
            }
            for (String str2 : SERVICES_TO_DUMP) {
                dumpServiceLocked(file, str2);
            }
        }
        atomicBoolean.set(true);
    }

    private static void wipe(File file) {
        Slog.d(TAG, "Wiping pre-reboot information...");
        synchronized (sLock) {
            for (File file2 : file.listFiles()) {
                file2.delete();
            }
        }
    }

    private static File getDumpDir() {
        File file = new File(Environment.getDataMiscDirectory(), PREREBOOT_DIR);
        if (file.exists() && file.isDirectory()) {
            return file;
        }
        throw new UnsupportedOperationException("Pre-reboot dump directory not found");
    }

    @GuardedBy({"sLock"})
    private static void dumpLogsLocked(File file, String str) {
        try {
            File file2 = new File(file, str);
            if (file2.createNewFile()) {
                file2.setWritable(true, true);
            } else {
                new FileWriter(file2, false).flush();
            }
            Runtime.getRuntime().exec(new String[]{"logcat", "-d", "-b", str, "-f", file2.getAbsolutePath()}).waitFor();
        } catch (IOException | InterruptedException e) {
            Slog.e(TAG, "Failed to dump system log buffer before reboot", e);
        }
    }

    @GuardedBy({"sLock"})
    private static void dumpServiceLocked(File file, String str) {
        IBinder checkService = ServiceManager.checkService(str);
        if (checkService == null) {
            return;
        }
        try {
            checkService.dump(ParcelFileDescriptor.open(new File(file, str), 738197504).getFileDescriptor(), (String[]) ArrayUtils.emptyArray(String.class));
        } catch (RemoteException | FileNotFoundException e) {
            Slog.e(TAG, String.format("Failed to dump %s service before reboot", str), e);
        }
    }
}
