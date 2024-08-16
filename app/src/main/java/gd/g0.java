package gd;

import java.util.List;
import za.DefaultConstructorMarker;

/* compiled from: KotlinType.kt */
/* loaded from: classes2.dex */
public abstract class g0 implements qb.a, kd.i {

    /* renamed from: e, reason: collision with root package name */
    private int f11812e;

    private g0() {
    }

    public /* synthetic */ g0(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    private final int T0() {
        if (i0.a(this)) {
            return super.hashCode();
        }
        return (((W0().hashCode() * 31) + U0().hashCode()) * 31) + (X0() ? 1 : 0);
    }

    public abstract List<TypeProjection> U0();

    public abstract c1 V0();

    public abstract TypeConstructor W0();

    public abstract boolean X0();

    public abstract g0 Y0(hd.g gVar);

    public abstract v1 Z0();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof g0)) {
            return false;
        }
        g0 g0Var = (g0) obj;
        return X0() == g0Var.X0() && hd.r.f12242a.a(Z0(), g0Var.Z0());
    }

    public final int hashCode() {
        int i10 = this.f11812e;
        if (i10 != 0) {
            return i10;
        }
        int T0 = T0();
        this.f11812e = T0;
        return T0;
    }

    @Override // qb.a
    public qb.g i() {
        return k.a(V0());
    }

    public abstract zc.h u();
}
