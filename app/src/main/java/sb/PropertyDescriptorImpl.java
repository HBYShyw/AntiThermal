package sb;

import ad.ContextReceiver;
import ad.ExtensionReceiver;
import ad.ImplicitReceiver;
import gd.DescriptorSubstitutor;
import gd.TypeSubstitutor;
import gd.Variance;
import gd.n1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.DescriptorVisibilities;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.PropertyAccessorDescriptor;
import pb.PropertyDescriptor;
import pb.PropertyGetterDescriptor;
import pb.PropertySetterDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import qd.SmartSet;

/* compiled from: PropertyDescriptorImpl.java */
/* renamed from: sb.c0, reason: use source file name */
/* loaded from: classes2.dex */
public class PropertyDescriptorImpl extends VariableDescriptorWithInitializerImpl implements PropertyDescriptor {
    private List<TypeParameterDescriptor> A;
    private PropertyGetterDescriptorImpl B;
    private PropertySetterDescriptor C;
    private boolean D;
    private pb.w E;
    private pb.w F;

    /* renamed from: m, reason: collision with root package name */
    private final Modality f18222m;

    /* renamed from: n, reason: collision with root package name */
    private pb.u f18223n;

    /* renamed from: o, reason: collision with root package name */
    private Collection<? extends PropertyDescriptor> f18224o;

    /* renamed from: p, reason: collision with root package name */
    private final PropertyDescriptor f18225p;

    /* renamed from: q, reason: collision with root package name */
    private final CallableMemberDescriptor.a f18226q;

    /* renamed from: r, reason: collision with root package name */
    private final boolean f18227r;

    /* renamed from: s, reason: collision with root package name */
    private final boolean f18228s;

    /* renamed from: t, reason: collision with root package name */
    private final boolean f18229t;

    /* renamed from: u, reason: collision with root package name */
    private final boolean f18230u;

    /* renamed from: v, reason: collision with root package name */
    private final boolean f18231v;

    /* renamed from: w, reason: collision with root package name */
    private final boolean f18232w;

    /* renamed from: x, reason: collision with root package name */
    private List<ReceiverParameterDescriptor> f18233x;

    /* renamed from: y, reason: collision with root package name */
    private ReceiverParameterDescriptor f18234y;

    /* renamed from: z, reason: collision with root package name */
    private ReceiverParameterDescriptor f18235z;

    /* compiled from: PropertyDescriptorImpl.java */
    /* renamed from: sb.c0$a */
    /* loaded from: classes2.dex */
    public class a {

        /* renamed from: a, reason: collision with root package name */
        private DeclarationDescriptor f18236a;

        /* renamed from: b, reason: collision with root package name */
        private Modality f18237b;

        /* renamed from: c, reason: collision with root package name */
        private pb.u f18238c;

        /* renamed from: f, reason: collision with root package name */
        private CallableMemberDescriptor.a f18241f;

        /* renamed from: i, reason: collision with root package name */
        private ReceiverParameterDescriptor f18244i;

        /* renamed from: k, reason: collision with root package name */
        private Name f18246k;

        /* renamed from: l, reason: collision with root package name */
        private gd.g0 f18247l;

        /* renamed from: d, reason: collision with root package name */
        private PropertyDescriptor f18239d = null;

        /* renamed from: e, reason: collision with root package name */
        private boolean f18240e = false;

        /* renamed from: g, reason: collision with root package name */
        private n1 f18242g = n1.f11857b;

        /* renamed from: h, reason: collision with root package name */
        private boolean f18243h = true;

        /* renamed from: j, reason: collision with root package name */
        private List<TypeParameterDescriptor> f18245j = null;

        public a() {
            this.f18236a = PropertyDescriptorImpl.this.b();
            this.f18237b = PropertyDescriptorImpl.this.o();
            this.f18238c = PropertyDescriptorImpl.this.g();
            this.f18241f = PropertyDescriptorImpl.this.getKind();
            this.f18244i = PropertyDescriptorImpl.this.f18234y;
            this.f18246k = PropertyDescriptorImpl.this.getName();
            this.f18247l = PropertyDescriptorImpl.this.getType();
        }

        private static /* synthetic */ void a(int i10) {
            String str = (i10 == 1 || i10 == 2 || i10 == 3 || i10 == 5 || i10 == 7 || i10 == 9 || i10 == 11 || i10 == 19 || i10 == 13 || i10 == 14 || i10 == 16 || i10 == 17) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
            Object[] objArr = new Object[(i10 == 1 || i10 == 2 || i10 == 3 || i10 == 5 || i10 == 7 || i10 == 9 || i10 == 11 || i10 == 19 || i10 == 13 || i10 == 14 || i10 == 16 || i10 == 17) ? 2 : 3];
            switch (i10) {
                case 1:
                case 2:
                case 3:
                case 5:
                case 7:
                case 9:
                case 11:
                case 13:
                case 14:
                case 16:
                case 17:
                case 19:
                    objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/PropertyDescriptorImpl$CopyConfiguration";
                    break;
                case 4:
                    objArr[0] = "type";
                    break;
                case 6:
                    objArr[0] = "modality";
                    break;
                case 8:
                    objArr[0] = "visibility";
                    break;
                case 10:
                    objArr[0] = "kind";
                    break;
                case 12:
                    objArr[0] = "typeParameters";
                    break;
                case 15:
                    objArr[0] = "substitution";
                    break;
                case 18:
                    objArr[0] = "name";
                    break;
                default:
                    objArr[0] = "owner";
                    break;
            }
            if (i10 == 1) {
                objArr[1] = "setOwner";
            } else if (i10 == 2) {
                objArr[1] = "setOriginal";
            } else if (i10 == 3) {
                objArr[1] = "setPreserveSourceElement";
            } else if (i10 == 5) {
                objArr[1] = "setReturnType";
            } else if (i10 == 7) {
                objArr[1] = "setModality";
            } else if (i10 == 9) {
                objArr[1] = "setVisibility";
            } else if (i10 == 11) {
                objArr[1] = "setKind";
            } else if (i10 == 19) {
                objArr[1] = "setName";
            } else if (i10 == 13) {
                objArr[1] = "setTypeParameters";
            } else if (i10 == 14) {
                objArr[1] = "setDispatchReceiverParameter";
            } else if (i10 == 16) {
                objArr[1] = "setSubstitution";
            } else if (i10 != 17) {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/PropertyDescriptorImpl$CopyConfiguration";
            } else {
                objArr[1] = "setCopyOverrides";
            }
            switch (i10) {
                case 1:
                case 2:
                case 3:
                case 5:
                case 7:
                case 9:
                case 11:
                case 13:
                case 14:
                case 16:
                case 17:
                case 19:
                    break;
                case 4:
                    objArr[2] = "setReturnType";
                    break;
                case 6:
                    objArr[2] = "setModality";
                    break;
                case 8:
                    objArr[2] = "setVisibility";
                    break;
                case 10:
                    objArr[2] = "setKind";
                    break;
                case 12:
                    objArr[2] = "setTypeParameters";
                    break;
                case 15:
                    objArr[2] = "setSubstitution";
                    break;
                case 18:
                    objArr[2] = "setName";
                    break;
                default:
                    objArr[2] = "setOwner";
                    break;
            }
            String format = String.format(str, objArr);
            if (i10 != 1 && i10 != 2 && i10 != 3 && i10 != 5 && i10 != 7 && i10 != 9 && i10 != 11 && i10 != 19 && i10 != 13 && i10 != 14 && i10 != 16 && i10 != 17) {
                throw new IllegalArgumentException(format);
            }
            throw new IllegalStateException(format);
        }

        public PropertyDescriptor n() {
            return PropertyDescriptorImpl.this.Z0(this);
        }

        PropertyGetterDescriptor o() {
            PropertyDescriptor propertyDescriptor = this.f18239d;
            if (propertyDescriptor == null) {
                return null;
            }
            return propertyDescriptor.h();
        }

        PropertySetterDescriptor p() {
            PropertyDescriptor propertyDescriptor = this.f18239d;
            if (propertyDescriptor == null) {
                return null;
            }
            return propertyDescriptor.k();
        }

        public a q(boolean z10) {
            this.f18243h = z10;
            return this;
        }

        public a r(CallableMemberDescriptor.a aVar) {
            if (aVar == null) {
                a(10);
            }
            this.f18241f = aVar;
            return this;
        }

        public a s(Modality modality) {
            if (modality == null) {
                a(6);
            }
            this.f18237b = modality;
            return this;
        }

        public a t(CallableMemberDescriptor callableMemberDescriptor) {
            this.f18239d = (PropertyDescriptor) callableMemberDescriptor;
            return this;
        }

        public a u(DeclarationDescriptor declarationDescriptor) {
            if (declarationDescriptor == null) {
                a(0);
            }
            this.f18236a = declarationDescriptor;
            return this;
        }

        public a v(n1 n1Var) {
            if (n1Var == null) {
                a(15);
            }
            this.f18242g = n1Var;
            return this;
        }

        public a w(pb.u uVar) {
            if (uVar == null) {
                a(8);
            }
            this.f18238c = uVar;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PropertyDescriptorImpl(DeclarationDescriptor declarationDescriptor, PropertyDescriptor propertyDescriptor, qb.g gVar, Modality modality, pb.u uVar, boolean z10, Name name, CallableMemberDescriptor.a aVar, SourceElement sourceElement, boolean z11, boolean z12, boolean z13, boolean z14, boolean z15, boolean z16) {
        super(declarationDescriptor, gVar, name, null, z10, sourceElement);
        if (declarationDescriptor == null) {
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
        if (name == null) {
            P(4);
        }
        if (aVar == null) {
            P(5);
        }
        if (sourceElement == null) {
            P(6);
        }
        this.f18224o = null;
        this.f18233x = Collections.emptyList();
        this.f18222m = modality;
        this.f18223n = uVar;
        this.f18225p = propertyDescriptor == null ? this : propertyDescriptor;
        this.f18226q = aVar;
        this.f18227r = z11;
        this.f18228s = z12;
        this.f18229t = z13;
        this.f18230u = z14;
        this.f18231v = z15;
        this.f18232w = z16;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x002a  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00e7  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00f1  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x011e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0129  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0099  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static /* synthetic */ void P(int i10) {
        String str;
        int i11;
        if (i10 != 28 && i10 != 38 && i10 != 39 && i10 != 41 && i10 != 42) {
            switch (i10) {
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                    break;
                default:
                    str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                    break;
            }
            if (i10 != 28 && i10 != 38 && i10 != 39 && i10 != 41 && i10 != 42) {
                switch (i10) {
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                        break;
                    default:
                        i11 = 3;
                        break;
                }
                Object[] objArr = new Object[i11];
                switch (i10) {
                    case 1:
                    case 8:
                        objArr[0] = "annotations";
                        break;
                    case 2:
                    case 9:
                        objArr[0] = "modality";
                        break;
                    case 3:
                    case 10:
                    case 20:
                        objArr[0] = "visibility";
                        break;
                    case 4:
                    case 11:
                        objArr[0] = "name";
                        break;
                    case 5:
                    case 12:
                    case 35:
                        objArr[0] = "kind";
                        break;
                    case 6:
                    case 13:
                    case 37:
                        objArr[0] = "source";
                        break;
                    case 7:
                    default:
                        objArr[0] = "containingDeclaration";
                        break;
                    case 14:
                        objArr[0] = "inType";
                        break;
                    case 15:
                    case 17:
                        objArr[0] = "outType";
                        break;
                    case 16:
                    case 18:
                        objArr[0] = "typeParameters";
                        break;
                    case 19:
                        objArr[0] = "contextReceiverParameters";
                        break;
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 28:
                    case 38:
                    case 39:
                    case 41:
                    case 42:
                        objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/PropertyDescriptorImpl";
                        break;
                    case 27:
                        objArr[0] = "originalSubstitutor";
                        break;
                    case 29:
                        objArr[0] = "copyConfiguration";
                        break;
                    case 30:
                        objArr[0] = "substitutor";
                        break;
                    case 31:
                        objArr[0] = "accessorDescriptor";
                        break;
                    case 32:
                        objArr[0] = "newOwner";
                        break;
                    case 33:
                        objArr[0] = "newModality";
                        break;
                    case 34:
                        objArr[0] = "newVisibility";
                        break;
                    case 36:
                        objArr[0] = "newName";
                        break;
                    case 40:
                        objArr[0] = "overriddenDescriptors";
                        break;
                }
                if (i10 != 28) {
                    objArr[1] = "getSourceToUseForCopy";
                } else if (i10 == 38) {
                    objArr[1] = "getOriginal";
                } else if (i10 == 39) {
                    objArr[1] = "getKind";
                } else if (i10 == 41) {
                    objArr[1] = "getOverriddenDescriptors";
                } else if (i10 != 42) {
                    switch (i10) {
                        case 21:
                            objArr[1] = "getTypeParameters";
                            break;
                        case 22:
                            objArr[1] = "getContextReceiverParameters";
                            break;
                        case 23:
                            objArr[1] = "getReturnType";
                            break;
                        case 24:
                            objArr[1] = "getModality";
                            break;
                        case 25:
                            objArr[1] = "getVisibility";
                            break;
                        case 26:
                            objArr[1] = "getAccessors";
                            break;
                        default:
                            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/PropertyDescriptorImpl";
                            break;
                    }
                } else {
                    objArr[1] = "copy";
                }
                switch (i10) {
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                        objArr[2] = "create";
                        break;
                    case 14:
                        objArr[2] = "setInType";
                        break;
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                        objArr[2] = "setType";
                        break;
                    case 20:
                        objArr[2] = "setVisibility";
                        break;
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 28:
                    case 38:
                    case 39:
                    case 41:
                    case 42:
                        break;
                    case 27:
                        objArr[2] = "substitute";
                        break;
                    case 29:
                        objArr[2] = "doSubstitute";
                        break;
                    case 30:
                    case 31:
                        objArr[2] = "getSubstitutedInitialSignatureDescriptor";
                        break;
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                        objArr[2] = "createSubstitutedCopy";
                        break;
                    case 40:
                        objArr[2] = "setOverriddenDescriptors";
                        break;
                    default:
                        objArr[2] = "<init>";
                        break;
                }
                String format = String.format(str, objArr);
                if (i10 != 28 && i10 != 38 && i10 != 39 && i10 != 41 && i10 != 42) {
                    switch (i10) {
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                        case 26:
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
            if (i10 != 28) {
            }
            switch (i10) {
            }
            String format2 = String.format(str, objArr2);
            if (i10 != 28) {
                switch (i10) {
                }
            }
            throw new IllegalStateException(format2);
        }
        str = "@NotNull method %s.%s must not return null";
        if (i10 != 28) {
            switch (i10) {
            }
            Object[] objArr22 = new Object[i11];
            switch (i10) {
            }
            if (i10 != 28) {
            }
            switch (i10) {
            }
            String format22 = String.format(str, objArr22);
            if (i10 != 28) {
            }
            throw new IllegalStateException(format22);
        }
        i11 = 2;
        Object[] objArr222 = new Object[i11];
        switch (i10) {
        }
        if (i10 != 28) {
        }
        switch (i10) {
        }
        String format222 = String.format(str, objArr222);
        if (i10 != 28) {
        }
        throw new IllegalStateException(format222);
    }

    public static PropertyDescriptorImpl X0(DeclarationDescriptor declarationDescriptor, qb.g gVar, Modality modality, pb.u uVar, boolean z10, Name name, CallableMemberDescriptor.a aVar, SourceElement sourceElement, boolean z11, boolean z12, boolean z13, boolean z14, boolean z15, boolean z16) {
        if (declarationDescriptor == null) {
            P(7);
        }
        if (gVar == null) {
            P(8);
        }
        if (modality == null) {
            P(9);
        }
        if (uVar == null) {
            P(10);
        }
        if (name == null) {
            P(11);
        }
        if (aVar == null) {
            P(12);
        }
        if (sourceElement == null) {
            P(13);
        }
        return new PropertyDescriptorImpl(declarationDescriptor, null, gVar, modality, uVar, z10, name, aVar, sourceElement, z11, z12, z13, z14, z15, z16);
    }

    private SourceElement b1(boolean z10, PropertyDescriptor propertyDescriptor) {
        SourceElement sourceElement;
        if (z10) {
            if (propertyDescriptor == null) {
                propertyDescriptor = a();
            }
            sourceElement = propertyDescriptor.z();
        } else {
            sourceElement = SourceElement.f16664a;
        }
        if (sourceElement == null) {
            P(28);
        }
        return sourceElement;
    }

    private static FunctionDescriptor c1(TypeSubstitutor typeSubstitutor, PropertyAccessorDescriptor propertyAccessorDescriptor) {
        if (typeSubstitutor == null) {
            P(30);
        }
        if (propertyAccessorDescriptor == null) {
            P(31);
        }
        if (propertyAccessorDescriptor.l0() != null) {
            return propertyAccessorDescriptor.l0().c(typeSubstitutor);
        }
        return null;
    }

    private static pb.u h1(pb.u uVar, CallableMemberDescriptor.a aVar) {
        return (aVar == CallableMemberDescriptor.a.FAKE_OVERRIDE && DescriptorVisibilities.g(uVar.f())) ? DescriptorVisibilities.f16736h : uVar;
    }

    private static ReceiverParameterDescriptor m1(TypeSubstitutor typeSubstitutor, PropertyDescriptor propertyDescriptor, ReceiverParameterDescriptor receiverParameterDescriptor) {
        gd.g0 p10 = typeSubstitutor.p(receiverParameterDescriptor.getType(), Variance.IN_VARIANCE);
        if (p10 == null) {
            return null;
        }
        return new ReceiverParameterDescriptorImpl(propertyDescriptor, new ContextReceiver(propertyDescriptor, p10, ((ImplicitReceiver) receiverParameterDescriptor.getValue()).a(), receiverParameterDescriptor.getValue()), receiverParameterDescriptor.i());
    }

    private static ReceiverParameterDescriptor n1(TypeSubstitutor typeSubstitutor, PropertyDescriptor propertyDescriptor, ReceiverParameterDescriptor receiverParameterDescriptor) {
        gd.g0 p10 = typeSubstitutor.p(receiverParameterDescriptor.getType(), Variance.IN_VARIANCE);
        if (p10 == null) {
            return null;
        }
        return new ReceiverParameterDescriptorImpl(propertyDescriptor, new ExtensionReceiver(propertyDescriptor, p10, receiverParameterDescriptor.getValue()), receiverParameterDescriptor.i());
    }

    @Override // pb.PropertyDescriptor
    public List<PropertyAccessorDescriptor> C() {
        ArrayList arrayList = new ArrayList(2);
        PropertyGetterDescriptorImpl propertyGetterDescriptorImpl = this.B;
        if (propertyGetterDescriptorImpl != null) {
            arrayList.add(propertyGetterDescriptorImpl);
        }
        PropertySetterDescriptor propertySetterDescriptor = this.C;
        if (propertySetterDescriptor != null) {
            arrayList.add(propertySetterDescriptor);
        }
        return arrayList;
    }

    public boolean D() {
        return this.f18231v;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // pb.CallableMemberDescriptor
    public void D0(Collection<? extends CallableMemberDescriptor> collection) {
        if (collection == 0) {
            P(40);
        }
        this.f18224o = collection;
    }

    public <V> V E(CallableDescriptor.a<V> aVar) {
        return null;
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        return declarationDescriptorVisitor.f(this, d10);
    }

    public boolean I() {
        return this.f18228s;
    }

    @Override // pb.MemberDescriptor
    public boolean N0() {
        return this.f18230u;
    }

    @Override // pb.MemberDescriptor
    public boolean U() {
        return this.f18229t;
    }

    @Override // pb.VariableDescriptorWithAccessors
    public boolean V() {
        return this.f18232w;
    }

    @Override // pb.CallableMemberDescriptor
    /* renamed from: W0, reason: merged with bridge method [inline-methods] */
    public PropertyDescriptor T0(DeclarationDescriptor declarationDescriptor, Modality modality, pb.u uVar, CallableMemberDescriptor.a aVar, boolean z10) {
        PropertyDescriptor n10 = g1().u(declarationDescriptor).t(null).s(modality).w(uVar).r(aVar).q(z10).n();
        if (n10 == null) {
            P(42);
        }
        return n10;
    }

    protected PropertyDescriptorImpl Y0(DeclarationDescriptor declarationDescriptor, Modality modality, pb.u uVar, PropertyDescriptor propertyDescriptor, CallableMemberDescriptor.a aVar, Name name, SourceElement sourceElement) {
        if (declarationDescriptor == null) {
            P(32);
        }
        if (modality == null) {
            P(33);
        }
        if (uVar == null) {
            P(34);
        }
        if (aVar == null) {
            P(35);
        }
        if (name == null) {
            P(36);
        }
        if (sourceElement == null) {
            P(37);
        }
        return new PropertyDescriptorImpl(declarationDescriptor, propertyDescriptor, i(), modality, uVar, p0(), name, aVar, sourceElement, x0(), I(), U(), N0(), D(), V());
    }

    protected PropertyDescriptor Z0(a aVar) {
        ReceiverParameterDescriptor receiverParameterDescriptor;
        ya.a<fd.j<uc.g<?>>> aVar2;
        if (aVar == null) {
            P(29);
        }
        PropertyDescriptorImpl Y0 = Y0(aVar.f18236a, aVar.f18237b, aVar.f18238c, aVar.f18239d, aVar.f18241f, aVar.f18246k, b1(aVar.f18240e, aVar.f18239d));
        List<TypeParameterDescriptor> m10 = aVar.f18245j == null ? m() : aVar.f18245j;
        ArrayList arrayList = new ArrayList(m10.size());
        TypeSubstitutor b10 = DescriptorSubstitutor.b(m10, aVar.f18242g, Y0, arrayList);
        gd.g0 g0Var = aVar.f18247l;
        gd.g0 p10 = b10.p(g0Var, Variance.OUT_VARIANCE);
        if (p10 == null) {
            return null;
        }
        gd.g0 p11 = b10.p(g0Var, Variance.IN_VARIANCE);
        if (p11 != null) {
            Y0.i1(p11);
        }
        ReceiverParameterDescriptor receiverParameterDescriptor2 = aVar.f18244i;
        if (receiverParameterDescriptor2 != null) {
            ReceiverParameterDescriptor c10 = receiverParameterDescriptor2.c(b10);
            if (c10 == null) {
                return null;
            }
            receiverParameterDescriptor = c10;
        } else {
            receiverParameterDescriptor = null;
        }
        ReceiverParameterDescriptor receiverParameterDescriptor3 = this.f18235z;
        ReceiverParameterDescriptor n12 = receiverParameterDescriptor3 != null ? n1(b10, Y0, receiverParameterDescriptor3) : null;
        ArrayList arrayList2 = new ArrayList();
        Iterator<ReceiverParameterDescriptor> it = this.f18233x.iterator();
        while (it.hasNext()) {
            ReceiverParameterDescriptor m12 = m1(b10, Y0, it.next());
            if (m12 != null) {
                arrayList2.add(m12);
            }
        }
        Y0.k1(p10, arrayList, receiverParameterDescriptor, n12, arrayList2);
        PropertyGetterDescriptorImpl propertyGetterDescriptorImpl = this.B == null ? null : new PropertyGetterDescriptorImpl(Y0, this.B.i(), aVar.f18237b, h1(this.B.g(), aVar.f18241f), this.B.d0(), this.B.D(), this.B.y(), aVar.f18241f, aVar.o(), SourceElement.f16664a);
        if (propertyGetterDescriptorImpl != null) {
            gd.g0 f10 = this.B.f();
            propertyGetterDescriptorImpl.W0(c1(b10, this.B));
            propertyGetterDescriptorImpl.Z0(f10 != null ? b10.p(f10, Variance.OUT_VARIANCE) : null);
        }
        PropertySetterDescriptorImpl propertySetterDescriptorImpl = this.C == null ? null : new PropertySetterDescriptorImpl(Y0, this.C.i(), aVar.f18237b, h1(this.C.g(), aVar.f18241f), this.C.d0(), this.C.D(), this.C.y(), aVar.f18241f, aVar.p(), SourceElement.f16664a);
        if (propertySetterDescriptorImpl != null) {
            List<ValueParameterDescriptor> Y02 = FunctionDescriptorImpl.Y0(propertySetterDescriptorImpl, this.C.l(), b10, false, false, null);
            if (Y02 == null) {
                Y0.j1(true);
                Y02 = Collections.singletonList(PropertySetterDescriptorImpl.Y0(propertySetterDescriptorImpl, wc.c.j(aVar.f18236a).H(), this.C.l().get(0).i()));
            }
            if (Y02.size() == 1) {
                propertySetterDescriptorImpl.W0(c1(b10, this.C));
                propertySetterDescriptorImpl.a1(Y02.get(0));
            } else {
                throw new IllegalStateException();
            }
        }
        pb.w wVar = this.E;
        FieldDescriptorImpl fieldDescriptorImpl = wVar == null ? null : new FieldDescriptorImpl(wVar.i(), Y0);
        pb.w wVar2 = this.F;
        Y0.e1(propertyGetterDescriptorImpl, propertySetterDescriptorImpl, fieldDescriptorImpl, wVar2 != null ? new FieldDescriptorImpl(wVar2.i(), Y0) : null);
        if (aVar.f18243h) {
            SmartSet c11 = SmartSet.c();
            Iterator<? extends PropertyDescriptor> it2 = e().iterator();
            while (it2.hasNext()) {
                c11.add(it2.next().c(b10));
            }
            Y0.D0(c11);
        }
        if (I() && (aVar2 = this.f18324l) != null) {
            Y0.T0(this.f18323k, aVar2);
        }
        return Y0;
    }

    @Override // pb.PropertyDescriptor
    /* renamed from: a1, reason: merged with bridge method [inline-methods] */
    public PropertyGetterDescriptorImpl h() {
        return this.B;
    }

    public void d1(PropertyGetterDescriptorImpl propertyGetterDescriptorImpl, PropertySetterDescriptor propertySetterDescriptor) {
        e1(propertyGetterDescriptorImpl, propertySetterDescriptor, null, null);
    }

    @Override // pb.CallableDescriptor
    public Collection<? extends PropertyDescriptor> e() {
        Collection<? extends PropertyDescriptor> collection = this.f18224o;
        if (collection == null) {
            collection = Collections.emptyList();
        }
        if (collection == null) {
            P(41);
        }
        return collection;
    }

    public void e1(PropertyGetterDescriptorImpl propertyGetterDescriptorImpl, PropertySetterDescriptor propertySetterDescriptor, pb.w wVar, pb.w wVar2) {
        this.B = propertyGetterDescriptorImpl;
        this.C = propertySetterDescriptor;
        this.E = wVar;
        this.F = wVar2;
    }

    @Override // sb.VariableDescriptorImpl, pb.CallableDescriptor
    public gd.g0 f() {
        gd.g0 type = getType();
        if (type == null) {
            P(23);
        }
        return type;
    }

    public boolean f1() {
        return this.D;
    }

    @Override // pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public pb.u g() {
        pb.u uVar = this.f18223n;
        if (uVar == null) {
            P(25);
        }
        return uVar;
    }

    public a g1() {
        return new a();
    }

    @Override // pb.CallableMemberDescriptor
    public CallableMemberDescriptor.a getKind() {
        CallableMemberDescriptor.a aVar = this.f18226q;
        if (aVar == null) {
            P(39);
        }
        return aVar;
    }

    public void i1(gd.g0 g0Var) {
        if (g0Var == null) {
            P(14);
        }
    }

    public void j1(boolean z10) {
        this.D = z10;
    }

    @Override // pb.PropertyDescriptor
    public PropertySetterDescriptor k() {
        return this.C;
    }

    public void k1(gd.g0 g0Var, List<? extends TypeParameterDescriptor> list, ReceiverParameterDescriptor receiverParameterDescriptor, ReceiverParameterDescriptor receiverParameterDescriptor2, List<ReceiverParameterDescriptor> list2) {
        if (g0Var == null) {
            P(17);
        }
        if (list == null) {
            P(18);
        }
        if (list2 == null) {
            P(19);
        }
        O0(g0Var);
        this.A = new ArrayList(list);
        this.f18235z = receiverParameterDescriptor2;
        this.f18234y = receiverParameterDescriptor;
        this.f18233x = list2;
    }

    public void l1(pb.u uVar) {
        if (uVar == null) {
            P(20);
        }
        this.f18223n = uVar;
    }

    @Override // sb.VariableDescriptorImpl, pb.CallableDescriptor
    public List<TypeParameterDescriptor> m() {
        List<TypeParameterDescriptor> list = this.A;
        if (list != null) {
            return list;
        }
        throw new IllegalStateException("typeParameters == null for " + toString());
    }

    @Override // sb.VariableDescriptorImpl, pb.CallableDescriptor
    public ReceiverParameterDescriptor m0() {
        return this.f18234y;
    }

    @Override // pb.MemberDescriptor
    public Modality o() {
        Modality modality = this.f18222m;
        if (modality == null) {
            P(24);
        }
        return modality;
    }

    @Override // sb.VariableDescriptorImpl, pb.CallableDescriptor
    public ReceiverParameterDescriptor r0() {
        return this.f18235z;
    }

    @Override // pb.PropertyDescriptor
    public pb.w s0() {
        return this.F;
    }

    @Override // pb.PropertyDescriptor
    public pb.w v0() {
        return this.E;
    }

    @Override // pb.CallableDescriptor
    public List<ReceiverParameterDescriptor> w0() {
        List<ReceiverParameterDescriptor> list = this.f18233x;
        if (list == null) {
            P(22);
        }
        return list;
    }

    @Override // pb.VariableDescriptor
    public boolean x0() {
        return this.f18227r;
    }

    @Override // pb.Substitutable
    public CallableDescriptor c(TypeSubstitutor typeSubstitutor) {
        if (typeSubstitutor == null) {
            P(27);
        }
        return typeSubstitutor.k() ? this : g1().v(typeSubstitutor.j()).t(a()).n();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [pb.u0] */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4 */
    @Override // sb.DeclarationDescriptorNonRootImpl, sb.DeclarationDescriptorImpl, pb.DeclarationDescriptor
    public PropertyDescriptor a() {
        PropertyDescriptor propertyDescriptor = this.f18225p;
        ?? r12 = this;
        if (propertyDescriptor != this) {
            r12 = propertyDescriptor.a();
        }
        if (r12 == 0) {
            P(38);
        }
        return r12;
    }
}
