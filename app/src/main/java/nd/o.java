package nd;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* compiled from: ArrayMap.kt */
/* loaded from: classes2.dex */
public final class o<T> extends c<T> {

    /* renamed from: e, reason: collision with root package name */
    private final T f16037e;

    /* renamed from: f, reason: collision with root package name */
    private final int f16038f;

    /* compiled from: ArrayMap.kt */
    /* loaded from: classes2.dex */
    public static final class a implements Iterator<T>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private boolean f16039e = true;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ o<T> f16040f;

        a(o<T> oVar) {
            this.f16040f = oVar;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f16039e;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.f16039e) {
                this.f16039e = false;
                return this.f16040f.g();
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public o(T t7, int i10) {
        super(null);
        za.k.e(t7, ThermalBaseConfig.Item.ATTR_VALUE);
        this.f16037e = t7;
        this.f16038f = i10;
    }

    @Override // nd.c
    public int d() {
        return 1;
    }

    @Override // nd.c
    public void e(int i10, T t7) {
        za.k.e(t7, ThermalBaseConfig.Item.ATTR_VALUE);
        throw new IllegalStateException();
    }

    public final int f() {
        return this.f16038f;
    }

    public final T g() {
        return this.f16037e;
    }

    @Override // nd.c
    public T get(int i10) {
        if (i10 == this.f16038f) {
            return this.f16037e;
        }
        return null;
    }

    @Override // nd.c, java.lang.Iterable
    public Iterator<T> iterator() {
        return new a(this);
    }
}
