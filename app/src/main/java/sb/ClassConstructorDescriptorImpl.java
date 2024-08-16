package sb;

import gd.TypeSubstitutor;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import oc.Name;
import oc.SpecialNames;
import pb.CallableMemberDescriptor;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ConstructorDescriptor;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;

/* compiled from: ClassConstructorDescriptorImpl.java */
/* renamed from: sb.f, reason: use source file name */
/* loaded from: classes2.dex */
public class ClassConstructorDescriptorImpl extends FunctionDescriptorImpl implements ClassConstructorDescriptor {
    protected final boolean I;

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ClassConstructorDescriptorImpl(ClassDescriptor classDescriptor, ConstructorDescriptor constructorDescriptor, qb.g gVar, boolean z10, CallableMemberDescriptor.a aVar, SourceElement sourceElement) {
        super(classDescriptor, constructorDescriptor, gVar, SpecialNames.f16455j, aVar, sourceElement);
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
        this.I = z10;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0018  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0023  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00aa A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0028  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x002d  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0032  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0049  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0053  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static /* synthetic */ void P(int i10) {
        String str;
        int i11;
        if (i10 != 21 && i10 != 27) {
            switch (i10) {
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                    break;
                default:
                    str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                    break;
            }
            if (i10 != 21 && i10 != 27) {
                switch (i10) {
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                        break;
                    default:
                        i11 = 3;
                        break;
                }
                Object[] objArr = new Object[i11];
                switch (i10) {
                    case 1:
                    case 5:
                    case 8:
                    case 25:
                        objArr[0] = "annotations";
                        break;
                    case 2:
                    case 24:
                        objArr[0] = "kind";
                        break;
                    case 3:
                    case 6:
                    case 9:
                    case 26:
                        objArr[0] = "source";
                        break;
                    case 4:
                    case 7:
                    default:
                        objArr[0] = "containingDeclaration";
                        break;
                    case 10:
                    case 13:
                        objArr[0] = "unsubstitutedValueParameters";
                        break;
                    case 11:
                    case 14:
                        objArr[0] = "visibility";
                        break;
                    case 12:
                        objArr[0] = "typeParameterDescriptors";
                        break;
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 21:
                    case 27:
                        objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/ClassConstructorDescriptorImpl";
                        break;
                    case 20:
                        objArr[0] = "originalSubstitutor";
                        break;
                    case 22:
                        objArr[0] = "overriddenDescriptors";
                        break;
                    case 23:
                        objArr[0] = "newOwner";
                        break;
                }
                if (i10 != 21) {
                    objArr[1] = "getOverriddenDescriptors";
                } else if (i10 != 27) {
                    switch (i10) {
                        case 15:
                        case 16:
                            objArr[1] = "calculateContextReceiverParameters";
                            break;
                        case 17:
                            objArr[1] = "getContainingDeclaration";
                            break;
                        case 18:
                            objArr[1] = "getConstructedClass";
                            break;
                        case 19:
                            objArr[1] = "getOriginal";
                            break;
                        default:
                            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/ClassConstructorDescriptorImpl";
                            break;
                    }
                } else {
                    objArr[1] = "copy";
                }
                switch (i10) {
                    case 4:
                    case 5:
                    case 6:
                        objArr[2] = "create";
                        break;
                    case 7:
                    case 8:
                    case 9:
                        objArr[2] = "createSynthesized";
                        break;
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                        objArr[2] = "initialize";
                        break;
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 21:
                    case 27:
                        break;
                    case 20:
                        objArr[2] = "substitute";
                        break;
                    case 22:
                        objArr[2] = "setOverriddenDescriptors";
                        break;
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                        objArr[2] = "createSubstitutedCopy";
                        break;
                    default:
                        objArr[2] = "<init>";
                        break;
                }
                String format = String.format(str, objArr);
                if (i10 != 21 && i10 != 27) {
                    switch (i10) {
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                            break;
                        default:
                            throw new IllegalArgumentException(format);
                    }
                }
                throw new IllegalStateException(format);
            }
            i11 = 2;
            Object[] objArr2 = new Object[i11];
            switch (i10) {
            }
            if (i10 != 21) {
            }
            switch (i10) {
            }
            String format2 = String.format(str, objArr2);
            if (i10 != 21) {
                switch (i10) {
                }
            }
            throw new IllegalStateException(format2);
        }
        str = "@NotNull method %s.%s must not return null";
        if (i10 != 21) {
            switch (i10) {
            }
            Object[] objArr22 = new Object[i11];
            switch (i10) {
            }
            if (i10 != 21) {
            }
            switch (i10) {
            }
            String format22 = String.format(str, objArr22);
            if (i10 != 21) {
            }
            throw new IllegalStateException(format22);
        }
        i11 = 2;
        Object[] objArr222 = new Object[i11];
        switch (i10) {
        }
        if (i10 != 21) {
        }
        switch (i10) {
        }
        String format222 = String.format(str, objArr222);
        if (i10 != 21) {
        }
        throw new IllegalStateException(format222);
    }

    private List<ReceiverParameterDescriptor> t1() {
        ClassDescriptor b10 = b();
        if (!b10.P0().isEmpty()) {
            List<ReceiverParameterDescriptor> P0 = b10.P0();
            if (P0 == null) {
                P(15);
            }
            return P0;
        }
        List<ReceiverParameterDescriptor> emptyList = Collections.emptyList();
        if (emptyList == null) {
            P(16);
        }
        return emptyList;
    }

    public static ClassConstructorDescriptorImpl w1(ClassDescriptor classDescriptor, qb.g gVar, boolean z10, SourceElement sourceElement) {
        if (classDescriptor == null) {
            P(4);
        }
        if (gVar == null) {
            P(5);
        }
        if (sourceElement == null) {
            P(6);
        }
        return new ClassConstructorDescriptorImpl(classDescriptor, null, gVar, z10, CallableMemberDescriptor.a.DECLARATION, sourceElement);
    }

    public ClassConstructorDescriptorImpl A1(List<ValueParameterDescriptor> list, pb.u uVar, List<TypeParameterDescriptor> list2) {
        if (list == null) {
            P(10);
        }
        if (uVar == null) {
            P(11);
        }
        if (list2 == null) {
            P(12);
        }
        super.a1(null, u1(), t1(), list2, list, null, Modality.FINAL, uVar);
        return this;
    }

    @Override // sb.FunctionDescriptorImpl, pb.CallableMemberDescriptor
    public void D0(Collection<? extends CallableMemberDescriptor> collection) {
        if (collection == null) {
            P(22);
        }
    }

    @Override // sb.FunctionDescriptorImpl, pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        return declarationDescriptorVisitor.l(this, d10);
    }

    @Override // pb.ConstructorDescriptor
    public boolean J() {
        return this.I;
    }

    @Override // pb.ConstructorDescriptor
    public ClassDescriptor K() {
        ClassDescriptor b10 = b();
        if (b10 == null) {
            P(18);
        }
        return b10;
    }

    @Override // sb.FunctionDescriptorImpl, pb.FunctionDescriptor, pb.CallableMemberDescriptor, pb.CallableDescriptor
    public Collection<? extends FunctionDescriptor> e() {
        Set emptySet = Collections.emptySet();
        if (emptySet == null) {
            P(21);
        }
        return emptySet;
    }

    public ReceiverParameterDescriptor u1() {
        ClassDescriptor b10 = b();
        if (!b10.r()) {
            return null;
        }
        DeclarationDescriptor b11 = b10.b();
        if (b11 instanceof ClassDescriptor) {
            return ((ClassDescriptor) b11).S0();
        }
        return null;
    }

    @Override // sb.FunctionDescriptorImpl
    /* renamed from: v1, reason: merged with bridge method [inline-methods] */
    public ClassConstructorDescriptor T0(DeclarationDescriptor declarationDescriptor, Modality modality, pb.u uVar, CallableMemberDescriptor.a aVar, boolean z10) {
        ClassConstructorDescriptor classConstructorDescriptor = (ClassConstructorDescriptor) super.T0(declarationDescriptor, modality, uVar, aVar, z10);
        if (classConstructorDescriptor == null) {
            P(27);
        }
        return classConstructorDescriptor;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sb.FunctionDescriptorImpl
    /* renamed from: x1, reason: merged with bridge method [inline-methods] */
    public ClassConstructorDescriptorImpl U0(DeclarationDescriptor declarationDescriptor, FunctionDescriptor functionDescriptor, CallableMemberDescriptor.a aVar, Name name, qb.g gVar, SourceElement sourceElement) {
        if (declarationDescriptor == null) {
            P(23);
        }
        if (aVar == null) {
            P(24);
        }
        if (gVar == null) {
            P(25);
        }
        if (sourceElement == null) {
            P(26);
        }
        CallableMemberDescriptor.a aVar2 = CallableMemberDescriptor.a.DECLARATION;
        if (aVar != aVar2 && aVar != CallableMemberDescriptor.a.SYNTHESIZED) {
            throw new IllegalStateException("Attempt at creating a constructor that is not a declaration: \ncopy from: " + this + "\nnewOwner: " + declarationDescriptor + "\nkind: " + aVar);
        }
        return new ClassConstructorDescriptorImpl((ClassDescriptor) declarationDescriptor, this, gVar, this.I, aVar2, sourceElement);
    }

    @Override // sb.DeclarationDescriptorNonRootImpl, pb.DeclarationDescriptor
    /* renamed from: y1, reason: merged with bridge method [inline-methods] */
    public ClassDescriptor b() {
        ClassDescriptor classDescriptor = (ClassDescriptor) super.b();
        if (classDescriptor == null) {
            P(17);
        }
        return classDescriptor;
    }

    public ClassConstructorDescriptorImpl z1(List<ValueParameterDescriptor> list, pb.u uVar) {
        if (list == null) {
            P(13);
        }
        if (uVar == null) {
            P(14);
        }
        A1(list, uVar, b().B());
        return this;
    }

    @Override // sb.FunctionDescriptorImpl, pb.FunctionDescriptor, pb.Substitutable
    public ClassConstructorDescriptor c(TypeSubstitutor typeSubstitutor) {
        if (typeSubstitutor == null) {
            P(20);
        }
        return (ClassConstructorDescriptor) super.c(typeSubstitutor);
    }

    @Override // sb.FunctionDescriptorImpl, sb.DeclarationDescriptorNonRootImpl, sb.DeclarationDescriptorImpl, pb.DeclarationDescriptor
    /* renamed from: a */
    public ClassConstructorDescriptor T0() {
        ClassConstructorDescriptor classConstructorDescriptor = (ClassConstructorDescriptor) super.T0();
        if (classConstructorDescriptor == null) {
            P(19);
        }
        return classConstructorDescriptor;
    }
}
