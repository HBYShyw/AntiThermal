package kotlin.collections;

import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ReversedViews.kt */
/* loaded from: classes2.dex */
public final class p0<T> extends AbstractMutableList<T> {

    /* renamed from: e, reason: collision with root package name */
    private final List<T> f14333e;

    public p0(List<T> list) {
        za.k.e(list, "delegate");
        this.f14333e = list;
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int i10, T t7) {
        int H;
        List<T> list = this.f14333e;
        H = x.H(this, i10);
        list.add(H, t7);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.f14333e.clear();
    }

    @Override // kotlin.collections.AbstractMutableList
    public int d() {
        return this.f14333e.size();
    }

    @Override // kotlin.collections.AbstractMutableList
    public T e(int i10) {
        int G;
        List<T> list = this.f14333e;
        G = x.G(this, i10);
        return list.remove(G);
    }

    @Override // java.util.AbstractList, java.util.List
    public T get(int i10) {
        int G;
        List<T> list = this.f14333e;
        G = x.G(this, i10);
        return list.get(G);
    }

    @Override // java.util.AbstractList, java.util.List
    public T set(int i10, T t7) {
        int G;
        List<T> list = this.f14333e;
        G = x.G(this, i10);
        return list.set(G, t7);
    }
}
