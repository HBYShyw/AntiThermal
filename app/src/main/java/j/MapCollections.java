package j;

import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.InnerUtils;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MapCollections.java */
/* renamed from: j.f, reason: use source file name */
/* loaded from: classes.dex */
public abstract class MapCollections<K, V> {

    /* renamed from: a, reason: collision with root package name */
    MapCollections<K, V>.b f12902a;

    /* renamed from: b, reason: collision with root package name */
    MapCollections<K, V>.c f12903b;

    /* renamed from: c, reason: collision with root package name */
    MapCollections<K, V>.e f12904c;

    /* compiled from: MapCollections.java */
    /* renamed from: j.f$a */
    /* loaded from: classes.dex */
    final class a<T> implements Iterator<T> {

        /* renamed from: e, reason: collision with root package name */
        final int f12905e;

        /* renamed from: f, reason: collision with root package name */
        int f12906f;

        /* renamed from: g, reason: collision with root package name */
        int f12907g;

        /* renamed from: h, reason: collision with root package name */
        boolean f12908h = false;

        a(int i10) {
            this.f12905e = i10;
            this.f12906f = MapCollections.this.d();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f12907g < this.f12906f;
        }

        @Override // java.util.Iterator
        public T next() {
            if (hasNext()) {
                T t7 = (T) MapCollections.this.b(this.f12907g, this.f12905e);
                this.f12907g++;
                this.f12908h = true;
                return t7;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.f12908h) {
                int i10 = this.f12907g - 1;
                this.f12907g = i10;
                this.f12906f--;
                this.f12908h = false;
                MapCollections.this.h(i10);
                return;
            }
            throw new IllegalStateException();
        }
    }

    /* compiled from: MapCollections.java */
    /* renamed from: j.f$b */
    /* loaded from: classes.dex */
    final class b implements Set<Map.Entry<K, V>> {
        b() {
        }

        @Override // java.util.Set, java.util.Collection
        public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
            int d10 = MapCollections.this.d();
            for (Map.Entry<K, V> entry : collection) {
                MapCollections.this.g(entry.getKey(), entry.getValue());
            }
            return d10 != MapCollections.this.d();
        }

        @Override // java.util.Set, java.util.Collection
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public boolean add(Map.Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public void clear() {
            MapCollections.this.a();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            int e10 = MapCollections.this.e(entry.getKey());
            if (e10 < 0) {
                return false;
            }
            return ContainerHelpers.c(MapCollections.this.b(e10, 1), entry.getValue());
        }

        @Override // java.util.Set, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean equals(Object obj) {
            return MapCollections.k(this, obj);
        }

        @Override // java.util.Set, java.util.Collection
        public int hashCode() {
            int i10 = 0;
            for (int d10 = MapCollections.this.d() - 1; d10 >= 0; d10--) {
                Object b10 = MapCollections.this.b(d10, 0);
                Object b11 = MapCollections.this.b(d10, 1);
                i10 += (b10 == null ? 0 : b10.hashCode()) ^ (b11 == null ? 0 : b11.hashCode());
            }
            return i10;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean isEmpty() {
            return MapCollections.this.d() == 0;
        }

        @Override // java.util.Set, java.util.Collection, java.lang.Iterable
        public Iterator<Map.Entry<K, V>> iterator() {
            return new d();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public int size() {
            return MapCollections.this.d();
        }

        @Override // java.util.Set, java.util.Collection
        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            throw new UnsupportedOperationException();
        }
    }

    /* compiled from: MapCollections.java */
    /* renamed from: j.f$c */
    /* loaded from: classes.dex */
    final class c implements Set<K> {
        c() {
        }

        @Override // java.util.Set, java.util.Collection
        public boolean add(K k10) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean addAll(Collection<? extends K> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public void clear() {
            MapCollections.this.a();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean contains(Object obj) {
            return MapCollections.this.e(obj) >= 0;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            return MapCollections.j(MapCollections.this.c(), collection);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean equals(Object obj) {
            return MapCollections.k(this, obj);
        }

        @Override // java.util.Set, java.util.Collection
        public int hashCode() {
            int i10 = 0;
            for (int d10 = MapCollections.this.d() - 1; d10 >= 0; d10--) {
                Object b10 = MapCollections.this.b(d10, 0);
                i10 += b10 == null ? 0 : b10.hashCode();
            }
            return i10;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean isEmpty() {
            return MapCollections.this.d() == 0;
        }

        @Override // java.util.Set, java.util.Collection, java.lang.Iterable
        public Iterator<K> iterator() {
            return new a(0);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean remove(Object obj) {
            int e10 = MapCollections.this.e(obj);
            if (e10 < 0) {
                return false;
            }
            MapCollections.this.h(e10);
            return true;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            return MapCollections.o(MapCollections.this.c(), collection);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            return MapCollections.p(MapCollections.this.c(), collection);
        }

        @Override // java.util.Set, java.util.Collection
        public int size() {
            return MapCollections.this.d();
        }

        @Override // java.util.Set, java.util.Collection
        public Object[] toArray() {
            return MapCollections.this.q(0);
        }

        @Override // java.util.Set, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) MapCollections.this.r(tArr, 0);
        }
    }

    /* compiled from: MapCollections.java */
    /* renamed from: j.f$d */
    /* loaded from: classes.dex */
    final class d implements Iterator<Map.Entry<K, V>>, Map.Entry<K, V> {

        /* renamed from: e, reason: collision with root package name */
        int f12912e;

        /* renamed from: g, reason: collision with root package name */
        boolean f12914g = false;

        /* renamed from: f, reason: collision with root package name */
        int f12913f = -1;

        d() {
            this.f12912e = MapCollections.this.d() - 1;
        }

        @Override // java.util.Iterator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Map.Entry<K, V> next() {
            if (hasNext()) {
                this.f12913f++;
                this.f12914g = true;
                return this;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (this.f12914g) {
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry) obj;
                return ContainerHelpers.c(entry.getKey(), MapCollections.this.b(this.f12913f, 0)) && ContainerHelpers.c(entry.getValue(), MapCollections.this.b(this.f12913f, 1));
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            if (this.f12914g) {
                return (K) MapCollections.this.b(this.f12913f, 0);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            if (this.f12914g) {
                return (V) MapCollections.this.b(this.f12913f, 1);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f12913f < this.f12912e;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            if (this.f12914g) {
                Object b10 = MapCollections.this.b(this.f12913f, 0);
                Object b11 = MapCollections.this.b(this.f12913f, 1);
                return (b10 == null ? 0 : b10.hashCode()) ^ (b11 != null ? b11.hashCode() : 0);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.f12914g) {
                MapCollections.this.h(this.f12913f);
                this.f12913f--;
                this.f12912e--;
                this.f12914g = false;
                return;
            }
            throw new IllegalStateException();
        }

        @Override // java.util.Map.Entry
        public V setValue(V v7) {
            if (this.f12914g) {
                return (V) MapCollections.this.i(this.f12913f, v7);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public String toString() {
            return getKey() + InnerUtils.EQUAL + getValue();
        }
    }

    /* compiled from: MapCollections.java */
    /* renamed from: j.f$e */
    /* loaded from: classes.dex */
    final class e implements Collection<V> {
        e() {
        }

        @Override // java.util.Collection
        public boolean add(V v7) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public void clear() {
            MapCollections.this.a();
        }

        @Override // java.util.Collection
        public boolean contains(Object obj) {
            return MapCollections.this.f(obj) >= 0;
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            return MapCollections.this.d() == 0;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return new a(1);
        }

        @Override // java.util.Collection
        public boolean remove(Object obj) {
            int f10 = MapCollections.this.f(obj);
            if (f10 < 0) {
                return false;
            }
            MapCollections.this.h(f10);
            return true;
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            int d10 = MapCollections.this.d();
            int i10 = 0;
            boolean z10 = false;
            while (i10 < d10) {
                if (collection.contains(MapCollections.this.b(i10, 1))) {
                    MapCollections.this.h(i10);
                    i10--;
                    d10--;
                    z10 = true;
                }
                i10++;
            }
            return z10;
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            int d10 = MapCollections.this.d();
            int i10 = 0;
            boolean z10 = false;
            while (i10 < d10) {
                if (!collection.contains(MapCollections.this.b(i10, 1))) {
                    MapCollections.this.h(i10);
                    i10--;
                    d10--;
                    z10 = true;
                }
                i10++;
            }
            return z10;
        }

        @Override // java.util.Collection
        public int size() {
            return MapCollections.this.d();
        }

        @Override // java.util.Collection
        public Object[] toArray() {
            return MapCollections.this.q(1);
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) MapCollections.this.r(tArr, 1);
        }
    }

    public static <K, V> boolean j(Map<K, V> map, Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (!map.containsKey(it.next())) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean k(Set<T> set, Object obj) {
        if (set == obj) {
            return true;
        }
        if (obj instanceof Set) {
            Set set2 = (Set) obj;
            try {
                if (set.size() == set2.size()) {
                    if (set.containsAll(set2)) {
                        return true;
                    }
                }
                return false;
            } catch (ClassCastException | NullPointerException unused) {
            }
        }
        return false;
    }

    public static <K, V> boolean o(Map<K, V> map, Collection<?> collection) {
        int size = map.size();
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            map.remove(it.next());
        }
        return size != map.size();
    }

    public static <K, V> boolean p(Map<K, V> map, Collection<?> collection) {
        int size = map.size();
        Iterator<K> it = map.keySet().iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
            }
        }
        return size != map.size();
    }

    protected abstract void a();

    protected abstract Object b(int i10, int i11);

    protected abstract Map<K, V> c();

    protected abstract int d();

    protected abstract int e(Object obj);

    protected abstract int f(Object obj);

    protected abstract void g(K k10, V v7);

    protected abstract void h(int i10);

    protected abstract V i(int i10, V v7);

    public Set<Map.Entry<K, V>> l() {
        if (this.f12902a == null) {
            this.f12902a = new b();
        }
        return this.f12902a;
    }

    public Set<K> m() {
        if (this.f12903b == null) {
            this.f12903b = new c();
        }
        return this.f12903b;
    }

    public Collection<V> n() {
        if (this.f12904c == null) {
            this.f12904c = new e();
        }
        return this.f12904c;
    }

    public Object[] q(int i10) {
        int d10 = d();
        Object[] objArr = new Object[d10];
        for (int i11 = 0; i11 < d10; i11++) {
            objArr[i11] = b(i11, i10);
        }
        return objArr;
    }

    public <T> T[] r(T[] tArr, int i10) {
        int d10 = d();
        if (tArr.length < d10) {
            tArr = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), d10));
        }
        for (int i11 = 0; i11 < d10; i11++) {
            tArr[i11] = b(i11, i10);
        }
        if (tArr.length > d10) {
            tArr[d10] = null;
        }
        return tArr;
    }
}
