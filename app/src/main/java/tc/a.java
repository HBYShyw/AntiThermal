package tc;

import gd.TypeProjection;
import gd.c1;
import gd.o0;
import hd.g;
import id.ErrorScopeKind;
import id.ErrorUtils;
import java.util.List;
import kotlin.collections.r;
import za.DefaultConstructorMarker;
import za.k;
import zc.h;

/* compiled from: CapturedTypeConstructor.kt */
/* loaded from: classes2.dex */
public final class a extends o0 implements kd.d {

    /* renamed from: f, reason: collision with root package name */
    private final TypeProjection f18707f;

    /* renamed from: g, reason: collision with root package name */
    private final b f18708g;

    /* renamed from: h, reason: collision with root package name */
    private final boolean f18709h;

    /* renamed from: i, reason: collision with root package name */
    private final c1 f18710i;

    public /* synthetic */ a(TypeProjection typeProjection, b bVar, boolean z10, c1 c1Var, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(typeProjection, (i10 & 2) != 0 ? new c(typeProjection) : bVar, (i10 & 4) != 0 ? false : z10, (i10 & 8) != 0 ? c1.f11749f.h() : c1Var);
    }

    @Override // gd.g0
    public List<TypeProjection> U0() {
        List<TypeProjection> j10;
        j10 = r.j();
        return j10;
    }

    @Override // gd.g0
    public c1 V0() {
        return this.f18710i;
    }

    @Override // gd.g0
    public boolean X0() {
        return this.f18709h;
    }

    @Override // gd.v1
    /* renamed from: e1 */
    public o0 c1(c1 c1Var) {
        k.e(c1Var, "newAttributes");
        return new a(this.f18707f, W0(), X0(), c1Var);
    }

    @Override // gd.g0
    /* renamed from: f1, reason: merged with bridge method [inline-methods] */
    public b W0() {
        return this.f18708g;
    }

    @Override // gd.o0
    /* renamed from: g1, reason: merged with bridge method [inline-methods] */
    public a a1(boolean z10) {
        return z10 == X0() ? this : new a(this.f18707f, W0(), z10, V0());
    }

    @Override // gd.v1
    /* renamed from: h1, reason: merged with bridge method [inline-methods] */
    public a g1(g gVar) {
        k.e(gVar, "kotlinTypeRefiner");
        TypeProjection u7 = this.f18707f.u(gVar);
        k.d(u7, "typeProjection.refine(kotlinTypeRefiner)");
        return new a(u7, W0(), X0(), V0());
    }

    @Override // gd.o0
    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Captured(");
        sb2.append(this.f18707f);
        sb2.append(')');
        sb2.append(X0() ? "?" : "");
        return sb2.toString();
    }

    @Override // gd.g0
    public h u() {
        return ErrorUtils.a(ErrorScopeKind.CAPTURED_TYPE_SCOPE, true, new String[0]);
    }

    public a(TypeProjection typeProjection, b bVar, boolean z10, c1 c1Var) {
        k.e(typeProjection, "typeProjection");
        k.e(bVar, "constructor");
        k.e(c1Var, "attributes");
        this.f18707f = typeProjection;
        this.f18708g = bVar;
        this.f18709h = z10;
        this.f18710i = c1Var;
    }
}
