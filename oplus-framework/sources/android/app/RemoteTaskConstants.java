package android.app;

/* loaded from: classes.dex */
public final class RemoteTaskConstants {
    public static final int CLOSE_REASON_INVALID = -1;
    public static final int CLOSE_REASON_NORMAL_CLOSE = 2;
    public static final int CLOSE_REASON_SUSPEND_CLOSE = 3;
    public static final int CLOSE_REASON_SWITCH_TASK_MD_TO_VD = 1;
    public static final int CLOSE_REASON_SWITCH_TASK_VD_TO_MD = 0;
    public static final int CLOSE_REASON_SYSTEM_CLOSE = 5;
    public static final int CLOSE_REASON_USER_CLOSE = 4;
    public static final String CROSS_DEVICE_SERVICE = "cross_device_service";
    public static final int DEVICE_AVAILABILITY_STATE_CONSTRAINED = 4;
    public static final int DEVICE_AVAILABILITY_STATE_CRITICAL = 16;
    public static final int DEVICE_AVAILABILITY_STATE_FREE = 8;
    public static final int FLAG_SWITCH_TASK_MD_TO_VD = 1;
    public static final int FLAG_SWITCH_TASK_NO_ACTION = -1;
    public static final int FLAG_SWITCH_TASK_VD_TO_MD = 0;
    public static final String KEY_REMOTE_TASK_LAUNCH_OPTION = "deviceintegration:remotetask.launchOption";
    public static final String KEY_REMOTE_TASK_SECURITY_TOKEN = "deviceintegration:remotetask.security.token";
    public static final String KEY_REMOTE_TASK_UUID = "deviceintegration:remotetask.uuid";
    public static final float OVERHEAT_THRESHOLD = 45.0f;
    public static final int REASON_DUPLICATE_LAUNCH = 2;
    public static final int REASON_NEW_INSTANCE_DENIED = 1;
    public static final int REASON_UNSPECIFIED = 0;
    public static final int REMOTE_TASK_FLAG_DEFAULT = 0;
    public static final int REMOTE_TASK_FLAG_LAUNCH = 32;
    public static final float RESUME_OVERHEAT_THRESHOLD = 40.0f;
    public static final int THERMAL_STATUS_NORMAL = 0;
    public static final int THERMAL_STATUS_OVERHEAT = 1;
}
