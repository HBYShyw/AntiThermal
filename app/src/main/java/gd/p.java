package gd;

import pb.ClassifierDescriptor;
import pb.TypeParameterDescriptor;
import sb.TypeParameterDescriptorImpl;
import za.DefaultConstructorMarker;

/* compiled from: SpecialTypes.kt */
/* loaded from: classes2.dex */
public final class p extends r implements n, kd.e {

    /* renamed from: h, reason: collision with root package name */
    public static final a f11860h = new a(null);

    /* renamed from: f, reason: collision with root package name */
    private final o0 f11861f;

    /* renamed from: g, reason: collision with root package name */
    private final boolean f11862g;

    /* compiled from: SpecialTypes.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final boolean a(v1 v1Var) {
            return (v1Var.W0() instanceof hd.n) || (v1Var.W0().v() instanceof TypeParameterDescriptor) || (v1Var instanceof hd.i) || (v1Var instanceof w0);
        }

        private final boolean c(v1 v1Var, boolean z10) {
            boolean z11 = false;
            if (!a(v1Var)) {
                return false;
            }
            if (v1Var instanceof w0) {
                return s1.l(v1Var);
            }
            ClassifierDescriptor v7 = v1Var.W0().v();
            TypeParameterDescriptorImpl typeParameterDescriptorImpl = v7 instanceof TypeParameterDescriptorImpl ? (TypeParameterDescriptorImpl) v7 : null;
            if (typeParameterDescriptorImpl != null && !typeParameterDescriptorImpl.c1()) {
                z11 = true;
            }
            if (z11) {
                return true;
            }
            if (z10 && (v1Var.W0().v() instanceof TypeParameterDescriptor)) {
                return s1.l(v1Var);
            }
            return !hd.o.f12239a.a(v1Var);
        }

        public final p b(v1 v1Var, boolean z10) {
            za.k.e(v1Var, "type");
            DefaultConstructorMarker defaultConstructorMarker = null;
            if (v1Var instanceof p) {
                return (p) v1Var;
            }
            if (!c(v1Var, z10)) {
                return null;
            }
            if (v1Var instanceof a0) {
                a0 a0Var = (a0) v1Var;
                za.k.a(a0Var.e1().W0(), a0Var.f1().W0());
            }
            return new p(d0.c(v1Var).a1(false), z10, defaultConstructorMarker);
        }
    }

    private p(o0 o0Var, boolean z10) {
        this.f11861f = o0Var;
        this.f11862g = z10;
    }

    public /* synthetic */ p(o0 o0Var, boolean z10, DefaultConstructorMarker defaultConstructorMarker) {
        this(o0Var, z10);
    }

    @Override // gd.n
    public boolean J0() {
        return (f1().W0() instanceof hd.n) || (f1().W0().v() instanceof TypeParameterDescriptor);
    }

    @Override // gd.n
    public g0 P(g0 g0Var) {
        za.k.e(g0Var, "replacement");
        return s0.e(g0Var.Z0(), this.f11862g);
    }

    @Override // gd.r, gd.g0
    public boolean X0() {
        return false;
    }

    @Override // gd.v1
    /* renamed from: d1 */
    public o0 a1(boolean z10) {
        return z10 ? f1().a1(z10) : this;
    }

    @Override // gd.v1
    /* renamed from: e1 */
    public o0 c1(c1 c1Var) {
        za.k.e(c1Var, "newAttributes");
        return new p(f1().c1(c1Var), this.f11862g);
    }

    @Override // gd.r
    protected o0 f1() {
        return this.f11861f;
    }

    public final o0 i1() {
        return this.f11861f;
    }

    @Override // gd.r
    /* renamed from: j1, reason: merged with bridge method [inline-methods] */
    public p h1(o0 o0Var) {
        za.k.e(o0Var, "delegate");
        return new p(o0Var, this.f11862g);
    }

    @Override // gd.o0
    public String toString() {
        return f1() + " & Any";
    }
}
