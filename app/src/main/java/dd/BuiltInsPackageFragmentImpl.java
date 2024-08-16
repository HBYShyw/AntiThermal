package dd;

import cd.DeserializedPackageFragmentImpl;
import fd.StorageManager;
import java.io.InputStream;
import jc.m;
import kc.BuiltInsBinaryVersion;
import kc.readPackageFragment;
import ma.o;
import mb.BuiltInsPackageFragment;
import oc.FqName;
import pb.ModuleDescriptor;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: BuiltInsPackageFragmentImpl.kt */
/* renamed from: dd.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class BuiltInsPackageFragmentImpl extends DeserializedPackageFragmentImpl implements BuiltInsPackageFragment {

    /* renamed from: s, reason: collision with root package name */
    public static final a f10928s = new a(null);

    /* renamed from: r, reason: collision with root package name */
    private final boolean f10929r;

    /* compiled from: BuiltInsPackageFragmentImpl.kt */
    /* renamed from: dd.c$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final BuiltInsPackageFragmentImpl a(FqName fqName, StorageManager storageManager, ModuleDescriptor moduleDescriptor, InputStream inputStream, boolean z10) {
            k.e(fqName, "fqName");
            k.e(storageManager, "storageManager");
            k.e(moduleDescriptor, "module");
            k.e(inputStream, "inputStream");
            o<m, BuiltInsBinaryVersion> a10 = readPackageFragment.a(inputStream);
            m a11 = a10.a();
            BuiltInsBinaryVersion b10 = a10.b();
            if (a11 != null) {
                return new BuiltInsPackageFragmentImpl(fqName, storageManager, moduleDescriptor, a11, b10, z10, null);
            }
            throw new UnsupportedOperationException("Kotlin built-in definition format version is not supported: expected " + BuiltInsBinaryVersion.f14280h + ", actual " + b10 + ". Please update Kotlin");
        }
    }

    private BuiltInsPackageFragmentImpl(FqName fqName, StorageManager storageManager, ModuleDescriptor moduleDescriptor, m mVar, BuiltInsBinaryVersion builtInsBinaryVersion, boolean z10) {
        super(fqName, storageManager, moduleDescriptor, mVar, builtInsBinaryVersion, null);
        this.f10929r = z10;
    }

    public /* synthetic */ BuiltInsPackageFragmentImpl(FqName fqName, StorageManager storageManager, ModuleDescriptor moduleDescriptor, m mVar, BuiltInsBinaryVersion builtInsBinaryVersion, boolean z10, DefaultConstructorMarker defaultConstructorMarker) {
        this(fqName, storageManager, moduleDescriptor, mVar, builtInsBinaryVersion, z10);
    }

    @Override // sb.PackageFragmentDescriptorImpl, sb.DeclarationDescriptorImpl
    public String toString() {
        return "builtins package fragment for " + d() + " from " + wc.c.p(this);
    }
}
