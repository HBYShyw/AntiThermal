package com.oplus.oms.split.core.tasks;

/* loaded from: classes.dex */
public class OplusRuntimeExecutionException extends RuntimeException {
    /* JADX INFO: Access modifiers changed from: protected */
    public OplusRuntimeExecutionException(Throwable th) {
        super(th);
    }

    protected int getErrorCode() {
        return -100;
    }
}
