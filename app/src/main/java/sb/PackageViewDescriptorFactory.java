package sb;

import fd.StorageManager;
import oc.FqName;
import pb.ModuleCapability;
import pb.PackageViewDescriptor;

/* compiled from: PackageViewDescriptorFactory.kt */
/* renamed from: sb.a0, reason: use source file name */
/* loaded from: classes2.dex */
public interface PackageViewDescriptorFactory {

    /* renamed from: a, reason: collision with root package name */
    public static final a f18210a = a.f18211a;

    /* compiled from: PackageViewDescriptorFactory.kt */
    /* renamed from: sb.a0$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ a f18211a = new a();

        /* renamed from: b, reason: collision with root package name */
        private static final ModuleCapability<PackageViewDescriptorFactory> f18212b = new ModuleCapability<>("PackageViewDescriptorFactory");

        private a() {
        }

        public final ModuleCapability<PackageViewDescriptorFactory> a() {
            return f18212b;
        }
    }

    /* compiled from: PackageViewDescriptorFactory.kt */
    /* renamed from: sb.a0$b */
    /* loaded from: classes2.dex */
    public static final class b implements PackageViewDescriptorFactory {

        /* renamed from: b, reason: collision with root package name */
        public static final b f18213b = new b();

        private b() {
        }

        @Override // sb.PackageViewDescriptorFactory
        public PackageViewDescriptor a(x xVar, FqName fqName, StorageManager storageManager) {
            za.k.e(xVar, "module");
            za.k.e(fqName, "fqName");
            za.k.e(storageManager, "storageManager");
            return new LazyPackageViewDescriptorImpl(xVar, fqName, storageManager);
        }
    }

    PackageViewDescriptor a(x xVar, FqName fqName, StorageManager storageManager);
}
