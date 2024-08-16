package td;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlinx.coroutines.internal.Symbol;
import ma.ExceptionsH;
import ma.Unit;
import sa.CoroutineStackFrame;
import td.i1;

/* compiled from: CancellableContinuationImpl.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0096\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0001\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\r\b\u0011\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u00028\u00000\u00022\b\u0012\u0004\u0012\u00028\u00000\u00032\u00060\u0004j\u0002`\u0005B\u001d\u0012\f\u0010e\u001a\b\u0012\u0004\u0012\u00028\u00000d\u0012\u0006\u0010'\u001a\u00020!¢\u0006\u0004\bt\u0010uJ\u000f\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0004\b\u0007\u0010\bJ\u0017\u0010\u000b\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\tH\u0002¢\u0006\u0004\b\u000b\u0010\fJ3\u0010\u0011\u001a\u00020\u000e2\u0018\u0010\u0010\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0004\u0012\u00020\u000e0\rj\u0002`\u000f2\b\u0010\n\u001a\u0004\u0018\u00010\tH\u0002¢\u0006\u0004\b\u0011\u0010\u0012J\u000f\u0010\u0013\u001a\u00020\u0006H\u0002¢\u0006\u0004\b\u0013\u0010\bJ\u000f\u0010\u0014\u001a\u00020\u0006H\u0002¢\u0006\u0004\b\u0014\u0010\bJ\u0011\u0010\u0016\u001a\u0004\u0018\u00010\u0015H\u0002¢\u0006\u0004\b\u0016\u0010\u0017J\u000f\u0010\u0018\u001a\u00020\u000eH\u0002¢\u0006\u0004\b\u0018\u0010\u0019J3\u0010\u001c\u001a\u00020\u000e2\u0018\u0010\u0010\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0004\u0012\u00020\u000e0\rj\u0002`\u000f2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001aH\u0002¢\u0006\u0004\b\u001c\u0010\u001dJ)\u0010\u001f\u001a\u00020\u001e2\u0018\u0010\u0010\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0004\u0012\u00020\u000e0\rj\u0002`\u000fH\u0002¢\u0006\u0004\b\u001f\u0010 J\u0017\u0010#\u001a\u00020\u000e2\u0006\u0010\"\u001a\u00020!H\u0002¢\u0006\u0004\b#\u0010$JK\u0010*\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001b\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\u001a2\u0006\u0010'\u001a\u00020!2\u0014\u0010(\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000e\u0018\u00010\r2\b\u0010)\u001a\u0004\u0018\u00010\u001aH\u0002¢\u0006\u0004\b*\u0010+J9\u0010,\u001a\u00020\u000e2\b\u0010&\u001a\u0004\u0018\u00010\u001a2\u0006\u0010'\u001a\u00020!2\u0016\b\u0002\u0010(\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000e\u0018\u00010\rH\u0002¢\u0006\u0004\b,\u0010-J;\u0010/\u001a\u0004\u0018\u00010.2\b\u0010&\u001a\u0004\u0018\u00010\u001a2\b\u0010)\u001a\u0004\u0018\u00010\u001a2\u0014\u0010(\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000e\u0018\u00010\rH\u0002¢\u0006\u0004\b/\u00100J\u0019\u00102\u001a\u0002012\b\u0010&\u001a\u0004\u0018\u00010\u001aH\u0002¢\u0006\u0004\b2\u00103J\u000f\u00104\u001a\u00020\u000eH\u0002¢\u0006\u0004\b4\u0010\u0019J\u000f\u00105\u001a\u00020\u000eH\u0016¢\u0006\u0004\b5\u0010\u0019J\u000f\u00106\u001a\u00020\u0006H\u0001¢\u0006\u0004\b6\u0010\bJ\u0011\u00107\u001a\u0004\u0018\u00010\u001aH\u0010¢\u0006\u0004\b7\u00108J!\u0010:\u001a\u00020\u000e2\b\u00109\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\n\u001a\u00020\tH\u0010¢\u0006\u0004\b:\u0010;J\u0019\u0010<\u001a\u00020\u00062\b\u0010\n\u001a\u0004\u0018\u00010\tH\u0016¢\u0006\u0004\b<\u0010\fJ\u0017\u0010=\u001a\u00020\u000e2\u0006\u0010\n\u001a\u00020\tH\u0000¢\u0006\u0004\b=\u0010>J\u001f\u0010?\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u001e2\b\u0010\n\u001a\u0004\u0018\u00010\t¢\u0006\u0004\b?\u0010@J)\u0010A\u001a\u00020\u000e2\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000e0\r2\u0006\u0010\n\u001a\u00020\t¢\u0006\u0004\bA\u0010\u0012J\u0017\u0010D\u001a\u00020\t2\u0006\u0010C\u001a\u00020BH\u0016¢\u0006\u0004\bD\u0010EJ\u0011\u0010F\u001a\u0004\u0018\u00010\u001aH\u0001¢\u0006\u0004\bF\u00108J \u0010I\u001a\u00020\u000e2\f\u0010H\u001a\b\u0012\u0004\u0012\u00028\u00000GH\u0016ø\u0001\u0000¢\u0006\u0004\bI\u0010JJ-\u0010L\u001a\u00020\u000e2\u0006\u0010K\u001a\u00028\u00002\u0014\u0010(\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000e\u0018\u00010\rH\u0016¢\u0006\u0004\bL\u0010MJ)\u0010N\u001a\u00020\u000e2\u0018\u0010\u0010\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0004\u0012\u00020\u000e0\rj\u0002`\u000fH\u0016¢\u0006\u0004\bN\u0010OJ\u000f\u0010P\u001a\u00020\u000eH\u0000¢\u0006\u0004\bP\u0010\u0019J#\u0010Q\u001a\u0004\u0018\u00010\u001a2\u0006\u0010K\u001a\u00028\u00002\b\u0010)\u001a\u0004\u0018\u00010\u001aH\u0016¢\u0006\u0004\bQ\u0010RJ9\u0010S\u001a\u0004\u0018\u00010\u001a2\u0006\u0010K\u001a\u00028\u00002\b\u0010)\u001a\u0004\u0018\u00010\u001a2\u0014\u0010(\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000e\u0018\u00010\rH\u0016¢\u0006\u0004\bS\u0010TJ\u0019\u0010V\u001a\u0004\u0018\u00010\u001a2\u0006\u0010U\u001a\u00020\tH\u0016¢\u0006\u0004\bV\u0010WJ\u0017\u0010Y\u001a\u00020\u000e2\u0006\u0010X\u001a\u00020\u001aH\u0016¢\u0006\u0004\bY\u0010JJ\u001f\u0010Z\u001a\u00028\u0001\"\u0004\b\u0001\u0010\u00012\b\u0010\u001b\u001a\u0004\u0018\u00010\u001aH\u0010¢\u0006\u0004\bZ\u0010[J\u001b\u0010\\\u001a\u0004\u0018\u00010\t2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001aH\u0010¢\u0006\u0004\b\\\u0010]J\u000f\u0010_\u001a\u00020^H\u0016¢\u0006\u0004\b_\u0010`J\u000f\u0010a\u001a\u00020^H\u0014¢\u0006\u0004\ba\u0010`R\u0014\u0010c\u001a\u00020^8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\bb\u0010`R \u0010e\u001a\b\u0012\u0004\u0012\u00028\u00000d8\u0000X\u0080\u0004¢\u0006\f\n\u0004\be\u0010f\u001a\u0004\bg\u0010hR\u001a\u0010j\u001a\u00020i8\u0016X\u0096\u0004¢\u0006\f\n\u0004\bj\u0010k\u001a\u0004\bl\u0010mR\u0016\u0010\u001b\u001a\u0004\u0018\u00010\u001a8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\bn\u00108R\u0014\u0010p\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\bo\u0010\bR\u001c\u0010s\u001a\n\u0018\u00010\u0004j\u0004\u0018\u0001`\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\bq\u0010r\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006v"}, d2 = {"Ltd/l;", "T", "Ltd/r0;", "Ltd/k;", "Lsa/e;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "", "C", "()Z", "", "cause", "r", "(Ljava/lang/Throwable;)Z", "Lkotlin/Function1;", "Lma/f0;", "Lkotlinx/coroutines/CompletionHandler;", "handler", "o", "(Lya/l;Ljava/lang/Throwable;)V", "O", "M", "Ltd/u0;", "A", "()Ltd/u0;", "H", "()V", "", "state", "E", "(Lya/l;Ljava/lang/Object;)V", "Ltd/i;", "D", "(Lya/l;)Ltd/i;", "", "mode", "u", "(I)V", "Ltd/v1;", "proposedUpdate", "resumeMode", "onCancellation", "idempotent", "L", "(Ltd/v1;Ljava/lang/Object;ILya/l;Ljava/lang/Object;)Ljava/lang/Object;", "J", "(Ljava/lang/Object;ILya/l;)V", "Lkotlinx/coroutines/internal/a0;", "N", "(Ljava/lang/Object;Ljava/lang/Object;Lya/l;)Lkotlinx/coroutines/internal/a0;", "", "m", "(Ljava/lang/Object;)Ljava/lang/Void;", "t", "z", "I", "k", "()Ljava/lang/Object;", "takenState", "b", "(Ljava/lang/Object;Ljava/lang/Throwable;)V", "q", "G", "(Ljava/lang/Throwable;)V", "n", "(Ltd/i;Ljava/lang/Throwable;)V", "p", "Ltd/i1;", "parent", "v", "(Ltd/i1;)Ljava/lang/Throwable;", "w", "Lma/p;", "result", "resumeWith", "(Ljava/lang/Object;)V", ThermalBaseConfig.Item.ATTR_VALUE, "d", "(Ljava/lang/Object;Lya/l;)V", "f", "(Lya/l;)V", "s", "a", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "j", "(Ljava/lang/Object;Ljava/lang/Object;Lya/l;)Ljava/lang/Object;", "exception", "i", "(Ljava/lang/Throwable;)Ljava/lang/Object;", "token", "l", "g", "(Ljava/lang/Object;)Ljava/lang/Object;", "e", "(Ljava/lang/Object;)Ljava/lang/Throwable;", "", "toString", "()Ljava/lang/String;", "F", "y", "stateDebugRepresentation", "Lqa/d;", "delegate", "Lqa/d;", "c", "()Lqa/d;", "Lqa/g;", "context", "Lqa/g;", "getContext", "()Lqa/g;", "x", "B", "isCompleted", "getCallerFrame", "()Lsa/e;", "callerFrame", "<init>", "(Lqa/d;I)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public class l<T> extends r0<T> implements k<T>, CoroutineStackFrame {

    /* renamed from: k, reason: collision with root package name */
    private static final /* synthetic */ AtomicIntegerFieldUpdater f18755k = AtomicIntegerFieldUpdater.newUpdater(l.class, "_decision");

    /* renamed from: l, reason: collision with root package name */
    private static final /* synthetic */ AtomicReferenceFieldUpdater f18756l = AtomicReferenceFieldUpdater.newUpdater(l.class, Object.class, "_state");
    private volatile /* synthetic */ int _decision;
    private volatile /* synthetic */ Object _state;

    /* renamed from: h, reason: collision with root package name */
    private final qa.d<T> f18757h;

    /* renamed from: i, reason: collision with root package name */
    private final qa.g f18758i;

    /* renamed from: j, reason: collision with root package name */
    private u0 f18759j;

    /* JADX WARN: Multi-variable type inference failed */
    public l(qa.d<? super T> dVar, int i10) {
        super(i10);
        this.f18757h = dVar;
        this.f18758i = dVar.getF18758i();
        this._decision = 0;
        this._state = d.f18729e;
    }

    private final u0 A() {
        i1 i1Var = (i1) getF18758i().c(i1.f18746d);
        if (i1Var == null) {
            return null;
        }
        u0 d10 = i1.a.d(i1Var, true, false, new p(this), 2, null);
        this.f18759j = d10;
        return d10;
    }

    private final boolean C() {
        return s0.c(this.f18787g) && ((kotlinx.coroutines.internal.e) this.f18757h).p();
    }

    private final i D(ya.l<? super Throwable, Unit> handler) {
        return handler instanceof i ? (i) handler : new f1(handler);
    }

    private final void E(ya.l<? super Throwable, Unit> handler, Object state) {
        throw new IllegalStateException(("It's prohibited to register multiple handlers, tried to register " + handler + ", already has " + state).toString());
    }

    private final void H() {
        Throwable s7;
        qa.d<T> dVar = this.f18757h;
        kotlinx.coroutines.internal.e eVar = dVar instanceof kotlinx.coroutines.internal.e ? (kotlinx.coroutines.internal.e) dVar : null;
        if (eVar == null || (s7 = eVar.s(this)) == null) {
            return;
        }
        s();
        q(s7);
    }

    private final void J(Object proposedUpdate, int resumeMode, ya.l<? super Throwable, Unit> onCancellation) {
        Object obj;
        do {
            obj = this._state;
            if (obj instanceof v1) {
            } else {
                if (obj instanceof o) {
                    o oVar = (o) obj;
                    if (oVar.c()) {
                        if (onCancellation != null) {
                            p(onCancellation, oVar.f18800a);
                            return;
                        }
                        return;
                    }
                }
                m(proposedUpdate);
                throw new ExceptionsH();
            }
        } while (!f18756l.compareAndSet(this, obj, L((v1) obj, proposedUpdate, resumeMode, onCancellation, null)));
        t();
        u(resumeMode);
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void K(l lVar, Object obj, int i10, ya.l lVar2, int i11, Object obj2) {
        if (obj2 != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: resumeImpl");
        }
        if ((i11 & 4) != 0) {
            lVar2 = null;
        }
        lVar.J(obj, i10, lVar2);
    }

    private final Object L(v1 state, Object proposedUpdate, int resumeMode, ya.l<? super Throwable, Unit> onCancellation, Object idempotent) {
        if (proposedUpdate instanceof v) {
            return proposedUpdate;
        }
        if (!s0.b(resumeMode) && idempotent == null) {
            return proposedUpdate;
        }
        if (onCancellation != null || (((state instanceof i) && !(state instanceof e)) || idempotent != null)) {
            return new CompletedContinuation(proposedUpdate, state instanceof i ? (i) state : null, onCancellation, idempotent, null, 16, null);
        }
        return proposedUpdate;
    }

    private final boolean M() {
        do {
            int i10 = this._decision;
            if (i10 != 0) {
                if (i10 == 1) {
                    return false;
                }
                throw new IllegalStateException("Already resumed".toString());
            }
        } while (!f18755k.compareAndSet(this, 0, 2));
        return true;
    }

    private final Symbol N(Object proposedUpdate, Object idempotent, ya.l<? super Throwable, Unit> onCancellation) {
        Object obj;
        do {
            obj = this._state;
            if (obj instanceof v1) {
            } else {
                if ((obj instanceof CompletedContinuation) && idempotent != null && ((CompletedContinuation) obj).idempotentResume == idempotent) {
                    return m.f18761a;
                }
                return null;
            }
        } while (!f18756l.compareAndSet(this, obj, L((v1) obj, proposedUpdate, this.f18787g, onCancellation, idempotent)));
        t();
        return m.f18761a;
    }

    private final boolean O() {
        do {
            int i10 = this._decision;
            if (i10 != 0) {
                if (i10 == 2) {
                    return false;
                }
                throw new IllegalStateException("Already suspended".toString());
            }
        } while (!f18755k.compareAndSet(this, 0, 1));
        return true;
    }

    private final Void m(Object proposedUpdate) {
        throw new IllegalStateException(("Already resumed, but proposed with update " + proposedUpdate).toString());
    }

    private final void o(ya.l<? super Throwable, Unit> handler, Throwable cause) {
        try {
            handler.invoke(cause);
        } catch (Throwable th) {
            f0.a(getF18758i(), new y("Exception in invokeOnCancellation handler for " + this, th));
        }
    }

    private final boolean r(Throwable cause) {
        if (C()) {
            return ((kotlinx.coroutines.internal.e) this.f18757h).q(cause);
        }
        return false;
    }

    private final void t() {
        if (C()) {
            return;
        }
        s();
    }

    private final void u(int mode) {
        if (M()) {
            return;
        }
        s0.a(this, mode);
    }

    private final String y() {
        Object obj = get_state();
        return obj instanceof v1 ? "Active" : obj instanceof o ? "Cancelled" : "Completed";
    }

    public boolean B() {
        return !(get_state() instanceof v1);
    }

    protected String F() {
        return "CancellableContinuation";
    }

    public final void G(Throwable cause) {
        if (r(cause)) {
            return;
        }
        q(cause);
        t();
    }

    public final boolean I() {
        Object obj = this._state;
        if ((obj instanceof CompletedContinuation) && ((CompletedContinuation) obj).idempotentResume != null) {
            s();
            return false;
        }
        this._decision = 0;
        this._state = d.f18729e;
        return true;
    }

    @Override // td.k
    public Object a(T value, Object idempotent) {
        return N(value, idempotent, null);
    }

    @Override // td.r0
    public void b(Object takenState, Throwable cause) {
        while (true) {
            Object obj = this._state;
            if (!(obj instanceof v1)) {
                if (obj instanceof v) {
                    return;
                }
                if (obj instanceof CompletedContinuation) {
                    CompletedContinuation completedContinuation = (CompletedContinuation) obj;
                    if (!completedContinuation.c()) {
                        if (f18756l.compareAndSet(this, obj, CompletedContinuation.b(completedContinuation, null, null, null, null, cause, 15, null))) {
                            completedContinuation.d(this, cause);
                            return;
                        }
                    } else {
                        throw new IllegalStateException("Must be called at most once".toString());
                    }
                } else if (f18756l.compareAndSet(this, obj, new CompletedContinuation(obj, null, null, null, cause, 14, null))) {
                    return;
                }
            } else {
                throw new IllegalStateException("Not completed".toString());
            }
        }
    }

    @Override // td.r0
    public final qa.d<T> c() {
        return this.f18757h;
    }

    @Override // td.k
    public void d(T value, ya.l<? super Throwable, Unit> onCancellation) {
        J(value, this.f18787g, onCancellation);
    }

    @Override // td.r0
    public Throwable e(Object state) {
        Throwable e10 = super.e(state);
        if (e10 != null) {
            return e10;
        }
        return null;
    }

    @Override // td.k
    public void f(ya.l<? super Throwable, Unit> handler) {
        i D = D(handler);
        while (true) {
            Object obj = this._state;
            if (obj instanceof d) {
                if (f18756l.compareAndSet(this, obj, D)) {
                    return;
                }
            } else if (obj instanceof i) {
                E(handler, obj);
            } else {
                boolean z10 = obj instanceof v;
                if (z10) {
                    v vVar = (v) obj;
                    if (!vVar.b()) {
                        E(handler, obj);
                    }
                    if (obj instanceof o) {
                        if (!z10) {
                            vVar = null;
                        }
                        o(handler, vVar != null ? vVar.f18800a : null);
                        return;
                    }
                    return;
                }
                if (obj instanceof CompletedContinuation) {
                    CompletedContinuation completedContinuation = (CompletedContinuation) obj;
                    if (completedContinuation.cancelHandler != null) {
                        E(handler, obj);
                    }
                    if (D instanceof e) {
                        return;
                    }
                    if (completedContinuation.c()) {
                        o(handler, completedContinuation.cancelCause);
                        return;
                    } else {
                        if (f18756l.compareAndSet(this, obj, CompletedContinuation.b(completedContinuation, null, D, null, null, null, 29, null))) {
                            return;
                        }
                    }
                } else {
                    if (D instanceof e) {
                        return;
                    }
                    if (f18756l.compareAndSet(this, obj, new CompletedContinuation(obj, D, null, null, null, 28, null))) {
                        return;
                    }
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // td.r0
    public <T> T g(Object state) {
        return state instanceof CompletedContinuation ? (T) ((CompletedContinuation) state).result : state;
    }

    @Override // sa.CoroutineStackFrame
    public CoroutineStackFrame getCallerFrame() {
        qa.d<T> dVar = this.f18757h;
        if (dVar instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) dVar;
        }
        return null;
    }

    @Override // qa.d
    /* renamed from: getContext, reason: from getter */
    public qa.g getF18758i() {
        return this.f18758i;
    }

    @Override // td.k
    public Object i(Throwable exception) {
        return N(new v(exception, false, 2, null), null, null);
    }

    @Override // td.k
    public Object j(T value, Object idempotent, ya.l<? super Throwable, Unit> onCancellation) {
        return N(value, idempotent, onCancellation);
    }

    @Override // td.r0
    public Object k() {
        return get_state();
    }

    @Override // td.k
    public void l(Object token) {
        u(this.f18787g);
    }

    public final void n(i handler, Throwable cause) {
        try {
            handler.a(cause);
        } catch (Throwable th) {
            f0.a(getF18758i(), new y("Exception in invokeOnCancellation handler for " + this, th));
        }
    }

    public final void p(ya.l<? super Throwable, Unit> onCancellation, Throwable cause) {
        try {
            onCancellation.invoke(cause);
        } catch (Throwable th) {
            f0.a(getF18758i(), new y("Exception in resume onCancellation handler for " + this, th));
        }
    }

    public boolean q(Throwable cause) {
        Object obj;
        boolean z10;
        do {
            obj = this._state;
            if (!(obj instanceof v1)) {
                return false;
            }
            z10 = obj instanceof i;
        } while (!f18756l.compareAndSet(this, obj, new o(this, cause, z10)));
        i iVar = z10 ? (i) obj : null;
        if (iVar != null) {
            n(iVar, cause);
        }
        t();
        u(this.f18787g);
        return true;
    }

    @Override // qa.d
    public void resumeWith(Object result) {
        K(this, z.b(result, this), this.f18787g, null, 4, null);
    }

    public final void s() {
        u0 u0Var = this.f18759j;
        if (u0Var == null) {
            return;
        }
        u0Var.a();
        this.f18759j = u1.f18798e;
    }

    public String toString() {
        return F() + '(' + DebugStrings.c(this.f18757h) + "){" + y() + "}@" + DebugStrings.b(this);
    }

    public Throwable v(i1 parent) {
        return parent.w();
    }

    public final Object w() {
        i1 i1Var;
        Object c10;
        boolean C = C();
        if (O()) {
            if (this.f18759j == null) {
                A();
            }
            if (C) {
                H();
            }
            c10 = ra.d.c();
            return c10;
        }
        if (C) {
            H();
        }
        Object obj = get_state();
        if (!(obj instanceof v)) {
            if (s0.b(this.f18787g) && (i1Var = (i1) getF18758i().c(i1.f18746d)) != null && !i1Var.b()) {
                CancellationException w10 = i1Var.w();
                b(obj, w10);
                throw w10;
            }
            return g(obj);
        }
        throw ((v) obj).f18800a;
    }

    /* renamed from: x, reason: from getter */
    public final Object get_state() {
        return this._state;
    }

    public void z() {
        u0 A = A();
        if (A != null && B()) {
            A.a();
            this.f18759j = u1.f18798e;
        }
    }
}
