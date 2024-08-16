package ma;

import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.collections._Arrays;
import za.CollectionToArray;

/* compiled from: ULongArray.kt */
/* renamed from: ma.a0, reason: use source file name */
/* loaded from: classes2.dex */
public final class ULongArray implements Collection<ULong>, ab.a {

    /* renamed from: e, reason: collision with root package name */
    private final long[] f15157e;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ULongArray.kt */
    /* renamed from: ma.a0$a */
    /* loaded from: classes2.dex */
    public static final class a implements Iterator<ULong>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private final long[] f15158e;

        /* renamed from: f, reason: collision with root package name */
        private int f15159f;

        public a(long[] jArr) {
            za.k.e(jArr, ThermalWindowConfigInfo.TAG_ARRAY);
            this.f15158e = jArr;
        }

        public long b() {
            int i10 = this.f15159f;
            long[] jArr = this.f15158e;
            if (i10 >= jArr.length) {
                throw new NoSuchElementException(String.valueOf(this.f15159f));
            }
            this.f15159f = i10 + 1;
            return ULong.b(jArr[i10]);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f15159f < this.f15158e.length;
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ ULong next() {
            return ULong.a(b());
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    public static boolean e(long[] jArr, long j10) {
        boolean u7;
        u7 = _Arrays.u(jArr, j10);
        return u7;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0032 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:18:? A[LOOP:0: B:7:0x0013->B:18:?, LOOP_END, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean f(long[] jArr, Collection<ULong> collection) {
        boolean z10;
        boolean u7;
        za.k.e(collection, "elements");
        if (!collection.isEmpty()) {
            for (Object obj : collection) {
                if (obj instanceof ULong) {
                    u7 = _Arrays.u(jArr, ((ULong) obj).f());
                    if (u7) {
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

    public static boolean g(long[] jArr, Object obj) {
        return (obj instanceof ULongArray) && za.k.a(jArr, ((ULongArray) obj).o());
    }

    public static int i(long[] jArr) {
        return jArr.length;
    }

    public static int k(long[] jArr) {
        return Arrays.hashCode(jArr);
    }

    public static boolean l(long[] jArr) {
        return jArr.length == 0;
    }

    public static Iterator<ULong> m(long[] jArr) {
        return new a(jArr);
    }

    public static String n(long[] jArr) {
        return "ULongArray(storage=" + Arrays.toString(jArr) + ')';
    }

    @Override // java.util.Collection
    public /* bridge */ /* synthetic */ boolean add(ULong uLong) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends ULong> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof ULong) {
            return d(((ULong) obj).f());
        }
        return false;
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<? extends Object> collection) {
        za.k.e(collection, "elements");
        return f(this.f15157e, collection);
    }

    public boolean d(long j10) {
        return e(this.f15157e, j10);
    }

    @Override // java.util.Collection
    public boolean equals(Object obj) {
        return g(this.f15157e, obj);
    }

    @Override // java.util.Collection
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public int size() {
        return i(this.f15157e);
    }

    @Override // java.util.Collection
    public int hashCode() {
        return k(this.f15157e);
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return l(this.f15157e);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<ULong> iterator() {
        return m(this.f15157e);
    }

    public final /* synthetic */ long[] o() {
        return this.f15157e;
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
        return n(this.f15157e);
    }
}
