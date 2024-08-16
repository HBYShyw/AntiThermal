package cd;

import ed.DeserializedPackageMemberScope;
import fd.StorageManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import lc.BinaryVersion;
import lc.NameResolverImpl;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import pb.ModuleDescriptor;
import pb.SourceElement;
import za.Lambda;

/* compiled from: DeserializedPackageFragmentImpl.kt */
/* renamed from: cd.q, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class DeserializedPackageFragmentImpl extends DeserializedPackageFragment {

    /* renamed from: l, reason: collision with root package name */
    private final BinaryVersion f5277l;

    /* renamed from: m, reason: collision with root package name */
    private final ed.f f5278m;

    /* renamed from: n, reason: collision with root package name */
    private final NameResolverImpl f5279n;

    /* renamed from: o, reason: collision with root package name */
    private final ProtoBasedClassDataFinder f5280o;

    /* renamed from: p, reason: collision with root package name */
    private jc.m f5281p;

    /* renamed from: q, reason: collision with root package name */
    private zc.h f5282q;

    /* compiled from: DeserializedPackageFragmentImpl.kt */
    /* renamed from: cd.q$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<ClassId, SourceElement> {
        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final SourceElement invoke(ClassId classId) {
            za.k.e(classId, "it");
            ed.f fVar = DeserializedPackageFragmentImpl.this.f5278m;
            if (fVar != null) {
                return fVar;
            }
            SourceElement sourceElement = SourceElement.f16664a;
            za.k.d(sourceElement, "NO_SOURCE");
            return sourceElement;
        }
    }

    /* compiled from: DeserializedPackageFragmentImpl.kt */
    /* renamed from: cd.q$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<Collection<? extends Name>> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<Name> invoke() {
            int u7;
            Collection<ClassId> b10 = DeserializedPackageFragmentImpl.this.O0().b();
            ArrayList arrayList = new ArrayList();
            for (Object obj : b10) {
                ClassId classId = (ClassId) obj;
                if ((classId.l() || ClassDeserializer.f5232c.a().contains(classId)) ? false : true) {
                    arrayList.add(obj);
                }
            }
            u7 = kotlin.collections.s.u(arrayList, 10);
            ArrayList arrayList2 = new ArrayList(u7);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                arrayList2.add(((ClassId) it.next()).j());
            }
            return arrayList2;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DeserializedPackageFragmentImpl(FqName fqName, StorageManager storageManager, ModuleDescriptor moduleDescriptor, jc.m mVar, BinaryVersion binaryVersion, ed.f fVar) {
        super(fqName, storageManager, moduleDescriptor);
        za.k.e(fqName, "fqName");
        za.k.e(storageManager, "storageManager");
        za.k.e(moduleDescriptor, "module");
        za.k.e(mVar, "proto");
        za.k.e(binaryVersion, "metadataVersion");
        this.f5277l = binaryVersion;
        this.f5278m = fVar;
        jc.p J = mVar.J();
        za.k.d(J, "proto.strings");
        jc.o I = mVar.I();
        za.k.d(I, "proto.qualifiedNames");
        NameResolverImpl nameResolverImpl = new NameResolverImpl(J, I);
        this.f5279n = nameResolverImpl;
        this.f5280o = new ProtoBasedClassDataFinder(mVar, nameResolverImpl, binaryVersion, new a());
        this.f5281p = mVar;
    }

    @Override // cd.DeserializedPackageFragment
    public void U0(k kVar) {
        za.k.e(kVar, "components");
        jc.m mVar = this.f5281p;
        if (mVar != null) {
            this.f5281p = null;
            jc.l H = mVar.H();
            za.k.d(H, "proto.`package`");
            this.f5282q = new DeserializedPackageMemberScope(this, H, this.f5279n, this.f5277l, this.f5278m, kVar, "scope of " + this, new b());
            return;
        }
        throw new IllegalStateException("Repeated call to DeserializedPackageFragmentImpl::initialize".toString());
    }

    @Override // cd.DeserializedPackageFragment
    /* renamed from: W0, reason: merged with bridge method [inline-methods] */
    public ProtoBasedClassDataFinder O0() {
        return this.f5280o;
    }

    @Override // pb.PackageFragmentDescriptor
    public zc.h u() {
        zc.h hVar = this.f5282q;
        if (hVar != null) {
            return hVar;
        }
        za.k.s("_memberScope");
        return null;
    }
}
