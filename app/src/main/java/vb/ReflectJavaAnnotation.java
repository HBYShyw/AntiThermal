package vb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import oc.ClassId;
import oc.Name;
import vb.f;
import xa.JvmClassMapping;

/* compiled from: ReflectJavaAnnotation.kt */
/* renamed from: vb.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectJavaAnnotation extends ReflectJavaElement implements fc.a {

    /* renamed from: a, reason: collision with root package name */
    private final Annotation f19231a;

    public ReflectJavaAnnotation(Annotation annotation) {
        za.k.e(annotation, "annotation");
        this.f19231a = annotation;
    }

    @Override // fc.a
    public boolean C() {
        return false;
    }

    public final Annotation W() {
        return this.f19231a;
    }

    @Override // fc.a
    /* renamed from: X, reason: merged with bridge method [inline-methods] */
    public l G() {
        return new l(JvmClassMapping.b(JvmClassMapping.a(this.f19231a)));
    }

    @Override // fc.a
    public Collection<fc.b> b() {
        Method[] declaredMethods = JvmClassMapping.b(JvmClassMapping.a(this.f19231a)).getDeclaredMethods();
        za.k.d(declaredMethods, "annotation.annotationClass.java.declaredMethods");
        ArrayList arrayList = new ArrayList(declaredMethods.length);
        for (Method method : declaredMethods) {
            f.a aVar = f.f19232b;
            Object invoke = method.invoke(this.f19231a, new Object[0]);
            za.k.d(invoke, "method.invoke(annotation)");
            arrayList.add(aVar.a(invoke, Name.f(method.getName())));
        }
        return arrayList;
    }

    @Override // fc.a
    public ClassId e() {
        return reflectClassUtil.a(JvmClassMapping.b(JvmClassMapping.a(this.f19231a)));
    }

    public boolean equals(Object obj) {
        return (obj instanceof ReflectJavaAnnotation) && this.f19231a == ((ReflectJavaAnnotation) obj).f19231a;
    }

    @Override // fc.a
    public boolean h() {
        return false;
    }

    public int hashCode() {
        return System.identityHashCode(this.f19231a);
    }

    public String toString() {
        return ReflectJavaAnnotation.class.getName() + ": " + this.f19231a;
    }
}
