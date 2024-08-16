package gd;

import pb.TypeParameterDescriptor;
import za.Lambda;

/* compiled from: StarProjectionImpl.kt */
/* loaded from: classes2.dex */
public final class u0 extends TypeProjectionBase {

    /* renamed from: a, reason: collision with root package name */
    private final TypeParameterDescriptor f11892a;

    /* renamed from: b, reason: collision with root package name */
    private final ma.h f11893b;

    /* compiled from: StarProjectionImpl.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<g0> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke() {
            return v0.b(u0.this.f11892a);
        }
    }

    public u0(TypeParameterDescriptor typeParameterDescriptor) {
        ma.h a10;
        za.k.e(typeParameterDescriptor, "typeParameter");
        this.f11892a = typeParameterDescriptor;
        a10 = ma.j.a(ma.l.PUBLICATION, new a());
        this.f11893b = a10;
    }

    private final g0 d() {
        return (g0) this.f11893b.getValue();
    }

    @Override // gd.TypeProjection
    public Variance a() {
        return Variance.OUT_VARIANCE;
    }

    @Override // gd.TypeProjection
    public boolean b() {
        return true;
    }

    @Override // gd.TypeProjection
    public g0 getType() {
        return d();
    }

    @Override // gd.TypeProjection
    public TypeProjection u(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        return this;
    }
}
