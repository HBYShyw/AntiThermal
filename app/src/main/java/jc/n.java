package jc;

import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jc.q;
import jc.u;
import qc.a;
import qc.d;
import qc.i;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class n extends i.d<n> {
    public static qc.s<n> A = new a();

    /* renamed from: z, reason: collision with root package name */
    private static final n f13651z;

    /* renamed from: g, reason: collision with root package name */
    private final qc.d f13652g;

    /* renamed from: h, reason: collision with root package name */
    private int f13653h;

    /* renamed from: i, reason: collision with root package name */
    private int f13654i;

    /* renamed from: j, reason: collision with root package name */
    private int f13655j;

    /* renamed from: k, reason: collision with root package name */
    private int f13656k;

    /* renamed from: l, reason: collision with root package name */
    private q f13657l;

    /* renamed from: m, reason: collision with root package name */
    private int f13658m;

    /* renamed from: n, reason: collision with root package name */
    private List<s> f13659n;

    /* renamed from: o, reason: collision with root package name */
    private q f13660o;

    /* renamed from: p, reason: collision with root package name */
    private int f13661p;

    /* renamed from: q, reason: collision with root package name */
    private List<q> f13662q;

    /* renamed from: r, reason: collision with root package name */
    private List<Integer> f13663r;

    /* renamed from: s, reason: collision with root package name */
    private int f13664s;

    /* renamed from: t, reason: collision with root package name */
    private u f13665t;

    /* renamed from: u, reason: collision with root package name */
    private int f13666u;

    /* renamed from: v, reason: collision with root package name */
    private int f13667v;

    /* renamed from: w, reason: collision with root package name */
    private List<Integer> f13668w;

    /* renamed from: x, reason: collision with root package name */
    private byte f13669x;

    /* renamed from: y, reason: collision with root package name */
    private int f13670y;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<n> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public n a(qc.e eVar, qc.g gVar) {
            return new n(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.c<n, b> {

        /* renamed from: h, reason: collision with root package name */
        private int f13671h;

        /* renamed from: k, reason: collision with root package name */
        private int f13674k;

        /* renamed from: m, reason: collision with root package name */
        private int f13676m;

        /* renamed from: p, reason: collision with root package name */
        private int f13679p;

        /* renamed from: t, reason: collision with root package name */
        private int f13683t;

        /* renamed from: u, reason: collision with root package name */
        private int f13684u;

        /* renamed from: i, reason: collision with root package name */
        private int f13672i = DataLinkConstants.ATOM;

        /* renamed from: j, reason: collision with root package name */
        private int f13673j = 2054;

        /* renamed from: l, reason: collision with root package name */
        private q f13675l = q.S();

        /* renamed from: n, reason: collision with root package name */
        private List<s> f13677n = Collections.emptyList();

        /* renamed from: o, reason: collision with root package name */
        private q f13678o = q.S();

        /* renamed from: q, reason: collision with root package name */
        private List<q> f13680q = Collections.emptyList();

        /* renamed from: r, reason: collision with root package name */
        private List<Integer> f13681r = Collections.emptyList();

        /* renamed from: s, reason: collision with root package name */
        private u f13682s = u.D();

        /* renamed from: v, reason: collision with root package name */
        private List<Integer> f13685v = Collections.emptyList();

        private b() {
            v();
        }

        static /* synthetic */ b l() {
            return q();
        }

        private static b q() {
            return new b();
        }

        private void r() {
            if ((this.f13671h & 512) != 512) {
                this.f13681r = new ArrayList(this.f13681r);
                this.f13671h |= 512;
            }
        }

        private void s() {
            if ((this.f13671h & 256) != 256) {
                this.f13680q = new ArrayList(this.f13680q);
                this.f13671h |= 256;
            }
        }

        private void t() {
            if ((this.f13671h & 32) != 32) {
                this.f13677n = new ArrayList(this.f13677n);
                this.f13671h |= 32;
            }
        }

        private void u() {
            if ((this.f13671h & 8192) != 8192) {
                this.f13685v = new ArrayList(this.f13685v);
                this.f13671h |= 8192;
            }
        }

        private void v() {
        }

        public b A(u uVar) {
            if ((this.f13671h & 1024) == 1024 && this.f13682s != u.D()) {
                this.f13682s = u.T(this.f13682s).f(uVar).o();
            } else {
                this.f13682s = uVar;
            }
            this.f13671h |= 1024;
            return this;
        }

        public b B(int i10) {
            this.f13671h |= 1;
            this.f13672i = i10;
            return this;
        }

        public b C(int i10) {
            this.f13671h |= 2048;
            this.f13683t = i10;
            return this;
        }

        public b D(int i10) {
            this.f13671h |= 4;
            this.f13674k = i10;
            return this;
        }

        public b E(int i10) {
            this.f13671h |= 2;
            this.f13673j = i10;
            return this;
        }

        public b F(int i10) {
            this.f13671h |= 128;
            this.f13679p = i10;
            return this;
        }

        public b G(int i10) {
            this.f13671h |= 16;
            this.f13676m = i10;
            return this;
        }

        public b H(int i10) {
            this.f13671h |= 4096;
            this.f13684u = i10;
            return this;
        }

        @Override // qc.q.a
        /* renamed from: n, reason: merged with bridge method [inline-methods] */
        public n build() {
            n o10 = o();
            if (o10.isInitialized()) {
                return o10;
            }
            throw a.AbstractC0092a.c(o10);
        }

        public n o() {
            n nVar = new n(this);
            int i10 = this.f13671h;
            int i11 = (i10 & 1) != 1 ? 0 : 1;
            nVar.f13654i = this.f13672i;
            if ((i10 & 2) == 2) {
                i11 |= 2;
            }
            nVar.f13655j = this.f13673j;
            if ((i10 & 4) == 4) {
                i11 |= 4;
            }
            nVar.f13656k = this.f13674k;
            if ((i10 & 8) == 8) {
                i11 |= 8;
            }
            nVar.f13657l = this.f13675l;
            if ((i10 & 16) == 16) {
                i11 |= 16;
            }
            nVar.f13658m = this.f13676m;
            if ((this.f13671h & 32) == 32) {
                this.f13677n = Collections.unmodifiableList(this.f13677n);
                this.f13671h &= -33;
            }
            nVar.f13659n = this.f13677n;
            if ((i10 & 64) == 64) {
                i11 |= 32;
            }
            nVar.f13660o = this.f13678o;
            if ((i10 & 128) == 128) {
                i11 |= 64;
            }
            nVar.f13661p = this.f13679p;
            if ((this.f13671h & 256) == 256) {
                this.f13680q = Collections.unmodifiableList(this.f13680q);
                this.f13671h &= -257;
            }
            nVar.f13662q = this.f13680q;
            if ((this.f13671h & 512) == 512) {
                this.f13681r = Collections.unmodifiableList(this.f13681r);
                this.f13671h &= -513;
            }
            nVar.f13663r = this.f13681r;
            if ((i10 & 1024) == 1024) {
                i11 |= 128;
            }
            nVar.f13665t = this.f13682s;
            if ((i10 & 2048) == 2048) {
                i11 |= 256;
            }
            nVar.f13666u = this.f13683t;
            if ((i10 & 4096) == 4096) {
                i11 |= 512;
            }
            nVar.f13667v = this.f13684u;
            if ((this.f13671h & 8192) == 8192) {
                this.f13685v = Collections.unmodifiableList(this.f13685v);
                this.f13671h &= -8193;
            }
            nVar.f13668w = this.f13685v;
            nVar.f13653h = i11;
            return nVar;
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b d() {
            return q().f(o());
        }

        @Override // qc.i.b
        /* renamed from: w, reason: merged with bridge method [inline-methods] */
        public b f(n nVar) {
            if (nVar == n.T()) {
                return this;
            }
            if (nVar.j0()) {
                B(nVar.V());
            }
            if (nVar.m0()) {
                E(nVar.Y());
            }
            if (nVar.l0()) {
                D(nVar.X());
            }
            if (nVar.p0()) {
                z(nVar.b0());
            }
            if (nVar.q0()) {
                G(nVar.c0());
            }
            if (!nVar.f13659n.isEmpty()) {
                if (this.f13677n.isEmpty()) {
                    this.f13677n = nVar.f13659n;
                    this.f13671h &= -33;
                } else {
                    t();
                    this.f13677n.addAll(nVar.f13659n);
                }
            }
            if (nVar.n0()) {
                y(nVar.Z());
            }
            if (nVar.o0()) {
                F(nVar.a0());
            }
            if (!nVar.f13662q.isEmpty()) {
                if (this.f13680q.isEmpty()) {
                    this.f13680q = nVar.f13662q;
                    this.f13671h &= -257;
                } else {
                    s();
                    this.f13680q.addAll(nVar.f13662q);
                }
            }
            if (!nVar.f13663r.isEmpty()) {
                if (this.f13681r.isEmpty()) {
                    this.f13681r = nVar.f13663r;
                    this.f13671h &= -513;
                } else {
                    r();
                    this.f13681r.addAll(nVar.f13663r);
                }
            }
            if (nVar.s0()) {
                A(nVar.e0());
            }
            if (nVar.k0()) {
                C(nVar.W());
            }
            if (nVar.r0()) {
                H(nVar.d0());
            }
            if (!nVar.f13668w.isEmpty()) {
                if (this.f13685v.isEmpty()) {
                    this.f13685v = nVar.f13668w;
                    this.f13671h &= -8193;
                } else {
                    u();
                    this.f13685v.addAll(nVar.f13668w);
                }
            }
            k(nVar);
            g(e().d(nVar.f13652g));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: x, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            n nVar = null;
            try {
                try {
                    n a10 = n.A.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (nVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                n nVar2 = (n) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    nVar = nVar2;
                    if (nVar != null) {
                        f(nVar);
                    }
                    throw th;
                }
            }
        }

        public b y(q qVar) {
            if ((this.f13671h & 64) == 64 && this.f13678o != q.S()) {
                this.f13678o = q.t0(this.f13678o).f(qVar).o();
            } else {
                this.f13678o = qVar;
            }
            this.f13671h |= 64;
            return this;
        }

        public b z(q qVar) {
            if ((this.f13671h & 8) == 8 && this.f13675l != q.S()) {
                this.f13675l = q.t0(this.f13675l).f(qVar).o();
            } else {
                this.f13675l = qVar;
            }
            this.f13671h |= 8;
            return this;
        }
    }

    static {
        n nVar = new n(true);
        f13651z = nVar;
        nVar.t0();
    }

    public static n T() {
        return f13651z;
    }

    private void t0() {
        this.f13654i = DataLinkConstants.ATOM;
        this.f13655j = 2054;
        this.f13656k = 0;
        this.f13657l = q.S();
        this.f13658m = 0;
        this.f13659n = Collections.emptyList();
        this.f13660o = q.S();
        this.f13661p = 0;
        this.f13662q = Collections.emptyList();
        this.f13663r = Collections.emptyList();
        this.f13665t = u.D();
        this.f13666u = 0;
        this.f13667v = 0;
        this.f13668w = Collections.emptyList();
    }

    public static b u0() {
        return b.l();
    }

    public static b v0(n nVar) {
        return u0().f(nVar);
    }

    public q P(int i10) {
        return this.f13662q.get(i10);
    }

    public int Q() {
        return this.f13662q.size();
    }

    public List<Integer> R() {
        return this.f13663r;
    }

    public List<q> S() {
        return this.f13662q;
    }

    @Override // qc.r
    /* renamed from: U, reason: merged with bridge method [inline-methods] */
    public n getDefaultInstanceForType() {
        return f13651z;
    }

    public int V() {
        return this.f13654i;
    }

    public int W() {
        return this.f13666u;
    }

    public int X() {
        return this.f13656k;
    }

    public int Y() {
        return this.f13655j;
    }

    public q Z() {
        return this.f13660o;
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        i.d<MessageType>.a t7 = t();
        if ((this.f13653h & 2) == 2) {
            fVar.a0(1, this.f13655j);
        }
        if ((this.f13653h & 4) == 4) {
            fVar.a0(2, this.f13656k);
        }
        if ((this.f13653h & 8) == 8) {
            fVar.d0(3, this.f13657l);
        }
        for (int i10 = 0; i10 < this.f13659n.size(); i10++) {
            fVar.d0(4, this.f13659n.get(i10));
        }
        if ((this.f13653h & 32) == 32) {
            fVar.d0(5, this.f13660o);
        }
        if ((this.f13653h & 128) == 128) {
            fVar.d0(6, this.f13665t);
        }
        if ((this.f13653h & 256) == 256) {
            fVar.a0(7, this.f13666u);
        }
        if ((this.f13653h & 512) == 512) {
            fVar.a0(8, this.f13667v);
        }
        if ((this.f13653h & 16) == 16) {
            fVar.a0(9, this.f13658m);
        }
        if ((this.f13653h & 64) == 64) {
            fVar.a0(10, this.f13661p);
        }
        if ((this.f13653h & 1) == 1) {
            fVar.a0(11, this.f13654i);
        }
        for (int i11 = 0; i11 < this.f13662q.size(); i11++) {
            fVar.d0(12, this.f13662q.get(i11));
        }
        if (R().size() > 0) {
            fVar.o0(106);
            fVar.o0(this.f13664s);
        }
        for (int i12 = 0; i12 < this.f13663r.size(); i12++) {
            fVar.b0(this.f13663r.get(i12).intValue());
        }
        for (int i13 = 0; i13 < this.f13668w.size(); i13++) {
            fVar.a0(31, this.f13668w.get(i13).intValue());
        }
        t7.a(19000, fVar);
        fVar.i0(this.f13652g);
    }

    public int a0() {
        return this.f13661p;
    }

    public q b0() {
        return this.f13657l;
    }

    public int c0() {
        return this.f13658m;
    }

    public int d0() {
        return this.f13667v;
    }

    public u e0() {
        return this.f13665t;
    }

    public s f0(int i10) {
        return this.f13659n.get(i10);
    }

    public int g0() {
        return this.f13659n.size();
    }

    @Override // qc.i, qc.q
    public qc.s<n> getParserForType() {
        return A;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13670y;
        if (i10 != -1) {
            return i10;
        }
        int o10 = (this.f13653h & 2) == 2 ? qc.f.o(1, this.f13655j) + 0 : 0;
        if ((this.f13653h & 4) == 4) {
            o10 += qc.f.o(2, this.f13656k);
        }
        if ((this.f13653h & 8) == 8) {
            o10 += qc.f.s(3, this.f13657l);
        }
        for (int i11 = 0; i11 < this.f13659n.size(); i11++) {
            o10 += qc.f.s(4, this.f13659n.get(i11));
        }
        if ((this.f13653h & 32) == 32) {
            o10 += qc.f.s(5, this.f13660o);
        }
        if ((this.f13653h & 128) == 128) {
            o10 += qc.f.s(6, this.f13665t);
        }
        if ((this.f13653h & 256) == 256) {
            o10 += qc.f.o(7, this.f13666u);
        }
        if ((this.f13653h & 512) == 512) {
            o10 += qc.f.o(8, this.f13667v);
        }
        if ((this.f13653h & 16) == 16) {
            o10 += qc.f.o(9, this.f13658m);
        }
        if ((this.f13653h & 64) == 64) {
            o10 += qc.f.o(10, this.f13661p);
        }
        if ((this.f13653h & 1) == 1) {
            o10 += qc.f.o(11, this.f13654i);
        }
        for (int i12 = 0; i12 < this.f13662q.size(); i12++) {
            o10 += qc.f.s(12, this.f13662q.get(i12));
        }
        int i13 = 0;
        for (int i14 = 0; i14 < this.f13663r.size(); i14++) {
            i13 += qc.f.p(this.f13663r.get(i14).intValue());
        }
        int i15 = o10 + i13;
        if (!R().isEmpty()) {
            i15 = i15 + 1 + qc.f.p(i13);
        }
        this.f13664s = i13;
        int i16 = 0;
        for (int i17 = 0; i17 < this.f13668w.size(); i17++) {
            i16 += qc.f.p(this.f13668w.get(i17).intValue());
        }
        int size = i15 + i16 + (i0().size() * 2) + o() + this.f13652g.size();
        this.f13670y = size;
        return size;
    }

    public List<s> h0() {
        return this.f13659n;
    }

    public List<Integer> i0() {
        return this.f13668w;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13669x;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        if (!l0()) {
            this.f13669x = (byte) 0;
            return false;
        }
        if (p0() && !b0().isInitialized()) {
            this.f13669x = (byte) 0;
            return false;
        }
        for (int i10 = 0; i10 < g0(); i10++) {
            if (!f0(i10).isInitialized()) {
                this.f13669x = (byte) 0;
                return false;
            }
        }
        if (n0() && !Z().isInitialized()) {
            this.f13669x = (byte) 0;
            return false;
        }
        for (int i11 = 0; i11 < Q(); i11++) {
            if (!P(i11).isInitialized()) {
                this.f13669x = (byte) 0;
                return false;
            }
        }
        if (s0() && !e0().isInitialized()) {
            this.f13669x = (byte) 0;
            return false;
        }
        if (!n()) {
            this.f13669x = (byte) 0;
            return false;
        }
        this.f13669x = (byte) 1;
        return true;
    }

    public boolean j0() {
        return (this.f13653h & 1) == 1;
    }

    public boolean k0() {
        return (this.f13653h & 256) == 256;
    }

    public boolean l0() {
        return (this.f13653h & 4) == 4;
    }

    public boolean m0() {
        return (this.f13653h & 2) == 2;
    }

    public boolean n0() {
        return (this.f13653h & 32) == 32;
    }

    public boolean o0() {
        return (this.f13653h & 64) == 64;
    }

    public boolean p0() {
        return (this.f13653h & 8) == 8;
    }

    public boolean q0() {
        return (this.f13653h & 16) == 16;
    }

    public boolean r0() {
        return (this.f13653h & 512) == 512;
    }

    public boolean s0() {
        return (this.f13653h & 128) == 128;
    }

    @Override // qc.q
    /* renamed from: w0, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return u0();
    }

    @Override // qc.q
    /* renamed from: x0, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return v0(this);
    }

    private n(i.c<n, ?> cVar) {
        super(cVar);
        this.f13664s = -1;
        this.f13669x = (byte) -1;
        this.f13670y = -1;
        this.f13652g = cVar.e();
    }

    private n(boolean z10) {
        this.f13664s = -1;
        this.f13669x = (byte) -1;
        this.f13670y = -1;
        this.f13652g = qc.d.f17259e;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0027. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v11 */
    /* JADX WARN: Type inference failed for: r4v14 */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v5 */
    /* JADX WARN: Type inference failed for: r4v7 */
    /* JADX WARN: Type inference failed for: r4v9 */
    /* JADX WARN: Type inference failed for: r5v0 */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v2, types: [boolean] */
    private n(qc.e eVar, qc.g gVar) {
        this.f13664s = -1;
        this.f13669x = (byte) -1;
        this.f13670y = -1;
        t0();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        char c10 = 0;
        while (true) {
            ?? r52 = 256;
            if (!z10) {
                try {
                    try {
                        try {
                            int K = eVar.K();
                            switch (K) {
                                case 0:
                                    z10 = true;
                                case 8:
                                    this.f13653h |= 2;
                                    this.f13655j = eVar.s();
                                case 16:
                                    this.f13653h |= 4;
                                    this.f13656k = eVar.s();
                                case 26:
                                    q.c builder = (this.f13653h & 8) == 8 ? this.f13657l.toBuilder() : null;
                                    q qVar = (q) eVar.u(q.f13722z, gVar);
                                    this.f13657l = qVar;
                                    if (builder != null) {
                                        builder.f(qVar);
                                        this.f13657l = builder.o();
                                    }
                                    this.f13653h |= 8;
                                case 34:
                                    int i10 = (c10 == true ? 1 : 0) & 32;
                                    c10 = c10;
                                    if (i10 != 32) {
                                        this.f13659n = new ArrayList();
                                        c10 = (c10 == true ? 1 : 0) | ' ';
                                    }
                                    this.f13659n.add(eVar.u(s.f13802s, gVar));
                                case 42:
                                    q.c builder2 = (this.f13653h & 32) == 32 ? this.f13660o.toBuilder() : null;
                                    q qVar2 = (q) eVar.u(q.f13722z, gVar);
                                    this.f13660o = qVar2;
                                    if (builder2 != null) {
                                        builder2.f(qVar2);
                                        this.f13660o = builder2.o();
                                    }
                                    this.f13653h |= 32;
                                case 50:
                                    u.b builder3 = (this.f13653h & 128) == 128 ? this.f13665t.toBuilder() : null;
                                    u uVar = (u) eVar.u(u.f13839r, gVar);
                                    this.f13665t = uVar;
                                    if (builder3 != null) {
                                        builder3.f(uVar);
                                        this.f13665t = builder3.o();
                                    }
                                    this.f13653h |= 128;
                                case 56:
                                    this.f13653h |= 256;
                                    this.f13666u = eVar.s();
                                case 64:
                                    this.f13653h |= 512;
                                    this.f13667v = eVar.s();
                                case 72:
                                    this.f13653h |= 16;
                                    this.f13658m = eVar.s();
                                case 80:
                                    this.f13653h |= 64;
                                    this.f13661p = eVar.s();
                                case 88:
                                    this.f13653h |= 1;
                                    this.f13654i = eVar.s();
                                case 98:
                                    int i11 = (c10 == true ? 1 : 0) & 256;
                                    c10 = c10;
                                    if (i11 != 256) {
                                        this.f13662q = new ArrayList();
                                        c10 = (c10 == true ? 1 : 0) | 256;
                                    }
                                    this.f13662q.add(eVar.u(q.f13722z, gVar));
                                case 104:
                                    int i12 = (c10 == true ? 1 : 0) & 512;
                                    c10 = c10;
                                    if (i12 != 512) {
                                        this.f13663r = new ArrayList();
                                        c10 = (c10 == true ? 1 : 0) | 512;
                                    }
                                    this.f13663r.add(Integer.valueOf(eVar.s()));
                                case 106:
                                    int j10 = eVar.j(eVar.A());
                                    int i13 = (c10 == true ? 1 : 0) & 512;
                                    c10 = c10;
                                    if (i13 != 512) {
                                        c10 = c10;
                                        if (eVar.e() > 0) {
                                            this.f13663r = new ArrayList();
                                            c10 = (c10 == true ? 1 : 0) | 512;
                                        }
                                    }
                                    while (eVar.e() > 0) {
                                        this.f13663r.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j10);
                                case 248:
                                    int i14 = (c10 == true ? 1 : 0) & 8192;
                                    c10 = c10;
                                    if (i14 != 8192) {
                                        this.f13668w = new ArrayList();
                                        c10 = (c10 == true ? 1 : 0) | 8192;
                                    }
                                    this.f13668w.add(Integer.valueOf(eVar.s()));
                                case 250:
                                    int j11 = eVar.j(eVar.A());
                                    int i15 = (c10 == true ? 1 : 0) & 8192;
                                    c10 = c10;
                                    if (i15 != 8192) {
                                        c10 = c10;
                                        if (eVar.e() > 0) {
                                            this.f13668w = new ArrayList();
                                            c10 = (c10 == true ? 1 : 0) | 8192;
                                        }
                                    }
                                    while (eVar.e() > 0) {
                                        this.f13668w.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j11);
                                default:
                                    r52 = j(eVar, J, gVar, K);
                                    if (r52 == 0) {
                                        z10 = true;
                                    }
                            }
                        } catch (IOException e10) {
                            throw new qc.k(e10.getMessage()).i(this);
                        }
                    } catch (qc.k e11) {
                        throw e11.i(this);
                    }
                } catch (Throwable th) {
                    if (((c10 == true ? 1 : 0) & 32) == 32) {
                        this.f13659n = Collections.unmodifiableList(this.f13659n);
                    }
                    if (((c10 == true ? 1 : 0) & 256) == r52) {
                        this.f13662q = Collections.unmodifiableList(this.f13662q);
                    }
                    if (((c10 == true ? 1 : 0) & 512) == 512) {
                        this.f13663r = Collections.unmodifiableList(this.f13663r);
                    }
                    if (((c10 == true ? 1 : 0) & 8192) == 8192) {
                        this.f13668w = Collections.unmodifiableList(this.f13668w);
                    }
                    try {
                        J.I();
                    } catch (IOException unused) {
                    } catch (Throwable th2) {
                        this.f13652g = q10.v();
                        throw th2;
                    }
                    this.f13652g = q10.v();
                    g();
                    throw th;
                }
            } else {
                if (((c10 == true ? 1 : 0) & 32) == 32) {
                    this.f13659n = Collections.unmodifiableList(this.f13659n);
                }
                if (((c10 == true ? 1 : 0) & 256) == 256) {
                    this.f13662q = Collections.unmodifiableList(this.f13662q);
                }
                if (((c10 == true ? 1 : 0) & 512) == 512) {
                    this.f13663r = Collections.unmodifiableList(this.f13663r);
                }
                if (((c10 == true ? 1 : 0) & 8192) == 8192) {
                    this.f13668w = Collections.unmodifiableList(this.f13668w);
                }
                try {
                    J.I();
                } catch (IOException unused2) {
                } catch (Throwable th3) {
                    this.f13652g = q10.v();
                    throw th3;
                }
                this.f13652g = q10.v();
                g();
                return;
            }
        }
    }
}
