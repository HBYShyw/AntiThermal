package dd;

import cd.AnnotationAndConstantLoaderImpl;
import cd.ContractDeserializer;
import cd.DeserializationConfiguration;
import cd.DeserializedClassDataFinder;
import cd.ErrorReporter;
import cd.FlexibleTypeDeserializer;
import cd.LocalClassifierTypeSettings;
import fd.StorageManager;
import gb.KDeclarationContainer;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.collections.r;
import kotlin.collections.s;
import mb.BuiltInsLoader;
import mb.StandardNames;
import oc.FqName;
import pb.ModuleDescriptor;
import pb.NotFoundClasses;
import pb.PackageFragmentProviderImpl;
import pb.m0;
import qc.g;
import rb.AdditionalClassPartsProvider;
import rb.ClassDescriptorFactory;
import xb.c;
import ya.l;
import yc.SamConversionResolverImpl;
import za.FunctionReference;
import za.Reflection;
import za.k;

/* compiled from: BuiltInsLoaderImpl.kt */
/* renamed from: dd.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class BuiltInsLoaderImpl implements BuiltInsLoader {

    /* renamed from: b, reason: collision with root package name */
    private final BuiltInsResourceLoader f10927b = new BuiltInsResourceLoader();

    /* compiled from: BuiltInsLoaderImpl.kt */
    /* renamed from: dd.b$a */
    /* loaded from: classes2.dex */
    /* synthetic */ class a extends FunctionReference implements l<String, InputStream> {
        a(Object obj) {
            super(1, obj);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(BuiltInsResourceLoader.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "loadResource(Ljava/lang/String;)Ljava/io/InputStream;";
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final InputStream invoke(String str) {
            k.e(str, "p0");
            return ((BuiltInsResourceLoader) this.f20351f).a(str);
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "loadResource";
        }
    }

    @Override // mb.BuiltInsLoader
    public m0 a(StorageManager storageManager, ModuleDescriptor moduleDescriptor, Iterable<? extends ClassDescriptorFactory> iterable, rb.c cVar, AdditionalClassPartsProvider additionalClassPartsProvider, boolean z10) {
        k.e(storageManager, "storageManager");
        k.e(moduleDescriptor, "builtInsModule");
        k.e(iterable, "classDescriptorFactories");
        k.e(cVar, "platformDependentDeclarationFilter");
        k.e(additionalClassPartsProvider, "additionalClassPartsProvider");
        return b(storageManager, moduleDescriptor, StandardNames.A, iterable, cVar, additionalClassPartsProvider, z10, new a(this.f10927b));
    }

    public final m0 b(StorageManager storageManager, ModuleDescriptor moduleDescriptor, Set<FqName> set, Iterable<? extends ClassDescriptorFactory> iterable, rb.c cVar, AdditionalClassPartsProvider additionalClassPartsProvider, boolean z10, l<? super String, ? extends InputStream> lVar) {
        int u7;
        List j10;
        k.e(storageManager, "storageManager");
        k.e(moduleDescriptor, "module");
        k.e(set, "packageFqNames");
        k.e(iterable, "classDescriptorFactories");
        k.e(cVar, "platformDependentDeclarationFilter");
        k.e(additionalClassPartsProvider, "additionalClassPartsProvider");
        k.e(lVar, "loadResource");
        u7 = s.u(set, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (FqName fqName : set) {
            String n10 = BuiltInSerializerProtocol.f10926n.n(fqName);
            InputStream invoke = lVar.invoke(n10);
            if (invoke != null) {
                arrayList.add(BuiltInsPackageFragmentImpl.f10928s.a(fqName, storageManager, moduleDescriptor, invoke, z10));
            } else {
                throw new IllegalStateException("Resource not found in classpath: " + n10);
            }
        }
        PackageFragmentProviderImpl packageFragmentProviderImpl = new PackageFragmentProviderImpl(arrayList);
        NotFoundClasses notFoundClasses = new NotFoundClasses(storageManager, moduleDescriptor);
        DeserializationConfiguration.a aVar = DeserializationConfiguration.a.f5263a;
        DeserializedClassDataFinder deserializedClassDataFinder = new DeserializedClassDataFinder(packageFragmentProviderImpl);
        BuiltInSerializerProtocol builtInSerializerProtocol = BuiltInSerializerProtocol.f10926n;
        AnnotationAndConstantLoaderImpl annotationAndConstantLoaderImpl = new AnnotationAndConstantLoaderImpl(moduleDescriptor, notFoundClasses, builtInSerializerProtocol);
        LocalClassifierTypeSettings.a aVar2 = LocalClassifierTypeSettings.a.f5291a;
        ErrorReporter errorReporter = ErrorReporter.f5285a;
        k.d(errorReporter, "DO_NOTHING");
        c.a aVar3 = c.a.f19665a;
        FlexibleTypeDeserializer.a aVar4 = FlexibleTypeDeserializer.a.f5286a;
        ContractDeserializer a10 = ContractDeserializer.f5239a.a();
        g e10 = builtInSerializerProtocol.e();
        j10 = r.j();
        cd.k kVar = new cd.k(storageManager, moduleDescriptor, aVar, deserializedClassDataFinder, annotationAndConstantLoaderImpl, packageFragmentProviderImpl, aVar2, errorReporter, aVar3, aVar4, iterable, notFoundClasses, a10, additionalClassPartsProvider, cVar, e10, null, new SamConversionResolverImpl(storageManager, j10), null, null, 851968, null);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((BuiltInsPackageFragmentImpl) it.next()).U0(kVar);
        }
        return packageFragmentProviderImpl;
    }
}
