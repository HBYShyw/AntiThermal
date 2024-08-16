package vb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import vb.ReflectJavaType;

/* compiled from: ReflectJavaMethod.kt */
/* renamed from: vb.u, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectJavaMethod extends t implements fc.r {

    /* renamed from: a, reason: collision with root package name */
    private final Method f19256a;

    public ReflectJavaMethod(Method method) {
        za.k.e(method, "member");
        this.f19256a = method;
    }

    @Override // fc.r
    public boolean R() {
        return z() != null;
    }

    @Override // vb.t
    /* renamed from: Z, reason: merged with bridge method [inline-methods] */
    public Method X() {
        return this.f19256a;
    }

    @Override // fc.r
    /* renamed from: a0, reason: merged with bridge method [inline-methods] */
    public ReflectJavaType f() {
        ReflectJavaType.a aVar = ReflectJavaType.f19262a;
        Type genericReturnType = X().getGenericReturnType();
        za.k.d(genericReturnType, "member.genericReturnType");
        return aVar.a(genericReturnType);
    }

    @Override // fc.r
    public List<fc.b0> l() {
        Type[] genericParameterTypes = X().getGenericParameterTypes();
        za.k.d(genericParameterTypes, "member.genericParameterTypes");
        Annotation[][] parameterAnnotations = X().getParameterAnnotations();
        za.k.d(parameterAnnotations, "member.parameterAnnotations");
        return Y(genericParameterTypes, parameterAnnotations, X().isVarArgs());
    }

    @Override // fc.z
    public List<ReflectJavaTypeParameter> m() {
        TypeVariable<Method>[] typeParameters = X().getTypeParameters();
        za.k.d(typeParameters, "member.typeParameters");
        ArrayList arrayList = new ArrayList(typeParameters.length);
        for (TypeVariable<Method> typeVariable : typeParameters) {
            arrayList.add(new ReflectJavaTypeParameter(typeVariable));
        }
        return arrayList;
    }

    @Override // fc.r
    public fc.b z() {
        Object defaultValue = X().getDefaultValue();
        if (defaultValue != null) {
            return f.f19232b.a(defaultValue, null);
        }
        return null;
    }
}
