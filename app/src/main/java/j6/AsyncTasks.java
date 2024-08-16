package j6;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import k6.TaskCompletionSource;
import k6.TaskExecutors;
import k6.d;
import kotlin.Metadata;
import ma.Unit;
import za.k;

/* compiled from: AsyncTasks.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\n\u0010\u000bJ\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0007J\u001e\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0002H\u0002¨\u0006\f"}, d2 = {"Lj6/b;", "", "Ljava/lang/Runnable;", "runnable", "Lk6/d;", "Lma/f0;", "d", "Ljava/util/concurrent/Executor;", "executor", "b", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* renamed from: j6.b, reason: use source file name */
/* loaded from: classes.dex */
public final class AsyncTasks {

    /* renamed from: a, reason: collision with root package name */
    public static final AsyncTasks f13012a = new AsyncTasks();

    private AsyncTasks() {
    }

    private final d<Unit> b(Executor executor, Runnable runnable) {
        final TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        CompletableFuture.runAsync(runnable, executor).whenComplete(new BiConsumer() { // from class: j6.a
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AsyncTasks.c(TaskCompletionSource.this, (Void) obj, (Throwable) obj2);
            }
        });
        return taskCompletionSource.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void c(TaskCompletionSource taskCompletionSource, Void r12, Throwable th) {
        k.e(taskCompletionSource, "$taskCompletionSource");
        if (th instanceof Exception) {
            taskCompletionSource.d((Exception) th);
        }
    }

    public static final d<Unit> d(Runnable runnable) {
        k.e(runnable, "runnable");
        return f13012a.b(TaskExecutors.f14056f.a().d(), runnable);
    }
}
