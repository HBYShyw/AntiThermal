package kotlin.collections;

import fb._Ranges;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.Set;
import rd.Sequence;
import sd.Appendable;
import za.Lambda;

/* compiled from: _Collections.kt */
/* renamed from: kotlin.collections.z */
/* loaded from: classes2.dex */
public class _Collections extends _CollectionsJvm {

    /* compiled from: Sequences.kt */
    /* renamed from: kotlin.collections.z$a */
    /* loaded from: classes2.dex */
    public static final class a<T> implements Sequence<T> {

        /* renamed from: a */
        final /* synthetic */ Iterable f14339a;

        public a(Iterable iterable) {
            this.f14339a = iterable;
        }

        @Override // rd.Sequence
        public Iterator<T> iterator() {
            return this.f14339a.iterator();
        }
    }

    /* compiled from: _Collections.kt */
    /* renamed from: kotlin.collections.z$b */
    /* loaded from: classes2.dex */
    public static final class b<T> extends Lambda implements ya.a<Iterator<? extends T>> {

        /* renamed from: e */
        final /* synthetic */ Iterable<T> f14340e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        b(Iterable<? extends T> iterable) {
            super(0);
            this.f14340e = iterable;
        }

        @Override // ya.a
        /* renamed from: a */
        public final Iterator<T> invoke() {
            return this.f14340e.iterator();
        }
    }

    public static final <T> List<T> A0(Iterable<? extends T> iterable) {
        List<T> B0;
        za.k.e(iterable, "<this>");
        if (iterable instanceof Collection) {
            B0 = B0((Collection) iterable);
            return B0;
        }
        return (List) x0(iterable, new ArrayList());
    }

    public static <T> List<T> B0(Collection<? extends T> collection) {
        za.k.e(collection, "<this>");
        return new ArrayList(collection);
    }

    public static <T> Set<T> C0(Iterable<? extends T> iterable) {
        za.k.e(iterable, "<this>");
        return iterable instanceof Collection ? new LinkedHashSet((Collection) iterable) : (Set) x0(iterable, new LinkedHashSet());
    }

    public static <T> Set<T> D0(Iterable<? extends T> iterable) {
        Set<T> e10;
        Set<T> d10;
        int e11;
        za.k.e(iterable, "<this>");
        if (iterable instanceof Collection) {
            Collection collection = (Collection) iterable;
            int size = collection.size();
            if (size == 0) {
                e10 = s0.e();
                return e10;
            }
            if (size != 1) {
                e11 = MapsJVM.e(collection.size());
                return (Set) x0(iterable, new LinkedHashSet(e11));
            }
            d10 = SetsJVM.d(iterable instanceof List ? ((List) iterable).get(0) : iterable.iterator().next());
            return d10;
        }
        return s0.g((Set) x0(iterable, new LinkedHashSet()));
    }

    public static <T> Set<T> E0(Iterable<? extends T> iterable, Iterable<? extends T> iterable2) {
        Set<T> C0;
        za.k.e(iterable, "<this>");
        za.k.e(iterable2, "other");
        C0 = C0(iterable);
        MutableCollections.z(C0, iterable2);
        return C0;
    }

    public static <T> Iterable<IndexedValue<T>> F0(Iterable<? extends T> iterable) {
        za.k.e(iterable, "<this>");
        return new f0(new b(iterable));
    }

    public static <T, R> List<ma.o<T, R>> G0(Iterable<? extends T> iterable, Iterable<? extends R> iterable2) {
        int u7;
        int u10;
        za.k.e(iterable, "<this>");
        za.k.e(iterable2, "other");
        Iterator<? extends T> it = iterable.iterator();
        Iterator<? extends R> it2 = iterable2.iterator();
        u7 = s.u(iterable, 10);
        u10 = s.u(iterable2, 10);
        ArrayList arrayList = new ArrayList(Math.min(u7, u10));
        while (it.hasNext() && it2.hasNext()) {
            arrayList.add(ma.u.a(it.next(), it2.next()));
        }
        return arrayList;
    }

    public static <T> boolean J(Iterable<? extends T> iterable, ya.l<? super T, Boolean> lVar) {
        za.k.e(iterable, "<this>");
        za.k.e(lVar, "predicate");
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return true;
        }
        Iterator<? extends T> it = iterable.iterator();
        while (it.hasNext()) {
            if (!lVar.invoke(it.next()).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static <T> Sequence<T> K(Iterable<? extends T> iterable) {
        za.k.e(iterable, "<this>");
        return new a(iterable);
    }

    public static <T> boolean L(Iterable<? extends T> iterable, T t7) {
        za.k.e(iterable, "<this>");
        if (iterable instanceof Collection) {
            return ((Collection) iterable).contains(t7);
        }
        return X(iterable, t7) >= 0;
    }

    public static <T> List<T> M(Iterable<? extends T> iterable) {
        Set C0;
        List<T> z02;
        za.k.e(iterable, "<this>");
        C0 = C0(iterable);
        z02 = z0(C0);
        return z02;
    }

    public static <T> List<T> N(Iterable<? extends T> iterable, int i10) {
        ArrayList arrayList;
        List<T> q10;
        Object d02;
        List<T> e10;
        List<T> j10;
        List<T> z02;
        za.k.e(iterable, "<this>");
        int i11 = 0;
        if (!(i10 >= 0)) {
            throw new IllegalArgumentException(("Requested element count " + i10 + " is less than zero.").toString());
        }
        if (i10 == 0) {
            z02 = z0(iterable);
            return z02;
        }
        if (iterable instanceof Collection) {
            Collection collection = (Collection) iterable;
            int size = collection.size() - i10;
            if (size <= 0) {
                j10 = r.j();
                return j10;
            }
            if (size == 1) {
                d02 = d0(iterable);
                e10 = CollectionsJVM.e(d02);
                return e10;
            }
            arrayList = new ArrayList(size);
            if (iterable instanceof List) {
                if (iterable instanceof RandomAccess) {
                    int size2 = collection.size();
                    while (i10 < size2) {
                        arrayList.add(((List) iterable).get(i10));
                        i10++;
                    }
                } else {
                    ListIterator listIterator = ((List) iterable).listIterator(i10);
                    while (listIterator.hasNext()) {
                        arrayList.add(listIterator.next());
                    }
                }
                return arrayList;
            }
        } else {
            arrayList = new ArrayList();
        }
        for (T t7 : iterable) {
            if (i11 >= i10) {
                arrayList.add(t7);
            } else {
                i11++;
            }
        }
        q10 = r.q(arrayList);
        return q10;
    }

    public static <T> List<T> O(List<? extends T> list, int i10) {
        int c10;
        za.k.e(list, "<this>");
        if (i10 >= 0) {
            c10 = _Ranges.c(list.size() - i10, 0);
            return v0(list, c10);
        }
        throw new IllegalArgumentException(("Requested element count " + i10 + " is less than zero.").toString());
    }

    public static <T> List<T> P(Iterable<? extends T> iterable, ya.l<? super T, Boolean> lVar) {
        za.k.e(iterable, "<this>");
        za.k.e(lVar, "predicate");
        ArrayList arrayList = new ArrayList();
        for (T t7 : iterable) {
            if (lVar.invoke(t7).booleanValue()) {
                arrayList.add(t7);
            }
        }
        return arrayList;
    }

    public static <T> List<T> Q(Iterable<? extends T> iterable) {
        za.k.e(iterable, "<this>");
        return (List) R(iterable, new ArrayList());
    }

    public static final <C extends Collection<? super T>, T> C R(Iterable<? extends T> iterable, C c10) {
        za.k.e(iterable, "<this>");
        za.k.e(c10, "destination");
        for (T t7 : iterable) {
            if (t7 != null) {
                c10.add(t7);
            }
        }
        return c10;
    }

    public static <T> T S(Iterable<? extends T> iterable) {
        Object T;
        za.k.e(iterable, "<this>");
        if (iterable instanceof List) {
            T = T((List) iterable);
            return (T) T;
        }
        Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        throw new NoSuchElementException("Collection is empty.");
    }

    public static <T> T T(List<? extends T> list) {
        za.k.e(list, "<this>");
        if (!list.isEmpty()) {
            return list.get(0);
        }
        throw new NoSuchElementException("List is empty.");
    }

    public static <T> T U(Iterable<? extends T> iterable) {
        za.k.e(iterable, "<this>");
        if (iterable instanceof List) {
            List list = (List) iterable;
            if (list.isEmpty()) {
                return null;
            }
            return (T) list.get(0);
        }
        Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    public static <T> T V(List<? extends T> list) {
        za.k.e(list, "<this>");
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static <T> T W(List<? extends T> list, int i10) {
        int l10;
        za.k.e(list, "<this>");
        if (i10 >= 0) {
            l10 = r.l(list);
            if (i10 <= l10) {
                return list.get(i10);
            }
        }
        return null;
    }

    public static final <T> int X(Iterable<? extends T> iterable, T t7) {
        za.k.e(iterable, "<this>");
        if (iterable instanceof List) {
            return ((List) iterable).indexOf(t7);
        }
        int i10 = 0;
        for (T t10 : iterable) {
            if (i10 < 0) {
                r.t();
            }
            if (za.k.a(t7, t10)) {
                return i10;
            }
            i10++;
        }
        return -1;
    }

    public static <T> Set<T> Y(Iterable<? extends T> iterable, Iterable<? extends T> iterable2) {
        Set<T> C0;
        za.k.e(iterable, "<this>");
        za.k.e(iterable2, "other");
        C0 = C0(iterable);
        MutableCollections.C(C0, iterable2);
        return C0;
    }

    public static final <T, A extends Appendable> A Z(Iterable<? extends T> iterable, A a10, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i10, CharSequence charSequence4, ya.l<? super T, ? extends CharSequence> lVar) {
        za.k.e(iterable, "<this>");
        za.k.e(a10, "buffer");
        za.k.e(charSequence, "separator");
        za.k.e(charSequence2, "prefix");
        za.k.e(charSequence3, "postfix");
        za.k.e(charSequence4, "truncated");
        a10.append(charSequence2);
        int i11 = 0;
        for (T t7 : iterable) {
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

    public static /* synthetic */ Appendable a0(Iterable iterable, Appendable appendable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i10, CharSequence charSequence4, ya.l lVar, int i11, Object obj) {
        return Z(iterable, appendable, (i11 & 2) != 0 ? ", " : charSequence, (i11 & 4) != 0 ? "" : charSequence2, (i11 & 8) == 0 ? charSequence3 : "", (i11 & 16) != 0 ? -1 : i10, (i11 & 32) != 0 ? "..." : charSequence4, (i11 & 64) != 0 ? null : lVar);
    }

    public static final <T> String b0(Iterable<? extends T> iterable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i10, CharSequence charSequence4, ya.l<? super T, ? extends CharSequence> lVar) {
        za.k.e(iterable, "<this>");
        za.k.e(charSequence, "separator");
        za.k.e(charSequence2, "prefix");
        za.k.e(charSequence3, "postfix");
        za.k.e(charSequence4, "truncated");
        String sb2 = ((StringBuilder) Z(iterable, new StringBuilder(), charSequence, charSequence2, charSequence3, i10, charSequence4, lVar)).toString();
        za.k.d(sb2, "joinTo(StringBuilder(), …ed, transform).toString()");
        return sb2;
    }

    public static /* synthetic */ String c0(Iterable iterable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i10, CharSequence charSequence4, ya.l lVar, int i11, Object obj) {
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
        return b0(iterable, charSequence, charSequence5, charSequence6, i12, charSequence7, lVar);
    }

    public static <T> T d0(Iterable<? extends T> iterable) {
        Object e02;
        za.k.e(iterable, "<this>");
        if (iterable instanceof List) {
            e02 = e0((List) iterable);
            return (T) e02;
        }
        Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            T next = it.next();
            while (it.hasNext()) {
                next = it.next();
            }
            return next;
        }
        throw new NoSuchElementException("Collection is empty.");
    }

    public static <T> T e0(List<? extends T> list) {
        int l10;
        za.k.e(list, "<this>");
        if (!list.isEmpty()) {
            l10 = r.l(list);
            return list.get(l10);
        }
        throw new NoSuchElementException("List is empty.");
    }

    public static <T> T f0(Iterable<? extends T> iterable) {
        za.k.e(iterable, "<this>");
        if (iterable instanceof List) {
            List list = (List) iterable;
            if (list.isEmpty()) {
                return null;
            }
            return (T) list.get(list.size() - 1);
        }
        Iterator<? extends T> it = iterable.iterator();
        if (!it.hasNext()) {
            return null;
        }
        T next = it.next();
        while (it.hasNext()) {
            next = it.next();
        }
        return next;
    }

    public static <T> T g0(List<? extends T> list) {
        za.k.e(list, "<this>");
        if (list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    public static <T, R> List<R> h0(Iterable<? extends T> iterable, ya.l<? super T, ? extends R> lVar) {
        int u7;
        za.k.e(iterable, "<this>");
        za.k.e(lVar, "transform");
        u7 = s.u(iterable, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<? extends T> it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add(lVar.invoke(it.next()));
        }
        return arrayList;
    }

    public static <T extends Comparable<? super T>> T i0(Iterable<? extends T> iterable) {
        za.k.e(iterable, "<this>");
        Iterator<? extends T> it = iterable.iterator();
        if (!it.hasNext()) {
            return null;
        }
        T next = it.next();
        while (it.hasNext()) {
            T next2 = it.next();
            if (next.compareTo(next2) > 0) {
                next = next2;
            }
        }
        return next;
    }

    public static <T> List<T> j0(Iterable<? extends T> iterable, T t7) {
        int u7;
        za.k.e(iterable, "<this>");
        u7 = s.u(iterable, 10);
        ArrayList arrayList = new ArrayList(u7);
        boolean z10 = false;
        for (T t10 : iterable) {
            boolean z11 = true;
            if (!z10 && za.k.a(t10, t7)) {
                z10 = true;
                z11 = false;
            }
            if (z11) {
                arrayList.add(t10);
            }
        }
        return arrayList;
    }

    public static <T> List<T> k0(Iterable<? extends T> iterable, Iterable<? extends T> iterable2) {
        List<T> m02;
        za.k.e(iterable, "<this>");
        za.k.e(iterable2, "elements");
        if (iterable instanceof Collection) {
            m02 = m0((Collection) iterable, iterable2);
            return m02;
        }
        ArrayList arrayList = new ArrayList();
        MutableCollections.z(arrayList, iterable);
        MutableCollections.z(arrayList, iterable2);
        return arrayList;
    }

    public static <T> List<T> l0(Iterable<? extends T> iterable, T t7) {
        List<T> n02;
        za.k.e(iterable, "<this>");
        if (iterable instanceof Collection) {
            n02 = n0((Collection) iterable, t7);
            return n02;
        }
        ArrayList arrayList = new ArrayList();
        MutableCollections.z(arrayList, iterable);
        arrayList.add(t7);
        return arrayList;
    }

    public static <T> List<T> m0(Collection<? extends T> collection, Iterable<? extends T> iterable) {
        za.k.e(collection, "<this>");
        za.k.e(iterable, "elements");
        if (iterable instanceof Collection) {
            Collection collection2 = (Collection) iterable;
            ArrayList arrayList = new ArrayList(collection.size() + collection2.size());
            arrayList.addAll(collection);
            arrayList.addAll(collection2);
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList(collection);
        MutableCollections.z(arrayList2, iterable);
        return arrayList2;
    }

    public static <T> List<T> n0(Collection<? extends T> collection, T t7) {
        za.k.e(collection, "<this>");
        ArrayList arrayList = new ArrayList(collection.size() + 1);
        arrayList.addAll(collection);
        arrayList.add(t7);
        return arrayList;
    }

    public static <T> List<T> o0(Iterable<? extends T> iterable) {
        List<T> z02;
        za.k.e(iterable, "<this>");
        if ((iterable instanceof Collection) && ((Collection) iterable).size() <= 1) {
            z02 = z0(iterable);
            return z02;
        }
        List<T> A0 = A0(iterable);
        _CollectionsJvm.I(A0);
        return A0;
    }

    public static <T> T p0(Iterable<? extends T> iterable) {
        Object q02;
        za.k.e(iterable, "<this>");
        if (iterable instanceof List) {
            q02 = q0((List) iterable);
            return (T) q02;
        }
        Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            T next = it.next();
            if (it.hasNext()) {
                throw new IllegalArgumentException("Collection has more than one element.");
            }
            return next;
        }
        throw new NoSuchElementException("Collection is empty.");
    }

    public static <T> T q0(List<? extends T> list) {
        za.k.e(list, "<this>");
        int size = list.size();
        if (size == 0) {
            throw new NoSuchElementException("List is empty.");
        }
        if (size == 1) {
            return list.get(0);
        }
        throw new IllegalArgumentException("List has more than one element.");
    }

    public static <T> T r0(Iterable<? extends T> iterable) {
        za.k.e(iterable, "<this>");
        if (iterable instanceof List) {
            List list = (List) iterable;
            if (list.size() == 1) {
                return (T) list.get(0);
            }
            return null;
        }
        Iterator<? extends T> it = iterable.iterator();
        if (!it.hasNext()) {
            return null;
        }
        T next = it.next();
        if (it.hasNext()) {
            return null;
        }
        return next;
    }

    public static <T> T s0(List<? extends T> list) {
        za.k.e(list, "<this>");
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    public static <T extends Comparable<? super T>> List<T> t0(Iterable<? extends T> iterable) {
        List<T> e10;
        List<T> z02;
        za.k.e(iterable, "<this>");
        if (iterable instanceof Collection) {
            Collection collection = (Collection) iterable;
            if (collection.size() <= 1) {
                z02 = z0(iterable);
                return z02;
            }
            Object[] array = collection.toArray(new Comparable[0]);
            _ArraysJvm.o((Comparable[]) array);
            e10 = _ArraysJvm.e(array);
            return e10;
        }
        List<T> A0 = A0(iterable);
        MutableCollectionsJVM.x(A0);
        return A0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> List<T> u0(Iterable<? extends T> iterable, Comparator<? super T> comparator) {
        List<T> e10;
        List<T> z02;
        za.k.e(iterable, "<this>");
        za.k.e(comparator, "comparator");
        if (iterable instanceof Collection) {
            Collection collection = (Collection) iterable;
            if (collection.size() <= 1) {
                z02 = z0(iterable);
                return z02;
            }
            Object[] array = collection.toArray(new Object[0]);
            _ArraysJvm.p(array, comparator);
            e10 = _ArraysJvm.e(array);
            return e10;
        }
        List<T> A0 = A0(iterable);
        MutableCollectionsJVM.y(A0, comparator);
        return A0;
    }

    public static final <T> List<T> v0(Iterable<? extends T> iterable, int i10) {
        List<T> q10;
        Object S;
        List<T> e10;
        List<T> z02;
        List<T> j10;
        za.k.e(iterable, "<this>");
        int i11 = 0;
        if (!(i10 >= 0)) {
            throw new IllegalArgumentException(("Requested element count " + i10 + " is less than zero.").toString());
        }
        if (i10 == 0) {
            j10 = r.j();
            return j10;
        }
        if (iterable instanceof Collection) {
            if (i10 >= ((Collection) iterable).size()) {
                z02 = z0(iterable);
                return z02;
            }
            if (i10 == 1) {
                S = S(iterable);
                e10 = CollectionsJVM.e(S);
                return e10;
            }
        }
        ArrayList arrayList = new ArrayList(i10);
        Iterator<? extends T> it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
            i11++;
            if (i11 == i10) {
                break;
            }
        }
        q10 = r.q(arrayList);
        return q10;
    }

    public static <T> List<T> w0(List<? extends T> list, int i10) {
        Object e02;
        List<T> e10;
        List<T> z02;
        List<T> j10;
        za.k.e(list, "<this>");
        if (!(i10 >= 0)) {
            throw new IllegalArgumentException(("Requested element count " + i10 + " is less than zero.").toString());
        }
        if (i10 == 0) {
            j10 = r.j();
            return j10;
        }
        int size = list.size();
        if (i10 >= size) {
            z02 = z0(list);
            return z02;
        }
        if (i10 == 1) {
            e02 = e0(list);
            e10 = CollectionsJVM.e(e02);
            return e10;
        }
        ArrayList arrayList = new ArrayList(i10);
        if (list instanceof RandomAccess) {
            for (int i11 = size - i10; i11 < size; i11++) {
                arrayList.add(list.get(i11));
            }
        } else {
            ListIterator<? extends T> listIterator = list.listIterator(size - i10);
            while (listIterator.hasNext()) {
                arrayList.add(listIterator.next());
            }
        }
        return arrayList;
    }

    public static final <T, C extends Collection<? super T>> C x0(Iterable<? extends T> iterable, C c10) {
        za.k.e(iterable, "<this>");
        za.k.e(c10, "destination");
        Iterator<? extends T> it = iterable.iterator();
        while (it.hasNext()) {
            c10.add(it.next());
        }
        return c10;
    }

    public static int[] y0(Collection<Integer> collection) {
        za.k.e(collection, "<this>");
        int[] iArr = new int[collection.size()];
        Iterator<Integer> it = collection.iterator();
        int i10 = 0;
        while (it.hasNext()) {
            iArr[i10] = it.next().intValue();
            i10++;
        }
        return iArr;
    }

    public static <T> List<T> z0(Iterable<? extends T> iterable) {
        List<T> q10;
        List<T> j10;
        List<T> e10;
        List<T> B0;
        za.k.e(iterable, "<this>");
        if (iterable instanceof Collection) {
            Collection collection = (Collection) iterable;
            int size = collection.size();
            if (size == 0) {
                j10 = r.j();
                return j10;
            }
            if (size != 1) {
                B0 = B0(collection);
                return B0;
            }
            e10 = CollectionsJVM.e(iterable instanceof List ? ((List) iterable).get(0) : iterable.iterator().next());
            return e10;
        }
        q10 = r.q(A0(iterable));
        return q10;
    }
}
