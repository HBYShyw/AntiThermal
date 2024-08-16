package kotlin.collections;

import fb.PrimitiveRanges;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* compiled from: Collections.kt */
/* loaded from: classes2.dex */
public class r extends CollectionsJVM {
    public static <T> ArrayList<T> f(T... tArr) {
        za.k.e(tArr, "elements");
        return tArr.length == 0 ? new ArrayList<>() : new ArrayList<>(new h(tArr, true));
    }

    public static final <T> Collection<T> g(T[] tArr) {
        za.k.e(tArr, "<this>");
        return new h(tArr, false);
    }

    public static final <T extends Comparable<? super T>> int h(List<? extends T> list, T t7, int i10, int i11) {
        int a10;
        za.k.e(list, "<this>");
        r(list.size(), i10, i11);
        int i12 = i11 - 1;
        while (i10 <= i12) {
            int i13 = (i10 + i12) >>> 1;
            a10 = pa.b.a(list.get(i13), t7);
            if (a10 < 0) {
                i10 = i13 + 1;
            } else {
                if (a10 <= 0) {
                    return i13;
                }
                i12 = i13 - 1;
            }
        }
        return -(i10 + 1);
    }

    public static /* synthetic */ int i(List list, Comparable comparable, int i10, int i11, int i12, Object obj) {
        if ((i12 & 2) != 0) {
            i10 = 0;
        }
        if ((i12 & 4) != 0) {
            i11 = list.size();
        }
        return h(list, comparable, i10, i11);
    }

    public static <T> List<T> j() {
        return b0.f14310e;
    }

    public static PrimitiveRanges k(Collection<?> collection) {
        za.k.e(collection, "<this>");
        return new PrimitiveRanges(0, collection.size() - 1);
    }

    public static <T> int l(List<? extends T> list) {
        za.k.e(list, "<this>");
        return list.size() - 1;
    }

    public static <T> List<T> m(T... tArr) {
        List<T> j10;
        List<T> e10;
        za.k.e(tArr, "elements");
        if (tArr.length > 0) {
            e10 = _ArraysJvm.e(tArr);
            return e10;
        }
        j10 = j();
        return j10;
    }

    public static <T> List<T> n(T t7) {
        List<T> j10;
        List<T> e10;
        if (t7 != null) {
            e10 = CollectionsJVM.e(t7);
            return e10;
        }
        j10 = j();
        return j10;
    }

    public static <T> List<T> o(T... tArr) {
        List<T> y4;
        za.k.e(tArr, "elements");
        y4 = _Arrays.y(tArr);
        return y4;
    }

    public static <T> List<T> p(T... tArr) {
        za.k.e(tArr, "elements");
        return tArr.length == 0 ? new ArrayList() : new ArrayList(new h(tArr, true));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> List<T> q(List<? extends T> list) {
        List<T> j10;
        List<T> e10;
        za.k.e(list, "<this>");
        int size = list.size();
        if (size == 0) {
            j10 = j();
            return j10;
        }
        if (size != 1) {
            return list;
        }
        e10 = CollectionsJVM.e(list.get(0));
        return e10;
    }

    private static final void r(int i10, int i11, int i12) {
        if (i11 > i12) {
            throw new IllegalArgumentException("fromIndex (" + i11 + ") is greater than toIndex (" + i12 + ").");
        }
        if (i11 < 0) {
            throw new IndexOutOfBoundsException("fromIndex (" + i11 + ") is less than zero.");
        }
        if (i12 <= i10) {
            return;
        }
        throw new IndexOutOfBoundsException("toIndex (" + i12 + ") is greater than size (" + i10 + ").");
    }

    public static void s() {
        throw new ArithmeticException("Count overflow has happened.");
    }

    public static void t() {
        throw new ArithmeticException("Index overflow has happened.");
    }
}
