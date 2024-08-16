package yb;

import java.util.Map;
import kotlin.collections.m0;
import oc.FqName;
import yb.JavaNullabilityAnnotationsStatus;

/* compiled from: JavaNullabilityAnnotationSettings.kt */
/* loaded from: classes2.dex */
public final class v {

    /* renamed from: a, reason: collision with root package name */
    private static final FqName f20136a;

    /* renamed from: b, reason: collision with root package name */
    private static final FqName f20137b;

    /* renamed from: c, reason: collision with root package name */
    private static final FqName f20138c;

    /* renamed from: d, reason: collision with root package name */
    private static final String f20139d;

    /* renamed from: e, reason: collision with root package name */
    private static final FqName[] f20140e;

    /* renamed from: f, reason: collision with root package name */
    private static final d0<JavaNullabilityAnnotationsStatus> f20141f;

    /* renamed from: g, reason: collision with root package name */
    private static final JavaNullabilityAnnotationsStatus f20142g;

    static {
        Map l10;
        FqName fqName = new FqName("org.jspecify.nullness");
        f20136a = fqName;
        FqName fqName2 = new FqName("io.reactivex.rxjava3.annotations");
        f20137b = fqName2;
        FqName fqName3 = new FqName("org.checkerframework.checker.nullness.compatqual");
        f20138c = fqName3;
        String b10 = fqName2.b();
        za.k.d(b10, "RXJAVA3_ANNOTATIONS_PACKAGE.asString()");
        f20139d = b10;
        f20140e = new FqName[]{new FqName(b10 + ".Nullable"), new FqName(b10 + ".NonNull")};
        FqName fqName4 = new FqName("org.jetbrains.annotations");
        JavaNullabilityAnnotationsStatus.a aVar = JavaNullabilityAnnotationsStatus.f20143d;
        FqName fqName5 = new FqName("androidx.annotation.RecentlyNullable");
        ReportLevel reportLevel = ReportLevel.WARN;
        ma.f fVar = new ma.f(1, 9);
        ReportLevel reportLevel2 = ReportLevel.STRICT;
        l10 = m0.l(ma.u.a(fqName4, aVar.a()), ma.u.a(new FqName("androidx.annotation"), aVar.a()), ma.u.a(new FqName("android.support.annotation"), aVar.a()), ma.u.a(new FqName("android.annotation"), aVar.a()), ma.u.a(new FqName("com.android.annotations"), aVar.a()), ma.u.a(new FqName("org.eclipse.jdt.annotation"), aVar.a()), ma.u.a(new FqName("org.checkerframework.checker.nullness.qual"), aVar.a()), ma.u.a(fqName3, aVar.a()), ma.u.a(new FqName("javax.annotation"), aVar.a()), ma.u.a(new FqName("edu.umd.cs.findbugs.annotations"), aVar.a()), ma.u.a(new FqName("io.reactivex.annotations"), aVar.a()), ma.u.a(fqName5, new JavaNullabilityAnnotationsStatus(reportLevel, null, null, 4, null)), ma.u.a(new FqName("androidx.annotation.RecentlyNonNull"), new JavaNullabilityAnnotationsStatus(reportLevel, null, null, 4, null)), ma.u.a(new FqName("lombok"), aVar.a()), ma.u.a(fqName, new JavaNullabilityAnnotationsStatus(reportLevel, fVar, reportLevel2)), ma.u.a(fqName2, new JavaNullabilityAnnotationsStatus(reportLevel, new ma.f(1, 8), reportLevel2)));
        f20141f = new e0(l10);
        f20142g = new JavaNullabilityAnnotationsStatus(reportLevel, null, null, 4, null);
    }

    public static final Jsr305Settings a(ma.f fVar) {
        ReportLevel c10;
        za.k.e(fVar, "configuredKotlinVersion");
        JavaNullabilityAnnotationsStatus javaNullabilityAnnotationsStatus = f20142g;
        if (javaNullabilityAnnotationsStatus.d() != null && javaNullabilityAnnotationsStatus.d().compareTo(fVar) <= 0) {
            c10 = javaNullabilityAnnotationsStatus.b();
        } else {
            c10 = javaNullabilityAnnotationsStatus.c();
        }
        ReportLevel reportLevel = c10;
        return new Jsr305Settings(reportLevel, c(reportLevel), null, 4, null);
    }

    public static /* synthetic */ Jsr305Settings b(ma.f fVar, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            fVar = ma.f.f15168j;
        }
        return a(fVar);
    }

    public static final ReportLevel c(ReportLevel reportLevel) {
        za.k.e(reportLevel, "globalReportLevel");
        if (reportLevel == ReportLevel.WARN) {
            return null;
        }
        return reportLevel;
    }

    public static final ReportLevel d(FqName fqName) {
        za.k.e(fqName, "annotationFqName");
        return h(fqName, d0.f20063a.a(), null, 4, null);
    }

    public static final FqName e() {
        return f20136a;
    }

    public static final FqName[] f() {
        return f20140e;
    }

    public static final ReportLevel g(FqName fqName, d0<? extends ReportLevel> d0Var, ma.f fVar) {
        za.k.e(fqName, "annotation");
        za.k.e(d0Var, "configuredReportLevels");
        za.k.e(fVar, "configuredKotlinVersion");
        ReportLevel a10 = d0Var.a(fqName);
        if (a10 != null) {
            return a10;
        }
        JavaNullabilityAnnotationsStatus a11 = f20141f.a(fqName);
        if (a11 == null) {
            return ReportLevel.IGNORE;
        }
        if (a11.d() != null && a11.d().compareTo(fVar) <= 0) {
            return a11.b();
        }
        return a11.c();
    }

    public static /* synthetic */ ReportLevel h(FqName fqName, d0 d0Var, ma.f fVar, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            fVar = new ma.f(1, 7, 20);
        }
        return g(fqName, d0Var, fVar);
    }
}
