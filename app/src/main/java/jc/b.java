package jc;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
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
public final class b extends qc.i implements qc.r {

    /* renamed from: l, reason: collision with root package name */
    private static final b f13370l;

    /* renamed from: m, reason: collision with root package name */
    public static qc.s<b> f13371m = new a();

    /* renamed from: f, reason: collision with root package name */
    private final qc.d f13372f;

    /* renamed from: g, reason: collision with root package name */
    private int f13373g;

    /* renamed from: h, reason: collision with root package name */
    private int f13374h;

    /* renamed from: i, reason: collision with root package name */
    private List<C0061b> f13375i;

    /* renamed from: j, reason: collision with root package name */
    private byte f13376j;

    /* renamed from: k, reason: collision with root package name */
    private int f13377k;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<b> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public b a(qc.e eVar, qc.g gVar) {
            return new b(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* renamed from: jc.b$b, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public static final class C0061b extends qc.i implements qc.r {

        /* renamed from: l, reason: collision with root package name */
        private static final C0061b f13378l;

        /* renamed from: m, reason: collision with root package name */
        public static qc.s<C0061b> f13379m = new a();

        /* renamed from: f, reason: collision with root package name */
        private final qc.d f13380f;

        /* renamed from: g, reason: collision with root package name */
        private int f13381g;

        /* renamed from: h, reason: collision with root package name */
        private int f13382h;

        /* renamed from: i, reason: collision with root package name */
        private c f13383i;

        /* renamed from: j, reason: collision with root package name */
        private byte f13384j;

        /* renamed from: k, reason: collision with root package name */
        private int f13385k;

        /* compiled from: ProtoBuf.java */
        /* renamed from: jc.b$b$a */
        /* loaded from: classes2.dex */
        static class a extends qc.b<C0061b> {
            a() {
            }

            @Override // qc.s
            /* renamed from: m, reason: merged with bridge method [inline-methods] */
            public C0061b a(qc.e eVar, qc.g gVar) {
                return new C0061b(eVar, gVar);
            }
        }

        /* compiled from: ProtoBuf.java */
        /* renamed from: jc.b$b$b, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0062b extends i.b<C0061b, C0062b> implements qc.r {

            /* renamed from: f, reason: collision with root package name */
            private int f13386f;

            /* renamed from: g, reason: collision with root package name */
            private int f13387g;

            /* renamed from: h, reason: collision with root package name */
            private c f13388h = c.G();

            private C0062b() {
                n();
            }

            static /* synthetic */ C0062b h() {
                return l();
            }

            private static C0062b l() {
                return new C0062b();
            }

            private void n() {
            }

            @Override // qc.q.a
            /* renamed from: i, reason: merged with bridge method [inline-methods] */
            public C0061b build() {
                C0061b j10 = j();
                if (j10.isInitialized()) {
                    return j10;
                }
                throw a.AbstractC0092a.c(j10);
            }

            public C0061b j() {
                C0061b c0061b = new C0061b(this);
                int i10 = this.f13386f;
                int i11 = (i10 & 1) != 1 ? 0 : 1;
                c0061b.f13382h = this.f13387g;
                if ((i10 & 2) == 2) {
                    i11 |= 2;
                }
                c0061b.f13383i = this.f13388h;
                c0061b.f13381g = i11;
                return c0061b;
            }

            @Override // qc.i.b
            /* renamed from: k, reason: merged with bridge method [inline-methods] */
            public C0062b d() {
                return l().f(j());
            }

            @Override // qc.i.b
            /* renamed from: o, reason: merged with bridge method [inline-methods] */
            public C0062b f(C0061b c0061b) {
                if (c0061b == C0061b.q()) {
                    return this;
                }
                if (c0061b.t()) {
                    r(c0061b.r());
                }
                if (c0061b.u()) {
                    q(c0061b.s());
                }
                g(e().d(c0061b.f13380f));
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
            @Override // qc.a.AbstractC0092a, qc.q.a
            /* renamed from: p, reason: merged with bridge method [inline-methods] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public C0062b m(qc.e eVar, qc.g gVar) {
                C0061b c0061b = null;
                try {
                    try {
                        C0061b a10 = C0061b.f13379m.a(eVar, gVar);
                        if (a10 != null) {
                            f(a10);
                        }
                        return this;
                    } catch (Throwable th) {
                        th = th;
                        if (c0061b != null) {
                        }
                        throw th;
                    }
                } catch (qc.k e10) {
                    C0061b c0061b2 = (C0061b) e10.a();
                    try {
                        throw e10;
                    } catch (Throwable th2) {
                        th = th2;
                        c0061b = c0061b2;
                        if (c0061b != null) {
                            f(c0061b);
                        }
                        throw th;
                    }
                }
            }

            public C0062b q(c cVar) {
                if ((this.f13386f & 2) == 2 && this.f13388h != c.G()) {
                    this.f13388h = c.a0(this.f13388h).f(cVar).j();
                } else {
                    this.f13388h = cVar;
                }
                this.f13386f |= 2;
                return this;
            }

            public C0062b r(int i10) {
                this.f13386f |= 1;
                this.f13387g = i10;
                return this;
            }
        }

        static {
            C0061b c0061b = new C0061b(true);
            f13378l = c0061b;
            c0061b.v();
        }

        public static C0061b q() {
            return f13378l;
        }

        private void v() {
            this.f13382h = 0;
            this.f13383i = c.G();
        }

        public static C0062b w() {
            return C0062b.h();
        }

        public static C0062b x(C0061b c0061b) {
            return w().f(c0061b);
        }

        @Override // qc.q
        public void a(qc.f fVar) {
            getSerializedSize();
            if ((this.f13381g & 1) == 1) {
                fVar.a0(1, this.f13382h);
            }
            if ((this.f13381g & 2) == 2) {
                fVar.d0(2, this.f13383i);
            }
            fVar.i0(this.f13380f);
        }

        @Override // qc.i, qc.q
        public qc.s<C0061b> getParserForType() {
            return f13379m;
        }

        @Override // qc.q
        public int getSerializedSize() {
            int i10 = this.f13385k;
            if (i10 != -1) {
                return i10;
            }
            int o10 = (this.f13381g & 1) == 1 ? 0 + qc.f.o(1, this.f13382h) : 0;
            if ((this.f13381g & 2) == 2) {
                o10 += qc.f.s(2, this.f13383i);
            }
            int size = o10 + this.f13380f.size();
            this.f13385k = size;
            return size;
        }

        @Override // qc.r
        public final boolean isInitialized() {
            byte b10 = this.f13384j;
            if (b10 == 1) {
                return true;
            }
            if (b10 == 0) {
                return false;
            }
            if (!t()) {
                this.f13384j = (byte) 0;
                return false;
            }
            if (!u()) {
                this.f13384j = (byte) 0;
                return false;
            }
            if (!s().isInitialized()) {
                this.f13384j = (byte) 0;
                return false;
            }
            this.f13384j = (byte) 1;
            return true;
        }

        public int r() {
            return this.f13382h;
        }

        public c s() {
            return this.f13383i;
        }

        public boolean t() {
            return (this.f13381g & 1) == 1;
        }

        public boolean u() {
            return (this.f13381g & 2) == 2;
        }

        @Override // qc.q
        /* renamed from: y, reason: merged with bridge method [inline-methods] */
        public C0062b newBuilderForType() {
            return w();
        }

        @Override // qc.q
        /* renamed from: z, reason: merged with bridge method [inline-methods] */
        public C0062b toBuilder() {
            return x(this);
        }

        /* compiled from: ProtoBuf.java */
        /* renamed from: jc.b$b$c */
        /* loaded from: classes2.dex */
        public static final class c extends qc.i implements qc.r {

            /* renamed from: u, reason: collision with root package name */
            private static final c f13389u;

            /* renamed from: v, reason: collision with root package name */
            public static qc.s<c> f13390v = new a();

            /* renamed from: f, reason: collision with root package name */
            private final qc.d f13391f;

            /* renamed from: g, reason: collision with root package name */
            private int f13392g;

            /* renamed from: h, reason: collision with root package name */
            private EnumC0064c f13393h;

            /* renamed from: i, reason: collision with root package name */
            private long f13394i;

            /* renamed from: j, reason: collision with root package name */
            private float f13395j;

            /* renamed from: k, reason: collision with root package name */
            private double f13396k;

            /* renamed from: l, reason: collision with root package name */
            private int f13397l;

            /* renamed from: m, reason: collision with root package name */
            private int f13398m;

            /* renamed from: n, reason: collision with root package name */
            private int f13399n;

            /* renamed from: o, reason: collision with root package name */
            private b f13400o;

            /* renamed from: p, reason: collision with root package name */
            private List<c> f13401p;

            /* renamed from: q, reason: collision with root package name */
            private int f13402q;

            /* renamed from: r, reason: collision with root package name */
            private int f13403r;

            /* renamed from: s, reason: collision with root package name */
            private byte f13404s;

            /* renamed from: t, reason: collision with root package name */
            private int f13405t;

            /* compiled from: ProtoBuf.java */
            /* renamed from: jc.b$b$c$a */
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
            /* renamed from: jc.b$b$c$b, reason: collision with other inner class name */
            /* loaded from: classes2.dex */
            public static final class C0063b extends i.b<c, C0063b> implements qc.r {

                /* renamed from: f, reason: collision with root package name */
                private int f13406f;

                /* renamed from: h, reason: collision with root package name */
                private long f13408h;

                /* renamed from: i, reason: collision with root package name */
                private float f13409i;

                /* renamed from: j, reason: collision with root package name */
                private double f13410j;

                /* renamed from: k, reason: collision with root package name */
                private int f13411k;

                /* renamed from: l, reason: collision with root package name */
                private int f13412l;

                /* renamed from: m, reason: collision with root package name */
                private int f13413m;

                /* renamed from: p, reason: collision with root package name */
                private int f13416p;

                /* renamed from: q, reason: collision with root package name */
                private int f13417q;

                /* renamed from: g, reason: collision with root package name */
                private EnumC0064c f13407g = EnumC0064c.BYTE;

                /* renamed from: n, reason: collision with root package name */
                private b f13414n = b.u();

                /* renamed from: o, reason: collision with root package name */
                private List<c> f13415o = Collections.emptyList();

                private C0063b() {
                    o();
                }

                static /* synthetic */ C0063b h() {
                    return l();
                }

                private static C0063b l() {
                    return new C0063b();
                }

                private void n() {
                    if ((this.f13406f & 256) != 256) {
                        this.f13415o = new ArrayList(this.f13415o);
                        this.f13406f |= 256;
                    }
                }

                private void o() {
                }

                public C0063b A(EnumC0064c enumC0064c) {
                    Objects.requireNonNull(enumC0064c);
                    this.f13406f |= 1;
                    this.f13407g = enumC0064c;
                    return this;
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
                    int i10 = this.f13406f;
                    int i11 = (i10 & 1) != 1 ? 0 : 1;
                    cVar.f13393h = this.f13407g;
                    if ((i10 & 2) == 2) {
                        i11 |= 2;
                    }
                    cVar.f13394i = this.f13408h;
                    if ((i10 & 4) == 4) {
                        i11 |= 4;
                    }
                    cVar.f13395j = this.f13409i;
                    if ((i10 & 8) == 8) {
                        i11 |= 8;
                    }
                    cVar.f13396k = this.f13410j;
                    if ((i10 & 16) == 16) {
                        i11 |= 16;
                    }
                    cVar.f13397l = this.f13411k;
                    if ((i10 & 32) == 32) {
                        i11 |= 32;
                    }
                    cVar.f13398m = this.f13412l;
                    if ((i10 & 64) == 64) {
                        i11 |= 64;
                    }
                    cVar.f13399n = this.f13413m;
                    if ((i10 & 128) == 128) {
                        i11 |= 128;
                    }
                    cVar.f13400o = this.f13414n;
                    if ((this.f13406f & 256) == 256) {
                        this.f13415o = Collections.unmodifiableList(this.f13415o);
                        this.f13406f &= -257;
                    }
                    cVar.f13401p = this.f13415o;
                    if ((i10 & 512) == 512) {
                        i11 |= 256;
                    }
                    cVar.f13402q = this.f13416p;
                    if ((i10 & 1024) == 1024) {
                        i11 |= 512;
                    }
                    cVar.f13403r = this.f13417q;
                    cVar.f13392g = i11;
                    return cVar;
                }

                @Override // qc.i.b
                /* renamed from: k, reason: merged with bridge method [inline-methods] */
                public C0063b d() {
                    return l().f(j());
                }

                public C0063b p(b bVar) {
                    if ((this.f13406f & 128) == 128 && this.f13414n != b.u()) {
                        this.f13414n = b.z(this.f13414n).f(bVar).j();
                    } else {
                        this.f13414n = bVar;
                    }
                    this.f13406f |= 128;
                    return this;
                }

                @Override // qc.i.b
                /* renamed from: q, reason: merged with bridge method [inline-methods] */
                public C0063b f(c cVar) {
                    if (cVar == c.G()) {
                        return this;
                    }
                    if (cVar.X()) {
                        A(cVar.N());
                    }
                    if (cVar.V()) {
                        y(cVar.L());
                    }
                    if (cVar.U()) {
                        x(cVar.K());
                    }
                    if (cVar.R()) {
                        u(cVar.H());
                    }
                    if (cVar.W()) {
                        z(cVar.M());
                    }
                    if (cVar.Q()) {
                        t(cVar.F());
                    }
                    if (cVar.S()) {
                        v(cVar.I());
                    }
                    if (cVar.O()) {
                        p(cVar.A());
                    }
                    if (!cVar.f13401p.isEmpty()) {
                        if (this.f13415o.isEmpty()) {
                            this.f13415o = cVar.f13401p;
                            this.f13406f &= -257;
                        } else {
                            n();
                            this.f13415o.addAll(cVar.f13401p);
                        }
                    }
                    if (cVar.P()) {
                        s(cVar.B());
                    }
                    if (cVar.T()) {
                        w(cVar.J());
                    }
                    g(e().d(cVar.f13391f));
                    return this;
                }

                /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
                @Override // qc.a.AbstractC0092a, qc.q.a
                /* renamed from: r, reason: merged with bridge method [inline-methods] */
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public C0063b m(qc.e eVar, qc.g gVar) {
                    c cVar = null;
                    try {
                        try {
                            c a10 = c.f13390v.a(eVar, gVar);
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

                public C0063b s(int i10) {
                    this.f13406f |= 512;
                    this.f13416p = i10;
                    return this;
                }

                public C0063b t(int i10) {
                    this.f13406f |= 32;
                    this.f13412l = i10;
                    return this;
                }

                public C0063b u(double d10) {
                    this.f13406f |= 8;
                    this.f13410j = d10;
                    return this;
                }

                public C0063b v(int i10) {
                    this.f13406f |= 64;
                    this.f13413m = i10;
                    return this;
                }

                public C0063b w(int i10) {
                    this.f13406f |= 1024;
                    this.f13417q = i10;
                    return this;
                }

                public C0063b x(float f10) {
                    this.f13406f |= 4;
                    this.f13409i = f10;
                    return this;
                }

                public C0063b y(long j10) {
                    this.f13406f |= 2;
                    this.f13408h = j10;
                    return this;
                }

                public C0063b z(int i10) {
                    this.f13406f |= 16;
                    this.f13411k = i10;
                    return this;
                }
            }

            /* compiled from: ProtoBuf.java */
            /* renamed from: jc.b$b$c$c, reason: collision with other inner class name */
            /* loaded from: classes2.dex */
            public enum EnumC0064c implements j.a {
                BYTE(0, 0),
                CHAR(1, 1),
                SHORT(2, 2),
                INT(3, 3),
                LONG(4, 4),
                FLOAT(5, 5),
                DOUBLE(6, 6),
                BOOLEAN(7, 7),
                STRING(8, 8),
                CLASS(9, 9),
                ENUM(10, 10),
                ANNOTATION(11, 11),
                ARRAY(12, 12);


                /* renamed from: s, reason: collision with root package name */
                private static j.b<EnumC0064c> f13431s = new a();

                /* renamed from: e, reason: collision with root package name */
                private final int f13433e;

                /* compiled from: ProtoBuf.java */
                /* renamed from: jc.b$b$c$c$a */
                /* loaded from: classes2.dex */
                static class a implements j.b<EnumC0064c> {
                    a() {
                    }

                    @Override // qc.j.b
                    /* renamed from: a, reason: merged with bridge method [inline-methods] */
                    public EnumC0064c findValueByNumber(int i10) {
                        return EnumC0064c.a(i10);
                    }
                }

                EnumC0064c(int i10, int i11) {
                    this.f13433e = i11;
                }

                public static EnumC0064c a(int i10) {
                    switch (i10) {
                        case 0:
                            return BYTE;
                        case 1:
                            return CHAR;
                        case 2:
                            return SHORT;
                        case 3:
                            return INT;
                        case 4:
                            return LONG;
                        case 5:
                            return FLOAT;
                        case 6:
                            return DOUBLE;
                        case 7:
                            return BOOLEAN;
                        case 8:
                            return STRING;
                        case 9:
                            return CLASS;
                        case 10:
                            return ENUM;
                        case 11:
                            return ANNOTATION;
                        case 12:
                            return ARRAY;
                        default:
                            return null;
                    }
                }

                @Override // qc.j.a
                public final int getNumber() {
                    return this.f13433e;
                }
            }

            static {
                c cVar = new c(true);
                f13389u = cVar;
                cVar.Y();
            }

            public static c G() {
                return f13389u;
            }

            private void Y() {
                this.f13393h = EnumC0064c.BYTE;
                this.f13394i = 0L;
                this.f13395j = 0.0f;
                this.f13396k = UserProfileInfo.Constant.NA_LAT_LON;
                this.f13397l = 0;
                this.f13398m = 0;
                this.f13399n = 0;
                this.f13400o = b.u();
                this.f13401p = Collections.emptyList();
                this.f13402q = 0;
                this.f13403r = 0;
            }

            public static C0063b Z() {
                return C0063b.h();
            }

            public static C0063b a0(c cVar) {
                return Z().f(cVar);
            }

            public b A() {
                return this.f13400o;
            }

            public int B() {
                return this.f13402q;
            }

            public c C(int i10) {
                return this.f13401p.get(i10);
            }

            public int D() {
                return this.f13401p.size();
            }

            public List<c> E() {
                return this.f13401p;
            }

            public int F() {
                return this.f13398m;
            }

            public double H() {
                return this.f13396k;
            }

            public int I() {
                return this.f13399n;
            }

            public int J() {
                return this.f13403r;
            }

            public float K() {
                return this.f13395j;
            }

            public long L() {
                return this.f13394i;
            }

            public int M() {
                return this.f13397l;
            }

            public EnumC0064c N() {
                return this.f13393h;
            }

            public boolean O() {
                return (this.f13392g & 128) == 128;
            }

            public boolean P() {
                return (this.f13392g & 256) == 256;
            }

            public boolean Q() {
                return (this.f13392g & 32) == 32;
            }

            public boolean R() {
                return (this.f13392g & 8) == 8;
            }

            public boolean S() {
                return (this.f13392g & 64) == 64;
            }

            public boolean T() {
                return (this.f13392g & 512) == 512;
            }

            public boolean U() {
                return (this.f13392g & 4) == 4;
            }

            public boolean V() {
                return (this.f13392g & 2) == 2;
            }

            public boolean W() {
                return (this.f13392g & 16) == 16;
            }

            public boolean X() {
                return (this.f13392g & 1) == 1;
            }

            @Override // qc.q
            public void a(qc.f fVar) {
                getSerializedSize();
                if ((this.f13392g & 1) == 1) {
                    fVar.S(1, this.f13393h.getNumber());
                }
                if ((this.f13392g & 2) == 2) {
                    fVar.t0(2, this.f13394i);
                }
                if ((this.f13392g & 4) == 4) {
                    fVar.W(3, this.f13395j);
                }
                if ((this.f13392g & 8) == 8) {
                    fVar.Q(4, this.f13396k);
                }
                if ((this.f13392g & 16) == 16) {
                    fVar.a0(5, this.f13397l);
                }
                if ((this.f13392g & 32) == 32) {
                    fVar.a0(6, this.f13398m);
                }
                if ((this.f13392g & 64) == 64) {
                    fVar.a0(7, this.f13399n);
                }
                if ((this.f13392g & 128) == 128) {
                    fVar.d0(8, this.f13400o);
                }
                for (int i10 = 0; i10 < this.f13401p.size(); i10++) {
                    fVar.d0(9, this.f13401p.get(i10));
                }
                if ((this.f13392g & 512) == 512) {
                    fVar.a0(10, this.f13403r);
                }
                if ((this.f13392g & 256) == 256) {
                    fVar.a0(11, this.f13402q);
                }
                fVar.i0(this.f13391f);
            }

            @Override // qc.q
            /* renamed from: b0, reason: merged with bridge method [inline-methods] */
            public C0063b newBuilderForType() {
                return Z();
            }

            @Override // qc.q
            /* renamed from: c0, reason: merged with bridge method [inline-methods] */
            public C0063b toBuilder() {
                return a0(this);
            }

            @Override // qc.i, qc.q
            public qc.s<c> getParserForType() {
                return f13390v;
            }

            @Override // qc.q
            public int getSerializedSize() {
                int i10 = this.f13405t;
                if (i10 != -1) {
                    return i10;
                }
                int h10 = (this.f13392g & 1) == 1 ? qc.f.h(1, this.f13393h.getNumber()) + 0 : 0;
                if ((this.f13392g & 2) == 2) {
                    h10 += qc.f.A(2, this.f13394i);
                }
                if ((this.f13392g & 4) == 4) {
                    h10 += qc.f.l(3, this.f13395j);
                }
                if ((this.f13392g & 8) == 8) {
                    h10 += qc.f.f(4, this.f13396k);
                }
                if ((this.f13392g & 16) == 16) {
                    h10 += qc.f.o(5, this.f13397l);
                }
                if ((this.f13392g & 32) == 32) {
                    h10 += qc.f.o(6, this.f13398m);
                }
                if ((this.f13392g & 64) == 64) {
                    h10 += qc.f.o(7, this.f13399n);
                }
                if ((this.f13392g & 128) == 128) {
                    h10 += qc.f.s(8, this.f13400o);
                }
                for (int i11 = 0; i11 < this.f13401p.size(); i11++) {
                    h10 += qc.f.s(9, this.f13401p.get(i11));
                }
                if ((this.f13392g & 512) == 512) {
                    h10 += qc.f.o(10, this.f13403r);
                }
                if ((this.f13392g & 256) == 256) {
                    h10 += qc.f.o(11, this.f13402q);
                }
                int size = h10 + this.f13391f.size();
                this.f13405t = size;
                return size;
            }

            @Override // qc.r
            public final boolean isInitialized() {
                byte b10 = this.f13404s;
                if (b10 == 1) {
                    return true;
                }
                if (b10 == 0) {
                    return false;
                }
                if (O() && !A().isInitialized()) {
                    this.f13404s = (byte) 0;
                    return false;
                }
                for (int i10 = 0; i10 < D(); i10++) {
                    if (!C(i10).isInitialized()) {
                        this.f13404s = (byte) 0;
                        return false;
                    }
                }
                this.f13404s = (byte) 1;
                return true;
            }

            private c(i.b bVar) {
                super(bVar);
                this.f13404s = (byte) -1;
                this.f13405t = -1;
                this.f13391f = bVar.e();
            }

            private c(boolean z10) {
                this.f13404s = (byte) -1;
                this.f13405t = -1;
                this.f13391f = qc.d.f17259e;
            }

            /* JADX WARN: Failed to find 'out' block for switch in B:6:0x001e. Please report as an issue. */
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r5v0 */
            /* JADX WARN: Type inference failed for: r5v1 */
            /* JADX WARN: Type inference failed for: r5v2, types: [boolean] */
            private c(qc.e eVar, qc.g gVar) {
                this.f13404s = (byte) -1;
                this.f13405t = -1;
                Y();
                d.b q10 = qc.d.q();
                qc.f J = qc.f.J(q10, 1);
                boolean z10 = false;
                int i10 = 0;
                while (true) {
                    ?? r52 = 256;
                    if (!z10) {
                        try {
                            try {
                                int K = eVar.K();
                                switch (K) {
                                    case 0:
                                        z10 = true;
                                    case 8:
                                        int n10 = eVar.n();
                                        EnumC0064c a10 = EnumC0064c.a(n10);
                                        if (a10 == null) {
                                            J.o0(K);
                                            J.o0(n10);
                                        } else {
                                            this.f13392g |= 1;
                                            this.f13393h = a10;
                                        }
                                    case 16:
                                        this.f13392g |= 2;
                                        this.f13394i = eVar.H();
                                    case 29:
                                        this.f13392g |= 4;
                                        this.f13395j = eVar.q();
                                    case 33:
                                        this.f13392g |= 8;
                                        this.f13396k = eVar.m();
                                    case 40:
                                        this.f13392g |= 16;
                                        this.f13397l = eVar.s();
                                    case 48:
                                        this.f13392g |= 32;
                                        this.f13398m = eVar.s();
                                    case 56:
                                        this.f13392g |= 64;
                                        this.f13399n = eVar.s();
                                    case 66:
                                        c builder = (this.f13392g & 128) == 128 ? this.f13400o.toBuilder() : null;
                                        b bVar = (b) eVar.u(b.f13371m, gVar);
                                        this.f13400o = bVar;
                                        if (builder != null) {
                                            builder.f(bVar);
                                            this.f13400o = builder.j();
                                        }
                                        this.f13392g |= 128;
                                    case 74:
                                        if ((i10 & 256) != 256) {
                                            this.f13401p = new ArrayList();
                                            i10 |= 256;
                                        }
                                        this.f13401p.add(eVar.u(f13390v, gVar));
                                    case 80:
                                        this.f13392g |= 512;
                                        this.f13403r = eVar.s();
                                    case 88:
                                        this.f13392g |= 256;
                                        this.f13402q = eVar.s();
                                    default:
                                        r52 = j(eVar, J, gVar, K);
                                        if (r52 == 0) {
                                            z10 = true;
                                        }
                                }
                            } catch (qc.k e10) {
                                throw e10.i(this);
                            } catch (IOException e11) {
                                throw new qc.k(e11.getMessage()).i(this);
                            }
                        } catch (Throwable th) {
                            if ((i10 & 256) == r52) {
                                this.f13401p = Collections.unmodifiableList(this.f13401p);
                            }
                            try {
                                J.I();
                            } catch (IOException unused) {
                            } catch (Throwable th2) {
                                this.f13391f = q10.v();
                                throw th2;
                            }
                            this.f13391f = q10.v();
                            g();
                            throw th;
                        }
                    } else {
                        if ((i10 & 256) == 256) {
                            this.f13401p = Collections.unmodifiableList(this.f13401p);
                        }
                        try {
                            J.I();
                        } catch (IOException unused2) {
                        } catch (Throwable th3) {
                            this.f13391f = q10.v();
                            throw th3;
                        }
                        this.f13391f = q10.v();
                        g();
                        return;
                    }
                }
            }
        }

        private C0061b(i.b bVar) {
            super(bVar);
            this.f13384j = (byte) -1;
            this.f13385k = -1;
            this.f13380f = bVar.e();
        }

        private C0061b(boolean z10) {
            this.f13384j = (byte) -1;
            this.f13385k = -1;
            this.f13380f = qc.d.f17259e;
        }

        private C0061b(qc.e eVar, qc.g gVar) {
            this.f13384j = (byte) -1;
            this.f13385k = -1;
            v();
            d.b q10 = qc.d.q();
            qc.f J = qc.f.J(q10, 1);
            boolean z10 = false;
            while (!z10) {
                try {
                    try {
                        int K = eVar.K();
                        if (K != 0) {
                            if (K == 8) {
                                this.f13381g |= 1;
                                this.f13382h = eVar.s();
                            } else if (K != 18) {
                                if (!j(eVar, J, gVar, K)) {
                                }
                            } else {
                                c.C0063b builder = (this.f13381g & 2) == 2 ? this.f13383i.toBuilder() : null;
                                c cVar = (c) eVar.u(c.f13390v, gVar);
                                this.f13383i = cVar;
                                if (builder != null) {
                                    builder.f(cVar);
                                    this.f13383i = builder.j();
                                }
                                this.f13381g |= 2;
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
                        this.f13380f = q10.v();
                        throw th2;
                    }
                    this.f13380f = q10.v();
                    g();
                    throw th;
                }
            }
            try {
                J.I();
            } catch (IOException unused2) {
            } catch (Throwable th3) {
                this.f13380f = q10.v();
                throw th3;
            }
            this.f13380f = q10.v();
            g();
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class c extends i.b<b, c> implements qc.r {

        /* renamed from: f, reason: collision with root package name */
        private int f13434f;

        /* renamed from: g, reason: collision with root package name */
        private int f13435g;

        /* renamed from: h, reason: collision with root package name */
        private List<C0061b> f13436h = Collections.emptyList();

        private c() {
            o();
        }

        static /* synthetic */ c h() {
            return l();
        }

        private static c l() {
            return new c();
        }

        private void n() {
            if ((this.f13434f & 2) != 2) {
                this.f13436h = new ArrayList(this.f13436h);
                this.f13434f |= 2;
            }
        }

        private void o() {
        }

        @Override // qc.q.a
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public b build() {
            b j10 = j();
            if (j10.isInitialized()) {
                return j10;
            }
            throw a.AbstractC0092a.c(j10);
        }

        public b j() {
            b bVar = new b(this);
            int i10 = (this.f13434f & 1) != 1 ? 0 : 1;
            bVar.f13374h = this.f13435g;
            if ((this.f13434f & 2) == 2) {
                this.f13436h = Collections.unmodifiableList(this.f13436h);
                this.f13434f &= -3;
            }
            bVar.f13375i = this.f13436h;
            bVar.f13373g = i10;
            return bVar;
        }

        @Override // qc.i.b
        /* renamed from: k, reason: merged with bridge method [inline-methods] */
        public c d() {
            return l().f(j());
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public c f(b bVar) {
            if (bVar == b.u()) {
                return this;
            }
            if (bVar.w()) {
                r(bVar.v());
            }
            if (!bVar.f13375i.isEmpty()) {
                if (this.f13436h.isEmpty()) {
                    this.f13436h = bVar.f13375i;
                    this.f13434f &= -3;
                } else {
                    n();
                    this.f13436h.addAll(bVar.f13375i);
                }
            }
            g(e().d(bVar.f13372f));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: q, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public c m(qc.e eVar, qc.g gVar) {
            b bVar = null;
            try {
                try {
                    b a10 = b.f13371m.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (bVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                b bVar2 = (b) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    bVar = bVar2;
                    if (bVar != null) {
                        f(bVar);
                    }
                    throw th;
                }
            }
        }

        public c r(int i10) {
            this.f13434f |= 1;
            this.f13435g = i10;
            return this;
        }
    }

    static {
        b bVar = new b(true);
        f13370l = bVar;
        bVar.x();
    }

    public static b u() {
        return f13370l;
    }

    private void x() {
        this.f13374h = 0;
        this.f13375i = Collections.emptyList();
    }

    public static c y() {
        return c.h();
    }

    public static c z(b bVar) {
        return y().f(bVar);
    }

    @Override // qc.q
    /* renamed from: A, reason: merged with bridge method [inline-methods] */
    public c newBuilderForType() {
        return y();
    }

    @Override // qc.q
    /* renamed from: B, reason: merged with bridge method [inline-methods] */
    public c toBuilder() {
        return z(this);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        if ((this.f13373g & 1) == 1) {
            fVar.a0(1, this.f13374h);
        }
        for (int i10 = 0; i10 < this.f13375i.size(); i10++) {
            fVar.d0(2, this.f13375i.get(i10));
        }
        fVar.i0(this.f13372f);
    }

    @Override // qc.i, qc.q
    public qc.s<b> getParserForType() {
        return f13371m;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13377k;
        if (i10 != -1) {
            return i10;
        }
        int o10 = (this.f13373g & 1) == 1 ? qc.f.o(1, this.f13374h) + 0 : 0;
        for (int i11 = 0; i11 < this.f13375i.size(); i11++) {
            o10 += qc.f.s(2, this.f13375i.get(i11));
        }
        int size = o10 + this.f13372f.size();
        this.f13377k = size;
        return size;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13376j;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        if (!w()) {
            this.f13376j = (byte) 0;
            return false;
        }
        for (int i10 = 0; i10 < s(); i10++) {
            if (!r(i10).isInitialized()) {
                this.f13376j = (byte) 0;
                return false;
            }
        }
        this.f13376j = (byte) 1;
        return true;
    }

    public C0061b r(int i10) {
        return this.f13375i.get(i10);
    }

    public int s() {
        return this.f13375i.size();
    }

    public List<C0061b> t() {
        return this.f13375i;
    }

    public int v() {
        return this.f13374h;
    }

    public boolean w() {
        return (this.f13373g & 1) == 1;
    }

    private b(i.b bVar) {
        super(bVar);
        this.f13376j = (byte) -1;
        this.f13377k = -1;
        this.f13372f = bVar.e();
    }

    private b(boolean z10) {
        this.f13376j = (byte) -1;
        this.f13377k = -1;
        this.f13372f = qc.d.f17259e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private b(qc.e eVar, qc.g gVar) {
        this.f13376j = (byte) -1;
        this.f13377k = -1;
        x();
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
                            this.f13373g |= 1;
                            this.f13374h = eVar.s();
                        } else if (K != 18) {
                            if (!j(eVar, J, gVar, K)) {
                            }
                        } else {
                            if ((i10 & 2) != 2) {
                                this.f13375i = new ArrayList();
                                i10 |= 2;
                            }
                            this.f13375i.add(eVar.u(C0061b.f13379m, gVar));
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
                    this.f13375i = Collections.unmodifiableList(this.f13375i);
                }
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13372f = q10.v();
                    throw th2;
                }
                this.f13372f = q10.v();
                g();
                throw th;
            }
        }
        if ((i10 & 2) == 2) {
            this.f13375i = Collections.unmodifiableList(this.f13375i);
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13372f = q10.v();
            throw th3;
        }
        this.f13372f = q10.v();
        g();
    }
}
