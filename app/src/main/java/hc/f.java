package hc;

import bc.LazyJavaPackageFragmentProvider;
import cd.ContractDeserializer;
import cd.DeserializationConfiguration;
import cd.ErrorReporter;
import cd.LocalClassifierTypeSettings;
import fd.LockBasedStorageManager;
import fd.StorageManager;
import java.util.List;
import jd.TypeAttributeTranslators;
import mb.KotlinBuiltIns;
import nc.JvmProtoBufUtil;
import ob.JvmBuiltIns;
import ob.JvmBuiltInsPackageFragmentProvider;
import oc.Name;
import pb.ModuleDescriptor;
import pb.NotFoundClasses;
import rb.AdditionalClassPartsProvider;
import rb.c;
import sb.CompositePackageFragmentProvider;
import xc.JavaDescriptorResolver;
import yb.JavaClassFinder;
import yc.SamConversionResolverImpl;
import za.DefaultConstructorMarker;
import zb.JavaResolverCache;

/* compiled from: DeserializationComponentsForJava.kt */
/* loaded from: classes2.dex */
public final class f {

    /* renamed from: b, reason: collision with root package name */
    public static final a f12162b = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final cd.k f12163a;

    /* compiled from: DeserializationComponentsForJava.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* compiled from: DeserializationComponentsForJava.kt */
        /* renamed from: hc.f$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0046a {

            /* renamed from: a, reason: collision with root package name */
            private final f f12164a;

            /* renamed from: b, reason: collision with root package name */
            private final DeserializedDescriptorResolver f12165b;

            public C0046a(f fVar, DeserializedDescriptorResolver deserializedDescriptorResolver) {
                za.k.e(fVar, "deserializationComponentsForJava");
                za.k.e(deserializedDescriptorResolver, "deserializedDescriptorResolver");
                this.f12164a = fVar;
                this.f12165b = deserializedDescriptorResolver;
            }

            public final f a() {
                return this.f12164a;
            }

            public final DeserializedDescriptorResolver b() {
                return this.f12165b;
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final C0046a a(p pVar, p pVar2, JavaClassFinder javaClassFinder, String str, ErrorReporter errorReporter, ec.b bVar) {
            List j10;
            List m10;
            za.k.e(pVar, "kotlinClassFinder");
            za.k.e(pVar2, "jvmBuiltInsKotlinClassFinder");
            za.k.e(javaClassFinder, "javaClassFinder");
            za.k.e(str, "moduleName");
            za.k.e(errorReporter, "errorReporter");
            za.k.e(bVar, "javaSourceElementFactory");
            LockBasedStorageManager lockBasedStorageManager = new LockBasedStorageManager("DeserializationComponentsForJava.ModuleData");
            JvmBuiltIns jvmBuiltIns = new JvmBuiltIns(lockBasedStorageManager, JvmBuiltIns.a.FROM_DEPENDENCIES);
            Name i10 = Name.i('<' + str + '>');
            za.k.d(i10, "special(\"<$moduleName>\")");
            sb.x xVar = new sb.x(i10, lockBasedStorageManager, jvmBuiltIns, null, null, null, 56, null);
            jvmBuiltIns.D0(xVar);
            jvmBuiltIns.I0(xVar, true);
            DeserializedDescriptorResolver deserializedDescriptorResolver = new DeserializedDescriptorResolver();
            bc.j jVar = new bc.j();
            NotFoundClasses notFoundClasses = new NotFoundClasses(lockBasedStorageManager, xVar);
            LazyJavaPackageFragmentProvider c10 = g.c(javaClassFinder, xVar, lockBasedStorageManager, notFoundClasses, pVar, deserializedDescriptorResolver, errorReporter, bVar, jVar, null, 512, null);
            f a10 = g.a(xVar, lockBasedStorageManager, notFoundClasses, c10, pVar, deserializedDescriptorResolver, errorReporter);
            deserializedDescriptorResolver.m(a10);
            JavaResolverCache javaResolverCache = JavaResolverCache.f20404a;
            za.k.d(javaResolverCache, "EMPTY");
            JavaDescriptorResolver javaDescriptorResolver = new JavaDescriptorResolver(c10, javaResolverCache);
            jVar.c(javaDescriptorResolver);
            ob.i H0 = jvmBuiltIns.H0();
            ob.i H02 = jvmBuiltIns.H0();
            DeserializationConfiguration.a aVar = DeserializationConfiguration.a.f5263a;
            hd.m a11 = hd.l.f12233b.a();
            j10 = kotlin.collections.r.j();
            JvmBuiltInsPackageFragmentProvider jvmBuiltInsPackageFragmentProvider = new JvmBuiltInsPackageFragmentProvider(lockBasedStorageManager, pVar2, xVar, notFoundClasses, H0, H02, aVar, a11, new SamConversionResolverImpl(lockBasedStorageManager, j10));
            xVar.h1(xVar);
            m10 = kotlin.collections.r.m(javaDescriptorResolver.a(), jvmBuiltInsPackageFragmentProvider);
            xVar.b1(new CompositePackageFragmentProvider(m10, "CompositeProvider@RuntimeModuleData for " + xVar));
            return new C0046a(a10, deserializedDescriptorResolver);
        }
    }

    public f(StorageManager storageManager, ModuleDescriptor moduleDescriptor, DeserializationConfiguration deserializationConfiguration, JavaClassDataFinder javaClassDataFinder, BinaryClassAnnotationAndConstantLoaderImpl binaryClassAnnotationAndConstantLoaderImpl, LazyJavaPackageFragmentProvider lazyJavaPackageFragmentProvider, NotFoundClasses notFoundClasses, ErrorReporter errorReporter, xb.c cVar, ContractDeserializer contractDeserializer, hd.l lVar, TypeAttributeTranslators typeAttributeTranslators) {
        List j10;
        List j11;
        AdditionalClassPartsProvider H0;
        za.k.e(storageManager, "storageManager");
        za.k.e(moduleDescriptor, "moduleDescriptor");
        za.k.e(deserializationConfiguration, "configuration");
        za.k.e(javaClassDataFinder, "classDataFinder");
        za.k.e(binaryClassAnnotationAndConstantLoaderImpl, "annotationAndConstantLoader");
        za.k.e(lazyJavaPackageFragmentProvider, "packageFragmentProvider");
        za.k.e(notFoundClasses, "notFoundClasses");
        za.k.e(errorReporter, "errorReporter");
        za.k.e(cVar, "lookupTracker");
        za.k.e(contractDeserializer, "contractDeserializer");
        za.k.e(lVar, "kotlinTypeChecker");
        za.k.e(typeAttributeTranslators, "typeAttributeTranslators");
        KotlinBuiltIns t7 = moduleDescriptor.t();
        JvmBuiltIns jvmBuiltIns = t7 instanceof JvmBuiltIns ? (JvmBuiltIns) t7 : null;
        LocalClassifierTypeSettings.a aVar = LocalClassifierTypeSettings.a.f5291a;
        JavaFlexibleTypeDeserializer javaFlexibleTypeDeserializer = JavaFlexibleTypeDeserializer.f12176a;
        j10 = kotlin.collections.r.j();
        AdditionalClassPartsProvider additionalClassPartsProvider = (jvmBuiltIns == null || (H0 = jvmBuiltIns.H0()) == null) ? AdditionalClassPartsProvider.a.f17688a : H0;
        rb.c cVar2 = (jvmBuiltIns == null || (cVar2 = jvmBuiltIns.H0()) == null) ? c.b.f17690a : cVar2;
        qc.g a10 = JvmProtoBufUtil.f16006a.a();
        j11 = kotlin.collections.r.j();
        this.f12163a = new cd.k(storageManager, moduleDescriptor, deserializationConfiguration, javaClassDataFinder, binaryClassAnnotationAndConstantLoaderImpl, lazyJavaPackageFragmentProvider, aVar, errorReporter, cVar, javaFlexibleTypeDeserializer, j10, notFoundClasses, contractDeserializer, additionalClassPartsProvider, cVar2, a10, lVar, new SamConversionResolverImpl(storageManager, j11), null, typeAttributeTranslators.a(), 262144, null);
    }

    public final cd.k a() {
        return this.f12163a;
    }
}
