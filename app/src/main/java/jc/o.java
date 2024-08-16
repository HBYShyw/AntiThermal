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
public final class o extends qc.i implements qc.r {

    /* renamed from: j, reason: collision with root package name */
    private static final o f13686j;

    /* renamed from: k, reason: collision with root package name */
    public static qc.s<o> f13687k = new a();

    /* renamed from: f, reason: collision with root package name */
    private final qc.d f13688f;

    /* renamed from: g, reason: collision with root package name */
    private List<c> f13689g;

    /* renamed from: h, reason: collision with root package name */
    private byte f13690h;

    /* renamed from: i, reason: collision with root package name */
    private int f13691i;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<o> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public o a(qc.e eVar, qc.g gVar) {
            return new o(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.b<o, b> implements qc.r {

        /* renamed from: f, reason: collision with root package name */
        private int f13692f;

        /* renamed from: g, reason: collision with root package name */
        private List<c> f13693g = Collections.emptyList();

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
            if ((this.f13692f & 1) != 1) {
                this.f13693g = new ArrayList(this.f13693g);
                this.f13692f |= 1;
            }
        }

        private void o() {
        }

        @Override // qc.q.a
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public o build() {
            o j10 = j();
            if (j10.isInitialized()) {
                return j10;
            }
            throw a.AbstractC0092a.c(j10);
        }

        public o j() {
            o oVar = new o(this);
            if ((this.f13692f & 1) == 1) {
                this.f13693g = Collections.unmodifiableList(this.f13693g);
                this.f13692f &= -2;
            }
            oVar.f13689g = this.f13693g;
            return oVar;
        }

        @Override // qc.i.b
        /* renamed from: k, reason: merged with bridge method [inline-methods] */
        public b d() {
            return l().f(j());
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b f(o oVar) {
            if (oVar == o.p()) {
                return this;
            }
            if (!oVar.f13689g.isEmpty()) {
                if (this.f13693g.isEmpty()) {
                    this.f13693g = oVar.f13689g;
                    this.f13692f &= -2;
                } else {
                    n();
                    this.f13693g.addAll(oVar.f13689g);
                }
            }
            g(e().d(oVar.f13688f));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: q, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            o oVar = null;
            try {
                try {
                    o a10 = o.f13687k.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (oVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                o oVar2 = (o) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    oVar = oVar2;
                    if (oVar != null) {
                        f(oVar);
                    }
                    throw th;
                }
            }
        }
    }

    static {
        o oVar = new o(true);
        f13686j = oVar;
        oVar.s();
    }

    public static o p() {
        return f13686j;
    }

    private void s() {
        this.f13689g = Collections.emptyList();
    }

    public static b t() {
        return b.h();
    }

    public static b u(o oVar) {
        return t().f(oVar);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        for (int i10 = 0; i10 < this.f13689g.size(); i10++) {
            fVar.d0(1, this.f13689g.get(i10));
        }
        fVar.i0(this.f13688f);
    }

    @Override // qc.i, qc.q
    public qc.s<o> getParserForType() {
        return f13687k;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13691i;
        if (i10 != -1) {
            return i10;
        }
        int i11 = 0;
        for (int i12 = 0; i12 < this.f13689g.size(); i12++) {
            i11 += qc.f.s(1, this.f13689g.get(i12));
        }
        int size = i11 + this.f13688f.size();
        this.f13691i = size;
        return size;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13690h;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        for (int i10 = 0; i10 < r(); i10++) {
            if (!q(i10).isInitialized()) {
                this.f13690h = (byte) 0;
                return false;
            }
        }
        this.f13690h = (byte) 1;
        return true;
    }

    public c q(int i10) {
        return this.f13689g.get(i10);
    }

    public int r() {
        return this.f13689g.size();
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

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class c extends qc.i implements qc.r {

        /* renamed from: m, reason: collision with root package name */
        private static final c f13694m;

        /* renamed from: n, reason: collision with root package name */
        public static qc.s<c> f13695n = new a();

        /* renamed from: f, reason: collision with root package name */
        private final qc.d f13696f;

        /* renamed from: g, reason: collision with root package name */
        private int f13697g;

        /* renamed from: h, reason: collision with root package name */
        private int f13698h;

        /* renamed from: i, reason: collision with root package name */
        private int f13699i;

        /* renamed from: j, reason: collision with root package name */
        private EnumC0066c f13700j;

        /* renamed from: k, reason: collision with root package name */
        private byte f13701k;

        /* renamed from: l, reason: collision with root package name */
        private int f13702l;

        /* compiled from: ProtoBuf.java */
        /* loaded from: classes2.dex */
        static class a extends qc.b<c> {
            a() {
            }

            @Override // qc.s
            /* renamed from: m, reason: merged with bridge method [inline-methods] */
            public c a(qc.e eVar, qc.g gVar) {
                return new c(eVar, gVar);
            }
        }

        /* compiled from: ProtoBuf.java */
        /* loaded from: classes2.dex */
        public static final class b extends i.b<c, b> implements qc.r {

            /* renamed from: f, reason: collision with root package name */
            private int f13703f;

            /* renamed from: h, reason: collision with root package name */
            private int f13705h;

            /* renamed from: g, reason: collision with root package name */
            private int f13704g = -1;

            /* renamed from: i, reason: collision with root package name */
            private EnumC0066c f13706i = EnumC0066c.PACKAGE;

            private b() {
                n();
            }

            static /* synthetic */ b h() {
                return l();
            }

            private static b l() {
                return new b();
            }

            private void n() {
            }

            @Override // qc.q.a
            /* renamed from: i, reason: merged with bridge method [inline-methods] */
            public c build() {
                c j10 = j();
                if (j10.isInitialized()) {
                    return j10;
                }
                throw a.AbstractC0092a.c(j10);
            }

            public c j() {
                c cVar = new c(this);
                int i10 = this.f13703f;
                int i11 = (i10 & 1) != 1 ? 0 : 1;
                cVar.f13698h = this.f13704g;
                if ((i10 & 2) == 2) {
                    i11 |= 2;
                }
                cVar.f13699i = this.f13705h;
                if ((i10 & 4) == 4) {
                    i11 |= 4;
                }
                cVar.f13700j = this.f13706i;
                cVar.f13697g = i11;
                return cVar;
            }

            @Override // qc.i.b
            /* renamed from: k, reason: merged with bridge method [inline-methods] */
            public b d() {
                return l().f(j());
            }

            @Override // qc.i.b
            /* renamed from: o, reason: merged with bridge method [inline-methods] */
            public b f(c cVar) {
                if (cVar == c.r()) {
                    return this;
                }
                if (cVar.w()) {
                    r(cVar.t());
                }
                if (cVar.x()) {
                    s(cVar.u());
                }
                if (cVar.v()) {
                    q(cVar.s());
                }
                g(e().d(cVar.f13696f));
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
            @Override // qc.a.AbstractC0092a, qc.q.a
            /* renamed from: p, reason: merged with bridge method [inline-methods] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public b m(qc.e eVar, qc.g gVar) {
                c cVar = null;
                try {
                    try {
                        c a10 = c.f13695n.a(eVar, gVar);
                        if (a10 != null) {
                            f(a10);
                        }
                        return this;
                    } catch (Throwable th) {
                        th = th;
                        if (cVar != null) {
                        }
                        throw th;
                    }
                } catch (qc.k e10) {
                    c cVar2 = (c) e10.a();
                    try {
                        throw e10;
                    } catch (Throwable th2) {
                        th = th2;
                        cVar = cVar2;
                        if (cVar != null) {
                            f(cVar);
                        }
                        throw th;
                    }
                }
            }

            public b q(EnumC0066c enumC0066c) {
                Objects.requireNonNull(enumC0066c);
                this.f13703f |= 4;
                this.f13706i = enumC0066c;
                return this;
            }

            public b r(int i10) {
                this.f13703f |= 1;
                this.f13704g = i10;
                return this;
            }

            public b s(int i10) {
                this.f13703f |= 2;
                this.f13705h = i10;
                return this;
            }
        }

        /* compiled from: ProtoBuf.java */
        /* renamed from: jc.o$c$c, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public enum EnumC0066c implements j.a {
            CLASS(0, 0),
            PACKAGE(1, 1),
            LOCAL(2, 2);


            /* renamed from: i, reason: collision with root package name */
            private static j.b<EnumC0066c> f13710i = new a();

            /* renamed from: e, reason: collision with root package name */
            private final int f13712e;

            /* compiled from: ProtoBuf.java */
            /* renamed from: jc.o$c$c$a */
            /* loaded from: classes2.dex */
            static class a implements j.b<EnumC0066c> {
                a() {
                }

                @Override // qc.j.b
                /* renamed from: a, reason: merged with bridge method [inline-methods] */
                public EnumC0066c findValueByNumber(int i10) {
                    return EnumC0066c.a(i10);
                }
            }

            EnumC0066c(int i10, int i11) {
                this.f13712e = i11;
            }

            public static EnumC0066c a(int i10) {
                if (i10 == 0) {
                    return CLASS;
                }
                if (i10 == 1) {
                    return PACKAGE;
                }
                if (i10 != 2) {
                    return null;
                }
                return LOCAL;
            }

            @Override // qc.j.a
            public final int getNumber() {
                return this.f13712e;
            }
        }

        static {
            c cVar = new c(true);
            f13694m = cVar;
            cVar.y();
        }

        public static b A(c cVar) {
            return z().f(cVar);
        }

        public static c r() {
            return f13694m;
        }

        private void y() {
            this.f13698h = -1;
            this.f13699i = 0;
            this.f13700j = EnumC0066c.PACKAGE;
        }

        public static b z() {
            return b.h();
        }

        @Override // qc.q
        /* renamed from: B, reason: merged with bridge method [inline-methods] */
        public b newBuilderForType() {
            return z();
        }

        @Override // qc.q
        /* renamed from: C, reason: merged with bridge method [inline-methods] */
        public b toBuilder() {
            return A(this);
        }

        @Override // qc.q
        public void a(qc.f fVar) {
            getSerializedSize();
            if ((this.f13697g & 1) == 1) {
                fVar.a0(1, this.f13698h);
            }
            if ((this.f13697g & 2) == 2) {
                fVar.a0(2, this.f13699i);
            }
            if ((this.f13697g & 4) == 4) {
                fVar.S(3, this.f13700j.getNumber());
            }
            fVar.i0(this.f13696f);
        }

        @Override // qc.i, qc.q
        public qc.s<c> getParserForType() {
            return f13695n;
        }

        @Override // qc.q
        public int getSerializedSize() {
            int i10 = this.f13702l;
            if (i10 != -1) {
                return i10;
            }
            int o10 = (this.f13697g & 1) == 1 ? 0 + qc.f.o(1, this.f13698h) : 0;
            if ((this.f13697g & 2) == 2) {
                o10 += qc.f.o(2, this.f13699i);
            }
            if ((this.f13697g & 4) == 4) {
                o10 += qc.f.h(3, this.f13700j.getNumber());
            }
            int size = o10 + this.f13696f.size();
            this.f13702l = size;
            return size;
        }

        @Override // qc.r
        public final boolean isInitialized() {
            byte b10 = this.f13701k;
            if (b10 == 1) {
                return true;
            }
            if (b10 == 0) {
                return false;
            }
            if (!x()) {
                this.f13701k = (byte) 0;
                return false;
            }
            this.f13701k = (byte) 1;
            return true;
        }

        public EnumC0066c s() {
            return this.f13700j;
        }

        public int t() {
            return this.f13698h;
        }

        public int u() {
            return this.f13699i;
        }

        public boolean v() {
            return (this.f13697g & 4) == 4;
        }

        public boolean w() {
            return (this.f13697g & 1) == 1;
        }

        public boolean x() {
            return (this.f13697g & 2) == 2;
        }

        private c(i.b bVar) {
            super(bVar);
            this.f13701k = (byte) -1;
            this.f13702l = -1;
            this.f13696f = bVar.e();
        }

        private c(boolean z10) {
            this.f13701k = (byte) -1;
            this.f13702l = -1;
            this.f13696f = qc.d.f17259e;
        }

        private c(qc.e eVar, qc.g gVar) {
            this.f13701k = (byte) -1;
            this.f13702l = -1;
            y();
            d.b q10 = qc.d.q();
            qc.f J = qc.f.J(q10, 1);
            boolean z10 = false;
            while (!z10) {
                try {
                    try {
                        int K = eVar.K();
                        if (K != 0) {
                            if (K == 8) {
                                this.f13697g |= 1;
                                this.f13698h = eVar.s();
                            } else if (K == 16) {
                                this.f13697g |= 2;
                                this.f13699i = eVar.s();
                            } else if (K != 24) {
                                if (!j(eVar, J, gVar, K)) {
                                }
                            } else {
                                int n10 = eVar.n();
                                EnumC0066c a10 = EnumC0066c.a(n10);
                                if (a10 == null) {
                                    J.o0(K);
                                    J.o0(n10);
                                } else {
                                    this.f13697g |= 4;
                                    this.f13700j = a10;
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
                    try {
                        J.I();
                    } catch (IOException unused) {
                    } catch (Throwable th2) {
                        this.f13696f = q10.v();
                        throw th2;
                    }
                    this.f13696f = q10.v();
                    g();
                    throw th;
                }
            }
            try {
                J.I();
            } catch (IOException unused2) {
            } catch (Throwable th3) {
                this.f13696f = q10.v();
                throw th3;
            }
            this.f13696f = q10.v();
            g();
        }
    }

    private o(i.b bVar) {
        super(bVar);
        this.f13690h = (byte) -1;
        this.f13691i = -1;
        this.f13688f = bVar.e();
    }

    private o(boolean z10) {
        this.f13690h = (byte) -1;
        this.f13691i = -1;
        this.f13688f = qc.d.f17259e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private o(qc.e eVar, qc.g gVar) {
        this.f13690h = (byte) -1;
        this.f13691i = -1;
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
                                    this.f13689g = new ArrayList();
                                    z11 |= true;
                                }
                                this.f13689g.add(eVar.u(c.f13695n, gVar));
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
                    this.f13689g = Collections.unmodifiableList(this.f13689g);
                }
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13688f = q10.v();
                    throw th2;
                }
                this.f13688f = q10.v();
                g();
                throw th;
            }
        }
        if (z11 & true) {
            this.f13689g = Collections.unmodifiableList(this.f13689g);
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13688f = q10.v();
            throw th3;
        }
        this.f13688f = q10.v();
        g();
    }
}
