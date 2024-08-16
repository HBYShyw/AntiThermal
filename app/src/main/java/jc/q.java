package jc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import qc.a;
import qc.d;
import qc.i;
import qc.j;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class q extends i.d<q> {

    /* renamed from: y, reason: collision with root package name */
    private static final q f13721y;

    /* renamed from: z, reason: collision with root package name */
    public static qc.s<q> f13722z = new a();

    /* renamed from: g, reason: collision with root package name */
    private final qc.d f13723g;

    /* renamed from: h, reason: collision with root package name */
    private int f13724h;

    /* renamed from: i, reason: collision with root package name */
    private List<b> f13725i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f13726j;

    /* renamed from: k, reason: collision with root package name */
    private int f13727k;

    /* renamed from: l, reason: collision with root package name */
    private q f13728l;

    /* renamed from: m, reason: collision with root package name */
    private int f13729m;

    /* renamed from: n, reason: collision with root package name */
    private int f13730n;

    /* renamed from: o, reason: collision with root package name */
    private int f13731o;

    /* renamed from: p, reason: collision with root package name */
    private int f13732p;

    /* renamed from: q, reason: collision with root package name */
    private int f13733q;

    /* renamed from: r, reason: collision with root package name */
    private q f13734r;

    /* renamed from: s, reason: collision with root package name */
    private int f13735s;

    /* renamed from: t, reason: collision with root package name */
    private q f13736t;

    /* renamed from: u, reason: collision with root package name */
    private int f13737u;

    /* renamed from: v, reason: collision with root package name */
    private int f13738v;

    /* renamed from: w, reason: collision with root package name */
    private byte f13739w;

    /* renamed from: x, reason: collision with root package name */
    private int f13740x;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<q> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public q a(qc.e eVar, qc.g gVar) {
            return new q(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class c extends i.c<q, c> {

        /* renamed from: h, reason: collision with root package name */
        private int f13761h;

        /* renamed from: j, reason: collision with root package name */
        private boolean f13763j;

        /* renamed from: k, reason: collision with root package name */
        private int f13764k;

        /* renamed from: m, reason: collision with root package name */
        private int f13766m;

        /* renamed from: n, reason: collision with root package name */
        private int f13767n;

        /* renamed from: o, reason: collision with root package name */
        private int f13768o;

        /* renamed from: p, reason: collision with root package name */
        private int f13769p;

        /* renamed from: q, reason: collision with root package name */
        private int f13770q;

        /* renamed from: s, reason: collision with root package name */
        private int f13772s;

        /* renamed from: u, reason: collision with root package name */
        private int f13774u;

        /* renamed from: v, reason: collision with root package name */
        private int f13775v;

        /* renamed from: i, reason: collision with root package name */
        private List<b> f13762i = Collections.emptyList();

        /* renamed from: l, reason: collision with root package name */
        private q f13765l = q.S();

        /* renamed from: r, reason: collision with root package name */
        private q f13771r = q.S();

        /* renamed from: t, reason: collision with root package name */
        private q f13773t = q.S();

        private c() {
            s();
        }

        static /* synthetic */ c l() {
            return q();
        }

        private static c q() {
            return new c();
        }

        private void r() {
            if ((this.f13761h & 1) != 1) {
                this.f13762i = new ArrayList(this.f13762i);
                this.f13761h |= 1;
            }
        }

        private void s() {
        }

        public c A(int i10) {
            this.f13761h |= 8192;
            this.f13775v = i10;
            return this;
        }

        public c B(int i10) {
            this.f13761h |= 4;
            this.f13764k = i10;
            return this;
        }

        public c C(int i10) {
            this.f13761h |= 16;
            this.f13766m = i10;
            return this;
        }

        public c D(boolean z10) {
            this.f13761h |= 2;
            this.f13763j = z10;
            return this;
        }

        public c E(int i10) {
            this.f13761h |= 1024;
            this.f13772s = i10;
            return this;
        }

        public c F(int i10) {
            this.f13761h |= 256;
            this.f13770q = i10;
            return this;
        }

        public c G(int i10) {
            this.f13761h |= 64;
            this.f13768o = i10;
            return this;
        }

        public c H(int i10) {
            this.f13761h |= 128;
            this.f13769p = i10;
            return this;
        }

        @Override // qc.q.a
        /* renamed from: n, reason: merged with bridge method [inline-methods] */
        public q build() {
            q o10 = o();
            if (o10.isInitialized()) {
                return o10;
            }
            throw a.AbstractC0092a.c(o10);
        }

        public q o() {
            q qVar = new q(this);
            int i10 = this.f13761h;
            if ((i10 & 1) == 1) {
                this.f13762i = Collections.unmodifiableList(this.f13762i);
                this.f13761h &= -2;
            }
            qVar.f13725i = this.f13762i;
            int i11 = (i10 & 2) != 2 ? 0 : 1;
            qVar.f13726j = this.f13763j;
            if ((i10 & 4) == 4) {
                i11 |= 2;
            }
            qVar.f13727k = this.f13764k;
            if ((i10 & 8) == 8) {
                i11 |= 4;
            }
            qVar.f13728l = this.f13765l;
            if ((i10 & 16) == 16) {
                i11 |= 8;
            }
            qVar.f13729m = this.f13766m;
            if ((i10 & 32) == 32) {
                i11 |= 16;
            }
            qVar.f13730n = this.f13767n;
            if ((i10 & 64) == 64) {
                i11 |= 32;
            }
            qVar.f13731o = this.f13768o;
            if ((i10 & 128) == 128) {
                i11 |= 64;
            }
            qVar.f13732p = this.f13769p;
            if ((i10 & 256) == 256) {
                i11 |= 128;
            }
            qVar.f13733q = this.f13770q;
            if ((i10 & 512) == 512) {
                i11 |= 256;
            }
            qVar.f13734r = this.f13771r;
            if ((i10 & 1024) == 1024) {
                i11 |= 512;
            }
            qVar.f13735s = this.f13772s;
            if ((i10 & 2048) == 2048) {
                i11 |= 1024;
            }
            qVar.f13736t = this.f13773t;
            if ((i10 & 4096) == 4096) {
                i11 |= 2048;
            }
            qVar.f13737u = this.f13774u;
            if ((i10 & 8192) == 8192) {
                i11 |= 4096;
            }
            qVar.f13738v = this.f13775v;
            qVar.f13724h = i11;
            return qVar;
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public c d() {
            return q().f(o());
        }

        public c t(q qVar) {
            if ((this.f13761h & 2048) == 2048 && this.f13773t != q.S()) {
                this.f13773t = q.t0(this.f13773t).f(qVar).o();
            } else {
                this.f13773t = qVar;
            }
            this.f13761h |= 2048;
            return this;
        }

        public c u(q qVar) {
            if ((this.f13761h & 8) == 8 && this.f13765l != q.S()) {
                this.f13765l = q.t0(this.f13765l).f(qVar).o();
            } else {
                this.f13765l = qVar;
            }
            this.f13761h |= 8;
            return this;
        }

        @Override // qc.i.b
        /* renamed from: v, reason: merged with bridge method [inline-methods] */
        public c f(q qVar) {
            if (qVar == q.S()) {
                return this;
            }
            if (!qVar.f13725i.isEmpty()) {
                if (this.f13762i.isEmpty()) {
                    this.f13762i = qVar.f13725i;
                    this.f13761h &= -2;
                } else {
                    r();
                    this.f13762i.addAll(qVar.f13725i);
                }
            }
            if (qVar.l0()) {
                D(qVar.Y());
            }
            if (qVar.i0()) {
                B(qVar.V());
            }
            if (qVar.j0()) {
                u(qVar.W());
            }
            if (qVar.k0()) {
                C(qVar.X());
            }
            if (qVar.g0()) {
                z(qVar.R());
            }
            if (qVar.p0()) {
                G(qVar.c0());
            }
            if (qVar.q0()) {
                H(qVar.d0());
            }
            if (qVar.o0()) {
                F(qVar.b0());
            }
            if (qVar.m0()) {
                x(qVar.Z());
            }
            if (qVar.n0()) {
                E(qVar.a0());
            }
            if (qVar.e0()) {
                t(qVar.M());
            }
            if (qVar.f0()) {
                y(qVar.N());
            }
            if (qVar.h0()) {
                A(qVar.U());
            }
            k(qVar);
            g(e().d(qVar.f13723g));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: w, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public c m(qc.e eVar, qc.g gVar) {
            q qVar = null;
            try {
                try {
                    q a10 = q.f13722z.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (qVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                q qVar2 = (q) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    qVar = qVar2;
                    if (qVar != null) {
                        f(qVar);
                    }
                    throw th;
                }
            }
        }

        public c x(q qVar) {
            if ((this.f13761h & 512) == 512 && this.f13771r != q.S()) {
                this.f13771r = q.t0(this.f13771r).f(qVar).o();
            } else {
                this.f13771r = qVar;
            }
            this.f13761h |= 512;
            return this;
        }

        public c y(int i10) {
            this.f13761h |= 4096;
            this.f13774u = i10;
            return this;
        }

        public c z(int i10) {
            this.f13761h |= 32;
            this.f13767n = i10;
            return this;
        }
    }

    static {
        q qVar = new q(true);
        f13721y = qVar;
        qVar.r0();
    }

    public static q S() {
        return f13721y;
    }

    private void r0() {
        this.f13725i = Collections.emptyList();
        this.f13726j = false;
        this.f13727k = 0;
        this.f13728l = S();
        this.f13729m = 0;
        this.f13730n = 0;
        this.f13731o = 0;
        this.f13732p = 0;
        this.f13733q = 0;
        this.f13734r = S();
        this.f13735s = 0;
        this.f13736t = S();
        this.f13737u = 0;
        this.f13738v = 0;
    }

    public static c s0() {
        return c.l();
    }

    public static c t0(q qVar) {
        return s0().f(qVar);
    }

    public q M() {
        return this.f13736t;
    }

    public int N() {
        return this.f13737u;
    }

    public b O(int i10) {
        return this.f13725i.get(i10);
    }

    public int P() {
        return this.f13725i.size();
    }

    public List<b> Q() {
        return this.f13725i;
    }

    public int R() {
        return this.f13730n;
    }

    @Override // qc.r
    /* renamed from: T, reason: merged with bridge method [inline-methods] */
    public q getDefaultInstanceForType() {
        return f13721y;
    }

    public int U() {
        return this.f13738v;
    }

    public int V() {
        return this.f13727k;
    }

    public q W() {
        return this.f13728l;
    }

    public int X() {
        return this.f13729m;
    }

    public boolean Y() {
        return this.f13726j;
    }

    public q Z() {
        return this.f13734r;
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        i.d<MessageType>.a t7 = t();
        if ((this.f13724h & 4096) == 4096) {
            fVar.a0(1, this.f13738v);
        }
        for (int i10 = 0; i10 < this.f13725i.size(); i10++) {
            fVar.d0(2, this.f13725i.get(i10));
        }
        if ((this.f13724h & 1) == 1) {
            fVar.L(3, this.f13726j);
        }
        if ((this.f13724h & 2) == 2) {
            fVar.a0(4, this.f13727k);
        }
        if ((this.f13724h & 4) == 4) {
            fVar.d0(5, this.f13728l);
        }
        if ((this.f13724h & 16) == 16) {
            fVar.a0(6, this.f13730n);
        }
        if ((this.f13724h & 32) == 32) {
            fVar.a0(7, this.f13731o);
        }
        if ((this.f13724h & 8) == 8) {
            fVar.a0(8, this.f13729m);
        }
        if ((this.f13724h & 64) == 64) {
            fVar.a0(9, this.f13732p);
        }
        if ((this.f13724h & 256) == 256) {
            fVar.d0(10, this.f13734r);
        }
        if ((this.f13724h & 512) == 512) {
            fVar.a0(11, this.f13735s);
        }
        if ((this.f13724h & 128) == 128) {
            fVar.a0(12, this.f13733q);
        }
        if ((this.f13724h & 1024) == 1024) {
            fVar.d0(13, this.f13736t);
        }
        if ((this.f13724h & 2048) == 2048) {
            fVar.a0(14, this.f13737u);
        }
        t7.a(200, fVar);
        fVar.i0(this.f13723g);
    }

    public int a0() {
        return this.f13735s;
    }

    public int b0() {
        return this.f13733q;
    }

    public int c0() {
        return this.f13731o;
    }

    public int d0() {
        return this.f13732p;
    }

    public boolean e0() {
        return (this.f13724h & 1024) == 1024;
    }

    public boolean f0() {
        return (this.f13724h & 2048) == 2048;
    }

    public boolean g0() {
        return (this.f13724h & 16) == 16;
    }

    @Override // qc.i, qc.q
    public qc.s<q> getParserForType() {
        return f13722z;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13740x;
        if (i10 != -1) {
            return i10;
        }
        int o10 = (this.f13724h & 4096) == 4096 ? qc.f.o(1, this.f13738v) + 0 : 0;
        for (int i11 = 0; i11 < this.f13725i.size(); i11++) {
            o10 += qc.f.s(2, this.f13725i.get(i11));
        }
        if ((this.f13724h & 1) == 1) {
            o10 += qc.f.a(3, this.f13726j);
        }
        if ((this.f13724h & 2) == 2) {
            o10 += qc.f.o(4, this.f13727k);
        }
        if ((this.f13724h & 4) == 4) {
            o10 += qc.f.s(5, this.f13728l);
        }
        if ((this.f13724h & 16) == 16) {
            o10 += qc.f.o(6, this.f13730n);
        }
        if ((this.f13724h & 32) == 32) {
            o10 += qc.f.o(7, this.f13731o);
        }
        if ((this.f13724h & 8) == 8) {
            o10 += qc.f.o(8, this.f13729m);
        }
        if ((this.f13724h & 64) == 64) {
            o10 += qc.f.o(9, this.f13732p);
        }
        if ((this.f13724h & 256) == 256) {
            o10 += qc.f.s(10, this.f13734r);
        }
        if ((this.f13724h & 512) == 512) {
            o10 += qc.f.o(11, this.f13735s);
        }
        if ((this.f13724h & 128) == 128) {
            o10 += qc.f.o(12, this.f13733q);
        }
        if ((this.f13724h & 1024) == 1024) {
            o10 += qc.f.s(13, this.f13736t);
        }
        if ((this.f13724h & 2048) == 2048) {
            o10 += qc.f.o(14, this.f13737u);
        }
        int o11 = o10 + o() + this.f13723g.size();
        this.f13740x = o11;
        return o11;
    }

    public boolean h0() {
        return (this.f13724h & 4096) == 4096;
    }

    public boolean i0() {
        return (this.f13724h & 2) == 2;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13739w;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        for (int i10 = 0; i10 < P(); i10++) {
            if (!O(i10).isInitialized()) {
                this.f13739w = (byte) 0;
                return false;
            }
        }
        if (j0() && !W().isInitialized()) {
            this.f13739w = (byte) 0;
            return false;
        }
        if (m0() && !Z().isInitialized()) {
            this.f13739w = (byte) 0;
            return false;
        }
        if (e0() && !M().isInitialized()) {
            this.f13739w = (byte) 0;
            return false;
        }
        if (!n()) {
            this.f13739w = (byte) 0;
            return false;
        }
        this.f13739w = (byte) 1;
        return true;
    }

    public boolean j0() {
        return (this.f13724h & 4) == 4;
    }

    public boolean k0() {
        return (this.f13724h & 8) == 8;
    }

    public boolean l0() {
        return (this.f13724h & 1) == 1;
    }

    public boolean m0() {
        return (this.f13724h & 256) == 256;
    }

    public boolean n0() {
        return (this.f13724h & 512) == 512;
    }

    public boolean o0() {
        return (this.f13724h & 128) == 128;
    }

    public boolean p0() {
        return (this.f13724h & 32) == 32;
    }

    public boolean q0() {
        return (this.f13724h & 64) == 64;
    }

    @Override // qc.q
    /* renamed from: u0, reason: merged with bridge method [inline-methods] */
    public c newBuilderForType() {
        return s0();
    }

    @Override // qc.q
    /* renamed from: v0, reason: merged with bridge method [inline-methods] */
    public c toBuilder() {
        return t0(this);
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends qc.i implements qc.r {

        /* renamed from: m, reason: collision with root package name */
        private static final b f13741m;

        /* renamed from: n, reason: collision with root package name */
        public static qc.s<b> f13742n = new a();

        /* renamed from: f, reason: collision with root package name */
        private final qc.d f13743f;

        /* renamed from: g, reason: collision with root package name */
        private int f13744g;

        /* renamed from: h, reason: collision with root package name */
        private c f13745h;

        /* renamed from: i, reason: collision with root package name */
        private q f13746i;

        /* renamed from: j, reason: collision with root package name */
        private int f13747j;

        /* renamed from: k, reason: collision with root package name */
        private byte f13748k;

        /* renamed from: l, reason: collision with root package name */
        private int f13749l;

        /* compiled from: ProtoBuf.java */
        /* loaded from: classes2.dex */
        static class a extends qc.b<b> {
            a() {
            }

            @Override // qc.s
            /* renamed from: m, reason: merged with bridge method [inline-methods] */
            public b a(qc.e eVar, qc.g gVar) {
                return new b(eVar, gVar);
            }
        }

        /* compiled from: ProtoBuf.java */
        /* renamed from: jc.q$b$b, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0067b extends i.b<b, C0067b> implements qc.r {

            /* renamed from: f, reason: collision with root package name */
            private int f13750f;

            /* renamed from: g, reason: collision with root package name */
            private c f13751g = c.INV;

            /* renamed from: h, reason: collision with root package name */
            private q f13752h = q.S();

            /* renamed from: i, reason: collision with root package name */
            private int f13753i;

            private C0067b() {
                n();
            }

            static /* synthetic */ C0067b h() {
                return l();
            }

            private static C0067b l() {
                return new C0067b();
            }

            private void n() {
            }

            @Override // qc.q.a
            /* renamed from: i, reason: merged with bridge method [inline-methods] */
            public b build() {
                b j10 = j();
                if (j10.isInitialized()) {
                    return j10;
                }
                throw a.AbstractC0092a.c(j10);
            }

            public b j() {
                b bVar = new b(this);
                int i10 = this.f13750f;
                int i11 = (i10 & 1) != 1 ? 0 : 1;
                bVar.f13745h = this.f13751g;
                if ((i10 & 2) == 2) {
                    i11 |= 2;
                }
                bVar.f13746i = this.f13752h;
                if ((i10 & 4) == 4) {
                    i11 |= 4;
                }
                bVar.f13747j = this.f13753i;
                bVar.f13744g = i11;
                return bVar;
            }

            @Override // qc.i.b
            /* renamed from: k, reason: merged with bridge method [inline-methods] */
            public C0067b d() {
                return l().f(j());
            }

            @Override // qc.i.b
            /* renamed from: o, reason: merged with bridge method [inline-methods] */
            public C0067b f(b bVar) {
                if (bVar == b.r()) {
                    return this;
                }
                if (bVar.v()) {
                    r(bVar.s());
                }
                if (bVar.w()) {
                    q(bVar.t());
                }
                if (bVar.x()) {
                    s(bVar.u());
                }
                g(e().d(bVar.f13743f));
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
            @Override // qc.a.AbstractC0092a, qc.q.a
            /* renamed from: p, reason: merged with bridge method [inline-methods] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public C0067b m(qc.e eVar, qc.g gVar) {
                b bVar = null;
                try {
                    try {
                        b a10 = b.f13742n.a(eVar, gVar);
                        if (a10 != null) {
                            f(a10);
                        }
                        return this;
                    } catch (Throwable th) {
                        th = th;
                        if (bVar != null) {
                        }
                        throw th;
                    }
                } catch (qc.k e10) {
                    b bVar2 = (b) e10.a();
                    try {
                        throw e10;
                    } catch (Throwable th2) {
                        th = th2;
                        bVar = bVar2;
                        if (bVar != null) {
                            f(bVar);
                        }
                        throw th;
                    }
                }
            }

            public C0067b q(q qVar) {
                if ((this.f13750f & 2) == 2 && this.f13752h != q.S()) {
                    this.f13752h = q.t0(this.f13752h).f(qVar).o();
                } else {
                    this.f13752h = qVar;
                }
                this.f13750f |= 2;
                return this;
            }

            public C0067b r(c cVar) {
                Objects.requireNonNull(cVar);
                this.f13750f |= 1;
                this.f13751g = cVar;
                return this;
            }

            public C0067b s(int i10) {
                this.f13750f |= 4;
                this.f13753i = i10;
                return this;
            }
        }

        /* compiled from: ProtoBuf.java */
        /* loaded from: classes2.dex */
        public enum c implements j.a {
            IN(0, 0),
            OUT(1, 1),
            INV(2, 2),
            STAR(3, 3);


            /* renamed from: j, reason: collision with root package name */
            private static j.b<c> f13758j = new a();

            /* renamed from: e, reason: collision with root package name */
            private final int f13760e;

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
                this.f13760e = i11;
            }

            public static c a(int i10) {
                if (i10 == 0) {
                    return IN;
                }
                if (i10 == 1) {
                    return OUT;
                }
                if (i10 == 2) {
                    return INV;
                }
                if (i10 != 3) {
                    return null;
                }
                return STAR;
            }

            @Override // qc.j.a
            public final int getNumber() {
                return this.f13760e;
            }
        }

        static {
            b bVar = new b(true);
            f13741m = bVar;
            bVar.y();
        }

        public static C0067b A(b bVar) {
            return z().f(bVar);
        }

        public static b r() {
            return f13741m;
        }

        private void y() {
            this.f13745h = c.INV;
            this.f13746i = q.S();
            this.f13747j = 0;
        }

        public static C0067b z() {
            return C0067b.h();
        }

        @Override // qc.q
        /* renamed from: B, reason: merged with bridge method [inline-methods] */
        public C0067b newBuilderForType() {
            return z();
        }

        @Override // qc.q
        /* renamed from: C, reason: merged with bridge method [inline-methods] */
        public C0067b toBuilder() {
            return A(this);
        }

        @Override // qc.q
        public void a(qc.f fVar) {
            getSerializedSize();
            if ((this.f13744g & 1) == 1) {
                fVar.S(1, this.f13745h.getNumber());
            }
            if ((this.f13744g & 2) == 2) {
                fVar.d0(2, this.f13746i);
            }
            if ((this.f13744g & 4) == 4) {
                fVar.a0(3, this.f13747j);
            }
            fVar.i0(this.f13743f);
        }

        @Override // qc.i, qc.q
        public qc.s<b> getParserForType() {
            return f13742n;
        }

        @Override // qc.q
        public int getSerializedSize() {
            int i10 = this.f13749l;
            if (i10 != -1) {
                return i10;
            }
            int h10 = (this.f13744g & 1) == 1 ? 0 + qc.f.h(1, this.f13745h.getNumber()) : 0;
            if ((this.f13744g & 2) == 2) {
                h10 += qc.f.s(2, this.f13746i);
            }
            if ((this.f13744g & 4) == 4) {
                h10 += qc.f.o(3, this.f13747j);
            }
            int size = h10 + this.f13743f.size();
            this.f13749l = size;
            return size;
        }

        @Override // qc.r
        public final boolean isInitialized() {
            byte b10 = this.f13748k;
            if (b10 == 1) {
                return true;
            }
            if (b10 == 0) {
                return false;
            }
            if (w() && !t().isInitialized()) {
                this.f13748k = (byte) 0;
                return false;
            }
            this.f13748k = (byte) 1;
            return true;
        }

        public c s() {
            return this.f13745h;
        }

        public q t() {
            return this.f13746i;
        }

        public int u() {
            return this.f13747j;
        }

        public boolean v() {
            return (this.f13744g & 1) == 1;
        }

        public boolean w() {
            return (this.f13744g & 2) == 2;
        }

        public boolean x() {
            return (this.f13744g & 4) == 4;
        }

        private b(i.b bVar) {
            super(bVar);
            this.f13748k = (byte) -1;
            this.f13749l = -1;
            this.f13743f = bVar.e();
        }

        private b(boolean z10) {
            this.f13748k = (byte) -1;
            this.f13749l = -1;
            this.f13743f = qc.d.f17259e;
        }

        private b(qc.e eVar, qc.g gVar) {
            this.f13748k = (byte) -1;
            this.f13749l = -1;
            y();
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
                                    int n10 = eVar.n();
                                    c a10 = c.a(n10);
                                    if (a10 == null) {
                                        J.o0(K);
                                        J.o0(n10);
                                    } else {
                                        this.f13744g |= 1;
                                        this.f13745h = a10;
                                    }
                                } else if (K == 18) {
                                    c builder = (this.f13744g & 2) == 2 ? this.f13746i.toBuilder() : null;
                                    q qVar = (q) eVar.u(q.f13722z, gVar);
                                    this.f13746i = qVar;
                                    if (builder != null) {
                                        builder.f(qVar);
                                        this.f13746i = builder.o();
                                    }
                                    this.f13744g |= 2;
                                } else if (K != 24) {
                                    if (!j(eVar, J, gVar, K)) {
                                    }
                                } else {
                                    this.f13744g |= 4;
                                    this.f13747j = eVar.s();
                                }
                            }
                            z10 = true;
                        } catch (IOException e10) {
                            throw new qc.k(e10.getMessage()).i(this);
                        }
                    } catch (qc.k e11) {
                        throw e11.i(this);
                    }
                } catch (Throwable th) {
                    try {
                        J.I();
                    } catch (IOException unused) {
                    } catch (Throwable th2) {
                        this.f13743f = q10.v();
                        throw th2;
                    }
                    this.f13743f = q10.v();
                    g();
                    throw th;
                }
            }
            try {
                J.I();
            } catch (IOException unused2) {
            } catch (Throwable th3) {
                this.f13743f = q10.v();
                throw th3;
            }
            this.f13743f = q10.v();
            g();
        }
    }

    private q(i.c<q, ?> cVar) {
        super(cVar);
        this.f13739w = (byte) -1;
        this.f13740x = -1;
        this.f13723g = cVar.e();
    }

    private q(boolean z10) {
        this.f13739w = (byte) -1;
        this.f13740x = -1;
        this.f13723g = qc.d.f17259e;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:5:0x001d. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    private q(qc.e eVar, qc.g gVar) {
        c builder;
        this.f13739w = (byte) -1;
        this.f13740x = -1;
        r0();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        boolean z11 = false;
        while (!z10) {
            try {
                try {
                    int K = eVar.K();
                    switch (K) {
                        case 0:
                            z10 = true;
                        case 8:
                            this.f13724h |= 4096;
                            this.f13738v = eVar.s();
                        case 18:
                            if (!(z11 & true)) {
                                this.f13725i = new ArrayList();
                                z11 |= true;
                            }
                            this.f13725i.add(eVar.u(b.f13742n, gVar));
                        case 24:
                            this.f13724h |= 1;
                            this.f13726j = eVar.k();
                        case 32:
                            this.f13724h |= 2;
                            this.f13727k = eVar.s();
                        case 42:
                            builder = (this.f13724h & 4) == 4 ? this.f13728l.toBuilder() : null;
                            q qVar = (q) eVar.u(f13722z, gVar);
                            this.f13728l = qVar;
                            if (builder != null) {
                                builder.f(qVar);
                                this.f13728l = builder.o();
                            }
                            this.f13724h |= 4;
                        case 48:
                            this.f13724h |= 16;
                            this.f13730n = eVar.s();
                        case 56:
                            this.f13724h |= 32;
                            this.f13731o = eVar.s();
                        case 64:
                            this.f13724h |= 8;
                            this.f13729m = eVar.s();
                        case 72:
                            this.f13724h |= 64;
                            this.f13732p = eVar.s();
                        case 82:
                            builder = (this.f13724h & 256) == 256 ? this.f13734r.toBuilder() : null;
                            q qVar2 = (q) eVar.u(f13722z, gVar);
                            this.f13734r = qVar2;
                            if (builder != null) {
                                builder.f(qVar2);
                                this.f13734r = builder.o();
                            }
                            this.f13724h |= 256;
                        case 88:
                            this.f13724h |= 512;
                            this.f13735s = eVar.s();
                        case 96:
                            this.f13724h |= 128;
                            this.f13733q = eVar.s();
                        case 106:
                            builder = (this.f13724h & 1024) == 1024 ? this.f13736t.toBuilder() : null;
                            q qVar3 = (q) eVar.u(f13722z, gVar);
                            this.f13736t = qVar3;
                            if (builder != null) {
                                builder.f(qVar3);
                                this.f13736t = builder.o();
                            }
                            this.f13724h |= 1024;
                        case 112:
                            this.f13724h |= 2048;
                            this.f13737u = eVar.s();
                        default:
                            if (!j(eVar, J, gVar, K)) {
                                z10 = true;
                            }
                    }
                } catch (qc.k e10) {
                    throw e10.i(this);
                } catch (IOException e11) {
                    throw new qc.k(e11.getMessage()).i(this);
                }
            } catch (Throwable th) {
                if (z11 & true) {
                    this.f13725i = Collections.unmodifiableList(this.f13725i);
                }
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13723g = q10.v();
                    throw th2;
                }
                this.f13723g = q10.v();
                g();
                throw th;
            }
        }
        if (z11 & true) {
            this.f13725i = Collections.unmodifiableList(this.f13725i);
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13723g = q10.v();
            throw th3;
        }
        this.f13723g = q10.v();
        g();
    }
}
