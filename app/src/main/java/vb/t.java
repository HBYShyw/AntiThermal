package vb;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import oc.FqName;
import oc.Name;
import oc.SpecialNames;
import pb.Visibilities;
import pb.n1;

/* compiled from: ReflectJavaMember.kt */
/* loaded from: classes2.dex */
public abstract class t extends ReflectJavaElement implements h, ReflectJavaModifierListOwner, fc.q {
    @Override // vb.h
    public AnnotatedElement D() {
        Member X = X();
        za.k.c(X, "null cannot be cast to non-null type java.lang.reflect.AnnotatedElement");
        return (AnnotatedElement) X;
    }

    @Override // vb.ReflectJavaModifierListOwner
    public int K() {
        return X().getModifiers();
    }

    @Override // fc.q
    /* renamed from: W, reason: merged with bridge method [inline-methods] */
    public l V() {
        Class<?> declaringClass = X().getDeclaringClass();
        za.k.d(declaringClass, "member.declaringClass");
        return new l(declaringClass);
    }

    public abstract Member X();

    /* JADX INFO: Access modifiers changed from: protected */
    public final List<fc.b0> Y(Type[] typeArr, Annotation[][] annotationArr, boolean z10) {
        String str;
        boolean z11;
        int D;
        Object W;
        za.k.e(typeArr, "parameterTypes");
        za.k.e(annotationArr, "parameterAnnotations");
        ArrayList arrayList = new ArrayList(typeArr.length);
        List<String> b10 = c.f19218a.b(X());
        int size = b10 != null ? b10.size() - typeArr.length : 0;
        int length = typeArr.length;
        for (int i10 = 0; i10 < length; i10++) {
            ReflectJavaType a10 = ReflectJavaType.f19262a.a(typeArr[i10]);
            if (b10 != null) {
                W = _Collections.W(b10, i10 + size);
                str = (String) W;
                if (str == null) {
                    throw new IllegalStateException(("No parameter with index " + i10 + '+' + size + " (name=" + getName() + " type=" + a10 + ") in " + this).toString());
                }
            } else {
                str = null;
            }
            if (z10) {
                D = _Arrays.D(typeArr);
                if (i10 == D) {
                    z11 = true;
                    arrayList.add(new ReflectJavaValueParameter(a10, annotationArr[i10], str, z11));
                }
            }
            z11 = false;
            arrayList.add(new ReflectJavaValueParameter(a10, annotationArr[i10], str, z11));
        }
        return arrayList;
    }

    public boolean equals(Object obj) {
        return (obj instanceof t) && za.k.a(X(), ((t) obj).X());
    }

    @Override // fc.s
    public n1 g() {
        int K = K();
        if (Modifier.isPublic(K)) {
            return Visibilities.h.f16718c;
        }
        if (Modifier.isPrivate(K)) {
            return Visibilities.e.f16715c;
        }
        if (Modifier.isProtected(K)) {
            return Modifier.isStatic(K) ? tb.c.f18706c : tb.b.f18705c;
        }
        return tb.a.f18704c;
    }

    @Override // fc.t
    public Name getName() {
        String name = X().getName();
        Name f10 = name != null ? Name.f(name) : null;
        return f10 == null ? SpecialNames.f16447b : f10;
    }

    public int hashCode() {
        return X().hashCode();
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

    @Override // fc.s
    public boolean n() {
        return Modifier.isAbstract(K());
    }

    @Override // fc.s
    public boolean o() {
        return Modifier.isStatic(K());
    }

    public String toString() {
        return getClass().getName() + ": " + X();
    }

    @Override // fc.s
    public boolean w() {
        return Modifier.isFinal(K());
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
