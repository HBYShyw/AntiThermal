package yb;

import za.DefaultConstructorMarker;

/* compiled from: JavaNullabilityAnnotationsStatus.kt */
/* renamed from: yb.w, reason: use source file name */
/* loaded from: classes2.dex */
public final class JavaNullabilityAnnotationsStatus {

    /* renamed from: d, reason: collision with root package name */
    public static final a f20143d = new a(null);

    /* renamed from: e, reason: collision with root package name */
    private static final JavaNullabilityAnnotationsStatus f20144e = new JavaNullabilityAnnotationsStatus(ReportLevel.STRICT, null, null, 6, null);

    /* renamed from: a, reason: collision with root package name */
    private final ReportLevel f20145a;

    /* renamed from: b, reason: collision with root package name */
    private final ma.f f20146b;

    /* renamed from: c, reason: collision with root package name */
    private final ReportLevel f20147c;

    /* compiled from: JavaNullabilityAnnotationsStatus.kt */
    /* renamed from: yb.w$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final JavaNullabilityAnnotationsStatus a() {
            return JavaNullabilityAnnotationsStatus.f20144e;
        }
    }

    public JavaNullabilityAnnotationsStatus(ReportLevel reportLevel, ma.f fVar, ReportLevel reportLevel2) {
        za.k.e(reportLevel, "reportLevelBefore");
        za.k.e(reportLevel2, "reportLevelAfter");
        this.f20145a = reportLevel;
        this.f20146b = fVar;
        this.f20147c = reportLevel2;
    }

    public final ReportLevel b() {
        return this.f20147c;
    }

    public final ReportLevel c() {
        return this.f20145a;
    }

    public final ma.f d() {
        return this.f20146b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof JavaNullabilityAnnotationsStatus)) {
            return false;
        }
        JavaNullabilityAnnotationsStatus javaNullabilityAnnotationsStatus = (JavaNullabilityAnnotationsStatus) obj;
        return this.f20145a == javaNullabilityAnnotationsStatus.f20145a && za.k.a(this.f20146b, javaNullabilityAnnotationsStatus.f20146b) && this.f20147c == javaNullabilityAnnotationsStatus.f20147c;
    }

    public int hashCode() {
        int hashCode = this.f20145a.hashCode() * 31;
        ma.f fVar = this.f20146b;
        return ((hashCode + (fVar == null ? 0 : fVar.hashCode())) * 31) + this.f20147c.hashCode();
    }

    public String toString() {
        return "JavaNullabilityAnnotationsStatus(reportLevelBefore=" + this.f20145a + ", sinceVersion=" + this.f20146b + ", reportLevelAfter=" + this.f20147c + ')';
    }

    public /* synthetic */ JavaNullabilityAnnotationsStatus(ReportLevel reportLevel, ma.f fVar, ReportLevel reportLevel2, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(reportLevel, (i10 & 2) != 0 ? new ma.f(1, 0) : fVar, (i10 & 4) != 0 ? reportLevel : reportLevel2);
    }
}
