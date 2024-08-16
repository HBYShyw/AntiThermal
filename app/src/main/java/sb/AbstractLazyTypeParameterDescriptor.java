package sb;

import fd.StorageManager;
import gd.Variance;
import oc.Name;
import pb.DeclarationDescriptor;
import pb.SourceElement;
import pb.SupertypeLoopChecker;

/* compiled from: AbstractLazyTypeParameterDescriptor.java */
/* renamed from: sb.b, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractLazyTypeParameterDescriptor extends AbstractTypeParameterDescriptor {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AbstractLazyTypeParameterDescriptor(StorageManager storageManager, DeclarationDescriptor declarationDescriptor, qb.g gVar, Name name, Variance variance, boolean z10, int i10, SourceElement sourceElement, SupertypeLoopChecker supertypeLoopChecker) {
        super(storageManager, declarationDescriptor, gVar, name, variance, z10, i10, sourceElement, supertypeLoopChecker);
        if (storageManager == null) {
            P(0);
        }
        if (declarationDescriptor == null) {
            P(1);
        }
        if (gVar == null) {
            P(2);
        }
        if (name == null) {
            P(3);
        }
        if (variance == null) {
            P(4);
        }
        if (sourceElement == null) {
            P(5);
        }
        if (supertypeLoopChecker == null) {
            P(6);
        }
    }

    private static /* synthetic */ void P(int i10) {
        Object[] objArr = new Object[3];
        switch (i10) {
            case 1:
                objArr[0] = "containingDeclaration";
                break;
            case 2:
                objArr[0] = "annotations";
                break;
            case 3:
                objArr[0] = "name";
                break;
            case 4:
                objArr[0] = "variance";
                break;
            case 5:
                objArr[0] = "source";
                break;
            case 6:
                objArr[0] = "supertypeLoopChecker";
                break;
            default:
                objArr[0] = "storageManager";
                break;
        }
        objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/AbstractLazyTypeParameterDescriptor";
        objArr[2] = "<init>";
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
    }

    @Override // sb.DeclarationDescriptorImpl
    public String toString() {
        Object[] objArr = new Object[3];
        String str = "";
        objArr[0] = N() ? "reified " : "";
        if (s() != Variance.INVARIANT) {
            str = s() + " ";
        }
        objArr[1] = str;
        objArr[2] = getName();
        return String.format("%s%s%s", objArr);
    }
}
