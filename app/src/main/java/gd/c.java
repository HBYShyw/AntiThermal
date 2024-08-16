package gd;

import gd.f1;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Set;
import kotlin.collections._Collections;

/* compiled from: AbstractTypeChecker.kt */
/* loaded from: classes2.dex */
public final class c {

    /* renamed from: a, reason: collision with root package name */
    public static final c f11746a = new c();

    private c() {
    }

    private final boolean c(f1 f1Var, kd.k kVar, kd.n nVar) {
        kd.p j10 = f1Var.j();
        if (j10.j(kVar)) {
            return true;
        }
        if (j10.B(kVar)) {
            return false;
        }
        if (f1Var.n() && j10.F0(kVar)) {
            return true;
        }
        return j10.y(j10.b(kVar), nVar);
    }

    private final boolean e(f1 f1Var, kd.k kVar, kd.k kVar2) {
        kd.p j10 = f1Var.j();
        if (f.f11760b) {
            if (!j10.f(kVar) && !j10.A0(j10.b(kVar))) {
                f1Var.l(kVar);
            }
            if (!j10.f(kVar2)) {
                f1Var.l(kVar2);
            }
        }
        if (j10.B(kVar2) || j10.i0(kVar) || j10.H(kVar)) {
            return true;
        }
        if ((kVar instanceof kd.d) && j10.C((kd.d) kVar)) {
            return true;
        }
        c cVar = f11746a;
        if (cVar.a(f1Var, kVar, f1.c.b.f11793a)) {
            return true;
        }
        if (j10.i0(kVar2) || cVar.a(f1Var, kVar2, f1.c.d.f11795a) || j10.Z(kVar)) {
            return false;
        }
        return cVar.b(f1Var, kVar, j10.b(kVar2));
    }

    public final boolean a(f1 f1Var, kd.k kVar, f1.c cVar) {
        String c02;
        za.k.e(f1Var, "<this>");
        za.k.e(kVar, "type");
        za.k.e(cVar, "supertypesPolicy");
        kd.p j10 = f1Var.j();
        if (!((j10.Z(kVar) && !j10.B(kVar)) || j10.i0(kVar))) {
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
                        f1.c cVar2 = j10.B(pop) ? f1.c.C0040c.f11794a : cVar;
                        if (!(!za.k.a(cVar2, f1.c.C0040c.f11794a))) {
                            cVar2 = null;
                        }
                        if (cVar2 == null) {
                            continue;
                        } else {
                            kd.p j11 = f1Var.j();
                            Iterator<kd.i> it = j11.Q(j11.b(pop)).iterator();
                            while (it.hasNext()) {
                                kd.k a10 = cVar2.a(f1Var, it.next());
                                if ((j10.Z(a10) && !j10.B(a10)) || j10.i0(a10)) {
                                    f1Var.e();
                                } else {
                                    h10.add(a10);
                                }
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
        return true;
    }

    public final boolean b(f1 f1Var, kd.k kVar, kd.n nVar) {
        String c02;
        za.k.e(f1Var, "state");
        za.k.e(kVar, "start");
        za.k.e(nVar, "end");
        kd.p j10 = f1Var.j();
        if (f11746a.c(f1Var, kVar, nVar)) {
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
                    f1.c cVar = j10.B(pop) ? f1.c.C0040c.f11794a : f1.c.b.f11793a;
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
                            if (f11746a.c(f1Var, a10, nVar)) {
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

    public final boolean d(f1 f1Var, kd.k kVar, kd.k kVar2) {
        za.k.e(f1Var, "state");
        za.k.e(kVar, "subType");
        za.k.e(kVar2, "superType");
        return e(f1Var, kVar, kVar2);
    }
}
