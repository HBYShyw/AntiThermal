package kotlinx.coroutines.internal;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import kotlin.Metadata;
import rd.Sequence;
import rd._Sequences;
import td.MainCoroutineDispatcher;

/* compiled from: MainDispatchers.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\b\u0010\tJ\b\u0010\u0003\u001a\u00020\u0002H\u0002R\u0014\u0010\u0007\u001a\u00020\u00048\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010\u0006¨\u0006\n"}, d2 = {"Lkotlinx/coroutines/internal/r;", "", "Ltd/s1;", "a", "", "b", "Z", "FAST_SERVICE_LOADER_ENABLED", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class r {

    /* renamed from: a, reason: collision with root package name */
    public static final r f14393a;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private static final boolean FAST_SERVICE_LOADER_ENABLED;

    /* renamed from: c, reason: collision with root package name */
    public static final MainCoroutineDispatcher f14395c;

    static {
        r rVar = new r();
        f14393a = rVar;
        FAST_SERVICE_LOADER_ENABLED = b0.e("kotlinx.coroutines.fast.service.loader", true);
        f14395c = rVar.a();
    }

    private r() {
    }

    private final MainCoroutineDispatcher a() {
        Sequence a10;
        List<MainDispatcherFactory> C;
        Object next;
        MainCoroutineDispatcher e10;
        try {
            if (FAST_SERVICE_LOADER_ENABLED) {
                C = g.f14365a.c();
            } else {
                a10 = rd.l.a(ServiceLoader.load(MainDispatcherFactory.class, MainDispatcherFactory.class.getClassLoader()).iterator());
                C = _Sequences.C(a10);
            }
            Iterator<T> it = C.iterator();
            if (it.hasNext()) {
                next = it.next();
                if (it.hasNext()) {
                    int c10 = ((MainDispatcherFactory) next).c();
                    do {
                        Object next2 = it.next();
                        int c11 = ((MainDispatcherFactory) next2).c();
                        if (c10 < c11) {
                            next = next2;
                            c10 = c11;
                        }
                    } while (it.hasNext());
                }
            } else {
                next = null;
            }
            MainDispatcherFactory mainDispatcherFactory = (MainDispatcherFactory) next;
            return (mainDispatcherFactory == null || (e10 = s.e(mainDispatcherFactory, C)) == null) ? s.b(null, null, 3, null) : e10;
        } catch (Throwable th) {
            return s.b(th, null, 2, null);
        }
    }
}
