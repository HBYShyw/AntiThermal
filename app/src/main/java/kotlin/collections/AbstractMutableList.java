package kotlin.collections;

import java.util.AbstractList;

/* compiled from: AbstractMutableList.kt */
/* renamed from: kotlin.collections.e, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractMutableList<E> extends AbstractList<E> implements ab.b {
    public abstract int d();

    public abstract E e(int i10);

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ E remove(int i10) {
        return e(i10);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ int size() {
        return d();
    }
}
