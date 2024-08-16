package sa;

/* compiled from: ContinuationImpl.kt */
/* loaded from: classes2.dex */
public abstract class j extends a {
    public j(qa.d<Object> dVar) {
        super(dVar);
        if (dVar != null) {
            if (!(dVar.getContext() == qa.h.f17173e)) {
                throw new IllegalArgumentException("Coroutines with restricted suspension must have EmptyCoroutineContext".toString());
            }
        }
    }

    @Override // qa.d
    public qa.g getContext() {
        return qa.h.f17173e;
    }
}
