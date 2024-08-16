package androidx.core.util;

/* compiled from: Pools.java */
/* loaded from: classes.dex */
public class g<T> extends f<T> {

    /* renamed from: c, reason: collision with root package name */
    private final Object f2309c;

    public g(int i10) {
        super(i10);
        this.f2309c = new Object();
    }

    @Override // androidx.core.util.f, androidx.core.util.e
    public boolean a(T t7) {
        boolean a10;
        synchronized (this.f2309c) {
            a10 = super.a(t7);
        }
        return a10;
    }

    @Override // androidx.core.util.f, androidx.core.util.e
    public T b() {
        T t7;
        synchronized (this.f2309c) {
            t7 = (T) super.b();
        }
        return t7;
    }
}
