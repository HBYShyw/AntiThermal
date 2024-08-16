package l4;

import androidx.core.util.Pair;

/* compiled from: MutablePair.java */
/* renamed from: l4.i, reason: use source file name */
/* loaded from: classes.dex */
public class MutablePair<T> {

    /* renamed from: a, reason: collision with root package name */
    T f14629a;

    /* renamed from: b, reason: collision with root package name */
    T f14630b;

    private static boolean a(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public void b(T t7, T t10) {
        this.f14629a = t7;
        this.f14630b = t10;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) obj;
        return a(pair.f2305a, this.f14629a) && a(pair.f2306b, this.f14630b);
    }

    public int hashCode() {
        T t7 = this.f14629a;
        int hashCode = t7 == null ? 0 : t7.hashCode();
        T t10 = this.f14630b;
        return hashCode ^ (t10 != null ? t10.hashCode() : 0);
    }

    public String toString() {
        return "Pair{" + this.f14629a + " " + this.f14630b + "}";
    }
}
