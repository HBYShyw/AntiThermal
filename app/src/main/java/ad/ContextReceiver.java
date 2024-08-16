package ad;

import gd.g0;
import oc.Name;
import pb.CallableDescriptor;
import za.k;

/* compiled from: ContextReceiver.kt */
/* renamed from: ad.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class ContextReceiver extends AbstractReceiverValue implements ImplicitReceiver {

    /* renamed from: c, reason: collision with root package name */
    private final CallableDescriptor f228c;

    /* renamed from: d, reason: collision with root package name */
    private final Name f229d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ContextReceiver(CallableDescriptor callableDescriptor, g0 g0Var, Name name, ReceiverValue receiverValue) {
        super(g0Var, receiverValue);
        k.e(callableDescriptor, "declarationDescriptor");
        k.e(g0Var, "receiverType");
        this.f228c = callableDescriptor;
        this.f229d = name;
    }

    @Override // ad.ImplicitReceiver
    public Name a() {
        return this.f229d;
    }

    public CallableDescriptor d() {
        return this.f228c;
    }

    public String toString() {
        return "Cxt { " + d() + " }";
    }
}
