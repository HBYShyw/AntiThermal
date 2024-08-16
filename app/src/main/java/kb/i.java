package kb;

import gd.g0;
import gd.s1;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import jb.KotlinReflectionInternalError;
import jb.o0;
import mb.KotlinBuiltIns;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.ConstructorDescriptor;
import pb.DeclarationDescriptor;
import pb.PropertyDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.ValueParameterDescriptor;
import pb.VariableDescriptor;
import sc.inlineClassesUtils;

/* compiled from: InlineClassAwareCaller.kt */
/* loaded from: classes2.dex */
public final class i {
    public static final Object a(Object obj, CallableMemberDescriptor callableMemberDescriptor) {
        g0 e10;
        Class<?> h10;
        Method f10;
        za.k.e(callableMemberDescriptor, "descriptor");
        return (((callableMemberDescriptor instanceof PropertyDescriptor) && inlineClassesUtils.d((VariableDescriptor) callableMemberDescriptor)) || (e10 = e(callableMemberDescriptor)) == null || (h10 = h(e10)) == null || (f10 = f(h10, callableMemberDescriptor)) == null) ? obj : f10.invoke(obj, new Object[0]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <M extends Member> e<M> b(e<? extends M> eVar, CallableMemberDescriptor callableMemberDescriptor, boolean z10) {
        boolean z11;
        za.k.e(eVar, "<this>");
        za.k.e(callableMemberDescriptor, "descriptor");
        boolean z12 = true;
        if (!inlineClassesUtils.a(callableMemberDescriptor)) {
            List<ValueParameterDescriptor> l10 = callableMemberDescriptor.l();
            za.k.d(l10, "descriptor.valueParameters");
            if (!(l10 instanceof Collection) || !l10.isEmpty()) {
                Iterator<T> it = l10.iterator();
                while (it.hasNext()) {
                    g0 type = ((ValueParameterDescriptor) it.next()).getType();
                    za.k.d(type, "it.type");
                    if (inlineClassesUtils.c(type)) {
                        z11 = true;
                        break;
                    }
                }
            }
            z11 = false;
            if (!z11) {
                g0 f10 = callableMemberDescriptor.f();
                if (!(f10 != null && inlineClassesUtils.c(f10)) && ((eVar instanceof d) || !g(callableMemberDescriptor))) {
                    z12 = false;
                }
            }
        }
        return z12 ? new h(callableMemberDescriptor, eVar, z10) : eVar;
    }

    public static /* synthetic */ e c(e eVar, CallableMemberDescriptor callableMemberDescriptor, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return b(eVar, callableMemberDescriptor, z10);
    }

    public static final Method d(Class<?> cls, CallableMemberDescriptor callableMemberDescriptor) {
        za.k.e(cls, "<this>");
        za.k.e(callableMemberDescriptor, "descriptor");
        try {
            Method declaredMethod = cls.getDeclaredMethod("box-impl", f(cls, callableMemberDescriptor).getReturnType());
            za.k.d(declaredMethod, "{\n        getDeclaredMet…riptor).returnType)\n    }");
            return declaredMethod;
        } catch (NoSuchMethodException unused) {
            throw new KotlinReflectionInternalError("No box method found in inline class: " + cls + " (calling " + callableMemberDescriptor + ')');
        }
    }

    private static final g0 e(CallableMemberDescriptor callableMemberDescriptor) {
        ReceiverParameterDescriptor r02 = callableMemberDescriptor.r0();
        ReceiverParameterDescriptor m02 = callableMemberDescriptor.m0();
        if (r02 != null) {
            return r02.getType();
        }
        if (m02 == null) {
            return null;
        }
        if (callableMemberDescriptor instanceof ConstructorDescriptor) {
            return m02.getType();
        }
        DeclarationDescriptor b10 = callableMemberDescriptor.b();
        ClassDescriptor classDescriptor = b10 instanceof ClassDescriptor ? (ClassDescriptor) b10 : null;
        if (classDescriptor != null) {
            return classDescriptor.x();
        }
        return null;
    }

    public static final Method f(Class<?> cls, CallableMemberDescriptor callableMemberDescriptor) {
        za.k.e(cls, "<this>");
        za.k.e(callableMemberDescriptor, "descriptor");
        try {
            Method declaredMethod = cls.getDeclaredMethod("unbox-impl", new Class[0]);
            za.k.d(declaredMethod, "{\n        getDeclaredMet…LINE_CLASS_MEMBERS)\n    }");
            return declaredMethod;
        } catch (NoSuchMethodException unused) {
            throw new KotlinReflectionInternalError("No unbox method found in inline class: " + cls + " (calling " + callableMemberDescriptor + ')');
        }
    }

    private static final boolean g(CallableMemberDescriptor callableMemberDescriptor) {
        g0 e10 = e(callableMemberDescriptor);
        return e10 != null && inlineClassesUtils.c(e10);
    }

    public static final Class<?> h(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        Class<?> i10 = i(g0Var.W0().v());
        if (i10 == null) {
            return null;
        }
        if (!s1.l(g0Var)) {
            return i10;
        }
        g0 e10 = inlineClassesUtils.e(g0Var);
        if (e10 == null || s1.l(e10) || KotlinBuiltIns.r0(e10)) {
            return null;
        }
        return i10;
    }

    public static final Class<?> i(DeclarationDescriptor declarationDescriptor) {
        if (!(declarationDescriptor instanceof ClassDescriptor) || !inlineClassesUtils.b(declarationDescriptor)) {
            return null;
        }
        ClassDescriptor classDescriptor = (ClassDescriptor) declarationDescriptor;
        Class<?> p10 = o0.p(classDescriptor);
        if (p10 != null) {
            return p10;
        }
        throw new KotlinReflectionInternalError("Class object for the class " + classDescriptor.getName() + " cannot be found (classId=" + wc.c.k((ClassifierDescriptor) declarationDescriptor) + ')');
    }
}
