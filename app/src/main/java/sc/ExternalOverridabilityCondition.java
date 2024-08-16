package sc;

import pb.CallableDescriptor;
import pb.ClassDescriptor;

/* compiled from: ExternalOverridabilityCondition.java */
/* renamed from: sc.f, reason: use source file name */
/* loaded from: classes2.dex */
public interface ExternalOverridabilityCondition {

    /* compiled from: ExternalOverridabilityCondition.java */
    /* renamed from: sc.f$a */
    /* loaded from: classes2.dex */
    public enum a {
        CONFLICTS_ONLY,
        SUCCESS_ONLY,
        BOTH
    }

    /* compiled from: ExternalOverridabilityCondition.java */
    /* renamed from: sc.f$b */
    /* loaded from: classes2.dex */
    public enum b {
        OVERRIDABLE,
        CONFLICT,
        INCOMPATIBLE,
        UNKNOWN
    }

    a a();

    b b(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2, ClassDescriptor classDescriptor);
}
