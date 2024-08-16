package sb;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.ReceiverParameterDescriptor;
import pb.SimpleFunctionDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;

/* compiled from: SimpleFunctionDescriptorImpl.java */
/* renamed from: sb.g0, reason: use source file name */
/* loaded from: classes2.dex */
public class SimpleFunctionDescriptorImpl extends FunctionDescriptorImpl implements SimpleFunctionDescriptor {
    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SimpleFunctionDescriptorImpl(DeclarationDescriptor declarationDescriptor, SimpleFunctionDescriptor simpleFunctionDescriptor, qb.g gVar, Name name, CallableMemberDescriptor.a aVar, SourceElement sourceElement) {
        super(declarationDescriptor, simpleFunctionDescriptor, gVar, name, aVar, sourceElement);
        if (declarationDescriptor == null) {
            P(0);
        }
        if (gVar == null) {
            P(1);
        }
        if (name == null) {
            P(2);
        }
        if (aVar == null) {
            P(3);
        }
        if (sourceElement == null) {
            P(4);
        }
    }

    private static /* synthetic */ void P(int i10) {
        String str = (i10 == 13 || i10 == 18 || i10 == 23 || i10 == 24 || i10 == 29 || i10 == 30) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 13 || i10 == 18 || i10 == 23 || i10 == 24 || i10 == 29 || i10 == 30) ? 2 : 3];
        switch (i10) {
            case 1:
            case 6:
            case 27:
                objArr[0] = "annotations";
                break;
            case 2:
            case 7:
                objArr[0] = "name";
                break;
            case 3:
            case 8:
            case 26:
                objArr[0] = "kind";
                break;
            case 4:
            case 9:
            case 28:
                objArr[0] = "source";
                break;
            case 5:
            default:
                objArr[0] = "containingDeclaration";
                break;
            case 10:
            case 15:
            case 20:
                objArr[0] = "typeParameters";
                break;
            case 11:
            case 16:
            case 21:
                objArr[0] = "unsubstitutedValueParameters";
                break;
            case 12:
            case 17:
            case 22:
                objArr[0] = "visibility";
                break;
            case 13:
            case 18:
            case 23:
            case 24:
            case 29:
            case 30:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/SimpleFunctionDescriptorImpl";
                break;
            case 14:
            case 19:
                objArr[0] = "contextReceiverParameters";
                break;
            case 25:
                objArr[0] = "newOwner";
                break;
        }
        if (i10 == 13 || i10 == 18 || i10 == 23) {
            objArr[1] = "initialize";
        } else if (i10 == 24) {
            objArr[1] = "getOriginal";
        } else if (i10 == 29) {
            objArr[1] = "copy";
        } else if (i10 != 30) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/SimpleFunctionDescriptorImpl";
        } else {
            objArr[1] = "newCopyBuilder";
        }
        switch (i10) {
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                objArr[2] = "create";
                break;
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            case 16:
            case 17:
            case 19:
            case 20:
            case 21:
            case 22:
                objArr[2] = "initialize";
                break;
            case 13:
            case 18:
            case 23:
            case 24:
            case 29:
            case 30:
                break;
            case 25:
            case 26:
            case 27:
            case 28:
                objArr[2] = "createSubstitutedCopy";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        if (i10 != 13 && i10 != 18 && i10 != 23 && i10 != 24 && i10 != 29 && i10 != 30) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    public static SimpleFunctionDescriptorImpl u1(DeclarationDescriptor declarationDescriptor, qb.g gVar, Name name, CallableMemberDescriptor.a aVar, SourceElement sourceElement) {
        if (declarationDescriptor == null) {
            P(5);
        }
        if (gVar == null) {
            P(6);
        }
        if (name == null) {
            P(7);
        }
        if (aVar == null) {
            P(8);
        }
        if (sourceElement == null) {
            P(9);
        }
        return new SimpleFunctionDescriptorImpl(declarationDescriptor, null, gVar, name, aVar, sourceElement);
    }

    @Override // sb.FunctionDescriptorImpl, pb.FunctionDescriptor, pb.SimpleFunctionDescriptor
    public FunctionDescriptor.a<? extends SimpleFunctionDescriptor> A() {
        FunctionDescriptor.a A = super.A();
        if (A == null) {
            P(30);
        }
        return A;
    }

    @Override // sb.FunctionDescriptorImpl
    protected FunctionDescriptorImpl U0(DeclarationDescriptor declarationDescriptor, FunctionDescriptor functionDescriptor, CallableMemberDescriptor.a aVar, Name name, qb.g gVar, SourceElement sourceElement) {
        if (declarationDescriptor == null) {
            P(25);
        }
        if (aVar == null) {
            P(26);
        }
        if (gVar == null) {
            P(27);
        }
        if (sourceElement == null) {
            P(28);
        }
        SimpleFunctionDescriptor simpleFunctionDescriptor = (SimpleFunctionDescriptor) functionDescriptor;
        if (name == null) {
            name = getName();
        }
        return new SimpleFunctionDescriptorImpl(declarationDescriptor, simpleFunctionDescriptor, gVar, name, aVar, sourceElement);
    }

    @Override // sb.FunctionDescriptorImpl
    /* renamed from: t1, reason: merged with bridge method [inline-methods] */
    public SimpleFunctionDescriptor T0(DeclarationDescriptor declarationDescriptor, Modality modality, pb.u uVar, CallableMemberDescriptor.a aVar, boolean z10) {
        SimpleFunctionDescriptor simpleFunctionDescriptor = (SimpleFunctionDescriptor) super.T0(declarationDescriptor, modality, uVar, aVar, z10);
        if (simpleFunctionDescriptor == null) {
            P(29);
        }
        return simpleFunctionDescriptor;
    }

    @Override // sb.FunctionDescriptorImpl, sb.DeclarationDescriptorNonRootImpl, sb.DeclarationDescriptorImpl, pb.DeclarationDescriptor
    /* renamed from: v1, reason: merged with bridge method [inline-methods] */
    public SimpleFunctionDescriptor a() {
        SimpleFunctionDescriptor simpleFunctionDescriptor = (SimpleFunctionDescriptor) super.a();
        if (simpleFunctionDescriptor == null) {
            P(24);
        }
        return simpleFunctionDescriptor;
    }

    @Override // sb.FunctionDescriptorImpl
    /* renamed from: w1, reason: merged with bridge method [inline-methods] */
    public SimpleFunctionDescriptorImpl a1(ReceiverParameterDescriptor receiverParameterDescriptor, ReceiverParameterDescriptor receiverParameterDescriptor2, List<ReceiverParameterDescriptor> list, List<? extends TypeParameterDescriptor> list2, List<ValueParameterDescriptor> list3, gd.g0 g0Var, Modality modality, pb.u uVar) {
        if (list == null) {
            P(14);
        }
        if (list2 == null) {
            P(15);
        }
        if (list3 == null) {
            P(16);
        }
        if (uVar == null) {
            P(17);
        }
        SimpleFunctionDescriptorImpl x12 = x1(receiverParameterDescriptor, receiverParameterDescriptor2, list, list2, list3, g0Var, modality, uVar, null);
        if (x12 == null) {
            P(18);
        }
        return x12;
    }

    public SimpleFunctionDescriptorImpl x1(ReceiverParameterDescriptor receiverParameterDescriptor, ReceiverParameterDescriptor receiverParameterDescriptor2, List<ReceiverParameterDescriptor> list, List<? extends TypeParameterDescriptor> list2, List<ValueParameterDescriptor> list3, gd.g0 g0Var, Modality modality, pb.u uVar, Map<? extends CallableDescriptor.a<?>, ?> map) {
        if (list == null) {
            P(19);
        }
        if (list2 == null) {
            P(20);
        }
        if (list3 == null) {
            P(21);
        }
        if (uVar == null) {
            P(22);
        }
        super.a1(receiverParameterDescriptor, receiverParameterDescriptor2, list, list2, list3, g0Var, modality, uVar);
        if (map != null && !map.isEmpty()) {
            this.H = new LinkedHashMap(map);
        }
        return this;
    }
}
