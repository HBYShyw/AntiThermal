package zb;

import cc.LazyJavaAnnotationDescriptor;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.Map;
import kotlin.collections.m0;
import ma.u;
import mb.StandardNames;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import qb.AnnotationDescriptor;
import yb.b0;
import za.k;

/* compiled from: JavaAnnotationMapper.kt */
/* loaded from: classes2.dex */
public final class c {

    /* renamed from: a, reason: collision with root package name */
    public static final c f20391a = new c();

    /* renamed from: b, reason: collision with root package name */
    private static final Name f20392b;

    /* renamed from: c, reason: collision with root package name */
    private static final Name f20393c;

    /* renamed from: d, reason: collision with root package name */
    private static final Name f20394d;

    /* renamed from: e, reason: collision with root package name */
    private static final Map<FqName, FqName> f20395e;

    static {
        Map<FqName, FqName> l10;
        Name f10 = Name.f("message");
        k.d(f10, "identifier(\"message\")");
        f20392b = f10;
        Name f11 = Name.f("allowedTargets");
        k.d(f11, "identifier(\"allowedTargets\")");
        f20393c = f11;
        Name f12 = Name.f(ThermalBaseConfig.Item.ATTR_VALUE);
        k.d(f12, "identifier(\"value\")");
        f20394d = f12;
        l10 = m0.l(u.a(StandardNames.a.H, b0.f20021d), u.a(StandardNames.a.L, b0.f20023f), u.a(StandardNames.a.P, b0.f20026i));
        f20395e = l10;
    }

    private c() {
    }

    public static /* synthetic */ AnnotationDescriptor f(c cVar, fc.a aVar, bc.g gVar, boolean z10, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            z10 = false;
        }
        return cVar.e(aVar, gVar, z10);
    }

    public final AnnotationDescriptor a(FqName fqName, fc.d dVar, bc.g gVar) {
        fc.a j10;
        k.e(fqName, "kotlinName");
        k.e(dVar, "annotationOwner");
        k.e(gVar, "c");
        if (k.a(fqName, StandardNames.a.f15337y)) {
            FqName fqName2 = b0.f20025h;
            k.d(fqName2, "DEPRECATED_ANNOTATION");
            fc.a j11 = dVar.j(fqName2);
            if (j11 != null || dVar.k()) {
                return new e(j11, gVar);
            }
        }
        FqName fqName3 = f20395e.get(fqName);
        if (fqName3 == null || (j10 = dVar.j(fqName3)) == null) {
            return null;
        }
        return f(f20391a, j10, gVar, false, 4, null);
    }

    public final Name b() {
        return f20392b;
    }

    public final Name c() {
        return f20394d;
    }

    public final Name d() {
        return f20393c;
    }

    public final AnnotationDescriptor e(fc.a aVar, bc.g gVar, boolean z10) {
        k.e(aVar, "annotation");
        k.e(gVar, "c");
        ClassId e10 = aVar.e();
        if (k.a(e10, ClassId.m(b0.f20021d))) {
            return new i(aVar, gVar);
        }
        if (k.a(e10, ClassId.m(b0.f20023f))) {
            return new h(aVar, gVar);
        }
        if (k.a(e10, ClassId.m(b0.f20026i))) {
            return new b(gVar, aVar, StandardNames.a.P);
        }
        if (k.a(e10, ClassId.m(b0.f20025h))) {
            return null;
        }
        return new LazyJavaAnnotationDescriptor(gVar, aVar, z10);
    }
}
