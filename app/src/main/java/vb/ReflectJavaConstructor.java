package vb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections._ArraysJvm;

/* compiled from: ReflectJavaConstructor.kt */
/* renamed from: vb.o, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectJavaConstructor extends t implements fc.k {

    /* renamed from: a, reason: collision with root package name */
    private final Constructor<?> f19252a;

    public ReflectJavaConstructor(Constructor<?> constructor) {
        za.k.e(constructor, "member");
        this.f19252a = constructor;
    }

    @Override // vb.t
    /* renamed from: Z, reason: merged with bridge method [inline-methods] */
    public Constructor<?> X() {
        return this.f19252a;
    }

    @Override // fc.k
    public List<fc.b0> l() {
        Object[] k10;
        Object[] k11;
        List<fc.b0> j10;
        Type[] genericParameterTypes = X().getGenericParameterTypes();
        za.k.d(genericParameterTypes, "types");
        if (genericParameterTypes.length == 0) {
            j10 = kotlin.collections.r.j();
            return j10;
        }
        Class<?> declaringClass = X().getDeclaringClass();
        if (declaringClass.getDeclaringClass() != null && !Modifier.isStatic(declaringClass.getModifiers())) {
            k11 = _ArraysJvm.k(genericParameterTypes, 1, genericParameterTypes.length);
            genericParameterTypes = (Type[]) k11;
        }
        Annotation[][] parameterAnnotations = X().getParameterAnnotations();
        if (parameterAnnotations.length >= genericParameterTypes.length) {
            if (parameterAnnotations.length > genericParameterTypes.length) {
                za.k.d(parameterAnnotations, "annotations");
                k10 = _ArraysJvm.k(parameterAnnotations, parameterAnnotations.length - genericParameterTypes.length, parameterAnnotations.length);
                parameterAnnotations = (Annotation[][]) k10;
            }
            za.k.d(genericParameterTypes, "realTypes");
            za.k.d(parameterAnnotations, "realAnnotations");
            return Y(genericParameterTypes, parameterAnnotations, X().isVarArgs());
        }
        throw new IllegalStateException("Illegal generic signature: " + X());
    }

    @Override // fc.z
    public List<ReflectJavaTypeParameter> m() {
        TypeVariable<Constructor<?>>[] typeParameters = X().getTypeParameters();
        za.k.d(typeParameters, "member.typeParameters");
        ArrayList arrayList = new ArrayList(typeParameters.length);
        for (TypeVariable<Constructor<?>> typeVariable : typeParameters) {
            arrayList.add(new ReflectJavaTypeParameter(typeVariable));
        }
        return arrayList;
    }
}
