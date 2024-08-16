package com.oplus.thermalcontrol;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.OplusActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.OplusBatteryManager;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.OplusOSTelephonyManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.SparseIntArray;
import b6.LocalLog;
import com.oplus.app.OplusAppInfo;
import com.oplus.app.OplusSplitScreenObserver;
import com.oplus.athena.interaction.PackageStateInfo;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.osense.OsenseResClient;
import com.oplus.osense.info.OsenseNotifyRequest;
import com.oplus.screenmode.IOplusScreenMode;
import com.oplus.splitscreen.OplusSplitScreenManager;
import com.oplus.thermalcontrol.config.UahConstants;
import com.oplus.thermalcontrol.config.feature.CpuLevelConfig;
import com.oplus.thermalcontrol.config.feature.GpuLevelConfig;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import com.oplus.uah.UAHResClient;
import com.oplus.uah.info.UAHResourceInfo;
import com.oplus.uah.info.UAHRuleCtrlRequest;
import d9.AppSettings;
import ea.StateManager;
import f6.CommonUtil;
import f6.d;
import f6.f;
import ia.ThermalCustomizeChargeUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.json.JSONObject;
import s6.ThermalFactory;
import s6.ThermalManager;
import v4.GuardElfContext;
import y5.AppFeature;

/* loaded from: classes2.dex */
public class ThermalControlUtils {
    private static final String ACTION = "oplus.intent.action.BIND_STATE_SERVICE";
    private static final int ACTION_SCENE_IN = 1;
    private static final int ACTION_SCENE_OUT = 0;
    private static final int ACTION_SCENE_UPDATE = 2;
    private static final String AGING_PERSIST_PROP = "persist.sys.oplus.eng.full.aging";
    public static final int AMBIENT_STATE_HIGH_TEMP = 3;
    public static final int AMBIENT_STATE_LOW_TEMP = 1;
    public static final int AMBIENT_STATE_MAX_EXTEND = 5;
    public static final int AMBIENT_STATE_NORMAL_TEMP = 2;
    private static final String ATHENA_CLASS_NAME = "com.oplus.athena.state.StateService";
    private static final String ATHENA_EXTRA_CALLER_PACKAGE = "caller_package";
    private static final String ATHENA_EXTRA_LEVEL = "level";
    private static final String ATHENA_PACKAGE_NAME = "com.oplus.athena";
    private static final String COSA_MODE_HIGHPERF = "2";
    private static final String COSA_MODE_NORMAL = "0";
    private static final String COSA_MODE_POWERSAVE = "1";
    private static final String COSA_MODE_RM_XMODE = "3";
    private static final float CUSTOMIZE_CHARGE_TEMP_BOTTOM_LIMIT = 33.0f;
    private static final float CUSTOMIZE_CHARGE_TEMP_HIGH_LIMIT = 40.0f;
    private static final String DEFAULT_SCENE_LISTEN_LIST = "101,102,103,104,105,106,107";
    public static final int DISABLE = 0;
    private static final int DISABLE_TORCH = 5;
    private static final String DISPLAY_MANAGER_CLASSNAME = "android.hardware.display.OplusDisplayManager";
    private static final String DISPLAY_MANAGER_SET_BRIGHTNESS = "setSpecBrightness";
    public static final int ENABLE = 1;
    private static final int ENABLE_TORCH = 6;
    private static final String EXTRA_KEY_BACKUP_RESTORE_HIGH_PERFORMANCE = "phone_clone_high_performance";
    private static final String EXTRA_KEY_BACKUP_RESTORE_STATE = "changeover_status";
    private static final String EXTRA_KEY_COSA_GAME_MODE = "cosa_gameMode";
    private static final String EXTRA_KEY_COSA_REFRESH_SET = "cosa_refreshSet";
    private static final String EXTRA_SCENE = "Scene";
    private static final String EXTRA_TEMP = "Temp";
    private static final String EXTRA_THERMAL = "ThermalLevel";
    private static final String EXTRA_WIFI_SPEED = "WifiSpeed";
    private static final String FPS_DESCRIPTION = "com.oplus.battery";
    private static final String FPS_INTERFACE = "com.oplus.vrr.IOPlusRefreshRate";
    public static final int GAME_HIGHPERF_MODE = 6;
    public static final int GAME_NORMAL_MODE = 7;
    public static final int GAME_POWERSAVE_MODE = 5;
    private static final int GET_AMBIENT_TEMP = 25;
    public static final int HIGH_PREF_MODE = 1;
    private static final int HIGH_TEMPERATURE_PROTECT_LEVEL = 5;
    private static final String HORAE_SERVICE_INTERFACE = "com.oplus.horae.IHoraeService";
    private static final String INTENT_OPLUS_THERMAL_LEVEL_CHANGE = "oplus.intent.action.THERMAL_LEVEL_CHANGE";
    private static final String INTENT_REQUEST_APP_CONTROL = "oplus.intent.action.REQUEST_APP_CLEAN_RUNNING";
    private static final String INTENT_THERMAL_FRAME_CONTROL = "oplus.intent.action.THERMAL_THROTTLING_PW";
    private static final String INTENT_THERMAL_FRAME_CONTROL_EXTRA = "ThermalThrottling";
    public static final int IN_NAVIGATION_VALUE = 16;
    private static final String LAUNCHER_PACKAGE_NAME = "com.android.launcher";
    private static final int LEVEL_0 = 0;
    private static final int LEVEL_1 = 1;
    private static final int LEVEL_2 = 2;
    private static final int LEVEL_3 = 3;
    private static final int LEVEL_4 = 4;
    private static final int LEVEL_5 = 5;
    private static final int LEVEL_6 = 6;
    private static final int LEVEL_7 = 7;
    private static final int MAX_BRIGHTNESS = 255;
    private static final int MAX_REGISTER_TIME = 5;
    private static final int MODE_SMART_CHARGE_MODE = 3;
    private static final int MSG_DELAY_TIME = 3000;
    private static final int MSG_MAX_DELAY_TIME = 60000;
    private static final int MSG_REBIND = 101;
    private static final int MTK = 2;
    public static final int NORMAL_MODE = 2;
    public static final int NUM_15 = 15;
    private static final int ONE_SECOND = 1000;
    private static final String OPLUS_CAMERA_BRIGHTNESS = "oplus.camera.brightness";
    private static final String OPLUS_CAMERA_EXIT_STATE = "oplus.camera.exit";
    private static final String OPLUS_CAMERA_FLASH_STATE = "oplus.camera.flash";
    private static final String OPLUS_CAMERA_RECORDING_STATE = "oplus_camera_3rd_activity";
    private static final String OPLUS_CAMERA_TORCH = "oplus.camera.torch";
    private static final String OPLUS_CAMERA_VIDEO_STATE = "oplus.camera.video";
    private static final String OPLUS_FLOAT_WINDOW_SHOW_STATE = "oplus_float_window_show";
    private static final String OPLUS_HBM_CONTROL_STATE = "customize_power_temperature_control_hbm";
    private static final String OPLUS_OSIE_CONTROL_STATE = "customize_power_temperature_control_osie";
    private static final String OPLUS_SCREENMODE_SERVICE_NAME = "oplusscreenmode";
    private static final String OPLUS_VIDEOSR_CONTROL_STATE = "customize_power_temperature_control_videosr";
    private static final String PACKAGE_BATTERY_THERMALCONTROL = "com.oplus.battery.thermalcontrol";
    private static final String PACKAGE_OPLUS_EXSERVICEUI = "com.oplus.exserviceui";
    public static final int PLAYING_MUSIC_VALUE = 7;
    public static final int POWERSAVE_MODE = 3;
    private static final int QUALCOMM = 1;
    public static final int RACING_MODE = 0;
    private static final int REFRESH_RATE_30 = 5;
    private static final int REFRESH_RATE_60 = 2;
    private static final int REFRESH_RATE_90 = 1;
    private static final String REFRESH_RATE_INTERFACE = "com.oplus.screenmode.IOplusScreenMode";
    private static final int REFRESH_RATE_RESET = 0;
    public static final int SCENE_TYPES_OFFSET = 100;
    public static final int SCENE_TYPE_AI_NAVIGATION = 106;
    public static final int SCENE_TYPE_AUDIO_CALL = 102;
    public static final int SCENE_TYPE_GAME = 104;
    public static final int SCENE_TYPE_MUSIC_PLAY = 107;
    public static final int SCENE_TYPE_VIDEO = 101;
    public static final int SCENE_TYPE_VIDEO_CALL = 103;
    public static final int SCENE_TYPE_VIDEO_LIVE = 105;
    private static final String SECONDARY_HOME_PACKAGE_NAME = "com.oplus.secondaryhome";
    private static final int SET_FPS_TRANSACTION_NUM = 20;
    private static final int SET_REFRESH_RETE_TRANSACTION_NUM = 6;
    private static final int STATE_SCREEN_OFF = 0;
    private static final int STATE_SCREEN_ON = 1;
    private static final int STATE_SCREEN_ON_BACKUP_RESTORE = 4;
    private static final int STATE_SCREEN_ON_INCALL = 2;
    public static final int SUPERPOWERSAVE_MODE = 4;
    private static final String SYSTEM_FOLDING_MODE_KEYS = "oplus_system_folding_mode";
    public static final String TAG = "ThermalControlUtils";
    public static final int TEMPERATURE_FOR_AGING = 63;
    public static final int THERMAL_CONTROL_COMMAND = 4;
    private static final String THERMAL_CONTROL_OPTIMIZATION_NUMBER = "oplus_customize_thermal_control_optimization_number";
    private static final String THERMAL_CPU_HEAT_CONTRIBUTION = "cpuheatcontribution";
    private static final String THERMAL_CURRENT_TEMPERATURE = "currenttemperature";
    private static final String THERMAL_LEVEL = "thermallevel";
    private static final String THERMAL_TRACE_INFO_MONITOR_STATE = "thermal_trace_info_monitor_state";
    public static final String UAH_CLIENT_IS_NULL = "UAHClient is null";
    private static final int USER_FOREGROUND = 1;
    private static final String VALUE_PHONE_CLONE_BACKUP = "2";
    private static final String VALUE_PHONE_CLONE_CONNECTING = "3";
    private static final String VALUE_PHONE_CLONE_RESTORE = "1";
    private static final String VALUE_PHONE_CLONE_TRANSMISSION_COMPLETE = "4";
    public static final int VISIBLE_APPS_VALUE = 1;
    private static volatile ThermalControlUtils sThermalControlUtils;
    private final ContentResolver mContentResolver;
    private Context mContext;
    private ContentObserver mHighTempSafetyStateObserver;
    private StateManager mStateManager;
    private ThermalManager mThermalManager;
    private ContentObserver mThermalOptimizationObserver;
    private AudioManager mAudioManager = null;
    private LocationManager mLocationManager = null;
    private OplusBatteryManager mOplusBatteryManager = null;
    private IBinder mHorae = null;
    private IBinder mRemoteHorae = null;
    private IBinder mDisplayBinder = null;
    private IBinder mVVRBinder = null;
    private IBinder mScreenModeBinder = null;
    private int mEnvironmentTemperatureType = 0;
    private int mAmbientTemperature = 0;
    private int mAmbientTempState = 2;
    private int mGameUserMode = -1;
    private String mLastForegroundPkg = LAUNCHER_PACKAGE_NAME;
    private String mSplitForegroundPkg = LAUNCHER_PACKAGE_NAME;
    private String mLastForegroundActivity = "com.android.launcher.Launcher";
    private int mRestrictLevel = -1;
    private int m5GStatus = -1;
    private int mWifiSpeed = -1;
    private boolean mIsPowerSaveMode = false;
    private boolean mIsSuperPowerSaveMode = false;
    private boolean mIsHighPrefMode = false;
    private boolean mIsBenchMode = false;
    private boolean mIsScreenOn = true;
    private boolean mIsCharging = false;
    private boolean mIsBackupRestore = false;
    private boolean mIsBackupRestoreScene = false;
    private boolean mScreenBlockPhoneCall = false;
    private boolean mIsBackupHighPerformance = false;
    private boolean mIsScreenBlocked = false;
    private boolean mIsSpeedCharging = false;
    private int mCountRegister = 0;
    private int mChargeLevel = -1;
    private int mPolicyChargeLevel = -1;
    private int mSpeedChargeAdd = -1;
    private int mCurrentRefreshRate = -1;
    private int mCurrentBrightness = -1;
    private int mThermalStatusFromBroadcast = -1;
    private int mDeviceProvisioned = -1;
    private int mSystemFoldingMode = -1;
    private int mTraceMonitorState = -1;
    private int mFloatWindowState = -1;
    private int mRecordingState = -1;
    private int mHighTempSafetyState = -1;
    private int mThermalOptimizationNum = 0;
    private float mTemperatureFromBroadcast = 0.0f;
    private boolean mIsUserForeground = true;
    private boolean mEnterSplitMode = false;
    private String mSceneListenList = DEFAULT_SCENE_LISTEN_LIST;
    private String mRestrictLevelList = "3";
    private int mPolicyCurrentScene = -1;
    private int mCameraVideoStopState = -1;
    private boolean mAlreadyCameraScreenBrightness = false;
    private boolean mAlreadyCameraExitState = false;
    private boolean mAlreadyCameraFlashDisableState = false;
    private boolean mAlreadyDisHotSpot = false;
    private boolean mAlreadyHighTempSafety = true;
    private boolean mIsGameXmode = false;
    private SplitScreenObserver mSplitScreenObserver = new SplitScreenObserver();
    private UAHResClient mUAHClient = UAHResClient.get(ThermalControlUtils.class);
    private ContentObserver mBackupRestoreObserver = new ContentObserver(0 == true ? 1 : 0) { // from class: com.oplus.thermalcontrol.ThermalControlUtils.4
        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            String string = Settings.Secure.getString(ThermalControlUtils.this.mContext.getContentResolver(), ThermalControlUtils.EXTRA_KEY_BACKUP_RESTORE_STATE);
            if (string != null) {
                ThermalControlUtils.this.mIsBackupRestore = string.equals("1") || string.equals("2") || string.equals("3") || string.equals("4");
                ThermalControlUtils.this.mIsBackupRestoreScene = "4".equals(string);
            } else {
                ThermalControlUtils.this.mIsBackupRestore = false;
                ThermalControlUtils.this.mIsBackupRestoreScene = false;
            }
            ThermalControllerCenter.getInstance(ThermalControlUtils.this.mContext).sendBackupRestoreChangedMessage();
            if (ThermalControlUtils.this.isBackupRestore() || ThermalControlUtils.this.mIsScreenOn) {
                return;
            }
            ThermalControlUtils.this.sendScreenStatus(false);
        }
    };
    private ContentObserver mBackupRestoreHighPerformanceObserver = new ContentObserver(0 == true ? 1 : 0) { // from class: com.oplus.thermalcontrol.ThermalControlUtils.5
        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            if (Settings.Secure.getInt(ThermalControlUtils.this.mContext.getContentResolver(), ThermalControlUtils.EXTRA_KEY_BACKUP_RESTORE_HIGH_PERFORMANCE, 0) == 1) {
                ThermalControlUtils.this.mIsBackupHighPerformance = true;
            } else {
                ThermalControlUtils.this.mIsBackupHighPerformance = false;
            }
            if (ThermalControlUtils.this.mIsBackupRestore) {
                ThermalControllerCenter.getInstance(ThermalControlUtils.this.mContext).sendBackupHighPerformanceChangedMessage();
            }
        }
    };
    private ContentObserver mDeviceProvisionedObserver = new ContentObserver(0 == true ? 1 : 0) { // from class: com.oplus.thermalcontrol.ThermalControlUtils.6
        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            ThermalControlUtils thermalControlUtils = ThermalControlUtils.this;
            thermalControlUtils.mDeviceProvisioned = Settings.Global.getInt(thermalControlUtils.mContext.getContentResolver(), "device_provisioned", 0);
        }
    };
    private ContentObserver mCosaGameModeObserver = new ContentObserver(0 == true ? 1 : 0) { // from class: com.oplus.thermalcontrol.ThermalControlUtils.7
        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            ThermalControlUtils.this.onCosaGameModeChanged();
        }
    };
    private ContentObserver mCosaRefreshSetObserver = new ContentObserver(0 == true ? 1 : 0) { // from class: com.oplus.thermalcontrol.ThermalControlUtils.8
        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            ThermalControlUtils.this.onCosaRefreshSetChanged();
        }
    };
    private ContentObserver mTraceMonitorStateObserver = new ContentObserver(0 == true ? 1 : 0) { // from class: com.oplus.thermalcontrol.ThermalControlUtils.9
        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            ThermalControlUtils thermalControlUtils = ThermalControlUtils.this;
            thermalControlUtils.mTraceMonitorState = AppSettings.e.b(thermalControlUtils.mContext.getContentResolver(), ThermalControlUtils.THERMAL_TRACE_INFO_MONITOR_STATE, -1);
            if (LocalLog.f()) {
                LocalLog.a(ThermalControlUtils.TAG, "mTraceMonitorState=" + ThermalControlUtils.this.mTraceMonitorState);
            }
        }
    };
    private ContentObserver mFloatWindowStateObserver = new ContentObserver(0 == true ? 1 : 0) { // from class: com.oplus.thermalcontrol.ThermalControlUtils.10
        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            ThermalControlUtils thermalControlUtils = ThermalControlUtils.this;
            thermalControlUtils.mFloatWindowState = Settings.System.getInt(thermalControlUtils.mContext.getContentResolver(), ThermalControlUtils.OPLUS_FLOAT_WINDOW_SHOW_STATE, -1);
            if (LocalLog.f()) {
                LocalLog.a(ThermalControlUtils.TAG, "mFloatWindowState=" + ThermalControlUtils.this.mFloatWindowState);
            }
            ThermalControllerCenter.getInstance(ThermalControlUtils.this.mContext).sendFloatWindowChangeMessage();
        }
    };
    private ContentObserver mRecordingStateObserver = new ContentObserver(0 == true ? 1 : 0) { // from class: com.oplus.thermalcontrol.ThermalControlUtils.11
        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            ThermalControlUtils thermalControlUtils = ThermalControlUtils.this;
            thermalControlUtils.mRecordingState = Settings.System.getInt(thermalControlUtils.mContext.getContentResolver(), ThermalControlUtils.OPLUS_CAMERA_RECORDING_STATE, -1);
            LocalLog.l(ThermalControlUtils.TAG, "RecordingState=" + ThermalControlUtils.this.mRecordingState);
            ThermalControllerCenter.getInstance(ThermalControlUtils.this.mContext).sendSceneChangeMessage();
        }
    };
    private ContentObserver mSystemFoldingModeObserver = new ContentObserver(0 == true ? 1 : 0) { // from class: com.oplus.thermalcontrol.ThermalControlUtils.12
        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            ThermalControlUtils thermalControlUtils = ThermalControlUtils.this;
            thermalControlUtils.mSystemFoldingMode = Settings.Global.getInt(thermalControlUtils.mContext.getContentResolver(), ThermalControlUtils.SYSTEM_FOLDING_MODE_KEYS, -1);
            ThermalControlUtils thermalControlUtils2 = ThermalControlUtils.this;
            thermalControlUtils2.sendSystemFoldingMode(thermalControlUtils2.mSystemFoldingMode);
        }
    };
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.thermalcontrol.ThermalControlUtils.13
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            ThermalControlUtils.this.mHorae = null;
            LocalLog.a(ThermalControlUtils.TAG, "horae died");
            ThermalStatusListener.destroyListener();
            ThermalControlUtils thermalControlUtils = ThermalControlUtils.this;
            thermalControlUtils.registerThermalBinder(ThermalStatusListener.getInstance(thermalControlUtils.mContext));
        }
    };
    private Handler mainHandler = new Handler(Looper.getMainLooper()) { // from class: com.oplus.thermalcontrol.ThermalControlUtils.14
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 101) {
                ThermalControlUtils thermalControlUtils = ThermalControlUtils.this;
                thermalControlUtils.registerThermalBinder(ThermalStatusListener.getInstance(thermalControlUtils.mContext));
            }
        }
    };

    /* loaded from: classes2.dex */
    private class SplitScreenObserver extends OplusSplitScreenObserver {
        private SplitScreenObserver() {
        }

        public void onStateChanged(String str, Bundle bundle) {
            if (!"splitScreenModeChange".equals(str) || bundle == null) {
                return;
            }
            ThermalControlUtils.this.mEnterSplitMode = bundle.getBoolean("isInSplitScreenMode", false);
            if (ThermalControlUtils.this.isFoldingMode() && ThermalControlUtils.this.mEnterSplitMode) {
                if (!ThermalControlUtils.LAUNCHER_PACKAGE_NAME.equals(ThermalControlUtils.this.mLastForegroundPkg)) {
                    ThermalControlUtils thermalControlUtils = ThermalControlUtils.this;
                    thermalControlUtils.mSplitForegroundPkg = thermalControlUtils.mLastForegroundPkg;
                }
                ThermalControlUtils.this.mLastForegroundPkg = ThermalControlUtils.LAUNCHER_PACKAGE_NAME;
            }
            if (LocalLog.f()) {
                LocalLog.a(ThermalControlUtils.TAG, "SplitScreenObserver: onStateChanged = " + ThermalControlUtils.this.mEnterSplitMode + ",mLastForegroundPkg=" + ThermalControlUtils.this.mLastForegroundPkg + ",mSplitForegroundPkg=" + ThermalControlUtils.this.mSplitForegroundPkg);
            }
            ThermalControllerCenter.getInstance(ThermalControlUtils.this.mContext).sendSplitModeMessage();
        }
    }

    /* loaded from: classes2.dex */
    public static class WindowInfo {
        public Bundle bundle;
        public List<Integer> scene;
        public int uid = 0;
        public int userId = 0;
        public int type = 0;
        public int mode = 0;
        public String pkg = "";
        public String activityName = "";
        public int taskId = -1;

        public static WindowInfo createFromBundle(Bundle bundle) {
            WindowInfo windowInfo = new WindowInfo();
            windowInfo.uid = bundle.getInt(TriggerEvent.EXTRA_UID);
            windowInfo.userId = bundle.getInt("userId");
            windowInfo.type = bundle.getInt("type");
            windowInfo.mode = bundle.getInt("mode");
            String string = bundle.getString("pkgName");
            windowInfo.pkg = string;
            if (string == null) {
                windowInfo.pkg = "";
            }
            String string2 = bundle.getString("activityName");
            windowInfo.activityName = string2;
            if (string2 == null) {
                windowInfo.activityName = "";
            }
            windowInfo.taskId = bundle.getInt("taskId");
            windowInfo.bundle = bundle;
            return windowInfo;
        }

        public String toString() {
            return "uid: " + this.uid + ", userId: " + this.userId + ", type: " + this.type + ", mode: " + this.mode + ", pkg: " + this.pkg + ", activityName: " + this.activityName + ", taskId: " + this.taskId + ", scene: " + this.scene;
        }
    }

    private ThermalControlUtils(Context context) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mStateManager = StateManager.f(context);
        new Thread(new Runnable() { // from class: com.oplus.thermalcontrol.ThermalControlUtils.1
            @Override // java.lang.Runnable
            public void run() {
                ThermalControlUtils.this.initData();
            }
        }).start();
    }

    private boolean binderThermal(ThermalStatusListener thermalStatusListener) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        if (this.mHorae == null) {
            connectHorae();
        }
        boolean z10 = false;
        try {
            if (this.mHorae != null) {
                obtain.writeInterfaceToken(HORAE_SERVICE_INTERFACE);
                obtain.writeStrongBinder(thermalStatusListener.asBinder());
                this.mHorae.transact(1, obtain, obtain2, 0);
                LocalLog.a(TAG, "write binder");
                z10 = true;
            }
            return z10;
        } catch (RemoteException e10) {
            LocalLog.b(TAG, "registerThermalBinder " + e10.toString());
            return false;
        } finally {
            obtain.recycle();
            obtain2.recycle();
        }
    }

    private synchronized IBinder connectHorae() {
        IBinder checkService = ServiceManager.checkService("horae");
        this.mHorae = checkService;
        if (checkService != null) {
            try {
                checkService.linkToDeath(this.mDeathRecipient, 0);
                LocalLog.a(TAG, "connect horae");
            } catch (RemoteException unused) {
                this.mHorae = null;
            }
        }
        return this.mHorae;
    }

    public static ThermalControlUtils getInstance(Context context) {
        if (sThermalControlUtils == null) {
            synchronized (ThermalControlUtils.class) {
                if (sThermalControlUtils == null) {
                    sThermalControlUtils = new ThermalControlUtils(context);
                }
            }
        }
        return sThermalControlUtils;
    }

    private boolean getIsScreenBlocked() {
        boolean isKeyguardLocked = ((KeyguardManager) this.mContext.getSystemService("keyguard")).isKeyguardLocked();
        boolean isInteractive = GuardElfContext.e().h().isInteractive();
        if (LocalLog.f()) {
            LocalLog.a(TAG, "lockflag " + isKeyguardLocked + ", screenflag " + isInteractive);
        }
        return isInteractive && isKeyguardLocked;
    }

    private void getSplitTopAppInfo() {
        List<OplusAppInfo> list;
        try {
            list = new OplusActivityManager().getAllTopAppInfos();
        } catch (RemoteException e10) {
            LocalLog.l(TAG, "getAllTopAppInfo,e=" + e10.getMessage());
            list = null;
        }
        if (list != null) {
            for (OplusAppInfo oplusAppInfo : list) {
                int i10 = oplusAppInfo.windowingMode;
                if (i10 == 3) {
                    this.mLastForegroundPkg = oplusAppInfo.appInfo.packageName;
                } else if (i10 == 4) {
                    this.mSplitForegroundPkg = oplusAppInfo.appInfo.packageName;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initData() {
        this.mOplusBatteryManager = new OplusBatteryManager();
        connectHorae();
        this.mIsScreenOn = GuardElfContext.e().h().isInteractive();
        int intProperty = GuardElfContext.e().d().getIntProperty(6);
        this.mIsCharging = intProperty == 2 || intProperty == 5;
        String string = Settings.Secure.getString(this.mContext.getContentResolver(), EXTRA_KEY_BACKUP_RESTORE_STATE);
        if (string != null) {
            this.mIsBackupRestore = string.equals("1") || string.equals("2") || string.equals("3") || string.equals("4");
            this.mIsBackupRestoreScene = "4".equals(string);
        } else {
            this.mIsBackupRestore = false;
            this.mIsBackupRestoreScene = false;
        }
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), EXTRA_KEY_BACKUP_RESTORE_HIGH_PERFORMANCE, 0) == 1) {
            this.mIsBackupHighPerformance = true;
        } else {
            this.mIsBackupHighPerformance = false;
        }
        String b10 = AppSettings.d.b(this.mContext.getContentResolver(), EXTRA_KEY_COSA_GAME_MODE);
        LocalLog.l(TAG, "cosaGameMode:" + b10);
        if (b10 != null) {
            if (b10.equals("0")) {
                this.mGameUserMode = 7;
            } else if (b10.equals("1")) {
                this.mGameUserMode = 5;
            } else if (b10.equals("2")) {
                this.mGameUserMode = 6;
            } else {
                this.mGameUserMode = -1;
            }
        } else {
            this.mGameUserMode = -1;
        }
        this.mDeviceProvisioned = Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0);
        this.mSystemFoldingMode = Settings.Global.getInt(this.mContext.getContentResolver(), SYSTEM_FOLDING_MODE_KEYS, -1);
        this.mEnterSplitMode = OplusSplitScreenManager.getInstance().isInSplitScreenMode();
        this.mTraceMonitorState = AppSettings.e.b(this.mContext.getContentResolver(), THERMAL_TRACE_INFO_MONITOR_STATE, -1);
        this.mFloatWindowState = Settings.System.getInt(this.mContext.getContentResolver(), OPLUS_FLOAT_WINDOW_SHOW_STATE, -1);
        this.mRecordingState = Settings.System.getInt(this.mContext.getContentResolver(), OPLUS_CAMERA_RECORDING_STATE, -1);
        this.mHighTempSafetyState = Settings.System.getInt(this.mContext.getContentResolver(), "oplus_settings_hightemp_safety_state", -1);
        this.mAmbientTemperature = getAmbientTempFromBind();
        this.mIsScreenBlocked = getIsScreenBlocked();
        this.mLastForegroundPkg = CommonUtil.y();
        this.mIsSuperPowerSaveMode = f.a1(this.mContext);
        this.mIsPowerSaveMode = f.f0(this.mContext);
        this.mIsHighPrefMode = CommonUtil.A() == 1;
        this.mThermalOptimizationNum = Settings.System.getIntForUser(this.mContext.getContentResolver(), THERMAL_CONTROL_OPTIMIZATION_NUMBER, 0, -2);
        if (ActivityManager.getCurrentUser() != this.mContext.getUserId()) {
            this.mIsUserForeground = false;
        }
    }

    private static boolean isBasedOnMtk(Context context) {
        return context.getPackageManager().hasSystemFeature("oplus.hw.manufacturer.mtk");
    }

    private static boolean isBasedOnQcom(Context context) {
        return context.getPackageManager().hasSystemFeature("oplus.hw.manufacturer.qualcomm");
    }

    private boolean isDaydreamvideo() {
        String str = this.mLastForegroundPkg;
        if (str != null) {
            return str.equals("com.oppo.daydreamvideo");
        }
        return false;
    }

    private static boolean isMtkGeminiSupport(Context context) {
        return context.getPackageManager().hasSystemFeature("mtk.gemini.support");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCosaGameModeChanged() {
        String b10 = AppSettings.d.b(this.mContext.getContentResolver(), EXTRA_KEY_COSA_GAME_MODE);
        LocalLog.l(TAG, "cosaGameMode:" + b10);
        if (b10 != null) {
            if (b10.equals("0")) {
                this.mGameUserMode = 7;
            } else if (b10.equals("1")) {
                this.mGameUserMode = 5;
            } else if (b10.equals("2")) {
                this.mGameUserMode = 6;
            } else {
                this.mGameUserMode = -1;
            }
            if (y5.b.y()) {
                if (!this.mIsGameXmode && b10.equals("3")) {
                    this.mIsGameXmode = true;
                    ThermalControllerCenter.getInstance(this.mContext).reloadGTstate(true);
                    return;
                } else if (this.mIsGameXmode && b10.equals("2")) {
                    this.mIsGameXmode = false;
                    return;
                } else if (this.mIsGameXmode) {
                    this.mIsGameXmode = false;
                    ThermalControllerCenter.getInstance(this.mContext).reloadGTstate(false);
                    return;
                }
            }
            ThermalControllerCenter.getInstance(this.mContext).sendCosaGameModeChangedMessage();
            return;
        }
        this.mGameUserMode = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCosaRefreshSetChanged() {
        String b10 = AppSettings.d.b(this.mContext.getContentResolver(), EXTRA_KEY_COSA_REFRESH_SET);
        LocalLog.l(TAG, "cosaRefreshSet:" + b10);
        if (b10 == null || !b10.equals("reset")) {
            return;
        }
        ThermalControllerCenter.getInstance(this.mContext).sendCosaRefreshSetChangedMessage();
    }

    private void registerThermalOptimizationObserver() {
        this.mThermalOptimizationObserver = new ContentObserver(null) { // from class: com.oplus.thermalcontrol.ThermalControlUtils.3
            @Override // android.database.ContentObserver
            public void onChange(boolean z10) {
                if (Settings.System.getIntForUser(ThermalControlUtils.this.mContext.getContentResolver(), ThermalControlUtils.THERMAL_CONTROL_OPTIMIZATION_NUMBER, -1, -2) == 0) {
                    LocalLog.l(ThermalControlUtils.TAG, "Thermal Optimization Num clear.");
                    ThermalControlUtils.this.mThermalOptimizationNum = 0;
                }
            }
        };
        this.mContentResolver.registerContentObserver(Settings.System.getUriFor(THERMAL_CONTROL_OPTIMIZATION_NUMBER), false, this.mThermalOptimizationObserver, -2);
    }

    public static List<Bundle> requestVisibleWindows() {
        try {
            Class<?> cls = Class.forName("android.view.OplusWindowManager");
            Object invoke = cls.getDeclaredMethod("requestVisibleWindows", new Class[0]).invoke(cls.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]), new Object[0]);
            if ((invoke instanceof List) && !((List) invoke).isEmpty() && (((List) invoke).get(0) instanceof Bundle)) {
                return (List) invoke;
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e10) {
            LocalLog.b(TAG, "requestVisibleWindows, e= " + e10);
        }
        return new ArrayList();
    }

    private void send5Gbraodcast(int i10, int i11, int i12, int i13, int i14, int i15, int i16) {
        Intent intent = new Intent();
        intent.setAction("oplus.intent.action.THERMAL_THROTTLING_5G");
        intent.putExtra(EXTRA_TEMP, String.valueOf(i10));
        intent.putExtra("Request", String.valueOf(i11));
        intent.putExtra("FallBack", String.valueOf(i12));
        intent.putExtra("DropCell", String.valueOf(i13));
        intent.putExtra("TxPower", String.valueOf(i14));
        intent.putExtra("Uplink", String.valueOf(i15));
        intent.putExtra("Downlink", String.valueOf(i16));
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
        if (LocalLog.f()) {
            LocalLog.a(TAG, "send broadcast");
        }
    }

    private void setRefreshRateData(int i10, int i11) {
        try {
            if (this.mScreenModeBinder == null) {
                this.mScreenModeBinder = ServiceManager.getService(OPLUS_SCREENMODE_SERVICE_NAME);
            }
            if (this.mScreenModeBinder != null) {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken(REFRESH_RATE_INTERFACE);
                obtain.writeInt(i10);
                obtain.writeInt(i11);
                this.mScreenModeBinder.transact(6, obtain, null, 1);
                obtain.recycle();
            }
        } catch (RemoteException e10) {
            LocalLog.b(TAG, "setFps error: " + e10.toString());
        }
    }

    private void setUserChargingLevel(int i10, String str) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "setUserChargingLevel " + i10 + " in user " + this.mContext.getUserId());
        }
        Intent intent = new Intent();
        intent.putExtra("name", "set_charge");
        intent.putExtra("level", i10);
        intent.putExtra("isScreenBlock", this.mIsScreenBlocked);
        intent.putExtra(ThermalPolicy.KEY_SPEED_CHARGE_ADD, 0);
        intent.putExtra("foregroundPkgName", str);
        sendUserSystemBroadcast(intent);
    }

    public void cancelChargingControl(String str) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "cancelChargingControl");
        }
        if (this.mOplusBatteryManager == null) {
            this.mOplusBatteryManager = new OplusBatteryManager();
        }
        if (this.mOplusBatteryManager != null) {
            this.mChargeLevel = 0;
            if (this.mContext.getUserId() != 0) {
                setUserChargingLevel(0, str);
            } else if (AppFeature.n()) {
                setSmartCoolDown(0, 0, str);
            } else {
                this.mOplusBatteryManager.setChargingLevel(String.valueOf(0), TAG);
            }
        }
    }

    public void disWifiHotSpot(boolean z10) {
        if (!this.mAlreadyDisHotSpot || z10) {
            if (z10) {
                this.mAlreadyDisHotSpot = false;
            } else {
                this.mAlreadyDisHotSpot = true;
            }
            if (LocalLog.f()) {
                LocalLog.a(TAG, "disWifiHotSpot:" + z10);
            }
            Intent intent = new Intent();
            intent.setAction("oplus.intent.action.ACTION_THERMAL_CONTROL_HIGHTEMP_FIRST");
            intent.setPackage(this.mContext.getPackageName());
            if (z10) {
                intent.putExtra("first_step", 1);
            } else {
                intent.putExtra("first_step", -1);
            }
            if (this.mContext.getUserId() == 0) {
                this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
            }
        }
    }

    public void disZoomWindow(WindowInfo windowInfo, int i10, int i11) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "closeZoomWindow: pkg:" + windowInfo.pkg + " userId:" + this.mContext.getUserId() + " policy:" + i10);
        }
        Intent intent = new Intent();
        intent.setAction("oplus.intent.action.ACTION_THERMAL_CONTROL_DIS_WINDOW");
        intent.setPackage(this.mContext.getPackageName());
        windowInfo.bundle.putInt("close_policy", i10);
        windowInfo.bundle.putString("foreground_pkg", this.mLastForegroundPkg);
        windowInfo.bundle.putString("tempLevel", String.valueOf(i11));
        windowInfo.bundle.putString("ambient_temperature", String.valueOf(this.mAmbientTemperature));
        intent.putExtras(windowInfo.bundle);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    public void disableFrameInsert(boolean z10) {
        Intent intent = new Intent(INTENT_THERMAL_FRAME_CONTROL);
        if (z10) {
            intent.putExtra(INTENT_THERMAL_FRAME_CONTROL_EXTRA, "1");
        } else {
            intent.putExtra(INTENT_THERMAL_FRAME_CONTROL_EXTRA, "0");
        }
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    public void doSceneChange(String str, int i10, int i11) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "CurrentForegroundPkg " + this.mLastForegroundPkg + ", pkgName " + str + ", mPolicyCurrentScene " + this.mPolicyCurrentScene + ", scene " + i10 + ", actionType " + i11);
        }
        String str2 = this.mSceneListenList;
        if (str2 != null && str2.length() != 0) {
            if (!this.mSceneListenList.contains(String.valueOf(i10))) {
                LocalLog.a(TAG, "scene " + i10 + " not in list " + this.mSceneListenList);
                return;
            }
            if (this.mLastForegroundPkg.equals(str) || (isFoldingMode() && isSplitMode() && this.mSplitForegroundPkg.equals(str))) {
                if (i11 != 1 && i11 != 2) {
                    if (i11 == 0 && i10 == this.mPolicyCurrentScene) {
                        ThermalControllerCenter.getInstance(this.mContext).sendSceneChangeMessage();
                        return;
                    }
                    return;
                }
                if (i10 != this.mPolicyCurrentScene) {
                    ThermalControllerCenter.getInstance(this.mContext).sendSceneChangeMessage();
                    return;
                }
                return;
            }
            return;
        }
        LocalLog.a(TAG, "scene list is null");
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00a5 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getAmbientTempFromBind() {
        int i10;
        if (this.mContext.getUserId() != 0) {
            if (LocalLog.f()) {
                LocalLog.a(TAG, "getAmbientTemperature " + this.mAmbientTemperature + " in user " + this.mContext.getUserId());
            }
            return this.mAmbientTemperature;
        }
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        if (this.mHorae == null) {
            connectHorae();
        }
        try {
            try {
                if (this.mHorae != null) {
                    obtain.writeInterfaceToken(HORAE_SERVICE_INTERFACE);
                    this.mHorae.transact(25, obtain, obtain2, 0);
                    i10 = obtain2.readInt();
                    try {
                        LocalLog.l(TAG, "get Ambient Temperature=" + i10);
                    } catch (RemoteException e10) {
                        e = e10;
                        LocalLog.b(TAG, "getAmbientTempFromBind " + e.toString());
                        if (i10 < 0) {
                        }
                    }
                } else {
                    i10 = 0;
                }
            } finally {
                obtain.recycle();
                obtain2.recycle();
            }
        } catch (RemoteException e11) {
            e = e11;
            i10 = 0;
        }
        if (i10 < 0) {
            return i10;
        }
        if (LocalLog.f()) {
            LocalLog.a(TAG, "get Ambient Temperature < 0.");
        }
        return 0;
    }

    public int getAmbientTempState() {
        return this.mAmbientTempState;
    }

    public int getAmbientTemperature() {
        return this.mAmbientTemperature;
    }

    public int getAppControlLevel() {
        return this.mRestrictLevel;
    }

    public List<WindowInfo> getAppFloatingWindowsInfo(List<WindowInfo> list) {
        ArrayList arrayList = new ArrayList();
        for (WindowInfo windowInfo : list) {
            if (windowInfo.type == 2038) {
                windowInfo.scene = getAppState(windowInfo.pkg);
                arrayList.add(windowInfo);
            }
        }
        return arrayList;
    }

    public List<Integer> getAppState(String str) {
        ArrayList arrayList = new ArrayList();
        StateManager stateManager = this.mStateManager;
        if (stateManager == null) {
            this.mStateManager = StateManager.f(this.mContext);
            return arrayList;
        }
        return stateManager.j(str, UserHandle.myUserId());
    }

    public int getAppType(String str) {
        if (this.mStateManager == null) {
            this.mStateManager = StateManager.f(this.mContext);
            return 0;
        }
        return StateManager.e(this.mContext, str);
    }

    public ArrayList<Integer> getAudioPidList() {
        String parameters;
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        }
        ArrayList<Integer> arrayList = new ArrayList<>();
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null && (parameters = audioManager.getParameters("get_pid")) != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(parameters, ":");
            while (stringTokenizer.hasMoreTokens()) {
                arrayList.add(Integer.valueOf(Integer.parseInt(stringTokenizer.nextToken())));
            }
        }
        return arrayList;
    }

    public int getCameraVideoStopState() {
        return this.mCameraVideoStopState;
    }

    public int getChargingLevel() {
        return this.mChargeLevel;
    }

    public int getCurrentBrightness() {
        return this.mCurrentBrightness;
    }

    public int getCurrentRefreshRate() {
        return this.mCurrentRefreshRate;
    }

    public float getCurrentTemperature(boolean z10) {
        if (!z10) {
            return this.mTemperatureFromBroadcast;
        }
        return getTempFromBind();
    }

    public int getCurrentThermalStatus() {
        if (this.mContext.getUserId() != 0) {
            if (LocalLog.f()) {
                LocalLog.a(TAG, "getCurrentThermalStatus " + this.mThermalStatusFromBroadcast + " in user " + this.mContext.getUserId());
            }
            return this.mThermalStatusFromBroadcast;
        }
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        if (this.mHorae == null) {
            connectHorae();
        }
        int i10 = -2;
        try {
            try {
                if (this.mHorae != null) {
                    obtain.writeInterfaceToken(HORAE_SERVICE_INTERFACE);
                    this.mHorae.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    i10 = obtain2.readInt();
                    LocalLog.l(TAG, "get thermalStatus =" + i10);
                }
            } catch (RemoteException e10) {
                LocalLog.b(TAG, "getThermalStatus " + e10.toString());
            }
            return i10;
        } finally {
            obtain.recycle();
            obtain2.recycle();
        }
    }

    public int getDeviceProvisioned() {
        return this.mDeviceProvisioned;
    }

    public int getEnvironmentTemperatureType() {
        return this.mEnvironmentTemperatureType;
    }

    public List<WindowInfo> getFloatingWindowsInfo(boolean z10) {
        ArrayList arrayList = new ArrayList();
        ArrayMap arrayMap = new ArrayMap();
        List<Bundle> requestVisibleWindows = requestVisibleWindows();
        SparseIntArray sparseIntArray = new SparseIntArray();
        Iterator<Bundle> it = requestVisibleWindows.iterator();
        String str = "";
        boolean z11 = false;
        while (it.hasNext()) {
            WindowInfo createFromBundle = WindowInfo.createFromBundle(it.next());
            if (LocalLog.f()) {
                LocalLog.a(TAG, "getZoomWindowsInfo " + createFromBundle);
            }
            boolean z12 = createFromBundle.type == 2038 && createFromBundle.uid != 1000;
            String str2 = (createFromBundle.uid + createFromBundle.type + createFromBundle.mode) + createFromBundle.activityName;
            int i10 = createFromBundle.mode;
            if (i10 != 1) {
                if (i10 == 2) {
                    arrayMap.put(str2, createFromBundle);
                } else if (i10 != 3) {
                    if (i10 == 4 || i10 == 6) {
                        if (!createFromBundle.pkg.equals(this.mLastForegroundPkg)) {
                            str = createFromBundle.pkg;
                            this.mSplitForegroundPkg = str;
                            if (LocalLog.f()) {
                                LocalLog.a(TAG, "getFloatingWindowsInfo: mSplitForegroundPkg=" + this.mSplitForegroundPkg);
                            }
                        }
                    } else if (i10 != 100) {
                        if (z12) {
                            arrayMap.put(str2, createFromBundle);
                        } else if (createFromBundle.type == 1) {
                            sparseIntArray.put(createFromBundle.uid, 1);
                        }
                    } else if (createFromBundle.type == 1 && !z11) {
                        arrayMap.put(str2, createFromBundle);
                        z11 = true;
                    }
                }
            }
            if (z12) {
                arrayMap.put(str2, createFromBundle);
            } else {
                sparseIntArray.put(createFromBundle.uid, 1);
            }
        }
        for (WindowInfo windowInfo : arrayMap.values()) {
            if (z10) {
                if (sparseIntArray.get(windowInfo.uid, 0) != 1) {
                    windowInfo.bundle.putString("split_foreground_pkg", str);
                    arrayList.add(windowInfo);
                }
            } else {
                arrayList.add(windowInfo);
            }
        }
        return arrayList;
    }

    public int getFoldingMode() {
        return this.mSystemFoldingMode;
    }

    public String getForegroundPkg() {
        return this.mLastForegroundPkg;
    }

    public int getGameUserMode() {
        return this.mGameUserMode;
    }

    public boolean getIsScreenBlockPhoneCall() {
        return this.mScreenBlockPhoneCall;
    }

    public List<PackageStateInfo> getPackages(int i10) {
        ArrayList arrayList = new ArrayList();
        StateManager stateManager = this.mStateManager;
        if (stateManager == null) {
            this.mStateManager = StateManager.f(this.mContext);
            return arrayList;
        }
        return stateManager.g(i10);
    }

    public List<WindowInfo> getPipWindowsInfo(List<WindowInfo> list) {
        ArrayList arrayList = new ArrayList();
        for (WindowInfo windowInfo : list) {
            if (windowInfo.mode == 2) {
                windowInfo.scene = getAppState(windowInfo.pkg);
                arrayList.add(windowInfo);
            }
        }
        return arrayList;
    }

    public int getPolicyChargeLevel() {
        return this.mPolicyChargeLevel;
    }

    public List<String> getRecentTask() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = (ArrayList) ((ActivityManager) this.mContext.getSystemService("activity")).getRecentTasks(ActivityManager.getMaxRecentTasksStatic(), 2);
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                ActivityManager.RecentTaskInfo recentTaskInfo = (ActivityManager.RecentTaskInfo) it.next();
                ComponentName componentName = recentTaskInfo.baseActivity;
                if (componentName != null && componentName.getPackageName() != null) {
                    arrayList.add(recentTaskInfo.baseActivity.getPackageName());
                }
            }
        }
        return arrayList;
    }

    public int getRefreshRate(int i10) {
        if (i10 == 1) {
            return 2;
        }
        if (i10 != 2) {
            return i10 != 3 ? 0 : 1;
        }
        return 5;
    }

    public int getSpeedChargeAddLevel() {
        return this.mSpeedChargeAdd;
    }

    public String getSplitForegroundPkg() {
        return this.mSplitForegroundPkg;
    }

    public float getTempFromBind() {
        if (this.mContext.getUserId() != 0) {
            if (LocalLog.f()) {
                LocalLog.a(TAG, "getCurrentTemperature " + this.mTemperatureFromBroadcast + " in user " + this.mContext.getUserId());
            }
            return this.mTemperatureFromBroadcast;
        }
        float f10 = 0.0f;
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        if (this.mHorae == null) {
            connectHorae();
        }
        try {
            try {
                if (this.mHorae != null) {
                    obtain.writeInterfaceToken(HORAE_SERVICE_INTERFACE);
                    this.mHorae.transact(17, obtain, obtain2, 0);
                    f10 = obtain2.readFloat();
                    LocalLog.l(TAG, "get temperature=" + f10);
                }
            } catch (RemoteException e10) {
                LocalLog.b(TAG, "getCurrentTemperature " + e10.toString());
            }
            return f10;
        } finally {
            obtain.recycle();
            obtain2.recycle();
        }
    }

    public boolean getUserForeground() {
        return this.mIsUserForeground;
    }

    public int getUserMode() {
        if (this.mIsSuperPowerSaveMode) {
            return 4;
        }
        if (this.mIsPowerSaveMode) {
            return 3;
        }
        if (this.mIsBenchMode) {
            return 0;
        }
        return this.mIsHighPrefMode ? 1 : 2;
    }

    public boolean getWifiHotSpotState() {
        return ((WifiManager) this.mContext.getSystemService(ThermalPolicy.KEY_WIFI)).getWifiApState() == 13;
    }

    public List<WindowInfo> getZoomWindowsInfo(List<WindowInfo> list) {
        ArrayList arrayList = new ArrayList();
        for (WindowInfo windowInfo : list) {
            if (windowInfo.mode == 100) {
                windowInfo.scene = getAppState(windowInfo.pkg);
                arrayList.add(windowInfo);
            }
        }
        return arrayList;
    }

    public boolean isAging() {
        return "true".equals(SystemProperties.get(AGING_PERSIST_PROP));
    }

    public boolean isAudioBackground(List<Integer> list) {
        List<PackageStateInfo> packages = getPackages(107);
        if (LocalLog.f()) {
            LocalLog.a(TAG, "audioInfo: " + packages);
        }
        if (packages == null || packages.isEmpty()) {
            return false;
        }
        return list == null || list.isEmpty() || !list.contains(107);
    }

    public boolean isAudioOrNavigation(List<Integer> list) {
        return isNavigationBackground(list) || isAudioBackground(list);
    }

    public boolean isBackupHighPerformance() {
        return this.mIsBackupHighPerformance;
    }

    public boolean isBackupRestore() {
        if ("com.coloros.backuprestore".equals(getForegroundPkg()) && this.mIsBackupRestore) {
            return true;
        }
        return this.mSystemFoldingMode == 0 && SECONDARY_HOME_PACKAGE_NAME.equals(getForegroundPkg()) && this.mIsBackupRestore;
    }

    public boolean isBackupRestoreScene() {
        return this.mIsBackupRestoreScene;
    }

    public boolean isCameraOn() {
        Method b10 = d.a().b("android.os.OplusBatteryManager", "isCameraOn", new Class[0]);
        if (this.mOplusBatteryManager == null) {
            this.mOplusBatteryManager = new OplusBatteryManager();
        }
        try {
            return ((Boolean) b10.invoke(this.mOplusBatteryManager, new Object[0])).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException e10) {
            LocalLog.b(TAG, "isCameraOn " + e10.toString());
            return false;
        }
    }

    public boolean isCharging() {
        return this.mIsCharging;
    }

    public boolean isFloatWindowOn() {
        return this.mFloatWindowState == 1;
    }

    public boolean isFoldingMode() {
        return this.mSystemFoldingMode == 1;
    }

    public boolean isGameAlive() {
        List<PackageStateInfo> packages = getPackages(104);
        if (LocalLog.f()) {
            LocalLog.a(TAG, "gameAliveInfo: " + packages);
        }
        return (packages == null || packages.isEmpty()) ? false : true;
    }

    public boolean isNavigationBackground(List<Integer> list) {
        List<PackageStateInfo> packages = getPackages(106);
        if (LocalLog.f()) {
            LocalLog.a(TAG, "navigationInfo: " + packages);
        }
        if (packages == null || packages.isEmpty()) {
            return false;
        }
        return list == null || list.isEmpty() || !list.contains(106);
    }

    public boolean isNeedSkipScreenBlocked(boolean z10) {
        return z10 || isDaydreamvideo() || isVideoInCall();
    }

    public boolean isPhoneInCall(Context context) {
        int phoneCount = ((TelephonyManager) context.getSystemService("phone")).getPhoneCount();
        for (int i10 = 0; i10 < phoneCount; i10++) {
            if (OplusOSTelephonyManager.getDefault(context).getCallStateGemini(i10) != 0) {
                if (LocalLog.f()) {
                    LocalLog.a(TAG, "Single card phone in call!!");
                }
                return true;
            }
        }
        return false;
    }

    public boolean isRecordingOn() {
        return this.mRecordingState == 1;
    }

    public boolean isScreenOn() {
        return this.mIsScreenOn;
    }

    public boolean isSpeedCharging() {
        return this.mIsSpeedCharging;
    }

    public boolean isSplitMode() {
        return this.mEnterSplitMode;
    }

    public boolean isTraceMonitorOn() {
        return this.mTraceMonitorState == 1;
    }

    public boolean isVideoInCall() {
        List<PackageStateInfo> packages = getPackages(103);
        if (LocalLog.f()) {
            LocalLog.a(TAG, "videoCallInfo: " + packages);
        }
        return (packages == null || packages.isEmpty()) ? false : true;
    }

    public boolean isVideoScene() {
        List<PackageStateInfo> packages = getPackages(101);
        if (LocalLog.f()) {
            LocalLog.a(TAG, "videoInfo: " + packages);
        }
        return (packages == null || packages.isEmpty()) ? false : true;
    }

    public void onDestory() {
        this.mContentResolver.unregisterContentObserver(this.mBackupRestoreObserver);
        this.mContentResolver.unregisterContentObserver(this.mBackupRestoreHighPerformanceObserver);
        this.mContentResolver.unregisterContentObserver(this.mCosaGameModeObserver);
        this.mContentResolver.unregisterContentObserver(this.mCosaRefreshSetObserver);
        this.mContentResolver.unregisterContentObserver(this.mDeviceProvisionedObserver);
        this.mContentResolver.unregisterContentObserver(this.mSystemFoldingModeObserver);
        this.mContentResolver.unregisterContentObserver(this.mTraceMonitorStateObserver);
        this.mContentResolver.unregisterContentObserver(this.mFloatWindowStateObserver);
        this.mContentResolver.unregisterContentObserver(this.mRecordingStateObserver);
        this.mContentResolver.unregisterContentObserver(this.mHighTempSafetyStateObserver);
        OplusSplitScreenManager.getInstance().unregisterSplitScreenObserver(this.mSplitScreenObserver);
        this.mContentResolver.unregisterContentObserver(this.mThermalOptimizationObserver);
    }

    public void onStart(Looper looper) {
        this.mThermalManager = ThermalFactory.a(this.mContext, looper);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(EXTRA_KEY_BACKUP_RESTORE_STATE), false, this.mBackupRestoreObserver, -2);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(EXTRA_KEY_BACKUP_RESTORE_HIGH_PERFORMANCE), false, this.mBackupRestoreHighPerformanceObserver, -2);
        this.mContentResolver.registerContentObserver(AppSettings.d.d(EXTRA_KEY_COSA_GAME_MODE), false, this.mCosaGameModeObserver, 0);
        this.mContentResolver.registerContentObserver(AppSettings.d.d(EXTRA_KEY_COSA_REFRESH_SET), false, this.mCosaRefreshSetObserver, 0);
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("device_provisioned"), false, this.mDeviceProvisionedObserver, -2);
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor(SYSTEM_FOLDING_MODE_KEYS), false, this.mSystemFoldingModeObserver, 0);
        this.mContentResolver.registerContentObserver(AppSettings.e.e(THERMAL_TRACE_INFO_MONITOR_STATE), false, this.mTraceMonitorStateObserver, 0);
        this.mContentResolver.registerContentObserver(Settings.System.getUriFor(OPLUS_FLOAT_WINDOW_SHOW_STATE), false, this.mFloatWindowStateObserver, 0);
        this.mContentResolver.registerContentObserver(Settings.System.getUriFor(OPLUS_CAMERA_RECORDING_STATE), false, this.mRecordingStateObserver, 0);
        OplusSplitScreenManager.getInstance().registerSplitScreenObserver(this.mSplitScreenObserver);
        this.mHighTempSafetyStateObserver = new ContentObserver(null) { // from class: com.oplus.thermalcontrol.ThermalControlUtils.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z10) {
                ThermalControlUtils thermalControlUtils = ThermalControlUtils.this;
                thermalControlUtils.mHighTempSafetyState = Settings.System.getInt(thermalControlUtils.mContext.getContentResolver(), "oplus_settings_hightemp_safety_state", -1);
            }
        };
        this.mContentResolver.registerContentObserver(Settings.System.getUriFor("oplus_settings_hightemp_safety_state"), false, this.mHighTempSafetyStateObserver, -2);
        registerThermalOptimizationObserver();
    }

    public void onThermalOptimization() {
        this.mThermalOptimizationNum++;
        Settings.System.putIntForUser(this.mContext.getContentResolver(), THERMAL_CONTROL_OPTIMIZATION_NUMBER, this.mThermalOptimizationNum, -2);
        LocalLog.a(TAG, "Thermal Optimization Num:" + this.mThermalOptimizationNum);
    }

    public void registerThermalBinder(ThermalStatusListener thermalStatusListener) {
        if (!binderThermal(thermalStatusListener)) {
            if (this.mCountRegister < 5) {
                this.mainHandler.sendEmptyMessageDelayed(101, 3000L);
                this.mCountRegister++;
            } else {
                this.mainHandler.sendEmptyMessageDelayed(101, 60000L);
            }
            LocalLog.l(TAG, "rebind horae " + this.mCountRegister + " times");
            return;
        }
        this.mCountRegister = 0;
        if (this.mainHandler.hasMessages(101)) {
            this.mainHandler.removeMessages(101);
        }
        if (LocalLog.f()) {
            LocalLog.a(TAG, "write binder success");
        }
        int i10 = Settings.Global.getInt(this.mContext.getContentResolver(), SYSTEM_FOLDING_MODE_KEYS, -1);
        this.mSystemFoldingMode = i10;
        sendSystemFoldingMode(i10);
    }

    public boolean requestRefreshRate(boolean z10, int i10) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "open =" + z10 + " , rate:" + i10);
        }
        try {
            return IOplusScreenMode.Stub.asInterface(ServiceManager.getService(OPLUS_SCREENMODE_SERVICE_NAME)).requestRefreshRateWithToken(z10, i10, (IBinder) null);
        } catch (Exception unused) {
            LocalLog.b(TAG, "requestRefreshRate Exception");
            return false;
        }
    }

    public void resetFps(String str) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "resetFps");
        }
        try {
            if (this.mVVRBinder == null) {
                this.mVVRBinder = ServiceManager.getService("oplus_vrr_service");
            }
            if (this.mVVRBinder != null) {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken(FPS_INTERFACE);
                obtain.writeFloat(0.0f);
                obtain.writeString(str);
                obtain.writeBoolean(false);
                obtain.writeString(FPS_DESCRIPTION);
                this.mVVRBinder.transact(20, obtain, null, 1);
                obtain.recycle();
            }
        } catch (RemoteException e10) {
            LocalLog.b(TAG, "setFps error: " + e10.toString());
        }
    }

    public void sendChargeModeChanged(int i10) {
        LocalLog.l(TAG, "Charging mode " + i10);
        this.mIsSpeedCharging = i10 == 3;
        setChargingLevel(this.mPolicyChargeLevel, this.mSpeedChargeAdd, this.mLastForegroundPkg);
    }

    public void sendChargeStatus(int i10) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "sendChargeStatus:" + i10);
        }
        boolean z10 = i10 != 0;
        this.mIsCharging = z10;
        if (z10) {
            ThermalControllerCenter.getInstance(this.mContext).sendChargeChangedMessage();
        }
    }

    public void sendFlashTorch(boolean z10) {
        if (this.mContext.getUserId() != 0) {
            if (LocalLog.f()) {
                LocalLog.a(TAG, "sendFlashTorch " + z10 + " in user " + this.mContext.getUserId());
            }
            Intent intent = new Intent();
            intent.putExtra("name", "flash_torch");
            intent.putExtra("state", z10);
            sendUserSystemBroadcast(intent);
            return;
        }
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        if (this.mHorae == null) {
            connectHorae();
        }
        try {
            try {
                if (this.mHorae != null) {
                    obtain.writeInterfaceToken(HORAE_SERVICE_INTERFACE);
                    obtain.writeInt(z10 ? 1 : 0);
                    this.mHorae.transact(19, obtain, obtain2, 0);
                    if (LocalLog.f()) {
                        LocalLog.a(TAG, "torch status " + z10);
                    }
                }
            } catch (RemoteException e10) {
                LocalLog.b(TAG, "sendFlashTorch " + e10.toString());
            }
        } finally {
            obtain.recycle();
            obtain2.recycle();
        }
    }

    public void sendPkgName(String str) {
        if (this.mContext.getUserId() != 0) {
            if (LocalLog.f()) {
                LocalLog.a(TAG, "sendPkgName " + str + " in user " + this.mContext.getUserId());
            }
            Intent intent = new Intent();
            intent.putExtra("name", "send_pkg");
            intent.putExtra("pkgName", str);
            sendUserSystemBroadcast(intent);
            return;
        }
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        if (this.mHorae == null) {
            connectHorae();
        }
        try {
            try {
                if (this.mHorae != null) {
                    obtain.writeInterfaceToken(HORAE_SERVICE_INTERFACE);
                    obtain.writeString(str);
                    this.mHorae.transact(18, obtain, obtain2, 0);
                    if (LocalLog.f()) {
                        LocalLog.a(TAG, "send pkgName " + str);
                    }
                }
            } catch (RemoteException e10) {
                LocalLog.b(TAG, "sendPkgName " + e10.toString());
            }
        } finally {
            obtain.recycle();
            obtain2.recycle();
        }
    }

    public void sendScreenStatus(boolean z10) {
        if (z10) {
            this.mIsScreenBlocked = getIsScreenBlocked();
        } else {
            this.mIsScreenBlocked = true;
        }
        this.mIsScreenOn = z10;
        boolean isPhoneInCall = isPhoneInCall(this.mContext);
        if (!z10 && !isPhoneInCall && !isBackupRestore()) {
            cancelChargingControl(this.mLastForegroundPkg);
        }
        ThermalControllerCenter.getInstance(this.mContext).sendScreenChangedMessage();
        if (this.mContext.getUserId() != 0) {
            if (LocalLog.f()) {
                LocalLog.a(TAG, "sendScreenStatus " + z10 + " in user " + this.mContext.getUserId());
            }
            Intent intent = new Intent();
            intent.putExtra("name", "screen_status");
            intent.putExtra("isScreenOn", z10);
            sendUserSystemBroadcast(intent);
            return;
        }
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        if (this.mHorae == null) {
            connectHorae();
        }
        try {
            try {
                if (this.mHorae != null) {
                    obtain.writeInterfaceToken(HORAE_SERVICE_INTERFACE);
                    if (z10) {
                        obtain.writeInt(1);
                    } else if (isPhoneInCall) {
                        LocalLog.a(TAG, "send phoneInCall status");
                        obtain.writeInt(2);
                    } else if (isBackupRestore()) {
                        LocalLog.a(TAG, "send BackupRestore status");
                        obtain.writeInt(4);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mHorae.transact(12, obtain, obtain2, 0);
                    LocalLog.a(TAG, "send screen status " + z10);
                }
            } catch (RemoteException e10) {
                LocalLog.b(TAG, "sendScreenStatus " + e10.toString());
            }
        } finally {
            obtain.recycle();
            obtain2.recycle();
        }
    }

    public void sendSystemFoldingMode(int i10) {
        ThermalControllerCenter.getInstance(this.mContext).sendFoldingModeMessage();
        if (this.mContext.getUserId() != 0) {
            if (LocalLog.f()) {
                LocalLog.a(TAG, "sendSystemFoldingMode " + i10 + " in user " + this.mContext.getUserId());
            }
            Intent intent = new Intent();
            intent.putExtra("name", "system_folding_mode");
            intent.putExtra("state", i10);
            sendUserSystemBroadcast(intent);
            return;
        }
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        if (this.mHorae == null) {
            connectHorae();
        }
        try {
            try {
                if (this.mHorae != null) {
                    obtain.writeInterfaceToken(HORAE_SERVICE_INTERFACE);
                    obtain.writeInt(i10);
                    this.mHorae.transact(24, obtain, obtain2, 0);
                    if (LocalLog.f()) {
                        LocalLog.a(TAG, "folding status " + i10);
                    }
                }
            } catch (RemoteException e10) {
                LocalLog.b(TAG, "sendSystemFoldingMode " + e10.toString());
            }
        } finally {
            obtain.recycle();
            obtain2.recycle();
        }
    }

    public void sendThermalLevelChangeBroadcast(int i10, int i11, Map<String, Integer> map) {
        Intent intent = new Intent(INTENT_OPLUS_THERMAL_LEVEL_CHANGE);
        intent.putExtra(THERMAL_LEVEL, i10);
        intent.putExtra(THERMAL_CURRENT_TEMPERATURE, i11);
        intent.putExtra(THERMAL_CPU_HEAT_CONTRIBUTION, map.getOrDefault(ThermalPolicy.KEY_CPU, 0).intValue());
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    public void sendUserSystemBroadcast(Intent intent) {
        intent.setAction("oplus.intent.action.ACTION_THERMAL_CONTROL_USER_SYSTEM");
        intent.setPackage(this.mContext.getPackageName());
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    public void set5GStatus(int i10) {
        LocalLog.l(TAG, " set modem=" + i10);
        if (this.m5GStatus == i10) {
            LocalLog.l(TAG, "not set the same modem=" + i10);
            return;
        }
        this.m5GStatus = i10;
        switch (i10) {
            case 0:
                send5Gbraodcast((int) getCurrentTemperature(false), 0, 0, 0, 0, 0, 0);
                return;
            case 1:
                send5Gbraodcast((int) getCurrentTemperature(false), 1, 0, 0, 0, 4, 4);
                return;
            case 2:
                send5Gbraodcast((int) getCurrentTemperature(false), 1, 0, 0, 0, 5, 5);
                return;
            case 3:
                send5Gbraodcast((int) getCurrentTemperature(false), 1, 1, 0, 0, 0, 0);
                return;
            case 4:
            case 5:
            case 6:
            case 7:
                send5Gbraodcast((int) getCurrentTemperature(false), 1, 1, 0, 0, i10, i10);
                return;
            default:
                return;
        }
    }

    public void setAmbientTempState(int i10) {
        this.mAmbientTempState = i10;
    }

    public void setAmbientTemperature(int i10) {
        boolean z10 = i10 != this.mAmbientTemperature;
        this.mAmbientTemperature = i10;
        if (z10) {
            ThermalControllerCenter.getInstance(this.mContext).sendAmbientTempChangeMessage();
        }
    }

    public void setAppControlLevel(int i10) {
        String str = this.mRestrictLevelList;
        if (str == null || !str.contains(String.valueOf(i10))) {
            return;
        }
        if (LocalLog.f()) {
            LocalLog.a(TAG, "setAppControlLevel,level=" + i10);
        }
        Intent intent = new Intent(INTENT_REQUEST_APP_CONTROL);
        intent.setPackage(ATHENA_PACKAGE_NAME);
        intent.putExtra(ATHENA_EXTRA_CALLER_PACKAGE, PACKAGE_BATTERY_THERMALCONTROL);
        intent.putExtra("level", i10);
        intent.putExtra("reason", PACKAGE_BATTERY_THERMALCONTROL);
        this.mContext.startService(intent);
    }

    public void setBenchMode(boolean z10) {
        this.mIsBenchMode = z10;
    }

    public void setCameraExitState(boolean z10) {
        if (!this.mAlreadyCameraExitState || z10) {
            if (z10) {
                this.mAlreadyCameraExitState = false;
            } else {
                this.mAlreadyCameraExitState = true;
            }
            Intent intent = new Intent();
            intent.setPackage(this.mContext.getPackageName());
            intent.setAction("oplus.intent.action.OPLUS_CAMERA_OPTION");
            intent.putExtra("option", OPLUS_CAMERA_EXIT_STATE);
            intent.putExtra("status", z10 ? 1 : 0);
            this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
        }
    }

    public void setCameraFlashDisableState(boolean z10) {
        if (!this.mAlreadyCameraFlashDisableState || z10) {
            if (z10) {
                this.mAlreadyCameraFlashDisableState = false;
            } else {
                this.mAlreadyCameraFlashDisableState = true;
            }
            Intent intent = new Intent();
            intent.setPackage(this.mContext.getPackageName());
            intent.setAction("oplus.intent.action.OPLUS_CAMERA_OPTION");
            intent.putExtra("option", OPLUS_CAMERA_FLASH_STATE);
            intent.putExtra("status", z10 ? 1 : 0);
            this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
        }
    }

    public void setCameraScreenBrightness(int i10) {
        if (this.mAlreadyCameraScreenBrightness && i10 == 255) {
            return;
        }
        if (i10 != 255) {
            this.mAlreadyCameraScreenBrightness = false;
        } else {
            this.mAlreadyCameraScreenBrightness = true;
        }
        Intent intent = new Intent();
        intent.setPackage(this.mContext.getPackageName());
        intent.setAction("oplus.intent.action.OPLUS_CAMERA_OPTION");
        intent.putExtra("option", OPLUS_CAMERA_BRIGHTNESS);
        intent.putExtra("status", i10);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    public void setCameraVideoStopState(int i10) {
        this.mCameraVideoStopState = i10;
        Intent intent = new Intent();
        intent.setPackage(this.mContext.getPackageName());
        intent.setAction("oplus.intent.action.OPLUS_CAMERA_OPTION");
        intent.putExtra("option", OPLUS_CAMERA_VIDEO_STATE);
        intent.putExtra("status", i10);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    public void setChargingLevel(int i10, int i11, String str) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "isScreenOn " + isScreenOn() + ", isBackupRestore " + isBackupRestore() + ", mIsScreenBlocked " + this.mIsScreenBlocked + ", speedChargeAdd " + i11);
        }
        if (i10 < 0) {
            LocalLog.l(TAG, "charge level invalid.");
            return;
        }
        this.mPolicyChargeLevel = i10;
        this.mSpeedChargeAdd = i11;
        if (!isScreenOn()) {
            if (!isBackupRestore() && !isPhoneInCall(this.mContext)) {
                LocalLog.a(TAG, "not set charging level.");
                return;
            }
            LocalLog.a(TAG, "phone In Call or is BackupRestore.");
        } else if (this.mIsScreenBlocked && isCharging()) {
            boolean isPhoneInCall = isPhoneInCall(this.mContext);
            if (!isNeedSkipScreenBlocked(isPhoneInCall)) {
                cancelChargingControl(str);
                LocalLog.a(TAG, "screen is blocked");
                return;
            } else if (isPhoneInCall) {
                this.mScreenBlockPhoneCall = true;
            }
        }
        if (this.mOplusBatteryManager == null) {
            this.mOplusBatteryManager = new OplusBatteryManager();
        }
        if (this.mContext.getUserId() != 0) {
            setUserChargingLevel(i10, str);
            return;
        }
        if (isCharging()) {
            if (this.mIsSpeedCharging && i10 != 0) {
                int i12 = i11 > 0 ? i10 + i11 : i10;
                if (AppFeature.C()) {
                    float f10 = this.mTemperatureFromBroadcast;
                    if (f10 <= CUSTOMIZE_CHARGE_TEMP_HIGH_LIMIT && f10 >= CUSTOMIZE_CHARGE_TEMP_BOTTOM_LIMIT) {
                        int g6 = ThermalCustomizeChargeUtils.h(this.mContext).g(str);
                        int i13 = i12 + g6;
                        setSmartCoolDown(i13, i10, str);
                        this.mChargeLevel = i13;
                        LocalLog.l(TAG, "Customize level: " + i10 + ", speedChargeAdd:" + i11 + ", customizeLevel =" + g6);
                    }
                }
                setSmartCoolDown(i12, i10, str);
                this.mChargeLevel = i12;
                LocalLog.l(TAG, "Speed Charge level: " + i12 + ", speedChargeAdd:" + i11);
            } else {
                if (AppFeature.C() && i10 != 0) {
                    float f11 = this.mTemperatureFromBroadcast;
                    if (f11 <= CUSTOMIZE_CHARGE_TEMP_HIGH_LIMIT && f11 >= CUSTOMIZE_CHARGE_TEMP_BOTTOM_LIMIT) {
                        int g10 = ThermalCustomizeChargeUtils.h(this.mContext).g(str);
                        int i14 = i10 + g10;
                        setSmartCoolDown(i14, i10, str);
                        this.mChargeLevel = i14;
                        LocalLog.l(TAG, "Customize level: " + i10 + ", customizeLevel:" + g10);
                    }
                }
                setSmartCoolDown(i10, i10, str);
                this.mChargeLevel = i10;
            }
            if (LocalLog.f()) {
                LocalLog.a(TAG, "set charge level " + this.mChargeLevel);
            }
        }
    }

    public void setCpuAndGpuLevel(CpuLevelConfig.ThermalCpuLevelPolicy thermalCpuLevelPolicy, GpuLevelConfig.ThermalGpuLevelPolicy thermalGpuLevelPolicy, int i10, int i11, String str) {
        int i12 = thermalCpuLevelPolicy != null ? thermalCpuLevelPolicy.mLevel : -1;
        int i13 = thermalGpuLevelPolicy != null ? thermalGpuLevelPolicy.mLevel : -1;
        if (i12 < 0) {
            i12 = 15;
        }
        if (i13 < 0) {
            i13 = 15;
        }
        if (LocalLog.f()) {
            LocalLog.a(TAG, "setCpuAndGpuLevel temp=" + i11 + " serious:" + i10);
        }
        OsenseNotifyRequest osenseNotifyRequest = new OsenseNotifyRequest(4, 1, (i13 * 16) + i12, i10, i11, str);
        OsenseResClient osenseResClient = OsenseResClient.get(getClass());
        if (osenseResClient != null) {
            osenseResClient.osenseSetNotification(osenseNotifyRequest);
        } else {
            LocalLog.b(TAG, "OsenseResClient is null");
        }
    }

    public void setEnvironmentTemperatureType(int i10) {
        this.mEnvironmentTemperatureType = i10;
    }

    public void setFps(int i10, String str) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "setFps:" + i10);
        }
        try {
            if (this.mVVRBinder == null) {
                this.mVVRBinder = ServiceManager.getService("oplus_vrr_service");
            }
            if (this.mVVRBinder != null) {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken(FPS_INTERFACE);
                obtain.writeFloat(i10);
                obtain.writeString(str);
                obtain.writeBoolean(false);
                obtain.writeString(FPS_DESCRIPTION);
                this.mVVRBinder.transact(20, obtain, null, 1);
                obtain.recycle();
            }
        } catch (RemoteException e10) {
            LocalLog.b(TAG, "setFps error: " + e10.toString());
        }
    }

    public void setHbmControlState(int i10) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "setHbmControlState state: " + i10);
        }
        Settings.Secure.putInt(this.mContext.getContentResolver(), OPLUS_HBM_CONTROL_STATE, i10);
    }

    public void setHighPrefMode(boolean z10) {
        LocalLog.l(TAG, "setHighPrefMode:" + z10);
        this.mIsHighPrefMode = z10;
    }

    public void setHighTempSafetyState(int i10) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "setHighTempSafetyState,state=" + i10);
        }
        if (i10 == this.mHighTempSafetyState) {
            if (this.mAlreadyHighTempSafety && i10 == 0) {
                LocalLog.l(TAG, "setHighTempSafetyState countdown.");
            } else {
                LocalLog.l(TAG, "setHighTempSafetyState same.");
                return;
            }
        }
        Intent intent = new Intent();
        intent.setAction("oplus.intent.action.ACTION_THERMAL_CONTROL_HIGHTEMP_THIRD");
        intent.setPackage(this.mContext.getPackageName());
        if (i10 == 1) {
            intent.putExtra("third_step", 1);
            this.mAlreadyHighTempSafety = true;
        } else {
            intent.putExtra("third_step", -1);
            this.mAlreadyHighTempSafety = false;
        }
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    public void setIpaFeatureState(boolean z10) {
        if (this.mContext.getUserId() != 0) {
            if (LocalLog.f()) {
                LocalLog.a(TAG, "not set Ipa Feature in user " + this.mContext.getUserId());
                return;
            }
            return;
        }
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        if (this.mHorae == null) {
            connectHorae();
        }
        try {
            try {
                if (this.mHorae != null) {
                    obtain.writeInterfaceToken(HORAE_SERVICE_INTERFACE);
                    obtain.writeInt(z10 ? 1 : 0);
                    this.mHorae.transact(107, obtain, obtain2, 0);
                    LocalLog.l(TAG, "Ipa Feature:" + z10);
                }
            } catch (RemoteException e10) {
                LocalLog.b(TAG, "setIpaFeatureState " + e10.toString());
            }
        } finally {
            obtain.recycle();
            obtain2.recycle();
        }
    }

    public void setIsScreenBlock(boolean z10) {
        LocalLog.l(TAG, "USER_PRESENT, Screen unlock.");
        this.mIsScreenBlocked = z10;
    }

    public void setIsScreenBlockPhoneCall(boolean z10) {
        this.mScreenBlockPhoneCall = z10;
    }

    public void setLastForegroundActivity(String str) {
        this.mLastForegroundActivity = str;
    }

    public void setLastForegroundPkg(String str) {
        if (this.mIsScreenBlocked) {
            LocalLog.l(TAG, "Screen unlock, mIsScreenBlocked incorrect.");
            this.mIsScreenBlocked = false;
        }
        if (TextUtils.equals(str, this.mLastForegroundPkg)) {
            LocalLog.l(TAG, "mForegroundPkg not changed");
            return;
        }
        if (TextUtils.equals(str, PACKAGE_OPLUS_EXSERVICEUI)) {
            LocalLog.l(TAG, "mForegroundPkg ignore exserviceui");
            return;
        }
        sendPkgName(str);
        if (!LAUNCHER_PACKAGE_NAME.equals(str) && !LAUNCHER_PACKAGE_NAME.equals(this.mLastForegroundPkg)) {
            this.mSplitForegroundPkg = this.mLastForegroundPkg;
        }
        if (isSplitMode()) {
            LocalLog.l(TAG, "mSplitForegroundPkg=" + this.mSplitForegroundPkg);
        }
        this.mLastForegroundPkg = str;
        LocalLog.l(TAG, "mForegroundPkg=" + this.mLastForegroundPkg);
        ThermalControllerCenter.getInstance(this.mContext).sendCategoryChangedMessage();
    }

    public void setOsieControlState(int i10) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "setOsieControlState state: " + i10);
        }
        Settings.Secure.putInt(this.mContext.getContentResolver(), OPLUS_OSIE_CONTROL_STATE, i10);
    }

    public void setPolicyCurrentScene(int i10) {
        this.mPolicyCurrentScene = i10;
    }

    public void setPowersaveMode(boolean z10) {
        LocalLog.l(TAG, "setPowersaveMode:" + z10);
        this.mIsPowerSaveMode = z10;
    }

    public void setRefreshRate(int i10) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "setRefreshRate state: " + i10);
        }
        if (i10 == 0) {
            setRefreshRateData(0, 0);
            this.mCurrentRefreshRate = 0;
            return;
        }
        if (i10 == 1) {
            setRefreshRateData(1, 2);
            this.mCurrentRefreshRate = 2;
        } else if (i10 == 2) {
            setRefreshRateData(1, 5);
            this.mCurrentRefreshRate = 5;
        } else if (i10 != 3) {
            setRefreshRateData(0, 0);
            this.mCurrentRefreshRate = 0;
        } else {
            setRefreshRateData(1, 1);
            this.mCurrentRefreshRate = 1;
        }
    }

    public void setRestrictLevelList(String str) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "setRestrictLevelList:" + str);
        }
        this.mRestrictLevelList = str;
    }

    public void setSceneListenList(String str) {
        this.mSceneListenList = str;
    }

    public int setSmartCoolDown(int i10, int i11, String str) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "setSmartCoolDown: cooldown = " + i10 + ", normal_cooldown = " + i11);
        }
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            Object newInstance = cls.newInstance();
            Class<?> cls2 = Integer.TYPE;
            return ((Integer) cls.getMethod("setSmartCoolDown", cls2, cls2, String.class).invoke(newInstance, Integer.valueOf(i10), Integer.valueOf(i11), str)).intValue();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e10) {
            LocalLog.b(TAG, "setSmartCoolDown fail:" + e10);
            OplusBatteryManager oplusBatteryManager = this.mOplusBatteryManager;
            if (oplusBatteryManager == null) {
                return -1;
            }
            oplusBatteryManager.setChargingLevel(String.valueOf(i10), TAG);
            return -1;
        }
    }

    public void setSpecBrightness(int i10, String str, int i11) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "setSpecBrightness, gear=" + i10 + ",reason=" + str);
        }
        try {
            Class<?> cls = Class.forName(DISPLAY_MANAGER_CLASSNAME);
            Object newInstance = cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            Class<?> cls2 = Integer.TYPE;
            cls.getDeclaredMethod(DISPLAY_MANAGER_SET_BRIGHTNESS, cls2, String.class, cls2).invoke(newInstance, Integer.valueOf(i10), str, Integer.valueOf(i11));
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e10) {
            LocalLog.b(TAG, "setSpecBrightness fail:" + e10);
        }
    }

    public void setSuperPowerSaveMode(boolean z10) {
        LocalLog.l(TAG, "setSuperPowerSaveMode:" + z10);
        this.mIsSuperPowerSaveMode = z10;
    }

    public void setThermalConfig(int[] iArr, int[] iArr2, int[] iArr3) {
        if (this.mContext.getUserId() != 0) {
            LocalLog.a(TAG, "not set config in user " + this.mContext.getUserId());
            return;
        }
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        if (this.mHorae == null) {
            connectHorae();
        }
        try {
            try {
                if (this.mHorae != null) {
                    obtain.writeInterfaceToken(HORAE_SERVICE_INTERFACE);
                    if (iArr != null && iArr.length > 0) {
                        obtain.writeInt(iArr.length);
                        obtain.writeIntArray(iArr);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (iArr2 != null && iArr2.length > 0) {
                        obtain.writeInt(iArr2.length);
                        obtain.writeIntArray(iArr2);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (iArr3 != null && iArr3.length > 0) {
                        obtain.writeInt(iArr3.length);
                        obtain.writeIntArray(iArr3);
                    } else {
                        obtain.writeInt(0);
                    }
                    LocalLog.a(TAG, "set status config");
                    this.mHorae.transact(6, obtain, obtain2, 0);
                }
            } catch (RemoteException e10) {
                LocalLog.b(TAG, "setThermalConfig " + e10.toString());
            }
        } finally {
            obtain.recycle();
            obtain2.recycle();
        }
    }

    public void setThermalStatusFromBroadcast(int i10, float f10) {
        this.mTemperatureFromBroadcast = f10;
        this.mThermalStatusFromBroadcast = i10;
    }

    public void setTorchState(boolean z10) {
        Settings.System.putIntForUser(this.mContext.getContentResolver(), OPLUS_CAMERA_TORCH, !z10 ? 1 : 0, 0);
    }

    public void setUserForeground(boolean z10) {
        this.mIsUserForeground = z10;
    }

    public void setVideosrControlState(int i10) {
        if (i10 == -2) {
            i10 = 0;
        }
        if (LocalLog.f()) {
            LocalLog.a(TAG, "setVideosrControlState state: " + i10);
        }
        Settings.Secure.putInt(this.mContext.getContentResolver(), OPLUS_VIDEOSR_CONTROL_STATE, i10);
        setOsieControlState(i10);
        setHbmControlState(i10);
    }

    public void setWifiSpeed(int i10, int i11, String str) {
        if (i10 < 0) {
            LocalLog.l(TAG, "wifi speed invalid.");
            return;
        }
        if (this.mWifiSpeed == i10) {
            LocalLog.l(TAG, "not set the same wifi=" + i10);
            return;
        }
        if (LocalLog.f()) {
            LocalLog.a(TAG, "wifi speed=" + i10);
        }
        this.mWifiSpeed = i10;
        int currentTemperature = (int) getCurrentTemperature(false);
        Intent intent = new Intent();
        intent.setAction("oplus.intent.action.THERMAL_THROTTLING_WIFI");
        intent.putExtra(EXTRA_TEMP, currentTemperature);
        intent.putExtra(EXTRA_THERMAL, i11);
        intent.putExtra(EXTRA_SCENE, str);
        intent.putExtra(EXTRA_WIFI_SPEED, i10);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    public void startCpuAndGpuControl(CpuLevelConfig.ThermalCpuLevelPolicy thermalCpuLevelPolicy, GpuLevelConfig.ThermalGpuLevelPolicy thermalGpuLevelPolicy, int i10) {
        ArrayList<UAHResourceInfo> arrayList = new ArrayList<>();
        if (thermalCpuLevelPolicy != null) {
            thermalCpuLevelPolicy.addUahResInfoTo(arrayList);
        }
        if (thermalGpuLevelPolicy != null) {
            thermalGpuLevelPolicy.addUahResInfoTo(arrayList);
        }
        arrayList.add(new UAHResourceInfo(UahConstants.THERMAL_SERIAL_TMPERATURE_LEVEL, String.valueOf(i10)));
        UAHRuleCtrlRequest uAHRuleCtrlRequest = new UAHRuleCtrlRequest(4, 1, arrayList);
        UAHResClient uAHResClient = this.mUAHClient;
        if (uAHResClient != null) {
            uAHResClient.ctrlRule(uAHRuleCtrlRequest);
        } else {
            LocalLog.b(TAG, UAH_CLIENT_IS_NULL);
        }
        if (LocalLog.f()) {
            JSONObject jSONObject = new JSONObject();
            if (thermalCpuLevelPolicy != null) {
                thermalCpuLevelPolicy.putIntoJsonObject(jSONObject);
            }
            if (thermalGpuLevelPolicy != null) {
                thermalGpuLevelPolicy.putIntoJsonObject(jSONObject);
            }
            LocalLog.a(TAG, "startCpuAndGpuControl, " + thermalCpuLevelPolicy + ", " + thermalGpuLevelPolicy + ", tsensorCpu:" + HeatSourceController.getInstance(this.mContext).getLastTsensorCpu() + ", thermalSerious:" + i10 + ", " + jSONObject.toString());
        }
    }

    public void startHighTemperature(int i10) {
        if (i10 == this.mRestrictLevel) {
            return;
        }
        if (LocalLog.f()) {
            LocalLog.a(TAG, "setHighTemperature,level=" + i10);
        }
        this.mRestrictLevel = i10;
        ThermalControlMonitor.getInstance(this.mContext).clearHighTempMsg();
        Intent intent = new Intent();
        intent.setAction("oplus.intent.action.ACTION_THERMAL_CONTROL_HIGHTEMP_SECOND");
        intent.setPackage(this.mContext.getPackageName());
        if (i10 == 5) {
            intent.putExtra("second_step", 1);
        } else {
            intent.putExtra("second_step", -1);
        }
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    public void stopCpuAndGpuControl(int i10, int i11, String str) {
        OsenseNotifyRequest osenseNotifyRequest = new OsenseNotifyRequest(4, 0, 0, i10, i11, str);
        OsenseResClient osenseResClient = OsenseResClient.get(getClass());
        if (osenseResClient != null) {
            osenseResClient.osenseSetNotification(osenseNotifyRequest);
        } else {
            LocalLog.b(TAG, "OsenseResClient is null");
        }
    }

    public void unregisterThermalBinder(ThermalStatusListener thermalStatusListener) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        if (this.mHorae == null) {
            connectHorae();
        }
        try {
            try {
                if (this.mHorae != null) {
                    obtain.writeInterfaceToken(HORAE_SERVICE_INTERFACE);
                    obtain.writeStrongBinder(thermalStatusListener.asBinder());
                    LocalLog.a(TAG, "unregister binder");
                    this.mHorae.transact(2, obtain, obtain2, 0);
                }
            } catch (RemoteException e10) {
                LocalLog.b(TAG, "unregisterThermalBinder " + e10.toString());
            }
        } finally {
            obtain.recycle();
            obtain2.recycle();
        }
    }

    public void stopCpuAndGpuControl(int i10) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "stopCpuAndGpuControl  serious:" + i10);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new UAHResourceInfo(UahConstants.THERMAL_SERIAL_TMPERATURE_LEVEL, String.valueOf(i10)));
        UAHRuleCtrlRequest uAHRuleCtrlRequest = new UAHRuleCtrlRequest(4, 0, arrayList);
        UAHResClient uAHResClient = this.mUAHClient;
        if (uAHResClient != null) {
            uAHResClient.ctrlRule(uAHRuleCtrlRequest);
        } else {
            LocalLog.b(TAG, UAH_CLIENT_IS_NULL);
        }
    }
}
