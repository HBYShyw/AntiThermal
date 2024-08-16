package ma;

import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.collections._Arrays;
import za.CollectionToArray;

/* compiled from: UShortArray.kt */
/* renamed from: ma.d0, reason: use source file name */
/* loaded from: classes2.dex */
public final class UShortArray implements Collection<UShort>, ab.a {

    /* renamed from: e, reason: collision with root package name */
    private final short[] f15164e;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: UShortArray.kt */
    /* renamed from: ma.d0$a */
    /* loaded from: classes2.dex */
    public static final class a implements Iterator<UShort>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private final short[] f15165e;

        /* renamed from: f, reason: collision with root package name */
        private int f15166f;

        public a(short[] sArr) {
            za.k.e(sArr, ThermalWindowConfigInfo.TAG_ARRAY);
            this.f15165e = sArr;
        }

        public short b() {
            int i10 = this.f15166f;
            short[] sArr = this.f15165e;
            if (i10 >= sArr.length) {
                throw new NoSuchElementException(String.valueOf(this.f15166f));
            }
            this.f15166f = i10 + 1;
            return UShort.b(sArr[i10]);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f15166f < this.f15165e.length;
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ UShort next() {
            return UShort.a(b());
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    public static boolean e(short[] sArr, short s7) {
        boolean w10;
        w10 = _Arrays.w(sArr, s7);
        return w10;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0032 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:18:? A[LOOP:0: B:7:0x0013->B:18:?, LOOP_END, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean f(short[] sArr, Collection<UShort> collection) {
        boolean z10;
        boolean w10;
        za.k.e(collection, "elements");
        if (!collection.isEmpty()) {
            for (Object obj : collection) {
                if (obj instanceof UShort) {
                    w10 = _Arrays.w(sArr, ((UShort) obj).f());
                    if (w10) {
                        z10 = true;
                        if (z10) {
                            return false;
                        }
                    }
                }
                z10 = false;
                if (z10) {
                }
            }
        }
        return true;
    }

    public static boolean g(short[] sArr, Object obj) {
        return (obj instanceof UShortArray) && za.k.a(sArr, ((UShortArray) obj).o());
    }

    public static int i(short[] sArr) {
        return sArr.length;
    }

    public static int k(short[] sArr) {
        return Arrays.hashCode(sArr);
    }

    public static boolean l(short[] sArr) {
        return sArr.length == 0;
    }

    public static Iterator<UShort> m(short[] sArr) {
        return new a(sArr);
    }

    public static String n(short[] sArr) {
        return "UShortArray(storage=" + Arrays.toString(sArr) + ')';
    }

    @Override // java.util.Collection
    public /* bridge */ /* synthetic */ boolean add(UShort uShort) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends UShort> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof UShort) {
            return d(((UShort) obj).f());
        }
        return false;
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<? extends Object> collection) {
        za.k.e(collection, "elements");
        return f(this.f15164e, collection);
    }

    public boolean d(short s7) {
        return e(this.f15164e, s7);
    }

    @Override // java.util.Collection
    public boolean equals(Object obj) {
        return g(this.f15164e, obj);
    }

    @Override // java.util.Collection
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public int size() {
        return i(this.f15164e);
    }

    @Override // java.util.Collection
    public int hashCode() {
        return k(this.f15164e);
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return l(this.f15164e);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<UShort> iterator() {
        return m(this.f15164e);
    }

    public final /* synthetic */ short[] o() {
        return this.f15164e;
    }

    @Override // java.util.Collection
    public boolean remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        return CollectionToArray.a(this);
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        za.k.e(tArr, ThermalWindowConfigInfo.TAG_ARRAY);
        return (T[]) CollectionToArray.b(this, tArr);
    }

    public String toString() {
        return n(this.f15164e);
    }
}
