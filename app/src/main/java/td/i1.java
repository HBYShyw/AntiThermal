package td;

import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import ma.Unit;
import qa.g;

/* compiled from: Job.kt */
@Metadata(bv = {}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001:\u0001\u0016J\f\u0010\u0004\u001a\u00060\u0002j\u0002`\u0003H'J\b\u0010\u0006\u001a\u00020\u0005H&J\u001a\u0010\t\u001a\u00020\b2\u0010\b\u0002\u0010\u0007\u001a\n\u0018\u00010\u0002j\u0004\u0018\u0001`\u0003H&J\u0010\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\nH'J6\u0010\u0015\u001a\u00020\u00142\b\b\u0002\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u00052\u0018\u0010\u0013\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u0011\u0012\u0004\u0012\u00020\b0\u0010j\u0002`\u0012H'R\u0014\u0010\u0018\u001a\u00020\u00058&X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017¨\u0006\u0019"}, d2 = {"Ltd/i1;", "Lqa/g$b;", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "w", "", "start", "cause", "Lma/f0;", "m0", "Ltd/s;", "child", "Ltd/q;", "U", "onCancelling", "invokeImmediately", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/CompletionHandler;", "handler", "Ltd/u0;", "v", "b", "()Z", "isActive", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public interface i1 extends g.b {

    /* renamed from: d, reason: collision with root package name */
    public static final b f18746d = b.f18747e;

    /* compiled from: Job.kt */
    @Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes2.dex */
    public static final class a {
        public static /* synthetic */ void a(i1 i1Var, CancellationException cancellationException, int i10, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: cancel");
            }
            if ((i10 & 1) != 0) {
                cancellationException = null;
            }
            i1Var.m0(cancellationException);
        }

        public static <R> R b(i1 i1Var, R r10, ya.p<? super R, ? super g.b, ? extends R> pVar) {
            return (R) g.b.a.a(i1Var, r10, pVar);
        }

        public static <E extends g.b> E c(i1 i1Var, g.c<E> cVar) {
            return (E) g.b.a.b(i1Var, cVar);
        }

        public static /* synthetic */ u0 d(i1 i1Var, boolean z10, boolean z11, ya.l lVar, int i10, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: invokeOnCompletion");
            }
            if ((i10 & 1) != 0) {
                z10 = false;
            }
            if ((i10 & 2) != 0) {
                z11 = true;
            }
            return i1Var.v(z10, z11, lVar);
        }

        public static qa.g e(i1 i1Var, g.c<?> cVar) {
            return g.b.a.c(i1Var, cVar);
        }

        public static qa.g f(i1 i1Var, qa.g gVar) {
            return g.b.a.d(i1Var, gVar);
        }
    }

    /* compiled from: Job.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0003\u0010\u0004¨\u0006\u0005"}, d2 = {"Ltd/i1$b;", "Lqa/g$c;", "Ltd/i1;", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class b implements g.c<i1> {

        /* renamed from: e, reason: collision with root package name */
        static final /* synthetic */ b f18747e = new b();

        private b() {
        }
    }

    q U(s child);

    boolean b();

    void m0(CancellationException cancellationException);

    boolean start();

    u0 v(boolean z10, boolean z11, ya.l<? super Throwable, Unit> lVar);

    CancellationException w();
}
