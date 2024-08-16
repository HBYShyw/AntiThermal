package com.android.server;

import android.R;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.AlarmManager;
import android.app.BroadcastOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.net.ConnectivityManager;
import android.net.INetworkPolicyManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IDeviceIdleController;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManagerInternal;
import android.os.PowerSaveState;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ShellCallback;
import android.os.ShellCommand;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.telephony.emergency.EmergencyNumber;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.MutableLong;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.TimeUtils;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IBatteryStats;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FastXmlSerializer;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.XmlUtils;
import com.android.modules.expresslog.Counter;
import com.android.server.AnyMotionDetector;
import com.android.server.DeviceIdleInternal;
import com.android.server.PowerAllowlistInternal;
import com.android.server.am.BatteryStatsService;
import com.android.server.am.HostingRecord;
import com.android.server.am.IOplusSceneManager;
import com.android.server.backup.BackupManagerConstants;
import com.android.server.bluetooth.IOplusBluetoothManagerServiceExt;
import com.android.server.clipboard.ClipboardService;
import com.android.server.deviceidle.ConstraintController;
import com.android.server.deviceidle.DeviceIdleConstraintTracker;
import com.android.server.deviceidle.IDeviceIdleConstraint;
import com.android.server.deviceidle.TvConstraintController;
import com.android.server.net.NetworkPolicyManagerInternal;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DeviceIdleController extends SystemService implements AnyMotionDetector.DeviceIdleCallback {
    private static final int ACTIVE_REASON_ALARM = 7;
    private static final int ACTIVE_REASON_CHARGING = 3;
    private static final int ACTIVE_REASON_EMERGENCY_CALL = 8;
    private static final int ACTIVE_REASON_FORCED = 6;
    private static final int ACTIVE_REASON_FROM_BINDER_CALL = 5;
    private static final int ACTIVE_REASON_MOTION = 1;
    private static final int ACTIVE_REASON_SCREEN = 2;
    private static final int ACTIVE_REASON_UNKNOWN = 0;
    private static final int ACTIVE_REASON_UNLOCKED = 4;
    private static final boolean COMPRESS_TIME = false;
    private static final boolean DEBUG = false;
    private static final int EVENT_BUFFER_SIZE = 100;
    private static final int EVENT_DEEP_IDLE = 4;
    private static final int EVENT_DEEP_MAINTENANCE = 5;
    private static final int EVENT_LIGHT_IDLE = 2;
    private static final int EVENT_LIGHT_MAINTENANCE = 3;
    private static final int EVENT_NORMAL = 1;
    private static final int EVENT_NULL = 0;

    @VisibleForTesting
    static final int LIGHT_STATE_ACTIVE = 0;

    @VisibleForTesting
    static final int LIGHT_STATE_IDLE = 4;

    @VisibleForTesting
    static final int LIGHT_STATE_IDLE_MAINTENANCE = 6;

    @VisibleForTesting
    static final int LIGHT_STATE_INACTIVE = 1;

    @VisibleForTesting
    static final int LIGHT_STATE_OVERRIDE = 7;

    @VisibleForTesting
    static final int LIGHT_STATE_WAITING_FOR_NETWORK = 5;

    @VisibleForTesting
    static final float MIN_PRE_IDLE_FACTOR_CHANGE = 0.05f;

    @VisibleForTesting
    static final long MIN_STATE_STEP_ALARM_CHANGE = 60000;
    private static final int MSG_FINISH_IDLE_OP = 8;
    private static final int MSG_REPORT_ACTIVE = 5;
    private static final int MSG_REPORT_IDLE_OFF = 4;
    private static final int MSG_REPORT_IDLE_ON = 2;
    private static final int MSG_REPORT_IDLE_ON_LIGHT = 3;

    @VisibleForTesting
    static final int MSG_REPORT_STATIONARY_STATUS = 7;
    private static final int MSG_REPORT_TEMP_APP_WHITELIST_ADDED_TO_NPMS = 14;
    private static final int MSG_REPORT_TEMP_APP_WHITELIST_CHANGED = 13;
    private static final int MSG_REPORT_TEMP_APP_WHITELIST_REMOVED_TO_NPMS = 15;

    @VisibleForTesting
    static final int MSG_RESET_PRE_IDLE_TIMEOUT_FACTOR = 12;
    private static final int MSG_SEND_CONSTRAINT_MONITORING = 10;
    private static final int MSG_TEMP_APP_WHITELIST_TIMEOUT = 6;

    @VisibleForTesting
    static final int MSG_UPDATE_PRE_IDLE_TIMEOUT_FACTOR = 11;
    private static final int MSG_WRITE_CONFIG = 1;

    @VisibleForTesting
    static final int SET_IDLE_FACTOR_RESULT_IGNORED = 0;

    @VisibleForTesting
    static final int SET_IDLE_FACTOR_RESULT_INVALID = 3;

    @VisibleForTesting
    static final int SET_IDLE_FACTOR_RESULT_NOT_SUPPORT = 2;

    @VisibleForTesting
    static final int SET_IDLE_FACTOR_RESULT_OK = 1;

    @VisibleForTesting
    static final int SET_IDLE_FACTOR_RESULT_UNINIT = -1;

    @VisibleForTesting
    static final int STATE_ACTIVE = 0;

    @VisibleForTesting
    static final int STATE_IDLE = 5;

    @VisibleForTesting
    static final int STATE_IDLE_MAINTENANCE = 6;

    @VisibleForTesting
    static final int STATE_IDLE_PENDING = 2;

    @VisibleForTesting
    static final int STATE_INACTIVE = 1;

    @VisibleForTesting
    static final int STATE_LOCATING = 4;

    @VisibleForTesting
    static final int STATE_QUICK_DOZE_DELAY = 7;

    @VisibleForTesting
    static final int STATE_SENSING = 3;
    private static final String TAG = "DeviceIdleController";
    private static final String USER_ALLOWLIST_ADDITION_METRIC_ID = "battery.value_app_added_to_power_allowlist";
    private static final String USER_ALLOWLIST_REMOVAL_METRIC_ID = "battery.value_app_removed_from_power_allowlist";

    @GuardedBy({"this"})
    private int mActiveIdleOpCount;
    private PowerManager.WakeLock mActiveIdleWakeLock;

    @GuardedBy({"this"})
    private int mActiveReason;
    private AlarmManager mAlarmManager;

    @GuardedBy({"this"})
    private boolean mAlarmsActive;
    private AnyMotionDetector mAnyMotionDetector;
    private final AppStateTrackerImpl mAppStateTracker;
    private IBatteryStats mBatteryStats;
    BinderService mBinderService;

    @GuardedBy({"this"})
    private boolean mCharging;
    public final AtomicFile mConfigFile;
    private Constants mConstants;
    private ConstraintController mConstraintController;
    private final ArrayMap<IDeviceIdleConstraint, DeviceIdleConstraintTracker> mConstraints;

    @GuardedBy({"this"})
    private long mCurLightIdleBudget;

    @VisibleForTesting
    final AlarmManager.OnAlarmListener mDeepAlarmListener;

    @GuardedBy({"this"})
    private boolean mDeepEnabled;
    private DeviceIdleControllerWrapper mDeviceIdleControllerWrapper;
    private IDeviceIdleControllerExt mDeviceIdleExt;
    private final EmergencyCallListener mEmergencyCallListener;
    private final int[] mEventCmds;
    private final String[] mEventReasons;
    private final long[] mEventTimes;

    @GuardedBy({"this"})
    private boolean mForceIdle;
    private final LocationListener mGenericLocationListener;
    private PowerManager.WakeLock mGoingIdleWakeLock;
    private final LocationListener mGpsLocationListener;
    final MyHandler mHandler;

    @GuardedBy({"this"})
    private boolean mHasFusedLocation;

    @GuardedBy({"this"})
    private boolean mHasGps;
    private Intent mIdleIntent;
    private Bundle mIdleIntentOptions;

    @GuardedBy({"this"})
    private long mIdleStartTime;
    private final IIntentReceiver mIdleStartedDoneReceiver;

    @GuardedBy({"this"})
    private long mInactiveTimeout;
    private final Injector mInjector;
    private final BroadcastReceiver mInteractivityReceiver;
    private final boolean mIsLocationPrefetchEnabled;

    @GuardedBy({"this"})
    private boolean mJobsActive;

    @GuardedBy({"this"})
    private Location mLastGenericLocation;

    @GuardedBy({"this"})
    private Location mLastGpsLocation;

    @GuardedBy({"this"})
    private long mLastMotionEventElapsed;

    @GuardedBy({"this"})
    private float mLastPreIdleFactor;
    private final AlarmManager.OnAlarmListener mLightAlarmListener;

    @GuardedBy({"this"})
    private boolean mLightEnabled;
    private Intent mLightIdleIntent;
    private Bundle mLightIdleIntentOptions;

    @GuardedBy({"this"})
    private int mLightState;
    private ActivityManagerInternal mLocalActivityManager;
    private ActivityTaskManagerInternal mLocalActivityTaskManager;
    private AlarmManagerInternal mLocalAlarmManager;
    private PowerManagerInternal mLocalPowerManager;
    private DeviceIdleInternal mLocalService;

    @GuardedBy({"this"})
    private boolean mLocated;

    @GuardedBy({"this"})
    private boolean mLocating;
    private LocationRequest mLocationRequest;

    @GuardedBy({"this"})
    private long mMaintenanceStartTime;

    @VisibleForTesting
    final MotionListener mMotionListener;
    private final AlarmManager.OnAlarmListener mMotionRegistrationAlarmListener;
    private Sensor mMotionSensor;
    private final AlarmManager.OnAlarmListener mMotionTimeoutAlarmListener;

    @GuardedBy({"this"})
    private boolean mNetworkConnected;
    private INetworkPolicyManager mNetworkPolicyManager;
    private NetworkPolicyManagerInternal mNetworkPolicyManagerInternal;

    @GuardedBy({"this"})
    private long mNextAlarmTime;

    @GuardedBy({"this"})
    private long mNextIdleDelay;

    @GuardedBy({"this"})
    private long mNextIdlePendingDelay;

    @GuardedBy({"this"})
    private long mNextLightAlarmTime;

    @GuardedBy({"this"})
    private long mNextLightIdleDelay;

    @GuardedBy({"this"})
    private long mNextLightIdleDelayFlex;

    @GuardedBy({"this"})
    private long mNextSensingTimeoutAlarmTime;

    @GuardedBy({"this"})
    private boolean mNotMoving;

    @GuardedBy({"this"})
    private int mNumBlockingConstraints;
    private PackageManagerInternal mPackageManagerInternal;
    private PowerManager mPowerManager;
    private Bundle mPowerSaveTempWhilelistChangedOptions;
    private Intent mPowerSaveTempWhitelistChangedIntent;
    private int[] mPowerSaveWhitelistAllAppIdArray;
    private final SparseBooleanArray mPowerSaveWhitelistAllAppIds;
    private final ArrayMap<String, Integer> mPowerSaveWhitelistApps;
    private final ArrayMap<String, Integer> mPowerSaveWhitelistAppsExceptIdle;
    private Intent mPowerSaveWhitelistChangedIntent;
    private Bundle mPowerSaveWhitelistChangedOptions;
    private int[] mPowerSaveWhitelistExceptIdleAppIdArray;
    private final SparseBooleanArray mPowerSaveWhitelistExceptIdleAppIds;
    private final SparseBooleanArray mPowerSaveWhitelistSystemAppIds;
    private final SparseBooleanArray mPowerSaveWhitelistSystemAppIdsExceptIdle;
    private int[] mPowerSaveWhitelistUserAppIdArray;
    private final SparseBooleanArray mPowerSaveWhitelistUserAppIds;
    private final ArrayMap<String, Integer> mPowerSaveWhitelistUserApps;
    private final ArraySet<String> mPowerSaveWhitelistUserAppsExceptIdle;

    @GuardedBy({"this"})
    private float mPreIdleFactor;

    @GuardedBy({"this"})
    private boolean mQuickDozeActivated;

    @GuardedBy({"this"})
    private boolean mQuickDozeActivatedWhileIdling;
    private final BroadcastReceiver mReceiver;
    private ArrayMap<String, Integer> mRemovedFromSystemWhitelistApps;

    @GuardedBy({"this"})
    private boolean mScreenLocked;
    private ActivityTaskManagerInternal.ScreenObserver mScreenObserver;

    @GuardedBy({"this"})
    private boolean mScreenOn;
    private final AlarmManager.OnAlarmListener mSensingTimeoutAlarmListener;
    private SensorManager mSensorManager;

    @GuardedBy({"this"})
    private int mState;
    private final ArraySet<DeviceIdleInternal.StationaryListener> mStationaryListeners;
    private final ArraySet<PowerAllowlistInternal.TempAllowlistChangeListener> mTempAllowlistChangeListeners;
    private int[] mTempWhitelistAppIdArray;
    private final SparseArray<Pair<MutableLong, String>> mTempWhitelistAppIdEndTimes;
    private final boolean mUseMotionSensor;

    @VisibleForTesting
    static String stateToString(int i) {
        switch (i) {
            case 0:
                return "ACTIVE";
            case 1:
                return "INACTIVE";
            case 2:
                return "IDLE_PENDING";
            case 3:
                return "SENSING";
            case 4:
                return "LOCATING";
            case 5:
                return "IDLE";
            case 6:
                return "IDLE_MAINTENANCE";
            case 7:
                return "QUICK_DOZE_DELAY";
            default:
                return Integer.toString(i);
        }
    }

    @VisibleForTesting
    static String lightStateToString(int i) {
        return i != 0 ? i != 1 ? i != 4 ? i != 5 ? i != 6 ? i != 7 ? Integer.toString(i) : "OVERRIDE" : "IDLE_MAINTENANCE" : "WAITING_FOR_NETWORK" : "IDLE" : "INACTIVE" : "ACTIVE";
    }

    private void addEvent(int i, String str) {
        int[] iArr = this.mEventCmds;
        if (iArr[0] != i) {
            System.arraycopy(iArr, 0, iArr, 1, 99);
            long[] jArr = this.mEventTimes;
            System.arraycopy(jArr, 0, jArr, 1, 99);
            String[] strArr = this.mEventReasons;
            System.arraycopy(strArr, 0, strArr, 1, 99);
            this.mEventCmds[0] = i;
            this.mEventTimes[0] = SystemClock.elapsedRealtime();
            this.mEventReasons[0] = str;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        synchronized (this) {
            stepLightIdleStateLocked("s:alarm");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        synchronized (this) {
            if (this.mStationaryListeners.size() > 0) {
                startMonitoringMotionLocked();
                scheduleMotionTimeoutAlarmLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        synchronized (this) {
            if (!isStationaryLocked()) {
                Slog.w(TAG, "motion timeout went off and device isn't stationary");
            } else {
                postStationaryStatusUpdated();
            }
        }
    }

    private void postStationaryStatus(DeviceIdleInternal.StationaryListener stationaryListener) {
        this.mHandler.obtainMessage(7, stationaryListener).sendToTarget();
    }

    private void postStationaryStatusUpdated() {
        this.mHandler.sendEmptyMessage(7);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"this"})
    public boolean isStationaryLocked() {
        long elapsedRealtime = this.mInjector.getElapsedRealtime();
        MotionListener motionListener = this.mMotionListener;
        return motionListener.active && elapsedRealtime - Math.max(motionListener.activatedTimeElapsed, this.mLastMotionEventElapsed) >= this.mConstants.MOTION_INACTIVE_TIMEOUT;
    }

    @VisibleForTesting
    void registerStationaryListener(DeviceIdleInternal.StationaryListener stationaryListener) {
        synchronized (this) {
            if (this.mStationaryListeners.add(stationaryListener)) {
                postStationaryStatus(stationaryListener);
                if (this.mMotionListener.active) {
                    if (!isStationaryLocked() && this.mStationaryListeners.size() == 1) {
                        scheduleMotionTimeoutAlarmLocked();
                    }
                } else {
                    startMonitoringMotionLocked();
                    scheduleMotionTimeoutAlarmLocked();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterStationaryListener(DeviceIdleInternal.StationaryListener stationaryListener) {
        int i;
        synchronized (this) {
            if (this.mStationaryListeners.remove(stationaryListener) && this.mStationaryListeners.size() == 0 && ((i = this.mState) == 0 || i == 1 || this.mQuickDozeActivated)) {
                maybeStopMonitoringMotionLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerTempAllowlistChangeListener(PowerAllowlistInternal.TempAllowlistChangeListener tempAllowlistChangeListener) {
        synchronized (this) {
            this.mTempAllowlistChangeListeners.add(tempAllowlistChangeListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterTempAllowlistChangeListener(PowerAllowlistInternal.TempAllowlistChangeListener tempAllowlistChangeListener) {
        synchronized (this) {
            this.mTempAllowlistChangeListeners.remove(tempAllowlistChangeListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class MotionListener extends TriggerEventListener implements SensorEventListener {
        long activatedTimeElapsed;
        boolean active = false;

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        MotionListener() {
        }

        public boolean isActive() {
            return this.active;
        }

        @Override // android.hardware.TriggerEventListener
        public void onTrigger(TriggerEvent triggerEvent) {
            synchronized (DeviceIdleController.this) {
                this.active = false;
                DeviceIdleController.this.motionLocked();
            }
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            synchronized (DeviceIdleController.this) {
                DeviceIdleController.this.mSensorManager.unregisterListener(this, DeviceIdleController.this.mMotionSensor);
                this.active = false;
                DeviceIdleController.this.motionLocked();
            }
        }

        public boolean registerLocked() {
            boolean registerListener;
            if (DeviceIdleController.this.mMotionSensor.getReportingMode() == 2) {
                SensorManager sensorManager = DeviceIdleController.this.mSensorManager;
                DeviceIdleController deviceIdleController = DeviceIdleController.this;
                registerListener = sensorManager.requestTriggerSensor(deviceIdleController.mMotionListener, deviceIdleController.mMotionSensor);
            } else {
                SensorManager sensorManager2 = DeviceIdleController.this.mSensorManager;
                DeviceIdleController deviceIdleController2 = DeviceIdleController.this;
                registerListener = sensorManager2.registerListener(deviceIdleController2.mMotionListener, deviceIdleController2.mMotionSensor, 3);
            }
            if (registerListener) {
                this.active = true;
                this.activatedTimeElapsed = DeviceIdleController.this.mInjector.getElapsedRealtime();
            } else {
                Slog.e(DeviceIdleController.TAG, "Unable to register for " + DeviceIdleController.this.mMotionSensor);
            }
            return registerListener;
        }

        public void unregisterLocked() {
            if (DeviceIdleController.this.mMotionSensor.getReportingMode() == 2) {
                SensorManager sensorManager = DeviceIdleController.this.mSensorManager;
                DeviceIdleController deviceIdleController = DeviceIdleController.this;
                sensorManager.cancelTriggerSensor(deviceIdleController.mMotionListener, deviceIdleController.mMotionSensor);
            } else {
                DeviceIdleController.this.mSensorManager.unregisterListener(DeviceIdleController.this.mMotionListener);
            }
            this.active = false;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class Constants implements DeviceConfig.OnPropertiesChangedListener {
        private static final long DEFAULT_IDLE_AFTER_INACTIVE_TIMEOUT_SMALL_BATTERY = 900000;
        private static final long DEFAULT_INACTIVE_TIMEOUT_SMALL_BATTERY = 900000;
        private static final String KEY_FLEX_TIME_SHORT = "flex_time_short";
        private static final String KEY_IDLE_AFTER_INACTIVE_TIMEOUT = "idle_after_inactive_to";
        private static final String KEY_IDLE_FACTOR = "idle_factor";
        private static final String KEY_IDLE_PENDING_FACTOR = "idle_pending_factor";
        private static final String KEY_IDLE_PENDING_TIMEOUT = "idle_pending_to";
        private static final String KEY_IDLE_TIMEOUT = "idle_to";
        private static final String KEY_INACTIVE_TIMEOUT = "inactive_to";
        private static final String KEY_LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT = "light_after_inactive_to";
        private static final String KEY_LIGHT_IDLE_FACTOR = "light_idle_factor";
        private static final String KEY_LIGHT_IDLE_MAINTENANCE_MAX_BUDGET = "light_idle_maintenance_max_budget";
        private static final String KEY_LIGHT_IDLE_MAINTENANCE_MIN_BUDGET = "light_idle_maintenance_min_budget";
        private static final String KEY_LIGHT_IDLE_TIMEOUT = "light_idle_to";
        private static final String KEY_LIGHT_IDLE_TIMEOUT_INITIAL_FLEX = "light_idle_to_initial_flex";
        private static final String KEY_LIGHT_IDLE_TIMEOUT_MAX_FLEX = "light_max_idle_to_flex";
        private static final String KEY_LIGHT_MAX_IDLE_TIMEOUT = "light_max_idle_to";
        private static final String KEY_LOCATING_TIMEOUT = "locating_to";
        private static final String KEY_LOCATION_ACCURACY = "location_accuracy";
        private static final String KEY_MAX_IDLE_PENDING_TIMEOUT = "max_idle_pending_to";
        private static final String KEY_MAX_IDLE_TIMEOUT = "max_idle_to";
        private static final String KEY_MAX_TEMP_APP_ALLOWLIST_DURATION_MS = "max_temp_app_allowlist_duration_ms";
        private static final String KEY_MIN_DEEP_MAINTENANCE_TIME = "min_deep_maintenance_time";
        private static final String KEY_MIN_LIGHT_MAINTENANCE_TIME = "min_light_maintenance_time";
        private static final String KEY_MIN_TIME_TO_ALARM = "min_time_to_alarm";
        private static final String KEY_MMS_TEMP_APP_ALLOWLIST_DURATION_MS = "mms_temp_app_allowlist_duration_ms";
        private static final String KEY_MOTION_INACTIVE_TIMEOUT = "motion_inactive_to";
        private static final String KEY_MOTION_INACTIVE_TIMEOUT_FLEX = "motion_inactive_to_flex";
        private static final String KEY_NOTIFICATION_ALLOWLIST_DURATION_MS = "notification_allowlist_duration_ms";
        private static final String KEY_PRE_IDLE_FACTOR_LONG = "pre_idle_factor_long";
        private static final String KEY_PRE_IDLE_FACTOR_SHORT = "pre_idle_factor_short";
        private static final String KEY_QUICK_DOZE_DELAY_TIMEOUT = "quick_doze_delay_to";
        private static final String KEY_SENSING_TIMEOUT = "sensing_to";
        private static final String KEY_SMS_TEMP_APP_ALLOWLIST_DURATION_MS = "sms_temp_app_allowlist_duration_ms";
        private static final String KEY_USE_WINDOW_ALARMS = "use_window_alarms";
        private static final String KEY_WAIT_FOR_UNLOCK = "wait_for_unlock";
        public long IDLE_AFTER_INACTIVE_TIMEOUT;
        public long INACTIVE_TIMEOUT;
        private final boolean mSmallBatteryDevice;
        private long mDefaultFlexTimeShort = 60000;
        private long mDefaultLightIdleAfterInactiveTimeout = 240000;
        private long mDefaultLightIdleTimeout = 300000;
        private long mDefaultLightIdleTimeoutInitialFlex = 60000;
        private long mDefaultLightIdleTimeoutMaxFlex = 900000;
        private float mDefaultLightIdleFactor = 2.0f;
        private long mDefaultLightMaxIdleTimeout = 900000;
        private long mDefaultLightIdleMaintenanceMinBudget = 60000;
        private long mDefaultLightIdleMaintenanceMaxBudget = 300000;
        private long mDefaultMinLightMaintenanceTime = 5000;
        private long mDefaultMinDeepMaintenanceTime = 30000;
        private long mDefaultInactiveTimeout = 1800000;
        private long mDefaultSensingTimeout = 240000;
        private long mDefaultLocatingTimeout = 30000;
        private float mDefaultLocationAccuracy = 20.0f;
        private long mDefaultMotionInactiveTimeout = BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_FUZZ_MILLISECONDS;
        private long mDefaultMotionInactiveTimeoutFlex = 60000;
        private long mDefaultIdleAfterInactiveTimeout = 1800000;
        private long mDefaultIdlePendingTimeout = 300000;
        private long mDefaultMaxIdlePendingTimeout = BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_FUZZ_MILLISECONDS;
        private float mDefaultIdlePendingFactor = 2.0f;
        private long mDefaultQuickDozeDelayTimeout = 60000;
        private long mDefaultIdleTimeout = ClipboardService.DEFAULT_CLIPBOARD_TIMEOUT_MILLIS;
        private long mDefaultMaxIdleTimeout = 21600000;
        private float mDefaultIdleFactor = 2.0f;
        private long mDefaultMinTimeToAlarm = 1800000;
        private long mDefaultMaxTempAppAllowlistDurationMs = 300000;
        private long mDefaultMmsTempAppAllowlistDurationMs = 60000;
        private long mDefaultSmsTempAppAllowlistDurationMs = 20000;
        private long mDefaultNotificationAllowlistDurationMs = 30000;
        private boolean mDefaultWaitForUnlock = true;
        private float mDefaultPreIdleFactorLong = 1.67f;
        private float mDefaultPreIdleFactorShort = 0.33f;
        private boolean mDefaultUseWindowAlarms = true;
        public long FLEX_TIME_SHORT = 60000;
        public long LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT = 240000;
        public long LIGHT_IDLE_TIMEOUT = 300000;
        public long LIGHT_IDLE_TIMEOUT_INITIAL_FLEX = 60000;
        public long LIGHT_IDLE_TIMEOUT_MAX_FLEX = 900000;
        public float LIGHT_IDLE_FACTOR = 2.0f;
        public long LIGHT_MAX_IDLE_TIMEOUT = 900000;
        public long LIGHT_IDLE_MAINTENANCE_MIN_BUDGET = 60000;
        public long LIGHT_IDLE_MAINTENANCE_MAX_BUDGET = 300000;
        public long MIN_LIGHT_MAINTENANCE_TIME = 5000;
        public long MIN_DEEP_MAINTENANCE_TIME = 30000;
        public long SENSING_TIMEOUT = 240000;
        public long LOCATING_TIMEOUT = 30000;
        public float LOCATION_ACCURACY = 20.0f;
        public long MOTION_INACTIVE_TIMEOUT = BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_FUZZ_MILLISECONDS;
        public long MOTION_INACTIVE_TIMEOUT_FLEX = 60000;
        public long IDLE_PENDING_TIMEOUT = 300000;
        public long MAX_IDLE_PENDING_TIMEOUT = BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_FUZZ_MILLISECONDS;
        public float IDLE_PENDING_FACTOR = 2.0f;
        public long QUICK_DOZE_DELAY_TIMEOUT = 60000;
        public long IDLE_TIMEOUT = ClipboardService.DEFAULT_CLIPBOARD_TIMEOUT_MILLIS;
        public long MAX_IDLE_TIMEOUT = 21600000;
        public float IDLE_FACTOR = 2.0f;
        public long MIN_TIME_TO_ALARM = 1800000;
        public long MAX_TEMP_APP_ALLOWLIST_DURATION_MS = 300000;
        public long MMS_TEMP_APP_ALLOWLIST_DURATION_MS = 60000;
        public long SMS_TEMP_APP_ALLOWLIST_DURATION_MS = 20000;
        public long NOTIFICATION_ALLOWLIST_DURATION_MS = 30000;
        public float PRE_IDLE_FACTOR_LONG = 1.67f;
        public float PRE_IDLE_FACTOR_SHORT = 0.33f;
        public boolean WAIT_FOR_UNLOCK = true;
        public boolean USE_WINDOW_ALARMS = true;

        private long getTimeout(long j, long j2) {
            return j;
        }

        public Constants() {
            this.INACTIVE_TIMEOUT = 1800000L;
            this.IDLE_AFTER_INACTIVE_TIMEOUT = 1800000L;
            initDefault();
            boolean isSmallBatteryDevice = ActivityManager.isSmallBatteryDevice();
            this.mSmallBatteryDevice = isSmallBatteryDevice;
            if (isSmallBatteryDevice) {
                this.INACTIVE_TIMEOUT = 900000L;
                this.IDLE_AFTER_INACTIVE_TIMEOUT = 900000L;
            }
            DeviceIdleController.this.mDeviceIdleExt.initArgs(this, DeviceIdleController.this.getContext(), DeviceIdleController.this.mHandler, DeviceIdleController.this);
            DeviceConfig.addOnPropertiesChangedListener("device_idle", AppSchedulingModuleThread.getExecutor(), this);
            onPropertiesChanged(DeviceConfig.getProperties("device_idle", new String[0]));
        }

        private void initDefault() {
            Resources resources = DeviceIdleController.this.getContext().getResources();
            this.mDefaultFlexTimeShort = getTimeout(resources.getInteger(17695012), this.mDefaultFlexTimeShort);
            this.mDefaultLightIdleAfterInactiveTimeout = getTimeout(resources.getInteger(17695019), this.mDefaultLightIdleAfterInactiveTimeout);
            this.mDefaultLightIdleTimeout = getTimeout(resources.getInteger(17695025), this.mDefaultLightIdleTimeout);
            this.mDefaultLightIdleTimeoutInitialFlex = getTimeout(resources.getInteger(17695023), this.mDefaultLightIdleTimeoutInitialFlex);
            this.mDefaultLightIdleTimeoutMaxFlex = getTimeout(resources.getInteger(17695024), this.mDefaultLightIdleTimeoutMaxFlex);
            this.mDefaultLightIdleFactor = resources.getFloat(17695020);
            this.mDefaultLightMaxIdleTimeout = getTimeout(resources.getInteger(17695026), this.mDefaultLightMaxIdleTimeout);
            this.mDefaultLightIdleMaintenanceMinBudget = getTimeout(resources.getInteger(17695022), this.mDefaultLightIdleMaintenanceMinBudget);
            this.mDefaultLightIdleMaintenanceMaxBudget = getTimeout(resources.getInteger(17695021), this.mDefaultLightIdleMaintenanceMaxBudget);
            this.mDefaultMinLightMaintenanceTime = getTimeout(resources.getInteger(17695033), this.mDefaultMinLightMaintenanceTime);
            this.mDefaultMinDeepMaintenanceTime = getTimeout(resources.getInteger(17695032), this.mDefaultMinDeepMaintenanceTime);
            this.mDefaultInactiveTimeout = getTimeout(resources.getInteger(17695018), this.mDefaultInactiveTimeout);
            this.mDefaultSensingTimeout = getTimeout(resources.getInteger(17695042), this.mDefaultSensingTimeout);
            this.mDefaultLocatingTimeout = getTimeout(resources.getInteger(17695027), this.mDefaultLocatingTimeout);
            this.mDefaultLocationAccuracy = resources.getFloat(17695028);
            this.mDefaultMotionInactiveTimeout = getTimeout(resources.getInteger(17695037), this.mDefaultMotionInactiveTimeout);
            this.mDefaultMotionInactiveTimeoutFlex = getTimeout(resources.getInteger(17695036), this.mDefaultMotionInactiveTimeoutFlex);
            this.mDefaultIdleAfterInactiveTimeout = getTimeout(resources.getInteger(17695013), this.mDefaultIdleAfterInactiveTimeout);
            this.mDefaultIdlePendingTimeout = getTimeout(resources.getInteger(17695016), this.mDefaultIdlePendingTimeout);
            this.mDefaultMaxIdlePendingTimeout = getTimeout(resources.getInteger(17695029), this.mDefaultMaxIdlePendingTimeout);
            this.mDefaultIdlePendingFactor = resources.getFloat(17695015);
            this.mDefaultQuickDozeDelayTimeout = getTimeout(resources.getInteger(17695041), this.mDefaultQuickDozeDelayTimeout);
            this.mDefaultIdleTimeout = getTimeout(resources.getInteger(17695017), this.mDefaultIdleTimeout);
            this.mDefaultMaxIdleTimeout = getTimeout(resources.getInteger(17695030), this.mDefaultMaxIdleTimeout);
            this.mDefaultIdleFactor = resources.getFloat(17695014);
            this.mDefaultMinTimeToAlarm = getTimeout(resources.getInteger(17695034), this.mDefaultMinTimeToAlarm);
            this.mDefaultMaxTempAppAllowlistDurationMs = resources.getInteger(17695031);
            this.mDefaultMmsTempAppAllowlistDurationMs = resources.getInteger(17695035);
            this.mDefaultSmsTempAppAllowlistDurationMs = resources.getInteger(17695043);
            this.mDefaultNotificationAllowlistDurationMs = resources.getInteger(17695038);
            this.mDefaultWaitForUnlock = resources.getBoolean(17891924);
            this.mDefaultPreIdleFactorLong = resources.getFloat(17695039);
            this.mDefaultPreIdleFactorShort = resources.getFloat(17695040);
            boolean z = resources.getBoolean(17891923);
            this.mDefaultUseWindowAlarms = z;
            this.FLEX_TIME_SHORT = this.mDefaultFlexTimeShort;
            this.LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT = this.mDefaultLightIdleAfterInactiveTimeout;
            this.LIGHT_IDLE_TIMEOUT = this.mDefaultLightIdleTimeout;
            this.LIGHT_IDLE_TIMEOUT_INITIAL_FLEX = this.mDefaultLightIdleTimeoutInitialFlex;
            this.LIGHT_IDLE_TIMEOUT_MAX_FLEX = this.mDefaultLightIdleTimeoutMaxFlex;
            this.LIGHT_IDLE_FACTOR = this.mDefaultLightIdleFactor;
            this.LIGHT_MAX_IDLE_TIMEOUT = this.mDefaultLightMaxIdleTimeout;
            this.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET = this.mDefaultLightIdleMaintenanceMinBudget;
            this.LIGHT_IDLE_MAINTENANCE_MAX_BUDGET = this.mDefaultLightIdleMaintenanceMaxBudget;
            this.MIN_LIGHT_MAINTENANCE_TIME = this.mDefaultMinLightMaintenanceTime;
            this.MIN_DEEP_MAINTENANCE_TIME = this.mDefaultMinDeepMaintenanceTime;
            this.INACTIVE_TIMEOUT = this.mDefaultInactiveTimeout;
            this.SENSING_TIMEOUT = this.mDefaultSensingTimeout;
            this.LOCATING_TIMEOUT = this.mDefaultLocatingTimeout;
            this.LOCATION_ACCURACY = this.mDefaultLocationAccuracy;
            this.MOTION_INACTIVE_TIMEOUT = this.mDefaultMotionInactiveTimeout;
            this.MOTION_INACTIVE_TIMEOUT_FLEX = this.mDefaultMotionInactiveTimeoutFlex;
            this.IDLE_AFTER_INACTIVE_TIMEOUT = this.mDefaultIdleAfterInactiveTimeout;
            this.IDLE_PENDING_TIMEOUT = this.mDefaultIdlePendingTimeout;
            this.MAX_IDLE_PENDING_TIMEOUT = this.mDefaultMaxIdlePendingTimeout;
            this.IDLE_PENDING_FACTOR = this.mDefaultIdlePendingFactor;
            this.QUICK_DOZE_DELAY_TIMEOUT = this.mDefaultQuickDozeDelayTimeout;
            this.IDLE_TIMEOUT = this.mDefaultIdleTimeout;
            this.MAX_IDLE_TIMEOUT = this.mDefaultMaxIdleTimeout;
            this.IDLE_FACTOR = this.mDefaultIdleFactor;
            this.MIN_TIME_TO_ALARM = this.mDefaultMinTimeToAlarm;
            this.MAX_TEMP_APP_ALLOWLIST_DURATION_MS = this.mDefaultMaxTempAppAllowlistDurationMs;
            this.MMS_TEMP_APP_ALLOWLIST_DURATION_MS = this.mDefaultMmsTempAppAllowlistDurationMs;
            this.SMS_TEMP_APP_ALLOWLIST_DURATION_MS = this.mDefaultSmsTempAppAllowlistDurationMs;
            this.NOTIFICATION_ALLOWLIST_DURATION_MS = this.mDefaultNotificationAllowlistDurationMs;
            this.WAIT_FOR_UNLOCK = this.mDefaultWaitForUnlock;
            this.PRE_IDLE_FACTOR_LONG = this.mDefaultPreIdleFactorLong;
            this.PRE_IDLE_FACTOR_SHORT = this.mDefaultPreIdleFactorShort;
            this.USE_WINDOW_ALARMS = z;
        }

        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            char c;
            synchronized (DeviceIdleController.this) {
                for (String str : properties.getKeyset()) {
                    if (str != null) {
                        switch (str.hashCode()) {
                            case -1781086459:
                                if (str.equals(KEY_NOTIFICATION_ALLOWLIST_DURATION_MS)) {
                                    c = 29;
                                    break;
                                }
                                break;
                            case -1102128050:
                                if (str.equals(KEY_LIGHT_IDLE_MAINTENANCE_MAX_BUDGET)) {
                                    c = '\b';
                                    break;
                                }
                                break;
                            case -1067343247:
                                if (str.equals(KEY_LIGHT_IDLE_FACTOR)) {
                                    c = 5;
                                    break;
                                }
                                break;
                            case -986742087:
                                if (str.equals(KEY_USE_WINDOW_ALARMS)) {
                                    c = '!';
                                    break;
                                }
                                break;
                            case -919175870:
                                if (str.equals(KEY_LIGHT_MAX_IDLE_TIMEOUT)) {
                                    c = 6;
                                    break;
                                }
                                break;
                            case -564968069:
                                if (str.equals(KEY_PRE_IDLE_FACTOR_SHORT)) {
                                    c = ' ';
                                    break;
                                }
                                break;
                            case -547781361:
                                if (str.equals(KEY_MIN_LIGHT_MAINTENANCE_TIME)) {
                                    c = '\t';
                                    break;
                                }
                                break;
                            case -492261706:
                                if (str.equals(KEY_SMS_TEMP_APP_ALLOWLIST_DURATION_MS)) {
                                    c = 28;
                                    break;
                                }
                                break;
                            case -318123838:
                                if (str.equals(KEY_IDLE_PENDING_FACTOR)) {
                                    c = 20;
                                    break;
                                }
                                break;
                            case -173192557:
                                if (str.equals(KEY_MAX_IDLE_PENDING_TIMEOUT)) {
                                    c = 19;
                                    break;
                                }
                                break;
                            case -80111214:
                                if (str.equals(KEY_MIN_TIME_TO_ALARM)) {
                                    c = 25;
                                    break;
                                }
                                break;
                            case 134792310:
                                if (str.equals(KEY_LIGHT_IDLE_TIMEOUT_INITIAL_FLEX)) {
                                    c = 3;
                                    break;
                                }
                                break;
                            case 197367965:
                                if (str.equals(KEY_LIGHT_IDLE_TIMEOUT)) {
                                    c = 2;
                                    break;
                                }
                                break;
                            case 361511631:
                                if (str.equals(KEY_INACTIVE_TIMEOUT)) {
                                    c = 11;
                                    break;
                                }
                                break;
                            case 370224338:
                                if (str.equals(KEY_MOTION_INACTIVE_TIMEOUT_FLEX)) {
                                    c = 16;
                                    break;
                                }
                                break;
                            case 415987654:
                                if (str.equals(KEY_MOTION_INACTIVE_TIMEOUT)) {
                                    c = 15;
                                    break;
                                }
                                break;
                            case 551187755:
                                if (str.equals(KEY_LOCATING_TIMEOUT)) {
                                    c = '\r';
                                    break;
                                }
                                break;
                            case 866187779:
                                if (str.equals(KEY_LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT)) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case 891348287:
                                if (str.equals(KEY_MIN_DEEP_MAINTENANCE_TIME)) {
                                    c = '\n';
                                    break;
                                }
                                break;
                            case 918455627:
                                if (str.equals(KEY_MAX_TEMP_APP_ALLOWLIST_DURATION_MS)) {
                                    c = 26;
                                    break;
                                }
                                break;
                            case 1001374852:
                                if (str.equals(KEY_WAIT_FOR_UNLOCK)) {
                                    c = 30;
                                    break;
                                }
                                break;
                            case 1228499357:
                                if (str.equals(KEY_PRE_IDLE_FACTOR_LONG)) {
                                    c = 31;
                                    break;
                                }
                                break;
                            case 1280980182:
                                if (str.equals(KEY_LIGHT_IDLE_TIMEOUT_MAX_FLEX)) {
                                    c = 4;
                                    break;
                                }
                                break;
                            case 1350761616:
                                if (str.equals(KEY_FLEX_TIME_SHORT)) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case 1369871264:
                                if (str.equals(KEY_LIGHT_IDLE_MAINTENANCE_MIN_BUDGET)) {
                                    c = 7;
                                    break;
                                }
                                break;
                            case 1383403841:
                                if (str.equals(KEY_IDLE_AFTER_INACTIVE_TIMEOUT)) {
                                    c = 17;
                                    break;
                                }
                                break;
                            case 1536604751:
                                if (str.equals(KEY_SENSING_TIMEOUT)) {
                                    c = '\f';
                                    break;
                                }
                                break;
                            case 1547108378:
                                if (str.equals(KEY_IDLE_FACTOR)) {
                                    c = 24;
                                    break;
                                }
                                break;
                            case 1563458830:
                                if (str.equals(KEY_QUICK_DOZE_DELAY_TIMEOUT)) {
                                    c = 21;
                                    break;
                                }
                                break;
                            case 1664365254:
                                if (str.equals(KEY_IDLE_TIMEOUT)) {
                                    c = 22;
                                    break;
                                }
                                break;
                            case 1679398766:
                                if (str.equals(KEY_IDLE_PENDING_TIMEOUT)) {
                                    c = 18;
                                    break;
                                }
                                break;
                            case 1695275755:
                                if (str.equals(KEY_MAX_IDLE_TIMEOUT)) {
                                    c = 23;
                                    break;
                                }
                                break;
                            case 1930831427:
                                if (str.equals(KEY_LOCATION_ACCURACY)) {
                                    c = 14;
                                    break;
                                }
                                break;
                            case 1944720892:
                                if (str.equals(KEY_MMS_TEMP_APP_ALLOWLIST_DURATION_MS)) {
                                    c = 27;
                                    break;
                                }
                                break;
                        }
                        c = 65535;
                        long j = 900000;
                        switch (c) {
                            case 0:
                                this.FLEX_TIME_SHORT = properties.getLong(KEY_FLEX_TIME_SHORT, this.mDefaultFlexTimeShort);
                                break;
                            case 1:
                                this.LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT = properties.getLong(KEY_LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT, this.mDefaultLightIdleAfterInactiveTimeout);
                                break;
                            case 2:
                                this.LIGHT_IDLE_TIMEOUT = properties.getLong(KEY_LIGHT_IDLE_TIMEOUT, this.mDefaultLightIdleTimeout);
                                break;
                            case 3:
                                this.LIGHT_IDLE_TIMEOUT_INITIAL_FLEX = properties.getLong(KEY_LIGHT_IDLE_TIMEOUT_INITIAL_FLEX, this.mDefaultLightIdleTimeoutInitialFlex);
                                break;
                            case 4:
                                this.LIGHT_IDLE_TIMEOUT_MAX_FLEX = properties.getLong(KEY_LIGHT_IDLE_TIMEOUT_MAX_FLEX, this.mDefaultLightIdleTimeoutMaxFlex);
                                break;
                            case 5:
                                this.LIGHT_IDLE_FACTOR = Math.max(1.0f, properties.getFloat(KEY_LIGHT_IDLE_FACTOR, this.mDefaultLightIdleFactor));
                                break;
                            case 6:
                                this.LIGHT_MAX_IDLE_TIMEOUT = properties.getLong(KEY_LIGHT_MAX_IDLE_TIMEOUT, this.mDefaultLightMaxIdleTimeout);
                                break;
                            case 7:
                                this.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET = properties.getLong(KEY_LIGHT_IDLE_MAINTENANCE_MIN_BUDGET, this.mDefaultLightIdleMaintenanceMinBudget);
                                break;
                            case '\b':
                                this.LIGHT_IDLE_MAINTENANCE_MAX_BUDGET = properties.getLong(KEY_LIGHT_IDLE_MAINTENANCE_MAX_BUDGET, this.mDefaultLightIdleMaintenanceMaxBudget);
                                break;
                            case '\t':
                                this.MIN_LIGHT_MAINTENANCE_TIME = properties.getLong(KEY_MIN_LIGHT_MAINTENANCE_TIME, this.mDefaultMinLightMaintenanceTime);
                                break;
                            case '\n':
                                this.MIN_DEEP_MAINTENANCE_TIME = properties.getLong(KEY_MIN_DEEP_MAINTENANCE_TIME, this.mDefaultMinDeepMaintenanceTime);
                                break;
                            case 11:
                                if (!this.mSmallBatteryDevice) {
                                    j = this.mDefaultInactiveTimeout;
                                }
                                this.INACTIVE_TIMEOUT = properties.getLong(KEY_INACTIVE_TIMEOUT, j);
                                break;
                            case '\f':
                                this.SENSING_TIMEOUT = properties.getLong(KEY_SENSING_TIMEOUT, this.mDefaultSensingTimeout);
                                break;
                            case '\r':
                                this.LOCATING_TIMEOUT = properties.getLong(KEY_LOCATING_TIMEOUT, this.mDefaultLocatingTimeout);
                                break;
                            case 14:
                                this.LOCATION_ACCURACY = properties.getFloat(KEY_LOCATION_ACCURACY, this.mDefaultLocationAccuracy);
                                break;
                            case 15:
                                this.MOTION_INACTIVE_TIMEOUT = properties.getLong(KEY_MOTION_INACTIVE_TIMEOUT, this.mDefaultMotionInactiveTimeout);
                                break;
                            case 16:
                                this.MOTION_INACTIVE_TIMEOUT_FLEX = properties.getLong(KEY_MOTION_INACTIVE_TIMEOUT_FLEX, this.mDefaultMotionInactiveTimeoutFlex);
                                break;
                            case 17:
                                if (!this.mSmallBatteryDevice) {
                                    j = this.mDefaultIdleAfterInactiveTimeout;
                                }
                                this.IDLE_AFTER_INACTIVE_TIMEOUT = properties.getLong(KEY_IDLE_AFTER_INACTIVE_TIMEOUT, j);
                                break;
                            case 18:
                                this.IDLE_PENDING_TIMEOUT = properties.getLong(KEY_IDLE_PENDING_TIMEOUT, this.mDefaultIdlePendingTimeout);
                                break;
                            case 19:
                                this.MAX_IDLE_PENDING_TIMEOUT = properties.getLong(KEY_MAX_IDLE_PENDING_TIMEOUT, this.mDefaultMaxIdlePendingTimeout);
                                break;
                            case 20:
                                this.IDLE_PENDING_FACTOR = properties.getFloat(KEY_IDLE_PENDING_FACTOR, this.mDefaultIdlePendingFactor);
                                break;
                            case 21:
                                this.QUICK_DOZE_DELAY_TIMEOUT = properties.getLong(KEY_QUICK_DOZE_DELAY_TIMEOUT, this.mDefaultQuickDozeDelayTimeout);
                                break;
                            case 22:
                                this.IDLE_TIMEOUT = properties.getLong(KEY_IDLE_TIMEOUT, this.mDefaultIdleTimeout);
                                break;
                            case 23:
                                this.MAX_IDLE_TIMEOUT = properties.getLong(KEY_MAX_IDLE_TIMEOUT, this.mDefaultMaxIdleTimeout);
                                break;
                            case 24:
                                this.IDLE_FACTOR = properties.getFloat(KEY_IDLE_FACTOR, this.mDefaultIdleFactor);
                                break;
                            case 25:
                                this.MIN_TIME_TO_ALARM = properties.getLong(KEY_MIN_TIME_TO_ALARM, this.mDefaultMinTimeToAlarm);
                                break;
                            case 26:
                                this.MAX_TEMP_APP_ALLOWLIST_DURATION_MS = properties.getLong(KEY_MAX_TEMP_APP_ALLOWLIST_DURATION_MS, this.mDefaultMaxTempAppAllowlistDurationMs);
                                break;
                            case 27:
                                this.MMS_TEMP_APP_ALLOWLIST_DURATION_MS = properties.getLong(KEY_MMS_TEMP_APP_ALLOWLIST_DURATION_MS, this.mDefaultMmsTempAppAllowlistDurationMs);
                                break;
                            case 28:
                                this.SMS_TEMP_APP_ALLOWLIST_DURATION_MS = properties.getLong(KEY_SMS_TEMP_APP_ALLOWLIST_DURATION_MS, this.mDefaultSmsTempAppAllowlistDurationMs);
                                break;
                            case 29:
                                this.NOTIFICATION_ALLOWLIST_DURATION_MS = properties.getLong(KEY_NOTIFICATION_ALLOWLIST_DURATION_MS, this.mDefaultNotificationAllowlistDurationMs);
                                break;
                            case 30:
                                this.WAIT_FOR_UNLOCK = properties.getBoolean(KEY_WAIT_FOR_UNLOCK, this.mDefaultWaitForUnlock);
                                break;
                            case 31:
                                this.PRE_IDLE_FACTOR_LONG = properties.getFloat(KEY_PRE_IDLE_FACTOR_LONG, this.mDefaultPreIdleFactorLong);
                                break;
                            case ' ':
                                this.PRE_IDLE_FACTOR_SHORT = properties.getFloat(KEY_PRE_IDLE_FACTOR_SHORT, this.mDefaultPreIdleFactorShort);
                                break;
                            case '!':
                                this.USE_WINDOW_ALARMS = properties.getBoolean(KEY_USE_WINDOW_ALARMS, this.mDefaultUseWindowAlarms);
                                break;
                            default:
                                Slog.e(DeviceIdleController.TAG, "Unknown configuration key: " + str);
                                break;
                        }
                    }
                }
            }
        }

        void dump(PrintWriter printWriter) {
            printWriter.println("  Settings:");
            printWriter.print("    ");
            printWriter.print(KEY_FLEX_TIME_SHORT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.FLEX_TIME_SHORT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_LIGHT_IDLE_TIMEOUT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.LIGHT_IDLE_TIMEOUT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_LIGHT_IDLE_TIMEOUT_INITIAL_FLEX);
            printWriter.print("=");
            TimeUtils.formatDuration(this.LIGHT_IDLE_TIMEOUT_INITIAL_FLEX, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_LIGHT_IDLE_TIMEOUT_MAX_FLEX);
            printWriter.print("=");
            TimeUtils.formatDuration(this.LIGHT_IDLE_TIMEOUT_MAX_FLEX, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_LIGHT_IDLE_FACTOR);
            printWriter.print("=");
            printWriter.print(this.LIGHT_IDLE_FACTOR);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_LIGHT_MAX_IDLE_TIMEOUT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.LIGHT_MAX_IDLE_TIMEOUT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_LIGHT_IDLE_MAINTENANCE_MIN_BUDGET);
            printWriter.print("=");
            TimeUtils.formatDuration(this.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_LIGHT_IDLE_MAINTENANCE_MAX_BUDGET);
            printWriter.print("=");
            TimeUtils.formatDuration(this.LIGHT_IDLE_MAINTENANCE_MAX_BUDGET, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_MIN_LIGHT_MAINTENANCE_TIME);
            printWriter.print("=");
            TimeUtils.formatDuration(this.MIN_LIGHT_MAINTENANCE_TIME, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_MIN_DEEP_MAINTENANCE_TIME);
            printWriter.print("=");
            TimeUtils.formatDuration(this.MIN_DEEP_MAINTENANCE_TIME, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_INACTIVE_TIMEOUT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.INACTIVE_TIMEOUT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_SENSING_TIMEOUT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.SENSING_TIMEOUT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_LOCATING_TIMEOUT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.LOCATING_TIMEOUT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_LOCATION_ACCURACY);
            printWriter.print("=");
            printWriter.print(this.LOCATION_ACCURACY);
            printWriter.print("m");
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_MOTION_INACTIVE_TIMEOUT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.MOTION_INACTIVE_TIMEOUT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_MOTION_INACTIVE_TIMEOUT_FLEX);
            printWriter.print("=");
            TimeUtils.formatDuration(this.MOTION_INACTIVE_TIMEOUT_FLEX, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_IDLE_AFTER_INACTIVE_TIMEOUT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.IDLE_AFTER_INACTIVE_TIMEOUT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_IDLE_PENDING_TIMEOUT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.IDLE_PENDING_TIMEOUT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_MAX_IDLE_PENDING_TIMEOUT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.MAX_IDLE_PENDING_TIMEOUT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_IDLE_PENDING_FACTOR);
            printWriter.print("=");
            printWriter.println(this.IDLE_PENDING_FACTOR);
            printWriter.print("    ");
            printWriter.print(KEY_QUICK_DOZE_DELAY_TIMEOUT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.QUICK_DOZE_DELAY_TIMEOUT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_IDLE_TIMEOUT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.IDLE_TIMEOUT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_MAX_IDLE_TIMEOUT);
            printWriter.print("=");
            TimeUtils.formatDuration(this.MAX_IDLE_TIMEOUT, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_IDLE_FACTOR);
            printWriter.print("=");
            printWriter.println(this.IDLE_FACTOR);
            printWriter.print("    ");
            printWriter.print(KEY_MIN_TIME_TO_ALARM);
            printWriter.print("=");
            TimeUtils.formatDuration(this.MIN_TIME_TO_ALARM, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_MAX_TEMP_APP_ALLOWLIST_DURATION_MS);
            printWriter.print("=");
            TimeUtils.formatDuration(this.MAX_TEMP_APP_ALLOWLIST_DURATION_MS, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_MMS_TEMP_APP_ALLOWLIST_DURATION_MS);
            printWriter.print("=");
            TimeUtils.formatDuration(this.MMS_TEMP_APP_ALLOWLIST_DURATION_MS, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_SMS_TEMP_APP_ALLOWLIST_DURATION_MS);
            printWriter.print("=");
            TimeUtils.formatDuration(this.SMS_TEMP_APP_ALLOWLIST_DURATION_MS, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_NOTIFICATION_ALLOWLIST_DURATION_MS);
            printWriter.print("=");
            TimeUtils.formatDuration(this.NOTIFICATION_ALLOWLIST_DURATION_MS, printWriter);
            printWriter.println();
            printWriter.print("    ");
            printWriter.print(KEY_WAIT_FOR_UNLOCK);
            printWriter.print("=");
            printWriter.println(this.WAIT_FOR_UNLOCK);
            printWriter.print("    ");
            printWriter.print(KEY_PRE_IDLE_FACTOR_LONG);
            printWriter.print("=");
            printWriter.println(this.PRE_IDLE_FACTOR_LONG);
            printWriter.print("    ");
            printWriter.print(KEY_PRE_IDLE_FACTOR_SHORT);
            printWriter.print("=");
            printWriter.println(this.PRE_IDLE_FACTOR_SHORT);
            printWriter.print("    ");
            printWriter.print(KEY_USE_WINDOW_ALARMS);
            printWriter.print("=");
            printWriter.println(this.USE_WINDOW_ALARMS);
        }
    }

    @Override // com.android.server.AnyMotionDetector.DeviceIdleCallback
    public void onAnyMotionResult(int i) {
        synchronized (this) {
            if (i != -1) {
                try {
                    cancelSensingTimeoutAlarmLocked();
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (i != 1 && i != -1) {
                if (i == 0) {
                    int i2 = this.mState;
                    if (i2 == 3) {
                        this.mNotMoving = true;
                        stepIdleStateLocked("s:stationary");
                    } else if (i2 == 4) {
                        this.mNotMoving = true;
                        if (this.mLocated) {
                            stepIdleStateLocked("s:stationary");
                        }
                    }
                }
            }
            handleMotionDetectedLocked(this.mConstants.INACTIVE_TIMEOUT, "non_stationary");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class MyHandler extends Handler {
        MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            boolean deviceIdleMode;
            boolean lightDeviceIdleMode;
            boolean isStationaryLocked;
            DeviceIdleInternal.StationaryListener[] stationaryListenerArr;
            PowerAllowlistInternal.TempAllowlistChangeListener[] tempAllowlistChangeListenerArr;
            int i = 0;
            switch (message.what) {
                case 1:
                    DeviceIdleController.this.handleWriteConfigFile();
                    return;
                case 2:
                case 3:
                    EventLogTags.writeDeviceIdleOnStart();
                    if (message.what == 2) {
                        deviceIdleMode = DeviceIdleController.this.mLocalPowerManager.setDeviceIdleMode(true);
                        lightDeviceIdleMode = DeviceIdleController.this.mLocalPowerManager.setLightDeviceIdleMode(false);
                    } else {
                        deviceIdleMode = DeviceIdleController.this.mLocalPowerManager.setDeviceIdleMode(false);
                        lightDeviceIdleMode = DeviceIdleController.this.mLocalPowerManager.setLightDeviceIdleMode(true);
                    }
                    try {
                        DeviceIdleController.this.mNetworkPolicyManager.setDeviceIdleMode(true);
                        DeviceIdleController.this.mBatteryStats.noteDeviceIdleMode(message.what == 2 ? 2 : 1, (String) null, Process.myUid());
                    } catch (RemoteException unused) {
                    }
                    if (deviceIdleMode) {
                        DeviceIdleController.this.getContext().sendBroadcastAsUser(DeviceIdleController.this.mIdleIntent, UserHandle.ALL, null, DeviceIdleController.this.mIdleIntentOptions);
                    }
                    if (lightDeviceIdleMode) {
                        DeviceIdleController.this.getContext().sendBroadcastAsUser(DeviceIdleController.this.mLightIdleIntent, UserHandle.ALL, null, DeviceIdleController.this.mLightIdleIntentOptions);
                    }
                    EventLogTags.writeDeviceIdleOnComplete();
                    DeviceIdleController.this.mDeviceIdleExt.onIdleOn(DeviceIdleController.this.mPowerSaveWhitelistUserApps, message.what == 2);
                    DeviceIdleController.this.mGoingIdleWakeLock.release();
                    return;
                case 4:
                    EventLogTags.writeDeviceIdleOffStart("unknown");
                    boolean deviceIdleMode2 = DeviceIdleController.this.mLocalPowerManager.setDeviceIdleMode(false);
                    boolean lightDeviceIdleMode2 = DeviceIdleController.this.mLocalPowerManager.setLightDeviceIdleMode(false);
                    try {
                        DeviceIdleController.this.mNetworkPolicyManager.setDeviceIdleMode(false);
                        DeviceIdleController.this.mBatteryStats.noteDeviceIdleMode(0, (String) null, Process.myUid());
                    } catch (RemoteException unused2) {
                    }
                    if (deviceIdleMode2) {
                        DeviceIdleController.this.incActiveIdleOps();
                        DeviceIdleController.this.mLocalActivityManager.broadcastIntentWithCallback(DeviceIdleController.this.mIdleIntent, DeviceIdleController.this.mIdleStartedDoneReceiver, (String[]) null, -1, (int[]) null, (BiFunction) null, DeviceIdleController.this.mIdleIntentOptions);
                        if (DeviceIdleController.this.mState == 6) {
                            DeviceIdleController.this.mDeviceIdleExt.onBroadcastIdleState();
                        }
                    }
                    if (lightDeviceIdleMode2) {
                        DeviceIdleController.this.incActiveIdleOps();
                        DeviceIdleController.this.mLocalActivityManager.broadcastIntentWithCallback(DeviceIdleController.this.mLightIdleIntent, DeviceIdleController.this.mIdleStartedDoneReceiver, (String[]) null, -1, (int[]) null, (BiFunction) null, DeviceIdleController.this.mLightIdleIntentOptions);
                    }
                    DeviceIdleController.this.decActiveIdleOps();
                    EventLogTags.writeDeviceIdleOffComplete();
                    return;
                case 5:
                    String str = (String) message.obj;
                    int i2 = message.arg1;
                    EventLogTags.writeDeviceIdleOffStart(str != null ? str : "unknown");
                    boolean deviceIdleMode3 = DeviceIdleController.this.mLocalPowerManager.setDeviceIdleMode(false);
                    boolean lightDeviceIdleMode3 = DeviceIdleController.this.mLocalPowerManager.setLightDeviceIdleMode(false);
                    try {
                        DeviceIdleController.this.mNetworkPolicyManager.setDeviceIdleMode(false);
                        DeviceIdleController.this.mBatteryStats.noteDeviceIdleMode(0, str, i2);
                    } catch (RemoteException unused3) {
                    }
                    if (deviceIdleMode3) {
                        DeviceIdleController.this.getContext().sendBroadcastAsUser(DeviceIdleController.this.mIdleIntent, UserHandle.ALL, null, DeviceIdleController.this.mIdleIntentOptions);
                    }
                    if (lightDeviceIdleMode3) {
                        DeviceIdleController.this.getContext().sendBroadcastAsUser(DeviceIdleController.this.mLightIdleIntent, UserHandle.ALL, null, DeviceIdleController.this.mLightIdleIntentOptions);
                    }
                    DeviceIdleController.this.mDeviceIdleExt.onIdleExit();
                    EventLogTags.writeDeviceIdleOffComplete();
                    return;
                case 6:
                    DeviceIdleController.this.checkTempAppWhitelistTimeout(message.arg1);
                    return;
                case 7:
                    DeviceIdleInternal.StationaryListener stationaryListener = (DeviceIdleInternal.StationaryListener) message.obj;
                    synchronized (DeviceIdleController.this) {
                        isStationaryLocked = DeviceIdleController.this.isStationaryLocked();
                        stationaryListenerArr = stationaryListener == null ? (DeviceIdleInternal.StationaryListener[]) DeviceIdleController.this.mStationaryListeners.toArray(new DeviceIdleInternal.StationaryListener[DeviceIdleController.this.mStationaryListeners.size()]) : null;
                    }
                    if (stationaryListenerArr != null) {
                        int length = stationaryListenerArr.length;
                        while (i < length) {
                            stationaryListenerArr[i].onDeviceStationaryChanged(isStationaryLocked);
                            i++;
                        }
                    }
                    if (stationaryListener != null) {
                        stationaryListener.onDeviceStationaryChanged(isStationaryLocked);
                        return;
                    }
                    return;
                case 8:
                    DeviceIdleController.this.decActiveIdleOps();
                    return;
                case 9:
                default:
                    return;
                case 10:
                    IDeviceIdleConstraint iDeviceIdleConstraint = (IDeviceIdleConstraint) message.obj;
                    if (message.arg1 == 1) {
                        iDeviceIdleConstraint.startMonitoring();
                        return;
                    } else {
                        iDeviceIdleConstraint.stopMonitoring();
                        return;
                    }
                case 11:
                    DeviceIdleController.this.updatePreIdleFactor();
                    return;
                case 12:
                    DeviceIdleController.this.updatePreIdleFactor();
                    DeviceIdleController.this.maybeDoImmediateMaintenance("idle factor");
                    return;
                case 13:
                    int i3 = message.arg1;
                    boolean z = message.arg2 == 1;
                    synchronized (DeviceIdleController.this) {
                        tempAllowlistChangeListenerArr = (PowerAllowlistInternal.TempAllowlistChangeListener[]) DeviceIdleController.this.mTempAllowlistChangeListeners.toArray(new PowerAllowlistInternal.TempAllowlistChangeListener[DeviceIdleController.this.mTempAllowlistChangeListeners.size()]);
                    }
                    int length2 = tempAllowlistChangeListenerArr.length;
                    while (i < length2) {
                        PowerAllowlistInternal.TempAllowlistChangeListener tempAllowlistChangeListener = tempAllowlistChangeListenerArr[i];
                        if (z) {
                            tempAllowlistChangeListener.onAppAdded(i3);
                        } else {
                            tempAllowlistChangeListener.onAppRemoved(i3);
                        }
                        i++;
                    }
                    return;
                case 14:
                    DeviceIdleController.this.mNetworkPolicyManagerInternal.onTempPowerSaveWhitelistChange(message.arg1, true, message.arg2, (String) message.obj);
                    return;
                case 15:
                    DeviceIdleController.this.mNetworkPolicyManagerInternal.onTempPowerSaveWhitelistChange(message.arg1, false, 0, (String) null);
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class BinderService extends IDeviceIdleController.Stub {
        private BinderService() {
        }

        public void addPowerSaveWhitelistApp(String str) {
            addPowerSaveWhitelistApps(Collections.singletonList(str));
        }

        public int addPowerSaveWhitelistApps(List<String> list) {
            DeviceIdleController.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            DeviceIdleController.this.mDeviceIdleExt.addInvalidDozeWhitelist(list);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DeviceIdleController.this.addPowerSaveWhitelistAppsInternal(list);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void removePowerSaveWhitelistApp(String str) {
            DeviceIdleController.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (!DeviceIdleController.this.removePowerSaveWhitelistAppInternal(str) && DeviceIdleController.this.mPowerSaveWhitelistAppsExceptIdle.containsKey(str)) {
                    throw new UnsupportedOperationException("Cannot remove system whitelisted app");
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void removeSystemPowerWhitelistApp(String str) {
            DeviceIdleController.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DeviceIdleController.this.removeSystemPowerWhitelistAppInternal(str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void restoreSystemPowerWhitelistApp(String str) {
            DeviceIdleController.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DeviceIdleController.this.restoreSystemPowerWhitelistAppInternal(str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public String[] getRemovedSystemPowerWhitelistApps() {
            return DeviceIdleController.this.getRemovedSystemPowerWhitelistAppsInternal(Binder.getCallingUid(), UserHandle.getCallingUserId());
        }

        public String[] getSystemPowerWhitelistExceptIdle() {
            return DeviceIdleController.this.getSystemPowerWhitelistExceptIdleInternal(Binder.getCallingUid(), UserHandle.getCallingUserId());
        }

        public String[] getSystemPowerWhitelist() {
            return DeviceIdleController.this.getSystemPowerWhitelistInternal(Binder.getCallingUid(), UserHandle.getCallingUserId());
        }

        public String[] getUserPowerWhitelist() {
            return DeviceIdleController.this.getUserPowerWhitelistInternal(Binder.getCallingUid(), UserHandle.getCallingUserId());
        }

        public String[] getFullPowerWhitelistExceptIdle() {
            return DeviceIdleController.this.getFullPowerWhitelistExceptIdleInternal(Binder.getCallingUid(), UserHandle.getCallingUserId());
        }

        public String[] getFullPowerWhitelist() {
            return DeviceIdleController.this.getFullPowerWhitelistInternal(Binder.getCallingUid(), UserHandle.getCallingUserId());
        }

        public int[] getAppIdWhitelistExceptIdle() {
            return DeviceIdleController.this.getAppIdWhitelistExceptIdleInternal();
        }

        public int[] getAppIdWhitelist() {
            return DeviceIdleController.this.getAppIdWhitelistInternal();
        }

        public int[] getAppIdUserWhitelist() {
            return DeviceIdleController.this.getAppIdUserWhitelistInternal();
        }

        public int[] getAppIdTempWhitelist() {
            return DeviceIdleController.this.getAppIdTempWhitelistInternal();
        }

        public boolean isPowerSaveWhitelistExceptIdleApp(String str) {
            if (DeviceIdleController.this.mPackageManagerInternal.filterAppAccess(str, Binder.getCallingUid(), UserHandle.getCallingUserId())) {
                return false;
            }
            return DeviceIdleController.this.isPowerSaveWhitelistExceptIdleAppInternal(str);
        }

        public boolean isPowerSaveWhitelistApp(String str) {
            if (DeviceIdleController.this.mPackageManagerInternal.filterAppAccess(str, Binder.getCallingUid(), UserHandle.getCallingUserId())) {
                return false;
            }
            return DeviceIdleController.this.isPowerSaveWhitelistAppInternal(str);
        }

        public long whitelistAppTemporarily(String str, int i, int i2, String str2) throws RemoteException {
            long max = Math.max(IDeviceIdleControllerExt.ADVANCE_TIME, DeviceIdleController.this.mConstants.MAX_TEMP_APP_ALLOWLIST_DURATION_MS / 2);
            DeviceIdleController.this.addPowerSaveTempAllowlistAppChecked(str, max, i, i2, str2);
            return max;
        }

        public void addPowerSaveTempWhitelistApp(String str, long j, int i, int i2, String str2) throws RemoteException {
            DeviceIdleController.this.addPowerSaveTempAllowlistAppChecked(str, j, i, i2, str2);
        }

        public long addPowerSaveTempWhitelistAppForMms(String str, int i, int i2, String str2) throws RemoteException {
            long j = DeviceIdleController.this.mConstants.MMS_TEMP_APP_ALLOWLIST_DURATION_MS;
            DeviceIdleController.this.addPowerSaveTempAllowlistAppChecked(str, j, i, i2, str2);
            return j;
        }

        public long addPowerSaveTempWhitelistAppForSms(String str, int i, int i2, String str2) throws RemoteException {
            long j = DeviceIdleController.this.mConstants.SMS_TEMP_APP_ALLOWLIST_DURATION_MS;
            DeviceIdleController.this.addPowerSaveTempAllowlistAppChecked(str, j, i, i2, str2);
            return j;
        }

        public void exitIdle(String str) {
            DeviceIdleController.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DeviceIdleController.this.exitIdleInternal(str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int setPreIdleTimeoutMode(int i) {
            DeviceIdleController.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DeviceIdleController.this.setPreIdleTimeoutMode(i);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void resetPreIdleTimeoutMode() {
            DeviceIdleController.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DeviceIdleController.this.resetPreIdleTimeoutMode();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            DeviceIdleController.this.dump(fileDescriptor, printWriter, strArr);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
            new Shell().exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class LocalService implements DeviceIdleInternal {
        private LocalService() {
        }

        public void onConstraintStateChanged(IDeviceIdleConstraint iDeviceIdleConstraint, boolean z) {
            synchronized (DeviceIdleController.this) {
                DeviceIdleController.this.onConstraintStateChangedLocked(iDeviceIdleConstraint, z);
            }
        }

        public void registerDeviceIdleConstraint(IDeviceIdleConstraint iDeviceIdleConstraint, String str, int i) {
            DeviceIdleController.this.registerDeviceIdleConstraintInternal(iDeviceIdleConstraint, str, i);
        }

        public void unregisterDeviceIdleConstraint(IDeviceIdleConstraint iDeviceIdleConstraint) {
            DeviceIdleController.this.unregisterDeviceIdleConstraintInternal(iDeviceIdleConstraint);
        }

        public void exitIdle(String str) {
            DeviceIdleController.this.exitIdleInternal(str);
        }

        public void addPowerSaveTempWhitelistApp(int i, String str, long j, int i2, boolean z, int i3, String str2) {
            DeviceIdleController.this.addPowerSaveTempAllowlistAppInternal(i, str, j, 0, i2, z, i3, str2);
        }

        public void addPowerSaveTempWhitelistApp(int i, String str, long j, int i2, int i3, boolean z, int i4, String str2) {
            DeviceIdleController.this.addPowerSaveTempAllowlistAppInternal(i, str, j, i2, i3, z, i4, str2);
        }

        public void addPowerSaveTempWhitelistAppDirect(int i, long j, int i2, boolean z, int i3, String str, int i4) {
            DeviceIdleController.this.addPowerSaveTempWhitelistAppDirectInternal(i4, i, j, i2, z, i3, str);
        }

        public long getNotificationAllowlistDuration() {
            return DeviceIdleController.this.mConstants.NOTIFICATION_ALLOWLIST_DURATION_MS;
        }

        public void setJobsActive(boolean z) {
            DeviceIdleController.this.setJobsActive(z);
        }

        public void setAlarmsActive(boolean z) {
            DeviceIdleController.this.setAlarmsActive(z);
        }

        public boolean isAppOnWhitelist(int i) {
            return DeviceIdleController.this.isAppOnWhitelistInternal(i);
        }

        public int[] getPowerSaveWhitelistUserAppIds() {
            return DeviceIdleController.this.getPowerSaveWhitelistUserAppIds();
        }

        public int[] getPowerSaveTempWhitelistAppIds() {
            return DeviceIdleController.this.getAppIdTempWhitelistInternal();
        }

        public void registerStationaryListener(DeviceIdleInternal.StationaryListener stationaryListener) {
            DeviceIdleController.this.registerStationaryListener(stationaryListener);
        }

        public void unregisterStationaryListener(DeviceIdleInternal.StationaryListener stationaryListener) {
            DeviceIdleController.this.unregisterStationaryListener(stationaryListener);
        }

        public int getTempAllowListType(int i, int i2) {
            return DeviceIdleController.this.getTempAllowListType(i, i2);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class LocalPowerAllowlistService implements PowerAllowlistInternal {
        private LocalPowerAllowlistService() {
        }

        public void registerTempAllowlistChangeListener(PowerAllowlistInternal.TempAllowlistChangeListener tempAllowlistChangeListener) {
            DeviceIdleController.this.registerTempAllowlistChangeListener(tempAllowlistChangeListener);
        }

        public void unregisterTempAllowlistChangeListener(PowerAllowlistInternal.TempAllowlistChangeListener tempAllowlistChangeListener) {
            DeviceIdleController.this.unregisterTempAllowlistChangeListener(tempAllowlistChangeListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class EmergencyCallListener extends TelephonyCallback implements TelephonyCallback.OutgoingEmergencyCallListener, TelephonyCallback.CallStateListener {
        private volatile boolean mIsEmergencyCallActive;

        private EmergencyCallListener() {
        }

        public void onOutgoingEmergencyCall(EmergencyNumber emergencyNumber, int i) {
            this.mIsEmergencyCallActive = true;
            synchronized (DeviceIdleController.this) {
                DeviceIdleController.this.mActiveReason = 8;
                DeviceIdleController.this.becomeActiveLocked("emergency call", Process.myUid());
            }
        }

        @Override // android.telephony.TelephonyCallback.CallStateListener
        public void onCallStateChanged(int i) {
            if (i == 0 && this.mIsEmergencyCallActive) {
                this.mIsEmergencyCallActive = false;
                synchronized (DeviceIdleController.this) {
                    DeviceIdleController.this.becomeInactiveIfAppropriateLocked();
                }
            }
        }

        boolean isEmergencyCallActive() {
            return this.mIsEmergencyCallActive;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        private ConnectivityManager mConnectivityManager;
        private Constants mConstants;
        private final Context mContext;
        private LocationManager mLocationManager;

        Injector(Context context) {
            this.mContext = context.createAttributionContext(DeviceIdleController.TAG);
        }

        AlarmManager getAlarmManager() {
            return (AlarmManager) this.mContext.getSystemService(AlarmManager.class);
        }

        AnyMotionDetector getAnyMotionDetector(Handler handler, SensorManager sensorManager, AnyMotionDetector.DeviceIdleCallback deviceIdleCallback, float f) {
            return new AnyMotionDetector(getPowerManager(), handler, sensorManager, deviceIdleCallback, f);
        }

        AppStateTrackerImpl getAppStateTracker(Context context, Looper looper) {
            return new AppStateTrackerImpl(context, looper);
        }

        ConnectivityManager getConnectivityManager() {
            if (this.mConnectivityManager == null) {
                this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class);
            }
            return this.mConnectivityManager;
        }

        Constants getConstants(DeviceIdleController deviceIdleController) {
            if (this.mConstants == null) {
                Objects.requireNonNull(deviceIdleController);
                this.mConstants = new Constants();
            }
            return this.mConstants;
        }

        long getElapsedRealtime() {
            return SystemClock.elapsedRealtime();
        }

        LocationManager getLocationManager() {
            if (this.mLocationManager == null) {
                this.mLocationManager = (LocationManager) this.mContext.getSystemService(LocationManager.class);
            }
            return this.mLocationManager;
        }

        MyHandler getHandler(DeviceIdleController deviceIdleController) {
            Objects.requireNonNull(deviceIdleController);
            return new MyHandler(AppSchedulingModuleThread.getHandler().getLooper());
        }

        Sensor getMotionSensor() {
            SensorManager sensorManager = getSensorManager();
            int integer = this.mContext.getResources().getInteger(R.integer.config_bluetooth_operating_voltage_mv);
            Sensor defaultSensor = integer > 0 ? sensorManager.getDefaultSensor(integer, true) : null;
            if (defaultSensor == null && this.mContext.getResources().getBoolean(R.bool.config_bluetooth_default_profiles)) {
                defaultSensor = sensorManager.getDefaultSensor(26, true);
            }
            return defaultSensor == null ? sensorManager.getDefaultSensor(17, true) : defaultSensor;
        }

        PowerManager getPowerManager() {
            return (PowerManager) this.mContext.getSystemService(PowerManager.class);
        }

        SensorManager getSensorManager() {
            return (SensorManager) this.mContext.getSystemService(SensorManager.class);
        }

        TelephonyManager getTelephonyManager() {
            return (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        }

        ConstraintController getConstraintController(Handler handler, DeviceIdleInternal deviceIdleInternal) {
            if (this.mContext.getPackageManager().hasSystemFeature("android.software.leanback_only")) {
                return new TvConstraintController(this.mContext, handler);
            }
            return null;
        }

        boolean isLocationPrefetchEnabled() {
            return this.mContext.getResources().getBoolean(R.bool.config_bluetooth_hfp_inband_ringing_support);
        }

        boolean useMotionSensor() {
            return this.mContext.getResources().getBoolean(R.bool.config_bluetooth_le_peripheral_mode_supported);
        }
    }

    @VisibleForTesting
    DeviceIdleController(Context context, Injector injector) {
        super(context);
        this.mNumBlockingConstraints = 0;
        this.mConstraints = new ArrayMap<>();
        this.mPowerSaveWhitelistAppsExceptIdle = new ArrayMap<>();
        this.mPowerSaveWhitelistUserAppsExceptIdle = new ArraySet<>();
        this.mPowerSaveWhitelistApps = new ArrayMap<>();
        this.mPowerSaveWhitelistUserApps = new ArrayMap<>();
        this.mPowerSaveWhitelistSystemAppIdsExceptIdle = new SparseBooleanArray();
        this.mPowerSaveWhitelistSystemAppIds = new SparseBooleanArray();
        this.mPowerSaveWhitelistExceptIdleAppIds = new SparseBooleanArray();
        this.mPowerSaveWhitelistExceptIdleAppIdArray = new int[0];
        this.mPowerSaveWhitelistAllAppIds = new SparseBooleanArray();
        this.mPowerSaveWhitelistAllAppIdArray = new int[0];
        this.mPowerSaveWhitelistUserAppIds = new SparseBooleanArray();
        this.mPowerSaveWhitelistUserAppIdArray = new int[0];
        this.mTempWhitelistAppIdEndTimes = new SparseArray<>();
        this.mTempWhitelistAppIdArray = new int[0];
        this.mRemovedFromSystemWhitelistApps = new ArrayMap<>();
        this.mStationaryListeners = new ArraySet<>();
        this.mTempAllowlistChangeListeners = new ArraySet<>();
        this.mEventCmds = new int[100];
        this.mEventTimes = new long[100];
        this.mEventReasons = new String[100];
        this.mReceiver = new BroadcastReceiver() { // from class: com.android.server.DeviceIdleController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                Uri data;
                String schemeSpecificPart;
                String action = intent.getAction();
                action.hashCode();
                boolean z = true;
                char c = 65535;
                switch (action.hashCode()) {
                    case -1538406691:
                        if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1172645946:
                        if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 525384130:
                        if (action.equals("android.intent.action.PACKAGE_REMOVED")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        boolean booleanExtra = intent.getBooleanExtra("present", true);
                        boolean z2 = intent.getIntExtra("plugged", 0) != 0;
                        synchronized (DeviceIdleController.this) {
                            DeviceIdleController deviceIdleController = DeviceIdleController.this;
                            if (!booleanExtra || !z2) {
                                z = false;
                            }
                            deviceIdleController.updateChargingLocked(z);
                        }
                        return;
                    case 1:
                        DeviceIdleController.this.updateConnectivityState(intent);
                        return;
                    case 2:
                        DeviceIdleController.this.mDeviceIdleExt.removePackage(intent);
                        if (intent.getBooleanExtra("android.intent.extra.REPLACING", false) || (data = intent.getData()) == null || (schemeSpecificPart = data.getSchemeSpecificPart()) == null) {
                            return;
                        }
                        DeviceIdleController.this.removePowerSaveWhitelistAppInternal(schemeSpecificPart);
                        return;
                    default:
                        return;
                }
            }
        };
        this.mLightAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda2
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                DeviceIdleController.this.lambda$new$0();
            }
        };
        this.mMotionRegistrationAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda3
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                DeviceIdleController.this.lambda$new$1();
            }
        };
        this.mMotionTimeoutAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda4
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                DeviceIdleController.this.lambda$new$2();
            }
        };
        this.mSensingTimeoutAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.android.server.DeviceIdleController.2
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                synchronized (DeviceIdleController.this) {
                    if (DeviceIdleController.this.mState == 3) {
                        DeviceIdleController.this.becomeInactiveIfAppropriateLocked();
                    }
                }
            }
        };
        this.mDeepAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.android.server.DeviceIdleController.3
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                synchronized (DeviceIdleController.this) {
                    DeviceIdleController.this.stepIdleStateLocked("s:alarm");
                }
            }
        };
        this.mIdleStartedDoneReceiver = new IIntentReceiver.Stub() { // from class: com.android.server.DeviceIdleController.4
            public void performReceive(Intent intent, int i, String str, Bundle bundle, boolean z, boolean z2, int i2) {
                if ("android.os.action.DEVICE_IDLE_MODE_CHANGED".equals(intent.getAction())) {
                    DeviceIdleController deviceIdleController = DeviceIdleController.this;
                    deviceIdleController.mHandler.sendEmptyMessageDelayed(8, deviceIdleController.mConstants.MIN_DEEP_MAINTENANCE_TIME);
                } else {
                    DeviceIdleController deviceIdleController2 = DeviceIdleController.this;
                    deviceIdleController2.mHandler.sendEmptyMessageDelayed(8, deviceIdleController2.mConstants.MIN_LIGHT_MAINTENANCE_TIME);
                }
            }
        };
        this.mInteractivityReceiver = new BroadcastReceiver() { // from class: com.android.server.DeviceIdleController.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                synchronized (DeviceIdleController.this) {
                    DeviceIdleController.this.updateInteractivityLocked();
                }
            }
        };
        this.mEmergencyCallListener = new EmergencyCallListener();
        this.mMotionListener = new MotionListener();
        this.mGenericLocationListener = new LocationListener() { // from class: com.android.server.DeviceIdleController.6
            @Override // android.location.LocationListener
            public void onProviderDisabled(String str) {
            }

            @Override // android.location.LocationListener
            public void onProviderEnabled(String str) {
            }

            @Override // android.location.LocationListener
            public void onStatusChanged(String str, int i, Bundle bundle) {
            }

            @Override // android.location.LocationListener
            public void onLocationChanged(Location location) {
                synchronized (DeviceIdleController.this) {
                    DeviceIdleController.this.receivedGenericLocationLocked(location);
                }
            }
        };
        this.mGpsLocationListener = new LocationListener() { // from class: com.android.server.DeviceIdleController.7
            @Override // android.location.LocationListener
            public void onProviderDisabled(String str) {
            }

            @Override // android.location.LocationListener
            public void onProviderEnabled(String str) {
            }

            @Override // android.location.LocationListener
            public void onStatusChanged(String str, int i, Bundle bundle) {
            }

            @Override // android.location.LocationListener
            public void onLocationChanged(Location location) {
                synchronized (DeviceIdleController.this) {
                    DeviceIdleController.this.receivedGpsLocationLocked(location);
                }
            }
        };
        this.mScreenObserver = new ActivityTaskManagerInternal.ScreenObserver() { // from class: com.android.server.DeviceIdleController.8
            public void onAwakeStateChanged(boolean z) {
            }

            public void onKeyguardStateChanged(boolean z) {
                synchronized (DeviceIdleController.this) {
                    DeviceIdleController.this.keyguardShowingLocked(z);
                }
            }
        };
        this.mDeviceIdleControllerWrapper = new DeviceIdleControllerWrapper();
        this.mDeviceIdleExt = (IDeviceIdleControllerExt) ExtLoader.type(IDeviceIdleControllerExt.class).base(this).create();
        this.mInjector = injector;
        this.mConfigFile = new AtomicFile(new File(getSystemDir(), "deviceidle.xml"));
        this.mHandler = injector.getHandler(this);
        AppStateTrackerImpl appStateTracker = injector.getAppStateTracker(context, AppSchedulingModuleThread.get().getLooper());
        this.mAppStateTracker = appStateTracker;
        LocalServices.addService(AppStateTracker.class, appStateTracker);
        this.mIsLocationPrefetchEnabled = injector.isLocationPrefetchEnabled();
        this.mUseMotionSensor = injector.useMotionSensor();
        this.mDeviceIdleExt.init(context);
    }

    public DeviceIdleController(Context context) {
        this(context, new Injector(context));
    }

    boolean isAppOnWhitelistInternal(int i) {
        boolean z;
        synchronized (this) {
            z = Arrays.binarySearch(this.mPowerSaveWhitelistAllAppIdArray, i) >= 0;
        }
        return z;
    }

    int[] getPowerSaveWhitelistUserAppIds() {
        int[] iArr;
        synchronized (this) {
            iArr = this.mPowerSaveWhitelistUserAppIdArray;
        }
        return iArr;
    }

    private static File getSystemDir() {
        return new File(Environment.getDataDirectory(), "system");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [com.android.server.DeviceIdleController$BinderService, android.os.IBinder] */
    @Override // com.android.server.SystemService
    public void onStart() {
        PackageManager packageManager = getContext().getPackageManager();
        synchronized (this) {
            boolean z = getContext().getResources().getBoolean(17891655);
            this.mDeepEnabled = z;
            this.mLightEnabled = z;
            SystemConfig systemConfig = SystemConfig.getInstance();
            ArraySet<String> allowInPowerSaveExceptIdle = systemConfig.getAllowInPowerSaveExceptIdle();
            for (int i = 0; i < allowInPowerSaveExceptIdle.size(); i++) {
                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(allowInPowerSaveExceptIdle.valueAt(i), AudioDevice.OUT_FM);
                    int appId = UserHandle.getAppId(applicationInfo.uid);
                    this.mPowerSaveWhitelistAppsExceptIdle.put(applicationInfo.packageName, Integer.valueOf(appId));
                    this.mPowerSaveWhitelistSystemAppIdsExceptIdle.put(appId, true);
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
            ArraySet<String> allowInPowerSave = systemConfig.getAllowInPowerSave();
            for (int i2 = 0; i2 < allowInPowerSave.size(); i2++) {
                try {
                    ApplicationInfo applicationInfo2 = packageManager.getApplicationInfo(allowInPowerSave.valueAt(i2), AudioDevice.OUT_FM);
                    int appId2 = UserHandle.getAppId(applicationInfo2.uid);
                    this.mPowerSaveWhitelistAppsExceptIdle.put(applicationInfo2.packageName, Integer.valueOf(appId2));
                    this.mPowerSaveWhitelistSystemAppIdsExceptIdle.put(appId2, true);
                    this.mPowerSaveWhitelistApps.put(applicationInfo2.packageName, Integer.valueOf(appId2));
                    this.mPowerSaveWhitelistSystemAppIds.put(appId2, true);
                } catch (PackageManager.NameNotFoundException unused2) {
                }
            }
            this.mConstants = this.mInjector.getConstants(this);
            readConfigFileLocked();
            updateWhitelistAppIdsLocked();
            this.mNetworkConnected = true;
            this.mScreenOn = true;
            this.mScreenLocked = false;
            this.mCharging = true;
            this.mActiveReason = 0;
            moveToStateLocked(0, "boot");
            moveToLightStateLocked(0, "boot");
            this.mInactiveTimeout = this.mConstants.INACTIVE_TIMEOUT;
            this.mPreIdleFactor = 1.0f;
            this.mLastPreIdleFactor = 1.0f;
        }
        ?? binderService = new BinderService();
        this.mBinderService = binderService;
        publishBinderService("deviceidle", binderService);
        LocalService localService = new LocalService();
        this.mLocalService = localService;
        publishLocalService(DeviceIdleInternal.class, localService);
        publishLocalService(PowerAllowlistInternal.class, new LocalPowerAllowlistService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i == 500) {
            synchronized (this) {
                Slog.d(TAG, "onBootPhase PHASE_SYSTEM_SERVICES_READY");
                this.mAlarmManager = this.mInjector.getAlarmManager();
                this.mLocalAlarmManager = (AlarmManagerInternal) getLocalService(AlarmManagerInternal.class);
                this.mBatteryStats = BatteryStatsService.getService();
                this.mLocalActivityManager = (ActivityManagerInternal) getLocalService(ActivityManagerInternal.class);
                this.mLocalActivityTaskManager = (ActivityTaskManagerInternal) getLocalService(ActivityTaskManagerInternal.class);
                this.mPackageManagerInternal = (PackageManagerInternal) getLocalService(PackageManagerInternal.class);
                this.mLocalPowerManager = (PowerManagerInternal) getLocalService(PowerManagerInternal.class);
                PowerManager powerManager = this.mInjector.getPowerManager();
                this.mPowerManager = powerManager;
                PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(1, "deviceidle_maint");
                this.mActiveIdleWakeLock = newWakeLock;
                newWakeLock.setReferenceCounted(false);
                PowerManager.WakeLock newWakeLock2 = this.mPowerManager.newWakeLock(1, "deviceidle_going_idle");
                this.mGoingIdleWakeLock = newWakeLock2;
                newWakeLock2.setReferenceCounted(true);
                this.mNetworkPolicyManager = INetworkPolicyManager.Stub.asInterface(ServiceManager.getService("netpolicy"));
                this.mNetworkPolicyManagerInternal = (NetworkPolicyManagerInternal) getLocalService(NetworkPolicyManagerInternal.class);
                this.mSensorManager = this.mInjector.getSensorManager();
                if (this.mUseMotionSensor) {
                    this.mMotionSensor = this.mInjector.getMotionSensor();
                }
                if (this.mIsLocationPrefetchEnabled) {
                    this.mLocationRequest = new LocationRequest.Builder(0L).setQuality(100).setMaxUpdates(1).build();
                }
                ConstraintController constraintController = this.mInjector.getConstraintController(this.mHandler, (DeviceIdleInternal) getLocalService(LocalService.class));
                this.mConstraintController = constraintController;
                if (constraintController != null) {
                    constraintController.start();
                }
                this.mAnyMotionDetector = this.mInjector.getAnyMotionDetector(this.mHandler, this.mSensorManager, this, getContext().getResources().getInteger(R.integer.config_bluetooth_rx_cur_ma) / 100.0f);
                this.mAppStateTracker.onSystemServicesReady();
                this.mDeviceIdleExt.initCustomizeDozeModeState();
                Bundle bundle = BroadcastOptions.makeBasic().setDeliveryGroupPolicy(1).setDeferralPolicy(2).toBundle();
                Intent intent = new Intent("android.os.action.DEVICE_IDLE_MODE_CHANGED");
                this.mIdleIntent = intent;
                intent.addFlags(1342177280);
                Intent intent2 = new Intent("android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED");
                this.mLightIdleIntent = intent2;
                intent2.addFlags(1342177280);
                this.mLightIdleIntentOptions = bundle;
                this.mIdleIntentOptions = bundle;
                Intent intent3 = new Intent("android.os.action.POWER_SAVE_WHITELIST_CHANGED");
                this.mPowerSaveWhitelistChangedIntent = intent3;
                intent3.addFlags(1073741824);
                Intent intent4 = new Intent("android.os.action.POWER_SAVE_TEMP_WHITELIST_CHANGED");
                this.mPowerSaveTempWhitelistChangedIntent = intent4;
                intent4.addFlags(1073741824);
                this.mPowerSaveWhitelistChangedOptions = bundle;
                this.mPowerSaveTempWhilelistChangedOptions = bundle;
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
                getContext().registerReceiver(this.mReceiver, intentFilter);
                IntentFilter intentFilter2 = new IntentFilter();
                intentFilter2.addAction("android.intent.action.PACKAGE_REMOVED");
                intentFilter2.addDataScheme("package");
                getContext().registerReceiver(this.mReceiver, intentFilter2);
                IntentFilter intentFilter3 = new IntentFilter();
                intentFilter3.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                getContext().registerReceiver(this.mReceiver, intentFilter3);
                IntentFilter intentFilter4 = new IntentFilter();
                intentFilter4.addAction("android.intent.action.SCREEN_OFF");
                intentFilter4.addAction("android.intent.action.SCREEN_ON");
                getContext().registerReceiver(this.mInteractivityReceiver, intentFilter4);
                this.mLocalActivityManager.setDeviceIdleAllowlist(this.mPowerSaveWhitelistAllAppIdArray, this.mPowerSaveWhitelistExceptIdleAppIdArray);
                this.mLocalPowerManager.setDeviceIdleWhitelist(this.mPowerSaveWhitelistAllAppIdArray);
                this.mLocalPowerManager.registerLowPowerModeObserver(15, new Consumer() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda15
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DeviceIdleController.this.lambda$onBootPhase$3((PowerSaveState) obj);
                    }
                });
                updateQuickDozeFlagLocked(this.mLocalPowerManager.getLowPowerState(15).batterySaverEnabled);
                this.mLocalActivityTaskManager.registerScreenObserver(this.mScreenObserver);
                this.mInjector.getTelephonyManager().registerTelephonyCallback(AppSchedulingModuleThread.getExecutor(), this.mEmergencyCallListener);
                passWhiteListsToForceAppStandbyTrackerLocked();
                updateInteractivityLocked();
            }
            updateConnectivityState(null);
        }
        this.mDeviceIdleExt.hookonBootPhase(i, getContext(), this, this.mConstants);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$3(PowerSaveState powerSaveState) {
        synchronized (this) {
            updateQuickDozeFlagLocked(powerSaveState.batterySaverEnabled);
        }
    }

    @VisibleForTesting
    boolean hasMotionSensor() {
        return this.mUseMotionSensor && this.mMotionSensor != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerDeviceIdleConstraintInternal(IDeviceIdleConstraint iDeviceIdleConstraint, String str, int i) {
        int i2;
        if (i == 0) {
            i2 = 0;
        } else {
            if (i != 1) {
                Slog.wtf(TAG, "Registering device-idle constraint with invalid type: " + i);
                return;
            }
            i2 = 3;
        }
        synchronized (this) {
            if (this.mConstraints.containsKey(iDeviceIdleConstraint)) {
                Slog.e(TAG, "Re-registering device-idle constraint: " + iDeviceIdleConstraint + ".");
                return;
            }
            this.mConstraints.put(iDeviceIdleConstraint, new DeviceIdleConstraintTracker(str, i2));
            updateActiveConstraintsLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterDeviceIdleConstraintInternal(IDeviceIdleConstraint iDeviceIdleConstraint) {
        synchronized (this) {
            onConstraintStateChangedLocked(iDeviceIdleConstraint, false);
            setConstraintMonitoringLocked(iDeviceIdleConstraint, false);
            this.mConstraints.remove(iDeviceIdleConstraint);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"this"})
    public void onConstraintStateChangedLocked(IDeviceIdleConstraint iDeviceIdleConstraint, boolean z) {
        DeviceIdleConstraintTracker deviceIdleConstraintTracker = this.mConstraints.get(iDeviceIdleConstraint);
        if (deviceIdleConstraintTracker == null) {
            Slog.e(TAG, "device-idle constraint " + iDeviceIdleConstraint + " has not been registered.");
            return;
        }
        if (z == deviceIdleConstraintTracker.active || !deviceIdleConstraintTracker.monitoring) {
            return;
        }
        deviceIdleConstraintTracker.active = z;
        int i = this.mNumBlockingConstraints + (z ? 1 : -1);
        this.mNumBlockingConstraints = i;
        if (i == 0) {
            if (this.mState == 0) {
                becomeInactiveIfAppropriateLocked();
                return;
            }
            long j = this.mNextAlarmTime;
            if (j == 0 || j < SystemClock.elapsedRealtime()) {
                stepIdleStateLocked("s:" + deviceIdleConstraintTracker.name);
            }
        }
    }

    @GuardedBy({"this"})
    private void setConstraintMonitoringLocked(IDeviceIdleConstraint iDeviceIdleConstraint, boolean z) {
        DeviceIdleConstraintTracker deviceIdleConstraintTracker = this.mConstraints.get(iDeviceIdleConstraint);
        if (deviceIdleConstraintTracker.monitoring != z) {
            deviceIdleConstraintTracker.monitoring = z;
            updateActiveConstraintsLocked();
            this.mHandler.obtainMessage(10, z ? 1 : 0, -1, iDeviceIdleConstraint).sendToTarget();
        }
    }

    @GuardedBy({"this"})
    private void updateActiveConstraintsLocked() {
        this.mNumBlockingConstraints = 0;
        for (int i = 0; i < this.mConstraints.size(); i++) {
            IDeviceIdleConstraint keyAt = this.mConstraints.keyAt(i);
            DeviceIdleConstraintTracker valueAt = this.mConstraints.valueAt(i);
            boolean z = valueAt.minState == this.mState;
            if (z != valueAt.monitoring) {
                setConstraintMonitoringLocked(keyAt, z);
                valueAt.active = z;
            }
            if (valueAt.monitoring && valueAt.active) {
                this.mNumBlockingConstraints++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int addPowerSaveWhitelistAppsInternal(List<String> list) {
        int i;
        synchronized (this) {
            int i2 = 0;
            i = 0;
            for (int size = list.size() - 1; size >= 0; size--) {
                String str = list.get(size);
                if (str != null) {
                    try {
                        if (this.mPowerSaveWhitelistUserApps.put(str, Integer.valueOf(UserHandle.getAppId(getContext().getPackageManager().getApplicationInfo(str, AudioDevice.OUT_SPEAKER_SAFE).uid))) == null) {
                            i2++;
                            Counter.logIncrement(USER_ALLOWLIST_ADDITION_METRIC_ID);
                        }
                    } catch (PackageManager.NameNotFoundException unused) {
                        Slog.e(TAG, "Tried to add unknown package to power save whitelist: " + str);
                    }
                }
                i++;
            }
            if (i2 > 0) {
                reportPowerSaveWhitelistChangedLocked();
                updateWhitelistAppIdsLocked();
                writeConfigFileLocked();
            }
        }
        return list.size() - i;
    }

    public boolean removePowerSaveWhitelistAppInternal(String str) {
        synchronized (this) {
            if (this.mPowerSaveWhitelistUserApps.remove(str) == null) {
                return false;
            }
            reportPowerSaveWhitelistChangedLocked();
            updateWhitelistAppIdsLocked();
            writeConfigFileLocked();
            Counter.logIncrement(USER_ALLOWLIST_REMOVAL_METRIC_ID);
            return true;
        }
    }

    public boolean getPowerSaveWhitelistAppInternal(String str) {
        boolean containsKey;
        synchronized (this) {
            containsKey = this.mPowerSaveWhitelistUserApps.containsKey(str);
        }
        return containsKey;
    }

    void resetSystemPowerWhitelistInternal() {
        synchronized (this) {
            this.mPowerSaveWhitelistApps.putAll((ArrayMap<? extends String, ? extends Integer>) this.mRemovedFromSystemWhitelistApps);
            this.mRemovedFromSystemWhitelistApps.clear();
            reportPowerSaveWhitelistChangedLocked();
            updateWhitelistAppIdsLocked();
            writeConfigFileLocked();
        }
    }

    public boolean restoreSystemPowerWhitelistAppInternal(String str) {
        synchronized (this) {
            if (!this.mRemovedFromSystemWhitelistApps.containsKey(str)) {
                return false;
            }
            this.mPowerSaveWhitelistApps.put(str, this.mRemovedFromSystemWhitelistApps.remove(str));
            reportPowerSaveWhitelistChangedLocked();
            updateWhitelistAppIdsLocked();
            writeConfigFileLocked();
            return true;
        }
    }

    public boolean removeSystemPowerWhitelistAppInternal(String str) {
        synchronized (this) {
            if (!this.mPowerSaveWhitelistApps.containsKey(str)) {
                return false;
            }
            this.mRemovedFromSystemWhitelistApps.put(str, this.mPowerSaveWhitelistApps.remove(str));
            reportPowerSaveWhitelistChangedLocked();
            updateWhitelistAppIdsLocked();
            writeConfigFileLocked();
            return true;
        }
    }

    public boolean addPowerSaveWhitelistExceptIdleInternal(String str) {
        synchronized (this) {
            try {
                try {
                    if (this.mPowerSaveWhitelistAppsExceptIdle.put(str, Integer.valueOf(UserHandle.getAppId(getContext().getPackageManager().getApplicationInfo(str, AudioDevice.OUT_SPEAKER_SAFE).uid))) == null) {
                        this.mPowerSaveWhitelistUserAppsExceptIdle.add(str);
                        reportPowerSaveWhitelistChangedLocked();
                        this.mPowerSaveWhitelistExceptIdleAppIdArray = buildAppIdArray(this.mPowerSaveWhitelistAppsExceptIdle, this.mPowerSaveWhitelistUserApps, this.mPowerSaveWhitelistExceptIdleAppIds);
                        passWhiteListsToForceAppStandbyTrackerLocked();
                    }
                } catch (PackageManager.NameNotFoundException unused) {
                    return false;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return true;
    }

    public void resetPowerSaveWhitelistExceptIdleInternal() {
        synchronized (this) {
            if (this.mPowerSaveWhitelistAppsExceptIdle.removeAll(this.mPowerSaveWhitelistUserAppsExceptIdle)) {
                reportPowerSaveWhitelistChangedLocked();
                this.mPowerSaveWhitelistExceptIdleAppIdArray = buildAppIdArray(this.mPowerSaveWhitelistAppsExceptIdle, this.mPowerSaveWhitelistUserApps, this.mPowerSaveWhitelistExceptIdleAppIds);
                this.mPowerSaveWhitelistUserAppsExceptIdle.clear();
                passWhiteListsToForceAppStandbyTrackerLocked();
            }
        }
    }

    public boolean getPowerSaveWhitelistExceptIdleInternal(String str) {
        boolean containsKey;
        synchronized (this) {
            containsKey = this.mPowerSaveWhitelistAppsExceptIdle.containsKey(str);
        }
        return containsKey;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] getSystemPowerWhitelistExceptIdleInternal(final int i, final int i2) {
        String[] strArr;
        synchronized (this) {
            int size = this.mPowerSaveWhitelistAppsExceptIdle.size();
            strArr = new String[size];
            for (int i3 = 0; i3 < size; i3++) {
                strArr[i3] = this.mPowerSaveWhitelistAppsExceptIdle.keyAt(i3);
            }
        }
        return (String[]) ArrayUtils.filter(strArr, new IntFunction() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda7
            @Override // java.util.function.IntFunction
            public final Object apply(int i4) {
                String[] lambda$getSystemPowerWhitelistExceptIdleInternal$4;
                lambda$getSystemPowerWhitelistExceptIdleInternal$4 = DeviceIdleController.lambda$getSystemPowerWhitelistExceptIdleInternal$4(i4);
                return lambda$getSystemPowerWhitelistExceptIdleInternal$4;
            }
        }, new Predicate() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getSystemPowerWhitelistExceptIdleInternal$5;
                lambda$getSystemPowerWhitelistExceptIdleInternal$5 = DeviceIdleController.this.lambda$getSystemPowerWhitelistExceptIdleInternal$5(i, i2, (String) obj);
                return lambda$getSystemPowerWhitelistExceptIdleInternal$5;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String[] lambda$getSystemPowerWhitelistExceptIdleInternal$4(int i) {
        return new String[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getSystemPowerWhitelistExceptIdleInternal$5(int i, int i2, String str) {
        return !this.mPackageManagerInternal.filterAppAccess(str, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] getSystemPowerWhitelistInternal(final int i, final int i2) {
        String[] strArr;
        synchronized (this) {
            int size = this.mPowerSaveWhitelistApps.size();
            strArr = new String[size];
            for (int i3 = 0; i3 < size; i3++) {
                strArr[i3] = this.mPowerSaveWhitelistApps.keyAt(i3);
            }
        }
        return (String[]) ArrayUtils.filter(strArr, new IntFunction() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final Object apply(int i4) {
                String[] lambda$getSystemPowerWhitelistInternal$6;
                lambda$getSystemPowerWhitelistInternal$6 = DeviceIdleController.lambda$getSystemPowerWhitelistInternal$6(i4);
                return lambda$getSystemPowerWhitelistInternal$6;
            }
        }, new Predicate() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getSystemPowerWhitelistInternal$7;
                lambda$getSystemPowerWhitelistInternal$7 = DeviceIdleController.this.lambda$getSystemPowerWhitelistInternal$7(i, i2, (String) obj);
                return lambda$getSystemPowerWhitelistInternal$7;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String[] lambda$getSystemPowerWhitelistInternal$6(int i) {
        return new String[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getSystemPowerWhitelistInternal$7(int i, int i2, String str) {
        return !this.mPackageManagerInternal.filterAppAccess(str, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] getRemovedSystemPowerWhitelistAppsInternal(final int i, final int i2) {
        String[] strArr;
        synchronized (this) {
            int size = this.mRemovedFromSystemWhitelistApps.size();
            strArr = new String[size];
            for (int i3 = 0; i3 < size; i3++) {
                strArr[i3] = this.mRemovedFromSystemWhitelistApps.keyAt(i3);
            }
        }
        return (String[]) ArrayUtils.filter(strArr, new IntFunction() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda9
            @Override // java.util.function.IntFunction
            public final Object apply(int i4) {
                String[] lambda$getRemovedSystemPowerWhitelistAppsInternal$8;
                lambda$getRemovedSystemPowerWhitelistAppsInternal$8 = DeviceIdleController.lambda$getRemovedSystemPowerWhitelistAppsInternal$8(i4);
                return lambda$getRemovedSystemPowerWhitelistAppsInternal$8;
            }
        }, new Predicate() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getRemovedSystemPowerWhitelistAppsInternal$9;
                lambda$getRemovedSystemPowerWhitelistAppsInternal$9 = DeviceIdleController.this.lambda$getRemovedSystemPowerWhitelistAppsInternal$9(i, i2, (String) obj);
                return lambda$getRemovedSystemPowerWhitelistAppsInternal$9;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String[] lambda$getRemovedSystemPowerWhitelistAppsInternal$8(int i) {
        return new String[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getRemovedSystemPowerWhitelistAppsInternal$9(int i, int i2, String str) {
        return !this.mPackageManagerInternal.filterAppAccess(str, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] getUserPowerWhitelistInternal(final int i, final int i2) {
        String[] strArr;
        synchronized (this) {
            strArr = new String[this.mPowerSaveWhitelistUserApps.size()];
            for (int i3 = 0; i3 < this.mPowerSaveWhitelistUserApps.size(); i3++) {
                strArr[i3] = this.mPowerSaveWhitelistUserApps.keyAt(i3);
            }
        }
        return (String[]) ArrayUtils.filter(strArr, new IntFunction() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda11
            @Override // java.util.function.IntFunction
            public final Object apply(int i4) {
                String[] lambda$getUserPowerWhitelistInternal$10;
                lambda$getUserPowerWhitelistInternal$10 = DeviceIdleController.lambda$getUserPowerWhitelistInternal$10(i4);
                return lambda$getUserPowerWhitelistInternal$10;
            }
        }, new Predicate() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getUserPowerWhitelistInternal$11;
                lambda$getUserPowerWhitelistInternal$11 = DeviceIdleController.this.lambda$getUserPowerWhitelistInternal$11(i, i2, (String) obj);
                return lambda$getUserPowerWhitelistInternal$11;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String[] lambda$getUserPowerWhitelistInternal$10(int i) {
        return new String[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getUserPowerWhitelistInternal$11(int i, int i2, String str) {
        return !this.mPackageManagerInternal.filterAppAccess(str, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] getFullPowerWhitelistExceptIdleInternal(final int i, final int i2) {
        String[] strArr;
        synchronized (this) {
            strArr = new String[this.mPowerSaveWhitelistAppsExceptIdle.size() + this.mPowerSaveWhitelistUserApps.size()];
            int i3 = 0;
            for (int i4 = 0; i4 < this.mPowerSaveWhitelistAppsExceptIdle.size(); i4++) {
                strArr[i3] = this.mPowerSaveWhitelistAppsExceptIdle.keyAt(i4);
                i3++;
            }
            for (int i5 = 0; i5 < this.mPowerSaveWhitelistUserApps.size(); i5++) {
                strArr[i3] = this.mPowerSaveWhitelistUserApps.keyAt(i5);
                i3++;
            }
        }
        return (String[]) ArrayUtils.filter(strArr, new IntFunction() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda13
            @Override // java.util.function.IntFunction
            public final Object apply(int i6) {
                String[] lambda$getFullPowerWhitelistExceptIdleInternal$12;
                lambda$getFullPowerWhitelistExceptIdleInternal$12 = DeviceIdleController.lambda$getFullPowerWhitelistExceptIdleInternal$12(i6);
                return lambda$getFullPowerWhitelistExceptIdleInternal$12;
            }
        }, new Predicate() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda14
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getFullPowerWhitelistExceptIdleInternal$13;
                lambda$getFullPowerWhitelistExceptIdleInternal$13 = DeviceIdleController.this.lambda$getFullPowerWhitelistExceptIdleInternal$13(i, i2, (String) obj);
                return lambda$getFullPowerWhitelistExceptIdleInternal$13;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String[] lambda$getFullPowerWhitelistExceptIdleInternal$12(int i) {
        return new String[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getFullPowerWhitelistExceptIdleInternal$13(int i, int i2, String str) {
        return !this.mPackageManagerInternal.filterAppAccess(str, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] getFullPowerWhitelistInternal(final int i, final int i2) {
        String[] strArr;
        synchronized (this) {
            strArr = new String[this.mPowerSaveWhitelistApps.size() + this.mPowerSaveWhitelistUserApps.size()];
            int i3 = 0;
            for (int i4 = 0; i4 < this.mPowerSaveWhitelistApps.size(); i4++) {
                strArr[i3] = this.mPowerSaveWhitelistApps.keyAt(i4);
                i3++;
            }
            for (int i5 = 0; i5 < this.mPowerSaveWhitelistUserApps.size(); i5++) {
                strArr[i3] = this.mPowerSaveWhitelistUserApps.keyAt(i5);
                i3++;
            }
        }
        return (String[]) ArrayUtils.filter(strArr, new IntFunction() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda5
            @Override // java.util.function.IntFunction
            public final Object apply(int i6) {
                String[] lambda$getFullPowerWhitelistInternal$14;
                lambda$getFullPowerWhitelistInternal$14 = DeviceIdleController.lambda$getFullPowerWhitelistInternal$14(i6);
                return lambda$getFullPowerWhitelistInternal$14;
            }
        }, new Predicate() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getFullPowerWhitelistInternal$15;
                lambda$getFullPowerWhitelistInternal$15 = DeviceIdleController.this.lambda$getFullPowerWhitelistInternal$15(i, i2, (String) obj);
                return lambda$getFullPowerWhitelistInternal$15;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String[] lambda$getFullPowerWhitelistInternal$14(int i) {
        return new String[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getFullPowerWhitelistInternal$15(int i, int i2, String str) {
        return !this.mPackageManagerInternal.filterAppAccess(str, i, i2);
    }

    public boolean isPowerSaveWhitelistExceptIdleAppInternal(String str) {
        boolean z;
        synchronized (this) {
            z = this.mPowerSaveWhitelistAppsExceptIdle.containsKey(str) || this.mPowerSaveWhitelistUserApps.containsKey(str);
        }
        return z;
    }

    public boolean isPowerSaveWhitelistAppInternal(String str) {
        boolean z;
        synchronized (this) {
            z = this.mPowerSaveWhitelistApps.containsKey(str) || this.mPowerSaveWhitelistUserApps.containsKey(str);
        }
        return z;
    }

    public int[] getAppIdWhitelistExceptIdleInternal() {
        int[] iArr;
        synchronized (this) {
            iArr = this.mPowerSaveWhitelistExceptIdleAppIdArray;
        }
        return iArr;
    }

    public int[] getAppIdWhitelistInternal() {
        int[] iArr;
        synchronized (this) {
            iArr = this.mPowerSaveWhitelistAllAppIdArray;
        }
        return iArr;
    }

    public int[] getAppIdUserWhitelistInternal() {
        int[] iArr;
        synchronized (this) {
            iArr = this.mPowerSaveWhitelistUserAppIdArray;
        }
        return iArr;
    }

    public int[] getAppIdTempWhitelistInternal() {
        int[] iArr;
        synchronized (this) {
            iArr = this.mTempWhitelistAppIdArray;
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTempAllowListType(int i, int i2) {
        if (i != -1) {
            return i != 102 ? i2 : this.mLocalActivityManager.getPushMessagingOverQuotaBehavior();
        }
        return -1;
    }

    void addPowerSaveTempAllowlistAppChecked(String str, long j, int i, int i2, String str2) throws RemoteException {
        getContext().enforceCallingOrSelfPermission("android.permission.CHANGE_DEVICE_IDLE_TEMP_WHITELIST", "No permission to change device idle whitelist");
        int callingUid = Binder.getCallingUid();
        int handleIncomingUser = ActivityManager.getService().handleIncomingUser(Binder.getCallingPid(), callingUid, i, false, false, "addPowerSaveTempWhitelistApp", (String) null);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int tempAllowListType = getTempAllowListType(i2, 0);
            if (tempAllowListType != -1) {
                addPowerSaveTempAllowlistAppInternal(callingUid, str, j, tempAllowListType, handleIncomingUser, true, i2, str2);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    void removePowerSaveTempAllowlistAppChecked(String str, int i) throws RemoteException {
        getContext().enforceCallingOrSelfPermission("android.permission.CHANGE_DEVICE_IDLE_TEMP_WHITELIST", "No permission to change device idle whitelist");
        int handleIncomingUser = ActivityManager.getService().handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, false, false, "removePowerSaveTempWhitelistApp", (String) null);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            removePowerSaveTempAllowlistAppInternal(str, handleIncomingUser);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    void addPowerSaveTempAllowlistAppInternal(int i, String str, long j, int i2, int i3, boolean z, int i4, String str2) {
        try {
            addPowerSaveTempWhitelistAppDirectInternal(i, getContext().getPackageManager().getPackageUidAsUser(str, i3), j, i2, z, i4, str2);
        } catch (PackageManager.NameNotFoundException unused) {
        }
    }

    void addPowerSaveTempWhitelistAppDirectInternal(int i, int i2, long j, int i3, boolean z, int i4, String str) {
        boolean z2;
        boolean z3;
        int i5;
        int i6;
        String str2;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        int appId = UserHandle.getAppId(i2);
        synchronized (this) {
            long min = Math.min(j, this.mConstants.MAX_TEMP_APP_ALLOWLIST_DURATION_MS);
            Pair<MutableLong, String> pair = this.mTempWhitelistAppIdEndTimes.get(appId);
            z2 = false;
            boolean z4 = pair == null;
            if (z4) {
                pair = new Pair<>(new MutableLong(0L), str);
                this.mTempWhitelistAppIdEndTimes.put(appId, pair);
            }
            ((MutableLong) pair.first).value = elapsedRealtime + min;
            if (z4) {
                try {
                    this.mBatteryStats.noteEvent(32785, str, i2);
                } catch (RemoteException unused) {
                }
                postTempActiveTimeoutMessage(i2, min);
                updateTempWhitelistAppIdsLocked(i2, true, min, i3, i4, str, i);
                if (z) {
                    z2 = true;
                } else {
                    this.mHandler.obtainMessage(14, appId, i4, str).sendToTarget();
                }
                reportTempWhitelistChangedLocked(i2, true);
            } else {
                ActivityManagerInternal activityManagerInternal = this.mLocalActivityManager;
                if (activityManagerInternal != null) {
                    z3 = true;
                    i6 = appId;
                    str2 = str;
                    i5 = i4;
                    activityManagerInternal.updateDeviceIdleTempAllowlist((int[]) null, i2, true, min, i3, i4, str, i);
                }
            }
            z3 = true;
            i6 = appId;
            str2 = str;
            i5 = i4;
        }
        if (z2) {
            this.mNetworkPolicyManagerInternal.onTempPowerSaveWhitelistChange(i6, z3, i5, str2);
        }
    }

    private void removePowerSaveTempAllowlistAppInternal(String str, int i) {
        try {
            removePowerSaveTempWhitelistAppDirectInternal(getContext().getPackageManager().getPackageUidAsUser(str, i));
        } catch (PackageManager.NameNotFoundException unused) {
        }
    }

    private void removePowerSaveTempWhitelistAppDirectInternal(int i) {
        int appId = UserHandle.getAppId(i);
        synchronized (this) {
            int indexOfKey = this.mTempWhitelistAppIdEndTimes.indexOfKey(appId);
            if (indexOfKey < 0) {
                return;
            }
            String str = (String) this.mTempWhitelistAppIdEndTimes.valueAt(indexOfKey).second;
            this.mTempWhitelistAppIdEndTimes.removeAt(indexOfKey);
            onAppRemovedFromTempWhitelistLocked(i, str);
        }
    }

    private void postTempActiveTimeoutMessage(int i, long j) {
        MyHandler myHandler = this.mHandler;
        myHandler.sendMessageDelayed(myHandler.obtainMessage(6, i, 0), j);
    }

    void checkTempAppWhitelistTimeout(int i) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        int appId = UserHandle.getAppId(i);
        synchronized (this) {
            Pair<MutableLong, String> pair = this.mTempWhitelistAppIdEndTimes.get(appId);
            if (pair == null) {
                return;
            }
            Object obj = pair.first;
            if (elapsedRealtime >= ((MutableLong) obj).value) {
                this.mTempWhitelistAppIdEndTimes.delete(appId);
                onAppRemovedFromTempWhitelistLocked(i, (String) pair.second);
            } else {
                postTempActiveTimeoutMessage(i, ((MutableLong) obj).value - elapsedRealtime);
            }
        }
    }

    @GuardedBy({"this"})
    private void onAppRemovedFromTempWhitelistLocked(int i, String str) {
        int appId = UserHandle.getAppId(i);
        updateTempWhitelistAppIdsLocked(i, false, 0L, 0, 0, str, -1);
        this.mHandler.obtainMessage(15, appId, 0).sendToTarget();
        reportTempWhitelistChangedLocked(i, false);
        try {
            this.mBatteryStats.noteEvent(16401, str, appId);
        } catch (RemoteException unused) {
        }
    }

    public void exitIdleInternal(String str) {
        synchronized (this) {
            this.mActiveReason = 5;
            becomeActiveLocked(str, Binder.getCallingUid());
        }
    }

    @VisibleForTesting
    boolean isNetworkConnected() {
        boolean z;
        synchronized (this) {
            z = this.mNetworkConnected;
        }
        return z;
    }

    void updateConnectivityState(Intent intent) {
        ConnectivityManager connectivityManager;
        synchronized (this) {
            connectivityManager = this.mInjector.getConnectivityManager();
        }
        if (connectivityManager == null) {
            return;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        synchronized (this) {
            boolean z = false;
            if (activeNetworkInfo != null) {
                if (intent == null) {
                    z = activeNetworkInfo.isConnected();
                } else {
                    if (activeNetworkInfo.getType() != intent.getIntExtra("networkType", -1)) {
                        return;
                    } else {
                        z = !intent.getBooleanExtra("noConnectivity", false);
                    }
                }
            }
            if (z != this.mNetworkConnected) {
                this.mNetworkConnected = z;
                if (z && this.mLightState == 5) {
                    stepLightIdleStateLocked("network");
                }
            }
        }
    }

    @VisibleForTesting
    boolean isScreenOn() {
        boolean z;
        synchronized (this) {
            z = this.mScreenOn;
        }
        return z;
    }

    @GuardedBy({"this"})
    void updateInteractivityLocked() {
        boolean isInteractive = this.mPowerManager.isInteractive();
        if (!isInteractive && this.mScreenOn) {
            this.mScreenOn = false;
            if (this.mForceIdle) {
                return;
            }
            becomeInactiveIfAppropriateLocked();
            return;
        }
        if (isInteractive) {
            this.mScreenOn = true;
            if (this.mForceIdle) {
                return;
            }
            if (this.mScreenLocked && this.mConstants.WAIT_FOR_UNLOCK) {
                return;
            }
            this.mActiveReason = 2;
            becomeActiveLocked("screen", Process.myUid());
        }
    }

    @VisibleForTesting
    boolean isCharging() {
        boolean z;
        synchronized (this) {
            z = this.mCharging;
        }
        return z;
    }

    @GuardedBy({"this"})
    void updateChargingLocked(boolean z) {
        if (!z && this.mCharging) {
            this.mCharging = false;
            if (this.mForceIdle) {
                return;
            }
            becomeInactiveIfAppropriateLocked();
            return;
        }
        if (z) {
            this.mCharging = z;
            if (this.mForceIdle) {
                return;
            }
            this.mActiveReason = 3;
            becomeActiveLocked("charging", Process.myUid());
        }
    }

    @VisibleForTesting
    boolean isQuickDozeEnabled() {
        boolean z;
        synchronized (this) {
            z = this.mQuickDozeActivated;
        }
        return z;
    }

    @GuardedBy({"this"})
    @VisibleForTesting
    void updateQuickDozeFlagLocked(boolean z) {
        int i;
        this.mQuickDozeActivated = z;
        this.mQuickDozeActivatedWhileIdling = z && ((i = this.mState) == 5 || i == 6);
        if (z) {
            becomeInactiveIfAppropriateLocked();
        }
    }

    @VisibleForTesting
    boolean isKeyguardShowing() {
        boolean z;
        synchronized (this) {
            z = this.mScreenLocked;
        }
        return z;
    }

    @GuardedBy({"this"})
    @VisibleForTesting
    void keyguardShowingLocked(boolean z) {
        if (this.mScreenLocked != z) {
            this.mScreenLocked = z;
            if (!this.mScreenOn || this.mForceIdle || z) {
                return;
            }
            this.mActiveReason = 4;
            becomeActiveLocked("unlocked", Process.myUid());
        }
    }

    @GuardedBy({"this"})
    @VisibleForTesting
    void scheduleReportActiveLocked(String str, int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(5, i, 0, str));
    }

    @GuardedBy({"this"})
    void becomeActiveLocked(String str, int i) {
        becomeActiveLocked(str, i, this.mConstants.INACTIVE_TIMEOUT, true);
    }

    @GuardedBy({"this"})
    private void becomeActiveLocked(String str, int i, long j, boolean z) {
        if (this.mState == 0 && this.mLightState == 0) {
            return;
        }
        moveToStateLocked(0, str);
        this.mInactiveTimeout = j;
        resetIdleManagementLocked();
        if (this.mLightState != 6) {
            this.mMaintenanceStartTime = 0L;
        }
        if (z) {
            moveToLightStateLocked(0, str);
            resetLightIdleManagementLocked();
            scheduleReportActiveLocked(str, i);
            addEvent(1, str);
        }
    }

    @VisibleForTesting
    void setDeepEnabledForTest(boolean z) {
        synchronized (this) {
            this.mDeepEnabled = z;
        }
    }

    @VisibleForTesting
    void setLightEnabledForTest(boolean z) {
        synchronized (this) {
            this.mLightEnabled = z;
        }
    }

    @GuardedBy({"this"})
    private void verifyAlarmStateLocked() {
        if (this.mState == 0 && this.mNextAlarmTime != 0) {
            Slog.wtf(TAG, "mState=ACTIVE but mNextAlarmTime=" + this.mNextAlarmTime);
        }
        if (this.mState != 5 && this.mLocalAlarmManager.isIdling()) {
            Slog.wtf(TAG, "mState=" + stateToString(this.mState) + " but AlarmManager is idling");
        }
        if (this.mState == 5 && !this.mLocalAlarmManager.isIdling()) {
            Slog.wtf(TAG, "mState=IDLE but AlarmManager is not idling");
        }
        if (this.mLightState != 0 || this.mNextLightAlarmTime == 0) {
            return;
        }
        Slog.wtf(TAG, "mLightState=ACTIVE but mNextLightAlarmTime is " + TimeUtils.formatDuration(this.mNextLightAlarmTime - SystemClock.elapsedRealtime()) + " from now");
    }

    @GuardedBy({"this"})
    void becomeInactiveIfAppropriateLocked() {
        verifyAlarmStateLocked();
        boolean z = this.mScreenOn && !(this.mConstants.WAIT_FOR_UNLOCK && this.mScreenLocked);
        boolean isEmergencyCallActive = this.mEmergencyCallListener.isEmergencyCallActive();
        if (this.mForceIdle || !(this.mCharging || z || isEmergencyCallActive)) {
            if (this.mDeepEnabled) {
                if (this.mQuickDozeActivated) {
                    int i = this.mState;
                    if (i == 7 || i == 5 || i == 6) {
                        return;
                    }
                    moveToStateLocked(7, "no activity");
                    resetIdleManagementLocked();
                    if (isUpcomingAlarmClock()) {
                        scheduleAlarmLocked((this.mAlarmManager.getNextWakeFromIdleTime() - this.mInjector.getElapsedRealtime()) + this.mConstants.QUICK_DOZE_DELAY_TIMEOUT);
                    } else {
                        scheduleAlarmLocked(this.mConstants.QUICK_DOZE_DELAY_TIMEOUT);
                    }
                } else if (this.mState == 0) {
                    moveToStateLocked(1, "no activity");
                    resetIdleManagementLocked();
                    long j = this.mInactiveTimeout;
                    if (shouldUseIdleTimeoutFactorLocked()) {
                        j = this.mPreIdleFactor * ((float) j);
                    }
                    if (isUpcomingAlarmClock()) {
                        scheduleAlarmLocked((this.mAlarmManager.getNextWakeFromIdleTime() - this.mInjector.getElapsedRealtime()) + j);
                    } else {
                        scheduleAlarmLocked(j);
                    }
                }
            }
            if (this.mLightState == 0 && this.mLightEnabled) {
                moveToLightStateLocked(1, "no activity");
                resetLightIdleManagementLocked();
                Constants constants = this.mConstants;
                scheduleLightAlarmLocked(constants.LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT, constants.FLEX_TIME_SHORT, true);
            }
        }
    }

    @GuardedBy({"this"})
    private void resetIdleManagementLocked() {
        this.mNextIdlePendingDelay = 0L;
        this.mNextIdleDelay = 0L;
        this.mIdleStartTime = 0L;
        this.mQuickDozeActivatedWhileIdling = false;
        cancelAlarmLocked();
        cancelSensingTimeoutAlarmLocked();
        cancelLocatingLocked();
        maybeStopMonitoringMotionLocked();
        this.mAnyMotionDetector.stop();
        updateActiveConstraintsLocked();
    }

    @GuardedBy({"this"})
    private void resetLightIdleManagementLocked() {
        Constants constants = this.mConstants;
        this.mNextLightIdleDelay = constants.LIGHT_IDLE_TIMEOUT;
        this.mMaintenanceStartTime = 0L;
        this.mNextLightIdleDelayFlex = constants.LIGHT_IDLE_TIMEOUT_INITIAL_FLEX;
        this.mCurLightIdleBudget = constants.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET;
        cancelLightAlarmLocked();
    }

    @GuardedBy({"this"})
    void exitForceIdleLocked() {
        if (this.mForceIdle) {
            this.mForceIdle = false;
            if (this.mScreenOn || this.mCharging) {
                this.mActiveReason = 6;
                becomeActiveLocked("exit-force", Process.myUid());
            }
        }
    }

    @VisibleForTesting
    void setLightStateForTest(int i) {
        synchronized (this) {
            this.mLightState = i;
        }
    }

    @VisibleForTesting
    int getLightState() {
        int i;
        synchronized (this) {
            i = this.mLightState;
        }
        return i;
    }

    @GuardedBy({"this"})
    @VisibleForTesting
    @SuppressLint({"WakelockTimeout"})
    void stepLightIdleStateLocked(String str) {
        int i = this.mLightState;
        if (i == 0 || i == 7) {
            return;
        }
        EventLogTags.writeDeviceIdleLightStep();
        if (this.mEmergencyCallListener.isEmergencyCallActive()) {
            Slog.wtf(TAG, "stepLightIdleStateLocked called when emergency call is active");
            if (this.mLightState != 0) {
                this.mActiveReason = 8;
                becomeActiveLocked("emergency", Process.myUid());
                return;
            }
            return;
        }
        int i2 = this.mLightState;
        if (i2 == 1) {
            Constants constants = this.mConstants;
            this.mCurLightIdleBudget = constants.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET;
            this.mNextLightIdleDelay = constants.LIGHT_IDLE_TIMEOUT;
            this.mNextLightIdleDelayFlex = constants.LIGHT_IDLE_TIMEOUT_INITIAL_FLEX;
            this.mMaintenanceStartTime = 0L;
        } else {
            if (i2 == 4 || i2 == 5) {
                if (this.mNetworkConnected || i2 == 5) {
                    this.mActiveIdleOpCount = 1;
                    this.mActiveIdleWakeLock.acquire();
                    this.mMaintenanceStartTime = SystemClock.elapsedRealtime();
                    long j = this.mCurLightIdleBudget;
                    Constants constants2 = this.mConstants;
                    long j2 = constants2.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET;
                    if (j < j2) {
                        this.mCurLightIdleBudget = j2;
                    } else {
                        long j3 = constants2.LIGHT_IDLE_MAINTENANCE_MAX_BUDGET;
                        if (j > j3) {
                            this.mCurLightIdleBudget = j3;
                        }
                    }
                    scheduleLightAlarmLocked(this.mCurLightIdleBudget, constants2.FLEX_TIME_SHORT, true);
                    moveToLightStateLocked(6, str);
                    addEvent(3, null);
                    this.mHandler.sendEmptyMessage(4);
                    return;
                }
                scheduleLightAlarmLocked(this.mNextLightIdleDelay, this.mNextLightIdleDelayFlex / 2, true);
                moveToLightStateLocked(5, str);
                return;
            }
            if (i2 != 6) {
                return;
            }
        }
        if (this.mMaintenanceStartTime != 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime() - this.mMaintenanceStartTime;
            long j4 = this.mConstants.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET;
            if (elapsedRealtime < j4) {
                this.mCurLightIdleBudget += j4 - elapsedRealtime;
            } else {
                this.mCurLightIdleBudget -= elapsedRealtime - j4;
            }
        }
        this.mMaintenanceStartTime = 0L;
        scheduleLightAlarmLocked(this.mNextLightIdleDelay, this.mNextLightIdleDelayFlex, true);
        this.mNextLightIdleDelay = Math.min(this.mConstants.LIGHT_MAX_IDLE_TIMEOUT, ((float) this.mNextLightIdleDelay) * r0.LIGHT_IDLE_FACTOR);
        this.mNextLightIdleDelayFlex = Math.min(this.mConstants.LIGHT_IDLE_TIMEOUT_MAX_FLEX, ((float) this.mNextLightIdleDelayFlex) * r0.LIGHT_IDLE_FACTOR);
        moveToLightStateLocked(4, str);
        addEvent(2, null);
        this.mGoingIdleWakeLock.acquire();
        this.mHandler.sendEmptyMessage(3);
    }

    @VisibleForTesting
    int getState() {
        int i;
        synchronized (this) {
            i = this.mState;
        }
        return i;
    }

    private boolean isUpcomingAlarmClock() {
        return this.mInjector.getElapsedRealtime() + this.mConstants.MIN_TIME_TO_ALARM >= this.mAlarmManager.getNextWakeFromIdleTime();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:22:0x0054. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00d2  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x017f  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0185  */
    @GuardedBy({"this"})
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void stepIdleStateLocked(String str) {
        long min;
        long j;
        IDeviceIdleControllerExt iDeviceIdleControllerExt;
        EventLogTags.writeDeviceIdleStep();
        if (this.mEmergencyCallListener.isEmergencyCallActive()) {
            Slog.wtf(TAG, "stepIdleStateLocked called when emergency call is active");
            if (this.mState != 0) {
                this.mActiveReason = 8;
                becomeActiveLocked("emergency", Process.myUid());
                return;
            }
            return;
        }
        if (isUpcomingAlarmClock()) {
            if (this.mState != 0) {
                this.mActiveReason = 7;
                becomeActiveLocked(HostingRecord.TRIGGER_TYPE_ALARM, Process.myUid());
                becomeInactiveIfAppropriateLocked();
                return;
            }
            return;
        }
        if (this.mNumBlockingConstraints == 0 || this.mForceIdle) {
            switch (this.mState) {
                case 1:
                    startMonitoringMotionLocked();
                    long j2 = this.mConstants.IDLE_AFTER_INACTIVE_TIMEOUT;
                    if (shouldUseIdleTimeoutFactorLocked()) {
                        j2 = this.mPreIdleFactor * ((float) j2);
                    }
                    scheduleAlarmLocked(j2);
                    moveToStateLocked(2, str);
                    return;
                case 2:
                    cancelLocatingLocked();
                    this.mLocated = false;
                    this.mLastGenericLocation = null;
                    this.mLastGpsLocation = null;
                    moveToStateLocked(3, str);
                    if (this.mUseMotionSensor && this.mAnyMotionDetector.hasSensor()) {
                        scheduleSensingTimeoutAlarmLocked(this.mConstants.SENSING_TIMEOUT);
                        this.mNotMoving = false;
                        this.mAnyMotionDetector.checkForAnyMotion();
                        return;
                    }
                    if (this.mNumBlockingConstraints != 0) {
                        cancelAlarmLocked();
                        return;
                    }
                    this.mNotMoving = true;
                    cancelSensingTimeoutAlarmLocked();
                    moveToStateLocked(4, str);
                    if (!this.mIsLocationPrefetchEnabled) {
                        scheduleAlarmLocked(this.mConstants.LOCATING_TIMEOUT);
                        if ("s:shell".equals(str) || ((iDeviceIdleControllerExt = this.mDeviceIdleExt) != null && iDeviceIdleControllerExt.isClosedSuperFirewall())) {
                            LocationManager locationManager = this.mInjector.getLocationManager();
                            if (locationManager != null && locationManager.getProvider("fused") != null) {
                                locationManager.requestLocationUpdates("fused", this.mLocationRequest, AppSchedulingModuleThread.getExecutor(), this.mGenericLocationListener);
                                this.mLocating = true;
                            } else {
                                this.mHasFusedLocation = false;
                            }
                            if (locationManager != null && locationManager.getProvider(IOplusSceneManager.APP_SCENE_GPS) != null) {
                                this.mHasGps = true;
                                locationManager.requestLocationUpdates(IOplusSceneManager.APP_SCENE_GPS, 1000L, 5.0f, this.mGpsLocationListener, this.mHandler.getLooper());
                                this.mLocating = true;
                            } else {
                                this.mHasGps = false;
                            }
                        }
                        if (this.mLocating) {
                            return;
                        }
                    } else {
                        this.mLocating = false;
                    }
                    cancelAlarmLocked();
                    cancelLocatingLocked();
                    this.mAnyMotionDetector.stop();
                    Constants constants = this.mConstants;
                    this.mNextIdlePendingDelay = constants.IDLE_PENDING_TIMEOUT;
                    this.mNextIdleDelay = constants.IDLE_TIMEOUT;
                    this.mDeviceIdleExt.enterSmartDozeIfNeeded(str);
                    moveToStateLocked(5, str);
                    scheduleAlarmLocked(this.mNextIdleDelay);
                    this.mNextIdleDelay = ((float) this.mNextIdleDelay) * this.mConstants.IDLE_FACTOR;
                    this.mIdleStartTime = SystemClock.elapsedRealtime();
                    min = Math.min(this.mNextIdleDelay, this.mConstants.MAX_IDLE_TIMEOUT);
                    this.mNextIdleDelay = min;
                    j = this.mConstants.IDLE_TIMEOUT;
                    if (min < j) {
                        this.mNextIdleDelay = j;
                    }
                    if (this.mLightState != 7) {
                        moveToLightStateLocked(7, "deep");
                        cancelLightAlarmLocked();
                    }
                    addEvent(4, null);
                    this.mGoingIdleWakeLock.acquire();
                    this.mHandler.sendEmptyMessage(2);
                    return;
                case 3:
                    cancelSensingTimeoutAlarmLocked();
                    moveToStateLocked(4, str);
                    if (!this.mIsLocationPrefetchEnabled) {
                    }
                    cancelAlarmLocked();
                    cancelLocatingLocked();
                    this.mAnyMotionDetector.stop();
                    Constants constants2 = this.mConstants;
                    this.mNextIdlePendingDelay = constants2.IDLE_PENDING_TIMEOUT;
                    this.mNextIdleDelay = constants2.IDLE_TIMEOUT;
                    this.mDeviceIdleExt.enterSmartDozeIfNeeded(str);
                    moveToStateLocked(5, str);
                    scheduleAlarmLocked(this.mNextIdleDelay);
                    this.mNextIdleDelay = ((float) this.mNextIdleDelay) * this.mConstants.IDLE_FACTOR;
                    this.mIdleStartTime = SystemClock.elapsedRealtime();
                    min = Math.min(this.mNextIdleDelay, this.mConstants.MAX_IDLE_TIMEOUT);
                    this.mNextIdleDelay = min;
                    j = this.mConstants.IDLE_TIMEOUT;
                    if (min < j) {
                    }
                    if (this.mLightState != 7) {
                    }
                    addEvent(4, null);
                    this.mGoingIdleWakeLock.acquire();
                    this.mHandler.sendEmptyMessage(2);
                    return;
                case 4:
                    cancelAlarmLocked();
                    cancelLocatingLocked();
                    this.mAnyMotionDetector.stop();
                    Constants constants22 = this.mConstants;
                    this.mNextIdlePendingDelay = constants22.IDLE_PENDING_TIMEOUT;
                    this.mNextIdleDelay = constants22.IDLE_TIMEOUT;
                    this.mDeviceIdleExt.enterSmartDozeIfNeeded(str);
                    moveToStateLocked(5, str);
                    scheduleAlarmLocked(this.mNextIdleDelay);
                    this.mNextIdleDelay = ((float) this.mNextIdleDelay) * this.mConstants.IDLE_FACTOR;
                    this.mIdleStartTime = SystemClock.elapsedRealtime();
                    min = Math.min(this.mNextIdleDelay, this.mConstants.MAX_IDLE_TIMEOUT);
                    this.mNextIdleDelay = min;
                    j = this.mConstants.IDLE_TIMEOUT;
                    if (min < j) {
                    }
                    if (this.mLightState != 7) {
                    }
                    addEvent(4, null);
                    this.mGoingIdleWakeLock.acquire();
                    this.mHandler.sendEmptyMessage(2);
                    return;
                case 5:
                    this.mActiveIdleOpCount = 1;
                    this.mActiveIdleWakeLock.acquire();
                    moveToStateLocked(6, str);
                    scheduleAlarmLocked(this.mNextIdlePendingDelay);
                    this.mMaintenanceStartTime = SystemClock.elapsedRealtime();
                    long min2 = Math.min(this.mConstants.MAX_IDLE_PENDING_TIMEOUT, ((float) this.mNextIdlePendingDelay) * r1.IDLE_PENDING_FACTOR);
                    this.mNextIdlePendingDelay = min2;
                    long j3 = this.mConstants.IDLE_PENDING_TIMEOUT;
                    if (min2 < j3) {
                        this.mNextIdlePendingDelay = j3;
                    }
                    addEvent(5, null);
                    this.mHandler.sendEmptyMessage(4);
                    return;
                case 6:
                    this.mDeviceIdleExt.enterSmartDozeIfNeeded(str);
                    moveToStateLocked(5, str);
                    scheduleAlarmLocked(this.mNextIdleDelay);
                    this.mNextIdleDelay = ((float) this.mNextIdleDelay) * this.mConstants.IDLE_FACTOR;
                    this.mIdleStartTime = SystemClock.elapsedRealtime();
                    min = Math.min(this.mNextIdleDelay, this.mConstants.MAX_IDLE_TIMEOUT);
                    this.mNextIdleDelay = min;
                    j = this.mConstants.IDLE_TIMEOUT;
                    if (min < j) {
                    }
                    if (this.mLightState != 7) {
                    }
                    addEvent(4, null);
                    this.mGoingIdleWakeLock.acquire();
                    this.mHandler.sendEmptyMessage(2);
                    return;
                case 7:
                    Constants constants222 = this.mConstants;
                    this.mNextIdlePendingDelay = constants222.IDLE_PENDING_TIMEOUT;
                    this.mNextIdleDelay = constants222.IDLE_TIMEOUT;
                    this.mDeviceIdleExt.enterSmartDozeIfNeeded(str);
                    moveToStateLocked(5, str);
                    scheduleAlarmLocked(this.mNextIdleDelay);
                    this.mNextIdleDelay = ((float) this.mNextIdleDelay) * this.mConstants.IDLE_FACTOR;
                    this.mIdleStartTime = SystemClock.elapsedRealtime();
                    min = Math.min(this.mNextIdleDelay, this.mConstants.MAX_IDLE_TIMEOUT);
                    this.mNextIdleDelay = min;
                    j = this.mConstants.IDLE_TIMEOUT;
                    if (min < j) {
                    }
                    if (this.mLightState != 7) {
                    }
                    addEvent(4, null);
                    this.mGoingIdleWakeLock.acquire();
                    this.mHandler.sendEmptyMessage(2);
                    return;
                default:
                    return;
            }
        }
    }

    @GuardedBy({"this"})
    private void moveToLightStateLocked(int i, String str) {
        this.mLightState = i;
        EventLogTags.writeDeviceIdleLight(i, str);
        Trace.traceCounter(524288L, "DozeLightState", i);
    }

    @GuardedBy({"this"})
    private void moveToStateLocked(int i, String str) {
        this.mState = i;
        EventLogTags.writeDeviceIdle(i, str);
        Trace.traceCounter(524288L, "DozeDeepState", i);
        updateActiveConstraintsLocked();
    }

    void incActiveIdleOps() {
        synchronized (this) {
            this.mActiveIdleOpCount++;
        }
    }

    void decActiveIdleOps() {
        synchronized (this) {
            int i = this.mActiveIdleOpCount - 1;
            this.mActiveIdleOpCount = i;
            if (i <= 0) {
                exitMaintenanceEarlyIfNeededLocked();
                this.mActiveIdleWakeLock.release();
            }
        }
    }

    @VisibleForTesting
    void setActiveIdleOpsForTest(int i) {
        synchronized (this) {
            this.mActiveIdleOpCount = i;
        }
    }

    void setJobsActive(boolean z) {
        synchronized (this) {
            this.mJobsActive = z;
            if (!z) {
                exitMaintenanceEarlyIfNeededLocked();
            }
        }
    }

    void setAlarmsActive(boolean z) {
        synchronized (this) {
            this.mAlarmsActive = z;
            if (!z) {
                exitMaintenanceEarlyIfNeededLocked();
            }
        }
    }

    @VisibleForTesting
    int setPreIdleTimeoutMode(int i) {
        return setPreIdleTimeoutFactor(getPreIdleTimeoutByMode(i));
    }

    @VisibleForTesting
    float getPreIdleTimeoutByMode(int i) {
        if (i == 0) {
            return 1.0f;
        }
        if (i == 1) {
            return this.mConstants.PRE_IDLE_FACTOR_LONG;
        }
        if (i == 2) {
            return this.mConstants.PRE_IDLE_FACTOR_SHORT;
        }
        Slog.w(TAG, "Invalid time out factor mode: " + i);
        return 1.0f;
    }

    @VisibleForTesting
    float getPreIdleTimeoutFactor() {
        float f;
        synchronized (this) {
            f = this.mPreIdleFactor;
        }
        return f;
    }

    @VisibleForTesting
    int setPreIdleTimeoutFactor(float f) {
        synchronized (this) {
            if (!this.mDeepEnabled) {
                return 2;
            }
            if (f <= MIN_PRE_IDLE_FACTOR_CHANGE) {
                return 3;
            }
            if (Math.abs(f - this.mPreIdleFactor) < MIN_PRE_IDLE_FACTOR_CHANGE) {
                return 0;
            }
            this.mLastPreIdleFactor = this.mPreIdleFactor;
            this.mPreIdleFactor = f;
            postUpdatePreIdleFactor();
            return 1;
        }
    }

    @VisibleForTesting
    void resetPreIdleTimeoutMode() {
        synchronized (this) {
            this.mLastPreIdleFactor = this.mPreIdleFactor;
            this.mPreIdleFactor = 1.0f;
        }
        postResetPreIdleTimeoutFactor();
    }

    private void postUpdatePreIdleFactor() {
        this.mHandler.sendEmptyMessage(11);
    }

    private void postResetPreIdleTimeoutFactor() {
        this.mHandler.sendEmptyMessage(12);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePreIdleFactor() {
        synchronized (this) {
            if (shouldUseIdleTimeoutFactorLocked()) {
                int i = this.mState;
                if (i == 1 || i == 2) {
                    long j = this.mNextAlarmTime;
                    if (j == 0) {
                        return;
                    }
                    long elapsedRealtime = j - SystemClock.elapsedRealtime();
                    if (elapsedRealtime < 60000) {
                        return;
                    }
                    long j2 = (((float) elapsedRealtime) / this.mLastPreIdleFactor) * this.mPreIdleFactor;
                    if (Math.abs(elapsedRealtime - j2) < 60000) {
                    } else {
                        scheduleAlarmLocked(j2);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeDoImmediateMaintenance(String str) {
        synchronized (this) {
            if (this.mState == 5 && SystemClock.elapsedRealtime() - this.mIdleStartTime > this.mConstants.IDLE_TIMEOUT) {
                stepIdleStateLocked(str);
            }
        }
    }

    @GuardedBy({"this"})
    private boolean shouldUseIdleTimeoutFactorLocked() {
        return this.mActiveReason != 1;
    }

    @VisibleForTesting
    void setIdleStartTimeForTest(long j) {
        synchronized (this) {
            this.mIdleStartTime = j;
            maybeDoImmediateMaintenance("testing");
        }
    }

    @VisibleForTesting
    long getNextAlarmTime() {
        long j;
        synchronized (this) {
            j = this.mNextAlarmTime;
        }
        return j;
    }

    @VisibleForTesting
    boolean isEmergencyCallActive() {
        return this.mEmergencyCallListener.isEmergencyCallActive();
    }

    @GuardedBy({"this"})
    boolean isOpsInactiveLocked() {
        return (this.mActiveIdleOpCount > 0 || this.mJobsActive || this.mAlarmsActive) ? false : true;
    }

    @GuardedBy({"this"})
    void exitMaintenanceEarlyIfNeededLocked() {
        if ((this.mState == 6 || this.mLightState == 6) && isOpsInactiveLocked()) {
            SystemClock.elapsedRealtime();
            if (this.mState == 6) {
                stepIdleStateLocked("s:early");
            } else {
                stepLightIdleStateLocked("s:early");
            }
        }
    }

    @GuardedBy({"this"})
    void motionLocked() {
        this.mLastMotionEventElapsed = this.mInjector.getElapsedRealtime();
        handleMotionDetectedLocked(this.mConstants.MOTION_INACTIVE_TIMEOUT, "motion");
    }

    @GuardedBy({"this"})
    void handleMotionDetectedLocked(long j, String str) {
        if (this.mStationaryListeners.size() > 0) {
            postStationaryStatusUpdated();
            cancelMotionTimeoutAlarmLocked();
            scheduleMotionRegistrationAlarmLocked();
        }
        if ((!this.mQuickDozeActivated || this.mQuickDozeActivatedWhileIdling) && !this.mDeviceIdleExt.isInSmartDozeMode(this.mState)) {
            maybeStopMonitoringMotionLocked();
            boolean z = this.mState != 0 || this.mLightState == 7;
            becomeActiveLocked(str, Process.myUid(), j, this.mLightState == 7);
            if (z) {
                becomeInactiveIfAppropriateLocked();
            }
        }
    }

    @GuardedBy({"this"})
    void receivedGenericLocationLocked(Location location) {
        if (this.mState != 4) {
            cancelLocatingLocked();
            return;
        }
        this.mLastGenericLocation = new Location(location);
        if (location.getAccuracy() <= this.mConstants.LOCATION_ACCURACY || !this.mHasGps) {
            this.mLocated = true;
            if (this.mNotMoving) {
                stepIdleStateLocked("s:location");
            }
        }
    }

    @GuardedBy({"this"})
    void receivedGpsLocationLocked(Location location) {
        if (this.mState != 4) {
            cancelLocatingLocked();
            return;
        }
        this.mLastGpsLocation = new Location(location);
        if (location.getAccuracy() > this.mConstants.LOCATION_ACCURACY) {
            return;
        }
        this.mLocated = true;
        if (this.mNotMoving) {
            stepIdleStateLocked("s:gps");
        }
    }

    void startMonitoringMotionLocked() {
        if (this.mMotionSensor != null) {
            MotionListener motionListener = this.mMotionListener;
            if (motionListener.active) {
                return;
            }
            motionListener.registerLocked();
        }
    }

    private void maybeStopMonitoringMotionLocked() {
        if (this.mMotionSensor == null || this.mStationaryListeners.size() != 0) {
            return;
        }
        MotionListener motionListener = this.mMotionListener;
        if (motionListener.active) {
            motionListener.unregisterLocked();
            cancelMotionTimeoutAlarmLocked();
        }
        cancelMotionRegistrationAlarmLocked();
    }

    @GuardedBy({"this"})
    void cancelAlarmLocked() {
        if (this.mNextAlarmTime != 0) {
            this.mNextAlarmTime = 0L;
            this.mAlarmManager.cancel(this.mDeepAlarmListener);
        }
    }

    @GuardedBy({"this"})
    private void cancelLightAlarmLocked() {
        if (this.mNextLightAlarmTime != 0) {
            this.mNextLightAlarmTime = 0L;
            this.mAlarmManager.cancel(this.mLightAlarmListener);
        }
    }

    @GuardedBy({"this"})
    void cancelLocatingLocked() {
        if (this.mLocating) {
            LocationManager locationManager = this.mInjector.getLocationManager();
            locationManager.removeUpdates(this.mGenericLocationListener);
            locationManager.removeUpdates(this.mGpsLocationListener);
            this.mLocating = false;
        }
    }

    private void cancelMotionTimeoutAlarmLocked() {
        this.mAlarmManager.cancel(this.mMotionTimeoutAlarmListener);
    }

    private void cancelMotionRegistrationAlarmLocked() {
        this.mAlarmManager.cancel(this.mMotionRegistrationAlarmListener);
    }

    @GuardedBy({"this"})
    void cancelSensingTimeoutAlarmLocked() {
        if (this.mNextSensingTimeoutAlarmTime != 0) {
            this.mNextSensingTimeoutAlarmTime = 0L;
            this.mAlarmManager.cancel(this.mSensingTimeoutAlarmListener);
        }
    }

    @GuardedBy({"this"})
    @VisibleForTesting
    void scheduleAlarmLocked(long j) {
        int i;
        if (!this.mUseMotionSensor || this.mMotionSensor != null || (i = this.mState) == 7 || i == 5 || i == 6) {
            long elapsedRealtime = SystemClock.elapsedRealtime() + j;
            this.mNextAlarmTime = elapsedRealtime;
            int i2 = this.mState;
            if (i2 == 5) {
                this.mAlarmManager.setIdleUntil(2, elapsedRealtime, "DeviceIdleController.deep", this.mDeepAlarmListener, this.mHandler);
                return;
            }
            if (i2 == 4) {
                this.mAlarmManager.setExact(2, elapsedRealtime, "DeviceIdleController.deep", this.mDeepAlarmListener, this.mHandler);
                return;
            }
            Constants constants = this.mConstants;
            if (constants.USE_WINDOW_ALARMS) {
                this.mAlarmManager.setWindow(2, elapsedRealtime, constants.FLEX_TIME_SHORT, "DeviceIdleController.deep", this.mDeepAlarmListener, this.mHandler);
            } else {
                this.mAlarmManager.set(2, elapsedRealtime, "DeviceIdleController.deep", this.mDeepAlarmListener, this.mHandler);
            }
        }
    }

    @GuardedBy({"this"})
    void scheduleLightAlarmLocked(long j, long j2, boolean z) {
        long elapsedRealtime = this.mInjector.getElapsedRealtime() + j;
        this.mNextLightAlarmTime = elapsedRealtime;
        if (this.mConstants.USE_WINDOW_ALARMS) {
            this.mAlarmManager.setWindow(z ? 2 : 3, elapsedRealtime, j2, "DeviceIdleController.light", this.mLightAlarmListener, this.mHandler);
        } else {
            this.mAlarmManager.set(z ? 2 : 3, elapsedRealtime, "DeviceIdleController.light", this.mLightAlarmListener, this.mHandler);
        }
    }

    @VisibleForTesting
    long getNextLightAlarmTimeForTesting() {
        long j;
        synchronized (this) {
            j = this.mNextLightAlarmTime;
        }
        return j;
    }

    private void scheduleMotionRegistrationAlarmLocked() {
        long elapsedRealtime = this.mInjector.getElapsedRealtime();
        Constants constants = this.mConstants;
        long j = elapsedRealtime + (constants.MOTION_INACTIVE_TIMEOUT / 2);
        if (constants.USE_WINDOW_ALARMS) {
            this.mAlarmManager.setWindow(2, j, constants.MOTION_INACTIVE_TIMEOUT_FLEX, "DeviceIdleController.motion_registration", this.mMotionRegistrationAlarmListener, this.mHandler);
        } else {
            this.mAlarmManager.set(2, j, "DeviceIdleController.motion_registration", this.mMotionRegistrationAlarmListener, this.mHandler);
        }
    }

    private void scheduleMotionTimeoutAlarmLocked() {
        long elapsedRealtime = this.mInjector.getElapsedRealtime();
        Constants constants = this.mConstants;
        long j = elapsedRealtime + constants.MOTION_INACTIVE_TIMEOUT;
        if (constants.USE_WINDOW_ALARMS) {
            this.mAlarmManager.setWindow(2, j, constants.MOTION_INACTIVE_TIMEOUT_FLEX, "DeviceIdleController.motion", this.mMotionTimeoutAlarmListener, this.mHandler);
        } else {
            this.mAlarmManager.set(2, j, "DeviceIdleController.motion", this.mMotionTimeoutAlarmListener, this.mHandler);
        }
    }

    @GuardedBy({"this"})
    void scheduleSensingTimeoutAlarmLocked(long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime() + j;
        this.mNextSensingTimeoutAlarmTime = elapsedRealtime;
        Constants constants = this.mConstants;
        if (constants.USE_WINDOW_ALARMS) {
            this.mAlarmManager.setWindow(2, elapsedRealtime, constants.FLEX_TIME_SHORT, "DeviceIdleController.sensing", this.mSensingTimeoutAlarmListener, this.mHandler);
        } else {
            this.mAlarmManager.set(2, elapsedRealtime, "DeviceIdleController.sensing", this.mSensingTimeoutAlarmListener, this.mHandler);
        }
    }

    private static int[] buildAppIdArray(ArrayMap<String, Integer> arrayMap, ArrayMap<String, Integer> arrayMap2, SparseBooleanArray sparseBooleanArray) {
        sparseBooleanArray.clear();
        if (arrayMap != null) {
            for (int i = 0; i < arrayMap.size(); i++) {
                sparseBooleanArray.put(arrayMap.valueAt(i).intValue(), true);
            }
        }
        if (arrayMap2 != null) {
            for (int i2 = 0; i2 < arrayMap2.size(); i2++) {
                sparseBooleanArray.put(arrayMap2.valueAt(i2).intValue(), true);
            }
        }
        int size = sparseBooleanArray.size();
        int[] iArr = new int[size];
        for (int i3 = 0; i3 < size; i3++) {
            iArr[i3] = sparseBooleanArray.keyAt(i3);
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWhitelistAppIdsLocked() {
        this.mPowerSaveWhitelistExceptIdleAppIdArray = buildAppIdArray(this.mPowerSaveWhitelistAppsExceptIdle, this.mPowerSaveWhitelistUserApps, this.mPowerSaveWhitelistExceptIdleAppIds);
        this.mPowerSaveWhitelistAllAppIdArray = buildAppIdArray(this.mPowerSaveWhitelistApps, this.mPowerSaveWhitelistUserApps, this.mPowerSaveWhitelistAllAppIds);
        this.mPowerSaveWhitelistUserAppIdArray = buildAppIdArray(null, this.mPowerSaveWhitelistUserApps, this.mPowerSaveWhitelistUserAppIds);
        ActivityManagerInternal activityManagerInternal = this.mLocalActivityManager;
        if (activityManagerInternal != null) {
            activityManagerInternal.setDeviceIdleAllowlist(this.mPowerSaveWhitelistAllAppIdArray, this.mPowerSaveWhitelistExceptIdleAppIdArray);
        }
        PowerManagerInternal powerManagerInternal = this.mLocalPowerManager;
        if (powerManagerInternal != null) {
            powerManagerInternal.setDeviceIdleWhitelist(this.mPowerSaveWhitelistAllAppIdArray);
        }
        passWhiteListsToForceAppStandbyTrackerLocked();
    }

    @GuardedBy({"this"})
    private void updateTempWhitelistAppIdsLocked(int i, boolean z, long j, int i2, int i3, String str, int i4) {
        int size = this.mTempWhitelistAppIdEndTimes.size();
        if (this.mTempWhitelistAppIdArray.length != size) {
            this.mTempWhitelistAppIdArray = new int[size];
        }
        for (int i5 = 0; i5 < size; i5++) {
            this.mTempWhitelistAppIdArray[i5] = this.mTempWhitelistAppIdEndTimes.keyAt(i5);
        }
        ActivityManagerInternal activityManagerInternal = this.mLocalActivityManager;
        if (activityManagerInternal != null) {
            activityManagerInternal.updateDeviceIdleTempAllowlist(this.mTempWhitelistAppIdArray, i, z, j, i2, i3, str, i4);
        }
        PowerManagerInternal powerManagerInternal = this.mLocalPowerManager;
        if (powerManagerInternal != null) {
            powerManagerInternal.setDeviceIdleTempWhitelist(this.mTempWhitelistAppIdArray);
        }
        passWhiteListsToForceAppStandbyTrackerLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportPowerSaveWhitelistChangedLocked() {
        getContext().sendBroadcastAsUser(this.mPowerSaveWhitelistChangedIntent, UserHandle.SYSTEM, null, this.mPowerSaveWhitelistChangedOptions);
    }

    private void reportTempWhitelistChangedLocked(int i, boolean z) {
        this.mHandler.obtainMessage(13, i, z ? 1 : 0).sendToTarget();
        getContext().sendBroadcastAsUser(this.mPowerSaveTempWhitelistChangedIntent, UserHandle.SYSTEM, null, this.mPowerSaveTempWhilelistChangedOptions);
    }

    private void passWhiteListsToForceAppStandbyTrackerLocked() {
        this.mAppStateTracker.setPowerSaveExemptionListAppIds(this.mPowerSaveWhitelistExceptIdleAppIdArray, this.mPowerSaveWhitelistUserAppIdArray, this.mTempWhitelistAppIdArray);
    }

    @GuardedBy({"this"})
    void readConfigFileLocked() {
        this.mPowerSaveWhitelistUserApps.clear();
        try {
            FileInputStream openRead = this.mConfigFile.openRead();
            try {
                XmlPullParser newPullParser = Xml.newPullParser();
                newPullParser.setInput(openRead, StandardCharsets.UTF_8.name());
                readConfigFileLocked(newPullParser);
            } catch (XmlPullParserException unused) {
            } catch (Throwable th) {
                try {
                    openRead.close();
                } catch (IOException unused2) {
                }
                throw th;
            }
            openRead.close();
        } catch (FileNotFoundException | IOException unused3) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0099 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0061 A[SYNTHETIC] */
    @GuardedBy({"this"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void readConfigFileLocked(XmlPullParser xmlPullParser) {
        int next;
        char c;
        PackageManager packageManager = getContext().getPackageManager();
        do {
            try {
                next = xmlPullParser.next();
                if (next == 2) {
                    break;
                }
            } catch (IOException e) {
                Slog.w(TAG, "Failed parsing config " + e);
                return;
            } catch (IllegalStateException e2) {
                Slog.w(TAG, "Failed parsing config " + e2);
                return;
            } catch (IndexOutOfBoundsException e3) {
                Slog.w(TAG, "Failed parsing config " + e3);
                return;
            } catch (NullPointerException e4) {
                Slog.w(TAG, "Failed parsing config " + e4);
                return;
            } catch (NumberFormatException e5) {
                Slog.w(TAG, "Failed parsing config " + e5);
                return;
            } catch (XmlPullParserException e6) {
                Slog.w(TAG, "Failed parsing config " + e6);
                return;
            }
        } while (next != 1);
        if (next != 2) {
            throw new IllegalStateException("no start tag found");
        }
        int depth = xmlPullParser.getDepth();
        while (true) {
            int next2 = xmlPullParser.next();
            if (next2 == 1) {
                return;
            }
            if (next2 == 3 && xmlPullParser.getDepth() <= depth) {
                return;
            }
            if (next2 != 3 && next2 != 4) {
                String name = xmlPullParser.getName();
                int hashCode = name.hashCode();
                if (hashCode != 3797) {
                    if (hashCode == 111376009 && name.equals("un-wl")) {
                        c = 1;
                        if (c != 0) {
                            String attributeValue = xmlPullParser.getAttributeValue(null, "n");
                            if (attributeValue != null) {
                                try {
                                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(attributeValue, AudioDevice.OUT_SPEAKER_SAFE);
                                    this.mPowerSaveWhitelistUserApps.put(applicationInfo.packageName, Integer.valueOf(UserHandle.getAppId(applicationInfo.uid)));
                                } catch (PackageManager.NameNotFoundException unused) {
                                }
                            }
                        } else if (c == 1) {
                            String attributeValue2 = xmlPullParser.getAttributeValue(null, "n");
                            if (this.mPowerSaveWhitelistApps.containsKey(attributeValue2)) {
                                this.mRemovedFromSystemWhitelistApps.put(attributeValue2, this.mPowerSaveWhitelistApps.remove(attributeValue2));
                            }
                        } else {
                            Slog.w(TAG, "Unknown element under <config>: " + xmlPullParser.getName());
                            XmlUtils.skipCurrentTag(xmlPullParser);
                        }
                    }
                    c = 65535;
                    if (c != 0) {
                    }
                } else {
                    if (name.equals("wl")) {
                        c = 0;
                        if (c != 0) {
                        }
                    }
                    c = 65535;
                    if (c != 0) {
                    }
                }
            }
        }
    }

    void writeConfigFileLocked() {
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessageDelayed(1, 5000L);
    }

    void handleWriteConfigFile() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            synchronized (this) {
                XmlSerializer fastXmlSerializer = new FastXmlSerializer();
                fastXmlSerializer.setOutput(byteArrayOutputStream, StandardCharsets.UTF_8.name());
                writeConfigFileLocked(fastXmlSerializer);
            }
        } catch (IOException unused) {
        }
        synchronized (this.mConfigFile) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = this.mConfigFile.startWrite();
                byteArrayOutputStream.writeTo(fileOutputStream);
                this.mConfigFile.finishWrite(fileOutputStream);
            } catch (IOException e) {
                Slog.w(TAG, "Error writing config file", e);
                this.mConfigFile.failWrite(fileOutputStream);
            }
        }
    }

    void writeConfigFileLocked(XmlSerializer xmlSerializer) throws IOException {
        xmlSerializer.startDocument(null, Boolean.TRUE);
        xmlSerializer.startTag(null, "config");
        for (int i = 0; i < this.mPowerSaveWhitelistUserApps.size(); i++) {
            String keyAt = this.mPowerSaveWhitelistUserApps.keyAt(i);
            xmlSerializer.startTag(null, "wl");
            xmlSerializer.attribute(null, "n", keyAt);
            xmlSerializer.endTag(null, "wl");
        }
        for (int i2 = 0; i2 < this.mRemovedFromSystemWhitelistApps.size(); i2++) {
            xmlSerializer.startTag(null, "un-wl");
            xmlSerializer.attribute(null, "n", this.mRemovedFromSystemWhitelistApps.keyAt(i2));
            xmlSerializer.endTag(null, "un-wl");
        }
        xmlSerializer.endTag(null, "config");
        xmlSerializer.endDocument();
    }

    static void dumpHelp(PrintWriter printWriter) {
        printWriter.println("Device idle controller (deviceidle) commands:");
        printWriter.println("  help");
        printWriter.println("    Print this help text.");
        printWriter.println("  step [light|deep]");
        printWriter.println("    Immediately step to next state, without waiting for alarm.");
        printWriter.println("  force-idle [light|deep]");
        printWriter.println("    Force directly into idle mode, regardless of other device state.");
        printWriter.println("  force-inactive");
        printWriter.println("    Force to be inactive, ready to freely step idle states.");
        printWriter.println("  unforce");
        printWriter.println("    Resume normal functioning after force-idle or force-inactive.");
        printWriter.println("  get [light|deep|force|screen|charging|network]");
        printWriter.println("    Retrieve the current given state.");
        printWriter.println("  disable [light|deep|all]");
        printWriter.println("    Completely disable device idle mode.");
        printWriter.println("  enable [light|deep|all]");
        printWriter.println("    Re-enable device idle mode after it had previously been disabled.");
        printWriter.println("  enabled [light|deep|all]");
        printWriter.println("    Print 1 if device idle mode is currently enabled, else 0.");
        printWriter.println("  whitelist");
        printWriter.println("    Print currently whitelisted apps.");
        printWriter.println("  whitelist [package ...]");
        printWriter.println("    Add (prefix with +) or remove (prefix with -) packages.");
        printWriter.println("  sys-whitelist [package ...|reset]");
        printWriter.println("    Prefix the package with '-' to remove it from the system whitelist or '+' to put it back in the system whitelist.");
        printWriter.println("    Note that only packages that were earlier removed from the system whitelist can be added back.");
        printWriter.println("    reset will reset the whitelist to the original state");
        printWriter.println("    Prints the system whitelist if no arguments are specified");
        printWriter.println("  except-idle-whitelist [package ...|reset]");
        printWriter.println("    Prefix the package with '+' to add it to whitelist or '=' to check if it is already whitelisted");
        printWriter.println("    [reset] will reset the whitelist to it's original state");
        printWriter.println("    Note that unlike <whitelist> cmd, changes made using this won't be persisted across boots");
        printWriter.println("  tempwhitelist");
        printWriter.println("    Print packages that are temporarily whitelisted.");
        printWriter.println("  tempwhitelist [-u USER] [-d DURATION] [-r] [package]");
        printWriter.println("    Temporarily place package in whitelist for DURATION milliseconds.");
        printWriter.println("    If no DURATION is specified, 10 seconds is used");
        printWriter.println("    If [-r] option is used, then the package is removed from temp whitelist and any [-d] is ignored");
        printWriter.println("  motion");
        printWriter.println("    Simulate a motion event to bring the device out of deep doze");
        printWriter.println("  pre-idle-factor [0|1|2]");
        printWriter.println("    Set a new factor to idle time before step to idle(inactive_to and idle_after_inactive_to)");
        printWriter.println("  reset-pre-idle-factor");
        printWriter.println("    Reset factor to idle time to default");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class Shell extends ShellCommand {
        int userId = 0;

        Shell() {
        }

        public int onCommand(String str) {
            return DeviceIdleController.this.onShellCommand(this, str);
        }

        public void onHelp() {
            DeviceIdleController.dumpHelp(getOutPrintWriter());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:208:0x033d A[Catch: all -> 0x039b, TryCatch #27 {all -> 0x039b, blocks: (B:234:0x0318, B:236:0x0320, B:208:0x033d, B:210:0x0346, B:214:0x035f, B:217:0x036c, B:220:0x0381, B:228:0x034e, B:230:0x0352, B:203:0x032b, B:205:0x032f), top: B:233:0x0318, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:214:0x035f A[Catch: all -> 0x039b, TryCatch #27 {all -> 0x039b, blocks: (B:234:0x0318, B:236:0x0320, B:208:0x033d, B:210:0x0346, B:214:0x035f, B:217:0x036c, B:220:0x0381, B:228:0x034e, B:230:0x0352, B:203:0x032b, B:205:0x032f), top: B:233:0x0318, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:220:0x0381 A[Catch: all -> 0x039b, TRY_LEAVE, TryCatch #27 {all -> 0x039b, blocks: (B:234:0x0318, B:236:0x0320, B:208:0x033d, B:210:0x0346, B:214:0x035f, B:217:0x036c, B:220:0x0381, B:228:0x034e, B:230:0x0352, B:203:0x032b, B:205:0x032f), top: B:233:0x0318, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0352 A[Catch: all -> 0x039b, TryCatch #27 {all -> 0x039b, blocks: (B:234:0x0318, B:236:0x0320, B:208:0x033d, B:210:0x0346, B:214:0x035f, B:217:0x036c, B:220:0x0381, B:228:0x034e, B:230:0x0352, B:203:0x032b, B:205:0x032f), top: B:233:0x0318, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x035b  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x03f4 A[Catch: all -> 0x0435, TryCatch #10 {all -> 0x0435, blocks: (B:281:0x03cf, B:283:0x03d7, B:259:0x03f4, B:261:0x03fd, B:265:0x0416, B:267:0x041b, B:275:0x0405, B:277:0x0409, B:254:0x03e2, B:256:0x03e6), top: B:280:0x03cf, outer: #13 }] */
    /* JADX WARN: Removed duplicated region for block: B:265:0x0416 A[Catch: all -> 0x0435, TryCatch #10 {all -> 0x0435, blocks: (B:281:0x03cf, B:283:0x03d7, B:259:0x03f4, B:261:0x03fd, B:265:0x0416, B:267:0x041b, B:275:0x0405, B:277:0x0409, B:254:0x03e2, B:256:0x03e6), top: B:280:0x03cf, outer: #13 }] */
    /* JADX WARN: Removed duplicated region for block: B:267:0x041b A[Catch: all -> 0x0435, TRY_LEAVE, TryCatch #10 {all -> 0x0435, blocks: (B:281:0x03cf, B:283:0x03d7, B:259:0x03f4, B:261:0x03fd, B:265:0x0416, B:267:0x041b, B:275:0x0405, B:277:0x0409, B:254:0x03e2, B:256:0x03e6), top: B:280:0x03cf, outer: #13 }] */
    /* JADX WARN: Removed duplicated region for block: B:277:0x0409 A[Catch: all -> 0x0435, TryCatch #10 {all -> 0x0435, blocks: (B:281:0x03cf, B:283:0x03d7, B:259:0x03f4, B:261:0x03fd, B:265:0x0416, B:267:0x041b, B:275:0x0405, B:277:0x0409, B:254:0x03e2, B:256:0x03e6), top: B:280:0x03cf, outer: #13 }] */
    /* JADX WARN: Removed duplicated region for block: B:278:0x0412  */
    /* JADX WARN: Removed duplicated region for block: B:580:0x08e6 A[Catch: all -> 0x090b, NumberFormatException -> 0x090e, TRY_LEAVE, TryCatch #17 {all -> 0x090b, blocks: (B:574:0x08ad, B:576:0x08b3, B:578:0x08bd, B:580:0x08e6, B:586:0x08d5, B:588:0x08dd, B:590:0x090e), top: B:573:0x08ad, outer: #9, inners: #12 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    int onShellCommand(Shell shell, String str) {
        long clearCallingIdentity;
        String nextArg;
        char c;
        char c2;
        char c3;
        char c4;
        PrintWriter outPrintWriter = shell.getOutPrintWriter();
        if ("step".equals(str)) {
            getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            synchronized (this) {
                clearCallingIdentity = Binder.clearCallingIdentity();
                String nextArg2 = shell.getNextArg();
                if (nextArg2 != null) {
                    try {
                        if (!"deep".equals(nextArg2)) {
                            if ("light".equals(nextArg2)) {
                                stepLightIdleStateLocked("s:shell");
                                outPrintWriter.print("Stepped to light: ");
                                outPrintWriter.println(lightStateToString(this.mLightState));
                            } else {
                                outPrintWriter.println("Unknown idle mode: " + nextArg2);
                            }
                        }
                    } finally {
                    }
                }
                stepIdleStateLocked("s:shell");
                outPrintWriter.print("Stepped to deep: ");
                outPrintWriter.println(stateToString(this.mState));
            }
        } else {
            char c5 = 1;
            if ("force-active".equals(str)) {
                getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                synchronized (this) {
                    clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        this.mForceIdle = true;
                        becomeActiveLocked("force-active", Process.myUid());
                        outPrintWriter.print("Light state: ");
                        outPrintWriter.print(lightStateToString(this.mLightState));
                        outPrintWriter.print(", deep state: ");
                        outPrintWriter.println(stateToString(this.mState));
                    } finally {
                    }
                }
            } else {
                int i = -1;
                if ("force-idle".equals(str)) {
                    getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                    synchronized (this) {
                        clearCallingIdentity = Binder.clearCallingIdentity();
                        String nextArg3 = shell.getNextArg();
                        if (nextArg3 != null) {
                            try {
                                if (!"deep".equals(nextArg3)) {
                                    if ("light".equals(nextArg3)) {
                                        this.mForceIdle = true;
                                        becomeInactiveIfAppropriateLocked();
                                        int i2 = this.mLightState;
                                        while (i2 != 4) {
                                            stepLightIdleStateLocked("s:shell");
                                            int i3 = this.mLightState;
                                            if (i2 == i3) {
                                                outPrintWriter.print("Unable to go light idle; stopped at ");
                                                outPrintWriter.println(lightStateToString(this.mLightState));
                                                exitForceIdleLocked();
                                                return -1;
                                            }
                                            i2 = i3;
                                        }
                                        outPrintWriter.println("Now forced in to light idle mode");
                                    } else {
                                        outPrintWriter.println("Unknown idle mode: " + nextArg3);
                                    }
                                }
                            } finally {
                            }
                        }
                        if (!this.mDeepEnabled) {
                            outPrintWriter.println("Unable to go deep idle; not enabled");
                            return -1;
                        }
                        this.mForceIdle = true;
                        becomeInactiveIfAppropriateLocked();
                        int i4 = this.mState;
                        while (i4 != 5) {
                            stepIdleStateLocked("s:shell");
                            int i5 = this.mState;
                            if (i4 == i5) {
                                outPrintWriter.print("Unable to go deep idle; stopped at ");
                                outPrintWriter.println(stateToString(this.mState));
                                exitForceIdleLocked();
                                return -1;
                            }
                            i4 = i5;
                        }
                        outPrintWriter.println("Now forced in to deep idle mode");
                    }
                } else if ("force-inactive".equals(str)) {
                    getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                    synchronized (this) {
                        clearCallingIdentity = Binder.clearCallingIdentity();
                        try {
                            this.mForceIdle = true;
                            becomeInactiveIfAppropriateLocked();
                            outPrintWriter.print("Light state: ");
                            outPrintWriter.print(lightStateToString(this.mLightState));
                            outPrintWriter.print(", deep state: ");
                            outPrintWriter.println(stateToString(this.mState));
                        } finally {
                        }
                    }
                } else if ("unforce".equals(str)) {
                    getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                    synchronized (this) {
                        clearCallingIdentity = Binder.clearCallingIdentity();
                        try {
                            exitForceIdleLocked();
                            outPrintWriter.print("Light state: ");
                            outPrintWriter.print(lightStateToString(this.mLightState));
                            outPrintWriter.print(", deep state: ");
                            outPrintWriter.println(stateToString(this.mState));
                        } finally {
                        }
                    }
                } else if ("get".equals(str)) {
                    getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                    synchronized (this) {
                        String nextArg4 = shell.getNextArg();
                        if (nextArg4 != null) {
                            clearCallingIdentity = Binder.clearCallingIdentity();
                            try {
                                switch (nextArg4.hashCode()) {
                                    case -907689876:
                                        if (nextArg4.equals("screen")) {
                                            c5 = 4;
                                            break;
                                        }
                                        c5 = 65535;
                                        break;
                                    case 3079404:
                                        if (nextArg4.equals("deep")) {
                                            break;
                                        }
                                        c5 = 65535;
                                        break;
                                    case 97618667:
                                        if (nextArg4.equals("force")) {
                                            c5 = 2;
                                            break;
                                        }
                                        c5 = 65535;
                                        break;
                                    case 102970646:
                                        if (nextArg4.equals("light")) {
                                            c5 = 0;
                                            break;
                                        }
                                        c5 = 65535;
                                        break;
                                    case 107947501:
                                        if (nextArg4.equals("quick")) {
                                            c5 = 3;
                                            break;
                                        }
                                        c5 = 65535;
                                        break;
                                    case 1436115569:
                                        if (nextArg4.equals("charging")) {
                                            c5 = 5;
                                            break;
                                        }
                                        c5 = 65535;
                                        break;
                                    case 1843485230:
                                        if (nextArg4.equals("network")) {
                                            c5 = 6;
                                            break;
                                        }
                                        c5 = 65535;
                                        break;
                                    default:
                                        c5 = 65535;
                                        break;
                                }
                                switch (c5) {
                                    case 0:
                                        outPrintWriter.println(lightStateToString(this.mLightState));
                                        break;
                                    case 1:
                                        outPrintWriter.println(stateToString(this.mState));
                                        break;
                                    case 2:
                                        outPrintWriter.println(this.mForceIdle);
                                        break;
                                    case 3:
                                        outPrintWriter.println(this.mQuickDozeActivated);
                                        break;
                                    case 4:
                                        outPrintWriter.println(this.mScreenOn);
                                        break;
                                    case 5:
                                        outPrintWriter.println(this.mCharging);
                                        break;
                                    case 6:
                                        outPrintWriter.println(this.mNetworkConnected);
                                        break;
                                    default:
                                        outPrintWriter.println("Unknown get option: " + nextArg4);
                                        break;
                                }
                                Binder.restoreCallingIdentity(clearCallingIdentity);
                            } finally {
                            }
                        } else {
                            outPrintWriter.println("Argument required");
                        }
                    }
                } else if ("disable".equals(str)) {
                    getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                    synchronized (this) {
                        clearCallingIdentity = Binder.clearCallingIdentity();
                        String nextArg5 = shell.getNextArg();
                        if (nextArg5 != null) {
                            try {
                                if (!"deep".equals(nextArg5) && !"all".equals(nextArg5)) {
                                    c3 = 0;
                                    c4 = c3;
                                    if (nextArg5 != null || "light".equals(nextArg5) || "all".equals(nextArg5)) {
                                        if (this.mLightEnabled) {
                                            this.mLightEnabled = false;
                                            outPrintWriter.println("Light idle mode disabled");
                                            c4 = 1;
                                            if (c5 != 0) {
                                                this.mActiveReason = 6;
                                                StringBuilder sb = new StringBuilder();
                                                sb.append(nextArg5 == null ? "all" : nextArg5);
                                                sb.append("-disabled");
                                                becomeActiveLocked(sb.toString(), Process.myUid());
                                            }
                                            if (c4 == 0) {
                                                outPrintWriter.println("Unknown idle mode: " + nextArg5);
                                            }
                                        } else {
                                            c4 = 1;
                                        }
                                    }
                                    c5 = c3;
                                    if (c5 != 0) {
                                    }
                                    if (c4 == 0) {
                                    }
                                }
                            } finally {
                            }
                        }
                        if (this.mDeepEnabled) {
                            this.mDeepEnabled = false;
                            outPrintWriter.println("Deep idle mode disabled");
                            c3 = 1;
                            c4 = c3;
                            if (nextArg5 != null) {
                            }
                            if (this.mLightEnabled) {
                            }
                        } else {
                            c4 = 1;
                            c3 = 0;
                            if (nextArg5 != null) {
                            }
                            if (this.mLightEnabled) {
                            }
                        }
                    }
                } else if (IOplusBluetoothManagerServiceExt.FLAG_ENABLE.equals(str)) {
                    getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                    if (this.mDeviceIdleExt.isCustomizeDozeModeDisabled()) {
                        outPrintWriter.println("Customize denied.");
                        return 0;
                    }
                    synchronized (this) {
                        clearCallingIdentity = Binder.clearCallingIdentity();
                        String nextArg6 = shell.getNextArg();
                        if (nextArg6 != null) {
                            try {
                                if (!"deep".equals(nextArg6) && !"all".equals(nextArg6)) {
                                    c = 0;
                                    c2 = c;
                                    if (nextArg6 != null || "light".equals(nextArg6) || "all".equals(nextArg6)) {
                                        if (this.mLightEnabled) {
                                            c2 = 1;
                                        } else {
                                            this.mLightEnabled = true;
                                            outPrintWriter.println("Light idle mode enable");
                                            c2 = 1;
                                            if (c5 != 0) {
                                                becomeInactiveIfAppropriateLocked();
                                            }
                                            if (c2 == 0) {
                                                outPrintWriter.println("Unknown idle mode: " + nextArg6);
                                            }
                                        }
                                    }
                                    c5 = c;
                                    if (c5 != 0) {
                                    }
                                    if (c2 == 0) {
                                    }
                                }
                            } finally {
                            }
                        }
                        if (this.mDeepEnabled) {
                            c2 = 1;
                            c = 0;
                            if (nextArg6 != null) {
                            }
                            if (this.mLightEnabled) {
                            }
                        } else {
                            this.mDeepEnabled = true;
                            outPrintWriter.println("Deep idle mode enabled");
                            c = 1;
                            c2 = c;
                            if (nextArg6 != null) {
                            }
                            if (this.mLightEnabled) {
                            }
                        }
                    }
                } else if ("enabled".equals(str)) {
                    synchronized (this) {
                        String nextArg7 = shell.getNextArg();
                        if (nextArg7 != null && !"all".equals(nextArg7)) {
                            if ("deep".equals(nextArg7)) {
                                outPrintWriter.println(this.mDeepEnabled ? "1" : 0);
                            } else if ("light".equals(nextArg7)) {
                                outPrintWriter.println(this.mLightEnabled ? "1" : 0);
                            } else {
                                outPrintWriter.println("Unknown idle mode: " + nextArg7);
                            }
                        }
                        if (this.mDeepEnabled && this.mLightEnabled) {
                            r6 = "1";
                        }
                        outPrintWriter.println(r6);
                    }
                } else if ("whitelist".equals(str)) {
                    String nextArg8 = shell.getNextArg();
                    if (nextArg8 != null) {
                        getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                        clearCallingIdentity = Binder.clearCallingIdentity();
                        do {
                            try {
                                if (nextArg8.length() >= 1 && (nextArg8.charAt(0) == '-' || nextArg8.charAt(0) == '+' || nextArg8.charAt(0) == '=')) {
                                    char charAt = nextArg8.charAt(0);
                                    String substring = nextArg8.substring(1);
                                    if (charAt == '+') {
                                        if (addPowerSaveWhitelistAppsInternal(Collections.singletonList(substring)) == 1) {
                                            outPrintWriter.println("Added: " + substring);
                                        } else {
                                            outPrintWriter.println("Unknown package: " + substring);
                                        }
                                    } else if (charAt != '-') {
                                        outPrintWriter.println(getPowerSaveWhitelistAppInternal(substring));
                                    } else if (removePowerSaveWhitelistAppInternal(substring)) {
                                        outPrintWriter.println("Removed: " + substring);
                                    }
                                    nextArg8 = shell.getNextArg();
                                }
                                outPrintWriter.println("Package must be prefixed with +, -, or =: " + nextArg8);
                                return -1;
                            } finally {
                            }
                        } while (nextArg8 != null);
                    }
                    if (!DumpUtils.checkDumpPermission(getContext(), TAG, outPrintWriter)) {
                        return -1;
                    }
                    synchronized (this) {
                        for (int i6 = 0; i6 < this.mPowerSaveWhitelistAppsExceptIdle.size(); i6++) {
                            outPrintWriter.print("system-excidle,");
                            outPrintWriter.print(this.mPowerSaveWhitelistAppsExceptIdle.keyAt(i6));
                            outPrintWriter.print(",");
                            outPrintWriter.println(this.mPowerSaveWhitelistAppsExceptIdle.valueAt(i6));
                        }
                        for (int i7 = 0; i7 < this.mPowerSaveWhitelistApps.size(); i7++) {
                            outPrintWriter.print("system,");
                            outPrintWriter.print(this.mPowerSaveWhitelistApps.keyAt(i7));
                            outPrintWriter.print(",");
                            outPrintWriter.println(this.mPowerSaveWhitelistApps.valueAt(i7));
                        }
                        for (int i8 = 0; i8 < this.mPowerSaveWhitelistUserApps.size(); i8++) {
                            outPrintWriter.print("user,");
                            outPrintWriter.print(this.mPowerSaveWhitelistUserApps.keyAt(i8));
                            outPrintWriter.print(",");
                            outPrintWriter.println(this.mPowerSaveWhitelistUserApps.valueAt(i8));
                        }
                    }
                } else if ("tempwhitelist".equals(str)) {
                    long j = IDeviceIdleControllerExt.ADVANCE_TIME;
                    boolean z = false;
                    while (true) {
                        String nextOption = shell.getNextOption();
                        if (nextOption == null) {
                            String nextArg9 = shell.getNextArg();
                            if (nextArg9 != null) {
                                try {
                                    if (z) {
                                        removePowerSaveTempAllowlistAppChecked(nextArg9, shell.userId);
                                    } else {
                                        addPowerSaveTempAllowlistAppChecked(nextArg9, j, shell.userId, FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_SHELL, "shell");
                                    }
                                } catch (Exception e) {
                                    outPrintWriter.println("Failed: " + e);
                                    return -1;
                                }
                            } else {
                                if (z) {
                                    outPrintWriter.println("[-r] requires a package name");
                                    return -1;
                                }
                                if (!DumpUtils.checkDumpPermission(getContext(), TAG, outPrintWriter)) {
                                    return -1;
                                }
                                dumpTempWhitelistSchedule(outPrintWriter, false);
                            }
                        } else if ("-u".equals(nextOption)) {
                            String nextArg10 = shell.getNextArg();
                            if (nextArg10 == null) {
                                outPrintWriter.println("-u requires a user number");
                                return -1;
                            }
                            shell.userId = Integer.parseInt(nextArg10);
                        } else if ("-d".equals(nextOption)) {
                            String nextArg11 = shell.getNextArg();
                            if (nextArg11 == null) {
                                outPrintWriter.println("-d requires a duration");
                                return -1;
                            }
                            j = Long.parseLong(nextArg11);
                        } else if ("-r".equals(nextOption)) {
                            z = true;
                        }
                    }
                } else {
                    if ("except-idle-whitelist".equals(str)) {
                        getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                        clearCallingIdentity = Binder.clearCallingIdentity();
                        try {
                            String nextArg12 = shell.getNextArg();
                            if (nextArg12 == null) {
                                outPrintWriter.println("No arguments given");
                                return -1;
                            }
                            if ("reset".equals(nextArg12)) {
                                resetPowerSaveWhitelistExceptIdleInternal();
                            }
                            while (nextArg12.length() >= 1 && (nextArg12.charAt(0) == '-' || nextArg12.charAt(0) == '+' || nextArg12.charAt(0) == '=')) {
                                char charAt2 = nextArg12.charAt(0);
                                String substring2 = nextArg12.substring(1);
                                if (charAt2 != '+') {
                                    if (charAt2 != '=') {
                                        outPrintWriter.println("Unknown argument: " + nextArg12);
                                        return -1;
                                    }
                                    outPrintWriter.println(getPowerSaveWhitelistExceptIdleInternal(substring2));
                                } else if (addPowerSaveWhitelistExceptIdleInternal(substring2)) {
                                    outPrintWriter.println("Added: " + substring2);
                                } else {
                                    outPrintWriter.println("Unknown package: " + substring2);
                                }
                                nextArg12 = shell.getNextArg();
                                if (nextArg12 == null) {
                                }
                            }
                            outPrintWriter.println("Package must be prefixed with +, -, or =: " + nextArg12);
                            return -1;
                        } finally {
                        }
                    }
                    if ("sys-whitelist".equals(str)) {
                        String nextArg13 = shell.getNextArg();
                        if (nextArg13 != null) {
                            getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                            clearCallingIdentity = Binder.clearCallingIdentity();
                            try {
                                if ("reset".equals(nextArg13)) {
                                    resetSystemPowerWhitelistInternal();
                                }
                                while (nextArg13.length() >= 1 && (nextArg13.charAt(0) == '-' || nextArg13.charAt(0) == '+')) {
                                    char charAt3 = nextArg13.charAt(0);
                                    String substring3 = nextArg13.substring(1);
                                    if (charAt3 != '+') {
                                        if (charAt3 == '-' && removeSystemPowerWhitelistAppInternal(substring3)) {
                                            outPrintWriter.println("Removed " + substring3);
                                        }
                                    } else if (restoreSystemPowerWhitelistAppInternal(substring3)) {
                                        outPrintWriter.println("Restored " + substring3);
                                    }
                                    nextArg13 = shell.getNextArg();
                                    if (nextArg13 == null) {
                                    }
                                }
                                outPrintWriter.println("Package must be prefixed with + or - " + nextArg13);
                                return -1;
                            } finally {
                            }
                        }
                        if (!DumpUtils.checkDumpPermission(getContext(), TAG, outPrintWriter)) {
                            return -1;
                        }
                        synchronized (this) {
                            for (int i9 = 0; i9 < this.mPowerSaveWhitelistApps.size(); i9++) {
                                outPrintWriter.print(this.mPowerSaveWhitelistApps.keyAt(i9));
                                outPrintWriter.print(",");
                                outPrintWriter.println(this.mPowerSaveWhitelistApps.valueAt(i9));
                            }
                        }
                    } else if ("motion".equals(str)) {
                        getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                        synchronized (this) {
                            clearCallingIdentity = Binder.clearCallingIdentity();
                            try {
                                motionLocked();
                                outPrintWriter.print("Light state: ");
                                outPrintWriter.print(lightStateToString(this.mLightState));
                                outPrintWriter.print(", deep state: ");
                                outPrintWriter.println(stateToString(this.mState));
                            } finally {
                            }
                        }
                    } else if ("pre-idle-factor".equals(str)) {
                        getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                        synchronized (this) {
                            clearCallingIdentity = Binder.clearCallingIdentity();
                            try {
                                try {
                                    nextArg = shell.getNextArg();
                                } finally {
                                }
                            } catch (NumberFormatException unused) {
                                outPrintWriter.println("Unknown idle timeout factor,(error code: " + i + ")");
                            }
                            if (nextArg != null) {
                                int parseInt = Integer.parseInt(nextArg);
                                i = setPreIdleTimeoutMode(parseInt);
                                if (i == 1) {
                                    outPrintWriter.println("pre-idle-factor: " + parseInt);
                                } else if (i == 2) {
                                    outPrintWriter.println("Deep idle not supported");
                                } else if (i == 0) {
                                    outPrintWriter.println("Idle timeout factor not changed");
                                }
                                if (c5 == 0) {
                                    outPrintWriter.println("Unknown idle timeout factor: " + nextArg + ",(error code: " + i + ")");
                                }
                            }
                            c5 = 0;
                            if (c5 == 0) {
                            }
                        }
                    } else if ("reset-pre-idle-factor".equals(str)) {
                        getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                        synchronized (this) {
                            clearCallingIdentity = Binder.clearCallingIdentity();
                            try {
                                resetPreIdleTimeoutMode();
                            } finally {
                            }
                        }
                    } else {
                        if (!"oplushelper".equals(str)) {
                            return shell.handleDefaultCommands(str);
                        }
                        this.mDeviceIdleExt.onShellCommand(shell);
                    }
                }
            }
        }
        return 0;
    }

    void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(getContext(), TAG, printWriter)) {
            if (strArr != null) {
                int i = 0;
                int i2 = 0;
                while (i < strArr.length) {
                    String str = strArr[i];
                    if ("-h".equals(str)) {
                        dumpHelp(printWriter);
                        return;
                    }
                    if ("-u".equals(str)) {
                        i++;
                        if (i < strArr.length) {
                            i2 = Integer.parseInt(strArr[i]);
                        }
                    } else if (!"-a".equals(str)) {
                        if (str.length() > 0 && str.charAt(0) == '-') {
                            printWriter.println("Unknown option: " + str);
                            return;
                        }
                        if ("custom-whitelist".equals(str)) {
                            this.mDeviceIdleExt.dump(printWriter);
                            return;
                        }
                        Shell shell = new Shell();
                        shell.userId = i2;
                        String[] strArr2 = new String[strArr.length - i];
                        System.arraycopy(strArr, i, strArr2, 0, strArr.length - i);
                        shell.exec(this.mBinderService, (FileDescriptor) null, fileDescriptor, (FileDescriptor) null, strArr2, (ShellCallback) null, new ResultReceiver(null));
                        return;
                    }
                    i++;
                }
            }
            synchronized (this) {
                this.mConstants.dump(printWriter);
                if (this.mEventCmds[0] != 0) {
                    printWriter.println("  Idling history:");
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    for (int i3 = 99; i3 >= 0; i3--) {
                        int i4 = this.mEventCmds[i3];
                        if (i4 != 0) {
                            String str2 = i4 != 1 ? i4 != 2 ? i4 != 3 ? i4 != 4 ? i4 != 5 ? "         ??" : " deep-maint" : "  deep-idle" : "light-maint" : " light-idle" : "     normal";
                            printWriter.print("    ");
                            printWriter.print(str2);
                            printWriter.print(": ");
                            TimeUtils.formatDuration(this.mEventTimes[i3], elapsedRealtime, printWriter);
                            if (this.mEventReasons[i3] != null) {
                                printWriter.print(" (");
                                printWriter.print(this.mEventReasons[i3]);
                                printWriter.print(")");
                            }
                            printWriter.println();
                        }
                    }
                }
                int size = this.mPowerSaveWhitelistAppsExceptIdle.size();
                if (size > 0) {
                    printWriter.println("  Whitelist (except idle) system apps:");
                    for (int i5 = 0; i5 < size; i5++) {
                        printWriter.print("    ");
                        printWriter.println(this.mPowerSaveWhitelistAppsExceptIdle.keyAt(i5));
                    }
                }
                int size2 = this.mPowerSaveWhitelistApps.size();
                if (size2 > 0) {
                    printWriter.println("  Whitelist system apps:");
                    for (int i6 = 0; i6 < size2; i6++) {
                        printWriter.print("    ");
                        printWriter.println(this.mPowerSaveWhitelistApps.keyAt(i6));
                    }
                }
                int size3 = this.mRemovedFromSystemWhitelistApps.size();
                if (size3 > 0) {
                    printWriter.println("  Removed from whitelist system apps:");
                    for (int i7 = 0; i7 < size3; i7++) {
                        printWriter.print("    ");
                        printWriter.println(this.mRemovedFromSystemWhitelistApps.keyAt(i7));
                    }
                }
                int size4 = this.mPowerSaveWhitelistUserApps.size();
                if (size4 > 0) {
                    printWriter.println("  Whitelist user apps:");
                    for (int i8 = 0; i8 < size4; i8++) {
                        printWriter.print("    ");
                        printWriter.println(this.mPowerSaveWhitelistUserApps.keyAt(i8));
                    }
                }
                int size5 = this.mPowerSaveWhitelistExceptIdleAppIds.size();
                if (size5 > 0) {
                    printWriter.println("  Whitelist (except idle) all app ids:");
                    for (int i9 = 0; i9 < size5; i9++) {
                        printWriter.print("    ");
                        printWriter.print(this.mPowerSaveWhitelistExceptIdleAppIds.keyAt(i9));
                        printWriter.println();
                    }
                }
                int size6 = this.mPowerSaveWhitelistUserAppIds.size();
                if (size6 > 0) {
                    printWriter.println("  Whitelist user app ids:");
                    for (int i10 = 0; i10 < size6; i10++) {
                        printWriter.print("    ");
                        printWriter.print(this.mPowerSaveWhitelistUserAppIds.keyAt(i10));
                        printWriter.println();
                    }
                }
                int size7 = this.mPowerSaveWhitelistAllAppIds.size();
                if (size7 > 0) {
                    printWriter.println("  Whitelist all app ids:");
                    for (int i11 = 0; i11 < size7; i11++) {
                        printWriter.print("    ");
                        printWriter.print(this.mPowerSaveWhitelistAllAppIds.keyAt(i11));
                        printWriter.println();
                    }
                }
                dumpTempWhitelistSchedule(printWriter, true);
                int[] iArr = this.mTempWhitelistAppIdArray;
                int length = iArr != null ? iArr.length : 0;
                if (length > 0) {
                    printWriter.println("  Temp whitelist app ids:");
                    for (int i12 = 0; i12 < length; i12++) {
                        printWriter.print("    ");
                        printWriter.print(this.mTempWhitelistAppIdArray[i12]);
                        printWriter.println();
                    }
                }
                printWriter.print("  mLightEnabled=");
                printWriter.print(this.mLightEnabled);
                printWriter.print("  mDeepEnabled=");
                printWriter.println(this.mDeepEnabled);
                printWriter.print("  mForceIdle=");
                printWriter.println(this.mForceIdle);
                printWriter.print("  mUseMotionSensor=");
                printWriter.print(this.mUseMotionSensor);
                if (this.mUseMotionSensor) {
                    printWriter.print(" mMotionSensor=");
                    printWriter.println(this.mMotionSensor);
                } else {
                    printWriter.println();
                }
                printWriter.print("  mScreenOn=");
                printWriter.println(this.mScreenOn);
                printWriter.print("  mScreenLocked=");
                printWriter.println(this.mScreenLocked);
                printWriter.print("  mNetworkConnected=");
                printWriter.println(this.mNetworkConnected);
                printWriter.print("  mCharging=");
                printWriter.println(this.mCharging);
                printWriter.print("  activeEmergencyCall=");
                printWriter.println(this.mEmergencyCallListener.isEmergencyCallActive());
                if (this.mConstraints.size() != 0) {
                    printWriter.println("  mConstraints={");
                    for (int i13 = 0; i13 < this.mConstraints.size(); i13++) {
                        DeviceIdleConstraintTracker valueAt = this.mConstraints.valueAt(i13);
                        printWriter.print("    \"");
                        printWriter.print(valueAt.name);
                        printWriter.print("\"=");
                        if (valueAt.minState == this.mState) {
                            printWriter.println(valueAt.active);
                        } else {
                            printWriter.print("ignored <mMinState=");
                            printWriter.print(stateToString(valueAt.minState));
                            printWriter.println(">");
                        }
                    }
                    printWriter.println("  }");
                }
                if (this.mUseMotionSensor || this.mStationaryListeners.size() > 0) {
                    printWriter.print("  mMotionActive=");
                    printWriter.println(this.mMotionListener.active);
                    printWriter.print("  mNotMoving=");
                    printWriter.println(this.mNotMoving);
                    printWriter.print("  mMotionListener.activatedTimeElapsed=");
                    printWriter.println(this.mMotionListener.activatedTimeElapsed);
                    printWriter.print("  mLastMotionEventElapsed=");
                    printWriter.println(this.mLastMotionEventElapsed);
                    printWriter.print("  ");
                    printWriter.print(this.mStationaryListeners.size());
                    printWriter.println(" stationary listeners registered");
                }
                if (this.mIsLocationPrefetchEnabled) {
                    printWriter.print("  mLocating=");
                    printWriter.print(this.mLocating);
                    printWriter.print(" mHasGps=");
                    printWriter.print(this.mHasGps);
                    printWriter.print(" mHasFused=");
                    printWriter.print(this.mHasFusedLocation);
                    printWriter.print(" mLocated=");
                    printWriter.println(this.mLocated);
                    if (this.mLastGenericLocation != null) {
                        printWriter.print("  mLastGenericLocation=");
                        printWriter.println(this.mLastGenericLocation);
                    }
                    if (this.mLastGpsLocation != null) {
                        printWriter.print("  mLastGpsLocation=");
                        printWriter.println(this.mLastGpsLocation);
                    }
                } else {
                    printWriter.println("  Location prefetching disabled");
                }
                printWriter.print("  mState=");
                printWriter.print(stateToString(this.mState));
                printWriter.print(" mLightState=");
                printWriter.println(lightStateToString(this.mLightState));
                printWriter.print("  mInactiveTimeout=");
                TimeUtils.formatDuration(this.mInactiveTimeout, printWriter);
                printWriter.println();
                if (this.mActiveIdleOpCount != 0) {
                    printWriter.print("  mActiveIdleOpCount=");
                    printWriter.println(this.mActiveIdleOpCount);
                }
                if (this.mNextAlarmTime != 0) {
                    printWriter.print("  mNextAlarmTime=");
                    TimeUtils.formatDuration(this.mNextAlarmTime, SystemClock.elapsedRealtime(), printWriter);
                    printWriter.println();
                }
                if (this.mNextIdlePendingDelay != 0) {
                    printWriter.print("  mNextIdlePendingDelay=");
                    TimeUtils.formatDuration(this.mNextIdlePendingDelay, printWriter);
                    printWriter.println();
                }
                if (this.mNextIdleDelay != 0) {
                    printWriter.print("  mNextIdleDelay=");
                    TimeUtils.formatDuration(this.mNextIdleDelay, printWriter);
                    printWriter.println();
                }
                if (this.mNextLightIdleDelay != 0) {
                    printWriter.print("  mNextLightIdleDelay=");
                    TimeUtils.formatDuration(this.mNextLightIdleDelay, printWriter);
                    if (this.mConstants.USE_WINDOW_ALARMS) {
                        printWriter.print(" (flex=");
                        TimeUtils.formatDuration(this.mNextLightIdleDelayFlex, printWriter);
                        printWriter.println(")");
                    } else {
                        printWriter.println();
                    }
                }
                if (this.mNextLightAlarmTime != 0) {
                    printWriter.print("  mNextLightAlarmTime=");
                    TimeUtils.formatDuration(this.mNextLightAlarmTime, SystemClock.elapsedRealtime(), printWriter);
                    printWriter.println();
                }
                if (this.mCurLightIdleBudget != 0) {
                    printWriter.print("  mCurLightIdleBudget=");
                    TimeUtils.formatDuration(this.mCurLightIdleBudget, printWriter);
                    printWriter.println();
                }
                if (this.mMaintenanceStartTime != 0) {
                    printWriter.print("  mMaintenanceStartTime=");
                    TimeUtils.formatDuration(this.mMaintenanceStartTime, SystemClock.elapsedRealtime(), printWriter);
                    printWriter.println();
                }
                if (this.mJobsActive) {
                    printWriter.print("  mJobsActive=");
                    printWriter.println(this.mJobsActive);
                }
                if (this.mAlarmsActive) {
                    printWriter.print("  mAlarmsActive=");
                    printWriter.println(this.mAlarmsActive);
                }
                if (Math.abs(this.mPreIdleFactor - 1.0f) > MIN_PRE_IDLE_FACTOR_CHANGE) {
                    printWriter.print("  mPreIdleFactor=");
                    printWriter.println(this.mPreIdleFactor);
                }
            }
        }
    }

    void dumpTempWhitelistSchedule(PrintWriter printWriter, boolean z) {
        String str;
        int size = this.mTempWhitelistAppIdEndTimes.size();
        if (size > 0) {
            if (z) {
                printWriter.println("  Temp whitelist schedule:");
                str = "    ";
            } else {
                str = "";
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            for (int i = 0; i < size; i++) {
                printWriter.print(str);
                printWriter.print("UID=");
                printWriter.print(this.mTempWhitelistAppIdEndTimes.keyAt(i));
                printWriter.print(": ");
                Pair<MutableLong, String> valueAt = this.mTempWhitelistAppIdEndTimes.valueAt(i);
                TimeUtils.formatDuration(((MutableLong) valueAt.first).value, elapsedRealtime, printWriter);
                printWriter.print(" - ");
                printWriter.println((String) valueAt.second);
            }
        }
    }

    public IDeviceIdleControllerWrapper getWrapper() {
        return this.mDeviceIdleControllerWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class DeviceIdleControllerWrapper implements IDeviceIdleControllerWrapper {
        private DeviceIdleControllerWrapper() {
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public int addPowerSaveWhitelistAppsInternal(List<String> list) {
            return DeviceIdleController.this.addPowerSaveWhitelistAppsInternal(list);
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public int getState() {
            return DeviceIdleController.this.mState;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public void setState(int i) {
            DeviceIdleController.this.mState = i;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public ArrayMap<String, Integer> getPowerSaveWhitelistUserApps() {
            return DeviceIdleController.this.mPowerSaveWhitelistUserApps;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public boolean getDeepEnabled() {
            return DeviceIdleController.this.mDeepEnabled;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public void setDeepEnabled(boolean z) {
            DeviceIdleController.this.mDeepEnabled = z;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public boolean getLightEnabled() {
            return DeviceIdleController.this.mLightEnabled;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public void setLightEnabled(boolean z) {
            DeviceIdleController.this.mLightEnabled = z;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public void setActiveReason(int i) {
            DeviceIdleController.this.mActiveReason = i;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public void addPowerSaveWhitelistApps(ArrayMap<String, Integer> arrayMap) {
            if (arrayMap == null) {
                return;
            }
            synchronized (DeviceIdleController.this) {
                DeviceIdleController.this.mPowerSaveWhitelistAppsExceptIdle.putAll((ArrayMap) arrayMap);
                DeviceIdleController.this.mPowerSaveWhitelistApps.putAll((ArrayMap) arrayMap);
                DeviceIdleController.this.reportPowerSaveWhitelistChangedLocked();
                DeviceIdleController.this.updateWhitelistAppIdsLocked();
            }
        }
    }
}
