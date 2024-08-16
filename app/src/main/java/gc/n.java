package gc;

import ac.PossiblyExternalAnnotationDescriptor;
import cc.LazyJavaAnnotationDescriptor;
import cc.LazyJavaTypeParameterDescriptor;
import gd.g0;
import gd.s1;
import gd.u1;
import java.util.List;
import mb.KotlinBuiltIns;
import oc.FqNameUnsafe;
import pb.ClassDescriptor;
import pb.ValueParameterDescriptor;
import qb.AnnotationDescriptor;
import yb.AnnotationQualifierApplicabilityType;
import yb.AnnotationTypeQualifierResolver;
import yb.JavaTypeQualifiersByElementType;
import za.DefaultConstructorMarker;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: signatureEnhancement.kt */
/* loaded from: classes2.dex */
public final class n extends AbstractSignatureParts<AnnotationDescriptor> {

    /* renamed from: a, reason: collision with root package name */
    private final qb.a f11726a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f11727b;

    /* renamed from: c, reason: collision with root package name */
    private final bc.g f11728c;

    /* renamed from: d, reason: collision with root package name */
    private final AnnotationQualifierApplicabilityType f11729d;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f11730e;

    public /* synthetic */ n(qb.a aVar, boolean z10, bc.g gVar, AnnotationQualifierApplicabilityType annotationQualifierApplicabilityType, boolean z11, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(aVar, z10, gVar, annotationQualifierApplicabilityType, (i10 & 16) != 0 ? false : z11);
    }

    @Override // gc.AbstractSignatureParts
    public boolean A(kd.i iVar) {
        za.k.e(iVar, "<this>");
        return ((g0) iVar).Z0() instanceof g;
    }

    @Override // gc.AbstractSignatureParts
    /* renamed from: D, reason: merged with bridge method [inline-methods] */
    public AnnotationTypeQualifierResolver h() {
        return this.f11728c.a().a();
    }

    @Override // gc.AbstractSignatureParts
    /* renamed from: E, reason: merged with bridge method [inline-methods] */
    public g0 p(kd.i iVar) {
        za.k.e(iVar, "<this>");
        return u1.a((g0) iVar);
    }

    @Override // gc.AbstractSignatureParts
    /* renamed from: F, reason: merged with bridge method [inline-methods] */
    public boolean r(AnnotationDescriptor annotationDescriptor) {
        za.k.e(annotationDescriptor, "<this>");
        return ((annotationDescriptor instanceof PossiblyExternalAnnotationDescriptor) && ((PossiblyExternalAnnotationDescriptor) annotationDescriptor).h()) || ((annotationDescriptor instanceof LazyJavaAnnotationDescriptor) && !o() && (((LazyJavaAnnotationDescriptor) annotationDescriptor).k() || l() == AnnotationQualifierApplicabilityType.TYPE_PARAMETER_BOUNDS));
    }

    @Override // gc.AbstractSignatureParts
    /* renamed from: G, reason: merged with bridge method [inline-methods] */
    public kd.r v() {
        return hd.q.f12241a;
    }

    @Override // gc.AbstractSignatureParts
    public Iterable<AnnotationDescriptor> i(kd.i iVar) {
        za.k.e(iVar, "<this>");
        return ((g0) iVar).i();
    }

    @Override // gc.AbstractSignatureParts
    public Iterable<AnnotationDescriptor> k() {
        List j10;
        qb.g i10;
        qb.a aVar = this.f11726a;
        if (aVar != null && (i10 = aVar.i()) != null) {
            return i10;
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // gc.AbstractSignatureParts
    public AnnotationQualifierApplicabilityType l() {
        return this.f11729d;
    }

    @Override // gc.AbstractSignatureParts
    public JavaTypeQualifiersByElementType m() {
        return this.f11728c.b();
    }

    @Override // gc.AbstractSignatureParts
    public boolean n() {
        qb.a aVar = this.f11726a;
        return (aVar instanceof ValueParameterDescriptor) && ((ValueParameterDescriptor) aVar).q0() != null;
    }

    @Override // gc.AbstractSignatureParts
    public boolean o() {
        return this.f11728c.a().q().c();
    }

    @Override // gc.AbstractSignatureParts
    public FqNameUnsafe s(kd.i iVar) {
        za.k.e(iVar, "<this>");
        ClassDescriptor f10 = s1.f((g0) iVar);
        if (f10 != null) {
            return sc.e.m(f10);
        }
        return null;
    }

    @Override // gc.AbstractSignatureParts
    public boolean u() {
        return this.f11730e;
    }

    @Override // gc.AbstractSignatureParts
    public boolean w(kd.i iVar) {
        za.k.e(iVar, "<this>");
        return KotlinBuiltIns.d0((g0) iVar);
    }

    @Override // gc.AbstractSignatureParts
    public boolean x() {
        return this.f11727b;
    }

    @Override // gc.AbstractSignatureParts
    public boolean y(kd.i iVar, kd.i iVar2) {
        za.k.e(iVar, "<this>");
        za.k.e(iVar2, "other");
        return this.f11728c.a().k().d((g0) iVar, (g0) iVar2);
    }

    @Override // gc.AbstractSignatureParts
    public boolean z(kd.o oVar) {
        za.k.e(oVar, "<this>");
        return oVar instanceof LazyJavaTypeParameterDescriptor;
    }

    public n(qb.a aVar, boolean z10, bc.g gVar, AnnotationQualifierApplicabilityType annotationQualifierApplicabilityType, boolean z11) {
        za.k.e(gVar, "containerContext");
        za.k.e(annotationQualifierApplicabilityType, "containerApplicabilityType");
        this.f11726a = aVar;
        this.f11727b = z10;
        this.f11728c = gVar;
        this.f11729d = annotationQualifierApplicabilityType;
        this.f11730e = z11;
    }
}
