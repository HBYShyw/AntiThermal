package androidx.core.util;

/* compiled from: Pools.java */
/* loaded from: classes.dex */
public class f<T> implements e<T> {

    /* renamed from: a, reason: collision with root package name */
    private final Object[] f2307a;

    /* renamed from: b, reason: collision with root package name */
    private int f2308b;

    public f(int i10) {
        if (i10 > 0) {
            this.f2307a = new Object[i10];
            return;
        }
        throw new IllegalArgumentException("The max pool size must be > 0");
    }

    private boolean c(T t7) {
        for (int i10 = 0; i10 < this.f2308b; i10++) {
            if (this.f2307a[i10] == t7) {
                return true;
            }
        }
        return false;
    }

    @Override // androidx.core.util.e
    public boolean a(T t7) {
        if (!c(t7)) {
            int i10 = this.f2308b;
            Object[] objArr = this.f2307a;
            if (i10 >= objArr.length) {
                return false;
            }
            objArr[i10] = t7;
            this.f2308b = i10 + 1;
            return true;
        }
        throw new IllegalStateException("Already in the pool!");
    }

    @Override // androidx.core.util.e
    public T b() {
        int i10 = this.f2308b;
        if (i10 <= 0) {
            return null;
        }
        int i11 = i10 - 1;
        Object[] objArr = this.f2307a;
        T t7 = (T) objArr[i11];
        objArr[i11] = null;
        this.f2308b = i10 - 1;
        return t7;
    }
}
