package na;

import fb.PrimitiveRanges;
import fb._Ranges;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import kotlin.collections.PrimitiveIterators;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: MapBuilder.kt */
/* loaded from: classes2.dex */
public final class d<K, V> implements Map<K, V>, Serializable, ab.c {

    /* renamed from: q, reason: collision with root package name */
    private static final a f15927q = new a(null);

    /* renamed from: e, reason: collision with root package name */
    private K[] f15928e;

    /* renamed from: f, reason: collision with root package name */
    private V[] f15929f;

    /* renamed from: g, reason: collision with root package name */
    private int[] f15930g;

    /* renamed from: h, reason: collision with root package name */
    private int[] f15931h;

    /* renamed from: i, reason: collision with root package name */
    private int f15932i;

    /* renamed from: j, reason: collision with root package name */
    private int f15933j;

    /* renamed from: k, reason: collision with root package name */
    private int f15934k;

    /* renamed from: l, reason: collision with root package name */
    private int f15935l;

    /* renamed from: m, reason: collision with root package name */
    private na.f<K> f15936m;

    /* renamed from: n, reason: collision with root package name */
    private g<V> f15937n;

    /* renamed from: o, reason: collision with root package name */
    private na.e<K, V> f15938o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f15939p;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: MapBuilder.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int c(int i10) {
            int c10;
            c10 = _Ranges.c(i10, 1);
            return Integer.highestOneBit(c10 * 3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int d(int i10) {
            return Integer.numberOfLeadingZeros(i10) + 1;
        }
    }

    /* compiled from: MapBuilder.kt */
    /* loaded from: classes2.dex */
    public static final class b<K, V> extends C0081d<K, V> implements Iterator<Map.Entry<K, V>>, ab.a {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(d<K, V> dVar) {
            super(dVar);
            k.e(dVar, "map");
        }

        @Override // java.util.Iterator
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public c<K, V> next() {
            if (b() < ((d) e()).f15933j) {
                int b10 = b();
                g(b10 + 1);
                h(b10);
                c<K, V> cVar = new c<>(e(), d());
                f();
                return cVar;
            }
            throw new NoSuchElementException();
        }

        public final void k(StringBuilder sb2) {
            k.e(sb2, "sb");
            if (b() < ((d) e()).f15933j) {
                int b10 = b();
                g(b10 + 1);
                h(b10);
                Object obj = ((d) e()).f15928e[d()];
                if (k.a(obj, e())) {
                    sb2.append("(this Map)");
                } else {
                    sb2.append(obj);
                }
                sb2.append('=');
                Object[] objArr = ((d) e()).f15929f;
                k.b(objArr);
                Object obj2 = objArr[d()];
                if (k.a(obj2, e())) {
                    sb2.append("(this Map)");
                } else {
                    sb2.append(obj2);
                }
                f();
                return;
            }
            throw new NoSuchElementException();
        }

        public final int l() {
            if (b() < ((d) e()).f15933j) {
                int b10 = b();
                g(b10 + 1);
                h(b10);
                Object obj = ((d) e()).f15928e[d()];
                int hashCode = obj != null ? obj.hashCode() : 0;
                Object[] objArr = ((d) e()).f15929f;
                k.b(objArr);
                Object obj2 = objArr[d()];
                int hashCode2 = hashCode ^ (obj2 != null ? obj2.hashCode() : 0);
                f();
                return hashCode2;
            }
            throw new NoSuchElementException();
        }
    }

    /* compiled from: MapBuilder.kt */
    /* loaded from: classes2.dex */
    public static final class c<K, V> implements Map.Entry<K, V>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private final d<K, V> f15940e;

        /* renamed from: f, reason: collision with root package name */
        private final int f15941f;

        public c(d<K, V> dVar, int i10) {
            k.e(dVar, "map");
            this.f15940e = dVar;
            this.f15941f = i10;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                if (k.a(entry.getKey(), getKey()) && k.a(entry.getValue(), getValue())) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return (K) ((d) this.f15940e).f15928e[this.f15941f];
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            Object[] objArr = ((d) this.f15940e).f15929f;
            k.b(objArr);
            return (V) objArr[this.f15941f];
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            K key = getKey();
            int hashCode = key != null ? key.hashCode() : 0;
            V value = getValue();
            return hashCode ^ (value != null ? value.hashCode() : 0);
        }

        @Override // java.util.Map.Entry
        public V setValue(V v7) {
            this.f15940e.m();
            Object[] k10 = this.f15940e.k();
            int i10 = this.f15941f;
            V v10 = (V) k10[i10];
            k10[i10] = v7;
            return v10;
        }

        public String toString() {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getKey());
            sb2.append('=');
            sb2.append(getValue());
            return sb2.toString();
        }
    }

    /* compiled from: MapBuilder.kt */
    /* renamed from: na.d$d, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public static class C0081d<K, V> {

        /* renamed from: e, reason: collision with root package name */
        private final d<K, V> f15942e;

        /* renamed from: f, reason: collision with root package name */
        private int f15943f;

        /* renamed from: g, reason: collision with root package name */
        private int f15944g;

        public C0081d(d<K, V> dVar) {
            k.e(dVar, "map");
            this.f15942e = dVar;
            this.f15944g = -1;
            f();
        }

        public final int b() {
            return this.f15943f;
        }

        public final int d() {
            return this.f15944g;
        }

        public final d<K, V> e() {
            return this.f15942e;
        }

        public final void f() {
            while (this.f15943f < ((d) this.f15942e).f15933j) {
                int[] iArr = ((d) this.f15942e).f15930g;
                int i10 = this.f15943f;
                if (iArr[i10] >= 0) {
                    return;
                } else {
                    this.f15943f = i10 + 1;
                }
            }
        }

        public final void g(int i10) {
            this.f15943f = i10;
        }

        public final void h(int i10) {
            this.f15944g = i10;
        }

        public final boolean hasNext() {
            return this.f15943f < ((d) this.f15942e).f15933j;
        }

        public final void remove() {
            if (this.f15944g != -1) {
                this.f15942e.m();
                this.f15942e.M(this.f15944g);
                this.f15944g = -1;
                return;
            }
            throw new IllegalStateException("Call next() before removing element from the iterator.".toString());
        }
    }

    /* compiled from: MapBuilder.kt */
    /* loaded from: classes2.dex */
    public static final class e<K, V> extends C0081d<K, V> implements Iterator<K>, ab.a {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public e(d<K, V> dVar) {
            super(dVar);
            k.e(dVar, "map");
        }

        @Override // java.util.Iterator
        public K next() {
            if (b() < ((d) e()).f15933j) {
                int b10 = b();
                g(b10 + 1);
                h(b10);
                K k10 = (K) ((d) e()).f15928e[d()];
                f();
                return k10;
            }
            throw new NoSuchElementException();
        }
    }

    /* compiled from: MapBuilder.kt */
    /* loaded from: classes2.dex */
    public static final class f<K, V> extends C0081d<K, V> implements Iterator<V>, ab.a {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public f(d<K, V> dVar) {
            super(dVar);
            k.e(dVar, "map");
        }

        @Override // java.util.Iterator
        public V next() {
            if (b() < ((d) e()).f15933j) {
                int b10 = b();
                g(b10 + 1);
                h(b10);
                Object[] objArr = ((d) e()).f15929f;
                k.b(objArr);
                V v7 = (V) objArr[d()];
                f();
                return v7;
            }
            throw new NoSuchElementException();
        }
    }

    private d(K[] kArr, V[] vArr, int[] iArr, int[] iArr2, int i10, int i11) {
        this.f15928e = kArr;
        this.f15929f = vArr;
        this.f15930g = iArr;
        this.f15931h = iArr2;
        this.f15932i = i10;
        this.f15933j = i11;
        this.f15934k = f15927q.d(y());
    }

    private final int C(K k10) {
        return ((k10 != null ? k10.hashCode() : 0) * (-1640531527)) >>> this.f15934k;
    }

    private final boolean F(Collection<? extends Map.Entry<? extends K, ? extends V>> collection) {
        boolean z10 = false;
        if (collection.isEmpty()) {
            return false;
        }
        s(collection.size());
        Iterator<? extends Map.Entry<? extends K, ? extends V>> it = collection.iterator();
        while (it.hasNext()) {
            if (G(it.next())) {
                z10 = true;
            }
        }
        return z10;
    }

    private final boolean G(Map.Entry<? extends K, ? extends V> entry) {
        int i10 = i(entry.getKey());
        V[] k10 = k();
        if (i10 >= 0) {
            k10[i10] = entry.getValue();
            return true;
        }
        int i11 = (-i10) - 1;
        if (k.a(entry.getValue(), k10[i11])) {
            return false;
        }
        k10[i11] = entry.getValue();
        return true;
    }

    private final boolean H(int i10) {
        int C = C(this.f15928e[i10]);
        int i11 = this.f15932i;
        while (true) {
            int[] iArr = this.f15931h;
            if (iArr[C] == 0) {
                iArr[C] = i10 + 1;
                this.f15930g[i10] = C;
                return true;
            }
            i11--;
            if (i11 < 0) {
                return false;
            }
            C = C == 0 ? y() - 1 : C - 1;
        }
    }

    private final void I(int i10) {
        if (this.f15933j > size()) {
            n();
        }
        int i11 = 0;
        if (i10 != y()) {
            this.f15931h = new int[i10];
            this.f15934k = f15927q.d(i10);
        } else {
            kotlin.collections.j.l(this.f15931h, 0, 0, y());
        }
        while (i11 < this.f15933j) {
            int i12 = i11 + 1;
            if (!H(i11)) {
                throw new IllegalStateException("This cannot happen with fixed magic multiplier and grow-only hash array. Have object hashCodes changed?");
            }
            i11 = i12;
        }
    }

    private final void K(int i10) {
        int f10;
        f10 = _Ranges.f(this.f15932i * 2, y() / 2);
        int i11 = f10;
        int i12 = 0;
        int i13 = i10;
        do {
            i10 = i10 == 0 ? y() - 1 : i10 - 1;
            i12++;
            if (i12 > this.f15932i) {
                this.f15931h[i13] = 0;
                return;
            }
            int[] iArr = this.f15931h;
            int i14 = iArr[i10];
            if (i14 == 0) {
                iArr[i13] = 0;
                return;
            }
            if (i14 < 0) {
                iArr[i13] = -1;
            } else {
                int i15 = i14 - 1;
                if (((C(this.f15928e[i15]) - i10) & (y() - 1)) >= i12) {
                    this.f15931h[i13] = i14;
                    this.f15930g[i15] = i13;
                }
                i11--;
            }
            i13 = i10;
            i12 = 0;
            i11--;
        } while (i11 >= 0);
        this.f15931h[i13] = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void M(int i10) {
        na.c.f(this.f15928e, i10);
        K(this.f15930g[i10]);
        this.f15930g[i10] = -1;
        this.f15935l = size() - 1;
    }

    private final boolean O(int i10) {
        int w10 = w();
        int i11 = this.f15933j;
        int i12 = w10 - i11;
        int size = i11 - size();
        return i12 < i10 && i12 + size >= i10 && size >= w() / 4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final V[] k() {
        V[] vArr = this.f15929f;
        if (vArr != null) {
            return vArr;
        }
        V[] vArr2 = (V[]) na.c.d(w());
        this.f15929f = vArr2;
        return vArr2;
    }

    private final void n() {
        int i10;
        V[] vArr = this.f15929f;
        int i11 = 0;
        int i12 = 0;
        while (true) {
            i10 = this.f15933j;
            if (i11 >= i10) {
                break;
            }
            if (this.f15930g[i11] >= 0) {
                K[] kArr = this.f15928e;
                kArr[i12] = kArr[i11];
                if (vArr != null) {
                    vArr[i12] = vArr[i11];
                }
                i12++;
            }
            i11++;
        }
        na.c.g(this.f15928e, i12, i10);
        if (vArr != null) {
            na.c.g(vArr, i12, this.f15933j);
        }
        this.f15933j = i12;
    }

    private final boolean q(Map<?, ?> map) {
        return size() == map.size() && o(map.entrySet());
    }

    private final void r(int i10) {
        if (i10 >= 0) {
            if (i10 > w()) {
                int w10 = (w() * 3) / 2;
                if (i10 <= w10) {
                    i10 = w10;
                }
                this.f15928e = (K[]) na.c.e(this.f15928e, i10);
                V[] vArr = this.f15929f;
                this.f15929f = vArr != null ? (V[]) na.c.e(vArr, i10) : null;
                int[] copyOf = Arrays.copyOf(this.f15930g, i10);
                k.d(copyOf, "copyOf(this, newSize)");
                this.f15930g = copyOf;
                int c10 = f15927q.c(i10);
                if (c10 > y()) {
                    I(c10);
                    return;
                }
                return;
            }
            return;
        }
        throw new OutOfMemoryError();
    }

    private final void s(int i10) {
        if (O(i10)) {
            I(y());
        } else {
            r(this.f15933j + i10);
        }
    }

    private final int u(K k10) {
        int C = C(k10);
        int i10 = this.f15932i;
        while (true) {
            int i11 = this.f15931h[C];
            if (i11 == 0) {
                return -1;
            }
            if (i11 > 0) {
                int i12 = i11 - 1;
                if (k.a(this.f15928e[i12], k10)) {
                    return i12;
                }
            }
            i10--;
            if (i10 < 0) {
                return -1;
            }
            C = C == 0 ? y() - 1 : C - 1;
        }
    }

    private final int v(V v7) {
        int i10 = this.f15933j;
        while (true) {
            i10--;
            if (i10 < 0) {
                return -1;
            }
            if (this.f15930g[i10] >= 0) {
                V[] vArr = this.f15929f;
                k.b(vArr);
                if (k.a(vArr[i10], v7)) {
                    return i10;
                }
            }
        }
    }

    private final Object writeReplace() {
        if (this.f15939p) {
            return new i(this);
        }
        throw new NotSerializableException("The map cannot be serialized while it is being built.");
    }

    private final int y() {
        return this.f15931h.length;
    }

    public int A() {
        return this.f15935l;
    }

    public Collection<V> B() {
        g<V> gVar = this.f15937n;
        if (gVar != null) {
            return gVar;
        }
        g<V> gVar2 = new g<>(this);
        this.f15937n = gVar2;
        return gVar2;
    }

    public final boolean D() {
        return this.f15939p;
    }

    public final e<K, V> E() {
        return new e<>(this);
    }

    public final boolean J(Map.Entry<? extends K, ? extends V> entry) {
        k.e(entry, "entry");
        m();
        int u7 = u(entry.getKey());
        if (u7 < 0) {
            return false;
        }
        V[] vArr = this.f15929f;
        k.b(vArr);
        if (!k.a(vArr[u7], entry.getValue())) {
            return false;
        }
        M(u7);
        return true;
    }

    public final int L(K k10) {
        m();
        int u7 = u(k10);
        if (u7 < 0) {
            return -1;
        }
        M(u7);
        return u7;
    }

    public final boolean N(V v7) {
        m();
        int v10 = v(v7);
        if (v10 < 0) {
            return false;
        }
        M(v10);
        return true;
    }

    public final f<K, V> P() {
        return new f<>(this);
    }

    @Override // java.util.Map
    public void clear() {
        m();
        PrimitiveIterators it = new PrimitiveRanges(0, this.f15933j - 1).iterator();
        while (it.hasNext()) {
            int b10 = it.b();
            int[] iArr = this.f15930g;
            int i10 = iArr[b10];
            if (i10 >= 0) {
                this.f15931h[i10] = 0;
                iArr[b10] = -1;
            }
        }
        na.c.g(this.f15928e, 0, this.f15933j);
        V[] vArr = this.f15929f;
        if (vArr != null) {
            na.c.g(vArr, 0, this.f15933j);
        }
        this.f15935l = 0;
        this.f15933j = 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return u(obj) >= 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return v(obj) >= 0;
    }

    @Override // java.util.Map
    public final /* bridge */ Set<Map.Entry<K, V>> entrySet() {
        return x();
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof Map) && q((Map) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public V get(Object obj) {
        int u7 = u(obj);
        if (u7 < 0) {
            return null;
        }
        V[] vArr = this.f15929f;
        k.b(vArr);
        return vArr[u7];
    }

    @Override // java.util.Map
    public int hashCode() {
        b<K, V> t7 = t();
        int i10 = 0;
        while (t7.hasNext()) {
            i10 += t7.l();
        }
        return i10;
    }

    public final int i(K k10) {
        int f10;
        m();
        while (true) {
            int C = C(k10);
            f10 = _Ranges.f(this.f15932i * 2, y() / 2);
            int i10 = 0;
            while (true) {
                int i11 = this.f15931h[C];
                if (i11 <= 0) {
                    if (this.f15933j >= w()) {
                        s(1);
                    } else {
                        int i12 = this.f15933j;
                        int i13 = i12 + 1;
                        this.f15933j = i13;
                        this.f15928e[i12] = k10;
                        this.f15930g[i12] = C;
                        this.f15931h[C] = i13;
                        this.f15935l = size() + 1;
                        if (i10 > this.f15932i) {
                            this.f15932i = i10;
                        }
                        return i12;
                    }
                } else {
                    if (k.a(this.f15928e[i11 - 1], k10)) {
                        return -i11;
                    }
                    i10++;
                    if (i10 > f10) {
                        I(y() * 2);
                        break;
                    }
                    C = C == 0 ? y() - 1 : C - 1;
                }
            }
        }
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.Map
    public final /* bridge */ Set<K> keySet() {
        return z();
    }

    public final Map<K, V> l() {
        m();
        this.f15939p = true;
        return this;
    }

    public final void m() {
        if (this.f15939p) {
            throw new UnsupportedOperationException();
        }
    }

    public final boolean o(Collection<?> collection) {
        k.e(collection, "m");
        for (Object obj : collection) {
            if (obj != null) {
                try {
                    if (!p((Map.Entry) obj)) {
                    }
                } catch (ClassCastException unused) {
                }
            }
            return false;
        }
        return true;
    }

    public final boolean p(Map.Entry<? extends K, ? extends V> entry) {
        k.e(entry, "entry");
        int u7 = u(entry.getKey());
        if (u7 < 0) {
            return false;
        }
        V[] vArr = this.f15929f;
        k.b(vArr);
        return k.a(vArr[u7], entry.getValue());
    }

    @Override // java.util.Map
    public V put(K k10, V v7) {
        m();
        int i10 = i(k10);
        V[] k11 = k();
        if (i10 < 0) {
            int i11 = (-i10) - 1;
            V v10 = k11[i11];
            k11[i11] = v7;
            return v10;
        }
        k11[i10] = v7;
        return null;
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        k.e(map, "from");
        m();
        F(map.entrySet());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public V remove(Object obj) {
        int L = L(obj);
        if (L < 0) {
            return null;
        }
        V[] vArr = this.f15929f;
        k.b(vArr);
        V v7 = vArr[L];
        na.c.f(vArr, L);
        return v7;
    }

    @Override // java.util.Map
    public final /* bridge */ int size() {
        return A();
    }

    public final b<K, V> t() {
        return new b<>(this);
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder((size() * 3) + 2);
        sb2.append("{");
        b<K, V> t7 = t();
        int i10 = 0;
        while (t7.hasNext()) {
            if (i10 > 0) {
                sb2.append(", ");
            }
            t7.k(sb2);
            i10++;
        }
        sb2.append("}");
        String sb3 = sb2.toString();
        k.d(sb3, "sb.toString()");
        return sb3;
    }

    @Override // java.util.Map
    public final /* bridge */ Collection<V> values() {
        return B();
    }

    public final int w() {
        return this.f15928e.length;
    }

    public Set<Map.Entry<K, V>> x() {
        na.e<K, V> eVar = this.f15938o;
        if (eVar != null) {
            return eVar;
        }
        na.e<K, V> eVar2 = new na.e<>(this);
        this.f15938o = eVar2;
        return eVar2;
    }

    public Set<K> z() {
        na.f<K> fVar = this.f15936m;
        if (fVar != null) {
            return fVar;
        }
        na.f<K> fVar2 = new na.f<>(this);
        this.f15936m = fVar2;
        return fVar2;
    }

    public d() {
        this(8);
    }

    public d(int i10) {
        this(na.c.d(i10), null, new int[i10], new int[f15927q.c(i10)], 2, 0);
    }
}
