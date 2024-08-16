package ma;

import java.io.Serializable;

/* compiled from: Tuples.kt */
/* loaded from: classes2.dex */
public final class o<A, B> implements Serializable {

    /* renamed from: e, reason: collision with root package name */
    private final A f15182e;

    /* renamed from: f, reason: collision with root package name */
    private final B f15183f;

    public o(A a10, B b10) {
        this.f15182e = a10;
        this.f15183f = b10;
    }

    public final A a() {
        return this.f15182e;
    }

    public final B b() {
        return this.f15183f;
    }

    public final A c() {
        return this.f15182e;
    }

    public final B d() {
        return this.f15183f;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof o)) {
            return false;
        }
        o oVar = (o) obj;
        return za.k.a(this.f15182e, oVar.f15182e) && za.k.a(this.f15183f, oVar.f15183f);
    }

    public int hashCode() {
        A a10 = this.f15182e;
        int hashCode = (a10 == null ? 0 : a10.hashCode()) * 31;
        B b10 = this.f15183f;
        return hashCode + (b10 != null ? b10.hashCode() : 0);
    }

    public String toString() {
        return '(' + this.f15182e + ", " + this.f15183f + ')';
    }
}
