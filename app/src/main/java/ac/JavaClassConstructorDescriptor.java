package ac;

import gd.g0;
import java.util.List;
import kotlin.collections.r;
import ma.o;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import sb.ClassConstructorDescriptorImpl;
import sc.DescriptorFactory;

/* compiled from: JavaClassConstructorDescriptor.java */
/* renamed from: ac.b, reason: use source file name */
/* loaded from: classes2.dex */
public class JavaClassConstructorDescriptor extends ClassConstructorDescriptorImpl implements JavaCallableMemberDescriptor {
    private Boolean J;
    private Boolean K;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    protected JavaClassConstructorDescriptor(ClassDescriptor classDescriptor, JavaClassConstructorDescriptor javaClassConstructorDescriptor, qb.g gVar, boolean z10, CallableMemberDescriptor.a aVar, SourceElement sourceElement) {
        super(classDescriptor, javaClassConstructorDescriptor, gVar, z10, aVar, sourceElement);
        if (classDescriptor == null) {
            P(0);
        }
        if (gVar == null) {
            P(1);
        }
        if (aVar == null) {
            P(2);
        }
        if (sourceElement == null) {
            P(3);
        }
        this.J = null;
        this.K = null;
    }

    public static JavaClassConstructorDescriptor C1(ClassDescriptor classDescriptor, qb.g gVar, boolean z10, SourceElement sourceElement) {
        if (classDescriptor == null) {
            P(4);
        }
        if (gVar == null) {
            P(5);
        }
        if (sourceElement == null) {
            P(6);
        }
        return new JavaClassConstructorDescriptor(classDescriptor, null, gVar, z10, CallableMemberDescriptor.a.DECLARATION, sourceElement);
    }

    private static /* synthetic */ void P(int i10) {
        String str = (i10 == 11 || i10 == 18) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 11 || i10 == 18) ? 2 : 3];
        switch (i10) {
            case 1:
            case 5:
            case 9:
            case 15:
                objArr[0] = "annotations";
                break;
            case 2:
            case 8:
            case 13:
                objArr[0] = "kind";
                break;
            case 3:
            case 6:
            case 10:
                objArr[0] = "source";
                break;
            case 4:
            default:
                objArr[0] = "containingDeclaration";
                break;
            case 7:
            case 12:
                objArr[0] = "newOwner";
                break;
            case 11:
            case 18:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/load/java/descriptors/JavaClassConstructorDescriptor";
                break;
            case 14:
                objArr[0] = "sourceElement";
                break;
            case 16:
                objArr[0] = "enhancedValueParameterTypes";
                break;
            case 17:
                objArr[0] = "enhancedReturnType";
                break;
        }
        if (i10 == 11) {
            objArr[1] = "createSubstitutedCopy";
        } else if (i10 != 18) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/load/java/descriptors/JavaClassConstructorDescriptor";
        } else {
            objArr[1] = "enhance";
        }
        switch (i10) {
            case 4:
            case 5:
            case 6:
                objArr[2] = "createJavaConstructor";
                break;
            case 7:
            case 8:
            case 9:
            case 10:
                objArr[2] = "createSubstitutedCopy";
                break;
            case 11:
            case 18:
                break;
            case 12:
            case 13:
            case 14:
            case 15:
                objArr[2] = "createDescriptor";
                break;
            case 16:
            case 17:
                objArr[2] = "enhance";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        if (i10 != 11 && i10 != 18) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    protected JavaClassConstructorDescriptor B1(ClassDescriptor classDescriptor, JavaClassConstructorDescriptor javaClassConstructorDescriptor, CallableMemberDescriptor.a aVar, SourceElement sourceElement, qb.g gVar) {
        if (classDescriptor == null) {
            P(12);
        }
        if (aVar == null) {
            P(13);
        }
        if (sourceElement == null) {
            P(14);
        }
        if (gVar == null) {
            P(15);
        }
        return new JavaClassConstructorDescriptor(classDescriptor, javaClassConstructorDescriptor, gVar, this.I, aVar, sourceElement);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sb.ClassConstructorDescriptorImpl
    /* renamed from: D1, reason: merged with bridge method [inline-methods] */
    public JavaClassConstructorDescriptor U0(DeclarationDescriptor declarationDescriptor, FunctionDescriptor functionDescriptor, CallableMemberDescriptor.a aVar, Name name, qb.g gVar, SourceElement sourceElement) {
        if (declarationDescriptor == null) {
            P(7);
        }
        if (aVar == null) {
            P(8);
        }
        if (gVar == null) {
            P(9);
        }
        if (sourceElement == null) {
            P(10);
        }
        if (aVar != CallableMemberDescriptor.a.DECLARATION && aVar != CallableMemberDescriptor.a.SYNTHESIZED) {
            throw new IllegalStateException("Attempt at creating a constructor that is not a declaration: \ncopy from: " + this + "\nnewOwner: " + declarationDescriptor + "\nkind: " + aVar);
        }
        JavaClassConstructorDescriptor B1 = B1((ClassDescriptor) declarationDescriptor, (JavaClassConstructorDescriptor) functionDescriptor, aVar, sourceElement, gVar);
        B1.h1(Z0());
        B1.i1(O());
        return B1;
    }

    @Override // ac.JavaCallableMemberDescriptor
    /* renamed from: E1, reason: merged with bridge method [inline-methods] */
    public JavaClassConstructorDescriptor G(g0 g0Var, List<g0> list, g0 g0Var2, o<CallableDescriptor.a<?>, ?> oVar) {
        List<ReceiverParameterDescriptor> j10;
        if (list == null) {
            P(16);
        }
        if (g0Var2 == null) {
            P(17);
        }
        JavaClassConstructorDescriptor U0 = U0(b(), null, getKind(), null, i(), z());
        ReceiverParameterDescriptor i10 = g0Var == null ? null : DescriptorFactory.i(U0, g0Var, qb.g.f17195b.b());
        ReceiverParameterDescriptor m02 = m0();
        j10 = r.j();
        U0.a1(i10, m02, j10, m(), h.a(list, l(), U0), g0Var2, o(), g());
        if (oVar != null) {
            U0.d1(oVar.c(), oVar.d());
        }
        return U0;
    }

    @Override // sb.FunctionDescriptorImpl, pb.CallableDescriptor
    public boolean O() {
        return this.K.booleanValue();
    }

    @Override // sb.FunctionDescriptorImpl
    public boolean Z0() {
        return this.J.booleanValue();
    }

    @Override // sb.FunctionDescriptorImpl
    public void h1(boolean z10) {
        this.J = Boolean.valueOf(z10);
    }

    @Override // sb.FunctionDescriptorImpl
    public void i1(boolean z10) {
        this.K = Boolean.valueOf(z10);
    }
}
