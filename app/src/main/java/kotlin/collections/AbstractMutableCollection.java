package kotlin.collections;

import java.util.AbstractCollection;

/* compiled from: AbstractMutableCollection.kt */
/* renamed from: kotlin.collections.d, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractMutableCollection<E> extends AbstractCollection<E> implements ab.a {
    public abstract int d();

    @Override // java.util.AbstractCollection, java.util.Collection
    public final /* bridge */ int size() {
        return d();
    }
}
