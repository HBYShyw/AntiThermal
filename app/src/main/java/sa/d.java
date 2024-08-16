package sa;

import qa.ContinuationInterceptor;
import qa.g;

/* compiled from: ContinuationImpl.kt */
/* loaded from: classes2.dex */
public abstract class d extends a {

    /* renamed from: f, reason: collision with root package name */
    private final qa.g f18193f;

    /* renamed from: g, reason: collision with root package name */
    private transient qa.d<Object> f18194g;

    public d(qa.d<Object> dVar, qa.g gVar) {
        super(dVar);
        this.f18193f = gVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sa.a
    public void b() {
        qa.d<?> dVar = this.f18194g;
        if (dVar != null && dVar != this) {
            g.b c10 = getContext().c(ContinuationInterceptor.f17170a);
            za.k.b(c10);
            ((ContinuationInterceptor) c10).u(dVar);
        }
        this.f18194g = c.f18192e;
    }

    @Override // qa.d
    public qa.g getContext() {
        qa.g gVar = this.f18193f;
        za.k.b(gVar);
        return gVar;
    }

    public final qa.d<Object> intercepted() {
        qa.d<Object> dVar = this.f18194g;
        if (dVar == null) {
            ContinuationInterceptor continuationInterceptor = (ContinuationInterceptor) getContext().c(ContinuationInterceptor.f17170a);
            if (continuationInterceptor == null || (dVar = continuationInterceptor.L(this)) == null) {
                dVar = this;
            }
            this.f18194g = dVar;
        }
        return dVar;
    }

    public d(qa.d<Object> dVar) {
        this(dVar, dVar != null ? dVar.getContext() : null);
    }
}
