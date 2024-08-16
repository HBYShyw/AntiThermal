package nd;

import gb.KClass;
import nd.a;

/* compiled from: ArrayMapOwner.kt */
/* loaded from: classes2.dex */
public final class n<K, V, T extends V> extends a.AbstractC0083a<K, V, T> implements cb.c<a<K, V>, V> {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public n(KClass<? extends K> kClass, int i10) {
        super(kClass, i10);
        za.k.e(kClass, "key");
    }

    @Override // cb.c
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public T a(a<K, V> aVar, gb.l<?> lVar) {
        za.k.e(aVar, "thisRef");
        za.k.e(lVar, "property");
        return c(aVar);
    }
}
