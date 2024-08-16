package com.android.server.recoverysystem;

import android.apex.CompressedApexInfo;
import android.apex.CompressedApexInfoList;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.boot.IBootControl;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.IRecoverySystem;
import android.os.IRecoverySystemProgressListener;
import android.os.PowerManager;
import android.os.RecoverySystem;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ShellCallback;
import android.os.SystemProperties;
import android.ota.nano.OtaPackageMetadata;
import android.provider.DeviceConfig;
import android.sysprop.ApexProperties;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.FastImmutableArraySet;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.widget.LockSettingsInternal;
import com.android.internal.widget.RebootEscrowListener;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.hdmi.HotplugDetectionAction;
import com.android.server.pm.ApexManager;
import com.android.server.recoverysystem.hal.BootControlHIDL;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RecoverySystemService extends IRecoverySystem.Stub implements RebootEscrowListener {

    @VisibleForTesting
    static final String AB_UPDATE = "ro.build.ab_update";
    private static final long APEX_INFO_SIZE_LIMIT = 2457600;
    private static final boolean DEBUG = false;

    @VisibleForTesting
    static final String INIT_SERVICE_CLEAR_BCB = "init.svc.clear-bcb";

    @VisibleForTesting
    static final String INIT_SERVICE_SETUP_BCB = "init.svc.setup-bcb";

    @VisibleForTesting
    static final String INIT_SERVICE_UNCRYPT = "init.svc.uncrypt";
    static final String LSKF_CAPTURED_COUNT_PREF = "lskf_captured_count";
    static final String LSKF_CAPTURED_TIMESTAMP_PREF = "lskf_captured_timestamp";
    static final String REQUEST_LSKF_COUNT_PREF_SUFFIX = "_request_lskf_count";
    static final String REQUEST_LSKF_TIMESTAMP_PREF_SUFFIX = "_request_lskf_timestamp";
    private static final int ROR_NEED_PREPARATION = 0;
    private static final int ROR_NOT_REQUESTED = 0;
    private static final int ROR_REQUESTED_NEED_CLEAR = 1;
    private static final int ROR_REQUESTED_SKIP_CLEAR = 2;
    private static final int ROR_SKIP_PREPARATION_AND_NOTIFY = 1;
    private static final int ROR_SKIP_PREPARATION_NOT_NOTIFY = 2;
    private static final int SOCKET_CONNECTION_MAX_RETRY = 30;
    private static final String TAG = "RecoverySystemService";
    private static final String UNCRYPT_SOCKET = "uncrypt";

    @GuardedBy({"this"})
    private final ArrayMap<String, IntentSender> mCallerPendingRequest;

    @GuardedBy({"this"})
    private final ArraySet<String> mCallerPreparedForReboot;
    private final Context mContext;
    private final Injector mInjector;
    private static final Object sRequestLock = new Object();
    static final FastImmutableArraySet<Integer> FATAL_ARM_ESCROW_ERRORS = new FastImmutableArraySet<>(new Integer[]{2, 3, 4, 5, 6});

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private @interface ResumeOnRebootActionsOnClear {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private @interface ResumeOnRebootActionsOnRequest {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class RebootPreparationError {
        final int mProviderErrorCode;

        @RecoverySystem.ResumeOnRebootRebootErrorCode
        final int mRebootErrorCode;

        RebootPreparationError(int i, int i2) {
            this.mRebootErrorCode = i;
            this.mProviderErrorCode = i2;
        }

        int getErrorCodeForMetrics() {
            return this.mRebootErrorCode + this.mProviderErrorCode;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PreferencesManager {
        private static final String METRICS_DIR = "recovery_system";
        private static final String METRICS_PREFS_FILE = "RecoverySystemMetricsPrefs.xml";
        private final File mMetricsPrefsFile;
        protected final SharedPreferences mSharedPreferences;

        PreferencesManager(Context context) {
            File file = new File(new File(Environment.getDataSystemCeDirectory(0), METRICS_DIR), METRICS_PREFS_FILE);
            this.mMetricsPrefsFile = file;
            this.mSharedPreferences = context.getSharedPreferences(file, 0);
        }

        public long getLong(String str, long j) {
            return this.mSharedPreferences.getLong(str, j);
        }

        public int getInt(String str, int i) {
            return this.mSharedPreferences.getInt(str, i);
        }

        public void putLong(String str, long j) {
            this.mSharedPreferences.edit().putLong(str, j).commit();
        }

        public void putInt(String str, int i) {
            this.mSharedPreferences.edit().putInt(str, i).commit();
        }

        public synchronized void incrementIntKey(String str, int i) {
            putInt(str, getInt(str, i) + 1);
        }

        public void deletePrefsFile() {
            if (this.mMetricsPrefsFile.delete()) {
                return;
            }
            Slog.w(RecoverySystemService.TAG, "Failed to delete metrics prefs");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Injector {
        protected final Context mContext;
        protected final PreferencesManager mPrefs;

        Injector(Context context) {
            this.mContext = context;
            this.mPrefs = new PreferencesManager(context);
        }

        public Context getContext() {
            return this.mContext;
        }

        public LockSettingsInternal getLockSettingsService() {
            return (LockSettingsInternal) LocalServices.getService(LockSettingsInternal.class);
        }

        public PowerManager getPowerManager() {
            return (PowerManager) this.mContext.getSystemService("power");
        }

        public String systemPropertiesGet(String str) {
            return SystemProperties.get(str);
        }

        public void systemPropertiesSet(String str, String str2) {
            SystemProperties.set(str, str2);
        }

        public boolean uncryptPackageFileDelete() {
            return RecoverySystem.UNCRYPT_PACKAGE_FILE.delete();
        }

        public String getUncryptPackageFileName() {
            return RecoverySystem.UNCRYPT_PACKAGE_FILE.getName();
        }

        public FileWriter getUncryptPackageFileWriter() throws IOException {
            return new FileWriter(RecoverySystem.UNCRYPT_PACKAGE_FILE);
        }

        public UncryptSocket connectService() {
            UncryptSocket uncryptSocket = new UncryptSocket();
            if (uncryptSocket.connectService()) {
                return uncryptSocket;
            }
            uncryptSocket.close();
            return null;
        }

        public IBootControl getBootControl() throws RemoteException {
            String str = IBootControl.DESCRIPTOR + "/default";
            if (ServiceManager.isDeclared(str)) {
                Slog.i(RecoverySystemService.TAG, "AIDL version of BootControl HAL present, using instance " + str);
                return IBootControl.Stub.asInterface(ServiceManager.waitForDeclaredService(str));
            }
            BootControlHIDL service = BootControlHIDL.getService();
            if (!BootControlHIDL.isServicePresent()) {
                Slog.e(RecoverySystemService.TAG, "Neither AIDL nor HIDL version of the BootControl HAL is present.");
                return null;
            }
            if (BootControlHIDL.isV1_2ServicePresent()) {
                return service;
            }
            Slog.w(RecoverySystemService.TAG, "Device doesn't implement boot control HAL V1_2.");
            return null;
        }

        public void threadSleep(long j) throws InterruptedException {
            Thread.sleep(j);
        }

        public int getUidFromPackageName(String str) {
            try {
                return this.mContext.getPackageManager().getPackageUidAsUser(str, 0);
            } catch (PackageManager.NameNotFoundException unused) {
                Slog.w(RecoverySystemService.TAG, "Failed to find uid for " + str);
                return -1;
            }
        }

        public PreferencesManager getMetricsPrefs() {
            return this.mPrefs;
        }

        public long getCurrentTimeMillis() {
            return System.currentTimeMillis();
        }

        public void reportRebootEscrowPreparationMetrics(int i, @ResumeOnRebootActionsOnRequest int i2, int i3) {
            FrameworkStatsLog.write(339, i, i2, i3);
        }

        public void reportRebootEscrowLskfCapturedMetrics(int i, int i2, int i3) {
            FrameworkStatsLog.write(340, i, i2, i3);
        }

        public void reportRebootEscrowRebootMetrics(int i, int i2, int i3, int i4, boolean z, boolean z2, int i5, int i6) {
            FrameworkStatsLog.write(341, i, i2, i3, i4, z, z2, i5, i6);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Lifecycle extends SystemService {
        private RecoverySystemService mRecoverySystemService;

        public Lifecycle(Context context) {
            super(context);
        }

        public void onBootPhase(int i) {
            if (i == 500) {
                this.mRecoverySystemService.onSystemServicesReady();
            }
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.recoverysystem.RecoverySystemService, android.os.IBinder] */
        public void onStart() {
            ?? recoverySystemService = new RecoverySystemService(getContext());
            this.mRecoverySystemService = recoverySystemService;
            publishBinderService("recovery", (IBinder) recoverySystemService);
        }
    }

    private RecoverySystemService(Context context) {
        this(new Injector(context));
    }

    @VisibleForTesting
    RecoverySystemService(Injector injector) {
        this.mCallerPendingRequest = new ArrayMap<>();
        this.mCallerPreparedForReboot = new ArraySet<>();
        this.mInjector = injector;
        this.mContext = injector.getContext();
    }

    @VisibleForTesting
    void onSystemServicesReady() {
        LockSettingsInternal lockSettingsService = this.mInjector.getLockSettingsService();
        if (lockSettingsService == null) {
            Slog.e(TAG, "Failed to get lock settings service, skipping set RebootEscrowListener");
        } else {
            lockSettingsService.setRebootEscrowListener(this);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x00a4, code lost:
    
        android.util.Slog.e(com.android.server.recoverysystem.RecoverySystemService.TAG, "uncrypt failed with status: " + r3);
        r7.sendAck();
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00c2, code lost:
    
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean uncrypt(String str, IRecoverySystemProgressListener iRecoverySystemProgressListener) {
        synchronized (sRequestLock) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
            if (!checkAndWaitForUncryptService()) {
                Slog.e(TAG, "uncrypt service is unavailable.");
                return false;
            }
            this.mInjector.uncryptPackageFileDelete();
            try {
                FileWriter uncryptPackageFileWriter = this.mInjector.getUncryptPackageFileWriter();
                try {
                    uncryptPackageFileWriter.write(str + "\n");
                    uncryptPackageFileWriter.close();
                    this.mInjector.systemPropertiesSet("ctl.start", UNCRYPT_SOCKET);
                    UncryptSocket connectService = this.mInjector.connectService();
                    if (connectService == null) {
                        Slog.e(TAG, "Failed to connect to uncrypt socket");
                        return false;
                    }
                    int i = Integer.MIN_VALUE;
                    while (true) {
                        try {
                            int percentageUncrypted = connectService.getPercentageUncrypted();
                            if (percentageUncrypted != i || i == Integer.MIN_VALUE) {
                                if (percentageUncrypted < 0 || percentageUncrypted > 100) {
                                    break;
                                }
                                Slog.i(TAG, "uncrypt read status: " + percentageUncrypted);
                                if (iRecoverySystemProgressListener != null) {
                                    try {
                                        iRecoverySystemProgressListener.onProgress(percentageUncrypted);
                                    } catch (RemoteException unused) {
                                        Slog.w(TAG, "RemoteException when posting progress");
                                    }
                                }
                                if (percentageUncrypted == 100) {
                                    Slog.i(TAG, "uncrypt successfully finished.");
                                    connectService.sendAck();
                                    return true;
                                }
                                i = percentageUncrypted;
                            }
                        } catch (IOException e) {
                            Slog.e(TAG, "IOException when reading status: ", e);
                            return false;
                        } finally {
                            connectService.close();
                        }
                    }
                } catch (Throwable th) {
                    if (uncryptPackageFileWriter != null) {
                        try {
                            uncryptPackageFileWriter.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (IOException e2) {
                Slog.e(TAG, "IOException when writing \"" + this.mInjector.getUncryptPackageFileName() + "\":", e2);
                return false;
            }
        }
    }

    public boolean clearBcb() {
        boolean z;
        synchronized (sRequestLock) {
            z = setupOrClearBcb(false, null);
        }
        return z;
    }

    public boolean setupBcb(String str) {
        boolean z;
        synchronized (sRequestLock) {
            z = setupOrClearBcb(true, str);
        }
        return z;
    }

    public void rebootRecoveryWithCommand(String str) {
        synchronized (sRequestLock) {
            if (setupOrClearBcb(true, str)) {
                this.mInjector.getPowerManager().reboot("recovery");
            }
        }
    }

    private void enforcePermissionForResumeOnReboot() {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.RECOVERY") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.REBOOT") != 0) {
            throw new SecurityException("Caller must have android.permission.RECOVERY or android.permission.REBOOT for resume on reboot.");
        }
    }

    private void reportMetricsOnRequestLskf(String str, int i) {
        int size;
        int uidFromPackageName = this.mInjector.getUidFromPackageName(str);
        synchronized (this) {
            size = this.mCallerPendingRequest.size();
        }
        PreferencesManager metricsPrefs = this.mInjector.getMetricsPrefs();
        metricsPrefs.putLong(str + REQUEST_LSKF_TIMESTAMP_PREF_SUFFIX, this.mInjector.getCurrentTimeMillis());
        metricsPrefs.incrementIntKey(str + REQUEST_LSKF_COUNT_PREF_SUFFIX, 0);
        this.mInjector.reportRebootEscrowPreparationMetrics(uidFromPackageName, i, size);
    }

    public boolean requestLskf(String str, IntentSender intentSender) {
        enforcePermissionForResumeOnReboot();
        if (str == null) {
            Slog.w(TAG, "Missing packageName when requesting lskf.");
            return false;
        }
        int updateRoRPreparationStateOnNewRequest = updateRoRPreparationStateOnNewRequest(str, intentSender);
        reportMetricsOnRequestLskf(str, updateRoRPreparationStateOnNewRequest);
        if (updateRoRPreparationStateOnNewRequest != 0) {
            if (updateRoRPreparationStateOnNewRequest == 1) {
                sendPreparedForRebootIntentIfNeeded(intentSender);
                return true;
            }
            if (updateRoRPreparationStateOnNewRequest == 2) {
                return true;
            }
            throw new IllegalStateException("Unsupported action type on new request " + updateRoRPreparationStateOnNewRequest);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            LockSettingsInternal lockSettingsService = this.mInjector.getLockSettingsService();
            if (lockSettingsService == null) {
                Slog.e(TAG, "Failed to get lock settings service, skipping prepareRebootEscrow");
                return false;
            }
            if (lockSettingsService.prepareRebootEscrow()) {
                return true;
            }
            clearRoRPreparationState();
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @ResumeOnRebootActionsOnRequest
    private synchronized int updateRoRPreparationStateOnNewRequest(String str, IntentSender intentSender) {
        if (!this.mCallerPreparedForReboot.isEmpty()) {
            if (this.mCallerPreparedForReboot.contains(str)) {
                Slog.i(TAG, "RoR already has prepared for " + str);
            }
            this.mCallerPreparedForReboot.add(str);
            return 1;
        }
        boolean isEmpty = this.mCallerPendingRequest.isEmpty();
        if (this.mCallerPendingRequest.containsKey(str)) {
            Slog.i(TAG, "Duplicate RoR preparation request for " + str);
        }
        this.mCallerPendingRequest.put(str, intentSender);
        return isEmpty ? 0 : 2;
    }

    private void reportMetricsOnPreparedForReboot() {
        ArrayList<String> arrayList;
        long currentTimeMillis = this.mInjector.getCurrentTimeMillis();
        synchronized (this) {
            arrayList = new ArrayList(this.mCallerPreparedForReboot);
        }
        PreferencesManager metricsPrefs = this.mInjector.getMetricsPrefs();
        metricsPrefs.putLong(LSKF_CAPTURED_TIMESTAMP_PREF, currentTimeMillis);
        metricsPrefs.incrementIntKey(LSKF_CAPTURED_COUNT_PREF, 0);
        for (String str : arrayList) {
            int uidFromPackageName = this.mInjector.getUidFromPackageName(str);
            long j = metricsPrefs.getLong(str + REQUEST_LSKF_TIMESTAMP_PREF_SUFFIX, -1L);
            int i = (j == -1 || currentTimeMillis <= j) ? -1 : ((int) (currentTimeMillis - j)) / 1000;
            Slog.i(TAG, String.format("Reporting lskf captured, lskf capture takes %d seconds for package %s", Integer.valueOf(i), str));
            this.mInjector.reportRebootEscrowLskfCapturedMetrics(uidFromPackageName, arrayList.size(), i);
        }
    }

    public void onPreparedForReboot(boolean z) {
        if (z) {
            updateRoRPreparationStateOnPreparedForReboot();
            reportMetricsOnPreparedForReboot();
        }
    }

    private synchronized void updateRoRPreparationStateOnPreparedForReboot() {
        if (!this.mCallerPreparedForReboot.isEmpty()) {
            Slog.w(TAG, "onPreparedForReboot called when some clients have prepared.");
        }
        if (this.mCallerPendingRequest.isEmpty()) {
            Slog.w(TAG, "onPreparedForReboot called but no client has requested.");
        }
        for (int i = 0; i < this.mCallerPendingRequest.size(); i++) {
            sendPreparedForRebootIntentIfNeeded(this.mCallerPendingRequest.valueAt(i));
            this.mCallerPreparedForReboot.add(this.mCallerPendingRequest.keyAt(i));
        }
        this.mCallerPendingRequest.clear();
    }

    private void sendPreparedForRebootIntentIfNeeded(IntentSender intentSender) {
        if (intentSender != null) {
            try {
                intentSender.sendIntent(null, 0, null, null, null);
            } catch (IntentSender.SendIntentException e) {
                Slog.w(TAG, "Could not send intent for prepared reboot: " + e.getMessage());
            }
        }
    }

    public boolean clearLskf(String str) {
        enforcePermissionForResumeOnReboot();
        if (str == null) {
            Slog.w(TAG, "Missing packageName when clearing lskf.");
            return false;
        }
        int updateRoRPreparationStateOnClear = updateRoRPreparationStateOnClear(str);
        if (updateRoRPreparationStateOnClear == 0) {
            Slog.w(TAG, "RoR clear called before preparation for caller " + str);
            return true;
        }
        if (updateRoRPreparationStateOnClear != 1) {
            if (updateRoRPreparationStateOnClear == 2) {
                return true;
            }
            throw new IllegalStateException("Unsupported action type on clear " + updateRoRPreparationStateOnClear);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            LockSettingsInternal lockSettingsService = this.mInjector.getLockSettingsService();
            if (lockSettingsService == null) {
                Slog.e(TAG, "Failed to get lock settings service, skipping clearRebootEscrow");
                return false;
            }
            return lockSettingsService.clearRebootEscrow();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @ResumeOnRebootActionsOnClear
    private synchronized int updateRoRPreparationStateOnClear(String str) {
        boolean z = false;
        if (!this.mCallerPreparedForReboot.contains(str) && !this.mCallerPendingRequest.containsKey(str)) {
            Slog.w(TAG, str + " hasn't prepared for resume on reboot");
            return 0;
        }
        this.mCallerPendingRequest.remove(str);
        this.mCallerPreparedForReboot.remove(str);
        if (this.mCallerPendingRequest.isEmpty() && this.mCallerPreparedForReboot.isEmpty()) {
            z = true;
        }
        return z ? 1 : 2;
    }

    private boolean isAbDevice() {
        return "true".equalsIgnoreCase(this.mInjector.systemPropertiesGet(AB_UPDATE));
    }

    private boolean verifySlotForNextBoot(boolean z) {
        if (!isAbDevice()) {
            Slog.w(TAG, "Device isn't a/b, skipping slot verification.");
            return true;
        }
        try {
            IBootControl bootControl = this.mInjector.getBootControl();
            if (bootControl == null) {
                Slog.w(TAG, "Cannot get the boot control HAL, skipping slot verification.");
                return true;
            }
            try {
                int currentSlot = bootControl.getCurrentSlot();
                if (currentSlot != 0 && currentSlot != 1) {
                    throw new IllegalStateException("Current boot slot should be 0 or 1, got " + currentSlot);
                }
                int activeBootSlot = bootControl.getActiveBootSlot();
                if (z) {
                    currentSlot = currentSlot == 0 ? 1 : 0;
                }
                if (activeBootSlot == currentSlot) {
                    return true;
                }
                Slog.w(TAG, "The next active boot slot doesn't match the expected value, expected " + currentSlot + ", got " + activeBootSlot);
                return false;
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to query the active slots", e);
                return false;
            }
        } catch (RemoteException e2) {
            Slog.w(TAG, "Failed to get the boot control HAL " + e2);
            return false;
        }
    }

    private RebootPreparationError armRebootEscrow(String str, boolean z) {
        if (str == null) {
            Slog.w(TAG, "Missing packageName when rebooting with lskf.");
            return new RebootPreparationError(2000, 0);
        }
        if (!isLskfCaptured(str)) {
            return new RebootPreparationError(3000, 0);
        }
        if (!verifySlotForNextBoot(z)) {
            return new RebootPreparationError(4000, 0);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            LockSettingsInternal lockSettingsService = this.mInjector.getLockSettingsService();
            if (lockSettingsService == null) {
                Slog.e(TAG, "Failed to get lock settings service, skipping armRebootEscrow");
                return new RebootPreparationError(HotplugDetectionAction.POLLING_INTERVAL_MS_FOR_TV, 3);
            }
            int armRebootEscrow = lockSettingsService.armRebootEscrow();
            if (armRebootEscrow != 0) {
                Slog.w(TAG, "Failure to escrow key for reboot, providerErrorCode: " + armRebootEscrow);
                return new RebootPreparationError(HotplugDetectionAction.POLLING_INTERVAL_MS_FOR_TV, armRebootEscrow);
            }
            return new RebootPreparationError(0, 0);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean useServerBasedRoR() {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return DeviceConfig.getBoolean("ota", "server_based_ror_enabled", false);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void reportMetricsOnRebootWithLskf(String str, boolean z, RebootPreparationError rebootPreparationError) {
        int size;
        int uidFromPackageName = this.mInjector.getUidFromPackageName(str);
        boolean useServerBasedRoR = useServerBasedRoR();
        synchronized (this) {
            size = this.mCallerPreparedForReboot.size();
        }
        long currentTimeMillis = this.mInjector.getCurrentTimeMillis();
        PreferencesManager metricsPrefs = this.mInjector.getMetricsPrefs();
        long j = metricsPrefs.getLong(LSKF_CAPTURED_TIMESTAMP_PREF, -1L);
        int i = (j == -1 || currentTimeMillis <= j) ? -1 : ((int) (currentTimeMillis - j)) / 1000;
        int i2 = metricsPrefs.getInt(str + REQUEST_LSKF_COUNT_PREF_SUFFIX, -1);
        int i3 = metricsPrefs.getInt(LSKF_CAPTURED_COUNT_PREF, -1);
        Slog.i(TAG, String.format("Reporting reboot with lskf, package name %s, client count %d, request count %d, lskf captured count %d, duration since lskf captured %d seconds.", str, Integer.valueOf(size), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i)));
        this.mInjector.reportRebootEscrowRebootMetrics(rebootPreparationError.getErrorCodeForMetrics(), uidFromPackageName, size, i2, z, useServerBasedRoR, i, i3);
    }

    private synchronized void clearRoRPreparationState() {
        this.mCallerPendingRequest.clear();
        this.mCallerPreparedForReboot.clear();
    }

    private void clearRoRPreparationStateOnRebootFailure(RebootPreparationError rebootPreparationError) {
        if (FATAL_ARM_ESCROW_ERRORS.contains(Integer.valueOf(rebootPreparationError.mProviderErrorCode))) {
            Slog.w(TAG, "Clearing resume on reboot states for all clients on arm escrow error: " + rebootPreparationError.mProviderErrorCode);
            clearRoRPreparationState();
        }
    }

    @RecoverySystem.ResumeOnRebootRebootErrorCode
    private int rebootWithLskfImpl(String str, String str2, boolean z) {
        RebootPreparationError armRebootEscrow = armRebootEscrow(str, z);
        reportMetricsOnRebootWithLskf(str, z, armRebootEscrow);
        clearRoRPreparationStateOnRebootFailure(armRebootEscrow);
        int i = armRebootEscrow.mRebootErrorCode;
        if (i != 0) {
            return i;
        }
        this.mInjector.getMetricsPrefs().deletePrefsFile();
        this.mInjector.getPowerManager().reboot(str2);
        return 1000;
    }

    @RecoverySystem.ResumeOnRebootRebootErrorCode
    public int rebootWithLskfAssumeSlotSwitch(String str, String str2) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
        return rebootWithLskfImpl(str, str2, true);
    }

    @RecoverySystem.ResumeOnRebootRebootErrorCode
    public int rebootWithLskf(String str, String str2, boolean z) {
        enforcePermissionForResumeOnReboot();
        return rebootWithLskfImpl(str, str2, z);
    }

    public static boolean isUpdatableApexSupported() {
        return ((Boolean) ApexProperties.updatable().orElse(Boolean.FALSE)).booleanValue();
    }

    private static CompressedApexInfoList getCompressedApexInfoList(String str) throws IOException {
        ZipFile zipFile = new ZipFile(str);
        try {
            ZipEntry entry = zipFile.getEntry("apex_info.pb");
            if (entry == null) {
                zipFile.close();
                return null;
            }
            if (entry.getSize() >= APEX_INFO_SIZE_LIMIT) {
                throw new IllegalArgumentException("apex_info.pb has size " + entry.getSize() + " which is larger than the permitted limit" + APEX_INFO_SIZE_LIMIT);
            }
            if (entry.getSize() == 0) {
                CompressedApexInfoList compressedApexInfoList = new CompressedApexInfoList();
                compressedApexInfoList.apexInfos = new CompressedApexInfo[0];
                zipFile.close();
                return compressedApexInfoList;
            }
            Log.i(TAG, "Allocating " + entry.getSize() + " bytes of memory to store OTA Metadata");
            int size = (int) entry.getSize();
            byte[] bArr = new byte[size];
            InputStream inputStream = zipFile.getInputStream(entry);
            try {
                int read = inputStream.read(bArr);
                String str2 = "Read " + read + " when expecting " + size;
                Log.e(TAG, str2);
                if (read != size) {
                    throw new IOException(str2);
                }
                inputStream.close();
                OtaPackageMetadata.ApexMetadata parseFrom = OtaPackageMetadata.ApexMetadata.parseFrom(bArr);
                CompressedApexInfoList compressedApexInfoList2 = new CompressedApexInfoList();
                compressedApexInfoList2.apexInfos = (CompressedApexInfo[]) Arrays.stream(parseFrom.apexInfo).filter(new Predicate() { // from class: com.android.server.recoverysystem.RecoverySystemService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean z;
                        z = ((OtaPackageMetadata.ApexInfo) obj).isCompressed;
                        return z;
                    }
                }).map(new Function() { // from class: com.android.server.recoverysystem.RecoverySystemService$$ExternalSyntheticLambda1
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        CompressedApexInfo lambda$getCompressedApexInfoList$1;
                        lambda$getCompressedApexInfoList$1 = RecoverySystemService.lambda$getCompressedApexInfoList$1((OtaPackageMetadata.ApexInfo) obj);
                        return lambda$getCompressedApexInfoList$1;
                    }
                }).toArray(new IntFunction() { // from class: com.android.server.recoverysystem.RecoverySystemService$$ExternalSyntheticLambda2
                    @Override // java.util.function.IntFunction
                    public final Object apply(int i) {
                        CompressedApexInfo[] lambda$getCompressedApexInfoList$2;
                        lambda$getCompressedApexInfoList$2 = RecoverySystemService.lambda$getCompressedApexInfoList$2(i);
                        return lambda$getCompressedApexInfoList$2;
                    }
                });
                zipFile.close();
                return compressedApexInfoList2;
            } finally {
            }
        } catch (Throwable th) {
            try {
                zipFile.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ CompressedApexInfo lambda$getCompressedApexInfoList$1(OtaPackageMetadata.ApexInfo apexInfo) {
        CompressedApexInfo compressedApexInfo = new CompressedApexInfo();
        compressedApexInfo.moduleName = apexInfo.packageName;
        compressedApexInfo.decompressedSize = apexInfo.decompressedSize;
        compressedApexInfo.versionCode = apexInfo.version;
        return compressedApexInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ CompressedApexInfo[] lambda$getCompressedApexInfoList$2(int i) {
        return new CompressedApexInfo[i];
    }

    public boolean allocateSpaceForUpdate(String str) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
        if (!isUpdatableApexSupported()) {
            Log.i(TAG, "Updatable Apex not supported, allocateSpaceForUpdate does nothing.");
            return true;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                try {
                    CompressedApexInfoList compressedApexInfoList = getCompressedApexInfoList(str);
                    if (compressedApexInfoList == null) {
                        Log.i(TAG, "apex_info.pb not present in OTA package. Assuming device doesn't support compressedAPEX, continueing without allocating space.");
                        return true;
                    }
                    ApexManager.getInstance().reserveSpaceForCompressedApex(compressedApexInfoList);
                    return true;
                } catch (IOException | UnsupportedOperationException e) {
                    Slog.e(TAG, "Failed to reserve space for compressed apex: ", e);
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return false;
                }
            } catch (RemoteException e2) {
                e2.rethrowAsRuntimeException();
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return false;
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean isLskfCaptured(String str) {
        boolean contains;
        enforcePermissionForResumeOnReboot();
        synchronized (this) {
            contains = this.mCallerPreparedForReboot.contains(str);
        }
        if (contains) {
            return true;
        }
        Slog.i(TAG, "Reboot requested before prepare completed for caller " + str);
        return false;
    }

    private boolean checkAndWaitForUncryptService() {
        for (int i = 0; i < 30; i++) {
            if (!("running".equals(this.mInjector.systemPropertiesGet(INIT_SERVICE_UNCRYPT)) || "running".equals(this.mInjector.systemPropertiesGet(INIT_SERVICE_SETUP_BCB)) || "running".equals(this.mInjector.systemPropertiesGet(INIT_SERVICE_CLEAR_BCB)))) {
                return true;
            }
            try {
                this.mInjector.threadSleep(1000L);
            } catch (InterruptedException e) {
                Slog.w(TAG, "Interrupted:", e);
            }
        }
        return false;
    }

    private boolean setupOrClearBcb(boolean z, String str) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
        if (!checkAndWaitForUncryptService()) {
            Slog.e(TAG, "uncrypt service is unavailable.");
            return false;
        }
        if (z) {
            this.mInjector.systemPropertiesSet("ctl.start", "setup-bcb");
        } else {
            this.mInjector.systemPropertiesSet("ctl.start", "clear-bcb");
        }
        UncryptSocket connectService = this.mInjector.connectService();
        if (connectService == null) {
            Slog.e(TAG, "Failed to connect to uncrypt socket");
            return false;
        }
        try {
            if (z) {
                connectService.sendCommand(str);
            }
            int percentageUncrypted = connectService.getPercentageUncrypted();
            connectService.sendAck();
            if (percentageUncrypted == 100) {
                StringBuilder sb = new StringBuilder();
                sb.append("uncrypt ");
                sb.append(z ? "setup" : "clear");
                sb.append(" bcb successfully finished.");
                Slog.i(TAG, sb.toString());
                connectService.close();
                return true;
            }
            Slog.e(TAG, "uncrypt failed with status: " + percentageUncrypted);
            return false;
        } catch (IOException e) {
            Slog.e(TAG, "IOException when communicating with uncrypt:", e);
            return false;
        } finally {
            connectService.close();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class UncryptSocket {
        private DataInputStream mInputStream;
        private LocalSocket mLocalSocket;
        private DataOutputStream mOutputStream;

        public boolean connectService() {
            boolean z;
            this.mLocalSocket = new LocalSocket();
            int i = 0;
            while (true) {
                if (i >= 30) {
                    z = false;
                    break;
                }
                try {
                    this.mLocalSocket.connect(new LocalSocketAddress(RecoverySystemService.UNCRYPT_SOCKET, LocalSocketAddress.Namespace.RESERVED));
                    z = true;
                    break;
                } catch (IOException unused) {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        Slog.w(RecoverySystemService.TAG, "Interrupted:", e);
                    }
                    i++;
                }
            }
            if (!z) {
                Slog.e(RecoverySystemService.TAG, "Timed out connecting to uncrypt socket");
                close();
                return false;
            }
            try {
                this.mInputStream = new DataInputStream(this.mLocalSocket.getInputStream());
                this.mOutputStream = new DataOutputStream(this.mLocalSocket.getOutputStream());
                return true;
            } catch (IOException unused2) {
                close();
                return false;
            }
        }

        public void sendCommand(String str) throws IOException {
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            this.mOutputStream.writeInt(bytes.length);
            this.mOutputStream.write(bytes, 0, bytes.length);
        }

        public int getPercentageUncrypted() throws IOException {
            return this.mInputStream.readInt();
        }

        public void sendAck() throws IOException {
            this.mOutputStream.writeInt(0);
        }

        public void close() {
            IoUtils.closeQuietly(this.mInputStream);
            IoUtils.closeQuietly(this.mOutputStream);
            IoUtils.closeQuietly(this.mLocalSocket);
        }
    }

    private boolean isCallerShell() {
        int callingUid = Binder.getCallingUid();
        return callingUid == 2000 || callingUid == 0;
    }

    private void enforceShell() {
        if (!isCallerShell()) {
            throw new SecurityException("Caller must be shell");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
        enforceShell();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            new RecoverySystemShellCommand(this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }
}
