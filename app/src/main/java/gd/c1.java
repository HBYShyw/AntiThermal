package gd;

import gb.KClass;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Collections;
import nd.AttributeArrayOwner;
import qd.collections;
import za.DefaultConstructorMarker;

/* compiled from: TypeAttributes.kt */
/* loaded from: classes2.dex */
public final class c1 extends AttributeArrayOwner<a1<?>, a1<?>> {

    /* renamed from: f, reason: collision with root package name */
    public static final a f11749f = new a(null);

    /* renamed from: g, reason: collision with root package name */
    private static final c1 f11750g;

    /* compiled from: TypeAttributes.kt */
    /* loaded from: classes2.dex */
    public static final class a extends nd.s<a1<?>, a1<?>> {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // nd.s
        public <T extends a1<?>> int b(ConcurrentHashMap<KClass<? extends a1<?>>, Integer> concurrentHashMap, KClass<T> kClass, ya.l<? super KClass<? extends a1<?>>, Integer> lVar) {
            int intValue;
            za.k.e(concurrentHashMap, "<this>");
            za.k.e(kClass, "kClass");
            za.k.e(lVar, "compute");
            Integer num = concurrentHashMap.get(kClass);
            if (num != null) {
                return num.intValue();
            }
            synchronized (concurrentHashMap) {
                Integer num2 = concurrentHashMap.get(kClass);
                if (num2 == null) {
                    Integer invoke = lVar.invoke(kClass);
                    concurrentHashMap.putIfAbsent(kClass, Integer.valueOf(invoke.intValue()));
                    num2 = invoke;
                }
                za.k.d(num2, "this[kClass] ?: compute(…putIfAbsent(kClass, it) }");
                intValue = num2.intValue();
            }
            return intValue;
        }

        public final c1 g(List<? extends a1<?>> list) {
            za.k.e(list, "attributes");
            if (list.isEmpty()) {
                return h();
            }
            return new c1(list, null);
        }

        public final c1 h() {
            return c1.f11750g;
        }
    }

    static {
        List j10;
        j10 = kotlin.collections.r.j();
        f11750g = new c1((List<? extends a1<?>>) j10);
    }

    private c1(List<? extends a1<?>> list) {
        for (a1<?> a1Var : list) {
            f(a1Var.b(), a1Var);
        }
    }

    public /* synthetic */ c1(List list, DefaultConstructorMarker defaultConstructorMarker) {
        this((List<? extends a1<?>>) list);
    }

    @Override // nd.a
    protected nd.s<a1<?>, a1<?>> e() {
        return f11749f;
    }

    public final c1 h(c1 c1Var) {
        Object a10;
        za.k.e(c1Var, "other");
        if (isEmpty() && c1Var.isEmpty()) {
            return this;
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = f11749f.e().iterator();
        while (it.hasNext()) {
            int intValue = ((Number) it.next()).intValue();
            a1<?> a1Var = d().get(intValue);
            a1<?> a1Var2 = c1Var.d().get(intValue);
            if (a1Var == null) {
                a10 = a1Var2 != null ? a1Var2.a(a1Var) : null;
            } else {
                a10 = a1Var.a(a1Var2);
            }
            collections.a(arrayList, a10);
        }
        return f11749f.g(arrayList);
    }

    public final boolean i(a1<?> a1Var) {
        za.k.e(a1Var, "attribute");
        return d().get(f11749f.d(a1Var.b())) != null;
    }

    public final c1 k(c1 c1Var) {
        Object c10;
        za.k.e(c1Var, "other");
        if (isEmpty() && c1Var.isEmpty()) {
            return this;
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = f11749f.e().iterator();
        while (it.hasNext()) {
            int intValue = ((Number) it.next()).intValue();
            a1<?> a1Var = d().get(intValue);
            a1<?> a1Var2 = c1Var.d().get(intValue);
            if (a1Var == null) {
                c10 = a1Var2 != null ? a1Var2.c(a1Var) : null;
            } else {
                c10 = a1Var.c(a1Var2);
            }
            collections.a(arrayList, c10);
        }
        return f11749f.g(arrayList);
    }

    public final c1 l(a1<?> a1Var) {
        List z02;
        List<? extends a1<?>> n02;
        za.k.e(a1Var, "attribute");
        if (i(a1Var)) {
            return this;
        }
        if (isEmpty()) {
            return new c1(a1Var);
        }
        z02 = _Collections.z0(this);
        n02 = _Collections.n0(z02, a1Var);
        return f11749f.g(n02);
    }

    public final c1 m(a1<?> a1Var) {
        za.k.e(a1Var, "attribute");
        if (isEmpty()) {
            return this;
        }
        nd.c<a1<?>> d10 = d();
        ArrayList arrayList = new ArrayList();
        for (a1<?> a1Var2 : d10) {
            if (!za.k.a(a1Var2, a1Var)) {
                arrayList.add(a1Var2);
            }
        }
        return arrayList.size() == d().d() ? this : f11749f.g(arrayList);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private c1(a1<?> a1Var) {
        this((List<? extends a1<?>>) r1);
        List e10;
        e10 = CollectionsJVM.e(a1Var);
    }
}
