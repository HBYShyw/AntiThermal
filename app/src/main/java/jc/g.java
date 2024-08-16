package jc;

import java.io.IOException;
import qc.a;
import qc.d;
import qc.i;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class g extends i.d<g> {

    /* renamed from: l, reason: collision with root package name */
    private static final g f13534l;

    /* renamed from: m, reason: collision with root package name */
    public static qc.s<g> f13535m = new a();

    /* renamed from: g, reason: collision with root package name */
    private final qc.d f13536g;

    /* renamed from: h, reason: collision with root package name */
    private int f13537h;

    /* renamed from: i, reason: collision with root package name */
    private int f13538i;

    /* renamed from: j, reason: collision with root package name */
    private byte f13539j;

    /* renamed from: k, reason: collision with root package name */
    private int f13540k;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<g> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public g a(qc.e eVar, qc.g gVar) {
            return new g(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.c<g, b> {

        /* renamed from: h, reason: collision with root package name */
        private int f13541h;

        /* renamed from: i, reason: collision with root package name */
        private int f13542i;

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
        public g build() {
            g o10 = o();
            if (o10.isInitialized()) {
                return o10;
            }
            throw a.AbstractC0092a.c(o10);
        }

        public g o() {
            g gVar = new g(this);
            int i10 = (this.f13541h & 1) != 1 ? 0 : 1;
            gVar.f13538i = this.f13542i;
            gVar.f13537h = i10;
            return gVar;
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b d() {
            return q().f(o());
        }

        @Override // qc.i.b
        /* renamed from: s, reason: merged with bridge method [inline-methods] */
        public b f(g gVar) {
            if (gVar == g.y()) {
                return this;
            }
            if (gVar.B()) {
                u(gVar.A());
            }
            k(gVar);
            g(e().d(gVar.f13536g));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: t, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            g gVar2 = null;
            try {
                try {
                    g a10 = g.f13535m.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (gVar2 != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                g gVar3 = (g) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    gVar2 = gVar3;
                    if (gVar2 != null) {
                        f(gVar2);
                    }
                    throw th;
                }
            }
        }

        public b u(int i10) {
            this.f13541h |= 1;
            this.f13542i = i10;
            return this;
        }
    }

    static {
        g gVar = new g(true);
        f13534l = gVar;
        gVar.C();
    }

    private void C() {
        this.f13538i = 0;
    }

    public static b D() {
        return b.l();
    }

    public static b E(g gVar) {
        return D().f(gVar);
    }

    public static g y() {
        return f13534l;
    }

    public int A() {
        return this.f13538i;
    }

    public boolean B() {
        return (this.f13537h & 1) == 1;
    }

    @Override // qc.q
    /* renamed from: F, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return D();
    }

    @Override // qc.q
    /* renamed from: G, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return E(this);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        i.d<MessageType>.a t7 = t();
        if ((this.f13537h & 1) == 1) {
            fVar.a0(1, this.f13538i);
        }
        t7.a(200, fVar);
        fVar.i0(this.f13536g);
    }

    @Override // qc.i, qc.q
    public qc.s<g> getParserForType() {
        return f13535m;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13540k;
        if (i10 != -1) {
            return i10;
        }
        int o10 = ((this.f13537h & 1) == 1 ? 0 + qc.f.o(1, this.f13538i) : 0) + o() + this.f13536g.size();
        this.f13540k = o10;
        return o10;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13539j;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        if (!n()) {
            this.f13539j = (byte) 0;
            return false;
        }
        this.f13539j = (byte) 1;
        return true;
    }

    @Override // qc.r
    /* renamed from: z, reason: merged with bridge method [inline-methods] */
    public g getDefaultInstanceForType() {
        return f13534l;
    }

    private g(i.c<g, ?> cVar) {
        super(cVar);
        this.f13539j = (byte) -1;
        this.f13540k = -1;
        this.f13536g = cVar.e();
    }

    private g(boolean z10) {
        this.f13539j = (byte) -1;
        this.f13540k = -1;
        this.f13536g = qc.d.f17259e;
    }

    private g(qc.e eVar, qc.g gVar) {
        this.f13539j = (byte) -1;
        this.f13540k = -1;
        C();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        while (!z10) {
            try {
                try {
                    int K = eVar.K();
                    if (K != 0) {
                        if (K != 8) {
                            if (!j(eVar, J, gVar, K)) {
                            }
                        } else {
                            this.f13537h |= 1;
                            this.f13538i = eVar.s();
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
                    this.f13536g = q10.v();
                    throw th2;
                }
                this.f13536g = q10.v();
                g();
                throw th;
            }
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13536g = q10.v();
            throw th3;
        }
        this.f13536g = q10.v();
        g();
    }
}
