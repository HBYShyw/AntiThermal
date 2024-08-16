package jc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jc.e;
import jc.q;
import jc.t;
import qc.a;
import qc.d;
import qc.i;

/* compiled from: ProtoBuf.java */
/* loaded from: classes2.dex */
public final class i extends i.d<i> {
    public static qc.s<i> A = new a();

    /* renamed from: z, reason: collision with root package name */
    private static final i f13570z;

    /* renamed from: g, reason: collision with root package name */
    private final qc.d f13571g;

    /* renamed from: h, reason: collision with root package name */
    private int f13572h;

    /* renamed from: i, reason: collision with root package name */
    private int f13573i;

    /* renamed from: j, reason: collision with root package name */
    private int f13574j;

    /* renamed from: k, reason: collision with root package name */
    private int f13575k;

    /* renamed from: l, reason: collision with root package name */
    private q f13576l;

    /* renamed from: m, reason: collision with root package name */
    private int f13577m;

    /* renamed from: n, reason: collision with root package name */
    private List<s> f13578n;

    /* renamed from: o, reason: collision with root package name */
    private q f13579o;

    /* renamed from: p, reason: collision with root package name */
    private int f13580p;

    /* renamed from: q, reason: collision with root package name */
    private List<q> f13581q;

    /* renamed from: r, reason: collision with root package name */
    private List<Integer> f13582r;

    /* renamed from: s, reason: collision with root package name */
    private int f13583s;

    /* renamed from: t, reason: collision with root package name */
    private List<u> f13584t;

    /* renamed from: u, reason: collision with root package name */
    private t f13585u;

    /* renamed from: v, reason: collision with root package name */
    private List<Integer> f13586v;

    /* renamed from: w, reason: collision with root package name */
    private e f13587w;

    /* renamed from: x, reason: collision with root package name */
    private byte f13588x;

    /* renamed from: y, reason: collision with root package name */
    private int f13589y;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<i> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public i a(qc.e eVar, qc.g gVar) {
            return new i(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.c<i, b> {

        /* renamed from: h, reason: collision with root package name */
        private int f13590h;

        /* renamed from: k, reason: collision with root package name */
        private int f13593k;

        /* renamed from: m, reason: collision with root package name */
        private int f13595m;

        /* renamed from: p, reason: collision with root package name */
        private int f13598p;

        /* renamed from: i, reason: collision with root package name */
        private int f13591i = 6;

        /* renamed from: j, reason: collision with root package name */
        private int f13592j = 6;

        /* renamed from: l, reason: collision with root package name */
        private q f13594l = q.S();

        /* renamed from: n, reason: collision with root package name */
        private List<s> f13596n = Collections.emptyList();

        /* renamed from: o, reason: collision with root package name */
        private q f13597o = q.S();

        /* renamed from: q, reason: collision with root package name */
        private List<q> f13599q = Collections.emptyList();

        /* renamed from: r, reason: collision with root package name */
        private List<Integer> f13600r = Collections.emptyList();

        /* renamed from: s, reason: collision with root package name */
        private List<u> f13601s = Collections.emptyList();

        /* renamed from: t, reason: collision with root package name */
        private t f13602t = t.r();

        /* renamed from: u, reason: collision with root package name */
        private List<Integer> f13603u = Collections.emptyList();

        /* renamed from: v, reason: collision with root package name */
        private e f13604v = e.p();

        private b() {
            w();
        }

        static /* synthetic */ b l() {
            return q();
        }

        private static b q() {
            return new b();
        }

        private void r() {
            if ((this.f13590h & 512) != 512) {
                this.f13600r = new ArrayList(this.f13600r);
                this.f13590h |= 512;
            }
        }

        private void s() {
            if ((this.f13590h & 256) != 256) {
                this.f13599q = new ArrayList(this.f13599q);
                this.f13590h |= 256;
            }
        }

        private void t() {
            if ((this.f13590h & 32) != 32) {
                this.f13596n = new ArrayList(this.f13596n);
                this.f13590h |= 32;
            }
        }

        private void u() {
            if ((this.f13590h & 1024) != 1024) {
                this.f13601s = new ArrayList(this.f13601s);
                this.f13590h |= 1024;
            }
        }

        private void v() {
            if ((this.f13590h & 4096) != 4096) {
                this.f13603u = new ArrayList(this.f13603u);
                this.f13590h |= 4096;
            }
        }

        private void w() {
        }

        public b A(q qVar) {
            if ((this.f13590h & 64) == 64 && this.f13597o != q.S()) {
                this.f13597o = q.t0(this.f13597o).f(qVar).o();
            } else {
                this.f13597o = qVar;
            }
            this.f13590h |= 64;
            return this;
        }

        public b B(q qVar) {
            if ((this.f13590h & 8) == 8 && this.f13594l != q.S()) {
                this.f13594l = q.t0(this.f13594l).f(qVar).o();
            } else {
                this.f13594l = qVar;
            }
            this.f13590h |= 8;
            return this;
        }

        public b C(t tVar) {
            if ((this.f13590h & 2048) == 2048 && this.f13602t != t.r()) {
                this.f13602t = t.z(this.f13602t).f(tVar).j();
            } else {
                this.f13602t = tVar;
            }
            this.f13590h |= 2048;
            return this;
        }

        public b D(int i10) {
            this.f13590h |= 1;
            this.f13591i = i10;
            return this;
        }

        public b E(int i10) {
            this.f13590h |= 4;
            this.f13593k = i10;
            return this;
        }

        public b F(int i10) {
            this.f13590h |= 2;
            this.f13592j = i10;
            return this;
        }

        public b G(int i10) {
            this.f13590h |= 128;
            this.f13598p = i10;
            return this;
        }

        public b H(int i10) {
            this.f13590h |= 16;
            this.f13595m = i10;
            return this;
        }

        @Override // qc.q.a
        /* renamed from: n, reason: merged with bridge method [inline-methods] */
        public i build() {
            i o10 = o();
            if (o10.isInitialized()) {
                return o10;
            }
            throw a.AbstractC0092a.c(o10);
        }

        public i o() {
            i iVar = new i(this);
            int i10 = this.f13590h;
            int i11 = (i10 & 1) != 1 ? 0 : 1;
            iVar.f13573i = this.f13591i;
            if ((i10 & 2) == 2) {
                i11 |= 2;
            }
            iVar.f13574j = this.f13592j;
            if ((i10 & 4) == 4) {
                i11 |= 4;
            }
            iVar.f13575k = this.f13593k;
            if ((i10 & 8) == 8) {
                i11 |= 8;
            }
            iVar.f13576l = this.f13594l;
            if ((i10 & 16) == 16) {
                i11 |= 16;
            }
            iVar.f13577m = this.f13595m;
            if ((this.f13590h & 32) == 32) {
                this.f13596n = Collections.unmodifiableList(this.f13596n);
                this.f13590h &= -33;
            }
            iVar.f13578n = this.f13596n;
            if ((i10 & 64) == 64) {
                i11 |= 32;
            }
            iVar.f13579o = this.f13597o;
            if ((i10 & 128) == 128) {
                i11 |= 64;
            }
            iVar.f13580p = this.f13598p;
            if ((this.f13590h & 256) == 256) {
                this.f13599q = Collections.unmodifiableList(this.f13599q);
                this.f13590h &= -257;
            }
            iVar.f13581q = this.f13599q;
            if ((this.f13590h & 512) == 512) {
                this.f13600r = Collections.unmodifiableList(this.f13600r);
                this.f13590h &= -513;
            }
            iVar.f13582r = this.f13600r;
            if ((this.f13590h & 1024) == 1024) {
                this.f13601s = Collections.unmodifiableList(this.f13601s);
                this.f13590h &= -1025;
            }
            iVar.f13584t = this.f13601s;
            if ((i10 & 2048) == 2048) {
                i11 |= 128;
            }
            iVar.f13585u = this.f13602t;
            if ((this.f13590h & 4096) == 4096) {
                this.f13603u = Collections.unmodifiableList(this.f13603u);
                this.f13590h &= -4097;
            }
            iVar.f13586v = this.f13603u;
            if ((i10 & 8192) == 8192) {
                i11 |= 256;
            }
            iVar.f13587w = this.f13604v;
            iVar.f13572h = i11;
            return iVar;
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b d() {
            return q().f(o());
        }

        public b x(e eVar) {
            if ((this.f13590h & 8192) == 8192 && this.f13604v != e.p()) {
                this.f13604v = e.u(this.f13604v).f(eVar).j();
            } else {
                this.f13604v = eVar;
            }
            this.f13590h |= 8192;
            return this;
        }

        @Override // qc.i.b
        /* renamed from: y, reason: merged with bridge method [inline-methods] */
        public b f(i iVar) {
            if (iVar == i.V()) {
                return this;
            }
            if (iVar.n0()) {
                D(iVar.X());
            }
            if (iVar.p0()) {
                F(iVar.Z());
            }
            if (iVar.o0()) {
                E(iVar.Y());
            }
            if (iVar.s0()) {
                B(iVar.c0());
            }
            if (iVar.t0()) {
                H(iVar.d0());
            }
            if (!iVar.f13578n.isEmpty()) {
                if (this.f13596n.isEmpty()) {
                    this.f13596n = iVar.f13578n;
                    this.f13590h &= -33;
                } else {
                    t();
                    this.f13596n.addAll(iVar.f13578n);
                }
            }
            if (iVar.q0()) {
                A(iVar.a0());
            }
            if (iVar.r0()) {
                G(iVar.b0());
            }
            if (!iVar.f13581q.isEmpty()) {
                if (this.f13599q.isEmpty()) {
                    this.f13599q = iVar.f13581q;
                    this.f13590h &= -257;
                } else {
                    s();
                    this.f13599q.addAll(iVar.f13581q);
                }
            }
            if (!iVar.f13582r.isEmpty()) {
                if (this.f13600r.isEmpty()) {
                    this.f13600r = iVar.f13582r;
                    this.f13590h &= -513;
                } else {
                    r();
                    this.f13600r.addAll(iVar.f13582r);
                }
            }
            if (!iVar.f13584t.isEmpty()) {
                if (this.f13601s.isEmpty()) {
                    this.f13601s = iVar.f13584t;
                    this.f13590h &= -1025;
                } else {
                    u();
                    this.f13601s.addAll(iVar.f13584t);
                }
            }
            if (iVar.u0()) {
                C(iVar.h0());
            }
            if (!iVar.f13586v.isEmpty()) {
                if (this.f13603u.isEmpty()) {
                    this.f13603u = iVar.f13586v;
                    this.f13590h &= -4097;
                } else {
                    v();
                    this.f13603u.addAll(iVar.f13586v);
                }
            }
            if (iVar.m0()) {
                x(iVar.U());
            }
            k(iVar);
            g(e().d(iVar.f13571g));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: z, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            i iVar = null;
            try {
                try {
                    i a10 = i.A.a(eVar, gVar);
                    if (a10 != null) {
                        f(a10);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (iVar != null) {
                    }
                    throw th;
                }
            } catch (qc.k e10) {
                i iVar2 = (i) e10.a();
                try {
                    throw e10;
                } catch (Throwable th2) {
                    th = th2;
                    iVar = iVar2;
                    if (iVar != null) {
                        f(iVar);
                    }
                    throw th;
                }
            }
        }
    }

    static {
        i iVar = new i(true);
        f13570z = iVar;
        iVar.v0();
    }

    public static i V() {
        return f13570z;
    }

    private void v0() {
        this.f13573i = 6;
        this.f13574j = 6;
        this.f13575k = 0;
        this.f13576l = q.S();
        this.f13577m = 0;
        this.f13578n = Collections.emptyList();
        this.f13579o = q.S();
        this.f13580p = 0;
        this.f13581q = Collections.emptyList();
        this.f13582r = Collections.emptyList();
        this.f13584t = Collections.emptyList();
        this.f13585u = t.r();
        this.f13586v = Collections.emptyList();
        this.f13587w = e.p();
    }

    public static b w0() {
        return b.l();
    }

    public static b x0(i iVar) {
        return w0().f(iVar);
    }

    public static i z0(InputStream inputStream, qc.g gVar) {
        return A.c(inputStream, gVar);
    }

    @Override // qc.q
    /* renamed from: A0, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return x0(this);
    }

    public q Q(int i10) {
        return this.f13581q.get(i10);
    }

    public int R() {
        return this.f13581q.size();
    }

    public List<Integer> S() {
        return this.f13582r;
    }

    public List<q> T() {
        return this.f13581q;
    }

    public e U() {
        return this.f13587w;
    }

    @Override // qc.r
    /* renamed from: W, reason: merged with bridge method [inline-methods] */
    public i getDefaultInstanceForType() {
        return f13570z;
    }

    public int X() {
        return this.f13573i;
    }

    public int Y() {
        return this.f13575k;
    }

    public int Z() {
        return this.f13574j;
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        i.d<MessageType>.a t7 = t();
        if ((this.f13572h & 2) == 2) {
            fVar.a0(1, this.f13574j);
        }
        if ((this.f13572h & 4) == 4) {
            fVar.a0(2, this.f13575k);
        }
        if ((this.f13572h & 8) == 8) {
            fVar.d0(3, this.f13576l);
        }
        for (int i10 = 0; i10 < this.f13578n.size(); i10++) {
            fVar.d0(4, this.f13578n.get(i10));
        }
        if ((this.f13572h & 32) == 32) {
            fVar.d0(5, this.f13579o);
        }
        for (int i11 = 0; i11 < this.f13584t.size(); i11++) {
            fVar.d0(6, this.f13584t.get(i11));
        }
        if ((this.f13572h & 16) == 16) {
            fVar.a0(7, this.f13577m);
        }
        if ((this.f13572h & 64) == 64) {
            fVar.a0(8, this.f13580p);
        }
        if ((this.f13572h & 1) == 1) {
            fVar.a0(9, this.f13573i);
        }
        for (int i12 = 0; i12 < this.f13581q.size(); i12++) {
            fVar.d0(10, this.f13581q.get(i12));
        }
        if (S().size() > 0) {
            fVar.o0(90);
            fVar.o0(this.f13583s);
        }
        for (int i13 = 0; i13 < this.f13582r.size(); i13++) {
            fVar.b0(this.f13582r.get(i13).intValue());
        }
        if ((this.f13572h & 128) == 128) {
            fVar.d0(30, this.f13585u);
        }
        for (int i14 = 0; i14 < this.f13586v.size(); i14++) {
            fVar.a0(31, this.f13586v.get(i14).intValue());
        }
        if ((this.f13572h & 256) == 256) {
            fVar.d0(32, this.f13587w);
        }
        t7.a(19000, fVar);
        fVar.i0(this.f13571g);
    }

    public q a0() {
        return this.f13579o;
    }

    public int b0() {
        return this.f13580p;
    }

    public q c0() {
        return this.f13576l;
    }

    public int d0() {
        return this.f13577m;
    }

    public s e0(int i10) {
        return this.f13578n.get(i10);
    }

    public int f0() {
        return this.f13578n.size();
    }

    public List<s> g0() {
        return this.f13578n;
    }

    @Override // qc.i, qc.q
    public qc.s<i> getParserForType() {
        return A;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13589y;
        if (i10 != -1) {
            return i10;
        }
        int o10 = (this.f13572h & 2) == 2 ? qc.f.o(1, this.f13574j) + 0 : 0;
        if ((this.f13572h & 4) == 4) {
            o10 += qc.f.o(2, this.f13575k);
        }
        if ((this.f13572h & 8) == 8) {
            o10 += qc.f.s(3, this.f13576l);
        }
        for (int i11 = 0; i11 < this.f13578n.size(); i11++) {
            o10 += qc.f.s(4, this.f13578n.get(i11));
        }
        if ((this.f13572h & 32) == 32) {
            o10 += qc.f.s(5, this.f13579o);
        }
        for (int i12 = 0; i12 < this.f13584t.size(); i12++) {
            o10 += qc.f.s(6, this.f13584t.get(i12));
        }
        if ((this.f13572h & 16) == 16) {
            o10 += qc.f.o(7, this.f13577m);
        }
        if ((this.f13572h & 64) == 64) {
            o10 += qc.f.o(8, this.f13580p);
        }
        if ((this.f13572h & 1) == 1) {
            o10 += qc.f.o(9, this.f13573i);
        }
        for (int i13 = 0; i13 < this.f13581q.size(); i13++) {
            o10 += qc.f.s(10, this.f13581q.get(i13));
        }
        int i14 = 0;
        for (int i15 = 0; i15 < this.f13582r.size(); i15++) {
            i14 += qc.f.p(this.f13582r.get(i15).intValue());
        }
        int i16 = o10 + i14;
        if (!S().isEmpty()) {
            i16 = i16 + 1 + qc.f.p(i14);
        }
        this.f13583s = i14;
        if ((this.f13572h & 128) == 128) {
            i16 += qc.f.s(30, this.f13585u);
        }
        int i17 = 0;
        for (int i18 = 0; i18 < this.f13586v.size(); i18++) {
            i17 += qc.f.p(this.f13586v.get(i18).intValue());
        }
        int size = i16 + i17 + (l0().size() * 2);
        if ((this.f13572h & 256) == 256) {
            size += qc.f.s(32, this.f13587w);
        }
        int o11 = size + o() + this.f13571g.size();
        this.f13589y = o11;
        return o11;
    }

    public t h0() {
        return this.f13585u;
    }

    public u i0(int i10) {
        return this.f13584t.get(i10);
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13588x;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        if (!o0()) {
            this.f13588x = (byte) 0;
            return false;
        }
        if (s0() && !c0().isInitialized()) {
            this.f13588x = (byte) 0;
            return false;
        }
        for (int i10 = 0; i10 < f0(); i10++) {
            if (!e0(i10).isInitialized()) {
                this.f13588x = (byte) 0;
                return false;
            }
        }
        if (q0() && !a0().isInitialized()) {
            this.f13588x = (byte) 0;
            return false;
        }
        for (int i11 = 0; i11 < R(); i11++) {
            if (!Q(i11).isInitialized()) {
                this.f13588x = (byte) 0;
                return false;
            }
        }
        for (int i12 = 0; i12 < j0(); i12++) {
            if (!i0(i12).isInitialized()) {
                this.f13588x = (byte) 0;
                return false;
            }
        }
        if (u0() && !h0().isInitialized()) {
            this.f13588x = (byte) 0;
            return false;
        }
        if (m0() && !U().isInitialized()) {
            this.f13588x = (byte) 0;
            return false;
        }
        if (!n()) {
            this.f13588x = (byte) 0;
            return false;
        }
        this.f13588x = (byte) 1;
        return true;
    }

    public int j0() {
        return this.f13584t.size();
    }

    public List<u> k0() {
        return this.f13584t;
    }

    public List<Integer> l0() {
        return this.f13586v;
    }

    public boolean m0() {
        return (this.f13572h & 256) == 256;
    }

    public boolean n0() {
        return (this.f13572h & 1) == 1;
    }

    public boolean o0() {
        return (this.f13572h & 4) == 4;
    }

    public boolean p0() {
        return (this.f13572h & 2) == 2;
    }

    public boolean q0() {
        return (this.f13572h & 32) == 32;
    }

    public boolean r0() {
        return (this.f13572h & 64) == 64;
    }

    public boolean s0() {
        return (this.f13572h & 8) == 8;
    }

    public boolean t0() {
        return (this.f13572h & 16) == 16;
    }

    public boolean u0() {
        return (this.f13572h & 128) == 128;
    }

    @Override // qc.q
    /* renamed from: y0, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return w0();
    }

    private i(i.c<i, ?> cVar) {
        super(cVar);
        this.f13583s = -1;
        this.f13588x = (byte) -1;
        this.f13589y = -1;
        this.f13571g = cVar.e();
    }

    private i(boolean z10) {
        this.f13583s = -1;
        this.f13588x = (byte) -1;
        this.f13589y = -1;
        this.f13571g = qc.d.f17259e;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0029. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v11 */
    /* JADX WARN: Type inference failed for: r4v13 */
    /* JADX WARN: Type inference failed for: r4v15 */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v5 */
    /* JADX WARN: Type inference failed for: r4v7 */
    /* JADX WARN: Type inference failed for: r4v9 */
    /* JADX WARN: Type inference failed for: r5v0 */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v2, types: [boolean] */
    private i(qc.e eVar, qc.g gVar) {
        this.f13583s = -1;
        this.f13588x = (byte) -1;
        this.f13589y = -1;
        v0();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        char c10 = 0;
        while (true) {
            ?? r52 = 1024;
            if (!z10) {
                try {
                    try {
                        try {
                            int K = eVar.K();
                            switch (K) {
                                case 0:
                                    z10 = true;
                                case 8:
                                    this.f13572h |= 2;
                                    this.f13574j = eVar.s();
                                case 16:
                                    this.f13572h |= 4;
                                    this.f13575k = eVar.s();
                                case 26:
                                    q.c builder = (this.f13572h & 8) == 8 ? this.f13576l.toBuilder() : null;
                                    q qVar = (q) eVar.u(q.f13722z, gVar);
                                    this.f13576l = qVar;
                                    if (builder != null) {
                                        builder.f(qVar);
                                        this.f13576l = builder.o();
                                    }
                                    this.f13572h |= 8;
                                case 34:
                                    int i10 = (c10 == true ? 1 : 0) & 32;
                                    c10 = c10;
                                    if (i10 != 32) {
                                        this.f13578n = new ArrayList();
                                        c10 = (c10 == true ? 1 : 0) | ' ';
                                    }
                                    this.f13578n.add(eVar.u(s.f13802s, gVar));
                                case 42:
                                    q.c builder2 = (this.f13572h & 32) == 32 ? this.f13579o.toBuilder() : null;
                                    q qVar2 = (q) eVar.u(q.f13722z, gVar);
                                    this.f13579o = qVar2;
                                    if (builder2 != null) {
                                        builder2.f(qVar2);
                                        this.f13579o = builder2.o();
                                    }
                                    this.f13572h |= 32;
                                case 50:
                                    int i11 = (c10 == true ? 1 : 0) & 1024;
                                    c10 = c10;
                                    if (i11 != 1024) {
                                        this.f13584t = new ArrayList();
                                        c10 = (c10 == true ? 1 : 0) | 1024;
                                    }
                                    this.f13584t.add(eVar.u(u.f13839r, gVar));
                                case 56:
                                    this.f13572h |= 16;
                                    this.f13577m = eVar.s();
                                case 64:
                                    this.f13572h |= 64;
                                    this.f13580p = eVar.s();
                                case 72:
                                    this.f13572h |= 1;
                                    this.f13573i = eVar.s();
                                case 82:
                                    int i12 = (c10 == true ? 1 : 0) & 256;
                                    c10 = c10;
                                    if (i12 != 256) {
                                        this.f13581q = new ArrayList();
                                        c10 = (c10 == true ? 1 : 0) | 256;
                                    }
                                    this.f13581q.add(eVar.u(q.f13722z, gVar));
                                case 88:
                                    int i13 = (c10 == true ? 1 : 0) & 512;
                                    c10 = c10;
                                    if (i13 != 512) {
                                        this.f13582r = new ArrayList();
                                        c10 = (c10 == true ? 1 : 0) | 512;
                                    }
                                    this.f13582r.add(Integer.valueOf(eVar.s()));
                                case 90:
                                    int j10 = eVar.j(eVar.A());
                                    int i14 = (c10 == true ? 1 : 0) & 512;
                                    c10 = c10;
                                    if (i14 != 512) {
                                        c10 = c10;
                                        if (eVar.e() > 0) {
                                            this.f13582r = new ArrayList();
                                            c10 = (c10 == true ? 1 : 0) | 512;
                                        }
                                    }
                                    while (eVar.e() > 0) {
                                        this.f13582r.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j10);
                                case 242:
                                    t.b builder3 = (this.f13572h & 128) == 128 ? this.f13585u.toBuilder() : null;
                                    t tVar = (t) eVar.u(t.f13828m, gVar);
                                    this.f13585u = tVar;
                                    if (builder3 != null) {
                                        builder3.f(tVar);
                                        this.f13585u = builder3.j();
                                    }
                                    this.f13572h |= 128;
                                case 248:
                                    int i15 = (c10 == true ? 1 : 0) & 4096;
                                    c10 = c10;
                                    if (i15 != 4096) {
                                        this.f13586v = new ArrayList();
                                        c10 = (c10 == true ? 1 : 0) | 4096;
                                    }
                                    this.f13586v.add(Integer.valueOf(eVar.s()));
                                case 250:
                                    int j11 = eVar.j(eVar.A());
                                    int i16 = (c10 == true ? 1 : 0) & 4096;
                                    c10 = c10;
                                    if (i16 != 4096) {
                                        c10 = c10;
                                        if (eVar.e() > 0) {
                                            this.f13586v = new ArrayList();
                                            c10 = (c10 == true ? 1 : 0) | 4096;
                                        }
                                    }
                                    while (eVar.e() > 0) {
                                        this.f13586v.add(Integer.valueOf(eVar.s()));
                                    }
                                    eVar.i(j11);
                                case 258:
                                    e.b builder4 = (this.f13572h & 256) == 256 ? this.f13587w.toBuilder() : null;
                                    e eVar2 = (e) eVar.u(e.f13500k, gVar);
                                    this.f13587w = eVar2;
                                    if (builder4 != null) {
                                        builder4.f(eVar2);
                                        this.f13587w = builder4.j();
                                    }
                                    this.f13572h |= 256;
                                default:
                                    r52 = j(eVar, J, gVar, K);
                                    if (r52 == 0) {
                                        z10 = true;
                                    }
                            }
                        } catch (IOException e10) {
                            throw new qc.k(e10.getMessage()).i(this);
                        }
                    } catch (qc.k e11) {
                        throw e11.i(this);
                    }
                } catch (Throwable th) {
                    if (((c10 == true ? 1 : 0) & 32) == 32) {
                        this.f13578n = Collections.unmodifiableList(this.f13578n);
                    }
                    if (((c10 == true ? 1 : 0) & 1024) == r52) {
                        this.f13584t = Collections.unmodifiableList(this.f13584t);
                    }
                    if (((c10 == true ? 1 : 0) & 256) == 256) {
                        this.f13581q = Collections.unmodifiableList(this.f13581q);
                    }
                    if (((c10 == true ? 1 : 0) & 512) == 512) {
                        this.f13582r = Collections.unmodifiableList(this.f13582r);
                    }
                    if (((c10 == true ? 1 : 0) & 4096) == 4096) {
                        this.f13586v = Collections.unmodifiableList(this.f13586v);
                    }
                    try {
                        J.I();
                    } catch (IOException unused) {
                    } catch (Throwable th2) {
                        this.f13571g = q10.v();
                        throw th2;
                    }
                    this.f13571g = q10.v();
                    g();
                    throw th;
                }
            } else {
                if (((c10 == true ? 1 : 0) & 32) == 32) {
                    this.f13578n = Collections.unmodifiableList(this.f13578n);
                }
                if (((c10 == true ? 1 : 0) & 1024) == 1024) {
                    this.f13584t = Collections.unmodifiableList(this.f13584t);
                }
                if (((c10 == true ? 1 : 0) & 256) == 256) {
                    this.f13581q = Collections.unmodifiableList(this.f13581q);
                }
                if (((c10 == true ? 1 : 0) & 512) == 512) {
                    this.f13582r = Collections.unmodifiableList(this.f13582r);
                }
                if (((c10 == true ? 1 : 0) & 4096) == 4096) {
                    this.f13586v = Collections.unmodifiableList(this.f13586v);
                }
                try {
                    J.I();
                } catch (IOException unused2) {
                } catch (Throwable th3) {
                    this.f13571g = q10.v();
                    throw th3;
                }
                this.f13571g = q10.v();
                g();
                return;
            }
        }
    }
}
