package gd;

import id.ErrorScopeKind;
import id.ErrorUtils;
import java.util.List;
import za.DefaultConstructorMarker;

/* compiled from: StubTypes.kt */
/* loaded from: classes2.dex */
public abstract class e extends o0 {

    /* renamed from: i, reason: collision with root package name */
    public static final a f11752i = new a(null);

    /* renamed from: f, reason: collision with root package name */
    private final hd.n f11753f;

    /* renamed from: g, reason: collision with root package name */
    private final boolean f11754g;

    /* renamed from: h, reason: collision with root package name */
    private final zc.h f11755h;

    /* compiled from: StubTypes.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public e(hd.n nVar, boolean z10) {
        za.k.e(nVar, "originalTypeVariable");
        this.f11753f = nVar;
        this.f11754g = z10;
        this.f11755h = ErrorUtils.b(ErrorScopeKind.STUB_TYPE_SCOPE, nVar.toString());
    }

    @Override // gd.g0
    public List<TypeProjection> U0() {
        List<TypeProjection> j10;
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // gd.g0
    public c1 V0() {
        return c1.f11749f.h();
    }

    @Override // gd.g0
    public boolean X0() {
        return this.f11754g;
    }

    @Override // gd.v1
    /* renamed from: d1 */
    public o0 a1(boolean z10) {
        return z10 == X0() ? this : g1(z10);
    }

    @Override // gd.v1
    /* renamed from: e1 */
    public o0 c1(c1 c1Var) {
        za.k.e(c1Var, "newAttributes");
        return this;
    }

    public final hd.n f1() {
        return this.f11753f;
    }

    public abstract e g1(boolean z10);

    @Override // gd.v1
    /* renamed from: h1, reason: merged with bridge method [inline-methods] */
    public e g1(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        return this;
    }

    @Override // gd.g0
    public zc.h u() {
        return this.f11755h;
    }
}
