package jc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jc.q;
import qc.a;
import qc.d;
import qc.i;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class r extends i.d<r> {

    /* renamed from: t, reason: collision with root package name */
    private static final r f13776t;

    /* renamed from: u, reason: collision with root package name */
    public static qc.s<r> f13777u = new a();

    /* renamed from: g, reason: collision with root package name */
    private final qc.d f13778g;

    /* renamed from: h, reason: collision with root package name */
    private int f13779h;

    /* renamed from: i, reason: collision with root package name */
    private int f13780i;

    /* renamed from: j, reason: collision with root package name */
    private int f13781j;

    /* renamed from: k, reason: collision with root package name */
    private List<s> f13782k;

    /* renamed from: l, reason: collision with root package name */
    private q f13783l;

    /* renamed from: m, reason: collision with root package name */
    private int f13784m;

    /* renamed from: n, reason: collision with root package name */
    private q f13785n;

    /* renamed from: o, reason: collision with root package name */
    private int f13786o;

    /* renamed from: p, reason: collision with root package name */
    private List<jc.b> f13787p;

    /* renamed from: q, reason: collision with root package name */
    private List<Integer> f13788q;

    /* renamed from: r, reason: collision with root package name */
    private byte f13789r;

    /* renamed from: s, reason: collision with root package name */
    private int f13790s;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<r> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public r a(qc.e eVar, qc.g gVar) {
            return new r(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.c<r, b> {

        /* renamed from: h, reason: collision with root package name */
        private int f13791h;

        /* renamed from: j, reason: collision with root package name */
        private int f13793j;

        /* renamed from: m, reason: collision with root package name */
        private int f13796m;

        /* renamed from: o, reason: collision with root package name */
        private int f13798o;

        /* renamed from: i, reason: collision with root package name */
        private int f13792i = 6;

        /* renamed from: k, reason: collision with root package name */
        private List<s> f13794k = Collections.emptyList();

        /* renamed from: l, reason: collision with root package name */
        private q f13795l = q.S();

        /* renamed from: n, reason: collision with root package name */
        private q f13797n = q.S();

        /* renamed from: p, reason: collision with root package name */
        private List<jc.b> f13799p = Collections.emptyList();

        /* renamed from: q, reason: collision with root package name */
        private List<Integer> f13800q = Collections.emptyList();

        private b() {
            u();
        }

        static /* synthetic */ b l() {
            return q();
        }

        private static b q() {
            return new b();
        }

        private void r() {
            if ((this.f13791h & 128) != 128) {
                this.f13799p = new ArrayList(this.f13799p);
                this.f13791h |= 128;
            }
        }

        private void s() {
            if ((this.f13791h & 4) != 4) {
                this.f13794k = new ArrayList(this.f13794k);
                this.f13791h |= 4;
            }
        }

        private void t() {
            if ((this.f13791h & 256) != 256) {
                this.f13800q = new ArrayList(this.f13800q);
                this.f13791h |= 256;
            }
        }

        private void u() {
        }

        public b A(int i10) {
            this.f13791h |= 1;
            this.f13792i = i10;
            return this;
        }

        public b B(int i10) {
            this.f13791h |= 2;
            this.f13793j = i10;
            return this;
        }

        public b C(int i10) {
            this.f13791h |= 16;
            this.f13796m = i10;
            return this;
        }

        @Override // qc.q.a
        /* renamed from: n, reason: merged with bridge method [inline-methods] */
        public r build() {
            r o10 = o();
            if (o10.isInitialized()) {
                return o10;
            }
            throw a.AbstractC0092a.c(o10);
        }

        public r o() {
            r rVar = new r(this);
            int i10 = this.f13791h;
            int i11 = (i10 & 1) != 1 ? 0 : 1;
            rVar.f13780i = this.f13792i;
            if ((i10 & 2) == 2) {
                i11 |= 2;
            }
            rVar.f13781j = this.f13793j;
            if ((this.f13791h & 4) == 4) {
                this.f13794k = Collections.unmodifiableList(this.f13794k);
                this.f13791h &= -5;
            }
            rVar.f13782k = this.f13794k;
            if ((i10 & 8) == 8) {
                i11 |= 4;
            }
            rVar.f13783l = this.f13795l;
            if ((i10 & 16) == 16) {
                i11 |= 8;
            }
            rVar.f13784m = this.f13796m;
            if ((i10 & 32) == 32) {
                i11 |= 16;
            }
            rVar.f13785n = this.f13797n;
            if ((i10 & 64) == 64) {
                i11 |= 32;
            }
            rVar.f13786o = this.f13798o;
            if ((this.f13791h & 128) == 128) {
                this.f13799p = Collections.unmodifiableList(this.f13799p);
                this.f13791h &= -129;
            }
            rVar.f13787p = this.f13799p;
            if ((this.f13791h & 256) == 256) {
                this.f13800q = Collections.unmodifiableList(this.f13800q);
                this.f13791h &= -257;
            }
            rVar.f13788q = this.f13800q;
            rVar.f13779h = i11;
            return rVar;
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b d() {
            return q().f(o());
        }

        public b v(q qVar) {
            if ((this.f13791h & 32) == 32 && this.f13797n != q.S()) {
                this.f13797n = q.t0(this.f13797n).f(qVar).o();
            } else {
                this.f13797n = qVar;
            }
            this.f13791h |= 32;
            return this;
        }

        @Override // qc.i.b
        /* renamed from: w, reason: merged with bridge method [inline-methods] */
        public b f(r rVar) {
            if (rVar == r.M()) {
                return this;
            }
            if (rVar.a0()) {
                A(rVar.Q());
            }
            if (rVar.b0()) {
                B(rVar.R());
            }
            if (!rVar.f13782k.isEmpty()) {
                if (this.f13794k.isEmpty()) {
                    this.f13794k = rVar.f13782k;
                    this.f13791h &= -5;
                } else {
                    s();
                    this.f13794k.addAll(rVar.f13782k);
                }
            }
            if (rVar.c0()) {
                y(rVar.V());
            }
            if (rVar.d0()) {
                C(rVar.W());
            }
            if (rVar.Y()) {
                v(rVar.O());
            }
            if (rVar.Z()) {
                z(rVar.P());
            }
            if (!rVar.f13787p.isEmpty()) {
                if (this.f13799p.isEmpty()) {
                    this.f13799p = rVar.f13787p;
                    this.f13791h &= -129;
                } else {
                    r();
                    this.f13799p.addAll(rVar.f13787p);
                }
            }
            if (!rVar.f13788q.isEmpty()) {
                if (this.f13800q.isEmpty()) {
                    this.f13800q = rVar.f13788q;
                    this.f13791h &= -257;
                } else {
                    t();
                    this.f13800q.addAll(rVar.f13788q);
                }
            }
            k(rVar);
            g(e().d(rVar.f13778g));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: x, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            r rVar = null;
            try {
                try {
                    r a10 = r.f13777u.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (rVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                r rVar2 = (r) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    rVar = rVar2;
                    if (rVar != null) {
                        f(rVar);
                    }
                    throw th;
                }
            }
        }

        public b y(q qVar) {
            if ((this.f13791h & 8) == 8 && this.f13795l != q.S()) {
                this.f13795l = q.t0(this.f13795l).f(qVar).o();
            } else {
                this.f13795l = qVar;
            }
            this.f13791h |= 8;
            return this;
        }

        public b z(int i10) {
            this.f13791h |= 64;
            this.f13798o = i10;
            return this;
        }
    }

    static {
        r rVar = new r(true);
        f13776t = rVar;
        rVar.e0();
    }

    public static r M() {
        return f13776t;
    }

    private void e0() {
        this.f13780i = 6;
        this.f13781j = 0;
        this.f13782k = Collections.emptyList();
        this.f13783l = q.S();
        this.f13784m = 0;
        this.f13785n = q.S();
        this.f13786o = 0;
        this.f13787p = Collections.emptyList();
        this.f13788q = Collections.emptyList();
    }

    public static b f0() {
        return b.l();
    }

    public static b g0(r rVar) {
        return f0().f(rVar);
    }

    public static r i0(InputStream inputStream, qc.g gVar) {
        return f13777u.b(inputStream, gVar);
    }

    public jc.b J(int i10) {
        return this.f13787p.get(i10);
    }

    public int K() {
        return this.f13787p.size();
    }

    public List<jc.b> L() {
        return this.f13787p;
    }

    @Override // qc.r
    /* renamed from: N, reason: merged with bridge method [inline-methods] */
    public r getDefaultInstanceForType() {
        return f13776t;
    }

    public q O() {
        return this.f13785n;
    }

    public int P() {
        return this.f13786o;
    }

    public int Q() {
        return this.f13780i;
    }

    public int R() {
        return this.f13781j;
    }

    public s S(int i10) {
        return this.f13782k.get(i10);
    }

    public int T() {
        return this.f13782k.size();
    }

    public List<s> U() {
        return this.f13782k;
    }

    public q V() {
        return this.f13783l;
    }

    public int W() {
        return this.f13784m;
    }

    public List<Integer> X() {
        return this.f13788q;
    }

    public boolean Y() {
        return (this.f13779h & 16) == 16;
    }

    public boolean Z() {
        return (this.f13779h & 32) == 32;
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        i.d<MessageType>.a t7 = t();
        if ((this.f13779h & 1) == 1) {
            fVar.a0(1, this.f13780i);
        }
        if ((this.f13779h & 2) == 2) {
            fVar.a0(2, this.f13781j);
        }
        for (int i10 = 0; i10 < this.f13782k.size(); i10++) {
            fVar.d0(3, this.f13782k.get(i10));
        }
        if ((this.f13779h & 4) == 4) {
            fVar.d0(4, this.f13783l);
        }
        if ((this.f13779h & 8) == 8) {
            fVar.a0(5, this.f13784m);
        }
        if ((this.f13779h & 16) == 16) {
            fVar.d0(6, this.f13785n);
        }
        if ((this.f13779h & 32) == 32) {
            fVar.a0(7, this.f13786o);
        }
        for (int i11 = 0; i11 < this.f13787p.size(); i11++) {
            fVar.d0(8, this.f13787p.get(i11));
        }
        for (int i12 = 0; i12 < this.f13788q.size(); i12++) {
            fVar.a0(31, this.f13788q.get(i12).intValue());
        }
        t7.a(200, fVar);
        fVar.i0(this.f13778g);
    }

    public boolean a0() {
        return (this.f13779h & 1) == 1;
    }

    public boolean b0() {
        return (this.f13779h & 2) == 2;
    }

    public boolean c0() {
        return (this.f13779h & 4) == 4;
    }

    public boolean d0() {
        return (this.f13779h & 8) == 8;
    }

    @Override // qc.i, qc.q
    public qc.s<r> getParserForType() {
        return f13777u;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13790s;
        if (i10 != -1) {
            return i10;
        }
        int o10 = (this.f13779h & 1) == 1 ? qc.f.o(1, this.f13780i) + 0 : 0;
        if ((this.f13779h & 2) == 2) {
            o10 += qc.f.o(2, this.f13781j);
        }
        for (int i11 = 0; i11 < this.f13782k.size(); i11++) {
            o10 += qc.f.s(3, this.f13782k.get(i11));
        }
        if ((this.f13779h & 4) == 4) {
            o10 += qc.f.s(4, this.f13783l);
        }
        if ((this.f13779h & 8) == 8) {
            o10 += qc.f.o(5, this.f13784m);
        }
        if ((this.f13779h & 16) == 16) {
            o10 += qc.f.s(6, this.f13785n);
        }
        if ((this.f13779h & 32) == 32) {
            o10 += qc.f.o(7, this.f13786o);
        }
        for (int i12 = 0; i12 < this.f13787p.size(); i12++) {
            o10 += qc.f.s(8, this.f13787p.get(i12));
        }
        int i13 = 0;
        for (int i14 = 0; i14 < this.f13788q.size(); i14++) {
            i13 += qc.f.p(this.f13788q.get(i14).intValue());
        }
        int size = o10 + i13 + (X().size() * 2) + o() + this.f13778g.size();
        this.f13790s = size;
        return size;
    }

    @Override // qc.q
    /* renamed from: h0, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return f0();
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13789r;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        if (!b0()) {
            this.f13789r = (byte) 0;
            return false;
        }
        for (int i10 = 0; i10 < T(); i10++) {
            if (!S(i10).isInitialized()) {
                this.f13789r = (byte) 0;
                return false;
            }
        }
        if (c0() && !V().isInitialized()) {
            this.f13789r = (byte) 0;
            return false;
        }
        if (Y() && !O().isInitialized()) {
            this.f13789r = (byte) 0;
            return false;
        }
        for (int i11 = 0; i11 < K(); i11++) {
            if (!J(i11).isInitialized()) {
                this.f13789r = (byte) 0;
                return false;
            }
        }
        if (!n()) {
            this.f13789r = (byte) 0;
            return false;
        }
        this.f13789r = (byte) 1;
        return true;
    }

    @Override // qc.q
    /* renamed from: j0, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return g0(this);
    }

    private r(i.c<r, ?> cVar) {
        super(cVar);
        this.f13789r = (byte) -1;
        this.f13790s = -1;
        this.f13778g = cVar.e();
    }

    private r(boolean z10) {
        this.f13789r = (byte) -1;
        this.f13790s = -1;
        this.f13778g = qc.d.f17259e;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0022. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v0 */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v2, types: [boolean] */
    private r(qc.e eVar, qc.g gVar) {
        q.c builder;
        this.f13789r = (byte) -1;
        this.f13790s = -1;
        e0();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        int i10 = 0;
        while (true) {
            ?? r52 = 128;
            if (!z10) {
                try {
                    try {
                        try {
                            int K = eVar.K();
                            switch (K) {
                                case 0:
                                    z10 = true;
                                case 8:
                                    this.f13779h |= 1;
                                    this.f13780i = eVar.s();
                                case 16:
                                    this.f13779h |= 2;
                                    this.f13781j = eVar.s();
                                case 26:
                                    if ((i10 & 4) != 4) {
                                        this.f13782k = new ArrayList();
                                        i10 |= 4;
                                    }
                                    this.f13782k.add(eVar.u(s.f13802s, gVar));
                                case 34:
                                    builder = (this.f13779h & 4) == 4 ? this.f13783l.toBuilder() : null;
                                    q qVar = (q) eVar.u(q.f13722z, gVar);
                                    this.f13783l = qVar;
                                    if (builder != null) {
                                        builder.f(qVar);
                                        this.f13783l = builder.o();
                                    }
                                    this.f13779h |= 4;
                                case 40:
                                    this.f13779h |= 8;
                                    this.f13784m = eVar.s();
                                case 50:
                                    builder = (this.f13779h & 16) == 16 ? this.f13785n.toBuilder() : null;
                                    q qVar2 = (q) eVar.u(q.f13722z, gVar);
                                    this.f13785n = qVar2;
                                    if (builder != null) {
                                        builder.f(qVar2);
                                        this.f13785n = builder.o();
                                    }
                                    this.f13779h |= 16;
                                case 56:
                                    this.f13779h |= 32;
                                    this.f13786o = eVar.s();
                                case 66:
                                    if ((i10 & 128) != 128) {
                                        this.f13787p = new ArrayList();
                                        i10 |= 128;
                                    }
                                    this.f13787p.add(eVar.u(jc.b.f13371m, gVar));
                                case 248:
                                    if ((i10 & 256) != 256) {
                                        this.f13788q = new ArrayList();
                                        i10 |= 256;
                                    }
                                    this.f13788q.add(Integer.valueOf(eVar.s()));
                                case 250:
                                    int j10 = eVar.j(eVar.A());
                                    if ((i10 & 256) != 256 && eVar.e() > 0) {
                                        this.f13788q = new ArrayList();
                                        i10 |= 256;
                                    }
                                    while (eVar.e() > 0) {
                                        this.f13788q.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j10);
                                    break;
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
                    if ((i10 & 4) == 4) {
                        this.f13782k = Collections.unmodifiableList(this.f13782k);
                    }
                    if ((i10 & 128) == r52) {
                        this.f13787p = Collections.unmodifiableList(this.f13787p);
                    }
                    if ((i10 & 256) == 256) {
                        this.f13788q = Collections.unmodifiableList(this.f13788q);
                    }
                    try {
                        J.I();
                    } catch (IOException unused) {
                    } catch (Throwable th2) {
                        this.f13778g = q10.v();
                        throw th2;
                    }
                    this.f13778g = q10.v();
                    g();
                    throw th;
                }
            } else {
                if ((i10 & 4) == 4) {
                    this.f13782k = Collections.unmodifiableList(this.f13782k);
                }
                if ((i10 & 128) == 128) {
                    this.f13787p = Collections.unmodifiableList(this.f13787p);
                }
                if ((i10 & 256) == 256) {
                    this.f13788q = Collections.unmodifiableList(this.f13788q);
                }
                try {
                    J.I();
                } catch (IOException unused2) {
                } catch (Throwable th3) {
                    this.f13778g = q10.v();
                    throw th3;
                }
                this.f13778g = q10.v();
                g();
                return;
            }
        }
    }
}
