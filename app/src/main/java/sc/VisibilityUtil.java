package sc;

import java.util.Collection;
import pb.CallableMemberDescriptor;
import pb.DescriptorVisibilities;

/* compiled from: VisibilityUtil.kt */
/* renamed from: sc.q, reason: use source file name */
/* loaded from: classes2.dex */
public final class VisibilityUtil {
    public static final CallableMemberDescriptor a(Collection<? extends CallableMemberDescriptor> collection) {
        Integer d10;
        za.k.e(collection, "descriptors");
        collection.isEmpty();
        CallableMemberDescriptor callableMemberDescriptor = null;
        for (CallableMemberDescriptor callableMemberDescriptor2 : collection) {
            if (callableMemberDescriptor == null || ((d10 = DescriptorVisibilities.d(callableMemberDescriptor.g(), callableMemberDescriptor2.g())) != null && d10.intValue() < 0)) {
                callableMemberDescriptor = callableMemberDescriptor2;
            }
        }
        za.k.b(callableMemberDescriptor);
        return callableMemberDescriptor;
    }
}
