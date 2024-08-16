package sa;

/* compiled from: ContinuationImpl.kt */
/* loaded from: classes2.dex */
public final class c implements qa.d<Object> {

    /* renamed from: e, reason: collision with root package name */
    public static final c f18192e = new c();

    private c() {
    }

    @Override // qa.d
    public qa.g getContext() {
        throw new IllegalStateException("This continuation is already complete".toString());
    }

    @Override // qa.d
    public void resumeWith(Object obj) {
        throw new IllegalStateException("This continuation is already complete".toString());
    }

    public String toString() {
        return "This continuation is already complete";
    }
}
