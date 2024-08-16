package jc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import qc.a;
import qc.d;
import qc.i;
import qc.j;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class s extends i.d<s> {

    /* renamed from: r, reason: collision with root package name */
    private static final s f13801r;

    /* renamed from: s, reason: collision with root package name */
    public static qc.s<s> f13802s = new a();

    /* renamed from: g, reason: collision with root package name */
    private final qc.d f13803g;

    /* renamed from: h, reason: collision with root package name */
    private int f13804h;

    /* renamed from: i, reason: collision with root package name */
    private int f13805i;

    /* renamed from: j, reason: collision with root package name */
    private int f13806j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f13807k;

    /* renamed from: l, reason: collision with root package name */
    private c f13808l;

    /* renamed from: m, reason: collision with root package name */
    private List<q> f13809m;

    /* renamed from: n, reason: collision with root package name */
    private List<Integer> f13810n;

    /* renamed from: o, reason: collision with root package name */
    private int f13811o;

    /* renamed from: p, reason: collision with root package name */
    private byte f13812p;

    /* renamed from: q, reason: collision with root package name */
    private int f13813q;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<s> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public s a(qc.e eVar, qc.g gVar) {
            return new s(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.c<s, b> {

        /* renamed from: h, reason: collision with root package name */
        private int f13814h;

        /* renamed from: i, reason: collision with root package name */
        private int f13815i;

        /* renamed from: j, reason: collision with root package name */
        private int f13816j;

        /* renamed from: k, reason: collision with root package name */
        private boolean f13817k;

        /* renamed from: l, reason: collision with root package name */
        private c f13818l = c.INV;

        /* renamed from: m, reason: collision with root package name */
        private List<q> f13819m = Collections.emptyList();

        /* renamed from: n, reason: collision with root package name */
        private List<Integer> f13820n = Collections.emptyList();

        private b() {
            t();
        }

        static /* synthetic */ b l() {
            return q();
        }

        private static b q() {
            return new b();
        }

        private void r() {
            if ((this.f13814h & 32) != 32) {
                this.f13820n = new ArrayList(this.f13820n);
                this.f13814h |= 32;
            }
        }

        private void s() {
            if ((this.f13814h & 16) != 16) {
                this.f13819m = new ArrayList(this.f13819m);
                this.f13814h |= 16;
            }
        }

        private void t() {
        }

        @Override // qc.q.a
        /* renamed from: n, reason: merged with bridge method [inline-methods] */
        public s build() {
            s o10 = o();
            if (o10.isInitialized()) {
                return o10;
            }
            throw a.AbstractC0092a.c(o10);
        }

        public s o() {
            s sVar = new s(this);
            int i10 = this.f13814h;
            int i11 = (i10 & 1) != 1 ? 0 : 1;
            sVar.f13805i = this.f13815i;
            if ((i10 & 2) == 2) {
                i11 |= 2;
            }
            sVar.f13806j = this.f13816j;
            if ((i10 & 4) == 4) {
                i11 |= 4;
            }
            sVar.f13807k = this.f13817k;
            if ((i10 & 8) == 8) {
                i11 |= 8;
            }
            sVar.f13808l = this.f13818l;
            if ((this.f13814h & 16) == 16) {
                this.f13819m = Collections.unmodifiableList(this.f13819m);
                this.f13814h &= -17;
            }
            sVar.f13809m = this.f13819m;
            if ((this.f13814h & 32) == 32) {
                this.f13820n = Collections.unmodifiableList(this.f13820n);
                this.f13814h &= -33;
            }
            sVar.f13810n = this.f13820n;
            sVar.f13804h = i11;
            return sVar;
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b d() {
            return q().f(o());
        }

        @Override // qc.i.b
        /* renamed from: u, reason: merged with bridge method [inline-methods] */
        public b f(s sVar) {
            if (sVar == s.F()) {
                return this;
            }
            if (sVar.P()) {
                w(sVar.H());
            }
            if (sVar.Q()) {
                x(sVar.I());
            }
            if (sVar.R()) {
                y(sVar.J());
            }
            if (sVar.S()) {
                z(sVar.O());
            }
            if (!sVar.f13809m.isEmpty()) {
                if (this.f13819m.isEmpty()) {
                    this.f13819m = sVar.f13809m;
                    this.f13814h &= -17;
                } else {
                    s();
                    this.f13819m.addAll(sVar.f13809m);
                }
            }
            if (!sVar.f13810n.isEmpty()) {
                if (this.f13820n.isEmpty()) {
                    this.f13820n = sVar.f13810n;
                    this.f13814h &= -33;
                } else {
                    r();
                    this.f13820n.addAll(sVar.f13810n);
                }
            }
            k(sVar);
            g(e().d(sVar.f13803g));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: v, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            s sVar = null;
            try {
                try {
                    s a10 = s.f13802s.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (sVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                s sVar2 = (s) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    sVar = sVar2;
                    if (sVar != null) {
                        f(sVar);
                    }
                    throw th;
                }
            }
        }

        public b w(int i10) {
            this.f13814h |= 1;
            this.f13815i = i10;
            return this;
        }

        public b x(int i10) {
            this.f13814h |= 2;
            this.f13816j = i10;
            return this;
        }

        public b y(boolean z10) {
            this.f13814h |= 4;
            this.f13817k = z10;
            return this;
        }

        public b z(c cVar) {
            Objects.requireNonNull(cVar);
            this.f13814h |= 8;
            this.f13818l = cVar;
            return this;
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public enum c implements j.a {
        IN(0, 0),
        OUT(1, 1),
        INV(2, 2);


        /* renamed from: i, reason: collision with root package name */
        private static j.b<c> f13824i = new a();

        /* renamed from: e, reason: collision with root package name */
        private final int f13826e;

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
            this.f13826e = i11;
        }

        public static c a(int i10) {
            if (i10 == 0) {
                return IN;
            }
            if (i10 == 1) {
                return OUT;
            }
            if (i10 != 2) {
                return null;
            }
            return INV;
        }

        @Override // qc.j.a
        public final int getNumber() {
            return this.f13826e;
        }
    }

    static {
        s sVar = new s(true);
        f13801r = sVar;
        sVar.T();
    }

    public static s F() {
        return f13801r;
    }

    private void T() {
        this.f13805i = 0;
        this.f13806j = 0;
        this.f13807k = false;
        this.f13808l = c.INV;
        this.f13809m = Collections.emptyList();
        this.f13810n = Collections.emptyList();
    }

    public static b U() {
        return b.l();
    }

    public static b V(s sVar) {
        return U().f(sVar);
    }

    @Override // qc.r
    /* renamed from: G, reason: merged with bridge method [inline-methods] */
    public s getDefaultInstanceForType() {
        return f13801r;
    }

    public int H() {
        return this.f13805i;
    }

    public int I() {
        return this.f13806j;
    }

    public boolean J() {
        return this.f13807k;
    }

    public q K(int i10) {
        return this.f13809m.get(i10);
    }

    public int L() {
        return this.f13809m.size();
    }

    public List<Integer> M() {
        return this.f13810n;
    }

    public List<q> N() {
        return this.f13809m;
    }

    public c O() {
        return this.f13808l;
    }

    public boolean P() {
        return (this.f13804h & 1) == 1;
    }

    public boolean Q() {
        return (this.f13804h & 2) == 2;
    }

    public boolean R() {
        return (this.f13804h & 4) == 4;
    }

    public boolean S() {
        return (this.f13804h & 8) == 8;
    }

    @Override // qc.q
    /* renamed from: W, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return U();
    }

    @Override // qc.q
    /* renamed from: X, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return V(this);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        i.d<MessageType>.a t7 = t();
        if ((this.f13804h & 1) == 1) {
            fVar.a0(1, this.f13805i);
        }
        if ((this.f13804h & 2) == 2) {
            fVar.a0(2, this.f13806j);
        }
        if ((this.f13804h & 4) == 4) {
            fVar.L(3, this.f13807k);
        }
        if ((this.f13804h & 8) == 8) {
            fVar.S(4, this.f13808l.getNumber());
        }
        for (int i10 = 0; i10 < this.f13809m.size(); i10++) {
            fVar.d0(5, this.f13809m.get(i10));
        }
        if (M().size() > 0) {
            fVar.o0(50);
            fVar.o0(this.f13811o);
        }
        for (int i11 = 0; i11 < this.f13810n.size(); i11++) {
            fVar.b0(this.f13810n.get(i11).intValue());
        }
        t7.a(1000, fVar);
        fVar.i0(this.f13803g);
    }

    @Override // qc.i, qc.q
    public qc.s<s> getParserForType() {
        return f13802s;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13813q;
        if (i10 != -1) {
            return i10;
        }
        int o10 = (this.f13804h & 1) == 1 ? qc.f.o(1, this.f13805i) + 0 : 0;
        if ((this.f13804h & 2) == 2) {
            o10 += qc.f.o(2, this.f13806j);
        }
        if ((this.f13804h & 4) == 4) {
            o10 += qc.f.a(3, this.f13807k);
        }
        if ((this.f13804h & 8) == 8) {
            o10 += qc.f.h(4, this.f13808l.getNumber());
        }
        for (int i11 = 0; i11 < this.f13809m.size(); i11++) {
            o10 += qc.f.s(5, this.f13809m.get(i11));
        }
        int i12 = 0;
        for (int i13 = 0; i13 < this.f13810n.size(); i13++) {
            i12 += qc.f.p(this.f13810n.get(i13).intValue());
        }
        int i14 = o10 + i12;
        if (!M().isEmpty()) {
            i14 = i14 + 1 + qc.f.p(i12);
        }
        this.f13811o = i12;
        int o11 = i14 + o() + this.f13803g.size();
        this.f13813q = o11;
        return o11;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13812p;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        if (!P()) {
            this.f13812p = (byte) 0;
            return false;
        }
        if (!Q()) {
            this.f13812p = (byte) 0;
            return false;
        }
        for (int i10 = 0; i10 < L(); i10++) {
            if (!K(i10).isInitialized()) {
                this.f13812p = (byte) 0;
                return false;
            }
        }
        if (!n()) {
            this.f13812p = (byte) 0;
            return false;
        }
        this.f13812p = (byte) 1;
        return true;
    }

    private s(i.c<s, ?> cVar) {
        super(cVar);
        this.f13811o = -1;
        this.f13812p = (byte) -1;
        this.f13813q = -1;
        this.f13803g = cVar.e();
    }

    private s(boolean z10) {
        this.f13811o = -1;
        this.f13812p = (byte) -1;
        this.f13813q = -1;
        this.f13803g = qc.d.f17259e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private s(qc.e eVar, qc.g gVar) {
        this.f13811o = -1;
        this.f13812p = (byte) -1;
        this.f13813q = -1;
        T();
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
                            this.f13804h |= 1;
                            this.f13805i = eVar.s();
                        } else if (K == 16) {
                            this.f13804h |= 2;
                            this.f13806j = eVar.s();
                        } else if (K == 24) {
                            this.f13804h |= 4;
                            this.f13807k = eVar.k();
                        } else if (K == 32) {
                            int n10 = eVar.n();
                            c a10 = c.a(n10);
                            if (a10 == null) {
                                J.o0(K);
                                J.o0(n10);
                            } else {
                                this.f13804h |= 8;
                                this.f13808l = a10;
                            }
                        } else if (K == 42) {
                            if ((i10 & 16) != 16) {
                                this.f13809m = new ArrayList();
                                i10 |= 16;
                            }
                            this.f13809m.add(eVar.u(q.f13722z, gVar));
                        } else if (K == 48) {
                            if ((i10 & 32) != 32) {
                                this.f13810n = new ArrayList();
                                i10 |= 32;
                            }
                            this.f13810n.add(Integer.valueOf(eVar.s()));
                        } else if (K != 50) {
                            if (!j(eVar, J, gVar, K)) {
                            }
                        } else {
                            int j10 = eVar.j(eVar.A());
                            if ((i10 & 32) != 32 && eVar.e() > 0) {
                                this.f13810n = new ArrayList();
                                i10 |= 32;
                            }
                            while (eVar.e() > 0) {
                                this.f13810n.add(Integer.valueOf(eVar.s()));
                            }
                            eVar.i(j10);
                        }
                    }
                    z10 = true;
                } catch (qc.k e10) {
                    throw e10.i(this);
                } catch (IOException e11) {
                    throw new qc.k(e11.getMessage()).i(this);
                }
            } catch (Throwable th) {
                if ((i10 & 16) == 16) {
                    this.f13809m = Collections.unmodifiableList(this.f13809m);
                }
                if ((i10 & 32) == 32) {
                    this.f13810n = Collections.unmodifiableList(this.f13810n);
                }
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13803g = q10.v();
                    throw th2;
                }
                this.f13803g = q10.v();
                g();
                throw th;
            }
        }
        if ((i10 & 16) == 16) {
            this.f13809m = Collections.unmodifiableList(this.f13809m);
        }
        if ((i10 & 32) == 32) {
            this.f13810n = Collections.unmodifiableList(this.f13810n);
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13803g = q10.v();
            throw th3;
        }
        this.f13803g = q10.v();
        g();
    }
}
