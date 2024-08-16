package l6;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import k6.TaskCompletionSource;
import kotlin.Metadata;
import ma.Unit;
import za.k;

/* compiled from: CompletableFutureTasks.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\t\u0010\nJ(\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005¨\u0006\u000b"}, d2 = {"Ll6/b;", "", "TResult", "Ljava/util/concurrent/Executor;", "executor", "Ljava/util/function/Supplier;", "supplier", "Lk6/d;", "b", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* renamed from: l6.b, reason: use source file name */
/* loaded from: classes.dex */
public final class CompletableFutureTasks {

    /* renamed from: a, reason: collision with root package name */
    public static final CompletableFutureTasks f14636a = new CompletableFutureTasks();

    private CompletableFutureTasks() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void c(TaskCompletionSource taskCompletionSource, Object obj, Throwable th) {
        Unit unit;
        k.e(taskCompletionSource, "$taskCompletionSource");
        if (obj == null) {
            unit = null;
        } else {
            taskCompletionSource.e(obj);
            unit = Unit.f15173a;
        }
        if (unit == null) {
            taskCompletionSource.c(-1, "result is null");
        }
        if (th instanceof Exception) {
            taskCompletionSource.d((Exception) th);
        }
    }

    public final <TResult> k6.d<TResult> b(Executor executor, Supplier<TResult> supplier) {
        k.e(executor, "executor");
        k.e(supplier, "supplier");
        final TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        CompletableFuture.supplyAsync(supplier, executor).whenComplete(new BiConsumer() { // from class: l6.a
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                CompletableFutureTasks.c(TaskCompletionSource.this, obj, (Throwable) obj2);
            }
        });
        return taskCompletionSource.a();
    }
}
