package sb;

import fd.StorageManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import kotlin.collections.s0;
import mb.KotlinBuiltIns;
import oc.FqName;
import oc.Name;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.ModuleCapability;
import pb.ModuleDescriptor;
import pb.PackageViewDescriptor;
import pc.TargetPlatform;
import sb.PackageViewDescriptorFactory;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: ModuleDescriptorImpl.kt */
/* loaded from: classes2.dex */
public final class x extends DeclarationDescriptorImpl implements ModuleDescriptor {

    /* renamed from: g, reason: collision with root package name */
    private final StorageManager f18395g;

    /* renamed from: h, reason: collision with root package name */
    private final KotlinBuiltIns f18396h;

    /* renamed from: i, reason: collision with root package name */
    private final Name f18397i;

    /* renamed from: j, reason: collision with root package name */
    private final Map<ModuleCapability<?>, Object> f18398j;

    /* renamed from: k, reason: collision with root package name */
    private final PackageViewDescriptorFactory f18399k;

    /* renamed from: l, reason: collision with root package name */
    private v f18400l;

    /* renamed from: m, reason: collision with root package name */
    private pb.m0 f18401m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f18402n;

    /* renamed from: o, reason: collision with root package name */
    private final fd.g<FqName, PackageViewDescriptor> f18403o;

    /* renamed from: p, reason: collision with root package name */
    private final ma.h f18404p;

    /* compiled from: ModuleDescriptorImpl.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<CompositePackageFragmentProvider> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CompositePackageFragmentProvider invoke() {
            int u7;
            v vVar = x.this.f18400l;
            x xVar = x.this;
            if (vVar != null) {
                List<x> a10 = vVar.a();
                x.this.X0();
                a10.contains(x.this);
                Iterator<T> it = a10.iterator();
                while (it.hasNext()) {
                    ((x) it.next()).c1();
                }
                u7 = kotlin.collections.s.u(a10, 10);
                ArrayList arrayList = new ArrayList(u7);
                Iterator<T> it2 = a10.iterator();
                while (it2.hasNext()) {
                    pb.m0 m0Var = ((x) it2.next()).f18401m;
                    za.k.b(m0Var);
                    arrayList.add(m0Var);
                }
                return new CompositePackageFragmentProvider(arrayList, "CompositeProvider@ModuleDescriptor for " + x.this.getName());
            }
            throw new AssertionError("Dependencies of module " + xVar.Y0() + " were not set before querying module content");
        }
    }

    /* compiled from: ModuleDescriptorImpl.kt */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.l<FqName, PackageViewDescriptor> {
        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final PackageViewDescriptor invoke(FqName fqName) {
            za.k.e(fqName, "fqName");
            PackageViewDescriptorFactory packageViewDescriptorFactory = x.this.f18399k;
            x xVar = x.this;
            return packageViewDescriptorFactory.a(xVar, fqName, xVar.f18395g);
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public x(Name name, StorageManager storageManager, KotlinBuiltIns kotlinBuiltIns, TargetPlatform targetPlatform) {
        this(name, storageManager, kotlinBuiltIns, targetPlatform, null, null, 48, null);
        za.k.e(name, "moduleName");
        za.k.e(storageManager, "storageManager");
        za.k.e(kotlinBuiltIns, "builtIns");
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ x(Name name, StorageManager storageManager, KotlinBuiltIns kotlinBuiltIns, TargetPlatform targetPlatform, Map map, Name name2, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(name, storageManager, kotlinBuiltIns, r6, r7, (i10 & 32) != 0 ? null : name2);
        Map map2;
        Map i11;
        TargetPlatform targetPlatform2 = (i10 & 8) != 0 ? null : targetPlatform;
        if ((i10 & 16) != 0) {
            i11 = kotlin.collections.m0.i();
            map2 = i11;
        } else {
            map2 = map;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String Y0() {
        String name = getName().toString();
        za.k.d(name, "name.toString()");
        return name;
    }

    private final CompositePackageFragmentProvider a1() {
        return (CompositePackageFragmentProvider) this.f18404p.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean c1() {
        return this.f18401m != null;
    }

    @Override // pb.ModuleDescriptor
    public boolean E0(ModuleDescriptor moduleDescriptor) {
        boolean L;
        za.k.e(moduleDescriptor, "targetModule");
        if (za.k.a(this, moduleDescriptor)) {
            return true;
        }
        v vVar = this.f18400l;
        za.k.b(vVar);
        L = _Collections.L(vVar.b(), moduleDescriptor);
        return L || y0().contains(moduleDescriptor) || moduleDescriptor.y0().contains(this);
    }

    @Override // pb.ModuleDescriptor
    public PackageViewDescriptor H(FqName fqName) {
        za.k.e(fqName, "fqName");
        X0();
        return this.f18403o.invoke(fqName);
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        return (R) ModuleDescriptor.a.a(this, declarationDescriptorVisitor, d10);
    }

    public void X0() {
        if (d1()) {
            return;
        }
        pb.b0.a(this);
    }

    public final pb.m0 Z0() {
        X0();
        return a1();
    }

    @Override // pb.DeclarationDescriptor
    public DeclarationDescriptor b() {
        return ModuleDescriptor.a.b(this);
    }

    public final void b1(pb.m0 m0Var) {
        za.k.e(m0Var, "providerForModuleContent");
        c1();
        this.f18401m = m0Var;
    }

    public boolean d1() {
        return this.f18402n;
    }

    public final void e1(List<x> list) {
        Set<x> e10;
        za.k.e(list, "descriptors");
        e10 = s0.e();
        f1(list, e10);
    }

    public final void f1(List<x> list, Set<x> set) {
        List j10;
        Set e10;
        za.k.e(list, "descriptors");
        za.k.e(set, "friends");
        j10 = kotlin.collections.r.j();
        e10 = s0.e();
        g1(new w(list, set, j10, e10));
    }

    public final void g1(v vVar) {
        za.k.e(vVar, "dependencies");
        this.f18400l = vVar;
    }

    public final void h1(x... xVarArr) {
        List<x> f02;
        za.k.e(xVarArr, "descriptors");
        f02 = _Arrays.f0(xVarArr);
        e1(f02);
    }

    @Override // pb.ModuleDescriptor
    public <T> T k0(ModuleCapability<T> moduleCapability) {
        za.k.e(moduleCapability, "capability");
        T t7 = (T) this.f18398j.get(moduleCapability);
        if (t7 == null) {
            return null;
        }
        return t7;
    }

    @Override // pb.ModuleDescriptor
    public KotlinBuiltIns t() {
        return this.f18396h;
    }

    @Override // pb.ModuleDescriptor
    public Collection<FqName> v(FqName fqName, ya.l<? super Name, Boolean> lVar) {
        za.k.e(fqName, "fqName");
        za.k.e(lVar, "nameFilter");
        X0();
        return Z0().v(fqName, lVar);
    }

    @Override // pb.ModuleDescriptor
    public List<ModuleDescriptor> y0() {
        v vVar = this.f18400l;
        if (vVar != null) {
            return vVar.c();
        }
        throw new AssertionError("Dependencies of module " + Y0() + " were not set");
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public x(Name name, StorageManager storageManager, KotlinBuiltIns kotlinBuiltIns, TargetPlatform targetPlatform, Map<ModuleCapability<?>, ? extends Object> map, Name name2) {
        super(qb.g.f17195b.b(), name);
        ma.h b10;
        za.k.e(name, "moduleName");
        za.k.e(storageManager, "storageManager");
        za.k.e(kotlinBuiltIns, "builtIns");
        za.k.e(map, "capabilities");
        this.f18395g = storageManager;
        this.f18396h = kotlinBuiltIns;
        this.f18397i = name2;
        if (name.g()) {
            this.f18398j = map;
            PackageViewDescriptorFactory packageViewDescriptorFactory = (PackageViewDescriptorFactory) k0(PackageViewDescriptorFactory.f18210a.a());
            this.f18399k = packageViewDescriptorFactory == null ? PackageViewDescriptorFactory.b.f18213b : packageViewDescriptorFactory;
            this.f18402n = true;
            this.f18403o = storageManager.d(new b());
            b10 = ma.j.b(new a());
            this.f18404p = b10;
            return;
        }
        throw new IllegalArgumentException("Module name must be special: " + name);
    }
}
