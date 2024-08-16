package gd;

/* compiled from: TypeProjectionImpl.java */
/* renamed from: gd.m1, reason: use source file name */
/* loaded from: classes2.dex */
public class TypeProjectionImpl extends TypeProjectionBase {

    /* renamed from: a, reason: collision with root package name */
    private final Variance f11854a;

    /* renamed from: b, reason: collision with root package name */
    private final g0 f11855b;

    public TypeProjectionImpl(Variance variance, g0 g0Var) {
        if (variance == null) {
            c(0);
        }
        if (g0Var == null) {
            c(1);
        }
        this.f11854a = variance;
        this.f11855b = g0Var;
    }

    private static /* synthetic */ void c(int i10) {
        String str = (i10 == 4 || i10 == 5) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 4 || i10 == 5) ? 2 : 3];
        switch (i10) {
            case 1:
            case 2:
            case 3:
                objArr[0] = "type";
                break;
            case 4:
            case 5:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/types/TypeProjectionImpl";
                break;
            case 6:
                objArr[0] = "kotlinTypeRefiner";
                break;
            default:
                objArr[0] = "projection";
                break;
        }
        if (i10 == 4) {
            objArr[1] = "getProjectionKind";
        } else if (i10 != 5) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/types/TypeProjectionImpl";
        } else {
            objArr[1] = "getType";
        }
        if (i10 == 3) {
            objArr[2] = "replaceType";
        } else if (i10 != 4 && i10 != 5) {
            if (i10 != 6) {
                objArr[2] = "<init>";
            } else {
                objArr[2] = "refine";
            }
        }
        String format = String.format(str, objArr);
        if (i10 != 4 && i10 != 5) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    @Override // gd.TypeProjection
    public Variance a() {
        Variance variance = this.f11854a;
        if (variance == null) {
            c(4);
        }
        return variance;
    }

    @Override // gd.TypeProjection
    public boolean b() {
        return false;
    }

    @Override // gd.TypeProjection
    public g0 getType() {
        g0 g0Var = this.f11855b;
        if (g0Var == null) {
            c(5);
        }
        return g0Var;
    }

    @Override // gd.TypeProjection
    public TypeProjection u(hd.g gVar) {
        if (gVar == null) {
            c(6);
        }
        return new TypeProjectionImpl(this.f11854a, gVar.a(this.f11855b));
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public TypeProjectionImpl(g0 g0Var) {
        this(Variance.INVARIANT, g0Var);
        if (g0Var == null) {
            c(2);
        }
    }
}
