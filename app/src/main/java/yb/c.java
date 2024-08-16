package yb;

import gc.NullabilityQualifierWithMigrationStatus;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.MapsJVM;
import kotlin.collections.m0;
import kotlin.collections.s0;
import oc.FqName;

/* compiled from: AnnotationQualifiersFqNames.kt */
/* loaded from: classes2.dex */
public final class c {

    /* renamed from: a, reason: collision with root package name */
    private static final FqName f20040a = new FqName("javax.annotation.meta.TypeQualifierNickname");

    /* renamed from: b, reason: collision with root package name */
    private static final FqName f20041b = new FqName("javax.annotation.meta.TypeQualifier");

    /* renamed from: c, reason: collision with root package name */
    private static final FqName f20042c = new FqName("javax.annotation.meta.TypeQualifierDefault");

    /* renamed from: d, reason: collision with root package name */
    private static final FqName f20043d = new FqName("kotlin.annotations.jvm.UnderMigration");

    /* renamed from: e, reason: collision with root package name */
    private static final List<AnnotationQualifierApplicabilityType> f20044e;

    /* renamed from: f, reason: collision with root package name */
    private static final Map<FqName, r> f20045f;

    /* renamed from: g, reason: collision with root package name */
    private static final Map<FqName, r> f20046g;

    /* renamed from: h, reason: collision with root package name */
    private static final Set<FqName> f20047h;

    static {
        List<AnnotationQualifierApplicabilityType> m10;
        Map<FqName, r> f10;
        List e10;
        List e11;
        Map l10;
        Map<FqName, r> n10;
        Set<FqName> h10;
        AnnotationQualifierApplicabilityType annotationQualifierApplicabilityType = AnnotationQualifierApplicabilityType.VALUE_PARAMETER;
        m10 = kotlin.collections.r.m(AnnotationQualifierApplicabilityType.FIELD, AnnotationQualifierApplicabilityType.METHOD_RETURN_TYPE, annotationQualifierApplicabilityType, AnnotationQualifierApplicabilityType.TYPE_PARAMETER_BOUNDS, AnnotationQualifierApplicabilityType.TYPE_USE);
        f20044e = m10;
        FqName i10 = c0.i();
        gc.h hVar = gc.h.NOT_NULL;
        f10 = MapsJVM.f(ma.u.a(i10, new r(new NullabilityQualifierWithMigrationStatus(hVar, false, 2, null), m10, false)));
        f20045f = f10;
        FqName fqName = new FqName("javax.annotation.ParametersAreNullableByDefault");
        NullabilityQualifierWithMigrationStatus nullabilityQualifierWithMigrationStatus = new NullabilityQualifierWithMigrationStatus(gc.h.NULLABLE, false, 2, null);
        e10 = CollectionsJVM.e(annotationQualifierApplicabilityType);
        FqName fqName2 = new FqName("javax.annotation.ParametersAreNonnullByDefault");
        NullabilityQualifierWithMigrationStatus nullabilityQualifierWithMigrationStatus2 = new NullabilityQualifierWithMigrationStatus(hVar, false, 2, null);
        e11 = CollectionsJVM.e(annotationQualifierApplicabilityType);
        l10 = m0.l(ma.u.a(fqName, new r(nullabilityQualifierWithMigrationStatus, e10, false, 4, null)), ma.u.a(fqName2, new r(nullabilityQualifierWithMigrationStatus2, e11, false, 4, null)));
        n10 = m0.n(l10, f10);
        f20046g = n10;
        h10 = s0.h(c0.f(), c0.e());
        f20047h = h10;
    }

    public static final Map<FqName, r> a() {
        return f20046g;
    }

    public static final Set<FqName> b() {
        return f20047h;
    }

    public static final Map<FqName, r> c() {
        return f20045f;
    }

    public static final FqName d() {
        return f20043d;
    }

    public static final FqName e() {
        return f20042c;
    }

    public static final FqName f() {
        return f20041b;
    }

    public static final FqName g() {
        return f20040a;
    }
}
