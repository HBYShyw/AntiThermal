package com.android.server;

import android.util.ArrayMap;
import java.util.Collection;
import java.util.LinkedList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CircularQueue<K, V> extends LinkedList<K> {
    private final ArrayMap<K, V> mArrayMap = new ArrayMap<>();
    private final int mLimit;

    public CircularQueue(int i) {
        this.mLimit = i;
    }

    @Override // java.util.LinkedList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.Deque, java.util.Queue
    public boolean add(K k) throws IllegalArgumentException {
        throw new IllegalArgumentException("Call of add(key) prohibited. Please call put(key, value) instead. ");
    }

    public V put(K k, V v) {
        super.add(k);
        this.mArrayMap.put(k, v);
        V v2 = null;
        while (size() > this.mLimit) {
            v2 = this.mArrayMap.remove(super.remove());
        }
        return v2;
    }

    public V removeElement(K k) {
        super.remove(k);
        return this.mArrayMap.remove(k);
    }

    public V getElement(K k) {
        return this.mArrayMap.get(k);
    }

    public boolean containsKey(K k) {
        return this.mArrayMap.containsKey(k);
    }

    public Collection<V> values() {
        return this.mArrayMap.values();
    }
}
