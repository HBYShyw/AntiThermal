package pb;

import ad.ReceiverValue;

/* compiled from: DescriptorVisibility.kt */
/* loaded from: classes2.dex */
public abstract class u {
    public final Integer a(u uVar) {
        za.k.e(uVar, "visibility");
        return b().a(uVar.b());
    }

    public abstract n1 b();

    public abstract String c();

    public final boolean d() {
        return b().c();
    }

    public abstract boolean e(ReceiverValue receiverValue, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptor declarationDescriptor, boolean z10);

    public abstract u f();

    public final String toString() {
        return b().toString();
    }
}
