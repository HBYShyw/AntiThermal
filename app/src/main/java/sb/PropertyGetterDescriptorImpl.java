package sb;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.Modality;
import pb.PropertyAccessorDescriptor;
import pb.PropertyDescriptor;
import pb.PropertyGetterDescriptor;
import pb.SourceElement;
import pb.ValueParameterDescriptor;

/* compiled from: PropertyGetterDescriptorImpl.java */
/* renamed from: sb.d0, reason: use source file name */
/* loaded from: classes2.dex */
public class PropertyGetterDescriptorImpl extends PropertyAccessorDescriptorImpl implements PropertyGetterDescriptor {

    /* renamed from: q, reason: collision with root package name */
    private gd.g0 f18255q;

    /* renamed from: r, reason: collision with root package name */
    private final PropertyGetterDescriptor f18256r;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public PropertyGetterDescriptorImpl(PropertyDescriptor propertyDescriptor, qb.g gVar, Modality modality, pb.u uVar, boolean z10, boolean z11, boolean z12, CallableMemberDescriptor.a aVar, PropertyGetterDescriptor propertyGetterDescriptor, SourceElement sourceElement) {
        super(modality, uVar, propertyDescriptor, gVar, Name.i("<get-" + propertyDescriptor.getName() + ">"), z10, z11, z12, aVar, sourceElement);
        PropertyGetterDescriptorImpl propertyGetterDescriptorImpl;
        PropertyGetterDescriptorImpl propertyGetterDescriptorImpl2;
        if (propertyDescriptor == null) {
            P(0);
        }
        if (gVar == null) {
            P(1);
        }
        if (modality == null) {
            P(2);
        }
        if (uVar == null) {
            P(3);
        }
        if (aVar == null) {
            P(4);
        }
        if (sourceElement == null) {
            P(5);
        }
        if (propertyGetterDescriptor != 0) {
            propertyGetterDescriptorImpl2 = this;
            propertyGetterDescriptorImpl = propertyGetterDescriptor;
        } else {
            propertyGetterDescriptorImpl = this;
            propertyGetterDescriptorImpl2 = propertyGetterDescriptorImpl;
        }
        propertyGetterDescriptorImpl2.f18256r = propertyGetterDescriptorImpl;
    }

    private static /* synthetic */ void P(int i10) {
        String str = (i10 == 6 || i10 == 7 || i10 == 8) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 6 || i10 == 7 || i10 == 8) ? 2 : 3];
        switch (i10) {
            case 1:
                objArr[0] = "annotations";
                break;
            case 2:
                objArr[0] = "modality";
                break;
            case 3:
                objArr[0] = "visibility";
                break;
            case 4:
                objArr[0] = "kind";
                break;
            case 5:
                objArr[0] = "source";
                break;
            case 6:
            case 7:
            case 8:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/PropertyGetterDescriptorImpl";
                break;
            default:
                objArr[0] = "correspondingProperty";
                break;
        }
        if (i10 == 6) {
            objArr[1] = "getOverriddenDescriptors";
        } else if (i10 == 7) {
            objArr[1] = "getValueParameters";
        } else if (i10 != 8) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/PropertyGetterDescriptorImpl";
        } else {
            objArr[1] = "getOriginal";
        }
        if (i10 != 6 && i10 != 7 && i10 != 8) {
            objArr[2] = "<init>";
        }
        String format = String.format(str, objArr);
        if (i10 != 6 && i10 != 7 && i10 != 8) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        return declarationDescriptorVisitor.k(this, d10);
    }

    @Override // sb.PropertyAccessorDescriptorImpl, sb.DeclarationDescriptorNonRootImpl, sb.DeclarationDescriptorImpl, pb.DeclarationDescriptor
    /* renamed from: Y0, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public PropertyGetterDescriptor a() {
        PropertyGetterDescriptor propertyGetterDescriptor = this.f18256r;
        if (propertyGetterDescriptor == null) {
            P(8);
        }
        return propertyGetterDescriptor;
    }

    public void Z0(gd.g0 g0Var) {
        if (g0Var == null) {
            g0Var = K0().getType();
        }
        this.f18255q = g0Var;
    }

    @Override // pb.FunctionDescriptor, pb.CallableMemberDescriptor, pb.CallableDescriptor
    public Collection<? extends PropertyGetterDescriptor> e() {
        Collection<PropertyAccessorDescriptor> U0 = super.U0(true);
        if (U0 == null) {
            P(6);
        }
        return U0;
    }

    @Override // pb.CallableDescriptor
    public gd.g0 f() {
        return this.f18255q;
    }

    @Override // pb.CallableDescriptor
    public List<ValueParameterDescriptor> l() {
        List<ValueParameterDescriptor> emptyList = Collections.emptyList();
        if (emptyList == null) {
            P(7);
        }
        return emptyList;
    }
}
