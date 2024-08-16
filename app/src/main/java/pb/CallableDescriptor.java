package pb;

import java.util.Collection;
import java.util.List;

/* compiled from: CallableDescriptor.java */
/* renamed from: pb.a, reason: use source file name */
/* loaded from: classes2.dex */
public interface CallableDescriptor extends DeclarationDescriptorNonRoot, DeclarationDescriptorWithVisibility, Substitutable<CallableDescriptor> {

    /* compiled from: CallableDescriptor.java */
    /* renamed from: pb.a$a */
    /* loaded from: classes2.dex */
    public interface a<V> {
    }

    <V> V E(a<V> aVar);

    boolean O();

    @Override // pb.DeclarationDescriptor
    CallableDescriptor a();

    Collection<? extends CallableDescriptor> e();

    gd.g0 f();

    List<ValueParameterDescriptor> l();

    List<TypeParameterDescriptor> m();

    ReceiverParameterDescriptor m0();

    ReceiverParameterDescriptor r0();

    List<ReceiverParameterDescriptor> w0();
}
