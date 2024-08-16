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
public final class e extends qc.i implements qc.r {

    /* renamed from: j, reason: collision with root package name */
    private static final e f13499j;

    /* renamed from: k, reason: collision with root package name */
    public static qc.s<e> f13500k = new a();

    /* renamed from: f, reason: collision with root package name */
    private final qc.d f13501f;

    /* renamed from: g, reason: collision with root package name */
    private List<f> f13502g;

    /* renamed from: h, reason: collision with root package name */
    private byte f13503h;

    /* renamed from: i, reason: collision with root package name */
    private int f13504i;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<e> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public e a(qc.e eVar, qc.g gVar) {
            return new e(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.b<e, b> implements qc.r {

        /* renamed from: f, reason: collision with root package name */
        private int f13505f;

        /* renamed from: g, reason: collision with root package name */
        private List<f> f13506g = Collections.emptyList();

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
            if ((this.f13505f & 1) != 1) {
                this.f13506g = new ArrayList(this.f13506g);
                this.f13505f |= 1;
            }
        }

        private void o() {
        }

        @Override // qc.q.a
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public e build() {
            e j10 = j();
            if (j10.isInitialized()) {
                return j10;
            }
            throw a.AbstractC0092a.c(j10);
        }

        public e j() {
            e eVar = new e(this);
            if ((this.f13505f & 1) == 1) {
                this.f13506g = Collections.unmodifiableList(this.f13506g);
                this.f13505f &= -2;
            }
            eVar.f13502g = this.f13506g;
            return eVar;
        }

        @Override // qc.i.b
        /* renamed from: k, reason: merged with bridge method [inline-methods] */
        public b d() {
            return l().f(j());
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b f(e eVar) {
            if (eVar == e.p()) {
                return this;
            }
            if (!eVar.f13502g.isEmpty()) {
                if (this.f13506g.isEmpty()) {
                    this.f13506g = eVar.f13502g;
                    this.f13505f &= -2;
                } else {
                    n();
                    this.f13506g.addAll(eVar.f13502g);
                }
            }
            g(e().d(eVar.f13501f));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: q, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            e eVar2 = null;
            try {
                try {
                    e a10 = e.f13500k.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (eVar2 != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                e eVar3 = (e) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    eVar2 = eVar3;
                    if (eVar2 != null) {
                        f(eVar2);
                    }
                    throw th;
                }
            }
        }
    }

    static {
        e eVar = new e(true);
        f13499j = eVar;
        eVar.s();
    }

    public static e p() {
        return f13499j;
    }

    private void s() {
        this.f13502g = Collections.emptyList();
    }

    public static b t() {
        return b.h();
    }

    public static b u(e eVar) {
        return t().f(eVar);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        for (int i10 = 0; i10 < this.f13502g.size(); i10++) {
            fVar.d0(1, this.f13502g.get(i10));
        }
        fVar.i0(this.f13501f);
    }

    @Override // qc.i, qc.q
    public qc.s<e> getParserForType() {
        return f13500k;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13504i;
        if (i10 != -1) {
            return i10;
        }
        int i11 = 0;
        for (int i12 = 0; i12 < this.f13502g.size(); i12++) {
            i11 += qc.f.s(1, this.f13502g.get(i12));
        }
        int size = i11 + this.f13501f.size();
        this.f13504i = size;
        return size;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13503h;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        for (int i10 = 0; i10 < r(); i10++) {
            if (!q(i10).isInitialized()) {
                this.f13503h = (byte) 0;
                return false;
            }
        }
        this.f13503h = (byte) 1;
        return true;
    }

    public f q(int i10) {
        return this.f13502g.get(i10);
    }

    public int r() {
        return this.f13502g.size();
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

    private e(i.b bVar) {
        super(bVar);
        this.f13503h = (byte) -1;
        this.f13504i = -1;
        this.f13501f = bVar.e();
    }

    private e(boolean z10) {
        this.f13503h = (byte) -1;
        this.f13504i = -1;
        this.f13501f = qc.d.f17259e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private e(qc.e eVar, qc.g gVar) {
        this.f13503h = (byte) -1;
        this.f13504i = -1;
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
                                    this.f13502g = new ArrayList();
                                    z11 |= true;
                                }
                                this.f13502g.add(eVar.u(f.f13508o, gVar));
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
                    this.f13502g = Collections.unmodifiableList(this.f13502g);
                }
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13501f = q10.v();
                    throw th2;
                }
                this.f13501f = q10.v();
                g();
                throw th;
            }
        }
        if (z11 & true) {
            this.f13502g = Collections.unmodifiableList(this.f13502g);
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13501f = q10.v();
            throw th3;
        }
        this.f13501f = q10.v();
        g();
    }
}
