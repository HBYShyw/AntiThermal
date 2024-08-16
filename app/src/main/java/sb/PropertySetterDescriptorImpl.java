package sb;

import gd.o0;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import oc.Name;
import oc.SpecialNames;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.Modality;
import pb.PropertyAccessorDescriptor;
import pb.PropertyDescriptor;
import pb.PropertySetterDescriptor;
import pb.SourceElement;
import pb.ValueParameterDescriptor;

/* compiled from: PropertySetterDescriptorImpl.java */
/* renamed from: sb.e0, reason: use source file name */
/* loaded from: classes2.dex */
public class PropertySetterDescriptorImpl extends PropertyAccessorDescriptorImpl implements PropertySetterDescriptor {

    /* renamed from: q, reason: collision with root package name */
    private ValueParameterDescriptor f18271q;

    /* renamed from: r, reason: collision with root package name */
    private final PropertySetterDescriptor f18272r;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public PropertySetterDescriptorImpl(PropertyDescriptor propertyDescriptor, qb.g gVar, Modality modality, pb.u uVar, boolean z10, boolean z11, boolean z12, CallableMemberDescriptor.a aVar, PropertySetterDescriptor propertySetterDescriptor, SourceElement sourceElement) {
        super(modality, uVar, propertyDescriptor, gVar, Name.i("<set-" + propertyDescriptor.getName() + ">"), z10, z11, z12, aVar, sourceElement);
        PropertySetterDescriptorImpl propertySetterDescriptorImpl;
        PropertySetterDescriptorImpl propertySetterDescriptorImpl2;
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
        if (propertySetterDescriptor != 0) {
            propertySetterDescriptorImpl2 = this;
            propertySetterDescriptorImpl = propertySetterDescriptor;
        } else {
            propertySetterDescriptorImpl = this;
            propertySetterDescriptorImpl2 = propertySetterDescriptorImpl;
        }
        propertySetterDescriptorImpl2.f18272r = propertySetterDescriptorImpl;
    }

    private static /* synthetic */ void P(int i10) {
        String str;
        int i11;
        switch (i10) {
            case 10:
            case 11:
            case 12:
            case 13:
                str = "@NotNull method %s.%s must not return null";
                break;
            default:
                str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
        }
        switch (i10) {
            case 10:
            case 11:
            case 12:
            case 13:
                i11 = 2;
                break;
            default:
                i11 = 3;
                break;
        }
        Object[] objArr = new Object[i11];
        switch (i10) {
            case 1:
            case 9:
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
                objArr[0] = "parameter";
                break;
            case 7:
                objArr[0] = "setterDescriptor";
                break;
            case 8:
                objArr[0] = "type";
                break;
            case 10:
            case 11:
            case 12:
            case 13:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/PropertySetterDescriptorImpl";
                break;
            default:
                objArr[0] = "correspondingProperty";
                break;
        }
        switch (i10) {
            case 10:
                objArr[1] = "getOverriddenDescriptors";
                break;
            case 11:
                objArr[1] = "getValueParameters";
                break;
            case 12:
                objArr[1] = "getReturnType";
                break;
            case 13:
                objArr[1] = "getOriginal";
                break;
            default:
                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/PropertySetterDescriptorImpl";
                break;
        }
        switch (i10) {
            case 6:
                objArr[2] = "initialize";
                break;
            case 7:
            case 8:
            case 9:
                objArr[2] = "createSetterParameter";
                break;
            case 10:
            case 11:
            case 12:
            case 13:
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        switch (i10) {
            case 10:
            case 11:
            case 12:
            case 13:
                throw new IllegalStateException(format);
            default:
                throw new IllegalArgumentException(format);
        }
    }

    public static ValueParameterDescriptorImpl Y0(PropertySetterDescriptor propertySetterDescriptor, gd.g0 g0Var, qb.g gVar) {
        if (propertySetterDescriptor == null) {
            P(7);
        }
        if (g0Var == null) {
            P(8);
        }
        if (gVar == null) {
            P(9);
        }
        return new ValueParameterDescriptorImpl(propertySetterDescriptor, null, 0, gVar, SpecialNames.f16460o, g0Var, false, false, false, null, SourceElement.f16664a);
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        return declarationDescriptorVisitor.a(this, d10);
    }

    @Override // sb.PropertyAccessorDescriptorImpl, sb.DeclarationDescriptorNonRootImpl, sb.DeclarationDescriptorImpl, pb.DeclarationDescriptor
    /* renamed from: Z0, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public PropertySetterDescriptor T0() {
        PropertySetterDescriptor propertySetterDescriptor = this.f18272r;
        if (propertySetterDescriptor == null) {
            P(13);
        }
        return propertySetterDescriptor;
    }

    public void a1(ValueParameterDescriptor valueParameterDescriptor) {
        if (valueParameterDescriptor == null) {
            P(6);
        }
        this.f18271q = valueParameterDescriptor;
    }

    @Override // pb.FunctionDescriptor, pb.CallableMemberDescriptor, pb.CallableDescriptor
    public Collection<? extends PropertySetterDescriptor> e() {
        Collection<PropertyAccessorDescriptor> U0 = super.U0(false);
        if (U0 == null) {
            P(10);
        }
        return U0;
    }

    @Override // pb.CallableDescriptor
    public gd.g0 f() {
        o0 Z = wc.c.j(this).Z();
        if (Z == null) {
            P(12);
        }
        return Z;
    }

    @Override // pb.CallableDescriptor
    public List<ValueParameterDescriptor> l() {
        ValueParameterDescriptor valueParameterDescriptor = this.f18271q;
        if (valueParameterDescriptor != null) {
            List<ValueParameterDescriptor> singletonList = Collections.singletonList(valueParameterDescriptor);
            if (singletonList == null) {
                P(11);
            }
            return singletonList;
        }
        throw new IllegalStateException();
    }
}
