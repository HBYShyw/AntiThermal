package h;

import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.InnerUtils;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/* compiled from: SafeIterableMap.java */
/* renamed from: h.b, reason: use source file name */
/* loaded from: classes.dex */
public class SafeIterableMap<K, V> implements Iterable<Map.Entry<K, V>> {

    /* renamed from: e, reason: collision with root package name */
    c<K, V> f11949e;

    /* renamed from: f, reason: collision with root package name */
    private c<K, V> f11950f;

    /* renamed from: g, reason: collision with root package name */
    private WeakHashMap<f<K, V>, Boolean> f11951g = new WeakHashMap<>();

    /* renamed from: h, reason: collision with root package name */
    private int f11952h = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SafeIterableMap.java */
    /* renamed from: h.b$a */
    /* loaded from: classes.dex */
    public static class a<K, V> extends e<K, V> {
        a(c<K, V> cVar, c<K, V> cVar2) {
            super(cVar, cVar2);
        }

        @Override // h.SafeIterableMap.e
        c<K, V> b(c<K, V> cVar) {
            return cVar.f11956h;
        }

        @Override // h.SafeIterableMap.e
        c<K, V> c(c<K, V> cVar) {
            return cVar.f11955g;
        }
    }

    /* compiled from: SafeIterableMap.java */
    /* renamed from: h.b$b */
    /* loaded from: classes.dex */
    private static class b<K, V> extends e<K, V> {
        b(c<K, V> cVar, c<K, V> cVar2) {
            super(cVar, cVar2);
        }

        @Override // h.SafeIterableMap.e
        c<K, V> b(c<K, V> cVar) {
            return cVar.f11955g;
        }

        @Override // h.SafeIterableMap.e
        c<K, V> c(c<K, V> cVar) {
            return cVar.f11956h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SafeIterableMap.java */
    /* renamed from: h.b$c */
    /* loaded from: classes.dex */
    public static class c<K, V> implements Map.Entry<K, V> {

        /* renamed from: e, reason: collision with root package name */
        final K f11953e;

        /* renamed from: f, reason: collision with root package name */
        final V f11954f;

        /* renamed from: g, reason: collision with root package name */
        c<K, V> f11955g;

        /* renamed from: h, reason: collision with root package name */
        c<K, V> f11956h;

        c(K k10, V v7) {
            this.f11953e = k10;
            this.f11954f = v7;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof c)) {
                return false;
            }
            c cVar = (c) obj;
            return this.f11953e.equals(cVar.f11953e) && this.f11954f.equals(cVar.f11954f);
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.f11953e;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.f11954f;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.f11954f.hashCode() ^ this.f11953e.hashCode();
        }

        @Override // java.util.Map.Entry
        public V setValue(V v7) {
            throw new UnsupportedOperationException("An entry modification is not supported");
        }

        public String toString() {
            return this.f11953e + InnerUtils.EQUAL + this.f11954f;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SafeIterableMap.java */
    /* renamed from: h.b$d */
    /* loaded from: classes.dex */
    public class d implements Iterator<Map.Entry<K, V>>, f<K, V> {

        /* renamed from: e, reason: collision with root package name */
        private c<K, V> f11957e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f11958f = true;

        d() {
        }

        @Override // h.SafeIterableMap.f
        public void a(c<K, V> cVar) {
            c<K, V> cVar2 = this.f11957e;
            if (cVar == cVar2) {
                c<K, V> cVar3 = cVar2.f11956h;
                this.f11957e = cVar3;
                this.f11958f = cVar3 == null;
            }
        }

        @Override // java.util.Iterator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public Map.Entry<K, V> next() {
            if (this.f11958f) {
                this.f11958f = false;
                this.f11957e = SafeIterableMap.this.f11949e;
            } else {
                c<K, V> cVar = this.f11957e;
                this.f11957e = cVar != null ? cVar.f11955g : null;
            }
            return this.f11957e;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.f11958f) {
                return SafeIterableMap.this.f11949e != null;
            }
            c<K, V> cVar = this.f11957e;
            return (cVar == null || cVar.f11955g == null) ? false : true;
        }
    }

    /* compiled from: SafeIterableMap.java */
    /* renamed from: h.b$e */
    /* loaded from: classes.dex */
    private static abstract class e<K, V> implements Iterator<Map.Entry<K, V>>, f<K, V> {

        /* renamed from: e, reason: collision with root package name */
        c<K, V> f11960e;

        /* renamed from: f, reason: collision with root package name */
        c<K, V> f11961f;

        e(c<K, V> cVar, c<K, V> cVar2) {
            this.f11960e = cVar2;
            this.f11961f = cVar;
        }

        private c<K, V> e() {
            c<K, V> cVar = this.f11961f;
            c<K, V> cVar2 = this.f11960e;
            if (cVar == cVar2 || cVar2 == null) {
                return null;
            }
            return c(cVar);
        }

        @Override // h.SafeIterableMap.f
        public void a(c<K, V> cVar) {
            if (this.f11960e == cVar && cVar == this.f11961f) {
                this.f11961f = null;
                this.f11960e = null;
            }
            c<K, V> cVar2 = this.f11960e;
            if (cVar2 == cVar) {
                this.f11960e = b(cVar2);
            }
            if (this.f11961f == cVar) {
                this.f11961f = e();
            }
        }

        abstract c<K, V> b(c<K, V> cVar);

        abstract c<K, V> c(c<K, V> cVar);

        @Override // java.util.Iterator
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public Map.Entry<K, V> next() {
            c<K, V> cVar = this.f11961f;
            this.f11961f = e();
            return cVar;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f11961f != null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SafeIterableMap.java */
    /* renamed from: h.b$f */
    /* loaded from: classes.dex */
    public interface f<K, V> {
        void a(c<K, V> cVar);
    }

    public Iterator<Map.Entry<K, V>> c() {
        b bVar = new b(this.f11950f, this.f11949e);
        this.f11951g.put(bVar, Boolean.FALSE);
        return bVar;
    }

    public Map.Entry<K, V> d() {
        return this.f11949e;
    }

    protected c<K, V> e(K k10) {
        c<K, V> cVar = this.f11949e;
        while (cVar != null && !cVar.f11953e.equals(k10)) {
            cVar = cVar.f11955g;
        }
        return cVar;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SafeIterableMap)) {
            return false;
        }
        SafeIterableMap safeIterableMap = (SafeIterableMap) obj;
        if (size() != safeIterableMap.size()) {
            return false;
        }
        Iterator<Map.Entry<K, V>> it = iterator();
        Iterator<Map.Entry<K, V>> it2 = safeIterableMap.iterator();
        while (it.hasNext() && it2.hasNext()) {
            Map.Entry<K, V> next = it.next();
            Map.Entry<K, V> next2 = it2.next();
            if ((next == null && next2 != null) || (next != null && !next.equals(next2))) {
                return false;
            }
        }
        return (it.hasNext() || it2.hasNext()) ? false : true;
    }

    public SafeIterableMap<K, V>.d f() {
        SafeIterableMap<K, V>.d dVar = new d();
        this.f11951g.put(dVar, Boolean.FALSE);
        return dVar;
    }

    public Map.Entry<K, V> g() {
        return this.f11950f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public c<K, V> h(K k10, V v7) {
        c<K, V> cVar = new c<>(k10, v7);
        this.f11952h++;
        c<K, V> cVar2 = this.f11950f;
        if (cVar2 == null) {
            this.f11949e = cVar;
            this.f11950f = cVar;
            return cVar;
        }
        cVar2.f11955g = cVar;
        cVar.f11956h = cVar2;
        this.f11950f = cVar;
        return cVar;
    }

    public int hashCode() {
        Iterator<Map.Entry<K, V>> it = iterator();
        int i10 = 0;
        while (it.hasNext()) {
            i10 += it.next().hashCode();
        }
        return i10;
    }

    public V i(K k10, V v7) {
        c<K, V> e10 = e(k10);
        if (e10 != null) {
            return e10.f11954f;
        }
        h(k10, v7);
        return null;
    }

    @Override // java.lang.Iterable
    public Iterator<Map.Entry<K, V>> iterator() {
        a aVar = new a(this.f11949e, this.f11950f);
        this.f11951g.put(aVar, Boolean.FALSE);
        return aVar;
    }

    public V k(K k10) {
        c<K, V> e10 = e(k10);
        if (e10 == null) {
            return null;
        }
        this.f11952h--;
        if (!this.f11951g.isEmpty()) {
            Iterator<f<K, V>> it = this.f11951g.keySet().iterator();
            while (it.hasNext()) {
                it.next().a(e10);
            }
        }
        c<K, V> cVar = e10.f11956h;
        if (cVar != null) {
            cVar.f11955g = e10.f11955g;
        } else {
            this.f11949e = e10.f11955g;
        }
        c<K, V> cVar2 = e10.f11955g;
        if (cVar2 != null) {
            cVar2.f11956h = cVar;
        } else {
            this.f11950f = cVar;
        }
        e10.f11955g = null;
        e10.f11956h = null;
        return e10.f11954f;
    }

    public int size() {
        return this.f11952h;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[");
        Iterator<Map.Entry<K, V>> it = iterator();
        while (it.hasNext()) {
            sb2.append(it.next().toString());
            if (it.hasNext()) {
                sb2.append(", ");
            }
        }
        sb2.append("]");
        return sb2.toString();
    }
}
