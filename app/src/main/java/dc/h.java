package dc;

import gd.TypeParameterUpperBoundEraser;
import gd.TypeProjection;
import gd.a0;
import gd.c1;
import gd.g0;
import gd.n0;
import gd.o0;
import hd.KotlinTypeChecker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import kotlin.collections.s;
import ma.o;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import sd.v;
import ya.l;
import za.Lambda;
import za.k;

/* compiled from: RawType.kt */
/* loaded from: classes2.dex */
public final class h extends a0 implements n0 {

    /* compiled from: RawType.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements l<String, CharSequence> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f10925e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CharSequence invoke(String str) {
            k.e(str, "it");
            return "(raw) " + str;
        }
    }

    private h(o0 o0Var, o0 o0Var2, boolean z10) {
        super(o0Var, o0Var2);
        if (z10) {
            return;
        }
        KotlinTypeChecker.f12213a.b(o0Var, o0Var2);
    }

    private static final boolean j1(String str, String str2) {
        String j02;
        j02 = v.j0(str2, "out ");
        return k.a(str, j02) || k.a(str2, "*");
    }

    private static final List<String> k1(rc.c cVar, g0 g0Var) {
        int u7;
        List<TypeProjection> U0 = g0Var.U0();
        u7 = s.u(U0, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = U0.iterator();
        while (it.hasNext()) {
            arrayList.add(cVar.x((TypeProjection) it.next()));
        }
        return arrayList;
    }

    private static final String l1(String str, String str2) {
        boolean H;
        String G0;
        String D0;
        H = v.H(str, '<', false, 2, null);
        if (!H) {
            return str;
        }
        StringBuilder sb2 = new StringBuilder();
        G0 = v.G0(str, '<', null, 2, null);
        sb2.append(G0);
        sb2.append('<');
        sb2.append(str2);
        sb2.append('>');
        D0 = v.D0(str, '>', null, 2, null);
        sb2.append(D0);
        return sb2.toString();
    }

    @Override // gd.a0
    public o0 d1() {
        return e1();
    }

    @Override // gd.a0
    public String g1(rc.c cVar, rc.f fVar) {
        String c02;
        List G0;
        k.e(cVar, "renderer");
        k.e(fVar, "options");
        String w10 = cVar.w(e1());
        String w11 = cVar.w(f1());
        if (fVar.m()) {
            return "raw (" + w10 + ".." + w11 + ')';
        }
        if (f1().U0().isEmpty()) {
            return cVar.t(w10, w11, ld.a.i(this));
        }
        List<String> k12 = k1(cVar, e1());
        List<String> k13 = k1(cVar, f1());
        c02 = _Collections.c0(k12, ", ", null, null, 0, null, a.f10925e, 30, null);
        G0 = _Collections.G0(k12, k13);
        boolean z10 = true;
        if (!(G0 instanceof Collection) || !G0.isEmpty()) {
            Iterator it = G0.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                o oVar = (o) it.next();
                if (!j1((String) oVar.c(), (String) oVar.d())) {
                    z10 = false;
                    break;
                }
            }
        }
        if (z10) {
            w11 = l1(w11, c02);
        }
        String l12 = l1(w10, c02);
        return k.a(l12, w11) ? l12 : cVar.t(l12, w11, ld.a.i(this));
    }

    @Override // gd.v1
    /* renamed from: h1, reason: merged with bridge method [inline-methods] */
    public h a1(boolean z10) {
        return new h(e1().a1(z10), f1().a1(z10));
    }

    @Override // gd.v1
    /* renamed from: i1, reason: merged with bridge method [inline-methods] */
    public a0 g1(hd.g gVar) {
        k.e(gVar, "kotlinTypeRefiner");
        g0 a10 = gVar.a(e1());
        k.c(a10, "null cannot be cast to non-null type org.jetbrains.kotlin.types.SimpleType");
        g0 a11 = gVar.a(f1());
        k.c(a11, "null cannot be cast to non-null type org.jetbrains.kotlin.types.SimpleType");
        return new h((o0) a10, (o0) a11, true);
    }

    @Override // gd.v1
    /* renamed from: m1, reason: merged with bridge method [inline-methods] */
    public h c1(c1 c1Var) {
        k.e(c1Var, "newAttributes");
        return new h(e1().c1(c1Var), f1().c1(c1Var));
    }

    @Override // gd.a0, gd.g0
    public zc.h u() {
        ClassifierDescriptor v7 = W0().v();
        TypeParameterUpperBoundEraser typeParameterUpperBoundEraser = null;
        byte b10 = 0;
        ClassDescriptor classDescriptor = v7 instanceof ClassDescriptor ? (ClassDescriptor) v7 : null;
        if (classDescriptor != null) {
            zc.h I0 = classDescriptor.I0(new RawSubstitution(typeParameterUpperBoundEraser, 1, b10 == true ? 1 : 0));
            k.d(I0, "classDescriptor.getMemberScope(RawSubstitution())");
            return I0;
        }
        throw new IllegalStateException(("Incorrect classifier: " + W0().v()).toString());
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public h(o0 o0Var, o0 o0Var2) {
        this(o0Var, o0Var2, false);
        k.e(o0Var, "lowerBound");
        k.e(o0Var2, "upperBound");
    }
}
