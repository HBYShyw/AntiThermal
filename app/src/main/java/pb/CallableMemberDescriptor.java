package pb;

import java.util.Collection;

/* compiled from: CallableMemberDescriptor.java */
/* renamed from: pb.b, reason: use source file name */
/* loaded from: classes2.dex */
public interface CallableMemberDescriptor extends CallableDescriptor, MemberDescriptor {

    /* compiled from: CallableMemberDescriptor.java */
    /* renamed from: pb.b$a */
    /* loaded from: classes2.dex */
    public enum a {
        DECLARATION,
        FAKE_OVERRIDE,
        DELEGATION,
        SYNTHESIZED;

        public boolean a() {
            return this != FAKE_OVERRIDE;
        }
    }

    void D0(Collection<? extends CallableMemberDescriptor> collection);

    CallableMemberDescriptor T(DeclarationDescriptor declarationDescriptor, Modality modality, u uVar, a aVar, boolean z10);

    @Override // pb.CallableDescriptor, pb.DeclarationDescriptor
    CallableMemberDescriptor a();

    @Override // pb.CallableDescriptor
    Collection<? extends CallableMemberDescriptor> e();

    a getKind();
}
