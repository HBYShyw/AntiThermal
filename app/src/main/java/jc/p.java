package jc;

import java.io.IOException;
import qc.a;
import qc.d;
import qc.i;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class p extends qc.i implements qc.r {

    /* renamed from: j, reason: collision with root package name */
    private static final p f13713j;

    /* renamed from: k, reason: collision with root package name */
    public static qc.s<p> f13714k = new a();

    /* renamed from: f, reason: collision with root package name */
    private final qc.d f13715f;

    /* renamed from: g, reason: collision with root package name */
    private qc.o f13716g;

    /* renamed from: h, reason: collision with root package name */
    private byte f13717h;

    /* renamed from: i, reason: collision with root package name */
    private int f13718i;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<p> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public p a(qc.e eVar, qc.g gVar) {
            return new p(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.b<p, b> implements qc.r {

        /* renamed from: f, reason: collision with root package name */
        private int f13719f;

        /* renamed from: g, reason: collision with root package name */
        private qc.o f13720g = qc.n.f17325f;

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
            if ((this.f13719f & 1) != 1) {
                this.f13720g = new qc.n(this.f13720g);
                this.f13719f |= 1;
            }
        }

        private void o() {
        }

        @Override // qc.q.a
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public p build() {
            p j10 = j();
            if (j10.isInitialized()) {
                return j10;
            }
            throw a.AbstractC0092a.c(j10);
        }

        public p j() {
            p pVar = new p(this);
            if ((this.f13719f & 1) == 1) {
                this.f13720g = this.f13720g.getUnmodifiableView();
                this.f13719f &= -2;
            }
            pVar.f13716g = this.f13720g;
            return pVar;
        }

        @Override // qc.i.b
        /* renamed from: k, reason: merged with bridge method [inline-methods] */
        public b d() {
            return l().f(j());
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b f(p pVar) {
            if (pVar == p.p()) {
                return this;
            }
            if (!pVar.f13716g.isEmpty()) {
                if (this.f13720g.isEmpty()) {
                    this.f13720g = pVar.f13716g;
                    this.f13719f &= -2;
                } else {
                    n();
                    this.f13720g.addAll(pVar.f13716g);
                }
            }
            g(e().d(pVar.f13715f));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: q, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            p pVar = null;
            try {
                try {
                    p a10 = p.f13714k.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (pVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                p pVar2 = (p) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    pVar = pVar2;
                    if (pVar != null) {
                        f(pVar);
                    }
                    throw th;
                }
            }
        }
    }

    static {
        p pVar = new p(true);
        f13713j = pVar;
        pVar.s();
    }

    public static p p() {
        return f13713j;
    }

    private void s() {
        this.f13716g = qc.n.f17325f;
    }

    public static b t() {
        return b.h();
    }

    public static b u(p pVar) {
        return t().f(pVar);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        for (int i10 = 0; i10 < this.f13716g.size(); i10++) {
            fVar.O(1, this.f13716g.getByteString(i10));
        }
        fVar.i0(this.f13715f);
    }

    @Override // qc.i, qc.q
    public qc.s<p> getParserForType() {
        return f13714k;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13718i;
        if (i10 != -1) {
            return i10;
        }
        int i11 = 0;
        for (int i12 = 0; i12 < this.f13716g.size(); i12++) {
            i11 += qc.f.e(this.f13716g.getByteString(i12));
        }
        int size = 0 + i11 + (r().size() * 1) + this.f13715f.size();
        this.f13718i = size;
        return size;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13717h;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        this.f13717h = (byte) 1;
        return true;
    }

    public String q(int i10) {
        return this.f13716g.get(i10);
    }

    public qc.t r() {
        return this.f13716g;
    }

    @Override // qc.q
    /* renamed from: v, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return t();
    }

    @Override // qc.q
    /* renamed from: w, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return u(this);
    }

    private p(i.b bVar) {
        super(bVar);
        this.f13717h = (byte) -1;
        this.f13718i = -1;
        this.f13715f = bVar.e();
    }

    private p(boolean z10) {
        this.f13717h = (byte) -1;
        this.f13718i = -1;
        this.f13715f = qc.d.f17259e;
    }

    private p(qc.e eVar, qc.g gVar) {
        this.f13717h = (byte) -1;
        this.f13718i = -1;
        s();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        boolean z11 = false;
        while (!z10) {
            try {
                try {
                    int K = eVar.K();
                    if (K != 0) {
                        if (K != 10) {
                            if (!j(eVar, J, gVar, K)) {
                            }
                        } else {
                            qc.d l10 = eVar.l();
                            if (!(z11 & true)) {
                                this.f13716g = new qc.n();
                                z11 |= true;
                            }
                            this.f13716g.b(l10);
                        }
                    }
                    z10 = true;
                } catch (qc.k e10) {
                    throw e10.i(this);
                } catch (IOException e11) {
                    throw new qc.k(e11.getMessage()).i(this);
                }
            } catch (Throwable th) {
                if (z11 & true) {
                    this.f13716g = this.f13716g.getUnmodifiableView();
                }
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13715f = q10.v();
                    throw th2;
                }
                this.f13715f = q10.v();
                g();
                throw th;
            }
        }
        if (z11 & true) {
            this.f13716g = this.f13716g.getUnmodifiableView();
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13715f = q10.v();
            throw th3;
        }
        this.f13715f = q10.v();
        g();
    }
}
