package jc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jc.q;
import jc.t;
import jc.w;
import qc.a;
import qc.d;
import qc.i;
import qc.j;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class c extends i.d<c> {
    private static final c O;
    public static qc.s<c> P = new a();
    private int A;
    private int B;
    private q C;
    private int D;
    private List<Integer> E;
    private int F;
    private List<q> G;
    private List<Integer> H;
    private int I;
    private t J;
    private List<Integer> K;
    private w L;
    private byte M;
    private int N;

    /* renamed from: g, reason: collision with root package name */
    private final qc.d f13437g;

    /* renamed from: h, reason: collision with root package name */
    private int f13438h;

    /* renamed from: i, reason: collision with root package name */
    private int f13439i;

    /* renamed from: j, reason: collision with root package name */
    private int f13440j;

    /* renamed from: k, reason: collision with root package name */
    private int f13441k;

    /* renamed from: l, reason: collision with root package name */
    private List<s> f13442l;

    /* renamed from: m, reason: collision with root package name */
    private List<q> f13443m;

    /* renamed from: n, reason: collision with root package name */
    private List<Integer> f13444n;

    /* renamed from: o, reason: collision with root package name */
    private int f13445o;

    /* renamed from: p, reason: collision with root package name */
    private List<Integer> f13446p;

    /* renamed from: q, reason: collision with root package name */
    private int f13447q;

    /* renamed from: r, reason: collision with root package name */
    private List<q> f13448r;

    /* renamed from: s, reason: collision with root package name */
    private List<Integer> f13449s;

    /* renamed from: t, reason: collision with root package name */
    private int f13450t;

    /* renamed from: u, reason: collision with root package name */
    private List<d> f13451u;

    /* renamed from: v, reason: collision with root package name */
    private List<i> f13452v;

    /* renamed from: w, reason: collision with root package name */
    private List<n> f13453w;

    /* renamed from: x, reason: collision with root package name */
    private List<r> f13454x;

    /* renamed from: y, reason: collision with root package name */
    private List<g> f13455y;

    /* renamed from: z, reason: collision with root package name */
    private List<Integer> f13456z;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<c> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public c a(qc.e eVar, qc.g gVar) {
            return new c(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.c<c, b> {

        /* renamed from: h, reason: collision with root package name */
        private int f13457h;

        /* renamed from: j, reason: collision with root package name */
        private int f13459j;

        /* renamed from: k, reason: collision with root package name */
        private int f13460k;

        /* renamed from: x, reason: collision with root package name */
        private int f13473x;

        /* renamed from: z, reason: collision with root package name */
        private int f13475z;

        /* renamed from: i, reason: collision with root package name */
        private int f13458i = 6;

        /* renamed from: l, reason: collision with root package name */
        private List<s> f13461l = Collections.emptyList();

        /* renamed from: m, reason: collision with root package name */
        private List<q> f13462m = Collections.emptyList();

        /* renamed from: n, reason: collision with root package name */
        private List<Integer> f13463n = Collections.emptyList();

        /* renamed from: o, reason: collision with root package name */
        private List<Integer> f13464o = Collections.emptyList();

        /* renamed from: p, reason: collision with root package name */
        private List<q> f13465p = Collections.emptyList();

        /* renamed from: q, reason: collision with root package name */
        private List<Integer> f13466q = Collections.emptyList();

        /* renamed from: r, reason: collision with root package name */
        private List<d> f13467r = Collections.emptyList();

        /* renamed from: s, reason: collision with root package name */
        private List<i> f13468s = Collections.emptyList();

        /* renamed from: t, reason: collision with root package name */
        private List<n> f13469t = Collections.emptyList();

        /* renamed from: u, reason: collision with root package name */
        private List<r> f13470u = Collections.emptyList();

        /* renamed from: v, reason: collision with root package name */
        private List<g> f13471v = Collections.emptyList();

        /* renamed from: w, reason: collision with root package name */
        private List<Integer> f13472w = Collections.emptyList();

        /* renamed from: y, reason: collision with root package name */
        private q f13474y = q.S();
        private List<Integer> A = Collections.emptyList();
        private List<q> B = Collections.emptyList();
        private List<Integer> C = Collections.emptyList();
        private t D = t.r();
        private List<Integer> E = Collections.emptyList();
        private w F = w.p();

        private b() {
            H();
        }

        private void A() {
            if ((this.f13457h & 2048) != 2048) {
                this.f13469t = new ArrayList(this.f13469t);
                this.f13457h |= 2048;
            }
        }

        private void B() {
            if ((this.f13457h & 16384) != 16384) {
                this.f13472w = new ArrayList(this.f13472w);
                this.f13457h |= 16384;
            }
        }

        private void C() {
            if ((this.f13457h & 32) != 32) {
                this.f13463n = new ArrayList(this.f13463n);
                this.f13457h |= 32;
            }
        }

        private void D() {
            if ((this.f13457h & 16) != 16) {
                this.f13462m = new ArrayList(this.f13462m);
                this.f13457h |= 16;
            }
        }

        private void E() {
            if ((this.f13457h & 4096) != 4096) {
                this.f13470u = new ArrayList(this.f13470u);
                this.f13457h |= 4096;
            }
        }

        private void F() {
            if ((this.f13457h & 8) != 8) {
                this.f13461l = new ArrayList(this.f13461l);
                this.f13457h |= 8;
            }
        }

        private void G() {
            if ((this.f13457h & 4194304) != 4194304) {
                this.E = new ArrayList(this.E);
                this.f13457h |= 4194304;
            }
        }

        private void H() {
        }

        static /* synthetic */ b l() {
            return q();
        }

        private static b q() {
            return new b();
        }

        private void r() {
            if ((this.f13457h & 512) != 512) {
                this.f13467r = new ArrayList(this.f13467r);
                this.f13457h |= 512;
            }
        }

        private void s() {
            if ((this.f13457h & 256) != 256) {
                this.f13466q = new ArrayList(this.f13466q);
                this.f13457h |= 256;
            }
        }

        private void t() {
            if ((this.f13457h & 128) != 128) {
                this.f13465p = new ArrayList(this.f13465p);
                this.f13457h |= 128;
            }
        }

        private void u() {
            if ((this.f13457h & 8192) != 8192) {
                this.f13471v = new ArrayList(this.f13471v);
                this.f13457h |= 8192;
            }
        }

        private void v() {
            if ((this.f13457h & 1024) != 1024) {
                this.f13468s = new ArrayList(this.f13468s);
                this.f13457h |= 1024;
            }
        }

        private void w() {
            if ((this.f13457h & 262144) != 262144) {
                this.A = new ArrayList(this.A);
                this.f13457h |= 262144;
            }
        }

        private void x() {
            if ((this.f13457h & 1048576) != 1048576) {
                this.C = new ArrayList(this.C);
                this.f13457h |= 1048576;
            }
        }

        private void y() {
            if ((this.f13457h & 524288) != 524288) {
                this.B = new ArrayList(this.B);
                this.f13457h |= 524288;
            }
        }

        private void z() {
            if ((this.f13457h & 64) != 64) {
                this.f13464o = new ArrayList(this.f13464o);
                this.f13457h |= 64;
            }
        }

        @Override // qc.i.b
        /* renamed from: I, reason: merged with bridge method [inline-methods] */
        public b f(c cVar) {
            if (cVar == c.t0()) {
                return this;
            }
            if (cVar.g1()) {
                O(cVar.y0());
            }
            if (cVar.h1()) {
                P(cVar.z0());
            }
            if (cVar.f1()) {
                N(cVar.l0());
            }
            if (!cVar.f13442l.isEmpty()) {
                if (this.f13461l.isEmpty()) {
                    this.f13461l = cVar.f13442l;
                    this.f13457h &= -9;
                } else {
                    F();
                    this.f13461l.addAll(cVar.f13442l);
                }
            }
            if (!cVar.f13443m.isEmpty()) {
                if (this.f13462m.isEmpty()) {
                    this.f13462m = cVar.f13443m;
                    this.f13457h &= -17;
                } else {
                    D();
                    this.f13462m.addAll(cVar.f13443m);
                }
            }
            if (!cVar.f13444n.isEmpty()) {
                if (this.f13463n.isEmpty()) {
                    this.f13463n = cVar.f13444n;
                    this.f13457h &= -33;
                } else {
                    C();
                    this.f13463n.addAll(cVar.f13444n);
                }
            }
            if (!cVar.f13446p.isEmpty()) {
                if (this.f13464o.isEmpty()) {
                    this.f13464o = cVar.f13446p;
                    this.f13457h &= -65;
                } else {
                    z();
                    this.f13464o.addAll(cVar.f13446p);
                }
            }
            if (!cVar.f13448r.isEmpty()) {
                if (this.f13465p.isEmpty()) {
                    this.f13465p = cVar.f13448r;
                    this.f13457h &= -129;
                } else {
                    t();
                    this.f13465p.addAll(cVar.f13448r);
                }
            }
            if (!cVar.f13449s.isEmpty()) {
                if (this.f13466q.isEmpty()) {
                    this.f13466q = cVar.f13449s;
                    this.f13457h &= -257;
                } else {
                    s();
                    this.f13466q.addAll(cVar.f13449s);
                }
            }
            if (!cVar.f13451u.isEmpty()) {
                if (this.f13467r.isEmpty()) {
                    this.f13467r = cVar.f13451u;
                    this.f13457h &= -513;
                } else {
                    r();
                    this.f13467r.addAll(cVar.f13451u);
                }
            }
            if (!cVar.f13452v.isEmpty()) {
                if (this.f13468s.isEmpty()) {
                    this.f13468s = cVar.f13452v;
                    this.f13457h &= -1025;
                } else {
                    v();
                    this.f13468s.addAll(cVar.f13452v);
                }
            }
            if (!cVar.f13453w.isEmpty()) {
                if (this.f13469t.isEmpty()) {
                    this.f13469t = cVar.f13453w;
                    this.f13457h &= -2049;
                } else {
                    A();
                    this.f13469t.addAll(cVar.f13453w);
                }
            }
            if (!cVar.f13454x.isEmpty()) {
                if (this.f13470u.isEmpty()) {
                    this.f13470u = cVar.f13454x;
                    this.f13457h &= -4097;
                } else {
                    E();
                    this.f13470u.addAll(cVar.f13454x);
                }
            }
            if (!cVar.f13455y.isEmpty()) {
                if (this.f13471v.isEmpty()) {
                    this.f13471v = cVar.f13455y;
                    this.f13457h &= -8193;
                } else {
                    u();
                    this.f13471v.addAll(cVar.f13455y);
                }
            }
            if (!cVar.f13456z.isEmpty()) {
                if (this.f13472w.isEmpty()) {
                    this.f13472w = cVar.f13456z;
                    this.f13457h &= -16385;
                } else {
                    B();
                    this.f13472w.addAll(cVar.f13456z);
                }
            }
            if (cVar.i1()) {
                Q(cVar.D0());
            }
            if (cVar.j1()) {
                K(cVar.E0());
            }
            if (cVar.k1()) {
                R(cVar.F0());
            }
            if (!cVar.E.isEmpty()) {
                if (this.A.isEmpty()) {
                    this.A = cVar.E;
                    this.f13457h &= -262145;
                } else {
                    w();
                    this.A.addAll(cVar.E);
                }
            }
            if (!cVar.G.isEmpty()) {
                if (this.B.isEmpty()) {
                    this.B = cVar.G;
                    this.f13457h &= -524289;
                } else {
                    y();
                    this.B.addAll(cVar.G);
                }
            }
            if (!cVar.H.isEmpty()) {
                if (this.C.isEmpty()) {
                    this.C = cVar.H;
                    this.f13457h &= -1048577;
                } else {
                    x();
                    this.C.addAll(cVar.H);
                }
            }
            if (cVar.l1()) {
                L(cVar.c1());
            }
            if (!cVar.K.isEmpty()) {
                if (this.E.isEmpty()) {
                    this.E = cVar.K;
                    this.f13457h &= -4194305;
                } else {
                    G();
                    this.E.addAll(cVar.K);
                }
            }
            if (cVar.m1()) {
                M(cVar.e1());
            }
            k(cVar);
            g(e().d(cVar.f13437g));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: J, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            c cVar = null;
            try {
                try {
                    c a10 = c.P.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (cVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                c cVar2 = (c) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    cVar = cVar2;
                    if (cVar != null) {
                        f(cVar);
                    }
                    throw th;
                }
            }
        }

        public b K(q qVar) {
            if ((this.f13457h & 65536) == 65536 && this.f13474y != q.S()) {
                this.f13474y = q.t0(this.f13474y).f(qVar).o();
            } else {
                this.f13474y = qVar;
            }
            this.f13457h |= 65536;
            return this;
        }

        public b L(t tVar) {
            if ((this.f13457h & 2097152) == 2097152 && this.D != t.r()) {
                this.D = t.z(this.D).f(tVar).j();
            } else {
                this.D = tVar;
            }
            this.f13457h |= 2097152;
            return this;
        }

        public b M(w wVar) {
            if ((this.f13457h & 8388608) == 8388608 && this.F != w.p()) {
                this.F = w.u(this.F).f(wVar).j();
            } else {
                this.F = wVar;
            }
            this.f13457h |= 8388608;
            return this;
        }

        public b N(int i10) {
            this.f13457h |= 4;
            this.f13460k = i10;
            return this;
        }

        public b O(int i10) {
            this.f13457h |= 1;
            this.f13458i = i10;
            return this;
        }

        public b P(int i10) {
            this.f13457h |= 2;
            this.f13459j = i10;
            return this;
        }

        public b Q(int i10) {
            this.f13457h |= 32768;
            this.f13473x = i10;
            return this;
        }

        public b R(int i10) {
            this.f13457h |= 131072;
            this.f13475z = i10;
            return this;
        }

        @Override // qc.q.a
        /* renamed from: n, reason: merged with bridge method [inline-methods] */
        public c build() {
            c o10 = o();
            if (o10.isInitialized()) {
                return o10;
            }
            throw a.AbstractC0092a.c(o10);
        }

        public c o() {
            c cVar = new c(this);
            int i10 = this.f13457h;
            int i11 = (i10 & 1) != 1 ? 0 : 1;
            cVar.f13439i = this.f13458i;
            if ((i10 & 2) == 2) {
                i11 |= 2;
            }
            cVar.f13440j = this.f13459j;
            if ((i10 & 4) == 4) {
                i11 |= 4;
            }
            cVar.f13441k = this.f13460k;
            if ((this.f13457h & 8) == 8) {
                this.f13461l = Collections.unmodifiableList(this.f13461l);
                this.f13457h &= -9;
            }
            cVar.f13442l = this.f13461l;
            if ((this.f13457h & 16) == 16) {
                this.f13462m = Collections.unmodifiableList(this.f13462m);
                this.f13457h &= -17;
            }
            cVar.f13443m = this.f13462m;
            if ((this.f13457h & 32) == 32) {
                this.f13463n = Collections.unmodifiableList(this.f13463n);
                this.f13457h &= -33;
            }
            cVar.f13444n = this.f13463n;
            if ((this.f13457h & 64) == 64) {
                this.f13464o = Collections.unmodifiableList(this.f13464o);
                this.f13457h &= -65;
            }
            cVar.f13446p = this.f13464o;
            if ((this.f13457h & 128) == 128) {
                this.f13465p = Collections.unmodifiableList(this.f13465p);
                this.f13457h &= -129;
            }
            cVar.f13448r = this.f13465p;
            if ((this.f13457h & 256) == 256) {
                this.f13466q = Collections.unmodifiableList(this.f13466q);
                this.f13457h &= -257;
            }
            cVar.f13449s = this.f13466q;
            if ((this.f13457h & 512) == 512) {
                this.f13467r = Collections.unmodifiableList(this.f13467r);
                this.f13457h &= -513;
            }
            cVar.f13451u = this.f13467r;
            if ((this.f13457h & 1024) == 1024) {
                this.f13468s = Collections.unmodifiableList(this.f13468s);
                this.f13457h &= -1025;
            }
            cVar.f13452v = this.f13468s;
            if ((this.f13457h & 2048) == 2048) {
                this.f13469t = Collections.unmodifiableList(this.f13469t);
                this.f13457h &= -2049;
            }
            cVar.f13453w = this.f13469t;
            if ((this.f13457h & 4096) == 4096) {
                this.f13470u = Collections.unmodifiableList(this.f13470u);
                this.f13457h &= -4097;
            }
            cVar.f13454x = this.f13470u;
            if ((this.f13457h & 8192) == 8192) {
                this.f13471v = Collections.unmodifiableList(this.f13471v);
                this.f13457h &= -8193;
            }
            cVar.f13455y = this.f13471v;
            if ((this.f13457h & 16384) == 16384) {
                this.f13472w = Collections.unmodifiableList(this.f13472w);
                this.f13457h &= -16385;
            }
            cVar.f13456z = this.f13472w;
            if ((i10 & 32768) == 32768) {
                i11 |= 8;
            }
            cVar.B = this.f13473x;
            if ((i10 & 65536) == 65536) {
                i11 |= 16;
            }
            cVar.C = this.f13474y;
            if ((i10 & 131072) == 131072) {
                i11 |= 32;
            }
            cVar.D = this.f13475z;
            if ((this.f13457h & 262144) == 262144) {
                this.A = Collections.unmodifiableList(this.A);
                this.f13457h &= -262145;
            }
            cVar.E = this.A;
            if ((this.f13457h & 524288) == 524288) {
                this.B = Collections.unmodifiableList(this.B);
                this.f13457h &= -524289;
            }
            cVar.G = this.B;
            if ((this.f13457h & 1048576) == 1048576) {
                this.C = Collections.unmodifiableList(this.C);
                this.f13457h &= -1048577;
            }
            cVar.H = this.C;
            if ((i10 & 2097152) == 2097152) {
                i11 |= 64;
            }
            cVar.J = this.D;
            if ((this.f13457h & 4194304) == 4194304) {
                this.E = Collections.unmodifiableList(this.E);
                this.f13457h &= -4194305;
            }
            cVar.K = this.E;
            if ((i10 & 8388608) == 8388608) {
                i11 |= 128;
            }
            cVar.L = this.F;
            cVar.f13438h = i11;
            return cVar;
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
        public b d() {
            return q().f(o());
        }
    }

    /* compiled from: ProtoBuf.java */
    /* renamed from: jc.c$c, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public enum EnumC0065c implements j.a {
        CLASS(0, 0),
        INTERFACE(1, 1),
        ENUM_CLASS(2, 2),
        ENUM_ENTRY(3, 3),
        ANNOTATION_CLASS(4, 4),
        OBJECT(5, 5),
        COMPANION_OBJECT(6, 6);


        /* renamed from: m, reason: collision with root package name */
        private static j.b<EnumC0065c> f13483m = new a();

        /* renamed from: e, reason: collision with root package name */
        private final int f13485e;

        /* compiled from: ProtoBuf.java */
        /* renamed from: jc.c$c$a */
        /* loaded from: classes2.dex */
        static class a implements j.b<EnumC0065c> {
            a() {
            }

            @Override // qc.j.b
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public EnumC0065c findValueByNumber(int i10) {
                return EnumC0065c.a(i10);
            }
        }

        EnumC0065c(int i10, int i11) {
            this.f13485e = i11;
        }

        public static EnumC0065c a(int i10) {
            switch (i10) {
                case 0:
                    return CLASS;
                case 1:
                    return INTERFACE;
                case 2:
                    return ENUM_CLASS;
                case 3:
                    return ENUM_ENTRY;
                case 4:
                    return ANNOTATION_CLASS;
                case 5:
                    return OBJECT;
                case 6:
                    return COMPANION_OBJECT;
                default:
                    return null;
            }
        }

        @Override // qc.j.a
        public final int getNumber() {
            return this.f13485e;
        }
    }

    static {
        c cVar = new c(true);
        O = cVar;
        cVar.n1();
    }

    private void n1() {
        this.f13439i = 6;
        this.f13440j = 0;
        this.f13441k = 0;
        this.f13442l = Collections.emptyList();
        this.f13443m = Collections.emptyList();
        this.f13444n = Collections.emptyList();
        this.f13446p = Collections.emptyList();
        this.f13448r = Collections.emptyList();
        this.f13449s = Collections.emptyList();
        this.f13451u = Collections.emptyList();
        this.f13452v = Collections.emptyList();
        this.f13453w = Collections.emptyList();
        this.f13454x = Collections.emptyList();
        this.f13455y = Collections.emptyList();
        this.f13456z = Collections.emptyList();
        this.B = 0;
        this.C = q.S();
        this.D = 0;
        this.E = Collections.emptyList();
        this.G = Collections.emptyList();
        this.H = Collections.emptyList();
        this.J = t.r();
        this.K = Collections.emptyList();
        this.L = w.p();
    }

    public static b o1() {
        return b.l();
    }

    public static b p1(c cVar) {
        return o1().f(cVar);
    }

    public static c r1(InputStream inputStream, qc.g gVar) {
        return P.c(inputStream, gVar);
    }

    public static c t0() {
        return O;
    }

    public i A0(int i10) {
        return this.f13452v.get(i10);
    }

    public int B0() {
        return this.f13452v.size();
    }

    public List<i> C0() {
        return this.f13452v;
    }

    public int D0() {
        return this.B;
    }

    public q E0() {
        return this.C;
    }

    public int F0() {
        return this.D;
    }

    public int G0() {
        return this.E.size();
    }

    public List<Integer> H0() {
        return this.E;
    }

    public q I0(int i10) {
        return this.G.get(i10);
    }

    public int J0() {
        return this.G.size();
    }

    public int K0() {
        return this.H.size();
    }

    public List<Integer> L0() {
        return this.H;
    }

    public List<q> M0() {
        return this.G;
    }

    public List<Integer> N0() {
        return this.f13446p;
    }

    public n O0(int i10) {
        return this.f13453w.get(i10);
    }

    public int P0() {
        return this.f13453w.size();
    }

    public List<n> Q0() {
        return this.f13453w;
    }

    public List<Integer> R0() {
        return this.f13456z;
    }

    public q S0(int i10) {
        return this.f13443m.get(i10);
    }

    public int T0() {
        return this.f13443m.size();
    }

    public List<Integer> U0() {
        return this.f13444n;
    }

    public List<q> V0() {
        return this.f13443m;
    }

    public r W0(int i10) {
        return this.f13454x.get(i10);
    }

    public int X0() {
        return this.f13454x.size();
    }

    public List<r> Y0() {
        return this.f13454x;
    }

    public s Z0(int i10) {
        return this.f13442l.get(i10);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        i.d<MessageType>.a t7 = t();
        if ((this.f13438h & 1) == 1) {
            fVar.a0(1, this.f13439i);
        }
        if (U0().size() > 0) {
            fVar.o0(18);
            fVar.o0(this.f13445o);
        }
        for (int i10 = 0; i10 < this.f13444n.size(); i10++) {
            fVar.b0(this.f13444n.get(i10).intValue());
        }
        if ((this.f13438h & 2) == 2) {
            fVar.a0(3, this.f13440j);
        }
        if ((this.f13438h & 4) == 4) {
            fVar.a0(4, this.f13441k);
        }
        for (int i11 = 0; i11 < this.f13442l.size(); i11++) {
            fVar.d0(5, this.f13442l.get(i11));
        }
        for (int i12 = 0; i12 < this.f13443m.size(); i12++) {
            fVar.d0(6, this.f13443m.get(i12));
        }
        if (N0().size() > 0) {
            fVar.o0(58);
            fVar.o0(this.f13447q);
        }
        for (int i13 = 0; i13 < this.f13446p.size(); i13++) {
            fVar.b0(this.f13446p.get(i13).intValue());
        }
        for (int i14 = 0; i14 < this.f13451u.size(); i14++) {
            fVar.d0(8, this.f13451u.get(i14));
        }
        for (int i15 = 0; i15 < this.f13452v.size(); i15++) {
            fVar.d0(9, this.f13452v.get(i15));
        }
        for (int i16 = 0; i16 < this.f13453w.size(); i16++) {
            fVar.d0(10, this.f13453w.get(i16));
        }
        for (int i17 = 0; i17 < this.f13454x.size(); i17++) {
            fVar.d0(11, this.f13454x.get(i17));
        }
        for (int i18 = 0; i18 < this.f13455y.size(); i18++) {
            fVar.d0(13, this.f13455y.get(i18));
        }
        if (R0().size() > 0) {
            fVar.o0(130);
            fVar.o0(this.A);
        }
        for (int i19 = 0; i19 < this.f13456z.size(); i19++) {
            fVar.b0(this.f13456z.get(i19).intValue());
        }
        if ((this.f13438h & 8) == 8) {
            fVar.a0(17, this.B);
        }
        if ((this.f13438h & 16) == 16) {
            fVar.d0(18, this.C);
        }
        if ((this.f13438h & 32) == 32) {
            fVar.a0(19, this.D);
        }
        for (int i20 = 0; i20 < this.f13448r.size(); i20++) {
            fVar.d0(20, this.f13448r.get(i20));
        }
        if (r0().size() > 0) {
            fVar.o0(170);
            fVar.o0(this.f13450t);
        }
        for (int i21 = 0; i21 < this.f13449s.size(); i21++) {
            fVar.b0(this.f13449s.get(i21).intValue());
        }
        if (H0().size() > 0) {
            fVar.o0(178);
            fVar.o0(this.F);
        }
        for (int i22 = 0; i22 < this.E.size(); i22++) {
            fVar.b0(this.E.get(i22).intValue());
        }
        for (int i23 = 0; i23 < this.G.size(); i23++) {
            fVar.d0(23, this.G.get(i23));
        }
        if (L0().size() > 0) {
            fVar.o0(194);
            fVar.o0(this.I);
        }
        for (int i24 = 0; i24 < this.H.size(); i24++) {
            fVar.b0(this.H.get(i24).intValue());
        }
        if ((this.f13438h & 64) == 64) {
            fVar.d0(30, this.J);
        }
        for (int i25 = 0; i25 < this.K.size(); i25++) {
            fVar.a0(31, this.K.get(i25).intValue());
        }
        if ((this.f13438h & 128) == 128) {
            fVar.d0(32, this.L);
        }
        t7.a(19000, fVar);
        fVar.i0(this.f13437g);
    }

    public int a1() {
        return this.f13442l.size();
    }

    public List<s> b1() {
        return this.f13442l;
    }

    public t c1() {
        return this.J;
    }

    public List<Integer> d1() {
        return this.K;
    }

    public w e1() {
        return this.L;
    }

    public boolean f1() {
        return (this.f13438h & 4) == 4;
    }

    public boolean g1() {
        return (this.f13438h & 1) == 1;
    }

    @Override // qc.i, qc.q
    public qc.s<c> getParserForType() {
        return P;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.N;
        if (i10 != -1) {
            return i10;
        }
        int o10 = (this.f13438h & 1) == 1 ? qc.f.o(1, this.f13439i) + 0 : 0;
        int i11 = 0;
        for (int i12 = 0; i12 < this.f13444n.size(); i12++) {
            i11 += qc.f.p(this.f13444n.get(i12).intValue());
        }
        int i13 = o10 + i11;
        if (!U0().isEmpty()) {
            i13 = i13 + 1 + qc.f.p(i11);
        }
        this.f13445o = i11;
        if ((this.f13438h & 2) == 2) {
            i13 += qc.f.o(3, this.f13440j);
        }
        if ((this.f13438h & 4) == 4) {
            i13 += qc.f.o(4, this.f13441k);
        }
        for (int i14 = 0; i14 < this.f13442l.size(); i14++) {
            i13 += qc.f.s(5, this.f13442l.get(i14));
        }
        for (int i15 = 0; i15 < this.f13443m.size(); i15++) {
            i13 += qc.f.s(6, this.f13443m.get(i15));
        }
        int i16 = 0;
        for (int i17 = 0; i17 < this.f13446p.size(); i17++) {
            i16 += qc.f.p(this.f13446p.get(i17).intValue());
        }
        int i18 = i13 + i16;
        if (!N0().isEmpty()) {
            i18 = i18 + 1 + qc.f.p(i16);
        }
        this.f13447q = i16;
        for (int i19 = 0; i19 < this.f13451u.size(); i19++) {
            i18 += qc.f.s(8, this.f13451u.get(i19));
        }
        for (int i20 = 0; i20 < this.f13452v.size(); i20++) {
            i18 += qc.f.s(9, this.f13452v.get(i20));
        }
        for (int i21 = 0; i21 < this.f13453w.size(); i21++) {
            i18 += qc.f.s(10, this.f13453w.get(i21));
        }
        for (int i22 = 0; i22 < this.f13454x.size(); i22++) {
            i18 += qc.f.s(11, this.f13454x.get(i22));
        }
        for (int i23 = 0; i23 < this.f13455y.size(); i23++) {
            i18 += qc.f.s(13, this.f13455y.get(i23));
        }
        int i24 = 0;
        for (int i25 = 0; i25 < this.f13456z.size(); i25++) {
            i24 += qc.f.p(this.f13456z.get(i25).intValue());
        }
        int i26 = i18 + i24;
        if (!R0().isEmpty()) {
            i26 = i26 + 2 + qc.f.p(i24);
        }
        this.A = i24;
        if ((this.f13438h & 8) == 8) {
            i26 += qc.f.o(17, this.B);
        }
        if ((this.f13438h & 16) == 16) {
            i26 += qc.f.s(18, this.C);
        }
        if ((this.f13438h & 32) == 32) {
            i26 += qc.f.o(19, this.D);
        }
        for (int i27 = 0; i27 < this.f13448r.size(); i27++) {
            i26 += qc.f.s(20, this.f13448r.get(i27));
        }
        int i28 = 0;
        for (int i29 = 0; i29 < this.f13449s.size(); i29++) {
            i28 += qc.f.p(this.f13449s.get(i29).intValue());
        }
        int i30 = i26 + i28;
        if (!r0().isEmpty()) {
            i30 = i30 + 2 + qc.f.p(i28);
        }
        this.f13450t = i28;
        int i31 = 0;
        for (int i32 = 0; i32 < this.E.size(); i32++) {
            i31 += qc.f.p(this.E.get(i32).intValue());
        }
        int i33 = i30 + i31;
        if (!H0().isEmpty()) {
            i33 = i33 + 2 + qc.f.p(i31);
        }
        this.F = i31;
        for (int i34 = 0; i34 < this.G.size(); i34++) {
            i33 += qc.f.s(23, this.G.get(i34));
        }
        int i35 = 0;
        for (int i36 = 0; i36 < this.H.size(); i36++) {
            i35 += qc.f.p(this.H.get(i36).intValue());
        }
        int i37 = i33 + i35;
        if (!L0().isEmpty()) {
            i37 = i37 + 2 + qc.f.p(i35);
        }
        this.I = i35;
        if ((this.f13438h & 64) == 64) {
            i37 += qc.f.s(30, this.J);
        }
        int i38 = 0;
        for (int i39 = 0; i39 < this.K.size(); i39++) {
            i38 += qc.f.p(this.K.get(i39).intValue());
        }
        int size = i37 + i38 + (d1().size() * 2);
        if ((this.f13438h & 128) == 128) {
            size += qc.f.s(32, this.L);
        }
        int o11 = size + o() + this.f13437g.size();
        this.N = o11;
        return o11;
    }

    public boolean h1() {
        return (this.f13438h & 2) == 2;
    }

    public boolean i1() {
        return (this.f13438h & 8) == 8;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.M;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        if (!h1()) {
            this.M = (byte) 0;
            return false;
        }
        for (int i10 = 0; i10 < a1(); i10++) {
            if (!Z0(i10).isInitialized()) {
                this.M = (byte) 0;
                return false;
            }
        }
        for (int i11 = 0; i11 < T0(); i11++) {
            if (!S0(i11).isInitialized()) {
                this.M = (byte) 0;
                return false;
            }
        }
        for (int i12 = 0; i12 < q0(); i12++) {
            if (!p0(i12).isInitialized()) {
                this.M = (byte) 0;
                return false;
            }
        }
        for (int i13 = 0; i13 < n0(); i13++) {
            if (!m0(i13).isInitialized()) {
                this.M = (byte) 0;
                return false;
            }
        }
        for (int i14 = 0; i14 < B0(); i14++) {
            if (!A0(i14).isInitialized()) {
                this.M = (byte) 0;
                return false;
            }
        }
        for (int i15 = 0; i15 < P0(); i15++) {
            if (!O0(i15).isInitialized()) {
                this.M = (byte) 0;
                return false;
            }
        }
        for (int i16 = 0; i16 < X0(); i16++) {
            if (!W0(i16).isInitialized()) {
                this.M = (byte) 0;
                return false;
            }
        }
        for (int i17 = 0; i17 < w0(); i17++) {
            if (!v0(i17).isInitialized()) {
                this.M = (byte) 0;
                return false;
            }
        }
        if (j1() && !E0().isInitialized()) {
            this.M = (byte) 0;
            return false;
        }
        for (int i18 = 0; i18 < J0(); i18++) {
            if (!I0(i18).isInitialized()) {
                this.M = (byte) 0;
                return false;
            }
        }
        if (l1() && !c1().isInitialized()) {
            this.M = (byte) 0;
            return false;
        }
        if (!n()) {
            this.M = (byte) 0;
            return false;
        }
        this.M = (byte) 1;
        return true;
    }

    public boolean j1() {
        return (this.f13438h & 16) == 16;
    }

    public boolean k1() {
        return (this.f13438h & 32) == 32;
    }

    public int l0() {
        return this.f13441k;
    }

    public boolean l1() {
        return (this.f13438h & 64) == 64;
    }

    public d m0(int i10) {
        return this.f13451u.get(i10);
    }

    public boolean m1() {
        return (this.f13438h & 128) == 128;
    }

    public int n0() {
        return this.f13451u.size();
    }

    public List<d> o0() {
        return this.f13451u;
    }

    public q p0(int i10) {
        return this.f13448r.get(i10);
    }

    public int q0() {
        return this.f13448r.size();
    }

    @Override // qc.q
    /* renamed from: q1, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return o1();
    }

    public List<Integer> r0() {
        return this.f13449s;
    }

    public List<q> s0() {
        return this.f13448r;
    }

    @Override // qc.q
    /* renamed from: s1, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return p1(this);
    }

    @Override // qc.r
    /* renamed from: u0, reason: merged with bridge method [inline-methods] */
    public c getDefaultInstanceForType() {
        return O;
    }

    public g v0(int i10) {
        return this.f13455y.get(i10);
    }

    public int w0() {
        return this.f13455y.size();
    }

    public List<g> x0() {
        return this.f13455y;
    }

    public int y0() {
        return this.f13439i;
    }

    public int z0() {
        return this.f13440j;
    }

    private c(i.c<c, ?> cVar) {
        super(cVar);
        this.f13445o = -1;
        this.f13447q = -1;
        this.f13450t = -1;
        this.A = -1;
        this.F = -1;
        this.I = -1;
        this.M = (byte) -1;
        this.N = -1;
        this.f13437g = cVar.e();
    }

    private c(boolean z10) {
        this.f13445o = -1;
        this.f13447q = -1;
        this.f13450t = -1;
        this.A = -1;
        this.F = -1;
        this.I = -1;
        this.M = (byte) -1;
        this.N = -1;
        this.f13437g = qc.d.f17259e;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x003c. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v4, types: [boolean] */
    /* JADX WARN: Type inference failed for: r8v11 */
    /* JADX WARN: Type inference failed for: r8v13 */
    /* JADX WARN: Type inference failed for: r8v15 */
    /* JADX WARN: Type inference failed for: r8v17 */
    /* JADX WARN: Type inference failed for: r8v19 */
    /* JADX WARN: Type inference failed for: r8v21 */
    /* JADX WARN: Type inference failed for: r8v23 */
    /* JADX WARN: Type inference failed for: r8v25 */
    /* JADX WARN: Type inference failed for: r8v27 */
    /* JADX WARN: Type inference failed for: r8v29 */
    /* JADX WARN: Type inference failed for: r8v3 */
    /* JADX WARN: Type inference failed for: r8v31 */
    /* JADX WARN: Type inference failed for: r8v33 */
    /* JADX WARN: Type inference failed for: r8v35 */
    /* JADX WARN: Type inference failed for: r8v37 */
    /* JADX WARN: Type inference failed for: r8v39 */
    /* JADX WARN: Type inference failed for: r8v41 */
    /* JADX WARN: Type inference failed for: r8v43 */
    /* JADX WARN: Type inference failed for: r8v45 */
    /* JADX WARN: Type inference failed for: r8v47 */
    /* JADX WARN: Type inference failed for: r8v5 */
    /* JADX WARN: Type inference failed for: r8v7 */
    /* JADX WARN: Type inference failed for: r8v9 */
    private c(qc.e eVar, qc.g gVar) {
        boolean z10;
        this.f13445o = -1;
        this.f13447q = -1;
        this.f13450t = -1;
        this.A = -1;
        this.F = -1;
        this.I = -1;
        this.M = (byte) -1;
        this.N = -1;
        n1();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z11 = false;
        char c10 = 0;
        while (true) {
            ?? r52 = 4194304;
            if (!z11) {
                try {
                    try {
                        try {
                            int K = eVar.K();
                            switch (K) {
                                case 0:
                                    z10 = true;
                                    z11 = z10;
                                case 8:
                                    z10 = true;
                                    this.f13438h |= 1;
                                    this.f13439i = eVar.s();
                                case 16:
                                    int i10 = (c10 == true ? 1 : 0) & 32;
                                    char c11 = c10;
                                    if (i10 != 32) {
                                        this.f13444n = new ArrayList();
                                        c11 = (c10 == true ? 1 : 0) | ' ';
                                    }
                                    this.f13444n.add(Integer.valueOf(eVar.s()));
                                    c10 = c11;
                                    z10 = true;
                                case 18:
                                    int j10 = eVar.j(eVar.A());
                                    int i11 = (c10 == true ? 1 : 0) & 32;
                                    char c12 = c10;
                                    if (i11 != 32) {
                                        c12 = c10;
                                        if (eVar.e() > 0) {
                                            this.f13444n = new ArrayList();
                                            c12 = (c10 == true ? 1 : 0) | ' ';
                                        }
                                    }
                                    while (eVar.e() > 0) {
                                        this.f13444n.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j10);
                                    c10 = c12;
                                    z10 = true;
                                case 24:
                                    this.f13438h |= 2;
                                    this.f13440j = eVar.s();
                                    c10 = c10;
                                    z10 = true;
                                case 32:
                                    this.f13438h |= 4;
                                    this.f13441k = eVar.s();
                                    c10 = c10;
                                    z10 = true;
                                case 42:
                                    int i12 = (c10 == true ? 1 : 0) & 8;
                                    char c13 = c10;
                                    if (i12 != 8) {
                                        this.f13442l = new ArrayList();
                                        c13 = (c10 == true ? 1 : 0) | '\b';
                                    }
                                    this.f13442l.add(eVar.u(s.f13802s, gVar));
                                    c10 = c13;
                                    z10 = true;
                                case 50:
                                    int i13 = (c10 == true ? 1 : 0) & 16;
                                    char c14 = c10;
                                    if (i13 != 16) {
                                        this.f13443m = new ArrayList();
                                        c14 = (c10 == true ? 1 : 0) | 16;
                                    }
                                    this.f13443m.add(eVar.u(q.f13722z, gVar));
                                    c10 = c14;
                                    z10 = true;
                                case 56:
                                    int i14 = (c10 == true ? 1 : 0) & 64;
                                    char c15 = c10;
                                    if (i14 != 64) {
                                        this.f13446p = new ArrayList();
                                        c15 = (c10 == true ? 1 : 0) | '@';
                                    }
                                    this.f13446p.add(Integer.valueOf(eVar.s()));
                                    c10 = c15;
                                    z10 = true;
                                case 58:
                                    int j11 = eVar.j(eVar.A());
                                    int i15 = (c10 == true ? 1 : 0) & 64;
                                    char c16 = c10;
                                    if (i15 != 64) {
                                        c16 = c10;
                                        if (eVar.e() > 0) {
                                            this.f13446p = new ArrayList();
                                            c16 = (c10 == true ? 1 : 0) | '@';
                                        }
                                    }
                                    while (eVar.e() > 0) {
                                        this.f13446p.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j11);
                                    c10 = c16;
                                    z10 = true;
                                case 66:
                                    int i16 = (c10 == true ? 1 : 0) & 512;
                                    char c17 = c10;
                                    if (i16 != 512) {
                                        this.f13451u = new ArrayList();
                                        c17 = (c10 == true ? 1 : 0) | 512;
                                    }
                                    this.f13451u.add(eVar.u(d.f13487o, gVar));
                                    c10 = c17;
                                    z10 = true;
                                case 74:
                                    int i17 = (c10 == true ? 1 : 0) & 1024;
                                    char c18 = c10;
                                    if (i17 != 1024) {
                                        this.f13452v = new ArrayList();
                                        c18 = (c10 == true ? 1 : 0) | 1024;
                                    }
                                    this.f13452v.add(eVar.u(i.A, gVar));
                                    c10 = c18;
                                    z10 = true;
                                case 82:
                                    int i18 = (c10 == true ? 1 : 0) & 2048;
                                    char c19 = c10;
                                    if (i18 != 2048) {
                                        this.f13453w = new ArrayList();
                                        c19 = (c10 == true ? 1 : 0) | 2048;
                                    }
                                    this.f13453w.add(eVar.u(n.A, gVar));
                                    c10 = c19;
                                    z10 = true;
                                case 90:
                                    int i19 = (c10 == true ? 1 : 0) & 4096;
                                    char c20 = c10;
                                    if (i19 != 4096) {
                                        this.f13454x = new ArrayList();
                                        c20 = (c10 == true ? 1 : 0) | 4096;
                                    }
                                    this.f13454x.add(eVar.u(r.f13777u, gVar));
                                    c10 = c20;
                                    z10 = true;
                                case 106:
                                    int i20 = (c10 == true ? 1 : 0) & 8192;
                                    char c21 = c10;
                                    if (i20 != 8192) {
                                        this.f13455y = new ArrayList();
                                        c21 = (c10 == true ? 1 : 0) | 8192;
                                    }
                                    this.f13455y.add(eVar.u(g.f13535m, gVar));
                                    c10 = c21;
                                    z10 = true;
                                case 128:
                                    int i21 = (c10 == true ? 1 : 0) & 16384;
                                    char c22 = c10;
                                    if (i21 != 16384) {
                                        this.f13456z = new ArrayList();
                                        c22 = (c10 == true ? 1 : 0) | 16384;
                                    }
                                    this.f13456z.add(Integer.valueOf(eVar.s()));
                                    c10 = c22;
                                    z10 = true;
                                case 130:
                                    int j12 = eVar.j(eVar.A());
                                    int i22 = (c10 == true ? 1 : 0) & 16384;
                                    char c23 = c10;
                                    if (i22 != 16384) {
                                        c23 = c10;
                                        if (eVar.e() > 0) {
                                            this.f13456z = new ArrayList();
                                            c23 = (c10 == true ? 1 : 0) | 16384;
                                        }
                                    }
                                    while (eVar.e() > 0) {
                                        this.f13456z.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j12);
                                    c10 = c23;
                                    z10 = true;
                                case 136:
                                    this.f13438h |= 8;
                                    this.B = eVar.s();
                                    c10 = c10;
                                    z10 = true;
                                case 146:
                                    q.c builder = (this.f13438h & 16) == 16 ? this.C.toBuilder() : null;
                                    q qVar = (q) eVar.u(q.f13722z, gVar);
                                    this.C = qVar;
                                    if (builder != null) {
                                        builder.f(qVar);
                                        this.C = builder.o();
                                    }
                                    this.f13438h |= 16;
                                    c10 = c10;
                                    z10 = true;
                                case 152:
                                    this.f13438h |= 32;
                                    this.D = eVar.s();
                                    c10 = c10;
                                    z10 = true;
                                case 162:
                                    int i23 = (c10 == true ? 1 : 0) & 128;
                                    char c24 = c10;
                                    if (i23 != 128) {
                                        this.f13448r = new ArrayList();
                                        c24 = (c10 == true ? 1 : 0) | 128;
                                    }
                                    this.f13448r.add(eVar.u(q.f13722z, gVar));
                                    c10 = c24;
                                    z10 = true;
                                case 168:
                                    int i24 = (c10 == true ? 1 : 0) & 256;
                                    char c25 = c10;
                                    if (i24 != 256) {
                                        this.f13449s = new ArrayList();
                                        c25 = (c10 == true ? 1 : 0) | 256;
                                    }
                                    this.f13449s.add(Integer.valueOf(eVar.s()));
                                    c10 = c25;
                                    z10 = true;
                                case 170:
                                    int j13 = eVar.j(eVar.A());
                                    int i25 = (c10 == true ? 1 : 0) & 256;
                                    char c26 = c10;
                                    if (i25 != 256) {
                                        c26 = c10;
                                        if (eVar.e() > 0) {
                                            this.f13449s = new ArrayList();
                                            c26 = (c10 == true ? 1 : 0) | 256;
                                        }
                                    }
                                    while (eVar.e() > 0) {
                                        this.f13449s.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j13);
                                    c10 = c26;
                                    z10 = true;
                                case 176:
                                    int i26 = (c10 == true ? 1 : 0) & 262144;
                                    char c27 = c10;
                                    if (i26 != 262144) {
                                        this.E = new ArrayList();
                                        c27 = (c10 == true ? 1 : 0) | 0;
                                    }
                                    this.E.add(Integer.valueOf(eVar.s()));
                                    c10 = c27;
                                    z10 = true;
                                case 178:
                                    int j14 = eVar.j(eVar.A());
                                    int i27 = (c10 == true ? 1 : 0) & 262144;
                                    char c28 = c10;
                                    if (i27 != 262144) {
                                        c28 = c10;
                                        if (eVar.e() > 0) {
                                            this.E = new ArrayList();
                                            c28 = (c10 == true ? 1 : 0) | 0;
                                        }
                                    }
                                    while (eVar.e() > 0) {
                                        this.E.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j14);
                                    c10 = c28;
                                    z10 = true;
                                case 186:
                                    int i28 = (c10 == true ? 1 : 0) & 524288;
                                    char c29 = c10;
                                    if (i28 != 524288) {
                                        this.G = new ArrayList();
                                        c29 = (c10 == true ? 1 : 0) | 0;
                                    }
                                    this.G.add(eVar.u(q.f13722z, gVar));
                                    c10 = c29;
                                    z10 = true;
                                case 192:
                                    int i29 = (c10 == true ? 1 : 0) & 1048576;
                                    char c30 = c10;
                                    if (i29 != 1048576) {
                                        this.H = new ArrayList();
                                        c30 = (c10 == true ? 1 : 0) | 0;
                                    }
                                    this.H.add(Integer.valueOf(eVar.s()));
                                    c10 = c30;
                                    z10 = true;
                                case 194:
                                    int j15 = eVar.j(eVar.A());
                                    int i30 = (c10 == true ? 1 : 0) & 1048576;
                                    char c31 = c10;
                                    if (i30 != 1048576) {
                                        c31 = c10;
                                        if (eVar.e() > 0) {
                                            this.H = new ArrayList();
                                            c31 = (c10 == true ? 1 : 0) | 0;
                                        }
                                    }
                                    while (eVar.e() > 0) {
                                        this.H.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j15);
                                    c10 = c31;
                                    z10 = true;
                                case 242:
                                    t.b builder2 = (this.f13438h & 64) == 64 ? this.J.toBuilder() : null;
                                    t tVar = (t) eVar.u(t.f13828m, gVar);
                                    this.J = tVar;
                                    if (builder2 != null) {
                                        builder2.f(tVar);
                                        this.J = builder2.j();
                                    }
                                    this.f13438h |= 64;
                                    c10 = c10;
                                    z10 = true;
                                case 248:
                                    int i31 = (c10 == true ? 1 : 0) & 4194304;
                                    char c32 = c10;
                                    if (i31 != 4194304) {
                                        this.K = new ArrayList();
                                        c32 = (c10 == true ? 1 : 0) | 0;
                                    }
                                    this.K.add(Integer.valueOf(eVar.s()));
                                    c10 = c32;
                                    z10 = true;
                                case 250:
                                    int j16 = eVar.j(eVar.A());
                                    int i32 = (c10 == true ? 1 : 0) & 4194304;
                                    char c33 = c10;
                                    if (i32 != 4194304) {
                                        c33 = c10;
                                        if (eVar.e() > 0) {
                                            this.K = new ArrayList();
                                            c33 = (c10 == true ? 1 : 0) | 0;
                                        }
                                    }
                                    while (eVar.e() > 0) {
                                        this.K.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j16);
                                    c10 = c33;
                                    z10 = true;
                                case 258:
                                    w.b builder3 = (this.f13438h & 128) == 128 ? this.L.toBuilder() : null;
                                    w wVar = (w) eVar.u(w.f13889k, gVar);
                                    this.L = wVar;
                                    if (builder3 != null) {
                                        builder3.f(wVar);
                                        this.L = builder3.j();
                                    }
                                    this.f13438h |= 128;
                                    c10 = c10;
                                    z10 = true;
                                default:
                                    z10 = true;
                                    r52 = j(eVar, J, gVar, K);
                                    c10 = r52 != 0 ? c10 : c10;
                                    z11 = z10;
                            }
                        } catch (qc.k e10) {
                            throw e10.i(this);
                        }
                    } catch (IOException e11) {
                        throw new qc.k(e11.getMessage()).i(this);
                    }
                } catch (Throwable th) {
                    if (((c10 == true ? 1 : 0) & 32) == 32) {
                        this.f13444n = Collections.unmodifiableList(this.f13444n);
                    }
                    if (((c10 == true ? 1 : 0) & 8) == 8) {
                        this.f13442l = Collections.unmodifiableList(this.f13442l);
                    }
                    if (((c10 == true ? 1 : 0) & 16) == 16) {
                        this.f13443m = Collections.unmodifiableList(this.f13443m);
                    }
                    if (((c10 == true ? 1 : 0) & 64) == 64) {
                        this.f13446p = Collections.unmodifiableList(this.f13446p);
                    }
                    if (((c10 == true ? 1 : 0) & 512) == 512) {
                        this.f13451u = Collections.unmodifiableList(this.f13451u);
                    }
                    if (((c10 == true ? 1 : 0) & 1024) == 1024) {
                        this.f13452v = Collections.unmodifiableList(this.f13452v);
                    }
                    if (((c10 == true ? 1 : 0) & 2048) == 2048) {
                        this.f13453w = Collections.unmodifiableList(this.f13453w);
                    }
                    if (((c10 == true ? 1 : 0) & 4096) == 4096) {
                        this.f13454x = Collections.unmodifiableList(this.f13454x);
                    }
                    if (((c10 == true ? 1 : 0) & 8192) == 8192) {
                        this.f13455y = Collections.unmodifiableList(this.f13455y);
                    }
                    if (((c10 == true ? 1 : 0) & 16384) == 16384) {
                        this.f13456z = Collections.unmodifiableList(this.f13456z);
                    }
                    if (((c10 == true ? 1 : 0) & 128) == 128) {
                        this.f13448r = Collections.unmodifiableList(this.f13448r);
                    }
                    if (((c10 == true ? 1 : 0) & 256) == 256) {
                        this.f13449s = Collections.unmodifiableList(this.f13449s);
                    }
                    if (((c10 == true ? 1 : 0) & 262144) == 262144) {
                        this.E = Collections.unmodifiableList(this.E);
                    }
                    if (((c10 == true ? 1 : 0) & 524288) == 524288) {
                        this.G = Collections.unmodifiableList(this.G);
                    }
                    if (((c10 == true ? 1 : 0) & 1048576) == 1048576) {
                        this.H = Collections.unmodifiableList(this.H);
                    }
                    if (((c10 == true ? 1 : 0) & r52) == r52) {
                        this.K = Collections.unmodifiableList(this.K);
                    }
                    try {
                        J.I();
                    } catch (IOException unused) {
                    } catch (Throwable th2) {
                        this.f13437g = q10.v();
                        throw th2;
                    }
                    this.f13437g = q10.v();
                    g();
                    throw th;
                }
            } else {
                if (((c10 == true ? 1 : 0) & 32) == 32) {
                    this.f13444n = Collections.unmodifiableList(this.f13444n);
                }
                if (((c10 == true ? 1 : 0) & 8) == 8) {
                    this.f13442l = Collections.unmodifiableList(this.f13442l);
                }
                if (((c10 == true ? 1 : 0) & 16) == 16) {
                    this.f13443m = Collections.unmodifiableList(this.f13443m);
                }
                if (((c10 == true ? 1 : 0) & 64) == 64) {
                    this.f13446p = Collections.unmodifiableList(this.f13446p);
                }
                if (((c10 == true ? 1 : 0) & 512) == 512) {
                    this.f13451u = Collections.unmodifiableList(this.f13451u);
                }
                if (((c10 == true ? 1 : 0) & 1024) == 1024) {
                    this.f13452v = Collections.unmodifiableList(this.f13452v);
                }
                if (((c10 == true ? 1 : 0) & 2048) == 2048) {
                    this.f13453w = Collections.unmodifiableList(this.f13453w);
                }
                if (((c10 == true ? 1 : 0) & 4096) == 4096) {
                    this.f13454x = Collections.unmodifiableList(this.f13454x);
                }
                if (((c10 == true ? 1 : 0) & 8192) == 8192) {
                    this.f13455y = Collections.unmodifiableList(this.f13455y);
                }
                if (((c10 == true ? 1 : 0) & 16384) == 16384) {
                    this.f13456z = Collections.unmodifiableList(this.f13456z);
                }
                if (((c10 == true ? 1 : 0) & 128) == 128) {
                    this.f13448r = Collections.unmodifiableList(this.f13448r);
                }
                if (((c10 == true ? 1 : 0) & 256) == 256) {
                    this.f13449s = Collections.unmodifiableList(this.f13449s);
                }
                if (((c10 == true ? 1 : 0) & 262144) == 262144) {
                    this.E = Collections.unmodifiableList(this.E);
                }
                if (((c10 == true ? 1 : 0) & 524288) == 524288) {
                    this.G = Collections.unmodifiableList(this.G);
                }
                if (((c10 == true ? 1 : 0) & 1048576) == 1048576) {
                    this.H = Collections.unmodifiableList(this.H);
                }
                if (((c10 == true ? 1 : 0) & 4194304) == 4194304) {
                    this.K = Collections.unmodifiableList(this.K);
                }
                try {
                    J.I();
                } catch (IOException unused2) {
                } catch (Throwable th3) {
                    this.f13437g = q10.v();
                    throw th3;
                }
                this.f13437g = q10.v();
                g();
                return;
            }
        }
    }
}
