package hd;

import gb.KDeclarationContainer;
import gd.IntersectionTypeConstructor;
import gd.c1;
import gd.d0;
import gd.g0;
import gd.o0;
import gd.s0;
import gd.v1;
import gd.w0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.collections._Collections;
import uc.IntegerLiteralTypeConstructor;
import za.DefaultConstructorMarker;
import za.FunctionReference;
import za.Lambda;
import za.Reflection;

/* compiled from: IntersectionType.kt */
/* loaded from: classes2.dex */
public final class w {

    /* renamed from: a, reason: collision with root package name */
    public static final w f12245a = new w();

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    /* compiled from: IntersectionType.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: e, reason: collision with root package name */
        public static final a f12246e = new c("START", 0);

        /* renamed from: f, reason: collision with root package name */
        public static final a f12247f = new C0049a("ACCEPT_NULL", 1);

        /* renamed from: g, reason: collision with root package name */
        public static final a f12248g = new d("UNKNOWN", 2);

        /* renamed from: h, reason: collision with root package name */
        public static final a f12249h = new b("NOT_NULL", 3);

        /* renamed from: i, reason: collision with root package name */
        private static final /* synthetic */ a[] f12250i = a();

        /* compiled from: IntersectionType.kt */
        /* renamed from: hd.w$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0049a extends a {
            C0049a(String str, int i10) {
                super(str, i10, null);
            }

            @Override // hd.w.a
            public a b(v1 v1Var) {
                za.k.e(v1Var, "nextType");
                return c(v1Var);
            }
        }

        /* compiled from: IntersectionType.kt */
        /* loaded from: classes2.dex */
        static final class b extends a {
            b(String str, int i10) {
                super(str, i10, null);
            }

            @Override // hd.w.a
            /* renamed from: d, reason: merged with bridge method [inline-methods] */
            public b b(v1 v1Var) {
                za.k.e(v1Var, "nextType");
                return this;
            }
        }

        /* compiled from: IntersectionType.kt */
        /* loaded from: classes2.dex */
        static final class c extends a {
            c(String str, int i10) {
                super(str, i10, null);
            }

            @Override // hd.w.a
            public a b(v1 v1Var) {
                za.k.e(v1Var, "nextType");
                return c(v1Var);
            }
        }

        /* compiled from: IntersectionType.kt */
        /* loaded from: classes2.dex */
        static final class d extends a {
            d(String str, int i10) {
                super(str, i10, null);
            }

            @Override // hd.w.a
            public a b(v1 v1Var) {
                za.k.e(v1Var, "nextType");
                a c10 = c(v1Var);
                return c10 == a.f12247f ? this : c10;
            }
        }

        private a(String str, int i10) {
        }

        public /* synthetic */ a(String str, int i10, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, i10);
        }

        private static final /* synthetic */ a[] a() {
            return new a[]{f12246e, f12247f, f12248g, f12249h};
        }

        public static a valueOf(String str) {
            return (a) Enum.valueOf(a.class, str);
        }

        public static a[] values() {
            return (a[]) f12250i.clone();
        }

        public abstract a b(v1 v1Var);

        protected final a c(v1 v1Var) {
            za.k.e(v1Var, "<this>");
            return v1Var.X0() ? f12247f : ((v1Var instanceof gd.p) && (((gd.p) v1Var).i1() instanceof w0)) ? f12249h : v1Var instanceof w0 ? f12248g : o.f12239a.a(v1Var) ? f12249h : f12248g;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: IntersectionType.kt */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.a<String> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Set<o0> f12251e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        b(Set<? extends o0> set) {
            super(0);
            this.f12251e = set;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final String invoke() {
            String c02;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("This collections cannot be empty! input types: ");
            c02 = _Collections.c0(this.f12251e, null, null, null, 0, null, null, 63, null);
            sb2.append(c02);
            return sb2.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: IntersectionType.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class c extends FunctionReference implements ya.p<g0, g0, Boolean> {
        c(Object obj) {
            super(2, obj);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(w.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "isStrictSupertype(Lorg/jetbrains/kotlin/types/KotlinType;Lorg/jetbrains/kotlin/types/KotlinType;)Z";
        }

        @Override // ya.p
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(g0 g0Var, g0 g0Var2) {
            za.k.e(g0Var, "p0");
            za.k.e(g0Var2, "p1");
            return Boolean.valueOf(((w) this.f20351f).e(g0Var, g0Var2));
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "isStrictSupertype";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: IntersectionType.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class d extends FunctionReference implements ya.p<g0, g0, Boolean> {
        d(Object obj) {
            super(2, obj);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(m.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "equalTypes(Lorg/jetbrains/kotlin/types/KotlinType;Lorg/jetbrains/kotlin/types/KotlinType;)Z";
        }

        @Override // ya.p
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(g0 g0Var, g0 g0Var2) {
            za.k.e(g0Var, "p0");
            za.k.e(g0Var2, "p1");
            return Boolean.valueOf(((m) this.f20351f).d(g0Var, g0Var2));
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "equalTypes";
        }
    }

    private w() {
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0051 A[EDGE_INSN: B:23:0x0051->B:7:0x0051 BREAK  A[LOOP:1: B:14:0x0028->B:24:?], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[LOOP:1: B:14:0x0028->B:24:?, LOOP_END, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final Collection<o0> b(Collection<? extends o0> collection, ya.p<? super o0, ? super o0, Boolean> pVar) {
        boolean z10;
        ArrayList<o0> arrayList = new ArrayList(collection);
        Iterator it = arrayList.iterator();
        za.k.d(it, "filteredTypes.iterator()");
        while (it.hasNext()) {
            o0 o0Var = (o0) it.next();
            boolean z11 = true;
            if (!arrayList.isEmpty()) {
                for (o0 o0Var2 : arrayList) {
                    if (o0Var2 != o0Var) {
                        za.k.d(o0Var2, "lower");
                        za.k.d(o0Var, "upper");
                        if (pVar.invoke(o0Var2, o0Var).booleanValue()) {
                            z10 = true;
                            if (!z10) {
                                break;
                            }
                        }
                    }
                    z10 = false;
                    if (!z10) {
                    }
                }
            }
            z11 = false;
            if (z11) {
                it.remove();
            }
        }
        return arrayList;
    }

    private final o0 d(Set<? extends o0> set) {
        Object p02;
        Object p03;
        if (set.size() == 1) {
            p03 = _Collections.p0(set);
            return (o0) p03;
        }
        new b(set);
        Collection<o0> b10 = b(set, new c(this));
        b10.isEmpty();
        o0 b11 = IntegerLiteralTypeConstructor.f18998f.b(b10);
        if (b11 != null) {
            return b11;
        }
        Collection<o0> b12 = b(b10, new d(l.f12233b.a()));
        b12.isEmpty();
        if (b12.size() >= 2) {
            return new IntersectionTypeConstructor(set).d();
        }
        p02 = _Collections.p0(b12);
        return (o0) p02;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean e(g0 g0Var, g0 g0Var2) {
        m a10 = l.f12233b.a();
        return a10.b(g0Var, g0Var2) && !a10.b(g0Var2, g0Var);
    }

    public final o0 c(List<? extends o0> list) {
        int u7;
        int u10;
        za.k.e(list, "types");
        list.size();
        ArrayList<o0> arrayList = new ArrayList();
        for (o0 o0Var : list) {
            if (o0Var.W0() instanceof IntersectionTypeConstructor) {
                Collection<g0> q10 = o0Var.W0().q();
                za.k.d(q10, "type.constructor.supertypes");
                u10 = kotlin.collections.s.u(q10, 10);
                ArrayList arrayList2 = new ArrayList(u10);
                for (g0 g0Var : q10) {
                    za.k.d(g0Var, "it");
                    o0 d10 = d0.d(g0Var);
                    if (o0Var.X0()) {
                        d10 = d10.a1(true);
                    }
                    arrayList2.add(d10);
                }
                arrayList.addAll(arrayList2);
            } else {
                arrayList.add(o0Var);
            }
        }
        a aVar = a.f12246e;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            aVar = aVar.b((v1) it.next());
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (o0 o0Var2 : arrayList) {
            if (aVar == a.f12249h) {
                if (o0Var2 instanceof i) {
                    o0Var2 = s0.k((i) o0Var2);
                }
                o0Var2 = s0.i(o0Var2, false, 1, null);
            }
            linkedHashSet.add(o0Var2);
        }
        u7 = kotlin.collections.s.u(list, 10);
        ArrayList arrayList3 = new ArrayList(u7);
        Iterator<T> it2 = list.iterator();
        while (it2.hasNext()) {
            arrayList3.add(((o0) it2.next()).V0());
        }
        Iterator it3 = arrayList3.iterator();
        if (it3.hasNext()) {
            Object next = it3.next();
            while (it3.hasNext()) {
                next = ((c1) next).k((c1) it3.next());
            }
            return d(linkedHashSet).c1((c1) next);
        }
        throw new UnsupportedOperationException("Empty collection can't be reduced.");
    }
}
