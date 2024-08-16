package hd;

import gd.TypeProjection;
import gd.c1;
import gd.o0;
import gd.v1;
import id.ErrorScopeKind;
import id.ErrorUtils;
import java.util.List;
import pb.TypeParameterDescriptor;
import za.DefaultConstructorMarker;

/* compiled from: NewCapturedType.kt */
/* loaded from: classes2.dex */
public final class i extends o0 implements kd.d {

    /* renamed from: f, reason: collision with root package name */
    private final kd.b f12217f;

    /* renamed from: g, reason: collision with root package name */
    private final j f12218g;

    /* renamed from: h, reason: collision with root package name */
    private final v1 f12219h;

    /* renamed from: i, reason: collision with root package name */
    private final c1 f12220i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f12221j;

    /* renamed from: k, reason: collision with root package name */
    private final boolean f12222k;

    public /* synthetic */ i(kd.b bVar, j jVar, v1 v1Var, c1 c1Var, boolean z10, boolean z11, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(bVar, jVar, v1Var, (i10 & 8) != 0 ? c1.f11749f.h() : c1Var, (i10 & 16) != 0 ? false : z10, (i10 & 32) != 0 ? false : z11);
    }

    @Override // gd.g0
    public List<TypeProjection> U0() {
        List<TypeProjection> j10;
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // gd.g0
    public c1 V0() {
        return this.f12220i;
    }

    @Override // gd.g0
    public boolean X0() {
        return this.f12221j;
    }

    @Override // gd.v1
    /* renamed from: e1 */
    public o0 c1(c1 c1Var) {
        za.k.e(c1Var, "newAttributes");
        return new i(this.f12217f, W0(), this.f12219h, c1Var, X0(), this.f12222k);
    }

    public final kd.b f1() {
        return this.f12217f;
    }

    @Override // gd.g0
    /* renamed from: g1, reason: merged with bridge method [inline-methods] */
    public j W0() {
        return this.f12218g;
    }

    public final v1 h1() {
        return this.f12219h;
    }

    public final boolean i1() {
        return this.f12222k;
    }

    @Override // gd.o0
    /* renamed from: j1, reason: merged with bridge method [inline-methods] */
    public i a1(boolean z10) {
        return new i(this.f12217f, W0(), this.f12219h, V0(), z10, false, 32, null);
    }

    @Override // gd.v1
    /* renamed from: k1, reason: merged with bridge method [inline-methods] */
    public i g1(g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        kd.b bVar = this.f12217f;
        j u7 = W0().u(gVar);
        v1 v1Var = this.f12219h;
        return new i(bVar, u7, v1Var != null ? gVar.a(v1Var).Z0() : null, V0(), X0(), false, 32, null);
    }

    @Override // gd.g0
    public zc.h u() {
        return ErrorUtils.a(ErrorScopeKind.CAPTURED_TYPE_SCOPE, true, new String[0]);
    }

    public i(kd.b bVar, j jVar, v1 v1Var, c1 c1Var, boolean z10, boolean z11) {
        za.k.e(bVar, "captureStatus");
        za.k.e(jVar, "constructor");
        za.k.e(c1Var, "attributes");
        this.f12217f = bVar;
        this.f12218g = jVar;
        this.f12219h = v1Var;
        this.f12220i = c1Var;
        this.f12221j = z10;
        this.f12222k = z11;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public i(kd.b bVar, v1 v1Var, TypeProjection typeProjection, TypeParameterDescriptor typeParameterDescriptor) {
        this(bVar, new j(typeProjection, null, null, typeParameterDescriptor, 6, null), v1Var, null, false, false, 56, null);
        za.k.e(bVar, "captureStatus");
        za.k.e(typeProjection, "projection");
        za.k.e(typeParameterDescriptor, "typeParameter");
    }
}
