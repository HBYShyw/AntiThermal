package ma;

import java.io.Serializable;
import za.DefaultConstructorMarker;

/* compiled from: LazyJVM.kt */
/* loaded from: classes2.dex */
final class s<T> implements h<T>, Serializable {

    /* renamed from: e, reason: collision with root package name */
    private ya.a<? extends T> f15191e;

    /* renamed from: f, reason: collision with root package name */
    private volatile Object f15192f;

    /* renamed from: g, reason: collision with root package name */
    private final Object f15193g;

    public s(ya.a<? extends T> aVar, Object obj) {
        za.k.e(aVar, "initializer");
        this.f15191e = aVar;
        this.f15192f = b0.f15160a;
        this.f15193g = obj == null ? this : obj;
    }

    private final Object writeReplace() {
        return new d(getValue());
    }

    public boolean a() {
        return this.f15192f != b0.f15160a;
    }

    @Override // ma.h
    public T getValue() {
        T t7;
        T t10 = (T) this.f15192f;
        b0 b0Var = b0.f15160a;
        if (t10 != b0Var) {
            return t10;
        }
        synchronized (this.f15193g) {
            t7 = (T) this.f15192f;
            if (t7 == b0Var) {
                ya.a<? extends T> aVar = this.f15191e;
                za.k.b(aVar);
                t7 = aVar.invoke();
                this.f15192f = t7;
                this.f15191e = null;
            }
        }
        return t7;
    }

    public String toString() {
        return a() ? String.valueOf(getValue()) : "Lazy value not initialized yet.";
    }

    public /* synthetic */ s(ya.a aVar, Object obj, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(aVar, (i10 & 2) != 0 ? null : obj);
    }
}
