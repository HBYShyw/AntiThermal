package md;

import za.k;

/* compiled from: CapturedTypeApproximation.kt */
/* loaded from: classes2.dex */
public final class a<T> {

    /* renamed from: a, reason: collision with root package name */
    private final T f15455a;

    /* renamed from: b, reason: collision with root package name */
    private final T f15456b;

    public a(T t7, T t10) {
        this.f15455a = t7;
        this.f15456b = t10;
    }

    public final T a() {
        return this.f15455a;
    }

    public final T b() {
        return this.f15456b;
    }

    public final T c() {
        return this.f15455a;
    }

    public final T d() {
        return this.f15456b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof a)) {
            return false;
        }
        a aVar = (a) obj;
        return k.a(this.f15455a, aVar.f15455a) && k.a(this.f15456b, aVar.f15456b);
    }

    public int hashCode() {
        T t7 = this.f15455a;
        int hashCode = (t7 == null ? 0 : t7.hashCode()) * 31;
        T t10 = this.f15456b;
        return hashCode + (t10 != null ? t10.hashCode() : 0);
    }

    public String toString() {
        return "ApproximationBounds(lower=" + this.f15455a + ", upper=" + this.f15456b + ')';
    }
}
