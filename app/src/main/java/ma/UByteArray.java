package ma;

import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.collections._Arrays;
import za.CollectionToArray;

/* compiled from: UByteArray.kt */
/* renamed from: ma.w, reason: use source file name */
/* loaded from: classes2.dex */
public final class UByteArray implements Collection<UByte>, ab.a {

    /* renamed from: e, reason: collision with root package name */
    private final byte[] f15199e;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: UByteArray.kt */
    /* renamed from: ma.w$a */
    /* loaded from: classes2.dex */
    public static final class a implements Iterator<UByte>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private final byte[] f15200e;

        /* renamed from: f, reason: collision with root package name */
        private int f15201f;

        public a(byte[] bArr) {
            za.k.e(bArr, ThermalWindowConfigInfo.TAG_ARRAY);
            this.f15200e = bArr;
        }

        public byte b() {
            int i10 = this.f15201f;
            byte[] bArr = this.f15200e;
            if (i10 >= bArr.length) {
                throw new NoSuchElementException(String.valueOf(this.f15201f));
            }
            this.f15201f = i10 + 1;
            return UByte.b(bArr[i10]);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f15201f < this.f15200e.length;
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ UByte next() {
            return UByte.a(b());
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    public static boolean e(byte[] bArr, byte b10) {
        boolean s7;
        s7 = _Arrays.s(bArr, b10);
        return s7;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0032 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:18:? A[LOOP:0: B:7:0x0013->B:18:?, LOOP_END, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean f(byte[] bArr, Collection<UByte> collection) {
        boolean z10;
        boolean s7;
        za.k.e(collection, "elements");
        if (!collection.isEmpty()) {
            for (Object obj : collection) {
                if (obj instanceof UByte) {
                    s7 = _Arrays.s(bArr, ((UByte) obj).f());
                    if (s7) {
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

    public static boolean g(byte[] bArr, Object obj) {
        return (obj instanceof UByteArray) && za.k.a(bArr, ((UByteArray) obj).o());
    }

    public static int i(byte[] bArr) {
        return bArr.length;
    }

    public static int k(byte[] bArr) {
        return Arrays.hashCode(bArr);
    }

    public static boolean l(byte[] bArr) {
        return bArr.length == 0;
    }

    public static Iterator<UByte> m(byte[] bArr) {
        return new a(bArr);
    }

    public static String n(byte[] bArr) {
        return "UByteArray(storage=" + Arrays.toString(bArr) + ')';
    }

    @Override // java.util.Collection
    public /* bridge */ /* synthetic */ boolean add(UByte uByte) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends UByte> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof UByte) {
            return d(((UByte) obj).f());
        }
        return false;
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<? extends Object> collection) {
        za.k.e(collection, "elements");
        return f(this.f15199e, collection);
    }

    public boolean d(byte b10) {
        return e(this.f15199e, b10);
    }

    @Override // java.util.Collection
    public boolean equals(Object obj) {
        return g(this.f15199e, obj);
    }

    @Override // java.util.Collection
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public int size() {
        return i(this.f15199e);
    }

    @Override // java.util.Collection
    public int hashCode() {
        return k(this.f15199e);
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return l(this.f15199e);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<UByte> iterator() {
        return m(this.f15199e);
    }

    public final /* synthetic */ byte[] o() {
        return this.f15199e;
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
        return n(this.f15199e);
    }
}
