package sb;

import ad.TransientReceiver;
import gd.TypeSubstitutor;
import gd.Variance;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import oc.SpecialNames;
import pb.CallableDescriptor;
import pb.ClassDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.DescriptorVisibilities;
import pb.ParameterDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;

/* compiled from: AbstractReceiverParameterDescriptor.java */
/* renamed from: sb.c, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractReceiverParameterDescriptor extends DeclarationDescriptorImpl implements ReceiverParameterDescriptor {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AbstractReceiverParameterDescriptor(qb.g gVar) {
        super(gVar, SpecialNames.f16454i);
        if (gVar == null) {
            P(0);
        }
    }

    private static /* synthetic */ void P(int i10) {
        String str;
        int i11;
        switch (i10) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                str = "@NotNull method %s.%s must not return null";
                break;
            default:
                str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
        }
        switch (i10) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                i11 = 2;
                break;
            default:
                i11 = 3;
                break;
        }
        Object[] objArr = new Object[i11];
        switch (i10) {
            case 1:
                objArr[0] = "substitutor";
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/AbstractReceiverParameterDescriptor";
                break;
            default:
                objArr[0] = "annotations";
                break;
        }
        switch (i10) {
            case 2:
                objArr[1] = "getContextReceiverParameters";
                break;
            case 3:
                objArr[1] = "getTypeParameters";
                break;
            case 4:
                objArr[1] = "getType";
                break;
            case 5:
                objArr[1] = "getValueParameters";
                break;
            case 6:
                objArr[1] = "getOverriddenDescriptors";
                break;
            case 7:
                objArr[1] = "getVisibility";
                break;
            case 8:
                objArr[1] = "getOriginal";
                break;
            case 9:
                objArr[1] = "getSource";
                break;
            default:
                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/AbstractReceiverParameterDescriptor";
                break;
        }
        switch (i10) {
            case 1:
                objArr[2] = "substitute";
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        switch (i10) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                throw new IllegalStateException(format);
            default:
                throw new IllegalArgumentException(format);
        }
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        return declarationDescriptorVisitor.m(this, d10);
    }

    @Override // sb.DeclarationDescriptorImpl, pb.DeclarationDescriptor
    /* renamed from: J0, reason: merged with bridge method [inline-methods] */
    public ParameterDescriptor a() {
        return this;
    }

    @Override // pb.CallableDescriptor
    public boolean O() {
        return false;
    }

    @Override // pb.CallableDescriptor
    public Collection<? extends CallableDescriptor> e() {
        Set emptySet = Collections.emptySet();
        if (emptySet == null) {
            P(6);
        }
        return emptySet;
    }

    @Override // pb.CallableDescriptor
    public gd.g0 f() {
        return getType();
    }

    @Override // pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public pb.u g() {
        pb.u uVar = DescriptorVisibilities.f16734f;
        if (uVar == null) {
            P(7);
        }
        return uVar;
    }

    @Override // pb.ValueDescriptor
    public gd.g0 getType() {
        gd.g0 type = getValue().getType();
        if (type == null) {
            P(4);
        }
        return type;
    }

    @Override // pb.CallableDescriptor
    public List<ValueParameterDescriptor> l() {
        List<ValueParameterDescriptor> emptyList = Collections.emptyList();
        if (emptyList == null) {
            P(5);
        }
        return emptyList;
    }

    @Override // pb.CallableDescriptor
    public List<TypeParameterDescriptor> m() {
        List<TypeParameterDescriptor> emptyList = Collections.emptyList();
        if (emptyList == null) {
            P(3);
        }
        return emptyList;
    }

    @Override // pb.CallableDescriptor
    public ReceiverParameterDescriptor m0() {
        return null;
    }

    @Override // pb.CallableDescriptor
    public ReceiverParameterDescriptor r0() {
        return null;
    }

    @Override // pb.DeclarationDescriptorWithSource
    public SourceElement z() {
        SourceElement sourceElement = SourceElement.f16664a;
        if (sourceElement == null) {
            P(9);
        }
        return sourceElement;
    }

    @Override // pb.Substitutable
    public CallableDescriptor c(TypeSubstitutor typeSubstitutor) {
        gd.g0 p10;
        if (typeSubstitutor == null) {
            P(1);
        }
        if (typeSubstitutor.k()) {
            return this;
        }
        if (b() instanceof ClassDescriptor) {
            p10 = typeSubstitutor.p(getType(), Variance.OUT_VARIANCE);
        } else {
            p10 = typeSubstitutor.p(getType(), Variance.INVARIANT);
        }
        if (p10 == null) {
            return null;
        }
        return p10 == getType() ? this : new ReceiverParameterDescriptorImpl(b(), new TransientReceiver(p10), i());
    }
}
