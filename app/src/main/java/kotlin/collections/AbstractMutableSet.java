package kotlin.collections;

import java.util.AbstractSet;

/* compiled from: AbstractMutableSet.kt */
/* renamed from: kotlin.collections.g, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractMutableSet<E> extends AbstractSet<E> implements ab.d {
    public abstract int d();

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final /* bridge */ int size() {
        return d();
    }
}
