package qb;

import od.capitalizeDecapitalize;
import za.DefaultConstructorMarker;

/* compiled from: AnnotationUseSiteTarget.kt */
/* renamed from: qb.e, reason: use source file name */
/* loaded from: classes2.dex */
public enum AnnotationUseSiteTarget {
    FIELD(null, 1, null),
    FILE(null, 1, null),
    PROPERTY(null, 1, null),
    PROPERTY_GETTER("get"),
    PROPERTY_SETTER("set"),
    RECEIVER(null, 1, null),
    CONSTRUCTOR_PARAMETER("param"),
    SETTER_PARAMETER("setparam"),
    PROPERTY_DELEGATE_FIELD("delegate");


    /* renamed from: e, reason: collision with root package name */
    private final String f17188e;

    AnnotationUseSiteTarget(String str) {
        this.f17188e = str == null ? capitalizeDecapitalize.f(name()) : str;
    }

    public final String b() {
        return this.f17188e;
    }

    /* synthetic */ AnnotationUseSiteTarget(String str, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? null : str);
    }
}
