package hc;

import bc.LazyJavaPackageFragmentProvider;
import bc.c;
import cd.ContractDeserializer;
import cd.DeserializationConfiguration;
import cd.ErrorReporter;
import fd.StorageManager;
import hc.PackagePartProvider;
import java.util.List;
import jd.TypeAttributeTranslators;
import kotlin.collections.CollectionsJVM;
import mb.ReflectionTypes;
import oc.ClassId;
import pb.ModuleDescriptor;
import pb.NotFoundClasses;
import pb.SupertypeLoopChecker;
import xb.c;
import yb.AnnotationTypeQualifierResolver;
import yb.JavaClassFinder;
import yb.JavaClassesTracker;
import yb.JavaModuleAnnotationsProvider;
import yb.JavaTypeEnhancementState;
import yc.SamConversionResolverImpl;
import zb.JavaPropertyInitializerEvaluator;
import zb.JavaResolverCache;
import zb.SignaturePropagator;

/* compiled from: DeserializationComponentsForJava.kt */
/* loaded from: classes2.dex */
public final class g {

    /* compiled from: DeserializationComponentsForJava.kt */
    /* loaded from: classes2.dex */
    public static final class a implements JavaModuleAnnotationsProvider {
        a() {
        }

        @Override // yb.JavaModuleAnnotationsProvider
        public List<fc.a> a(ClassId classId) {
            za.k.e(classId, "classId");
            return null;
        }
    }

    public static final f a(ModuleDescriptor moduleDescriptor, StorageManager storageManager, NotFoundClasses notFoundClasses, LazyJavaPackageFragmentProvider lazyJavaPackageFragmentProvider, p pVar, DeserializedDescriptorResolver deserializedDescriptorResolver, ErrorReporter errorReporter) {
        List e10;
        za.k.e(moduleDescriptor, "module");
        za.k.e(storageManager, "storageManager");
        za.k.e(notFoundClasses, "notFoundClasses");
        za.k.e(lazyJavaPackageFragmentProvider, "lazyJavaPackageFragmentProvider");
        za.k.e(pVar, "reflectKotlinClassFinder");
        za.k.e(deserializedDescriptorResolver, "deserializedDescriptorResolver");
        za.k.e(errorReporter, "errorReporter");
        JavaClassDataFinder javaClassDataFinder = new JavaClassDataFinder(pVar, deserializedDescriptorResolver);
        BinaryClassAnnotationAndConstantLoaderImpl binaryClassAnnotationAndConstantLoaderImpl = new BinaryClassAnnotationAndConstantLoaderImpl(moduleDescriptor, notFoundClasses, storageManager, pVar);
        DeserializationConfiguration.a aVar = DeserializationConfiguration.a.f5263a;
        c.a aVar2 = c.a.f19665a;
        ContractDeserializer a10 = ContractDeserializer.f5239a.a();
        hd.m a11 = hd.l.f12233b.a();
        e10 = CollectionsJVM.e(gd.o.f11859a);
        return new f(storageManager, moduleDescriptor, aVar, javaClassDataFinder, binaryClassAnnotationAndConstantLoaderImpl, lazyJavaPackageFragmentProvider, notFoundClasses, errorReporter, aVar2, a10, a11, new TypeAttributeTranslators(e10));
    }

    public static final LazyJavaPackageFragmentProvider b(JavaClassFinder javaClassFinder, ModuleDescriptor moduleDescriptor, StorageManager storageManager, NotFoundClasses notFoundClasses, p pVar, DeserializedDescriptorResolver deserializedDescriptorResolver, ErrorReporter errorReporter, ec.b bVar, bc.i iVar, PackagePartProvider packagePartProvider) {
        List j10;
        za.k.e(javaClassFinder, "javaClassFinder");
        za.k.e(moduleDescriptor, "module");
        za.k.e(storageManager, "storageManager");
        za.k.e(notFoundClasses, "notFoundClasses");
        za.k.e(pVar, "reflectKotlinClassFinder");
        za.k.e(deserializedDescriptorResolver, "deserializedDescriptorResolver");
        za.k.e(errorReporter, "errorReporter");
        za.k.e(bVar, "javaSourceElementFactory");
        za.k.e(iVar, "singleModuleClassResolver");
        za.k.e(packagePartProvider, "packagePartProvider");
        SignaturePropagator signaturePropagator = SignaturePropagator.f20411a;
        za.k.d(signaturePropagator, "DO_NOTHING");
        JavaResolverCache javaResolverCache = JavaResolverCache.f20404a;
        za.k.d(javaResolverCache, "EMPTY");
        JavaPropertyInitializerEvaluator.a aVar = JavaPropertyInitializerEvaluator.a.f20403a;
        j10 = kotlin.collections.r.j();
        SamConversionResolverImpl samConversionResolverImpl = new SamConversionResolverImpl(storageManager, j10);
        SupertypeLoopChecker.a aVar2 = SupertypeLoopChecker.a.f16675a;
        c.a aVar3 = c.a.f19665a;
        ReflectionTypes reflectionTypes = new ReflectionTypes(moduleDescriptor, notFoundClasses);
        JavaTypeEnhancementState.b bVar2 = JavaTypeEnhancementState.f20148d;
        AnnotationTypeQualifierResolver annotationTypeQualifierResolver = new AnnotationTypeQualifierResolver(bVar2.a());
        c.a aVar4 = c.a.f4684a;
        return new LazyJavaPackageFragmentProvider(new bc.b(storageManager, javaClassFinder, pVar, deserializedDescriptorResolver, signaturePropagator, errorReporter, javaResolverCache, aVar, samConversionResolverImpl, bVar, iVar, packagePartProvider, aVar2, aVar3, moduleDescriptor, reflectionTypes, annotationTypeQualifierResolver, new gc.l(new gc.d(aVar4)), JavaClassesTracker.a.f20127a, aVar4, hd.l.f12233b.a(), bVar2.a(), new a(), null, 8388608, null));
    }

    public static /* synthetic */ LazyJavaPackageFragmentProvider c(JavaClassFinder javaClassFinder, ModuleDescriptor moduleDescriptor, StorageManager storageManager, NotFoundClasses notFoundClasses, p pVar, DeserializedDescriptorResolver deserializedDescriptorResolver, ErrorReporter errorReporter, ec.b bVar, bc.i iVar, PackagePartProvider packagePartProvider, int i10, Object obj) {
        return b(javaClassFinder, moduleDescriptor, storageManager, notFoundClasses, pVar, deserializedDescriptorResolver, errorReporter, bVar, iVar, (i10 & 512) != 0 ? PackagePartProvider.a.f12208a : packagePartProvider);
    }
}
