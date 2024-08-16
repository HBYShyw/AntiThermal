package gd;

import java.util.List;
import pb.ClassifierDescriptor;
import pb.TypeParameterDescriptor;
import za.DefaultConstructorMarker;

/* compiled from: TypeSubstitution.kt */
/* loaded from: classes2.dex */
public final class e0 extends n1 {

    /* renamed from: c, reason: collision with root package name */
    private final TypeParameterDescriptor[] f11756c;

    /* renamed from: d, reason: collision with root package name */
    private final TypeProjection[] f11757d;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f11758e;

    public /* synthetic */ e0(TypeParameterDescriptor[] typeParameterDescriptorArr, TypeProjection[] typeProjectionArr, boolean z10, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(typeParameterDescriptorArr, typeProjectionArr, (i10 & 4) != 0 ? false : z10);
    }

    @Override // gd.n1
    public boolean b() {
        return this.f11758e;
    }

    @Override // gd.n1
    public TypeProjection e(g0 g0Var) {
        za.k.e(g0Var, "key");
        ClassifierDescriptor v7 = g0Var.W0().v();
        TypeParameterDescriptor typeParameterDescriptor = v7 instanceof TypeParameterDescriptor ? (TypeParameterDescriptor) v7 : null;
        if (typeParameterDescriptor == null) {
            return null;
        }
        int j10 = typeParameterDescriptor.j();
        TypeParameterDescriptor[] typeParameterDescriptorArr = this.f11756c;
        if (j10 >= typeParameterDescriptorArr.length || !za.k.a(typeParameterDescriptorArr[j10].n(), typeParameterDescriptor.n())) {
            return null;
        }
        return this.f11757d[j10];
    }

    @Override // gd.n1
    public boolean f() {
        return this.f11757d.length == 0;
    }

    public final TypeProjection[] i() {
        return this.f11757d;
    }

    public final TypeParameterDescriptor[] j() {
        return this.f11756c;
    }

    public e0(TypeParameterDescriptor[] typeParameterDescriptorArr, TypeProjection[] typeProjectionArr, boolean z10) {
        za.k.e(typeParameterDescriptorArr, "parameters");
        za.k.e(typeProjectionArr, "arguments");
        this.f11756c = typeParameterDescriptorArr;
        this.f11757d = typeProjectionArr;
        this.f11758e = z10;
        int length = typeParameterDescriptorArr.length;
        int length2 = typeProjectionArr.length;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public e0(List<? extends TypeParameterDescriptor> list, List<? extends TypeProjection> list2) {
        this((TypeParameterDescriptor[]) list.toArray(new TypeParameterDescriptor[0]), (TypeProjection[]) list2.toArray(new TypeProjection[0]), false, 4, null);
        za.k.e(list, "parameters");
        za.k.e(list2, "argumentsList");
    }
}
