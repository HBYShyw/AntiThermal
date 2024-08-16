package sc;

import gd.g0;
import gd.o0;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import pb.CallableDescriptor;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.InlineClassRepresentation;
import pb.PropertyDescriptor;
import pb.PropertyGetterDescriptor;
import pb.VariableDescriptor;

/* compiled from: inlineClassesUtils.kt */
/* renamed from: sc.g, reason: use source file name */
/* loaded from: classes2.dex */
public final class inlineClassesUtils {

    /* renamed from: a, reason: collision with root package name */
    private static final FqName f18436a;

    /* renamed from: b, reason: collision with root package name */
    private static final ClassId f18437b;

    static {
        FqName fqName = new FqName("kotlin.jvm.JvmInline");
        f18436a = fqName;
        ClassId m10 = ClassId.m(fqName);
        za.k.d(m10, "topLevel(JVM_INLINE_ANNOTATION_FQ_NAME)");
        f18437b = m10;
    }

    public static final boolean a(CallableDescriptor callableDescriptor) {
        za.k.e(callableDescriptor, "<this>");
        if (callableDescriptor instanceof PropertyGetterDescriptor) {
            PropertyDescriptor K0 = ((PropertyGetterDescriptor) callableDescriptor).K0();
            za.k.d(K0, "correspondingProperty");
            if (d(K0)) {
                return true;
            }
        }
        return false;
    }

    public static final boolean b(DeclarationDescriptor declarationDescriptor) {
        za.k.e(declarationDescriptor, "<this>");
        return (declarationDescriptor instanceof ClassDescriptor) && (((ClassDescriptor) declarationDescriptor).G0() instanceof InlineClassRepresentation);
    }

    public static final boolean c(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        ClassifierDescriptor v7 = g0Var.W0().v();
        if (v7 != null) {
            return b(v7);
        }
        return false;
    }

    public static final boolean d(VariableDescriptor variableDescriptor) {
        InlineClassRepresentation<o0> n10;
        za.k.e(variableDescriptor, "<this>");
        if (variableDescriptor.r0() == null) {
            DeclarationDescriptor b10 = variableDescriptor.b();
            Name name = null;
            ClassDescriptor classDescriptor = b10 instanceof ClassDescriptor ? (ClassDescriptor) b10 : null;
            if (classDescriptor != null && (n10 = wc.c.n(classDescriptor)) != null) {
                name = n10.c();
            }
            if (za.k.a(name, variableDescriptor.getName())) {
                return true;
            }
        }
        return false;
    }

    public static final g0 e(g0 g0Var) {
        InlineClassRepresentation<o0> n10;
        za.k.e(g0Var, "<this>");
        ClassifierDescriptor v7 = g0Var.W0().v();
        if (!(v7 instanceof ClassDescriptor)) {
            v7 = null;
        }
        ClassDescriptor classDescriptor = (ClassDescriptor) v7;
        if (classDescriptor == null || (n10 = wc.c.n(classDescriptor)) == null) {
            return null;
        }
        return n10.d();
    }
}
