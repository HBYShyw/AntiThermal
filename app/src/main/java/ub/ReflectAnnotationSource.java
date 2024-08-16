package ub;

import java.lang.annotation.Annotation;
import pb.SourceElement;
import pb.b1;

/* compiled from: ReflectAnnotationSource.kt */
/* renamed from: ub.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectAnnotationSource implements SourceElement {

    /* renamed from: b, reason: collision with root package name */
    private final Annotation f18971b;

    public ReflectAnnotationSource(Annotation annotation) {
        za.k.e(annotation, "annotation");
        this.f18971b = annotation;
    }

    @Override // pb.SourceElement
    public b1 a() {
        b1 b1Var = b1.f16671a;
        za.k.d(b1Var, "NO_SOURCE_FILE");
        return b1Var;
    }

    public final Annotation d() {
        return this.f18971b;
    }
}
