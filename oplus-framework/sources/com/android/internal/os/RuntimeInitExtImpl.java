package com.android.internal.os;

import android.app.ActivityThread;
import android.app.ApplicationErrorReport;
import android.os.Build;
import android.os.DeadObjectException;
import android.os.Debug;
import android.os.FileUtils;
import android.os.SystemProperties;
import android.system.Os;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.lang.Thread;

/* loaded from: classes.dex */
public class RuntimeInitExtImpl implements IRuntimeInitExt {
    private static final int HPROF_COUNT = 4;
    private static final String OOM_EXCEPTION = "java.lang.OutOfMemoryError";
    private static final String[] PRELOAD_CLASSES = {"system.ext.registry.PreloadRegistry", "system.ext.registry.BaseCommonCoreRegistry", "system.ext.registry.SocCommonCoreRegistry"};
    static final String TAG = "AndroidRuntime";

    public void uncaughtExceptionExt(Thread t, Throwable e, Thread.UncaughtExceptionHandler eh) {
        if ("main".equals(t.getName()) && t == ActivityThread.currentActivityThread().getLooper().getThread()) {
            try {
                t.getUncaughtExceptionHandler().uncaughtException(t, e);
            } catch (Throwable t2) {
                if (!(t2 instanceof DeadObjectException)) {
                    try {
                        Log.printlns(4, 6, TAG, "Error reporting crash", t2);
                    } catch (Throwable th) {
                    }
                }
            }
            t.setUncaughtExceptionHandler(eh);
        }
    }

    public void collectHeapdump(Throwable e) {
        boolean isLogkitOpen = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        boolean isLogAlwaysOn = SystemProperties.getBoolean("persist.sys.alwayson.enable", false);
        if (!Build.IS_DEBUGGABLE && !isLogkitOpen && !isLogAlwaysOn) {
            return;
        }
        ApplicationErrorReport.CrashInfo crashInfo = new ApplicationErrorReport.CrashInfo(e);
        if (OOM_EXCEPTION.equals(crashInfo.exceptionClassName)) {
            String fileName = Os.getpid() + ".hprof";
            try {
                File dumpDir = new File("/data/persist_log/hprofdump/");
                if (!dumpDir.exists()) {
                    dumpDir.mkdirs();
                }
                String logpath = "/data/persist_log/hprofdump/" + fileName;
                Debug.dumpHprofData(logpath);
                FileUtils.deleteOlderFiles(dumpDir, 4, 0L);
            } catch (IOException err) {
                Log.e(TAG, err.getMessage());
            }
            Log.printlns(4, 6, TAG, "OutOfMemoryError IN SYSTEM PROCESS: Already dump hprof!", e);
        }
    }

    public static void preload(ClassLoader preloadClassLoader) {
        if (preloadClassLoader == null) {
            preloadClassLoader = RuntimeInitExtImpl.class.getClassLoader();
        }
        for (String preloadClass : PRELOAD_CLASSES) {
            try {
                Class.forName(preloadClass, true, preloadClassLoader);
            } catch (ClassNotFoundException e) {
                Log.w(TAG, "Class not found for extloader");
            } catch (UnsatisfiedLinkError e2) {
                Log.w(TAG, "Problem preloading " + preloadClass + ": " + e2);
            } catch (Throwable t) {
                Log.e(TAG, "Error preloading " + preloadClass + ".", t);
                if (t instanceof Error) {
                    throw ((Error) t);
                }
                if (t instanceof RuntimeException) {
                    throw ((RuntimeException) t);
                }
                throw new RuntimeException(t);
            }
        }
    }
}
