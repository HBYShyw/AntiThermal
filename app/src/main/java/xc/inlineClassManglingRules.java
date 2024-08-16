package xc;

import gd.g0;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import mb.StandardNames;
import pb.CallableMemberDescriptor;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.DescriptorVisibilities;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import sc.inlineClassesUtils;
import za.k;

/* compiled from: inlineClassManglingRules.kt */
/* renamed from: xc.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class inlineClassManglingRules {
    private static final boolean a(ClassDescriptor classDescriptor) {
        return k.a(wc.c.l(classDescriptor), StandardNames.f15279q);
    }

    public static final boolean b(g0 g0Var) {
        k.e(g0Var, "<this>");
        ClassifierDescriptor v7 = g0Var.W0().v();
        return v7 != null && c(v7);
    }

    public static final boolean c(DeclarationDescriptor declarationDescriptor) {
        k.e(declarationDescriptor, "<this>");
        return inlineClassesUtils.b(declarationDescriptor) && !a((ClassDescriptor) declarationDescriptor);
    }

    private static final boolean d(g0 g0Var) {
        ClassifierDescriptor v7 = g0Var.W0().v();
        TypeParameterDescriptor typeParameterDescriptor = v7 instanceof TypeParameterDescriptor ? (TypeParameterDescriptor) v7 : null;
        if (typeParameterDescriptor == null) {
            return false;
        }
        return e(ld.a.j(typeParameterDescriptor));
    }

    private static final boolean e(g0 g0Var) {
        return b(g0Var) || d(g0Var);
    }

    public static final boolean f(CallableMemberDescriptor callableMemberDescriptor) {
        k.e(callableMemberDescriptor, "descriptor");
        ClassConstructorDescriptor classConstructorDescriptor = callableMemberDescriptor instanceof ClassConstructorDescriptor ? (ClassConstructorDescriptor) callableMemberDescriptor : null;
        if (classConstructorDescriptor == null || DescriptorVisibilities.g(classConstructorDescriptor.g())) {
            return false;
        }
        ClassDescriptor K = classConstructorDescriptor.K();
        k.d(K, "constructorDescriptor.constructedClass");
        if (inlineClassesUtils.b(K) || sc.e.G(classConstructorDescriptor.K())) {
            return false;
        }
        List<ValueParameterDescriptor> l10 = classConstructorDescriptor.l();
        k.d(l10, "constructorDescriptor.valueParameters");
        if ((l10 instanceof Collection) && l10.isEmpty()) {
            return false;
        }
        Iterator<T> it = l10.iterator();
        while (it.hasNext()) {
            g0 type = ((ValueParameterDescriptor) it.next()).getType();
            k.d(type, "it.type");
            if (e(type)) {
                return true;
            }
        }
        return false;
    }
}
