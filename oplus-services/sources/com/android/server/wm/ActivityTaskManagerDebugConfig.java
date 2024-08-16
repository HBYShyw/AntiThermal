package com.android.server.wm;

import android.os.SystemProperties;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityTaskManagerDebugConfig {
    public static boolean APPEND_CATEGORY_NAME = false;
    public static boolean DEBUG_ALL = false;
    static final String POSTFIX_ADD_REMOVE;
    static final String POSTFIX_APP;
    static final String POSTFIX_CLEANUP;
    public static final String POSTFIX_CONFIGURATION;
    static final String POSTFIX_CONTAINERS;
    static final String POSTFIX_FOCUS;
    static final String POSTFIX_IDLE;
    static final String POSTFIX_IMMERSIVE;
    public static final String POSTFIX_LOCKTASK;
    static final String POSTFIX_PAUSE;
    static final String POSTFIX_RECENTS;
    static final String POSTFIX_RELEASE;
    static final String POSTFIX_RESULTS;
    static final String POSTFIX_ROOT_TASK;
    static final String POSTFIX_SAVED_STATE;
    static final String POSTFIX_STATES;
    public static final String POSTFIX_SWITCH;
    static final String POSTFIX_TASKS;
    static final String POSTFIX_TRANSITION;
    static final String POSTFIX_USER_LEAVING;
    static final String POSTFIX_VISIBILITY;
    static final String TAG_ATM = "ActivityTaskManager";
    static final boolean TAG_WITH_CLASS_NAME = false;
    public static boolean DEBUG_ALL_ACTIVITIES = false;
    static boolean DEBUG_TASKS = false;
    static boolean DEBUG_STACK = false;
    public static boolean DEBUG_RECENTS = false;
    public static boolean DEBUG_RECENTS_TRIM_TASKS = false;
    public static boolean DEBUG_ROOT_TASK = false;
    public static boolean DEBUG_SWITCH = false;
    public static boolean DEBUG_TRANSITION = false;
    public static boolean DEBUG_VISIBILITY = false;
    public static boolean DEBUG_APP = false;
    public static boolean DEBUG_IDLE = false;
    public static boolean DEBUG_RELEASE = false;
    public static boolean DEBUG_USER_LEAVING = false;
    public static boolean DEBUG_PERMISSIONS_REVIEW = false;
    public static boolean DEBUG_RESULTS = false;
    public static boolean DEBUG_ACTIVITY_STARTS = false;
    public static boolean DEBUG_CLEANUP = false;
    public static boolean DEBUG_METRICS = false;
    private static final boolean DEBUG_VERSION = "userdebug".equals(SystemProperties.get("ro.build.type"));
    public static boolean DEBUG_AMS = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    static {
        boolean z = APPEND_CATEGORY_NAME;
        POSTFIX_APP = z ? "_App" : "";
        POSTFIX_CLEANUP = z ? "_Cleanup" : "";
        POSTFIX_IDLE = z ? "_Idle" : "";
        POSTFIX_RELEASE = z ? "_Release" : "";
        POSTFIX_USER_LEAVING = z ? "_UserLeaving" : "";
        POSTFIX_ADD_REMOVE = z ? "_AddRemove" : "";
        POSTFIX_CONFIGURATION = z ? "_Configuration" : "";
        POSTFIX_CONTAINERS = z ? "_Containers" : "";
        POSTFIX_FOCUS = z ? "_Focus" : "";
        POSTFIX_IMMERSIVE = z ? "_Immersive" : "";
        POSTFIX_LOCKTASK = z ? "_LockTask" : "";
        POSTFIX_PAUSE = z ? "_Pause" : "";
        POSTFIX_RECENTS = z ? "_Recents" : "";
        POSTFIX_SAVED_STATE = z ? "_SavedState" : "";
        POSTFIX_ROOT_TASK = z ? "_RootTask" : "";
        POSTFIX_STATES = z ? "_States" : "";
        POSTFIX_SWITCH = z ? "_Switch" : "";
        POSTFIX_TASKS = z ? "_Tasks" : "";
        POSTFIX_TRANSITION = z ? "_Transition" : "";
        POSTFIX_VISIBILITY = z ? "_Visibility" : "";
        POSTFIX_RESULTS = z ? "_Results" : "";
    }
}
