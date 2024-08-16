package lc;

import java.util.ArrayList;
import java.util.List;
import jc.n;
import jc.q;
import jc.r;
import jc.u;
import kotlin.collections.s;
import za.k;

/* compiled from: protoTypeTableUtil.kt */
/* renamed from: lc.f, reason: use source file name */
/* loaded from: classes2.dex */
public final class protoTypeTableUtil {
    public static final q a(q qVar, TypeTable typeTable) {
        k.e(qVar, "<this>");
        k.e(typeTable, "typeTable");
        if (qVar.e0()) {
            return qVar.M();
        }
        if (qVar.f0()) {
            return typeTable.a(qVar.N());
        }
        return null;
    }

    public static final List<q> b(jc.c cVar, TypeTable typeTable) {
        int u7;
        k.e(cVar, "<this>");
        k.e(typeTable, "typeTable");
        List<q> s02 = cVar.s0();
        if (!(!s02.isEmpty())) {
            s02 = null;
        }
        if (s02 == null) {
            List<Integer> r02 = cVar.r0();
            k.d(r02, "contextReceiverTypeIdList");
            u7 = s.u(r02, 10);
            s02 = new ArrayList<>(u7);
            for (Integer num : r02) {
                k.d(num, "it");
                s02.add(typeTable.a(num.intValue()));
            }
        }
        return s02;
    }

    public static final List<q> c(jc.i iVar, TypeTable typeTable) {
        int u7;
        k.e(iVar, "<this>");
        k.e(typeTable, "typeTable");
        List<q> T = iVar.T();
        if (!(!T.isEmpty())) {
            T = null;
        }
        if (T == null) {
            List<Integer> S = iVar.S();
            k.d(S, "contextReceiverTypeIdList");
            u7 = s.u(S, 10);
            T = new ArrayList<>(u7);
            for (Integer num : S) {
                k.d(num, "it");
                T.add(typeTable.a(num.intValue()));
            }
        }
        return T;
    }

    public static final List<q> d(n nVar, TypeTable typeTable) {
        int u7;
        k.e(nVar, "<this>");
        k.e(typeTable, "typeTable");
        List<q> S = nVar.S();
        if (!(!S.isEmpty())) {
            S = null;
        }
        if (S == null) {
            List<Integer> R = nVar.R();
            k.d(R, "contextReceiverTypeIdList");
            u7 = s.u(R, 10);
            S = new ArrayList<>(u7);
            for (Integer num : R) {
                k.d(num, "it");
                S.add(typeTable.a(num.intValue()));
            }
        }
        return S;
    }

    public static final q e(r rVar, TypeTable typeTable) {
        k.e(rVar, "<this>");
        k.e(typeTable, "typeTable");
        if (rVar.Y()) {
            q O = rVar.O();
            k.d(O, "expandedType");
            return O;
        }
        if (rVar.Z()) {
            return typeTable.a(rVar.P());
        }
        throw new IllegalStateException("No expandedType in ProtoBuf.TypeAlias".toString());
    }

    public static final q f(q qVar, TypeTable typeTable) {
        k.e(qVar, "<this>");
        k.e(typeTable, "typeTable");
        if (qVar.j0()) {
            return qVar.W();
        }
        if (qVar.k0()) {
            return typeTable.a(qVar.X());
        }
        return null;
    }

    public static final boolean g(jc.i iVar) {
        k.e(iVar, "<this>");
        return iVar.q0() || iVar.r0();
    }

    public static final boolean h(n nVar) {
        k.e(nVar, "<this>");
        return nVar.n0() || nVar.o0();
    }

    public static final q i(jc.c cVar, TypeTable typeTable) {
        k.e(cVar, "<this>");
        k.e(typeTable, "typeTable");
        if (cVar.j1()) {
            return cVar.E0();
        }
        if (cVar.k1()) {
            return typeTable.a(cVar.F0());
        }
        return null;
    }

    public static final q j(q qVar, TypeTable typeTable) {
        k.e(qVar, "<this>");
        k.e(typeTable, "typeTable");
        if (qVar.m0()) {
            return qVar.Z();
        }
        if (qVar.n0()) {
            return typeTable.a(qVar.a0());
        }
        return null;
    }

    public static final q k(jc.i iVar, TypeTable typeTable) {
        k.e(iVar, "<this>");
        k.e(typeTable, "typeTable");
        if (iVar.q0()) {
            return iVar.a0();
        }
        if (iVar.r0()) {
            return typeTable.a(iVar.b0());
        }
        return null;
    }

    public static final q l(n nVar, TypeTable typeTable) {
        k.e(nVar, "<this>");
        k.e(typeTable, "typeTable");
        if (nVar.n0()) {
            return nVar.Z();
        }
        if (nVar.o0()) {
            return typeTable.a(nVar.a0());
        }
        return null;
    }

    public static final q m(jc.i iVar, TypeTable typeTable) {
        k.e(iVar, "<this>");
        k.e(typeTable, "typeTable");
        if (iVar.s0()) {
            q c02 = iVar.c0();
            k.d(c02, "returnType");
            return c02;
        }
        if (iVar.t0()) {
            return typeTable.a(iVar.d0());
        }
        throw new IllegalStateException("No returnType in ProtoBuf.Function".toString());
    }

    public static final q n(n nVar, TypeTable typeTable) {
        k.e(nVar, "<this>");
        k.e(typeTable, "typeTable");
        if (nVar.p0()) {
            q b02 = nVar.b0();
            k.d(b02, "returnType");
            return b02;
        }
        if (nVar.q0()) {
            return typeTable.a(nVar.c0());
        }
        throw new IllegalStateException("No returnType in ProtoBuf.Property".toString());
    }

    public static final List<q> o(jc.c cVar, TypeTable typeTable) {
        int u7;
        k.e(cVar, "<this>");
        k.e(typeTable, "typeTable");
        List<q> V0 = cVar.V0();
        if (!(!V0.isEmpty())) {
            V0 = null;
        }
        if (V0 == null) {
            List<Integer> U0 = cVar.U0();
            k.d(U0, "supertypeIdList");
            u7 = s.u(U0, 10);
            V0 = new ArrayList<>(u7);
            for (Integer num : U0) {
                k.d(num, "it");
                V0.add(typeTable.a(num.intValue()));
            }
        }
        return V0;
    }

    public static final q p(q.b bVar, TypeTable typeTable) {
        k.e(bVar, "<this>");
        k.e(typeTable, "typeTable");
        if (bVar.w()) {
            return bVar.t();
        }
        if (bVar.x()) {
            return typeTable.a(bVar.u());
        }
        return null;
    }

    public static final q q(u uVar, TypeTable typeTable) {
        k.e(uVar, "<this>");
        k.e(typeTable, "typeTable");
        if (uVar.N()) {
            q H = uVar.H();
            k.d(H, "type");
            return H;
        }
        if (uVar.O()) {
            return typeTable.a(uVar.I());
        }
        throw new IllegalStateException("No type in ProtoBuf.ValueParameter".toString());
    }

    public static final q r(r rVar, TypeTable typeTable) {
        k.e(rVar, "<this>");
        k.e(typeTable, "typeTable");
        if (rVar.c0()) {
            q V = rVar.V();
            k.d(V, "underlyingType");
            return V;
        }
        if (rVar.d0()) {
            return typeTable.a(rVar.W());
        }
        throw new IllegalStateException("No underlyingType in ProtoBuf.TypeAlias".toString());
    }

    public static final List<q> s(jc.s sVar, TypeTable typeTable) {
        int u7;
        k.e(sVar, "<this>");
        k.e(typeTable, "typeTable");
        List<q> N = sVar.N();
        if (!(!N.isEmpty())) {
            N = null;
        }
        if (N == null) {
            List<Integer> M = sVar.M();
            k.d(M, "upperBoundIdList");
            u7 = s.u(M, 10);
            N = new ArrayList<>(u7);
            for (Integer num : M) {
                k.d(num, "it");
                N.add(typeTable.a(num.intValue()));
            }
        }
        return N;
    }

    public static final q t(u uVar, TypeTable typeTable) {
        k.e(uVar, "<this>");
        k.e(typeTable, "typeTable");
        if (uVar.P()) {
            return uVar.J();
        }
        if (uVar.Q()) {
            return typeTable.a(uVar.K());
        }
        return null;
    }
}
