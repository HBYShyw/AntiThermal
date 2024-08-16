package androidx.core.util;

/* compiled from: Pair.java */
/* renamed from: androidx.core.util.d, reason: use source file name */
/* loaded from: classes.dex */
public class Pair<F, S> {

    /* renamed from: a, reason: collision with root package name */
    public final F f2305a;

    /* renamed from: b, reason: collision with root package name */
    public final S f2306b;

    public Pair(F f10, S s7) {
        this.f2305a = f10;
        this.f2306b = s7;
    }

    public static <A, B> Pair<A, B> a(A a10, B b10) {
        return new Pair<>(a10, b10);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) obj;
        return ObjectsCompat.a(pair.f2305a, this.f2305a) && ObjectsCompat.a(pair.f2306b, this.f2306b);
    }

    public int hashCode() {
        F f10 = this.f2305a;
        int hashCode = f10 == null ? 0 : f10.hashCode();
        S s7 = this.f2306b;
        return hashCode ^ (s7 != null ? s7.hashCode() : 0);
    }

    public String toString() {
        return "Pair{" + this.f2305a + " " + this.f2306b + "}";
    }
}
