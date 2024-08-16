package sb;

import ad.ReceiverValue;
import pb.ClassDescriptor;
import pb.DeclarationDescriptor;

/* compiled from: LazyClassReceiverParameterDescriptor.java */
/* renamed from: sb.q, reason: use source file name */
/* loaded from: classes2.dex */
public class LazyClassReceiverParameterDescriptor extends AbstractReceiverParameterDescriptor {

    /* renamed from: g, reason: collision with root package name */
    private final ClassDescriptor f18371g;

    /* renamed from: h, reason: collision with root package name */
    private final ad.e f18372h;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LazyClassReceiverParameterDescriptor(ClassDescriptor classDescriptor) {
        super(qb.g.f17195b.b());
        if (classDescriptor == null) {
            P(0);
        }
        this.f18371g = classDescriptor;
        this.f18372h = new ad.e(classDescriptor, null);
    }

    private static /* synthetic */ void P(int i10) {
        String str = (i10 == 1 || i10 == 2) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 1 || i10 == 2) ? 2 : 3];
        if (i10 == 1 || i10 == 2) {
            objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/LazyClassReceiverParameterDescriptor";
        } else if (i10 != 3) {
            objArr[0] = "descriptor";
        } else {
            objArr[0] = "newOwner";
        }
        if (i10 == 1) {
            objArr[1] = "getValue";
        } else if (i10 != 2) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/LazyClassReceiverParameterDescriptor";
        } else {
            objArr[1] = "getContainingDeclaration";
        }
        if (i10 != 1 && i10 != 2) {
            if (i10 != 3) {
                objArr[2] = "<init>";
            } else {
                objArr[2] = "copy";
            }
        }
        String format = String.format(str, objArr);
        if (i10 != 1 && i10 != 2) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    @Override // pb.DeclarationDescriptor
    public DeclarationDescriptor b() {
        ClassDescriptor classDescriptor = this.f18371g;
        if (classDescriptor == null) {
            P(2);
        }
        return classDescriptor;
    }

    @Override // pb.ReceiverParameterDescriptor
    public ReceiverValue getValue() {
        ad.e eVar = this.f18372h;
        if (eVar == null) {
            P(1);
        }
        return eVar;
    }

    @Override // sb.DeclarationDescriptorImpl
    public String toString() {
        return "class " + this.f18371g.getName() + "::this";
    }
}
