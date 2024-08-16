package td;

import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import ma.Unit;
import ma.p;

/* compiled from: DispatchedTask.kt */
@Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\b \u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00060\u0002j\u0002`\u0003B\u000f\u0012\u0006\u0010\u001b\u001a\u00020\u001a¢\u0006\u0004\b\u001c\u0010\u001dJ\u0011\u0010\u0005\u001a\u0004\u0018\u00010\u0004H ¢\u0006\u0004\b\u0005\u0010\u0006J!\u0010\u000b\u001a\u00020\n2\b\u0010\u0007\u001a\u0004\u0018\u00010\u00042\u0006\u0010\t\u001a\u00020\bH\u0010¢\u0006\u0004\b\u000b\u0010\fJ\u001f\u0010\u000e\u001a\u00028\u0001\"\u0004\b\u0001\u0010\u00012\b\u0010\r\u001a\u0004\u0018\u00010\u0004H\u0010¢\u0006\u0004\b\u000e\u0010\u000fJ\u001b\u0010\u0010\u001a\u0004\u0018\u00010\b2\b\u0010\r\u001a\u0004\u0018\u00010\u0004H\u0010¢\u0006\u0004\b\u0010\u0010\u0011J\u0006\u0010\u0012\u001a\u00020\nJ\u001a\u0010\u0015\u001a\u00020\n2\b\u0010\u0013\u001a\u0004\u0018\u00010\b2\b\u0010\u0014\u001a\u0004\u0018\u00010\bR\u001a\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00000\u00168 X \u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018¨\u0006\u001e"}, d2 = {"Ltd/r0;", "T", "Lkotlinx/coroutines/scheduling/h;", "Lkotlinx/coroutines/SchedulerTask;", "", "k", "()Ljava/lang/Object;", "takenState", "", "cause", "Lma/f0;", "b", "(Ljava/lang/Object;Ljava/lang/Throwable;)V", "state", "g", "(Ljava/lang/Object;)Ljava/lang/Object;", "e", "(Ljava/lang/Object;)Ljava/lang/Throwable;", "run", "exception", "finallyException", "h", "Lqa/d;", "c", "()Lqa/d;", "delegate", "", "resumeMode", "<init>", "(I)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public abstract class r0<T> extends kotlinx.coroutines.scheduling.h {

    /* renamed from: g, reason: collision with root package name */
    public int f18787g;

    public r0(int i10) {
        this.f18787g = i10;
    }

    public void b(Object takenState, Throwable cause) {
    }

    public abstract qa.d<T> c();

    public Throwable e(Object state) {
        v vVar = state instanceof v ? (v) state : null;
        if (vVar != null) {
            return vVar.f18800a;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T g(Object state) {
        return state;
    }

    public final void h(Throwable th, Throwable th2) {
        if (th == null && th2 == null) {
            return;
        }
        if (th != null && th2 != null) {
            ma.b.a(th, th2);
        }
        if (th == null) {
            th = th2;
        }
        za.k.b(th);
        f0.a(c().getF18758i(), new k0("Fatal exception in coroutines machinery for " + this + ". Please read KDoc to 'handleFatalException' method and report this incident to maintainers", th));
    }

    public abstract Object k();

    @Override // java.lang.Runnable
    public final void run() {
        Object a10;
        Object a11;
        kotlinx.coroutines.scheduling.i iVar = this.taskContext;
        try {
            kotlinx.coroutines.internal.e eVar = (kotlinx.coroutines.internal.e) c();
            qa.d<T> dVar = eVar.f14352i;
            Object obj = eVar.countOrElement;
            qa.g f18758i = dVar.getF18758i();
            Object c10 = kotlinx.coroutines.internal.e0.c(f18758i, obj);
            e2<?> g6 = c10 != kotlinx.coroutines.internal.e0.f14355a ? b0.g(dVar, f18758i, c10) : null;
            try {
                qa.g f18758i2 = dVar.getF18758i();
                Object k10 = k();
                Throwable e10 = e(k10);
                i1 i1Var = (e10 == null && s0.b(this.f18787g)) ? (i1) f18758i2.c(i1.f18746d) : null;
                if (i1Var != null && !i1Var.b()) {
                    CancellationException w10 = i1Var.w();
                    b(k10, w10);
                    p.a aVar = ma.p.f15184e;
                    dVar.resumeWith(ma.p.a(ma.q.a(w10)));
                } else if (e10 != null) {
                    p.a aVar2 = ma.p.f15184e;
                    dVar.resumeWith(ma.p.a(ma.q.a(e10)));
                } else {
                    p.a aVar3 = ma.p.f15184e;
                    dVar.resumeWith(ma.p.a(g(k10)));
                }
                Unit unit = Unit.f15173a;
                try {
                    p.a aVar4 = ma.p.f15184e;
                    iVar.a();
                    a11 = ma.p.a(unit);
                } catch (Throwable th) {
                    p.a aVar5 = ma.p.f15184e;
                    a11 = ma.p.a(ma.q.a(th));
                }
                h(null, ma.p.b(a11));
            } finally {
                if (g6 == null || g6.E0()) {
                    kotlinx.coroutines.internal.e0.a(f18758i, c10);
                }
            }
        } catch (Throwable th2) {
            try {
                p.a aVar6 = ma.p.f15184e;
                iVar.a();
                a10 = ma.p.a(Unit.f15173a);
            } catch (Throwable th3) {
                p.a aVar7 = ma.p.f15184e;
                a10 = ma.p.a(ma.q.a(th3));
            }
            h(th2, ma.p.b(a10));
        }
    }
}
