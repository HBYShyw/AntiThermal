package com.oplus.multiuser;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Slog;
import com.oplus.multiapp.OplusMultiAppManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oplus.util.OplusStatistics;

/* loaded from: classes.dex */
public class OplusMultiUserDcsUtil {
    private static final int ALARM_HOUR = 23;
    private static final int ALARM_MIN = 10;
    private static final int ALARM_SEC = 10;
    private static final String DCS_APP_ID = "20225";
    private static final String DCS_LOG_TAG = "20225003";
    private static final String EVENT_ID_CREATE_USER = "create_user_flow_statistics";
    private static final String EVENT_ID_DEFAULT_ACCESS_MODE = "default_access_mode_statistics";
    private static final String EVENT_ID_SWITCH_USER = "switch_user_flow_statistics";
    private static final String FLOW_1_1_1_CREATE_USERKEY_EX = "flow_1.1.1_createUserKeyEx";
    private static final String FLOW_1_1_CREATE_USERKEY_COST = "flow_1.1_createUserKey_cost";
    private static final String FLOW_1_2_1_PREPARE_USERDATA_Ex = "flow_1.2.1_prepareUserDataEx";
    private static final String FLOW_1_2_PREPARE_USERDATA_COST = "flow_1.2_prepareUserData_cost";
    private static final String FLOW_1_3_1_CREATE_NEW_USER_Ex = "flow_1.3.1_createNewUserEx";
    private static final String FLOW_1_3_CREATE_NEW_USER_COST = "flow_1.3_createNewUser_cost";
    private static final String FLOW_1_4_1_ON_USER_CREATED_Ex = "flow_1.4.1_onNewUserCreatedEx";
    private static final String FLOW_1_4_ON_USER_CREATED_COST = "flow_1.4_onNewUserCreated_cost";
    private static final String FLOW_1_CREATE_USER_TOTAL_COST = "flow_1_createUser_total_cost";
    private static final String FLOW_2_1_STARTUSERINTERNAL_FREEZING_COST = "flow_2.1_startUserInternal_freezingCost";
    private static final String FLOW_2_2_STARTUSERINTERNAL_UPDATECONFIGANDPROFILEIDS_COST = "flow_2.2_startUserInternal_updateConfigurationAndProfileIdsCost";
    private static final String FLOW_2_3_1_ONBEFORESTARTUSER_PREPARE_EX = "flow_2.3.1_onBeforeStartUser_prepareUserDataEx";
    private static final String FLOW_2_3_ONBEFORESTARTUSER_PREPARE_USERDATA_COST = "flow_2.3_onBeforeStartUser_prepareUserDataCost";
    private static final String FLOW_2_4_1_ONBEFORESTARTUSER_RECONCILEAPPDATA_EX = "flow_2.4.1_onBeforeStartUser_reconcileAppsDataEx";
    private static final String FLOW_2_4_ONBEFORESTARTUSER_RECONCILEAPPDATA_COST = "flow_2.4_onBeforeStartUser_reconcileAppsDataCost";
    private static final String FLOW_2_5_STARTUSERINTERNAL_MOVETOFOREGROUND_COST = "flow_2.5_startUserInternal_moveToForegroundCost";
    private static final String FLOW_2_STARTUSERINTERNAL_TOTAL_COST = "flow_2_startUserInternal_ToalCost";
    private static final String FLOW_3_1_ONSTARTUSER_MAX_SERVICE_NAME_AND_COST = "flow_3.1_onStartUser_max_service_name_and_cost";
    private static final String FLOW_3_2_1_ONSTARTUSER_ROLEMANAGER_EX = "flow_3.2.1_onStartUser_RoleManagerServiceEx";
    private static final String FLOW_3_2_ONSTARTUSER_ROLEMANAGER_COST = "flow_3.2_onStartUser_RoleManagerServiceCost";
    private static final String FLOW_3_3_1_ONSTARTUSER_PERMISSIONPOLICY_EX = "flow_3.3.1_onStartUser_PermissionPolicyServiceEx";
    private static final String FLOW_3_3_ONSTARTUSER_PERMISSIONPOLICY_COST = "flow_3.3_onStartUser_PermissionPolicyServiceCost";
    private static final String FLOW_3_4_1_ONSTARTUSER_STORAGEMANAGER_EX = "flow_3.4.1_onStartUser_StorageManagerServiceEx";
    private static final String FLOW_3_4_ONSTARTUSER_STORAGEMANAGER_COST = "flow_3.4_onStartUser_StorageManagerServiceCost";
    private static final String FLOW_3_ONSTARTUSER_TOAL_COST = "flow_3_onStartUser_ToalCost";
    private static final String FLOW_4_1_ONSWITCHUSER_MAX_SERVICE_NAME_AND_COST = "flow_4.1_onSwitchUser_max_service_name_and_cost";
    private static final String FLOW_4_ONSWITCHUSER_TOAL_COST = "flow_4_onSwitchUser_ToalCost";
    private static final String FLOW_5_1_1_DISPATCHUSERSWITCH_KEYGUARD_EX = "flow_5.1.1_dispatchUserSwitch_KeyguardEx";
    private static final String FLOW_5_1_DISPATCHUSERSWITCH_KEYGUARD_COST = "flow_5.1_dispatchUserSwitch_KeyguardUpdateMonitorCost";
    private static final String FLOW_5_2_1_DISPATCHUSERSWITCH_WALLPAPER_EX = "flow_5.2.1_dispatchUserSwitch_WallpaperEx";
    private static final String FLOW_5_2_DISPATCHUSERSWITCH_WALLPAPER_COST = "flow_5.2_dispatchUserSwitch_WallpaperManagerServiceCost";
    private static final String FLOW_5_DISPATCHUSERSWITCH_LAST_OBSERVER_NAME_AND_COST = "flow_5_dispatchUserSwitch_Last_Observer_name_and_cost";
    private static final String FLOW_6_DISPATCHUSERSWITCH_OBSERVER_NO_RESPOND = "flow_6_dispatchUserSwitch_Observers_no_respond";
    private static final String FLOW_7_SWITCHUSER_TO_CONTINUESWITCH_COST = "flow_7_switchUser_to_continueUserSwitch_Cost";
    private static final String FLOW_8_1_CONTINUESWITCH_TO_UNFROZEN_COST = "flow_8.1_continueUserSwitch_to_unFrozen_Cost";
    private static final String FLOW_8_2_UNFROZEN_REASON = "flow_8.2_unFrozen_Reason";
    private static final String FLOW_8_SWITCHUSER_TO_UNFROZEN_COST = "flow_8_switchUser_to_unFrozen_Cost";
    private static final String KEY_IS_SYSTEMCLONE = "isSystemClone";
    private static final String KEY_NEW_USER_TYPE = "newUserType";
    private static final String KEY_OLD_USER_TYPE = "oldUserType";
    private static final int MSG_REPORT_CREATE_USER = 1;
    private static final int MSG_REPORT_DEFAULT_ACCESS_MODE = 3;
    private static final int MSG_REPORT_SWITCH_USER = 2;
    private static final String MULTIAPP_ACCESS_MODE = "access_mode";
    private static final String MULTIAPP_DCS_LOG_TAG = "20225007";
    private static final String MULTIAPP_PKG_NAME = "pkg_name";
    private static final int PERIOD_DAY_HOUR = 24;
    private static final int PERIOD_DAY_MILLIS = 86400000;
    private static final int PERIOD_HOUR_MIN = 60;
    private static final int PERIOD_MIN_SEC = 60;
    private static final int PERIOD_SEC_MILLIS = 1000;
    private Context mContext;
    private DcsEventHandle mDcsEventHandle;
    private HandlerThread mHandlerThread;
    private final String TAG = OplusMultiUserDcsUtil.class.getName();
    private AlarmManager mAlarmManager = null;
    private long mNextAlarmTime = 0;
    private final AlarmManager.OnAlarmListener mUploadAccessModeAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.oplus.multiuser.OplusMultiUserDcsUtil$$ExternalSyntheticLambda0
        @Override // android.app.AlarmManager.OnAlarmListener
        public final void onAlarm() {
            OplusMultiUserDcsUtil.this.lambda$new$0();
        }
    };
    private Map<String, String> mCurrentDcsMap = new LinkedHashMap();

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mNextAlarmTime = getNextTime();
        scheduleAccessModeAlarmForDcs();
        Map<String, Integer> modeMap = OplusMultiAppManager.getInstance().getMultiAppAllAccessMode();
        this.mDcsEventHandle.obtainMessage(3, modeMap).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class DcsEventHandle extends Handler {
        public DcsEventHandle(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 1:
                        OplusMultiUserDcsUtil.this.handleCreateUserMessage(msg);
                        break;
                    case 2:
                        OplusMultiUserDcsUtil.this.handleSwitchUserMessage(msg);
                        break;
                    case 3:
                        OplusMultiUserDcsUtil.this.handleAccessModeMessage(msg);
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public OplusMultiUserDcsUtil(Context context) {
        this.mContext = null;
        this.mHandlerThread = null;
        this.mDcsEventHandle = null;
        this.mContext = context;
        HandlerThread handlerThread = new HandlerThread("OplusMultiUserDcsUtil_thread");
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mDcsEventHandle = new DcsEventHandle(this.mHandlerThread.getLooper());
        initAlarmForDcs();
    }

    public void postCreateUserMessage(OplusMultiUserStatisticsData args) {
        this.mDcsEventHandle.obtainMessage(1, args).sendToTarget();
    }

    public void postSwitchUserMessage(OplusMultiUserStatisticsData args) {
        this.mDcsEventHandle.obtainMessage(2, args).sendToTarget();
    }

    public void handleCreateUserMessage(Message msg) {
        this.mCurrentDcsMap.clear();
        if (msg != null && msg.obj != null) {
            OplusMultiUserStatisticsData args = (OplusMultiUserStatisticsData) msg.obj;
            fillMapAndCheckNull(FLOW_1_CREATE_USER_TOTAL_COST, args.flow_1_createUser_total_cost);
            fillMapAndCheckNull(FLOW_1_1_CREATE_USERKEY_COST, args.flow_1_1_createUserKey_cost);
            fillMapAndCheckNull("flow_1.1.1_createUserKeyEx", args.flow_1_1_1);
            fillMapAndCheckNull(FLOW_1_2_PREPARE_USERDATA_COST, args.flow_1_2_prepareUserData_cost);
            fillMapAndCheckNull("flow_1.2.1_prepareUserDataEx", args.flow_1_2_1);
            fillMapAndCheckNull(FLOW_1_3_CREATE_NEW_USER_COST, args.flow_1_3_createNewUser_cost);
            fillMapAndCheckNull("flow_1.3.1_createNewUserEx", args.flow_1_3_1);
            fillMapAndCheckNull(FLOW_1_4_ON_USER_CREATED_COST, args.flow_1_4_onNewUserCreated_cost);
            fillMapAndCheckNull("flow_1.4.1_onNewUserCreatedEx", args.flow_1_4_1);
            fillMapAndCheckNull(KEY_NEW_USER_TYPE, args.newUserType);
            fillMapAndCheckNull(KEY_IS_SYSTEMCLONE, args.isSystemclone);
            OplusStatistics.onCommon(this.mContext, DCS_APP_ID, DCS_LOG_TAG, EVENT_ID_CREATE_USER, this.mCurrentDcsMap, false);
            printMap("handleCreateUserMessage", this.mCurrentDcsMap);
        }
    }

    public void handleSwitchUserMessage(Message msg) {
        this.mCurrentDcsMap.clear();
        if (msg != null && msg.obj != null) {
            OplusMultiUserStatisticsData args = (OplusMultiUserStatisticsData) msg.obj;
            fillMapAndCheckNull(FLOW_8_SWITCHUSER_TO_UNFROZEN_COST, args.flow_8_switchUser_to_unFrozen_Cost);
            fillMapAndCheckNull(FLOW_8_1_CONTINUESWITCH_TO_UNFROZEN_COST, args.flow_8_1_continueUserSwitch_to_unFrozen_Cost);
            fillMapAndCheckNull(FLOW_8_2_UNFROZEN_REASON, args.flow_8_2_unFrozen_Reason);
            fillMapAndCheckNull(FLOW_7_SWITCHUSER_TO_CONTINUESWITCH_COST, args.flow_7_switchUser_to_continueUserSwitch_Cost);
            fillMapAndCheckNull(FLOW_2_STARTUSERINTERNAL_TOTAL_COST, args.flow_2_startUserInternal_ToalCost);
            fillMapAndCheckNull(FLOW_2_1_STARTUSERINTERNAL_FREEZING_COST, args.flow_2_1_startUserInternal_freezingCost);
            fillMapAndCheckNull(FLOW_2_2_STARTUSERINTERNAL_UPDATECONFIGANDPROFILEIDS_COST, args.flow_2_2_startUserInternal_updateConfigurationAndProfileIdsCost);
            fillMapAndCheckNull(FLOW_2_3_ONBEFORESTARTUSER_PREPARE_USERDATA_COST, args.flow_2_3_onBeforeStartUser_prepareUserDataCost);
            fillMapAndCheckNull("flow_2.3.1_onBeforeStartUser_prepareUserDataEx", args.flow_2_3_1);
            fillMapAndCheckNull(FLOW_2_4_ONBEFORESTARTUSER_RECONCILEAPPDATA_COST, args.flow_2_4_onBeforeStartUser_reconcileAppsDataCost);
            fillMapAndCheckNull("flow_2.4.1_onBeforeStartUser_reconcileAppsDataEx", args.flow_2_4_1);
            fillMapAndCheckNull(FLOW_2_5_STARTUSERINTERNAL_MOVETOFOREGROUND_COST, args.flow_2_5_startUserInternal_moveToForegroundCost);
            fillMapAndCheckNull(FLOW_3_ONSTARTUSER_TOAL_COST, args.flow_3_onStartUser_ToalCost);
            fillMapAndCheckNull(FLOW_3_1_ONSTARTUSER_MAX_SERVICE_NAME_AND_COST, args.flow_3_1_onStartUser_max_service_name_and_cost);
            fillMapAndCheckNull(FLOW_3_2_ONSTARTUSER_ROLEMANAGER_COST, args.flow_3_2_onStartUser_RoleManagerServiceCost);
            fillMapAndCheckNull("flow_3.2.1_onStartUser_RoleManagerServiceEx", args.flow_3_2_1);
            fillMapAndCheckNull(FLOW_3_3_ONSTARTUSER_PERMISSIONPOLICY_COST, args.flow_3_3_onStartUser_PermissionPolicyServiceCost);
            fillMapAndCheckNull("flow_3.3.1_onStartUser_PermissionPolicyServiceEx", args.flow_3_3_1);
            fillMapAndCheckNull(FLOW_3_4_ONSTARTUSER_STORAGEMANAGER_COST, args.flow_3_4_onStartUser_StorageManagerServiceCost);
            fillMapAndCheckNull("flow_3.4.1_onStartUser_StorageManagerServiceEx", args.flow_3_4_1);
            fillMapAndCheckNull(FLOW_4_ONSWITCHUSER_TOAL_COST, args.flow_4_onSwitchUser_ToalCost);
            fillMapAndCheckNull(FLOW_4_1_ONSWITCHUSER_MAX_SERVICE_NAME_AND_COST, args.flow_4_1_onSwitchUser_max_service_name_and_cost);
            fillMapAndCheckNull(FLOW_5_DISPATCHUSERSWITCH_LAST_OBSERVER_NAME_AND_COST, args.flow_5_dispatchUserSwitch_Last_Observer_Name_Cost);
            fillMapAndCheckNull(FLOW_5_1_DISPATCHUSERSWITCH_KEYGUARD_COST, args.flow_5_1_dispatchUserSwitch_KeyguardUpdateMonitorCost);
            fillMapAndCheckNull("flow_5.1.1_dispatchUserSwitch_KeyguardEx", args.flow_5_1_1);
            fillMapAndCheckNull(FLOW_5_2_DISPATCHUSERSWITCH_WALLPAPER_COST, args.flow_5_2_dispatchUserSwitch_WallpaperManagerServiceCost);
            fillMapAndCheckNull("flow_5.2.1_dispatchUserSwitch_WallpaperEx", args.flow_5_2_1);
            fillMapAndCheckNull(FLOW_6_DISPATCHUSERSWITCH_OBSERVER_NO_RESPOND, args.flow_6_dispatchUserSwitch_Observers_no_respond);
            fillMapAndCheckNull(KEY_OLD_USER_TYPE, args.oldUserType);
            fillMapAndCheckNull(KEY_NEW_USER_TYPE, args.newUserType);
            fillMapAndCheckNull(KEY_IS_SYSTEMCLONE, args.isSystemclone);
            OplusStatistics.onCommon(this.mContext, DCS_APP_ID, DCS_LOG_TAG, EVENT_ID_SWITCH_USER, this.mCurrentDcsMap, false);
            printMap("handleSwitchUserMessage", this.mCurrentDcsMap);
        }
    }

    private void fillMapAndCheckNull(String key, String value) {
        if (value == null) {
            value = "blank";
        }
        if (key != null) {
            this.mCurrentDcsMap.put(key, value);
        }
    }

    private void printMap(String functionName, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Slog.d(this.TAG, functionName + "()   key=[" + key + "]  value=[" + value + "]");
        }
    }

    private void initAlarmForDcs() {
        AlarmManager alarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        this.mAlarmManager = alarmManager;
        if (alarmManager == null) {
            Slog.d(this.TAG, " can not get AlarmManager");
        } else {
            this.mNextAlarmTime = getNextTime();
            scheduleAccessModeAlarmForDcs();
        }
    }

    private long getNextTime() {
        Calendar currentDate = Calendar.getInstance();
        long currentMillis = currentDate.getTimeInMillis();
        Calendar targetDate = Calendar.getInstance();
        targetDate.set(currentDate.get(1), currentDate.get(2), currentDate.get(5), 23, 10, 10);
        long expectedMillis = targetDate.getTimeInMillis();
        if (expectedMillis < 1000 + currentMillis) {
            return expectedMillis + 86400000;
        }
        return expectedMillis;
    }

    private void scheduleAccessModeAlarmForDcs() {
        this.mAlarmManager.setExact(0, this.mNextAlarmTime, "multiUserDcsUploader.accessMode", this.mUploadAccessModeAlarmListener, this.mDcsEventHandle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAccessModeMessage(Message msg) {
        Map<String, Integer> modeMap = (Map) msg.obj;
        List<Map<String, String>> uploadList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : modeMap.entrySet()) {
            String pkgName = entry.getKey();
            Integer accessMode = entry.getValue();
            if (pkgName != null && accessMode != null) {
                Map<String, String> map = new HashMap<>();
                map.put("pkg_name", pkgName);
                map.put(MULTIAPP_ACCESS_MODE, accessMode.toString());
                uploadList.add(map);
            }
        }
        if (uploadList.isEmpty()) {
            Slog.d(this.TAG, "uploadList is empty!");
        } else {
            OplusStatistics.onCommon(this.mContext, DCS_APP_ID, MULTIAPP_DCS_LOG_TAG, EVENT_ID_DEFAULT_ACCESS_MODE, uploadList, false);
            printMapList("handleAccessModeMessage", uploadList);
        }
    }

    private void printMapList(String functionName, List<Map<String, String>> list) {
        for (Map<String, String> map : list) {
            printMap(functionName, map);
        }
    }
}
