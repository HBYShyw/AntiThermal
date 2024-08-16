package com.android.server.am.trace;

import android.os.Process;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Slog;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SmartTraceUtils {
    public static final int DUMP_MAX_COUNT = 10;
    private static final String PROP_DUMP_CMD = "sys.smtrace.cmd";
    public static final String PROP_DUMP_CMDLINES = "persist.sys.smtrace.dump.cmdlines.extra";
    public static final String PROP_ENABLE_DUMP_PREDEFINED_PIDS = "persist.sys.smtrace.dump.predefined_pids.enable";
    public static final String PROP_ENABLE_ON_BG_APP = "persist.sys.smtrace.bgapp.enable";
    public static final String PROP_ENABLE_PERFETTO_DUMP = "persist.sys.perfetto_dump.enable";
    public static final String PROP_ENABLE_PERFETTO_ON_BG_APP = "persist.sys.perfetto.bgapp.enable";
    public static final String PROP_ENABLE_RECURSIVE_MODE = "persist.sys.smtrace.recursivemode.enable";
    public static final String PROP_ENABLE_SMART_TRACE = "persist.sys.smtrace.enable";
    private static final String PROP_PERFETTO_COMMAND = "sys.perfetto.cmd";
    private static final String PROP_PERFETTO_MAX_TRACE_COUNT = "persist.sys.perfetto.max_trace_count";
    private static final String TAG = "SmartTraceUtils";
    private static final String TRACE_DIRECTORY = "/data/misc/perfetto-traces/";

    public static boolean isSmartTraceEnabled() {
        return SystemProperties.getBoolean(PROP_ENABLE_SMART_TRACE, false);
    }

    public static boolean isSmartTraceEnabledOnBgApp() {
        return SystemProperties.getBoolean(PROP_ENABLE_ON_BG_APP, true);
    }

    public static boolean isDumpPredefinedPidsEnabled() {
        return SystemProperties.getBoolean(PROP_ENABLE_DUMP_PREDEFINED_PIDS, false);
    }

    public static boolean isPerfettoDumpEnabled() {
        return SystemProperties.getBoolean(PROP_ENABLE_PERFETTO_DUMP, false);
    }

    public static boolean isPerfettoDumpEnabledOnBgApp() {
        return SystemProperties.getBoolean(PROP_ENABLE_PERFETTO_ON_BG_APP, true);
    }

    public static boolean isRecursiveModeEnabled() {
        return SystemProperties.getBoolean(PROP_ENABLE_RECURSIVE_MODE, true);
    }

    public static void dumpStackTraces(int i, ArrayList<Integer> arrayList, ArrayList<Integer> arrayList2, File file) {
        if (isSmartTraceEnabled()) {
            if (isDumpingOn()) {
                Slog.e(TAG, "Attempting to run smart trace dump but trace is already in progress, skip it");
                return;
            }
            Set<Integer> targetPidsStuckInBinder = getTargetPidsStuckInBinder(i, arrayList, arrayList2, file);
            if (targetPidsStuckInBinder == null || targetPidsStuckInBinder.size() == 0) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(i);
            targetPidsStuckInBinder.forEach(new Consumer() { // from class: com.android.server.am.trace.SmartTraceUtils$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    SmartTraceUtils.lambda$dumpStackTraces$0(sb, (Integer) obj);
                }
            });
            sb.append(":" + file.getPath());
            SystemProperties.set(PROP_DUMP_CMD, sb.toString());
            Slog.i(TAG, "Start collect stack trace for " + sb.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpStackTraces$0(StringBuilder sb, Integer num) {
        sb.append(",");
        sb.append(num);
    }

    public static boolean traceStart() {
        if (isTracingOn()) {
            Slog.e(TAG, "Attempting to start perfetto trace but trace is already in progress, skip it");
            return false;
        }
        Slog.i(TAG, "Perfetto trace start..");
        SystemProperties.set(PROP_PERFETTO_COMMAND, "START");
        return true;
    }

    private static boolean isDumpingOn() {
        return !TextUtils.isEmpty(SystemProperties.get(PROP_DUMP_CMD, ""));
    }

    private static boolean isTracingOn() {
        return !TextUtils.isEmpty(SystemProperties.get(PROP_PERFETTO_COMMAND, ""));
    }

    private static Set<Integer> getTargetPidsStuckInBinder(int i, ArrayList<Integer> arrayList, ArrayList<Integer> arrayList2, File file) {
        BinderTransactions binderTransactions = new BinderTransactions(isRecursiveModeEnabled());
        binderTransactions.binderStateRead(file);
        Set<Integer> targetPidsStuckInBinder = binderTransactions.getTargetPidsStuckInBinder(i);
        targetPidsStuckInBinder.removeAll(arrayList);
        if (arrayList2 != null) {
            targetPidsStuckInBinder.removeAll(arrayList2);
        }
        int[] readExtraCmdlinesFromProperty = readExtraCmdlinesFromProperty();
        if (readExtraCmdlinesFromProperty != null) {
            for (int i2 : readExtraCmdlinesFromProperty) {
                targetPidsStuckInBinder.add(Integer.valueOf(i2));
            }
        }
        return targetPidsStuckInBinder;
    }

    private static int[] readExtraCmdlinesFromProperty() {
        String str = SystemProperties.get(PROP_DUMP_CMDLINES, "");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return Process.getPidsForCommands(str.split(","));
        } catch (NullPointerException e) {
            Slog.e(TAG, "Exception get pid for commonds " + str, e);
            return null;
        } catch (OutOfMemoryError e2) {
            Slog.e(TAG, "Out of Memory when get pid for commonds " + str, e2);
            return null;
        }
    }
}
