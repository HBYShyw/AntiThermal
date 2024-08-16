package jc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import qc.a;
import qc.d;
import qc.i;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class t extends qc.i implements qc.r {

    /* renamed from: l, reason: collision with root package name */
    private static final t f13827l;

    /* renamed from: m, reason: collision with root package name */
    public static qc.s<t> f13828m = new a();

    /* renamed from: f, reason: collision with root package name */
    private final qc.d f13829f;

    /* renamed from: g, reason: collision with root package name */
    private int f13830g;

    /* renamed from: h, reason: collision with root package name */
    private List<q> f13831h;

    /* renamed from: i, reason: collision with root package name */
    private int f13832i;

    /* renamed from: j, reason: collision with root package name */
    private byte f13833j;

    /* renamed from: k, reason: collision with root package name */
    private int f13834k;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<t> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public t a(qc.e eVar, qc.g gVar) {
            return new t(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.b<t, b> implements qc.r {

        /* renamed from: f, reason: collision with root package name */
        private int f13835f;

        /* renamed from: g, reason: collision with root package name */
        private List<q> f13836g = Collections.emptyList();

        /* renamed from: h, reason: collision with root package name */
        private int f13837h = -1;

        private b() {
            o();
        }

        static /* synthetic */ b h() {
            return l();
        }

        private static b l() {
            return new b();
        }

        private void n() {
            if ((this.f13835f & 1) != 1) {
                this.f13836g = new ArrayList(this.f13836g);
                this.f13835f |= 1;
            }
        }

        private void o() {
        }

        @Override // qc.q.a
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public t build() {
            t j10 = j();
            if (j10.isInitialized()) {
                return j10;
            }
            throw a.AbstractC0092a.c(j10);
        }

        public t j() {
            t tVar = new t(this);
            int i10 = this.f13835f;
            if ((i10 & 1) == 1) {
                this.f13836g = Collections.unmodifiableList(this.f13836g);
                this.f13835f &= -2;
            }
            tVar.f13831h = this.f13836g;
            int i11 = (i10 & 2) != 2 ? 0 : 1;
            tVar.f13832i = this.f13837h;
            tVar.f13830g = i11;
            return tVar;
        }

        @Override // qc.i.b
        /* renamed from: k, reason: merged with bridge method [inline-methods] */
        public b d() {
            return l().f(j());
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b f(t tVar) {
            if (tVar == t.r()) {
                return this;
            }
            if (!tVar.f13831h.isEmpty()) {
                if (this.f13836g.isEmpty()) {
                    this.f13836g = tVar.f13831h;
                    this.f13835f &= -2;
                } else {
                    n();
                    this.f13836g.addAll(tVar.f13831h);
                }
            }
            if (tVar.w()) {
                r(tVar.s());
            }
            g(e().d(tVar.f13829f));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: q, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            t tVar = null;
            try {
                try {
                    t a10 = t.f13828m.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (tVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                t tVar2 = (t) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    tVar = tVar2;
                    if (tVar != null) {
                        f(tVar);
                    }
                    throw th;
                }
            }
        }

        public b r(int i10) {
            this.f13835f |= 2;
            this.f13837h = i10;
            return this;
        }
    }

    static {
        t tVar = new t(true);
        f13827l = tVar;
        tVar.x();
    }

    public static t r() {
        return f13827l;
    }

    private void x() {
        this.f13831h = Collections.emptyList();
        this.f13832i = -1;
    }

    public static b y() {
        return b.h();
    }

    public static b z(t tVar) {
        return y().f(tVar);
    }

    @Override // qc.q
    /* renamed from: A, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return y();
    }

    @Override // qc.q
    /* renamed from: B, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return z(this);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        for (int i10 = 0; i10 < this.f13831h.size(); i10++) {
            fVar.d0(1, this.f13831h.get(i10));
        }
        if ((this.f13830g & 1) == 1) {
            fVar.a0(2, this.f13832i);
        }
        fVar.i0(this.f13829f);
    }

    @Override // qc.i, qc.q
    public qc.s<t> getParserForType() {
        return f13828m;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13834k;
        if (i10 != -1) {
            return i10;
        }
        int i11 = 0;
        for (int i12 = 0; i12 < this.f13831h.size(); i12++) {
            i11 += qc.f.s(1, this.f13831h.get(i12));
        }
        if ((this.f13830g & 1) == 1) {
            i11 += qc.f.o(2, this.f13832i);
        }
        int size = i11 + this.f13829f.size();
        this.f13834k = size;
        return size;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13833j;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        for (int i10 = 0; i10 < u(); i10++) {
            if (!t(i10).isInitialized()) {
                this.f13833j = (byte) 0;
                return false;
            }
        }
        this.f13833j = (byte) 1;
        return true;
    }

    public int s() {
        return this.f13832i;
    }

    public q t(int i10) {
        return this.f13831h.get(i10);
    }

    public int u() {
        return this.f13831h.size();
    }

    public List<q> v() {
        return this.f13831h;
    }

    public boolean w() {
        return (this.f13830g & 1) == 1;
    }

    private t(i.b bVar) {
        super(bVar);
        this.f13833j = (byte) -1;
        this.f13834k = -1;
        this.f13829f = bVar.e();
    }

    private t(boolean z10) {
        this.f13833j = (byte) -1;
        this.f13834k = -1;
        this.f13829f = qc.d.f17259e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private t(qc.e eVar, qc.g gVar) {
        this.f13833j = (byte) -1;
        this.f13834k = -1;
        x();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        boolean z11 = false;
        while (!z10) {
            try {
                try {
                    try {
                        int K = eVar.K();
                        if (K != 0) {
                            if (K == 10) {
                                if (!(z11 & true)) {
                                    this.f13831h = new ArrayList();
                                    z11 |= true;
                                }
                                this.f13831h.add(eVar.u(q.f13722z, gVar));
                            } else if (K != 16) {
                                if (!j(eVar, J, gVar, K)) {
                                }
                            } else {
                                this.f13830g |= 1;
                                this.f13832i = eVar.s();
                            }
                        }
                        z10 = true;
                    } catch (qc.k e10) {
                        throw e10.i(this);
                    }
                } catch (IOException e11) {
                    throw new qc.k(e11.getMessage()).i(this);
                }
            } catch (Throwable th) {
                if (z11 & true) {
                    this.f13831h = Collections.unmodifiableList(this.f13831h);
                }
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13829f = q10.v();
                    throw th2;
                }
                this.f13829f = q10.v();
                g();
                throw th;
            }
        }
        if (z11 & true) {
            this.f13831h = Collections.unmodifiableList(this.f13831h);
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13829f = q10.v();
            throw th3;
        }
        this.f13829f = q10.v();
        g();
    }
}
