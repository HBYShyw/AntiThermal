package com.android.server;

import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.net.util.NetworkConstants;
import android.os.BatteryStatsInternal;
import android.os.Binder;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.ShellCommand;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.KeyValueListParser;
import android.util.Slog;
import com.android.internal.os.AppIdToPackageMap;
import com.android.internal.os.BackgroundThread;
import com.android.internal.os.BinderCallsStats;
import com.android.internal.os.BinderInternal;
import com.android.internal.os.CachedDeviceState;
import com.android.internal.util.DumpUtils;
import com.android.server.BinderCallsStatsService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BinderCallsStatsService extends Binder {
    private static final String PERSIST_SYS_BINDER_CALLS_DETAILED_TRACKING = "persist.sys.binder_calls_detailed_tracking";
    private static final String SERVICE_NAME = "binder_calls_stats";
    private static final String TAG = "BinderCallsStatsService";
    private final BinderCallsStats mBinderCallsStats;
    private SettingsObserver mSettingsObserver;
    private final AuthorizedWorkSourceProvider mWorkSourceProvider;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class AuthorizedWorkSourceProvider implements BinderInternal.WorkSourceProvider {
        private ArraySet<Integer> mAppIdTrustlist = new ArraySet<>();

        AuthorizedWorkSourceProvider() {
        }

        public int resolveWorkSourceUid(int i) {
            int callingUid = getCallingUid();
            if (this.mAppIdTrustlist.contains(Integer.valueOf(UserHandle.getAppId(callingUid)))) {
                return i != -1 ? i : callingUid;
            }
            return callingUid;
        }

        public void systemReady(Context context) {
            this.mAppIdTrustlist = createAppidTrustlist(context);
        }

        public void dump(PrintWriter printWriter, AppIdToPackageMap appIdToPackageMap) {
            printWriter.println("AppIds of apps that can set the work source:");
            Iterator<Integer> it = this.mAppIdTrustlist.iterator();
            while (it.hasNext()) {
                printWriter.println("\t- " + appIdToPackageMap.mapAppId(it.next().intValue()));
            }
        }

        protected int getCallingUid() {
            return Binder.getCallingUid();
        }

        private ArraySet<Integer> createAppidTrustlist(Context context) {
            ArraySet<Integer> arraySet = new ArraySet<>();
            arraySet.add(Integer.valueOf(UserHandle.getAppId(Process.myUid())));
            PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> packagesHoldingPermissions = packageManager.getPackagesHoldingPermissions(new String[]{"android.permission.UPDATE_DEVICE_STATS"}, 786432);
            int size = packagesHoldingPermissions.size();
            for (int i = 0; i < size; i++) {
                PackageInfo packageInfo = packagesHoldingPermissions.get(i);
                try {
                    arraySet.add(Integer.valueOf(UserHandle.getAppId(packageManager.getPackageUid(packageInfo.packageName, 786432))));
                } catch (PackageManager.NameNotFoundException e) {
                    Slog.e(BinderCallsStatsService.TAG, "Cannot find uid for package name " + packageInfo.packageName, e);
                }
            }
            return arraySet;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class SettingsObserver extends ContentObserver {
        private final BinderCallsStats mBinderCallsStats;
        private final Context mContext;
        private boolean mEnabled;
        private final KeyValueListParser mParser;
        private final Uri mUri;
        private final AuthorizedWorkSourceProvider mWorkSourceProvider;

        SettingsObserver(Context context, BinderCallsStats binderCallsStats, AuthorizedWorkSourceProvider authorizedWorkSourceProvider) {
            super(BackgroundThread.getHandler());
            Uri uriFor = Settings.Global.getUriFor(BinderCallsStatsService.SERVICE_NAME);
            this.mUri = uriFor;
            this.mParser = new KeyValueListParser(',');
            this.mContext = context;
            context.getContentResolver().registerContentObserver(uriFor, false, this, 0);
            this.mBinderCallsStats = binderCallsStats;
            this.mWorkSourceProvider = authorizedWorkSourceProvider;
            onChange();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri, int i) {
            if (this.mUri.equals(uri)) {
                onChange();
            }
        }

        public void onChange() {
            if (SystemProperties.get(BinderCallsStatsService.PERSIST_SYS_BINDER_CALLS_DETAILED_TRACKING).isEmpty()) {
                try {
                    this.mParser.setString(Settings.Global.getString(this.mContext.getContentResolver(), BinderCallsStatsService.SERVICE_NAME));
                } catch (IllegalArgumentException e) {
                    Slog.e(BinderCallsStatsService.TAG, "Bad binder call stats settings", e);
                }
                this.mBinderCallsStats.setDetailedTracking(this.mParser.getBoolean("detailed_tracking", true));
                this.mBinderCallsStats.setSamplingInterval(this.mParser.getInt("sampling_interval", 1000));
                this.mBinderCallsStats.setMaxBinderCallStats(this.mParser.getInt("max_call_stats_count", NetworkConstants.ETHER_MTU));
                this.mBinderCallsStats.setTrackScreenInteractive(this.mParser.getBoolean("track_screen_state", false));
                this.mBinderCallsStats.setTrackDirectCallerUid(this.mParser.getBoolean("track_calling_uid", true));
                this.mBinderCallsStats.setIgnoreBatteryStatus(this.mParser.getBoolean("ignore_battery_status", false));
                this.mBinderCallsStats.setShardingModulo(this.mParser.getInt("sharding_modulo", 1));
                this.mBinderCallsStats.setCollectLatencyData(this.mParser.getBoolean("collect_latency_data", true));
                BinderCallsStats.SettingsObserver.configureLatencyObserver(this.mParser, this.mBinderCallsStats.getLatencyObserver());
                boolean z = this.mParser.getBoolean("enabled", true);
                if (this.mEnabled != z) {
                    if (z) {
                        Binder.setObserver(this.mBinderCallsStats);
                        Binder.setProxyTransactListener(new Binder.PropagateWorkSourceTransactListener());
                        Binder.setWorkSourceProvider(this.mWorkSourceProvider);
                    } else {
                        Binder.setObserver(null);
                        Binder.setProxyTransactListener(null);
                        Binder.setWorkSourceProvider(new BinderInternal.WorkSourceProvider() { // from class: com.android.server.BinderCallsStatsService$SettingsObserver$$ExternalSyntheticLambda0
                            public final int resolveWorkSourceUid(int i) {
                                return BinderCallsStatsService.SettingsObserver.lambda$onChange$0(i);
                            }
                        });
                    }
                    this.mEnabled = z;
                    this.mBinderCallsStats.reset();
                    this.mBinderCallsStats.setAddDebugEntries(z);
                    this.mBinderCallsStats.getLatencyObserver().reset();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int lambda$onChange$0(int i) {
            return Binder.getCallingUid();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Internal {
        private final BinderCallsStats mBinderCallsStats;

        Internal(BinderCallsStats binderCallsStats) {
            this.mBinderCallsStats = binderCallsStats;
        }

        public void reset() {
            this.mBinderCallsStats.reset();
        }

        public ArrayList<BinderCallsStats.ExportedCallStat> getExportedCallStats() {
            return this.mBinderCallsStats.getExportedCallStats();
        }

        public ArrayMap<String, Integer> getExportedExceptionStats() {
            return this.mBinderCallsStats.getExportedExceptionStats();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class LifeCycle extends SystemService {
        private BinderCallsStats mBinderCallsStats;
        private BinderCallsStatsService mService;
        private AuthorizedWorkSourceProvider mWorkSourceProvider;

        public LifeCycle(Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mBinderCallsStats = new BinderCallsStats(new BinderCallsStats.Injector());
            this.mWorkSourceProvider = new AuthorizedWorkSourceProvider();
            this.mService = new BinderCallsStatsService(this.mBinderCallsStats, this.mWorkSourceProvider);
            publishLocalService(Internal.class, new Internal(this.mBinderCallsStats));
            publishBinderService(BinderCallsStatsService.SERVICE_NAME, this.mService);
            if (SystemProperties.getBoolean(BinderCallsStatsService.PERSIST_SYS_BINDER_CALLS_DETAILED_TRACKING, false)) {
                Slog.i(BinderCallsStatsService.TAG, "Enabled CPU usage tracking for binder calls. Controlled by persist.sys.binder_calls_detailed_tracking or via dumpsys binder_calls_stats --enable-detailed-tracking");
                this.mBinderCallsStats.setDetailedTracking(true);
            }
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int i) {
            if (500 == i) {
                this.mBinderCallsStats.setDeviceState((CachedDeviceState.Readonly) getLocalService(CachedDeviceState.Readonly.class));
                final BatteryStatsInternal batteryStatsInternal = (BatteryStatsInternal) getLocalService(BatteryStatsInternal.class);
                this.mBinderCallsStats.setCallStatsObserver(new BinderInternal.CallStatsObserver() { // from class: com.android.server.BinderCallsStatsService.LifeCycle.1
                    public void noteCallStats(int i2, long j, Collection<BinderCallsStats.CallStat> collection) {
                        batteryStatsInternal.noteBinderCallStats(i2, j, collection);
                    }

                    public void noteBinderThreadNativeIds(int[] iArr) {
                        batteryStatsInternal.noteBinderThreadNativeIds(iArr);
                    }
                });
                this.mWorkSourceProvider.systemReady(getContext());
                this.mService.systemReady(getContext());
            }
        }
    }

    BinderCallsStatsService(BinderCallsStats binderCallsStats, AuthorizedWorkSourceProvider authorizedWorkSourceProvider) {
        this.mBinderCallsStats = binderCallsStats;
        this.mWorkSourceProvider = authorizedWorkSourceProvider;
    }

    public void systemReady(Context context) {
        this.mSettingsObserver = new SettingsObserver(context, this.mBinderCallsStats, this.mWorkSourceProvider);
    }

    public void reset() {
        Slog.i(TAG, "Resetting stats");
        this.mBinderCallsStats.reset();
    }

    @Override // android.os.Binder
    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpAndUsageStatsPermission(ActivityThread.currentApplication(), SERVICE_NAME, printWriter)) {
            int i = -1;
            boolean z = false;
            int i2 = 0;
            if (strArr != null) {
                int i3 = -1;
                boolean z2 = false;
                while (i2 < strArr.length) {
                    String str = strArr[i2];
                    if ("-a".equals(str)) {
                        z2 = true;
                    } else {
                        if ("-h".equals(str)) {
                            printWriter.println("dumpsys binder_calls_stats options:");
                            printWriter.println("  -a: Verbose");
                            printWriter.println("  --work-source-uid <UID>: Dump binder calls from the UID");
                            return;
                        }
                        if ("--work-source-uid".equals(str)) {
                            i2++;
                            if (i2 >= strArr.length) {
                                throw new IllegalArgumentException("Argument expected after \"" + str + "\"");
                            }
                            String str2 = strArr[i2];
                            try {
                                i3 = Integer.parseInt(str2);
                            } catch (NumberFormatException unused) {
                                printWriter.println("Invalid UID: " + str2);
                                return;
                            }
                        } else {
                            continue;
                        }
                    }
                    i2++;
                }
                if (strArr.length > 0 && i3 == -1 && new BinderCallsStatsShellCommand(printWriter).exec(this, (FileDescriptor) null, FileDescriptor.out, FileDescriptor.err, strArr) == 0) {
                    return;
                }
                z = z2;
                i = i3;
            }
            this.mBinderCallsStats.dump(printWriter, AppIdToPackageMap.getSnapshot(), i, z);
        }
    }

    public int handleShellCommand(ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2, ParcelFileDescriptor parcelFileDescriptor3, String[] strArr) {
        BinderCallsStatsShellCommand binderCallsStatsShellCommand = new BinderCallsStatsShellCommand(null);
        int exec = binderCallsStatsShellCommand.exec(this, parcelFileDescriptor.getFileDescriptor(), parcelFileDescriptor2.getFileDescriptor(), parcelFileDescriptor3.getFileDescriptor(), strArr);
        if (exec != 0) {
            binderCallsStatsShellCommand.onHelp();
        }
        return exec;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class BinderCallsStatsShellCommand extends ShellCommand {
        private final PrintWriter mPrintWriter;

        BinderCallsStatsShellCommand(PrintWriter printWriter) {
            this.mPrintWriter = printWriter;
        }

        public PrintWriter getOutPrintWriter() {
            PrintWriter printWriter = this.mPrintWriter;
            return printWriter != null ? printWriter : super.getOutPrintWriter();
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        public int onCommand(String str) {
            char c;
            PrintWriter outPrintWriter = getOutPrintWriter();
            if (str == null) {
                return -1;
            }
            switch (str.hashCode()) {
                case -1615291473:
                    if (str.equals("--reset")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -1289263917:
                    if (str.equals("--no-sampling")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case -1237677752:
                    if (str.equals("--disable")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case -534486470:
                    if (str.equals("--work-source-uid")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case -106516359:
                    if (str.equals("--dump-worksource-provider")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 1101165347:
                    if (str.equals("--enable")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 1448286703:
                    if (str.equals("--disable-detailed-tracking")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 2041864970:
                    if (str.equals("--enable-detailed-tracking")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    BinderCallsStatsService.this.reset();
                    outPrintWriter.println("binder_calls_stats reset.");
                    return 0;
                case 1:
                    BinderCallsStatsService.this.mBinderCallsStats.setSamplingInterval(1);
                    return 0;
                case 2:
                    Binder.setObserver(null);
                    return 0;
                case 3:
                    String nextArgRequired = getNextArgRequired();
                    try {
                        BinderCallsStatsService.this.mBinderCallsStats.recordAllCallsForWorkSourceUid(Integer.parseInt(nextArgRequired));
                        return 0;
                    } catch (NumberFormatException unused) {
                        outPrintWriter.println("Invalid UID: " + nextArgRequired);
                        return -1;
                    }
                case 4:
                    BinderCallsStatsService.this.mBinderCallsStats.setDetailedTracking(true);
                    BinderCallsStatsService.this.mWorkSourceProvider.dump(outPrintWriter, AppIdToPackageMap.getSnapshot());
                    return 0;
                case 5:
                    Binder.setObserver(BinderCallsStatsService.this.mBinderCallsStats);
                    return 0;
                case 6:
                    SystemProperties.set(BinderCallsStatsService.PERSIST_SYS_BINDER_CALLS_DETAILED_TRACKING, "");
                    BinderCallsStatsService.this.mBinderCallsStats.setDetailedTracking(false);
                    outPrintWriter.println("Detailed tracking disabled");
                    return 0;
                case 7:
                    SystemProperties.set(BinderCallsStatsService.PERSIST_SYS_BINDER_CALLS_DETAILED_TRACKING, "1");
                    BinderCallsStatsService.this.mBinderCallsStats.setDetailedTracking(true);
                    outPrintWriter.println("Detailed tracking enabled");
                    return 0;
                default:
                    return handleDefaultCommands(str);
            }
        }

        public void onHelp() {
            PrintWriter outPrintWriter = getOutPrintWriter();
            outPrintWriter.println("binder_calls_stats commands:");
            outPrintWriter.println("  --reset: Reset stats");
            outPrintWriter.println("  --enable: Enable tracking binder calls");
            outPrintWriter.println("  --disable: Disables tracking binder calls");
            outPrintWriter.println("  --no-sampling: Tracks all calls");
            outPrintWriter.println("  --enable-detailed-tracking: Enables detailed tracking");
            outPrintWriter.println("  --disable-detailed-tracking: Disables detailed tracking");
            outPrintWriter.println("  --work-source-uid <UID>: Track all binder calls from the UID");
        }
    }
}
