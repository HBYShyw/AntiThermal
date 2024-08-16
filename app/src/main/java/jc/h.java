package jc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import jc.q;
import qc.a;
import qc.d;
import qc.i;
import qc.j;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class h extends qc.i implements qc.r {

    /* renamed from: q, reason: collision with root package name */
    private static final h f13543q;

    /* renamed from: r, reason: collision with root package name */
    public static qc.s<h> f13544r = new a();

    /* renamed from: f, reason: collision with root package name */
    private final qc.d f13545f;

    /* renamed from: g, reason: collision with root package name */
    private int f13546g;

    /* renamed from: h, reason: collision with root package name */
    private int f13547h;

    /* renamed from: i, reason: collision with root package name */
    private int f13548i;

    /* renamed from: j, reason: collision with root package name */
    private c f13549j;

    /* renamed from: k, reason: collision with root package name */
    private q f13550k;

    /* renamed from: l, reason: collision with root package name */
    private int f13551l;

    /* renamed from: m, reason: collision with root package name */
    private List<h> f13552m;

    /* renamed from: n, reason: collision with root package name */
    private List<h> f13553n;

    /* renamed from: o, reason: collision with root package name */
    private byte f13554o;

    /* renamed from: p, reason: collision with root package name */
    private int f13555p;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<h> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public h a(qc.e eVar, qc.g gVar) {
            return new h(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.b<h, b> implements qc.r {

        /* renamed from: f, reason: collision with root package name */
        private int f13556f;

        /* renamed from: g, reason: collision with root package name */
        private int f13557g;

        /* renamed from: h, reason: collision with root package name */
        private int f13558h;

        /* renamed from: k, reason: collision with root package name */
        private int f13561k;

        /* renamed from: i, reason: collision with root package name */
        private c f13559i = c.TRUE;

        /* renamed from: j, reason: collision with root package name */
        private q f13560j = q.S();

        /* renamed from: l, reason: collision with root package name */
        private List<h> f13562l = Collections.emptyList();

        /* renamed from: m, reason: collision with root package name */
        private List<h> f13563m = Collections.emptyList();

        private b() {
            p();
        }

        static /* synthetic */ b h() {
            return l();
        }

        private static b l() {
            return new b();
        }

        private void n() {
            if ((this.f13556f & 32) != 32) {
                this.f13562l = new ArrayList(this.f13562l);
                this.f13556f |= 32;
            }
        }

        private void o() {
            if ((this.f13556f & 64) != 64) {
                this.f13563m = new ArrayList(this.f13563m);
                this.f13556f |= 64;
            }
        }

        private void p() {
        }

        @Override // qc.q.a
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public h build() {
            h j10 = j();
            if (j10.isInitialized()) {
                return j10;
            }
            throw a.AbstractC0092a.c(j10);
        }

        public h j() {
            h hVar = new h(this);
            int i10 = this.f13556f;
            int i11 = (i10 & 1) != 1 ? 0 : 1;
            hVar.f13547h = this.f13557g;
            if ((i10 & 2) == 2) {
                i11 |= 2;
            }
            hVar.f13548i = this.f13558h;
            if ((i10 & 4) == 4) {
                i11 |= 4;
            }
            hVar.f13549j = this.f13559i;
            if ((i10 & 8) == 8) {
                i11 |= 8;
            }
            hVar.f13550k = this.f13560j;
            if ((i10 & 16) == 16) {
                i11 |= 16;
            }
            hVar.f13551l = this.f13561k;
            if ((this.f13556f & 32) == 32) {
                this.f13562l = Collections.unmodifiableList(this.f13562l);
                this.f13556f &= -33;
            }
            hVar.f13552m = this.f13562l;
            if ((this.f13556f & 64) == 64) {
                this.f13563m = Collections.unmodifiableList(this.f13563m);
                this.f13556f &= -65;
            }
            hVar.f13553n = this.f13563m;
            hVar.f13546g = i11;
            return hVar;
        }

        @Override // qc.i.b
        /* renamed from: k, reason: merged with bridge method [inline-methods] */
        public b d() {
            return l().f(j());
        }

        @Override // qc.i.b
        /* renamed from: q, reason: merged with bridge method [inline-methods] */
        public b f(h hVar) {
            if (hVar == h.A()) {
                return this;
            }
            if (hVar.I()) {
                u(hVar.B());
            }
            if (hVar.L()) {
                w(hVar.G());
            }
            if (hVar.H()) {
                t(hVar.z());
            }
            if (hVar.J()) {
                s(hVar.C());
            }
            if (hVar.K()) {
                v(hVar.D());
            }
            if (!hVar.f13552m.isEmpty()) {
                if (this.f13562l.isEmpty()) {
                    this.f13562l = hVar.f13552m;
                    this.f13556f &= -33;
                } else {
                    n();
                    this.f13562l.addAll(hVar.f13552m);
                }
            }
            if (!hVar.f13553n.isEmpty()) {
                if (this.f13563m.isEmpty()) {
                    this.f13563m = hVar.f13553n;
                    this.f13556f &= -65;
                } else {
                    o();
                    this.f13563m.addAll(hVar.f13553n);
                }
            }
            g(e().d(hVar.f13545f));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: r, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            h hVar = null;
            try {
                try {
                    h a10 = h.f13544r.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (hVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                h hVar2 = (h) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    hVar = hVar2;
                    if (hVar != null) {
                        f(hVar);
                    }
                    throw th;
                }
            }
        }

        public b s(q qVar) {
            if ((this.f13556f & 8) == 8 && this.f13560j != q.S()) {
                this.f13560j = q.t0(this.f13560j).f(qVar).o();
            } else {
                this.f13560j = qVar;
            }
            this.f13556f |= 8;
            return this;
        }

        public b t(c cVar) {
            Objects.requireNonNull(cVar);
            this.f13556f |= 4;
            this.f13559i = cVar;
            return this;
        }

        public b u(int i10) {
            this.f13556f |= 1;
            this.f13557g = i10;
            return this;
        }

        public b v(int i10) {
            this.f13556f |= 16;
            this.f13561k = i10;
            return this;
        }

        public b w(int i10) {
            this.f13556f |= 2;
            this.f13558h = i10;
            return this;
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public enum c implements j.a {
        TRUE(0, 0),
        FALSE(1, 1),
        NULL(2, 2);


        /* renamed from: i, reason: collision with root package name */
        private static j.b<c> f13567i = new a();

        /* renamed from: e, reason: collision with root package name */
        private final int f13569e;

        /* compiled from: ProtoBuf.java */
        /* loaded from: classes2.dex */
        static class a implements j.b<c> {
            a() {
            }

            @Override // qc.j.b
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public c findValueByNumber(int i10) {
                return c.a(i10);
            }
        }

        c(int i10, int i11) {
            this.f13569e = i11;
        }

        public static c a(int i10) {
            if (i10 == 0) {
                return TRUE;
            }
            if (i10 == 1) {
                return FALSE;
            }
            if (i10 != 2) {
                return null;
            }
            return NULL;
        }

        @Override // qc.j.a
        public final int getNumber() {
            return this.f13569e;
        }
    }

    static {
        h hVar = new h(true);
        f13543q = hVar;
        hVar.M();
    }

    public static h A() {
        return f13543q;
    }

    private void M() {
        this.f13547h = 0;
        this.f13548i = 0;
        this.f13549j = c.TRUE;
        this.f13550k = q.S();
        this.f13551l = 0;
        this.f13552m = Collections.emptyList();
        this.f13553n = Collections.emptyList();
    }

    public static b N() {
        return b.h();
    }

    public static b O(h hVar) {
        return N().f(hVar);
    }

    public int B() {
        return this.f13547h;
    }

    public q C() {
        return this.f13550k;
    }

    public int D() {
        return this.f13551l;
    }

    public h E(int i10) {
        return this.f13553n.get(i10);
    }

    public int F() {
        return this.f13553n.size();
    }

    public int G() {
        return this.f13548i;
    }

    public boolean H() {
        return (this.f13546g & 4) == 4;
    }

    public boolean I() {
        return (this.f13546g & 1) == 1;
    }

    public boolean J() {
        return (this.f13546g & 8) == 8;
    }

    public boolean K() {
        return (this.f13546g & 16) == 16;
    }

    public boolean L() {
        return (this.f13546g & 2) == 2;
    }

    @Override // qc.q
    /* renamed from: P, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return N();
    }

    @Override // qc.q
    /* renamed from: Q, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return O(this);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        if ((this.f13546g & 1) == 1) {
            fVar.a0(1, this.f13547h);
        }
        if ((this.f13546g & 2) == 2) {
            fVar.a0(2, this.f13548i);
        }
        if ((this.f13546g & 4) == 4) {
            fVar.S(3, this.f13549j.getNumber());
        }
        if ((this.f13546g & 8) == 8) {
            fVar.d0(4, this.f13550k);
        }
        if ((this.f13546g & 16) == 16) {
            fVar.a0(5, this.f13551l);
        }
        for (int i10 = 0; i10 < this.f13552m.size(); i10++) {
            fVar.d0(6, this.f13552m.get(i10));
        }
        for (int i11 = 0; i11 < this.f13553n.size(); i11++) {
            fVar.d0(7, this.f13553n.get(i11));
        }
        fVar.i0(this.f13545f);
    }

    @Override // qc.i, qc.q
    public qc.s<h> getParserForType() {
        return f13544r;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13555p;
        if (i10 != -1) {
            return i10;
        }
        int o10 = (this.f13546g & 1) == 1 ? qc.f.o(1, this.f13547h) + 0 : 0;
        if ((this.f13546g & 2) == 2) {
            o10 += qc.f.o(2, this.f13548i);
        }
        if ((this.f13546g & 4) == 4) {
            o10 += qc.f.h(3, this.f13549j.getNumber());
        }
        if ((this.f13546g & 8) == 8) {
            o10 += qc.f.s(4, this.f13550k);
        }
        if ((this.f13546g & 16) == 16) {
            o10 += qc.f.o(5, this.f13551l);
        }
        for (int i11 = 0; i11 < this.f13552m.size(); i11++) {
            o10 += qc.f.s(6, this.f13552m.get(i11));
        }
        for (int i12 = 0; i12 < this.f13553n.size(); i12++) {
            o10 += qc.f.s(7, this.f13553n.get(i12));
        }
        int size = o10 + this.f13545f.size();
        this.f13555p = size;
        return size;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13554o;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        if (J() && !C().isInitialized()) {
            this.f13554o = (byte) 0;
            return false;
        }
        for (int i10 = 0; i10 < y(); i10++) {
            if (!x(i10).isInitialized()) {
                this.f13554o = (byte) 0;
                return false;
            }
        }
        for (int i11 = 0; i11 < F(); i11++) {
            if (!E(i11).isInitialized()) {
                this.f13554o = (byte) 0;
                return false;
            }
        }
        this.f13554o = (byte) 1;
        return true;
    }

    public h x(int i10) {
        return this.f13552m.get(i10);
    }

    public int y() {
        return this.f13552m.size();
    }

    public c z() {
        return this.f13549j;
    }

    private h(i.b bVar) {
        super(bVar);
        this.f13554o = (byte) -1;
        this.f13555p = -1;
        this.f13545f = bVar.e();
    }

    private h(boolean z10) {
        this.f13554o = (byte) -1;
        this.f13555p = -1;
        this.f13545f = qc.d.f17259e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private h(qc.e eVar, qc.g gVar) {
        this.f13554o = (byte) -1;
        this.f13555p = -1;
        M();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        int i10 = 0;
        while (!z10) {
            try {
                try {
                    int K = eVar.K();
                    if (K != 0) {
                        if (K == 8) {
                            this.f13546g |= 1;
                            this.f13547h = eVar.s();
                        } else if (K == 16) {
                            this.f13546g |= 2;
                            this.f13548i = eVar.s();
                        } else if (K == 24) {
                            int n10 = eVar.n();
                            c a10 = c.a(n10);
                            if (a10 == null) {
                                J.o0(K);
                                J.o0(n10);
                            } else {
                                this.f13546g |= 4;
                                this.f13549j = a10;
                            }
                        } else if (K == 34) {
                            q.c builder = (this.f13546g & 8) == 8 ? this.f13550k.toBuilder() : null;
                            q qVar = (q) eVar.u(q.f13722z, gVar);
                            this.f13550k = qVar;
                            if (builder != null) {
                                builder.f(qVar);
                                this.f13550k = builder.o();
                            }
                            this.f13546g |= 8;
                        } else if (K == 40) {
                            this.f13546g |= 16;
                            this.f13551l = eVar.s();
                        } else if (K == 50) {
                            if ((i10 & 32) != 32) {
                                this.f13552m = new ArrayList();
                                i10 |= 32;
                            }
                            this.f13552m.add(eVar.u(f13544r, gVar));
                        } else if (K != 58) {
                            if (!j(eVar, J, gVar, K)) {
                            }
                        } else {
                            if ((i10 & 64) != 64) {
                                this.f13553n = new ArrayList();
                                i10 |= 64;
                            }
                            this.f13553n.add(eVar.u(f13544r, gVar));
                        }
                    }
                    z10 = true;
                } catch (qc.k e10) {
                    throw e10.i(this);
                } catch (IOException e11) {
                    throw new qc.k(e11.getMessage()).i(this);
                }
            } catch (Throwable th) {
                if ((i10 & 32) == 32) {
                    this.f13552m = Collections.unmodifiableList(this.f13552m);
                }
                if ((i10 & 64) == 64) {
                    this.f13553n = Collections.unmodifiableList(this.f13553n);
                }
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13545f = q10.v();
                    throw th2;
                }
                this.f13545f = q10.v();
                g();
                throw th;
            }
        }
        if ((i10 & 32) == 32) {
            this.f13552m = Collections.unmodifiableList(this.f13552m);
        }
        if ((i10 & 64) == 64) {
            this.f13553n = Collections.unmodifiableList(this.f13553n);
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13545f = q10.v();
            throw th3;
        }
        this.f13545f = q10.v();
        g();
    }
}
