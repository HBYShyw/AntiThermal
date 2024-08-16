package com.android.server.display;

import android.R;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.display.AmbientBrightnessDayStats;
import android.hardware.display.BrightnessChangeEvent;
import android.hardware.display.BrightnessConfiguration;
import android.hardware.display.BrightnessInfo;
import android.hardware.display.DisplayManagerInternal;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.Settings;
import android.util.FloatProperty;
import android.util.Log;
import android.util.MathUtils;
import android.util.MutableFloat;
import android.util.MutableInt;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Display;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IBatteryStats;
import com.android.internal.display.BrightnessSynchronizer;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.RingBuffer;
import com.android.server.LocalServices;
import com.android.server.am.BatteryStatsService;
import com.android.server.display.AutomaticBrightnessController;
import com.android.server.display.BrightnessSetting;
import com.android.server.display.DisplayDeviceConfig;
import com.android.server.display.HighBrightnessModeController;
import com.android.server.display.RampAnimator;
import com.android.server.display.ScreenOffBrightnessSensorController;
import com.android.server.display.brightness.BrightnessEvent;
import com.android.server.display.brightness.BrightnessReason;
import com.android.server.display.brightness.DisplayBrightnessController;
import com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy;
import com.android.server.display.color.ColorDisplayService;
import com.android.server.display.state.DisplayStateController;
import com.android.server.display.utils.SensorUtils;
import com.android.server.display.whitebalance.DisplayWhiteBalanceController;
import com.android.server.display.whitebalance.DisplayWhiteBalanceFactory;
import com.android.server.display.whitebalance.DisplayWhiteBalanceSettings;
import com.android.server.policy.WindowManagerPolicy;
import java.io.PrintWriter;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DisplayPowerController2 implements AutomaticBrightnessController.Callbacks, DisplayWhiteBalanceController.Callbacks, DisplayPowerControllerInterface {
    private static final int BRIGHTNESS_CHANGE_STATSD_REPORT_INTERVAL_MS = 500;
    private static final int COLOR_FADE_OFF_ANIMATION_DURATION_MILLIS = 200;
    private static final int COLOR_FADE_ON_ANIMATION_DURATION_MILLIS = 250;
    private static final boolean DEBUG = false;
    private static final int MSG_BOOT_COMPLETED = 13;
    private static final int MSG_BRIGHTNESS_RAMP_DONE = 10;
    private static final int MSG_CONFIGURE_BRIGHTNESS = 4;
    private static final int MSG_SCREEN_OFF_UNBLOCKED = 3;
    private static final int MSG_SCREEN_ON_UNBLOCKED = 2;
    private static final int MSG_SET_TEMPORARY_AUTO_BRIGHTNESS_ADJUSTMENT = 6;
    private static final int MSG_SET_TEMPORARY_BRIGHTNESS = 5;
    private static final int MSG_STATSD_HBM_BRIGHTNESS = 11;
    private static final int MSG_STOP = 7;
    private static final int MSG_SWITCH_USER = 12;
    private static final int MSG_UPDATE_BRIGHTNESS = 8;
    private static final int MSG_UPDATE_POWER_STATE = 1;
    private static final int MSG_UPDATE_RBC = 9;
    private static final int RAMP_STATE_SKIP_AUTOBRIGHT = 2;
    private static final int RAMP_STATE_SKIP_INITIAL = 1;
    private static final int RAMP_STATE_SKIP_NONE = 0;
    private static final int REPORTED_TO_POLICY_SCREEN_OFF = 0;
    private static final int REPORTED_TO_POLICY_SCREEN_ON = 2;
    private static final int REPORTED_TO_POLICY_SCREEN_TURNING_OFF = 3;
    private static final int REPORTED_TO_POLICY_SCREEN_TURNING_ON = 1;
    private static final int REPORTED_TO_POLICY_UNREPORTED = -1;
    private static final int RINGBUFFER_MAX = 100;
    private static final int RINGBUFFER_RBC_MAX = 20;
    private static final String SCREEN_OFF_BLOCKED_TRACE_NAME = "Screen off blocked";
    private static final String SCREEN_ON_BLOCKED_TRACE_NAME = "Screen on blocked";
    private static final boolean USE_COLOR_FADE_ON_ANIMATION = false;
    private boolean mAppliedDimming;
    private boolean mAppliedLowPower;
    private boolean mAppliedThrottling;
    private AutomaticBrightnessController mAutomaticBrightnessController;
    private final AutomaticBrightnessStrategy mAutomaticBrightnessStrategy;
    private final IBatteryStats mBatteryStats;
    private final DisplayBlanker mBlanker;
    private boolean mBootCompleted;
    private final boolean mBrightnessBucketsInDozeConfig;
    private RingBuffer<BrightnessEvent> mBrightnessEventRingBuffer;
    private long mBrightnessRampDecreaseMaxTimeMillis;
    private long mBrightnessRampIncreaseMaxTimeMillis;
    private float mBrightnessRampRateFastDecrease;
    private float mBrightnessRampRateFastIncrease;
    private float mBrightnessRampRateSlowDecrease;
    private float mBrightnessRampRateSlowIncrease;
    private final BrightnessThrottler mBrightnessThrottler;
    private final BrightnessTracker mBrightnessTracker;
    private final ColorDisplayService.ColorDisplayServiceInternal mCdsi;
    private final Clock mClock;
    private final boolean mColorFadeEnabled;
    private final boolean mColorFadeFadesConfig;
    private ObjectAnimator mColorFadeOffAnimator;
    private ObjectAnimator mColorFadeOnAnimator;
    private final Context mContext;
    private final boolean mDisplayBlanksAfterDozeConfig;
    private final DisplayBrightnessController mDisplayBrightnessController;
    private DisplayDevice mDisplayDevice;
    private DisplayDeviceConfig mDisplayDeviceConfig;
    private final int mDisplayId;
    private final DisplayPowerProximityStateController mDisplayPowerProximityStateController;

    @GuardedBy({"mLock"})
    private boolean mDisplayReadyLocked;
    private final DisplayStateController mDisplayStateController;
    private int mDisplayStatsId;
    private final DisplayWhiteBalanceController mDisplayWhiteBalanceController;
    private final DisplayWhiteBalanceSettings mDisplayWhiteBalanceSettings;
    private boolean mDozing;
    private final DisplayControllerHandler mHandler;
    private final HighBrightnessModeController mHbmController;
    private final HighBrightnessModeMetadata mHighBrightnessModeMetadata;
    private BrightnessMappingStrategy mIdleModeBrightnessMapper;
    private float mInitialAutoBrightness;
    private final Injector mInjector;
    private BrightnessMappingStrategy mInteractiveModeBrightnessMapper;
    private boolean mIsDisplayInternal;
    private boolean mIsEnabled;
    private boolean mIsInTransition;
    private boolean mIsRbcActive;
    private final BrightnessEvent mLastBrightnessEvent;
    private Sensor mLightSensor;
    private final LogicalDisplay mLogicalDisplay;
    private float[] mNitsRange;
    private final Runnable mOnBrightnessChangeRunnable;

    @GuardedBy({"mLock"})
    private boolean mPendingRequestChangedLocked;

    @GuardedBy({"mLock"})
    private DisplayManagerInternal.DisplayPowerRequest mPendingRequestLocked;
    private boolean mPendingScreenOff;
    private ScreenOffUnblocker mPendingScreenOffUnblocker;
    private ScreenOnUnblocker mPendingScreenOnUnblocker;

    @GuardedBy({"mLock"})
    private boolean mPendingUpdatePowerStateLocked;
    private DisplayManagerInternal.DisplayPowerRequest mPowerRequest;
    private DisplayPowerState mPowerState;
    private final float mScreenBrightnessDimConfig;
    private final float mScreenBrightnessDozeConfig;
    private final float mScreenBrightnessMinimumDimAmount;
    private RampAnimator.DualRampAnimator<DisplayPowerState> mScreenBrightnessRampAnimator;
    private long mScreenOffBlockStartRealTime;
    private Sensor mScreenOffBrightnessSensor;
    private ScreenOffBrightnessSensorController mScreenOffBrightnessSensorController;
    private long mScreenOnBlockStartRealTime;
    private final SensorManager mSensorManager;
    private final SettingsObserver mSettingsObserver;
    private final boolean mSkipScreenOnBrightnessRamp;
    private boolean mStopped;
    private final String mTag;
    private final BrightnessEvent mTempBrightnessEvent;
    private String mThermalBrightnessThrottlingDataId;
    private String mUniqueDisplayId;
    private boolean mUseSoftwareAutoBrightnessConfig;
    private final WakelockController mWakelockController;
    private final WindowManagerPolicy mWindowManagerPolicy;
    private static final float SCREEN_ANIMATION_RATE_MINIMUM = 0.0f;
    private static final float[] BRIGHTNESS_RANGE_BOUNDARIES = {SCREEN_ANIMATION_RATE_MINIMUM, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 20.0f, 30.0f, 40.0f, 50.0f, 60.0f, 70.0f, 80.0f, 90.0f, 100.0f, 200.0f, 300.0f, 400.0f, 500.0f, 600.0f, 700.0f, 800.0f, 900.0f, 1000.0f, 1200.0f, 1400.0f, 1600.0f, 1800.0f, 2000.0f, 2250.0f, 2500.0f, 2750.0f, 3000.0f};
    private static final int[] BRIGHTNESS_RANGE_INDEX = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37};
    private final Object mLock = new Object();
    private int mLeadDisplayId = -1;

    @GuardedBy({"mCachedBrightnessInfo"})
    private final CachedBrightnessInfo mCachedBrightnessInfo = new CachedBrightnessInfo();
    private int mReportedScreenStateToPolicy = -1;
    private final BrightnessReason mBrightnessReason = new BrightnessReason();
    private final BrightnessReason mBrightnessReasonTemp = new BrightnessReason();
    private float mLastStatsBrightness = SCREEN_ANIMATION_RATE_MINIMUM;
    private final RingBuffer<BrightnessEvent> mRbcEventRingBuffer = new RingBuffer<>(BrightnessEvent.class, 20);
    private int mSkipRampState = 0;

    @GuardedBy({"mLock"})
    private SparseArray<DisplayPowerControllerInterface> mDisplayBrightnessFollowers = new SparseArray<>();
    private final Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() { // from class: com.android.server.display.DisplayPowerController2.3
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            DisplayPowerController2.this.sendUpdatePowerState();
        }
    };
    private final RampAnimator.Listener mRampAnimatorListener = new RampAnimator.Listener() { // from class: com.android.server.display.DisplayPowerController2.4
        @Override // com.android.server.display.RampAnimator.Listener
        public void onAnimationStart(boolean z) {
        }

        @Override // com.android.server.display.RampAnimator.Listener
        public void onAnimationEnd(boolean z) {
            DisplayPowerController2.this.sendUpdatePowerState();
            DisplayPowerController2.this.mHandler.sendMessageAtTime(DisplayPowerController2.this.mHandler.obtainMessage(10), DisplayPowerController2.this.mClock.uptimeMillis());
        }
    };
    private final Runnable mCleanListener = new Runnable() { // from class: com.android.server.display.DisplayPowerController2$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            DisplayPowerController2.this.sendUpdatePowerState();
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Clock {
        long uptimeMillis();
    }

    private int convertBrightnessReasonToStatsEnum(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            default:
                return 0;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x01f4  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x020d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    DisplayPowerController2(Context context, Injector injector, DisplayManagerInternal.DisplayPowerCallbacks displayPowerCallbacks, Handler handler, SensorManager sensorManager, DisplayBlanker displayBlanker, LogicalDisplay logicalDisplay, BrightnessTracker brightnessTracker, BrightnessSetting brightnessSetting, Runnable runnable, HighBrightnessModeMetadata highBrightnessModeMetadata, boolean z) {
        DisplayWhiteBalanceSettings displayWhiteBalanceSettings;
        DisplayWhiteBalanceController displayWhiteBalanceController;
        Injector injector2 = injector != null ? injector : new Injector();
        this.mInjector = injector2;
        this.mClock = injector2.getClock();
        this.mLogicalDisplay = logicalDisplay;
        int displayIdLocked = logicalDisplay.getDisplayIdLocked();
        this.mDisplayId = displayIdLocked;
        this.mSensorManager = sensorManager;
        DisplayControllerHandler displayControllerHandler = new DisplayControllerHandler(handler.getLooper());
        this.mHandler = displayControllerHandler;
        this.mDisplayDeviceConfig = logicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayDeviceConfig();
        this.mIsEnabled = logicalDisplay.isEnabledLocked();
        this.mIsInTransition = logicalDisplay.isInTransitionLocked();
        this.mIsDisplayInternal = logicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayDeviceInfoLocked().type == 1;
        WakelockController wakelockController = injector2.getWakelockController(displayIdLocked, displayPowerCallbacks);
        this.mWakelockController = wakelockController;
        DisplayPowerProximityStateController displayPowerProximityStateController = injector2.getDisplayPowerProximityStateController(wakelockController, this.mDisplayDeviceConfig, displayControllerHandler.getLooper(), new Runnable() { // from class: com.android.server.display.DisplayPowerController2$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPowerController2.this.lambda$new$0();
            }
        }, displayIdLocked, sensorManager);
        this.mDisplayPowerProximityStateController = displayPowerProximityStateController;
        this.mHighBrightnessModeMetadata = highBrightnessModeMetadata;
        this.mDisplayStateController = new DisplayStateController(displayPowerProximityStateController);
        this.mAutomaticBrightnessStrategy = new AutomaticBrightnessStrategy(context, displayIdLocked);
        this.mTag = "DisplayPowerController2[" + displayIdLocked + "]";
        this.mThermalBrightnessThrottlingDataId = logicalDisplay.getDisplayInfoLocked().thermalBrightnessThrottlingDataId;
        this.mDisplayDevice = logicalDisplay.getPrimaryDisplayDeviceLocked();
        String uniqueId = logicalDisplay.getPrimaryDisplayDeviceLocked().getUniqueId();
        this.mUniqueDisplayId = uniqueId;
        this.mDisplayStatsId = uniqueId.hashCode();
        this.mLastBrightnessEvent = new BrightnessEvent(displayIdLocked);
        this.mTempBrightnessEvent = new BrightnessEvent(displayIdLocked);
        if (displayIdLocked == 0) {
            this.mBatteryStats = BatteryStatsService.getService();
        } else {
            this.mBatteryStats = null;
        }
        this.mSettingsObserver = new SettingsObserver(displayControllerHandler);
        this.mWindowManagerPolicy = (WindowManagerPolicy) LocalServices.getService(WindowManagerPolicy.class);
        this.mBlanker = displayBlanker;
        this.mContext = context;
        this.mBrightnessTracker = brightnessTracker;
        this.mOnBrightnessChangeRunnable = runnable;
        PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
        Resources resources = context.getResources();
        this.mScreenBrightnessDozeConfig = com.android.server.display.brightness.BrightnessUtils.clampAbsoluteBrightness(powerManager.getBrightnessConstraint(4));
        this.mScreenBrightnessDimConfig = com.android.server.display.brightness.BrightnessUtils.clampAbsoluteBrightness(powerManager.getBrightnessConstraint(3));
        this.mScreenBrightnessMinimumDimAmount = resources.getFloat(R.dimen.conversation_face_pile_protection_width_expanded);
        loadBrightnessRampRates();
        this.mSkipScreenOnBrightnessRamp = resources.getBoolean(17891821);
        this.mHbmController = createHbmControllerLocked();
        this.mBrightnessThrottler = createBrightnessThrottlerLocked();
        this.mDisplayBrightnessController = new DisplayBrightnessController(context, null, displayIdLocked, logicalDisplay.getDisplayInfoLocked().brightnessDefault, brightnessSetting, new Runnable() { // from class: com.android.server.display.DisplayPowerController2$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPowerController2.this.lambda$new$1();
            }
        }, new HandlerExecutor(displayControllerHandler));
        saveBrightnessInfo(getScreenBrightnessSetting());
        if (displayIdLocked == 0) {
            try {
                displayWhiteBalanceSettings = new DisplayWhiteBalanceSettings(context, displayControllerHandler);
                try {
                    displayWhiteBalanceController = DisplayWhiteBalanceFactory.create(displayControllerHandler, sensorManager, resources);
                    try {
                        displayWhiteBalanceSettings.setCallbacks(this);
                        displayWhiteBalanceController.setCallbacks(this);
                    } catch (Exception e) {
                        e = e;
                        Slog.e(this.mTag, "failed to set up display white-balance: " + e);
                        this.mDisplayWhiteBalanceSettings = displayWhiteBalanceSettings;
                        this.mDisplayWhiteBalanceController = displayWhiteBalanceController;
                        loadNitsRange(resources);
                        if (this.mDisplayId != 0) {
                        }
                        setUpAutoBrightness(resources, handler);
                        this.mColorFadeEnabled = !ActivityManager.isLowRamDeviceStatic();
                        this.mColorFadeFadesConfig = resources.getBoolean(R.bool.config_awareSettingAvailable);
                        this.mDisplayBlanksAfterDozeConfig = resources.getBoolean(17891621);
                        this.mBrightnessBucketsInDozeConfig = resources.getBoolean(17891622);
                        this.mBootCompleted = z;
                    }
                } catch (Exception e2) {
                    e = e2;
                    displayWhiteBalanceController = null;
                }
            } catch (Exception e3) {
                e = e3;
                displayWhiteBalanceSettings = null;
                displayWhiteBalanceController = null;
            }
        } else {
            displayWhiteBalanceSettings = null;
            displayWhiteBalanceController = null;
        }
        this.mDisplayWhiteBalanceSettings = displayWhiteBalanceSettings;
        this.mDisplayWhiteBalanceController = displayWhiteBalanceController;
        loadNitsRange(resources);
        if (this.mDisplayId != 0) {
            ColorDisplayService.ColorDisplayServiceInternal colorDisplayServiceInternal = (ColorDisplayService.ColorDisplayServiceInternal) LocalServices.getService(ColorDisplayService.ColorDisplayServiceInternal.class);
            this.mCdsi = colorDisplayServiceInternal;
            if (colorDisplayServiceInternal.setReduceBrightColorsListener(new ColorDisplayService.ReduceBrightColorsListener() { // from class: com.android.server.display.DisplayPowerController2.1
                @Override // com.android.server.display.color.ColorDisplayService.ReduceBrightColorsListener
                public void onReduceBrightColorsActivationChanged(boolean z2, boolean z3) {
                    DisplayPowerController2.this.applyReduceBrightColorsSplineAdjustment();
                }

                @Override // com.android.server.display.color.ColorDisplayService.ReduceBrightColorsListener
                public void onReduceBrightColorsStrengthChanged(int i) {
                    DisplayPowerController2.this.applyReduceBrightColorsSplineAdjustment();
                }
            })) {
                applyReduceBrightColorsSplineAdjustment();
            }
        } else {
            this.mCdsi = null;
        }
        setUpAutoBrightness(resources, handler);
        this.mColorFadeEnabled = !ActivityManager.isLowRamDeviceStatic();
        this.mColorFadeFadesConfig = resources.getBoolean(R.bool.config_awareSettingAvailable);
        this.mDisplayBlanksAfterDozeConfig = resources.getBoolean(17891621);
        this.mBrightnessBucketsInDozeConfig = resources.getBoolean(17891622);
        this.mBootCompleted = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyReduceBrightColorsSplineAdjustment() {
        this.mHandler.obtainMessage(9).sendToTarget();
        sendUpdatePowerState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRbcChanged() {
        AutomaticBrightnessController automaticBrightnessController = this.mAutomaticBrightnessController;
        if (automaticBrightnessController == null) {
            return;
        }
        if ((!automaticBrightnessController.isInIdleMode() && this.mInteractiveModeBrightnessMapper == null) || (this.mAutomaticBrightnessController.isInIdleMode() && this.mIdleModeBrightnessMapper == null)) {
            Log.w(this.mTag, "No brightness mapping available to recalculate splines for this mode");
            return;
        }
        float[] fArr = new float[this.mNitsRange.length];
        int i = 0;
        while (true) {
            float[] fArr2 = this.mNitsRange;
            if (i < fArr2.length) {
                fArr[i] = this.mCdsi.getReduceBrightColorsAdjustedBrightnessNits(fArr2[i]);
                i++;
            } else {
                boolean isReduceBrightColorsActivated = this.mCdsi.isReduceBrightColorsActivated();
                this.mIsRbcActive = isReduceBrightColorsActivated;
                this.mAutomaticBrightnessController.recalculateSplines(isReduceBrightColorsActivated, fArr);
                return;
            }
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public boolean isProximitySensorAvailable() {
        return this.mDisplayPowerProximityStateController.isProximitySensorAvailable();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public ParceledListSlice<BrightnessChangeEvent> getBrightnessEvents(int i, boolean z) {
        BrightnessTracker brightnessTracker = this.mBrightnessTracker;
        if (brightnessTracker == null) {
            return null;
        }
        return brightnessTracker.getEvents(i, z);
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void onSwitchUser(int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(12, Integer.valueOf(i)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnSwitchUser(int i) {
        handleSettingsChange(true);
        handleBrightnessModeChange();
        BrightnessTracker brightnessTracker = this.mBrightnessTracker;
        if (brightnessTracker != null) {
            brightnessTracker.onSwitchUser(i);
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public ParceledListSlice<AmbientBrightnessDayStats> getAmbientBrightnessStats(int i) {
        BrightnessTracker brightnessTracker = this.mBrightnessTracker;
        if (brightnessTracker == null) {
            return null;
        }
        return brightnessTracker.getAmbientBrightnessStats(i);
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void persistBrightnessTrackerState() {
        BrightnessTracker brightnessTracker = this.mBrightnessTracker;
        if (brightnessTracker != null) {
            brightnessTracker.persistBrightnessTrackerState();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x002b A[Catch: all -> 0x003b, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0008, B:9:0x000a, B:11:0x0014, B:14:0x002b, B:16:0x0032, B:17:0x0037, B:18:0x0039, B:20:0x001d, B:22:0x0023), top: B:3:0x0003 }] */
    @Override // com.android.server.display.DisplayPowerControllerInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean requestPowerState(DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, boolean z) {
        synchronized (this.mLock) {
            if (this.mStopped) {
                return true;
            }
            boolean pendingWaitForNegativeProximityLocked = this.mDisplayPowerProximityStateController.setPendingWaitForNegativeProximityLocked(z);
            DisplayManagerInternal.DisplayPowerRequest displayPowerRequest2 = this.mPendingRequestLocked;
            if (displayPowerRequest2 == null) {
                this.mPendingRequestLocked = new DisplayManagerInternal.DisplayPowerRequest(displayPowerRequest);
            } else {
                if (!displayPowerRequest2.equals(displayPowerRequest)) {
                    this.mPendingRequestLocked.copyFrom(displayPowerRequest);
                }
                if (pendingWaitForNegativeProximityLocked) {
                    this.mDisplayReadyLocked = false;
                    if (!this.mPendingRequestChangedLocked) {
                        this.mPendingRequestChangedLocked = true;
                        sendUpdatePowerStateLocked();
                    }
                }
                return this.mDisplayReadyLocked;
            }
            pendingWaitForNegativeProximityLocked = true;
            if (pendingWaitForNegativeProximityLocked) {
            }
            return this.mDisplayReadyLocked;
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public BrightnessConfiguration getDefaultBrightnessConfiguration() {
        AutomaticBrightnessController automaticBrightnessController = this.mAutomaticBrightnessController;
        if (automaticBrightnessController == null) {
            return null;
        }
        return automaticBrightnessController.getDefaultConfig();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void onDisplayChanged(final HighBrightnessModeMetadata highBrightnessModeMetadata, int i) {
        this.mLeadDisplayId = i;
        final DisplayDevice primaryDisplayDeviceLocked = this.mLogicalDisplay.getPrimaryDisplayDeviceLocked();
        if (primaryDisplayDeviceLocked == null) {
            Slog.wtf(this.mTag, "Display Device is null in DisplayPowerController2 for display: " + this.mLogicalDisplay.getDisplayIdLocked());
            return;
        }
        final String uniqueId = primaryDisplayDeviceLocked.getUniqueId();
        final DisplayDeviceConfig displayDeviceConfig = primaryDisplayDeviceLocked.getDisplayDeviceConfig();
        final IBinder displayTokenLocked = primaryDisplayDeviceLocked.getDisplayTokenLocked();
        final DisplayDeviceInfo displayDeviceInfoLocked = primaryDisplayDeviceLocked.getDisplayDeviceInfoLocked();
        final boolean isEnabledLocked = this.mLogicalDisplay.isEnabledLocked();
        final boolean isInTransitionLocked = this.mLogicalDisplay.isInTransitionLocked();
        final boolean z = this.mLogicalDisplay.getPrimaryDisplayDeviceLocked() != null && this.mLogicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayDeviceInfoLocked().type == 1;
        final String str = this.mLogicalDisplay.getDisplayInfoLocked().thermalBrightnessThrottlingDataId;
        this.mHandler.postAtTime(new Runnable() { // from class: com.android.server.display.DisplayPowerController2$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPowerController2.this.lambda$onDisplayChanged$2(primaryDisplayDeviceLocked, uniqueId, displayDeviceConfig, str, displayTokenLocked, displayDeviceInfoLocked, highBrightnessModeMetadata, isEnabledLocked, isInTransitionLocked, z);
            }
        }, this.mClock.uptimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$onDisplayChanged$2(DisplayDevice displayDevice, String str, DisplayDeviceConfig displayDeviceConfig, String str2, IBinder iBinder, DisplayDeviceInfo displayDeviceInfo, HighBrightnessModeMetadata highBrightnessModeMetadata, boolean z, boolean z2, boolean z3) {
        boolean z4;
        boolean z5 = true;
        if (this.mDisplayDevice != displayDevice) {
            this.mDisplayDevice = displayDevice;
            this.mUniqueDisplayId = str;
            this.mDisplayStatsId = str.hashCode();
            this.mDisplayDeviceConfig = displayDeviceConfig;
            this.mThermalBrightnessThrottlingDataId = str2;
            loadFromDisplayDeviceConfig(iBinder, displayDeviceInfo, highBrightnessModeMetadata);
            this.mDisplayPowerProximityStateController.notifyDisplayDeviceChanged(displayDeviceConfig);
            this.mPowerState.resetScreenState();
        } else if (!Objects.equals(this.mThermalBrightnessThrottlingDataId, str2)) {
            this.mThermalBrightnessThrottlingDataId = str2;
            this.mBrightnessThrottler.loadThermalBrightnessThrottlingDataFromDisplayDeviceConfig(displayDeviceConfig.getThermalBrightnessThrottlingDataMapByThrottlingId(), this.mThermalBrightnessThrottlingDataId, this.mUniqueDisplayId);
        } else {
            z4 = false;
            if (this.mIsEnabled == z || this.mIsInTransition != z2) {
                this.mIsEnabled = z;
                this.mIsInTransition = z2;
            } else {
                z5 = z4;
            }
            this.mIsDisplayInternal = z3;
            if (z5) {
                return;
            }
            lambda$new$0();
            return;
        }
        z4 = true;
        if (this.mIsEnabled == z) {
        }
        this.mIsEnabled = z;
        this.mIsInTransition = z2;
        this.mIsDisplayInternal = z3;
        if (z5) {
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void stop() {
        synchronized (this.mLock) {
            clearDisplayBrightnessFollowersLocked();
            this.mStopped = true;
            this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(7), this.mClock.uptimeMillis());
            DisplayWhiteBalanceController displayWhiteBalanceController = this.mDisplayWhiteBalanceController;
            if (displayWhiteBalanceController != null) {
                displayWhiteBalanceController.setEnabled(false);
            }
            AutomaticBrightnessController automaticBrightnessController = this.mAutomaticBrightnessController;
            if (automaticBrightnessController != null) {
                automaticBrightnessController.stop();
            }
            this.mDisplayBrightnessController.stop();
            this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
        }
    }

    private void loadFromDisplayDeviceConfig(IBinder iBinder, DisplayDeviceInfo displayDeviceInfo, HighBrightnessModeMetadata highBrightnessModeMetadata) {
        loadBrightnessRampRates();
        loadNitsRange(this.mContext.getResources());
        setUpAutoBrightness(this.mContext.getResources(), this.mHandler);
        reloadReduceBrightColours();
        RampAnimator.DualRampAnimator<DisplayPowerState> dualRampAnimator = this.mScreenBrightnessRampAnimator;
        if (dualRampAnimator != null) {
            dualRampAnimator.setAnimationTimeLimits(this.mBrightnessRampIncreaseMaxTimeMillis, this.mBrightnessRampDecreaseMaxTimeMillis);
        }
        this.mHbmController.setHighBrightnessModeMetadata(highBrightnessModeMetadata);
        this.mHbmController.resetHbmData(displayDeviceInfo.width, displayDeviceInfo.height, iBinder, displayDeviceInfo.uniqueId, this.mDisplayDeviceConfig.getHighBrightnessModeData(), new HighBrightnessModeController.HdrBrightnessDeviceConfig() { // from class: com.android.server.display.DisplayPowerController2.2
            @Override // com.android.server.display.HighBrightnessModeController.HdrBrightnessDeviceConfig
            public float getHdrBrightnessFromSdr(float f, float f2) {
                return DisplayPowerController2.this.mDisplayDeviceConfig.getHdrBrightnessFromSdr(f, f2);
            }
        });
        this.mBrightnessThrottler.loadThermalBrightnessThrottlingDataFromDisplayDeviceConfig(this.mDisplayDeviceConfig.getThermalBrightnessThrottlingDataMapByThrottlingId(), this.mThermalBrightnessThrottlingDataId, this.mUniqueDisplayId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUpdatePowerState() {
        synchronized (this.mLock) {
            sendUpdatePowerStateLocked();
        }
    }

    @GuardedBy({"mLock"})
    private void sendUpdatePowerStateLocked() {
        if (this.mStopped || this.mPendingUpdatePowerStateLocked) {
            return;
        }
        this.mPendingUpdatePowerStateLocked = true;
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(1), this.mClock.uptimeMillis());
    }

    private void initialize(int i) {
        DisplayPowerState displayPowerState = this.mInjector.getDisplayPowerState(this.mBlanker, this.mColorFadeEnabled ? new ColorFade(this.mDisplayId) : null, this.mDisplayId, i);
        this.mPowerState = displayPowerState;
        if (this.mColorFadeEnabled) {
            FloatProperty<DisplayPowerState> floatProperty = DisplayPowerState.COLOR_FADE_LEVEL;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(displayPowerState, floatProperty, SCREEN_ANIMATION_RATE_MINIMUM, 1.0f);
            this.mColorFadeOnAnimator = ofFloat;
            ofFloat.setDuration(250L);
            this.mColorFadeOnAnimator.addListener(this.mAnimatorListener);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mPowerState, floatProperty, 1.0f, SCREEN_ANIMATION_RATE_MINIMUM);
            this.mColorFadeOffAnimator = ofFloat2;
            ofFloat2.setDuration(200L);
            this.mColorFadeOffAnimator.addListener(this.mAnimatorListener);
        }
        RampAnimator.DualRampAnimator<DisplayPowerState> dualRampAnimator = this.mInjector.getDualRampAnimator(this.mPowerState, DisplayPowerState.SCREEN_BRIGHTNESS_FLOAT, DisplayPowerState.SCREEN_SDR_BRIGHTNESS_FLOAT);
        this.mScreenBrightnessRampAnimator = dualRampAnimator;
        dualRampAnimator.setAnimationTimeLimits(this.mBrightnessRampIncreaseMaxTimeMillis, this.mBrightnessRampDecreaseMaxTimeMillis);
        this.mScreenBrightnessRampAnimator.setListener(this.mRampAnimatorListener);
        noteScreenState(this.mPowerState.getScreenState());
        noteScreenBrightness(this.mPowerState.getScreenBrightness());
        float convertToAdjustedNits = this.mDisplayBrightnessController.convertToAdjustedNits(this.mPowerState.getScreenBrightness());
        BrightnessTracker brightnessTracker = this.mBrightnessTracker;
        if (brightnessTracker != null && convertToAdjustedNits >= SCREEN_ANIMATION_RATE_MINIMUM) {
            brightnessTracker.start(convertToAdjustedNits);
        }
        this.mDisplayBrightnessController.registerBrightnessSettingChangeListener(new BrightnessSetting.BrightnessSettingListener() { // from class: com.android.server.display.DisplayPowerController2$$ExternalSyntheticLambda4
            @Override // com.android.server.display.BrightnessSetting.BrightnessSettingListener
            public final void onBrightnessChanged(float f) {
                DisplayPowerController2.this.lambda$initialize$3(f);
            }
        });
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("screen_auto_brightness_adj"), false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("screen_brightness_mode"), false, this.mSettingsObserver, -1);
        handleBrightnessModeChange();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initialize$3(float f) {
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(8, Float.valueOf(f)), this.mClock.uptimeMillis());
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x01da  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setUpAutoBrightness(Resources resources, Handler handler) {
        float f;
        float f2;
        int i;
        float f3;
        ScreenOffBrightnessSensorController screenOffBrightnessSensorController;
        Sensor sensor;
        boolean isAutoBrightnessAvailable = this.mDisplayDeviceConfig.isAutoBrightnessAvailable();
        this.mUseSoftwareAutoBrightnessConfig = isAutoBrightnessAvailable;
        if (isAutoBrightnessAvailable) {
            BrightnessMappingStrategy brightnessMappingStrategy = this.mInteractiveModeBrightnessMapper;
            if (brightnessMappingStrategy != null) {
                float userLux = brightnessMappingStrategy.getUserLux();
                f = this.mInteractiveModeBrightnessMapper.convertToNits(this.mInteractiveModeBrightnessMapper.getUserBrightness());
                f2 = userLux;
            } else {
                f = -1.0f;
                f2 = -1.0f;
            }
            boolean z = resources.getBoolean(17891666);
            this.mInteractiveModeBrightnessMapper = this.mInjector.getInteractiveModeBrightnessMapper(resources, this.mDisplayDeviceConfig, this.mDisplayWhiteBalanceController);
            if (z) {
                this.mIdleModeBrightnessMapper = BrightnessMappingStrategy.createForIdleMode(resources, this.mDisplayDeviceConfig, this.mDisplayWhiteBalanceController);
            }
            if (this.mInteractiveModeBrightnessMapper != null) {
                float fraction = resources.getFraction(R.fraction.config_screenAutoBrightnessDozeScaleFactor, 1, 1);
                HysteresisLevels hysteresisLevels = this.mInjector.getHysteresisLevels(this.mDisplayDeviceConfig.getAmbientBrighteningPercentages(), this.mDisplayDeviceConfig.getAmbientDarkeningPercentages(), this.mDisplayDeviceConfig.getAmbientBrighteningLevels(), this.mDisplayDeviceConfig.getAmbientDarkeningLevels(), this.mDisplayDeviceConfig.getAmbientLuxDarkeningMinThreshold(), this.mDisplayDeviceConfig.getAmbientLuxBrighteningMinThreshold());
                HysteresisLevels hysteresisLevels2 = this.mInjector.getHysteresisLevels(this.mDisplayDeviceConfig.getScreenBrighteningPercentages(), this.mDisplayDeviceConfig.getScreenDarkeningPercentages(), this.mDisplayDeviceConfig.getScreenBrighteningLevels(), this.mDisplayDeviceConfig.getScreenDarkeningLevels(), this.mDisplayDeviceConfig.getScreenDarkeningMinThreshold(), this.mDisplayDeviceConfig.getScreenBrighteningMinThreshold(), true);
                HysteresisLevels hysteresisLevels3 = this.mInjector.getHysteresisLevels(this.mDisplayDeviceConfig.getAmbientBrighteningPercentagesIdle(), this.mDisplayDeviceConfig.getAmbientDarkeningPercentagesIdle(), this.mDisplayDeviceConfig.getAmbientBrighteningLevelsIdle(), this.mDisplayDeviceConfig.getAmbientDarkeningLevelsIdle(), this.mDisplayDeviceConfig.getAmbientLuxDarkeningMinThresholdIdle(), this.mDisplayDeviceConfig.getAmbientLuxBrighteningMinThresholdIdle());
                HysteresisLevels hysteresisLevels4 = this.mInjector.getHysteresisLevels(this.mDisplayDeviceConfig.getScreenBrighteningPercentagesIdle(), this.mDisplayDeviceConfig.getScreenDarkeningPercentagesIdle(), this.mDisplayDeviceConfig.getScreenBrighteningLevelsIdle(), this.mDisplayDeviceConfig.getScreenDarkeningLevelsIdle(), this.mDisplayDeviceConfig.getScreenDarkeningMinThresholdIdle(), this.mDisplayDeviceConfig.getScreenBrighteningMinThresholdIdle());
                long autoBrightnessBrighteningLightDebounce = this.mDisplayDeviceConfig.getAutoBrightnessBrighteningLightDebounce();
                long autoBrightnessDarkeningLightDebounce = this.mDisplayDeviceConfig.getAutoBrightnessDarkeningLightDebounce();
                boolean z2 = resources.getBoolean(R.bool.config_bluetooth_address_validation);
                int integer = resources.getInteger(R.integer.config_notificationsBatteryMediumARGB);
                int integer2 = resources.getInteger(R.integer.config_bluetooth_max_advertisers);
                int integer3 = resources.getInteger(R.integer.config_bluetooth_idle_cur_ma);
                if (integer3 == -1) {
                    i = integer2;
                } else {
                    if (integer3 > integer2) {
                        Slog.w(this.mTag, "Expected config_autoBrightnessInitialLightSensorRate (" + integer3 + ") to be less than or equal to config_autoBrightnessLightSensorRate (" + integer2 + ").");
                    }
                    i = integer3;
                }
                loadAmbientLightSensor();
                BrightnessTracker brightnessTracker = this.mBrightnessTracker;
                if (brightnessTracker != null && this.mDisplayId == 0) {
                    brightnessTracker.setLightSensor(this.mLightSensor);
                }
                AutomaticBrightnessController automaticBrightnessController = this.mAutomaticBrightnessController;
                if (automaticBrightnessController != null) {
                    automaticBrightnessController.stop();
                }
                if (f >= SCREEN_ANIMATION_RATE_MINIMUM) {
                    float convertToFloatScale = this.mInteractiveModeBrightnessMapper.convertToFloatScale(f);
                    if (convertToFloatScale != Float.NaN) {
                        f3 = convertToFloatScale;
                        AutomaticBrightnessController automaticBrightnessController2 = this.mInjector.getAutomaticBrightnessController(this, handler.getLooper(), this.mSensorManager, this.mLightSensor, this.mInteractiveModeBrightnessMapper, integer, SCREEN_ANIMATION_RATE_MINIMUM, 1.0f, fraction, integer2, i, autoBrightnessBrighteningLightDebounce, autoBrightnessDarkeningLightDebounce, z2, hysteresisLevels, hysteresisLevels2, hysteresisLevels3, hysteresisLevels4, this.mContext, this.mHbmController, this.mBrightnessThrottler, this.mIdleModeBrightnessMapper, this.mDisplayDeviceConfig.getAmbientHorizonShort(), this.mDisplayDeviceConfig.getAmbientHorizonLong(), f2, f3);
                        this.mAutomaticBrightnessController = automaticBrightnessController2;
                        this.mDisplayBrightnessController.setAutomaticBrightnessController(automaticBrightnessController2);
                        this.mAutomaticBrightnessStrategy.setAutomaticBrightnessController(this.mAutomaticBrightnessController);
                        this.mBrightnessEventRingBuffer = new RingBuffer<>(BrightnessEvent.class, 100);
                        screenOffBrightnessSensorController = this.mScreenOffBrightnessSensorController;
                        if (screenOffBrightnessSensorController != null) {
                            screenOffBrightnessSensorController.stop();
                            this.mScreenOffBrightnessSensorController = null;
                        }
                        loadScreenOffBrightnessSensor();
                        int[] screenOffBrightnessSensorValueToLux = this.mDisplayDeviceConfig.getScreenOffBrightnessSensorValueToLux();
                        sensor = this.mScreenOffBrightnessSensor;
                        if (sensor != null || screenOffBrightnessSensorValueToLux == null) {
                            return;
                        }
                        this.mScreenOffBrightnessSensorController = this.mInjector.getScreenOffBrightnessSensorController(this.mSensorManager, sensor, this.mHandler, new DisplayPowerController$$ExternalSyntheticLambda8(), screenOffBrightnessSensorValueToLux, this.mInteractiveModeBrightnessMapper);
                        return;
                    }
                }
                f3 = -1.0f;
                AutomaticBrightnessController automaticBrightnessController22 = this.mInjector.getAutomaticBrightnessController(this, handler.getLooper(), this.mSensorManager, this.mLightSensor, this.mInteractiveModeBrightnessMapper, integer, SCREEN_ANIMATION_RATE_MINIMUM, 1.0f, fraction, integer2, i, autoBrightnessBrighteningLightDebounce, autoBrightnessDarkeningLightDebounce, z2, hysteresisLevels, hysteresisLevels2, hysteresisLevels3, hysteresisLevels4, this.mContext, this.mHbmController, this.mBrightnessThrottler, this.mIdleModeBrightnessMapper, this.mDisplayDeviceConfig.getAmbientHorizonShort(), this.mDisplayDeviceConfig.getAmbientHorizonLong(), f2, f3);
                this.mAutomaticBrightnessController = automaticBrightnessController22;
                this.mDisplayBrightnessController.setAutomaticBrightnessController(automaticBrightnessController22);
                this.mAutomaticBrightnessStrategy.setAutomaticBrightnessController(this.mAutomaticBrightnessController);
                this.mBrightnessEventRingBuffer = new RingBuffer<>(BrightnessEvent.class, 100);
                screenOffBrightnessSensorController = this.mScreenOffBrightnessSensorController;
                if (screenOffBrightnessSensorController != null) {
                }
                loadScreenOffBrightnessSensor();
                int[] screenOffBrightnessSensorValueToLux2 = this.mDisplayDeviceConfig.getScreenOffBrightnessSensorValueToLux();
                sensor = this.mScreenOffBrightnessSensor;
                if (sensor != null) {
                    return;
                } else {
                    return;
                }
            }
            this.mUseSoftwareAutoBrightnessConfig = false;
        }
    }

    private void loadBrightnessRampRates() {
        this.mBrightnessRampRateFastDecrease = this.mDisplayDeviceConfig.getBrightnessRampFastDecrease();
        this.mBrightnessRampRateFastIncrease = this.mDisplayDeviceConfig.getBrightnessRampFastIncrease();
        this.mBrightnessRampRateSlowDecrease = this.mDisplayDeviceConfig.getBrightnessRampSlowDecrease();
        this.mBrightnessRampRateSlowIncrease = this.mDisplayDeviceConfig.getBrightnessRampSlowIncrease();
        this.mBrightnessRampDecreaseMaxTimeMillis = this.mDisplayDeviceConfig.getBrightnessRampDecreaseMaxMillis();
        this.mBrightnessRampIncreaseMaxTimeMillis = this.mDisplayDeviceConfig.getBrightnessRampIncreaseMaxMillis();
    }

    private void loadNitsRange(Resources resources) {
        DisplayDeviceConfig displayDeviceConfig = this.mDisplayDeviceConfig;
        if (displayDeviceConfig != null && displayDeviceConfig.getNits() != null) {
            this.mNitsRange = this.mDisplayDeviceConfig.getNits();
        } else {
            Slog.w(this.mTag, "Screen brightness nits configuration is unavailable; falling back");
            this.mNitsRange = BrightnessMappingStrategy.getFloatArray(resources.obtainTypedArray(R.array.vendor_required_apps_managed_user));
        }
    }

    private void reloadReduceBrightColours() {
        ColorDisplayService.ColorDisplayServiceInternal colorDisplayServiceInternal = this.mCdsi;
        if (colorDisplayServiceInternal == null || !colorDisplayServiceInternal.isReduceBrightColorsActivated()) {
            return;
        }
        applyReduceBrightColorsSplineAdjustment();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setAutomaticScreenBrightnessMode(boolean z) {
        AutomaticBrightnessController automaticBrightnessController = this.mAutomaticBrightnessController;
        if (automaticBrightnessController != null) {
            if (z) {
                automaticBrightnessController.switchToIdleMode();
            } else {
                automaticBrightnessController.switchToInteractiveScreenBrightnessMode();
            }
        }
        DisplayWhiteBalanceController displayWhiteBalanceController = this.mDisplayWhiteBalanceController;
        if (displayWhiteBalanceController != null) {
            displayWhiteBalanceController.setStrongModeEnabled(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupHandlerThreadAfterStop() {
        this.mDisplayPowerProximityStateController.cleanup();
        this.mHbmController.stop();
        this.mBrightnessThrottler.stop();
        this.mHandler.removeCallbacksAndMessages(null);
        this.mWakelockController.releaseAll();
        DisplayPowerState displayPowerState = this.mPowerState;
        reportStats(displayPowerState != null ? displayPowerState.getScreenBrightness() : SCREEN_ANIMATION_RATE_MINIMUM);
        DisplayPowerState displayPowerState2 = this.mPowerState;
        if (displayPowerState2 != null) {
            displayPowerState2.stop();
            this.mPowerState = null;
        }
        ScreenOffBrightnessSensorController screenOffBrightnessSensorController = this.mScreenOffBrightnessSensorController;
        if (screenOffBrightnessSensorController != null) {
            screenOffBrightnessSensorController.stop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updatePowerState, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        Trace.traceBegin(131072L, "DisplayPowerController#updatePowerState");
        updatePowerStateInternal();
        Trace.traceEnd(131072L);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0226  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0233 A[LOOP:0: B:105:0x022d->B:107:0x0233, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0247  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0253  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0285  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x02c5  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x02d5  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x03da A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:205:0x03e9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:212:0x048e  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x049c  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x04d4  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x04f1  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x04fe A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:229:0x0524  */
    /* JADX WARN: Removed duplicated region for block: B:232:0x0538  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x054a  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x054f  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0558  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x057b  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x059e  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x05ab  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x05c0  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x05c7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:282:0x05e0  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x05e8  */
    /* JADX WARN: Removed duplicated region for block: B:288:0x05f5  */
    /* JADX WARN: Removed duplicated region for block: B:290:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:291:0x05ea  */
    /* JADX WARN: Removed duplicated region for block: B:302:0x0527  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x04f3  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x04a1  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x0491  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x03cc  */
    /* JADX WARN: Removed duplicated region for block: B:323:0x02a6  */
    /* JADX WARN: Removed duplicated region for block: B:327:0x0275  */
    /* JADX WARN: Removed duplicated region for block: B:330:0x0228  */
    /* JADX WARN: Removed duplicated region for block: B:331:0x021b  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x01ec  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01d3  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01f8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updatePowerStateInternal() {
        int i;
        int i2;
        byte b;
        int i3;
        boolean z;
        boolean z2;
        float f;
        float f2;
        int i4;
        DisplayManagerInternal.DisplayPowerRequest displayPowerRequest;
        float f3;
        int i5;
        int i6;
        boolean z3;
        boolean saveBrightnessInfo;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        int i7;
        BrightnessEvent brightnessEvent;
        RingBuffer<BrightnessEvent> ringBuffer;
        int i8;
        boolean z9;
        float clampScreenBrightness;
        float hdrBrightnessValue;
        float f4;
        boolean z10;
        boolean z11;
        ScreenOffBrightnessSensorController screenOffBrightnessSensorController;
        this.mBrightnessReasonTemp.set(null);
        this.mTempBrightnessEvent.reset();
        synchronized (this.mLock) {
            if (this.mStopped) {
                return;
            }
            this.mPendingUpdatePowerStateLocked = false;
            DisplayManagerInternal.DisplayPowerRequest displayPowerRequest2 = this.mPendingRequestLocked;
            if (displayPowerRequest2 == null) {
                return;
            }
            DisplayManagerInternal.DisplayPowerRequest displayPowerRequest3 = this.mPowerRequest;
            if (displayPowerRequest3 == null) {
                this.mPowerRequest = new DisplayManagerInternal.DisplayPowerRequest(this.mPendingRequestLocked);
                this.mDisplayPowerProximityStateController.updatePendingProximityRequestsLocked();
                this.mPendingRequestChangedLocked = false;
                i2 = 3;
                b = true;
            } else {
                if (this.mPendingRequestChangedLocked) {
                    i = displayPowerRequest3.policy;
                    displayPowerRequest3.copyFrom(displayPowerRequest2);
                    this.mDisplayPowerProximityStateController.updatePendingProximityRequestsLocked();
                    this.mPendingRequestChangedLocked = false;
                    this.mDisplayReadyLocked = false;
                } else {
                    i = displayPowerRequest3.policy;
                }
                i2 = i;
                b = false;
            }
            byte b2 = !this.mDisplayReadyLocked;
            SparseArray<DisplayPowerControllerInterface> clone = this.mDisplayBrightnessFollowers.clone();
            int updateDisplayState = this.mDisplayStateController.updateDisplayState(this.mPowerRequest, this.mIsEnabled, this.mIsInTransition);
            ScreenOffBrightnessSensorController screenOffBrightnessSensorController2 = this.mScreenOffBrightnessSensorController;
            if (screenOffBrightnessSensorController2 != null) {
                screenOffBrightnessSensorController2.setLightSensorEnabled(this.mAutomaticBrightnessStrategy.shouldUseAutoBrightness() && this.mIsEnabled && (updateDisplayState == 1 || (updateDisplayState == 3 && !this.mDisplayBrightnessController.isAllowAutoBrightnessWhileDozingConfig())) && this.mLeadDisplayId == -1);
            }
            if (b != false) {
                initialize(readyToUpdateDisplayState() ? updateDisplayState : 0);
            }
            animateScreenStateChange(updateDisplayState, this.mDisplayStateController.shouldPerformScreenOffTransition());
            int screenState = this.mPowerState.getScreenState();
            boolean updateUserSetScreenBrightness = this.mDisplayBrightnessController.updateUserSetScreenBrightness();
            DisplayBrightnessState updateBrightness = this.mDisplayBrightnessController.updateBrightness(this.mPowerRequest, screenState);
            float brightness = updateBrightness.getBrightness();
            float brightness2 = updateBrightness.getBrightness();
            this.mBrightnessReasonTemp.set(updateBrightness.getBrightnessReason());
            boolean isShortTermModelActive = this.mAutomaticBrightnessStrategy.isShortTermModelActive();
            this.mAutomaticBrightnessStrategy.setAutoBrightnessState(screenState, this.mDisplayBrightnessController.isAllowAutoBrightnessWhileDozingConfig(), brightness, this.mBrightnessReasonTemp.getReason(), this.mPowerRequest.policy, this.mDisplayBrightnessController.getLastUserSetScreenBrightness(), updateUserSetScreenBrightness);
            boolean z12 = Float.isNaN(brightness) && (this.mAutomaticBrightnessStrategy.getAutoBrightnessAdjustmentChanged() || updateUserSetScreenBrightness);
            this.mHbmController.setAutoBrightnessEnabled(this.mAutomaticBrightnessStrategy.shouldUseAutoBrightness() ? 1 : 2);
            float currentBrightness = this.mDisplayBrightnessController.getCurrentBrightness();
            if (Float.isNaN(brightness)) {
                if (this.mAutomaticBrightnessStrategy.isAutoBrightnessEnabled()) {
                    brightness = this.mAutomaticBrightnessStrategy.getAutomaticScreenBrightness(this.mTempBrightnessEvent);
                    if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(brightness) || brightness == -1.0f) {
                        brightness2 = this.mAutomaticBrightnessController.getRawAutomaticScreenBrightness();
                        brightness = clampScreenBrightness(brightness);
                        z = this.mAutomaticBrightnessStrategy.hasAppliedAutoBrightness() && !this.mAutomaticBrightnessStrategy.getAutoBrightnessAdjustmentChanged();
                        i3 = this.mAutomaticBrightnessStrategy.getAutoBrightnessAdjustmentReasonsFlags();
                        z2 = currentBrightness != brightness;
                        this.mBrightnessReasonTemp.setReason(4);
                        ScreenOffBrightnessSensorController screenOffBrightnessSensorController3 = this.mScreenOffBrightnessSensorController;
                        if (screenOffBrightnessSensorController3 != null) {
                            screenOffBrightnessSensorController3.setLightSensorEnabled(false);
                        }
                        if (Float.isNaN(brightness) && Display.isDozeState(screenState)) {
                            brightness2 = this.mScreenBrightnessDozeConfig;
                            brightness = clampScreenBrightness(brightness2);
                            this.mBrightnessReasonTemp.setReason(3);
                        }
                        if (Float.isNaN(brightness) && this.mAutomaticBrightnessStrategy.isAutoBrightnessEnabled() && (screenOffBrightnessSensorController = this.mScreenOffBrightnessSensorController) != null) {
                            brightness = screenOffBrightnessSensorController.getAutomaticScreenBrightness();
                            if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(brightness)) {
                                brightness2 = brightness;
                            } else {
                                float clampScreenBrightness2 = clampScreenBrightness(brightness);
                                boolean z13 = this.mDisplayBrightnessController.getCurrentBrightness() != clampScreenBrightness2;
                                this.mBrightnessReasonTemp.setReason(9);
                                z2 = z13;
                                brightness2 = brightness;
                                brightness = clampScreenBrightness2;
                            }
                        }
                        if (Float.isNaN(brightness)) {
                            f = brightness;
                            currentBrightness = brightness2;
                        } else {
                            float clampScreenBrightness3 = clampScreenBrightness(currentBrightness);
                            if (clampScreenBrightness3 != currentBrightness) {
                                z2 = true;
                            }
                            this.mBrightnessReasonTemp.setReason(1);
                            f = clampScreenBrightness3;
                        }
                        if (!this.mBrightnessThrottler.isThrottled()) {
                            this.mTempBrightnessEvent.setThermalMax(this.mBrightnessThrottler.getBrightnessCap());
                            f2 = Math.min(f, this.mBrightnessThrottler.getBrightnessCap());
                            this.mBrightnessReasonTemp.addModifier(8);
                            if (!this.mAppliedThrottling) {
                                z = false;
                            }
                            this.mAppliedThrottling = true;
                        } else {
                            if (this.mAppliedThrottling) {
                                this.mAppliedThrottling = false;
                            }
                            f2 = f;
                        }
                        AutomaticBrightnessController automaticBrightnessController = this.mAutomaticBrightnessController;
                        float ambientLux = automaticBrightnessController != null ? SCREEN_ANIMATION_RATE_MINIMUM : automaticBrightnessController.getAmbientLux();
                        for (i4 = 0; i4 < clone.size(); i4++) {
                            clone.valueAt(i4).setBrightnessToFollow(currentBrightness, this.mDisplayBrightnessController.convertToNits(currentBrightness), ambientLux);
                        }
                        if (z2) {
                            this.mDisplayBrightnessController.updateScreenBrightnessSetting(f2);
                        }
                        if (this.mPowerRequest.policy != 2) {
                            if (f2 > SCREEN_ANIMATION_RATE_MINIMUM) {
                                f2 = Math.max(Math.min(f2 - this.mScreenBrightnessMinimumDimAmount, this.mScreenBrightnessDimConfig), SCREEN_ANIMATION_RATE_MINIMUM);
                                z11 = true;
                                this.mBrightnessReasonTemp.addModifier(1);
                            } else {
                                z11 = true;
                            }
                            if (!this.mAppliedDimming) {
                                z = false;
                            }
                            this.mAppliedDimming = z11;
                        } else if (this.mAppliedDimming) {
                            this.mAppliedDimming = false;
                            z = false;
                        }
                        displayPowerRequest = this.mPowerRequest;
                        if (!displayPowerRequest.lowPowerMode) {
                            if (f2 > SCREEN_ANIMATION_RATE_MINIMUM) {
                                f2 = Math.max(f2 * Math.min(displayPowerRequest.screenLowPowerBrightnessFactor, 1.0f), SCREEN_ANIMATION_RATE_MINIMUM);
                                this.mBrightnessReasonTemp.addModifier(2);
                            }
                            if (this.mAppliedLowPower) {
                                z10 = true;
                            } else {
                                z10 = true;
                                z = false;
                            }
                            this.mAppliedLowPower = z10;
                        } else if (this.mAppliedLowPower) {
                            this.mAppliedLowPower = false;
                            f3 = f2;
                            z = false;
                            this.mHbmController.onBrightnessChanged(f3, f, this.mBrightnessThrottler.getBrightnessMaxReason());
                            boolean z14 = this.mBrightnessReasonTemp.getReason() != 7 || this.mAutomaticBrightnessStrategy.isTemporaryAutoBrightnessAdjustmentApplied();
                            if (!this.mPendingScreenOff) {
                                if (this.mSkipScreenOnBrightnessRamp) {
                                    i8 = 2;
                                    if (screenState == 2) {
                                        int i9 = this.mSkipRampState;
                                        if (i9 == 0 && this.mDozing) {
                                            this.mInitialAutoBrightness = f3;
                                            this.mSkipRampState = 1;
                                        } else if (i9 == 1 && this.mUseSoftwareAutoBrightnessConfig && !BrightnessSynchronizer.floatEquals(f3, this.mInitialAutoBrightness)) {
                                            i8 = 2;
                                            this.mSkipRampState = 2;
                                        } else {
                                            i8 = 2;
                                            if (this.mSkipRampState == 2) {
                                                this.mSkipRampState = 0;
                                            }
                                        }
                                    } else {
                                        this.mSkipRampState = 0;
                                    }
                                    z9 = !(screenState == i8 || this.mSkipRampState == 0) || this.mDisplayPowerProximityStateController.shouldSkipRampBecauseOfProximityChangeToNegative();
                                    boolean z15 = !Display.isDozeState(screenState) && this.mBrightnessBucketsInDozeConfig;
                                    boolean z16 = !this.mColorFadeEnabled && this.mPowerState.getColorFadeLevel() == 1.0f;
                                    clampScreenBrightness = clampScreenBrightness(f3);
                                    hdrBrightnessValue = (this.mHbmController.getHighBrightnessMode() != 2 && (this.mBrightnessReasonTemp.getModifier() & 1) == 0 && (this.mBrightnessReasonTemp.getModifier() & 2) == 0) ? this.mHbmController.getHdrBrightnessValue() : clampScreenBrightness;
                                    float screenBrightness = this.mPowerState.getScreenBrightness();
                                    i6 = i2;
                                    float sdrScreenBrightness = this.mPowerState.getSdrScreenBrightness();
                                    if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(hdrBrightnessValue) && (hdrBrightnessValue != screenBrightness || clampScreenBrightness != sdrScreenBrightness)) {
                                        if (!z9 || z15 || !z16 || z14) {
                                            animateScreenBrightness(hdrBrightnessValue, clampScreenBrightness, SCREEN_ANIMATION_RATE_MINIMUM);
                                        } else {
                                            boolean z17 = hdrBrightnessValue > screenBrightness;
                                            if (z17 && z) {
                                                f4 = this.mBrightnessRampRateSlowIncrease;
                                            } else if (z17 && !z) {
                                                f4 = this.mBrightnessRampRateFastIncrease;
                                            } else if (!z17 && z) {
                                                f4 = this.mBrightnessRampRateSlowDecrease;
                                            } else {
                                                f4 = this.mBrightnessRampRateFastDecrease;
                                            }
                                            animateScreenBrightness(hdrBrightnessValue, clampScreenBrightness, f4);
                                        }
                                    }
                                    boolean z18 = z12;
                                    z3 = isShortTermModelActive;
                                    i5 = screenState;
                                    notifyBrightnessTrackerChanged(f3, z18, isShortTermModelActive, this.mAutomaticBrightnessStrategy.isAutoBrightnessEnabled(), z14);
                                    saveBrightnessInfo = saveBrightnessInfo(getScreenBrightnessSetting(), hdrBrightnessValue);
                                }
                                i8 = 2;
                                if (screenState == i8) {
                                }
                                if (Display.isDozeState(screenState)) {
                                }
                                if (this.mColorFadeEnabled) {
                                }
                                clampScreenBrightness = clampScreenBrightness(f3);
                                if (this.mHbmController.getHighBrightnessMode() != 2) {
                                }
                                float screenBrightness2 = this.mPowerState.getScreenBrightness();
                                i6 = i2;
                                float sdrScreenBrightness2 = this.mPowerState.getSdrScreenBrightness();
                                if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(hdrBrightnessValue)) {
                                    if (!z9) {
                                    }
                                    animateScreenBrightness(hdrBrightnessValue, clampScreenBrightness, SCREEN_ANIMATION_RATE_MINIMUM);
                                }
                                boolean z182 = z12;
                                z3 = isShortTermModelActive;
                                i5 = screenState;
                                notifyBrightnessTrackerChanged(f3, z182, isShortTermModelActive, this.mAutomaticBrightnessStrategy.isAutoBrightnessEnabled(), z14);
                                saveBrightnessInfo = saveBrightnessInfo(getScreenBrightnessSetting(), hdrBrightnessValue);
                            } else {
                                i5 = screenState;
                                i6 = i2;
                                z3 = isShortTermModelActive;
                                saveBrightnessInfo = saveBrightnessInfo(getScreenBrightnessSetting());
                            }
                            if (saveBrightnessInfo && !z14) {
                                lambda$new$1();
                            }
                            if (this.mBrightnessReasonTemp.equals(this.mBrightnessReason) || i3 != 0) {
                                Slog.v(this.mTag, "Brightness [" + f3 + "] reason changing to: '" + this.mBrightnessReasonTemp.toString(i3) + "', previous reason: '" + this.mBrightnessReason + "'.");
                                this.mBrightnessReason.set(this.mBrightnessReasonTemp);
                            } else if (this.mBrightnessReasonTemp.getReason() == 1 && updateUserSetScreenBrightness) {
                                Slog.v(this.mTag, "Brightness [" + f3 + "] manual adjustment.");
                            }
                            this.mTempBrightnessEvent.setTime(System.currentTimeMillis());
                            this.mTempBrightnessEvent.setBrightness(f3);
                            this.mTempBrightnessEvent.setPhysicalDisplayId(this.mUniqueDisplayId);
                            this.mTempBrightnessEvent.setReason(this.mBrightnessReason);
                            this.mTempBrightnessEvent.setHbmMax(this.mHbmController.getCurrentBrightnessMax());
                            this.mTempBrightnessEvent.setHbmMode(this.mHbmController.getHighBrightnessMode());
                            BrightnessEvent brightnessEvent2 = this.mTempBrightnessEvent;
                            brightnessEvent2.setFlags(brightnessEvent2.getFlags() | (this.mIsRbcActive ? 1 : 0) | (this.mPowerRequest.lowPowerMode ? 32 : 0));
                            BrightnessEvent brightnessEvent3 = this.mTempBrightnessEvent;
                            ColorDisplayService.ColorDisplayServiceInternal colorDisplayServiceInternal = this.mCdsi;
                            brightnessEvent3.setRbcStrength(colorDisplayServiceInternal != null ? colorDisplayServiceInternal.getReduceBrightColorsStrength() : -1);
                            this.mTempBrightnessEvent.setPowerFactor(this.mPowerRequest.screenLowPowerBrightnessFactor);
                            this.mTempBrightnessEvent.setWasShortTermModelActive(z3);
                            this.mTempBrightnessEvent.setDisplayBrightnessStrategyName(updateBrightness.getDisplayBrightnessStrategyName());
                            this.mTempBrightnessEvent.setAutomaticBrightnessEnabled(this.mAutomaticBrightnessStrategy.shouldUseAutoBrightness());
                            boolean z19 = this.mTempBrightnessEvent.getReason().getReason() != 7 && this.mLastBrightnessEvent.getReason().getReason() == 7;
                            z4 = this.mLastBrightnessEvent.isRbcEnabled() != this.mTempBrightnessEvent.isRbcEnabled();
                            if ((!this.mTempBrightnessEvent.equalsMainData(this.mLastBrightnessEvent) && !z19) || i3 != 0) {
                                this.mTempBrightnessEvent.setInitialBrightness(this.mLastBrightnessEvent.getBrightness());
                                this.mLastBrightnessEvent.copyFrom(this.mTempBrightnessEvent);
                                brightnessEvent = new BrightnessEvent(this.mTempBrightnessEvent);
                                brightnessEvent.setAdjustmentFlags(i3);
                                brightnessEvent.setFlags(brightnessEvent.getFlags() | (!updateUserSetScreenBrightness ? 8 : 0));
                                Slog.i(this.mTag, brightnessEvent.toString(false));
                                if (!updateUserSetScreenBrightness || brightnessEvent.getReason().getReason() != 7) {
                                    logBrightnessEvent(brightnessEvent, f);
                                }
                                ringBuffer = this.mBrightnessEventRingBuffer;
                                if (ringBuffer != null) {
                                    ringBuffer.append(brightnessEvent);
                                }
                                if (z4) {
                                    this.mRbcEventRingBuffer.append(brightnessEvent);
                                }
                            }
                            if (this.mDisplayWhiteBalanceController != null) {
                                if (i5 == 2 && this.mDisplayWhiteBalanceSettings.isEnabled()) {
                                    this.mDisplayWhiteBalanceController.setEnabled(true);
                                    this.mDisplayWhiteBalanceController.updateDisplayColorTemperature();
                                } else {
                                    z5 = false;
                                    this.mDisplayWhiteBalanceController.setEnabled(false);
                                    z6 = (this.mPendingScreenOnUnblocker == null || (this.mColorFadeEnabled && (this.mColorFadeOnAnimator.isStarted() || this.mColorFadeOffAnimator.isStarted())) || !this.mPowerState.waitUntilClean(this.mCleanListener)) ? z5 : true;
                                    z7 = (z6 || this.mScreenBrightnessRampAnimator.isAnimating()) ? z5 : true;
                                    if (z6 && i5 != 1 && this.mReportedScreenStateToPolicy == 1) {
                                        setReportedScreenState(2);
                                        this.mWindowManagerPolicy.screenTurnedOn(this.mDisplayId);
                                    }
                                    if (!z7) {
                                        this.mWakelockController.acquireWakelock(5);
                                    }
                                    if (z6 || !b2 == true) {
                                        z8 = true;
                                    } else {
                                        synchronized (this.mLock) {
                                            if (this.mPendingRequestChangedLocked) {
                                                z8 = true;
                                            } else {
                                                z8 = true;
                                                this.mDisplayReadyLocked = true;
                                            }
                                        }
                                        sendOnStateChangedWithWakelock();
                                    }
                                    if (z7) {
                                        this.mWakelockController.releaseWakelock(5);
                                    }
                                    this.mDozing = i5 != 2 ? z8 : z5;
                                    i7 = this.mPowerRequest.policy;
                                    if (i6 != i7) {
                                        logDisplayPolicyChanged(i7);
                                        return;
                                    }
                                    return;
                                }
                            }
                            z5 = false;
                            if (this.mPendingScreenOnUnblocker == null) {
                            }
                            if (z6) {
                            }
                            if (z6) {
                                setReportedScreenState(2);
                                this.mWindowManagerPolicy.screenTurnedOn(this.mDisplayId);
                            }
                            if (!z7) {
                            }
                            if (z6) {
                            }
                            z8 = true;
                            if (z7) {
                            }
                            this.mDozing = i5 != 2 ? z8 : z5;
                            i7 = this.mPowerRequest.policy;
                            if (i6 != i7) {
                            }
                        }
                        f3 = f2;
                        this.mHbmController.onBrightnessChanged(f3, f, this.mBrightnessThrottler.getBrightnessMaxReason());
                        if (this.mBrightnessReasonTemp.getReason() != 7) {
                        }
                        if (!this.mPendingScreenOff) {
                        }
                        if (saveBrightnessInfo) {
                            lambda$new$1();
                        }
                        if (this.mBrightnessReasonTemp.equals(this.mBrightnessReason)) {
                        }
                        Slog.v(this.mTag, "Brightness [" + f3 + "] reason changing to: '" + this.mBrightnessReasonTemp.toString(i3) + "', previous reason: '" + this.mBrightnessReason + "'.");
                        this.mBrightnessReason.set(this.mBrightnessReasonTemp);
                        this.mTempBrightnessEvent.setTime(System.currentTimeMillis());
                        this.mTempBrightnessEvent.setBrightness(f3);
                        this.mTempBrightnessEvent.setPhysicalDisplayId(this.mUniqueDisplayId);
                        this.mTempBrightnessEvent.setReason(this.mBrightnessReason);
                        this.mTempBrightnessEvent.setHbmMax(this.mHbmController.getCurrentBrightnessMax());
                        this.mTempBrightnessEvent.setHbmMode(this.mHbmController.getHighBrightnessMode());
                        BrightnessEvent brightnessEvent22 = this.mTempBrightnessEvent;
                        brightnessEvent22.setFlags(brightnessEvent22.getFlags() | (this.mIsRbcActive ? 1 : 0) | (this.mPowerRequest.lowPowerMode ? 32 : 0));
                        BrightnessEvent brightnessEvent32 = this.mTempBrightnessEvent;
                        ColorDisplayService.ColorDisplayServiceInternal colorDisplayServiceInternal2 = this.mCdsi;
                        brightnessEvent32.setRbcStrength(colorDisplayServiceInternal2 != null ? colorDisplayServiceInternal2.getReduceBrightColorsStrength() : -1);
                        this.mTempBrightnessEvent.setPowerFactor(this.mPowerRequest.screenLowPowerBrightnessFactor);
                        this.mTempBrightnessEvent.setWasShortTermModelActive(z3);
                        this.mTempBrightnessEvent.setDisplayBrightnessStrategyName(updateBrightness.getDisplayBrightnessStrategyName());
                        this.mTempBrightnessEvent.setAutomaticBrightnessEnabled(this.mAutomaticBrightnessStrategy.shouldUseAutoBrightness());
                        if (this.mTempBrightnessEvent.getReason().getReason() != 7) {
                        }
                        if (this.mLastBrightnessEvent.isRbcEnabled() != this.mTempBrightnessEvent.isRbcEnabled()) {
                        }
                        if (!this.mTempBrightnessEvent.equalsMainData(this.mLastBrightnessEvent)) {
                            this.mTempBrightnessEvent.setInitialBrightness(this.mLastBrightnessEvent.getBrightness());
                            this.mLastBrightnessEvent.copyFrom(this.mTempBrightnessEvent);
                            brightnessEvent = new BrightnessEvent(this.mTempBrightnessEvent);
                            brightnessEvent.setAdjustmentFlags(i3);
                            brightnessEvent.setFlags(brightnessEvent.getFlags() | (!updateUserSetScreenBrightness ? 8 : 0));
                            Slog.i(this.mTag, brightnessEvent.toString(false));
                            if (!updateUserSetScreenBrightness) {
                            }
                            logBrightnessEvent(brightnessEvent, f);
                            ringBuffer = this.mBrightnessEventRingBuffer;
                            if (ringBuffer != null) {
                            }
                            if (z4) {
                            }
                            if (this.mDisplayWhiteBalanceController != null) {
                            }
                            z5 = false;
                            if (this.mPendingScreenOnUnblocker == null) {
                            }
                            if (z6) {
                            }
                            if (z6) {
                            }
                            if (!z7) {
                            }
                            if (z6) {
                            }
                            z8 = true;
                            if (z7) {
                            }
                            this.mDozing = i5 != 2 ? z8 : z5;
                            i7 = this.mPowerRequest.policy;
                            if (i6 != i7) {
                            }
                        }
                        this.mTempBrightnessEvent.setInitialBrightness(this.mLastBrightnessEvent.getBrightness());
                        this.mLastBrightnessEvent.copyFrom(this.mTempBrightnessEvent);
                        brightnessEvent = new BrightnessEvent(this.mTempBrightnessEvent);
                        brightnessEvent.setAdjustmentFlags(i3);
                        brightnessEvent.setFlags(brightnessEvent.getFlags() | (!updateUserSetScreenBrightness ? 8 : 0));
                        Slog.i(this.mTag, brightnessEvent.toString(false));
                        if (!updateUserSetScreenBrightness) {
                        }
                        logBrightnessEvent(brightnessEvent, f);
                        ringBuffer = this.mBrightnessEventRingBuffer;
                        if (ringBuffer != null) {
                        }
                        if (z4) {
                        }
                        if (this.mDisplayWhiteBalanceController != null) {
                        }
                        z5 = false;
                        if (this.mPendingScreenOnUnblocker == null) {
                        }
                        if (z6) {
                        }
                        if (z6) {
                        }
                        if (!z7) {
                        }
                        if (z6) {
                        }
                        z8 = true;
                        if (z7) {
                        }
                        this.mDozing = i5 != 2 ? z8 : z5;
                        i7 = this.mPowerRequest.policy;
                        if (i6 != i7) {
                        }
                    }
                }
            } else {
                brightness = clampScreenBrightness(brightness);
            }
            i3 = 0;
            z = false;
            z2 = false;
            if (Float.isNaN(brightness)) {
                brightness2 = this.mScreenBrightnessDozeConfig;
                brightness = clampScreenBrightness(brightness2);
                this.mBrightnessReasonTemp.setReason(3);
            }
            if (Float.isNaN(brightness)) {
                brightness = screenOffBrightnessSensorController.getAutomaticScreenBrightness();
                if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(brightness)) {
                }
            }
            if (Float.isNaN(brightness)) {
            }
            if (!this.mBrightnessThrottler.isThrottled()) {
            }
            AutomaticBrightnessController automaticBrightnessController2 = this.mAutomaticBrightnessController;
            if (automaticBrightnessController2 != null) {
            }
            while (i4 < clone.size()) {
            }
            if (z2) {
            }
            if (this.mPowerRequest.policy != 2) {
            }
            displayPowerRequest = this.mPowerRequest;
            if (!displayPowerRequest.lowPowerMode) {
            }
            f3 = f2;
            this.mHbmController.onBrightnessChanged(f3, f, this.mBrightnessThrottler.getBrightnessMaxReason());
            if (this.mBrightnessReasonTemp.getReason() != 7) {
            }
            if (!this.mPendingScreenOff) {
            }
            if (saveBrightnessInfo) {
            }
            if (this.mBrightnessReasonTemp.equals(this.mBrightnessReason)) {
            }
            Slog.v(this.mTag, "Brightness [" + f3 + "] reason changing to: '" + this.mBrightnessReasonTemp.toString(i3) + "', previous reason: '" + this.mBrightnessReason + "'.");
            this.mBrightnessReason.set(this.mBrightnessReasonTemp);
            this.mTempBrightnessEvent.setTime(System.currentTimeMillis());
            this.mTempBrightnessEvent.setBrightness(f3);
            this.mTempBrightnessEvent.setPhysicalDisplayId(this.mUniqueDisplayId);
            this.mTempBrightnessEvent.setReason(this.mBrightnessReason);
            this.mTempBrightnessEvent.setHbmMax(this.mHbmController.getCurrentBrightnessMax());
            this.mTempBrightnessEvent.setHbmMode(this.mHbmController.getHighBrightnessMode());
            BrightnessEvent brightnessEvent222 = this.mTempBrightnessEvent;
            brightnessEvent222.setFlags(brightnessEvent222.getFlags() | (this.mIsRbcActive ? 1 : 0) | (this.mPowerRequest.lowPowerMode ? 32 : 0));
            BrightnessEvent brightnessEvent322 = this.mTempBrightnessEvent;
            ColorDisplayService.ColorDisplayServiceInternal colorDisplayServiceInternal22 = this.mCdsi;
            brightnessEvent322.setRbcStrength(colorDisplayServiceInternal22 != null ? colorDisplayServiceInternal22.getReduceBrightColorsStrength() : -1);
            this.mTempBrightnessEvent.setPowerFactor(this.mPowerRequest.screenLowPowerBrightnessFactor);
            this.mTempBrightnessEvent.setWasShortTermModelActive(z3);
            this.mTempBrightnessEvent.setDisplayBrightnessStrategyName(updateBrightness.getDisplayBrightnessStrategyName());
            this.mTempBrightnessEvent.setAutomaticBrightnessEnabled(this.mAutomaticBrightnessStrategy.shouldUseAutoBrightness());
            if (this.mTempBrightnessEvent.getReason().getReason() != 7) {
            }
            if (this.mLastBrightnessEvent.isRbcEnabled() != this.mTempBrightnessEvent.isRbcEnabled()) {
            }
            if (!this.mTempBrightnessEvent.equalsMainData(this.mLastBrightnessEvent)) {
            }
            this.mTempBrightnessEvent.setInitialBrightness(this.mLastBrightnessEvent.getBrightness());
            this.mLastBrightnessEvent.copyFrom(this.mTempBrightnessEvent);
            brightnessEvent = new BrightnessEvent(this.mTempBrightnessEvent);
            brightnessEvent.setAdjustmentFlags(i3);
            brightnessEvent.setFlags(brightnessEvent.getFlags() | (!updateUserSetScreenBrightness ? 8 : 0));
            Slog.i(this.mTag, brightnessEvent.toString(false));
            if (!updateUserSetScreenBrightness) {
            }
            logBrightnessEvent(brightnessEvent, f);
            ringBuffer = this.mBrightnessEventRingBuffer;
            if (ringBuffer != null) {
            }
            if (z4) {
            }
            if (this.mDisplayWhiteBalanceController != null) {
            }
            z5 = false;
            if (this.mPendingScreenOnUnblocker == null) {
            }
            if (z6) {
            }
            if (z6) {
            }
            if (!z7) {
            }
            if (z6) {
            }
            z8 = true;
            if (z7) {
            }
            this.mDozing = i5 != 2 ? z8 : z5;
            i7 = this.mPowerRequest.policy;
            if (i6 != i7) {
            }
        }
    }

    @Override // com.android.server.display.AutomaticBrightnessController.Callbacks
    public void updateBrightness() {
        sendUpdatePowerState();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void ignoreProximitySensorUntilChanged() {
        this.mDisplayPowerProximityStateController.ignoreProximitySensorUntilChanged();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setBrightnessConfiguration(BrightnessConfiguration brightnessConfiguration, boolean z) {
        this.mHandler.obtainMessage(4, z ? 1 : 0, 0, brightnessConfiguration).sendToTarget();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setTemporaryBrightness(float f) {
        this.mHandler.obtainMessage(5, Float.floatToIntBits(f), 0).sendToTarget();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setTemporaryAutoBrightnessAdjustment(float f) {
        this.mHandler.obtainMessage(6, Float.floatToIntBits(f), 0).sendToTarget();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public BrightnessInfo getBrightnessInfo() {
        BrightnessInfo brightnessInfo;
        synchronized (this.mCachedBrightnessInfo) {
            CachedBrightnessInfo cachedBrightnessInfo = this.mCachedBrightnessInfo;
            brightnessInfo = new BrightnessInfo(cachedBrightnessInfo.brightness.value, cachedBrightnessInfo.adjustedBrightness.value, cachedBrightnessInfo.brightnessMin.value, cachedBrightnessInfo.brightnessMax.value, cachedBrightnessInfo.hbmMode.value, cachedBrightnessInfo.hbmTransitionPoint.value, cachedBrightnessInfo.brightnessMaxReason.value);
        }
        return brightnessInfo;
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void onBootCompleted() {
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(13), this.mClock.uptimeMillis());
    }

    private boolean saveBrightnessInfo(float f) {
        return saveBrightnessInfo(f, f);
    }

    private boolean saveBrightnessInfo(float f, float f2) {
        boolean checkAndSetInt;
        synchronized (this.mCachedBrightnessInfo) {
            float min = Math.min(this.mHbmController.getCurrentBrightnessMin(), this.mBrightnessThrottler.getBrightnessCap());
            float min2 = Math.min(this.mHbmController.getCurrentBrightnessMax(), this.mBrightnessThrottler.getBrightnessCap());
            CachedBrightnessInfo cachedBrightnessInfo = this.mCachedBrightnessInfo;
            boolean checkAndSetFloat = cachedBrightnessInfo.checkAndSetFloat(cachedBrightnessInfo.brightness, f) | false;
            CachedBrightnessInfo cachedBrightnessInfo2 = this.mCachedBrightnessInfo;
            boolean checkAndSetFloat2 = checkAndSetFloat | cachedBrightnessInfo2.checkAndSetFloat(cachedBrightnessInfo2.adjustedBrightness, f2);
            CachedBrightnessInfo cachedBrightnessInfo3 = this.mCachedBrightnessInfo;
            boolean checkAndSetFloat3 = checkAndSetFloat2 | cachedBrightnessInfo3.checkAndSetFloat(cachedBrightnessInfo3.brightnessMin, min);
            CachedBrightnessInfo cachedBrightnessInfo4 = this.mCachedBrightnessInfo;
            boolean checkAndSetFloat4 = checkAndSetFloat3 | cachedBrightnessInfo4.checkAndSetFloat(cachedBrightnessInfo4.brightnessMax, min2);
            CachedBrightnessInfo cachedBrightnessInfo5 = this.mCachedBrightnessInfo;
            boolean checkAndSetInt2 = checkAndSetFloat4 | cachedBrightnessInfo5.checkAndSetInt(cachedBrightnessInfo5.hbmMode, this.mHbmController.getHighBrightnessMode());
            CachedBrightnessInfo cachedBrightnessInfo6 = this.mCachedBrightnessInfo;
            boolean checkAndSetFloat5 = checkAndSetInt2 | cachedBrightnessInfo6.checkAndSetFloat(cachedBrightnessInfo6.hbmTransitionPoint, this.mHbmController.getTransitionPoint());
            CachedBrightnessInfo cachedBrightnessInfo7 = this.mCachedBrightnessInfo;
            checkAndSetInt = cachedBrightnessInfo7.checkAndSetInt(cachedBrightnessInfo7.brightnessMaxReason, this.mBrightnessThrottler.getBrightnessMaxReason()) | checkAndSetFloat5;
        }
        return checkAndSetInt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: postBrightnessChangeRunnable, reason: merged with bridge method [inline-methods] */
    public void lambda$new$1() {
        this.mHandler.post(this.mOnBrightnessChangeRunnable);
    }

    private HighBrightnessModeController createHbmControllerLocked() {
        DisplayDevice primaryDisplayDeviceLocked = this.mLogicalDisplay.getPrimaryDisplayDeviceLocked();
        DisplayDeviceConfig displayDeviceConfig = primaryDisplayDeviceLocked.getDisplayDeviceConfig();
        IBinder displayTokenLocked = this.mLogicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayTokenLocked();
        String uniqueId = this.mLogicalDisplay.getPrimaryDisplayDeviceLocked().getUniqueId();
        DisplayDeviceConfig.HighBrightnessModeData highBrightnessModeData = displayDeviceConfig != null ? displayDeviceConfig.getHighBrightnessModeData() : null;
        DisplayDeviceInfo displayDeviceInfoLocked = primaryDisplayDeviceLocked.getDisplayDeviceInfoLocked();
        return new HighBrightnessModeController(this.mHandler, displayDeviceInfoLocked.width, displayDeviceInfoLocked.height, displayTokenLocked, uniqueId, SCREEN_ANIMATION_RATE_MINIMUM, 1.0f, highBrightnessModeData, new HighBrightnessModeController.HdrBrightnessDeviceConfig() { // from class: com.android.server.display.DisplayPowerController2.5
            @Override // com.android.server.display.HighBrightnessModeController.HdrBrightnessDeviceConfig
            public float getHdrBrightnessFromSdr(float f, float f2) {
                return DisplayPowerController2.this.mDisplayDeviceConfig.getHdrBrightnessFromSdr(f, f2);
            }
        }, new Runnable() { // from class: com.android.server.display.DisplayPowerController2$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPowerController2.this.lambda$createHbmControllerLocked$4();
            }
        }, this.mHighBrightnessModeMetadata, this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createHbmControllerLocked$4() {
        sendUpdatePowerState();
        lambda$new$1();
        AutomaticBrightnessController automaticBrightnessController = this.mAutomaticBrightnessController;
        if (automaticBrightnessController != null) {
            automaticBrightnessController.update();
        }
    }

    private BrightnessThrottler createBrightnessThrottlerLocked() {
        return new BrightnessThrottler(this.mHandler, new Runnable() { // from class: com.android.server.display.DisplayPowerController2$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPowerController2.this.lambda$createBrightnessThrottlerLocked$5();
            }
        }, this.mUniqueDisplayId, this.mLogicalDisplay.getDisplayInfoLocked().thermalBrightnessThrottlingDataId, this.mLogicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayDeviceConfig().getThermalBrightnessThrottlingDataMapByThrottlingId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createBrightnessThrottlerLocked$5() {
        sendUpdatePowerState();
        lambda$new$1();
    }

    private void blockScreenOn() {
        if (this.mPendingScreenOnUnblocker == null) {
            Trace.asyncTraceBegin(131072L, SCREEN_ON_BLOCKED_TRACE_NAME, 0);
            this.mPendingScreenOnUnblocker = new ScreenOnUnblocker();
            this.mScreenOnBlockStartRealTime = SystemClock.elapsedRealtime();
            Slog.i(this.mTag, "Blocking screen on until initial contents have been drawn.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unblockScreenOn() {
        if (this.mPendingScreenOnUnblocker != null) {
            this.mPendingScreenOnUnblocker = null;
            long elapsedRealtime = SystemClock.elapsedRealtime() - this.mScreenOnBlockStartRealTime;
            Slog.i(this.mTag, "Unblocked screen on after " + elapsedRealtime + " ms");
            Trace.asyncTraceEnd(131072L, SCREEN_ON_BLOCKED_TRACE_NAME, 0);
        }
    }

    private void blockScreenOff() {
        if (this.mPendingScreenOffUnblocker == null) {
            Trace.asyncTraceBegin(131072L, SCREEN_OFF_BLOCKED_TRACE_NAME, 0);
            this.mPendingScreenOffUnblocker = new ScreenOffUnblocker();
            this.mScreenOffBlockStartRealTime = SystemClock.elapsedRealtime();
            Slog.i(this.mTag, "Blocking screen off");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unblockScreenOff() {
        if (this.mPendingScreenOffUnblocker != null) {
            this.mPendingScreenOffUnblocker = null;
            long elapsedRealtime = SystemClock.elapsedRealtime() - this.mScreenOffBlockStartRealTime;
            Slog.i(this.mTag, "Unblocked screen off after " + elapsedRealtime + " ms");
            Trace.asyncTraceEnd(131072L, SCREEN_OFF_BLOCKED_TRACE_NAME, 0);
        }
    }

    private boolean setScreenState(int i) {
        return setScreenState(i, false);
    }

    private boolean setScreenState(int i, boolean z) {
        int i2;
        boolean z2 = i == 1;
        if (this.mPowerState.getScreenState() != i || this.mReportedScreenStateToPolicy == -1) {
            if (z2 && !this.mDisplayPowerProximityStateController.isScreenOffBecauseOfProximity()) {
                int i3 = this.mReportedScreenStateToPolicy;
                if (i3 == 2 || i3 == -1) {
                    setReportedScreenState(3);
                    blockScreenOff();
                    this.mWindowManagerPolicy.screenTurningOff(this.mDisplayId, this.mPendingScreenOffUnblocker);
                    unblockScreenOff();
                } else if (this.mPendingScreenOffUnblocker != null) {
                    return false;
                }
            }
            if (!z && this.mPowerState.getScreenState() != i && readyToUpdateDisplayState()) {
                Trace.traceCounter(131072L, "ScreenState", i);
                SystemProperties.set("debug.tracing.screen_state", String.valueOf(i));
                this.mPowerState.setScreenState(i);
                noteScreenState(i);
            }
        }
        if (z2 && this.mReportedScreenStateToPolicy != 0 && !this.mDisplayPowerProximityStateController.isScreenOffBecauseOfProximity()) {
            setReportedScreenState(0);
            unblockScreenOn();
            this.mWindowManagerPolicy.screenTurnedOff(this.mDisplayId, this.mIsInTransition);
        } else if (!z2 && this.mReportedScreenStateToPolicy == 3) {
            unblockScreenOff();
            this.mWindowManagerPolicy.screenTurnedOff(this.mDisplayId, this.mIsInTransition);
            setReportedScreenState(0);
        }
        if (!z2 && ((i2 = this.mReportedScreenStateToPolicy) == 0 || i2 == -1)) {
            setReportedScreenState(1);
            if (this.mPowerState.getColorFadeLevel() == SCREEN_ANIMATION_RATE_MINIMUM) {
                blockScreenOn();
            } else {
                unblockScreenOn();
            }
            this.mWindowManagerPolicy.screenTurningOn(this.mDisplayId, this.mPendingScreenOnUnblocker);
        }
        return this.mPendingScreenOnUnblocker == null;
    }

    private void setReportedScreenState(int i) {
        Trace.traceCounter(131072L, "ReportedScreenStateToPolicy", i);
        this.mReportedScreenStateToPolicy = i;
    }

    private void loadAmbientLightSensor() {
        this.mLightSensor = SensorUtils.findSensor(this.mSensorManager, this.mDisplayDeviceConfig.getAmbientLightSensor(), this.mDisplayId == 0 ? 5 : 0);
    }

    private void loadScreenOffBrightnessSensor() {
        this.mScreenOffBrightnessSensor = SensorUtils.findSensor(this.mSensorManager, this.mDisplayDeviceConfig.getScreenOffBrightnessSensor(), 0);
    }

    private float clampScreenBrightness(float f) {
        if (Float.isNaN(f)) {
            f = SCREEN_ANIMATION_RATE_MINIMUM;
        }
        return MathUtils.constrain(f, this.mHbmController.getCurrentBrightnessMin(), this.mHbmController.getCurrentBrightnessMax());
    }

    private void animateScreenBrightness(float f, float f2, float f3) {
        if (this.mScreenBrightnessRampAnimator.animateTo(f, f2, f3)) {
            Trace.traceCounter(131072L, "TargetScreenBrightness", (int) f);
            SystemProperties.set("debug.tracing.screen_brightness", String.valueOf(f));
            noteScreenBrightness(f);
        }
    }

    private void animateScreenStateChange(int i, boolean z) {
        if (this.mColorFadeEnabled && (this.mColorFadeOnAnimator.isStarted() || this.mColorFadeOffAnimator.isStarted())) {
            if (i != 2) {
                return;
            } else {
                this.mPendingScreenOff = false;
            }
        }
        if (this.mDisplayBlanksAfterDozeConfig && Display.isDozeState(this.mPowerState.getScreenState()) && !Display.isDozeState(i)) {
            this.mPowerState.prepareColorFade(this.mContext, this.mColorFadeFadesConfig ? 2 : 0);
            ObjectAnimator objectAnimator = this.mColorFadeOffAnimator;
            if (objectAnimator != null) {
                objectAnimator.end();
            }
            setScreenState(1, i != 1);
        }
        if (this.mPendingScreenOff && i != 1) {
            setScreenState(1);
            this.mPendingScreenOff = false;
            this.mPowerState.dismissColorFadeResources();
        }
        if (i == 2) {
            if (setScreenState(2)) {
                this.mPowerState.setColorFadeLevel(1.0f);
                this.mPowerState.dismissColorFade();
                return;
            }
            return;
        }
        if (i == 3) {
            if (!(this.mScreenBrightnessRampAnimator.isAnimating() && this.mPowerState.getScreenState() == 2) && setScreenState(3)) {
                this.mPowerState.setColorFadeLevel(1.0f);
                this.mPowerState.dismissColorFade();
                return;
            }
            return;
        }
        if (i == 4) {
            if (!this.mScreenBrightnessRampAnimator.isAnimating() || this.mPowerState.getScreenState() == 4) {
                if (this.mPowerState.getScreenState() != 4) {
                    if (!setScreenState(3)) {
                        return;
                    } else {
                        setScreenState(4);
                    }
                }
                this.mPowerState.setColorFadeLevel(1.0f);
                this.mPowerState.dismissColorFade();
                return;
            }
            return;
        }
        if (i == 6) {
            if (!this.mScreenBrightnessRampAnimator.isAnimating() || this.mPowerState.getScreenState() == 6) {
                if (this.mPowerState.getScreenState() != 6) {
                    if (!setScreenState(2)) {
                        return;
                    } else {
                        setScreenState(6);
                    }
                }
                this.mPowerState.setColorFadeLevel(1.0f);
                this.mPowerState.dismissColorFade();
                return;
            }
            return;
        }
        this.mPendingScreenOff = true;
        if (!this.mColorFadeEnabled) {
            this.mPowerState.setColorFadeLevel(SCREEN_ANIMATION_RATE_MINIMUM);
        }
        if (this.mPowerState.getColorFadeLevel() == SCREEN_ANIMATION_RATE_MINIMUM) {
            setScreenState(1);
            this.mPendingScreenOff = false;
            this.mPowerState.dismissColorFadeResources();
        } else {
            if (z) {
                if (this.mPowerState.prepareColorFade(this.mContext, this.mColorFadeFadesConfig ? 2 : 1) && this.mPowerState.getScreenState() != 1) {
                    this.mColorFadeOffAnimator.start();
                    return;
                }
            }
            this.mColorFadeOffAnimator.end();
        }
    }

    private void sendOnStateChangedWithWakelock() {
        if (this.mWakelockController.acquireWakelock(4)) {
            this.mHandler.post(this.mWakelockController.getOnStateChangedRunnable());
        }
    }

    private void logDisplayPolicyChanged(int i) {
        LogMaker logMaker = new LogMaker(1696);
        logMaker.setType(6);
        logMaker.setSubtype(i);
        MetricsLogger.action(logMaker);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSettingsChange(boolean z) {
        DisplayBrightnessController displayBrightnessController = this.mDisplayBrightnessController;
        displayBrightnessController.setPendingScreenBrightness(displayBrightnessController.getScreenBrightnessSetting());
        this.mAutomaticBrightnessStrategy.updatePendingAutoBrightnessAdjustments(z);
        if (z) {
            DisplayBrightnessController displayBrightnessController2 = this.mDisplayBrightnessController;
            displayBrightnessController2.setAndNotifyCurrentScreenBrightness(displayBrightnessController2.getPendingScreenBrightness());
            AutomaticBrightnessController automaticBrightnessController = this.mAutomaticBrightnessController;
            if (automaticBrightnessController != null) {
                automaticBrightnessController.resetShortTermModel();
            }
        }
        sendUpdatePowerState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBrightnessModeChange() {
        final int intForUser = Settings.System.getIntForUser(this.mContext.getContentResolver(), "screen_brightness_mode", 0, -2);
        this.mHandler.postAtTime(new Runnable() { // from class: com.android.server.display.DisplayPowerController2$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPowerController2.this.lambda$handleBrightnessModeChange$6(intForUser);
            }
        }, this.mClock.uptimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleBrightnessModeChange$6(int i) {
        this.mAutomaticBrightnessStrategy.setUseAutoBrightness(i == 1);
        lambda$new$0();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public float getScreenBrightnessSetting() {
        return this.mDisplayBrightnessController.getScreenBrightnessSetting();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setBrightness(float f) {
        this.mDisplayBrightnessController.setBrightness(f);
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public int getDisplayId() {
        return this.mDisplayId;
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public int getLeadDisplayId() {
        return this.mLeadDisplayId;
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setBrightnessToFollow(float f, float f2, float f3) {
        this.mHbmController.onAmbientLuxChange(f3);
        if (f2 < SCREEN_ANIMATION_RATE_MINIMUM) {
            this.mDisplayBrightnessController.setBrightnessToFollow(Float.valueOf(f));
        } else {
            float convertToFloatScale = this.mDisplayBrightnessController.convertToFloatScale(f2);
            if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(convertToFloatScale)) {
                this.mDisplayBrightnessController.setBrightnessToFollow(Float.valueOf(convertToFloatScale));
            } else {
                this.mDisplayBrightnessController.setBrightnessToFollow(Float.valueOf(f));
            }
        }
        sendUpdatePowerState();
    }

    private void notifyBrightnessTrackerChanged(float f, boolean z, boolean z2, boolean z3, boolean z4) {
        AutomaticBrightnessController automaticBrightnessController;
        AutomaticBrightnessController automaticBrightnessController2;
        float convertToAdjustedNits = this.mDisplayBrightnessController.convertToAdjustedNits(f);
        if (z4 || (automaticBrightnessController = this.mAutomaticBrightnessController) == null || automaticBrightnessController.isInIdleMode() || !z3 || this.mBrightnessTracker == null || !this.mAutomaticBrightnessStrategy.shouldUseAutoBrightness() || convertToAdjustedNits < SCREEN_ANIMATION_RATE_MINIMUM) {
            return;
        }
        if (z && ((automaticBrightnessController2 = this.mAutomaticBrightnessController) == null || !automaticBrightnessController2.hasValidAmbientLux())) {
            z = false;
        }
        boolean z5 = z;
        DisplayManagerInternal.DisplayPowerRequest displayPowerRequest = this.mPowerRequest;
        this.mBrightnessTracker.notifyBrightnessChanged(convertToAdjustedNits, z5, displayPowerRequest.lowPowerMode ? displayPowerRequest.screenLowPowerBrightnessFactor : 1.0f, z2, this.mAutomaticBrightnessController.isDefaultConfig(), this.mUniqueDisplayId, this.mAutomaticBrightnessController.getLastSensorValues(), this.mAutomaticBrightnessController.getLastSensorTimestamps());
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void addDisplayBrightnessFollower(DisplayPowerControllerInterface displayPowerControllerInterface) {
        synchronized (this.mLock) {
            this.mDisplayBrightnessFollowers.append(displayPowerControllerInterface.getDisplayId(), displayPowerControllerInterface);
            sendUpdatePowerStateLocked();
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void removeDisplayBrightnessFollower(final DisplayPowerControllerInterface displayPowerControllerInterface) {
        synchronized (this.mLock) {
            this.mDisplayBrightnessFollowers.remove(displayPowerControllerInterface.getDisplayId());
            this.mHandler.postAtTime(new Runnable() { // from class: com.android.server.display.DisplayPowerController2$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayPowerControllerInterface.this.setBrightnessToFollow(Float.NaN, -1.0f, DisplayPowerController2.SCREEN_ANIMATION_RATE_MINIMUM);
                }
            }, this.mClock.uptimeMillis());
        }
    }

    @GuardedBy({"mLock"})
    private void clearDisplayBrightnessFollowersLocked() {
        for (int i = 0; i < this.mDisplayBrightnessFollowers.size(); i++) {
            final DisplayPowerControllerInterface valueAt = this.mDisplayBrightnessFollowers.valueAt(i);
            this.mHandler.postAtTime(new Runnable() { // from class: com.android.server.display.DisplayPowerController2$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayPowerControllerInterface.this.setBrightnessToFollow(Float.NaN, -1.0f, DisplayPowerController2.SCREEN_ANIMATION_RATE_MINIMUM);
                }
            }, this.mClock.uptimeMillis());
        }
        this.mDisplayBrightnessFollowers.clear();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void dump(final PrintWriter printWriter) {
        synchronized (this.mLock) {
            printWriter.println();
            printWriter.println("Display Power Controller:");
            printWriter.println("  mDisplayId=" + this.mDisplayId);
            printWriter.println("  mLeadDisplayId=" + this.mLeadDisplayId);
            printWriter.println("  mLightSensor=" + this.mLightSensor);
            printWriter.println("  mDisplayBrightnessFollowers=" + this.mDisplayBrightnessFollowers);
            printWriter.println();
            printWriter.println("Display Power Controller Locked State:");
            printWriter.println("  mDisplayReadyLocked=" + this.mDisplayReadyLocked);
            printWriter.println("  mPendingRequestLocked=" + this.mPendingRequestLocked);
            printWriter.println("  mPendingRequestChangedLocked=" + this.mPendingRequestChangedLocked);
            printWriter.println("  mPendingUpdatePowerStateLocked=" + this.mPendingUpdatePowerStateLocked);
        }
        printWriter.println();
        printWriter.println("Display Power Controller Configuration:");
        printWriter.println("  mScreenBrightnessDozeConfig=" + this.mScreenBrightnessDozeConfig);
        printWriter.println("  mScreenBrightnessDimConfig=" + this.mScreenBrightnessDimConfig);
        printWriter.println("  mUseSoftwareAutoBrightnessConfig=" + this.mUseSoftwareAutoBrightnessConfig);
        printWriter.println("  mSkipScreenOnBrightnessRamp=" + this.mSkipScreenOnBrightnessRamp);
        printWriter.println("  mColorFadeFadesConfig=" + this.mColorFadeFadesConfig);
        printWriter.println("  mColorFadeEnabled=" + this.mColorFadeEnabled);
        printWriter.println("  mIsDisplayInternal=" + this.mIsDisplayInternal);
        synchronized (this.mCachedBrightnessInfo) {
            printWriter.println("  mCachedBrightnessInfo.brightness=" + this.mCachedBrightnessInfo.brightness.value);
            printWriter.println("  mCachedBrightnessInfo.adjustedBrightness=" + this.mCachedBrightnessInfo.adjustedBrightness.value);
            printWriter.println("  mCachedBrightnessInfo.brightnessMin=" + this.mCachedBrightnessInfo.brightnessMin.value);
            printWriter.println("  mCachedBrightnessInfo.brightnessMax=" + this.mCachedBrightnessInfo.brightnessMax.value);
            printWriter.println("  mCachedBrightnessInfo.hbmMode=" + this.mCachedBrightnessInfo.hbmMode.value);
            printWriter.println("  mCachedBrightnessInfo.hbmTransitionPoint=" + this.mCachedBrightnessInfo.hbmTransitionPoint.value);
            printWriter.println("  mCachedBrightnessInfo.brightnessMaxReason =" + this.mCachedBrightnessInfo.brightnessMaxReason.value);
        }
        printWriter.println("  mDisplayBlanksAfterDozeConfig=" + this.mDisplayBlanksAfterDozeConfig);
        printWriter.println("  mBrightnessBucketsInDozeConfig=" + this.mBrightnessBucketsInDozeConfig);
        this.mHandler.runWithScissors(new Runnable() { // from class: com.android.server.display.DisplayPowerController2$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPowerController2.this.lambda$dump$9(printWriter);
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: dumpLocal, reason: merged with bridge method [inline-methods] */
    public void lambda$dump$9(PrintWriter printWriter) {
        printWriter.println();
        printWriter.println("Display Power Controller Thread State:");
        printWriter.println("  mPowerRequest=" + this.mPowerRequest);
        printWriter.println("  mBrightnessReason=" + this.mBrightnessReason);
        printWriter.println("  mAppliedDimming=" + this.mAppliedDimming);
        printWriter.println("  mAppliedLowPower=" + this.mAppliedLowPower);
        printWriter.println("  mAppliedThrottling=" + this.mAppliedThrottling);
        printWriter.println("  mDozing=" + this.mDozing);
        printWriter.println("  mSkipRampState=" + skipRampStateToString(this.mSkipRampState));
        printWriter.println("  mScreenOnBlockStartRealTime=" + this.mScreenOnBlockStartRealTime);
        printWriter.println("  mScreenOffBlockStartRealTime=" + this.mScreenOffBlockStartRealTime);
        printWriter.println("  mPendingScreenOnUnblocker=" + this.mPendingScreenOnUnblocker);
        printWriter.println("  mPendingScreenOffUnblocker=" + this.mPendingScreenOffUnblocker);
        printWriter.println("  mPendingScreenOff=" + this.mPendingScreenOff);
        printWriter.println("  mReportedToPolicy=" + reportedToPolicyToString(this.mReportedScreenStateToPolicy));
        printWriter.println("  mIsRbcActive=" + this.mIsRbcActive);
        this.mAutomaticBrightnessStrategy.dump(new IndentingPrintWriter(printWriter, "    "));
        if (this.mScreenBrightnessRampAnimator != null) {
            printWriter.println("  mScreenBrightnessRampAnimator.isAnimating()=" + this.mScreenBrightnessRampAnimator.isAnimating());
        }
        if (this.mColorFadeOnAnimator != null) {
            printWriter.println("  mColorFadeOnAnimator.isStarted()=" + this.mColorFadeOnAnimator.isStarted());
        }
        if (this.mColorFadeOffAnimator != null) {
            printWriter.println("  mColorFadeOffAnimator.isStarted()=" + this.mColorFadeOffAnimator.isStarted());
        }
        DisplayPowerState displayPowerState = this.mPowerState;
        if (displayPowerState != null) {
            displayPowerState.dump(printWriter);
        }
        AutomaticBrightnessController automaticBrightnessController = this.mAutomaticBrightnessController;
        if (automaticBrightnessController != null) {
            automaticBrightnessController.dump(printWriter);
            dumpBrightnessEvents(printWriter);
        }
        dumpRbcEvents(printWriter);
        HighBrightnessModeController highBrightnessModeController = this.mHbmController;
        if (highBrightnessModeController != null) {
            highBrightnessModeController.dump(printWriter);
        }
        BrightnessThrottler brightnessThrottler = this.mBrightnessThrottler;
        if (brightnessThrottler != null) {
            brightnessThrottler.dump(printWriter);
        }
        printWriter.println();
        DisplayWhiteBalanceController displayWhiteBalanceController = this.mDisplayWhiteBalanceController;
        if (displayWhiteBalanceController != null) {
            displayWhiteBalanceController.dump(printWriter);
            this.mDisplayWhiteBalanceSettings.dump(printWriter);
        }
        printWriter.println();
        WakelockController wakelockController = this.mWakelockController;
        if (wakelockController != null) {
            wakelockController.dumpLocal(printWriter);
        }
        printWriter.println();
        DisplayBrightnessController displayBrightnessController = this.mDisplayBrightnessController;
        if (displayBrightnessController != null) {
            displayBrightnessController.dump(printWriter);
        }
        printWriter.println();
        DisplayStateController displayStateController = this.mDisplayStateController;
        if (displayStateController != null) {
            displayStateController.dumpsys(printWriter);
        }
    }

    private static String reportedToPolicyToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? Integer.toString(i) : "REPORTED_TO_POLICY_SCREEN_ON" : "REPORTED_TO_POLICY_SCREEN_TURNING_ON" : "REPORTED_TO_POLICY_SCREEN_OFF";
    }

    private static String skipRampStateToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? Integer.toString(i) : "RAMP_STATE_SKIP_AUTOBRIGHT" : "RAMP_STATE_SKIP_INITIAL" : "RAMP_STATE_SKIP_NONE";
    }

    private void dumpBrightnessEvents(PrintWriter printWriter) {
        int size = this.mBrightnessEventRingBuffer.size();
        if (size < 1) {
            printWriter.println("No Automatic Brightness Adjustments");
            return;
        }
        printWriter.println("Automatic Brightness Adjustments Last " + size + " Events: ");
        BrightnessEvent[] brightnessEventArr = (BrightnessEvent[]) this.mBrightnessEventRingBuffer.toArray();
        for (int i = 0; i < this.mBrightnessEventRingBuffer.size(); i++) {
            printWriter.println("  " + brightnessEventArr[i].toString());
        }
    }

    private void dumpRbcEvents(PrintWriter printWriter) {
        int size = this.mRbcEventRingBuffer.size();
        if (size < 1) {
            printWriter.println("No Reduce Bright Colors Adjustments");
            return;
        }
        printWriter.println("Reduce Bright Colors Adjustments Last " + size + " Events: ");
        BrightnessEvent[] brightnessEventArr = (BrightnessEvent[]) this.mRbcEventRingBuffer.toArray();
        for (int i = 0; i < this.mRbcEventRingBuffer.size(); i++) {
            printWriter.println("  " + brightnessEventArr[i]);
        }
    }

    private void noteScreenState(int i) {
        FrameworkStatsLog.write(FrameworkStatsLog.SCREEN_STATE_CHANGED_V2, i, this.mDisplayStatsId);
        IBatteryStats iBatteryStats = this.mBatteryStats;
        if (iBatteryStats != null) {
            try {
                iBatteryStats.noteScreenState(i);
            } catch (RemoteException unused) {
            }
        }
    }

    private void noteScreenBrightness(float f) {
        IBatteryStats iBatteryStats = this.mBatteryStats;
        if (iBatteryStats != null) {
            try {
                iBatteryStats.noteScreenBrightness(BrightnessSynchronizer.brightnessFloatToInt(f));
            } catch (RemoteException unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportStats(float f) {
        if (this.mLastStatsBrightness == f) {
            return;
        }
        synchronized (this.mCachedBrightnessInfo) {
            MutableFloat mutableFloat = this.mCachedBrightnessInfo.hbmTransitionPoint;
            if (mutableFloat == null) {
                return;
            }
            float f2 = mutableFloat.value;
            boolean z = f > f2;
            boolean z2 = this.mLastStatsBrightness > f2;
            if (z || z2) {
                this.mLastStatsBrightness = f;
                this.mHandler.removeMessages(11);
                if (z != z2) {
                    logHbmBrightnessStats(f, this.mDisplayStatsId);
                    return;
                }
                Message obtainMessage = this.mHandler.obtainMessage();
                obtainMessage.what = 11;
                obtainMessage.arg1 = Float.floatToIntBits(f);
                obtainMessage.arg2 = this.mDisplayStatsId;
                this.mHandler.sendMessageAtTime(obtainMessage, this.mClock.uptimeMillis() + 500);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logHbmBrightnessStats(float f, int i) {
        synchronized (this.mHandler) {
            FrameworkStatsLog.write(FrameworkStatsLog.DISPLAY_HBM_BRIGHTNESS_CHANGED, i, f);
        }
    }

    private int nitsToRangeIndex(float f) {
        int i = 0;
        while (true) {
            float[] fArr = BRIGHTNESS_RANGE_BOUNDARIES;
            if (i >= fArr.length) {
                return 38;
            }
            if (f < fArr[i]) {
                return BRIGHTNESS_RANGE_INDEX[i];
            }
            i++;
        }
    }

    private void logBrightnessEvent(BrightnessEvent brightnessEvent, float f) {
        int modifier = brightnessEvent.getReason().getModifier();
        int flags = brightnessEvent.getFlags();
        boolean z = f == brightnessEvent.getHbmMax();
        float convertToAdjustedNits = this.mDisplayBrightnessController.convertToAdjustedNits(brightnessEvent.getBrightness());
        float powerFactor = brightnessEvent.isLowPowerModeSet() ? brightnessEvent.getPowerFactor() : -1.0f;
        int rbcStrength = brightnessEvent.isRbcEnabled() ? brightnessEvent.getRbcStrength() : -1;
        float convertToAdjustedNits2 = brightnessEvent.getHbmMode() == 0 ? -1.0f : this.mDisplayBrightnessController.convertToAdjustedNits(brightnessEvent.getHbmMax());
        float convertToAdjustedNits3 = brightnessEvent.getThermalMax() == 1.0f ? -1.0f : this.mDisplayBrightnessController.convertToAdjustedNits(brightnessEvent.getThermalMax());
        if (this.mIsDisplayInternal) {
            FrameworkStatsLog.write(FrameworkStatsLog.DISPLAY_BRIGHTNESS_CHANGED, this.mDisplayBrightnessController.convertToAdjustedNits(brightnessEvent.getInitialBrightness()), convertToAdjustedNits, brightnessEvent.getLux(), brightnessEvent.getPhysicalDisplayId(), brightnessEvent.wasShortTermModelActive(), powerFactor, rbcStrength, convertToAdjustedNits2, convertToAdjustedNits3, brightnessEvent.isAutomaticBrightnessEnabled(), 1, convertBrightnessReasonToStatsEnum(brightnessEvent.getReason().getReason()), nitsToRangeIndex(convertToAdjustedNits), z, brightnessEvent.getHbmMode() == 1, brightnessEvent.getHbmMode() == 2, (modifier & 2) > 0, this.mBrightnessThrottler.getBrightnessMaxReason(), (modifier & 1) > 0, brightnessEvent.isRbcEnabled(), (flags & 2) > 0, (flags & 4) > 0, (flags & 8) > 0, (flags & 16) > 0, (flags & 32) > 0);
        }
    }

    private boolean readyToUpdateDisplayState() {
        return this.mDisplayId == 0 || this.mBootCompleted;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DisplayControllerHandler extends Handler {
        DisplayControllerHandler(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            boolean z = false;
            switch (message.what) {
                case 1:
                    DisplayPowerController2.this.lambda$new$0();
                    return;
                case 2:
                    if (DisplayPowerController2.this.mPendingScreenOnUnblocker == message.obj) {
                        DisplayPowerController2.this.unblockScreenOn();
                        DisplayPowerController2.this.lambda$new$0();
                        return;
                    }
                    return;
                case 3:
                    if (DisplayPowerController2.this.mPendingScreenOffUnblocker == message.obj) {
                        DisplayPowerController2.this.unblockScreenOff();
                        DisplayPowerController2.this.lambda$new$0();
                        return;
                    }
                    return;
                case 4:
                    BrightnessConfiguration brightnessConfiguration = (BrightnessConfiguration) message.obj;
                    DisplayPowerController2.this.mAutomaticBrightnessStrategy.setBrightnessConfiguration(brightnessConfiguration, message.arg1 == 1);
                    if (DisplayPowerController2.this.mBrightnessTracker != null) {
                        BrightnessTracker brightnessTracker = DisplayPowerController2.this.mBrightnessTracker;
                        if (brightnessConfiguration != null && brightnessConfiguration.shouldCollectColorSamples()) {
                            z = true;
                        }
                        brightnessTracker.setShouldCollectColorSample(z);
                    }
                    DisplayPowerController2.this.lambda$new$0();
                    return;
                case 5:
                    DisplayPowerController2.this.mDisplayBrightnessController.setTemporaryBrightness(Float.valueOf(Float.intBitsToFloat(message.arg1)));
                    DisplayPowerController2.this.lambda$new$0();
                    return;
                case 6:
                    DisplayPowerController2.this.mAutomaticBrightnessStrategy.setTemporaryAutoBrightnessAdjustment(Float.intBitsToFloat(message.arg1));
                    DisplayPowerController2.this.lambda$new$0();
                    return;
                case 7:
                    DisplayPowerController2.this.cleanupHandlerThreadAfterStop();
                    return;
                case 8:
                    if (DisplayPowerController2.this.mStopped) {
                        return;
                    }
                    DisplayPowerController2.this.handleSettingsChange(false);
                    return;
                case 9:
                    DisplayPowerController2.this.handleRbcChanged();
                    return;
                case 10:
                    if (DisplayPowerController2.this.mPowerState != null) {
                        DisplayPowerController2.this.reportStats(DisplayPowerController2.this.mPowerState.getScreenBrightness());
                        return;
                    }
                    return;
                case 11:
                    DisplayPowerController2.this.logHbmBrightnessStats(Float.intBitsToFloat(message.arg1), message.arg2);
                    return;
                case 12:
                    DisplayPowerController2.this.handleOnSwitchUser(message.arg1);
                    return;
                case 13:
                    DisplayPowerController2.this.mBootCompleted = true;
                    DisplayPowerController2.this.lambda$new$0();
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (uri.equals(Settings.System.getUriFor("screen_brightness_mode"))) {
                DisplayPowerController2.this.handleBrightnessModeChange();
            } else {
                DisplayPowerController2.this.handleSettingsChange(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ScreenOnUnblocker implements WindowManagerPolicy.ScreenOnListener {
        private ScreenOnUnblocker() {
        }

        public void onScreenOn() {
            DisplayPowerController2.this.mHandler.sendMessageAtTime(DisplayPowerController2.this.mHandler.obtainMessage(2, this), DisplayPowerController2.this.mClock.uptimeMillis());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ScreenOffUnblocker implements WindowManagerPolicy.ScreenOffListener {
        private ScreenOffUnblocker() {
        }

        public void onScreenOff() {
            DisplayPowerController2.this.mHandler.sendMessageAtTime(DisplayPowerController2.this.mHandler.obtainMessage(3, this), DisplayPowerController2.this.mClock.uptimeMillis());
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setAutoBrightnessLoggingEnabled(boolean z) {
        AutomaticBrightnessController automaticBrightnessController = this.mAutomaticBrightnessController;
        if (automaticBrightnessController != null) {
            automaticBrightnessController.setLoggingEnabled(z);
        }
    }

    @Override // com.android.server.display.whitebalance.DisplayWhiteBalanceController.Callbacks
    public void updateWhiteBalance() {
        sendUpdatePowerState();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setDisplayWhiteBalanceLoggingEnabled(boolean z) {
        DisplayWhiteBalanceController displayWhiteBalanceController = this.mDisplayWhiteBalanceController;
        if (displayWhiteBalanceController != null) {
            displayWhiteBalanceController.setLoggingEnabled(z);
            this.mDisplayWhiteBalanceSettings.setLoggingEnabled(z);
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setAmbientColorTemperatureOverride(float f) {
        DisplayWhiteBalanceController displayWhiteBalanceController = this.mDisplayWhiteBalanceController;
        if (displayWhiteBalanceController != null) {
            displayWhiteBalanceController.setAmbientColorTemperatureOverride(f);
            sendUpdatePowerState();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        Injector() {
        }

        Clock getClock() {
            return new Clock() { // from class: com.android.server.display.DisplayPowerController2$Injector$$ExternalSyntheticLambda0
                @Override // com.android.server.display.DisplayPowerController2.Clock
                public final long uptimeMillis() {
                    return SystemClock.uptimeMillis();
                }
            };
        }

        DisplayPowerState getDisplayPowerState(DisplayBlanker displayBlanker, ColorFade colorFade, int i, int i2) {
            return new DisplayPowerState(displayBlanker, colorFade, i, i2, null);
        }

        RampAnimator.DualRampAnimator<DisplayPowerState> getDualRampAnimator(DisplayPowerState displayPowerState, FloatProperty<DisplayPowerState> floatProperty, FloatProperty<DisplayPowerState> floatProperty2) {
            return new RampAnimator.DualRampAnimator<>(displayPowerState, floatProperty, floatProperty2);
        }

        WakelockController getWakelockController(int i, DisplayManagerInternal.DisplayPowerCallbacks displayPowerCallbacks) {
            return new WakelockController(i, displayPowerCallbacks);
        }

        DisplayPowerProximityStateController getDisplayPowerProximityStateController(WakelockController wakelockController, DisplayDeviceConfig displayDeviceConfig, Looper looper, Runnable runnable, int i, SensorManager sensorManager) {
            return new DisplayPowerProximityStateController(wakelockController, displayDeviceConfig, looper, runnable, i, sensorManager, null);
        }

        AutomaticBrightnessController getAutomaticBrightnessController(AutomaticBrightnessController.Callbacks callbacks, Looper looper, SensorManager sensorManager, Sensor sensor, BrightnessMappingStrategy brightnessMappingStrategy, int i, float f, float f2, float f3, int i2, int i3, long j, long j2, boolean z, HysteresisLevels hysteresisLevels, HysteresisLevels hysteresisLevels2, HysteresisLevels hysteresisLevels3, HysteresisLevels hysteresisLevels4, Context context, HighBrightnessModeController highBrightnessModeController, BrightnessThrottler brightnessThrottler, BrightnessMappingStrategy brightnessMappingStrategy2, int i4, int i5, float f4, float f5) {
            return new AutomaticBrightnessController(callbacks, looper, sensorManager, sensor, brightnessMappingStrategy, i, f, f2, f3, i2, i3, j, j2, z, hysteresisLevels, hysteresisLevels2, hysteresisLevels3, hysteresisLevels4, context, highBrightnessModeController, brightnessThrottler, brightnessMappingStrategy2, i4, i5, f4, f5);
        }

        BrightnessMappingStrategy getInteractiveModeBrightnessMapper(Resources resources, DisplayDeviceConfig displayDeviceConfig, DisplayWhiteBalanceController displayWhiteBalanceController) {
            return BrightnessMappingStrategy.create(resources, displayDeviceConfig, displayWhiteBalanceController);
        }

        HysteresisLevels getHysteresisLevels(float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, float f, float f2) {
            return new HysteresisLevels(fArr, fArr2, fArr3, fArr4, f, f2);
        }

        HysteresisLevels getHysteresisLevels(float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, float f, float f2, boolean z) {
            return new HysteresisLevels(fArr, fArr2, fArr3, fArr4, f, f2, z);
        }

        ScreenOffBrightnessSensorController getScreenOffBrightnessSensorController(SensorManager sensorManager, Sensor sensor, Handler handler, ScreenOffBrightnessSensorController.Clock clock, int[] iArr, BrightnessMappingStrategy brightnessMappingStrategy) {
            return new ScreenOffBrightnessSensorController(sensorManager, sensor, handler, clock, iArr, brightnessMappingStrategy);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class CachedBrightnessInfo {
        public MutableFloat brightness = new MutableFloat(Float.NaN);
        public MutableFloat adjustedBrightness = new MutableFloat(Float.NaN);
        public MutableFloat brightnessMin = new MutableFloat(Float.NaN);
        public MutableFloat brightnessMax = new MutableFloat(Float.NaN);
        public MutableInt hbmMode = new MutableInt(0);
        public MutableFloat hbmTransitionPoint = new MutableFloat(Float.POSITIVE_INFINITY);
        public MutableInt brightnessMaxReason = new MutableInt(0);

        CachedBrightnessInfo() {
        }

        public boolean checkAndSetFloat(MutableFloat mutableFloat, float f) {
            if (mutableFloat.value == f) {
                return false;
            }
            mutableFloat.value = f;
            return true;
        }

        public boolean checkAndSetInt(MutableInt mutableInt, int i) {
            if (mutableInt.value == i) {
                return false;
            }
            mutableInt.value = i;
            return true;
        }
    }
}
