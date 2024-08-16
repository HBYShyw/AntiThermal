package jc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import qc.a;
import qc.d;
import qc.i;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class w extends qc.i implements qc.r {

    /* renamed from: j, reason: collision with root package name */
    private static final w f13888j;

    /* renamed from: k, reason: collision with root package name */
    public static qc.s<w> f13889k = new a();

    /* renamed from: f, reason: collision with root package name */
    private final qc.d f13890f;

    /* renamed from: g, reason: collision with root package name */
    private List<v> f13891g;

    /* renamed from: h, reason: collision with root package name */
    private byte f13892h;

    /* renamed from: i, reason: collision with root package name */
    private int f13893i;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<w> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public w a(qc.e eVar, qc.g gVar) {
            return new w(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.b<w, b> implements qc.r {

        /* renamed from: f, reason: collision with root package name */
        private int f13894f;

        /* renamed from: g, reason: collision with root package name */
        private List<v> f13895g = Collections.emptyList();

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
            if ((this.f13894f & 1) != 1) {
                this.f13895g = new ArrayList(this.f13895g);
                this.f13894f |= 1;
            }
        }

        private void o() {
        }

        @Override // qc.q.a
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public w build() {
            w j10 = j();
            if (j10.isInitialized()) {
                return j10;
            }
            throw a.AbstractC0092a.c(j10);
        }

        public w j() {
            w wVar = new w(this);
            if ((this.f13894f & 1) == 1) {
                this.f13895g = Collections.unmodifiableList(this.f13895g);
                this.f13894f &= -2;
            }
            wVar.f13891g = this.f13895g;
            return wVar;
        }

        @Override // qc.i.b
        /* renamed from: k, reason: merged with bridge method [inline-methods] */
        public b d() {
            return l().f(j());
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b f(w wVar) {
            if (wVar == w.p()) {
                return this;
            }
            if (!wVar.f13891g.isEmpty()) {
                if (this.f13895g.isEmpty()) {
                    this.f13895g = wVar.f13891g;
                    this.f13894f &= -2;
                } else {
                    n();
                    this.f13895g.addAll(wVar.f13891g);
                }
            }
            g(e().d(wVar.f13890f));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: q, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            w wVar = null;
            try {
                try {
                    w a10 = w.f13889k.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (wVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                w wVar2 = (w) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    wVar = wVar2;
                    if (wVar != null) {
                        f(wVar);
                    }
                    throw th;
                }
            }
        }
    }

    static {
        w wVar = new w(true);
        f13888j = wVar;
        wVar.s();
    }

    public static w p() {
        return f13888j;
    }

    private void s() {
        this.f13891g = Collections.emptyList();
    }

    public static b t() {
        return b.h();
    }

    public static b u(w wVar) {
        return t().f(wVar);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        for (int i10 = 0; i10 < this.f13891g.size(); i10++) {
            fVar.d0(1, this.f13891g.get(i10));
        }
        fVar.i0(this.f13890f);
    }

    @Override // qc.i, qc.q
    public qc.s<w> getParserForType() {
        return f13889k;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13893i;
        if (i10 != -1) {
            return i10;
        }
        int i11 = 0;
        for (int i12 = 0; i12 < this.f13891g.size(); i12++) {
            i11 += qc.f.s(1, this.f13891g.get(i12));
        }
        int size = i11 + this.f13890f.size();
        this.f13893i = size;
        return size;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13892h;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        this.f13892h = (byte) 1;
        return true;
    }

    public int q() {
        return this.f13891g.size();
    }

    public List<v> r() {
        return this.f13891g;
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

    private w(i.b bVar) {
        super(bVar);
        this.f13892h = (byte) -1;
        this.f13893i = -1;
        this.f13890f = bVar.e();
    }

    private w(boolean z10) {
        this.f13892h = (byte) -1;
        this.f13893i = -1;
        this.f13890f = qc.d.f17259e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private w(qc.e eVar, qc.g gVar) {
        this.f13892h = (byte) -1;
        this.f13893i = -1;
        s();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        boolean z11 = false;
        while (!z10) {
            try {
                try {
                    try {
                        int K = eVar.K();
                        if (K != 0) {
                            if (K != 10) {
                                if (!j(eVar, J, gVar, K)) {
                                }
                            } else {
                                if (!(z11 & true)) {
                                    this.f13891g = new ArrayList();
                                    z11 |= true;
                                }
                                this.f13891g.add(eVar.u(v.f13858q, gVar));
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
                if (z11 & true) {
                    this.f13891g = Collections.unmodifiableList(this.f13891g);
                }
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13890f = q10.v();
                    throw th2;
                }
                this.f13890f = q10.v();
                g();
                throw th;
            }
        }
        if (z11 & true) {
            this.f13891g = Collections.unmodifiableList(this.f13891g);
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13890f = q10.v();
            throw th3;
        }
        this.f13890f = q10.v();
        g();
    }
}
