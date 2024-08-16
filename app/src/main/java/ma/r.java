package ma;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import za.DefaultConstructorMarker;

/* compiled from: LazyJVM.kt */
/* loaded from: classes2.dex */
final class r<T> implements h<T>, Serializable {

    /* renamed from: h, reason: collision with root package name */
    public static final a f15186h = new a(null);

    /* renamed from: i, reason: collision with root package name */
    private static final AtomicReferenceFieldUpdater<r<?>, Object> f15187i = AtomicReferenceFieldUpdater.newUpdater(r.class, Object.class, "f");

    /* renamed from: e, reason: collision with root package name */
    private volatile ya.a<? extends T> f15188e;

    /* renamed from: f, reason: collision with root package name */
    private volatile Object f15189f;

    /* renamed from: g, reason: collision with root package name */
    private final Object f15190g;

    /* compiled from: LazyJVM.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public r(ya.a<? extends T> aVar) {
        za.k.e(aVar, "initializer");
        this.f15188e = aVar;
        b0 b0Var = b0.f15160a;
        this.f15189f = b0Var;
        this.f15190g = b0Var;
    }

    private final Object writeReplace() {
        return new d(getValue());
    }

    public boolean a() {
        return this.f15189f != b0.f15160a;
    }

    @Override // ma.h
    public T getValue() {
        T t7 = (T) this.f15189f;
        b0 b0Var = b0.f15160a;
        if (t7 != b0Var) {
            return t7;
        }
        ya.a<? extends T> aVar = this.f15188e;
        if (aVar != null) {
            T invoke = aVar.invoke();
            if (f15187i.compareAndSet(this, b0Var, invoke)) {
                this.f15188e = null;
                return invoke;
            }
        }
        return (T) this.f15189f;
    }

    public String toString() {
        return a() ? String.valueOf(getValue()) : "Lazy value not initialized yet.";
    }
}
