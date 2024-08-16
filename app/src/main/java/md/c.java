package md;

import gd.g0;
import hd.KotlinTypeChecker;
import pb.TypeParameterDescriptor;
import za.k;

/* compiled from: CapturedTypeApproximation.kt */
/* loaded from: classes2.dex */
final class c {

    /* renamed from: a, reason: collision with root package name */
    private final TypeParameterDescriptor f15459a;

    /* renamed from: b, reason: collision with root package name */
    private final g0 f15460b;

    /* renamed from: c, reason: collision with root package name */
    private final g0 f15461c;

    public c(TypeParameterDescriptor typeParameterDescriptor, g0 g0Var, g0 g0Var2) {
        k.e(typeParameterDescriptor, "typeParameter");
        k.e(g0Var, "inProjection");
        k.e(g0Var2, "outProjection");
        this.f15459a = typeParameterDescriptor;
        this.f15460b = g0Var;
        this.f15461c = g0Var2;
    }

    public final g0 a() {
        return this.f15460b;
    }

    public final g0 b() {
        return this.f15461c;
    }

    public final TypeParameterDescriptor c() {
        return this.f15459a;
    }

    public final boolean d() {
        return KotlinTypeChecker.f12213a.b(this.f15460b, this.f15461c);
    }
}
