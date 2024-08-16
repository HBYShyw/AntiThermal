package ma;

import java.io.Serializable;

/* compiled from: Tuples.kt */
/* loaded from: classes2.dex */
public final class t<A, B, C> implements Serializable {

    /* renamed from: e, reason: collision with root package name */
    private final A f15194e;

    /* renamed from: f, reason: collision with root package name */
    private final B f15195f;

    /* renamed from: g, reason: collision with root package name */
    private final C f15196g;

    public t(A a10, B b10, C c10) {
        this.f15194e = a10;
        this.f15195f = b10;
        this.f15196g = c10;
    }

    public final A a() {
        return this.f15194e;
    }

    public final B b() {
        return this.f15195f;
    }

    public final C c() {
        return this.f15196g;
    }

    public final C d() {
        return this.f15196g;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof t)) {
            return false;
        }
        t tVar = (t) obj;
        return za.k.a(this.f15194e, tVar.f15194e) && za.k.a(this.f15195f, tVar.f15195f) && za.k.a(this.f15196g, tVar.f15196g);
    }

    public int hashCode() {
        A a10 = this.f15194e;
        int hashCode = (a10 == null ? 0 : a10.hashCode()) * 31;
        B b10 = this.f15195f;
        int hashCode2 = (hashCode + (b10 == null ? 0 : b10.hashCode())) * 31;
        C c10 = this.f15196g;
        return hashCode2 + (c10 != null ? c10.hashCode() : 0);
    }

    public String toString() {
        return '(' + this.f15194e + ", " + this.f15195f + ", " + this.f15196g + ')';
    }
}
