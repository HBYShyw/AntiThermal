package com.oplus.oms.split.splitreport;

/* loaded from: classes.dex */
public class SplitLoadError {
    public static final int ACTIVATE_APPLICATION_FAILED = -25;
    public static final int CREATE_APPLICATION_FAILED = -24;
    public static final int CREATE_CLASSLOADER_FAILED = -27;
    public static final int CREATE_PROVIDERS_FAILED = -26;
    public static final int INTERNAL_ERROR = -100;
    public static final int INTERRUPTED_ERROR = -99;
    public static final int LOAD_DEX_FAILED = -23;
    public static final int LOAD_LIB_FAILED = -22;
    public static final int LOAD_RES_FAILED = -21;
    private final Throwable mCause;
    private final int mErrorCode;
    private final String mSplitName;
    private final String mVersion;

    public SplitLoadError(String splitName, String version, int errorCode, Throwable cause) {
        this.mSplitName = splitName;
        this.mVersion = version;
        this.mErrorCode = errorCode;
        this.mCause = cause;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public String toString() {
        return "{\"splitName\":\"" + this.mSplitName + "\",\"version\":\"" + this.mVersion + "\",\",errorCode\":" + this.mErrorCode + "\",errorMsg\":\"" + this.mCause.getMessage() + "\"}";
    }
}
