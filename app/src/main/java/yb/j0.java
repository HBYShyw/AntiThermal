package yb;

import ac.JavaMethodDescriptor;
import java.util.Iterator;
import oc.FqName;
import pb.CallableMemberDescriptor;
import pb.FunctionDescriptor;
import pb.n1;
import qb.AnnotationDescriptor;

/* compiled from: utils.kt */
/* loaded from: classes2.dex */
public final class j0 {
    public static final AnnotationDescriptor a(bc.g gVar, fc.c0 c0Var) {
        AnnotationDescriptor annotationDescriptor;
        boolean z10;
        za.k.e(gVar, "c");
        za.k.e(c0Var, "wildcardType");
        if (c0Var.J() != null) {
            Iterator<AnnotationDescriptor> it = new bc.d(gVar, c0Var, false, 4, null).iterator();
            while (true) {
                if (!it.hasNext()) {
                    annotationDescriptor = null;
                    break;
                }
                annotationDescriptor = it.next();
                AnnotationDescriptor annotationDescriptor2 = annotationDescriptor;
                FqName[] f10 = v.f();
                int length = f10.length;
                int i10 = 0;
                while (true) {
                    if (i10 >= length) {
                        z10 = false;
                        break;
                    }
                    if (za.k.a(annotationDescriptor2.d(), f10[i10])) {
                        z10 = true;
                        break;
                    }
                    i10++;
                }
                if (z10) {
                    break;
                }
            }
            return annotationDescriptor;
        }
        throw new IllegalArgumentException("Nullability annotations on unbounded wildcards aren't supported".toString());
    }

    public static final boolean b(CallableMemberDescriptor callableMemberDescriptor) {
        za.k.e(callableMemberDescriptor, "memberDescriptor");
        return (callableMemberDescriptor instanceof FunctionDescriptor) && za.k.a(callableMemberDescriptor.E(JavaMethodDescriptor.L), Boolean.TRUE);
    }

    public static final boolean c(JavaTypeEnhancementState javaTypeEnhancementState) {
        za.k.e(javaTypeEnhancementState, "javaTypeEnhancementState");
        return javaTypeEnhancementState.c().invoke(v.e()) == ReportLevel.STRICT;
    }

    public static final pb.u d(n1 n1Var) {
        za.k.e(n1Var, "<this>");
        pb.u g6 = JavaDescriptorVisibilities.g(n1Var);
        za.k.d(g6, "toDescriptorVisibility(this)");
        return g6;
    }
}
