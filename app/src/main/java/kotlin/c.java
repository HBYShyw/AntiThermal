package kotlin;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlinx.coroutines.internal.InlineList;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.n;
import ma.Unit;
import td.DebugStrings;
import td.m;
import ya.l;
import za.k;

/* compiled from: AbstractChannel.kt */
@Metadata(bv = {}, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b \u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000\u0002:\u0001\u0019B)\u0012 \u00100\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\b\u0018\u00010.j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`/¢\u0006\u0004\b1\u00102J\u001b\u0010\u0006\u001a\u00020\u00052\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0003H\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u001b\u0010\t\u001a\u00020\b2\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0003H\u0002¢\u0006\u0004\b\t\u0010\nJ\u000f\u0010\f\u001a\u00020\u000bH\u0002¢\u0006\u0004\b\f\u0010\rJ\u0017\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00028\u0000H\u0014¢\u0006\u0004\b\u0010\u0010\u0011J\u0011\u0010\u0013\u001a\u0004\u0018\u00010\u0012H\u0004¢\u0006\u0004\b\u0013\u0010\u0014J\u001d\u0010\u0016\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00152\u0006\u0010\u000e\u001a\u00028\u0000H\u0004¢\u0006\u0004\b\u0016\u0010\u0017J$\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u00182\u0006\u0010\u000e\u001a\u00028\u0000ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\u0019\u0010\u0011J\u0017\u0010\u001b\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u001aH\u0014¢\u0006\u0004\b\u001b\u0010\u001cJ\u0017\u0010\u001d\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0015H\u0014¢\u0006\u0004\b\u001d\u0010\u001eJ\u000f\u0010 \u001a\u00020\u001fH\u0016¢\u0006\u0004\b \u0010!R\u0014\u0010#\u001a\u00020\u001f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\"\u0010!R\u001a\u0010%\u001a\u00020$8\u0004X\u0084\u0004¢\u0006\f\n\u0004\b%\u0010&\u001a\u0004\b'\u0010(R\u001a\u0010+\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00038DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b)\u0010*R\u0014\u0010-\u001a\u00020\u001f8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b,\u0010!\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u00063"}, d2 = {"Lvd/c;", "E", "Lvd/t;", "Lvd/j;", "closed", "", "h", "(Lvd/j;)Ljava/lang/Throwable;", "Lma/f0;", "g", "(Lvd/j;)V", "", "b", "()I", "element", "", "i", "(Ljava/lang/Object;)Ljava/lang/Object;", "Lvd/s;", "m", "()Lvd/s;", "Lvd/q;", "k", "(Ljava/lang/Object;)Lvd/q;", "Lvd/i;", "a", "Lkotlinx/coroutines/internal/n;", "j", "(Lkotlinx/coroutines/internal/n;)V", "l", "()Lvd/q;", "", "toString", "()Ljava/lang/String;", "f", "queueDebugStateString", "Lkotlinx/coroutines/internal/l;", "queue", "Lkotlinx/coroutines/internal/l;", "e", "()Lkotlinx/coroutines/internal/l;", "d", "()Lvd/j;", "closedForSend", "c", "bufferDebugString", "Lkotlin/Function1;", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "onUndeliveredElement", "<init>", "(Lya/l;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public abstract class c<E> implements t<E> {

    /* renamed from: d, reason: collision with root package name */
    private static final /* synthetic */ AtomicReferenceFieldUpdater f19281d = AtomicReferenceFieldUpdater.newUpdater(c.class, Object.class, "onCloseHandler");

    /* renamed from: b, reason: collision with root package name */
    protected final l<E, Unit> f19282b;

    /* renamed from: c, reason: collision with root package name */
    private final kotlinx.coroutines.internal.l f19283c = new kotlinx.coroutines.internal.l();
    private volatile /* synthetic */ Object onCloseHandler = null;

    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0007\b\u0000\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\u00020\u0002B\u000f\u0012\u0006\u0010\u000f\u001a\u00028\u0001¢\u0006\u0004\b\u0010\u0010\u0011J\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u0016J\b\u0010\b\u001a\u00020\u0007H\u0016J\b\u0010\n\u001a\u00020\tH\u0016R\u0016\u0010\u000e\u001a\u0004\u0018\u00010\u000b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\r¨\u0006\u0012"}, d2 = {"Lvd/c$a;", "E", "Lvd/s;", "Lkotlinx/coroutines/internal/n$b;", "otherOp", "Lkotlinx/coroutines/internal/a0;", "H", "Lma/f0;", "F", "", "toString", "", "G", "()Ljava/lang/Object;", "pollResult", "element", "<init>", "(Ljava/lang/Object;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class a<E> extends s {

        /* renamed from: h, reason: collision with root package name */
        public final E f19284h;

        public a(E e10) {
            this.f19284h = e10;
        }

        @Override // kotlin.s
        public void F() {
        }

        @Override // kotlin.s
        /* renamed from: G, reason: from getter */
        public Object getF19284h() {
            return this.f19284h;
        }

        @Override // kotlin.s
        public Symbol H(n.b otherOp) {
            return m.f18761a;
        }

        @Override // kotlinx.coroutines.internal.n
        public String toString() {
            return "SendBuffered@" + DebugStrings.b(this) + '(' + this.f19284h + ')';
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public c(l<? super E, Unit> lVar) {
        this.f19282b = lVar;
    }

    private final int b() {
        kotlinx.coroutines.internal.l lVar = this.f19283c;
        int i10 = 0;
        for (n nVar = (n) lVar.t(); !k.a(nVar, lVar); nVar = nVar.u()) {
            if (nVar instanceof n) {
                i10++;
            }
        }
        return i10;
    }

    private final String f() {
        String str;
        n u7 = this.f19283c.u();
        if (u7 == this.f19283c) {
            return "EmptyQueue";
        }
        if (u7 instanceof j) {
            str = u7.toString();
        } else if (u7 instanceof o) {
            str = "ReceiveQueued";
        } else if (u7 instanceof s) {
            str = "SendQueued";
        } else {
            str = "UNEXPECTED:" + u7;
        }
        n v7 = this.f19283c.v();
        if (v7 == u7) {
            return str;
        }
        String str2 = str + ",queueSize=" + b();
        if (!(v7 instanceof j)) {
            return str2;
        }
        return str2 + ",closedForSend=" + v7;
    }

    private final void g(j<?> closed) {
        Object b10 = InlineList.b(null, 1, null);
        while (true) {
            n v7 = closed.v();
            o oVar = v7 instanceof o ? (o) v7 : null;
            if (oVar == null) {
                break;
            } else if (!oVar.A()) {
                oVar.w();
            } else {
                b10 = InlineList.c(b10, oVar);
            }
        }
        if (b10 != null) {
            if (!(b10 instanceof ArrayList)) {
                ((o) b10).G(closed);
            } else {
                ArrayList arrayList = (ArrayList) b10;
                for (int size = arrayList.size() - 1; -1 < size; size--) {
                    ((o) arrayList.get(size)).G(closed);
                }
            }
        }
        j(closed);
    }

    private final Throwable h(j<?> closed) {
        g(closed);
        return closed.M();
    }

    @Override // kotlin.t
    public final Object a(E element) {
        Object i10 = i(element);
        if (i10 == Function1.f19276b) {
            return i.f19298a.c(Unit.f15173a);
        }
        if (i10 == Function1.f19277c) {
            j<?> d10 = d();
            return d10 == null ? i.f19298a.b() : i.f19298a.a(h(d10));
        }
        if (i10 instanceof j) {
            return i.f19298a.a(h((j) i10));
        }
        throw new IllegalStateException(("trySend returned " + i10).toString());
    }

    protected String c() {
        return "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final j<?> d() {
        n v7 = this.f19283c.v();
        j<?> jVar = v7 instanceof j ? (j) v7 : null;
        if (jVar == null) {
            return null;
        }
        g(jVar);
        return jVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: e, reason: from getter */
    public final kotlinx.coroutines.internal.l getF19283c() {
        return this.f19283c;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Object i(E element) {
        q<E> l10;
        do {
            l10 = l();
            if (l10 == null) {
                return Function1.f19277c;
            }
        } while (l10.j(element, null) == null);
        l10.i(element);
        return l10.c();
    }

    protected void j(n closed) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public final q<?> k(E element) {
        n v7;
        kotlinx.coroutines.internal.l lVar = this.f19283c;
        a aVar = new a(element);
        do {
            v7 = lVar.v();
            if (v7 instanceof q) {
                return (q) v7;
            }
        } while (!v7.o(aVar, lVar));
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [kotlinx.coroutines.internal.n] */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v3 */
    public q<E> l() {
        ?? r02;
        n C;
        kotlinx.coroutines.internal.l lVar = this.f19283c;
        while (true) {
            r02 = (n) lVar.t();
            if (r02 != lVar && (r02 instanceof q)) {
                if (((((q) r02) instanceof j) && !r02.z()) || (C = r02.C()) == null) {
                    break;
                }
                C.y();
            }
        }
        r02 = 0;
        return (q) r02;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final s m() {
        n nVar;
        n C;
        kotlinx.coroutines.internal.l lVar = this.f19283c;
        while (true) {
            nVar = (n) lVar.t();
            if (nVar != lVar && (nVar instanceof s)) {
                if (((((s) nVar) instanceof j) && !nVar.z()) || (C = nVar.C()) == null) {
                    break;
                }
                C.y();
            }
        }
        nVar = null;
        return (s) nVar;
    }

    public String toString() {
        return DebugStrings.a(this) + '@' + DebugStrings.b(this) + '{' + f() + '}' + c();
    }
}
