package jc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jc.l;
import jc.o;
import jc.p;
import qc.a;
import qc.d;
import qc.i;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class m extends i.d<m> {

    /* renamed from: o, reason: collision with root package name */
    private static final m f13636o;

    /* renamed from: p, reason: collision with root package name */
    public static qc.s<m> f13637p = new a();

    /* renamed from: g, reason: collision with root package name */
    private final qc.d f13638g;

    /* renamed from: h, reason: collision with root package name */
    private int f13639h;

    /* renamed from: i, reason: collision with root package name */
    private p f13640i;

    /* renamed from: j, reason: collision with root package name */
    private o f13641j;

    /* renamed from: k, reason: collision with root package name */
    private l f13642k;

    /* renamed from: l, reason: collision with root package name */
    private List<c> f13643l;

    /* renamed from: m, reason: collision with root package name */
    private byte f13644m;

    /* renamed from: n, reason: collision with root package name */
    private int f13645n;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<m> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public m a(qc.e eVar, qc.g gVar) {
            return new m(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.c<m, b> {

        /* renamed from: h, reason: collision with root package name */
        private int f13646h;

        /* renamed from: i, reason: collision with root package name */
        private p f13647i = p.p();

        /* renamed from: j, reason: collision with root package name */
        private o f13648j = o.p();

        /* renamed from: k, reason: collision with root package name */
        private l f13649k = l.F();

        /* renamed from: l, reason: collision with root package name */
        private List<c> f13650l = Collections.emptyList();

        private b() {
            s();
        }

        static /* synthetic */ b l() {
            return q();
        }

        private static b q() {
            return new b();
        }

        private void r() {
            if ((this.f13646h & 8) != 8) {
                this.f13650l = new ArrayList(this.f13650l);
                this.f13646h |= 8;
            }
        }

        private void s() {
        }

        @Override // qc.q.a
        /* renamed from: n, reason: merged with bridge method [inline-methods] */
        public m build() {
            m o10 = o();
            if (o10.isInitialized()) {
                return o10;
            }
            throw a.AbstractC0092a.c(o10);
        }

        public m o() {
            m mVar = new m(this);
            int i10 = this.f13646h;
            int i11 = (i10 & 1) != 1 ? 0 : 1;
            mVar.f13640i = this.f13647i;
            if ((i10 & 2) == 2) {
                i11 |= 2;
            }
            mVar.f13641j = this.f13648j;
            if ((i10 & 4) == 4) {
                i11 |= 4;
            }
            mVar.f13642k = this.f13649k;
            if ((this.f13646h & 8) == 8) {
                this.f13650l = Collections.unmodifiableList(this.f13650l);
                this.f13646h &= -9;
            }
            mVar.f13643l = this.f13650l;
            mVar.f13639h = i11;
            return mVar;
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b d() {
            return q().f(o());
        }

        @Override // qc.i.b
        /* renamed from: t, reason: merged with bridge method [inline-methods] */
        public b f(m mVar) {
            if (mVar == m.F()) {
                return this;
            }
            if (mVar.M()) {
                x(mVar.J());
            }
            if (mVar.L()) {
                w(mVar.I());
            }
            if (mVar.K()) {
                v(mVar.H());
            }
            if (!mVar.f13643l.isEmpty()) {
                if (this.f13650l.isEmpty()) {
                    this.f13650l = mVar.f13643l;
                    this.f13646h &= -9;
                } else {
                    r();
                    this.f13650l.addAll(mVar.f13643l);
                }
            }
            k(mVar);
            g(e().d(mVar.f13638g));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: u, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            m mVar = null;
            try {
                try {
                    m a10 = m.f13637p.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (mVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                m mVar2 = (m) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    mVar = mVar2;
                    if (mVar != null) {
                        f(mVar);
                    }
                    throw th;
                }
            }
        }

        public b v(l lVar) {
            if ((this.f13646h & 4) == 4 && this.f13649k != l.F()) {
                this.f13649k = l.W(this.f13649k).f(lVar).o();
            } else {
                this.f13649k = lVar;
            }
            this.f13646h |= 4;
            return this;
        }

        public b w(o oVar) {
            if ((this.f13646h & 2) == 2 && this.f13648j != o.p()) {
                this.f13648j = o.u(this.f13648j).f(oVar).j();
            } else {
                this.f13648j = oVar;
            }
            this.f13646h |= 2;
            return this;
        }

        public b x(p pVar) {
            if ((this.f13646h & 1) == 1 && this.f13647i != p.p()) {
                this.f13647i = p.u(this.f13647i).f(pVar).j();
            } else {
                this.f13647i = pVar;
            }
            this.f13646h |= 1;
            return this;
        }
    }

    static {
        m mVar = new m(true);
        f13636o = mVar;
        mVar.N();
    }

    public static m F() {
        return f13636o;
    }

    private void N() {
        this.f13640i = p.p();
        this.f13641j = o.p();
        this.f13642k = l.F();
        this.f13643l = Collections.emptyList();
    }

    public static b O() {
        return b.l();
    }

    public static b P(m mVar) {
        return O().f(mVar);
    }

    public static m R(InputStream inputStream, qc.g gVar) {
        return f13637p.c(inputStream, gVar);
    }

    public c C(int i10) {
        return this.f13643l.get(i10);
    }

    public int D() {
        return this.f13643l.size();
    }

    public List<c> E() {
        return this.f13643l;
    }

    @Override // qc.r
    /* renamed from: G, reason: merged with bridge method [inline-methods] */
    public m getDefaultInstanceForType() {
        return f13636o;
    }

    public l H() {
        return this.f13642k;
    }

    public o I() {
        return this.f13641j;
    }

    public p J() {
        return this.f13640i;
    }

    public boolean K() {
        return (this.f13639h & 4) == 4;
    }

    public boolean L() {
        return (this.f13639h & 2) == 2;
    }

    public boolean M() {
        return (this.f13639h & 1) == 1;
    }

    @Override // qc.q
    /* renamed from: Q, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return O();
    }

    @Override // qc.q
    /* renamed from: S, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return P(this);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        i.d<MessageType>.a t7 = t();
        if ((this.f13639h & 1) == 1) {
            fVar.d0(1, this.f13640i);
        }
        if ((this.f13639h & 2) == 2) {
            fVar.d0(2, this.f13641j);
        }
        if ((this.f13639h & 4) == 4) {
            fVar.d0(3, this.f13642k);
        }
        for (int i10 = 0; i10 < this.f13643l.size(); i10++) {
            fVar.d0(4, this.f13643l.get(i10));
        }
        t7.a(200, fVar);
        fVar.i0(this.f13638g);
    }

    @Override // qc.i, qc.q
    public qc.s<m> getParserForType() {
        return f13637p;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13645n;
        if (i10 != -1) {
            return i10;
        }
        int s7 = (this.f13639h & 1) == 1 ? qc.f.s(1, this.f13640i) + 0 : 0;
        if ((this.f13639h & 2) == 2) {
            s7 += qc.f.s(2, this.f13641j);
        }
        if ((this.f13639h & 4) == 4) {
            s7 += qc.f.s(3, this.f13642k);
        }
        for (int i11 = 0; i11 < this.f13643l.size(); i11++) {
            s7 += qc.f.s(4, this.f13643l.get(i11));
        }
        int o10 = s7 + o() + this.f13638g.size();
        this.f13645n = o10;
        return o10;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13644m;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        if (L() && !I().isInitialized()) {
            this.f13644m = (byte) 0;
            return false;
        }
        if (K() && !H().isInitialized()) {
            this.f13644m = (byte) 0;
            return false;
        }
        for (int i10 = 0; i10 < D(); i10++) {
            if (!C(i10).isInitialized()) {
                this.f13644m = (byte) 0;
                return false;
            }
        }
        if (!n()) {
            this.f13644m = (byte) 0;
            return false;
        }
        this.f13644m = (byte) 1;
        return true;
    }

    private m(i.c<m, ?> cVar) {
        super(cVar);
        this.f13644m = (byte) -1;
        this.f13645n = -1;
        this.f13638g = cVar.e();
    }

    private m(boolean z10) {
        this.f13644m = (byte) -1;
        this.f13645n = -1;
        this.f13638g = qc.d.f17259e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v4 */
    private m(qc.e eVar, qc.g gVar) {
        this.f13644m = (byte) -1;
        this.f13645n = -1;
        N();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        char c10 = 0;
        while (!z10) {
            try {
                try {
                    int K = eVar.K();
                    if (K != 0) {
                        if (K == 10) {
                            p.b builder = (this.f13639h & 1) == 1 ? this.f13640i.toBuilder() : null;
                            p pVar = (p) eVar.u(p.f13714k, gVar);
                            this.f13640i = pVar;
                            if (builder != null) {
                                builder.f(pVar);
                                this.f13640i = builder.j();
                            }
                            this.f13639h |= 1;
                        } else if (K == 18) {
                            o.b builder2 = (this.f13639h & 2) == 2 ? this.f13641j.toBuilder() : null;
                            o oVar = (o) eVar.u(o.f13687k, gVar);
                            this.f13641j = oVar;
                            if (builder2 != null) {
                                builder2.f(oVar);
                                this.f13641j = builder2.j();
                            }
                            this.f13639h |= 2;
                        } else if (K == 26) {
                            l.b builder3 = (this.f13639h & 4) == 4 ? this.f13642k.toBuilder() : null;
                            l lVar = (l) eVar.u(l.f13620q, gVar);
                            this.f13642k = lVar;
                            if (builder3 != null) {
                                builder3.f(lVar);
                                this.f13642k = builder3.o();
                            }
                            this.f13639h |= 4;
                        } else if (K != 34) {
                            if (!j(eVar, J, gVar, K)) {
                            }
                        } else {
                            int i10 = (c10 == true ? 1 : 0) & 8;
                            c10 = c10;
                            if (i10 != 8) {
                                this.f13643l = new ArrayList();
                                c10 = (c10 == true ? 1 : 0) | '\b';
                            }
                            this.f13643l.add(eVar.u(c.P, gVar));
                        }
                    }
                    z10 = true;
                } catch (qc.k e10) {
                    throw e10.i(this);
                } catch (IOException e11) {
                    throw new qc.k(e11.getMessage()).i(this);
                }
            } catch (Throwable th) {
                if (((c10 == true ? 1 : 0) & 8) == 8) {
                    this.f13643l = Collections.unmodifiableList(this.f13643l);
                }
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13638g = q10.v();
                    throw th2;
                }
                this.f13638g = q10.v();
                g();
                throw th;
            }
        }
        if (((c10 == true ? 1 : 0) & 8) == 8) {
            this.f13643l = Collections.unmodifiableList(this.f13643l);
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13638g = q10.v();
            throw th3;
        }
        this.f13638g = q10.v();
        g();
    }
}
