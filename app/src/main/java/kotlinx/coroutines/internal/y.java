package kotlinx.coroutines.internal;

import kotlin.Metadata;
import ra.IntrinsicsJvm;
import sa.CoroutineStackFrame;
import td.AbstractCoroutine;
import td.i1;

/* compiled from: Scopes.kt */
@Metadata(bv = {}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0010\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u00028\u00000\u00022\u00060\u0003j\u0002`\u0004B\u001d\u0012\u0006\u0010\u0016\u001a\u00020\u0015\u0012\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00028\u00000\u0017¢\u0006\u0004\b\u0019\u0010\u001aJ\u0012\u0010\b\u001a\u00020\u00072\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0014J\u0012\u0010\t\u001a\u00020\u00072\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0014R\u0019\u0010\f\u001a\n\u0018\u00010\u0003j\u0004\u0018\u0001`\u00048F¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\u0010\u001a\u00020\r8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0014\u001a\u0004\u0018\u00010\u00118@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u001b"}, d2 = {"Lkotlinx/coroutines/internal/y;", "T", "Ltd/a;", "Lsa/e;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "", "state", "Lma/f0;", "o", "z0", "getCallerFrame", "()Lsa/e;", "callerFrame", "", "Q", "()Z", "isScopedCoroutine", "Ltd/i1;", "D0", "()Ltd/i1;", "parent", "Lqa/g;", "context", "Lqa/d;", "uCont", "<init>", "(Lqa/g;Lqa/d;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public class y<T> extends AbstractCoroutine<T> implements CoroutineStackFrame {

    /* renamed from: g, reason: collision with root package name */
    public final qa.d<T> f14403g;

    /* JADX WARN: Multi-variable type inference failed */
    public y(qa.g gVar, qa.d<? super T> dVar) {
        super(gVar, true, true);
        this.f14403g = dVar;
    }

    public final i1 D0() {
        td.q I = I();
        if (I != null) {
            return I.getParent();
        }
        return null;
    }

    @Override // td.p1
    protected final boolean Q() {
        return true;
    }

    @Override // sa.CoroutineStackFrame
    public final CoroutineStackFrame getCallerFrame() {
        qa.d<T> dVar = this.f14403g;
        if (dVar instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) dVar;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // td.p1
    public void o(Object obj) {
        qa.d b10;
        b10 = IntrinsicsJvm.b(this.f14403g);
        f.c(b10, td.z.a(obj, this.f14403g), null, 2, null);
    }

    @Override // td.AbstractCoroutine
    protected void z0(Object obj) {
        qa.d<T> dVar = this.f14403g;
        dVar.resumeWith(td.z.a(obj, dVar));
    }
}
