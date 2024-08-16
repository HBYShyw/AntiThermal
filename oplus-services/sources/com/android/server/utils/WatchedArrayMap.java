package com.android.server.utils;

import android.util.ArrayMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WatchedArrayMap<K, V> extends WatchableImpl implements Map<K, V>, Snappable {
    private final Watcher mObserver;
    private final ArrayMap<K, V> mStorage;
    private volatile boolean mWatching;

    private void onChanged() {
        dispatchChange(this);
    }

    private void registerChild(Object obj) {
        if (this.mWatching && (obj instanceof Watchable)) {
            ((Watchable) obj).registerObserver(this.mObserver);
        }
    }

    private void unregisterChild(Object obj) {
        if (this.mWatching && (obj instanceof Watchable)) {
            ((Watchable) obj).unregisterObserver(this.mObserver);
        }
    }

    private void unregisterChildIf(Object obj) {
        if (this.mWatching && (obj instanceof Watchable) && !this.mStorage.containsValue(obj)) {
            ((Watchable) obj).unregisterObserver(this.mObserver);
        }
    }

    @Override // com.android.server.utils.WatchableImpl, com.android.server.utils.Watchable
    public void registerObserver(Watcher watcher) {
        super.registerObserver(watcher);
        if (registeredObserverCount() == 1) {
            this.mWatching = true;
            int size = this.mStorage.size();
            for (int i = 0; i < size; i++) {
                registerChild(this.mStorage.valueAt(i));
            }
        }
    }

    @Override // com.android.server.utils.WatchableImpl, com.android.server.utils.Watchable
    public void unregisterObserver(Watcher watcher) {
        super.unregisterObserver(watcher);
        if (registeredObserverCount() == 0) {
            int size = this.mStorage.size();
            for (int i = 0; i < size; i++) {
                unregisterChild(this.mStorage.valueAt(i));
            }
            this.mWatching = false;
        }
    }

    public WatchedArrayMap() {
        this(0, false);
    }

    public WatchedArrayMap(int i) {
        this(i, false);
    }

    public WatchedArrayMap(int i, boolean z) {
        this.mWatching = false;
        this.mObserver = new Watcher() { // from class: com.android.server.utils.WatchedArrayMap.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                WatchedArrayMap.this.dispatchChange(watchable);
            }
        };
        this.mStorage = new ArrayMap<>(i, z);
    }

    public WatchedArrayMap(Map<? extends K, ? extends V> map) {
        this.mWatching = false;
        this.mObserver = new Watcher() { // from class: com.android.server.utils.WatchedArrayMap.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                WatchedArrayMap.this.dispatchChange(watchable);
            }
        };
        this.mStorage = new ArrayMap<>();
        if (map != null) {
            putAll(map);
        }
    }

    public WatchedArrayMap(ArrayMap<K, V> arrayMap) {
        this.mWatching = false;
        this.mObserver = new Watcher() { // from class: com.android.server.utils.WatchedArrayMap.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                WatchedArrayMap.this.dispatchChange(watchable);
            }
        };
        this.mStorage = new ArrayMap<>(arrayMap);
    }

    public WatchedArrayMap(WatchedArrayMap<K, V> watchedArrayMap) {
        this.mWatching = false;
        this.mObserver = new Watcher() { // from class: com.android.server.utils.WatchedArrayMap.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                WatchedArrayMap.this.dispatchChange(watchable);
            }
        };
        this.mStorage = new ArrayMap<>(watchedArrayMap.mStorage);
    }

    public void copyFrom(ArrayMap<K, V> arrayMap) {
        clear();
        int size = arrayMap.size();
        this.mStorage.ensureCapacity(size);
        for (int i = 0; i < size; i++) {
            put(arrayMap.keyAt(i), arrayMap.valueAt(i));
        }
    }

    public void copyTo(ArrayMap<K, V> arrayMap) {
        arrayMap.clear();
        int size = size();
        arrayMap.ensureCapacity(size);
        for (int i = 0; i < size; i++) {
            arrayMap.put(keyAt(i), valueAt(i));
        }
    }

    public ArrayMap<K, V> untrackedStorage() {
        return this.mStorage;
    }

    @Override // java.util.Map
    public void clear() {
        if (this.mWatching) {
            int size = this.mStorage.size();
            for (int i = 0; i < size; i++) {
                unregisterChild(this.mStorage.valueAt(i));
            }
        }
        this.mStorage.clear();
        onChanged();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return this.mStorage.containsKey(obj);
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return this.mStorage.containsValue(obj);
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return Collections.unmodifiableSet(this.mStorage.entrySet());
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        if (obj instanceof WatchedArrayMap) {
            return this.mStorage.equals(((WatchedArrayMap) obj).mStorage);
        }
        return false;
    }

    @Override // java.util.Map
    public V get(Object obj) {
        return this.mStorage.get(obj);
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.mStorage.hashCode();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.mStorage.isEmpty();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return Collections.unmodifiableSet(this.mStorage.keySet());
    }

    @Override // java.util.Map
    public V put(K k, V v) {
        V put = this.mStorage.put(k, v);
        registerChild(v);
        onChanged();
        return put;
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.Map
    public V remove(Object obj) {
        V remove = this.mStorage.remove(obj);
        unregisterChildIf(remove);
        onChanged();
        return remove;
    }

    @Override // java.util.Map
    public int size() {
        return this.mStorage.size();
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return Collections.unmodifiableCollection(this.mStorage.values());
    }

    public K keyAt(int i) {
        return this.mStorage.keyAt(i);
    }

    public V valueAt(int i) {
        return this.mStorage.valueAt(i);
    }

    public int indexOfKey(K k) {
        return this.mStorage.indexOfKey(k);
    }

    public int indexOfValue(V v) {
        return this.mStorage.indexOfValue(v);
    }

    public V setValueAt(int i, V v) {
        V valueAt = this.mStorage.setValueAt(i, v);
        if (v != valueAt) {
            unregisterChildIf(valueAt);
            registerChild(v);
            onChanged();
        }
        return valueAt;
    }

    public V removeAt(int i) {
        V removeAt = this.mStorage.removeAt(i);
        unregisterChildIf(removeAt);
        onChanged();
        return removeAt;
    }

    @Override // com.android.server.utils.Snappable
    public WatchedArrayMap<K, V> snapshot() {
        WatchedArrayMap<K, V> watchedArrayMap = new WatchedArrayMap<>();
        snapshot(watchedArrayMap, this);
        return watchedArrayMap;
    }

    public void snapshot(WatchedArrayMap<K, V> watchedArrayMap) {
        snapshot(this, watchedArrayMap);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <K, V> void snapshot(WatchedArrayMap<K, V> watchedArrayMap, WatchedArrayMap<K, V> watchedArrayMap2) {
        if (watchedArrayMap.size() != 0) {
            throw new IllegalArgumentException("snapshot destination is not empty");
        }
        int size = watchedArrayMap2.size();
        ((WatchedArrayMap) watchedArrayMap).mStorage.ensureCapacity(size);
        for (int i = 0; i < size; i++) {
            Object maybeSnapshot = Snapshots.maybeSnapshot(watchedArrayMap2.valueAt(i));
            ((WatchedArrayMap) watchedArrayMap).mStorage.put(watchedArrayMap2.keyAt(i), maybeSnapshot);
        }
        watchedArrayMap.seal();
    }
}
