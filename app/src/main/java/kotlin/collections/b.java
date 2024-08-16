package kotlin.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* compiled from: AbstractIterator.kt */
/* loaded from: classes2.dex */
public abstract class b<T> implements Iterator<T>, ab.a {

    /* renamed from: e, reason: collision with root package name */
    private u0 f14307e = u0.NotReady;

    /* renamed from: f, reason: collision with root package name */
    private T f14308f;

    /* compiled from: AbstractIterator.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f14309a;

        static {
            int[] iArr = new int[u0.values().length];
            try {
                iArr[u0.Done.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[u0.Ready.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            f14309a = iArr;
        }
    }

    private final boolean f() {
        this.f14307e = u0.Failed;
        b();
        return this.f14307e == u0.Ready;
    }

    protected abstract void b();

    /* JADX INFO: Access modifiers changed from: protected */
    public final void d() {
        this.f14307e = u0.Done;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void e(T t7) {
        this.f14308f = t7;
        this.f14307e = u0.Ready;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        u0 u0Var = this.f14307e;
        if (u0Var != u0.Failed) {
            int i10 = a.f14309a[u0Var.ordinal()];
            if (i10 == 1) {
                return false;
            }
            if (i10 != 2) {
                return f();
            }
            return true;
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    @Override // java.util.Iterator
    public T next() {
        if (hasNext()) {
            this.f14307e = u0.NotReady;
            return this.f14308f;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}
