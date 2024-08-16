package l6;

import java.util.concurrent.Executor;
import k6.PendingResult;
import kotlin.Metadata;
import za.k;

/* compiled from: OnSuccessPendingResult.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000\u0002B\u001d\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00000\t¢\u0006\u0004\b\u000b\u0010\fJ\u0016\u0010\u0006\u001a\u00020\u00052\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0016¨\u0006\r"}, d2 = {"Ll6/d;", "TResult", "Lk6/c;", "Lk6/d;", "task", "Lma/f0;", "a", "Ljava/util/concurrent/Executor;", "executor", "Lk6/b;", "listener", "<init>", "(Ljava/util/concurrent/Executor;Lk6/b;)V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* renamed from: l6.d, reason: use source file name */
/* loaded from: classes.dex */
public final class OnSuccessPendingResult<TResult> implements PendingResult<TResult> {

    /* renamed from: a, reason: collision with root package name */
    private final Executor f14639a;

    /* renamed from: b, reason: collision with root package name */
    private final k6.b<TResult> f14640b;

    public OnSuccessPendingResult(Executor executor, k6.b<TResult> bVar) {
        k.e(executor, "executor");
        k.e(bVar, "listener");
        this.f14639a = executor;
        this.f14640b = bVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static final void c(k6.d dVar, OnSuccessPendingResult onSuccessPendingResult) {
        k.e(dVar, "$task");
        k.e(onSuccessPendingResult, "this$0");
        Object b10 = dVar.b();
        if (b10 == null) {
            return;
        }
        onSuccessPendingResult.f14640b.onSuccess(b10);
    }

    @Override // k6.PendingResult
    public void a(final k6.d<TResult> dVar) {
        k.e(dVar, "task");
        if (dVar.c()) {
            this.f14639a.execute(new Runnable() { // from class: l6.c
                @Override // java.lang.Runnable
                public final void run() {
                    OnSuccessPendingResult.c(k6.d.this, this);
                }
            });
        }
    }
}
