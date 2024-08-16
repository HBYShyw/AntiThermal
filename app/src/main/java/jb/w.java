package jb;

import gb.m;
import jb.ReflectProperties;
import jb.b0;
import pb.PropertyDescriptor;
import za.Lambda;

/* compiled from: KProperty0Impl.kt */
/* loaded from: classes2.dex */
public class w<V> extends b0<V> implements gb.m<V> {

    /* renamed from: q, reason: collision with root package name */
    private final ReflectProperties.b<a<V>> f13356q;

    /* renamed from: r, reason: collision with root package name */
    private final ma.h<Object> f13357r;

    /* compiled from: KProperty0Impl.kt */
    /* loaded from: classes2.dex */
    public static final class a<R> extends b0.c<R> implements m.a<R> {

        /* renamed from: l, reason: collision with root package name */
        private final w<R> f13358l;

        /* JADX WARN: Multi-variable type inference failed */
        public a(w<? extends R> wVar) {
            za.k.e(wVar, "property");
            this.f13358l = wVar;
        }

        @Override // gb.l.a
        /* renamed from: O, reason: merged with bridge method [inline-methods] */
        public w<R> s() {
            return this.f13358l;
        }

        @Override // ya.a
        public R invoke() {
            return s().get();
        }
    }

    /* compiled from: KProperty0Impl.kt */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<a<? extends V>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ w<V> f13359e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        b(w<? extends V> wVar) {
            super(0);
            this.f13359e = wVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final a<V> invoke() {
            return new a<>(this.f13359e);
        }
    }

    /* compiled from: KProperty0Impl.kt */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<Object> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ w<V> f13360e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        c(w<? extends V> wVar) {
            super(0);
            this.f13360e = wVar;
        }

        @Override // ya.a
        public final Object invoke() {
            w<V> wVar = this.f13360e;
            return wVar.N(wVar.L(), null, null);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public w(KDeclarationContainerImpl kDeclarationContainerImpl, PropertyDescriptor propertyDescriptor) {
        super(kDeclarationContainerImpl, propertyDescriptor);
        ma.h<Object> a10;
        za.k.e(kDeclarationContainerImpl, "container");
        za.k.e(propertyDescriptor, "descriptor");
        ReflectProperties.b<a<V>> b10 = ReflectProperties.b(new b(this));
        za.k.d(b10, "lazy { Getter(this) }");
        this.f13356q = b10;
        a10 = ma.j.a(ma.l.PUBLICATION, new c(this));
        this.f13357r = a10;
    }

    @Override // gb.l
    /* renamed from: S, reason: merged with bridge method [inline-methods] */
    public a<V> h() {
        a<V> invoke = this.f13356q.invoke();
        za.k.d(invoke, "_getter()");
        return invoke;
    }

    @Override // gb.m
    public V get() {
        return h().d(new Object[0]);
    }

    @Override // ya.a
    public V invoke() {
        return get();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public w(KDeclarationContainerImpl kDeclarationContainerImpl, String str, String str2, Object obj) {
        super(kDeclarationContainerImpl, str, str2, obj);
        ma.h<Object> a10;
        za.k.e(kDeclarationContainerImpl, "container");
        za.k.e(str, "name");
        za.k.e(str2, "signature");
        ReflectProperties.b<a<V>> b10 = ReflectProperties.b(new b(this));
        za.k.d(b10, "lazy { Getter(this) }");
        this.f13356q = b10;
        a10 = ma.j.a(ma.l.PUBLICATION, new c(this));
        this.f13357r = a10;
    }
}
