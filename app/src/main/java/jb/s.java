package jb;

import jb.ReflectProperties;
import jb.b0;
import ma.Unit;
import pb.PropertyDescriptor;

/* compiled from: KProperty2Impl.kt */
/* loaded from: classes2.dex */
public final class s<D, E, V> extends y<D, E, V> implements gb.i {

    /* renamed from: s, reason: collision with root package name */
    private final ReflectProperties.b<a<D, E, V>> f13327s;

    /* compiled from: KProperty2Impl.kt */
    /* loaded from: classes2.dex */
    public static final class a<D, E, V> extends b0.d<V> implements ya.q {

        /* renamed from: l, reason: collision with root package name */
        private final s<D, E, V> f13328l;

        public a(s<D, E, V> sVar) {
            za.k.e(sVar, "property");
            this.f13328l = sVar;
        }

        @Override // gb.l.a
        /* renamed from: O, reason: merged with bridge method [inline-methods] */
        public s<D, E, V> s() {
            return this.f13328l;
        }

        public void P(D d10, E e10, V v7) {
            s().V(d10, e10, v7);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // ya.q
        public /* bridge */ /* synthetic */ Object g(Object obj, Object obj2, Object obj3) {
            P(obj, obj2, obj3);
            return Unit.f15173a;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public s(KDeclarationContainerImpl kDeclarationContainerImpl, PropertyDescriptor propertyDescriptor) {
        super(kDeclarationContainerImpl, propertyDescriptor);
        za.k.e(kDeclarationContainerImpl, "container");
        za.k.e(propertyDescriptor, "descriptor");
        ReflectProperties.b<a<D, E, V>> b10 = ReflectProperties.b(new t(this));
        za.k.d(b10, "lazy { Setter(this) }");
        this.f13327s = b10;
    }

    @Override // gb.i
    /* renamed from: U, reason: merged with bridge method [inline-methods] */
    public a<D, E, V> k() {
        a<D, E, V> invoke = this.f13327s.invoke();
        za.k.d(invoke, "_setter()");
        return invoke;
    }

    public void V(D d10, E e10, V v7) {
        k().d(d10, e10, v7);
    }
}
