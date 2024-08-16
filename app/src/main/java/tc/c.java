package tc;

import gd.TypeProjection;
import gd.Variance;
import gd.g0;
import hd.g;
import hd.j;
import java.util.Collection;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.r;
import mb.KotlinBuiltIns;
import pb.ClassifierDescriptor;
import pb.TypeParameterDescriptor;
import za.k;

/* compiled from: CapturedTypeConstructor.kt */
/* loaded from: classes2.dex */
public final class c implements b {

    /* renamed from: a, reason: collision with root package name */
    private final TypeProjection f18711a;

    /* renamed from: b, reason: collision with root package name */
    private j f18712b;

    public c(TypeProjection typeProjection) {
        k.e(typeProjection, "projection");
        this.f18711a = typeProjection;
        b().a();
        Variance variance = Variance.INVARIANT;
    }

    @Override // tc.b
    public TypeProjection b() {
        return this.f18711a;
    }

    public Void c() {
        return null;
    }

    public final j d() {
        return this.f18712b;
    }

    @Override // gd.TypeConstructor
    /* renamed from: e, reason: merged with bridge method [inline-methods] */
    public c u(g gVar) {
        k.e(gVar, "kotlinTypeRefiner");
        TypeProjection u7 = b().u(gVar);
        k.d(u7, "projection.refine(kotlinTypeRefiner)");
        return new c(u7);
    }

    public final void f(j jVar) {
        this.f18712b = jVar;
    }

    @Override // gd.TypeConstructor
    public List<TypeParameterDescriptor> getParameters() {
        List<TypeParameterDescriptor> j10;
        j10 = r.j();
        return j10;
    }

    @Override // gd.TypeConstructor
    public Collection<g0> q() {
        g0 I;
        List e10;
        if (b().a() == Variance.OUT_VARIANCE) {
            I = b().getType();
        } else {
            I = t().I();
        }
        k.d(I, "if (projection.projectio… builtIns.nullableAnyType");
        e10 = CollectionsJVM.e(I);
        return e10;
    }

    @Override // gd.TypeConstructor
    public KotlinBuiltIns t() {
        KotlinBuiltIns t7 = b().getType().W0().t();
        k.d(t7, "projection.type.constructor.builtIns");
        return t7;
    }

    public String toString() {
        return "CapturedTypeConstructor(" + b() + ')';
    }

    @Override // gd.TypeConstructor
    public /* bridge */ /* synthetic */ ClassifierDescriptor v() {
        return (ClassifierDescriptor) c();
    }

    @Override // gd.TypeConstructor
    public boolean w() {
        return false;
    }
}
