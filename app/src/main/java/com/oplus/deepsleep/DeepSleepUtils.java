package com.oplus.deepsleep;

import android.app.OplusNotificationManager;
import android.app.OplusWhiteListManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothPan;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.Debug;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Formatter;
import androidx.core.content.ContextCompat;
import b6.LocalLog;
import com.oplus.deepsleep.data.DeepSleepSharepref;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import d6.ConfigUpdateUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import y5.b;

/* loaded from: classes.dex */
public class DeepSleepUtils {
    private static final String ACTION_CHECK_MUSIC_PLAYER = "oplus.intent.action.deepsleep.CheckMusicPlayer";
    public static final int AI_SLEEP_DEFAULT_SLEEP_PERCENT = 5;
    public static final int AI_SLEEP_DEFAULT_WAKE_PERCENT = 8;
    public static final int AUDIO_STATE_INVALID = -1;
    public static final int AUDIO_STATE_PLAYBACK = 2;
    public static final int AUDIO_STATE_SILENCE = 1;
    public static final int AUDIO_STATE_UNKNOWN = 0;
    private static final int INT_DAY_FIVE = 5;
    private static final int INT_DAY_FOUR = 4;
    private static final int INT_DAY_ONE = 1;
    private static final int INT_DAY_SEVEN = 7;
    private static final int INT_DAY_SIX = 6;
    private static final int INT_DAY_THREE = 3;
    private static final int INT_DAY_TWO = 2;
    private static final int OPLUS_WHITELIST_TYPE_MIRAGE_DISPLAY = 101;
    private static final int PHONEID_ONE = 0;
    private static final int PHONEID_TWO = 1;
    private static final String PREFIX_AUDIO_STATE = "AudioState:";
    private static final String PREFIX_POSSIBLE_MUSIC_PLAYER = "PossibleMusicPlayer:";
    private static final String PROP_NETWORK_DISABLE_TIME = "sys.deepsleep.disable.network";
    private static final String PROP_NETWORK_RESTORE_TIME = "sys.deepsleep.restore.network";
    private static final String STR_AI_SLEEP_END_TIME_DCS = "deep_sleep_ai_sleep_end_dcs";
    private static final String STR_AI_SLEEP_START_TIME_DCS = "deep_sleep_ai_sleep_start_dcs";
    private static final String STR_APP_TRAFFIC_DURATION = "deep_sleep_app_traffic_duration";
    private static final String STR_APP_TRAFFIC_ENTER_TIME = "deep_sleep_app_traffic_enter_time";
    private static final String STR_COUNT_RESTORE_NET = "deep_sleep_count_restore_net";
    private static final String STR_DEEP_SLEEP_CHECK_ALREADY_UPLOAD = "deep_sleep_check_already_upload";
    private static final String STR_DEEP_SLEEP_ENTER_COUNT = "deep_sleep_enter_count";
    private static final String STR_DEEP_SLEEP_ENTER_TIME = "deep_sleep_enter_time";
    private static final String STR_DEEP_SLEEP_FIRST_FAIL_TIME = "deep_sleep_first_fail_time";
    private static final String STR_DEEP_SLEEP_IDLE_MAINTENCE_RESTORED = "oplus_deep_sleep_is_idle_maintence_restore_network";
    private static final String STR_DEEP_SLEEP_IS_BT_THERNERING_ON = "deep_sleep_is_bt_tethering_on";
    private static final String STR_DEEP_SLEEP_IS_DISABLE_NET_ALLOWED = "deep_sleep_is_disable_net_allowed";
    private static final String STR_DEEP_SLEEP_IS_MIGRAGE_DISPLAY_ON = "deep_sleep_is_migrage_display_on";
    private static final String STR_DEEP_SLEEP_IS_USB_THERNERING_ON = "deep_sleep_is_usb_tethering_on";
    private static final String STR_DEEP_SLEEP_IS_VOWIFI_REGISTERED = "deep_sleep_is_VoWifi_Registered";
    private static final String STR_DEEP_SLEEP_IS_WIFI_AP_ON = "deep_sleep_is_WifiAp_on";
    private static final String STR_DEEP_SLEEP_IS_WLAN_SHARING_ON = "deep_sleep_is_WLANSharing_on";
    private static final String STR_DEEP_SLEEP_LAST_FAIL_TIME = "deep_sleep_last_fail_time";
    private static final String STR_DEEP_SLEEP_NET_TYPE_ERROR = "deep_sleep_is_net_type_error";
    private static final String STR_DEEP_SLEEP_PREDICT_CHECK_DATE = "deep_sleep_check_date";
    private static final String STR_DEEP_SLEEP_PREDICT_EXCEPTION = "deep_sleep_predict_exception";
    private static final String STR_DEEP_SLEEP_PREDICT_FAIL = "deep_sleep_is_failed";
    private static final String STR_DEEP_SLEEP_PREDICT_ORIGINAL = "deep_sleep_predict_original";
    private static final String STR_DEEP_SLEEP_PREDICT_SELECT_ERROR = "deep_sleep_predict_select_error";
    private static final String STR_DEEP_SLEEP_PREDICT_SHORT_DURATION = "deep_sleep_predict_short_duration";
    private static final String STR_DEEP_SLEEP_PREDICT_STATUS_DAYFIVE = "deep_sleep_day_five";
    private static final String STR_DEEP_SLEEP_PREDICT_STATUS_DAYFOUR = "deep_sleep_day_four";
    private static final String STR_DEEP_SLEEP_PREDICT_STATUS_DAYONE = "deep_sleep_day_one";
    private static final String STR_DEEP_SLEEP_PREDICT_STATUS_DAYSEVEN = "deep_sleep_day_seven";
    private static final String STR_DEEP_SLEEP_PREDICT_STATUS_DAYSIX = "deep_sleep_day_six";
    private static final String STR_DEEP_SLEEP_PREDICT_STATUS_DAYTHREE = "deep_sleep_day_three";
    private static final String STR_DEEP_SLEEP_PREDICT_STATUS_DAYTWO = "deep_sleep_day_two";
    private static final String STR_DEEP_SLEEP_PREDICT_WRONG_TIME = "deep_sleep_predict_wrong_time";
    private static final String STR_DEEP_SLEEP_SETTINGS_FAILED_TIMES_OF_SEVENDAYS = "oplus_deep_sleep_failedtimes_of_sevendays";
    private static final String STR_DEEP_SLEEP_SETTINGS_FIRST_FAIL_TIME = "oplus_deep_sleep_first_fail_time";
    private static final String STR_DEEP_SLEEP_SETTINGS_LAST_FAIL_TIME = "oplus_deep_sleep_last_fail_time";
    private static final String STR_DEEP_SLEEP_SETTINGS_PREDICT_CHECK = "oplus_deep_sleep_predict_check_status";
    private static final String STR_DEEP_SLEEP_SPECIAL_APP_INSTALLED = "deep_sleep_specialApp_Installed";
    public static final String STR_DEEP_SLEEP_STATUS = "oplus_deep_sleep_status";
    private static final String STR_DIS_NET_DURATION = "deep_sleep_dis_net_duration";
    private static final String STR_DIS_NET_ELAPSED_TIME = "deep_sleep_dis_net_elapsed_time";
    private static final String STR_ENTERED_DEEPSLEEP = "deep_sleep_entered_deepsleep";
    private static final String STR_ENTERED_SENSING = "deep_sleep_entered_sensing";
    private static final String STR_EVER_DIS_NET = "deep_sleep_ever_dis_net";
    private static final String STR_IMPROVED_NETWORK_SWITCH_STATE = "deep_sleep_improved_switch_state";
    private static final String STR_INACTIVE_STATUS = "oplusguardelf_inactive_status";
    private static final String STR_INTERNET_PHONE_DETECTED = "deep_sleep_internet_phone_detected";
    private static final String STR_IS_AI_PREDIC = "deep_sleep_is_aipredict";
    private static final String STR_IS_TEST_MODE = "deep_sleep_is_test_mode";
    private static final String STR_LIGHT_CLEAR_TIME = "deep_sleep_light_clear_time";
    private static final String STR_MORNING_CLEAR_TIME = "deep_sleep_morning_clear_time";
    private static final String STR_MOTION_DETECTED = "deep_sleep_motion_detected";
    private static final String STR_NAVIGATION_DETECTED = "deep_sleep_navigation_detected";
    private static final String STR_NETWORK_DISABLE_STATE = "deepsleep_network_disable_state";
    public static final String STR_NETWORK_SWITCH_STATE = "deepsleep_network_switch";
    public static final String STR_NETWORK_TYPE = "oplus_deepsleep_network_type";
    public static final String STR_NETWORK_TYPE_BAK = "deepsleep_network_type_bak";
    private static final String STR_NEXT_ALARM_TIME = "deep_sleep_next_alarm_time";
    private static final String STR_PLAYBACK_DETECTED = "deep_sleep_playback_detected";
    private static final String STR_PREDICT_END_TIME = "deep_sleep_predict_end_time";
    private static final String STR_PREDICT_START_TIME = "deep_sleep_predict_start_time";
    private static final String STR_RESET_AIR_BY_DEEPSLEEP = "deepsleep_reset_airplane";
    private static final String STR_SCREENOFF_TIME_AI_DCS = "deep_sleep_screenoff_aidcs";
    private static final String STR_STATUS = "deep_sleep_state";
    private static final String STR_SUPER_SLEEP_STATUS = "oplusguardelf_super_sleep_status";
    private static final String STR_TRAFFIC_LOW_DETECTED = "deep_sleep_traffic_low_detected";
    private static final String STR_TRAFFIC_MONITOR_ALARM = "deep_sleep_traffic_monitor_alarm";
    private static final String STR_WAIT_TRAFFIC_STOP_DURATION = "deep_sleep_wait_traffic_stop_duration";
    private static final String STR_WAIT_TRAFFIC_STOP_ENTER_TIME = "deep_sleep_wait_traffic_stop_enter_time";
    private static final String TAG = "DeepSleepUtils";
    private static DeepSleepUtils sDeepSleepUtils;
    private int mAISleepPercent = 5;
    private int mAIWakePercent = 8;
    private final BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private VowifiRegisterListener mVowifiRegisterListenerOne;
    private VowifiRegisterListener mVowifiRegisterListenerTwo;
    public static final List<Integer> AI_SLEEP_PERCENT_LIST = Collections.unmodifiableList(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    private static boolean sIsPanReady = false;
    private static BluetoothPan sPanProfile = null;

    /* loaded from: classes.dex */
    class PanServiceListener implements BluetoothProfile.ServiceListener {
        PanServiceListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i10, BluetoothProfile bluetoothProfile) {
            BluetoothPan unused = DeepSleepUtils.sPanProfile = (BluetoothPan) bluetoothProfile;
            boolean unused2 = DeepSleepUtils.sIsPanReady = true;
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i10) {
            boolean unused = DeepSleepUtils.sIsPanReady = false;
        }
    }

    private DeepSleepUtils(Context context) {
        this.mVowifiRegisterListenerOne = null;
        this.mVowifiRegisterListenerTwo = null;
        this.mContext = context;
        this.mVowifiRegisterListenerOne = new VowifiRegisterListener(context, 0);
        this.mVowifiRegisterListenerTwo = new VowifiRegisterListener(this.mContext, 1);
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        defaultAdapter.getProfileProxy(this.mContext, new PanServiceListener(), 5);
    }

    private File dumpService(Context context, String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        return saveDumpInformation(context, str, new String[]{str2}, "/dump_" + str + "_" + str2 + "_" + System.currentTimeMillis() + ".txt");
    }

    public static synchronized DeepSleepUtils getInstance(Context context) {
        DeepSleepUtils deepSleepUtils;
        synchronized (DeepSleepUtils.class) {
            if (sDeepSleepUtils == null) {
                sDeepSleepUtils = new DeepSleepUtils(context);
            }
            deepSleepUtils = sDeepSleepUtils;
        }
        return deepSleepUtils;
    }

    public static List<String> getMigrateDisplayList(Context context) {
        return new OplusWhiteListManager(context).getGlobalWhiteList(101);
    }

    private boolean isBluetoothPanConnectedInternal() {
        if (sIsPanReady && sPanProfile != null) {
            if (ContextCompat.a(this.mContext, "android.permission.BLUETOOTH_CONNECT") != 0) {
                LocalLog.b(TAG, "BLUETOOTH_CONNECT Permission denied");
                return false;
            }
            boolean isTetheringOn = sPanProfile.isTetheringOn();
            if (!isTetheringOn) {
                return false;
            }
            LocalLog.l(TAG, "isBluetoothPanConnected: isOn = " + isTetheringOn);
            List connectedDevices = sPanProfile.getConnectedDevices();
            if (connectedDevices != null && !connectedDevices.isEmpty()) {
                LocalLog.l(TAG, "isBluetoothPanConnected: listConnected = " + connectedDevices);
                return true;
            }
        }
        return false;
    }

    private File saveDumpInformation(Context context, String str, String[] strArr, String str2) {
        if (strArr == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        File file = new File(context.getFilesDir() + str2);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                Debug.dumpService(str, fileOutputStream.getFD(), strArr);
                fileOutputStream.close();
            } finally {
            }
        } catch (IOException e10) {
            e10.printStackTrace();
        }
        return file;
    }

    public void doCheckPossibleMusicPlayer() {
        Intent intent = new Intent(ACTION_CHECK_MUSIC_PLAYER);
        intent.putExtra("package", "com.oplus.battery");
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    public int getAISleepPercent() {
        return this.mAISleepPercent;
    }

    public int getAIWakePercent() {
        return this.mAIWakePercent;
    }

    public long getAiSleepEndTimeDcs() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_AI_SLEEP_END_TIME_DCS, 0L);
    }

    public long getAiSleepStartTimeDcs() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_AI_SLEEP_START_TIME_DCS, 0L);
    }

    public boolean getAirPlaneModeStatus() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) != 0;
    }

    public long getAppTrafficDuration() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_APP_TRAFFIC_DURATION, 0L);
    }

    public long getAppTrafficEnterElapsedTime() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_APP_TRAFFIC_ENTER_TIME, 0L);
    }

    public int getCountRestoreNet() {
        return DeepSleepSharepref.getIntValue(this.mContext, STR_COUNT_RESTORE_NET, 0);
    }

    public boolean getDeepSleepAlreadyCheckUpload() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_CHECK_ALREADY_UPLOAD, false);
    }

    public int getDeepSleepCheckDate() {
        return DeepSleepSharepref.getIntValue(this.mContext, STR_DEEP_SLEEP_PREDICT_CHECK_DATE, 1);
    }

    public boolean getDeepSleepCheckStatus(int i10) {
        switch (i10) {
            case 1:
                return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYONE, true);
            case 2:
                return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYTWO, true);
            case 3:
                return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYTHREE, true);
            case 4:
                return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYFOUR, true);
            case 5:
                return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYFIVE, true);
            case 6:
                return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYSIX, true);
            case 7:
                return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYSEVEN, true);
            default:
                return true;
        }
    }

    public int getDeepSleepEnterCount() {
        return DeepSleepSharepref.getIntValue(this.mContext, STR_DEEP_SLEEP_ENTER_COUNT, 0);
    }

    public long getDeepSleepEntertTime() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_DEEP_SLEEP_ENTER_TIME, 0L);
    }

    public long getDeepSleepFirstFailTime() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_DEEP_SLEEP_FIRST_FAIL_TIME, Long.MIN_VALUE);
    }

    public boolean getDeepSleepIsBtTheringOn() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_IS_BT_THERNERING_ON, false);
    }

    public boolean getDeepSleepIsDisableNetAllowed() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_IS_DISABLE_NET_ALLOWED, true);
    }

    public boolean getDeepSleepIsMaintenceReStored() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_IDLE_MAINTENCE_RESTORED, false);
    }

    public boolean getDeepSleepIsMigrageDisplaygOn() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_IS_MIGRAGE_DISPLAY_ON, false);
    }

    public boolean getDeepSleepIsSpecialAppInstalled() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_SPECIAL_APP_INSTALLED, false);
    }

    public boolean getDeepSleepIsUsbTheringOn() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_IS_USB_THERNERING_ON, false);
    }

    public boolean getDeepSleepIsVoWifiRegistered() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_IS_VOWIFI_REGISTERED, false);
    }

    public boolean getDeepSleepIsWifiApOn() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_IS_WIFI_AP_ON, false);
    }

    public boolean getDeepSleepIsWlanSharingOn() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_IS_WLAN_SHARING_ON, false);
    }

    public long getDeepSleepLastFailTime() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_DEEP_SLEEP_LAST_FAIL_TIME, Long.MIN_VALUE);
    }

    public boolean getDeepSleepNetTypeError() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_NET_TYPE_ERROR, false);
    }

    public String getDeepSleepPredictException() {
        return DeepSleepSharepref.getStringValue(this.mContext, STR_DEEP_SLEEP_PREDICT_EXCEPTION, "noException");
    }

    public boolean getDeepSleepPredictFailed() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_PREDICT_FAIL, false);
    }

    public String getDeepSleepPredictOriginal() {
        return DeepSleepSharepref.getStringValue(this.mContext, STR_DEEP_SLEEP_PREDICT_ORIGINAL, "nodata");
    }

    public boolean getDeepSleepPredictSelectError() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_PREDICT_SELECT_ERROR, false);
    }

    public boolean getDeepSleepPredictShortDuration() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_PREDICT_SHORT_DURATION, false);
    }

    public boolean getDeepSleepPredictWrongTime() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_DEEP_SLEEP_PREDICT_WRONG_TIME, false);
    }

    public boolean getDeepSleepResetAirPlaneStatus() {
        return Settings.System.getInt(this.mContext.getContentResolver(), STR_RESET_AIR_BY_DEEPSLEEP, 0) != 0;
    }

    public int getDeepSleepStatus() {
        return Settings.System.getInt(this.mContext.getContentResolver(), STR_DEEP_SLEEP_STATUS, 0);
    }

    public void getDeepSleepWhiteList(Context context, List<String> list) {
        ArrayList globalWhiteList;
        if (list == null || (globalWhiteList = new OplusWhiteListManager(context).getGlobalWhiteList(6)) == null || globalWhiteList.isEmpty()) {
            return;
        }
        list.addAll(globalWhiteList);
    }

    public long getDisNetDuration() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_DIS_NET_DURATION, 0L);
    }

    public long getDisNetElapsedTime() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_DIS_NET_ELAPSED_TIME, 0L);
    }

    public boolean getEverDisNet() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_EVER_DIS_NET, false);
    }

    public boolean getEverEnteredDeepsleep() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_ENTERED_DEEPSLEEP, false);
    }

    public boolean getEverEnteredSensing() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_ENTERED_SENSING, false);
    }

    public boolean getExtremeSleepStatus() {
        return Settings.System.getInt(this.mContext.getContentResolver(), STR_SUPER_SLEEP_STATUS, 0) == 1 && b.D();
    }

    public boolean getImprovedDeepSleepNetWorkSwitch() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_IMPROVED_NETWORK_SWITCH_STATE, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [java.util.List] */
    public List<String> getInUsePackagesList() {
        ArrayList arrayList = new ArrayList();
        try {
            try {
                try {
                    Class<?> cls = Class.forName("android.location.OplusLocationManager");
                    Object newInstance = cls.newInstance();
                    Method method = cls.getMethod("getInUsePackagesList", new Class[0]);
                    if (method != null && newInstance != null) {
                        arrayList = (List) method.invoke(newInstance, new Object[0]);
                    }
                } catch (IllegalAccessException unused) {
                    LocalLog.a(TAG, "getInUsePackagesList IllegalAccessException");
                } catch (InvocationTargetException unused2) {
                    LocalLog.a(TAG, "getInUsePackagesList InvocationTargetException");
                }
            } catch (NoSuchMethodError unused3) {
                LocalLog.a(TAG, "getInUsePackagesList NoSuchMethodError");
            } catch (NoSuchMethodException unused4) {
                LocalLog.a(TAG, "getInUsePackagesList NoSuchMethodException");
            }
        } catch (ClassNotFoundException unused5) {
            LocalLog.a(TAG, "getInUsePackagesList ClassNotFoundException");
        } catch (InstantiationException unused6) {
            LocalLog.a(TAG, "getInUsePackagesList InstantiationException");
        }
        return arrayList;
    }

    public boolean getInternetPhoneDetected() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_INTERNET_PHONE_DETECTED, false);
    }

    public boolean getIsAiPredict() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_IS_AI_PREDIC, false);
    }

    public boolean getIsTestMode() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_IS_TEST_MODE, false);
    }

    public long getLightClearTime() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_LIGHT_CLEAR_TIME, 0L);
    }

    public int getLocalNetWorkSwitchState() {
        return DeepSleepSharepref.getIntValue(this.mContext, STR_NETWORK_SWITCH_STATE, 0);
    }

    public int getLocalNetWorkType() {
        return DeepSleepSharepref.getIntValue(this.mContext, STR_NETWORK_TYPE, 0);
    }

    public int getLocalNetWorkTypeBak() {
        return DeepSleepSharepref.getIntValue(this.mContext, STR_NETWORK_TYPE_BAK, 0);
    }

    public int getLocalStatus() {
        return DeepSleepSharepref.getIntValue(this.mContext, STR_STATUS, 0);
    }

    public long getMorningClearTime() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_MORNING_CLEAR_TIME, 0L);
    }

    public boolean getMotionDetected() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_MOTION_DETECTED, false);
    }

    public boolean getNavigationDetected() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_NAVIGATION_DETECTED, false);
    }

    public long getNextAlarmTime() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_NEXT_ALARM_TIME, 0L);
    }

    public boolean getPlaybackDetected() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_PLAYBACK_DETECTED, false);
    }

    public int getPlaybackState(List<String> list) {
        File dumpService = dumpService(this.mContext, "power", "possibleMusicPlayer");
        int i10 = -1;
        if (dumpService == null || !dumpService.exists()) {
            return -1;
        }
        try {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(dumpService), "UTF-8"));
                int i11 = -1;
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine != null) {
                            if (readLine.contains(PREFIX_POSSIBLE_MUSIC_PLAYER)) {
                                String substring = readLine.substring(readLine.indexOf(PREFIX_POSSIBLE_MUSIC_PLAYER) + 20);
                                if (substring != null && !"".equals(substring)) {
                                    list.add(substring);
                                    LocalLog.l(TAG, " PossibleMusicPlayer pkg: " + substring);
                                }
                            } else if (readLine.contains(PREFIX_AUDIO_STATE)) {
                                String substring2 = readLine.substring(readLine.indexOf(PREFIX_AUDIO_STATE) + 11);
                                if (substring2 != null) {
                                    if ("Playback".equals(substring2)) {
                                        i11 = 2;
                                    } else if ("Silence".equals(substring2)) {
                                        i11 = 1;
                                    } else {
                                        i11 = "Unknown".equals(substring2) ? 0 : -1;
                                    }
                                }
                                LocalLog.l(TAG, " playbackState=" + i11);
                            }
                        } else {
                            try {
                                break;
                            } catch (IOException e10) {
                                e = e10;
                                i10 = i11;
                                LocalLog.d(TAG, "getPlaybackState: failed parsing dumpsys power possibleMusicPlayer.  " + e);
                                return i10;
                            }
                        }
                    } catch (Throwable th) {
                        i10 = i11;
                        try {
                            bufferedReader.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                bufferedReader.close();
                if (!dumpService.exists()) {
                    return i11;
                }
                dumpService.delete();
                return i11;
            } finally {
                if (dumpService.exists()) {
                    dumpService.delete();
                }
            }
        } catch (IOException e11) {
            e = e11;
        }
    }

    public long getPredictEndTime() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_PREDICT_END_TIME, 0L);
    }

    public long getPredictStartTime() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_PREDICT_START_TIME, 0L);
    }

    public long getScreenoffTimeAiDcs() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_SCREENOFF_TIME_AI_DCS, 0L);
    }

    public long getTotalTraffic() {
        long totalRxBytes = TrafficStats.getTotalRxBytes();
        long totalTxBytes = TrafficStats.getTotalTxBytes();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getTotalTrafficKB: rx=");
        sb2.append(Formatter.formatFileSize(this.mContext, totalRxBytes));
        sb2.append(", tx=");
        sb2.append(Formatter.formatFileSize(this.mContext, totalTxBytes));
        sb2.append(", total=");
        long j10 = totalRxBytes + totalTxBytes;
        sb2.append(Formatter.formatFileSize(this.mContext, j10));
        LocalLog.a(TAG, sb2.toString());
        return j10;
    }

    public boolean getTrafficLowDetected() {
        return DeepSleepSharepref.getBooleanValue(this.mContext, STR_TRAFFIC_LOW_DETECTED, false);
    }

    public long getTrafficMonitorTime() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_TRAFFIC_MONITOR_ALARM, 0L);
    }

    public long getWaitTrafficStopDuration() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_WAIT_TRAFFIC_STOP_DURATION, 0L);
    }

    public long getWaitTrafficStopEnterTime() {
        return DeepSleepSharepref.getLongValue(this.mContext, STR_WAIT_TRAFFIC_STOP_ENTER_TIME, 0L);
    }

    public boolean isBluetoothPanConnected() {
        boolean isBluetoothPanConnectedInternal = isBluetoothPanConnectedInternal();
        saveDeepSleepIsBtTheringOn(isBluetoothPanConnectedInternal);
        LocalLog.l(TAG, "isBluetoothPanConnected: " + isBluetoothPanConnectedInternal);
        return isBluetoothPanConnectedInternal;
    }

    public boolean isInternetPhone(Context context, DeepSleepRecord deepSleepRecord) {
        OplusWhiteListManager oplusWhiteListManager = new OplusWhiteListManager(context);
        ArrayList globalWhiteList = oplusWhiteListManager.getGlobalWhiteList(8);
        ArrayList<String> globalWhiteList2 = oplusWhiteListManager.getGlobalWhiteList(9);
        if (globalWhiteList2 != null && !globalWhiteList2.isEmpty() && globalWhiteList != null && !globalWhiteList.isEmpty()) {
            LocalLog.l(TAG, "isInternetPhone: listAudioIn=" + globalWhiteList2 + ",listPossibleAudioOut=" + globalWhiteList);
            for (String str : globalWhiteList2) {
                if (globalWhiteList.contains(str)) {
                    deepSleepRecord.recoardEvent("InternetPhone: pkg " + str, System.currentTimeMillis(), true);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMirageDisplay() {
        boolean z10 = !getMigrateDisplayList(this.mContext).isEmpty();
        saveDeepSleepIsMigrageDisplayOn(z10);
        LocalLog.l(TAG, "isMirageDisplay: " + z10);
        return z10;
    }

    public boolean isNavigationMode() {
        try {
            return new OplusNotificationManager().isNavigationMode(-1);
        } catch (Exception unused) {
            LocalLog.a(TAG, "isNavigation Exception");
            return false;
        }
    }

    public boolean isSystemAudioOut(Context context) {
        ArrayList globalWhiteList = new OplusWhiteListManager(context).getGlobalWhiteList(7);
        if (globalWhiteList == null || globalWhiteList.isEmpty()) {
            return false;
        }
        LocalLog.a(TAG, "isSystemAudioOut: list=" + globalWhiteList);
        return true;
    }

    public boolean isUsbTethering() {
        boolean contains = SystemProperties.get("sys.usb.config").contains("rndis");
        saveDeepSleepIsUsbTheringOn(contains);
        LocalLog.l(TAG, "isUsbTethering: " + contains);
        return contains;
    }

    public boolean isVoWifiRegistered() {
        boolean vowifiRegistered = this.mVowifiRegisterListenerOne.vowifiRegistered();
        boolean vowifiRegistered2 = this.mVowifiRegisterListenerTwo.vowifiRegistered();
        LocalLog.l(TAG, "isSpecialScenesNotDisableNetWork : isVoWifiRegisteredOne = " + vowifiRegistered + ", isVoWifiRegisteredTwo = " + vowifiRegistered2);
        boolean z10 = vowifiRegistered || vowifiRegistered2;
        saveDeepSleepIsVoWifiRegistered(z10);
        return z10;
    }

    public boolean isWLANSharingStateOn() {
        boolean z10 = ((WifiManager) this.mContext.getSystemService(ThermalPolicy.KEY_WIFI)).getWifiApState() == 113;
        saveDeepSleepIsWlanSharingOn(z10);
        LocalLog.l(TAG, "isWLANSharingStateOn: " + z10);
        return z10;
    }

    public boolean isWifiApStateOn() {
        boolean z10 = ((WifiManager) this.mContext.getSystemService(ThermalPolicy.KEY_WIFI)).getWifiApState() == 13;
        saveDeepSleepIsWifiApOn(z10);
        LocalLog.l(TAG, "isWifiApStateOn: " + z10);
        return z10;
    }

    public void removeAllListeners() {
        this.mVowifiRegisterListenerOne.unRegisterListener();
        this.mVowifiRegisterListenerTwo.unRegisterListener();
    }

    public void saveAiSleepEndTimeDcs(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_AI_SLEEP_END_TIME_DCS, j10);
    }

    public void saveAiSleepStartTimeDcs(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_AI_SLEEP_START_TIME_DCS, j10);
    }

    public void saveAppTrafficDuration(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_APP_TRAFFIC_DURATION, j10);
    }

    public void saveAppTrafficEnterElapsedTime(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_APP_TRAFFIC_ENTER_TIME, j10);
    }

    public void saveCountRestoreNet(int i10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_COUNT_RESTORE_NET, i10);
    }

    public void saveDeepSleepAlreadyCheckUpload(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_CHECK_ALREADY_UPLOAD, z10);
    }

    public void saveDeepSleepCheckDate(int i10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_CHECK_DATE, i10);
    }

    public void saveDeepSleepCheckStatus(boolean z10) {
        switch (getDeepSleepCheckDate()) {
            case 1:
                DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYONE, z10);
                return;
            case 2:
                DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYTWO, z10);
                return;
            case 3:
                DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYTHREE, z10);
                return;
            case 4:
                DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYFOUR, z10);
                return;
            case 5:
                DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYFIVE, z10);
                return;
            case 6:
                DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYSIX, z10);
                return;
            case 7:
                DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_STATUS_DAYSEVEN, z10);
                return;
            default:
                return;
        }
    }

    public void saveDeepSleepEnterCount(int i10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_ENTER_COUNT, i10);
    }

    public void saveDeepSleepEntertTime(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_ENTER_TIME, j10);
    }

    public void saveDeepSleepFirstFailTime(Long l10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_FIRST_FAIL_TIME, l10.longValue());
    }

    public void saveDeepSleepIsBtTheringOn(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_IS_BT_THERNERING_ON, z10);
    }

    public void saveDeepSleepIsDisableNetAllowed(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_IS_DISABLE_NET_ALLOWED, z10);
    }

    public void saveDeepSleepIsMaintenceReStored(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_IDLE_MAINTENCE_RESTORED, z10);
    }

    public void saveDeepSleepIsMigrageDisplayOn(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_IS_MIGRAGE_DISPLAY_ON, z10);
    }

    public void saveDeepSleepIsSpecialAppInstalled(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_SPECIAL_APP_INSTALLED, z10);
    }

    public void saveDeepSleepIsUsbTheringOn(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_IS_USB_THERNERING_ON, z10);
    }

    public void saveDeepSleepIsVoWifiRegistered(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_IS_VOWIFI_REGISTERED, z10);
    }

    public void saveDeepSleepIsWifiApOn(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_IS_WIFI_AP_ON, z10);
    }

    public void saveDeepSleepIsWlanSharingOn(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_IS_WLAN_SHARING_ON, z10);
    }

    public void saveDeepSleepLastFailTime(Long l10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_LAST_FAIL_TIME, l10.longValue());
    }

    public void saveDeepSleepNetTypeError(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_NET_TYPE_ERROR, z10);
    }

    public void saveDeepSleepPredictException(String str) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_EXCEPTION, str);
    }

    public void saveDeepSleepPredictFailed(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_FAIL, z10);
    }

    public void saveDeepSleepPredictOriginal(String str) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_ORIGINAL, str);
    }

    public void saveDeepSleepPredictSelectError(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_SELECT_ERROR, z10);
    }

    public void saveDeepSleepPredictShortDuration(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_SHORT_DURATION, z10);
    }

    public void saveDeepSleepPredictWrongTime(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DEEP_SLEEP_PREDICT_WRONG_TIME, z10);
    }

    public void saveDeepSleepStatus(int i10) {
        Settings.System.putInt(this.mContext.getContentResolver(), STR_DEEP_SLEEP_STATUS, i10);
    }

    public void saveDisNetDuration(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DIS_NET_DURATION, j10);
    }

    public void saveDisNetElapsedTime(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_DIS_NET_ELAPSED_TIME, j10);
    }

    public void saveEverDisNet(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_EVER_DIS_NET, z10);
    }

    public void saveEverEnteredDeepsleep(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_ENTERED_DEEPSLEEP, z10);
    }

    public void saveEverEnteredSensing(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_ENTERED_SENSING, z10);
    }

    public void saveImprovedDeepSleepNetWorkSwitch(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_IMPROVED_NETWORK_SWITCH_STATE, z10);
    }

    public void saveInternetPhoneDetected(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_INTERNET_PHONE_DETECTED, z10);
    }

    public void saveIsAiPredict(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_IS_AI_PREDIC, z10);
    }

    public void saveIsTestMode(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_IS_TEST_MODE, z10);
    }

    public void saveLightClearTime(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_LIGHT_CLEAR_TIME, j10);
    }

    public void saveLocalNetWorkSwitchState(int i10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_NETWORK_SWITCH_STATE, i10);
    }

    public void saveLocalNetWorkType(int i10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_NETWORK_TYPE, i10);
    }

    public void saveLocalNetWorkTypeBak(int i10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_NETWORK_TYPE_BAK, i10);
    }

    public void saveLocalStatus(int i10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_STATUS, i10);
    }

    public void saveMorningClearTime(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_MORNING_CLEAR_TIME, j10);
    }

    public void saveMotionDetected(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_MOTION_DETECTED, z10);
    }

    public void saveNavigationDetected(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_NAVIGATION_DETECTED, z10);
    }

    public void saveNetWorkDisableStateSettingProvider(int i10) {
        Settings.System.putInt(this.mContext.getContentResolver(), STR_NETWORK_DISABLE_STATE, i10);
    }

    public void saveNetWorkTypeSettingProvider(int i10) {
        Settings.System.putInt(this.mContext.getContentResolver(), STR_NETWORK_TYPE, i10);
    }

    public void saveNextAlarmTime(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_NEXT_ALARM_TIME, j10);
    }

    public void savePlaybackDetected(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_PLAYBACK_DETECTED, z10);
    }

    public void savePredictEndTime(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_PREDICT_END_TIME, j10);
        Settings.System.putLong(this.mContext.getContentResolver(), STR_PREDICT_END_TIME, j10);
    }

    public void savePredictResultSettingProvider(boolean z10) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), STR_DEEP_SLEEP_SETTINGS_PREDICT_CHECK, !z10 ? 1 : 0);
    }

    public void savePredictStartTime(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_PREDICT_START_TIME, j10);
        Settings.System.putLong(this.mContext.getContentResolver(), STR_PREDICT_START_TIME, j10);
    }

    public void savePropNetWorkDisableTime(long j10) {
        SystemProperties.set(PROP_NETWORK_DISABLE_TIME, String.valueOf(j10));
    }

    public void savePropNetWorkRestoreTime(long j10) {
        SystemProperties.set(PROP_NETWORK_RESTORE_TIME, String.valueOf(j10));
    }

    public void saveRecentFailCountSettingProvider(int i10) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), STR_DEEP_SLEEP_SETTINGS_FAILED_TIMES_OF_SEVENDAYS, i10);
    }

    public void saveScreenoffTimeAiDcs(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_SCREENOFF_TIME_AI_DCS, j10);
    }

    public void saveSleepFirstFailTimeSettingProvider(long j10) {
        Settings.Secure.putLong(this.mContext.getContentResolver(), STR_DEEP_SLEEP_SETTINGS_FIRST_FAIL_TIME, j10);
    }

    public void saveSleepLastFailTimeSettingProvider(long j10) {
        Settings.Secure.putLong(this.mContext.getContentResolver(), STR_DEEP_SLEEP_SETTINGS_LAST_FAIL_TIME, j10);
    }

    public void saveTrafficLowDetected(boolean z10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_TRAFFIC_LOW_DETECTED, z10);
    }

    public void saveTrafficMonitorTime(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_TRAFFIC_MONITOR_ALARM, j10);
    }

    public void saveWaitTrafficStopDuration(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_WAIT_TRAFFIC_STOP_DURATION, j10);
    }

    public void saveWaitTrafficStopEnterTime(long j10) {
        DeepSleepSharepref.insertXML(this.mContext, STR_WAIT_TRAFFIC_STOP_ENTER_TIME, j10);
    }

    public void setAISleepPercent(int i10) {
        LocalLog.a(TAG, "set ai sleep percent " + i10);
        if (AI_SLEEP_PERCENT_LIST.contains(Integer.valueOf(i10))) {
            this.mAISleepPercent = i10;
            return;
        }
        LocalLog.l(TAG, "set invalid ai sleep percent " + i10);
    }

    public void setAIWakePercent(int i10) {
        LocalLog.a(TAG, "set ai wake percent " + i10);
        if (AI_SLEEP_PERCENT_LIST.contains(Integer.valueOf(i10))) {
            this.mAIWakePercent = i10;
            return;
        }
        LocalLog.l(TAG, "set invalid ai wake percent " + i10);
    }

    public void setAirPlaneModeStatus(boolean z10) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "airplane_mode_on", z10 ? 1 : 0);
        Intent intent = new Intent("android.intent.action.AIRPLANE_MODE");
        intent.putExtra("state", z10);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    public void setAllListeners() {
        this.mVowifiRegisterListenerOne.startListen();
        this.mVowifiRegisterListenerTwo.startListen();
    }

    public void setDeepSleepResetAirPlaneStatus(boolean z10) {
        Settings.System.putInt(this.mContext.getContentResolver(), STR_RESET_AIR_BY_DEEPSLEEP, z10 ? 1 : 0);
    }

    public void setExtremeSleepStatus(int i10) {
        Settings.System.putInt(this.mContext.getContentResolver(), STR_SUPER_SLEEP_STATUS, i10);
    }

    public void setInactiveStatus(int i10) {
        Settings.System.putInt(this.mContext.getContentResolver(), STR_INACTIVE_STATUS, i10);
    }

    public boolean specialAppHavebeenInstalled() {
        PackageManager packageManager = this.mContext.getPackageManager();
        List<String> v7 = ConfigUpdateUtil.n(this.mContext).v();
        for (int i10 = 0; i10 < v7.size(); i10++) {
            String str = v7.get(i10);
            if (packageManager.getPackageInfo(str, 4194304) != null) {
                saveDeepSleepIsSpecialAppInstalled(true);
                LocalLog.l(TAG, "specialAppHavebeenInstalled: pkg=" + str);
                return true;
            }
            continue;
        }
        saveDeepSleepIsSpecialAppInstalled(false);
        return false;
    }
}
