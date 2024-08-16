package td;

import kotlin.Metadata;

/* compiled from: EventLoop.common.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\bÁ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000b\u0010\u0004J\u000f\u0010\u0003\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\u0003\u0010\u0004J\u0017\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u0005H\u0000¢\u0006\u0004\b\u0007\u0010\bR\u0014\u0010\u0006\u001a\u00020\u00058@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006\f"}, d2 = {"Ltd/c2;", "", "Lma/f0;", "b", "()V", "Ltd/w0;", "eventLoop", "c", "(Ltd/w0;)V", "a", "()Ltd/w0;", "<init>", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class c2 {

    /* renamed from: a, reason: collision with root package name */
    public static final c2 f18727a = new c2();

    /* renamed from: b, reason: collision with root package name */
    private static final ThreadLocal<w0> f18728b = new ThreadLocal<>();

    private c2() {
    }

    public final w0 a() {
        ThreadLocal<w0> threadLocal = f18728b;
        w0 w0Var = threadLocal.get();
        if (w0Var != null) {
            return w0Var;
        }
        w0 a10 = z0.a();
        threadLocal.set(a10);
        return a10;
    }

    public final void b() {
        f18728b.set(null);
    }

    public final void c(w0 eventLoop) {
        f18728b.set(eventLoop);
    }
}
