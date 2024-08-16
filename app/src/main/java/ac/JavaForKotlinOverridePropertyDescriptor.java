package ac;

import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import za.k;

/* compiled from: JavaForKotlinOverridePropertyDescriptor.kt */
/* renamed from: ac.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class JavaForKotlinOverridePropertyDescriptor extends JavaPropertyDescriptor {
    private final SimpleFunctionDescriptor J;
    private final SimpleFunctionDescriptor K;
    private final PropertyDescriptor L;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public JavaForKotlinOverridePropertyDescriptor(ClassDescriptor classDescriptor, SimpleFunctionDescriptor simpleFunctionDescriptor, SimpleFunctionDescriptor simpleFunctionDescriptor2, PropertyDescriptor propertyDescriptor) {
        super(classDescriptor, qb.g.f17195b.b(), simpleFunctionDescriptor.o(), simpleFunctionDescriptor.g(), simpleFunctionDescriptor2 != null, propertyDescriptor.getName(), simpleFunctionDescriptor.z(), null, CallableMemberDescriptor.a.DECLARATION, false, null);
        k.e(classDescriptor, "ownerDescriptor");
        k.e(simpleFunctionDescriptor, "getterMethod");
        k.e(propertyDescriptor, "overriddenProperty");
        this.J = simpleFunctionDescriptor;
        this.K = simpleFunctionDescriptor2;
        this.L = propertyDescriptor;
    }
}
