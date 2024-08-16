package ma;

import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.collections._Arrays;
import za.CollectionToArray;

/* compiled from: UIntArray.kt */
/* renamed from: ma.y, reason: use source file name */
/* loaded from: classes2.dex */
public final class UIntArray implements Collection<UInt>, ab.a {

    /* renamed from: e, reason: collision with root package name */
    private final int[] f15204e;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: UIntArray.kt */
    /* renamed from: ma.y$a */
    /* loaded from: classes2.dex */
    public static final class a implements Iterator<UInt>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private final int[] f15205e;

        /* renamed from: f, reason: collision with root package name */
        private int f15206f;

        public a(int[] iArr) {
            za.k.e(iArr, ThermalWindowConfigInfo.TAG_ARRAY);
            this.f15205e = iArr;
        }

        public int b() {
            int i10 = this.f15206f;
            int[] iArr = this.f15205e;
            if (i10 >= iArr.length) {
                throw new NoSuchElementException(String.valueOf(this.f15206f));
            }
            this.f15206f = i10 + 1;
            return UInt.b(iArr[i10]);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f15206f < this.f15205e.length;
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ UInt next() {
            return UInt.a(b());
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    public static boolean e(int[] iArr, int i10) {
        boolean t7;
        t7 = _Arrays.t(iArr, i10);
        return t7;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0032 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:18:? A[LOOP:0: B:7:0x0013->B:18:?, LOOP_END, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean f(int[] iArr, Collection<UInt> collection) {
        boolean z10;
        boolean t7;
        za.k.e(collection, "elements");
        if (!collection.isEmpty()) {
            for (Object obj : collection) {
                if (obj instanceof UInt) {
                    t7 = _Arrays.t(iArr, ((UInt) obj).f());
                    if (t7) {
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

    public static boolean g(int[] iArr, Object obj) {
        return (obj instanceof UIntArray) && za.k.a(iArr, ((UIntArray) obj).o());
    }

    public static int i(int[] iArr) {
        return iArr.length;
    }

    public static int k(int[] iArr) {
        return Arrays.hashCode(iArr);
    }

    public static boolean l(int[] iArr) {
        return iArr.length == 0;
    }

    public static Iterator<UInt> m(int[] iArr) {
        return new a(iArr);
    }

    public static String n(int[] iArr) {
        return "UIntArray(storage=" + Arrays.toString(iArr) + ')';
    }

    @Override // java.util.Collection
    public /* bridge */ /* synthetic */ boolean add(UInt uInt) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends UInt> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof UInt) {
            return d(((UInt) obj).f());
        }
        return false;
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<? extends Object> collection) {
        za.k.e(collection, "elements");
        return f(this.f15204e, collection);
    }

    public boolean d(int i10) {
        return e(this.f15204e, i10);
    }

    @Override // java.util.Collection
    public boolean equals(Object obj) {
        return g(this.f15204e, obj);
    }

    @Override // java.util.Collection
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public int size() {
        return i(this.f15204e);
    }

    @Override // java.util.Collection
    public int hashCode() {
        return k(this.f15204e);
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return l(this.f15204e);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<UInt> iterator() {
        return m(this.f15204e);
    }

    public final /* synthetic */ int[] o() {
        return this.f15204e;
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
        return n(this.f15204e);
    }
}
