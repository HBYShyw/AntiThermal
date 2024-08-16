package cd;

import fd.StorageManager;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.collections.s0;
import oc.FqName;
import oc.Name;
import pb.DeclarationDescriptorWithSource;
import pb.ModuleDescriptor;
import pb.PackageFragmentDescriptor;
import pb.p0;
import qd.collections;
import za.Lambda;

/* compiled from: AbstractDeserializedPackageFragmentProvider.kt */
/* renamed from: cd.a, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractDeserializedPackageFragmentProvider implements p0 {

    /* renamed from: a, reason: collision with root package name */
    private final StorageManager f5182a;

    /* renamed from: b, reason: collision with root package name */
    private final KotlinMetadataFinder f5183b;

    /* renamed from: c, reason: collision with root package name */
    private final ModuleDescriptor f5184c;

    /* renamed from: d, reason: collision with root package name */
    protected k f5185d;

    /* renamed from: e, reason: collision with root package name */
    private final fd.h<FqName, PackageFragmentDescriptor> f5186e;

    /* compiled from: AbstractDeserializedPackageFragmentProvider.kt */
    /* renamed from: cd.a$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<FqName, PackageFragmentDescriptor> {
        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final PackageFragmentDescriptor invoke(FqName fqName) {
            za.k.e(fqName, "fqName");
            DeserializedPackageFragment d10 = AbstractDeserializedPackageFragmentProvider.this.d(fqName);
            if (d10 == null) {
                return null;
            }
            d10.U0(AbstractDeserializedPackageFragmentProvider.this.e());
            return d10;
        }
    }

    public AbstractDeserializedPackageFragmentProvider(StorageManager storageManager, KotlinMetadataFinder kotlinMetadataFinder, ModuleDescriptor moduleDescriptor) {
        za.k.e(storageManager, "storageManager");
        za.k.e(kotlinMetadataFinder, "finder");
        za.k.e(moduleDescriptor, "moduleDescriptor");
        this.f5182a = storageManager;
        this.f5183b = kotlinMetadataFinder;
        this.f5184c = moduleDescriptor;
        this.f5186e = storageManager.b(new a());
    }

    @Override // pb.p0
    public void a(FqName fqName, Collection<PackageFragmentDescriptor> collection) {
        za.k.e(fqName, "fqName");
        za.k.e(collection, "packageFragments");
        collections.a(collection, this.f5186e.invoke(fqName));
    }

    @Override // pb.m0
    public List<PackageFragmentDescriptor> b(FqName fqName) {
        List<PackageFragmentDescriptor> n10;
        za.k.e(fqName, "fqName");
        n10 = kotlin.collections.r.n(this.f5186e.invoke(fqName));
        return n10;
    }

    @Override // pb.p0
    public boolean c(FqName fqName) {
        DeclarationDescriptorWithSource d10;
        za.k.e(fqName, "fqName");
        if (this.f5186e.m(fqName)) {
            d10 = (PackageFragmentDescriptor) this.f5186e.invoke(fqName);
        } else {
            d10 = d(fqName);
        }
        return d10 == null;
    }

    protected abstract DeserializedPackageFragment d(FqName fqName);

    protected final k e() {
        k kVar = this.f5185d;
        if (kVar != null) {
            return kVar;
        }
        za.k.s("components");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final KotlinMetadataFinder f() {
        return this.f5183b;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final ModuleDescriptor g() {
        return this.f5184c;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final StorageManager h() {
        return this.f5182a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void i(k kVar) {
        za.k.e(kVar, "<set-?>");
        this.f5185d = kVar;
    }

    @Override // pb.m0
    public Collection<FqName> v(FqName fqName, ya.l<? super Name, Boolean> lVar) {
        Set e10;
        za.k.e(fqName, "fqName");
        za.k.e(lVar, "nameFilter");
        e10 = s0.e();
        return e10;
    }
}
