package sb;

import fd.StorageManager;
import oc.Name;
import pb.DeclarationDescriptor;
import pb.SourceElement;

/* compiled from: ClassDescriptorBase.java */
/* renamed from: sb.g, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class ClassDescriptorBase extends AbstractClassDescriptor {

    /* renamed from: j, reason: collision with root package name */
    private final DeclarationDescriptor f18275j;

    /* renamed from: k, reason: collision with root package name */
    private final SourceElement f18276k;

    /* renamed from: l, reason: collision with root package name */
    private final boolean f18277l;

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ClassDescriptorBase(StorageManager storageManager, DeclarationDescriptor declarationDescriptor, Name name, SourceElement sourceElement, boolean z10) {
        super(storageManager, name);
        if (storageManager == null) {
            J0(0);
        }
        if (declarationDescriptor == null) {
            J0(1);
        }
        if (name == null) {
            J0(2);
        }
        if (sourceElement == null) {
            J0(3);
        }
        this.f18275j = declarationDescriptor;
        this.f18276k = sourceElement;
        this.f18277l = z10;
    }

    private static /* synthetic */ void J0(int i10) {
        String str = (i10 == 4 || i10 == 5) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 4 || i10 == 5) ? 2 : 3];
        if (i10 == 1) {
            objArr[0] = "containingDeclaration";
        } else if (i10 == 2) {
            objArr[0] = "name";
        } else if (i10 == 3) {
            objArr[0] = "source";
        } else if (i10 == 4 || i10 == 5) {
            objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/ClassDescriptorBase";
        } else {
            objArr[0] = "storageManager";
        }
        if (i10 == 4) {
            objArr[1] = "getContainingDeclaration";
        } else if (i10 != 5) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/ClassDescriptorBase";
        } else {
            objArr[1] = "getSource";
        }
        if (i10 != 4 && i10 != 5) {
            objArr[2] = "<init>";
        }
        String format = String.format(str, objArr);
        if (i10 != 4 && i10 != 5) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    public boolean D() {
        return this.f18277l;
    }

    @Override // pb.ClassDescriptor, pb.DeclarationDescriptorNonRoot, pb.DeclarationDescriptor
    public DeclarationDescriptor b() {
        DeclarationDescriptor declarationDescriptor = this.f18275j;
        if (declarationDescriptor == null) {
            J0(4);
        }
        return declarationDescriptor;
    }

    @Override // pb.DeclarationDescriptorWithSource
    public SourceElement z() {
        SourceElement sourceElement = this.f18276k;
        if (sourceElement == null) {
            J0(5);
        }
        return sourceElement;
    }
}
