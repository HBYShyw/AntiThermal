package cd;

import fd.StorageManager;
import gd.b1;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import lc.BinaryVersion;
import lc.NameResolver;
import lc.TypeTable;
import lc.VersionRequirement;
import oc.ClassId;
import pb.ClassDescriptor;
import pb.ModuleDescriptor;
import pb.NotFoundClasses;
import pb.PackageFragmentDescriptor;
import pb.m0;
import qb.AnnotationDescriptor;
import rb.AdditionalClassPartsProvider;
import rb.ClassDescriptorFactory;
import rb.PlatformDependentTypeTransformer;
import rb.c;
import yc.SamConversionResolver;
import za.DefaultConstructorMarker;

/* compiled from: context.kt */
/* loaded from: classes2.dex */
public final class k {

    /* renamed from: a, reason: collision with root package name */
    private final StorageManager f5242a;

    /* renamed from: b, reason: collision with root package name */
    private final ModuleDescriptor f5243b;

    /* renamed from: c, reason: collision with root package name */
    private final DeserializationConfiguration f5244c;

    /* renamed from: d, reason: collision with root package name */
    private final ClassDataFinder f5245d;

    /* renamed from: e, reason: collision with root package name */
    private final AnnotationAndConstantLoader<AnnotationDescriptor, uc.g<?>> f5246e;

    /* renamed from: f, reason: collision with root package name */
    private final m0 f5247f;

    /* renamed from: g, reason: collision with root package name */
    private final LocalClassifierTypeSettings f5248g;

    /* renamed from: h, reason: collision with root package name */
    private final ErrorReporter f5249h;

    /* renamed from: i, reason: collision with root package name */
    private final xb.c f5250i;

    /* renamed from: j, reason: collision with root package name */
    private final FlexibleTypeDeserializer f5251j;

    /* renamed from: k, reason: collision with root package name */
    private final Iterable<ClassDescriptorFactory> f5252k;

    /* renamed from: l, reason: collision with root package name */
    private final NotFoundClasses f5253l;

    /* renamed from: m, reason: collision with root package name */
    private final ContractDeserializer f5254m;

    /* renamed from: n, reason: collision with root package name */
    private final AdditionalClassPartsProvider f5255n;

    /* renamed from: o, reason: collision with root package name */
    private final rb.c f5256o;

    /* renamed from: p, reason: collision with root package name */
    private final qc.g f5257p;

    /* renamed from: q, reason: collision with root package name */
    private final hd.l f5258q;

    /* renamed from: r, reason: collision with root package name */
    private final SamConversionResolver f5259r;

    /* renamed from: s, reason: collision with root package name */
    private final PlatformDependentTypeTransformer f5260s;

    /* renamed from: t, reason: collision with root package name */
    private final List<b1> f5261t;

    /* renamed from: u, reason: collision with root package name */
    private final ClassDeserializer f5262u;

    /* JADX WARN: Multi-variable type inference failed */
    public k(StorageManager storageManager, ModuleDescriptor moduleDescriptor, DeserializationConfiguration deserializationConfiguration, ClassDataFinder classDataFinder, AnnotationAndConstantLoader<? extends AnnotationDescriptor, ? extends uc.g<?>> annotationAndConstantLoader, m0 m0Var, LocalClassifierTypeSettings localClassifierTypeSettings, ErrorReporter errorReporter, xb.c cVar, FlexibleTypeDeserializer flexibleTypeDeserializer, Iterable<? extends ClassDescriptorFactory> iterable, NotFoundClasses notFoundClasses, ContractDeserializer contractDeserializer, AdditionalClassPartsProvider additionalClassPartsProvider, rb.c cVar2, qc.g gVar, hd.l lVar, SamConversionResolver samConversionResolver, PlatformDependentTypeTransformer platformDependentTypeTransformer, List<? extends b1> list) {
        za.k.e(storageManager, "storageManager");
        za.k.e(moduleDescriptor, "moduleDescriptor");
        za.k.e(deserializationConfiguration, "configuration");
        za.k.e(classDataFinder, "classDataFinder");
        za.k.e(annotationAndConstantLoader, "annotationAndConstantLoader");
        za.k.e(m0Var, "packageFragmentProvider");
        za.k.e(localClassifierTypeSettings, "localClassifierTypeSettings");
        za.k.e(errorReporter, "errorReporter");
        za.k.e(cVar, "lookupTracker");
        za.k.e(flexibleTypeDeserializer, "flexibleTypeDeserializer");
        za.k.e(iterable, "fictitiousClassDescriptorFactories");
        za.k.e(notFoundClasses, "notFoundClasses");
        za.k.e(contractDeserializer, "contractDeserializer");
        za.k.e(additionalClassPartsProvider, "additionalClassPartsProvider");
        za.k.e(cVar2, "platformDependentDeclarationFilter");
        za.k.e(gVar, "extensionRegistryLite");
        za.k.e(lVar, "kotlinTypeChecker");
        za.k.e(samConversionResolver, "samConversionResolver");
        za.k.e(platformDependentTypeTransformer, "platformDependentTypeTransformer");
        za.k.e(list, "typeAttributeTranslators");
        this.f5242a = storageManager;
        this.f5243b = moduleDescriptor;
        this.f5244c = deserializationConfiguration;
        this.f5245d = classDataFinder;
        this.f5246e = annotationAndConstantLoader;
        this.f5247f = m0Var;
        this.f5248g = localClassifierTypeSettings;
        this.f5249h = errorReporter;
        this.f5250i = cVar;
        this.f5251j = flexibleTypeDeserializer;
        this.f5252k = iterable;
        this.f5253l = notFoundClasses;
        this.f5254m = contractDeserializer;
        this.f5255n = additionalClassPartsProvider;
        this.f5256o = cVar2;
        this.f5257p = gVar;
        this.f5258q = lVar;
        this.f5259r = samConversionResolver;
        this.f5260s = platformDependentTypeTransformer;
        this.f5261t = list;
        this.f5262u = new ClassDeserializer(this);
    }

    public final m a(PackageFragmentDescriptor packageFragmentDescriptor, NameResolver nameResolver, TypeTable typeTable, VersionRequirement versionRequirement, BinaryVersion binaryVersion, ed.f fVar) {
        List j10;
        za.k.e(packageFragmentDescriptor, "descriptor");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(typeTable, "typeTable");
        za.k.e(versionRequirement, "versionRequirementTable");
        za.k.e(binaryVersion, "metadataVersion");
        j10 = kotlin.collections.r.j();
        return new m(this, nameResolver, packageFragmentDescriptor, typeTable, versionRequirement, binaryVersion, fVar, null, j10);
    }

    public final ClassDescriptor b(ClassId classId) {
        za.k.e(classId, "classId");
        return ClassDeserializer.e(this.f5262u, classId, null, 2, null);
    }

    public final AdditionalClassPartsProvider c() {
        return this.f5255n;
    }

    public final AnnotationAndConstantLoader<AnnotationDescriptor, uc.g<?>> d() {
        return this.f5246e;
    }

    public final ClassDataFinder e() {
        return this.f5245d;
    }

    public final ClassDeserializer f() {
        return this.f5262u;
    }

    public final DeserializationConfiguration g() {
        return this.f5244c;
    }

    public final ContractDeserializer h() {
        return this.f5254m;
    }

    public final ErrorReporter i() {
        return this.f5249h;
    }

    public final qc.g j() {
        return this.f5257p;
    }

    public final Iterable<ClassDescriptorFactory> k() {
        return this.f5252k;
    }

    public final FlexibleTypeDeserializer l() {
        return this.f5251j;
    }

    public final hd.l m() {
        return this.f5258q;
    }

    public final LocalClassifierTypeSettings n() {
        return this.f5248g;
    }

    public final xb.c o() {
        return this.f5250i;
    }

    public final ModuleDescriptor p() {
        return this.f5243b;
    }

    public final NotFoundClasses q() {
        return this.f5253l;
    }

    public final m0 r() {
        return this.f5247f;
    }

    public final rb.c s() {
        return this.f5256o;
    }

    public final PlatformDependentTypeTransformer t() {
        return this.f5260s;
    }

    public final StorageManager u() {
        return this.f5242a;
    }

    public final List<b1> v() {
        return this.f5261t;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ k(StorageManager storageManager, ModuleDescriptor moduleDescriptor, DeserializationConfiguration deserializationConfiguration, ClassDataFinder classDataFinder, AnnotationAndConstantLoader annotationAndConstantLoader, m0 m0Var, LocalClassifierTypeSettings localClassifierTypeSettings, ErrorReporter errorReporter, xb.c cVar, FlexibleTypeDeserializer flexibleTypeDeserializer, Iterable iterable, NotFoundClasses notFoundClasses, ContractDeserializer contractDeserializer, AdditionalClassPartsProvider additionalClassPartsProvider, rb.c cVar2, qc.g gVar, hd.l lVar, SamConversionResolver samConversionResolver, PlatformDependentTypeTransformer platformDependentTypeTransformer, List list, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(storageManager, moduleDescriptor, deserializationConfiguration, classDataFinder, annotationAndConstantLoader, m0Var, localClassifierTypeSettings, errorReporter, cVar, flexibleTypeDeserializer, iterable, notFoundClasses, contractDeserializer, r16, r17, gVar, r19, samConversionResolver, r21, r22);
        List list2;
        List e10;
        AdditionalClassPartsProvider additionalClassPartsProvider2 = (i10 & 8192) != 0 ? AdditionalClassPartsProvider.a.f17688a : additionalClassPartsProvider;
        rb.c cVar3 = (i10 & 16384) != 0 ? c.a.f17689a : cVar2;
        hd.l a10 = (65536 & i10) != 0 ? hd.l.f12233b.a() : lVar;
        PlatformDependentTypeTransformer platformDependentTypeTransformer2 = (262144 & i10) != 0 ? PlatformDependentTypeTransformer.a.f17692a : platformDependentTypeTransformer;
        if ((i10 & 524288) != 0) {
            e10 = CollectionsJVM.e(gd.o.f11859a);
            list2 = e10;
        } else {
            list2 = list;
        }
    }
}
