package sa;

import java.io.Serializable;
import ma.Unit;
import ma.p;
import ma.q;

/* compiled from: ContinuationImpl.kt */
/* loaded from: classes2.dex */
public abstract class a implements qa.d<Object>, CoroutineStackFrame, Serializable {

    /* renamed from: e, reason: collision with root package name */
    private final qa.d<Object> f18191e;

    public a(qa.d<Object> dVar) {
        this.f18191e = dVar;
    }

    protected void b() {
    }

    public qa.d<Unit> create(qa.d<?> dVar) {
        za.k.e(dVar, "completion");
        throw new UnsupportedOperationException("create(Continuation) has not been overridden");
    }

    @Override // sa.CoroutineStackFrame
    public CoroutineStackFrame getCallerFrame() {
        qa.d<Object> dVar = this.f18191e;
        if (dVar instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) dVar;
        }
        return null;
    }

    public final qa.d<Object> getCompletion() {
        return this.f18191e;
    }

    public StackTraceElement getStackTraceElement() {
        return g.d(this);
    }

    protected abstract Object invokeSuspend(Object obj);

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [qa.d, java.lang.Object, qa.d<java.lang.Object>] */
    @Override // qa.d
    public final void resumeWith(Object obj) {
        Object invokeSuspend;
        Object c10;
        while (true) {
            h.b(this);
            a aVar = this;
            ?? r02 = aVar.f18191e;
            za.k.b(r02);
            try {
                invokeSuspend = aVar.invokeSuspend(obj);
                c10 = ra.d.c();
            } catch (Throwable th) {
                p.a aVar2 = p.f15184e;
                obj = p.a(q.a(th));
            }
            if (invokeSuspend == c10) {
                return;
            }
            p.a aVar3 = p.f15184e;
            obj = p.a(invokeSuspend);
            aVar.b();
            if (!(r02 instanceof a)) {
                r02.resumeWith(obj);
                return;
            }
            this = r02;
        }
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Continuation at ");
        Object stackTraceElement = getStackTraceElement();
        if (stackTraceElement == null) {
            stackTraceElement = getClass().getName();
        }
        sb2.append(stackTraceElement);
        return sb2.toString();
    }

    public qa.d<Unit> create(Object obj, qa.d<?> dVar) {
        za.k.e(dVar, "completion");
        throw new UnsupportedOperationException("create(Any?;Continuation) has not been overridden");
    }
}
