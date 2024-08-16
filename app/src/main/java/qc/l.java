package qc;

import java.util.Iterator;
import java.util.Map;

/* compiled from: LazyField.java */
/* loaded from: classes2.dex */
public class l extends m {

    /* renamed from: e, reason: collision with root package name */
    private final q f17318e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LazyField.java */
    /* loaded from: classes2.dex */
    public static class b<K> implements Map.Entry<K, Object> {

        /* renamed from: e, reason: collision with root package name */
        private Map.Entry<K, l> f17319e;

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.f17319e.getKey();
        }

        @Override // java.util.Map.Entry
        public Object getValue() {
            l value = this.f17319e.getValue();
            if (value == null) {
                return null;
            }
            return value.e();
        }

        @Override // java.util.Map.Entry
        public Object setValue(Object obj) {
            if (obj instanceof q) {
                return this.f17319e.getValue().d((q) obj);
            }
            throw new IllegalArgumentException("LazyField now only used for MessageSet, and the value of MessageSet must be an instance of MessageLite");
        }

        private b(Map.Entry<K, l> entry) {
            this.f17319e = entry;
        }
    }

    /* compiled from: LazyField.java */
    /* loaded from: classes2.dex */
    static class c<K> implements Iterator<Map.Entry<K, Object>> {

        /* renamed from: e, reason: collision with root package name */
        private Iterator<Map.Entry<K, Object>> f17320e;

        public c(Iterator<Map.Entry<K, Object>> it) {
            this.f17320e = it;
        }

        @Override // java.util.Iterator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Map.Entry<K, Object> next() {
            Map.Entry<K, Object> next = this.f17320e.next();
            return next.getValue() instanceof l ? new b(next) : next;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f17320e.hasNext();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.f17320e.remove();
        }
    }

    public q e() {
        return c(this.f17318e);
    }

    public boolean equals(Object obj) {
        return e().equals(obj);
    }

    public int hashCode() {
        return e().hashCode();
    }

    public String toString() {
        return e().toString();
    }
}
