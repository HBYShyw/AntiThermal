package com.oplus.oms.split.splitinstall;

/* loaded from: classes.dex */
public final class InstallException extends Exception {
    private final int mErrorCode;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InstallException(int errorCode, Throwable e) {
        super("Split Install Error: " + errorCode, e);
        this.mErrorCode = errorCode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getErrorCode() {
        return this.mErrorCode;
    }
}
