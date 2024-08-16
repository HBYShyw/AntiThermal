package sb;

import gd.TypeSubstitutor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorWithSource;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.PropertyAccessorDescriptor;
import pb.PropertyDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;

/* compiled from: PropertyAccessorDescriptorImpl.java */
/* renamed from: sb.b0, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class PropertyAccessorDescriptorImpl extends DeclarationDescriptorNonRootImpl implements PropertyAccessorDescriptor {

    /* renamed from: i, reason: collision with root package name */
    private boolean f18214i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f18215j;

    /* renamed from: k, reason: collision with root package name */
    private final Modality f18216k;

    /* renamed from: l, reason: collision with root package name */
    private final PropertyDescriptor f18217l;

    /* renamed from: m, reason: collision with root package name */
    private final boolean f18218m;

    /* renamed from: n, reason: collision with root package name */
    private final CallableMemberDescriptor.a f18219n;

    /* renamed from: o, reason: collision with root package name */
    private pb.u f18220o;

    /* renamed from: p, reason: collision with root package name */
    private FunctionDescriptor f18221p;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PropertyAccessorDescriptorImpl(Modality modality, pb.u uVar, PropertyDescriptor propertyDescriptor, qb.g gVar, Name name, boolean z10, boolean z11, boolean z12, CallableMemberDescriptor.a aVar, SourceElement sourceElement) {
        super(propertyDescriptor.b(), gVar, name, sourceElement);
        if (modality == null) {
            P(0);
        }
        if (uVar == null) {
            P(1);
        }
        if (propertyDescriptor == null) {
            P(2);
        }
        if (gVar == null) {
            P(3);
        }
        if (name == null) {
            P(4);
        }
        if (sourceElement == null) {
            P(5);
        }
        this.f18221p = null;
        this.f18216k = modality;
        this.f18220o = uVar;
        this.f18217l = propertyDescriptor;
        this.f18214i = z10;
        this.f18215j = z11;
        this.f18218m = z12;
        this.f18219n = aVar;
    }

    private static /* synthetic */ void P(int i10) {
        String str;
        int i11;
        switch (i10) {
            case 6:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                str = "@NotNull method %s.%s must not return null";
                break;
            case 7:
            default:
                str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
        }
        switch (i10) {
            case 6:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                i11 = 2;
                break;
            case 7:
            default:
                i11 = 3;
                break;
        }
        Object[] objArr = new Object[i11];
        switch (i10) {
            case 1:
                objArr[0] = "visibility";
                break;
            case 2:
                objArr[0] = "correspondingProperty";
                break;
            case 3:
                objArr[0] = "annotations";
                break;
            case 4:
                objArr[0] = "name";
                break;
            case 5:
                objArr[0] = "source";
                break;
            case 6:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/PropertyAccessorDescriptorImpl";
                break;
            case 7:
                objArr[0] = "substitutor";
                break;
            case 16:
                objArr[0] = "overriddenDescriptors";
                break;
            default:
                objArr[0] = "modality";
                break;
        }
        switch (i10) {
            case 6:
                objArr[1] = "getKind";
                break;
            case 7:
            default:
                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/PropertyAccessorDescriptorImpl";
                break;
            case 8:
                objArr[1] = "substitute";
                break;
            case 9:
                objArr[1] = "getTypeParameters";
                break;
            case 10:
                objArr[1] = "getModality";
                break;
            case 11:
                objArr[1] = "getVisibility";
                break;
            case 12:
                objArr[1] = "getCorrespondingVariable";
                break;
            case 13:
                objArr[1] = "getCorrespondingProperty";
                break;
            case 14:
                objArr[1] = "getContextReceiverParameters";
                break;
            case 15:
                objArr[1] = "getOverriddenDescriptors";
                break;
        }
        switch (i10) {
            case 6:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                break;
            case 7:
                objArr[2] = "substitute";
                break;
            case 16:
                objArr[2] = "setOverriddenDescriptors";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        switch (i10) {
            case 6:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                throw new IllegalStateException(format);
            case 7:
            default:
                throw new IllegalArgumentException(format);
        }
    }

    @Override // pb.FunctionDescriptor
    public boolean B0() {
        return false;
    }

    @Override // pb.FunctionDescriptor
    public boolean C0() {
        return false;
    }

    @Override // pb.MemberDescriptor
    public boolean D() {
        return this.f18215j;
    }

    @Override // pb.CallableMemberDescriptor
    public void D0(Collection<? extends CallableMemberDescriptor> collection) {
        if (collection == null) {
            P(16);
        }
    }

    @Override // pb.CallableDescriptor
    public <V> V E(CallableDescriptor.a<V> aVar) {
        return null;
    }

    @Override // pb.PropertyAccessorDescriptor
    public PropertyDescriptor K0() {
        PropertyDescriptor propertyDescriptor = this.f18217l;
        if (propertyDescriptor == null) {
            P(13);
        }
        return propertyDescriptor;
    }

    @Override // pb.FunctionDescriptor
    public boolean L0() {
        return false;
    }

    @Override // pb.MemberDescriptor
    public boolean N0() {
        return false;
    }

    @Override // pb.CallableDescriptor
    public boolean O() {
        return false;
    }

    @Override // pb.CallableMemberDescriptor
    /* renamed from: O0, reason: merged with bridge method [inline-methods] */
    public PropertyAccessorDescriptor T0(DeclarationDescriptor declarationDescriptor, Modality modality, pb.u uVar, CallableMemberDescriptor.a aVar, boolean z10) {
        throw new UnsupportedOperationException("Accessors must be copied by the corresponding property");
    }

    @Override // pb.FunctionDescriptor
    public boolean Q0() {
        return false;
    }

    @Override // sb.DeclarationDescriptorNonRootImpl, sb.DeclarationDescriptorImpl, pb.DeclarationDescriptor
    public abstract PropertyAccessorDescriptor a();

    @Override // pb.MemberDescriptor
    public boolean U() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Collection<PropertyAccessorDescriptor> U0(boolean z10) {
        ArrayList arrayList = new ArrayList(0);
        for (PropertyDescriptor propertyDescriptor : K0().e()) {
            DeclarationDescriptorWithSource h10 = z10 ? propertyDescriptor.h() : propertyDescriptor.k();
            if (h10 != null) {
                arrayList.add(h10);
            }
        }
        return arrayList;
    }

    public void V0(boolean z10) {
        this.f18214i = z10;
    }

    public void W0(FunctionDescriptor functionDescriptor) {
        this.f18221p = functionDescriptor;
    }

    @Override // pb.FunctionDescriptor
    public boolean X() {
        return false;
    }

    public void X0(pb.u uVar) {
        this.f18220o = uVar;
    }

    @Override // pb.FunctionDescriptor
    public boolean Y() {
        return false;
    }

    @Override // pb.Substitutable
    public FunctionDescriptor c(TypeSubstitutor typeSubstitutor) {
        if (typeSubstitutor == null) {
            P(7);
        }
        return this;
    }

    @Override // pb.PropertyAccessorDescriptor
    public boolean d0() {
        return this.f18214i;
    }

    @Override // pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public pb.u g() {
        pb.u uVar = this.f18220o;
        if (uVar == null) {
            P(11);
        }
        return uVar;
    }

    @Override // pb.CallableMemberDescriptor
    public CallableMemberDescriptor.a getKind() {
        CallableMemberDescriptor.a aVar = this.f18219n;
        if (aVar == null) {
            P(6);
        }
        return aVar;
    }

    @Override // pb.FunctionDescriptor
    public FunctionDescriptor l0() {
        return this.f18221p;
    }

    @Override // pb.CallableDescriptor
    public List<TypeParameterDescriptor> m() {
        List<TypeParameterDescriptor> emptyList = Collections.emptyList();
        if (emptyList == null) {
            P(9);
        }
        return emptyList;
    }

    @Override // pb.CallableDescriptor
    public ReceiverParameterDescriptor m0() {
        return K0().m0();
    }

    @Override // pb.MemberDescriptor
    public Modality o() {
        Modality modality = this.f18216k;
        if (modality == null) {
            P(10);
        }
        return modality;
    }

    @Override // pb.CallableDescriptor
    public ReceiverParameterDescriptor r0() {
        return K0().r0();
    }

    @Override // pb.CallableDescriptor
    public List<ReceiverParameterDescriptor> w0() {
        List<ReceiverParameterDescriptor> w02 = K0().w0();
        if (w02 == null) {
            P(14);
        }
        return w02;
    }

    @Override // pb.FunctionDescriptor
    public boolean y() {
        return this.f18218m;
    }
}
