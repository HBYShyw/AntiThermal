package com.oplus.multiuser;

import android.content.Context;
import android.os.SystemClock;
import android.util.ArraySet;
import android.util.Slog;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusMultiUserStatisticsManager implements IOplusMultiUserStatisticsManager {
    private static final String KeyguardUpdateMonitor = "KeyguardUpdateMonitor";
    private static final String PermissionPolicyService = "com.android.server.policy.PermissionPolicyService";
    private static final String RoleManagerService = "com.android.server.role.RoleManagerService";
    private static final String StorageManagerService = "com.android.server.StorageManagerService";
    private static final String WallpaperManagerService = "WallpaperManagerService";
    private static final String TAG = OplusMultiUserStatisticsManager.class.getName();
    private static OplusMultiUserStatisticsManager sInstance = null;
    private static final Object mLock = new Object();
    private OplusMultiUserDcsUtil mOplusMultiUserDcsUtil = null;
    private long SERVICE_CALL_WARN_TIME_MS_ONSTART = 50;
    private long SERVICE_CALL_WARN_TIME_MS_ONSWITCH = 50;
    private long DISPATCH_SEND_RESULT_WARNING_TIMEOUT_MS = 300;
    private SwitchUserEvent mSwitchUserEvent = null;
    private CreateUserEvent mCreateUserEvent = null;
    private Object mLockSwitchUserEvent = new Object();
    private Object mLockCreateUserEvent = new Object();
    private long mAmsSwitchUser_EnterTime = 0;
    private boolean mUserSwitchUiEnabled = false;
    private ShortUserInfo mOldUserInfo = null;
    private ShortUserInfo mNewUserInfo = null;

    public static OplusMultiUserStatisticsManager getInstance() {
        OplusMultiUserStatisticsManager oplusMultiUserStatisticsManager;
        synchronized (mLock) {
            if (sInstance == null) {
                sInstance = new OplusMultiUserStatisticsManager();
            }
            oplusMultiUserStatisticsManager = sInstance;
        }
        return oplusMultiUserStatisticsManager;
    }

    private OplusMultiUserStatisticsManager() {
    }

    private boolean isSystemClone(int flags) {
        if ((536870912 & flags) == 0) {
            return false;
        }
        return true;
    }

    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void systemReady(Context context) {
        this.mOplusMultiUserDcsUtil = new OplusMultiUserDcsUtil(context);
    }

    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void addString(String key, String value) {
        String str = TAG;
        Slog.d(str, "addString   key=" + key + " value=" + value);
        if (this.mOplusMultiUserDcsUtil == null) {
            Slog.e(str, "addString dcsUtil was NULL");
            return;
        }
        try {
            boolean handle = handleAddStringForCreateUser(key, value);
            if (!handle) {
                handleAddStringForSwitchUser(key, value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private boolean handleAddStringForSwitchUser(String key, String value) {
        char c;
        boolean handle = false;
        synchronized (this.mLockSwitchUserEvent) {
            if (key != null) {
                if (this.mSwitchUserEvent != null) {
                    switch (key.hashCode()) {
                        case -842098511:
                            if (key.equals(IOplusMultiUserStatisticsManager.FLOW_3_4_1_ONSTARTUSER_STORAGEMANAGER_EX)) {
                                c = 4;
                                break;
                            }
                            c = 65535;
                            break;
                        case 194235352:
                            if (key.equals(IOplusMultiUserStatisticsManager.FLOW_3_2_1_ONSTARTUSER_ROLEMANAGER_EX)) {
                                c = 2;
                                break;
                            }
                            c = 65535;
                            break;
                        case 376926680:
                            if (key.equals(IOplusMultiUserStatisticsManager.FLOW_2_4_1_ONBEFORESTARTUSER_RECONCILEAPPDATA_EX)) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case 608855242:
                            if (key.equals(IOplusMultiUserStatisticsManager.FLOW_5_1_1_DISPATCHUSERSWITCH_KEYGUARD_EX)) {
                                c = 5;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1013970081:
                            if (key.equals(IOplusMultiUserStatisticsManager.FLOW_3_3_1_ONSTARTUSER_PERMISSIONPOLICY_EX)) {
                                c = 3;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1124504259:
                            if (key.equals(IOplusMultiUserStatisticsManager.FLOW_5_2_1_DISPATCHUSERSWITCH_WALLPAPER_EX)) {
                                c = 6;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1917664873:
                            if (key.equals(IOplusMultiUserStatisticsManager.FLOW_2_3_1_ONBEFORESTARTUSER_PREPARE_EX)) {
                                c = 0;
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
                            this.mSwitchUserEvent.onBeforeStartUser_prepareUserDataEx = value;
                            handle = true;
                            break;
                        case 1:
                            this.mSwitchUserEvent.onBeforeStartUser_reconcileAppsDataEx = value;
                            handle = true;
                            break;
                        case 2:
                            this.mSwitchUserEvent.onStartUser_RoleManagerServiceEx = value;
                            handle = true;
                            break;
                        case 3:
                            this.mSwitchUserEvent.onStartUser_PermissionPolicyServiceEx = value;
                            handle = true;
                            break;
                        case 4:
                            this.mSwitchUserEvent.onStartUser_StorageManagerServiceEx = value;
                            handle = true;
                            break;
                        case 5:
                            this.mSwitchUserEvent.dispatchUserSwitch_KeyguardUpdateMonitorEx = value;
                            handle = true;
                            break;
                        case 6:
                            this.mSwitchUserEvent.dispatchUserSwitch_WallpaperManagerServiceEx = value;
                            handle = true;
                            break;
                    }
                }
            }
        }
        return handle;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private boolean handleAddStringForCreateUser(String key, String value) {
        char c;
        boolean handle = false;
        synchronized (this.mLockCreateUserEvent) {
            if (key != null) {
                if (this.mCreateUserEvent != null) {
                    switch (key.hashCode()) {
                        case -940643662:
                            if (key.equals(IOplusMultiUserStatisticsManager.FLOW_1_1_1_CREATE_USERKEY_EX)) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case -236476489:
                            if (key.equals(IOplusMultiUserStatisticsManager.FLOW_1_2_1_PREPARE_USERDATA_Ex)) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case 805004139:
                            if (key.equals(IOplusMultiUserStatisticsManager.FLOW_1_3_1_CREATE_NEW_USER_Ex)) {
                                c = 2;
                                break;
                            }
                            c = 65535;
                            break;
                        case 2061528325:
                            if (key.equals(IOplusMultiUserStatisticsManager.FLOW_1_4_1_ON_USER_CREATED_Ex)) {
                                c = 3;
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
                            this.mCreateUserEvent.createUserKeyEx = value;
                            handle = true;
                            break;
                        case 1:
                            this.mCreateUserEvent.prepareUserDataEx = value;
                            handle = true;
                            break;
                        case 2:
                            this.mCreateUserEvent.createNewUserEx = value;
                            handle = true;
                            break;
                        case 3:
                            this.mCreateUserEvent.onNewUserCreatedEx = value;
                            handle = true;
                            break;
                    }
                }
            }
        }
        return handle;
    }

    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void createUserEnter(long functionStartTime, String name, String userType, int flags, boolean preCreate, int userId) {
        if (this.mOplusMultiUserDcsUtil == null) {
            Slog.e(TAG, "createUserEnter dcsUtil was NULL");
            return;
        }
        synchronized (this.mLockCreateUserEvent) {
            this.mCreateUserEvent = new CreateUserEvent(functionStartTime, userId, userType, flags);
        }
    }

    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void createUserExit(long functionStartTime, String name, String userType, int flags, boolean preCreate, int userId, long totalTime, long smCreateKeyCost, long upPrepareCost, long pmCreateUserCost, long pmOnUserCreatedCost) {
        boolean isSystemClone = isSystemClone(flags);
        synchronized (this.mLockCreateUserEvent) {
            try {
                try {
                    CreateUserEvent createUserEvent = this.mCreateUserEvent;
                    if (createUserEvent != null) {
                        createUserEvent.mCreateUser_ExitTime = SystemClock.elapsedRealtime();
                        if (this.mCreateUserEvent.isMatched(functionStartTime, userId, userType, flags)) {
                            OplusMultiUserStatisticsData args = new OplusMultiUserStatisticsData();
                            try {
                                args.newUserType = userType;
                                args.isSystemclone = "" + isSystemClone;
                                args.flow_1_createUser_total_cost = "" + totalTime;
                            } catch (Throwable th) {
                                th = th;
                                throw th;
                            }
                            try {
                                args.flow_1_1_createUserKey_cost = "" + smCreateKeyCost;
                                args.flow_1_1_1 = this.mCreateUserEvent.createUserKeyEx;
                            } catch (Throwable th2) {
                                th = th2;
                                throw th;
                            }
                            try {
                                args.flow_1_2_prepareUserData_cost = "" + upPrepareCost;
                                args.flow_1_2_1 = this.mCreateUserEvent.prepareUserDataEx;
                            } catch (Throwable th3) {
                                th = th3;
                                throw th;
                            }
                            try {
                                args.flow_1_3_createNewUser_cost = "" + pmCreateUserCost;
                                args.flow_1_3_1 = this.mCreateUserEvent.createNewUserEx;
                                args.flow_1_4_onNewUserCreated_cost = "" + pmOnUserCreatedCost;
                                args.flow_1_4_1 = this.mCreateUserEvent.onNewUserCreatedEx;
                                OplusMultiUserDcsUtil oplusMultiUserDcsUtil = this.mOplusMultiUserDcsUtil;
                                if (oplusMultiUserDcsUtil != null) {
                                    oplusMultiUserDcsUtil.postCreateUserMessage(args);
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                throw th;
                            }
                        }
                        this.mCreateUserEvent = null;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            } catch (Throwable th6) {
                th = th6;
                throw th;
            }
        }
    }

    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void amsSwitchUserEnter(long functionStartTime, boolean userSwitchUiEnabled, int oldUserId, int oldUserFlags, String oldUserType, int newUserId, int newUserFlags, String newUserType) {
        this.mAmsSwitchUser_EnterTime = functionStartTime;
        this.mUserSwitchUiEnabled = userSwitchUiEnabled;
        if (oldUserId == newUserId) {
            Slog.e(TAG, "amsSwitchUserEnter  oldUserId == newUserId  " + oldUserId);
        } else {
            this.mOldUserInfo = new ShortUserInfo(oldUserId, oldUserFlags, oldUserType);
            this.mNewUserInfo = new ShortUserInfo(newUserId, newUserFlags, newUserType);
        }
    }

    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void startUserInternalEnter(boolean foreground, int oldUserId, int newUserId, long functionStart, long freezingStart, long freezingCost, boolean screenFrozen) {
        Object obj;
        ShortUserInfo shortUserInfo = this.mOldUserInfo;
        if (shortUserInfo != null && this.mNewUserInfo != null) {
            boolean isSystemCloneSwitch = isSystemClone(shortUserInfo.mFlags) || isSystemClone(this.mNewUserInfo.mFlags);
            Object obj2 = this.mLockSwitchUserEvent;
            synchronized (obj2) {
                try {
                    if (foreground) {
                        try {
                            if (oldUserId == this.mOldUserInfo.mId && newUserId == this.mNewUserInfo.mId) {
                                if (this.mSwitchUserEvent == null) {
                                    try {
                                        obj = obj2;
                                    } catch (Throwable th) {
                                        th = th;
                                        obj = obj2;
                                        throw th;
                                    }
                                    try {
                                        this.mSwitchUserEvent = new SwitchUserEvent(this.mAmsSwitchUser_EnterTime, this.mUserSwitchUiEnabled, oldUserId, newUserId, isSystemCloneSwitch);
                                    } catch (Throwable th2) {
                                        th = th2;
                                        throw th;
                                    }
                                } else {
                                    obj = obj2;
                                }
                                this.mSwitchUserEvent.mOldUserType = this.mOldUserInfo.mUserType;
                                this.mSwitchUserEvent.mNewUserType = this.mNewUserInfo.mUserType;
                                if (functionStart >= 0) {
                                    this.mSwitchUserEvent.startUserInternal_EnterTime = functionStart;
                                }
                                this.mSwitchUserEvent.mForeground = foreground;
                                if (freezingStart >= 0) {
                                    this.mSwitchUserEvent.startUserInternal_freezingTime = freezingStart;
                                }
                                if (freezingCost >= 0) {
                                    try {
                                        this.mSwitchUserEvent.startUserInternal_freezingCost = freezingCost;
                                    } catch (Throwable th3) {
                                        th = th3;
                                        throw th;
                                    }
                                }
                                this.mSwitchUserEvent.isScreenFrozen = screenFrozen;
                                this.mOldUserInfo = null;
                                this.mNewUserInfo = null;
                                return;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            obj = obj2;
                        }
                    }
                    obj = obj2;
                    this.mOldUserInfo = null;
                    this.mNewUserInfo = null;
                    return;
                } catch (Throwable th5) {
                    th = th5;
                }
            }
        }
        Slog.e(TAG, "startUserInternalEnter   no call switchUser  ");
    }

    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void onBeforeStartUserExit(int userId, long totalCost, long prepareUserDataCost, long reconcileAppsDataCost) {
        synchronized (this.mLockSwitchUserEvent) {
            SwitchUserEvent switchUserEvent = this.mSwitchUserEvent;
            if (switchUserEvent != null && switchUserEvent.mAmsSwitchUser_TargetUserId == userId) {
                this.mSwitchUserEvent.onBeforeStartUser_TotalCost = totalCost;
                this.mSwitchUserEvent.onBeforeStartUser_prepareUserDataCost = prepareUserDataCost;
                this.mSwitchUserEvent.onBeforeStartUser_reconcileAppsDataCost = reconcileAppsDataCost;
            }
        }
    }

    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void startUserInternalExit(long functionCost, long moveToForegroundCost, long updateConfigCost, int oldUserId, int newUserId, long functionStart) {
        synchronized (this.mLockSwitchUserEvent) {
            SwitchUserEvent switchUserEvent = this.mSwitchUserEvent;
            if (switchUserEvent != null && switchUserEvent.startUserInternal_EnterTime == functionStart && this.mSwitchUserEvent.isMatched(oldUserId, newUserId)) {
                this.mSwitchUserEvent.startUserInternal_updateConfigCost = updateConfigCost;
                this.mSwitchUserEvent.startUserInternal_moveToForegroundCost = moveToForegroundCost;
                this.mSwitchUserEvent.startUserInternal_ToalCost = functionCost;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void handleIfTooLong(String onWhat, long duration, String serviceName, int userId, Map<String, Long> serviceMap) {
        char c;
        if (onWhat != null) {
            switch (onWhat.hashCode()) {
                case -1805606060:
                    if (onWhat.equals(IOplusMultiUserStatisticsManager.SWITCH)) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 80204866:
                    if (onWhat.equals(IOplusMultiUserStatisticsManager.START)) {
                        c = 1;
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
                    if (serviceName != null && duration > this.SERVICE_CALL_WARN_TIME_MS_ONSWITCH) {
                        serviceMap.put(serviceName, Long.valueOf(duration));
                        return;
                    }
                    return;
                case 1:
                    if (serviceName != null && duration > this.SERVICE_CALL_WARN_TIME_MS_ONSTART) {
                        serviceMap.put(serviceName, Long.valueOf(duration));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void onUserExit(String onWhat, long functionCost, int userId, Map<String, Long> serviceMap) {
        char c;
        synchronized (this.mLockSwitchUserEvent) {
            if (serviceMap != null && onWhat != null) {
                SwitchUserEvent switchUserEvent = this.mSwitchUserEvent;
                if (switchUserEvent != null && switchUserEvent.mAmsSwitchUser_TargetUserId == userId) {
                    switch (onWhat.hashCode()) {
                        case -1805606060:
                            if (onWhat.equals(IOplusMultiUserStatisticsManager.SWITCH)) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case 80204866:
                            if (onWhat.equals(IOplusMultiUserStatisticsManager.START)) {
                                c = 1;
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
                            handleServiceOnSwitch(onWhat, functionCost, userId, serviceMap);
                            break;
                        case 1:
                            handleServiceOnStart(onWhat, functionCost, userId, serviceMap);
                            break;
                    }
                }
            }
        }
        if (serviceMap != null) {
            serviceMap.clear();
        }
    }

    private void handleServiceOnSwitch(String onWhat, long functionCost, int userId, Map<String, Long> srcMap) {
        this.mSwitchUserEvent.onSwitchUser_TotalCost = functionCost;
        long maxCost = 0;
        for (Map.Entry<String, Long> entry : srcMap.entrySet()) {
            String serviceName = entry.getKey();
            long serviceCost = entry.getValue().longValue();
            if (serviceName != null && maxCost < serviceCost) {
                maxCost = serviceCost;
                this.mSwitchUserEvent.onSwitchUser_max_service_name_and_cost = serviceName + "_" + serviceCost;
            }
        }
    }

    private void handleServiceOnStart(String onWhat, long functionCost, int userId, Map<String, Long> srcMap) {
        this.mSwitchUserEvent.onStartUser_TotalCost = functionCost;
        long maxCost = 0;
        for (Map.Entry<String, Long> entry : srcMap.entrySet()) {
            String serviceName = entry.getKey();
            long serviceCost = entry.getValue().longValue();
            if (serviceName != null) {
                if (maxCost < serviceCost) {
                    maxCost = serviceCost;
                    this.mSwitchUserEvent.onStartUser_max_service_name_and_cost = serviceName + "_" + serviceCost;
                }
                if (serviceName.contains(RoleManagerService)) {
                    this.mSwitchUserEvent.onStartUser_RoleManagerServiceCost = serviceCost;
                } else if (serviceName.contains(PermissionPolicyService)) {
                    this.mSwitchUserEvent.onStartUser_PermissionPolicyServiceCost = serviceCost;
                } else if (serviceName.contains(StorageManagerService)) {
                    this.mSwitchUserEvent.onStartUser_StorageManagerServiceCost = serviceCost;
                }
            }
        }
    }

    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void dispatchSwitchEnter(long functionStartTime, int oldUserId, int newUserId) {
        synchronized (this.mLockSwitchUserEvent) {
            SwitchUserEvent switchUserEvent = this.mSwitchUserEvent;
            if (switchUserEvent != null && switchUserEvent.isMatched(oldUserId, newUserId)) {
                this.mSwitchUserEvent.dispatchUserSwitch_EnterTime = functionStartTime;
            }
        }
    }

    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void dispatchSwitchIfTooLong(long duration, String serviceName, int oldUserId, int newUserId) {
        synchronized (this.mLockSwitchUserEvent) {
            SwitchUserEvent switchUserEvent = this.mSwitchUserEvent;
            if (switchUserEvent != null && switchUserEvent.isMatched(oldUserId, newUserId) && serviceName != null && duration > this.DISPATCH_SEND_RESULT_WARNING_TIMEOUT_MS) {
                this.mSwitchUserEvent.dispatchUserSwitch_Last_Observer_Name_Cost = serviceName + "_" + duration;
                if (serviceName.contains(KeyguardUpdateMonitor)) {
                    this.mSwitchUserEvent.dispatchUserSwitch_KeyguardUpdateMonitorCost = duration;
                } else if (serviceName.contains(WallpaperManagerService)) {
                    this.mSwitchUserEvent.dispatchUserSwitch_WallpaperManagerServiceCost = duration;
                }
            }
        }
    }

    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void timeoutUserSwitchEnter(ArraySet<String> callbacks, int oldUserId, int newUserId) {
        if (callbacks != null) {
            synchronized (this.mLockSwitchUserEvent) {
                SwitchUserEvent switchUserEvent = this.mSwitchUserEvent;
                if (switchUserEvent != null && switchUserEvent.isMatched(oldUserId, newUserId) && this.mSwitchUserEvent.dispatchUserSwitch_EnterTime > 0) {
                    long delay = SystemClock.elapsedRealtime() - this.mSwitchUserEvent.dispatchUserSwitch_EnterTime;
                    this.mSwitchUserEvent.dispatchUserSwitch_Observers_no_respond = "" + callbacks + "_" + delay;
                }
            }
        }
    }

    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void continueUserSwitchEnter(int oldUserId, int newUserId) {
        synchronized (this.mLockSwitchUserEvent) {
            SwitchUserEvent switchUserEvent = this.mSwitchUserEvent;
            if (switchUserEvent != null && switchUserEvent.isMatched(oldUserId, newUserId)) {
                this.mSwitchUserEvent.continueSwitch_EnterTime = SystemClock.elapsedRealtime();
                SwitchUserEvent switchUserEvent2 = this.mSwitchUserEvent;
                switchUserEvent2.switchuser_to_continueCost = switchUserEvent2.continueSwitch_EnterTime - this.mSwitchUserEvent.mAmsSwitchUser_EnterTime;
                if (!this.mSwitchUserEvent.isScreenFrozen) {
                    OplusMultiUserStatisticsData args = new OplusMultiUserStatisticsData();
                    args.flow_7_switchUser_to_continueUserSwitch_Cost = Long.toString(this.mSwitchUserEvent.switchuser_to_continueCost);
                    handleSwitchDone(args);
                }
            }
        }
    }

    @Override // com.oplus.multiuser.IOplusMultiUserStatisticsManager
    public void screenUnFrozen(String reason) {
        synchronized (this.mLockSwitchUserEvent) {
            SwitchUserEvent switchUserEvent = this.mSwitchUserEvent;
            if (switchUserEvent != null && switchUserEvent.isScreenFrozen) {
                Slog.d(TAG, "screenUnFrozen   reason=" + reason + "," + this.mSwitchUserEvent);
                this.mSwitchUserEvent.isScreenFrozen = false;
                if (this.mSwitchUserEvent.continueSwitch_EnterTime > 0) {
                    this.mSwitchUserEvent.unFrozen_EnterTime = SystemClock.elapsedRealtime();
                    SwitchUserEvent switchUserEvent2 = this.mSwitchUserEvent;
                    switchUserEvent2.switchuser_to_unfrozenCost = switchUserEvent2.unFrozen_EnterTime - this.mSwitchUserEvent.mAmsSwitchUser_EnterTime;
                    SwitchUserEvent switchUserEvent3 = this.mSwitchUserEvent;
                    switchUserEvent3.continueUserSwitch_to_unFrozen_Cost = switchUserEvent3.unFrozen_EnterTime - this.mSwitchUserEvent.continueSwitch_EnterTime;
                    OplusMultiUserStatisticsData args = new OplusMultiUserStatisticsData();
                    args.flow_8_switchUser_to_unFrozen_Cost = Long.toString(this.mSwitchUserEvent.switchuser_to_unfrozenCost);
                    args.flow_8_1_continueUserSwitch_to_unFrozen_Cost = Long.toString(this.mSwitchUserEvent.continueUserSwitch_to_unFrozen_Cost);
                    args.flow_8_2_unFrozen_Reason = reason;
                    args.flow_7_switchUser_to_continueUserSwitch_Cost = Long.toString(this.mSwitchUserEvent.switchuser_to_continueCost);
                    handleSwitchDone(args);
                }
            }
        }
    }

    private void handleSwitchDone(OplusMultiUserStatisticsData args) {
        args.flow_2_startUserInternal_ToalCost = Long.toString(this.mSwitchUserEvent.startUserInternal_ToalCost);
        args.flow_2_1_startUserInternal_freezingCost = Long.toString(this.mSwitchUserEvent.startUserInternal_freezingCost);
        args.flow_2_2_startUserInternal_updateConfigurationAndProfileIdsCost = Long.toString(this.mSwitchUserEvent.startUserInternal_updateConfigCost);
        args.flow_2_3_onBeforeStartUser_prepareUserDataCost = Long.toString(this.mSwitchUserEvent.onBeforeStartUser_prepareUserDataCost);
        args.flow_2_3_1 = this.mSwitchUserEvent.onBeforeStartUser_prepareUserDataEx;
        args.flow_2_4_onBeforeStartUser_reconcileAppsDataCost = Long.toString(this.mSwitchUserEvent.onBeforeStartUser_reconcileAppsDataCost);
        args.flow_2_4_1 = this.mSwitchUserEvent.onBeforeStartUser_reconcileAppsDataEx;
        args.flow_2_5_startUserInternal_moveToForegroundCost = Long.toString(this.mSwitchUserEvent.startUserInternal_moveToForegroundCost);
        args.flow_3_onStartUser_ToalCost = Long.toString(this.mSwitchUserEvent.onStartUser_TotalCost);
        args.flow_3_1_onStartUser_max_service_name_and_cost = this.mSwitchUserEvent.onStartUser_max_service_name_and_cost;
        args.flow_3_2_onStartUser_RoleManagerServiceCost = Long.toString(this.mSwitchUserEvent.onStartUser_RoleManagerServiceCost);
        args.flow_3_2_1 = this.mSwitchUserEvent.onStartUser_RoleManagerServiceEx;
        args.flow_3_3_onStartUser_PermissionPolicyServiceCost = Long.toString(this.mSwitchUserEvent.onStartUser_PermissionPolicyServiceCost);
        args.flow_3_3_1 = this.mSwitchUserEvent.onStartUser_PermissionPolicyServiceEx;
        args.flow_3_4_onStartUser_StorageManagerServiceCost = Long.toString(this.mSwitchUserEvent.onStartUser_StorageManagerServiceCost);
        args.flow_3_4_1 = this.mSwitchUserEvent.onStartUser_StorageManagerServiceEx;
        args.flow_4_onSwitchUser_ToalCost = Long.toString(this.mSwitchUserEvent.onSwitchUser_TotalCost);
        args.flow_4_1_onSwitchUser_max_service_name_and_cost = this.mSwitchUserEvent.onSwitchUser_max_service_name_and_cost;
        args.flow_5_dispatchUserSwitch_Last_Observer_Name_Cost = this.mSwitchUserEvent.dispatchUserSwitch_Last_Observer_Name_Cost;
        args.flow_5_1_dispatchUserSwitch_KeyguardUpdateMonitorCost = Long.toString(this.mSwitchUserEvent.dispatchUserSwitch_KeyguardUpdateMonitorCost);
        args.flow_5_1_1 = this.mSwitchUserEvent.dispatchUserSwitch_KeyguardUpdateMonitorEx;
        args.flow_5_2_dispatchUserSwitch_WallpaperManagerServiceCost = Long.toString(this.mSwitchUserEvent.dispatchUserSwitch_WallpaperManagerServiceCost);
        args.flow_5_2_1 = this.mSwitchUserEvent.dispatchUserSwitch_WallpaperManagerServiceEx;
        args.flow_6_dispatchUserSwitch_Observers_no_respond = this.mSwitchUserEvent.dispatchUserSwitch_Observers_no_respond;
        args.oldUserType = this.mSwitchUserEvent.mOldUserType;
        args.newUserType = this.mSwitchUserEvent.mNewUserType;
        args.isSystemclone = Boolean.toString(this.mSwitchUserEvent.isSystemClone);
        OplusMultiUserDcsUtil oplusMultiUserDcsUtil = this.mOplusMultiUserDcsUtil;
        if (oplusMultiUserDcsUtil != null) {
            oplusMultiUserDcsUtil.postSwitchUserMessage(args);
        }
        this.mSwitchUserEvent = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class CreateUserEvent {
        long mCreateUser_EnterTime;
        int mFlags;
        int mUserId;
        String mUserType;
        long mCreateUser_ExitTime = 0;
        String createUserKeyEx = null;
        String prepareUserDataEx = null;
        String createNewUserEx = null;
        String onNewUserCreatedEx = null;

        public CreateUserEvent(long functionStartTime, int userId, String userType, int flags) {
            this.mCreateUser_EnterTime = 0L;
            this.mUserId = -1;
            this.mUserType = null;
            this.mFlags = -1;
            this.mCreateUser_EnterTime = functionStartTime;
            this.mUserId = userId;
            this.mUserType = userType;
            this.mFlags = flags;
        }

        public boolean isMatched(long functionStartTime, int userId, String userType, int flags) {
            int i;
            long j = this.mCreateUser_EnterTime;
            if (j > 0 && j == functionStartTime && userType != null && this.mUserType.equals(userType) && (i = this.mUserId) > 0 && i == userId && this.mFlags == flags) {
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class SwitchUserEvent {
        boolean isSystemClone;
        int mAmsSwitchUser_CurrentUserId;
        long mAmsSwitchUser_EnterTime;
        int mAmsSwitchUser_TargetUserId;
        boolean mUserSwitchUiEnabled;
        String mOldUserType = null;
        String mNewUserType = null;
        boolean mForeground = false;
        long startUserInternal_EnterTime = 0;
        long onBeforeStartUser_TotalCost = 0;
        long onBeforeStartUser_prepareUserDataCost = 0;
        long onBeforeStartUser_reconcileAppsDataCost = 0;
        String onBeforeStartUser_prepareUserDataEx = null;
        String onBeforeStartUser_reconcileAppsDataEx = null;
        long startUserInternal_freezingTime = 0;
        long startUserInternal_freezingCost = 0;
        long startUserInternal_updateConfigCost = 0;
        long startUserInternal_moveToForegroundCost = 0;
        long startUserInternal_ToalCost = 0;
        String onStartUser_max_service_name_and_cost = null;
        long onStartUser_TotalCost = 0;
        long onStartUser_RoleManagerServiceCost = 0;
        long onStartUser_PermissionPolicyServiceCost = 0;
        long onStartUser_StorageManagerServiceCost = 0;
        public String onStartUser_RoleManagerServiceEx = null;
        public String onStartUser_PermissionPolicyServiceEx = null;
        public String onStartUser_StorageManagerServiceEx = null;
        long onSwitchUser_TotalCost = -1;
        String onSwitchUser_max_service_name_and_cost = null;
        long dispatchUserSwitch_EnterTime = 0;
        String dispatchUserSwitch_Last_Observer_Name_Cost = null;
        long dispatchUserSwitch_KeyguardUpdateMonitorCost = 0;
        long dispatchUserSwitch_WallpaperManagerServiceCost = 0;
        String dispatchUserSwitch_KeyguardUpdateMonitorEx = null;
        String dispatchUserSwitch_WallpaperManagerServiceEx = null;
        String dispatchUserSwitch_Observers_no_respond = null;
        long continueSwitch_EnterTime = 0;
        long switchuser_to_continueCost = 0;
        long unFrozen_EnterTime = 0;
        long switchuser_to_unfrozenCost = 0;
        long continueUserSwitch_to_unFrozen_Cost = 0;
        boolean isScreenFrozen = false;

        public SwitchUserEvent(long functionStart, boolean userSwitchUiEnabled, int currentUserId, int targetUserId, boolean systemclone) {
            this.mAmsSwitchUser_EnterTime = 0L;
            this.mUserSwitchUiEnabled = false;
            this.mAmsSwitchUser_CurrentUserId = 0;
            this.mAmsSwitchUser_TargetUserId = 0;
            this.isSystemClone = false;
            this.isSystemClone = systemclone;
            this.mAmsSwitchUser_EnterTime = functionStart;
            this.mUserSwitchUiEnabled = userSwitchUiEnabled;
            this.mAmsSwitchUser_CurrentUserId = currentUserId;
            this.mAmsSwitchUser_TargetUserId = targetUserId;
        }

        public boolean isMatched(int currentUserId, int targetUserId) {
            if (this.mAmsSwitchUser_CurrentUserId != currentUserId || this.mAmsSwitchUser_TargetUserId != targetUserId) {
                return false;
            }
            return true;
        }

        public String toString() {
            String res = "SwitchUserEvent{from=" + this.mAmsSwitchUser_CurrentUserId + ",to=" + this.mAmsSwitchUser_TargetUserId + "}";
            return res;
        }
    }

    /* loaded from: classes.dex */
    class ShortUserInfo {
        int mFlags;
        int mId;
        String mUserType;

        public ShortUserInfo(int userId, int flags, String userType) {
            this.mId = 0;
            this.mFlags = 0;
            this.mUserType = null;
            this.mId = userId;
            this.mFlags = flags;
            this.mUserType = userType;
        }

        public String toString() {
            String res = "{id=" + this.mId + ",flags=" + this.mFlags + ",type=" + this.mUserType + "}";
            return res;
        }
    }
}
