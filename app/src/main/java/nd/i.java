package nd;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* compiled from: ArrayMap.kt */
/* loaded from: classes2.dex */
public final class i extends c {

    /* renamed from: e, reason: collision with root package name */
    public static final i f16028e = new i();

    /* compiled from: ArrayMap.kt */
    /* loaded from: classes2.dex */
    public static final class a implements Iterator, ab.a {
        a() {
        }

        @Override // java.util.Iterator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public Void next() {
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return false;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    private i() {
        super(null);
    }

    @Override // nd.c
    public int d() {
        return 0;
    }

    @Override // nd.c
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public Void get(int i10) {
        return null;
    }

    @Override // nd.c
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public void e(int i10, Void r22) {
        za.k.e(r22, ThermalBaseConfig.Item.ATTR_VALUE);
        throw new IllegalStateException();
    }

    @Override // nd.c, java.lang.Iterable
    public Iterator iterator() {
        return new a();
    }
}
