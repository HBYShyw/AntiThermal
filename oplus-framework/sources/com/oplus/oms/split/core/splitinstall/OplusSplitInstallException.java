package com.oplus.oms.split.core.splitinstall;

/* loaded from: classes.dex */
public class OplusSplitInstallException extends RuntimeException {
    private final int mErrorCode;

    public OplusSplitInstallException(int errorCode) {
        super("Split Install Error: " + errorCode);
        this.mErrorCode = errorCode;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }
}
