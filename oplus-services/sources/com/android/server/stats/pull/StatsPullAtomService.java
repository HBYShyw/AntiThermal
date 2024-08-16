package com.android.server.stats.pull;

import android.app.ActivityManagerInternal;
import android.app.AppOpsManager;
import android.app.INotificationManager;
import android.app.PendingIntentStats;
import android.app.ProcessMemoryState;
import android.app.RuntimeAppOpAccessMessage;
import android.app.StatsManager;
import android.app.usage.NetworkStatsManager;
import android.bluetooth.BluetoothActivityEnergyInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.UidTraffic;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IncrementalStatesInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.PermissionInfo;
import android.content.pm.UserInfo;
import android.hardware.display.DisplayManager;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.health.HealthInfo;
import android.icu.util.TimeZone;
import android.media.AudioManager;
import android.media.MediaDrm;
import android.media.UnsupportedSchemeException;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.NetworkStats;
import android.net.NetworkTemplate;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryStatsInternal;
import android.os.BatteryStatsManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.CoolingDevice;
import android.os.Debug;
import android.os.Environment;
import android.os.IBinder;
import android.os.IStoraged;
import android.os.IThermalEventListener;
import android.os.IThermalService;
import android.os.OutcomeReceiver;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.os.StatFs;
import android.os.SynchronousResultReceiver;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Temperature;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.connectivity.WifiActivityEnergyInfo;
import android.os.incremental.IncrementalManager;
import android.os.storage.DiskInfo;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.security.metrics.IKeystoreMetrics;
import android.security.metrics.KeyCreationWithAuthInfo;
import android.security.metrics.KeyCreationWithGeneralInfo;
import android.security.metrics.KeyCreationWithPurposeAndModesInfo;
import android.security.metrics.KeyOperationWithGeneralInfo;
import android.security.metrics.KeyOperationWithPurposeAndModesInfo;
import android.security.metrics.KeystoreAtom;
import android.security.metrics.RkpErrorStats;
import android.security.metrics.StorageStats;
import android.telephony.ModemActivityInfo;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.MathUtils;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.StatsEvent;
import android.util.proto.ProtoOutputStream;
import android.uwb.UwbActivityEnergyInfo;
import android.uwb.UwbManager;
import android.view.Display;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.procstats.IProcessStats;
import com.android.internal.app.procstats.ProcessStats;
import com.android.internal.app.procstats.StatsEventOutput;
import com.android.internal.os.BackgroundThread;
import com.android.internal.os.BinderCallsStats;
import com.android.internal.os.KernelAllocationStats;
import com.android.internal.os.KernelCpuBpfTracking;
import com.android.internal.os.KernelCpuThreadReader;
import com.android.internal.os.KernelCpuThreadReaderDiff;
import com.android.internal.os.KernelCpuThreadReaderSettingsObserver;
import com.android.internal.os.KernelCpuTotalBpfMapReader;
import com.android.internal.os.KernelCpuUidTimeReader;
import com.android.internal.os.KernelSingleProcessCpuThreadReader;
import com.android.internal.os.LooperStats;
import com.android.internal.os.PowerProfile;
import com.android.internal.os.ProcessCpuTracker;
import com.android.internal.os.SelectedProcessCpuThreadReader;
import com.android.internal.os.StoragedUidIoStatsReader;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.ConcurrentUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.net.module.util.NetworkStatsUtils;
import com.android.role.RoleManagerLocal;
import com.android.server.BinderCallsStatsService;
import com.android.server.IOplusNecConnectMonitor;
import com.android.server.LocalManagerRegistry;
import com.android.server.LocalServices;
import com.android.server.OplusServiceFactory;
import com.android.server.PinnerService;
import com.android.server.SystemServerInitThreadPool$;
import com.android.server.SystemService;
import com.android.server.SystemServiceManager;
import com.android.server.am.MemoryStatUtil;
import com.android.server.health.HealthServiceWrapper;
import com.android.server.job.JobSchedulerShellCommand;
import com.android.server.job.controllers.JobStatus;
import com.android.server.notification.OplusNotificationListenerService;
import com.android.server.pm.UserManagerInternal;
import com.android.server.power.stats.KernelWakelockReader;
import com.android.server.power.stats.KernelWakelockStats;
import com.android.server.power.stats.SystemServerCpuThreadReader;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.stats.pull.IonMemoryUtil;
import com.android.server.stats.pull.ProcfsMemoryUtil;
import com.android.server.stats.pull.StatsPullAtomService;
import com.android.server.stats.pull.SystemMemoryUtil;
import com.android.server.stats.pull.netstats.NetworkStatsExt;
import com.android.server.stats.pull.netstats.SubInfo;
import com.android.server.storage.DiskStatsFileLogger;
import com.android.server.storage.DiskStatsLoggingService;
import com.android.server.timezonedetector.MetricsTimeZoneDetectorState;
import com.android.server.timezonedetector.TimeZoneDetectorInternal;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import libcore.io.IoUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class StatsPullAtomService extends SystemService {
    private static final long APP_OPS_SAMPLING_INITIALIZATION_DELAY_MILLIS = 45000;
    private static final int APP_OPS_SIZE_ESTIMATE = 2000;
    private static final String APP_OPS_TARGET_COLLECTION_SIZE = "app_ops_target_collection_size";
    private static final String COMMON_PERMISSION_PREFIX = "android.permission.";
    private static final int CPU_CYCLES_PER_UID_CLUSTER_VALUES = 3;
    private static final int CPU_TIME_PER_THREAD_FREQ_MAX_NUM_FREQUENCIES = 8;
    private static final String DANGEROUS_PERMISSION_STATE_SAMPLE_RATE = "dangerous_permission_state_sample_rate";
    private static final boolean DEBUG = true;
    private static final int DIMENSION_KEY_SIZE_HARD_LIMIT = 800;
    private static final int DIMENSION_KEY_SIZE_SOFT_LIMIT = 500;
    private static final long EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS = 2000;
    private static final int MAX_PROCSTATS_RAW_SHARD_SIZE = 58982;
    private static final int MAX_PROCSTATS_SHARDS = 5;
    private static final int MAX_PROCSTATS_SHARD_SIZE = 49152;
    private static final long MILLIS_PER_SEC = 1000;
    private static final long MILLI_AMP_HR_TO_NANO_AMP_SECS = 3600000000L;
    private static final int MIN_CPU_TIME_PER_UID_FREQ = 10;
    private static final int NET_STACK_THRE = 1000;
    private static final int OP_FLAGS_PULLED = 9;
    private static final String RESULT_RECEIVER_CONTROLLER_KEY = "controller_activity";
    private static final int SYSTEM_UID = 1000;
    private static final String TAG = "StatsPullAtomService";
    private final Object mAppOpsLock;

    @GuardedBy({"mAttributedAppOpsLock"})
    private int mAppOpsSamplingRate;
    private final Object mAppSizeLock;
    private final Object mAppsOnExternalStorageInfoLock;
    private final Object mAttributedAppOpsLock;

    @GuardedBy({"mProcStatsLock"})
    private File mBaseDir;
    private final Object mBinderCallsStatsExceptionsLock;
    private final Object mBinderCallsStatsLock;
    private final Object mBluetoothActivityInfoLock;
    private final Object mBluetoothBytesTransferLock;
    private final Object mBuildInformationLock;
    private final Object mCategorySizeLock;
    private final Context mContext;
    private final Object mCooldownDeviceLock;
    private final Object mCpuActiveTimeLock;
    private final Object mCpuClusterTimeLock;
    private final Object mCpuTimePerClusterFreqLock;
    private final Object mCpuTimePerThreadFreqLock;
    private final Object mCpuTimePerUidFreqLock;
    private final Object mCpuTimePerUidLock;

    @GuardedBy({"mCpuActiveTimeLock"})
    private KernelCpuUidTimeReader.KernelCpuUidActiveTimeReader mCpuUidActiveTimeReader;

    @GuardedBy({"mClusterTimeLock"})
    private KernelCpuUidTimeReader.KernelCpuUidClusterTimeReader mCpuUidClusterTimeReader;

    @GuardedBy({"mCpuTimePerUidFreqLock"})
    private KernelCpuUidTimeReader.KernelCpuUidFreqTimeReader mCpuUidFreqTimeReader;

    @GuardedBy({"mCpuTimePerUidLock"})
    private KernelCpuUidTimeReader.KernelCpuUidUserSysTimeReader mCpuUidUserSysTimeReader;

    @GuardedBy({"mDangerousAppOpsListLock"})
    private final ArraySet<Integer> mDangerousAppOpsList;
    private final Object mDangerousAppOpsListLock;
    private final Object mDangerousPermissionStateLock;
    private final Object mDataBytesTransferLock;
    private final Object mDebugElapsedClockLock;

    @GuardedBy({"mDebugElapsedClockLock"})
    private long mDebugElapsedClockPreviousValue;

    @GuardedBy({"mDebugElapsedClockLock"})
    private long mDebugElapsedClockPullCount;
    private final Object mDebugFailingElapsedClockLock;

    @GuardedBy({"mDebugFailingElapsedClockLock"})
    private long mDebugFailingElapsedClockPreviousValue;

    @GuardedBy({"mDebugFailingElapsedClockLock"})
    private long mDebugFailingElapsedClockPullCount;
    private final Object mDeviceCalculatedPowerUseLock;
    private final Object mDirectoryUsageLock;
    private final Object mDiskIoLock;
    private final Object mDiskStatsLock;
    private final Object mExternalStorageInfoLock;
    private final Object mFaceSettingsLock;
    private final Object mHealthHalLock;

    @GuardedBy({"mHealthHalLock"})
    private HealthServiceWrapper mHealthService;

    @GuardedBy({"mDataBytesTransferLock"})
    private final ArrayList<SubInfo> mHistoricalSubs;

    @GuardedBy({"mKeystoreLock"})
    private IKeystoreMetrics mIKeystoreMetrics;
    private final Object mInstalledIncrementalPackagesLock;
    private final Object mIonHeapSizeLock;

    @GuardedBy({"mCpuTimePerThreadFreqLock"})
    private KernelCpuThreadReaderDiff mKernelCpuThreadReader;
    private final Object mKernelWakelockLock;

    @GuardedBy({"mKernelWakelockLock"})
    private KernelWakelockReader mKernelWakelockReader;
    private final Object mKeystoreLock;
    private final Object mLooperStatsLock;
    private final Object mModemActivityInfoLock;

    @GuardedBy({"mDataBytesTransferLock"})
    private final ArrayList<NetworkStatsExt> mNetworkStatsBaselines;
    private NetworkStatsManager mNetworkStatsManager;

    @GuardedBy({"mNotificationStatsLock"})
    private INotificationManager mNotificationManagerService;
    private final Object mNotificationRemoteViewsLock;
    private final Object mNotificationStatsLock;
    private final Object mNumBiometricsEnrolledLock;
    private final Object mPowerProfileLock;
    private final Object mProcStatsLock;
    private final Object mProcessCpuTimeLock;

    @GuardedBy({"mProcessCpuTimeLock"})
    private ProcessCpuTracker mProcessCpuTracker;
    private final Object mProcessMemoryHighWaterMarkLock;
    private final Object mProcessMemoryStateLock;

    @GuardedBy({"mProcStatsLock"})
    private IProcessStats mProcessStatsService;
    private final Object mProcessSystemIonHeapSizeLock;
    private final Object mRoleHolderLock;
    private final Object mRuntimeAppOpAccessMessageLock;
    private final Object mSettingsStatsLock;
    private StatsPullAtomCallbackImpl mStatsCallbackImpl;
    private StatsManager mStatsManager;
    private StatsSubscriptionsListener mStatsSubscriptionsListener;
    private StorageManager mStorageManager;

    @GuardedBy({"mStoragedLock"})
    private IStoraged mStorageService;
    private final Object mStoragedLock;

    @GuardedBy({"mDiskIoLock"})
    private StoragedUidIoStatsReader mStoragedUidIoStatsReader;
    private SubscriptionManager mSubscriptionManager;
    private SelectedProcessCpuThreadReader mSurfaceFlingerProcessCpuThreadReader;
    private final Object mSystemElapsedRealtimeLock;
    private final Object mSystemIonHeapSizeLock;
    private final Object mSystemUptimeLock;
    private TelephonyManager mTelephony;
    private final Object mTemperatureLock;
    private final Object mThermalLock;

    @GuardedBy({"mThermalLock"})
    private IThermalService mThermalService;
    private final Object mTimeZoneDataInfoLock;
    private final Object mTimeZoneDetectionInfoLock;

    @GuardedBy({"mKernelWakelockLock"})
    private KernelWakelockStats mTmpWakelockStats;
    private final Object mUwbActivityInfoLock;
    private UwbManager mUwbManager;
    private final Object mWifiActivityInfoLock;
    private WifiManager mWifiManager;
    private boolean netStackError;
    private static final int RANDOM_SEED = new Random().nextInt();
    private static final long NETSTATS_UID_DEFAULT_BUCKET_DURATION_MS = TimeUnit.HOURS.toMillis(2);

    private int convertToAccessibilityShortcutType(int i) {
        if (i == 0) {
            return 1;
        }
        if (i != 1) {
            return i != 2 ? 0 : 6;
        }
        return 5;
    }

    private static int convertToMetricsDetectionMode(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 3;
            if (i != 2) {
                return i != 3 ? 0 : 2;
            }
        }
        return i2;
    }

    private native void initializeNativePullers();

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$countAccessibilityServices$26(int i) {
        return i == 58;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$hasDolbyVisionIssue$23(int i) {
        return i == 1;
    }

    private long milliAmpHrsToNanoAmpSecs(double d) {
        return (long) ((d * 3.6E9d) + 0.5d);
    }

    public void onStart() {
    }

    public StatsPullAtomService(Context context) {
        super(context);
        this.mThermalLock = new Object();
        this.mStoragedLock = new Object();
        this.mNotificationStatsLock = new Object();
        this.mDebugElapsedClockPreviousValue = 0L;
        this.mDebugElapsedClockPullCount = 0L;
        this.mDebugFailingElapsedClockPreviousValue = 0L;
        this.mDebugFailingElapsedClockPullCount = 0L;
        this.mAppOpsSamplingRate = 0;
        this.mDangerousAppOpsListLock = new Object();
        this.mDangerousAppOpsList = new ArraySet<>();
        this.mNetworkStatsBaselines = new ArrayList<>();
        this.mHistoricalSubs = new ArrayList<>();
        this.netStackError = false;
        this.mDataBytesTransferLock = new Object();
        this.mBluetoothBytesTransferLock = new Object();
        this.mKernelWakelockLock = new Object();
        this.mCpuTimePerClusterFreqLock = new Object();
        this.mCpuTimePerUidLock = new Object();
        this.mCpuTimePerUidFreqLock = new Object();
        this.mCpuActiveTimeLock = new Object();
        this.mCpuClusterTimeLock = new Object();
        this.mWifiActivityInfoLock = new Object();
        this.mModemActivityInfoLock = new Object();
        this.mBluetoothActivityInfoLock = new Object();
        this.mUwbActivityInfoLock = new Object();
        this.mSystemElapsedRealtimeLock = new Object();
        this.mSystemUptimeLock = new Object();
        this.mProcessMemoryStateLock = new Object();
        this.mProcessMemoryHighWaterMarkLock = new Object();
        this.mSystemIonHeapSizeLock = new Object();
        this.mIonHeapSizeLock = new Object();
        this.mProcessSystemIonHeapSizeLock = new Object();
        this.mTemperatureLock = new Object();
        this.mCooldownDeviceLock = new Object();
        this.mBinderCallsStatsLock = new Object();
        this.mBinderCallsStatsExceptionsLock = new Object();
        this.mLooperStatsLock = new Object();
        this.mDiskStatsLock = new Object();
        this.mDirectoryUsageLock = new Object();
        this.mAppSizeLock = new Object();
        this.mCategorySizeLock = new Object();
        this.mNumBiometricsEnrolledLock = new Object();
        this.mProcStatsLock = new Object();
        this.mDiskIoLock = new Object();
        this.mPowerProfileLock = new Object();
        this.mProcessCpuTimeLock = new Object();
        this.mCpuTimePerThreadFreqLock = new Object();
        this.mDeviceCalculatedPowerUseLock = new Object();
        this.mDebugElapsedClockLock = new Object();
        this.mDebugFailingElapsedClockLock = new Object();
        this.mBuildInformationLock = new Object();
        this.mRoleHolderLock = new Object();
        this.mTimeZoneDataInfoLock = new Object();
        this.mTimeZoneDetectionInfoLock = new Object();
        this.mExternalStorageInfoLock = new Object();
        this.mAppsOnExternalStorageInfoLock = new Object();
        this.mFaceSettingsLock = new Object();
        this.mAppOpsLock = new Object();
        this.mRuntimeAppOpAccessMessageLock = new Object();
        this.mNotificationRemoteViewsLock = new Object();
        this.mDangerousPermissionStateLock = new Object();
        this.mHealthHalLock = new Object();
        this.mAttributedAppOpsLock = new Object();
        this.mSettingsStatsLock = new Object();
        this.mInstalledIncrementalPackagesLock = new Object();
        this.mKeystoreLock = new Object();
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class StatsPullAtomCallbackImpl implements StatsManager.StatsPullAtomCallback {
        private StatsPullAtomCallbackImpl() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Failed to find 'out' block for switch in B:25:0x0045. Please report as an issue. */
        /* JADX WARN: Failed to find 'out' block for switch in B:26:0x0048. Please report as an issue. */
        /* JADX WARN: Failed to find 'out' block for switch in B:27:0x004b. Please report as an issue. */
        /* JADX WARN: Removed duplicated region for block: B:479:0x03f8 A[Catch: all -> 0x04f4, TryCatch #36 {all -> 0x04f4, blocks: (B:30:0x0054, B:31:0x006a, B:33:0x006b, B:34:0x0071, B:43:0x007f, B:44:0x0080, B:45:0x0086, B:54:0x0094, B:55:0x0095, B:56:0x009b, B:65:0x00a9, B:66:0x00aa, B:67:0x00b0, B:76:0x00be, B:77:0x00bf, B:78:0x00c5, B:87:0x00d3, B:88:0x00d4, B:89:0x00da, B:98:0x00e8, B:99:0x00e9, B:100:0x00ef, B:109:0x00fd, B:110:0x00fe, B:111:0x0104, B:120:0x0113, B:121:0x0114, B:122:0x011a, B:131:0x0128, B:132:0x0129, B:133:0x012f, B:142:0x013d, B:143:0x013e, B:144:0x0144, B:153:0x0152, B:154:0x0153, B:155:0x0159, B:164:0x0167, B:165:0x0168, B:168:0x0172, B:169:0x0178, B:178:0x0186, B:179:0x0187, B:182:0x0191, B:183:0x0197, B:192:0x01a5, B:193:0x01a6, B:194:0x01ac, B:203:0x01ba, B:204:0x01bb, B:207:0x01c5, B:210:0x01cf, B:213:0x01d9, B:216:0x01e3, B:219:0x01ed, B:222:0x01f7, B:223:0x01fd, B:232:0x020b, B:233:0x020c, B:236:0x0216, B:239:0x0220, B:240:0x0226, B:249:0x0234, B:250:0x0235, B:253:0x023f, B:254:0x0245, B:263:0x0253, B:264:0x0254, B:265:0x025a, B:274:0x0268, B:275:0x0269, B:278:0x0273, B:279:0x0279, B:288:0x0287, B:289:0x0288, B:290:0x028e, B:299:0x029c, B:300:0x029d, B:303:0x02a7, B:304:0x02ad, B:313:0x02bb, B:314:0x02bc, B:315:0x02c2, B:324:0x02d0, B:325:0x02d1, B:326:0x02d7, B:335:0x02e5, B:336:0x02e6, B:337:0x02ec, B:346:0x02fa, B:347:0x02fb, B:348:0x0301, B:357:0x030f, B:358:0x0310, B:359:0x0316, B:368:0x0324, B:369:0x0325, B:370:0x032b, B:379:0x033a, B:380:0x033b, B:381:0x0341, B:390:0x034f, B:391:0x0350, B:392:0x0356, B:401:0x0364, B:402:0x0365, B:403:0x036b, B:412:0x0379, B:413:0x037a, B:414:0x0380, B:423:0x038e, B:424:0x038f, B:425:0x0395, B:434:0x03a3, B:435:0x03a4, B:436:0x03aa, B:445:0x03b8, B:446:0x03b9, B:447:0x03bf, B:456:0x03cd, B:457:0x03ce, B:458:0x03d4, B:467:0x03e2, B:468:0x03e3, B:469:0x03e9, B:478:0x03f7, B:479:0x03f8, B:480:0x03fe, B:489:0x040c, B:490:0x040d, B:491:0x0413, B:500:0x0421, B:501:0x0422, B:502:0x0428, B:511:0x0436, B:512:0x0437, B:513:0x043d, B:522:0x044b, B:523:0x044c, B:524:0x0452, B:533:0x0460, B:534:0x0461, B:535:0x0467, B:544:0x0475, B:545:0x0476, B:546:0x047c, B:555:0x048a, B:556:0x048b, B:557:0x0491, B:566:0x049f, B:567:0x04a0, B:568:0x04a6, B:577:0x04b4, B:578:0x04b5, B:579:0x04bb, B:588:0x04c9, B:589:0x04ca, B:590:0x04d0, B:599:0x04de, B:600:0x04df, B:601:0x04e5, B:610:0x04f3, B:611:0x04f7, B:612:0x04fd, B:621:0x050b, B:622:0x050c, B:623:0x0512, B:632:0x0520, B:633:0x0521, B:634:0x0527, B:643:0x0535, B:644:0x0536, B:645:0x053c, B:654:0x054a, B:655:0x054b, B:656:0x0551, B:665:0x055f, B:666:0x0560, B:667:0x0566, B:676:0x0574, B:677:0x0575, B:678:0x057b, B:687:0x0589, B:688:0x058a, B:689:0x0590, B:698:0x059e, B:427:0x0396, B:428:0x039c, B:361:0x0317, B:362:0x031d, B:185:0x0198, B:186:0x019e, B:124:0x011b, B:125:0x0121, B:58:0x009c, B:59:0x00a2, B:570:0x04a7, B:571:0x04ad, B:242:0x0227, B:243:0x022d, B:636:0x0528, B:637:0x052e, B:504:0x0429, B:505:0x042f, B:438:0x03ab, B:439:0x03b1, B:372:0x032c, B:373:0x0333, B:196:0x01ad, B:197:0x01b3, B:306:0x02ae, B:307:0x02b4, B:135:0x0130, B:136:0x0136, B:69:0x00b1, B:70:0x00b7, B:581:0x04bc, B:582:0x04c2, B:647:0x053d, B:648:0x0543, B:515:0x043e, B:516:0x0444, B:449:0x03c0, B:450:0x03c6, B:383:0x0342, B:384:0x0348, B:317:0x02c3, B:318:0x02c9, B:146:0x0145, B:147:0x014b, B:256:0x0246, B:257:0x024c, B:80:0x00c6, B:81:0x00cc, B:592:0x04d1, B:593:0x04d7, B:658:0x0552, B:659:0x0558, B:526:0x0453, B:527:0x0459, B:460:0x03d5, B:461:0x03db, B:394:0x0357, B:395:0x035d, B:328:0x02d8, B:329:0x02de, B:157:0x015a, B:158:0x0160, B:267:0x025b, B:268:0x0261, B:91:0x00db, B:92:0x00e1, B:603:0x04e6, B:604:0x04ec, B:669:0x0567, B:670:0x056d, B:537:0x0468, B:538:0x046e, B:471:0x03ea, B:472:0x03f0, B:405:0x036c, B:406:0x0372, B:339:0x02ed, B:340:0x02f3, B:102:0x00f0, B:103:0x00f6, B:36:0x0072, B:37:0x0078, B:171:0x0179, B:172:0x017f, B:281:0x027a, B:282:0x0280, B:680:0x057c, B:681:0x0582, B:548:0x047d, B:549:0x0483, B:614:0x04fe, B:615:0x0504, B:225:0x01fe, B:226:0x0204, B:482:0x03ff, B:483:0x0405, B:416:0x0381, B:417:0x0387, B:350:0x0302, B:351:0x0308, B:113:0x0105, B:114:0x010c, B:47:0x0087, B:48:0x008d, B:292:0x028f, B:293:0x0295, B:691:0x0591, B:692:0x0597, B:559:0x0492, B:560:0x0498, B:625:0x0513, B:626:0x0519, B:493:0x0414, B:494:0x041a), top: B:5:0x001f, inners: #0, #1, #2, #3, #4, #5, #6, #7, #8, #9, #10, #11, #12, #13, #14, #15, #16, #17, #18, #19, #20, #21, #22, #23, #24, #25, #26, #27, #28, #29, #30, #31, #32, #33, #34, #35, #37, #38, #39, #40, #41, #42, #43, #44, #45, #46, #47, #48, #49, #50, #51, #52, #53, #54, #55, #56, #57 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public int onPullAtom(int i, List<StatsEvent> list) {
            int pullBluetoothBytesTransferLocked;
            int pullBluetoothActivityInfoLocked;
            int pullTimeZoneDataInfoLocked;
            int pullExternalStorageInfoLocked;
            int pullNotificationRemoteViewsLocked;
            int pullDangerousPermissionStateLocked;
            int pullRuntimeAppOpAccessMessageLocked;
            int pullIonHeapSizeLocked;
            int pullDataBytesTransferLocked;
            int pullKernelWakelockLocked;
            int pullCpuTimePerUidLocked;
            int pullCpuTimePerUidFreqLocked;
            int pullWifiActivityInfoLocked;
            int pullModemActivityInfoLocked;
            int pullProcessMemoryStateLocked;
            int pullSystemElapsedRealtimeLocked;
            int pullSystemUptimeLocked;
            int pullCpuActiveTimeLocked;
            int pullCpuClusterTimeLocked;
            int pullHealthHalLocked;
            int pullTemperatureLocked;
            int pullBinderCallsStatsLocked;
            int pullBinderCallsStatsExceptionsLocked;
            int pullLooperStatsLocked;
            int pullDiskStatsLocked;
            int pullDirectoryUsageLocked;
            int pullAppSizeLocked;
            int pullCategorySizeLocked;
            int pullProcStatsLocked;
            int pullNumBiometricsEnrolledLocked;
            int pullDiskIOLocked;
            int pullPowerProfileLocked;
            int pullProcStatsLocked2;
            int pullProcessCpuTimeLocked;
            int pullCpuTimePerThreadFreqLocked;
            int pullDeviceCalculatedPowerUseLocked;
            int pullAttributedAppOpsLocked;
            int pullSettingsStatsLocked;
            int pullCpuTimePerClusterFreqLocked;
            int pullCpuCyclesPerUidClusterLocked;
            int pullTimeZoneDetectorStateLocked;
            int pullInstalledIncrementalPackagesLocked;
            int pullProcessStateLocked;
            int pullProcessAssociationLocked;
            int pullUwbActivityInfoLocked;
            int pullProcessMemoryHighWaterMarkLocked;
            int pullBuildInformationLocked;
            int pullDebugElapsedClockLocked;
            int pullDebugFailingElapsedClockLocked;
            int pullNumBiometricsEnrolledLocked2;
            int pullRoleHolderLocked;
            int pullSystemIonHeapSizeLocked;
            int pullAppsOnExternalStorageInfoLocked;
            int pullFaceSettingsLocked;
            int pullCooldownDeviceLocked;
            int pullAppOpsLocked;
            int pullProcessSystemIonHeapSizeLocked;
            if (Trace.isTagEnabled(524288L)) {
                Trace.traceBegin(524288L, "StatsPull-" + i);
            }
            try {
                if (i == 10006) {
                    synchronized (StatsPullAtomService.this.mBluetoothBytesTransferLock) {
                        pullBluetoothBytesTransferLocked = StatsPullAtomService.this.pullBluetoothBytesTransferLocked(i, list);
                    }
                    Trace.traceEnd(524288L);
                    return pullBluetoothBytesTransferLocked;
                }
                if (i == 10007) {
                    synchronized (StatsPullAtomService.this.mBluetoothActivityInfoLock) {
                        pullBluetoothActivityInfoLocked = StatsPullAtomService.this.pullBluetoothActivityInfoLocked(i, list);
                    }
                    Trace.traceEnd(524288L);
                    return pullBluetoothActivityInfoLocked;
                }
                if (i == 10052) {
                    synchronized (StatsPullAtomService.this.mTimeZoneDataInfoLock) {
                        pullTimeZoneDataInfoLocked = StatsPullAtomService.this.pullTimeZoneDataInfoLocked(i, list);
                    }
                    Trace.traceEnd(524288L);
                    return pullTimeZoneDataInfoLocked;
                }
                if (i == 10053) {
                    synchronized (StatsPullAtomService.this.mExternalStorageInfoLock) {
                        pullExternalStorageInfoLocked = StatsPullAtomService.this.pullExternalStorageInfoLocked(i, list);
                    }
                    Trace.traceEnd(524288L);
                    return pullExternalStorageInfoLocked;
                }
                if (i == 10066) {
                    synchronized (StatsPullAtomService.this.mNotificationRemoteViewsLock) {
                        pullNotificationRemoteViewsLocked = StatsPullAtomService.this.pullNotificationRemoteViewsLocked(i, list);
                    }
                    Trace.traceEnd(524288L);
                    return pullNotificationRemoteViewsLocked;
                }
                if (i != 10067) {
                    if (i == 10069) {
                        synchronized (StatsPullAtomService.this.mRuntimeAppOpAccessMessageLock) {
                            pullRuntimeAppOpAccessMessageLocked = StatsPullAtomService.this.pullRuntimeAppOpAccessMessageLocked(i, list);
                        }
                        Trace.traceEnd(524288L);
                        return pullRuntimeAppOpAccessMessageLocked;
                    }
                    if (i == 10070) {
                        synchronized (StatsPullAtomService.this.mIonHeapSizeLock) {
                            pullIonHeapSizeLocked = StatsPullAtomService.this.pullIonHeapSizeLocked(i, list);
                        }
                        Trace.traceEnd(524288L);
                        return pullIonHeapSizeLocked;
                    }
                    if (i != 10082 && i != 10083) {
                        switch (i) {
                            case 10000:
                            case 10001:
                            case 10002:
                            case 10003:
                                break;
                            case 10004:
                                synchronized (StatsPullAtomService.this.mKernelWakelockLock) {
                                    pullKernelWakelockLocked = StatsPullAtomService.this.pullKernelWakelockLocked(i, list);
                                }
                                Trace.traceEnd(524288L);
                                return pullKernelWakelockLocked;
                            default:
                                switch (i) {
                                    case 10009:
                                        synchronized (StatsPullAtomService.this.mCpuTimePerUidLock) {
                                            pullCpuTimePerUidLocked = StatsPullAtomService.this.pullCpuTimePerUidLocked(i, list);
                                        }
                                        Trace.traceEnd(524288L);
                                        return pullCpuTimePerUidLocked;
                                    case 10010:
                                        synchronized (StatsPullAtomService.this.mCpuTimePerUidFreqLock) {
                                            pullCpuTimePerUidFreqLocked = StatsPullAtomService.this.pullCpuTimePerUidFreqLocked(i, list);
                                        }
                                        Trace.traceEnd(524288L);
                                        return pullCpuTimePerUidFreqLocked;
                                    case 10011:
                                        synchronized (StatsPullAtomService.this.mWifiActivityInfoLock) {
                                            pullWifiActivityInfoLocked = StatsPullAtomService.this.pullWifiActivityInfoLocked(i, list);
                                        }
                                        Trace.traceEnd(524288L);
                                        return pullWifiActivityInfoLocked;
                                    case 10012:
                                        synchronized (StatsPullAtomService.this.mModemActivityInfoLock) {
                                            pullModemActivityInfoLocked = StatsPullAtomService.this.pullModemActivityInfoLocked(i, list);
                                        }
                                        Trace.traceEnd(524288L);
                                        return pullModemActivityInfoLocked;
                                    case 10013:
                                        synchronized (StatsPullAtomService.this.mProcessMemoryStateLock) {
                                            pullProcessMemoryStateLocked = StatsPullAtomService.this.pullProcessMemoryStateLocked(i, list);
                                        }
                                        Trace.traceEnd(524288L);
                                        return pullProcessMemoryStateLocked;
                                    case 10014:
                                        synchronized (StatsPullAtomService.this.mSystemElapsedRealtimeLock) {
                                            pullSystemElapsedRealtimeLocked = StatsPullAtomService.this.pullSystemElapsedRealtimeLocked(i, list);
                                        }
                                        Trace.traceEnd(524288L);
                                        return pullSystemElapsedRealtimeLocked;
                                    case 10015:
                                        synchronized (StatsPullAtomService.this.mSystemUptimeLock) {
                                            pullSystemUptimeLocked = StatsPullAtomService.this.pullSystemUptimeLocked(i, list);
                                        }
                                        Trace.traceEnd(524288L);
                                        return pullSystemUptimeLocked;
                                    case 10016:
                                        synchronized (StatsPullAtomService.this.mCpuActiveTimeLock) {
                                            pullCpuActiveTimeLocked = StatsPullAtomService.this.pullCpuActiveTimeLocked(i, list);
                                        }
                                        Trace.traceEnd(524288L);
                                        return pullCpuActiveTimeLocked;
                                    case 10017:
                                        synchronized (StatsPullAtomService.this.mCpuClusterTimeLock) {
                                            pullCpuClusterTimeLocked = StatsPullAtomService.this.pullCpuClusterTimeLocked(i, list);
                                        }
                                        Trace.traceEnd(524288L);
                                        return pullCpuClusterTimeLocked;
                                    default:
                                        switch (i) {
                                            case 10019:
                                            case OplusNotificationListenerService.REASON_OPLUSOS_FORCESTOP /* 10020 */:
                                            case 10030:
                                                synchronized (StatsPullAtomService.this.mHealthHalLock) {
                                                    pullHealthHalLocked = StatsPullAtomService.this.pullHealthHalLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullHealthHalLocked;
                                            case OplusNotificationListenerService.REASON_OPLUSOS_BPMSUSPEND /* 10021 */:
                                                synchronized (StatsPullAtomService.this.mTemperatureLock) {
                                                    pullTemperatureLocked = StatsPullAtomService.this.pullTemperatureLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullTemperatureLocked;
                                            case 10022:
                                                synchronized (StatsPullAtomService.this.mBinderCallsStatsLock) {
                                                    pullBinderCallsStatsLocked = StatsPullAtomService.this.pullBinderCallsStatsLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullBinderCallsStatsLocked;
                                            case 10023:
                                                synchronized (StatsPullAtomService.this.mBinderCallsStatsExceptionsLock) {
                                                    pullBinderCallsStatsExceptionsLocked = StatsPullAtomService.this.pullBinderCallsStatsExceptionsLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullBinderCallsStatsExceptionsLocked;
                                            case 10024:
                                                synchronized (StatsPullAtomService.this.mLooperStatsLock) {
                                                    pullLooperStatsLocked = StatsPullAtomService.this.pullLooperStatsLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullLooperStatsLocked;
                                            case 10025:
                                                synchronized (StatsPullAtomService.this.mDiskStatsLock) {
                                                    pullDiskStatsLocked = StatsPullAtomService.this.pullDiskStatsLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullDiskStatsLocked;
                                            case 10026:
                                                synchronized (StatsPullAtomService.this.mDirectoryUsageLock) {
                                                    pullDirectoryUsageLocked = StatsPullAtomService.this.pullDirectoryUsageLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullDirectoryUsageLocked;
                                            case 10027:
                                                synchronized (StatsPullAtomService.this.mAppSizeLock) {
                                                    pullAppSizeLocked = StatsPullAtomService.this.pullAppSizeLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullAppSizeLocked;
                                            case 10028:
                                                synchronized (StatsPullAtomService.this.mCategorySizeLock) {
                                                    pullCategorySizeLocked = StatsPullAtomService.this.pullCategorySizeLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullCategorySizeLocked;
                                            case 10029:
                                                synchronized (StatsPullAtomService.this.mProcStatsLock) {
                                                    pullProcStatsLocked = StatsPullAtomService.this.pullProcStatsLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullProcStatsLocked;
                                            case 10031:
                                                synchronized (StatsPullAtomService.this.mNumBiometricsEnrolledLock) {
                                                    pullNumBiometricsEnrolledLocked = StatsPullAtomService.this.pullNumBiometricsEnrolledLocked(1, i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullNumBiometricsEnrolledLocked;
                                            case 10032:
                                                synchronized (StatsPullAtomService.this.mDiskIoLock) {
                                                    pullDiskIOLocked = StatsPullAtomService.this.pullDiskIOLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullDiskIOLocked;
                                            case 10033:
                                                synchronized (StatsPullAtomService.this.mPowerProfileLock) {
                                                    pullPowerProfileLocked = StatsPullAtomService.this.pullPowerProfileLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullPowerProfileLocked;
                                            case 10034:
                                                synchronized (StatsPullAtomService.this.mProcStatsLock) {
                                                    pullProcStatsLocked2 = StatsPullAtomService.this.pullProcStatsLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullProcStatsLocked2;
                                            case 10035:
                                                synchronized (StatsPullAtomService.this.mProcessCpuTimeLock) {
                                                    pullProcessCpuTimeLocked = StatsPullAtomService.this.pullProcessCpuTimeLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullProcessCpuTimeLocked;
                                            case 10037:
                                                synchronized (StatsPullAtomService.this.mCpuTimePerThreadFreqLock) {
                                                    pullCpuTimePerThreadFreqLocked = StatsPullAtomService.this.pullCpuTimePerThreadFreqLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullCpuTimePerThreadFreqLocked;
                                            case 10039:
                                                synchronized (StatsPullAtomService.this.mDeviceCalculatedPowerUseLock) {
                                                    pullDeviceCalculatedPowerUseLocked = StatsPullAtomService.this.pullDeviceCalculatedPowerUseLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullDeviceCalculatedPowerUseLocked;
                                            case 10064:
                                                int pullProcessMemorySnapshot = StatsPullAtomService.this.pullProcessMemorySnapshot(i, list);
                                                Trace.traceEnd(524288L);
                                                return pullProcessMemorySnapshot;
                                            case 10075:
                                                synchronized (StatsPullAtomService.this.mAttributedAppOpsLock) {
                                                    pullAttributedAppOpsLocked = StatsPullAtomService.this.pullAttributedAppOpsLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullAttributedAppOpsLocked;
                                            case 10080:
                                                synchronized (StatsPullAtomService.this.mSettingsStatsLock) {
                                                    pullSettingsStatsLocked = StatsPullAtomService.this.pullSettingsStatsLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullSettingsStatsLocked;
                                            case 10092:
                                                int pullSystemMemory = StatsPullAtomService.this.pullSystemMemory(i, list);
                                                Trace.traceEnd(524288L);
                                                return pullSystemMemory;
                                            case 10095:
                                                synchronized (StatsPullAtomService.this.mCpuTimePerClusterFreqLock) {
                                                    pullCpuTimePerClusterFreqLocked = StatsPullAtomService.this.pullCpuTimePerClusterFreqLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullCpuTimePerClusterFreqLocked;
                                            case 10096:
                                                synchronized (StatsPullAtomService.this.mCpuTimePerUidFreqLock) {
                                                    pullCpuCyclesPerUidClusterLocked = StatsPullAtomService.this.pullCpuCyclesPerUidClusterLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullCpuCyclesPerUidClusterLocked;
                                            case 10098:
                                                int pullCpuCyclesPerThreadGroupCluster = StatsPullAtomService.this.pullCpuCyclesPerThreadGroupCluster(i, list);
                                                Trace.traceEnd(524288L);
                                                return pullCpuCyclesPerThreadGroupCluster;
                                            case 10100:
                                                break;
                                            case 10102:
                                                synchronized (StatsPullAtomService.this.mTimeZoneDetectionInfoLock) {
                                                    pullTimeZoneDetectorStateLocked = StatsPullAtomService.this.pullTimeZoneDetectorStateLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullTimeZoneDetectorStateLocked;
                                            case 10103:
                                            case 10118:
                                            case 10119:
                                            case 10120:
                                            case 10121:
                                            case 10122:
                                            case 10123:
                                            case 10124:
                                            case 10125:
                                                int pullKeystoreAtoms = StatsPullAtomService.this.pullKeystoreAtoms(i, list);
                                                Trace.traceEnd(524288L);
                                                return pullKeystoreAtoms;
                                            case 10105:
                                                int pullProcessDmabufMemory = StatsPullAtomService.this.pullProcessDmabufMemory(i, list);
                                                Trace.traceEnd(524288L);
                                                return pullProcessDmabufMemory;
                                            case 10114:
                                                synchronized (StatsPullAtomService.this.mInstalledIncrementalPackagesLock) {
                                                    pullInstalledIncrementalPackagesLocked = StatsPullAtomService.this.pullInstalledIncrementalPackagesLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullInstalledIncrementalPackagesLocked;
                                            case 10117:
                                                int pullVmStat = StatsPullAtomService.this.pullVmStat(i, list);
                                                Trace.traceEnd(524288L);
                                                return pullVmStat;
                                            case 10127:
                                                int pullAccessibilityShortcutStatsLocked = StatsPullAtomService.this.pullAccessibilityShortcutStatsLocked(i, list);
                                                Trace.traceEnd(524288L);
                                                return pullAccessibilityShortcutStatsLocked;
                                            case 10128:
                                                int pullAccessibilityFloatingMenuStatsLocked = StatsPullAtomService.this.pullAccessibilityFloatingMenuStatsLocked(i, list);
                                                Trace.traceEnd(524288L);
                                                return pullAccessibilityFloatingMenuStatsLocked;
                                            case 10130:
                                                int pullMediaCapabilitiesStats = StatsPullAtomService.this.pullMediaCapabilitiesStats(i, list);
                                                Trace.traceEnd(524288L);
                                                return pullMediaCapabilitiesStats;
                                            case 10150:
                                                int pullSystemServerPinnerStats = StatsPullAtomService.this.pullSystemServerPinnerStats(i, list);
                                                Trace.traceEnd(524288L);
                                                return pullSystemServerPinnerStats;
                                            case 10151:
                                                int pullPendingIntentsPerPackage = StatsPullAtomService.this.pullPendingIntentsPerPackage(i, list);
                                                Trace.traceEnd(524288L);
                                                return pullPendingIntentsPerPackage;
                                            case 10171:
                                                synchronized (StatsPullAtomService.this.mProcStatsLock) {
                                                    pullProcessStateLocked = StatsPullAtomService.this.pullProcessStateLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullProcessStateLocked;
                                            case 10172:
                                                synchronized (StatsPullAtomService.this.mProcStatsLock) {
                                                    pullProcessAssociationLocked = StatsPullAtomService.this.pullProcessAssociationLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullProcessAssociationLocked;
                                            case 10175:
                                                int pullHdrCapabilities = StatsPullAtomService.this.pullHdrCapabilities(i, list);
                                                Trace.traceEnd(524288L);
                                                return pullHdrCapabilities;
                                            case 10188:
                                                synchronized (StatsPullAtomService.this.mUwbActivityInfoLock) {
                                                    pullUwbActivityInfoLocked = StatsPullAtomService.this.pullUwbActivityInfoLocked(i, list);
                                                }
                                                Trace.traceEnd(524288L);
                                                return pullUwbActivityInfoLocked;
                                            case 10189:
                                                int pullCachedAppsHighWatermark = StatsPullAtomService.this.pullCachedAppsHighWatermark(i, list);
                                                Trace.traceEnd(524288L);
                                                return pullCachedAppsHighWatermark;
                                            default:
                                                switch (i) {
                                                    case 10042:
                                                        synchronized (StatsPullAtomService.this.mProcessMemoryHighWaterMarkLock) {
                                                            pullProcessMemoryHighWaterMarkLocked = StatsPullAtomService.this.pullProcessMemoryHighWaterMarkLocked(i, list);
                                                        }
                                                        Trace.traceEnd(524288L);
                                                        return pullProcessMemoryHighWaterMarkLocked;
                                                    case 10043:
                                                    case 10045:
                                                        break;
                                                    case 10044:
                                                        synchronized (StatsPullAtomService.this.mBuildInformationLock) {
                                                            pullBuildInformationLocked = StatsPullAtomService.this.pullBuildInformationLocked(i, list);
                                                        }
                                                        Trace.traceEnd(524288L);
                                                        return pullBuildInformationLocked;
                                                    case 10046:
                                                        synchronized (StatsPullAtomService.this.mDebugElapsedClockLock) {
                                                            pullDebugElapsedClockLocked = StatsPullAtomService.this.pullDebugElapsedClockLocked(i, list);
                                                        }
                                                        Trace.traceEnd(524288L);
                                                        return pullDebugElapsedClockLocked;
                                                    case 10047:
                                                        synchronized (StatsPullAtomService.this.mDebugFailingElapsedClockLock) {
                                                            pullDebugFailingElapsedClockLocked = StatsPullAtomService.this.pullDebugFailingElapsedClockLocked(i, list);
                                                        }
                                                        Trace.traceEnd(524288L);
                                                        return pullDebugFailingElapsedClockLocked;
                                                    case 10048:
                                                        synchronized (StatsPullAtomService.this.mNumBiometricsEnrolledLock) {
                                                            pullNumBiometricsEnrolledLocked2 = StatsPullAtomService.this.pullNumBiometricsEnrolledLocked(4, i, list);
                                                        }
                                                        Trace.traceEnd(524288L);
                                                        return pullNumBiometricsEnrolledLocked2;
                                                    case 10049:
                                                        synchronized (StatsPullAtomService.this.mRoleHolderLock) {
                                                            pullRoleHolderLocked = StatsPullAtomService.this.pullRoleHolderLocked(i, list);
                                                        }
                                                        Trace.traceEnd(524288L);
                                                        return pullRoleHolderLocked;
                                                    case 10050:
                                                        break;
                                                    default:
                                                        switch (i) {
                                                            case 10056:
                                                                synchronized (StatsPullAtomService.this.mSystemIonHeapSizeLock) {
                                                                    pullSystemIonHeapSizeLocked = StatsPullAtomService.this.pullSystemIonHeapSizeLocked(i, list);
                                                                }
                                                                Trace.traceEnd(524288L);
                                                                return pullSystemIonHeapSizeLocked;
                                                            case 10057:
                                                                synchronized (StatsPullAtomService.this.mAppsOnExternalStorageInfoLock) {
                                                                    pullAppsOnExternalStorageInfoLocked = StatsPullAtomService.this.pullAppsOnExternalStorageInfoLocked(i, list);
                                                                }
                                                                Trace.traceEnd(524288L);
                                                                return pullAppsOnExternalStorageInfoLocked;
                                                            case 10058:
                                                                synchronized (StatsPullAtomService.this.mFaceSettingsLock) {
                                                                    pullFaceSettingsLocked = StatsPullAtomService.this.pullFaceSettingsLocked(i, list);
                                                                }
                                                                Trace.traceEnd(524288L);
                                                                return pullFaceSettingsLocked;
                                                            case 10059:
                                                                synchronized (StatsPullAtomService.this.mCooldownDeviceLock) {
                                                                    pullCooldownDeviceLocked = StatsPullAtomService.this.pullCooldownDeviceLocked(i, list);
                                                                }
                                                                Trace.traceEnd(524288L);
                                                                return pullCooldownDeviceLocked;
                                                            case 10060:
                                                                synchronized (StatsPullAtomService.this.mAppOpsLock) {
                                                                    pullAppOpsLocked = StatsPullAtomService.this.pullAppOpsLocked(i, list);
                                                                }
                                                                Trace.traceEnd(524288L);
                                                                return pullAppOpsLocked;
                                                            case 10061:
                                                                synchronized (StatsPullAtomService.this.mProcessSystemIonHeapSizeLock) {
                                                                    pullProcessSystemIonHeapSizeLocked = StatsPullAtomService.this.pullProcessSystemIonHeapSizeLocked(i, list);
                                                                }
                                                                Trace.traceEnd(524288L);
                                                                return pullProcessSystemIonHeapSizeLocked;
                                                            default:
                                                                throw new UnsupportedOperationException("Unknown tagId=" + i);
                                                        }
                                                }
                                        }
                                }
                        }
                    }
                    synchronized (StatsPullAtomService.this.mDataBytesTransferLock) {
                        pullDataBytesTransferLocked = StatsPullAtomService.this.pullDataBytesTransferLocked(i, list);
                    }
                    Trace.traceEnd(524288L);
                    return pullDataBytesTransferLocked;
                }
                synchronized (StatsPullAtomService.this.mDangerousPermissionStateLock) {
                    pullDangerousPermissionStateLocked = StatsPullAtomService.this.pullDangerousPermissionStateLocked(i, list);
                }
                Trace.traceEnd(524288L);
                return pullDangerousPermissionStateLocked;
            } catch (Throwable th) {
                Trace.traceEnd(524288L);
                throw th;
            }
            Trace.traceEnd(524288L);
            throw th;
        }
    }

    public void onBootPhase(int i) {
        super.onBootPhase(i);
        if (i == 500) {
            BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    StatsPullAtomService.this.lambda$onBootPhase$0();
                }
            });
        } else if (i == 600) {
            BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    StatsPullAtomService.this.lambda$onBootPhase$1();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$0() {
        initializeNativePullers();
        initializePullersState();
        registerPullers();
        registerEventListeners();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$1() {
        initAndRegisterNetworkStatsPullers();
        initAndRegisterDeferredPullers();
    }

    void initializePullersState() {
        this.mStatsManager = (StatsManager) this.mContext.getSystemService("stats");
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        this.mTelephony = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mSubscriptionManager = (SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
        this.mStatsSubscriptionsListener = new StatsSubscriptionsListener(this.mSubscriptionManager);
        this.mStorageManager = (StorageManager) this.mContext.getSystemService(StorageManager.class);
        this.mNetworkStatsManager = (NetworkStatsManager) this.mContext.getSystemService(NetworkStatsManager.class);
        this.mStoragedUidIoStatsReader = new StoragedUidIoStatsReader();
        File file = new File(SystemServiceManager.ensureSystemDir(), "stats_pull");
        this.mBaseDir = file;
        file.mkdirs();
        this.mCpuUidUserSysTimeReader = new KernelCpuUidTimeReader.KernelCpuUidUserSysTimeReader(false);
        this.mCpuUidFreqTimeReader = new KernelCpuUidTimeReader.KernelCpuUidFreqTimeReader(false);
        this.mCpuUidActiveTimeReader = new KernelCpuUidTimeReader.KernelCpuUidActiveTimeReader(false);
        this.mCpuUidClusterTimeReader = new KernelCpuUidTimeReader.KernelCpuUidClusterTimeReader(false);
        this.mKernelWakelockReader = new KernelWakelockReader();
        this.mTmpWakelockStats = new KernelWakelockStats();
        this.mKernelCpuThreadReader = KernelCpuThreadReaderSettingsObserver.getSettingsModifiedReader(this.mContext);
        try {
            this.mHealthService = HealthServiceWrapper.create(null);
        } catch (RemoteException | NoSuchElementException unused) {
            Slog.e(TAG, "failed to initialize healthHalWrapper");
        }
        PackageManager packageManager = this.mContext.getPackageManager();
        for (int i = 0; i < 136; i++) {
            String opToPermission = AppOpsManager.opToPermission(i);
            if (opToPermission != null) {
                try {
                    if (packageManager.getPermissionInfo(opToPermission, 0).getProtection() == 1) {
                        this.mDangerousAppOpsList.add(Integer.valueOf(i));
                    }
                } catch (PackageManager.NameNotFoundException unused2) {
                }
            }
        }
        this.mSurfaceFlingerProcessCpuThreadReader = new SelectedProcessCpuThreadReader("/system/bin/surfaceflinger");
        getIKeystoreMetricsService();
    }

    void registerEventListeners() {
        byte b = 0;
        ((ConnectivityManager) this.mContext.getSystemService("connectivity")).registerNetworkCallback(new NetworkRequest.Builder().build(), new ConnectivityStatsCallback());
        IThermalService iThermalService = getIThermalService();
        if (iThermalService != null) {
            try {
                iThermalService.registerThermalEventListener(new ThermalEventListener());
                Slog.i(TAG, "register thermal listener successfully");
            } catch (RemoteException unused) {
                Slog.i(TAG, "failed to register thermal listener");
            }
        }
    }

    void registerPullers() {
        Slog.d(TAG, "Registering pullers with statsd");
        this.mStatsCallbackImpl = new StatsPullAtomCallbackImpl();
        registerBluetoothBytesTransfer();
        registerKernelWakelock();
        registerCpuTimePerClusterFreq();
        registerCpuTimePerUid();
        registerCpuCyclesPerUidCluster();
        registerCpuTimePerUidFreq();
        registerCpuCyclesPerThreadGroupCluster();
        registerCpuActiveTime();
        registerCpuClusterTime();
        registerWifiActivityInfo();
        registerModemActivityInfo();
        registerBluetoothActivityInfo();
        registerSystemElapsedRealtime();
        registerSystemUptime();
        registerProcessMemoryState();
        registerProcessMemoryHighWaterMark();
        registerProcessMemorySnapshot();
        registerSystemIonHeapSize();
        registerIonHeapSize();
        registerProcessSystemIonHeapSize();
        registerSystemMemory();
        registerProcessDmabufMemory();
        registerVmStat();
        registerTemperature();
        registerCoolingDevice();
        registerBinderCallsStats();
        registerBinderCallsStatsExceptions();
        registerLooperStats();
        registerDiskStats();
        registerDirectoryUsage();
        registerAppSize();
        registerCategorySize();
        registerNumFingerprintsEnrolled();
        registerNumFacesEnrolled();
        registerProcStats();
        registerProcStatsPkgProc();
        registerProcessState();
        registerProcessAssociation();
        registerDiskIO();
        registerPowerProfile();
        registerProcessCpuTime();
        registerCpuTimePerThreadFreq();
        registerDeviceCalculatedPowerUse();
        registerDebugElapsedClock();
        registerDebugFailingElapsedClock();
        registerBuildInformation();
        registerRoleHolder();
        registerTimeZoneDataInfo();
        registerTimeZoneDetectorState();
        registerExternalStorageInfo();
        registerAppsOnExternalStorageInfo();
        registerFaceSettings();
        registerAppOps();
        registerAttributedAppOps();
        registerRuntimeAppOpAccessMessage();
        registerNotificationRemoteViews();
        registerDangerousPermissionState();
        registerDangerousPermissionStateSampled();
        registerBatteryLevel();
        registerRemainingBatteryCapacity();
        registerFullBatteryCapacity();
        registerBatteryVoltage();
        registerBatteryCycleCount();
        registerSettingsStats();
        registerInstalledIncrementalPackages();
        registerKeystoreStorageStats();
        registerKeystoreKeyCreationWithGeneralInfo();
        registerKeystoreKeyCreationWithAuthInfo();
        registerKeystoreKeyCreationWithPurposeModesInfo();
        registerKeystoreAtomWithOverflow();
        registerKeystoreKeyOperationWithPurposeAndModesInfo();
        registerKeystoreKeyOperationWithGeneralInfo();
        registerRkpErrorStats();
        registerKeystoreCrashStats();
        registerAccessibilityShortcutStats();
        registerAccessibilityFloatingMenuStats();
        registerMediaCapabilitiesStats();
        registerPendingIntentsPerPackagePuller();
        registerPinnerServiceStats();
        registerHdrCapabilitiesPuller();
        registerCachedAppsHighWatermarkPuller();
    }

    private void initAndRegisterNetworkStatsPullers() {
        Slog.d(TAG, "Registering NetworkStats pullers with statsd");
        this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(10000));
        this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(10001));
        this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(10002));
        this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(10003));
        this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(10083));
        this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(10082));
        this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(10100));
        Executor necExecutor = OplusServiceFactory.getInstance().getFeature(IOplusNecConnectMonitor.DEFAULT, new Object[]{this.mContext}).getNecExecutor();
        if (necExecutor != null) {
            Slog.e(TAG, "using sNecExecutor  for SubscriptionsListener");
            this.mSubscriptionManager.addOnSubscriptionsChangedListener(necExecutor, this.mStatsSubscriptionsListener);
        } else {
            Slog.e(TAG, "using default BackgroundThread for SubscriptionsListener");
            this.mSubscriptionManager.addOnSubscriptionsChangedListener(BackgroundThread.getExecutor(), this.mStatsSubscriptionsListener);
        }
        registerWifiBytesTransfer();
        registerWifiBytesTransferBackground();
        registerMobileBytesTransfer();
        registerMobileBytesTransferBackground();
        registerBytesTransferByTagAndMetered();
        registerDataUsageBytesTransfer();
        registerOemManagedBytesTransfer();
    }

    private void initAndRegisterDeferredPullers() {
        this.mUwbManager = this.mContext.getPackageManager().hasSystemFeature("android.hardware.uwb") ? (UwbManager) this.mContext.getSystemService(UwbManager.class) : null;
        registerUwbActivityInfo();
    }

    private IThermalService getIThermalService() {
        IThermalService iThermalService;
        synchronized (this.mThermalLock) {
            if (this.mThermalService == null) {
                IThermalService asInterface = IThermalService.Stub.asInterface(ServiceManager.getService("thermalservice"));
                this.mThermalService = asInterface;
                if (asInterface != null) {
                    try {
                        asInterface.asBinder().linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda20
                            @Override // android.os.IBinder.DeathRecipient
                            public final void binderDied() {
                                StatsPullAtomService.this.lambda$getIThermalService$2();
                            }
                        }, 0);
                    } catch (RemoteException e) {
                        Slog.e(TAG, "linkToDeath with thermalService failed", e);
                        this.mThermalService = null;
                    }
                }
            }
            iThermalService = this.mThermalService;
        }
        return iThermalService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getIThermalService$2() {
        synchronized (this.mThermalLock) {
            this.mThermalService = null;
        }
    }

    private IKeystoreMetrics getIKeystoreMetricsService() {
        IKeystoreMetrics iKeystoreMetrics;
        synchronized (this.mKeystoreLock) {
            if (this.mIKeystoreMetrics == null) {
                IKeystoreMetrics asInterface = IKeystoreMetrics.Stub.asInterface(ServiceManager.getService("android.security.metrics"));
                this.mIKeystoreMetrics = asInterface;
                if (asInterface != null) {
                    try {
                        asInterface.asBinder().linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda27
                            @Override // android.os.IBinder.DeathRecipient
                            public final void binderDied() {
                                StatsPullAtomService.this.lambda$getIKeystoreMetricsService$3();
                            }
                        }, 0);
                    } catch (RemoteException e) {
                        Slog.e(TAG, "linkToDeath with IKeystoreMetrics failed", e);
                        this.mIKeystoreMetrics = null;
                    }
                }
            }
            iKeystoreMetrics = this.mIKeystoreMetrics;
        }
        return iKeystoreMetrics;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getIKeystoreMetricsService$3() {
        synchronized (this.mKeystoreLock) {
            this.mIKeystoreMetrics = null;
        }
    }

    private IStoraged getIStoragedService() {
        synchronized (this.mStoragedLock) {
            if (this.mStorageService == null) {
                this.mStorageService = IStoraged.Stub.asInterface(ServiceManager.getService("storaged"));
            }
            IStoraged iStoraged = this.mStorageService;
            if (iStoraged != null) {
                try {
                    iStoraged.asBinder().linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda19
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            StatsPullAtomService.this.lambda$getIStoragedService$4();
                        }
                    }, 0);
                } catch (RemoteException e) {
                    Slog.e(TAG, "linkToDeath with storagedService failed", e);
                    this.mStorageService = null;
                }
            }
        }
        return this.mStorageService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getIStoragedService$4() {
        synchronized (this.mStoragedLock) {
            this.mStorageService = null;
        }
    }

    private INotificationManager getINotificationManagerService() {
        synchronized (this.mNotificationStatsLock) {
            if (this.mNotificationManagerService == null) {
                this.mNotificationManagerService = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
            }
            INotificationManager iNotificationManager = this.mNotificationManagerService;
            if (iNotificationManager != null) {
                try {
                    iNotificationManager.asBinder().linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda21
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            StatsPullAtomService.this.lambda$getINotificationManagerService$5();
                        }
                    }, 0);
                } catch (RemoteException e) {
                    Slog.e(TAG, "linkToDeath with notificationManager failed", e);
                    this.mNotificationManagerService = null;
                }
            }
        }
        return this.mNotificationManagerService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getINotificationManagerService$5() {
        synchronized (this.mNotificationStatsLock) {
            this.mNotificationManagerService = null;
        }
    }

    private IProcessStats getIProcessStatsService() {
        synchronized (this.mProcStatsLock) {
            if (this.mProcessStatsService == null) {
                this.mProcessStatsService = IProcessStats.Stub.asInterface(ServiceManager.getService("procstats"));
            }
            IProcessStats iProcessStats = this.mProcessStatsService;
            if (iProcessStats != null) {
                try {
                    iProcessStats.asBinder().linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda25
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            StatsPullAtomService.this.lambda$getIProcessStatsService$6();
                        }
                    }, 0);
                } catch (RemoteException e) {
                    Slog.e(TAG, "linkToDeath with ProcessStats failed", e);
                    this.mProcessStatsService = null;
                }
            }
        }
        return this.mProcessStatsService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getIProcessStatsService$6() {
        synchronized (this.mProcStatsLock) {
            this.mProcessStatsService = null;
        }
    }

    private void registerWifiBytesTransfer() {
        this.mStatsManager.setPullAtomCallback(10000, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2, 3, 4, 5}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private List<NetworkStatsExt> collectNetworkStatsSnapshotForAtom(int i) {
        ArrayList arrayList = new ArrayList();
        if (i == 10082) {
            Iterator<SubInfo> it = this.mHistoricalSubs.iterator();
            while (it.hasNext()) {
                arrayList.addAll(getDataUsageBytesTransferSnapshotForSub(it.next()));
            }
        } else if (i == 10083) {
            NetworkStats uidNetworkStatsSnapshotForTemplate = getUidNetworkStatsSnapshotForTemplate(new NetworkTemplate.Builder(4).build(), true);
            NetworkStats uidNetworkStatsSnapshotForTemplate2 = getUidNetworkStatsSnapshotForTemplate(new NetworkTemplate.Builder(1).setMeteredness(1).build(), true);
            if (uidNetworkStatsSnapshotForTemplate != null && uidNetworkStatsSnapshotForTemplate2 != null) {
                arrayList.add(new NetworkStatsExt(sliceNetworkStatsByUidTagAndMetered(uidNetworkStatsSnapshotForTemplate.add(uidNetworkStatsSnapshotForTemplate2)), new int[]{1, 0}, false, true, true, 0, null, -1));
            }
        } else if (i != 10100) {
            switch (i) {
                case 10000:
                    NetworkStats uidNetworkStatsSnapshotForTransport = getUidNetworkStatsSnapshotForTransport(1);
                    if (uidNetworkStatsSnapshotForTransport != null) {
                        arrayList.add(new NetworkStatsExt(sliceNetworkStatsByUid(uidNetworkStatsSnapshotForTransport), new int[]{1}, false));
                        break;
                    }
                    break;
                case 10001:
                    NetworkStats uidNetworkStatsSnapshotForTransport2 = getUidNetworkStatsSnapshotForTransport(1);
                    if (uidNetworkStatsSnapshotForTransport2 != null) {
                        arrayList.add(new NetworkStatsExt(sliceNetworkStatsByUidAndFgbg(uidNetworkStatsSnapshotForTransport2), new int[]{1}, true));
                        break;
                    }
                    break;
                case 10002:
                    NetworkStats uidNetworkStatsSnapshotForTransport3 = getUidNetworkStatsSnapshotForTransport(0);
                    if (uidNetworkStatsSnapshotForTransport3 != null) {
                        arrayList.add(new NetworkStatsExt(sliceNetworkStatsByUid(uidNetworkStatsSnapshotForTransport3), new int[]{0}, false));
                        break;
                    }
                    break;
                case 10003:
                    NetworkStats uidNetworkStatsSnapshotForTransport4 = getUidNetworkStatsSnapshotForTransport(0);
                    if (uidNetworkStatsSnapshotForTransport4 != null) {
                        arrayList.add(new NetworkStatsExt(sliceNetworkStatsByUidAndFgbg(uidNetworkStatsSnapshotForTransport4), new int[]{0}, true));
                        break;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown atomTag " + i);
            }
        } else {
            arrayList.addAll(getDataUsageBytesTransferSnapshotForOemManaged());
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullDataBytesTransferLocked(int i, List<StatsEvent> list) {
        List<NetworkStatsExt> collectNetworkStatsSnapshotForAtom = collectNetworkStatsSnapshotForAtom(i);
        int i2 = 1;
        if (collectNetworkStatsSnapshotForAtom == null) {
            Slog.e(TAG, "current snapshot is null for " + i + ", return.");
            return 1;
        }
        for (final NetworkStatsExt networkStatsExt : collectNetworkStatsSnapshotForAtom) {
            NetworkStatsExt networkStatsExt2 = (NetworkStatsExt) CollectionUtils.find(this.mNetworkStatsBaselines, new Predicate() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$pullDataBytesTransferLocked$7;
                    lambda$pullDataBytesTransferLocked$7 = StatsPullAtomService.lambda$pullDataBytesTransferLocked$7(NetworkStatsExt.this, (NetworkStatsExt) obj);
                    return lambda$pullDataBytesTransferLocked$7;
                }
            });
            if (networkStatsExt2 == null) {
                Slog.e(TAG, "baseline is null for " + i + ", return.");
                return i2;
            }
            NetworkStatsExt networkStatsExt3 = new NetworkStatsExt(removeEmptyEntries(networkStatsExt.stats.subtract(networkStatsExt2.stats)), networkStatsExt.transports, networkStatsExt.slicedByFgbg, networkStatsExt.slicedByTag, networkStatsExt.slicedByMetered, networkStatsExt.ratType, networkStatsExt.subInfo, networkStatsExt.oemManaged);
            if (networkStatsExt3.stats.iterator().hasNext()) {
                if (i == 10082) {
                    addDataUsageBytesTransferAtoms(networkStatsExt3, list);
                } else if (i == 10083) {
                    addBytesTransferByTagAndMeteredAtoms(networkStatsExt3, list);
                } else if (i == 10100) {
                    addOemDataUsageBytesTransferAtoms(networkStatsExt3, list);
                } else {
                    addNetworkStats(i, list, networkStatsExt3);
                }
            }
            i2 = 1;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$pullDataBytesTransferLocked$7(NetworkStatsExt networkStatsExt, NetworkStatsExt networkStatsExt2) {
        return networkStatsExt2.hasSameSlicing(networkStatsExt);
    }

    private static NetworkStats removeEmptyEntries(NetworkStats networkStats) {
        NetworkStats networkStats2 = new NetworkStats(0L, 1);
        Iterator it = networkStats.iterator();
        while (it.hasNext()) {
            NetworkStats.Entry entry = (NetworkStats.Entry) it.next();
            if (entry.getRxBytes() != 0 || entry.getRxPackets() != 0 || entry.getTxBytes() != 0 || entry.getTxPackets() != 0 || entry.getOperations() != 0) {
                networkStats2 = networkStats2.addEntry(entry);
            }
        }
        return networkStats2;
    }

    private void addNetworkStats(int i, List<StatsEvent> list, NetworkStatsExt networkStatsExt) {
        StatsEvent buildStatsEvent;
        Iterator it = networkStatsExt.stats.iterator();
        while (it.hasNext()) {
            NetworkStats.Entry entry = (NetworkStats.Entry) it.next();
            if (networkStatsExt.slicedByFgbg) {
                buildStatsEvent = FrameworkStatsLog.buildStatsEvent(i, entry.getUid(), entry.getSet() > 0, entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets());
            } else {
                buildStatsEvent = FrameworkStatsLog.buildStatsEvent(i, entry.getUid(), entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets());
            }
            list.add(buildStatsEvent);
        }
    }

    private void addBytesTransferByTagAndMeteredAtoms(NetworkStatsExt networkStatsExt, List<StatsEvent> list) {
        Iterator it = networkStatsExt.stats.iterator();
        while (it.hasNext()) {
            NetworkStats.Entry entry = (NetworkStats.Entry) it.next();
            list.add(FrameworkStatsLog.buildStatsEvent(10083, entry.getUid(), entry.getMetered() == 1, entry.getTag(), entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets()));
        }
    }

    private void addDataUsageBytesTransferAtoms(NetworkStatsExt networkStatsExt, List<StatsEvent> list) {
        int i = networkStatsExt.ratType;
        boolean z = true;
        boolean z2 = i == -2;
        if (!z2 && i != 20) {
            z = false;
        }
        Iterator it = networkStatsExt.stats.iterator();
        while (it.hasNext()) {
            NetworkStats.Entry entry = (NetworkStats.Entry) it.next();
            int set = entry.getSet();
            long rxBytes = entry.getRxBytes();
            long rxPackets = entry.getRxPackets();
            long txBytes = entry.getTxBytes();
            long txPackets = entry.getTxPackets();
            int i2 = z2 ? 13 : networkStatsExt.ratType;
            SubInfo subInfo = networkStatsExt.subInfo;
            list.add(FrameworkStatsLog.buildStatsEvent(10082, set, rxBytes, rxPackets, txBytes, txPackets, i2, subInfo.mcc, subInfo.mnc, subInfo.carrierId, subInfo.isOpportunistic ? 2 : 3, z));
        }
    }

    private void addOemDataUsageBytesTransferAtoms(NetworkStatsExt networkStatsExt, List<StatsEvent> list) {
        int i = networkStatsExt.oemManaged;
        int[] iArr = networkStatsExt.transports;
        int length = iArr.length;
        int i2 = 0;
        while (i2 < length) {
            int i3 = iArr[i2];
            Iterator it = networkStatsExt.stats.iterator();
            while (it.hasNext()) {
                NetworkStats.Entry entry = (NetworkStats.Entry) it.next();
                list.add(FrameworkStatsLog.buildStatsEvent(10100, entry.getUid(), entry.getSet() > 0, i, i3, entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets()));
                length = length;
                i2 = i2;
            }
            i2++;
        }
    }

    private List<NetworkStatsExt> getDataUsageBytesTransferSnapshotForOemManaged() {
        int i = 3;
        List<Pair> of = List.of(new Pair(5, 3), new Pair(1, 0), new Pair(4, 1));
        int[] iArr = {3, 1, 2};
        ArrayList arrayList = new ArrayList();
        for (Pair pair : of) {
            Integer num = (Integer) pair.first;
            int i2 = 0;
            while (i2 < i) {
                int i3 = iArr[i2];
                NetworkStats uidNetworkStatsSnapshotForTemplate = getUidNetworkStatsSnapshotForTemplate(new NetworkTemplate.Builder(num.intValue()).setOemManaged(i3).build(), false);
                Integer num2 = (Integer) pair.second;
                if (uidNetworkStatsSnapshotForTemplate != null) {
                    arrayList.add(new NetworkStatsExt(sliceNetworkStatsByUidAndFgbg(uidNetworkStatsSnapshotForTemplate), new int[]{num2.intValue()}, true, false, false, 0, null, i3));
                }
                i2++;
                i = 3;
            }
        }
        return arrayList;
    }

    private NetworkStats getUidNetworkStatsSnapshotForTransport(int i) {
        NetworkTemplate build;
        if (i == 0) {
            build = new NetworkTemplate.Builder(1).setMeteredness(1).build();
        } else if (i == 1) {
            build = new NetworkTemplate.Builder(4).build();
        } else {
            Log.wtf(TAG, "Unexpected transport.");
            build = null;
        }
        return getUidNetworkStatsSnapshotForTemplate(build, false);
    }

    private NetworkStats getUidNetworkStatsSnapshotForTemplate(NetworkTemplate networkTemplate, boolean z) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long millis = TimeUnit.MICROSECONDS.toMillis(SystemClock.currentTimeMicro());
        long j = Settings.Global.getLong(this.mContext.getContentResolver(), "netstats_uid_bucket_duration", NETSTATS_UID_DEFAULT_BUCKET_DURATION_MS);
        if (networkTemplate.getMatchRule() == 4 && networkTemplate.getSubscriberIds().isEmpty()) {
            this.mNetworkStatsManager.forceUpdate();
        }
        long elapsedRealtime2 = SystemClock.elapsedRealtime();
        long j2 = (millis - elapsedRealtime) - j;
        NetworkStats fromPublicNetworkStats = NetworkStatsUtils.fromPublicNetworkStats(this.mNetworkStatsManager.querySummary(networkTemplate, j2, millis));
        if (!z) {
            return fromPublicNetworkStats;
        }
        NetworkStats fromPublicNetworkStats2 = NetworkStatsUtils.fromPublicNetworkStats(this.mNetworkStatsManager.queryTaggedSummary(networkTemplate, j2, millis));
        long elapsedRealtime3 = SystemClock.elapsedRealtime();
        int myUid = Process.myUid();
        if (myUid == 1000) {
            if (elapsedRealtime3 - elapsedRealtime2 > 1000) {
                Log.d(TAG, "getNetworkStatsSession stack ");
                this.netStackError = true;
            } else {
                this.netStackError = false;
            }
            if (printStack() != null) {
                OplusServiceFactory.getInstance().getFeature(IOplusNecConnectMonitor.DEFAULT, new Object[]{this.mContext}).addNetStackRecord(myUid, printStack(), this.netStackError);
            } else {
                OplusServiceFactory.getInstance().getFeature(IOplusNecConnectMonitor.DEFAULT, new Object[]{this.mContext}).addNetStackRecord(myUid, getPackageNameByUid(myUid), this.netStackError);
            }
        }
        return fromPublicNetworkStats.add(fromPublicNetworkStats2);
    }

    private String getPackageNameByUid(int i) {
        if (i == 0) {
            return "unknown";
        }
        try {
            return this.mContext.getPackageManager().getNameForUid(i);
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    private String printStack() {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        if (stackTrace.length <= 0) {
            return null;
        }
        boolean z = false;
        for (int i = 0; i < stackTrace.length; i++) {
            if (stackTrace[i].getClassName() != null && stackTrace[i].getClassName().equals("com.android.server.stats.pull.StatsPullAtomService")) {
                z = true;
            }
            if (z && !stackTrace[i].getClassName().equals("com.android.server.stats.pull.StatsPullAtomService")) {
                return stackTrace[i].getClassName();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v2 */
    public List<NetworkStatsExt> getDataUsageBytesTransferSnapshotForSub(SubInfo subInfo) {
        ArrayList arrayList = new ArrayList();
        int[] allCollapsedRatTypes = getAllCollapsedRatTypes();
        int length = allCollapsedRatTypes.length;
        int i = 0;
        int i2 = 0;
        while (i2 < length) {
            int i3 = allCollapsedRatTypes[i2];
            NetworkStats uidNetworkStatsSnapshotForTemplate = getUidNetworkStatsSnapshotForTemplate(new NetworkTemplate.Builder(1).setSubscriberIds(Set.of(subInfo.subscriberId)).setRatType(i3).setMeteredness(1).build(), i);
            if (uidNetworkStatsSnapshotForTemplate != null) {
                arrayList.add(new NetworkStatsExt(sliceNetworkStatsByFgbg(uidNetworkStatsSnapshotForTemplate), new int[]{i}, true, false, false, i3, subInfo, -1));
            }
            i2++;
            i = 0;
        }
        return arrayList;
    }

    private static int[] getAllCollapsedRatTypes() {
        int[] allNetworkTypes = TelephonyManager.getAllNetworkTypes();
        HashSet hashSet = new HashSet();
        for (int i : allNetworkTypes) {
            hashSet.add(Integer.valueOf(NetworkStatsManager.getCollapsedRatType(i)));
        }
        hashSet.add(Integer.valueOf(NetworkStatsManager.getCollapsedRatType(-2)));
        hashSet.add(0);
        return com.android.net.module.util.CollectionUtils.toIntArray(hashSet);
    }

    private NetworkStats sliceNetworkStatsByUid(NetworkStats networkStats) {
        return sliceNetworkStats(networkStats, new Function() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda15
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                NetworkStats.Entry lambda$sliceNetworkStatsByUid$8;
                lambda$sliceNetworkStatsByUid$8 = StatsPullAtomService.lambda$sliceNetworkStatsByUid$8((NetworkStats.Entry) obj);
                return lambda$sliceNetworkStatsByUid$8;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ NetworkStats.Entry lambda$sliceNetworkStatsByUid$8(NetworkStats.Entry entry) {
        return new NetworkStats.Entry((String) null, entry.getUid(), -1, 0, -1, -1, -1, entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets(), 0L);
    }

    private NetworkStats sliceNetworkStatsByFgbg(NetworkStats networkStats) {
        return sliceNetworkStats(networkStats, new Function() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                NetworkStats.Entry lambda$sliceNetworkStatsByFgbg$9;
                lambda$sliceNetworkStatsByFgbg$9 = StatsPullAtomService.lambda$sliceNetworkStatsByFgbg$9((NetworkStats.Entry) obj);
                return lambda$sliceNetworkStatsByFgbg$9;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ NetworkStats.Entry lambda$sliceNetworkStatsByFgbg$9(NetworkStats.Entry entry) {
        return new NetworkStats.Entry((String) null, -1, entry.getSet(), 0, -1, -1, -1, entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets(), 0L);
    }

    private NetworkStats sliceNetworkStatsByUidAndFgbg(NetworkStats networkStats) {
        return sliceNetworkStats(networkStats, new Function() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                NetworkStats.Entry lambda$sliceNetworkStatsByUidAndFgbg$10;
                lambda$sliceNetworkStatsByUidAndFgbg$10 = StatsPullAtomService.lambda$sliceNetworkStatsByUidAndFgbg$10((NetworkStats.Entry) obj);
                return lambda$sliceNetworkStatsByUidAndFgbg$10;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ NetworkStats.Entry lambda$sliceNetworkStatsByUidAndFgbg$10(NetworkStats.Entry entry) {
        return new NetworkStats.Entry((String) null, entry.getUid(), entry.getSet(), 0, -1, -1, -1, entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets(), 0L);
    }

    private NetworkStats sliceNetworkStatsByUidTagAndMetered(NetworkStats networkStats) {
        return sliceNetworkStats(networkStats, new Function() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda24
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                NetworkStats.Entry lambda$sliceNetworkStatsByUidTagAndMetered$11;
                lambda$sliceNetworkStatsByUidTagAndMetered$11 = StatsPullAtomService.lambda$sliceNetworkStatsByUidTagAndMetered$11((NetworkStats.Entry) obj);
                return lambda$sliceNetworkStatsByUidTagAndMetered$11;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ NetworkStats.Entry lambda$sliceNetworkStatsByUidTagAndMetered$11(NetworkStats.Entry entry) {
        return new NetworkStats.Entry((String) null, entry.getUid(), -1, entry.getTag(), entry.getMetered(), -1, -1, entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets(), 0L);
    }

    private NetworkStats sliceNetworkStats(NetworkStats networkStats, Function<NetworkStats.Entry, NetworkStats.Entry> function) {
        NetworkStats networkStats2 = new NetworkStats(0L, 1);
        Iterator it = networkStats.iterator();
        while (it.hasNext()) {
            networkStats2 = networkStats2.addEntry(function.apply((NetworkStats.Entry) it.next()));
        }
        return networkStats2;
    }

    private void registerWifiBytesTransferBackground() {
        this.mStatsManager.setPullAtomCallback(10001, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3, 4, 5, 6}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerMobileBytesTransfer() {
        this.mStatsManager.setPullAtomCallback(10002, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2, 3, 4, 5}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerMobileBytesTransferBackground() {
        this.mStatsManager.setPullAtomCallback(10003, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3, 4, 5, 6}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerBytesTransferByTagAndMetered() {
        this.mStatsManager.setPullAtomCallback(10083, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{4, 5, 6, 7}).build(), BackgroundThread.getExecutor(), this.mStatsCallbackImpl);
    }

    private void registerDataUsageBytesTransfer() {
        this.mStatsManager.setPullAtomCallback(10082, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2, 3, 4, 5}).build(), BackgroundThread.getExecutor(), this.mStatsCallbackImpl);
    }

    private void registerOemManagedBytesTransfer() {
        this.mStatsManager.setPullAtomCallback(10100, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{5, 6, 7, 8}).build(), BackgroundThread.getExecutor(), this.mStatsCallbackImpl);
    }

    private void registerBluetoothBytesTransfer() {
        this.mStatsManager.setPullAtomCallback(10006, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2, 3}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private static <T extends Parcelable> T awaitControllerInfo(SynchronousResultReceiver synchronousResultReceiver) {
        if (synchronousResultReceiver == null) {
            return null;
        }
        try {
            SynchronousResultReceiver.Result awaitResult = synchronousResultReceiver.awaitResult(EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS);
            Bundle bundle = awaitResult.bundle;
            if (bundle != null) {
                bundle.setDefusable(true);
                T t = (T) awaitResult.bundle.getParcelable(RESULT_RECEIVER_CONTROLLER_KEY);
                if (t != null) {
                    return t;
                }
            }
        } catch (TimeoutException unused) {
            Slog.w(TAG, "timeout reading " + synchronousResultReceiver.getName() + " stats");
        }
        return null;
    }

    private BluetoothActivityEnergyInfo fetchBluetoothData() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null) {
            final SynchronousResultReceiver synchronousResultReceiver = new SynchronousResultReceiver("bluetooth");
            defaultAdapter.requestControllerActivityEnergyInfo(new SystemServerInitThreadPool$.ExternalSyntheticLambda1(), new BluetoothAdapter.OnBluetoothActivityEnergyInfoCallback() { // from class: com.android.server.stats.pull.StatsPullAtomService.1
                public void onBluetoothActivityEnergyInfoAvailable(BluetoothActivityEnergyInfo bluetoothActivityEnergyInfo) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(StatsPullAtomService.RESULT_RECEIVER_CONTROLLER_KEY, bluetoothActivityEnergyInfo);
                    synchronousResultReceiver.send(0, bundle);
                }

                public void onBluetoothActivityEnergyInfoError(int i) {
                    Slog.w(StatsPullAtomService.TAG, "error reading Bluetooth stats: " + i);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(StatsPullAtomService.RESULT_RECEIVER_CONTROLLER_KEY, null);
                    synchronousResultReceiver.send(0, bundle);
                }
            });
            return awaitControllerInfo(synchronousResultReceiver);
        }
        Slog.e(TAG, "Failed to get bluetooth adapter!");
        return null;
    }

    int pullBluetoothBytesTransferLocked(int i, List<StatsEvent> list) {
        BluetoothActivityEnergyInfo fetchBluetoothData = fetchBluetoothData();
        if (fetchBluetoothData == null) {
            return 1;
        }
        for (UidTraffic uidTraffic : fetchBluetoothData.getUidTraffic()) {
            list.add(FrameworkStatsLog.buildStatsEvent(i, uidTraffic.getUid(), uidTraffic.getRxBytes(), uidTraffic.getTxBytes()));
        }
        return 0;
    }

    private void registerKernelWakelock() {
        this.mStatsManager.setPullAtomCallback(10004, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullKernelWakelockLocked(int i, List<StatsEvent> list) {
        for (Map.Entry<String, KernelWakelockStats.Entry> entry : this.mKernelWakelockReader.readKernelWakelockStats(this.mTmpWakelockStats).entrySet()) {
            String key = entry.getKey();
            KernelWakelockStats.Entry value = entry.getValue();
            list.add(FrameworkStatsLog.buildStatsEvent(i, key, value.mCount, value.mVersion, value.mTotalTime));
        }
        return 0;
    }

    private void registerCpuTimePerClusterFreq() {
        if (KernelCpuBpfTracking.isSupported()) {
            this.mStatsManager.setPullAtomCallback(10095, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
        }
    }

    int pullCpuTimePerClusterFreqLocked(int i, List<StatsEvent> list) {
        int[] freqsClusters = KernelCpuBpfTracking.getFreqsClusters();
        long[] freqs = KernelCpuBpfTracking.getFreqs();
        long[] read = KernelCpuTotalBpfMapReader.read();
        if (read == null) {
            return 1;
        }
        for (int i2 = 0; i2 < read.length; i2++) {
            list.add(FrameworkStatsLog.buildStatsEvent(i, freqsClusters[i2], (int) freqs[i2], read[i2]));
        }
        return 0;
    }

    private void registerCpuTimePerUid() {
        this.mStatsManager.setPullAtomCallback(10009, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2, 3}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCpuTimePerUidLocked(final int i, final List<StatsEvent> list) {
        this.mCpuUidUserSysTimeReader.readAbsolute(new KernelCpuUidTimeReader.Callback() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda22
            public final void onUidCpuTime(int i2, Object obj) {
                StatsPullAtomService.lambda$pullCpuTimePerUidLocked$12(list, i, i2, (long[]) obj);
            }
        });
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$pullCpuTimePerUidLocked$12(List list, int i, int i2, long[] jArr) {
        list.add(FrameworkStatsLog.buildStatsEvent(i, i2, jArr[0], jArr[1]));
    }

    private void registerCpuCyclesPerUidCluster() {
        if (KernelCpuBpfTracking.isSupported() || KernelCpuBpfTracking.getClusters() > 0) {
            this.mStatsManager.setPullAtomCallback(10096, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3, 4, 5}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
        }
    }

    int pullCpuCyclesPerUidClusterLocked(int i, List<StatsEvent> list) {
        PowerProfile powerProfile = new PowerProfile(this.mContext);
        final int[] freqsClusters = KernelCpuBpfTracking.getFreqsClusters();
        final int clusters = KernelCpuBpfTracking.getClusters();
        final long[] freqs = KernelCpuBpfTracking.getFreqs();
        final double[] dArr = new double[freqs.length];
        int i2 = -1;
        int i3 = 0;
        int i4 = 0;
        while (i3 < freqs.length) {
            int i5 = freqsClusters[i3];
            if (i5 != i2) {
                i4 = 0;
            }
            dArr[i3] = powerProfile.getAveragePowerForCpuCore(i5, i4);
            i3++;
            i4++;
            i2 = i5;
        }
        final SparseArray sparseArray = new SparseArray();
        this.mCpuUidFreqTimeReader.readAbsolute(new KernelCpuUidTimeReader.Callback() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda13
            public final void onUidCpuTime(int i6, Object obj) {
                StatsPullAtomService.lambda$pullCpuCyclesPerUidClusterLocked$13(sparseArray, clusters, freqsClusters, freqs, dArr, i6, (long[]) obj);
            }
        });
        int size = sparseArray.size();
        for (int i6 = 0; i6 < size; i6++) {
            int keyAt = sparseArray.keyAt(i6);
            double[] dArr2 = (double[]) sparseArray.valueAt(i6);
            for (int i7 = 0; i7 < clusters; i7++) {
                int i8 = i7 * 3;
                list.add(FrameworkStatsLog.buildStatsEvent(i, keyAt, i7, (long) (dArr2[i8] / 1000000.0d), (long) dArr2[i8 + 1], (long) (dArr2[i8 + 2] / 1000.0d)));
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$pullCpuCyclesPerUidClusterLocked$13(SparseArray sparseArray, int i, int[] iArr, long[] jArr, double[] dArr, int i2, long[] jArr2) {
        if (UserHandle.isIsolated(i2)) {
            return;
        }
        int appId = UserHandle.isSharedAppGid(i2) ? 59999 : UserHandle.getAppId(i2);
        double[] dArr2 = (double[]) sparseArray.get(appId);
        if (dArr2 == null) {
            dArr2 = new double[i * 3];
            sparseArray.put(appId, dArr2);
        }
        for (int i3 = 0; i3 < jArr2.length; i3++) {
            int i4 = iArr[i3];
            long j = jArr2[i3];
            int i5 = i4 * 3;
            dArr2[i5] = dArr2[i5] + (jArr[i3] * j);
            int i6 = i5 + 1;
            double d = j;
            dArr2[i6] = dArr2[i6] + d;
            int i7 = i5 + 2;
            dArr2[i7] = dArr2[i7] + (dArr[i3] * d);
        }
    }

    private void registerCpuTimePerUidFreq() {
        this.mStatsManager.setPullAtomCallback(10010, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCpuTimePerUidFreqLocked(int i, List<StatsEvent> list) {
        final SparseArray sparseArray = new SparseArray();
        this.mCpuUidFreqTimeReader.readAbsolute(new KernelCpuUidTimeReader.Callback() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda12
            public final void onUidCpuTime(int i2, Object obj) {
                StatsPullAtomService.lambda$pullCpuTimePerUidFreqLocked$14(sparseArray, i2, (long[]) obj);
            }
        });
        int size = sparseArray.size();
        for (int i2 = 0; i2 < size; i2++) {
            int keyAt = sparseArray.keyAt(i2);
            long[] jArr = (long[]) sparseArray.valueAt(i2);
            for (int i3 = 0; i3 < jArr.length; i3++) {
                long j = jArr[i3];
                if (j >= 10) {
                    list.add(FrameworkStatsLog.buildStatsEvent(i, keyAt, i3, j));
                }
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$pullCpuTimePerUidFreqLocked$14(SparseArray sparseArray, int i, long[] jArr) {
        if (UserHandle.isIsolated(i)) {
            return;
        }
        int appId = UserHandle.isSharedAppGid(i) ? 59999 : UserHandle.getAppId(i);
        long[] jArr2 = (long[]) sparseArray.get(appId);
        if (jArr2 == null) {
            jArr2 = new long[jArr.length];
            sparseArray.put(appId, jArr2);
        }
        for (int i2 = 0; i2 < jArr.length; i2++) {
            jArr2[i2] = jArr2[i2] + jArr[i2];
        }
    }

    private void registerCpuCyclesPerThreadGroupCluster() {
        if (KernelCpuBpfTracking.isSupported()) {
            this.mStatsManager.setPullAtomCallback(10098, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3, 4}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
        }
    }

    int pullCpuCyclesPerThreadGroupCluster(int i, List<StatsEvent> list) {
        long[] jArr;
        SystemServerCpuThreadReader.SystemServiceCpuThreadTimes systemServiceCpuThreadTimes = ((BatteryStatsInternal) LocalServices.getService(BatteryStatsInternal.class)).getSystemServiceCpuThreadTimes();
        if (systemServiceCpuThreadTimes == null) {
            return 1;
        }
        addCpuCyclesPerThreadGroupClusterAtoms(i, list, 2, systemServiceCpuThreadTimes.threadCpuTimesUs);
        addCpuCyclesPerThreadGroupClusterAtoms(i, list, 1, systemServiceCpuThreadTimes.binderThreadCpuTimesUs);
        KernelSingleProcessCpuThreadReader.ProcessCpuUsage readAbsolute = this.mSurfaceFlingerProcessCpuThreadReader.readAbsolute();
        if (readAbsolute != null && (jArr = readAbsolute.threadCpuTimesMillis) != null) {
            int length = jArr.length;
            long[] jArr2 = new long[length];
            for (int i2 = 0; i2 < length; i2++) {
                jArr2[i2] = readAbsolute.threadCpuTimesMillis[i2] * 1000;
            }
            addCpuCyclesPerThreadGroupClusterAtoms(i, list, 3, jArr2);
        }
        return 0;
    }

    private static void addCpuCyclesPerThreadGroupClusterAtoms(int i, List<StatsEvent> list, int i2, long[] jArr) {
        int[] freqsClusters = KernelCpuBpfTracking.getFreqsClusters();
        int clusters = KernelCpuBpfTracking.getClusters();
        long[] freqs = KernelCpuBpfTracking.getFreqs();
        long[] jArr2 = new long[clusters];
        long[] jArr3 = new long[clusters];
        for (int i3 = 0; i3 < jArr.length; i3++) {
            int i4 = freqsClusters[i3];
            jArr2[i4] = jArr2[i4] + ((freqs[i3] * jArr[i3]) / 1000);
            jArr3[i4] = jArr3[i4] + jArr[i3];
        }
        for (int i5 = 0; i5 < clusters; i5++) {
            list.add(FrameworkStatsLog.buildStatsEvent(i, i2, i5, jArr2[i5] / 1000000, jArr3[i5] / 1000));
        }
    }

    private void registerCpuActiveTime() {
        this.mStatsManager.setPullAtomCallback(10016, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCpuActiveTimeLocked(final int i, final List<StatsEvent> list) {
        this.mCpuUidActiveTimeReader.readAbsolute(new KernelCpuUidTimeReader.Callback() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda11
            public final void onUidCpuTime(int i2, Object obj) {
                StatsPullAtomService.lambda$pullCpuActiveTimeLocked$15(list, i, i2, (Long) obj);
            }
        });
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$pullCpuActiveTimeLocked$15(List list, int i, int i2, Long l) {
        list.add(FrameworkStatsLog.buildStatsEvent(i, i2, l.longValue()));
    }

    private void registerCpuClusterTime() {
        this.mStatsManager.setPullAtomCallback(10017, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCpuClusterTimeLocked(final int i, final List<StatsEvent> list) {
        this.mCpuUidClusterTimeReader.readAbsolute(new KernelCpuUidTimeReader.Callback() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda26
            public final void onUidCpuTime(int i2, Object obj) {
                StatsPullAtomService.lambda$pullCpuClusterTimeLocked$16(list, i, i2, (long[]) obj);
            }
        });
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$pullCpuClusterTimeLocked$16(List list, int i, int i2, long[] jArr) {
        for (int i3 = 0; i3 < jArr.length; i3++) {
            list.add(FrameworkStatsLog.buildStatsEvent(i, i2, i3, jArr[i3]));
        }
    }

    private void registerWifiActivityInfo() {
        this.mStatsManager.setPullAtomCallback(10011, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullWifiActivityInfoLocked(int i, List<StatsEvent> list) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            final SynchronousResultReceiver synchronousResultReceiver = new SynchronousResultReceiver("wifi");
            this.mWifiManager.getWifiActivityEnergyInfoAsync(new Executor() { // from class: com.android.server.stats.pull.StatsPullAtomService.2
                @Override // java.util.concurrent.Executor
                public void execute(Runnable runnable) {
                    runnable.run();
                }
            }, new WifiManager.OnWifiActivityEnergyInfoListener() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda28
                public final void onWifiActivityEnergyInfo(WifiActivityEnergyInfo wifiActivityEnergyInfo) {
                    StatsPullAtomService.lambda$pullWifiActivityInfoLocked$17(synchronousResultReceiver, wifiActivityEnergyInfo);
                }
            });
            WifiActivityEnergyInfo awaitControllerInfo = awaitControllerInfo(synchronousResultReceiver);
            if (awaitControllerInfo == null) {
                return 1;
            }
            list.add(FrameworkStatsLog.buildStatsEvent(i, awaitControllerInfo.getTimeSinceBootMillis(), awaitControllerInfo.getStackState(), awaitControllerInfo.getControllerTxDurationMillis(), awaitControllerInfo.getControllerRxDurationMillis(), awaitControllerInfo.getControllerIdleDurationMillis(), awaitControllerInfo.getControllerEnergyUsedMicroJoules()));
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return 0;
        } catch (RuntimeException e) {
            Slog.e(TAG, "failed to getWifiActivityEnergyInfoAsync", e);
            return 1;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$pullWifiActivityInfoLocked$17(SynchronousResultReceiver synchronousResultReceiver, WifiActivityEnergyInfo wifiActivityEnergyInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_RECEIVER_CONTROLLER_KEY, wifiActivityEnergyInfo);
        synchronousResultReceiver.send(0, bundle);
    }

    private void registerModemActivityInfo() {
        this.mStatsManager.setPullAtomCallback(10012, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullModemActivityInfoLocked(int i, List<StatsEvent> list) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            final CompletableFuture completableFuture = new CompletableFuture();
            this.mTelephony.requestModemActivityInfo(new SystemServerInitThreadPool$.ExternalSyntheticLambda1(), new OutcomeReceiver<ModemActivityInfo, TelephonyManager.ModemActivityInfoException>() { // from class: com.android.server.stats.pull.StatsPullAtomService.3
                @Override // android.os.OutcomeReceiver
                public void onResult(ModemActivityInfo modemActivityInfo) {
                    completableFuture.complete(modemActivityInfo);
                }

                @Override // android.os.OutcomeReceiver
                public void onError(TelephonyManager.ModemActivityInfoException modemActivityInfoException) {
                    Slog.w(StatsPullAtomService.TAG, "error reading modem stats:" + modemActivityInfoException);
                    completableFuture.complete(null);
                }
            });
            ModemActivityInfo modemActivityInfo = (ModemActivityInfo) completableFuture.get(EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            if (modemActivityInfo == null) {
                return 1;
            }
            list.add(FrameworkStatsLog.buildStatsEvent(i, modemActivityInfo.getTimestampMillis(), modemActivityInfo.getSleepTimeMillis(), modemActivityInfo.getIdleTimeMillis(), modemActivityInfo.getTransmitDurationMillisAtPowerLevel(0), modemActivityInfo.getTransmitDurationMillisAtPowerLevel(1), modemActivityInfo.getTransmitDurationMillisAtPowerLevel(2), modemActivityInfo.getTransmitDurationMillisAtPowerLevel(3), modemActivityInfo.getTransmitDurationMillisAtPowerLevel(4), modemActivityInfo.getReceiveTimeMillis(), -1L));
            return 0;
        } catch (InterruptedException | TimeoutException e) {
            Slog.w(TAG, "timeout or interrupt reading modem stats: " + e);
            return 1;
        } catch (ExecutionException e2) {
            Slog.w(TAG, "exception reading modem stats: " + e2.getCause());
            return 1;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void registerBluetoothActivityInfo() {
        this.mStatsManager.setPullAtomCallback(10007, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullBluetoothActivityInfoLocked(int i, List<StatsEvent> list) {
        BluetoothActivityEnergyInfo fetchBluetoothData = fetchBluetoothData();
        if (fetchBluetoothData == null) {
            return 1;
        }
        list.add(FrameworkStatsLog.buildStatsEvent(i, fetchBluetoothData.getTimestampMillis(), fetchBluetoothData.getBluetoothStackState(), fetchBluetoothData.getControllerTxTimeMillis(), fetchBluetoothData.getControllerRxTimeMillis(), fetchBluetoothData.getControllerIdleTimeMillis(), fetchBluetoothData.getControllerEnergyUsed()));
        return 0;
    }

    private void registerUwbActivityInfo() {
        if (this.mUwbManager == null) {
            return;
        }
        this.mStatsManager.setPullAtomCallback(10188, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullUwbActivityInfoLocked(int i, List<StatsEvent> list) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            final SynchronousResultReceiver synchronousResultReceiver = new SynchronousResultReceiver("uwb");
            this.mUwbManager.getUwbActivityEnergyInfoAsync(new SystemServerInitThreadPool$.ExternalSyntheticLambda1(), new Consumer() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda14
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    StatsPullAtomService.lambda$pullUwbActivityInfoLocked$18(synchronousResultReceiver, (UwbActivityEnergyInfo) obj);
                }
            });
            UwbActivityEnergyInfo awaitControllerInfo = awaitControllerInfo(synchronousResultReceiver);
            if (awaitControllerInfo == null) {
                return 1;
            }
            list.add(FrameworkStatsLog.buildStatsEvent(i, awaitControllerInfo.getControllerTxDurationMillis(), awaitControllerInfo.getControllerRxDurationMillis(), awaitControllerInfo.getControllerIdleDurationMillis(), awaitControllerInfo.getControllerWakeCount()));
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return 0;
        } catch (RuntimeException e) {
            Slog.e(TAG, "failed to getUwbActivityEnergyInfoAsync", e);
            return 1;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$pullUwbActivityInfoLocked$18(SynchronousResultReceiver synchronousResultReceiver, UwbActivityEnergyInfo uwbActivityEnergyInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_RECEIVER_CONTROLLER_KEY, uwbActivityEnergyInfo);
        synchronousResultReceiver.send(0, bundle);
    }

    private void registerSystemElapsedRealtime() {
        this.mStatsManager.setPullAtomCallback(10014, new StatsManager.PullAtomMetadata.Builder().setCoolDownMillis(1000L).setTimeoutMillis(500L).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullSystemElapsedRealtimeLocked(int i, List<StatsEvent> list) {
        list.add(FrameworkStatsLog.buildStatsEvent(i, SystemClock.elapsedRealtime()));
        return 0;
    }

    private void registerSystemUptime() {
        this.mStatsManager.setPullAtomCallback(10015, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullSystemUptimeLocked(int i, List<StatsEvent> list) {
        list.add(FrameworkStatsLog.buildStatsEvent(i, SystemClock.uptimeMillis()));
        return 0;
    }

    private void registerProcessMemoryState() {
        this.mStatsManager.setPullAtomCallback(10013, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{4, 5, 6, 7, 8}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullProcessMemoryStateLocked(int i, List<StatsEvent> list) {
        for (ProcessMemoryState processMemoryState : ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).getMemoryStateForProcesses()) {
            MemoryStatUtil.MemoryStat readMemoryStatFromFilesystem = MemoryStatUtil.readMemoryStatFromFilesystem(processMemoryState.uid, processMemoryState.pid);
            if (readMemoryStatFromFilesystem != null) {
                list.add(FrameworkStatsLog.buildStatsEvent(i, processMemoryState.uid, processMemoryState.processName, processMemoryState.oomScore, readMemoryStatFromFilesystem.pgfault, readMemoryStatFromFilesystem.pgmajfault, readMemoryStatFromFilesystem.rssInBytes, readMemoryStatFromFilesystem.cacheInBytes, readMemoryStatFromFilesystem.swapInBytes, -1L, -1L, -1));
            }
        }
        return 0;
    }

    private void registerProcessMemoryHighWaterMark() {
        this.mStatsManager.setPullAtomCallback(10042, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullProcessMemoryHighWaterMarkLocked(int i, List<StatsEvent> list) {
        List<ProcessMemoryState> memoryStateForProcesses = ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).getMemoryStateForProcesses();
        for (ProcessMemoryState processMemoryState : memoryStateForProcesses) {
            ProcfsMemoryUtil.MemorySnapshot readMemorySnapshotFromProcfs = ProcfsMemoryUtil.readMemorySnapshotFromProcfs(processMemoryState.pid);
            if (readMemorySnapshotFromProcfs != null) {
                int i2 = processMemoryState.uid;
                String str = processMemoryState.processName;
                int i3 = readMemorySnapshotFromProcfs.rssHighWaterMarkInKilobytes;
                list.add(FrameworkStatsLog.buildStatsEvent(i, i2, str, i3 * 1024, i3));
            }
        }
        final SparseArray<String> processCmdlines = ProcfsMemoryUtil.getProcessCmdlines();
        memoryStateForProcesses.forEach(new Consumer() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda23
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                StatsPullAtomService.lambda$pullProcessMemoryHighWaterMarkLocked$19(processCmdlines, (ProcessMemoryState) obj);
            }
        });
        int size = processCmdlines.size();
        for (int i4 = 0; i4 < size; i4++) {
            ProcfsMemoryUtil.MemorySnapshot readMemorySnapshotFromProcfs2 = ProcfsMemoryUtil.readMemorySnapshotFromProcfs(processCmdlines.keyAt(i4));
            if (readMemorySnapshotFromProcfs2 != null) {
                int i5 = readMemorySnapshotFromProcfs2.uid;
                String valueAt = processCmdlines.valueAt(i4);
                int i6 = readMemorySnapshotFromProcfs2.rssHighWaterMarkInKilobytes;
                list.add(FrameworkStatsLog.buildStatsEvent(i, i5, valueAt, i6 * 1024, i6));
            }
        }
        SystemProperties.set("sys.rss_hwm_reset.on", "1");
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$pullProcessMemoryHighWaterMarkLocked$19(SparseArray sparseArray, ProcessMemoryState processMemoryState) {
        sparseArray.delete(processMemoryState.pid);
    }

    private void registerProcessMemorySnapshot() {
        this.mStatsManager.setPullAtomCallback(10064, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullProcessMemorySnapshot(int i, List<StatsEvent> list) {
        List<ProcessMemoryState> memoryStateForProcesses = ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).getMemoryStateForProcesses();
        KernelAllocationStats.ProcessGpuMem[] gpuAllocations = KernelAllocationStats.getGpuAllocations();
        SparseIntArray sparseIntArray = new SparseIntArray(gpuAllocations.length);
        for (KernelAllocationStats.ProcessGpuMem processGpuMem : gpuAllocations) {
            sparseIntArray.put(processGpuMem.pid, processGpuMem.gpuMemoryKb);
        }
        for (ProcessMemoryState processMemoryState : memoryStateForProcesses) {
            ProcfsMemoryUtil.MemorySnapshot readMemorySnapshotFromProcfs = ProcfsMemoryUtil.readMemorySnapshotFromProcfs(processMemoryState.pid);
            if (readMemorySnapshotFromProcfs != null) {
                int i2 = processMemoryState.uid;
                String str = processMemoryState.processName;
                int i3 = processMemoryState.pid;
                int i4 = processMemoryState.oomScore;
                int i5 = readMemorySnapshotFromProcfs.rssInKilobytes;
                int i6 = readMemorySnapshotFromProcfs.anonRssInKilobytes;
                int i7 = readMemorySnapshotFromProcfs.swapInKilobytes;
                list.add(FrameworkStatsLog.buildStatsEvent(i, i2, str, i3, i4, i5, i6, i7, i6 + i7, sparseIntArray.get(i3), processMemoryState.hasForegroundServices, readMemorySnapshotFromProcfs.rssShmemKilobytes, processMemoryState.mHostingComponentTypes, processMemoryState.mHistoricalHostingComponentTypes));
            }
        }
        final SparseArray<String> processCmdlines = ProcfsMemoryUtil.getProcessCmdlines();
        memoryStateForProcesses.forEach(new Consumer() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                StatsPullAtomService.lambda$pullProcessMemorySnapshot$20(processCmdlines, (ProcessMemoryState) obj);
            }
        });
        int size = processCmdlines.size();
        for (int i8 = 0; i8 < size; i8++) {
            int keyAt = processCmdlines.keyAt(i8);
            ProcfsMemoryUtil.MemorySnapshot readMemorySnapshotFromProcfs2 = ProcfsMemoryUtil.readMemorySnapshotFromProcfs(keyAt);
            if (readMemorySnapshotFromProcfs2 != null) {
                int i9 = readMemorySnapshotFromProcfs2.uid;
                String valueAt = processCmdlines.valueAt(i8);
                int i10 = readMemorySnapshotFromProcfs2.rssInKilobytes;
                int i11 = readMemorySnapshotFromProcfs2.anonRssInKilobytes;
                int i12 = readMemorySnapshotFromProcfs2.swapInKilobytes;
                list.add(FrameworkStatsLog.buildStatsEvent(i, i9, valueAt, keyAt, JobSchedulerShellCommand.CMD_ERR_NO_JOB, i10, i11, i12, i11 + i12, sparseIntArray.get(keyAt), false, readMemorySnapshotFromProcfs2.rssShmemKilobytes, 0, 0));
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$pullProcessMemorySnapshot$20(SparseArray sparseArray, ProcessMemoryState processMemoryState) {
        sparseArray.delete(processMemoryState.pid);
    }

    private void registerSystemIonHeapSize() {
        this.mStatsManager.setPullAtomCallback(10056, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullSystemIonHeapSizeLocked(int i, List<StatsEvent> list) {
        list.add(FrameworkStatsLog.buildStatsEvent(i, IonMemoryUtil.readSystemIonHeapSizeFromDebugfs()));
        return 0;
    }

    private void registerIonHeapSize() {
        if (new File("/sys/kernel/ion/total_heaps_kb").exists()) {
            this.mStatsManager.setPullAtomCallback(10070, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
        }
    }

    int pullIonHeapSizeLocked(int i, List<StatsEvent> list) {
        list.add(FrameworkStatsLog.buildStatsEvent(i, (int) Debug.getIonHeapsSizeKb()));
        return 0;
    }

    private void registerProcessSystemIonHeapSize() {
        this.mStatsManager.setPullAtomCallback(10061, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullProcessSystemIonHeapSizeLocked(int i, List<StatsEvent> list) {
        for (IonMemoryUtil.IonAllocations ionAllocations : IonMemoryUtil.readProcessSystemIonHeapSizesFromDebugfs()) {
            list.add(FrameworkStatsLog.buildStatsEvent(i, Process.getUidForPid(ionAllocations.pid), ProcfsMemoryUtil.readCmdlineFromProcfs(ionAllocations.pid), (int) (ionAllocations.totalSizeInBytes / 1024), ionAllocations.count, (int) (ionAllocations.maxSizeInBytes / 1024)));
        }
        return 0;
    }

    private void registerProcessDmabufMemory() {
        this.mStatsManager.setPullAtomCallback(10105, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullProcessDmabufMemory(int i, List<StatsEvent> list) {
        KernelAllocationStats.ProcessDmabuf[] dmabufAllocations = KernelAllocationStats.getDmabufAllocations();
        if (dmabufAllocations == null) {
            return 1;
        }
        for (KernelAllocationStats.ProcessDmabuf processDmabuf : dmabufAllocations) {
            list.add(FrameworkStatsLog.buildStatsEvent(i, processDmabuf.uid, processDmabuf.processName, processDmabuf.oomScore, processDmabuf.retainedSizeKb, processDmabuf.retainedBuffersCount, 0, 0, processDmabuf.surfaceFlingerSizeKb, processDmabuf.surfaceFlingerCount));
        }
        return 0;
    }

    private void registerSystemMemory() {
        this.mStatsManager.setPullAtomCallback(10092, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullSystemMemory(int i, List<StatsEvent> list) {
        SystemMemoryUtil.Metrics metrics = SystemMemoryUtil.getMetrics();
        list.add(FrameworkStatsLog.buildStatsEvent(i, metrics.unreclaimableSlabKb, metrics.vmallocUsedKb, metrics.pageTablesKb, metrics.kernelStackKb, metrics.totalIonKb, metrics.unaccountedKb, metrics.gpuTotalUsageKb, metrics.gpuPrivateAllocationsKb, metrics.dmaBufTotalExportedKb, metrics.shmemKb, metrics.totalKb, metrics.freeKb, metrics.availableKb, metrics.activeKb, metrics.inactiveKb, metrics.activeAnonKb, metrics.inactiveAnonKb, metrics.activeFileKb, metrics.inactiveFileKb, metrics.swapTotalKb, metrics.swapFreeKb, metrics.cmaTotalKb, metrics.cmaFreeKb));
        return 0;
    }

    private void registerVmStat() {
        this.mStatsManager.setPullAtomCallback(10117, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullVmStat(int i, List<StatsEvent> list) {
        ProcfsMemoryUtil.VmStat readVmStat = ProcfsMemoryUtil.readVmStat();
        if (readVmStat == null) {
            return 0;
        }
        list.add(FrameworkStatsLog.buildStatsEvent(i, readVmStat.oomKillCount));
        return 0;
    }

    private void registerTemperature() {
        this.mStatsManager.setPullAtomCallback(OplusNotificationListenerService.REASON_OPLUSOS_BPMSUSPEND, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullTemperatureLocked(int i, List<StatsEvent> list) {
        IThermalService iThermalService = getIThermalService();
        if (iThermalService == null) {
            return 1;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (Temperature temperature : iThermalService.getCurrentTemperatures()) {
                list.add(FrameworkStatsLog.buildStatsEvent(i, temperature.getType(), temperature.getName(), (int) (temperature.getValue() * 10.0f), temperature.getStatus()));
            }
            return 0;
        } catch (RemoteException unused) {
            Slog.e(TAG, "Disconnected from thermal service. Cannot pull temperatures.");
            return 1;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void registerCoolingDevice() {
        this.mStatsManager.setPullAtomCallback(10059, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCooldownDeviceLocked(int i, List<StatsEvent> list) {
        IThermalService iThermalService = getIThermalService();
        if (iThermalService == null) {
            return 1;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (CoolingDevice coolingDevice : iThermalService.getCurrentCoolingDevices()) {
                list.add(FrameworkStatsLog.buildStatsEvent(i, coolingDevice.getType(), coolingDevice.getName(), (int) coolingDevice.getValue()));
            }
            return 0;
        } catch (RemoteException unused) {
            Slog.e(TAG, "Disconnected from thermal service. Cannot pull temperatures.");
            return 1;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void registerBinderCallsStats() {
        this.mStatsManager.setPullAtomCallback(10022, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{4, 5, 6, 8, 12}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullBinderCallsStatsLocked(int i, List<StatsEvent> list) {
        BinderCallsStatsService.Internal internal = (BinderCallsStatsService.Internal) LocalServices.getService(BinderCallsStatsService.Internal.class);
        if (internal == null) {
            Slog.e(TAG, "failed to get binderStats");
            return 1;
        }
        ArrayList<BinderCallsStats.ExportedCallStat> exportedCallStats = internal.getExportedCallStats();
        internal.reset();
        for (BinderCallsStats.ExportedCallStat exportedCallStat : exportedCallStats) {
            list.add(FrameworkStatsLog.buildStatsEvent(i, exportedCallStat.workSourceUid, exportedCallStat.className, exportedCallStat.methodName, exportedCallStat.callCount, exportedCallStat.exceptionCount, exportedCallStat.latencyMicros, exportedCallStat.maxLatencyMicros, exportedCallStat.cpuTimeMicros, exportedCallStat.maxCpuTimeMicros, exportedCallStat.maxReplySizeBytes, exportedCallStat.maxRequestSizeBytes, exportedCallStat.recordedCallCount, exportedCallStat.screenInteractive, exportedCallStat.callingUid));
        }
        return 0;
    }

    private void registerBinderCallsStatsExceptions() {
        this.mStatsManager.setPullAtomCallback(10023, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullBinderCallsStatsExceptionsLocked(int i, List<StatsEvent> list) {
        BinderCallsStatsService.Internal internal = (BinderCallsStatsService.Internal) LocalServices.getService(BinderCallsStatsService.Internal.class);
        if (internal == null) {
            Slog.e(TAG, "failed to get binderStats");
            return 1;
        }
        Iterator it = internal.getExportedExceptionStats().entrySet().iterator();
        while (it.hasNext()) {
            list.add(FrameworkStatsLog.buildStatsEvent(i, (String) ((Map.Entry) it.next()).getKey(), ((Integer) r0.getValue()).intValue()));
        }
        return 0;
    }

    private void registerLooperStats() {
        this.mStatsManager.setPullAtomCallback(10024, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{5, 6, 7, 8, 9}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullLooperStatsLocked(int i, List<StatsEvent> list) {
        LooperStats looperStats = (LooperStats) LocalServices.getService(LooperStats.class);
        if (looperStats == null) {
            return 1;
        }
        List<LooperStats.ExportedEntry> entries = looperStats.getEntries();
        looperStats.reset();
        for (LooperStats.ExportedEntry exportedEntry : entries) {
            list.add(FrameworkStatsLog.buildStatsEvent(i, exportedEntry.workSourceUid, exportedEntry.handlerClassName, exportedEntry.threadName, exportedEntry.messageName, exportedEntry.messageCount, exportedEntry.exceptionCount, exportedEntry.recordedMessageCount, exportedEntry.totalLatencyMicros, exportedEntry.cpuUsageMicros, exportedEntry.isInteractive, exportedEntry.maxCpuUsageMicros, exportedEntry.maxLatencyMicros, exportedEntry.recordedDelayMessageCount, exportedEntry.delayMillis, exportedEntry.maxDelayMillis));
        }
        return 0;
    }

    private void registerDiskStats() {
        this.mStatsManager.setPullAtomCallback(10025, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x003a, code lost:
    
        if (r6 == null) goto L23;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [java.io.IOException] */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.io.IOException] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    int pullDiskStatsLocked(int i, List<StatsEvent> list) {
        FileOutputStream fileOutputStream;
        int i2;
        byte[] bArr = new byte[512];
        for (int i3 = 0; i3 < 512; i3++) {
            bArr[i3] = (byte) i3;
        }
        File file = new File(Environment.getDataDirectory(), "system/statsdperftest.tmp");
        long elapsedRealtime = SystemClock.elapsedRealtime();
        FileOutputStream fileOutputStream2 = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            try {
                fileOutputStream.write(bArr);
            } catch (IOException e) {
                e = e;
                fileOutputStream2 = e;
            } catch (Throwable th) {
                th = th;
                fileOutputStream2 = fileOutputStream;
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (IOException unused) {
                    }
                }
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            fileOutputStream = null;
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            fileOutputStream.close();
        } catch (IOException unused2) {
            long elapsedRealtime2 = SystemClock.elapsedRealtime() - elapsedRealtime;
            if (file.exists()) {
                file.delete();
            }
            if (fileOutputStream2 != null) {
                Slog.e(TAG, "Error performing diskstats latency test");
                elapsedRealtime2 = -1;
            }
            boolean isFileEncrypted = StorageManager.isFileEncrypted();
            IStoraged iStoragedService = getIStoragedService();
            if (iStoragedService == null) {
                return 1;
            }
            try {
                i2 = iStoragedService.getRecentPerf();
            } catch (RemoteException unused3) {
                Slog.e(TAG, "storaged not found");
                i2 = -1;
            }
            list.add(FrameworkStatsLog.buildStatsEvent(i, elapsedRealtime2, isFileEncrypted, i2));
            return 0;
        }
    }

    private void registerDirectoryUsage() {
        this.mStatsManager.setPullAtomCallback(10026, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullDirectoryUsageLocked(int i, List<StatsEvent> list) {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        StatFs statFs2 = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        StatFs statFs3 = new StatFs(Environment.getDownloadCacheDirectory().getAbsolutePath());
        StatFs statFs4 = new StatFs(Environment.getMetadataDirectory().getAbsolutePath());
        list.add(FrameworkStatsLog.buildStatsEvent(i, 1, statFs.getAvailableBytes(), statFs.getTotalBytes()));
        list.add(FrameworkStatsLog.buildStatsEvent(i, 2, statFs3.getAvailableBytes(), statFs3.getTotalBytes()));
        list.add(FrameworkStatsLog.buildStatsEvent(i, 3, statFs2.getAvailableBytes(), statFs2.getTotalBytes()));
        list.add(FrameworkStatsLog.buildStatsEvent(i, 4, statFs4.getAvailableBytes(), statFs4.getTotalBytes()));
        return 0;
    }

    private void registerAppSize() {
        this.mStatsManager.setPullAtomCallback(10027, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullAppSizeLocked(int i, List<StatsEvent> list) {
        try {
            JSONObject jSONObject = new JSONObject(IoUtils.readFileAsString(DiskStatsLoggingService.DUMPSYS_CACHE_PATH));
            long optLong = jSONObject.optLong(DiskStatsFileLogger.LAST_QUERY_TIMESTAMP_KEY, -1L);
            JSONArray jSONArray = jSONObject.getJSONArray(DiskStatsFileLogger.PACKAGE_NAMES_KEY);
            JSONArray jSONArray2 = jSONObject.getJSONArray(DiskStatsFileLogger.APP_SIZES_KEY);
            JSONArray jSONArray3 = jSONObject.getJSONArray(DiskStatsFileLogger.APP_DATA_KEY);
            JSONArray jSONArray4 = jSONObject.getJSONArray(DiskStatsFileLogger.APP_CACHES_KEY);
            int length = jSONArray.length();
            if (jSONArray2.length() == length && jSONArray3.length() == length && jSONArray4.length() == length) {
                int i2 = 0;
                while (i2 < length) {
                    list.add(FrameworkStatsLog.buildStatsEvent(i, jSONArray.getString(i2), jSONArray2.optLong(i2, -1L), jSONArray3.optLong(i2, -1L), jSONArray4.optLong(i2, -1L), optLong));
                    i2++;
                    jSONArray2 = jSONArray2;
                    jSONArray3 = jSONArray3;
                    length = length;
                }
                return 0;
            }
            Slog.e(TAG, "formatting error in diskstats cache file!");
            return 1;
        } catch (IOException | JSONException unused) {
            Slog.w(TAG, "Unable to read diskstats cache file within pullAppSize");
            return 1;
        }
    }

    private void registerCategorySize() {
        this.mStatsManager.setPullAtomCallback(10028, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCategorySizeLocked(int i, List<StatsEvent> list) {
        try {
            JSONObject jSONObject = new JSONObject(IoUtils.readFileAsString(DiskStatsLoggingService.DUMPSYS_CACHE_PATH));
            long optLong = jSONObject.optLong(DiskStatsFileLogger.LAST_QUERY_TIMESTAMP_KEY, -1L);
            list.add(FrameworkStatsLog.buildStatsEvent(i, 1, jSONObject.optLong(DiskStatsFileLogger.APP_SIZE_AGG_KEY, -1L), optLong));
            list.add(FrameworkStatsLog.buildStatsEvent(i, 2, jSONObject.optLong(DiskStatsFileLogger.APP_DATA_SIZE_AGG_KEY, -1L), optLong));
            list.add(FrameworkStatsLog.buildStatsEvent(i, 3, jSONObject.optLong(DiskStatsFileLogger.APP_CACHE_AGG_KEY, -1L), optLong));
            list.add(FrameworkStatsLog.buildStatsEvent(i, 4, jSONObject.optLong(DiskStatsFileLogger.PHOTOS_KEY, -1L), optLong));
            list.add(FrameworkStatsLog.buildStatsEvent(i, 5, jSONObject.optLong(DiskStatsFileLogger.VIDEOS_KEY, -1L), optLong));
            list.add(FrameworkStatsLog.buildStatsEvent(i, 6, jSONObject.optLong(DiskStatsFileLogger.AUDIO_KEY, -1L), optLong));
            list.add(FrameworkStatsLog.buildStatsEvent(i, 7, jSONObject.optLong(DiskStatsFileLogger.DOWNLOADS_KEY, -1L), optLong));
            list.add(FrameworkStatsLog.buildStatsEvent(i, 8, jSONObject.optLong(DiskStatsFileLogger.SYSTEM_KEY, -1L), optLong));
            list.add(FrameworkStatsLog.buildStatsEvent(i, 9, jSONObject.optLong(DiskStatsFileLogger.MISC_KEY, -1L), optLong));
            return 0;
        } catch (IOException | JSONException unused) {
            Slog.w(TAG, "Unable to read diskstats cache file within pullCategorySize");
            return 1;
        }
    }

    private void registerNumFingerprintsEnrolled() {
        this.mStatsManager.setPullAtomCallback(10031, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerNumFacesEnrolled() {
        this.mStatsManager.setPullAtomCallback(10048, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullNumBiometricsEnrolledLocked(int i, int i2, List<StatsEvent> list) {
        UserManager userManager;
        int size;
        PackageManager packageManager = this.mContext.getPackageManager();
        FingerprintManager fingerprintManager = packageManager.hasSystemFeature("android.hardware.fingerprint") ? (FingerprintManager) this.mContext.getSystemService(FingerprintManager.class) : null;
        FaceManager faceManager = packageManager.hasSystemFeature("android.hardware.biometrics.face") ? (FaceManager) this.mContext.getSystemService(FaceManager.class) : null;
        if (i == 1 && fingerprintManager == null) {
            return 1;
        }
        if ((i == 4 && faceManager == null) || (userManager = (UserManager) this.mContext.getSystemService(UserManager.class)) == null) {
            return 1;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Iterator it = userManager.getUsers().iterator();
            while (it.hasNext()) {
                int identifier = ((UserInfo) it.next()).getUserHandle().getIdentifier();
                if (i == 1) {
                    size = fingerprintManager.getEnrolledFingerprints(identifier).size();
                } else {
                    if (i != 4) {
                        return 1;
                    }
                    size = faceManager.getEnrolledFaces(identifier).size();
                }
                list.add(FrameworkStatsLog.buildStatsEvent(i2, identifier, size));
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return 0;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void registerProcStats() {
        this.mStatsManager.setPullAtomCallback(10029, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerProcStatsPkgProc() {
        this.mStatsManager.setPullAtomCallback(10034, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerProcessState() {
        this.mStatsManager.setPullAtomCallback(10171, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerProcessAssociation() {
        this.mStatsManager.setPullAtomCallback(10172, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    @GuardedBy({"mProcStatsLock"})
    private ProcessStats getStatsFromProcessStatsService(int i) {
        IProcessStats iProcessStatsService = getIProcessStatsService();
        if (iProcessStatsService == null) {
            return null;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            long readProcStatsHighWaterMark = readProcStatsHighWaterMark(i);
            ProcessStats processStats = new ProcessStats(false);
            long committedStatsMerged = iProcessStatsService.getCommittedStatsMerged(readProcStatsHighWaterMark, 31, true, (List) null, processStats);
            new File(this.mBaseDir.getAbsolutePath() + SliceClientPermissions.SliceAuthority.DELIMITER + highWaterMarkFilePrefix(i) + "_" + readProcStatsHighWaterMark).delete();
            new File(this.mBaseDir.getAbsolutePath() + SliceClientPermissions.SliceAuthority.DELIMITER + highWaterMarkFilePrefix(i) + "_" + committedStatsMerged).createNewFile();
            return processStats;
        } catch (RemoteException | IOException e) {
            Slog.e(TAG, "Getting procstats failed: ", e);
            return null;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mProcStatsLock"})
    public int pullProcStatsLocked(int i, List<StatsEvent> list) {
        ProcessStats statsFromProcessStatsService = getStatsFromProcessStatsService(i);
        if (statsFromProcessStatsService == null) {
            return 1;
        }
        ProtoOutputStream[] protoOutputStreamArr = new ProtoOutputStream[5];
        for (int i2 = 0; i2 < 5; i2++) {
            protoOutputStreamArr[i2] = new ProtoOutputStream();
        }
        statsFromProcessStatsService.dumpAggregatedProtoForStatsd(protoOutputStreamArr, 58982L);
        for (int i3 = 0; i3 < 5; i3++) {
            byte[] bytes = protoOutputStreamArr[i3].getBytes();
            if (bytes.length > 0) {
                list.add(FrameworkStatsLog.buildStatsEvent(i, bytes, i3));
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mProcStatsLock"})
    public int pullProcessStateLocked(int i, List<StatsEvent> list) {
        ProcessStats statsFromProcessStatsService = getStatsFromProcessStatsService(i);
        if (statsFromProcessStatsService == null) {
            return 1;
        }
        statsFromProcessStatsService.dumpProcessState(i, new StatsEventOutput(list));
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mProcStatsLock"})
    public int pullProcessAssociationLocked(int i, List<StatsEvent> list) {
        ProcessStats statsFromProcessStatsService = getStatsFromProcessStatsService(i);
        if (statsFromProcessStatsService == null) {
            return 1;
        }
        statsFromProcessStatsService.dumpProcessAssociation(i, new StatsEventOutput(list));
        return 0;
    }

    private String highWaterMarkFilePrefix(int i) {
        if (i == 10029) {
            return String.valueOf(31);
        }
        if (i == 10034) {
            return String.valueOf(2);
        }
        return "atom-" + i;
    }

    private long readProcStatsHighWaterMark(final int i) {
        try {
            File[] listFiles = this.mBaseDir.listFiles(new FilenameFilter() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda7
                @Override // java.io.FilenameFilter
                public final boolean accept(File file, String str) {
                    boolean lambda$readProcStatsHighWaterMark$21;
                    lambda$readProcStatsHighWaterMark$21 = StatsPullAtomService.this.lambda$readProcStatsHighWaterMark$21(i, file, str);
                    return lambda$readProcStatsHighWaterMark$21;
                }
            });
            if (listFiles != null && listFiles.length != 0) {
                if (listFiles.length > 1) {
                    Slog.e(TAG, "Only 1 file expected for high water mark. Found " + listFiles.length);
                }
                return Long.valueOf(listFiles[0].getName().split("_")[1]).longValue();
            }
            return 0L;
        } catch (NumberFormatException e) {
            Slog.e(TAG, "Failed to parse file name.", e);
            return 0L;
        } catch (SecurityException e2) {
            Slog.e(TAG, "Failed to get procstats high watermark file.", e2);
            return 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$readProcStatsHighWaterMark$21(int i, File file, String str) {
        return str.toLowerCase().startsWith(highWaterMarkFilePrefix(i) + '_');
    }

    private void registerDiskIO() {
        this.mStatsManager.setPullAtomCallback(10032, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11}).setCoolDownMillis(3000L).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullDiskIOLocked(final int i, final List<StatsEvent> list) {
        this.mStoragedUidIoStatsReader.readAbsolute(new StoragedUidIoStatsReader.Callback() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda2
            public final void onUidStorageStats(int i2, long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10) {
                StatsPullAtomService.lambda$pullDiskIOLocked$22(list, i, i2, j, j2, j3, j4, j5, j6, j7, j8, j9, j10);
            }
        });
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$pullDiskIOLocked$22(List list, int i, int i2, long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10) {
        list.add(FrameworkStatsLog.buildStatsEvent(i, i2, j, j2, j3, j4, j5, j6, j7, j8, j9, j10));
    }

    private void registerPowerProfile() {
        this.mStatsManager.setPullAtomCallback(10033, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullPowerProfileLocked(int i, List<StatsEvent> list) {
        PowerProfile powerProfile = new PowerProfile(this.mContext);
        ProtoOutputStream protoOutputStream = new ProtoOutputStream();
        powerProfile.dumpDebug(protoOutputStream);
        protoOutputStream.flush();
        list.add(FrameworkStatsLog.buildStatsEvent(i, protoOutputStream.getBytes()));
        return 0;
    }

    private void registerProcessCpuTime() {
        this.mStatsManager.setPullAtomCallback(10035, new StatsManager.PullAtomMetadata.Builder().setCoolDownMillis(5000L).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullProcessCpuTimeLocked(int i, List<StatsEvent> list) {
        if (this.mProcessCpuTracker == null) {
            ProcessCpuTracker processCpuTracker = new ProcessCpuTracker(false);
            this.mProcessCpuTracker = processCpuTracker;
            processCpuTracker.init();
        }
        this.mProcessCpuTracker.update();
        for (int i2 = 0; i2 < this.mProcessCpuTracker.countStats(); i2++) {
            ProcessCpuTracker.Stats stats = this.mProcessCpuTracker.getStats(i2);
            list.add(FrameworkStatsLog.buildStatsEvent(i, stats.uid, stats.name, stats.base_utime, stats.base_stime));
        }
        return 0;
    }

    private void registerCpuTimePerThreadFreq() {
        this.mStatsManager.setPullAtomCallback(10037, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{7, 9, 11, 13, 15, 17, 19, 21}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCpuTimePerThreadFreqLocked(int i, List<StatsEvent> list) {
        KernelCpuThreadReaderDiff kernelCpuThreadReaderDiff = this.mKernelCpuThreadReader;
        if (kernelCpuThreadReaderDiff == null) {
            Slog.e(TAG, "mKernelCpuThreadReader is null");
            return 1;
        }
        ArrayList processCpuUsageDiffed = kernelCpuThreadReaderDiff.getProcessCpuUsageDiffed();
        if (processCpuUsageDiffed == null) {
            Slog.e(TAG, "processCpuUsages is null");
            return 1;
        }
        int[] cpuFrequenciesKhz = this.mKernelCpuThreadReader.getCpuFrequenciesKhz();
        if (cpuFrequenciesKhz.length > 8) {
            Slog.w(TAG, "Expected maximum 8 frequencies, but got " + cpuFrequenciesKhz.length);
            return 1;
        }
        for (int i2 = 0; i2 < processCpuUsageDiffed.size(); i2++) {
            KernelCpuThreadReader.ProcessCpuUsage processCpuUsage = (KernelCpuThreadReader.ProcessCpuUsage) processCpuUsageDiffed.get(i2);
            ArrayList arrayList = processCpuUsage.threadCpuUsages;
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                KernelCpuThreadReader.ThreadCpuUsage threadCpuUsage = (KernelCpuThreadReader.ThreadCpuUsage) arrayList.get(i3);
                if (threadCpuUsage.usageTimesMillis.length != cpuFrequenciesKhz.length) {
                    Slog.w(TAG, "Unexpected number of usage times, expected " + cpuFrequenciesKhz.length + " but got " + threadCpuUsage.usageTimesMillis.length);
                    return 1;
                }
                int[] iArr = new int[8];
                int[] iArr2 = new int[8];
                for (int i4 = 0; i4 < 8; i4++) {
                    if (i4 < cpuFrequenciesKhz.length) {
                        iArr[i4] = cpuFrequenciesKhz[i4];
                        iArr2[i4] = threadCpuUsage.usageTimesMillis[i4];
                    } else {
                        iArr[i4] = 0;
                        iArr2[i4] = 0;
                    }
                }
                list.add(FrameworkStatsLog.buildStatsEvent(i, processCpuUsage.uid, processCpuUsage.processId, threadCpuUsage.threadId, processCpuUsage.processName, threadCpuUsage.threadName, iArr[0], iArr2[0], iArr[1], iArr2[1], iArr[2], iArr2[2], iArr[3], iArr2[3], iArr[4], iArr2[4], iArr[5], iArr2[5], iArr[6], iArr2[6], iArr[7], iArr2[7]));
            }
        }
        return 0;
    }

    private void registerDeviceCalculatedPowerUse() {
        this.mStatsManager.setPullAtomCallback(10039, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullDeviceCalculatedPowerUseLocked(int i, List<StatsEvent> list) {
        try {
            list.add(FrameworkStatsLog.buildStatsEvent(i, milliAmpHrsToNanoAmpSecs(((BatteryStatsManager) this.mContext.getSystemService(BatteryStatsManager.class)).getBatteryUsageStats().getConsumedPower())));
            return 0;
        } catch (Exception e) {
            Log.e(TAG, "Could not obtain battery usage stats", e);
            return 1;
        }
    }

    private void registerDebugElapsedClock() {
        this.mStatsManager.setPullAtomCallback(10046, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{1, 2, 3, 4}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullDebugElapsedClockLocked(int i, List<StatsEvent> list) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mDebugElapsedClockPreviousValue;
        long j2 = j == 0 ? 0L : elapsedRealtime - j;
        list.add(FrameworkStatsLog.buildStatsEvent(i, this.mDebugElapsedClockPullCount, elapsedRealtime, elapsedRealtime, j2, 1));
        long j3 = this.mDebugElapsedClockPullCount;
        if (j3 % 2 == 1) {
            list.add(FrameworkStatsLog.buildStatsEvent(i, j3, elapsedRealtime, elapsedRealtime, j2, 2));
        }
        this.mDebugElapsedClockPullCount++;
        this.mDebugElapsedClockPreviousValue = elapsedRealtime;
        return 0;
    }

    private void registerDebugFailingElapsedClock() {
        this.mStatsManager.setPullAtomCallback(10047, new StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{1, 2, 3, 4}).build(), ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullDebugFailingElapsedClockLocked(int i, List<StatsEvent> list) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mDebugFailingElapsedClockPullCount;
        long j2 = 1 + j;
        this.mDebugFailingElapsedClockPullCount = j2;
        if (j % 5 == 0) {
            this.mDebugFailingElapsedClockPreviousValue = elapsedRealtime;
            Slog.e(TAG, "Failing debug elapsed clock");
            return 1;
        }
        long j3 = this.mDebugFailingElapsedClockPreviousValue;
        list.add(FrameworkStatsLog.buildStatsEvent(i, j2, elapsedRealtime, elapsedRealtime, j3 == 0 ? 0L : elapsedRealtime - j3));
        this.mDebugFailingElapsedClockPreviousValue = elapsedRealtime;
        return 0;
    }

    private void registerBuildInformation() {
        this.mStatsManager.setPullAtomCallback(10044, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullBuildInformationLocked(int i, List<StatsEvent> list) {
        list.add(FrameworkStatsLog.buildStatsEvent(i, Build.FINGERPRINT, Build.BRAND, Build.PRODUCT, Build.DEVICE, Build.VERSION.RELEASE_OR_CODENAME, Build.ID, Build.VERSION.INCREMENTAL, Build.TYPE, Build.TAGS));
        return 0;
    }

    private void registerRoleHolder() {
        this.mStatsManager.setPullAtomCallback(10049, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullRoleHolderLocked(int i, List<StatsEvent> list) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            PackageManager packageManager = this.mContext.getPackageManager();
            RoleManagerLocal roleManagerLocal = (RoleManagerLocal) LocalManagerRegistry.getManager(RoleManagerLocal.class);
            List users = ((UserManager) this.mContext.getSystemService(UserManager.class)).getUsers();
            int size = users.size();
            for (int i2 = 0; i2 < size; i2++) {
                int identifier = ((UserInfo) users.get(i2)).getUserHandle().getIdentifier();
                for (Map.Entry entry : roleManagerLocal.getRolesAndHolders(identifier).entrySet()) {
                    String str = (String) entry.getKey();
                    for (String str2 : (Set) entry.getValue()) {
                        try {
                            list.add(FrameworkStatsLog.buildStatsEvent(i, packageManager.getPackageInfoAsUser(str2, 0, identifier).applicationInfo.uid, str2, str));
                        } catch (PackageManager.NameNotFoundException unused) {
                            Slog.w(TAG, "Role holder " + str2 + " not found");
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                            return 1;
                        }
                    }
                }
            }
            return 0;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void registerDangerousPermissionState() {
        this.mStatsManager.setPullAtomCallback(10050, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullDangerousPermissionStateLocked(int i, List<StatsEvent> list) {
        PackageInfo packageInfo;
        int i2;
        List list2;
        UserHandle userHandle;
        int i3;
        int i4;
        StatsEvent buildStatsEvent;
        int i5 = i;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        float f = DeviceConfig.getFloat("permissions", DANGEROUS_PERMISSION_STATE_SAMPLE_RATE, 0.015f);
        HashSet hashSet = new HashSet();
        try {
            PackageManager packageManager = this.mContext.getPackageManager();
            List users = ((UserManager) this.mContext.getSystemService(UserManager.class)).getUsers();
            int size = users.size();
            int i6 = 0;
            while (i6 < size) {
                UserHandle userHandle2 = ((UserInfo) users.get(i6)).getUserHandle();
                List installedPackagesAsUser = packageManager.getInstalledPackagesAsUser(4096, userHandle2.getIdentifier());
                int size2 = installedPackagesAsUser.size();
                int i7 = 0;
                while (i7 < size2) {
                    PackageInfo packageInfo2 = (PackageInfo) installedPackagesAsUser.get(i7);
                    if (packageInfo2.requestedPermissions != null && !hashSet.contains(Integer.valueOf(packageInfo2.applicationInfo.uid))) {
                        hashSet.add(Integer.valueOf(packageInfo2.applicationInfo.uid));
                        if (i5 != 10067 || ThreadLocalRandom.current().nextFloat() <= f) {
                            int length = packageInfo2.requestedPermissions.length;
                            int i8 = 0;
                            while (i8 < length) {
                                int i9 = i7;
                                String str = packageInfo2.requestedPermissions[i8];
                                float f2 = f;
                                try {
                                    PermissionInfo permissionInfo = packageManager.getPermissionInfo(str, 0);
                                    try {
                                        int permissionFlags = packageManager.getPermissionFlags(str, packageInfo2.packageName, userHandle2);
                                        i2 = size2;
                                        if (str.startsWith(COMMON_PERMISSION_PREFIX)) {
                                            str = str.substring(19);
                                        }
                                        if (i5 == 10050) {
                                            int i10 = packageInfo2.applicationInfo.uid;
                                            list2 = installedPackagesAsUser;
                                            packageInfo = packageInfo2;
                                            userHandle = userHandle2;
                                            boolean z = (packageInfo2.requestedPermissionsFlags[i8] & 2) != 0;
                                            i3 = i6;
                                            i4 = i8;
                                            buildStatsEvent = FrameworkStatsLog.buildStatsEvent(i, str, i10, "", z, permissionFlags, permissionInfo.getProtection() | permissionInfo.getProtectionFlags());
                                        } else {
                                            packageInfo = packageInfo2;
                                            list2 = installedPackagesAsUser;
                                            userHandle = userHandle2;
                                            i3 = i6;
                                            i4 = i8;
                                            buildStatsEvent = FrameworkStatsLog.buildStatsEvent(i, str, packageInfo.applicationInfo.uid, (packageInfo.requestedPermissionsFlags[i4] & 2) != 0, permissionFlags, permissionInfo.getProtection() | permissionInfo.getProtectionFlags());
                                        }
                                        list.add(buildStatsEvent);
                                    } catch (PackageManager.NameNotFoundException unused) {
                                        packageInfo = packageInfo2;
                                        i2 = size2;
                                        list2 = installedPackagesAsUser;
                                        userHandle = userHandle2;
                                        i3 = i6;
                                        i4 = i8;
                                    }
                                } catch (PackageManager.NameNotFoundException unused2) {
                                    packageInfo = packageInfo2;
                                    i2 = size2;
                                    list2 = installedPackagesAsUser;
                                    userHandle = userHandle2;
                                    i3 = i6;
                                    i4 = i8;
                                }
                                i8 = i4 + 1;
                                packageInfo2 = packageInfo;
                                i7 = i9;
                                f = f2;
                                size2 = i2;
                                userHandle2 = userHandle;
                                installedPackagesAsUser = list2;
                                i6 = i3;
                                i5 = i;
                            }
                        }
                    }
                    i5 = i;
                    i7++;
                    f = f;
                    size2 = size2;
                    userHandle2 = userHandle2;
                    installedPackagesAsUser = installedPackagesAsUser;
                    i6 = i6;
                }
                i6++;
                i5 = i;
            }
            return 0;
        } catch (Throwable th) {
            try {
                Log.e(TAG, "Could not read permissions", th);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return 1;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    private void registerTimeZoneDataInfo() {
        this.mStatsManager.setPullAtomCallback(10052, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullTimeZoneDataInfoLocked(int i, List<StatsEvent> list) {
        try {
            list.add(FrameworkStatsLog.buildStatsEvent(i, TimeZone.getTZDataVersion()));
            return 0;
        } catch (MissingResourceException e) {
            Slog.e(TAG, "Getting tzdb version failed: ", e);
            return 1;
        }
    }

    private void registerTimeZoneDetectorState() {
        this.mStatsManager.setPullAtomCallback(10102, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullTimeZoneDetectorStateLocked(int i, List<StatsEvent> list) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                MetricsTimeZoneDetectorState generateMetricsState = ((TimeZoneDetectorInternal) LocalServices.getService(TimeZoneDetectorInternal.class)).generateMetricsState();
                list.add(FrameworkStatsLog.buildStatsEvent(i, generateMetricsState.isTelephonyDetectionSupported(), generateMetricsState.isGeoDetectionSupported(), generateMetricsState.getUserLocationEnabledSetting(), generateMetricsState.getAutoDetectionEnabledSetting(), generateMetricsState.getGeoDetectionEnabledSetting(), convertToMetricsDetectionMode(generateMetricsState.getDetectionMode()), generateMetricsState.getDeviceTimeZoneIdOrdinal(), convertTimeZoneSuggestionToProtoBytes(generateMetricsState.getLatestManualSuggestion()), convertTimeZoneSuggestionToProtoBytes(generateMetricsState.getLatestTelephonySuggestion()), convertTimeZoneSuggestionToProtoBytes(generateMetricsState.getLatestGeolocationSuggestion()), generateMetricsState.isTelephonyTimeZoneFallbackSupported(), generateMetricsState.getDeviceTimeZoneId(), generateMetricsState.isEnhancedMetricsCollectionEnabled(), generateMetricsState.getGeoDetectionRunInBackgroundEnabled()));
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return 0;
            } catch (RuntimeException e) {
                Slog.e(TAG, "Getting time zone detection state failed: ", e);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return 1;
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    private static byte[] convertTimeZoneSuggestionToProtoBytes(MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion metricsTimeZoneSuggestion) {
        if (metricsTimeZoneSuggestion == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ProtoOutputStream protoOutputStream = new ProtoOutputStream(byteArrayOutputStream);
        protoOutputStream.write(1159641169921L, metricsTimeZoneSuggestion.isCertain() ? 1 : 2);
        if (metricsTimeZoneSuggestion.isCertain()) {
            for (int i : metricsTimeZoneSuggestion.getZoneIdOrdinals()) {
                protoOutputStream.write(2220498092034L, i);
            }
            String[] zoneIds = metricsTimeZoneSuggestion.getZoneIds();
            if (zoneIds != null) {
                for (String str : zoneIds) {
                    protoOutputStream.write(2237677961219L, str);
                }
            }
        }
        protoOutputStream.flush();
        IoUtils.closeQuietly(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void registerExternalStorageInfo() {
        this.mStatsManager.setPullAtomCallback(10053, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullExternalStorageInfoLocked(int i, List<StatsEvent> list) {
        int i2;
        StorageManager storageManager = this.mStorageManager;
        if (storageManager == null) {
            return 1;
        }
        for (VolumeInfo volumeInfo : storageManager.getVolumes()) {
            String environmentForState = VolumeInfo.getEnvironmentForState(volumeInfo.getState());
            DiskInfo disk = volumeInfo.getDisk();
            if (disk != null && environmentForState.equals("mounted")) {
                int i3 = 2;
                if (volumeInfo.getType() == 0) {
                    i2 = 1;
                } else {
                    i2 = volumeInfo.getType() == 1 ? 2 : 3;
                }
                if (disk.isSd()) {
                    i3 = 1;
                } else if (!disk.isUsb()) {
                    i3 = 3;
                }
                list.add(FrameworkStatsLog.buildStatsEvent(i, i3, i2, disk.size));
            }
        }
        return 0;
    }

    private void registerAppsOnExternalStorageInfo() {
        this.mStatsManager.setPullAtomCallback(10057, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullAppsOnExternalStorageInfoLocked(int i, List<StatsEvent> list) {
        VolumeInfo findVolumeByUuid;
        DiskInfo disk;
        int i2;
        if (this.mStorageManager == null) {
            return 1;
        }
        for (ApplicationInfo applicationInfo : this.mContext.getPackageManager().getInstalledApplications(0)) {
            UUID uuid = applicationInfo.storageUuid;
            if (uuid != null && (findVolumeByUuid = this.mStorageManager.findVolumeByUuid(uuid.toString())) != null && (disk = findVolumeByUuid.getDisk()) != null) {
                if (disk.isSd()) {
                    i2 = 1;
                } else if (disk.isUsb()) {
                    i2 = 2;
                } else {
                    i2 = applicationInfo.isExternal() ? 3 : -1;
                }
                if (i2 != -1) {
                    list.add(FrameworkStatsLog.buildStatsEvent(i, i2, applicationInfo.packageName));
                }
            }
        }
        return 0;
    }

    private void registerFaceSettings() {
        this.mStatsManager.setPullAtomCallback(10058, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullFaceSettingsLocked(int i, List<StatsEvent> list) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            UserManager userManager = (UserManager) this.mContext.getSystemService(UserManager.class);
            if (userManager == null) {
                return 1;
            }
            List users = userManager.getUsers();
            int size = users.size();
            for (int i2 = 0; i2 < size; i2++) {
                int identifier = ((UserInfo) users.get(i2)).getUserHandle().getIdentifier();
                list.add(FrameworkStatsLog.buildStatsEvent(i, Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_keyguard_enabled", 1, identifier) != 0, Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_dismisses_keyguard", 1, identifier) != 0, Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_attention_required", 0, identifier) != 0, Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_app_enabled", 1, identifier) != 0, Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_always_require_confirmation", 0, identifier) != 0, Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_diversity_required", 1, identifier) != 0));
            }
            return 0;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void registerAppOps() {
        this.mStatsManager.setPullAtomCallback(10060, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerRuntimeAppOpAccessMessage() {
        this.mStatsManager.setPullAtomCallback(10069, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AppOpEntry {
        public final String mAttributionTag;
        public final int mHash;
        public final AppOpsManager.HistoricalOp mOp;
        public final String mPackageName;
        public final int mUid;

        AppOpEntry(String str, String str2, AppOpsManager.HistoricalOp historicalOp, int i) {
            this.mPackageName = str;
            this.mAttributionTag = str2;
            this.mUid = i;
            this.mOp = historicalOp;
            this.mHash = ((str.hashCode() + StatsPullAtomService.RANDOM_SEED) & Integer.MAX_VALUE) % 100;
        }
    }

    int pullAppOpsLocked(int i, List<StatsEvent> list) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            AppOpsManager appOpsManager = (AppOpsManager) this.mContext.getSystemService(AppOpsManager.class);
            CompletableFuture completableFuture = new CompletableFuture();
            appOpsManager.getHistoricalOps(new AppOpsManager.HistoricalOpsRequest.Builder(0L, JobStatus.NO_LATEST_RUNTIME).setFlags(9).build(), AsyncTask.THREAD_POOL_EXECUTOR, new StatsPullAtomService$$ExternalSyntheticLambda6(completableFuture));
            if (sampleAppOps(list, processHistoricalOps((AppOpsManager.HistoricalOps) completableFuture.get(EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS), i, 100), i, 100) != 100) {
                Slog.e(TAG, "Atom 10060 downsampled - too many dimensions");
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return 0;
        } catch (Throwable th) {
            try {
                Slog.e(TAG, "Could not read appops", th);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return 1;
            } catch (Throwable th2) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th2;
            }
        }
    }

    private int sampleAppOps(List<StatsEvent> list, List<AppOpEntry> list2, int i, int i2) {
        int i3;
        int i4;
        StatsEvent buildStatsEvent;
        List<StatsEvent> list3 = list;
        List<AppOpEntry> list4 = list2;
        int i5 = i;
        int i6 = i2;
        int size = list2.size();
        int i7 = 0;
        while (i7 < size) {
            AppOpEntry appOpEntry = list4.get(i7);
            if (appOpEntry.mHash >= i6) {
                i3 = i7;
                i4 = size;
            } else {
                if (i5 == 10075) {
                    i3 = i7;
                    i4 = size;
                    buildStatsEvent = FrameworkStatsLog.buildStatsEvent(i, appOpEntry.mUid, appOpEntry.mPackageName, appOpEntry.mAttributionTag, appOpEntry.mOp.getOpCode(), appOpEntry.mOp.getForegroundAccessCount(9), appOpEntry.mOp.getBackgroundAccessCount(9), appOpEntry.mOp.getForegroundRejectCount(9), appOpEntry.mOp.getBackgroundRejectCount(9), appOpEntry.mOp.getForegroundAccessDuration(9), appOpEntry.mOp.getBackgroundAccessDuration(9), this.mDangerousAppOpsList.contains(Integer.valueOf(appOpEntry.mOp.getOpCode())), i2);
                } else {
                    i3 = i7;
                    i4 = size;
                    buildStatsEvent = FrameworkStatsLog.buildStatsEvent(i, appOpEntry.mUid, appOpEntry.mPackageName, appOpEntry.mOp.getOpCode(), appOpEntry.mOp.getForegroundAccessCount(9), appOpEntry.mOp.getBackgroundAccessCount(9), appOpEntry.mOp.getForegroundRejectCount(9), appOpEntry.mOp.getBackgroundRejectCount(9), appOpEntry.mOp.getForegroundAccessDuration(9), appOpEntry.mOp.getBackgroundAccessDuration(9), this.mDangerousAppOpsList.contains(Integer.valueOf(appOpEntry.mOp.getOpCode())));
                }
                list3 = list;
                list3.add(buildStatsEvent);
            }
            i7 = i3 + 1;
            list4 = list2;
            i5 = i;
            i6 = i2;
            size = i4;
        }
        if (list.size() <= 800) {
            return i2;
        }
        int constrain = MathUtils.constrain((i2 * 500) / list.size(), 0, i2 - 1);
        list.clear();
        return sampleAppOps(list3, list2, i, constrain);
    }

    private void registerAttributedAppOps() {
        this.mStatsManager.setPullAtomCallback(10075, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullAttributedAppOpsLocked(int i, List<StatsEvent> list) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            AppOpsManager appOpsManager = (AppOpsManager) this.mContext.getSystemService(AppOpsManager.class);
            CompletableFuture completableFuture = new CompletableFuture();
            appOpsManager.getHistoricalOps(new AppOpsManager.HistoricalOpsRequest.Builder(0L, JobStatus.NO_LATEST_RUNTIME).setFlags(9).build(), AsyncTask.THREAD_POOL_EXECUTOR, new StatsPullAtomService$$ExternalSyntheticLambda6(completableFuture));
            AppOpsManager.HistoricalOps historicalOps = (AppOpsManager.HistoricalOps) completableFuture.get(EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            if (this.mAppOpsSamplingRate == 0) {
                this.mContext.getMainThreadHandler().postDelayed(new Runnable() { // from class: com.android.server.stats.pull.StatsPullAtomService.4
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            StatsPullAtomService.this.estimateAppOpsSamplingRate();
                        } finally {
                        }
                    }
                }, APP_OPS_SAMPLING_INITIALIZATION_DELAY_MILLIS);
                this.mAppOpsSamplingRate = 100;
            }
            this.mAppOpsSamplingRate = Math.min(this.mAppOpsSamplingRate, sampleAppOps(list, processHistoricalOps(historicalOps, i, this.mAppOpsSamplingRate), i, this.mAppOpsSamplingRate));
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return 0;
        } catch (Throwable th) {
            try {
                Slog.e(TAG, "Could not read appops", th);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return 1;
            } catch (Throwable th2) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void estimateAppOpsSamplingRate() throws Exception {
        int i = DeviceConfig.getInt("permissions", APP_OPS_TARGET_COLLECTION_SIZE, 2000);
        AppOpsManager appOpsManager = (AppOpsManager) this.mContext.getSystemService(AppOpsManager.class);
        CompletableFuture completableFuture = new CompletableFuture();
        long j = 0;
        appOpsManager.getHistoricalOps(new AppOpsManager.HistoricalOpsRequest.Builder(Math.max(Instant.now().minus(1L, (TemporalUnit) ChronoUnit.DAYS).toEpochMilli(), 0L), JobStatus.NO_LATEST_RUNTIME).setFlags(9).build(), AsyncTask.THREAD_POOL_EXECUTOR, new StatsPullAtomService$$ExternalSyntheticLambda6(completableFuture));
        List<AppOpEntry> processHistoricalOps = processHistoricalOps((AppOpsManager.HistoricalOps) completableFuture.get(EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS), 10075, 100);
        int size = processHistoricalOps.size();
        for (int i2 = 0; i2 < size; i2++) {
            AppOpEntry appOpEntry = processHistoricalOps.get(i2);
            int length = appOpEntry.mPackageName.length() + 32;
            j += length + (appOpEntry.mAttributionTag == null ? 1 : r5.length());
        }
        int constrain = (int) MathUtils.constrain((i * 100) / j, 0L, 100L);
        synchronized (this.mAttributedAppOpsLock) {
            this.mAppOpsSamplingRate = Math.min(this.mAppOpsSamplingRate, constrain);
        }
    }

    private List<AppOpEntry> processHistoricalOps(AppOpsManager.HistoricalOps historicalOps, int i, int i2) {
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < historicalOps.getUidCount(); i3++) {
            AppOpsManager.HistoricalUidOps uidOpsAt = historicalOps.getUidOpsAt(i3);
            int uid = uidOpsAt.getUid();
            for (int i4 = 0; i4 < uidOpsAt.getPackageCount(); i4++) {
                AppOpsManager.HistoricalPackageOps packageOpsAt = uidOpsAt.getPackageOpsAt(i4);
                if (i == 10075) {
                    int i5 = 0;
                    while (i5 < packageOpsAt.getAttributedOpsCount()) {
                        int i6 = 0;
                        for (AppOpsManager.AttributedHistoricalOps attributedOpsAt = packageOpsAt.getAttributedOpsAt(i5); i6 < attributedOpsAt.getOpCount(); attributedOpsAt = attributedOpsAt) {
                            processHistoricalOp(attributedOpsAt.getOpAt(i6), arrayList, uid, i2, packageOpsAt.getPackageName(), attributedOpsAt.getTag());
                            i6++;
                            i5 = i5;
                        }
                        i5++;
                    }
                } else if (i == 10060) {
                    for (int i7 = 0; i7 < packageOpsAt.getOpCount(); i7++) {
                        processHistoricalOp(packageOpsAt.getOpAt(i7), arrayList, uid, i2, packageOpsAt.getPackageName(), null);
                    }
                }
            }
        }
        return arrayList;
    }

    private void processHistoricalOp(AppOpsManager.HistoricalOp historicalOp, List<AppOpEntry> list, int i, int i2, String str, String str2) {
        int i3;
        if (str2 == null || !str2.startsWith(str)) {
            i3 = 0;
        } else {
            i3 = str.length();
            if (i3 < str2.length() && str2.charAt(i3) == '.') {
                i3++;
            }
        }
        AppOpEntry appOpEntry = new AppOpEntry(str, str2 == null ? null : str2.substring(i3), historicalOp, i);
        if (appOpEntry.mHash < i2) {
            list.add(appOpEntry);
        }
    }

    int pullRuntimeAppOpAccessMessageLocked(int i, List<StatsEvent> list) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            RuntimeAppOpAccessMessage collectRuntimeAppOpAccessMessage = ((AppOpsManager) this.mContext.getSystemService(AppOpsManager.class)).collectRuntimeAppOpAccessMessage();
            if (collectRuntimeAppOpAccessMessage == null) {
                Slog.i(TAG, "No runtime appop access message collected");
                return 0;
            }
            list.add(FrameworkStatsLog.buildStatsEvent(i, collectRuntimeAppOpAccessMessage.getUid(), collectRuntimeAppOpAccessMessage.getPackageName(), "", collectRuntimeAppOpAccessMessage.getAttributionTag() == null ? "" : collectRuntimeAppOpAccessMessage.getAttributionTag(), collectRuntimeAppOpAccessMessage.getMessage(), collectRuntimeAppOpAccessMessage.getSamplingStrategy(), AppOpsManager.strOpToOp(collectRuntimeAppOpAccessMessage.getOp())));
            return 0;
        } catch (Throwable th) {
            try {
                Slog.e(TAG, "Could not read runtime appop access message", th);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return 1;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    static void unpackStreamedData(int i, List<StatsEvent> list, List<ParcelFileDescriptor> list2) throws IOException {
        ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new ParcelFileDescriptor.AutoCloseInputStream(list2.get(0));
        int[] iArr = new int[1];
        list.add(FrameworkStatsLog.buildStatsEvent(i, Arrays.copyOf(readFully(autoCloseInputStream, iArr), iArr[0])));
    }

    static byte[] readFully(InputStream inputStream, int[] iArr) throws IOException {
        int available = inputStream.available();
        byte[] bArr = new byte[available > 0 ? available + 1 : 16384];
        int i = 0;
        while (true) {
            int read = inputStream.read(bArr, i, bArr.length - i);
            Slog.i(TAG, "Read " + read + " bytes at " + i + " of avail " + bArr.length);
            if (read < 0) {
                Slog.i(TAG, "**** FINISHED READING: pos=" + i + " len=" + bArr.length);
                iArr[0] = i;
                return bArr;
            }
            i += read;
            if (i >= bArr.length) {
                int i2 = i + 16384;
                byte[] bArr2 = new byte[i2];
                Slog.i(TAG, "Copying " + i + " bytes to new array len " + i2);
                System.arraycopy(bArr, 0, bArr2, 0, i);
                bArr = bArr2;
            }
        }
    }

    private void registerNotificationRemoteViews() {
        this.mStatsManager.setPullAtomCallback(10066, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullNotificationRemoteViewsLocked(int i, List<StatsEvent> list) {
        INotificationManager iNotificationManagerService = getINotificationManagerService();
        if (iNotificationManagerService == null) {
            return 1;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            long currentTimeMicro = (SystemClock.currentTimeMicro() * 1000) - TimeUnit.NANOSECONDS.convert(1L, TimeUnit.DAYS);
            ArrayList arrayList = new ArrayList();
            iNotificationManagerService.pullStats(currentTimeMicro, 1, true, arrayList);
            if (arrayList.size() != 1) {
                return 1;
            }
            unpackStreamedData(i, list, arrayList);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return 0;
        } catch (RemoteException e) {
            Slog.e(TAG, "Getting notistats failed: ", e);
            return 1;
        } catch (IOException e2) {
            Slog.e(TAG, "Getting notistats failed: ", e2);
            return 1;
        } catch (SecurityException e3) {
            Slog.e(TAG, "Getting notistats failed: ", e3);
            return 1;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void registerDangerousPermissionStateSampled() {
        this.mStatsManager.setPullAtomCallback(10067, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerBatteryLevel() {
        this.mStatsManager.setPullAtomCallback(10043, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerRemainingBatteryCapacity() {
        this.mStatsManager.setPullAtomCallback(10019, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerFullBatteryCapacity() {
        this.mStatsManager.setPullAtomCallback(OplusNotificationListenerService.REASON_OPLUSOS_FORCESTOP, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerBatteryVoltage() {
        this.mStatsManager.setPullAtomCallback(10030, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerBatteryCycleCount() {
        this.mStatsManager.setPullAtomCallback(10045, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullHealthHalLocked(int i, List<StatsEvent> list) {
        int i2;
        HealthServiceWrapper healthServiceWrapper = this.mHealthService;
        if (healthServiceWrapper == null) {
            return 1;
        }
        try {
            HealthInfo healthInfo = healthServiceWrapper.getHealthInfo();
            if (healthInfo == null) {
                return 1;
            }
            if (i == 10019) {
                i2 = healthInfo.batteryChargeCounterUah;
            } else if (i == 10020) {
                i2 = healthInfo.batteryFullChargeUah;
            } else if (i == 10030) {
                i2 = healthInfo.batteryVoltageMillivolts;
            } else if (i == 10043) {
                i2 = healthInfo.batteryLevel;
            } else {
                if (i != 10045) {
                    return 1;
                }
                i2 = healthInfo.batteryCycleCount;
            }
            list.add(FrameworkStatsLog.buildStatsEvent(i, i2));
            return 0;
        } catch (RemoteException | IllegalStateException unused) {
            return 1;
        }
    }

    private void registerSettingsStats() {
        this.mStatsManager.setPullAtomCallback(10080, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullSettingsStatsLocked(int i, List<StatsEvent> list) {
        UserManager userManager = (UserManager) this.mContext.getSystemService(UserManager.class);
        if (userManager == null) {
            return 1;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Iterator it = userManager.getUsers().iterator();
            while (it.hasNext()) {
                int identifier = ((UserInfo) it.next()).getUserHandle().getIdentifier();
                if (identifier == 0) {
                    list.addAll(SettingsStatsUtil.logGlobalSettings(this.mContext, i, 0));
                }
                list.addAll(SettingsStatsUtil.logSystemSettings(this.mContext, i, identifier));
                list.addAll(SettingsStatsUtil.logSecureSettings(this.mContext, i, identifier));
            }
            return 0;
        } catch (Exception e) {
            Slog.e(TAG, "failed to pullSettingsStats", e);
            return 1;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void registerInstalledIncrementalPackages() {
        this.mStatsManager.setPullAtomCallback(10114, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullInstalledIncrementalPackagesLocked(int i, List<StatsEvent> list) {
        PackageManager packageManager = this.mContext.getPackageManager();
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        if (!packageManager.hasSystemFeature("android.software.incremental_delivery")) {
            return 0;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                for (int i2 : ((UserManagerInternal) LocalServices.getService(UserManagerInternal.class)).getUserIds()) {
                    for (PackageInfo packageInfo : packageManager.getInstalledPackagesAsUser(0, i2)) {
                        if (IncrementalManager.isIncrementalPath(packageInfo.applicationInfo.getBaseCodePath())) {
                            IncrementalStatesInfo incrementalStatesInfo = packageManagerInternal.getIncrementalStatesInfo(packageInfo.packageName, 1000, i2);
                            list.add(FrameworkStatsLog.buildStatsEvent(i, packageInfo.applicationInfo.uid, incrementalStatesInfo.isLoading(), incrementalStatesInfo.getLoadingCompletedTime()));
                        }
                    }
                }
                return 0;
            } catch (Exception e) {
                Slog.e(TAG, "failed to pullInstalledIncrementalPackagesLocked", e);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return 1;
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void registerKeystoreStorageStats() {
        this.mStatsManager.setPullAtomCallback(10103, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreKeyCreationWithGeneralInfo() {
        this.mStatsManager.setPullAtomCallback(10118, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreKeyCreationWithAuthInfo() {
        this.mStatsManager.setPullAtomCallback(10119, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreKeyCreationWithPurposeModesInfo() {
        this.mStatsManager.setPullAtomCallback(10120, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreAtomWithOverflow() {
        this.mStatsManager.setPullAtomCallback(10121, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreKeyOperationWithPurposeAndModesInfo() {
        this.mStatsManager.setPullAtomCallback(10122, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreKeyOperationWithGeneralInfo() {
        this.mStatsManager.setPullAtomCallback(10123, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerRkpErrorStats() {
        this.mStatsManager.setPullAtomCallback(10124, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreCrashStats() {
        this.mStatsManager.setPullAtomCallback(10125, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerAccessibilityShortcutStats() {
        this.mStatsManager.setPullAtomCallback(10127, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerAccessibilityFloatingMenuStats() {
        this.mStatsManager.setPullAtomCallback(10128, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerMediaCapabilitiesStats() {
        this.mStatsManager.setPullAtomCallback(10130, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int parseKeystoreStorageStats(KeystoreAtom[] keystoreAtomArr, List<StatsEvent> list) {
        for (KeystoreAtom keystoreAtom : keystoreAtomArr) {
            if (keystoreAtom.payload.getTag() != 0) {
                return 1;
            }
            StorageStats storageStats = keystoreAtom.payload.getStorageStats();
            list.add(FrameworkStatsLog.buildStatsEvent(10103, storageStats.storage_type, storageStats.size, storageStats.unused_size));
        }
        return 0;
    }

    int parseKeystoreKeyCreationWithGeneralInfo(KeystoreAtom[] keystoreAtomArr, List<StatsEvent> list) {
        for (KeystoreAtom keystoreAtom : keystoreAtomArr) {
            if (keystoreAtom.payload.getTag() != 1) {
                return 1;
            }
            KeyCreationWithGeneralInfo keyCreationWithGeneralInfo = keystoreAtom.payload.getKeyCreationWithGeneralInfo();
            list.add(FrameworkStatsLog.buildStatsEvent(10118, keyCreationWithGeneralInfo.algorithm, keyCreationWithGeneralInfo.key_size, keyCreationWithGeneralInfo.ec_curve, keyCreationWithGeneralInfo.key_origin, keyCreationWithGeneralInfo.error_code, keyCreationWithGeneralInfo.attestation_requested, keystoreAtom.count));
        }
        return 0;
    }

    int parseKeystoreKeyCreationWithAuthInfo(KeystoreAtom[] keystoreAtomArr, List<StatsEvent> list) {
        for (KeystoreAtom keystoreAtom : keystoreAtomArr) {
            if (keystoreAtom.payload.getTag() != 2) {
                return 1;
            }
            KeyCreationWithAuthInfo keyCreationWithAuthInfo = keystoreAtom.payload.getKeyCreationWithAuthInfo();
            list.add(FrameworkStatsLog.buildStatsEvent(10119, keyCreationWithAuthInfo.user_auth_type, keyCreationWithAuthInfo.log10_auth_key_timeout_seconds, keyCreationWithAuthInfo.security_level, keystoreAtom.count));
        }
        return 0;
    }

    int parseKeystoreKeyCreationWithPurposeModesInfo(KeystoreAtom[] keystoreAtomArr, List<StatsEvent> list) {
        for (KeystoreAtom keystoreAtom : keystoreAtomArr) {
            if (keystoreAtom.payload.getTag() != 3) {
                return 1;
            }
            KeyCreationWithPurposeAndModesInfo keyCreationWithPurposeAndModesInfo = keystoreAtom.payload.getKeyCreationWithPurposeAndModesInfo();
            list.add(FrameworkStatsLog.buildStatsEvent(10120, keyCreationWithPurposeAndModesInfo.algorithm, keyCreationWithPurposeAndModesInfo.purpose_bitmap, keyCreationWithPurposeAndModesInfo.padding_mode_bitmap, keyCreationWithPurposeAndModesInfo.digest_bitmap, keyCreationWithPurposeAndModesInfo.block_mode_bitmap, keystoreAtom.count));
        }
        return 0;
    }

    int parseKeystoreAtomWithOverflow(KeystoreAtom[] keystoreAtomArr, List<StatsEvent> list) {
        for (KeystoreAtom keystoreAtom : keystoreAtomArr) {
            if (keystoreAtom.payload.getTag() != 4) {
                return 1;
            }
            list.add(FrameworkStatsLog.buildStatsEvent(10121, keystoreAtom.payload.getKeystore2AtomWithOverflow().atom_id, keystoreAtom.count));
        }
        return 0;
    }

    int parseKeystoreKeyOperationWithPurposeModesInfo(KeystoreAtom[] keystoreAtomArr, List<StatsEvent> list) {
        for (KeystoreAtom keystoreAtom : keystoreAtomArr) {
            if (keystoreAtom.payload.getTag() != 5) {
                return 1;
            }
            KeyOperationWithPurposeAndModesInfo keyOperationWithPurposeAndModesInfo = keystoreAtom.payload.getKeyOperationWithPurposeAndModesInfo();
            list.add(FrameworkStatsLog.buildStatsEvent(10122, keyOperationWithPurposeAndModesInfo.purpose, keyOperationWithPurposeAndModesInfo.padding_mode_bitmap, keyOperationWithPurposeAndModesInfo.digest_bitmap, keyOperationWithPurposeAndModesInfo.block_mode_bitmap, keystoreAtom.count));
        }
        return 0;
    }

    int parseKeystoreKeyOperationWithGeneralInfo(KeystoreAtom[] keystoreAtomArr, List<StatsEvent> list) {
        for (KeystoreAtom keystoreAtom : keystoreAtomArr) {
            if (keystoreAtom.payload.getTag() != 6) {
                return 1;
            }
            KeyOperationWithGeneralInfo keyOperationWithGeneralInfo = keystoreAtom.payload.getKeyOperationWithGeneralInfo();
            list.add(FrameworkStatsLog.buildStatsEvent(10123, keyOperationWithGeneralInfo.outcome, keyOperationWithGeneralInfo.error_code, keyOperationWithGeneralInfo.key_upgraded, keyOperationWithGeneralInfo.security_level, keystoreAtom.count));
        }
        return 0;
    }

    int parseRkpErrorStats(KeystoreAtom[] keystoreAtomArr, List<StatsEvent> list) {
        for (KeystoreAtom keystoreAtom : keystoreAtomArr) {
            if (keystoreAtom.payload.getTag() != 7) {
                return 1;
            }
            RkpErrorStats rkpErrorStats = keystoreAtom.payload.getRkpErrorStats();
            list.add(FrameworkStatsLog.buildStatsEvent(10124, rkpErrorStats.rkpError, keystoreAtom.count, rkpErrorStats.security_level));
        }
        return 0;
    }

    int parseKeystoreCrashStats(KeystoreAtom[] keystoreAtomArr, List<StatsEvent> list) {
        for (KeystoreAtom keystoreAtom : keystoreAtomArr) {
            if (keystoreAtom.payload.getTag() != 8) {
                return 1;
            }
            list.add(FrameworkStatsLog.buildStatsEvent(10125, keystoreAtom.payload.getCrashStats().count_of_crash_events));
        }
        return 0;
    }

    int pullKeystoreAtoms(int i, List<StatsEvent> list) {
        IKeystoreMetrics iKeystoreMetricsService = getIKeystoreMetricsService();
        if (iKeystoreMetricsService == null) {
            Slog.w(TAG, "Keystore service is null");
            return 1;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            KeystoreAtom[] pullMetrics = iKeystoreMetricsService.pullMetrics(i);
            if (i == 10103) {
                return parseKeystoreStorageStats(pullMetrics, list);
            }
            switch (i) {
                case 10118:
                    return parseKeystoreKeyCreationWithGeneralInfo(pullMetrics, list);
                case 10119:
                    return parseKeystoreKeyCreationWithAuthInfo(pullMetrics, list);
                case 10120:
                    return parseKeystoreKeyCreationWithPurposeModesInfo(pullMetrics, list);
                case 10121:
                    return parseKeystoreAtomWithOverflow(pullMetrics, list);
                case 10122:
                    return parseKeystoreKeyOperationWithPurposeModesInfo(pullMetrics, list);
                case 10123:
                    return parseKeystoreKeyOperationWithGeneralInfo(pullMetrics, list);
                case 10124:
                    return parseRkpErrorStats(pullMetrics, list);
                case 10125:
                    return parseKeystoreCrashStats(pullMetrics, list);
                default:
                    Slog.w(TAG, "Unsupported keystore atom: " + i);
                    return 1;
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Disconnected from keystore service. Cannot pull.", e);
            return 1;
        } catch (ServiceSpecificException e2) {
            Slog.e(TAG, "pulling keystore metrics failed", e2);
            return 1;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    int pullAccessibilityShortcutStatsLocked(int i, List<StatsEvent> list) {
        UserManager userManager = (UserManager) this.mContext.getSystemService(UserManager.class);
        if (userManager == null) {
            return 1;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            Iterator it = userManager.getUsers().iterator();
            while (it.hasNext()) {
                int identifier = ((UserInfo) it.next()).getUserHandle().getIdentifier();
                if (isAccessibilityShortcutUser(this.mContext, identifier)) {
                    list.add(FrameworkStatsLog.buildStatsEvent(i, convertToAccessibilityShortcutType(Settings.Secure.getIntForUser(contentResolver, "accessibility_button_mode", 0, identifier)), countAccessibilityServices(Settings.Secure.getStringForUser(contentResolver, "accessibility_button_targets", identifier)), 2, countAccessibilityServices(Settings.Secure.getStringForUser(contentResolver, "accessibility_shortcut_target_service", identifier)), 3, Settings.Secure.getIntForUser(contentResolver, "accessibility_display_magnification_enabled", 0, identifier)));
                }
            }
            return 0;
        } catch (RuntimeException e) {
            Slog.e(TAG, "pulling accessibility shortcuts stats failed at getUsers", e);
            return 1;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    int pullAccessibilityFloatingMenuStatsLocked(int i, List<StatsEvent> list) {
        UserManager userManager = (UserManager) this.mContext.getSystemService(UserManager.class);
        if (userManager == null) {
            return 1;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            Iterator it = userManager.getUsers().iterator();
            while (true) {
                if (!it.hasNext()) {
                    return 0;
                }
                int identifier = ((UserInfo) it.next()).getUserHandle().getIdentifier();
                if (isAccessibilityFloatingMenuUser(this.mContext, identifier)) {
                    list.add(FrameworkStatsLog.buildStatsEvent(i, Settings.Secure.getIntForUser(contentResolver, "accessibility_floating_menu_size", 0, identifier), Settings.Secure.getIntForUser(contentResolver, "accessibility_floating_menu_icon_type", 0, identifier), Settings.Secure.getIntForUser(contentResolver, "accessibility_floating_menu_fade_enabled", 1, identifier) == 1, Settings.Secure.getFloatForUser(contentResolver, "accessibility_floating_menu_opacity", 0.55f, identifier)));
                }
            }
        } catch (RuntimeException e) {
            Slog.e(TAG, "pulling accessibility floating menu stats failed at getUsers", e);
            return 1;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    int pullMediaCapabilitiesStats(int i, List<StatsEvent> list) {
        AudioManager audioManager;
        int i2;
        boolean z;
        if (!this.mContext.getPackageManager().hasSystemFeature("android.software.leanback") || (audioManager = (AudioManager) this.mContext.getSystemService(AudioManager.class)) == null) {
            return 1;
        }
        Map surroundFormats = audioManager.getSurroundFormats();
        byte[] bytes = toBytes(new ArrayList(surroundFormats.keySet()));
        byte[] bytes2 = toBytes((List<Integer>) audioManager.getReportedSurroundFormats());
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Iterator it = surroundFormats.keySet().iterator();
        while (it.hasNext()) {
            int intValue = ((Integer) it.next()).intValue();
            if (!audioManager.isSurroundFormatEnabled(intValue)) {
                arrayList.add(Integer.valueOf(intValue));
            } else {
                arrayList2.add(Integer.valueOf(intValue));
            }
        }
        byte[] bytes3 = toBytes(arrayList);
        byte[] bytes4 = toBytes(arrayList2);
        int encodedSurroundMode = audioManager.getEncodedSurroundMode();
        DisplayManager displayManager = (DisplayManager) this.mContext.getSystemService(DisplayManager.class);
        Display display = displayManager.getDisplay(0);
        Display.HdrCapabilities hdrCapabilities = display.getHdrCapabilities();
        byte[] bytes5 = hdrCapabilities != null ? toBytes(hdrCapabilities.getSupportedHdrTypes()) : new byte[0];
        byte[] bytes6 = toBytes(display.getSupportedModes());
        List<UUID> supportedCryptoSchemes = MediaDrm.getSupportedCryptoSchemes();
        try {
            i2 = !supportedCryptoSchemes.isEmpty() ? new MediaDrm(supportedCryptoSchemes.get(0)).getConnectedHdcpLevel() : -1;
        } catch (UnsupportedSchemeException e) {
            Slog.e(TAG, "pulling hdcp level failed.", e);
            i2 = -1;
        }
        int matchContentFrameRateUserPreference = displayManager.getMatchContentFrameRateUserPreference();
        byte[] bytes7 = toBytes(displayManager.getUserDisabledHdrTypes());
        Display.Mode globalUserPreferredDisplayMode = displayManager.getGlobalUserPreferredDisplayMode();
        int physicalWidth = globalUserPreferredDisplayMode != null ? globalUserPreferredDisplayMode.getPhysicalWidth() : -1;
        int physicalHeight = globalUserPreferredDisplayMode != null ? globalUserPreferredDisplayMode.getPhysicalHeight() : -1;
        float refreshRate = globalUserPreferredDisplayMode != null ? globalUserPreferredDisplayMode.getRefreshRate() : 0.0f;
        try {
            z = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "minimal_post_processing_allowed", 1) == 0;
        } catch (Settings.SettingNotFoundException e2) {
            Slog.e(TAG, "unable to find setting for MINIMAL_POST_PROCESSING_ALLOWED.", e2);
            z = false;
        }
        list.add(FrameworkStatsLog.buildStatsEvent(i, bytes, bytes2, bytes3, bytes4, encodedSurroundMode, bytes5, bytes6, i2, matchContentFrameRateUserPreference, bytes7, physicalWidth, physicalHeight, refreshRate, z));
        return 0;
    }

    private void registerPendingIntentsPerPackagePuller() {
        this.mStatsManager.setPullAtomCallback(10151, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullHdrCapabilities(int i, List<StatsEvent> list) {
        DisplayManager displayManager = (DisplayManager) this.mContext.getSystemService(DisplayManager.class);
        Display display = displayManager.getDisplay(0);
        int conversionMode = displayManager.getHdrConversionMode().getConversionMode();
        int preferredHdrOutputType = displayManager.getHdrConversionMode().getPreferredHdrOutputType();
        list.add(FrameworkStatsLog.buildStatsEvent(i, toBytes(displayManager.getSupportedHdrOutputTypes()), conversionMode == 1, preferredHdrOutputType == -1 ? 0 : preferredHdrOutputType, hasDolbyVisionIssue(display), conversionMode != 0));
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullCachedAppsHighWatermark(int i, List<StatsEvent> list) {
        list.add(((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).getCachedAppsHighWatermarkStats(i, true));
        return 0;
    }

    private boolean hasDolbyVisionIssue(Display display) {
        final AtomicInteger atomicInteger = new AtomicInteger();
        Arrays.stream(display.getSupportedModes()).map(new Function() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda16
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((Display.Mode) obj).getSupportedHdrTypes();
            }
        }).filter(new Predicate() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda17
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$hasDolbyVisionIssue$24;
                lambda$hasDolbyVisionIssue$24 = StatsPullAtomService.lambda$hasDolbyVisionIssue$24((int[]) obj);
                return lambda$hasDolbyVisionIssue$24;
            }
        }).forEach(new Consumer() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                atomicInteger.incrementAndGet();
            }
        });
        return atomicInteger.get() != 0 && atomicInteger.get() < display.getSupportedModes().length;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$hasDolbyVisionIssue$24(int[] iArr) {
        return Arrays.stream(iArr).anyMatch(new IntPredicate() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda8
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                boolean lambda$hasDolbyVisionIssue$23;
                lambda$hasDolbyVisionIssue$23 = StatsPullAtomService.lambda$hasDolbyVisionIssue$23(i);
                return lambda$hasDolbyVisionIssue$23;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullPendingIntentsPerPackage(int i, List<StatsEvent> list) {
        for (PendingIntentStats pendingIntentStats : ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).getPendingIntentStats()) {
            list.add(FrameworkStatsLog.buildStatsEvent(i, pendingIntentStats.uid, pendingIntentStats.count, pendingIntentStats.sizeKb));
        }
        return 0;
    }

    private void registerPinnerServiceStats() {
        this.mStatsManager.setPullAtomCallback(10150, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerHdrCapabilitiesPuller() {
        this.mStatsManager.setPullAtomCallback(10175, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerCachedAppsHighWatermarkPuller() {
        this.mStatsManager.setPullAtomCallback(10189, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullSystemServerPinnerStats(int i, List<StatsEvent> list) {
        for (PinnerService.PinnedFileStats pinnedFileStats : ((PinnerService) LocalServices.getService(PinnerService.class)).dumpDataForStatsd()) {
            list.add(FrameworkStatsLog.buildStatsEvent(i, pinnedFileStats.uid, pinnedFileStats.filename, pinnedFileStats.sizeKb));
        }
        return 0;
    }

    private byte[] toBytes(List<Integer> list) {
        ProtoOutputStream protoOutputStream = new ProtoOutputStream();
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            protoOutputStream.write(2259152797697L, it.next().intValue());
        }
        return protoOutputStream.getBytes();
    }

    private byte[] toBytes(int[] iArr) {
        ProtoOutputStream protoOutputStream = new ProtoOutputStream();
        for (int i : iArr) {
            protoOutputStream.write(2259152797697L, i);
        }
        return protoOutputStream.getBytes();
    }

    private byte[] toBytes(Display.Mode[] modeArr) {
        Map<Integer, Integer> createModeGroups = createModeGroups(modeArr);
        ProtoOutputStream protoOutputStream = new ProtoOutputStream();
        for (Display.Mode mode : modeArr) {
            ProtoOutputStream protoOutputStream2 = new ProtoOutputStream();
            protoOutputStream2.write(1120986464257L, mode.getPhysicalHeight());
            protoOutputStream2.write(1120986464258L, mode.getPhysicalWidth());
            protoOutputStream2.write(1108101562371L, mode.getRefreshRate());
            protoOutputStream2.write(1120986464260L, createModeGroups.get(Integer.valueOf(mode.getModeId())).intValue());
            protoOutputStream.write(2246267895809L, protoOutputStream2.getBytes());
        }
        return protoOutputStream.getBytes();
    }

    private Map<Integer, Integer> createModeGroups(Display.Mode[] modeArr) {
        ArrayMap arrayMap = new ArrayMap();
        int i = 1;
        for (Display.Mode mode : modeArr) {
            if (!arrayMap.containsKey(Integer.valueOf(mode.getModeId()))) {
                arrayMap.put(Integer.valueOf(mode.getModeId()), Integer.valueOf(i));
                for (float f : mode.getAlternativeRefreshRates()) {
                    int findModeId = findModeId(modeArr, mode.getPhysicalWidth(), mode.getPhysicalHeight(), f);
                    if (findModeId != -1 && !arrayMap.containsKey(Integer.valueOf(findModeId))) {
                        arrayMap.put(Integer.valueOf(findModeId), Integer.valueOf(i));
                    }
                }
                i++;
            }
        }
        return arrayMap;
    }

    private int findModeId(Display.Mode[] modeArr, int i, int i2, float f) {
        for (Display.Mode mode : modeArr) {
            if (mode.matches(i, i2, f)) {
                return mode.getModeId();
            }
        }
        return -1;
    }

    private int countAccessibilityServices(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int count = (int) str.chars().filter(new IntPredicate() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda10
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                boolean lambda$countAccessibilityServices$26;
                lambda$countAccessibilityServices$26 = StatsPullAtomService.lambda$countAccessibilityServices$26(i);
                return lambda$countAccessibilityServices$26;
            }
        }).count();
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        return count + 1;
    }

    private boolean isAccessibilityShortcutUser(Context context, int i) {
        ContentResolver contentResolver = context.getContentResolver();
        String stringForUser = Settings.Secure.getStringForUser(contentResolver, "accessibility_button_targets", i);
        String stringForUser2 = Settings.Secure.getStringForUser(contentResolver, "accessibility_shortcut_target_service", i);
        return (TextUtils.isEmpty(stringForUser) ^ true) || ((Settings.Secure.getIntForUser(contentResolver, "accessibility_shortcut_dialog_shown", 0, i) == 1) && !TextUtils.isEmpty(stringForUser2)) || (Settings.Secure.getIntForUser(contentResolver, "accessibility_display_magnification_enabled", 0, i) == 1);
    }

    private boolean isAccessibilityFloatingMenuUser(Context context, int i) {
        ContentResolver contentResolver = context.getContentResolver();
        return Settings.Secure.getIntForUser(contentResolver, "accessibility_button_mode", 0, i) == 1 && !TextUtils.isEmpty(Settings.Secure.getStringForUser(contentResolver, "accessibility_button_targets", i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ThermalEventListener extends IThermalEventListener.Stub {
        private ThermalEventListener() {
        }

        public void notifyThrottling(Temperature temperature) {
            FrameworkStatsLog.write(189, temperature.getType(), temperature.getName(), (int) (temperature.getValue() * 10.0f), temperature.getStatus());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ConnectivityStatsCallback extends ConnectivityManager.NetworkCallback {
        private ConnectivityStatsCallback() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            FrameworkStatsLog.write(98, network.getNetId(), 1);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            FrameworkStatsLog.write(98, network.getNetId(), 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class StatsSubscriptionsListener extends SubscriptionManager.OnSubscriptionsChangedListener {
        private final SubscriptionManager mSm;

        StatsSubscriptionsListener(SubscriptionManager subscriptionManager) {
            this.mSm = subscriptionManager;
        }

        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            for (final SubscriptionInfo subscriptionInfo : this.mSm.getCompleteActiveSubscriptionInfoList()) {
                if (((SubInfo) CollectionUtils.find(StatsPullAtomService.this.mHistoricalSubs, new Predicate() { // from class: com.android.server.stats.pull.StatsPullAtomService$StatsSubscriptionsListener$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$onSubscriptionsChanged$0;
                        lambda$onSubscriptionsChanged$0 = StatsPullAtomService.StatsSubscriptionsListener.lambda$onSubscriptionsChanged$0(subscriptionInfo, (SubInfo) obj);
                        return lambda$onSubscriptionsChanged$0;
                    }
                })) == null) {
                    int subscriptionId = subscriptionInfo.getSubscriptionId();
                    String mccString = subscriptionInfo.getMccString();
                    String mncString = subscriptionInfo.getMncString();
                    String subscriberId = StatsPullAtomService.this.mTelephony.getSubscriberId(subscriptionId);
                    if (TextUtils.isEmpty(subscriberId) || TextUtils.isEmpty(mccString) || TextUtils.isEmpty(mncString) || subscriptionInfo.getCarrierId() == -1) {
                        Slog.e(StatsPullAtomService.TAG, "subInfo of subId " + subscriptionId + " is invalid, ignored.");
                    } else {
                        SubInfo subInfo = new SubInfo(subscriptionId, subscriptionInfo.getCarrierId(), mccString, mncString, subscriberId, subscriptionInfo.isOpportunistic());
                        Slog.i(StatsPullAtomService.TAG, "subId " + subscriptionId + " added into historical sub list");
                        synchronized (StatsPullAtomService.this.mDataBytesTransferLock) {
                            StatsPullAtomService.this.mHistoricalSubs.add(subInfo);
                            StatsPullAtomService.this.mNetworkStatsBaselines.addAll(StatsPullAtomService.this.getDataUsageBytesTransferSnapshotForSub(subInfo));
                        }
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$onSubscriptionsChanged$0(SubscriptionInfo subscriptionInfo, SubInfo subInfo) {
            return subInfo.subId == subscriptionInfo.getSubscriptionId();
        }
    }
}
