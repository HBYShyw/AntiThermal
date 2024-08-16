package kotlinx.coroutines.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import ma.Unit;
import sa.CoroutineStackFrame;
import td.CompletedWithCancellation;
import td.CoroutineDispatcher;
import td.DebugStrings;
import td.c2;
import td.r0;
import td.w0;

/* compiled from: DispatchedContinuation.kt */
@Metadata(bv = {}, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u00028\u00000\u00022\u00060\u0003j\u0002`\u00042\b\u0012\u0004\u0012\u00028\u00000\u0005B\u001d\u0012\u0006\u00107\u001a\u000206\u0012\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005¢\u0006\u0004\b8\u00109J\r\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ\r\u0010\n\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bJ\r\u0010\f\u001a\u00020\t¢\u0006\u0004\b\f\u0010\u000bJ\u0015\u0010\u000e\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\r¢\u0006\u0004\b\u000e\u0010\u000fJ\u001b\u0010\u0013\u001a\u0004\u0018\u00010\u00122\n\u0010\u0011\u001a\u0006\u0012\u0002\b\u00030\u0010¢\u0006\u0004\b\u0013\u0010\u0014J\u0015\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0012¢\u0006\u0004\b\u0016\u0010\u0017J\u0011\u0010\u0019\u001a\u0004\u0018\u00010\u0018H\u0010¢\u0006\u0004\b\u0019\u0010\u001aJ \u0010\u001d\u001a\u00020\t2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00028\u00000\u001bH\u0016ø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u001eJ!\u0010 \u001a\u00020\t2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0015\u001a\u00020\u0012H\u0010¢\u0006\u0004\b \u0010!J\u000f\u0010#\u001a\u00020\"H\u0016¢\u0006\u0004\b#\u0010$R\u001e\u0010(\u001a\u0004\u0018\u00010\u00188\u0000@\u0000X\u0081\u000e¢\u0006\f\n\u0004\b%\u0010&\u0012\u0004\b'\u0010\u000bR\u0014\u0010)\u001a\u00020\u00188\u0000X\u0081\u0004¢\u0006\u0006\n\u0004\b\u0019\u0010&R\u001a\u0010+\u001a\b\u0012\u0002\b\u0003\u0018\u00010\r8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b*\u0010\u000fR\u0014\u0010/\u001a\u00020,8\u0016X\u0096\u0005¢\u0006\u0006\u001a\u0004\b-\u0010.R\u001c\u00102\u001a\n\u0018\u00010\u0003j\u0004\u0018\u0001`\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b0\u00101R\u001a\u00105\u001a\b\u0012\u0004\u0012\u00028\u00000\u00058PX\u0090\u0004¢\u0006\u0006\u001a\u0004\b3\u00104\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006:"}, d2 = {"Lkotlinx/coroutines/internal/e;", "T", "Ltd/r0;", "Lsa/e;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "Lqa/d;", "", "p", "()Z", "Lma/f0;", "m", "()V", "r", "Ltd/l;", "n", "()Ltd/l;", "Ltd/k;", "continuation", "", "s", "(Ltd/k;)Ljava/lang/Throwable;", "cause", "q", "(Ljava/lang/Throwable;)Z", "", "k", "()Ljava/lang/Object;", "Lma/p;", "result", "resumeWith", "(Ljava/lang/Object;)V", "takenState", "b", "(Ljava/lang/Object;Ljava/lang/Throwable;)V", "", "toString", "()Ljava/lang/String;", "j", "Ljava/lang/Object;", "get_state$kotlinx_coroutines_core$annotations", "_state", "countOrElement", "o", "reusableCancellableContinuation", "Lqa/g;", "getContext", "()Lqa/g;", "context", "getCallerFrame", "()Lsa/e;", "callerFrame", "c", "()Lqa/d;", "delegate", "Ltd/c0;", "dispatcher", "<init>", "(Ltd/c0;Lqa/d;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class e<T> extends r0<T> implements CoroutineStackFrame, qa.d<T> {

    /* renamed from: l, reason: collision with root package name */
    private static final /* synthetic */ AtomicReferenceFieldUpdater f14350l = AtomicReferenceFieldUpdater.newUpdater(e.class, Object.class, "_reusableCancellableContinuation");
    private volatile /* synthetic */ Object _reusableCancellableContinuation;

    /* renamed from: h, reason: collision with root package name */
    public final CoroutineDispatcher f14351h;

    /* renamed from: i, reason: collision with root package name */
    public final qa.d<T> f14352i;

    /* renamed from: j, reason: collision with root package name and from kotlin metadata */
    public Object _state;

    /* renamed from: k, reason: collision with root package name and from kotlin metadata */
    public final Object countOrElement;

    /* JADX WARN: Multi-variable type inference failed */
    public e(CoroutineDispatcher coroutineDispatcher, qa.d<? super T> dVar) {
        super(-1);
        this.f14351h = coroutineDispatcher;
        this.f14352i = dVar;
        this._state = f.a();
        this.countOrElement = e0.b(getF18758i());
        this._reusableCancellableContinuation = null;
    }

    private final td.l<?> o() {
        Object obj = this._reusableCancellableContinuation;
        if (obj instanceof td.l) {
            return (td.l) obj;
        }
        return null;
    }

    @Override // td.r0
    public void b(Object takenState, Throwable cause) {
        if (takenState instanceof CompletedWithCancellation) {
            ((CompletedWithCancellation) takenState).onCancellation.invoke(cause);
        }
    }

    @Override // td.r0
    public qa.d<T> c() {
        return this;
    }

    @Override // sa.CoroutineStackFrame
    public CoroutineStackFrame getCallerFrame() {
        qa.d<T> dVar = this.f14352i;
        if (dVar instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) dVar;
        }
        return null;
    }

    @Override // qa.d
    /* renamed from: getContext */
    public qa.g getF18758i() {
        return this.f14352i.getF18758i();
    }

    @Override // td.r0
    public Object k() {
        Object obj = this._state;
        this._state = f.a();
        return obj;
    }

    public final void m() {
        do {
        } while (this._reusableCancellableContinuation == f.f14363b);
    }

    public final td.l<T> n() {
        while (true) {
            Object obj = this._reusableCancellableContinuation;
            if (obj == null) {
                this._reusableCancellableContinuation = f.f14363b;
                return null;
            }
            if (obj instanceof td.l) {
                if (f14350l.compareAndSet(this, obj, f.f14363b)) {
                    return (td.l) obj;
                }
            } else if (obj != f.f14363b && !(obj instanceof Throwable)) {
                throw new IllegalStateException(("Inconsistent state " + obj).toString());
            }
        }
    }

    public final boolean p() {
        return this._reusableCancellableContinuation != null;
    }

    public final boolean q(Throwable cause) {
        while (true) {
            Object obj = this._reusableCancellableContinuation;
            Symbol symbol = f.f14363b;
            if (za.k.a(obj, symbol)) {
                if (f14350l.compareAndSet(this, symbol, cause)) {
                    return true;
                }
            } else {
                if (obj instanceof Throwable) {
                    return true;
                }
                if (f14350l.compareAndSet(this, obj, null)) {
                    return false;
                }
            }
        }
    }

    public final void r() {
        m();
        td.l<?> o10 = o();
        if (o10 != null) {
            o10.s();
        }
    }

    @Override // qa.d
    public void resumeWith(Object result) {
        qa.g f18758i = this.f14352i.getF18758i();
        Object d10 = td.z.d(result, null, 1, null);
        if (this.f14351h.u0(f18758i)) {
            this._state = d10;
            this.f18787g = 0;
            this.f14351h.t0(f18758i, this);
            return;
        }
        w0 a10 = c2.f18727a.a();
        if (a10.C0()) {
            this._state = d10;
            this.f18787g = 0;
            a10.y0(this);
            return;
        }
        a10.A0(true);
        try {
            qa.g f18758i2 = getF18758i();
            Object c10 = e0.c(f18758i2, this.countOrElement);
            try {
                this.f14352i.resumeWith(result);
                Unit unit = Unit.f15173a;
                do {
                } while (a10.E0());
            } finally {
                e0.a(f18758i2, c10);
            }
        } finally {
            try {
            } finally {
            }
        }
    }

    public final Throwable s(td.k<?> continuation) {
        Symbol symbol;
        do {
            Object obj = this._reusableCancellableContinuation;
            symbol = f.f14363b;
            if (obj != symbol) {
                if (obj instanceof Throwable) {
                    if (f14350l.compareAndSet(this, obj, null)) {
                        return (Throwable) obj;
                    }
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                throw new IllegalStateException(("Inconsistent state " + obj).toString());
            }
        } while (!f14350l.compareAndSet(this, symbol, continuation));
        return null;
    }

    public String toString() {
        return "DispatchedContinuation[" + this.f14351h + ", " + DebugStrings.c(this.f14352i) + ']';
    }
}
