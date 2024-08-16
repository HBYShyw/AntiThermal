package jb;

import ac.JavaClassConstructorDescriptor;
import ac.JavaMethodDescriptor;
import ac.JavaPropertyDescriptor;
import java.lang.reflect.Method;
import jb.i;
import jb.j;
import lc.ProtoBufUtil;
import mb.PrimitiveType;
import mb.StandardNames;
import mc.JvmProtoBuf;
import nc.JvmMemberSignature;
import nc.JvmProtoBufUtil;
import ob.CloneableClassScope;
import ob.JavaToKotlinClassMap;
import oc.ClassId;
import oc.FqName;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.PropertyDescriptor;
import pb.PropertyGetterDescriptor;
import pb.PropertySetterDescriptor;
import pb.SourceElement;
import qc.i;
import sc.DescriptorFactory;
import sc.inlineClassesUtils;
import vb.ReflectJavaConstructor;
import vb.ReflectJavaField;
import vb.ReflectJavaMethod;
import vb.reflectClassUtil;
import xc.JvmPrimitiveType;
import yb.JvmAbi;

/* compiled from: RuntimeTypeMapper.kt */
/* loaded from: classes2.dex */
public final class l0 {

    /* renamed from: a, reason: collision with root package name */
    public static final l0 f13288a = new l0();

    /* renamed from: b, reason: collision with root package name */
    private static final ClassId f13289b;

    static {
        ClassId m10 = ClassId.m(new FqName("java.lang.Void"));
        za.k.d(m10, "topLevel(FqName(\"java.lang.Void\"))");
        f13289b = m10;
    }

    private l0() {
    }

    private final PrimitiveType a(Class<?> cls) {
        if (cls.isPrimitive()) {
            return JvmPrimitiveType.b(cls.getSimpleName()).f();
        }
        return null;
    }

    private final boolean b(FunctionDescriptor functionDescriptor) {
        if (DescriptorFactory.p(functionDescriptor) || DescriptorFactory.q(functionDescriptor)) {
            return true;
        }
        return za.k.a(functionDescriptor.getName(), CloneableClassScope.f16335e.a()) && functionDescriptor.l().isEmpty();
    }

    private final i.e d(FunctionDescriptor functionDescriptor) {
        return new i.e(new JvmMemberSignature.b(e(functionDescriptor), hc.w.c(functionDescriptor, false, false, 1, null)));
    }

    private final String e(CallableMemberDescriptor callableMemberDescriptor) {
        String b10 = yb.h0.b(callableMemberDescriptor);
        if (b10 != null) {
            return b10;
        }
        if (callableMemberDescriptor instanceof PropertyGetterDescriptor) {
            String b11 = wc.c.s(callableMemberDescriptor).getName().b();
            za.k.d(b11, "descriptor.propertyIfAccessor.name.asString()");
            return JvmAbi.b(b11);
        }
        if (callableMemberDescriptor instanceof PropertySetterDescriptor) {
            String b12 = wc.c.s(callableMemberDescriptor).getName().b();
            za.k.d(b12, "descriptor.propertyIfAccessor.name.asString()");
            return JvmAbi.e(b12);
        }
        String b13 = callableMemberDescriptor.getName().b();
        za.k.d(b13, "descriptor.name.asString()");
        return b13;
    }

    public final ClassId c(Class<?> cls) {
        za.k.e(cls, "klass");
        if (cls.isArray()) {
            Class<?> componentType = cls.getComponentType();
            za.k.d(componentType, "klass.componentType");
            PrimitiveType a10 = a(componentType);
            if (a10 != null) {
                return new ClassId(StandardNames.f15283u, a10.c());
            }
            ClassId m10 = ClassId.m(StandardNames.a.f15305i.l());
            za.k.d(m10, "topLevel(StandardNames.FqNames.array.toSafe())");
            return m10;
        }
        if (za.k.a(cls, Void.TYPE)) {
            return f13289b;
        }
        PrimitiveType a11 = a(cls);
        if (a11 != null) {
            return new ClassId(StandardNames.f15283u, a11.e());
        }
        ClassId a12 = reflectClassUtil.a(cls);
        if (!a12.k()) {
            JavaToKotlinClassMap javaToKotlinClassMap = JavaToKotlinClassMap.f16339a;
            FqName b10 = a12.b();
            za.k.d(b10, "classId.asSingleFqName()");
            ClassId m11 = javaToKotlinClassMap.m(b10);
            if (m11 != null) {
                return m11;
            }
        }
        return a12;
    }

    public final j f(PropertyDescriptor propertyDescriptor) {
        za.k.e(propertyDescriptor, "possiblyOverriddenProperty");
        PropertyDescriptor T0 = ((PropertyDescriptor) sc.e.L(propertyDescriptor)).T0();
        za.k.d(T0, "unwrapFakeOverride(possi…rriddenProperty).original");
        if (T0 instanceof ed.j) {
            ed.j jVar = (ed.j) T0;
            jc.n M = jVar.M();
            i.f<jc.n, JvmProtoBuf.d> fVar = JvmProtoBuf.f15367d;
            za.k.d(fVar, "propertySignature");
            JvmProtoBuf.d dVar = (JvmProtoBuf.d) ProtoBufUtil.a(M, fVar);
            if (dVar != null) {
                return new j.c(T0, M, dVar, jVar.h0(), jVar.b0());
            }
        } else if (T0 instanceof JavaPropertyDescriptor) {
            SourceElement z10 = ((JavaPropertyDescriptor) T0).z();
            ec.a aVar = z10 instanceof ec.a ? (ec.a) z10 : null;
            fc.l b10 = aVar != null ? aVar.b() : null;
            if (b10 instanceof ReflectJavaField) {
                return new j.a(((ReflectJavaField) b10).X());
            }
            if (b10 instanceof ReflectJavaMethod) {
                Method X = ((ReflectJavaMethod) b10).X();
                PropertySetterDescriptor k10 = T0.k();
                SourceElement z11 = k10 != null ? k10.z() : null;
                ec.a aVar2 = z11 instanceof ec.a ? (ec.a) z11 : null;
                fc.l b11 = aVar2 != null ? aVar2.b() : null;
                ReflectJavaMethod reflectJavaMethod = b11 instanceof ReflectJavaMethod ? (ReflectJavaMethod) b11 : null;
                return new j.b(X, reflectJavaMethod != null ? reflectJavaMethod.X() : null);
            }
            throw new KotlinReflectionInternalError("Incorrect resolution sequence for Java field " + T0 + " (source = " + b10 + ')');
        }
        PropertyGetterDescriptor h10 = T0.h();
        za.k.b(h10);
        i.e d10 = d(h10);
        PropertySetterDescriptor k11 = T0.k();
        return new j.d(d10, k11 != null ? d(k11) : null);
    }

    public final i g(FunctionDescriptor functionDescriptor) {
        Method X;
        JvmMemberSignature.b b10;
        JvmMemberSignature.b e10;
        za.k.e(functionDescriptor, "possiblySubstitutedFunction");
        FunctionDescriptor T0 = ((FunctionDescriptor) sc.e.L(functionDescriptor)).T0();
        za.k.d(T0, "unwrapFakeOverride(possi…titutedFunction).original");
        if (T0 instanceof ed.b) {
            ed.b bVar = (ed.b) T0;
            qc.q M = bVar.M();
            if ((M instanceof jc.i) && (e10 = JvmProtoBufUtil.f16006a.e((jc.i) M, bVar.h0(), bVar.b0())) != null) {
                return new i.e(e10);
            }
            if ((M instanceof jc.d) && (b10 = JvmProtoBufUtil.f16006a.b((jc.d) M, bVar.h0(), bVar.b0())) != null) {
                DeclarationDescriptor b11 = functionDescriptor.b();
                za.k.d(b11, "possiblySubstitutedFunction.containingDeclaration");
                if (inlineClassesUtils.b(b11)) {
                    return new i.e(b10);
                }
                return new i.d(b10);
            }
            return d(T0);
        }
        if (T0 instanceof JavaMethodDescriptor) {
            SourceElement z10 = ((JavaMethodDescriptor) T0).z();
            ec.a aVar = z10 instanceof ec.a ? (ec.a) z10 : null;
            fc.l b12 = aVar != null ? aVar.b() : null;
            ReflectJavaMethod reflectJavaMethod = b12 instanceof ReflectJavaMethod ? (ReflectJavaMethod) b12 : null;
            if (reflectJavaMethod != null && (X = reflectJavaMethod.X()) != null) {
                return new i.c(X);
            }
            throw new KotlinReflectionInternalError("Incorrect resolution sequence for Java method " + T0);
        }
        if (T0 instanceof JavaClassConstructorDescriptor) {
            SourceElement z11 = ((JavaClassConstructorDescriptor) T0).z();
            ec.a aVar2 = z11 instanceof ec.a ? (ec.a) z11 : null;
            fc.l b13 = aVar2 != null ? aVar2.b() : null;
            if (b13 instanceof ReflectJavaConstructor) {
                return new i.b(((ReflectJavaConstructor) b13).X());
            }
            if (b13 instanceof vb.l) {
                vb.l lVar = (vb.l) b13;
                if (lVar.y()) {
                    return new i.a(lVar.D());
                }
            }
            throw new KotlinReflectionInternalError("Incorrect resolution sequence for Java constructor " + T0 + " (" + b13 + ')');
        }
        if (b(T0)) {
            return d(T0);
        }
        throw new KotlinReflectionInternalError("Unknown origin of " + T0 + " (" + T0.getClass() + ')');
    }
}
