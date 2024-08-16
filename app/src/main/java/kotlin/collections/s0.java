package kotlin.collections;

import java.util.LinkedHashSet;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Sets.kt */
/* loaded from: classes2.dex */
public class s0 extends SetsJVM {
    public static <T> Set<T> e() {
        return d0.f14319e;
    }

    public static <T> LinkedHashSet<T> f(T... tArr) {
        int e10;
        za.k.e(tArr, "elements");
        e10 = MapsJVM.e(tArr.length);
        return (LinkedHashSet) _Arrays.Y(tArr, new LinkedHashSet(e10));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> Set<T> g(Set<? extends T> set) {
        Set<T> e10;
        Set<T> d10;
        za.k.e(set, "<this>");
        int size = set.size();
        if (size == 0) {
            e10 = e();
            return e10;
        }
        if (size != 1) {
            return set;
        }
        d10 = SetsJVM.d(set.iterator().next());
        return d10;
    }

    public static <T> Set<T> h(T... tArr) {
        Set<T> e10;
        Set<T> r02;
        za.k.e(tArr, "elements");
        if (tArr.length > 0) {
            r02 = _Arrays.r0(tArr);
            return r02;
        }
        e10 = e();
        return e10;
    }
}
