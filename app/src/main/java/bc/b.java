package bc;

import cd.ErrorReporter;
import fd.StorageManager;
import gc.l;
import hc.DeserializedDescriptorResolver;
import hc.PackagePartProvider;
import hc.p;
import mb.ReflectionTypes;
import pb.ModuleDescriptor;
import pb.SupertypeLoopChecker;
import yb.AnnotationTypeQualifierResolver;
import yb.JavaClassFinder;
import yb.JavaClassesTracker;
import yb.JavaModuleAnnotationsProvider;
import yb.JavaTypeEnhancementState;
import yc.SamConversionResolver;
import za.DefaultConstructorMarker;
import zb.JavaPropertyInitializerEvaluator;
import zb.JavaResolverCache;
import zb.SignaturePropagator;

/* compiled from: context.kt */
/* loaded from: classes2.dex */
public final class b {

    /* renamed from: a, reason: collision with root package name */
    private final StorageManager f4660a;

    /* renamed from: b, reason: collision with root package name */
    private final JavaClassFinder f4661b;

    /* renamed from: c, reason: collision with root package name */
    private final p f4662c;

    /* renamed from: d, reason: collision with root package name */
    private final DeserializedDescriptorResolver f4663d;

    /* renamed from: e, reason: collision with root package name */
    private final SignaturePropagator f4664e;

    /* renamed from: f, reason: collision with root package name */
    private final ErrorReporter f4665f;

    /* renamed from: g, reason: collision with root package name */
    private final JavaResolverCache f4666g;

    /* renamed from: h, reason: collision with root package name */
    private final JavaPropertyInitializerEvaluator f4667h;

    /* renamed from: i, reason: collision with root package name */
    private final SamConversionResolver f4668i;

    /* renamed from: j, reason: collision with root package name */
    private final ec.b f4669j;

    /* renamed from: k, reason: collision with root package name */
    private final i f4670k;

    /* renamed from: l, reason: collision with root package name */
    private final PackagePartProvider f4671l;

    /* renamed from: m, reason: collision with root package name */
    private final SupertypeLoopChecker f4672m;

    /* renamed from: n, reason: collision with root package name */
    private final xb.c f4673n;

    /* renamed from: o, reason: collision with root package name */
    private final ModuleDescriptor f4674o;

    /* renamed from: p, reason: collision with root package name */
    private final ReflectionTypes f4675p;

    /* renamed from: q, reason: collision with root package name */
    private final AnnotationTypeQualifierResolver f4676q;

    /* renamed from: r, reason: collision with root package name */
    private final l f4677r;

    /* renamed from: s, reason: collision with root package name */
    private final JavaClassesTracker f4678s;

    /* renamed from: t, reason: collision with root package name */
    private final c f4679t;

    /* renamed from: u, reason: collision with root package name */
    private final hd.l f4680u;

    /* renamed from: v, reason: collision with root package name */
    private final JavaTypeEnhancementState f4681v;

    /* renamed from: w, reason: collision with root package name */
    private final JavaModuleAnnotationsProvider f4682w;

    /* renamed from: x, reason: collision with root package name */
    private final xc.f f4683x;

    public b(StorageManager storageManager, JavaClassFinder javaClassFinder, p pVar, DeserializedDescriptorResolver deserializedDescriptorResolver, SignaturePropagator signaturePropagator, ErrorReporter errorReporter, JavaResolverCache javaResolverCache, JavaPropertyInitializerEvaluator javaPropertyInitializerEvaluator, SamConversionResolver samConversionResolver, ec.b bVar, i iVar, PackagePartProvider packagePartProvider, SupertypeLoopChecker supertypeLoopChecker, xb.c cVar, ModuleDescriptor moduleDescriptor, ReflectionTypes reflectionTypes, AnnotationTypeQualifierResolver annotationTypeQualifierResolver, l lVar, JavaClassesTracker javaClassesTracker, c cVar2, hd.l lVar2, JavaTypeEnhancementState javaTypeEnhancementState, JavaModuleAnnotationsProvider javaModuleAnnotationsProvider, xc.f fVar) {
        za.k.e(storageManager, "storageManager");
        za.k.e(javaClassFinder, "finder");
        za.k.e(pVar, "kotlinClassFinder");
        za.k.e(deserializedDescriptorResolver, "deserializedDescriptorResolver");
        za.k.e(signaturePropagator, "signaturePropagator");
        za.k.e(errorReporter, "errorReporter");
        za.k.e(javaResolverCache, "javaResolverCache");
        za.k.e(javaPropertyInitializerEvaluator, "javaPropertyInitializerEvaluator");
        za.k.e(samConversionResolver, "samConversionResolver");
        za.k.e(bVar, "sourceElementFactory");
        za.k.e(iVar, "moduleClassResolver");
        za.k.e(packagePartProvider, "packagePartProvider");
        za.k.e(supertypeLoopChecker, "supertypeLoopChecker");
        za.k.e(cVar, "lookupTracker");
        za.k.e(moduleDescriptor, "module");
        za.k.e(reflectionTypes, "reflectionTypes");
        za.k.e(annotationTypeQualifierResolver, "annotationTypeQualifierResolver");
        za.k.e(lVar, "signatureEnhancement");
        za.k.e(javaClassesTracker, "javaClassesTracker");
        za.k.e(cVar2, "settings");
        za.k.e(lVar2, "kotlinTypeChecker");
        za.k.e(javaTypeEnhancementState, "javaTypeEnhancementState");
        za.k.e(javaModuleAnnotationsProvider, "javaModuleResolver");
        za.k.e(fVar, "syntheticPartsProvider");
        this.f4660a = storageManager;
        this.f4661b = javaClassFinder;
        this.f4662c = pVar;
        this.f4663d = deserializedDescriptorResolver;
        this.f4664e = signaturePropagator;
        this.f4665f = errorReporter;
        this.f4666g = javaResolverCache;
        this.f4667h = javaPropertyInitializerEvaluator;
        this.f4668i = samConversionResolver;
        this.f4669j = bVar;
        this.f4670k = iVar;
        this.f4671l = packagePartProvider;
        this.f4672m = supertypeLoopChecker;
        this.f4673n = cVar;
        this.f4674o = moduleDescriptor;
        this.f4675p = reflectionTypes;
        this.f4676q = annotationTypeQualifierResolver;
        this.f4677r = lVar;
        this.f4678s = javaClassesTracker;
        this.f4679t = cVar2;
        this.f4680u = lVar2;
        this.f4681v = javaTypeEnhancementState;
        this.f4682w = javaModuleAnnotationsProvider;
        this.f4683x = fVar;
    }

    public final AnnotationTypeQualifierResolver a() {
        return this.f4676q;
    }

    public final DeserializedDescriptorResolver b() {
        return this.f4663d;
    }

    public final ErrorReporter c() {
        return this.f4665f;
    }

    public final JavaClassFinder d() {
        return this.f4661b;
    }

    public final JavaClassesTracker e() {
        return this.f4678s;
    }

    public final JavaModuleAnnotationsProvider f() {
        return this.f4682w;
    }

    public final JavaPropertyInitializerEvaluator g() {
        return this.f4667h;
    }

    public final JavaResolverCache h() {
        return this.f4666g;
    }

    public final JavaTypeEnhancementState i() {
        return this.f4681v;
    }

    public final p j() {
        return this.f4662c;
    }

    public final hd.l k() {
        return this.f4680u;
    }

    public final xb.c l() {
        return this.f4673n;
    }

    public final ModuleDescriptor m() {
        return this.f4674o;
    }

    public final i n() {
        return this.f4670k;
    }

    public final PackagePartProvider o() {
        return this.f4671l;
    }

    public final ReflectionTypes p() {
        return this.f4675p;
    }

    public final c q() {
        return this.f4679t;
    }

    public final l r() {
        return this.f4677r;
    }

    public final SignaturePropagator s() {
        return this.f4664e;
    }

    public final ec.b t() {
        return this.f4669j;
    }

    public final StorageManager u() {
        return this.f4660a;
    }

    public final SupertypeLoopChecker v() {
        return this.f4672m;
    }

    public final xc.f w() {
        return this.f4683x;
    }

    public final b x(JavaResolverCache javaResolverCache) {
        za.k.e(javaResolverCache, "javaResolverCache");
        return new b(this.f4660a, this.f4661b, this.f4662c, this.f4663d, this.f4664e, this.f4665f, javaResolverCache, this.f4667h, this.f4668i, this.f4669j, this.f4670k, this.f4671l, this.f4672m, this.f4673n, this.f4674o, this.f4675p, this.f4676q, this.f4677r, this.f4678s, this.f4679t, this.f4680u, this.f4681v, this.f4682w, null, 8388608, null);
    }

    public /* synthetic */ b(StorageManager storageManager, JavaClassFinder javaClassFinder, p pVar, DeserializedDescriptorResolver deserializedDescriptorResolver, SignaturePropagator signaturePropagator, ErrorReporter errorReporter, JavaResolverCache javaResolverCache, JavaPropertyInitializerEvaluator javaPropertyInitializerEvaluator, SamConversionResolver samConversionResolver, ec.b bVar, i iVar, PackagePartProvider packagePartProvider, SupertypeLoopChecker supertypeLoopChecker, xb.c cVar, ModuleDescriptor moduleDescriptor, ReflectionTypes reflectionTypes, AnnotationTypeQualifierResolver annotationTypeQualifierResolver, l lVar, JavaClassesTracker javaClassesTracker, c cVar2, hd.l lVar2, JavaTypeEnhancementState javaTypeEnhancementState, JavaModuleAnnotationsProvider javaModuleAnnotationsProvider, xc.f fVar, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(storageManager, javaClassFinder, pVar, deserializedDescriptorResolver, signaturePropagator, errorReporter, javaResolverCache, javaPropertyInitializerEvaluator, samConversionResolver, bVar, iVar, packagePartProvider, supertypeLoopChecker, cVar, moduleDescriptor, reflectionTypes, annotationTypeQualifierResolver, lVar, javaClassesTracker, cVar2, lVar2, javaTypeEnhancementState, javaModuleAnnotationsProvider, (i10 & 8388608) != 0 ? xc.f.f19717a.a() : fVar);
    }
}
