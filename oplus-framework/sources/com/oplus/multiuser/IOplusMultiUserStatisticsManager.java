package com.oplus.multiuser;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.util.ArraySet;
import java.util.Map;

/* loaded from: classes.dex */
public interface IOplusMultiUserStatisticsManager extends IOplusCommonFeature {
    public static final String CLEANUP = "Cleanup";
    public static final IOplusMultiUserStatisticsManager DEFAULT = new IOplusMultiUserStatisticsManager() { // from class: com.oplus.multiuser.IOplusMultiUserStatisticsManager.1
    };
    public static final String FLOW_1_1_1_CREATE_USERKEY_EX = "flow_1.1.1_createUserKeyEx";
    public static final String FLOW_1_2_1_PREPARE_USERDATA_Ex = "flow_1.2.1_prepareUserDataEx";
    public static final String FLOW_1_3_1_CREATE_NEW_USER_Ex = "flow_1.3.1_createNewUserEx";
    public static final String FLOW_1_4_1_ON_USER_CREATED_Ex = "flow_1.4.1_onNewUserCreatedEx";
    public static final String FLOW_2_3_1_ONBEFORESTARTUSER_PREPARE_EX = "flow_2.3.1_onBeforeStartUser_prepareUserDataEx";
    public static final String FLOW_2_4_1_ONBEFORESTARTUSER_RECONCILEAPPDATA_EX = "flow_2.4.1_onBeforeStartUser_reconcileAppsDataEx";
    public static final String FLOW_3_2_1_ONSTARTUSER_ROLEMANAGER_EX = "flow_3.2.1_onStartUser_RoleManagerServiceEx";
    public static final String FLOW_3_3_1_ONSTARTUSER_PERMISSIONPOLICY_EX = "flow_3.3.1_onStartUser_PermissionPolicyServiceEx";
    public static final String FLOW_3_4_1_ONSTARTUSER_STORAGEMANAGER_EX = "flow_3.4.1_onStartUser_StorageManagerServiceEx";
    public static final String FLOW_5_1_1_DISPATCHUSERSWITCH_KEYGUARD_EX = "flow_5.1.1_dispatchUserSwitch_KeyguardEx";
    public static final String FLOW_5_2_1_DISPATCHUSERSWITCH_WALLPAPER_EX = "flow_5.2.1_dispatchUserSwitch_WallpaperEx";
    public static final String NAME = "IOplusMultiUserStatisticsManager";
    public static final String START = "Start";
    public static final String STOP = "Stop";
    public static final String SWITCH = "Switch";
    public static final String UNLOCKED = "Unlocked";
    public static final String UNLOCKING = "Unlocking";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusMultiUserStatisticsManager;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void systemReady(Context context) {
    }

    default void addString(String key, String value) {
    }

    default void createUserEnter(long functionStartTime, String name, String userType, int flags, boolean preCreate, int userId) {
    }

    default void createUserExit(long functionStartTime, String name, String userType, int flags, boolean preCreate, int userId, long totalTime, long smCreateKeyCost, long upPrepareCost, long pmCreateUserCost, long pmOnUserCreatedCost) {
    }

    default void amsSwitchUserEnter(long functionStart, boolean userSwitchUiEnabled, int oldUserId, int oldUserFlags, String oldUserType, int newUserId, int newUserFlags, String newUserType) {
    }

    default void startUserInternalEnter(boolean foreground, int oldUserId, int newUserId, long functionStart, long freezingStart, long freezingCost, boolean screenFrozen) {
    }

    default void onBeforeStartUserExit(int userId, long totalTime, long prepareUserDataTime, long reconcileAppsDataTime) {
    }

    default void startUserInternalExit(long functionCost, long moveToForegroundCost, long updateConfigCost, int oldUserId, int newUserId, long functionStart) {
    }

    default void handleIfTooLong(String onWhat, long duration, String serviceName, int userId, Map<String, Long> serviceMap) {
    }

    default void onUserExit(String onWhat, long functionCost, int userId, Map<String, Long> serviceMap) {
    }

    default void dispatchSwitchEnter(long functionStartTime, int oldUserId, int newUserId) {
    }

    default void dispatchSwitchIfTooLong(long duration, String serviceName, int oldUserId, int newUserId) {
    }

    default void timeoutUserSwitchEnter(ArraySet<String> callbacks, int oldUserId, int newUserId) {
    }

    default void continueUserSwitchEnter(int oldUserId, int newUserId) {
    }

    default void screenUnFrozen(String reason) {
    }
}
