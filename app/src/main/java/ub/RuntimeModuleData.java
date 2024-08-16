package ub;

import hc.f;
import ma.Unit;
import pb.ModuleDescriptor;
import za.DefaultConstructorMarker;

/* compiled from: RuntimeModuleData.kt */
/* renamed from: ub.k, reason: use source file name */
/* loaded from: classes2.dex */
public final class RuntimeModuleData {

    /* renamed from: c, reason: collision with root package name */
    public static final a f18981c = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final cd.k f18982a;

    /* renamed from: b, reason: collision with root package name */
    private final PackagePartScopeCache f18983b;

    /* compiled from: RuntimeModuleData.kt */
    /* renamed from: ub.k$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final RuntimeModuleData a(ClassLoader classLoader) {
            za.k.e(classLoader, "classLoader");
            g gVar = new g(classLoader);
            f.a aVar = hc.f.f12162b;
            ClassLoader classLoader2 = Unit.class.getClassLoader();
            za.k.d(classLoader2, "Unit::class.java.classLoader");
            f.a.C0046a a10 = aVar.a(gVar, new g(classLoader2), new d(classLoader), "runtime module for " + classLoader, RuntimeErrorReporter.f18980b, RuntimeSourceElementFactory.f18984a);
            return new RuntimeModuleData(a10.a().a(), new PackagePartScopeCache(a10.b(), gVar), null);
        }
    }

    private RuntimeModuleData(cd.k kVar, PackagePartScopeCache packagePartScopeCache) {
        this.f18982a = kVar;
        this.f18983b = packagePartScopeCache;
    }

    public /* synthetic */ RuntimeModuleData(cd.k kVar, PackagePartScopeCache packagePartScopeCache, DefaultConstructorMarker defaultConstructorMarker) {
        this(kVar, packagePartScopeCache);
    }

    public final cd.k a() {
        return this.f18982a;
    }

    public final ModuleDescriptor b() {
        return this.f18982a.p();
    }

    public final PackagePartScopeCache c() {
        return this.f18983b;
    }
}
