package com.android.server;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.VersionedPackage;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.os.PowerManager;
import android.os.RecoverySystem;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.ExceptionUtils;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.PackageWatchdog;
import com.android.server.am.SettingsToPropertiesMapper;
import com.android.server.pm.PackageManagerServiceUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class RescueParty {

    @VisibleForTesting
    static final long DEFAULT_FACTORY_RESET_THROTTLE_DURATION_MIN = 10;

    @VisibleForTesting
    static final long DEFAULT_OBSERVING_DURATION_MS = TimeUnit.DAYS.toMillis(2);

    @VisibleForTesting
    static final int DEVICE_CONFIG_RESET_MODE = 4;

    @VisibleForTesting
    static final int LEVEL_FACTORY_RESET = 5;

    @VisibleForTesting
    static final int LEVEL_NONE = 0;

    @VisibleForTesting
    static final int LEVEL_RESET_SETTINGS_TRUSTED_DEFAULTS = 3;

    @VisibleForTesting
    static final int LEVEL_RESET_SETTINGS_UNTRUSTED_CHANGES = 2;

    @VisibleForTesting
    static final int LEVEL_RESET_SETTINGS_UNTRUSTED_DEFAULTS = 1;

    @VisibleForTesting
    static final int LEVEL_WARM_REBOOT = 4;
    private static final String NAME = "rescue-party-observer";

    @VisibleForTesting
    static final String NAMESPACE_CONFIGURATION = "configuration";

    @VisibleForTesting
    static final String NAMESPACE_TO_PACKAGE_MAPPING_FLAG = "namespace_to_package_mapping";
    private static final int PERSISTENT_MASK = 9;
    static final String PROP_ATTEMPTING_FACTORY_RESET = "sys.attempting_factory_reset";
    static final String PROP_ATTEMPTING_REBOOT = "sys.attempting_reboot";
    private static final String PROP_DEVICE_CONFIG_DISABLE_FLAG = "persist.device_config.configuration.disable_rescue_party";
    private static final String PROP_DISABLE_FACTORY_RESET_FLAG = "persist.device_config.configuration.disable_rescue_party_factory_reset";
    private static final String PROP_DISABLE_RESCUE = "persist.sys.disable_rescue";

    @VisibleForTesting
    static final String PROP_ENABLE_RESCUE = "persist.sys.enable_rescue";
    static final String PROP_LAST_FACTORY_RESET_TIME_MS = "persist.sys.last_factory_reset";
    static final String PROP_MAX_RESCUE_LEVEL_ATTEMPTED = "sys.max_rescue_level_attempted";

    @VisibleForTesting
    static final String PROP_RESCUE_BOOT_COUNT = "sys.rescue_boot_count";
    private static final String PROP_THROTTLE_DURATION_MIN_FLAG = "persist.device_config.configuration.rescue_party_throttle_duration_min";
    private static final String PROP_VIRTUAL_DEVICE = "ro.hardware.virtual_device";

    @VisibleForTesting
    static final String TAG = "RescueParty";
    private static IRescuePartyExt mRspExt;

    /* renamed from: -$$Nest$smisDisabled, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m337$$Nest$smisDisabled() {
        return isDisabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int mapRescueLevelToUserImpact(int i) {
        if (i == 1 || i == 2) {
            return 10;
        }
        if (i == 3 || i == 4) {
            return 50;
        }
        return i != 5 ? 0 : 100;
    }

    public static void registerHealthObserver(Context context) {
        PackageWatchdog.getInstance(context).registerHealthObserver(RescuePartyObserver.getInstance(context));
    }

    private static boolean isDisabled() {
        if (SystemProperties.getBoolean(PROP_ENABLE_RESCUE, false)) {
            return false;
        }
        if (SystemProperties.getBoolean(PROP_DEVICE_CONFIG_DISABLE_FLAG, false)) {
            Slog.v(TAG, "Disabled because of DeviceConfig flag");
            return true;
        }
        if (Build.IS_ENG) {
            Slog.v(TAG, "Disabled because of eng build");
            return true;
        }
        if (Build.IS_USERDEBUG && isUsbActive()) {
            Slog.v(TAG, "Disabled because of active USB connection");
            return true;
        }
        if (!SystemProperties.getBoolean(PROP_DISABLE_RESCUE, false)) {
            return false;
        }
        Slog.v(TAG, "Disabled because of manual property");
        return true;
    }

    public static boolean isAttemptingFactoryReset() {
        return isFactoryResetPropertySet() || isRebootPropertySet();
    }

    static boolean isFactoryResetPropertySet() {
        return SystemProperties.getBoolean(PROP_ATTEMPTING_FACTORY_RESET, false);
    }

    static boolean isRebootPropertySet() {
        return SystemProperties.getBoolean(PROP_ATTEMPTING_REBOOT, false);
    }

    public static void onSettingsProviderPublished(Context context) {
        handleNativeRescuePartyResets();
        DeviceConfig.setMonitorCallback(context.getContentResolver(), Executors.newSingleThreadExecutor(), new RescuePartyMonitorCallback(context));
    }

    public static void resetDeviceConfigForPackages(List<String> list) {
        if (list == null) {
            return;
        }
        ArraySet<String> arraySet = new ArraySet();
        Iterator<String> it = list.iterator();
        RescuePartyObserver instanceIfCreated = RescuePartyObserver.getInstanceIfCreated();
        if (instanceIfCreated != null) {
            while (it.hasNext()) {
                Set affectedNamespaceSet = instanceIfCreated.getAffectedNamespaceSet(it.next());
                if (affectedNamespaceSet != null) {
                    arraySet.addAll(affectedNamespaceSet);
                }
            }
        }
        Set<String> presetNamespacesForPackages = getPresetNamespacesForPackages(list);
        if (presetNamespacesForPackages != null) {
            arraySet.addAll(presetNamespacesForPackages);
        }
        for (String str : arraySet) {
            try {
                if (!DeviceConfig.setProperties(new DeviceConfig.Properties.Builder(str).build())) {
                    PackageManagerServiceUtils.logCriticalInfo(6, "Failed to clear properties under " + str + ". Running `device_config get_sync_disabled_for_tests` will confirm if config-bulk-update is enabled.");
                }
            } catch (DeviceConfig.BadConfigException unused) {
                PackageManagerServiceUtils.logCriticalInfo(5, "namespace " + str + " is already banned, skip reset.");
            }
        }
    }

    private static Set<String> getPresetNamespacesForPackages(List<String> list) {
        ArraySet arraySet = new ArraySet();
        try {
            try {
                String[] split = DeviceConfig.getString(NAMESPACE_CONFIGURATION, NAMESPACE_TO_PACKAGE_MAPPING_FLAG, "").split(",");
                for (int i = 0; i < split.length; i++) {
                    if (!TextUtils.isEmpty(split[i])) {
                        String[] split2 = split[i].split(":");
                        if (split2.length != 2) {
                            throw new RuntimeException("Invalid mapping entry: " + split[i]);
                        }
                        String str = split2[0];
                        if (list.contains(split2[1])) {
                            arraySet.add(str);
                        }
                    }
                }
                return arraySet;
            } catch (Exception e) {
                arraySet.clear();
                Slog.e(TAG, "Failed to read preset package to namespaces mapping.", e);
                return arraySet;
            }
        } catch (Throwable unused) {
            return arraySet;
        }
    }

    @VisibleForTesting
    static long getElapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class RescuePartyMonitorCallback implements DeviceConfig.MonitorCallback {
        Context mContext;

        RescuePartyMonitorCallback(Context context) {
            this.mContext = context;
        }

        public void onNamespaceUpdate(String str) {
            RescueParty.startObservingPackages(this.mContext, str);
        }

        public void onDeviceConfigAccess(String str, String str2) {
            RescuePartyObserver.getInstance(this.mContext).recordDeviceConfigAccess(str, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void startObservingPackages(Context context, String str) {
        RescuePartyObserver rescuePartyObserver = RescuePartyObserver.getInstance(context);
        Set callingPackagesSet = rescuePartyObserver.getCallingPackagesSet(str);
        if (callingPackagesSet == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(callingPackagesSet);
        Slog.i(TAG, "Starting to observe: " + arrayList + ", updated namespace: " + str);
        PackageWatchdog.getInstance(context).startObservingHealth(rescuePartyObserver, arrayList, DEFAULT_OBSERVING_DURATION_MS);
    }

    private static void handleNativeRescuePartyResets() {
        if (SettingsToPropertiesMapper.isNativeFlagsResetPerformed()) {
            String[] resetNativeCategories = SettingsToPropertiesMapper.getResetNativeCategories();
            for (int i = 0; i < resetNativeCategories.length; i++) {
                if (!NAMESPACE_CONFIGURATION.equals(resetNativeCategories[i])) {
                    DeviceConfig.resetToDefaults(4, resetNativeCategories[i]);
                }
            }
        }
    }

    private static int getMaxRescueLevel(boolean z) {
        return (!z || SystemProperties.getBoolean(PROP_DISABLE_FACTORY_RESET_FLAG, false)) ? 3 : 5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getRescueLevel(int i, boolean z) {
        if (i == 1) {
            return 1;
        }
        if (i == 2) {
            return 2;
        }
        if (i == 3) {
            return 3;
        }
        if (i == 4) {
            return Math.min(getMaxRescueLevel(z), 4);
        }
        if (i >= 5) {
            return Math.min(getMaxRescueLevel(z), 5);
        }
        Slog.w(TAG, "Expected positive mitigation count, was " + i);
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void executeRescueLevel(Context context, String str, int i) {
        Slog.w(TAG, "Attempting rescue level " + levelToString(i));
        try {
            executeRescueLevelInternal(context, i, str);
            EventLogTags.writeRescueSuccess(i);
            String str2 = "Finished rescue level " + levelToString(i);
            if (!TextUtils.isEmpty(str)) {
                str2 = str2 + " for package " + str;
            }
            PackageManagerServiceUtils.logCriticalInfo(3, str2);
        } catch (Throwable th) {
            logRescueException(i, str, th);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0083 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0084  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void executeRescueLevelInternal(final Context context, final int i, final String str) throws Exception {
        FrameworkStatsLog.write(122, i);
        mRspExt = (IRescuePartyExt) ExtLoader.type(IRescuePartyExt.class).create();
        Exception e = null;
        try {
        } catch (Exception e2) {
            e = e2;
        }
        if (i == 1) {
            try {
                resetAllSettingsIfNecessary(context, 2, i);
            } catch (Exception e3) {
                e = e3;
            }
            resetDeviceConfig(context, true, str);
        } else if (i == 2) {
            try {
                resetAllSettingsIfNecessary(context, 3, i);
            } catch (Exception e4) {
                e = e4;
            }
            resetDeviceConfig(context, true, str);
        } else {
            if (i != 3) {
                if (i == 4) {
                    SystemProperties.set(PROP_ATTEMPTING_REBOOT, "true");
                    new Thread(new Runnable() { // from class: com.android.server.RescueParty$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            RescueParty.lambda$executeRescueLevelInternal$0(context, i, str);
                        }
                    }).start();
                } else if (i == 5 && !isRebootPropertySet()) {
                    SystemProperties.set(PROP_ATTEMPTING_FACTORY_RESET, "true");
                    SystemProperties.set(PROP_LAST_FACTORY_RESET_TIME_MS, Long.toString(System.currentTimeMillis()));
                    new Thread(new Runnable() { // from class: com.android.server.RescueParty.1
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                RescueParty.mRspExt.checkForDumpService();
                                RecoverySystem.rebootPromptAndWipeUserData(context, RescueParty.TAG);
                            } catch (Throwable th) {
                                RescueParty.logRescueException(i, str, th);
                            }
                        }
                    }).start();
                }
                if (e == null) {
                    throw e;
                }
                return;
            }
            try {
                resetAllSettingsIfNecessary(context, 4, i);
            } catch (Exception e5) {
                e = e5;
            }
            resetDeviceConfig(context, false, str);
        }
        if (e == null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$executeRescueLevelInternal$0(Context context, int i, String str) {
        try {
            PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
            if (powerManager != null) {
                powerManager.reboot(TAG);
            }
        } catch (Throwable th) {
            logRescueException(i, str, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logRescueException(int i, String str, Throwable th) {
        String completeMessage = ExceptionUtils.getCompleteMessage(th);
        EventLogTags.writeRescueFailure(i, completeMessage);
        String str2 = "Failed rescue level " + levelToString(i);
        if (!TextUtils.isEmpty(str)) {
            str2 = str2 + " for package " + str;
        }
        PackageManagerServiceUtils.logCriticalInfo(6, str2 + ": " + completeMessage);
    }

    private static void resetAllSettingsIfNecessary(Context context, int i, int i2) throws Exception {
        RuntimeException runtimeException;
        if (SystemProperties.getInt(PROP_MAX_RESCUE_LEVEL_ATTEMPTED, 0) >= i2) {
            return;
        }
        SystemProperties.set(PROP_MAX_RESCUE_LEVEL_ATTEMPTED, Integer.toString(i2));
        ContentResolver contentResolver = context.getContentResolver();
        try {
            Settings.Global.resetToDefaultsAsUser(contentResolver, null, i, 0);
            runtimeException = null;
        } catch (Exception e) {
            runtimeException = new RuntimeException("Failed to reset global settings", e);
        }
        for (int i3 : getAllUserIds()) {
            try {
                Settings.Secure.resetToDefaultsAsUser(contentResolver, null, i, i3);
            } catch (Exception e2) {
                runtimeException = new RuntimeException("Failed to reset secure settings for " + i3, e2);
            }
        }
        if (runtimeException != null) {
            throw runtimeException;
        }
    }

    private static void resetDeviceConfig(Context context, boolean z, String str) throws Exception {
        context.getContentResolver();
        try {
            if (!z || str == null) {
                resetAllAffectedNamespaces(context);
            } else {
                performScopedReset(context, str);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset config settings", e);
        }
    }

    private static void resetAllAffectedNamespaces(Context context) {
        Set<String> allAffectedNamespaceSet = RescuePartyObserver.getInstance(context).getAllAffectedNamespaceSet();
        Slog.w(TAG, "Performing reset for all affected namespaces: " + Arrays.toString(allAffectedNamespaceSet.toArray()));
        for (String str : allAffectedNamespaceSet) {
            if (!NAMESPACE_CONFIGURATION.equals(str)) {
                DeviceConfig.resetToDefaults(4, str);
            }
        }
    }

    private static void performScopedReset(Context context, String str) {
        Set<String> affectedNamespaceSet = RescuePartyObserver.getInstance(context).getAffectedNamespaceSet(str);
        if (affectedNamespaceSet != null) {
            Slog.w(TAG, "Performing scoped reset for package: " + str + ", affected namespaces: " + Arrays.toString(affectedNamespaceSet.toArray()));
            for (String str2 : affectedNamespaceSet) {
                if (!NAMESPACE_CONFIGURATION.equals(str2)) {
                    DeviceConfig.resetToDefaults(4, str2);
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class RescuePartyObserver implements PackageWatchdog.PackageHealthObserver {

        @GuardedBy({"RescuePartyObserver.class"})
        static RescuePartyObserver sRescuePartyObserver;
        private final Context mContext;
        private final Map<String, Set<String>> mCallingPackageNamespaceSetMap = new HashMap();
        private final Map<String, Set<String>> mNamespaceCallingPackageSetMap = new HashMap();

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public String getName() {
            return RescueParty.NAME;
        }

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public boolean isPersistent() {
            return true;
        }

        private RescuePartyObserver(Context context) {
            this.mContext = context;
        }

        public static RescuePartyObserver getInstance(Context context) {
            RescuePartyObserver rescuePartyObserver;
            synchronized (RescuePartyObserver.class) {
                if (sRescuePartyObserver == null) {
                    sRescuePartyObserver = new RescuePartyObserver(context);
                }
                rescuePartyObserver = sRescuePartyObserver;
            }
            return rescuePartyObserver;
        }

        public static RescuePartyObserver getInstanceIfCreated() {
            RescuePartyObserver rescuePartyObserver;
            synchronized (RescuePartyObserver.class) {
                rescuePartyObserver = sRescuePartyObserver;
            }
            return rescuePartyObserver;
        }

        @VisibleForTesting
        static void reset() {
            synchronized (RescuePartyObserver.class) {
                sRescuePartyObserver = null;
            }
        }

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public int onHealthCheckFailed(VersionedPackage versionedPackage, int i, int i2) {
            if (RescueParty.m337$$Nest$smisDisabled()) {
                return 0;
            }
            if (i == 3 || i == 4) {
                return RescueParty.mapRescueLevelToUserImpact(RescueParty.getRescueLevel(i2, mayPerformReboot(versionedPackage)));
            }
            return 0;
        }

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public boolean execute(VersionedPackage versionedPackage, int i, int i2) {
            if (RescueParty.m337$$Nest$smisDisabled()) {
                return false;
            }
            if (i != 3 && i != 4) {
                return false;
            }
            RescueParty.executeRescueLevel(this.mContext, versionedPackage == null ? null : versionedPackage.getPackageName(), RescueParty.getRescueLevel(i2, mayPerformReboot(versionedPackage)));
            return true;
        }

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public boolean mayObservePackage(String str) {
            try {
                if (this.mContext.getPackageManager().getModuleInfo(str, 0) != null) {
                    return true;
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
            return isPersistentSystemApp(str);
        }

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public int onBootLoop(int i) {
            if (RescueParty.m337$$Nest$smisDisabled()) {
                return 0;
            }
            return RescueParty.mapRescueLevelToUserImpact(RescueParty.getRescueLevel(i, true));
        }

        @Override // com.android.server.PackageWatchdog.PackageHealthObserver
        public boolean executeBootLoopMitigation(int i) {
            if (RescueParty.m337$$Nest$smisDisabled()) {
                return false;
            }
            RescueParty.executeRescueLevel(this.mContext, null, RescueParty.getRescueLevel(i, !shouldThrottleReboot()));
            return true;
        }

        private boolean mayPerformReboot(VersionedPackage versionedPackage) {
            if (versionedPackage == null || shouldThrottleReboot()) {
                return false;
            }
            return isPersistentSystemApp(versionedPackage.getPackageName());
        }

        private boolean shouldThrottleReboot() {
            return System.currentTimeMillis() < Long.valueOf(SystemProperties.getLong(RescueParty.PROP_LAST_FACTORY_RESET_TIME_MS, 0L)).longValue() + TimeUnit.MINUTES.toMillis(SystemProperties.getLong(RescueParty.PROP_THROTTLE_DURATION_MIN_FLAG, RescueParty.DEFAULT_FACTORY_RESET_THROTTLE_DURATION_MIN));
        }

        private boolean isPersistentSystemApp(String str) {
            try {
                return (this.mContext.getPackageManager().getApplicationInfo(str, 0).flags & 9) == 9;
            } catch (PackageManager.NameNotFoundException unused) {
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void recordDeviceConfigAccess(String str, String str2) {
            Set<String> set = this.mCallingPackageNamespaceSetMap.get(str);
            if (set == null) {
                set = new ArraySet<>();
                this.mCallingPackageNamespaceSetMap.put(str, set);
            }
            set.add(str2);
            Set<String> set2 = this.mNamespaceCallingPackageSetMap.get(str2);
            if (set2 == null) {
                set2 = new ArraySet<>();
            }
            set2.add(str);
            this.mNamespaceCallingPackageSetMap.put(str2, set2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Set<String> getAffectedNamespaceSet(String str) {
            return this.mCallingPackageNamespaceSetMap.get(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Set<String> getAllAffectedNamespaceSet() {
            return new HashSet(this.mNamespaceCallingPackageSetMap.keySet());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Set<String> getCallingPackagesSet(String str) {
            return this.mNamespaceCallingPackageSetMap.get(str);
        }
    }

    private static int[] getAllUserIds() {
        int[] iArr = {0};
        try {
            for (File file : FileUtils.listFilesOrEmpty(Environment.getDataSystemDeDirectory())) {
                try {
                    int parseInt = Integer.parseInt(file.getName());
                    if (parseInt != 0) {
                        iArr = ArrayUtils.appendInt(iArr, parseInt);
                    }
                } catch (NumberFormatException unused) {
                }
            }
        } catch (Throwable th) {
            Slog.w(TAG, "Trouble discovering users", th);
        }
        return iArr;
    }

    private static boolean isUsbActive() {
        if (SystemProperties.getBoolean(PROP_VIRTUAL_DEVICE, false)) {
            Slog.v(TAG, "Assuming virtual device is connected over USB");
            return true;
        }
        try {
            return "CONFIGURED".equals(FileUtils.readTextFile(new File("/sys/class/android_usb/android0/state"), 128, "").trim());
        } catch (Throwable th) {
            Slog.w(TAG, "Failed to determine if device was on USB", th);
            return false;
        }
    }

    private static String levelToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? Integer.toString(i) : "FACTORY_RESET" : "WARM_REBOOT" : "RESET_SETTINGS_TRUSTED_DEFAULTS" : "RESET_SETTINGS_UNTRUSTED_CHANGES" : "RESET_SETTINGS_UNTRUSTED_DEFAULTS" : "NONE";
    }
}
