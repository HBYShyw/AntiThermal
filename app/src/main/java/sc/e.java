package sc;

import gd.TypeConstructor;
import gd.g0;
import gd.i0;
import gd.s1;
import hd.KotlinTypeChecker;
import id.ErrorUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import mb.KotlinBuiltIns;
import oc.FqName;
import oc.FqNameUnsafe;
import oc.SpecialNames;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorWithSource;
import pb.DeclarationDescriptorWithVisibility;
import pb.DescriptorVisibilities;
import pb.Modality;
import pb.ModuleDescriptor;
import pb.PackageFragmentDescriptor;
import pb.PackageViewDescriptor;
import pb.PropertySetterDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.VariableDescriptor;
import pb.b1;
import pb.u;

/* compiled from: DescriptorUtils.java */
/* loaded from: classes2.dex */
public class e {

    /* renamed from: a, reason: collision with root package name */
    public static final FqName f18426a = new FqName("kotlin.jvm.JvmName");

    public static boolean A(DeclarationDescriptor declarationDescriptor) {
        return D(declarationDescriptor, ClassKind.ENUM_CLASS);
    }

    public static boolean B(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(36);
        }
        return D(declarationDescriptor, ClassKind.ENUM_ENTRY);
    }

    public static boolean C(DeclarationDescriptor declarationDescriptor) {
        return D(declarationDescriptor, ClassKind.INTERFACE);
    }

    private static boolean D(DeclarationDescriptor declarationDescriptor, ClassKind classKind) {
        if (classKind == null) {
            a(37);
        }
        return (declarationDescriptor instanceof ClassDescriptor) && ((ClassDescriptor) declarationDescriptor).getKind() == classKind;
    }

    public static boolean E(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(1);
        }
        while (declarationDescriptor != null) {
            if (u(declarationDescriptor) || y(declarationDescriptor)) {
                return true;
            }
            declarationDescriptor = declarationDescriptor.b();
        }
        return false;
    }

    private static boolean F(g0 g0Var, DeclarationDescriptor declarationDescriptor) {
        if (g0Var == null) {
            a(30);
        }
        if (declarationDescriptor == null) {
            a(31);
        }
        ClassifierDescriptor v7 = g0Var.W0().v();
        if (v7 == null) {
            return false;
        }
        DeclarationDescriptor a10 = v7.a();
        return (a10 instanceof ClassifierDescriptor) && (declarationDescriptor instanceof ClassifierDescriptor) && ((ClassifierDescriptor) declarationDescriptor).n().equals(((ClassifierDescriptor) a10).n());
    }

    public static boolean G(DeclarationDescriptor declarationDescriptor) {
        return (D(declarationDescriptor, ClassKind.CLASS) || D(declarationDescriptor, ClassKind.INTERFACE)) && ((ClassDescriptor) declarationDescriptor).o() == Modality.SEALED;
    }

    public static boolean H(ClassDescriptor classDescriptor, ClassDescriptor classDescriptor2) {
        if (classDescriptor == null) {
            a(28);
        }
        if (classDescriptor2 == null) {
            a(29);
        }
        return I(classDescriptor.x(), classDescriptor2.a());
    }

    public static boolean I(g0 g0Var, DeclarationDescriptor declarationDescriptor) {
        if (g0Var == null) {
            a(32);
        }
        if (declarationDescriptor == null) {
            a(33);
        }
        if (F(g0Var, declarationDescriptor)) {
            return true;
        }
        Iterator<g0> it = g0Var.W0().q().iterator();
        while (it.hasNext()) {
            if (I(it.next(), declarationDescriptor)) {
                return true;
            }
        }
        return false;
    }

    public static boolean J(DeclarationDescriptor declarationDescriptor) {
        return declarationDescriptor != null && (declarationDescriptor.b() instanceof PackageFragmentDescriptor);
    }

    public static boolean K(VariableDescriptor variableDescriptor, g0 g0Var) {
        if (variableDescriptor == null) {
            a(63);
        }
        if (g0Var == null) {
            a(64);
        }
        if (variableDescriptor.p0() || i0.a(g0Var)) {
            return false;
        }
        if (s1.b(g0Var)) {
            return true;
        }
        KotlinBuiltIns j10 = wc.c.j(variableDescriptor);
        if (!KotlinBuiltIns.r0(g0Var)) {
            KotlinTypeChecker kotlinTypeChecker = KotlinTypeChecker.f12213a;
            if (!kotlinTypeChecker.d(j10.W(), g0Var) && !kotlinTypeChecker.d(j10.K().x(), g0Var) && !kotlinTypeChecker.d(j10.i(), g0Var)) {
                mb.o oVar = mb.o.f15357a;
                if (!mb.o.d(g0Var)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <D extends CallableMemberDescriptor> D L(D d10) {
        if (d10 == null) {
            a(59);
        }
        while (d10.getKind() == CallableMemberDescriptor.a.FAKE_OVERRIDE) {
            Collection<? extends CallableMemberDescriptor> e10 = d10.e();
            if (!e10.isEmpty()) {
                d10 = (D) e10.iterator().next();
            } else {
                throw new IllegalStateException("Fake override should have at least one overridden descriptor: " + d10);
            }
        }
        return d10;
    }

    public static <D extends DeclarationDescriptorWithVisibility> D M(D d10) {
        if (d10 == null) {
            a(61);
        }
        if (d10 instanceof CallableMemberDescriptor) {
            return L((CallableMemberDescriptor) d10);
        }
        if (d10 == null) {
            a(62);
        }
        return d10;
    }

    private static /* synthetic */ void a(int i10) {
        String str;
        int i11;
        switch (i10) {
            case 4:
            case 7:
            case 9:
            case 10:
            case 12:
            case 22:
            case 40:
            case 42:
            case 43:
            case 47:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 60:
            case 62:
            case 69:
            case 73:
            case 80:
            case 81:
            case 83:
            case 86:
            case 91:
            case 93:
                str = "@NotNull method %s.%s must not return null";
                break;
            default:
                str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
        }
        switch (i10) {
            case 4:
            case 7:
            case 9:
            case 10:
            case 12:
            case 22:
            case 40:
            case 42:
            case 43:
            case 47:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 60:
            case 62:
            case 69:
            case 73:
            case 80:
            case 81:
            case 83:
            case 86:
            case 91:
            case 93:
                i11 = 2;
                break;
            default:
                i11 = 3;
                break;
        }
        Object[] objArr = new Object[i11];
        switch (i10) {
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
            case 8:
            case 11:
            case 13:
            case 14:
            case 15:
            case 21:
            case 23:
            case 24:
            case 34:
            case 35:
            case 36:
            case 57:
            case 58:
            case 59:
            case 61:
            case 79:
            case 92:
            case 94:
                objArr[0] = "descriptor";
                break;
            case 4:
            case 7:
            case 9:
            case 10:
            case 12:
            case 22:
            case 40:
            case 42:
            case 43:
            case 47:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 60:
            case 62:
            case 69:
            case 73:
            case 80:
            case 81:
            case 83:
            case 86:
            case 91:
            case 93:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/resolve/DescriptorUtils";
                break;
            case 16:
                objArr[0] = "first";
                break;
            case 17:
                objArr[0] = "second";
                break;
            case 18:
            case 19:
                objArr[0] = "aClass";
                break;
            case 20:
                objArr[0] = "kotlinType";
                break;
            case 25:
                objArr[0] = "declarationDescriptor";
                break;
            case 26:
            case 28:
                objArr[0] = "subClass";
                break;
            case 27:
            case 29:
            case 33:
                objArr[0] = "superClass";
                break;
            case 30:
            case 32:
            case 45:
            case 64:
                objArr[0] = "type";
                break;
            case 31:
                objArr[0] = "other";
                break;
            case 37:
                objArr[0] = "classKind";
                break;
            case 38:
            case 39:
            case 41:
            case 44:
            case 48:
            case 54:
            case 65:
            case 66:
            case 67:
            case 74:
            case 75:
                objArr[0] = "classDescriptor";
                break;
            case 46:
                objArr[0] = "typeConstructor";
                break;
            case 55:
                objArr[0] = "innerClassName";
                break;
            case 56:
                objArr[0] = "location";
                break;
            case 63:
                objArr[0] = "variable";
                break;
            case 68:
                objArr[0] = "f";
                break;
            case 70:
                objArr[0] = "current";
                break;
            case 71:
                objArr[0] = "result";
                break;
            case 72:
                objArr[0] = "memberDescriptor";
                break;
            case 76:
            case 77:
            case 78:
                objArr[0] = "annotated";
                break;
            case 82:
            case 84:
            case 87:
            case 89:
                objArr[0] = "scope";
                break;
            case 85:
            case 88:
            case 90:
                objArr[0] = "name";
                break;
            default:
                objArr[0] = "containingDeclaration";
                break;
        }
        switch (i10) {
            case 4:
                objArr[1] = "getFqNameSafe";
                break;
            case 7:
                objArr[1] = "getFqNameUnsafe";
                break;
            case 9:
            case 10:
                objArr[1] = "getFqNameFromTopLevelClass";
                break;
            case 12:
                objArr[1] = "getClassIdForNonLocalClass";
                break;
            case 22:
                objArr[1] = "getContainingModule";
                break;
            case 40:
                objArr[1] = "getSuperclassDescriptors";
                break;
            case 42:
            case 43:
                objArr[1] = "getSuperClassType";
                break;
            case 47:
                objArr[1] = "getClassDescriptorForTypeConstructor";
                break;
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
                objArr[1] = "getDefaultConstructorVisibility";
                break;
            case 60:
                objArr[1] = "unwrapFakeOverride";
                break;
            case 62:
                objArr[1] = "unwrapFakeOverrideToAnyDeclaration";
                break;
            case 69:
                objArr[1] = "getAllOverriddenDescriptors";
                break;
            case 73:
                objArr[1] = "getAllOverriddenDeclarations";
                break;
            case 80:
            case 81:
                objArr[1] = "getContainingSourceFile";
                break;
            case 83:
                objArr[1] = "getAllDescriptors";
                break;
            case 86:
                objArr[1] = "getFunctionByName";
                break;
            case 91:
                objArr[1] = "getPropertyByName";
                break;
            case 93:
                objArr[1] = "getDirectMember";
                break;
            default:
                objArr[1] = "kotlin/reflect/jvm/internal/impl/resolve/DescriptorUtils";
                break;
        }
        switch (i10) {
            case 1:
                objArr[2] = "isLocal";
                break;
            case 2:
                objArr[2] = "getFqName";
                break;
            case 3:
                objArr[2] = "getFqNameSafe";
                break;
            case 4:
            case 7:
            case 9:
            case 10:
            case 12:
            case 22:
            case 40:
            case 42:
            case 43:
            case 47:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 60:
            case 62:
            case 69:
            case 73:
            case 80:
            case 81:
            case 83:
            case 86:
            case 91:
            case 93:
                break;
            case 5:
                objArr[2] = "getFqNameSafeIfPossible";
                break;
            case 6:
                objArr[2] = "getFqNameUnsafe";
                break;
            case 8:
                objArr[2] = "getFqNameFromTopLevelClass";
                break;
            case 11:
                objArr[2] = "getClassIdForNonLocalClass";
                break;
            case 13:
                objArr[2] = "isExtension";
                break;
            case 14:
                objArr[2] = "isOverride";
                break;
            case 15:
                objArr[2] = "isStaticDeclaration";
                break;
            case 16:
            case 17:
                objArr[2] = "areInSameModule";
                break;
            case 18:
            case 19:
                objArr[2] = "getParentOfType";
                break;
            case 20:
            case 23:
                objArr[2] = "getContainingModuleOrNull";
                break;
            case 21:
                objArr[2] = "getContainingModule";
                break;
            case 24:
                objArr[2] = "getContainingClass";
                break;
            case 25:
                objArr[2] = "isAncestor";
                break;
            case 26:
            case 27:
                objArr[2] = "isDirectSubclass";
                break;
            case 28:
            case 29:
                objArr[2] = "isSubclass";
                break;
            case 30:
            case 31:
                objArr[2] = "isSameClass";
                break;
            case 32:
            case 33:
                objArr[2] = "isSubtypeOfClass";
                break;
            case 34:
                objArr[2] = "isAnonymousObject";
                break;
            case 35:
                objArr[2] = "isAnonymousFunction";
                break;
            case 36:
                objArr[2] = "isEnumEntry";
                break;
            case 37:
                objArr[2] = "isKindOf";
                break;
            case 38:
                objArr[2] = "hasAbstractMembers";
                break;
            case 39:
                objArr[2] = "getSuperclassDescriptors";
                break;
            case 41:
                objArr[2] = "getSuperClassType";
                break;
            case 44:
                objArr[2] = "getSuperClassDescriptor";
                break;
            case 45:
                objArr[2] = "getClassDescriptorForType";
                break;
            case 46:
                objArr[2] = "getClassDescriptorForTypeConstructor";
                break;
            case 48:
                objArr[2] = "getDefaultConstructorVisibility";
                break;
            case 54:
            case 55:
            case 56:
                objArr[2] = "getInnerClassByName";
                break;
            case 57:
                objArr[2] = "isStaticNestedClass";
                break;
            case 58:
                objArr[2] = "isTopLevelOrInnerClass";
                break;
            case 59:
                objArr[2] = "unwrapFakeOverride";
                break;
            case 61:
                objArr[2] = "unwrapFakeOverrideToAnyDeclaration";
                break;
            case 63:
            case 64:
                objArr[2] = "shouldRecordInitializerForProperty";
                break;
            case 65:
                objArr[2] = "classCanHaveAbstractFakeOverride";
                break;
            case 66:
                objArr[2] = "classCanHaveAbstractDeclaration";
                break;
            case 67:
                objArr[2] = "classCanHaveOpenMembers";
                break;
            case 68:
                objArr[2] = "getAllOverriddenDescriptors";
                break;
            case 70:
            case 71:
                objArr[2] = "collectAllOverriddenDescriptors";
                break;
            case 72:
                objArr[2] = "getAllOverriddenDeclarations";
                break;
            case 74:
                objArr[2] = "isSingletonOrAnonymousObject";
                break;
            case 75:
                objArr[2] = "canHaveDeclaredConstructors";
                break;
            case 76:
                objArr[2] = "getJvmName";
                break;
            case 77:
                objArr[2] = "findJvmNameAnnotation";
                break;
            case 78:
                objArr[2] = "hasJvmNameAnnotation";
                break;
            case 79:
                objArr[2] = "getContainingSourceFile";
                break;
            case 82:
                objArr[2] = "getAllDescriptors";
                break;
            case 84:
            case 85:
                objArr[2] = "getFunctionByName";
                break;
            case 87:
            case 88:
                objArr[2] = "getFunctionByNameOrNull";
                break;
            case 89:
            case 90:
                objArr[2] = "getPropertyByName";
                break;
            case 92:
                objArr[2] = "getDirectMember";
                break;
            case 94:
                objArr[2] = "isMethodOfAny";
                break;
            default:
                objArr[2] = "getDispatchReceiverParameterIfNeeded";
                break;
        }
        String format = String.format(str, objArr);
        switch (i10) {
            case 4:
            case 7:
            case 9:
            case 10:
            case 12:
            case 22:
            case 40:
            case 42:
            case 43:
            case 47:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 60:
            case 62:
            case 69:
            case 73:
            case 80:
            case 81:
            case 83:
            case 86:
            case 91:
            case 93:
                throw new IllegalStateException(format);
            default:
                throw new IllegalArgumentException(format);
        }
    }

    public static boolean b(DeclarationDescriptor declarationDescriptor, DeclarationDescriptor declarationDescriptor2) {
        if (declarationDescriptor == null) {
            a(16);
        }
        if (declarationDescriptor2 == null) {
            a(17);
        }
        return g(declarationDescriptor).equals(g(declarationDescriptor2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <D extends CallableDescriptor> void c(D d10, Set<D> set) {
        if (d10 == null) {
            a(70);
        }
        if (set == 0) {
            a(71);
        }
        if (set.contains(d10)) {
            return;
        }
        Iterator<? extends CallableDescriptor> it = d10.a().e().iterator();
        while (it.hasNext()) {
            CallableDescriptor a10 = it.next().a();
            c(a10, set);
            set.add(a10);
        }
    }

    public static <D extends CallableDescriptor> Set<D> d(D d10) {
        if (d10 == null) {
            a(68);
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        c(d10.a(), linkedHashSet);
        return linkedHashSet;
    }

    public static ClassDescriptor e(g0 g0Var) {
        if (g0Var == null) {
            a(45);
        }
        return f(g0Var.W0());
    }

    public static ClassDescriptor f(TypeConstructor typeConstructor) {
        if (typeConstructor == null) {
            a(46);
        }
        ClassDescriptor classDescriptor = (ClassDescriptor) typeConstructor.v();
        if (classDescriptor == null) {
            a(47);
        }
        return classDescriptor;
    }

    public static ModuleDescriptor g(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(21);
        }
        ModuleDescriptor i10 = i(declarationDescriptor);
        if (i10 == null) {
            a(22);
        }
        return i10;
    }

    public static ModuleDescriptor h(g0 g0Var) {
        if (g0Var == null) {
            a(20);
        }
        ClassifierDescriptor v7 = g0Var.W0().v();
        if (v7 == null) {
            return null;
        }
        return i(v7);
    }

    public static ModuleDescriptor i(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(23);
        }
        while (declarationDescriptor != null) {
            if (declarationDescriptor instanceof ModuleDescriptor) {
                return (ModuleDescriptor) declarationDescriptor;
            }
            if (declarationDescriptor instanceof PackageViewDescriptor) {
                return ((PackageViewDescriptor) declarationDescriptor).A0();
            }
            declarationDescriptor = declarationDescriptor.b();
        }
        return null;
    }

    public static b1 j(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(79);
        }
        if (declarationDescriptor instanceof PropertySetterDescriptor) {
            declarationDescriptor = ((PropertySetterDescriptor) declarationDescriptor).K0();
        }
        if (declarationDescriptor instanceof DeclarationDescriptorWithSource) {
            b1 a10 = ((DeclarationDescriptorWithSource) declarationDescriptor).z().a();
            if (a10 == null) {
                a(80);
            }
            return a10;
        }
        b1 b1Var = b1.f16671a;
        if (b1Var == null) {
            a(81);
        }
        return b1Var;
    }

    public static u k(ClassDescriptor classDescriptor, boolean z10) {
        if (classDescriptor == null) {
            a(48);
        }
        ClassKind kind = classDescriptor.getKind();
        if (kind != ClassKind.ENUM_CLASS && !kind.b()) {
            if (G(classDescriptor)) {
                if (z10) {
                    u uVar = DescriptorVisibilities.f16731c;
                    if (uVar == null) {
                        a(50);
                    }
                    return uVar;
                }
                u uVar2 = DescriptorVisibilities.f16729a;
                if (uVar2 == null) {
                    a(51);
                }
                return uVar2;
            }
            if (u(classDescriptor)) {
                u uVar3 = DescriptorVisibilities.f16740l;
                if (uVar3 == null) {
                    a(52);
                }
                return uVar3;
            }
            u uVar4 = DescriptorVisibilities.f16733e;
            if (uVar4 == null) {
                a(53);
            }
            return uVar4;
        }
        u uVar5 = DescriptorVisibilities.f16729a;
        if (uVar5 == null) {
            a(49);
        }
        return uVar5;
    }

    public static ReceiverParameterDescriptor l(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(0);
        }
        if (declarationDescriptor instanceof ClassDescriptor) {
            return ((ClassDescriptor) declarationDescriptor).S0();
        }
        return null;
    }

    public static FqNameUnsafe m(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(2);
        }
        FqName o10 = o(declarationDescriptor);
        return o10 != null ? o10.j() : p(declarationDescriptor);
    }

    public static FqName n(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(3);
        }
        FqName o10 = o(declarationDescriptor);
        if (o10 == null) {
            o10 = p(declarationDescriptor).l();
        }
        if (o10 == null) {
            a(4);
        }
        return o10;
    }

    private static FqName o(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(5);
        }
        if (!(declarationDescriptor instanceof ModuleDescriptor) && !ErrorUtils.m(declarationDescriptor)) {
            if (declarationDescriptor instanceof PackageViewDescriptor) {
                return ((PackageViewDescriptor) declarationDescriptor).d();
            }
            if (declarationDescriptor instanceof PackageFragmentDescriptor) {
                return ((PackageFragmentDescriptor) declarationDescriptor).d();
            }
            return null;
        }
        return FqName.f16431c;
    }

    private static FqNameUnsafe p(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(6);
        }
        FqNameUnsafe c10 = m(declarationDescriptor.b()).c(declarationDescriptor.getName());
        if (c10 == null) {
            a(7);
        }
        return c10;
    }

    public static <D extends DeclarationDescriptor> D q(DeclarationDescriptor declarationDescriptor, Class<D> cls) {
        if (cls == null) {
            a(18);
        }
        return (D) r(declarationDescriptor, cls, true);
    }

    public static <D extends DeclarationDescriptor> D r(DeclarationDescriptor declarationDescriptor, Class<D> cls, boolean z10) {
        if (cls == null) {
            a(19);
        }
        if (declarationDescriptor == null) {
            return null;
        }
        if (z10) {
            declarationDescriptor = (D) declarationDescriptor.b();
        }
        while (declarationDescriptor != null) {
            if (cls.isInstance(declarationDescriptor)) {
                return (D) declarationDescriptor;
            }
            declarationDescriptor = (D) declarationDescriptor.b();
        }
        return null;
    }

    public static ClassDescriptor s(ClassDescriptor classDescriptor) {
        if (classDescriptor == null) {
            a(44);
        }
        Iterator<g0> it = classDescriptor.n().q().iterator();
        while (it.hasNext()) {
            ClassDescriptor e10 = e(it.next());
            if (e10.getKind() != ClassKind.INTERFACE) {
                return e10;
            }
        }
        return null;
    }

    public static boolean t(DeclarationDescriptor declarationDescriptor) {
        return D(declarationDescriptor, ClassKind.ANNOTATION_CLASS);
    }

    public static boolean u(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(34);
        }
        return v(declarationDescriptor) && declarationDescriptor.getName().equals(SpecialNames.f16447b);
    }

    public static boolean v(DeclarationDescriptor declarationDescriptor) {
        return D(declarationDescriptor, ClassKind.CLASS);
    }

    public static boolean w(DeclarationDescriptor declarationDescriptor) {
        return v(declarationDescriptor) || A(declarationDescriptor);
    }

    public static boolean x(DeclarationDescriptor declarationDescriptor) {
        return D(declarationDescriptor, ClassKind.OBJECT) && ((ClassDescriptor) declarationDescriptor).F();
    }

    public static boolean y(DeclarationDescriptor declarationDescriptor) {
        return (declarationDescriptor instanceof DeclarationDescriptorWithVisibility) && ((DeclarationDescriptorWithVisibility) declarationDescriptor).g() == DescriptorVisibilities.f16734f;
    }

    public static boolean z(ClassDescriptor classDescriptor, ClassDescriptor classDescriptor2) {
        if (classDescriptor == null) {
            a(26);
        }
        if (classDescriptor2 == null) {
            a(27);
        }
        Iterator<g0> it = classDescriptor.n().q().iterator();
        while (it.hasNext()) {
            if (F(it.next(), classDescriptor2.a())) {
                return true;
            }
        }
        return false;
    }
}
