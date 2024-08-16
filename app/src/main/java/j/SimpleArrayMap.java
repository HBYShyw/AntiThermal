package j;

import java.util.ConcurrentModificationException;
import java.util.Map;

/* compiled from: SimpleArrayMap.java */
/* renamed from: j.g, reason: use source file name */
/* loaded from: classes.dex */
public class SimpleArrayMap<K, V> {

    /* renamed from: h, reason: collision with root package name */
    static Object[] f12917h;

    /* renamed from: i, reason: collision with root package name */
    static int f12918i;

    /* renamed from: j, reason: collision with root package name */
    static Object[] f12919j;

    /* renamed from: k, reason: collision with root package name */
    static int f12920k;

    /* renamed from: e, reason: collision with root package name */
    int[] f12921e;

    /* renamed from: f, reason: collision with root package name */
    Object[] f12922f;

    /* renamed from: g, reason: collision with root package name */
    int f12923g;

    public SimpleArrayMap() {
        this.f12921e = ContainerHelpers.f12886a;
        this.f12922f = ContainerHelpers.f12888c;
        this.f12923g = 0;
    }

    private void a(int i10) {
        if (i10 == 8) {
            synchronized (SimpleArrayMap.class) {
                Object[] objArr = f12919j;
                if (objArr != null) {
                    this.f12922f = objArr;
                    f12919j = (Object[]) objArr[0];
                    this.f12921e = (int[]) objArr[1];
                    objArr[1] = null;
                    objArr[0] = null;
                    f12920k--;
                    return;
                }
            }
        } else if (i10 == 4) {
            synchronized (SimpleArrayMap.class) {
                Object[] objArr2 = f12917h;
                if (objArr2 != null) {
                    this.f12922f = objArr2;
                    f12917h = (Object[]) objArr2[0];
                    this.f12921e = (int[]) objArr2[1];
                    objArr2[1] = null;
                    objArr2[0] = null;
                    f12918i--;
                    return;
                }
            }
        }
        this.f12921e = new int[i10];
        this.f12922f = new Object[i10 << 1];
    }

    private static int b(int[] iArr, int i10, int i11) {
        try {
            return ContainerHelpers.a(iArr, i10, i11);
        } catch (ArrayIndexOutOfBoundsException unused) {
            throw new ConcurrentModificationException();
        }
    }

    private static void e(int[] iArr, Object[] objArr, int i10) {
        if (iArr.length == 8) {
            synchronized (SimpleArrayMap.class) {
                if (f12920k < 10) {
                    objArr[0] = f12919j;
                    objArr[1] = iArr;
                    for (int i11 = (i10 << 1) - 1; i11 >= 2; i11--) {
                        objArr[i11] = null;
                    }
                    f12919j = objArr;
                    f12920k++;
                }
            }
            return;
        }
        if (iArr.length == 4) {
            synchronized (SimpleArrayMap.class) {
                if (f12918i < 10) {
                    objArr[0] = f12917h;
                    objArr[1] = iArr;
                    for (int i12 = (i10 << 1) - 1; i12 >= 2; i12--) {
                        objArr[i12] = null;
                    }
                    f12917h = objArr;
                    f12918i++;
                }
            }
        }
    }

    public void clear() {
        int i10 = this.f12923g;
        if (i10 > 0) {
            int[] iArr = this.f12921e;
            Object[] objArr = this.f12922f;
            this.f12921e = ContainerHelpers.f12886a;
            this.f12922f = ContainerHelpers.f12888c;
            this.f12923g = 0;
            e(iArr, objArr, i10);
        }
        if (this.f12923g > 0) {
            throw new ConcurrentModificationException();
        }
    }

    public boolean containsKey(Object obj) {
        return g(obj) >= 0;
    }

    public boolean containsValue(Object obj) {
        return i(obj) >= 0;
    }

    public void d(int i10) {
        int i11 = this.f12923g;
        int[] iArr = this.f12921e;
        if (iArr.length < i10) {
            Object[] objArr = this.f12922f;
            a(i10);
            if (this.f12923g > 0) {
                System.arraycopy(iArr, 0, this.f12921e, 0, i11);
                System.arraycopy(objArr, 0, this.f12922f, 0, i11 << 1);
            }
            e(iArr, objArr, i11);
        }
        if (this.f12923g != i11) {
            throw new ConcurrentModificationException();
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SimpleArrayMap) {
            SimpleArrayMap simpleArrayMap = (SimpleArrayMap) obj;
            if (size() != simpleArrayMap.size()) {
                return false;
            }
            for (int i10 = 0; i10 < this.f12923g; i10++) {
                try {
                    K j10 = j(i10);
                    V n10 = n(i10);
                    Object obj2 = simpleArrayMap.get(j10);
                    if (n10 == null) {
                        if (obj2 != null || !simpleArrayMap.containsKey(j10)) {
                            return false;
                        }
                    } else if (!n10.equals(obj2)) {
                        return false;
                    }
                } catch (ClassCastException | NullPointerException unused) {
                    return false;
                }
            }
            return true;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (size() != map.size()) {
                return false;
            }
            for (int i11 = 0; i11 < this.f12923g; i11++) {
                try {
                    K j11 = j(i11);
                    V n11 = n(i11);
                    Object obj3 = map.get(j11);
                    if (n11 == null) {
                        if (obj3 != null || !map.containsKey(j11)) {
                            return false;
                        }
                    } else if (!n11.equals(obj3)) {
                        return false;
                    }
                } catch (ClassCastException | NullPointerException unused2) {
                }
            }
            return true;
        }
        return false;
    }

    int f(Object obj, int i10) {
        int i11 = this.f12923g;
        if (i11 == 0) {
            return -1;
        }
        int b10 = b(this.f12921e, i11, i10);
        if (b10 < 0 || obj.equals(this.f12922f[b10 << 1])) {
            return b10;
        }
        int i12 = b10 + 1;
        while (i12 < i11 && this.f12921e[i12] == i10) {
            if (obj.equals(this.f12922f[i12 << 1])) {
                return i12;
            }
            i12++;
        }
        for (int i13 = b10 - 1; i13 >= 0 && this.f12921e[i13] == i10; i13--) {
            if (obj.equals(this.f12922f[i13 << 1])) {
                return i13;
            }
        }
        return ~i12;
    }

    public int g(Object obj) {
        return obj == null ? h() : f(obj, obj.hashCode());
    }

    public V get(Object obj) {
        return getOrDefault(obj, null);
    }

    public V getOrDefault(Object obj, V v7) {
        int g6 = g(obj);
        return g6 >= 0 ? (V) this.f12922f[(g6 << 1) + 1] : v7;
    }

    int h() {
        int i10 = this.f12923g;
        if (i10 == 0) {
            return -1;
        }
        int b10 = b(this.f12921e, i10, 0);
        if (b10 < 0 || this.f12922f[b10 << 1] == null) {
            return b10;
        }
        int i11 = b10 + 1;
        while (i11 < i10 && this.f12921e[i11] == 0) {
            if (this.f12922f[i11 << 1] == null) {
                return i11;
            }
            i11++;
        }
        for (int i12 = b10 - 1; i12 >= 0 && this.f12921e[i12] == 0; i12--) {
            if (this.f12922f[i12 << 1] == null) {
                return i12;
            }
        }
        return ~i11;
    }

    public int hashCode() {
        int[] iArr = this.f12921e;
        Object[] objArr = this.f12922f;
        int i10 = this.f12923g;
        int i11 = 1;
        int i12 = 0;
        int i13 = 0;
        while (i12 < i10) {
            Object obj = objArr[i11];
            i13 += (obj == null ? 0 : obj.hashCode()) ^ iArr[i12];
            i12++;
            i11 += 2;
        }
        return i13;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int i(Object obj) {
        int i10 = this.f12923g * 2;
        Object[] objArr = this.f12922f;
        if (obj == null) {
            for (int i11 = 1; i11 < i10; i11 += 2) {
                if (objArr[i11] == null) {
                    return i11 >> 1;
                }
            }
            return -1;
        }
        for (int i12 = 1; i12 < i10; i12 += 2) {
            if (obj.equals(objArr[i12])) {
                return i12 >> 1;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return this.f12923g <= 0;
    }

    public K j(int i10) {
        return (K) this.f12922f[i10 << 1];
    }

    public void k(SimpleArrayMap<? extends K, ? extends V> simpleArrayMap) {
        int i10 = simpleArrayMap.f12923g;
        d(this.f12923g + i10);
        if (this.f12923g != 0) {
            for (int i11 = 0; i11 < i10; i11++) {
                put(simpleArrayMap.j(i11), simpleArrayMap.n(i11));
            }
        } else if (i10 > 0) {
            System.arraycopy(simpleArrayMap.f12921e, 0, this.f12921e, 0, i10);
            System.arraycopy(simpleArrayMap.f12922f, 0, this.f12922f, 0, i10 << 1);
            this.f12923g = i10;
        }
    }

    public V l(int i10) {
        Object[] objArr = this.f12922f;
        int i11 = i10 << 1;
        V v7 = (V) objArr[i11 + 1];
        int i12 = this.f12923g;
        int i13 = 0;
        if (i12 <= 1) {
            e(this.f12921e, objArr, i12);
            this.f12921e = ContainerHelpers.f12886a;
            this.f12922f = ContainerHelpers.f12888c;
        } else {
            int i14 = i12 - 1;
            int[] iArr = this.f12921e;
            if (iArr.length > 8 && i12 < iArr.length / 3) {
                a(i12 > 8 ? i12 + (i12 >> 1) : 8);
                if (i12 != this.f12923g) {
                    throw new ConcurrentModificationException();
                }
                if (i10 > 0) {
                    System.arraycopy(iArr, 0, this.f12921e, 0, i10);
                    System.arraycopy(objArr, 0, this.f12922f, 0, i11);
                }
                if (i10 < i14) {
                    int i15 = i10 + 1;
                    int i16 = i14 - i10;
                    System.arraycopy(iArr, i15, this.f12921e, i10, i16);
                    System.arraycopy(objArr, i15 << 1, this.f12922f, i11, i16 << 1);
                }
            } else {
                if (i10 < i14) {
                    int i17 = i10 + 1;
                    int i18 = i14 - i10;
                    System.arraycopy(iArr, i17, iArr, i10, i18);
                    Object[] objArr2 = this.f12922f;
                    System.arraycopy(objArr2, i17 << 1, objArr2, i11, i18 << 1);
                }
                Object[] objArr3 = this.f12922f;
                int i19 = i14 << 1;
                objArr3[i19] = null;
                objArr3[i19 + 1] = null;
            }
            i13 = i14;
        }
        if (i12 == this.f12923g) {
            this.f12923g = i13;
            return v7;
        }
        throw new ConcurrentModificationException();
    }

    public V m(int i10, V v7) {
        int i11 = (i10 << 1) + 1;
        Object[] objArr = this.f12922f;
        V v10 = (V) objArr[i11];
        objArr[i11] = v7;
        return v10;
    }

    public V n(int i10) {
        return (V) this.f12922f[(i10 << 1) + 1];
    }

    public V put(K k10, V v7) {
        int i10;
        int f10;
        int i11 = this.f12923g;
        if (k10 == null) {
            f10 = h();
            i10 = 0;
        } else {
            int hashCode = k10.hashCode();
            i10 = hashCode;
            f10 = f(k10, hashCode);
        }
        if (f10 >= 0) {
            int i12 = (f10 << 1) + 1;
            Object[] objArr = this.f12922f;
            V v10 = (V) objArr[i12];
            objArr[i12] = v7;
            return v10;
        }
        int i13 = ~f10;
        int[] iArr = this.f12921e;
        if (i11 >= iArr.length) {
            int i14 = 4;
            if (i11 >= 8) {
                i14 = (i11 >> 1) + i11;
            } else if (i11 >= 4) {
                i14 = 8;
            }
            Object[] objArr2 = this.f12922f;
            a(i14);
            if (i11 == this.f12923g) {
                int[] iArr2 = this.f12921e;
                if (iArr2.length > 0) {
                    System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                    System.arraycopy(objArr2, 0, this.f12922f, 0, objArr2.length);
                }
                e(iArr, objArr2, i11);
            } else {
                throw new ConcurrentModificationException();
            }
        }
        if (i13 < i11) {
            int[] iArr3 = this.f12921e;
            int i15 = i13 + 1;
            System.arraycopy(iArr3, i13, iArr3, i15, i11 - i13);
            Object[] objArr3 = this.f12922f;
            System.arraycopy(objArr3, i13 << 1, objArr3, i15 << 1, (this.f12923g - i13) << 1);
        }
        int i16 = this.f12923g;
        if (i11 == i16) {
            int[] iArr4 = this.f12921e;
            if (i13 < iArr4.length) {
                iArr4[i13] = i10;
                Object[] objArr4 = this.f12922f;
                int i17 = i13 << 1;
                objArr4[i17] = k10;
                objArr4[i17 + 1] = v7;
                this.f12923g = i16 + 1;
                return null;
            }
        }
        throw new ConcurrentModificationException();
    }

    public V putIfAbsent(K k10, V v7) {
        V v10 = get(k10);
        return v10 == null ? put(k10, v7) : v10;
    }

    public V remove(Object obj) {
        int g6 = g(obj);
        if (g6 >= 0) {
            return l(g6);
        }
        return null;
    }

    public V replace(K k10, V v7) {
        int g6 = g(k10);
        if (g6 >= 0) {
            return m(g6, v7);
        }
        return null;
    }

    public int size() {
        return this.f12923g;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb2 = new StringBuilder(this.f12923g * 28);
        sb2.append('{');
        for (int i10 = 0; i10 < this.f12923g; i10++) {
            if (i10 > 0) {
                sb2.append(", ");
            }
            K j10 = j(i10);
            if (j10 != this) {
                sb2.append(j10);
            } else {
                sb2.append("(this Map)");
            }
            sb2.append('=');
            V n10 = n(i10);
            if (n10 != this) {
                sb2.append(n10);
            } else {
                sb2.append("(this Map)");
            }
        }
        sb2.append('}');
        return sb2.toString();
    }

    public boolean remove(Object obj, Object obj2) {
        int g6 = g(obj);
        if (g6 < 0) {
            return false;
        }
        V n10 = n(g6);
        if (obj2 != n10 && (obj2 == null || !obj2.equals(n10))) {
            return false;
        }
        l(g6);
        return true;
    }

    public boolean replace(K k10, V v7, V v10) {
        int g6 = g(k10);
        if (g6 < 0) {
            return false;
        }
        V n10 = n(g6);
        if (n10 != v7 && (v7 == null || !v7.equals(n10))) {
            return false;
        }
        m(g6, v10);
        return true;
    }

    public SimpleArrayMap(int i10) {
        if (i10 == 0) {
            this.f12921e = ContainerHelpers.f12886a;
            this.f12922f = ContainerHelpers.f12888c;
        } else {
            a(i10);
        }
        this.f12923g = 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public SimpleArrayMap(SimpleArrayMap<K, V> simpleArrayMap) {
        this();
        if (simpleArrayMap != 0) {
            k(simpleArrayMap);
        }
    }
}
