package kotlin.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

/* compiled from: _ArraysJvm.kt */
/* renamed from: kotlin.collections.m */
/* loaded from: classes2.dex */
public class _ArraysJvm extends Arrays {

    /* compiled from: _ArraysJvm.kt */
    /* renamed from: kotlin.collections.m$a */
    /* loaded from: classes2.dex */
    public static final class a extends AbstractList<Integer> implements RandomAccess {

        /* renamed from: f */
        final /* synthetic */ int[] f14329f;

        a(int[] iArr) {
            this.f14329f = iArr;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public final /* bridge */ boolean contains(Object obj) {
            if (obj instanceof Integer) {
                return e(((Number) obj).intValue());
            }
            return false;
        }

        @Override // kotlin.collections.AbstractCollection
        public int d() {
            return this.f14329f.length;
        }

        public boolean e(int i10) {
            boolean t7;
            t7 = _Arrays.t(this.f14329f, i10);
            return t7;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        /* renamed from: f */
        public Integer get(int i10) {
            return Integer.valueOf(this.f14329f[i10]);
        }

        public int g(int i10) {
            return _Arrays.H(this.f14329f, i10);
        }

        public int h(int i10) {
            return _Arrays.Q(this.f14329f, i10);
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int indexOf(Object obj) {
            if (obj instanceof Integer) {
                return g(((Number) obj).intValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.f14329f.length == 0;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int lastIndexOf(Object obj) {
            if (obj instanceof Integer) {
                return h(((Number) obj).intValue());
            }
            return -1;
        }
    }

    public static List<Integer> d(int[] iArr) {
        za.k.e(iArr, "<this>");
        return new a(iArr);
    }

    public static <T> List<T> e(T[] tArr) {
        za.k.e(tArr, "<this>");
        List<T> a10 = ArraysUtilJVM.a(tArr);
        za.k.d(a10, "asList(this)");
        return a10;
    }

    public static byte[] f(byte[] bArr, byte[] bArr2, int i10, int i11, int i12) {
        za.k.e(bArr, "<this>");
        za.k.e(bArr2, "destination");
        System.arraycopy(bArr, i11, bArr2, i10, i12 - i11);
        return bArr2;
    }

    public static <T> T[] g(T[] tArr, T[] tArr2, int i10, int i11, int i12) {
        za.k.e(tArr, "<this>");
        za.k.e(tArr2, "destination");
        System.arraycopy(tArr, i11, tArr2, i10, i12 - i11);
        return tArr2;
    }

    public static /* synthetic */ byte[] h(byte[] bArr, byte[] bArr2, int i10, int i11, int i12, int i13, Object obj) {
        byte[] f10;
        if ((i13 & 2) != 0) {
            i10 = 0;
        }
        if ((i13 & 4) != 0) {
            i11 = 0;
        }
        if ((i13 & 8) != 0) {
            i12 = bArr.length;
        }
        f10 = f(bArr, bArr2, i10, i11, i12);
        return f10;
    }

    public static /* synthetic */ Object[] i(Object[] objArr, Object[] objArr2, int i10, int i11, int i12, int i13, Object obj) {
        Object[] g6;
        if ((i13 & 2) != 0) {
            i10 = 0;
        }
        if ((i13 & 4) != 0) {
            i11 = 0;
        }
        if ((i13 & 8) != 0) {
            i12 = objArr.length;
        }
        g6 = g(objArr, objArr2, i10, i11, i12);
        return g6;
    }

    public static byte[] j(byte[] bArr, int i10, int i11) {
        za.k.e(bArr, "<this>");
        ArraysJVM.b(i11, bArr.length);
        byte[] copyOfRange = Arrays.copyOfRange(bArr, i10, i11);
        za.k.d(copyOfRange, "copyOfRange(this, fromIndex, toIndex)");
        return copyOfRange;
    }

    public static <T> T[] k(T[] tArr, int i10, int i11) {
        za.k.e(tArr, "<this>");
        ArraysJVM.b(i11, tArr.length);
        T[] tArr2 = (T[]) Arrays.copyOfRange(tArr, i10, i11);
        za.k.d(tArr2, "copyOfRange(this, fromIndex, toIndex)");
        return tArr2;
    }

    public static void l(int[] iArr, int i10, int i11, int i12) {
        za.k.e(iArr, "<this>");
        Arrays.fill(iArr, i11, i12, i10);
    }

    public static <T> void m(T[] tArr, T t7, int i10, int i11) {
        za.k.e(tArr, "<this>");
        Arrays.fill(tArr, i10, i11, t7);
    }

    public static /* synthetic */ void n(Object[] objArr, Object obj, int i10, int i11, int i12, Object obj2) {
        if ((i12 & 2) != 0) {
            i10 = 0;
        }
        if ((i12 & 4) != 0) {
            i11 = objArr.length;
        }
        m(objArr, obj, i10, i11);
    }

    public static final <T> void o(T[] tArr) {
        za.k.e(tArr, "<this>");
        if (tArr.length > 1) {
            Arrays.sort(tArr);
        }
    }

    public static final <T> void p(T[] tArr, Comparator<? super T> comparator) {
        za.k.e(tArr, "<this>");
        za.k.e(comparator, "comparator");
        if (tArr.length > 1) {
            Arrays.sort(tArr, comparator);
        }
    }
}
