package qc;

import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.InnerUtils;
import java.lang.Comparable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import qc.h;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: SmallSortedMap.java */
/* loaded from: classes2.dex */
public class v<K extends Comparable<K>, V> extends AbstractMap<K, V> {

    /* renamed from: e, reason: collision with root package name */
    private final int f17353e;

    /* renamed from: f, reason: collision with root package name */
    private List<v<K, V>.c> f17354f;

    /* renamed from: g, reason: collision with root package name */
    private Map<K, V> f17355g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f17356h;

    /* renamed from: i, reason: collision with root package name */
    private volatile v<K, V>.e f17357i;

    /* JADX INFO: Add missing generic type declarations: [FieldDescriptorType] */
    /* compiled from: SmallSortedMap.java */
    /* loaded from: classes2.dex */
    static class a<FieldDescriptorType> extends v<FieldDescriptorType, Object> {
        a(int i10) {
            super(i10, null);
        }

        @Override // qc.v
        public void n() {
            if (!m()) {
                for (int i10 = 0; i10 < j(); i10++) {
                    Map.Entry<FieldDescriptorType, Object> i11 = i(i10);
                    if (((h.b) i11.getKey()).isRepeated()) {
                        i11.setValue(Collections.unmodifiableList((List) i11.getValue()));
                    }
                }
                for (Map.Entry<FieldDescriptorType, Object> entry : k()) {
                    if (((h.b) entry.getKey()).isRepeated()) {
                        entry.setValue(Collections.unmodifiableList((List) entry.getValue()));
                    }
                }
            }
            super.n();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
            return super.p((h.b) obj, obj2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SmallSortedMap.java */
    /* loaded from: classes2.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        private static final Iterator<Object> f17358a = new a();

        /* renamed from: b, reason: collision with root package name */
        private static final Iterable<Object> f17359b = new C0094b();

        /* compiled from: SmallSortedMap.java */
        /* loaded from: classes2.dex */
        static class a implements Iterator<Object> {
            a() {
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return false;
            }

            @Override // java.util.Iterator
            public Object next() {
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        /* compiled from: SmallSortedMap.java */
        /* renamed from: qc.v$b$b, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static class C0094b implements Iterable<Object> {
            C0094b() {
            }

            @Override // java.lang.Iterable
            public Iterator<Object> iterator() {
                return b.f17358a;
            }
        }

        static <T> Iterable<T> b() {
            return (Iterable<T>) f17359b;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SmallSortedMap.java */
    /* loaded from: classes2.dex */
    public class c implements Comparable<v<K, V>.c>, Map.Entry<K, V> {

        /* renamed from: e, reason: collision with root package name */
        private final K f17360e;

        /* renamed from: f, reason: collision with root package name */
        private V f17361f;

        c(v vVar, Map.Entry<K, V> entry) {
            this(entry.getKey(), entry.getValue());
        }

        private boolean b(Object obj, Object obj2) {
            return obj == null ? obj2 == null : obj.equals(obj2);
        }

        @Override // java.lang.Comparable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compareTo(v<K, V>.c cVar) {
            return getKey().compareTo(cVar.getKey());
        }

        @Override // java.util.Map.Entry
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public K getKey() {
            return this.f17360e;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return b(this.f17360e, entry.getKey()) && b(this.f17361f, entry.getValue());
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.f17361f;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            K k10 = this.f17360e;
            int hashCode = k10 == null ? 0 : k10.hashCode();
            V v7 = this.f17361f;
            return hashCode ^ (v7 != null ? v7.hashCode() : 0);
        }

        @Override // java.util.Map.Entry
        public V setValue(V v7) {
            v.this.g();
            V v10 = this.f17361f;
            this.f17361f = v7;
            return v10;
        }

        public String toString() {
            String valueOf = String.valueOf(this.f17360e);
            String valueOf2 = String.valueOf(this.f17361f);
            StringBuilder sb2 = new StringBuilder(valueOf.length() + 1 + valueOf2.length());
            sb2.append(valueOf);
            sb2.append(InnerUtils.EQUAL);
            sb2.append(valueOf2);
            return sb2.toString();
        }

        c(K k10, V v7) {
            this.f17360e = k10;
            this.f17361f = v7;
        }
    }

    /* compiled from: SmallSortedMap.java */
    /* loaded from: classes2.dex */
    private class e extends AbstractSet<Map.Entry<K, V>> {
        private e() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public boolean add(Map.Entry<K, V> entry) {
            if (contains(entry)) {
                return false;
            }
            v.this.p(entry.getKey(), entry.getValue());
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            v.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            Map.Entry entry = (Map.Entry) obj;
            Object obj2 = v.this.get(entry.getKey());
            Object value = entry.getValue();
            return obj2 == value || (obj2 != null && obj2.equals(value));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new d(v.this, null);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Map.Entry entry = (Map.Entry) obj;
            if (!contains(entry)) {
                return false;
            }
            v.this.remove(entry.getKey());
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return v.this.size();
        }

        /* synthetic */ e(v vVar, a aVar) {
            this();
        }
    }

    /* synthetic */ v(int i10, a aVar) {
        this(i10);
    }

    private int f(K k10) {
        int size = this.f17354f.size() - 1;
        if (size >= 0) {
            int compareTo = k10.compareTo(this.f17354f.get(size).getKey());
            if (compareTo > 0) {
                return -(size + 2);
            }
            if (compareTo == 0) {
                return size;
            }
        }
        int i10 = 0;
        while (i10 <= size) {
            int i11 = (i10 + size) / 2;
            int compareTo2 = k10.compareTo(this.f17354f.get(i11).getKey());
            if (compareTo2 < 0) {
                size = i11 - 1;
            } else {
                if (compareTo2 <= 0) {
                    return i11;
                }
                i10 = i11 + 1;
            }
        }
        return -(i10 + 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        if (this.f17356h) {
            throw new UnsupportedOperationException();
        }
    }

    private void h() {
        g();
        if (!this.f17354f.isEmpty() || (this.f17354f instanceof ArrayList)) {
            return;
        }
        this.f17354f = new ArrayList(this.f17353e);
    }

    private SortedMap<K, V> l() {
        g();
        if (this.f17355g.isEmpty() && !(this.f17355g instanceof TreeMap)) {
            this.f17355g = new TreeMap();
        }
        return (SortedMap) this.f17355g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <FieldDescriptorType extends h.b<FieldDescriptorType>> v<FieldDescriptorType, Object> o(int i10) {
        return new a(i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public V q(int i10) {
        g();
        V value = this.f17354f.remove(i10).getValue();
        if (!this.f17355g.isEmpty()) {
            Iterator<Map.Entry<K, V>> it = l().entrySet().iterator();
            this.f17354f.add(new c(this, it.next()));
            it.remove();
        }
        return value;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        g();
        if (!this.f17354f.isEmpty()) {
            this.f17354f.clear();
        }
        if (this.f17355g.isEmpty()) {
            return;
        }
        this.f17355g.clear();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        Comparable comparable = (Comparable) obj;
        return f(comparable) >= 0 || this.f17355g.containsKey(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.f17357i == null) {
            this.f17357i = new e(this, null);
        }
        return this.f17357i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        Comparable comparable = (Comparable) obj;
        int f10 = f(comparable);
        if (f10 >= 0) {
            return this.f17354f.get(f10).getValue();
        }
        return this.f17355g.get(comparable);
    }

    public Map.Entry<K, V> i(int i10) {
        return this.f17354f.get(i10);
    }

    public int j() {
        return this.f17354f.size();
    }

    public Iterable<Map.Entry<K, V>> k() {
        return this.f17355g.isEmpty() ? b.b() : this.f17355g.entrySet();
    }

    public boolean m() {
        return this.f17356h;
    }

    public void n() {
        if (this.f17356h) {
            return;
        }
        this.f17355g = this.f17355g.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.f17355g);
        this.f17356h = true;
    }

    public V p(K k10, V v7) {
        g();
        int f10 = f(k10);
        if (f10 >= 0) {
            return this.f17354f.get(f10).setValue(v7);
        }
        h();
        int i10 = -(f10 + 1);
        if (i10 >= this.f17353e) {
            return l().put(k10, v7);
        }
        int size = this.f17354f.size();
        int i11 = this.f17353e;
        if (size == i11) {
            v<K, V>.c remove = this.f17354f.remove(i11 - 1);
            l().put(remove.getKey(), remove.getValue());
        }
        this.f17354f.add(i10, new c(k10, v7));
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        g();
        Comparable comparable = (Comparable) obj;
        int f10 = f(comparable);
        if (f10 >= 0) {
            return (V) q(f10);
        }
        if (this.f17355g.isEmpty()) {
            return null;
        }
        return this.f17355g.remove(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.f17354f.size() + this.f17355g.size();
    }

    /* compiled from: SmallSortedMap.java */
    /* loaded from: classes2.dex */
    private class d implements Iterator<Map.Entry<K, V>> {

        /* renamed from: e, reason: collision with root package name */
        private int f17363e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f17364f;

        /* renamed from: g, reason: collision with root package name */
        private Iterator<Map.Entry<K, V>> f17365g;

        private d() {
            this.f17363e = -1;
        }

        private Iterator<Map.Entry<K, V>> a() {
            if (this.f17365g == null) {
                this.f17365g = v.this.f17355g.entrySet().iterator();
            }
            return this.f17365g;
        }

        @Override // java.util.Iterator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public Map.Entry<K, V> next() {
            this.f17364f = true;
            int i10 = this.f17363e + 1;
            this.f17363e = i10;
            if (i10 < v.this.f17354f.size()) {
                return (Map.Entry) v.this.f17354f.get(this.f17363e);
            }
            return a().next();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f17363e + 1 < v.this.f17354f.size() || a().hasNext();
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.f17364f) {
                this.f17364f = false;
                v.this.g();
                if (this.f17363e < v.this.f17354f.size()) {
                    v vVar = v.this;
                    int i10 = this.f17363e;
                    this.f17363e = i10 - 1;
                    vVar.q(i10);
                    return;
                }
                a().remove();
                return;
            }
            throw new IllegalStateException("remove() was called before next()");
        }

        /* synthetic */ d(v vVar, a aVar) {
            this();
        }
    }

    private v(int i10) {
        this.f17353e = i10;
        this.f17354f = Collections.emptyList();
        this.f17355g = Collections.emptyMap();
    }
}
