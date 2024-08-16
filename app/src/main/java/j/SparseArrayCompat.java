package j;

/* compiled from: SparseArrayCompat.java */
/* renamed from: j.h, reason: use source file name */
/* loaded from: classes.dex */
public class SparseArrayCompat<E> implements Cloneable {

    /* renamed from: i, reason: collision with root package name */
    private static final Object f12924i = new Object();

    /* renamed from: e, reason: collision with root package name */
    private boolean f12925e;

    /* renamed from: f, reason: collision with root package name */
    private int[] f12926f;

    /* renamed from: g, reason: collision with root package name */
    private Object[] f12927g;

    /* renamed from: h, reason: collision with root package name */
    private int f12928h;

    public SparseArrayCompat() {
        this(10);
    }

    private void d() {
        int i10 = this.f12928h;
        int[] iArr = this.f12926f;
        Object[] objArr = this.f12927g;
        int i11 = 0;
        for (int i12 = 0; i12 < i10; i12++) {
            Object obj = objArr[i12];
            if (obj != f12924i) {
                if (i12 != i11) {
                    iArr[i11] = iArr[i12];
                    objArr[i11] = obj;
                    objArr[i12] = null;
                }
                i11++;
            }
        }
        this.f12925e = false;
        this.f12928h = i11;
    }

    public void a(int i10, E e10) {
        int i11 = this.f12928h;
        if (i11 != 0 && i10 <= this.f12926f[i11 - 1]) {
            i(i10, e10);
            return;
        }
        if (this.f12925e && i11 >= this.f12926f.length) {
            d();
        }
        int i12 = this.f12928h;
        if (i12 >= this.f12926f.length) {
            int e11 = ContainerHelpers.e(i12 + 1);
            int[] iArr = new int[e11];
            Object[] objArr = new Object[e11];
            int[] iArr2 = this.f12926f;
            System.arraycopy(iArr2, 0, iArr, 0, iArr2.length);
            Object[] objArr2 = this.f12927g;
            System.arraycopy(objArr2, 0, objArr, 0, objArr2.length);
            this.f12926f = iArr;
            this.f12927g = objArr;
        }
        this.f12926f[i12] = i10;
        this.f12927g[i12] = e10;
        this.f12928h = i12 + 1;
    }

    public void b() {
        int i10 = this.f12928h;
        Object[] objArr = this.f12927g;
        for (int i11 = 0; i11 < i10; i11++) {
            objArr[i11] = null;
        }
        this.f12928h = 0;
        this.f12925e = false;
    }

    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public SparseArrayCompat<E> clone() {
        try {
            SparseArrayCompat<E> sparseArrayCompat = (SparseArrayCompat) super.clone();
            sparseArrayCompat.f12926f = (int[]) this.f12926f.clone();
            sparseArrayCompat.f12927g = (Object[]) this.f12927g.clone();
            return sparseArrayCompat;
        } catch (CloneNotSupportedException e10) {
            throw new AssertionError(e10);
        }
    }

    public E e(int i10) {
        return f(i10, null);
    }

    public E f(int i10, E e10) {
        int a10 = ContainerHelpers.a(this.f12926f, this.f12928h, i10);
        if (a10 >= 0) {
            Object[] objArr = this.f12927g;
            if (objArr[a10] != f12924i) {
                return (E) objArr[a10];
            }
        }
        return e10;
    }

    public int g(E e10) {
        if (this.f12925e) {
            d();
        }
        for (int i10 = 0; i10 < this.f12928h; i10++) {
            if (this.f12927g[i10] == e10) {
                return i10;
            }
        }
        return -1;
    }

    public int h(int i10) {
        if (this.f12925e) {
            d();
        }
        return this.f12926f[i10];
    }

    public void i(int i10, E e10) {
        int a10 = ContainerHelpers.a(this.f12926f, this.f12928h, i10);
        if (a10 >= 0) {
            this.f12927g[a10] = e10;
            return;
        }
        int i11 = ~a10;
        int i12 = this.f12928h;
        if (i11 < i12) {
            Object[] objArr = this.f12927g;
            if (objArr[i11] == f12924i) {
                this.f12926f[i11] = i10;
                objArr[i11] = e10;
                return;
            }
        }
        if (this.f12925e && i12 >= this.f12926f.length) {
            d();
            i11 = ~ContainerHelpers.a(this.f12926f, this.f12928h, i10);
        }
        int i13 = this.f12928h;
        if (i13 >= this.f12926f.length) {
            int e11 = ContainerHelpers.e(i13 + 1);
            int[] iArr = new int[e11];
            Object[] objArr2 = new Object[e11];
            int[] iArr2 = this.f12926f;
            System.arraycopy(iArr2, 0, iArr, 0, iArr2.length);
            Object[] objArr3 = this.f12927g;
            System.arraycopy(objArr3, 0, objArr2, 0, objArr3.length);
            this.f12926f = iArr;
            this.f12927g = objArr2;
        }
        int i14 = this.f12928h;
        if (i14 - i11 != 0) {
            int[] iArr3 = this.f12926f;
            int i15 = i11 + 1;
            System.arraycopy(iArr3, i11, iArr3, i15, i14 - i11);
            Object[] objArr4 = this.f12927g;
            System.arraycopy(objArr4, i11, objArr4, i15, this.f12928h - i11);
        }
        this.f12926f[i11] = i10;
        this.f12927g[i11] = e10;
        this.f12928h++;
    }

    public void j(int i10) {
        int a10 = ContainerHelpers.a(this.f12926f, this.f12928h, i10);
        if (a10 >= 0) {
            Object[] objArr = this.f12927g;
            Object obj = objArr[a10];
            Object obj2 = f12924i;
            if (obj != obj2) {
                objArr[a10] = obj2;
                this.f12925e = true;
            }
        }
    }

    public int k() {
        if (this.f12925e) {
            d();
        }
        return this.f12928h;
    }

    public E l(int i10) {
        if (this.f12925e) {
            d();
        }
        return (E) this.f12927g[i10];
    }

    public String toString() {
        if (k() <= 0) {
            return "{}";
        }
        StringBuilder sb2 = new StringBuilder(this.f12928h * 28);
        sb2.append('{');
        for (int i10 = 0; i10 < this.f12928h; i10++) {
            if (i10 > 0) {
                sb2.append(", ");
            }
            sb2.append(h(i10));
            sb2.append('=');
            E l10 = l(i10);
            if (l10 != this) {
                sb2.append(l10);
            } else {
                sb2.append("(this Map)");
            }
        }
        sb2.append('}');
        return sb2.toString();
    }

    public SparseArrayCompat(int i10) {
        this.f12925e = false;
        if (i10 == 0) {
            this.f12926f = ContainerHelpers.f12886a;
            this.f12927g = ContainerHelpers.f12888c;
        } else {
            int e10 = ContainerHelpers.e(i10);
            this.f12926f = new int[e10];
            this.f12927g = new Object[e10];
        }
    }
}
