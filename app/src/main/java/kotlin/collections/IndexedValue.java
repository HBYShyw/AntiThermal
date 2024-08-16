package kotlin.collections;

/* compiled from: IndexedValue.kt */
/* renamed from: kotlin.collections.e0, reason: use source file name */
/* loaded from: classes2.dex */
public final class IndexedValue<T> {

    /* renamed from: a, reason: collision with root package name */
    private final int f14320a;

    /* renamed from: b, reason: collision with root package name */
    private final T f14321b;

    public IndexedValue(int i10, T t7) {
        this.f14320a = i10;
        this.f14321b = t7;
    }

    public final int a() {
        return this.f14320a;
    }

    public final T b() {
        return this.f14321b;
    }

    public final int c() {
        return this.f14320a;
    }

    public final T d() {
        return this.f14321b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IndexedValue)) {
            return false;
        }
        IndexedValue indexedValue = (IndexedValue) obj;
        return this.f14320a == indexedValue.f14320a && za.k.a(this.f14321b, indexedValue.f14321b);
    }

    public int hashCode() {
        int hashCode = Integer.hashCode(this.f14320a) * 31;
        T t7 = this.f14321b;
        return hashCode + (t7 == null ? 0 : t7.hashCode());
    }

    public String toString() {
        return "IndexedValue(index=" + this.f14320a + ", value=" + this.f14321b + ')';
    }
}
