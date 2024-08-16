package gd;

import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: AnnotationsTypeAttribute.kt */
/* loaded from: classes2.dex */
public final class k {

    /* renamed from: a, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f11848a = {Reflection.g(new PropertyReference1Impl(Reflection.d(k.class, "descriptors"), "annotationsAttribute", "getAnnotationsAttribute(Lorg/jetbrains/kotlin/types/TypeAttributes;)Lorg/jetbrains/kotlin/types/AnnotationsTypeAttribute;"))};

    /* renamed from: b, reason: collision with root package name */
    private static final cb.c f11849b;

    static {
        cb.c c10 = c1.f11749f.c(Reflection.b(j.class));
        za.k.c(c10, "null cannot be cast to non-null type kotlin.properties.ReadOnlyProperty<org.jetbrains.kotlin.types.TypeAttributes, T of org.jetbrains.kotlin.types.TypeAttributes.Companion.attributeAccessor?>");
        f11849b = c10;
    }

    public static final qb.g a(c1 c1Var) {
        qb.g e10;
        za.k.e(c1Var, "<this>");
        j b10 = b(c1Var);
        return (b10 == null || (e10 = b10.e()) == null) ? qb.g.f17195b.b() : e10;
    }

    public static final j b(c1 c1Var) {
        za.k.e(c1Var, "<this>");
        return (j) f11849b.a(c1Var, f11848a[0]);
    }
}
