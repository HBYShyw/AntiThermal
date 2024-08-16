package td;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import kotlin.Metadata;
import ma.Unit;
import ma.p;
import rd.Sequence;
import rd._Sequences;

/* compiled from: CoroutineExceptionHandlerImpl.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0018\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0002H\u0000¨\u0006\u0006"}, d2 = {"Lqa/g;", "context", "", "exception", "Lma/f0;", "a", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class e0 {

    /* renamed from: a, reason: collision with root package name */
    private static final List<d0> f18733a;

    static {
        Sequence a10;
        List<d0> C;
        a10 = rd.l.a(ServiceLoader.load(d0.class, d0.class.getClassLoader()).iterator());
        C = _Sequences.C(a10);
        f18733a = C;
    }

    public static final void a(qa.g gVar, Throwable th) {
        Iterator<d0> it = f18733a.iterator();
        while (it.hasNext()) {
            try {
                it.next().N(gVar, th);
            } catch (Throwable th2) {
                Thread currentThread = Thread.currentThread();
                currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, f0.b(th, th2));
            }
        }
        Thread currentThread2 = Thread.currentThread();
        try {
            p.a aVar = ma.p.f15184e;
            ma.b.a(th, new p0(gVar));
            ma.p.a(Unit.f15173a);
        } catch (Throwable th3) {
            p.a aVar2 = ma.p.f15184e;
            ma.p.a(ma.q.a(th3));
        }
        currentThread2.getUncaughtExceptionHandler().uncaughtException(currentThread2, th);
    }
}
