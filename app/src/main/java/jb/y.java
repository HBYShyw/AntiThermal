package jb;

import java.lang.reflect.Member;
import jb.ReflectProperties;
import jb.b0;
import pb.PropertyDescriptor;

/* compiled from: KProperty2Impl.kt */
/* loaded from: classes2.dex */
public class y<D, E, V> extends b0<V> implements ya.p {

    /* renamed from: q, reason: collision with root package name */
    private final ReflectProperties.b<a<D, E, V>> f13366q;

    /* renamed from: r, reason: collision with root package name */
    private final ma.h<Member> f13367r;

    /* compiled from: KProperty2Impl.kt */
    /* loaded from: classes2.dex */
    public static final class a<D, E, V> extends b0.c<V> implements ya.p {

        /* renamed from: l, reason: collision with root package name */
        private final y<D, E, V> f13368l;

        /* JADX WARN: Multi-variable type inference failed */
        public a(y<D, E, ? extends V> yVar) {
            za.k.e(yVar, "property");
            this.f13368l = yVar;
        }

        @Override // gb.l.a
        /* renamed from: O, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
        public y<D, E, V> s() {
            return this.f13368l;
        }

        @Override // ya.p
        public V invoke(D d10, E e10) {
            return M().S(d10, e10);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public y(KDeclarationContainerImpl kDeclarationContainerImpl, PropertyDescriptor propertyDescriptor) {
        super(kDeclarationContainerImpl, propertyDescriptor);
        ma.h<Member> a10;
        za.k.e(kDeclarationContainerImpl, "container");
        za.k.e(propertyDescriptor, "descriptor");
        ReflectProperties.b<a<D, E, V>> b10 = ReflectProperties.b(new z(this));
        za.k.d(b10, "lazy { Getter(this) }");
        this.f13366q = b10;
        a10 = ma.j.a(ma.l.PUBLICATION, new a0(this));
        this.f13367r = a10;
    }

    public V S(D d10, E e10) {
        return P().d(d10, e10);
    }

    @Override // gb.l
    /* renamed from: T, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public a<D, E, V> h() {
        a<D, E, V> invoke = this.f13366q.invoke();
        za.k.d(invoke, "_getter()");
        return invoke;
    }

    @Override // ya.p
    public V invoke(D d10, E e10) {
        return S(d10, e10);
    }
}
