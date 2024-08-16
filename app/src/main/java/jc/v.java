package jc;

import java.io.IOException;
import java.util.Objects;
import qc.a;
import qc.d;
import qc.i;
import qc.j;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class v extends qc.i implements qc.r {

    /* renamed from: p, reason: collision with root package name */
    private static final v f13857p;

    /* renamed from: q, reason: collision with root package name */
    public static qc.s<v> f13858q = new a();

    /* renamed from: f, reason: collision with root package name */
    private final qc.d f13859f;

    /* renamed from: g, reason: collision with root package name */
    private int f13860g;

    /* renamed from: h, reason: collision with root package name */
    private int f13861h;

    /* renamed from: i, reason: collision with root package name */
    private int f13862i;

    /* renamed from: j, reason: collision with root package name */
    private c f13863j;

    /* renamed from: k, reason: collision with root package name */
    private int f13864k;

    /* renamed from: l, reason: collision with root package name */
    private int f13865l;

    /* renamed from: m, reason: collision with root package name */
    private d f13866m;

    /* renamed from: n, reason: collision with root package name */
    private byte f13867n;

    /* renamed from: o, reason: collision with root package name */
    private int f13868o;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<v> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public v a(qc.e eVar, qc.g gVar) {
            return new v(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.b<v, b> implements qc.r {

        /* renamed from: f, reason: collision with root package name */
        private int f13869f;

        /* renamed from: g, reason: collision with root package name */
        private int f13870g;

        /* renamed from: h, reason: collision with root package name */
        private int f13871h;

        /* renamed from: j, reason: collision with root package name */
        private int f13873j;

        /* renamed from: k, reason: collision with root package name */
        private int f13874k;

        /* renamed from: i, reason: collision with root package name */
        private c f13872i = c.ERROR;

        /* renamed from: l, reason: collision with root package name */
        private d f13875l = d.LANGUAGE_VERSION;

        private b() {
            n();
        }

        static /* synthetic */ b h() {
            return l();
        }

        private static b l() {
            return new b();
        }

        private void n() {
        }

        @Override // qc.q.a
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public v build() {
            v j10 = j();
            if (j10.isInitialized()) {
                return j10;
            }
            throw a.AbstractC0092a.c(j10);
        }

        public v j() {
            v vVar = new v(this);
            int i10 = this.f13869f;
            int i11 = (i10 & 1) != 1 ? 0 : 1;
            vVar.f13861h = this.f13870g;
            if ((i10 & 2) == 2) {
                i11 |= 2;
            }
            vVar.f13862i = this.f13871h;
            if ((i10 & 4) == 4) {
                i11 |= 4;
            }
            vVar.f13863j = this.f13872i;
            if ((i10 & 8) == 8) {
                i11 |= 8;
            }
            vVar.f13864k = this.f13873j;
            if ((i10 & 16) == 16) {
                i11 |= 16;
            }
            vVar.f13865l = this.f13874k;
            if ((i10 & 32) == 32) {
                i11 |= 32;
            }
            vVar.f13866m = this.f13875l;
            vVar.f13860g = i11;
            return vVar;
        }

        @Override // qc.i.b
        /* renamed from: k, reason: merged with bridge method [inline-methods] */
        public b d() {
            return l().f(j());
        }

        @Override // qc.i.b
        /* renamed from: o, reason: merged with bridge method [inline-methods] */
        public b f(v vVar) {
            if (vVar == v.u()) {
                return this;
            }
            if (vVar.E()) {
                t(vVar.y());
            }
            if (vVar.F()) {
                u(vVar.z());
            }
            if (vVar.C()) {
                r(vVar.w());
            }
            if (vVar.B()) {
                q(vVar.v());
            }
            if (vVar.D()) {
                s(vVar.x());
            }
            if (vVar.G()) {
                v(vVar.A());
            }
            g(e().d(vVar.f13859f));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            v vVar = null;
            try {
                try {
                    v a10 = v.f13858q.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (vVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                v vVar2 = (v) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    vVar = vVar2;
                    if (vVar != null) {
                        f(vVar);
                    }
                    throw th;
                }
            }
        }

        public b q(int i10) {
            this.f13869f |= 8;
            this.f13873j = i10;
            return this;
        }

        public b r(c cVar) {
            Objects.requireNonNull(cVar);
            this.f13869f |= 4;
            this.f13872i = cVar;
            return this;
        }

        public b s(int i10) {
            this.f13869f |= 16;
            this.f13874k = i10;
            return this;
        }

        public b t(int i10) {
            this.f13869f |= 1;
            this.f13870g = i10;
            return this;
        }

        public b u(int i10) {
            this.f13869f |= 2;
            this.f13871h = i10;
            return this;
        }

        public b v(d dVar) {
            Objects.requireNonNull(dVar);
            this.f13869f |= 32;
            this.f13875l = dVar;
            return this;
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public enum c implements j.a {
        WARNING(0, 0),
        ERROR(1, 1),
        HIDDEN(2, 2);


        /* renamed from: i, reason: collision with root package name */
        private static j.b<c> f13879i = new a();

        /* renamed from: e, reason: collision with root package name */
        private final int f13881e;

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
            this.f13881e = i11;
        }

        public static c a(int i10) {
            if (i10 == 0) {
                return WARNING;
            }
            if (i10 == 1) {
                return ERROR;
            }
            if (i10 != 2) {
                return null;
            }
            return HIDDEN;
        }

        @Override // qc.j.a
        public final int getNumber() {
            return this.f13881e;
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public enum d implements j.a {
        LANGUAGE_VERSION(0, 0),
        COMPILER_VERSION(1, 1),
        API_VERSION(2, 2);


        /* renamed from: i, reason: collision with root package name */
        private static j.b<d> f13885i = new a();

        /* renamed from: e, reason: collision with root package name */
        private final int f13887e;

        /* compiled from: ProtoBuf.java */
        /* loaded from: classes2.dex */
        static class a implements j.b<d> {
            a() {
            }

            @Override // qc.j.b
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public d findValueByNumber(int i10) {
                return d.a(i10);
            }
        }

        d(int i10, int i11) {
            this.f13887e = i11;
        }

        public static d a(int i10) {
            if (i10 == 0) {
                return LANGUAGE_VERSION;
            }
            if (i10 == 1) {
                return COMPILER_VERSION;
            }
            if (i10 != 2) {
                return null;
            }
            return API_VERSION;
        }

        @Override // qc.j.a
        public final int getNumber() {
            return this.f13887e;
        }
    }

    static {
        v vVar = new v(true);
        f13857p = vVar;
        vVar.H();
    }

    private void H() {
        this.f13861h = 0;
        this.f13862i = 0;
        this.f13863j = c.ERROR;
        this.f13864k = 0;
        this.f13865l = 0;
        this.f13866m = d.LANGUAGE_VERSION;
    }

    public static b I() {
        return b.h();
    }

    public static b J(v vVar) {
        return I().f(vVar);
    }

    public static v u() {
        return f13857p;
    }

    public d A() {
        return this.f13866m;
    }

    public boolean B() {
        return (this.f13860g & 8) == 8;
    }

    public boolean C() {
        return (this.f13860g & 4) == 4;
    }

    public boolean D() {
        return (this.f13860g & 16) == 16;
    }

    public boolean E() {
        return (this.f13860g & 1) == 1;
    }

    public boolean F() {
        return (this.f13860g & 2) == 2;
    }

    public boolean G() {
        return (this.f13860g & 32) == 32;
    }

    @Override // qc.q
    /* renamed from: K, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return I();
    }

    @Override // qc.q
    /* renamed from: L, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return J(this);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        if ((this.f13860g & 1) == 1) {
            fVar.a0(1, this.f13861h);
        }
        if ((this.f13860g & 2) == 2) {
            fVar.a0(2, this.f13862i);
        }
        if ((this.f13860g & 4) == 4) {
            fVar.S(3, this.f13863j.getNumber());
        }
        if ((this.f13860g & 8) == 8) {
            fVar.a0(4, this.f13864k);
        }
        if ((this.f13860g & 16) == 16) {
            fVar.a0(5, this.f13865l);
        }
        if ((this.f13860g & 32) == 32) {
            fVar.S(6, this.f13866m.getNumber());
        }
        fVar.i0(this.f13859f);
    }

    @Override // qc.i, qc.q
    public qc.s<v> getParserForType() {
        return f13858q;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13868o;
        if (i10 != -1) {
            return i10;
        }
        int o10 = (this.f13860g & 1) == 1 ? 0 + qc.f.o(1, this.f13861h) : 0;
        if ((this.f13860g & 2) == 2) {
            o10 += qc.f.o(2, this.f13862i);
        }
        if ((this.f13860g & 4) == 4) {
            o10 += qc.f.h(3, this.f13863j.getNumber());
        }
        if ((this.f13860g & 8) == 8) {
            o10 += qc.f.o(4, this.f13864k);
        }
        if ((this.f13860g & 16) == 16) {
            o10 += qc.f.o(5, this.f13865l);
        }
        if ((this.f13860g & 32) == 32) {
            o10 += qc.f.h(6, this.f13866m.getNumber());
        }
        int size = o10 + this.f13859f.size();
        this.f13868o = size;
        return size;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13867n;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        this.f13867n = (byte) 1;
        return true;
    }

    public int v() {
        return this.f13864k;
    }

    public c w() {
        return this.f13863j;
    }

    public int x() {
        return this.f13865l;
    }

    public int y() {
        return this.f13861h;
    }

    public int z() {
        return this.f13862i;
    }

    private v(i.b bVar) {
        super(bVar);
        this.f13867n = (byte) -1;
        this.f13868o = -1;
        this.f13859f = bVar.e();
    }

    private v(boolean z10) {
        this.f13867n = (byte) -1;
        this.f13868o = -1;
        this.f13859f = qc.d.f17259e;
    }

    private v(qc.e eVar, qc.g gVar) {
        this.f13867n = (byte) -1;
        this.f13868o = -1;
        H();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        while (!z10) {
            try {
                try {
                    try {
                        int K = eVar.K();
                        if (K != 0) {
                            if (K == 8) {
                                this.f13860g |= 1;
                                this.f13861h = eVar.s();
                            } else if (K == 16) {
                                this.f13860g |= 2;
                                this.f13862i = eVar.s();
                            } else if (K == 24) {
                                int n10 = eVar.n();
                                c a10 = c.a(n10);
                                if (a10 == null) {
                                    J.o0(K);
                                    J.o0(n10);
                                } else {
                                    this.f13860g |= 4;
                                    this.f13863j = a10;
                                }
                            } else if (K == 32) {
                                this.f13860g |= 8;
                                this.f13864k = eVar.s();
                            } else if (K == 40) {
                                this.f13860g |= 16;
                                this.f13865l = eVar.s();
                            } else if (K != 48) {
                                if (!j(eVar, J, gVar, K)) {
                                }
                            } else {
                                int n11 = eVar.n();
                                d a11 = d.a(n11);
                                if (a11 == null) {
                                    J.o0(K);
                                    J.o0(n11);
                                } else {
                                    this.f13860g |= 32;
                                    this.f13866m = a11;
                                }
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
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13859f = q10.v();
                    throw th2;
                }
                this.f13859f = q10.v();
                g();
                throw th;
            }
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13859f = q10.v();
            throw th3;
        }
        this.f13859f = q10.v();
        g();
    }
}
