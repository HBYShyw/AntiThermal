package qb;

/* compiled from: AnnotatedImpl.java */
/* renamed from: qb.b, reason: use source file name */
/* loaded from: classes2.dex */
public class AnnotatedImpl implements a {

    /* renamed from: e, reason: collision with root package name */
    private final g f17174e;

    public AnnotatedImpl(g gVar) {
        if (gVar == null) {
            P(0);
        }
        this.f17174e = gVar;
    }

    private static /* synthetic */ void P(int i10) {
        String str = i10 != 1 ? "Argument for @NotNull parameter '%s' of %s.%s must not be null" : "@NotNull method %s.%s must not return null";
        Object[] objArr = new Object[i10 != 1 ? 3 : 2];
        if (i10 != 1) {
            objArr[0] = "annotations";
        } else {
            objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/annotations/AnnotatedImpl";
        }
        if (i10 != 1) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/annotations/AnnotatedImpl";
        } else {
            objArr[1] = "getAnnotations";
        }
        if (i10 != 1) {
            objArr[2] = "<init>";
        }
        String format = String.format(str, objArr);
        if (i10 == 1) {
            throw new IllegalStateException(format);
        }
    }

    @Override // qb.a
    public g i() {
        g gVar = this.f17174e;
        if (gVar == null) {
            P(1);
        }
        return gVar;
    }
}
