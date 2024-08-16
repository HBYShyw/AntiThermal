package com.oplus.oms.split.splitload;

/* loaded from: classes.dex */
final class SplitLoadException extends Exception {
    private final int mErrorCode;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitLoadException(int errorCode, Throwable e) {
        super("Split Load Error: " + errorCode, e);
        this.mErrorCode = errorCode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getErrorCode() {
        return this.mErrorCode;
    }
}
