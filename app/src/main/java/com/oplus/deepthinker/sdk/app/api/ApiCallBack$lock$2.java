package com.oplus.deepthinker.sdk.app.api;

import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import za.Lambda;

/* compiled from: ApiCallBack.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "Ljava/util/concurrent/locks/ReentrantLock;", "TResult", "invoke"}, k = 3, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
final class ApiCallBack$lock$2 extends Lambda implements ya.a<ReentrantLock> {
    public static final ApiCallBack$lock$2 INSTANCE = new ApiCallBack$lock$2();

    ApiCallBack$lock$2() {
        super(0);
    }

    @Override // ya.a
    public final ReentrantLock invoke() {
        return new ReentrantLock();
    }
}
