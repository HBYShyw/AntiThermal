package gd;

import gd.f1;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.MutableCollections;
import kotlin.collections._Collections;
import ma.NoWhenBranchMatchedException;
import ma.Unit;
import qd.SmartList;
import za.Lambda;

/* compiled from: AbstractTypeChecker.kt */
/* loaded from: classes2.dex */
public final class f {

    /* renamed from: a */
    public static final f f11759a = new f();

    /* renamed from: b */
    public static boolean f11760b;

    /* compiled from: AbstractTypeChecker.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a */
        public static final /* synthetic */ int[] f11761a;

        /* renamed from: b */
        public static final /* synthetic */ int[] f11762b;

        static {
            int[] iArr = new int[kd.u.values().length];
            try {
                iArr[kd.u.INV.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[kd.u.OUT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[kd.u.IN.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f11761a = iArr;
            int[] iArr2 = new int[f1.b.values().length];
            try {
                iArr2[f1.b.CHECK_ONLY_LOWER.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr2[f1.b.CHECK_SUBTYPE_AND_LOWER.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr2[f1.b.SKIP_LOWER.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
            f11762b = iArr2;
        }
    }

    /* compiled from: AbstractTypeChecker.kt */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.l<f1.a, Unit> {

        /* renamed from: e */
        final /* synthetic */ List<kd.k> f11763e;

        /* renamed from: f */
        final /* synthetic */ f1 f11764f;

        /* renamed from: g */
        final /* synthetic */ kd.p f11765g;

        /* renamed from: h */
        final /* synthetic */ kd.k f11766h;

        /* compiled from: AbstractTypeChecker.kt */
        /* loaded from: classes2.dex */
        public static final class a extends Lambda implements ya.a<Boolean> {

            /* renamed from: e */
            final /* synthetic */ f1 f11767e;

            /* renamed from: f */
            final /* synthetic */ kd.p f11768f;

            /* renamed from: g */
            final /* synthetic */ kd.k f11769g;

            /* renamed from: h */
            final /* synthetic */ kd.k f11770h;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(f1 f1Var, kd.p pVar, kd.k kVar, kd.k kVar2) {
                super(0);
                this.f11767e = f1Var;
                this.f11768f = pVar;
                this.f11769g = kVar;
                this.f11770h = kVar2;
            }

            @Override // ya.a
            /* renamed from: a */
            public final Boolean invoke() {
                return Boolean.valueOf(f.f11759a.q(this.f11767e, this.f11768f.I(this.f11769g), this.f11770h));
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        b(List<? extends kd.k> list, f1 f1Var, kd.p pVar, kd.k kVar) {
            super(1);
            this.f11763e = list;
            this.f11764f = f1Var;
            this.f11765g = pVar;
            this.f11766h = kVar;
        }

        public final void a(f1.a aVar) {
            za.k.e(aVar, "$this$runForkingPoint");
            Iterator<kd.k> it = this.f11763e.iterator();
            while (it.hasNext()) {
                aVar.a(new a(this.f11764f, this.f11765g, it.next(), this.f11766h));
            }
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(f1.a aVar) {
            a(aVar);
            return Unit.f15173a;
        }
    }

    private f() {
    }

    private final Boolean a(f1 f1Var, kd.k kVar, kd.k kVar2) {
        kd.p j10 = f1Var.j();
        if (!j10.o0(kVar) && !j10.o0(kVar2)) {
            return null;
        }
        if (d(j10, kVar) && d(j10, kVar2)) {
            return Boolean.TRUE;
        }
        if (j10.o0(kVar)) {
            if (e(j10, f1Var, kVar, kVar2, false)) {
                return Boolean.TRUE;
            }
        } else if (j10.o0(kVar2) && (c(j10, kVar) || e(j10, f1Var, kVar2, kVar, true))) {
            return Boolean.TRUE;
        }
        return null;
    }

    private static final boolean b(kd.p pVar, kd.k kVar) {
        if (!(kVar instanceof kd.d)) {
            return false;
        }
        kd.m n10 = pVar.n(pVar.f0((kd.d) kVar));
        return !pVar.d0(n10) && pVar.o0(pVar.s0(pVar.X(n10)));
    }

    private static final boolean c(kd.p pVar, kd.k kVar) {
        boolean z10;
        kd.n b10 = pVar.b(kVar);
        if (b10 instanceof kd.h) {
            Collection<kd.i> Q = pVar.Q(b10);
            if (!(Q instanceof Collection) || !Q.isEmpty()) {
                Iterator<T> it = Q.iterator();
                while (it.hasNext()) {
                    kd.k c10 = pVar.c((kd.i) it.next());
                    if (c10 != null && pVar.o0(c10)) {
                        z10 = true;
                        break;
                    }
                }
            }
            z10 = false;
            if (z10) {
                return true;
            }
        }
        return false;
    }

    private static final boolean d(kd.p pVar, kd.k kVar) {
        return pVar.o0(kVar) || b(pVar, kVar);
    }

    private static final boolean e(kd.p pVar, f1 f1Var, kd.k kVar, kd.k kVar2, boolean z10) {
        Collection<kd.i> x02 = pVar.x0(kVar);
        if (!(x02 instanceof Collection) || !x02.isEmpty()) {
            for (kd.i iVar : x02) {
                if (za.k.a(pVar.F(iVar), pVar.b(kVar2)) || (z10 && t(f11759a, f1Var, kVar2, iVar, false, 8, null))) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:73:0x011e, code lost:
    
        if (r1 != false) goto L189;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final Boolean f(f1 f1Var, kd.k kVar, kd.k kVar2) {
        kd.k kVar3;
        kd.p j10 = f1Var.j();
        if (j10.v(kVar) || j10.v(kVar2)) {
            return f1Var.m() ? Boolean.TRUE : (!j10.B(kVar) || j10.B(kVar2)) ? Boolean.valueOf(AbstractStrictEqualityTypeChecker.f11751a.b(j10, j10.e(kVar, false), j10.e(kVar2, false))) : Boolean.FALSE;
        }
        if (j10.l0(kVar) && j10.l0(kVar2)) {
            return Boolean.valueOf(f11759a.p(j10, kVar, kVar2) || f1Var.n());
        }
        if (!j10.F0(kVar) && !j10.F0(kVar2)) {
            kd.e u7 = j10.u(kVar2);
            if (u7 == null || (kVar3 = j10.J(u7)) == null) {
                kVar3 = kVar2;
            }
            kd.d g6 = j10.g(kVar3);
            kd.i r02 = g6 != null ? j10.r0(g6) : null;
            if (g6 != null && r02 != null) {
                if (j10.B(kVar2)) {
                    r02 = j10.k(r02, true);
                } else if (j10.i0(kVar2)) {
                    r02 = j10.m(r02);
                }
                kd.i iVar = r02;
                int i10 = a.f11762b[f1Var.g(kVar, g6).ordinal()];
                if (i10 != 1) {
                    if (i10 == 2 && t(f11759a, f1Var, kVar, iVar, false, 8, null)) {
                        return Boolean.TRUE;
                    }
                } else {
                    return Boolean.valueOf(t(f11759a, f1Var, kVar, iVar, false, 8, null));
                }
            }
            kd.n b10 = j10.b(kVar2);
            if (j10.A0(b10)) {
                j10.B(kVar2);
                Collection<kd.i> Q = j10.Q(b10);
                if (!(Q instanceof Collection) || !Q.isEmpty()) {
                    Iterator<T> it = Q.iterator();
                    while (it.hasNext()) {
                        if (!t(f11759a, f1Var, kVar, (kd.i) it.next(), false, 8, null)) {
                            break;
                        }
                    }
                }
                r1 = true;
                return Boolean.valueOf(r1);
            }
            kd.n b11 = j10.b(kVar);
            if (!(kVar instanceof kd.d)) {
                if (j10.A0(b11)) {
                    Collection<kd.i> Q2 = j10.Q(b11);
                    if (!(Q2 instanceof Collection) || !Q2.isEmpty()) {
                        Iterator<T> it2 = Q2.iterator();
                        while (it2.hasNext()) {
                            if (!(((kd.i) it2.next()) instanceof kd.d)) {
                                break;
                            }
                        }
                    }
                    r1 = true;
                }
                return null;
            }
            kd.o m10 = f11759a.m(f1Var.j(), kVar2, kVar);
            if (m10 != null && j10.E(m10, j10.b(kVar2))) {
                return Boolean.TRUE;
            }
            return null;
        }
        return Boolean.valueOf(f1Var.n());
    }

    private final List<kd.k> g(f1 f1Var, kd.k kVar, kd.n nVar) {
        String c02;
        f1.c t02;
        List<kd.k> j10;
        List<kd.k> e10;
        List<kd.k> j11;
        kd.p j12 = f1Var.j();
        List<kd.k> q10 = j12.q(kVar, nVar);
        if (q10 != null) {
            return q10;
        }
        if (!j12.e0(nVar) && j12.Z(kVar)) {
            j11 = kotlin.collections.r.j();
            return j11;
        }
        if (j12.V(nVar)) {
            if (j12.y(j12.b(kVar), nVar)) {
                kd.k a02 = j12.a0(kVar, kd.b.FOR_SUBTYPING);
                if (a02 != null) {
                    kVar = a02;
                }
                e10 = CollectionsJVM.e(kVar);
                return e10;
            }
            j10 = kotlin.collections.r.j();
            return j10;
        }
        SmartList smartList = new SmartList();
        f1Var.k();
        ArrayDeque<kd.k> h10 = f1Var.h();
        za.k.b(h10);
        Set<kd.k> i10 = f1Var.i();
        za.k.b(i10);
        h10.push(kVar);
        while (!h10.isEmpty()) {
            if (i10.size() <= 1000) {
                kd.k pop = h10.pop();
                za.k.d(pop, "current");
                if (i10.add(pop)) {
                    kd.k a03 = j12.a0(pop, kd.b.FOR_SUBTYPING);
                    if (a03 == null) {
                        a03 = pop;
                    }
                    if (j12.y(j12.b(a03), nVar)) {
                        smartList.add(a03);
                        t02 = f1.c.C0040c.f11794a;
                    } else if (j12.c0(a03) == 0) {
                        t02 = f1.c.b.f11793a;
                    } else {
                        t02 = f1Var.j().t0(a03);
                    }
                    if (!(!za.k.a(t02, f1.c.C0040c.f11794a))) {
                        t02 = null;
                    }
                    if (t02 != null) {
                        kd.p j13 = f1Var.j();
                        Iterator<kd.i> it = j13.Q(j13.b(pop)).iterator();
                        while (it.hasNext()) {
                            h10.add(t02.a(f1Var, it.next()));
                        }
                    }
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Too many supertypes for type: ");
                sb2.append(kVar);
                sb2.append(". Supertypes = ");
                c02 = _Collections.c0(i10, null, null, null, 0, null, null, 63, null);
                sb2.append(c02);
                throw new IllegalStateException(sb2.toString().toString());
            }
        }
        f1Var.e();
        return smartList;
    }

    private final List<kd.k> h(f1 f1Var, kd.k kVar, kd.n nVar) {
        return w(f1Var, g(f1Var, kVar, nVar));
    }

    private final boolean i(f1 f1Var, kd.i iVar, kd.i iVar2, boolean z10) {
        kd.p j10 = f1Var.j();
        kd.i o10 = f1Var.o(f1Var.p(iVar));
        kd.i o11 = f1Var.o(f1Var.p(iVar2));
        f fVar = f11759a;
        Boolean f10 = fVar.f(f1Var, j10.v0(o10), j10.s0(o11));
        if (f10 != null) {
            boolean booleanValue = f10.booleanValue();
            f1Var.c(o10, o11, z10);
            return booleanValue;
        }
        Boolean c10 = f1Var.c(o10, o11, z10);
        return c10 != null ? c10.booleanValue() : fVar.u(f1Var, j10.v0(o10), j10.s0(o11));
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0063, code lost:
    
        return r8.k0(r8.F(r9), r2);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final kd.o m(kd.p pVar, kd.i iVar, kd.i iVar2) {
        kd.i X;
        int c02 = pVar.c0(iVar);
        int i10 = 0;
        while (true) {
            if (i10 >= c02) {
                return null;
            }
            kd.m S = pVar.S(iVar, i10);
            kd.m mVar = pVar.d0(S) ^ true ? S : null;
            if (mVar != null && (X = pVar.X(mVar)) != null) {
                boolean z10 = pVar.q0(pVar.O(pVar.v0(X))) && pVar.q0(pVar.O(pVar.v0(iVar2)));
                if (za.k.a(X, iVar2) || (z10 && za.k.a(pVar.F(X), pVar.F(iVar2)))) {
                    break;
                }
                kd.o m10 = m(pVar, X, iVar2);
                if (m10 != null) {
                    return m10;
                }
            }
            i10++;
        }
    }

    private final boolean n(f1 f1Var, kd.k kVar) {
        String c02;
        f1.c cVar;
        kd.p j10 = f1Var.j();
        kd.n b10 = j10.b(kVar);
        if (j10.e0(b10)) {
            return j10.y0(b10);
        }
        if (j10.y0(j10.b(kVar))) {
            return true;
        }
        f1Var.k();
        ArrayDeque<kd.k> h10 = f1Var.h();
        za.k.b(h10);
        Set<kd.k> i10 = f1Var.i();
        za.k.b(i10);
        h10.push(kVar);
        while (!h10.isEmpty()) {
            if (i10.size() <= 1000) {
                kd.k pop = h10.pop();
                za.k.d(pop, "current");
                if (i10.add(pop)) {
                    if (j10.Z(pop)) {
                        cVar = f1.c.C0040c.f11794a;
                    } else {
                        cVar = f1.c.b.f11793a;
                    }
                    if (!(!za.k.a(cVar, f1.c.C0040c.f11794a))) {
                        cVar = null;
                    }
                    if (cVar == null) {
                        continue;
                    } else {
                        kd.p j11 = f1Var.j();
                        Iterator<kd.i> it = j11.Q(j11.b(pop)).iterator();
                        while (it.hasNext()) {
                            kd.k a10 = cVar.a(f1Var, it.next());
                            if (j10.y0(j10.b(a10))) {
                                f1Var.e();
                                return true;
                            }
                            h10.add(a10);
                        }
                    }
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Too many supertypes for type: ");
                sb2.append(kVar);
                sb2.append(". Supertypes = ");
                c02 = _Collections.c0(i10, null, null, null, 0, null, null, 63, null);
                sb2.append(c02);
                throw new IllegalStateException(sb2.toString().toString());
            }
        }
        f1Var.e();
        return false;
    }

    private final boolean o(kd.p pVar, kd.i iVar) {
        return (!pVar.U(pVar.F(iVar)) || pVar.w0(iVar) || pVar.i0(iVar) || pVar.H(iVar) || !za.k.a(pVar.b(pVar.v0(iVar)), pVar.b(pVar.s0(iVar)))) ? false : true;
    }

    private final boolean p(kd.p pVar, kd.k kVar, kd.k kVar2) {
        kd.k kVar3;
        kd.k kVar4;
        kd.e u7 = pVar.u(kVar);
        if (u7 == null || (kVar3 = pVar.J(u7)) == null) {
            kVar3 = kVar;
        }
        kd.e u10 = pVar.u(kVar2);
        if (u10 == null || (kVar4 = pVar.J(u10)) == null) {
            kVar4 = kVar2;
        }
        if (pVar.b(kVar3) != pVar.b(kVar4)) {
            return false;
        }
        if (pVar.i0(kVar) || !pVar.i0(kVar2)) {
            return !pVar.B(kVar) || pVar.B(kVar2);
        }
        return false;
    }

    public static /* synthetic */ boolean t(f fVar, f1 f1Var, kd.i iVar, kd.i iVar2, boolean z10, int i10, Object obj) {
        if ((i10 & 8) != 0) {
            z10 = false;
        }
        return fVar.s(f1Var, iVar, iVar2, z10);
    }

    private final boolean u(f1 f1Var, kd.k kVar, kd.k kVar2) {
        int u7;
        Object T;
        int u10;
        kd.i X;
        kd.p j10 = f1Var.j();
        if (f11760b) {
            if (!j10.f(kVar) && !j10.A0(j10.b(kVar))) {
                f1Var.l(kVar);
            }
            if (!j10.f(kVar2)) {
                f1Var.l(kVar2);
            }
        }
        boolean z10 = false;
        if (!c.f11746a.d(f1Var, kVar, kVar2)) {
            return false;
        }
        f fVar = f11759a;
        Boolean a10 = fVar.a(f1Var, j10.v0(kVar), j10.s0(kVar2));
        if (a10 != null) {
            boolean booleanValue = a10.booleanValue();
            f1.d(f1Var, kVar, kVar2, false, 4, null);
            return booleanValue;
        }
        kd.n b10 = j10.b(kVar2);
        boolean z11 = true;
        if ((j10.y(j10.b(kVar), b10) && j10.n0(b10) == 0) || j10.P(j10.b(kVar2))) {
            return true;
        }
        List<kd.k> l10 = fVar.l(f1Var, kVar, b10);
        int i10 = 10;
        u7 = kotlin.collections.s.u(l10, 10);
        ArrayList<kd.k> arrayList = new ArrayList(u7);
        for (kd.k kVar3 : l10) {
            kd.k c10 = j10.c(f1Var.o(kVar3));
            if (c10 != null) {
                kVar3 = c10;
            }
            arrayList.add(kVar3);
        }
        int size = arrayList.size();
        if (size == 0) {
            return f11759a.n(f1Var, kVar);
        }
        if (size != 1) {
            kd.a aVar = new kd.a(j10.n0(b10));
            int n02 = j10.n0(b10);
            int i11 = 0;
            boolean z12 = false;
            while (i11 < n02) {
                z12 = (z12 || j10.x(j10.k0(b10, i11)) != kd.u.OUT) ? z11 : z10;
                if (!z12) {
                    u10 = kotlin.collections.s.u(arrayList, i10);
                    ArrayList arrayList2 = new ArrayList(u10);
                    for (kd.k kVar4 : arrayList) {
                        kd.m C0 = j10.C0(kVar4, i11);
                        if (C0 != null) {
                            if (!(j10.R(C0) == kd.u.INV)) {
                                C0 = null;
                            }
                            if (C0 != null && (X = j10.X(C0)) != null) {
                                arrayList2.add(X);
                            }
                        }
                        throw new IllegalStateException(("Incorrect type: " + kVar4 + ", subType: " + kVar + ", superType: " + kVar2).toString());
                    }
                    aVar.add(j10.D0(j10.G(arrayList2)));
                }
                i11++;
                z10 = false;
                z11 = true;
                i10 = 10;
            }
            if (z12 || !f11759a.q(f1Var, aVar, kVar2)) {
                return f1Var.q(new b(arrayList, f1Var, j10, kVar2));
            }
            return true;
        }
        f fVar2 = f11759a;
        T = _Collections.T(arrayList);
        return fVar2.q(f1Var, j10.I((kd.k) T), kVar2);
    }

    private final boolean v(kd.p pVar, kd.i iVar, kd.i iVar2, kd.n nVar) {
        kd.o Y;
        kd.k c10 = pVar.c(iVar);
        if (!(c10 instanceof kd.d)) {
            return false;
        }
        kd.d dVar = (kd.d) c10;
        if (pVar.j0(dVar) || !pVar.d0(pVar.n(pVar.f0(dVar))) || pVar.m0(dVar) != kd.b.FOR_SUBTYPING) {
            return false;
        }
        kd.n F = pVar.F(iVar2);
        kd.t tVar = F instanceof kd.t ? (kd.t) F : null;
        return (tVar == null || (Y = pVar.Y(tVar)) == null || !pVar.E(Y, nVar)) ? false : true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final List<kd.k> w(f1 f1Var, List<? extends kd.k> list) {
        kd.p j10 = f1Var.j();
        if (list.size() < 2) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (true) {
            boolean z10 = true;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            kd.l I = j10.I((kd.k) next);
            int t7 = j10.t(I);
            int i10 = 0;
            while (true) {
                if (i10 >= t7) {
                    break;
                }
                if (!(j10.z(j10.X(j10.N(I, i10))) == null)) {
                    z10 = false;
                    break;
                }
                i10++;
            }
            if (z10) {
                arrayList.add(next);
            }
        }
        return arrayList.isEmpty() ^ true ? arrayList : list;
    }

    public final kd.u j(kd.u uVar, kd.u uVar2) {
        za.k.e(uVar, "declared");
        za.k.e(uVar2, "useSite");
        kd.u uVar3 = kd.u.INV;
        if (uVar == uVar3) {
            return uVar2;
        }
        if (uVar2 == uVar3 || uVar == uVar2) {
            return uVar;
        }
        return null;
    }

    public final boolean k(f1 f1Var, kd.i iVar, kd.i iVar2) {
        za.k.e(f1Var, "state");
        za.k.e(iVar, "a");
        za.k.e(iVar2, "b");
        kd.p j10 = f1Var.j();
        if (iVar == iVar2) {
            return true;
        }
        f fVar = f11759a;
        if (fVar.o(j10, iVar) && fVar.o(j10, iVar2)) {
            kd.i o10 = f1Var.o(f1Var.p(iVar));
            kd.i o11 = f1Var.o(f1Var.p(iVar2));
            kd.k v02 = j10.v0(o10);
            if (!j10.y(j10.F(o10), j10.F(o11))) {
                return false;
            }
            if (j10.c0(v02) == 0) {
                return j10.b0(o10) || j10.b0(o11) || j10.B(v02) == j10.B(j10.v0(o11));
            }
        }
        return t(fVar, f1Var, iVar, iVar2, false, 8, null) && t(fVar, f1Var, iVar2, iVar, false, 8, null);
    }

    public final List<kd.k> l(f1 f1Var, kd.k kVar, kd.n nVar) {
        String c02;
        f1.c cVar;
        za.k.e(f1Var, "state");
        za.k.e(kVar, "subType");
        za.k.e(nVar, "superConstructor");
        kd.p j10 = f1Var.j();
        if (j10.Z(kVar)) {
            return f11759a.h(f1Var, kVar, nVar);
        }
        if (!j10.e0(nVar) && !j10.z0(nVar)) {
            return f11759a.g(f1Var, kVar, nVar);
        }
        SmartList<kd.k> smartList = new SmartList();
        f1Var.k();
        ArrayDeque<kd.k> h10 = f1Var.h();
        za.k.b(h10);
        Set<kd.k> i10 = f1Var.i();
        za.k.b(i10);
        h10.push(kVar);
        while (!h10.isEmpty()) {
            if (i10.size() <= 1000) {
                kd.k pop = h10.pop();
                za.k.d(pop, "current");
                if (i10.add(pop)) {
                    if (j10.Z(pop)) {
                        smartList.add(pop);
                        cVar = f1.c.C0040c.f11794a;
                    } else {
                        cVar = f1.c.b.f11793a;
                    }
                    if (!(!za.k.a(cVar, f1.c.C0040c.f11794a))) {
                        cVar = null;
                    }
                    if (cVar != null) {
                        kd.p j11 = f1Var.j();
                        Iterator<kd.i> it = j11.Q(j11.b(pop)).iterator();
                        while (it.hasNext()) {
                            h10.add(cVar.a(f1Var, it.next()));
                        }
                    }
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Too many supertypes for type: ");
                sb2.append(kVar);
                sb2.append(". Supertypes = ");
                c02 = _Collections.c0(i10, null, null, null, 0, null, null, 63, null);
                sb2.append(c02);
                throw new IllegalStateException(sb2.toString().toString());
            }
        }
        f1Var.e();
        ArrayList arrayList = new ArrayList();
        for (kd.k kVar2 : smartList) {
            f fVar = f11759a;
            za.k.d(kVar2, "it");
            MutableCollections.z(arrayList, fVar.h(f1Var, kVar2, nVar));
        }
        return arrayList;
    }

    public final boolean q(f1 f1Var, kd.l lVar, kd.k kVar) {
        int i10;
        int i11;
        boolean k10;
        int i12;
        za.k.e(f1Var, "<this>");
        za.k.e(lVar, "capturedSubArguments");
        za.k.e(kVar, "superType");
        kd.p j10 = f1Var.j();
        kd.n b10 = j10.b(kVar);
        int t7 = j10.t(lVar);
        int n02 = j10.n0(b10);
        if (t7 != n02 || t7 != j10.c0(kVar)) {
            return false;
        }
        for (int i13 = 0; i13 < n02; i13++) {
            kd.m S = j10.S(kVar, i13);
            if (!j10.d0(S)) {
                kd.i X = j10.X(S);
                kd.m N = j10.N(lVar, i13);
                j10.R(N);
                kd.u uVar = kd.u.INV;
                kd.i X2 = j10.X(N);
                f fVar = f11759a;
                kd.u j11 = fVar.j(j10.x(j10.k0(b10, i13)), j10.R(S));
                if (j11 == null) {
                    return f1Var.m();
                }
                if (j11 == uVar && (fVar.v(j10, X2, X, b10) || fVar.v(j10, X, X2, b10))) {
                    continue;
                } else {
                    i10 = f1Var.f11784g;
                    if (i10 <= 100) {
                        i11 = f1Var.f11784g;
                        f1Var.f11784g = i11 + 1;
                        int i14 = a.f11761a[j11.ordinal()];
                        if (i14 == 1) {
                            k10 = fVar.k(f1Var, X2, X);
                        } else if (i14 != 2) {
                            if (i14 != 3) {
                                throw new NoWhenBranchMatchedException();
                            }
                            k10 = t(fVar, f1Var, X, X2, false, 8, null);
                        } else {
                            k10 = t(fVar, f1Var, X2, X, false, 8, null);
                        }
                        i12 = f1Var.f11784g;
                        f1Var.f11784g = i12 - 1;
                        if (!k10) {
                            return false;
                        }
                    } else {
                        throw new IllegalStateException(("Arguments depth is too high. Some related argument: " + X2).toString());
                    }
                }
            }
        }
        return true;
    }

    public final boolean r(f1 f1Var, kd.i iVar, kd.i iVar2) {
        za.k.e(f1Var, "state");
        za.k.e(iVar, "subType");
        za.k.e(iVar2, "superType");
        return t(this, f1Var, iVar, iVar2, false, 8, null);
    }

    public final boolean s(f1 f1Var, kd.i iVar, kd.i iVar2, boolean z10) {
        za.k.e(f1Var, "state");
        za.k.e(iVar, "subType");
        za.k.e(iVar2, "superType");
        if (iVar == iVar2) {
            return true;
        }
        if (f1Var.f(iVar, iVar2)) {
            return i(f1Var, iVar, iVar2, z10);
        }
        return false;
    }
}
