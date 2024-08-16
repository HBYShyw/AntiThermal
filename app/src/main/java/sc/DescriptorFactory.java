package sc;

import ad.ContextClassReceiver;
import ad.ContextReceiver;
import ad.ExtensionReceiver;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import gd.TypeProjectionImpl;
import gd.Variance;
import gd.c1;
import gd.g0;
import gd.h0;
import java.util.Collections;
import mb.StandardNames;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.DescriptorVisibilities;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.PropertyDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SimpleFunctionDescriptor;
import pb.SourceElement;
import pb.findClassInModule;
import pb.u;
import qb.g;
import sb.ClassConstructorDescriptorImpl;
import sb.PropertyDescriptorImpl;
import sb.PropertyGetterDescriptorImpl;
import sb.PropertySetterDescriptorImpl;
import sb.ReceiverParameterDescriptorImpl;
import sb.SimpleFunctionDescriptorImpl;
import sb.ValueParameterDescriptorImpl;

/* compiled from: DescriptorFactory.java */
/* renamed from: sc.d, reason: use source file name */
/* loaded from: classes2.dex */
public class DescriptorFactory {

    /* compiled from: DescriptorFactory.java */
    /* renamed from: sc.d$a */
    /* loaded from: classes2.dex */
    private static class a extends ClassConstructorDescriptorImpl {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public a(ClassDescriptor classDescriptor, SourceElement sourceElement, boolean z10) {
            super(classDescriptor, null, qb.g.f17195b.b(), true, CallableMemberDescriptor.a.DECLARATION, sourceElement);
            if (classDescriptor == null) {
                P(0);
            }
            if (sourceElement == null) {
                P(1);
            }
            z1(Collections.emptyList(), e.k(classDescriptor, z10));
        }

        private static /* synthetic */ void P(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "containingClass";
            } else {
                objArr[0] = "source";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/resolve/DescriptorFactory$DefaultClassConstructorDescriptor";
            objArr[2] = "<init>";
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }
    }

    private static /* synthetic */ void a(int i10) {
        String str = (i10 == 12 || i10 == 23 || i10 == 25) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 12 || i10 == 23 || i10 == 25) ? 2 : 3];
        switch (i10) {
            case 1:
            case 4:
            case 8:
            case 14:
            case 16:
            case 18:
            case 31:
            case 33:
            case 35:
                objArr[0] = "annotations";
                break;
            case 2:
            case 5:
            case 9:
                objArr[0] = "parameterAnnotations";
                break;
            case 3:
            case 7:
            case 13:
            case 15:
            case 17:
            default:
                objArr[0] = "propertyDescriptor";
                break;
            case 6:
            case 11:
            case 19:
                objArr[0] = "sourceElement";
                break;
            case 10:
                objArr[0] = "visibility";
                break;
            case 12:
            case 23:
            case 25:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/resolve/DescriptorFactory";
                break;
            case 20:
                objArr[0] = "containingClass";
                break;
            case 21:
                objArr[0] = "source";
                break;
            case 22:
            case 24:
            case 26:
                objArr[0] = "enumClass";
                break;
            case 27:
            case 28:
            case 29:
                objArr[0] = "descriptor";
                break;
            case 30:
            case 32:
            case 34:
                objArr[0] = "owner";
                break;
        }
        if (i10 == 12) {
            objArr[1] = "createSetter";
        } else if (i10 == 23) {
            objArr[1] = "createEnumValuesMethod";
        } else if (i10 != 25) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/resolve/DescriptorFactory";
        } else {
            objArr[1] = "createEnumValueOfMethod";
        }
        switch (i10) {
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                objArr[2] = "createSetter";
                break;
            case 12:
            case 23:
            case 25:
                break;
            case 13:
            case 14:
                objArr[2] = "createDefaultGetter";
                break;
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
                objArr[2] = "createGetter";
                break;
            case 20:
            case 21:
                objArr[2] = "createPrimaryConstructorForObject";
                break;
            case 22:
                objArr[2] = "createEnumValuesMethod";
                break;
            case 24:
                objArr[2] = "createEnumValueOfMethod";
                break;
            case 26:
                objArr[2] = "createEnumEntriesProperty";
                break;
            case 27:
                objArr[2] = "isEnumValuesMethod";
                break;
            case 28:
                objArr[2] = "isEnumValueOfMethod";
                break;
            case 29:
                objArr[2] = "isEnumSpecialMethod";
                break;
            case 30:
            case 31:
                objArr[2] = "createExtensionReceiverParameterForCallable";
                break;
            case 32:
            case 33:
                objArr[2] = "createContextReceiverParameterForCallable";
                break;
            case 34:
            case 35:
                objArr[2] = "createContextReceiverParameterForClass";
                break;
            default:
                objArr[2] = "createDefaultSetter";
                break;
        }
        String format = String.format(str, objArr);
        if (i10 != 12 && i10 != 23 && i10 != 25) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    public static ReceiverParameterDescriptor b(CallableDescriptor callableDescriptor, g0 g0Var, Name name, qb.g gVar) {
        if (callableDescriptor == null) {
            a(32);
        }
        if (gVar == null) {
            a(33);
        }
        if (g0Var == null) {
            return null;
        }
        return new ReceiverParameterDescriptorImpl(callableDescriptor, new ContextReceiver(callableDescriptor, g0Var, name, null), gVar);
    }

    public static ReceiverParameterDescriptor c(ClassDescriptor classDescriptor, g0 g0Var, Name name, qb.g gVar) {
        if (classDescriptor == null) {
            a(34);
        }
        if (gVar == null) {
            a(35);
        }
        if (g0Var == null) {
            return null;
        }
        return new ReceiverParameterDescriptorImpl(classDescriptor, new ContextClassReceiver(classDescriptor, g0Var, name, null), gVar);
    }

    public static PropertyGetterDescriptorImpl d(PropertyDescriptor propertyDescriptor, qb.g gVar) {
        if (propertyDescriptor == null) {
            a(13);
        }
        if (gVar == null) {
            a(14);
        }
        return j(propertyDescriptor, gVar, true, false, false);
    }

    public static PropertySetterDescriptorImpl e(PropertyDescriptor propertyDescriptor, qb.g gVar, qb.g gVar2) {
        if (propertyDescriptor == null) {
            a(0);
        }
        if (gVar == null) {
            a(1);
        }
        if (gVar2 == null) {
            a(2);
        }
        return n(propertyDescriptor, gVar, gVar2, true, false, false, propertyDescriptor.z());
    }

    public static PropertyDescriptor f(ClassDescriptor classDescriptor) {
        if (classDescriptor == null) {
            a(26);
        }
        ClassDescriptor a10 = findClassInModule.a(e.g(classDescriptor), oc.i.f16464a.i());
        if (a10 == null) {
            return null;
        }
        g.a aVar = qb.g.f17195b;
        qb.g b10 = aVar.b();
        Modality modality = Modality.FINAL;
        u uVar = DescriptorVisibilities.f16733e;
        Name name = StandardNames.f15267e;
        CallableMemberDescriptor.a aVar2 = CallableMemberDescriptor.a.SYNTHESIZED;
        PropertyDescriptorImpl X0 = PropertyDescriptorImpl.X0(classDescriptor, b10, modality, uVar, false, name, aVar2, classDescriptor.z(), false, false, false, false, false, false);
        PropertyGetterDescriptorImpl propertyGetterDescriptorImpl = new PropertyGetterDescriptorImpl(X0, aVar.b(), modality, uVar, false, false, false, aVar2, null, classDescriptor.z());
        X0.d1(propertyGetterDescriptorImpl, null);
        X0.k1(h0.h(c1.f11749f.h(), a10.n(), Collections.singletonList(new TypeProjectionImpl(classDescriptor.x())), false), Collections.emptyList(), null, null, Collections.emptyList());
        propertyGetterDescriptorImpl.Z0(X0.f());
        return X0;
    }

    public static SimpleFunctionDescriptor g(ClassDescriptor classDescriptor) {
        if (classDescriptor == null) {
            a(24);
        }
        g.a aVar = qb.g.f17195b;
        SimpleFunctionDescriptorImpl u12 = SimpleFunctionDescriptorImpl.u1(classDescriptor, aVar.b(), StandardNames.f15268f, CallableMemberDescriptor.a.SYNTHESIZED, classDescriptor.z());
        SimpleFunctionDescriptorImpl a12 = u12.a1(null, null, Collections.emptyList(), Collections.emptyList(), Collections.singletonList(new ValueParameterDescriptorImpl(u12, null, 0, aVar.b(), Name.f(ThermalBaseConfig.Item.ATTR_VALUE), wc.c.j(classDescriptor).W(), false, false, false, null, classDescriptor.z())), classDescriptor.x(), Modality.FINAL, DescriptorVisibilities.f16733e);
        if (a12 == null) {
            a(25);
        }
        return a12;
    }

    public static SimpleFunctionDescriptor h(ClassDescriptor classDescriptor) {
        if (classDescriptor == null) {
            a(22);
        }
        SimpleFunctionDescriptorImpl a12 = SimpleFunctionDescriptorImpl.u1(classDescriptor, qb.g.f17195b.b(), StandardNames.f15266d, CallableMemberDescriptor.a.SYNTHESIZED, classDescriptor.z()).a1(null, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), wc.c.j(classDescriptor).l(Variance.INVARIANT, classDescriptor.x()), Modality.FINAL, DescriptorVisibilities.f16733e);
        if (a12 == null) {
            a(23);
        }
        return a12;
    }

    public static ReceiverParameterDescriptor i(CallableDescriptor callableDescriptor, g0 g0Var, qb.g gVar) {
        if (callableDescriptor == null) {
            a(30);
        }
        if (gVar == null) {
            a(31);
        }
        if (g0Var == null) {
            return null;
        }
        return new ReceiverParameterDescriptorImpl(callableDescriptor, new ExtensionReceiver(callableDescriptor, g0Var, null), gVar);
    }

    public static PropertyGetterDescriptorImpl j(PropertyDescriptor propertyDescriptor, qb.g gVar, boolean z10, boolean z11, boolean z12) {
        if (propertyDescriptor == null) {
            a(15);
        }
        if (gVar == null) {
            a(16);
        }
        return k(propertyDescriptor, gVar, z10, z11, z12, propertyDescriptor.z());
    }

    public static PropertyGetterDescriptorImpl k(PropertyDescriptor propertyDescriptor, qb.g gVar, boolean z10, boolean z11, boolean z12, SourceElement sourceElement) {
        if (propertyDescriptor == null) {
            a(17);
        }
        if (gVar == null) {
            a(18);
        }
        if (sourceElement == null) {
            a(19);
        }
        return new PropertyGetterDescriptorImpl(propertyDescriptor, gVar, propertyDescriptor.o(), propertyDescriptor.g(), z10, z11, z12, CallableMemberDescriptor.a.DECLARATION, null, sourceElement);
    }

    public static ClassConstructorDescriptorImpl l(ClassDescriptor classDescriptor, SourceElement sourceElement) {
        if (classDescriptor == null) {
            a(20);
        }
        if (sourceElement == null) {
            a(21);
        }
        return new a(classDescriptor, sourceElement, false);
    }

    public static PropertySetterDescriptorImpl m(PropertyDescriptor propertyDescriptor, qb.g gVar, qb.g gVar2, boolean z10, boolean z11, boolean z12, u uVar, SourceElement sourceElement) {
        if (propertyDescriptor == null) {
            a(7);
        }
        if (gVar == null) {
            a(8);
        }
        if (gVar2 == null) {
            a(9);
        }
        if (uVar == null) {
            a(10);
        }
        if (sourceElement == null) {
            a(11);
        }
        PropertySetterDescriptorImpl propertySetterDescriptorImpl = new PropertySetterDescriptorImpl(propertyDescriptor, gVar, propertyDescriptor.o(), uVar, z10, z11, z12, CallableMemberDescriptor.a.DECLARATION, null, sourceElement);
        propertySetterDescriptorImpl.a1(PropertySetterDescriptorImpl.Y0(propertySetterDescriptorImpl, propertyDescriptor.getType(), gVar2));
        return propertySetterDescriptorImpl;
    }

    public static PropertySetterDescriptorImpl n(PropertyDescriptor propertyDescriptor, qb.g gVar, qb.g gVar2, boolean z10, boolean z11, boolean z12, SourceElement sourceElement) {
        if (propertyDescriptor == null) {
            a(3);
        }
        if (gVar == null) {
            a(4);
        }
        if (gVar2 == null) {
            a(5);
        }
        if (sourceElement == null) {
            a(6);
        }
        return m(propertyDescriptor, gVar, gVar2, z10, z11, z12, propertyDescriptor.g(), sourceElement);
    }

    private static boolean o(FunctionDescriptor functionDescriptor) {
        if (functionDescriptor == null) {
            a(29);
        }
        return functionDescriptor.getKind() == CallableMemberDescriptor.a.SYNTHESIZED && e.A(functionDescriptor.b());
    }

    public static boolean p(FunctionDescriptor functionDescriptor) {
        if (functionDescriptor == null) {
            a(28);
        }
        return functionDescriptor.getName().equals(StandardNames.f15268f) && o(functionDescriptor);
    }

    public static boolean q(FunctionDescriptor functionDescriptor) {
        if (functionDescriptor == null) {
            a(27);
        }
        return functionDescriptor.getName().equals(StandardNames.f15266d) && o(functionDescriptor);
    }
}
