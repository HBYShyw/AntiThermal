package cb;

import gb.l;
import za.k;

/* compiled from: ObservableProperty.kt */
/* renamed from: cb.b, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class ObservableProperty<V> implements d<Object, V> {

    /* renamed from: a, reason: collision with root package name */
    private V f5040a;

    public ObservableProperty(V v7) {
        this.f5040a = v7;
    }

    @Override // cb.d, cb.c
    public V a(Object obj, l<?> lVar) {
        k.e(lVar, "property");
        return this.f5040a;
    }

    @Override // cb.d
    public void b(Object obj, l<?> lVar, V v7) {
        k.e(lVar, "property");
        V v10 = this.f5040a;
        if (d(lVar, v10, v7)) {
            this.f5040a = v7;
            c(lVar, v10, v7);
        }
    }

    protected void c(l<?> lVar, V v7, V v10) {
        k.e(lVar, "property");
    }

    protected boolean d(l<?> lVar, V v7, V v10) {
        k.e(lVar, "property");
        return true;
    }
}
