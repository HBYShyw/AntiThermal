package com.oplus.deepsleep;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.util.EventLog;
import android.util.Pair;
import android.util.Xml;
import androidx.core.content.ContextCompat;
import b6.LocalLog;
import c6.NotifyUtil;
import com.oplus.deepthinker.sdk.app.ClientConnection;
import com.oplus.deepthinker.sdk.app.OplusDeepThinkerManager;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepClusterWithPercentile;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepPredictResultWithPercentile;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import com.oplus.sceneservice.sdk.dataprovider.bean.SceneStatusInfo;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import d6.ConfigUpdateUtil;
import ea.DeepThinkerProxy;
import f6.CommonUtil;
import f6.f;
import i6.IDeepThinkerBridge;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import x5.UploadDataUtil;
import y5.AppFeature;
import y5.b;
import z5.GuardElfDataManager;

/* loaded from: classes.dex */
public class ControllerCenter {
    private static final String ACTION_APP_TRAFFIC_START_TIMEOUT = "oplus.intent.action.deepsleep.AppTrafficStartTimeout";
    private static final String ACTION_AUTO_STOP_TRAFFIC = "oplus.intent.action.deepsleep.AutoStopTraffic";
    private static final String ACTION_BATTERY_CURVE_END_MONITOR = "intent.action.deepsleep.batterycurve_end";
    private static final String ACTION_BATTERY_CURVE_START_MONITOR = "intent.action.deepsleep.batterycurve_start";
    private static final String ACTION_CAL_PREDICT_CHECK = "oplus.intent.action.deepsleep.CalPredictCheck";
    private static final String ACTION_DEEPSLEEP_STEP = "oplus.intent.action.deepsleep.step";
    private static final String ACTION_FORCESTOP_MONITOR = "oplus.intent.action.supersleep.ForcestopMonitor";
    private static final String ACTION_MUSIC_MONITOR = "oplus.intent.action.supersleep.MusicMonitor";
    private static final String ACTION_START_TRAFFIC_NEXT_DOZE_MAINTENANCE = "oplus.intent.action.deepsleep.StartTrafficNextDozeMaintenance";
    private static final String ACTION_TRAFFIC_MONITOR = "oplus.intent.action.deepsleep.TrafficMonitor";
    private static final int AI_SLEEP_DATA_THREAD_NUM = 2;
    private static final long AUTO_STOP_TRAFFIC_TIMEOUT = 600000;
    private static final int BATTERY_STATUS_TAG = 2723;
    private static final long DELAY_ALLOW_RCD_FROM_ACTIVE = 120000;
    private static final int DELAY_START_CLEAR_APPS = 300;
    private static final long DELAY_UPDATE_WHITELIST = 10000;
    private static final long DELAY_UPLOAD_AI_DCS = 600000;
    private static final long DETECT_START_DELAY = 600000;
    private static final int DFT_SLEEP_END_HOUR = 7;
    private static final int DFT_SLEEP_START_HOUR = 0;
    public static final String FIND_PHONE_SWITCH_STATE = "findmyphone_switch";
    private static final long FIVE_MINUTES_DURATION = 300000;
    private static final int FIVE_SECONDS_DURATION = 5000;
    private static final long FUZZY_INEXACT = 10000;
    private static final double HOURS_ONE_DAY = 24.0d;
    private static final int INT_NUMBER_TWO = 2;
    private static final int MAX_CNT_DIS_NET_UNEXPECTED = 3;
    private static final int MAX_MOTION_DETECTED = 3;
    private static final int MAX_PLAYBACK_CHECK = 3;
    private static final int MAX_RANDOM_MINUTES = 59;
    private static final int MAX_TRAFFIC_CHECK = 6;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int MIN_CLEAR_INTERVAL = 3600000;
    private static final int MIN_MORNING_CLEAR_INTERVAL = 64800000;
    private static final long MIN_NETWORK_SWITCH_TIME = 5000;
    private static final long MIN_SCREEN_ON_DURATION = 20000;
    private static final long MIN_SLEEP_DURATION = 1200000;
    private static final long MIN_UPLOAD_SCREENOFF_DURATION = 10800000;
    private static final int MSG_APP_TRAFFIC_CHANGE = 7;
    private static final int MSG_BOOT_COMPLETE = 8;
    private static final int MSG_CHANGE_AIRPLANE_MODE = 17;
    private static final int MSG_INTERRUPTED_WHEN_DEEPSLEEP = 5;
    private static final int MSG_QUICK_ENTER_DEEPSLEEP = 15;
    private static final int MSG_REDISABLE_MOBILE_NET = 19;
    private static final int MSG_REDISABLE_WIFI_NET = 20;
    private static final int MSG_RESTORE_NETWORK_REQ = 6;
    private static final int MSG_SHOW_NETOFF_NOTIFY = 16;
    private static final int MSG_SIGNICANT_MOTION_DETECTED = 4;
    private static final int MSG_START_CLEAR_APPS = 9;
    private static final int MSG_STEP_DEEP_SLEEP = 1;
    private static final int MSG_TEST_FORCE_GET_AI_PREDICT = 18;
    private static final int MSG_TRAFFIC_MONITOR = 10;
    private static final int MSG_UPDATE_WHITELIST_PKG = 12;
    private static final int MSG_UPLOAD_AI_STATISTICS_DCS = 13;
    private static final int MSG_WIFI_EN_DIS = 11;
    private static final long MS_IN_SECOND = 1000;
    public static final int NETWORK_SWITCH_ALL_OFF = 0;
    public static final int NETWORK_SWITCH_ALL_ON = 3;
    public static final int NETWORK_SWITCH_MOBILE_ON = 2;
    public static final int NETWORK_SWITCH_WIFI_ON = 1;
    public static final int NETWORK_TYPE_MOBILE = 2;
    public static final int NETWORK_TYPE_NONE = 0;
    public static final int NETWORK_TYPE_WIFI = 1;
    private static final long ONE_DAY = 86400000;
    private static final long ONE_HOUR_MILLS = 3600000;
    private static final float ONE_HUNDRED_PERCENT = 100.0f;
    private static final int ONE_INTEGER = 1;
    private static final long ONE_MINUTE_DURATION = 60000;
    public static final int ONE_WEEK_DURATION = 7;
    private static final long PLAYBACK_CHECK_INTERVAL = 600000;
    private static final long RESERVED_EXIT_DEEPSLEEP_TIME = 900000;
    private static final long SENSING_DURATION = 1200000;
    private static final long SHORT_SENSING_DURATION = 180000;
    private static final String SIMULATETED_TRAFFIC_PKG_JOB = "traffic:high";
    private static final int START_CLEAR_WAKELOCK_TIMEOUT = 1000;
    public static final int STATUS_ACTIVE = 0;
    public static final int STATUS_APP_TRAFFIC = 5;
    public static final int STATUS_DEEP_SLEEP = 4;
    public static final int STATUS_INACTIVE = 1;
    public static final int STATUS_SENSING = 2;
    public static final int STATUS_WAITTING_TRAFFIC_STOP = 3;
    private static final int SUPERSLEEP_BATTERY_CARVE_DURATION = 3600000;
    private static final int SUPERSLEEP_BATTERY_CARVE_END_TIME = 43200000;
    private static final int SUPERSLEEP_BATTERY_CARVE_START_TIME = 21;
    private static final long SUPERSLEEP_DETECT_START_DELAY = 300000;
    private static final long SUPERSLEEP_DISCONNECT_NETWORK = 1800000;
    private static final long SUPERSLEEP_FORCESTOP_DELAY = 600000;
    private static final long SUPERSLEEP_MIN_SLEEP_DURATION = 0;
    private static final long SUPERSLEEP_MUSIC_CHECK_TIMEOUT = 1800000;
    private static final long SUPERSLEEP_RESERVED_EXIT_DEEPSLEEP_TIME = 0;
    private static final long SUPERSLEEP_SENSING_DURATION = 300000;
    private static final long SUPERSLEEP_TRAFFIC_MONITOR_INTERVAL = 300000;
    private static final String TAG = "ControllerCenter";
    private static final int TEN_INTEGER = 10;
    private static final long TEN_MINUTES_DURATION = 600000;
    private static final int TEN_SECONDS_DURATION = 10000;
    private static final int TEST_SLEEP_SELECT = 5;
    private static final long THREE_MINUTES_DURATION = 180000;
    private static final int THREE_SECONDS_DURATION = 3000;
    private static final long THRESH_SPEED = 10;
    private static final long TIMEOUT_APP_STRAFFIC_START = 60000;
    private static final int TIMEOUT_DISABLE_NET = 3000;
    private static final int TIMEOUT_RESTORE_NET = 10000;
    private static final long TRAFFIC_MONITOR_INTERVAL = 300000;
    private static final long WAKELOCK_TIMEOUT = 5000;
    public static final int ZERO_INTEGER = 0;
    private static ControllerCenter sControllerCenter;
    private static Class<?> sOSysNetControlManager;
    private static Method sOSysNetControlManagerNewInstance;
    private static Method sSetDataEnabled;
    private static Method sSetWifiEnabled;
    private AlarmManager mAlarmManager;
    private ClientConnection mClientConnection;
    private ConfigUpdateUtil mConfigUpdateUtil;
    private Context mContext;
    private HandlerThread mDataThread;
    private OplusDeepThinkerManager mDeepThinkerManager;
    private boolean mDisableNetAllowed;
    private boolean mEverEnterDeepIdle;
    private GuardElfDataManager mGuardElfDataManager;
    private boolean mIsAiForecast;
    private boolean mIsMaintenanceRestored;
    private boolean mIsScreenOff;
    private boolean mIsUserPresent;
    private long mLastTrafficTimeStamp;
    private long mLastTrafficTotal;
    private List<String> mListTraffic;
    private List<String> mListTrafficAutoStop;
    private List<String> mListTrafficStartTime;
    private boolean mNeedClearApps;
    private boolean mNeedMorningClear;
    private int mNetWorkSwitchState;
    private int mNetWorkType;
    private long mNextAlarmTime;
    private String mPkgRestoreNet;
    private PowerManager mPowerManager;
    private long mPredictedSleepEndTime;
    private long mPredictedSleepStartTime;
    private DeepSleepRecord mRecord;
    private boolean mRedisableNetUnusable;
    private boolean mRedisabledMobNet;
    private boolean mRedisabledWifi;
    private SensorManager mSensorManager;
    private SignicantMotionListener mSignicantMotionListener;
    private Sensor mSignificantSensor;
    private int mStatus;
    private StatusObserver mStatusObserver;
    private TelephonyManager mTelephonyManager;
    private boolean mTestMode;
    private long mTestSleepEndTime;
    private long mTestSleepStartTime;
    private long mTimeChgToActive;
    private UploadDataUtil mUploadDataUtil;
    private DeepSleepUtils mUtils;
    private PowerManager.WakeLock mWakeLock;
    private PowerManager.WakeLock mWakeLockNet;
    private PowerManager.WakeLock mWakeLockProvider;
    private PowerManager.WakeLock mWakeLockStartClear;
    private boolean mWifiEnalbe;
    private WifiManager mWifiManager;
    private long mWifiOperationTimeStamp;
    private WorkHandler mWorkHandler;
    private boolean mIgnoreTraffic = false;
    private boolean mSimuAIPredict = false;
    private Object mLock = new Object();
    private List<String> mListPkgPlayback = new ArrayList();
    private List<String> mListPkgAudioIn = new ArrayList();
    private List<String> mListWhitelistPkg = new ArrayList();
    private long mLastWakeupTime = Long.MIN_VALUE;
    private int mCntMotionDetected = 0;
    private int mCntPlaybackCheck = 0;
    private int mCntNavigatingCheck = 0;
    private int mCntTrafficCheck = 0;
    private int mCntDisableNetUnexpected = 0;
    private boolean mIsWakeFromDeepSleep = false;
    private boolean mSystemPlaybackStatus = false;
    private boolean mWaitingTrafficStop = false;
    private AtomicBoolean mIsScreenOnInvokedLastTime = new AtomicBoolean(false);
    private long mTimeScreenOff = -1;
    private boolean mForecastSleepDurationHandled = false;
    private boolean mIsNeedSendMaintenanceRestoredBroadcast = false;
    private boolean mTriggerForMaintence = false;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.oplus.deepsleep.ControllerCenter.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ControllerCenter.ACTION_DEEPSLEEP_STEP.equals(action)) {
                ControllerCenter.this.mWakeLock.acquire(5000L);
                synchronized (ControllerCenter.this.mLock) {
                    ControllerCenter.this.mWorkHandler.sendEmptyMessage(1);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onReceiver: ACTION_DEEPSLEEP_STEP. stauts = ");
                    ControllerCenter controllerCenter = ControllerCenter.this;
                    sb2.append(controllerCenter.statusToString(controllerCenter.mStatus));
                    LocalLog.a(ControllerCenter.TAG, sb2.toString());
                }
                return;
            }
            if (ControllerCenter.ACTION_APP_TRAFFIC_START_TIMEOUT.equals(action)) {
                LocalLog.a(ControllerCenter.TAG, "onReceiver: ACTION_APP_TRAFFIC_START_TIMEOUT");
                ControllerCenter.this.mWakeLock.acquire(5000L);
                synchronized (ControllerCenter.this.mLock) {
                    ControllerCenter.this.appTraffitcTurnToStopLocked();
                }
                ControllerCenter.this.mWakeLock.release();
                return;
            }
            if (ControllerCenter.ACTION_TRAFFIC_MONITOR.equals(action)) {
                LocalLog.a(ControllerCenter.TAG, "onReceiver: ACTION_TRAFFIC_MONITOR");
                ControllerCenter.this.mWakeLock.acquire(5000L);
                ControllerCenter.this.mWorkHandler.sendEmptyMessage(10);
                return;
            }
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                NetworkInfo activeNetworkInfo = ((ConnectivityManager) ControllerCenter.this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
                boolean z10 = activeNetworkInfo != null && activeNetworkInfo.isConnected();
                if (!z10 && (ControllerCenter.this.mNeedClearApps || ControllerCenter.this.mNeedMorningClear)) {
                    ControllerCenter.this.mWakeLock.acquire(5000L);
                    ControllerCenter.this.mWorkHandler.sendEmptyMessageDelayed(9, 300L);
                } else {
                    ControllerCenter.this.mWakeLockNet.acquire(ControllerCenter.MS_IN_SECOND);
                }
                boolean z11 = activeNetworkInfo != null && activeNetworkInfo.getType() == 0;
                boolean z12 = activeNetworkInfo != null && activeNetworkInfo.getType() == 1;
                if (!ControllerCenter.this.mUtils.getExtremeSleepStatus() && ControllerCenter.this.mStatus == 4 && z10 && ControllerCenter.this.mDisableNetAllowed && ControllerCenter.this.mNetWorkSwitchState != 0) {
                    if (ControllerCenter.this.mCntDisableNetUnexpected >= 3) {
                        ControllerCenter.this.mRedisableNetUnusable = true;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("CONNECTIVITY_ACTION: in deep sleep, redisable net unusable. cnt=");
                        sb3.append(ControllerCenter.this.mCntDisableNetUnexpected);
                        sb3.append(", wifiConnected=");
                        sb3.append(z12);
                        sb3.append(", mobNetConnected=");
                        sb3.append(z11);
                        sb3.append(", netWorkType=");
                        ControllerCenter controllerCenter2 = ControllerCenter.this;
                        sb3.append(controllerCenter2.networkTypeToString(controllerCenter2.mNetWorkType));
                        sb3.append(", netWorkSwitchState=");
                        ControllerCenter controllerCenter3 = ControllerCenter.this;
                        sb3.append(controllerCenter3.networkSwitchToString(controllerCenter3.mNetWorkSwitchState));
                        LocalLog.l(ControllerCenter.TAG, sb3.toString());
                    } else if (z11) {
                        ControllerCenter.this.mRedisabledMobNet = true;
                        ControllerCenter.access$1408(ControllerCenter.this);
                        ControllerCenter.this.mWorkHandler.sendEmptyMessage(19);
                        ControllerCenter.this.mWakeLock.acquire(5000L);
                        ControllerCenter.this.mRecord.recoardEvent("redisableMobileData", System.currentTimeMillis(), true);
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("CONNECTIVITY_ACTION: in deep sleep, redisable mobileNet. cnt=");
                        sb4.append(ControllerCenter.this.mCntDisableNetUnexpected);
                        sb4.append(", netWorkType=");
                        ControllerCenter controllerCenter4 = ControllerCenter.this;
                        sb4.append(controllerCenter4.networkTypeToString(controllerCenter4.mNetWorkType));
                        sb4.append(", netWorkSwitchState=");
                        ControllerCenter controllerCenter5 = ControllerCenter.this;
                        sb4.append(controllerCenter5.networkSwitchToString(controllerCenter5.mNetWorkSwitchState));
                        LocalLog.l(ControllerCenter.TAG, sb4.toString());
                    } else if (z12) {
                        ControllerCenter.this.mRedisabledWifi = true;
                        ControllerCenter.access$1408(ControllerCenter.this);
                        ControllerCenter.this.mWorkHandler.sendEmptyMessage(20);
                        ControllerCenter.this.mWakeLock.acquire(5000L);
                        ControllerCenter.this.mRecord.recoardEvent("redisableWifi", System.currentTimeMillis(), true);
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("CONNECTIVITY_ACTION: in deep sleep, redisable wifi. cnt=");
                        sb5.append(ControllerCenter.this.mCntDisableNetUnexpected);
                        sb5.append(", netWorkType=");
                        ControllerCenter controllerCenter6 = ControllerCenter.this;
                        sb5.append(controllerCenter6.networkTypeToString(controllerCenter6.mNetWorkType));
                        sb5.append(", netWorkSwitchState=");
                        ControllerCenter controllerCenter7 = ControllerCenter.this;
                        sb5.append(controllerCenter7.networkSwitchToString(controllerCenter7.mNetWorkSwitchState));
                        LocalLog.l(ControllerCenter.TAG, sb5.toString());
                    }
                }
                if (ControllerCenter.this.mStatus != 0 || SystemClock.uptimeMillis() <= ControllerCenter.this.mTimeChgToActive + ControllerCenter.DELAY_ALLOW_RCD_FROM_ACTIVE) {
                    String str = "netUnconnected";
                    if (z12) {
                        str = "wifiConnected";
                    } else if (z11) {
                        str = "mobileConnected";
                    }
                    DeepSleepRecord deepSleepRecord = ControllerCenter.this.mRecord;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("CONNECTIVITY_ACTION:");
                    sb6.append(str);
                    sb6.append(", status:");
                    ControllerCenter controllerCenter8 = ControllerCenter.this;
                    sb6.append(controllerCenter8.statusToString(controllerCenter8.mStatus));
                    deepSleepRecord.recoardEvent(sb6.toString(), System.currentTimeMillis(), true);
                }
                if (z10 && ControllerCenter.this.mIsNeedSendMaintenanceRestoredBroadcast) {
                    ControllerCenter.this.sendDeepSleepMaintenceRestoreBroadcast();
                    ControllerCenter.this.mIsNeedSendMaintenanceRestoredBroadcast = false;
                }
                StringBuilder sb7 = new StringBuilder();
                sb7.append("CONNECTIVITY_ACTION: mobNetConnected=");
                sb7.append(z11);
                sb7.append(", wifiConnected=");
                sb7.append(z12);
                sb7.append(", connected=");
                sb7.append(z10);
                sb7.append(", status=");
                ControllerCenter controllerCenter9 = ControllerCenter.this;
                sb7.append(controllerCenter9.statusToString(controllerCenter9.mStatus));
                sb7.append(", extremeSleepStatus=");
                sb7.append(ControllerCenter.this.mUtils.getExtremeSleepStatus());
                sb7.append(", disableNetAllowed=");
                sb7.append(ControllerCenter.this.mDisableNetAllowed);
                sb7.append(", deepSleepRestore=");
                sb7.append(intent.getBooleanExtra("deepsleeprestore", false));
                sb7.append(", deepSleepDisable=");
                sb7.append(intent.getBooleanExtra("deepsleepdisable", false));
                LocalLog.a(ControllerCenter.TAG, sb7.toString());
                return;
            }
            if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                LocalLog.a(ControllerCenter.TAG, "WIFI_STATE_CHANGED_ACTION: getWifiState = " + ((WifiManager) ControllerCenter.this.mContext.getSystemService(ThermalPolicy.KEY_WIFI)).getWifiState() + ", deepSleepRestore=" + intent.getBooleanExtra("deepsleeprestore", false) + ", deepSleepDisable=" + intent.getBooleanExtra("deepsleepdisable", false));
                return;
            }
            if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (networkInfo == null) {
                    LocalLog.a(ControllerCenter.TAG, "NETWORK_STATE_CHANGED_ACTION: NetworkInfo is null");
                    return;
                }
                LocalLog.a(ControllerCenter.TAG, "NETWORK_STATE_CHANGED_ACTION: DetailedState = " + networkInfo.getDetailedState() + ", deepSleepRestore=" + intent.getBooleanExtra("deepsleeprestore", false) + ", deepSleepDisable=" + intent.getBooleanExtra("deepsleepdisable", false));
                return;
            }
            if (ControllerCenter.ACTION_AUTO_STOP_TRAFFIC.equals(action)) {
                ControllerCenter.this.autoStopTrafficTimeout();
                return;
            }
            if ("android.intent.action.USER_PRESENT".equals(action)) {
                ControllerCenter.this.mIsUserPresent = true;
                if (ControllerCenter.this.mIsWakeFromDeepSleep && ControllerCenter.this.mUtils.getDeepSleepEnterCount() > 0) {
                    ControllerCenter.this.mIsWakeFromDeepSleep = false;
                    ControllerCenter.this.mUtils.saveDeepSleepPredictFailed(true);
                    if (ControllerCenter.this.mUtils.getDeepSleepEnterCount() == 1) {
                        ControllerCenter.this.mUtils.saveDeepSleepFirstFailTime(Long.valueOf(System.currentTimeMillis()));
                        ControllerCenter.this.mUtils.saveDeepSleepLastFailTime(Long.valueOf(System.currentTimeMillis()));
                    } else {
                        ControllerCenter.this.mUtils.saveDeepSleepLastFailTime(Long.valueOf(System.currentTimeMillis()));
                    }
                }
                LocalLog.a(ControllerCenter.TAG, "ACTION_USER_PRESENT");
                return;
            }
            if (ControllerCenter.ACTION_CAL_PREDICT_CHECK.equals(action)) {
                LocalLog.a(ControllerCenter.TAG, "begin checking predict");
                ControllerCenter.this.handleDeepSleepCheck();
                return;
            }
            if (ControllerCenter.ACTION_MUSIC_MONITOR.equals(action)) {
                if (ControllerCenter.this.mUtils.getExtremeSleepStatus() && ControllerCenter.this.mSystemPlaybackStatus) {
                    LocalLog.b(ControllerCenter.TAG, "ExDeepsleep: play music");
                    ControllerCenter.this.mSystemPlaybackStatus = false;
                    ControllerCenter.this.extremeSleepStatus();
                    return;
                }
                return;
            }
            if (ControllerCenter.ACTION_FORCESTOP_MONITOR.equals(action)) {
                SuperSleepStatsHelper.getInstance(ControllerCenter.this.mContext).forcestopApps();
                return;
            }
            if (ControllerCenter.ACTION_BATTERY_CURVE_START_MONITOR.equals(action)) {
                SuperSleepStatsHelper.getInstance(ControllerCenter.this.mContext).batteryCurveOptimize(1);
                return;
            }
            if (ControllerCenter.ACTION_BATTERY_CURVE_END_MONITOR.equals(action)) {
                SuperSleepStatsHelper.getInstance(ControllerCenter.this.mContext).batteryCurveOptimize(0);
                return;
            }
            if ("android.os.action.DEVICE_IDLE_MODE_CHANGED".equals(action)) {
                boolean isDeviceIdleMode = ControllerCenter.this.mPowerManager.isDeviceIdleMode();
                if (isDeviceIdleMode && !ControllerCenter.this.mEverEnterDeepIdle) {
                    ControllerCenter.this.mEverEnterDeepIdle = true;
                    if (ControllerCenter.this.mStatus == 4) {
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("ACTION_DEVICE_IDLE_MODE_CHANGED. first enter deepidle while in deepsleep. NetWorkSwitch=");
                        ControllerCenter controllerCenter10 = ControllerCenter.this;
                        sb8.append(controllerCenter10.networkSwitchToString(controllerCenter10.mNetWorkSwitchState));
                        LocalLog.a(ControllerCenter.TAG, sb8.toString());
                        if (ControllerCenter.this.mNetWorkSwitchState != 0) {
                            ControllerCenter.this.setStartTrafficNextDozeMaintenanceAlarm();
                        }
                    }
                }
                LocalLog.a(ControllerCenter.TAG, "ACTION_DEVICE_IDLE_MODE_CHANGED. isDeviceIdle=" + isDeviceIdleMode);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class SignicantMotionListener extends TriggerEventListener {
        private SignicantMotionListener() {
        }

        @Override // android.hardware.TriggerEventListener
        public void onTrigger(TriggerEvent triggerEvent) {
            ControllerCenter.this.mWakeLock.acquire(5000L);
            LocalLog.a(ControllerCenter.TAG, "onTrigger");
            ControllerCenter.access$5608(ControllerCenter.this);
            ControllerCenter.this.mWorkHandler.sendEmptyMessage(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class StatusObserver extends ContentObserver {
        public StatusObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            synchronized (ControllerCenter.this.mLock) {
                ControllerCenter.this.mWakeLockProvider.acquire();
                LocalLog.a(ControllerCenter.TAG, "status observer onChange");
                boolean z11 = ControllerCenter.this.mUtils.getDeepSleepStatus() == 1;
                boolean z12 = ControllerCenter.this.mStatus == 4;
                if (z11 != z12) {
                    LocalLog.a(ControllerCenter.TAG, "isDeepSleep=" + z11 + ", isActualDeepSleep=" + z12);
                    ControllerCenter.this.mUtils.saveDeepSleepStatus(z12 ? 1 : 0);
                }
                ControllerCenter.this.mWakeLockProvider.release();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class WorkHandler extends Handler {
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            synchronized (ControllerCenter.this.mLock) {
                ControllerCenter.this.mWakeLock.acquire(5000L);
                switch (message.what) {
                    case 1:
                        ControllerCenter.this.stepDeepSleepLocked();
                        break;
                    case 4:
                        ControllerCenter.this.unregSignificantMotion();
                        ControllerCenter.this.signicantMotionDetectedlocked();
                        break;
                    case 5:
                        ControllerCenter.this.interruptHandleLocked();
                        break;
                    case 6:
                        Bundle data = message.getData();
                        if (data != null && ControllerCenter.this.mStatus == 4) {
                            ControllerCenter.this.mPkgRestoreNet = data.getString(DeviceDomainManager.ARG_PKG);
                        }
                        ControllerCenter.this.mIsMaintenanceRestored = true;
                        ControllerCenter.this.mUtils.saveDeepSleepIsMaintenceReStored(ControllerCenter.this.mIsMaintenanceRestored);
                        ControllerCenter.this.restoreNetworkReqLocked();
                        break;
                    case 7:
                        ControllerCenter.this.appTrafficChangeLocked(message.getData());
                        break;
                    case 8:
                        LocalLog.a(ControllerCenter.TAG, "MSG_BOOT_COMPLETE");
                        ControllerCenter.this.bootCompleteLocked();
                        break;
                    case 9:
                        LocalLog.a(ControllerCenter.TAG, "MSG_START_CLEAR_APPS");
                        if (ControllerCenter.this.mNeedMorningClear) {
                            ControllerCenter.this.mNeedMorningClear = false;
                            ControllerCenter.this.startMorningClearApps();
                            break;
                        } else if (ControllerCenter.this.mNeedClearApps) {
                            ControllerCenter.this.mNeedClearApps = false;
                            ControllerCenter.this.startClearApps();
                            break;
                        }
                        break;
                    case 10:
                        ControllerCenter.this.trafficMonitorLocked();
                        break;
                    case 11:
                        ControllerCenter controllerCenter = ControllerCenter.this;
                        controllerCenter.setWifiEnable(controllerCenter.mWifiEnalbe, "msgHandle");
                        break;
                    case 12:
                        ControllerCenter.this.updateWhiteList();
                        break;
                    case 13:
                        ControllerCenter.this.uploadAiStatistics();
                        break;
                    case 15:
                        ControllerCenter.this.quickEnterDeepsleep();
                        break;
                    case 16:
                        ControllerCenter.this.showNetOffNotifyRightNow();
                        break;
                    case 17:
                        ControllerCenter.this.setAirplaneModeOn(message.getData().getBoolean("AirEnable"));
                        break;
                    case 18:
                        ControllerCenter.this.testForceGetAIPredictReal();
                        break;
                    case 19:
                        if (ControllerCenter.this.mStatus == 4) {
                            ControllerCenter.this.setMobileDataEnable(false, "redisable");
                            break;
                        } else {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("REDISABLE MOBILEDATA: status=");
                            ControllerCenter controllerCenter2 = ControllerCenter.this;
                            sb2.append(controllerCenter2.statusToString(controllerCenter2.mStatus));
                            LocalLog.l(ControllerCenter.TAG, sb2.toString());
                            break;
                        }
                    case 20:
                        if (ControllerCenter.this.mStatus == 4) {
                            ControllerCenter.this.setWifiEnable(false, "redisable");
                            break;
                        } else {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("REDISABLE WIFI: status=");
                            ControllerCenter controllerCenter3 = ControllerCenter.this;
                            sb3.append(controllerCenter3.statusToString(controllerCenter3.mStatus));
                            LocalLog.l(ControllerCenter.TAG, sb3.toString());
                            break;
                        }
                }
                if (!ControllerCenter.this.mWorkHandler.hasMessages(1) && ControllerCenter.this.mWakeLock.isHeld()) {
                    ControllerCenter.this.mWakeLock.release();
                }
            }
        }

        private WorkHandler(Looper looper) {
            super(looper);
        }
    }

    private ControllerCenter(final Context context) {
        this.mStatus = 0;
        this.mListTraffic = new ArrayList();
        this.mListTrafficStartTime = new ArrayList();
        this.mListTrafficAutoStop = new ArrayList();
        this.mSignicantMotionListener = new SignicantMotionListener();
        this.mIsMaintenanceRestored = false;
        this.mContext = context;
        this.mWifiManager = (WifiManager) context.getSystemService(ThermalPolicy.KEY_WIFI);
        this.mAlarmManager = (AlarmManager) context.getSystemService("alarm");
        this.mTelephonyManager = TelephonyManager.from(context);
        this.mGuardElfDataManager = GuardElfDataManager.d(context);
        this.mConfigUpdateUtil = ConfigUpdateUtil.n(context);
        this.mUploadDataUtil = UploadDataUtil.S0(context);
        this.mUtils = DeepSleepUtils.getInstance(context);
        PowerManager powerManager = (PowerManager) this.mContext.getSystemService("power");
        this.mPowerManager = powerManager;
        PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(1, "deep:sleep");
        this.mWakeLock = newWakeLock;
        newWakeLock.setReferenceCounted(false);
        this.mWakeLockProvider = this.mPowerManager.newWakeLock(1, "deepsleep:provider");
        this.mWakeLockStartClear = this.mPowerManager.newWakeLock(1, "start:clear");
        this.mWakeLockNet = this.mPowerManager.newWakeLock(1, "deepSleep:Net");
        this.mIsScreenOff = !this.mPowerManager.isInteractive();
        this.mIsMaintenanceRestored = this.mUtils.getDeepSleepIsMaintenceReStored();
        LocalLog.a(TAG, "init: isScreenOff=" + this.mIsScreenOff);
        this.mListTraffic = this.mGuardElfDataManager.e("app_traffic_deepsleep.xml");
        this.mListTrafficStartTime = this.mGuardElfDataManager.e("app_traffic_deepsleep_start_time.xml");
        List<String> e10 = this.mGuardElfDataManager.e("app_traffic_autostop_deepsleep.xml");
        this.mListTrafficAutoStop = e10;
        if (!e10.isEmpty()) {
            setAutoStopTrafficAlarm();
        }
        LocalLog.a(TAG, "init: listTraffic=" + this.mListTraffic + ", listTrafficStartTime=" + this.mListTrafficStartTime + ", listTrafficAutoStop=" + this.mListTrafficAutoStop);
        HandlerThread handlerThread = new HandlerThread("deepsleep");
        this.mDataThread = new HandlerThread("aiSleepData");
        handlerThread.start();
        this.mDataThread.start();
        initDeepThinker();
        this.mWorkHandler = new WorkHandler(handlerThread.getLooper());
        this.mRecord = new DeepSleepRecord(context, handlerThread.getLooper());
        this.mStatusObserver = new StatusObserver(this.mWorkHandler);
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DEEPSLEEP_STEP);
        intentFilter.addAction(ACTION_APP_TRAFFIC_START_TIMEOUT);
        intentFilter.addAction(ACTION_TRAFFIC_MONITOR);
        intentFilter.addAction(ACTION_AUTO_STOP_TRAFFIC);
        intentFilter.addAction(ACTION_CAL_PREDICT_CHECK);
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction(ACTION_MUSIC_MONITOR);
        intentFilter.addAction(ACTION_FORCESTOP_MONITOR);
        intentFilter.addAction(ACTION_BATTERY_CURVE_START_MONITOR);
        intentFilter.addAction(ACTION_BATTERY_CURVE_END_MONITOR);
        intentFilter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
        this.mWorkHandler.post(new Runnable() { // from class: com.oplus.deepsleep.a
            @Override // java.lang.Runnable
            public final void run() {
                ControllerCenter.this.lambda$new$0(context, intentFilter);
            }
        });
        this.mStatus = this.mUtils.getLocalStatus();
        this.mNetWorkType = this.mUtils.getLocalNetWorkType();
        this.mNetWorkSwitchState = this.mUtils.getLocalNetWorkSwitchState();
        this.mPredictedSleepStartTime = this.mUtils.getPredictStartTime();
        this.mPredictedSleepEndTime = this.mUtils.getPredictEndTime();
        if (this.mStatus != 0) {
            this.mWorkHandler.sendEmptyMessage(5);
        }
        this.mWorkHandler.sendEmptyMessageDelayed(12, 10000L);
        checkDeepSleepResetAirPlane();
        try {
            Class<?> cls = Class.forName("com.oplus.nwpower.OSysNetControlManager");
            sOSysNetControlManager = cls;
            sOSysNetControlManagerNewInstance = cls.getMethod("getInstance", new Class[0]);
            Class<?> cls2 = sOSysNetControlManager;
            Class<?> cls3 = Boolean.TYPE;
            sSetDataEnabled = cls2.getMethod("setDataEnabled", cls3);
            sSetWifiEnabled = sOSysNetControlManager.getMethod("setWifiEnabled", cls3);
        } catch (Exception e11) {
            LocalLog.a(TAG, "setDataEnabled | setWifiEnabled error = " + e11.toString());
        }
        if (this.mIsScreenOff) {
            this.mEverEnterDeepIdle = this.mPowerManager.isDeviceIdleMode();
        }
    }

    static /* synthetic */ int access$1408(ControllerCenter controllerCenter) {
        int i10 = controllerCenter.mCntDisableNetUnexpected;
        controllerCenter.mCntDisableNetUnexpected = i10 + 1;
        return i10;
    }

    static /* synthetic */ int access$5608(ControllerCenter controllerCenter) {
        int i10 = controllerCenter.mCntMotionDetected;
        controllerCenter.mCntMotionDetected = i10 + 1;
        return i10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appTrafficChangeLocked(Bundle bundle) {
        boolean z10;
        boolean z11;
        if (bundle == null) {
            LocalLog.a(TAG, "appTrafficChange: no data.");
            return;
        }
        String string = bundle.getString(DeviceDomainManager.ARG_PKG);
        String string2 = bundle.getString("req");
        String string3 = bundle.getString("job");
        boolean z12 = false;
        boolean z13 = bundle.getBoolean("autoStop", false);
        if (string == null || string2 == null) {
            return;
        }
        if ("start".equals(string2) && !isWhiteListPkg(string)) {
            LocalLog.a(TAG, "appTrafficChangeLocked: not allow pkg=" + string);
            return;
        }
        String str = string3 != null ? string + ":" + string3 : string;
        boolean z14 = true;
        if ("start".equals(string2)) {
            if (this.mListTraffic.contains(str)) {
                z11 = false;
                z14 = false;
            } else {
                this.mListTraffic.add(str);
                this.mListTrafficStartTime.add(str + "!" + System.currentTimeMillis());
                if (this.mListTraffic.size() == 1) {
                    z11 = true;
                } else {
                    z11 = true;
                    z14 = false;
                }
            }
            if (z13) {
                autoStopTrafficStart(string);
            }
            z10 = false;
            z12 = z14;
        } else if ("stop".equals(string2)) {
            if (this.mListTraffic.contains(str)) {
                this.mListTraffic.remove(str);
                z10 = this.mListTraffic.isEmpty();
                rmvListTrafficStartTime(string, str);
                z11 = true;
            } else {
                z10 = false;
                z11 = false;
            }
            for (int size = this.mListTraffic.size() - 1; size >= 0; size--) {
                String str2 = this.mListTraffic.get(size);
                String[] split = str2.split(":");
                if (split.length != 0 && split[0].equals(str)) {
                    this.mListTraffic.remove(size);
                    if (this.mListTraffic.isEmpty()) {
                        z10 = true;
                    }
                    rmvListTrafficStartTime(string, str2);
                    z11 = true;
                }
            }
        } else {
            z10 = false;
            z11 = false;
        }
        if (!this.mListTraffic.isEmpty() && LocalLog.f()) {
            LocalLog.a(TAG, "list: " + this.mListTraffic);
        }
        if (z11) {
            this.mGuardElfDataManager.k(this.mListTraffic, "app_traffic_deepsleep.xml");
            this.mGuardElfDataManager.k(this.mListTrafficStartTime, "app_traffic_deepsleep_start_time.xml");
        }
        if (z12) {
            LocalLog.a(TAG, "trun to start. status = " + statusToString(this.mStatus));
            appTraffitcTurnToStartLocked(string, str);
        }
        if (z10) {
            LocalLog.a(TAG, "trun to stop. status = " + statusToString(this.mStatus));
            appTraffitcTurnToStopLocked();
        }
    }

    private void appTraffitcTurnToStartLocked(String str, String str2) {
        if (this.mStatus == 5) {
            canceAppTrafficStartTimeoutAlarm();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appTraffitcTurnToStopLocked() {
        LocalLog.a(TAG, "appTraffitcTurnToStop: status = " + statusToString(this.mStatus));
        if (this.mUtils.getExtremeSleepStatus() && this.mWaitingTrafficStop) {
            changeStatus(0);
            extremeSleepStatus();
            return;
        }
        int i10 = this.mStatus;
        if (i10 == 3) {
            cancelTrafficMonitorAlarm();
            changeStatus(2);
            stepDeepSleepLocked();
            calcWaittingTrafficStopDuration();
            return;
        }
        if (i10 == 5) {
            cancelTrafficMonitorAlarm();
            disableNetWork(false);
            changeStatus(4);
            calcAppTrafficDuration();
        }
    }

    private void autoStopTrafficStart(String str) {
        if (!this.mListTrafficAutoStop.contains(str)) {
            this.mListTrafficAutoStop.add(str);
            this.mGuardElfDataManager.k(this.mListTrafficAutoStop, "app_traffic_autostop_deepsleep.xml");
        }
        setAutoStopTrafficAlarm();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void autoStopTrafficTimeout() {
        for (int size = this.mListTrafficAutoStop.size() - 1; size >= 0; size--) {
            String str = this.mListTrafficAutoStop.get(size);
            LocalLog.a(TAG, "autoStopTrafficTimeout. pkg = " + str);
            Message obtainMessage = this.mWorkHandler.obtainMessage();
            obtainMessage.what = 7;
            Bundle bundle = new Bundle();
            bundle.putString(DeviceDomainManager.ARG_PKG, str);
            bundle.putString("req", "stop");
            obtainMessage.setData(bundle);
            this.mWorkHandler.sendMessage(obtainMessage);
        }
        this.mListTrafficAutoStop.clear();
        this.mGuardElfDataManager.k(this.mListTrafficAutoStop, "app_traffic_autostop_deepsleep.xml");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bootCompleteLocked() {
        this.mListTraffic.clear();
        this.mListTrafficStartTime.clear();
        this.mGuardElfDataManager.k(this.mListTraffic, "app_traffic_deepsleep.xml");
        this.mGuardElfDataManager.k(this.mListTrafficStartTime, "app_traffic_deepsleep_start_time.xml");
        this.mUtils.saveNetWorkTypeSettingProvider(0);
        this.mUtils.saveMorningClearTime(-64800000L);
        this.mUtils.saveLightClearTime(-3600000L);
        if (this.mUtils.getScreenoffTimeAiDcs() != 0) {
            this.mUtils.saveScreenoffTimeAiDcs(0L);
        }
        this.mRecord.recoardEvent("bootComplete:mIsScreenOff " + String.valueOf(this.mIsScreenOff), System.currentTimeMillis(), true);
        if (this.mIsScreenOff) {
            return;
        }
        turnToStatusActiveLocked();
        uploadDeepSleepstatistics(true, false, "BootComplete");
    }

    private float calRecentFailed() {
        int i10 = !this.mUtils.getDeepSleepCheckStatus(1) ? 1 : 0;
        if (!this.mUtils.getDeepSleepCheckStatus(2)) {
            i10++;
        }
        if (!this.mUtils.getDeepSleepCheckStatus(3)) {
            i10++;
        }
        if (!this.mUtils.getDeepSleepCheckStatus(4)) {
            i10++;
        }
        if (!this.mUtils.getDeepSleepCheckStatus(5)) {
            i10++;
        }
        if (!this.mUtils.getDeepSleepCheckStatus(6)) {
            i10++;
        }
        if (!this.mUtils.getDeepSleepCheckStatus(7)) {
            i10++;
        }
        this.mUtils.saveRecentFailCountSettingProvider(i10);
        float f10 = i10 / 7.0f;
        LocalLog.a(TAG, "failedRatio is :" + f10);
        return f10;
    }

    private void calcAppTrafficDuration() {
        long appTrafficEnterElapsedTime = this.mUtils.getAppTrafficEnterElapsedTime();
        if (appTrafficEnterElapsedTime == 0) {
            LocalLog.a(TAG, "calcAppTrafficDuration: appTrafficEnterTime is 0!!!");
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime() - appTrafficEnterElapsedTime;
        if (elapsedRealtime <= 0) {
            LocalLog.a(TAG, "calcAppTrafficDuration: duration =" + elapsedRealtime);
            return;
        }
        long appTrafficDuration = this.mUtils.getAppTrafficDuration() + elapsedRealtime;
        this.mUtils.saveAppTrafficDuration(appTrafficDuration);
        LocalLog.a(TAG, "calcAppTrafficDuration: durationTotal =" + appTrafficDuration);
    }

    private void calcDisNetDuration() {
        long disNetElapsedTime = this.mUtils.getDisNetElapsedTime();
        if (disNetElapsedTime == 0) {
            LocalLog.a(TAG, "Dis Duration: disNetElapsedTime is 0!!!");
            return;
        }
        long currentTimeMillis = System.currentTimeMillis() - disNetElapsedTime;
        if (currentTimeMillis <= 0) {
            LocalLog.a(TAG, "Dis Duration: duration =" + currentTimeMillis);
            return;
        }
        long disNetDuration = this.mUtils.getDisNetDuration() + currentTimeMillis;
        this.mUtils.saveDisNetDuration(disNetDuration);
        LocalLog.a(TAG, "Dis Duration: durationTotal =" + disNetDuration);
    }

    private void calcWaittingTrafficStopDuration() {
        long waitTrafficStopEnterTime = this.mUtils.getWaitTrafficStopEnterTime();
        if (waitTrafficStopEnterTime == 0) {
            LocalLog.a(TAG, "calcWaittingTrafficStopDuration: waitTrafficStopEnterTime is 0!!!");
            return;
        }
        long currentTimeMillis = System.currentTimeMillis() - waitTrafficStopEnterTime;
        if (currentTimeMillis <= 0) {
            LocalLog.a(TAG, "calcWaittingTrafficStopDuration: duration =" + currentTimeMillis);
            return;
        }
        long waitTrafficStopDuration = this.mUtils.getWaitTrafficStopDuration() + currentTimeMillis;
        this.mUtils.saveWaitTrafficStopDuration(waitTrafficStopDuration);
        LocalLog.a(TAG, "calcWaittingTrafficStopDuration: durationTotal =" + waitTrafficStopDuration);
    }

    private void canceAppTrafficStartTimeoutAlarm() {
        LocalLog.a(TAG, "canceAppTrafficStartTimeoutAlarm");
        Intent intent = new Intent(ACTION_APP_TRAFFIC_START_TIMEOUT);
        intent.setPackage("com.oplus.battery");
        this.mAlarmManager.cancel(PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL));
    }

    private void cancelAlarm() {
        LocalLog.a(TAG, "cancelAlarm");
        Intent intent = new Intent(ACTION_DEEPSLEEP_STEP);
        intent.setPackage("com.oplus.battery");
        this.mAlarmManager.cancel(PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL));
        this.mUtils.saveNextAlarmTime(0L);
    }

    private void cancelStartTrafficNextDozeMaintenanceAlarm() {
        if (this.mTriggerForMaintence) {
            LocalLog.a(TAG, "cancelStartTrafficNextDozeMaintenanceAlarm: mTriggerForMaintence = " + this.mTriggerForMaintence);
            return;
        }
        LocalLog.a(TAG, "cancelStartTrafficNextDozeMaintenanceAlarm");
        Intent intent = new Intent(ACTION_START_TRAFFIC_NEXT_DOZE_MAINTENANCE);
        intent.setPackage("com.oplus.battery");
        this.mAlarmManager.cancel(PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL));
    }

    private void cancelTrafficMonitorAlarm() {
        LocalLog.a(TAG, "cancelTrafficMonitorAlarm");
        Intent intent = new Intent(ACTION_TRAFFIC_MONITOR);
        intent.setPackage("com.oplus.battery");
        this.mAlarmManager.cancel(PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL));
        this.mUtils.saveTrafficMonitorTime(0L);
    }

    private void changeStatus(int i10) {
        int i11;
        if (i10 == 2 || i10 == 3 || i10 == 4 || i10 == 5 || (i10 == 0 && (i11 = this.mStatus) != 0 && i11 != 1)) {
            this.mRecord.recoardEvent("status to " + statusToString(i10), System.currentTimeMillis(), true);
        }
        this.mStatus = i10;
        this.mUtils.saveLocalStatus(i10);
        if (this.mUtils.getExtremeSleepStatus()) {
            this.mUtils.setInactiveStatus(i10);
        }
        if (i10 == 0) {
            this.mTimeChgToActive = SystemClock.uptimeMillis();
        }
        LocalLog.l(TAG, "status change to " + statusToString(i10));
    }

    private void checkDeepSleepResetAirPlane() {
        if (this.mUtils.getDeepSleepResetAirPlaneStatus() && this.mUtils.getAirPlaneModeStatus()) {
            this.mUtils.setAirPlaneModeStatus(false);
            this.mUtils.setDeepSleepResetAirPlaneStatus(false);
        }
    }

    private long convertDoubleTime(double d10, long j10) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j10);
        calendar.set(13, 0);
        calendar.set(14, 0);
        int i10 = (int) (60.0d * d10);
        int i11 = i10 / 60;
        int i12 = i10 % 60;
        calendar.set(11, i11);
        calendar.set(12, i12);
        if (i11 >= HOURS_ONE_DAY) {
            LocalLog.a(TAG, "convertDoubleTime: teime error. time=" + d10 + ", hour=" + i11 + ", minute=" + i12);
            return -1L;
        }
        return calendar.getTimeInMillis();
    }

    private List<String> determineNetWorkWhiteUidList() {
        List<String> u7 = ConfigUpdateUtil.n(this.mContext).f() ? ConfigUpdateUtil.n(this.mContext).u() : getAssetsXMLValue();
        ArrayList arrayList = new ArrayList();
        for (int i10 = 0; i10 < u7.size(); i10++) {
            String str = u7.get(i10);
            if (str.contains("mcs") || ((str.contains("sos") && getEarthquakeWarningSwitchStatus()) || (str.contains("findmyphone") && getFindPhoneSwitchStatus()))) {
                int P = CommonUtil.P(this.mContext, str);
                arrayList.add(String.valueOf(P));
                LocalLog.a(TAG, "NetworkDisableWhiteList: uid = " + P + ", pkgName = " + str);
            }
        }
        return arrayList;
    }

    private void disableNetWork(boolean z10) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.mCntDisableNetUnexpected = 0;
        LocalLog.l(TAG, "disableNetWork:  netWorkSwitch=" + networkSwitchToString(this.mNetWorkSwitchState));
        if (z10) {
            this.mNeedMorningClear = true;
            this.mNeedClearApps = false;
        } else {
            this.mNeedClearApps = true;
            this.mNeedMorningClear = false;
        }
        int i10 = this.mNetWorkSwitchState;
        if (i10 == 3) {
            this.mUtils.savePropNetWorkRestoreTime(0L);
            this.mUtils.savePropNetWorkDisableTime(elapsedRealtime);
            this.mUtils.saveNetWorkDisableStateSettingProvider(1);
            setMobileDataEnable(false, "disable");
            setWifiEnable(false, "disable");
            this.mWakeLockNet.acquire(3000L);
        } else if (i10 == 1) {
            this.mUtils.savePropNetWorkRestoreTime(0L);
            this.mUtils.savePropNetWorkDisableTime(elapsedRealtime);
            this.mUtils.saveNetWorkDisableStateSettingProvider(1);
            setWifiEnable(false, "disable");
            this.mWakeLockNet.acquire(3000L);
        } else if (i10 == 2) {
            this.mUtils.savePropNetWorkRestoreTime(0L);
            this.mUtils.savePropNetWorkDisableTime(elapsedRealtime);
            this.mUtils.saveNetWorkDisableStateSettingProvider(1);
            setMobileDataEnable(false, "disable");
            this.mWakeLockNet.acquire(3000L);
        }
        if (this.mNetWorkSwitchState != 0) {
            setStartTrafficNextDozeMaintenanceAlarm();
        }
        if (this.mNetWorkType == 0) {
            if (this.mNeedMorningClear) {
                this.mNeedMorningClear = false;
                startMorningClearApps();
            } else if (this.mNeedClearApps) {
                this.mNeedClearApps = false;
                startClearApps();
            }
        }
        this.mUtils.saveLocalNetWorkSwitchState(this.mNetWorkSwitchState);
        this.mUtils.saveLocalNetWorkType(this.mNetWorkType);
        this.mUtils.saveLocalNetWorkTypeBak(this.mNetWorkType);
        this.mUtils.saveNetWorkTypeSettingProvider(this.mNetWorkType);
        this.mUtils.saveDisNetElapsedTime(System.currentTimeMillis());
        boolean z11 = !this.mUtils.getAirPlaneModeStatus();
        if (this.mConfigUpdateUtil.j() && z11) {
            this.mWakeLockNet.acquire(10000L);
            setAirplaneModeDelay(true, 5000);
        }
    }

    private void enterDeepSleepLocked() {
        if (this.mStatus != 2) {
            LocalLog.l(TAG, "enterDeepSleep: ignore!!!!!!!! status = " + statusToString(this.mStatus));
            return;
        }
        if (this.mPowerManager.isInteractive()) {
            LocalLog.l(TAG, "enterDeepSleep: screen on! turn to active!");
            turnToStatusActiveLocked();
            return;
        }
        LocalLog.a(TAG, "enterDeepSleep");
        long currentTimeMillis = System.currentTimeMillis();
        long j10 = this.mPredictedSleepEndTime - RESERVED_EXIT_DEEPSLEEP_TIME;
        if (j10 <= currentTimeMillis) {
            LocalLog.l(TAG, "enterDeepSleep: ignore. expetedExitDeepsleepTime = " + j10 + ", now = " + currentTimeMillis);
            DeepSleepRecord deepSleepRecord = this.mRecord;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("ignore enter DeepSleep. expetedExitDeepsleepTime=");
            sb2.append(printTimeInMillis(j10));
            deepSleepRecord.recoardEvent(sb2.toString(), System.currentTimeMillis(), true);
            turnToStatusActiveLocked();
            return;
        }
        this.mListTraffic.clear();
        this.mListTrafficStartTime.clear();
        schedueAlarm(j10, true);
        changeStatus(4);
        this.mUtils.saveDeepSleepEnterCount(this.mUtils.getDeepSleepEnterCount() + 1);
        this.mUtils.saveDeepSleepEntertTime(System.currentTimeMillis());
        this.mUtils.saveEverDisNet(true);
        disableNetWork(true);
        this.mUtils.saveDeepSleepStatus(1);
        this.mUtils.saveEverEnteredDeepsleep(true);
    }

    private void eventMapPutDurationKind(Map<String, String> map, StringBuilder sb2, String str, long j10) {
        sb2.setLength(0);
        CommonUtil.j(sb2, j10);
        map.put(str, sb2.toString());
    }

    private void eventMapPutTimeKind(Map<String, String> map, Calendar calendar, String str, long j10) {
        calendar.setTimeInMillis(j10);
        map.put(str, calendar.getTime().toString());
    }

    private void extremeEnterDeepSleepLocked() {
        if (this.mStatus != 2) {
            LocalLog.a(TAG, "enterDeepSleep: ignore!!!!!!!! status = " + statusToString(this.mStatus));
            return;
        }
        if (this.mPowerManager.isInteractive()) {
            LocalLog.l(TAG, "enterDeepSleep: screen on! turn to active!");
            turnToStatusActiveLocked();
            return;
        }
        LocalLog.b(TAG, "enterDeepSleep");
        long currentTimeMillis = System.currentTimeMillis();
        schedueAlarm(currentTimeMillis + ((this.mPredictedSleepEndTime - 0) - currentTimeMillis), true);
        changeStatus(4);
        this.mUtils.saveDeepSleepEntertTime(System.currentTimeMillis());
        this.mUtils.saveEverDisNet(true);
        disableNetWork(true);
        this.mUtils.saveDeepSleepStatus(1);
        this.mUtils.saveEverEnteredDeepsleep(true);
    }

    private void extremeForecastSleepDurationNoAI() {
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.set(11, 0);
        calendar.set(12, 0);
        long timeInMillis = calendar.getTimeInMillis() - 1800000;
        calendar.set(11, 7);
        calendar.set(12, 0);
        long timeInMillis2 = calendar.getTimeInMillis();
        if (currentTimeMillis >= timeInMillis - 300000 && currentTimeMillis >= timeInMillis2 - 300000) {
            setPredictSleepTime(timeInMillis + 86400000, timeInMillis2 + 86400000);
        } else {
            setPredictSleepTime(timeInMillis, timeInMillis2);
        }
        calendar.setTimeInMillis(this.mPredictedSleepStartTime);
        if (LocalLog.f()) {
            LocalLog.a(TAG, "forecastExtremeSleepDurationNoAI: dft. start: " + calendar.getTime());
        }
        calendar.setTimeInMillis(this.mPredictedSleepEndTime);
        if (LocalLog.f()) {
            LocalLog.a(TAG, "forecastExtremeSleepDurationNoAI: dft. end: " + calendar.getTime());
        }
    }

    private boolean extremeGetNetWorkType() {
        boolean z10;
        boolean z11;
        int i10;
        int i11;
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        boolean z12 = true;
        boolean z13 = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (activeNetworkInfo != null) {
            int type = activeNetworkInfo.getType();
            z11 = type == 0;
            z10 = type == 1;
        } else {
            z10 = false;
            z11 = false;
        }
        boolean dataEnabled = this.mTelephonyManager.getDataEnabled();
        boolean isWifiEnabled = this.mWifiManager.isWifiEnabled();
        LocalLog.a(TAG, "extremeGetNetWorkType: connected=" + z13 + ", isMobile=" + z11 + ", isWifi=" + z10 + ", isMobileDataEnable=" + dataEnabled + ", isWifiEnable=" + isWifiEnabled);
        if (z10 && isWifiEnabled) {
            this.mNetWorkType = 1;
        } else if (z11 && dataEnabled) {
            this.mNetWorkType = 2;
        } else {
            this.mNetWorkType = 0;
        }
        if (dataEnabled && isWifiEnabled && this.mNetWorkType != 0) {
            this.mNetWorkSwitchState = 3;
        } else if (z11 && dataEnabled) {
            this.mNetWorkSwitchState = 2;
        } else if (isWifiEnabled) {
            this.mNetWorkSwitchState = 1;
        } else {
            this.mNetWorkSwitchState = 0;
        }
        int i12 = this.mNetWorkType;
        if (i12 != 1 ? !(i12 != 2 || ((i10 = this.mNetWorkSwitchState) != 0 && i10 != 1)) : !((i11 = this.mNetWorkSwitchState) != 0 && i11 != 2)) {
            z12 = false;
        }
        LocalLog.a(TAG, "extremeGetNetWorkType: netType = " + networkTypeToString(this.mNetWorkType) + ", netSiwtch =" + networkSwitchToString(this.mNetWorkSwitchState) + ", sucess=" + z12);
        return z12;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void extremeSleepStatus() {
        LocalLog.b(TAG, "enter ExDeepsleep");
        if (this.mUtils.isNavigationMode()) {
            LocalLog.b(TAG, "ExDeepsleep isNavigationMode");
            return;
        }
        if (((AudioManager) this.mContext.getSystemService("audio")).isMusicActive()) {
            this.mSystemPlaybackStatus = true;
            supersleepCancelAlarm();
            setMusicMonitorAlarm();
            LocalLog.b(TAG, "ExDeepsleep playback");
            return;
        }
        this.mWaitingTrafficStop = true;
        if (shouldWaittingTrafficStop()) {
            LocalLog.b(TAG, "ExDeepsleep shouldWaittingTrafficStop");
            supersleepCancelAlarm();
            return;
        }
        this.mWaitingTrafficStop = false;
        synchronized (this.mLock) {
            this.mWakeLock.acquire();
            if (this.mStatus != 0) {
                LocalLog.a(TAG, "ExDeepsleep onScreenOff: status= " + statusToString(this.mStatus));
                turnToStatusActiveLocked();
            }
            this.mListPkgPlayback.clear();
            this.mListPkgAudioIn.clear();
            this.mCntMotionDetected = 0;
            this.mCntPlaybackCheck = 0;
            this.mCntNavigatingCheck = 0;
            this.mCntTrafficCheck = 0;
            this.mPkgRestoreNet = null;
            if (isShortScreenOn()) {
                this.mWorkHandler.sendEmptyMessage(15);
            } else {
                schedueAlarm(System.currentTimeMillis() + 300000, true);
            }
            changeStatus(1);
            setForcestopAlarm();
            setBatterycurveStatus();
            resetStatisticsVal();
            this.mIsScreenOff = true;
            this.mIsUserPresent = false;
            this.mWakeLock.release();
        }
    }

    private void extremeStepStatusInactiveLocked(long j10) {
        LocalLog.a(TAG, "stepExtremeStatusInactiveLocked");
        forecastSleepDuration();
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis < this.mPredictedSleepStartTime - 300000) {
            LocalLog.a(TAG, "stepExtremeStatusInactiveLocked: set alarm to predictedSleepStartTime - SUPERSLEEP_SENSING_DURATION");
            schedueAlarm(System.currentTimeMillis() + ((this.mPredictedSleepStartTime - 300000) - currentTimeMillis), true);
        } else {
            if (currentTimeMillis < this.mPredictedSleepEndTime - 300000) {
                regSignificantMotion();
                schedueAlarm(System.currentTimeMillis() + j10, true);
                this.mLastTrafficTotal = this.mUtils.getTotalTraffic();
                this.mLastTrafficTimeStamp = System.currentTimeMillis();
                changeStatus(2);
                this.mUtils.saveEverEnteredSensing(true);
                return;
            }
            LocalLog.a(TAG, "stepExtremeStatusInactiveLocked: sleep duration error. turn to ACTIVE");
            changeStatus(0);
        }
    }

    private void forecastSleepDuration() {
        this.mIsAiForecast = false;
        if (forecastSleepDurationTestMode()) {
            return;
        }
        if (this.mUtils.getExtremeSleepStatus()) {
            extremeForecastSleepDurationNoAI();
        } else {
            if (forecastSleepDurationAI()) {
                return;
            }
            forecastSleepDurationNoAI();
        }
    }

    private boolean forecastSleepDurationAI() {
        List<Pair<Double, Double>> aISleepDuration = getAISleepDuration();
        if (aISleepDuration != null && !aISleepDuration.isEmpty()) {
            long currentTimeMillis = System.currentTimeMillis();
            List<Pair<Long, Long>> parseAISleepDuration = parseAISleepDuration(aISleepDuration, currentTimeMillis);
            if (parseAISleepDuration != null && !parseAISleepDuration.isEmpty()) {
                return selectAISleepDuration(parseAISleepDuration, currentTimeMillis);
            }
            if (LocalLog.f()) {
                LocalLog.a(TAG, "forecastSleepDurationAI: listSleepDuration is empty.");
            }
            return false;
        }
        if (LocalLog.f()) {
            LocalLog.a(TAG, "forecastSleepDurationAI: no listCluster.");
        }
        return false;
    }

    private void forecastSleepDurationNoAI() {
        long currentTimeMillis = System.currentTimeMillis();
        Random random = new Random();
        int nextInt = random.nextInt(59);
        int nextInt2 = random.nextInt(59);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.set(11, 0);
        calendar.set(12, nextInt);
        long timeInMillis = calendar.getTimeInMillis();
        calendar.set(11, 6);
        calendar.set(12, nextInt2);
        long timeInMillis2 = calendar.getTimeInMillis();
        if (currentTimeMillis >= timeInMillis - 1200000 && currentTimeMillis >= timeInMillis2 - 2400000) {
            setPredictSleepTime(timeInMillis + 86400000, timeInMillis2 + 86400000);
        } else {
            setPredictSleepTime(timeInMillis, timeInMillis2);
        }
        calendar.setTimeInMillis(this.mPredictedSleepStartTime);
        LocalLog.l(TAG, "forecastSleepDuration: dft. start: " + calendar.getTime());
        calendar.setTimeInMillis(this.mPredictedSleepEndTime);
        LocalLog.l(TAG, "forecastSleepDuration: dft. end: " + calendar.getTime());
    }

    private boolean forecastSleepDurationTestMode() {
        if (!this.mTestMode) {
            return false;
        }
        long j10 = this.mTestSleepStartTime;
        if (j10 == 0) {
            return false;
        }
        long j11 = this.mTestSleepEndTime;
        if (j11 == 0) {
            return false;
        }
        setPredictSleepTime(j10, j11);
        if (LocalLog.f()) {
            LocalLog.a(TAG, "forecastSleepDurationTestMode: test sleep start time: " + printTimeInMillis(this.mPredictedSleepStartTime));
            LocalLog.a(TAG, "forecastSleepDurationTestMode: test sleep end time: " + printTimeInMillis(this.mPredictedSleepEndTime));
        }
        if (this.mSimuAIPredict) {
            this.mIsAiForecast = true;
            LocalLog.a(TAG, "forecastSleepDurationTestMode: simu AI predict");
        }
        return true;
    }

    private List<Pair<Double, Double>> getAISleepDuration() {
        DeepSleepPredictResultWithPercentile deepSleepPredictResultWithPercentile;
        ArrayList arrayList = new ArrayList();
        try {
            if (this.mDeepThinkerManager == null) {
                initDeepThinker();
            }
            OplusDeepThinkerManager oplusDeepThinkerManager = this.mDeepThinkerManager;
            if (oplusDeepThinkerManager != null) {
                deepSleepPredictResultWithPercentile = oplusDeepThinkerManager.getDeepSleepPredictResultWithPercentile();
                LocalLog.a(TAG, "getAISleepDuration: predictResult is " + deepSleepPredictResultWithPercentile);
            } else {
                deepSleepPredictResultWithPercentile = null;
            }
        } catch (NumberFormatException unused) {
            this.mUtils.saveDeepSleepPredictException("NumberFormatException");
            LocalLog.a(TAG, "getAISleepDuration: NumberFormatException.");
            this.mRecord.recoardEvent("AIPredict NumberFormatException", System.currentTimeMillis(), true);
        }
        if (deepSleepPredictResultWithPercentile == null) {
            LocalLog.b(TAG, "getAISleepDuration: predictResult is null.");
            this.mRecord.recoardEvent("AIPredict return null", System.currentTimeMillis(), true);
            return null;
        }
        List<DeepSleepClusterWithPercentile> deepSleepClusterWithPercentiles = deepSleepPredictResultWithPercentile.getDeepSleepClusterWithPercentiles();
        if (deepSleepClusterWithPercentiles != null && !deepSleepClusterWithPercentiles.isEmpty()) {
            LocalLog.a(TAG, "getAISleepDuration: size=" + deepSleepClusterWithPercentiles.size());
            StringBuilder sb2 = new StringBuilder();
            for (int i10 = 0; i10 < deepSleepClusterWithPercentiles.size(); i10++) {
                DeepSleepClusterWithPercentile deepSleepClusterWithPercentile = deepSleepClusterWithPercentiles.get(i10);
                Double d10 = deepSleepClusterWithPercentile.getSleepTimePercentiles().get(Integer.valueOf(this.mUtils.getAISleepPercent()));
                Double d11 = deepSleepClusterWithPercentile.getWakeTimePercentiles().get(Integer.valueOf(this.mUtils.getAIWakePercent()));
                if (d10 != null && d11 != null) {
                    double doubleValue = d10.doubleValue();
                    double doubleValue2 = d11.doubleValue();
                    arrayList.add(new Pair(Double.valueOf(doubleValue), Double.valueOf(doubleValue2)));
                    sb2.append(doubleValue);
                    sb2.append(" : ");
                    sb2.append(doubleValue2);
                    sb2.append(". ");
                }
                LocalLog.b(TAG, "getAISleepDuration error startTime = " + d10 + ", endTime = " + d11);
            }
            LocalLog.a(TAG, "getAISleepDuration: strSleepDuration=" + ((Object) sb2));
            this.mUtils.saveDeepSleepPredictOriginal(sb2.toString());
            return arrayList;
        }
        LocalLog.a(TAG, "getAISleepDuration: no listCluster.");
        this.mRecord.recoardEvent("AIPredict result no listCluster", System.currentTimeMillis(), true);
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0028, code lost:
    
        r3.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x002c, code lost:
    
        b6.LocalLog.b(com.oplus.deepsleep.ControllerCenter.TAG, "close asset xml inputStream failed");
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0038, code lost:
    
        if (r3 == null) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0026, code lost:
    
        if (r3 != null) goto L24;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private List<String> getAssetsXMLValue() {
        ArrayList<String> arrayList = new ArrayList<>();
        InputStream inputStream = null;
        try {
            try {
                inputStream = this.mContext.getAssets().open("sys_deepsleep_config_list.xml");
                XmlPullParser newPullParser = Xml.newPullParser();
                if (inputStream != null) {
                    newPullParser.setInput(inputStream, "UTF-8");
                    arrayList = parseXml(newPullParser);
                }
            } catch (Exception e10) {
                LocalLog.m(TAG, "getAssetsXMLValue: Got execption. ", e10);
            }
            return arrayList;
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception unused) {
                    LocalLog.b(TAG, "close asset xml inputStream failed");
                }
            }
            throw th;
        }
    }

    private void getDeepThinkerManager() {
        new Thread(new Runnable() { // from class: com.oplus.deepsleep.ControllerCenter.3
            @Override // java.lang.Runnable
            public void run() {
                ControllerCenter controllerCenter = ControllerCenter.this;
                controllerCenter.mDeepThinkerManager = DeepThinkerProxy.j(controllerCenter.mContext).i();
            }
        }).start();
    }

    private boolean getEarthquakeWarningSwitchStatus() {
        int i10 = -1;
        try {
            i10 = Settings.System.getIntForUser(this.mContext.getContentResolver(), "setting_key_com_oplus_sos_earthquake_warning", -1, UserHandle.myUserId());
            LocalLog.l(TAG, "getEarthquakeWarningSwitchStatus:" + i10);
        } catch (NumberFormatException unused) {
            LocalLog.l(TAG, "getEarthquakeWarningSwitchStatus wrong!");
        }
        return i10 == 1;
    }

    private boolean getFindPhoneSwitchStatus() {
        int i10 = -1;
        try {
            i10 = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), FIND_PHONE_SWITCH_STATE, -1, UserHandle.myUserId());
            LocalLog.l(TAG, "getFindPhoneSwitchStatus:" + i10);
        } catch (NumberFormatException unused) {
            LocalLog.l(TAG, "getFindPhoneSwitchStatus wrong!");
        }
        return i10 == 1;
    }

    public static synchronized ControllerCenter getInstance(Context context) {
        synchronized (ControllerCenter.class) {
            if (UserHandle.myUserId() != 0) {
                LocalLog.a(TAG, "UserId()=" + UserHandle.myUserId());
                return null;
            }
            if (sControllerCenter == null) {
                sControllerCenter = new ControllerCenter(context);
            }
            return sControllerCenter;
        }
    }

    private boolean getNetWorkType() {
        boolean z10;
        boolean z11;
        int i10;
        int i11;
        boolean z12 = true;
        if (isNotAllowDisableNetwork()) {
            this.mNetWorkSwitchState = 0;
            if (this.mUtils.getLocalNetWorkSwitchState() != 0) {
                this.mUtils.saveLocalNetWorkSwitchState(0);
            }
            this.mNetWorkType = 0;
            this.mDisableNetAllowed = false;
            this.mUtils.saveDeepSleepIsDisableNetAllowed(false);
            LocalLog.l(TAG, "getNetWorkType: now allowed disable net.");
            return true;
        }
        this.mDisableNetAllowed = true;
        this.mUtils.saveDeepSleepIsDisableNetAllowed(true);
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        boolean z13 = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (activeNetworkInfo != null) {
            int type = activeNetworkInfo.getType();
            z11 = type == 0;
            z10 = type == 1;
        } else {
            z10 = false;
            z11 = false;
        }
        boolean dataEnabled = this.mTelephonyManager.getDataEnabled();
        boolean isWifiEnabled = this.mWifiManager.isWifiEnabled();
        LocalLog.a(TAG, "getNetWorkType: connected=" + z13 + ", isMobile=" + z11 + ", isWifi=" + z10 + ", isMobileDataEnable=" + dataEnabled + ", isWifiEnable=" + isWifiEnabled);
        if (z10 && isWifiEnabled) {
            this.mNetWorkType = 1;
        } else if (z11 && dataEnabled) {
            this.mNetWorkType = 2;
        } else {
            this.mNetWorkType = 0;
        }
        if (dataEnabled && isWifiEnabled) {
            this.mNetWorkSwitchState = 3;
        } else if (dataEnabled) {
            this.mNetWorkSwitchState = 2;
        } else if (isWifiEnabled) {
            this.mNetWorkSwitchState = 1;
        } else {
            this.mNetWorkSwitchState = 0;
        }
        int i12 = this.mNetWorkType;
        if (i12 != 1 ? !(i12 != 2 || ((i10 = this.mNetWorkSwitchState) != 0 && i10 != 1)) : !((i11 = this.mNetWorkSwitchState) != 0 && i11 != 2)) {
            z12 = false;
        }
        LocalLog.l(TAG, "getNetWorkType: netType = " + networkTypeToString(this.mNetWorkType) + ", netSiwtch =" + networkSwitchToString(this.mNetWorkSwitchState) + ", sucess=" + z12);
        return z12;
    }

    private void initDeepThinker() {
        this.mDeepThinkerManager = new OplusDeepThinkerManager(this.mContext);
        this.mClientConnection = new ClientConnection(this.mContext, Executors.newFixedThreadPool(2), new Handler(this.mDataThread.getLooper()));
        OplusDeepThinkerManager oplusDeepThinkerManager = this.mDeepThinkerManager;
        if (oplusDeepThinkerManager != null) {
            oplusDeepThinkerManager.setRemoteGetter(new Supplier<IDeepThinkerBridge>() { // from class: com.oplus.deepsleep.ControllerCenter.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.function.Supplier
                public IDeepThinkerBridge get() {
                    LocalLog.a(ControllerCenter.TAG, "get deepthinker bridge");
                    return ControllerCenter.this.mClientConnection.getDeepThinkerBridge();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void interruptHandleLocked() {
        LocalLog.a(TAG, "interruptHandle. status = " + statusToString(this.mStatus) + ", isScreenOff=" + this.mIsScreenOff);
        this.mIsAiForecast = this.mUtils.getIsAiPredict();
        this.mRecord.recoardEvent("ProcessReboot:mIsScreenOff " + String.valueOf(this.mIsScreenOff), System.currentTimeMillis(), true);
        if (!this.mIsScreenOff) {
            turnToStatusActiveLocked();
            uploadDeepSleepstatistics(true, false, "ProcessReboot");
            return;
        }
        int i10 = this.mStatus;
        if (i10 != 0) {
            if (i10 != 1) {
                if (i10 != 2) {
                    if (i10 != 3) {
                        if (i10 != 4) {
                            if (i10 != 5) {
                                LocalLog.a(TAG, "interruptHandleLocked: unknown status=" + this.mStatus);
                                return;
                            }
                        }
                    }
                    long trafficMonitorTime = this.mUtils.getTrafficMonitorTime();
                    if (trafficMonitorTime != 0) {
                        setTrafficMonitorAlarm(trafficMonitorTime);
                    }
                    regSignificantMotion();
                    setInterruptedAlarm();
                    return;
                }
                regSignificantMotion();
                setInterruptedAlarm();
                return;
            }
            setInterruptedAlarm();
        }
    }

    private boolean isDeepSleepEnabled() {
        if (this.mConfigUpdateUtil.k()) {
            return true;
        }
        LocalLog.a(TAG, " sleep Mode rus Switch off!");
        return false;
    }

    private boolean isInSleepDuration() {
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mUtils.getExtremeSleepStatus()) {
            if (currentTimeMillis >= this.mPredictedSleepStartTime && currentTimeMillis <= this.mPredictedSleepEndTime - 0) {
                return true;
            }
        } else if (currentTimeMillis >= this.mPredictedSleepStartTime && currentTimeMillis <= this.mPredictedSleepEndTime - 1200000) {
            return true;
        }
        return false;
    }

    private boolean isNotAllowDisableNetwork() {
        if (!this.mConfigUpdateUtil.i()) {
            EventLog.writeEvent(BATTERY_STATUS_TAG, "Battery_ControllerCenter DeepSleepNetworkSwitch close");
            this.mRecord.recoardEvent("rusNotAllowDisableNet", System.currentTimeMillis(), true);
            return true;
        }
        if (!f.B(this.mContext)) {
            EventLog.writeEvent(BATTERY_STATUS_TAG, "Battery_ControllerCenter DeepSleepModeNetOffSwitch close");
            this.mRecord.recoardEvent("settingNotAllowDisableNet", System.currentTimeMillis(), true);
            return true;
        }
        if (!AppFeature.g() && this.mUtils.isVoWifiRegistered()) {
            EventLog.writeEvent(BATTERY_STATUS_TAG, "Battery_ControllerCenter isVoWifiRegistered");
            this.mRecord.recoardEvent("VoWifiRegistered", System.currentTimeMillis(), true);
            return true;
        }
        if (this.mUtils.specialAppHavebeenInstalled()) {
            EventLog.writeEvent(BATTERY_STATUS_TAG, "Battery_ControllerCenter specialAppHavebeenInstalled");
            this.mRecord.recoardEvent("specialAppInstalled", System.currentTimeMillis(), true);
            return true;
        }
        if (this.mUtils.isWifiApStateOn()) {
            EventLog.writeEvent(BATTERY_STATUS_TAG, "Battery_ControllerCenter isWifiApStateOn");
            this.mRecord.recoardEvent("WifiApStateOn", System.currentTimeMillis(), true);
            return true;
        }
        if (this.mUtils.isWLANSharingStateOn()) {
            EventLog.writeEvent(BATTERY_STATUS_TAG, "Battery_ControllerCenter isWLANSharingStateOn");
            this.mRecord.recoardEvent("WLANSharingStateOn", System.currentTimeMillis(), true);
            return true;
        }
        if (this.mUtils.isBluetoothPanConnected()) {
            EventLog.writeEvent(BATTERY_STATUS_TAG, "Battery_ControllerCenter isBluetoothPanConnected");
            this.mRecord.recoardEvent("BluetoothPanConnected", System.currentTimeMillis(), true);
            return true;
        }
        if (this.mUtils.isUsbTethering()) {
            EventLog.writeEvent(BATTERY_STATUS_TAG, "Battery_ControllerCenter isUsbTethering");
            this.mRecord.recoardEvent("UsbTethering", System.currentTimeMillis(), true);
            return true;
        }
        if (!this.mUtils.isMirageDisplay()) {
            return false;
        }
        EventLog.writeEvent(BATTERY_STATUS_TAG, "Battery_ControllerCenter isMirageDisplay");
        this.mRecord.recoardEvent("MirageDisplay", System.currentTimeMillis(), true);
        return true;
    }

    private boolean isShortScreenOn() {
        if (!this.mUtils.getEverEnteredDeepsleep()) {
            return false;
        }
        this.mUtils.saveEverEnteredDeepsleep(false);
        if (SystemClock.elapsedRealtime() - this.mLastWakeupTime > MIN_SCREEN_ON_DURATION || this.mIsUserPresent) {
            return false;
        }
        LocalLog.a(TAG, "ShortScreenOn");
        return true;
    }

    private boolean isTrafficSpeedLow() {
        long totalTraffic = this.mUtils.getTotalTraffic();
        long currentTimeMillis = System.currentTimeMillis();
        long j10 = (currentTimeMillis - this.mLastTrafficTimeStamp) / MS_IN_SECOND;
        long j11 = totalTraffic - this.mLastTrafficTotal;
        if (j10 == 0) {
            j10 = 1;
        }
        long j12 = (j11 / 1024) / j10;
        this.mLastTrafficTotal = totalTraffic;
        this.mLastTrafficTimeStamp = currentTimeMillis;
        LocalLog.a(TAG, "Traffic Speed: speed=" + j12 + "KB/s. deltaTraffic=" + Formatter.formatFileSize(this.mContext, j11) + ", timeInterval=" + j10 + "s, status=" + statusToString(this.mStatus));
        return j12 < THRESH_SPEED;
    }

    private boolean isUseNetworkDisableWhiteList() {
        int g6 = ConfigUpdateUtil.n(this.mContext).g();
        if (g6 == 2) {
            return true;
        }
        return g6 == 1 && (getEarthquakeWarningSwitchStatus() || getFindPhoneSwitchStatus());
    }

    private boolean isWhiteListPkg(String str) {
        if (this.mListWhitelistPkg.isEmpty()) {
            return true;
        }
        return this.mListWhitelistPkg.contains(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Context context, IntentFilter intentFilter) {
        SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        this.mSensorManager = sensorManager;
        this.mSignificantSensor = sensorManager.getDefaultSensor(17, true);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(DeepSleepUtils.STR_DEEP_SLEEP_STATUS), false, this.mStatusObserver);
        context.registerReceiver(this.mBroadcastReceiver, intentFilter, "oplus.permission.OPLUS_COMPONENT_SAFE", this.mWorkHandler, 2);
    }

    private void maybeNeedShowNetOffNotify() {
        boolean z10 = false;
        if (Settings.System.getInt(this.mContext.getContentResolver(), "is_need_show_netoff_notify", 0) == 1) {
            long h10 = this.mConfigUpdateUtil.h();
            long currentTimeMillis = System.currentTimeMillis();
            long j10 = this.mPredictedSleepStartTime;
            long j11 = this.mPredictedSleepEndTime;
            if (currentTimeMillis >= 86400000 + j10 || j10 >= j11) {
                return;
            }
            boolean z11 = j10 - currentTimeMillis <= h10 * MS_IN_SECOND;
            if (currentTimeMillis >= j10 && currentTimeMillis <= j11) {
                z10 = true;
            }
            if (z11 || z10) {
                if (this.mWorkHandler.hasMessages(16)) {
                    this.mWorkHandler.removeMessages(16);
                }
                this.mWorkHandler.sendEmptyMessageDelayed(16, 5000L);
            }
        }
    }

    private void networkDisableWhiteList(List<String> list, int i10, int i11) {
        boolean z10 = i10 == 1;
        try {
            Class<?> cls = Class.forName("android.nwpower.OAppNetControlManager");
            int intValue = ((Integer) cls.getMethod("networkDisableWhiteList", List.class, Integer.TYPE).invoke(cls.getMethod("getInstance", new Class[0]).invoke(cls, new Object[0]), list, Integer.valueOf(i10))).intValue();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("set netWork：function = new  result = ");
            sb2.append(intValue == 0 ? "success" : "fail");
            LocalLog.a(TAG, sb2.toString());
            if (intValue != 0) {
                oldDisconnectNet(z10, i11);
            }
        } catch (Exception e10) {
            LocalLog.a(TAG, "networkDisableWhiteList error = " + e10.toString());
            oldDisconnectNet(z10, i11);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String networkSwitchToString(int i10) {
        return i10 != 0 ? i10 != 1 ? i10 != 2 ? i10 != 3 ? Integer.toString(i10) : "ALL_ON" : "MOBILE_DATA" : "WIFI" : "ALL_OFF";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String networkTypeToString(int i10) {
        return i10 != 0 ? i10 != 1 ? i10 != 2 ? Integer.toString(i10) : "MOBILE_DATA" : "WIFI" : "NONE";
    }

    private void oldDisconnectNet(boolean z10, int i10) {
        LocalLog.a(TAG, "set netWork：function = old, enable = " + z10 + ", netType = " + i10);
        if (i10 == 1) {
            try {
                sSetWifiEnabled.invoke(sOSysNetControlManagerNewInstance.invoke(sOSysNetControlManager, new Object[0]), Boolean.valueOf(z10));
                return;
            } catch (Exception unused) {
                LocalLog.l(TAG, "mSetWifiEnabled.invoke fail; try mWifiManager.setWifiEnabled!");
                this.mWifiManager.setWifiEnabled(z10);
                return;
            }
        }
        try {
            sSetDataEnabled.invoke(sOSysNetControlManagerNewInstance.invoke(sOSysNetControlManager, new Object[0]), Boolean.valueOf(z10));
        } catch (Exception unused2) {
            LocalLog.l(TAG, "mSetDataEnabled.invoke fail; try mTelephonyManager.setDataEnabled!");
            this.mTelephonyManager.setDataEnabled(z10);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0055, code lost:
    
        if (r23 >= (r12 - 2400000)) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0057, code lost:
    
        r10 = r10 + 86400000;
        r12 = r12 + 86400000;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0066, code lost:
    
        if (r23 >= (r12 - 2400000)) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private List<Pair<Long, Long>> parseAISleepDuration(List<Pair<Double, Double>> list, long j10) {
        ArrayList arrayList = new ArrayList();
        for (int i10 = 0; i10 < list.size(); i10++) {
            double doubleValue = ((Double) list.get(i10).first).doubleValue();
            double doubleValue2 = ((Double) list.get(i10).second).doubleValue();
            long convertDoubleTime = convertDoubleTime(doubleValue, j10);
            long convertDoubleTime2 = convertDoubleTime(doubleValue2, j10);
            if (convertDoubleTime < 0 || convertDoubleTime2 < 0) {
                LocalLog.a(TAG, "parseAISleepDuration: time error. startTimeDouble=" + doubleValue + ", endTimeDouble=" + doubleValue2);
                this.mUtils.saveDeepSleepPredictWrongTime(true);
            } else if (doubleValue >= doubleValue2) {
                if (doubleValue > doubleValue2) {
                    convertDoubleTime -= 86400000;
                } else {
                    convertDoubleTime2 = j10 + 86400000;
                    convertDoubleTime = j10;
                }
                LocalLog.a(TAG, "parseAISleepDuration: startTimeInMillis=" + printTimeInMillis(convertDoubleTime));
                LocalLog.a(TAG, "parseAISleepDuration: endTimeInMillis=" + printTimeInMillis(convertDoubleTime2));
                if (convertDoubleTime2 - convertDoubleTime < 2400000) {
                    this.mUtils.saveDeepSleepPredictShortDuration(true);
                    LocalLog.a(TAG, "parseAISleepDuration: too short");
                } else {
                    arrayList.add(new Pair(Long.valueOf(convertDoubleTime), Long.valueOf(convertDoubleTime2)));
                }
            }
        }
        return arrayList;
    }

    private ArrayList<String> parseXml(XmlPullParser xmlPullParser) {
        int next;
        String attributeValue;
        LocalLog.a(TAG, "parseXml");
        if (xmlPullParser == null) {
            return null;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        boolean z10 = false;
        try {
            xmlPullParser.nextTag();
            do {
                next = xmlPullParser.next();
                if (next == 2) {
                    String name = xmlPullParser.getName();
                    LocalLog.a(TAG, "parser.getName(): " + name);
                    if ("NetworkDisableWhiteList".equals(name) && (attributeValue = xmlPullParser.getAttributeValue(null, "pkgName")) != null) {
                        arrayList.add(attributeValue);
                        LocalLog.a(TAG, "NetworkDisableWhiteList: pkgName = " + attributeValue);
                    }
                }
            } while (next != 1);
            z10 = true;
        } catch (IOException e10) {
            LocalLog.d(TAG, "failed parsing " + e10);
        } catch (XmlPullParserException e11) {
            LocalLog.d(TAG, "failed parsing " + e11);
        }
        LocalLog.a(TAG, "bSuccess: " + z10);
        return arrayList;
    }

    private String printTimeInMillis(long j10) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j10);
        return calendar.getTime().toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void quickEnterDeepsleep() {
        LocalLog.a(TAG, "quickEnterDeepsleep");
        stepStatusInactiveLocked(180000L);
    }

    private void recheckNavigating() {
        if (this.mStatus != 2) {
            LocalLog.a(TAG, "recheckNavigating: Status error.  status=" + statusToString(this.mStatus));
            this.mRecord.recoardEvent("recheckNavigating: Status error., status: " + statusToString(this.mStatus), System.currentTimeMillis(), true);
            return;
        }
        int i10 = this.mCntNavigatingCheck;
        this.mCntNavigatingCheck = i10 + 1;
        if (i10 < 3) {
            schedueAlarm(System.currentTimeMillis() + 600000, false);
            return;
        }
        this.mCntNavigatingCheck = 0;
        turnToStatusActiveLocked();
        this.mUtils.saveNavigationDetected(true);
    }

    private void recheckSystemPlaybackStatus() {
        if (this.mStatus != 2) {
            LocalLog.a(TAG, "recheckSystemPlaybackStatus: error.  status=" + statusToString(this.mStatus));
            this.mRecord.recoardEvent("recheckPlayback: Status error., status: " + statusToString(this.mStatus), System.currentTimeMillis(), true);
            return;
        }
        int i10 = this.mCntPlaybackCheck;
        this.mCntPlaybackCheck = i10 + 1;
        if (i10 < 3) {
            this.mUtils.doCheckPossibleMusicPlayer();
            schedueAlarm(System.currentTimeMillis() + 600000, false);
        } else {
            this.mCntPlaybackCheck = 0;
            turnToStatusActiveLocked();
            this.mUtils.savePlaybackDetected(true);
        }
    }

    private void reconfirmDeepSleepStatus() {
        if (this.mUtils.getDeepSleepStatus() != 0) {
            LocalLog.a(TAG, "reconfirmDeepSleepStatus");
            this.mUtils.saveDeepSleepStatus(0);
        }
        this.mUtils.saveNetWorkTypeSettingProvider(0);
    }

    private void reconfirmNetSwitch() {
        if (this.mUtils.getEverDisNet()) {
            LocalLog.a(TAG, "reconfirmNetSwitch: netSwitch=" + this.mNetWorkSwitchState);
            int i10 = this.mNetWorkSwitchState;
            if (i10 == 3) {
                setWifiEnable(true, "reconfirm");
                setMobileDataEnable(true, "reconfirm");
            } else if (i10 == 2) {
                setMobileDataEnable(true, "reconfirm");
            } else if (i10 == 1) {
                setWifiEnable(true, "reconfirm");
            }
            this.mUtils.saveEverDisNet(false);
        }
    }

    private void regSignificantMotion() {
        if (this.mSignificantSensor == null) {
            LocalLog.a(TAG, "regSignificantMotion: SignificantMotionSensor is null.");
            return;
        }
        LocalLog.a(TAG, "regSignificantMotion");
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager == null || sensorManager.requestTriggerSensor(this.mSignicantMotionListener, this.mSignificantSensor)) {
            return;
        }
        LocalLog.a(TAG, "Unable to register for SignificantSensor");
    }

    private void resetStatisticsVal() {
        if (this.mUtils.getEverEnteredDeepsleep()) {
            this.mUtils.saveEverEnteredDeepsleep(false);
        }
        if (this.mUtils.getEverEnteredSensing()) {
            this.mUtils.saveEverEnteredSensing(false);
        }
        if (this.mUtils.getTrafficLowDetected()) {
            this.mUtils.saveTrafficLowDetected(false);
        }
        if (this.mUtils.getPlaybackDetected()) {
            this.mUtils.savePlaybackDetected(false);
        }
        if (this.mUtils.getInternetPhoneDetected()) {
            this.mUtils.saveInternetPhoneDetected(false);
        }
        if (this.mUtils.getNavigationDetected()) {
            this.mUtils.saveNavigationDetected(false);
        }
        if (this.mUtils.getMotionDetected()) {
            this.mUtils.saveMotionDetected(false);
        }
        if (this.mUtils.getWaitTrafficStopDuration() != 0) {
            this.mUtils.saveWaitTrafficStopDuration(0L);
        }
        if (this.mUtils.getDisNetDuration() != 0) {
            this.mUtils.saveDisNetDuration(0L);
        }
        if (this.mUtils.getAppTrafficDuration() != 0) {
            this.mUtils.saveAppTrafficDuration(0L);
        }
        if (this.mUtils.getEverDisNet()) {
            this.mUtils.saveEverDisNet(false);
        }
        if (this.mUtils.getCountRestoreNet() != 0) {
            this.mUtils.saveCountRestoreNet(0);
        }
        if (!"noException".equals(this.mUtils.getDeepSleepPredictException())) {
            this.mUtils.saveDeepSleepPredictException("noException");
        }
        if (this.mUtils.getDeepSleepPredictWrongTime()) {
            this.mUtils.saveDeepSleepPredictWrongTime(false);
        }
        if (this.mUtils.getDeepSleepPredictShortDuration()) {
            this.mUtils.saveDeepSleepPredictShortDuration(false);
        }
        if (this.mUtils.getDeepSleepPredictSelectError()) {
            this.mUtils.saveDeepSleepPredictSelectError(false);
        }
        if ("nodata".equals(this.mUtils.getDeepSleepPredictOriginal())) {
            return;
        }
        this.mUtils.saveDeepSleepPredictOriginal("nodata");
    }

    private void restoreNetWork(boolean z10) {
        LocalLog.l(TAG, "restoreNetWork: netWorkSwitch = " + networkSwitchToString(this.mNetWorkSwitchState) + ", netType = " + networkTypeToString(this.mNetWorkType));
        boolean hasMessages = this.mWorkHandler.hasMessages(17);
        boolean airPlaneModeStatus = this.mUtils.getAirPlaneModeStatus();
        boolean deepSleepResetAirPlaneStatus = this.mUtils.getDeepSleepResetAirPlaneStatus();
        if (hasMessages) {
            this.mWorkHandler.removeMessages(17);
        }
        if (deepSleepResetAirPlaneStatus && airPlaneModeStatus) {
            this.mUtils.setAirPlaneModeStatus(false);
            this.mUtils.setDeepSleepResetAirPlaneStatus(false);
        }
        int i10 = this.mNetWorkSwitchState;
        if (i10 == 3) {
            if (z10) {
                this.mUtils.savePropNetWorkRestoreTime(SystemClock.elapsedRealtime());
                this.mUtils.savePropNetWorkDisableTime(0L);
                this.mUtils.saveNetWorkDisableStateSettingProvider(0);
            }
            setWifiEnable(true, "restore");
            if (z10 || this.mNetWorkType != 1) {
                setMobileDataEnable(true, "restore");
            }
            this.mWakeLockNet.acquire(10000L);
        } else if (i10 == 1) {
            if (z10) {
                this.mUtils.savePropNetWorkRestoreTime(SystemClock.elapsedRealtime());
                this.mUtils.savePropNetWorkDisableTime(0L);
                this.mUtils.saveNetWorkDisableStateSettingProvider(0);
            }
            setWifiEnable(true, "restore");
            this.mWakeLockNet.acquire(10000L);
        } else if (i10 == 2) {
            if (z10) {
                this.mUtils.savePropNetWorkRestoreTime(SystemClock.elapsedRealtime());
                this.mUtils.savePropNetWorkDisableTime(0L);
                this.mUtils.saveNetWorkDisableStateSettingProvider(0);
            }
            setMobileDataEnable(true, "restore");
            this.mWakeLockNet.acquire(10000L);
        }
        this.mUtils.saveNetWorkTypeSettingProvider(0);
        calcDisNetDuration();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreNetworkReqLocked() {
        if (this.mStatus != 4) {
            LocalLog.a(TAG, "restoreNetworkReq: ignore. status = " + statusToString(this.mStatus));
            return;
        }
        restoreNetWork(false);
        this.mIsNeedSendMaintenanceRestoredBroadcast = true;
        if (this.mListTraffic.isEmpty()) {
            setAppTrafficStartTimeoutAlarm();
        }
        DeepSleepUtils deepSleepUtils = this.mUtils;
        deepSleepUtils.saveCountRestoreNet(deepSleepUtils.getCountRestoreNet() + 1);
        this.mLastTrafficTotal = this.mUtils.getTotalTraffic();
        this.mLastTrafficTimeStamp = System.currentTimeMillis();
        setTrafficMonitorAlarm(System.currentTimeMillis() + 300000);
        changeStatus(5);
        this.mUtils.saveAppTrafficEnterElapsedTime(SystemClock.elapsedRealtime());
    }

    private void rmvListTrafficStartTime(String str, String str2) {
        for (int size = this.mListTrafficStartTime.size() - 1; size >= 0; size--) {
            String[] split = this.mListTrafficStartTime.get(size).split("!");
            if (split.length == 2 && str2.equals(split[0])) {
                this.mListTrafficStartTime.remove(size);
            }
        }
    }

    private void rmvSimuTrafficPkgJob() {
        if (this.mListTraffic.contains(SIMULATETED_TRAFFIC_PKG_JOB)) {
            this.mListTraffic.remove(SIMULATETED_TRAFFIC_PKG_JOB);
            for (int size = this.mListTrafficStartTime.size() - 1; size >= 0; size--) {
                String[] split = this.mListTrafficStartTime.get(size).split("!");
                if (split.length == 2 && SIMULATETED_TRAFFIC_PKG_JOB.equals(split[0])) {
                    this.mListTrafficStartTime.remove(size);
                }
            }
            this.mGuardElfDataManager.k(this.mListTraffic, "app_traffic_deepsleep.xml");
            this.mGuardElfDataManager.k(this.mListTrafficStartTime, "app_traffic_deepsleep_start_time.xml");
        }
    }

    private void schedueAlarm(long j10, boolean z10) {
        Intent intent = new Intent(ACTION_DEEPSLEEP_STEP);
        intent.setPackage("com.oplus.battery");
        PendingIntent broadcastAsUser = PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL);
        if (z10) {
            this.mNextAlarmTime = j10;
            this.mAlarmManager.setExact(0, j10, broadcastAsUser);
        } else {
            long j11 = j10 - 10000;
            this.mNextAlarmTime = j11;
            this.mAlarmManager.set(0, j11, broadcastAsUser);
        }
        this.mUtils.saveNextAlarmTime(j10);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j10);
        LocalLog.a(TAG, "schedueAlarm: time=" + calendar.getTime() + ", status = " + statusToString(this.mStatus) + ", isExact=" + z10);
    }

    private void scheduleNextSleepLongScreenoff() {
        long j10 = this.mPredictedSleepStartTime;
        long j11 = this.mPredictedSleepEndTime;
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis >= this.mPredictedSleepEndTime - RESERVED_EXIT_DEEPSLEEP_TIME) {
            j10 += 86400000;
            j11 += 86400000;
            LocalLog.a(TAG, "scheduleNextSleepLongScreenoff: sleep next day");
        }
        if (this.mTestMode && this.mTestSleepStartTime != 0 && this.mTestSleepEndTime != 0) {
            this.mTestSleepStartTime = j10;
            this.mTestSleepEndTime = j11;
        }
        long j12 = j10 - 1800000;
        if (currentTimeMillis < j12) {
            LocalLog.a(TAG, "scheduleNextSleepLongScreenoff: set next sleep");
            schedueAlarm(System.currentTimeMillis() + (j12 - currentTimeMillis), false);
        } else {
            LocalLog.a(TAG, "scheduleNextSleepLongScreenoff: next sleep duration is near.");
            schedueAlarm(System.currentTimeMillis() + 300000, false);
        }
    }

    private boolean selectAISleepDuration(List<Pair<Long, Long>> list, long j10) {
        Pair<Long, Long> pair = null;
        long j11 = Long.MAX_VALUE;
        long j12 = 0;
        Pair<Long, Long> pair2 = null;
        for (int i10 = 0; i10 < list.size(); i10++) {
            long longValue = ((Long) list.get(i10).first).longValue();
            long longValue2 = ((Long) list.get(i10).second).longValue();
            if (j10 < longValue - 1200000 || j10 >= longValue2 - 2400000) {
                long j13 = longValue - j10;
                if (j13 < j11) {
                    pair2 = list.get(i10);
                    j11 = j13;
                }
            } else {
                long j14 = longValue2 - j10;
                if (j14 > j12) {
                    pair = list.get(i10);
                    j12 = j14;
                }
            }
        }
        if (pair == null && pair2 == null) {
            LocalLog.a(TAG, "selectAISleepDuration: no suitable duration.");
            for (int i11 = 0; i11 < list.size(); i11++) {
                LocalLog.a(TAG, "selectAISleepDuration: startTimeInMillis=" + printTimeInMillis(((Long) list.get(i11).first).longValue()) + ", endTimeInMillis=" + printTimeInMillis(((Long) list.get(i11).second).longValue()));
            }
            this.mUtils.saveDeepSleepPredictSelectError(true);
            return false;
        }
        this.mIsAiForecast = true;
        if (pair != null) {
            setPredictSleepTime(((Long) pair.first).longValue(), ((Long) pair.second).longValue());
        } else if (pair2 != null) {
            setPredictSleepTime(((Long) pair2.first).longValue(), ((Long) pair2.second).longValue());
        }
        LocalLog.l(TAG, "selectSleepDuration: start: " + printTimeInMillis(this.mPredictedSleepStartTime));
        LocalLog.l(TAG, "selectSleepDuration: end: " + printTimeInMillis(this.mPredictedSleepEndTime));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDeepSleepMaintenceRestoreBroadcast() {
        if (!this.mTriggerForMaintence) {
            LocalLog.a(TAG, "sendDeepSleepMaintenceRestoreBroadcast: mTriggerForMaintence = " + this.mTriggerForMaintence);
            return;
        }
        LocalLog.a(TAG, "sendDeepSleepMaintenceRestoreBroadcast");
        Intent intent = new Intent("oplus.intent.action.DEEP_SLEEP_MAINTENCE_RESTORE_NETWORK");
        intent.setFlags(268435456);
        Context context = this.mContext;
        if (context != null) {
            context.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
        }
    }

    private void setAirplaneModeDelay(boolean z10, int i10) {
        Message obtainMessage = this.mWorkHandler.obtainMessage();
        obtainMessage.what = 17;
        Bundle bundle = new Bundle();
        bundle.putBoolean("AirEnable", z10);
        obtainMessage.setData(bundle);
        this.mWorkHandler.sendMessageDelayed(obtainMessage, i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAirplaneModeOn(boolean z10) {
        LocalLog.a(TAG, "DEEP_SLEEP Status,setAirplaneMode  " + z10);
        if (!z10 && !this.mUtils.getAirPlaneModeStatus()) {
            this.mUtils.setDeepSleepResetAirPlaneStatus(z10);
            LocalLog.a(TAG, "DEEP_SLEEP Status,AirplaneMode turning ON Fail !!! ");
            return;
        }
        this.mUtils.setAirPlaneModeStatus(z10);
        this.mUtils.setDeepSleepResetAirPlaneStatus(z10);
        if (z10) {
            setAirplaneModeDelay(false, SceneStatusInfo.SceneConstant.TRIP_GO_TO_STATION);
        }
    }

    private void setAppTrafficStartTimeoutAlarm() {
        LocalLog.a(TAG, "setAppTrafficStartTimeoutAlarm");
        Intent intent = new Intent(ACTION_APP_TRAFFIC_START_TIMEOUT);
        intent.setPackage("com.oplus.battery");
        this.mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + 60000, PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL));
    }

    private void setAutoStopTrafficAlarm() {
        LocalLog.a(TAG, "setAutoStopTrafficAlarm");
        Intent intent = new Intent(ACTION_AUTO_STOP_TRAFFIC);
        intent.setPackage("com.oplus.battery");
        this.mAlarmManager.set(2, (SystemClock.elapsedRealtime() + 600000) - 10000, PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL));
    }

    private void setBatterycurveStatus() {
        if (!SuperSleepStatsHelper.getInstance(this.mContext).isSupportBatteryOptimize()) {
            LocalLog.b(TAG, "donnot supportBatteryOptimize");
        }
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.set(11, 21);
        calendar.set(12, 0);
        long timeInMillis = calendar.getTimeInMillis();
        long j10 = 43200000 + timeInMillis;
        calendar.setTimeInMillis(currentTimeMillis);
        LocalLog.a(TAG, "sendBatterycurveMessage: now =" + calendar.getTime());
        calendar.setTimeInMillis(timeInMillis);
        LocalLog.a(TAG, "sendBatterycurveMessage: start =" + calendar.getTime());
        calendar.setTimeInMillis(j10);
        LocalLog.a(TAG, "sendBatterycurveMessage: end =" + calendar.getTime());
        if (currentTimeMillis >= timeInMillis && currentTimeMillis < j10 - ONE_HOUR_MILLS) {
            SuperSleepStatsHelper.getInstance(this.mContext).batteryCurveOptimize(1);
        } else {
            LocalLog.a(TAG, "setBatterycarveAlarm");
            Intent intent = new Intent(ACTION_BATTERY_CURVE_START_MONITOR);
            intent.setPackage("com.oplus.battery");
            this.mAlarmManager.setExact(0, timeInMillis, PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL));
        }
        Intent intent2 = new Intent(ACTION_BATTERY_CURVE_END_MONITOR);
        intent2.setPackage("com.oplus.battery");
        this.mAlarmManager.setExact(0, j10, PendingIntent.getBroadcastAsUser(this.mContext, 0, intent2, 67108864, UserHandle.ALL));
    }

    private void setForcestopAlarm() {
        LocalLog.a(TAG, "setForcestopAlarm");
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Intent intent = new Intent(ACTION_FORCESTOP_MONITOR);
        intent.setPackage("com.oplus.battery");
        this.mAlarmManager.setExact(2, elapsedRealtime + 600000, PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL));
    }

    private void setInterruptedAlarm() {
        long nextAlarmTime = this.mUtils.getNextAlarmTime();
        this.mNextAlarmTime = nextAlarmTime;
        if (nextAlarmTime != 0) {
            int i10 = this.mStatus;
            if (i10 != 4 && i10 != 5) {
                schedueAlarm(nextAlarmTime, false);
                return;
            } else {
                schedueAlarm(nextAlarmTime, true);
                return;
            }
        }
        LocalLog.a(TAG, "setInterruptedAlarm: alarm time is 0");
        int i11 = this.mStatus;
        if (i11 != 0) {
            if (i11 == 1) {
                schedueAlarm(System.currentTimeMillis() + 600000, false);
                return;
            }
            if (i11 == 2) {
                schedueAlarm(System.currentTimeMillis() + 1200000, false);
                return;
            }
            if (i11 != 3) {
                if (i11 != 4 && i11 != 5) {
                    LocalLog.a(TAG, "setInterruptedAlarm: unknown status=" + this.mStatus);
                    return;
                }
                schedueAlarm(this.mPredictedSleepEndTime - RESERVED_EXIT_DEEPSLEEP_TIME, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMobileDataEnable(boolean z10, String str) {
        boolean z11;
        boolean z12 = false;
        if (!z10 && this.mPowerManager.isInteractive()) {
            LocalLog.l(TAG, "ignore disable mobileData while screen on. reason=" + str);
            uploadNetWorkSwitchOnOff(z10, "MOBILEDATA", false, str);
            this.mRecord.recoardEvent("MobileDataDisableIgnoreDueToInteractive", System.currentTimeMillis(), true);
            return;
        }
        if (isUseNetworkDisableWhiteList()) {
            networkDisableWhiteList(determineNetWorkWhiteUidList(), z10 ? 1 : 0, 2);
            uploadNetWorkSwitchOnOff(z10, "useNetworkDisableWhiteList_MOBILEDATA", true, str);
            z12 = true;
            z11 = true;
        } else {
            LocalLog.a(TAG, "diable netWork：function = old");
            try {
                sSetDataEnabled.invoke(sOSysNetControlManagerNewInstance.invoke(sOSysNetControlManager, new Object[0]), Boolean.valueOf(z10));
                z11 = true;
            } catch (Exception unused) {
                LocalLog.l(TAG, "mSetDataEnabled.invoke fail; try mTelephonyManager.setDataEnabled!");
                this.mTelephonyManager.setDataEnabled(z10);
                z11 = false;
            }
            uploadNetWorkSwitchOnOff(z10, "MOBILEDATA", true, str);
        }
        DeepSleepRecord deepSleepRecord = this.mRecord;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("MobileData ");
        sb2.append(z10 ? "enable" : "disable");
        sb2.append(", reason:");
        sb2.append(str);
        sb2.append(", useCustomMethod:");
        sb2.append(z11);
        sb2.append(", useNetworkDisableWhiteList:");
        sb2.append(z12);
        deepSleepRecord.recoardEvent(sb2.toString(), System.currentTimeMillis(), true);
        LocalLog.l(TAG, "setMobileDataEnable: enable = " + z10 + ", useCustomMethod = " + z11);
    }

    private void setMusicMonitorAlarm() {
        LocalLog.a(TAG, "setMusicMonitorAlarm");
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Intent intent = new Intent(ACTION_MUSIC_MONITOR);
        intent.setPackage("com.oplus.battery");
        this.mAlarmManager.setExact(2, elapsedRealtime + 1800000, PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL));
    }

    private void setPredictSleepTime(long j10, long j11) {
        this.mPredictedSleepStartTime = j10;
        this.mPredictedSleepEndTime = j11;
        this.mUtils.savePredictStartTime(j10);
        this.mUtils.savePredictEndTime(j11);
        this.mUtils.saveIsAiPredict(this.mIsAiForecast);
        this.mUtils.saveIsTestMode(this.mTestMode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStartTrafficNextDozeMaintenanceAlarm() {
        if (this.mTriggerForMaintence) {
            LocalLog.a(TAG, "setStartTrafficNextDozeMaintenanceAlarm: mTriggerForMaintence = " + this.mTriggerForMaintence);
            return;
        }
        if (!this.mEverEnterDeepIdle) {
            LocalLog.a(TAG, "setStartTrafficNextDozeMaintenanceAlarm: not ever enter deepidle. ignore.");
            return;
        }
        LocalLog.a(TAG, "setStartTrafficNextDozeMaintenanceAlarm");
        Intent intent = new Intent(ACTION_START_TRAFFIC_NEXT_DOZE_MAINTENANCE);
        intent.setPackage("com.oplus.battery");
        this.mAlarmManager.set(2, SystemClock.elapsedRealtime() + 300000, PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL));
    }

    private void setTrafficMonitorAlarm(long j10) {
        LocalLog.a(TAG, "setTrafficMonitorAlarm");
        Intent intent = new Intent(ACTION_TRAFFIC_MONITOR);
        intent.setPackage("com.oplus.battery");
        this.mAlarmManager.set(0, j10 - 10000, PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL));
        this.mUtils.saveTrafficMonitorTime(j10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setWifiEnable(boolean z10, String str) {
        boolean z11;
        this.mWifiEnalbe = z10;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j10 = elapsedRealtime - this.mWifiOperationTimeStamp;
        if (j10 < 5000) {
            this.mWorkHandler.removeMessages(11);
            this.mWakeLock.acquire(5000L);
            long j11 = 5000 - j10;
            this.mWorkHandler.sendEmptyMessageDelayed(11, j11);
            LocalLog.a(TAG, "setWifiEnable: too frequent.  enable = " + z10 + ", delay " + j11);
            return;
        }
        boolean z12 = false;
        if (!z10 && this.mPowerManager.isInteractive()) {
            LocalLog.l(TAG, "ignore disable wifi while screen on. reason=" + str);
            uploadNetWorkSwitchOnOff(z10, "WIFI", false, str);
            this.mRecord.recoardEvent("WifiDisableIgnoreDueToInteractive", System.currentTimeMillis(), true);
            return;
        }
        boolean z13 = this.mWifiManager.getWifiState() == 3;
        boolean z14 = this.mWifiManager.getWifiState() == 1;
        LocalLog.a(TAG, "setWifiEnable: enable = " + z10 + ", curIsWifiEnabled = " + z13 + ", curIsWifiDisabled = " + z14);
        if (isUseNetworkDisableWhiteList()) {
            networkDisableWhiteList(determineNetWorkWhiteUidList(), z10 ? 1 : 0, 1);
            uploadNetWorkSwitchOnOff(z10, "networkDisableWhiteList_WIFI", true, str);
            z12 = true;
            z11 = true;
        } else if (!(z10 && z14) && (z10 || !z13)) {
            z11 = true;
        } else {
            LocalLog.a(TAG, "diable netWork：function = old");
            try {
                sSetWifiEnabled.invoke(sOSysNetControlManagerNewInstance.invoke(sOSysNetControlManager, new Object[0]), Boolean.valueOf(z10));
                z11 = true;
            } catch (Exception unused) {
                LocalLog.l(TAG, "mSetWifiEnabled.invoke fail; try mWifiManager.setWifiEnabled!");
                this.mWifiManager.setWifiEnabled(z10);
                z11 = false;
            }
            this.mWifiOperationTimeStamp = elapsedRealtime;
            uploadNetWorkSwitchOnOff(z10, "WIFI", true, str);
        }
        DeepSleepRecord deepSleepRecord = this.mRecord;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Wifi ");
        sb2.append(z10 ? "enable" : "disable");
        sb2.append(", reason:");
        sb2.append(str);
        sb2.append(", useCustomMethod:");
        sb2.append(z11);
        sb2.append(", useNetworkDisableWhiteList:");
        sb2.append(z12);
        deepSleepRecord.recoardEvent(sb2.toString(), System.currentTimeMillis(), true);
        LocalLog.l(TAG, "setWifiEnable: enable = " + z10 + ", useCustomMethod = " + z11);
    }

    private boolean shouldWaittingTrafficStop() {
        if (this.mIgnoreTraffic) {
            return false;
        }
        if (!this.mUtils.getIsTestMode() && !this.mConfigUpdateUtil.i()) {
            return false;
        }
        boolean isTrafficSpeedLow = isTrafficSpeedLow();
        if (!this.mUtils.getIsTestMode() && isTrafficSpeedLow) {
            return false;
        }
        if (!isTrafficSpeedLow && !this.mListTraffic.contains(SIMULATETED_TRAFFIC_PKG_JOB)) {
            this.mListTraffic.add(SIMULATETED_TRAFFIC_PKG_JOB);
            this.mListTrafficStartTime.add("traffic:high!" + System.currentTimeMillis());
            this.mGuardElfDataManager.k(this.mListTraffic, "app_traffic_deepsleep.xml");
            this.mGuardElfDataManager.k(this.mListTrafficStartTime, "app_traffic_deepsleep_start_time.xml");
            LocalLog.a(TAG, "stepStatusSensing: traffic high.");
        }
        if (this.mListTraffic.isEmpty()) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        this.mLastTrafficTotal = this.mUtils.getTotalTraffic();
        this.mLastTrafficTimeStamp = currentTimeMillis;
        if (this.mUtils.getExtremeSleepStatus()) {
            setTrafficMonitorAlarm(300000 + currentTimeMillis);
        } else {
            setTrafficMonitorAlarm(300000 + currentTimeMillis);
        }
        changeStatus(3);
        this.mUtils.saveWaitTrafficStopEnterTime(currentTimeMillis);
        LocalLog.a(TAG, "stepStatusSensing: pkg traffic. " + this.mListTraffic);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showNetOffNotifyRightNow() {
        if (this.mIsScreenOff) {
            return;
        }
        boolean B = f.B(this.mContext);
        if (!b.D() || f.p0(this.mContext) == 2) {
            if (!B) {
                NotifyUtil v7 = NotifyUtil.v(this.mContext);
                if (b.D()) {
                    v7.U();
                } else {
                    v7.V();
                }
                HashMap hashMap = new HashMap();
                hashMap.put("showNetOffNotify", String.valueOf(true));
                hashMap.put("showNotifyTime", DateFormat.format("yyyy-MM-dd-HH-mm-ss", System.currentTimeMillis()).toString());
                LocalLog.a(TAG, "upload: eventMap = " + hashMap);
                this.mUploadDataUtil.v(hashMap);
            }
            Settings.System.putInt(this.mContext.getContentResolver(), "is_need_show_netoff_notify", 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void signicantMotionDetectedlocked() {
        LocalLog.a(TAG, "signicant Motion Detected: status = " + statusToString(this.mStatus) + ", cntMotionDetected = " + this.mCntMotionDetected);
        this.mRecord.recoardEvent("signicantMotionDetected", System.currentTimeMillis(), true);
        int i10 = this.mStatus;
        if (i10 == 0 || i10 == 1) {
            LocalLog.a(TAG, "signicant Motion Detected: unexpected status = " + statusToString(this.mStatus));
            return;
        }
        if (i10 == 2) {
            if (this.mCntMotionDetected >= 3) {
                turnToStatusActiveLocked();
                this.mUtils.saveMotionDetected(true);
                return;
            } else {
                changeStatus(1);
                schedueAlarm(System.currentTimeMillis() + 600000, false);
                return;
            }
        }
        if (i10 != 3 && i10 != 4 && i10 != 5) {
            LocalLog.a(TAG, "signicant Motion Detected: unknown state = " + statusToString(this.mStatus));
            return;
        }
        turnToStatusActiveLocked();
        this.mUtils.saveMotionDetected(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startClearApps() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long lightClearTime = elapsedRealtime - this.mUtils.getLightClearTime();
        if (lightClearTime < ONE_HOUR_MILLS) {
            LocalLog.a(TAG, "startClearApps: too frequent. " + lightClearTime);
            return;
        }
        this.mUtils.saveLightClearTime(elapsedRealtime);
        LocalLog.a(TAG, "startClearApps");
        this.mWakeLockStartClear.acquire(MS_IN_SECOND);
        Intent w10 = CommonUtil.w(this.mContext, new Intent("oplus.intent.action.REQUEST_SMART_LIGHT_CLEAR"));
        if (w10 == null) {
            LocalLog.a(TAG, "startClearApps clearIntent is null");
        } else {
            this.mContext.startService(w10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startMorningClearApps() {
        if (!this.mIsAiForecast) {
            LocalLog.a(TAG, "startMorningClearApps: not ai predict.");
            startClearApps();
            return;
        }
        long morningClearTime = this.mUtils.getMorningClearTime();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j10 = elapsedRealtime - morningClearTime;
        if (j10 < 64800000) {
            LocalLog.a(TAG, "startMorningClearApps: too frequent. " + j10);
            startClearApps();
            return;
        }
        this.mWakeLockStartClear.acquire(MS_IN_SECOND);
        Intent w10 = CommonUtil.w(this.mContext, new Intent("oplus.intent.action.REQUEST_SMART_DEEP_CLEAR"));
        if (w10 == null) {
            LocalLog.a(TAG, "startMorningClearApps clearIntent is null");
            startClearApps();
        } else {
            this.mUtils.saveMorningClearTime(elapsedRealtime);
            this.mUtils.saveLightClearTime(elapsedRealtime);
            LocalLog.a(TAG, "startMorningClearApps");
            this.mContext.startService(w10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String statusToString(int i10) {
        return i10 != 0 ? i10 != 1 ? i10 != 2 ? i10 != 3 ? i10 != 4 ? i10 != 5 ? Integer.toString(i10) : "APP_TRAFFIC" : "DEEP_SLEEP" : "WAITTING_TRAFFIC_STOP" : "SENSING" : "INACTIVE" : "ACTIVE";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stepDeepSleepLocked() {
        LocalLog.a(TAG, "stepDeepSleep: status = " + statusToString(this.mStatus));
        int i10 = this.mStatus;
        if (i10 == 0) {
            if (!isDeepSleepEnabled()) {
                this.mRecord.recoardEvent("stepDeepSleep:rusDeepSleepDisable", System.currentTimeMillis(), true);
                return;
            }
            if (this.mPowerManager.isInteractive()) {
                return;
            }
            synchronized (this.mLock) {
                this.mWakeLock.acquire();
                uploadDeepSleepstatistics(false, true, "LongScreenOff");
                this.mRecord.recoardEvent("LongScreenOff", System.currentTimeMillis(), true);
                turnToStatusInactiveLocked(false);
                this.mWakeLock.release();
            }
            return;
        }
        if (i10 == 1) {
            if (this.mUtils.getExtremeSleepStatus()) {
                extremeStepStatusInactiveLocked(300000L);
                return;
            } else {
                stepStatusInactiveLocked(1200000L);
                return;
            }
        }
        if (i10 == 2) {
            stepStatusSensingLocked();
            return;
        }
        if (i10 == 3) {
            LocalLog.a(TAG, "stepDeepSleep: unexpected status = " + statusToString(this.mStatus));
            return;
        }
        if (i10 == 4) {
            this.mIsWakeFromDeepSleep = false;
            this.mUtils.saveDeepSleepEnterCount(0);
            turnToStatusActiveLocked();
            scheduleNextSleepLongScreenoff();
            return;
        }
        if (i10 != 5) {
            LocalLog.a(TAG, "stepDeepSleep: unknown state = " + statusToString(this.mStatus));
            return;
        }
        turnToStatusActiveLocked();
        scheduleNextSleepLongScreenoff();
    }

    private void stepStatusInactiveLocked(long j10) {
        LocalLog.a(TAG, "stepStatusInactive");
        if (!this.mForecastSleepDurationHandled) {
            forecastSleepDuration();
            this.mForecastSleepDurationHandled = true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long j11 = this.mTimeScreenOff;
        if (j11 != -1) {
            this.mRecord.recoardEvent("screenoff", j11, false);
            this.mTimeScreenOff = -1L;
        }
        this.mRecord.recoardEvent("stepInactive: Predicted " + this.mUtils.getAISleepPercent() + " " + this.mUtils.getAIWakePercent() + " " + printTimeInMillis(this.mPredictedSleepStartTime) + " " + printTimeInMillis(this.mPredictedSleepEndTime) + ", IsAiForecast:" + String.valueOf(this.mIsAiForecast), currentTimeMillis, true);
        if (currentTimeMillis < this.mPredictedSleepStartTime - 1200000) {
            LocalLog.a(TAG, "stepStatusInactive: set alarm to predictedSleepStartTime - SENSING_DURATION");
            schedueAlarm((this.mPredictedSleepStartTime - 1200000) + 10000, true);
            return;
        }
        if (currentTimeMillis < this.mPredictedSleepEndTime - 2400000) {
            regSignificantMotion();
            long currentTimeMillis2 = System.currentTimeMillis();
            schedueAlarm(j10 + currentTimeMillis2, true);
            this.mLastTrafficTotal = this.mUtils.getTotalTraffic();
            this.mLastTrafficTimeStamp = currentTimeMillis2;
            changeStatus(2);
            this.mUtils.saveEverEnteredSensing(true);
            return;
        }
        LocalLog.a(TAG, "stepStatusInactive: sleep duration error. turn to ACTIVE");
        changeStatus(0);
        this.mRecord.recoardEvent("status to " + statusToString(0), System.currentTimeMillis(), true);
    }

    private void stepStatusSensingLocked() {
        if (!isInSleepDuration()) {
            this.mRecord.recoardEvent("stepSensing: notInSleepDuration", System.currentTimeMillis(), true);
            changeStatus(1);
            this.mWorkHandler.sendEmptyMessage(1);
            LocalLog.l(TAG, "stepStatusSensing: not in sleep duration");
            return;
        }
        if (this.mIsAiForecast && this.mUtils.getScreenoffTimeAiDcs() == 0) {
            this.mUtils.saveScreenoffTimeAiDcs(f.r0(this.mContext));
            this.mUtils.saveAiSleepStartTimeDcs(this.mPredictedSleepStartTime);
            this.mUtils.saveAiSleepEndTimeDcs(this.mPredictedSleepEndTime);
        }
        if (this.mUtils.isNavigationMode()) {
            if (this.mTestMode) {
                this.mRecord.recoardEvent("stepSensing: testmode. NotificationNavigation", System.currentTimeMillis(), true);
                turnToStatusActiveLocked();
                this.mUtils.saveNavigationDetected(true);
                LocalLog.l(TAG, "stepStatusSensing: testmode. NotificationNavigation");
                return;
            }
            List<String> inUsePackagesList = this.mUtils.getInUsePackagesList();
            if (inUsePackagesList != null && !inUsePackagesList.isEmpty()) {
                recheckNavigating();
                LocalLog.l(TAG, "stepStatusSensing: navigating. listInUse: " + inUsePackagesList);
                this.mRecord.recoardEvent("stepSensing: navigating, listInUse: " + inUsePackagesList, System.currentTimeMillis(), true);
                return;
            }
            LocalLog.l(TAG, "stepStatusSensing: NotificationNavigation, but no app navigating");
            this.mRecord.recoardEvent("stepSensing: NotificationNavigation, but no app navigating", System.currentTimeMillis(), true);
        }
        if (this.mUtils.isSystemAudioOut(this.mContext)) {
            this.mRecord.recoardEvent("stepSensing: AudioPlayback", System.currentTimeMillis(), true);
            recheckSystemPlaybackStatus();
            LocalLog.l(TAG, "stepStatusSensing: playback");
            return;
        }
        ArrayList arrayList = new ArrayList();
        int playbackState = this.mUtils.getPlaybackState(arrayList);
        if (playbackState != 2 && (playbackState != 0 || arrayList.isEmpty())) {
            if (this.mUtils.isInternetPhone(this.mContext, this.mRecord)) {
                this.mRecord.recoardEvent("stepSensing: InternetPhone", System.currentTimeMillis(), true);
                turnToStatusActiveLocked();
                this.mUtils.saveInternetPhoneDetected(true);
                LocalLog.l(TAG, "stepStatusSensing: InternetPhone");
                return;
            }
            if (this.mUtils.getExtremeSleepStatus()) {
                if (!extremeGetNetWorkType()) {
                    turnToStatusActiveLocked();
                    return;
                } else {
                    if (shouldWaittingTrafficStop()) {
                        return;
                    }
                    extremeEnterDeepSleepLocked();
                    return;
                }
            }
            if (!getNetWorkType()) {
                this.mRecord.recoardEvent("stepSensing: GetNetWorkTypeError:" + networkTypeToString(this.mNetWorkType) + " " + networkSwitchToString(this.mNetWorkSwitchState), System.currentTimeMillis(), true);
                LocalLog.l(TAG, "stepStatusSensing: net work type error");
                this.mUtils.saveDeepSleepNetTypeError(true);
                turnToStatusActiveLocked();
                return;
            }
            this.mRecord.recoardEvent("stepSensing: NetWorkType:" + networkTypeToString(this.mNetWorkType) + " NetWorkSwitch:" + networkSwitchToString(this.mNetWorkSwitchState), System.currentTimeMillis(), true);
            this.mUtils.saveDeepSleepNetTypeError(false);
            if (shouldWaittingTrafficStop()) {
                return;
            }
            enterDeepSleepLocked();
            return;
        }
        if (playbackState == 0) {
            this.mRecord.recoardEvent("stepSensing: playbackChecking.  PossibleMusicPlayer: " + arrayList, System.currentTimeMillis(), true);
        } else {
            this.mRecord.recoardEvent("stepSensing: playback", System.currentTimeMillis(), true);
        }
        recheckSystemPlaybackStatus();
        LocalLog.l(TAG, "stepStatusSensing: playbackOrChecking");
    }

    private void supersleepCancelAlarm() {
        LocalLog.a(TAG, "supersleepCancelAlarm");
        Intent intent = new Intent(ACTION_MUSIC_MONITOR);
        intent.setPackage("com.oplus.battery");
        this.mAlarmManager.cancel(PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL));
        Intent intent2 = new Intent(ACTION_FORCESTOP_MONITOR);
        intent2.setPackage("com.oplus.battery");
        this.mAlarmManager.cancel(PendingIntent.getBroadcastAsUser(this.mContext, 0, intent2, 67108864, UserHandle.ALL));
    }

    private void supersleepCancelBatterycurveAlarm() {
        LocalLog.a(TAG, "supersleepCancelBatterycurveAlarm");
        Intent intent = new Intent(ACTION_BATTERY_CURVE_START_MONITOR);
        intent.setPackage("com.oplus.battery");
        this.mAlarmManager.cancel(PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 67108864, UserHandle.ALL));
        Intent intent2 = new Intent(ACTION_BATTERY_CURVE_END_MONITOR);
        intent2.setPackage("com.oplus.battery");
        this.mAlarmManager.cancel(PendingIntent.getBroadcastAsUser(this.mContext, 0, intent2, 67108864, UserHandle.ALL));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void testForceGetAIPredictReal() {
        this.mIsAiForecast = false;
        if (!forecastSleepDurationAI()) {
            forecastSleepDurationNoAI();
        }
        LocalLog.a(TAG, "testForceGetAIPredict: mIsAiForecast = " + this.mIsAiForecast);
        LocalLog.a(TAG, "testForceGetAIPredict: mPredictedSleepStartTime = " + this.mPredictedSleepStartTime);
        LocalLog.a(TAG, "testForceGetAIPredict: mPredictedSleepEndTime = " + this.mPredictedSleepEndTime);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0057, code lost:
    
        if (r3 >= 6) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void trafficMonitorLocked() {
        int i10 = this.mStatus;
        if (i10 != 3 && i10 != 5) {
            LocalLog.l(TAG, "trafficMonitor:  status=" + statusToString(this.mStatus));
            this.mRecord.recoardEvent("trafficMonitor: error status=" + statusToString(this.mStatus), System.currentTimeMillis(), true);
            return;
        }
        if (!isTrafficSpeedLow()) {
            int i11 = this.mStatus;
            if (i11 == 3) {
                int i12 = this.mCntTrafficCheck;
                this.mCntTrafficCheck = i12 + 1;
            }
            if (i11 != 5) {
                this.mRecord.recoardEvent("trafficMonitor: traffic speed not low. do not check traffic", System.currentTimeMillis(), true);
            }
            setTrafficMonitorAlarm(System.currentTimeMillis() + 300000);
            this.mRecord.recoardEvent("trafficMonitor: traffic speed not low. recheck traffic", System.currentTimeMillis(), true);
            return;
        }
        LocalLog.a(TAG, "trafficMonitor:  listTraffic=" + this.mListTraffic + ", listTrafficStartTime=" + this.mListTrafficStartTime);
        this.mListTraffic.clear();
        this.mListTrafficStartTime.clear();
        this.mGuardElfDataManager.k(this.mListTraffic, "app_traffic_deepsleep.xml");
        this.mGuardElfDataManager.k(this.mListTrafficStartTime, "app_traffic_deepsleep_start_time.xml");
        this.mRecord.recoardEvent("trafficMonitor: appTraffitcTurnToStop", System.currentTimeMillis(), true);
        appTraffitcTurnToStopLocked();
        this.mUtils.saveTrafficLowDetected(true);
    }

    private void turnToStatusActiveLocked() {
        LocalLog.a(TAG, "turnToStatusActive: status = " + statusToString(this.mStatus));
        int i10 = this.mStatus;
        if (i10 == 2) {
            unregSignificantMotion();
        } else if (i10 == 3) {
            unregSignificantMotion();
            cancelTrafficMonitorAlarm();
            calcWaittingTrafficStopDuration();
        } else if (i10 == 4) {
            restoreNetWork(true);
            this.mNetWorkType = 0;
            this.mUtils.saveLocalNetWorkType(0);
            unregSignificantMotion();
            this.mUtils.saveDeepSleepStatus(0);
            this.mIsWakeFromDeepSleep = true;
            cancelStartTrafficNextDozeMaintenanceAlarm();
            this.mIsMaintenanceRestored = false;
            this.mUtils.saveDeepSleepIsMaintenceReStored(false);
        } else if (i10 == 5) {
            this.mNetWorkType = 0;
            this.mUtils.saveLocalNetWorkType(0);
            canceAppTrafficStartTimeoutAlarm();
            unregSignificantMotion();
            cancelTrafficMonitorAlarm();
            this.mUtils.saveDeepSleepStatus(0);
            calcAppTrafficDuration();
        }
        cancelAlarm();
        if (this.mIsScreenOff && this.mUtils.getExtremeSleepStatus() && this.mStatus == 4) {
            changeStatus(1);
            return;
        }
        rmvSimuTrafficPkgJob();
        changeStatus(0);
        reconfirmDeepSleepStatus();
        reconfirmNetSwitch();
    }

    private void turnToStatusInactiveLocked(boolean z10) {
        LocalLog.a(TAG, "turnToStatusInactiveLocked");
        this.mListPkgPlayback.clear();
        this.mListPkgAudioIn.clear();
        this.mCntMotionDetected = 0;
        this.mCntPlaybackCheck = 0;
        this.mCntNavigatingCheck = 0;
        this.mCntTrafficCheck = 0;
        this.mCntDisableNetUnexpected = 0;
        this.mDisableNetAllowed = false;
        this.mRedisableNetUnusable = false;
        this.mRedisabledMobNet = false;
        this.mRedisabledWifi = false;
        this.mPkgRestoreNet = null;
        this.mForecastSleepDurationHandled = false;
        if (z10 && isShortScreenOn()) {
            this.mWorkHandler.sendEmptyMessage(15);
        } else {
            schedueAlarm(System.currentTimeMillis() + 600000, false);
        }
        changeStatus(1);
        resetStatisticsVal();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregSignificantMotion() {
        if (this.mSignificantSensor == null) {
            LocalLog.a(TAG, "unregSignificantMotion: SignificantMotionSensor is null.");
        } else if (this.mSensorManager != null) {
            LocalLog.a(TAG, "unregSignificantMotion");
            this.mSensorManager.cancelTriggerSensor(this.mSignicantMotionListener, this.mSignificantSensor);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWhiteList() {
        int i10;
        this.mListWhitelistPkg.clear();
        this.mUtils.getDeepSleepWhiteList(this.mContext, this.mListWhitelistPkg);
        if (this.mListWhitelistPkg.isEmpty() || this.mListTraffic.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        int size = this.mListTraffic.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            }
            String[] split = this.mListTraffic.get(size).split(":");
            if (split.length != 0) {
                String str = split[0];
                if (!this.mListWhitelistPkg.contains(str) && !arrayList.contains(str)) {
                    arrayList.add(str);
                }
            }
        }
        for (i10 = 0; i10 < arrayList.size(); i10++) {
            Message obtainMessage = this.mWorkHandler.obtainMessage();
            obtainMessage.what = 7;
            Bundle bundle = new Bundle();
            bundle.putString(DeviceDomainManager.ARG_PKG, (String) arrayList.get(i10));
            bundle.putString("req", "stop");
            obtainMessage.setData(bundle);
            this.mWorkHandler.sendMessage(obtainMessage);
            LocalLog.a(TAG, "updateWhiteList: rmv " + ((String) arrayList.get(i10)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadAiStatistics() {
        long screenoffTimeAiDcs = this.mUtils.getScreenoffTimeAiDcs();
        this.mUtils.saveScreenoffTimeAiDcs(0L);
        if (this.mUtils.getIsTestMode()) {
            LocalLog.a(TAG, "uploadAiStatistics: testmode");
            return;
        }
        if (this.mLastWakeupTime == Long.MIN_VALUE) {
            LocalLog.a(TAG, "uploadAiStatistics: wrong wakeup time");
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.mLastWakeupTime;
        HashMap hashMap = new HashMap();
        hashMap.put("screenOnDuration", String.valueOf(elapsedRealtime));
        if (elapsedRealtime <= 60000) {
            hashMap.put("screenOnLevel", "LessThanOneMin");
        } else if (elapsedRealtime <= 180000) {
            hashMap.put("screenOnLevel", "OneToThreeMin");
        } else if (elapsedRealtime <= 300000) {
            hashMap.put("screenOnLevel", "ThreeToFiveMin");
        } else if (elapsedRealtime <= 600000) {
            hashMap.put("screenOnLevel", "FiveToTenMin");
        } else {
            hashMap.put("screenOnLevel", "MoreThanTenMin");
        }
        long currentTimeMillis = System.currentTimeMillis() - elapsedRealtime;
        Calendar calendar = Calendar.getInstance();
        hashMap.put("screenOnTime", String.valueOf(currentTimeMillis));
        eventMapPutTimeKind(hashMap, calendar, "screenOnTimeKind", currentTimeMillis);
        hashMap.put("screenOffTime", String.valueOf(screenoffTimeAiDcs));
        eventMapPutTimeKind(hashMap, calendar, "screenOffTimeKind", screenoffTimeAiDcs);
        long aiSleepStartTimeDcs = this.mUtils.getAiSleepStartTimeDcs();
        long aiSleepEndTimeDcs = this.mUtils.getAiSleepEndTimeDcs();
        hashMap.put("predictSleepStart", String.valueOf(aiSleepStartTimeDcs));
        eventMapPutTimeKind(hashMap, calendar, "predictSleepStartKind", aiSleepStartTimeDcs);
        hashMap.put("predictSleepEnd", String.valueOf(aiSleepEndTimeDcs));
        eventMapPutTimeKind(hashMap, calendar, "predictSleepEndKind", aiSleepEndTimeDcs);
        boolean z10 = currentTimeMillis < aiSleepEndTimeDcs - RESERVED_EXIT_DEEPSLEEP_TIME;
        hashMap.put("isInPredictDuration", String.valueOf(z10));
        hashMap.put("isUserPresent", String.valueOf(this.mIsUserPresent));
        boolean z11 = (this.mIsUserPresent && z10) ? false : true;
        if ((!z11 || !z10) && !this.mUtils.getDeepSleepAlreadyCheckUpload()) {
            hashMap.put("aiPredictResult", String.valueOf(z11));
            this.mUtils.saveDeepSleepAlreadyCheckUpload(true);
        }
        LocalLog.a(TAG, "upload: eventMap = " + hashMap);
        this.mUploadDataUtil.b(hashMap);
    }

    private void uploadDeepSleepstatistics(boolean z10, boolean z11, String str) {
        long j10;
        long s02 = f.s0(this.mContext);
        if (s02 > 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime() - s02;
            if (!"LongScreenOff".equals(str)) {
                f.O2(this.mContext, 0L);
            }
            j10 = elapsedRealtime;
        } else {
            j10 = 0;
        }
        boolean everEnteredSensing = this.mUtils.getEverEnteredSensing();
        boolean isTestMode = this.mUtils.getIsTestMode();
        LocalLog.a(TAG, "upload: screenOffDuration = " + j10 + ", everEnteredSensing = " + everEnteredSensing + ", reboot = " + z10 + ", testMode = " + isTestMode);
        if (everEnteredSensing) {
            this.mUtils.saveEverEnteredSensing(false);
        }
        if (isTestMode || j10 <= MIN_UPLOAD_SCREENOFF_DURATION) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        HashMap hashMap = new HashMap();
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb2 = new StringBuilder(128);
        hashMap.put("isScreenOffNow", String.valueOf(z11));
        hashMap.put("screenOnTime", String.valueOf(currentTimeMillis));
        eventMapPutTimeKind(hashMap, calendar, "screenOnTimeKind", currentTimeMillis);
        long r02 = f.r0(this.mContext);
        hashMap.put("screenOffTime", String.valueOf(r02));
        eventMapPutTimeKind(hashMap, calendar, "screenOffTimeKind", r02);
        hashMap.put("screenOffDuration", String.valueOf(j10));
        eventMapPutDurationKind(hashMap, sb2, "screenoffDurationKind", j10);
        long predictStartTime = this.mUtils.getPredictStartTime();
        long predictEndTime = this.mUtils.getPredictEndTime();
        hashMap.put("predictSleepStart", String.valueOf(predictStartTime));
        eventMapPutTimeKind(hashMap, calendar, "predictSleepStartKind", predictStartTime);
        hashMap.put("predictSleepEnd", String.valueOf(predictEndTime));
        eventMapPutTimeKind(hashMap, calendar, "predictSleepEndKind", predictEndTime);
        long j11 = this.mPredictedSleepEndTime - this.mPredictedSleepStartTime;
        hashMap.put("predictSleepDuration", String.valueOf(j11));
        eventMapPutDurationKind(hashMap, sb2, "predictSleepDurationKind", j11);
        long deepSleepEntertTime = this.mUtils.getDeepSleepEntertTime();
        hashMap.put("deepSleepEnterTime", String.valueOf(deepSleepEntertTime));
        eventMapPutTimeKind(hashMap, calendar, "deepSleepEnterTimeKind", deepSleepEntertTime);
        long disNetDuration = this.mUtils.getDisNetDuration();
        hashMap.put("netDisableDuration", String.valueOf(disNetDuration));
        eventMapPutDurationKind(hashMap, sb2, "netDisableDurationKind", disNetDuration);
        hashMap.put("appTrafficDuration", String.valueOf(this.mUtils.getAppTrafficDuration()));
        hashMap.put("waitTrafficStopDuration", String.valueOf(this.mUtils.getWaitTrafficStopDuration()));
        hashMap.put("isAIPredict", String.valueOf(this.mIsAiForecast));
        hashMap.put("reboot", String.valueOf(z10));
        hashMap.put("connectivityType", networkTypeToString(this.mUtils.getLocalNetWorkTypeBak()));
        hashMap.put("netSwitchType", networkSwitchToString(this.mUtils.getLocalNetWorkSwitchState()));
        hashMap.put("playbackDetected", String.valueOf(this.mUtils.getPlaybackDetected()));
        hashMap.put("internetPhoneDetected", String.valueOf(this.mUtils.getInternetPhoneDetected()));
        hashMap.put("navigationDetected", String.valueOf(this.mUtils.getNavigationDetected()));
        hashMap.put("motionDetected", String.valueOf(this.mUtils.getMotionDetected()));
        hashMap.put("trafficLowDetected", String.valueOf(this.mUtils.getTrafficLowDetected()));
        hashMap.put("rusNetSwitchNotChange", String.valueOf(!this.mConfigUpdateUtil.i()));
        hashMap.put("countRestorenet", String.valueOf(this.mUtils.getCountRestoreNet()));
        hashMap.put("rusDeepSleepSwitch", String.valueOf(this.mConfigUpdateUtil.k()));
        hashMap.put("settingDeepSleepNetOffSwitch", String.valueOf(f.B(this.mContext)));
        hashMap.put("predictException", this.mUtils.getDeepSleepPredictException());
        hashMap.put("predictWrongTime", String.valueOf(this.mUtils.getDeepSleepPredictWrongTime()));
        hashMap.put("predictShortDuration", String.valueOf(this.mUtils.getDeepSleepPredictShortDuration()));
        hashMap.put("predictSelectError", String.valueOf(this.mUtils.getDeepSleepPredictSelectError()));
        hashMap.put("predictTimeOriginal", this.mUtils.getDeepSleepPredictOriginal());
        hashMap.put("isUserPresent", String.valueOf(this.mIsUserPresent));
        hashMap.put("isInPredictDuration", String.valueOf(currentTimeMillis > this.mPredictedSleepStartTime && currentTimeMillis < this.mPredictedSleepEndTime - RESERVED_EXIT_DEEPSLEEP_TIME));
        hashMap.put("isVoWifiRegistered", String.valueOf(this.mUtils.getDeepSleepIsVoWifiRegistered()));
        hashMap.put("isSpecialAppInstalled", String.valueOf(this.mUtils.getDeepSleepIsSpecialAppInstalled()));
        hashMap.put("isWifiApOn", String.valueOf(this.mUtils.getDeepSleepIsWifiApOn()));
        hashMap.put("isWlanSharingOn", String.valueOf(this.mUtils.getDeepSleepIsWlanSharingOn()));
        hashMap.put("isBtTheringOn", String.valueOf(this.mUtils.getDeepSleepIsBtTheringOn()));
        hashMap.put("isUsbTheringOn", String.valueOf(this.mUtils.getDeepSleepIsUsbTheringOn()));
        hashMap.put("isMigrageDisplaygOn", String.valueOf(this.mUtils.getDeepSleepIsMigrageDisplaygOn()));
        hashMap.put("isDisableNetAllowed", String.valueOf(this.mUtils.getDeepSleepIsDisableNetAllowed()));
        hashMap.put("isNetTypeError", String.valueOf(this.mUtils.getDeepSleepNetTypeError()));
        hashMap.put("redisableNetUnusable", String.valueOf(this.mRedisableNetUnusable));
        hashMap.put("redisabledMobNet", String.valueOf(this.mRedisabledMobNet));
        hashMap.put("redisabledWifi", String.valueOf(this.mRedisabledWifi));
        hashMap.put("hasSignificantSensor", String.valueOf(this.mSignificantSensor != null));
        hashMap.put("uploadReason", str);
        hashMap.put("everEnteredDeepsleep", String.valueOf(this.mUtils.getEverEnteredDeepsleep()));
        hashMap.put("everEnteredSensing", String.valueOf(everEnteredSensing));
        hashMap.put("rcdEvents", this.mRecord.getRcdEvents());
        hashMap.put("version", "8.4");
        LocalLog.a(TAG, "upload: eventMap = " + hashMap);
        this.mUploadDataUtil.v(hashMap);
    }

    private void uploadNetWorkSwitchOnOff(boolean z10, String str, boolean z11, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("switchType", str);
        hashMap.put("switchOnOff", z10 ? "on" : "off");
        hashMap.put("isScreenOn", String.valueOf(this.mPowerManager.isInteractive()));
        hashMap.put("reallyExecute", String.valueOf(z11));
        hashMap.put("deepsleepStatus", statusToString(this.mStatus));
        hashMap.put("reason", str2);
        hashMap.put("extremeSleepStatus", String.valueOf(this.mUtils.getExtremeSleepStatus()));
        Calendar calendar = Calendar.getInstance();
        long currentTimeMillis = System.currentTimeMillis();
        eventMapPutTimeKind(hashMap, calendar, "deepSleepEnterTime", this.mUtils.getDeepSleepEntertTime());
        eventMapPutTimeKind(hashMap, calendar, "nowCurrentTime", currentTimeMillis);
        this.mUploadDataUtil.u(hashMap);
    }

    public void calDeepSleepStatus() {
        if (!this.mUtils.getDeepSleepPredictFailed()) {
            this.mUtils.saveDeepSleepCheckStatus(true);
        } else {
            this.mUtils.saveDeepSleepCheckStatus(false);
            this.mUtils.saveDeepSleepPredictFailed(false);
        }
    }

    public void dumpRcdEvent(PrintWriter printWriter) {
        printWriter.println("deepsleepRcdEvevent:");
        if (this.mRecord.getRcdEvents() != null) {
            printWriter.println(this.mRecord.getRcdEvents());
        }
    }

    public void dumpStatus(PrintWriter printWriter) {
        printWriter.println("status: " + statusToString(this.mStatus));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.mNextAlarmTime);
        printWriter.println("alarm time: " + calendar.getTime());
        long j10 = this.mPredictedSleepStartTime;
        long j11 = this.mPredictedSleepEndTime;
        calendar.setTimeInMillis(j10);
        printWriter.println("sleep start time: " + calendar.getTime());
        calendar.setTimeInMillis(j11);
        printWriter.println("sleep end time: " + calendar.getTime());
        printWriter.println("AI forecast: " + this.mIsAiForecast);
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        boolean z10 = activeNetworkInfo != null && activeNetworkInfo.getType() == 0;
        boolean z11 = activeNetworkInfo != null && activeNetworkInfo.getType() == 1;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("wifi: ");
        sb2.append(z11 ? "connected" : "unconnected");
        printWriter.println(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("mobile data: ");
        sb3.append(z10 ? "connected" : "unconnected");
        printWriter.println(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("wifi switch: ");
        sb4.append(this.mWifiManager.isWifiEnabled() ? "on" : "off");
        printWriter.println(sb4.toString());
        if (ContextCompat.a(this.mContext, "android.permission.READ_PHONE_STATE") != 0) {
            printWriter.println("mobile data don't have permission");
        } else {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("mobile data switch: ");
            sb5.append(this.mTelephonyManager.isDataEnabled() ? "on" : "off");
            printWriter.println(sb5.toString());
        }
        synchronized (this.mLock) {
            for (int i10 = 0; i10 < this.mListTraffic.size(); i10++) {
                printWriter.println("traffic app: " + this.mListTraffic.get(i10));
            }
            printWriter.println("traffic_app_size: " + this.mListTraffic.size());
            for (int i11 = 0; i11 < this.mListTrafficStartTime.size(); i11++) {
                printWriter.println("traffic app time: " + this.mListTrafficStartTime.get(i11));
            }
            printWriter.println("traffic_app_time_size: " + this.mListTrafficStartTime.size());
            for (int i12 = 0; i12 < this.mListTrafficAutoStop.size(); i12++) {
                printWriter.println("traffic autostop: " + this.mListTrafficAutoStop.get(i12));
            }
            printWriter.println("traffic_autostop_size: " + this.mListTrafficAutoStop.size());
            for (int i13 = 0; i13 < this.mListWhitelistPkg.size(); i13++) {
                printWriter.println("wl: " + this.mListWhitelistPkg.get(i13));
            }
            printWriter.println("wl_size: " + this.mListWhitelistPkg.size());
        }
    }

    public void handleDeepSleepCheck() {
        this.mIsWakeFromDeepSleep = false;
        calDeepSleepStatus();
        int deepSleepCheckDate = this.mUtils.getDeepSleepCheckDate();
        boolean deepSleepCheckStatus = this.mUtils.getDeepSleepCheckStatus(deepSleepCheckDate);
        float calRecentFailed = calRecentFailed();
        LocalLog.a(TAG, "DeepSleepCheck: failedRatio = " + calRecentFailed);
        if (!this.mTestMode) {
            this.mIsAiForecast = this.mUtils.getIsAiPredict();
        }
        if (this.mIsAiForecast) {
            if (calRecentFailed > this.mConfigUpdateUtil.A() / ONE_HUNDRED_PERCENT) {
                this.mUtils.saveImprovedDeepSleepNetWorkSwitch(false);
                LocalLog.a(TAG, "DeepSleepCheck: failed too frequently, Threshold = " + this.mConfigUpdateUtil.A());
            }
        } else if (calRecentFailed > this.mConfigUpdateUtil.B() / ONE_HUNDRED_PERCENT) {
            this.mUtils.saveImprovedDeepSleepNetWorkSwitch(false);
            LocalLog.a(TAG, "DeepSleepCheck: failed too frequently and noAI, Threshold = " + this.mConfigUpdateUtil.B());
        }
        if (calRecentFailed <= this.mConfigUpdateUtil.z() / ONE_HUNDRED_PERCENT) {
            this.mUtils.saveImprovedDeepSleepNetWorkSwitch(true);
            LocalLog.a(TAG, "DeepSleepCheck: disable network again, Threshold = " + this.mConfigUpdateUtil.z());
        }
        LocalLog.a(TAG, "DeepSleepCheck: ImprovedDisableNetSwitchNow = " + this.mUtils.getImprovedDeepSleepNetWorkSwitch());
        this.mUtils.savePredictResultSettingProvider(deepSleepCheckStatus);
        DeepSleepUtils deepSleepUtils = this.mUtils;
        deepSleepUtils.saveSleepFirstFailTimeSettingProvider(deepSleepUtils.getDeepSleepFirstFailTime());
        DeepSleepUtils deepSleepUtils2 = this.mUtils;
        deepSleepUtils2.saveSleepLastFailTimeSettingProvider(deepSleepUtils2.getDeepSleepLastFailTime());
        if (deepSleepCheckDate == 7) {
            this.mUtils.saveDeepSleepCheckDate(1);
        } else {
            this.mUtils.saveDeepSleepCheckDate(deepSleepCheckDate + 1);
        }
        this.mUtils.saveDeepSleepAlreadyCheckUpload(false);
        this.mUtils.saveDeepSleepEnterCount(0);
        this.mUtils.saveDeepSleepFirstFailTime(Long.MIN_VALUE);
        this.mUtils.saveDeepSleepLastFailTime(Long.MIN_VALUE);
    }

    public void onAppTrafficChange(String str, String str2, String str3, boolean z10) {
        if (LocalLog.f()) {
            LocalLog.a(TAG, "onAppTrafficChange: pkg=" + str + ", job=" + str2 + ", req=" + str3);
        }
        if (str == null || str3 == null) {
            return;
        }
        this.mWakeLock.acquire(5000L);
        Message obtainMessage = this.mWorkHandler.obtainMessage();
        obtainMessage.what = 7;
        Bundle bundle = new Bundle();
        bundle.putString(DeviceDomainManager.ARG_PKG, str);
        bundle.putString("req", str3);
        bundle.putBoolean("autoStop", z10);
        if (str2 != null) {
            bundle.putString("job", str2);
        }
        obtainMessage.setData(bundle);
        this.mWorkHandler.sendMessage(obtainMessage);
    }

    public void onBootComplete() {
        this.mWorkHandler.sendEmptyMessage(8);
    }

    public void onRestoreNetworkReq(String str) {
        if (str != null) {
            if (this.mTriggerForMaintence) {
                if (!str.equals("oplus.intent.action.idle_maintenance") || this.mIsMaintenanceRestored) {
                    return;
                }
            } else if (str.equals("oplus.intent.action.idle_maintenance")) {
                return;
            }
        }
        this.mWakeLock.acquire(5000L);
        Message obtainMessage = this.mWorkHandler.obtainMessage();
        obtainMessage.what = 6;
        Bundle bundle = new Bundle();
        if (str != null) {
            bundle.putString(DeviceDomainManager.ARG_PKG, str);
        }
        obtainMessage.setData(bundle);
        this.mWorkHandler.sendMessage(obtainMessage);
        LocalLog.a(TAG, "onRestoreNetworkReq: pkg=" + str);
    }

    public void onScreenOff(boolean z10) {
        this.mIsScreenOnInvokedLastTime.set(false);
        if (this.mWorkHandler.hasMessages(13)) {
            this.mWorkHandler.removeMessages(13);
            this.mWorkHandler.sendEmptyMessage(13);
        }
        if (f.s0(this.mContext) != 0) {
            f.O2(this.mContext, 0L);
        }
        if (z10) {
            this.mRecord.recoardEvent("isCharging", System.currentTimeMillis(), true);
            LocalLog.a(TAG, "onScreenOff: charging");
            return;
        }
        if (this.mSignificantSensor == null) {
            this.mRecord.recoardEvent("noSignificantSensor", System.currentTimeMillis(), false);
            LocalLog.a(TAG, "onScreenOff: no significant sensor!");
            return;
        }
        if (b.D()) {
            if (!this.mUtils.getExtremeSleepStatus() && !isDeepSleepEnabled()) {
                return;
            }
        } else if (!isDeepSleepEnabled()) {
            this.mRecord.recoardEvent("rusDeepSleepDisable", System.currentTimeMillis(), true);
            return;
        }
        int callState = ((TelecomManager) this.mContext.getSystemService("telecom")).getCallState();
        if (callState != 0) {
            LocalLog.a(TAG, "onScreenOff: callstate=" + callState);
            this.mRecord.recoardEvent("callState:" + String.valueOf(callState), System.currentTimeMillis(), true);
            return;
        }
        if (!AppFeature.f() && !f.q1(this.mContext)) {
            if (this.mUtils.getExtremeSleepStatus()) {
                extremeSleepStatus();
                return;
            }
            if (b.D()) {
                setBatterycurveStatus();
            }
            synchronized (this.mLock) {
                this.mWakeLock.acquire();
                f.O2(this.mContext, SystemClock.elapsedRealtime());
                this.mTimeScreenOff = System.currentTimeMillis();
                if (this.mStatus != 0) {
                    LocalLog.a(TAG, "onScreenOff: status= " + statusToString(this.mStatus));
                    turnToStatusActiveLocked();
                }
                turnToStatusInactiveLocked(true);
                this.mIsScreenOff = true;
                this.mIsUserPresent = false;
                this.mIsWakeFromDeepSleep = false;
                this.mEverEnterDeepIdle = false;
                this.mWakeLock.release();
            }
            return;
        }
        LocalLog.l(TAG, "onScreenOff: Feature Disable DeepSleep");
        this.mRecord.recoardEvent("EnterpriseDisableDeepsleep:", System.currentTimeMillis(), false);
    }

    public void onScreenOn() {
        if (this.mIsScreenOnInvokedLastTime.get()) {
            return;
        }
        this.mIsScreenOnInvokedLastTime.set(true);
        LocalLog.a(TAG, "onScreenOn");
        synchronized (this.mLock) {
            if (this.mUtils.getExtremeSleepStatus()) {
                this.mSystemPlaybackStatus = false;
                this.mWaitingTrafficStop = false;
                this.mIsScreenOff = false;
            }
            turnToStatusActiveLocked();
            this.mIsScreenOff = false;
            uploadDeepSleepstatistics(false, false, "ScreenOn");
            if (this.mUtils.getScreenoffTimeAiDcs() != 0) {
                this.mWorkHandler.sendEmptyMessageDelayed(13, 600000L);
            }
            this.mLastWakeupTime = SystemClock.elapsedRealtime();
            maybeNeedShowNetOffNotify();
            if (b.D() && SuperSleepStatsHelper.getInstance(this.mContext).isSupportBatteryOptimize()) {
                supersleepCancelBatterycurveAlarm();
                SuperSleepStatsHelper.getInstance(this.mContext).batteryCurveOptimize(0);
            }
        }
    }

    public void onShutDown() {
        synchronized (this.mLock) {
            int i10 = this.mStatus;
            if (i10 == 4) {
                calcDisNetDuration();
            } else if (i10 == 5) {
                calcAppTrafficDuration();
            } else if (i10 == 3) {
                calcWaittingTrafficStopDuration();
            }
            if (this.mUtils.getScreenoffTimeAiDcs() != 0) {
                this.mUtils.saveScreenoffTimeAiDcs(0L);
            }
            if (this.mStatus != 0) {
                this.mRecord.recoardEvent("ShutDown", System.currentTimeMillis(), true);
                uploadDeepSleepstatistics(false, this.mIsScreenOff, "ShutDown");
            }
        }
    }

    public void onWhiteListChanged() {
        LocalLog.a(TAG, "onWhiteListChanged");
        this.mWorkHandler.sendEmptyMessageDelayed(12, 10000L);
    }

    public void setSimuAIPredict(boolean z10) {
        this.mSimuAIPredict = z10;
        LocalLog.a(TAG, "set Simu AI Predict: " + z10);
    }

    public void setSleepFailCount(int i10) {
        this.mUtils.saveRecentFailCountSettingProvider(i10);
    }

    public void setSleepFirstFailTime(long j10) {
        this.mUtils.savePredictResultSettingProvider(false);
        this.mUtils.saveSleepFirstFailTimeSettingProvider(j10);
    }

    public void setSleepLastFailTime(long j10) {
        this.mUtils.savePredictResultSettingProvider(false);
        this.mUtils.saveSleepLastFailTimeSettingProvider(j10);
    }

    public void setSleepSuccess() {
        this.mUtils.savePredictResultSettingProvider(true);
        this.mUtils.saveSleepFirstFailTimeSettingProvider(Long.MIN_VALUE);
        this.mUtils.saveSleepLastFailTimeSettingProvider(Long.MIN_VALUE);
    }

    public void setTestMode(boolean z10, boolean z11) {
        this.mTestMode = z10;
        this.mIgnoreTraffic = z11;
        LocalLog.a(TAG, "set Test Mode: testMode = " + z10 + ", ignoreTraffic=" + z11);
    }

    public void setTestSleepDuration(long j10, long j11) {
        this.mTestSleepStartTime = j10;
        this.mTestSleepEndTime = j11;
        LocalLog.a(TAG, "setTestSleepDuration: startTime=" + printTimeInMillis(j10));
        LocalLog.a(TAG, "setTestSleepDuration: endTime=" + printTimeInMillis(j11));
    }

    public void setTriggerForOneMaintence() {
        LocalLog.a(TAG, "setTriggerForOneMaintence");
        this.mTriggerForMaintence = true;
    }

    public void testForceGetAIPredict() {
        this.mWorkHandler.sendEmptyMessage(18);
    }

    public void testForceStep() {
        LocalLog.l(TAG, "testForceStep");
        cancelAlarm();
        this.mWorkHandler.sendEmptyMessage(1);
    }

    public void testSleepDurationSelect() {
        ArrayList arrayList = new ArrayList();
        Random random = new Random();
        for (int i10 = 0; i10 < 5; i10++) {
            arrayList.add(new Pair<>(Double.valueOf(random.nextDouble() * HOURS_ONE_DAY), Double.valueOf(random.nextDouble() * HOURS_ONE_DAY)));
        }
        long currentTimeMillis = System.currentTimeMillis();
        List<Pair<Long, Long>> parseAISleepDuration = parseAISleepDuration(arrayList, currentTimeMillis);
        if (parseAISleepDuration != null && !parseAISleepDuration.isEmpty()) {
            selectAISleepDuration(parseAISleepDuration, currentTimeMillis);
        } else {
            LocalLog.a(TAG, "testSleepDurationSelect: listSleepDuration is empty.");
        }
    }
}
