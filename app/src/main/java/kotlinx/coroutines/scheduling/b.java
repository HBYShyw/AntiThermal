package kotlinx.coroutines.scheduling;

import fb._Ranges;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlinx.coroutines.internal.b0;
import kotlinx.coroutines.internal.d0;
import td.CoroutineDispatcher;
import td.Executors;

/* compiled from: Dispatcher.kt */
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\bÀ\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\t\b\u0002¢\u0006\u0004\b\u000f\u0010\u0010J\u0010\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0003H\u0016J\u001c\u0010\u000b\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00072\n\u0010\n\u001a\u00060\u0003j\u0002`\tH\u0016J\b\u0010\f\u001a\u00020\u0005H\u0016J\b\u0010\u000e\u001a\u00020\rH\u0016¨\u0006\u0011"}, d2 = {"Lkotlinx/coroutines/scheduling/b;", "Ltd/b1;", "Ljava/util/concurrent/Executor;", "Ljava/lang/Runnable;", "command", "Lma/f0;", "execute", "Lqa/g;", "context", "Lkotlinx/coroutines/Runnable;", "block", "t0", "close", "", "toString", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class b extends Executors implements Executor {

    /* renamed from: h, reason: collision with root package name */
    public static final b f14433h = new b();

    /* renamed from: i, reason: collision with root package name */
    private static final CoroutineDispatcher f14434i;

    static {
        int c10;
        int d10;
        m mVar = m.f14453g;
        c10 = _Ranges.c(64, b0.a());
        d10 = d0.d("kotlinx.coroutines.io.parallelism", c10, 0, 0, 12, null);
        f14434i = mVar.v0(d10);
    }

    private b() {
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new IllegalStateException("Cannot be invoked on Dispatchers.IO".toString());
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        t0(qa.h.f17173e, runnable);
    }

    @Override // td.CoroutineDispatcher
    public void t0(qa.g gVar, Runnable runnable) {
        f14434i.t0(gVar, runnable);
    }

    @Override // td.CoroutineDispatcher
    public String toString() {
        return "Dispatchers.IO";
    }
}
