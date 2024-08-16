package za;

import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* compiled from: ArrayIterator.kt */
/* loaded from: classes2.dex */
final class a<T> implements Iterator<T>, ab.a {

    /* renamed from: e, reason: collision with root package name */
    private final T[] f20347e;

    /* renamed from: f, reason: collision with root package name */
    private int f20348f;

    public a(T[] tArr) {
        k.e(tArr, ThermalWindowConfigInfo.TAG_ARRAY);
        this.f20347e = tArr;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.f20348f < this.f20347e.length;
    }

    @Override // java.util.Iterator
    public T next() {
        try {
            T[] tArr = this.f20347e;
            int i10 = this.f20348f;
            this.f20348f = i10 + 1;
            return tArr[i10];
        } catch (ArrayIndexOutOfBoundsException e10) {
            this.f20348f--;
            throw new NoSuchElementException(e10.getMessage());
        }
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}
