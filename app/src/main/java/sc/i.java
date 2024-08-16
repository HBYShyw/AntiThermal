package sc;

import pb.CallableMemberDescriptor;

/* compiled from: OverridingStrategy.kt */
/* loaded from: classes2.dex */
public abstract class i extends j {
    @Override // sc.j
    public void b(CallableMemberDescriptor callableMemberDescriptor, CallableMemberDescriptor callableMemberDescriptor2) {
        za.k.e(callableMemberDescriptor, "first");
        za.k.e(callableMemberDescriptor2, "second");
        e(callableMemberDescriptor, callableMemberDescriptor2);
    }

    @Override // sc.j
    public void c(CallableMemberDescriptor callableMemberDescriptor, CallableMemberDescriptor callableMemberDescriptor2) {
        za.k.e(callableMemberDescriptor, "fromSuper");
        za.k.e(callableMemberDescriptor2, "fromCurrent");
        e(callableMemberDescriptor, callableMemberDescriptor2);
    }

    protected abstract void e(CallableMemberDescriptor callableMemberDescriptor, CallableMemberDescriptor callableMemberDescriptor2);
}
