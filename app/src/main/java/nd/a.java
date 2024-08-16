package nd;

import gb.KClass;
import java.util.Iterator;

/* compiled from: ArrayMapOwner.kt */
/* loaded from: classes2.dex */
public abstract class a<K, V> implements Iterable<V>, ab.a {

    /* compiled from: ArrayMapOwner.kt */
    /* renamed from: nd.a$a, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public static abstract class AbstractC0083a<K, V, T extends V> {

        /* renamed from: a, reason: collision with root package name */
        private final KClass<? extends K> f16008a;

        /* renamed from: b, reason: collision with root package name */
        private final int f16009b;

        public AbstractC0083a(KClass<? extends K> kClass, int i10) {
            za.k.e(kClass, "key");
            this.f16008a = kClass;
            this.f16009b = i10;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final T c(a<K, V> aVar) {
            za.k.e(aVar, "thisRef");
            return aVar.d().get(this.f16009b);
        }
    }

    protected abstract c<V> d();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract s<K, V> e();

    public final boolean isEmpty() {
        return d().d() == 0;
    }

    @Override // java.lang.Iterable
    public final Iterator<V> iterator() {
        return d().iterator();
    }
}
