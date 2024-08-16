package sb;

import java.util.Collections;
import java.util.List;
import oc.Name;
import pb.DeclarationDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import pb.VariableDescriptor;

/* compiled from: VariableDescriptorImpl.java */
/* renamed from: sb.m0, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class VariableDescriptorImpl extends DeclarationDescriptorNonRootImpl implements VariableDescriptor {

    /* renamed from: i, reason: collision with root package name */
    protected gd.g0 f18305i;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public VariableDescriptorImpl(DeclarationDescriptor declarationDescriptor, qb.g gVar, Name name, gd.g0 g0Var, SourceElement sourceElement) {
        super(declarationDescriptor, gVar, name, sourceElement);
        if (declarationDescriptor == null) {
            P(0);
        }
        if (gVar == null) {
            P(1);
        }
        if (name == null) {
            P(2);
        }
        if (sourceElement == null) {
            P(3);
        }
        this.f18305i = g0Var;
    }

    private static /* synthetic */ void P(int i10) {
        String str;
        int i11;
        switch (i10) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                str = "@NotNull method %s.%s must not return null";
                break;
            default:
                str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
        }
        switch (i10) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                i11 = 2;
                break;
            default:
                i11 = 3;
                break;
        }
        Object[] objArr = new Object[i11];
        switch (i10) {
            case 1:
                objArr[0] = "annotations";
                break;
            case 2:
                objArr[0] = "name";
                break;
            case 3:
                objArr[0] = "source";
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/VariableDescriptorImpl";
                break;
            default:
                objArr[0] = "containingDeclaration";
                break;
        }
        switch (i10) {
            case 4:
                objArr[1] = "getType";
                break;
            case 5:
                objArr[1] = "getOriginal";
                break;
            case 6:
                objArr[1] = "getValueParameters";
                break;
            case 7:
                objArr[1] = "getOverriddenDescriptors";
                break;
            case 8:
                objArr[1] = "getTypeParameters";
                break;
            case 9:
                objArr[1] = "getContextReceiverParameters";
                break;
            case 10:
                objArr[1] = "getReturnType";
                break;
            default:
                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/VariableDescriptorImpl";
                break;
        }
        switch (i10) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        switch (i10) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                throw new IllegalStateException(format);
            default:
                throw new IllegalArgumentException(format);
        }
    }

    public boolean O() {
        return false;
    }

    public void O0(gd.g0 g0Var) {
        this.f18305i = g0Var;
    }

    public gd.g0 f() {
        gd.g0 type = getType();
        if (type == null) {
            P(10);
        }
        return type;
    }

    @Override // pb.ValueDescriptor
    public gd.g0 getType() {
        gd.g0 g0Var = this.f18305i;
        if (g0Var == null) {
            P(4);
        }
        return g0Var;
    }

    @Override // pb.CallableDescriptor
    public List<ValueParameterDescriptor> l() {
        List<ValueParameterDescriptor> emptyList = Collections.emptyList();
        if (emptyList == null) {
            P(6);
        }
        return emptyList;
    }

    public List<TypeParameterDescriptor> m() {
        List<TypeParameterDescriptor> emptyList = Collections.emptyList();
        if (emptyList == null) {
            P(8);
        }
        return emptyList;
    }

    public ReceiverParameterDescriptor m0() {
        return null;
    }

    public ReceiverParameterDescriptor r0() {
        return null;
    }
}
