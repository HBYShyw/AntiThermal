package sb;

import oc.Name;
import pb.DeclarationDescriptor;
import qb.AnnotatedImpl;

/* compiled from: DeclarationDescriptorImpl.java */
/* renamed from: sb.j, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class DeclarationDescriptorImpl extends AnnotatedImpl implements DeclarationDescriptor {

    /* renamed from: f, reason: collision with root package name */
    private final Name f18288f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DeclarationDescriptorImpl(qb.g gVar, Name name) {
        super(gVar);
        if (gVar == null) {
            P(0);
        }
        if (name == null) {
            P(1);
        }
        this.f18288f = name;
    }

    private static /* synthetic */ void P(int i10) {
        String str = (i10 == 2 || i10 == 3 || i10 == 5 || i10 == 6) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 2 || i10 == 3 || i10 == 5 || i10 == 6) ? 2 : 3];
        switch (i10) {
            case 1:
                objArr[0] = "name";
                break;
            case 2:
            case 3:
            case 5:
            case 6:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/DeclarationDescriptorImpl";
                break;
            case 4:
                objArr[0] = "descriptor";
                break;
            default:
                objArr[0] = "annotations";
                break;
        }
        if (i10 == 2) {
            objArr[1] = "getName";
        } else if (i10 == 3) {
            objArr[1] = "getOriginal";
        } else if (i10 == 5 || i10 == 6) {
            objArr[1] = "toString";
        } else {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/DeclarationDescriptorImpl";
        }
        if (i10 != 2 && i10 != 3) {
            if (i10 == 4) {
                objArr[2] = "toString";
            } else if (i10 != 5 && i10 != 6) {
                objArr[2] = "<init>";
            }
        }
        String format = String.format(str, objArr);
        if (i10 != 2 && i10 != 3 && i10 != 5 && i10 != 6) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    public static String Q(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            P(4);
        }
        try {
            String str = rc.c.f17711j.q(declarationDescriptor) + "[" + declarationDescriptor.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(declarationDescriptor)) + "]";
            if (str == null) {
                P(5);
            }
            return str;
        } catch (Throwable unused) {
            String str2 = declarationDescriptor.getClass().getSimpleName() + " " + declarationDescriptor.getName();
            if (str2 == null) {
                P(6);
            }
            return str2;
        }
    }

    public DeclarationDescriptor a() {
        return this;
    }

    @Override // pb.Named
    public Name getName() {
        Name name = this.f18288f;
        if (name == null) {
            P(2);
        }
        return name;
    }

    public String toString() {
        return Q(this);
    }
}
