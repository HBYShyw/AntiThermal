package pb;

import java.util.Collection;
import oc.Name;

/* compiled from: ValueParameterDescriptor.kt */
/* renamed from: pb.j1, reason: use source file name */
/* loaded from: classes2.dex */
public interface ValueParameterDescriptor extends ParameterDescriptor, VariableDescriptor {
    ValueParameterDescriptor W(CallableDescriptor callableDescriptor, Name name, int i10);

    @Override // pb.CallableDescriptor, pb.DeclarationDescriptor
    ValueParameterDescriptor a();

    @Override // pb.ValueDescriptor, pb.DeclarationDescriptorNonRoot, pb.DeclarationDescriptor
    CallableDescriptor b();

    @Override // pb.CallableDescriptor
    Collection<ValueParameterDescriptor> e();

    boolean g0();

    boolean i0();

    int j();

    gd.g0 q0();

    boolean z0();
}
