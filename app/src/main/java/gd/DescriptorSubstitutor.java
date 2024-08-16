package gd;

import java.util.HashMap;
import java.util.List;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import sb.TypeParameterDescriptorImpl;

/* compiled from: DescriptorSubstitutor.java */
/* renamed from: gd.t, reason: use source file name */
/* loaded from: classes2.dex */
public class DescriptorSubstitutor {
    private static /* synthetic */ void a(int i10) {
        String str = i10 != 4 ? "Argument for @NotNull parameter '%s' of %s.%s must not be null" : "@NotNull method %s.%s must not return null";
        Object[] objArr = new Object[i10 != 4 ? 3 : 2];
        switch (i10) {
            case 1:
            case 6:
                objArr[0] = "originalSubstitution";
                break;
            case 2:
            case 7:
                objArr[0] = "newContainingDeclaration";
                break;
            case 3:
            case 8:
                objArr[0] = "result";
                break;
            case 4:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/types/DescriptorSubstitutor";
                break;
            case 5:
            default:
                objArr[0] = "typeParameters";
                break;
        }
        if (i10 != 4) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/types/DescriptorSubstitutor";
        } else {
            objArr[1] = "substituteTypeParameters";
        }
        if (i10 != 4) {
            objArr[2] = "substituteTypeParameters";
        }
        String format = String.format(str, objArr);
        if (i10 == 4) {
            throw new IllegalStateException(format);
        }
    }

    public static TypeSubstitutor b(List<TypeParameterDescriptor> list, n1 n1Var, DeclarationDescriptor declarationDescriptor, List<TypeParameterDescriptor> list2) {
        if (list == null) {
            a(0);
        }
        if (n1Var == null) {
            a(1);
        }
        if (declarationDescriptor == null) {
            a(2);
        }
        if (list2 == null) {
            a(3);
        }
        TypeSubstitutor c10 = c(list, n1Var, declarationDescriptor, list2, null);
        if (c10 != null) {
            return c10;
        }
        throw new AssertionError("Substitution failed");
    }

    public static TypeSubstitutor c(List<TypeParameterDescriptor> list, n1 n1Var, DeclarationDescriptor declarationDescriptor, List<TypeParameterDescriptor> list2, boolean[] zArr) {
        if (list == null) {
            a(5);
        }
        if (n1Var == null) {
            a(6);
        }
        if (declarationDescriptor == null) {
            a(7);
        }
        if (list2 == null) {
            a(8);
        }
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        int i10 = 0;
        for (TypeParameterDescriptor typeParameterDescriptor : list) {
            TypeParameterDescriptorImpl Y0 = TypeParameterDescriptorImpl.Y0(declarationDescriptor, typeParameterDescriptor.i(), typeParameterDescriptor.N(), typeParameterDescriptor.s(), typeParameterDescriptor.getName(), i10, SourceElement.f16664a, typeParameterDescriptor.o0());
            hashMap.put(typeParameterDescriptor.n(), new TypeProjectionImpl(Y0.x()));
            hashMap2.put(typeParameterDescriptor, Y0);
            list2.add(Y0);
            i10++;
        }
        h1 j10 = h1.j(hashMap);
        TypeSubstitutor h10 = TypeSubstitutor.h(n1Var, j10);
        TypeSubstitutor h11 = TypeSubstitutor.h(n1Var.h(), j10);
        for (TypeParameterDescriptor typeParameterDescriptor2 : list) {
            TypeParameterDescriptorImpl typeParameterDescriptorImpl = (TypeParameterDescriptorImpl) hashMap2.get(typeParameterDescriptor2);
            for (g0 g0Var : typeParameterDescriptor2.getUpperBounds()) {
                ClassifierDescriptor v7 = g0Var.W0().v();
                g0 p10 = (((v7 instanceof TypeParameterDescriptor) && ld.a.k((TypeParameterDescriptor) v7)) ? h10 : h11).p(g0Var, Variance.OUT_VARIANCE);
                if (p10 == null) {
                    return null;
                }
                if (p10 != g0Var && zArr != null) {
                    zArr[0] = true;
                }
                typeParameterDescriptorImpl.V0(p10);
            }
            typeParameterDescriptorImpl.e1();
        }
        return h10;
    }
}
