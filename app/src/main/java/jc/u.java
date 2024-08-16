package jc;

import java.io.IOException;
import jc.q;
import qc.a;
import qc.d;
import qc.i;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class u extends i.d<u> {

    /* renamed from: q, reason: collision with root package name */
    private static final u f13838q;

    /* renamed from: r, reason: collision with root package name */
    public static qc.s<u> f13839r = new a();

    /* renamed from: g, reason: collision with root package name */
    private final qc.d f13840g;

    /* renamed from: h, reason: collision with root package name */
    private int f13841h;

    /* renamed from: i, reason: collision with root package name */
    private int f13842i;

    /* renamed from: j, reason: collision with root package name */
    private int f13843j;

    /* renamed from: k, reason: collision with root package name */
    private q f13844k;

    /* renamed from: l, reason: collision with root package name */
    private int f13845l;

    /* renamed from: m, reason: collision with root package name */
    private q f13846m;

    /* renamed from: n, reason: collision with root package name */
    private int f13847n;

    /* renamed from: o, reason: collision with root package name */
    private byte f13848o;

    /* renamed from: p, reason: collision with root package name */
    private int f13849p;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<u> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public u a(qc.e eVar, qc.g gVar) {
            return new u(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.c<u, b> {

        /* renamed from: h, reason: collision with root package name */
        private int f13850h;

        /* renamed from: i, reason: collision with root package name */
        private int f13851i;

        /* renamed from: j, reason: collision with root package name */
        private int f13852j;

        /* renamed from: l, reason: collision with root package name */
        private int f13854l;

        /* renamed from: n, reason: collision with root package name */
        private int f13856n;

        /* renamed from: k, reason: collision with root package name */
        private q f13853k = q.S();

        /* renamed from: m, reason: collision with root package name */
        private q f13855m = q.S();

        private b() {
            r();
        }

        static /* synthetic */ b l() {
            return q();
        }

        private static b q() {
            return new b();
        }

        private void r() {
        }

        @Override // qc.q.a
        /* renamed from: n, reason: merged with bridge method [inline-methods] */
        public u build() {
            u o10 = o();
            if (o10.isInitialized()) {
                return o10;
            }
            throw a.AbstractC0092a.c(o10);
        }

        public u o() {
            u uVar = new u(this);
            int i10 = this.f13850h;
            int i11 = (i10 & 1) != 1 ? 0 : 1;
            uVar.f13842i = this.f13851i;
            if ((i10 & 2) == 2) {
                i11 |= 2;
            }
            uVar.f13843j = this.f13852j;
            if ((i10 & 4) == 4) {
                i11 |= 4;
            }
            uVar.f13844k = this.f13853k;
            if ((i10 & 8) == 8) {
                i11 |= 8;
            }
            uVar.f13845l = this.f13854l;
            if ((i10 & 16) == 16) {
                i11 |= 16;
            }
            uVar.f13846m = this.f13855m;
            if ((i10 & 32) == 32) {
                i11 |= 32;
            }
            uVar.f13847n = this.f13856n;
            uVar.f13841h = i11;
            return uVar;
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b d() {
            return q().f(o());
        }

        @Override // qc.i.b
        /* renamed from: s, reason: merged with bridge method [inline-methods] */
        public b f(u uVar) {
            if (uVar == u.D()) {
                return this;
            }
            if (uVar.L()) {
                w(uVar.F());
            }
            if (uVar.M()) {
                x(uVar.G());
            }
            if (uVar.N()) {
                u(uVar.H());
            }
            if (uVar.O()) {
                y(uVar.I());
            }
            if (uVar.P()) {
                v(uVar.J());
            }
            if (uVar.Q()) {
                z(uVar.K());
            }
            k(uVar);
            g(e().d(uVar.f13840g));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: t, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            u uVar = null;
            try {
                try {
                    u a10 = u.f13839r.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (uVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                u uVar2 = (u) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    uVar = uVar2;
                    if (uVar != null) {
                        f(uVar);
                    }
                    throw th;
                }
            }
        }

        public b u(q qVar) {
            if ((this.f13850h & 4) == 4 && this.f13853k != q.S()) {
                this.f13853k = q.t0(this.f13853k).f(qVar).o();
            } else {
                this.f13853k = qVar;
            }
            this.f13850h |= 4;
            return this;
        }

        public b v(q qVar) {
            if ((this.f13850h & 16) == 16 && this.f13855m != q.S()) {
                this.f13855m = q.t0(this.f13855m).f(qVar).o();
            } else {
                this.f13855m = qVar;
            }
            this.f13850h |= 16;
            return this;
        }

        public b w(int i10) {
            this.f13850h |= 1;
            this.f13851i = i10;
            return this;
        }

        public b x(int i10) {
            this.f13850h |= 2;
            this.f13852j = i10;
            return this;
        }

        public b y(int i10) {
            this.f13850h |= 8;
            this.f13854l = i10;
            return this;
        }

        public b z(int i10) {
            this.f13850h |= 32;
            this.f13856n = i10;
            return this;
        }
    }

    static {
        u uVar = new u(true);
        f13838q = uVar;
        uVar.R();
    }

    public static u D() {
        return f13838q;
    }

    private void R() {
        this.f13842i = 0;
        this.f13843j = 0;
        this.f13844k = q.S();
        this.f13845l = 0;
        this.f13846m = q.S();
        this.f13847n = 0;
    }

    public static b S() {
        return b.l();
    }

    public static b T(u uVar) {
        return S().f(uVar);
    }

    @Override // qc.r
    /* renamed from: E, reason: merged with bridge method [inline-methods] */
    public u getDefaultInstanceForType() {
        return f13838q;
    }

    public int F() {
        return this.f13842i;
    }

    public int G() {
        return this.f13843j;
    }

    public q H() {
        return this.f13844k;
    }

    public int I() {
        return this.f13845l;
    }

    public q J() {
        return this.f13846m;
    }

    public int K() {
        return this.f13847n;
    }

    public boolean L() {
        return (this.f13841h & 1) == 1;
    }

    public boolean M() {
        return (this.f13841h & 2) == 2;
    }

    public boolean N() {
        return (this.f13841h & 4) == 4;
    }

    public boolean O() {
        return (this.f13841h & 8) == 8;
    }

    public boolean P() {
        return (this.f13841h & 16) == 16;
    }

    public boolean Q() {
        return (this.f13841h & 32) == 32;
    }

    @Override // qc.q
    /* renamed from: U, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return S();
    }

    @Override // qc.q
    /* renamed from: V, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return T(this);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        i.d<MessageType>.a t7 = t();
        if ((this.f13841h & 1) == 1) {
            fVar.a0(1, this.f13842i);
        }
        if ((this.f13841h & 2) == 2) {
            fVar.a0(2, this.f13843j);
        }
        if ((this.f13841h & 4) == 4) {
            fVar.d0(3, this.f13844k);
        }
        if ((this.f13841h & 16) == 16) {
            fVar.d0(4, this.f13846m);
        }
        if ((this.f13841h & 8) == 8) {
            fVar.a0(5, this.f13845l);
        }
        if ((this.f13841h & 32) == 32) {
            fVar.a0(6, this.f13847n);
        }
        t7.a(200, fVar);
        fVar.i0(this.f13840g);
    }

    @Override // qc.i, qc.q
    public qc.s<u> getParserForType() {
        return f13839r;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13849p;
        if (i10 != -1) {
            return i10;
        }
        int o10 = (this.f13841h & 1) == 1 ? 0 + qc.f.o(1, this.f13842i) : 0;
        if ((this.f13841h & 2) == 2) {
            o10 += qc.f.o(2, this.f13843j);
        }
        if ((this.f13841h & 4) == 4) {
            o10 += qc.f.s(3, this.f13844k);
        }
        if ((this.f13841h & 16) == 16) {
            o10 += qc.f.s(4, this.f13846m);
        }
        if ((this.f13841h & 8) == 8) {
            o10 += qc.f.o(5, this.f13845l);
        }
        if ((this.f13841h & 32) == 32) {
            o10 += qc.f.o(6, this.f13847n);
        }
        int o11 = o10 + o() + this.f13840g.size();
        this.f13849p = o11;
        return o11;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13848o;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        if (!M()) {
            this.f13848o = (byte) 0;
            return false;
        }
        if (N() && !H().isInitialized()) {
            this.f13848o = (byte) 0;
            return false;
        }
        if (P() && !J().isInitialized()) {
            this.f13848o = (byte) 0;
            return false;
        }
        if (!n()) {
            this.f13848o = (byte) 0;
            return false;
        }
        this.f13848o = (byte) 1;
        return true;
    }

    private u(i.c<u, ?> cVar) {
        super(cVar);
        this.f13848o = (byte) -1;
        this.f13849p = -1;
        this.f13840g = cVar.e();
    }

    private u(boolean z10) {
        this.f13848o = (byte) -1;
        this.f13849p = -1;
        this.f13840g = qc.d.f17259e;
    }

    private u(qc.e eVar, qc.g gVar) {
        q.c builder;
        this.f13848o = (byte) -1;
        this.f13849p = -1;
        R();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        while (!z10) {
            try {
                try {
                    int K = eVar.K();
                    if (K != 0) {
                        if (K == 8) {
                            this.f13841h |= 1;
                            this.f13842i = eVar.s();
                        } else if (K != 16) {
                            if (K == 26) {
                                builder = (this.f13841h & 4) == 4 ? this.f13844k.toBuilder() : null;
                                q qVar = (q) eVar.u(q.f13722z, gVar);
                                this.f13844k = qVar;
                                if (builder != null) {
                                    builder.f(qVar);
                                    this.f13844k = builder.o();
                                }
                                this.f13841h |= 4;
                            } else if (K == 34) {
                                builder = (this.f13841h & 16) == 16 ? this.f13846m.toBuilder() : null;
                                q qVar2 = (q) eVar.u(q.f13722z, gVar);
                                this.f13846m = qVar2;
                                if (builder != null) {
                                    builder.f(qVar2);
                                    this.f13846m = builder.o();
                                }
                                this.f13841h |= 16;
                            } else if (K == 40) {
                                this.f13841h |= 8;
                                this.f13845l = eVar.s();
                            } else if (K != 48) {
                                if (!j(eVar, J, gVar, K)) {
                                }
                            } else {
                                this.f13841h |= 32;
                                this.f13847n = eVar.s();
                            }
                        } else {
                            this.f13841h |= 2;
                            this.f13843j = eVar.s();
                        }
                    }
                    z10 = true;
                } catch (qc.k e10) {
                    throw e10.i(this);
                } catch (IOException e11) {
                    throw new qc.k(e11.getMessage()).i(this);
                }
            } catch (Throwable th) {
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13840g = q10.v();
                    throw th2;
                }
                this.f13840g = q10.v();
                g();
                throw th;
            }
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13840g = q10.v();
            throw th3;
        }
        this.f13840g = q10.v();
        g();
    }
}
