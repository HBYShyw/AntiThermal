package kotlinx.coroutines.sync;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.n;
import kotlinx.coroutines.internal.v;
import ma.Unit;
import ra.IntrinsicsJvm;
import sa.h;
import td.k;
import td.m;
import td.u0;
import ya.l;
import za.Lambda;

/* compiled from: Mutex.kt */
@Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0000\u0018\u00002\u00020\u00012\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\u0004\u0012\u00020\u00010\u0002:\u0004\u000b\n\u0005\bB\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0007¢\u0006\u0004\b\u0011\u0010\u0012J\u001d\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0082@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006J\u0019\u0010\b\u001a\u00020\u00072\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016¢\u0006\u0004\b\b\u0010\tJ\u001d\u0010\n\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0096@ø\u0001\u0000¢\u0006\u0004\b\n\u0010\u0006J\u0019\u0010\u000b\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016¢\u0006\u0004\b\u000b\u0010\fJ\u000f\u0010\u000e\u001a\u00020\rH\u0016¢\u0006\u0004\b\u000e\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/sync/c;", "Lkotlinx/coroutines/sync/b;", "", "owner", "Lma/f0;", "c", "(Ljava/lang/Object;Lqa/d;)Ljava/lang/Object;", "", "d", "(Ljava/lang/Object;)Z", "b", "a", "(Ljava/lang/Object;)V", "", "toString", "()Ljava/lang/String;", "locked", "<init>", "(Z)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class c implements kotlinx.coroutines.sync.b {

    /* renamed from: a, reason: collision with root package name */
    static final /* synthetic */ AtomicReferenceFieldUpdater f14460a = AtomicReferenceFieldUpdater.newUpdater(c.class, Object.class, "_state");
    volatile /* synthetic */ Object _state;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Mutex.kt */
    @Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0082\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\u001f\u0012\b\u0010\n\u001a\u0004\u0018\u00010\t\u0012\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b¢\u0006\u0004\b\r\u0010\u000eJ\b\u0010\u0004\u001a\u00020\u0003H\u0016J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\b\u0010\b\u001a\u00020\u0007H\u0016¨\u0006\u000f"}, d2 = {"Lkotlinx/coroutines/sync/c$a;", "Lkotlinx/coroutines/sync/c$b;", "Lkotlinx/coroutines/sync/c;", "", "H", "Lma/f0;", "F", "", "toString", "", "owner", "Ltd/k;", "cont", "<init>", "(Lkotlinx/coroutines/sync/c;Ljava/lang/Object;Ltd/k;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public final class a extends b {

        /* renamed from: k, reason: collision with root package name */
        private final k<Unit> f14461k;

        /* compiled from: Mutex.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"", "it", "Lma/f0;", "a", "(Ljava/lang/Throwable;)V"}, k = 3, mv = {1, 6, 0})
        /* renamed from: kotlinx.coroutines.sync.c$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0071a extends Lambda implements l<Throwable, Unit> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ c f14463e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ a f14464f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C0071a(c cVar, a aVar) {
                super(1);
                this.f14463e = cVar;
                this.f14464f = aVar;
            }

            public final void a(Throwable th) {
                this.f14463e.a(this.f14464f.owner);
            }

            @Override // ya.l
            public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                a(th);
                return Unit.f15173a;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public a(Object obj, k<? super Unit> kVar) {
            super(obj);
            this.f14461k = kVar;
        }

        @Override // kotlinx.coroutines.sync.c.b
        public void F() {
            this.f14461k.l(m.f18761a);
        }

        @Override // kotlinx.coroutines.sync.c.b
        public boolean H() {
            return G() && this.f14461k.j(Unit.f15173a, null, new C0071a(c.this, this)) != null;
        }

        @Override // kotlinx.coroutines.internal.n
        public String toString() {
            return "LockCont[" + this.owner + ", " + this.f14461k + "] for " + c.this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Mutex.kt */
    @Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0006\b¢\u0004\u0018\u00002\u00020\u00012\u00020\u0002B\u0011\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\u000b¢\u0006\u0004\b\u000f\u0010\u0010J\r\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\r\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ\u000f\u0010\t\u001a\u00020\u0003H&¢\u0006\u0004\b\t\u0010\u0005J\u000f\u0010\n\u001a\u00020\u0006H&¢\u0006\u0004\b\n\u0010\bR\u0016\u0010\u000e\u001a\u0004\u0018\u00010\u000b8\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\f\u0010\r¨\u0006\u0011"}, d2 = {"Lkotlinx/coroutines/sync/c$b;", "Lkotlinx/coroutines/internal/n;", "Ltd/u0;", "", "G", "()Z", "Lma/f0;", "a", "()V", "H", "F", "", "h", "Ljava/lang/Object;", "owner", "<init>", "(Lkotlinx/coroutines/sync/c;Ljava/lang/Object;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public abstract class b extends n implements u0 {

        /* renamed from: j, reason: collision with root package name */
        private static final /* synthetic */ AtomicIntegerFieldUpdater f14465j = AtomicIntegerFieldUpdater.newUpdater(b.class, "isTaken");

        /* renamed from: h, reason: collision with root package name and from kotlin metadata */
        public final Object owner;
        private volatile /* synthetic */ int isTaken = 0;

        public b(Object obj) {
            this.owner = obj;
        }

        public abstract void F();

        public final boolean G() {
            return f14465j.compareAndSet(this, 0, 1);
        }

        public abstract boolean H();

        @Override // td.u0
        public final void a() {
            A();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Mutex.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0005\u001a\u00020\u0004¢\u0006\u0004\b\u0007\u0010\bJ\b\u0010\u0003\u001a\u00020\u0002H\u0016R\u0016\u0010\u0005\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e¢\u0006\u0006\n\u0004\b\u0005\u0010\u0006¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/sync/c$c;", "Lkotlinx/coroutines/internal/l;", "", "toString", "", "owner", "Ljava/lang/Object;", "<init>", "(Ljava/lang/Object;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* renamed from: kotlinx.coroutines.sync.c$c, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public static final class C0072c extends kotlinx.coroutines.internal.l {
        public volatile Object owner;

        public C0072c(Object obj) {
            this.owner = obj;
        }

        @Override // kotlinx.coroutines.internal.n
        public String toString() {
            return "LockedQueue[" + this.owner + ']';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Mutex.kt */
    @Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u000f\u0012\u0006\u0010\f\u001a\u00020\t¢\u0006\u0004\b\r\u0010\u000eJ\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u001a\u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u0006\u001a\u0004\u0018\u00010\u0004H\u0016R\u0014\u0010\f\u001a\u00020\t8\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\n\u0010\u000b¨\u0006\u000f"}, d2 = {"Lkotlinx/coroutines/sync/c$d;", "Lkotlinx/coroutines/internal/c;", "Lkotlinx/coroutines/sync/c;", "affected", "", "i", "failure", "Lma/f0;", "h", "Lkotlinx/coroutines/sync/c$c;", "b", "Lkotlinx/coroutines/sync/c$c;", "queue", "<init>", "(Lkotlinx/coroutines/sync/c$c;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class d extends kotlinx.coroutines.internal.c<c> {

        /* renamed from: b, reason: collision with root package name and from kotlin metadata */
        public final C0072c queue;

        public d(C0072c c0072c) {
            this.queue = c0072c;
        }

        @Override // kotlinx.coroutines.internal.c
        /* renamed from: h, reason: merged with bridge method [inline-methods] */
        public void d(c cVar, Object obj) {
            c.f14460a.compareAndSet(cVar, this, obj == null ? kotlinx.coroutines.sync.d.f14476f : this.queue);
        }

        @Override // kotlinx.coroutines.internal.c
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public Object g(c affected) {
            Symbol symbol;
            if (this.queue.F()) {
                return null;
            }
            symbol = kotlinx.coroutines.sync.d.f14472b;
            return symbol;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Mutex.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"", "it", "Lma/f0;", "a", "(Ljava/lang/Throwable;)V"}, k = 3, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class e extends Lambda implements l<Throwable, Unit> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Object f14470f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        e(Object obj) {
            super(1);
            this.f14470f = obj;
        }

        public final void a(Throwable th) {
            c.this.a(this.f14470f);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
            a(th);
            return Unit.f15173a;
        }
    }

    public c(boolean z10) {
        this._state = z10 ? kotlinx.coroutines.sync.d.f14475e : kotlinx.coroutines.sync.d.f14476f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x006e, code lost:
    
        td.n.b(r0, r1);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final Object c(Object obj, qa.d<? super Unit> dVar) {
        qa.d b10;
        Symbol symbol;
        Object c10;
        Object c11;
        b10 = IntrinsicsJvm.b(dVar);
        td.l a10 = td.n.a(b10);
        a aVar = new a(obj, a10);
        while (true) {
            Object obj2 = this._state;
            if (obj2 instanceof kotlinx.coroutines.sync.a) {
                kotlinx.coroutines.sync.a aVar2 = (kotlinx.coroutines.sync.a) obj2;
                Object obj3 = aVar2.locked;
                symbol = kotlinx.coroutines.sync.d.f14474d;
                if (obj3 != symbol) {
                    f14460a.compareAndSet(this, obj2, new C0072c(aVar2.locked));
                } else {
                    if (f14460a.compareAndSet(this, obj2, obj == null ? kotlinx.coroutines.sync.d.f14475e : new kotlinx.coroutines.sync.a(obj))) {
                        a10.d(Unit.f15173a, new e(obj));
                        break;
                    }
                }
            } else if (obj2 instanceof C0072c) {
                C0072c c0072c = (C0072c) obj2;
                if (c0072c.owner != obj) {
                    c0072c.n(aVar);
                    if (this._state == obj2 || !aVar.G()) {
                        break;
                    }
                    aVar = new a(obj, a10);
                } else {
                    throw new IllegalStateException(("Already locked by " + obj).toString());
                }
            } else {
                if (!(obj2 instanceof v)) {
                    throw new IllegalStateException(("Illegal state " + obj2).toString());
                }
                ((v) obj2).c(this);
            }
        }
        Object w10 = a10.w();
        c10 = ra.d.c();
        if (w10 == c10) {
            h.c(dVar);
        }
        c11 = ra.d.c();
        return w10 == c11 ? w10 : Unit.f15173a;
    }

    @Override // kotlinx.coroutines.sync.b
    public void a(Object owner) {
        kotlinx.coroutines.sync.a aVar;
        Symbol symbol;
        while (true) {
            Object obj = this._state;
            if (obj instanceof kotlinx.coroutines.sync.a) {
                if (owner == null) {
                    Object obj2 = ((kotlinx.coroutines.sync.a) obj).locked;
                    symbol = kotlinx.coroutines.sync.d.f14474d;
                    if (!(obj2 != symbol)) {
                        throw new IllegalStateException("Mutex is not locked".toString());
                    }
                } else {
                    kotlinx.coroutines.sync.a aVar2 = (kotlinx.coroutines.sync.a) obj;
                    if (!(aVar2.locked == owner)) {
                        throw new IllegalStateException(("Mutex is locked by " + aVar2.locked + " but expected " + owner).toString());
                    }
                }
                AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = f14460a;
                aVar = kotlinx.coroutines.sync.d.f14476f;
                if (atomicReferenceFieldUpdater.compareAndSet(this, obj, aVar)) {
                    return;
                }
            } else if (obj instanceof v) {
                ((v) obj).c(this);
            } else if (obj instanceof C0072c) {
                if (owner != null) {
                    C0072c c0072c = (C0072c) obj;
                    if (!(c0072c.owner == owner)) {
                        throw new IllegalStateException(("Mutex is locked by " + c0072c.owner + " but expected " + owner).toString());
                    }
                }
                C0072c c0072c2 = (C0072c) obj;
                n B = c0072c2.B();
                if (B == null) {
                    d dVar = new d(c0072c2);
                    if (f14460a.compareAndSet(this, obj, dVar) && dVar.c(this) == null) {
                        return;
                    }
                } else {
                    b bVar = (b) B;
                    if (bVar.H()) {
                        Object obj3 = bVar.owner;
                        if (obj3 == null) {
                            obj3 = kotlinx.coroutines.sync.d.f14473c;
                        }
                        c0072c2.owner = obj3;
                        bVar.F();
                        return;
                    }
                }
            } else {
                throw new IllegalStateException(("Illegal state " + obj).toString());
            }
        }
    }

    @Override // kotlinx.coroutines.sync.b
    public Object b(Object obj, qa.d<? super Unit> dVar) {
        Object c10;
        if (d(obj)) {
            return Unit.f15173a;
        }
        Object c11 = c(obj, dVar);
        c10 = ra.d.c();
        return c11 == c10 ? c11 : Unit.f15173a;
    }

    public boolean d(Object owner) {
        Symbol symbol;
        while (true) {
            Object obj = this._state;
            if (obj instanceof kotlinx.coroutines.sync.a) {
                Object obj2 = ((kotlinx.coroutines.sync.a) obj).locked;
                symbol = kotlinx.coroutines.sync.d.f14474d;
                if (obj2 != symbol) {
                    return false;
                }
                if (f14460a.compareAndSet(this, obj, owner == null ? kotlinx.coroutines.sync.d.f14475e : new kotlinx.coroutines.sync.a(owner))) {
                    return true;
                }
            } else {
                if (obj instanceof C0072c) {
                    if (((C0072c) obj).owner != owner) {
                        return false;
                    }
                    throw new IllegalStateException(("Already locked by " + owner).toString());
                }
                if (!(obj instanceof v)) {
                    throw new IllegalStateException(("Illegal state " + obj).toString());
                }
                ((v) obj).c(this);
            }
        }
    }

    public String toString() {
        while (true) {
            Object obj = this._state;
            if (obj instanceof kotlinx.coroutines.sync.a) {
                return "Mutex[" + ((kotlinx.coroutines.sync.a) obj).locked + ']';
            }
            if (!(obj instanceof v)) {
                if (!(obj instanceof C0072c)) {
                    throw new IllegalStateException(("Illegal state " + obj).toString());
                }
                return "Mutex[" + ((C0072c) obj).owner + ']';
            }
            ((v) obj).c(this);
        }
    }
}
