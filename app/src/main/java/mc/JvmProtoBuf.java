package mc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import jc.l;
import jc.n;
import jc.q;
import jc.s;
import qc.a;
import qc.d;
import qc.f;
import qc.g;
import qc.i;
import qc.j;
import qc.k;
import qc.r;
import qc.z;

/* compiled from: JvmProtoBuf.java */
/* renamed from: mc.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class JvmProtoBuf {

    /* renamed from: a, reason: collision with root package name */
    public static final i.f<jc.d, c> f15364a;

    /* renamed from: b, reason: collision with root package name */
    public static final i.f<jc.i, c> f15365b;

    /* renamed from: c, reason: collision with root package name */
    public static final i.f<jc.i, Integer> f15366c;

    /* renamed from: d, reason: collision with root package name */
    public static final i.f<n, d> f15367d;

    /* renamed from: e, reason: collision with root package name */
    public static final i.f<n, Integer> f15368e;

    /* renamed from: f, reason: collision with root package name */
    public static final i.f<q, List<jc.b>> f15369f;

    /* renamed from: g, reason: collision with root package name */
    public static final i.f<q, Boolean> f15370g;

    /* renamed from: h, reason: collision with root package name */
    public static final i.f<s, List<jc.b>> f15371h;

    /* renamed from: i, reason: collision with root package name */
    public static final i.f<jc.c, Integer> f15372i;

    /* renamed from: j, reason: collision with root package name */
    public static final i.f<jc.c, List<n>> f15373j;

    /* renamed from: k, reason: collision with root package name */
    public static final i.f<jc.c, Integer> f15374k;

    /* renamed from: l, reason: collision with root package name */
    public static final i.f<jc.c, Integer> f15375l;

    /* renamed from: m, reason: collision with root package name */
    public static final i.f<l, Integer> f15376m;

    /* renamed from: n, reason: collision with root package name */
    public static final i.f<l, List<n>> f15377n;

    /* compiled from: JvmProtoBuf.java */
    /* renamed from: mc.a$e */
    /* loaded from: classes2.dex */
    public static final class e extends i implements r {

        /* renamed from: l, reason: collision with root package name */
        private static final e f15417l;

        /* renamed from: m, reason: collision with root package name */
        public static qc.s<e> f15418m = new a();

        /* renamed from: f, reason: collision with root package name */
        private final qc.d f15419f;

        /* renamed from: g, reason: collision with root package name */
        private List<c> f15420g;

        /* renamed from: h, reason: collision with root package name */
        private List<Integer> f15421h;

        /* renamed from: i, reason: collision with root package name */
        private int f15422i;

        /* renamed from: j, reason: collision with root package name */
        private byte f15423j;

        /* renamed from: k, reason: collision with root package name */
        private int f15424k;

        /* compiled from: JvmProtoBuf.java */
        /* renamed from: mc.a$e$a */
        /* loaded from: classes2.dex */
        static class a extends qc.b<e> {
            a() {
            }

            @Override // qc.s
            /* renamed from: m, reason: merged with bridge method [inline-methods] */
            public e a(qc.e eVar, g gVar) {
                return new e(eVar, gVar);
            }
        }

        /* compiled from: JvmProtoBuf.java */
        /* renamed from: mc.a$e$b */
        /* loaded from: classes2.dex */
        public static final class b extends i.b<e, b> implements r {

            /* renamed from: f, reason: collision with root package name */
            private int f15425f;

            /* renamed from: g, reason: collision with root package name */
            private List<c> f15426g = Collections.emptyList();

            /* renamed from: h, reason: collision with root package name */
            private List<Integer> f15427h = Collections.emptyList();

            private b() {
                p();
            }

            static /* synthetic */ b h() {
                return l();
            }

            private static b l() {
                return new b();
            }

            private void n() {
                if ((this.f15425f & 2) != 2) {
                    this.f15427h = new ArrayList(this.f15427h);
                    this.f15425f |= 2;
                }
            }

            private void o() {
                if ((this.f15425f & 1) != 1) {
                    this.f15426g = new ArrayList(this.f15426g);
                    this.f15425f |= 1;
                }
            }

            private void p() {
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
                if ((this.f15425f & 1) == 1) {
                    this.f15426g = Collections.unmodifiableList(this.f15426g);
                    this.f15425f &= -2;
                }
                eVar.f15420g = this.f15426g;
                if ((this.f15425f & 2) == 2) {
                    this.f15427h = Collections.unmodifiableList(this.f15427h);
                    this.f15425f &= -3;
                }
                eVar.f15421h = this.f15427h;
                return eVar;
            }

            @Override // qc.i.b
            /* renamed from: k, reason: merged with bridge method [inline-methods] */
            public b d() {
                return l().f(j());
            }

            @Override // qc.i.b
            /* renamed from: q, reason: merged with bridge method [inline-methods] */
            public b f(e eVar) {
                if (eVar == e.r()) {
                    return this;
                }
                if (!eVar.f15420g.isEmpty()) {
                    if (this.f15426g.isEmpty()) {
                        this.f15426g = eVar.f15420g;
                        this.f15425f &= -2;
                    } else {
                        o();
                        this.f15426g.addAll(eVar.f15420g);
                    }
                }
                if (!eVar.f15421h.isEmpty()) {
                    if (this.f15427h.isEmpty()) {
                        this.f15427h = eVar.f15421h;
                        this.f15425f &= -3;
                    } else {
                        n();
                        this.f15427h.addAll(eVar.f15421h);
                    }
                }
                g(e().d(eVar.f15419f));
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
            @Override // qc.a.AbstractC0092a, qc.q.a
            /* renamed from: r, reason: merged with bridge method [inline-methods] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public b m(qc.e eVar, g gVar) {
                e eVar2 = null;
                try {
                    try {
                        e a10 = e.f15418m.a(eVar, gVar);
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
                } catch (k e10) {
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
            f15417l = eVar;
            eVar.u();
        }

        public static e r() {
            return f15417l;
        }

        private void u() {
            this.f15420g = Collections.emptyList();
            this.f15421h = Collections.emptyList();
        }

        public static b v() {
            return b.h();
        }

        public static b w(e eVar) {
            return v().f(eVar);
        }

        public static e y(InputStream inputStream, g gVar) {
            return f15418m.b(inputStream, gVar);
        }

        @Override // qc.q
        public void a(f fVar) {
            getSerializedSize();
            for (int i10 = 0; i10 < this.f15420g.size(); i10++) {
                fVar.d0(1, this.f15420g.get(i10));
            }
            if (s().size() > 0) {
                fVar.o0(42);
                fVar.o0(this.f15422i);
            }
            for (int i11 = 0; i11 < this.f15421h.size(); i11++) {
                fVar.b0(this.f15421h.get(i11).intValue());
            }
            fVar.i0(this.f15419f);
        }

        @Override // qc.i, qc.q
        public qc.s<e> getParserForType() {
            return f15418m;
        }

        @Override // qc.q
        public int getSerializedSize() {
            int i10 = this.f15424k;
            if (i10 != -1) {
                return i10;
            }
            int i11 = 0;
            for (int i12 = 0; i12 < this.f15420g.size(); i12++) {
                i11 += f.s(1, this.f15420g.get(i12));
            }
            int i13 = 0;
            for (int i14 = 0; i14 < this.f15421h.size(); i14++) {
                i13 += f.p(this.f15421h.get(i14).intValue());
            }
            int i15 = i11 + i13;
            if (!s().isEmpty()) {
                i15 = i15 + 1 + f.p(i13);
            }
            this.f15422i = i13;
            int size = i15 + this.f15419f.size();
            this.f15424k = size;
            return size;
        }

        @Override // qc.r
        public final boolean isInitialized() {
            byte b10 = this.f15423j;
            if (b10 == 1) {
                return true;
            }
            if (b10 == 0) {
                return false;
            }
            this.f15423j = (byte) 1;
            return true;
        }

        public List<Integer> s() {
            return this.f15421h;
        }

        public List<c> t() {
            return this.f15420g;
        }

        @Override // qc.q
        /* renamed from: x, reason: merged with bridge method [inline-methods] */
        public b newBuilderForType() {
            return v();
        }

        @Override // qc.q
        /* renamed from: z, reason: merged with bridge method [inline-methods] */
        public b toBuilder() {
            return w(this);
        }

        /* compiled from: JvmProtoBuf.java */
        /* renamed from: mc.a$e$c */
        /* loaded from: classes2.dex */
        public static final class c extends i implements r {

            /* renamed from: r, reason: collision with root package name */
            private static final c f15428r;

            /* renamed from: s, reason: collision with root package name */
            public static qc.s<c> f15429s = new a();

            /* renamed from: f, reason: collision with root package name */
            private final qc.d f15430f;

            /* renamed from: g, reason: collision with root package name */
            private int f15431g;

            /* renamed from: h, reason: collision with root package name */
            private int f15432h;

            /* renamed from: i, reason: collision with root package name */
            private int f15433i;

            /* renamed from: j, reason: collision with root package name */
            private Object f15434j;

            /* renamed from: k, reason: collision with root package name */
            private EnumC0077c f15435k;

            /* renamed from: l, reason: collision with root package name */
            private List<Integer> f15436l;

            /* renamed from: m, reason: collision with root package name */
            private int f15437m;

            /* renamed from: n, reason: collision with root package name */
            private List<Integer> f15438n;

            /* renamed from: o, reason: collision with root package name */
            private int f15439o;

            /* renamed from: p, reason: collision with root package name */
            private byte f15440p;

            /* renamed from: q, reason: collision with root package name */
            private int f15441q;

            /* compiled from: JvmProtoBuf.java */
            /* renamed from: mc.a$e$c$a */
            /* loaded from: classes2.dex */
            static class a extends qc.b<c> {
                a() {
                }

                @Override // qc.s
                /* renamed from: m, reason: merged with bridge method [inline-methods] */
                public c a(qc.e eVar, g gVar) {
                    return new c(eVar, gVar);
                }
            }

            /* compiled from: JvmProtoBuf.java */
            /* renamed from: mc.a$e$c$b */
            /* loaded from: classes2.dex */
            public static final class b extends i.b<c, b> implements r {

                /* renamed from: f, reason: collision with root package name */
                private int f15442f;

                /* renamed from: h, reason: collision with root package name */
                private int f15444h;

                /* renamed from: g, reason: collision with root package name */
                private int f15443g = 1;

                /* renamed from: i, reason: collision with root package name */
                private Object f15445i = "";

                /* renamed from: j, reason: collision with root package name */
                private EnumC0077c f15446j = EnumC0077c.NONE;

                /* renamed from: k, reason: collision with root package name */
                private List<Integer> f15447k = Collections.emptyList();

                /* renamed from: l, reason: collision with root package name */
                private List<Integer> f15448l = Collections.emptyList();

                private b() {
                    p();
                }

                static /* synthetic */ b h() {
                    return l();
                }

                private static b l() {
                    return new b();
                }

                private void n() {
                    if ((this.f15442f & 32) != 32) {
                        this.f15448l = new ArrayList(this.f15448l);
                        this.f15442f |= 32;
                    }
                }

                private void o() {
                    if ((this.f15442f & 16) != 16) {
                        this.f15447k = new ArrayList(this.f15447k);
                        this.f15442f |= 16;
                    }
                }

                private void p() {
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
                    int i10 = this.f15442f;
                    int i11 = (i10 & 1) != 1 ? 0 : 1;
                    cVar.f15432h = this.f15443g;
                    if ((i10 & 2) == 2) {
                        i11 |= 2;
                    }
                    cVar.f15433i = this.f15444h;
                    if ((i10 & 4) == 4) {
                        i11 |= 4;
                    }
                    cVar.f15434j = this.f15445i;
                    if ((i10 & 8) == 8) {
                        i11 |= 8;
                    }
                    cVar.f15435k = this.f15446j;
                    if ((this.f15442f & 16) == 16) {
                        this.f15447k = Collections.unmodifiableList(this.f15447k);
                        this.f15442f &= -17;
                    }
                    cVar.f15436l = this.f15447k;
                    if ((this.f15442f & 32) == 32) {
                        this.f15448l = Collections.unmodifiableList(this.f15448l);
                        this.f15442f &= -33;
                    }
                    cVar.f15438n = this.f15448l;
                    cVar.f15431g = i11;
                    return cVar;
                }

                @Override // qc.i.b
                /* renamed from: k, reason: merged with bridge method [inline-methods] */
                public b d() {
                    return l().f(j());
                }

                @Override // qc.i.b
                /* renamed from: q, reason: merged with bridge method [inline-methods] */
                public b f(c cVar) {
                    if (cVar == c.x()) {
                        return this;
                    }
                    if (cVar.J()) {
                        u(cVar.A());
                    }
                    if (cVar.I()) {
                        t(cVar.z());
                    }
                    if (cVar.K()) {
                        this.f15442f |= 4;
                        this.f15445i = cVar.f15434j;
                    }
                    if (cVar.H()) {
                        s(cVar.y());
                    }
                    if (!cVar.f15436l.isEmpty()) {
                        if (this.f15447k.isEmpty()) {
                            this.f15447k = cVar.f15436l;
                            this.f15442f &= -17;
                        } else {
                            o();
                            this.f15447k.addAll(cVar.f15436l);
                        }
                    }
                    if (!cVar.f15438n.isEmpty()) {
                        if (this.f15448l.isEmpty()) {
                            this.f15448l = cVar.f15438n;
                            this.f15442f &= -33;
                        } else {
                            n();
                            this.f15448l.addAll(cVar.f15438n);
                        }
                    }
                    g(e().d(cVar.f15430f));
                    return this;
                }

                /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
                @Override // qc.a.AbstractC0092a, qc.q.a
                /* renamed from: r, reason: merged with bridge method [inline-methods] */
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public b m(qc.e eVar, g gVar) {
                    c cVar = null;
                    try {
                        try {
                            c a10 = c.f15429s.a(eVar, gVar);
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
                    } catch (k e10) {
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

                public b s(EnumC0077c enumC0077c) {
                    Objects.requireNonNull(enumC0077c);
                    this.f15442f |= 8;
                    this.f15446j = enumC0077c;
                    return this;
                }

                public b t(int i10) {
                    this.f15442f |= 2;
                    this.f15444h = i10;
                    return this;
                }

                public b u(int i10) {
                    this.f15442f |= 1;
                    this.f15443g = i10;
                    return this;
                }
            }

            /* compiled from: JvmProtoBuf.java */
            /* renamed from: mc.a$e$c$c, reason: collision with other inner class name */
            /* loaded from: classes2.dex */
            public enum EnumC0077c implements j.a {
                NONE(0, 0),
                INTERNAL_TO_CLASS_ID(1, 1),
                DESC_TO_CLASS_ID(2, 2);


                /* renamed from: i, reason: collision with root package name */
                private static j.b<EnumC0077c> f15452i = new a();

                /* renamed from: e, reason: collision with root package name */
                private final int f15454e;

                /* compiled from: JvmProtoBuf.java */
                /* renamed from: mc.a$e$c$c$a */
                /* loaded from: classes2.dex */
                static class a implements j.b<EnumC0077c> {
                    a() {
                    }

                    @Override // qc.j.b
                    /* renamed from: a, reason: merged with bridge method [inline-methods] */
                    public EnumC0077c findValueByNumber(int i10) {
                        return EnumC0077c.a(i10);
                    }
                }

                EnumC0077c(int i10, int i11) {
                    this.f15454e = i11;
                }

                public static EnumC0077c a(int i10) {
                    if (i10 == 0) {
                        return NONE;
                    }
                    if (i10 == 1) {
                        return INTERNAL_TO_CLASS_ID;
                    }
                    if (i10 != 2) {
                        return null;
                    }
                    return DESC_TO_CLASS_ID;
                }

                @Override // qc.j.a
                public final int getNumber() {
                    return this.f15454e;
                }
            }

            static {
                c cVar = new c(true);
                f15428r = cVar;
                cVar.L();
            }

            private void L() {
                this.f15432h = 1;
                this.f15433i = 0;
                this.f15434j = "";
                this.f15435k = EnumC0077c.NONE;
                this.f15436l = Collections.emptyList();
                this.f15438n = Collections.emptyList();
            }

            public static b M() {
                return b.h();
            }

            public static b N(c cVar) {
                return M().f(cVar);
            }

            public static c x() {
                return f15428r;
            }

            public int A() {
                return this.f15432h;
            }

            public int B() {
                return this.f15438n.size();
            }

            public List<Integer> C() {
                return this.f15438n;
            }

            public String D() {
                Object obj = this.f15434j;
                if (obj instanceof String) {
                    return (String) obj;
                }
                qc.d dVar = (qc.d) obj;
                String w10 = dVar.w();
                if (dVar.n()) {
                    this.f15434j = w10;
                }
                return w10;
            }

            public qc.d E() {
                Object obj = this.f15434j;
                if (obj instanceof String) {
                    qc.d h10 = qc.d.h((String) obj);
                    this.f15434j = h10;
                    return h10;
                }
                return (qc.d) obj;
            }

            public int F() {
                return this.f15436l.size();
            }

            public List<Integer> G() {
                return this.f15436l;
            }

            public boolean H() {
                return (this.f15431g & 8) == 8;
            }

            public boolean I() {
                return (this.f15431g & 2) == 2;
            }

            public boolean J() {
                return (this.f15431g & 1) == 1;
            }

            public boolean K() {
                return (this.f15431g & 4) == 4;
            }

            @Override // qc.q
            /* renamed from: O, reason: merged with bridge method [inline-methods] */
            public b newBuilderForType() {
                return M();
            }

            @Override // qc.q
            /* renamed from: P, reason: merged with bridge method [inline-methods] */
            public b toBuilder() {
                return N(this);
            }

            @Override // qc.q
            public void a(f fVar) {
                getSerializedSize();
                if ((this.f15431g & 1) == 1) {
                    fVar.a0(1, this.f15432h);
                }
                if ((this.f15431g & 2) == 2) {
                    fVar.a0(2, this.f15433i);
                }
                if ((this.f15431g & 8) == 8) {
                    fVar.S(3, this.f15435k.getNumber());
                }
                if (G().size() > 0) {
                    fVar.o0(34);
                    fVar.o0(this.f15437m);
                }
                for (int i10 = 0; i10 < this.f15436l.size(); i10++) {
                    fVar.b0(this.f15436l.get(i10).intValue());
                }
                if (C().size() > 0) {
                    fVar.o0(42);
                    fVar.o0(this.f15439o);
                }
                for (int i11 = 0; i11 < this.f15438n.size(); i11++) {
                    fVar.b0(this.f15438n.get(i11).intValue());
                }
                if ((this.f15431g & 4) == 4) {
                    fVar.O(6, E());
                }
                fVar.i0(this.f15430f);
            }

            @Override // qc.i, qc.q
            public qc.s<c> getParserForType() {
                return f15429s;
            }

            @Override // qc.q
            public int getSerializedSize() {
                int i10 = this.f15441q;
                if (i10 != -1) {
                    return i10;
                }
                int o10 = (this.f15431g & 1) == 1 ? f.o(1, this.f15432h) + 0 : 0;
                if ((this.f15431g & 2) == 2) {
                    o10 += f.o(2, this.f15433i);
                }
                if ((this.f15431g & 8) == 8) {
                    o10 += f.h(3, this.f15435k.getNumber());
                }
                int i11 = 0;
                for (int i12 = 0; i12 < this.f15436l.size(); i12++) {
                    i11 += f.p(this.f15436l.get(i12).intValue());
                }
                int i13 = o10 + i11;
                if (!G().isEmpty()) {
                    i13 = i13 + 1 + f.p(i11);
                }
                this.f15437m = i11;
                int i14 = 0;
                for (int i15 = 0; i15 < this.f15438n.size(); i15++) {
                    i14 += f.p(this.f15438n.get(i15).intValue());
                }
                int i16 = i13 + i14;
                if (!C().isEmpty()) {
                    i16 = i16 + 1 + f.p(i14);
                }
                this.f15439o = i14;
                if ((this.f15431g & 4) == 4) {
                    i16 += f.d(6, E());
                }
                int size = i16 + this.f15430f.size();
                this.f15441q = size;
                return size;
            }

            @Override // qc.r
            public final boolean isInitialized() {
                byte b10 = this.f15440p;
                if (b10 == 1) {
                    return true;
                }
                if (b10 == 0) {
                    return false;
                }
                this.f15440p = (byte) 1;
                return true;
            }

            public EnumC0077c y() {
                return this.f15435k;
            }

            public int z() {
                return this.f15433i;
            }

            private c(i.b bVar) {
                super(bVar);
                this.f15437m = -1;
                this.f15439o = -1;
                this.f15440p = (byte) -1;
                this.f15441q = -1;
                this.f15430f = bVar.e();
            }

            private c(boolean z10) {
                this.f15437m = -1;
                this.f15439o = -1;
                this.f15440p = (byte) -1;
                this.f15441q = -1;
                this.f15430f = qc.d.f17259e;
            }

            private c(qc.e eVar, g gVar) {
                this.f15437m = -1;
                this.f15439o = -1;
                this.f15440p = (byte) -1;
                this.f15441q = -1;
                L();
                d.b q10 = qc.d.q();
                f J = f.J(q10, 1);
                boolean z10 = false;
                int i10 = 0;
                while (!z10) {
                    try {
                        try {
                            int K = eVar.K();
                            if (K != 0) {
                                if (K == 8) {
                                    this.f15431g |= 1;
                                    this.f15432h = eVar.s();
                                } else if (K == 16) {
                                    this.f15431g |= 2;
                                    this.f15433i = eVar.s();
                                } else if (K == 24) {
                                    int n10 = eVar.n();
                                    EnumC0077c a10 = EnumC0077c.a(n10);
                                    if (a10 == null) {
                                        J.o0(K);
                                        J.o0(n10);
                                    } else {
                                        this.f15431g |= 8;
                                        this.f15435k = a10;
                                    }
                                } else if (K == 32) {
                                    if ((i10 & 16) != 16) {
                                        this.f15436l = new ArrayList();
                                        i10 |= 16;
                                    }
                                    this.f15436l.add(Integer.valueOf(eVar.s()));
                                } else if (K == 34) {
                                    int j10 = eVar.j(eVar.A());
                                    if ((i10 & 16) != 16 && eVar.e() > 0) {
                                        this.f15436l = new ArrayList();
                                        i10 |= 16;
                                    }
                                    while (eVar.e() > 0) {
                                        this.f15436l.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j10);
                                } else if (K == 40) {
                                    if ((i10 & 32) != 32) {
                                        this.f15438n = new ArrayList();
                                        i10 |= 32;
                                    }
                                    this.f15438n.add(Integer.valueOf(eVar.s()));
                                } else if (K == 42) {
                                    int j11 = eVar.j(eVar.A());
                                    if ((i10 & 32) != 32 && eVar.e() > 0) {
                                        this.f15438n = new ArrayList();
                                        i10 |= 32;
                                    }
                                    while (eVar.e() > 0) {
                                        this.f15438n.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j11);
                                } else if (K != 50) {
                                    if (!j(eVar, J, gVar, K)) {
                                    }
                                } else {
                                    qc.d l10 = eVar.l();
                                    this.f15431g |= 4;
                                    this.f15434j = l10;
                                }
                            }
                            z10 = true;
                        } catch (k e10) {
                            throw e10.i(this);
                        } catch (IOException e11) {
                            throw new k(e11.getMessage()).i(this);
                        }
                    } catch (Throwable th) {
                        if ((i10 & 16) == 16) {
                            this.f15436l = Collections.unmodifiableList(this.f15436l);
                        }
                        if ((i10 & 32) == 32) {
                            this.f15438n = Collections.unmodifiableList(this.f15438n);
                        }
                        try {
                            J.I();
                        } catch (IOException unused) {
                        } catch (Throwable th2) {
                            this.f15430f = q10.v();
                            throw th2;
                        }
                        this.f15430f = q10.v();
                        g();
                        throw th;
                    }
                }
                if ((i10 & 16) == 16) {
                    this.f15436l = Collections.unmodifiableList(this.f15436l);
                }
                if ((i10 & 32) == 32) {
                    this.f15438n = Collections.unmodifiableList(this.f15438n);
                }
                try {
                    J.I();
                } catch (IOException unused2) {
                } catch (Throwable th3) {
                    this.f15430f = q10.v();
                    throw th3;
                }
                this.f15430f = q10.v();
                g();
            }
        }

        private e(i.b bVar) {
            super(bVar);
            this.f15422i = -1;
            this.f15423j = (byte) -1;
            this.f15424k = -1;
            this.f15419f = bVar.e();
        }

        private e(boolean z10) {
            this.f15422i = -1;
            this.f15423j = (byte) -1;
            this.f15424k = -1;
            this.f15419f = qc.d.f17259e;
        }

        /* JADX WARN: Multi-variable type inference failed */
        private e(qc.e eVar, g gVar) {
            this.f15422i = -1;
            this.f15423j = (byte) -1;
            this.f15424k = -1;
            u();
            d.b q10 = qc.d.q();
            f J = f.J(q10, 1);
            boolean z10 = false;
            int i10 = 0;
            while (!z10) {
                try {
                    try {
                        int K = eVar.K();
                        if (K != 0) {
                            if (K == 10) {
                                if ((i10 & 1) != 1) {
                                    this.f15420g = new ArrayList();
                                    i10 |= 1;
                                }
                                this.f15420g.add(eVar.u(c.f15429s, gVar));
                            } else if (K == 40) {
                                if ((i10 & 2) != 2) {
                                    this.f15421h = new ArrayList();
                                    i10 |= 2;
                                }
                                this.f15421h.add(Integer.valueOf(eVar.s()));
                            } else if (K != 42) {
                                if (!j(eVar, J, gVar, K)) {
                                }
                            } else {
                                int j10 = eVar.j(eVar.A());
                                if ((i10 & 2) != 2 && eVar.e() > 0) {
                                    this.f15421h = new ArrayList();
                                    i10 |= 2;
                                }
                                while (eVar.e() > 0) {
                                    this.f15421h.add(Integer.valueOf(eVar.s()));
                                }
                                eVar.i(j10);
                            }
                        }
                        z10 = true;
                    } catch (k e10) {
                        throw e10.i(this);
                    } catch (IOException e11) {
                        throw new k(e11.getMessage()).i(this);
                    }
                } catch (Throwable th) {
                    if ((i10 & 1) == 1) {
                        this.f15420g = Collections.unmodifiableList(this.f15420g);
                    }
                    if ((i10 & 2) == 2) {
                        this.f15421h = Collections.unmodifiableList(this.f15421h);
                    }
                    try {
                        J.I();
                    } catch (IOException unused) {
                    } catch (Throwable th2) {
                        this.f15419f = q10.v();
                        throw th2;
                    }
                    this.f15419f = q10.v();
                    g();
                    throw th;
                }
            }
            if ((i10 & 1) == 1) {
                this.f15420g = Collections.unmodifiableList(this.f15420g);
            }
            if ((i10 & 2) == 2) {
                this.f15421h = Collections.unmodifiableList(this.f15421h);
            }
            try {
                J.I();
            } catch (IOException unused2) {
            } catch (Throwable th3) {
                this.f15419f = q10.v();
                throw th3;
            }
            this.f15419f = q10.v();
            g();
        }
    }

    static {
        jc.d C = jc.d.C();
        c q10 = c.q();
        c q11 = c.q();
        z.b bVar = z.b.f17389q;
        f15364a = i.i(C, q10, q11, null, 100, bVar, c.class);
        f15365b = i.i(jc.i.V(), c.q(), c.q(), null, 100, bVar, c.class);
        jc.i V = jc.i.V();
        z.b bVar2 = z.b.f17383k;
        f15366c = i.i(V, 0, null, null, 101, bVar2, Integer.class);
        f15367d = i.i(n.T(), d.t(), d.t(), null, 100, bVar, d.class);
        f15368e = i.i(n.T(), 0, null, null, 101, bVar2, Integer.class);
        f15369f = i.h(q.S(), jc.b.u(), null, 100, bVar, false, jc.b.class);
        f15370g = i.i(q.S(), Boolean.FALSE, null, null, 101, z.b.f17386n, Boolean.class);
        f15371h = i.h(s.F(), jc.b.u(), null, 100, bVar, false, jc.b.class);
        f15372i = i.i(jc.c.t0(), 0, null, null, 101, bVar2, Integer.class);
        f15373j = i.h(jc.c.t0(), n.T(), null, 102, bVar, false, n.class);
        f15374k = i.i(jc.c.t0(), 0, null, null, 103, bVar2, Integer.class);
        f15375l = i.i(jc.c.t0(), 0, null, null, 104, bVar2, Integer.class);
        f15376m = i.i(l.F(), 0, null, null, 101, bVar2, Integer.class);
        f15377n = i.h(l.F(), n.T(), null, 102, bVar, false, n.class);
    }

    public static void a(g gVar) {
        gVar.a(f15364a);
        gVar.a(f15365b);
        gVar.a(f15366c);
        gVar.a(f15367d);
        gVar.a(f15368e);
        gVar.a(f15369f);
        gVar.a(f15370g);
        gVar.a(f15371h);
        gVar.a(f15372i);
        gVar.a(f15373j);
        gVar.a(f15374k);
        gVar.a(f15375l);
        gVar.a(f15376m);
        gVar.a(f15377n);
    }

    /* compiled from: JvmProtoBuf.java */
    /* renamed from: mc.a$b */
    /* loaded from: classes2.dex */
    public static final class b extends i implements r {

        /* renamed from: l, reason: collision with root package name */
        private static final b f15378l;

        /* renamed from: m, reason: collision with root package name */
        public static qc.s<b> f15379m = new a();

        /* renamed from: f, reason: collision with root package name */
        private final qc.d f15380f;

        /* renamed from: g, reason: collision with root package name */
        private int f15381g;

        /* renamed from: h, reason: collision with root package name */
        private int f15382h;

        /* renamed from: i, reason: collision with root package name */
        private int f15383i;

        /* renamed from: j, reason: collision with root package name */
        private byte f15384j;

        /* renamed from: k, reason: collision with root package name */
        private int f15385k;

        /* compiled from: JvmProtoBuf.java */
        /* renamed from: mc.a$b$a */
        /* loaded from: classes2.dex */
        static class a extends qc.b<b> {
            a() {
            }

            @Override // qc.s
            /* renamed from: m, reason: merged with bridge method [inline-methods] */
            public b a(qc.e eVar, g gVar) {
                return new b(eVar, gVar);
            }
        }

        /* compiled from: JvmProtoBuf.java */
        /* renamed from: mc.a$b$b, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0076b extends i.b<b, C0076b> implements r {

            /* renamed from: f, reason: collision with root package name */
            private int f15386f;

            /* renamed from: g, reason: collision with root package name */
            private int f15387g;

            /* renamed from: h, reason: collision with root package name */
            private int f15388h;

            private C0076b() {
                n();
            }

            static /* synthetic */ C0076b h() {
                return l();
            }

            private static C0076b l() {
                return new C0076b();
            }

            private void n() {
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
                int i10 = this.f15386f;
                int i11 = (i10 & 1) != 1 ? 0 : 1;
                bVar.f15382h = this.f15387g;
                if ((i10 & 2) == 2) {
                    i11 |= 2;
                }
                bVar.f15383i = this.f15388h;
                bVar.f15381g = i11;
                return bVar;
            }

            @Override // qc.i.b
            /* renamed from: k, reason: merged with bridge method [inline-methods] */
            public C0076b d() {
                return l().f(j());
            }

            @Override // qc.i.b
            /* renamed from: o, reason: merged with bridge method [inline-methods] */
            public C0076b f(b bVar) {
                if (bVar == b.q()) {
                    return this;
                }
                if (bVar.u()) {
                    r(bVar.s());
                }
                if (bVar.t()) {
                    q(bVar.r());
                }
                g(e().d(bVar.f15380f));
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
            @Override // qc.a.AbstractC0092a, qc.q.a
            /* renamed from: p, reason: merged with bridge method [inline-methods] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public C0076b m(qc.e eVar, g gVar) {
                b bVar = null;
                try {
                    try {
                        b a10 = b.f15379m.a(eVar, gVar);
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
                } catch (k e10) {
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

            public C0076b q(int i10) {
                this.f15386f |= 2;
                this.f15388h = i10;
                return this;
            }

            public C0076b r(int i10) {
                this.f15386f |= 1;
                this.f15387g = i10;
                return this;
            }
        }

        static {
            b bVar = new b(true);
            f15378l = bVar;
            bVar.v();
        }

        public static b q() {
            return f15378l;
        }

        private void v() {
            this.f15382h = 0;
            this.f15383i = 0;
        }

        public static C0076b w() {
            return C0076b.h();
        }

        public static C0076b x(b bVar) {
            return w().f(bVar);
        }

        @Override // qc.q
        public void a(f fVar) {
            getSerializedSize();
            if ((this.f15381g & 1) == 1) {
                fVar.a0(1, this.f15382h);
            }
            if ((this.f15381g & 2) == 2) {
                fVar.a0(2, this.f15383i);
            }
            fVar.i0(this.f15380f);
        }

        @Override // qc.i, qc.q
        public qc.s<b> getParserForType() {
            return f15379m;
        }

        @Override // qc.q
        public int getSerializedSize() {
            int i10 = this.f15385k;
            if (i10 != -1) {
                return i10;
            }
            int o10 = (this.f15381g & 1) == 1 ? 0 + f.o(1, this.f15382h) : 0;
            if ((this.f15381g & 2) == 2) {
                o10 += f.o(2, this.f15383i);
            }
            int size = o10 + this.f15380f.size();
            this.f15385k = size;
            return size;
        }

        @Override // qc.r
        public final boolean isInitialized() {
            byte b10 = this.f15384j;
            if (b10 == 1) {
                return true;
            }
            if (b10 == 0) {
                return false;
            }
            this.f15384j = (byte) 1;
            return true;
        }

        public int r() {
            return this.f15383i;
        }

        public int s() {
            return this.f15382h;
        }

        public boolean t() {
            return (this.f15381g & 2) == 2;
        }

        public boolean u() {
            return (this.f15381g & 1) == 1;
        }

        @Override // qc.q
        /* renamed from: y, reason: merged with bridge method [inline-methods] */
        public C0076b newBuilderForType() {
            return w();
        }

        @Override // qc.q
        /* renamed from: z, reason: merged with bridge method [inline-methods] */
        public C0076b toBuilder() {
            return x(this);
        }

        private b(i.b bVar) {
            super(bVar);
            this.f15384j = (byte) -1;
            this.f15385k = -1;
            this.f15380f = bVar.e();
        }

        private b(boolean z10) {
            this.f15384j = (byte) -1;
            this.f15385k = -1;
            this.f15380f = qc.d.f17259e;
        }

        private b(qc.e eVar, g gVar) {
            this.f15384j = (byte) -1;
            this.f15385k = -1;
            v();
            d.b q10 = qc.d.q();
            f J = f.J(q10, 1);
            boolean z10 = false;
            while (!z10) {
                try {
                    try {
                        int K = eVar.K();
                        if (K != 0) {
                            if (K == 8) {
                                this.f15381g |= 1;
                                this.f15382h = eVar.s();
                            } else if (K != 16) {
                                if (!j(eVar, J, gVar, K)) {
                                }
                            } else {
                                this.f15381g |= 2;
                                this.f15383i = eVar.s();
                            }
                        }
                        z10 = true;
                    } catch (k e10) {
                        throw e10.i(this);
                    } catch (IOException e11) {
                        throw new k(e11.getMessage()).i(this);
                    }
                } catch (Throwable th) {
                    try {
                        J.I();
                    } catch (IOException unused) {
                    } catch (Throwable th2) {
                        this.f15380f = q10.v();
                        throw th2;
                    }
                    this.f15380f = q10.v();
                    g();
                    throw th;
                }
            }
            try {
                J.I();
            } catch (IOException unused2) {
            } catch (Throwable th3) {
                this.f15380f = q10.v();
                throw th3;
            }
            this.f15380f = q10.v();
            g();
        }
    }

    /* compiled from: JvmProtoBuf.java */
    /* renamed from: mc.a$c */
    /* loaded from: classes2.dex */
    public static final class c extends i implements r {

        /* renamed from: l, reason: collision with root package name */
        private static final c f15389l;

        /* renamed from: m, reason: collision with root package name */
        public static qc.s<c> f15390m = new a();

        /* renamed from: f, reason: collision with root package name */
        private final qc.d f15391f;

        /* renamed from: g, reason: collision with root package name */
        private int f15392g;

        /* renamed from: h, reason: collision with root package name */
        private int f15393h;

        /* renamed from: i, reason: collision with root package name */
        private int f15394i;

        /* renamed from: j, reason: collision with root package name */
        private byte f15395j;

        /* renamed from: k, reason: collision with root package name */
        private int f15396k;

        /* compiled from: JvmProtoBuf.java */
        /* renamed from: mc.a$c$a */
        /* loaded from: classes2.dex */
        static class a extends qc.b<c> {
            a() {
            }

            @Override // qc.s
            /* renamed from: m, reason: merged with bridge method [inline-methods] */
            public c a(qc.e eVar, g gVar) {
                return new c(eVar, gVar);
            }
        }

        /* compiled from: JvmProtoBuf.java */
        /* renamed from: mc.a$c$b */
        /* loaded from: classes2.dex */
        public static final class b extends i.b<c, b> implements r {

            /* renamed from: f, reason: collision with root package name */
            private int f15397f;

            /* renamed from: g, reason: collision with root package name */
            private int f15398g;

            /* renamed from: h, reason: collision with root package name */
            private int f15399h;

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
                int i10 = this.f15397f;
                int i11 = (i10 & 1) != 1 ? 0 : 1;
                cVar.f15393h = this.f15398g;
                if ((i10 & 2) == 2) {
                    i11 |= 2;
                }
                cVar.f15394i = this.f15399h;
                cVar.f15392g = i11;
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
                if (cVar == c.q()) {
                    return this;
                }
                if (cVar.u()) {
                    r(cVar.s());
                }
                if (cVar.t()) {
                    q(cVar.r());
                }
                g(e().d(cVar.f15391f));
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
            @Override // qc.a.AbstractC0092a, qc.q.a
            /* renamed from: p, reason: merged with bridge method [inline-methods] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public b m(qc.e eVar, g gVar) {
                c cVar = null;
                try {
                    try {
                        c a10 = c.f15390m.a(eVar, gVar);
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
                } catch (k e10) {
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

            public b q(int i10) {
                this.f15397f |= 2;
                this.f15399h = i10;
                return this;
            }

            public b r(int i10) {
                this.f15397f |= 1;
                this.f15398g = i10;
                return this;
            }
        }

        static {
            c cVar = new c(true);
            f15389l = cVar;
            cVar.v();
        }

        public static c q() {
            return f15389l;
        }

        private void v() {
            this.f15393h = 0;
            this.f15394i = 0;
        }

        public static b w() {
            return b.h();
        }

        public static b x(c cVar) {
            return w().f(cVar);
        }

        @Override // qc.q
        public void a(f fVar) {
            getSerializedSize();
            if ((this.f15392g & 1) == 1) {
                fVar.a0(1, this.f15393h);
            }
            if ((this.f15392g & 2) == 2) {
                fVar.a0(2, this.f15394i);
            }
            fVar.i0(this.f15391f);
        }

        @Override // qc.i, qc.q
        public qc.s<c> getParserForType() {
            return f15390m;
        }

        @Override // qc.q
        public int getSerializedSize() {
            int i10 = this.f15396k;
            if (i10 != -1) {
                return i10;
            }
            int o10 = (this.f15392g & 1) == 1 ? 0 + f.o(1, this.f15393h) : 0;
            if ((this.f15392g & 2) == 2) {
                o10 += f.o(2, this.f15394i);
            }
            int size = o10 + this.f15391f.size();
            this.f15396k = size;
            return size;
        }

        @Override // qc.r
        public final boolean isInitialized() {
            byte b10 = this.f15395j;
            if (b10 == 1) {
                return true;
            }
            if (b10 == 0) {
                return false;
            }
            this.f15395j = (byte) 1;
            return true;
        }

        public int r() {
            return this.f15394i;
        }

        public int s() {
            return this.f15393h;
        }

        public boolean t() {
            return (this.f15392g & 2) == 2;
        }

        public boolean u() {
            return (this.f15392g & 1) == 1;
        }

        @Override // qc.q
        /* renamed from: y, reason: merged with bridge method [inline-methods] */
        public b newBuilderForType() {
            return w();
        }

        @Override // qc.q
        /* renamed from: z, reason: merged with bridge method [inline-methods] */
        public b toBuilder() {
            return x(this);
        }

        private c(i.b bVar) {
            super(bVar);
            this.f15395j = (byte) -1;
            this.f15396k = -1;
            this.f15391f = bVar.e();
        }

        private c(boolean z10) {
            this.f15395j = (byte) -1;
            this.f15396k = -1;
            this.f15391f = qc.d.f17259e;
        }

        private c(qc.e eVar, g gVar) {
            this.f15395j = (byte) -1;
            this.f15396k = -1;
            v();
            d.b q10 = qc.d.q();
            f J = f.J(q10, 1);
            boolean z10 = false;
            while (!z10) {
                try {
                    try {
                        int K = eVar.K();
                        if (K != 0) {
                            if (K == 8) {
                                this.f15392g |= 1;
                                this.f15393h = eVar.s();
                            } else if (K != 16) {
                                if (!j(eVar, J, gVar, K)) {
                                }
                            } else {
                                this.f15392g |= 2;
                                this.f15394i = eVar.s();
                            }
                        }
                        z10 = true;
                    } catch (k e10) {
                        throw e10.i(this);
                    } catch (IOException e11) {
                        throw new k(e11.getMessage()).i(this);
                    }
                } catch (Throwable th) {
                    try {
                        J.I();
                    } catch (IOException unused) {
                    } catch (Throwable th2) {
                        this.f15391f = q10.v();
                        throw th2;
                    }
                    this.f15391f = q10.v();
                    g();
                    throw th;
                }
            }
            try {
                J.I();
            } catch (IOException unused2) {
            } catch (Throwable th3) {
                this.f15391f = q10.v();
                throw th3;
            }
            this.f15391f = q10.v();
            g();
        }
    }

    /* compiled from: JvmProtoBuf.java */
    /* renamed from: mc.a$d */
    /* loaded from: classes2.dex */
    public static final class d extends i implements r {

        /* renamed from: o, reason: collision with root package name */
        private static final d f15400o;

        /* renamed from: p, reason: collision with root package name */
        public static qc.s<d> f15401p = new a();

        /* renamed from: f, reason: collision with root package name */
        private final qc.d f15402f;

        /* renamed from: g, reason: collision with root package name */
        private int f15403g;

        /* renamed from: h, reason: collision with root package name */
        private b f15404h;

        /* renamed from: i, reason: collision with root package name */
        private c f15405i;

        /* renamed from: j, reason: collision with root package name */
        private c f15406j;

        /* renamed from: k, reason: collision with root package name */
        private c f15407k;

        /* renamed from: l, reason: collision with root package name */
        private c f15408l;

        /* renamed from: m, reason: collision with root package name */
        private byte f15409m;

        /* renamed from: n, reason: collision with root package name */
        private int f15410n;

        /* compiled from: JvmProtoBuf.java */
        /* renamed from: mc.a$d$a */
        /* loaded from: classes2.dex */
        static class a extends qc.b<d> {
            a() {
            }

            @Override // qc.s
            /* renamed from: m, reason: merged with bridge method [inline-methods] */
            public d a(qc.e eVar, g gVar) {
                return new d(eVar, gVar);
            }
        }

        /* compiled from: JvmProtoBuf.java */
        /* renamed from: mc.a$d$b */
        /* loaded from: classes2.dex */
        public static final class b extends i.b<d, b> implements r {

            /* renamed from: f, reason: collision with root package name */
            private int f15411f;

            /* renamed from: g, reason: collision with root package name */
            private b f15412g = b.q();

            /* renamed from: h, reason: collision with root package name */
            private c f15413h = c.q();

            /* renamed from: i, reason: collision with root package name */
            private c f15414i = c.q();

            /* renamed from: j, reason: collision with root package name */
            private c f15415j = c.q();

            /* renamed from: k, reason: collision with root package name */
            private c f15416k = c.q();

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
            public d build() {
                d j10 = j();
                if (j10.isInitialized()) {
                    return j10;
                }
                throw a.AbstractC0092a.c(j10);
            }

            public d j() {
                d dVar = new d(this);
                int i10 = this.f15411f;
                int i11 = (i10 & 1) != 1 ? 0 : 1;
                dVar.f15404h = this.f15412g;
                if ((i10 & 2) == 2) {
                    i11 |= 2;
                }
                dVar.f15405i = this.f15413h;
                if ((i10 & 4) == 4) {
                    i11 |= 4;
                }
                dVar.f15406j = this.f15414i;
                if ((i10 & 8) == 8) {
                    i11 |= 8;
                }
                dVar.f15407k = this.f15415j;
                if ((i10 & 16) == 16) {
                    i11 |= 16;
                }
                dVar.f15408l = this.f15416k;
                dVar.f15403g = i11;
                return dVar;
            }

            @Override // qc.i.b
            /* renamed from: k, reason: merged with bridge method [inline-methods] */
            public b d() {
                return l().f(j());
            }

            public b o(c cVar) {
                if ((this.f15411f & 16) == 16 && this.f15416k != c.q()) {
                    this.f15416k = c.x(this.f15416k).f(cVar).j();
                } else {
                    this.f15416k = cVar;
                }
                this.f15411f |= 16;
                return this;
            }

            public b p(b bVar) {
                if ((this.f15411f & 1) == 1 && this.f15412g != b.q()) {
                    this.f15412g = b.x(this.f15412g).f(bVar).j();
                } else {
                    this.f15412g = bVar;
                }
                this.f15411f |= 1;
                return this;
            }

            @Override // qc.i.b
            /* renamed from: q, reason: merged with bridge method [inline-methods] */
            public b f(d dVar) {
                if (dVar == d.t()) {
                    return this;
                }
                if (dVar.A()) {
                    p(dVar.v());
                }
                if (dVar.D()) {
                    u(dVar.y());
                }
                if (dVar.B()) {
                    s(dVar.w());
                }
                if (dVar.C()) {
                    t(dVar.x());
                }
                if (dVar.z()) {
                    o(dVar.u());
                }
                g(e().d(dVar.f15402f));
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
            @Override // qc.a.AbstractC0092a, qc.q.a
            /* renamed from: r, reason: merged with bridge method [inline-methods] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public b m(qc.e eVar, g gVar) {
                d dVar = null;
                try {
                    try {
                        d a10 = d.f15401p.a(eVar, gVar);
                        if (a10 != null) {
                            f(a10);
                        }
                        return this;
                    } catch (Throwable th) {
                        th = th;
                        if (dVar != null) {
                        }
                        throw th;
                    }
                } catch (k e10) {
                    d dVar2 = (d) e10.a();
                    try {
                        throw e10;
                    } catch (Throwable th2) {
                        th = th2;
                        dVar = dVar2;
                        if (dVar != null) {
                            f(dVar);
                        }
                        throw th;
                    }
                }
            }

            public b s(c cVar) {
                if ((this.f15411f & 4) == 4 && this.f15414i != c.q()) {
                    this.f15414i = c.x(this.f15414i).f(cVar).j();
                } else {
                    this.f15414i = cVar;
                }
                this.f15411f |= 4;
                return this;
            }

            public b t(c cVar) {
                if ((this.f15411f & 8) == 8 && this.f15415j != c.q()) {
                    this.f15415j = c.x(this.f15415j).f(cVar).j();
                } else {
                    this.f15415j = cVar;
                }
                this.f15411f |= 8;
                return this;
            }

            public b u(c cVar) {
                if ((this.f15411f & 2) == 2 && this.f15413h != c.q()) {
                    this.f15413h = c.x(this.f15413h).f(cVar).j();
                } else {
                    this.f15413h = cVar;
                }
                this.f15411f |= 2;
                return this;
            }
        }

        static {
            d dVar = new d(true);
            f15400o = dVar;
            dVar.E();
        }

        private void E() {
            this.f15404h = b.q();
            this.f15405i = c.q();
            this.f15406j = c.q();
            this.f15407k = c.q();
            this.f15408l = c.q();
        }

        public static b F() {
            return b.h();
        }

        public static b G(d dVar) {
            return F().f(dVar);
        }

        public static d t() {
            return f15400o;
        }

        public boolean A() {
            return (this.f15403g & 1) == 1;
        }

        public boolean B() {
            return (this.f15403g & 4) == 4;
        }

        public boolean C() {
            return (this.f15403g & 8) == 8;
        }

        public boolean D() {
            return (this.f15403g & 2) == 2;
        }

        @Override // qc.q
        /* renamed from: H, reason: merged with bridge method [inline-methods] */
        public b newBuilderForType() {
            return F();
        }

        @Override // qc.q
        /* renamed from: I, reason: merged with bridge method [inline-methods] */
        public b toBuilder() {
            return G(this);
        }

        @Override // qc.q
        public void a(f fVar) {
            getSerializedSize();
            if ((this.f15403g & 1) == 1) {
                fVar.d0(1, this.f15404h);
            }
            if ((this.f15403g & 2) == 2) {
                fVar.d0(2, this.f15405i);
            }
            if ((this.f15403g & 4) == 4) {
                fVar.d0(3, this.f15406j);
            }
            if ((this.f15403g & 8) == 8) {
                fVar.d0(4, this.f15407k);
            }
            if ((this.f15403g & 16) == 16) {
                fVar.d0(5, this.f15408l);
            }
            fVar.i0(this.f15402f);
        }

        @Override // qc.i, qc.q
        public qc.s<d> getParserForType() {
            return f15401p;
        }

        @Override // qc.q
        public int getSerializedSize() {
            int i10 = this.f15410n;
            if (i10 != -1) {
                return i10;
            }
            int s7 = (this.f15403g & 1) == 1 ? 0 + f.s(1, this.f15404h) : 0;
            if ((this.f15403g & 2) == 2) {
                s7 += f.s(2, this.f15405i);
            }
            if ((this.f15403g & 4) == 4) {
                s7 += f.s(3, this.f15406j);
            }
            if ((this.f15403g & 8) == 8) {
                s7 += f.s(4, this.f15407k);
            }
            if ((this.f15403g & 16) == 16) {
                s7 += f.s(5, this.f15408l);
            }
            int size = s7 + this.f15402f.size();
            this.f15410n = size;
            return size;
        }

        @Override // qc.r
        public final boolean isInitialized() {
            byte b10 = this.f15409m;
            if (b10 == 1) {
                return true;
            }
            if (b10 == 0) {
                return false;
            }
            this.f15409m = (byte) 1;
            return true;
        }

        public c u() {
            return this.f15408l;
        }

        public b v() {
            return this.f15404h;
        }

        public c w() {
            return this.f15406j;
        }

        public c x() {
            return this.f15407k;
        }

        public c y() {
            return this.f15405i;
        }

        public boolean z() {
            return (this.f15403g & 16) == 16;
        }

        private d(i.b bVar) {
            super(bVar);
            this.f15409m = (byte) -1;
            this.f15410n = -1;
            this.f15402f = bVar.e();
        }

        private d(boolean z10) {
            this.f15409m = (byte) -1;
            this.f15410n = -1;
            this.f15402f = qc.d.f17259e;
        }

        private d(qc.e eVar, g gVar) {
            this.f15409m = (byte) -1;
            this.f15410n = -1;
            E();
            d.b q10 = qc.d.q();
            f J = f.J(q10, 1);
            boolean z10 = false;
            while (!z10) {
                try {
                    try {
                        try {
                            int K = eVar.K();
                            if (K != 0) {
                                if (K == 10) {
                                    b.C0076b builder = (this.f15403g & 1) == 1 ? this.f15404h.toBuilder() : null;
                                    b bVar = (b) eVar.u(b.f15379m, gVar);
                                    this.f15404h = bVar;
                                    if (builder != null) {
                                        builder.f(bVar);
                                        this.f15404h = builder.j();
                                    }
                                    this.f15403g |= 1;
                                } else if (K == 18) {
                                    c.b builder2 = (this.f15403g & 2) == 2 ? this.f15405i.toBuilder() : null;
                                    c cVar = (c) eVar.u(c.f15390m, gVar);
                                    this.f15405i = cVar;
                                    if (builder2 != null) {
                                        builder2.f(cVar);
                                        this.f15405i = builder2.j();
                                    }
                                    this.f15403g |= 2;
                                } else if (K == 26) {
                                    c.b builder3 = (this.f15403g & 4) == 4 ? this.f15406j.toBuilder() : null;
                                    c cVar2 = (c) eVar.u(c.f15390m, gVar);
                                    this.f15406j = cVar2;
                                    if (builder3 != null) {
                                        builder3.f(cVar2);
                                        this.f15406j = builder3.j();
                                    }
                                    this.f15403g |= 4;
                                } else if (K == 34) {
                                    c.b builder4 = (this.f15403g & 8) == 8 ? this.f15407k.toBuilder() : null;
                                    c cVar3 = (c) eVar.u(c.f15390m, gVar);
                                    this.f15407k = cVar3;
                                    if (builder4 != null) {
                                        builder4.f(cVar3);
                                        this.f15407k = builder4.j();
                                    }
                                    this.f15403g |= 8;
                                } else if (K != 42) {
                                    if (!j(eVar, J, gVar, K)) {
                                    }
                                } else {
                                    c.b builder5 = (this.f15403g & 16) == 16 ? this.f15408l.toBuilder() : null;
                                    c cVar4 = (c) eVar.u(c.f15390m, gVar);
                                    this.f15408l = cVar4;
                                    if (builder5 != null) {
                                        builder5.f(cVar4);
                                        this.f15408l = builder5.j();
                                    }
                                    this.f15403g |= 16;
                                }
                            }
                            z10 = true;
                        } catch (k e10) {
                            throw e10.i(this);
                        }
                    } catch (IOException e11) {
                        throw new k(e11.getMessage()).i(this);
                    }
                } catch (Throwable th) {
                    try {
                        J.I();
                    } catch (IOException unused) {
                    } catch (Throwable th2) {
                        this.f15402f = q10.v();
                        throw th2;
                    }
                    this.f15402f = q10.v();
                    g();
                    throw th;
                }
            }
            try {
                J.I();
            } catch (IOException unused2) {
            } catch (Throwable th3) {
                this.f15402f = q10.v();
                throw th3;
            }
            this.f15402f = q10.v();
            g();
        }
    }
}
