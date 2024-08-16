package yb;

import java.util.EnumMap;

/* compiled from: JavaTypeQualifiersByElementType.kt */
/* renamed from: yb.y, reason: use source file name */
/* loaded from: classes2.dex */
public final class JavaTypeQualifiersByElementType {

    /* renamed from: a, reason: collision with root package name */
    private final EnumMap<AnnotationQualifierApplicabilityType, r> f20154a;

    public JavaTypeQualifiersByElementType(EnumMap<AnnotationQualifierApplicabilityType, r> enumMap) {
        za.k.e(enumMap, "defaultQualifiers");
        this.f20154a = enumMap;
    }

    public final r a(AnnotationQualifierApplicabilityType annotationQualifierApplicabilityType) {
        return this.f20154a.get(annotationQualifierApplicabilityType);
    }

    public final EnumMap<AnnotationQualifierApplicabilityType, r> b() {
        return this.f20154a;
    }
}
