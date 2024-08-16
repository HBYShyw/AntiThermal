package hd;

import gd.f1;
import gd.g0;
import gd.v1;
import hd.KotlinTypePreparator;
import sc.OverridingUtil;
import za.DefaultConstructorMarker;

/* compiled from: NewKotlinTypeChecker.kt */
/* loaded from: classes2.dex */
public final class m implements l {

    /* renamed from: c, reason: collision with root package name */
    private final g f12236c;

    /* renamed from: d, reason: collision with root package name */
    private final KotlinTypePreparator f12237d;

    /* renamed from: e, reason: collision with root package name */
    private final OverridingUtil f12238e;

    public m(g gVar, KotlinTypePreparator kotlinTypePreparator) {
        za.k.e(gVar, "kotlinTypeRefiner");
        za.k.e(kotlinTypePreparator, "kotlinTypePreparator");
        this.f12236c = gVar;
        this.f12237d = kotlinTypePreparator;
        OverridingUtil m10 = OverridingUtil.m(c());
        za.k.d(m10, "createWithTypeRefiner(kotlinTypeRefiner)");
        this.f12238e = m10;
    }

    @Override // hd.l
    public OverridingUtil a() {
        return this.f12238e;
    }

    @Override // hd.KotlinTypeChecker
    public boolean b(g0 g0Var, g0 g0Var2) {
        za.k.e(g0Var, "subtype");
        za.k.e(g0Var2, "supertype");
        return g(ClassicTypeCheckerState.b(true, false, null, f(), c(), 6, null), g0Var.Z0(), g0Var2.Z0());
    }

    @Override // hd.l
    public g c() {
        return this.f12236c;
    }

    @Override // hd.KotlinTypeChecker
    public boolean d(g0 g0Var, g0 g0Var2) {
        za.k.e(g0Var, "a");
        za.k.e(g0Var2, "b");
        return e(ClassicTypeCheckerState.b(false, false, null, f(), c(), 6, null), g0Var.Z0(), g0Var2.Z0());
    }

    public final boolean e(f1 f1Var, v1 v1Var, v1 v1Var2) {
        za.k.e(f1Var, "<this>");
        za.k.e(v1Var, "a");
        za.k.e(v1Var2, "b");
        return gd.f.f11759a.k(f1Var, v1Var, v1Var2);
    }

    public KotlinTypePreparator f() {
        return this.f12237d;
    }

    public final boolean g(f1 f1Var, v1 v1Var, v1 v1Var2) {
        za.k.e(f1Var, "<this>");
        za.k.e(v1Var, "subType");
        za.k.e(v1Var2, "superType");
        return gd.f.t(gd.f.f11759a, f1Var, v1Var, v1Var2, false, 8, null);
    }

    public /* synthetic */ m(g gVar, KotlinTypePreparator kotlinTypePreparator, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(gVar, (i10 & 2) != 0 ? KotlinTypePreparator.a.f12214a : kotlinTypePreparator);
    }
}
