package jc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import jc.h;
import qc.a;
import qc.d;
import qc.i;
import qc.j;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class f extends qc.i implements qc.r {

    /* renamed from: n, reason: collision with root package name */
    private static final f f13507n;

    /* renamed from: o, reason: collision with root package name */
    public static qc.s<f> f13508o = new a();

    /* renamed from: f, reason: collision with root package name */
    private final qc.d f13509f;

    /* renamed from: g, reason: collision with root package name */
    private int f13510g;

    /* renamed from: h, reason: collision with root package name */
    private c f13511h;

    /* renamed from: i, reason: collision with root package name */
    private List<h> f13512i;

    /* renamed from: j, reason: collision with root package name */
    private h f13513j;

    /* renamed from: k, reason: collision with root package name */
    private d f13514k;

    /* renamed from: l, reason: collision with root package name */
    private byte f13515l;

    /* renamed from: m, reason: collision with root package name */
    private int f13516m;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<f> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public f a(qc.e eVar, qc.g gVar) {
            return new f(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.b<f, b> implements qc.r {

        /* renamed from: f, reason: collision with root package name */
        private int f13517f;

        /* renamed from: g, reason: collision with root package name */
        private c f13518g = c.RETURNS_CONSTANT;

        /* renamed from: h, reason: collision with root package name */
        private List<h> f13519h = Collections.emptyList();

        /* renamed from: i, reason: collision with root package name */
        private h f13520i = h.A();

        /* renamed from: j, reason: collision with root package name */
        private d f13521j = d.AT_MOST_ONCE;

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
            if ((this.f13517f & 2) != 2) {
                this.f13519h = new ArrayList(this.f13519h);
                this.f13517f |= 2;
            }
        }

        private void o() {
        }

        @Override // qc.q.a
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public f build() {
            f j10 = j();
            if (j10.isInitialized()) {
                return j10;
            }
            throw a.AbstractC0092a.c(j10);
        }

        public f j() {
            f fVar = new f(this);
            int i10 = this.f13517f;
            int i11 = (i10 & 1) != 1 ? 0 : 1;
            fVar.f13511h = this.f13518g;
            if ((this.f13517f & 2) == 2) {
                this.f13519h = Collections.unmodifiableList(this.f13519h);
                this.f13517f &= -3;
            }
            fVar.f13512i = this.f13519h;
            if ((i10 & 4) == 4) {
                i11 |= 2;
            }
            fVar.f13513j = this.f13520i;
            if ((i10 & 8) == 8) {
                i11 |= 4;
            }
            fVar.f13514k = this.f13521j;
            fVar.f13510g = i11;
            return fVar;
        }

        @Override // qc.i.b
        /* renamed from: k, reason: merged with bridge method [inline-methods] */
        public b d() {
            return l().f(j());
        }

        public b p(h hVar) {
            if ((this.f13517f & 4) == 4 && this.f13520i != h.A()) {
                this.f13520i = h.O(this.f13520i).f(hVar).j();
            } else {
                this.f13520i = hVar;
            }
            this.f13517f |= 4;
            return this;
        }

        @Override // qc.i.b
        /* renamed from: q, reason: merged with bridge method [inline-methods] */
        public b f(f fVar) {
            if (fVar == f.u()) {
                return this;
            }
            if (fVar.A()) {
                s(fVar.x());
            }
            if (!fVar.f13512i.isEmpty()) {
                if (this.f13519h.isEmpty()) {
                    this.f13519h = fVar.f13512i;
                    this.f13517f &= -3;
                } else {
                    n();
                    this.f13519h.addAll(fVar.f13512i);
                }
            }
            if (fVar.z()) {
                p(fVar.t());
            }
            if (fVar.B()) {
                t(fVar.y());
            }
            g(e().d(fVar.f13509f));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: r, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            f fVar = null;
            try {
                try {
                    f a10 = f.f13508o.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (fVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                f fVar2 = (f) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    fVar = fVar2;
                    if (fVar != null) {
                        f(fVar);
                    }
                    throw th;
                }
            }
        }

        public b s(c cVar) {
            Objects.requireNonNull(cVar);
            this.f13517f |= 1;
            this.f13518g = cVar;
            return this;
        }

        public b t(d dVar) {
            Objects.requireNonNull(dVar);
            this.f13517f |= 8;
            this.f13521j = dVar;
            return this;
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public enum c implements j.a {
        RETURNS_CONSTANT(0, 0),
        CALLS(1, 1),
        RETURNS_NOT_NULL(2, 2);


        /* renamed from: i, reason: collision with root package name */
        private static j.b<c> f13525i = new a();

        /* renamed from: e, reason: collision with root package name */
        private final int f13527e;

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
            this.f13527e = i11;
        }

        public static c a(int i10) {
            if (i10 == 0) {
                return RETURNS_CONSTANT;
            }
            if (i10 == 1) {
                return CALLS;
            }
            if (i10 != 2) {
                return null;
            }
            return RETURNS_NOT_NULL;
        }

        @Override // qc.j.a
        public final int getNumber() {
            return this.f13527e;
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public enum d implements j.a {
        AT_MOST_ONCE(0, 0),
        EXACTLY_ONCE(1, 1),
        AT_LEAST_ONCE(2, 2);


        /* renamed from: i, reason: collision with root package name */
        private static j.b<d> f13531i = new a();

        /* renamed from: e, reason: collision with root package name */
        private final int f13533e;

        /* compiled from: ProtoBuf.java */
        /* loaded from: classes2.dex */
        static class a implements j.b<d> {
            a() {
            }

            @Override // qc.j.b
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public d findValueByNumber(int i10) {
                return d.a(i10);
            }
        }

        d(int i10, int i11) {
            this.f13533e = i11;
        }

        public static d a(int i10) {
            if (i10 == 0) {
                return AT_MOST_ONCE;
            }
            if (i10 == 1) {
                return EXACTLY_ONCE;
            }
            if (i10 != 2) {
                return null;
            }
            return AT_LEAST_ONCE;
        }

        @Override // qc.j.a
        public final int getNumber() {
            return this.f13533e;
        }
    }

    static {
        f fVar = new f(true);
        f13507n = fVar;
        fVar.C();
    }

    private void C() {
        this.f13511h = c.RETURNS_CONSTANT;
        this.f13512i = Collections.emptyList();
        this.f13513j = h.A();
        this.f13514k = d.AT_MOST_ONCE;
    }

    public static b D() {
        return b.h();
    }

    public static b E(f fVar) {
        return D().f(fVar);
    }

    public static f u() {
        return f13507n;
    }

    public boolean A() {
        return (this.f13510g & 1) == 1;
    }

    public boolean B() {
        return (this.f13510g & 4) == 4;
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
        if ((this.f13510g & 1) == 1) {
            fVar.S(1, this.f13511h.getNumber());
        }
        for (int i10 = 0; i10 < this.f13512i.size(); i10++) {
            fVar.d0(2, this.f13512i.get(i10));
        }
        if ((this.f13510g & 2) == 2) {
            fVar.d0(3, this.f13513j);
        }
        if ((this.f13510g & 4) == 4) {
            fVar.S(4, this.f13514k.getNumber());
        }
        fVar.i0(this.f13509f);
    }

    @Override // qc.i, qc.q
    public qc.s<f> getParserForType() {
        return f13508o;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13516m;
        if (i10 != -1) {
            return i10;
        }
        int h10 = (this.f13510g & 1) == 1 ? qc.f.h(1, this.f13511h.getNumber()) + 0 : 0;
        for (int i11 = 0; i11 < this.f13512i.size(); i11++) {
            h10 += qc.f.s(2, this.f13512i.get(i11));
        }
        if ((this.f13510g & 2) == 2) {
            h10 += qc.f.s(3, this.f13513j);
        }
        if ((this.f13510g & 4) == 4) {
            h10 += qc.f.h(4, this.f13514k.getNumber());
        }
        int size = h10 + this.f13509f.size();
        this.f13516m = size;
        return size;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13515l;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        for (int i10 = 0; i10 < w(); i10++) {
            if (!v(i10).isInitialized()) {
                this.f13515l = (byte) 0;
                return false;
            }
        }
        if (z() && !t().isInitialized()) {
            this.f13515l = (byte) 0;
            return false;
        }
        this.f13515l = (byte) 1;
        return true;
    }

    public h t() {
        return this.f13513j;
    }

    public h v(int i10) {
        return this.f13512i.get(i10);
    }

    public int w() {
        return this.f13512i.size();
    }

    public c x() {
        return this.f13511h;
    }

    public d y() {
        return this.f13514k;
    }

    public boolean z() {
        return (this.f13510g & 2) == 2;
    }

    private f(i.b bVar) {
        super(bVar);
        this.f13515l = (byte) -1;
        this.f13516m = -1;
        this.f13509f = bVar.e();
    }

    private f(boolean z10) {
        this.f13515l = (byte) -1;
        this.f13516m = -1;
        this.f13509f = qc.d.f17259e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private f(qc.e eVar, qc.g gVar) {
        this.f13515l = (byte) -1;
        this.f13516m = -1;
        C();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        int i10 = 0;
        while (!z10) {
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
                                this.f13510g |= 1;
                                this.f13511h = a10;
                            }
                        } else if (K == 18) {
                            if ((i10 & 2) != 2) {
                                this.f13512i = new ArrayList();
                                i10 |= 2;
                            }
                            this.f13512i.add(eVar.u(h.f13544r, gVar));
                        } else if (K == 26) {
                            h.b builder = (this.f13510g & 2) == 2 ? this.f13513j.toBuilder() : null;
                            h hVar = (h) eVar.u(h.f13544r, gVar);
                            this.f13513j = hVar;
                            if (builder != null) {
                                builder.f(hVar);
                                this.f13513j = builder.j();
                            }
                            this.f13510g |= 2;
                        } else if (K != 32) {
                            if (!j(eVar, J, gVar, K)) {
                            }
                        } else {
                            int n11 = eVar.n();
                            d a11 = d.a(n11);
                            if (a11 == null) {
                                J.o0(K);
                                J.o0(n11);
                            } else {
                                this.f13510g |= 4;
                                this.f13514k = a11;
                            }
                        }
                    }
                    z10 = true;
                } catch (qc.k e10) {
                    throw e10.i(this);
                } catch (IOException e11) {
                    throw new qc.k(e11.getMessage()).i(this);
                }
            } catch (Throwable th) {
                if ((i10 & 2) == 2) {
                    this.f13512i = Collections.unmodifiableList(this.f13512i);
                }
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13509f = q10.v();
                    throw th2;
                }
                this.f13509f = q10.v();
                g();
                throw th;
            }
        }
        if ((i10 & 2) == 2) {
            this.f13512i = Collections.unmodifiableList(this.f13512i);
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13509f = q10.v();
            throw th3;
        }
        this.f13509f = q10.v();
        g();
    }
}
