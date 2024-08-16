package com.oplus.deepthinker.sdk.app.aidl.eventfountain;

/* loaded from: classes.dex */
public class EventFountainRegisterCode {
    public static final int BINDER_TRANSACTION_ERROR = 128;
    public static final int EVENT_NOT_AVAILABLE = 2;
    public static final int INVALID_PARAMETERS = 16;
    public static final int NOT_IMPLEMENTED = 0;
    public static final int OS_VERSION_NOT_SUPPORT = 8;
    public static final int PERMISSION_NOT_GRANT = 256;
    public static final int PID_REGISTER_LIMITED = 4;
    public static final int REGISTER_SUCCESS = 1;
    public static final int SERVER_INTERVAL_ERROR = 32;
    public static final int UNSUPPORTED_PARAMETER = 64;

    public static String resultCodeToString(int i10) {
        return i10 != 0 ? i10 != 1 ? i10 != 2 ? i10 != 4 ? i10 != 8 ? i10 != 16 ? i10 != 32 ? i10 != 64 ? i10 != 128 ? i10 != 256 ? "UNKNOWN_RESULT_CODE" : "PERMISSION_NOT_GRANT" : "BINDER_TRANSACTION_ERROR" : "UNSUPPORTED_PARAMETER" : "SERVER_INTERVAL_ERROR" : "INVALID_PARAMETERS" : "OS_VERSION_NOT_SUPPORT" : "PID_REGISTER_LIMITED" : "EVENT_NOT_AVAILABLE" : "REGISTER_SUCCESS" : "NOT_IMPLEMENTED";
    }
}
