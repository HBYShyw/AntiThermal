package id;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.collections.r;
import kotlin.collections.s0;
import mb.DefaultBuiltIns;
import mb.KotlinBuiltIns;
import oc.FqName;
import oc.Name;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.ModuleCapability;
import pb.ModuleDescriptor;
import pb.PackageViewDescriptor;

/* compiled from: ErrorModuleDescriptor.kt */
/* renamed from: id.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class ErrorModuleDescriptor implements ModuleDescriptor {

    /* renamed from: e, reason: collision with root package name */
    public static final ErrorModuleDescriptor f12754e = new ErrorModuleDescriptor();

    /* renamed from: f, reason: collision with root package name */
    private static final Name f12755f;

    /* renamed from: g, reason: collision with root package name */
    private static final List<ModuleDescriptor> f12756g;

    /* renamed from: h, reason: collision with root package name */
    private static final List<ModuleDescriptor> f12757h;

    /* renamed from: i, reason: collision with root package name */
    private static final Set<ModuleDescriptor> f12758i;

    /* renamed from: j, reason: collision with root package name */
    private static final KotlinBuiltIns f12759j;

    static {
        List<ModuleDescriptor> j10;
        List<ModuleDescriptor> j11;
        Set<ModuleDescriptor> e10;
        Name i10 = Name.i(ErrorEntity.ERROR_MODULE.b());
        za.k.d(i10, "special(ErrorEntity.ERROR_MODULE.debugText)");
        f12755f = i10;
        j10 = r.j();
        f12756g = j10;
        j11 = r.j();
        f12757h = j11;
        e10 = s0.e();
        f12758i = e10;
        f12759j = DefaultBuiltIns.f15215h.a();
    }

    private ErrorModuleDescriptor() {
    }

    @Override // pb.ModuleDescriptor
    public boolean E0(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "targetModule");
        return false;
    }

    @Override // pb.ModuleDescriptor
    public PackageViewDescriptor H(FqName fqName) {
        za.k.e(fqName, "fqName");
        throw new IllegalStateException("Should not be called!");
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        za.k.e(declarationDescriptorVisitor, "visitor");
        return null;
    }

    public Name P() {
        return f12755f;
    }

    @Override // pb.DeclarationDescriptor
    public DeclarationDescriptor a() {
        return this;
    }

    @Override // pb.DeclarationDescriptor
    public DeclarationDescriptor b() {
        return null;
    }

    @Override // pb.Named
    public Name getName() {
        return P();
    }

    @Override // qb.a
    public qb.g i() {
        return qb.g.f17195b.b();
    }

    @Override // pb.ModuleDescriptor
    public <T> T k0(ModuleCapability<T> moduleCapability) {
        za.k.e(moduleCapability, "capability");
        return null;
    }

    @Override // pb.ModuleDescriptor
    public KotlinBuiltIns t() {
        return f12759j;
    }

    @Override // pb.ModuleDescriptor
    public Collection<FqName> v(FqName fqName, ya.l<? super Name, Boolean> lVar) {
        List j10;
        za.k.e(fqName, "fqName");
        za.k.e(lVar, "nameFilter");
        j10 = r.j();
        return j10;
    }

    @Override // pb.ModuleDescriptor
    public List<ModuleDescriptor> y0() {
        return f12757h;
    }
}
