package com.android.server;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Looper;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.ShellCommand;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.KeyValueListParser;
import android.util.Slog;
import com.android.internal.os.AppIdToPackageMap;
import com.android.internal.os.BackgroundThread;
import com.android.internal.os.CachedDeviceState;
import com.android.internal.os.LooperStats;
import com.android.internal.util.DumpUtils;
import com.android.server.bluetooth.IOplusBluetoothManagerServiceExt;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class LooperStatsService extends Binder {
    private static final String DEBUG_SYS_LOOPER_STATS_ENABLED = "debug.sys.looper_stats_enabled";
    private static final boolean DEFAULT_ENABLED = true;
    private static final int DEFAULT_ENTRIES_SIZE_CAP = 1500;
    private static final int DEFAULT_SAMPLING_INTERVAL = 1000;
    private static final boolean DEFAULT_TRACK_SCREEN_INTERACTIVE = false;
    private static final String LOOPER_STATS_SERVICE_NAME = "looper_stats";
    private static final String SETTINGS_ENABLED_KEY = "enabled";
    private static final String SETTINGS_IGNORE_BATTERY_STATUS_KEY = "ignore_battery_status";
    private static final String SETTINGS_SAMPLING_INTERVAL_KEY = "sampling_interval";
    private static final String SETTINGS_TRACK_SCREEN_INTERACTIVE_KEY = "track_screen_state";
    private static final String TAG = "LooperStatsService";
    private final Context mContext;
    private boolean mEnabled;
    private boolean mIgnoreBatteryStatus;
    private final LooperStats mStats;
    private boolean mTrackScreenInteractive;

    private LooperStatsService(Context context, LooperStats looperStats) {
        this.mEnabled = false;
        this.mTrackScreenInteractive = false;
        this.mIgnoreBatteryStatus = false;
        this.mContext = context;
        this.mStats = looperStats;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initFromSettings() {
        KeyValueListParser keyValueListParser = new KeyValueListParser(',');
        try {
            keyValueListParser.setString(Settings.Global.getString(this.mContext.getContentResolver(), LOOPER_STATS_SERVICE_NAME));
        } catch (IllegalArgumentException e) {
            Slog.e(TAG, "Bad looper_stats settings", e);
        }
        setSamplingInterval(keyValueListParser.getInt(SETTINGS_SAMPLING_INTERVAL_KEY, 1000));
        setTrackScreenInteractive(keyValueListParser.getBoolean(SETTINGS_TRACK_SCREEN_INTERACTIVE_KEY, false));
        setIgnoreBatteryStatus(keyValueListParser.getBoolean(SETTINGS_IGNORE_BATTERY_STATUS_KEY, false));
        setEnabled(SystemProperties.getBoolean(DEBUG_SYS_LOOPER_STATS_ENABLED, keyValueListParser.getBoolean(SETTINGS_ENABLED_KEY, true)));
    }

    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
        new LooperShellCommand().exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
    }

    @Override // android.os.Binder
    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            AppIdToPackageMap snapshot = AppIdToPackageMap.getSnapshot();
            printWriter.print("Start time: ");
            printWriter.println(DateFormat.format("yyyy-MM-dd HH:mm:ss", this.mStats.getStartTimeMillis()));
            printWriter.print("On battery time (ms): ");
            printWriter.println(this.mStats.getBatteryTimeMillis());
            List entries = this.mStats.getEntries();
            entries.sort(Comparator.comparing(new Function() { // from class: com.android.server.LooperStatsService$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    Integer lambda$dump$0;
                    lambda$dump$0 = LooperStatsService.lambda$dump$0((LooperStats.ExportedEntry) obj);
                    return lambda$dump$0;
                }
            }).thenComparing(new Function() { // from class: com.android.server.LooperStatsService$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    String str;
                    str = ((LooperStats.ExportedEntry) obj).threadName;
                    return str;
                }
            }).thenComparing(new Function() { // from class: com.android.server.LooperStatsService$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    String str;
                    str = ((LooperStats.ExportedEntry) obj).handlerClassName;
                    return str;
                }
            }).thenComparing(new Function() { // from class: com.android.server.LooperStatsService$$ExternalSyntheticLambda3
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    String str;
                    str = ((LooperStats.ExportedEntry) obj).messageName;
                    return str;
                }
            }));
            printWriter.println(String.join(",", Arrays.asList("work_source_uid", "thread_name", "handler_class", "message_name", "is_interactive", "message_count", "recorded_message_count", "total_latency_micros", "max_latency_micros", "total_cpu_micros", "max_cpu_micros", "recorded_delay_message_count", "total_delay_millis", "max_delay_millis", "exception_count")));
            Iterator it = entries.iterator();
            while (it.hasNext()) {
                LooperStats.ExportedEntry exportedEntry = (LooperStats.ExportedEntry) it.next();
                if (!exportedEntry.messageName.startsWith("__DEBUG_")) {
                    printWriter.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", snapshot.mapUid(exportedEntry.workSourceUid), exportedEntry.threadName, exportedEntry.handlerClassName, exportedEntry.messageName, Boolean.valueOf(exportedEntry.isInteractive), Long.valueOf(exportedEntry.messageCount), Long.valueOf(exportedEntry.recordedMessageCount), Long.valueOf(exportedEntry.totalLatencyMicros), Long.valueOf(exportedEntry.maxLatencyMicros), Long.valueOf(exportedEntry.cpuUsageMicros), Long.valueOf(exportedEntry.maxCpuUsageMicros), Long.valueOf(exportedEntry.recordedDelayMessageCount), Long.valueOf(exportedEntry.delayMillis), Long.valueOf(exportedEntry.maxDelayMillis), Long.valueOf(exportedEntry.exceptionCount));
                    it = it;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$dump$0(LooperStats.ExportedEntry exportedEntry) {
        return Integer.valueOf(exportedEntry.workSourceUid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnabled(boolean z) {
        if (this.mEnabled != z) {
            this.mEnabled = z;
            this.mStats.reset();
            this.mStats.setAddDebugEntries(z);
            Looper.setObserver(z ? this.mStats : null);
        }
    }

    private void setTrackScreenInteractive(boolean z) {
        if (this.mTrackScreenInteractive != z) {
            this.mTrackScreenInteractive = z;
            this.mStats.reset();
        }
    }

    private void setIgnoreBatteryStatus(boolean z) {
        if (this.mIgnoreBatteryStatus != z) {
            this.mStats.setIgnoreBatteryStatus(z);
            this.mIgnoreBatteryStatus = z;
            this.mStats.reset();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSamplingInterval(int i) {
        if (i > 0) {
            this.mStats.setSamplingInterval(i);
            return;
        }
        Slog.w(TAG, "Ignored invalid sampling interval (value must be positive): " + i);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Lifecycle extends SystemService {
        private final LooperStatsService mService;
        private final SettingsObserver mSettingsObserver;
        private final LooperStats mStats;

        public Lifecycle(Context context) {
            super(context);
            LooperStats looperStats = new LooperStats(1000, 1500);
            this.mStats = looperStats;
            LooperStatsService looperStatsService = new LooperStatsService(getContext(), looperStats);
            this.mService = looperStatsService;
            this.mSettingsObserver = new SettingsObserver(looperStatsService);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            publishLocalService(LooperStats.class, this.mStats);
            publishBinderService(LooperStatsService.LOOPER_STATS_SERVICE_NAME, this.mService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int i) {
            if (500 == i) {
                this.mService.initFromSettings();
                getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor(LooperStatsService.LOOPER_STATS_SERVICE_NAME), false, this.mSettingsObserver, 0);
                this.mStats.setDeviceState((CachedDeviceState.Readonly) getLocalService(CachedDeviceState.Readonly.class));
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class SettingsObserver extends ContentObserver {
        private final LooperStatsService mService;

        SettingsObserver(LooperStatsService looperStatsService) {
            super(BackgroundThread.getHandler());
            this.mService = looperStatsService;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri, int i) {
            this.mService.initFromSettings();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class LooperShellCommand extends ShellCommand {
        private LooperShellCommand() {
        }

        public int onCommand(String str) {
            if (IOplusBluetoothManagerServiceExt.FLAG_ENABLE.equals(str)) {
                LooperStatsService.this.setEnabled(true);
                return 0;
            }
            if ("disable".equals(str)) {
                LooperStatsService.this.setEnabled(false);
                return 0;
            }
            if ("reset".equals(str)) {
                LooperStatsService.this.mStats.reset();
                return 0;
            }
            if (LooperStatsService.SETTINGS_SAMPLING_INTERVAL_KEY.equals(str)) {
                LooperStatsService.this.setSamplingInterval(Integer.parseUnsignedInt(getNextArgRequired()));
                return 0;
            }
            return handleDefaultCommands(str);
        }

        public void onHelp() {
            PrintWriter outPrintWriter = getOutPrintWriter();
            outPrintWriter.println("looper_stats commands:");
            outPrintWriter.println("  enable: Enable collecting stats.");
            outPrintWriter.println("  disable: Disable collecting stats.");
            outPrintWriter.println("  sampling_interval: Change the sampling interval.");
            outPrintWriter.println("  reset: Reset stats.");
        }
    }
}
