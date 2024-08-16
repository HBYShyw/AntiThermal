package android.telephony.ims;

/* loaded from: classes.dex */
public interface ImsRilConstant {
    public static final int CALL_PROGRESS_INFO_INDICATION = 1001;
    public static final int CALL_PROGRESS_INFO_TYPE_CALL_FORWARDING = 2;
    public static final int CALL_PROGRESS_INFO_TYPE_CALL_REJ_Q850 = 0;
    public static final int CALL_PROGRESS_INFO_TYPE_CALL_WAITING = 1;
    public static final int CALL_PROGRESS_INFO_TYPE_INVALID = -1;
    public static final int CALL_PROGRESS_INFO_TYPE_REMOTE_AVAILABLE = 3;
    public static final int CALL_REJECTION_CODE_INVALID = -1;
    public static final int CALL_REJECTION_CODE_NO_USER_RESPONDING = 18;
    public static final int CALL_REJECTION_CODE_SUBSCRIBER_ABSENT = 20;
    public static final int CALL_REJECTION_CODE_UNALLOCATED_NUMBER = 1;
    public static final int CALL_REJECTION_CODE_USER_BUSY = 17;
    public static final int COMMON_REQID_CALL_CLEAN_CONN = 3;
    public static final int COMMON_REQID_CLEAN_ALL_CONN = 4;
    public static final int COMMON_REQID_END = 4;
    public static final int COMMON_REQID_GET_CURRENT_CALLS = 2;
    public static final int COMMON_REQID_GET_PENDING_REQUEST = 8;
    public static final int COMMON_REQID_VOPS = 1;
    public static final int COMMON_SOLID_GET_PENDING_REQUEST_DONE = 9;
    public static final int COMMON_UNSOLID_END_CALLTRACKER_STATE = 6;
    public static final int COMMON_UNSOLID_MARK_DISCONNECTING_STATE = 7;
    public static final int COMMON_UNSOLID_UPDATE_CALLTRACKER_STATE = 5;
    public static final int CRS_TYPE_AUDIO = 1;
    public static final int CRS_TYPE_INVALID = 0;
    public static final int CRS_TYPE_UPDATE_INDICATION = 1000;
    public static final int CRS_TYPE_VIDEO = 2;
    public static final int EVENT_COMMON_REQ_TO_IMS = 100;
    public static final int EVENT_COMMON_REQ_TO_IMS_DONE = 101;
    public static final int IMS_RIL_INDICATION = 102;
    public static final String KEY_CRS_TYPE = "crsType";
    public static final String KEY_IS_PREPARATORY = "isPreparatory";
    public static final String KEY_PROGRESS_INFO_TYPE = "progressInfoType";
    public static final String KEY_PROGRESS_REASON_CODE = "progressReasonCode";
    public static final int PDC_REFRESH_INDICATION_BASE = 1002;
    public static final int PDC_REFRESH_INDICATION_COMPLETE = 1003;
    public static final int PDC_REFRESH_INDICATION_START = 1002;
}
