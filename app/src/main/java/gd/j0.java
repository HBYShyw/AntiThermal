package gd;

import fd.StorageManager;
import za.Lambda;

/* compiled from: SpecialTypes.kt */
/* loaded from: classes2.dex */
public final class j0 extends x1 {

    /* renamed from: f, reason: collision with root package name */
    private final StorageManager f11833f;

    /* renamed from: g, reason: collision with root package name */
    private final ya.a<g0> f11834g;

    /* renamed from: h, reason: collision with root package name */
    private final fd.i<g0> f11835h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SpecialTypes.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.a<g0> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ hd.g f11836e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ j0 f11837f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(hd.g gVar, j0 j0Var) {
            super(0);
            this.f11836e = gVar;
            this.f11837f = j0Var;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke() {
            return this.f11836e.a((kd.i) this.f11837f.f11834g.invoke());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public j0(StorageManager storageManager, ya.a<? extends g0> aVar) {
        za.k.e(storageManager, "storageManager");
        za.k.e(aVar, "computation");
        this.f11833f = storageManager;
        this.f11834g = aVar;
        this.f11835h = storageManager.g(aVar);
    }

    @Override // gd.x1
    protected g0 a1() {
        return this.f11835h.invoke();
    }

    @Override // gd.x1
    public boolean b1() {
        return this.f11835h.e();
    }

    @Override // gd.g0
    /* renamed from: d1, reason: merged with bridge method [inline-methods] */
    public j0 g1(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        return new j0(this.f11833f, new a(gVar, this));
    }
}
