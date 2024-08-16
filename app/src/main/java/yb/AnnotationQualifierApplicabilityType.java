package yb;

/* compiled from: AnnotationQualifierApplicabilityType.kt */
/* renamed from: yb.b, reason: use source file name */
/* loaded from: classes2.dex */
public enum AnnotationQualifierApplicabilityType {
    METHOD_RETURN_TYPE("METHOD"),
    VALUE_PARAMETER("PARAMETER"),
    FIELD("FIELD"),
    TYPE_USE("TYPE_USE"),
    TYPE_PARAMETER_BOUNDS("TYPE_USE"),
    TYPE_PARAMETER("TYPE_PARAMETER");


    /* renamed from: e, reason: collision with root package name */
    private final String f20017e;

    AnnotationQualifierApplicabilityType(String str) {
        this.f20017e = str;
    }

    public final String b() {
        return this.f20017e;
    }
}
