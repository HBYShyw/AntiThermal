package sb;

import fd.StorageManager;
import gd.Variance;
import java.util.ArrayList;
import java.util.List;
import oc.Name;
import pb.DeclarationDescriptor;
import pb.SourceElement;
import pb.SupertypeLoopChecker;
import pb.TypeParameterDescriptor;

/* compiled from: TypeParameterDescriptorImpl.java */
/* renamed from: sb.k0, reason: use source file name */
/* loaded from: classes2.dex */
public class TypeParameterDescriptorImpl extends AbstractTypeParameterDescriptor {

    /* renamed from: o, reason: collision with root package name */
    private final ya.l<gd.g0, Void> f18293o;

    /* renamed from: p, reason: collision with root package name */
    private final List<gd.g0> f18294p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f18295q;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    private TypeParameterDescriptorImpl(DeclarationDescriptor declarationDescriptor, qb.g gVar, boolean z10, Variance variance, Name name, int i10, SourceElement sourceElement, ya.l<gd.g0, Void> lVar, SupertypeLoopChecker supertypeLoopChecker, StorageManager storageManager) {
        super(storageManager, declarationDescriptor, gVar, name, variance, z10, i10, sourceElement, supertypeLoopChecker);
        if (declarationDescriptor == null) {
            P(19);
        }
        if (gVar == null) {
            P(20);
        }
        if (variance == null) {
            P(21);
        }
        if (name == null) {
            P(22);
        }
        if (sourceElement == null) {
            P(23);
        }
        if (supertypeLoopChecker == null) {
            P(24);
        }
        if (storageManager == null) {
            P(25);
        }
        this.f18294p = new ArrayList(1);
        this.f18295q = false;
        this.f18293o = lVar;
    }

    private static /* synthetic */ void P(int i10) {
        String str = (i10 == 5 || i10 == 28) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 5 || i10 == 28) ? 2 : 3];
        switch (i10) {
            case 1:
            case 7:
            case 13:
            case 20:
                objArr[0] = "annotations";
                break;
            case 2:
            case 8:
            case 14:
            case 21:
                objArr[0] = "variance";
                break;
            case 3:
            case 9:
            case 15:
            case 22:
                objArr[0] = "name";
                break;
            case 4:
            case 11:
            case 18:
            case 25:
                objArr[0] = "storageManager";
                break;
            case 5:
            case 28:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/TypeParameterDescriptorImpl";
                break;
            case 6:
            case 12:
            case 19:
            default:
                objArr[0] = "containingDeclaration";
                break;
            case 10:
            case 16:
            case 23:
                objArr[0] = "source";
                break;
            case 17:
                objArr[0] = "supertypeLoopsResolver";
                break;
            case 24:
                objArr[0] = "supertypeLoopsChecker";
                break;
            case 26:
                objArr[0] = "bound";
                break;
            case 27:
                objArr[0] = "type";
                break;
        }
        if (i10 == 5) {
            objArr[1] = "createWithDefaultBound";
        } else if (i10 != 28) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/TypeParameterDescriptorImpl";
        } else {
            objArr[1] = "resolveUpperBounds";
        }
        switch (i10) {
            case 5:
            case 28:
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                objArr[2] = "createForFurtherModification";
                break;
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
                objArr[2] = "<init>";
                break;
            case 26:
                objArr[2] = "addUpperBound";
                break;
            case 27:
                objArr[2] = "reportSupertypeLoopError";
                break;
            default:
                objArr[2] = "createWithDefaultBound";
                break;
        }
        String format = String.format(str, objArr);
        if (i10 != 5 && i10 != 28) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    private void W0() {
        if (this.f18295q) {
            return;
        }
        throw new IllegalStateException("Type parameter descriptor is not initialized: " + d1());
    }

    private void X0() {
        if (this.f18295q) {
            throw new IllegalStateException("Type parameter descriptor is already initialized: " + d1());
        }
    }

    public static TypeParameterDescriptorImpl Y0(DeclarationDescriptor declarationDescriptor, qb.g gVar, boolean z10, Variance variance, Name name, int i10, SourceElement sourceElement, StorageManager storageManager) {
        if (declarationDescriptor == null) {
            P(6);
        }
        if (gVar == null) {
            P(7);
        }
        if (variance == null) {
            P(8);
        }
        if (name == null) {
            P(9);
        }
        if (sourceElement == null) {
            P(10);
        }
        if (storageManager == null) {
            P(11);
        }
        return Z0(declarationDescriptor, gVar, z10, variance, name, i10, sourceElement, null, SupertypeLoopChecker.a.f16675a, storageManager);
    }

    public static TypeParameterDescriptorImpl Z0(DeclarationDescriptor declarationDescriptor, qb.g gVar, boolean z10, Variance variance, Name name, int i10, SourceElement sourceElement, ya.l<gd.g0, Void> lVar, SupertypeLoopChecker supertypeLoopChecker, StorageManager storageManager) {
        if (declarationDescriptor == null) {
            P(12);
        }
        if (gVar == null) {
            P(13);
        }
        if (variance == null) {
            P(14);
        }
        if (name == null) {
            P(15);
        }
        if (sourceElement == null) {
            P(16);
        }
        if (supertypeLoopChecker == null) {
            P(17);
        }
        if (storageManager == null) {
            P(18);
        }
        return new TypeParameterDescriptorImpl(declarationDescriptor, gVar, z10, variance, name, i10, sourceElement, lVar, supertypeLoopChecker, storageManager);
    }

    public static TypeParameterDescriptor a1(DeclarationDescriptor declarationDescriptor, qb.g gVar, boolean z10, Variance variance, Name name, int i10, StorageManager storageManager) {
        if (declarationDescriptor == null) {
            P(0);
        }
        if (gVar == null) {
            P(1);
        }
        if (variance == null) {
            P(2);
        }
        if (name == null) {
            P(3);
        }
        if (storageManager == null) {
            P(4);
        }
        TypeParameterDescriptorImpl Y0 = Y0(declarationDescriptor, gVar, z10, variance, name, i10, SourceElement.f16664a, storageManager);
        Y0.V0(wc.c.j(declarationDescriptor).y());
        Y0.e1();
        return Y0;
    }

    private void b1(gd.g0 g0Var) {
        if (gd.i0.a(g0Var)) {
            return;
        }
        this.f18294p.add(g0Var);
    }

    private String d1() {
        return getName() + " declared in " + sc.e.m(b());
    }

    @Override // sb.AbstractTypeParameterDescriptor
    protected void T0(gd.g0 g0Var) {
        if (g0Var == null) {
            P(27);
        }
        ya.l<gd.g0, Void> lVar = this.f18293o;
        if (lVar == null) {
            return;
        }
        lVar.invoke(g0Var);
    }

    @Override // sb.AbstractTypeParameterDescriptor
    protected List<gd.g0> U0() {
        W0();
        List<gd.g0> list = this.f18294p;
        if (list == null) {
            P(28);
        }
        return list;
    }

    public void V0(gd.g0 g0Var) {
        if (g0Var == null) {
            P(26);
        }
        X0();
        b1(g0Var);
    }

    public boolean c1() {
        return this.f18295q;
    }

    public void e1() {
        X0();
        this.f18295q = true;
    }
}
