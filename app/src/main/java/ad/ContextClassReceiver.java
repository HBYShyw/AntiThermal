package ad;

import gd.g0;
import oc.Name;
import pb.ClassDescriptor;
import za.k;

/* compiled from: ContextClassReceiver.kt */
/* renamed from: ad.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class ContextClassReceiver extends AbstractReceiverValue implements ImplicitReceiver {

    /* renamed from: c, reason: collision with root package name */
    private final ClassDescriptor f226c;

    /* renamed from: d, reason: collision with root package name */
    private final Name f227d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ContextClassReceiver(ClassDescriptor classDescriptor, g0 g0Var, Name name, ReceiverValue receiverValue) {
        super(g0Var, receiverValue);
        k.e(classDescriptor, "classDescriptor");
        k.e(g0Var, "receiverType");
        this.f226c = classDescriptor;
        this.f227d = name;
    }

    @Override // ad.ImplicitReceiver
    public Name a() {
        return this.f227d;
    }

    public String toString() {
        return getType() + ": Ctx { " + this.f226c + " }";
    }
}
