package ac;

import fd.j;
import gd.g0;
import java.util.List;
import kotlin.collections.r;
import ma.o;
import mb.KotlinBuiltIns;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptor;
import pb.Modality;
import pb.PropertyDescriptor;
import pb.PropertySetterDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.u;
import sb.PropertyDescriptorImpl;
import sb.PropertyGetterDescriptorImpl;
import sb.PropertySetterDescriptorImpl;
import sc.DescriptorFactory;

/* compiled from: JavaPropertyDescriptor.java */
/* renamed from: ac.f, reason: use source file name */
/* loaded from: classes2.dex */
public class JavaPropertyDescriptor extends PropertyDescriptorImpl implements JavaCallableMemberDescriptor {
    private final boolean G;
    private final o<CallableDescriptor.a<?>, ?> H;
    private g0 I;

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public JavaPropertyDescriptor(DeclarationDescriptor declarationDescriptor, qb.g gVar, Modality modality, u uVar, boolean z10, Name name, SourceElement sourceElement, PropertyDescriptor propertyDescriptor, CallableMemberDescriptor.a aVar, boolean z11, o<CallableDescriptor.a<?>, ?> oVar) {
        super(declarationDescriptor, propertyDescriptor, gVar, modality, uVar, z10, name, aVar, sourceElement, false, false, false, false, false, false);
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
        if (sourceElement == null) {
            P(5);
        }
        if (aVar == null) {
            P(6);
        }
        this.I = null;
        this.G = z11;
        this.H = oVar;
    }

    private static /* synthetic */ void P(int i10) {
        String str = i10 != 21 ? "Argument for @NotNull parameter '%s' of %s.%s must not be null" : "@NotNull method %s.%s must not return null";
        Object[] objArr = new Object[i10 != 21 ? 3 : 2];
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
                objArr[0] = "visibility";
                break;
            case 4:
            case 11:
                objArr[0] = "name";
                break;
            case 5:
            case 12:
            case 18:
                objArr[0] = "source";
                break;
            case 6:
            case 16:
                objArr[0] = "kind";
                break;
            case 7:
            default:
                objArr[0] = "containingDeclaration";
                break;
            case 13:
                objArr[0] = "newOwner";
                break;
            case 14:
                objArr[0] = "newModality";
                break;
            case 15:
                objArr[0] = "newVisibility";
                break;
            case 17:
                objArr[0] = "newName";
                break;
            case 19:
                objArr[0] = "enhancedValueParameterTypes";
                break;
            case 20:
                objArr[0] = "enhancedReturnType";
                break;
            case 21:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/load/java/descriptors/JavaPropertyDescriptor";
                break;
            case 22:
                objArr[0] = "inType";
                break;
        }
        if (i10 != 21) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/load/java/descriptors/JavaPropertyDescriptor";
        } else {
            objArr[1] = "enhance";
        }
        switch (i10) {
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                objArr[2] = "create";
                break;
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                objArr[2] = "createSubstitutedCopy";
                break;
            case 19:
            case 20:
                objArr[2] = "enhance";
                break;
            case 21:
                break;
            case 22:
                objArr[2] = "setInType";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        if (i10 == 21) {
            throw new IllegalStateException(format);
        }
    }

    public static JavaPropertyDescriptor o1(DeclarationDescriptor declarationDescriptor, qb.g gVar, Modality modality, u uVar, boolean z10, Name name, SourceElement sourceElement, boolean z11) {
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
        if (sourceElement == null) {
            P(12);
        }
        return new JavaPropertyDescriptor(declarationDescriptor, gVar, modality, uVar, z10, name, sourceElement, null, CallableMemberDescriptor.a.DECLARATION, z11, null);
    }

    @Override // sb.PropertyDescriptorImpl, pb.CallableDescriptor
    public <V> V E(CallableDescriptor.a<V> aVar) {
        o<CallableDescriptor.a<?>, ?> oVar = this.H;
        if (oVar == null || !oVar.c().equals(aVar)) {
            return null;
        }
        return (V) this.H.d();
    }

    @Override // ac.JavaCallableMemberDescriptor
    public JavaCallableMemberDescriptor G(g0 g0Var, List<g0> list, g0 g0Var2, o<CallableDescriptor.a<?>, ?> oVar) {
        PropertyGetterDescriptorImpl propertyGetterDescriptorImpl;
        PropertySetterDescriptorImpl propertySetterDescriptorImpl;
        List<ReceiverParameterDescriptor> j10;
        if (list == null) {
            P(19);
        }
        if (g0Var2 == null) {
            P(20);
        }
        PropertyDescriptor a10 = a() == this ? null : a();
        JavaPropertyDescriptor javaPropertyDescriptor = new JavaPropertyDescriptor(b(), i(), o(), g(), p0(), getName(), z(), a10, getKind(), this.G, oVar);
        PropertyGetterDescriptorImpl h10 = h();
        if (h10 != null) {
            propertyGetterDescriptorImpl = r15;
            PropertyGetterDescriptorImpl propertyGetterDescriptorImpl2 = new PropertyGetterDescriptorImpl(javaPropertyDescriptor, h10.i(), h10.o(), h10.g(), h10.d0(), h10.D(), h10.y(), getKind(), a10 == null ? null : a10.h(), h10.z());
            propertyGetterDescriptorImpl.W0(h10.l0());
            propertyGetterDescriptorImpl.Z0(g0Var2);
        } else {
            propertyGetterDescriptorImpl = null;
        }
        PropertySetterDescriptor k10 = k();
        if (k10 != null) {
            PropertySetterDescriptorImpl propertySetterDescriptorImpl2 = new PropertySetterDescriptorImpl(javaPropertyDescriptor, k10.i(), k10.o(), k10.g(), k10.d0(), k10.D(), k10.y(), getKind(), a10 == null ? null : a10.k(), k10.z());
            propertySetterDescriptorImpl2.W0(propertySetterDescriptorImpl2.l0());
            propertySetterDescriptorImpl2.a1(k10.l().get(0));
            propertySetterDescriptorImpl = propertySetterDescriptorImpl2;
        } else {
            propertySetterDescriptorImpl = null;
        }
        javaPropertyDescriptor.e1(propertyGetterDescriptorImpl, propertySetterDescriptorImpl, v0(), s0());
        javaPropertyDescriptor.j1(f1());
        ya.a<j<uc.g<?>>> aVar = this.f18324l;
        if (aVar != null) {
            javaPropertyDescriptor.T0(this.f18323k, aVar);
        }
        javaPropertyDescriptor.D0(e());
        ReceiverParameterDescriptor i10 = g0Var == null ? null : DescriptorFactory.i(this, g0Var, qb.g.f17195b.b());
        List<TypeParameterDescriptor> m10 = m();
        ReceiverParameterDescriptor m02 = m0();
        j10 = r.j();
        javaPropertyDescriptor.k1(g0Var2, m10, m02, i10, j10);
        return javaPropertyDescriptor;
    }

    @Override // sb.PropertyDescriptorImpl, pb.VariableDescriptor
    public boolean I() {
        g0 type = getType();
        return this.G && pb.j.a(type) && (!gc.r.i(type) || KotlinBuiltIns.u0(type));
    }

    @Override // sb.VariableDescriptorImpl, pb.CallableDescriptor
    public boolean O() {
        return false;
    }

    @Override // sb.PropertyDescriptorImpl
    protected PropertyDescriptorImpl Y0(DeclarationDescriptor declarationDescriptor, Modality modality, u uVar, PropertyDescriptor propertyDescriptor, CallableMemberDescriptor.a aVar, Name name, SourceElement sourceElement) {
        if (declarationDescriptor == null) {
            P(13);
        }
        if (modality == null) {
            P(14);
        }
        if (uVar == null) {
            P(15);
        }
        if (aVar == null) {
            P(16);
        }
        if (name == null) {
            P(17);
        }
        if (sourceElement == null) {
            P(18);
        }
        return new JavaPropertyDescriptor(declarationDescriptor, i(), modality, uVar, p0(), name, sourceElement, propertyDescriptor, aVar, this.G, this.H);
    }

    @Override // sb.PropertyDescriptorImpl
    public void i1(g0 g0Var) {
        if (g0Var == null) {
            P(22);
        }
        this.I = g0Var;
    }
}
