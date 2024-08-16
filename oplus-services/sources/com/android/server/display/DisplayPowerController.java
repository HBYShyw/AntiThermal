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
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.display.AmbientBrightnessDayStats;
import android.hardware.display.BrightnessChangeEvent;
import android.hardware.display.BrightnessConfiguration;
import android.hardware.display.BrightnessInfo;
import android.hardware.display.DisplayManagerInternal;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.FloatProperty;
import android.util.MathUtils;
import android.util.MutableFloat;
import android.util.MutableInt;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import android.view.Display;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IBatteryStats;
import com.android.internal.display.BrightnessSynchronizer;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.FrameworkStatsLog;
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
import com.android.server.display.color.ColorDisplayService;
import com.android.server.display.utils.SensorUtils;
import com.android.server.display.whitebalance.DisplayWhiteBalanceController;
import com.android.server.display.whitebalance.DisplayWhiteBalanceFactory;
import com.android.server.display.whitebalance.DisplayWhiteBalanceSettings;
import com.android.server.policy.WindowManagerPolicy;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DisplayPowerController implements AutomaticBrightnessController.Callbacks, DisplayWhiteBalanceController.Callbacks, DisplayPowerControllerInterface {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int BRIGHTNESS_CHANGE_STATSD_REPORT_INTERVAL_MS = 500;
    private static final int COLOR_FADE_OFF_ANIMATION_DURATION_MILLIS = 200;
    private static final int COLOR_FADE_ON_ANIMATION_DURATION_MILLIS = 250;
    private static final boolean DEBUG_PRETEND_PROXIMITY_SENSOR_ABSENT = false;
    private static final String FACE_SCREEN_BRIGHTNESS = "face_fill_screen_brightness";
    private static final float FLOAT_EPSINON = 1.0E-6f;
    private static final String GLOBAL_HBM_SELL_MODE = "global_hbm_sell_mode";
    private static final int MSG_BOOT_COMPLETED = 15;
    private static final int MSG_BRIGHTNESS_RAMP_DONE = 12;
    private static final int MSG_CONFIGURE_BRIGHTNESS = 5;
    private static final int MSG_IGNORE_PROXIMITY = 8;
    private static final int MSG_PROXIMITY_SENSOR_DEBOUNCED = 2;
    private static final int MSG_RESET_FPS_AFTER_FINISH_DC_BRIGHTNESS = 21;
    private static final int MSG_RESET_SCREEN_ON_CABC = 22;
    private static final int MSG_SCREEN_OFF_UNBLOCKED = 4;
    private static final int MSG_SCREEN_ON_UNBLOCKED = 3;
    private static final int MSG_SET_TEMPORARY_AUTO_BRIGHTNESS_ADJUSTMENT = 7;
    private static final int MSG_SET_TEMPORARY_BRIGHTNESS = 6;
    private static final int MSG_STATSD_HBM_BRIGHTNESS = 13;
    private static final int MSG_STOP = 9;
    private static final int MSG_SWITCH_USER = 14;
    private static final int MSG_UPDATE_BRIGHTNESS = 10;
    private static final int MSG_UPDATE_POWER_STATE = 1;
    private static final int MSG_UPDATE_RBC = 11;
    private static final int PROXIMITY_NEGATIVE = 0;
    private static final int PROXIMITY_POSITIVE = 1;
    private static final int PROXIMITY_SENSOR_NEGATIVE_DEBOUNCE_DELAY = 0;
    private static final int PROXIMITY_SENSOR_POSITIVE_DEBOUNCE_DELAY = 0;
    private static final int PROXIMITY_UNKNOWN = -1;
    private static final int RAMP_STATE_SKIP_AUTOBRIGHT = 2;
    private static final int RAMP_STATE_SKIP_INITIAL = 1;
    private static final int RAMP_STATE_SKIP_NONE = 0;
    private static final String REAL_SCREEN_BRIGHTNESS = "real_screen_brightness";
    private static final int REPORTED_TO_POLICY_SCREEN_OFF = 0;
    private static final int REPORTED_TO_POLICY_SCREEN_ON = 2;
    private static final int REPORTED_TO_POLICY_SCREEN_TURNING_OFF = 3;
    private static final int REPORTED_TO_POLICY_SCREEN_TURNING_ON = 1;
    private static final int REPORTED_TO_POLICY_UNREPORTED = -1;
    private static final int RINGBUFFER_MAX = 100;
    private static final String SCREEN_OFF_BLOCKED_TRACE_NAME = "Screen off blocked";
    private static final String SCREEN_ON_BLOCKED_TRACE_NAME = "Screen on blocked";
    private static final String SECOND_SCREEN_AUTO_BRIGHTNESS_ADJ = "second_screen_auto_brightness_adj";
    private static final boolean USE_COLOR_FADE_ON_ANIMATION = false;
    private boolean isRM;
    private final boolean mAllowAutoBrightnessWhileDozingConfig;
    private boolean mAppliedAutoBrightness;
    private boolean mAppliedBrightnessBoost;
    private boolean mAppliedDimming;
    private boolean mAppliedLowPower;
    private boolean mAppliedScreenBrightnessOverride;
    private boolean mAppliedTemporaryAutoBrightnessAdjustment;
    private boolean mAppliedTemporaryBrightness;
    private boolean mAppliedThrottling;
    private float mAutoBrightnessAdjustment;
    private IColorAutomaticBrightnessController mAutomaticBrightnessController;
    private final IBatteryStats mBatteryStats;
    private final DisplayBlanker mBlanker;
    private boolean mBootCompleted;
    private boolean mBrightnessBucketsInDozeConfig;
    private BrightnessConfiguration mBrightnessConfiguration;
    private RingBuffer<BrightnessEvent> mBrightnessEventRingBuffer;
    private long mBrightnessRampDecreaseMaxTimeMillis;
    private long mBrightnessRampIncreaseMaxTimeMillis;
    private float mBrightnessRampRateFastDecrease;
    private float mBrightnessRampRateFastIncrease;
    private float mBrightnessRampRateSlowDecrease;
    private float mBrightnessRampRateSlowIncrease;
    private final BrightnessSetting mBrightnessSetting;
    private BrightnessSetting.BrightnessSettingListener mBrightnessSettingListener;
    private final BrightnessThrottler mBrightnessThrottler;
    private float mBrightnessToFollow;
    private final BrightnessTracker mBrightnessTracker;
    private final DisplayManagerInternal.DisplayPowerCallbacks mCallbacks;
    private final ColorDisplayService.ColorDisplayServiceInternal mCdsi;
    private final Clock mClock;
    private final boolean mColorFadeEnabled;
    private final boolean mColorFadeFadesConfig;
    private ObjectAnimator mColorFadeOffAnimator;
    private ObjectAnimator mColorFadeOnAnimator;
    private final Context mContext;
    private float mCurrentScreenBrightnessSetting;
    private final boolean mDisplayBlanksAfterDozeConfig;
    private DisplayDevice mDisplayDevice;
    private DisplayDeviceConfig mDisplayDeviceConfig;
    private final int mDisplayId;

    @GuardedBy({"mLock"})
    private boolean mDisplayReadyLocked;
    private int mDisplayStatsId;
    private final DisplayWhiteBalanceController mDisplayWhiteBalanceController;
    private final DisplayWhiteBalanceSettings mDisplayWhiteBalanceSettings;
    private boolean mDozing;
    public IOplusDisplayPowerControllerExt mDpcExt;
    private final DisplayControllerHandler mHandler;
    private final HighBrightnessModeController mHbmController;
    private final HighBrightnessModeMetadata mHighBrightnessModeMetadata;
    private BrightnessMappingStrategy mIdleModeBrightnessMapper;
    private boolean mIgnoreProximityUntilChanged;
    private float mInitialAutoBrightness;
    private final Injector mInjector;
    private BrightnessMappingStrategy mInteractiveModeBrightnessMapper;
    private boolean mIsDisplayInternal;
    private boolean mIsEnabled;
    private boolean mIsInTransition;
    private boolean mIsPrimaryDisplay;
    private boolean mIsRbcActive;
    private final BrightnessEvent mLastBrightnessEvent;
    private int mLastState;
    private Sensor mLightSensor;
    private final LogicalDisplay mLogicalDisplay;
    private float[] mNitsRange;
    private final Runnable mOnBrightnessChangeRunnable;
    private int mOnProximityNegativeMessages;
    private int mOnProximityPositiveMessages;
    private boolean mOnStateChangedPending;
    private float mPendingAutoBrightnessAdjustment;

    @GuardedBy({"mLock"})
    private boolean mPendingRequestChangedLocked;

    @GuardedBy({"mLock"})
    private DisplayManagerInternal.DisplayPowerRequest mPendingRequestLocked;
    private float mPendingScreenBrightnessSetting;
    private boolean mPendingScreenOff;
    private ScreenOffUnblocker mPendingScreenOffUnblocker;
    private ScreenOnUnblocker mPendingScreenOnUnblocker;

    @GuardedBy({"mLock"})
    private boolean mPendingUpdatePowerStateLocked;

    @GuardedBy({"mLock"})
    private boolean mPendingWaitForNegativeProximityLocked;
    private final boolean mPersistBrightnessNitsForDefaultDisplay;
    private PowerManager mPowerManager;
    private DisplayManagerInternal.DisplayPowerRequest mPowerRequest;
    private DisplayPowerState mPowerState;
    private Sensor mProximitySensor;
    private boolean mProximitySensorEnabled;
    private float mProximityThreshold;
    private float mScreenBrightnessDefault;
    private float mScreenBrightnessDimConfig;
    private final float mScreenBrightnessDozeConfig;
    private final float mScreenBrightnessMinimumDimAmount;
    private int mScreenBrightnessNormalMaximum;
    private RampAnimator.DualRampAnimator<DisplayPowerState> mScreenBrightnessRampAnimator;
    private int mScreenBrightnessRangeMaximum;
    private int mScreenBrightnessRangeMinimum;
    private boolean mScreenOffBecauseOfProximity;
    private long mScreenOffBlockStartRealTime;
    private Sensor mScreenOffBrightnessSensor;
    private ScreenOffBrightnessSensorController mScreenOffBrightnessSensorController;
    private long mScreenOnBlockStartRealTime;
    private final SensorManager mSensorManager;
    private final SettingsObserver mSettingsObserver;
    private boolean mShouldResetShortTermModel;
    private final boolean mSkipScreenOnBrightnessRamp;
    private boolean mStopped;
    private final String mSuspendBlockerIdOnStateChanged;
    private final String mSuspendBlockerIdProxDebounce;
    private final String mSuspendBlockerIdProxNegative;
    private final String mSuspendBlockerIdProxPositive;
    private final String mSuspendBlockerIdUnfinishedBusiness;
    private final String mTag;
    private final BrightnessEvent mTempBrightnessEvent;
    private float mTemporaryAutoBrightnessAdjustment;
    private float mTemporaryScreenBrightness;
    private String mThermalBrightnessThrottlingDataId;
    private boolean mUnfinishedBusiness;
    private String mUniqueDisplayId;
    private boolean mUseAutoBrightness;
    private boolean mUseSoftwareAutoBrightnessConfig;
    private boolean mWaitingForNegativeProximity;
    private final WindowManagerPolicy mWindowManagerPolicy;
    private static boolean DEBUG = SystemProperties.getBoolean("dbg.dms.dpc", false);
    private static final boolean MTK_DEBUG = "eng".equals(Build.TYPE);
    private static final int DC_MODE_BRIGHT_EDGE = SystemProperties.getInt("ro.vendor.display.dc.brightness.threshold", 260);
    private static final String DC_MODE_CUSTOMIZATION_KEY = "ro.vendor.display.dc.brightness.customization";
    private static final boolean DC_MODE_BRIGHT_CUSTOMIZATION = SystemProperties.getBoolean(DC_MODE_CUSTOMIZATION_KEY, false);
    static boolean DEBUG_PANIC = false;
    private static final float SCREEN_ANIMATION_RATE_MINIMUM = 0.0f;
    private static final float TYPICAL_PROXIMITY_THRESHOLD = 5.0f;
    private static final float[] BRIGHTNESS_RANGE_BOUNDARIES = {SCREEN_ANIMATION_RATE_MINIMUM, 1.0f, 2.0f, 3.0f, 4.0f, TYPICAL_PROXIMITY_THRESHOLD, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 20.0f, 30.0f, 40.0f, 50.0f, 60.0f, 70.0f, 80.0f, 90.0f, 100.0f, 200.0f, 300.0f, 400.0f, 500.0f, 600.0f, 700.0f, 800.0f, 900.0f, 1000.0f, 1200.0f, 1400.0f, 1600.0f, 1800.0f, 2000.0f, 2250.0f, 2500.0f, 2750.0f, 3000.0f};
    private static final int[] BRIGHTNESS_RANGE_INDEX = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37};
    private static boolean DisplayDisable = SystemProperties.getBoolean("ro.oplus.display.fingerprint_disable", false);
    private static final String FP_SENSOR_TYPE = SystemProperties.get("persist.vendor.fingerprint.sensor_type", "unknow");
    private final Object mLock = new Object();
    private int mLeadDisplayId = -1;

    @GuardedBy({"mCachedBrightnessInfo"})
    private final CachedBrightnessInfo mCachedBrightnessInfo = new CachedBrightnessInfo();
    private int mProximity = -1;
    private int mPendingProximity = -1;
    private long mPendingProximityDebounceTime = -1;
    private int mReportedScreenStateToPolicy = -1;
    private final BrightnessReason mBrightnessReason = new BrightnessReason();
    private final BrightnessReason mBrightnessReasonTemp = new BrightnessReason();
    private float mLastStatsBrightness = SCREEN_ANIMATION_RATE_MINIMUM;
    private int mSkipRampState = 0;
    private float mLastUserSetScreenBrightness = Float.NaN;
    private float mTemporaryCurrentScreenBrightness = Float.NaN;
    private boolean mSmoothBrightnessEnable = OplusPixelworksHelper.isSupportBrightnessSmooth();
    private boolean mDCBrightnessChange = false;
    private boolean mUpdateFpsForDc = false;
    private boolean mResetFpsStatePending = false;
    private float mOplusOverriedBrightness = Float.NaN;
    private float mOplusLastOverriedBrightness = Float.NaN;

    @GuardedBy({"mLock"})
    private final SparseArray<DisplayPowerControllerInterface> mDisplayBrightnessFollowers = new SparseArray<>();
    private final Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() { // from class: com.android.server.display.DisplayPowerController.3
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
            DisplayPowerController.this.sendUpdatePowerState();
            DisplayPowerController.this.mDpcExt.onAnimationChanged(animator, 2);
        }
    };
    private final RampAnimator.Listener mRampAnimatorListener = new RampAnimator.Listener() { // from class: com.android.server.display.DisplayPowerController.4
        @Override // com.android.server.display.RampAnimator.Listener
        public void onAnimationStart(boolean z) {
            DisplayPowerController.this.mDpcExt.setAnimating(true, z);
        }

        @Override // com.android.server.display.RampAnimator.Listener
        public void onAnimationEnd(boolean z) {
            DisplayPowerController.this.sendUpdatePowerState();
            if (z) {
                DisplayPowerController.this.updateFpsIfNeeded(r1.mDpcExt.getMaximumScreenBrightnessSetting());
                DisplayPowerController.this.mDpcExt.setLowPowerAnimatingState(false);
                DisplayPowerController.this.mDpcExt.setHDRAnimatingState(false);
                DisplayPowerController.this.mHandler.sendMessageAtTime(DisplayPowerController.this.mHandler.obtainMessage(12), DisplayPowerController.this.mClock.uptimeMillis());
            }
            DisplayPowerController.this.mDpcExt.setAnimating(false, z);
        }
    };
    private final Runnable mCleanListener = new Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda6
        @Override // java.lang.Runnable
        public final void run() {
            DisplayPowerController.this.sendUpdatePowerState();
        }
    };
    private final Runnable mOnStateChangedRunnable = new Runnable() { // from class: com.android.server.display.DisplayPowerController.6
        @Override // java.lang.Runnable
        public void run() {
            DisplayPowerController.this.mOnStateChangedPending = false;
            DisplayPowerController.this.mCallbacks.onStateChanged();
            DisplayPowerController.this.mCallbacks.releaseSuspendBlocker(DisplayPowerController.this.mSuspendBlockerIdOnStateChanged);
        }
    };
    private final Runnable mOnProximityPositiveRunnable = new Runnable() { // from class: com.android.server.display.DisplayPowerController.7
        @Override // java.lang.Runnable
        public void run() {
            DisplayPowerController displayPowerController = DisplayPowerController.this;
            displayPowerController.mOnProximityPositiveMessages--;
            DisplayPowerController.this.mCallbacks.onProximityPositive();
            DisplayPowerController.this.mCallbacks.releaseSuspendBlocker(DisplayPowerController.this.mSuspendBlockerIdProxPositive);
        }
    };
    private final Runnable mOnProximityNegativeRunnable = new Runnable() { // from class: com.android.server.display.DisplayPowerController.8
        @Override // java.lang.Runnable
        public void run() {
            DisplayPowerController displayPowerController = DisplayPowerController.this;
            displayPowerController.mOnProximityNegativeMessages--;
            DisplayPowerController.this.mCallbacks.onProximityNegative();
            DisplayPowerController.this.mCallbacks.releaseSuspendBlocker(DisplayPowerController.this.mSuspendBlockerIdProxNegative);
        }
    };
    private final SensorEventListener mProximitySensorListener = new SensorEventListener() { // from class: com.android.server.display.DisplayPowerController.9
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (DisplayPowerController.this.mProximitySensorEnabled) {
                long uptimeMillis = DisplayPowerController.this.mClock.uptimeMillis();
                boolean z = false;
                float f = sensorEvent.values[0];
                if (f >= DisplayPowerController.SCREEN_ANIMATION_RATE_MINIMUM && f < DisplayPowerController.this.mProximityThreshold && !DisplayPowerController.this.mDpcExt.isIgnoreProximity()) {
                    z = true;
                }
                DisplayPowerController.this.handleProximitySensorEvent(uptimeMillis, z);
            }
        }
    };
    private final IOplusDisplayPowerControllerWrapper mWrapper = new OplusDisplayPowerControllerWrapper();

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Clock {
        long uptimeMillis();
    }

    private static float clampAbsoluteBrightness(float f) {
        return f;
    }

    private static float clampAutoBrightnessAdjustment(float f) {
        return f;
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:24:0x029f  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x02b8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public DisplayPowerController(Context context, Injector injector, DisplayManagerInternal.DisplayPowerCallbacks displayPowerCallbacks, Handler handler, SensorManager sensorManager, DisplayBlanker displayBlanker, LogicalDisplay logicalDisplay, BrightnessTracker brightnessTracker, BrightnessSetting brightnessSetting, Runnable runnable, HighBrightnessModeMetadata highBrightnessModeMetadata, boolean z) {
        DisplayWhiteBalanceController displayWhiteBalanceController;
        DisplayWhiteBalanceSettings displayWhiteBalanceSettings;
        this.mIsPrimaryDisplay = false;
        Injector injector2 = injector != null ? injector : new Injector();
        this.mInjector = injector2;
        this.mClock = injector2.getClock();
        this.mLogicalDisplay = logicalDisplay;
        int displayIdLocked = logicalDisplay.getDisplayIdLocked();
        this.mDisplayId = displayIdLocked;
        this.mTag = "DisplayPowerController[" + displayIdLocked + "]";
        this.mHighBrightnessModeMetadata = highBrightnessModeMetadata;
        this.mSuspendBlockerIdUnfinishedBusiness = getSuspendBlockerUnfinishedBusinessId(displayIdLocked);
        this.mSuspendBlockerIdOnStateChanged = getSuspendBlockerOnStateChangedId(displayIdLocked);
        this.mSuspendBlockerIdProxPositive = getSuspendBlockerProxPositiveId(displayIdLocked);
        this.mSuspendBlockerIdProxNegative = getSuspendBlockerProxNegativeId(displayIdLocked);
        this.mSuspendBlockerIdProxDebounce = getSuspendBlockerProxDebounceId(displayIdLocked);
        this.mDisplayDevice = logicalDisplay.getPrimaryDisplayDeviceLocked();
        String uniqueId = logicalDisplay.getPrimaryDisplayDeviceLocked().getUniqueId();
        this.mUniqueDisplayId = uniqueId;
        this.mDisplayStatsId = uniqueId.hashCode();
        this.mIsEnabled = logicalDisplay.isEnabledLocked();
        this.mIsInTransition = logicalDisplay.isInTransitionLocked();
        this.mIsDisplayInternal = logicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayDeviceInfoLocked().type == 1;
        DisplayControllerHandler displayControllerHandler = new DisplayControllerHandler(handler.getLooper());
        this.mHandler = displayControllerHandler;
        this.mLastBrightnessEvent = new BrightnessEvent(displayIdLocked);
        this.mTempBrightnessEvent = new BrightnessEvent(displayIdLocked);
        this.mThermalBrightnessThrottlingDataId = logicalDisplay.getDisplayInfoLocked().thermalBrightnessThrottlingDataId;
        if (displayIdLocked == 0) {
            this.mBatteryStats = BatteryStatsService.getService();
        } else {
            this.mBatteryStats = null;
        }
        this.mSettingsObserver = new SettingsObserver(displayControllerHandler);
        this.mCallbacks = displayPowerCallbacks;
        this.mSensorManager = sensorManager;
        this.mWindowManagerPolicy = (WindowManagerPolicy) LocalServices.getService(WindowManagerPolicy.class);
        this.mBlanker = displayBlanker;
        this.mContext = context;
        this.mBrightnessTracker = brightnessTracker;
        this.mBrightnessSetting = brightnessSetting;
        this.mLightSensor = sensorManager.getDefaultSensor(5);
        IOplusDisplayPowerControllerExt iOplusDisplayPowerControllerExt = (IOplusDisplayPowerControllerExt) ExtLoader.type(IOplusDisplayPowerControllerExt.class).base(this).create();
        this.mDpcExt = iOplusDisplayPowerControllerExt;
        iOplusDisplayPowerControllerExt.init(context, displayIdLocked);
        if (displayIdLocked == 0) {
            this.mDpcExt.setDisplayPowerController(this);
            this.mDpcExt.setOplusDisplayPowerControllerCallback(displayPowerCallbacks);
            this.mDpcExt.setDisplayPowerControlHandler(handler);
        }
        this.mOnBrightnessChangeRunnable = runnable;
        PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
        this.mPowerManager = powerManager;
        Resources resources = context.getResources();
        this.mScreenBrightnessDozeConfig = clampAbsoluteBrightness(powerManager.getBrightnessConstraint(4));
        this.mScreenBrightnessDimConfig = clampAbsoluteBrightness(powerManager.getBrightnessConstraint(3));
        this.mScreenBrightnessMinimumDimAmount = resources.getFloat(R.dimen.conversation_face_pile_protection_width_expanded);
        this.mScreenBrightnessDefault = clampAbsoluteBrightness(logicalDisplay.getDisplayInfoLocked().brightnessDefault);
        this.mUseSoftwareAutoBrightnessConfig = resources.getBoolean(R.bool.config_bluetooth_sco_off_call) && (displayIdLocked == 0 || this.mDpcExt.useSoftwareAutoBrightnessConfigInOtherDisplay(displayIdLocked));
        this.mAllowAutoBrightnessWhileDozingConfig = resources.getBoolean(R.bool.config_allowTheaterModeWakeFromKey);
        this.mPersistBrightnessNitsForDefaultDisplay = resources.getBoolean(17891779);
        this.mDisplayDeviceConfig = logicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayDeviceConfig();
        loadBrightnessRampRates();
        this.mSkipScreenOnBrightnessRamp = resources.getBoolean(17891821);
        this.mHbmController = createHbmControllerLocked();
        this.mBrightnessThrottler = createBrightnessThrottlerLocked();
        saveBrightnessInfo(getScreenBrightnessSetting());
        this.mColorFadeEnabled = !ActivityManager.isLowRamDeviceStatic();
        this.mColorFadeFadesConfig = resources.getBoolean(R.bool.config_awareSettingAvailable);
        this.mDisplayBlanksAfterDozeConfig = resources.getBoolean(17891621);
        this.mBrightnessBucketsInDozeConfig = resources.getBoolean(17891622);
        loadProximitySensor();
        this.mCurrentScreenBrightnessSetting = getScreenBrightnessSetting();
        this.mAutoBrightnessAdjustment = getAutoBrightnessAdjustmentSetting();
        this.mTemporaryScreenBrightness = Float.NaN;
        this.mPendingScreenBrightnessSetting = Float.NaN;
        this.mTemporaryAutoBrightnessAdjustment = Float.NaN;
        this.mPendingAutoBrightnessAdjustment = Float.NaN;
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
                        this.mScreenBrightnessDimConfig = this.mDpcExt.getMinimumScreenBrightnessSetting(this.mScreenBrightnessDimConfig);
                        this.mBrightnessBucketsInDozeConfig = true;
                        this.mBrightnessBucketsInDozeConfig = resources.getBoolean(17891622);
                        loadProximitySensor();
                        loadNitBasedBrightnessSetting();
                        this.mBrightnessToFollow = Float.NaN;
                        this.mAutoBrightnessAdjustment = getAutoBrightnessAdjustmentSetting();
                        this.mTemporaryScreenBrightness = Float.NaN;
                        this.mPendingScreenBrightnessSetting = Float.NaN;
                        this.mTemporaryAutoBrightnessAdjustment = Float.NaN;
                        this.mPendingAutoBrightnessAdjustment = Float.NaN;
                        this.mDpcExt.initParameters(this.mHandler);
                        boolean isPrimaryDisplay = this.mDpcExt.isPrimaryDisplay(this.mUniqueDisplayId);
                        this.mIsPrimaryDisplay = isPrimaryDisplay;
                        this.mDpcExt.setUniqueDisplayId(isPrimaryDisplay, this.mUniqueDisplayId);
                        this.mDpcExt.setDCMode();
                        Slog.d(this.mTag, "DPC construct " + this.mUniqueDisplayId + " mIsPrimaryDisplay:" + this.mIsPrimaryDisplay);
                        this.isRM = "1".equals(SystemProperties.get("debug.display.cabc.reset", "0"));
                        this.mBootCompleted = z;
                    }
                } catch (Exception e2) {
                    e = e2;
                    displayWhiteBalanceController = null;
                }
            } catch (Exception e3) {
                e = e3;
                displayWhiteBalanceController = null;
                displayWhiteBalanceSettings = null;
            }
        } else {
            displayWhiteBalanceController = null;
            displayWhiteBalanceSettings = null;
        }
        this.mDisplayWhiteBalanceSettings = displayWhiteBalanceSettings;
        this.mDisplayWhiteBalanceController = displayWhiteBalanceController;
        loadNitsRange(resources);
        if (this.mDisplayId != 0) {
            ColorDisplayService.ColorDisplayServiceInternal colorDisplayServiceInternal = (ColorDisplayService.ColorDisplayServiceInternal) LocalServices.getService(ColorDisplayService.ColorDisplayServiceInternal.class);
            this.mCdsi = colorDisplayServiceInternal;
            if (colorDisplayServiceInternal.setReduceBrightColorsListener(new ColorDisplayService.ReduceBrightColorsListener() { // from class: com.android.server.display.DisplayPowerController.1
                @Override // com.android.server.display.color.ColorDisplayService.ReduceBrightColorsListener
                public void onReduceBrightColorsActivationChanged(boolean z2, boolean z3) {
                    DisplayPowerController.this.applyReduceBrightColorsSplineAdjustment(false, z2);
                }

                @Override // com.android.server.display.color.ColorDisplayService.ReduceBrightColorsListener
                public void onReduceBrightColorsStrengthChanged(int i) {
                    DisplayPowerController.this.applyReduceBrightColorsSplineAdjustment(true, false);
                }
            })) {
                applyReduceBrightColorsSplineAdjustment(false, false);
            }
        } else {
            this.mCdsi = null;
        }
        setUpAutoBrightness(resources, handler);
        this.mScreenBrightnessDimConfig = this.mDpcExt.getMinimumScreenBrightnessSetting(this.mScreenBrightnessDimConfig);
        this.mBrightnessBucketsInDozeConfig = true;
        this.mBrightnessBucketsInDozeConfig = resources.getBoolean(17891622);
        loadProximitySensor();
        loadNitBasedBrightnessSetting();
        this.mBrightnessToFollow = Float.NaN;
        this.mAutoBrightnessAdjustment = getAutoBrightnessAdjustmentSetting();
        this.mTemporaryScreenBrightness = Float.NaN;
        this.mPendingScreenBrightnessSetting = Float.NaN;
        this.mTemporaryAutoBrightnessAdjustment = Float.NaN;
        this.mPendingAutoBrightnessAdjustment = Float.NaN;
        this.mDpcExt.initParameters(this.mHandler);
        boolean isPrimaryDisplay2 = this.mDpcExt.isPrimaryDisplay(this.mUniqueDisplayId);
        this.mIsPrimaryDisplay = isPrimaryDisplay2;
        this.mDpcExt.setUniqueDisplayId(isPrimaryDisplay2, this.mUniqueDisplayId);
        this.mDpcExt.setDCMode();
        Slog.d(this.mTag, "DPC construct " + this.mUniqueDisplayId + " mIsPrimaryDisplay:" + this.mIsPrimaryDisplay);
        this.isRM = "1".equals(SystemProperties.get("debug.display.cabc.reset", "0"));
        this.mBootCompleted = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyReduceBrightColorsSplineAdjustment(boolean z, boolean z2) {
        this.mHandler.obtainMessage(11, z ? 1 : 0, z2 ? 1 : 0).sendToTarget();
        sendUpdatePowerState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRbcChanged() {
        if (this.mAutomaticBrightnessController == null) {
            return;
        }
        float[] fArr = new float[this.mNitsRange.length];
        int i = 0;
        while (true) {
            float[] fArr2 = this.mNitsRange;
            if (i >= fArr2.length) {
                return;
            }
            fArr[i] = this.mCdsi.getReduceBrightColorsAdjustedBrightnessNits(fArr2[i]);
            i++;
        }
    }

    private Sensor findDisplayLightSensor(String str) {
        if (!TextUtils.isEmpty(str)) {
            List<Sensor> sensorList = this.mSensorManager.getSensorList(-1);
            for (int i = 0; i < sensorList.size(); i++) {
                Sensor sensor = sensorList.get(i);
                if (str.equals(sensor.getStringType())) {
                    return sensor;
                }
            }
        }
        return this.mSensorManager.getDefaultSensor(5);
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public boolean isProximitySensorAvailable() {
        return this.mProximitySensor != null;
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
        if (this.mDpcExt.onSwitchUser(i)) {
            handleSettingsChange(true);
            return;
        }
        int lastBrightnessMode = this.mDpcExt.getLastBrightnessMode();
        String format = String.format("newUserId=%d,autoMode=%d,dimming=%s,cur(%f,%f)", Integer.valueOf(i), Integer.valueOf(lastBrightnessMode), Boolean.valueOf(this.mAppliedDimming), Float.valueOf(this.mTemporaryCurrentScreenBrightness), Float.valueOf(this.mCurrentScreenBrightnessSetting));
        this.mCurrentScreenBrightnessSetting = this.mDpcExt.getRealBrightness(this.mCurrentScreenBrightnessSetting);
        Slog.d(this.mTag, "onSwitchUser " + format + ", sync brightness to " + this.mCurrentScreenBrightnessSetting);
        if (!this.mAppliedDimming) {
            Settings.System.putStringForUser(this.mContext.getContentResolver(), "screen_brightness", Integer.toString((int) this.mCurrentScreenBrightnessSetting), -2);
            Settings.System.putStringForUser(this.mContext.getContentResolver(), FACE_SCREEN_BRIGHTNESS, Integer.toString((int) this.mCurrentScreenBrightnessSetting), -2);
            Settings.System.putStringForUser(this.mContext.getContentResolver(), REAL_SCREEN_BRIGHTNESS, Integer.toString((int) this.mCurrentScreenBrightnessSetting), -2);
            Settings.System.putFloatForUser(this.mContext.getContentResolver(), "screen_auto_brightness_adj", this.mCurrentScreenBrightnessSetting, -2);
        }
        Settings.System.putIntForUser(this.mContext.getContentResolver(), "screen_brightness_mode", lastBrightnessMode, -2);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(14, Integer.valueOf(i)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnSwitchUser(int i) {
        handleSettingsChange(true);
        handleBrightnessModeChange();
        BrightnessTracker brightnessTracker = this.mBrightnessTracker;
        if (brightnessTracker != null) {
            brightnessTracker.onSwitchUser(i);
        }
        this.mDpcExt.setDCMode();
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
            this.mBrightnessToFollow = f;
        } else {
            float convertToFloatScale = convertToFloatScale(f2);
            if (isValidBrightnessValue(convertToFloatScale)) {
                this.mBrightnessToFollow = convertToFloatScale;
            } else {
                this.mBrightnessToFollow = f;
            }
        }
        sendUpdatePowerState();
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
            this.mHandler.postAtTime(new Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayPowerControllerInterface.this.setBrightnessToFollow(Float.NaN, -1.0f, DisplayPowerController.SCREEN_ANIMATION_RATE_MINIMUM);
                }
            }, this.mClock.uptimeMillis());
        }
    }

    @GuardedBy({"mLock"})
    private void clearDisplayBrightnessFollowersLocked() {
        for (int i = 0; i < this.mDisplayBrightnessFollowers.size(); i++) {
            final DisplayPowerControllerInterface valueAt = this.mDisplayBrightnessFollowers.valueAt(i);
            this.mHandler.postAtTime(new Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayPowerControllerInterface.this.setBrightnessToFollow(Float.NaN, -1.0f, DisplayPowerController.SCREEN_ANIMATION_RATE_MINIMUM);
                }
            }, this.mClock.uptimeMillis());
        }
        this.mDisplayBrightnessFollowers.clear();
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

    /* JADX WARN: Removed duplicated region for block: B:20:0x0039 A[Catch: all -> 0x009b, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0008, B:11:0x000d, B:13:0x0011, B:14:0x0016, B:16:0x001a, B:18:0x002f, B:20:0x0039, B:22:0x0041, B:24:0x0047, B:25:0x005b, B:27:0x0064, B:28:0x0097, B:29:0x0099, B:31:0x0023, B:33:0x0029), top: B:3:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0041 A[Catch: all -> 0x009b, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0008, B:11:0x000d, B:13:0x0011, B:14:0x0016, B:16:0x001a, B:18:0x002f, B:20:0x0039, B:22:0x0041, B:24:0x0047, B:25:0x005b, B:27:0x0064, B:28:0x0097, B:29:0x0099, B:31:0x0023, B:33:0x0029), top: B:3:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0064 A[Catch: all -> 0x009b, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0008, B:11:0x000d, B:13:0x0011, B:14:0x0016, B:16:0x001a, B:18:0x002f, B:20:0x0039, B:22:0x0041, B:24:0x0047, B:25:0x005b, B:27:0x0064, B:28:0x0097, B:29:0x0099, B:31:0x0023, B:33:0x0029), top: B:3:0x0003 }] */
    @Override // com.android.server.display.DisplayPowerControllerInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean requestPowerState(DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, boolean z) {
        boolean z2;
        synchronized (this.mLock) {
            if (this.mStopped) {
                return true;
            }
            if (!z || this.mPendingWaitForNegativeProximityLocked) {
                z2 = false;
            } else {
                this.mPendingWaitForNegativeProximityLocked = true;
                z2 = true;
            }
            DisplayManagerInternal.DisplayPowerRequest displayPowerRequest2 = this.mPendingRequestLocked;
            if (displayPowerRequest2 == null) {
                this.mPendingRequestLocked = new DisplayManagerInternal.DisplayPowerRequest(displayPowerRequest);
            } else {
                if (!displayPowerRequest2.equals(displayPowerRequest)) {
                    this.mPendingRequestLocked.copyFrom(displayPowerRequest);
                }
                if (this.mDpcExt.isUseProximityForceSuspendStateChanged(this.mDisplayId)) {
                    this.mPendingRequestLocked.copyFrom(displayPowerRequest);
                    z2 = true;
                }
                if (z2) {
                    this.mDisplayReadyLocked = false;
                    if (!this.mPendingRequestChangedLocked) {
                        this.mPendingRequestChangedLocked = true;
                        this.mDpcExt.updateBrightnessAnimationStatus(this.mPowerState, this.mPendingRequestLocked.policy, this.mLogicalDisplay, this.mDisplayId);
                        sendUpdatePowerStateLocked();
                    }
                }
                this.mDpcExt.setPowerRequestPolicy(displayPowerRequest.policy);
                if (z2) {
                    Slog.d(this.mTag, "requestPowerState: " + displayPowerRequest + ", waitForNegativeProximity=" + z + " displayReady=" + this.mDisplayReadyLocked + " pendingRequest=" + this.mPendingRequestChangedLocked);
                }
                return this.mDisplayReadyLocked;
            }
            z2 = true;
            if (this.mDpcExt.isUseProximityForceSuspendStateChanged(this.mDisplayId)) {
            }
            if (z2) {
            }
            this.mDpcExt.setPowerRequestPolicy(displayPowerRequest.policy);
            if (z2) {
            }
            return this.mDisplayReadyLocked;
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public BrightnessConfiguration getDefaultBrightnessConfiguration() {
        IColorAutomaticBrightnessController iColorAutomaticBrightnessController = this.mAutomaticBrightnessController;
        if (iColorAutomaticBrightnessController == null) {
            return null;
        }
        return iColorAutomaticBrightnessController.getDefaultConfig();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void onDisplayChanged(final HighBrightnessModeMetadata highBrightnessModeMetadata, int i) {
        this.mLeadDisplayId = i;
        final DisplayDevice primaryDisplayDeviceLocked = this.mLogicalDisplay.getPrimaryDisplayDeviceLocked();
        if (primaryDisplayDeviceLocked == null) {
            Slog.wtf(this.mTag, "Display Device is null in DisplayPowerController for display: " + this.mLogicalDisplay.getDisplayIdLocked());
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
        final int displayIdLocked = this.mLogicalDisplay.getDisplayIdLocked();
        this.mHandler.postAtTime(new Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPowerController.this.lambda$onDisplayChanged$2(primaryDisplayDeviceLocked, uniqueId, displayDeviceConfig, str, displayTokenLocked, displayDeviceInfoLocked, highBrightnessModeMetadata, isEnabledLocked, isInTransitionLocked, z, displayIdLocked);
            }
        }, this.mClock.uptimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:18:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0066  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$onDisplayChanged$2(DisplayDevice displayDevice, String str, DisplayDeviceConfig displayDeviceConfig, String str2, IBinder iBinder, DisplayDeviceInfo displayDeviceInfo, HighBrightnessModeMetadata highBrightnessModeMetadata, boolean z, boolean z2, boolean z3, int i) {
        boolean z4;
        IOplusDisplayPowerControllerExt iOplusDisplayPowerControllerExt;
        boolean z5 = true;
        if (this.mDisplayDevice != displayDevice) {
            this.mDisplayDevice = displayDevice;
            this.mUniqueDisplayId = str;
            this.mDisplayStatsId = str.hashCode();
            this.mDisplayDeviceConfig = displayDeviceConfig;
            this.mThermalBrightnessThrottlingDataId = str2;
            loadFromDisplayDeviceConfig(iBinder, displayDeviceInfo, highBrightnessModeMetadata);
            loadNitBasedBrightnessSetting();
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
            if (!z5 || this.mWrapper.getLogicalDisplayMapper().isRemapDisabledSecondaryDisplayId(i)) {
                if (DEBUG) {
                    Trace.beginAsyncSection("DisplayPowerController#updatePowerState", 0);
                }
                iOplusDisplayPowerControllerExt = this.mDpcExt;
                if (iOplusDisplayPowerControllerExt != null) {
                    boolean isPrimaryDisplay = iOplusDisplayPowerControllerExt.isPrimaryDisplay(this.mUniqueDisplayId);
                    this.mIsPrimaryDisplay = isPrimaryDisplay;
                    this.mDpcExt.setUniqueDisplayId(isPrimaryDisplay, str);
                }
                Slog.d(this.mTag, "onDisplayChanged id=" + i + " uniqueDisplayId=" + str + " enable=" + this.mLogicalDisplay.isEnabledLocked());
                updatePowerState();
            }
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
        if (DEBUG) {
        }
        iOplusDisplayPowerControllerExt = this.mDpcExt;
        if (iOplusDisplayPowerControllerExt != null) {
        }
        Slog.d(this.mTag, "onDisplayChanged id=" + i + " uniqueDisplayId=" + str + " enable=" + this.mLogicalDisplay.isEnabledLocked());
        updatePowerState();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void stop() {
        synchronized (this.mLock) {
            clearDisplayBrightnessFollowersLocked();
            this.mStopped = true;
            this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(9), this.mClock.uptimeMillis());
            DisplayWhiteBalanceController displayWhiteBalanceController = this.mDisplayWhiteBalanceController;
            if (displayWhiteBalanceController != null) {
                displayWhiteBalanceController.setEnabled(false);
            }
            IColorAutomaticBrightnessController iColorAutomaticBrightnessController = this.mAutomaticBrightnessController;
            if (iColorAutomaticBrightnessController != null) {
                iColorAutomaticBrightnessController.stop(this.mIsPrimaryDisplay);
            }
            this.mDpcExt.stop(this.mIsPrimaryDisplay);
            BrightnessSetting brightnessSetting = this.mBrightnessSetting;
            if (brightnessSetting != null) {
                brightnessSetting.unregisterListener(this.mBrightnessSettingListener);
            }
            this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
        }
    }

    private void loadFromDisplayDeviceConfig(IBinder iBinder, DisplayDeviceInfo displayDeviceInfo, HighBrightnessModeMetadata highBrightnessModeMetadata) {
        loadBrightnessRampRates();
        loadProximitySensor();
        loadNitsRange(this.mContext.getResources());
        setUpAutoBrightness(this.mContext.getResources(), this.mHandler);
        reloadReduceBrightColours();
        RampAnimator.DualRampAnimator<DisplayPowerState> dualRampAnimator = this.mScreenBrightnessRampAnimator;
        if (dualRampAnimator != null) {
            dualRampAnimator.setAnimationTimeLimits(this.mBrightnessRampIncreaseMaxTimeMillis, this.mBrightnessRampDecreaseMaxTimeMillis);
        }
        this.mHbmController.setHighBrightnessModeMetadata(highBrightnessModeMetadata);
        this.mHbmController.resetHbmData(displayDeviceInfo.width, displayDeviceInfo.height, iBinder, displayDeviceInfo.uniqueId, this.mDisplayDeviceConfig.getHighBrightnessModeData(), new HighBrightnessModeController.HdrBrightnessDeviceConfig() { // from class: com.android.server.display.DisplayPowerController.2
            @Override // com.android.server.display.HighBrightnessModeController.HdrBrightnessDeviceConfig
            public float getHdrBrightnessFromSdr(float f, float f2) {
                return DisplayPowerController.this.mDisplayDeviceConfig.getHdrBrightnessFromSdr(f, f2);
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
        DisplayPowerState displayPowerState = this.mInjector.getDisplayPowerState(this.mBlanker, this.mColorFadeEnabled ? new ColorFade(this.mDisplayId) : null, this.mDisplayId, i, this.mDpcExt);
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
            this.mDpcExt.setDuration(200);
        }
        RampAnimator.DualRampAnimator<DisplayPowerState> dualRampAnimator = this.mInjector.getDualRampAnimator(this.mPowerState, DisplayPowerState.SCREEN_BRIGHTNESS_FLOAT, DisplayPowerState.SCREEN_SDR_BRIGHTNESS_FLOAT);
        this.mScreenBrightnessRampAnimator = dualRampAnimator;
        dualRampAnimator.setAnimationTimeLimits(this.mBrightnessRampIncreaseMaxTimeMillis, this.mBrightnessRampDecreaseMaxTimeMillis);
        this.mDpcExt.setPowerState(this.mPowerState);
        this.mScreenBrightnessRampAnimator.setDisplayId(this.mDisplayId);
        this.mScreenBrightnessRampAnimator.setListener(this.mRampAnimatorListener);
        noteScreenState(this.mPowerState.getScreenState());
        noteScreenBrightness(this.mPowerState.getScreenBrightness());
        float convertToAdjustedNits = convertToAdjustedNits(this.mPowerState.getScreenBrightness());
        BrightnessTracker brightnessTracker = this.mBrightnessTracker;
        if (brightnessTracker != null && convertToAdjustedNits >= SCREEN_ANIMATION_RATE_MINIMUM) {
            brightnessTracker.start(convertToAdjustedNits);
        }
        BrightnessSetting.BrightnessSettingListener brightnessSettingListener = new BrightnessSetting.BrightnessSettingListener() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda9
            @Override // com.android.server.display.BrightnessSetting.BrightnessSettingListener
            public final void onBrightnessChanged(float f) {
                DisplayPowerController.this.lambda$initialize$3(f);
            }
        };
        this.mBrightnessSettingListener = brightnessSettingListener;
        this.mBrightnessSetting.registerListener(brightnessSettingListener);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("screen_auto_brightness_adj"), false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(GLOBAL_HBM_SELL_MODE), false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("screen_auto_brightness_adj_talkback"), false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("screen_brightness"), false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(SECOND_SCREEN_AUTO_BRIGHTNESS_ADJ), false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("screen_brightness_mode"), false, this.mSettingsObserver, -1);
        handleBrightnessModeChange();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initialize$3(float f) {
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(10, Float.valueOf(f)), this.mClock.uptimeMillis());
    }

    private void setUpAutoBrightness(Resources resources, Handler handler) {
        int i;
        boolean z = this.mDisplayDeviceConfig.isAutoBrightnessAvailable() && ((i = this.mDisplayId) == 0 || this.mDpcExt.useSoftwareAutoBrightnessConfigInOtherDisplay(i));
        this.mUseSoftwareAutoBrightnessConfig = z;
        if (z || this.mDpcExt.hasRemapDisable()) {
            BrightnessMappingStrategy brightnessMappingStrategy = this.mInteractiveModeBrightnessMapper;
            if (brightnessMappingStrategy != null) {
                brightnessMappingStrategy.getUserLux();
                this.mInteractiveModeBrightnessMapper.convertToNits(this.mInteractiveModeBrightnessMapper.getUserBrightness());
            }
            boolean z2 = resources.getBoolean(17891666);
            this.mInteractiveModeBrightnessMapper = this.mInjector.getInteractiveModeBrightnessMapper(resources, this.mDisplayDeviceConfig, this.mDisplayWhiteBalanceController);
            if (z2) {
                this.mIdleModeBrightnessMapper = BrightnessMappingStrategy.createForIdleMode(resources, this.mDisplayDeviceConfig, this.mDisplayWhiteBalanceController);
            }
            if (this.mInteractiveModeBrightnessMapper != null) {
                float fraction = resources.getFraction(R.fraction.config_screenAutoBrightnessDozeScaleFactor, 1, 1);
                this.mInjector.getHysteresisLevels(this.mDisplayDeviceConfig.getAmbientBrighteningPercentages(), this.mDisplayDeviceConfig.getAmbientDarkeningPercentages(), this.mDisplayDeviceConfig.getAmbientBrighteningLevels(), this.mDisplayDeviceConfig.getAmbientDarkeningLevels(), this.mDisplayDeviceConfig.getAmbientLuxDarkeningMinThreshold(), this.mDisplayDeviceConfig.getAmbientLuxBrighteningMinThreshold());
                this.mInjector.getHysteresisLevels(this.mDisplayDeviceConfig.getScreenBrighteningPercentages(), this.mDisplayDeviceConfig.getScreenDarkeningPercentages(), this.mDisplayDeviceConfig.getScreenBrighteningLevels(), this.mDisplayDeviceConfig.getScreenDarkeningLevels(), this.mDisplayDeviceConfig.getScreenDarkeningMinThreshold(), this.mDisplayDeviceConfig.getScreenBrighteningMinThreshold(), true);
                this.mInjector.getHysteresisLevels(this.mDisplayDeviceConfig.getAmbientBrighteningPercentagesIdle(), this.mDisplayDeviceConfig.getAmbientDarkeningPercentagesIdle(), this.mDisplayDeviceConfig.getAmbientBrighteningLevelsIdle(), this.mDisplayDeviceConfig.getAmbientDarkeningLevelsIdle(), this.mDisplayDeviceConfig.getAmbientLuxDarkeningMinThresholdIdle(), this.mDisplayDeviceConfig.getAmbientLuxBrighteningMinThresholdIdle());
                this.mInjector.getHysteresisLevels(this.mDisplayDeviceConfig.getScreenBrighteningPercentagesIdle(), this.mDisplayDeviceConfig.getScreenDarkeningPercentagesIdle(), this.mDisplayDeviceConfig.getScreenBrighteningLevelsIdle(), this.mDisplayDeviceConfig.getScreenDarkeningLevelsIdle(), this.mDisplayDeviceConfig.getScreenDarkeningMinThresholdIdle(), this.mDisplayDeviceConfig.getScreenBrighteningMinThresholdIdle());
                this.mDisplayDeviceConfig.getAutoBrightnessBrighteningLightDebounce();
                long autoBrightnessDarkeningLightDebounce = this.mDisplayDeviceConfig.getAutoBrightnessDarkeningLightDebounce();
                resources.getBoolean(R.bool.config_bluetooth_address_validation);
                resources.getInteger(R.integer.config_notificationsBatteryMediumARGB);
                int integer = resources.getInteger(R.integer.config_bluetooth_max_advertisers);
                int integer2 = resources.getInteger(R.integer.config_bluetooth_idle_cur_ma);
                if (integer2 != -1 && integer2 > integer) {
                    Slog.w(this.mTag, "Expected config_autoBrightnessInitialLightSensorRate (" + integer2 + ") to be less than or equal to config_autoBrightnessLightSensorRate (" + integer + ").");
                }
                loadAmbientLightSensor();
                BrightnessTracker brightnessTracker = this.mBrightnessTracker;
                if (brightnessTracker != null && this.mDisplayId == 0) {
                    brightnessTracker.setLightSensor(this.mLightSensor);
                }
                IColorAutomaticBrightnessController iColorAutomaticBrightnessController = this.mAutomaticBrightnessController;
                if (iColorAutomaticBrightnessController != null) {
                    iColorAutomaticBrightnessController.stop(this.mIsPrimaryDisplay);
                }
                this.mDpcExt.stop(this.mIsPrimaryDisplay);
                this.mAutomaticBrightnessController = this.mDpcExt.initAutomaticBrightnessController(this, handler.getLooper(), this.mSensorManager, this.mLightSensor, this.mInteractiveModeBrightnessMapper, fraction, integer, autoBrightnessDarkeningLightDebounce);
                this.mBrightnessEventRingBuffer = new RingBuffer<>(BrightnessEvent.class, 100);
                ScreenOffBrightnessSensorController screenOffBrightnessSensorController = this.mScreenOffBrightnessSensorController;
                if (screenOffBrightnessSensorController != null) {
                    screenOffBrightnessSensorController.stop();
                    this.mScreenOffBrightnessSensorController = null;
                }
                loadScreenOffBrightnessSensor();
                int[] screenOffBrightnessSensorValueToLux = this.mDisplayDeviceConfig.getScreenOffBrightnessSensorValueToLux();
                Sensor sensor = this.mScreenOffBrightnessSensor;
                if (sensor == null || screenOffBrightnessSensorValueToLux == null) {
                    return;
                }
                this.mScreenOffBrightnessSensorController = this.mInjector.getScreenOffBrightnessSensorController(this.mSensorManager, sensor, this.mHandler, new DisplayPowerController$$ExternalSyntheticLambda8(), screenOffBrightnessSensorValueToLux, this.mInteractiveModeBrightnessMapper);
                return;
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
        applyReduceBrightColorsSplineAdjustment(false, false);
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setAutomaticScreenBrightnessMode(boolean z) {
        DisplayWhiteBalanceController displayWhiteBalanceController = this.mDisplayWhiteBalanceController;
        if (displayWhiteBalanceController != null) {
            displayWhiteBalanceController.setStrongModeEnabled(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupHandlerThreadAfterStop() {
        setProximitySensorEnabled(false);
        this.mHbmController.stop();
        this.mBrightnessThrottler.stop();
        this.mHandler.removeCallbacksAndMessages(null);
        if (this.mUnfinishedBusiness) {
            this.mCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdUnfinishedBusiness);
            this.mUnfinishedBusiness = false;
        }
        if (this.mOnStateChangedPending) {
            this.mCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdOnStateChanged);
            this.mOnStateChangedPending = false;
        }
        for (int i = 0; i < this.mOnProximityPositiveMessages; i++) {
            this.mCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdProxPositive);
        }
        this.mOnProximityPositiveMessages = 0;
        for (int i2 = 0; i2 < this.mOnProximityNegativeMessages; i2++) {
            this.mCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdProxNegative);
        }
        this.mOnProximityNegativeMessages = 0;
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
    public void updatePowerState() {
        Trace.traceBegin(131072L, "DisplayPowerController#updatePowerState");
        updatePowerStateInternal();
        Trace.traceEnd(131072L);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:103:0x01c0  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0293  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x02a1  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x02dd  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x031f  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0347  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0351  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x0368  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x03eb  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x03f6  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x03fb  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x0430  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x043e  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0445  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x04a0  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x04d1  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x04de A[LOOP:0: B:251:0x04d8->B:253:0x04de, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:257:0x04f7  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x0535  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x057e  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x058a  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0095  */
    /* JADX WARN: Removed duplicated region for block: B:312:0x0607 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:316:0x0616 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:323:0x06c8  */
    /* JADX WARN: Removed duplicated region for block: B:326:0x06d6  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x0703  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x071c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:337:0x0743  */
    /* JADX WARN: Removed duplicated region for block: B:340:0x074e  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x075c  */
    /* JADX WARN: Removed duplicated region for block: B:346:0x0763  */
    /* JADX WARN: Removed duplicated region for block: B:354:0x0784  */
    /* JADX WARN: Removed duplicated region for block: B:366:0x07af  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x07bc  */
    /* JADX WARN: Removed duplicated region for block: B:379:0x07fc  */
    /* JADX WARN: Removed duplicated region for block: B:383:0x0804  */
    /* JADX WARN: Removed duplicated region for block: B:387:0x0819  */
    /* JADX WARN: Removed duplicated region for block: B:390:0x084a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:402:0x0878  */
    /* JADX WARN: Removed duplicated region for block: B:410:0x0896  */
    /* JADX WARN: Removed duplicated region for block: B:413:0x089f  */
    /* JADX WARN: Removed duplicated region for block: B:415:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:430:0x06db  */
    /* JADX WARN: Removed duplicated region for block: B:431:0x06cb  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:441:0x05f7  */
    /* JADX WARN: Removed duplicated region for block: B:443:0x0564  */
    /* JADX WARN: Removed duplicated region for block: B:446:0x0522  */
    /* JADX WARN: Removed duplicated region for block: B:449:0x04d3  */
    /* JADX WARN: Removed duplicated region for block: B:450:0x04c4  */
    /* JADX WARN: Removed duplicated region for block: B:462:0x03b7  */
    /* JADX WARN: Removed duplicated region for block: B:466:0x0349  */
    /* JADX WARN: Removed duplicated region for block: B:468:0x0340  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:471:0x02e6  */
    /* JADX WARN: Removed duplicated region for block: B:472:0x02c9  */
    /* JADX WARN: Removed duplicated region for block: B:478:0x0163  */
    /* JADX WARN: Removed duplicated region for block: B:480:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01aa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updatePowerStateInternal() {
        int i;
        byte b;
        int i2;
        boolean z;
        float f;
        ScreenOffBrightnessSensorController screenOffBrightnessSensorController;
        IColorAutomaticBrightnessController iColorAutomaticBrightnessController;
        int screenState;
        boolean z2;
        boolean updateUserSetScreenBrightness;
        boolean z3;
        float f2;
        int i3;
        BrightnessTracker brightnessTracker;
        boolean z4;
        int i4;
        float f3;
        float currentScreenBrightnessSetting;
        float screenNormalMaxBrightness;
        int i5;
        DisplayManagerInternal.DisplayPowerRequest displayPowerRequest;
        boolean z5;
        int i6;
        int i7;
        int i8;
        int i9;
        boolean saveBrightnessInfo;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        int i10;
        RingBuffer<BrightnessEvent> ringBuffer;
        ScreenOffBrightnessSensorController screenOffBrightnessSensorController2;
        float clampScreenBrightness;
        float f4;
        boolean z10;
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
                updatePendingProximityRequestsLocked();
                this.mPendingRequestChangedLocked = false;
                i = 3;
                b = true;
            } else {
                if (this.mPendingRequestChangedLocked) {
                    i = displayPowerRequest3.policy;
                    displayPowerRequest3.copyFrom(displayPowerRequest2);
                    updatePendingProximityRequestsLocked();
                    this.mPendingRequestChangedLocked = false;
                    this.mDisplayReadyLocked = false;
                } else {
                    i = displayPowerRequest3.policy;
                }
                b = false;
            }
            boolean z11 = !this.mDisplayReadyLocked;
            SparseArray<DisplayPowerControllerInterface> clone = this.mDisplayBrightnessFollowers.clone();
            DisplayManagerInternal.DisplayPowerRequest displayPowerRequest4 = this.mPowerRequest;
            int i11 = displayPowerRequest4.policy;
            if (i11 == 0) {
                i2 = 1;
                z = true;
            } else if (i11 != 1) {
                z = false;
                i2 = 2;
            } else {
                i2 = displayPowerRequest4.dozeScreenState;
                if (i2 == 0) {
                    DisplayPowerState displayPowerState = this.mPowerState;
                    i2 = displayPowerState != null ? this.mDpcExt.getDozeScreenStateWhenUnKnown(this.mDisplayId, displayPowerState.getScreenState()) : 3;
                }
                if (this.mAllowAutoBrightnessWhileDozingConfig) {
                    z = false;
                } else {
                    f = this.mPowerRequest.dozeScreenBrightness;
                    this.mBrightnessReasonTemp.setReason(2);
                    z = false;
                    screenOffBrightnessSensorController = this.mScreenOffBrightnessSensorController;
                    if (screenOffBrightnessSensorController != null) {
                        screenOffBrightnessSensorController.setLightSensorEnabled(this.mUseAutoBrightness && this.mIsEnabled && (i2 == 1 || (i2 == 3 && !this.mAllowAutoBrightnessWhileDozingConfig)) && this.mLeadDisplayId == -1);
                    }
                    if (!this.mDpcExt.applyOplusProximitySensorLocked(this.mPowerRequest, this.mProximity, this.mProximitySensorEnabled, this.mWaitingForNegativeProximity, this.mScreenOffBecauseOfProximity, i2, this.mProximitySensor == null, this.mDisplayId)) {
                        if (this.mProximitySensor != null) {
                            if (this.mPowerRequest.useProximitySensor && i2 != 1) {
                                setProximitySensorEnabled(true);
                                if (!this.mScreenOffBecauseOfProximity && this.mProximity == 1 && !this.mIgnoreProximityUntilChanged) {
                                    this.mScreenOffBecauseOfProximity = true;
                                    sendOnProximityPositiveWithWakelock();
                                }
                            } else if (this.mWaitingForNegativeProximity && this.mScreenOffBecauseOfProximity && this.mProximity == 1 && i2 != 1) {
                                setProximitySensorEnabled(true);
                            } else {
                                z10 = false;
                                setProximitySensorEnabled(false);
                                this.mWaitingForNegativeProximity = false;
                                if (this.mScreenOffBecauseOfProximity && (this.mProximity != 1 || this.mIgnoreProximityUntilChanged)) {
                                    this.mScreenOffBecauseOfProximity = z10;
                                    sendOnProximityNegativeWithWakelock();
                                }
                            }
                            z10 = false;
                            if (this.mScreenOffBecauseOfProximity) {
                                this.mScreenOffBecauseOfProximity = z10;
                                sendOnProximityNegativeWithWakelock();
                            }
                        } else {
                            setProximitySensorEnabled(false);
                            this.mWaitingForNegativeProximity = false;
                            this.mIgnoreProximityUntilChanged = false;
                            if (this.mScreenOffBecauseOfProximity) {
                                this.mScreenOffBecauseOfProximity = false;
                                sendOnProximityNegativeWithWakelock();
                            }
                        }
                    }
                    if (this.mIsEnabled || this.mIsInTransition || this.mLogicalDisplay.getLogicalDisplayExt().isSecondaryDisplayEnabled() || this.mDpcExt.shouldIgnoreDoze(i2) || (this.mScreenOffBecauseOfProximity && !this.mDpcExt.getUseProximityForceSuspendState(this.mDisplayId))) {
                        if (DEBUG_PANIC) {
                            Slog.i(this.mTag, "logicalDisplay enable=" + this.mLogicalDisplay.isEnabledLocked() + " transition=" + this.mIsInTransition + " id=" + this.mUniqueDisplayId);
                        }
                        i2 = 1;
                    }
                    if (this.mScreenOffBecauseOfProximity && !this.mDpcExt.getUseProximityForceSuspendState(this.mDisplayId)) {
                        this.mDpcExt.cancelPwkBecauseProximity();
                    }
                    if (b != false) {
                        initialize(readyToUpdateDisplayState() ? i2 : 0);
                    }
                    int screenState2 = this.mPowerState.getScreenState();
                    iColorAutomaticBrightnessController = this.mAutomaticBrightnessController;
                    if (iColorAutomaticBrightnessController != null) {
                        iColorAutomaticBrightnessController.setPowerRequest(this.mPowerRequest);
                    }
                    this.mDpcExt.animateLongTakeStateChange(this.mPowerRequest, screenState2, i2, this.mDisplayId);
                    animateScreenStateChange(i2, z);
                    screenState = this.mPowerState.getScreenState();
                    if (screenState == 1) {
                        this.mBrightnessReasonTemp.setReason(5);
                        f = -1.0f;
                    }
                    if (Float.isNaN(f) && isValidBrightnessValue(this.mBrightnessToFollow)) {
                        f = this.mBrightnessToFollow;
                        this.mBrightnessReasonTemp.setReason(10);
                    }
                    if (!Float.isNaN(f) && isValidBrightnessValue(this.mPowerRequest.screenBrightnessOverride)) {
                        f = this.mPowerRequest.screenBrightnessOverride;
                        if (!Float.isNaN(this.mOplusOverriedBrightness)) {
                            if (f == this.mOplusLastOverriedBrightness) {
                                f = this.mOplusOverriedBrightness;
                            } else {
                                this.mOplusOverriedBrightness = Float.NaN;
                                this.mOplusLastOverriedBrightness = Float.NaN;
                            }
                        }
                        this.mBrightnessReasonTemp.setReason(6);
                        this.mAppliedScreenBrightnessOverride = true;
                        this.mDpcExt.updateScreenBrightnessOverride(f, true);
                        this.mDpcExt.setWinOverride(true);
                    } else {
                        this.mAppliedScreenBrightnessOverride = false;
                        this.mOplusOverriedBrightness = Float.NaN;
                        this.mOplusLastOverriedBrightness = Float.NaN;
                        this.mDpcExt.setWinOverride(false);
                    }
                    z2 = !this.mUseAutoBrightness && (screenState == 2 || (!this.mAllowAutoBrightnessWhileDozingConfig && Display.isDozeState(screenState))) && Float.isNaN(f) && this.mAutomaticBrightnessController != null && this.mBrightnessReasonTemp.getReason() != 10;
                    if (this.mAutomaticBrightnessController != null && this.mDpcExt.hasRemapDisable()) {
                        z2 = this.mDpcExt.updateAutoBrightnessEnabled(this.mWrapper.getLogicalDisplayMapper(), this.mUseAutoBrightness, screenState, this.mPowerRequest);
                    }
                    boolean z12 = z2;
                    if (this.mUseAutoBrightness) {
                    }
                    updateUserSetScreenBrightness = updateUserSetScreenBrightness();
                    if (!isValidBrightnessValue(this.mTemporaryScreenBrightness)) {
                        if (this.mAppliedScreenBrightnessOverride && isValidBrightnessValue(this.mPowerRequest.screenBrightnessOverride)) {
                            this.mOplusOverriedBrightness = this.mTemporaryScreenBrightness;
                            this.mOplusLastOverriedBrightness = this.mPowerRequest.screenBrightnessOverride;
                        }
                        this.mDpcExt.setByUser(true);
                        f = this.mTemporaryScreenBrightness;
                        this.mAppliedTemporaryBrightness = true;
                        this.mBrightnessReasonTemp.setReason(7);
                        z3 = false;
                    } else {
                        z3 = false;
                        this.mAppliedTemporaryBrightness = false;
                        this.mDpcExt.setByUser(false);
                    }
                    boolean updateAutoBrightnessAdjustment = updateAutoBrightnessAdjustment();
                    if (Float.isNaN(this.mTemporaryAutoBrightnessAdjustment)) {
                        float f5 = this.mTemporaryAutoBrightnessAdjustment;
                        this.mAppliedTemporaryAutoBrightnessAdjustment = true;
                        i3 = 1;
                        f2 = f5;
                    } else {
                        float f6 = this.mAutoBrightnessAdjustment;
                        this.mAppliedTemporaryAutoBrightnessAdjustment = z3;
                        f2 = f6;
                        i3 = 2;
                    }
                    if (!this.mPowerRequest.boostScreenBrightness && f != -1.0f) {
                        f = this.mPowerManager.getMaxBrightness();
                        this.mBrightnessReasonTemp.setReason(8);
                        this.mAppliedBrightnessBoost = true;
                    } else {
                        this.mAppliedBrightnessBoost = false;
                    }
                    boolean z13 = !Float.isNaN(f) && (updateAutoBrightnessAdjustment || updateUserSetScreenBrightness);
                    if (this.mAutomaticBrightnessController != null) {
                        this.mDpcExt.configure(this.mIsPrimaryDisplay, z12, f2, screenState != 2, z13, this.mDisplayId, screenState);
                    }
                    this.mHbmController.setAutoBrightnessEnabled(!this.mUseAutoBrightness ? 1 : 2);
                    brightnessTracker = this.mBrightnessTracker;
                    if (brightnessTracker != null) {
                        BrightnessConfiguration brightnessConfiguration = this.mBrightnessConfiguration;
                        brightnessTracker.setShouldCollectColorSample(brightnessConfiguration != null && brightnessConfiguration.shouldCollectColorSamples());
                    }
                    if (!Float.isNaN(f)) {
                        if (z12) {
                            f = this.mDpcExt.getAutomaticScreenBrightness();
                            f3 = this.mAutomaticBrightnessController.getRawAutomaticScreenBrightness();
                            f4 = this.mAutomaticBrightnessController.getAutomaticScreenBrightnessAdjustment();
                        } else {
                            f3 = f;
                            f4 = f2;
                        }
                        if (isValidBrightnessValue(f) || f == -1.0f) {
                            f = clampScreenBrightness(f);
                            z4 = this.mAppliedAutoBrightness && !updateAutoBrightnessAdjustment;
                            this.mAppliedAutoBrightness = true;
                            this.mBrightnessReasonTemp.setReason(4);
                            ScreenOffBrightnessSensorController screenOffBrightnessSensorController3 = this.mScreenOffBrightnessSensorController;
                            if (screenOffBrightnessSensorController3 != null) {
                                screenOffBrightnessSensorController3.setLightSensorEnabled(false);
                            }
                        } else {
                            this.mAppliedAutoBrightness = false;
                            z4 = false;
                        }
                        if (f2 == f4) {
                            i3 = 0;
                        }
                        i4 = i3;
                    } else {
                        float clampScreenBrightness2 = screenState != 1 ? clampScreenBrightness(f) : f;
                        this.mAppliedAutoBrightness = false;
                        z4 = false;
                        i4 = 0;
                        float f7 = clampScreenBrightness2;
                        f3 = f;
                        f = f7;
                    }
                    currentScreenBrightnessSetting = this.mDpcExt.getCurrentScreenBrightnessSetting(this.mDisplayId, this.mCurrentScreenBrightnessSetting);
                    screenNormalMaxBrightness = this.mDpcExt.getScreenNormalMaxBrightness();
                    if (Float.isNaN(f) && currentScreenBrightnessSetting >= screenNormalMaxBrightness) {
                        clampScreenBrightness = clampScreenBrightness(currentScreenBrightnessSetting);
                        if (this.mAutomaticBrightnessController != null) {
                            clampScreenBrightness = this.mDpcExt.getAIBrightness();
                        }
                        if (clampScreenBrightness < screenNormalMaxBrightness) {
                            f = clampScreenBrightness(clampScreenBrightness);
                        } else if (!z12 && clampScreenBrightness < screenNormalMaxBrightness) {
                            f = screenNormalMaxBrightness;
                        }
                    }
                    if (Float.isNaN(f) && Display.isDozeState(screenState)) {
                        f3 = this.mScreenBrightnessDozeConfig;
                        f = clampScreenBrightness(f3);
                        this.mBrightnessReasonTemp.setReason(3);
                    }
                    if (Float.isNaN(f) && z12 && (screenOffBrightnessSensorController2 = this.mScreenOffBrightnessSensorController) != null) {
                        f = screenOffBrightnessSensorController2.getAutomaticScreenBrightness();
                        if (isValidBrightnessValue(f)) {
                            f3 = f;
                        } else {
                            float clampScreenBrightness3 = clampScreenBrightness(f);
                            this.mBrightnessReasonTemp.setReason(9);
                            f3 = f;
                            f = clampScreenBrightness3;
                        }
                    }
                    if (Float.isNaN(f)) {
                        IColorAutomaticBrightnessController iColorAutomaticBrightnessController2 = this.mAutomaticBrightnessController;
                        if (iColorAutomaticBrightnessController2 != null && !iColorAutomaticBrightnessController2.isCtsTest()) {
                            currentScreenBrightnessSetting = clampScreenBrightness(currentScreenBrightnessSetting);
                        }
                        if (Math.abs(this.mCurrentScreenBrightnessSetting) <= FLOAT_EPSINON && !Float.isNaN(this.mTemporaryCurrentScreenBrightness)) {
                            Slog.d(this.mTag, "currentSetting=" + this.mCurrentScreenBrightnessSetting + " tmpCurrentSetting=" + this.mTemporaryCurrentScreenBrightness);
                            this.mCurrentScreenBrightnessSetting = this.mTemporaryCurrentScreenBrightness;
                            this.mTemporaryCurrentScreenBrightness = Float.NaN;
                        }
                        this.mBrightnessReasonTemp.setReason(1);
                        f = currentScreenBrightnessSetting;
                    }
                    if (!this.mBrightnessThrottler.isThrottled()) {
                        this.mTempBrightnessEvent.setThermalMax(this.mBrightnessThrottler.getBrightnessCap());
                        f = Math.min(f, this.mBrightnessThrottler.getBrightnessCap());
                        this.mBrightnessReasonTemp.addModifier(8);
                        if (!this.mAppliedThrottling) {
                            z4 = false;
                        }
                        this.mAppliedThrottling = true;
                    } else if (this.mAppliedThrottling) {
                        this.mAppliedThrottling = false;
                    }
                    IColorAutomaticBrightnessController iColorAutomaticBrightnessController3 = this.mAutomaticBrightnessController;
                    float ambientLux = iColorAutomaticBrightnessController3 != null ? SCREEN_ANIMATION_RATE_MINIMUM : iColorAutomaticBrightnessController3.getAmbientLux();
                    for (i5 = 0; i5 < clone.size(); i5++) {
                        clone.valueAt(i5).setBrightnessToFollow(f3, convertToNits(f3), ambientLux);
                    }
                    if (this.mPowerRequest.policy != 2) {
                        if (f > this.mScreenBrightnessRangeMinimum) {
                            float applydimmingbrightness = this.mDpcExt.applydimmingbrightness((int) f);
                            this.mBrightnessReasonTemp.addModifier(1);
                            f = applydimmingbrightness;
                        }
                        if (!this.mAppliedDimming) {
                            this.mDpcExt.pokeDynamicVsyncAnimation(6000, "SCREEN DIM");
                            z4 = false;
                        }
                        this.mAppliedDimming = true;
                        this.mDpcExt.setDimRateType();
                    } else if (this.mAppliedDimming) {
                        this.mAppliedDimming = false;
                        this.mDpcExt.recoverOriginRateType();
                        z4 = false;
                    }
                    displayPowerRequest = this.mPowerRequest;
                    if (!displayPowerRequest.lowPowerMode) {
                        if (f > this.mScreenBrightnessRangeMinimum) {
                            f = Math.max(this.mDpcExt.getLowPowerModeBtnExp(f, Math.min(displayPowerRequest.screenLowPowerBrightnessFactor, 1.0f)), this.mScreenBrightnessRangeMinimum);
                            this.mBrightnessReasonTemp.addModifier(2);
                        }
                        if (!this.mAppliedLowPower) {
                            this.mDpcExt.setLowPowerAnimatingState(true);
                            z4 = false;
                        }
                        this.mAppliedLowPower = true;
                    } else if (this.mAppliedLowPower) {
                        this.mAppliedLowPower = false;
                        this.mDpcExt.setLowPowerAnimatingState(true);
                        z5 = false;
                        this.mDpcExt.setSavePowerMode(this.mAppliedLowPower ? 1 : 0);
                        boolean z14 = !this.mAppliedTemporaryBrightness || this.mAppliedTemporaryAutoBrightnessAdjustment;
                        if (!this.mPendingScreenOff) {
                            if (this.mSkipScreenOnBrightnessRamp) {
                                if (screenState == 2) {
                                    int i12 = this.mSkipRampState;
                                    if (i12 == 0 && this.mDozing) {
                                        this.mInitialAutoBrightness = f;
                                        this.mSkipRampState = 1;
                                    } else if (i12 == 1 && this.mUseSoftwareAutoBrightnessConfig && !BrightnessSynchronizer.floatEquals(f, this.mInitialAutoBrightness)) {
                                        this.mSkipRampState = 2;
                                    } else if (this.mSkipRampState == 2) {
                                        this.mSkipRampState = 0;
                                    }
                                } else {
                                    this.mSkipRampState = 0;
                                }
                            }
                            boolean z15 = screenState == 5 || screenState2 == 5;
                            if ((!(screenState == 2 && this.mSkipRampState == 0) && (screenState != 3 || this.mBrightnessBucketsInDozeConfig)) || z15) {
                                i6 = i2;
                                i7 = i4;
                                i8 = 4;
                                i9 = 8;
                            } else {
                                i7 = i4;
                                i6 = i2;
                                i8 = 4;
                                i9 = 8;
                                this.mDpcExt.handleDisplayChanged(z5, z12, (int) f, screenState, this.mDisplayId, this.mBrightnessReasonTemp.getReason());
                            }
                            saveBrightnessInfo = false;
                        } else {
                            i6 = i2;
                            i7 = i4;
                            i8 = 4;
                            i9 = 8;
                            saveBrightnessInfo = saveBrightnessInfo(getScreenBrightnessSetting());
                        }
                        if (saveBrightnessInfo && !z14) {
                            postBrightnessChangeRunnable();
                        }
                        if ((this.mBrightnessReasonTemp.equals(this.mBrightnessReason) || i7 != 0) && DEBUG_PANIC) {
                            Slog.d(this.mTag, "Brightness [" + f + "] reason changing to: '" + this.mBrightnessReasonTemp.toString(i7) + "', previous reason: '" + this.mBrightnessReason + "'. id=" + this.mUniqueDisplayId);
                            this.mBrightnessReason.set(this.mBrightnessReasonTemp);
                        } else if (this.mBrightnessReasonTemp.getReason() == 1 && updateUserSetScreenBrightness) {
                            Slog.d(this.mTag, "Brightness [" + f + "] manual adjustment. id=" + this.mUniqueDisplayId);
                        }
                        this.mTempBrightnessEvent.setTime(System.currentTimeMillis());
                        this.mTempBrightnessEvent.setBrightness(f);
                        this.mTempBrightnessEvent.setPhysicalDisplayId(this.mUniqueDisplayId);
                        this.mTempBrightnessEvent.setReason(this.mBrightnessReason);
                        this.mTempBrightnessEvent.setHbmMax(this.mHbmController.getCurrentBrightnessMax());
                        this.mTempBrightnessEvent.setHbmMode(this.mHbmController.getHighBrightnessMode());
                        BrightnessEvent brightnessEvent = this.mTempBrightnessEvent;
                        brightnessEvent.setFlags(brightnessEvent.getFlags() | (this.mIsRbcActive ? 1 : 0) | (this.mPowerRequest.lowPowerMode ? 32 : 0));
                        BrightnessEvent brightnessEvent2 = this.mTempBrightnessEvent;
                        ColorDisplayService.ColorDisplayServiceInternal colorDisplayServiceInternal = this.mCdsi;
                        brightnessEvent2.setRbcStrength(colorDisplayServiceInternal != null ? colorDisplayServiceInternal.getReduceBrightColorsStrength() : -1);
                        this.mTempBrightnessEvent.setPowerFactor(this.mPowerRequest.screenLowPowerBrightnessFactor);
                        this.mTempBrightnessEvent.setWasShortTermModelActive(false);
                        this.mTempBrightnessEvent.setAutomaticBrightnessEnabled(this.mUseAutoBrightness);
                        boolean z16 = this.mTempBrightnessEvent.getReason().getReason() != 7 && this.mLastBrightnessEvent.getReason().getReason() == 7;
                        if ((!this.mTempBrightnessEvent.equalsMainData(this.mLastBrightnessEvent) && !z16) || i7 != 0) {
                            this.mTempBrightnessEvent.setInitialBrightness(this.mLastBrightnessEvent.getBrightness());
                            this.mLastBrightnessEvent.copyFrom(this.mTempBrightnessEvent);
                            BrightnessEvent brightnessEvent3 = new BrightnessEvent(this.mTempBrightnessEvent);
                            brightnessEvent3.setAdjustmentFlags(i7);
                            int flags = brightnessEvent3.getFlags();
                            if (!updateUserSetScreenBrightness) {
                                i9 = 0;
                            }
                            brightnessEvent3.setFlags(flags | i9);
                            if (DEBUG_PANIC) {
                                Slog.i(this.mTag, brightnessEvent3.toString(false));
                            }
                            ringBuffer = this.mBrightnessEventRingBuffer;
                            if (ringBuffer != null) {
                                ringBuffer.append(brightnessEvent3);
                            }
                        }
                        if (this.mDisplayWhiteBalanceController != null) {
                            if (screenState == 2 && this.mDisplayWhiteBalanceSettings.isEnabled()) {
                                this.mDisplayWhiteBalanceController.setEnabled(true);
                                this.mDisplayWhiteBalanceController.updateDisplayColorTemperature();
                            } else {
                                this.mDisplayWhiteBalanceController.setEnabled(false);
                            }
                        }
                        z6 = (this.mPendingScreenOnUnblocker == null || (this.mColorFadeEnabled && (this.mColorFadeOnAnimator.isStarted() || this.mColorFadeOffAnimator.isStarted())) || this.mDpcExt.isBlockDisplayByBiometrics() || !this.mPowerState.waitUntilClean(this.mCleanListener)) ? false : true;
                        z7 = (z6 || this.mScreenBrightnessRampAnimator.isAnimating()) ? false : true;
                        if (z6 && screenState != 1 && screenState != 3 && screenState != i8 && this.mReportedScreenStateToPolicy == 1) {
                            setReportedScreenState(2);
                            this.mWindowManagerPolicy.screenTurnedOn(this.mDisplayId);
                            Slog.d(this.mTag, "screenTurnedOn state=" + screenState + " brightnessState=" + f + " mustNotify=" + z11);
                        }
                        if (!z7 && !this.mUnfinishedBusiness) {
                            if (DEBUG) {
                                Slog.d(this.mTag, "Unfinished business...");
                            }
                            this.mCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdUnfinishedBusiness);
                            this.mUnfinishedBusiness = true;
                        }
                        if ((!DEBUG_PANIC || DEBUG) && z6 && z11) {
                            Slog.d(this.mTag, "updatePowerState: ready = " + z6 + ", mustNotify = " + z11 + ", state = " + screenState);
                        }
                        if (z6 || !z11) {
                            z8 = true;
                        } else {
                            this.mDpcExt.onUpdatePowerState(screenState, this.mPowerRequest.policy, (int) f);
                            synchronized (this.mLock) {
                                if (this.mPendingRequestChangedLocked) {
                                    z8 = true;
                                } else {
                                    z8 = true;
                                    this.mDisplayReadyLocked = true;
                                    if (DEBUG) {
                                        Slog.d(this.mTag, "Display ready!");
                                    }
                                }
                            }
                            sendOnStateChangedWithWakelock();
                        }
                        if (z7 || !this.mUnfinishedBusiness) {
                            z9 = false;
                        } else {
                            if (DEBUG) {
                                Slog.d(this.mTag, "Finished business...");
                            }
                            z9 = false;
                            this.mUnfinishedBusiness = false;
                            this.mCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdUnfinishedBusiness);
                        }
                        if (screenState != 2) {
                            z9 = z8;
                        }
                        this.mDozing = z9;
                        i10 = this.mPowerRequest.policy;
                        if (i == i10) {
                            logDisplayPolicyChanged(i10);
                            String format = String.format("policy:%s->%s,state:%s->%s", DisplayManagerInternal.DisplayPowerRequest.policyToString(i), DisplayManagerInternal.DisplayPowerRequest.policyToString(this.mPowerRequest.policy), Display.stateToString(screenState2), Display.stateToString(i6));
                            Slog.d(this.mTag, "updatePowerStateInternal " + format + " " + this.mPowerRequest);
                            return;
                        }
                        return;
                    }
                    z5 = z4;
                    this.mDpcExt.setSavePowerMode(this.mAppliedLowPower ? 1 : 0);
                    if (this.mAppliedTemporaryBrightness) {
                    }
                    if (!this.mPendingScreenOff) {
                    }
                    if (saveBrightnessInfo) {
                        postBrightnessChangeRunnable();
                    }
                    if (this.mBrightnessReasonTemp.equals(this.mBrightnessReason)) {
                    }
                    Slog.d(this.mTag, "Brightness [" + f + "] reason changing to: '" + this.mBrightnessReasonTemp.toString(i7) + "', previous reason: '" + this.mBrightnessReason + "'. id=" + this.mUniqueDisplayId);
                    this.mBrightnessReason.set(this.mBrightnessReasonTemp);
                    this.mTempBrightnessEvent.setTime(System.currentTimeMillis());
                    this.mTempBrightnessEvent.setBrightness(f);
                    this.mTempBrightnessEvent.setPhysicalDisplayId(this.mUniqueDisplayId);
                    this.mTempBrightnessEvent.setReason(this.mBrightnessReason);
                    this.mTempBrightnessEvent.setHbmMax(this.mHbmController.getCurrentBrightnessMax());
                    this.mTempBrightnessEvent.setHbmMode(this.mHbmController.getHighBrightnessMode());
                    BrightnessEvent brightnessEvent4 = this.mTempBrightnessEvent;
                    brightnessEvent4.setFlags(brightnessEvent4.getFlags() | (this.mIsRbcActive ? 1 : 0) | (this.mPowerRequest.lowPowerMode ? 32 : 0));
                    BrightnessEvent brightnessEvent22 = this.mTempBrightnessEvent;
                    ColorDisplayService.ColorDisplayServiceInternal colorDisplayServiceInternal2 = this.mCdsi;
                    brightnessEvent22.setRbcStrength(colorDisplayServiceInternal2 != null ? colorDisplayServiceInternal2.getReduceBrightColorsStrength() : -1);
                    this.mTempBrightnessEvent.setPowerFactor(this.mPowerRequest.screenLowPowerBrightnessFactor);
                    this.mTempBrightnessEvent.setWasShortTermModelActive(false);
                    this.mTempBrightnessEvent.setAutomaticBrightnessEnabled(this.mUseAutoBrightness);
                    if (this.mTempBrightnessEvent.getReason().getReason() != 7) {
                    }
                    if (!this.mTempBrightnessEvent.equalsMainData(this.mLastBrightnessEvent)) {
                        this.mTempBrightnessEvent.setInitialBrightness(this.mLastBrightnessEvent.getBrightness());
                        this.mLastBrightnessEvent.copyFrom(this.mTempBrightnessEvent);
                        BrightnessEvent brightnessEvent32 = new BrightnessEvent(this.mTempBrightnessEvent);
                        brightnessEvent32.setAdjustmentFlags(i7);
                        int flags2 = brightnessEvent32.getFlags();
                        if (!updateUserSetScreenBrightness) {
                        }
                        brightnessEvent32.setFlags(flags2 | i9);
                        if (DEBUG_PANIC) {
                        }
                        ringBuffer = this.mBrightnessEventRingBuffer;
                        if (ringBuffer != null) {
                        }
                        if (this.mDisplayWhiteBalanceController != null) {
                        }
                        if (this.mPendingScreenOnUnblocker == null) {
                        }
                        if (z6) {
                        }
                        if (z6) {
                            setReportedScreenState(2);
                            this.mWindowManagerPolicy.screenTurnedOn(this.mDisplayId);
                            Slog.d(this.mTag, "screenTurnedOn state=" + screenState + " brightnessState=" + f + " mustNotify=" + z11);
                        }
                        if (!z7) {
                            if (DEBUG) {
                            }
                            this.mCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdUnfinishedBusiness);
                            this.mUnfinishedBusiness = true;
                        }
                        if (!DEBUG_PANIC) {
                        }
                        Slog.d(this.mTag, "updatePowerState: ready = " + z6 + ", mustNotify = " + z11 + ", state = " + screenState);
                        if (z6) {
                        }
                        z8 = true;
                        if (z7) {
                        }
                        z9 = false;
                        if (screenState != 2) {
                        }
                        this.mDozing = z9;
                        i10 = this.mPowerRequest.policy;
                        if (i == i10) {
                        }
                    }
                    this.mTempBrightnessEvent.setInitialBrightness(this.mLastBrightnessEvent.getBrightness());
                    this.mLastBrightnessEvent.copyFrom(this.mTempBrightnessEvent);
                    BrightnessEvent brightnessEvent322 = new BrightnessEvent(this.mTempBrightnessEvent);
                    brightnessEvent322.setAdjustmentFlags(i7);
                    int flags22 = brightnessEvent322.getFlags();
                    if (!updateUserSetScreenBrightness) {
                    }
                    brightnessEvent322.setFlags(flags22 | i9);
                    if (DEBUG_PANIC) {
                    }
                    ringBuffer = this.mBrightnessEventRingBuffer;
                    if (ringBuffer != null) {
                    }
                    if (this.mDisplayWhiteBalanceController != null) {
                    }
                    if (this.mPendingScreenOnUnblocker == null) {
                    }
                    if (z6) {
                    }
                    if (z6) {
                    }
                    if (!z7) {
                    }
                    if (!DEBUG_PANIC) {
                    }
                    Slog.d(this.mTag, "updatePowerState: ready = " + z6 + ", mustNotify = " + z11 + ", state = " + screenState);
                    if (z6) {
                    }
                    z8 = true;
                    if (z7) {
                    }
                    z9 = false;
                    if (screenState != 2) {
                    }
                    this.mDozing = z9;
                    i10 = this.mPowerRequest.policy;
                    if (i == i10) {
                    }
                }
            }
            f = Float.NaN;
            screenOffBrightnessSensorController = this.mScreenOffBrightnessSensorController;
            if (screenOffBrightnessSensorController != null) {
            }
            if (!this.mDpcExt.applyOplusProximitySensorLocked(this.mPowerRequest, this.mProximity, this.mProximitySensorEnabled, this.mWaitingForNegativeProximity, this.mScreenOffBecauseOfProximity, i2, this.mProximitySensor == null, this.mDisplayId)) {
            }
            if (this.mIsEnabled) {
            }
            if (DEBUG_PANIC) {
            }
            i2 = 1;
            if (this.mScreenOffBecauseOfProximity) {
                this.mDpcExt.cancelPwkBecauseProximity();
            }
            if (b != false) {
            }
            int screenState22 = this.mPowerState.getScreenState();
            iColorAutomaticBrightnessController = this.mAutomaticBrightnessController;
            if (iColorAutomaticBrightnessController != null) {
            }
            this.mDpcExt.animateLongTakeStateChange(this.mPowerRequest, screenState22, i2, this.mDisplayId);
            animateScreenStateChange(i2, z);
            screenState = this.mPowerState.getScreenState();
            if (screenState == 1) {
            }
            if (Float.isNaN(f)) {
                f = this.mBrightnessToFollow;
                this.mBrightnessReasonTemp.setReason(10);
            }
            if (!Float.isNaN(f)) {
            }
            this.mAppliedScreenBrightnessOverride = false;
            this.mOplusOverriedBrightness = Float.NaN;
            this.mOplusLastOverriedBrightness = Float.NaN;
            this.mDpcExt.setWinOverride(false);
            if (this.mUseAutoBrightness) {
            }
            if (this.mAutomaticBrightnessController != null) {
                z2 = this.mDpcExt.updateAutoBrightnessEnabled(this.mWrapper.getLogicalDisplayMapper(), this.mUseAutoBrightness, screenState, this.mPowerRequest);
            }
            boolean z122 = z2;
            if (this.mUseAutoBrightness) {
            }
            updateUserSetScreenBrightness = updateUserSetScreenBrightness();
            if (!isValidBrightnessValue(this.mTemporaryScreenBrightness)) {
            }
            boolean updateAutoBrightnessAdjustment2 = updateAutoBrightnessAdjustment();
            if (Float.isNaN(this.mTemporaryAutoBrightnessAdjustment)) {
            }
            if (!this.mPowerRequest.boostScreenBrightness) {
            }
            this.mAppliedBrightnessBoost = false;
            if (Float.isNaN(f)) {
            }
            if (this.mAutomaticBrightnessController != null) {
            }
            this.mHbmController.setAutoBrightnessEnabled(!this.mUseAutoBrightness ? 1 : 2);
            brightnessTracker = this.mBrightnessTracker;
            if (brightnessTracker != null) {
            }
            if (!Float.isNaN(f)) {
            }
            currentScreenBrightnessSetting = this.mDpcExt.getCurrentScreenBrightnessSetting(this.mDisplayId, this.mCurrentScreenBrightnessSetting);
            screenNormalMaxBrightness = this.mDpcExt.getScreenNormalMaxBrightness();
            if (Float.isNaN(f)) {
                clampScreenBrightness = clampScreenBrightness(currentScreenBrightnessSetting);
                if (this.mAutomaticBrightnessController != null) {
                }
                if (clampScreenBrightness < screenNormalMaxBrightness) {
                }
            }
            if (Float.isNaN(f)) {
                f3 = this.mScreenBrightnessDozeConfig;
                f = clampScreenBrightness(f3);
                this.mBrightnessReasonTemp.setReason(3);
            }
            if (Float.isNaN(f)) {
                f = screenOffBrightnessSensorController2.getAutomaticScreenBrightness();
                if (isValidBrightnessValue(f)) {
                }
            }
            if (Float.isNaN(f)) {
            }
            if (!this.mBrightnessThrottler.isThrottled()) {
            }
            IColorAutomaticBrightnessController iColorAutomaticBrightnessController32 = this.mAutomaticBrightnessController;
            if (iColorAutomaticBrightnessController32 != null) {
            }
            while (i5 < clone.size()) {
            }
            if (this.mPowerRequest.policy != 2) {
            }
            displayPowerRequest = this.mPowerRequest;
            if (!displayPowerRequest.lowPowerMode) {
            }
            z5 = z4;
            this.mDpcExt.setSavePowerMode(this.mAppliedLowPower ? 1 : 0);
            if (this.mAppliedTemporaryBrightness) {
            }
            if (!this.mPendingScreenOff) {
            }
            if (saveBrightnessInfo) {
            }
            if (this.mBrightnessReasonTemp.equals(this.mBrightnessReason)) {
            }
            Slog.d(this.mTag, "Brightness [" + f + "] reason changing to: '" + this.mBrightnessReasonTemp.toString(i7) + "', previous reason: '" + this.mBrightnessReason + "'. id=" + this.mUniqueDisplayId);
            this.mBrightnessReason.set(this.mBrightnessReasonTemp);
            this.mTempBrightnessEvent.setTime(System.currentTimeMillis());
            this.mTempBrightnessEvent.setBrightness(f);
            this.mTempBrightnessEvent.setPhysicalDisplayId(this.mUniqueDisplayId);
            this.mTempBrightnessEvent.setReason(this.mBrightnessReason);
            this.mTempBrightnessEvent.setHbmMax(this.mHbmController.getCurrentBrightnessMax());
            this.mTempBrightnessEvent.setHbmMode(this.mHbmController.getHighBrightnessMode());
            BrightnessEvent brightnessEvent42 = this.mTempBrightnessEvent;
            brightnessEvent42.setFlags(brightnessEvent42.getFlags() | (this.mIsRbcActive ? 1 : 0) | (this.mPowerRequest.lowPowerMode ? 32 : 0));
            BrightnessEvent brightnessEvent222 = this.mTempBrightnessEvent;
            ColorDisplayService.ColorDisplayServiceInternal colorDisplayServiceInternal22 = this.mCdsi;
            brightnessEvent222.setRbcStrength(colorDisplayServiceInternal22 != null ? colorDisplayServiceInternal22.getReduceBrightColorsStrength() : -1);
            this.mTempBrightnessEvent.setPowerFactor(this.mPowerRequest.screenLowPowerBrightnessFactor);
            this.mTempBrightnessEvent.setWasShortTermModelActive(false);
            this.mTempBrightnessEvent.setAutomaticBrightnessEnabled(this.mUseAutoBrightness);
            if (this.mTempBrightnessEvent.getReason().getReason() != 7) {
            }
            if (!this.mTempBrightnessEvent.equalsMainData(this.mLastBrightnessEvent)) {
            }
            this.mTempBrightnessEvent.setInitialBrightness(this.mLastBrightnessEvent.getBrightness());
            this.mLastBrightnessEvent.copyFrom(this.mTempBrightnessEvent);
            BrightnessEvent brightnessEvent3222 = new BrightnessEvent(this.mTempBrightnessEvent);
            brightnessEvent3222.setAdjustmentFlags(i7);
            int flags222 = brightnessEvent3222.getFlags();
            if (!updateUserSetScreenBrightness) {
            }
            brightnessEvent3222.setFlags(flags222 | i9);
            if (DEBUG_PANIC) {
            }
            ringBuffer = this.mBrightnessEventRingBuffer;
            if (ringBuffer != null) {
            }
            if (this.mDisplayWhiteBalanceController != null) {
            }
            if (this.mPendingScreenOnUnblocker == null) {
            }
            if (z6) {
            }
            if (z6) {
            }
            if (!z7) {
            }
            if (!DEBUG_PANIC) {
            }
            Slog.d(this.mTag, "updatePowerState: ready = " + z6 + ", mustNotify = " + z11 + ", state = " + screenState);
            if (z6) {
            }
            z8 = true;
            if (z7) {
            }
            z9 = false;
            if (screenState != 2) {
            }
            this.mDozing = z9;
            i10 = this.mPowerRequest.policy;
            if (i == i10) {
            }
        }
    }

    public float brightnessNonlinearCompensation(float f, float f2) {
        return MathUtils.pow(f2, 0.45454544f) * f;
    }

    public void updateFpsWhenDcChange(boolean z) {
        if (this.mDCBrightnessChange == z) {
            return;
        }
        Slog.d(this.mTag, "debug enter: " + z);
        this.mDpcExt.updateFpsWhenDcChange(z);
        this.mDCBrightnessChange = z;
    }

    public void updateFpsIfNeeded(float f) {
        boolean z = this.mDpcExt.isDCMode() && f < ((float) DC_MODE_BRIGHT_EDGE) && this.mScreenBrightnessRampAnimator.isAnimating();
        if (DC_MODE_BRIGHT_CUSTOMIZATION) {
            if (this.mUpdateFpsForDc != z) {
                if (z) {
                    this.mResetFpsStatePending = false;
                    updateFpsWhenDcChange(true);
                } else {
                    this.mResetFpsStatePending = true;
                    this.mHandler.removeMessages(21);
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(21), 1000L);
                }
            }
            this.mUpdateFpsForDc = z;
        }
    }

    @Override // com.android.server.display.AutomaticBrightnessController.Callbacks
    public void updateBrightness() {
        sendUpdatePowerState();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void ignoreProximitySensorUntilChanged() {
        this.mHandler.sendEmptyMessage(8);
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setBrightnessConfiguration(BrightnessConfiguration brightnessConfiguration, boolean z) {
        this.mHandler.obtainMessage(5, z ? 1 : 0, 0, brightnessConfiguration).sendToTarget();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setTemporaryBrightness(float f) {
        this.mHandler.obtainMessage(6, Float.floatToIntBits(f), 0).sendToTarget();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setTemporaryAutoBrightnessAdjustment(float f) {
        this.mDpcExt.setTemporaryAutoBrightnessAdjustment(f);
        if (f == this.mDpcExt.getAdjustmentGalleryIn() || f == this.mDpcExt.getAdjustmentGalleryOut()) {
            if (!this.mDpcExt.isGalleryBrightnessEnhanceSupport()) {
                return;
            }
            Slog.d(this.mTag, "setTemporaryAutoBrightnessAdjustment=" + f);
        }
        this.mHandler.obtainMessage(7, Float.floatToIntBits(f), 0).sendToTarget();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public BrightnessInfo getBrightnessInfo() {
        return this.mDpcExt.getAccessibilityBrightnessInfo(this.mCurrentScreenBrightnessSetting);
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

    void postBrightnessChangeRunnable() {
        this.mHandler.post(this.mOnBrightnessChangeRunnable);
    }

    private HighBrightnessModeController createHbmControllerLocked() {
        DisplayDevice primaryDisplayDeviceLocked = this.mLogicalDisplay.getPrimaryDisplayDeviceLocked();
        DisplayDeviceConfig displayDeviceConfig = primaryDisplayDeviceLocked.getDisplayDeviceConfig();
        IBinder displayTokenLocked = this.mLogicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayTokenLocked();
        String uniqueId = this.mLogicalDisplay.getPrimaryDisplayDeviceLocked().getUniqueId();
        DisplayDeviceConfig.HighBrightnessModeData highBrightnessModeData = displayDeviceConfig != null ? displayDeviceConfig.getHighBrightnessModeData() : null;
        DisplayDeviceInfo displayDeviceInfoLocked = primaryDisplayDeviceLocked.getDisplayDeviceInfoLocked();
        return new HighBrightnessModeController(this.mHandler, displayDeviceInfoLocked.width, displayDeviceInfoLocked.height, displayTokenLocked, uniqueId, SCREEN_ANIMATION_RATE_MINIMUM, 1.0f, highBrightnessModeData, new HighBrightnessModeController.HdrBrightnessDeviceConfig() { // from class: com.android.server.display.DisplayPowerController.5
            @Override // com.android.server.display.HighBrightnessModeController.HdrBrightnessDeviceConfig
            public float getHdrBrightnessFromSdr(float f, float f2) {
                return DisplayPowerController.this.mDisplayDeviceConfig.getHdrBrightnessFromSdr(f, f2);
            }
        }, new Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPowerController.this.lambda$createHbmControllerLocked$4();
            }
        }, this.mHighBrightnessModeMetadata, this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createHbmControllerLocked$4() {
        sendUpdatePowerState();
        postBrightnessChangeRunnable();
    }

    private BrightnessThrottler createBrightnessThrottlerLocked() {
        return new BrightnessThrottler(this.mHandler, new Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPowerController.this.lambda$createBrightnessThrottlerLocked$5();
            }
        }, this.mUniqueDisplayId, this.mLogicalDisplay.getDisplayInfoLocked().thermalBrightnessThrottlingDataId, this.mLogicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayDeviceConfig().getThermalBrightnessThrottlingDataMapByThrottlingId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createBrightnessThrottlerLocked$5() {
        sendUpdatePowerState();
        postBrightnessChangeRunnable();
    }

    private void blockScreenOn() {
        this.mDpcExt.removeMessageWhenScreenOn(this.mHandler, 3);
        if (this.mPendingScreenOnUnblocker == null) {
            Trace.asyncTraceBegin(131072L, SCREEN_ON_BLOCKED_TRACE_NAME, 0);
            this.mPendingScreenOnUnblocker = new ScreenOnUnblocker();
            this.mScreenOnBlockStartRealTime = SystemClock.elapsedRealtime();
            Slog.i(this.mTag, "Blocking screen on until initial contents have been drawn.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unblockScreenOn() {
        this.mDpcExt.removeMessageWhenScreenOn(this.mHandler, 3);
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
            this.mBrightnessTracker.screenOffAction();
            Trace.asyncTraceEnd(131072L, SCREEN_OFF_BLOCKED_TRACE_NAME, 0);
        }
    }

    private boolean setScreenState(int i) {
        return setScreenState(i, false);
    }

    private boolean setScreenState(int i, boolean z) {
        int i2;
        boolean z2 = i == 1 || i == 3 || i == 4;
        if (this.mPowerState.getScreenState() != i || this.mReportedScreenStateToPolicy == -1) {
            if (z2 && !this.mScreenOffBecauseOfProximity) {
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
            this.mDpcExt.setScreenStateExt(i, this.mPowerState, this.mPowerRequest);
            if (!z && this.mPowerState.getScreenState() != i && readyToUpdateDisplayState()) {
                Trace.traceCounter(131072L, "ScreenState", i);
                this.mDpcExt.handlePwkMonitorForTheia(i, z2);
                try {
                    SystemProperties.set("debug.tracing.screen_state", String.valueOf(i));
                } catch (Exception unused) {
                    Slog.w(this.mTag, "Failed to set property.");
                }
                this.mPowerState.setScreenState(i);
                this.mDpcExt.setScreenState(i);
                noteScreenState(i);
            }
        }
        if (DEBUG_PANIC) {
            Slog.d(this.mTag, "setScreenState: isOff=" + z2 + ", mReportedScreenStateToPolicy=" + this.mReportedScreenStateToPolicy);
        }
        if (z2 && this.mReportedScreenStateToPolicy != 0 && !this.mScreenOffBecauseOfProximity) {
            setReportedScreenState(0);
            unblockScreenOn();
            this.mDpcExt.unblockDisplayReady();
            this.mWindowManagerPolicy.screenTurnedOff(this.mDisplayId, this.mIsInTransition);
        } else if (!z2 && this.mReportedScreenStateToPolicy == 3) {
            unblockScreenOff();
            this.mWindowManagerPolicy.screenTurnedOff(this.mDisplayId, this.mIsInTransition);
            setReportedScreenState(0);
        }
        if (!z2 && ((i2 = this.mReportedScreenStateToPolicy) == 0 || i2 == -1)) {
            setReportedScreenState(1);
            if (DEBUG) {
                Slog.d(this.mTag, "setScreenState: ColorFadeLevel=" + this.mPowerState.getColorFadeLevel());
            }
            if (this.mPowerState.getColorFadeLevel() == SCREEN_ANIMATION_RATE_MINIMUM) {
                blockScreenOn();
            } else {
                unblockScreenOn();
            }
            this.mWindowManagerPolicy.screenTurningOn(this.mDisplayId, this.mPendingScreenOnUnblocker);
        }
        if (this.isRM) {
            resetCabc(i);
        }
        if (this.mPendingScreenOnUnblocker == null) {
            return !this.mDpcExt.isBlockScreenOnByBiometrics() || "optical".equals(FP_SENSOR_TYPE);
        }
        return false;
    }

    private void resetCabc(int i) {
        if (this.mLastState != i && i == 2 && this.mReportedScreenStateToPolicy == 2 && getScreenBrightnessSetting() > SCREEN_ANIMATION_RATE_MINIMUM) {
            this.mLastState = i;
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(22), 100L);
        }
        if (i != 2) {
            this.mLastState = i;
        }
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

    private void loadProximitySensor() {
        if (this.mDisplayId != 0) {
            return;
        }
        Sensor findSensor = SensorUtils.findSensor(this.mSensorManager, this.mDisplayDeviceConfig.getProximitySensor(), 8);
        this.mProximitySensor = findSensor;
        if (findSensor != null) {
            this.mProximityThreshold = Math.min(findSensor.getMaximumRange(), TYPICAL_PROXIMITY_THRESHOLD);
        }
    }

    private float clampScreenBrightness(float f) {
        return this.mDpcExt.hasRemapDisable() ? MathUtils.constrain(f, this.mScreenBrightnessRangeMinimum, this.mScreenBrightnessRangeMaximum) : f;
    }

    private boolean isValidBrightnessValue(float f) {
        return BrightnessSynchronizer.floatCompare(f, (float) this.mScreenBrightnessRangeMinimum, false) && BrightnessSynchronizer.floatCompare(f, (float) this.mScreenBrightnessRangeMaximum, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateScreenBrightness(float f, float f2, float f3) {
        this.mTemporaryCurrentScreenBrightness = f;
        this.mDpcExt.animateScreenBrightness(this.mScreenBrightnessRampAnimator, f, f2, f3, this.mPowerRequest, this.mPowerState);
        if (this.mScreenBrightnessRampAnimator.animateTo(f, f2, f3)) {
            Trace.traceCounter(131072L, "TargetScreenBrightness", (int) f);
            noteScreenBrightness(f);
        }
    }

    private void animateScreenStateChange(int i, boolean z) {
        if (DisplayDisable && i == 2 && this.mDpcExt.isBlockedBySideFingerprint()) {
            Slog.d(this.mTag, "animateScreenStateChange state:" + Display.stateToString(i));
            return;
        }
        if (this.mColorFadeEnabled && (this.mColorFadeOnAnimator.isStarted() || this.mColorFadeOffAnimator.isStarted())) {
            if (i != 2) {
                Slog.d(this.mTag, "animateScreenStateChange animation in progress state:" + Display.stateToString(i));
                return;
            }
            this.mPendingScreenOff = false;
            if (this.mColorFadeOffAnimator.isStarted()) {
                this.mColorFadeOffAnimator.cancel();
            }
        }
        if (this.mDisplayBlanksAfterDozeConfig && Display.isDozeState(this.mPowerState.getScreenState()) && !Display.isDozeState(i) && i != 2) {
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
            if (Display.isDozeState(this.mPowerState.getScreenState()) && this.mPowerState.getColorFadeLevel() == SCREEN_ANIMATION_RATE_MINIMUM) {
                this.mPowerState.setColorFadeLevel(1.0f);
                this.mPowerState.dismissColorFade();
                Slog.d(this.mTag, "animateScreenStateChange target == Display.STATE_ON, current is doze");
            }
            if (!setScreenState(2)) {
                Slog.d(this.mTag, "animateScreenStateChange screen on blocked blocker=" + this.mPendingScreenOnUnblocker);
                return;
            }
            this.mPowerState.setColorFadeLevel(1.0f);
            this.mPowerState.dismissColorFade();
            return;
        }
        if (i == 3) {
            if (this.mScreenBrightnessRampAnimator.isAnimating() && this.mPowerState.getScreenState() == 2) {
                Slog.d(this.mTag, "animateScreenStateChange DOZE isAnimating");
                return;
            } else if (!setScreenState(3)) {
                Slog.d(this.mTag, "animateScreenStateChange DOZE setScreenState");
                return;
            } else {
                this.mPowerState.setColorFadeLevel(1.0f);
                this.mPowerState.dismissColorFade();
                return;
            }
        }
        if (i == 4) {
            if (this.mScreenBrightnessRampAnimator.isAnimating() && this.mPowerState.getScreenState() != 4) {
                Slog.d(this.mTag, "animateScreenStateChange DOZE_SUSPEND isAnimating");
                return;
            }
            if (this.mPowerState.getScreenState() != 4) {
                setScreenState(4);
            }
            this.mPowerState.setColorFadeLevel(1.0f);
            this.mPowerState.dismissColorFade();
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
        boolean isFolding = this.mDpcExt.isFolding();
        if (DEBUG) {
            Slog.d(this.mTag, "isFolding = " + isFolding);
        }
        if (!this.mColorFadeEnabled || isFolding) {
            this.mPowerState.setColorFadeLevel(SCREEN_ANIMATION_RATE_MINIMUM);
        }
        if (this.mDpcExt.isSilentRebootFirstGoToSleep(this.mDisplayId)) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void setProximitySensorEnabled(boolean z) {
        if (z) {
            if (this.mProximitySensorEnabled) {
                return;
            }
            if (DEBUG) {
                Slog.d(this.mTag, "setProximitySensorEnabled : True");
            }
            this.mProximitySensorEnabled = true;
            this.mIgnoreProximityUntilChanged = false;
            if (this.mDpcExt.registerPSensor(this.mSensorManager, this.mProximitySensorListener, 3, this.mHandler)) {
                return;
            }
            this.mSensorManager.registerListener(this.mProximitySensorListener, this.mProximitySensor, 3, this.mHandler);
            return;
        }
        if (this.mProximitySensorEnabled) {
            if (DEBUG) {
                Slog.d(this.mTag, "setProximitySensorEnabled : False");
            }
            this.mProximitySensorEnabled = false;
            this.mDpcExt.setUseProximityForceSuspend(false, this.mDisplayId);
            this.mProximity = -1;
            this.mIgnoreProximityUntilChanged = false;
            this.mPendingProximity = -1;
            this.mHandler.removeMessages(2);
            this.mSensorManager.unregisterListener(this.mProximitySensorListener);
            clearPendingProximityDebounceTime();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleProximitySensorEvent(long j, boolean z) {
        if (this.mProximitySensorEnabled) {
            int i = this.mPendingProximity;
            if (i != 0 || z) {
                if (i == 1 && z) {
                    return;
                }
                this.mHandler.removeMessages(2);
                if (z) {
                    if (this.mDpcExt.interceptProximityEvent()) {
                        return;
                    }
                    this.mPendingProximity = 1;
                    setPendingProximityDebounceTime(j + 0);
                } else {
                    this.mPendingProximity = 0;
                    setPendingProximityDebounceTime(j + 0);
                }
                debounceProximitySensor();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void debounceProximitySensor() {
        if (!this.mProximitySensorEnabled || this.mPendingProximity == -1 || this.mPendingProximityDebounceTime < 0) {
            return;
        }
        if (this.mPendingProximityDebounceTime <= this.mClock.uptimeMillis()) {
            if (this.mProximity != this.mPendingProximity) {
                this.mIgnoreProximityUntilChanged = false;
                Slog.i(this.mTag, "No longer ignoring proximity [" + this.mPendingProximity + "]");
            }
            this.mProximity = this.mPendingProximity;
            this.mDpcExt.onProximityDebounceTimeArrived(this.mDisplayId);
            updatePowerState();
            clearPendingProximityDebounceTime();
            return;
        }
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(2), this.mPendingProximityDebounceTime);
    }

    private void clearPendingProximityDebounceTime() {
        if (this.mPendingProximityDebounceTime >= 0) {
            this.mPendingProximityDebounceTime = -1L;
            this.mCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdProxDebounce);
        }
    }

    private void setPendingProximityDebounceTime(long j) {
        if (this.mPendingProximityDebounceTime < 0) {
            this.mCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdProxDebounce);
        }
        this.mPendingProximityDebounceTime = j;
    }

    private void sendOnStateChangedWithWakelock() {
        if (this.mOnStateChangedPending) {
            return;
        }
        this.mOnStateChangedPending = true;
        this.mCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdOnStateChanged);
        this.mHandler.post(this.mOnStateChangedRunnable);
    }

    private void logDisplayPolicyChanged(int i) {
        LogMaker logMaker = new LogMaker(1696);
        logMaker.setType(6);
        logMaker.setSubtype(i);
        MetricsLogger.action(logMaker);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSettingsChange(boolean z) {
        this.mPendingScreenBrightnessSetting = getScreenBrightnessSetting();
        this.mPendingAutoBrightnessAdjustment = getAutoBrightnessAdjustmentSetting();
        this.mDpcExt.setGlobalHbmSellMode();
        DisplayPowerState displayPowerState = this.mPowerState;
        int screenState = displayPowerState != null ? displayPowerState.getScreenState() : 2;
        if (this.mDpcExt.hasRemapDisable()) {
            this.mPendingAutoBrightnessAdjustment = this.mDpcExt.getAdjustmentSetting(this.mContext, this.mDisplayId, screenState, this.mPendingAutoBrightnessAdjustment);
        }
        int i = this.mDisplayId;
        sendUpdatePowerState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBrightnessModeChange() {
        final int intForUser = Settings.System.getIntForUser(this.mContext.getContentResolver(), "screen_brightness_mode", 0, -2);
        this.mHandler.postAtTime(new Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPowerController.this.lambda$handleBrightnessModeChange$6(intForUser);
            }
        }, this.mClock.uptimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleBrightnessModeChange$6(int i) {
        this.mUseAutoBrightness = i == 1;
        updatePowerState();
    }

    private float getAutoBrightnessAdjustmentSetting() {
        float floatForUser = Settings.System.getFloatForUser(this.mContext.getContentResolver(), "screen_auto_brightness_adj", SCREEN_ANIMATION_RATE_MINIMUM, -2);
        return Float.isNaN(floatForUser) ? SCREEN_ANIMATION_RATE_MINIMUM : clampAutoBrightnessAdjustment(floatForUser);
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public float getScreenBrightnessSetting() {
        float f;
        Float.isNaN(this.mBrightnessSetting.getBrightness());
        try {
            f = Settings.System.getStringForUser(this.mContext.getContentResolver(), "screen_brightness", -2) != null ? Integer.parseInt(r0) : this.mScreenBrightnessDefault;
        } catch (NumberFormatException unused) {
            f = this.mScreenBrightnessDefault;
        }
        return this.mDpcExt.handleScreenBrightnessSettingChange(f);
    }

    private void loadNitBasedBrightnessSetting() {
        if (this.mDisplayId == 0 && this.mPersistBrightnessNitsForDefaultDisplay) {
            float brightnessNitsForDefaultDisplay = this.mBrightnessSetting.getBrightnessNitsForDefaultDisplay();
            if (brightnessNitsForDefaultDisplay >= SCREEN_ANIMATION_RATE_MINIMUM) {
                float convertToFloatScale = convertToFloatScale(brightnessNitsForDefaultDisplay);
                if (isValidBrightnessValue(convertToFloatScale)) {
                    this.mBrightnessSetting.setBrightness(convertToFloatScale);
                    this.mCurrentScreenBrightnessSetting = convertToFloatScale;
                    return;
                }
            }
        }
        this.mCurrentScreenBrightnessSetting = getScreenBrightnessSetting();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setBrightness(float f) {
        if (this.mDpcExt.setBrightnessByAccessibility()) {
            this.mDpcExt.setBrightnessExt(f);
            return;
        }
        this.mBrightnessSetting.setBrightness(f);
        if (this.mDisplayId == 0 && this.mPersistBrightnessNitsForDefaultDisplay) {
            float convertToNits = convertToNits(f);
            if (convertToNits >= SCREEN_ANIMATION_RATE_MINIMUM) {
                this.mBrightnessSetting.setBrightnessNitsForDefaultDisplay(convertToNits);
            }
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void onBootCompleted() {
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(15), this.mClock.uptimeMillis());
    }

    private void updateScreenBrightnessSetting(float f) {
        if (!isValidBrightnessValue(f) || f == this.mCurrentScreenBrightnessSetting) {
            return;
        }
        setCurrentScreenBrightness(f);
        setBrightness(f);
    }

    private void setCurrentScreenBrightness(float f) {
        if (f != this.mCurrentScreenBrightnessSetting) {
            if (DEBUG_PANIC) {
                Slog.d(this.mTag, "setCurrentScreenBrightness " + this.mCurrentScreenBrightnessSetting + "->" + f);
            }
            boolean postBrightnessChanged = this.mDpcExt.postBrightnessChanged(this.mCurrentScreenBrightnessSetting, f);
            this.mCurrentScreenBrightnessSetting = f;
            if (postBrightnessChanged) {
                postBrightnessChangeRunnable();
            }
        }
    }

    private void putAutoBrightnessAdjustmentSetting(float f) {
        if (this.mDisplayId == 0) {
            this.mAutoBrightnessAdjustment = f;
            Settings.System.putFloatForUser(this.mContext.getContentResolver(), "screen_auto_brightness_adj", f, -2);
        }
    }

    private boolean updateAutoBrightnessAdjustment() {
        if (this.mDpcExt.isSpecialAdj(this.mTemporaryAutoBrightnessAdjustment)) {
            this.mAutoBrightnessAdjustment = this.mTemporaryAutoBrightnessAdjustment;
            this.mPendingAutoBrightnessAdjustment = Float.NaN;
            this.mTemporaryAutoBrightnessAdjustment = Float.NaN;
            return true;
        }
        if (Float.isNaN(this.mPendingAutoBrightnessAdjustment)) {
            return false;
        }
        float f = this.mAutoBrightnessAdjustment;
        if (f == this.mPendingAutoBrightnessAdjustment || this.mDpcExt.isSpecialAdj(f)) {
            this.mPendingAutoBrightnessAdjustment = Float.NaN;
            this.mTemporaryAutoBrightnessAdjustment = Float.NaN;
            return false;
        }
        float f2 = this.mPendingAutoBrightnessAdjustment;
        if (f2 == SCREEN_ANIMATION_RATE_MINIMUM) {
            this.mPendingAutoBrightnessAdjustment = Float.NaN;
            return false;
        }
        this.mAutoBrightnessAdjustment = f2;
        this.mPendingAutoBrightnessAdjustment = Float.NaN;
        this.mTemporaryAutoBrightnessAdjustment = Float.NaN;
        return true;
    }

    private boolean updateUserSetScreenBrightness() {
        if (!Float.isNaN(this.mPendingScreenBrightnessSetting)) {
            float f = this.mPendingScreenBrightnessSetting;
            if (f >= SCREEN_ANIMATION_RATE_MINIMUM) {
                if (BrightnessSynchronizer.floatEquals(this.mCurrentScreenBrightnessSetting, f)) {
                    this.mPendingScreenBrightnessSetting = Float.NaN;
                    this.mTemporaryScreenBrightness = Float.NaN;
                    return false;
                }
                setCurrentScreenBrightness(this.mPendingScreenBrightnessSetting);
                this.mLastUserSetScreenBrightness = this.mPendingScreenBrightnessSetting;
                this.mPendingScreenBrightnessSetting = Float.NaN;
                this.mTemporaryScreenBrightness = Float.NaN;
                return true;
            }
        }
        if (this.mCurrentScreenBrightnessSetting == this.mTemporaryScreenBrightness) {
            this.mTemporaryScreenBrightness = -1.0f;
        }
        return false;
    }

    private float convertToNits(float f) {
        return this.mAutomaticBrightnessController == null ? -1.0f : 1.0f;
    }

    private float getBrightnessByNit(float f) {
        return this.mDpcExt.getBrightnessByNit(f);
    }

    private float getNitByBrightness(float f) {
        return this.mDpcExt.getNitByBrightness(f);
    }

    private float convertToAdjustedNits(float f) {
        return this.mDpcExt.getNitByBrightness(f);
    }

    private float convertToFloatScale(float f) {
        IColorAutomaticBrightnessController iColorAutomaticBrightnessController = this.mAutomaticBrightnessController;
        if (iColorAutomaticBrightnessController == null) {
            return Float.NaN;
        }
        return iColorAutomaticBrightnessController.convertToFloatScale(f);
    }

    @GuardedBy({"mLock"})
    private void updatePendingProximityRequestsLocked() {
        this.mWaitingForNegativeProximity |= this.mPendingWaitForNegativeProximityLocked;
        this.mPendingWaitForNegativeProximityLocked = false;
        if (this.mIgnoreProximityUntilChanged) {
            this.mWaitingForNegativeProximity = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ignoreProximitySensorUntilChangedInternal() {
        if (!this.mIgnoreProximityUntilChanged && this.mPowerRequest.useProximitySensor && this.mProximity == 1) {
            this.mIgnoreProximityUntilChanged = true;
            Slog.i(this.mTag, "Ignoring proximity");
            updatePowerState();
        }
    }

    private void sendOnProximityPositiveWithWakelock() {
        this.mCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdProxPositive);
        this.mHandler.post(this.mOnProximityPositiveRunnable);
        this.mOnProximityPositiveMessages++;
    }

    private void sendOnProximityNegativeWithWakelock() {
        this.mOnProximityNegativeMessages++;
        this.mCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdProxNegative);
        this.mHandler.post(this.mOnProximityNegativeRunnable);
    }

    private boolean readyToUpdateDisplayState() {
        return this.mDisplayId == 0 || this.mBootCompleted;
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
            printWriter.println("  mPendingWaitForNegativeProximityLocked=" + this.mPendingWaitForNegativeProximityLocked);
            printWriter.println("  mPendingUpdatePowerStateLocked=" + this.mPendingUpdatePowerStateLocked);
        }
        printWriter.println();
        printWriter.println("Display Power Controller Configuration:");
        printWriter.println("  mScreenBrightnessRangeDefault=" + this.mScreenBrightnessDefault);
        printWriter.println("  mScreenBrightnessDozeConfig=" + this.mScreenBrightnessDozeConfig);
        printWriter.println("  mScreenBrightnessDimConfig=" + this.mScreenBrightnessDimConfig);
        printWriter.println("  mScreenBrightnessRangeMinimum=" + this.mScreenBrightnessRangeMinimum);
        printWriter.println("  mScreenBrightnessRangeMaximum=" + this.mScreenBrightnessRangeMaximum);
        printWriter.println("  mScreenBrightnessNormalMaximum=" + this.mScreenBrightnessNormalMaximum);
        printWriter.println("  mCurrentScreenBrightnessSetting=" + this.mCurrentScreenBrightnessSetting);
        printWriter.println("  mUseSoftwareAutoBrightnessConfig=" + this.mUseSoftwareAutoBrightnessConfig);
        printWriter.println("  mAllowAutoBrightnessWhileDozingConfig=" + this.mAllowAutoBrightnessWhileDozingConfig);
        printWriter.println("  mPersistBrightnessNitsForDefaultDisplay=" + this.mPersistBrightnessNitsForDefaultDisplay);
        printWriter.println("  mSkipScreenOnBrightnessRamp=" + this.mSkipScreenOnBrightnessRamp);
        printWriter.println("  mColorFadeFadesConfig=" + this.mColorFadeFadesConfig);
        printWriter.println("  mColorFadeEnabled=" + this.mColorFadeEnabled);
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
        this.mHandler.runWithScissors(new Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPowerController.this.lambda$dump$7(printWriter);
            }
        }, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: dumpLocal, reason: merged with bridge method [inline-methods] */
    public void lambda$dump$7(PrintWriter printWriter) {
        printWriter.println();
        printWriter.println("Display Power Controller Thread State:");
        printWriter.println("  mPowerRequest=" + this.mPowerRequest);
        printWriter.println("  mUnfinishedBusiness=" + this.mUnfinishedBusiness);
        printWriter.println("  mWaitingForNegativeProximity=" + this.mWaitingForNegativeProximity);
        printWriter.println("  mProximitySensor=" + this.mProximitySensor);
        printWriter.println("  mProximitySensorEnabled=" + this.mProximitySensorEnabled);
        printWriter.println("  mProximityThreshold=" + this.mProximityThreshold);
        printWriter.println("  mProximity=" + proximityToString(this.mProximity));
        printWriter.println("  mPendingProximity=" + proximityToString(this.mPendingProximity));
        printWriter.println("  mPendingProximityDebounceTime=" + TimeUtils.formatUptime(this.mPendingProximityDebounceTime));
        printWriter.println("  mScreenOffBecauseOfProximity=" + this.mScreenOffBecauseOfProximity);
        printWriter.println("  mLastUserSetScreenBrightness=" + this.mLastUserSetScreenBrightness);
        printWriter.println("  mPendingScreenBrightnessSetting=" + this.mPendingScreenBrightnessSetting);
        printWriter.println("  mTemporaryScreenBrightness=" + this.mTemporaryScreenBrightness);
        printWriter.println("  mBrightnessToFollow=" + this.mBrightnessToFollow);
        printWriter.println("  mAutoBrightnessAdjustment=" + this.mAutoBrightnessAdjustment);
        printWriter.println("  mBrightnessReason=" + this.mBrightnessReason);
        printWriter.println("  mTemporaryAutoBrightnessAdjustment=" + this.mTemporaryAutoBrightnessAdjustment);
        printWriter.println("  mPendingAutoBrightnessAdjustment=" + this.mPendingAutoBrightnessAdjustment);
        printWriter.println("  mAppliedAutoBrightness=" + this.mAppliedAutoBrightness);
        printWriter.println("  mAppliedDimming=" + this.mAppliedDimming);
        printWriter.println("  mAppliedLowPower=" + this.mAppliedLowPower);
        printWriter.println("  mAppliedThrottling=" + this.mAppliedThrottling);
        printWriter.println("  mAppliedScreenBrightnessOverride=" + this.mAppliedScreenBrightnessOverride);
        printWriter.println("  mAppliedTemporaryBrightness=" + this.mAppliedTemporaryBrightness);
        printWriter.println("  mAppliedTemporaryAutoBrightnessAdjustment=" + this.mAppliedTemporaryAutoBrightnessAdjustment);
        printWriter.println("  mAppliedBrightnessBoost=" + this.mAppliedBrightnessBoost);
        printWriter.println("  mDozing=" + this.mDozing);
        printWriter.println("  mSkipRampState=" + skipRampStateToString(this.mSkipRampState));
        printWriter.println("  mScreenOnBlockStartRealTime=" + this.mScreenOnBlockStartRealTime);
        printWriter.println("  mScreenOffBlockStartRealTime=" + this.mScreenOffBlockStartRealTime);
        printWriter.println("  mPendingScreenOnUnblocker=" + this.mPendingScreenOnUnblocker);
        printWriter.println("  mPendingScreenOffUnblocker=" + this.mPendingScreenOffUnblocker);
        printWriter.println("  mPendingScreenOff=" + this.mPendingScreenOff);
        printWriter.println("  mReportedToPolicy=" + reportedToPolicyToString(this.mReportedScreenStateToPolicy));
        printWriter.println("  mIsRbcActive=" + this.mIsRbcActive);
        printWriter.println("  mOnStateChangePending=" + this.mOnStateChangedPending);
        printWriter.println("  mOnProximityPositiveMessages=" + this.mOnProximityPositiveMessages);
        printWriter.println("  mOnProximityNegativeMessages=" + this.mOnProximityNegativeMessages);
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
        IColorAutomaticBrightnessController iColorAutomaticBrightnessController = this.mAutomaticBrightnessController;
        if (iColorAutomaticBrightnessController != null) {
            iColorAutomaticBrightnessController.dump(printWriter);
            dumpBrightnessEvents(printWriter);
        }
        ScreenOffBrightnessSensorController screenOffBrightnessSensorController = this.mScreenOffBrightnessSensorController;
        if (screenOffBrightnessSensorController != null) {
            screenOffBrightnessSensorController.dump(printWriter);
        }
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
        this.mDpcExt.dump(printWriter);
    }

    private static String proximityToString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? Integer.toString(i) : "Positive" : "Negative" : "Unknown";
    }

    private static String reportedToPolicyToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? Integer.toString(i) : "REPORTED_TO_POLICY_SCREEN_ON" : "REPORTED_TO_POLICY_SCREEN_TURNING_ON" : "REPORTED_TO_POLICY_SCREEN_OFF";
    }

    private static String skipRampStateToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? Integer.toString(i) : "RAMP_STATE_SKIP_AUTOBRIGHT" : "RAMP_STATE_SKIP_INITIAL" : "RAMP_STATE_SKIP_NONE";
    }

    private void dumpBrightnessEvents(PrintWriter printWriter) {
        RingBuffer<BrightnessEvent> ringBuffer = this.mBrightnessEventRingBuffer;
        if (ringBuffer == null) {
            return;
        }
        int size = ringBuffer.size();
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
                iBatteryStats.noteScreenBrightness((int) f);
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
                this.mHandler.removeMessages(13);
                if (z != z2) {
                    logHbmBrightnessStats(f, this.mDisplayStatsId);
                    return;
                }
                Message obtainMessage = this.mHandler.obtainMessage();
                obtainMessage.what = 13;
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
        float convertToAdjustedNits = convertToAdjustedNits(brightnessEvent.getBrightness());
        float powerFactor = brightnessEvent.isLowPowerModeSet() ? brightnessEvent.getPowerFactor() : -1.0f;
        int rbcStrength = brightnessEvent.isRbcEnabled() ? brightnessEvent.getRbcStrength() : -1;
        float convertToAdjustedNits2 = brightnessEvent.getHbmMode() == 0 ? -1.0f : convertToAdjustedNits(brightnessEvent.getHbmMax());
        float convertToAdjustedNits3 = brightnessEvent.getThermalMax() == 1.0f ? -1.0f : convertToAdjustedNits(brightnessEvent.getThermalMax());
        if (this.mIsDisplayInternal) {
            FrameworkStatsLog.write(FrameworkStatsLog.DISPLAY_BRIGHTNESS_CHANGED, convertToAdjustedNits(brightnessEvent.getInitialBrightness()), convertToAdjustedNits, brightnessEvent.getLux(), brightnessEvent.getPhysicalDisplayId(), brightnessEvent.wasShortTermModelActive(), powerFactor, rbcStrength, convertToAdjustedNits2, convertToAdjustedNits3, brightnessEvent.isAutomaticBrightnessEnabled(), 1, convertBrightnessReasonToStatsEnum(brightnessEvent.getReason().getReason()), nitsToRangeIndex(convertToAdjustedNits), z, brightnessEvent.getHbmMode() == 1, brightnessEvent.getHbmMode() == 2, (modifier & 2) > 0, this.mBrightnessThrottler.getBrightnessMaxReason(), (modifier & 1) > 0, brightnessEvent.isRbcEnabled(), (flags & 2) > 0, (flags & 4) > 0, (flags & 8) > 0, (flags & 16) > 0, (flags & 32) > 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DisplayControllerHandler extends Handler {
        DisplayControllerHandler(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            DisplayPowerController displayPowerController = DisplayPowerController.this;
            displayPowerController.mDpcExt.onDisplayControllerHandler(message, displayPowerController.mHandler);
            int i = message.what;
            if (i == 21) {
                if (DisplayPowerController.this.mResetFpsStatePending) {
                    DisplayPowerController.this.updateFpsWhenDcChange(false);
                    return;
                }
                return;
            }
            if (i != 22) {
                switch (i) {
                    case 1:
                        DisplayPowerController.this.updatePowerState();
                        return;
                    case 2:
                        DisplayPowerController.this.debounceProximitySensor();
                        return;
                    case 3:
                        if (DisplayPowerController.this.mPendingScreenOnUnblocker == message.obj) {
                            DisplayPowerController.this.unblockScreenOn();
                            DisplayPowerController.this.updatePowerState();
                            return;
                        }
                        return;
                    case 4:
                        if (DisplayPowerController.this.mPendingScreenOffUnblocker == message.obj) {
                            DisplayPowerController.this.unblockScreenOff();
                            DisplayPowerController.this.updatePowerState();
                            return;
                        }
                        return;
                    case 5:
                        DisplayPowerController.this.mBrightnessConfiguration = (BrightnessConfiguration) message.obj;
                        DisplayPowerController.this.mShouldResetShortTermModel = message.arg1 == 1;
                        DisplayPowerController.this.updatePowerState();
                        return;
                    case 6:
                        DisplayPowerController.this.mTemporaryScreenBrightness = Float.intBitsToFloat(message.arg1);
                        DisplayPowerController displayPowerController2 = DisplayPowerController.this;
                        displayPowerController2.mTemporaryScreenBrightness = displayPowerController2.mDpcExt.handleSetTemporaryBrightnessMessage(displayPowerController2.mTemporaryScreenBrightness, "MSG_SET_TEMPORARY_BRIGHTNESS", DisplayPowerController.this.mDisplayId);
                        DisplayPowerController.this.updatePowerState();
                        return;
                    case 7:
                        DisplayPowerController.this.mTemporaryAutoBrightnessAdjustment = Float.intBitsToFloat(message.arg1);
                        DisplayPowerController.this.updatePowerState();
                        return;
                    case 8:
                        DisplayPowerController.this.ignoreProximitySensorUntilChangedInternal();
                        return;
                    case 9:
                        DisplayPowerController.this.cleanupHandlerThreadAfterStop();
                        return;
                    case 10:
                        if (DisplayPowerController.this.mStopped) {
                            return;
                        }
                        DisplayPowerController.this.handleSettingsChange(false);
                        return;
                    case 11:
                        DisplayPowerController.this.handleRbcChanged();
                        return;
                    case 12:
                        if (DisplayPowerController.this.mPowerState != null) {
                            DisplayPowerController.this.reportStats(DisplayPowerController.this.mPowerState.getScreenBrightness());
                            return;
                        }
                        return;
                    case 13:
                        DisplayPowerController.this.logHbmBrightnessStats(Float.intBitsToFloat(message.arg1), message.arg2);
                        return;
                    case 14:
                        DisplayPowerController.this.handleOnSwitchUser(message.arg1);
                        return;
                    case 15:
                        DisplayPowerController.this.mBootCompleted = true;
                        DisplayPowerController.this.updatePowerState();
                        return;
                    default:
                        return;
                }
            }
            DisplayPowerController.this.mDpcExt.setRmMode();
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
            int screenState = DisplayPowerController.this.mPowerState != null ? DisplayPowerController.this.mPowerState.getScreenState() : 2;
            DisplayPowerController displayPowerController = DisplayPowerController.this;
            displayPowerController.mDpcExt.onChange(displayPowerController.mContext, DisplayPowerController.this.mDisplayId, z, uri, screenState);
            if (uri.equals(Settings.System.getUriFor("screen_brightness_mode"))) {
                DisplayPowerController.this.handleBrightnessModeChange();
            } else {
                DisplayPowerController.this.handleSettingsChange(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ScreenOnUnblocker implements WindowManagerPolicy.ScreenOnListener {
        private ScreenOnUnblocker() {
        }

        public void onScreenOn() {
            Message obtainMessage = DisplayPowerController.this.mHandler.obtainMessage(3, this);
            DisplayPowerController displayPowerController = DisplayPowerController.this;
            if (displayPowerController.mDpcExt.sendMessageWhenScreenOnUnblocker(displayPowerController.mHandler, obtainMessage)) {
                return;
            }
            DisplayPowerController.this.mHandler.sendMessageAtTime(obtainMessage, DisplayPowerController.this.mClock.uptimeMillis());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ScreenOffUnblocker implements WindowManagerPolicy.ScreenOffListener {
        private ScreenOffUnblocker() {
        }

        public void onScreenOff() {
            DisplayPowerController.this.mHandler.sendMessageAtTime(DisplayPowerController.this.mHandler.obtainMessage(4, this), DisplayPowerController.this.mClock.uptimeMillis());
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setAutoBrightnessLoggingEnabled(boolean z) {
        IColorAutomaticBrightnessController iColorAutomaticBrightnessController = this.mAutomaticBrightnessController;
        if (iColorAutomaticBrightnessController != null) {
            iColorAutomaticBrightnessController.setLoggingEnabled(z);
        }
        IOplusDisplayPowerControllerExt iOplusDisplayPowerControllerExt = this.mDpcExt;
        if (iOplusDisplayPowerControllerExt != null) {
            iOplusDisplayPowerControllerExt.setLoggingEnabled(z);
        }
        RampAnimator.DualRampAnimator<DisplayPowerState> dualRampAnimator = this.mScreenBrightnessRampAnimator;
        if (dualRampAnimator != null) {
            dualRampAnimator.setLoggingEnabled(z);
        }
        DEBUG_PANIC = z;
        Slog.d(this.mTag, "setLoggingEnabled loggingEnabled=" + z);
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

    @VisibleForTesting
    String getSuspendBlockerUnfinishedBusinessId(int i) {
        return "[" + i + "]unfinished business";
    }

    String getSuspendBlockerOnStateChangedId(int i) {
        return "[" + i + "]on state changed";
    }

    String getSuspendBlockerProxPositiveId(int i) {
        return "[" + i + "]prox positive";
    }

    String getSuspendBlockerProxNegativeId(int i) {
        return "[" + i + "]prox negative";
    }

    @VisibleForTesting
    String getSuspendBlockerProxDebounceId(int i) {
        return "[" + i + "]prox debounce";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        Injector() {
        }

        Clock getClock() {
            return new Clock() { // from class: com.android.server.display.DisplayPowerController$Injector$$ExternalSyntheticLambda0
                @Override // com.android.server.display.DisplayPowerController.Clock
                public final long uptimeMillis() {
                    return SystemClock.uptimeMillis();
                }
            };
        }

        DisplayPowerState getDisplayPowerState(DisplayBlanker displayBlanker, ColorFade colorFade, int i, int i2, IOplusDisplayPowerControllerExt iOplusDisplayPowerControllerExt) {
            return new DisplayPowerState(displayBlanker, colorFade, i, i2, iOplusDisplayPowerControllerExt);
        }

        RampAnimator.DualRampAnimator<DisplayPowerState> getDualRampAnimator(DisplayPowerState displayPowerState, FloatProperty<DisplayPowerState> floatProperty, FloatProperty<DisplayPowerState> floatProperty2) {
            return new RampAnimator.DualRampAnimator<>(displayPowerState, floatProperty, floatProperty2);
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

    public void setFakeLux(boolean z, int i) {
        this.mDpcExt.setFakeLux(z, i);
    }

    public void setAuxFakeLux(boolean z, int i) {
        this.mDpcExt.setAuxFakeLux(z, i);
    }

    public void setMainFakeLux(boolean z, int i) {
        this.mDpcExt.setMainFakeLux(z, i);
    }

    public float getTemporaryAutoBrightnessAdjustment() {
        return this.mTemporaryAutoBrightnessAdjustment;
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

    public IOplusDisplayPowerControllerWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class OplusDisplayPowerControllerWrapper implements IOplusDisplayPowerControllerWrapper {
        private LogicalDisplayMapper mLogicalDisplayMapper;

        private OplusDisplayPowerControllerWrapper() {
            this.mLogicalDisplayMapper = null;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setDebug(boolean z) {
            DisplayPowerController.DEBUG = z;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void updatePowerState() {
            DisplayPowerController.this.updatePowerState();
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void sendUpdatePowerState() {
            DisplayPowerController.this.sendUpdatePowerState();
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void animateScreenBrightness(float f, float f2, float f3) {
            DisplayPowerController.this.animateScreenBrightness(f, f2, f3);
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setCurrentScreenBrightnessSetting(int i) {
            DisplayPowerController.this.mCurrentScreenBrightnessSetting = i;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setScreenBrightnessRangeMinimum(int i) {
            DisplayPowerController.this.mScreenBrightnessRangeMinimum = i;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setScreenBrightnessRangeMaximum(int i) {
            DisplayPowerController.this.mScreenBrightnessRangeMaximum = i;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setScreenBrightnessNormalMaximum(int i) {
            DisplayPowerController.this.mScreenBrightnessNormalMaximum = i;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setScreenBrightnessDefault(int i) {
            DisplayPowerController.this.mScreenBrightnessDefault = i;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setAutoBrightnessAdjustment(float f) {
            DisplayPowerController.this.mAutoBrightnessAdjustment = f;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setProximitySensorEnabled(boolean z) {
            DisplayPowerController.this.setProximitySensorEnabled(z);
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setScreenOffBecauseOfProximity(boolean z) {
            DisplayPowerController.this.mScreenOffBecauseOfProximity = z;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setWaitingForNegativeProximity(boolean z) {
            DisplayPowerController.this.mWaitingForNegativeProximity = z;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void handleCoverModeProximitySensorEvent(long j, boolean z) {
            DisplayPowerController.this.handleProximitySensorEvent(j, z);
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setLogicalDisplayMapper(LogicalDisplayMapper logicalDisplayMapper) {
            this.mLogicalDisplayMapper = logicalDisplayMapper;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public LogicalDisplayMapper getLogicalDisplayMapper() {
            return this.mLogicalDisplayMapper;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public DisplayManagerInternal.DisplayPowerCallbacks getCallbacks() {
            return DisplayPowerController.this.mCallbacks;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public boolean isScreenOnUnblockerExist() {
            return DisplayPowerController.this.mPendingScreenOnUnblocker != null;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void sendMsgUnblockScreenOn(boolean z) {
            if (z || !DisplayPowerController.this.mHandler.hasMessages(3)) {
                return;
            }
            DisplayPowerController.this.mHandler.removeMessages(3);
            Message obtainMessage = DisplayPowerController.this.mHandler.obtainMessage(3, DisplayPowerController.this.mPendingScreenOnUnblocker);
            obtainMessage.setAsynchronous(true);
            DisplayPowerController.this.mHandler.sendMessage(obtainMessage);
            Slog.d(DisplayPowerController.this.mTag, "MSG_SCREEN_ON_UNBLOCKED sended");
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public int getDisplayId() {
            return DisplayPowerController.this.mDisplayId;
        }
    }
}
