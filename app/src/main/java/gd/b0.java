package gd;

import hd.KotlinTypeChecker;
import ma.NoWhenBranchMatchedException;
import pb.TypeParameterDescriptor;
import za.DefaultConstructorMarker;

/* compiled from: flexibleTypes.kt */
/* loaded from: classes2.dex */
public final class b0 extends a0 implements n {

    /* renamed from: i, reason: collision with root package name */
    public static final a f11743i = new a(null);

    /* renamed from: j, reason: collision with root package name */
    public static boolean f11744j;

    /* renamed from: h, reason: collision with root package name */
    private boolean f11745h;

    /* compiled from: flexibleTypes.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public b0(o0 o0Var, o0 o0Var2) {
        super(o0Var, o0Var2);
        za.k.e(o0Var, "lowerBound");
        za.k.e(o0Var2, "upperBound");
    }

    private final void i1() {
        if (!f11744j || this.f11745h) {
            return;
        }
        this.f11745h = true;
        d0.b(e1());
        d0.b(f1());
        za.k.a(e1(), f1());
        KotlinTypeChecker.f12213a.b(e1(), f1());
    }

    @Override // gd.n
    public boolean J0() {
        return (e1().W0().v() instanceof TypeParameterDescriptor) && za.k.a(e1().W0(), f1().W0());
    }

    @Override // gd.n
    public g0 P(g0 g0Var) {
        v1 d10;
        za.k.e(g0Var, "replacement");
        v1 Z0 = g0Var.Z0();
        if (Z0 instanceof a0) {
            d10 = Z0;
        } else {
            if (!(Z0 instanceof o0)) {
                throw new NoWhenBranchMatchedException();
            }
            o0 o0Var = (o0) Z0;
            d10 = h0.d(o0Var, o0Var.a1(true));
        }
        return u1.b(d10, Z0);
    }

    @Override // gd.v1
    public v1 a1(boolean z10) {
        return h0.d(e1().a1(z10), f1().a1(z10));
    }

    @Override // gd.v1
    public v1 c1(c1 c1Var) {
        za.k.e(c1Var, "newAttributes");
        return h0.d(e1().c1(c1Var), f1().c1(c1Var));
    }

    @Override // gd.a0
    public o0 d1() {
        i1();
        return e1();
    }

    @Override // gd.a0
    public String g1(rc.c cVar, rc.f fVar) {
        za.k.e(cVar, "renderer");
        za.k.e(fVar, "options");
        if (fVar.m()) {
            return '(' + cVar.w(e1()) + ".." + cVar.w(f1()) + ')';
        }
        return cVar.t(cVar.w(e1()), cVar.w(f1()), ld.a.i(this));
    }

    @Override // gd.v1
    /* renamed from: h1, reason: merged with bridge method [inline-methods] */
    public a0 g1(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        g0 a10 = gVar.a(e1());
        za.k.c(a10, "null cannot be cast to non-null type org.jetbrains.kotlin.types.SimpleType");
        g0 a11 = gVar.a(f1());
        za.k.c(a11, "null cannot be cast to non-null type org.jetbrains.kotlin.types.SimpleType");
        return new b0((o0) a10, (o0) a11);
    }

    @Override // gd.a0
    public String toString() {
        return '(' + e1() + ".." + f1() + ')';
    }
}
