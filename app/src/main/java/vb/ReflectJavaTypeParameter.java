package vb;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.collections._Collections;
import oc.FqName;
import oc.Name;

/* compiled from: ReflectJavaTypeParameter.kt */
/* renamed from: vb.a0, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectJavaTypeParameter extends ReflectJavaElement implements h, fc.y {

    /* renamed from: a, reason: collision with root package name */
    private final TypeVariable<?> f19207a;

    public ReflectJavaTypeParameter(TypeVariable<?> typeVariable) {
        za.k.e(typeVariable, "typeVariable");
        this.f19207a = typeVariable;
    }

    @Override // vb.h
    public AnnotatedElement D() {
        TypeVariable<?> typeVariable = this.f19207a;
        if (typeVariable instanceof AnnotatedElement) {
            return (AnnotatedElement) typeVariable;
        }
        return null;
    }

    @Override // fc.y
    /* renamed from: W, reason: merged with bridge method [inline-methods] */
    public List<ReflectJavaClassifierType> getUpperBounds() {
        Object s02;
        List<ReflectJavaClassifierType> j10;
        Type[] bounds = this.f19207a.getBounds();
        za.k.d(bounds, "typeVariable.bounds");
        ArrayList arrayList = new ArrayList(bounds.length);
        for (Type type : bounds) {
            arrayList.add(new ReflectJavaClassifierType(type));
        }
        s02 = _Collections.s0(arrayList);
        ReflectJavaClassifierType reflectJavaClassifierType = (ReflectJavaClassifierType) s02;
        if (!za.k.a(reflectJavaClassifierType != null ? reflectJavaClassifierType.W() : null, Object.class)) {
            return arrayList;
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    public boolean equals(Object obj) {
        return (obj instanceof ReflectJavaTypeParameter) && za.k.a(this.f19207a, ((ReflectJavaTypeParameter) obj).f19207a);
    }

    @Override // fc.t
    public Name getName() {
        Name f10 = Name.f(this.f19207a.getName());
        za.k.d(f10, "identifier(typeVariable.name)");
        return f10;
    }

    public int hashCode() {
        return this.f19207a.hashCode();
    }

    @Override // fc.d
    public /* bridge */ /* synthetic */ Collection i() {
        return i();
    }

    @Override // fc.d
    public /* bridge */ /* synthetic */ fc.a j(FqName fqName) {
        return j(fqName);
    }

    @Override // fc.d
    public boolean k() {
        return false;
    }

    public String toString() {
        return ReflectJavaTypeParameter.class.getName() + ": " + this.f19207a;
    }

    @Override // vb.h, fc.d
    public List<ReflectJavaAnnotation> i() {
        List<ReflectJavaAnnotation> j10;
        Annotation[] declaredAnnotations;
        List<ReflectJavaAnnotation> b10;
        AnnotatedElement D = D();
        if (D != null && (declaredAnnotations = D.getDeclaredAnnotations()) != null && (b10 = i.b(declaredAnnotations)) != null) {
            return b10;
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // vb.h, fc.d
    public ReflectJavaAnnotation j(FqName fqName) {
        Annotation[] declaredAnnotations;
        za.k.e(fqName, "fqName");
        AnnotatedElement D = D();
        if (D == null || (declaredAnnotations = D.getDeclaredAnnotations()) == null) {
            return null;
        }
        return i.a(declaredAnnotations, fqName);
    }
}
