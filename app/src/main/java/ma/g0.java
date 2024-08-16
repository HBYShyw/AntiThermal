package ma;

import java.io.Serializable;

/* compiled from: Lazy.kt */
/* loaded from: classes2.dex */
public final class g0<T> implements h<T>, Serializable {

    /* renamed from: e, reason: collision with root package name */
    private ya.a<? extends T> f15175e;

    /* renamed from: f, reason: collision with root package name */
    private Object f15176f;

    public g0(ya.a<? extends T> aVar) {
        za.k.e(aVar, "initializer");
        this.f15175e = aVar;
        this.f15176f = b0.f15160a;
    }

    private final Object writeReplace() {
        return new d(getValue());
    }

    public boolean a() {
        return this.f15176f != b0.f15160a;
    }

    @Override // ma.h
    public T getValue() {
        if (this.f15176f == b0.f15160a) {
            ya.a<? extends T> aVar = this.f15175e;
            za.k.b(aVar);
            this.f15176f = aVar.invoke();
            this.f15175e = null;
        }
        return (T) this.f15176f;
    }

    public String toString() {
        return a() ? String.valueOf(getValue()) : "Lazy value not initialized yet.";
    }
}
