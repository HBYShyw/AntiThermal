package com.android.server;

import android.R;
import android.app.ActivityThread;
import android.app.AppCompatCallbacks;
import android.app.ApplicationErrorReport;
import android.app.ContextImpl;
import android.app.INotificationManager;
import android.app.SystemServiceRegistry;
import android.app.admin.DevicePolicySafetyChecker;
import android.app.usage.UsageStatsManagerInternal;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteCompatibilityWalFlags;
import android.database.sqlite.SQLiteGlobal;
import android.graphics.GraphicsStatsService;
import android.graphics.Typeface;
import android.hardware.display.DisplayManagerInternal;
import android.location.ICountryDetector;
import android.net.ConnectivityManager;
import android.net.ConnectivityModuleConnector;
import android.net.NetworkStackClient;
import android.os.ArtModuleServiceManager;
import android.os.BaseBundle;
import android.os.Binder;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.FactoryTest;
import android.os.FileUtils;
import android.os.IBinder;
import android.os.IIncidentManager;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Process;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.storage.IStorageManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Dumpable;
import android.util.EventLog;
import android.util.IndentingPrintWriter;
import android.util.Pair;
import android.util.Slog;
import android.util.TimeUtils;
import com.android.i18n.timezone.ZoneInfoDb;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.internal.os.BinderInternal;
import com.android.internal.os.RuntimeInit;
import com.android.internal.policy.AttributeCache;
import com.android.internal.util.ConcurrentUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.widget.ILockSettings;
import com.android.internal.widget.LockSettingsInternal;
import com.android.server.BinderCallsStatsService;
import com.android.server.LooperStatsService;
import com.android.server.NetworkScoreService;
import com.android.server.TelephonyRegistry;
import com.android.server.am.ActivityManagerService;
import com.android.server.am.IOplusSceneManager;
import com.android.server.ambientcontext.AmbientContextManagerService;
import com.android.server.app.GameManagerService;
import com.android.server.appbinding.AppBindingService;
import com.android.server.art.ArtModuleServiceInitializer;
import com.android.server.art.DexUseManagerLocal;
import com.android.server.attention.AttentionManagerService;
import com.android.server.audio.AudioService;
import com.android.server.biometrics.AuthService;
import com.android.server.biometrics.BiometricService;
import com.android.server.biometrics.sensors.iris.IrisService;
import com.android.server.broadcastradio.BroadcastRadioService;
import com.android.server.camera.CameraServiceProxy;
import com.android.server.clipboard.ClipboardService;
import com.android.server.compat.PlatformCompat;
import com.android.server.compat.PlatformCompatNative;
import com.android.server.connectivity.PacProxyService;
import com.android.server.contentcapture.ContentCaptureManagerInternal;
import com.android.server.coverage.CoverageService;
import com.android.server.cpu.CpuMonitorService;
import com.android.server.devicepolicy.DevicePolicyManagerService;
import com.android.server.devicestate.DeviceStateManagerService;
import com.android.server.display.DisplayManagerService;
import com.android.server.display.color.ColorDisplayService;
import com.android.server.dreams.DreamManagerService;
import com.android.server.emergency.EmergencyAffordanceService;
import com.android.server.gpu.GpuService;
import com.android.server.grammaticalinflection.GrammaticalInflectionService;
import com.android.server.graphics.fonts.FontManagerService;
import com.android.server.hdmi.HdmiControlService;
import com.android.server.incident.IncidentCompanionService;
import com.android.server.input.InputManagerService;
import com.android.server.inputmethod.InputMethodManagerService;
import com.android.server.integrity.AppIntegrityManagerService;
import com.android.server.lights.LightsService;
import com.android.server.locales.LocaleManagerService;
import com.android.server.location.LocationManagerService;
import com.android.server.location.altitude.AltitudeService;
import com.android.server.logcat.LogcatManagerService;
import com.android.server.media.MediaRouterService;
import com.android.server.media.metrics.MediaMetricsManagerService;
import com.android.server.media.projection.MediaProjectionManagerService;
import com.android.server.net.NetworkManagementService;
import com.android.server.net.NetworkPolicyManagerService;
import com.android.server.net.watchlist.NetworkWatchlistService;
import com.android.server.notification.NotificationManagerService;
import com.android.server.oemlock.OemLockService;
import com.android.server.om.OverlayManagerService;
import com.android.server.os.BugreportManagerService;
import com.android.server.os.DeviceIdentifiersPolicyService;
import com.android.server.os.NativeTombstoneManagerService;
import com.android.server.os.SchedulingPolicyService;
import com.android.server.people.PeopleService;
import com.android.server.permission.access.AccessCheckingService;
import com.android.server.pm.ApexManager;
import com.android.server.pm.ApexSystemServiceInfo;
import com.android.server.pm.BackgroundInstallControlService;
import com.android.server.pm.CrossProfileAppsService;
import com.android.server.pm.DataLoaderManagerService;
import com.android.server.pm.DexOptHelper;
import com.android.server.pm.DynamicCodeLoggingService;
import com.android.server.pm.IPackageManagerServiceUtilsExt;
import com.android.server.pm.Installer;
import com.android.server.pm.LauncherAppsService;
import com.android.server.pm.PackageManagerService;
import com.android.server.pm.ShortcutService;
import com.android.server.pm.UserManagerService;
import com.android.server.pm.dex.OdsignStatsLogger;
import com.android.server.pm.verify.domain.DomainVerificationService;
import com.android.server.policy.AppOpsPolicy;
import com.android.server.policy.PermissionPolicyService;
import com.android.server.policy.role.RoleServicePlatformHelperImpl;
import com.android.server.power.PowerManagerService;
import com.android.server.power.ShutdownThread;
import com.android.server.power.ThermalManagerService;
import com.android.server.power.hint.HintManagerService;
import com.android.server.powerstats.PowerStatsService;
import com.android.server.profcollect.ProfcollectForwardingService;
import com.android.server.recoverysystem.RecoverySystemService;
import com.android.server.resources.ResourcesManagerService;
import com.android.server.restrictions.RestrictionsManagerService;
import com.android.server.role.RoleServicePlatformHelper;
import com.android.server.rotationresolver.RotationResolverManagerService;
import com.android.server.security.AttestationVerificationManagerService;
import com.android.server.security.FileIntegrityService;
import com.android.server.security.KeyAttestationApplicationIdProviderService;
import com.android.server.security.KeyChainSystemService;
import com.android.server.security.rkp.RemoteProvisioningService;
import com.android.server.sensorprivacy.SensorPrivacyService;
import com.android.server.sensors.SensorService;
import com.android.server.signedconfig.SignedConfigService;
import com.android.server.soundtrigger.SoundTriggerService;
import com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareService;
import com.android.server.statusbar.StatusBarManagerService;
import com.android.server.storage.DeviceStorageMonitorService;
import com.android.server.telecom.TelecomLoaderService;
import com.android.server.testharness.TestHarnessModeService;
import com.android.server.textclassifier.TextClassificationManagerService;
import com.android.server.textservices.TextServicesManagerService;
import com.android.server.timedetector.NetworkTimeUpdateService;
import com.android.server.tracing.TracingServiceProxy;
import com.android.server.trust.TrustManagerService;
import com.android.server.tv.TvInputManagerService;
import com.android.server.tv.TvRemoteService;
import com.android.server.tv.interactive.TvInteractiveAppManagerService;
import com.android.server.tv.tunerresourcemanager.TunerResourceManagerService;
import com.android.server.twilight.TwilightService;
import com.android.server.uri.UriGrantsManagerService;
import com.android.server.utils.TimingsTraceAndSlog;
import com.android.server.vibrator.VibratorManagerService;
import com.android.server.vr.VrManagerService;
import com.android.server.wearable.WearableSensingManagerService;
import com.android.server.webkit.WebViewUpdateService;
import com.android.server.wm.ActivityTaskManagerService;
import com.android.server.wm.WindowManagerGlobalLock;
import com.android.server.wm.WindowManagerService;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Timer;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import system.ext.loader.core.ExtLoader;
import system.ext.preload.IServicesPreloadExt;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SystemServer implements Dumpable {
    private static final String ACCESSIBILITY_MANAGER_SERVICE_CLASS = "com.android.server.accessibility.AccessibilityManagerService$Lifecycle";
    private static final String ACCOUNT_SERVICE_CLASS = "com.android.server.accounts.AccountManagerService$Lifecycle";
    private static final String ADB_SERVICE_CLASS = "com.android.server.adb.AdbService$Lifecycle";
    private static final String AD_SERVICES_MANAGER_SERVICE_CLASS = "com.android.server.adservices.AdServicesManagerService$Lifecycle";
    private static final String ALARM_MANAGER_SERVICE_CLASS = "com.android.server.alarm.AlarmManagerService";
    private static final String APPSEARCH_MODULE_LIFECYCLE_CLASS = "com.android.server.appsearch.AppSearchModule$Lifecycle";
    private static final String APPWIDGET_SERVICE_CLASS = "com.android.server.appwidget.AppWidgetService";
    private static final String APP_COMPAT_OVERRIDES_SERVICE_CLASS = "com.android.server.compat.overrides.AppCompatOverridesService$Lifecycle";
    private static final String APP_HIBERNATION_SERVICE_CLASS = "com.android.server.apphibernation.AppHibernationService";
    private static final String APP_PREDICTION_MANAGER_SERVICE_CLASS = "com.android.server.appprediction.AppPredictionManagerService";
    private static final String AUTO_FILL_MANAGER_SERVICE_CLASS = "com.android.server.autofill.AutofillManagerService";
    private static final String BACKUP_MANAGER_SERVICE_CLASS = "com.android.server.backup.BackupManagerService$Lifecycle";
    private static final String BLOB_STORE_MANAGER_SERVICE_CLASS = "com.android.server.blob.BlobStoreManagerService";
    private static final String BLOCK_MAP_FILE = "/cache/recovery/block.map";
    private static final String BLUETOOTH_APEX_SERVICE_JAR_PATH = "/apex/com.android.btservices/javalib/service-bluetooth.jar";
    private static final String BLUETOOTH_SERVICE_CLASS = "com.android.server.bluetooth.BluetoothService";
    private static final String CAR_SERVICE_HELPER_SERVICE_CLASS = "com.android.internal.car.CarServiceHelperService";
    private static final String COMPANION_DEVICE_MANAGER_SERVICE_CLASS = "com.android.server.companion.CompanionDeviceManagerService";
    private static final String CONNECTIVITY_SERVICE_APEX_PATH = "/apex/com.android.tethering/javalib/service-connectivity.jar";
    private static final String CONNECTIVITY_SERVICE_INITIALIZER_CLASS = "com.android.server.ConnectivityServiceInitializer";
    private static final String CONTENT_CAPTURE_MANAGER_SERVICE_CLASS = "com.android.server.contentcapture.ContentCaptureManagerService";
    private static final String CONTENT_SERVICE_CLASS = "com.android.server.content.ContentService$Lifecycle";
    private static final String CONTENT_SUGGESTIONS_SERVICE_CLASS = "com.android.server.contentsuggestions.ContentSuggestionsManagerService";
    private static final String CREDENTIAL_MANAGER_SERVICE_CLASS = "com.android.server.credentials.CredentialManagerService";
    private static int DEFAULT_SYSTEM_THEME = 16974843;
    private static final String DEVICE_IDLE_CONTROLLER_CLASS = "com.android.server.DeviceIdleController";
    private static final String DEVICE_LOCK_APEX_PATH = "/apex/com.android.devicelock/javalib/service-devicelock.jar";
    private static final String DEVICE_LOCK_SERVICE_CLASS = "com.android.server.devicelock.DeviceLockService";
    private static final String GAME_MANAGER_SERVICE_CLASS = "com.android.server.app.GameManagerService$Lifecycle";
    private static final String GNSS_TIME_UPDATE_SERVICE_CLASS = "com.android.server.timedetector.GnssTimeUpdateService$Lifecycle";
    private static final String HEALTHCONNECT_MANAGER_SERVICE_CLASS = "com.android.server.healthconnect.HealthConnectManagerService";
    private static final String HEALTH_SERVICE_CLASS = "com.android.clockwork.healthservices.HealthService";
    private static final File HEAP_DUMP_PATH = new File("/data/system/heapdump/");
    private static final String IOT_SERVICE_CLASS = "com.android.things.server.IoTSystemService";
    private static final String IP_CONNECTIVITY_METRICS_CLASS = "com.android.server.connectivity.IpConnectivityMetrics";
    private static final String ISOLATED_COMPILATION_SERVICE_CLASS = "com.android.server.compos.IsolatedCompilationService";
    private static final String JOB_SCHEDULER_SERVICE_CLASS = "com.android.server.job.JobSchedulerService";
    private static final String LOCATION_TIME_ZONE_MANAGER_SERVICE_CLASS = "com.android.server.timezonedetector.location.LocationTimeZoneManagerService$Lifecycle";
    private static final String LOCK_SETTINGS_SERVICE_CLASS = "com.android.server.locksettings.LockSettingsService$Lifecycle";
    private static final String LOWPAN_SERVICE_CLASS = "com.android.server.lowpan.LowpanService";
    private static final int MAX_HEAP_DUMPS = 2;
    private static final String MEDIA_COMMUNICATION_SERVICE_CLASS = "com.android.server.media.MediaCommunicationService";
    private static final String MEDIA_RESOURCE_MONITOR_SERVICE_CLASS = "com.android.server.media.MediaResourceMonitorService";
    private static final String MEDIA_SESSION_SERVICE_CLASS = "com.android.server.media.MediaSessionService";
    private static final String MIDI_SERVICE_CLASS = "com.android.server.midi.MidiService$Lifecycle";
    private static final String MUSIC_RECOGNITION_MANAGER_SERVICE_CLASS = "com.android.server.musicrecognition.MusicRecognitionManagerService";
    private static final String NETWORK_STATS_SERVICE_INITIALIZER_CLASS = "com.android.server.NetworkStatsServiceInitializer";
    private static final String ON_DEVICE_PERSONALIZATION_SYSTEM_SERVICE_CLASS = "com.android.server.ondevicepersonalization.OnDevicePersonalizationSystemService$Lifecycle";
    private static final String PERSISTENT_DATA_BLOCK_PROP = "ro.frp.pst";
    private static final String PRINT_MANAGER_SERVICE_CLASS = "com.android.server.print.PrintManagerService";
    private static final String REBOOT_READINESS_LIFECYCLE_CLASS = "com.android.server.scheduling.RebootReadinessManagerService$Lifecycle";
    private static final String RESOURCE_ECONOMY_SERVICE_CLASS = "com.android.server.tare.InternalResourceService";
    private static final String ROLE_SERVICE_CLASS = "com.android.role.RoleService";
    private static final String ROLLBACK_MANAGER_SERVICE_CLASS = "com.android.server.rollback.RollbackManagerService";
    private static final String SAFETY_CENTER_SERVICE_CLASS = "com.android.safetycenter.SafetyCenterService";
    private static final String SCHEDULING_APEX_PATH = "/apex/com.android.scheduling/javalib/service-scheduling.jar";
    private static final String SDK_SANDBOX_MANAGER_SERVICE_CLASS = "com.android.server.sdksandbox.SdkSandboxManagerService$Lifecycle";
    private static final String SEARCH_MANAGER_SERVICE_CLASS = "com.android.server.search.SearchManagerService$Lifecycle";
    private static final String SEARCH_UI_MANAGER_SERVICE_CLASS = "com.android.server.searchui.SearchUiManagerService";
    private static final String SELECTION_TOOLBAR_MANAGER_SERVICE_CLASS = "com.android.server.selectiontoolbar.SelectionToolbarManagerService";
    private static final String SLICE_MANAGER_SERVICE_CLASS = "com.android.server.slice.SliceManagerService$Lifecycle";
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 200;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 100;
    private static final String SMARTSPACE_MANAGER_SERVICE_CLASS = "com.android.server.smartspace.SmartspaceManagerService";
    private static final String SPEECH_RECOGNITION_MANAGER_SERVICE_CLASS = "com.android.server.speech.SpeechRecognitionManagerService";
    private static final String START_BLOB_STORE_SERVICE = "startBlobStoreManagerService";
    private static final String START_HIDL_SERVICES = "StartHidlServices";
    private static final String START_SENSOR_MANAGER_SERVICE = "StartISensorManagerService";
    private static final String STATS_BOOTSTRAP_ATOM_SERVICE_LIFECYCLE_CLASS = "com.android.server.stats.bootstrap.StatsBootstrapAtomService$Lifecycle";
    private static final String STATS_COMPANION_APEX_PATH = "/apex/com.android.os.statsd/javalib/service-statsd.jar";
    private static final String STATS_COMPANION_LIFECYCLE_CLASS = "com.android.server.stats.StatsCompanion$Lifecycle";
    private static final String STATS_PULL_ATOM_SERVICE_CLASS = "com.android.server.stats.pull.StatsPullAtomService";
    private static final String STORAGE_MANAGER_SERVICE_CLASS = "com.android.server.StorageManagerService$Lifecycle";
    private static final String STORAGE_STATS_SERVICE_CLASS = "com.android.server.usage.StorageStatsService$Lifecycle";
    private static final String SYSPROP_FDTRACK_ABORT_THRESHOLD = "persist.sys.debug.fdtrack_abort_threshold";
    private static final String SYSPROP_FDTRACK_ENABLE_THRESHOLD = "persist.sys.debug.fdtrack_enable_threshold";
    private static final String SYSPROP_FDTRACK_INTERVAL = "persist.sys.debug.fdtrack_interval";
    private static final String SYSPROP_START_COUNT = "sys.system_server.start_count";
    private static final String SYSPROP_START_ELAPSED = "sys.system_server.start_elapsed";
    private static final String SYSPROP_START_UPTIME = "sys.system_server.start_uptime";
    private static final String SYSTEM_CAPTIONS_MANAGER_SERVICE_CLASS = "com.android.server.systemcaptions.SystemCaptionsManagerService";
    private static final String TAG = "SystemServer";
    private static final String TETHERING_CONNECTOR_CLASS = "android.net.ITetheringConnector";
    private static final String TEXT_TO_SPEECH_MANAGER_SERVICE_CLASS = "com.android.server.texttospeech.TextToSpeechManagerService";
    private static final String THERMAL_OBSERVER_CLASS = "com.android.clockwork.ThermalObserver";
    private static final String TIME_DETECTOR_SERVICE_CLASS = "com.android.server.timedetector.TimeDetectorService$Lifecycle";
    private static final String TIME_ZONE_DETECTOR_SERVICE_CLASS = "com.android.server.timezonedetector.TimeZoneDetectorService$Lifecycle";
    private static final String TRANSLATION_MANAGER_SERVICE_CLASS = "com.android.server.translation.TranslationManagerService";
    private static final String UNCRYPT_PACKAGE_FILE = "/cache/recovery/uncrypt_file";
    private static final String UPDATABLE_DEVICE_CONFIG_SERVICE_CLASS = "com.android.server.deviceconfig.DeviceConfigInit$Lifecycle";
    private static final String USB_SERVICE_CLASS = "com.android.server.usb.UsbService$Lifecycle";
    private static final String UWB_APEX_SERVICE_JAR_PATH = "/apex/com.android.uwb/javalib/service-uwb.jar";
    private static final String UWB_SERVICE_CLASS = "com.android.server.uwb.UwbService";
    private static final String VIRTUAL_DEVICE_MANAGER_SERVICE_CLASS = "com.android.server.companion.virtual.VirtualDeviceManagerService";
    private static final String VOICE_RECOGNITION_MANAGER_SERVICE_CLASS = "com.android.server.voiceinteraction.VoiceInteractionManagerService";
    private static final String WALLPAPER_EFFECTS_GENERATION_MANAGER_SERVICE_CLASS = "com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService";
    private static final String WALLPAPER_SERVICE_CLASS = "com.android.server.wallpaper.WallpaperManagerService$Lifecycle";
    private static final String WEAR_CONNECTIVITY_SERVICE_CLASS = "com.android.clockwork.connectivity.WearConnectivityService";
    private static final String WEAR_DISPLAYOFFLOAD_SERVICE_CLASS = "com.android.clockwork.displayoffload.DisplayOffloadService";
    private static final String WEAR_DISPLAY_SERVICE_CLASS = "com.android.clockwork.display.WearDisplayService";
    private static final String WEAR_GLOBAL_ACTIONS_SERVICE_CLASS = "com.android.clockwork.globalactions.GlobalActionsService";
    private static final String WEAR_POWER_SERVICE_CLASS = "com.android.clockwork.power.WearPowerService";
    private static final String WEAR_SIDEKICK_SERVICE_CLASS = "com.google.android.clockwork.sidekick.SidekickService";
    private static final String WEAR_TIME_SERVICE_CLASS = "com.android.clockwork.time.WearTimeService";
    private static final String WIFI_APEX_SERVICE_JAR_PATH = "/apex/com.android.wifi/javalib/service-wifi.jar";
    private static final String WIFI_AWARE_SERVICE_CLASS = "com.android.server.wifi.aware.WifiAwareService";
    private static final String WIFI_P2P_SERVICE_CLASS = "com.android.server.wifi.p2p.WifiP2pService";
    private static final String WIFI_RTT_SERVICE_CLASS = "com.android.server.wifi.rtt.RttService";
    private static final String WIFI_SCANNING_SERVICE_CLASS = "com.android.server.wifi.scanner.WifiScanningService";
    private static final String WIFI_SERVICE_CLASS = "com.android.server.wifi.WifiService";
    private static final int sMaxBinderThreads = 31;
    private static LinkedList<Pair<String, ApplicationErrorReport.CrashInfo>> sPendingWtfs;
    private ActivityManagerService mActivityManagerService;
    private ContentResolver mContentResolver;
    private DataLoaderManagerService mDataLoaderManagerService;
    private DisplayManagerService mDisplayManagerService;
    private EntropyMixer mEntropyMixer;
    private boolean mFirstBoot;
    private PackageManager mPackageManager;
    private PackageManagerService mPackageManagerService;
    private PowerManagerService mPowerManagerService;
    private Timer mProfilerSnapshotTimer;
    private final boolean mRuntimeRestart;
    private final long mRuntimeStartElapsedTime;
    private final long mRuntimeStartUptime;
    public ISystemServerSocExt mSocExt;
    private final int mStartCount;
    private Context mSystemContext;
    private ISystemServerExt mSystemServerExt;
    private SystemServiceManager mSystemServiceManager;
    private WebViewUpdateService mWebViewUpdateService;
    private WindowManagerGlobalLock mWindowManagerGlobalLock;
    private Future<?> mZygotePreload;
    private long mIncrementalServiceHandle = 0;
    private final SystemServerDumper mDumper = new SystemServerDumper();
    private IServicesPreloadExt mServicesPreloadExt = (IServicesPreloadExt) ExtLoader.type(IServicesPreloadExt.class).base(this).create();
    private final int mFactoryTestMode = FactoryTest.getMode();

    private static native void fdtrackAbort();

    private static native void initZygoteChildHeapProfiling();

    private static native void setIncrementalServiceSystemReady(long j);

    private static native void startHidlServices();

    private static native void startISensorManagerService();

    private static native void startIStatsService();

    private static native long startIncrementalService();

    private static native void startMemtrackProxyService();

    private static int getMaxFd() {
        FileDescriptor fileDescriptor = null;
        try {
            try {
                fileDescriptor = Os.open("/dev/null", OsConstants.O_RDONLY | OsConstants.O_CLOEXEC, 0);
                int int$ = fileDescriptor.getInt$();
                try {
                    Os.close(fileDescriptor);
                    return int$;
                } catch (ErrnoException e) {
                    throw new RuntimeException(e);
                }
            } catch (ErrnoException e2) {
                Slog.e("System", "Failed to get maximum fd: " + e2);
                if (fileDescriptor == null) {
                    return Integer.MAX_VALUE;
                }
                try {
                    Os.close(fileDescriptor);
                    return Integer.MAX_VALUE;
                } catch (ErrnoException e3) {
                    throw new RuntimeException(e3);
                }
            }
        } catch (Throwable th) {
            if (fileDescriptor != null) {
                try {
                    Os.close(fileDescriptor);
                } catch (ErrnoException e4) {
                    throw new RuntimeException(e4);
                }
            }
            throw th;
        }
    }

    private static void dumpHprof() {
        TreeSet treeSet = new TreeSet();
        for (File file : HEAP_DUMP_PATH.listFiles()) {
            if (file.isFile() && file.getName().startsWith("fdtrack-")) {
                treeSet.add(file);
            }
        }
        if (treeSet.size() >= 2) {
            treeSet.pollLast();
            Iterator it = treeSet.iterator();
            while (it.hasNext()) {
                File file2 = (File) it.next();
                if (!file2.delete()) {
                    Slog.w("System", "Failed to clean up hprof " + file2);
                }
            }
        }
        try {
            Debug.dumpHprofData("/data/system/heapdump/fdtrack-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".hprof");
        } catch (IOException e) {
            Slog.e("System", "Failed to dump fdtrack hprof", e);
        }
    }

    private static void spawnFdLeakCheckThread() {
        final int i = SystemProperties.getInt(SYSPROP_FDTRACK_ENABLE_THRESHOLD, 1600);
        final int i2 = SystemProperties.getInt(SYSPROP_FDTRACK_ABORT_THRESHOLD, 3000);
        final int i3 = SystemProperties.getInt(SYSPROP_FDTRACK_INTERVAL, 120);
        new Thread(new Runnable() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SystemServer.lambda$spawnFdLeakCheckThread$0(i, i2, i3);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$spawnFdLeakCheckThread$0(int i, int i2, int i3) {
        boolean z = false;
        long j = 0;
        while (true) {
            int maxFd = getMaxFd();
            if (maxFd > i) {
                System.gc();
                System.runFinalization();
                maxFd = getMaxFd();
            }
            if (maxFd > i && !z) {
                Slog.i("System", "fdtrack enable threshold reached, enabling");
                FrameworkStatsLog.write(364, 2, maxFd);
                System.loadLibrary("fdtrack");
                z = true;
            } else if (maxFd > i2) {
                Slog.i("System", "fdtrack abort threshold reached, dumping and aborting");
                FrameworkStatsLog.write(364, 3, maxFd);
                dumpHprof();
                fdtrackAbort();
            } else {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                if (elapsedRealtime > j) {
                    long j2 = elapsedRealtime + ClipboardService.DEFAULT_CLIPBOARD_TIMEOUT_MILLIS;
                    FrameworkStatsLog.write(364, z ? 2 : 1, maxFd);
                    j = j2;
                }
            }
            try {
                Thread.sleep(i3 * 1000);
            } catch (InterruptedException unused) {
            }
        }
    }

    public static void main(String[] strArr) {
        new SystemServer().run();
    }

    public SystemServer() {
        int i = SystemProperties.getInt(SYSPROP_START_COUNT, 0) + 1;
        this.mStartCount = i;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.mRuntimeStartElapsedTime = elapsedRealtime;
        long uptimeMillis = SystemClock.uptimeMillis();
        this.mRuntimeStartUptime = uptimeMillis;
        Process.setStartTimes(elapsedRealtime, uptimeMillis, elapsedRealtime, uptimeMillis);
        this.mRuntimeRestart = i > 1;
        this.mServicesPreloadExt.preload(SystemServer.class.getClassLoader());
        ISystemServerExt iSystemServerExt = (ISystemServerExt) ExtLoader.type(ISystemServerExt.class).create();
        this.mSystemServerExt = iSystemServerExt;
        int systemThemeStyle = iSystemServerExt.getSystemThemeStyle();
        if (systemThemeStyle != -1) {
            DEFAULT_SYSTEM_THEME = systemThemeStyle;
        }
        this.mSocExt = (ISystemServerSocExt) ExtLoader.type(ISystemServerSocExt.class).base(this).create();
    }

    @Override // android.util.Dumpable
    public String getDumpableName() {
        return SystemServer.class.getSimpleName();
    }

    @Override // android.util.Dumpable
    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.printf("Runtime restart: %b\n", Boolean.valueOf(this.mRuntimeRestart));
        printWriter.printf("Start count: %d\n", Integer.valueOf(this.mStartCount));
        printWriter.print("Runtime start-up time: ");
        TimeUtils.formatDuration(this.mRuntimeStartUptime, printWriter);
        printWriter.println();
        printWriter.print("Runtime start-elapsed time: ");
        TimeUtils.formatDuration(this.mRuntimeStartElapsedTime, printWriter);
        printWriter.println();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class SystemServerDumper extends Binder {

        @GuardedBy({"mDumpables"})
        private final ArrayMap<String, Dumpable> mDumpables;

        private SystemServerDumper() {
            this.mDumpables = new ArrayMap<>(4);
        }

        @Override // android.os.Binder
        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            IndentingPrintWriter indentingPrintWriter;
            boolean z = strArr != null && strArr.length > 0;
            synchronized (this.mDumpables) {
                if (z) {
                    try {
                        if ("--list".equals(strArr[0])) {
                            int size = this.mDumpables.size();
                            for (int i = 0; i < size; i++) {
                                printWriter.println(this.mDumpables.keyAt(i));
                            }
                            return;
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                if (z && "--name".equals(strArr[0])) {
                    if (strArr.length < 2) {
                        printWriter.println("Must pass at least one argument to --name");
                        return;
                    }
                    String str = strArr[1];
                    Dumpable dumpable = this.mDumpables.get(str);
                    if (dumpable == null) {
                        printWriter.printf("No dumpable named %s\n", str);
                        return;
                    }
                    indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
                    try {
                        dumpable.dump(indentingPrintWriter, (String[]) Arrays.copyOfRange(strArr, 2, strArr.length));
                        indentingPrintWriter.close();
                        return;
                    } finally {
                    }
                }
                if (SystemServer.this.mSystemServerExt.stabilityDynamicLogConfig(printWriter, strArr)) {
                    return;
                }
                int size2 = this.mDumpables.size();
                indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
                for (int i2 = 0; i2 < size2; i2++) {
                    try {
                        Dumpable valueAt = this.mDumpables.valueAt(i2);
                        indentingPrintWriter.printf("%s:\n", new Object[]{valueAt.getDumpableName()});
                        indentingPrintWriter.increaseIndent();
                        valueAt.dump(indentingPrintWriter, strArr);
                        indentingPrintWriter.decreaseIndent();
                        indentingPrintWriter.println();
                    } finally {
                    }
                }
                indentingPrintWriter.close();
                return;
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addDumpable(Dumpable dumpable) {
            synchronized (this.mDumpables) {
                this.mDumpables.put(dumpable.getDumpableName(), dumpable);
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    private void run() {
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog();
        try {
            timingsTraceAndSlog.traceBegin("InitBeforeStartServices");
            this.mSystemServerExt.runBootProtector(this.mStartCount);
            SystemProperties.set(SYSPROP_START_COUNT, String.valueOf(this.mStartCount));
            SystemProperties.set(SYSPROP_START_ELAPSED, String.valueOf(this.mRuntimeStartElapsedTime));
            SystemProperties.set(SYSPROP_START_UPTIME, String.valueOf(this.mRuntimeStartUptime));
            EventLog.writeEvent(EventLogTags.SYSTEM_SERVER_START, Integer.valueOf(this.mStartCount), Long.valueOf(this.mRuntimeStartUptime), Long.valueOf(this.mRuntimeStartElapsedTime));
            SystemTimeZone.initializeTimeZoneSettingsIfRequired();
            if (!SystemProperties.get("persist.sys.language").isEmpty()) {
                SystemProperties.set("persist.sys.locale", Locale.getDefault().toLanguageTag());
                SystemProperties.set("persist.sys.language", "");
                SystemProperties.set("persist.sys.country", "");
                SystemProperties.set("persist.sys.localevar", "");
            }
            Binder.setWarnOnBlocking(true);
            PackageItemInfo.forceSafeLabels();
            SQLiteGlobal.sDefaultSyncMode = "FULL";
            SQLiteCompatibilityWalFlags.init((String) null);
            Slog.i(TAG, "Entered the Android system server!");
            long elapsedRealtime = SystemClock.elapsedRealtime();
            EventLog.writeEvent(EventLogTags.BOOT_PROGRESS_SYSTEM_RUN, elapsedRealtime);
            if (!this.mRuntimeRestart) {
                FrameworkStatsLog.write(FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED, 19, elapsedRealtime);
            }
            ((IPackageManagerServiceUtilsExt) ExtLoader.type(IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Android:SysServerInit_START");
            this.mSystemServerExt.setBootstage(true);
            SystemProperties.set("persist.sys.dalvik.vm.lib.2", VMRuntime.getRuntime().vmLibrary());
            VMRuntime.getRuntime().clearGrowthLimit();
            Build.ensureFingerprintProperty();
            Environment.setUserRequired(true);
            BaseBundle.setShouldDefuse(true);
            Parcel.setStackTraceParceling(true);
            BinderInternal.disableBackgroundScheduling(true);
            BinderInternal.setMaxThreads(31);
            Process.setCanSelfBackground(false);
            Looper.prepareMainLooper();
            Looper.getMainLooper().setSlowLogThresholdMs(SLOW_DISPATCH_THRESHOLD_MS, SLOW_DELIVERY_THRESHOLD_MS);
            SystemServiceRegistry.sEnableServiceNotFoundWtf = true;
            System.loadLibrary("android_servers");
            initZygoteChildHeapProfiling();
            performPendingShutdown();
            createSystemContext();
            ActivityThread.initializeMainlineModules();
            ServiceManager.addService("system_server_dumper", this.mDumper);
            this.mDumper.addDumpable(this);
            SystemServiceManager systemServiceManager = new SystemServiceManager(this.mSystemContext);
            this.mSystemServiceManager = systemServiceManager;
            systemServiceManager.setStartInfo(this.mRuntimeRestart, this.mRuntimeStartElapsedTime, this.mRuntimeStartUptime);
            this.mDumper.addDumpable(this.mSystemServiceManager);
            LocalServices.addService(SystemServiceManager.class, this.mSystemServiceManager);
            this.mDumper.addDumpable(SystemServerInitThreadPool.start());
            this.mSystemServerExt.initFontsForserializeFontMap();
            Typeface.loadPreinstalledSystemFontMap();
            if (Build.IS_DEBUGGABLE) {
                String str = SystemProperties.get("persist.sys.dalvik.jvmtiagent");
                if (!str.isEmpty()) {
                    int indexOf = str.indexOf(61);
                    try {
                        Debug.attachJvmtiAgent(str.substring(0, indexOf), str.substring(indexOf + 1, str.length()), null);
                    } catch (Exception unused) {
                        Slog.e("System", "*************************************************");
                        Slog.e("System", "********** Failed to load jvmti plugin: " + str);
                    }
                }
            }
            timingsTraceAndSlog.traceEnd();
            this.mSystemServerExt.initSystemServer(this.mSystemContext);
            this.mSocExt.setPrameters(this.mSystemServiceManager, this.mSystemContext);
            timingsTraceAndSlog.traceBegin("initBadBehaviorDefense");
            this.mSystemServerExt.initBadBehaviorDefense(this.mSystemContext);
            timingsTraceAndSlog.traceEnd();
            RuntimeInit.setDefaultApplicationWtfHandler(new RuntimeInit.ApplicationWtfHandler() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda4
                public final boolean handleApplicationWtf(IBinder iBinder, String str2, boolean z, ApplicationErrorReport.ParcelableCrashInfo parcelableCrashInfo, int i) {
                    boolean handleEarlySystemWtf;
                    handleEarlySystemWtf = SystemServer.handleEarlySystemWtf(iBinder, str2, z, parcelableCrashInfo, i);
                    return handleEarlySystemWtf;
                }
            });
            try {
                timingsTraceAndSlog.traceBegin("StartServices");
                startBootstrapServices(timingsTraceAndSlog);
                startCoreServices(timingsTraceAndSlog);
                startOtherServices(timingsTraceAndSlog);
                startApexServices(timingsTraceAndSlog);
                updateWatchdogTimeout(timingsTraceAndSlog);
                timingsTraceAndSlog.traceEnd();
                StrictMode.initVmDefaults(null);
                if (!this.mRuntimeRestart && !isFirstBootOrUpgrade()) {
                    long elapsedRealtime2 = SystemClock.elapsedRealtime();
                    FrameworkStatsLog.write(FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED, 20, elapsedRealtime2);
                    if (elapsedRealtime2 > 60000) {
                        Slog.wtf("SystemServerTiming", "SystemServer init took too long. uptimeMillis=" + elapsedRealtime2);
                    }
                }
                this.mSystemServerExt.setBootstage(false);
                Process.setThreadPriority(-2);
                ((IPackageManagerServiceUtilsExt) ExtLoader.type(IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Android:SysServerInit_END");
                Looper.loop();
                throw new RuntimeException("Main thread loop unexpectedly exited");
            } finally {
            }
        } catch (Throwable th) {
            timingsTraceAndSlog.traceEnd();
            throw th;
        }
    }

    private static boolean isValidTimeZoneId(String str) {
        return (str == null || str.isEmpty() || !ZoneInfoDb.getInstance().hasTimeZone(str)) ? false : true;
    }

    private boolean isFirstBootOrUpgrade() {
        return this.mPackageManagerService.isFirstBoot() || this.mPackageManagerService.isDeviceUpgrading();
    }

    private void reportWtf(String str, Throwable th) {
        Slog.w(TAG, "***********************************************");
        Slog.wtf(TAG, "BOOT FAILURE " + str, th);
    }

    private void performPendingShutdown() {
        String str = SystemProperties.get("sys.shutdown.requested", "");
        if (str == null || str.length() <= 0) {
            return;
        }
        final boolean z = str.charAt(0) == '1';
        String str2 = null;
        final String substring = str.length() > 1 ? str.substring(1, str.length()) : null;
        if (substring != null && substring.startsWith("recovery-update")) {
            File file = new File(UNCRYPT_PACKAGE_FILE);
            if (file.exists()) {
                try {
                    str2 = FileUtils.readTextFile(file, 0, null);
                } catch (IOException e) {
                    Slog.e(TAG, "Error reading uncrypt package file", e);
                }
                if (str2 != null && str2.startsWith("/data") && !new File(BLOCK_MAP_FILE).exists()) {
                    Slog.e(TAG, "Can't find block map file, uncrypt failed or unexpected runtime restart?");
                    return;
                }
            }
        }
        Message obtain = Message.obtain(UiThread.getHandler(), new Runnable() { // from class: com.android.server.SystemServer.1
            @Override // java.lang.Runnable
            public void run() {
                ShutdownThread.rebootOrShutdown((Context) null, z, substring);
            }
        });
        obtain.setAsynchronous(true);
        UiThread.getHandler().sendMessage(obtain);
    }

    private void createSystemContext() {
        ActivityThread systemMain = ActivityThread.systemMain();
        ContextImpl systemContext = systemMain.getSystemContext();
        this.mSystemContext = systemContext;
        systemContext.setTheme(DEFAULT_SYSTEM_THEME);
        systemMain.getSystemUiContext().setTheme(DEFAULT_SYSTEM_THEME);
    }

    /* JADX WARN: Type inference failed for: r3v4, types: [com.android.server.compat.PlatformCompat, android.os.IBinder] */
    private void startBootstrapServices(TimingsTraceAndSlog timingsTraceAndSlog) {
        timingsTraceAndSlog.traceBegin("startBootstrapServices");
        this.mSystemServerExt.setDataNormalizationManager();
        timingsTraceAndSlog.traceBegin("ArtModuleServiceInitializer");
        ArtModuleServiceInitializer.setArtModuleServiceManager(new ArtModuleServiceManager());
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartWatchdog");
        Watchdog watchdog = Watchdog.getInstance();
        watchdog.start();
        this.mDumper.addDumpable(watchdog);
        timingsTraceAndSlog.traceEnd();
        Slog.i(TAG, "Reading configuration...");
        timingsTraceAndSlog.traceBegin("ReadingSystemConfig");
        SystemServerInitThreadPool.submit(new Runnable() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                SystemConfig.getInstance();
            }
        }, "ReadingSystemConfig");
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("PlatformCompat");
        ?? platformCompat = new PlatformCompat(this.mSystemContext);
        ServiceManager.addService("platform_compat", (IBinder) platformCompat);
        ServiceManager.addService("platform_compat_native", new PlatformCompatNative(platformCompat));
        AppCompatCallbacks.install(new long[0]);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartFileIntegrityService");
        this.mSystemServiceManager.startService(FileIntegrityService.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartInstaller");
        Installer startService = this.mSystemServiceManager.startService((Class<Installer>) Installer.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("DeviceIdentifiersPolicyService");
        this.mSystemServiceManager.startService(DeviceIdentifiersPolicyService.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("UriGrantsManagerService");
        this.mSystemServiceManager.startService(UriGrantsManagerService.Lifecycle.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartPowerStatsService");
        this.mSystemServiceManager.startService(PowerStatsService.class);
        timingsTraceAndSlog.traceEnd();
        this.mSystemServerExt.addOplusDevicePolicyService();
        timingsTraceAndSlog.traceBegin("StartIStatsService");
        startIStatsService();
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("MemtrackProxyService");
        startMemtrackProxyService();
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartAccessCheckingService");
        this.mSystemServiceManager.startService(AccessCheckingService.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartActivityManager");
        ActivityTaskManagerService service = this.mSystemServiceManager.startService(ActivityTaskManagerService.Lifecycle.class).getService();
        Slog.i(TAG, "Ams Service");
        ActivityManagerService startService2 = ActivityManagerService.Lifecycle.startService(this.mSystemServiceManager, service);
        this.mActivityManagerService = startService2;
        startService2.setSystemServiceManager(this.mSystemServiceManager);
        this.mActivityManagerService.setInstaller(startService);
        this.mWindowManagerGlobalLock = service.getGlobalLock();
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartDataLoaderManagerService");
        this.mDataLoaderManagerService = this.mSystemServiceManager.startService(DataLoaderManagerService.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartIncrementalService");
        this.mIncrementalServiceHandle = startIncrementalService();
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartPowerManager");
        Slog.i(TAG, "Power Service");
        this.mPowerManagerService = this.mSystemServiceManager.startService(PowerManagerService.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartThermalManager");
        this.mSystemServiceManager.startService(ThermalManagerService.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartHintManager");
        this.mSystemServiceManager.startService(HintManagerService.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("InitPowerManagement");
        this.mActivityManagerService.initPowerManagement();
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartRecoverySystemService");
        this.mSystemServiceManager.startService(RecoverySystemService.Lifecycle.class);
        timingsTraceAndSlog.traceEnd();
        RescueParty.registerHealthObserver(this.mSystemContext);
        PackageWatchdog.getInstance(this.mSystemContext).noteBoot();
        timingsTraceAndSlog.traceBegin("StartLightsService");
        this.mSystemServiceManager.startService(LightsService.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartDisplayOffloadService");
        if (SystemProperties.getBoolean("config.enable_display_offload", false)) {
            this.mSystemServiceManager.startService(WEAR_DISPLAYOFFLOAD_SERVICE_CLASS);
        }
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartSidekickService");
        if (SystemProperties.getBoolean("config.enable_sidekick_graphics", false)) {
            this.mSystemServiceManager.startService(WEAR_SIDEKICK_SERVICE_CLASS);
        }
        timingsTraceAndSlog.traceEnd();
        Slog.i(TAG, "DisplayManager Service");
        timingsTraceAndSlog.traceBegin("StartDisplayManager");
        this.mDisplayManagerService = (DisplayManagerService) this.mSystemServiceManager.startService(DisplayManagerService.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("WaitForDisplay");
        this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, 100);
        timingsTraceAndSlog.traceEnd();
        if (!this.mRuntimeRestart) {
            FrameworkStatsLog.write(FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED, 14, SystemClock.elapsedRealtime());
        }
        this.mSystemServerExt.waitForFutureNoInterrupt();
        timingsTraceAndSlog.traceBegin("StartDomainVerificationService");
        SystemService domainVerificationService = new DomainVerificationService(this.mSystemContext, SystemConfig.getInstance(), (PlatformCompat) platformCompat);
        this.mSystemServiceManager.startService(domainVerificationService);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartPackageManagerService");
        try {
            Watchdog.getInstance().pauseWatchingCurrentThread("packagemanagermain");
            this.mPackageManagerService = PackageManagerService.main(this.mSystemContext, startService, domainVerificationService, this.mFactoryTestMode != 0);
            Watchdog.getInstance().resumeWatchingCurrentThread("packagemanagermain");
            this.mFirstBoot = this.mPackageManagerService.isFirstBoot();
            this.mPackageManager = this.mSystemContext.getPackageManager();
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("DexUseManagerLocal");
            LocalManagerRegistry.addManager(DexUseManagerLocal.class, DexUseManagerLocal.createInstance(this.mSystemContext));
            timingsTraceAndSlog.traceEnd();
            if (!this.mRuntimeRestart && !isFirstBootOrUpgrade()) {
                FrameworkStatsLog.write(FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED, 15, SystemClock.elapsedRealtime());
            }
            if (!SystemProperties.getBoolean("config.disable_otadexopt", false)) {
                timingsTraceAndSlog.traceBegin("StartOtaDexOptService");
                try {
                    Watchdog.getInstance().pauseWatchingCurrentThread("moveab");
                    this.mSystemServerExt.addOtaDexoptService(this.mSystemContext, this.mPackageManagerService);
                } finally {
                    try {
                    } finally {
                    }
                }
            }
            timingsTraceAndSlog.traceBegin("StartUserManagerService");
            this.mSystemServiceManager.startService(UserManagerService.LifeCycle.class);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("InitAttributerCache");
            AttributeCache.init(this.mSystemContext);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("SetSystemProcess");
            this.mActivityManagerService.setSystemProcess();
            timingsTraceAndSlog.traceEnd();
            platformCompat.registerPackageReceiver(this.mSystemContext);
            timingsTraceAndSlog.traceBegin("InitWatchdog");
            watchdog.init(this.mSystemContext, this.mActivityManagerService);
            timingsTraceAndSlog.traceEnd();
            this.mDisplayManagerService.setupSchedulerPolicies();
            timingsTraceAndSlog.traceBegin("StartOverlayManagerService");
            this.mSystemServiceManager.startService((SystemService) new OverlayManagerService(this.mSystemContext));
            timingsTraceAndSlog.traceEnd();
            Slog.i(TAG, "Sensor Service");
            timingsTraceAndSlog.traceBegin("StartResourcesManagerService");
            SystemService resourcesManagerService = new ResourcesManagerService(this.mSystemContext);
            resourcesManagerService.setActivityManagerService(this.mActivityManagerService);
            this.mSystemServiceManager.startService(resourcesManagerService);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartSensorPrivacyService");
            this.mSystemServiceManager.startService((SystemService) new SensorPrivacyService(this.mSystemContext));
            timingsTraceAndSlog.traceEnd();
            if (SystemProperties.getInt("persist.sys.displayinset.top", 0) > 0) {
                this.mActivityManagerService.updateSystemUiContext();
                ((DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class)).onOverlayChanged();
            }
            timingsTraceAndSlog.traceBegin("StartSensorService");
            this.mSystemServiceManager.startService(SensorService.class);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceEnd();
            this.mSystemServerExt.startBootstrapServices();
        } catch (Throwable th) {
            Watchdog.getInstance().resumeWatchingCurrentThread("packagemanagermain");
            throw th;
        }
    }

    private void startCoreServices(TimingsTraceAndSlog timingsTraceAndSlog) {
        timingsTraceAndSlog.traceBegin("startCoreServices");
        timingsTraceAndSlog.traceBegin("StartSystemConfigService");
        this.mSystemServiceManager.startService(SystemConfigService.class);
        timingsTraceAndSlog.traceEnd();
        Slog.i(TAG, "Battery Service");
        timingsTraceAndSlog.traceBegin("StartBatteryService");
        this.mSystemServiceManager.startService(BatteryService.class);
        timingsTraceAndSlog.traceEnd();
        Slog.i(TAG, "UsageStats Service");
        timingsTraceAndSlog.traceBegin("StartUsageService");
        this.mSystemServerExt.startUsageStatsService(this.mSystemServiceManager);
        this.mActivityManagerService.setUsageStatsManager((UsageStatsManagerInternal) LocalServices.getService(UsageStatsManagerInternal.class));
        timingsTraceAndSlog.traceEnd();
        if (this.mPackageManager.hasSystemFeature("android.software.webview")) {
            timingsTraceAndSlog.traceBegin("StartWebViewUpdateService");
            this.mWebViewUpdateService = this.mSystemServiceManager.startService(WebViewUpdateService.class);
            timingsTraceAndSlog.traceEnd();
        }
        timingsTraceAndSlog.traceBegin("StartCachedDeviceStateService");
        this.mSystemServiceManager.startService(CachedDeviceStateService.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartBinderCallsStatsService");
        this.mSystemServiceManager.startService(BinderCallsStatsService.LifeCycle.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartLooperStatsService");
        this.mSystemServiceManager.startService(LooperStatsService.Lifecycle.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartRollbackManagerService");
        this.mSystemServiceManager.startService(ROLLBACK_MANAGER_SERVICE_CLASS);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartNativeTombstoneManagerService");
        this.mSystemServiceManager.startService(NativeTombstoneManagerService.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartBugreportManagerService");
        this.mSystemServiceManager.startService(BugreportManagerService.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin(GpuService.TAG);
        this.mSystemServiceManager.startService(GpuService.class);
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartRemoteProvisioningService");
        this.mSystemServiceManager.startService(RemoteProvisioningService.class);
        timingsTraceAndSlog.traceEnd();
        if (Build.IS_DEBUGGABLE || Build.IS_ENG) {
            timingsTraceAndSlog.traceBegin("CpuMonitorService");
            this.mSystemServiceManager.startService(CpuMonitorService.class);
            timingsTraceAndSlog.traceEnd();
        }
        timingsTraceAndSlog.traceEnd();
        this.mSystemServerExt.startCoreServices();
    }

    /* JADX WARN: Can't wrap try/catch for region: R(156:196|(3:197|198|199)|200|(1:202)|203|(1:570)|207|(5:208|209|(1:211)|213|214)|(2:215|216)|217|(1:219)(1:564)|220|(1:222)(1:563)|223|(1:225)(1:562)|226|227|228|229|230|231|232|(2:233|234)|(2:235|236)|237|(1:239)|240|(2:241|242)|(2:243|244)|245|(1:247)|248|(1:250)|251|(1:253)|254|(1:256)|257|(1:259)|260|261|262|263|264|265|266|267|268|269|270|271|272|273|274|275|276|277|278|279|280|281|282|283|284|285|286|287|288|289|290|291|292|293|294|295|296|297|298|299|300|(4:302|303|304|305)|(4:310|311|312|313)|317|(1:319)(1:499)|320|(1:322)(6:487|488|489|490|491|492)|323|(1:325)|(1:327)|(1:329)|(4:331|332|333|334)|338|(1:340)|341|342|343|344|(1:484)|(4:350|351|352|353)|357|358|359|360|(1:362)|363|(1:365)|366|(1:368)|369|(1:480)|373|(1:375)|376|(1:378)|379|380|381|382|383|384|385|(1:473)(6:388|389|390|391|392|393)|394|395|396|397|(1:399)|400|(1:402)|403|(1:405)|406|(1:408)|409|(1:463)|413|(1:462)|417|(1:419)|420|(1:422)|423|(1:425)|426|427|428|429|430|431|(1:433)|(1:435)|(1:437)|438|(4:440|441|442|443)|(4:448|449|450|451)|455) */
    /* JADX WARN: Can't wrap try/catch for region: R(162:196|(3:197|198|199)|200|(1:202)|203|(1:570)|207|208|209|(1:211)|213|214|215|216|217|(1:219)(1:564)|220|(1:222)(1:563)|223|(1:225)(1:562)|226|227|228|229|230|231|232|233|234|(2:235|236)|237|(1:239)|240|(2:241|242)|(2:243|244)|245|(1:247)|248|(1:250)|251|(1:253)|254|(1:256)|257|(1:259)|260|261|262|263|264|265|266|267|268|269|270|271|272|273|274|275|276|277|278|279|280|281|282|283|284|285|286|287|288|289|290|291|292|293|294|295|296|297|298|299|300|(4:302|303|304|305)|(4:310|311|312|313)|317|(1:319)(1:499)|320|(1:322)(6:487|488|489|490|491|492)|323|(1:325)|(1:327)|(1:329)|(4:331|332|333|334)|338|(1:340)|341|342|343|344|(1:484)|(4:350|351|352|353)|357|358|359|360|(1:362)|363|(1:365)|366|(1:368)|369|(1:480)|373|(1:375)|376|(1:378)|379|380|381|382|383|384|385|(1:473)(6:388|389|390|391|392|393)|394|395|396|397|(1:399)|400|(1:402)|403|(1:405)|406|(1:408)|409|(1:463)|413|(1:462)|417|(1:419)|420|(1:422)|423|(1:425)|426|427|428|429|430|431|(1:433)|(1:435)|(1:437)|438|(4:440|441|442|443)|(4:448|449|450|451)|455) */
    /* JADX WARN: Can't wrap try/catch for region: R(163:196|(3:197|198|199)|200|(1:202)|203|(1:570)|207|208|209|(1:211)|213|214|215|216|217|(1:219)(1:564)|220|(1:222)(1:563)|223|(1:225)(1:562)|226|227|228|229|230|231|232|233|234|235|236|237|(1:239)|240|(2:241|242)|(2:243|244)|245|(1:247)|248|(1:250)|251|(1:253)|254|(1:256)|257|(1:259)|260|261|262|263|264|265|266|267|268|269|270|271|272|273|274|275|276|277|278|279|280|281|282|283|284|285|286|287|288|289|290|291|292|293|294|295|296|297|298|299|300|(4:302|303|304|305)|(4:310|311|312|313)|317|(1:319)(1:499)|320|(1:322)(6:487|488|489|490|491|492)|323|(1:325)|(1:327)|(1:329)|(4:331|332|333|334)|338|(1:340)|341|342|343|344|(1:484)|(4:350|351|352|353)|357|358|359|360|(1:362)|363|(1:365)|366|(1:368)|369|(1:480)|373|(1:375)|376|(1:378)|379|380|381|382|383|384|385|(1:473)(6:388|389|390|391|392|393)|394|395|396|397|(1:399)|400|(1:402)|403|(1:405)|406|(1:408)|409|(1:463)|413|(1:462)|417|(1:419)|420|(1:422)|423|(1:425)|426|427|428|429|430|431|(1:433)|(1:435)|(1:437)|438|(4:440|441|442|443)|(4:448|449|450|451)|455) */
    /* JADX WARN: Can't wrap try/catch for region: R(165:196|197|198|199|200|(1:202)|203|(1:570)|207|208|209|(1:211)|213|214|215|216|217|(1:219)(1:564)|220|(1:222)(1:563)|223|(1:225)(1:562)|226|227|228|229|230|231|232|233|234|235|236|237|(1:239)|240|(2:241|242)|(2:243|244)|245|(1:247)|248|(1:250)|251|(1:253)|254|(1:256)|257|(1:259)|260|261|262|263|264|265|266|267|268|269|270|271|272|273|274|275|276|277|278|279|280|281|282|283|284|285|286|287|288|289|290|291|292|293|294|295|296|297|298|299|300|(4:302|303|304|305)|(4:310|311|312|313)|317|(1:319)(1:499)|320|(1:322)(6:487|488|489|490|491|492)|323|(1:325)|(1:327)|(1:329)|(4:331|332|333|334)|338|(1:340)|341|342|343|344|(1:484)|(4:350|351|352|353)|357|358|359|360|(1:362)|363|(1:365)|366|(1:368)|369|(1:480)|373|(1:375)|376|(1:378)|379|380|381|382|383|384|385|(1:473)(6:388|389|390|391|392|393)|394|395|396|397|(1:399)|400|(1:402)|403|(1:405)|406|(1:408)|409|(1:463)|413|(1:462)|417|(1:419)|420|(1:422)|423|(1:425)|426|427|428|429|430|431|(1:433)|(1:435)|(1:437)|438|(4:440|441|442|443)|(4:448|449|450|451)|455) */
    /* JADX WARN: Can't wrap try/catch for region: R(91:8|9|(1:618)|15|(1:17)|18|(1:20)|21|(1:23)(1:617)|24|(1:27)|28|(1:30)(2:613|(1:615)(1:616))|31|(1:35)|36|37|(1:39)(2:609|(1:611))|40|(6:42|(1:44)(5:597|598|599|600|601)|45|46|47|48)(1:608)|49|50|51|52|(7:56|57|58|59|60|61|62)|69|70|71|72|73|74|75|(2:76|77)|78|79|80|81|(1:83)(167:196|197|198|199|200|(1:202)|203|(1:570)|207|208|209|(1:211)|213|214|215|216|217|(1:219)(1:564)|220|(1:222)(1:563)|223|(1:225)(1:562)|226|227|228|229|230|231|232|233|234|235|236|237|(1:239)|240|241|242|243|244|245|(1:247)|248|(1:250)|251|(1:253)|254|(1:256)|257|(1:259)|260|261|262|263|264|265|266|267|268|269|270|271|272|273|274|275|276|277|278|279|280|281|282|283|284|285|286|287|288|289|290|291|292|293|294|295|296|297|298|299|300|(4:302|303|304|305)|(4:310|311|312|313)|317|(1:319)(1:499)|320|(1:322)(6:487|488|489|490|491|492)|323|(1:325)|(1:327)|(1:329)|(4:331|332|333|334)|338|(1:340)|341|342|343|344|(1:484)|(4:350|351|352|353)|357|358|359|360|(1:362)|363|(1:365)|366|(1:368)|369|(1:480)|373|(1:375)|376|(1:378)|379|380|381|382|383|384|385|(1:473)(6:388|389|390|391|392|393)|394|395|396|397|(1:399)|400|(1:402)|403|(1:405)|406|(1:408)|409|(1:463)|413|(1:462)|417|(1:419)|420|(1:422)|423|(1:425)|426|427|428|429|430|431|(1:433)|(1:435)|(1:437)|438|(4:440|441|442|443)|(4:448|449|450|451)|455)|84|(1:86)|87|(1:89)|90|(1:92)|93|(1:95)|96|(1:98)|99|(1:101)(1:195)|102|(1:104)|105|(2:107|(1:109)(1:110))|111|(1:113)(1:194)|114|(2:189|190)|116|(1:118)|119|120|121|122|123|124|125|1187|(1:133)|134|(1:136)|137|(2:138|139)|140|(1:142)|143|(6:145|146|147|148|149|150)|155|156|(1:158)|159|(1:161)|162|(1:164)|165|(1:167)|168|169|170|171|172) */
    /* JADX WARN: Can't wrap try/catch for region: R(92:8|9|(1:618)|15|(1:17)|18|(1:20)|21|(1:23)(1:617)|24|(1:27)|28|(1:30)(2:613|(1:615)(1:616))|31|(1:35)|36|37|(1:39)(2:609|(1:611))|40|(6:42|(1:44)(5:597|598|599|600|601)|45|46|47|48)(1:608)|49|50|51|52|(7:56|57|58|59|60|61|62)|69|70|71|72|73|74|75|(2:76|77)|78|79|80|81|(1:83)(167:196|197|198|199|200|(1:202)|203|(1:570)|207|208|209|(1:211)|213|214|215|216|217|(1:219)(1:564)|220|(1:222)(1:563)|223|(1:225)(1:562)|226|227|228|229|230|231|232|233|234|235|236|237|(1:239)|240|241|242|243|244|245|(1:247)|248|(1:250)|251|(1:253)|254|(1:256)|257|(1:259)|260|261|262|263|264|265|266|267|268|269|270|271|272|273|274|275|276|277|278|279|280|281|282|283|284|285|286|287|288|289|290|291|292|293|294|295|296|297|298|299|300|(4:302|303|304|305)|(4:310|311|312|313)|317|(1:319)(1:499)|320|(1:322)(6:487|488|489|490|491|492)|323|(1:325)|(1:327)|(1:329)|(4:331|332|333|334)|338|(1:340)|341|342|343|344|(1:484)|(4:350|351|352|353)|357|358|359|360|(1:362)|363|(1:365)|366|(1:368)|369|(1:480)|373|(1:375)|376|(1:378)|379|380|381|382|383|384|385|(1:473)(6:388|389|390|391|392|393)|394|395|396|397|(1:399)|400|(1:402)|403|(1:405)|406|(1:408)|409|(1:463)|413|(1:462)|417|(1:419)|420|(1:422)|423|(1:425)|426|427|428|429|430|431|(1:433)|(1:435)|(1:437)|438|(4:440|441|442|443)|(4:448|449|450|451)|455)|84|(1:86)|87|(1:89)|90|(1:92)|93|(1:95)|96|(1:98)|99|(1:101)(1:195)|102|(1:104)|105|(2:107|(1:109)(1:110))|111|(1:113)(1:194)|114|(2:189|190)|116|(1:118)|119|120|121|122|123|124|125|1187|(1:133)|134|(1:136)|137|138|139|140|(1:142)|143|(6:145|146|147|148|149|150)|155|156|(1:158)|159|(1:161)|162|(1:164)|165|(1:167)|168|169|170|171|172) */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x117b, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x117c, code lost:
    
        reportWtf("RegisterLogMteState", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x1168, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x1169, code lost:
    
        reportWtf("making Window Manager Service ready", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x0ddd, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x0de1, code lost:
    
        reportWtf("starting MediaRouterService", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:460:0x0ddf, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x0de0, code lost:
    
        r2 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x0c51, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:466:0x0c52, code lost:
    
        reportWtf("starting CertBlacklister", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x0c06, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:476:0x0c07, code lost:
    
        reportWtf("starting RuntimeService", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0beb, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x0bec, code lost:
    
        reportWtf("starting DiskStats Service", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:482:0x0aec, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0aed, code lost:
    
        android.util.Slog.e(com.android.server.SystemServer.TAG, "Failure starting HardwarePropertiesManagerService", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0a8e, code lost:
    
        android.util.Slog.e(com.android.server.SystemServer.TAG, "Failure starting AdbService");
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x092c, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x092d, code lost:
    
        reportWtf("starting LocationTimeZoneManagerService service", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0915, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0916, code lost:
    
        reportWtf("starting AltitudeService service", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x08fa, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x08ff, code lost:
    
        reportWtf("starting TimeZoneDetectorService service", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x08fc, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x08fd, code lost:
    
        r23 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x08d9, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x08da, code lost:
    
        r23 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x08e0, code lost:
    
        reportWtf("starting Country Detector", r0);
        r1 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x08dd, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x08de, code lost:
    
        r23 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x08b0, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x08b1, code lost:
    
        reportWtf("starting TimeDetectorService service", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x086b, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x086c, code lost:
    
        reportWtf("starting UpdateLockService", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0850, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0851, code lost:
    
        reportWtf("starting SystemUpdateManagerService", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x082e, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:530:0x082f, code lost:
    
        r22 = r1;
        r12 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0836, code lost:
    
        reportWtf("starting VCN Management Service", r0);
        r12 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0832, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:534:0x0833, code lost:
    
        r22 = r1;
        r12 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x080f, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0813, code lost:
    
        reportWtf("starting VPN Manager Service", r0);
        r5 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0811, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:540:0x0812, code lost:
    
        r5 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:542:0x07e1, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x07e2, code lost:
    
        reportWtf("starting PacProxyService", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0697, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0698, code lost:
    
        reportWtf("initializing NetworkStackClient", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0680, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0681, code lost:
    
        reportWtf("initializing ConnectivityModuleConnector", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x04f3, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x04f4, code lost:
    
        reportWtf("performing fstrim", r0);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x1039  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x105a  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x1073  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x109e  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x113d  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x1188 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:189:0x1108 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x10ae  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x104e  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0513  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x05da  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x061e  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x063e  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x06e1  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x073c  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x076a  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x0787  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x07a4  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x07c1  */
    /* JADX WARN: Removed duplicated region for block: B:302:0x0943  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x095c  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x0985  */
    /* JADX WARN: Removed duplicated region for block: B:322:0x09b2  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x0a16  */
    /* JADX WARN: Removed duplicated region for block: B:327:0x0a27  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x0a3f  */
    /* JADX WARN: Removed duplicated region for block: B:331:0x0a50  */
    /* JADX WARN: Removed duplicated region for block: B:340:0x0a72  */
    /* JADX WARN: Removed duplicated region for block: B:346:0x0aa2  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x0abf  */
    /* JADX WARN: Removed duplicated region for block: B:362:0x0af9  */
    /* JADX WARN: Removed duplicated region for block: B:365:0x0b24  */
    /* JADX WARN: Removed duplicated region for block: B:368:0x0b56  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x0b6f  */
    /* JADX WARN: Removed duplicated region for block: B:375:0x0ba4  */
    /* JADX WARN: Removed duplicated region for block: B:378:0x0bcc  */
    /* JADX WARN: Removed duplicated region for block: B:387:0x0c24 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:399:0x0ca0  */
    /* JADX WARN: Removed duplicated region for block: B:402:0x0cb7  */
    /* JADX WARN: Removed duplicated region for block: B:405:0x0cdf  */
    /* JADX WARN: Removed duplicated region for block: B:408:0x0d2c  */
    /* JADX WARN: Removed duplicated region for block: B:411:0x0d45  */
    /* JADX WARN: Removed duplicated region for block: B:415:0x0d68  */
    /* JADX WARN: Removed duplicated region for block: B:419:0x0d8b  */
    /* JADX WARN: Removed duplicated region for block: B:422:0x0da4  */
    /* JADX WARN: Removed duplicated region for block: B:425:0x0dbd  */
    /* JADX WARN: Removed duplicated region for block: B:433:0x0e04  */
    /* JADX WARN: Removed duplicated region for block: B:435:0x0e18  */
    /* JADX WARN: Removed duplicated region for block: B:437:0x0e29  */
    /* JADX WARN: Removed duplicated region for block: B:440:0x0e5b  */
    /* JADX WARN: Removed duplicated region for block: B:448:0x0e71  */
    /* JADX WARN: Removed duplicated region for block: B:487:0x09bc  */
    /* JADX WARN: Removed duplicated region for block: B:499:0x0995  */
    /* JADX WARN: Removed duplicated region for block: B:562:0x064e  */
    /* JADX WARN: Removed duplicated region for block: B:563:0x062e  */
    /* JADX WARN: Removed duplicated region for block: B:564:0x05ea  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0502  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0f12  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0f76  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0f91  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x100f  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x102a  */
    /* JADX WARN: Type inference failed for: r12v0, types: [com.android.server.TelephonyRegistry, android.os.IBinder] */
    /* JADX WARN: Type inference failed for: r12v12, types: [com.android.server.VcnManagementService, android.os.IBinder] */
    /* JADX WARN: Type inference failed for: r23v2 */
    /* JADX WARN: Type inference failed for: r23v3 */
    /* JADX WARN: Type inference failed for: r23v4 */
    /* JADX WARN: Type inference failed for: r23v5 */
    /* JADX WARN: Type inference failed for: r23v7 */
    /* JADX WARN: Type inference failed for: r23v8 */
    /* JADX WARN: Type inference failed for: r5v19 */
    /* JADX WARN: Type inference failed for: r5v20 */
    /* JADX WARN: Type inference failed for: r5v23, types: [com.android.server.VpnManagerService, android.os.IBinder] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void startOtherServices(final TimingsTraceAndSlog timingsTraceAndSlog) {
        boolean z;
        ILockSettings iLockSettings;
        DevicePolicyManagerService.Lifecycle lifecycle;
        ILockSettings iLockSettings2;
        NetworkManagementService networkManagementService;
        NetworkPolicyManagerService networkPolicyManagerService;
        NetworkManagementService networkManagementService2;
        ?? r23;
        NetworkPolicyManagerService networkPolicyManagerService2;
        SystemServiceManager systemServiceManager;
        StringBuilder sb;
        NetworkTimeUpdateService networkTimeUpdateService;
        boolean hasSystemFeature;
        boolean hasSystemFeature2;
        boolean hasSystemFeature3;
        MediaRouterService mediaRouterService;
        final DevicePolicyManagerService.Lifecycle lifecycle2;
        VcnManagementService vcnManagementService;
        VpnManagerService vpnManagerService;
        ILockSettings iLockSettings3;
        NetworkTimeUpdateService networkTimeUpdateService2;
        StatusBarManagerService statusBarManagerService;
        Object startService;
        MmsServiceBroker mmsServiceBroker;
        final HsumBootUserInitializer createInstance;
        String[] strArr;
        StringBuilder sb2;
        timingsTraceAndSlog.traceBegin("startOtherServices");
        this.mSystemServiceManager.updateOtherServicesStartIndex();
        final Context context = this.mSystemContext;
        boolean z2 = SystemProperties.getBoolean("config.disable_systemtextclassifier", false);
        boolean z3 = SystemProperties.getBoolean("config.disable_networktime", false);
        boolean z4 = SystemProperties.getBoolean("config.disable_cameraservice", false);
        boolean equals = SystemProperties.get("ro.boot.qemu").equals("1");
        boolean z5 = SystemProperties.getBoolean("persist.sys.ltw.disable", false);
        final boolean hasSystemFeature4 = context.getPackageManager().hasSystemFeature("android.hardware.type.watch");
        boolean hasSystemFeature5 = context.getPackageManager().hasSystemFeature("org.chromium.arc");
        boolean hasSystemFeature6 = context.getPackageManager().hasSystemFeature("android.software.leanback");
        boolean hasSystemFeature7 = context.getPackageManager().hasSystemFeature("android.hardware.vr.high_performance");
        if (Build.IS_DEBUGGABLE && SystemProperties.getBoolean("debug.crash_system", false)) {
            throw new RuntimeException();
        }
        try {
            this.mZygotePreload = SystemServerInitThreadPool.submit(new Runnable() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    SystemServer.lambda$startOtherServices$1();
                }
            }, "SecondaryZygotePreload");
            timingsTraceAndSlog.traceBegin("StartKeyAttestationApplicationIdProviderService");
            ServiceManager.addService("sec_key_att_app_id_provider", new KeyAttestationApplicationIdProviderService(context));
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartKeyChainSystemService");
            this.mSystemServiceManager.startService(KeyChainSystemService.class);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartBinaryTransparencyService");
            this.mSystemServiceManager.startService(BinaryTransparencyService.class);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartSchedulingPolicyService");
            ServiceManager.addService("scheduling_policy", new SchedulingPolicyService());
            timingsTraceAndSlog.traceEnd();
            if (this.mPackageManager.hasSystemFeature("android.hardware.microphone") || this.mPackageManager.hasSystemFeature("android.software.telecom") || this.mPackageManager.hasSystemFeature("android.hardware.telephony")) {
                timingsTraceAndSlog.traceBegin("StartTelecomLoaderService");
                this.mSystemServiceManager.startService(TelecomLoaderService.class);
                timingsTraceAndSlog.traceEnd();
            }
            timingsTraceAndSlog.traceBegin("StartTelephonyRegistry");
            final ?? telephonyRegistry = new TelephonyRegistry(context, new TelephonyRegistry.ConfigurationProvider());
            ServiceManager.addService("telephony.registry", (IBinder) telephonyRegistry);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartEntropyMixer");
            this.mEntropyMixer = new EntropyMixer(context);
            timingsTraceAndSlog.traceEnd();
            this.mContentResolver = context.getContentResolver();
            timingsTraceAndSlog.traceBegin("StartAccountManagerService");
            this.mSystemServiceManager.startService(ACCOUNT_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartContentService");
            this.mSystemServiceManager.startService(CONTENT_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("InstallSystemProviders");
            this.mActivityManagerService.getContentProviderHelper().installSystemProviders();
            this.mSystemServiceManager.startService(UPDATABLE_DEVICE_CONFIG_SERVICE_CLASS);
            SQLiteCompatibilityWalFlags.reset();
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartDropBoxManager");
            this.mSystemServiceManager.startService(DropBoxManagerService.class);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartRoleManagerService");
            LocalManagerRegistry.addManager(RoleServicePlatformHelper.class, new RoleServicePlatformHelperImpl(this.mSystemContext));
            this.mSystemServiceManager.startService(ROLE_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartVibratorManagerService");
            this.mSystemServiceManager.startService(VibratorManagerService.Lifecycle.class);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartDynamicSystemService");
            ServiceManager.addService("dynamic_system", new DynamicSystemService(context));
            timingsTraceAndSlog.traceEnd();
            if (context.getPackageManager().hasSystemFeature("android.hardware.consumerir")) {
                timingsTraceAndSlog.traceBegin("StartConsumerIrService");
                ServiceManager.addService("consumer_ir", new ConsumerIrService(context));
                timingsTraceAndSlog.traceEnd();
            }
            timingsTraceAndSlog.traceBegin("StartResourceEconomy");
            this.mSystemServiceManager.startService(RESOURCE_ECONOMY_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            Slog.i(TAG, "AlarmManager Service");
            timingsTraceAndSlog.traceBegin("StartAlarmManagerService");
            this.mSystemServiceManager.startService(ALARM_MANAGER_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartInputManagerService");
            final InputManagerService inputManagerService = this.mSystemServerExt.getInputManagerService(context);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("DeviceStateManagerService");
            this.mSystemServiceManager.startService(DeviceStateManagerService.class);
            timingsTraceAndSlog.traceEnd();
            if (!z4) {
                timingsTraceAndSlog.traceBegin("StartCameraServiceProxy");
                this.mSystemServiceManager.startService(CameraServiceProxy.class);
                timingsTraceAndSlog.traceEnd();
            }
            timingsTraceAndSlog.traceBegin("StartWindowManagerService");
            this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, 200);
            WindowManagerService main = WindowManagerService.main(context, inputManagerService, !this.mFirstBoot, this.mSystemServerExt.getSubPhoneWindowManager(), this.mActivityManagerService.mActivityTaskManager);
            ServiceManager.addService("window", main, false, 17);
            ServiceManager.addService(IOplusSceneManager.APP_SCENE_DEFAULT_INPUT, inputManagerService, false, 1);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("SetWindowManagerService");
            this.mActivityManagerService.setWindowManager(main);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("WindowManagerServiceOnInitReady");
            main.onInitReady();
            timingsTraceAndSlog.traceEnd();
            SystemServerInitThreadPool.submit(new Runnable() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    SystemServer.lambda$startOtherServices$2();
                }
            }, START_SENSOR_MANAGER_SERVICE);
            SystemServerInitThreadPool.submit(new Runnable() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    SystemServer.lambda$startOtherServices$3();
                }
            }, START_HIDL_SERVICES);
            if (!hasSystemFeature4 && hasSystemFeature7) {
                timingsTraceAndSlog.traceBegin("StartVrManagerService");
                this.mSystemServiceManager.startService(VrManagerService.class);
                timingsTraceAndSlog.traceEnd();
            }
            timingsTraceAndSlog.traceBegin("StartInputManager");
            inputManagerService.setWindowManagerCallbacks(main.getInputManagerCallback());
            inputManagerService.start();
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("DisplayManagerWindowManagerAndInputReady");
            this.mDisplayManagerService.windowManagerAndInputReady();
            timingsTraceAndSlog.traceEnd();
            if (this.mFactoryTestMode == 1) {
                Slog.i(TAG, "No Bluetooth Service (factory test)");
            } else if (!context.getPackageManager().hasSystemFeature("android.hardware.bluetooth")) {
                Slog.i(TAG, "No Bluetooth Service (Bluetooth Hardware Not Present)");
            } else {
                timingsTraceAndSlog.traceBegin("StartBluetoothService");
                this.mSystemServiceManager.startService(BLUETOOTH_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
            }
            timingsTraceAndSlog.traceBegin("IpConnectivityMetrics");
            this.mSystemServiceManager.startService(IP_CONNECTIVITY_METRICS_CLASS);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("NetworkWatchlistService");
            this.mSystemServiceManager.startService(NetworkWatchlistService.Lifecycle.class);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("PinnerService");
            this.mSystemServiceManager.startService(PinnerService.class);
            timingsTraceAndSlog.traceEnd();
            this.mSocExt.startServiceForActivityTrigger(this.mSystemServiceManager);
            if (Build.IS_DEBUGGABLE && ProfcollectForwardingService.enabled()) {
                timingsTraceAndSlog.traceBegin("ProfcollectForwardingService");
                this.mSystemServiceManager.startService(ProfcollectForwardingService.class);
                timingsTraceAndSlog.traceEnd();
            }
            timingsTraceAndSlog.traceBegin("SignedConfigService");
            SignedConfigService.registerUpdateReceiver(this.mSystemContext);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("AppIntegrityService");
            this.mSystemServiceManager.startService(AppIntegrityManagerService.class);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartLogcatManager");
            this.mSystemServiceManager.startService(LogcatManagerService.class);
            timingsTraceAndSlog.traceEnd();
            final boolean detectSafeMode = main.detectSafeMode();
            if (detectSafeMode) {
                Settings.Global.putInt(context.getContentResolver(), "airplane_mode_on", 1);
            } else if (context.getResources().getBoolean(R.bool.config_bluetooth_pan_enable_autoconnect)) {
                Settings.Global.putInt(context.getContentResolver(), "airplane_mode_on", 0);
            }
            if (this.mFactoryTestMode != 1) {
                timingsTraceAndSlog.traceBegin("StartInputMethodManagerLifecycle");
                String string = context.getResources().getString(R.string.config_usbPermissionActivity);
                if (string.isEmpty()) {
                    this.mSystemServiceManager.startService(InputMethodManagerService.Lifecycle.class);
                    z = z5;
                } else {
                    try {
                        sb2 = new StringBuilder();
                        z = z5;
                    } catch (Throwable th) {
                        th = th;
                        z = z5;
                    }
                    try {
                        sb2.append("Starting custom IMMS: ");
                        sb2.append(string);
                        Slog.i(TAG, sb2.toString());
                        this.mSystemServiceManager.startService(string);
                    } catch (Throwable th2) {
                        th = th2;
                        reportWtf("starting " + string, th);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartAccessibilityManagerService");
                        this.mSystemServiceManager.startService(ACCESSIBILITY_MANAGER_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("MakeDisplayReady");
                        Slog.i(TAG, "displayReady");
                        main.displayReady();
                        timingsTraceAndSlog.traceEnd();
                        if (this.mFactoryTestMode != 1) {
                            timingsTraceAndSlog.traceBegin("StartStorageManagerService");
                            try {
                                this.mSystemServiceManager.startService(STORAGE_MANAGER_SERVICE_CLASS);
                                Slog.i(TAG, "mount service");
                                IStorageManager.Stub.asInterface(ServiceManager.getService("mount"));
                            } catch (Throwable th3) {
                                reportWtf("starting StorageManagerService", th3);
                            }
                            timingsTraceAndSlog.traceEnd();
                            timingsTraceAndSlog.traceBegin("StartStorageStatsService");
                            try {
                                this.mSystemServiceManager.startService(STORAGE_STATS_SERVICE_CLASS);
                            } catch (Throwable th4) {
                                reportWtf("starting StorageStatsService", th4);
                            }
                            timingsTraceAndSlog.traceEnd();
                        }
                        timingsTraceAndSlog.traceBegin("StartUiModeManager");
                        this.mSystemServiceManager.startService(UiModeManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartLocaleManagerService");
                        this.mSystemServiceManager.startService(LocaleManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartGrammarInflectionService");
                        this.mSystemServiceManager.startService(GrammaticalInflectionService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartAppHibernationService");
                        this.mSystemServiceManager.startService(APP_HIBERNATION_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("ArtManagerLocal");
                        DexOptHelper.initializeArtManagerLocal(context, this.mPackageManagerService);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("UpdatePackagesIfNeeded");
                        Watchdog.getInstance().pauseWatchingCurrentThread("dexopt");
                        this.mPackageManagerService.updatePackagesIfNeeded();
                        Watchdog.getInstance().resumeWatchingCurrentThread("dexopt");
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("PerformFstrimIfNeeded");
                        Slog.i(TAG, "performFstrimIfNeeded");
                        this.mPackageManagerService.performFstrimIfNeeded();
                        timingsTraceAndSlog.traceEnd();
                        if (this.mFactoryTestMode == 1) {
                        }
                        timingsTraceAndSlog.traceBegin("StartMediaProjectionManager");
                        this.mSystemServiceManager.startService(MediaProjectionManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        if (hasSystemFeature4) {
                        }
                        if (!this.mPackageManager.hasSystemFeature("android.software.slices_disabled")) {
                        }
                        if (context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartStatsCompanion");
                        this.mSystemServiceManager.startServiceFromJar(STATS_COMPANION_LIFECYCLE_CLASS, STATS_COMPANION_APEX_PATH);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartRebootReadinessManagerService");
                        this.mSystemServiceManager.startServiceFromJar(REBOOT_READINESS_LIFECYCLE_CLASS, SCHEDULING_APEX_PATH);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartStatsPullAtomService");
                        this.mSystemServiceManager.startService(STATS_PULL_ATOM_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StatsBootstrapAtomService");
                        this.mSystemServiceManager.startService(STATS_BOOTSTRAP_ATOM_SERVICE_LIFECYCLE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartIncidentCompanionService");
                        this.mSystemServiceManager.startService(IncidentCompanionService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StarSdkSandboxManagerService");
                        this.mSystemServiceManager.startService(SDK_SANDBOX_MANAGER_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartAdServicesManagerService");
                        startService = this.mSystemServiceManager.startService(AD_SERVICES_MANAGER_SERVICE_CLASS);
                        if (startService instanceof Dumpable) {
                        }
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartOnDevicePersonalizationSystemService");
                        this.mSystemServiceManager.startService(ON_DEVICE_PERSONALIZATION_SYSTEM_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        if (detectSafeMode) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.hardware.telephony")) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.software.autofill")) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.software.credentials")) {
                        }
                        if (deviceHasConfigString(context, R.string.config_screenshotErrorReceiverComponent)) {
                        }
                        timingsTraceAndSlog.traceBegin("StartSelectionToolbarManagerService");
                        this.mSystemServiceManager.startService(SELECTION_TOOLBAR_MANAGER_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartClipboardService");
                        this.mSystemServiceManager.startService(ClipboardService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("AppServiceManager");
                        this.mSystemServiceManager.startService(AppBindingService.Lifecycle.class);
                        timingsTraceAndSlog.traceEnd();
                        this.mSystemServerExt.startOtherServices();
                        this.mSocExt.startOtherServices();
                        timingsTraceAndSlog.traceBegin("startTracingServiceProxy");
                        this.mSystemServiceManager.startService(TracingServiceProxy.class);
                        timingsTraceAndSlog.traceEnd();
                        this.mSystemServerExt.linearVibratorSystemReady();
                        timingsTraceAndSlog.traceBegin("MakeLockSettingsServiceReady");
                        if (iLockSettings3 != null) {
                        }
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartBootPhaseLockSettingsReady");
                        this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, SystemService.PHASE_LOCK_SETTINGS_READY);
                        timingsTraceAndSlog.traceEnd();
                        createInstance = HsumBootUserInitializer.createInstance(this.mActivityManagerService, this.mPackageManagerService, this.mContentResolver, context.getResources().getBoolean(17891723));
                        if (createInstance != null) {
                        }
                        timingsTraceAndSlog.traceBegin("StartBootPhaseSystemServicesReady");
                        this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, 500);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("MakeWindowManagerServiceReady");
                        Slog.i(TAG, "wms systemReady");
                        main.systemReady();
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("RegisterLogMteState");
                        LogMteState.register(context);
                        timingsTraceAndSlog.traceEnd();
                        synchronized (SystemService.class) {
                        }
                    }
                }
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartAccessibilityManagerService");
                try {
                    this.mSystemServiceManager.startService(ACCESSIBILITY_MANAGER_SERVICE_CLASS);
                } catch (Throwable th5) {
                    reportWtf("starting Accessibility Manager", th5);
                }
                timingsTraceAndSlog.traceEnd();
            } else {
                z = z5;
            }
            timingsTraceAndSlog.traceBegin("MakeDisplayReady");
            try {
                Slog.i(TAG, "displayReady");
                main.displayReady();
            } catch (Throwable th6) {
                reportWtf("making display ready", th6);
            }
            timingsTraceAndSlog.traceEnd();
            if (this.mFactoryTestMode != 1 && !"0".equals(SystemProperties.get("system_init.startmountservice"))) {
                timingsTraceAndSlog.traceBegin("StartStorageManagerService");
                this.mSystemServiceManager.startService(STORAGE_MANAGER_SERVICE_CLASS);
                Slog.i(TAG, "mount service");
                IStorageManager.Stub.asInterface(ServiceManager.getService("mount"));
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartStorageStatsService");
                this.mSystemServiceManager.startService(STORAGE_STATS_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
            }
            timingsTraceAndSlog.traceBegin("StartUiModeManager");
            this.mSystemServiceManager.startService(UiModeManagerService.class);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartLocaleManagerService");
            try {
                this.mSystemServiceManager.startService(LocaleManagerService.class);
            } catch (Throwable th7) {
                reportWtf("starting LocaleManagerService service", th7);
            }
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartGrammarInflectionService");
            try {
                this.mSystemServiceManager.startService(GrammaticalInflectionService.class);
            } catch (Throwable th8) {
                reportWtf("starting GrammarInflectionService service", th8);
            }
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartAppHibernationService");
            this.mSystemServiceManager.startService(APP_HIBERNATION_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("ArtManagerLocal");
            DexOptHelper.initializeArtManagerLocal(context, this.mPackageManagerService);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("UpdatePackagesIfNeeded");
            try {
                Watchdog.getInstance().pauseWatchingCurrentThread("dexopt");
                this.mPackageManagerService.updatePackagesIfNeeded();
            } finally {
                try {
                    Watchdog.getInstance().resumeWatchingCurrentThread("dexopt");
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("PerformFstrimIfNeeded");
                    Slog.i(TAG, "performFstrimIfNeeded");
                    this.mPackageManagerService.performFstrimIfNeeded();
                    timingsTraceAndSlog.traceEnd();
                    if (this.mFactoryTestMode == 1) {
                    }
                    timingsTraceAndSlog.traceBegin("StartMediaProjectionManager");
                    this.mSystemServiceManager.startService(MediaProjectionManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (hasSystemFeature4) {
                    }
                    if (!this.mPackageManager.hasSystemFeature("android.software.slices_disabled")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartStatsCompanion");
                    this.mSystemServiceManager.startServiceFromJar(STATS_COMPANION_LIFECYCLE_CLASS, STATS_COMPANION_APEX_PATH);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartRebootReadinessManagerService");
                    this.mSystemServiceManager.startServiceFromJar(REBOOT_READINESS_LIFECYCLE_CLASS, SCHEDULING_APEX_PATH);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartStatsPullAtomService");
                    this.mSystemServiceManager.startService(STATS_PULL_ATOM_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StatsBootstrapAtomService");
                    this.mSystemServiceManager.startService(STATS_BOOTSTRAP_ATOM_SERVICE_LIFECYCLE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartIncidentCompanionService");
                    this.mSystemServiceManager.startService(IncidentCompanionService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StarSdkSandboxManagerService");
                    this.mSystemServiceManager.startService(SDK_SANDBOX_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartAdServicesManagerService");
                    startService = this.mSystemServiceManager.startService(AD_SERVICES_MANAGER_SERVICE_CLASS);
                    if (startService instanceof Dumpable) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartOnDevicePersonalizationSystemService");
                    this.mSystemServiceManager.startService(ON_DEVICE_PERSONALIZATION_SYSTEM_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (detectSafeMode) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.hardware.telephony")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.autofill")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.credentials")) {
                    }
                    if (deviceHasConfigString(context, R.string.config_screenshotErrorReceiverComponent)) {
                    }
                    timingsTraceAndSlog.traceBegin("StartSelectionToolbarManagerService");
                    this.mSystemServiceManager.startService(SELECTION_TOOLBAR_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartClipboardService");
                    this.mSystemServiceManager.startService(ClipboardService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("AppServiceManager");
                    this.mSystemServiceManager.startService(AppBindingService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.startOtherServices();
                    this.mSocExt.startOtherServices();
                    timingsTraceAndSlog.traceBegin("startTracingServiceProxy");
                    this.mSystemServiceManager.startService(TracingServiceProxy.class);
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.linearVibratorSystemReady();
                    timingsTraceAndSlog.traceBegin("MakeLockSettingsServiceReady");
                    if (iLockSettings3 != null) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartBootPhaseLockSettingsReady");
                    this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, SystemService.PHASE_LOCK_SETTINGS_READY);
                    timingsTraceAndSlog.traceEnd();
                    createInstance = HsumBootUserInitializer.createInstance(this.mActivityManagerService, this.mPackageManagerService, this.mContentResolver, context.getResources().getBoolean(17891723));
                    if (createInstance != null) {
                    }
                    timingsTraceAndSlog.traceBegin("StartBootPhaseSystemServicesReady");
                    this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, 500);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("MakeWindowManagerServiceReady");
                    Slog.i(TAG, "wms systemReady");
                    main.systemReady();
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("RegisterLogMteState");
                    LogMteState.register(context);
                    timingsTraceAndSlog.traceEnd();
                    synchronized (SystemService.class) {
                    }
                } catch (Throwable th9) {
                }
            }
            Watchdog.getInstance().resumeWatchingCurrentThread("dexopt");
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("PerformFstrimIfNeeded");
            Slog.i(TAG, "performFstrimIfNeeded");
            this.mPackageManagerService.performFstrimIfNeeded();
            timingsTraceAndSlog.traceEnd();
            if (this.mFactoryTestMode == 1) {
                lifecycle2 = null;
                iLockSettings3 = null;
                vpnManagerService = null;
                vcnManagementService = null;
                networkTimeUpdateService2 = null;
                networkManagementService2 = null;
                r23 = null;
                networkPolicyManagerService2 = null;
                mediaRouterService = null;
            } else {
                timingsTraceAndSlog.traceBegin("StartLockSettingsService");
                try {
                    this.mSystemServiceManager.startService(LOCK_SETTINGS_SERVICE_CLASS);
                    iLockSettings = ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings"));
                } catch (Throwable th10) {
                    reportWtf("starting LockSettingsService service", th10);
                    iLockSettings = null;
                }
                timingsTraceAndSlog.traceEnd();
                boolean z6 = !SystemProperties.get(PERSISTENT_DATA_BLOCK_PROP).equals("");
                if (z6) {
                    timingsTraceAndSlog.traceBegin("StartPersistentDataBlock");
                    this.mSystemServiceManager.startService(PersistentDataBlockService.class);
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartTestHarnessMode");
                this.mSystemServiceManager.startService(TestHarnessModeService.class);
                timingsTraceAndSlog.traceEnd();
                if (z6 || OemLockService.isHalPresent()) {
                    timingsTraceAndSlog.traceBegin("StartOemLockService");
                    this.mSystemServiceManager.startService(OemLockService.class);
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartDeviceIdleController");
                this.mSystemServiceManager.startService(DEVICE_IDLE_CONTROLLER_CLASS);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartDevicePolicyManager");
                DevicePolicyManagerService.Lifecycle lifecycle3 = (DevicePolicyManagerService.Lifecycle) this.mSystemServiceManager.startService(DevicePolicyManagerService.Lifecycle.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartStatusBarManagerService");
                try {
                    statusBarManagerService = new StatusBarManagerService(context);
                    if (!hasSystemFeature4) {
                        statusBarManagerService.publishGlobalActionsProvider();
                    }
                    lifecycle = lifecycle3;
                    iLockSettings2 = iLockSettings;
                } catch (Throwable th11) {
                    th = th11;
                    lifecycle = lifecycle3;
                    iLockSettings2 = iLockSettings;
                }
                try {
                    ServiceManager.addService("statusbar", statusBarManagerService, false, 20);
                } catch (Throwable th12) {
                    th = th12;
                    reportWtf("starting StatusBarManagerService", th);
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.addOplusTileManagerService(context);
                    if (!deviceHasConfigString(context, R.string.config_packagedKeyboardName)) {
                    }
                    startContentCaptureService(context, timingsTraceAndSlog);
                    startAttentionService(context, timingsTraceAndSlog);
                    startRotationResolverService(context, timingsTraceAndSlog);
                    startSystemCaptionsManagerService(context, timingsTraceAndSlog);
                    startTextToSpeechManagerService(context, timingsTraceAndSlog);
                    startAmbientContextService(timingsTraceAndSlog);
                    startWearableSensingService(timingsTraceAndSlog);
                    timingsTraceAndSlog.traceBegin("StartSpeechRecognitionManagerService");
                    this.mSystemServiceManager.startService(SPEECH_RECOGNITION_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (!deviceHasConfigString(context, R.string.config_mainBuiltInDisplayCutout)) {
                    }
                    if (!deviceHasConfigString(context, R.string.config_mobile_hotspot_provision_app_no_ui)) {
                    }
                    timingsTraceAndSlog.traceBegin("StartSearchUiService");
                    this.mSystemServiceManager.startService(SEARCH_UI_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartSmartspaceService");
                    this.mSystemServiceManager.startService(SMARTSPACE_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("InitConnectivityModuleConnector");
                    ConnectivityModuleConnector.getInstance().init(context);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("InitNetworkStackClient");
                    NetworkStackClient.getInstance().init();
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartNetworkManagementService");
                    networkManagementService = NetworkManagementService.create(context);
                    ServiceManager.addService("network_management", networkManagementService);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartFontManagerService");
                    this.mSystemServiceManager.startService(new FontManagerService.Lifecycle(context, detectSafeMode));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartTextServicesManager");
                    this.mSystemServiceManager.startService(TextServicesManagerService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    if (!z2) {
                    }
                    timingsTraceAndSlog.traceBegin("StartNetworkScoreService");
                    this.mSystemServiceManager.startService(NetworkScoreService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartNetworkStatsService");
                    this.mSystemServiceManager.startServiceFromJar(NETWORK_STATS_SERVICE_INITIALIZER_CLASS, CONNECTIVITY_SERVICE_APEX_PATH);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartNetworkPolicyManagerService");
                    networkPolicyManagerService = new NetworkPolicyManagerService(context, this.mActivityManagerService, networkManagementService);
                    ServiceManager.addService("netpolicy", networkPolicyManagerService);
                    timingsTraceAndSlog.traceEnd();
                    if (context.getPackageManager().hasSystemFeature("android.hardware.wifi")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.wifi.rtt")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.wifi.aware")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.wifi.direct")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.lowpan")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartPacProxyService");
                    ServiceManager.addService("pac_proxy", new PacProxyService(context));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartConnectivityService");
                    this.mSystemServiceManager.startServiceFromJar(CONNECTIVITY_SERVICE_INITIALIZER_CLASS, CONNECTIVITY_SERVICE_APEX_PATH);
                    networkPolicyManagerService.bindConnectivityManager();
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartVpnManagerService");
                    ?? r5 = VpnManagerService.create(context);
                    ServiceManager.addService("vpn_management", (IBinder) r5);
                    VpnManagerService vpnManagerService2 = r5;
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartVcnManagementService");
                    ?? create = VcnManagementService.create(context);
                    ServiceManager.addService("vcn_management", (IBinder) create);
                    networkManagementService2 = networkManagementService;
                    VcnManagementService vcnManagementService2 = create;
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartSystemUpdateManagerService");
                    ServiceManager.addService("system_update", new SystemUpdateManagerService(context));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartUpdateLockService");
                    ServiceManager.addService("updatelock", new UpdateLockService(context));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartNotificationManager");
                    this.mSystemServiceManager.startService(NotificationManagerService.class);
                    SystemNotificationChannels.removeDeprecated(context);
                    SystemNotificationChannels.createAll(context);
                    INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartDeviceMonitor");
                    this.mSystemServiceManager.startService(DeviceStorageMonitorService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartTimeDetectorService");
                    this.mSystemServiceManager.startService(TIME_DETECTOR_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartLocationManagerService");
                    this.mSystemServiceManager.startService(LocationManagerService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartCountryDetectorService");
                    ICountryDetector.Stub countryDetectorService = new CountryDetectorService(context);
                    ServiceManager.addService("country_detector", countryDetectorService);
                    Object obj = countryDetectorService;
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartTimeZoneDetectorService");
                    r23 = obj;
                    this.mSystemServiceManager.startService(TIME_ZONE_DETECTOR_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartAltitudeService");
                    this.mSystemServiceManager.startService(AltitudeService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartLocationTimeZoneManagerService");
                    this.mSystemServiceManager.startService(LOCATION_TIME_ZONE_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (context.getResources().getBoolean(17891664)) {
                    }
                    if (!hasSystemFeature4) {
                    }
                    this.mSystemServerExt.writeAgingCriticalEvent();
                    if (!context.getResources().getBoolean(17891682)) {
                    }
                    timingsTraceAndSlog.traceBegin("StartWallpaperEffectsGenerationService");
                    this.mSystemServiceManager.startService(WALLPAPER_EFFECTS_GENERATION_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartAudioService");
                    if (hasSystemFeature5) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartSoundTriggerMiddlewareService");
                    this.mSystemServiceManager.startService(SoundTriggerMiddlewareService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.hardware.broadcastradio")) {
                    }
                    if (!hasSystemFeature6) {
                    }
                    if (hasSystemFeature4) {
                    }
                    if (!hasSystemFeature4) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.midi")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartAdbService");
                    this.mSystemServiceManager.startService(ADB_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (!this.mPackageManager.hasSystemFeature("android.hardware.usb.host")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartUsbService");
                    this.mSystemServiceManager.startService(USB_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (!hasSystemFeature4) {
                    }
                    timingsTraceAndSlog.traceBegin("StartHardwarePropertiesManagerService");
                    ServiceManager.addService("hardware_properties", new HardwarePropertiesManagerService(context));
                    timingsTraceAndSlog.traceEnd();
                    if (!hasSystemFeature4) {
                    }
                    timingsTraceAndSlog.traceBegin("StartColorDisplay");
                    this.mSystemServiceManager.startService(ColorDisplayService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartJobScheduler");
                    if (!this.mSystemServerExt.startJobSchedulerService()) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartSoundTrigger");
                    this.mSystemServiceManager.startService(SoundTriggerService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartTrustManager");
                    this.mSystemServiceManager.startService(TrustManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.software.backup")) {
                    }
                    if (!this.mPackageManager.hasSystemFeature("android.software.app_widgets")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartAppWidgetService");
                    this.mSystemServiceManager.startService(APPWIDGET_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartVoiceRecognitionManager");
                    this.mSystemServiceManager.startService(VOICE_RECOGNITION_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (GestureLauncherService.isGestureLauncherEnabled(context.getResources())) {
                    }
                    timingsTraceAndSlog.traceBegin("StartSensorNotification");
                    this.mSystemServiceManager.startService(SensorNotificationService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.hardware.context_hub")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartDiskStatsService");
                    ServiceManager.addService("diskstats", new DiskStatsService(context));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("RuntimeService");
                    ServiceManager.addService("runtime", new RuntimeService(context));
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.addCabcService(context, timingsTraceAndSlog);
                    timingsTraceAndSlog.traceBegin("RenderAcceleratingService");
                    this.mSystemServerExt.addRenderAcceleratingService(context, timingsTraceAndSlog);
                    timingsTraceAndSlog.traceEnd();
                    if (hasSystemFeature4) {
                    }
                    networkTimeUpdateService = null;
                    timingsTraceAndSlog.traceBegin("CertBlacklister");
                    new CertBlacklister(context);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartEmergencyAffordanceService");
                    this.mSystemServiceManager.startService(EmergencyAffordanceService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin(START_BLOB_STORE_SERVICE);
                    this.mSystemServiceManager.startService(BLOB_STORE_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartDreamManager");
                    this.mSystemServiceManager.startService(DreamManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("AddGraphicsStatsService");
                    ServiceManager.addService("graphicsstats", new GraphicsStatsService(context));
                    timingsTraceAndSlog.traceEnd();
                    if (CoverageService.ENABLED) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.print")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartAttestationVerificationService");
                    this.mSystemServiceManager.startService(AttestationVerificationManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.software.companion_device_setup")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartRestrictionManager");
                    this.mSystemServiceManager.startService(RestrictionsManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartMediaSessionService");
                    Slog.i(TAG, "Media Session Service");
                    this.mSystemServiceManager.startService(MEDIA_SESSION_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.hardware.hdmi.cec")) {
                    }
                    if (!this.mPackageManager.hasSystemFeature("android.software.live_tv")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartTvInteractiveAppManager");
                    this.mSystemServiceManager.startService(TvInteractiveAppManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (!this.mPackageManager.hasSystemFeature("android.software.live_tv")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartTvInputManager");
                    this.mSystemServiceManager.startService(TvInputManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.hardware.tv.tuner")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.picture_in_picture")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.leanback")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartMediaRouterService");
                    MediaRouterService mediaRouterService2 = new MediaRouterService(context);
                    ServiceManager.addService("media_router", mediaRouterService2);
                    timingsTraceAndSlog.traceEnd();
                    hasSystemFeature = this.mPackageManager.hasSystemFeature("android.hardware.biometrics.face");
                    hasSystemFeature2 = this.mPackageManager.hasSystemFeature("android.hardware.biometrics.iris");
                    hasSystemFeature3 = this.mPackageManager.hasSystemFeature("android.hardware.fingerprint");
                    if (hasSystemFeature) {
                    }
                    if (hasSystemFeature2) {
                    }
                    if (hasSystemFeature3) {
                    }
                    timingsTraceAndSlog.traceBegin("StartBiometricService");
                    this.mSystemServiceManager.startService(BiometricService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartAuthService");
                    this.mSystemServiceManager.startService(AuthService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (!hasSystemFeature4) {
                    }
                    if (!hasSystemFeature4) {
                    }
                    timingsTraceAndSlog.traceBegin("StartShortcutServiceLifecycle");
                    this.mSystemServiceManager.startService(ShortcutService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartLauncherAppsService");
                    this.mSystemServiceManager.startService(LauncherAppsService.class);
                    timingsTraceAndSlog.traceEnd();
                    Slog.i(TAG, "Dynamic filter service");
                    this.mSystemServerExt.startDynamicFilterService(this.mSystemServiceManager);
                    timingsTraceAndSlog.traceBegin("StartCrossProfileAppsService");
                    this.mSystemServiceManager.startService(CrossProfileAppsService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartPeopleService");
                    this.mSystemServiceManager.startService(PeopleService.class);
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.addLinearmotorVibratorService(context);
                    this.mSystemServerExt.addStorageHealthInfoService(context);
                    timingsTraceAndSlog.traceBegin("StartMediaMetricsManager");
                    this.mSystemServiceManager.startService(MediaMetricsManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartBackgroundInstallControlService");
                    this.mSystemServiceManager.startService(BackgroundInstallControlService.class);
                    timingsTraceAndSlog.traceEnd();
                    mediaRouterService = mediaRouterService2;
                    lifecycle2 = lifecycle;
                    vcnManagementService = vcnManagementService2;
                    vpnManagerService = vpnManagerService2;
                    iLockSettings3 = iLockSettings2;
                    networkTimeUpdateService2 = networkTimeUpdateService;
                    timingsTraceAndSlog.traceBegin("StartMediaProjectionManager");
                    this.mSystemServiceManager.startService(MediaProjectionManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (hasSystemFeature4) {
                    }
                    if (!this.mPackageManager.hasSystemFeature("android.software.slices_disabled")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartStatsCompanion");
                    this.mSystemServiceManager.startServiceFromJar(STATS_COMPANION_LIFECYCLE_CLASS, STATS_COMPANION_APEX_PATH);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartRebootReadinessManagerService");
                    this.mSystemServiceManager.startServiceFromJar(REBOOT_READINESS_LIFECYCLE_CLASS, SCHEDULING_APEX_PATH);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartStatsPullAtomService");
                    this.mSystemServiceManager.startService(STATS_PULL_ATOM_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StatsBootstrapAtomService");
                    this.mSystemServiceManager.startService(STATS_BOOTSTRAP_ATOM_SERVICE_LIFECYCLE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartIncidentCompanionService");
                    this.mSystemServiceManager.startService(IncidentCompanionService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StarSdkSandboxManagerService");
                    this.mSystemServiceManager.startService(SDK_SANDBOX_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartAdServicesManagerService");
                    startService = this.mSystemServiceManager.startService(AD_SERVICES_MANAGER_SERVICE_CLASS);
                    if (startService instanceof Dumpable) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartOnDevicePersonalizationSystemService");
                    this.mSystemServiceManager.startService(ON_DEVICE_PERSONALIZATION_SYSTEM_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (detectSafeMode) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.hardware.telephony")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.autofill")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.credentials")) {
                    }
                    if (deviceHasConfigString(context, R.string.config_screenshotErrorReceiverComponent)) {
                    }
                    timingsTraceAndSlog.traceBegin("StartSelectionToolbarManagerService");
                    this.mSystemServiceManager.startService(SELECTION_TOOLBAR_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartClipboardService");
                    this.mSystemServiceManager.startService(ClipboardService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("AppServiceManager");
                    this.mSystemServiceManager.startService(AppBindingService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.startOtherServices();
                    this.mSocExt.startOtherServices();
                    timingsTraceAndSlog.traceBegin("startTracingServiceProxy");
                    this.mSystemServiceManager.startService(TracingServiceProxy.class);
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.linearVibratorSystemReady();
                    timingsTraceAndSlog.traceBegin("MakeLockSettingsServiceReady");
                    if (iLockSettings3 != null) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartBootPhaseLockSettingsReady");
                    this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, SystemService.PHASE_LOCK_SETTINGS_READY);
                    timingsTraceAndSlog.traceEnd();
                    createInstance = HsumBootUserInitializer.createInstance(this.mActivityManagerService, this.mPackageManagerService, this.mContentResolver, context.getResources().getBoolean(17891723));
                    if (createInstance != null) {
                    }
                    timingsTraceAndSlog.traceBegin("StartBootPhaseSystemServicesReady");
                    this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, 500);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("MakeWindowManagerServiceReady");
                    Slog.i(TAG, "wms systemReady");
                    main.systemReady();
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("RegisterLogMteState");
                    LogMteState.register(context);
                    timingsTraceAndSlog.traceEnd();
                    synchronized (SystemService.class) {
                    }
                }
                timingsTraceAndSlog.traceEnd();
                this.mSystemServerExt.addOplusTileManagerService(context);
                if (!deviceHasConfigString(context, R.string.config_packagedKeyboardName)) {
                    timingsTraceAndSlog.traceBegin("StartMusicRecognitionManagerService");
                    this.mSystemServiceManager.startService(MUSIC_RECOGNITION_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                } else {
                    Slog.d(TAG, "MusicRecognitionManagerService not defined by OEM or disabled by flag");
                }
                startContentCaptureService(context, timingsTraceAndSlog);
                startAttentionService(context, timingsTraceAndSlog);
                startRotationResolverService(context, timingsTraceAndSlog);
                startSystemCaptionsManagerService(context, timingsTraceAndSlog);
                startTextToSpeechManagerService(context, timingsTraceAndSlog);
                startAmbientContextService(timingsTraceAndSlog);
                startWearableSensingService(timingsTraceAndSlog);
                timingsTraceAndSlog.traceBegin("StartSpeechRecognitionManagerService");
                this.mSystemServiceManager.startService(SPEECH_RECOGNITION_MANAGER_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                if (!deviceHasConfigString(context, R.string.config_mainBuiltInDisplayCutout)) {
                    timingsTraceAndSlog.traceBegin("StartAppPredictionService");
                    this.mSystemServiceManager.startService(APP_PREDICTION_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                } else {
                    Slog.d(TAG, "AppPredictionService not defined by OEM");
                }
                if (!deviceHasConfigString(context, R.string.config_mobile_hotspot_provision_app_no_ui)) {
                    timingsTraceAndSlog.traceBegin("StartContentSuggestionsService");
                    this.mSystemServiceManager.startService(CONTENT_SUGGESTIONS_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                } else {
                    Slog.d(TAG, "ContentSuggestionsService not defined by OEM");
                }
                timingsTraceAndSlog.traceBegin("StartSearchUiService");
                this.mSystemServiceManager.startService(SEARCH_UI_MANAGER_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartSmartspaceService");
                this.mSystemServiceManager.startService(SMARTSPACE_MANAGER_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("InitConnectivityModuleConnector");
                ConnectivityModuleConnector.getInstance().init(context);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("InitNetworkStackClient");
                NetworkStackClient.getInstance().init();
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartNetworkManagementService");
                try {
                    networkManagementService = NetworkManagementService.create(context);
                } catch (Throwable th13) {
                    th = th13;
                    networkManagementService = null;
                }
                try {
                    ServiceManager.addService("network_management", networkManagementService);
                } catch (Throwable th14) {
                    th = th14;
                    reportWtf("starting NetworkManagement Service", th);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartFontManagerService");
                    this.mSystemServiceManager.startService(new FontManagerService.Lifecycle(context, detectSafeMode));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartTextServicesManager");
                    this.mSystemServiceManager.startService(TextServicesManagerService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    if (!z2) {
                    }
                    timingsTraceAndSlog.traceBegin("StartNetworkScoreService");
                    this.mSystemServiceManager.startService(NetworkScoreService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartNetworkStatsService");
                    this.mSystemServiceManager.startServiceFromJar(NETWORK_STATS_SERVICE_INITIALIZER_CLASS, CONNECTIVITY_SERVICE_APEX_PATH);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartNetworkPolicyManagerService");
                    networkPolicyManagerService = new NetworkPolicyManagerService(context, this.mActivityManagerService, networkManagementService);
                    ServiceManager.addService("netpolicy", networkPolicyManagerService);
                    timingsTraceAndSlog.traceEnd();
                    if (context.getPackageManager().hasSystemFeature("android.hardware.wifi")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.wifi.rtt")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.wifi.aware")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.wifi.direct")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.lowpan")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartPacProxyService");
                    ServiceManager.addService("pac_proxy", new PacProxyService(context));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartConnectivityService");
                    this.mSystemServiceManager.startServiceFromJar(CONNECTIVITY_SERVICE_INITIALIZER_CLASS, CONNECTIVITY_SERVICE_APEX_PATH);
                    networkPolicyManagerService.bindConnectivityManager();
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartVpnManagerService");
                    ?? r52 = VpnManagerService.create(context);
                    ServiceManager.addService("vpn_management", (IBinder) r52);
                    VpnManagerService vpnManagerService22 = r52;
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartVcnManagementService");
                    ?? create2 = VcnManagementService.create(context);
                    ServiceManager.addService("vcn_management", (IBinder) create2);
                    networkManagementService2 = networkManagementService;
                    VcnManagementService vcnManagementService22 = create2;
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartSystemUpdateManagerService");
                    ServiceManager.addService("system_update", new SystemUpdateManagerService(context));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartUpdateLockService");
                    ServiceManager.addService("updatelock", new UpdateLockService(context));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartNotificationManager");
                    this.mSystemServiceManager.startService(NotificationManagerService.class);
                    SystemNotificationChannels.removeDeprecated(context);
                    SystemNotificationChannels.createAll(context);
                    INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartDeviceMonitor");
                    this.mSystemServiceManager.startService(DeviceStorageMonitorService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartTimeDetectorService");
                    this.mSystemServiceManager.startService(TIME_DETECTOR_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartLocationManagerService");
                    this.mSystemServiceManager.startService(LocationManagerService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartCountryDetectorService");
                    ICountryDetector.Stub countryDetectorService2 = new CountryDetectorService(context);
                    ServiceManager.addService("country_detector", countryDetectorService2);
                    Object obj2 = countryDetectorService2;
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartTimeZoneDetectorService");
                    r23 = obj2;
                    this.mSystemServiceManager.startService(TIME_ZONE_DETECTOR_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartAltitudeService");
                    this.mSystemServiceManager.startService(AltitudeService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartLocationTimeZoneManagerService");
                    this.mSystemServiceManager.startService(LOCATION_TIME_ZONE_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (context.getResources().getBoolean(17891664)) {
                    }
                    if (!hasSystemFeature4) {
                    }
                    this.mSystemServerExt.writeAgingCriticalEvent();
                    if (!context.getResources().getBoolean(17891682)) {
                    }
                    timingsTraceAndSlog.traceBegin("StartWallpaperEffectsGenerationService");
                    this.mSystemServiceManager.startService(WALLPAPER_EFFECTS_GENERATION_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartAudioService");
                    if (hasSystemFeature5) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartSoundTriggerMiddlewareService");
                    this.mSystemServiceManager.startService(SoundTriggerMiddlewareService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.hardware.broadcastradio")) {
                    }
                    if (!hasSystemFeature6) {
                    }
                    if (hasSystemFeature4) {
                    }
                    if (!hasSystemFeature4) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.midi")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartAdbService");
                    this.mSystemServiceManager.startService(ADB_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (!this.mPackageManager.hasSystemFeature("android.hardware.usb.host")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartUsbService");
                    this.mSystemServiceManager.startService(USB_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (!hasSystemFeature4) {
                    }
                    timingsTraceAndSlog.traceBegin("StartHardwarePropertiesManagerService");
                    ServiceManager.addService("hardware_properties", new HardwarePropertiesManagerService(context));
                    timingsTraceAndSlog.traceEnd();
                    if (!hasSystemFeature4) {
                    }
                    timingsTraceAndSlog.traceBegin("StartColorDisplay");
                    this.mSystemServiceManager.startService(ColorDisplayService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartJobScheduler");
                    if (!this.mSystemServerExt.startJobSchedulerService()) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartSoundTrigger");
                    this.mSystemServiceManager.startService(SoundTriggerService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartTrustManager");
                    this.mSystemServiceManager.startService(TrustManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.software.backup")) {
                    }
                    if (!this.mPackageManager.hasSystemFeature("android.software.app_widgets")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartAppWidgetService");
                    this.mSystemServiceManager.startService(APPWIDGET_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartVoiceRecognitionManager");
                    this.mSystemServiceManager.startService(VOICE_RECOGNITION_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (GestureLauncherService.isGestureLauncherEnabled(context.getResources())) {
                    }
                    timingsTraceAndSlog.traceBegin("StartSensorNotification");
                    this.mSystemServiceManager.startService(SensorNotificationService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.hardware.context_hub")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartDiskStatsService");
                    ServiceManager.addService("diskstats", new DiskStatsService(context));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("RuntimeService");
                    ServiceManager.addService("runtime", new RuntimeService(context));
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.addCabcService(context, timingsTraceAndSlog);
                    timingsTraceAndSlog.traceBegin("RenderAcceleratingService");
                    this.mSystemServerExt.addRenderAcceleratingService(context, timingsTraceAndSlog);
                    timingsTraceAndSlog.traceEnd();
                    if (hasSystemFeature4) {
                    }
                    networkTimeUpdateService = null;
                    timingsTraceAndSlog.traceBegin("CertBlacklister");
                    new CertBlacklister(context);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartEmergencyAffordanceService");
                    this.mSystemServiceManager.startService(EmergencyAffordanceService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin(START_BLOB_STORE_SERVICE);
                    this.mSystemServiceManager.startService(BLOB_STORE_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartDreamManager");
                    this.mSystemServiceManager.startService(DreamManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("AddGraphicsStatsService");
                    ServiceManager.addService("graphicsstats", new GraphicsStatsService(context));
                    timingsTraceAndSlog.traceEnd();
                    if (CoverageService.ENABLED) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.print")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartAttestationVerificationService");
                    this.mSystemServiceManager.startService(AttestationVerificationManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.software.companion_device_setup")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartRestrictionManager");
                    this.mSystemServiceManager.startService(RestrictionsManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartMediaSessionService");
                    Slog.i(TAG, "Media Session Service");
                    this.mSystemServiceManager.startService(MEDIA_SESSION_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.hardware.hdmi.cec")) {
                    }
                    if (!this.mPackageManager.hasSystemFeature("android.software.live_tv")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartTvInteractiveAppManager");
                    this.mSystemServiceManager.startService(TvInteractiveAppManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (!this.mPackageManager.hasSystemFeature("android.software.live_tv")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartTvInputManager");
                    this.mSystemServiceManager.startService(TvInputManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.hardware.tv.tuner")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.picture_in_picture")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.leanback")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartMediaRouterService");
                    MediaRouterService mediaRouterService22 = new MediaRouterService(context);
                    ServiceManager.addService("media_router", mediaRouterService22);
                    timingsTraceAndSlog.traceEnd();
                    hasSystemFeature = this.mPackageManager.hasSystemFeature("android.hardware.biometrics.face");
                    hasSystemFeature2 = this.mPackageManager.hasSystemFeature("android.hardware.biometrics.iris");
                    hasSystemFeature3 = this.mPackageManager.hasSystemFeature("android.hardware.fingerprint");
                    if (hasSystemFeature) {
                    }
                    if (hasSystemFeature2) {
                    }
                    if (hasSystemFeature3) {
                    }
                    timingsTraceAndSlog.traceBegin("StartBiometricService");
                    this.mSystemServiceManager.startService(BiometricService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartAuthService");
                    this.mSystemServiceManager.startService(AuthService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (!hasSystemFeature4) {
                    }
                    if (!hasSystemFeature4) {
                    }
                    timingsTraceAndSlog.traceBegin("StartShortcutServiceLifecycle");
                    this.mSystemServiceManager.startService(ShortcutService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartLauncherAppsService");
                    this.mSystemServiceManager.startService(LauncherAppsService.class);
                    timingsTraceAndSlog.traceEnd();
                    Slog.i(TAG, "Dynamic filter service");
                    this.mSystemServerExt.startDynamicFilterService(this.mSystemServiceManager);
                    timingsTraceAndSlog.traceBegin("StartCrossProfileAppsService");
                    this.mSystemServiceManager.startService(CrossProfileAppsService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartPeopleService");
                    this.mSystemServiceManager.startService(PeopleService.class);
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.addLinearmotorVibratorService(context);
                    this.mSystemServerExt.addStorageHealthInfoService(context);
                    timingsTraceAndSlog.traceBegin("StartMediaMetricsManager");
                    this.mSystemServiceManager.startService(MediaMetricsManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartBackgroundInstallControlService");
                    this.mSystemServiceManager.startService(BackgroundInstallControlService.class);
                    timingsTraceAndSlog.traceEnd();
                    mediaRouterService = mediaRouterService22;
                    lifecycle2 = lifecycle;
                    vcnManagementService = vcnManagementService22;
                    vpnManagerService = vpnManagerService22;
                    iLockSettings3 = iLockSettings2;
                    networkTimeUpdateService2 = networkTimeUpdateService;
                    timingsTraceAndSlog.traceBegin("StartMediaProjectionManager");
                    this.mSystemServiceManager.startService(MediaProjectionManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (hasSystemFeature4) {
                    }
                    if (!this.mPackageManager.hasSystemFeature("android.software.slices_disabled")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartStatsCompanion");
                    this.mSystemServiceManager.startServiceFromJar(STATS_COMPANION_LIFECYCLE_CLASS, STATS_COMPANION_APEX_PATH);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartRebootReadinessManagerService");
                    this.mSystemServiceManager.startServiceFromJar(REBOOT_READINESS_LIFECYCLE_CLASS, SCHEDULING_APEX_PATH);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartStatsPullAtomService");
                    this.mSystemServiceManager.startService(STATS_PULL_ATOM_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StatsBootstrapAtomService");
                    this.mSystemServiceManager.startService(STATS_BOOTSTRAP_ATOM_SERVICE_LIFECYCLE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartIncidentCompanionService");
                    this.mSystemServiceManager.startService(IncidentCompanionService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StarSdkSandboxManagerService");
                    this.mSystemServiceManager.startService(SDK_SANDBOX_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartAdServicesManagerService");
                    startService = this.mSystemServiceManager.startService(AD_SERVICES_MANAGER_SERVICE_CLASS);
                    if (startService instanceof Dumpable) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartOnDevicePersonalizationSystemService");
                    this.mSystemServiceManager.startService(ON_DEVICE_PERSONALIZATION_SYSTEM_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (detectSafeMode) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.hardware.telephony")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.autofill")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.credentials")) {
                    }
                    if (deviceHasConfigString(context, R.string.config_screenshotErrorReceiverComponent)) {
                    }
                    timingsTraceAndSlog.traceBegin("StartSelectionToolbarManagerService");
                    this.mSystemServiceManager.startService(SELECTION_TOOLBAR_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartClipboardService");
                    this.mSystemServiceManager.startService(ClipboardService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("AppServiceManager");
                    this.mSystemServiceManager.startService(AppBindingService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.startOtherServices();
                    this.mSocExt.startOtherServices();
                    timingsTraceAndSlog.traceBegin("startTracingServiceProxy");
                    this.mSystemServiceManager.startService(TracingServiceProxy.class);
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.linearVibratorSystemReady();
                    timingsTraceAndSlog.traceBegin("MakeLockSettingsServiceReady");
                    if (iLockSettings3 != null) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartBootPhaseLockSettingsReady");
                    this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, SystemService.PHASE_LOCK_SETTINGS_READY);
                    timingsTraceAndSlog.traceEnd();
                    createInstance = HsumBootUserInitializer.createInstance(this.mActivityManagerService, this.mPackageManagerService, this.mContentResolver, context.getResources().getBoolean(17891723));
                    if (createInstance != null) {
                    }
                    timingsTraceAndSlog.traceBegin("StartBootPhaseSystemServicesReady");
                    this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, 500);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("MakeWindowManagerServiceReady");
                    Slog.i(TAG, "wms systemReady");
                    main.systemReady();
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("RegisterLogMteState");
                    LogMteState.register(context);
                    timingsTraceAndSlog.traceEnd();
                    synchronized (SystemService.class) {
                    }
                }
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartFontManagerService");
                this.mSystemServiceManager.startService(new FontManagerService.Lifecycle(context, detectSafeMode));
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartTextServicesManager");
                this.mSystemServiceManager.startService(TextServicesManagerService.Lifecycle.class);
                timingsTraceAndSlog.traceEnd();
                if (!z2) {
                    timingsTraceAndSlog.traceBegin("StartTextClassificationManagerService");
                    this.mSystemServiceManager.startService(TextClassificationManagerService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartNetworkScoreService");
                this.mSystemServiceManager.startService(NetworkScoreService.Lifecycle.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartNetworkStatsService");
                this.mSystemServiceManager.startServiceFromJar(NETWORK_STATS_SERVICE_INITIALIZER_CLASS, CONNECTIVITY_SERVICE_APEX_PATH);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartNetworkPolicyManagerService");
                try {
                    networkPolicyManagerService = new NetworkPolicyManagerService(context, this.mActivityManagerService, networkManagementService);
                } catch (Throwable th15) {
                    th = th15;
                    networkPolicyManagerService = null;
                }
                try {
                    ServiceManager.addService("netpolicy", networkPolicyManagerService);
                } catch (Throwable th16) {
                    th = th16;
                    reportWtf("starting NetworkPolicy Service", th);
                    timingsTraceAndSlog.traceEnd();
                    if (context.getPackageManager().hasSystemFeature("android.hardware.wifi")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.wifi.rtt")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.wifi.aware")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.wifi.direct")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.lowpan")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartPacProxyService");
                    ServiceManager.addService("pac_proxy", new PacProxyService(context));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartConnectivityService");
                    this.mSystemServiceManager.startServiceFromJar(CONNECTIVITY_SERVICE_INITIALIZER_CLASS, CONNECTIVITY_SERVICE_APEX_PATH);
                    networkPolicyManagerService.bindConnectivityManager();
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartVpnManagerService");
                    ?? r522 = VpnManagerService.create(context);
                    ServiceManager.addService("vpn_management", (IBinder) r522);
                    VpnManagerService vpnManagerService222 = r522;
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartVcnManagementService");
                    ?? create22 = VcnManagementService.create(context);
                    ServiceManager.addService("vcn_management", (IBinder) create22);
                    networkManagementService2 = networkManagementService;
                    VcnManagementService vcnManagementService222 = create22;
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartSystemUpdateManagerService");
                    ServiceManager.addService("system_update", new SystemUpdateManagerService(context));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartUpdateLockService");
                    ServiceManager.addService("updatelock", new UpdateLockService(context));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartNotificationManager");
                    this.mSystemServiceManager.startService(NotificationManagerService.class);
                    SystemNotificationChannels.removeDeprecated(context);
                    SystemNotificationChannels.createAll(context);
                    INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartDeviceMonitor");
                    this.mSystemServiceManager.startService(DeviceStorageMonitorService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartTimeDetectorService");
                    this.mSystemServiceManager.startService(TIME_DETECTOR_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartLocationManagerService");
                    this.mSystemServiceManager.startService(LocationManagerService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartCountryDetectorService");
                    ICountryDetector.Stub countryDetectorService22 = new CountryDetectorService(context);
                    ServiceManager.addService("country_detector", countryDetectorService22);
                    Object obj22 = countryDetectorService22;
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartTimeZoneDetectorService");
                    r23 = obj22;
                    this.mSystemServiceManager.startService(TIME_ZONE_DETECTOR_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartAltitudeService");
                    this.mSystemServiceManager.startService(AltitudeService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartLocationTimeZoneManagerService");
                    this.mSystemServiceManager.startService(LOCATION_TIME_ZONE_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (context.getResources().getBoolean(17891664)) {
                    }
                    if (!hasSystemFeature4) {
                    }
                    this.mSystemServerExt.writeAgingCriticalEvent();
                    if (!context.getResources().getBoolean(17891682)) {
                    }
                    timingsTraceAndSlog.traceBegin("StartWallpaperEffectsGenerationService");
                    this.mSystemServiceManager.startService(WALLPAPER_EFFECTS_GENERATION_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartAudioService");
                    if (hasSystemFeature5) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartSoundTriggerMiddlewareService");
                    this.mSystemServiceManager.startService(SoundTriggerMiddlewareService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.hardware.broadcastradio")) {
                    }
                    if (!hasSystemFeature6) {
                    }
                    if (hasSystemFeature4) {
                    }
                    if (!hasSystemFeature4) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.midi")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartAdbService");
                    this.mSystemServiceManager.startService(ADB_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (!this.mPackageManager.hasSystemFeature("android.hardware.usb.host")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartUsbService");
                    this.mSystemServiceManager.startService(USB_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (!hasSystemFeature4) {
                    }
                    timingsTraceAndSlog.traceBegin("StartHardwarePropertiesManagerService");
                    ServiceManager.addService("hardware_properties", new HardwarePropertiesManagerService(context));
                    timingsTraceAndSlog.traceEnd();
                    if (!hasSystemFeature4) {
                    }
                    timingsTraceAndSlog.traceBegin("StartColorDisplay");
                    this.mSystemServiceManager.startService(ColorDisplayService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartJobScheduler");
                    if (!this.mSystemServerExt.startJobSchedulerService()) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartSoundTrigger");
                    this.mSystemServiceManager.startService(SoundTriggerService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartTrustManager");
                    this.mSystemServiceManager.startService(TrustManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.software.backup")) {
                    }
                    if (!this.mPackageManager.hasSystemFeature("android.software.app_widgets")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartAppWidgetService");
                    this.mSystemServiceManager.startService(APPWIDGET_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartVoiceRecognitionManager");
                    this.mSystemServiceManager.startService(VOICE_RECOGNITION_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (GestureLauncherService.isGestureLauncherEnabled(context.getResources())) {
                    }
                    timingsTraceAndSlog.traceBegin("StartSensorNotification");
                    this.mSystemServiceManager.startService(SensorNotificationService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.hardware.context_hub")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartDiskStatsService");
                    ServiceManager.addService("diskstats", new DiskStatsService(context));
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("RuntimeService");
                    ServiceManager.addService("runtime", new RuntimeService(context));
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.addCabcService(context, timingsTraceAndSlog);
                    timingsTraceAndSlog.traceBegin("RenderAcceleratingService");
                    this.mSystemServerExt.addRenderAcceleratingService(context, timingsTraceAndSlog);
                    timingsTraceAndSlog.traceEnd();
                    if (hasSystemFeature4) {
                    }
                    networkTimeUpdateService = null;
                    timingsTraceAndSlog.traceBegin("CertBlacklister");
                    new CertBlacklister(context);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartEmergencyAffordanceService");
                    this.mSystemServiceManager.startService(EmergencyAffordanceService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin(START_BLOB_STORE_SERVICE);
                    this.mSystemServiceManager.startService(BLOB_STORE_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartDreamManager");
                    this.mSystemServiceManager.startService(DreamManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("AddGraphicsStatsService");
                    ServiceManager.addService("graphicsstats", new GraphicsStatsService(context));
                    timingsTraceAndSlog.traceEnd();
                    if (CoverageService.ENABLED) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.print")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartAttestationVerificationService");
                    this.mSystemServiceManager.startService(AttestationVerificationManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.software.companion_device_setup")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartRestrictionManager");
                    this.mSystemServiceManager.startService(RestrictionsManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartMediaSessionService");
                    Slog.i(TAG, "Media Session Service");
                    this.mSystemServiceManager.startService(MEDIA_SESSION_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.hardware.hdmi.cec")) {
                    }
                    if (!this.mPackageManager.hasSystemFeature("android.software.live_tv")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartTvInteractiveAppManager");
                    this.mSystemServiceManager.startService(TvInteractiveAppManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (!this.mPackageManager.hasSystemFeature("android.software.live_tv")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartTvInputManager");
                    this.mSystemServiceManager.startService(TvInputManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (this.mPackageManager.hasSystemFeature("android.hardware.tv.tuner")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.picture_in_picture")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.leanback")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartMediaRouterService");
                    MediaRouterService mediaRouterService222 = new MediaRouterService(context);
                    ServiceManager.addService("media_router", mediaRouterService222);
                    timingsTraceAndSlog.traceEnd();
                    hasSystemFeature = this.mPackageManager.hasSystemFeature("android.hardware.biometrics.face");
                    hasSystemFeature2 = this.mPackageManager.hasSystemFeature("android.hardware.biometrics.iris");
                    hasSystemFeature3 = this.mPackageManager.hasSystemFeature("android.hardware.fingerprint");
                    if (hasSystemFeature) {
                    }
                    if (hasSystemFeature2) {
                    }
                    if (hasSystemFeature3) {
                    }
                    timingsTraceAndSlog.traceBegin("StartBiometricService");
                    this.mSystemServiceManager.startService(BiometricService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartAuthService");
                    this.mSystemServiceManager.startService(AuthService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (!hasSystemFeature4) {
                    }
                    if (!hasSystemFeature4) {
                    }
                    timingsTraceAndSlog.traceBegin("StartShortcutServiceLifecycle");
                    this.mSystemServiceManager.startService(ShortcutService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartLauncherAppsService");
                    this.mSystemServiceManager.startService(LauncherAppsService.class);
                    timingsTraceAndSlog.traceEnd();
                    Slog.i(TAG, "Dynamic filter service");
                    this.mSystemServerExt.startDynamicFilterService(this.mSystemServiceManager);
                    timingsTraceAndSlog.traceBegin("StartCrossProfileAppsService");
                    this.mSystemServiceManager.startService(CrossProfileAppsService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartPeopleService");
                    this.mSystemServiceManager.startService(PeopleService.class);
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.addLinearmotorVibratorService(context);
                    this.mSystemServerExt.addStorageHealthInfoService(context);
                    timingsTraceAndSlog.traceBegin("StartMediaMetricsManager");
                    this.mSystemServiceManager.startService(MediaMetricsManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartBackgroundInstallControlService");
                    this.mSystemServiceManager.startService(BackgroundInstallControlService.class);
                    timingsTraceAndSlog.traceEnd();
                    mediaRouterService = mediaRouterService222;
                    lifecycle2 = lifecycle;
                    vcnManagementService = vcnManagementService222;
                    vpnManagerService = vpnManagerService222;
                    iLockSettings3 = iLockSettings2;
                    networkTimeUpdateService2 = networkTimeUpdateService;
                    timingsTraceAndSlog.traceBegin("StartMediaProjectionManager");
                    this.mSystemServiceManager.startService(MediaProjectionManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                    if (hasSystemFeature4) {
                    }
                    if (!this.mPackageManager.hasSystemFeature("android.software.slices_disabled")) {
                    }
                    if (context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
                    }
                    timingsTraceAndSlog.traceBegin("StartStatsCompanion");
                    this.mSystemServiceManager.startServiceFromJar(STATS_COMPANION_LIFECYCLE_CLASS, STATS_COMPANION_APEX_PATH);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartRebootReadinessManagerService");
                    this.mSystemServiceManager.startServiceFromJar(REBOOT_READINESS_LIFECYCLE_CLASS, SCHEDULING_APEX_PATH);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartStatsPullAtomService");
                    this.mSystemServiceManager.startService(STATS_PULL_ATOM_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StatsBootstrapAtomService");
                    this.mSystemServiceManager.startService(STATS_BOOTSTRAP_ATOM_SERVICE_LIFECYCLE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartIncidentCompanionService");
                    this.mSystemServiceManager.startService(IncidentCompanionService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StarSdkSandboxManagerService");
                    this.mSystemServiceManager.startService(SDK_SANDBOX_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartAdServicesManagerService");
                    startService = this.mSystemServiceManager.startService(AD_SERVICES_MANAGER_SERVICE_CLASS);
                    if (startService instanceof Dumpable) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartOnDevicePersonalizationSystemService");
                    this.mSystemServiceManager.startService(ON_DEVICE_PERSONALIZATION_SYSTEM_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    if (detectSafeMode) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.hardware.telephony")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.autofill")) {
                    }
                    if (this.mPackageManager.hasSystemFeature("android.software.credentials")) {
                    }
                    if (deviceHasConfigString(context, R.string.config_screenshotErrorReceiverComponent)) {
                    }
                    timingsTraceAndSlog.traceBegin("StartSelectionToolbarManagerService");
                    this.mSystemServiceManager.startService(SELECTION_TOOLBAR_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartClipboardService");
                    this.mSystemServiceManager.startService(ClipboardService.class);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("AppServiceManager");
                    this.mSystemServiceManager.startService(AppBindingService.Lifecycle.class);
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.startOtherServices();
                    this.mSocExt.startOtherServices();
                    timingsTraceAndSlog.traceBegin("startTracingServiceProxy");
                    this.mSystemServiceManager.startService(TracingServiceProxy.class);
                    timingsTraceAndSlog.traceEnd();
                    this.mSystemServerExt.linearVibratorSystemReady();
                    timingsTraceAndSlog.traceBegin("MakeLockSettingsServiceReady");
                    if (iLockSettings3 != null) {
                    }
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartBootPhaseLockSettingsReady");
                    this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, SystemService.PHASE_LOCK_SETTINGS_READY);
                    timingsTraceAndSlog.traceEnd();
                    createInstance = HsumBootUserInitializer.createInstance(this.mActivityManagerService, this.mPackageManagerService, this.mContentResolver, context.getResources().getBoolean(17891723));
                    if (createInstance != null) {
                    }
                    timingsTraceAndSlog.traceBegin("StartBootPhaseSystemServicesReady");
                    this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, 500);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("MakeWindowManagerServiceReady");
                    Slog.i(TAG, "wms systemReady");
                    main.systemReady();
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("RegisterLogMteState");
                    LogMteState.register(context);
                    timingsTraceAndSlog.traceEnd();
                    synchronized (SystemService.class) {
                    }
                }
                timingsTraceAndSlog.traceEnd();
                if (context.getPackageManager().hasSystemFeature("android.hardware.wifi")) {
                    timingsTraceAndSlog.traceBegin("StartWifi");
                    this.mSystemServiceManager.startServiceFromJar(WIFI_SERVICE_CLASS, WIFI_APEX_SERVICE_JAR_PATH);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartWifiScanning");
                    this.mSystemServiceManager.startServiceFromJar(WIFI_SCANNING_SERVICE_CLASS, WIFI_APEX_SERVICE_JAR_PATH);
                    timingsTraceAndSlog.traceEnd();
                }
                if (context.getPackageManager().hasSystemFeature("android.hardware.wifi.rtt")) {
                    timingsTraceAndSlog.traceBegin("StartRttService");
                    this.mSystemServiceManager.startServiceFromJar(WIFI_RTT_SERVICE_CLASS, WIFI_APEX_SERVICE_JAR_PATH);
                    timingsTraceAndSlog.traceEnd();
                }
                if (context.getPackageManager().hasSystemFeature("android.hardware.wifi.aware")) {
                    timingsTraceAndSlog.traceBegin("StartWifiAware");
                    this.mSystemServiceManager.startServiceFromJar(WIFI_AWARE_SERVICE_CLASS, WIFI_APEX_SERVICE_JAR_PATH);
                    timingsTraceAndSlog.traceEnd();
                }
                if (context.getPackageManager().hasSystemFeature("android.hardware.wifi.direct")) {
                    timingsTraceAndSlog.traceBegin("StartWifiP2P");
                    this.mSystemServiceManager.startServiceFromJar(WIFI_P2P_SERVICE_CLASS, WIFI_APEX_SERVICE_JAR_PATH);
                    timingsTraceAndSlog.traceEnd();
                }
                if (context.getPackageManager().hasSystemFeature("android.hardware.lowpan")) {
                    timingsTraceAndSlog.traceBegin("StartLowpan");
                    this.mSystemServiceManager.startService(LOWPAN_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartPacProxyService");
                ServiceManager.addService("pac_proxy", new PacProxyService(context));
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartConnectivityService");
                this.mSystemServiceManager.startServiceFromJar(CONNECTIVITY_SERVICE_INITIALIZER_CLASS, CONNECTIVITY_SERVICE_APEX_PATH);
                networkPolicyManagerService.bindConnectivityManager();
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartVpnManagerService");
                ?? r5222 = VpnManagerService.create(context);
                ServiceManager.addService("vpn_management", (IBinder) r5222);
                VpnManagerService vpnManagerService2222 = r5222;
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartVcnManagementService");
                ?? create222 = VcnManagementService.create(context);
                ServiceManager.addService("vcn_management", (IBinder) create222);
                networkManagementService2 = networkManagementService;
                VcnManagementService vcnManagementService2222 = create222;
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartSystemUpdateManagerService");
                ServiceManager.addService("system_update", new SystemUpdateManagerService(context));
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartUpdateLockService");
                ServiceManager.addService("updatelock", new UpdateLockService(context));
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartNotificationManager");
                this.mSystemServiceManager.startService(NotificationManagerService.class);
                SystemNotificationChannels.removeDeprecated(context);
                SystemNotificationChannels.createAll(context);
                INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartDeviceMonitor");
                this.mSystemServiceManager.startService(DeviceStorageMonitorService.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartTimeDetectorService");
                this.mSystemServiceManager.startService(TIME_DETECTOR_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartLocationManagerService");
                this.mSystemServiceManager.startService(LocationManagerService.Lifecycle.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartCountryDetectorService");
                ICountryDetector.Stub countryDetectorService222 = new CountryDetectorService(context);
                ServiceManager.addService("country_detector", countryDetectorService222);
                Object obj222 = countryDetectorService222;
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartTimeZoneDetectorService");
                r23 = obj222;
                this.mSystemServiceManager.startService(TIME_ZONE_DETECTOR_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartAltitudeService");
                this.mSystemServiceManager.startService(AltitudeService.Lifecycle.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartLocationTimeZoneManagerService");
                this.mSystemServiceManager.startService(LOCATION_TIME_ZONE_MANAGER_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                if (context.getResources().getBoolean(17891664)) {
                    timingsTraceAndSlog.traceBegin("StartGnssTimeUpdateService");
                    try {
                        this.mSystemServiceManager.startService(GNSS_TIME_UPDATE_SERVICE_CLASS);
                    } catch (Throwable th17) {
                        reportWtf("starting GnssTimeUpdateService service", th17);
                    }
                    timingsTraceAndSlog.traceEnd();
                }
                if (!hasSystemFeature4) {
                    timingsTraceAndSlog.traceBegin("StartSearchManagerService");
                    try {
                        this.mSystemServiceManager.startService(SEARCH_MANAGER_SERVICE_CLASS);
                    } catch (Throwable th18) {
                        reportWtf("starting Search Service", th18);
                    }
                    timingsTraceAndSlog.traceEnd();
                }
                this.mSystemServerExt.writeAgingCriticalEvent();
                if (!context.getResources().getBoolean(17891682)) {
                    timingsTraceAndSlog.traceBegin("StartWallpaperManagerService");
                    this.mSystemServiceManager.startService(WALLPAPER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                } else {
                    Slog.i(TAG, "Wallpaper service disabled by config");
                }
                timingsTraceAndSlog.traceBegin("StartWallpaperEffectsGenerationService");
                this.mSystemServiceManager.startService(WALLPAPER_EFFECTS_GENERATION_MANAGER_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartAudioService");
                if (hasSystemFeature5) {
                    this.mSystemServiceManager.startService(AudioService.Lifecycle.class);
                    networkPolicyManagerService2 = networkPolicyManagerService;
                } else {
                    String string2 = context.getResources().getString(R.string.config_tvRemoteServicePackage);
                    try {
                        systemServiceManager = this.mSystemServiceManager;
                        sb = new StringBuilder();
                        sb.append(string2);
                        networkPolicyManagerService2 = networkPolicyManagerService;
                    } catch (Throwable th19) {
                        th = th19;
                        networkPolicyManagerService2 = networkPolicyManagerService;
                    }
                    try {
                        sb.append("$Lifecycle");
                        systemServiceManager.startService(sb.toString());
                    } catch (Throwable th20) {
                        th = th20;
                        reportWtf("starting " + string2, th);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartSoundTriggerMiddlewareService");
                        this.mSystemServiceManager.startService(SoundTriggerMiddlewareService.Lifecycle.class);
                        timingsTraceAndSlog.traceEnd();
                        if (this.mPackageManager.hasSystemFeature("android.hardware.broadcastradio")) {
                        }
                        if (!hasSystemFeature6) {
                        }
                        if (hasSystemFeature4) {
                        }
                        if (!hasSystemFeature4) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.software.midi")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartAdbService");
                        this.mSystemServiceManager.startService(ADB_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        if (!this.mPackageManager.hasSystemFeature("android.hardware.usb.host")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartUsbService");
                        this.mSystemServiceManager.startService(USB_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        if (!hasSystemFeature4) {
                        }
                        timingsTraceAndSlog.traceBegin("StartHardwarePropertiesManagerService");
                        ServiceManager.addService("hardware_properties", new HardwarePropertiesManagerService(context));
                        timingsTraceAndSlog.traceEnd();
                        if (!hasSystemFeature4) {
                        }
                        timingsTraceAndSlog.traceBegin("StartColorDisplay");
                        this.mSystemServiceManager.startService(ColorDisplayService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartJobScheduler");
                        if (!this.mSystemServerExt.startJobSchedulerService()) {
                        }
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartSoundTrigger");
                        this.mSystemServiceManager.startService(SoundTriggerService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartTrustManager");
                        this.mSystemServiceManager.startService(TrustManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        if (this.mPackageManager.hasSystemFeature("android.software.backup")) {
                        }
                        if (!this.mPackageManager.hasSystemFeature("android.software.app_widgets")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartAppWidgetService");
                        this.mSystemServiceManager.startService(APPWIDGET_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartVoiceRecognitionManager");
                        this.mSystemServiceManager.startService(VOICE_RECOGNITION_MANAGER_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        if (GestureLauncherService.isGestureLauncherEnabled(context.getResources())) {
                        }
                        timingsTraceAndSlog.traceBegin("StartSensorNotification");
                        this.mSystemServiceManager.startService(SensorNotificationService.class);
                        timingsTraceAndSlog.traceEnd();
                        if (this.mPackageManager.hasSystemFeature("android.hardware.context_hub")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartDiskStatsService");
                        ServiceManager.addService("diskstats", new DiskStatsService(context));
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("RuntimeService");
                        ServiceManager.addService("runtime", new RuntimeService(context));
                        timingsTraceAndSlog.traceEnd();
                        this.mSystemServerExt.addCabcService(context, timingsTraceAndSlog);
                        timingsTraceAndSlog.traceBegin("RenderAcceleratingService");
                        this.mSystemServerExt.addRenderAcceleratingService(context, timingsTraceAndSlog);
                        timingsTraceAndSlog.traceEnd();
                        if (hasSystemFeature4) {
                        }
                        networkTimeUpdateService = null;
                        timingsTraceAndSlog.traceBegin("CertBlacklister");
                        new CertBlacklister(context);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartEmergencyAffordanceService");
                        this.mSystemServiceManager.startService(EmergencyAffordanceService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin(START_BLOB_STORE_SERVICE);
                        this.mSystemServiceManager.startService(BLOB_STORE_MANAGER_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartDreamManager");
                        this.mSystemServiceManager.startService(DreamManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("AddGraphicsStatsService");
                        ServiceManager.addService("graphicsstats", new GraphicsStatsService(context));
                        timingsTraceAndSlog.traceEnd();
                        if (CoverageService.ENABLED) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.software.print")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartAttestationVerificationService");
                        this.mSystemServiceManager.startService(AttestationVerificationManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        if (this.mPackageManager.hasSystemFeature("android.software.companion_device_setup")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartRestrictionManager");
                        this.mSystemServiceManager.startService(RestrictionsManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartMediaSessionService");
                        Slog.i(TAG, "Media Session Service");
                        this.mSystemServiceManager.startService(MEDIA_SESSION_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        if (this.mPackageManager.hasSystemFeature("android.hardware.hdmi.cec")) {
                        }
                        if (!this.mPackageManager.hasSystemFeature("android.software.live_tv")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartTvInteractiveAppManager");
                        this.mSystemServiceManager.startService(TvInteractiveAppManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        if (!this.mPackageManager.hasSystemFeature("android.software.live_tv")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartTvInputManager");
                        this.mSystemServiceManager.startService(TvInputManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        if (this.mPackageManager.hasSystemFeature("android.hardware.tv.tuner")) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.software.picture_in_picture")) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.software.leanback")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartMediaRouterService");
                        MediaRouterService mediaRouterService2222 = new MediaRouterService(context);
                        ServiceManager.addService("media_router", mediaRouterService2222);
                        timingsTraceAndSlog.traceEnd();
                        hasSystemFeature = this.mPackageManager.hasSystemFeature("android.hardware.biometrics.face");
                        hasSystemFeature2 = this.mPackageManager.hasSystemFeature("android.hardware.biometrics.iris");
                        hasSystemFeature3 = this.mPackageManager.hasSystemFeature("android.hardware.fingerprint");
                        if (hasSystemFeature) {
                        }
                        if (hasSystemFeature2) {
                        }
                        if (hasSystemFeature3) {
                        }
                        timingsTraceAndSlog.traceBegin("StartBiometricService");
                        this.mSystemServiceManager.startService(BiometricService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartAuthService");
                        this.mSystemServiceManager.startService(AuthService.class);
                        timingsTraceAndSlog.traceEnd();
                        if (!hasSystemFeature4) {
                        }
                        if (!hasSystemFeature4) {
                        }
                        timingsTraceAndSlog.traceBegin("StartShortcutServiceLifecycle");
                        this.mSystemServiceManager.startService(ShortcutService.Lifecycle.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartLauncherAppsService");
                        this.mSystemServiceManager.startService(LauncherAppsService.class);
                        timingsTraceAndSlog.traceEnd();
                        Slog.i(TAG, "Dynamic filter service");
                        this.mSystemServerExt.startDynamicFilterService(this.mSystemServiceManager);
                        timingsTraceAndSlog.traceBegin("StartCrossProfileAppsService");
                        this.mSystemServiceManager.startService(CrossProfileAppsService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartPeopleService");
                        this.mSystemServiceManager.startService(PeopleService.class);
                        timingsTraceAndSlog.traceEnd();
                        this.mSystemServerExt.addLinearmotorVibratorService(context);
                        this.mSystemServerExt.addStorageHealthInfoService(context);
                        timingsTraceAndSlog.traceBegin("StartMediaMetricsManager");
                        this.mSystemServiceManager.startService(MediaMetricsManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartBackgroundInstallControlService");
                        this.mSystemServiceManager.startService(BackgroundInstallControlService.class);
                        timingsTraceAndSlog.traceEnd();
                        mediaRouterService = mediaRouterService2222;
                        lifecycle2 = lifecycle;
                        vcnManagementService = vcnManagementService2222;
                        vpnManagerService = vpnManagerService2222;
                        iLockSettings3 = iLockSettings2;
                        networkTimeUpdateService2 = networkTimeUpdateService;
                        timingsTraceAndSlog.traceBegin("StartMediaProjectionManager");
                        this.mSystemServiceManager.startService(MediaProjectionManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        if (hasSystemFeature4) {
                        }
                        if (!this.mPackageManager.hasSystemFeature("android.software.slices_disabled")) {
                        }
                        if (context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartStatsCompanion");
                        this.mSystemServiceManager.startServiceFromJar(STATS_COMPANION_LIFECYCLE_CLASS, STATS_COMPANION_APEX_PATH);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartRebootReadinessManagerService");
                        this.mSystemServiceManager.startServiceFromJar(REBOOT_READINESS_LIFECYCLE_CLASS, SCHEDULING_APEX_PATH);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartStatsPullAtomService");
                        this.mSystemServiceManager.startService(STATS_PULL_ATOM_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StatsBootstrapAtomService");
                        this.mSystemServiceManager.startService(STATS_BOOTSTRAP_ATOM_SERVICE_LIFECYCLE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartIncidentCompanionService");
                        this.mSystemServiceManager.startService(IncidentCompanionService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StarSdkSandboxManagerService");
                        this.mSystemServiceManager.startService(SDK_SANDBOX_MANAGER_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartAdServicesManagerService");
                        startService = this.mSystemServiceManager.startService(AD_SERVICES_MANAGER_SERVICE_CLASS);
                        if (startService instanceof Dumpable) {
                        }
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartOnDevicePersonalizationSystemService");
                        this.mSystemServiceManager.startService(ON_DEVICE_PERSONALIZATION_SYSTEM_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        if (detectSafeMode) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.hardware.telephony")) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.software.autofill")) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.software.credentials")) {
                        }
                        if (deviceHasConfigString(context, R.string.config_screenshotErrorReceiverComponent)) {
                        }
                        timingsTraceAndSlog.traceBegin("StartSelectionToolbarManagerService");
                        this.mSystemServiceManager.startService(SELECTION_TOOLBAR_MANAGER_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartClipboardService");
                        this.mSystemServiceManager.startService(ClipboardService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("AppServiceManager");
                        this.mSystemServiceManager.startService(AppBindingService.Lifecycle.class);
                        timingsTraceAndSlog.traceEnd();
                        this.mSystemServerExt.startOtherServices();
                        this.mSocExt.startOtherServices();
                        timingsTraceAndSlog.traceBegin("startTracingServiceProxy");
                        this.mSystemServiceManager.startService(TracingServiceProxy.class);
                        timingsTraceAndSlog.traceEnd();
                        this.mSystemServerExt.linearVibratorSystemReady();
                        timingsTraceAndSlog.traceBegin("MakeLockSettingsServiceReady");
                        if (iLockSettings3 != null) {
                        }
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartBootPhaseLockSettingsReady");
                        this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, SystemService.PHASE_LOCK_SETTINGS_READY);
                        timingsTraceAndSlog.traceEnd();
                        createInstance = HsumBootUserInitializer.createInstance(this.mActivityManagerService, this.mPackageManagerService, this.mContentResolver, context.getResources().getBoolean(17891723));
                        if (createInstance != null) {
                        }
                        timingsTraceAndSlog.traceBegin("StartBootPhaseSystemServicesReady");
                        this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, 500);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("MakeWindowManagerServiceReady");
                        Slog.i(TAG, "wms systemReady");
                        main.systemReady();
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("RegisterLogMteState");
                        LogMteState.register(context);
                        timingsTraceAndSlog.traceEnd();
                        synchronized (SystemService.class) {
                        }
                    }
                }
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartSoundTriggerMiddlewareService");
                this.mSystemServiceManager.startService(SoundTriggerMiddlewareService.Lifecycle.class);
                timingsTraceAndSlog.traceEnd();
                if (this.mPackageManager.hasSystemFeature("android.hardware.broadcastradio")) {
                    timingsTraceAndSlog.traceBegin("StartBroadcastRadioService");
                    this.mSystemServiceManager.startService(BroadcastRadioService.class);
                    timingsTraceAndSlog.traceEnd();
                }
                if (!hasSystemFeature6) {
                    Slog.i(TAG, "DockObserver");
                    timingsTraceAndSlog.traceBegin("StartDockObserver");
                    this.mSystemServiceManager.startService(DockObserver.class);
                    timingsTraceAndSlog.traceEnd();
                }
                if (hasSystemFeature4) {
                    timingsTraceAndSlog.traceBegin("StartThermalObserver");
                    this.mSystemServiceManager.startService(THERMAL_OBSERVER_CLASS);
                    timingsTraceAndSlog.traceEnd();
                }
                if (!hasSystemFeature4) {
                    timingsTraceAndSlog.traceBegin("StartWiredAccessoryManager");
                    try {
                        inputManagerService.setWiredAccessoryCallbacks(new WiredAccessoryManager(context, inputManagerService));
                    } catch (Throwable th21) {
                        reportWtf("starting WiredAccessoryManager", th21);
                    }
                    timingsTraceAndSlog.traceEnd();
                }
                if (this.mPackageManager.hasSystemFeature("android.software.midi")) {
                    timingsTraceAndSlog.traceBegin("StartMidiManager");
                    this.mSystemServiceManager.startService(MIDI_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartAdbService");
                this.mSystemServiceManager.startService(ADB_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                if (!this.mPackageManager.hasSystemFeature("android.hardware.usb.host") || this.mPackageManager.hasSystemFeature("android.hardware.usb.accessory") || equals) {
                    timingsTraceAndSlog.traceBegin("StartUsbService");
                    this.mSystemServiceManager.startService(USB_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                }
                if (!hasSystemFeature4) {
                    timingsTraceAndSlog.traceBegin("StartSerialService");
                    try {
                        ServiceManager.addService("serial", new SerialService(context));
                    } catch (Throwable th22) {
                        Slog.e(TAG, "Failure starting SerialService", th22);
                    }
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartHardwarePropertiesManagerService");
                ServiceManager.addService("hardware_properties", new HardwarePropertiesManagerService(context));
                timingsTraceAndSlog.traceEnd();
                if (!hasSystemFeature4) {
                    timingsTraceAndSlog.traceBegin("StartTwilightService");
                    this.mSystemServiceManager.startService(TwilightService.class);
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartColorDisplay");
                this.mSystemServiceManager.startService(ColorDisplayService.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartJobScheduler");
                if (!this.mSystemServerExt.startJobSchedulerService()) {
                    this.mSystemServiceManager.startService(JOB_SCHEDULER_SERVICE_CLASS);
                }
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartSoundTrigger");
                this.mSystemServiceManager.startService(SoundTriggerService.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartTrustManager");
                this.mSystemServiceManager.startService(TrustManagerService.class);
                timingsTraceAndSlog.traceEnd();
                if (this.mPackageManager.hasSystemFeature("android.software.backup")) {
                    timingsTraceAndSlog.traceBegin("StartBackupManager");
                    this.mSystemServiceManager.startService(BACKUP_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                }
                if (!this.mPackageManager.hasSystemFeature("android.software.app_widgets") || context.getResources().getBoolean(17891654)) {
                    timingsTraceAndSlog.traceBegin("StartAppWidgetService");
                    this.mSystemServiceManager.startService(APPWIDGET_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartVoiceRecognitionManager");
                this.mSystemServiceManager.startService(VOICE_RECOGNITION_MANAGER_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                if (GestureLauncherService.isGestureLauncherEnabled(context.getResources())) {
                    timingsTraceAndSlog.traceBegin("StartGestureLauncher");
                    this.mSystemServiceManager.startService(GestureLauncherService.class);
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartSensorNotification");
                this.mSystemServiceManager.startService(SensorNotificationService.class);
                timingsTraceAndSlog.traceEnd();
                if (this.mPackageManager.hasSystemFeature("android.hardware.context_hub")) {
                    timingsTraceAndSlog.traceBegin("StartContextHubSystemService");
                    this.mSystemServiceManager.startService(ContextHubSystemService.class);
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartDiskStatsService");
                ServiceManager.addService("diskstats", new DiskStatsService(context));
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("RuntimeService");
                ServiceManager.addService("runtime", new RuntimeService(context));
                timingsTraceAndSlog.traceEnd();
                this.mSystemServerExt.addCabcService(context, timingsTraceAndSlog);
                timingsTraceAndSlog.traceBegin("RenderAcceleratingService");
                this.mSystemServerExt.addRenderAcceleratingService(context, timingsTraceAndSlog);
                timingsTraceAndSlog.traceEnd();
                if (!hasSystemFeature4 || z3) {
                    networkTimeUpdateService = null;
                } else {
                    timingsTraceAndSlog.traceBegin("StartNetworkTimeUpdateService");
                    try {
                        networkTimeUpdateService = new NetworkTimeUpdateService(context);
                    } catch (Throwable th23) {
                        th = th23;
                        networkTimeUpdateService = null;
                    }
                    try {
                        ServiceManager.addService("network_time_update_service", networkTimeUpdateService);
                    } catch (Throwable th24) {
                        th = th24;
                        reportWtf("starting NetworkTimeUpdate service", th);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("CertBlacklister");
                        new CertBlacklister(context);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartEmergencyAffordanceService");
                        this.mSystemServiceManager.startService(EmergencyAffordanceService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin(START_BLOB_STORE_SERVICE);
                        this.mSystemServiceManager.startService(BLOB_STORE_MANAGER_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartDreamManager");
                        this.mSystemServiceManager.startService(DreamManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("AddGraphicsStatsService");
                        ServiceManager.addService("graphicsstats", new GraphicsStatsService(context));
                        timingsTraceAndSlog.traceEnd();
                        if (CoverageService.ENABLED) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.software.print")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartAttestationVerificationService");
                        this.mSystemServiceManager.startService(AttestationVerificationManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        if (this.mPackageManager.hasSystemFeature("android.software.companion_device_setup")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartRestrictionManager");
                        this.mSystemServiceManager.startService(RestrictionsManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartMediaSessionService");
                        Slog.i(TAG, "Media Session Service");
                        this.mSystemServiceManager.startService(MEDIA_SESSION_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        if (this.mPackageManager.hasSystemFeature("android.hardware.hdmi.cec")) {
                        }
                        if (!this.mPackageManager.hasSystemFeature("android.software.live_tv")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartTvInteractiveAppManager");
                        this.mSystemServiceManager.startService(TvInteractiveAppManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        if (!this.mPackageManager.hasSystemFeature("android.software.live_tv")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartTvInputManager");
                        this.mSystemServiceManager.startService(TvInputManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        if (this.mPackageManager.hasSystemFeature("android.hardware.tv.tuner")) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.software.picture_in_picture")) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.software.leanback")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartMediaRouterService");
                        MediaRouterService mediaRouterService22222 = new MediaRouterService(context);
                        ServiceManager.addService("media_router", mediaRouterService22222);
                        timingsTraceAndSlog.traceEnd();
                        hasSystemFeature = this.mPackageManager.hasSystemFeature("android.hardware.biometrics.face");
                        hasSystemFeature2 = this.mPackageManager.hasSystemFeature("android.hardware.biometrics.iris");
                        hasSystemFeature3 = this.mPackageManager.hasSystemFeature("android.hardware.fingerprint");
                        if (hasSystemFeature) {
                        }
                        if (hasSystemFeature2) {
                        }
                        if (hasSystemFeature3) {
                        }
                        timingsTraceAndSlog.traceBegin("StartBiometricService");
                        this.mSystemServiceManager.startService(BiometricService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartAuthService");
                        this.mSystemServiceManager.startService(AuthService.class);
                        timingsTraceAndSlog.traceEnd();
                        if (!hasSystemFeature4) {
                        }
                        if (!hasSystemFeature4) {
                        }
                        timingsTraceAndSlog.traceBegin("StartShortcutServiceLifecycle");
                        this.mSystemServiceManager.startService(ShortcutService.Lifecycle.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartLauncherAppsService");
                        this.mSystemServiceManager.startService(LauncherAppsService.class);
                        timingsTraceAndSlog.traceEnd();
                        Slog.i(TAG, "Dynamic filter service");
                        this.mSystemServerExt.startDynamicFilterService(this.mSystemServiceManager);
                        timingsTraceAndSlog.traceBegin("StartCrossProfileAppsService");
                        this.mSystemServiceManager.startService(CrossProfileAppsService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartPeopleService");
                        this.mSystemServiceManager.startService(PeopleService.class);
                        timingsTraceAndSlog.traceEnd();
                        this.mSystemServerExt.addLinearmotorVibratorService(context);
                        this.mSystemServerExt.addStorageHealthInfoService(context);
                        timingsTraceAndSlog.traceBegin("StartMediaMetricsManager");
                        this.mSystemServiceManager.startService(MediaMetricsManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartBackgroundInstallControlService");
                        this.mSystemServiceManager.startService(BackgroundInstallControlService.class);
                        timingsTraceAndSlog.traceEnd();
                        mediaRouterService = mediaRouterService22222;
                        lifecycle2 = lifecycle;
                        vcnManagementService = vcnManagementService2222;
                        vpnManagerService = vpnManagerService2222;
                        iLockSettings3 = iLockSettings2;
                        networkTimeUpdateService2 = networkTimeUpdateService;
                        timingsTraceAndSlog.traceBegin("StartMediaProjectionManager");
                        this.mSystemServiceManager.startService(MediaProjectionManagerService.class);
                        timingsTraceAndSlog.traceEnd();
                        if (hasSystemFeature4) {
                        }
                        if (!this.mPackageManager.hasSystemFeature("android.software.slices_disabled")) {
                        }
                        if (context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
                        }
                        timingsTraceAndSlog.traceBegin("StartStatsCompanion");
                        this.mSystemServiceManager.startServiceFromJar(STATS_COMPANION_LIFECYCLE_CLASS, STATS_COMPANION_APEX_PATH);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartRebootReadinessManagerService");
                        this.mSystemServiceManager.startServiceFromJar(REBOOT_READINESS_LIFECYCLE_CLASS, SCHEDULING_APEX_PATH);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartStatsPullAtomService");
                        this.mSystemServiceManager.startService(STATS_PULL_ATOM_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StatsBootstrapAtomService");
                        this.mSystemServiceManager.startService(STATS_BOOTSTRAP_ATOM_SERVICE_LIFECYCLE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartIncidentCompanionService");
                        this.mSystemServiceManager.startService(IncidentCompanionService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StarSdkSandboxManagerService");
                        this.mSystemServiceManager.startService(SDK_SANDBOX_MANAGER_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartAdServicesManagerService");
                        startService = this.mSystemServiceManager.startService(AD_SERVICES_MANAGER_SERVICE_CLASS);
                        if (startService instanceof Dumpable) {
                        }
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartOnDevicePersonalizationSystemService");
                        this.mSystemServiceManager.startService(ON_DEVICE_PERSONALIZATION_SYSTEM_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        if (detectSafeMode) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.hardware.telephony")) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.software.autofill")) {
                        }
                        if (this.mPackageManager.hasSystemFeature("android.software.credentials")) {
                        }
                        if (deviceHasConfigString(context, R.string.config_screenshotErrorReceiverComponent)) {
                        }
                        timingsTraceAndSlog.traceBegin("StartSelectionToolbarManagerService");
                        this.mSystemServiceManager.startService(SELECTION_TOOLBAR_MANAGER_SERVICE_CLASS);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartClipboardService");
                        this.mSystemServiceManager.startService(ClipboardService.class);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("AppServiceManager");
                        this.mSystemServiceManager.startService(AppBindingService.Lifecycle.class);
                        timingsTraceAndSlog.traceEnd();
                        this.mSystemServerExt.startOtherServices();
                        this.mSocExt.startOtherServices();
                        timingsTraceAndSlog.traceBegin("startTracingServiceProxy");
                        this.mSystemServiceManager.startService(TracingServiceProxy.class);
                        timingsTraceAndSlog.traceEnd();
                        this.mSystemServerExt.linearVibratorSystemReady();
                        timingsTraceAndSlog.traceBegin("MakeLockSettingsServiceReady");
                        if (iLockSettings3 != null) {
                        }
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("StartBootPhaseLockSettingsReady");
                        this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, SystemService.PHASE_LOCK_SETTINGS_READY);
                        timingsTraceAndSlog.traceEnd();
                        createInstance = HsumBootUserInitializer.createInstance(this.mActivityManagerService, this.mPackageManagerService, this.mContentResolver, context.getResources().getBoolean(17891723));
                        if (createInstance != null) {
                        }
                        timingsTraceAndSlog.traceBegin("StartBootPhaseSystemServicesReady");
                        this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, 500);
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("MakeWindowManagerServiceReady");
                        Slog.i(TAG, "wms systemReady");
                        main.systemReady();
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("RegisterLogMteState");
                        LogMteState.register(context);
                        timingsTraceAndSlog.traceEnd();
                        synchronized (SystemService.class) {
                        }
                    }
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("CertBlacklister");
                new CertBlacklister(context);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartEmergencyAffordanceService");
                this.mSystemServiceManager.startService(EmergencyAffordanceService.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin(START_BLOB_STORE_SERVICE);
                this.mSystemServiceManager.startService(BLOB_STORE_MANAGER_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartDreamManager");
                this.mSystemServiceManager.startService(DreamManagerService.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("AddGraphicsStatsService");
                ServiceManager.addService("graphicsstats", new GraphicsStatsService(context));
                timingsTraceAndSlog.traceEnd();
                if (CoverageService.ENABLED) {
                    timingsTraceAndSlog.traceBegin("AddCoverageService");
                    this.mSystemServerExt.addOplusTestService(context);
                    timingsTraceAndSlog.traceEnd();
                }
                if (this.mPackageManager.hasSystemFeature("android.software.print")) {
                    timingsTraceAndSlog.traceBegin("StartPrintManager");
                    this.mSystemServiceManager.startService(PRINT_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartAttestationVerificationService");
                this.mSystemServiceManager.startService(AttestationVerificationManagerService.class);
                timingsTraceAndSlog.traceEnd();
                if (this.mPackageManager.hasSystemFeature("android.software.companion_device_setup")) {
                    timingsTraceAndSlog.traceBegin("StartCompanionDeviceManager");
                    this.mSystemServiceManager.startService(COMPANION_DEVICE_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("StartVirtualDeviceManager");
                    this.mSystemServiceManager.startService(VIRTUAL_DEVICE_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartRestrictionManager");
                this.mSystemServiceManager.startService(RestrictionsManagerService.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartMediaSessionService");
                Slog.i(TAG, "Media Session Service");
                this.mSystemServiceManager.startService(MEDIA_SESSION_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                if (this.mPackageManager.hasSystemFeature("android.hardware.hdmi.cec")) {
                    timingsTraceAndSlog.traceBegin("StartHdmiControlService");
                    this.mSystemServiceManager.startService(HdmiControlService.class);
                    timingsTraceAndSlog.traceEnd();
                }
                if (!this.mPackageManager.hasSystemFeature("android.software.live_tv") || this.mPackageManager.hasSystemFeature("android.software.leanback")) {
                    timingsTraceAndSlog.traceBegin("StartTvInteractiveAppManager");
                    this.mSystemServiceManager.startService(TvInteractiveAppManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                }
                if (!this.mPackageManager.hasSystemFeature("android.software.live_tv") || this.mPackageManager.hasSystemFeature("android.software.leanback")) {
                    timingsTraceAndSlog.traceBegin("StartTvInputManager");
                    this.mSystemServiceManager.startService(TvInputManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                }
                if (this.mPackageManager.hasSystemFeature("android.hardware.tv.tuner")) {
                    timingsTraceAndSlog.traceBegin("StartTunerResourceManager");
                    this.mSystemServiceManager.startService(TunerResourceManagerService.class);
                    timingsTraceAndSlog.traceEnd();
                }
                if (this.mPackageManager.hasSystemFeature("android.software.picture_in_picture")) {
                    timingsTraceAndSlog.traceBegin("StartMediaResourceMonitor");
                    this.mSystemServiceManager.startService(MEDIA_RESOURCE_MONITOR_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                }
                if (this.mPackageManager.hasSystemFeature("android.software.leanback")) {
                    timingsTraceAndSlog.traceBegin("StartTvRemoteService");
                    this.mSystemServiceManager.startService(TvRemoteService.class);
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartMediaRouterService");
                MediaRouterService mediaRouterService222222 = new MediaRouterService(context);
                ServiceManager.addService("media_router", mediaRouterService222222);
                timingsTraceAndSlog.traceEnd();
                hasSystemFeature = this.mPackageManager.hasSystemFeature("android.hardware.biometrics.face");
                hasSystemFeature2 = this.mPackageManager.hasSystemFeature("android.hardware.biometrics.iris");
                hasSystemFeature3 = this.mPackageManager.hasSystemFeature("android.hardware.fingerprint");
                if (hasSystemFeature) {
                    timingsTraceAndSlog.traceBegin("StartFaceSensor");
                    timingsTraceAndSlog.traceEnd();
                }
                if (hasSystemFeature2) {
                    timingsTraceAndSlog.traceBegin("StartIrisSensor");
                    this.mSystemServiceManager.startService(IrisService.class);
                    timingsTraceAndSlog.traceEnd();
                }
                if (hasSystemFeature3) {
                    timingsTraceAndSlog.traceBegin("StartFingerprintSensor");
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartBiometricService");
                this.mSystemServiceManager.startService(BiometricService.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartAuthService");
                this.mSystemServiceManager.startService(AuthService.class);
                timingsTraceAndSlog.traceEnd();
                if (!hasSystemFeature4) {
                    timingsTraceAndSlog.traceBegin("StartDynamicCodeLoggingService");
                    try {
                        DynamicCodeLoggingService.schedule(context);
                    } catch (Throwable th25) {
                        reportWtf("starting DynamicCodeLoggingService", th25);
                    }
                    timingsTraceAndSlog.traceEnd();
                }
                if (!hasSystemFeature4) {
                    timingsTraceAndSlog.traceBegin("StartPruneInstantAppsJobService");
                    try {
                        PruneInstantAppsJobService.schedule(context);
                    } catch (Throwable th26) {
                        reportWtf("StartPruneInstantAppsJobService", th26);
                    }
                    timingsTraceAndSlog.traceEnd();
                }
                timingsTraceAndSlog.traceBegin("StartShortcutServiceLifecycle");
                this.mSystemServiceManager.startService(ShortcutService.Lifecycle.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartLauncherAppsService");
                this.mSystemServiceManager.startService(LauncherAppsService.class);
                timingsTraceAndSlog.traceEnd();
                Slog.i(TAG, "Dynamic filter service");
                this.mSystemServerExt.startDynamicFilterService(this.mSystemServiceManager);
                timingsTraceAndSlog.traceBegin("StartCrossProfileAppsService");
                this.mSystemServiceManager.startService(CrossProfileAppsService.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartPeopleService");
                this.mSystemServiceManager.startService(PeopleService.class);
                timingsTraceAndSlog.traceEnd();
                this.mSystemServerExt.addLinearmotorVibratorService(context);
                this.mSystemServerExt.addStorageHealthInfoService(context);
                timingsTraceAndSlog.traceBegin("StartMediaMetricsManager");
                this.mSystemServiceManager.startService(MediaMetricsManagerService.class);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartBackgroundInstallControlService");
                this.mSystemServiceManager.startService(BackgroundInstallControlService.class);
                timingsTraceAndSlog.traceEnd();
                mediaRouterService = mediaRouterService222222;
                lifecycle2 = lifecycle;
                vcnManagementService = vcnManagementService2222;
                vpnManagerService = vpnManagerService2222;
                iLockSettings3 = iLockSettings2;
                networkTimeUpdateService2 = networkTimeUpdateService;
            }
            timingsTraceAndSlog.traceBegin("StartMediaProjectionManager");
            this.mSystemServiceManager.startService(MediaProjectionManagerService.class);
            timingsTraceAndSlog.traceEnd();
            if (hasSystemFeature4) {
                timingsTraceAndSlog.traceBegin("StartWearPowerService");
                this.mSystemServiceManager.startService(WEAR_POWER_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartHealthService");
                this.mSystemServiceManager.startService(HEALTH_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartWearConnectivityService");
                this.mSystemServiceManager.startService(WEAR_CONNECTIVITY_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartWearDisplayService");
                this.mSystemServiceManager.startService(WEAR_DISPLAY_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartWearTimeService");
                this.mSystemServiceManager.startService(WEAR_TIME_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
                timingsTraceAndSlog.traceBegin("StartWearGlobalActionsService");
                this.mSystemServiceManager.startService(WEAR_GLOBAL_ACTIONS_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
            }
            if (!this.mPackageManager.hasSystemFeature("android.software.slices_disabled")) {
                timingsTraceAndSlog.traceBegin("StartSliceManagerService");
                this.mSystemServiceManager.startService(SLICE_MANAGER_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
            }
            if (context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
                timingsTraceAndSlog.traceBegin("StartIoTSystemService");
                this.mSystemServiceManager.startService(IOT_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
            }
            timingsTraceAndSlog.traceBegin("StartStatsCompanion");
            this.mSystemServiceManager.startServiceFromJar(STATS_COMPANION_LIFECYCLE_CLASS, STATS_COMPANION_APEX_PATH);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartRebootReadinessManagerService");
            this.mSystemServiceManager.startServiceFromJar(REBOOT_READINESS_LIFECYCLE_CLASS, SCHEDULING_APEX_PATH);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartStatsPullAtomService");
            this.mSystemServiceManager.startService(STATS_PULL_ATOM_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StatsBootstrapAtomService");
            this.mSystemServiceManager.startService(STATS_BOOTSTRAP_ATOM_SERVICE_LIFECYCLE_CLASS);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartIncidentCompanionService");
            this.mSystemServiceManager.startService(IncidentCompanionService.class);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StarSdkSandboxManagerService");
            this.mSystemServiceManager.startService(SDK_SANDBOX_MANAGER_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartAdServicesManagerService");
            startService = this.mSystemServiceManager.startService(AD_SERVICES_MANAGER_SERVICE_CLASS);
            if (startService instanceof Dumpable) {
                this.mDumper.addDumpable((Dumpable) startService);
            }
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartOnDevicePersonalizationSystemService");
            this.mSystemServiceManager.startService(ON_DEVICE_PERSONALIZATION_SYSTEM_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            if (detectSafeMode) {
                this.mActivityManagerService.enterSafeMode();
            }
            if (this.mPackageManager.hasSystemFeature("android.hardware.telephony")) {
                timingsTraceAndSlog.traceBegin("StartMmsService");
                MmsServiceBroker mmsServiceBroker2 = (MmsServiceBroker) this.mSystemServiceManager.startService(MmsServiceBroker.class);
                timingsTraceAndSlog.traceEnd();
                mmsServiceBroker = mmsServiceBroker2;
            } else {
                mmsServiceBroker = null;
            }
            if (this.mPackageManager.hasSystemFeature("android.software.autofill")) {
                timingsTraceAndSlog.traceBegin("StartAutoFillService");
                this.mSystemServiceManager.startService(AUTO_FILL_MANAGER_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
            }
            if (this.mPackageManager.hasSystemFeature("android.software.credentials")) {
                if (DeviceConfig.getBoolean("credential_manager", "enable_credential_manager", true)) {
                    timingsTraceAndSlog.traceBegin("StartCredentialManagerService");
                    this.mSystemServiceManager.startService(CREDENTIAL_MANAGER_SERVICE_CLASS);
                    timingsTraceAndSlog.traceEnd();
                } else {
                    Slog.d(TAG, "CredentialManager disabled.");
                }
            }
            if (deviceHasConfigString(context, R.string.config_screenshotErrorReceiverComponent)) {
                timingsTraceAndSlog.traceBegin("StartTranslationManagerService");
                this.mSystemServiceManager.startService(TRANSLATION_MANAGER_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
            } else {
                Slog.d(TAG, "TranslationService not defined by OEM");
            }
            timingsTraceAndSlog.traceBegin("StartSelectionToolbarManagerService");
            this.mSystemServiceManager.startService(SELECTION_TOOLBAR_MANAGER_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartClipboardService");
            this.mSystemServiceManager.startService(ClipboardService.class);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("AppServiceManager");
            this.mSystemServiceManager.startService(AppBindingService.Lifecycle.class);
            timingsTraceAndSlog.traceEnd();
            this.mSystemServerExt.startOtherServices();
            this.mSocExt.startOtherServices();
            timingsTraceAndSlog.traceBegin("startTracingServiceProxy");
            this.mSystemServiceManager.startService(TracingServiceProxy.class);
            timingsTraceAndSlog.traceEnd();
            this.mSystemServerExt.linearVibratorSystemReady();
            timingsTraceAndSlog.traceBegin("MakeLockSettingsServiceReady");
            if (iLockSettings3 != null) {
                try {
                    iLockSettings3.systemReady();
                } catch (Throwable th27) {
                    reportWtf("making Lock Settings Service ready", th27);
                }
            }
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartBootPhaseLockSettingsReady");
            this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, SystemService.PHASE_LOCK_SETTINGS_READY);
            timingsTraceAndSlog.traceEnd();
            createInstance = HsumBootUserInitializer.createInstance(this.mActivityManagerService, this.mPackageManagerService, this.mContentResolver, context.getResources().getBoolean(17891723));
            if (createInstance != null) {
                timingsTraceAndSlog.traceBegin("HsumBootUserInitializer.init");
                createInstance.init(timingsTraceAndSlog);
                timingsTraceAndSlog.traceEnd();
            }
            timingsTraceAndSlog.traceBegin("StartBootPhaseSystemServicesReady");
            this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, 500);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("MakeWindowManagerServiceReady");
            Slog.i(TAG, "wms systemReady");
            main.systemReady();
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("RegisterLogMteState");
            LogMteState.register(context);
            timingsTraceAndSlog.traceEnd();
            synchronized (SystemService.class) {
                LinkedList<Pair<String, ApplicationErrorReport.CrashInfo>> linkedList = sPendingWtfs;
                if (linkedList != null) {
                    this.mActivityManagerService.schedulePendingSystemServerWtfs(linkedList);
                    sPendingWtfs = null;
                }
            }
            if (detectSafeMode) {
                this.mActivityManagerService.showSafeModeOverlay();
            }
            Configuration computeNewConfiguration = main.computeNewConfiguration(0);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            context.getDisplay().getMetrics(displayMetrics);
            context.getResources().updateConfiguration(computeNewConfiguration, displayMetrics);
            Resources.Theme theme = context.getTheme();
            if (theme.getChangingConfigurations() != 0) {
                theme.rebase();
            }
            timingsTraceAndSlog.traceBegin("StartPermissionPolicyService");
            this.mSystemServiceManager.startService(PermissionPolicyService.class);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("MakePackageManagerServiceReady");
            Slog.i(TAG, "Package systemReady");
            this.mPackageManagerService.systemReady();
            timingsTraceAndSlog.traceEnd();
            this.mSystemServerExt.dynamicFilterServiceSystemReady(timingsTraceAndSlog);
            timingsTraceAndSlog.traceBegin("MakeDisplayManagerServiceReady");
            try {
                Slog.i(TAG, "DisplayManager systemReady");
                this.mDisplayManagerService.systemReady(detectSafeMode);
            } catch (Throwable th28) {
                reportWtf("making Display Manager Service ready", th28);
            }
            timingsTraceAndSlog.traceEnd();
            if (!z) {
                this.mSystemServerExt.addCrossDeviceService(this.mSystemContext, this.mActivityManagerService, timingsTraceAndSlog);
            }
            this.mSystemServiceManager.setSafeMode(detectSafeMode);
            timingsTraceAndSlog.traceBegin("StartDeviceSpecificServices");
            String[] stringArray = this.mSystemContext.getResources().getStringArray(R.array.config_displayWhiteBalanceLowLightAmbientBiases);
            int length = stringArray.length;
            int i = 0;
            while (i < length) {
                String str = stringArray[i];
                timingsTraceAndSlog.traceBegin("StartDeviceSpecificServices " + str);
                try {
                    this.mSystemServiceManager.startService(str);
                    strArr = stringArray;
                } catch (Throwable th29) {
                    StringBuilder sb3 = new StringBuilder();
                    strArr = stringArray;
                    sb3.append("starting ");
                    sb3.append(str);
                    reportWtf(sb3.toString(), th29);
                }
                timingsTraceAndSlog.traceEnd();
                i++;
                stringArray = strArr;
            }
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin(GameManagerService.TAG);
            this.mSystemServiceManager.startService(GAME_MANAGER_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            if (context.getPackageManager().hasSystemFeature("android.hardware.uwb")) {
                timingsTraceAndSlog.traceBegin("UwbService");
                this.mSystemServiceManager.startServiceFromJar(UWB_SERVICE_CLASS, UWB_APEX_SERVICE_JAR_PATH);
                timingsTraceAndSlog.traceEnd();
            }
            timingsTraceAndSlog.traceBegin("StartBootPhaseDeviceSpecificServicesReady");
            this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, SystemService.PHASE_DEVICE_SPECIFIC_SERVICES_READY);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartSafetyCenterService");
            this.mSystemServiceManager.startService(SAFETY_CENTER_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("AppSearchModule");
            this.mSystemServiceManager.startService(APPSEARCH_MODULE_LIFECYCLE_CLASS);
            timingsTraceAndSlog.traceEnd();
            if (SystemProperties.getBoolean("ro.config.isolated_compilation_enabled", false)) {
                timingsTraceAndSlog.traceBegin("IsolatedCompilationService");
                this.mSystemServiceManager.startService(ISOLATED_COMPILATION_SERVICE_CLASS);
                timingsTraceAndSlog.traceEnd();
            }
            this.mSystemServerExt.systemReady();
            timingsTraceAndSlog.traceBegin("StartMediaCommunicationService");
            this.mSystemServiceManager.startService(MEDIA_COMMUNICATION_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("AppCompatOverridesService");
            this.mSystemServiceManager.startService(APP_COMPAT_OVERRIDES_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("HealthConnectManagerService");
            this.mSystemServiceManager.startService(HEALTHCONNECT_MANAGER_SERVICE_CLASS);
            timingsTraceAndSlog.traceEnd();
            if (this.mPackageManager.hasSystemFeature("android.software.device_lock")) {
                timingsTraceAndSlog.traceBegin("DeviceLockService");
                this.mSystemServiceManager.startServiceFromJar(DEVICE_LOCK_SERVICE_CLASS, DEVICE_LOCK_APEX_PATH);
                timingsTraceAndSlog.traceEnd();
            }
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            Slog.i(TAG, "Ams systemReady");
            final NetworkManagementService networkManagementService3 = networkManagementService2;
            final NetworkPolicyManagerService networkPolicyManagerService3 = networkPolicyManagerService2;
            final VpnManagerService vpnManagerService3 = vpnManagerService;
            final VcnManagementService vcnManagementService3 = vcnManagementService;
            final CountryDetectorService countryDetectorService3 = r23;
            final NetworkTimeUpdateService networkTimeUpdateService3 = networkTimeUpdateService2;
            final MediaRouterService mediaRouterService3 = mediaRouterService;
            final MmsServiceBroker mmsServiceBroker3 = mmsServiceBroker;
            this.mActivityManagerService.systemReady(new Runnable() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    SystemServer.this.lambda$startOtherServices$6(timingsTraceAndSlog, lifecycle2, hasSystemFeature4, context, detectSafeMode, connectivityManager, networkManagementService3, networkPolicyManagerService3, vpnManagerService3, vcnManagementService3, createInstance, countryDetectorService3, networkTimeUpdateService3, inputManagerService, telephonyRegistry, mediaRouterService3, mmsServiceBroker3);
                }
            }, timingsTraceAndSlog);
            timingsTraceAndSlog.traceBegin("LockSettingsThirdPartyAppsStarted");
            LockSettingsInternal lockSettingsInternal = (LockSettingsInternal) LocalServices.getService(LockSettingsInternal.class);
            if (lockSettingsInternal != null) {
                lockSettingsInternal.onThirdPartyAppsStarted();
            }
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("StartSystemUI");
            try {
                startSystemUi(context, main);
            } catch (Throwable th30) {
                reportWtf("starting System UI", th30);
            }
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceEnd();
        } catch (Throwable th31) {
            Slog.e("System", "******************************************");
            Slog.e("System", "************ Failure starting core service");
            throw th31;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startOtherServices$1() {
        try {
            Slog.i(TAG, "SecondaryZygotePreload");
            TimingsTraceAndSlog newAsyncLog = TimingsTraceAndSlog.newAsyncLog();
            newAsyncLog.traceBegin("SecondaryZygotePreload");
            String[] strArr = Build.QCOM_TANGO_ON_64BIT_ONLY_CHIP ? Build.QCOM_TANGO_SUPPORTED_32_BIT_ABIS : Build.SUPPORTED_32_BIT_ABIS;
            if (strArr.length > 0 && !Process.ZYGOTE_PROCESS.preloadDefault(strArr[0])) {
                Slog.e(TAG, "Unable to preload default resources for secondary");
            }
            newAsyncLog.traceEnd();
        } catch (Exception e) {
            Slog.e(TAG, "Exception preloading default resources", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startOtherServices$2() {
        TimingsTraceAndSlog newAsyncLog = TimingsTraceAndSlog.newAsyncLog();
        newAsyncLog.traceBegin(START_SENSOR_MANAGER_SERVICE);
        startISensorManagerService();
        newAsyncLog.traceEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startOtherServices$3() {
        TimingsTraceAndSlog newAsyncLog = TimingsTraceAndSlog.newAsyncLog();
        newAsyncLog.traceBegin(START_HIDL_SERVICES);
        startHidlServices();
        newAsyncLog.traceEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOtherServices$6(TimingsTraceAndSlog timingsTraceAndSlog, DevicePolicyManagerService.Lifecycle lifecycle, boolean z, Context context, boolean z2, ConnectivityManager connectivityManager, NetworkManagementService networkManagementService, NetworkPolicyManagerService networkPolicyManagerService, VpnManagerService vpnManagerService, VcnManagementService vcnManagementService, HsumBootUserInitializer hsumBootUserInitializer, CountryDetectorService countryDetectorService, NetworkTimeUpdateService networkTimeUpdateService, InputManagerService inputManagerService, TelephonyRegistry telephonyRegistry, MediaRouterService mediaRouterService, MmsServiceBroker mmsServiceBroker) {
        Slog.i(TAG, "Making services ready");
        timingsTraceAndSlog.traceBegin("StartActivityManagerReadyPhase");
        this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, 550);
        timingsTraceAndSlog.traceEnd();
        this.mSystemServerExt.systemRunning();
        timingsTraceAndSlog.traceBegin("StartObservingNativeCrashes");
        try {
            this.mActivityManagerService.startObservingNativeCrashes();
        } catch (Throwable th) {
            reportWtf("observing native crashes", th);
        }
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("RegisterAppOpsPolicy");
        try {
            this.mActivityManagerService.setAppOpsPolicy(new AppOpsPolicy(this.mSystemContext));
        } catch (Throwable th2) {
            reportWtf("registering app ops policy", th2);
        }
        timingsTraceAndSlog.traceEnd();
        Future<?> submit = this.mWebViewUpdateService != null ? SystemServerInitThreadPool.submit(new Runnable() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SystemServer.this.lambda$startOtherServices$4();
            }
        }, "WebViewFactoryPreparation") : null;
        if (this.mPackageManager.hasSystemFeature("android.hardware.type.automotive")) {
            timingsTraceAndSlog.traceBegin("StartCarServiceHelperService");
            DevicePolicySafetyChecker startService = this.mSystemServiceManager.startService(CAR_SERVICE_HELPER_SERVICE_CLASS);
            if (startService instanceof Dumpable) {
                this.mDumper.addDumpable((Dumpable) startService);
            }
            if (startService instanceof DevicePolicySafetyChecker) {
                lifecycle.setDevicePolicySafetyChecker(startService);
            }
            timingsTraceAndSlog.traceEnd();
        }
        if (z) {
            timingsTraceAndSlog.traceBegin("StartWearService");
            String string = context.getString(R.string.editTextMenuTitle);
            if (!TextUtils.isEmpty(string)) {
                ComponentName unflattenFromString = ComponentName.unflattenFromString(string);
                if (unflattenFromString != null) {
                    Intent intent = new Intent();
                    intent.setComponent(unflattenFromString);
                    intent.addFlags(256);
                    context.startServiceAsUser(intent, UserHandle.SYSTEM);
                } else {
                    Slog.d(TAG, "Null wear service component name.");
                }
            }
            timingsTraceAndSlog.traceEnd();
        }
        if (z2) {
            timingsTraceAndSlog.traceBegin("EnableAirplaneModeInSafeMode");
            try {
                connectivityManager.setAirplaneMode(true);
            } catch (Throwable th3) {
                reportWtf("enabling Airplane Mode during Safe Mode bootup", th3);
            }
            timingsTraceAndSlog.traceEnd();
        }
        timingsTraceAndSlog.traceBegin("MakeNetworkManagementServiceReady");
        if (networkManagementService != null) {
            try {
                networkManagementService.systemReady();
            } catch (Throwable th4) {
                reportWtf("making Network Managment Service ready", th4);
            }
        }
        CountDownLatch networkScoreAndNetworkManagementServiceReady = networkPolicyManagerService != null ? networkPolicyManagerService.networkScoreAndNetworkManagementServiceReady() : null;
        timingsTraceAndSlog.traceEnd();
        ((IPackageManagerServiceUtilsExt) ExtLoader.type(IPackageManagerServiceUtilsExt.class).create()).addBootEvent("SystemServer:NetworkStatsService systemReady");
        timingsTraceAndSlog.traceBegin("MakeConnectivityServiceReady");
        if (connectivityManager != null) {
            try {
                connectivityManager.systemReady();
            } catch (Throwable th5) {
                reportWtf("making Connectivity Service ready", th5);
            }
        }
        timingsTraceAndSlog.traceEnd();
        ((IPackageManagerServiceUtilsExt) ExtLoader.type(IPackageManagerServiceUtilsExt.class).create()).addBootEvent("SystemServer:ConnectivityService systemReady");
        timingsTraceAndSlog.traceBegin("MakeVpnManagerServiceReady");
        if (vpnManagerService != null) {
            try {
                vpnManagerService.systemReady();
            } catch (Throwable th6) {
                reportWtf("making VpnManagerService ready", th6);
            }
        }
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("MakeVcnManagementServiceReady");
        if (vcnManagementService != null) {
            try {
                vcnManagementService.systemReady();
            } catch (Throwable th7) {
                reportWtf("making VcnManagementService ready", th7);
            }
        }
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("MakeNetworkPolicyServiceReady");
        if (networkPolicyManagerService != null) {
            try {
                networkPolicyManagerService.systemReady(networkScoreAndNetworkManagementServiceReady);
            } catch (Throwable th8) {
                reportWtf("making Network Policy Service ready", th8);
            }
        }
        timingsTraceAndSlog.traceEnd();
        ((IPackageManagerServiceUtilsExt) ExtLoader.type(IPackageManagerServiceUtilsExt.class).create()).addBootEvent("SystemServer:NetworkPolicyManagerServ systemReady");
        this.mPackageManagerService.waitForAppDataPrepared();
        timingsTraceAndSlog.traceBegin("PhaseThirdPartyAppsCanStart");
        if (submit != null) {
            ConcurrentUtils.waitForFutureNoInterrupt(submit, "WebViewFactoryPreparation");
        }
        this.mSystemServiceManager.startBootPhase(timingsTraceAndSlog, 600);
        timingsTraceAndSlog.traceEnd();
        if (hsumBootUserInitializer != null) {
            timingsTraceAndSlog.traceBegin("HsumBootUserInitializer.systemRunning");
            hsumBootUserInitializer.systemRunning(timingsTraceAndSlog);
            timingsTraceAndSlog.traceEnd();
        }
        timingsTraceAndSlog.traceBegin("StartNetworkStack");
        try {
            NetworkStackClient.getInstance().start();
        } catch (Throwable th9) {
            reportWtf("starting Network Stack", th9);
        }
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("StartTethering");
        try {
            ConnectivityModuleConnector.getInstance().startModuleService(TETHERING_CONNECTOR_CLASS, "android.permission.MAINLINE_NETWORK_STACK", new ConnectivityModuleConnector.ModuleServiceCallback() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda2
                @Override // android.net.ConnectivityModuleConnector.ModuleServiceCallback
                public final void onModuleServiceConnected(IBinder iBinder) {
                    ServiceManager.addService("tethering", iBinder, false, 6);
                }
            });
        } catch (Throwable th10) {
            reportWtf("starting Tethering", th10);
        }
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("MakeCountryDetectionServiceReady");
        if (countryDetectorService != null) {
            try {
                countryDetectorService.systemRunning();
            } catch (Throwable th11) {
                reportWtf("Notifying CountryDetectorService running", th11);
            }
        }
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("MakeNetworkTimeUpdateReady");
        if (networkTimeUpdateService != null) {
            try {
                networkTimeUpdateService.systemRunning();
            } catch (Throwable th12) {
                reportWtf("Notifying NetworkTimeService running", th12);
            }
        }
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("MakeInputManagerServiceReady");
        if (inputManagerService != null) {
            try {
                inputManagerService.systemRunning();
            } catch (Throwable th13) {
                reportWtf("Notifying InputManagerService running", th13);
            }
        }
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("MakeTelephonyRegistryReady");
        if (telephonyRegistry != null) {
            try {
                telephonyRegistry.systemRunning();
            } catch (Throwable th14) {
                reportWtf("Notifying TelephonyRegistry running", th14);
            }
        }
        timingsTraceAndSlog.traceEnd();
        timingsTraceAndSlog.traceBegin("MakeMediaRouterServiceReady");
        if (mediaRouterService != null) {
            try {
                mediaRouterService.systemRunning();
            } catch (Throwable th15) {
                reportWtf("Notifying MediaRouterService running", th15);
            }
        }
        timingsTraceAndSlog.traceEnd();
        if (this.mPackageManager.hasSystemFeature("android.hardware.telephony")) {
            timingsTraceAndSlog.traceBegin("MakeMmsServiceReady");
            if (mmsServiceBroker != null) {
                try {
                    mmsServiceBroker.systemRunning();
                } catch (Throwable th16) {
                    reportWtf("Notifying MmsService running", th16);
                }
            }
            timingsTraceAndSlog.traceEnd();
        }
        timingsTraceAndSlog.traceBegin("IncidentDaemonReady");
        try {
            IIncidentManager asInterface = IIncidentManager.Stub.asInterface(ServiceManager.getService("incident"));
            if (asInterface != null) {
                asInterface.systemRunning();
            }
        } catch (Throwable th17) {
            reportWtf("Notifying incident daemon running", th17);
        }
        timingsTraceAndSlog.traceEnd();
        ((IPackageManagerServiceUtilsExt) ExtLoader.type(IPackageManagerServiceUtilsExt.class).create()).addBootEvent("SystemServer:PhaseThirdPartyAppsCanStart");
        if (this.mIncrementalServiceHandle != 0) {
            timingsTraceAndSlog.traceBegin("MakeIncrementalServiceReady");
            setIncrementalServiceSystemReady(this.mIncrementalServiceHandle);
            timingsTraceAndSlog.traceEnd();
        }
        timingsTraceAndSlog.traceBegin("OdsignStatsLogger");
        try {
            OdsignStatsLogger.triggerStatsWrite();
        } catch (Throwable th18) {
            reportWtf("Triggering OdsignStatsLogger", th18);
        }
        timingsTraceAndSlog.traceEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOtherServices$4() {
        Slog.i(TAG, "WebViewFactoryPreparation");
        TimingsTraceAndSlog newAsyncLog = TimingsTraceAndSlog.newAsyncLog();
        newAsyncLog.traceBegin("WebViewFactoryPreparation");
        ConcurrentUtils.waitForFutureNoInterrupt(this.mZygotePreload, "Zygote preload");
        this.mZygotePreload = null;
        this.mWebViewUpdateService.prepareWebViewInSystemServer();
        newAsyncLog.traceEnd();
    }

    private void startApexServices(TimingsTraceAndSlog timingsTraceAndSlog) {
        timingsTraceAndSlog.traceBegin("startApexServices");
        for (ApexSystemServiceInfo apexSystemServiceInfo : ApexManager.getInstance().getApexSystemServices()) {
            String name = apexSystemServiceInfo.getName();
            String jarPath = apexSystemServiceInfo.getJarPath();
            timingsTraceAndSlog.traceBegin("starting " + name);
            if (TextUtils.isEmpty(jarPath)) {
                this.mSystemServiceManager.startService(name);
            } else {
                this.mSystemServiceManager.startServiceFromJar(name, jarPath);
            }
            timingsTraceAndSlog.traceEnd();
        }
        this.mSystemServiceManager.sealStartedServices();
        timingsTraceAndSlog.traceEnd();
    }

    private void updateWatchdogTimeout(TimingsTraceAndSlog timingsTraceAndSlog) {
        timingsTraceAndSlog.traceBegin("UpdateWatchdogTimeout");
        Watchdog.getInstance().registerSettingsObserver(this.mSystemContext);
        timingsTraceAndSlog.traceEnd();
    }

    private boolean deviceHasConfigString(Context context, int i) {
        return !TextUtils.isEmpty(context.getString(i));
    }

    private void startSystemCaptionsManagerService(Context context, TimingsTraceAndSlog timingsTraceAndSlog) {
        if (!deviceHasConfigString(context, R.string.config_retailDemoPackageSignature)) {
            Slog.d(TAG, "SystemCaptionsManagerService disabled because resource is not overlaid");
            return;
        }
        timingsTraceAndSlog.traceBegin("StartSystemCaptionsManagerService");
        this.mSystemServiceManager.startService(SYSTEM_CAPTIONS_MANAGER_SERVICE_CLASS);
        timingsTraceAndSlog.traceEnd();
    }

    private void startTextToSpeechManagerService(Context context, TimingsTraceAndSlog timingsTraceAndSlog) {
        timingsTraceAndSlog.traceBegin("StartTextToSpeechManagerService");
        this.mSystemServiceManager.startService(TEXT_TO_SPEECH_MANAGER_SERVICE_CLASS);
        timingsTraceAndSlog.traceEnd();
    }

    private void startContentCaptureService(Context context, TimingsTraceAndSlog timingsTraceAndSlog) {
        boolean z;
        ActivityManagerService activityManagerService;
        String property = DeviceConfig.getProperty("content_capture", "service_explicitly_enabled");
        if (property == null || property.equalsIgnoreCase("default")) {
            z = false;
        } else {
            z = Boolean.parseBoolean(property);
            if (z) {
                Slog.d(TAG, "ContentCaptureService explicitly enabled by DeviceConfig");
            } else {
                Slog.d(TAG, "ContentCaptureService explicitly disabled by DeviceConfig");
                return;
            }
        }
        if (!z && !deviceHasConfigString(context, R.string.config_mms_user_agent_profile_url)) {
            Slog.d(TAG, "ContentCaptureService disabled because resource is not overlaid");
            return;
        }
        timingsTraceAndSlog.traceBegin("StartContentCaptureService");
        this.mSystemServiceManager.startService(CONTENT_CAPTURE_MANAGER_SERVICE_CLASS);
        ContentCaptureManagerInternal contentCaptureManagerInternal = (ContentCaptureManagerInternal) LocalServices.getService(ContentCaptureManagerInternal.class);
        if (contentCaptureManagerInternal != null && (activityManagerService = this.mActivityManagerService) != null) {
            activityManagerService.setContentCaptureManager(contentCaptureManagerInternal);
        }
        timingsTraceAndSlog.traceEnd();
    }

    private void startAttentionService(Context context, TimingsTraceAndSlog timingsTraceAndSlog) {
        if (!AttentionManagerService.isServiceConfigured(context)) {
            Slog.d(TAG, "AttentionService is not configured on this device");
            return;
        }
        timingsTraceAndSlog.traceBegin("StartAttentionManagerService");
        this.mSystemServiceManager.startService(AttentionManagerService.class);
        timingsTraceAndSlog.traceEnd();
    }

    private void startRotationResolverService(Context context, TimingsTraceAndSlog timingsTraceAndSlog) {
        if (!RotationResolverManagerService.isServiceConfigured(context)) {
            Slog.d(TAG, "RotationResolverService is not configured on this device");
            return;
        }
        timingsTraceAndSlog.traceBegin("StartRotationResolverService");
        this.mSystemServiceManager.startService(RotationResolverManagerService.class);
        timingsTraceAndSlog.traceEnd();
    }

    private void startAmbientContextService(TimingsTraceAndSlog timingsTraceAndSlog) {
        timingsTraceAndSlog.traceBegin("StartAmbientContextService");
        this.mSystemServiceManager.startService(AmbientContextManagerService.class);
        timingsTraceAndSlog.traceEnd();
    }

    private void startWearableSensingService(TimingsTraceAndSlog timingsTraceAndSlog) {
        timingsTraceAndSlog.traceBegin("startWearableSensingService");
        this.mSystemServiceManager.startService(WearableSensingManagerService.class);
        timingsTraceAndSlog.traceEnd();
    }

    private static void startSystemUi(Context context, WindowManagerService windowManagerService) {
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        Intent intent = new Intent();
        intent.setComponent(packageManagerInternal.getSystemUiServiceComponent());
        intent.addFlags(256);
        context.startServiceAsUser(intent, UserHandle.SYSTEM);
        windowManagerService.onSystemUiStarted();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean handleEarlySystemWtf(IBinder iBinder, String str, boolean z, ApplicationErrorReport.ParcelableCrashInfo parcelableCrashInfo, int i) {
        int myPid = Process.myPid();
        com.android.server.am.EventLogTags.writeAmWtf(UserHandle.getUserId(1000), myPid, "system_server", -1, str, parcelableCrashInfo.exceptionMessage);
        FrameworkStatsLog.write(80, 1000, str, "system_server", myPid, 3);
        synchronized (SystemServer.class) {
            if (sPendingWtfs == null) {
                sPendingWtfs = new LinkedList<>();
            }
            sPendingWtfs.add(new Pair<>(str, parcelableCrashInfo));
        }
        return false;
    }
}
