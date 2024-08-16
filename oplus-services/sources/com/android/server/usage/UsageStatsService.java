package com.android.server.usage;

import android.annotation.EnforcePermission;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.IUidObserver;
import android.app.PendingIntent;
import android.app.UidObserver;
import android.app.admin.DevicePolicyManagerInternal;
import android.app.usage.AppLaunchEstimateInfo;
import android.app.usage.AppStandbyInfo;
import android.app.usage.BroadcastResponseStatsList;
import android.app.usage.ConfigurationStats;
import android.app.usage.EventStats;
import android.app.usage.IUsageStatsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.app.usage.UsageStatsManagerInternal;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.LocusId;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ParceledListSlice;
import android.content.pm.ShortcutServiceInternal;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SparseSetArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.content.PackageMonitor;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.job.controllers.JobStatus;
import com.android.server.location.gnss.hal.GnssNative;
import com.android.server.pm.PackageManagerService;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.usage.AppStandbyInternal;
import com.android.server.usage.AppTimeLimitController;
import com.android.server.usage.UsageStatsService;
import com.android.server.usage.UserUsageStatsService;
import com.android.server.utils.AlarmQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UsageStatsService extends SystemService implements UserUsageStatsService.StatsUpdatedListener {
    private static final File COMMON_USAGE_STATS_DIR;
    static final boolean COMPRESS_TIME = false;
    static final boolean DEBUG;
    static final boolean DEBUG_RESPONSE_STATS;
    static boolean DEBUG_USAGE = false;
    private static final boolean ENABLE_KERNEL_UPDATES = true;
    public static final boolean ENABLE_TIME_CHANGE_CORRECTION = SystemProperties.getBoolean("persist.debug.time_correction", true);
    private static final long FLUSH_INTERVAL = 1200000;
    private static final String GLOBAL_COMPONENT_USAGE_FILE_NAME = "globalcomponentusage";
    private static final File KERNEL_COUNTER_FILE;
    private static final File LEGACY_COMMON_USAGE_STATS_DIR;
    private static final File LEGACY_USER_USAGE_STATS_DIR;
    static final int MSG_FLUSH_TO_DISK = 1;
    static final int MSG_HANDLE_LAUNCH_TIME_ON_USER_UNLOCK = 8;
    static final int MSG_NOTIFY_ESTIMATED_LAUNCH_TIMES_CHANGED = 9;
    static final int MSG_ON_START = 7;
    static final int MSG_PACKAGE_REMOVED = 6;
    static final int MSG_REMOVE_USER = 2;
    static final int MSG_REPORT_EVENT = 0;
    static final int MSG_REPORT_EVENT_TO_ALL_USERID = 4;
    static final int MSG_UID_STATE_CHANGED = 3;
    static final int MSG_UNLOCKED_USER = 5;
    private static final long ONE_DAY = 86400000;
    private static final long ONE_WEEK = 604800000;
    static final String TAG = "UsageStatsService";
    private static final long TEN_SECONDS = 10000;
    static final long TIME_CHANGE_THRESHOLD_MILLIS = 2000;
    private static final char TOKEN_DELIMITER = '/';
    private static final long TWENTY_MINUTES = 1200000;
    private static final long UNKNOWN_LAUNCH_TIME_DELAY_MS = 31536000000L;
    AppOpsManager mAppOps;
    AppStandbyInternal mAppStandby;
    AppTimeLimitController mAppTimeLimit;
    DevicePolicyManagerInternal mDpmInternal;
    private final CopyOnWriteArraySet<UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener> mEstimatedLaunchTimeChangedListeners;
    private final IUsageStatsServiceExt mExt;
    Handler mHandler;
    private final Injector mInjector;
    private final Map<String, Long> mLastTimeComponentUsedGlobal;

    @GuardedBy({"mLock"})
    private final SparseArray<LaunchTimeAlarmQueue> mLaunchTimeAlarmQueues;
    private final Object mLock;
    PackageManager mPackageManager;
    PackageManagerInternal mPackageManagerInternal;
    private final PackageMonitor mPackageMonitor;

    @GuardedBy({"mPendingLaunchTimeChangePackages"})
    private final SparseSetArray<String> mPendingLaunchTimeChangePackages;
    private long mRealTimeSnapshot;
    private final SparseArray<LinkedList<UsageEvents.Event>> mReportedEvents;
    private BroadcastResponseStatsTracker mResponseStatsTracker;
    ShortcutServiceInternal mShortcutServiceInternal;
    private AppStandbyInternal.AppIdleStateChangeListener mStandbyChangeListener;
    private long mSystemTimeSnapshot;
    private final IUidObserver mUidObserver;
    private final SparseIntArray mUidToKernelCounter;

    @GuardedBy({"mUsageEventListeners"})
    private final ArraySet<UsageStatsManagerInternal.UsageEventListener> mUsageEventListeners;
    final SparseArray<ArraySet<String>> mUsageReporters;
    int mUsageSource;
    UserManager mUserManager;
    private final SparseArray<UserUsageStatsService> mUserState;
    private final CopyOnWriteArraySet<Integer> mUserUnlockedStates;
    final SparseArray<ActivityData> mVisibleActivities;

    private static long calculateNextLaunchTime(boolean z, long j) {
        return j + (z ? 604800000L : 86400000L);
    }

    static {
        boolean z = true;
        boolean z2 = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        DEBUG_USAGE = z2;
        DEBUG = z2;
        if (!z2 && !Log.isLoggable(TAG, 3)) {
            z = false;
        }
        DEBUG_RESPONSE_STATS = z;
        KERNEL_COUNTER_FILE = new File("/proc/uid_procstat/set");
        File file = new File(Environment.getDataSystemDirectory(), "usagestats");
        COMMON_USAGE_STATS_DIR = file;
        LEGACY_USER_USAGE_STATS_DIR = file;
        LEGACY_COMMON_USAGE_STATS_DIR = new File(Environment.getDataSystemDeDirectory(), "usagestats");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ActivityData {
        public int lastEvent;
        private final String mTaskRootClass;
        private final String mTaskRootPackage;
        private final String mUsageSourcePackage;

        private ActivityData(String str, String str2, String str3) {
            this.lastEvent = 0;
            this.mTaskRootPackage = str;
            this.mTaskRootClass = str2;
            this.mUsageSourcePackage = str3;
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class Injector {
        Injector() {
        }

        AppStandbyInternal getAppStandbyController(Context context) {
            return AppStandbyInternal.newAppStandbyController(UsageStatsService.class.getClassLoader(), context);
        }
    }

    public UsageStatsService(Context context) {
        this(context, new Injector());
    }

    @VisibleForTesting
    UsageStatsService(Context context, Injector injector) {
        super(context);
        this.mLock = new Object();
        this.mExt = (IUsageStatsServiceExt) ExtLoader.type(IUsageStatsServiceExt.class).base(this).create();
        this.mUserState = new SparseArray<>();
        this.mUserUnlockedStates = new CopyOnWriteArraySet<>();
        this.mUidToKernelCounter = new SparseIntArray();
        this.mLastTimeComponentUsedGlobal = new ArrayMap();
        this.mPackageMonitor = new MyPackageMonitor();
        this.mReportedEvents = new SparseArray<>();
        this.mUsageReporters = new SparseArray<>();
        this.mVisibleActivities = new SparseArray<>();
        this.mLaunchTimeAlarmQueues = new SparseArray<>();
        this.mUsageEventListeners = new ArraySet<>();
        this.mEstimatedLaunchTimeChangedListeners = new CopyOnWriteArraySet<>();
        this.mPendingLaunchTimeChangePackages = new SparseSetArray<>();
        this.mStandbyChangeListener = new AppStandbyInternal.AppIdleStateChangeListener() { // from class: com.android.server.usage.UsageStatsService.1
            public void onAppIdleStateChanged(String str, int i, boolean z, int i2, int i3) {
                UsageEvents.Event event = new UsageEvents.Event(11, SystemClock.elapsedRealtime());
                event.mBucketAndReason = (i2 << 16) | (i3 & GnssNative.GNSS_AIDING_TYPE_ALL);
                event.mPackage = str;
                UsageStatsService.this.reportEventOrAddToQueue(i, event);
            }
        };
        this.mUidObserver = new UidObserver() { // from class: com.android.server.usage.UsageStatsService.3
            public void onUidStateChanged(int i, int i2, long j, int i3) {
                UsageStatsService.this.mHandler.obtainMessage(3, i, i2).sendToTarget();
            }

            public void onUidGone(int i, boolean z) {
                onUidStateChanged(i, 20, 0L, 0);
            }
        };
        this.mInjector = injector;
    }

    @SuppressLint({"AndroidFrameworkRequiresPermission"})
    public void onStart() {
        this.mAppOps = (AppOpsManager) getContext().getSystemService("appops");
        this.mUserManager = (UserManager) getContext().getSystemService("user");
        this.mPackageManager = getContext().getPackageManager();
        this.mPackageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        HandlerThread backgroundHandlerThread = this.mExt.getBackgroundHandlerThread();
        if (backgroundHandlerThread == null) {
            backgroundHandlerThread = new HandlerThread("UsageStatsServiceHandler");
            backgroundHandlerThread.start();
        }
        this.mHandler = new H(backgroundHandlerThread.getLooper());
        AppStandbyInternal appStandbyController = this.mInjector.getAppStandbyController(getContext());
        this.mAppStandby = appStandbyController;
        this.mResponseStatsTracker = new BroadcastResponseStatsTracker(appStandbyController);
        this.mAppTimeLimit = new AppTimeLimitController(getContext(), new AppTimeLimitController.TimeLimitCallbackListener() { // from class: com.android.server.usage.UsageStatsService.2
            @Override // com.android.server.usage.AppTimeLimitController.TimeLimitCallbackListener
            public void onLimitReached(int i, int i2, long j, long j2, PendingIntent pendingIntent) {
                if (pendingIntent == null) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("android.app.usage.extra.OBSERVER_ID", i);
                intent.putExtra("android.app.usage.extra.TIME_LIMIT", j);
                intent.putExtra("android.app.usage.extra.TIME_USED", j2);
                try {
                    pendingIntent.send(UsageStatsService.this.getContext(), 0, intent);
                } catch (PendingIntent.CanceledException unused) {
                    Slog.w(UsageStatsService.TAG, "Couldn't deliver callback: " + pendingIntent);
                }
            }

            @Override // com.android.server.usage.AppTimeLimitController.TimeLimitCallbackListener
            public void onSessionEnd(int i, int i2, long j, PendingIntent pendingIntent) {
                if (pendingIntent == null) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("android.app.usage.extra.OBSERVER_ID", i);
                intent.putExtra("android.app.usage.extra.TIME_USED", j);
                try {
                    pendingIntent.send(UsageStatsService.this.getContext(), 0, intent);
                } catch (PendingIntent.CanceledException unused) {
                    Slog.w(UsageStatsService.TAG, "Couldn't deliver callback: " + pendingIntent);
                }
            }
        }, this.mHandler.getLooper());
        this.mAppStandby.addListener(this.mStandbyChangeListener);
        this.mPackageMonitor.getWrapper().getExtImpl().register(getContext(), (Looper) null, UserHandle.ALL, true, new int[]{7});
        IntentFilter intentFilter = new IntentFilter("android.intent.action.USER_REMOVED");
        intentFilter.addAction("android.intent.action.USER_STARTED");
        getContext().registerReceiverAsUser(new UserActionsReceiver(), UserHandle.ALL, intentFilter, null, this.mHandler);
        getContext().registerReceiverAsUser(new UidRemovedReceiver(), UserHandle.ALL, new IntentFilter("android.intent.action.UID_REMOVED"), null, this.mHandler);
        this.mRealTimeSnapshot = SystemClock.elapsedRealtime();
        this.mSystemTimeSnapshot = System.currentTimeMillis();
        publishLocalService(UsageStatsManagerInternal.class, new LocalService());
        publishLocalService(AppStandbyInternal.class, this.mAppStandby);
        publishBinderServices();
        this.mHandler.obtainMessage(7).sendToTarget();
    }

    @VisibleForTesting
    void publishBinderServices() {
        publishBinderService("usagestats", new BinderService());
    }

    public void onBootPhase(int i) {
        this.mAppStandby.onBootPhase(i);
        if (i == 500) {
            getDpmInternal();
            getShortcutServiceInternal();
            this.mResponseStatsTracker.onSystemServicesReady(getContext());
            File file = KERNEL_COUNTER_FILE;
            if (file.exists()) {
                try {
                    ActivityManager.getService().registerUidObserver(this.mUidObserver, 3, -1, (String) null);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Slog.w(TAG, "Missing procfs interface: " + file);
            }
            readUsageSourceSetting();
        }
    }

    public void onUserStarting(SystemService.TargetUser targetUser) {
        this.mUserState.put(targetUser.getUserIdentifier(), null);
    }

    public void onUserUnlocking(SystemService.TargetUser targetUser) {
        this.mHandler.obtainMessage(5, targetUser.getUserIdentifier(), 0).sendToTarget();
    }

    public void onUserStopping(SystemService.TargetUser targetUser) {
        int userIdentifier = targetUser.getUserIdentifier();
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(Integer.valueOf(userIdentifier))) {
                persistPendingEventsLocked(userIdentifier);
                return;
            }
            UsageEvents.Event event = new UsageEvents.Event(29, SystemClock.elapsedRealtime());
            event.mPackage = PackageManagerService.PLATFORM_PACKAGE_NAME;
            reportEvent(event, userIdentifier);
            UserUsageStatsService userUsageStatsService = this.mUserState.get(userIdentifier);
            if (userUsageStatsService != null) {
                userUsageStatsService.userStopped();
            }
            this.mUserUnlockedStates.remove(Integer.valueOf(userIdentifier));
            this.mUserState.put(userIdentifier, null);
            LaunchTimeAlarmQueue launchTimeAlarmQueue = this.mLaunchTimeAlarmQueues.get(userIdentifier);
            if (launchTimeAlarmQueue != null) {
                launchTimeAlarmQueue.removeAllAlarms();
                this.mLaunchTimeAlarmQueues.remove(userIdentifier);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserUnlocked(int i) {
        HashMap<String, Long> installedPackages = getInstalledPackages(i);
        UsageStatsIdleService.scheduleUpdateMappingsJob(getContext(), i);
        boolean shouldDeleteObsoleteData = shouldDeleteObsoleteData(UserHandle.of(i));
        synchronized (this.mLock) {
            this.mUserUnlockedStates.add(Integer.valueOf(i));
            UsageEvents.Event event = new UsageEvents.Event(28, SystemClock.elapsedRealtime());
            event.mPackage = PackageManagerService.PLATFORM_PACKAGE_NAME;
            migrateStatsToSystemCeIfNeededLocked(i);
            LinkedList<UsageEvents.Event> linkedList = new LinkedList<>();
            loadPendingEventsLocked(i, linkedList);
            LinkedList<UsageEvents.Event> linkedList2 = this.mReportedEvents.get(i);
            if (linkedList2 != null) {
                linkedList.addAll(linkedList2);
            }
            boolean z = !linkedList.isEmpty();
            initializeUserUsageStatsServiceLocked(i, System.currentTimeMillis(), installedPackages, shouldDeleteObsoleteData);
            UserUsageStatsService userUsageStatsServiceLocked = getUserUsageStatsServiceLocked(i);
            if (userUsageStatsServiceLocked == null) {
                Slog.i(TAG, "Attempted to unlock stopped or removed user " + i);
                return;
            }
            while (linkedList.peek() != null) {
                reportEvent(linkedList.poll(), i);
            }
            reportEvent(event, i);
            this.mHandler.obtainMessage(8, i, 0).sendToTarget();
            this.mReportedEvents.remove(i);
            deleteRecursively(new File(Environment.getDataSystemDeDirectory(i), "usagestats"));
            if (z) {
                userUsageStatsServiceLocked.persistActiveStats();
            }
        }
    }

    private HashMap<String, Long> getInstalledPackages(int i) {
        PackageManager packageManager = this.mPackageManager;
        if (packageManager == null) {
            return null;
        }
        List installedPackagesAsUser = packageManager.getInstalledPackagesAsUser(8192, i);
        HashMap<String, Long> hashMap = new HashMap<>();
        for (int size = installedPackagesAsUser.size() - 1; size >= 0; size--) {
            PackageInfo packageInfo = (PackageInfo) installedPackagesAsUser.get(size);
            hashMap.put(packageInfo.packageName, Long.valueOf(packageInfo.firstInstallTime));
        }
        return hashMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DevicePolicyManagerInternal getDpmInternal() {
        if (this.mDpmInternal == null) {
            this.mDpmInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        }
        return this.mDpmInternal;
    }

    private ShortcutServiceInternal getShortcutServiceInternal() {
        if (this.mShortcutServiceInternal == null) {
            this.mShortcutServiceInternal = (ShortcutServiceInternal) LocalServices.getService(ShortcutServiceInternal.class);
        }
        return this.mShortcutServiceInternal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readUsageSourceSetting() {
        synchronized (this.mLock) {
            this.mUsageSource = Settings.Global.getInt(getContext().getContentResolver(), "app_time_limit_usage_source", 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class LaunchTimeAlarmQueue extends AlarmQueue<String> {
        private final int mUserId;

        LaunchTimeAlarmQueue(int i, Context context, Looper looper) {
            super(context, looper, "*usage.launchTime*", "Estimated launch times", true, 30000L);
            this.mUserId = i;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.utils.AlarmQueue
        public boolean isForUser(String str, int i) {
            return this.mUserId == i;
        }

        @Override // com.android.server.utils.AlarmQueue
        protected void processExpiredAlarms(ArraySet<String> arraySet) {
            if (UsageStatsService.DEBUG) {
                Slog.d(UsageStatsService.TAG, "Processing " + arraySet.size() + " expired alarms: " + arraySet.toString());
            }
            if (arraySet.size() > 0) {
                synchronized (UsageStatsService.this.mPendingLaunchTimeChangePackages) {
                    UsageStatsService.this.mPendingLaunchTimeChangePackages.addAll(this.mUserId, arraySet);
                }
                UsageStatsService.this.mHandler.sendEmptyMessage(9);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class UserActionsReceiver extends BroadcastReceiver {
        private UserActionsReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -1);
            String action = intent.getAction();
            if ("android.intent.action.USER_REMOVED".equals(action)) {
                if (intExtra >= 0) {
                    UsageStatsService.this.mHandler.obtainMessage(2, intExtra, 0).sendToTarget();
                    UsageStatsService.this.mResponseStatsTracker.onUserRemoved(intExtra);
                    return;
                }
                return;
            }
            if (!"android.intent.action.USER_STARTED".equals(action) || intExtra < 0) {
                return;
            }
            UsageStatsService.this.mAppStandby.postCheckIdleStates(intExtra);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class UidRemovedReceiver extends BroadcastReceiver {
        private UidRemovedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("android.intent.extra.UID", -1);
            if (intExtra == -1) {
                return;
            }
            synchronized (UsageStatsService.this.mLock) {
                UsageStatsService.this.mResponseStatsTracker.onUidRemoved(intExtra);
            }
        }
    }

    @Override // com.android.server.usage.UserUsageStatsService.StatsUpdatedListener
    public void onStatsUpdated() {
        this.mHandler.sendEmptyMessageDelayed(1, 1200000L);
    }

    @Override // com.android.server.usage.UserUsageStatsService.StatsUpdatedListener
    public void onStatsReloaded() {
        this.mAppStandby.postOneTimeCheckIdleStates();
    }

    @Override // com.android.server.usage.UserUsageStatsService.StatsUpdatedListener
    public void onNewUpdate(int i) {
        this.mAppStandby.initializeDefaultsForSystemApps(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean sameApp(int i, int i2, String str) {
        return this.mPackageManagerInternal.getPackageUid(str, 0L, i2) == i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInstantApp(String str, int i) {
        return this.mPackageManagerInternal.isPackageEphemeral(i, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldObfuscateInstantAppsForCaller(int i, int i2) {
        return !this.mPackageManagerInternal.canAccessInstantApps(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldHideShortcutInvocationEvents(int i, String str, int i2, int i3) {
        if (getShortcutServiceInternal() != null) {
            return !r1.hasShortcutHostPermission(i, str, i2, i3);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldHideLocusIdEvents(int i, int i2) {
        return (i2 == 1000 || getContext().checkPermission("android.permission.ACCESS_LOCUS_ID_USAGE_STATS", i, i2) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldObfuscateNotificationEvents(int i, int i2) {
        return (i2 == 1000 || getContext().checkPermission("android.permission.MANAGE_NOTIFICATIONS", i, i2) == 0) ? false : true;
    }

    private static void deleteRecursively(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                deleteRecursively(file2);
            }
        }
        if (!file.exists() || file.delete()) {
            return;
        }
        Slog.e(TAG, "Failed to delete " + file);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UserUsageStatsService getUserUsageStatsServiceLocked(int i) {
        UserUsageStatsService userUsageStatsService = this.mUserState.get(i);
        if (userUsageStatsService == null) {
            Slog.wtf(TAG, "Failed to fetch usage stats service for user " + i + ". The user might not have been initialized yet.");
        }
        return userUsageStatsService;
    }

    private void initializeUserUsageStatsServiceLocked(int i, long j, HashMap<String, Long> hashMap, boolean z) {
        UserUsageStatsService userUsageStatsService = new UserUsageStatsService(getContext(), i, new File(Environment.getDataSystemCeDirectory(i), "usagestats"), this);
        try {
            userUsageStatsService.init(j, hashMap, z);
            this.mUserState.put(i, userUsageStatsService);
        } catch (Exception e) {
            if (this.mUserManager.isUserUnlocked(i)) {
                Slog.w(TAG, "Failed to initialized unlocked user " + i);
                throw e;
            }
            Slog.w(TAG, "Attempted to initialize service for stopped or removed user " + i);
        }
    }

    private void migrateStatsToSystemCeIfNeededLocked(int i) {
        File file = new File(Environment.getDataSystemCeDirectory(i), "usagestats");
        if (!file.mkdirs() && !file.exists()) {
            throw new IllegalStateException("Usage stats directory does not exist: " + file.getAbsolutePath());
        }
        File file2 = new File(file, "migrated");
        if (file2.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file2));
                try {
                    if (Integer.parseInt(bufferedReader.readLine()) >= 4) {
                        deleteLegacyUserDir(i);
                        bufferedReader.close();
                        return;
                    }
                    bufferedReader.close();
                } catch (Throwable th) {
                    try {
                        bufferedReader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException | NumberFormatException e) {
                Slog.e(TAG, "Failed to read migration status file, possibly corrupted.");
                deleteRecursively(file);
                if (file.exists()) {
                    Slog.e(TAG, "Unable to delete usage stats CE directory.");
                    throw new RuntimeException(e);
                }
                if (!file.mkdirs() && !file.exists()) {
                    throw new IllegalStateException("Usage stats directory does not exist: " + file.getAbsolutePath());
                }
            }
        }
        Slog.i(TAG, "Starting migration to system CE for user " + i);
        File file3 = new File(LEGACY_USER_USAGE_STATS_DIR, Integer.toString(i));
        if (file3.exists()) {
            copyRecursively(file, file3);
        }
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file2));
            try {
                bufferedWriter.write(Integer.toString(4));
                bufferedWriter.write("\n");
                bufferedWriter.flush();
                bufferedWriter.close();
                Slog.i(TAG, "Finished migration to system CE for user " + i);
                deleteLegacyUserDir(i);
            } finally {
            }
        } catch (IOException e2) {
            Slog.e(TAG, "Failed to write migrated status file");
            throw new RuntimeException(e2);
        }
    }

    private static void copyRecursively(File file, File file2) {
        File file3;
        File[] listFiles = file2.listFiles();
        if (listFiles == null) {
            try {
                Files.copy(file2.toPath(), new File(file, file2.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                return;
            } catch (IOException e) {
                Slog.e(TAG, "Failed to move usage stats file : " + file2.toString());
                throw new RuntimeException(e);
            }
        }
        for (int length = listFiles.length - 1; length >= 0; length--) {
            if (listFiles[length].isDirectory()) {
                file3 = new File(file, listFiles[length].getName());
                if (!file3.mkdirs() && !file3.exists()) {
                    throw new IllegalStateException("Failed to create usage stats directory during migration: " + file3.getAbsolutePath());
                }
            } else {
                file3 = file;
            }
            copyRecursively(file3, listFiles[length]);
        }
    }

    private void deleteLegacyUserDir(int i) {
        File file = new File(LEGACY_USER_USAGE_STATS_DIR, Integer.toString(i));
        if (file.exists()) {
            deleteRecursively(file);
            if (file.exists()) {
                Slog.w(TAG, "Error occurred while attempting to delete legacy usage stats dir for user " + i);
            }
        }
    }

    void shutdown() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(0);
            UsageEvents.Event event = new UsageEvents.Event(26, SystemClock.elapsedRealtime());
            event.mPackage = PackageManagerService.PLATFORM_PACKAGE_NAME;
            reportEventToAllUserId(event);
            flushToDiskLocked();
            persistGlobalComponentUsageLocked();
        }
        this.mAppStandby.flushToDisk();
    }

    void prepareForPossibleShutdown() {
        UsageEvents.Event event = new UsageEvents.Event(26, SystemClock.elapsedRealtime());
        event.mPackage = PackageManagerService.PLATFORM_PACKAGE_NAME;
        this.mHandler.obtainMessage(4, event).sendToTarget();
        this.mHandler.sendEmptyMessage(1);
    }

    private void loadPendingEventsLocked(int i, LinkedList<UsageEvents.Event> linkedList) {
        File[] listFiles = new File(Environment.getDataSystemDeDirectory(i), "usagestats").listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return;
        }
        Arrays.sort(listFiles);
        int length = listFiles.length;
        for (int i2 = 0; i2 < length; i2++) {
            AtomicFile atomicFile = new AtomicFile(listFiles[i2]);
            LinkedList linkedList2 = new LinkedList();
            try {
                FileInputStream openRead = atomicFile.openRead();
                try {
                    UsageStatsProtoV2.readPendingEvents(openRead, linkedList2);
                    if (openRead != null) {
                        openRead.close();
                    }
                    linkedList.addAll(linkedList2);
                } catch (Throwable th) {
                    if (openRead != null) {
                        try {
                            openRead.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                    break;
                }
            } catch (Exception unused) {
                Slog.e(TAG, "Could not read " + listFiles[i2] + " for user " + i);
            }
        }
    }

    private void persistPendingEventsLocked(int i) {
        LinkedList<UsageEvents.Event> linkedList = this.mReportedEvents.get(i);
        if (linkedList == null || linkedList.isEmpty()) {
            return;
        }
        File dataSystemDeDirectory = Environment.getDataSystemDeDirectory(i);
        File file = new File(dataSystemDeDirectory, "usagestats");
        if (!file.mkdir() && !file.exists()) {
            if (dataSystemDeDirectory.exists()) {
                Slog.e(TAG, "Failed to create " + file);
                return;
            }
            Slog.w(TAG, "User " + i + " was already removed! Discarding pending events");
            linkedList.clear();
            return;
        }
        File file2 = new File(file, "pendingevents_" + System.currentTimeMillis());
        AtomicFile atomicFile = new AtomicFile(file2);
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream startWrite = atomicFile.startWrite();
                try {
                    UsageStatsProtoV2.writePendingEvents(startWrite, linkedList);
                    atomicFile.finishWrite(startWrite);
                    linkedList.clear();
                } catch (Exception unused) {
                    fileOutputStream = startWrite;
                    Slog.e(TAG, "Failed to write " + file2.getAbsolutePath() + " for user " + i);
                    atomicFile.failWrite(fileOutputStream);
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = startWrite;
                    atomicFile.failWrite(fileOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception unused2) {
        }
        atomicFile.failWrite(fileOutputStream);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadGlobalComponentUsageLocked() {
        AtomicFile atomicFile = new AtomicFile(new File(COMMON_USAGE_STATS_DIR, GLOBAL_COMPONENT_USAGE_FILE_NAME));
        if (!atomicFile.exists()) {
            atomicFile = new AtomicFile(new File(LEGACY_COMMON_USAGE_STATS_DIR, GLOBAL_COMPONENT_USAGE_FILE_NAME));
            if (!atomicFile.exists()) {
                return;
            } else {
                Slog.i(TAG, "Reading globalcomponentusage file from old location");
            }
        }
        ArrayMap arrayMap = new ArrayMap();
        try {
            FileInputStream openRead = atomicFile.openRead();
            try {
                UsageStatsProtoV2.readGlobalComponentUsage(openRead, arrayMap);
                if (openRead != null) {
                    openRead.close();
                }
                Map.Entry[] entryArr = (Map.Entry[]) arrayMap.entrySet().toArray();
                int length = entryArr.length;
                for (int i = 0; i < length; i++) {
                    this.mLastTimeComponentUsedGlobal.putIfAbsent((String) entryArr[i].getKey(), (Long) entryArr[i].getValue());
                }
            } finally {
            }
        } catch (Exception unused) {
            Slog.e(TAG, "Could not read " + atomicFile.getBaseFile());
        }
    }

    private void persistGlobalComponentUsageLocked() {
        if (this.mLastTimeComponentUsedGlobal.isEmpty()) {
            return;
        }
        File file = COMMON_USAGE_STATS_DIR;
        if (!file.mkdirs() && !file.exists()) {
            throw new IllegalStateException("Common usage stats directory does not exist: " + file.getAbsolutePath());
        }
        File file2 = new File(file, GLOBAL_COMPONENT_USAGE_FILE_NAME);
        AtomicFile atomicFile = new AtomicFile(file2);
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream startWrite = atomicFile.startWrite();
                try {
                    UsageStatsProtoV2.writeGlobalComponentUsage(startWrite, this.mLastTimeComponentUsedGlobal);
                    atomicFile.finishWrite(startWrite);
                } catch (Exception unused) {
                    fileOutputStream = startWrite;
                    Slog.e(TAG, "Failed to write " + file2.getAbsolutePath());
                    atomicFile.failWrite(fileOutputStream);
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = startWrite;
                    atomicFile.failWrite(fileOutputStream);
                    throw th;
                }
            } catch (Exception unused2) {
            }
            atomicFile.failWrite(fileOutputStream);
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportEventOrAddToQueue(int i, UsageEvents.Event event) {
        if (this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
            this.mHandler.obtainMessage(0, i, 0, event).sendToTarget();
            return;
        }
        synchronized (this.mLock) {
            LinkedList<UsageEvents.Event> linkedList = this.mReportedEvents.get(i);
            if (linkedList == null) {
                linkedList = new LinkedList<>();
                this.mReportedEvents.put(i, linkedList);
            }
            linkedList.add(event);
            if (linkedList.size() == 1) {
                this.mHandler.sendEmptyMessageDelayed(1, 1200000L);
            }
        }
    }

    private void convertToSystemTimeLocked(UsageEvents.Event event) {
        long currentTimeMillis = System.currentTimeMillis();
        if (ENABLE_TIME_CHANGE_CORRECTION) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j = currentTimeMillis - ((elapsedRealtime - this.mRealTimeSnapshot) + this.mSystemTimeSnapshot);
            if (Math.abs(j) > TIME_CHANGE_THRESHOLD_MILLIS) {
                Slog.i(TAG, "Time changed in by " + (j / 1000) + " seconds");
                this.mRealTimeSnapshot = elapsedRealtime;
                this.mSystemTimeSnapshot = currentTimeMillis;
            }
        }
        event.mTimeStamp = Math.max(0L, event.mTimeStamp - this.mRealTimeSnapshot) + this.mSystemTimeSnapshot;
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x023f A[Catch: all -> 0x0263, DONT_GENERATE, TryCatch #5 {, blocks: (B:14:0x002b, B:16:0x0037, B:17:0x007a, B:20:0x007c, B:31:0x0239, B:33:0x023f, B:35:0x0241, B:36:0x0244, B:50:0x0094, B:51:0x0096, B:53:0x00a2, B:54:0x00dd, B:56:0x00df, B:58:0x00e3, B:59:0x00ea, B:60:0x00ec, B:65:0x00fa, B:84:0x0132, B:85:0x0133, B:87:0x0137, B:89:0x0143, B:92:0x014f, B:96:0x015a, B:97:0x015b, B:98:0x016d, B:100:0x0179, B:102:0x017d, B:103:0x018b, B:104:0x01a3, B:106:0x01a9, B:109:0x0184, B:110:0x019c, B:111:0x01b7, B:114:0x01c9, B:116:0x01cd, B:117:0x01db, B:119:0x01fd, B:121:0x0205, B:123:0x0209, B:124:0x0223, B:126:0x0232, B:129:0x01d4, B:67:0x00fb, B:69:0x0102, B:71:0x0108, B:74:0x012b, B:76:0x0115, B:79:0x012e, B:62:0x00ed, B:63:0x00f7), top: B:13:0x002b, inners: #0, #2, #3, #6, #7 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0241 A[Catch: all -> 0x0263, TryCatch #5 {, blocks: (B:14:0x002b, B:16:0x0037, B:17:0x007a, B:20:0x007c, B:31:0x0239, B:33:0x023f, B:35:0x0241, B:36:0x0244, B:50:0x0094, B:51:0x0096, B:53:0x00a2, B:54:0x00dd, B:56:0x00df, B:58:0x00e3, B:59:0x00ea, B:60:0x00ec, B:65:0x00fa, B:84:0x0132, B:85:0x0133, B:87:0x0137, B:89:0x0143, B:92:0x014f, B:96:0x015a, B:97:0x015b, B:98:0x016d, B:100:0x0179, B:102:0x017d, B:103:0x018b, B:104:0x01a3, B:106:0x01a9, B:109:0x0184, B:110:0x019c, B:111:0x01b7, B:114:0x01c9, B:116:0x01cd, B:117:0x01db, B:119:0x01fd, B:121:0x0205, B:123:0x0209, B:124:0x0223, B:126:0x0232, B:129:0x01d4, B:67:0x00fb, B:69:0x0102, B:71:0x0108, B:74:0x012b, B:76:0x0115, B:79:0x012e, B:62:0x00ed, B:63:0x00f7), top: B:13:0x002b, inners: #0, #2, #3, #6, #7 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void reportEvent(UsageEvents.Event event, int i) {
        UserUsageStatsService userUsageStatsServiceLocked;
        ArraySet arraySet;
        int i2 = event.mEventType;
        int packageUid = (i2 == 1 || i2 == 2 || i2 == 23) ? this.mPackageManagerInternal.getPackageUid(event.mPackage, 0L, i) : 0;
        String str = event.mPackage;
        if (str != null && isInstantApp(str, i)) {
            event.mFlags |= 1;
        }
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
                Slog.wtf(TAG, "Failed to report event for locked user " + i + " (" + event.mPackage + SliceClientPermissions.SliceAuthority.DELIMITER + event.mClass + " eventType:" + event.mEventType + " instanceId:" + event.mInstanceId + ")");
                return;
            }
            int i3 = event.mEventType;
            if (i3 == 1) {
                FrameworkStatsLog.write(269, packageUid, event.mPackage, "", 1);
                if (this.mVisibleActivities.get(event.mInstanceId) == null) {
                    String usageSourcePackage = getUsageSourcePackage(event);
                    try {
                        this.mAppTimeLimit.noteUsageStart(usageSourcePackage, i);
                    } catch (IllegalArgumentException e) {
                        Slog.e(TAG, "Failed to note usage start", e);
                    }
                    ActivityData activityData = new ActivityData(event.mTaskRootPackage, event.mTaskRootClass, usageSourcePackage);
                    activityData.lastEvent = 1;
                    this.mVisibleActivities.put(event.mInstanceId, activityData);
                    long estimatedLaunchTime = this.mAppStandby.getEstimatedLaunchTime(event.mPackage, i);
                    long currentTimeMillis = System.currentTimeMillis();
                    if (estimatedLaunchTime < currentTimeMillis || estimatedLaunchTime > currentTimeMillis + 604800000) {
                        if (DEBUG) {
                            Slog.d(TAG, event.getPackageName() + " app launch resetting future launch estimate");
                        }
                        this.mAppStandby.setEstimatedLaunchTime(event.mPackage, i, 0L);
                        if (stageChangedEstimatedLaunchTime(i, event.mPackage)) {
                            this.mHandler.sendEmptyMessage(9);
                        }
                    }
                }
                userUsageStatsServiceLocked = getUserUsageStatsServiceLocked(i);
                if (userUsageStatsServiceLocked == null) {
                }
            } else if (i3 == 2) {
                ActivityData activityData2 = this.mVisibleActivities.get(event.mInstanceId);
                if (activityData2 == null) {
                    String usageSourcePackage2 = getUsageSourcePackage(event);
                    try {
                        this.mAppTimeLimit.noteUsageStart(usageSourcePackage2, i);
                    } catch (IllegalArgumentException e2) {
                        Slog.e(TAG, "Failed to note usage start", e2);
                    }
                    activityData2 = new ActivityData(event.mTaskRootPackage, event.mTaskRootClass, usageSourcePackage2);
                    this.mVisibleActivities.put(event.mInstanceId, activityData2);
                } else {
                    FrameworkStatsLog.write(269, packageUid, event.mPackage, "", 2);
                }
                activityData2.lastEvent = 2;
                if (event.mTaskRootPackage == null) {
                    event.mTaskRootPackage = activityData2.mTaskRootPackage;
                    event.mTaskRootClass = activityData2.mTaskRootClass;
                }
                userUsageStatsServiceLocked = getUserUsageStatsServiceLocked(i);
                if (userUsageStatsServiceLocked == null) {
                }
            } else {
                if (i3 != 7 && i3 != 31) {
                    if (i3 != 23) {
                        if (i3 == 24) {
                            event.mEventType = 23;
                        }
                    }
                    ActivityData activityData3 = (ActivityData) this.mVisibleActivities.removeReturnOld(event.mInstanceId);
                    if (activityData3 == null) {
                        Slog.w(TAG, "Unexpected activity event reported! (" + event.mPackage + SliceClientPermissions.SliceAuthority.DELIMITER + event.mClass + " event : " + event.mEventType + " instanceId : " + event.mInstanceId + ")");
                        return;
                    }
                    if (activityData3.lastEvent != 2) {
                        FrameworkStatsLog.write(269, packageUid, event.mPackage, "", 2);
                    }
                    synchronized (this.mUsageReporters) {
                        arraySet = (ArraySet) this.mUsageReporters.removeReturnOld(event.mInstanceId);
                    }
                    if (arraySet != null) {
                        synchronized (arraySet) {
                            int size = arraySet.size();
                            for (int i4 = 0; i4 < size; i4++) {
                                try {
                                    this.mAppTimeLimit.noteUsageStop(buildFullToken(event.mPackage, (String) arraySet.valueAt(i4)), i);
                                } catch (IllegalArgumentException e3) {
                                    Slog.w(TAG, "Failed to stop usage for during reporter death: " + e3);
                                }
                            }
                        }
                    }
                    if (event.mTaskRootPackage == null) {
                        event.mTaskRootPackage = activityData3.mTaskRootPackage;
                        event.mTaskRootClass = activityData3.mTaskRootClass;
                    }
                    try {
                        this.mAppTimeLimit.noteUsageStop(activityData3.mUsageSourcePackage, i);
                    } catch (IllegalArgumentException e4) {
                        Slog.w(TAG, "Failed to note usage stop", e4);
                    }
                } else {
                    convertToSystemTimeLocked(event);
                    this.mLastTimeComponentUsedGlobal.put(event.mPackage, Long.valueOf(event.mTimeStamp));
                }
                userUsageStatsServiceLocked = getUserUsageStatsServiceLocked(i);
                if (userUsageStatsServiceLocked == null) {
                    return;
                }
                userUsageStatsServiceLocked.reportEvent(event);
                synchronized (this.mUsageEventListeners) {
                    int size2 = this.mUsageEventListeners.size();
                    for (int i5 = 0; i5 < size2; i5++) {
                        this.mUsageEventListeners.valueAt(i5).onUsageEvent(i, event);
                    }
                }
                return;
            }
        }
    }

    private String getUsageSourcePackage(UsageEvents.Event event) {
        if (this.mUsageSource == 2) {
            return event.mPackage;
        }
        return event.mTaskRootPackage;
    }

    void reportEventToAllUserId(UsageEvents.Event event) {
        synchronized (this.mLock) {
            int size = this.mUserState.size();
            for (int i = 0; i < size; i++) {
                reportEventOrAddToQueue(this.mUserState.keyAt(i), new UsageEvents.Event(event));
            }
        }
    }

    void flushToDisk() {
        synchronized (this.mLock) {
            UsageEvents.Event event = new UsageEvents.Event(25, SystemClock.elapsedRealtime());
            event.mPackage = PackageManagerService.PLATFORM_PACKAGE_NAME;
            reportEventToAllUserId(event);
            flushToDiskLocked();
        }
        this.mAppStandby.flushToDisk();
    }

    void onUserRemoved(int i) {
        synchronized (this.mLock) {
            Slog.i(TAG, "Removing user " + i + " and all data.");
            this.mUserState.remove(i);
            this.mAppTimeLimit.onUserRemoved(i);
            LaunchTimeAlarmQueue launchTimeAlarmQueue = this.mLaunchTimeAlarmQueues.get(i);
            if (launchTimeAlarmQueue != null) {
                launchTimeAlarmQueue.removeAllAlarms();
                this.mLaunchTimeAlarmQueues.remove(i);
            }
        }
        synchronized (this.mPendingLaunchTimeChangePackages) {
            this.mPendingLaunchTimeChangePackages.remove(i);
        }
        this.mAppStandby.onUserRemoved(i);
        UsageStatsIdleService.cancelPruneJob(getContext(), i);
        UsageStatsIdleService.cancelUpdateMappingsJob(getContext(), i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageRemoved(int i, String str) {
        synchronized (this.mPendingLaunchTimeChangePackages) {
            ArraySet arraySet = this.mPendingLaunchTimeChangePackages.get(i);
            if (arraySet != null) {
                arraySet.remove(str);
            }
        }
        synchronized (this.mLock) {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
                LaunchTimeAlarmQueue launchTimeAlarmQueue = this.mLaunchTimeAlarmQueues.get(i);
                if (launchTimeAlarmQueue != null) {
                    launchTimeAlarmQueue.removeAlarmForKey(str);
                }
                UserUsageStatsService userUsageStatsService = this.mUserState.get(i);
                if (userUsageStatsService == null) {
                    return;
                }
                int onPackageRemoved = userUsageStatsService.onPackageRemoved(str, currentTimeMillis);
                if (onPackageRemoved != -1) {
                    UsageStatsIdleService.schedulePruneJob(getContext(), i);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean pruneUninstalledPackagesData(int i) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
                return false;
            }
            UserUsageStatsService userUsageStatsService = this.mUserState.get(i);
            if (userUsageStatsService == null) {
                return false;
            }
            return userUsageStatsService.pruneUninstalledPackagesData();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updatePackageMappingsData(int i) {
        if (!shouldDeleteObsoleteData(UserHandle.of(i))) {
            return true;
        }
        HashMap<String, Long> installedPackages = getInstalledPackages(i);
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
                return false;
            }
            UserUsageStatsService userUsageStatsService = this.mUserState.get(i);
            if (userUsageStatsService == null) {
                return false;
            }
            return userUsageStatsService.updatePackageMappingsLocked(installedPackages);
        }
    }

    List<UsageStats> queryUsageStats(int i, int i2, long j, long j2, boolean z) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
                Slog.w(TAG, "Failed to query usage stats for locked user " + i);
                return null;
            }
            UserUsageStatsService userUsageStatsServiceLocked = getUserUsageStatsServiceLocked(i);
            if (userUsageStatsServiceLocked == null) {
                return null;
            }
            List<UsageStats> queryUsageStats = userUsageStatsServiceLocked.queryUsageStats(i2, j, j2);
            if (queryUsageStats == null) {
                return null;
            }
            if (z) {
                for (int size = queryUsageStats.size() - 1; size >= 0; size--) {
                    UsageStats usageStats = queryUsageStats.get(size);
                    if (isInstantApp(usageStats.mPackageName, i)) {
                        queryUsageStats.set(size, usageStats.getObfuscatedForInstantApp());
                    }
                }
            }
            return queryUsageStats;
        }
    }

    List<ConfigurationStats> queryConfigurationStats(int i, int i2, long j, long j2) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
                Slog.w(TAG, "Failed to query configuration stats for locked user " + i);
                return null;
            }
            UserUsageStatsService userUsageStatsServiceLocked = getUserUsageStatsServiceLocked(i);
            if (userUsageStatsServiceLocked == null) {
                return null;
            }
            return userUsageStatsServiceLocked.queryConfigurationStats(i2, j, j2);
        }
    }

    List<EventStats> queryEventStats(int i, int i2, long j, long j2) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
                Slog.w(TAG, "Failed to query event stats for locked user " + i);
                return null;
            }
            UserUsageStatsService userUsageStatsServiceLocked = getUserUsageStatsServiceLocked(i);
            if (userUsageStatsServiceLocked == null) {
                return null;
            }
            return userUsageStatsServiceLocked.queryEventStats(i2, j, j2);
        }
    }

    UsageEvents queryEvents(int i, long j, long j2, int i2) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
                Slog.w(TAG, "Failed to query events for locked user " + i);
                return null;
            }
            UserUsageStatsService userUsageStatsServiceLocked = getUserUsageStatsServiceLocked(i);
            if (userUsageStatsServiceLocked == null) {
                return null;
            }
            return userUsageStatsServiceLocked.queryEvents(j, j2, i2);
        }
    }

    UsageEvents queryEventsForPackage(int i, long j, long j2, String str, boolean z) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
                Slog.w(TAG, "Failed to query package events for locked user " + i);
                return null;
            }
            UserUsageStatsService userUsageStatsServiceLocked = getUserUsageStatsServiceLocked(i);
            if (userUsageStatsServiceLocked == null) {
                return null;
            }
            return userUsageStatsServiceLocked.queryEventsForPackage(j, j2, str, z);
        }
    }

    private UsageEvents queryEarliestAppEvents(int i, long j, long j2, int i2) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
                Slog.w(TAG, "Failed to query earliest events for locked user " + i);
                return null;
            }
            UserUsageStatsService userUsageStatsServiceLocked = getUserUsageStatsServiceLocked(i);
            if (userUsageStatsServiceLocked == null) {
                return null;
            }
            return userUsageStatsServiceLocked.queryEarliestAppEvents(j, j2, i2);
        }
    }

    private UsageEvents queryEarliestEventsForPackage(int i, long j, long j2, String str, int i2) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
                Slog.w(TAG, "Failed to query earliest package events for locked user " + i);
                return null;
            }
            UserUsageStatsService userUsageStatsServiceLocked = getUserUsageStatsServiceLocked(i);
            if (userUsageStatsServiceLocked == null) {
                return null;
            }
            return userUsageStatsServiceLocked.queryEarliestEventsForPackage(j, j2, str, i2);
        }
    }

    long getEstimatedPackageLaunchTime(int i, String str) {
        long estimatedLaunchTime = this.mAppStandby.getEstimatedLaunchTime(str, i);
        long currentTimeMillis = System.currentTimeMillis();
        if (estimatedLaunchTime < currentTimeMillis || estimatedLaunchTime == JobStatus.NO_LATEST_RUNTIME) {
            estimatedLaunchTime = calculateEstimatedPackageLaunchTime(i, str);
            this.mAppStandby.setEstimatedLaunchTime(str, i, estimatedLaunchTime);
            synchronized (this.mLock) {
                LaunchTimeAlarmQueue launchTimeAlarmQueue = this.mLaunchTimeAlarmQueues.get(i);
                if (launchTimeAlarmQueue == null) {
                    launchTimeAlarmQueue = new LaunchTimeAlarmQueue(i, getContext(), BackgroundThread.get().getLooper());
                    this.mLaunchTimeAlarmQueues.put(i, launchTimeAlarmQueue);
                }
                launchTimeAlarmQueue.addAlarm(str, SystemClock.elapsedRealtime() + (estimatedLaunchTime - currentTimeMillis));
            }
        }
        return estimatedLaunchTime;
    }

    private long calculateEstimatedPackageLaunchTime(int i, String str) {
        synchronized (this.mLock) {
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis + 31536000000L;
            UsageEvents queryEarliestEventsForPackage = queryEarliestEventsForPackage(i, currentTimeMillis - 604800000, currentTimeMillis, str, 1);
            if (queryEarliestEventsForPackage == null) {
                if (DEBUG) {
                    Slog.d(TAG, "No events for " + i + ":" + str);
                }
                return j;
            }
            UsageEvents.Event event = new UsageEvents.Event();
            if (queryEarliestEventsForPackage.getNextEvent(event)) {
                boolean z = currentTimeMillis - event.getTimeStamp() > 86400000;
                if (DEBUG) {
                    Slog.d(TAG, i + ":" + str + " history > 24 hours=" + z);
                }
                do {
                    if (event.getEventType() == 1) {
                        long calculateNextLaunchTime = calculateNextLaunchTime(z, event.getTimeStamp());
                        if (calculateNextLaunchTime > currentTimeMillis) {
                            return calculateNextLaunchTime;
                        }
                    }
                } while (queryEarliestEventsForPackage.getNextEvent(event));
                return j;
            }
            if (DEBUG) {
                Slog.d(TAG, i + ":" + str + " has no events");
            }
            return j;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00de A[Catch: all -> 0x0130, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x0020, B:9:0x0022, B:11:0x0036, B:12:0x004c, B:14:0x0053, B:16:0x005d, B:19:0x006d, B:21:0x0071, B:22:0x0097, B:23:0x00a1, B:25:0x00a8, B:31:0x00d8, B:33:0x00de, B:35:0x00e2, B:36:0x0105, B:37:0x010e, B:39:0x0119, B:42:0x00c0, B:50:0x0127, B:51:0x012e), top: B:3:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x010c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void handleEstimatedLaunchTimesOnUserUnlock(int i) {
        UsageEvents usageEvents;
        ArrayMap arrayMap;
        long j;
        synchronized (this.mLock) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long currentTimeMillis = System.currentTimeMillis();
            UsageEvents queryEarliestAppEvents = queryEarliestAppEvents(i, currentTimeMillis - 604800000, currentTimeMillis, 1);
            if (queryEarliestAppEvents == null) {
                return;
            }
            ArrayMap arrayMap2 = new ArrayMap();
            UsageEvents.Event event = new UsageEvents.Event();
            LaunchTimeAlarmQueue launchTimeAlarmQueue = this.mLaunchTimeAlarmQueues.get(i);
            if (launchTimeAlarmQueue == null) {
                launchTimeAlarmQueue = new LaunchTimeAlarmQueue(i, getContext(), BackgroundThread.get().getLooper());
                this.mLaunchTimeAlarmQueues.put(i, launchTimeAlarmQueue);
            }
            boolean nextEvent = queryEarliestAppEvents.getNextEvent(event);
            boolean z = false;
            while (nextEvent) {
                String packageName = event.getPackageName();
                if (arrayMap2.containsKey(packageName)) {
                    usageEvents = queryEarliestAppEvents;
                } else {
                    boolean z2 = currentTimeMillis - event.getTimeStamp() > 86400000;
                    if (DEBUG) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(i);
                        usageEvents = queryEarliestAppEvents;
                        sb.append(":");
                        sb.append(packageName);
                        sb.append(" history > 24 hours=");
                        sb.append(z2);
                        Slog.d(TAG, sb.toString());
                    } else {
                        usageEvents = queryEarliestAppEvents;
                    }
                    arrayMap2.put(packageName, Boolean.valueOf(z2));
                }
                if (event.getEventType() == 1) {
                    long estimatedLaunchTime = this.mAppStandby.getEstimatedLaunchTime(packageName, i);
                    if (estimatedLaunchTime >= currentTimeMillis) {
                        if (estimatedLaunchTime == JobStatus.NO_LATEST_RUNTIME) {
                        }
                        j = 604800000;
                        if (estimatedLaunchTime >= currentTimeMillis + 604800000) {
                            if (DEBUG) {
                                StringBuilder sb2 = new StringBuilder();
                                arrayMap = arrayMap2;
                                sb2.append("User ");
                                sb2.append(i);
                                sb2.append(" unlock resulting in estimated launch time change for ");
                                sb2.append(packageName);
                                Slog.d(TAG, sb2.toString());
                            } else {
                                arrayMap = arrayMap2;
                            }
                            z = stageChangedEstimatedLaunchTime(i, packageName) | z;
                        } else {
                            arrayMap = arrayMap2;
                        }
                        launchTimeAlarmQueue.addAlarm(packageName, (estimatedLaunchTime - currentTimeMillis) + elapsedRealtime);
                    }
                    estimatedLaunchTime = calculateNextLaunchTime(((Boolean) arrayMap2.get(packageName)).booleanValue(), event.getTimeStamp());
                    this.mAppStandby.setEstimatedLaunchTime(packageName, i, estimatedLaunchTime);
                    j = 604800000;
                    if (estimatedLaunchTime >= currentTimeMillis + 604800000) {
                    }
                    launchTimeAlarmQueue.addAlarm(packageName, (estimatedLaunchTime - currentTimeMillis) + elapsedRealtime);
                } else {
                    arrayMap = arrayMap2;
                    j = 604800000;
                }
                queryEarliestAppEvents = usageEvents;
                nextEvent = queryEarliestAppEvents.getNextEvent(event);
                arrayMap2 = arrayMap;
            }
            if (z) {
                this.mHandler.sendEmptyMessage(9);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEstimatedLaunchTime(int i, String str, long j) {
        if (j > System.currentTimeMillis()) {
            if (j != this.mAppStandby.getEstimatedLaunchTime(str, i)) {
                this.mAppStandby.setEstimatedLaunchTime(str, i, j);
                if (stageChangedEstimatedLaunchTime(i, str)) {
                    this.mHandler.sendEmptyMessage(9);
                    return;
                }
                return;
            }
            return;
        }
        if (DEBUG) {
            Slog.w(TAG, "Ignoring new estimate for " + i + ":" + str + " because it's old");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEstimatedLaunchTimes(int i, List<AppLaunchEstimateInfo> list) {
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = false;
        for (int size = list.size() - 1; size >= 0; size--) {
            AppLaunchEstimateInfo appLaunchEstimateInfo = list.get(size);
            if (appLaunchEstimateInfo.estimatedLaunchTime <= currentTimeMillis) {
                if (DEBUG) {
                    Slog.w(TAG, "Ignoring new estimate for " + i + ":" + appLaunchEstimateInfo.packageName + " because it's old");
                }
            } else {
                long estimatedLaunchTime = this.mAppStandby.getEstimatedLaunchTime(appLaunchEstimateInfo.packageName, i);
                long j = appLaunchEstimateInfo.estimatedLaunchTime;
                if (j != estimatedLaunchTime) {
                    this.mAppStandby.setEstimatedLaunchTime(appLaunchEstimateInfo.packageName, i, j);
                    z |= stageChangedEstimatedLaunchTime(i, appLaunchEstimateInfo.packageName);
                }
            }
        }
        if (z) {
            this.mHandler.sendEmptyMessage(9);
        }
    }

    private boolean stageChangedEstimatedLaunchTime(int i, String str) {
        boolean add;
        synchronized (this.mPendingLaunchTimeChangePackages) {
            add = this.mPendingLaunchTimeChangePackages.add(i, str);
        }
        return add;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerListener(UsageStatsManagerInternal.UsageEventListener usageEventListener) {
        synchronized (this.mUsageEventListeners) {
            this.mUsageEventListeners.add(usageEventListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterListener(UsageStatsManagerInternal.UsageEventListener usageEventListener) {
        synchronized (this.mUsageEventListeners) {
            this.mUsageEventListeners.remove(usageEventListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerLaunchTimeChangedListener(UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener estimatedLaunchTimeChangedListener) {
        this.mEstimatedLaunchTimeChangedListeners.add(estimatedLaunchTimeChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterLaunchTimeChangedListener(UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener estimatedLaunchTimeChangedListener) {
        this.mEstimatedLaunchTimeChangedListeners.remove(estimatedLaunchTimeChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldDeleteObsoleteData(UserHandle userHandle) {
        DevicePolicyManagerInternal dpmInternal = getDpmInternal();
        return dpmInternal == null || dpmInternal.getProfileOwnerOrDeviceOwnerSupervisionComponent(userHandle) == null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String buildFullToken(String str, String str2) {
        StringBuilder sb = new StringBuilder(str.length() + str2.length() + 1);
        sb.append(str);
        sb.append(TOKEN_DELIMITER);
        sb.append(str2);
        return sb.toString();
    }

    private void flushToDiskLocked() {
        int size = this.mUserState.size();
        for (int i = 0; i < size; i++) {
            int keyAt = this.mUserState.keyAt(i);
            if (!this.mUserUnlockedStates.contains(Integer.valueOf(keyAt))) {
                persistPendingEventsLocked(keyAt);
            } else {
                UserUsageStatsService userUsageStatsService = this.mUserState.get(keyAt);
                if (userUsageStatsService != null) {
                    userUsageStatsService.persistActiveStats();
                }
            }
        }
        this.mHandler.removeMessages(1);
    }

    void dump(String[] strArr, PrintWriter printWriter) {
        boolean z;
        boolean z2;
        int[] iArr;
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        ArrayList arrayList = new ArrayList();
        int i = 0;
        if (strArr != null) {
            z = false;
            z2 = false;
            for (int i2 = 0; i2 < strArr.length; i2++) {
                String str = strArr[i2];
                if ("--checkin".equals(str)) {
                    z = true;
                } else if ("-c".equals(str)) {
                    z2 = true;
                } else {
                    if ("flush".equals(str)) {
                        synchronized (this.mLock) {
                            flushToDiskLocked();
                        }
                        this.mAppStandby.flushToDisk();
                        printWriter.println("Flushed stats to disk");
                        return;
                    }
                    if ("is-app-standby-enabled".equals(str)) {
                        printWriter.println(this.mAppStandby.isAppIdleEnabled());
                        return;
                    }
                    if ("apptimelimit".equals(str)) {
                        synchronized (this.mLock) {
                            int i3 = i2 + 1;
                            if (i3 >= strArr.length) {
                                this.mAppTimeLimit.dump(null, printWriter);
                            } else {
                                this.mAppTimeLimit.dump((String[]) Arrays.copyOfRange(strArr, i3, strArr.length), printWriter);
                            }
                        }
                        return;
                    }
                    if ("file".equals(str)) {
                        IndentingPrintWriter indentingPrintWriter2 = new IndentingPrintWriter(printWriter, "  ");
                        synchronized (this.mLock) {
                            if (i2 + 1 >= strArr.length) {
                                int size = this.mUserState.size();
                                while (i < size) {
                                    int keyAt = this.mUserState.keyAt(i);
                                    if (this.mUserUnlockedStates.contains(Integer.valueOf(keyAt))) {
                                        indentingPrintWriter2.println("user=" + keyAt);
                                        indentingPrintWriter2.increaseIndent();
                                        this.mUserState.valueAt(i).dumpFile(indentingPrintWriter2, null);
                                        indentingPrintWriter2.decreaseIndent();
                                    }
                                    i++;
                                }
                            } else {
                                int parseUserIdFromArgs = parseUserIdFromArgs(strArr, i2, indentingPrintWriter2);
                                if (parseUserIdFromArgs != -10000) {
                                    this.mUserState.get(parseUserIdFromArgs).dumpFile(indentingPrintWriter2, (String[]) Arrays.copyOfRange(strArr, i2 + 2, strArr.length));
                                }
                            }
                        }
                        return;
                    }
                    if ("database-info".equals(str)) {
                        IndentingPrintWriter indentingPrintWriter3 = new IndentingPrintWriter(printWriter, "  ");
                        synchronized (this.mLock) {
                            if (i2 + 1 >= strArr.length) {
                                int size2 = this.mUserState.size();
                                while (i < size2) {
                                    int keyAt2 = this.mUserState.keyAt(i);
                                    if (this.mUserUnlockedStates.contains(Integer.valueOf(keyAt2))) {
                                        indentingPrintWriter3.println("user=" + keyAt2);
                                        indentingPrintWriter3.increaseIndent();
                                        this.mUserState.valueAt(i).dumpDatabaseInfo(indentingPrintWriter3);
                                        indentingPrintWriter3.decreaseIndent();
                                    }
                                    i++;
                                }
                            } else {
                                int parseUserIdFromArgs2 = parseUserIdFromArgs(strArr, i2, indentingPrintWriter3);
                                if (parseUserIdFromArgs2 != -10000) {
                                    this.mUserState.get(parseUserIdFromArgs2).dumpDatabaseInfo(indentingPrintWriter3);
                                }
                            }
                        }
                        return;
                    }
                    if ("appstandby".equals(str)) {
                        printWriter.println("UsageStatsService Thread name = " + this.mHandler.getLooper().getThread().getName());
                        printWriter.println();
                        this.mAppStandby.dumpState(strArr, printWriter);
                        return;
                    }
                    if ("stats-directory".equals(str)) {
                        IndentingPrintWriter indentingPrintWriter4 = new IndentingPrintWriter(printWriter, "  ");
                        synchronized (this.mLock) {
                            int parseUserIdFromArgs3 = parseUserIdFromArgs(strArr, i2, indentingPrintWriter4);
                            if (parseUserIdFromArgs3 != -10000) {
                                indentingPrintWriter4.println(new File(Environment.getDataSystemCeDirectory(parseUserIdFromArgs3), "usagestats").getAbsolutePath());
                            }
                        }
                        return;
                    }
                    if ("mappings".equals(str)) {
                        IndentingPrintWriter indentingPrintWriter5 = new IndentingPrintWriter(printWriter, "  ");
                        synchronized (this.mLock) {
                            int parseUserIdFromArgs4 = parseUserIdFromArgs(strArr, i2, indentingPrintWriter5);
                            if (parseUserIdFromArgs4 != -10000) {
                                this.mUserState.get(parseUserIdFromArgs4).dumpMappings(indentingPrintWriter5);
                            }
                        }
                        return;
                    }
                    if ("broadcast-response-stats".equals(str)) {
                        synchronized (this.mLock) {
                            this.mResponseStatsTracker.dump(indentingPrintWriter);
                        }
                        return;
                    }
                    if ("app-component-usage".equals(str)) {
                        IndentingPrintWriter indentingPrintWriter6 = new IndentingPrintWriter(printWriter, "  ");
                        synchronized (this.mLock) {
                            if (!this.mLastTimeComponentUsedGlobal.isEmpty()) {
                                indentingPrintWriter6.println("App Component Usages:");
                                indentingPrintWriter6.increaseIndent();
                                for (String str2 : this.mLastTimeComponentUsedGlobal.keySet()) {
                                    indentingPrintWriter6.println("package=" + str2 + " lastUsed=" + UserUsageStatsService.formatDateTime(this.mLastTimeComponentUsedGlobal.get(str2).longValue(), true));
                                }
                                indentingPrintWriter6.decreaseIndent();
                            }
                        }
                        return;
                    }
                    if (str != null && !str.startsWith("-")) {
                        arrayList.add(str);
                    }
                }
            }
        } else {
            z = false;
            z2 = false;
        }
        synchronized (this.mLock) {
            int size3 = this.mUserState.size();
            iArr = new int[size3];
            while (i < size3) {
                int keyAt3 = this.mUserState.keyAt(i);
                iArr[i] = keyAt3;
                indentingPrintWriter.printPair("user", Integer.valueOf(keyAt3));
                indentingPrintWriter.println();
                indentingPrintWriter.increaseIndent();
                if (this.mUserUnlockedStates.contains(Integer.valueOf(keyAt3))) {
                    if (z) {
                        this.mUserState.valueAt(i).checkin(indentingPrintWriter);
                    } else {
                        this.mUserState.valueAt(i).dump(indentingPrintWriter, arrayList, z2);
                        indentingPrintWriter.println();
                    }
                }
                indentingPrintWriter.decreaseIndent();
                i++;
            }
            indentingPrintWriter.println();
            indentingPrintWriter.printPair("Usage Source", UsageStatsManager.usageSourceToString(this.mUsageSource));
            indentingPrintWriter.println();
            this.mAppTimeLimit.dump(null, printWriter);
            indentingPrintWriter.println();
            this.mResponseStatsTracker.dump(indentingPrintWriter);
        }
        this.mAppStandby.dumpUsers(indentingPrintWriter, iArr, arrayList);
        if (CollectionUtils.isEmpty(arrayList)) {
            printWriter.println();
            this.mAppStandby.dumpState(strArr, printWriter);
        }
    }

    private int parseUserIdFromArgs(String[] strArr, int i, IndentingPrintWriter indentingPrintWriter) {
        try {
            int parseInt = Integer.parseInt(strArr[i + 1]);
            if (this.mUserState.indexOfKey(parseInt) < 0) {
                indentingPrintWriter.println("the specified user does not exist.");
                return -10000;
            }
            if (this.mUserUnlockedStates.contains(Integer.valueOf(parseInt))) {
                return parseInt;
            }
            indentingPrintWriter.println("the specified user is currently in a locked state.");
            return -10000;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException unused) {
            indentingPrintWriter.println("invalid user specified.");
            return -10000;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class H extends Handler {
        public H(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int size;
            int keyAt;
            switch (message.what) {
                case 0:
                    UsageStatsService.this.reportEvent((UsageEvents.Event) message.obj, message.arg1);
                    return;
                case 1:
                    UsageStatsService.this.flushToDisk();
                    return;
                case 2:
                    UsageStatsService.this.onUserRemoved(message.arg1);
                    return;
                case 3:
                    int i = message.arg1;
                    int i2 = message.arg2 <= 2 ? 0 : 1;
                    synchronized (UsageStatsService.this.mUidToKernelCounter) {
                        if (i2 != UsageStatsService.this.mUidToKernelCounter.get(i, 0)) {
                            UsageStatsService.this.mUidToKernelCounter.put(i, i2);
                            try {
                                FileUtils.stringToFile(UsageStatsService.KERNEL_COUNTER_FILE, i + " " + i2);
                            } catch (IOException e) {
                                Slog.w(UsageStatsService.TAG, "Failed to update counter set: " + e);
                            }
                        }
                    }
                    return;
                case 4:
                    UsageStatsService.this.reportEventToAllUserId((UsageEvents.Event) message.obj);
                    return;
                case 5:
                    try {
                        UsageStatsService.this.onUserUnlocked(message.arg1);
                        return;
                    } catch (Exception e2) {
                        if (UsageStatsService.this.mUserManager.isUserUnlocked(message.arg1)) {
                            throw e2;
                        }
                        Slog.w(UsageStatsService.TAG, "Attempted to unlock stopped or removed user " + message.arg1);
                        return;
                    }
                case 6:
                    UsageStatsService.this.onPackageRemoved(message.arg1, (String) message.obj);
                    return;
                case 7:
                    synchronized (UsageStatsService.this.mLock) {
                        UsageStatsService.this.loadGlobalComponentUsageLocked();
                    }
                    return;
                case 8:
                    UsageStatsService.this.handleEstimatedLaunchTimesOnUserUnlock(message.arg1);
                    return;
                case 9:
                    removeMessages(9);
                    ArraySet arraySet = new ArraySet();
                    synchronized (UsageStatsService.this.mPendingLaunchTimeChangePackages) {
                        size = UsageStatsService.this.mPendingLaunchTimeChangePackages.size();
                    }
                    for (int i3 = size - 1; i3 >= 0; i3--) {
                        arraySet.clear();
                        synchronized (UsageStatsService.this.mPendingLaunchTimeChangePackages) {
                            keyAt = UsageStatsService.this.mPendingLaunchTimeChangePackages.keyAt(i3);
                            arraySet.addAll(UsageStatsService.this.mPendingLaunchTimeChangePackages.get(keyAt));
                            UsageStatsService.this.mPendingLaunchTimeChangePackages.remove(keyAt);
                        }
                        if (UsageStatsService.DEBUG) {
                            Slog.d(UsageStatsService.TAG, "Notifying listeners for " + keyAt + "-->" + arraySet);
                        }
                        for (int size2 = arraySet.size() - 1; size2 >= 0; size2--) {
                            String str = (String) arraySet.valueAt(size2);
                            long estimatedPackageLaunchTime = UsageStatsService.this.getEstimatedPackageLaunchTime(keyAt, str);
                            Iterator it = UsageStatsService.this.mEstimatedLaunchTimeChangedListeners.iterator();
                            while (it.hasNext()) {
                                ((UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener) it.next()).onEstimatedLaunchTimeChanged(keyAt, str, estimatedPackageLaunchTime);
                            }
                        }
                    }
                    return;
                default:
                    super.handleMessage(message);
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearLastUsedTimestamps(String str, int i) {
        this.mAppStandby.clearLastUsedTimestampsForTest(str, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class BinderService extends IUsageStatsManager.Stub {
        private BinderService() {
        }

        private boolean hasPermission(String str) {
            int callingUid = Binder.getCallingUid();
            if (callingUid == 1000) {
                return true;
            }
            int noteOp = UsageStatsService.this.mAppOps.noteOp(43, callingUid, str);
            return noteOp == 3 ? UsageStatsService.this.getContext().checkCallingPermission("android.permission.PACKAGE_USAGE_STATS") == 0 : noteOp == 0;
        }

        private boolean hasObserverPermission() {
            int callingUid = Binder.getCallingUid();
            DevicePolicyManagerInternal dpmInternal = UsageStatsService.this.getDpmInternal();
            if (callingUid != 1000) {
                return (dpmInternal != null && (dpmInternal.isActiveProfileOwner(callingUid) || dpmInternal.isActiveDeviceOwner(callingUid))) || UsageStatsService.this.getContext().checkCallingPermission("android.permission.OBSERVE_APP_USAGE") == 0;
            }
            return true;
        }

        private boolean hasPermissions(String... strArr) {
            if (Binder.getCallingUid() == 1000) {
                return true;
            }
            Context context = UsageStatsService.this.getContext();
            boolean z = true;
            for (String str : strArr) {
                z = z && context.checkCallingPermission(str) == 0;
            }
            return z;
        }

        private void checkCallerIsSystemOrSameApp(String str) {
            if (isCallingUidSystem()) {
                return;
            }
            checkCallerIsSameApp(str);
        }

        private void checkCallerIsSameApp(String str) {
            int callingUid = Binder.getCallingUid();
            if (UsageStatsService.this.mPackageManagerInternal.getPackageUid(str, 0L, UserHandle.getUserId(callingUid)) == callingUid) {
                return;
            }
            throw new SecurityException("Calling uid " + callingUid + " cannot query eventsfor package " + str);
        }

        private boolean isCallingUidSystem() {
            return UserHandle.getAppId(Binder.getCallingUid()) == 1000;
        }

        public ParceledListSlice<UsageStats> queryUsageStats(int i, long j, long j2, String str, int i2) {
            if (!hasPermission(str)) {
                return null;
            }
            int callingUid = Binder.getCallingUid();
            int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), callingUid, i2, false, true, "queryUsageStats", str);
            boolean shouldObfuscateInstantAppsForCaller = UsageStatsService.this.shouldObfuscateInstantAppsForCaller(callingUid, UserHandle.getCallingUserId());
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                List<UsageStats> queryUsageStats = UsageStatsService.this.queryUsageStats(handleIncomingUser, i, j, j2, shouldObfuscateInstantAppsForCaller);
                if (queryUsageStats != null) {
                    return new ParceledListSlice<>(queryUsageStats);
                }
                return null;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public ParceledListSlice<ConfigurationStats> queryConfigurationStats(int i, long j, long j2, String str) throws RemoteException {
            if (!hasPermission(str)) {
                return null;
            }
            int callingUserId = UserHandle.getCallingUserId();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                List<ConfigurationStats> queryConfigurationStats = UsageStatsService.this.queryConfigurationStats(callingUserId, i, j, j2);
                if (queryConfigurationStats != null) {
                    return new ParceledListSlice<>(queryConfigurationStats);
                }
                return null;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public ParceledListSlice<EventStats> queryEventStats(int i, long j, long j2, String str) throws RemoteException {
            if (!hasPermission(str)) {
                return null;
            }
            int callingUserId = UserHandle.getCallingUserId();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                List<EventStats> queryEventStats = UsageStatsService.this.queryEventStats(callingUserId, i, j, j2);
                if (queryEventStats != null) {
                    return new ParceledListSlice<>(queryEventStats);
                }
                return null;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v2 */
        /* JADX WARN: Type inference failed for: r3v3 */
        /* JADX WARN: Type inference failed for: r3v4 */
        /* JADX WARN: Type inference failed for: r3v8 */
        /* JADX WARN: Type inference failed for: r3v9 */
        public UsageEvents queryEvents(long j, long j2, String str) {
            if (!hasPermission(str)) {
                return null;
            }
            int callingUserId = UserHandle.getCallingUserId();
            int callingUid = Binder.getCallingUid();
            int callingPid = Binder.getCallingPid();
            boolean shouldObfuscateInstantAppsForCaller = UsageStatsService.this.shouldObfuscateInstantAppsForCaller(callingUid, callingUserId);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                boolean shouldHideShortcutInvocationEvents = UsageStatsService.this.shouldHideShortcutInvocationEvents(callingUserId, str, callingPid, callingUid);
                boolean shouldHideLocusIdEvents = UsageStatsService.this.shouldHideLocusIdEvents(callingPid, callingUid);
                boolean shouldObfuscateNotificationEvents = UsageStatsService.this.shouldObfuscateNotificationEvents(callingPid, callingUid);
                ?? r3 = shouldObfuscateInstantAppsForCaller;
                if (shouldHideShortcutInvocationEvents) {
                    r3 = (shouldObfuscateInstantAppsForCaller ? 1 : 0) | 2;
                }
                if (shouldHideLocusIdEvents) {
                    r3 = (r3 == true ? 1 : 0) | '\b';
                }
                return UsageStatsService.this.queryEvents(callingUserId, j, j2, shouldObfuscateNotificationEvents ? r3 | 4 : r3);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public UsageEvents queryEventsForPackage(long j, long j2, String str) {
            int userId = UserHandle.getUserId(Binder.getCallingUid());
            checkCallerIsSameApp(str);
            boolean hasPermission = hasPermission(str);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return UsageStatsService.this.queryEventsForPackage(userId, j, j2, str, hasPermission);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v10 */
        /* JADX WARN: Type inference failed for: r3v3 */
        /* JADX WARN: Type inference failed for: r3v4 */
        /* JADX WARN: Type inference failed for: r3v5 */
        /* JADX WARN: Type inference failed for: r3v9 */
        public UsageEvents queryEventsForUser(long j, long j2, int i, String str) {
            if (!hasPermission(str)) {
                return null;
            }
            int callingUserId = UserHandle.getCallingUserId();
            if (i != callingUserId) {
                UsageStatsService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "No permission to query usage stats for this user");
            }
            int callingUid = Binder.getCallingUid();
            int callingPid = Binder.getCallingPid();
            boolean shouldObfuscateInstantAppsForCaller = UsageStatsService.this.shouldObfuscateInstantAppsForCaller(callingUid, callingUserId);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                boolean shouldHideShortcutInvocationEvents = UsageStatsService.this.shouldHideShortcutInvocationEvents(i, str, callingPid, callingUid);
                boolean shouldObfuscateNotificationEvents = UsageStatsService.this.shouldObfuscateNotificationEvents(callingPid, callingUid);
                boolean shouldHideLocusIdEvents = UsageStatsService.this.shouldHideLocusIdEvents(callingPid, callingUid);
                ?? r3 = shouldObfuscateInstantAppsForCaller;
                if (shouldHideShortcutInvocationEvents) {
                    r3 = (shouldObfuscateInstantAppsForCaller ? 1 : 0) | 2;
                }
                if (shouldHideLocusIdEvents) {
                    r3 = (r3 == true ? 1 : 0) | '\b';
                }
                return UsageStatsService.this.queryEvents(i, j, j2, shouldObfuscateNotificationEvents ? r3 | 4 : r3);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public UsageEvents queryEventsForPackageForUser(long j, long j2, int i, String str, String str2) {
            if (!hasPermission(str2)) {
                return null;
            }
            if (i != UserHandle.getCallingUserId()) {
                UsageStatsService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "No permission to query usage stats for this user");
            }
            checkCallerIsSystemOrSameApp(str);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return UsageStatsService.this.queryEventsForPackage(i, j, j2, str, true);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public boolean isAppStandbyEnabled() {
            return UsageStatsService.this.mAppStandby.isAppIdleEnabled();
        }

        public boolean isAppInactive(String str, int i, String str2) {
            int callingUid = Binder.getCallingUid();
            try {
                int handleIncomingUser = ActivityManager.getService().handleIncomingUser(Binder.getCallingPid(), callingUid, i, false, false, "isAppInactive", (String) null);
                if (str.equals(str2)) {
                    if (UsageStatsService.this.mPackageManagerInternal.getPackageUid(str2, 0L, handleIncomingUser) != callingUid) {
                        return false;
                    }
                } else if (!hasPermission(str2)) {
                    return false;
                }
                boolean shouldObfuscateInstantAppsForCaller = UsageStatsService.this.shouldObfuscateInstantAppsForCaller(callingUid, handleIncomingUser);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return UsageStatsService.this.mAppStandby.isAppIdleFiltered(str, handleIncomingUser, SystemClock.elapsedRealtime(), shouldObfuscateInstantAppsForCaller);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void setAppInactive(String str, boolean z, int i) {
            try {
                int handleIncomingUser = ActivityManager.getService().handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, false, true, "setAppInactive", (String) null);
                UsageStatsService.this.getContext().enforceCallingPermission("android.permission.CHANGE_APP_IDLE_STATE", "No permission to change app idle state");
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (UsageStatsService.this.mAppStandby.getAppId(str) < 0) {
                        return;
                    }
                    UsageStatsService.this.mAppStandby.setAppIdleAsync(str, z, handleIncomingUser);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public int getAppStandbyBucket(String str, String str2, int i) {
            int callingUid = Binder.getCallingUid();
            try {
                int handleIncomingUser = ActivityManager.getService().handleIncomingUser(Binder.getCallingPid(), callingUid, i, false, false, "getAppStandbyBucket", (String) null);
                int packageUid = UsageStatsService.this.mPackageManagerInternal.getPackageUid(str, 0L, handleIncomingUser);
                boolean z = packageUid == callingUid;
                if (!z && !hasPermission(str2)) {
                    throw new SecurityException("Don't have permission to query app standby bucket");
                }
                boolean isInstantApp = UsageStatsService.this.isInstantApp(str, handleIncomingUser);
                boolean shouldObfuscateInstantAppsForCaller = UsageStatsService.this.shouldObfuscateInstantAppsForCaller(callingUid, handleIncomingUser);
                if (packageUid < 0 || (!z && isInstantApp && shouldObfuscateInstantAppsForCaller)) {
                    throw new IllegalArgumentException("Cannot get standby bucket for non existent package (" + str + ")");
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return UsageStatsService.this.mAppStandby.getAppStandbyBucket(str, handleIncomingUser, SystemClock.elapsedRealtime(), false);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        @EnforcePermission("android.permission.CHANGE_APP_IDLE_STATE")
        public void setAppStandbyBucket(String str, int i, int i2) {
            super.setAppStandbyBucket_enforcePermission();
            int callingUid = Binder.getCallingUid();
            int callingPid = Binder.getCallingPid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                UsageStatsService.this.mAppStandby.setAppStandbyBucket(str, i, i2, callingUid, callingPid);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public ParceledListSlice<AppStandbyInfo> getAppStandbyBuckets(String str, int i) {
            final int callingUid = Binder.getCallingUid();
            try {
                final int handleIncomingUser = ActivityManager.getService().handleIncomingUser(Binder.getCallingPid(), callingUid, i, false, false, "getAppStandbyBucket", (String) null);
                if (!hasPermission(str)) {
                    throw new SecurityException("Don't have permission to query app standby bucket");
                }
                final boolean shouldObfuscateInstantAppsForCaller = UsageStatsService.this.shouldObfuscateInstantAppsForCaller(callingUid, handleIncomingUser);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    List appStandbyBuckets = UsageStatsService.this.mAppStandby.getAppStandbyBuckets(handleIncomingUser);
                    if (appStandbyBuckets == null) {
                        return ParceledListSlice.emptyList();
                    }
                    appStandbyBuckets.removeIf(new Predicate() { // from class: com.android.server.usage.UsageStatsService$BinderService$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            boolean lambda$getAppStandbyBuckets$0;
                            lambda$getAppStandbyBuckets$0 = UsageStatsService.BinderService.this.lambda$getAppStandbyBuckets$0(callingUid, handleIncomingUser, shouldObfuscateInstantAppsForCaller, (AppStandbyInfo) obj);
                            return lambda$getAppStandbyBuckets$0;
                        }
                    });
                    return new ParceledListSlice<>(appStandbyBuckets);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$getAppStandbyBuckets$0(int i, int i2, boolean z, AppStandbyInfo appStandbyInfo) {
            return !UsageStatsService.this.sameApp(i, i2, appStandbyInfo.mPackageName) && UsageStatsService.this.isInstantApp(appStandbyInfo.mPackageName, i2) && z;
        }

        @EnforcePermission("android.permission.CHANGE_APP_IDLE_STATE")
        public void setAppStandbyBuckets(ParceledListSlice parceledListSlice, int i) {
            super.setAppStandbyBuckets_enforcePermission();
            int callingUid = Binder.getCallingUid();
            int callingPid = Binder.getCallingPid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                UsageStatsService.this.mAppStandby.setAppStandbyBuckets(parceledListSlice.getList(), i, callingUid, callingPid);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int getAppMinStandbyBucket(String str, String str2, int i) {
            int callingUid = Binder.getCallingUid();
            try {
                int handleIncomingUser = ActivityManager.getService().handleIncomingUser(Binder.getCallingPid(), callingUid, i, false, false, "getAppStandbyBucket", (String) null);
                int packageUid = UsageStatsService.this.mPackageManagerInternal.getPackageUid(str, 0L, handleIncomingUser);
                if (packageUid != callingUid && !hasPermission(str2)) {
                    throw new SecurityException("Don't have permission to query min app standby bucket");
                }
                boolean isInstantApp = UsageStatsService.this.isInstantApp(str, handleIncomingUser);
                boolean shouldObfuscateInstantAppsForCaller = UsageStatsService.this.shouldObfuscateInstantAppsForCaller(callingUid, handleIncomingUser);
                if (packageUid < 0 || (isInstantApp && shouldObfuscateInstantAppsForCaller)) {
                    throw new IllegalArgumentException("Cannot get min standby bucket for non existent package (" + str + ")");
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return UsageStatsService.this.mAppStandby.getAppMinStandbyBucket(str, UserHandle.getAppId(packageUid), handleIncomingUser, false);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        @EnforcePermission("android.permission.CHANGE_APP_LAUNCH_TIME_ESTIMATE")
        public void setEstimatedLaunchTime(String str, long j, int i) {
            super.setEstimatedLaunchTime_enforcePermission();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                UsageStatsService.this.setEstimatedLaunchTime(i, str, j);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        @EnforcePermission("android.permission.CHANGE_APP_LAUNCH_TIME_ESTIMATE")
        public void setEstimatedLaunchTimes(ParceledListSlice parceledListSlice, int i) {
            super.setEstimatedLaunchTimes_enforcePermission();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                UsageStatsService.this.setEstimatedLaunchTimes(i, parceledListSlice.getList());
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void onCarrierPrivilegedAppsChanged() {
            if (UsageStatsService.DEBUG) {
                Slog.i(UsageStatsService.TAG, "Carrier privileged apps changed");
            }
            UsageStatsService.this.getContext().enforceCallingOrSelfPermission("android.permission.BIND_CARRIER_SERVICES", "onCarrierPrivilegedAppsChanged can only be called by privileged apps.");
            UsageStatsService.this.mAppStandby.clearCarrierPrivilegedApps();
        }

        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpAndUsageStatsPermission(UsageStatsService.this.getContext(), UsageStatsService.TAG, printWriter)) {
                UsageStatsService.this.dump(strArr, printWriter);
            }
        }

        public void reportChooserSelection(String str, int i, String str2, String[] strArr, String str3) {
            if (str == null) {
                throw new IllegalArgumentException("Package selection must not be null.");
            }
            if (str2 == null || str2.isBlank() || str3 == null || str3.isBlank()) {
                return;
            }
            if (UsageStatsService.this.mPackageManagerInternal.getPackageUid(str, 0L, i) < 0) {
                Slog.w(UsageStatsService.TAG, "Event report user selecting an invalid package");
                return;
            }
            UsageEvents.Event event = new UsageEvents.Event(9, SystemClock.elapsedRealtime());
            event.mPackage = str;
            event.mAction = str3;
            event.mContentType = str2;
            event.mContentAnnotations = strArr;
            UsageStatsService.this.reportEventOrAddToQueue(i, event);
        }

        public void reportUserInteraction(String str, int i) {
            Objects.requireNonNull(str);
            if (!isCallingUidSystem()) {
                throw new SecurityException("Only system is allowed to call reportUserInteraction");
            }
            UsageEvents.Event event = new UsageEvents.Event(7, SystemClock.elapsedRealtime());
            event.mPackage = str;
            UsageStatsService.this.reportEventOrAddToQueue(i, event);
        }

        public void registerAppUsageObserver(int i, String[] strArr, long j, PendingIntent pendingIntent, String str) {
            if (!hasObserverPermission()) {
                throw new SecurityException("Caller doesn't have OBSERVE_APP_USAGE permission");
            }
            if (strArr == null || strArr.length == 0) {
                throw new IllegalArgumentException("Must specify at least one package");
            }
            if (pendingIntent == null) {
                throw new NullPointerException("callbackIntent can't be null");
            }
            int callingUid = Binder.getCallingUid();
            int userId = UserHandle.getUserId(callingUid);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                UsageStatsService.this.registerAppUsageObserver(callingUid, i, strArr, j, pendingIntent, userId);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void unregisterAppUsageObserver(int i, String str) {
            if (!hasObserverPermission()) {
                throw new SecurityException("Caller doesn't have OBSERVE_APP_USAGE permission");
            }
            int callingUid = Binder.getCallingUid();
            int userId = UserHandle.getUserId(callingUid);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                UsageStatsService.this.unregisterAppUsageObserver(callingUid, i, userId);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void registerUsageSessionObserver(int i, String[] strArr, long j, long j2, PendingIntent pendingIntent, PendingIntent pendingIntent2, String str) {
            if (!hasObserverPermission()) {
                throw new SecurityException("Caller doesn't have OBSERVE_APP_USAGE permission");
            }
            if (strArr == null || strArr.length == 0) {
                throw new IllegalArgumentException("Must specify at least one observed entity");
            }
            if (pendingIntent == null) {
                throw new NullPointerException("limitReachedCallbackIntent can't be null");
            }
            int callingUid = Binder.getCallingUid();
            int userId = UserHandle.getUserId(callingUid);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                UsageStatsService.this.registerUsageSessionObserver(callingUid, i, strArr, j, j2, pendingIntent, pendingIntent2, userId);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void unregisterUsageSessionObserver(int i, String str) {
            if (!hasObserverPermission()) {
                throw new SecurityException("Caller doesn't have OBSERVE_APP_USAGE permission");
            }
            int callingUid = Binder.getCallingUid();
            int userId = UserHandle.getUserId(callingUid);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                UsageStatsService.this.unregisterUsageSessionObserver(callingUid, i, userId);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void registerAppUsageLimitObserver(int i, String[] strArr, long j, long j2, PendingIntent pendingIntent, String str) {
            int callingUid = Binder.getCallingUid();
            DevicePolicyManagerInternal dpmInternal = UsageStatsService.this.getDpmInternal();
            if (!hasPermissions("android.permission.SUSPEND_APPS", "android.permission.OBSERVE_APP_USAGE") && (dpmInternal == null || !dpmInternal.isActiveSupervisionApp(callingUid))) {
                throw new SecurityException("Caller must be the active supervision app or it must have both SUSPEND_APPS and OBSERVE_APP_USAGE permissions");
            }
            if (strArr == null || strArr.length == 0) {
                throw new IllegalArgumentException("Must specify at least one package");
            }
            if (pendingIntent == null && j2 < j) {
                throw new NullPointerException("callbackIntent can't be null");
            }
            int userId = UserHandle.getUserId(callingUid);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                UsageStatsService.this.registerAppUsageLimitObserver(callingUid, i, strArr, j, j2, pendingIntent, userId);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void unregisterAppUsageLimitObserver(int i, String str) {
            int callingUid = Binder.getCallingUid();
            DevicePolicyManagerInternal dpmInternal = UsageStatsService.this.getDpmInternal();
            if (!hasPermissions("android.permission.SUSPEND_APPS", "android.permission.OBSERVE_APP_USAGE") && (dpmInternal == null || !dpmInternal.isActiveSupervisionApp(callingUid))) {
                throw new SecurityException("Caller must be the active supervision app or it must have both SUSPEND_APPS and OBSERVE_APP_USAGE permissions");
            }
            int userId = UserHandle.getUserId(callingUid);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                UsageStatsService.this.unregisterAppUsageLimitObserver(callingUid, i, userId);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void reportUsageStart(IBinder iBinder, String str, String str2) {
            reportPastUsageStart(iBinder, str, 0L, str2);
        }

        public void reportPastUsageStart(IBinder iBinder, String str, long j, String str2) {
            ArraySet<String> arraySet;
            int userId = UserHandle.getUserId(Binder.getCallingUid());
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (UsageStatsService.this.mUsageReporters) {
                    arraySet = UsageStatsService.this.mUsageReporters.get(iBinder.hashCode());
                    if (arraySet == null) {
                        arraySet = new ArraySet<>();
                        UsageStatsService.this.mUsageReporters.put(iBinder.hashCode(), arraySet);
                    }
                }
                synchronized (arraySet) {
                    if (!arraySet.add(str)) {
                        throw new IllegalArgumentException(str + " for " + str2 + " is already reported as started for this activity");
                    }
                }
                UsageStatsService usageStatsService = UsageStatsService.this;
                usageStatsService.mAppTimeLimit.noteUsageStart(usageStatsService.buildFullToken(str2, str), userId, j);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void reportUsageStop(IBinder iBinder, String str, String str2) {
            ArraySet<String> arraySet;
            int userId = UserHandle.getUserId(Binder.getCallingUid());
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (UsageStatsService.this.mUsageReporters) {
                    arraySet = UsageStatsService.this.mUsageReporters.get(iBinder.hashCode());
                    if (arraySet == null) {
                        throw new IllegalArgumentException("Unknown reporter trying to stop token " + str + " for " + str2);
                    }
                }
                synchronized (arraySet) {
                    if (!arraySet.remove(str)) {
                        throw new IllegalArgumentException(str + " for " + str2 + " is already reported as stopped for this activity");
                    }
                }
                UsageStatsService usageStatsService = UsageStatsService.this;
                usageStatsService.mAppTimeLimit.noteUsageStop(usageStatsService.buildFullToken(str2, str), userId);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int getUsageSource() {
            int i;
            if (!hasObserverPermission()) {
                throw new SecurityException("Caller doesn't have OBSERVE_APP_USAGE permission");
            }
            synchronized (UsageStatsService.this.mLock) {
                i = UsageStatsService.this.mUsageSource;
            }
            return i;
        }

        public void forceUsageSourceSettingRead() {
            UsageStatsService.this.readUsageSourceSetting();
        }

        public long getLastTimeAnyComponentUsed(String str, String str2) {
            long millis;
            if (!hasPermissions("android.permission.INTERACT_ACROSS_USERS")) {
                throw new SecurityException("Caller doesn't have INTERACT_ACROSS_USERS permission");
            }
            if (!hasPermission(str2)) {
                throw new SecurityException("Don't have permission to query usage stats");
            }
            synchronized (UsageStatsService.this.mLock) {
                long longValue = ((Long) UsageStatsService.this.mLastTimeComponentUsedGlobal.getOrDefault(str, 0L)).longValue();
                TimeUnit timeUnit = TimeUnit.DAYS;
                millis = (longValue / timeUnit.toMillis(1L)) * timeUnit.toMillis(1L);
            }
            return millis;
        }

        public BroadcastResponseStatsList queryBroadcastResponseStats(String str, long j, String str2, int i) {
            Objects.requireNonNull(str2);
            if (j < 0) {
                throw new IllegalArgumentException("id needs to be >=0");
            }
            UsageStatsService.this.getContext().enforceCallingOrSelfPermission("android.permission.ACCESS_BROADCAST_RESPONSE_STATS", "queryBroadcastResponseStats");
            int callingUid = Binder.getCallingUid();
            return new BroadcastResponseStatsList(UsageStatsService.this.mResponseStatsTracker.queryBroadcastResponseStats(callingUid, str, j, ActivityManager.handleIncomingUser(Binder.getCallingPid(), callingUid, i, false, false, "queryBroadcastResponseStats", str2)));
        }

        public void clearBroadcastResponseStats(String str, long j, String str2, int i) {
            Objects.requireNonNull(str2);
            if (j < 0) {
                throw new IllegalArgumentException("id needs to be >=0");
            }
            UsageStatsService.this.getContext().enforceCallingOrSelfPermission("android.permission.ACCESS_BROADCAST_RESPONSE_STATS", "clearBroadcastResponseStats");
            int callingUid = Binder.getCallingUid();
            UsageStatsService.this.mResponseStatsTracker.clearBroadcastResponseStats(callingUid, str, j, ActivityManager.handleIncomingUser(Binder.getCallingPid(), callingUid, i, false, false, "clearBroadcastResponseStats", str2));
        }

        public void clearBroadcastEvents(String str, int i) {
            Objects.requireNonNull(str);
            UsageStatsService.this.getContext().enforceCallingOrSelfPermission("android.permission.ACCESS_BROADCAST_RESPONSE_STATS", "clearBroadcastEvents");
            int callingUid = Binder.getCallingUid();
            UsageStatsService.this.mResponseStatsTracker.clearBroadcastEvents(callingUid, ActivityManager.handleIncomingUser(Binder.getCallingPid(), callingUid, i, false, false, "clearBroadcastResponseStats", str));
        }

        public String getAppStandbyConstant(String str) {
            Objects.requireNonNull(str);
            if (!hasPermissions("android.permission.READ_DEVICE_CONFIG")) {
                throw new SecurityException("Caller doesn't have READ_DEVICE_CONFIG permission");
            }
            return UsageStatsService.this.mAppStandby.getAppStandbyConstant(str);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public int handleShellCommand(ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2, ParcelFileDescriptor parcelFileDescriptor3, String[] strArr) {
            return new UsageStatsShellCommand(UsageStatsService.this).exec(this, parcelFileDescriptor.getFileDescriptor(), parcelFileDescriptor2.getFileDescriptor(), parcelFileDescriptor3.getFileDescriptor(), strArr);
        }
    }

    void registerAppUsageObserver(int i, int i2, String[] strArr, long j, PendingIntent pendingIntent, int i3) {
        this.mAppTimeLimit.addAppUsageObserver(i, i2, strArr, j, pendingIntent, i3);
    }

    void unregisterAppUsageObserver(int i, int i2, int i3) {
        this.mAppTimeLimit.removeAppUsageObserver(i, i2, i3);
    }

    void registerUsageSessionObserver(int i, int i2, String[] strArr, long j, long j2, PendingIntent pendingIntent, PendingIntent pendingIntent2, int i3) {
        this.mAppTimeLimit.addUsageSessionObserver(i, i2, strArr, j, j2, pendingIntent, pendingIntent2, i3);
    }

    void unregisterUsageSessionObserver(int i, int i2, int i3) {
        this.mAppTimeLimit.removeUsageSessionObserver(i, i2, i3);
    }

    void registerAppUsageLimitObserver(int i, int i2, String[] strArr, long j, long j2, PendingIntent pendingIntent, int i3) {
        this.mAppTimeLimit.addAppUsageLimitObserver(i, i2, strArr, j, j2, pendingIntent, i3);
    }

    void unregisterAppUsageLimitObserver(int i, int i2, int i3) {
        this.mAppTimeLimit.removeAppUsageLimitObserver(i, i2, i3);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class LocalService extends UsageStatsManagerInternal {
        public void reportAppJobState(String str, int i, int i2, long j) {
        }

        private LocalService() {
        }

        public void reportEvent(ComponentName componentName, int i, int i2, int i3, ComponentName componentName2) {
            if (componentName == null) {
                Slog.w(UsageStatsService.TAG, "Event reported without a component name");
                return;
            }
            UsageEvents.Event event = new UsageEvents.Event(i2, SystemClock.elapsedRealtime());
            event.mPackage = componentName.getPackageName();
            event.mClass = componentName.getClassName();
            event.mInstanceId = i3;
            if (componentName2 == null) {
                event.mTaskRootPackage = null;
                event.mTaskRootClass = null;
            } else {
                event.mTaskRootPackage = componentName2.getPackageName();
                event.mTaskRootClass = componentName2.getClassName();
            }
            UsageStatsService.this.reportEventOrAddToQueue(i, event);
        }

        public void reportEvent(String str, int i, int i2) {
            if (str == null) {
                Slog.w(UsageStatsService.TAG, "Event reported without a package name, eventType:" + i2);
                return;
            }
            UsageEvents.Event event = new UsageEvents.Event(i2, SystemClock.elapsedRealtime());
            event.mPackage = str;
            UsageStatsService.this.reportEventOrAddToQueue(i, event);
        }

        public void reportConfigurationChange(Configuration configuration, int i) {
            if (configuration == null) {
                Slog.w(UsageStatsService.TAG, "Configuration event reported with a null config");
                return;
            }
            UsageEvents.Event event = new UsageEvents.Event(5, SystemClock.elapsedRealtime());
            event.mPackage = PackageManagerService.PLATFORM_PACKAGE_NAME;
            event.mConfiguration = new Configuration(configuration);
            UsageStatsService.this.reportEventOrAddToQueue(i, event);
        }

        public void reportInterruptiveNotification(String str, String str2, int i) {
            if (str == null || str2 == null) {
                Slog.w(UsageStatsService.TAG, "Event reported without a package name or a channel ID");
                return;
            }
            UsageEvents.Event event = new UsageEvents.Event(12, SystemClock.elapsedRealtime());
            event.mPackage = str.intern();
            event.mNotificationChannelId = str2.intern();
            UsageStatsService.this.reportEventOrAddToQueue(i, event);
        }

        public void reportShortcutUsage(String str, String str2, int i) {
            if (str == null || str2 == null) {
                Slog.w(UsageStatsService.TAG, "Event reported without a package name or a shortcut ID");
                return;
            }
            UsageEvents.Event event = new UsageEvents.Event(8, SystemClock.elapsedRealtime());
            event.mPackage = str.intern();
            event.mShortcutId = str2.intern();
            UsageStatsService.this.reportEventOrAddToQueue(i, event);
        }

        public void reportLocusUpdate(ComponentName componentName, int i, LocusId locusId, IBinder iBinder) {
            if (locusId == null) {
                return;
            }
            UsageEvents.Event event = new UsageEvents.Event(30, SystemClock.elapsedRealtime());
            event.mLocusId = locusId.getId();
            event.mPackage = componentName.getPackageName();
            event.mClass = componentName.getClassName();
            event.mInstanceId = iBinder.hashCode();
            UsageStatsService.this.reportEventOrAddToQueue(i, event);
        }

        public void reportContentProviderUsage(String str, String str2, int i) {
            UsageStatsService.this.mAppStandby.postReportContentProviderUsage(str, str2, i);
        }

        public boolean isAppIdle(String str, int i, int i2) {
            return UsageStatsService.this.mAppStandby.isAppIdleFiltered(str, i, i2, SystemClock.elapsedRealtime());
        }

        public int getAppStandbyBucket(String str, int i, long j) {
            return UsageStatsService.this.mAppStandby.getAppStandbyBucket(str, i, j, false);
        }

        public int[] getIdleUidsForUser(int i) {
            return UsageStatsService.this.mAppStandby.getIdleUidsForUser(i);
        }

        public void prepareShutdown() {
            UsageStatsService.this.shutdown();
        }

        public void prepareForPossibleShutdown() {
            UsageStatsService.this.prepareForPossibleShutdown();
        }

        public byte[] getBackupPayload(int i, String str) {
            if (!UsageStatsService.this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
                Slog.w(UsageStatsService.TAG, "Failed to get backup payload for locked user " + i);
                return null;
            }
            synchronized (UsageStatsService.this.mLock) {
                UserUsageStatsService userUsageStatsServiceLocked = UsageStatsService.this.getUserUsageStatsServiceLocked(i);
                if (userUsageStatsServiceLocked == null) {
                    return null;
                }
                Slog.i(UsageStatsService.TAG, "Returning backup payload for u=" + i);
                return userUsageStatsServiceLocked.getBackupPayload(str);
            }
        }

        public void applyRestoredPayload(int i, String str, byte[] bArr) {
            synchronized (UsageStatsService.this.mLock) {
                if (!UsageStatsService.this.mUserUnlockedStates.contains(Integer.valueOf(i))) {
                    Slog.w(UsageStatsService.TAG, "Failed to apply restored payload for locked user " + i);
                    return;
                }
                UserUsageStatsService userUsageStatsServiceLocked = UsageStatsService.this.getUserUsageStatsServiceLocked(i);
                if (userUsageStatsServiceLocked == null) {
                    return;
                }
                UsageStatsService.this.mAppStandby.restoreAppsToRare(userUsageStatsServiceLocked.applyRestoredPayload(str, bArr), i);
            }
        }

        public List<UsageStats> queryUsageStatsForUser(int i, int i2, long j, long j2, boolean z) {
            return UsageStatsService.this.queryUsageStats(i, i2, j, j2, z);
        }

        public UsageEvents queryEventsForUser(int i, long j, long j2, int i2) {
            return UsageStatsService.this.queryEvents(i, j, j2, i2);
        }

        public void setLastJobRunTime(String str, int i, long j) {
            UsageStatsService.this.mAppStandby.setLastJobRunTime(str, i, j);
        }

        public long getEstimatedPackageLaunchTime(String str, int i) {
            return UsageStatsService.this.getEstimatedPackageLaunchTime(i, str);
        }

        public long getTimeSinceLastJobRun(String str, int i) {
            return UsageStatsService.this.mAppStandby.getTimeSinceLastJobRun(str, i);
        }

        public void onActiveAdminAdded(String str, int i) {
            UsageStatsService.this.mAppStandby.addActiveDeviceAdmin(str, i);
        }

        public void setActiveAdminApps(Set<String> set, int i) {
            UsageStatsService.this.mAppStandby.setActiveAdminApps(set, i);
        }

        public void setAdminProtectedPackages(Set<String> set, int i) {
            UsageStatsService.this.mAppStandby.setAdminProtectedPackages(set, i);
        }

        public void onAdminDataAvailable() {
            UsageStatsService.this.mAppStandby.onAdminDataAvailable();
        }

        public void reportSyncScheduled(String str, int i, boolean z) {
            UsageStatsService.this.mAppStandby.postReportSyncScheduled(str, i, z);
        }

        public void reportExemptedSyncStart(String str, int i) {
            UsageStatsService.this.mAppStandby.postReportExemptedSyncStart(str, i);
        }

        public UsageStatsManagerInternal.AppUsageLimitData getAppUsageLimit(String str, UserHandle userHandle) {
            return UsageStatsService.this.mAppTimeLimit.getAppUsageLimit(str, userHandle);
        }

        public boolean pruneUninstalledPackagesData(int i) {
            return UsageStatsService.this.pruneUninstalledPackagesData(i);
        }

        public boolean updatePackageMappingsData(int i) {
            return UsageStatsService.this.updatePackageMappingsData(i);
        }

        public void registerListener(UsageStatsManagerInternal.UsageEventListener usageEventListener) {
            UsageStatsService.this.registerListener(usageEventListener);
        }

        public void unregisterListener(UsageStatsManagerInternal.UsageEventListener usageEventListener) {
            UsageStatsService.this.unregisterListener(usageEventListener);
        }

        public void registerLaunchTimeChangedListener(UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener estimatedLaunchTimeChangedListener) {
            UsageStatsService.this.registerLaunchTimeChangedListener(estimatedLaunchTimeChangedListener);
        }

        public void unregisterLaunchTimeChangedListener(UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener estimatedLaunchTimeChangedListener) {
            UsageStatsService.this.unregisterLaunchTimeChangedListener(estimatedLaunchTimeChangedListener);
        }

        public void reportBroadcastDispatched(int i, String str, UserHandle userHandle, long j, long j2, int i2) {
            UsageStatsService.this.mResponseStatsTracker.reportBroadcastDispatchEvent(i, str, userHandle, j, j2, i2);
        }

        public void reportNotificationPosted(String str, UserHandle userHandle, long j) {
            UsageStatsService.this.mResponseStatsTracker.reportNotificationPosted(str, userHandle, j);
        }

        public void reportNotificationUpdated(String str, UserHandle userHandle, long j) {
            UsageStatsService.this.mResponseStatsTracker.reportNotificationUpdated(str, userHandle, j);
        }

        public void reportNotificationRemoved(String str, UserHandle userHandle, long j) {
            UsageStatsService.this.mResponseStatsTracker.reportNotificationCancelled(str, userHandle, j);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class MyPackageMonitor extends PackageMonitor {
        private MyPackageMonitor() {
        }

        public void onPackageRemoved(String str, int i) {
            int changingUserId = getChangingUserId();
            if (UsageStatsService.this.shouldDeleteObsoleteData(UserHandle.of(changingUserId))) {
                UsageStatsService.this.mHandler.obtainMessage(6, changingUserId, 0, str).sendToTarget();
            }
            UsageStatsService.this.mResponseStatsTracker.onPackageRemoved(str, UserHandle.getUserId(i));
            super.onPackageRemoved(str, i);
        }
    }
}
