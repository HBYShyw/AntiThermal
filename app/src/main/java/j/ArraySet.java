package j;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* compiled from: ArraySet.java */
/* renamed from: j.b, reason: use source file name */
/* loaded from: classes.dex */
public final class ArraySet<E> implements Collection<E>, Set<E> {

    /* renamed from: i, reason: collision with root package name */
    private static final int[] f12875i = new int[0];

    /* renamed from: j, reason: collision with root package name */
    private static final Object[] f12876j = new Object[0];

    /* renamed from: k, reason: collision with root package name */
    private static Object[] f12877k;

    /* renamed from: l, reason: collision with root package name */
    private static int f12878l;

    /* renamed from: m, reason: collision with root package name */
    private static Object[] f12879m;

    /* renamed from: n, reason: collision with root package name */
    private static int f12880n;

    /* renamed from: e, reason: collision with root package name */
    private int[] f12881e;

    /* renamed from: f, reason: collision with root package name */
    Object[] f12882f;

    /* renamed from: g, reason: collision with root package name */
    int f12883g;

    /* renamed from: h, reason: collision with root package name */
    private MapCollections<E, E> f12884h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ArraySet.java */
    /* renamed from: j.b$a */
    /* loaded from: classes.dex */
    public class a extends MapCollections<E, E> {
        a() {
        }

        @Override // j.MapCollections
        protected void a() {
            ArraySet.this.clear();
        }

        @Override // j.MapCollections
        protected Object b(int i10, int i11) {
            return ArraySet.this.f12882f[i10];
        }

        @Override // j.MapCollections
        protected Map<E, E> c() {
            throw new UnsupportedOperationException("not a map");
        }

        @Override // j.MapCollections
        protected int d() {
            return ArraySet.this.f12883g;
        }

        @Override // j.MapCollections
        protected int e(Object obj) {
            return ArraySet.this.indexOf(obj);
        }

        @Override // j.MapCollections
        protected int f(Object obj) {
            return ArraySet.this.indexOf(obj);
        }

        @Override // j.MapCollections
        protected void g(E e10, E e11) {
            ArraySet.this.add(e10);
        }

        @Override // j.MapCollections
        protected void h(int i10) {
            ArraySet.this.i(i10);
        }

        @Override // j.MapCollections
        protected E i(int i10, E e10) {
            throw new UnsupportedOperationException("not a map");
        }
    }

    public ArraySet() {
        this(0);
    }

    private void c(int i10) {
        if (i10 == 8) {
            synchronized (ArraySet.class) {
                Object[] objArr = f12879m;
                if (objArr != null) {
                    this.f12882f = objArr;
                    f12879m = (Object[]) objArr[0];
                    this.f12881e = (int[]) objArr[1];
                    objArr[1] = null;
                    objArr[0] = null;
                    f12880n--;
                    return;
                }
            }
        } else if (i10 == 4) {
            synchronized (ArraySet.class) {
                Object[] objArr2 = f12877k;
                if (objArr2 != null) {
                    this.f12882f = objArr2;
                    f12877k = (Object[]) objArr2[0];
                    this.f12881e = (int[]) objArr2[1];
                    objArr2[1] = null;
                    objArr2[0] = null;
                    f12878l--;
                    return;
                }
            }
        }
        this.f12881e = new int[i10];
        this.f12882f = new Object[i10];
    }

    private static void e(int[] iArr, Object[] objArr, int i10) {
        if (iArr.length == 8) {
            synchronized (ArraySet.class) {
                if (f12880n < 10) {
                    objArr[0] = f12879m;
                    objArr[1] = iArr;
                    for (int i11 = i10 - 1; i11 >= 2; i11--) {
                        objArr[i11] = null;
                    }
                    f12879m = objArr;
                    f12880n++;
                }
            }
            return;
        }
        if (iArr.length == 4) {
            synchronized (ArraySet.class) {
                if (f12878l < 10) {
                    objArr[0] = f12877k;
                    objArr[1] = iArr;
                    for (int i12 = i10 - 1; i12 >= 2; i12--) {
                        objArr[i12] = null;
                    }
                    f12877k = objArr;
                    f12878l++;
                }
            }
        }
    }

    private MapCollections<E, E> f() {
        if (this.f12884h == null) {
            this.f12884h = new a();
        }
        return this.f12884h;
    }

    private int g(Object obj, int i10) {
        int i11 = this.f12883g;
        if (i11 == 0) {
            return -1;
        }
        int a10 = ContainerHelpers.a(this.f12881e, i11, i10);
        if (a10 < 0 || obj.equals(this.f12882f[a10])) {
            return a10;
        }
        int i12 = a10 + 1;
        while (i12 < i11 && this.f12881e[i12] == i10) {
            if (obj.equals(this.f12882f[i12])) {
                return i12;
            }
            i12++;
        }
        for (int i13 = a10 - 1; i13 >= 0 && this.f12881e[i13] == i10; i13--) {
            if (obj.equals(this.f12882f[i13])) {
                return i13;
            }
        }
        return ~i12;
    }

    private int h() {
        int i10 = this.f12883g;
        if (i10 == 0) {
            return -1;
        }
        int a10 = ContainerHelpers.a(this.f12881e, i10, 0);
        if (a10 < 0 || this.f12882f[a10] == null) {
            return a10;
        }
        int i11 = a10 + 1;
        while (i11 < i10 && this.f12881e[i11] == 0) {
            if (this.f12882f[i11] == null) {
                return i11;
            }
            i11++;
        }
        for (int i12 = a10 - 1; i12 >= 0 && this.f12881e[i12] == 0; i12--) {
            if (this.f12882f[i12] == null) {
                return i12;
            }
        }
        return ~i11;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean add(E e10) {
        int i10;
        int g6;
        if (e10 == null) {
            g6 = h();
            i10 = 0;
        } else {
            int hashCode = e10.hashCode();
            i10 = hashCode;
            g6 = g(e10, hashCode);
        }
        if (g6 >= 0) {
            return false;
        }
        int i11 = ~g6;
        int i12 = this.f12883g;
        int[] iArr = this.f12881e;
        if (i12 >= iArr.length) {
            int i13 = 4;
            if (i12 >= 8) {
                i13 = (i12 >> 1) + i12;
            } else if (i12 >= 4) {
                i13 = 8;
            }
            Object[] objArr = this.f12882f;
            c(i13);
            int[] iArr2 = this.f12881e;
            if (iArr2.length > 0) {
                System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                System.arraycopy(objArr, 0, this.f12882f, 0, objArr.length);
            }
            e(iArr, objArr, this.f12883g);
        }
        int i14 = this.f12883g;
        if (i11 < i14) {
            int[] iArr3 = this.f12881e;
            int i15 = i11 + 1;
            System.arraycopy(iArr3, i11, iArr3, i15, i14 - i11);
            Object[] objArr2 = this.f12882f;
            System.arraycopy(objArr2, i11, objArr2, i15, this.f12883g - i11);
        }
        this.f12881e[i11] = i10;
        this.f12882f[i11] = e10;
        this.f12883g++;
        return true;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends E> collection) {
        d(this.f12883g + collection.size());
        Iterator<? extends E> it = collection.iterator();
        boolean z10 = false;
        while (it.hasNext()) {
            z10 |= add(it.next());
        }
        return z10;
    }

    @Override // java.util.Collection, java.util.Set
    public void clear() {
        int i10 = this.f12883g;
        if (i10 != 0) {
            e(this.f12881e, this.f12882f, i10);
            this.f12881e = f12875i;
            this.f12882f = f12876j;
            this.f12883g = 0;
        }
    }

    @Override // java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    public void d(int i10) {
        int[] iArr = this.f12881e;
        if (iArr.length < i10) {
            Object[] objArr = this.f12882f;
            c(i10);
            int i11 = this.f12883g;
            if (i11 > 0) {
                System.arraycopy(iArr, 0, this.f12881e, 0, i11);
                System.arraycopy(objArr, 0, this.f12882f, 0, this.f12883g);
            }
            e(iArr, objArr, this.f12883g);
        }
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Set) {
            Set set = (Set) obj;
            if (size() != set.size()) {
                return false;
            }
            for (int i10 = 0; i10 < this.f12883g; i10++) {
                try {
                    if (!set.contains(k(i10))) {
                        return false;
                    }
                } catch (ClassCastException | NullPointerException unused) {
                }
            }
            return true;
        }
        return false;
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        int[] iArr = this.f12881e;
        int i10 = this.f12883g;
        int i11 = 0;
        for (int i12 = 0; i12 < i10; i12++) {
            i11 += iArr[i12];
        }
        return i11;
    }

    public E i(int i10) {
        Object[] objArr = this.f12882f;
        E e10 = (E) objArr[i10];
        int i11 = this.f12883g;
        if (i11 <= 1) {
            e(this.f12881e, objArr, i11);
            this.f12881e = f12875i;
            this.f12882f = f12876j;
            this.f12883g = 0;
        } else {
            int[] iArr = this.f12881e;
            if (iArr.length > 8 && i11 < iArr.length / 3) {
                c(i11 > 8 ? i11 + (i11 >> 1) : 8);
                this.f12883g--;
                if (i10 > 0) {
                    System.arraycopy(iArr, 0, this.f12881e, 0, i10);
                    System.arraycopy(objArr, 0, this.f12882f, 0, i10);
                }
                int i12 = this.f12883g;
                if (i10 < i12) {
                    int i13 = i10 + 1;
                    System.arraycopy(iArr, i13, this.f12881e, i10, i12 - i10);
                    System.arraycopy(objArr, i13, this.f12882f, i10, this.f12883g - i10);
                }
            } else {
                int i14 = i11 - 1;
                this.f12883g = i14;
                if (i10 < i14) {
                    int i15 = i10 + 1;
                    System.arraycopy(iArr, i15, iArr, i10, i14 - i10);
                    Object[] objArr2 = this.f12882f;
                    System.arraycopy(objArr2, i15, objArr2, i10, this.f12883g - i10);
                }
                this.f12882f[this.f12883g] = null;
            }
        }
        return e10;
    }

    public int indexOf(Object obj) {
        return obj == null ? h() : g(obj, obj.hashCode());
    }

    @Override // java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.f12883g <= 0;
    }

    @Override // java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return f().m().iterator();
    }

    public E k(int i10) {
        return (E) this.f12882f[i10];
    }

    @Override // java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        int indexOf = indexOf(obj);
        if (indexOf < 0) {
            return false;
        }
        i(indexOf);
        return true;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean removeAll(Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        boolean z10 = false;
        while (it.hasNext()) {
            z10 |= remove(it.next());
        }
        return z10;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean retainAll(Collection<?> collection) {
        boolean z10 = false;
        for (int i10 = this.f12883g - 1; i10 >= 0; i10--) {
            if (!collection.contains(this.f12882f[i10])) {
                i(i10);
                z10 = true;
            }
        }
        return z10;
    }

    @Override // java.util.Collection, java.util.Set
    public int size() {
        return this.f12883g;
    }

    @Override // java.util.Collection, java.util.Set
    public Object[] toArray() {
        int i10 = this.f12883g;
        Object[] objArr = new Object[i10];
        System.arraycopy(this.f12882f, 0, objArr, 0, i10);
        return objArr;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb2 = new StringBuilder(this.f12883g * 14);
        sb2.append('{');
        for (int i10 = 0; i10 < this.f12883g; i10++) {
            if (i10 > 0) {
                sb2.append(", ");
            }
            E k10 = k(i10);
            if (k10 != this) {
                sb2.append(k10);
            } else {
                sb2.append("(this Set)");
            }
        }
        sb2.append('}');
        return sb2.toString();
    }

    public ArraySet(int i10) {
        if (i10 == 0) {
            this.f12881e = f12875i;
            this.f12882f = f12876j;
        } else {
            c(i10);
        }
        this.f12883g = 0;
    }

    @Override // java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] tArr) {
        if (tArr.length < this.f12883g) {
            tArr = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), this.f12883g));
        }
        System.arraycopy(this.f12882f, 0, tArr, 0, this.f12883g);
        int length = tArr.length;
        int i10 = this.f12883g;
        if (length > i10) {
            tArr[i10] = null;
        }
        return tArr;
    }
}
