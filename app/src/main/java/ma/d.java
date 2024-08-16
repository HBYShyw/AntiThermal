package ma;

import java.io.Serializable;

/* compiled from: Lazy.kt */
/* loaded from: classes2.dex */
public final class d<T> implements h<T>, Serializable {

    /* renamed from: e, reason: collision with root package name */
    private final T f15163e;

    public d(T t7) {
        this.f15163e = t7;
    }

    @Override // ma.h
    public T getValue() {
        return this.f15163e;
    }

    public String toString() {
        return String.valueOf(getValue());
    }
}
