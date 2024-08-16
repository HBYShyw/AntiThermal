package jb;

import gb.n;
import java.lang.reflect.Member;
import jb.ReflectProperties;
import jb.b0;
import pb.PropertyDescriptor;
import za.Lambda;

/* compiled from: KProperty1Impl.kt */
/* loaded from: classes2.dex */
public class x<T, V> extends b0<V> implements gb.n<T, V> {

    /* renamed from: q, reason: collision with root package name */
    private final ReflectProperties.b<a<T, V>> f13361q;

    /* renamed from: r, reason: collision with root package name */
    private final ma.h<Member> f13362r;

    /* compiled from: KProperty1Impl.kt */
    /* loaded from: classes2.dex */
    public static final class a<T, V> extends b0.c<V> implements n.a<T, V> {

        /* renamed from: l, reason: collision with root package name */
        private final x<T, V> f13363l;

        /* JADX WARN: Multi-variable type inference failed */
        public a(x<T, ? extends V> xVar) {
            za.k.e(xVar, "property");
            this.f13363l = xVar;
        }

        @Override // gb.l.a
        /* renamed from: O, reason: merged with bridge method [inline-methods] */
        public x<T, V> s() {
            return this.f13363l;
        }

        @Override // ya.l
        public V invoke(T t7) {
            return s().get(t7);
        }
    }

    /* compiled from: KProperty1Impl.kt */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<a<T, ? extends V>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ x<T, V> f13364e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        b(x<T, ? extends V> xVar) {
            super(0);
            this.f13364e = xVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final a<T, V> invoke() {
            return new a<>(this.f13364e);
        }
    }

    /* compiled from: KProperty1Impl.kt */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<Member> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ x<T, V> f13365e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        c(x<T, ? extends V> xVar) {
            super(0);
            this.f13365e = xVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Member invoke() {
            return this.f13365e.L();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public x(KDeclarationContainerImpl kDeclarationContainerImpl, String str, String str2, Object obj) {
        super(kDeclarationContainerImpl, str, str2, obj);
        ma.h<Member> a10;
        za.k.e(kDeclarationContainerImpl, "container");
        za.k.e(str, "name");
        za.k.e(str2, "signature");
        ReflectProperties.b<a<T, V>> b10 = ReflectProperties.b(new b(this));
        za.k.d(b10, "lazy { Getter(this) }");
        this.f13361q = b10;
        a10 = ma.j.a(ma.l.PUBLICATION, new c(this));
        this.f13362r = a10;
    }

    @Override // gb.l
    /* renamed from: S, reason: merged with bridge method [inline-methods] */
    public a<T, V> h() {
        a<T, V> invoke = this.f13361q.invoke();
        za.k.d(invoke, "_getter()");
        return invoke;
    }

    @Override // gb.n
    public V get(T t7) {
        return h().d(t7);
    }

    @Override // ya.l
    public V invoke(T t7) {
        return get(t7);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public x(KDeclarationContainerImpl kDeclarationContainerImpl, PropertyDescriptor propertyDescriptor) {
        super(kDeclarationContainerImpl, propertyDescriptor);
        ma.h<Member> a10;
        za.k.e(kDeclarationContainerImpl, "container");
        za.k.e(propertyDescriptor, "descriptor");
        ReflectProperties.b<a<T, V>> b10 = ReflectProperties.b(new b(this));
        za.k.d(b10, "lazy { Getter(this) }");
        this.f13361q = b10;
        a10 = ma.j.a(ma.l.PUBLICATION, new c(this));
        this.f13362r = a10;
    }
}
