package com.oplus.deepthinker.sdk.app.api;

import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventCallback;
import java.util.concurrent.locks.ReentrantLock;
import k6.d;
import kotlin.Metadata;
import ma.h;
import ma.j;
import za.k;

/* compiled from: ApiCallBack.kt */
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b&\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0007¢\u0006\u0004\b\u0017\u0010\u0018J\u001d\u0010\b\u001a\u00020\u00052\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0000¢\u0006\u0004\b\u0006\u0010\u0007J\u0017\u0010\n\u001a\u00020\u00052\u0006\u0010\t\u001a\u00028\u0000H\u0004¢\u0006\u0004\b\n\u0010\u000bJ\u001a\u0010\u0010\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\f2\b\u0010\u000f\u001a\u0004\u0018\u00010\u000eH\u0004R\u001b\u0010\u0016\u001a\u00020\u00118BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015¨\u0006\u0019"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/api/ApiCallBack;", "TResult", "Lcom/oplus/deepthinker/sdk/app/aidl/eventfountain/EventCallback;", "Lk6/d;", "task", "Lma/f0;", "setTask$com_oplus_deepthinker_sdk_release", "(Lk6/d;)V", "setTask", "result", "onSuccess", "(Ljava/lang/Object;)V", "", "code", "", "msg", "onFailure", "Ljava/util/concurrent/locks/ReentrantLock;", "lock$delegate", "Lma/h;", "getLock", "()Ljava/util/concurrent/locks/ReentrantLock;", "lock", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public abstract class ApiCallBack<TResult> extends EventCallback {

    /* renamed from: lock$delegate, reason: from kotlin metadata */
    private final h lock;
    private volatile d<TResult> task;

    public ApiCallBack() {
        h b10;
        b10 = j.b(ApiCallBack$lock$2.INSTANCE);
        this.lock = b10;
    }

    private final ReentrantLock getLock() {
        return (ReentrantLock) this.lock.getValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void onFailure(int i10, String str) {
        getLock().lock();
        try {
            d<TResult> dVar = this.task;
            if (dVar != null) {
                dVar.d(i10, str);
            }
            this.task = null;
        } finally {
            getLock().unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void onSuccess(TResult result) {
        getLock().lock();
        try {
            d<TResult> dVar = this.task;
            if (dVar != null) {
                dVar.e(result);
            }
            this.task = null;
        } finally {
            getLock().unlock();
        }
    }

    public final void setTask$com_oplus_deepthinker_sdk_release(d<TResult> task) {
        k.e(task, "task");
        getLock().lock();
        try {
            this.task = task;
        } finally {
            getLock().unlock();
        }
    }
}
