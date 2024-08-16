package jc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jc.t;
import jc.w;
import qc.a;
import qc.d;
import qc.i;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class l extends i.d<l> {

    /* renamed from: p, reason: collision with root package name */
    private static final l f13619p;

    /* renamed from: q, reason: collision with root package name */
    public static qc.s<l> f13620q = new a();

    /* renamed from: g, reason: collision with root package name */
    private final qc.d f13621g;

    /* renamed from: h, reason: collision with root package name */
    private int f13622h;

    /* renamed from: i, reason: collision with root package name */
    private List<i> f13623i;

    /* renamed from: j, reason: collision with root package name */
    private List<n> f13624j;

    /* renamed from: k, reason: collision with root package name */
    private List<r> f13625k;

    /* renamed from: l, reason: collision with root package name */
    private t f13626l;

    /* renamed from: m, reason: collision with root package name */
    private w f13627m;

    /* renamed from: n, reason: collision with root package name */
    private byte f13628n;

    /* renamed from: o, reason: collision with root package name */
    private int f13629o;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<l> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public l a(qc.e eVar, qc.g gVar) {
            return new l(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.c<l, b> {

        /* renamed from: h, reason: collision with root package name */
        private int f13630h;

        /* renamed from: i, reason: collision with root package name */
        private List<i> f13631i = Collections.emptyList();

        /* renamed from: j, reason: collision with root package name */
        private List<n> f13632j = Collections.emptyList();

        /* renamed from: k, reason: collision with root package name */
        private List<r> f13633k = Collections.emptyList();

        /* renamed from: l, reason: collision with root package name */
        private t f13634l = t.r();

        /* renamed from: m, reason: collision with root package name */
        private w f13635m = w.p();

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
            if ((this.f13630h & 1) != 1) {
                this.f13631i = new ArrayList(this.f13631i);
                this.f13630h |= 1;
            }
        }

        private void s() {
            if ((this.f13630h & 2) != 2) {
                this.f13632j = new ArrayList(this.f13632j);
                this.f13630h |= 2;
            }
        }

        private void t() {
            if ((this.f13630h & 4) != 4) {
                this.f13633k = new ArrayList(this.f13633k);
                this.f13630h |= 4;
            }
        }

        private void u() {
        }

        @Override // qc.q.a
        /* renamed from: n, reason: merged with bridge method [inline-methods] */
        public l build() {
            l o10 = o();
            if (o10.isInitialized()) {
                return o10;
            }
            throw a.AbstractC0092a.c(o10);
        }

        public l o() {
            l lVar = new l(this);
            int i10 = this.f13630h;
            if ((i10 & 1) == 1) {
                this.f13631i = Collections.unmodifiableList(this.f13631i);
                this.f13630h &= -2;
            }
            lVar.f13623i = this.f13631i;
            if ((this.f13630h & 2) == 2) {
                this.f13632j = Collections.unmodifiableList(this.f13632j);
                this.f13630h &= -3;
            }
            lVar.f13624j = this.f13632j;
            if ((this.f13630h & 4) == 4) {
                this.f13633k = Collections.unmodifiableList(this.f13633k);
                this.f13630h &= -5;
            }
            lVar.f13625k = this.f13633k;
            int i11 = (i10 & 8) != 8 ? 0 : 1;
            lVar.f13626l = this.f13634l;
            if ((i10 & 16) == 16) {
                i11 |= 2;
            }
            lVar.f13627m = this.f13635m;
            lVar.f13622h = i11;
            return lVar;
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b d() {
            return q().f(o());
        }

        @Override // qc.i.b
        /* renamed from: v, reason: merged with bridge method [inline-methods] */
        public b f(l lVar) {
            if (lVar == l.F()) {
                return this;
            }
            if (!lVar.f13623i.isEmpty()) {
                if (this.f13631i.isEmpty()) {
                    this.f13631i = lVar.f13623i;
                    this.f13630h &= -2;
                } else {
                    r();
                    this.f13631i.addAll(lVar.f13623i);
                }
            }
            if (!lVar.f13624j.isEmpty()) {
                if (this.f13632j.isEmpty()) {
                    this.f13632j = lVar.f13624j;
                    this.f13630h &= -3;
                } else {
                    s();
                    this.f13632j.addAll(lVar.f13624j);
                }
            }
            if (!lVar.f13625k.isEmpty()) {
                if (this.f13633k.isEmpty()) {
                    this.f13633k = lVar.f13625k;
                    this.f13630h &= -5;
                } else {
                    t();
                    this.f13633k.addAll(lVar.f13625k);
                }
            }
            if (lVar.S()) {
                x(lVar.Q());
            }
            if (lVar.T()) {
                y(lVar.R());
            }
            k(lVar);
            g(e().d(lVar.f13621g));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: w, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            l lVar = null;
            try {
                try {
                    l a10 = l.f13620q.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (lVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                l lVar2 = (l) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    lVar = lVar2;
                    if (lVar != null) {
                        f(lVar);
                    }
                    throw th;
                }
            }
        }

        public b x(t tVar) {
            if ((this.f13630h & 8) == 8 && this.f13634l != t.r()) {
                this.f13634l = t.z(this.f13634l).f(tVar).j();
            } else {
                this.f13634l = tVar;
            }
            this.f13630h |= 8;
            return this;
        }

        public b y(w wVar) {
            if ((this.f13630h & 16) == 16 && this.f13635m != w.p()) {
                this.f13635m = w.u(this.f13635m).f(wVar).j();
            } else {
                this.f13635m = wVar;
            }
            this.f13630h |= 16;
            return this;
        }
    }

    static {
        l lVar = new l(true);
        f13619p = lVar;
        lVar.U();
    }

    public static l F() {
        return f13619p;
    }

    private void U() {
        this.f13623i = Collections.emptyList();
        this.f13624j = Collections.emptyList();
        this.f13625k = Collections.emptyList();
        this.f13626l = t.r();
        this.f13627m = w.p();
    }

    public static b V() {
        return b.l();
    }

    public static b W(l lVar) {
        return V().f(lVar);
    }

    public static l Y(InputStream inputStream, qc.g gVar) {
        return f13620q.c(inputStream, gVar);
    }

    @Override // qc.r
    /* renamed from: G, reason: merged with bridge method [inline-methods] */
    public l getDefaultInstanceForType() {
        return f13619p;
    }

    public i H(int i10) {
        return this.f13623i.get(i10);
    }

    public int I() {
        return this.f13623i.size();
    }

    public List<i> J() {
        return this.f13623i;
    }

    public n K(int i10) {
        return this.f13624j.get(i10);
    }

    public int L() {
        return this.f13624j.size();
    }

    public List<n> M() {
        return this.f13624j;
    }

    public r N(int i10) {
        return this.f13625k.get(i10);
    }

    public int O() {
        return this.f13625k.size();
    }

    public List<r> P() {
        return this.f13625k;
    }

    public t Q() {
        return this.f13626l;
    }

    public w R() {
        return this.f13627m;
    }

    public boolean S() {
        return (this.f13622h & 1) == 1;
    }

    public boolean T() {
        return (this.f13622h & 2) == 2;
    }

    @Override // qc.q
    /* renamed from: X, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return V();
    }

    @Override // qc.q
    /* renamed from: Z, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return W(this);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        i.d<MessageType>.a t7 = t();
        for (int i10 = 0; i10 < this.f13623i.size(); i10++) {
            fVar.d0(3, this.f13623i.get(i10));
        }
        for (int i11 = 0; i11 < this.f13624j.size(); i11++) {
            fVar.d0(4, this.f13624j.get(i11));
        }
        for (int i12 = 0; i12 < this.f13625k.size(); i12++) {
            fVar.d0(5, this.f13625k.get(i12));
        }
        if ((this.f13622h & 1) == 1) {
            fVar.d0(30, this.f13626l);
        }
        if ((this.f13622h & 2) == 2) {
            fVar.d0(32, this.f13627m);
        }
        t7.a(200, fVar);
        fVar.i0(this.f13621g);
    }

    @Override // qc.i, qc.q
    public qc.s<l> getParserForType() {
        return f13620q;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13629o;
        if (i10 != -1) {
            return i10;
        }
        int i11 = 0;
        for (int i12 = 0; i12 < this.f13623i.size(); i12++) {
            i11 += qc.f.s(3, this.f13623i.get(i12));
        }
        for (int i13 = 0; i13 < this.f13624j.size(); i13++) {
            i11 += qc.f.s(4, this.f13624j.get(i13));
        }
        for (int i14 = 0; i14 < this.f13625k.size(); i14++) {
            i11 += qc.f.s(5, this.f13625k.get(i14));
        }
        if ((this.f13622h & 1) == 1) {
            i11 += qc.f.s(30, this.f13626l);
        }
        if ((this.f13622h & 2) == 2) {
            i11 += qc.f.s(32, this.f13627m);
        }
        int o10 = i11 + o() + this.f13621g.size();
        this.f13629o = o10;
        return o10;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13628n;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        for (int i10 = 0; i10 < I(); i10++) {
            if (!H(i10).isInitialized()) {
                this.f13628n = (byte) 0;
                return false;
            }
        }
        for (int i11 = 0; i11 < L(); i11++) {
            if (!K(i11).isInitialized()) {
                this.f13628n = (byte) 0;
                return false;
            }
        }
        for (int i12 = 0; i12 < O(); i12++) {
            if (!N(i12).isInitialized()) {
                this.f13628n = (byte) 0;
                return false;
            }
        }
        if (S() && !Q().isInitialized()) {
            this.f13628n = (byte) 0;
            return false;
        }
        if (!n()) {
            this.f13628n = (byte) 0;
            return false;
        }
        this.f13628n = (byte) 1;
        return true;
    }

    private l(i.c<l, ?> cVar) {
        super(cVar);
        this.f13628n = (byte) -1;
        this.f13629o = -1;
        this.f13621g = cVar.e();
    }

    private l(boolean z10) {
        this.f13628n = (byte) -1;
        this.f13629o = -1;
        this.f13621g = qc.d.f17259e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v4 */
    /* JADX WARN: Type inference failed for: r4v6 */
    /* JADX WARN: Type inference failed for: r4v8 */
    private l(qc.e eVar, qc.g gVar) {
        this.f13628n = (byte) -1;
        this.f13629o = -1;
        U();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        char c10 = 0;
        while (!z10) {
            try {
                try {
                    int K = eVar.K();
                    if (K != 0) {
                        if (K == 26) {
                            int i10 = (c10 == true ? 1 : 0) & 1;
                            c10 = c10;
                            if (i10 != 1) {
                                this.f13623i = new ArrayList();
                                c10 = (c10 == true ? 1 : 0) | 1;
                            }
                            this.f13623i.add(eVar.u(i.A, gVar));
                        } else if (K == 34) {
                            int i11 = (c10 == true ? 1 : 0) & 2;
                            c10 = c10;
                            if (i11 != 2) {
                                this.f13624j = new ArrayList();
                                c10 = (c10 == true ? 1 : 0) | 2;
                            }
                            this.f13624j.add(eVar.u(n.A, gVar));
                        } else if (K != 42) {
                            if (K == 242) {
                                t.b builder = (this.f13622h & 1) == 1 ? this.f13626l.toBuilder() : null;
                                t tVar = (t) eVar.u(t.f13828m, gVar);
                                this.f13626l = tVar;
                                if (builder != null) {
                                    builder.f(tVar);
                                    this.f13626l = builder.j();
                                }
                                this.f13622h |= 1;
                            } else if (K != 258) {
                                if (!j(eVar, J, gVar, K)) {
                                }
                            } else {
                                w.b builder2 = (this.f13622h & 2) == 2 ? this.f13627m.toBuilder() : null;
                                w wVar = (w) eVar.u(w.f13889k, gVar);
                                this.f13627m = wVar;
                                if (builder2 != null) {
                                    builder2.f(wVar);
                                    this.f13627m = builder2.j();
                                }
                                this.f13622h |= 2;
                            }
                        } else {
                            int i12 = (c10 == true ? 1 : 0) & 4;
                            c10 = c10;
                            if (i12 != 4) {
                                this.f13625k = new ArrayList();
                                c10 = (c10 == true ? 1 : 0) | 4;
                            }
                            this.f13625k.add(eVar.u(r.f13777u, gVar));
                        }
                    }
                    z10 = true;
                } catch (qc.k e10) {
                    throw e10.i(this);
                } catch (IOException e11) {
                    throw new qc.k(e11.getMessage()).i(this);
                }
            } catch (Throwable th) {
                if (((c10 == true ? 1 : 0) & 1) == 1) {
                    this.f13623i = Collections.unmodifiableList(this.f13623i);
                }
                if (((c10 == true ? 1 : 0) & 2) == 2) {
                    this.f13624j = Collections.unmodifiableList(this.f13624j);
                }
                if (((c10 == true ? 1 : 0) & 4) == 4) {
                    this.f13625k = Collections.unmodifiableList(this.f13625k);
                }
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13621g = q10.v();
                    throw th2;
                }
                this.f13621g = q10.v();
                g();
                throw th;
            }
        }
        if (((c10 == true ? 1 : 0) & 1) == 1) {
            this.f13623i = Collections.unmodifiableList(this.f13623i);
        }
        if (((c10 == true ? 1 : 0) & 2) == 2) {
            this.f13624j = Collections.unmodifiableList(this.f13624j);
        }
        if (((c10 == true ? 1 : 0) & 4) == 4) {
            this.f13625k = Collections.unmodifiableList(this.f13625k);
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13621g = q10.v();
            throw th3;
        }
        this.f13621g = q10.v();
        g();
    }
}
