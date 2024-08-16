package cd;

import ed.DeserializedMemberScope;
import fd.StorageManager;
import oc.FqName;
import oc.Name;
import pb.ModuleDescriptor;
import sb.PackageFragmentDescriptorImpl;

/* compiled from: DeserializedPackageFragment.kt */
/* renamed from: cd.p, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class DeserializedPackageFragment extends PackageFragmentDescriptorImpl {

    /* renamed from: k, reason: collision with root package name */
    private final StorageManager f5276k;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DeserializedPackageFragment(FqName fqName, StorageManager storageManager, ModuleDescriptor moduleDescriptor) {
        super(moduleDescriptor, fqName);
        za.k.e(fqName, "fqName");
        za.k.e(storageManager, "storageManager");
        za.k.e(moduleDescriptor, "module");
        this.f5276k = storageManager;
    }

    public abstract ClassDataFinder O0();

    public boolean T0(Name name) {
        za.k.e(name, "name");
        zc.h u7 = u();
        return (u7 instanceof DeserializedMemberScope) && ((DeserializedMemberScope) u7).q().contains(name);
    }

    public abstract void U0(k kVar);
}
