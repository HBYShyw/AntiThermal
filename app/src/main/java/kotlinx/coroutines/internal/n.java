package kotlinx.coroutines.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import td.DebugStrings;
import za.PropertyReference0Impl;

/* compiled from: LockFreeLinkedList.kt */
@Metadata(bv = {}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0017\u0018\u00002\u00020\u0001:\u000212B\u0007¢\u0006\u0004\b0\u0010\"J\u000f\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0004\b\u0003\u0010\u0004J \u0010\u0007\u001a\u00060\u0000j\u0002`\u00052\n\u0010\u0006\u001a\u00060\u0000j\u0002`\u0005H\u0082\u0010¢\u0006\u0004\b\u0007\u0010\bJ\u001b\u0010\u000b\u001a\u00020\n2\n\u0010\t\u001a\u00060\u0000j\u0002`\u0005H\u0002¢\u0006\u0004\b\u000b\u0010\fJ\"\u0010\u000f\u001a\n\u0018\u00010\u0000j\u0004\u0018\u0001`\u00052\b\u0010\u000e\u001a\u0004\u0018\u00010\rH\u0082\u0010¢\u0006\u0004\b\u000f\u0010\u0010J\u0019\u0010\u0013\u001a\u00020\u00122\n\u0010\u0011\u001a\u00060\u0000j\u0002`\u0005¢\u0006\u0004\b\u0013\u0010\u0014J\u0019\u0010\u0015\u001a\u00020\n2\n\u0010\u0011\u001a\u00060\u0000j\u0002`\u0005¢\u0006\u0004\b\u0015\u0010\fJ'\u0010\u0016\u001a\u00020\u00122\n\u0010\u0011\u001a\u00060\u0000j\u0002`\u00052\n\u0010\t\u001a\u00060\u0000j\u0002`\u0005H\u0001¢\u0006\u0004\b\u0016\u0010\u0017J/\u0010\u001b\u001a\u00020\u001a2\n\u0010\u0011\u001a\u00060\u0000j\u0002`\u00052\n\u0010\t\u001a\u00060\u0000j\u0002`\u00052\u0006\u0010\u0019\u001a\u00020\u0018H\u0001¢\u0006\u0004\b\u001b\u0010\u001cJ\u000f\u0010\u001d\u001a\u00020\u0012H\u0016¢\u0006\u0004\b\u001d\u0010\u001eJ\u0017\u0010\u001f\u001a\n\u0018\u00010\u0000j\u0004\u0018\u0001`\u0005H\u0001¢\u0006\u0004\b\u001f\u0010 J\r\u0010!\u001a\u00020\n¢\u0006\u0004\b!\u0010\"J\u000f\u0010#\u001a\u00020\nH\u0001¢\u0006\u0004\b#\u0010\"J\u0015\u0010$\u001a\n\u0018\u00010\u0000j\u0004\u0018\u0001`\u0005¢\u0006\u0004\b$\u0010 J\u000f\u0010&\u001a\u00020%H\u0016¢\u0006\u0004\b&\u0010'R\u0014\u0010)\u001a\u00020\u00128VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b(\u0010\u001eR\u0011\u0010\t\u001a\u00020\u00018F¢\u0006\u0006\u001a\u0004\b*\u0010+R\u0015\u0010-\u001a\u00060\u0000j\u0002`\u00058F¢\u0006\u0006\u001a\u0004\b,\u0010 R\u0015\u0010/\u001a\u00060\u0000j\u0002`\u00058F¢\u0006\u0006\u001a\u0004\b.\u0010 ¨\u00063"}, d2 = {"Lkotlinx/coroutines/internal/n;", "", "Lkotlinx/coroutines/internal/w;", "D", "()Lkotlinx/coroutines/internal/w;", "Lkotlinx/coroutines/internal/Node;", "current", "r", "(Lkotlinx/coroutines/internal/n;)Lkotlinx/coroutines/internal/n;", "next", "Lma/f0;", "s", "(Lkotlinx/coroutines/internal/n;)V", "Lkotlinx/coroutines/internal/v;", "op", "q", "(Lkotlinx/coroutines/internal/v;)Lkotlinx/coroutines/internal/n;", "node", "", "p", "(Lkotlinx/coroutines/internal/n;)Z", "n", "o", "(Lkotlinx/coroutines/internal/n;Lkotlinx/coroutines/internal/n;)Z", "Lkotlinx/coroutines/internal/n$a;", "condAdd", "", "E", "(Lkotlinx/coroutines/internal/n;Lkotlinx/coroutines/internal/n;Lkotlinx/coroutines/internal/n$a;)I", "A", "()Z", "C", "()Lkotlinx/coroutines/internal/n;", "w", "()V", "y", "B", "", "toString", "()Ljava/lang/String;", "z", "isRemoved", "t", "()Ljava/lang/Object;", "u", "nextNode", "v", "prevNode", "<init>", "a", "b", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public class n {

    /* renamed from: e, reason: collision with root package name */
    static final /* synthetic */ AtomicReferenceFieldUpdater f14378e = AtomicReferenceFieldUpdater.newUpdater(n.class, Object.class, "_next");

    /* renamed from: f, reason: collision with root package name */
    static final /* synthetic */ AtomicReferenceFieldUpdater f14379f = AtomicReferenceFieldUpdater.newUpdater(n.class, Object.class, "_prev");

    /* renamed from: g, reason: collision with root package name */
    private static final /* synthetic */ AtomicReferenceFieldUpdater f14380g = AtomicReferenceFieldUpdater.newUpdater(n.class, Object.class, "_removedRef");
    volatile /* synthetic */ Object _next = this;
    volatile /* synthetic */ Object _prev = this;
    private volatile /* synthetic */ Object _removedRef = null;

    /* compiled from: LockFreeLinkedList.kt */
    @Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b!\u0018\u00002\f\u0012\b\u0012\u00060\u0002j\u0002`\u00030\u0001B\u0013\u0012\n\u0010\u000b\u001a\u00060\u0002j\u0002`\u0003¢\u0006\u0004\b\u000e\u0010\u000fJ\u001e\u0010\b\u001a\u00020\u00072\n\u0010\u0004\u001a\u00060\u0002j\u0002`\u00032\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0016R\u0018\u0010\u000b\u001a\u00060\u0002j\u0002`\u00038\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\t\u0010\nR\u001e\u0010\r\u001a\n\u0018\u00010\u0002j\u0004\u0018\u0001`\u00038\u0006@\u0006X\u0087\u000e¢\u0006\u0006\n\u0004\b\f\u0010\n¨\u0006\u0010"}, d2 = {"Lkotlinx/coroutines/internal/n$a;", "Lkotlinx/coroutines/internal/c;", "Lkotlinx/coroutines/internal/n;", "Lkotlinx/coroutines/internal/Node;", "affected", "", "failure", "Lma/f0;", "h", "b", "Lkotlinx/coroutines/internal/n;", "newNode", "c", "oldNext", "<init>", "(Lkotlinx/coroutines/internal/n;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static abstract class a extends kotlinx.coroutines.internal.c<n> {

        /* renamed from: b, reason: collision with root package name and from kotlin metadata */
        public final n newNode;

        /* renamed from: c, reason: collision with root package name and from kotlin metadata */
        public n oldNext;

        public a(n nVar) {
            this.newNode = nVar;
        }

        @Override // kotlinx.coroutines.internal.c
        /* renamed from: h, reason: merged with bridge method [inline-methods] */
        public void d(n nVar, Object obj) {
            boolean z10 = obj == null;
            n nVar2 = z10 ? this.newNode : this.oldNext;
            if (nVar2 != null && n.f14378e.compareAndSet(nVar, this, nVar2) && z10) {
                n nVar3 = this.newNode;
                n nVar4 = this.oldNext;
                za.k.b(nVar4);
                nVar3.s(nVar4);
            }
        }
    }

    /* compiled from: LockFreeLinkedList.kt */
    @Metadata(bv = {}, d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001¨\u0006\u0002"}, d2 = {"Lkotlinx/coroutines/internal/n$b;", "Lkotlinx/coroutines/internal/v;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class b extends v {
    }

    private final w D() {
        w wVar = (w) this._removedRef;
        if (wVar != null) {
            return wVar;
        }
        w wVar2 = new w(this);
        f14380g.lazySet(this, wVar2);
        return wVar2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0048, code lost:
    
        if (kotlinx.coroutines.internal.n.f14378e.compareAndSet(r3, r2, ((kotlinx.coroutines.internal.w) r4).f14402a) != false) goto L30;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final n q(v op) {
        while (true) {
            n nVar = (n) this._prev;
            n nVar2 = nVar;
            while (true) {
                n nVar3 = null;
                while (true) {
                    Object obj = nVar2._next;
                    if (obj == this) {
                        if (nVar == nVar2 || f14379f.compareAndSet(this, nVar, nVar2)) {
                            return nVar2;
                        }
                    } else {
                        if (z()) {
                            return null;
                        }
                        if (obj == op) {
                            return nVar2;
                        }
                        if (obj instanceof v) {
                            if (op != null && op.b((v) obj)) {
                                return null;
                            }
                            ((v) obj).c(nVar2);
                        } else if (!(obj instanceof w)) {
                            nVar3 = nVar2;
                            nVar2 = (n) obj;
                        } else {
                            if (nVar3 != null) {
                                break;
                            }
                            nVar2 = (n) nVar2._prev;
                        }
                    }
                }
                nVar2 = nVar3;
            }
        }
    }

    private final n r(n current) {
        while (current.z()) {
            current = (n) current._prev;
        }
        return current;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void s(n next) {
        n nVar;
        do {
            nVar = (n) next._prev;
            if (t() != next) {
                return;
            }
        } while (!f14379f.compareAndSet(next, nVar, this));
        if (z()) {
            next.q(null);
        }
    }

    public boolean A() {
        return C() == null;
    }

    public final n B() {
        while (true) {
            n nVar = (n) t();
            if (nVar == this) {
                return null;
            }
            if (nVar.A()) {
                return nVar;
            }
            nVar.w();
        }
    }

    public final n C() {
        Object t7;
        n nVar;
        do {
            t7 = t();
            if (t7 instanceof w) {
                return ((w) t7).ref;
            }
            if (t7 == this) {
                return (n) t7;
            }
            nVar = (n) t7;
        } while (!f14378e.compareAndSet(this, t7, nVar.D()));
        nVar.q(null);
        return null;
    }

    public final int E(n node, n next, a condAdd) {
        f14379f.lazySet(node, this);
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = f14378e;
        atomicReferenceFieldUpdater.lazySet(node, next);
        condAdd.oldNext = next;
        if (atomicReferenceFieldUpdater.compareAndSet(this, next, condAdd)) {
            return condAdd.c(this) == null ? 1 : 2;
        }
        return 0;
    }

    public final void n(n node) {
        do {
        } while (!v().o(node, this));
    }

    public final boolean o(n node, n next) {
        f14379f.lazySet(node, this);
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = f14378e;
        atomicReferenceFieldUpdater.lazySet(node, next);
        if (!atomicReferenceFieldUpdater.compareAndSet(this, next, node)) {
            return false;
        }
        node.s(next);
        return true;
    }

    public final boolean p(n node) {
        f14379f.lazySet(node, this);
        f14378e.lazySet(node, this);
        while (t() == this) {
            if (f14378e.compareAndSet(this, this, node)) {
                node.s(this);
                return true;
            }
        }
        return false;
    }

    public final Object t() {
        while (true) {
            Object obj = this._next;
            if (!(obj instanceof v)) {
                return obj;
            }
            ((v) obj).c(this);
        }
    }

    public String toString() {
        return new PropertyReference0Impl(this) { // from class: kotlinx.coroutines.internal.n.c
            @Override // za.PropertyReference0Impl, gb.m
            public Object get() {
                return DebugStrings.a(this.f20351f);
            }
        } + '@' + DebugStrings.b(this);
    }

    public final n u() {
        return m.b(t());
    }

    public final n v() {
        n q10 = q(null);
        return q10 == null ? r((n) this._prev) : q10;
    }

    public final void w() {
        ((w) t()).ref.y();
    }

    public final void y() {
        while (true) {
            Object t7 = this.t();
            if (t7 instanceof w) {
                this = ((w) t7).ref;
            } else {
                this.q(null);
                return;
            }
        }
    }

    public boolean z() {
        return t() instanceof w;
    }
}
