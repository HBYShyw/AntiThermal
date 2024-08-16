package gd;

import za.DefaultConstructorMarker;

/* compiled from: DisjointKeysUnionTypeSubstitution.kt */
/* renamed from: gd.u, reason: use source file name */
/* loaded from: classes2.dex */
public final class DisjointKeysUnionTypeSubstitution extends n1 {

    /* renamed from: e, reason: collision with root package name */
    public static final a f11889e = new a(null);

    /* renamed from: c, reason: collision with root package name */
    private final n1 f11890c;

    /* renamed from: d, reason: collision with root package name */
    private final n1 f11891d;

    /* compiled from: DisjointKeysUnionTypeSubstitution.kt */
    /* renamed from: gd.u$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final n1 a(n1 n1Var, n1 n1Var2) {
            za.k.e(n1Var, "first");
            za.k.e(n1Var2, "second");
            return n1Var.f() ? n1Var2 : n1Var2.f() ? n1Var : new DisjointKeysUnionTypeSubstitution(n1Var, n1Var2, null);
        }
    }

    private DisjointKeysUnionTypeSubstitution(n1 n1Var, n1 n1Var2) {
        this.f11890c = n1Var;
        this.f11891d = n1Var2;
    }

    public /* synthetic */ DisjointKeysUnionTypeSubstitution(n1 n1Var, n1 n1Var2, DefaultConstructorMarker defaultConstructorMarker) {
        this(n1Var, n1Var2);
    }

    public static final n1 i(n1 n1Var, n1 n1Var2) {
        return f11889e.a(n1Var, n1Var2);
    }

    @Override // gd.n1
    public boolean a() {
        return this.f11890c.a() || this.f11891d.a();
    }

    @Override // gd.n1
    public boolean b() {
        return this.f11890c.b() || this.f11891d.b();
    }

    @Override // gd.n1
    public qb.g d(qb.g gVar) {
        za.k.e(gVar, "annotations");
        return this.f11891d.d(this.f11890c.d(gVar));
    }

    @Override // gd.n1
    public TypeProjection e(g0 g0Var) {
        za.k.e(g0Var, "key");
        TypeProjection e10 = this.f11890c.e(g0Var);
        return e10 == null ? this.f11891d.e(g0Var) : e10;
    }

    @Override // gd.n1
    public boolean f() {
        return false;
    }

    @Override // gd.n1
    public g0 g(g0 g0Var, Variance variance) {
        za.k.e(g0Var, "topLevelType");
        za.k.e(variance, "position");
        return this.f11891d.g(this.f11890c.g(g0Var, variance), variance);
    }
}
