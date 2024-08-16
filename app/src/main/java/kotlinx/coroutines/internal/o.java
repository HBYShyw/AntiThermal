package kotlinx.coroutines.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;

/* compiled from: LockFreeTaskQueue.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0007\b\u0010\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u00012\u00020\u0001B\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0007¢\u0006\u0004\b\u0011\u0010\u0012J\r\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0015\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00028\u0000¢\u0006\u0004\b\b\u0010\tJ\u000f\u0010\n\u001a\u0004\u0018\u00018\u0000¢\u0006\u0004\b\n\u0010\u000bR\u0011\u0010\u000f\u001a\u00020\f8F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/internal/o;", "", "E", "Lma/f0;", "b", "()V", "element", "", "a", "(Ljava/lang/Object;)Z", "d", "()Ljava/lang/Object;", "", "c", "()I", "size", "singleConsumer", "<init>", "(Z)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public class o<E> {

    /* renamed from: a, reason: collision with root package name */
    private static final /* synthetic */ AtomicReferenceFieldUpdater f14383a = AtomicReferenceFieldUpdater.newUpdater(o.class, Object.class, "_cur");
    private volatile /* synthetic */ Object _cur;

    public o(boolean z10) {
        this._cur = new p(8, z10);
    }

    public final boolean a(E element) {
        while (true) {
            p pVar = (p) this._cur;
            int a10 = pVar.a(element);
            if (a10 == 0) {
                return true;
            }
            if (a10 == 1) {
                f14383a.compareAndSet(this, pVar, pVar.i());
            } else if (a10 == 2) {
                return false;
            }
        }
    }

    public final void b() {
        while (true) {
            p pVar = (p) this._cur;
            if (pVar.d()) {
                return;
            } else {
                f14383a.compareAndSet(this, pVar, pVar.i());
            }
        }
    }

    public final int c() {
        return ((p) this._cur).f();
    }

    public final E d() {
        while (true) {
            p pVar = (p) this._cur;
            E e10 = (E) pVar.j();
            if (e10 != p.f14387h) {
                return e10;
            }
            f14383a.compareAndSet(this, pVar, pVar.i());
        }
    }
}
