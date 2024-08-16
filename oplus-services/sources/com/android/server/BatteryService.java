package com.android.server;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.BroadcastOptions;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.hardware.health.HealthInfo;
import android.metrics.LogMaker;
import android.os.BatteryManagerInternal;
import android.os.BatteryProperty;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBatteryPropertiesRegistrar;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ShellCallback;
import android.os.ShellCommand;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UEventObserver;
import android.os.UserHandle;
import android.provider.Settings;
import android.sysprop.PowerProperties;
import android.util.EventLog;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.app.IBatteryStats;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.server.am.BatteryStatsService;
import com.android.server.health.HealthInfoCallback;
import com.android.server.health.HealthServiceWrapper;
import com.android.server.health.Utils;
import com.android.server.lights.LightsManager;
import com.android.server.lights.LogicalLight;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class BatteryService extends SystemService {
    private static final long BATTERY_LEVEL_CHANGE_THROTTLE_MS = 60000;
    private static final int BATTERY_PLUGGED_NONE = 0;
    private static final int BATTERY_SCALE = 100;
    static boolean DEBUG = false;
    private static final String[] DUMPSYS_ARGS = {"--checkin", "--unplugged"};
    private static final String DUMPSYS_DATA_PATH = "/data/system/";
    private static final long HEALTH_HAL_WAIT_MS = 1000;
    private static final int MAX_BATTERY_LEVELS_QUEUE_SIZE = 100;
    static final int OPTION_FORCE_UPDATE = 1;
    private static final String TAG = "BatteryService";
    private ActivityManagerInternal mActivityManagerInternal;
    private Bundle mBatteryChangedOptions;
    private boolean mBatteryInputSuspended;
    private boolean mBatteryLevelCritical;
    private boolean mBatteryLevelLow;
    private ArrayDeque<Bundle> mBatteryLevelsEventQueue;
    private int mBatteryNearlyFullLevel;
    private Bundle mBatteryOptions;
    private BatteryPropertiesRegistrar mBatteryPropertiesRegistrar;
    public IBatteryServiceExt mBatteryServiceExt;
    private final IBatteryStats mBatteryStats;
    BinderService mBinderService;
    private int mChargeStartLevel;
    private long mChargeStartTime;
    private final Context mContext;
    private int mCriticalBatteryLevel;
    private int mDischargeStartLevel;
    private long mDischargeStartTime;
    private final Handler mHandler;
    private HealthInfo mHealthInfo;
    private HealthServiceWrapper mHealthServiceWrapper;
    private int mInvalidCharger;
    private int mLastBatteryCycleCount;
    private int mLastBatteryHealth;
    private int mLastBatteryLevel;
    private long mLastBatteryLevelChangedSentMs;
    private boolean mLastBatteryLevelCritical;
    private boolean mLastBatteryPresent;
    private int mLastBatteryStatus;
    private int mLastBatteryTemperature;
    private int mLastBatteryVoltage;
    private int mLastChargeCounter;
    private int mLastCharingState;
    private final HealthInfo mLastHealthInfo;
    private int mLastInvalidCharger;
    private int mLastLowBatteryWarningLevel;
    private int mLastMaxChargingCurrent;
    private int mLastMaxChargingVoltage;
    private int mLastPlugType;
    private Led mLed;
    private final Object mLock;
    private int mLowBatteryCloseWarningLevel;
    private int mLowBatteryWarningLevel;
    private MetricsLogger mMetricsLogger;
    private int mPlugType;
    private Bundle mPowerOptions;
    private boolean mSentLowBatteryBroadcast;
    private int mSequence;
    private int mShutdownBatteryTemperature;
    private boolean mUpdatesStopped;

    public BatteryService(Context context) {
        super(context);
        this.mLock = new Object();
        this.mLastHealthInfo = new HealthInfo();
        this.mSequence = 1;
        this.mLastPlugType = -1;
        this.mSentLowBatteryBroadcast = false;
        this.mBatteryChangedOptions = BroadcastOptions.makeBasic().setDeliveryGroupPolicy(1).setDeferralPolicy(2).toBundle();
        this.mPowerOptions = BroadcastOptions.makeBasic().setDeliveryGroupPolicy(1).setDeliveryGroupMatchingKey("android", "android.intent.action.ACTION_POWER_CONNECTED").setDeferralPolicy(2).toBundle();
        this.mBatteryOptions = BroadcastOptions.makeBasic().setDeliveryGroupPolicy(1).setDeliveryGroupMatchingKey("android", "android.intent.action.BATTERY_OKAY").setDeferralPolicy(2).toBundle();
        this.mBatteryServiceExt = (IBatteryServiceExt) ExtLoader.type(IBatteryServiceExt.class).base(this).create();
        this.mContext = context;
        this.mHandler = new Handler(true);
        this.mLed = new Led(context, (LightsManager) getLocalService(LightsManager.class));
        this.mBatteryStats = BatteryStatsService.getService();
        this.mActivityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        this.mCriticalBatteryLevel = context.getResources().getInteger(R.integer.config_defaultPeakRefreshRate);
        int integer = context.getResources().getInteger(R.integer.config_previousVibrationsDumpLimit);
        this.mLowBatteryWarningLevel = integer;
        this.mLowBatteryCloseWarningLevel = integer + context.getResources().getInteger(R.integer.config_phonenumber_compare_min_match);
        this.mShutdownBatteryTemperature = context.getResources().getInteger(R.integer.dock_enter_exit_duration);
        this.mBatteryLevelsEventQueue = new ArrayDeque<>();
        this.mMetricsLogger = new MetricsLogger();
        this.mBatteryServiceExt.initBatteryServiceExtImpl(context, this, this.mLed);
        if (new File("/sys/devices/virtual/switch/invalid_charger/state").exists()) {
            new UEventObserver() { // from class: com.android.server.BatteryService.1
                public void onUEvent(UEventObserver.UEvent uEvent) {
                    boolean equals = "1".equals(uEvent.get("SWITCH_STATE"));
                    synchronized (BatteryService.this.mLock) {
                        if (BatteryService.this.mInvalidCharger != equals) {
                            BatteryService.this.mInvalidCharger = equals ? 1 : 0;
                        }
                    }
                }
            }.startObserving("DEVPATH=/devices/virtual/switch/invalid_charger");
        }
        this.mBatteryInputSuspended = ((Boolean) PowerProperties.battery_input_suspended().orElse(Boolean.FALSE)).booleanValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.server.BatteryService$BatteryPropertiesRegistrar, android.os.IBinder] */
    @Override // com.android.server.SystemService
    public void onStart() {
        this.mBatteryServiceExt.onStart();
        registerHealthCallback();
        BinderService binderService = new BinderService();
        this.mBinderService = binderService;
        publishBinderService("battery", binderService);
        ?? batteryPropertiesRegistrar = new BatteryPropertiesRegistrar();
        this.mBatteryPropertiesRegistrar = batteryPropertiesRegistrar;
        publishBinderService("batteryproperties", batteryPropertiesRegistrar);
        publishLocalService(BatteryManagerInternal.class, new LocalService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        synchronized (this.mLock) {
            this.mBatteryServiceExt.onBootPhase(i);
        }
        if (i == 550) {
            synchronized (this.mLock) {
                this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("low_power_trigger_level"), false, new ContentObserver(this.mHandler) { // from class: com.android.server.BatteryService.2
                    @Override // android.database.ContentObserver
                    public void onChange(boolean z) {
                        synchronized (BatteryService.this.mLock) {
                            BatteryService.this.updateBatteryWarningLevelLocked();
                        }
                    }
                }, -1);
                updateBatteryWarningLevelLocked();
            }
        }
    }

    private void registerHealthCallback() {
        traceBegin("HealthInitWrapper");
        try {
            try {
                this.mHealthServiceWrapper = HealthServiceWrapper.create(new HealthInfoCallback() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda4
                    public final void update(HealthInfo healthInfo) {
                        BatteryService.this.update(healthInfo);
                    }
                });
                traceEnd();
                traceBegin("HealthInitWaitUpdate");
                long uptimeMillis = SystemClock.uptimeMillis();
                synchronized (this.mLock) {
                    while (this.mHealthInfo == null) {
                        Slog.i(TAG, "health: Waited " + (SystemClock.uptimeMillis() - uptimeMillis) + "ms for callbacks. Waiting another " + HEALTH_HAL_WAIT_MS + " ms...");
                        try {
                            this.mLock.wait(HEALTH_HAL_WAIT_MS);
                        } catch (InterruptedException unused) {
                            Slog.i(TAG, "health: InterruptedException when waiting for update.  Continuing...");
                        }
                    }
                }
                Slog.i(TAG, "health: Waited " + (SystemClock.uptimeMillis() - uptimeMillis) + "ms and received the update.");
            } catch (RemoteException e) {
                Slog.e(TAG, "health: cannot register callback. (RemoteException)");
                throw e.rethrowFromSystemServer();
            } catch (NoSuchElementException e2) {
                Slog.e(TAG, "health: cannot register callback. (no supported health HAL service)");
                throw e2;
            }
        } finally {
            traceEnd();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBatteryWarningLevelLocked() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        int integer = this.mContext.getResources().getInteger(R.integer.config_previousVibrationsDumpLimit);
        this.mLastLowBatteryWarningLevel = this.mLowBatteryWarningLevel;
        int i = Settings.Global.getInt(contentResolver, "low_power_trigger_level", integer);
        this.mLowBatteryWarningLevel = i;
        if (i == 0) {
            this.mLowBatteryWarningLevel = integer;
        }
        int i2 = this.mLowBatteryWarningLevel;
        int i3 = this.mCriticalBatteryLevel;
        if (i2 < i3) {
            this.mLowBatteryWarningLevel = i3;
        }
        this.mLowBatteryCloseWarningLevel = this.mLowBatteryWarningLevel + this.mContext.getResources().getInteger(R.integer.config_phonenumber_compare_min_match);
        lambda$setChargerAcOnline$1(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPoweredLocked(int i) {
        HealthInfo healthInfo = this.mHealthInfo;
        if (healthInfo.batteryStatus == 1) {
            return true;
        }
        if ((i & 1) != 0 && healthInfo.chargerAcOnline) {
            return true;
        }
        if ((i & 2) != 0 && healthInfo.chargerUsbOnline) {
            return true;
        }
        if ((i & 4) == 0 || !healthInfo.chargerWirelessOnline) {
            return (i & 8) != 0 && healthInfo.chargerDockOnline;
        }
        return true;
    }

    private boolean shouldSendBatteryLowLocked() {
        int i;
        int i2;
        boolean z = this.mPlugType != 0;
        boolean z2 = this.mLastPlugType != 0;
        if (z) {
            return false;
        }
        HealthInfo healthInfo = this.mHealthInfo;
        if (healthInfo.batteryStatus == 1 || (i = healthInfo.batteryLevel) > (i2 = this.mLowBatteryWarningLevel)) {
            return false;
        }
        return z2 || this.mLastBatteryLevel > i2 || i > this.mLastLowBatteryWarningLevel;
    }

    private boolean shouldShutdownLocked() {
        HealthInfo healthInfo = this.mHealthInfo;
        int i = healthInfo.batteryCapacityLevel;
        return i != -1 ? i == 1 : healthInfo.batteryLevel <= 0 && healthInfo.batteryPresent && healthInfo.batteryStatus != 2;
    }

    private void shutdownIfNoPowerLocked() {
        if (shouldShutdownLocked()) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.BatteryService.3
                @Override // java.lang.Runnable
                public void run() {
                    if (BatteryService.this.mActivityManagerInternal.isSystemReady()) {
                        Slog.v(BatteryService.TAG, "mHealthInfo.batteryLevel = " + BatteryService.this.mHealthInfo.batteryLevel + "shutdown because of low power");
                        BatteryService.this.mBatteryServiceExt.writeEventLowBatteryPowerOff();
                        Intent intent = new Intent("com.android.internal.intent.action.REQUEST_SHUTDOWN");
                        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
                        intent.putExtra("android.intent.extra.REASON", "battery");
                        intent.setFlags(AudioFormat.EVRC);
                        BatteryService.this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
                        BatteryService.this.mLed.mLedExt.turnOffBatteryLights();
                    }
                }
            });
        }
    }

    private void shutdownIfOverTempLocked() {
        if (!this.mBatteryServiceExt.ignoreShutdownIfOverTempByOplusLocked() && this.mHealthInfo.batteryTemperatureTenthsCelsius > this.mShutdownBatteryTemperature) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.BatteryService.4
                @Override // java.lang.Runnable
                public void run() {
                    if (BatteryService.this.mActivityManagerInternal.isSystemReady()) {
                        Intent intent = new Intent("com.android.internal.intent.action.REQUEST_SHUTDOWN");
                        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
                        intent.putExtra("android.intent.extra.REASON", "thermal,battery");
                        intent.setFlags(AudioFormat.EVRC);
                        BatteryService.this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update(HealthInfo healthInfo) {
        traceBegin("HealthInfoUpdate");
        Trace.traceCounter(131072L, "BatteryChargeCounter", healthInfo.batteryChargeCounterUah);
        Trace.traceCounter(131072L, "BatteryCurrent", healthInfo.batteryCurrentMicroamps);
        Trace.traceCounter(131072L, "PlugType", plugType(healthInfo));
        Trace.traceCounter(131072L, "BatteryStatus", healthInfo.batteryStatus);
        if (!this.mUpdatesStopped) {
            this.mBatteryServiceExt.updateBatteryService();
        }
        synchronized (this.mLock) {
            if (this.mBatteryServiceExt.getDebugCommand()) {
                Slog.v(TAG, "update mUpdatesStopped = " + this.mUpdatesStopped);
            }
            if (!this.mUpdatesStopped) {
                this.mHealthInfo = healthInfo;
                lambda$setChargerAcOnline$1(false);
                this.mLock.notifyAll();
            } else {
                Utils.copyV1Battery(this.mLastHealthInfo, healthInfo);
            }
        }
        this.mBatteryServiceExt.notifyTempChanged();
        traceEnd();
    }

    private static int plugType(HealthInfo healthInfo) {
        if (healthInfo.chargerAcOnline) {
            return 1;
        }
        if (healthInfo.chargerUsbOnline) {
            return 2;
        }
        if (healthInfo.chargerWirelessOnline) {
            return 4;
        }
        return healthInfo.chargerDockOnline ? 8 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: processValuesLocked, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$setChargerAcOnline$1(boolean z) {
        boolean z2;
        long j;
        boolean z3;
        HealthInfo healthInfo = this.mHealthInfo;
        this.mBatteryLevelCritical = healthInfo.batteryStatus != 1 && healthInfo.batteryLevel <= this.mCriticalBatteryLevel;
        int plugType = plugType(healthInfo);
        this.mPlugType = plugType;
        this.mBatteryServiceExt.processValuesForOplusLocked(z, plugType, this.mHealthInfo);
        if (DEBUG) {
            Slog.d(TAG, "Processing new values: info=" + this.mHealthInfo + ", mBatteryLevelCritical=" + this.mBatteryLevelCritical + ", mPlugType=" + this.mPlugType);
        }
        try {
            IBatteryStats iBatteryStats = this.mBatteryStats;
            HealthInfo healthInfo2 = this.mHealthInfo;
            iBatteryStats.setBatteryState(healthInfo2.batteryStatus, healthInfo2.batteryHealth, this.mPlugType, healthInfo2.batteryLevel, healthInfo2.batteryTemperatureTenthsCelsius, healthInfo2.batteryVoltageMillivolts, healthInfo2.batteryChargeCounterUah, healthInfo2.batteryFullChargeUah, healthInfo2.batteryChargeTimeToFullNowSeconds);
        } catch (RemoteException unused) {
        }
        shutdownIfNoPowerLocked();
        shutdownIfOverTempLocked();
        boolean shouldUpdateChargingState = z ? false : this.mBatteryServiceExt.shouldUpdateChargingState(this.mHealthInfo.batteryTemperatureTenthsCelsius, this.mLastBatteryTemperature);
        if (!z) {
            HealthInfo healthInfo3 = this.mHealthInfo;
            if (healthInfo3.batteryStatus == this.mLastBatteryStatus && healthInfo3.batteryHealth == this.mLastBatteryHealth && healthInfo3.batteryPresent == this.mLastBatteryPresent && healthInfo3.batteryLevel == this.mLastBatteryLevel && this.mPlugType == this.mLastPlugType && healthInfo3.batteryVoltageMillivolts == this.mLastBatteryVoltage && !shouldUpdateChargingState && healthInfo3.maxChargingCurrentMicroamps == this.mLastMaxChargingCurrent && healthInfo3.maxChargingVoltageMicrovolts == this.mLastMaxChargingVoltage && healthInfo3.batteryChargeCounterUah == this.mLastChargeCounter && this.mInvalidCharger == this.mLastInvalidCharger && healthInfo3.batteryCycleCount == this.mLastBatteryCycleCount && healthInfo3.chargingState == this.mLastCharingState) {
                return;
            }
        }
        int i = this.mPlugType;
        int i2 = this.mLastPlugType;
        if (i != i2) {
            if (i2 == 0) {
                this.mChargeStartLevel = this.mHealthInfo.batteryLevel;
                this.mChargeStartTime = SystemClock.elapsedRealtime();
                LogMaker logMaker = new LogMaker(1417);
                logMaker.setType(4);
                logMaker.addTaggedData(1421, Integer.valueOf(this.mPlugType));
                logMaker.addTaggedData(1418, Integer.valueOf(this.mHealthInfo.batteryLevel));
                this.mMetricsLogger.write(logMaker);
                if (this.mDischargeStartTime == 0 || this.mDischargeStartLevel == this.mHealthInfo.batteryLevel) {
                    z3 = false;
                    j = 0;
                } else {
                    j = SystemClock.elapsedRealtime() - this.mDischargeStartTime;
                    EventLog.writeEvent(EventLogTags.BATTERY_DISCHARGE, Long.valueOf(j), Integer.valueOf(this.mDischargeStartLevel), Integer.valueOf(this.mHealthInfo.batteryLevel));
                    this.mDischargeStartTime = 0L;
                    z3 = true;
                }
                z2 = z3;
            } else {
                if (i == 0) {
                    this.mDischargeStartTime = SystemClock.elapsedRealtime();
                    this.mDischargeStartLevel = this.mHealthInfo.batteryLevel;
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    long j2 = this.mChargeStartTime;
                    long j3 = elapsedRealtime - j2;
                    if (j2 != 0 && j3 != 0) {
                        LogMaker logMaker2 = new LogMaker(1417);
                        logMaker2.setType(5);
                        logMaker2.addTaggedData(1421, Integer.valueOf(this.mLastPlugType));
                        logMaker2.addTaggedData(1420, Long.valueOf(j3));
                        logMaker2.addTaggedData(1418, Integer.valueOf(this.mChargeStartLevel));
                        logMaker2.addTaggedData(1419, Integer.valueOf(this.mHealthInfo.batteryLevel));
                        this.mMetricsLogger.write(logMaker2);
                    }
                    this.mChargeStartTime = 0L;
                }
                z2 = false;
                j = 0;
            }
            this.mBatteryServiceExt.onPlugChangedForOplusSysStateManager(this.mPlugType);
        } else {
            z2 = false;
            j = 0;
        }
        HealthInfo healthInfo4 = this.mHealthInfo;
        int i3 = healthInfo4.batteryStatus;
        if (i3 != this.mLastBatteryStatus || healthInfo4.batteryHealth != this.mLastBatteryHealth || healthInfo4.batteryPresent != this.mLastBatteryPresent || this.mPlugType != this.mLastPlugType) {
            EventLog.writeEvent(EventLogTags.BATTERY_STATUS, Integer.valueOf(i3), Integer.valueOf(this.mHealthInfo.batteryHealth), Integer.valueOf(this.mHealthInfo.batteryPresent ? 1 : 0), Integer.valueOf(this.mPlugType), this.mHealthInfo.batteryTechnology);
            SystemProperties.set("debug.tracing.battery_status", Integer.toString(this.mHealthInfo.batteryStatus));
            SystemProperties.set("debug.tracing.plug_type", Integer.toString(this.mPlugType));
        }
        int i4 = this.mHealthInfo.batteryLevel;
        if (i4 != this.mLastBatteryLevel) {
            EventLog.writeEvent(EventLogTags.BATTERY_LEVEL, Integer.valueOf(i4), Integer.valueOf(this.mHealthInfo.batteryVoltageMillivolts), Integer.valueOf(this.mHealthInfo.batteryTemperatureTenthsCelsius));
        }
        if (this.mBatteryLevelCritical && !this.mLastBatteryLevelCritical && this.mPlugType == 0) {
            j = SystemClock.elapsedRealtime() - this.mDischargeStartTime;
            z2 = true;
        }
        if (!this.mBatteryLevelLow) {
            if (this.mPlugType == 0) {
                HealthInfo healthInfo5 = this.mHealthInfo;
                if (healthInfo5.batteryStatus != 1 && healthInfo5.batteryLevel <= this.mLowBatteryWarningLevel) {
                    this.mBatteryLevelLow = true;
                }
            }
        } else if (this.mPlugType != 0) {
            this.mBatteryLevelLow = false;
        } else {
            int i5 = this.mHealthInfo.batteryLevel;
            if (i5 >= this.mLowBatteryCloseWarningLevel) {
                this.mBatteryLevelLow = false;
            } else if (z && i5 >= this.mLowBatteryWarningLevel) {
                this.mBatteryLevelLow = false;
            }
        }
        this.mSequence++;
        int i6 = this.mPlugType;
        if (i6 != 0 && this.mLastPlugType == 0) {
            final Intent intent = new Intent("android.intent.action.ACTION_POWER_CONNECTED");
            intent.setFlags(67108864);
            intent.putExtra("seq", this.mSequence);
            this.mBatteryServiceExt.appendFlagToStatusIntent(intent, R.raw.loaderror);
            this.mHandler.post(new Runnable() { // from class: com.android.server.BatteryService.5
                @Override // java.lang.Runnable
                public void run() {
                    BatteryService.this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, null, BatteryService.this.mPowerOptions);
                }
            });
        } else if (i6 == 0 && this.mLastPlugType != 0) {
            final Intent intent2 = new Intent("android.intent.action.ACTION_POWER_DISCONNECTED");
            intent2.setFlags(67108864);
            intent2.putExtra("seq", this.mSequence);
            this.mBatteryServiceExt.appendFlagToStatusIntent(intent2, AudioDevice.OUT_FM);
            this.mHandler.post(new Runnable() { // from class: com.android.server.BatteryService.6
                @Override // java.lang.Runnable
                public void run() {
                    BatteryService.this.mContext.sendBroadcastAsUser(intent2, UserHandle.ALL, null, BatteryService.this.mPowerOptions);
                }
            });
        }
        if (shouldSendBatteryLowLocked()) {
            this.mSentLowBatteryBroadcast = true;
            final Intent intent3 = new Intent("android.intent.action.BATTERY_LOW");
            intent3.setFlags(67108864);
            intent3.putExtra("seq", this.mSequence);
            this.mHandler.post(new Runnable() { // from class: com.android.server.BatteryService.7
                @Override // java.lang.Runnable
                public void run() {
                    BatteryService.this.mContext.sendBroadcastAsUser(intent3, UserHandle.ALL, null, BatteryService.this.mBatteryOptions);
                }
            });
        } else if (this.mSentLowBatteryBroadcast && this.mHealthInfo.batteryLevel >= this.mLowBatteryCloseWarningLevel) {
            this.mSentLowBatteryBroadcast = false;
            final Intent intent4 = new Intent("android.intent.action.BATTERY_OKAY");
            intent4.setFlags(67108864);
            intent4.putExtra("seq", this.mSequence);
            this.mHandler.post(new Runnable() { // from class: com.android.server.BatteryService.8
                @Override // java.lang.Runnable
                public void run() {
                    BatteryService.this.mContext.sendBroadcastAsUser(intent4, UserHandle.ALL, null, BatteryService.this.mBatteryOptions);
                }
            });
        }
        if (z || !this.mBatteryServiceExt.isNeedSkipBatteryChangedBroadcast(this.mHealthInfo, this.mPlugType, this.mInvalidCharger, shouldUpdateChargingState)) {
            this.mBatteryServiceExt.setBatterySaverTest(isPoweredLocked(15), this.mHealthInfo.batteryLevel, this.mBatteryLevelLow);
            sendBatteryChangedIntentLocked();
            if (this.mLastBatteryLevel != this.mHealthInfo.batteryLevel || this.mLastPlugType != this.mPlugType) {
                sendBatteryLevelChangedIntentLocked();
            }
            this.mLed.updateLightsLocked();
            if (z2 && j != 0) {
                logOutlierLocked(j);
            }
            HealthInfo healthInfo6 = this.mHealthInfo;
            this.mLastBatteryStatus = healthInfo6.batteryStatus;
            this.mLastBatteryHealth = healthInfo6.batteryHealth;
            this.mLastBatteryPresent = healthInfo6.batteryPresent;
            this.mLastBatteryLevel = healthInfo6.batteryLevel;
            this.mLastPlugType = this.mPlugType;
            this.mLastBatteryVoltage = healthInfo6.batteryVoltageMillivolts;
            this.mLastBatteryTemperature = healthInfo6.batteryTemperatureTenthsCelsius;
            this.mLastMaxChargingCurrent = healthInfo6.maxChargingCurrentMicroamps;
            this.mLastMaxChargingVoltage = healthInfo6.maxChargingVoltageMicrovolts;
            this.mLastChargeCounter = healthInfo6.batteryChargeCounterUah;
            this.mLastBatteryLevelCritical = this.mBatteryLevelCritical;
            this.mLastInvalidCharger = this.mInvalidCharger;
        }
        this.mBatteryServiceExt.saveLastStatsAfterValuesChanged();
        HealthInfo healthInfo7 = this.mHealthInfo;
        this.mLastBatteryStatus = healthInfo7.batteryStatus;
        this.mLastBatteryHealth = healthInfo7.batteryHealth;
        this.mLastBatteryPresent = healthInfo7.batteryPresent;
        this.mLastBatteryLevel = healthInfo7.batteryLevel;
        this.mLastPlugType = this.mPlugType;
        this.mLastBatteryVoltage = healthInfo7.batteryVoltageMillivolts;
        this.mLastBatteryTemperature = healthInfo7.batteryTemperatureTenthsCelsius;
        this.mLastMaxChargingCurrent = healthInfo7.maxChargingCurrentMicroamps;
        this.mLastMaxChargingVoltage = healthInfo7.maxChargingVoltageMicrovolts;
        this.mLastChargeCounter = healthInfo7.batteryChargeCounterUah;
        this.mLastBatteryLevelCritical = this.mBatteryLevelCritical;
        this.mLastInvalidCharger = this.mInvalidCharger;
        this.mLastBatteryCycleCount = healthInfo7.batteryCycleCount;
        this.mLastCharingState = healthInfo7.chargingState;
    }

    private void sendBatteryChangedIntentLocked() {
        final Intent intent = new Intent("android.intent.action.BATTERY_CHANGED");
        intent.addFlags(1610612736);
        int iconLocked = getIconLocked(this.mHealthInfo.batteryLevel);
        intent.putExtra("seq", this.mSequence);
        intent.putExtra("status", this.mHealthInfo.batteryStatus);
        intent.putExtra("health", this.mHealthInfo.batteryHealth);
        intent.putExtra("present", this.mHealthInfo.batteryPresent);
        intent.putExtra("level", this.mHealthInfo.batteryLevel);
        intent.putExtra("battery_low", this.mSentLowBatteryBroadcast);
        intent.putExtra("scale", 100);
        intent.putExtra("icon-small", iconLocked);
        intent.putExtra("plugged", this.mPlugType);
        intent.putExtra("voltage", this.mHealthInfo.batteryVoltageMillivolts);
        intent.putExtra("temperature", this.mHealthInfo.batteryTemperatureTenthsCelsius);
        intent.putExtra("technology", this.mHealthInfo.batteryTechnology);
        intent.putExtra("invalid_charger", this.mInvalidCharger);
        intent.putExtra("max_charging_current", this.mHealthInfo.maxChargingCurrentMicroamps);
        intent.putExtra("max_charging_voltage", this.mHealthInfo.maxChargingVoltageMicrovolts);
        intent.putExtra("charge_counter", this.mHealthInfo.batteryChargeCounterUah);
        intent.putExtra("android.os.extra.CYCLE_COUNT", this.mHealthInfo.batteryCycleCount);
        intent.putExtra("android.os.extra.CHARGING_STATUS", this.mHealthInfo.chargingState);
        this.mBatteryServiceExt.appendExtraToBatteryStatusChangedIntend(intent);
        if (DEBUG) {
            Slog.d(TAG, "Sending ACTION_BATTERY_CHANGED. scale:100, info:" + this.mHealthInfo.toString() + this.mBatteryServiceExt.getBatteryStatusStrForDebug());
        }
        this.mHandler.post(new Runnable() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BatteryService.this.lambda$sendBatteryChangedIntentLocked$0(intent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendBatteryChangedIntentLocked$0(Intent intent) {
        ActivityManager.broadcastStickyIntent(intent, -1, this.mBatteryChangedOptions, -1);
    }

    private void sendBatteryLevelChangedIntentLocked() {
        Bundle bundle = new Bundle();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        bundle.putInt("seq", this.mSequence);
        bundle.putInt("status", this.mHealthInfo.batteryStatus);
        bundle.putInt("health", this.mHealthInfo.batteryHealth);
        bundle.putBoolean("present", this.mHealthInfo.batteryPresent);
        bundle.putInt("level", this.mHealthInfo.batteryLevel);
        bundle.putBoolean("battery_low", this.mSentLowBatteryBroadcast);
        bundle.putInt("scale", 100);
        bundle.putInt("plugged", this.mPlugType);
        bundle.putInt("voltage", this.mHealthInfo.batteryVoltageMillivolts);
        bundle.putInt("temperature", this.mHealthInfo.batteryTemperatureTenthsCelsius);
        bundle.putInt("charge_counter", this.mHealthInfo.batteryChargeCounterUah);
        bundle.putLong("android.os.extra.EVENT_TIMESTAMP", elapsedRealtime);
        bundle.putInt("android.os.extra.CYCLE_COUNT", this.mHealthInfo.batteryCycleCount);
        bundle.putInt("android.os.extra.CHARGING_STATUS", this.mHealthInfo.chargingState);
        boolean isEmpty = this.mBatteryLevelsEventQueue.isEmpty();
        this.mBatteryLevelsEventQueue.add(bundle);
        if (this.mBatteryLevelsEventQueue.size() > 100) {
            this.mBatteryLevelsEventQueue.removeFirst();
        }
        if (isEmpty) {
            long j = this.mLastBatteryLevelChangedSentMs;
            this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    BatteryService.this.sendEnqueuedBatteryLevelChangedEvents();
                }
            }, elapsedRealtime - j > 60000 ? 0L : (j + 60000) - elapsedRealtime);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendEnqueuedBatteryLevelChangedEvents() {
        ArrayList<? extends Parcelable> arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList<>(this.mBatteryLevelsEventQueue);
            this.mBatteryLevelsEventQueue.clear();
        }
        Intent intent = new Intent("android.intent.action.BATTERY_LEVEL_CHANGED");
        intent.addFlags(16777216);
        intent.putParcelableArrayListExtra("android.os.extra.EVENTS", arrayList);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.BATTERY_STATS");
        this.mLastBatteryLevelChangedSentMs = SystemClock.elapsedRealtime();
    }

    private void logBatteryStatsLocked() {
        DropBoxManager dropBoxManager;
        File file;
        String str;
        StringBuilder sb;
        IBinder service = ServiceManager.getService("batterystats");
        if (service == null || (dropBoxManager = (DropBoxManager) this.mContext.getSystemService("dropbox")) == null || !dropBoxManager.isTagEnabled("BATTERY_DISCHARGE_INFO")) {
            return;
        }
        FileOutputStream fileOutputStream = null;
        try {
            try {
                file = new File("/data/system/batterystats.dump");
                try {
                    FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                    try {
                        service.dump(fileOutputStream2.getFD(), DUMPSYS_ARGS);
                        FileUtils.sync(fileOutputStream2);
                        dropBoxManager.addFile("BATTERY_DISCHARGE_INFO", file, 2);
                        try {
                            fileOutputStream2.close();
                        } catch (IOException unused) {
                            Slog.e(TAG, "failed to close dumpsys output stream");
                        }
                    } catch (RemoteException e) {
                        e = e;
                        fileOutputStream = fileOutputStream2;
                        Slog.e(TAG, "failed to dump battery service", e);
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException unused2) {
                                Slog.e(TAG, "failed to close dumpsys output stream");
                            }
                        }
                        if (file == null || file.delete()) {
                            return;
                        }
                        str = TAG;
                        sb = new StringBuilder();
                        sb.append("failed to delete temporary dumpsys file: ");
                        sb.append(file.getAbsolutePath());
                        Slog.e(str, sb.toString());
                    } catch (IOException e2) {
                        e = e2;
                        fileOutputStream = fileOutputStream2;
                        Slog.e(TAG, "failed to write dumpsys file", e);
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException unused3) {
                                Slog.e(TAG, "failed to close dumpsys output stream");
                            }
                        }
                        if (file == null || file.delete()) {
                            return;
                        }
                        str = TAG;
                        sb = new StringBuilder();
                        sb.append("failed to delete temporary dumpsys file: ");
                        sb.append(file.getAbsolutePath());
                        Slog.e(str, sb.toString());
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException unused4) {
                                Slog.e(TAG, "failed to close dumpsys output stream");
                            }
                        }
                        if (file == null) {
                            throw th;
                        }
                        if (file.delete()) {
                            throw th;
                        }
                        Slog.e(TAG, "failed to delete temporary dumpsys file: " + file.getAbsolutePath());
                        throw th;
                    }
                } catch (RemoteException e3) {
                    e = e3;
                } catch (IOException e4) {
                    e = e4;
                }
            } catch (RemoteException e5) {
                e = e5;
                file = null;
            } catch (IOException e6) {
                e = e6;
                file = null;
            } catch (Throwable th2) {
                th = th2;
                file = null;
            }
            if (file.delete()) {
                return;
            }
            str = TAG;
            sb = new StringBuilder();
            sb.append("failed to delete temporary dumpsys file: ");
            sb.append(file.getAbsolutePath());
            Slog.e(str, sb.toString());
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private void logOutlierLocked(long j) {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        String string = Settings.Global.getString(contentResolver, "battery_discharge_threshold");
        String string2 = Settings.Global.getString(contentResolver, "battery_discharge_duration_threshold");
        if (string == null || string2 == null) {
            return;
        }
        try {
            long parseLong = Long.parseLong(string2);
            int parseInt = Integer.parseInt(string);
            if (j <= parseLong && this.mDischargeStartLevel - this.mHealthInfo.batteryLevel >= parseInt) {
                logBatteryStatsLocked();
            }
            if (DEBUG) {
                Slog.v(TAG, "duration threshold: " + parseLong + " discharge threshold: " + parseInt);
            }
            if (DEBUG) {
                Slog.v(TAG, "duration: " + j + " discharge: " + (this.mDischargeStartLevel - this.mHealthInfo.batteryLevel));
            }
        } catch (NumberFormatException unused) {
            Slog.e(TAG, "Invalid DischargeThresholds GService string: " + string2 + " or " + string);
        }
    }

    private int getIconLocked(int i) {
        int i2 = this.mHealthInfo.batteryStatus;
        return i2 == 2 ? R.drawable.tab_bottom_right : i2 == 3 ? R.drawable.sym_keyboard_num6 : (i2 == 4 || i2 == 5) ? (!isPoweredLocked(15) || this.mHealthInfo.batteryLevel < 100) ? R.drawable.sym_keyboard_num6 : R.drawable.tab_bottom_right : R.drawable.tab_press_bar_right;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class Shell extends ShellCommand {
        Shell() {
        }

        public int onCommand(String str) {
            return BatteryService.this.onShellCommand(this, str);
        }

        public void onHelp() {
            BatteryService.dumpHelp(getOutPrintWriter());
        }
    }

    static void dumpHelp(PrintWriter printWriter) {
        printWriter.println("Battery service (battery) commands:");
        printWriter.println("  help");
        printWriter.println("    Print this help text.");
        printWriter.println("  get [-f] [ac|usb|wireless|dock|status|level|temp|present|counter|invalid]");
        printWriter.println("  set [-f] [ac|usb|wireless|dock|status|level|temp|present|counter|invalid] <value>");
        printWriter.println("    Force a battery property value, freezing battery state.");
        printWriter.println("    -f: force a battery change broadcast be sent, prints new sequence.");
        printWriter.println("  unplug [-f]");
        printWriter.println("    Force battery unplugged, freezing battery state.");
        printWriter.println("    -f: force a battery change broadcast be sent, prints new sequence.");
        printWriter.println("  reset [-f]");
        printWriter.println("    Unfreeze battery state, returning to current hardware values.");
        printWriter.println("    -f: force a battery change broadcast be sent, prints new sequence.");
        if (Build.IS_DEBUGGABLE) {
            printWriter.println("  suspend_input");
            printWriter.println("    Suspend charging even if plugged in. ");
        }
    }

    int parseOptions(Shell shell) {
        int i = 0;
        while (true) {
            String nextOption = shell.getNextOption();
            if (nextOption == null) {
                return i;
            }
            if ("-f".equals(nextOption)) {
                i |= 1;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    int onShellCommand(Shell shell, String str) {
        char c;
        char c2;
        int i;
        char c3;
        boolean z;
        if (str == null) {
            return shell.handleDefaultCommands(str);
        }
        PrintWriter outPrintWriter = shell.getOutPrintWriter();
        switch (str.hashCode()) {
            case -840325209:
                if (str.equals("unplug")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -541966841:
                if (str.equals("suspend_input")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 102230:
                if (str.equals("get")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 113762:
                if (str.equals("set")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 108404047:
                if (str.equals("reset")) {
                    c = 4;
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
                int parseOptions = parseOptions(shell);
                getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                unplugBattery((parseOptions & 1) != 0, outPrintWriter);
                break;
            case 1:
                getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                suspendBatteryInput();
                break;
            case 2:
                String nextArg = shell.getNextArg();
                if (nextArg == null) {
                    outPrintWriter.println("No property specified");
                    return -1;
                }
                switch (nextArg.hashCode()) {
                    case -1000044642:
                        if (nextArg.equals("wireless")) {
                            c2 = 0;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case -892481550:
                        if (nextArg.equals("status")) {
                            c2 = 1;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case -318277445:
                        if (nextArg.equals("present")) {
                            c2 = 2;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 3106:
                        if (nextArg.equals("ac")) {
                            c2 = 3;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 116100:
                        if (nextArg.equals("usb")) {
                            c2 = 4;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 3088947:
                        if (nextArg.equals("dock")) {
                            c2 = 5;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 3556308:
                        if (nextArg.equals("temp")) {
                            c2 = 6;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 102865796:
                        if (nextArg.equals("level")) {
                            c2 = 7;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 957830652:
                        if (nextArg.equals("counter")) {
                            c2 = '\b';
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 1959784951:
                        if (nextArg.equals("invalid")) {
                            c2 = '\t';
                            break;
                        }
                        c2 = 65535;
                        break;
                    default:
                        c2 = 65535;
                        break;
                }
                switch (c2) {
                    case 0:
                        outPrintWriter.println(this.mHealthInfo.chargerWirelessOnline);
                        break;
                    case 1:
                        outPrintWriter.println(this.mHealthInfo.batteryStatus);
                        break;
                    case 2:
                        outPrintWriter.println(this.mHealthInfo.batteryPresent);
                        break;
                    case 3:
                        outPrintWriter.println(this.mHealthInfo.chargerAcOnline);
                        break;
                    case 4:
                        outPrintWriter.println(this.mHealthInfo.chargerUsbOnline);
                        break;
                    case 5:
                        outPrintWriter.println(this.mHealthInfo.chargerDockOnline);
                        break;
                    case 6:
                        outPrintWriter.println(this.mHealthInfo.batteryTemperatureTenthsCelsius);
                        break;
                    case 7:
                        outPrintWriter.println(this.mHealthInfo.batteryLevel);
                        break;
                    case '\b':
                        outPrintWriter.println(this.mHealthInfo.batteryChargeCounterUah);
                        break;
                    case '\t':
                        outPrintWriter.println(this.mInvalidCharger);
                        break;
                    default:
                        outPrintWriter.println("Unknown get option: " + nextArg);
                        break;
                }
            case 3:
                int parseOptions2 = parseOptions(shell);
                getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                String nextArg2 = shell.getNextArg();
                if (nextArg2 == null) {
                    outPrintWriter.println("No property specified");
                    return -1;
                }
                String nextArg3 = shell.getNextArg();
                if (nextArg3 == null) {
                    outPrintWriter.println("No value specified");
                    return -1;
                }
                try {
                    if (this.mUpdatesStopped) {
                        i = parseOptions2;
                    } else {
                        i = parseOptions2;
                        Utils.copyV1Battery(this.mLastHealthInfo, this.mHealthInfo);
                    }
                    switch (nextArg2.hashCode()) {
                        case -1000044642:
                            if (nextArg2.equals("wireless")) {
                                c3 = 3;
                                break;
                            }
                            c3 = 65535;
                            break;
                        case -892481550:
                            if (nextArg2.equals("status")) {
                                c3 = 5;
                                break;
                            }
                            c3 = 65535;
                            break;
                        case -318277445:
                            if (nextArg2.equals("present")) {
                                c3 = 0;
                                break;
                            }
                            c3 = 65535;
                            break;
                        case 3106:
                            if (nextArg2.equals("ac")) {
                                c3 = 1;
                                break;
                            }
                            c3 = 65535;
                            break;
                        case 116100:
                            if (nextArg2.equals("usb")) {
                                c3 = 2;
                                break;
                            }
                            c3 = 65535;
                            break;
                        case 3088947:
                            if (nextArg2.equals("dock")) {
                                c3 = 4;
                                break;
                            }
                            c3 = 65535;
                            break;
                        case 3556308:
                            if (nextArg2.equals("temp")) {
                                c3 = '\b';
                                break;
                            }
                            c3 = 65535;
                            break;
                        case 102865796:
                            if (nextArg2.equals("level")) {
                                c3 = 6;
                                break;
                            }
                            c3 = 65535;
                            break;
                        case 957830652:
                            if (nextArg2.equals("counter")) {
                                c3 = 7;
                                break;
                            }
                            c3 = 65535;
                            break;
                        case 1959784951:
                            if (nextArg2.equals("invalid")) {
                                c3 = '\t';
                                break;
                            }
                            c3 = 65535;
                            break;
                        default:
                            c3 = 65535;
                            break;
                    }
                    switch (c3) {
                        case 0:
                            this.mHealthInfo.batteryPresent = Integer.parseInt(nextArg3) != 0;
                            z = true;
                            break;
                        case 1:
                            this.mHealthInfo.chargerAcOnline = Integer.parseInt(nextArg3) != 0;
                            z = true;
                            break;
                        case 2:
                            this.mHealthInfo.chargerUsbOnline = Integer.parseInt(nextArg3) != 0;
                            z = true;
                            break;
                        case 3:
                            this.mHealthInfo.chargerWirelessOnline = Integer.parseInt(nextArg3) != 0;
                            z = true;
                            break;
                        case 4:
                            this.mHealthInfo.chargerDockOnline = Integer.parseInt(nextArg3) != 0;
                            z = true;
                            break;
                        case 5:
                            this.mHealthInfo.batteryStatus = Integer.parseInt(nextArg3);
                            z = true;
                            break;
                        case 6:
                            this.mHealthInfo.batteryLevel = Integer.parseInt(nextArg3);
                            z = true;
                            break;
                        case 7:
                            this.mHealthInfo.batteryChargeCounterUah = Integer.parseInt(nextArg3);
                            z = true;
                            break;
                        case '\b':
                            this.mHealthInfo.batteryTemperatureTenthsCelsius = Integer.parseInt(nextArg3);
                            z = true;
                            break;
                        case '\t':
                            this.mInvalidCharger = Integer.parseInt(nextArg3);
                            z = true;
                            break;
                        default:
                            outPrintWriter.println("Unknown set option: " + nextArg2);
                            z = false;
                            break;
                    }
                    if (z) {
                        long clearCallingIdentity = Binder.clearCallingIdentity();
                        try {
                            this.mUpdatesStopped = true;
                            lambda$unplugBattery$3((i & 1) != 0, outPrintWriter);
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                            break;
                        } catch (Throwable th) {
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                            throw th;
                        }
                    }
                } catch (NumberFormatException unused) {
                    outPrintWriter.println("Bad value: " + nextArg3);
                    return -1;
                }
                break;
            case 4:
                int parseOptions3 = parseOptions(shell);
                getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                resetBattery((parseOptions3 & 1) != 0, outPrintWriter);
                break;
            default:
                return shell.handleDefaultCommands(str);
        }
        this.mBatteryServiceExt.setDebugCommand(this.mUpdatesStopped);
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setChargerAcOnline(boolean z, final boolean z2) {
        if (!this.mUpdatesStopped) {
            Utils.copyV1Battery(this.mLastHealthInfo, this.mHealthInfo);
        }
        this.mHealthInfo.chargerAcOnline = z;
        this.mUpdatesStopped = true;
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda3
            public final void runOrThrow() {
                BatteryService.this.lambda$setChargerAcOnline$1(z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBatteryLevel(int i, final boolean z) {
        if (!this.mUpdatesStopped) {
            Utils.copyV1Battery(this.mLastHealthInfo, this.mHealthInfo);
        }
        this.mHealthInfo.batteryLevel = i;
        this.mUpdatesStopped = true;
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda1
            public final void runOrThrow() {
                BatteryService.this.lambda$setBatteryLevel$2(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unplugBattery(final boolean z, final PrintWriter printWriter) {
        if (!this.mUpdatesStopped) {
            Utils.copyV1Battery(this.mLastHealthInfo, this.mHealthInfo);
        }
        HealthInfo healthInfo = this.mHealthInfo;
        healthInfo.chargerAcOnline = false;
        healthInfo.chargerUsbOnline = false;
        healthInfo.chargerWirelessOnline = false;
        healthInfo.chargerDockOnline = false;
        this.mUpdatesStopped = true;
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda5
            public final void runOrThrow() {
                BatteryService.this.lambda$unplugBattery$3(z, printWriter);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetBattery(final boolean z, final PrintWriter printWriter) {
        if (this.mUpdatesStopped) {
            this.mUpdatesStopped = false;
            Utils.copyV1Battery(this.mHealthInfo, this.mLastHealthInfo);
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda2
                public final void runOrThrow() {
                    BatteryService.this.lambda$resetBattery$4(z, printWriter);
                }
            });
        }
        if (this.mBatteryInputSuspended) {
            PowerProperties.battery_input_suspended(Boolean.FALSE);
            this.mBatteryInputSuspended = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void suspendBatteryInput() {
        if (!Build.IS_DEBUGGABLE) {
            throw new SecurityException("battery suspend_input is only supported on debuggable builds");
        }
        PowerProperties.battery_input_suspended(Boolean.TRUE);
        this.mBatteryInputSuspended = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: processValuesLocked, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$unplugBattery$3(boolean z, PrintWriter printWriter) {
        lambda$setChargerAcOnline$1(z);
        if (printWriter == null || !z) {
            return;
        }
        printWriter.println(this.mSequence);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpInternal(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        synchronized (this.mLock) {
            if (this.mBatteryServiceExt.dumpInternalBase(fileDescriptor, printWriter, strArr)) {
                if (strArr != null && strArr.length != 0 && !"-a".equals(strArr[0])) {
                    new Shell().exec(this.mBinderService, (FileDescriptor) null, fileDescriptor, (FileDescriptor) null, strArr, (ShellCallback) null, new ResultReceiver(null));
                }
                printWriter.println("Current Battery Service state:");
                if (this.mUpdatesStopped) {
                    printWriter.println("  (UPDATES STOPPED -- use 'reset' to restart)");
                }
                printWriter.println("  AC powered: " + this.mHealthInfo.chargerAcOnline);
                printWriter.println("  USB powered: " + this.mHealthInfo.chargerUsbOnline);
                printWriter.println("  Wireless powered: " + this.mHealthInfo.chargerWirelessOnline);
                printWriter.println("  Dock powered: " + this.mHealthInfo.chargerDockOnline);
                printWriter.println("  Max charging current: " + this.mHealthInfo.maxChargingCurrentMicroamps);
                printWriter.println("  Max charging voltage: " + this.mHealthInfo.maxChargingVoltageMicrovolts);
                printWriter.println("  Charge counter: " + this.mHealthInfo.batteryChargeCounterUah);
                printWriter.println("  status: " + this.mHealthInfo.batteryStatus);
                printWriter.println("  health: " + this.mHealthInfo.batteryHealth);
                printWriter.println("  present: " + this.mHealthInfo.batteryPresent);
                printWriter.println("  level: " + this.mHealthInfo.batteryLevel);
                printWriter.println("  scale: 100");
                printWriter.println("  voltage: " + this.mHealthInfo.batteryVoltageMillivolts);
                printWriter.println("  temperature: " + this.mHealthInfo.batteryTemperatureTenthsCelsius);
                printWriter.println("  technology: " + this.mHealthInfo.batteryTechnology);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpProto(FileDescriptor fileDescriptor) {
        int i;
        ProtoOutputStream protoOutputStream = new ProtoOutputStream(fileDescriptor);
        synchronized (this.mLock) {
            protoOutputStream.write(1133871366145L, this.mUpdatesStopped);
            HealthInfo healthInfo = this.mHealthInfo;
            if (healthInfo.chargerAcOnline) {
                i = 1;
            } else if (healthInfo.chargerUsbOnline) {
                i = 2;
            } else if (healthInfo.chargerWirelessOnline) {
                i = 4;
            } else {
                i = healthInfo.chargerDockOnline ? 8 : 0;
            }
            protoOutputStream.write(1159641169922L, i);
            protoOutputStream.write(1120986464259L, this.mHealthInfo.maxChargingCurrentMicroamps);
            protoOutputStream.write(1120986464260L, this.mHealthInfo.maxChargingVoltageMicrovolts);
            protoOutputStream.write(1120986464261L, this.mHealthInfo.batteryChargeCounterUah);
            protoOutputStream.write(1159641169926L, this.mHealthInfo.batteryStatus);
            protoOutputStream.write(1159641169927L, this.mHealthInfo.batteryHealth);
            protoOutputStream.write(1133871366152L, this.mHealthInfo.batteryPresent);
            protoOutputStream.write(1120986464265L, this.mHealthInfo.batteryLevel);
            protoOutputStream.write(1120986464266L, 100);
            protoOutputStream.write(1120986464267L, this.mHealthInfo.batteryVoltageMillivolts);
            protoOutputStream.write(1120986464268L, this.mHealthInfo.batteryTemperatureTenthsCelsius);
            protoOutputStream.write(1138166333453L, this.mHealthInfo.batteryTechnology);
        }
        protoOutputStream.flush();
    }

    private static void traceBegin(String str) {
        Trace.traceBegin(524288L, str);
    }

    private static void traceEnd() {
        Trace.traceEnd(524288L);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class Led {
        static final int LOW_BATTERY_BEHAVIOR_DEFAULT = 0;
        static final int LOW_BATTERY_BEHAVIOR_FLASHING = 2;
        static final int LOW_BATTERY_BEHAVIOR_SOLID = 1;
        private final int mBatteryFullARGB;
        private final int mBatteryLedOff;
        private final int mBatteryLedOn;
        private final LogicalLight mBatteryLight;
        private final int mBatteryLowARGB;
        private final int mBatteryLowBehavior;
        private final int mBatteryMediumARGB;
        private ILedExt mLedExt;
        private LedWrapper mLedWrapper = new LedWrapper();

        public Led(Context context, LightsManager lightsManager) {
            ILedExt iLedExt = (ILedExt) ExtLoader.type(ILedExt.class).base(this).create();
            this.mLedExt = iLedExt;
            iLedExt.initLedExtImpl(context, lightsManager, this);
            this.mBatteryLight = lightsManager.getLight(3);
            this.mBatteryLowARGB = context.getResources().getInteger(R.integer.date_picker_mode);
            this.mBatteryMediumARGB = context.getResources().getInteger(R.integer.db_connection_pool_size);
            this.mBatteryFullARGB = context.getResources().getInteger(R.integer.config_windowOutsetBottom);
            this.mBatteryLedOn = context.getResources().getInteger(R.integer.date_picker_header_max_lines_material);
            this.mBatteryLedOff = context.getResources().getInteger(R.integer.config_zen_repeat_callers_threshold);
            BatteryService.this.mBatteryNearlyFullLevel = context.getResources().getInteger(R.integer.db_default_idle_connection_timeout);
            this.mBatteryLowBehavior = context.getResources().getInteger(R.integer.date_picker_mode_material);
        }

        public void updateLightsLocked() {
            if (this.mLedExt.isIgnoreUpdateLights(BatteryService.this.mHealthInfo) || this.mBatteryLight == null) {
                return;
            }
            int i = BatteryService.this.mHealthInfo.batteryLevel;
            int i2 = BatteryService.this.mHealthInfo.batteryStatus;
            if (i >= BatteryService.this.mLowBatteryWarningLevel) {
                if (i2 == 2 || i2 == 5) {
                    if (i2 == 5 || i >= BatteryService.this.mBatteryNearlyFullLevel) {
                        this.mBatteryLight.setColor(this.mBatteryFullARGB);
                        return;
                    } else {
                        this.mBatteryLight.setColor(this.mBatteryMediumARGB);
                        return;
                    }
                }
                this.mBatteryLight.turnOff();
                return;
            }
            int i3 = this.mBatteryLowBehavior;
            if (i3 == 1) {
                this.mBatteryLight.setColor(this.mBatteryLowARGB);
                return;
            }
            if (i3 == 2) {
                this.mBatteryLight.setFlashing(this.mBatteryLowARGB, 1, this.mBatteryLedOn, this.mBatteryLedOff);
            } else if (i2 == 2) {
                this.mBatteryLight.setColor(this.mBatteryLowARGB);
            } else {
                this.mBatteryLight.setFlashing(this.mBatteryLowARGB, 1, this.mBatteryLedOn, this.mBatteryLedOff);
            }
        }

        public void onUpdateLights() {
            synchronized (BatteryService.this.mLock) {
                updateLightsLocked();
            }
        }

        public ILedWrapper getWrapper() {
            return this.mLedWrapper;
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private class LedWrapper implements ILedWrapper {
            private LedWrapper() {
            }

            @Override // com.android.server.ILedWrapper
            public ILedExt getExtImpl() {
                return Led.this.mLedExt;
            }

            @Override // com.android.server.ILedWrapper
            public LogicalLight getBatteryLight() {
                return Led.this.mBatteryLight;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class BinderService extends Binder {
        private BinderService() {
        }

        @Override // android.os.Binder
        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(BatteryService.this.mContext, BatteryService.TAG, printWriter)) {
                if (strArr.length > 0 && "--proto".equals(strArr[0])) {
                    BatteryService.this.dumpProto(fileDescriptor);
                } else {
                    BatteryService.this.dumpInternal(fileDescriptor, printWriter, strArr);
                }
            }
        }

        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
            new Shell().exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class BatteryPropertiesRegistrar extends IBatteryPropertiesRegistrar.Stub {
        private BatteryPropertiesRegistrar() {
        }

        public int getProperty(int i, BatteryProperty batteryProperty) throws RemoteException {
            switch (i) {
                case 7:
                case 8:
                case 9:
                case 10:
                    BatteryService.this.mContext.enforceCallingPermission("android.permission.BATTERY_STATS", null);
                    break;
            }
            return BatteryService.this.mHealthServiceWrapper.getProperty(i, batteryProperty);
        }

        public void scheduleUpdate() throws RemoteException {
            BatteryService.this.mHealthServiceWrapper.scheduleUpdate();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class LocalService extends BatteryManagerInternal {
        private LocalService() {
        }

        public boolean isPowered(int i) {
            boolean isPoweredLocked;
            synchronized (BatteryService.this.mLock) {
                isPoweredLocked = BatteryService.this.isPoweredLocked(i);
            }
            return isPoweredLocked;
        }

        public int getPlugType() {
            int i;
            synchronized (BatteryService.this.mLock) {
                i = BatteryService.this.mPlugType;
            }
            return i;
        }

        public int getBatteryLevel() {
            int i;
            synchronized (BatteryService.this.mLock) {
                i = BatteryService.this.mHealthInfo.batteryLevel;
            }
            return i;
        }

        public int getBatteryChargeCounter() {
            int i;
            synchronized (BatteryService.this.mLock) {
                i = BatteryService.this.mHealthInfo.batteryChargeCounterUah;
            }
            return i;
        }

        public int getBatteryFullCharge() {
            int i;
            synchronized (BatteryService.this.mLock) {
                i = BatteryService.this.mHealthInfo.batteryFullChargeUah;
            }
            return i;
        }

        public int getBatteryHealth() {
            int i;
            synchronized (BatteryService.this.mLock) {
                i = BatteryService.this.mHealthInfo.batteryHealth;
            }
            return i;
        }

        public boolean getBatteryLevelLow() {
            boolean z;
            synchronized (BatteryService.this.mLock) {
                z = BatteryService.this.mBatteryLevelLow;
            }
            return z;
        }

        public int getInvalidCharger() {
            int i;
            synchronized (BatteryService.this.mLock) {
                i = BatteryService.this.mInvalidCharger;
            }
            return i;
        }

        public void setChargerAcOnline(boolean z, boolean z2) {
            BatteryService.this.setChargerAcOnline(z, z2);
        }

        public void setBatteryLevel(int i, boolean z) {
            BatteryService.this.setBatteryLevel(i, z);
        }

        public void unplugBattery(boolean z) {
            BatteryService.this.unplugBattery(z, null);
        }

        public void resetBattery(boolean z) {
            BatteryService.this.resetBattery(z, null);
        }

        public void suspendBatteryInput() {
            BatteryService.this.suspendBatteryInput();
        }
    }
}
