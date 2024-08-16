package com.android.server.oplus.osense;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class OsenseConstants {
    public static final int COMPLEX_SCENE_FLOAT_WINDOW = 2;
    public static final int COMPLEX_SCENE_FOCUS_CHANGED = 3;
    public static final int COMPLEX_SCENE_SPLIT_SCREEN = 1;
    private static final int INVALID_ID = 1000;
    public static final String KEY_BOOLEAN_ADD = "isAdded";
    public static final String KEY_BOOLEAN_REINSTALL = "isReInstall";
    public static final String KEY_COMPLEX_SCENE_ARRAY = "complexSceneTypeArray";
    public static final String KEY_INTEGER_HASH = "hashCode";
    public static final String KEY_INTEGER_PID = "pid";
    public static final String KEY_INTEGER_TYPE = "type";
    public static final String KEY_INTEGER_UID = "uid";
    public static final String KEY_INTEGER_UID_CUR = "curUid";
    public static final String KEY_INTEGER_UID_PRE = "prevUid";
    public static final String KEY_LIST_INFO = "infoList";
    public static final String KEY_LIST_INTEGER_ADD = "addedUidList";
    public static final String KEY_LIST_INTEGER_RM = "removedUidList";
    public static final String KEY_LIVE_WALLPAPER = "liveWallpaper";
    public static final String KEY_LONG_ELAPSED_TIME = "elapsedTime";
    public static final String KEY_STRING_ACTION = "action";
    public static final String KEY_STRING_HASH = "hashStr";
    public static final String KEY_STRING_PERNOTIFY = "perNotification";
    public static final String KEY_STRING_PKG = "pkgName";
    public static final String KEY_STRING_PKG_CUR = "curPkgName";
    public static final String KEY_STRING_PKG_PRE = "prevPkgName";
    public static final String KEY_STRING_SENSOR_TYPE = "sensorType";
    public static final String KEY_STRING_STATE = "state";
    private static final String TAG = "OsenseConstants";

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public enum FeatureType {
        FEATURE_APPMNG(1, "NRT-AppMng"),
        FEATURE_MEMORY(2, "NRT-Memory"),
        FEATURE_CPU(3, "NRT-CPU"),
        FEATURE_IO(4, "NRT-IO"),
        FEATURE_BOOST(5, "RT-Boost"),
        FEATURE_INVALID(1000, "Invalid");

        private final int mId;
        private final String mName;

        FeatureType(int i, String str) {
            this.mId = i;
            this.mName = str;
        }

        public int getId() {
            return this.mId;
        }

        public String getName() {
            return this.mName;
        }

        public static int getFeatureId(FeatureType featureType) {
            if (featureType == null) {
                return FEATURE_INVALID.getId();
            }
            return featureType.getId();
        }

        public static FeatureType getFeatureType(int i) {
            if (i < 0) {
                return FEATURE_INVALID;
            }
            for (FeatureType featureType : values()) {
                if (featureType.getId() == i) {
                    return featureType;
                }
            }
            return FEATURE_INVALID;
        }

        @Override // java.lang.Enum
        public String toString() {
            return getName();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public enum PolicyType {
        POLICY_ORMS(1, "orms"),
        POLICY_OFREEZER(2, "ofreezer"),
        POLICY_CLEAN(3, "clean"),
        POLICY_PRELOAD(4, "preload"),
        POLICY_PKG_DISABLER(5, "pkg-disabler"),
        POLICY_APP_STARTUP(6, "app-startup"),
        POLICY_APP_CPULIMIT(7, "app-cpulimit"),
        POLICY_APP_COMPACT(8, "app-compact"),
        POLICY_ACTIVITY_PRELOAD(9, "activity-preload"),
        POLICY_OGUARD(10, "oguard"),
        POLICY_IOLIMIT(11, "iolimit"),
        POLICY_REENTRANT(12, "reentrant"),
        POLICY_CPN(13, "cpnProxy"),
        POLICY_INVALID_TYPE(1000, "invalid");

        private final int mId;
        private final String mName;

        PolicyType(int i, String str) {
            this.mId = i;
            this.mName = str;
        }

        public int getId() {
            return this.mId;
        }

        public String getName() {
            return this.mName;
        }

        public static int getPolicyId(PolicyType policyType) {
            if (policyType == null) {
                return POLICY_INVALID_TYPE.getId();
            }
            return policyType.getId();
        }

        public static int getPolicyId(int i) {
            if (i < 0) {
                return POLICY_INVALID_TYPE.getId();
            }
            for (PolicyType policyType : values()) {
                if (policyType.getId() == i) {
                    return i;
                }
            }
            return POLICY_INVALID_TYPE.getId();
        }

        public static PolicyType getPolicyType(int i) {
            if (i < 0) {
                return POLICY_INVALID_TYPE;
            }
            for (PolicyType policyType : values()) {
                if (policyType.getId() == i) {
                    return policyType;
                }
            }
            return POLICY_INVALID_TYPE;
        }

        public static PolicyType getPolicyType(String str) {
            if (str == null) {
                return POLICY_INVALID_TYPE;
            }
            for (PolicyType policyType : values()) {
                if (policyType.getName().equals(str)) {
                    return policyType;
                }
            }
            return POLICY_INVALID_TYPE;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public enum SceneType {
        SCENE_ALL(0),
        SCENE_SCREEN_STATUS(1),
        SCENE_BOOT_COMPLETED(2),
        SCENE_RES_MEM_PSI_NORMAL(3),
        SCENE_LOW_POWER_MODE(4),
        SCENE_SUPER_POWER_SAVE_MODE(5),
        SCENE_THERMAL_LEVEL_CHANGED(6),
        SCENE_APP_SWITCH(7),
        SCENE_BIG_APP_SWITCH(8),
        SCENE_SPECIAL_REQUEST(9),
        SCENE_LAUNCHER(10),
        SCENE_RES_MEM_PSI_CAMERA(11),
        SCENE_QUICK_BOOT_ENTER(12),
        SCENE_QUICK_BOOT_EXIT(13),
        SCENE_GAME(14),
        SCENE_AI_PREDICT(15),
        SCENE_GAME_PREDICT(16),
        SCENE_RES_MEM_PSI_GAME(17),
        SCENE_SLIP(18),
        SCENE_IDLE_RECOVER(19),
        SCENE_RES_CPU_PSI(20),
        SCENE_RES_IO_PSI(21),
        SCENE_PERMANENT_ALIVE_ACTIVITY(22),
        SCENE_PERMANENT_ALIVE_PROCESS(23),
        SCENE_ACTIVITY_IDLE(24),
        SCENE_MEM_RELEASED(25),
        SCENE_RES_MEM_PSI_LEAK(26),
        SCENE_RES_MEM_PSI_THREADEXCEEDED(27),
        SCENE_INVALID_TYPE(1000);

        private final int mId;

        SceneType(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        public static int getSceneId(int i) {
            if (i < 0) {
                return SCENE_INVALID_TYPE.getId();
            }
            for (SceneType sceneType : values()) {
                if (sceneType.getId() == i) {
                    return i;
                }
            }
            return SCENE_INVALID_TYPE.getId();
        }

        public static SceneType getSceneType(String str) {
            for (SceneType sceneType : values()) {
                if (sceneType.name().equals(str)) {
                    return sceneType;
                }
            }
            return SCENE_INVALID_TYPE;
        }

        public static int getSceneId(SceneType sceneType) {
            if (sceneType == null) {
                return SCENE_INVALID_TYPE.getId();
            }
            return sceneType.getId();
        }

        public static SceneType getSceneType(int i) {
            if (i < 0) {
                return SCENE_INVALID_TYPE;
            }
            for (SceneType sceneType : values()) {
                if (sceneType.getId() == i) {
                    return sceneType;
                }
            }
            return SCENE_INVALID_TYPE;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public enum SysStatusType {
        STATUS_ALL(0),
        STATUS_SCREEN_STATUS(1),
        STATUS_BOOT_COMPLETED(2),
        STATUS_LOW_MEMORY(3),
        STATUS_LOW_POWER_MODE(4),
        STATUS_SUPER_POWER_SAVE_MODE(5),
        STATUS_THERMAL_LEVEL_CHANGED(6),
        STATUS_USER_PRESENT(7),
        STATUS_DATE_CHANGED(8),
        STATUS_AOD_CHANGED(9),
        STATUS_POWER_STATUS(10),
        STATUS_USER_STATUS(11),
        STATUS_SHUT_DOWN(12),
        STATUS_INVALID_TYPE(1000);

        private final int mId;

        SysStatusType(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        public static int getSysStatusId(SysStatusType sysStatusType) {
            if (sysStatusType == null) {
                return STATUS_INVALID_TYPE.getId();
            }
            return sysStatusType.getId();
        }

        public static SysStatusType getSysStatusType(int i) {
            if (i < 0) {
                return STATUS_INVALID_TYPE;
            }
            for (SysStatusType sysStatusType : values()) {
                if (sysStatusType.getId() == i) {
                    return sysStatusType;
                }
            }
            return STATUS_INVALID_TYPE;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public enum AppStatusType {
        STATUS_ALL(0, "all-status"),
        STATUS_TRAFFIC(1, "traffic"),
        STATUS_AUDIO_FOCUS(2, "audio-focus"),
        STATUS_GPS(3, "gps"),
        STATUS_BT(4, "bt"),
        STATUS_SYSTEM_WINDOW(5, "system-window"),
        STATUS_DEFAULT_LAUNCHER(6, "def-launcher"),
        STATUS_DEFAULT_SMS(7, "def-sms"),
        STATUS_DEFAULT_DIALER(8, "def-dialer"),
        STATUS_VPN_CONN(9, "vpn-conn"),
        STATUS_INPUT(10, "input-method"),
        STATUS_SCREEN_RECORDER(11, "screen-recorder"),
        STATUS_WALLPAPER(12, "wallpaper"),
        STATUS_TOP_APP(13, "top-app"),
        STATUS_FOREGROUND_SERVICE(14, "fg-service"),
        STATUS_PKG_STATUS_CHANGED(15, "pkg-status"),
        STATUS_PIP(16, "pip"),
        STATUS_WIDGET(17, "widget"),
        STATUS_AUDIO_RECORDER(18, "audio-recorder"),
        STATUS_ACTIVITY_WINDOW(19, "activity-window"),
        STATUS_ONGOING_NOTI(20, "ongoing-noti"),
        STATUS_VIDEO(21, "video"),
        STATUS_SENSOR(22, "sensor"),
        STATUS_RECENT_LOCKED(23, "recent-locked"),
        STATUS_PROTECTED_GAME(24, "protected-game"),
        STATUS_FLASH_BACK(25, "flash-back"),
        STATUS_NOTIFICATION(26, "notification"),
        STATUS_APP_CARD_VISIBLE(27, "app-card"),
        STATUS_NFC_PAYMENT(28, "nfc-payment"),
        STATUS_ORIGINAL_GPS(29, "original-gps"),
        STATUS_INVALID_TYPE(1000, "invalid");

        private final int mId;
        private final String mName;

        AppStatusType(int i, String str) {
            this.mId = i;
            this.mName = str;
        }

        public int getId() {
            return this.mId;
        }

        public String getName() {
            return this.mName;
        }

        public static int getAppStatusId(AppStatusType appStatusType) {
            if (appStatusType == null) {
                return STATUS_INVALID_TYPE.getId();
            }
            return appStatusType.getId();
        }

        public static AppStatusType getAppStatusType(int i) {
            if (i < 0) {
                return STATUS_INVALID_TYPE;
            }
            for (AppStatusType appStatusType : values()) {
                if (appStatusType.getId() == i) {
                    return appStatusType;
                }
            }
            return STATUS_INVALID_TYPE;
        }

        @Override // java.lang.Enum
        public String toString() {
            return getName();
        }
    }
}
