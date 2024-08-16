package com.oplus.wrapper.os;

import android.os.Bundle;
import android.os.SynchronousResultReceiver;
import java.util.concurrent.TimeoutException;

/* loaded from: classes.dex */
public class SynchronousResultReceiver {
    private final android.os.SynchronousResultReceiver mSynchronousResultReceiver;

    public SynchronousResultReceiver(String name) {
        this.mSynchronousResultReceiver = new android.os.SynchronousResultReceiver(name);
    }

    public String getName() {
        return this.mSynchronousResultReceiver.getName();
    }

    public Result awaitResult(long timeoutMillis) throws TimeoutException {
        SynchronousResultReceiver.Result awaitResult = this.mSynchronousResultReceiver.awaitResult(timeoutMillis);
        if (awaitResult == null) {
            return null;
        }
        return new Result(awaitResult);
    }

    /* loaded from: classes.dex */
    public static class Result {
        private final SynchronousResultReceiver.Result mResult;

        private Result(SynchronousResultReceiver.Result result) {
            this.mResult = result;
        }

        public Bundle getBundle() {
            return this.mResult.bundle;
        }
    }
}
