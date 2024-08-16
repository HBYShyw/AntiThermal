package sb;

import ad.ReceiverValue;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import pb.DeclarationDescriptor;

/* compiled from: ReceiverParameterDescriptorImpl.java */
/* renamed from: sb.f0, reason: use source file name */
/* loaded from: classes2.dex */
public class ReceiverParameterDescriptorImpl extends AbstractReceiverParameterDescriptor {

    /* renamed from: g, reason: collision with root package name */
    private final DeclarationDescriptor f18273g;

    /* renamed from: h, reason: collision with root package name */
    private ReceiverValue f18274h;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ReceiverParameterDescriptorImpl(DeclarationDescriptor declarationDescriptor, ReceiverValue receiverValue, qb.g gVar) {
        super(gVar);
        if (declarationDescriptor == null) {
            P(0);
        }
        if (receiverValue == null) {
            P(1);
        }
        if (gVar == null) {
            P(2);
        }
        this.f18273g = declarationDescriptor;
        this.f18274h = receiverValue;
    }

    private static /* synthetic */ void P(int i10) {
        String str = (i10 == 3 || i10 == 4) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 3 || i10 == 4) ? 2 : 3];
        switch (i10) {
            case 1:
                objArr[0] = ThermalBaseConfig.Item.ATTR_VALUE;
                break;
            case 2:
                objArr[0] = "annotations";
                break;
            case 3:
            case 4:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/ReceiverParameterDescriptorImpl";
                break;
            case 5:
                objArr[0] = "newOwner";
                break;
            case 6:
                objArr[0] = "outType";
                break;
            default:
                objArr[0] = "containingDeclaration";
                break;
        }
        if (i10 == 3) {
            objArr[1] = "getValue";
        } else if (i10 != 4) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/ReceiverParameterDescriptorImpl";
        } else {
            objArr[1] = "getContainingDeclaration";
        }
        if (i10 != 3 && i10 != 4) {
            if (i10 == 5) {
                objArr[2] = "copy";
            } else if (i10 != 6) {
                objArr[2] = "<init>";
            } else {
                objArr[2] = "setOutType";
            }
        }
        String format = String.format(str, objArr);
        if (i10 != 3 && i10 != 4) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    @Override // pb.DeclarationDescriptor
    public DeclarationDescriptor b() {
        DeclarationDescriptor declarationDescriptor = this.f18273g;
        if (declarationDescriptor == null) {
            P(4);
        }
        return declarationDescriptor;
    }

    @Override // pb.ReceiverParameterDescriptor
    public ReceiverValue getValue() {
        ReceiverValue receiverValue = this.f18274h;
        if (receiverValue == null) {
            P(3);
        }
        return receiverValue;
    }
}
