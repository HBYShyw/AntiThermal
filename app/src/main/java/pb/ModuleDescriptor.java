package pb;

import java.util.Collection;
import java.util.List;
import mb.KotlinBuiltIns;
import oc.FqName;
import oc.Name;

/* compiled from: ModuleDescriptor.kt */
/* renamed from: pb.h0, reason: use source file name */
/* loaded from: classes2.dex */
public interface ModuleDescriptor extends DeclarationDescriptor {

    /* compiled from: ModuleDescriptor.kt */
    /* renamed from: pb.h0$a */
    /* loaded from: classes2.dex */
    public static final class a {
        public static <R, D> R a(ModuleDescriptor moduleDescriptor, DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
            za.k.e(declarationDescriptorVisitor, "visitor");
            return declarationDescriptorVisitor.j(moduleDescriptor, d10);
        }

        public static DeclarationDescriptor b(ModuleDescriptor moduleDescriptor) {
            return null;
        }
    }

    boolean E0(ModuleDescriptor moduleDescriptor);

    PackageViewDescriptor H(FqName fqName);

    <T> T k0(ModuleCapability<T> moduleCapability);

    KotlinBuiltIns t();

    Collection<FqName> v(FqName fqName, ya.l<? super Name, Boolean> lVar);

    List<ModuleDescriptor> y0();
}
