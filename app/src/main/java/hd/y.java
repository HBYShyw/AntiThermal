package hd;

import gd.TypeConstructor;
import gd.TypeProjection;
import gd.Variance;
import gd.g0;
import gd.h1;
import gd.s1;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import pb.DeclarationDescriptor;

/* compiled from: utils.kt */
/* loaded from: classes2.dex */
public final class y {
    private static final g0 a(g0 g0Var) {
        return md.b.a(g0Var).d();
    }

    private static final String b(TypeConstructor typeConstructor) {
        StringBuilder sb2 = new StringBuilder();
        c("type: " + typeConstructor, sb2);
        c("hashCode: " + typeConstructor.hashCode(), sb2);
        c("javaClass: " + typeConstructor.getClass().getCanonicalName(), sb2);
        for (DeclarationDescriptor v7 = typeConstructor.v(); v7 != null; v7 = v7.b()) {
            c("fqName: " + rc.c.f17708g.q(v7), sb2);
            c("javaClass: " + v7.getClass().getCanonicalName(), sb2);
        }
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    private static final StringBuilder c(String str, StringBuilder sb2) {
        za.k.e(str, "<this>");
        sb2.append(str);
        za.k.d(sb2, "append(value)");
        sb2.append('\n');
        za.k.d(sb2, "append('\\n')");
        return sb2;
    }

    public static final g0 d(g0 g0Var, g0 g0Var2, TypeCheckingProcedureCallbacks typeCheckingProcedureCallbacks) {
        boolean z10;
        za.k.e(g0Var, "subtype");
        za.k.e(g0Var2, "supertype");
        za.k.e(typeCheckingProcedureCallbacks, "typeCheckingProcedureCallbacks");
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add(new s(g0Var, null));
        TypeConstructor W0 = g0Var2.W0();
        while (!arrayDeque.isEmpty()) {
            s sVar = (s) arrayDeque.poll();
            g0 b10 = sVar.b();
            TypeConstructor W02 = b10.W0();
            if (typeCheckingProcedureCallbacks.a(W02, W0)) {
                boolean X0 = b10.X0();
                for (s a10 = sVar.a(); a10 != null; a10 = a10.a()) {
                    g0 b11 = a10.b();
                    List<TypeProjection> U0 = b11.U0();
                    if (!(U0 instanceof Collection) || !U0.isEmpty()) {
                        Iterator<T> it = U0.iterator();
                        while (it.hasNext()) {
                            if (((TypeProjection) it.next()).a() != Variance.INVARIANT) {
                                z10 = true;
                                break;
                            }
                        }
                    }
                    z10 = false;
                    if (z10) {
                        g0 n10 = tc.d.f(h1.f11827c.a(b11), false, 1, null).c().n(b10, Variance.INVARIANT);
                        za.k.d(n10, "TypeConstructorSubstitut…uted, Variance.INVARIANT)");
                        b10 = a(n10);
                    } else {
                        b10 = h1.f11827c.a(b11).c().n(b10, Variance.INVARIANT);
                        za.k.d(b10, "{\n                    Ty…ARIANT)\n                }");
                    }
                    X0 = X0 || b11.X0();
                }
                TypeConstructor W03 = b10.W0();
                if (typeCheckingProcedureCallbacks.a(W03, W0)) {
                    return s1.p(b10, X0);
                }
                throw new AssertionError("Type constructors should be equals!\nsubstitutedSuperType: " + b(W03) + ", \n\nsupertype: " + b(W0) + " \n" + typeCheckingProcedureCallbacks.a(W03, W0));
            }
            for (g0 g0Var3 : W02.q()) {
                za.k.d(g0Var3, "immediateSupertype");
                arrayDeque.add(new s(g0Var3, sVar));
            }
        }
        return null;
    }
}
