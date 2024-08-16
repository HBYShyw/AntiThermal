package vb;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import oc.FqName;

/* compiled from: ReflectJavaAnnotationOwner.kt */
/* loaded from: classes2.dex */
public interface h extends fc.d {
    AnnotatedElement D();

    @Override // fc.d
    List<ReflectJavaAnnotation> i();

    @Override // fc.d
    ReflectJavaAnnotation j(FqName fqName);
}
