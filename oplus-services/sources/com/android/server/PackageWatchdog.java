package com.android.server;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.VersionedPackage;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.net.ConnectivityModuleConnector;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.DeviceConfig;
import android.service.watchdog.ExplicitHealthCheckService;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.LongArrayQueue;
import android.util.MathUtils;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class PackageWatchdog {
    private static final String ATTR_DURATION = "duration";
    private static final String ATTR_EXPLICIT_HEALTH_CHECK_DURATION = "health-check-duration";
    private static final String ATTR_MITIGATION_CALLS = "mitigation-calls";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PASSED_HEALTH_CHECK = "passed-health-check";
    private static final String ATTR_VERSION = "version";
    private static final int DB_VERSION = 1;

    @VisibleForTesting
    static final int DEFAULT_BOOT_LOOP_TRIGGER_COUNT = 5;
    static final long DEFAULT_BOOT_LOOP_TRIGGER_WINDOW_MS;

    @VisibleForTesting
    static final long DEFAULT_DEESCALATION_WINDOW_MS;
    private static final boolean DEFAULT_EXPLICIT_HEALTH_CHECK_ENABLED = true;

    @VisibleForTesting
    static final long DEFAULT_OBSERVING_DURATION_MS;

    @VisibleForTesting
    static final int DEFAULT_TRIGGER_FAILURE_COUNT = 5;

    @VisibleForTesting
    static final int DEFAULT_TRIGGER_FAILURE_DURATION_MS;
    public static final int FAILURE_REASON_APP_CRASH = 3;
    public static final int FAILURE_REASON_APP_NOT_RESPONDING = 4;
    public static final int FAILURE_REASON_EXPLICIT_HEALTH_CHECK = 2;
    public static final int FAILURE_REASON_NATIVE_CRASH = 1;
    public static final int FAILURE_REASON_UNKNOWN = 0;
    private static final String METADATA_FILE = "/metadata/watchdog/mitigation_count.txt";
    private static final long NATIVE_CRASH_POLLING_INTERVAL_MILLIS = TimeUnit.SECONDS.toMillis(30);
    private static final long NUMBER_OF_NATIVE_CRASH_POLLS = 10;
    static final String PROPERTY_WATCHDOG_EXPLICIT_HEALTH_CHECK_ENABLED = "watchdog_explicit_health_check_enabled";
    static final String PROPERTY_WATCHDOG_TRIGGER_DURATION_MILLIS = "watchdog_trigger_failure_duration_millis";
    static final String PROPERTY_WATCHDOG_TRIGGER_FAILURE_COUNT = "watchdog_trigger_failure_count";
    private static final String PROP_BOOT_MITIGATION_COUNT = "sys.boot_mitigation_count";
    private static final String PROP_BOOT_MITIGATION_WINDOW_START = "sys.boot_mitigation_start";
    private static final String PROP_RESCUE_BOOT_COUNT = "sys.rescue_boot_count";
    private static final String PROP_RESCUE_BOOT_START = "sys.rescue_boot_start";
    private static final String TAG = "PackageWatchdog";
    private static final String TAG_OBSERVER = "observer";
    private static final String TAG_PACKAGE = "package";
    private static final String TAG_PACKAGE_WATCHDOG = "package-watchdog";

    @GuardedBy({"PackageWatchdog.class"})
    private static PackageWatchdog sPackageWatchdog;

    @GuardedBy({"mLock"})
    private final ArrayMap<String, ObserverInternal> mAllObservers;
    private final BootThreshold mBootThreshold;
    private final ConnectivityModuleConnector mConnectivityModuleConnector;
    private final Context mContext;
    private final ExplicitHealthCheckController mHealthCheckController;

    @GuardedBy({"mLock"})
    private boolean mIsHealthCheckEnabled;

    @GuardedBy({"mLock"})
    private boolean mIsPackagesReady;
    private final Object mLock;
    private final Handler mLongTaskHandler;
    private long mNumberOfNativeCrashPollsRemaining;
    private final DeviceConfig.OnPropertiesChangedListener mOnPropertyChangedListener;
    private final AtomicFile mPolicyFile;

    @GuardedBy({"mLock"})
    private Set<String> mRequestedHealthCheckPackages;
    private final Runnable mSaveToFile;
    private final Handler mShortTaskHandler;
    private final Runnable mSyncRequests;

    @GuardedBy({"mLock"})
    private boolean mSyncRequired;
    private final Runnable mSyncStateWithScheduledReason;
    private final SystemClock mSystemClock;

    @GuardedBy({"mLock"})
    private int mTriggerFailureCount;

    @GuardedBy({"mLock"})
    private int mTriggerFailureDurationMs;

    @GuardedBy({"mLock"})
    private long mUptimeAtLastStateSync;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface FailureReasons {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface HealthCheckState {
        public static final int ACTIVE = 0;
        public static final int FAILED = 3;
        public static final int INACTIVE = 1;
        public static final int PASSED = 2;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface PackageHealthObserver {
        boolean execute(VersionedPackage versionedPackage, int i, int i2);

        default boolean executeBootLoopMitigation(int i) {
            return false;
        }

        String getName();

        default boolean isPersistent() {
            return false;
        }

        default boolean mayObservePackage(String str) {
            return false;
        }

        default int onBootLoop(int i) {
            return 0;
        }

        int onHealthCheckFailed(VersionedPackage versionedPackage, int i, int i2);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface PackageHealthObserverImpact {
        public static final int USER_IMPACT_LEVEL_0 = 0;
        public static final int USER_IMPACT_LEVEL_10 = 10;
        public static final int USER_IMPACT_LEVEL_100 = 100;
        public static final int USER_IMPACT_LEVEL_30 = 30;
        public static final int USER_IMPACT_LEVEL_50 = 50;
        public static final int USER_IMPACT_LEVEL_70 = 70;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @FunctionalInterface
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface SystemClock {
        long uptimeMillis();
    }

    static {
        TimeUnit timeUnit = TimeUnit.MINUTES;
        DEFAULT_TRIGGER_FAILURE_DURATION_MS = (int) timeUnit.toMillis(1L);
        DEFAULT_OBSERVING_DURATION_MS = TimeUnit.DAYS.toMillis(2L);
        DEFAULT_DEESCALATION_WINDOW_MS = TimeUnit.HOURS.toMillis(1L);
        DEFAULT_BOOT_LOOP_TRIGGER_WINDOW_MS = timeUnit.toMillis(NUMBER_OF_NATIVE_CRASH_POLLS);
    }

    private PackageWatchdog(Context context) {
        this(context, new AtomicFile(new File(new File(Environment.getDataDirectory(), "system"), "package-watchdog.xml")), new Handler(Looper.myLooper()), BackgroundThread.getHandler(), new ExplicitHealthCheckController(context), ConnectivityModuleConnector.getInstance(), new SystemClock() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda3
            @Override // com.android.server.PackageWatchdog.SystemClock
            public final long uptimeMillis() {
                return SystemClock.uptimeMillis();
            }
        });
    }

    @VisibleForTesting
    PackageWatchdog(Context context, AtomicFile atomicFile, Handler handler, Handler handler2, ExplicitHealthCheckController explicitHealthCheckController, ConnectivityModuleConnector connectivityModuleConnector, SystemClock systemClock) {
        this.mLock = new Object();
        this.mAllObservers = new ArrayMap<>();
        this.mSyncRequests = new Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                PackageWatchdog.this.syncRequests();
            }
        };
        this.mSyncStateWithScheduledReason = new Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                PackageWatchdog.this.syncStateWithScheduledReason();
            }
        };
        this.mSaveToFile = new Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                PackageWatchdog.this.saveToFile();
            }
        };
        this.mOnPropertyChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda8
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                PackageWatchdog.this.onPropertyChanged(properties);
            }
        };
        this.mRequestedHealthCheckPackages = new ArraySet();
        this.mIsHealthCheckEnabled = true;
        this.mTriggerFailureDurationMs = DEFAULT_TRIGGER_FAILURE_DURATION_MS;
        this.mTriggerFailureCount = 5;
        this.mSyncRequired = false;
        this.mContext = context;
        this.mPolicyFile = atomicFile;
        this.mShortTaskHandler = handler;
        this.mLongTaskHandler = handler2;
        this.mHealthCheckController = explicitHealthCheckController;
        this.mConnectivityModuleConnector = connectivityModuleConnector;
        this.mSystemClock = systemClock;
        this.mNumberOfNativeCrashPollsRemaining = NUMBER_OF_NATIVE_CRASH_POLLS;
        this.mBootThreshold = new BootThreshold(5, DEFAULT_BOOT_LOOP_TRIGGER_WINDOW_MS);
        loadFromFile();
        sPackageWatchdog = this;
    }

    public static PackageWatchdog getInstance(Context context) {
        PackageWatchdog packageWatchdog;
        synchronized (PackageWatchdog.class) {
            if (sPackageWatchdog == null) {
                new PackageWatchdog(context);
            }
            packageWatchdog = sPackageWatchdog;
        }
        return packageWatchdog;
    }

    public void onPackagesReady() {
        synchronized (this.mLock) {
            this.mIsPackagesReady = true;
            this.mHealthCheckController.setCallbacks(new Consumer() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda11
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PackageWatchdog.this.lambda$onPackagesReady$0((String) obj);
                }
            }, new Consumer() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda12
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PackageWatchdog.this.lambda$onPackagesReady$1((List) obj);
                }
            }, new Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    PackageWatchdog.this.onSyncRequestNotified();
                }
            });
            setPropertyChangedListenerLocked();
            updateConfigs();
            registerConnectivityModuleHealthListener();
        }
    }

    public void registerHealthObserver(PackageHealthObserver packageHealthObserver) {
        synchronized (this.mLock) {
            ObserverInternal observerInternal = this.mAllObservers.get(packageHealthObserver.getName());
            if (observerInternal != null) {
                observerInternal.registeredObserver = packageHealthObserver;
            } else {
                ObserverInternal observerInternal2 = new ObserverInternal(packageHealthObserver.getName(), new ArrayList());
                observerInternal2.registeredObserver = packageHealthObserver;
                this.mAllObservers.put(packageHealthObserver.getName(), observerInternal2);
                syncState("added new observer");
            }
        }
    }

    public void startObservingHealth(final PackageHealthObserver packageHealthObserver, final List<String> list, long j) {
        if (list.isEmpty()) {
            Slog.wtf(TAG, "No packages to observe, " + packageHealthObserver.getName());
            return;
        }
        if (j < 1) {
            Slog.wtf(TAG, "Invalid duration " + j + "ms for observer " + packageHealthObserver.getName() + ". Not observing packages " + list);
            j = DEFAULT_OBSERVING_DURATION_MS;
        }
        final ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            MonitoredPackage newMonitoredPackage = newMonitoredPackage(list.get(i), j, false);
            if (newMonitoredPackage != null) {
                arrayList.add(newMonitoredPackage);
            } else {
                Slog.w(TAG, "Failed to create MonitoredPackage for pkg=" + list.get(i));
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        this.mLongTaskHandler.post(new Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                PackageWatchdog.this.lambda$startObservingHealth$2(packageHealthObserver, list, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startObservingHealth$2(PackageHealthObserver packageHealthObserver, List list, List list2) {
        syncState("observing new packages");
        synchronized (this.mLock) {
            ObserverInternal observerInternal = this.mAllObservers.get(packageHealthObserver.getName());
            if (observerInternal == null) {
                Slog.d(TAG, packageHealthObserver.getName() + " started monitoring health of packages " + list);
                this.mAllObservers.put(packageHealthObserver.getName(), new ObserverInternal(packageHealthObserver.getName(), list2));
            } else {
                Slog.d(TAG, packageHealthObserver.getName() + " added the following packages to monitor " + list);
                observerInternal.updatePackagesLocked(list2);
            }
        }
        registerHealthObserver(packageHealthObserver);
        syncState("updated observers");
    }

    public void unregisterHealthObserver(final PackageHealthObserver packageHealthObserver) {
        this.mLongTaskHandler.post(new Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                PackageWatchdog.this.lambda$unregisterHealthObserver$3(packageHealthObserver);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterHealthObserver$3(PackageHealthObserver packageHealthObserver) {
        synchronized (this.mLock) {
            this.mAllObservers.remove(packageHealthObserver.getName());
        }
        syncState("unregistering observer: " + packageHealthObserver.getName());
    }

    public void onPackageFailure(final List<VersionedPackage> list, final int i) {
        if (list == null) {
            Slog.w(TAG, "Could not resolve a list of failing packages");
        } else {
            this.mLongTaskHandler.post(new Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PackageWatchdog.this.lambda$onPackageFailure$4(i, list);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:16:0x001a A[Catch: all -> 0x0085, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000b, B:16:0x001a, B:17:0x0083, B:20:0x0020, B:22:0x0026, B:23:0x0033, B:25:0x003b, B:27:0x0047, B:29:0x0051, B:31:0x005b, B:32:0x0062, B:37:0x006d, B:46:0x0074, B:47:0x007d, B:49:0x0080), top: B:3:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x001f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$onPackageFailure$4(int i, List list) {
        boolean z;
        int i2;
        synchronized (this.mLock) {
            if (this.mAllObservers.isEmpty()) {
                return;
            }
            if (i != 1 && i != 2) {
                z = false;
                if (!z) {
                    handleFailureImmediately(list, i);
                } else {
                    for (int i3 = 0; i3 < list.size(); i3++) {
                        VersionedPackage versionedPackage = (VersionedPackage) list.get(i3);
                        PackageHealthObserver packageHealthObserver = null;
                        int i4 = Integer.MAX_VALUE;
                        MonitoredPackage monitoredPackage = null;
                        for (int i5 = 0; i5 < this.mAllObservers.size(); i5++) {
                            ObserverInternal valueAt = this.mAllObservers.valueAt(i5);
                            PackageHealthObserver packageHealthObserver2 = valueAt.registeredObserver;
                            if (packageHealthObserver2 != null && valueAt.onPackageFailureLocked(versionedPackage.getPackageName())) {
                                MonitoredPackage monitoredPackage2 = valueAt.getMonitoredPackage(versionedPackage.getPackageName());
                                int onHealthCheckFailed = packageHealthObserver2.onHealthCheckFailed(versionedPackage, i, monitoredPackage2 != null ? monitoredPackage2.getMitigationCountLocked() + 1 : 1);
                                if (onHealthCheckFailed != 0 && onHealthCheckFailed < i4) {
                                    monitoredPackage = monitoredPackage2;
                                    packageHealthObserver = packageHealthObserver2;
                                    i4 = onHealthCheckFailed;
                                }
                            }
                        }
                        if (packageHealthObserver != null) {
                            if (monitoredPackage != null) {
                                monitoredPackage.noteMitigationCallLocked();
                                i2 = monitoredPackage.getMitigationCountLocked();
                            } else {
                                i2 = 1;
                            }
                            packageHealthObserver.execute(versionedPackage, i, i2);
                        }
                    }
                }
            }
            z = true;
            if (!z) {
            }
        }
    }

    private void handleFailureImmediately(List<VersionedPackage> list, int i) {
        int onHealthCheckFailed;
        PackageHealthObserver packageHealthObserver = null;
        VersionedPackage versionedPackage = list.size() > 0 ? list.get(0) : null;
        Iterator<ObserverInternal> it = this.mAllObservers.values().iterator();
        int i2 = Integer.MAX_VALUE;
        while (it.hasNext()) {
            PackageHealthObserver packageHealthObserver2 = it.next().registeredObserver;
            if (packageHealthObserver2 != null && (onHealthCheckFailed = packageHealthObserver2.onHealthCheckFailed(versionedPackage, i, 1)) != 0 && onHealthCheckFailed < i2) {
                packageHealthObserver = packageHealthObserver2;
                i2 = onHealthCheckFailed;
            }
        }
        if (packageHealthObserver != null) {
            packageHealthObserver.execute(versionedPackage, i, 1);
        }
    }

    public void noteBoot() {
        int onBootLoop;
        synchronized (this.mLock) {
            if (this.mBootThreshold.incrementAndTest()) {
                this.mBootThreshold.reset();
                int mitigationCount = this.mBootThreshold.getMitigationCount() + 1;
                PackageHealthObserver packageHealthObserver = null;
                int i = Integer.MAX_VALUE;
                for (int i2 = 0; i2 < this.mAllObservers.size(); i2++) {
                    PackageHealthObserver packageHealthObserver2 = this.mAllObservers.valueAt(i2).registeredObserver;
                    if (packageHealthObserver2 != null && (onBootLoop = packageHealthObserver2.onBootLoop(mitigationCount)) != 0 && onBootLoop < i) {
                        packageHealthObserver = packageHealthObserver2;
                        i = onBootLoop;
                    }
                }
                if (packageHealthObserver != null) {
                    this.mBootThreshold.setMitigationCount(mitigationCount);
                    this.mBootThreshold.saveMitigationCountToMetadata();
                    packageHealthObserver.executeBootLoopMitigation(mitigationCount);
                }
            }
        }
    }

    public void writeNow() {
        synchronized (this.mLock) {
            if (!this.mAllObservers.isEmpty()) {
                this.mLongTaskHandler.removeCallbacks(this.mSaveToFile);
                pruneObserversLocked();
                saveToFile();
                Slog.i(TAG, "Last write to update package durations");
            }
        }
    }

    private void setExplicitHealthCheckEnabled(boolean z) {
        synchronized (this.mLock) {
            this.mIsHealthCheckEnabled = z;
            this.mHealthCheckController.setEnabled(z);
            this.mSyncRequired = true;
            StringBuilder sb = new StringBuilder();
            sb.append("health check state ");
            sb.append(z ? "enabled" : "disabled");
            syncState(sb.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: checkAndMitigateNativeCrashes, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$scheduleCheckAndMitigateNativeCrashes$6() {
        this.mNumberOfNativeCrashPollsRemaining--;
        if ("1".equals(SystemProperties.get("sys.init.updatable_crashing"))) {
            onPackageFailure(Collections.EMPTY_LIST, 1);
        } else if (this.mNumberOfNativeCrashPollsRemaining > 0) {
            this.mShortTaskHandler.postDelayed(new Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    PackageWatchdog.this.lambda$checkAndMitigateNativeCrashes$5();
                }
            }, NATIVE_CRASH_POLLING_INTERVAL_MILLIS);
        }
    }

    public void scheduleCheckAndMitigateNativeCrashes() {
        Slog.i(TAG, "Scheduling " + this.mNumberOfNativeCrashPollsRemaining + " polls to check and mitigate native crashes");
        this.mShortTaskHandler.post(new Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PackageWatchdog.this.lambda$scheduleCheckAndMitigateNativeCrashes$6();
            }
        });
    }

    @VisibleForTesting
    long getTriggerFailureCount() {
        long j;
        synchronized (this.mLock) {
            j = this.mTriggerFailureCount;
        }
        return j;
    }

    @VisibleForTesting
    long getTriggerFailureDurationMs() {
        long j;
        synchronized (this.mLock) {
            j = this.mTriggerFailureDurationMs;
        }
        return j;
    }

    private void syncRequestsAsync() {
        this.mShortTaskHandler.removeCallbacks(this.mSyncRequests);
        this.mShortTaskHandler.post(this.mSyncRequests);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncRequests() {
        boolean z;
        synchronized (this.mLock) {
            if (this.mIsPackagesReady) {
                Set<String> packagesPendingHealthChecksLocked = getPackagesPendingHealthChecksLocked();
                if (this.mSyncRequired || !packagesPendingHealthChecksLocked.equals(this.mRequestedHealthCheckPackages) || packagesPendingHealthChecksLocked.isEmpty()) {
                    this.mRequestedHealthCheckPackages = packagesPendingHealthChecksLocked;
                    z = true;
                }
            }
            z = false;
        }
        if (z) {
            Slog.i(TAG, "Syncing health check requests for packages: " + this.mRequestedHealthCheckPackages);
            this.mHealthCheckController.syncRequests(this.mRequestedHealthCheckPackages);
            this.mSyncRequired = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onHealthCheckPassed, reason: merged with bridge method [inline-methods] */
    public void lambda$onPackagesReady$0(String str) {
        boolean z;
        Slog.i(TAG, "Health check passed for package: " + str);
        synchronized (this.mLock) {
            z = false;
            for (int i = 0; i < this.mAllObservers.size(); i++) {
                MonitoredPackage monitoredPackage = this.mAllObservers.valueAt(i).getMonitoredPackage(str);
                if (monitoredPackage != null) {
                    z |= monitoredPackage.getHealthCheckStateLocked() != monitoredPackage.tryPassHealthCheckLocked();
                }
            }
        }
        if (z) {
            syncState("health check passed for " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onSupportedPackages, reason: merged with bridge method [inline-methods] */
    public void lambda$onPackagesReady$1(List<ExplicitHealthCheckService.PackageConfig> list) {
        boolean z;
        int tryPassHealthCheckLocked;
        ArrayMap arrayMap = new ArrayMap();
        for (ExplicitHealthCheckService.PackageConfig packageConfig : list) {
            arrayMap.put(packageConfig.getPackageName(), Long.valueOf(packageConfig.getHealthCheckTimeoutMillis()));
        }
        synchronized (this.mLock) {
            Slog.d(TAG, "Received supported packages " + list);
            Iterator<ObserverInternal> it = this.mAllObservers.values().iterator();
            z = false;
            while (it.hasNext()) {
                for (MonitoredPackage monitoredPackage : it.next().getMonitoredPackages().values()) {
                    String name = monitoredPackage.getName();
                    int healthCheckStateLocked = monitoredPackage.getHealthCheckStateLocked();
                    if (arrayMap.containsKey(name)) {
                        tryPassHealthCheckLocked = monitoredPackage.setHealthCheckActiveLocked(((Long) arrayMap.get(name)).longValue());
                    } else {
                        tryPassHealthCheckLocked = monitoredPackage.tryPassHealthCheckLocked();
                    }
                    z |= healthCheckStateLocked != tryPassHealthCheckLocked;
                }
            }
        }
        if (z) {
            syncState("updated health check supported packages " + list);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSyncRequestNotified() {
        synchronized (this.mLock) {
            this.mSyncRequired = true;
            syncRequestsAsync();
        }
    }

    @GuardedBy({"mLock"})
    private Set<String> getPackagesPendingHealthChecksLocked() {
        ArraySet arraySet = new ArraySet();
        Iterator<ObserverInternal> it = this.mAllObservers.values().iterator();
        while (it.hasNext()) {
            for (MonitoredPackage monitoredPackage : it.next().getMonitoredPackages().values()) {
                String name = monitoredPackage.getName();
                if (monitoredPackage.isPendingHealthChecksLocked()) {
                    arraySet.add(name);
                }
            }
        }
        return arraySet;
    }

    private void syncState(String str) {
        synchronized (this.mLock) {
            Slog.i(TAG, "Syncing state, reason: " + str);
            pruneObserversLocked();
            saveToFileAsync();
            syncRequestsAsync();
            scheduleNextSyncStateLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncStateWithScheduledReason() {
        syncState("scheduled");
    }

    @GuardedBy({"mLock"})
    private void scheduleNextSyncStateLocked() {
        long nextStateSyncMillisLocked = getNextStateSyncMillisLocked();
        this.mShortTaskHandler.removeCallbacks(this.mSyncStateWithScheduledReason);
        if (nextStateSyncMillisLocked == Long.MAX_VALUE) {
            Slog.i(TAG, "Cancelling state sync, nothing to sync");
            this.mUptimeAtLastStateSync = 0L;
        } else {
            this.mUptimeAtLastStateSync = this.mSystemClock.uptimeMillis();
            this.mShortTaskHandler.postDelayed(this.mSyncStateWithScheduledReason, nextStateSyncMillisLocked);
        }
    }

    @GuardedBy({"mLock"})
    private long getNextStateSyncMillisLocked() {
        long j = Long.MAX_VALUE;
        for (int i = 0; i < this.mAllObservers.size(); i++) {
            ArrayMap<String, MonitoredPackage> monitoredPackages = this.mAllObservers.valueAt(i).getMonitoredPackages();
            for (int i2 = 0; i2 < monitoredPackages.size(); i2++) {
                long shortestScheduleDurationMsLocked = monitoredPackages.valueAt(i2).getShortestScheduleDurationMsLocked();
                if (shortestScheduleDurationMsLocked < j) {
                    j = shortestScheduleDurationMsLocked;
                }
            }
        }
        return j;
    }

    @GuardedBy({"mLock"})
    private void pruneObserversLocked() {
        PackageHealthObserver packageHealthObserver;
        long uptimeMillis = this.mUptimeAtLastStateSync == 0 ? 0L : this.mSystemClock.uptimeMillis() - this.mUptimeAtLastStateSync;
        if (uptimeMillis <= 0) {
            Slog.i(TAG, "Not pruning observers, elapsed time: " + uptimeMillis + "ms");
            return;
        }
        Iterator<ObserverInternal> it = this.mAllObservers.values().iterator();
        while (it.hasNext()) {
            ObserverInternal next = it.next();
            Set<MonitoredPackage> prunePackagesLocked = next.prunePackagesLocked(uptimeMillis);
            if (!prunePackagesLocked.isEmpty()) {
                onHealthCheckFailed(next, prunePackagesLocked);
            }
            if (next.getMonitoredPackages().isEmpty() && ((packageHealthObserver = next.registeredObserver) == null || !packageHealthObserver.isPersistent())) {
                Slog.i(TAG, "Discarding observer " + next.name + ". All packages expired");
                it.remove();
            }
        }
    }

    private void onHealthCheckFailed(final ObserverInternal observerInternal, final Set<MonitoredPackage> set) {
        this.mLongTaskHandler.post(new Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                PackageWatchdog.this.lambda$onHealthCheckFailed$7(observerInternal, set);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onHealthCheckFailed$7(ObserverInternal observerInternal, Set set) {
        synchronized (this.mLock) {
            PackageHealthObserver packageHealthObserver = observerInternal.registeredObserver;
            if (packageHealthObserver != null) {
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    VersionedPackage versionedPackage = getVersionedPackage(((MonitoredPackage) it.next()).getName());
                    if (versionedPackage != null) {
                        Slog.i(TAG, "Explicit health check failed for package " + versionedPackage);
                        packageHealthObserver.execute(versionedPackage, 2, 1);
                    }
                }
            }
        }
    }

    private PackageInfo getPackageInfo(String str) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = this.mContext.getPackageManager();
        try {
            return packageManager.getPackageInfo(str, AudioDevice.OUT_SPEAKER_SAFE);
        } catch (PackageManager.NameNotFoundException unused) {
            return packageManager.getPackageInfo(str, 1073741824);
        }
    }

    private VersionedPackage getVersionedPackage(String str) {
        if (this.mContext.getPackageManager() != null && !TextUtils.isEmpty(str)) {
            try {
                return new VersionedPackage(str, getPackageInfo(str).getLongVersionCode());
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return null;
    }

    private void loadFromFile() {
        this.mAllObservers.clear();
        FileInputStream fileInputStream = null;
        try {
            try {
                fileInputStream = this.mPolicyFile.openRead();
                TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream);
                XmlUtils.beginDocument(resolvePullParser, TAG_PACKAGE_WATCHDOG);
                int depth = resolvePullParser.getDepth();
                while (XmlUtils.nextElementWithin(resolvePullParser, depth)) {
                    ObserverInternal read = ObserverInternal.read(resolvePullParser, this);
                    if (read != null) {
                        this.mAllObservers.put(read.name, read);
                    }
                }
            } catch (FileNotFoundException unused) {
            } catch (IOException | NumberFormatException | XmlPullParserException e) {
                Slog.wtf(TAG, "Unable to read monitored packages, deleting file", e);
                this.mPolicyFile.delete();
            }
        } finally {
            IoUtils.closeQuietly(fileInputStream);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPropertyChanged(DeviceConfig.Properties properties) {
        try {
            updateConfigs();
        } catch (Exception unused) {
            Slog.w(TAG, "Failed to reload device config changes");
        }
    }

    private void setPropertyChangedListenerLocked() {
        DeviceConfig.addOnPropertiesChangedListener("rollback", this.mContext.getMainExecutor(), this.mOnPropertyChangedListener);
    }

    @VisibleForTesting
    void removePropertyChangedListener() {
        DeviceConfig.removeOnPropertiesChangedListener(this.mOnPropertyChangedListener);
    }

    @VisibleForTesting
    void updateConfigs() {
        synchronized (this.mLock) {
            int i = DeviceConfig.getInt("rollback", PROPERTY_WATCHDOG_TRIGGER_FAILURE_COUNT, 5);
            this.mTriggerFailureCount = i;
            if (i <= 0) {
                this.mTriggerFailureCount = 5;
            }
            int i2 = DEFAULT_TRIGGER_FAILURE_DURATION_MS;
            int i3 = DeviceConfig.getInt("rollback", PROPERTY_WATCHDOG_TRIGGER_DURATION_MILLIS, i2);
            this.mTriggerFailureDurationMs = i3;
            if (i3 <= 0) {
                this.mTriggerFailureDurationMs = i2;
            }
            setExplicitHealthCheckEnabled(DeviceConfig.getBoolean("rollback", PROPERTY_WATCHDOG_EXPLICIT_HEALTH_CHECK_ENABLED, true));
        }
    }

    private void registerConnectivityModuleHealthListener() {
        this.mConnectivityModuleConnector.registerHealthListener(new ConnectivityModuleConnector.ConnectivityModuleHealthListener() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda2
            @Override // android.net.ConnectivityModuleConnector.ConnectivityModuleHealthListener
            public final void onNetworkStackFailure(String str) {
                PackageWatchdog.this.lambda$registerConnectivityModuleHealthListener$8(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerConnectivityModuleHealthListener$8(String str) {
        VersionedPackage versionedPackage = getVersionedPackage(str);
        if (versionedPackage == null) {
            Slog.wtf(TAG, "NetworkStack failed but could not find its package");
        } else {
            onPackageFailure(Collections.singletonList(versionedPackage), 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean saveToFile() {
        Slog.i(TAG, "Saving observer state to file");
        synchronized (this.mLock) {
            try {
                try {
                    FileOutputStream startWrite = this.mPolicyFile.startWrite();
                    try {
                        TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                        resolveSerializer.startDocument((String) null, Boolean.TRUE);
                        resolveSerializer.startTag((String) null, TAG_PACKAGE_WATCHDOG);
                        resolveSerializer.attributeInt((String) null, ATTR_VERSION, 1);
                        for (int i = 0; i < this.mAllObservers.size(); i++) {
                            this.mAllObservers.valueAt(i).writeLocked(resolveSerializer);
                        }
                        resolveSerializer.endTag((String) null, TAG_PACKAGE_WATCHDOG);
                        resolveSerializer.endDocument();
                        this.mPolicyFile.finishWrite(startWrite);
                    } catch (IOException e) {
                        Slog.w(TAG, "Failed to save monitored packages, restoring backup", e);
                        this.mPolicyFile.failWrite(startWrite);
                        return false;
                    } finally {
                        IoUtils.closeQuietly(startWrite);
                    }
                } catch (IOException e2) {
                    Slog.w(TAG, "Cannot update monitored packages", e2);
                    return false;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return true;
    }

    private void saveToFileAsync() {
        if (this.mLongTaskHandler.hasCallbacks(this.mSaveToFile)) {
            return;
        }
        this.mLongTaskHandler.post(this.mSaveToFile);
    }

    public static String longArrayQueueToString(LongArrayQueue longArrayQueue) {
        if (longArrayQueue.size() <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(longArrayQueue.get(0));
        for (int i = 1; i < longArrayQueue.size(); i++) {
            sb.append(",");
            sb.append(longArrayQueue.get(i));
        }
        return sb.toString();
    }

    public static LongArrayQueue parseLongArrayQueue(String str) {
        LongArrayQueue longArrayQueue = new LongArrayQueue();
        if (!TextUtils.isEmpty(str)) {
            for (String str2 : str.split(",")) {
                longArrayQueue.addLast(Long.parseLong(str2));
            }
        }
        return longArrayQueue;
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("Package Watchdog status");
        indentingPrintWriter.increaseIndent();
        synchronized (this.mLock) {
            for (String str : this.mAllObservers.keySet()) {
                indentingPrintWriter.println("Observer name: " + str);
                indentingPrintWriter.increaseIndent();
                this.mAllObservers.get(str).dump(indentingPrintWriter);
                indentingPrintWriter.decreaseIndent();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class ObserverInternal {

        @GuardedBy({"mLock"})
        private final ArrayMap<String, MonitoredPackage> mPackages = new ArrayMap<>();
        public final String name;

        @GuardedBy({"mLock"})
        public PackageHealthObserver registeredObserver;

        ObserverInternal(String str, List<MonitoredPackage> list) {
            this.name = str;
            updatePackagesLocked(list);
        }

        @GuardedBy({"mLock"})
        public boolean writeLocked(TypedXmlSerializer typedXmlSerializer) {
            try {
                typedXmlSerializer.startTag((String) null, PackageWatchdog.TAG_OBSERVER);
                typedXmlSerializer.attribute((String) null, PackageWatchdog.ATTR_NAME, this.name);
                for (int i = 0; i < this.mPackages.size(); i++) {
                    this.mPackages.valueAt(i).writeLocked(typedXmlSerializer);
                }
                typedXmlSerializer.endTag((String) null, PackageWatchdog.TAG_OBSERVER);
                return true;
            } catch (IOException e) {
                Slog.w(PackageWatchdog.TAG, "Cannot save observer", e);
                return false;
            }
        }

        @GuardedBy({"mLock"})
        public void updatePackagesLocked(List<MonitoredPackage> list) {
            for (int i = 0; i < list.size(); i++) {
                MonitoredPackage monitoredPackage = list.get(i);
                MonitoredPackage monitoredPackage2 = getMonitoredPackage(monitoredPackage.getName());
                if (monitoredPackage2 != null) {
                    monitoredPackage2.updateHealthCheckDuration(monitoredPackage.mDurationMs);
                } else {
                    putMonitoredPackage(monitoredPackage);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        @GuardedBy({"mLock"})
        public Set<MonitoredPackage> prunePackagesLocked(long j) {
            ArraySet arraySet = new ArraySet();
            Iterator<MonitoredPackage> it = this.mPackages.values().iterator();
            while (it.hasNext()) {
                MonitoredPackage next = it.next();
                int healthCheckStateLocked = next.getHealthCheckStateLocked();
                int handleElapsedTimeLocked = next.handleElapsedTimeLocked(j);
                if (healthCheckStateLocked != 3 && handleElapsedTimeLocked == 3) {
                    Slog.i(PackageWatchdog.TAG, "Package " + next.getName() + " failed health check");
                    arraySet.add(next);
                }
                if (next.isExpiredLocked()) {
                    it.remove();
                }
            }
            return arraySet;
        }

        @GuardedBy({"mLock"})
        public boolean onPackageFailureLocked(String str) {
            if (getMonitoredPackage(str) == null && this.registeredObserver.isPersistent() && this.registeredObserver.mayObservePackage(str)) {
                putMonitoredPackage(PackageWatchdog.sPackageWatchdog.newMonitoredPackage(str, PackageWatchdog.DEFAULT_OBSERVING_DURATION_MS, false));
            }
            MonitoredPackage monitoredPackage = getMonitoredPackage(str);
            if (monitoredPackage != null) {
                return monitoredPackage.onFailureLocked();
            }
            return false;
        }

        @GuardedBy({"mLock"})
        public ArrayMap<String, MonitoredPackage> getMonitoredPackages() {
            return this.mPackages;
        }

        @GuardedBy({"mLock"})
        public MonitoredPackage getMonitoredPackage(String str) {
            return this.mPackages.get(str);
        }

        @GuardedBy({"mLock"})
        public void putMonitoredPackage(MonitoredPackage monitoredPackage) {
            this.mPackages.put(monitoredPackage.getName(), monitoredPackage);
        }

        public static ObserverInternal read(TypedXmlPullParser typedXmlPullParser, PackageWatchdog packageWatchdog) {
            String str;
            if (PackageWatchdog.TAG_OBSERVER.equals(typedXmlPullParser.getName())) {
                str = typedXmlPullParser.getAttributeValue((String) null, PackageWatchdog.ATTR_NAME);
                if (TextUtils.isEmpty(str)) {
                    Slog.wtf(PackageWatchdog.TAG, "Unable to read observer name");
                    return null;
                }
            } else {
                str = null;
            }
            ArrayList arrayList = new ArrayList();
            int depth = typedXmlPullParser.getDepth();
            while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                try {
                    if (PackageWatchdog.TAG_PACKAGE.equals(typedXmlPullParser.getName())) {
                        try {
                            MonitoredPackage parseMonitoredPackage = packageWatchdog.parseMonitoredPackage(typedXmlPullParser);
                            if (parseMonitoredPackage != null) {
                                arrayList.add(parseMonitoredPackage);
                            }
                        } catch (NumberFormatException e) {
                            Slog.wtf(PackageWatchdog.TAG, "Skipping package for observer " + str, e);
                        }
                    }
                } catch (IOException | XmlPullParserException e2) {
                    Slog.wtf(PackageWatchdog.TAG, "Unable to read observer " + str, e2);
                    return null;
                }
            }
            if (arrayList.isEmpty()) {
                return null;
            }
            return new ObserverInternal(str, arrayList);
        }

        public void dump(IndentingPrintWriter indentingPrintWriter) {
            PackageHealthObserver packageHealthObserver = this.registeredObserver;
            indentingPrintWriter.println("Persistent: " + (packageHealthObserver != null && packageHealthObserver.isPersistent()));
            for (String str : this.mPackages.keySet()) {
                MonitoredPackage monitoredPackage = getMonitoredPackage(str);
                indentingPrintWriter.println(str + ": ");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.println("# Failures: " + monitoredPackage.mFailureHistory.size());
                indentingPrintWriter.println("Monitoring duration remaining: " + monitoredPackage.mDurationMs + "ms");
                indentingPrintWriter.println("Explicit health check duration: " + monitoredPackage.mHealthCheckDurationMs + "ms");
                StringBuilder sb = new StringBuilder();
                sb.append("Health check state: ");
                sb.append(monitoredPackage.toString(monitoredPackage.mHealthCheckState));
                indentingPrintWriter.println(sb.toString());
                indentingPrintWriter.decreaseIndent();
            }
        }
    }

    MonitoredPackage newMonitoredPackage(String str, long j, boolean z) {
        return newMonitoredPackage(str, j, Long.MAX_VALUE, z, new LongArrayQueue());
    }

    MonitoredPackage newMonitoredPackage(String str, long j, long j2, boolean z, LongArrayQueue longArrayQueue) {
        return new MonitoredPackage(str, j, j2, z, longArrayQueue);
    }

    MonitoredPackage parseMonitoredPackage(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException {
        return newMonitoredPackage(typedXmlPullParser.getAttributeValue((String) null, ATTR_NAME), typedXmlPullParser.getAttributeLong((String) null, ATTR_DURATION), typedXmlPullParser.getAttributeLong((String) null, ATTR_EXPLICIT_HEALTH_CHECK_DURATION), typedXmlPullParser.getAttributeBoolean((String) null, ATTR_PASSED_HEALTH_CHECK), parseLongArrayQueue(typedXmlPullParser.getAttributeValue((String) null, ATTR_MITIGATION_CALLS)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class MonitoredPackage {

        @GuardedBy({"mLock"})
        private long mDurationMs;

        @GuardedBy({"mLock"})
        private boolean mHasPassedHealthCheck;

        @GuardedBy({"mLock"})
        private long mHealthCheckDurationMs;

        @GuardedBy({"mLock"})
        private final LongArrayQueue mMitigationCalls;
        private final String mPackageName;

        @GuardedBy({"mLock"})
        private final LongArrayQueue mFailureHistory = new LongArrayQueue();
        private int mHealthCheckState = 1;

        private long toPositive(long j) {
            if (j > 0) {
                return j;
            }
            return Long.MAX_VALUE;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String toString(int i) {
            return i != 0 ? i != 1 ? i != 2 ? i != 3 ? "UNKNOWN" : "FAILED" : "PASSED" : "INACTIVE" : "ACTIVE";
        }

        MonitoredPackage(String str, long j, long j2, boolean z, LongArrayQueue longArrayQueue) {
            this.mPackageName = str;
            this.mDurationMs = j;
            this.mHealthCheckDurationMs = j2;
            this.mHasPassedHealthCheck = z;
            this.mMitigationCalls = longArrayQueue;
            updateHealthCheckStateLocked();
        }

        @GuardedBy({"mLock"})
        public void writeLocked(TypedXmlSerializer typedXmlSerializer) throws IOException {
            typedXmlSerializer.startTag((String) null, PackageWatchdog.TAG_PACKAGE);
            typedXmlSerializer.attribute((String) null, PackageWatchdog.ATTR_NAME, getName());
            typedXmlSerializer.attributeLong((String) null, PackageWatchdog.ATTR_DURATION, this.mDurationMs);
            typedXmlSerializer.attributeLong((String) null, PackageWatchdog.ATTR_EXPLICIT_HEALTH_CHECK_DURATION, this.mHealthCheckDurationMs);
            typedXmlSerializer.attributeBoolean((String) null, PackageWatchdog.ATTR_PASSED_HEALTH_CHECK, this.mHasPassedHealthCheck);
            typedXmlSerializer.attribute((String) null, PackageWatchdog.ATTR_MITIGATION_CALLS, PackageWatchdog.longArrayQueueToString(normalizeMitigationCalls()));
            typedXmlSerializer.endTag((String) null, PackageWatchdog.TAG_PACKAGE);
        }

        @GuardedBy({"mLock"})
        public boolean onFailureLocked() {
            long uptimeMillis = PackageWatchdog.this.mSystemClock.uptimeMillis();
            this.mFailureHistory.addLast(uptimeMillis);
            while (uptimeMillis - this.mFailureHistory.peekFirst() > PackageWatchdog.this.mTriggerFailureDurationMs) {
                this.mFailureHistory.removeFirst();
            }
            boolean z = this.mFailureHistory.size() >= PackageWatchdog.this.mTriggerFailureCount;
            if (z) {
                this.mFailureHistory.clear();
            }
            return z;
        }

        @GuardedBy({"mLock"})
        public void noteMitigationCallLocked() {
            this.mMitigationCalls.addLast(PackageWatchdog.this.mSystemClock.uptimeMillis());
        }

        @GuardedBy({"mLock"})
        public int getMitigationCountLocked() {
            try {
                long uptimeMillis = PackageWatchdog.this.mSystemClock.uptimeMillis();
                while (uptimeMillis - this.mMitigationCalls.peekFirst() > PackageWatchdog.DEFAULT_DEESCALATION_WINDOW_MS) {
                    this.mMitigationCalls.removeFirst();
                }
            } catch (NoSuchElementException unused) {
            }
            return this.mMitigationCalls.size();
        }

        @GuardedBy({"mLock"})
        public LongArrayQueue normalizeMitigationCalls() {
            LongArrayQueue longArrayQueue = new LongArrayQueue();
            long uptimeMillis = PackageWatchdog.this.mSystemClock.uptimeMillis();
            for (int i = 0; i < this.mMitigationCalls.size(); i++) {
                longArrayQueue.addLast(this.mMitigationCalls.get(i) - uptimeMillis);
            }
            return longArrayQueue;
        }

        @GuardedBy({"mLock"})
        public int setHealthCheckActiveLocked(long j) {
            if (j <= 0) {
                Slog.wtf(PackageWatchdog.TAG, "Cannot set non-positive health check duration " + j + "ms for package " + getName() + ". Using total duration " + this.mDurationMs + "ms instead");
                j = this.mDurationMs;
            }
            if (this.mHealthCheckState == 1) {
                this.mHealthCheckDurationMs = j;
            }
            return updateHealthCheckStateLocked();
        }

        @GuardedBy({"mLock"})
        public int handleElapsedTimeLocked(long j) {
            if (j <= 0) {
                Slog.w(PackageWatchdog.TAG, "Cannot handle non-positive elapsed time for package " + getName());
                return this.mHealthCheckState;
            }
            this.mDurationMs -= j;
            if (this.mHealthCheckState == 0) {
                this.mHealthCheckDurationMs -= j;
            }
            return updateHealthCheckStateLocked();
        }

        @GuardedBy({"mLock"})
        public void updateHealthCheckDuration(long j) {
            this.mDurationMs = j;
        }

        @GuardedBy({"mLock"})
        public int tryPassHealthCheckLocked() {
            if (this.mHealthCheckState != 3) {
                this.mHasPassedHealthCheck = true;
            }
            return updateHealthCheckStateLocked();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getName() {
            return this.mPackageName;
        }

        @GuardedBy({"mLock"})
        public int getHealthCheckStateLocked() {
            return this.mHealthCheckState;
        }

        @GuardedBy({"mLock"})
        public long getShortestScheduleDurationMsLocked() {
            return Math.min(toPositive(this.mDurationMs), isPendingHealthChecksLocked() ? toPositive(this.mHealthCheckDurationMs) : Long.MAX_VALUE);
        }

        @GuardedBy({"mLock"})
        public boolean isExpiredLocked() {
            return this.mDurationMs <= 0;
        }

        @GuardedBy({"mLock"})
        public boolean isPendingHealthChecksLocked() {
            int i = this.mHealthCheckState;
            return i == 0 || i == 1;
        }

        @GuardedBy({"mLock"})
        private int updateHealthCheckStateLocked() {
            int i = this.mHealthCheckState;
            if (this.mHasPassedHealthCheck) {
                this.mHealthCheckState = 2;
            } else {
                long j = this.mHealthCheckDurationMs;
                if (j <= 0 || this.mDurationMs <= 0) {
                    this.mHealthCheckState = 3;
                } else if (j == Long.MAX_VALUE) {
                    this.mHealthCheckState = 1;
                } else {
                    this.mHealthCheckState = 0;
                }
            }
            if (i != this.mHealthCheckState) {
                Slog.i(PackageWatchdog.TAG, "Updated health check state for package " + getName() + ": " + toString(i) + " -> " + toString(this.mHealthCheckState));
            }
            return this.mHealthCheckState;
        }

        @VisibleForTesting
        boolean isEqualTo(MonitoredPackage monitoredPackage) {
            return getName().equals(monitoredPackage.getName()) && this.mDurationMs == monitoredPackage.mDurationMs && this.mHasPassedHealthCheck == monitoredPackage.mHasPassedHealthCheck && this.mHealthCheckDurationMs == monitoredPackage.mHealthCheckDurationMs && this.mMitigationCalls.toString().equals(monitoredPackage.mMitigationCalls.toString());
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class BootThreshold {
        private final int mBootTriggerCount;
        private final long mTriggerWindow;

        BootThreshold(int i, long j) {
            this.mBootTriggerCount = i;
            this.mTriggerWindow = j;
        }

        public void reset() {
            setStart(0L);
            setCount(0);
        }

        private int getCount() {
            return SystemProperties.getInt(PackageWatchdog.PROP_RESCUE_BOOT_COUNT, 0);
        }

        private void setCount(int i) {
            SystemProperties.set(PackageWatchdog.PROP_RESCUE_BOOT_COUNT, Integer.toString(i));
        }

        public long getStart() {
            return SystemProperties.getLong(PackageWatchdog.PROP_RESCUE_BOOT_START, 0L);
        }

        public int getMitigationCount() {
            return SystemProperties.getInt(PackageWatchdog.PROP_BOOT_MITIGATION_COUNT, 0);
        }

        public void setStart(long j) {
            setPropertyStart(PackageWatchdog.PROP_RESCUE_BOOT_START, j);
        }

        public void setMitigationStart(long j) {
            setPropertyStart(PackageWatchdog.PROP_BOOT_MITIGATION_WINDOW_START, j);
        }

        public long getMitigationStart() {
            return SystemProperties.getLong(PackageWatchdog.PROP_BOOT_MITIGATION_WINDOW_START, 0L);
        }

        public void setMitigationCount(int i) {
            SystemProperties.set(PackageWatchdog.PROP_BOOT_MITIGATION_COUNT, Integer.toString(i));
        }

        public void setPropertyStart(String str, long j) {
            SystemProperties.set(str, Long.toString(MathUtils.constrain(j, 0L, PackageWatchdog.this.mSystemClock.uptimeMillis())));
        }

        public void saveMitigationCountToMetadata() {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(PackageWatchdog.METADATA_FILE));
                try {
                    bufferedWriter.write(String.valueOf(getMitigationCount()));
                    bufferedWriter.close();
                } finally {
                }
            } catch (Exception e) {
                Slog.e(PackageWatchdog.TAG, "Could not save metadata to file: " + e);
            }
        }

        public void readMitigationCountFromMetadataIfNecessary() {
            File file = new File(PackageWatchdog.METADATA_FILE);
            if (file.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(PackageWatchdog.METADATA_FILE));
                    try {
                        setMitigationCount(Integer.parseInt(bufferedReader.readLine()));
                        file.delete();
                        bufferedReader.close();
                    } finally {
                    }
                } catch (Exception e) {
                    Slog.i(PackageWatchdog.TAG, "Could not read metadata file: " + e);
                }
            }
        }

        public boolean incrementAndTest() {
            readMitigationCountFromMetadataIfNecessary();
            long uptimeMillis = PackageWatchdog.this.mSystemClock.uptimeMillis();
            if (uptimeMillis - getStart() < 0) {
                Slog.e(PackageWatchdog.TAG, "Window was less than zero. Resetting start to current time.");
                setStart(uptimeMillis);
                setMitigationStart(uptimeMillis);
            }
            if (uptimeMillis - getMitigationStart() > PackageWatchdog.DEFAULT_DEESCALATION_WINDOW_MS) {
                setMitigationCount(0);
                setMitigationStart(uptimeMillis);
            }
            long start = uptimeMillis - getStart();
            if (start >= this.mTriggerWindow) {
                setCount(1);
                setStart(uptimeMillis);
                return false;
            }
            int count = getCount() + 1;
            setCount(count);
            EventLogTags.writeRescueNote(0, count, start);
            return count >= this.mBootTriggerCount;
        }
    }
}
