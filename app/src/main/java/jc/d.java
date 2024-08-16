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
public final class d extends i.d<d> {

    /* renamed from: n, reason: collision with root package name */
    private static final d f13486n;

    /* renamed from: o, reason: collision with root package name */
    public static qc.s<d> f13487o = new a();

    /* renamed from: g, reason: collision with root package name */
    private final qc.d f13488g;

    /* renamed from: h, reason: collision with root package name */
    private int f13489h;

    /* renamed from: i, reason: collision with root package name */
    private int f13490i;

    /* renamed from: j, reason: collision with root package name */
    private List<u> f13491j;

    /* renamed from: k, reason: collision with root package name */
    private List<Integer> f13492k;

    /* renamed from: l, reason: collision with root package name */
    private byte f13493l;

    /* renamed from: m, reason: collision with root package name */
    private int f13494m;

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    static class a extends qc.b<d> {
        a() {
        }

        @Override // qc.s
        /* renamed from: m, reason: merged with bridge method [inline-methods] */
        public d a(qc.e eVar, qc.g gVar) {
            return new d(eVar, gVar);
        }
    }

    /* compiled from: ProtoBuf.java */
    /* loaded from: classes2.dex */
    public static final class b extends i.c<d, b> {

        /* renamed from: h, reason: collision with root package name */
        private int f13495h;

        /* renamed from: i, reason: collision with root package name */
        private int f13496i = 6;

        /* renamed from: j, reason: collision with root package name */
        private List<u> f13497j = Collections.emptyList();

        /* renamed from: k, reason: collision with root package name */
        private List<Integer> f13498k = Collections.emptyList();

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
            if ((this.f13495h & 2) != 2) {
                this.f13497j = new ArrayList(this.f13497j);
                this.f13495h |= 2;
            }
        }

        private void s() {
            if ((this.f13495h & 4) != 4) {
                this.f13498k = new ArrayList(this.f13498k);
                this.f13495h |= 4;
            }
        }

        private void t() {
        }

        @Override // qc.q.a
        /* renamed from: n, reason: merged with bridge method [inline-methods] */
        public d build() {
            d o10 = o();
            if (o10.isInitialized()) {
                return o10;
            }
            throw a.AbstractC0092a.c(o10);
        }

        public d o() {
            d dVar = new d(this);
            int i10 = (this.f13495h & 1) != 1 ? 0 : 1;
            dVar.f13490i = this.f13496i;
            if ((this.f13495h & 2) == 2) {
                this.f13497j = Collections.unmodifiableList(this.f13497j);
                this.f13495h &= -3;
            }
            dVar.f13491j = this.f13497j;
            if ((this.f13495h & 4) == 4) {
                this.f13498k = Collections.unmodifiableList(this.f13498k);
                this.f13495h &= -5;
            }
            dVar.f13492k = this.f13498k;
            dVar.f13489h = i10;
            return dVar;
        }

        @Override // qc.i.b
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public b d() {
            return q().f(o());
        }

        @Override // qc.i.b
        /* renamed from: u, reason: merged with bridge method [inline-methods] */
        public b f(d dVar) {
            if (dVar == d.C()) {
                return this;
            }
            if (dVar.J()) {
                w(dVar.E());
            }
            if (!dVar.f13491j.isEmpty()) {
                if (this.f13497j.isEmpty()) {
                    this.f13497j = dVar.f13491j;
                    this.f13495h &= -3;
                } else {
                    r();
                    this.f13497j.addAll(dVar.f13491j);
                }
            }
            if (!dVar.f13492k.isEmpty()) {
                if (this.f13498k.isEmpty()) {
                    this.f13498k = dVar.f13492k;
                    this.f13495h &= -5;
                } else {
                    s();
                    this.f13498k.addAll(dVar.f13492k);
                }
            }
            k(dVar);
            g(e().d(dVar.f13488g));
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x001d  */
        @Override // qc.a.AbstractC0092a, qc.q.a
        /* renamed from: v, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b m(qc.e eVar, qc.g gVar) {
            d dVar = null;
            try {
                try {
                    d a10 = d.f13487o.a(eVar, gVar);
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
            } catch (qc.k e10) {
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

        public b w(int i10) {
            this.f13495h |= 1;
            this.f13496i = i10;
            return this;
        }
    }

    static {
        d dVar = new d(true);
        f13486n = dVar;
        dVar.K();
    }

    public static d C() {
        return f13486n;
    }

    private void K() {
        this.f13490i = 6;
        this.f13491j = Collections.emptyList();
        this.f13492k = Collections.emptyList();
    }

    public static b L() {
        return b.l();
    }

    public static b M(d dVar) {
        return L().f(dVar);
    }

    @Override // qc.r
    /* renamed from: D, reason: merged with bridge method [inline-methods] */
    public d getDefaultInstanceForType() {
        return f13486n;
    }

    public int E() {
        return this.f13490i;
    }

    public u F(int i10) {
        return this.f13491j.get(i10);
    }

    public int G() {
        return this.f13491j.size();
    }

    public List<u> H() {
        return this.f13491j;
    }

    public List<Integer> I() {
        return this.f13492k;
    }

    public boolean J() {
        return (this.f13489h & 1) == 1;
    }

    @Override // qc.q
    /* renamed from: N, reason: merged with bridge method [inline-methods] */
    public b newBuilderForType() {
        return L();
    }

    @Override // qc.q
    /* renamed from: O, reason: merged with bridge method [inline-methods] */
    public b toBuilder() {
        return M(this);
    }

    @Override // qc.q
    public void a(qc.f fVar) {
        getSerializedSize();
        i.d<MessageType>.a t7 = t();
        if ((this.f13489h & 1) == 1) {
            fVar.a0(1, this.f13490i);
        }
        for (int i10 = 0; i10 < this.f13491j.size(); i10++) {
            fVar.d0(2, this.f13491j.get(i10));
        }
        for (int i11 = 0; i11 < this.f13492k.size(); i11++) {
            fVar.a0(31, this.f13492k.get(i11).intValue());
        }
        t7.a(19000, fVar);
        fVar.i0(this.f13488g);
    }

    @Override // qc.i, qc.q
    public qc.s<d> getParserForType() {
        return f13487o;
    }

    @Override // qc.q
    public int getSerializedSize() {
        int i10 = this.f13494m;
        if (i10 != -1) {
            return i10;
        }
        int o10 = (this.f13489h & 1) == 1 ? qc.f.o(1, this.f13490i) + 0 : 0;
        for (int i11 = 0; i11 < this.f13491j.size(); i11++) {
            o10 += qc.f.s(2, this.f13491j.get(i11));
        }
        int i12 = 0;
        for (int i13 = 0; i13 < this.f13492k.size(); i13++) {
            i12 += qc.f.p(this.f13492k.get(i13).intValue());
        }
        int size = o10 + i12 + (I().size() * 2) + o() + this.f13488g.size();
        this.f13494m = size;
        return size;
    }

    @Override // qc.r
    public final boolean isInitialized() {
        byte b10 = this.f13493l;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        for (int i10 = 0; i10 < G(); i10++) {
            if (!F(i10).isInitialized()) {
                this.f13493l = (byte) 0;
                return false;
            }
        }
        if (!n()) {
            this.f13493l = (byte) 0;
            return false;
        }
        this.f13493l = (byte) 1;
        return true;
    }

    private d(i.c<d, ?> cVar) {
        super(cVar);
        this.f13493l = (byte) -1;
        this.f13494m = -1;
        this.f13488g = cVar.e();
    }

    private d(boolean z10) {
        this.f13493l = (byte) -1;
        this.f13494m = -1;
        this.f13488g = qc.d.f17259e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private d(qc.e eVar, qc.g gVar) {
        this.f13493l = (byte) -1;
        this.f13494m = -1;
        K();
        d.b q10 = qc.d.q();
        qc.f J = qc.f.J(q10, 1);
        boolean z10 = false;
        int i10 = 0;
        while (!z10) {
            try {
                try {
                    try {
                        int K = eVar.K();
                        if (K != 0) {
                            if (K == 8) {
                                this.f13489h |= 1;
                                this.f13490i = eVar.s();
                            } else if (K == 18) {
                                if ((i10 & 2) != 2) {
                                    this.f13491j = new ArrayList();
                                    i10 |= 2;
                                }
                                this.f13491j.add(eVar.u(u.f13839r, gVar));
                            } else if (K == 248) {
                                if ((i10 & 4) != 4) {
                                    this.f13492k = new ArrayList();
                                    i10 |= 4;
                                }
                                this.f13492k.add(Integer.valueOf(eVar.s()));
                            } else if (K != 250) {
                                if (!j(eVar, J, gVar, K)) {
                                }
                            } else {
                                int j10 = eVar.j(eVar.A());
                                if ((i10 & 4) != 4 && eVar.e() > 0) {
                                    this.f13492k = new ArrayList();
                                    i10 |= 4;
                                }
                                while (eVar.e() > 0) {
                                    this.f13492k.add(Integer.valueOf(eVar.s()));
                                }
                                eVar.i(j10);
                            }
                        }
                        z10 = true;
                    } catch (IOException e10) {
                        throw new qc.k(e10.getMessage()).i(this);
                    }
                } catch (qc.k e11) {
                    throw e11.i(this);
                }
            } catch (Throwable th) {
                if ((i10 & 2) == 2) {
                    this.f13491j = Collections.unmodifiableList(this.f13491j);
                }
                if ((i10 & 4) == 4) {
                    this.f13492k = Collections.unmodifiableList(this.f13492k);
                }
                try {
                    J.I();
                } catch (IOException unused) {
                } catch (Throwable th2) {
                    this.f13488g = q10.v();
                    throw th2;
                }
                this.f13488g = q10.v();
                g();
                throw th;
            }
        }
        if ((i10 & 2) == 2) {
            this.f13491j = Collections.unmodifiableList(this.f13491j);
        }
        if ((i10 & 4) == 4) {
            this.f13492k = Collections.unmodifiableList(this.f13492k);
        }
        try {
            J.I();
        } catch (IOException unused2) {
        } catch (Throwable th3) {
            this.f13488g = q10.v();
            throw th3;
        }
        this.f13488g = q10.v();
        g();
    }
}
