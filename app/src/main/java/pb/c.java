package pb;

import fd.StorageManager;
import gd.TypeConstructor;
import gd.Variance;
import java.util.List;
import oc.Name;

/* compiled from: typeParameterUtils.kt */
/* loaded from: classes2.dex */
final class c implements TypeParameterDescriptor {

    /* renamed from: e, reason: collision with root package name */
    private final TypeParameterDescriptor f16672e;

    /* renamed from: f, reason: collision with root package name */
    private final DeclarationDescriptor f16673f;

    /* renamed from: g, reason: collision with root package name */
    private final int f16674g;

    public c(TypeParameterDescriptor typeParameterDescriptor, DeclarationDescriptor declarationDescriptor, int i10) {
        za.k.e(typeParameterDescriptor, "originalDescriptor");
        za.k.e(declarationDescriptor, "declarationDescriptor");
        this.f16672e = typeParameterDescriptor;
        this.f16673f = declarationDescriptor;
        this.f16674g = i10;
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        return (R) this.f16672e.H0(declarationDescriptorVisitor, d10);
    }

    @Override // pb.TypeParameterDescriptor
    public boolean N() {
        return this.f16672e.N();
    }

    @Override // pb.DeclarationDescriptorNonRoot, pb.DeclarationDescriptor
    public DeclarationDescriptor b() {
        return this.f16673f;
    }

    @Override // pb.Named
    public Name getName() {
        return this.f16672e.getName();
    }

    @Override // pb.TypeParameterDescriptor
    public List<gd.g0> getUpperBounds() {
        return this.f16672e.getUpperBounds();
    }

    @Override // qb.a
    public qb.g i() {
        return this.f16672e.i();
    }

    @Override // pb.TypeParameterDescriptor
    public int j() {
        return this.f16674g + this.f16672e.j();
    }

    @Override // pb.TypeParameterDescriptor, pb.ClassifierDescriptor
    public TypeConstructor n() {
        return this.f16672e.n();
    }

    @Override // pb.TypeParameterDescriptor
    public StorageManager o0() {
        return this.f16672e.o0();
    }

    @Override // pb.TypeParameterDescriptor
    public Variance s() {
        return this.f16672e.s();
    }

    @Override // pb.TypeParameterDescriptor
    public boolean t0() {
        return true;
    }

    public String toString() {
        return this.f16672e + "[inner-copy]";
    }

    @Override // pb.ClassifierDescriptor
    public gd.o0 x() {
        return this.f16672e.x();
    }

    @Override // pb.DeclarationDescriptorWithSource
    public SourceElement z() {
        return this.f16672e.z();
    }

    @Override // pb.DeclarationDescriptor
    public TypeParameterDescriptor a() {
        TypeParameterDescriptor a10 = this.f16672e.a();
        za.k.d(a10, "originalDescriptor.original");
        return a10;
    }
}
