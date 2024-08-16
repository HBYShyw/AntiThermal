package com.android.server.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WatchedArrayList<E> extends WatchableImpl implements Snappable {
    private final Watcher mObserver;
    private final ArrayList<E> mStorage;
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
        if (this.mWatching && (obj instanceof Watchable) && !this.mStorage.contains(obj)) {
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
                registerChild(this.mStorage.get(i));
            }
        }
    }

    @Override // com.android.server.utils.WatchableImpl, com.android.server.utils.Watchable
    public void unregisterObserver(Watcher watcher) {
        super.unregisterObserver(watcher);
        if (registeredObserverCount() == 0) {
            int size = this.mStorage.size();
            for (int i = 0; i < size; i++) {
                unregisterChild(this.mStorage.get(i));
            }
            this.mWatching = false;
        }
    }

    public WatchedArrayList() {
        this(0);
    }

    public WatchedArrayList(int i) {
        this.mWatching = false;
        this.mObserver = new Watcher() { // from class: com.android.server.utils.WatchedArrayList.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                WatchedArrayList.this.dispatchChange(watchable);
            }
        };
        this.mStorage = new ArrayList<>(i);
    }

    public WatchedArrayList(Collection<? extends E> collection) {
        this.mWatching = false;
        this.mObserver = new Watcher() { // from class: com.android.server.utils.WatchedArrayList.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                WatchedArrayList.this.dispatchChange(watchable);
            }
        };
        ArrayList<E> arrayList = new ArrayList<>();
        this.mStorage = arrayList;
        if (collection != null) {
            arrayList.addAll(collection);
        }
    }

    public WatchedArrayList(ArrayList<E> arrayList) {
        this.mWatching = false;
        this.mObserver = new Watcher() { // from class: com.android.server.utils.WatchedArrayList.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                WatchedArrayList.this.dispatchChange(watchable);
            }
        };
        this.mStorage = new ArrayList<>(arrayList);
    }

    public WatchedArrayList(WatchedArrayList<E> watchedArrayList) {
        this.mWatching = false;
        this.mObserver = new Watcher() { // from class: com.android.server.utils.WatchedArrayList.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                WatchedArrayList.this.dispatchChange(watchable);
            }
        };
        this.mStorage = new ArrayList<>(watchedArrayList.mStorage);
    }

    public void copyFrom(ArrayList<E> arrayList) {
        clear();
        int size = arrayList.size();
        this.mStorage.ensureCapacity(size);
        for (int i = 0; i < size; i++) {
            add(arrayList.get(i));
        }
    }

    public void copyTo(ArrayList<E> arrayList) {
        arrayList.clear();
        int size = size();
        arrayList.ensureCapacity(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(get(i));
        }
    }

    public ArrayList<E> untrackedStorage() {
        return this.mStorage;
    }

    public boolean add(E e) {
        boolean add = this.mStorage.add(e);
        registerChild(e);
        onChanged();
        return add;
    }

    public void add(int i, E e) {
        this.mStorage.add(i, e);
        registerChild(e);
        onChanged();
    }

    public boolean addAll(Collection<? extends E> collection) {
        if (collection.size() <= 0) {
            return false;
        }
        Iterator<? extends E> it = collection.iterator();
        while (it.hasNext()) {
            this.mStorage.add(it.next());
        }
        onChanged();
        return true;
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        if (collection.size() <= 0) {
            return false;
        }
        Iterator<? extends E> it = collection.iterator();
        while (it.hasNext()) {
            this.mStorage.add(i, it.next());
            i++;
        }
        onChanged();
        return true;
    }

    public void clear() {
        if (this.mWatching) {
            int size = this.mStorage.size();
            for (int i = 0; i < size; i++) {
                unregisterChild(this.mStorage.get(i));
            }
        }
        this.mStorage.clear();
        onChanged();
    }

    public boolean contains(Object obj) {
        return this.mStorage.contains(obj);
    }

    public boolean containsAll(Collection<?> collection) {
        return this.mStorage.containsAll(collection);
    }

    public void ensureCapacity(int i) {
        this.mStorage.ensureCapacity(i);
    }

    public E get(int i) {
        return this.mStorage.get(i);
    }

    public int indexOf(Object obj) {
        return this.mStorage.indexOf(obj);
    }

    public boolean isEmpty() {
        return this.mStorage.isEmpty();
    }

    public int lastIndexOf(Object obj) {
        return this.mStorage.lastIndexOf(obj);
    }

    public E remove(int i) {
        E remove = this.mStorage.remove(i);
        unregisterChildIf(remove);
        onChanged();
        return remove;
    }

    public boolean remove(Object obj) {
        if (!this.mStorage.remove(obj)) {
            return false;
        }
        unregisterChildIf(obj);
        onChanged();
        return true;
    }

    public E set(int i, E e) {
        E e2 = this.mStorage.set(i, e);
        if (e != e2) {
            unregisterChildIf(e2);
            registerChild(e);
            onChanged();
        }
        return e2;
    }

    public int size() {
        return this.mStorage.size();
    }

    public boolean equals(Object obj) {
        if (obj instanceof WatchedArrayList) {
            return this.mStorage.equals(((WatchedArrayList) obj).mStorage);
        }
        return false;
    }

    public int hashCode() {
        return this.mStorage.hashCode();
    }

    @Override // com.android.server.utils.Snappable
    public WatchedArrayList<E> snapshot() {
        WatchedArrayList<E> watchedArrayList = new WatchedArrayList<>(size());
        snapshot(watchedArrayList, this);
        return watchedArrayList;
    }

    public void snapshot(WatchedArrayList<E> watchedArrayList) {
        snapshot(this, watchedArrayList);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E> void snapshot(WatchedArrayList<E> watchedArrayList, WatchedArrayList<E> watchedArrayList2) {
        if (watchedArrayList.size() != 0) {
            throw new IllegalArgumentException("snapshot destination is not empty");
        }
        int size = watchedArrayList2.size();
        ((WatchedArrayList) watchedArrayList).mStorage.ensureCapacity(size);
        for (int i = 0; i < size; i++) {
            ((WatchedArrayList) watchedArrayList).mStorage.add(Snapshots.maybeSnapshot(watchedArrayList2.get(i)));
        }
        watchedArrayList.seal();
    }
}
