package sb;

import oc.Name;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorNonRoot;
import pb.DeclarationDescriptorWithSource;
import pb.SourceElement;

/* compiled from: DeclarationDescriptorNonRootImpl.java */
/* renamed from: sb.k, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class DeclarationDescriptorNonRootImpl extends DeclarationDescriptorImpl implements DeclarationDescriptorNonRoot {

    /* renamed from: g, reason: collision with root package name */
    private final DeclarationDescriptor f18291g;

    /* renamed from: h, reason: collision with root package name */
    private final SourceElement f18292h;

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DeclarationDescriptorNonRootImpl(DeclarationDescriptor declarationDescriptor, qb.g gVar, Name name, SourceElement sourceElement) {
        super(gVar, name);
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
        this.f18291g = declarationDescriptor;
        this.f18292h = sourceElement;
    }

    private static /* synthetic */ void P(int i10) {
        String str = (i10 == 4 || i10 == 5 || i10 == 6) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 4 || i10 == 5 || i10 == 6) ? 2 : 3];
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
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/DeclarationDescriptorNonRootImpl";
                break;
            default:
                objArr[0] = "containingDeclaration";
                break;
        }
        if (i10 == 4) {
            objArr[1] = "getOriginal";
        } else if (i10 == 5) {
            objArr[1] = "getContainingDeclaration";
        } else if (i10 != 6) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/DeclarationDescriptorNonRootImpl";
        } else {
            objArr[1] = "getSource";
        }
        if (i10 != 4 && i10 != 5 && i10 != 6) {
            objArr[2] = "<init>";
        }
        String format = String.format(str, objArr);
        if (i10 != 4 && i10 != 5 && i10 != 6) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    @Override // sb.DeclarationDescriptorImpl, pb.DeclarationDescriptor
    /* renamed from: J0, reason: merged with bridge method [inline-methods] */
    public DeclarationDescriptorWithSource a() {
        DeclarationDescriptorWithSource declarationDescriptorWithSource = (DeclarationDescriptorWithSource) super.a();
        if (declarationDescriptorWithSource == null) {
            P(4);
        }
        return declarationDescriptorWithSource;
    }

    public DeclarationDescriptor b() {
        DeclarationDescriptor declarationDescriptor = this.f18291g;
        if (declarationDescriptor == null) {
            P(5);
        }
        return declarationDescriptor;
    }

    public SourceElement z() {
        SourceElement sourceElement = this.f18292h;
        if (sourceElement == null) {
            P(6);
        }
        return sourceElement;
    }
}
