package nd;

import gb.KClass;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import za.Lambda;

/* compiled from: ArrayMapOwner.kt */
/* loaded from: classes2.dex */
public abstract class s<K, V> {

    /* renamed from: a, reason: collision with root package name */
    private final ConcurrentHashMap<KClass<? extends K>, Integer> f16081a = new ConcurrentHashMap<>();

    /* renamed from: b, reason: collision with root package name */
    private final AtomicInteger f16082b = new AtomicInteger(0);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ArrayMapOwner.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<KClass<? extends K>, Integer> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ s<K, V> f16083e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(s<K, V> sVar) {
            super(1);
            this.f16083e = sVar;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Integer invoke(KClass<? extends K> kClass) {
            za.k.e(kClass, "it");
            return Integer.valueOf(((s) this.f16083e).f16082b.getAndIncrement());
        }
    }

    public abstract <T extends K> int b(ConcurrentHashMap<KClass<? extends K>, Integer> concurrentHashMap, KClass<T> kClass, ya.l<? super KClass<? extends K>, Integer> lVar);

    /* JADX WARN: Multi-variable type inference failed */
    public final <T extends V, KK extends K> n<K, V, T> c(KClass<KK> kClass) {
        za.k.e(kClass, "kClass");
        return new n<>(kClass, d(kClass));
    }

    public final <T extends K> int d(KClass<T> kClass) {
        za.k.e(kClass, "kClass");
        return b(this.f16081a, kClass, new a(this));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Collection<Integer> e() {
        Collection<Integer> values = this.f16081a.values();
        za.k.d(values, "idPerType.values");
        return values;
    }
}
