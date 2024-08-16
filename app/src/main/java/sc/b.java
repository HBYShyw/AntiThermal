package sc;

import gd.TypeConstructor;
import hd.KotlinTypeChecker;
import pb.CallableDescriptor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class b implements KotlinTypeChecker.a {

    /* renamed from: a, reason: collision with root package name */
    private final boolean f18418a;

    /* renamed from: b, reason: collision with root package name */
    private final CallableDescriptor f18419b;

    /* renamed from: c, reason: collision with root package name */
    private final CallableDescriptor f18420c;

    public b(boolean z10, CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2) {
        this.f18418a = z10;
        this.f18419b = callableDescriptor;
        this.f18420c = callableDescriptor2;
    }

    @Override // hd.KotlinTypeChecker.a
    public boolean a(TypeConstructor typeConstructor, TypeConstructor typeConstructor2) {
        boolean d10;
        d10 = DescriptorEquivalenceForOverrides.d(this.f18418a, this.f18419b, this.f18420c, typeConstructor, typeConstructor2);
        return d10;
    }
}
