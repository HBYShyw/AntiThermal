package kotlin.collections;

import fb._Ranges;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import rd.Sequence;
import sd.Appendable;
import za.Lambda;

/* compiled from: _Arrays.kt */
/* renamed from: kotlin.collections.n */
/* loaded from: classes2.dex */
public class _Arrays extends _ArraysJvm {

    /* compiled from: Iterables.kt */
    /* renamed from: kotlin.collections.n$a */
    /* loaded from: classes2.dex */
    public static final class a<T> implements Iterable<T>, ab.a {

        /* renamed from: e */
        final /* synthetic */ Object[] f14330e;

        public a(Object[] objArr) {
            this.f14330e = objArr;
        }

        @Override // java.lang.Iterable
        public Iterator<T> iterator() {
            return za.b.a(this.f14330e);
        }
    }

    /* compiled from: Sequences.kt */
    /* renamed from: kotlin.collections.n$b */
    /* loaded from: classes2.dex */
    public static final class b<T> implements Sequence<T> {

        /* renamed from: a */
        final /* synthetic */ Object[] f14331a;

        public b(Object[] objArr) {
            this.f14331a = objArr;
        }

        @Override // rd.Sequence
        public Iterator<T> iterator() {
            return za.b.a(this.f14331a);
        }
    }

    /* compiled from: _Arrays.kt */
    /* renamed from: kotlin.collections.n$c */
    /* loaded from: classes2.dex */
    public static final class c<T> extends Lambda implements ya.a<Iterator<? extends T>> {

        /* renamed from: e */
        final /* synthetic */ T[] f14332e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(T[] tArr) {
            super(0);
            this.f14332e = tArr;
        }

        @Override // ya.a
        /* renamed from: a */
        public final Iterator<T> invoke() {
            return za.b.a(this.f14332e);
        }
    }

    public static <T> T A(T[] tArr) {
        za.k.e(tArr, "<this>");
        if (!(tArr.length == 0)) {
            return tArr[0];
        }
        throw new NoSuchElementException("Array is empty.");
    }

    public static <T> T B(T[] tArr) {
        za.k.e(tArr, "<this>");
        if (tArr.length == 0) {
            return null;
        }
        return tArr[0];
    }

    public static int C(int[] iArr) {
        za.k.e(iArr, "<this>");
        return iArr.length - 1;
    }

    public static <T> int D(T[] tArr) {
        za.k.e(tArr, "<this>");
        return tArr.length - 1;
    }

    public static Integer E(int[] iArr, int i10) {
        int C;
        za.k.e(iArr, "<this>");
        if (i10 >= 0) {
            C = C(iArr);
            if (i10 <= C) {
                return Integer.valueOf(iArr[i10]);
            }
        }
        return null;
    }

    public static <T> T F(T[] tArr, int i10) {
        int D;
        za.k.e(tArr, "<this>");
        if (i10 >= 0) {
            D = D(tArr);
            if (i10 <= D) {
                return tArr[i10];
            }
        }
        return null;
    }

    public static final int G(byte[] bArr, byte b10) {
        za.k.e(bArr, "<this>");
        int length = bArr.length;
        for (int i10 = 0; i10 < length; i10++) {
            if (b10 == bArr[i10]) {
                return i10;
            }
        }
        return -1;
    }

    public static final int H(int[] iArr, int i10) {
        za.k.e(iArr, "<this>");
        int length = iArr.length;
        for (int i11 = 0; i11 < length; i11++) {
            if (i10 == iArr[i11]) {
                return i11;
            }
        }
        return -1;
    }

    public static final int I(long[] jArr, long j10) {
        za.k.e(jArr, "<this>");
        int length = jArr.length;
        for (int i10 = 0; i10 < length; i10++) {
            if (j10 == jArr[i10]) {
                return i10;
            }
        }
        return -1;
    }

    public static <T> int J(T[] tArr, T t7) {
        za.k.e(tArr, "<this>");
        int i10 = 0;
        if (t7 == null) {
            int length = tArr.length;
            while (i10 < length) {
                if (tArr[i10] == null) {
                    return i10;
                }
                i10++;
            }
            return -1;
        }
        int length2 = tArr.length;
        while (i10 < length2) {
            if (za.k.a(t7, tArr[i10])) {
                return i10;
            }
            i10++;
        }
        return -1;
    }

    public static final int K(short[] sArr, short s7) {
        za.k.e(sArr, "<this>");
        int length = sArr.length;
        for (int i10 = 0; i10 < length; i10++) {
            if (s7 == sArr[i10]) {
                return i10;
            }
        }
        return -1;
    }

    public static final <T, A extends Appendable> A L(T[] tArr, A a10, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i10, CharSequence charSequence4, ya.l<? super T, ? extends CharSequence> lVar) {
        za.k.e(tArr, "<this>");
        za.k.e(a10, "buffer");
        za.k.e(charSequence, "separator");
        za.k.e(charSequence2, "prefix");
        za.k.e(charSequence3, "postfix");
        za.k.e(charSequence4, "truncated");
        a10.append(charSequence2);
        int i11 = 0;
        for (T t7 : tArr) {
            i11++;
            if (i11 > 1) {
                a10.append(charSequence);
            }
            if (i10 >= 0 && i11 > i10) {
                break;
            }
            Appendable.a(a10, t7, lVar);
        }
        if (i10 >= 0 && i11 > i10) {
            a10.append(charSequence4);
        }
        a10.append(charSequence3);
        return a10;
    }

    public static /* synthetic */ Appendable M(Object[] objArr, Appendable appendable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i10, CharSequence charSequence4, ya.l lVar, int i11, Object obj) {
        return L(objArr, appendable, (i11 & 2) != 0 ? ", " : charSequence, (i11 & 4) != 0 ? "" : charSequence2, (i11 & 8) == 0 ? charSequence3 : "", (i11 & 16) != 0 ? -1 : i10, (i11 & 32) != 0 ? "..." : charSequence4, (i11 & 64) != 0 ? null : lVar);
    }

    public static final <T> String N(T[] tArr, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i10, CharSequence charSequence4, ya.l<? super T, ? extends CharSequence> lVar) {
        za.k.e(tArr, "<this>");
        za.k.e(charSequence, "separator");
        za.k.e(charSequence2, "prefix");
        za.k.e(charSequence3, "postfix");
        za.k.e(charSequence4, "truncated");
        String sb2 = ((StringBuilder) L(tArr, new StringBuilder(), charSequence, charSequence2, charSequence3, i10, charSequence4, lVar)).toString();
        za.k.d(sb2, "joinTo(StringBuilder(), …ed, transform).toString()");
        return sb2;
    }

    public static /* synthetic */ String O(Object[] objArr, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i10, CharSequence charSequence4, ya.l lVar, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            charSequence = ", ";
        }
        CharSequence charSequence5 = (i11 & 2) != 0 ? "" : charSequence2;
        CharSequence charSequence6 = (i11 & 4) == 0 ? charSequence3 : "";
        if ((i11 & 8) != 0) {
            i10 = -1;
        }
        int i12 = i10;
        if ((i11 & 16) != 0) {
            charSequence4 = "...";
        }
        CharSequence charSequence7 = charSequence4;
        if ((i11 & 32) != 0) {
            lVar = null;
        }
        return N(objArr, charSequence, charSequence5, charSequence6, i12, charSequence7, lVar);
    }

    public static <T> T P(T[] tArr) {
        int D;
        za.k.e(tArr, "<this>");
        if (!(tArr.length == 0)) {
            D = D(tArr);
            return tArr[D];
        }
        throw new NoSuchElementException("Array is empty.");
    }

    public static final int Q(int[] iArr, int i10) {
        za.k.e(iArr, "<this>");
        int length = iArr.length - 1;
        if (length >= 0) {
            while (true) {
                int i11 = length - 1;
                if (i10 == iArr[length]) {
                    return length;
                }
                if (i11 < 0) {
                    break;
                }
                length = i11;
            }
        }
        return -1;
    }

    public static <T, R> List<R> R(T[] tArr, ya.l<? super T, ? extends R> lVar) {
        za.k.e(tArr, "<this>");
        za.k.e(lVar, "transform");
        ArrayList arrayList = new ArrayList(tArr.length);
        for (T t7 : tArr) {
            arrayList.add(lVar.invoke(t7));
        }
        return arrayList;
    }

    public static char S(char[] cArr) {
        za.k.e(cArr, "<this>");
        int length = cArr.length;
        if (length == 0) {
            throw new NoSuchElementException("Array is empty.");
        }
        if (length == 1) {
            return cArr[0];
        }
        throw new IllegalArgumentException("Array has more than one element.");
    }

    public static <T> T T(T[] tArr) {
        za.k.e(tArr, "<this>");
        int length = tArr.length;
        if (length == 0) {
            throw new NoSuchElementException("Array is empty.");
        }
        if (length == 1) {
            return tArr[0];
        }
        throw new IllegalArgumentException("Array has more than one element.");
    }

    public static <T> T U(T[] tArr) {
        za.k.e(tArr, "<this>");
        if (tArr.length == 1) {
            return tArr[0];
        }
        return null;
    }

    public static final <T> T[] V(T[] tArr, Comparator<? super T> comparator) {
        za.k.e(tArr, "<this>");
        za.k.e(comparator, "comparator");
        if (tArr.length == 0) {
            return tArr;
        }
        T[] tArr2 = (T[]) Arrays.copyOf(tArr, tArr.length);
        za.k.d(tArr2, "copyOf(this, size)");
        _ArraysJvm.p(tArr2, comparator);
        return tArr2;
    }

    public static <T> List<T> W(T[] tArr, Comparator<? super T> comparator) {
        List<T> e10;
        za.k.e(tArr, "<this>");
        za.k.e(comparator, "comparator");
        e10 = _ArraysJvm.e(V(tArr, comparator));
        return e10;
    }

    public static final <T> List<T> X(T[] tArr, int i10) {
        List<T> e10;
        List<T> f02;
        List<T> j10;
        za.k.e(tArr, "<this>");
        if (!(i10 >= 0)) {
            throw new IllegalArgumentException(("Requested element count " + i10 + " is less than zero.").toString());
        }
        if (i10 == 0) {
            j10 = r.j();
            return j10;
        }
        int length = tArr.length;
        if (i10 >= length) {
            f02 = f0(tArr);
            return f02;
        }
        if (i10 == 1) {
            e10 = CollectionsJVM.e(tArr[length - 1]);
            return e10;
        }
        ArrayList arrayList = new ArrayList(i10);
        for (int i11 = length - i10; i11 < length; i11++) {
            arrayList.add(tArr[i11]);
        }
        return arrayList;
    }

    public static final <T, C extends Collection<? super T>> C Y(T[] tArr, C c10) {
        za.k.e(tArr, "<this>");
        za.k.e(c10, "destination");
        for (T t7 : tArr) {
            c10.add(t7);
        }
        return c10;
    }

    public static List<Byte> Z(byte[] bArr) {
        List<Byte> j10;
        List<Byte> e10;
        za.k.e(bArr, "<this>");
        int length = bArr.length;
        if (length == 0) {
            j10 = r.j();
            return j10;
        }
        if (length != 1) {
            return i0(bArr);
        }
        e10 = CollectionsJVM.e(Byte.valueOf(bArr[0]));
        return e10;
    }

    public static List<Character> a0(char[] cArr) {
        List<Character> j10;
        List<Character> e10;
        za.k.e(cArr, "<this>");
        int length = cArr.length;
        if (length == 0) {
            j10 = r.j();
            return j10;
        }
        if (length != 1) {
            return j0(cArr);
        }
        e10 = CollectionsJVM.e(Character.valueOf(cArr[0]));
        return e10;
    }

    public static List<Double> b0(double[] dArr) {
        List<Double> j10;
        List<Double> e10;
        za.k.e(dArr, "<this>");
        int length = dArr.length;
        if (length == 0) {
            j10 = r.j();
            return j10;
        }
        if (length != 1) {
            return k0(dArr);
        }
        e10 = CollectionsJVM.e(Double.valueOf(dArr[0]));
        return e10;
    }

    public static List<Float> c0(float[] fArr) {
        List<Float> j10;
        List<Float> e10;
        za.k.e(fArr, "<this>");
        int length = fArr.length;
        if (length == 0) {
            j10 = r.j();
            return j10;
        }
        if (length != 1) {
            return l0(fArr);
        }
        e10 = CollectionsJVM.e(Float.valueOf(fArr[0]));
        return e10;
    }

    public static List<Integer> d0(int[] iArr) {
        List<Integer> j10;
        List<Integer> e10;
        za.k.e(iArr, "<this>");
        int length = iArr.length;
        if (length == 0) {
            j10 = r.j();
            return j10;
        }
        if (length != 1) {
            return m0(iArr);
        }
        e10 = CollectionsJVM.e(Integer.valueOf(iArr[0]));
        return e10;
    }

    public static List<Long> e0(long[] jArr) {
        List<Long> j10;
        List<Long> e10;
        za.k.e(jArr, "<this>");
        int length = jArr.length;
        if (length == 0) {
            j10 = r.j();
            return j10;
        }
        if (length != 1) {
            return n0(jArr);
        }
        e10 = CollectionsJVM.e(Long.valueOf(jArr[0]));
        return e10;
    }

    public static <T> List<T> f0(T[] tArr) {
        List<T> j10;
        List<T> e10;
        List<T> o02;
        za.k.e(tArr, "<this>");
        int length = tArr.length;
        if (length == 0) {
            j10 = r.j();
            return j10;
        }
        if (length != 1) {
            o02 = o0(tArr);
            return o02;
        }
        e10 = CollectionsJVM.e(tArr[0]);
        return e10;
    }

    public static List<Short> g0(short[] sArr) {
        List<Short> j10;
        List<Short> e10;
        za.k.e(sArr, "<this>");
        int length = sArr.length;
        if (length == 0) {
            j10 = r.j();
            return j10;
        }
        if (length != 1) {
            return p0(sArr);
        }
        e10 = CollectionsJVM.e(Short.valueOf(sArr[0]));
        return e10;
    }

    public static List<Boolean> h0(boolean[] zArr) {
        List<Boolean> j10;
        List<Boolean> e10;
        za.k.e(zArr, "<this>");
        int length = zArr.length;
        if (length == 0) {
            j10 = r.j();
            return j10;
        }
        if (length != 1) {
            return q0(zArr);
        }
        e10 = CollectionsJVM.e(Boolean.valueOf(zArr[0]));
        return e10;
    }

    public static final List<Byte> i0(byte[] bArr) {
        za.k.e(bArr, "<this>");
        ArrayList arrayList = new ArrayList(bArr.length);
        for (byte b10 : bArr) {
            arrayList.add(Byte.valueOf(b10));
        }
        return arrayList;
    }

    public static final List<Character> j0(char[] cArr) {
        za.k.e(cArr, "<this>");
        ArrayList arrayList = new ArrayList(cArr.length);
        for (char c10 : cArr) {
            arrayList.add(Character.valueOf(c10));
        }
        return arrayList;
    }

    public static final List<Double> k0(double[] dArr) {
        za.k.e(dArr, "<this>");
        ArrayList arrayList = new ArrayList(dArr.length);
        for (double d10 : dArr) {
            arrayList.add(Double.valueOf(d10));
        }
        return arrayList;
    }

    public static final List<Float> l0(float[] fArr) {
        za.k.e(fArr, "<this>");
        ArrayList arrayList = new ArrayList(fArr.length);
        for (float f10 : fArr) {
            arrayList.add(Float.valueOf(f10));
        }
        return arrayList;
    }

    public static final List<Integer> m0(int[] iArr) {
        za.k.e(iArr, "<this>");
        ArrayList arrayList = new ArrayList(iArr.length);
        for (int i10 : iArr) {
            arrayList.add(Integer.valueOf(i10));
        }
        return arrayList;
    }

    public static final List<Long> n0(long[] jArr) {
        za.k.e(jArr, "<this>");
        ArrayList arrayList = new ArrayList(jArr.length);
        for (long j10 : jArr) {
            arrayList.add(Long.valueOf(j10));
        }
        return arrayList;
    }

    public static <T> List<T> o0(T[] tArr) {
        za.k.e(tArr, "<this>");
        return new ArrayList(r.g(tArr));
    }

    public static final List<Short> p0(short[] sArr) {
        za.k.e(sArr, "<this>");
        ArrayList arrayList = new ArrayList(sArr.length);
        for (short s7 : sArr) {
            arrayList.add(Short.valueOf(s7));
        }
        return arrayList;
    }

    public static <T> Iterable<T> q(T[] tArr) {
        List j10;
        za.k.e(tArr, "<this>");
        if (!(tArr.length == 0)) {
            return new a(tArr);
        }
        j10 = r.j();
        return j10;
    }

    public static final List<Boolean> q0(boolean[] zArr) {
        za.k.e(zArr, "<this>");
        ArrayList arrayList = new ArrayList(zArr.length);
        for (boolean z10 : zArr) {
            arrayList.add(Boolean.valueOf(z10));
        }
        return arrayList;
    }

    public static <T> Sequence<T> r(T[] tArr) {
        Sequence<T> c10;
        za.k.e(tArr, "<this>");
        if (!(tArr.length == 0)) {
            return new b(tArr);
        }
        c10 = rd.l.c();
        return c10;
    }

    public static <T> Set<T> r0(T[] tArr) {
        Set<T> e10;
        Set<T> d10;
        int e11;
        za.k.e(tArr, "<this>");
        int length = tArr.length;
        if (length == 0) {
            e10 = s0.e();
            return e10;
        }
        if (length != 1) {
            e11 = MapsJVM.e(tArr.length);
            return (Set) Y(tArr, new LinkedHashSet(e11));
        }
        d10 = SetsJVM.d(tArr[0]);
        return d10;
    }

    public static boolean s(byte[] bArr, byte b10) {
        za.k.e(bArr, "<this>");
        return G(bArr, b10) >= 0;
    }

    public static <T> Iterable<IndexedValue<T>> s0(T[] tArr) {
        za.k.e(tArr, "<this>");
        return new f0(new c(tArr));
    }

    public static boolean t(int[] iArr, int i10) {
        za.k.e(iArr, "<this>");
        return H(iArr, i10) >= 0;
    }

    public static <T, R> List<ma.o<T, R>> t0(T[] tArr, R[] rArr) {
        za.k.e(tArr, "<this>");
        za.k.e(rArr, "other");
        int min = Math.min(tArr.length, rArr.length);
        ArrayList arrayList = new ArrayList(min);
        for (int i10 = 0; i10 < min; i10++) {
            arrayList.add(ma.u.a(tArr[i10], rArr[i10]));
        }
        return arrayList;
    }

    public static boolean u(long[] jArr, long j10) {
        za.k.e(jArr, "<this>");
        return I(jArr, j10) >= 0;
    }

    public static <T> boolean v(T[] tArr, T t7) {
        int J;
        za.k.e(tArr, "<this>");
        J = J(tArr, t7);
        return J >= 0;
    }

    public static boolean w(short[] sArr, short s7) {
        za.k.e(sArr, "<this>");
        return K(sArr, s7) >= 0;
    }

    public static <T> List<T> x(T[] tArr, int i10) {
        int c10;
        za.k.e(tArr, "<this>");
        if (i10 >= 0) {
            c10 = _Ranges.c(tArr.length - i10, 0);
            return X(tArr, c10);
        }
        throw new IllegalArgumentException(("Requested element count " + i10 + " is less than zero.").toString());
    }

    public static <T> List<T> y(T[] tArr) {
        za.k.e(tArr, "<this>");
        return (List) z(tArr, new ArrayList());
    }

    public static final <C extends Collection<? super T>, T> C z(T[] tArr, C c10) {
        za.k.e(tArr, "<this>");
        za.k.e(c10, "destination");
        for (T t7 : tArr) {
            if (t7 != null) {
                c10.add(t7);
            }
        }
        return c10;
    }
}
