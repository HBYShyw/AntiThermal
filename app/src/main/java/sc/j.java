package sc;

import java.util.Collection;
import pb.CallableMemberDescriptor;

/* compiled from: OverridingStrategy.kt */
/* loaded from: classes2.dex */
public abstract class j {
    public abstract void a(CallableMemberDescriptor callableMemberDescriptor);

    public abstract void b(CallableMemberDescriptor callableMemberDescriptor, CallableMemberDescriptor callableMemberDescriptor2);

    public abstract void c(CallableMemberDescriptor callableMemberDescriptor, CallableMemberDescriptor callableMemberDescriptor2);

    public void d(CallableMemberDescriptor callableMemberDescriptor, Collection<? extends CallableMemberDescriptor> collection) {
        za.k.e(callableMemberDescriptor, "member");
        za.k.e(collection, "overridden");
        callableMemberDescriptor.D0(collection);
    }
}
