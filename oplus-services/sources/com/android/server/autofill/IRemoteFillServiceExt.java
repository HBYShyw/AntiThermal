package com.android.server.autofill;

import android.content.ComponentName;
import android.service.autofill.FillContext;
import android.service.autofill.FillResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IRemoteFillServiceExt {
    default void delayCancelRequest(List<FillContext> list, int i, CompletableFuture<FillResponse> completableFuture) {
    }

    default long getOplusRequestTimeoutMillis(ComponentName componentName, long j) {
        return j;
    }

    default long getOplusTimeoutIdleBindMillis(ComponentName componentName, long j) {
        return j;
    }

    default long getOplusTimeoutMillis(ComponentName componentName, long j) {
        return j;
    }
}
