package com.oplus.oms.split.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class ProcessUtil {
    private static final String TAG = "Split:ProcessUtil";
    private static String sCurrentProcess;

    private ProcessUtil() {
    }

    public static String getProcessName(Context context) {
        if (!TextUtils.isEmpty(sCurrentProcess)) {
            return sCurrentProcess;
        }
        try {
            sCurrentProcess = getProcessNameByApplication();
        } catch (RuntimeException e) {
            SplitLog.w(TAG, "getProcessNameClassical Application failed " + e.getMessage(), new Object[0]);
        }
        if (!TextUtils.isEmpty(sCurrentProcess)) {
            return sCurrentProcess;
        }
        try {
            sCurrentProcess = getProcessNameClassical(context);
        } catch (Exception exception) {
            SplitLog.w(TAG, "getProcessNameClassical Classical failed " + exception.getMessage(), new Object[0]);
        }
        if (!TextUtils.isEmpty(sCurrentProcess)) {
            return sCurrentProcess;
        }
        sCurrentProcess = getProcessNameSecure();
        SplitLog.d(TAG, "Get process name: sCurrentProcess" + sCurrentProcess + " in secure mode.", new Object[0]);
        return sCurrentProcess;
    }

    private static String getProcessNameSecure() {
        String processName = "";
        BufferedReader reader = null;
        try {
            try {
                File file = new File("/proc/" + Process.myPid() + "/cmdline");
                reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath(), new OpenOption[0]), StandardCharsets.UTF_8));
                processName = reader.readLine();
                if (processName != null) {
                    processName = processName.trim();
                }
            } catch (IOException e) {
                SplitLog.e(TAG, "getProcessNameSecure failed " + e.getMessage(), new Object[0]);
            }
            return processName;
        } finally {
            FileUtil.closeQuietly(reader);
        }
    }

    public static String getProcessNameByApplication() {
        return Application.getProcessName();
    }

    private static String getProcessNameClassical(Context context) {
        List<ActivityManager.RunningAppProcessInfo> appProcessList;
        String processName = "";
        if (context == null) {
            return "";
        }
        int pid = Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService("activity");
        if (manager == null || (appProcessList = manager.getRunningAppProcesses()) == null) {
            return "";
        }
        for (ActivityManager.RunningAppProcessInfo process : appProcessList) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    public static boolean isProcessAlive(Context context, String processName) {
        if (context == null) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> running = am.getRunningAppProcesses();
        if (running == null || running.isEmpty()) {
            SplitLog.d(TAG, "isProcessAlive get null running process. processName: " + processName, new Object[0]);
            return false;
        }
        boolean alive = false;
        Iterator<ActivityManager.RunningAppProcessInfo> it = running.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ActivityManager.RunningAppProcessInfo pi = it.next();
            if (TextUtils.equals(pi.processName, processName)) {
                alive = true;
                break;
            }
        }
        SplitLog.d(TAG, "isProcessAlive processName: " + processName + ", alive: " + alive, new Object[0]);
        return alive;
    }

    public static void killProcess(Context context, Set<String> processNames) {
        if (context == null) {
            return;
        }
        String allProcess = TextUtils.join(",", processNames);
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> runningProcess = am.getRunningAppProcesses();
        if (runningProcess == null || runningProcess.isEmpty()) {
            SplitLog.w(TAG, "killProcess get empty getRunningAppProcesses: %s", allProcess);
            return;
        }
        for (ActivityManager.RunningAppProcessInfo rai : runningProcess) {
            for (String process : processNames) {
                if (TextUtils.equals(process, rai.processName)) {
                    Process.killProcess(rai.pid);
                    SplitLog.d(TAG, "kill process %s:%d", rai.processName, Integer.valueOf(rai.pid));
                }
            }
        }
    }

    public static void killProcess(Context context, String processName) {
        if (context == null) {
            return;
        }
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> runningProcess = am.getRunningAppProcesses();
        if (runningProcess == null || runningProcess.isEmpty()) {
            SplitLog.w(TAG, "killProcess get empty getRunningAppProcesses: %s", processName);
            return;
        }
        for (ActivityManager.RunningAppProcessInfo rai : runningProcess) {
            if (TextUtils.equals(processName, rai.processName)) {
                Process.killProcess(rai.pid);
                SplitLog.d(TAG, "kill process %s:%d", rai.processName, Integer.valueOf(rai.pid));
            }
        }
    }

    public static boolean isMainProcess(Context context) {
        if (context == null) {
            return false;
        }
        return TextUtils.equals(context.getPackageName(), getProcessName(context));
    }
}
