package com.oplus.ovoiceskillservice.utils;

/* loaded from: classes.dex */
public abstract class ThreadTask implements Runnable {

    /* loaded from: classes.dex */
    public enum TaskGuard {
        ENTER,
        RETRY,
        DISCARD
    }

    public TaskGuard guard() {
        return TaskGuard.ENTER;
    }
}
