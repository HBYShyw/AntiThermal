package gd;

import za.DefaultConstructorMarker;

/* compiled from: TypeSubstitution.kt */
/* loaded from: classes2.dex */
public abstract class n1 {

    /* renamed from: a, reason: collision with root package name */
    public static final b f11856a = new b(null);

    /* renamed from: b, reason: collision with root package name */
    public static final n1 f11857b = new a();

    /* compiled from: TypeSubstitution.kt */
    /* loaded from: classes2.dex */
    public static final class a extends n1 {
        a() {
        }

        @Override // gd.n1
        public /* bridge */ /* synthetic */ TypeProjection e(g0 g0Var) {
            return (TypeProjection) i(g0Var);
        }

        @Override // gd.n1
        public boolean f() {
            return true;
        }

        public Void i(g0 g0Var) {
            za.k.e(g0Var, "key");
            return null;
        }

        public String toString() {
            return "Empty TypeSubstitution";
        }
    }

    /* compiled from: TypeSubstitution.kt */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: TypeSubstitution.kt */
    /* loaded from: classes2.dex */
    public static final class c extends n1 {
        c() {
        }

        @Override // gd.n1
        public boolean a() {
            return false;
        }

        @Override // gd.n1
        public boolean b() {
            return false;
        }

        @Override // gd.n1
        public qb.g d(qb.g gVar) {
            za.k.e(gVar, "annotations");
            return n1.this.d(gVar);
        }

        @Override // gd.n1
        public TypeProjection e(g0 g0Var) {
            za.k.e(g0Var, "key");
            return n1.this.e(g0Var);
        }

        @Override // gd.n1
        public boolean f() {
            return n1.this.f();
        }

        @Override // gd.n1
        public g0 g(g0 g0Var, Variance variance) {
            za.k.e(g0Var, "topLevelType");
            za.k.e(variance, "position");
            return n1.this.g(g0Var, variance);
        }
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public final TypeSubstitutor c() {
        TypeSubstitutor g6 = TypeSubstitutor.g(this);
        za.k.d(g6, "create(this)");
        return g6;
    }

    public qb.g d(qb.g gVar) {
        za.k.e(gVar, "annotations");
        return gVar;
    }

    public abstract TypeProjection e(g0 g0Var);

    public boolean f() {
        return false;
    }

    public g0 g(g0 g0Var, Variance variance) {
        za.k.e(g0Var, "topLevelType");
        za.k.e(variance, "position");
        return g0Var;
    }

    public final n1 h() {
        return new c();
    }
}
