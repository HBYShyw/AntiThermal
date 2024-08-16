package com.android.server.location.contexthub;

import java.util.concurrent.ConcurrentLinkedDeque;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ConcurrentLinkedEvictingDeque<E> extends ConcurrentLinkedDeque<E> {
    private int mSize;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConcurrentLinkedEvictingDeque(int i) {
        this.mSize = i;
    }

    @Override // java.util.concurrent.ConcurrentLinkedDeque, java.util.AbstractCollection, java.util.Collection, java.util.Deque, java.util.Queue
    public boolean add(E e) {
        boolean add;
        synchronized (this) {
            if (size() == this.mSize) {
                poll();
            }
            add = super.add(e);
        }
        return add;
    }
}
