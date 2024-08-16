package vb;

import java.lang.annotation.Annotation;
import oc.Name;

/* compiled from: ReflectJavaAnnotationArguments.kt */
/* loaded from: classes2.dex */
public final class g extends f implements fc.c {

    /* renamed from: c, reason: collision with root package name */
    private final Annotation f19234c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g(Name name, Annotation annotation) {
        super(name, null);
        za.k.e(annotation, "annotation");
        this.f19234c = annotation;
    }

    @Override // fc.c
    public fc.a a() {
        return new ReflectJavaAnnotation(this.f19234c);
    }
}
