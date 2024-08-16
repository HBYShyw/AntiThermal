package gd;

import java.util.List;

/* compiled from: KotlinType.kt */
/* loaded from: classes2.dex */
public abstract class x1 extends g0 {
    public x1() {
        super(null);
    }

    @Override // gd.g0
    public List<TypeProjection> U0() {
        return a1().U0();
    }

    @Override // gd.g0
    public c1 V0() {
        return a1().V0();
    }

    @Override // gd.g0
    public TypeConstructor W0() {
        return a1().W0();
    }

    @Override // gd.g0
    public boolean X0() {
        return a1().X0();
    }

    @Override // gd.g0
    public final v1 Z0() {
        g0 a12 = a1();
        while (a12 instanceof x1) {
            a12 = ((x1) a12).a1();
        }
        za.k.c(a12, "null cannot be cast to non-null type org.jetbrains.kotlin.types.UnwrappedType");
        return (v1) a12;
    }

    protected abstract g0 a1();

    public boolean b1() {
        return true;
    }

    public String toString() {
        return b1() ? a1().toString() : "<Not computed yet>";
    }

    @Override // gd.g0
    public zc.h u() {
        return a1().u();
    }
}
