package kotlin;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import kotlinx.coroutines.internal.StackTraceRecovery;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.n;
import kotlinx.coroutines.internal.u;
import ma.Unit;
import ma.p;
import ma.q;
import ra.IntrinsicsJvm;
import sa.boxing;
import sa.h;
import td.DebugStrings;
import td.e;
import td.k;
import td.l;
import td.m;
import td.n;

/* compiled from: AbstractChannel.kt */
@Metadata(bv = {}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b \u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000\u00022\b\u0012\u0004\u0012\u00028\u00000\u0003:\u0003\u001f !B)\u0012 \u0010\u001c\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\n\u0018\u00010\u001aj\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\u001b¢\u0006\u0004\b\u001d\u0010\u001eJ\u0016\u0010\u0007\u001a\u00020\u00062\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0002J \u0010\u000b\u001a\u00020\n2\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\b2\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0002J\n\u0010\r\u001a\u0004\u0018\u00010\fH\u0014J\u0016\u0010\u000e\u001a\u00020\u00062\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0014J\u000f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u000fH\u0086\u0002J\u0010\u0010\u0012\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0011H\u0014J\b\u0010\u0013\u001a\u00020\nH\u0014J\b\u0010\u0014\u001a\u00020\nH\u0014R\u0014\u0010\u0017\u001a\u00020\u00068$X¤\u0004¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0019\u001a\u00020\u00068$X¤\u0004¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0016¨\u0006\""}, d2 = {"Lvd/a;", "E", "Lvd/c;", "Lvd/f;", "Lvd/o;", "receive", "", "p", "Ltd/k;", "cont", "Lma/f0;", "w", "", "v", "q", "Lvd/g;", "iterator", "Lvd/q;", "l", "u", "t", "r", "()Z", "isBufferAlwaysEmpty", "s", "isBufferEmpty", "Lkotlin/Function1;", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "onUndeliveredElement", "<init>", "(Lya/l;)V", "a", "b", "c", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public abstract class a<E> extends kotlin.c<E> implements f<E> {

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u00028\u00010\u0002B\u0015\u0012\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00028\u00010\u0010¢\u0006\u0004\b\u0012\u0010\u0013J\u0012\u0010\u0006\u001a\u00020\u00052\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u0002J\u0013\u0010\u0007\u001a\u00020\u0005H\u0082@ø\u0001\u0000¢\u0006\u0004\b\u0007\u0010\bJ\u0013\u0010\t\u001a\u00020\u0005H\u0096Bø\u0001\u0000¢\u0006\u0004\b\t\u0010\bJ\u0010\u0010\n\u001a\u00028\u0001H\u0096\u0002¢\u0006\u0004\b\n\u0010\u000bR$\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0004\u0010\f\u001a\u0004\b\r\u0010\u000b\"\u0004\b\u000e\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0014"}, d2 = {"Lvd/a$a;", "E", "Lvd/g;", "", "result", "", "b", "c", "(Lqa/d;)Ljava/lang/Object;", "a", "next", "()Ljava/lang/Object;", "Ljava/lang/Object;", "getResult", "d", "(Ljava/lang/Object;)V", "Lvd/a;", "channel", "<init>", "(Lvd/a;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* renamed from: vd.a$a, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public static final class C0112a<E> implements g<E> {

        /* renamed from: a, reason: collision with root package name */
        public final a<E> f19268a;

        /* renamed from: b, reason: collision with root package name */
        private Object f19269b = Function1.f19278d;

        public C0112a(a<E> aVar) {
            this.f19268a = aVar;
        }

        private final boolean b(Object result) {
            if (!(result instanceof j)) {
                return true;
            }
            j jVar = (j) result;
            if (jVar.f19301h == null) {
                return false;
            }
            throw StackTraceRecovery.a(jVar.L());
        }

        private final Object c(qa.d<? super Boolean> dVar) {
            qa.d b10;
            Object c10;
            b10 = IntrinsicsJvm.b(dVar);
            l a10 = n.a(b10);
            b bVar = new b(this, a10);
            while (true) {
                if (this.f19268a.p(bVar)) {
                    this.f19268a.w(a10, bVar);
                    break;
                }
                Object v7 = this.f19268a.v();
                d(v7);
                if (v7 instanceof j) {
                    j jVar = (j) v7;
                    if (jVar.f19301h == null) {
                        p.a aVar = p.f15184e;
                        a10.resumeWith(p.a(boxing.a(false)));
                    } else {
                        p.a aVar2 = p.f15184e;
                        a10.resumeWith(p.a(q.a(jVar.L())));
                    }
                } else if (v7 != Function1.f19278d) {
                    Boolean a11 = boxing.a(true);
                    ya.l<E, Unit> lVar = this.f19268a.f19282b;
                    a10.d(a11, lVar != null ? u.a(lVar, v7, a10.getF18758i()) : null);
                }
            }
            Object w10 = a10.w();
            c10 = ra.d.c();
            if (w10 == c10) {
                h.c(dVar);
            }
            return w10;
        }

        @Override // kotlin.g
        public Object a(qa.d<? super Boolean> dVar) {
            Object obj = this.f19269b;
            Symbol symbol = Function1.f19278d;
            if (obj != symbol) {
                return boxing.a(b(obj));
            }
            Object v7 = this.f19268a.v();
            this.f19269b = v7;
            if (v7 != symbol) {
                return boxing.a(b(v7));
            }
            return c(dVar);
        }

        public final void d(Object obj) {
            this.f19269b = obj;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.g
        public E next() {
            E e10 = (E) this.f19269b;
            if (!(e10 instanceof j)) {
                Symbol symbol = Function1.f19278d;
                if (e10 != symbol) {
                    this.f19269b = symbol;
                    return e10;
                }
                throw new IllegalStateException("'hasNext' should be called prior to 'next' invocation");
            }
            throw StackTraceRecovery.a(((j) e10).L());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {}, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0012\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u00028\u00010\u0002B#\u0012\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00028\u00010\u0015\u0012\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017¢\u0006\u0004\b\u001a\u0010\u001bJ#\u0010\u0007\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0003\u001a\u00028\u00012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0016¢\u0006\u0004\b\u0007\u0010\bJ\u0017\u0010\n\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00028\u0001H\u0016¢\u0006\u0004\b\n\u0010\u000bJ\u0014\u0010\u000e\u001a\u00020\t2\n\u0010\r\u001a\u0006\u0012\u0002\b\u00030\fH\u0016J%\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\t\u0018\u00010\u000f2\u0006\u0010\u0003\u001a\u00028\u0001H\u0016¢\u0006\u0004\b\u0011\u0010\u0012J\b\u0010\u0014\u001a\u00020\u0013H\u0016¨\u0006\u001c"}, d2 = {"Lvd/a$b;", "E", "Lvd/o;", ThermalBaseConfig.Item.ATTR_VALUE, "Lkotlinx/coroutines/internal/n$b;", "otherOp", "Lkotlinx/coroutines/internal/a0;", "j", "(Ljava/lang/Object;Lkotlinx/coroutines/internal/n$b;)Lkotlinx/coroutines/internal/a0;", "Lma/f0;", "i", "(Ljava/lang/Object;)V", "Lvd/j;", "closed", "G", "Lkotlin/Function1;", "", "H", "(Ljava/lang/Object;)Lya/l;", "", "toString", "Lvd/a$a;", "iterator", "Ltd/k;", "", "cont", "<init>", "(Lvd/a$a;Ltd/k;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static class b<E> extends o<E> {

        /* renamed from: h, reason: collision with root package name */
        public final C0112a<E> f19270h;

        /* renamed from: i, reason: collision with root package name */
        public final k<Boolean> f19271i;

        /* JADX WARN: Multi-variable type inference failed */
        public b(C0112a<E> c0112a, k<? super Boolean> kVar) {
            this.f19270h = c0112a;
            this.f19271i = kVar;
        }

        @Override // kotlin.o
        public void G(j<?> jVar) {
            Object i10;
            if (jVar.f19301h == null) {
                i10 = k.a.a(this.f19271i, Boolean.FALSE, null, 2, null);
            } else {
                i10 = this.f19271i.i(jVar.L());
            }
            if (i10 != null) {
                this.f19270h.d(jVar);
                this.f19271i.l(i10);
            }
        }

        public ya.l<Throwable, Unit> H(E value) {
            ya.l<E, Unit> lVar = this.f19270h.f19268a.f19282b;
            if (lVar != null) {
                return u.a(lVar, value, this.f19271i.getF18758i());
            }
            return null;
        }

        @Override // kotlin.q
        public void i(E value) {
            this.f19270h.d(value);
            this.f19271i.l(m.f18761a);
        }

        @Override // kotlin.q
        public Symbol j(E value, n.b otherOp) {
            if (this.f19271i.j(Boolean.TRUE, null, H(value)) == null) {
                return null;
            }
            return m.f18761a;
        }

        @Override // kotlinx.coroutines.internal.n
        public String toString() {
            return "ReceiveHasNext@" + DebugStrings.b(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0013\u0012\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\b¢\u0006\u0004\b\n\u0010\u000bJ\u0013\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0016¨\u0006\f"}, d2 = {"Lvd/a$c;", "Ltd/e;", "", "cause", "Lma/f0;", "a", "", "toString", "Lvd/o;", "receive", "<init>", "(Lvd/a;Lvd/o;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public final class c extends e {

        /* renamed from: e, reason: collision with root package name */
        private final o<?> f19272e;

        public c(o<?> oVar) {
            this.f19272e = oVar;
        }

        @Override // td.j
        public void a(Throwable th) {
            if (this.f19272e.A()) {
                a.this.t();
            }
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
            a(th);
            return Unit.f15173a;
        }

        public String toString() {
            return "RemoveReceiveOnCancel[" + this.f19272e + ']';
        }
    }

    /* compiled from: LockFreeLinkedList.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0006\u001a\u0004\u0018\u00010\u00052\n\u0010\u0004\u001a\u00060\u0002j\u0002`\u0003H\u0016¨\u0006\u0007"}, d2 = {"vd/a$d", "Lkotlinx/coroutines/internal/n$a;", "Lkotlinx/coroutines/internal/n;", "Lkotlinx/coroutines/internal/Node;", "affected", "", "i", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class d extends n.a {

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ a f19274d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public d(kotlinx.coroutines.internal.n nVar, a aVar) {
            super(nVar);
            this.f19274d = aVar;
        }

        @Override // kotlinx.coroutines.internal.c
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public Object g(kotlinx.coroutines.internal.n affected) {
            if (this.f19274d.s()) {
                return null;
            }
            return kotlinx.coroutines.internal.m.a();
        }
    }

    public a(ya.l<? super E, Unit> lVar) {
        super(lVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean p(o<? super E> receive) {
        boolean q10 = q(receive);
        if (q10) {
            u();
        }
        return q10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void w(k<?> kVar, o<?> oVar) {
        kVar.f(new c(oVar));
    }

    @Override // kotlin.p
    public final g<E> iterator() {
        return new C0112a(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlin.c
    public q<E> l() {
        q<E> l10 = super.l();
        if (l10 != null && !(l10 instanceof j)) {
            t();
        }
        return l10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean q(o<? super E> receive) {
        int E;
        kotlinx.coroutines.internal.n v7;
        if (r()) {
            kotlinx.coroutines.internal.n f19283c = getF19283c();
            do {
                v7 = f19283c.v();
                if (!(!(v7 instanceof s))) {
                    return false;
                }
            } while (!v7.o(receive, f19283c));
        } else {
            kotlinx.coroutines.internal.n f19283c2 = getF19283c();
            d dVar = new d(receive, this);
            do {
                kotlinx.coroutines.internal.n v10 = f19283c2.v();
                if (!(!(v10 instanceof s))) {
                    return false;
                }
                E = v10.E(receive, f19283c2, dVar);
                if (E != 1) {
                }
            } while (E != 2);
            return false;
        }
        return true;
    }

    protected abstract boolean r();

    protected abstract boolean s();

    protected void t() {
    }

    protected void u() {
    }

    protected Object v() {
        while (true) {
            s m10 = m();
            if (m10 == null) {
                return Function1.f19278d;
            }
            if (m10.H(null) != null) {
                m10.F();
                return m10.getF19284h();
            }
            m10.I();
        }
    }
}
