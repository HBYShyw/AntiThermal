package com.android.server.power;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.os.Process;
import android.os.RemoteException;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.pm.PackageManagerService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ShutdownCheckPoints {
    private static final int MAX_CHECK_POINTS = 100;
    private static final int MAX_DUMP_FILES = 20;
    private static final String TAG = "ShutdownCheckPoints";
    private final ArrayList<CheckPoint> mCheckPoints;
    private final Injector mInjector;
    private static final ShutdownCheckPoints INSTANCE = new ShutdownCheckPoints();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
    private static final File[] EMPTY_FILE_ARRAY = new File[0];

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Injector {
        IActivityManager activityManager();

        long currentTimeMillis();

        int maxCheckPoints();

        int maxDumpFiles();
    }

    private ShutdownCheckPoints() {
        this(new Injector() { // from class: com.android.server.power.ShutdownCheckPoints.1
            @Override // com.android.server.power.ShutdownCheckPoints.Injector
            public int maxCheckPoints() {
                return 100;
            }

            @Override // com.android.server.power.ShutdownCheckPoints.Injector
            public int maxDumpFiles() {
                return 20;
            }

            @Override // com.android.server.power.ShutdownCheckPoints.Injector
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }

            @Override // com.android.server.power.ShutdownCheckPoints.Injector
            public IActivityManager activityManager() {
                return ActivityManager.getService();
            }
        });
    }

    @VisibleForTesting
    ShutdownCheckPoints(Injector injector) {
        this.mCheckPoints = new ArrayList<>();
        this.mInjector = injector;
    }

    public static void recordCheckPoint(String str) {
        INSTANCE.recordCheckPointInternal(str);
    }

    public static void recordCheckPoint(int i, String str) {
        INSTANCE.recordCheckPointInternal(i, str);
    }

    public static void recordCheckPoint(String str, String str2, String str3) {
        INSTANCE.recordCheckPointInternal(str, str2, str3);
    }

    public static void dump(PrintWriter printWriter) {
        INSTANCE.dumpInternal(printWriter);
    }

    public static Thread newDumpThread(File file) {
        return INSTANCE.newDumpThreadInternal(file);
    }

    @VisibleForTesting
    void recordCheckPointInternal(String str) {
        recordCheckPointInternal(new SystemServerCheckPoint(this.mInjector, str));
        Slog.v(TAG, "System server shutdown checkpoint recorded");
    }

    @VisibleForTesting
    void recordCheckPointInternal(int i, String str) {
        CheckPoint binderCheckPoint;
        if (i == Process.myPid()) {
            binderCheckPoint = new SystemServerCheckPoint(this.mInjector, str);
        } else {
            binderCheckPoint = new BinderCheckPoint(this.mInjector, i, str);
        }
        recordCheckPointInternal(binderCheckPoint);
        Slog.v(TAG, "Binder shutdown checkpoint recorded with pid=" + i);
    }

    @VisibleForTesting
    void recordCheckPointInternal(String str, String str2, String str3) {
        CheckPoint intentCheckPoint;
        if (PackageManagerService.PLATFORM_PACKAGE_NAME.equals(str2)) {
            intentCheckPoint = new SystemServerCheckPoint(this.mInjector, str3);
        } else {
            intentCheckPoint = new IntentCheckPoint(this.mInjector, str, str2, str3);
        }
        recordCheckPointInternal(intentCheckPoint);
        Slog.v(TAG, String.format("Shutdown intent checkpoint recorded intent=%s from package=%s", str, str2));
    }

    private void recordCheckPointInternal(CheckPoint checkPoint) {
        synchronized (this.mCheckPoints) {
            this.mCheckPoints.add(checkPoint);
            if (this.mCheckPoints.size() > this.mInjector.maxCheckPoints()) {
                this.mCheckPoints.remove(0);
            }
        }
    }

    @VisibleForTesting
    void dumpInternal(PrintWriter printWriter) {
        ArrayList arrayList;
        synchronized (this.mCheckPoints) {
            arrayList = new ArrayList(this.mCheckPoints);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((CheckPoint) it.next()).dump(printWriter);
            printWriter.println();
        }
    }

    @VisibleForTesting
    Thread newDumpThreadInternal(File file) {
        return new FileDumperThread(this, file, this.mInjector.maxDumpFiles());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class CheckPoint {
        private final String mReason;
        private final long mTimestamp;

        abstract void dumpDetails(PrintWriter printWriter);

        abstract String getOrigin();

        CheckPoint(Injector injector, String str) {
            this.mTimestamp = injector.currentTimeMillis();
            this.mReason = str;
        }

        final void dump(PrintWriter printWriter) {
            printWriter.print("Shutdown request from ");
            printWriter.print(getOrigin());
            if (this.mReason != null) {
                printWriter.print(" for reason ");
                printWriter.print(this.mReason);
            }
            printWriter.print(" at ");
            printWriter.print(ShutdownCheckPoints.DATE_FORMAT.format(new Date(this.mTimestamp)));
            printWriter.println(" (epoch=" + this.mTimestamp + ")");
            dumpDetails(printWriter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class SystemServerCheckPoint extends CheckPoint {
        private final StackTraceElement[] mStackTraceElements;

        @Override // com.android.server.power.ShutdownCheckPoints.CheckPoint
        String getOrigin() {
            return "SYSTEM";
        }

        SystemServerCheckPoint(Injector injector, String str) {
            super(injector, str);
            this.mStackTraceElements = Thread.currentThread().getStackTrace();
        }

        @Override // com.android.server.power.ShutdownCheckPoints.CheckPoint
        void dumpDetails(PrintWriter printWriter) {
            String methodName = getMethodName();
            if (methodName == null) {
                methodName = "Failed to get method name";
            }
            printWriter.println(methodName);
            printStackTrace(printWriter);
        }

        String getMethodName() {
            int findCallSiteIndex = findCallSiteIndex();
            StackTraceElement[] stackTraceElementArr = this.mStackTraceElements;
            if (findCallSiteIndex >= stackTraceElementArr.length) {
                return null;
            }
            StackTraceElement stackTraceElement = stackTraceElementArr[findCallSiteIndex];
            return String.format("%s.%s", stackTraceElement.getClassName(), stackTraceElement.getMethodName());
        }

        void printStackTrace(PrintWriter printWriter) {
            int findCallSiteIndex = findCallSiteIndex();
            while (true) {
                findCallSiteIndex++;
                if (findCallSiteIndex >= this.mStackTraceElements.length) {
                    return;
                }
                printWriter.print(" at ");
                printWriter.println(this.mStackTraceElements[findCallSiteIndex]);
            }
        }

        private int findCallSiteIndex() {
            String canonicalName = ShutdownCheckPoints.class.getCanonicalName();
            int i = 0;
            while (true) {
                StackTraceElement[] stackTraceElementArr = this.mStackTraceElements;
                if (i >= stackTraceElementArr.length || stackTraceElementArr[i].getClassName().equals(canonicalName)) {
                    break;
                }
                i++;
            }
            while (true) {
                StackTraceElement[] stackTraceElementArr2 = this.mStackTraceElements;
                if (i >= stackTraceElementArr2.length || !stackTraceElementArr2[i].getClassName().equals(canonicalName)) {
                    break;
                }
                i++;
            }
            return i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class BinderCheckPoint extends SystemServerCheckPoint {
        private final IActivityManager mActivityManager;
        private final int mCallerProcessId;

        @Override // com.android.server.power.ShutdownCheckPoints.SystemServerCheckPoint, com.android.server.power.ShutdownCheckPoints.CheckPoint
        String getOrigin() {
            return "BINDER";
        }

        BinderCheckPoint(Injector injector, int i, String str) {
            super(injector, str);
            this.mCallerProcessId = i;
            this.mActivityManager = injector.activityManager();
        }

        @Override // com.android.server.power.ShutdownCheckPoints.SystemServerCheckPoint, com.android.server.power.ShutdownCheckPoints.CheckPoint
        void dumpDetails(PrintWriter printWriter) {
            String methodName = getMethodName();
            if (methodName == null) {
                methodName = "Failed to get method name";
            }
            printWriter.println(methodName);
            String processName = getProcessName();
            printWriter.print("From process ");
            if (processName == null) {
                processName = "?";
            }
            printWriter.print(processName);
            printWriter.println(" (pid=" + this.mCallerProcessId + ")");
        }

        String getProcessName() {
            List<ActivityManager.RunningAppProcessInfo> list;
            try {
                IActivityManager iActivityManager = this.mActivityManager;
                if (iActivityManager != null) {
                    list = iActivityManager.getRunningAppProcesses();
                } else {
                    Slog.v(ShutdownCheckPoints.TAG, "No ActivityManager available to find process name with pid=" + this.mCallerProcessId);
                    list = null;
                }
                if (list != null) {
                    for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
                        if (runningAppProcessInfo.pid == this.mCallerProcessId) {
                            return runningAppProcessInfo.processName;
                        }
                    }
                }
            } catch (RemoteException e) {
                Slog.e(ShutdownCheckPoints.TAG, "Failed to get running app processes from ActivityManager", e);
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class IntentCheckPoint extends CheckPoint {
        private final String mIntentName;
        private final String mPackageName;

        @Override // com.android.server.power.ShutdownCheckPoints.CheckPoint
        String getOrigin() {
            return "INTENT";
        }

        IntentCheckPoint(Injector injector, String str, String str2, String str3) {
            super(injector, str3);
            this.mIntentName = str;
            this.mPackageName = str2;
        }

        @Override // com.android.server.power.ShutdownCheckPoints.CheckPoint
        void dumpDetails(PrintWriter printWriter) {
            printWriter.print("Intent: ");
            printWriter.println(this.mIntentName);
            printWriter.print("Package: ");
            printWriter.println(this.mPackageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class FileDumperThread extends Thread {
        private final File mBaseFile;
        private final int mFileCountLimit;
        private final ShutdownCheckPoints mInstance;

        FileDumperThread(ShutdownCheckPoints shutdownCheckPoints, File file, int i) {
            this.mInstance = shutdownCheckPoints;
            this.mBaseFile = file;
            this.mFileCountLimit = i;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            this.mBaseFile.getParentFile().mkdirs();
            File[] listCheckPointsFiles = listCheckPointsFiles();
            int length = (listCheckPointsFiles.length - this.mFileCountLimit) + 1;
            for (int i = 0; i < length; i++) {
                listCheckPointsFiles[i].delete();
            }
            writeCheckpoints(new File(String.format("%s-%d", this.mBaseFile.getAbsolutePath(), Long.valueOf(System.currentTimeMillis()))));
        }

        private File[] listCheckPointsFiles() {
            final String str = this.mBaseFile.getName() + "-";
            File[] listFiles = this.mBaseFile.getParentFile().listFiles(new FilenameFilter() { // from class: com.android.server.power.ShutdownCheckPoints.FileDumperThread.1
                @Override // java.io.FilenameFilter
                public boolean accept(File file, String str2) {
                    if (!str2.startsWith(str)) {
                        return false;
                    }
                    try {
                        Long.valueOf(str2.substring(str.length()));
                        return true;
                    } catch (NumberFormatException unused) {
                        return false;
                    }
                }
            });
            if (listFiles == null) {
                return ShutdownCheckPoints.EMPTY_FILE_ARRAY;
            }
            Arrays.sort(listFiles);
            return listFiles;
        }

        private void writeCheckpoints(File file) {
            FileOutputStream fileOutputStream;
            AtomicFile atomicFile = new AtomicFile(this.mBaseFile);
            try {
                fileOutputStream = atomicFile.startWrite();
                try {
                    PrintWriter printWriter = new PrintWriter(fileOutputStream);
                    this.mInstance.dumpInternal(printWriter);
                    printWriter.flush();
                    atomicFile.finishWrite(fileOutputStream);
                } catch (IOException e) {
                    e = e;
                    Log.e(ShutdownCheckPoints.TAG, "Failed to write shutdown checkpoints", e);
                    if (fileOutputStream != null) {
                        atomicFile.failWrite(fileOutputStream);
                    }
                    this.mBaseFile.renameTo(file);
                }
            } catch (IOException e2) {
                e = e2;
                fileOutputStream = null;
            }
            this.mBaseFile.renameTo(file);
        }
    }
}
