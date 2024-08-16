package j;

/* compiled from: LongSparseArray.java */
/* renamed from: j.d, reason: use source file name */
/* loaded from: classes.dex */
public class LongSparseArray<E> implements Cloneable {

    /* renamed from: i, reason: collision with root package name */
    private static final Object f12889i = new Object();

    /* renamed from: e, reason: collision with root package name */
    private boolean f12890e;

    /* renamed from: f, reason: collision with root package name */
    private long[] f12891f;

    /* renamed from: g, reason: collision with root package name */
    private Object[] f12892g;

    /* renamed from: h, reason: collision with root package name */
    private int f12893h;

    public LongSparseArray() {
        this(10);
    }

    private void d() {
        int i10 = this.f12893h;
        long[] jArr = this.f12891f;
        Object[] objArr = this.f12892g;
        int i11 = 0;
        for (int i12 = 0; i12 < i10; i12++) {
            Object obj = objArr[i12];
            if (obj != f12889i) {
                if (i12 != i11) {
                    jArr[i11] = jArr[i12];
                    objArr[i11] = obj;
                    objArr[i12] = null;
                }
                i11++;
            }
        }
        this.f12890e = false;
        this.f12893h = i11;
    }

    public void a() {
        int i10 = this.f12893h;
        Object[] objArr = this.f12892g;
        for (int i11 = 0; i11 < i10; i11++) {
            objArr[i11] = null;
        }
        this.f12893h = 0;
        this.f12890e = false;
    }

    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public LongSparseArray<E> clone() {
        try {
            LongSparseArray<E> longSparseArray = (LongSparseArray) super.clone();
            longSparseArray.f12891f = (long[]) this.f12891f.clone();
            longSparseArray.f12892g = (Object[]) this.f12892g.clone();
            return longSparseArray;
        } catch (CloneNotSupportedException e10) {
            throw new AssertionError(e10);
        }
    }

    public boolean c(long j10) {
        return g(j10) >= 0;
    }

    public E e(long j10) {
        return f(j10, null);
    }

    public E f(long j10, E e10) {
        int b10 = ContainerHelpers.b(this.f12891f, this.f12893h, j10);
        if (b10 >= 0) {
            Object[] objArr = this.f12892g;
            if (objArr[b10] != f12889i) {
                return (E) objArr[b10];
            }
        }
        return e10;
    }

    public int g(long j10) {
        if (this.f12890e) {
            d();
        }
        return ContainerHelpers.b(this.f12891f, this.f12893h, j10);
    }

    public boolean h() {
        return n() == 0;
    }

    public long i(int i10) {
        if (this.f12890e) {
            d();
        }
        return this.f12891f[i10];
    }

    public void j(long j10, E e10) {
        int b10 = ContainerHelpers.b(this.f12891f, this.f12893h, j10);
        if (b10 >= 0) {
            this.f12892g[b10] = e10;
            return;
        }
        int i10 = ~b10;
        int i11 = this.f12893h;
        if (i10 < i11) {
            Object[] objArr = this.f12892g;
            if (objArr[i10] == f12889i) {
                this.f12891f[i10] = j10;
                objArr[i10] = e10;
                return;
            }
        }
        if (this.f12890e && i11 >= this.f12891f.length) {
            d();
            i10 = ~ContainerHelpers.b(this.f12891f, this.f12893h, j10);
        }
        int i12 = this.f12893h;
        if (i12 >= this.f12891f.length) {
            int f10 = ContainerHelpers.f(i12 + 1);
            long[] jArr = new long[f10];
            Object[] objArr2 = new Object[f10];
            long[] jArr2 = this.f12891f;
            System.arraycopy(jArr2, 0, jArr, 0, jArr2.length);
            Object[] objArr3 = this.f12892g;
            System.arraycopy(objArr3, 0, objArr2, 0, objArr3.length);
            this.f12891f = jArr;
            this.f12892g = objArr2;
        }
        int i13 = this.f12893h;
        if (i13 - i10 != 0) {
            long[] jArr3 = this.f12891f;
            int i14 = i10 + 1;
            System.arraycopy(jArr3, i10, jArr3, i14, i13 - i10);
            Object[] objArr4 = this.f12892g;
            System.arraycopy(objArr4, i10, objArr4, i14, this.f12893h - i10);
        }
        this.f12891f[i10] = j10;
        this.f12892g[i10] = e10;
        this.f12893h++;
    }

    public void k(long j10) {
        int b10 = ContainerHelpers.b(this.f12891f, this.f12893h, j10);
        if (b10 >= 0) {
            Object[] objArr = this.f12892g;
            Object obj = objArr[b10];
            Object obj2 = f12889i;
            if (obj != obj2) {
                objArr[b10] = obj2;
                this.f12890e = true;
            }
        }
    }

    public void l(int i10) {
        Object[] objArr = this.f12892g;
        Object obj = objArr[i10];
        Object obj2 = f12889i;
        if (obj != obj2) {
            objArr[i10] = obj2;
            this.f12890e = true;
        }
    }

    public int n() {
        if (this.f12890e) {
            d();
        }
        return this.f12893h;
    }

    public E o(int i10) {
        if (this.f12890e) {
            d();
        }
        return (E) this.f12892g[i10];
    }

    public String toString() {
        if (n() <= 0) {
            return "{}";
        }
        StringBuilder sb2 = new StringBuilder(this.f12893h * 28);
        sb2.append('{');
        for (int i10 = 0; i10 < this.f12893h; i10++) {
            if (i10 > 0) {
                sb2.append(", ");
            }
            sb2.append(i(i10));
            sb2.append('=');
            E o10 = o(i10);
            if (o10 != this) {
                sb2.append(o10);
            } else {
                sb2.append("(this Map)");
            }
        }
        sb2.append('}');
        return sb2.toString();
    }

    public LongSparseArray(int i10) {
        this.f12890e = false;
        if (i10 == 0) {
            this.f12891f = ContainerHelpers.f12887b;
            this.f12892g = ContainerHelpers.f12888c;
        } else {
            int f10 = ContainerHelpers.f(i10);
            this.f12891f = new long[f10];
            this.f12892g = new Object[f10];
        }
    }
}
