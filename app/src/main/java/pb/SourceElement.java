package pb;

/* compiled from: SourceElement.java */
/* renamed from: pb.a1, reason: use source file name */
/* loaded from: classes2.dex */
public interface SourceElement {

    /* renamed from: a, reason: collision with root package name */
    public static final SourceElement f16664a = new a();

    /* compiled from: SourceElement.java */
    /* renamed from: pb.a1$a */
    /* loaded from: classes2.dex */
    static class a implements SourceElement {
        a() {
        }

        private static /* synthetic */ void d(int i10) {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "kotlin/reflect/jvm/internal/impl/descriptors/SourceElement$1", "getContainingFile"));
        }

        @Override // pb.SourceElement
        public b1 a() {
            b1 b1Var = b1.f16671a;
            if (b1Var == null) {
                d(0);
            }
            return b1Var;
        }

        public String toString() {
            return "NO_SOURCE";
        }
    }

    b1 a();
}
