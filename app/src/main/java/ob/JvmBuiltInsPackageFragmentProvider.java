package ob;

import cd.AbstractDeserializedPackageFragmentProvider;
import cd.AnnotationAndConstantLoaderImpl;
import cd.ContractDeserializer;
import cd.DeserializationConfiguration;
import cd.DeserializedClassDataFinder;
import cd.DeserializedPackageFragment;
import cd.ErrorReporter;
import cd.FlexibleTypeDeserializer;
import cd.LocalClassifierTypeSettings;
import dd.BuiltInSerializerProtocol;
import dd.BuiltInsPackageFragmentImpl;
import fd.StorageManager;
import hc.p;
import java.io.InputStream;
import java.util.List;
import kotlin.collections.r;
import nb.BuiltInFictitiousFunctionClassFactory;
import oc.FqName;
import pb.ModuleDescriptor;
import pb.NotFoundClasses;
import rb.AdditionalClassPartsProvider;
import xb.c;
import yc.SamConversionResolver;
import za.DefaultConstructorMarker;

/* compiled from: JvmBuiltInsPackageFragmentProvider.kt */
/* renamed from: ob.j, reason: use source file name */
/* loaded from: classes2.dex */
public final class JvmBuiltInsPackageFragmentProvider extends AbstractDeserializedPackageFragmentProvider {

    /* renamed from: f, reason: collision with root package name */
    public static final a f16413f = new a(null);

    /* compiled from: JvmBuiltInsPackageFragmentProvider.kt */
    /* renamed from: ob.j$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public JvmBuiltInsPackageFragmentProvider(StorageManager storageManager, p pVar, ModuleDescriptor moduleDescriptor, NotFoundClasses notFoundClasses, AdditionalClassPartsProvider additionalClassPartsProvider, rb.c cVar, DeserializationConfiguration deserializationConfiguration, hd.l lVar, SamConversionResolver samConversionResolver) {
        super(storageManager, pVar, moduleDescriptor);
        List m10;
        za.k.e(storageManager, "storageManager");
        za.k.e(pVar, "finder");
        za.k.e(moduleDescriptor, "moduleDescriptor");
        za.k.e(notFoundClasses, "notFoundClasses");
        za.k.e(additionalClassPartsProvider, "additionalClassPartsProvider");
        za.k.e(cVar, "platformDependentDeclarationFilter");
        za.k.e(deserializationConfiguration, "deserializationConfiguration");
        za.k.e(lVar, "kotlinTypeChecker");
        za.k.e(samConversionResolver, "samConversionResolver");
        DeserializedClassDataFinder deserializedClassDataFinder = new DeserializedClassDataFinder(this);
        BuiltInSerializerProtocol builtInSerializerProtocol = BuiltInSerializerProtocol.f10926n;
        AnnotationAndConstantLoaderImpl annotationAndConstantLoaderImpl = new AnnotationAndConstantLoaderImpl(moduleDescriptor, notFoundClasses, builtInSerializerProtocol);
        LocalClassifierTypeSettings.a aVar = LocalClassifierTypeSettings.a.f5291a;
        ErrorReporter errorReporter = ErrorReporter.f5285a;
        za.k.d(errorReporter, "DO_NOTHING");
        c.a aVar2 = c.a.f19665a;
        FlexibleTypeDeserializer.a aVar3 = FlexibleTypeDeserializer.a.f5286a;
        m10 = r.m(new BuiltInFictitiousFunctionClassFactory(storageManager, moduleDescriptor), new JvmBuiltInClassDescriptorFactory(storageManager, moduleDescriptor, null, 4, null));
        i(new cd.k(storageManager, moduleDescriptor, deserializationConfiguration, deserializedClassDataFinder, annotationAndConstantLoaderImpl, this, aVar, errorReporter, aVar2, aVar3, m10, notFoundClasses, ContractDeserializer.f5239a.a(), additionalClassPartsProvider, cVar, builtInSerializerProtocol.e(), lVar, samConversionResolver, null, null, 786432, null));
    }

    @Override // cd.AbstractDeserializedPackageFragmentProvider
    protected DeserializedPackageFragment d(FqName fqName) {
        za.k.e(fqName, "fqName");
        InputStream a10 = f().a(fqName);
        if (a10 != null) {
            return BuiltInsPackageFragmentImpl.f10928s.a(fqName, h(), g(), a10, false);
        }
        return null;
    }
}
