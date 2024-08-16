package gd;

import id.ErrorScope;
import id.ThrowingScope;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: KotlinTypeFactory.kt */
/* loaded from: classes2.dex */
public final class p0 extends o0 {

    /* renamed from: f, reason: collision with root package name */
    private final TypeConstructor f11863f;

    /* renamed from: g, reason: collision with root package name */
    private final List<TypeProjection> f11864g;

    /* renamed from: h, reason: collision with root package name */
    private final boolean f11865h;

    /* renamed from: i, reason: collision with root package name */
    private final zc.h f11866i;

    /* renamed from: j, reason: collision with root package name */
    private final ya.l<hd.g, o0> f11867j;

    /* JADX WARN: Multi-variable type inference failed */
    public p0(TypeConstructor typeConstructor, List<? extends TypeProjection> list, boolean z10, zc.h hVar, ya.l<? super hd.g, ? extends o0> lVar) {
        za.k.e(typeConstructor, "constructor");
        za.k.e(list, "arguments");
        za.k.e(hVar, "memberScope");
        za.k.e(lVar, "refinedTypeFactory");
        this.f11863f = typeConstructor;
        this.f11864g = list;
        this.f11865h = z10;
        this.f11866i = hVar;
        this.f11867j = lVar;
        if (!(u() instanceof ErrorScope) || (u() instanceof ThrowingScope)) {
            return;
        }
        throw new IllegalStateException("SimpleTypeImpl should not be created for error type: " + u() + '\n' + W0());
    }

    @Override // gd.g0
    public List<TypeProjection> U0() {
        return this.f11864g;
    }

    @Override // gd.g0
    public c1 V0() {
        return c1.f11749f.h();
    }

    @Override // gd.g0
    public TypeConstructor W0() {
        return this.f11863f;
    }

    @Override // gd.g0
    public boolean X0() {
        return this.f11865h;
    }

    @Override // gd.v1
    /* renamed from: d1 */
    public o0 a1(boolean z10) {
        o0 k0Var;
        if (z10 == X0()) {
            return this;
        }
        if (z10) {
            k0Var = new m0(this);
        } else {
            k0Var = new k0(this);
        }
        return k0Var;
    }

    @Override // gd.v1
    /* renamed from: e1 */
    public o0 c1(c1 c1Var) {
        za.k.e(c1Var, "newAttributes");
        return c1Var.isEmpty() ? this : new q0(this, c1Var);
    }

    @Override // gd.v1
    /* renamed from: f1, reason: merged with bridge method [inline-methods] */
    public o0 g1(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        o0 invoke = this.f11867j.invoke(gVar);
        return invoke == null ? this : invoke;
    }

    @Override // gd.g0
    public zc.h u() {
        return this.f11866i;
    }
}
