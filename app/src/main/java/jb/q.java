package jb;

import jb.ReflectProperties;
import jb.b0;
import ma.Unit;
import pb.PropertyDescriptor;
import za.Lambda;

/* compiled from: KProperty0Impl.kt */
/* loaded from: classes2.dex */
public final class q<V> extends w<V> implements gb.i {

    /* renamed from: s, reason: collision with root package name */
    private final ReflectProperties.b<a<V>> f13321s;

    /* compiled from: KProperty0Impl.kt */
    /* loaded from: classes2.dex */
    public static final class a<R> extends b0.d<R> implements ya.l {

        /* renamed from: l, reason: collision with root package name */
        private final q<R> f13322l;

        public a(q<R> qVar) {
            za.k.e(qVar, "property");
            this.f13322l = qVar;
        }

        @Override // gb.l.a
        /* renamed from: O, reason: merged with bridge method [inline-methods] */
        public q<R> s() {
            return this.f13322l;
        }

        public void P(R r10) {
            s().U(r10);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // ya.l
        public /* bridge */ /* synthetic */ Object invoke(Object obj) {
            P(obj);
            return Unit.f15173a;
        }
    }

    /* compiled from: KProperty0Impl.kt */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<a<V>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ q<V> f13323e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(q<V> qVar) {
            super(0);
            this.f13323e = qVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final a<V> invoke() {
            return new a<>(this.f13323e);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public q(KDeclarationContainerImpl kDeclarationContainerImpl, PropertyDescriptor propertyDescriptor) {
        super(kDeclarationContainerImpl, propertyDescriptor);
        za.k.e(kDeclarationContainerImpl, "container");
        za.k.e(propertyDescriptor, "descriptor");
        ReflectProperties.b<a<V>> b10 = ReflectProperties.b(new b(this));
        za.k.d(b10, "lazy { Setter(this) }");
        this.f13321s = b10;
    }

    @Override // gb.i
    /* renamed from: T, reason: merged with bridge method [inline-methods] */
    public a<V> k() {
        a<V> invoke = this.f13321s.invoke();
        za.k.d(invoke, "_setter()");
        return invoke;
    }

    public void U(V v7) {
        k().d(v7);
    }
}
