package com.android.server.am;

import android.util.ArrayMap;
import android.util.SparseArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UidProcessMap<E> {
    final SparseArray<ArrayMap<String, E>> mMap = new SparseArray<>();

    public E get(int i, String str) {
        ArrayMap<String, E> arrayMap = this.mMap.get(i);
        if (arrayMap == null) {
            return null;
        }
        return arrayMap.get(str);
    }

    public E put(int i, String str, E e) {
        ArrayMap<String, E> arrayMap = this.mMap.get(i);
        if (arrayMap == null) {
            arrayMap = new ArrayMap<>(2);
            this.mMap.put(i, arrayMap);
        }
        arrayMap.put(str, e);
        return e;
    }

    public E remove(int i, String str) {
        ArrayMap<String, E> valueAt;
        int indexOfKey = this.mMap.indexOfKey(i);
        if (indexOfKey < 0 || (valueAt = this.mMap.valueAt(indexOfKey)) == null) {
            return null;
        }
        E remove = valueAt.remove(str);
        if (valueAt.isEmpty()) {
            this.mMap.removeAt(indexOfKey);
        }
        return remove;
    }

    public SparseArray<ArrayMap<String, E>> getMap() {
        return this.mMap;
    }

    public int size() {
        return this.mMap.size();
    }

    public void clear() {
        this.mMap.clear();
    }

    public void putAll(UidProcessMap<E> uidProcessMap) {
        for (int size = uidProcessMap.mMap.size() - 1; size >= 0; size--) {
            int keyAt = uidProcessMap.mMap.keyAt(size);
            ArrayMap<String, E> arrayMap = this.mMap.get(keyAt);
            if (arrayMap != null) {
                arrayMap.putAll((ArrayMap<? extends String, ? extends E>) uidProcessMap.mMap.valueAt(size));
            } else {
                this.mMap.put(keyAt, new ArrayMap<>(uidProcessMap.mMap.valueAt(size)));
            }
        }
    }
}
