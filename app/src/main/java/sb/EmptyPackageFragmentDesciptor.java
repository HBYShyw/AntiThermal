package sb;

import oc.FqName;
import pb.ModuleDescriptor;
import zc.h;

/* compiled from: EmptyPackageFragmentDesciptor.kt */
/* renamed from: sb.m, reason: use source file name */
/* loaded from: classes2.dex */
public final class EmptyPackageFragmentDesciptor extends PackageFragmentDescriptorImpl {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public EmptyPackageFragmentDesciptor(ModuleDescriptor moduleDescriptor, FqName fqName) {
        super(moduleDescriptor, fqName);
        za.k.e(moduleDescriptor, "module");
        za.k.e(fqName, "fqName");
    }

    @Override // pb.PackageFragmentDescriptor
    /* renamed from: O0, reason: merged with bridge method [inline-methods] */
    public h.b u() {
        return h.b.f20465b;
    }
}
