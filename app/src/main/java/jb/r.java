package jb;

import gb.j;
import jb.ReflectProperties;
import jb.b0;
import ma.Unit;
import pb.PropertyDescriptor;
import za.Lambda;

/* compiled from: KProperty1Impl.kt */
/* loaded from: classes2.dex */
public final class r<T, V> extends x<T, V> implements gb.j<T, V> {

    /* renamed from: s, reason: collision with root package name */
    private final ReflectProperties.b<a<T, V>> f13324s;

    /* compiled from: KProperty1Impl.kt */
    /* loaded from: classes2.dex */
    public static final class a<T, V> extends b0.d<V> implements j.a<T, V> {

        /* renamed from: l, reason: collision with root package name */
        private final r<T, V> f13325l;

        public a(r<T, V> rVar) {
            za.k.e(rVar, "property");
            this.f13325l = rVar;
        }

        @Override // gb.l.a
        /* renamed from: O, reason: merged with bridge method [inline-methods] */
        public r<T, V> s() {
            return this.f13325l;
        }

        public void P(T t7, V v7) {
            s().x(t7, v7);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // ya.p
        public /* bridge */ /* synthetic */ Unit invoke(Object obj, Object obj2) {
            P(obj, obj2);
            return Unit.f15173a;
        }
    }

    /* compiled from: KProperty1Impl.kt */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<a<T, V>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ r<T, V> f13326e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(r<T, V> rVar) {
            super(0);
            this.f13326e = rVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final a<T, V> invoke() {
            return new a<>(this.f13326e);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public r(KDeclarationContainerImpl kDeclarationContainerImpl, String str, String str2, Object obj) {
        super(kDeclarationContainerImpl, str, str2, obj);
        za.k.e(kDeclarationContainerImpl, "container");
        za.k.e(str, "name");
        za.k.e(str2, "signature");
        ReflectProperties.b<a<T, V>> b10 = ReflectProperties.b(new b(this));
        za.k.d(b10, "lazy { Setter(this) }");
        this.f13324s = b10;
    }

    @Override // gb.j, gb.i
    /* renamed from: T, reason: merged with bridge method [inline-methods] */
    public a<T, V> k() {
        a<T, V> invoke = this.f13324s.invoke();
        za.k.d(invoke, "_setter()");
        return invoke;
    }

    @Override // gb.j
    public void x(T t7, V v7) {
        k().d(t7, v7);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public r(KDeclarationContainerImpl kDeclarationContainerImpl, PropertyDescriptor propertyDescriptor) {
        super(kDeclarationContainerImpl, propertyDescriptor);
        za.k.e(kDeclarationContainerImpl, "container");
        za.k.e(propertyDescriptor, "descriptor");
        ReflectProperties.b<a<T, V>> b10 = ReflectProperties.b(new b(this));
        za.k.d(b10, "lazy { Setter(this) }");
        this.f13324s = b10;
    }
}
