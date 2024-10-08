package com.android.server.vibrator;

import android.R;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.CombinedVibration;
import android.os.ExternalVibration;
import android.os.Handler;
import android.os.IBinder;
import android.os.IExternalVibratorService;
import android.os.IVibratorManagerService;
import android.os.IVibratorStateListener;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ShellCallback;
import android.os.ShellCommand;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.VibratorInfo;
import android.os.vibrator.PrebakedSegment;
import android.os.vibrator.VibrationEffectSegment;
import android.text.TextUtils;
import android.util.Slog;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IBatteryStats;
import com.android.internal.util.DumpUtils;
import com.android.server.SystemService;
import com.android.server.job.controllers.JobStatus;
import com.android.server.notification.NotificationShellCmd;
import com.android.server.vibrator.Vibration;
import com.android.server.vibrator.VibrationSettings;
import com.android.server.vibrator.VibrationStats;
import com.android.server.vibrator.VibrationThread;
import com.android.server.vibrator.VibratorController;
import com.android.server.zenmode.IZenModeManagerExt;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import libcore.util.NativeAllocationRegistry;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class VibratorManagerService extends IVibratorManagerService.Stub {
    private static final int ATTRIBUTES_ALL_BYPASS_FLAGS = 3;
    private static final long BATTERY_STATS_REPEATING_VIBRATION_DURATION = 5000;
    private static boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final VibrationAttributes DEFAULT_ATTRIBUTES = new VibrationAttributes.Builder().build();
    private static final String EXTERNAL_VIBRATOR_SERVICE = "external_vibrator_service";
    private static final String TAG = "VibratorManagerService";
    private static final long VIBRATION_CANCEL_WAIT_MILLIS = 5000;

    @GuardedBy({"mLock"})
    private final SparseArray<AlwaysOnVibration> mAlwaysOnEffects;
    private final AppOpsManager mAppOps;
    private final IBatteryStats mBatteryStatsService;
    private final long mCapabilities;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private ExternalVibrationHolder mCurrentExternalVibration;

    @GuardedBy({"mLock"})
    private VibrationStepConductor mCurrentVibration;
    private final DeviceVibrationEffectAdapter mDeviceVibrationEffectAdapter;
    private final VibratorFrameworkStatsLogger mFrameworkStatsLogger;
    private final Handler mHandler;
    private final InputDeviceDelegate mInputDeviceDelegate;
    private BroadcastReceiver mIntentReceiver;
    private final Object mLock = new Object();
    private final NativeWrapper mNativeWrapper;

    @GuardedBy({"mLock"})
    private VibrationStepConductor mNextVibration;

    @GuardedBy({"mLock"})
    private boolean mServiceReady;
    private final VibrationScaler mVibrationScaler;
    private final VibrationSettings mVibrationSettings;
    private final VibrationThread mVibrationThread;
    private final VibrationThreadCallbacks mVibrationThreadCallbacks;
    private final int[] mVibratorIds;
    private final VibratorManagerRecords mVibratorManagerRecords;
    private IVibratorManagerServiceWrapper mVibratorManagerServiceWrapper;
    private final SparseArray<VibratorController> mVibrators;
    private final PowerManager.WakeLock mWakeLock;
    private IZenModeManagerExt mZenModeManagerExt;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface OnSyncedVibrationCompleteListener {
        void onComplete(long j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ VibrationEffect lambda$fixupAlwaysOnEffectsLocked$2(VibrationEffect vibrationEffect, VibratorController vibratorController) {
        return vibrationEffect;
    }

    static native void nativeCancelSynced(long j);

    static native long nativeGetCapabilities(long j);

    static native long nativeGetFinalizer();

    static native int[] nativeGetVibratorIds(long j);

    static native long nativeInit(OnSyncedVibrationCompleteListener onSyncedVibrationCompleteListener);

    static native boolean nativePrepareSynced(long j, int[] iArr);

    static native boolean nativeTriggerSynced(long j, long j2);

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Lifecycle extends SystemService {
        private VibratorManagerService mService;

        public Lifecycle(Context context) {
            super(context);
        }

        public void onStart() {
            VibratorManagerService vibratorManagerService = new VibratorManagerService(getContext(), new Injector());
            this.mService = vibratorManagerService;
            vibratorManagerService.getWrapper().getExtImpl().init(getContext());
            publishBinderService("vibrator_manager", this.mService);
        }

        public void onBootPhase(int i) {
            if (i == 500) {
                this.mService.systemReady();
            }
        }
    }

    @VisibleForTesting
    VibratorManagerService(Context context, Injector injector) {
        VibrationThreadCallbacks vibrationThreadCallbacks = new VibrationThreadCallbacks();
        this.mVibrationThreadCallbacks = vibrationThreadCallbacks;
        this.mAlwaysOnEffects = new SparseArray<>();
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.server.vibrator.VibratorManagerService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    synchronized (VibratorManagerService.this.mLock) {
                        VibratorManagerService vibratorManagerService = VibratorManagerService.this;
                        if (vibratorManagerService.shouldCancelOnScreenOffLocked(vibratorManagerService.mNextVibration)) {
                            VibratorManagerService.this.clearNextVibrationLocked(new Vibration.EndInfo(Vibration.Status.CANCELLED_BY_SCREEN_OFF));
                        }
                        VibratorManagerService vibratorManagerService2 = VibratorManagerService.this;
                        if (vibratorManagerService2.shouldCancelOnScreenOffLocked(vibratorManagerService2.mCurrentVibration)) {
                            VibratorManagerService.this.mCurrentVibration.notifyCancelled(new Vibration.EndInfo(Vibration.Status.CANCELLED_BY_SCREEN_OFF), false);
                        }
                    }
                }
            }
        };
        this.mVibratorManagerServiceWrapper = new VibratorManagerServiceWrapper();
        this.mZenModeManagerExt = (IZenModeManagerExt) ExtLoader.type(IZenModeManagerExt.class).create();
        this.mContext = context;
        Handler createHandler = injector.createHandler(Looper.myLooper());
        this.mHandler = createHandler;
        VibrationSettings vibrationSettings = new VibrationSettings(context, createHandler);
        this.mVibrationSettings = vibrationSettings;
        this.mVibrationScaler = new VibrationScaler(context, vibrationSettings);
        this.mInputDeviceDelegate = new InputDeviceDelegate(context, createHandler);
        this.mDeviceVibrationEffectAdapter = new DeviceVibrationEffectAdapter(vibrationSettings);
        VibrationCompleteListener vibrationCompleteListener = new VibrationCompleteListener(this);
        NativeWrapper nativeWrapper = injector.getNativeWrapper();
        this.mNativeWrapper = nativeWrapper;
        nativeWrapper.init(vibrationCompleteListener);
        this.mVibratorManagerRecords = new VibratorManagerRecords(context.getResources().getInteger(R.integer.leanback_setup_alpha_activity_in_bkg_delay));
        this.mBatteryStatsService = injector.getBatteryStatsService();
        this.mFrameworkStatsLogger = injector.getFrameworkStatsLogger(createHandler);
        this.mAppOps = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        PowerManager.WakeLock newWakeLock = ((PowerManager) context.getSystemService(PowerManager.class)).newWakeLock(1, "*vibrator*");
        this.mWakeLock = newWakeLock;
        newWakeLock.setReferenceCounted(true);
        VibrationThread vibrationThread = new VibrationThread(newWakeLock, vibrationThreadCallbacks);
        this.mVibrationThread = vibrationThread;
        vibrationThread.start();
        this.mCapabilities = nativeWrapper.getCapabilities();
        int[] vibratorIds = nativeWrapper.getVibratorIds();
        if (vibratorIds == null) {
            this.mVibratorIds = new int[0];
            this.mVibrators = new SparseArray<>(0);
        } else {
            this.mVibratorIds = vibratorIds;
            this.mVibrators = new SparseArray<>(vibratorIds.length);
            for (int i : vibratorIds) {
                this.mVibrators.put(i, injector.createVibratorController(i, vibrationCompleteListener));
            }
        }
        this.mNativeWrapper.cancelSynced();
        for (int i2 = 0; i2 < this.mVibrators.size(); i2++) {
            this.mVibrators.valueAt(i2).reset();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        context.registerReceiver(this.mIntentReceiver, intentFilter, 4);
        injector.addService(EXTERNAL_VIBRATOR_SERVICE, new ExternalVibratorService());
    }

    @VisibleForTesting
    void systemReady() {
        Slog.v(TAG, "Initializing VibratorManager service...");
        Trace.traceBegin(8388608L, "systemReady");
        for (int i = 0; i < this.mVibrators.size(); i++) {
            try {
                this.mVibrators.valueAt(i).reloadVibratorInfoIfNeeded();
            } catch (Throwable th) {
                synchronized (this.mLock) {
                    this.mServiceReady = true;
                    Slog.v(TAG, "VibratorManager service initialized");
                    Trace.traceEnd(8388608L);
                    throw th;
                }
            }
        }
        this.mVibrationSettings.onSystemReady();
        this.mInputDeviceDelegate.onSystemReady();
        getWrapper().getExtImpl().onSystemReady();
        this.mVibrationSettings.addListener(new VibrationSettings.OnVibratorSettingsChanged() { // from class: com.android.server.vibrator.VibratorManagerService$$ExternalSyntheticLambda0
            @Override // com.android.server.vibrator.VibrationSettings.OnVibratorSettingsChanged
            public final void onChange() {
                VibratorManagerService.this.updateServiceState();
            }
        });
        updateServiceState();
        getWrapper().getExtImpl().cancelScreenOffReceiver(this.mContext, this.mIntentReceiver);
        synchronized (this.mLock) {
            this.mServiceReady = true;
        }
        Slog.v(TAG, "VibratorManager service initialized");
        Trace.traceEnd(8388608L);
    }

    public int[] getVibratorIds() {
        int[] iArr = this.mVibratorIds;
        return Arrays.copyOf(iArr, iArr.length);
    }

    public VibratorInfo getVibratorInfo(int i) {
        VibratorController vibratorController = this.mVibrators.get(i);
        if (vibratorController == null) {
            return null;
        }
        VibratorInfo vibratorInfo = vibratorController.getVibratorInfo();
        synchronized (this.mLock) {
            if (this.mServiceReady) {
                return vibratorInfo;
            }
            if (vibratorController.isVibratorInfoLoadSuccessful()) {
                return vibratorInfo;
            }
            return null;
        }
    }

    public boolean isVibrating(int i) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_VIBRATOR_STATE", "isVibrating");
        VibratorController vibratorController = this.mVibrators.get(i);
        return vibratorController != null && vibratorController.isVibrating();
    }

    public boolean registerVibratorStateListener(int i, IVibratorStateListener iVibratorStateListener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_VIBRATOR_STATE", "registerVibratorStateListener");
        VibratorController vibratorController = this.mVibrators.get(i);
        if (vibratorController == null) {
            return false;
        }
        return vibratorController.registerVibratorStateListener(iVibratorStateListener);
    }

    public boolean unregisterVibratorStateListener(int i, IVibratorStateListener iVibratorStateListener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_VIBRATOR_STATE", "unregisterVibratorStateListener");
        VibratorController vibratorController = this.mVibrators.get(i);
        if (vibratorController == null) {
            return false;
        }
        return vibratorController.unregisterVibratorStateListener(iVibratorStateListener);
    }

    public boolean setAlwaysOnEffect(int i, String str, final int i2, CombinedVibration combinedVibration, VibrationAttributes vibrationAttributes) {
        Trace.traceBegin(8388608L, "setAlwaysOnEffect");
        try {
            this.mContext.enforceCallingOrSelfPermission("android.permission.VIBRATE_ALWAYS_ON", "setAlwaysOnEffect");
            if (combinedVibration == null) {
                synchronized (this.mLock) {
                    this.mAlwaysOnEffects.delete(i2);
                    onAllVibratorsLocked(new Consumer() { // from class: com.android.server.vibrator.VibratorManagerService$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            VibratorManagerService.lambda$setAlwaysOnEffect$0(i2, (VibratorController) obj);
                        }
                    });
                }
                return true;
            }
            if (!isEffectValid(combinedVibration)) {
                return false;
            }
            VibrationAttributes fixupVibrationAttributes = fixupVibrationAttributes(vibrationAttributes, combinedVibration);
            synchronized (this.mLock) {
                SparseArray<PrebakedSegment> fixupAlwaysOnEffectsLocked = fixupAlwaysOnEffectsLocked(combinedVibration);
                if (fixupAlwaysOnEffectsLocked == null) {
                    return false;
                }
                AlwaysOnVibration alwaysOnVibration = new AlwaysOnVibration(i2, new Vibration.CallerInfo(fixupVibrationAttributes, i, 0, str, null), fixupAlwaysOnEffectsLocked);
                this.mAlwaysOnEffects.put(i2, alwaysOnVibration);
                updateAlwaysOnLocked(alwaysOnVibration);
                return true;
            }
        } finally {
        }
        Trace.traceEnd(8388608L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setAlwaysOnEffect$0(int i, VibratorController vibratorController) {
        if (vibratorController.hasCapability(64L)) {
            vibratorController.updateAlwaysOn(i, null);
        }
    }

    public void vibrate(int i, int i2, String str, CombinedVibration combinedVibration, VibrationAttributes vibrationAttributes, String str2, IBinder iBinder) {
        vibrateInternal(i, i2, str, combinedVibration, vibrationAttributes, str2, iBinder);
    }

    @VisibleForTesting
    HalVibration vibrateInternal(int i, int i2, String str, CombinedVibration combinedVibration, VibrationAttributes vibrationAttributes, String str2, IBinder iBinder) {
        Trace.traceBegin(8388608L, "vibrate, reason = " + str2);
        try {
            this.mContext.enforceCallingOrSelfPermission("android.permission.VIBRATE", "vibrate");
            if (iBinder == null) {
                Slog.e(TAG, "token must not be null");
                return null;
            }
            enforceUpdateAppOpsStatsPermission(i);
            if (!isEffectValid(combinedVibration)) {
                return null;
            }
            getWrapper().getExtImpl().vibrate(i, Binder.getCallingPid(), str, combinedVibration, vibrationAttributes, iBinder);
            if (getWrapper().getExtImpl().ignoreVibrateForRichTapVibrationEffect(combinedVibration)) {
                return null;
            }
            if (getWrapper().getExtImpl().disposeRichtapEffectParams(combinedVibration)) {
                return null;
            }
            if (getWrapper().getExtImpl().ignoreVibrationForCamera(i, str, combinedVibration)) {
                return null;
            }
            CombinedVibration fixVibrationEffect = getWrapper().getExtImpl().fixVibrationEffect(getWrapper().getExtImpl().convertVibrationEffect(getWrapper().getExtImpl().transferEffectToWaveform(combinedVibration), vibrationAttributes, i, str, str2));
            VibrationAttributes fixupVibrationAttributes = fixupVibrationAttributes(vibrationAttributes, fixVibrationEffect);
            getWrapper().getExtImpl().fixVibrationEffectStrength(fixVibrationEffect, fixupVibrationAttributes);
            if (!isEffectValid(fixVibrationEffect)) {
                return null;
            }
            HalVibration halVibration = new HalVibration(iBinder, fixVibrationEffect, new Vibration.CallerInfo(fixupVibrationAttributes, i, i2, str, str2));
            fillVibrationFallbacks(halVibration, fixVibrationEffect);
            halVibration.getWrapper().setVibrationPid(Binder.getCallingPid());
            if (fixupVibrationAttributes.isFlagSet(4)) {
                this.mVibrationSettings.mSettingObserver.onChange(false);
            }
            synchronized (this.mLock) {
                if (DEBUG) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Starting vibrate for vibration ");
                    sb.append(halVibration.id);
                    sb.append(", ");
                    sb.append(halVibration.getEffect() == null ? "invalid" : halVibration.getEffect().toString());
                    sb.append(", ");
                    sb.append(halVibration.callerInfo);
                    Slog.d(TAG, sb.toString());
                }
                Vibration.EndInfo shouldIgnoreVibrationLocked = shouldIgnoreVibrationLocked(halVibration.callerInfo);
                if (shouldIgnoreVibrationLocked == null) {
                    shouldIgnoreVibrationLocked = shouldIgnoreVibrationForOngoingLocked(halVibration);
                }
                if (shouldIgnoreVibrationLocked == null) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        if (this.mCurrentVibration == null && !getWrapper().getExtImpl().checkIfRichtapPatternHeEffect(halVibration.getEffect())) {
                            getWrapper().getExtImpl().stopRichtapVibration();
                        }
                        ExternalVibrationHolder externalVibrationHolder = this.mCurrentExternalVibration;
                        if (externalVibrationHolder != null) {
                            externalVibrationHolder.mute();
                            halVibration.stats.reportInterruptedAnotherVibration(this.mCurrentExternalVibration.callerInfo);
                            endExternalVibrateLocked(new Vibration.EndInfo(Vibration.Status.CANCELLED_SUPERSEDED, halVibration.callerInfo), false);
                        } else {
                            VibrationStepConductor vibrationStepConductor = this.mCurrentVibration;
                            if (vibrationStepConductor != null) {
                                if (!vibrationStepConductor.getVibration().canPipelineWith(halVibration)) {
                                    halVibration.stats.reportInterruptedAnotherVibration(this.mCurrentVibration.getVibration().callerInfo);
                                    this.mCurrentVibration.notifyCancelled(new Vibration.EndInfo(Vibration.Status.CANCELLED_SUPERSEDED, halVibration.callerInfo), false);
                                } else if (DEBUG) {
                                    Slog.d(TAG, "Pipelining vibration " + halVibration.id);
                                }
                            }
                        }
                        Vibration.EndInfo startVibrationLocked = startVibrationLocked(halVibration);
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        shouldIgnoreVibrationLocked = startVibrationLocked;
                    } catch (Throwable th) {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        throw th;
                    }
                }
                if (shouldIgnoreVibrationLocked != null) {
                    endVibrationLocked(halVibration, shouldIgnoreVibrationLocked, true);
                }
            }
            return halVibration;
        } finally {
            Trace.traceEnd(8388608L);
        }
    }

    public void cancelVibrate(int i, IBinder iBinder) {
        Trace.traceBegin(8388608L, "cancelVibrate");
        try {
            this.mContext.enforceCallingOrSelfPermission("android.permission.VIBRATE", "cancelVibrate");
            synchronized (this.mLock) {
                if (DEBUG) {
                    Slog.d(TAG, "Canceling vibration");
                }
                Vibration.EndInfo endInfo = new Vibration.EndInfo(Vibration.Status.CANCELLED_BY_USER);
                getWrapper().getExtImpl().cancelVibrate(Binder.getCallingUid(), Binder.getCallingPid(), i, iBinder, this.mCurrentVibration);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    VibrationStepConductor vibrationStepConductor = this.mNextVibration;
                    if (vibrationStepConductor != null && shouldCancelVibration(vibrationStepConductor.getVibration(), i, iBinder)) {
                        clearNextVibrationLocked(endInfo);
                    }
                    VibrationStepConductor vibrationStepConductor2 = this.mCurrentVibration;
                    if (vibrationStepConductor2 != null && shouldCancelVibration(vibrationStepConductor2.getVibration(), i, iBinder)) {
                        this.mCurrentVibration.notifyCancelled(endInfo, false);
                    }
                    ExternalVibrationHolder externalVibrationHolder = this.mCurrentExternalVibration;
                    if (externalVibrationHolder != null && shouldCancelVibration(externalVibrationHolder.externalVibration.getVibrationAttributes(), i)) {
                        this.mCurrentExternalVibration.mute();
                        endExternalVibrateLocked(endInfo, false);
                    }
                    getWrapper().getExtImpl().stopRichtapVibration();
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        } finally {
            Trace.traceEnd(8388608L);
        }
    }

    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            boolean z = false;
            for (String str : strArr) {
                if (str.equals("--proto")) {
                    z = true;
                }
            }
            try {
                if (z) {
                    dumpProto(fileDescriptor);
                } else {
                    dumpText(printWriter);
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
                getWrapper().getExtImpl().dynamicallyConfigLogTag(printWriter, strArr);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        }
    }

    private void dumpText(PrintWriter printWriter) {
        if (DEBUG) {
            Slog.d(TAG, "Dumping vibrator manager service to text...");
        }
        synchronized (this.mLock) {
            printWriter.println("Vibrator Manager Service:");
            printWriter.println("  mVibrationSettings:");
            printWriter.println("    " + this.mVibrationSettings);
            printWriter.println();
            printWriter.println("  mVibratorControllers:");
            for (int i = 0; i < this.mVibrators.size(); i++) {
                printWriter.println("    " + this.mVibrators.valueAt(i));
            }
            printWriter.println();
            printWriter.println("  mCurrentVibration:");
            StringBuilder sb = new StringBuilder();
            sb.append("    ");
            VibrationStepConductor vibrationStepConductor = this.mCurrentVibration;
            Vibration.DebugInfo debugInfo = null;
            sb.append(vibrationStepConductor == null ? null : vibrationStepConductor.getVibration().getDebugInfo());
            printWriter.println(sb.toString());
            printWriter.println();
            printWriter.println("  mNextVibration:");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("    ");
            VibrationStepConductor vibrationStepConductor2 = this.mNextVibration;
            sb2.append(vibrationStepConductor2 == null ? null : vibrationStepConductor2.getVibration().getDebugInfo());
            printWriter.println(sb2.toString());
            printWriter.println();
            printWriter.println("  mCurrentExternalVibration:");
            StringBuilder sb3 = new StringBuilder();
            sb3.append("    ");
            ExternalVibrationHolder externalVibrationHolder = this.mCurrentExternalVibration;
            if (externalVibrationHolder != null) {
                debugInfo = externalVibrationHolder.getDebugInfo();
            }
            sb3.append(debugInfo);
            printWriter.println(sb3.toString());
            printWriter.println();
        }
        this.mVibratorManagerRecords.dumpText(printWriter);
    }

    synchronized void dumpProto(FileDescriptor fileDescriptor) {
        ProtoOutputStream protoOutputStream = new ProtoOutputStream(fileDescriptor);
        if (DEBUG) {
            Slog.d(TAG, "Dumping vibrator manager service to proto...");
        }
        synchronized (this.mLock) {
            this.mVibrationSettings.dumpProto(protoOutputStream);
            VibrationStepConductor vibrationStepConductor = this.mCurrentVibration;
            if (vibrationStepConductor != null) {
                vibrationStepConductor.getVibration().getDebugInfo().dumpProto(protoOutputStream, 1146756268034L);
            }
            ExternalVibrationHolder externalVibrationHolder = this.mCurrentExternalVibration;
            if (externalVibrationHolder != null) {
                externalVibrationHolder.getDebugInfo().dumpProto(protoOutputStream, 1146756268036L);
            }
            boolean z = false;
            boolean z2 = false;
            for (int i = 0; i < this.mVibrators.size(); i++) {
                protoOutputStream.write(2220498092033L, this.mVibrators.keyAt(i));
                z |= this.mVibrators.valueAt(i).isVibrating();
                z2 |= this.mVibrators.valueAt(i).isUnderExternalControl();
            }
            protoOutputStream.write(1133871366147L, z);
            protoOutputStream.write(1133871366149L, z2);
        }
        this.mVibratorManagerRecords.dumpProto(protoOutputStream);
        protoOutputStream.flush();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
        new VibratorManagerShellCommand(shellCallback.getShellCallbackBinder()).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void updateServiceState() {
        synchronized (this.mLock) {
            if (DEBUG) {
                Slog.d(TAG, "Updating device state...");
            }
            boolean updateInputDeviceVibrators = this.mInputDeviceDelegate.updateInputDeviceVibrators(this.mVibrationSettings.shouldVibrateInputDevices());
            getWrapper().getExtImpl().updateVibrator();
            for (int i = 0; i < this.mAlwaysOnEffects.size(); i++) {
                updateAlwaysOnLocked(this.mAlwaysOnEffects.valueAt(i));
            }
            VibrationStepConductor vibrationStepConductor = this.mCurrentVibration;
            if (vibrationStepConductor == null) {
                return;
            }
            Vibration.EndInfo shouldIgnoreVibrationLocked = shouldIgnoreVibrationLocked(vibrationStepConductor.getVibration().callerInfo);
            if (updateInputDeviceVibrators || shouldIgnoreVibrationLocked != null) {
                if (DEBUG) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Canceling vibration because settings changed: ");
                    sb.append(updateInputDeviceVibrators ? "input devices changed" : shouldIgnoreVibrationLocked.status);
                    Slog.d(TAG, sb.toString());
                }
                this.mCurrentVibration.notifyCancelled(new Vibration.EndInfo(Vibration.Status.CANCELLED_BY_SETTINGS_UPDATE), false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setExternalControl(boolean z, VibrationStats vibrationStats) {
        for (int i = 0; i < this.mVibrators.size(); i++) {
            this.mVibrators.valueAt(i).setExternalControl(z);
            vibrationStats.reportSetExternalControl();
        }
    }

    @GuardedBy({"mLock"})
    private void updateAlwaysOnLocked(AlwaysOnVibration alwaysOnVibration) {
        for (int i = 0; i < alwaysOnVibration.effects.size(); i++) {
            VibratorController vibratorController = this.mVibrators.get(alwaysOnVibration.effects.keyAt(i));
            PrebakedSegment valueAt = alwaysOnVibration.effects.valueAt(i);
            if (vibratorController != null) {
                vibratorController.updateAlwaysOn(alwaysOnVibration.alwaysOnId, shouldIgnoreVibrationLocked(alwaysOnVibration.callerInfo) == null ? this.mVibrationScaler.scale(valueAt, alwaysOnVibration.callerInfo.attrs.getUsage()) : null);
            }
        }
    }

    @GuardedBy({"mLock"})
    private Vibration.EndInfo startVibrationLocked(final HalVibration halVibration) {
        Trace.traceBegin(8388608L, "startVibrationLocked");
        try {
            halVibration.updateEffects(new Function() { // from class: com.android.server.vibrator.VibratorManagerService$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    VibrationEffect lambda$startVibrationLocked$1;
                    lambda$startVibrationLocked$1 = VibratorManagerService.this.lambda$startVibrationLocked$1(halVibration, (VibrationEffect) obj);
                    return lambda$startVibrationLocked$1;
                }
            });
            if (this.mInputDeviceDelegate.vibrateIfAvailable(halVibration.callerInfo, halVibration.getEffect())) {
                return new Vibration.EndInfo(Vibration.Status.FORWARDED_TO_INPUT_DEVICES);
            }
            VibrationStepConductor vibrationStepConductor = new VibrationStepConductor(halVibration, this.mVibrationSettings, this.mDeviceVibrationEffectAdapter, this.mVibrators, this.mVibrationThreadCallbacks);
            if (this.mCurrentVibration == null) {
                return startVibrationOnThreadLocked(vibrationStepConductor);
            }
            clearNextVibrationLocked(new Vibration.EndInfo(Vibration.Status.IGNORED_SUPERSEDED, halVibration.callerInfo));
            this.mNextVibration = vibrationStepConductor;
            Trace.traceEnd(8388608L);
            return null;
        } finally {
            Trace.traceEnd(8388608L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ VibrationEffect lambda$startVibrationLocked$1(HalVibration halVibration, VibrationEffect vibrationEffect) {
        return this.mVibrationScaler.scale(vibrationEffect, halVibration.callerInfo.attrs.getUsage());
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public Vibration.EndInfo startVibrationOnThreadLocked(VibrationStepConductor vibrationStepConductor) {
        Trace.traceBegin(8388608L, "startVibrationThreadLocked");
        try {
            HalVibration vibration = vibrationStepConductor.getVibration();
            int startAppOpModeLocked = startAppOpModeLocked(vibration.callerInfo);
            if (startAppOpModeLocked != 0) {
                if (startAppOpModeLocked != 2) {
                    return new Vibration.EndInfo(Vibration.Status.IGNORED_APP_OPS);
                }
                Slog.w(TAG, "Start AppOpsManager operation errored for uid " + vibration.callerInfo.uid);
                return new Vibration.EndInfo(Vibration.Status.IGNORED_ERROR_APP_OPS);
            }
            Trace.asyncTraceBegin(8388608L, "vibration", 0);
            if (getWrapper().getExtImpl().checkIfRichtapPatternHeEffect(vibration.getEffect())) {
                getWrapper().getExtImpl().startRichTapVibratorLocked(vibration);
                return null;
            }
            this.mCurrentVibration = vibrationStepConductor;
            if (this.mVibrationThread.runVibrationOnVibrationThread(vibrationStepConductor)) {
                return null;
            }
            this.mCurrentVibration = null;
            return new Vibration.EndInfo(Vibration.Status.IGNORED_ERROR_SCHEDULING);
        } finally {
            Trace.traceEnd(8388608L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void endVibrationLocked(HalVibration halVibration, Vibration.EndInfo endInfo, boolean z) {
        halVibration.end(endInfo);
        Vibration.CallerInfo callerInfo = halVibration.callerInfo;
        logVibrationStatus(callerInfo.uid, callerInfo.attrs, endInfo.status);
        this.mVibratorManagerRecords.record(halVibration);
        if (z) {
            this.mFrameworkStatsLogger.writeVibrationReportedAsync(halVibration.getStatsInfo(SystemClock.uptimeMillis()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void endVibrationAndWriteStatsLocked(ExternalVibrationHolder externalVibrationHolder, Vibration.EndInfo endInfo) {
        externalVibrationHolder.end(endInfo);
        logVibrationStatus(externalVibrationHolder.externalVibration.getUid(), externalVibrationHolder.externalVibration.getVibrationAttributes(), endInfo.status);
        this.mVibratorManagerRecords.record(externalVibrationHolder);
        this.mFrameworkStatsLogger.writeVibrationReportedAsync(externalVibrationHolder.getStatsInfo(SystemClock.uptimeMillis()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.vibrator.VibratorManagerService$2, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$server$vibrator$Vibration$Status;

        static {
            int[] iArr = new int[Vibration.Status.values().length];
            $SwitchMap$com$android$server$vibrator$Vibration$Status = iArr;
            try {
                iArr[Vibration.Status.IGNORED_BACKGROUND.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$server$vibrator$Vibration$Status[Vibration.Status.IGNORED_ERROR_APP_OPS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$server$vibrator$Vibration$Status[Vibration.Status.IGNORED_FOR_EXTERNAL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$server$vibrator$Vibration$Status[Vibration.Status.IGNORED_FOR_HIGHER_IMPORTANCE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$server$vibrator$Vibration$Status[Vibration.Status.IGNORED_FOR_ONGOING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$server$vibrator$Vibration$Status[Vibration.Status.IGNORED_FOR_RINGER_MODE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$server$vibrator$Vibration$Status[Vibration.Status.IGNORED_FROM_VIRTUAL_DEVICE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    private void logVibrationStatus(int i, VibrationAttributes vibrationAttributes, Vibration.Status status) {
        switch (AnonymousClass2.$SwitchMap$com$android$server$vibrator$Vibration$Status[status.ordinal()]) {
            case 1:
                Slog.e(TAG, "Ignoring incoming vibration as process with uid= " + i + " is background, attrs= " + vibrationAttributes);
                return;
            case 2:
                Slog.w(TAG, "Would be an error: vibrate from uid " + i);
                return;
            case 3:
                if (DEBUG) {
                    Slog.d(TAG, "Ignoring incoming vibration for current external vibration");
                    return;
                }
                return;
            case 4:
                if (DEBUG) {
                    Slog.d(TAG, "Ignoring incoming vibration in favor of ongoing vibration with higher importance");
                    return;
                }
                return;
            case 5:
                if (DEBUG) {
                    Slog.d(TAG, "Ignoring incoming vibration in favor of repeating vibration");
                    return;
                }
                return;
            case 6:
                if (DEBUG) {
                    Slog.d(TAG, "Ignoring incoming vibration because of ringer mode, attrs=" + vibrationAttributes);
                    return;
                }
                return;
            case 7:
                if (DEBUG) {
                    Slog.d(TAG, "Ignoring incoming vibration because it came from a virtual device, attrs= " + vibrationAttributes);
                    return;
                }
                return;
            default:
                if (DEBUG) {
                    Slog.d(TAG, "Vibration for uid=" + i + " and with attrs=" + vibrationAttributes + " ended with status " + status);
                    return;
                }
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void reportFinishedVibrationLocked(Vibration.EndInfo endInfo) {
        Trace.traceBegin(8388608L, "reportFinishVibrationLocked");
        Trace.asyncTraceEnd(8388608L, "vibration", 0);
        try {
            HalVibration vibration = this.mCurrentVibration.getVibration();
            if (DEBUG) {
                Slog.d(TAG, "Reporting vibration " + vibration.id + " finished with " + endInfo);
            }
            endVibrationLocked(vibration, endInfo, false);
            finishAppOpModeLocked(vibration.callerInfo);
        } finally {
            Trace.traceEnd(8388608L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSyncedVibrationComplete(long j) {
        synchronized (this.mLock) {
            VibrationStepConductor vibrationStepConductor = this.mCurrentVibration;
            if (vibrationStepConductor != null && vibrationStepConductor.getVibration().id == j) {
                if (DEBUG) {
                    Slog.d(TAG, "Synced vibration " + j + " complete, notifying thread");
                }
                this.mCurrentVibration.notifySyncedVibrationComplete();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVibrationComplete(int i, long j) {
        synchronized (this.mLock) {
            VibrationStepConductor vibrationStepConductor = this.mCurrentVibration;
            if (vibrationStepConductor != null && vibrationStepConductor.getVibration().id == j) {
                if (DEBUG) {
                    Slog.d(TAG, "Vibration " + j + " on vibrator " + i + " complete, notifying thread");
                }
                this.mCurrentVibration.notifyVibratorComplete(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public Vibration.EndInfo shouldIgnoreVibrationForOngoingLocked(Vibration vibration) {
        Vibration.EndInfo shouldIgnoreVibrationForOngoing;
        ExternalVibrationHolder externalVibrationHolder = this.mCurrentExternalVibration;
        if (externalVibrationHolder != null) {
            return shouldIgnoreVibrationForOngoing(vibration, externalVibrationHolder);
        }
        VibrationStepConductor vibrationStepConductor = this.mNextVibration;
        if (vibrationStepConductor != null && (shouldIgnoreVibrationForOngoing = shouldIgnoreVibrationForOngoing(vibration, vibrationStepConductor.getVibration())) != null) {
            return shouldIgnoreVibrationForOngoing;
        }
        VibrationStepConductor vibrationStepConductor2 = this.mCurrentVibration;
        if (vibrationStepConductor2 != null) {
            HalVibration vibration2 = vibrationStepConductor2.getVibration();
            if (!vibration2.hasEnded() && !this.mCurrentVibration.wasNotifiedToCancel()) {
                return shouldIgnoreVibrationForOngoing(vibration, vibration2);
            }
        }
        return null;
    }

    private static Vibration.EndInfo shouldIgnoreVibrationForOngoing(Vibration vibration, Vibration vibration2) {
        int vibrationImportance = getVibrationImportance(vibration);
        int vibrationImportance2 = getVibrationImportance(vibration2);
        if (vibrationImportance > vibrationImportance2) {
            return null;
        }
        if (vibrationImportance2 > vibrationImportance) {
            return new Vibration.EndInfo(Vibration.Status.IGNORED_FOR_HIGHER_IMPORTANCE, vibration2.callerInfo);
        }
        if (!vibration2.isRepeating() || vibration.isRepeating()) {
            return null;
        }
        return new Vibration.EndInfo(Vibration.Status.IGNORED_FOR_ONGOING, vibration2.callerInfo);
    }

    private static int getVibrationImportance(Vibration vibration) {
        int usage = vibration.callerInfo.attrs.getUsage();
        if (usage == 0) {
            usage = vibration.isRepeating() ? 33 : 18;
        }
        if (usage == 17) {
            return 4;
        }
        if (usage == 33) {
            return 5;
        }
        if (usage == 34) {
            return 1;
        }
        if (usage == 49) {
            return 3;
        }
        if (usage != 50) {
            return (usage == 65 || usage == 66) ? 2 : 0;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public Vibration.EndInfo shouldIgnoreVibrationLocked(Vibration.CallerInfo callerInfo) {
        Vibration.Status shouldIgnoreVibration = this.mVibrationSettings.shouldIgnoreVibration(callerInfo);
        if (shouldIgnoreVibration != null) {
            return new Vibration.EndInfo(shouldIgnoreVibration);
        }
        if (!this.mZenModeManagerExt.canVibrationGo(callerInfo.opPkg)) {
            return new Vibration.EndInfo(Vibration.Status.IGNORED_FOR_SETTINGS);
        }
        if (getWrapper().getExtImpl().isBlockedByApplicationLocked()) {
            return new Vibration.EndInfo(Vibration.Status.IGNORED_FOR_HIGHER_IMPORTANCE);
        }
        int checkAppOpModeLocked = checkAppOpModeLocked(callerInfo);
        if (checkAppOpModeLocked == 0) {
            return null;
        }
        if (checkAppOpModeLocked == 2) {
            return new Vibration.EndInfo(Vibration.Status.IGNORED_ERROR_APP_OPS);
        }
        return new Vibration.EndInfo(Vibration.Status.IGNORED_APP_OPS);
    }

    private boolean shouldCancelVibration(HalVibration halVibration, int i, IBinder iBinder) {
        return halVibration.callerToken == iBinder && shouldCancelVibration(halVibration.callerInfo.attrs, i);
    }

    private boolean shouldCancelVibration(VibrationAttributes vibrationAttributes, int i) {
        return vibrationAttributes.getUsage() == 0 ? i == 0 || i == -1 : (vibrationAttributes.getUsage() & i) == vibrationAttributes.getUsage();
    }

    @GuardedBy({"mLock"})
    private int checkAppOpModeLocked(Vibration.CallerInfo callerInfo) {
        int checkAudioOpNoThrow = this.mAppOps.checkAudioOpNoThrow(3, callerInfo.attrs.getAudioUsage(), callerInfo.uid, callerInfo.opPkg);
        int fixupAppOpModeLocked = fixupAppOpModeLocked(checkAudioOpNoThrow, callerInfo.attrs);
        if (checkAudioOpNoThrow != fixupAppOpModeLocked && fixupAppOpModeLocked == 0) {
            Slog.d(TAG, "Bypassing DND for vibrate from uid " + callerInfo.uid);
        }
        return fixupAppOpModeLocked;
    }

    @GuardedBy({"mLock"})
    private int startAppOpModeLocked(Vibration.CallerInfo callerInfo) {
        return fixupAppOpModeLocked(this.mAppOps.startOpNoThrow(3, callerInfo.uid, callerInfo.opPkg), callerInfo.attrs);
    }

    @GuardedBy({"mLock"})
    private void finishAppOpModeLocked(Vibration.CallerInfo callerInfo) {
        this.mAppOps.finishOp(3, callerInfo.uid, callerInfo.opPkg);
    }

    private void enforceUpdateAppOpsStatsPermission(int i) {
        if (i == Binder.getCallingUid() || Binder.getCallingPid() == Process.myPid()) {
            return;
        }
        this.mContext.enforcePermission("android.permission.UPDATE_APP_OPS_STATS", Binder.getCallingPid(), Binder.getCallingUid(), null);
    }

    private static boolean isEffectValid(CombinedVibration combinedVibration) {
        if (combinedVibration == null) {
            Slog.wtf(TAG, "effect must not be null");
            return false;
        }
        try {
            combinedVibration.validate();
            return true;
        } catch (Exception e) {
            Slog.wtf(TAG, "Encountered issue when verifying CombinedVibrationEffect.", e);
            return false;
        }
    }

    private void fillVibrationFallbacks(HalVibration halVibration, CombinedVibration combinedVibration) {
        if (combinedVibration instanceof CombinedVibration.Mono) {
            fillVibrationFallbacks(halVibration, ((CombinedVibration.Mono) combinedVibration).getEffect());
            return;
        }
        int i = 0;
        if (combinedVibration instanceof CombinedVibration.Stereo) {
            SparseArray effects = ((CombinedVibration.Stereo) combinedVibration).getEffects();
            while (i < effects.size()) {
                fillVibrationFallbacks(halVibration, (VibrationEffect) effects.valueAt(i));
                i++;
            }
            return;
        }
        if (combinedVibration instanceof CombinedVibration.Sequential) {
            List effects2 = ((CombinedVibration.Sequential) combinedVibration).getEffects();
            while (i < effects2.size()) {
                fillVibrationFallbacks(halVibration, (CombinedVibration) effects2.get(i));
                i++;
            }
        }
    }

    private void fillVibrationFallbacks(HalVibration halVibration, VibrationEffect vibrationEffect) {
        VibrationEffect.Composed composed = (VibrationEffect.Composed) vibrationEffect;
        int size = composed.getSegments().size();
        for (int i = 0; i < size; i++) {
            PrebakedSegment prebakedSegment = (VibrationEffectSegment) composed.getSegments().get(i);
            if (prebakedSegment instanceof PrebakedSegment) {
                PrebakedSegment prebakedSegment2 = prebakedSegment;
                VibrationEffect fallbackEffect = this.mVibrationSettings.getFallbackEffect(prebakedSegment2.getEffectId());
                if (prebakedSegment2.shouldFallback() && fallbackEffect != null) {
                    halVibration.addFallback(prebakedSegment2.getEffectId(), fallbackEffect);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public VibrationAttributes fixupVibrationAttributes(VibrationAttributes vibrationAttributes, CombinedVibration combinedVibration) {
        if (vibrationAttributes == null) {
            vibrationAttributes = DEFAULT_ATTRIBUTES;
        }
        vibrationAttributes.getUsage();
        int fixupVibrationAttributes = getWrapper().getExtImpl().fixupVibrationAttributes(vibrationAttributes, combinedVibration);
        if (fixupVibrationAttributes == 0 && combinedVibration != null && combinedVibration.isHapticFeedbackCandidate()) {
            fixupVibrationAttributes = 18;
        }
        int flags = vibrationAttributes.getFlags();
        if ((flags & 3) != 0 && !hasPermission("android.permission.WRITE_SECURE_SETTINGS") && !hasPermission("android.permission.MODIFY_PHONE_STATE") && !hasPermission("android.permission.MODIFY_AUDIO_ROUTING")) {
            flags &= -4;
        }
        return (fixupVibrationAttributes == vibrationAttributes.getUsage() && flags == vibrationAttributes.getFlags()) ? vibrationAttributes : new VibrationAttributes.Builder(vibrationAttributes).setUsage(fixupVibrationAttributes).setFlags(flags, vibrationAttributes.getFlags()).build();
    }

    @GuardedBy({"mLock"})
    private SparseArray<PrebakedSegment> fixupAlwaysOnEffectsLocked(CombinedVibration combinedVibration) {
        SparseArray effects;
        Trace.traceBegin(8388608L, "fixupAlwaysOnEffectsLocked");
        try {
            if (combinedVibration instanceof CombinedVibration.Mono) {
                final VibrationEffect effect = ((CombinedVibration.Mono) combinedVibration).getEffect();
                effects = transformAllVibratorsLocked(new Function() { // from class: com.android.server.vibrator.VibratorManagerService$$ExternalSyntheticLambda3
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        VibrationEffect lambda$fixupAlwaysOnEffectsLocked$2;
                        lambda$fixupAlwaysOnEffectsLocked$2 = VibratorManagerService.lambda$fixupAlwaysOnEffectsLocked$2(effect, (VibratorController) obj);
                        return lambda$fixupAlwaysOnEffectsLocked$2;
                    }
                });
            } else {
                if (!(combinedVibration instanceof CombinedVibration.Stereo)) {
                    return null;
                }
                effects = ((CombinedVibration.Stereo) combinedVibration).getEffects();
            }
            SparseArray<PrebakedSegment> sparseArray = new SparseArray<>();
            for (int i = 0; i < effects.size(); i++) {
                PrebakedSegment extractPrebakedSegment = extractPrebakedSegment((VibrationEffect) effects.valueAt(i));
                if (extractPrebakedSegment == null) {
                    Slog.e(TAG, "Only prebaked effects supported for always-on.");
                    return null;
                }
                int keyAt = effects.keyAt(i);
                VibratorController vibratorController = this.mVibrators.get(keyAt);
                if (vibratorController != null && vibratorController.hasCapability(64L)) {
                    sparseArray.put(keyAt, extractPrebakedSegment);
                }
            }
            if (sparseArray.size() == 0) {
                return null;
            }
            return sparseArray;
        } finally {
            Trace.traceEnd(8388608L);
        }
    }

    private static PrebakedSegment extractPrebakedSegment(VibrationEffect vibrationEffect) {
        if (!(vibrationEffect instanceof VibrationEffect.Composed)) {
            return null;
        }
        VibrationEffect.Composed composed = (VibrationEffect.Composed) vibrationEffect;
        if (composed.getSegments().size() != 1) {
            return null;
        }
        PrebakedSegment prebakedSegment = (VibrationEffectSegment) composed.getSegments().get(0);
        if (prebakedSegment instanceof PrebakedSegment) {
            return prebakedSegment;
        }
        return null;
    }

    @GuardedBy({"mLock"})
    private int fixupAppOpModeLocked(int i, VibrationAttributes vibrationAttributes) {
        if (i == 1 && vibrationAttributes.isFlagSet(1)) {
            return 0;
        }
        return i;
    }

    private boolean hasPermission(String str) {
        return this.mContext.checkCallingOrSelfPermission(str) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public boolean shouldCancelOnScreenOffLocked(VibrationStepConductor vibrationStepConductor) {
        if (vibrationStepConductor == null) {
            return false;
        }
        HalVibration vibration = vibrationStepConductor.getVibration();
        return this.mVibrationSettings.shouldCancelVibrationOnScreenOff(vibration.callerInfo, vibration.stats.getCreateUptimeMillis());
    }

    @GuardedBy({"mLock"})
    private void onAllVibratorsLocked(Consumer<VibratorController> consumer) {
        for (int i = 0; i < this.mVibrators.size(); i++) {
            consumer.accept(this.mVibrators.valueAt(i));
        }
    }

    @GuardedBy({"mLock"})
    private <T> SparseArray<T> transformAllVibratorsLocked(Function<VibratorController, T> function) {
        SparseArray<T> sparseArray = new SparseArray<>(this.mVibrators.size());
        for (int i = 0; i < this.mVibrators.size(); i++) {
            sparseArray.put(this.mVibrators.keyAt(i), function.apply(this.mVibrators.valueAt(i)));
        }
        return sparseArray;
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class Injector {
        Injector() {
        }

        NativeWrapper getNativeWrapper() {
            return new NativeWrapper();
        }

        Handler createHandler(Looper looper) {
            return new Handler(looper);
        }

        IBatteryStats getBatteryStatsService() {
            return IBatteryStats.Stub.asInterface(ServiceManager.getService("batterystats"));
        }

        VibratorFrameworkStatsLogger getFrameworkStatsLogger(Handler handler) {
            return new VibratorFrameworkStatsLogger(handler);
        }

        VibratorController createVibratorController(int i, VibratorController.OnVibrationCompleteListener onVibrationCompleteListener) {
            return new VibratorController(i, onVibrationCompleteListener);
        }

        void addService(String str, IBinder iBinder) {
            ServiceManager.addService(str, iBinder);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class VibrationThreadCallbacks implements VibrationThread.VibratorManagerHooks {
        private VibrationThreadCallbacks() {
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public boolean prepareSyncedVibration(long j, int[] iArr) {
            if ((VibratorManagerService.this.mCapabilities & j) != j) {
                return false;
            }
            return VibratorManagerService.this.mNativeWrapper.prepareSynced(iArr);
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public boolean triggerSyncedVibration(long j) {
            return VibratorManagerService.this.mNativeWrapper.triggerSynced(j);
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public void cancelSyncedVibration() {
            VibratorManagerService.this.mNativeWrapper.cancelSynced();
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public void noteVibratorOn(int i, long j) {
            if (j <= 0) {
                return;
            }
            if (j == JobStatus.NO_LATEST_RUNTIME) {
                j = 5000;
            }
            try {
                VibratorManagerService.this.mBatteryStatsService.noteVibratorOn(i, j);
                VibratorManagerService.this.mFrameworkStatsLogger.writeVibratorStateOnAsync(i, j);
            } catch (RemoteException e) {
                Slog.e(VibratorManagerService.TAG, "Error logging VibratorStateChanged to ON", e);
            }
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public void noteVibratorOff(int i) {
            try {
                VibratorManagerService.this.mBatteryStatsService.noteVibratorOff(i);
                VibratorManagerService.this.mFrameworkStatsLogger.writeVibratorStateOffAsync(i);
            } catch (RemoteException e) {
                Slog.e(VibratorManagerService.TAG, "Error logging VibratorStateChanged to OFF", e);
            }
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public void onVibrationCompleted(long j, Vibration.EndInfo endInfo) {
            if (VibratorManagerService.DEBUG) {
                Slog.d(VibratorManagerService.TAG, "Vibration " + j + " finished with " + endInfo);
            }
            synchronized (VibratorManagerService.this.mLock) {
                if (VibratorManagerService.this.mCurrentVibration != null && VibratorManagerService.this.mCurrentVibration.getVibration().id == j) {
                    VibratorManagerService.this.reportFinishedVibrationLocked(endInfo);
                }
            }
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public void onVibrationThreadReleased(long j) {
            if (VibratorManagerService.DEBUG) {
                Slog.d(VibratorManagerService.TAG, "VibrationThread released after finished vibration");
            }
            synchronized (VibratorManagerService.this.mLock) {
                if (VibratorManagerService.DEBUG) {
                    Slog.d(VibratorManagerService.TAG, "Processing VibrationThread released callback");
                }
                if (Build.IS_DEBUGGABLE && VibratorManagerService.this.mCurrentVibration != null && VibratorManagerService.this.mCurrentVibration.getVibration().id != j) {
                    Slog.wtf(VibratorManagerService.TAG, TextUtils.formatSimple("VibrationId mismatch on release. expected=%d, released=%d", new Object[]{Long.valueOf(VibratorManagerService.this.mCurrentVibration.getVibration().id), Long.valueOf(j)}));
                }
                if (VibratorManagerService.this.mCurrentVibration != null) {
                    VibratorManagerService.this.mFrameworkStatsLogger.writeVibrationReportedAsync(VibratorManagerService.this.mCurrentVibration.getVibration().getStatsInfo(SystemClock.uptimeMillis()));
                    VibratorManagerService.this.mCurrentVibration = null;
                }
                if (VibratorManagerService.this.mNextVibration != null) {
                    VibrationStepConductor vibrationStepConductor = VibratorManagerService.this.mNextVibration;
                    VibratorManagerService.this.mNextVibration = null;
                    Vibration.EndInfo startVibrationOnThreadLocked = VibratorManagerService.this.startVibrationOnThreadLocked(vibrationStepConductor);
                    if (startVibrationOnThreadLocked != null) {
                        VibratorManagerService.this.endVibrationLocked(vibrationStepConductor.getVibration(), startVibrationOnThreadLocked, true);
                    }
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class VibrationCompleteListener implements VibratorController.OnVibrationCompleteListener, OnSyncedVibrationCompleteListener {
        private WeakReference<VibratorManagerService> mServiceRef;

        VibrationCompleteListener(VibratorManagerService vibratorManagerService) {
            this.mServiceRef = new WeakReference<>(vibratorManagerService);
        }

        @Override // com.android.server.vibrator.VibratorManagerService.OnSyncedVibrationCompleteListener
        public void onComplete(long j) {
            VibratorManagerService vibratorManagerService = this.mServiceRef.get();
            if (vibratorManagerService != null) {
                vibratorManagerService.onSyncedVibrationComplete(j);
            }
        }

        @Override // com.android.server.vibrator.VibratorController.OnVibrationCompleteListener
        public void onComplete(int i, long j) {
            VibratorManagerService vibratorManagerService = this.mServiceRef.get();
            if (vibratorManagerService != null) {
                vibratorManagerService.onVibrationComplete(i, j);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class AlwaysOnVibration {
        public final int alwaysOnId;
        public final Vibration.CallerInfo callerInfo;
        public final SparseArray<PrebakedSegment> effects;

        AlwaysOnVibration(int i, Vibration.CallerInfo callerInfo, SparseArray<PrebakedSegment> sparseArray) {
            this.alwaysOnId = i;
            this.callerInfo = callerInfo;
            this.effects = sparseArray;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class ExternalVibrationHolder extends Vibration implements IBinder.DeathRecipient {
        public final ExternalVibration externalVibration;
        private Vibration.Status mStatus;
        public int scale;

        private ExternalVibrationHolder(ExternalVibration externalVibration) {
            super(externalVibration.getToken(), new Vibration.CallerInfo(externalVibration.getVibrationAttributes(), externalVibration.getUid(), -1, externalVibration.getPackage(), null));
            this.externalVibration = externalVibration;
            this.scale = 0;
            this.mStatus = Vibration.Status.RUNNING;
        }

        public void mute() {
            this.externalVibration.mute();
        }

        public void linkToDeath() {
            this.externalVibration.linkToDeath(this);
        }

        public void unlinkToDeath() {
            this.externalVibration.unlinkToDeath(this);
        }

        public boolean isHoldingSameVibration(ExternalVibration externalVibration) {
            return this.externalVibration.equals(externalVibration);
        }

        public void end(Vibration.EndInfo endInfo) {
            if (this.mStatus != Vibration.Status.RUNNING) {
                return;
            }
            this.mStatus = endInfo.status;
            this.stats.reportEnded(endInfo.endedBy);
            if (this.stats.hasStarted()) {
                VibrationStats vibrationStats = this.stats;
                vibrationStats.reportVibratorOn(vibrationStats.getEndUptimeMillis() - this.stats.getStartUptimeMillis());
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (VibratorManagerService.this.mLock) {
                if (VibratorManagerService.this.mCurrentExternalVibration != null) {
                    if (VibratorManagerService.DEBUG) {
                        Slog.d(VibratorManagerService.TAG, "External vibration finished because binder died");
                    }
                    VibratorManagerService.this.endExternalVibrateLocked(new Vibration.EndInfo(Vibration.Status.CANCELLED_BINDER_DIED), false);
                }
            }
        }

        public Vibration.DebugInfo getDebugInfo() {
            return new Vibration.DebugInfo(this.mStatus, this.stats, null, null, this.scale, this.callerInfo);
        }

        public VibrationStats.StatsInfo getStatsInfo(long j) {
            return new VibrationStats.StatsInfo(this.externalVibration.getUid(), 3, this.externalVibration.getVibrationAttributes().getUsage(), this.mStatus, this.stats, j);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.android.server.vibrator.Vibration
        public boolean isRepeating() {
            int usage = this.externalVibration.getVibrationAttributes().getUsage();
            return usage == 33 || usage == 17;
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class NativeWrapper {
        private long mNativeServicePtr = 0;

        public void init(OnSyncedVibrationCompleteListener onSyncedVibrationCompleteListener) {
            this.mNativeServicePtr = VibratorManagerService.nativeInit(onSyncedVibrationCompleteListener);
            long nativeGetFinalizer = VibratorManagerService.nativeGetFinalizer();
            if (nativeGetFinalizer != 0) {
                NativeAllocationRegistry.createMalloced(VibratorManagerService.class.getClassLoader(), nativeGetFinalizer).registerNativeAllocation(this, this.mNativeServicePtr);
            }
        }

        public long getCapabilities() {
            return VibratorManagerService.nativeGetCapabilities(this.mNativeServicePtr);
        }

        public int[] getVibratorIds() {
            return VibratorManagerService.nativeGetVibratorIds(this.mNativeServicePtr);
        }

        public boolean prepareSynced(int[] iArr) {
            return VibratorManagerService.nativePrepareSynced(this.mNativeServicePtr, iArr);
        }

        public boolean triggerSynced(long j) {
            return VibratorManagerService.nativeTriggerSynced(this.mNativeServicePtr, j);
        }

        public void cancelSynced() {
            VibratorManagerService.nativeCancelSynced(this.mNativeServicePtr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class VibratorManagerRecords {
        private final int mPreviousVibrationsLimit;
        private final SparseArray<LinkedList<Vibration.DebugInfo>> mPreviousVibrations = new SparseArray<>();
        private final LinkedList<Vibration.DebugInfo> mPreviousExternalVibrations = new LinkedList<>();

        VibratorManagerRecords(int i) {
            this.mPreviousVibrationsLimit = i;
        }

        synchronized void record(HalVibration halVibration) {
            int usage = halVibration.callerInfo.attrs.getUsage();
            if (!this.mPreviousVibrations.contains(usage)) {
                this.mPreviousVibrations.put(usage, new LinkedList<>());
            }
            record(this.mPreviousVibrations.get(usage), halVibration.getDebugInfo());
        }

        synchronized void record(ExternalVibrationHolder externalVibrationHolder) {
            record(this.mPreviousExternalVibrations, externalVibrationHolder.getDebugInfo());
        }

        synchronized void record(LinkedList<Vibration.DebugInfo> linkedList, Vibration.DebugInfo debugInfo) {
            if (linkedList.size() > this.mPreviousVibrationsLimit) {
                linkedList.removeFirst();
            }
            linkedList.addLast(debugInfo);
        }

        synchronized void dumpText(PrintWriter printWriter) {
            for (int i = 0; i < this.mPreviousVibrations.size(); i++) {
                printWriter.println();
                printWriter.print("  Previous vibrations for usage ");
                printWriter.print(VibrationAttributes.usageToString(this.mPreviousVibrations.keyAt(i)));
                printWriter.println(":");
                Iterator<Vibration.DebugInfo> it = this.mPreviousVibrations.valueAt(i).iterator();
                while (it.hasNext()) {
                    printWriter.println("    " + it.next());
                }
            }
            printWriter.println();
            printWriter.println("  Previous external vibrations:");
            Iterator<Vibration.DebugInfo> it2 = this.mPreviousExternalVibrations.iterator();
            while (it2.hasNext()) {
                printWriter.println("    " + it2.next());
            }
        }

        synchronized void dumpProto(ProtoOutputStream protoOutputStream) {
            for (int i = 0; i < this.mPreviousVibrations.size(); i++) {
                int keyAt = this.mPreviousVibrations.keyAt(i);
                long j = keyAt != 17 ? keyAt != 33 ? keyAt != 49 ? 2246267895824L : 2246267895822L : 2246267895821L : 2246267895823L;
                Iterator<Vibration.DebugInfo> it = this.mPreviousVibrations.valueAt(i).iterator();
                while (it.hasNext()) {
                    it.next().dumpProto(protoOutputStream, j);
                }
            }
            Iterator<Vibration.DebugInfo> it2 = this.mPreviousExternalVibrations.iterator();
            while (it2.hasNext()) {
                it2.next().dumpProto(protoOutputStream, 2246267895825L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void clearNextVibrationLocked(Vibration.EndInfo endInfo) {
        if (this.mNextVibration != null) {
            if (DEBUG) {
                Slog.d(TAG, "Dropping pending vibration " + this.mNextVibration.getVibration().id + " with end info: " + endInfo);
            }
            endVibrationLocked(this.mNextVibration.getVibration(), endInfo, true);
            this.mNextVibration = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void endExternalVibrateLocked(Vibration.EndInfo endInfo, boolean z) {
        Trace.traceBegin(8388608L, "endExternalVibrateLocked");
        try {
            ExternalVibrationHolder externalVibrationHolder = this.mCurrentExternalVibration;
            if (externalVibrationHolder == null) {
                return;
            }
            externalVibrationHolder.unlinkToDeath();
            if (!z) {
                setExternalControl(false, this.mCurrentExternalVibration.stats);
            }
            endVibrationAndWriteStatsLocked(this.mCurrentExternalVibration, endInfo);
            this.mCurrentExternalVibration = null;
        } finally {
            Trace.traceEnd(8388608L);
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class ExternalVibratorService extends IExternalVibratorService.Stub {
        ExternalVibratorService() {
        }

        public int onExternalVibrationStart(ExternalVibration externalVibration) {
            boolean z;
            boolean z2;
            if (!hasExternalControlCapability()) {
                return -100;
            }
            if (ActivityManager.checkComponentPermission("android.permission.VIBRATE", externalVibration.getUid(), -1, true) != 0) {
                Slog.w(VibratorManagerService.TAG, "pkg=" + externalVibration.getPackage() + ", uid=" + externalVibration.getUid() + " tried to play externally controlled vibration without VIBRATE permission, ignoring.");
                return -100;
            }
            ExternalVibrationHolder externalVibrationHolder = new ExternalVibrationHolder(externalVibration);
            VibrationAttributes fixupVibrationAttributes = VibratorManagerService.this.fixupVibrationAttributes(externalVibration.getVibrationAttributes(), null);
            if (fixupVibrationAttributes.isFlagSet(4)) {
                VibratorManagerService.this.mVibrationSettings.update();
            }
            synchronized (VibratorManagerService.this.mLock) {
                Vibration.EndInfo shouldIgnoreVibrationLocked = VibratorManagerService.this.shouldIgnoreVibrationLocked(externalVibrationHolder.callerInfo);
                if (shouldIgnoreVibrationLocked == null && VibratorManagerService.this.mCurrentExternalVibration != null && VibratorManagerService.this.mCurrentExternalVibration.isHoldingSameVibration(externalVibration)) {
                    return VibratorManagerService.this.mCurrentExternalVibration.scale;
                }
                if (shouldIgnoreVibrationLocked == null) {
                    shouldIgnoreVibrationLocked = VibratorManagerService.this.shouldIgnoreVibrationForOngoingLocked(externalVibrationHolder);
                }
                if (shouldIgnoreVibrationLocked != null) {
                    externalVibrationHolder.scale = -100;
                    VibratorManagerService.this.endVibrationAndWriteStatsLocked(externalVibrationHolder, shouldIgnoreVibrationLocked);
                    return externalVibrationHolder.scale;
                }
                if (VibratorManagerService.this.mCurrentExternalVibration == null) {
                    if (VibratorManagerService.this.mCurrentVibration != null) {
                        externalVibrationHolder.stats.reportInterruptedAnotherVibration(VibratorManagerService.this.mCurrentVibration.getVibration().callerInfo);
                        VibratorManagerService.this.clearNextVibrationLocked(new Vibration.EndInfo(Vibration.Status.IGNORED_FOR_EXTERNAL, externalVibrationHolder.callerInfo));
                        VibratorManagerService.this.mCurrentVibration.notifyCancelled(new Vibration.EndInfo(Vibration.Status.CANCELLED_SUPERSEDED, externalVibrationHolder.callerInfo), true);
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (VibratorManagerService.this.mCurrentVibration == null) {
                        VibratorManagerService.this.getWrapper().getExtImpl().stopRichtapVibration();
                    }
                    z = false;
                } else {
                    VibratorManagerService.this.mCurrentExternalVibration.mute();
                    externalVibrationHolder.stats.reportInterruptedAnotherVibration(VibratorManagerService.this.mCurrentExternalVibration.callerInfo);
                    VibratorManagerService.this.endExternalVibrateLocked(new Vibration.EndInfo(Vibration.Status.CANCELLED_SUPERSEDED, externalVibrationHolder.callerInfo), true);
                    z = true;
                    z2 = false;
                }
                VibratorManagerService.this.mCurrentExternalVibration = externalVibrationHolder;
                externalVibrationHolder.linkToDeath();
                externalVibrationHolder.scale = VibratorManagerService.this.mVibrationScaler.getExternalVibrationScale(fixupVibrationAttributes.getUsage());
                if (z2 && !VibratorManagerService.this.mVibrationThread.waitForThreadIdle(5000L)) {
                    Slog.e(VibratorManagerService.TAG, "Timed out waiting for vibration to cancel");
                    synchronized (VibratorManagerService.this.mLock) {
                        VibratorManagerService.this.endExternalVibrateLocked(new Vibration.EndInfo(Vibration.Status.IGNORED_ERROR_CANCELLING), false);
                    }
                    return -100;
                }
                if (!z) {
                    if (VibratorManagerService.DEBUG) {
                        Slog.d(VibratorManagerService.TAG, "Vibrator going under external control.");
                    }
                    VibratorManagerService.this.setExternalControl(true, externalVibrationHolder.stats);
                }
                if (VibratorManagerService.DEBUG) {
                    Slog.d(VibratorManagerService.TAG, "Playing external vibration: " + externalVibration);
                }
                externalVibrationHolder.stats.reportStarted();
                return externalVibrationHolder.scale;
            }
        }

        public void onExternalVibrationStop(ExternalVibration externalVibration) {
            synchronized (VibratorManagerService.this.mLock) {
                if (VibratorManagerService.this.mCurrentExternalVibration != null && VibratorManagerService.this.mCurrentExternalVibration.isHoldingSameVibration(externalVibration)) {
                    if (VibratorManagerService.DEBUG) {
                        Slog.d(VibratorManagerService.TAG, "Stopping external vibration: " + externalVibration);
                    }
                    VibratorManagerService.this.endExternalVibrateLocked(new Vibration.EndInfo(Vibration.Status.FINISHED), false);
                }
            }
        }

        private boolean hasExternalControlCapability() {
            for (int i = 0; i < VibratorManagerService.this.mVibrators.size(); i++) {
                if (((VibratorController) VibratorManagerService.this.mVibrators.valueAt(i)).hasCapability(8L)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class VibratorManagerShellCommand extends ShellCommand {
        public static final String SHELL_PACKAGE_NAME = "com.android.shell";
        private final IBinder mShellCallbacksToken;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public final class CommonOptions {
            public boolean background;
            public String description;
            public boolean force;

            /* JADX WARN: Removed duplicated region for block: B:16:0x0041 A[SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:19:0x0048 A[SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:23:0x0052 A[SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:26:0x0042 A[SYNTHETIC] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            CommonOptions() {
                this.force = false;
                this.description = NotificationShellCmd.CHANNEL_NAME;
                this.background = false;
                while (true) {
                    String peekNextArg = VibratorManagerShellCommand.this.peekNextArg();
                    if (peekNextArg != null) {
                        char c = 65535;
                        switch (peekNextArg.hashCode()) {
                            case 1461:
                                if (peekNextArg.equals("-B")) {
                                    c = 0;
                                }
                                switch (c) {
                                    case 0:
                                        VibratorManagerShellCommand.this.getNextArgRequired();
                                        this.background = true;
                                        break;
                                    case 1:
                                        VibratorManagerShellCommand.this.getNextArgRequired();
                                        this.description = VibratorManagerShellCommand.this.getNextArgRequired();
                                        break;
                                    case 2:
                                        VibratorManagerShellCommand.this.getNextArgRequired();
                                        this.force = true;
                                        break;
                                    default:
                                        return;
                                }
                            case 1495:
                                if (peekNextArg.equals("-d")) {
                                    c = 1;
                                }
                                switch (c) {
                                }
                                break;
                            case 1497:
                                if (peekNextArg.equals("-f")) {
                                    c = 2;
                                }
                                switch (c) {
                                }
                                break;
                            default:
                                switch (c) {
                                }
                                break;
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        private VibratorManagerShellCommand(IBinder iBinder) {
            this.mShellCallbacksToken = iBinder;
        }

        public int onCommand(String str) {
            Trace.traceBegin(8388608L, "onCommand " + str);
            try {
                return "list".equals(str) ? runListVibrators() : "synced".equals(str) ? runMono() : "combined".equals(str) ? runStereo() : "sequential".equals(str) ? runSequential() : "cancel".equals(str) ? runCancel() : handleDefaultCommands(str);
            } finally {
                Trace.traceEnd(8388608L);
            }
        }

        private int runListVibrators() {
            PrintWriter outPrintWriter = getOutPrintWriter();
            try {
                if (VibratorManagerService.this.mVibratorIds.length == 0) {
                    outPrintWriter.println("No vibrator found");
                } else {
                    for (int i : VibratorManagerService.this.mVibratorIds) {
                        outPrintWriter.println(i);
                    }
                }
                outPrintWriter.println("");
                outPrintWriter.close();
                return 0;
            } catch (Throwable th) {
                if (outPrintWriter != null) {
                    try {
                        outPrintWriter.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        private void runVibrate(CommonOptions commonOptions, CombinedVibration combinedVibration) {
            HalVibration vibrateInternal = VibratorManagerService.this.vibrateInternal(Binder.getCallingUid(), 0, SHELL_PACKAGE_NAME, combinedVibration, createVibrationAttributes(commonOptions), commonOptions.description, commonOptions.background ? VibratorManagerService.this : this.mShellCallbacksToken);
            if (vibrateInternal == null || commonOptions.background) {
                return;
            }
            try {
                vibrateInternal.waitForEnd();
            } catch (InterruptedException unused) {
            }
        }

        private int runMono() {
            runVibrate(new CommonOptions(), CombinedVibration.createParallel(nextEffect()));
            return 0;
        }

        private int runStereo() {
            CommonOptions commonOptions = new CommonOptions();
            CombinedVibration.ParallelCombination startParallel = CombinedVibration.startParallel();
            while ("-v".equals(getNextOption())) {
                startParallel.addVibrator(Integer.parseInt(getNextArgRequired()), nextEffect());
            }
            runVibrate(commonOptions, startParallel.combine());
            return 0;
        }

        private int runSequential() {
            CommonOptions commonOptions = new CommonOptions();
            CombinedVibration.SequentialCombination startSequential = CombinedVibration.startSequential();
            while ("-v".equals(getNextOption())) {
                startSequential.addNext(Integer.parseInt(getNextArgRequired()), nextEffect());
            }
            runVibrate(commonOptions, startSequential.combine());
            return 0;
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.android.server.vibrator.VibratorManagerService, android.os.IBinder] */
        private int runCancel() {
            ?? r1 = VibratorManagerService.this;
            r1.cancelVibrate(-1, r1);
            return 0;
        }

        private VibrationEffect nextEffect() {
            VibrationEffect.Composition startComposition = VibrationEffect.startComposition();
            while (true) {
                String peekNextArg = peekNextArg();
                if (peekNextArg != null) {
                    if ("oneshot".equals(peekNextArg)) {
                        addOneShotToComposition(startComposition);
                    } else if ("waveform".equals(peekNextArg)) {
                        addWaveformToComposition(startComposition);
                    } else if ("prebaked".equals(peekNextArg)) {
                        addPrebakedToComposition(startComposition);
                    } else {
                        if (!"primitives".equals(peekNextArg)) {
                            break;
                        }
                        addPrimitivesToComposition(startComposition);
                    }
                } else {
                    break;
                }
            }
            return startComposition.compose();
        }

        private void addOneShotToComposition(VibrationEffect.Composition composition) {
            getNextArgRequired();
            boolean z = false;
            int i = 0;
            while (true) {
                String nextOption = getNextOption();
                if (nextOption == null) {
                    break;
                }
                if ("-a".equals(nextOption)) {
                    z = true;
                } else if ("-w".equals(nextOption)) {
                    i = Integer.parseInt(getNextArgRequired());
                }
            }
            long parseLong = Long.parseLong(getNextArgRequired());
            int parseInt = z ? Integer.parseInt(getNextArgRequired()) : -1;
            composition.addOffDuration(Duration.ofMillis(i));
            composition.addEffect(VibrationEffect.createOneShot(parseLong, parseInt));
        }

        private void addWaveformToComposition(VibrationEffect.Composition composition) {
            Duration duration;
            Duration ofMillis;
            getNextArgRequired();
            int i = -1;
            boolean z = false;
            boolean z2 = false;
            int i2 = 0;
            boolean z3 = false;
            while (true) {
                String nextOption = getNextOption();
                if (nextOption == null) {
                    break;
                }
                if ("-a".equals(nextOption)) {
                    z = true;
                } else if ("-r".equals(nextOption)) {
                    i = Integer.parseInt(getNextArgRequired());
                } else if ("-w".equals(nextOption)) {
                    i2 = Integer.parseInt(getNextArgRequired());
                } else if ("-f".equals(nextOption)) {
                    z2 = true;
                } else if ("-c".equals(nextOption)) {
                    z3 = true;
                }
            }
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            float f = 0.0f;
            while (true) {
                String peekNextArg = peekNextArg();
                if (peekNextArg == null) {
                    break;
                }
                try {
                    arrayList.add(Integer.valueOf(Integer.parseInt(peekNextArg)));
                    getNextArgRequired();
                    if (z) {
                        arrayList2.add(Float.valueOf(Float.parseFloat(getNextArgRequired()) / 255.0f));
                    } else {
                        arrayList2.add(Float.valueOf(f));
                        f = 1.0f - f;
                    }
                    if (z2) {
                        arrayList3.add(Float.valueOf(Float.parseFloat(getNextArgRequired())));
                    }
                } catch (NumberFormatException unused) {
                }
            }
            composition.addOffDuration(Duration.ofMillis(i2));
            VibrationEffect.WaveformBuilder startWaveform = VibrationEffect.startWaveform();
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                if (z3) {
                    duration = Duration.ofMillis(((Integer) arrayList.get(i3)).intValue());
                } else {
                    duration = Duration.ZERO;
                }
                if (z3) {
                    ofMillis = Duration.ZERO;
                } else {
                    ofMillis = Duration.ofMillis(((Integer) arrayList.get(i3)).intValue());
                }
                if (z2) {
                    startWaveform.addTransition(duration, VibrationEffect.VibrationParameter.targetAmplitude(((Float) arrayList2.get(i3)).floatValue()), VibrationEffect.VibrationParameter.targetFrequency(((Float) arrayList3.get(i3)).floatValue()));
                } else {
                    startWaveform.addTransition(duration, VibrationEffect.VibrationParameter.targetAmplitude(((Float) arrayList2.get(i3)).floatValue()));
                }
                if (!ofMillis.isZero()) {
                    startWaveform.addSustain(ofMillis);
                }
                if (i3 > 0 && i3 == i) {
                    composition.addEffect(startWaveform.build());
                    if (z2) {
                        startWaveform = VibrationEffect.startWaveform(VibrationEffect.VibrationParameter.targetAmplitude(((Float) arrayList2.get(i3)).floatValue()), VibrationEffect.VibrationParameter.targetFrequency(((Float) arrayList3.get(i3)).floatValue()));
                    } else {
                        startWaveform = VibrationEffect.startWaveform(VibrationEffect.VibrationParameter.targetAmplitude(((Float) arrayList2.get(i3)).floatValue()));
                    }
                }
            }
            if (i < 0) {
                composition.addEffect(startWaveform.build());
            } else {
                composition.repeatEffectIndefinitely(startWaveform.build());
            }
        }

        private void addPrebakedToComposition(VibrationEffect.Composition composition) {
            getNextArgRequired();
            int i = 0;
            boolean z = false;
            while (true) {
                String nextOption = getNextOption();
                if (nextOption != null) {
                    if ("-b".equals(nextOption)) {
                        z = true;
                    } else if ("-w".equals(nextOption)) {
                        i = Integer.parseInt(getNextArgRequired());
                    }
                } else {
                    int parseInt = Integer.parseInt(getNextArgRequired());
                    composition.addOffDuration(Duration.ofMillis(i));
                    composition.addEffect(VibrationEffect.get(parseInt, z));
                    return;
                }
            }
        }

        private void addPrimitivesToComposition(VibrationEffect.Composition composition) {
            int i;
            getNextArgRequired();
            while (true) {
                String peekNextArg = peekNextArg();
                if (peekNextArg == null) {
                    return;
                }
                if ("-w".equals(peekNextArg)) {
                    getNextArgRequired();
                    i = Integer.parseInt(getNextArgRequired());
                    peekNextArg = peekNextArg();
                } else {
                    i = 0;
                }
                try {
                    composition.addPrimitive(Integer.parseInt(peekNextArg), 1.0f, i);
                    getNextArgRequired();
                } catch (NullPointerException | NumberFormatException unused) {
                    return;
                }
            }
        }

        private VibrationAttributes createVibrationAttributes(CommonOptions commonOptions) {
            return new VibrationAttributes.Builder().setFlags(commonOptions.force ? 1 : 0).setUsage(18).build();
        }

        public void onHelp() {
            PrintWriter outPrintWriter = getOutPrintWriter();
            try {
                outPrintWriter.println("Vibrator Manager commands:");
                outPrintWriter.println("  help");
                outPrintWriter.println("    Prints this help text.");
                outPrintWriter.println("");
                outPrintWriter.println("  list");
                outPrintWriter.println("    Prints the id of device vibrators. This does not include any ");
                outPrintWriter.println("    connected input device.");
                outPrintWriter.println("  synced [options] <effect>...");
                outPrintWriter.println("    Vibrates effect on all vibrators in sync.");
                outPrintWriter.println("  combined [options] (-v <vibrator-id> <effect>...)...");
                outPrintWriter.println("    Vibrates different effects on each vibrator in sync.");
                outPrintWriter.println("  sequential [options] (-v <vibrator-id> <effect>...)...");
                outPrintWriter.println("    Vibrates different effects on each vibrator in sequence.");
                outPrintWriter.println("  cancel");
                outPrintWriter.println("    Cancels any active vibration");
                outPrintWriter.println("");
                outPrintWriter.println("Effect commands:");
                outPrintWriter.println("  oneshot [-w delay] [-a] <duration> [<amplitude>]");
                outPrintWriter.println("    Vibrates for duration milliseconds; ignored when device is on ");
                outPrintWriter.println("    DND (Do Not Disturb) mode; touch feedback strength user setting ");
                outPrintWriter.println("    will be used to scale amplitude.");
                outPrintWriter.println("    If -w is provided, the effect will be played after the specified");
                outPrintWriter.println("    wait time in milliseconds.");
                outPrintWriter.println("    If -a is provided, the command accepts a second argument for ");
                outPrintWriter.println("    amplitude, in a scale of 1-255.");
                outPrintWriter.print("  waveform [-w delay] [-r index] [-a] [-f] [-c] ");
                outPrintWriter.println("(<duration> [<amplitude>] [<frequency>])...");
                outPrintWriter.println("    Vibrates for durations and amplitudes in list; ignored when ");
                outPrintWriter.println("    device is on DND (Do Not Disturb) mode; touch feedback strength ");
                outPrintWriter.println("    user setting will be used to scale amplitude.");
                outPrintWriter.println("    If -w is provided, the effect will be played after the specified");
                outPrintWriter.println("    wait time in milliseconds.");
                outPrintWriter.println("    If -r is provided, the waveform loops back to the specified");
                outPrintWriter.println("    index (e.g. 0 loops from the beginning)");
                outPrintWriter.println("    If -a is provided, the command expects amplitude to follow each");
                outPrintWriter.println("    duration; otherwise, it accepts durations only and alternates");
                outPrintWriter.println("    off/on");
                outPrintWriter.println("    If -f is provided, the command expects frequency to follow each");
                outPrintWriter.println("    amplitude or duration; otherwise, it uses resonant frequency");
                outPrintWriter.println("    If -c is provided, the waveform is continuous and will ramp");
                outPrintWriter.println("    between values; otherwise each entry is a fixed step.");
                outPrintWriter.println("    Duration is in milliseconds; amplitude is a scale of 1-255;");
                outPrintWriter.println("    frequency is an absolute value in hertz;");
                outPrintWriter.println("  prebaked [-w delay] [-b] <effect-id>");
                outPrintWriter.println("    Vibrates with prebaked effect; ignored when device is on DND ");
                outPrintWriter.println("    (Do Not Disturb) mode; touch feedback strength user setting ");
                outPrintWriter.println("    will be used to scale amplitude.");
                outPrintWriter.println("    If -w is provided, the effect will be played after the specified");
                outPrintWriter.println("    wait time in milliseconds.");
                outPrintWriter.println("    If -b is provided, the prebaked fallback effect will be played if");
                outPrintWriter.println("    the device doesn't support the given effect-id.");
                outPrintWriter.println("  primitives ([-w delay] <primitive-id>)...");
                outPrintWriter.println("    Vibrates with a composed effect; ignored when device is on DND ");
                outPrintWriter.println("    (Do Not Disturb) mode; touch feedback strength user setting ");
                outPrintWriter.println("    will be used to scale primitive intensities.");
                outPrintWriter.println("    If -w is provided, the next primitive will be played after the ");
                outPrintWriter.println("    specified wait time in milliseconds.");
                outPrintWriter.println("");
                outPrintWriter.println("Common Options:");
                outPrintWriter.println("  -f");
                outPrintWriter.println("    Force. Ignore Do Not Disturb setting.");
                outPrintWriter.println("  -B");
                outPrintWriter.println("    Run in the background; without this option the shell cmd will");
                outPrintWriter.println("    block until the vibration has completed.");
                outPrintWriter.println("  -d <description>");
                outPrintWriter.println("    Add description to the vibration.");
                outPrintWriter.println("");
                outPrintWriter.close();
            } catch (Throwable th) {
                if (outPrintWriter != null) {
                    try {
                        outPrintWriter.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    public IVibratorManagerServiceWrapper getWrapper() {
        return this.mVibratorManagerServiceWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class VibratorManagerServiceWrapper implements IVibratorManagerServiceWrapper {
        private IVibratorManagerServiceExt mVibratorManagerServiceExt;

        private VibratorManagerServiceWrapper() {
            this.mVibratorManagerServiceExt = (IVibratorManagerServiceExt) ExtLoader.type(IVibratorManagerServiceExt.class).base(VibratorManagerService.this).create();
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public IVibratorManagerServiceExt getExtImpl() {
            return this.mVibratorManagerServiceExt;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public void setDebuggable(boolean z) {
            VibratorManagerService.DEBUG = z;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public boolean isDebuggable() {
            return VibratorManagerService.DEBUG;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public VibrationStepConductor getCurrentVibrationStepConductor() {
            return VibratorManagerService.this.mCurrentVibration;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public SparseArray<VibratorController> getVibrators() {
            return VibratorManagerService.this.mVibrators;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public InputDeviceDelegate getInputDeviceDelegate() {
            return VibratorManagerService.this.mInputDeviceDelegate;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public Object getSyncLock() {
            return VibratorManagerService.this.mLock;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public PowerManager.WakeLock getVibratorPartialWakeLock() {
            return VibratorManagerService.this.mWakeLock;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public Handler getHandler() {
            return VibratorManagerService.this.mHandler;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public void noteVibratorOnExtImpl(int i, long j) {
            VibratorManagerService.this.mVibrationThreadCallbacks.noteVibratorOn(i, j);
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public void noteVibratorOffExtImpl(int i) {
            VibratorManagerService.this.mVibrationThreadCallbacks.noteVibratorOff(i);
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public VibrationSettings getVibrationSettings() {
            return VibratorManagerService.this.mVibrationSettings;
        }
    }

    public void updateVibrationAmplitude(int i, String str, float f) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.VIBRATE", "updateVibrationAmplitude");
        getWrapper().getExtImpl().updateVibrationAmplitude(i, str, f);
    }

    public boolean blockVibrationForApplication(String str, boolean z, IBinder iBinder) {
        boolean blockVibrationForApplicationLocked;
        this.mContext.enforceCallingOrSelfPermission("android.permission.VIBRATE", "blockVibrationForApplication");
        this.mContext.enforceCallingOrSelfPermission("com.oplus.permission.safe.CAMERA", "blockVibrationForApplication");
        synchronized (this.mLock) {
            blockVibrationForApplicationLocked = getWrapper().getExtImpl().blockVibrationForApplicationLocked(str, z, iBinder);
        }
        return blockVibrationForApplicationLocked;
    }

    public int getWaveformIndex(int i) {
        return getWrapper().getExtImpl().getWaveformIndex(i);
    }

    public int getEffectDuration(int i) {
        return getWrapper().getExtImpl().getEffectDuration(i);
    }

    public int getEffectType(int i) {
        return getWrapper().getExtImpl().getEffectType(i);
    }

    public int getRingtoneEffectId(String str) {
        return getWrapper().getExtImpl().getRingtoneEffectId(str);
    }
}
